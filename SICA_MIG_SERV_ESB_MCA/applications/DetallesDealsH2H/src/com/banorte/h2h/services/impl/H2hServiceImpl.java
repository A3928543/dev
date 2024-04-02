package com.banorte.h2h.services.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.banorte.h2h.dao.EnlaceUsdDao;
import com.banorte.h2h.dao.ProductosFXDao;
import com.banorte.h2h.dao.SicaDao;
import com.banorte.h2h.dto.DetalleH2H;
import com.banorte.h2h.services.H2hService;
import com.banorte.h2h.services.MailH2HSender;
import com.banorte.h2h.vo.MensajeVO;

public class H2hServiceImpl implements H2hService 
{
	private static final Logger log = Logger.getLogger(H2hServiceImpl.class);
	
	private SicaDao sicaDao;
	private ProductosFXDao productosFxDao;
	private EnlaceUsdDao enlaceUsdDao;
	private MailH2HSender mailH2hSender;
	private MensajeVO mensaje;
	
	public void iniciarProcesamientoDetallesDeal()
	{
		long valorIndiceH2H = 0;
		String valorStringIndiceH2H = null;
		
		try
		{
			log.info("--> iniciarProcesamientoDetallesDeal(" + new Date() + "): Ha iniciado el procesamiento de " +
		             "los detalles de deal................");
			
			valorStringIndiceH2H = sicaDao.consultaParametro(SicaDao.PARAMETRO_INDICE_H2H);
			
			if(valorStringIndiceH2H != null && !"".equals(valorStringIndiceH2H))
			{
				valorIndiceH2H = Long.parseLong(valorStringIndiceH2H);
				enviarDetallesDealH2H(valorIndiceH2H);
				consultarFoliosBanxico(valorIndiceH2H);
				solicitarCancelacionDetalles(valorIndiceH2H);
			}
			else
			{
				log.warn("--> iniciarProcesamientoDetallesDeal(" + new Date() + "): "+
						 "El valor del indice de h2h configurado no es valido y es requerido para indicar " +
						 "los registros que SICA envia.");
			}
		}
		catch(NumberFormatException e)
		{
			log.error("--> iniciarProcesamientoDetallesDeal(" + new Date() + "): "+
					 "El valor del indice de h2h debe ser NUMERICO.", e);
		}
		catch(Exception e)
		{
			log.error("--> iniciarProcesamientoDetallesDeal(" + new Date() + "): Ocurrio un error al procesar los detalles de deal.", e);
		}
		
		log.info("--> iniciarProcesamientoDetallesDeal(" + new Date() + "): Ha finalizado el procesamiento de " +
	             "los detalles de deal.");
	}
	
	private void solicitarCancelacionDetalles(long valorIndiceH2H)
	{
		log.info("--> solicitarCancelacionDetalles(" + new Date() + "): Inicia servicio para consultar " +
				 "los detalles de deal que deben ser cancelados ante Banxico.");
		
		List<DetalleH2H> detallesH2H = sicaDao.consultarDetallesPorCancelar();
		List<DetalleH2H> detallesCancelados = null;
		
		if(detallesH2H != null && !detallesH2H.isEmpty())
		{
			log.info("--> solicitarCancelacionDetalles(" + new Date() + "): Se encontraron " + detallesH2H.size() +
					 " detalles de deal para que se solicite su cancelacion, los detalles son: ");
			
			detallesCancelados = new ArrayList<DetalleH2H>();
			
			fijarIndiceH2HPosicion(detallesH2H, valorIndiceH2H);
			
			for(DetalleH2H d : 
)
			{
				try
				{
					d.setVersion(SicaDao.VERSION_CANCELAR);
					d.setStatusH2H(SicaDao.STATUSH2H_CANCELAR);
					d.setEnviada(SicaDao.DETALLE_NO_ENVIADO_H2H);
					
					productosFxDao.guardarDetalleH2H(d);
					if(d.isProcesadoOk())
					{
						log.warn("El detalle " + d.getIdDealPosicion().longValue() + 
								 " del deal " + d.getIdDeal().longValue() +
								 " ha sido insertado en PRODUCTOS FX para realizar la cancelacion.");
						detallesCancelados.add(d);
					}
					
				}
				catch(Exception e)
				{
					log.warn("El detalle " + d.getIdDealPosicion().longValue() + 
							 " del deal " + d.getIdDeal().longValue() +
							 " no fue insertado en PRODUCTOS FX para realizar la cancelacion.");
				}
			}
			
			if(!detallesCancelados.isEmpty())
			{
				actualizarDetallesSica(detallesCancelados, SicaDao.OPCION_CANCELAR_DETALLE);
				log.warn("Se han actualizado en SICA los detalles de deal que " +
						 "tenian solicitud de cancelacion y que fueron insertados correctamente en" +
						 "PRODUCTOS FX para realizar la cancelacion.");
			}
		}
		else
		{
			log.info("--> solicitarCancelacionDetalles(" + new Date() + "): No se encontraron " +
					 "detalles de deal en SICA que deban ser cancelados ante Banxico.");
		}
		
		log.info("--> solicitarCancelacionDetalles(" + new Date() + "): Ha finalizado el servicio " +
				 "que envia las cancelaciones ante Banxico.");
	}
	
