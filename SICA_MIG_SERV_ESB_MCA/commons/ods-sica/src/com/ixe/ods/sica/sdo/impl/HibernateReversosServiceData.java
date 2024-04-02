/*
* $Id: HibernateReversosServiceData.java,v 1.24.18.2.2.1.6.2.18.1.4.1.20.2.2.5 2016/08/31 21:21:47 mejiar Exp $
* Ixe Grupo Financiero
* Copyright (c) 2007 - 2009 LegoSoft S.C.
*/
package com.ixe.ods.sica.sdo.impl;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import org.apache.commons.lang.StringUtils;
import org.springframework.orm.hibernate.HibernateCallback;
import org.springframework.orm.hibernate.SessionFactoryUtils;

import com.ixe.ods.bup.model.ContratoSica;
import com.ixe.ods.bup.model.CuentaContrato;
import com.ixe.ods.bup.model.EmpleadoIxe;
import com.ixe.ods.bup.model.Persona;
import com.ixe.ods.bup.model.PersonaCuentaRol;
import com.ixe.ods.seguridad.model.IUsuario;
import com.ixe.ods.seguridad.model.SeguridadException;
import com.ixe.ods.seguridad.model.Usuario;
import com.ixe.ods.sica.Constantes;
import com.ixe.ods.sica.Redondeador;
import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.SicaFechaValorException;
import com.ixe.ods.sica.SicaValidationException;
import com.ixe.ods.sica.SiteService;
import com.ixe.ods.sica.model.Actividad;
import com.ixe.ods.sica.model.Canal;
import com.ixe.ods.sica.model.Contrato;
import com.ixe.ods.sica.model.ContratoCliente;
import com.ixe.ods.sica.model.Deal;
import com.ixe.ods.sica.model.DealDetalle;
import com.ixe.ods.sica.model.Divisa;
import com.ixe.ods.sica.model.IPlantilla;
import com.ixe.ods.sica.model.ParametroSica;
import com.ixe.ods.sica.model.Plantilla;
import com.ixe.ods.sica.model.PlantillaPantalla;
import com.ixe.ods.sica.model.Reverso;
import com.ixe.ods.sica.model.ReversoDetalle;
import com.ixe.ods.sica.model.Swap;
import com.ixe.ods.sica.model.TipoBloqueo;
import com.ixe.ods.sica.sdo.PizarronServiceData;
import com.ixe.ods.sica.sdo.ReversosServiceData;
import com.ixe.ods.sica.sdo.dto.TipoOperacionDealDto;
import com.ixe.ods.sica.services.FormasPagoLiqService;
import com.ixe.ods.sica.services.H2HService;
import com.ixe.ods.sica.services.ReversoMailSender;
import com.ixe.ods.sica.utils.DateUtils;
import com.ixe.ods.sica.vo.DealDetalleVO;
import com.ixe.ods.sica.vo.DealVO;
import com.ixe.ods.sica.vo.DetalleReversoVO;
import com.ixe.ods.sica.vo.ReversoVO;
import com.ixe.treasury.dom.common.ExternalSiteException;
import com.ixe.treasury.dom.common.FormaPagoLiq;

