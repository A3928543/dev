package com.banorte.reporte.mensual.rentabilidad.services.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.banorte.reporte.mensual.rentabilidad.dao.IConsultaDao;
import com.banorte.reporte.mensual.rentabilidad.dto.DatosOperacion;
import com.banorte.reporte.mensual.rentabilidad.dto.MensajesVo;
import com.banorte.reporte.mensual.rentabilidad.services.IReporteService;
import com.banorte.reporte.mensual.rentabilidad.services.ITransferenciasService;
import com.banorte.reporte.mensual.rentabilidad.services.mail.GeneralMailSender;
import com.banorte.reporte.mensual.rentabilidad.services.mail.MailSender;

public class TransferenciasServiceImpl implements ITransferenciasService {
	
	private static final Logger LOG = Logger.getLogger(TransferenciasServiceImpl.class);
	
	private IConsultaDao consultaDao;
	private MailSender mailSender;
	private MensajesVo mensajesVo;
	private IReporteService reporteService;
	private GeneralMailSender generalMailSender;

	public GeneralMailSender getGeneralMailSender() {
		return generalMailSender;
	}

	public void setGeneralMailSender(GeneralMailSender generalMailSender) {
		this.generalMailSender = generalMailSender;
	}

	@Override
	public void consultarReporteRentabilidad() 
	{

		List<DatosOperacion> operaciones = null;
		
		Map<String, Object> datos = null;
		
		String pathReporte = consultaDao.consultarParametro(IConsultaDao.PARAMETRO_PATH_REPORTE);
		String emailsTo = consultaDao.consultarParametro(IConsultaDao.PARAMETRO_EMAILS_TO);
		
		boolean seTienenEmailsDestinatarios = true;
		boolean realizarConsultaReporte = true;
		
		if(emailsTo == null || "".equals(emailsTo.trim()))
		{
			seTienenEmailsDestinatarios = false;
			LOG.warn("--> " + mensajesVo.getMensajeErrorEmailsTo());
		}
		
		if(pathReporte == null || "".equals(pathReporte.trim()))
		{
			LOG.error("--> " + mensajesVo.getMensajeErrorPath());
			
			datos = new HashMap<String, Object>();
			datos.put("mensaje", mensajesVo.getMensajeErrorPathEmail());
			enviarEmail(seTienenEmailsDestinatarios, emailsTo, datos);
		}
		else
		{
			try
			{
				datos = new HashMap<String, Object>();
				datos.put("mensaje", mensajesVo.getMensajeInicioproceso());
				enviarEmail(seTienenEmailsDestinatarios, emailsTo, datos);
			}
			catch(Exception e)
			{
				realizarConsultaReporte = false;
				datos = new HashMap<String, Object>();
				datos.put("mensaje", "Ha ocurrido un error Favor de informar a L.SoporteMercadosInterbancarios@banorte.com" + e.getLocalizedMessage());
				enviarEmail(seTienenEmailsDestinatarios, emailsTo, datos);
			}
			
			try
			{
				if(realizarConsultaReporte)
				{
					operaciones = consultaDao.consultarOperacionesReporte();

					if(operaciones != null && operaciones.size() > 0)
					{
						//SimpleDateFormat d = new SimpleDateFormat("yyyyMMdd");
						SimpleDateFormat d = new SimpleDateFormat("yyyyMM");
						
						// construir archivo reporte -- excel
						//ByteArrayOutputStream baos = reporteService.crearReporteExcel(transferencias);
						LOG.warn("Registros encontrados: " + operaciones.size());
						
			            
			            StringBuffer sb = new StringBuffer();
			            File f;
			            //f = new File(pathReporte + File.separator + d.format(new Date()) + "_enviadasTraExt" + ".txt");
			            f = new File(pathReporte + File.separator + "SICA_Cambios_" + d.format(new Date()) + ".txt");
				                   
			            FileWriter w = new FileWriter(f);
			            BufferedWriter bw = new BufferedWriter(w);
			            PrintWriter wr = new PrintWriter(bw);
			            for (DatosOperacion datosOperacion:operaciones) {
			            	sb.append(datosOperacion.getIdDeal());
			            	sb.append(datosOperacion.getIdCanal());
			            	sb.append(datosOperacion.getFolio());
			            	sb.append(datosOperacion.getFechaValor());
			            	sb.append(datosOperacion.getFechaCaptura());
			            	sb.append(datosOperacion.getFechaLiq());
			            	sb.append(datosOperacion.getMnemonico());
			            	sb.append(datosOperacion.getOperacion());
			            	sb.append(datosOperacion.getCliente().replace(";", ""));
			            	sb.append(datosOperacion.getProducto());
			            	sb.append(datosOperacion.getMonto());
			            	sb.append(datosOperacion.getIdDivisa());
			            	sb.append(datosOperacion.getTipoCambioCli());
			            	sb.append(datosOperacion.getTipoCambioMesa());
			            	sb.append(datosOperacion.getMontoEquiv());
			            	sb.append(datosOperacion.getUtilidad());
			            	sb.append(datosOperacion.getPromotor());
			            	sb.append(datosOperacion.getIdPromotor());
			            	sb.append(datosOperacion.getNombreUsuCaptura());
			            	sb.append(datosOperacion.getIdUsuCaptura());
			            	sb.append(datosOperacion.getClaveUsuCaptura());
			            	sb.append(datosOperacion.getContratoSica());
			            	sb.append(datosOperacion.getStatus());
			            	sb.append(datosOperacion.getMesa());
			            	sb.append(datosOperacion.getSectorEconomico().replace(";", "").concat(";"));
			            	sb.append(datosOperacion.getSectorBanxico().replace(";", ""));
			            	sb.append("\n");
						}
			            /*for (DatosOperacion datosOperacion : operaciones) {
			            	sb.append(datosTransferencia.getFecha().replaceAll("�","A").replaceAll("�","E").replaceAll("�","I").replaceAll("�","O").replaceAll("�","U").replaceAll("�","a").replaceAll("�","e").replaceAll("�","i").replaceAll("�","o").replaceAll("�","u").replaceAll("�","�").replaceAll("�","�").
		  							replaceAll("�","").replaceAll("�","").replaceAll(";"," ").replaceAll(":"," ").replaceAll("\\(","").replaceAll("\\)","").replaceAll("&","").replaceAll("/","").replaceAll("-","").replaceAll("N�","No").replaceAll("#","No").replaceAll("'","").replaceAll("�","No").replaceAll("`","").replaceAll("�","A").
		  							replaceAll("�","O").replaceAll("�","C").replaceAll("�","O").replaceAll("�","I").replaceAll("�"," ").replaceAll("�","").replaceAll("!","").replaceAll("$","").replaceAll("%","").replaceAll("=","").replaceAll("'","").replaceAll("�","").replaceAll("��","").replaceAll("��","").replaceAll("~","").replaceAll("\\{","").replaceAll("\\[","").
		  							replaceAll("^","").replaceAll("}","").replaceAll("]","").replaceAll("``","").replaceAll("�","A").replaceAll("�","a").replaceAll("�","A").replaceAll("�","a").replaceAll("�","A").replaceAll("�","a").replaceAll("�","A").replaceAll("�","a").replaceAll("�","E").replaceAll("�","e").replaceAll("�","E").replaceAll("�","e").
		  							replaceAll("�","I").replaceAll("�","i").replaceAll("�","O").replaceAll("�","o").replaceAll("�","O").replaceAll("�","o").replaceAll("�","O").replaceAll("�","o").replaceAll("�","O").replaceAll("�","U").replaceAll("�","u").replaceAll("\\\\","").replaceAll("\\*","").replaceAll("\\+"," ").replaceAll("\\\"","").replaceAll("NULL","").replace("?","�").replace("?","�"));
			            	sb.append(datosTransferencia.getReferencia().replaceAll("�","A").replaceAll("�","E").replaceAll("�","I").replaceAll("�","O").replaceAll("�","U").replaceAll("�","a").replaceAll("�","e").replaceAll("�","i").replaceAll("�","o").replaceAll("�","u").replaceAll("�","�").replaceAll("�","�").
		  							replaceAll("�","").replaceAll("�","").replaceAll(";"," ").replaceAll(":"," ").replaceAll("\\(","").replaceAll("\\)","").replaceAll("&","").replaceAll("/","").replaceAll("-","").replaceAll("N�","No").replaceAll("#","No").replaceAll("'","").replaceAll("�","No").replaceAll("`","").replaceAll("�","A").
		  							replaceAll("�","O").replaceAll("�","C").replaceAll("�","O").replaceAll("�","I").replaceAll("�"," ").replaceAll("�","").replaceAll("!","").replaceAll("$","").replaceAll("%","").replaceAll("=","").replaceAll("'","").replaceAll("�","").replaceAll("��","").replaceAll("��","").replaceAll("~","").replaceAll("\\{","").replaceAll("\\[","").
		  							replaceAll("^","").replaceAll("}","").replaceAll("]","").replaceAll("``","").replaceAll("�","A").replaceAll("�","a").replaceAll("�","A").replaceAll("�","a").replaceAll("�","A").replaceAll("�","a").replaceAll("�","A").replaceAll("�","a").replaceAll("�","E").replaceAll("�","e").replaceAll("�","E").replaceAll("�","e").
		  							replaceAll("�","I").replaceAll("�","i").replaceAll("�","O").replaceAll("�","o").replaceAll("�","O").replaceAll("�","o").replaceAll("�","O").replaceAll("�","o").replaceAll("�","O").replaceAll("�","U").replaceAll("�","u").replaceAll("\\\\","").replaceAll("\\*","").replaceAll("\\+"," ").replaceAll("\\\"","").replaceAll("NULL","").replace("?","�"));
			            	sb.append(datosTransferencia.getDeal().replaceAll("�","A").replaceAll("�","E").replaceAll("�","I").replaceAll("�","O").replaceAll("�","U").replaceAll("�","a").replaceAll("�","e").replaceAll("�","i").replaceAll("�","o").replaceAll("�","u").replaceAll("�","�").replaceAll("�","�").
		  							replaceAll("�","").replaceAll("�","").replaceAll(";"," ").replaceAll(":"," ").replaceAll("\\(","").replaceAll("\\)","").replaceAll("&","").replaceAll("/","").replaceAll("-","").replaceAll("N�","No").replaceAll("#","No").replaceAll("'","").replaceAll("�","No").replaceAll("`","").replaceAll("�","A").
		  							replaceAll("�","O").replaceAll("�","C").replaceAll("�","O").replaceAll("�","I").replaceAll("�"," ").replaceAll("�","").replaceAll("!","").replaceAll("$","").replaceAll("%","").replaceAll("=","").replaceAll("'","").replaceAll("�","").replaceAll("��","").replaceAll("��","").replaceAll("~","").replaceAll("\\{","").replaceAll("\\[","").
		  							replaceAll("^","").replaceAll("}","").replaceAll("]","").replaceAll("``","").replaceAll("�","A").replaceAll("�","a").replaceAll("�","A").replaceAll("�","a").replaceAll("�","A").replaceAll("�","a").replaceAll("�","A").replaceAll("�","a").replaceAll("�","E").replaceAll("�","e").replaceAll("�","E").replaceAll("�","e").
		  							replaceAll("�","I").replaceAll("�","i").replaceAll("�","O").replaceAll("�","o").replaceAll("�","O").replaceAll("�","o").replaceAll("�","O").replaceAll("�","o").replaceAll("�","O").replaceAll("�","U").replaceAll("�","u").replaceAll("\\\\","").replaceAll("\\*","").replaceAll("\\+"," ").replaceAll("\\\"","").replaceAll("NULL","").replace("?","�"));
			            	sb.append(datosTransferencia.getFolioDetalle().replaceAll("�","A").replaceAll("�","E").replaceAll("�","I").replaceAll("�","O").replaceAll("�","U").replaceAll("�","a").replaceAll("�","e").replaceAll("�","i").replaceAll("�","o").replaceAll("�","u").replaceAll("�","�").replaceAll("�","�").
		  							replaceAll("�","").replaceAll("�","").replaceAll(";"," ").replaceAll(":"," ").replaceAll("\\(","").replaceAll("\\)","").replaceAll("&","").replaceAll("/","").replaceAll("-","").replaceAll("N�","No").replaceAll("#","No").replaceAll("'","").replaceAll("�","No").replaceAll("`","").replaceAll("�","A").
		  							replaceAll("�","O").replaceAll("�","C").replaceAll("�","O").replaceAll("�","I").replaceAll("�"," ").replaceAll("�","").replaceAll("!","").replaceAll("$","").replaceAll("%","").replaceAll("=","").replaceAll("'","").replaceAll("�","").replaceAll("��","").replaceAll("��","").replaceAll("~","").replaceAll("\\{","").replaceAll("\\[","").
		  							replaceAll("^","").replaceAll("}","").replaceAll("]","").replaceAll("``","").replaceAll("�","A").replaceAll("�","a").replaceAll("�","A").replaceAll("�","a").replaceAll("�","A").replaceAll("�","a").replaceAll("�","A").replaceAll("�","a").replaceAll("�","E").replaceAll("�","e").replaceAll("�","E").replaceAll("�","e").
		  							replaceAll("�","I").replaceAll("�","i").replaceAll("�","O").replaceAll("�","o").replaceAll("�","O").replaceAll("�","o").replaceAll("�","O").replaceAll("�","o").replaceAll("�","O").replaceAll("�","U").replaceAll("�","u").replaceAll("\\\\","").replaceAll("\\*","").replaceAll("\\+"," ").replaceAll("\\\"","").replaceAll("NULL","").replace("?","�"));
			            	sb.append(datosTransferencia.getNombreCorresponsal().replaceAll("�","A").replaceAll("�","E").replaceAll("�","I").replaceAll("�","O").replaceAll("�","U").replaceAll("�","a").replaceAll("�","e").replaceAll("�","i").replaceAll("�","o").replaceAll("�","u").replaceAll("�","�").replaceAll("�","�").
		  							replaceAll("�","").replaceAll("�","").replaceAll(";"," ").replaceAll(":"," ").replaceAll("\\(","").replaceAll("\\)","").replaceAll("&","").replaceAll("/","").replaceAll("-","").replaceAll("N�","No").replaceAll("#","No").replaceAll("'","").replaceAll("�","No").replaceAll("`","").replaceAll("�","A").
		  							replaceAll("�","O").replaceAll("�","C").replaceAll("�","O").replaceAll("�","I").replaceAll("�"," ").replaceAll("�","").replaceAll("!","").replaceAll("$","").replaceAll("%","").replaceAll("=","").replaceAll("'","").replaceAll("�","").replaceAll("��","").replaceAll("��","").replaceAll("~","").replaceAll("\\{","").replaceAll("\\[","").
		  							replaceAll("^","").replaceAll("}","").replaceAll("]","").replaceAll("``","").replaceAll("�","A").replaceAll("�","a").replaceAll("�","A").replaceAll("�","a").replaceAll("�","A").replaceAll("�","a").replaceAll("�","A").replaceAll("�","a").replaceAll("�","E").replaceAll("�","e").replaceAll("�","E").replaceAll("�","e").
		  							replaceAll("�","I").replaceAll("�","i").replaceAll("�","O").replaceAll("�","o").replaceAll("�","O").replaceAll("�","o").replaceAll("�","O").replaceAll("�","o").replaceAll("�","O").replaceAll("�","U").replaceAll("�","u").replaceAll("\\\\","").replaceAll("\\*","").replaceAll("\\+"," ").replaceAll("\\\"","").replaceAll("NULL","").replace("?","�"));
			            	sb.append(datosTransferencia.getNombreEntidadDestino().replaceAll("�","A").replaceAll("�","E").replaceAll("�","I").replaceAll("�","O").replaceAll("�","U").replaceAll("�","a").replaceAll("�","e").replaceAll("�","i").replaceAll("�","o").replaceAll("�","u").replaceAll("�","�").replaceAll("�","�").
		  							replaceAll("�","").replaceAll("�","").replaceAll(";"," ").replaceAll(":"," ").replaceAll("\\(","").replaceAll("\\)","").replaceAll("&","").replaceAll("/","").replaceAll("-","").replaceAll("N�","No").replaceAll("#","No").replaceAll("'","").replaceAll("�","No").replaceAll("`","").replaceAll("�","A").
		  							replaceAll("�","O").replaceAll("�","C").replaceAll("�","O").replaceAll("�","I").replaceAll("�"," ").replaceAll("�","").replaceAll("!","").replaceAll("$","").replaceAll("%","").replaceAll("=","").replaceAll("'","").replaceAll("�","").replaceAll("��","").replaceAll("��","").replaceAll("~","").replaceAll("\\{","").replaceAll("\\[","").
		  							replaceAll("^","").replaceAll("}","").replaceAll("]","").replaceAll("``","").replaceAll("�","A").replaceAll("�","a").replaceAll("�","A").replaceAll("�","a").replaceAll("�","A").replaceAll("�","a").replaceAll("�","A").replaceAll("�","a").replaceAll("�","E").replaceAll("�","e").replaceAll("�","E").replaceAll("�","e").
		  							replaceAll("�","I").replaceAll("�","i").replaceAll("�","O").replaceAll("�","o").replaceAll("�","O").replaceAll("�","o").replaceAll("�","O").replaceAll("�","o").replaceAll("�","O").replaceAll("�","U").replaceAll("�","u").replaceAll("\\\\","").replaceAll("\\*","").replaceAll("\\+"," ").replaceAll("\\\"","").replaceAll("NULL","").replace("?","�"));
			            	sb.append(datosTransferencia.getBicAba().replaceAll("�","A").replaceAll("�","E").replaceAll("�","I").replaceAll("�","O").replaceAll("�","U").replaceAll("�","a").replaceAll("�","e").replaceAll("�","i").replaceAll("�","o").replaceAll("�","u").replaceAll("�","�").replaceAll("�","�").
		  							replaceAll("�","").replaceAll("�","").replaceAll(";"," ").replaceAll(":"," ").replaceAll("\\(","").replaceAll("\\)","").replaceAll("&","").replaceAll("/","").replaceAll("-","").replaceAll("N�","No").replaceAll("#","No").replaceAll("'","").replaceAll("�","No").replaceAll("`","").replaceAll("�","A").
		  							replaceAll("�","O").replaceAll("�","C").replaceAll("�","O").replaceAll("�","I").replaceAll("�"," ").replaceAll("�","").replaceAll("!","").replaceAll("$","").replaceAll("%","").replaceAll("=","").replaceAll("'","").replaceAll("�","").replaceAll("��","").replaceAll("��","").replaceAll("~","").replaceAll("\\{","").replaceAll("\\[","").
		  							replaceAll("^","").replaceAll("}","").replaceAll("]","").replaceAll("``","").replaceAll("�","A").replaceAll("�","a").replaceAll("�","A").replaceAll("�","a").replaceAll("�","A").replaceAll("�","a").replaceAll("�","A").replaceAll("�","a").replaceAll("�","E").replaceAll("�","e").replaceAll("�","E").replaceAll("�","e").
		  							replaceAll("�","I").replaceAll("�","i").replaceAll("�","O").replaceAll("�","o").replaceAll("�","O").replaceAll("�","o").replaceAll("�","O").replaceAll("�","o").replaceAll("�","O").replaceAll("�","U").replaceAll("�","u").replaceAll("\\\\","").replaceAll("\\*","").replaceAll("\\+"," ").replaceAll("\\\"","").replaceAll("NULL","").replace("?","�"));
			            	sb.append(datosTransferencia.getDatosDestinatario().replaceAll("�","A").replaceAll("�","E").replaceAll("�","I").replaceAll("�","O").replaceAll("�","U").replaceAll("�","a").replaceAll("�","e").replaceAll("�","i").replaceAll("�","o").replaceAll("�","u").replaceAll("�","�").replaceAll("�","�").
		  							replaceAll("�","").replaceAll("�","").replaceAll(";"," ").replaceAll(":"," ").replaceAll("\\(","").replaceAll("\\)","").replaceAll("&","").replaceAll("/","").replaceAll("-","").replaceAll("N�","No").replaceAll("#","No").replaceAll("'","").replaceAll("�","No").replaceAll("`","").replaceAll("�","A").
		  							replaceAll("�","O").replaceAll("�","C").replaceAll("�","O").replaceAll("�","I").replaceAll("�"," ").replaceAll("�","").replaceAll("!","").replaceAll("$","").replaceAll("%","").replaceAll("=","").replaceAll("'","").replaceAll("�","").replaceAll("��","").replaceAll("��","").replaceAll("~","").replaceAll("\\{","").replaceAll("\\[","").
		  							replaceAll("^","").replaceAll("}","").replaceAll("]","").replaceAll("``","").replaceAll("�","A").replaceAll("�","a").replaceAll("�","A").replaceAll("�","a").replaceAll("�","A").replaceAll("�","a").replaceAll("�","A").replaceAll("�","a").replaceAll("�","E").replaceAll("�","e").replaceAll("�","E").replaceAll("�","e").
		  							replaceAll("�","I").replaceAll("�","i").replaceAll("�","O").replaceAll("�","o").replaceAll("�","O").replaceAll("�","o").replaceAll("�","O").replaceAll("�","o").replaceAll("�","O").replaceAll("�","U").replaceAll("�","u").replaceAll("\\\\","").replaceAll("\\*","").replaceAll("\\+"," ").replaceAll("\\\"","").replaceAll("NULL","").replace("?","�"));
			            	sb.append(datosTransferencia.getOperacionOrigen().replaceAll("�","A").replaceAll("�","E").replaceAll("�","I").replaceAll("�","O").replaceAll("�","U").replaceAll("�","a").replaceAll("�","e").replaceAll("�","i").replaceAll("�","o").replaceAll("�","u").replaceAll("�","�").replaceAll("�","�").
		  							replaceAll("�","").replaceAll("�","").replaceAll(";"," ").replaceAll(":"," ").replaceAll("\\(","").replaceAll("\\)","").replaceAll("&","").replaceAll("/","").replaceAll("-","").replaceAll("N�","No").replaceAll("#","No").replaceAll("'","").replaceAll("�","No").replaceAll("`","").replaceAll("�","A").
		  							replaceAll("�","O").replaceAll("�","C").replaceAll("�","O").replaceAll("�","I").replaceAll("�"," ").replaceAll("�","").replaceAll("!","").replaceAll("$","").replaceAll("%","").replaceAll("=","").replaceAll("'","").replaceAll("�","").replaceAll("��","").replaceAll("��","").replaceAll("~","").replaceAll("\\{","").replaceAll("\\[","").
		  							replaceAll("^","").replaceAll("}","").replaceAll("]","").replaceAll("``","").replaceAll("�","A").replaceAll("�","a").replaceAll("�","A").replaceAll("�","a").replaceAll("�","A").replaceAll("�","a").replaceAll("�","A").replaceAll("�","a").replaceAll("�","E").replaceAll("�","e").replaceAll("�","E").replaceAll("�","e").
		  							replaceAll("�","I").replaceAll("�","i").replaceAll("�","O").replaceAll("�","o").replaceAll("�","O").replaceAll("�","o").replaceAll("�","O").replaceAll("�","o").replaceAll("�","O").replaceAll("�","U").replaceAll("�","u").replaceAll("\\\\","").replaceAll("\\*","").replaceAll("\\+"," ").replaceAll("\\\"","").replaceAll("NULL","").replace("?","�"));
			            	sb.append(datosTransferencia.getOperacionDestino().replaceAll("�","A").replaceAll("�","E").replaceAll("�","I").replaceAll("�","O").replaceAll("�","U").replaceAll("�","a").replaceAll("�","e").replaceAll("�","i").replaceAll("�","o").replaceAll("�","u").replaceAll("�","�").replaceAll("�","�").
		  							replaceAll("�","").replaceAll("�","").replaceAll(";"," ").replaceAll(":"," ").replaceAll("\\(","").replaceAll("\\)","").replaceAll("&","").replaceAll("/","").replaceAll("-","").replaceAll("N�","No").replaceAll("#","No").replaceAll("'","").replaceAll("�","No").replaceAll("`","").replaceAll("�","A").
		  							replaceAll("�","O").replaceAll("�","C").replaceAll("�","O").replaceAll("�","I").replaceAll("�"," ").replaceAll("�","").replaceAll("!","").replaceAll("$","").replaceAll("%","").replaceAll("=","").replaceAll("'","").replaceAll("�","").replaceAll("��","").replaceAll("��","").replaceAll("~","").replaceAll("\\{","").replaceAll("\\[","").
		  							replaceAll("^","").replaceAll("}","").replaceAll("]","").replaceAll("``","").replaceAll("�","A").replaceAll("�","a").replaceAll("�","A").replaceAll("�","a").replaceAll("�","A").replaceAll("�","a").replaceAll("�","A").replaceAll("�","a").replaceAll("�","E").replaceAll("�","e").replaceAll("�","E").replaceAll("�","e").
		  							replaceAll("�","I").replaceAll("�","i").replaceAll("�","O").replaceAll("�","o").replaceAll("�","O").replaceAll("�","o").replaceAll("�","O").replaceAll("�","o").replaceAll("�","O").replaceAll("�","U").replaceAll("�","u").replaceAll("\\\\","").replaceAll("\\*","").replaceAll("\\+"," ").replaceAll("\\\"","").replaceAll("NULL","").replace("?","�"));
			            	sb.append(datosTransferencia.getInstrumentoMonetarioOrigen().replaceAll("�","A").replaceAll("�","E").replaceAll("�","I").replaceAll("�","O").replaceAll("�","U").replaceAll("�","a").replaceAll("�","e").replaceAll("�","i").replaceAll("�","o").replaceAll("�","u").replaceAll("�","�").replaceAll("�","�").
		  							replaceAll("�","").replaceAll("�","").replaceAll(";"," ").replaceAll(":"," ").replaceAll("\\(","").replaceAll("\\)","").replaceAll("&","").replaceAll("/","").replaceAll("-","").replaceAll("N�","No").replaceAll("#","No").replaceAll("'","").replaceAll("�","No").replaceAll("`","").replaceAll("�","A").
		  							replaceAll("�","O").replaceAll("�","C").replaceAll("�","O").replaceAll("�","I").replaceAll("�"," ").replaceAll("�","").replaceAll("!","").replaceAll("$","").replaceAll("%","").replaceAll("=","").replaceAll("'","").replaceAll("�","").replaceAll("��","").replaceAll("��","").replaceAll("~","").replaceAll("\\{","").replaceAll("\\[","").
		  							replaceAll("^","").replaceAll("}","").replaceAll("]","").replaceAll("``","").replaceAll("�","A").replaceAll("�","a").replaceAll("�","A").replaceAll("�","a").replaceAll("�","A").replaceAll("�","a").replaceAll("�","A").replaceAll("�","a").replaceAll("�","E").replaceAll("�","e").replaceAll("�","E").replaceAll("�","e").
		  							replaceAll("�","I").replaceAll("�","i").replaceAll("�","O").replaceAll("�","o").replaceAll("�","O").replaceAll("�","o").replaceAll("�","O").replaceAll("�","o").replaceAll("�","O").replaceAll("�","U").replaceAll("�","u").replaceAll("\\\\","").replaceAll("\\*","").replaceAll("\\+"," ").replaceAll("\\\"","").replaceAll("NULL","").replace("?","�"));
			            	sb.append(datosTransferencia.getMonedaOrigen().replaceAll("�","A").replaceAll("�","E").replaceAll("�","I").replaceAll("�","O").replaceAll("�","U").replaceAll("�","a").replaceAll("�","e").replaceAll("�","i").replaceAll("�","o").replaceAll("�","u").replaceAll("�","�").replaceAll("�","�").
		  							replaceAll("�","").replaceAll("�","").replaceAll(";"," ").replaceAll(":"," ").replaceAll("\\(","").replaceAll("\\)","").replaceAll("&","").replaceAll("/","").replaceAll("-","").replaceAll("N�","No").replaceAll("#","No").replaceAll("'","").replaceAll("�","No").replaceAll("`","").replaceAll("�","A").
		  							replaceAll("�","O").replaceAll("�","C").replaceAll("�","O").replaceAll("�","I").replaceAll("�"," ").replaceAll("�","").replaceAll("!","").replaceAll("$","").replaceAll("%","").replaceAll("=","").replaceAll("'","").replaceAll("�","").replaceAll("��","").replaceAll("��","").replaceAll("~","").replaceAll("\\{","").replaceAll("\\[","").
		  							replaceAll("^","").replaceAll("}","").replaceAll("]","").replaceAll("``","").replaceAll("�","A").replaceAll("�","a").replaceAll("�","A").replaceAll("�","a").replaceAll("�","A").replaceAll("�","a").replaceAll("�","A").replaceAll("�","a").replaceAll("�","E").replaceAll("�","e").replaceAll("�","E").replaceAll("�","e").
		  							replaceAll("�","I").replaceAll("�","i").replaceAll("�","O").replaceAll("�","o").replaceAll("�","O").replaceAll("�","o").replaceAll("�","O").replaceAll("�","o").replaceAll("�","O").replaceAll("�","U").replaceAll("�","u").replaceAll("\\\\","").replaceAll("\\*","").replaceAll("\\+"," ").replaceAll("\\\"","").replaceAll("NULL","").replace("?","�"));
			            	sb.append(datosTransferencia.getMontoInstrumentoOrigen().replaceAll("�","A").replaceAll("�","E").replaceAll("�","I").replaceAll("�","O").replaceAll("�","U").replaceAll("�","a").replaceAll("�","e").replaceAll("�","i").replaceAll("�","o").replaceAll("�","u").replaceAll("�","�").replaceAll("�","�").
		  							replaceAll("�","").replaceAll("�","").replaceAll(";"," ").replaceAll(":"," ").replaceAll("\\(","").replaceAll("\\)","").replaceAll("&","").replaceAll("/","").replaceAll("-","").replaceAll("N�","No").replaceAll("#","No").replaceAll("'","").replaceAll("�","No").replaceAll("`","").replaceAll("�","A").
		  							replaceAll("�","O").replaceAll("�","C").replaceAll("�","O").replaceAll("�","I").replaceAll("�"," ").replaceAll("�","").replaceAll("!","").replaceAll("$","").replaceAll("%","").replaceAll("=","").replaceAll("'","").replaceAll("�","").replaceAll("��","").replaceAll("��","").replaceAll("~","").replaceAll("\\{","").replaceAll("\\[","").
		  							replaceAll("^","").replaceAll("}","").replaceAll("]","").replaceAll("``","").replaceAll("�","A").replaceAll("�","a").replaceAll("�","A").replaceAll("�","a").replaceAll("�","A").replaceAll("�","a").replaceAll("�","A").replaceAll("�","a").replaceAll("�","E").replaceAll("�","e").replaceAll("�","E").replaceAll("�","e").
		  							replaceAll("�","I").replaceAll("�","i").replaceAll("�","O").replaceAll("�","o").replaceAll("�","O").replaceAll("�","o").replaceAll("�","O").replaceAll("�","o").replaceAll("�","O").replaceAll("�","U").replaceAll("�","u").replaceAll("\\\\","").replaceAll("\\*","").replaceAll("\\+"," ").replaceAll("\\\"","").replaceAll("NULL","").replace("?","�"));
			            	sb.append(datosTransferencia.getMensaje().replaceAll("�","A").replaceAll("�","E").replaceAll("�","I").replaceAll("�","O").replaceAll("�","U").replaceAll("�","a").replaceAll("�","e").replaceAll("�","i").replaceAll("�","o").replaceAll("�","u").replaceAll("�","�").replaceAll("�","�").
		  							replaceAll("�","").replaceAll("�","").replaceAll(";"," ").replaceAll(":"," ").replaceAll("\\(","").replaceAll("\\)","").replaceAll("&","").replaceAll("/","").replaceAll("-","").replaceAll("N�","No").replaceAll("#","No").replaceAll("'","").replaceAll("�","No").replaceAll("`","").replaceAll("�","A").
		  							replaceAll("�","O").replaceAll("�","C").replaceAll("�","O").replaceAll("�","I").replaceAll("�"," ").replaceAll("�","").replaceAll("!","").replaceAll("$","").replaceAll("%","").replaceAll("=","").replaceAll("'","").replaceAll("�","").replaceAll("��","").replaceAll("��","").replaceAll("~","").replaceAll("\\{","").replaceAll("\\[","").
		  							replaceAll("^","").replaceAll("}","").replaceAll("]","").replaceAll("``","").replaceAll("�","A").replaceAll("�","a").replaceAll("�","A").replaceAll("�","a").replaceAll("�","A").replaceAll("�","a").replaceAll("�","A").replaceAll("�","a").replaceAll("�","E").replaceAll("�","e").replaceAll("�","E").replaceAll("�","e").
		  							replaceAll("�","I").replaceAll("�","i").replaceAll("�","O").replaceAll("�","o").replaceAll("�","O").replaceAll("�","o").replaceAll("�","O").replaceAll("�","o").replaceAll("�","O").replaceAll("�","U").replaceAll("�","u").replaceAll("\\\\","").replaceAll("\\*","").replaceAll("\\+"," ").replaceAll("\\\"","").replaceAll("NULL","").replace("?","�"));
			            	sb.append(datosTransferencia.getTipoPersona().replaceAll("�","A").replaceAll("�","E").replaceAll("�","I").replaceAll("�","O").replaceAll("�","U").replaceAll("�","a").replaceAll("�","e").replaceAll("�","i").replaceAll("�","o").replaceAll("�","u").replaceAll("�","�").replaceAll("�","�").
		  							replaceAll("�","").replaceAll("�","").replaceAll(";"," ").replaceAll(":"," ").replaceAll("\\(","").replaceAll("\\)","").replaceAll("&","").replaceAll("/","").replaceAll("-","").replaceAll("N�","No").replaceAll("#","No").replaceAll("'","").replaceAll("�","No").replaceAll("`","").replaceAll("�","A").
		  							replaceAll("�","O").replaceAll("�","C").replaceAll("�","O").replaceAll("�","I").replaceAll("�"," ").replaceAll("�","").replaceAll("!","").replaceAll("$","").replaceAll("%","").replaceAll("=","").replaceAll("'","").replaceAll("�","").replaceAll("��","").replaceAll("��","").replaceAll("~","").replaceAll("\\{","").replaceAll("\\[","").
		  							replaceAll("^","").replaceAll("}","").replaceAll("]","").replaceAll("``","").replaceAll("�","A").replaceAll("�","a").replaceAll("�","A").replaceAll("�","a").replaceAll("�","A").replaceAll("�","a").replaceAll("�","A").replaceAll("�","a").replaceAll("�","E").replaceAll("�","e").replaceAll("�","E").replaceAll("�","e").
		  							replaceAll("�","I").replaceAll("�","i").replaceAll("�","O").replaceAll("�","o").replaceAll("�","O").replaceAll("�","o").replaceAll("�","O").replaceAll("�","o").replaceAll("�","O").replaceAll("�","U").replaceAll("�","u").replaceAll("\\\\","").replaceAll("\\*","").replaceAll("\\+"," ").replaceAll("\\\"","").replaceAll("NULL","").replace("?","�"));
			            	sb.append(datosTransferencia.getNombre().replaceAll("�","A").replaceAll("�","E").replaceAll("�","I").replaceAll("�","O").replaceAll("�","U").replaceAll("�","a").replaceAll("�","e").replaceAll("�","i").replaceAll("�","o").replaceAll("�","u").replaceAll("�","�").replaceAll("�","�").
		  							replaceAll("�","").replaceAll("�","").replaceAll(";"," ").replaceAll(":"," ").replaceAll("\\(","").replaceAll("\\)","").replaceAll("&","").replaceAll("/","").replaceAll("-","").replaceAll("N�","No").replaceAll("#","No").replaceAll("'","").replaceAll("�","No").replaceAll("`","").replaceAll("�","A").
		  							replaceAll("�","O").replaceAll("�","C").replaceAll("�","O").replaceAll("�","I").replaceAll("�"," ").replaceAll("�","").replaceAll("!","").replaceAll("$","").replaceAll("%","").replaceAll("=","").replaceAll("'","").replaceAll("�","").replaceAll("��","").replaceAll("��","").replaceAll("~","").replaceAll("\\{","").replaceAll("\\[","").
		  							replaceAll("^","").replaceAll("}","").replaceAll("]","").replaceAll("``","").replaceAll("�","A").replaceAll("�","a").replaceAll("�","A").replaceAll("�","a").replaceAll("�","A").replaceAll("�","a").replaceAll("�","A").replaceAll("�","a").replaceAll("�","E").replaceAll("�","e").replaceAll("�","E").replaceAll("�","e").
		  							replaceAll("�","I").replaceAll("�","i").replaceAll("�","O").replaceAll("�","o").replaceAll("�","O").replaceAll("�","o").replaceAll("�","O").replaceAll("�","o").replaceAll("�","O").replaceAll("�","U").replaceAll("�","u").replaceAll("\\\\","").replaceAll("\\*","").replaceAll("\\+"," ").replaceAll("\\\"","").replaceAll("NULL","").replace("?","�"));
			            	sb.append(datosTransferencia.getApellidoPaterno().replaceAll("�","A").replaceAll("�","E").replaceAll("�","I").replaceAll("�","O").replaceAll("�","U").replaceAll("�","a").replaceAll("�","e").replaceAll("�","i").replaceAll("�","o").replaceAll("�","u").replaceAll("�","�").replaceAll("�","�").
		  							replaceAll("�","").replaceAll("�","").replaceAll(";"," ").replaceAll(":"," ").replaceAll("\\(","").replaceAll("\\)","").replaceAll("&","").replaceAll("/","").replaceAll("-","").replaceAll("N�","No").replaceAll("#","No").replaceAll("'","").replaceAll("�","No").replaceAll("`","").replaceAll("�","A").
		  							replaceAll("�","O").replaceAll("�","C").replaceAll("�","O").replaceAll("�","I").replaceAll("�"," ").replaceAll("�","").replaceAll("!","").replaceAll("$","").replaceAll("%","").replaceAll("=","").replaceAll("'","").replaceAll("�","").replaceAll("��","").replaceAll("��","").replaceAll("~","").replaceAll("\\{","").replaceAll("\\[","").
		  							replaceAll("^","").replaceAll("}","").replaceAll("]","").replaceAll("``","").replaceAll("�","A").replaceAll("�","a").replaceAll("�","A").replaceAll("�","a").replaceAll("�","A").replaceAll("�","a").replaceAll("�","A").replaceAll("�","a").replaceAll("�","E").replaceAll("�","e").replaceAll("�","E").replaceAll("�","e").
		  							replaceAll("�","I").replaceAll("�","i").replaceAll("�","O").replaceAll("�","o").replaceAll("�","O").replaceAll("�","o").replaceAll("�","O").replaceAll("�","o").replaceAll("�","O").replaceAll("�","U").replaceAll("�","u").replaceAll("\\\\","").replaceAll("\\*","").replaceAll("\\+"," ").replaceAll("\\\"","").replaceAll("NULL","").replace("?","�"));
			            	sb.append(datosTransferencia.getApellidoMaterno().replaceAll("�","A").replaceAll("�","E").replaceAll("�","I").replaceAll("�","O").replaceAll("�","U").replaceAll("�","a").replaceAll("�","e").replaceAll("�","i").replaceAll("�","o").replaceAll("�","u").replaceAll("�","�").replaceAll("�","�").
		  							replaceAll("�","").replaceAll("�","").replaceAll(";"," ").replaceAll(":"," ").replaceAll("\\(","").replaceAll("\\)","").replaceAll("&","").replaceAll("/","").replaceAll("-","").replaceAll("N�","No").replaceAll("#","No").replaceAll("'","").replaceAll("�","No").replaceAll("`","").replaceAll("�","A").
		  							replaceAll("�","O").replaceAll("�","C").replaceAll("�","O").replaceAll("�","I").replaceAll("�"," ").replaceAll("�","").replaceAll("!","").replaceAll("$","").replaceAll("%","").replaceAll("=","").replaceAll("'","").replaceAll("�","").replaceAll("��","").replaceAll("��","").replaceAll("~","").replaceAll("\\{","").replaceAll("\\[","").
		  							replaceAll("^","").replaceAll("}","").replaceAll("]","").replaceAll("``","").replaceAll("�","A").replaceAll("�","a").replaceAll("�","A").replaceAll("�","a").replaceAll("�","A").replaceAll("�","a").replaceAll("�","A").replaceAll("�","a").replaceAll("�","E").replaceAll("�","e").replaceAll("�","E").replaceAll("�","e").
		  							replaceAll("�","I").replaceAll("�","i").replaceAll("�","O").replaceAll("�","o").replaceAll("�","O").replaceAll("�","o").replaceAll("�","O").replaceAll("�","o").replaceAll("�","O").replaceAll("�","U").replaceAll("�","u").replaceAll("\\\\","").replaceAll("\\*","").replaceAll("\\+"," ").replaceAll("\\\"","").replaceAll("NULL","").replace("?","�"));
			            	sb.append(datosTransferencia.getFechaNacimiento().replaceAll("�","A").replaceAll("�","E").replaceAll("�","I").replaceAll("�","O").replaceAll("�","U").replaceAll("�","a").replaceAll("�","e").replaceAll("�","i").replaceAll("�","o").replaceAll("�","u").replaceAll("�","�").replaceAll("�","�").
		  							replaceAll("�","").replaceAll("�","").replaceAll(";"," ").replaceAll(":"," ").replaceAll("\\(","").replaceAll("\\)","").replaceAll("&","").replaceAll("/","").replaceAll("-","").replaceAll("N�","No").replaceAll("#","No").replaceAll("'","").replaceAll("�","No").replaceAll("`","").replaceAll("�","A").
		  							replaceAll("�","O").replaceAll("�","C").replaceAll("�","O").replaceAll("�","I").replaceAll("�"," ").replaceAll("�","").replaceAll("!","").replaceAll("$","").replaceAll("%","").replaceAll("=","").replaceAll("'","").replaceAll("�","").replaceAll("��","").replaceAll("��","").replaceAll("~","").replaceAll("\\{","").replaceAll("\\[","").
		  							replaceAll("^","").replaceAll("}","").replaceAll("]","").replaceAll("``","").replaceAll("�","A").replaceAll("�","a").replaceAll("�","A").replaceAll("�","a").replaceAll("�","A").replaceAll("�","a").replaceAll("�","A").replaceAll("�","a").replaceAll("�","E").replaceAll("�","e").replaceAll("�","E").replaceAll("�","e").
		  							replaceAll("�","I").replaceAll("�","i").replaceAll("�","O").replaceAll("�","o").replaceAll("�","O").replaceAll("�","o").replaceAll("�","O").replaceAll("�","o").replaceAll("�","O").replaceAll("�","U").replaceAll("�","u").replaceAll("\\\\","").replaceAll("\\*","").replaceAll("\\+"," ").replaceAll("\\\"","").replaceAll("NULL","").replace("?","�"));
			            	sb.append(datosTransferencia.getCurp().replaceAll("�","A").replaceAll("�","E").replaceAll("�","I").replaceAll("�","O").replaceAll("�","U").replaceAll("�","a").replaceAll("�","e").replaceAll("�","i").replaceAll("�","o").replaceAll("�","u").replaceAll("�","�").replaceAll("�","�").
		  							replaceAll("�","").replaceAll("�","").replaceAll(";"," ").replaceAll(":"," ").replaceAll("\\(","").replaceAll("\\)","").replaceAll("&","").replaceAll("/","").replaceAll("-","").replaceAll("N�","No").replaceAll("#","No").replaceAll("'","").replaceAll("�","No").replaceAll("`","").replaceAll("�","A").
		  							replaceAll("�","O").replaceAll("�","C").replaceAll("�","O").replaceAll("�","I").replaceAll("�"," ").replaceAll("�","").replaceAll("!","").replaceAll("$","").replaceAll("%","").replaceAll("=","").replaceAll("'","").replaceAll("�","").replaceAll("��","").replaceAll("��","").replaceAll("~","").replaceAll("\\{","").replaceAll("\\[","").
		  							replaceAll("^","").replaceAll("}","").replaceAll("]","").replaceAll("``","").replaceAll("�","A").replaceAll("�","a").replaceAll("�","A").replaceAll("�","a").replaceAll("�","A").replaceAll("�","a").replaceAll("�","A").replaceAll("�","a").replaceAll("�","E").replaceAll("�","e").replaceAll("�","E").replaceAll("�","e").
		  							replaceAll("�","I").replaceAll("�","i").replaceAll("�","O").replaceAll("�","o").replaceAll("�","O").replaceAll("�","o").replaceAll("�","O").replaceAll("�","o").replaceAll("�","O").replaceAll("�","U").replaceAll("�","u").replaceAll("\\\\","").replaceAll("\\*","").replaceAll("\\+"," ").replaceAll("\\\"","").replaceAll("NULL","").replace("?","�"));
			            	sb.append(datosTransferencia.getRfc().replaceAll("�","A").replaceAll("�","E").replaceAll("�","I").replaceAll("�","O").replaceAll("�","U").replaceAll("�","a").replaceAll("�","e").replaceAll("�","i").replaceAll("�","o").replaceAll("�","u").replaceAll("�","�").replaceAll("�","�").
		  							replaceAll("�","").replaceAll("�","").replaceAll(";"," ").replaceAll(":"," ").replaceAll("\\(","").replaceAll("\\)","").replaceAll("&","").replaceAll("/","").replaceAll("-","").replaceAll("N�","No").replaceAll("#","No").replaceAll("'","").replaceAll("�","No").replaceAll("`","").replaceAll("�","A").
		  							replaceAll("�","O").replaceAll("�","C").replaceAll("�","O").replaceAll("�","I").replaceAll("�"," ").replaceAll("�","").replaceAll("!","").replaceAll("$","").replaceAll("%","").replaceAll("=","").replaceAll("'","").replaceAll("�","").replaceAll("��","").replaceAll("��","").replaceAll("~","").replaceAll("\\{","").replaceAll("\\[","").
		  							replaceAll("^","").replaceAll("}","").replaceAll("]","").replaceAll("``","").replaceAll("�","A").replaceAll("�","a").replaceAll("�","A").replaceAll("�","a").replaceAll("�","A").replaceAll("�","a").replaceAll("�","A").replaceAll("�","a").replaceAll("�","E").replaceAll("�","e").replaceAll("�","E").replaceAll("�","e").
		  							replaceAll("�","I").replaceAll("�","i").replaceAll("�","O").replaceAll("�","o").replaceAll("�","O").replaceAll("�","o").replaceAll("�","O").replaceAll("�","o").replaceAll("�","O").replaceAll("�","U").replaceAll("�","u").replaceAll("\\\\","").replaceAll("\\*","").replaceAll("\\+"," ").replaceAll("\\\"","").replaceAll("NULL","").replace("?","�"));
			            	sb.append(datosTransferencia.getPaisNacimiento().replaceAll("�","A").replaceAll("�","E").replaceAll("�","I").replaceAll("�","O").replaceAll("�","U").replaceAll("�","a").replaceAll("�","e").replaceAll("�","i").replaceAll("�","o").replaceAll("�","u").replaceAll("�","�").replaceAll("�","�").
		  							replaceAll("�","").replaceAll("�","").replaceAll(";"," ").replaceAll(":"," ").replaceAll("\\(","").replaceAll("\\)","").replaceAll("&","").replaceAll("/","").replaceAll("-","").replaceAll("N�","No").replaceAll("#","No").replaceAll("'","").replaceAll("�","No").replaceAll("`","").replaceAll("�","A").
		  							replaceAll("�","O").replaceAll("�","C").replaceAll("�","O").replaceAll("�","I").replaceAll("�"," ").replaceAll("�","").replaceAll("!","").replaceAll("$","").replaceAll("%","").replaceAll("=","").replaceAll("'","").replaceAll("�","").replaceAll("��","").replaceAll("��","").replaceAll("~","").replaceAll("\\{","").replaceAll("\\[","").
		  							replaceAll("^","").replaceAll("}","").replaceAll("]","").replaceAll("``","").replaceAll("�","A").replaceAll("�","a").replaceAll("�","A").replaceAll("�","a").replaceAll("�","A").replaceAll("�","a").replaceAll("�","A").replaceAll("�","a").replaceAll("�","E").replaceAll("�","e").replaceAll("�","E").replaceAll("�","e").
		  							replaceAll("�","I").replaceAll("�","i").replaceAll("�","O").replaceAll("�","o").replaceAll("�","O").replaceAll("�","o").replaceAll("�","O").replaceAll("�","o").replaceAll("�","O").replaceAll("�","U").replaceAll("�","u").replaceAll("\\\\","").replaceAll("\\*","").replaceAll("\\+"," ").replaceAll("\\\"","").replaceAll("NULL","").replace("?","�"));
			            	sb.append(datosTransferencia.getPaisNacionalidad().replaceAll("�","A").replaceAll("�","E").replaceAll("�","I").replaceAll("�","O").replaceAll("�","U").replaceAll("�","a").replaceAll("�","e").replaceAll("�","i").replaceAll("�","o").replaceAll("�","u").replaceAll("�","�").replaceAll("�","�").
		  							replaceAll("�","").replaceAll("�","").replaceAll(";"," ").replaceAll(":"," ").replaceAll("\\(","").replaceAll("\\)","").replaceAll("&","").replaceAll("/","").replaceAll("-","").replaceAll("N�","No").replaceAll("#","No").replaceAll("'","").replaceAll("�","No").replaceAll("`","").replaceAll("�","A").
		  							replaceAll("�","O").replaceAll("�","C").replaceAll("�","O").replaceAll("�","I").replaceAll("�"," ").replaceAll("�","").replaceAll("!","").replaceAll("$","").replaceAll("%","").replaceAll("=","").replaceAll("'","").replaceAll("�","").replaceAll("��","").replaceAll("��","").replaceAll("~","").replaceAll("\\{","").replaceAll("\\[","").
		  							replaceAll("^","").replaceAll("}","").replaceAll("]","").replaceAll("``","").replaceAll("�","A").replaceAll("�","a").replaceAll("�","A").replaceAll("�","a").replaceAll("�","A").replaceAll("�","a").replaceAll("�","A").replaceAll("�","a").replaceAll("�","E").replaceAll("�","e").replaceAll("�","E").replaceAll("�","e").
		  							replaceAll("�","I").replaceAll("�","i").replaceAll("�","O").replaceAll("�","o").replaceAll("�","O").replaceAll("�","o").replaceAll("�","O").replaceAll("�","o").replaceAll("�","O").replaceAll("�","U").replaceAll("�","u").replaceAll("\\\\","").replaceAll("\\*","").replaceAll("\\+"," ").replaceAll("\\\"","").replaceAll("NULL","").replace("?","�"));
			            	sb.append(datosTransferencia.getActividadEconomica().replaceAll("�","A").replaceAll("�","E").replaceAll("�","I").replaceAll("�","O").replaceAll("�","U").replaceAll("�","a").replaceAll("�","e").replaceAll("�","i").replaceAll("�","o").replaceAll("�","u").replaceAll("�","�").replaceAll("�","�").
		  							replaceAll("�","").replaceAll("�","").replaceAll(";"," ").replaceAll(":"," ").replaceAll("\\(","").replaceAll("\\)","").replaceAll("&","").replaceAll("/","").replaceAll("-","").replaceAll("N�","No").replaceAll("#","No").replaceAll("'","").replaceAll("�","No").replaceAll("`","").replaceAll("�","A").
		  							replaceAll("�","O").replaceAll("�","C").replaceAll("�","O").replaceAll("�","I").replaceAll("�"," ").replaceAll("�","").replaceAll("!","").replaceAll("$","").replaceAll("%","").replaceAll("=","").replaceAll("'","").replaceAll("�","").replaceAll("��","").replaceAll("��","").replaceAll("~","").replaceAll("\\{","").replaceAll("\\[","").
		  							replaceAll("^","").replaceAll("}","").replaceAll("]","").replaceAll("``","").replaceAll("�","A").replaceAll("�","a").replaceAll("�","A").replaceAll("�","a").replaceAll("�","A").replaceAll("�","a").replaceAll("�","A").replaceAll("�","a").replaceAll("�","E").replaceAll("�","e").replaceAll("�","E").replaceAll("�","e").
		  							replaceAll("�","I").replaceAll("�","i").replaceAll("�","O").replaceAll("�","o").replaceAll("�","O").replaceAll("�","o").replaceAll("�","O").replaceAll("�","o").replaceAll("�","O").replaceAll("�","U").replaceAll("�","u").replaceAll("\\\\","").replaceAll("\\*","").replaceAll("\\+"," ").replaceAll("\\\"","").replaceAll("NULL","").replace("?","�"));
			            	sb.append(datosTransferencia.getRazonSocial().replaceAll("�","A").replaceAll("�","E").replaceAll("�","I").replaceAll("�","O").replaceAll("�","U").replaceAll("�","a").replaceAll("�","e").replaceAll("�","i").replaceAll("�","o").replaceAll("�","u").replaceAll("�","�").replaceAll("�","�").
		  							replaceAll("�","").replaceAll("�","").replaceAll(";"," ").replaceAll(":"," ").replaceAll("\\(","").replaceAll("\\)","").replaceAll("&","").replaceAll("/","").replaceAll("-","").replaceAll("N�","No").replaceAll("#","No").replaceAll("'","").replaceAll("�","No").replaceAll("`","").replaceAll("�","A").
		  							replaceAll("�","O").replaceAll("�","C").replaceAll("�","O").replaceAll("�","I").replaceAll("�"," ").replaceAll("�","").replaceAll("!","").replaceAll("$","").replaceAll("%","").replaceAll("=","").replaceAll("'","").replaceAll("�","").replaceAll("��","").replaceAll("��","").replaceAll("~","").replaceAll("\\{","").replaceAll("\\[","").
		  							replaceAll("^","").replaceAll("}","").replaceAll("]","").replaceAll("``","").replaceAll("�","A").replaceAll("�","a").replaceAll("�","A").replaceAll("�","a").replaceAll("�","A").replaceAll("�","a").replaceAll("�","A").replaceAll("�","a").replaceAll("�","E").replaceAll("�","e").replaceAll("�","E").replaceAll("�","e").
		  							replaceAll("�","I").replaceAll("�","i").replaceAll("�","O").replaceAll("�","o").replaceAll("�","O").replaceAll("�","o").replaceAll("�","O").replaceAll("�","o").replaceAll("�","O").replaceAll("�","U").replaceAll("�","u").replaceAll("\\\\","").replaceAll("\\*","").replaceAll("\\+"," ").replaceAll("\\\"","").replaceAll("NULL","").replace("?","�"));
			            	sb.append(datosTransferencia.getFechaConstitucion().replaceAll("�","A").replaceAll("�","E").replaceAll("�","I").replaceAll("�","O").replaceAll("�","U").replaceAll("�","a").replaceAll("�","e").replaceAll("�","i").replaceAll("�","o").replaceAll("�","u").replaceAll("�","�").replaceAll("�","�").
		  							replaceAll("�","").replaceAll("�","").replaceAll(";"," ").replaceAll(":"," ").replaceAll("\\(","").replaceAll("\\)","").replaceAll("&","").replaceAll("/","").replaceAll("-","").replaceAll("N�","No").replaceAll("#","No").replaceAll("'","").replaceAll("�","No").replaceAll("`","").replaceAll("�","A").
		  							replaceAll("�","O").replaceAll("�","C").replaceAll("�","O").replaceAll("�","I").replaceAll("�"," ").replaceAll("�","").replaceAll("!","").replaceAll("$","").replaceAll("%","").replaceAll("=","").replaceAll("'","").replaceAll("�","").replaceAll("��","").replaceAll("��","").replaceAll("~","").replaceAll("\\{","").replaceAll("\\[","").
		  							replaceAll("^","").replaceAll("}","").replaceAll("]","").replaceAll("``","").replaceAll("�","A").replaceAll("�","a").replaceAll("�","A").replaceAll("�","a").replaceAll("�","A").replaceAll("�","a").replaceAll("�","A").replaceAll("�","a").replaceAll("�","E").replaceAll("�","e").replaceAll("�","E").replaceAll("�","e").
		  							replaceAll("�","I").replaceAll("�","i").replaceAll("�","O").replaceAll("�","o").replaceAll("�","O").replaceAll("�","o").replaceAll("�","O").replaceAll("�","o").replaceAll("�","O").replaceAll("�","U").replaceAll("�","u").replaceAll("\\\\","").replaceAll("\\*","").replaceAll("\\+"," ").replaceAll("\\\"","").replaceAll("NULL","").replace("?","�"));
			            	sb.append(datosTransferencia.getRfcPM().replaceAll("�","A").replaceAll("�","E").replaceAll("�","I").replaceAll("�","O").replaceAll("�","U").replaceAll("�","a").replaceAll("�","e").replaceAll("�","i").replaceAll("�","o").replaceAll("�","u").replaceAll("�","�").replaceAll("�","�").
		  							replaceAll("�","").replaceAll("�","").replaceAll(";"," ").replaceAll(":"," ").replaceAll("\\(","").replaceAll("\\)","").replaceAll("&","").replaceAll("/","").replaceAll("-","").replaceAll("N�","No").replaceAll("#","No").replaceAll("'","").replaceAll("�","No").replaceAll("`","").replaceAll("�","A").
		  							replaceAll("�","O").replaceAll("�","C").replaceAll("�","O").replaceAll("�","I").replaceAll("�"," ").replaceAll("�","").replaceAll("!","").replaceAll("$","").replaceAll("%","").replaceAll("=","").replaceAll("'","").replaceAll("�","").replaceAll("��","").replaceAll("��","").replaceAll("~","").replaceAll("\\{","").replaceAll("\\[","").
		  							replaceAll("^","").replaceAll("}","").replaceAll("]","").replaceAll("``","").replaceAll("�","A").replaceAll("�","a").replaceAll("�","A").replaceAll("�","a").replaceAll("�","A").replaceAll("�","a").replaceAll("�","A").replaceAll("�","a").replaceAll("�","E").replaceAll("�","e").replaceAll("�","E").replaceAll("�","e").
		  							replaceAll("�","I").replaceAll("�","i").replaceAll("�","O").replaceAll("�","o").replaceAll("�","O").replaceAll("�","o").replaceAll("�","O").replaceAll("�","o").replaceAll("�","O").replaceAll("�","U").replaceAll("�","u").replaceAll("\\\\","").replaceAll("\\*","").replaceAll("\\+"," ").replaceAll("\\\"","").replaceAll("NULL","").replace("?","�"));
			            	sb.append(datosTransferencia.getPaisNacionalidadPM().replaceAll("�","A").replaceAll("�","E").replaceAll("�","I").replaceAll("�","O").replaceAll("�","U").replaceAll("�","a").replaceAll("�","e").replaceAll("�","i").replaceAll("�","o").replaceAll("�","u").replaceAll("�","�").replaceAll("�","�").
		  							replaceAll("�","").replaceAll("�","").replaceAll(";"," ").replaceAll(":"," ").replaceAll("\\(","").replaceAll("\\)","").replaceAll("&","").replaceAll("/","").replaceAll("-","").replaceAll("N�","No").replaceAll("#","No").replaceAll("'","").replaceAll("�","No").replaceAll("`","").replaceAll("�","A").
		  							replaceAll("�","O").replaceAll("�","C").replaceAll("�","O").replaceAll("�","I").replaceAll("�"," ").replaceAll("�","").replaceAll("!","").replaceAll("$","").replaceAll("%","").replaceAll("=","").replaceAll("'","").replaceAll("�","").replaceAll("��","").replaceAll("��","").replaceAll("~","").replaceAll("\\{","").replaceAll("\\[","").
		  							replaceAll("^","").replaceAll("}","").replaceAll("]","").replaceAll("``","").replaceAll("�","A").replaceAll("�","a").replaceAll("�","A").replaceAll("�","a").replaceAll("�","A").replaceAll("�","a").replaceAll("�","A").replaceAll("�","a").replaceAll("�","E").replaceAll("�","e").replaceAll("�","E").replaceAll("�","e").
		  							replaceAll("�","I").replaceAll("�","i").replaceAll("�","O").replaceAll("�","o").replaceAll("�","O").replaceAll("�","o").replaceAll("�","O").replaceAll("�","o").replaceAll("�","O").replaceAll("�","U").replaceAll("�","u").replaceAll("\\\\","").replaceAll("\\*","").replaceAll("\\+"," ").replaceAll("\\\"","").replaceAll("NULL","").replace("?","�"));
			            	sb.append(datosTransferencia.getGiroMercantil().replaceAll("�","A").replaceAll("�","E").replaceAll("�","I").replaceAll("�","O").replaceAll("�","U").replaceAll("�","a").replaceAll("�","e").replaceAll("�","i").replaceAll("�","o").replaceAll("�","u").replaceAll("�","�").replaceAll("�","�").
		  							replaceAll("�","").replaceAll("�","").replaceAll(";"," ").replaceAll(":"," ").replaceAll("\\(","").replaceAll("\\)","").replaceAll("&","").replaceAll("/","").replaceAll("-","").replaceAll("N�","No").replaceAll("#","No").replaceAll("'","").replaceAll("�","No").replaceAll("`","").replaceAll("�","A").
		  							replaceAll("�","O").replaceAll("�","C").replaceAll("�","O").replaceAll("�","I").replaceAll("�"," ").replaceAll("�","").replaceAll("!","").replaceAll("$","").replaceAll("%","").replaceAll("=","").replaceAll("'","").replaceAll("�","").replaceAll("��","").replaceAll("��","").replaceAll("~","").replaceAll("\\{","").replaceAll("\\[","").
		  							replaceAll("^","").replaceAll("}","").replaceAll("]","").replaceAll("``","").replaceAll("�","A").replaceAll("�","a").replaceAll("�","A").replaceAll("�","a").replaceAll("�","A").replaceAll("�","a").replaceAll("�","A").replaceAll("�","a").replaceAll("�","E").replaceAll("�","e").replaceAll("�","E").replaceAll("�","e").
		  							replaceAll("�","I").replaceAll("�","i").replaceAll("�","O").replaceAll("�","o").replaceAll("�","O").replaceAll("�","o").replaceAll("�","O").replaceAll("�","o").replaceAll("�","O").replaceAll("�","U").replaceAll("�","u").replaceAll("\\\\","").replaceAll("\\*","").replaceAll("\\+"," ").replaceAll("\\\"","").replaceAll("NULL","").replace("?","�"));
			            	sb.append(datosTransferencia.getApoderadoLegal().replaceAll("�","A").replaceAll("�","E").replaceAll("�","I").replaceAll("�","O").replaceAll("�","U").replaceAll("�","a").replaceAll("�","e").replaceAll("�","i").replaceAll("�","o").replaceAll("�","u").replaceAll("�","�").replaceAll("�","�").
		  							replaceAll("�","").replaceAll("�","").replaceAll(";"," ").replaceAll(":"," ").replaceAll("\\(","").replaceAll("\\)","").replaceAll("&","").replaceAll("/","").replaceAll("-","").replaceAll("N�","No").replaceAll("#","No").replaceAll("'","").replaceAll("�","No").replaceAll("`","").replaceAll("�","A").
		  							replaceAll("�","O").replaceAll("�","C").replaceAll("�","O").replaceAll("�","I").replaceAll("�"," ").replaceAll("�","").replaceAll("!","").replaceAll("$","").replaceAll("%","").replaceAll("=","").replaceAll("'","").replaceAll("�","").replaceAll("��","").replaceAll("��","").replaceAll("~","").replaceAll("\\{","").replaceAll("\\[","").
		  							replaceAll("^","").replaceAll("}","").replaceAll("]","").replaceAll("``","").replaceAll("�","A").replaceAll("�","a").replaceAll("�","A").replaceAll("�","a").replaceAll("�","A").replaceAll("�","a").replaceAll("�","A").replaceAll("�","a").replaceAll("�","E").replaceAll("�","e").replaceAll("�","E").replaceAll("�","e").
		  							replaceAll("�","I").replaceAll("�","i").replaceAll("�","O").replaceAll("�","o").replaceAll("�","O").replaceAll("�","o").replaceAll("�","O").replaceAll("�","o").replaceAll("�","O").replaceAll("�","U").replaceAll("�","u").replaceAll("\\\\","").replaceAll("\\*","").replaceAll("\\+"," ").replaceAll("\\\"","").replaceAll("NULL","").replace("?","�"));
			            	sb.append(datosTransferencia.getDomicilioUnificado().replaceAll("�","A").replaceAll("�","E").replaceAll("�","I").replaceAll("�","O").replaceAll("�","U").replaceAll("�","a").replaceAll("�","e").replaceAll("�","i").replaceAll("�","o").replaceAll("�","u").replaceAll("�","�").replaceAll("�","�").
		  							replaceAll("�","").replaceAll("�","").replaceAll(";"," ").replaceAll(":"," ").replaceAll("\\(","").replaceAll("\\)","").replaceAll("&","").replaceAll("/","").replaceAll("-","").replaceAll("N�","No").replaceAll("#","No").replaceAll("'","").replaceAll("�","No").replaceAll("`","").replaceAll("�","A").
		  							replaceAll("�","O").replaceAll("�","C").replaceAll("�","O").replaceAll("�","I").replaceAll("�"," ").replaceAll("�","").replaceAll("!","").replaceAll("$","").replaceAll("%","").replaceAll("=","").replaceAll("'","").replaceAll("�","").replaceAll("��","").replaceAll("��","").replaceAll("~","").replaceAll("\\{","").replaceAll("\\[","").
		  							replaceAll("^","").replaceAll("}","").replaceAll("]","").replaceAll("``","").replaceAll("�","A").replaceAll("�","a").replaceAll("�","A").replaceAll("�","a").replaceAll("�","A").replaceAll("�","a").replaceAll("�","A").replaceAll("�","a").replaceAll("�","E").replaceAll("�","e").replaceAll("�","E").replaceAll("�","e").
		  							replaceAll("�","I").replaceAll("�","i").replaceAll("�","O").replaceAll("�","o").replaceAll("�","O").replaceAll("�","o").replaceAll("�","O").replaceAll("�","o").replaceAll("�","O").replaceAll("�","U").replaceAll("�","u").replaceAll("\\\\","").replaceAll("\\*","").replaceAll("\\+"," ").replaceAll("\\\"","").replaceAll("NULL","").replace("?","�"));
			            	sb.append(datosTransferencia.getCiudadPoblacion().replaceAll("�","A").replaceAll("�","E").replaceAll("�","I").replaceAll("�","O").replaceAll("�","U").replaceAll("�","a").replaceAll("�","e").replaceAll("�","i").replaceAll("�","o").replaceAll("�","u").replaceAll("�","�").replaceAll("�","�").
		  							replaceAll("�","").replaceAll("�","").replaceAll(";"," ").replaceAll(":"," ").replaceAll("\\(","").replaceAll("\\)","").replaceAll("&","").replaceAll("/","").replaceAll("-","").replaceAll("N�","No").replaceAll("#","No").replaceAll("'","").replaceAll("�","No").replaceAll("`","").replaceAll("�","A").
		  							replaceAll("�","O").replaceAll("�","C").replaceAll("�","O").replaceAll("�","I").replaceAll("�"," ").replaceAll("�","").replaceAll("!","").replaceAll("$","").replaceAll("%","").replaceAll("=","").replaceAll("'","").replaceAll("�","").replaceAll("��","").replaceAll("��","").replaceAll("~","").replaceAll("\\{","").replaceAll("\\[","").
		  							replaceAll("^","").replaceAll("}","").replaceAll("]","").replaceAll("``","").replaceAll("�","A").replaceAll("�","a").replaceAll("�","A").replaceAll("�","a").replaceAll("�","A").replaceAll("�","a").replaceAll("�","A").replaceAll("�","a").replaceAll("�","E").replaceAll("�","e").replaceAll("�","E").replaceAll("�","e").
		  							replaceAll("�","I").replaceAll("�","i").replaceAll("�","O").replaceAll("�","o").replaceAll("�","O").replaceAll("�","o").replaceAll("�","O").replaceAll("�","o").replaceAll("�","O").replaceAll("�","U").replaceAll("�","u").replaceAll("\\\\","").replaceAll("\\*","").replaceAll("\\+"," ").replaceAll("\\\"","").replaceAll("NULL","").replace("?","�"));
			            	sb.append(datosTransferencia.getCodigoPostal().replaceAll("�","A").replaceAll("�","E").replaceAll("�","I").replaceAll("�","O").replaceAll("�","U").replaceAll("�","a").replaceAll("�","e").replaceAll("�","i").replaceAll("�","o").replaceAll("�","u").replaceAll("�","�").replaceAll("�","�").
		  							replaceAll("�","").replaceAll("�","").replaceAll(";"," ").replaceAll(":"," ").replaceAll("\\(","").replaceAll("\\)","").replaceAll("&","").replaceAll("/","").replaceAll("-","").replaceAll("N�","No").replaceAll("#","No").replaceAll("'","").replaceAll("�","No").replaceAll("`","").replaceAll("�","A").
		  							replaceAll("�","O").replaceAll("�","C").replaceAll("�","O").replaceAll("�","I").replaceAll("�"," ").replaceAll("�","").replaceAll("!","").replaceAll("$","").replaceAll("%","").replaceAll("=","").replaceAll("'","").replaceAll("�","").replaceAll("��","").replaceAll("��","").replaceAll("~","").replaceAll("\\{","").replaceAll("\\[","").
		  							replaceAll("^","").replaceAll("}","").replaceAll("]","").replaceAll("``","").replaceAll("�","A").replaceAll("�","a").replaceAll("�","A").replaceAll("�","a").replaceAll("�","A").replaceAll("�","a").replaceAll("�","A").replaceAll("�","a").replaceAll("�","E").replaceAll("�","e").replaceAll("�","E").replaceAll("�","e").
		  							replaceAll("�","I").replaceAll("�","i").replaceAll("�","O").replaceAll("�","o").replaceAll("�","O").replaceAll("�","o").replaceAll("�","O").replaceAll("�","o").replaceAll("�","O").replaceAll("�","U").replaceAll("�","u").replaceAll("\\\\","").replaceAll("\\*","").replaceAll("\\+"," ").replaceAll("\\\"","").replaceAll("NULL","").replace("?","�"));
			            	sb.append(datosTransferencia.getEntidadFederativa().replaceAll("�","A").replaceAll("�","E").replaceAll("�","I").replaceAll("�","O").replaceAll("�","U").replaceAll("�","a").replaceAll("�","e").replaceAll("�","i").replaceAll("�","o").replaceAll("�","u").replaceAll("�","�").replaceAll("�","�").
		  							replaceAll("�","").replaceAll("�","").replaceAll(";"," ").replaceAll(":"," ").replaceAll("\\(","").replaceAll("\\)","").replaceAll("&","").replaceAll("/","").replaceAll("-","").replaceAll("N�","No").replaceAll("#","No").replaceAll("'","").replaceAll("�","No").replaceAll("`","").replaceAll("�","A").
		  							replaceAll("�","O").replaceAll("�","C").replaceAll("�","O").replaceAll("�","I").replaceAll("�"," ").replaceAll("�","").replaceAll("!","").replaceAll("$","").replaceAll("%","").replaceAll("=","").replaceAll("'","").replaceAll("�","").replaceAll("��","").replaceAll("��","").replaceAll("~","").replaceAll("\\{","").replaceAll("\\[","").
		  							replaceAll("^","").replaceAll("}","").replaceAll("]","").replaceAll("``","").replaceAll("�","A").replaceAll("�","a").replaceAll("�","A").replaceAll("�","a").replaceAll("�","A").replaceAll("�","a").replaceAll("�","A").replaceAll("�","a").replaceAll("�","E").replaceAll("�","e").replaceAll("�","E").replaceAll("�","e").
		  							replaceAll("�","I").replaceAll("�","i").replaceAll("�","O").replaceAll("�","o").replaceAll("�","O").replaceAll("�","o").replaceAll("�","O").replaceAll("�","o").replaceAll("�","O").replaceAll("�","U").replaceAll("�","u").replaceAll("\\\\","").replaceAll("\\*","").replaceAll("\\+"," ").replaceAll("\\\"","").replaceAll("NULL","").replace("?","�"));
			            	sb.append(datosTransferencia.getTelefono().replaceAll("�","A").replaceAll("�","E").replaceAll("�","I").replaceAll("�","O").replaceAll("�","U").replaceAll("�","a").replaceAll("�","e").replaceAll("�","i").replaceAll("�","o").replaceAll("�","u").replaceAll("�","�").replaceAll("�","�").
		  							replaceAll("�","").replaceAll("�","").replaceAll(";"," ").replaceAll(":"," ").replaceAll("\\(","").replaceAll("\\)","").replaceAll("&","").replaceAll("/","").replaceAll("-","").replaceAll("N�","No").replaceAll("#","No").replaceAll("'","").replaceAll("�","No").replaceAll("`","").replaceAll("�","A").
		  							replaceAll("�","O").replaceAll("�","C").replaceAll("�","O").replaceAll("�","I").replaceAll("�"," ").replaceAll("�","").replaceAll("!","").replaceAll("$","").replaceAll("%","").replaceAll("=","").replaceAll("'","").replaceAll("�","").replaceAll("��","").replaceAll("��","").replaceAll("~","").replaceAll("\\{","").replaceAll("\\[","").
		  							replaceAll("^","").replaceAll("}","").replaceAll("]","").replaceAll("``","").replaceAll("�","A").replaceAll("�","a").replaceAll("�","A").replaceAll("�","a").replaceAll("�","A").replaceAll("�","a").replaceAll("�","A").replaceAll("�","a").replaceAll("�","E").replaceAll("�","e").replaceAll("�","E").replaceAll("�","e").
		  							replaceAll("�","I").replaceAll("�","i").replaceAll("�","O").replaceAll("�","o").replaceAll("�","O").replaceAll("�","o").replaceAll("�","O").replaceAll("�","o").replaceAll("�","O").replaceAll("�","U").replaceAll("�","u").replaceAll("\\\\","").replaceAll("\\*","").replaceAll("\\+"," ").replaceAll("\\\"","").replaceAll("NULL","").replace("?","�"));
			            	sb.append(datosTransferencia.getNumeroTelefono().replaceAll("�","A").replaceAll("�","E").replaceAll("�","I").replaceAll("�","O").replaceAll("�","U").replaceAll("�","a").replaceAll("�","e").replaceAll("�","i").replaceAll("�","o").replaceAll("�","u").replaceAll("�","�").replaceAll("�","�").
		  							replaceAll("�","").replaceAll("�","").replaceAll(";"," ").replaceAll(":"," ").replaceAll("\\(","").replaceAll("\\)","").replaceAll("&","").replaceAll("/","").replaceAll("-","").replaceAll("N�","No").replaceAll("#","No").replaceAll("'","").replaceAll("�","No").replaceAll("`","").replaceAll("�","A").
		  							replaceAll("�","O").replaceAll("�","C").replaceAll("�","O").replaceAll("�","I").replaceAll("�"," ").replaceAll("�","").replaceAll("!","").replaceAll("$","").replaceAll("%","").replaceAll("=","").replaceAll("'","").replaceAll("�","").replaceAll("��","").replaceAll("��","").replaceAll("~","").replaceAll("\\{","").replaceAll("\\[","").
		  							replaceAll("^","").replaceAll("}","").replaceAll("]","").replaceAll("``","").replaceAll("�","A").replaceAll("�","a").replaceAll("�","A").replaceAll("�","a").replaceAll("�","A").replaceAll("�","a").replaceAll("�","A").replaceAll("�","a").replaceAll("�","E").replaceAll("�","e").replaceAll("�","E").replaceAll("�","e").
		  							replaceAll("�","I").replaceAll("�","i").replaceAll("�","O").replaceAll("�","o").replaceAll("�","O").replaceAll("�","o").replaceAll("�","O").replaceAll("�","o").replaceAll("�","O").replaceAll("�","U").replaceAll("�","u").replaceAll("\\\\","").replaceAll("\\*","").replaceAll("\\+"," ").replaceAll("\\\"","").replaceAll("NULL","").replace("?","�"));
			            	sb.append(datosTransferencia.getClavePais().replaceAll("�","A").replaceAll("�","E").replaceAll("�","I").replaceAll("�","O").replaceAll("�","U").replaceAll("�","a").replaceAll("�","e").replaceAll("�","i").replaceAll("�","o").replaceAll("�","u").replaceAll("�","�").replaceAll("�","�").
		  							replaceAll("�","").replaceAll("�","").replaceAll(";"," ").replaceAll(":"," ").replaceAll("\\(","").replaceAll("\\)","").replaceAll("&","").replaceAll("/","").replaceAll("-","").replaceAll("N�","No").replaceAll("#","No").replaceAll("'","").replaceAll("�","No").replaceAll("`","").replaceAll("�","A").
		  							replaceAll("�","O").replaceAll("�","C").replaceAll("�","O").replaceAll("�","I").replaceAll("�"," ").replaceAll("�","").replaceAll("!","").replaceAll("$","").replaceAll("%","").replaceAll("=","").replaceAll("'","").replaceAll("�","").replaceAll("��","").replaceAll("��","").replaceAll("~","").replaceAll("\\{","").replaceAll("\\[","").
		  							replaceAll("^","").replaceAll("}","").replaceAll("]","").replaceAll("``","").replaceAll("�","A").replaceAll("�","a").replaceAll("�","A").replaceAll("�","a").replaceAll("�","A").replaceAll("�","a").replaceAll("�","A").replaceAll("�","a").replaceAll("�","E").replaceAll("�","e").replaceAll("�","E").replaceAll("�","e").
		  							replaceAll("�","I").replaceAll("�","i").replaceAll("�","O").replaceAll("�","o").replaceAll("�","O").replaceAll("�","o").replaceAll("�","O").replaceAll("�","o").replaceAll("�","O").replaceAll("�","U").replaceAll("�","u").replaceAll("\\\\","").replaceAll("\\*","").replaceAll("\\+"," ").replaceAll("\\\"","").replaceAll("NULL","").replace("?","�"));
			            	sb.append(datosTransferencia.getExtension().replaceAll("�","A").replaceAll("�","E").replaceAll("�","I").replaceAll("�","O").replaceAll("�","U").replaceAll("�","a").replaceAll("�","e").replaceAll("�","i").replaceAll("�","o").replaceAll("�","u").replaceAll("�","�").replaceAll("�","�").
		  							replaceAll("�","").replaceAll("�","").replaceAll(";"," ").replaceAll(":"," ").replaceAll("\\(","").replaceAll("\\)","").replaceAll("&","").replaceAll("/","").replaceAll("-","").replaceAll("N�","No").replaceAll("#","No").replaceAll("'","").replaceAll("�","No").replaceAll("`","").replaceAll("�","A").
		  							replaceAll("�","O").replaceAll("�","C").replaceAll("�","O").replaceAll("�","I").replaceAll("�"," ").replaceAll("�","").replaceAll("!","").replaceAll("$","").replaceAll("%","").replaceAll("=","").replaceAll("'","").replaceAll("�","").replaceAll("��","").replaceAll("��","").replaceAll("~","").replaceAll("\\{","").replaceAll("\\[","").
		  							replaceAll("^","").replaceAll("}","").replaceAll("]","").replaceAll("``","").replaceAll("�","A").replaceAll("�","a").replaceAll("�","A").replaceAll("�","a").replaceAll("�","A").replaceAll("�","a").replaceAll("�","A").replaceAll("�","a").replaceAll("�","E").replaceAll("�","e").replaceAll("�","E").replaceAll("�","e").
		  							replaceAll("�","I").replaceAll("�","i").replaceAll("�","O").replaceAll("�","o").replaceAll("�","O").replaceAll("�","o").replaceAll("�","O").replaceAll("�","o").replaceAll("�","O").replaceAll("�","U").replaceAll("�","u").replaceAll("\\\\","").replaceAll("\\*","").replaceAll("\\+"," ").replaceAll("\\\"","").replaceAll("NULL","").replace("?","�"));
			            	sb.append(datosTransferencia.getCorreoElectronico().replaceAll("�","A").replaceAll("�","E").replaceAll("�","I").replaceAll("�","O").replaceAll("�","U").replaceAll("�","a").replaceAll("�","e").replaceAll("�","i").replaceAll("�","o").replaceAll("�","u").replaceAll("�","�").replaceAll("�","�").
		  							replaceAll("�","").replaceAll("�","").replaceAll(";"," ").replaceAll(":"," ").replaceAll("\\(","").replaceAll("\\)","").replaceAll("&","").replaceAll("/","").replaceAll("-","").replaceAll("N�","No").replaceAll("#","No").replaceAll("'","").replaceAll("�","No").replaceAll("`","").replaceAll("�","A").
		  							replaceAll("�","O").replaceAll("�","C").replaceAll("�","O").replaceAll("�","I").replaceAll("�"," ").replaceAll("�","").replaceAll("!","").replaceAll("$","").replaceAll("%","").replaceAll("=","").replaceAll("'","").replaceAll("�","").replaceAll("��","").replaceAll("��","").replaceAll("~","").replaceAll("\\{","").replaceAll("\\[","").
		  							replaceAll("^","").replaceAll("}","").replaceAll("]","").replaceAll("``","").replaceAll("�","A").replaceAll("�","a").replaceAll("�","A").replaceAll("�","a").replaceAll("�","A").replaceAll("�","a").replaceAll("�","A").replaceAll("�","a").replaceAll("�","E").replaceAll("�","e").replaceAll("�","E").replaceAll("�","e").
		  							replaceAll("�","I").replaceAll("�","i").replaceAll("�","O").replaceAll("�","o").replaceAll("�","O").replaceAll("�","o").replaceAll("�","O").replaceAll("�","o").replaceAll("�","O").replaceAll("�","U").replaceAll("�","u").replaceAll("\\\\","").replaceAll("\\*","").replaceAll("\\+"," ").replaceAll("\\\"","").replaceAll("NULL","").replace("?","�"));
			            	sb.append(datosTransferencia.getFiel().replace("|", ""));
			            	sb.append("\n");			            	 
						}*/
			            wr.write(sb.toString());
			            wr.flush();
			            wr.close();
			            bw.close();

					}

					

				}

				
			}
			catch(Exception e)
			{
				LOG.error("-->" + mensajesVo.getMensajeErrorTransferencias(), e);
				datos = new HashMap<String, Object>();
				datos.put("mensaje", mensajesVo.getMensajeErrorTransferencias() +  e.getLocalizedMessage());
				enviarEmail(seTienenEmailsDestinatarios, emailsTo, datos);
				//se borran registros que no se reportaron a la UIF para que no se dupliquen
				//consultaDao.borrarOperacionesNoReportadas();
				//consultaDao.borrarOperacionesNoReportadasRec();
			}
			
			LOG.warn("-->" + mensajesVo.getMensajeFinProceso());
			datos = new HashMap<String, Object>();
			datos.put("mensaje", mensajesVo.getMensajeFinProceso());
			enviarEmail(seTienenEmailsDestinatarios, emailsTo, datos);
		}
	

	}

	public void enviarEmail(boolean seTienenEmailsDestinatarios, String emailsTo, Map<String, Object> datos)
	{
		if(seTienenEmailsDestinatarios)
		{
			mailSender.setTo(emailsTo);
			mailSender.send(datos);
		}
	}

	public IConsultaDao getConsultaDao() 
	{ 
		return consultaDao; 
	}
	
	public void setConsultaDao(IConsultaDao consultaDao) 
	{ 
		this.consultaDao = consultaDao; 
	}

	public MailSender getMailSender() 
	{
		return mailSender; 
	}
	
	public void setMailSender(MailSender mailSender) 
	{ 
		this.mailSender = mailSender; 
	}

	public MensajesVo getMensajesVo() 
	{ 
		return mensajesVo;
	}
	
	public void setMensajesVo(MensajesVo mensajesVo) 
	{ 
		this.mensajesVo = mensajesVo;
	}

	public IReporteService getReporteService() 
	{
		return reporteService;
	}

	public void setReporteService(IReporteService reporteService) 
	{
		this.reporteService = reporteService;
	}
}