	private void consultarFoliosBanxico(long valorIndiceH2H)
	{
		log.info("--> consultarFoliosBanxico(" + new Date() + "): Inicia servicio para consultar los folios banxico" +
				 " de los detalles deal que han sido reportados en el dia en curso.");
		
		List<DetalleH2H> detallesH2H = sicaDao.consultarFoliosBanxicoDetallesDealEnviadosH2H();
		List<DetalleH2H> detallesEncontrados = null;
		
		if(detallesH2H != null && !detallesH2H.isEmpty())
		{
			log.info("--> consultarFoliosBanxico(" + new Date() + "): Se encontraron " + detallesH2H.size() + 
					 " detalles de deal para consultar su folio banxico, los detalles son:");
			
			fijarIndiceH2HPosicion(detallesH2H, valorIndiceH2H);
			detallesEncontrados = enlaceUsdDao.consultarFoliosBanxicoDetallesSica(detallesH2H);
			
			if(detallesEncontrados != null && !detallesEncontrados.isEmpty())
			{
				log.info("--> consultarFoliosBanxico(" + new Date() + "): Se encontraron " + detallesEncontrados.size() + 
						 " folios banxico para los siguientes detalles de deal: ");
				
				for(DetalleH2H d : detallesEncontrados)
				{
					log.info("Detalle del Enlace: " + d.getIdDealPosicion().longValue() + 
							 ", indice: " + valorIndiceH2H); // + ", resultado: " + (d.getIdDealPosicion().longValue() - valorIndiceH2H));
					//d.setIdDealPosicion(d.getIdDealPosicion().longValue() - valorIndiceH2H);
					log.info("Detalle: " + d.getIdDealPosicion().longValue() + 
							 ", Deal: " + d.getIdDeal() + 
							 ", folio banxico: " + d.getFolioBanxico());
				}
				
				log.info("--> consultarFoliosBanxico(" + new Date() + "): Inicia la actualizacion de los " +
						 "folios banxico en SICA para los detalles de deal encontrados previamente.");
				actualizarDetallesSica(detallesEncontrados, SicaDao.OPCION_FOLIO_BANXICO);
				log.info("--> consultarFoliosBanxico(" + new Date() + "): ha finalizado la actualizacion de los " +
						 "folios banxico en SICA.");
			}
			else
			{
				log.info("--> consultarFoliosBanxico(" + new Date() + "): No se encontraron los folios banxico para " +
						 " detalles de deal mencionados.");
			}
		}
		else
		{
			log.info("--> consultarFoliosBanxico(" + new Date() + "): No se encontraron detalles de deal en SICA " +
					 "que esten en espera de que se consulte su folio banxico.");
		}
		
		log.info("--> consultarFoliosBanxico(" + new Date() + "): Ha finalizado el servicio para consultar los folios banxico.");
			
	}
	