/**
* Implementaci&oacute;n de la interfaz ReversosServiceData.
*
* @author Jean C. Favila
* @version $Revision: 1.24.18.2.2.1.6.2.18.1.4.1.20.2.2.5 $ $Date: 2016/08/31 21:21:47 $
*/
public class HibernateReversosServiceData extends HibernateWorkFlowServiceData
       implements ReversosServiceData {

   /**
    * Constructor vac&iacute;o.
    */
   public HibernateReversosServiceData() {
       super();
   }

   /**
    * Levanta una excepci&oacute;n si el deal tiene una solicitud de modificaci&oacute;n o de
    * cancelaci&oacute;n pendiente.
    *
    * @param deal El deal a validar.
    * @throws SicaException Si no se debe realizar el reverso para el deal especificado.
    */
   private void validarDealParaReverso(Deal deal) throws SicaException {
       if (deal.tieneSolModifPendiente()) {
           throw new SicaException("No se puede realizar el reverso pues el deal tiene una " +
                   "autorizaci\u00f3n de modificaci\u00f3n pendiente.");
       }
       if (deal.tieneSolCancPendiente()) {
           throw new SicaException("No se puede realizar el reverso pues el deal tiene una " +
                   "autorizaci\u00f3n de cancelaci\u00f3n pendiente.");
       }
   }

   /**
    * @see ReversosServiceData#findReversosExistentesParaDeal(int).
    */
   public List findReversosExistentesParaDeal(int idDeal) {
       return getHibernateTemplate().find("FROM Reverso AS r WHERE (r.dealOriginal.idDeal = ? " +
               "OR r.dealBalanceo.idDeal = ?)",
               new Object[]{new Integer(idDeal), new Integer(idDeal)});
   }

   /**
    * @see ReversosServiceData#findDeal(int).
    */
   public Deal findDeal(int idDeal) throws SicaException {
       try {
           List deals = getHibernateTemplate().find("SELECT DISTINCT d FROM Deal AS d LEFT JOIN " +
                   "FETCH d.contratoSica LEFT JOIN FETCH d.promotor LEFT JOIN FETCH d.usuario " +
                   "AS u LEFT JOIN FETCH u.persona LEFT JOIN FETCH d.dirFactura LEFT JOIN FETCH " +
                   "d.direccion LEFT JOIN FETCH d.swap LEFT JOIN FETCH d.detalles AS det " +
                   "LEFT JOIN FETCH det.divisa LEFT JOIN FETCH " +
                   "det.plantilla as plan LEFT JOIN FETCH plan.beneficiario WHERE d.idDeal = ?",
                   new Integer(idDeal));
           if (deals.isEmpty()) {
               throw new SicaException("No se encontr\u00f3 el deal con el n\u00famero " + idDeal
                       + ".");
           }
           Deal deal = (Deal) deals.get(0);
           try {
               Hibernate.initialize(deal.getCanalMesa());
               if (deal.getCliente() != null) {
                   Hibernate.initialize(deal.getCliente());
               }
               if (deal.isInterbancario()) {
                   if (deal.getBroker() != null) {
                       Hibernate.initialize(deal.getBroker());
                       deal.getBroker().getClaveReuters();
                       deal.getBroker().getId().getPersonaMoral().getNombreCompleto();
                   }
               }
               if (deal.getSwap() != null) {
                   Hibernate.initialize(deal.getSwap());
               }
           }
           catch (HibernateException e) {
               throw SessionFactoryUtils.convertHibernateAccessException(e);
           }
           deal.getCanalMesa().getCanal().getNombre();
           deal.getCanalMesa().getMesaCambio().getNombre();
           if (deal.getSwap() != null) {
               deal.getSwap().getTipoSwap();
           }
           validarDealParaReverso(deal);
           return deal;
       }
       catch (SicaException e) {
           if (logger.isWarnEnabled()) {
               logger.warn(e.getMessage(), e);
           }
           throw e;
       }
       catch (Exception e) {
           if (logger.isWarnEnabled()) {
               logger.warn(e.getMessage(), e);
           }
           throw new RuntimeException(e);
       }
   }

   /**
    * Inicializa las relaciones del reverso.
    *
    * @param reverso El reverso a inicializar.
    */
   private void inicializarReverso(Reverso reverso) {
       reverso.getDealOriginal().getCliente().getNombreCompleto();
       reverso.getDealBalanceo().getCliente().getNombreCompleto();
       reverso.getUsuario().getPersona().getNombreCompleto();
       List deals = new ArrayList();
       deals.add(reverso.getDealOriginal());
       deals.add(reverso.getDealBalanceo());
       if (reverso.getPorContratoSica() != null) {
           for (Iterator it = reverso.getPorContratoSica().getRoles().iterator(); it.hasNext();) {
               PersonaCuentaRol pcr = (PersonaCuentaRol) it.next();
               pcr.getStatus();
               pcr.getId().getPersona().getNombreCompleto();
           }
           reverso.getCliente().getNombreCompleto();
       }
       if (reverso.getDealCorreccion() != null) {
           reverso.getDealCorreccion().getCliente().getNombreCompleto();
           deals.add(reverso.getDealCorreccion());
       }
       for (Iterator it = deals.iterator(); it.hasNext();) {
           Deal deal = (Deal) it.next();
           for (Iterator it2 = deal.getDetalles().iterator(); it2.hasNext();) {
               DealDetalle det = (DealDetalle) it2.next();
               det.getDivisa().getDescripcion();
           }
       }
   }

   /**
    * @see com.ixe.ods.sica.sdo.ReversosServiceData#findReverso(int).
    */
   public Reverso findReverso(int idReverso) throws SicaException {
       List reversos = new ArrayList(new HashSet(getHibernateTemplate().
               find("FROM Reverso AS r INNER JOIN FETCH r.usuario AS u INNER JOIN FETCH " +
               "u.persona INNER JOIN FETCH r.dealOriginal INNER JOIN FETCH r.dealBalanceo LEFT " +
               "JOIN FETCH r.dealCorreccion WHERE r.idReverso = ?", new Integer(idReverso))));
       if (reversos.isEmpty()) {
           throw new SicaException("No se encontr\u00f3 el reverso n\u00famero " + idReverso);
       }
       Reverso reverso = (Reverso) reversos.get(0);
       inicializarReverso(reverso);
       return reverso;
   }

   /**
    * @see com.ixe.ods.sica.sdo.ReversosServiceData#findReversosPendientes().
    */
   public List findReversosPendientes() {
       List reversos = getHibernateTemplate().find("FROM Reverso AS r INNER JOIN FETCH " +
               "r.usuario AS u INNER JOIN FETCH u.persona INNER JOIN FETCH r.dealOriginal LEFT " +
               "JOIN FETCH r.detalles WHERE r.statusReverso = ?", Reverso.STATUS_PENDIENTE);
       reversos = new ArrayList(new HashSet(reversos));
       List resultados = new ArrayList();
       for (Iterator it = reversos.iterator(); it.hasNext();) {
           Reverso reverso = (Reverso) it.next();
           resultados.add(new ReversoVO(reverso));
       }
       return resultados;
   }

   /**
    * @see com.ixe.ods.sica.sdo.ReversosServiceData#generarReverso(String, int, boolean, String,
    *      String, String, int, java.util.List).
    */
   public void generarReverso(String ticket, int idDeal, boolean porCancelacion,
                              String porNoCuenta, String porFechaValor, String observaciones,
                              int idUsuario, List detallesReverso) throws SicaException {
		ContratoSica porContratoSica = null;
		Deal dealOriginal = findDeal(idDeal);
		validarDealParaReverso(dealOriginal);
		IUsuario usuario = (IUsuario) find(Usuario.class, new Integer(idUsuario));
		boolean isCambioFechaValor=false;
		boolean isCambioMonto=false;
		boolean isCambioTC=false;
		boolean isCambioCliente=false;
		Persona nuevoCliente=null;
		Integer idPersonaLC = dealOriginal.getCliente().getIdPersona();
		String fechaValor = dealOriginal.getTipoValor();
		
		validarReverso(idDeal, dealOriginal);
		
		if (!StringUtils.isEmpty(porNoCuenta)) {
			if (dealOriginal.getContratoSica() != null && porNoCuenta.trim().equals(
					dealOriginal.getContratoSica().getNoCuenta().trim())) {
				
				throw new SicaException(
						"No se puede reversar el deal por concepto de cliente, "
								+ "pues el contrato seleccionado es el mismo que el original.");
			}
			isCambioCliente=true;
			porContratoSica = findContrato(porNoCuenta);
			if (!dealOriginal.isInterbancario()) {
				Contrato contratoCorto = null;				
				nuevoCliente = ContratoCliente.clienteParaContratoSica(porContratoSica);
				idPersonaLC = nuevoCliente.getIdPersona();
				List cuenta = findFechaAperturaByContratoSica(porContratoSica);
				CuentaContrato cc = (CuentaContrato) cuenta.get(0);
				Date fecha = (Date) cc.getFechaApertura();

				if (fecha != null && fecha.after(getFechaValidaDocumentacion())) {
					contratoCorto = getContratoCorto(porContratoSica
							.getNoCuenta());
					if (contratoCorto.getIdBloqueo() != null
							&& contratoCorto.getIdBloqueo().intValue() != TipoBloqueo.SIN_BLOQUEO) {
						String descripcion = "";
						descripcion = obtenerDescripcionBloqueo(contratoCorto
								.getIdBloqueo());
						throw new SicaException(
								"El contrato esta Bloqueado por: "
										+ descripcion
										+ ". Favor de comunicarse al area de Contratos.");
					}
				}

				super.logger
						.info("******************* VALOR DE FECHA_VALIDA_DOCUMENTACION --> :  "
								+ getFechaValidaDocumentacion());
				super.logger
						.info("******************* FECHA APERTURA DEL CONTRATO--> :  "
								+ " >> " + fecha);
				super.logger
						.info("******************* Si la fecha de apertura es anterior al parametro definido va al MOC a validar Doc.  ");

				if (fecha.before(getFechaValidaDocumentacion())) {
					super.logger
							.info("******************* entra a validar documentacion al MOC  ");
					// Se valida en Contratacion si el cliente puede operar mas
					// de 3 mil dolares
					validarClientePorMontoMayorATresMilUSD(dealOriginal,
							nuevoCliente);
				}
			}
		}
		
		if (!StringUtils.isEmpty(porFechaValor)) {
			isCambioFechaValor=true;
			fechaValor = porFechaValor;
			if (porFechaValor.trim().equals(dealOriginal.getTipoValor().trim())) {
				throw new SicaException(
						"No se puede reversar el deal por concepto de fecha "
								+ "valor, pues la fecha valor seleccionada es la misma que el original.");
			}
		}

		// Elimina las autorizaciones pendientes del deal original.
		boolean tieneActividadesDealOrginal = terminarActividadesParaDeal(
				dealOriginal, Actividad.RES_SOL_CANC, usuario);
		Reverso reverso = new Reverso(dealOriginal, observaciones,
				porCancelacion, porContratoSica, porFechaValor, usuario);
		// Asigna el status al reverso en caso de que el deal original
		// haya tenido autorizaciones pendientes.
		if (tieneActividadesDealOrginal) {
			reverso.setStatusReverso(Reverso.STATUS_PENDIENTE);
		}
		
		store(reverso);
		
		
		double diferenciaDetalles= 0.0;
		
		for (Iterator it = detallesReverso.iterator(); it.hasNext();) {
			DetalleReversoVO detRevVO = (DetalleReversoVO) it.next();
			ReversoDetalle detRev = new ReversoDetalle();
			DealDetalle detOrig = dealOriginal.getDetalleConFolio(detRevVO.getFolioDetalle());
			detRev.setDealDetalle(detOrig);
			
			if ( isReversoCambioMonto(detRevVO, detOrig)){
				isCambioMonto=true;				
				detRev.setMontoNuevo(detRevVO.getMonto());
				boolean aplicaLC = getLineaCambioServiceData().validarAplicablesTfPagAnt(ticket, detOrig.getMnemonico(), detOrig.getClaveFormaLiquidacion(), detOrig.isRecibimos());
				if(aplicaLC){
					double montoUSDDetalleOriginal = Redondeador.redondear2Dec(detOrig.getMontoUSD());
		    		if(montoUSDDetalleOriginal == 0.0 ){
		    			montoUSDDetalleOriginal = Redondeador.redondear2Dec(getLineaCambioServiceData().getMontoUsdDealDetalle(detOrig));
		    		}
		    		
		    		double montoDetalleOriginal = detOrig.getMonto();
		    		
		    		detOrig.setMonto(detRev.getMontoNuevo().doubleValue());
		    		double montoUSDDetalleRev = Redondeador.redondear2Dec(detOrig.getMontoUSD());
		    		if(montoUSDDetalleRev == 0.0 ){
		    			montoUSDDetalleRev = Redondeador.redondear2Dec(getLineaCambioServiceData().getMontoUsdDealDetalle(detOrig));
		    		}
		    		
		    		detOrig.setMonto(montoDetalleOriginal);
					
					
					if(isCambioCliente || detOrig.getStatusDetalleDeal().equals(DealDetalle.STATUS_DET_TOTALMENTE_PAG_LIQ)){
						diferenciaDetalles = diferenciaDetalles + montoUSDDetalleRev;
					}else{
						diferenciaDetalles =  diferenciaDetalles + Redondeador.redondear2Dec(montoUSDDetalleRev - montoUSDDetalleOriginal);
					}
				}
			}
			if (isReversoCambioTC(detRevVO, detOrig)){
				isCambioTC=true;
				detRev.setTipoCambioNuevo(detRevVO.getTipoCambio());
			}
			reverso.getDetalles().add(detRev);
			detRev.setReverso(reverso);
			store(detRev);
		}
		
		//Validar Linea de Credito
		if (!porCancelacion){
			validarMontoLineaCredito(dealOriginal,
									fechaValor, 
									idPersonaLC, 
									diferenciaDetalles, 
									isCambioCliente, 
									isCambioFechaValor,
									isCambioMonto,
									isCambioTC,
									ticket
									);
		}
							
		
		if (dealOriginal.getIdLiquidacion() != null) {
			try {
				SiteService siteService = (SiteService) _appContext
						.getBean("siteService");
				siteService
						.solicitarBloqueo(ticket, new Long(dealOriginal
								.getIdLiquidacion().intValue()), new Long(
								usuario.getIdPersona().intValue()), reverso
								.getObservaciones());
			} catch (SeguridadException e) {
				warn(e.getMessage(), e);
				throw new SicaException(e.getMessage());
			} catch (ExternalSiteException e) {
				warn(e.getMessage(), e);
				throw new SicaException(e.getMessage());
			}
		} else if (!Reverso.STATUS_PENDIENTE.equals(reverso.getStatusReverso())) {
			reverso.setStatusReverso(Reverso.STATUS_PENDIENTE);
			update(reverso);
		}
		dealOriginal.setReversado(Deal.PROC_REVERSO);
		update(dealOriginal);
		for (Iterator it = dealOriginal.getDetalles().iterator(); it.hasNext();) {
			DealDetalle dealDetalle = (DealDetalle) it.next();
			if (!dealDetalle.isLiquidado() && !dealDetalle.isCancelado()) {
				dealDetalle.setReversado(Deal.PROC_REVERSO);
				update(dealDetalle);
			}
		}
		// Se env&iacute;a el correo.
		try {
			ReversoMailSender rms = (ReversoMailSender) _appContext
					.getBean("reversoMailSender");
			ParametroSica parametroSica = findParametro(ParametroSica.CC_EMAIL_SOL_REVERSO);
			String cc = null;
			if (parametroSica != null && parametroSica.getValor() != null) {
				cc = parametroSica.getValor().trim();
				Canal canal = reverso.getDealOriginal().getCanalMesa()
						.getCanal();
				if (!StringUtils.isEmpty(canal.getEmailJefe())) {
					cc += ",";
					cc += canal.getEmailJefe();
				}
			}
			if (cc != null){
				rms.enviarMailReversoPendienteDeAutorizar(reverso, cc.split("\\,"));
			}
		} catch (Exception e) {
			warn("No se pudo enviar el correo para el reverso "
					+ reverso.getIdReverso() + " " + e.getMessage(), e);
		}
   }

	private boolean isReversoCambioTC(DetalleReversoVO detRevVO, DealDetalle detOrig) {
		
		 return detRevVO.getTipoCambio() != null
				&& !detRevVO.getTipoCambio().isNaN()
				&& detOrig.getTipoCambio() != Redondeador
						.redondear6Dec(detRevVO.getTipoCambio().doubleValue());
	}

	private boolean isReversoCambioMonto(DetalleReversoVO detRevVO, DealDetalle detOrig) {
		return detRevVO.getMonto() != null
				&& !detRevVO.getMonto().isNaN()
				&& detOrig.getMonto() != Redondeador.redondear2Dec(detRevVO
						.getMonto().doubleValue());
	}

private void validarReverso(int idDeal, Deal dealOriginal) {
	if (Deal.STATUS_DEAL_CANCELADO.equals(dealOriginal.getStatusDeal())) {
		throw new SicaException("No se puede reversar el deal " + idDeal
				+ " pues se encuentra totalmente cancelado.");
	}
	
	if (dealOriginal.isCancelable()) {
		throw new SicaException(
				"El deal es cancelable, no debe realizarse el reverso.");
	}
}

   /**
    * Para los reversos el Deal de correccion se crea sin Pago Anticipado por lo que en ningun caso
    * se tiene que validar los montos del Deal con Pago Anticipado
    * Validar Montos de la Linea de Credito antes de persistir el Reverso
    * @param dealOriginal
    * @param fechaValor
    * @param montoModificado Valor opcional, solo si se realizo una modificacion de Monto en el reverso
 * @param isCambioTC Bandera que indica si hubo cambio de Tipo de Cambio
 * @param isCambioMonto Bandera que indica si hubo cambio de monto  
 * @param isCambioFechaValor Bandera que indica si hubo cambio de fecha valor 
 * @param isCambioCliente Bandera que indica si hubo cambio de Cliente 
 * @param ticket 
    */
   private boolean validarMontoLineaCredito(Deal dealOriginal, 
											   String fechaValor, 
											   Integer idPersona, 
											   double montoModificado, 
											   boolean isCambioCliente,
											   boolean isCambioFechaValor, 
											   boolean isCambioMonto, 
											   boolean isCambioTC, String ticket) {
		boolean validado = false;
		Date fechaLiquidacion = new Date();
		if (isCambioFechaValor) {
			fechaLiquidacion = calcularFechaLiquidacion(fechaValor,
					dealOriginal.getFechaCaptura());
		} else {
			fechaLiquidacion = calcularFechaLiquidacion(
					dealOriginal.getTipoValor(), dealOriginal.getFechaCaptura());
		}
		fechaValor = calcularFechaValorParaFechasCapturaLiquidacion(new Date(),
				fechaLiquidacion);
		
		getLineaCambioServiceData().validarLineaCreditoReverso(idPersona,
				dealOriginal, fechaValor, montoModificado, isCambioCliente,
				isCambioFechaValor, isCambioMonto, isCambioTC, ticket);
		validado = true;
	return validado;
   }
  

/**
    * Regresa el detalle de deal que corresponde al folioDetalle especificado.
    *
    * @param dealVO       El deal a inspeccionar.
    * @param folioDetalle El n&uacute;mero de detalle que desea encontrarse.
    * @return DealDetalleVO.
    * @throws SicaException Si no hay un detalle de deal con el folioDetalle especificado.
    */
   private DealDetalleVO getDealDetalleVO(DealVO dealVO, int folioDetalle) throws SicaException {
       for (Iterator it = dealVO.getDetalles().iterator(); it.hasNext();) {
           DealDetalleVO detVO = (DealDetalleVO) it.next();
           if (detVO.getFolioDetalle() == folioDetalle) {
               return detVO;
           }
       }
       throw new SicaException("No se encontr\u00f3 el detalle " + folioDetalle + " del deal " +
               dealVO.getIdDeal());
   }

   /**
    * Calcula y regresa la fecha de liquidaci&oacute;n con respecto a la fecha de captura para la
    * fecha valor especificada.
    *
    * @param fechaValor   La fecha valor a la que debe realizarse la liquidaci&oacute;n.
    * @param fechaCaptura La fecha de captura del deal.
    * @return Date.
    * @throws SicaException Si la fecha valor especificada no es una opci&oacute;n v&aacute;lida.
    * @throws SicaFechaValorException Si no se puede calcular la fecha de liquidaci&oacute;n.
    */
   private Date calcularFechaLiquidacion(String fechaValor, Date fechaCaptura)
           throws SicaException {
       PizarronServiceData psd = (PizarronServiceData) _appContext.
               getBean("jtaPizarronServiceData");
       Date fechaLiquidacion;
       if (Constantes.CASH.equals(fechaValor.trim())) {
           fechaLiquidacion = psd.getFechaOperacion(fechaCaptura);
       }
       else if (Constantes.TOM.equals(fechaValor.trim())) {
           fechaLiquidacion = psd.getFechaTOM(fechaCaptura);
       }
       else if (Constantes.SPOT.equals(fechaValor.trim())) {
           fechaLiquidacion = psd.getFechaSPOT(fechaCaptura);
       }
       else if (Constantes.HR72.equals(fechaValor.trim())) {
           fechaLiquidacion = psd.getFecha72HR(fechaCaptura);
       }
       else if (Constantes.VFUT.equals(fechaValor.trim())) {
           fechaLiquidacion = psd.getFechaVFUT(fechaCaptura);
       }
       else {
           throw new SicaException("La Fecha Valor " + fechaValor + " es desconocida.");
       }
       if (fechaLiquidacion.getTime() < DateUtils.inicioDia().getTime()) {
           throw new SicaFechaValorException("La fecha de liquidaci\u00f3n queda en el pasado.");
       }
       return fechaLiquidacion;
   }

   /**
    * Modifica los status del deal original o deal de balanceo.
    *
    * @param dealVO         El deal a revisar.
    * @param esDealOriginal True para el deal original, False para el deal contrario o de balanceo.
    */
   private void cambiarStatusParaReverso(DealVO dealVO, boolean esDealOriginal) {
       int cuantosAlta = 0;
       for (Iterator it = dealVO.getDetalles().iterator(); it.hasNext();) {
           DealDetalleVO dealDetalleVO = (DealDetalleVO) it.next();
           if (DealDetalle.STATUS_DET_TOTALMENTE_PAG_LIQ.
                   equals(dealDetalleVO.getStatusDetalleDeal())) {
               if (!esDealOriginal) {
                   dealDetalleVO.setStatusDetalleDeal(DealDetalle.STATUS_DET_PROCESO_CAPTURA);
                   dealDetalleVO.setDescripcionStatus("Alta");
                   cuantosAlta++;
               }
           }
           else if (!DealDetalle.STATUS_DET_CANCELADO.
                   equals(dealDetalleVO.getStatusDetalleDeal())) {
               dealDetalleVO.setReversado(Deal.REVERSADO);
               dealDetalleVO.setStatusDetalleDeal(DealDetalle.STATUS_DET_TOTALMENTE_PAG_LIQ);
               dealDetalleVO.setDescripcionStatus("Reversado");
           }
       }
       if (esDealOriginal || cuantosAlta == 0) {
           dealVO.setStatusDeal("RE");
           dealVO.setDescripcionStatus("Reversado");
       }
   }

   /**
    * Crea el value object para el deal de balanceo o contrario, de acuerdo al deal original
    * especificado.
    *
    * @param ticket       El ticket de la sesi&oacute;n del usuario.
    * @param dealOriginal El deal original que se va a reversar.
    * @return DealVO.
    * @throws SicaException Si no se puede crear el deal.
    */
   private DealVO crearDealContrarioVO(String ticket, Deal dealOriginal) throws SicaException {
       PizarronServiceData psd = (PizarronServiceData) _appContext.
               getBean("jtaPizarronServiceData");
       DealVO dealContrarioVO = new DealVO(dealOriginal, true);
       dealContrarioVO.setFechaCaptura(Constantes.DATE_FORMAT.format(new Date()));
       String fechaValor = psd.fechaValorParaCancelacion(dealOriginal.getFechaCaptura(),
               dealOriginal.getTipoValor(), true);
       if (Constantes.CASH.equals(fechaValor)) {
           dealContrarioVO.setFechaLiquidacion(
                   Constantes.DATE_FORMAT.format(DateUtils.inicioDia()));
       }
       dealContrarioVO.setFechaValor(fechaValor);
       cambiarStatusParaReverso(dealContrarioVO, false);
       if (Deal.REVERSADO != dealContrarioVO.getReversado()) {
           dealContrarioVO.setStatusDeal(Deal.STATUS_DEAL_PROCESO_CAPTURA);
           dealContrarioVO.setDescripcionStatus("Alta");
       }
       // Se asignan los mnemonicos contrarios a los detalles:
       FormasPagoLiqService fplService = (FormasPagoLiqService) _appContext.
               getBean("formasPagoLiqService");
       for (Iterator it = dealContrarioVO.getDetalles().iterator(); it.hasNext();) {
           DealDetalleVO detVO = (DealDetalleVO) it.next();
           if (detVO.getMnemonico() != null) {
               FormaPagoLiq fplOrig = fplService.getFormaPagoLiq(ticket, detVO.getMnemonico());
               FormaPagoLiq fplContrario = fplService.getFormaPagoLiq(ticket,
                       fplOrig.getIdReverso());
               detVO.setMnemonico(fplContrario.getMnemonico());
           }
           detVO.setDatosAdicionales(new ArrayList());
       }
       return dealContrarioVO;
   }

   /**
    * Calcula la fecha valor adecuada con respecto a una fecha de captura y liquidaci&oacute;n
    * determinadas.
    *
    * @param fechaCaptura     La fecha de captura para un deal.
    * @param fechaLiquidacion La fecha de liquidaci&oacute;n de un deal.
    * @return String.
    * @throws SicaFechaValorException Si no es posible calcular la fecha valor.
    */
   private String calcularFechaValorParaFechasCapturaLiquidacion(Date fechaCaptura,
                                                                 Date fechaLiquidacion)
           throws SicaFechaValorException {
       PizarronServiceData psd = (PizarronServiceData) _appContext.
               getBean("jtaPizarronServiceData");
       return psd.calcularFechaValorParaFechasCapturaLiquidacion(fechaCaptura, fechaLiquidacion);
   }

   /**
    * Crea el value object para el deal de correcci&oacute;n, de acuerdo al deal original
    * especificado.
    *
    * @param dealOriginal El deal original que se va a reversar.
    * @param reverso      El reverso.
    * @return DealVO.
    * @throws SicaException Si no se puede crear el deal.
    */
   private DealVO crearDealCorreccionVO(Deal dealOriginal, Reverso reverso)
           throws SicaException {
       DealVO dealCorreccionVO = new DealVO(dealOriginal, false);
       dealCorreccionVO.setFechaCaptura(Constantes.DATE_FORMAT.format(new Date()));
       ContratoSica nvoContratoSica = reverso.getPorContratoSica();
       if (nvoContratoSica != null) {
           dealCorreccionVO.setNoCuenta(nvoContratoSica.getNoCuenta());
           dealCorreccionVO.setReversoPorCliente(true);
           Persona cte = ContratoCliente.clienteParaContratoSica(nvoContratoSica);
           dealCorreccionVO.setNombreCliente(cte.getNombreCompleto());
       }
       try {
           Date fechaLiquidacion;
           if (reverso.getPorFechaValor() != null) {
               dealCorreccionVO.setReversoPorFechaValor(true);
               fechaLiquidacion = calcularFechaLiquidacion(reverso.getPorFechaValor(),
                       dealOriginal.getFechaCaptura());
           }
           else {
               fechaLiquidacion = calcularFechaLiquidacion(dealOriginal.getTipoValor(),
                       dealOriginal.getFechaCaptura());
           }
           dealCorreccionVO.setFechaLiquidacion(
                   Constantes.DATE_FORMAT.format(fechaLiquidacion));
           dealCorreccionVO.setFechaValor(calcularFechaValorParaFechasCapturaLiquidacion(
                   new Date(), fechaLiquidacion));
       }
       catch (SicaFechaValorException e) {
           dealCorreccionVO.setFechaValor(Constantes.CASH);
           dealCorreccionVO.setFechaLiquidacion(
                   Constantes.DATE_FORMAT.format(DateUtils.inicioDia()));
           warn(e.getMessage(), e);
       }
       // Revisamos los cambios a los detalles:
       for (Iterator it = reverso.getDetalles().iterator(); it.hasNext();) {
           ReversoDetalle revDetalle = (ReversoDetalle) it.next();
           DealDetalleVO detVO = getDealDetalleVO(dealCorreccionVO,
                   revDetalle.getDealDetalle().getFolioDetalle());
           if (revDetalle.getMontoNuevo() != null) {
               detVO.setMonto(Redondeador.redondear2Dec(
                       revDetalle.getMontoNuevo().doubleValue()));
               detVO.setMontoModificado(revDetalle.getMontoNuevo() != null);
           }
           if (revDetalle.getTipoCambioNuevo() != null) {
               detVO.setTipoCambio(Redondeador.redondear6Dec(
                       revDetalle.getTipoCambioNuevo().doubleValue()));
               detVO.setTipoCambioModificado(true);
           }
           detVO.setImporte(detVO.getMonto() * detVO.getTipoCambio());
       }
       if (!reverso.getDetalles().isEmpty()) {
           // Si es de pesos el detalle, debe quitarse pues de lo contrario se
           // desbalancear&iacute;a el deal:
           for (Iterator it = dealCorreccionVO.getDetalles().iterator(); it.hasNext();) {
               DealDetalleVO detVO = (DealDetalleVO) it.next();
               if (Divisa.PESO.equals(detVO.getIdDivisa())) {
                   if (detVO.isRecibimos()) {
                       dealCorreccionVO.getDetallesRecibimos().remove(detVO);
                   }
                   else {
                       dealCorreccionVO.getDetallesEntregamos().remove(detVO);
                   }
               }
           }
       }
       // Marcamos en alta los detalles del deal de correccion:
       for (Iterator it = dealCorreccionVO.getDetalles().iterator(); it.hasNext();) {
           DealDetalleVO detVO = (DealDetalleVO) it.next();
           if (!DealDetalle.STATUS_DET_CANCELADO.equals(detVO.getStatusDetalleDeal())) {
               detVO.setStatusDetalleDeal(DealDetalle.STATUS_DET_PROCESO_CAPTURA);
               detVO.setDescripcionStatus("Alta");
           }
       }
       dealCorreccionVO.setStatusDeal(Deal.STATUS_DEAL_PROCESO_CAPTURA);
       dealCorreccionVO.setDescripcionStatus("Alta");
       return dealCorreccionVO;
   }

   /**
    * Si el valor de datosAdicionales no est&aacute; vac&iacute;o, actualiza los tipos de cambio de
    * la mesa para los detalles del deal, de acuerdo a la informaci&oacute;n ah&iacute; contenida,
    * que proviene del autorizador de reversos.
    *
    * @param rev     El reverso.
    * @param deal    El deal que se quiere editar (de balanceo o de correcci&oacute;n).
    * @param prefijo 'B' para balanceo, 'C' para correcci&oacute;n.
    */
   private void revisarTcm(Reverso rev, DealVO deal, char prefijo) {
       if (rev.getDatosAdicionales() != null && rev.getDatosAdicionales().trim().length() > 0) {
           String[] infoDets = rev.getDatosAdicionales().split("\\|");
           for (int i = 0; i < infoDets.length; i++) {
               String[] infoDet = infoDets[i].split("\\@");
               if (infoDet.length == 3 && infoDet[0].charAt(0) == prefijo) {
                   DealDetalleVO det = deal.getDetalleConFolio(
                           Integer.valueOf(infoDet[1]).intValue());
                   det.setTipoCambioMesa(Double.parseDouble(infoDet[2]));
               }
           }
       }
   }

   /**
    * @see com.ixe.ods.sica.sdo.ReversosServiceData#prepararDealsReverso(String, int).
    * @see #crearDealContrarioVO(String, com.ixe.ods.sica.model.Deal).
    * @see #crearDealCorreccionVO(com.ixe.ods.sica.model.Deal, com.ixe.ods.sica.model.Reverso).
    */
   public List prepararDealsReverso(String ticket, int idReverso) throws SicaException {
       List reversos = new ArrayList(new HashSet(getHibernateTemplate().find("FROM Reverso AS r " +
               "LEFT JOIN FETCH r.porContratoSica AS cs LEFT JOIN FETCH cs.roles AS pcr LEFT " +
               "JOIN FETCH pcr.id.persona AS p LEFT JOIN FETCH r.detalles WHERE r.idReverso = ?",
               new Integer(idReverso))));
       if (reversos.isEmpty()) {
           throw new SicaException("No se encontr\u00f3 el reverso con n\u00famero " + idReverso);
       }
       Reverso reverso = (Reverso) reversos.get(0);
       inicializarReverso(reverso);
       if (!Reverso.STATUS_PENDIENTE.equals(reverso.getStatusReverso())) {
           throw new SicaValidationException("El reverso " + idReverso + " no se encuentra en " +
                   "status pendiente de autorizar.");
       }
       List deals = new ArrayList();
       Deal dealOriginal = findDeal(reverso.getDealOriginal().getIdDeal());
       DealVO dealOriginalVO = new DealVO(dealOriginal, false);
       cambiarStatusParaReverso(dealOriginalVO, true);
       deals.add(dealOriginalVO);
       // Se prepara el deal contrario:
       DealVO dealContrarioVO = crearDealContrarioVO(ticket, dealOriginal);
       revisarTcm(reverso, dealContrarioVO, 'B');
       deals.add(dealContrarioVO);
       // Se prepara el deal de correccion, si es necesario:
       if (!reverso.isPorCancelacion()) {
           DealVO dealCorreccionVO = crearDealCorreccionVO(dealOriginal, reverso);
           revisarTcm(reverso, dealCorreccionVO, 'C');
           deals.add(dealCorreccionVO);
       }
       return deals;
   }

   /**
    * @see com.ixe.ods.sica.sdo.ReversosServiceData#autorizarReverso(String,
    *      com.ixe.ods.sica.vo.ReversoVO, java.util.List, int).
    */
   public String autorizarReverso(String ticket, ReversoVO reversoVO, List deals, int idUsuario)
           throws SicaException {
       Reverso reverso = findReverso(reversoVO.getIdReverso());
       if (!Reverso.STATUS_PENDIENTE.equals(reverso.getStatusReverso())) {
           throw new SicaException("El reverso no se encuentra en status 'Pendiente'. " +
                   "Probablemente otra persona ya lo autoriz\u00f3.");
       }
       
       Deal dealOriginal = findDeal(reversoVO.getIdDealOriginal());
       // Se cambian los status de 'En Proceso de Reverso' por 'Reversado':
       dealOriginal.setReversado(Deal.REVERSADO);
       dealOriginal.setStatusDeal(Deal.STATUS_DEAL_TOTALMENTE_LIQUIDADO);
       Swap swap = dealOriginal.getSwap();
       boolean dealOrigInicioSwap = false;
       // Si el deal original esta en un swap, se desliga de este.
       if (swap != null) {
           dealOrigInicioSwap = dealOriginal.getSwap().getFolioSwapInicio().intValue() ==
                   dealOriginal.getIdDeal();
           dealOriginal.setSwap(null);
       }
       
       for (Iterator it = dealOriginal.getDetalles().iterator(); it.hasNext();) {
           DealDetalle detOrig = (DealDetalle) it.next();
           if (detOrig.isReversadoProcesoReverso()) {
               detOrig.setReversado(Deal.REVERSADO);
               detOrig.setStatusDetalleDeal(DealDetalle.STATUS_DET_TOTALMENTE_PAG_LIQ);
              
               if ((dealOriginal.isCompra() && detOrig.isRecibimos() && !detOrig.isCancelado()) || 
            		   (!dealOriginal.isCompra() && !detOrig.isRecibimos() && !detOrig.isCancelado())) {
            	  
                  	getLineaCambioServiceData().liberarLineaCreditoDealDetalle(ticket, detOrig);
            	   
               }
              
               update(detOrig);
               try
               {
            	   validarInformacionRegulatoria(dealOriginal, detOrig.getDivisa(), detOrig, detOrig.getStatusDetalleDeal());
               }
               catch(Exception e)
               {
            	   logger.error("Ocurrio un error al validar la informacion regulatoria del detalle " +
            			        " (idDealPosicion: " + detOrig.getIdDealPosicion() + ", deal: " + dealOriginal.getIdDeal() + 
            			        ") --  que fue actualizado como reversado. " +
            			        "Es probable que no se haya enviado el email.");
               }
           }
           
           try
           {
        	   getH2hService().solicitarCancelacionDetalleH2H(detOrig, H2HService.ES_REVERSO);
           }catch(Exception e){e.printStackTrace();}
       }
       update(dealOriginal);
       validarDealParaReverso(dealOriginal);
       List fpls = getDealService().getFormasPagoLiqService().getFormasTiposLiq(ticket);
       int i = 0;
       for (Iterator it = deals.iterator(); it.hasNext(); i++) {
           DealVO dealVO = (DealVO) it.next();
           Deal deal;
           if (i == 1) {
               // Crear el deal de balanceo:
        	   
               deal = crearDeal(ticket, dealVO, dealOriginal, true, false);
               dealOriginal.setOriginalDe(new Integer(deal.getIdDeal()));
               update(dealOriginal);
               reverso.setDealBalanceo(deal);
               if (deal.isProcesable(fpls)) {
                   try {
                       wfIniciarProcesoDeal(ticket, reverso.getDealBalanceo().getIdDeal());
                   }
                   catch (SicaException e) {
                       e.printStackTrace();
                       throw new SicaException(e.getMessage());
                   }
               }
           }
           else if (i == 2) {
               // Crear el deal de correcci&oacute;n.
               //deal = crearDeal(ticket, dealVO, dealOriginal, false);
               if (reverso.getPorContratoSica() != null) {
                   // Crear el deal de correcci&oacute;n.
                   deal = crearDeal(ticket, dealVO, dealOriginal, false, true);
            	   
                   //Se eliminan las plantillas del deal original si es por cliente
                   for (Iterator detsIt = deal.getDetalles().iterator(); detsIt.hasNext();) {
                       DealDetalle det = (DealDetalle) detsIt.next();
                       if (det.getPlantilla() != null) {
                           det.setPlantilla(null);
                       }
                   }
               }else{
                   // Crear el deal de correcci&oacute;n.
                   deal = crearDeal(ticket, dealVO, dealOriginal, false, false);
               }
               
               // Balanceamos si es necesario:
               if (deal.isInterbancario() && deal.getBalance() != 0.0) {
                   insertarMxn(ticket, deal, deal.getBalance() < 0.0, null,
                           Math.abs(Redondeador.redondear2Dec(deal.getBalance())));
               }
               reverso.setDealCorreccion(deal);
               // Ligamos el deal de correccion al swap, si es que existe:
               if (swap != null) {
                   deal.setSwap(swap);
                   update(deal);
                   if (dealOrigInicioSwap) {
                       swap.setFolioSwapInicio(new Integer(deal.getIdDeal()));
                       update(swap);
                   }
               }
           }
       }
       reverso.setFechaAutorizacion(new Date());
       IUsuario usuario = (IUsuario) find(Usuario.class, new Integer(idUsuario));
       reverso.setUsuarioAutorizacion(usuario);
       reverso.setStatusReverso(Reverso.STATUS_COMPLETO);
       update(reverso);
       // Se env&iacute;a el correo.
       try {
           ReversoMailSender rms = (ReversoMailSender) _appContext.getBean("reversoMailSender");
           ParametroSica parametroSica;
           Date fechaVFUTParaDealOriginal = getPizarronServiceData().
                   getFechaVFUT(dealOriginal.getFechaCaptura());
           Date fechaSistema = getPizarronServiceData().getFechaOperacion();
           //Si la fecha de captura es mayor a T+3, se notifica al SITE. 
           if (Constantes.VFUT.equals(dealOriginal.getTipoValor()) ||
                   fechaVFUTParaDealOriginal.getTime() < fechaSistema.getTime()) {
               parametroSica = findParametro(ParametroSica.CC_EMAIL_REVERSO_INCL_SITE);
           }
           else {
               parametroSica = findParametro(ParametroSica.CC_EMAIL_REVERSO);
           }
           String cc = parametroSica.getValor().trim();
           Canal canal = reverso.getDealOriginal().getCanalMesa().getCanal();
           if (!StringUtils.isEmpty(canal.getEmailJefe())) {
               cc += ",";
               cc += canal.getEmailJefe();
           }
           rms.enviarMailReverso(reverso, cc.split("\\,"));
       }
       catch (SicaException e) {
           warn(e.getMessage(), e);
       }
       catch (Exception e) {
           warn(e.getMessage(), e);
       }
       return "Se autoriz\u00f3 el reverso n\u00famero " + reverso.getIdReverso() +
               " para el deal: " + dealOriginal.getIdDeal() + " con deal de balanceo: " +
               reverso.getDealBalanceo().getIdDeal() +
               (reverso.getDealCorreccion() != null ? " y deal de correcci\u00f3n: " +
                       reverso.getDealCorreccion().getIdDeal() + "." : ".");
   }

   /**
    * Crea un deal para la autorizaci&oacute;n de reversos.
    *
    * @param ticket       El ticket de la sesi&oacute;n del usuario.
    * @param dealVO       El deal base.
    * @param dealOriginal El deal original.
    * @param balanceo     True para generar el deal de balanceo, false para generar el de
    *                     correcci&oacute;n.
    * @param revPorCliente True para indicar si la causa del reverso fue por cambio de Cliente o ContratoSica
    * @return Deal.
    * @throws SicaException Si no es posible crear el deal.
    */
   private Deal crearDeal(String ticket, DealVO dealVO, Deal dealOriginal, boolean balanceo, boolean revPorCliente)
           throws SicaException {
       try {
           Deal deal = new Deal();
           // Si es el de correccion y es interbancario, se le pone la instruccion de pago
           // anticipado del deal original:
           if (!balanceo && dealOriginal.isInterbancario()) {
               deal.setPagoAnticipado(dealOriginal.isPagoAnticipado());
           }
           deal.setFechaCaptura(new Date());
           deal.setFechaLiquidacion(Constantes.DATE_FORMAT.parse(dealVO.getFechaLiquidacion()));
           deal.setTipoValor(dealVO.getFechaValor());
           // Se asigna el promotor:
           if (dealVO.getIdPromotor() != null) {
               List emps = getHibernateTemplate().find("FROM EmpleadoIxe AS ei WHERE " +
                       "ei.idPersona = ?", dealVO.getIdPromotor());
               if (emps.isEmpty()) {
                   throw new SicaException("No se encontr\u00f3 el promotor con n\u00famero ");
               }
               deal.setPromotor((EmpleadoIxe) emps.get(0));
           }
           // Se asigna el usuario:
           List usrs = getHibernateTemplate().find("FROM Usuario AS u WHERE u.idUsuario = ?",
                   new Integer(dealVO.getIdUsuario()));
           if (usrs.isEmpty()) {
               throw new SicaException("No se encontr\u00f3 el usuario con n\u00famero " +
                       dealVO.getIdUsuario());
           }
           deal.setUsuario((IUsuario) usrs.get(0));
           if (dealVO.getNoCuenta() != null) {
               deal.setContratoSica(findContrato(dealVO.getNoCuenta()));
           }
           deal.setTipoDeal(dealOriginal.getTipoDeal());
           deal.setCanalMesa(dealOriginal.getCanalMesa());
           deal.setAcudirCon(dealOriginal.getAcudirCon());
           deal.setBroker(dealOriginal.getBroker());
           deal.setCompra(balanceo ? !dealOriginal.isCompra() : dealOriginal.isCompra());
           if (balanceo) {
               deal.setConFactura(false);
               deal.setEnviarAlCliente(false);
               deal.setReversado(Deal.REVERSADO);
               deal.setContrarioDe(new Integer(dealOriginal.getIdDeal()));
           }
           else {
               deal.setCorreccion(new Integer(dealOriginal.getIdDeal()));
               if(!revPorCliente)
               {
            	   deal.setConFactura(dealOriginal.isConFactura());
            	   deal.setEnviarAlCliente(dealOriginal.isEnviarAlCliente());
            	   deal.setDirFactura(dealOriginal.getDirFactura());
            	   deal.setEmailFactura(dealOriginal.getEmailFactura());
            	   deal.setEmailsComprobantes(dealOriginal.getEmailsComprobantes());
               }
           }
           deal.setDireccion(dealOriginal.getDireccion());
           deal.setLiquidacionAnticipada(dealOriginal.getLiquidacionAnticipada());
           deal.setLiquidacionEspecial(dealOriginal.getLiquidacionEspecial());
           deal.setMensajeria(dealOriginal.isMensajeria());
           deal.setNombreFactura(!revPorCliente ? dealOriginal.getNombreFactura() : null);
           deal.setObservaciones(dealOriginal.getObservaciones());
           deal.setRfcFactura(!revPorCliente ? dealOriginal.getRfcFactura() : null);
           // Los detalles de deal:
           for (Iterator it = dealVO.getDetalles().iterator(); it.hasNext();) {
               DealDetalleVO detVO = (DealDetalleVO) it.next();
               DealDetalle det;
               if (detVO.getIdDivisa().equals(dealOriginal.getCanalMesa().getMesaCambio().
                       getDivisaReferencia().getIdDivisa())) {
                   IPlantilla plantilla = null;
                   //Detalle de Pesos
                   det = insertarMxn(ticket, deal, detVO.isRecibimos(), detVO.getMnemonico(),
                           detVO.getMonto());
                   //Agrega plantilla Generica
                   if (balanceo) {
                       String mnemonico = detVO.getMnemonico();
                       if (mnemonico != null &&
                               getFormasPagoLiqService().getFormaPagoLiq(ticket, mnemonico).
                                       getUsaPlantilla().booleanValue()) {
                           asignarPlantillaReverso(detVO, det, plantilla);
                       }
                       else {
                           //Si no usa plantilla el mnemonico, se asigna null a la plantilla del
                           // detalle
                           det.setPlantilla(null);
                       }
                   }
               }else{
                   IPlantilla plantilla = null;
                   //Detalle de Divisa
                   TipoOperacionDealDto operacionDeal = new TipoOperacionDealDto();
                   if(!balanceo){
	                   operacionDeal.setReverso(true);
                   }
                   det = insertarDivisa(ticket, deal, detVO.isRecibimos(), dealVO.getFechaValor(),
                           detVO.getClaveFormaLiquidacion(), detVO.getIdDivisa(),
                           detVO.getTipoCambioMesa(), detVO.getMonto(), detVO.getTipoCambio(),
                           detVO.getPrecioReferenciaMidSpot(), detVO.getPrecioReferenciaSpot(),
                           detVO.getFactorDivisa(),
                           detVO.getIdSpread(), detVO.getIdPrecioReferencia(), detVO.getIdFactorDivisa(),
                           detVO.getMnemonico(), true, plantilla, null, operacionDeal);
                   //Agrega plantilla Generica
                   if (balanceo) {
                       String mnemonico = detVO.getMnemonico();
                       if (mnemonico != null &&
                               getFormasPagoLiqService().getFormaPagoLiq(ticket, mnemonico).
                                       getUsaPlantilla().booleanValue()) {
                           asignarPlantillaReverso(detVO, det, plantilla);
                       }else{
                           det.setPlantilla(null);
                       }
                   }else{
                       if (detVO.getIdPlantilla() != null) {
                           plantilla = (IPlantilla) find(Plantilla.class, detVO.getIdPlantilla());
                           det.setPlantilla(plantilla);
                       }
                   }
               }
               det.setComisionCobradaMxn(detVO.getComisionCobradaMxn());
               det.setComisionCobradaUsd(detVO.getComisionCobradaUsd());
               det.setComisionOficialUsd(detVO.getComisionOficialUsd());
               det.setStatusDetalleDeal(detVO.getStatusDetalleDeal());
               // Esto es para que no llegue al site:
               if (Deal.REVERSADO == detVO.getReversado()) {
                   det.setEvento(Deal.EV_APROBACION, DealDetalle.EV_IND_GRAL_REVERSO_NO_LIQUIDAR);
                   det.setReversado(Deal.REVERSADO);
               }
               update(det);
           }
           deal.setCompra(balanceo ? !dealOriginal.isCompra() : dealOriginal.isCompra());
           // Si es el de balanceo y no tiene detalles en alta, debe marcarse como Reversado:
           if (balanceo) {
               int altas = 0;
               for (Iterator it = deal.getDetalles().iterator(); it.hasNext();) {
                   DealDetalle detalle = (DealDetalle) it.next();
                   if (DealDetalle.STATUS_DET_PROCESO_CAPTURA.
                           equals(detalle.getStatusDetalleDeal())) {
                       altas++;
                       break;
                   }
               }
               if (altas == 0) {
                   deal.setReversado(Deal.REVERSADO);
                   deal.setStatusDeal(Deal.STATUS_DEAL_TOTALMENTE_LIQUIDADO);
               }
           }
           update(deal);
           return deal;
       }
       catch (ParseException e) {
           throw new SicaException("El formato de la fecha de liquidaci\u00f3n no es correcto: " +
                   dealVO.getFechaLiquidacion());
       }
       catch (SicaException e) {
           if (logger.isWarnEnabled()) {
               logger.warn(e.getMessage(), e);
           }
           throw e;
       }
   }

   /**
    * @see com.ixe.ods.sica.sdo.ReversosServiceData#negarReverso(String,
    *      com.ixe.ods.sica.vo.ReversoVO, int, boolean).
    */
   public String negarReverso(String ticket, ReversoVO reversoVO, int idUsuario,
                              boolean deniegaMesa) throws SicaException {
       Reverso reverso = findReverso(reversoVO.getIdReverso());
       if (!Reverso.STATUS_PENDIENTE.equals(reverso.getStatusReverso())) {
           throw new SicaException("El reverso no se encuentra en status 'Pendiente'. " +
                   "Probablemente otra persona ya lo autoriz\u00f3.");
       }
       reverso.setStatusReverso(Reverso.STATUS_CANCELADO);
       update(reverso);
       // Cambiamos los status reversados del deal original:
       Deal dealOrig = reverso.getDealOriginal();
       dealOrig.setReversado(Deal.NO_REVERSADO);
       dealOrig.setStatusDeal(Deal.STATUS_DEAL_PROCESO_CAPTURA);
       update(dealOrig);
       for (Iterator it = dealOrig.getDetalles().iterator(); it.hasNext();) {
           DealDetalle det = (DealDetalle) it.next();
           if (det.isReversadoProcesoReverso()) {
               det.setStatusDetalleDeal(DealDetalle.STATUS_DET_PROCESO_CAPTURA);
               det.setReversado(Deal.NO_REVERSADO);
               update(det);
           }
       }
       //Si la mesa deniega tiene que pedir el desbloqueo de la liquidacion.
       if (deniegaMesa) {
           if (dealOrig.getIdLiquidacion() != null) {
               try {
                   SiteService siteService = (SiteService) _appContext.getBean("siteService");
                   IUsuario usuario = (IUsuario) find(Usuario.class, new Integer(idUsuario));
                   siteService.desbloquearDeal(ticket,
                           new Long(dealOrig.getIdLiquidacion().intValue()),
                           new Long(usuario.getPersona().getIdPersona().intValue()), "");
               }                           
               catch (SeguridadException e) {
                   warn(e.getMessage(), e);
                   throw new SicaException(e.getMessage(), e);
               }
               catch (ExternalSiteException e) {
                   warn(e.getMessage(), e);
                   throw new SicaException(e.getMessage(), e);
               }
           }
       }
       // Se env&iacute;a el correo.
       try {
           ReversoMailSender rms = (ReversoMailSender) _appContext.getBean("reversoMailSender");
           rms.enviarMailReverso(reverso, null);
           return "Se ha denegado la autorizaci\u00f3n del reverso para el deal original: " +
                   dealOrig.getIdDeal();
       }
       catch (Exception e) {
           if (logger.isWarnEnabled()) {
               logger.warn(e.getMessage(), e);
           }
           return "Se ha denegado la autorizaci\u00f3n del reverso para el deal " +
                   "original: " + dealOrig.getIdDeal() + ", pero no fue posible enviar el correo.";
       }
   }

   /**
    * @see com.ixe.ods.sica.sdo.ReversosServiceData#findReversosCapturados(java.util.Date,
    *      java.util.Date).
    */
   public List findReversosCapturados(final Date fechaInicio, final Date fechaFin)
           throws SicaException {
       List reversos = getHibernateTemplate().executeFind(new HibernateCallback() {
           public Object doInHibernate(Session session) throws HibernateException, SQLException {
               Query q = session.createQuery("FROM Reverso AS r " +
               "INNER JOIN FETCH r.dealOriginal LEFT JOIN FETCH r.dealBalanceo LEFT JOIN FETCH " +
               "r.dealCorreccion INNER JOIN FETCH r.usuario AS u LEFT JOIN FETCH u.persona " +
               "LEFT JOIN FETCH r.porContratoSica LEFT JOIN FETCH r.detalles det LEFT JOIN " +
               "FETCH det.dealDetalle dd LEFT JOIN FETCH dd.divisa WHERE r.fecha BETWEEN " +
               "? AND ?");
               q.setParameter(0, DateUtils.inicioDia(fechaInicio));
               q.setParameter(1, DateUtils.finDia(fechaFin));
               q.setMaxResults(1500);
               return q.list();
           }
       });
       if (reversos.isEmpty()) {
           throw new SicaException("No se encontraron reversos registrados en las fechas " +
                   "especificadas.");
       }
       Collections.sort(reversos, new Comparator() {
           public int compare(Object o, Object o1) {
               return new Integer(((Reverso) o).getIdReverso()).
                       compareTo(new Integer(((Reverso) o1).getIdReverso()));
           }
       });
       return reversos;
   }

   /**
    * Obtiene el Parametro Sica correspondiente al Mnemonico definido.
    *
    * @param mnemonico El mnemonico de la plantilla.
    * @return ParametroSica
    */
   private ParametroSica obtenerIdPlantillaGenerica(String mnemonico) {
       String criterio = "PL_GEN_" + mnemonico;
       List params = getHibernateTemplate().find("FROM ParametroSica AS ps WHERE ps.idParametro " +
               "LIKE ?", criterio);
       if (params.isEmpty()) {
           throw new SicaException("No se encontr\u00f3 la plantilla gen\u00earica para el " +
                   "mnem\u00f3nico " + mnemonico + ".");
       }
       return (ParametroSica) params.get(0);
   }

   /**
    * Asigna la plantilla que le corresponde al Mnemonico del VO que viene
    * de la pantalla de Autorizaciones en la seccion de reversos.
    *
    * @param detVO     El Detalle VO con el mnemonico para el deal de balanceo.
    * @param det       El nuevo detalle para el deal de balanceo.
    * @param plantilla La plantilla para el nuevo detalle del deal de balanceo
    * @throws SicaException Si no se puede iniciar el procesamiento del deal.
    */
   private void asignarPlantillaReverso(DealDetalleVO detVO, DealDetalle det,
                                        IPlantilla plantilla) throws SicaException {
       PlantillaPantalla pp = findPlantillaPantallaByMnemonico(detVO.getMnemonico());
       //Para Plantillas de Tipo Cuenta Ixe
       if (PlantillaPantalla.CAPTURA_CUENTA_IXE.equals(pp.getNombrePantalla())) {
           det.setPlantilla(null);
       }
       //Para cualquier otro Tipo de Plantilla
       else {
           ParametroSica idPlantillaGenerica = obtenerIdPlantillaGenerica(detVO.getMnemonico());
           if (!"0".equals(idPlantillaGenerica.getValor())) {
               plantilla = (Plantilla) find(Plantilla.class,
                       new Integer(idPlantillaGenerica.getValor()));
               det.setPlantilla(plantilla);
           }
           else {
               throw new SicaException("No es posible autorizar el Reverso debido a que no " +
                       "se encontr\u00f3 una plantilla generica para el detalle del deal de " +
                       "balanceo. Por favor consulte a Sistemas.");
           }
       }
   }

   /**
    * Obtiene la referencia al bean de formasPagoLiq.
    *
    * @return FormasPagoLiqService
    */
   public FormasPagoLiqService getFormasPagoLiqService() {
       return formasPagoLiqService;
   }

   /**
    * Asigna el valor a _formasPagoLiqService.
    *
    * @param formasPagoLiqService El valor del bean.
    */
   public void setFormasPagoLiqService(FormasPagoLiqService formasPagoLiqService) {
       this.formasPagoLiqService = formasPagoLiqService;
   }

   /**
    * Obtiene la referencia al bean de pizarronServiceData.
    *
    * @return PizarronServiceData
    */
   public PizarronServiceData getPizarronServiceData() {
       return pizarronServiceData;
   }

   /**
    * Asigna el valor a _pizarronServiceData.
    *
    * @param pizarronServiceData El valor del bean.
    */
   public void setPizarronServiceData(PizarronServiceData pizarronServiceData) {
       this.pizarronServiceData = pizarronServiceData;
   }

   /**
    * Componente a servicios de ForasPagoLiq.
    */
   private FormasPagoLiqService formasPagoLiqService;

   /**
    * Componente a sevicios del Pizarron.
    */
   private PizarronServiceData pizarronServiceData;
}