	private void enviarDetallesDealH2H(long valorIndiceH2H)
	{
		log.info("--> iniciarProcesamientoDetallesDeal(" + new Date() + 
				 "): Inicia servicio para consultar y registrar los detalles de deal " +
				 "que no se han reportado a H2H....");
		
		List<DetalleH2H> detallesH2H = sicaDao.consultarNuevosDetallesDealEnviarH2H();
		
		if(detallesH2H != null && !detallesH2H.isEmpty())
		{
			log.info("--> Se encontraron " + detallesH2H.size() + " detalles de deal para reportar a H2H");
			
			fijarIndiceH2HPosicion(detallesH2H, valorIndiceH2H);
			
			log.info("--> inicia el registro de los detalles de deal en 'productos fx'...");
			guardarDetallesH2HProductosFX(detallesH2H);
			log.info("--> ha finalizado el registro de los detalles de deal en 'productos fx'...");
			
			log.info("--> inicia la actualizacion de los detalles de deal en 'SICA' para indicar que han sido enviados a H2H");
			actualizarDetallesSica(detallesH2H, SicaDao.OPCION_DETALLE_REPORTADO);
			log.info("--> ha finalizado la actualizacion de los detalles de deal en 'SICA'");
		}
		else
		{
			log.warn("--> iniciarProcesamientoDetallesDeal(" + new Date() + "): " +
					 "No se encontraron detalles de deals que deban enviarse al H2H.");
		}
		
		log.info("--> iniciarProcesamientoDetallesDeal(" + new Date() + 
				 "): Ha finalizado el procesamiento del servicio para consultar y registrar los detalles de deal " +
				 "que no se han reportado a H2H....");
	}
	
	private void actualizarDetallesSica(List<DetalleH2H> detallesH2H, int opcion)
	{
		try
		{
			sicaDao.actualizarDetallesSica(detallesH2H, opcion);
		}
		catch(Exception e)
		{
			log.error("--> actualizarDetallesSica(" + new Date() + "): Ocurrio un error al tratar de actualizar los detalles de liquidacion en SICA.");
		}
	}
	
	private void guardarDetallesH2HProductosFX(List<DetalleH2H> detallesH2H)
	{
		DetalleH2H detalleH2H = null;
		
		for(int indice = 0; indice < detallesH2H.size(); indice++)
		{
			try
			{
				detalleH2H = detallesH2H.get(indice);
				productosFxDao.guardarDetalleH2H(detalleH2H);
				log.info("El detalle " + detalleH2H.getIdDealPosicion().longValue() + 
						 " del deal " + detalleH2H.getIdDeal().longValue() +
						 " fue guardado en PRODUCTOS FX.");
			}
			catch(Exception e)
			{
				log.error("Ocurrio un error al tratar de guardar el detalle " + detalleH2H.getIdDealPosicion().longValue() + 
						 " del deal " + detalleH2H.getIdDeal().longValue() + " en PRODUCTOS FX.", e);
			}
		}
	}
	
	private void fijarIndiceH2HPosicion(List<DetalleH2H> detallesH2H, long valorIndiceH2H)
	{
		for(int indice = 0; indice < detallesH2H.size(); indice++)
		{
			detallesH2H.get(indice).setIndiceH2H(valorIndiceH2H);
			log.info("Detalle " + detallesH2H.get(indice).getIdDealPosicion().longValue() + ", " +
					 "Deal: " + detallesH2H.get(indice).getIdDeal().longValue() + 
					 (detallesH2H.get(indice).getFolioBanxico() != null ? 
					   ", Folio Banxico: " + detallesH2H.get(indice).getFolioBanxico() :
					   ""));
		}
	}
	
	public SicaDao getSicaDao() {
		return sicaDao;
	}
	public void setSicaDao(SicaDao sicaDao) {
		this.sicaDao = sicaDao;
	}
	public ProductosFXDao getProductosFxDao() {
		return productosFxDao;
	}
	public void setProductosFxDao(ProductosFXDao productosFxDao) {
		this.productosFxDao = productosFxDao;
	}
	public EnlaceUsdDao getEnlaceUsdDao() {
		return enlaceUsdDao;
	}
	public void setEnlaceUsdDao(EnlaceUsdDao enlaceUsdDao) {
		this.enlaceUsdDao = enlaceUsdDao;
	}
	public MailH2HSender getMailH2hSender() {
		return mailH2hSender;
	}
	public void setMailH2hSender(MailH2HSender mailH2hSender) {
		this.mailH2hSender = mailH2hSender;
	}
	public MensajeVO getMensaje() {
		return mensaje;
	}
	public void setMensaje(MensajeVO mensaje) {
		this.mensaje = mensaje;
	}
}
