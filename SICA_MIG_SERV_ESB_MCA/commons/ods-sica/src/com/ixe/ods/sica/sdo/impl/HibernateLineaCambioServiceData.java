/*
 * $Id: HibernateLineaCambioServiceData.java,v 1.16.22.2.46.11 2016/10/11 22:07:34 mejiar Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2008 - 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica.sdo.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.type.DateType;
import net.sf.hibernate.type.IntegerType;
import ognl.Ognl;
import ognl.OgnlException;

import org.springframework.orm.hibernate.HibernateCallback;
import org.springframework.util.Assert;

import com.ixe.ods.bup.model.ContratoSica;
import com.ixe.ods.bup.model.CuentaEjecutivo;
import com.ixe.ods.bup.model.Persona;
import com.ixe.ods.bup.model.PersonaMoral;
import com.ixe.ods.seguridad.model.ISistema;
import com.ixe.ods.seguridad.model.IUsuario;
import com.ixe.ods.seguridad.model.Sistema;
import com.ixe.ods.sica.Constantes;
import com.ixe.ods.sica.Redondeador;
import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.dao.LineaCambioDao;
import com.ixe.ods.sica.dto.PosicionMesaDto;
import com.ixe.ods.sica.model.Actividad;
import com.ixe.ods.sica.model.Deal;
import com.ixe.ods.sica.model.DealDetalle;
import com.ixe.ods.sica.model.Divisa;
import com.ixe.ods.sica.model.Limite;
import com.ixe.ods.sica.model.LineaCambio;
import com.ixe.ods.sica.model.LineaCambioLog;
import com.ixe.ods.sica.model.LineaCreditoReinicioExceso;
import com.ixe.ods.sica.model.ParametroSica;
import com.ixe.ods.sica.model.PrecioReferenciaActual;
import com.ixe.ods.sica.model.TipoLimite;
import com.ixe.ods.sica.sdo.LineaCambioServiceData;
import com.ixe.ods.sica.sdo.PizarronServiceData;
import com.ixe.ods.sica.services.FormasPagoLiqService;
import com.ixe.ods.sica.services.LineaCreditoConstantes;
import com.ixe.ods.sica.services.LineaCreditoMensajes;
import com.ixe.ods.sica.services.RiesgosService;
import com.ixe.ods.sica.utils.BDUtils;
import com.ixe.ods.sica.utils.DateUtils;
import com.ixe.treasury.dom.common.FormaPagoLiq;

/**
 * Implementaci&oacute;n de la interfaz LineaCambioServiceData. Realiza todas las operaciones a la
 * base de datos que tienen que ver con Lineas de Cambio.
 *
 * @author Jean C. Favila
 * @version $Revision: 1.16.22.2.46.11 $ $Date: 2016/10/11 22:07:34 $
 */
public class HibernateLineaCambioServiceData extends HibernateSicaServiceData
        implements LineaCambioServiceData {

    /**
     * Constructor por default.
     */
    public HibernateLineaCambioServiceData() {
        super();
    }

    /**
     * Regresa true si la l&iacute;nea de cambio especificada tiene alg&uaacute;n registro en
     * SC_LINEA_CAMBIO_LOG que indique que en alg&uaacute;n momento fue activada.
     *
     * @param idLineaCambio El n&uacute;mero de l&iacute;nea de cambio.
     * @return boolean.
     */
    public boolean lineaEstuvoActivada(int idLineaCambio) {
        List lcls = getHibernateTemplate().find("SELECT lcl.idLineaCambioLog FROM LineaCambioLog " +
                "AS lcl WHERE lcl.lineaCambio.idLineaCambio = ? AND lcl.tipoOper = ?",
                new Object[]{new Integer(idLineaCambio), LineaCambioLog.OPER_ACTIVACION});
        return !lcls.isEmpty();
    }

    /**
     * @see com.ixe.ods.sica.sdo.LineaCambioServiceData#findLineaCambioParaCliente(Integer).
     * @param idPersona El n&uacute;mero de persona a consultar.
     * @return LineaCambio.
     */
    public LineaCambio findLineaCambioParaCliente(Integer idPersona) {
        if (idPersona != null) {
            Integer idCorporativo;
            PersonaMoral corporativo = findCorporativo(idPersona);
            idCorporativo = corporativo == null ? idPersona : corporativo.getIdPersona();
            List lcs = getHibernateTemplate().find("FROM LineaCambio AS lc WHERE " +
                    "lc.corporativo.idPersona = ?", idCorporativo);
            
            if (lcs.isEmpty()) {
                return null;
            }
            return (LineaCambio) lcs.get(0);

        }
        return null;
    }

    /**
     * @param ejecutivos La lista con el o los ejecutivos de los cuales se quieren buscar las
     *      l&iacute;neas.
     * @param status El status de las l&iacute;neas.
     * @param limiteInferior Rango inferior de porcentaje de uso de las l&iacute;neas.
     * @param limiteSuperior Rango superior de porcentaje de uso de las l&iacute;neas.
     * @param isLineasPorRenovar Si se quieren buscar las l&iacute;neas pr&oacute;ximas a renovar o
     *      no.
     * @param fechaActual La fecha actual de la consulta.
     * @param diasVencimientoLineas Par&aacute;metro D&iacute;as de Vencimiento de las L&iacute;neas
     *      de Cr&eacute;dito.
     * @return List con las L&iacute;neas de Cre&acute;dito.
     * @see com.ixe.ods.sica.sdo.LineaCambioServiceData#findLineasCambioByEjecutivos(java.util.List,
     *  String, double, double, boolean, java.util.Date, Integer).
     */
    public List findLineasCambioByEjecutivos(List ejecutivos, String status, double limiteInferior,
                                             double limiteSuperior, boolean isLineasPorRenovar,
                                             Date fechaActual, Integer diasVencimientoLineas) {
    	final List ejecutivosAux = new ArrayList(ejecutivos);
        final String statusAux = status;
        final Double limiteInferiorAux = new Double (limiteInferior);
        final Double limiteSuperiorAux = new Double (limiteSuperior);
        final boolean isLineasPorRenovarAux = isLineasPorRenovar;
        final Date fechaActualAux = fechaActual;
        final Integer diasVencimientoLineasAux = diasVencimientoLineas;
        return getHibernateTemplate().executeFind(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException {
                StringBuffer sb = new StringBuffer("SELECT DISTINCT lc FROM ");
                sb.append("com.ixe.ods.bup.model.CuentaEjecutivo AS ce, ");
                sb.append("com.ixe.ods.bup.model.CuentaContrato AS cc, ");
                sb.append("com.ixe.ods.bup.model.ContratoSica AS cs LEFT JOIN cs.roles AS pcr, ");
                sb.append("com.ixe.ods.bup.model.Persona AS p, ");
                sb.append("com.ixe.ods.sica.model.LineaCambio AS lc ");
                sb.append("WHERE ce.id.ejecutivo.idPersona in (:ejecutivos) ");
                sb.append("AND ce.status.estatus = 'VIG' AND cs.noCuenta = ce.id.noCuenta ");
                sb.append("AND cc.noCuenta = cs.noCuenta AND cs.statusOrigen = 'Active' ");
                sb.append("AND cs.status.estatus = 'ACTIVA' ");
                sb.append("AND pcr.id.cuenta.noCuenta = cs.noCuenta ");
                sb.append("AND pcr.id.rol = 'TIT' AND pcr.status.estatus = 'VIG' ");
                sb.append("AND pcr.id.persona.idPersona = p.idPersona ");
                sb.append("AND p.idPersona = lc.corporativo.idPersona ");
                sb.append("AND lc.statusLinea = (:status) ");
                if (limiteInferiorAux.doubleValue() == 0.0 &&
                        limiteSuperiorAux.doubleValue() == 0.0) {
                    sb.append("AND lc.usoCash BETWEEN (lc.importePAyTF * ((:limiteInferior) / 100)) ").
                            append("AND (lc.importePAyTF * (lc.usoCash / 100)) ");
                }
                else {
                    sb.append("AND lc.usoCash BETWEEN (lc.importePAyTF * ((:limiteInferior) / 100)) ").
                            append("AND (lc.importePAyTF * ((:limiteSuperior) / 100)) ");
                }
        		if (!isLineasPorRenovarAux) {
        			sb.append("AND lc.vencimiento <= lc.vencimiento + (:diasVencimientoLineas) ");
        		}
        		else {
        			sb.append("AND lc.vencimiento <= (:fechaActual) + (:diasVencimientoLineas) ");
        		}
        		sb.append("AND ROWNUM < 2000");
        		Query q = session.createQuery(sb.toString());
        		q.setParameterList("ejecutivos", ejecutivosAux, new IntegerType());
        		q.setParameter("status", statusAux);
        		if (limiteInferiorAux.doubleValue() == 0.0 &&
                        limiteSuperiorAux.doubleValue() == 0.0) {
        			q.setParameter("limiteInferior", limiteInferiorAux);
        		}
        		else {
        			q.setParameter("limiteInferior", limiteInferiorAux);
        			q.setParameter("limiteSuperior", limiteSuperiorAux);
        		}
        		if (!isLineasPorRenovarAux) {
        			q.setParameter("diasVencimientoLineas", diasVencimientoLineasAux);
        		}
        		else {
        			sb.append("AND lc.vencimiento <= ? + ? ");
        			q.setParameter("diasVencimientoLineas", diasVencimientoLineasAux);
        			q.setParameter("fechaActual", fechaActualAux);
        		}
        		return q.list();
            }
        });
    }

    /**
     * Encuentra Las L&iacute;neas de Cr&eacute;dito de los Corporativos asignados a un grupo de
     * Ejecutivos por Estatus.
     *
     * @param clientes Los clientes encontrados de acuerdo al criterio de b&uacute;squeda
     *      Raz&oacute;n Social.
     * @param idTipoEjecutivo El Tipo Ejecutivo del SICA.
     * @param ejecutivos El Grupo de Ejecutivos.
     * @param estatus El Estatus seleccionado.
     * @return List Las L&iacute;neas de Cr&eacute;dito.
     */
    public List findLineasCambioByClientesAndEjecutivosAndEstatus(List clientes,
                                                                  String idTipoEjecutivo,
                                                                  List ejecutivos, String estatus) {
        List lineasCredito = new ArrayList();
        for(Iterator it = ejecutivos.iterator(); it.hasNext(); ) {
            HashMap ejecutivo = (HashMap) it.next();
            List lineasCreditoTmp = findLineasDeCreditoByClientesAndEjecutivoAndEstatus(clientes,
                    idTipoEjecutivo, (Integer)ejecutivo.get("idPersona"), estatus);
            if (lineasCreditoTmp.size() > 0) {
                lineasCredito.addAll(lineasCreditoTmp);
            }
        }
        return lineasCredito;
    }

    /**
     * @see com.ixe.ods.sica.sdo.LineaCambioServiceData#
     *      findLineasDeCreditoByClientesAndEjecutivoAndEstatus(List, String, Integer, String)
     */
    public List findLineasDeCreditoByClientesAndEjecutivoAndEstatus(List clientes,
                                                                    String idTipoEjecutivo,
                                                                    Integer idEjecutivo,
                                                                    String estatus) {
        List lineasCredito = new ArrayList();
        List lineas = new ArrayList();
        List cuentasEjecutivo = findCuentasEjecutivo(idTipoEjecutivo, idEjecutivo);
        Persona persona;
        Integer idCorporativo;
        for(Iterator it = cuentasEjecutivo.iterator(); it.hasNext(); ) {
            CuentaEjecutivo cuentaEjecutivo = (CuentaEjecutivo) it.next();
            persona = findPersonaContratoSica(cuentaEjecutivo.getId().getNoCuenta());
            if (persona != null) {
                PersonaMoral corp = findCorporativo(persona.getIdPersona());
	            idCorporativo = corp == null ? persona.getIdPersona() : corp.getIdPersona();
                if (clientes.size() > 0) {
	                for (Iterator i = clientes.iterator(); i.hasNext(); ) {
	                    Persona cliente = (Persona) i.next();
	                    if (idCorporativo.equals(cliente.getIdPersona())) {
	                        lineas = findLineaCreditoByCorporativoAndStatusLinea(idCorporativo,
                                    estatus);
	                        break;
	                    }
	                }
	            }
	            else {
	                lineas = findLineaCreditoByCorporativoAndStatusLinea(idCorporativo, estatus);
	            }
	            if (lineas.size() > 0) {
	                if (!lineasCredito.containsAll(lineas)) {
	                    lineasCredito.addAll(lineas);
	                }
	            }
            }
        }
        return lineasCredito;
    }

    /**
     * @see com.ixe.ods.sica.sdo.LineaCambioServiceData#findHistoriaLineaCambioLogByIdLineaCredito(
            Integer)
     */
    public List findHistoriaLineaCambioLogByIdLineaCredito(Integer idLineaCambio) {
        GregorianCalendar fechaInicio = new GregorianCalendar();
        fechaInicio.add(Calendar.MONTH, -1);
        return getHibernateTemplate().find("FROM LineaCambioLog AS lcl WHERE " +
                "lcl.lineaCambio.idLineaCambio = ? AND lcl.fechaOperacion >= ? ORDER BY " +
                "lcl.idLineaCambioLog",
                new Object[]{idLineaCambio, DateUtils.inicioDia(fechaInicio.getTime())});
    }
    
    /**
     * Si la divisa referencia de la Mesa del Deal es PESO, regresa el precio
     * referencia Mid Spot de alguno de los detalles o el actual si no existe uno.
     * 
     * Regresa 1 si la divisa referencia de la Mesa del Deal es diferente de PESO. 
     * 
     * @param deal El deal del cual se obtend&aacute; el precio referencia.
     * @return
     */
    private double getFactorUsd(Deal deal) {
        // Se calcula el monto en dolares:
        double factorUsd = 0;
        if (Divisa.PESO.equals(deal.getCanalMesa().getMesaCambio().getDivisaReferencia().
                getIdDivisa())) {
            // Encontramos el primer precio de referencia:
            for (Iterator it = deal.getDetalles().iterator(); it.hasNext();) {
                DealDetalle dealDetalle = (DealDetalle) it.next(); //correccion del problema de nullpointer
                if (null != dealDetalle.getPrecioReferenciaMidSpot() && dealDetalle.getPrecioReferenciaMidSpot().doubleValue() > 0) {
                	factorUsd = dealDetalle.getPrecioReferenciaMidSpot().doubleValue();
                    break;
                }
            }
            if (factorUsd == 0) {
            	PrecioReferenciaActual pr = null;
                pr = findPrecioReferenciaActual();
                factorUsd = pr.getPreRef().getMidSpot().doubleValue(); 
            }
        }else{
        	factorUsd = 1;
        }
        return factorUsd;
    }
    
  

    public List findLineaCambioLog(Integer idLinea, String tipoOperacion) {
    	List lineaCambioLogList = new ArrayList();
    	lineaCambioLogList = getHibernateTemplate().find("FROM LineaCambioLog AS lcl WHERE " +
        		"lcl.lineaCambio.idLineaCambio = ? AND lcl.tipoOper = ? ORDER BY lcl.idLineaCambioLog ", new Object[]{idLinea, tipoOperacion});
    	
        return lineaCambioLogList; 
    }

    
    public double getMontoUsdDealDetalle(DealDetalle detalle){
    	Deal deal = detalle.getDeal();
    	double factorUsd = getFactorUsd(deal);
    	return Redondeador.redondear2Dec(detalle.getImporte() / factorUsd);
    	
    }
    
   
    /**
     * @see LineaCambioServiceData#isDetalleALiberarLC(DealDetalle)
     */
    public boolean isDetalleALiberarLC(DealDetalle det) {
    	boolean uso = true;
    	Deal deal = det.getDeal();
    	
    	double fep = getFactorRiesgo().doubleValue();
    	LineaCambio linea = findLineaCambioParaCliente(deal.getCliente().getIdPersona());
    	if(linea == null ){
    		uso = false;
    		return uso;
    	}
        List usosDeal = findLineaCambioLog(linea.getIdLineaCambio(), LineaCreditoConstantes.USO);
        List liberadosDeal = findLineaCambioLog(linea.getIdLineaCambio(), LineaCreditoConstantes.LIBERACION);
        
        for (Iterator it = liberadosDeal.iterator(); it.hasNext();) {
            LineaCambioLog lcl = (LineaCambioLog) it.next();
            if (lcl.getIdDealDetalle() != null &&
                    lcl.getIdDealDetalle().intValue() == det.getIdDealPosicion()) {
            	
            	
            		if(det.getMontoUSD() == 0.0){
            			double importePAyTFUSD = getMontoUsdDealDetalle(det);
            			double importeFV = getMontoUsdDealDetalle(det) * fep;
            			if(Redondeador.redondear2Dec(lcl.getImporte().doubleValue()) == importePAyTFUSD || 
            					Redondeador.redondear2Dec(lcl.getImporte().doubleValue()) == importeFV){
            				uso = false;
            			}
            			
            		}else{
            			double importePAyTFUSD = Redondeador.redondear2Dec(det.getMontoUSD());
            			double importeFV = Redondeador.redondear2Dec(det.getMontoUSD() * fep);
            			if(Redondeador.redondear2Dec(lcl.getImporte().doubleValue()) == importePAyTFUSD || 
            					Redondeador.redondear2Dec(lcl.getImporte().doubleValue()) == importeFV){
            				uso = false;
            			}
            		}
            	
				if (!uso) {
					return uso;
				}

            }
        }
        
        if(usosDeal != null && usosDeal.size() >0){
	        for (Iterator it = usosDeal.iterator(); it.hasNext();) {
	            LineaCambioLog lcl = (LineaCambioLog) it.next();
	            if (lcl.getIdDealDetalle() != null &&
	                    lcl.getIdDealDetalle().intValue() == det.getIdDealPosicion()) {
	            	uso=true;
	                return uso;
	            }
	            uso=false;
	        }
        }else{
        	uso = false;
        }
        return uso;
    }
  

    /**
     * @deprecated ya no se debe usar esta liberacion
     * @param det El detalle a liberar.
     * @see com.ixe.ods.sica.sdo.LineaCambioServiceData#liberarDetalle(String,
     *          com.ixe.ods.sica.model.DealDetalle, boolean).
     */
//    public void liberarDetalle(String ticket, DealDetalle det, boolean liberarRemesas) {
//        if (det.getDeal().getContratoSica() == null || det.getDeal().getCliente() == null) {
//            return;
//        }
//        // Si no es recibimos, no debe liberar:
//        if (!det.isRecibimos()) {
//            return;
//        }
//        // Si no tiene mnem&oacute;nico, no puede haber afectado la l&iacute;nea:
//        if (det.getMnemonico() == null) {
//            return;
//        }
//       
//        try {
//            BigDecimal importeUsd = getUsoParaDetalle(det);
//            double factorUsd = getFactorUsd(det.getDeal());
//            LineaCambio linea = findLineaCambioParaCliente(det.getDeal().getCliente().
//                    getIdPersona());
//            if (linea == null) {
//                warn("\n\n\n**** No existe la l\u00ednea de cambios para el detalle de deal " +
//                        det.getIdDealPosicion() + ".\n\n\n\n");
//                return;
//            }
//            LineaCambioLog lcl = new LineaCambioLog(linea, LineaCambioLog.OPER_LIBERACION,
//                    importeUsd, det, BDUtils.generar6(factorUsd),
//                    "Liberaci\u00f3n de l\u00ednea de cr\u00e9dito",
//                    det.getDeal().getUsuario());
//            store(lcl);
//            boolean esRemesa = getFormasPagoLiqService().isRemesa(ticket,
//                    det.getClaveFormaLiquidacion(), det.getMnemonico(), det.isRecibimos());
//            usarLiberar(linea, importeUsd, getFechaValor(det.getDeal()), false,
//                    esRemesa && liberarRemesas);
//        }
//        catch (OgnlException e) {
//            warn(e.getMessage(), e);
//            throw new SicaException("No se pudo calcular la fecha valor para la afectar la " +
//                    "l\u00ednea de cr\u00eadito.", e);
//        }
//    }

    /**
    *
    * @param det El detalle a liberar.
     * @return 
    * @see com.ixe.ods.sica.sdo.LineaCambioServiceData#liberarDetalle(String,
    *          com.ixe.ods.sica.model.DealDetalle, boolean).
    */
   public LineaCambio liberarDetalleLineaCreditoPA(String ticket, DealDetalle detalle) {
	   
	   boolean isCompraDealDetalle = detalle.isRecibimos();
	   BigDecimal usoPAyTF;
	   BigDecimal usoFV;
	   double montoDetallePAyTF = 0.0;
	   double montoDetalleFV= 0.0;
	   double factorExposicionPotencial = getFactorRiesgo().doubleValue();
	   Deal deal = detalle.getDeal();
	   boolean isCompraDeal = deal.isCompra();
	   LineaCambio linea = findLineaCambioParaCliente(deal.getCliente().getIdPersona());
	   
	   if (linea == null) {
           warn("\n\n\n**** No existe la l\u00ednea de cambios para el detalle de deal " +
                   detalle.getIdDealPosicion() + ".\n\n\n\n");
           return null;
       }
	   if (deal.getContratoSica() == null || deal.getCliente() == null) {
           return null;
       }
	   
	   
	   if(detalle.getMontoUSD() == 0.0){
			montoDetallePAyTF = Redondeador.redondear2Dec(getMontoUsdDealDetalle(detalle));
			montoDetalleFV = Redondeador.redondear2Dec(getMontoUsdDealDetalle(detalle) * factorExposicionPotencial);
	   }else{
		   montoDetallePAyTF = Redondeador.redondear2Dec(detalle.getMontoUSD() );
		   montoDetalleFV = Redondeador.redondear2Dec(detalle.getMontoUSD() * factorExposicionPotencial);
	   }
	   
	   String liberarFechaValor = getFechaValor(deal);
	   
       if ((isCompraDeal && isCompraDealDetalle) || (!isCompraDeal && !isCompraDealDetalle)) {
    	   
    	   usoPAyTF = linea.getUsoCash().subtract(new BigDecimal(montoDetallePAyTF));
    	   usoPAyTF = validarUso(usoPAyTF);
    	   
    	   if(deal.isPagoAnticipado() || deal.isTomaEnFirme()){
    		   
    		   linea.setUsoCash(usoPAyTF);
    		   
    		   if(!deal.getTipoValor().equals(Constantes.CASH) && liberarFechaValor.equals(Constantes.CASH)){
    			   usoFV= linea.getUsoCash().subtract(new BigDecimal(montoDetalleFV));
    			   usoFV = validarUso(usoFV);
    			   linea.setUsoCash(usoFV);
    			   
    		   }else if(liberarFechaValor.equals(Constantes.TOM)){
    			   
    			   usoFV= linea.getUsoTom().subtract(new BigDecimal(montoDetalleFV));
    			   usoFV = validarUso(usoFV);
    			   linea.setUsoTom(usoFV);

    		   }else if(liberarFechaValor.equals(Constantes.SPOT)){
    			   
    			   usoFV= linea.getUsoSpot().subtract(new BigDecimal(montoDetalleFV));
    			   usoFV = validarUso(usoFV);
    			   linea.setUsoSpot(usoFV);

    		   }else if(liberarFechaValor.equals(Constantes.HR72)){
    			   
    			   usoFV= linea.getUso72Hr().subtract(new BigDecimal(montoDetalleFV));
    			   usoFV = validarUso(usoFV);
    			   linea.setUso72Hr(usoFV);
    			   
    		   }else if(liberarFechaValor.equals(Constantes.VFUT)){
    			   
    			   usoFV= linea.getUso96Hr().subtract(new BigDecimal(montoDetalleFV));
    			   usoFV = validarUso(usoFV);
    			   linea.setUso96Hr(usoFV);
    			   
    		   }
    		   
		        linea.setUltimaModificacion(new Date());
		        update(linea);
		        
		        LineaCambioLog lclPAyTF = new LineaCambioLog(linea, LineaCambioLog.OPER_LIBERACION,
		                        BDUtils.generar2(montoDetallePAyTF), detalle, BDUtils.generar6(detalle.getFactorDivisa().doubleValue()),
		                        LineaCreditoMensajes.LIBERACION,
		                        deal.getUsuario());
		        store(lclPAyTF);
		        
		        if (!deal.getTipoValor().equals(Constantes.CASH) ){
			        LineaCambioLog lclFV = new LineaCambioLog(linea, LineaCambioLog.OPER_LIBERACION,
	                       BDUtils.generar2(montoDetalleFV), detalle, BDUtils.generar6(detalle.getFactorDivisa().doubleValue()),
	                       LineaCreditoMensajes.LIBERACION,
	                       deal.getUsuario());
			        
			        store(lclFV);
		        }
    	   }
       }
	return linea;
   }

   private BigDecimal validarUso(BigDecimal uso) {
		if (uso != null && uso.doubleValue() < 0){
			uso = new BigDecimal(0);
		}
		return uso;
	}

/**
   *
   * @param det El detalle a liberar.
   * @see com.ixe.ods.sica.sdo.LineaCambioServiceData#liberarDetalle(String,
   *          com.ixe.ods.sica.model.DealDetalle, boolean).
   */
  public LineaCambio liberarDetalleLineaCredito(String ticket, DealDetalle detalle) {
	   
	   boolean isCompraDealDetalle = detalle.isRecibimos();
	   BigDecimal usoFV;
	   double factorExposicionPotencial = getFactorRiesgo().doubleValue();
	   Deal deal = detalle.getDeal();
	   boolean isCompraDeal = deal.isCompra();
	   double montoDetalleFV = 0.0;
	   LineaCambio linea = findLineaCambioParaCliente(deal.getCliente().getIdPersona());
	   
	   if (linea == null) {
          warn("\n\n\n**** No existe la l\u00ednea de cambios para el detalle de deal " +
                  detalle.getIdDealPosicion() + ".\n\n\n\n");
          return null;
      }
	   if (deal.getContratoSica() == null || 
			   deal.getCliente() == null || 
			   deal.getTipoValor().equals(Constantes.CASH)) {
          return null;
      }
	  
	   if(detalle.getMontoUSD() == 0.0){
			montoDetalleFV = Redondeador.redondear2Dec(getMontoUsdDealDetalle(detalle) * factorExposicionPotencial);
	   }else{
		   montoDetalleFV = Redondeador.redondear2Dec(detalle.getMontoUSD() * factorExposicionPotencial);
	   }
	   
	   String liberarFechaValor = getFechaValor(deal);
	   
	   
      if ((isCompraDeal && isCompraDealDetalle) || (!isCompraDeal && !isCompraDealDetalle)) {
    	  
    	  if( liberarFechaValor.equals(Constantes.CASH)){
  			   usoFV= linea.getUsoCash().subtract(new BigDecimal(montoDetalleFV));
  			   usoFV = validarUso(usoFV);
  			   linea.setUsoCash(usoFV);
  		   }else if(liberarFechaValor.equals(Constantes.TOM)){
   			   usoFV= linea.getUsoTom().subtract(new BigDecimal(montoDetalleFV));
   			   usoFV = validarUso(usoFV);
   			   linea.setUsoTom(usoFV);
   		   }else if(liberarFechaValor.equals(Constantes.SPOT)){
   			   usoFV= linea.getUsoSpot().subtract(new BigDecimal(montoDetalleFV));
   			   usoFV = validarUso(usoFV);
   			   linea.setUsoSpot(usoFV);
   		   }else if(liberarFechaValor.equals(Constantes.HR72)){
   			   usoFV= linea.getUso72Hr().subtract(new BigDecimal(montoDetalleFV));
   			   usoFV = validarUso(usoFV);
   			   linea.setUso72Hr(usoFV);
   		   }else if(liberarFechaValor.equals(Constantes.VFUT)){
   			   usoFV= linea.getUso96Hr().subtract(new BigDecimal(montoDetalleFV));
   			   usoFV = validarUso(usoFV);
   			   linea.setUso96Hr(usoFV);
   		   }

   		   linea.setUltimaModificacion(new Date());
		   getHibernateTemplate().update(linea);
		        
			LineaCambioLog lclFV = new LineaCambioLog(linea, LineaCambioLog.OPER_LIBERACION,
			       BDUtils.generar2(montoDetalleFV), detalle, BDUtils.generar6(detalle.getFactorDivisa().doubleValue()),
			       LineaCreditoMensajes.LIBERACION,
			       deal.getUsuario());
			getHibernateTemplate().save(lclFV);
      }
	return linea;
  }
    
    
    private double getTotalRemesasUsd(String ticket, Deal deal) {
        double totalRemesasUsd = 0.0;
        for (Iterator it = deal.getRecibimos().iterator(); it.hasNext();) {
            DealDetalle det = (DealDetalle) it.next();
            if (!det.isCancelado()) {
                if (getFormasPagoLiqService().isRemesa(ticket, det.getClaveFormaLiquidacion(),
                        det.getMnemonico(), det.isRecibimos())) {
                    totalRemesasUsd += det.getMontoUSD();
                }
            }
        }
        return totalRemesasUsd;
    }

    /**
     * Regresa la diferencia entre una fecha de liquidaci&oacute; y la fecha actual.
     *
     * @param fechaLiquidacion La fecha de liquidaci&oacute;n a revisar.
     * @return int.
     */
    private int getDiasDiferencia(Date fechaLiquidacion) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(DateUtils.inicioDia(fechaLiquidacion));
        Date fechaActual = DateUtils.inicioDia();
        int i = 0;
        for (; gc.getTime().getTime() > fechaActual.getTime(); i++) {
            gc.add(Calendar.DAY_OF_MONTH, -1);
        }
        return i + 1;
    }
    
    /**
     * @see LineaCambioServiceData#consumirLineaCredito(Deal, DealDetalle)
     */
    
    public void consumirLineaCredito(Deal deal, DealDetalle detalle) throws SicaException {
    	
    	
    	if(Constantes.CASH.equals(deal.getTipoValor())){
			return ;
		}
    	
    	if(deal.getCliente() == null ){
    		throw new SicaException(LineaCreditoMensajes.ERROR_DEAL_CAPTURA_RAPIDA_);
    	}
    	LineaCambio linea = findLineaCambioParaCliente(deal.getCliente().getIdPersona());
        
		if(linea == null || !linea.getStatusLinea().equals(LineaCreditoConstantes.STATUS_ACTIVADA)) {
			throw new SicaException(LineaCreditoMensajes.OPERACION_NO_CASH);
		}else if(linea != null && linea.getStatusLinea().equals(LineaCreditoConstantes.STATUS_ACTIVADA)){
			consumirLineaCreditoFechaValor(linea,deal,detalle);
		}
        
//		revisarAplicacionLineaCredito(ticket, deal);
        
    }
    
    /**
     * @see LineaCambioServiceData#consumirLineaCreditoFechaValor(LineaCambio, Deal, DealDetalle)
     */
    public void consumirLineaCreditoFechaValor(LineaCambio linea, Deal deal, DealDetalle detalle) {
    	boolean isCompraDeal = deal.isCompra();
    	boolean isCompraDealDetalle = detalle.isRecibimos();
    	BigDecimal uso;
    	double factorExposicionPotencial = getFactorRiesgo().doubleValue();
    	
    	if ((isCompraDeal && isCompraDealDetalle) || (!isCompraDeal && !isCompraDealDetalle)) {
    		double montoDetalleUsd = Redondeador.redondear2Dec(detalle.getMontoUSD()  * factorExposicionPotencial);
    		if(montoDetalleUsd == 0.0 ){
    			montoDetalleUsd = Redondeador.redondear2Dec(getMontoUsdDealDetalle(detalle) * factorExposicionPotencial );
    		}
    		Assert.isTrue(montoDetalleUsd > 0 , LineaCreditoMensajes.MONTO_DETALLE_CERO);
    		if(linea.getSaldoFV()!= null && linea.getSaldoFV().doubleValue() >= montoDetalleUsd){
		        if (deal.getFactorRiesgo() == null || deal.getFactorRiesgo().doubleValue() < 0.000001) {
		            deal.setFactorRiesgo(BDUtils.generar6(factorExposicionPotencial));
		        }
		        
		        if(Constantes.TOM.equals(deal.getTipoValor().trim())){
		        	uso = linea.getUsoTom().add(new BigDecimal(montoDetalleUsd));
		        	linea.setUsoTom(uso);
		        }else if(Constantes.SPOT.equals(deal.getTipoValor())){
		        	uso = linea.getUsoSpot().add(new BigDecimal(montoDetalleUsd));
		        	linea.setUsoSpot(uso);
		        }else if(Constantes.HR72.equals(deal.getTipoValor())){
		        	uso = linea.getUso72Hr().add(new BigDecimal(montoDetalleUsd));
		        	linea.setUso72Hr(uso);
		        }else if(Constantes.VFUT.equals(deal.getTipoValor())){
		        	uso = linea.getUso96Hr().add(new BigDecimal(montoDetalleUsd));
		        	linea.setUso96Hr(uso);
		        }	
		        linea.setUltimaModificacion(new Date());
		        getHibernateTemplate().update(linea);
		        if(!deal.isEvento( Deal.EV_IND_GRAL_PAG_ANT, new String[]{Deal.EV_USO})){
					deal.setEvento(Deal.EV_USO, Deal.EV_IND_GRAL_PAG_ANT);
				}
		        LineaCambioLog lcl = new LineaCambioLog(linea, LineaCambioLog.OPER_USO,
		                        BDUtils.generar2(montoDetalleUsd), detalle, BDUtils.generar6(detalle.getFactorDivisa().doubleValue()),
		                        LineaCreditoMensajes.USO,
		                        detalle.getDeal().getUsuario());
		        getHibernateTemplate().save(lcl);

    		}else{
    			throw new SicaException(LineaCreditoMensajes.REBASA_SALDOFV);
    		}
    	}
    }
    
    /**
	 * @see LineaCambioServiceData#consumirLineaCreditoByDeal(LineaCambio, Deal)
	 */
	public void consumirLineaCreditoByDeal(String ticket , LineaCambio linea, Deal deal) {
		double totalMontoDealUSD = revisarAplicacionLineaCredito(ticket, deal);

		if(linea.getSaldoPAyTF().doubleValue() >=  totalMontoDealUSD){
			for (Iterator it = deal.getDetalles().iterator(); it.hasNext();) {
				DealDetalle det    = (DealDetalle) it.next();
				boolean bMnemonico = validarAplicablesTfPagAnt(ticket, det.getMnemonico(),
                        det.getClaveFormaLiquidacion(), det.isRecibimos());
				
				if (!det.isCancelado() && det.getMnemonico()== null){
					deal.setPagoAnticipado(false);
					throw new SicaException("Favor de completar informaci\u00f3n en los detalles.");
					
				}
				
				if (bMnemonico &&  ((deal.isCompra() && det.isRecibimos()) || (!deal.isCompra() && !det.isRecibimos()))){
					if (!det.isCancelado()) {
						consumirLineaCreditoPAyTF(linea, deal, det);
						if(!deal.isEvento( Deal.EV_IND_GRAL_PAG_ANT, new String[]{Deal.EV_USO})){
							deal.setEvento(Deal.EV_USO, Deal.EV_IND_GRAL_PAG_ANT);
							update(deal);
						}
					}
				}
			}
		}else{
			if(deal.isPagoAnticipado()){
				deal.setPagoAnticipado(false);
			}else if (deal.isTomaEnFirme()){
				deal.setTomaEnFirme(false);
			}
				
			throw new SicaException(LineaCreditoMensajes.REBASA_SALDOPAYTF);
		}
	}

	
	
	
	/**
	 * @see LineaCambioServiceData#consumirLineaCreditoPA(Deal, DealDetalle)
	 */

	public void consumirLineaCreditoPA(Deal deal, DealDetalle det) {
		
		LineaCambio	linea = findLineaCambioParaCliente(deal.getCliente().getIdPersona());
		boolean isCompraDeal = deal.isCompra();
    	boolean isCompraDealDetalle = det.isRecibimos();
    	double factorExposicionPotencial = getFactorRiesgo().doubleValue();
    	BigDecimal usoPAyTF;
    	BigDecimal usoFV;
    	
		if ((isCompraDeal && isCompraDealDetalle) || (!isCompraDeal && !isCompraDealDetalle)) {
    		double montoDetallePAyTF = Redondeador.redondear2Dec(det.getMontoUSD());
    		double montoDetallelFV = Redondeador.redondear2Dec(det.getMontoUSD() * factorExposicionPotencial);
    		
    		if(montoDetallePAyTF == 0.0){
    			montoDetallePAyTF = Redondeador.redondear2Dec(getMontoUsdDealDetalle(det));
    		}

    		if( montoDetallelFV == 0.0){
    			montoDetallelFV = Redondeador.redondear2Dec(getMontoUsdDealDetalle(det) * factorExposicionPotencial );
    		}
    		
    		Assert.isTrue(montoDetallePAyTF > 0 , LineaCreditoMensajes.MONTO_DETALLE_CERO);
    		Assert.isTrue(montoDetallelFV > 0 , LineaCreditoMensajes.MONTO_DETALLE_CERO);
    		if(linea.getSaldoPAyTF()!= null && linea.getSaldoPAyTF().doubleValue() >= montoDetallePAyTF){
    			
    			usoPAyTF = linea.getUsoCash().add(new BigDecimal(montoDetallePAyTF));
    			
    			if(deal.getTipoValor().equals(Constantes.CASH)){
        			linea.setUsoCash(usoPAyTF);
        			linea.setUltimaModificacion(new Date());
        			update(linea);	
    		        LineaCambioLog lcl = new LineaCambioLog(linea, LineaCambioLog.OPER_USO,
    		                        BDUtils.generar2(montoDetallePAyTF), det, BDUtils.generar6(det.getFactorDivisa().doubleValue()),
    		                        LineaCreditoMensajes.USO,
    		                        deal.getUsuario());
    		        store(lcl);
    			}else if(linea.getSaldoFV()!= null && linea.getSaldoFV().doubleValue() >= montoDetallelFV){
    		        if (deal.getFactorRiesgo() == null || deal.getFactorRiesgo().doubleValue() < 0.000001) {
    		            deal.setFactorRiesgo(BDUtils.generar6(factorExposicionPotencial));
    		        }
    		        
    		        if(Constantes.TOM.equals(deal.getTipoValor().trim())){
    		        	usoFV = linea.getUsoTom().add(new BigDecimal(montoDetallelFV));
    		        	linea.setUsoTom(usoFV);
    		        }else if(Constantes.SPOT.equals(deal.getTipoValor())){
    		        	usoFV = linea.getUsoSpot().add(new BigDecimal(montoDetallelFV));
    		        	linea.setUsoSpot(usoFV);
    		        }else if(Constantes.HR72.equals(deal.getTipoValor())){
    		        	usoFV = linea.getUso72Hr().add(new BigDecimal(montoDetallelFV));
    		        	linea.setUso72Hr(usoFV);
    		        }else if(Constantes.VFUT.equals(deal.getTipoValor())){
    		        	usoFV = linea.getUso96Hr().add(new BigDecimal(montoDetallelFV));
    		        	linea.setUso96Hr(usoFV);
    		        }
    		        
    		        linea.setUsoCash(usoPAyTF);
    		        linea.setUltimaModificacion(new Date());
    		        update(linea);
    		        
    		        LineaCambioLog lclPAyTF = new LineaCambioLog(linea, LineaCambioLog.OPER_USO,
    		                        BDUtils.generar2(montoDetallePAyTF), det, BDUtils.generar6(det.getFactorDivisa().doubleValue()),
    		                        LineaCreditoMensajes.USO,
    		                        deal.getUsuario());
    		        LineaCambioLog lclFV = new LineaCambioLog(linea, LineaCambioLog.OPER_USO,
	                        BDUtils.generar2(montoDetallelFV), det, BDUtils.generar6(det.getFactorDivisa().doubleValue()),
	                        LineaCreditoMensajes.USO,
	                        deal.getUsuario());
    		        store(lclPAyTF);
    		        store(lclFV);
    			}else{
    				throw new SicaException(LineaCreditoMensajes.REBASA_SALDOFV);
    			}
    			
    			if(!deal.isEvento( Deal.EV_IND_GRAL_PAG_ANT, new String[]{Deal.EV_USO})){
					deal.setEvento(Deal.EV_USO, Deal.EV_IND_GRAL_PAG_ANT);
				}
    			
    		}else{
    	    	throw new SicaException(LineaCreditoMensajes.REBASA_SALDOPAYTF);
    		}
    		
		}	
		
	}
    
    
    /**
     * Se aplica el consumo sobre una linea de credito Activa y la fecha valor = CASH
     * tomando en cuenta que se encuentra seleccionada la opcion de Pago Anticipado y/o Toma en Firme
     * @param linea Linea de credito asociada al Cliente
     * @param deal Deal que contiene los detalles
     * @param detalle Detalle que aplcia a la Linea de Credito
     */
	
    private void consumirLineaCreditoPAyTF(LineaCambio linea, Deal deal, DealDetalle detalle) {
    	boolean isCompraDeal = deal.isCompra();
    	boolean isCompraDealDetalle = detalle.isRecibimos();
    	double factorUsd = getFactorUsd(deal);
    	BigDecimal uso;
    	
    	if ((isCompraDeal && isCompraDealDetalle) || (!isCompraDeal && !isCompraDealDetalle)) {
    		double montoDetalleUsd = Redondeador.redondear2Dec((detalle.getMontoUSD()));
    		
    		
    		if(montoDetalleUsd == 0.0 ){
    			montoDetalleUsd = Redondeador.redondear2Dec(getMontoUsdDealDetalle(detalle));
    		}
    		Assert.isTrue(montoDetalleUsd > 0 , LineaCreditoMensajes.MONTO_DETALLE_CERO);
    		if(linea.getSaldoPAyTF()!= null && linea.getSaldoPAyTF().doubleValue() >= montoDetalleUsd){
    			uso = linea.getUsoCash().add(new BigDecimal(montoDetalleUsd));
    			linea.setUsoCash(uso);
    			linea.setUltimaModificacion(new Date());
    			update(linea);	
		        LineaCambioLog lcl = new LineaCambioLog(linea, LineaCambioLog.OPER_USO,
		                        BDUtils.generar2(montoDetalleUsd), detalle, BDUtils.generar6(detalle.getFactorDivisa().doubleValue()),
		                        "Uso de l\u00ednea de cr\u00e9dito",
		                        detalle.getDeal().getUsuario());
		        store(lcl);
    		}else{
    			throw new SicaException(LineaCreditoMensajes.REBASA_SALDOPAYTF);
    		}
    	}
    }

    /**
     * Obtiene el Factor de Exposicion Potencial que esta definido en SC_PARAMETRO como (LC_CONSTANTE_FEP)
     * 
     * @return
     */
	private BigDecimal getFactorRiesgo() {
		ParametroSica parFED = (ParametroSica) getHibernateTemplate().load(
                ParametroSica.class, ParametroSica.FED);
    	double factorExposicionPotencial = Double.valueOf(parFED.getValor()).doubleValue();
		return new BigDecimal(factorExposicionPotencial);
	}
		
	

	/**
     * @deprecated
     * Eliminar esta funcionalidad pues ya no se utiliza
     * 
     * Hace uso de la l&iacute;nea de cr&eacute;dito para el deal especificado. Si la bandera
     * validar es true, no permite el uso si no hay saldo disponible.
     *
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param deal El deal a revisar.
     * @param validar Si se requiere validaci&oacute;n o no (cuando se va a permitir el excedente).
     * @return Un mensaje de error si ocurre.
     * @throws SicaException Si algo sale mal.
     */
    public String usar(String ticket, Deal deal, boolean validar) throws SicaException {
        LineaCambio linea = findLineaCambioParaCliente(deal.getCliente().getIdPersona());
        // Si ya se hizo uso, no debe volver a hacerse:
        if (deal.isEvento(Deal.EV_IND_GRAL_PAG_ANT, new String[] {Deal.EV_USO})) {
            debug("El deal " + deal.getIdDeal() + " ya hab\u00eda hecho uso de la l\u00ednea.");
            return null;
        }
        if (linea == null) {
            return "No se ha solicitado l\u00ednea de cr\u00e9dito.";
        }
        if (validar) {
            revisarAplicacionLineaCredito(ticket, deal);
            // Revisar Excepciones en las lineas de credito aplicables:
            ParametroSica parMaxExced = (ParametroSica) getHibernateTemplate().load(
                    ParametroSica.class, ParametroSica.MAX_EXCEDENTES);
            int maxExcepciones = Integer.valueOf(parMaxExced.getValor()).intValue();
            if (linea.getNumeroExcepcionesMes() > maxExcepciones) {
                deal.setTomaEnFirme(false);
                deal.setPagoAnticipado(false);
                update(deal);
                return "El n\u00famero de excepciones en el mes ha llegado al l\u00edmite. No se " +
                        "acepta toma en firme ni pago anticipado para este deal.";
            }
            // Se revisa el status de la linea:
            //ESTO SE DEBE DE ELIMINAR YA NO SE DEBE SOLICITAR EXCESO POR PANEL DE AUTORIZACIONES
            if (!LineaCambio.STATUS_ACTIVADA.equals(linea.getStatusLinea())) {
                if (deal.isPagoAnticipado() || deal.isTomaEnFirme()) {
                    deal.setEvento(Deal.EV_SOLICITUD, Deal.EV_IND_GRAL_PAG_ANT);
                }
                update(deal);
                return null;
            }
        }
        // Se calcula el monto en dolares:
        double factorUsd = getFactorUsd(deal);
        double factorRiesgo = 1.0 + getFactorRiesgo(deal).doubleValue();
        if (deal.getFactorRiesgo() == null || deal.getFactorRiesgo().doubleValue() < 0.000001) {
            deal.setFactorRiesgo(BDUtils.generar6(factorRiesgo - 1.0));
        }
        try {
            double montoUsd = deal.getMontoUSD() * factorRiesgo;
            double montoRemUsd = getTotalRemesasUsd(ticket, deal) * factorRiesgo;
            String expresionSaldo = getExpresionParaFechaValor(deal.getTipoValor(), false, false);
            String expresionSaldoRem = getExpresionParaFechaValor(deal.getTipoValor(),
                    false, false);
            
        
        //    debug("validar-"+validar +" montoUsd:"+montoUsd+" expresionSaldo:"+expresionSaldo);
            if (validar) {
                double saldoActual = ((BigDecimal) Ognl.getValue(expresionSaldo, linea)).
                        doubleValue();
                double saldoRemActual = ((BigDecimal) Ognl.getValue(expresionSaldoRem, linea)).
                        doubleValue();
              //  debug("saldoActual-"+saldoActual);
                // Revisamos si se excede el saldo de la linea:
                if (saldoActual - montoUsd < 0.0 || saldoRemActual - montoRemUsd < 0.0) {
                    linea.setNumeroExcepciones(linea.getNumeroExcepciones() + 1);
                    linea.setNumeroExcepcionesMes(linea.getNumeroExcepcionesMes() + 1);
                    if (deal.isPagoAnticipado() || deal.isTomaEnFirme()) {
                        deal.setEvento(Deal.EV_SOLICITUD, Deal.EV_IND_GRAL_PAG_ANT);
                    }
                    update(deal);
                    linea.setUltimaModificacion(new Date());
                    update(linea);
                    // Salimos sin afectar la linea:
                    return null;
                }
            }
            deal.setEvento(Deal.EV_USO, Deal.EV_IND_GRAL_PAG_ANT);
            deal.setMontoEquivLc(BDUtils.generar2(deal.getTotalRecibimos()));
            update(deal);
            // Se hace el uso de cada detalle de recibimos:
            for (Iterator it = deal.getRecibimos().iterator(); it.hasNext();) {
                DealDetalle det = (DealDetalle) it.next();
                if (!det.isCancelado()) {
                    double mTmp = det.getMontoUSD();
                    double montoDetUsd = mTmp == 0.0 ? Redondeador.redondear2Dec(
                            det.getImporte() / factorUsd * factorRiesgo) : mTmp * factorRiesgo;
                    LineaCambioLog lcl = new LineaCambioLog(linea, LineaCambioLog.OPER_USO,
                            BDUtils.generar2(montoDetUsd), det, BDUtils.generar6(factorUsd),
                            "Uso de l\u00ednea de cr\u00e9dito",
                            det.getDeal().getUsuario());
                    store(lcl);
                    boolean remesa = getFormasPagoLiqService().isRemesa(ticket,
                                    det.getClaveFormaLiquidacion(), det.getMnemonico(),
                            det.isRecibimos());
                    usarLiberar(linea, BDUtils.generar2(montoDetUsd),
                            getFechaValor(det.getDeal()), true, remesa);
                }
            }
        }
        catch (OgnlException e) {
            warn(e.getMessage(), e);
            throw new SicaException("No se pudo calcular la fecha valor para la afectar la " +
                    "l\u00ednea de cr\u00eadito.", e);
        }
        return null;
    }

    /**
     * Regresa la fecha valor que debe afectarse para el deal especificado.
     *
     * @param deal El deal a revisar.
     * @return String.
     */
    private String getFechaValor(Deal deal) {
        PizarronServiceData psd = getPizarronServiceData();
        return psd.fechaValorParaCancelacion(deal.getFechaCaptura(),
                deal.getTipoValor(), true);
    }

    /**
     * @deprecated ya no utilizar este metodo
     * @param linea
     * @param importeUsd
     * @param fechaValor
     * @param usar
     * @param remesas
     * @throws OgnlException
     */
    private void usarLiberar(LineaCambio linea, BigDecimal importeUsd, String fechaValor,
                             boolean usar, boolean remesas) throws OgnlException {
      
        String expresionUso = getExpresionParaFechaValor(fechaValor, true, false);
        BigDecimal usoActual = (BigDecimal) Ognl.getValue(expresionUso, linea);
        BigDecimal nuevoUso = usar ? usoActual.add(importeUsd) : usoActual.subtract(importeUsd);
        if (nuevoUso != null && nuevoUso.doubleValue() < 0.0) {
            nuevoUso = BDUtils.generar2(0);
        }
        Ognl.setValue(expresionUso, linea, nuevoUso);
        if (remesas) {
            String expresionUsoRem = getExpresionParaFechaValor(fechaValor, true, true);
            BigDecimal usoRemActual = (BigDecimal) Ognl.getValue(expresionUsoRem, linea);
            BigDecimal nuevoUsoRem = usar ?
                    usoRemActual.add(importeUsd) :
                    usoRemActual.subtract(importeUsd);
            if (nuevoUsoRem != null && nuevoUsoRem.doubleValue() < 0.0) {
                nuevoUsoRem = BDUtils.generar2(0);
            }
            Ognl.setValue(expresionUsoRem, linea, nuevoUsoRem);
        }
        linea.setUltimaModificacion(new Date());

        // Si el adeudo es menor al l&iacute;mite permitido y la l&iacute;nea est&aacute; suspendida
        // se reactiva:
        if (linea.getUsoCash() != null) {
            ParametroSica param = findParametro(ParametroSica.LIM_ADEUDO_LIN_CAM);
            double limiteAdeudo = Double.valueOf(param.getValor()).doubleValue();

            if (linea.getUsoCash().doubleValue() <= limiteAdeudo &&
                    LineaCambio.STATUS_SUSPENDIDA.equals(linea.getStatusLinea()) &&
                    lineaEstuvoActivada(linea.getIdLineaCambio().intValue())) {
                linea.setStatusLinea(LineaCambio.STATUS_ACTIVADA);
            }
        }
        update(linea);
    }

    /**
     * Valida que el deal tenga detalles aplicables a l&iacute;neas de cr&eacute;dito. 
     *
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param deal El deal a revisar.
     */
    public double revisarAplicacionLineaCredito(String ticket,  Deal deal) {
        boolean aplicaLC = false;
        int nulos = 0;
        double montoUsdAplicableLC = 0.0;
        // Primero para tomaEnFirme:
        if (deal.isTomaEnFirme() || deal.isPagoAnticipado()) {
            for (Iterator it = deal.getDetalles().iterator(); it.hasNext();) {
                DealDetalle det = (DealDetalle) it.next();
                if (!det.isCancelado() && det.getMnemonico() == null){
                	nulos++;
                }
                if (det.isAltaOProcesandoseOLiquidado()) {
                    if (validarAplicablesTfPagAnt(ticket, det.getMnemonico(),
                            det.getClaveFormaLiquidacion(), det.isRecibimos())) {
                    	
                    	if((deal.isCompra() && det.isRecibimos()) || 
                    			(!deal.isCompra() && !det.isRecibimos())){
	                        aplicaLC = true;
	                        if(det.getMontoUSD() == 0.0 ){
	                        	montoUsdAplicableLC = montoUsdAplicableLC + 
	                        			Redondeador.redondear2Dec(getMontoUsdDealDetalle(det));
	                		}else{
	                			montoUsdAplicableLC = montoUsdAplicableLC + det.getMontoUSD();
	                		}
                    	}
                    }
                }
            }
            if (!aplicaLC) {
                deal.setTomaEnFirme(false);
                deal.setPagoAnticipado(false);
                throw new SicaException("No se encontraron formas de pago o cobro aplicables  " +
                        "a la L\u00ednea de Cr\u00e9dito " +
                        (nulos > 0 ? " o falta completar informaci\u00f3n en los detalles." : "."));
            }
        }
        
       return montoUsdAplicableLC;
       
    }

    /**
     * Regresa true si el mnem&oacute;nico especificado est&aacute; marcado para usar l&iacute;nea
     * de cr&eacute;dito de alg&uacute;n tipo.
     *
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param mnemonico El mnem&oacute;nico a revisar, puede ser null si hay claveFormaLiquidacion.
     * @param claveFormaLiquidacion La clave del producto, puede ser null si hay mnemonico.
     * @param recibimos True para recibimos, False para entregamos.
     * @return boolean.
     */
    public boolean validarAplicablesTfPagAnt(String ticket, String mnemonico,
                                              String claveFormaLiquidacion, boolean recibimos) {
        List fpls = getDealService().getFormasPagoLiqService().getFormasTiposLiq(ticket);
        List mnemonicosAplicables = new ArrayList();
        for (Iterator iterator = fpls.iterator(); iterator.hasNext();) {
            FormaPagoLiq fpl = (FormaPagoLiq) iterator.next();
            if (recibimos == fpl.isRecibimos().booleanValue()) {
                if ((mnemonico != null && fpl.getMnemonico().equals(mnemonico)) ||
                        (mnemonico == null &&
                                fpl.getClaveFormaLiquidacion().equals(claveFormaLiquidacion))) {
                    mnemonicosAplicables.add(fpl);
                }
            }
        }
        for (Iterator it = mnemonicosAplicables.iterator(); it.hasNext();) {
            FormaPagoLiq fpl = (FormaPagoLiq) it.next();
            int tipoLinea = fpl.getUsaLineaCredito().intValue();
           
            if ( tipoLinea > 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * Regresa el valor de una expresi&oacute;n OGNL v&aacute;lida para obtener el uso o saldo de
     * una l&iacute;nea de cr&eacute;dito.
     *
     * @param fechaValor CASH | TOM | SPOT | 72HR | VFUT.
     * @param uso true para uso, false para saldo.
     * @param remesas true para remesas, false para global.
     * @return usoCash | saldoCash | usoTom | saldoTom ...
     */
    private String getExpresionParaFechaValor(String fechaValor, boolean uso, boolean remesas) {
        String expresion;
        fechaValor = fechaValor.trim();
        if (Constantes.CASH.equals(fechaValor)) {
            expresion = (uso ? "uso" : "saldo") + (remesas ? "Rem" : "") + "Cash";
        }
        else if (Constantes.TOM.equals(fechaValor)) {
            expresion = (uso ? "uso" : "saldo") + (remesas ? "Rem" : "") + "Tom";
        }
        else if (Constantes.SPOT.equals(fechaValor)) {
            expresion = (uso ? "uso" : "saldo") + (remesas ? "Rem" : "") + "Spot";
        }
        else if (Constantes.HR72.equals(fechaValor)) {
            expresion = (uso ? "uso" : "saldo") + (remesas ? "Rem" : "") + "72Hr";
        }
        else if (Constantes.VFUT.equals(fechaValor)) {
            expresion = (uso ? "uso" : "saldo") + (remesas ? "Rem" : "") + "96Hr";
        }
        else {
            throw new SicaException("No se encontr\u00f3 la fecha valor " + fechaValor);
        }
        return expresion;
    }

    public void liberarRemesa(String ticket, Actividad actividad, IUsuario usuario) {
        actividad.setFechaTerminacion(new Date());
        actividad.setUsuario(usuario);
        actividad.setResultado(Actividad.RES_NORMAL);
        actividad.setStatus(Actividad.STATUS_TERMINADO);
        update(actividad);
        
    }

    /**
     * Encuentra el VaR para la mesa principal, para la divisa D&oacute;lar y regresa el porcentaje
     * de consumo para este l&iacute;mite.
     *
     * @param deal El deal a revisar.
     * @return double.
     */
    private BigDecimal getFactorRiesgo(Deal deal) {
        if (deal.getFactorRiesgo() != null) {
            return BDUtils.generar6(deal.getFactorRiesgo().doubleValue());
        }
        int horizonteDeTiempo = getDiasDiferencia(deal.getFechaLiquidacion());
        RiesgosService riesgosService = (RiesgosService) _appContext.getBean("riesgosService");
        Map resultados = riesgosService.obtenerMonitorRiesgos(new Integer(1), Divisa.DOLAR,
                new Integer(horizonteDeTiempo));
        List posiciones = (List) resultados.get("posicionesMesas");
        for (Iterator it = posiciones.iterator(); it.hasNext();) {
            PosicionMesaDto pmDto = (PosicionMesaDto) it.next();
            if (pmDto.getMesaCambio().getIdMesaCambio() == 1) {
                for (Iterator it2 = pmDto.getLimites().iterator(); it2.hasNext();) {
                    Limite lim = (Limite) it2.next();
                    if (TipoLimite.V == lim.getTipoLimite().getIdTipoLimite()) {
                        return BDUtils.generar6(lim.getPorcentajeConsumo() / 100.00);
                    }
                }
            }
        }
        return BDUtils.generar6(1);
    }

    private Map getUsosLiberacionesParaDeal(String ticket, List usosLiberaciones, Deal deal) {
        BigDecimal paytf = BDUtils.generar2(0);
        BigDecimal fechaValor = BDUtils.generar2(0);
        for (Iterator it = usosLiberaciones.iterator(); it.hasNext();) {
            LineaCambioLog lcl = (LineaCambioLog) it.next();
            if (lcl.getDealDetalle().getDeal().getIdDeal() == deal.getIdDeal()) {
                boolean agregar = LineaCambioLog.OPER_USO.equals(lcl.getTipoOper().trim());
                DealDetalle det = lcl.getDealDetalle();
                boolean isPAyTF = deal.getTipoValor().equals(Constantes.CASH);
                if (agregar) {
                    paytf = paytf.add(lcl.getImporte());
                    if (!isPAyTF) {
                        fechaValor = fechaValor.add(lcl.getImporte());
                    }
                }else{
                    paytf = paytf.subtract(lcl.getImporte());
                    if (!isPAyTF) {
                        fechaValor = fechaValor.subtract(lcl.getImporte());
                    }
                }
            }
        }
        // Si paytf == 0, el deal no ha hecho uso de la linea, lo hallamos directamente del deal:
        if (paytf.doubleValue() < 0.01) {
            BigDecimal riesgo = BDUtils.generar6(1).add(getFactorRiesgo());
            paytf = BDUtils.generar2(deal.getMontoUSD()).multiply(riesgo);
            fechaValor = BDUtils.generar2(deal.getMontoUSD()).multiply(riesgo);
        }
        Map resultados = new HashMap();
        resultados.put("paytf", paytf);
        resultados.put("fechaValor", fechaValor);
        return resultados;
    }

    public List findDealsLineaCambioCliente(String ticket, Integer idCliente) {
        Persona corporativo = findCorporativo(idCliente);
        List empresas = new ArrayList();
        if (corporativo != null) {
            empresas = findSubsidiariosByPersonaMoral(corporativo.getIdPersona());
        empresas.add(0, corporativo);
        }
        else {
            empresas.add(find(Persona.class, idCliente));
        }
        StringBuffer sb = new StringBuffer();
        int i = 0;
        for (Iterator it = empresas.iterator(); it.hasNext(); i++) {
            Persona persona = (Persona) it.next();
            ContratoSica cs = findContratoSicaByIdPersona(persona.getIdPersona());
            if (cs != null) {
                if (i > 0) {
                    sb = sb.append(",");
                }
                sb = sb.append("'").append(cs.getNoCuenta()).append("'");
            }
        }
        if (sb.toString().trim().length() < 1) {
            return new ArrayList();
        }
        List deals = new ArrayList(new HashSet(getHibernateTemplate().find("FROM Deal AS d INNER " +
                "JOIN FETCH d.detalles AS det LEFT JOIN FETCH det.plantilla WHERE " +
                "d.fechaLiquidacion >= ? AND (d.pagoAnticipado = 'S' OR d.tomaEnFirme = 'S') AND " +
                "d.statusDeal != ? AND d.reversado = 0 AND d.contratoSica.noCuenta in (" +
                sb.toString() + ")",
                new Object[]{DateUtils.inicioDia(), Deal.STATUS_DEAL_CANCELADO})));
        if (deals.isEmpty()) {
            return new ArrayList();
        }
        final List idsDeals = new ArrayList();
        for (Iterator it = deals.iterator(); it.hasNext();) {
            Deal deal = (Deal) it.next();
            idsDeals.add(new Integer(deal.getIdDeal()));
        }
        List usosLiberaciones = getHibernateTemplate().executeFind(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException {
                String sql = "FROM LineaCambioLog AS lcl INNER JOIN FETCH lcl.dealDetalle.divisa " +
                        "WHERE lcl.dealDetalle.deal.idDeal  in (:idsDeals) AND lcl.tipoOper in " +
                        "('U', 'L')";
                return session.createQuery(sql).setParameterList("idsDeals", idsDeals,
                        new IntegerType()).list();
            }
        });
        List renglones = new ArrayList();
        for (Iterator it = deals.iterator(); it.hasNext();) {
            Deal deal = (Deal) it.next();
            renglones.add(getMapaMontoParaConsulta(deal, getUsosLiberacionesParaDeal(ticket,
                    usosLiberaciones, deal)));
        }
        return renglones;
    }

    private Map getMapaMontoParaConsulta(Deal deal, Map usosLiberaciones) {
        String fv = getPizarronServiceData().fechaValorParaCancelacion(deal.getFechaCaptura(),
                deal.getTipoValor(), true);
        Map reng = new HashMap();
        reng.put("idDeal", new Integer(deal.getIdDeal()));
        reng.put("cash", BDUtils.generar2(0));
        reng.put("tom", BDUtils.generar2(0));
        reng.put("spot", BDUtils.generar2(0));
        reng.put("hr72", BDUtils.generar2(0));
        reng.put("hr96", BDUtils.generar2(0));
        
        BigDecimal paytf = (BigDecimal) usosLiberaciones.get("paytf");
        BigDecimal fechaValor = (BigDecimal) usosLiberaciones.get("fechaValor");
        if (Constantes.CASH.equals(fv.trim())) {
            reng.put("cash", paytf);
		} else if (Constantes.TOM.equals(fv.trim())) {
			reng.put("tom", fechaValor);
		} else if (Constantes.SPOT.equals(fv.trim())) {
			reng.put("spot", fechaValor);
		} else if (Constantes.HR72.equals(fv.trim())) {
			reng.put("hr72", fechaValor);
		} else if (Constantes.VFUT.equals(fv.trim())) {
			reng.put("hr96", fechaValor);
		}
        reng.put("factorRiesgo", getFactorRiesgo());
        return reng;
    }

    /**
     * Regresa la referencia al bean 'formasPagoLiqService' configurado en el applicationContext de
     * Spring.
     *
     * @return FormasPagoLiqService.
     */
    private FormasPagoLiqService getFormasPagoLiqService() {
        return (FormasPagoLiqService) _appContext.getBean("formasPagoLiqService");
    }

    /**
     * Regresa el valor de pizarronServiceData.
     *
     * @return PizarronServiceData.
     */
    public PizarronServiceData getPizarronServiceData() {
        return pizarronServiceData;
    }

    /**
     * Establece el valor de pizarronServiceData.
     *
     * @param pizarronServiceData El valor a asignar.
     */
    public void setPizarronServiceData(PizarronServiceData pizarronServiceData) {
        this.pizarronServiceData = pizarronServiceData;
    }

    /**
     * Referencia al bean 'pizarronServiceData' o 'jtaPizarronServiceData'.
     */
    private PizarronServiceData pizarronServiceData;

    /**
     * @see LineaCambioServiceData#findCatalogoTipoAutorizacion()
     */
	public List findCatalogoTipoAutorizacion() {
		List resultList = getHibernateTemplate().find("from TipoAutorizacion");
		return resultList;
	}

	/**
	 * @see LineaCambioServiceData#findCatalogoFormalizacion()
	 */
	public List findCatalogoFormalizacion() {
		List resultList = getHibernateTemplate().find("from Formalizacion");
		return resultList;
	}

	/**
	 * @see LineaCambioServiceData#findCatalogoInstanciaFacultada()
	 */
	public List findCatalogoInstanciaFacultada() {
		List resultList = getHibernateTemplate().find("from InstanciaFacultada");
		return resultList;
	}

	/**
	 * @see LineaCambioServiceData#aplicarUsoLCDetalleSplit(DealDetalle)
	 */
	public void aplicarUsoLCDetalleSplit(DealDetalle det) {
		
		Deal deal= det.getDeal();
		
		LineaCambio linea = findLineaCambioParaCliente(deal.getCliente().getIdPersona());
		
		if(linea!= null && linea.getStatusLinea().equals(LineaCreditoConstantes.STATUS_ACTIVADA)){
			boolean isCompraDeal = deal.isCompra();
	    	boolean isCompraDealDetalle = det.isRecibimos();
	    	double factorExposicionPotencial = getFactorRiesgo().doubleValue();
			double montoDetallePAyTF = Redondeador.redondear2Dec(det.getMontoUSD());
			double montoDetallelFV = Redondeador.redondear2Dec(det.getMontoUSD() * factorExposicionPotencial);
			
			if(montoDetallePAyTF == 0.0 ){
				montoDetallePAyTF = Redondeador.redondear2Dec(getMontoUsdDealDetalle(det));
    		}
			
			if(montoDetallelFV == 0.0 ){
				montoDetallelFV = Redondeador.redondear2Dec(getMontoUsdDealDetalle(det) * factorExposicionPotencial );
    		}
			
	    	if ((isCompraDeal && isCompraDealDetalle) || (!isCompraDeal && !isCompraDealDetalle)) {
	    		insertarUsoLineaCambioLog(det, deal, linea,montoDetallePAyTF,montoDetallelFV);
	    	}
		}
		
	}

	private void insertarUsoLineaCambioLog(DealDetalle det, Deal deal,
			LineaCambio linea, double montoDetallePAyTF, double montoDetallelFV) {
		
		if(deal.getTipoValor().equals(Constantes.CASH)){
			if(deal.isPagoAnticipado() || deal.isTomaEnFirme()){
				LineaCambioLog lclPAyTF = new LineaCambioLog(linea, LineaCambioLog.OPER_USO,
						new BigDecimal(montoDetallePAyTF), det, BDUtils.generar6(det.getFactorDivisa().doubleValue()),
			             LineaCreditoMensajes.USO,
			             det.getDeal().getUsuario());
			     store(lclPAyTF);
			}
		}else{
			if(deal.isPagoAnticipado() || deal.isTomaEnFirme()){
				LineaCambioLog lclPAyTF = new LineaCambioLog(linea, LineaCambioLog.OPER_USO,
						new BigDecimal(montoDetallePAyTF), det, BDUtils.generar6(det.getFactorDivisa().doubleValue()),
			             LineaCreditoMensajes.USO,
			             det.getDeal().getUsuario());
			     store(lclPAyTF);
				LineaCambioLog lclFV = new LineaCambioLog(linea, LineaCambioLog.OPER_USO,
						new BigDecimal(montoDetallelFV), det, BDUtils.generar6(det.getFactorDivisa().doubleValue()),
			             LineaCreditoMensajes.USO,
			             det.getDeal().getUsuario());
			    store(lclFV);
			}else{
				LineaCambioLog lclFV = new LineaCambioLog(linea, LineaCambioLog.OPER_USO,
						new BigDecimal(montoDetallelFV), det, BDUtils.generar6(det.getFactorDivisa().doubleValue()),
			             LineaCreditoMensajes.USO,
			             det.getDeal().getUsuario());
			    store(lclFV);
			}
		}
	}

	public void insertarLiberacionLineaCambioLog(DealDetalle detalle) {
		Deal deal= detalle.getDeal();
		if(deal.getCliente() != null){
			LineaCambio linea = findLineaCambioParaCliente(deal.getCliente().getIdPersona());
		
			if(linea!= null && linea.getStatusLinea().equals(LineaCreditoConstantes.STATUS_ACTIVADA)){
				boolean isCompraDeal = deal.isCompra();
		    	boolean isCompraDealDetalle = detalle.isRecibimos();
		    	double factorExposicionPotencial = getFactorRiesgo().doubleValue();
				double montoDetallePAyTF = Redondeador.redondear2Dec(detalle.getMontoUSD() );
				double montoDetallelFV = Redondeador.redondear2Dec(detalle.getMontoUSD() * factorExposicionPotencial);
				
				if(montoDetallePAyTF == 0.0 ){
					montoDetallePAyTF = Redondeador.redondear2Dec(getMontoUsdDealDetalle(detalle));
	    		}
				
				if(montoDetallelFV == 0.0 ){
					montoDetallelFV = Redondeador.redondear2Dec(getMontoUsdDealDetalle(detalle) * factorExposicionPotencial );
	    		}
				
				boolean isDetalleaLiberarLC = isDetalleALiberarLC(detalle);
	
		    	if (isDetalleaLiberarLC && ( (isCompraDeal && isCompraDealDetalle) || (!isCompraDeal && !isCompraDealDetalle))){
		    		insertarLiberacionLineaCambioLog(detalle, deal, linea,montoDetallePAyTF,montoDetallelFV);
		    	}
			}
		}
	}

	/**
	 * Inserta Logs de Linea de Credito
	 * @param detOrig
	 * @param deal
	 * @param linea
	 * @param montoDetallePAyTF
	 * @param montoDetallelFV
	 */
	private void insertarLiberacionLineaCambioLog(DealDetalle detOrig,
			Deal deal, LineaCambio linea, double montoDetallePAyTF, double montoDetallelFV) {
		
		if(deal.getTipoValor().equals(Constantes.CASH)){
			
			if(deal.isPagoAnticipado() || deal.isTomaEnFirme()){
				LineaCambioLog lclPAyTF = new LineaCambioLog(linea, LineaCambioLog.OPER_LIBERACION,
			             new BigDecimal(montoDetallePAyTF), detOrig, BDUtils.generar6(detOrig.getFactorDivisa().doubleValue()),
			             LineaCreditoMensajes.LIBERACION,
			             detOrig.getDeal().getUsuario());
			     store(lclPAyTF);
			}
			
		}else{
			if(deal.isPagoAnticipado() || deal.isTomaEnFirme()){
				
				LineaCambioLog lclPAyTF = new LineaCambioLog(linea, LineaCambioLog.OPER_LIBERACION,
			             new BigDecimal(montoDetallePAyTF), detOrig, BDUtils.generar6(detOrig.getFactorDivisa().doubleValue()),
			             LineaCreditoMensajes.LIBERACION,
			             detOrig.getDeal().getUsuario());
			     store(lclPAyTF);
			     LineaCambioLog lclFV = new LineaCambioLog(linea, LineaCambioLog.OPER_LIBERACION,
			             new BigDecimal(montoDetallelFV), detOrig, BDUtils.generar6(detOrig.getFactorDivisa().doubleValue()),
			             LineaCreditoMensajes.LIBERACION,
			             detOrig.getDeal().getUsuario()); 
			     store(lclFV);
				
			}else{
				
				LineaCambioLog lclFV = new LineaCambioLog(linea, LineaCambioLog.OPER_LIBERACION,
			             new BigDecimal(montoDetallelFV), detOrig, BDUtils.generar6(detOrig.getFactorDivisa().doubleValue()),
			             LineaCreditoMensajes.LIBERACION,
			             detOrig.getDeal().getUsuario()); 
			     store(lclFV);
			}
		}
	}

	/**
	 * @see LineaCambioServiceData#aplicarUsoLCDetalleGoma(DealDetalle)
	 */
	public void aplicarUsoLCDetalleGoma(DealDetalle det) {
		
		Deal deal= det.getDeal();
		LineaCambio linea = findLineaCambioParaCliente(deal.getCliente().getIdPersona());
		if(linea!= null && linea.getStatusLinea().equals(LineaCreditoConstantes.STATUS_ACTIVADA)){
			boolean isCompraDeal = deal.isCompra();
	    	boolean isCompraDealDetalle = det.isRecibimos();
	    	double factorExposicionPotencial = getFactorRiesgo().doubleValue();
			double montoDetallePAyTF = Redondeador.redondear2Dec(det.getMontoUSD());
			double montoDetallelFV = Redondeador.redondear2Dec(det.getMontoUSD()  * factorExposicionPotencial);
			
			if(montoDetallePAyTF == 0.0 ){
				montoDetallePAyTF = Redondeador.redondear2Dec(getMontoUsdDealDetalle(det));
    		}
			
			if(montoDetallelFV == 0.0 ){
				montoDetallelFV = Redondeador.redondear2Dec(getMontoUsdDealDetalle(det) * factorExposicionPotencial );
    		}
			
	    	if ((isCompraDeal && isCompraDealDetalle) || (!isCompraDeal && !isCompraDealDetalle)) {
	    		insertarUsoLineaCambioLog(det, deal, linea, montoDetallePAyTF, montoDetallelFV);
	    	}
		}
	}

	
	

//	public void aplicarUsoLCDetalleMxn(Deal deal, DealDetalle detMxn) {
//		
//		double montoDetalleUsd = 0.0;
//		double montoDetalleMxn    = detMxn.getImporte()+ detMxn.getComisionCobradaMxn();
//		List recibimosList = new ArrayList();
//		List entregamosList= new ArrayList();
//		double montoRecibimosUsd = 0.0;
//		double montoEntregamosUsd = 0.0;
//		double montoRecibimosMxn = 0.0;
//		double montoEntregamosMxn = 0.0;
//		double montoComisionDealUsd = 0.0;
//		double balanceDealMxn = deal.getBalance();
//
//		recibimosList = deal.getRecibimos();
//		for (Iterator iterator = recibimosList.iterator(); iterator.hasNext();) {
//			DealDetalle detalle = (DealDetalle) iterator.next();
//			if (!detalle.isCancelado()){
//				montoRecibimosUsd = montoRecibimosUsd +  detalle.getMontoUSD();
//				if(detalle.isPesos()){
//					montoRecibimosMxn = montoRecibimosMxn + detalle.getMonto();
//				}
//			}
//		}
//		
//		entregamosList =  deal.getEntregamos();
//		for (Iterator iterator = entregamosList.iterator(); iterator
//				.hasNext();) {
//			DealDetalle detalle = (DealDetalle) iterator.next();
//			if (!detalle.isCancelado()){
//				montoEntregamosUsd = montoEntregamosUsd +  detalle.getMontoUSD() + detalle.getComisionCobradaUsd();
//				montoComisionDealUsd = montoComisionDealUsd + detalle.getComisionCobradaUsd();
//				if(detalle.isPesos()){
//					montoEntregamosMxn = montoEntregamosMxn + detalle.getMonto();
//				}
//			}
//		} 
//		
//		if(deal.isCompra() && detMxn.isRecibimos()){
//			
//			montoDetalleUsd = montoEntregamosUsd - montoRecibimosUsd;
//			
//			if (Math.abs(balanceDealMxn) < 0.01) {
//					if(deal.isPagoAnticipado()){
//						consumirLineaCreditoByDetalleMxnPA(deal,detMxn,montoDetalleUsd);
//					}else{
//						consumirLineaCreditoDetalleMxn(deal,detMxn,montoDetalleUsd);
//					}
//	    	}
//		}else if (deal.isCompra() && detMxn.isRecibimos()){
//			montoDetalleUsd = deal.getMontoUSD() - (montoEntregamosUsd + montoComisionDealUsd);
//			
//			if (Math.abs(balanceDealMxn - montoDetalleMxn) < 0.01) {
//				if(deal.isPagoAnticipado()){
//					consumirLineaCreditoByDetalleMxnPA(deal,detMxn,montoDetalleUsd);
//				}else{
//					consumirLineaCreditoDetalleMxn(deal,detMxn,montoDetalleUsd);
//					
//				}
//    	}
//		}
//	}

//	private void consumirLineaCreditoDetalleMxn(Deal deal, DealDetalle detMxn,
//			double montoDetalleUsd) {
//
//		LineaCambio linea= null;
//    	if(deal.getCliente()!= null){
//    		linea = findLineaCambioParaCliente(deal.getCliente().getIdPersona());
//    	}
//		if(Constantes.CASH.equals(deal.getTipoValor())){
//			return ;
//		}else if(linea == null || !linea.getStatusLinea().equals(LineaCreditoConstantes.STATUS_ACTIVADA)) {
//			throw new SicaException(LineaCreditoMensajes.OPERACION_NO_CASH);
//		}else if(linea != null && linea.getStatusLinea().equals(LineaCreditoConstantes.STATUS_ACTIVADA)){
//			consumirLineaCreditoFechaValorDetalleMxn(linea,deal,detMxn, montoDetalleUsd);
//		}
//	}

//	private void consumirLineaCreditoFechaValorDetalleMxn(LineaCambio linea,
//			Deal deal, DealDetalle detMxn, double montoDetUsd) {
//		boolean isCompraDeal = deal.isCompra();
//    	boolean isCompraDealDetalle = detMxn.isRecibimos();
//    	BigDecimal uso;
//    	double factorExposicionPotencial = getFactorRiesgo().doubleValue();
//    	
//    	if ((isCompraDeal && isCompraDealDetalle) || (!isCompraDeal && !isCompraDealDetalle)) {
//    		double montoDetalleUsd = Redondeador.redondear2Dec((montoDetUsd + detMxn.getComisionCobradaUsd()) * factorExposicionPotencial);
//    		Assert.isTrue(montoDetalleUsd > 0 , LineaCreditoMensajes.MONTO_DETALLE_CERO);
//		    
//    		if(linea.getSaldoFV()!= null && linea.getSaldoFV().doubleValue() > montoDetalleUsd){
//		        if (deal.getFactorRiesgo() == null || deal.getFactorRiesgo().doubleValue() < 0.000001) {
//		            deal.setFactorRiesgo(BDUtils.generar6(factorExposicionPotencial));
//		        }
//		        
//		        if(Constantes.TOM.equals(deal.getTipoValor())){
//		        	uso = linea.getUsoTom().add(new BigDecimal(montoDetalleUsd));
//		        	linea.setUsoTom(uso);
//		        }else if(Constantes.SPOT.equals(deal.getTipoValor())){
//		        	uso = linea.getUsoSpot().add(new BigDecimal(montoDetalleUsd));
//		        	linea.setUsoSpot(uso);
//		        }else if(Constantes.HR72.equals(deal.getTipoValor())){
//		        	uso = linea.getUso72Hr().add(new BigDecimal(montoDetalleUsd));
//		        	linea.setUso72Hr(uso);
//		        }else if(Constantes.VFUT.equals(deal.getTipoValor())){
//		        	uso = linea.getUso96Hr().add(new BigDecimal(montoDetalleUsd));
//		        	linea.setUso96Hr(uso);
//		        }	
//		        linea.setUltimaModificacion(new Date());
//		        update(linea);
//		        if(!deal.isEvento( Deal.EV_IND_GRAL_PAG_ANT, new String[]{Deal.EV_USO})){
//					deal.setEvento(Deal.EV_USO, Deal.EV_IND_GRAL_PAG_ANT);
//				}
//		        LineaCambioLog lcl = new LineaCambioLog(linea, LineaCambioLog.OPER_USO,
//		                        BDUtils.generar2(montoDetalleUsd), detMxn, new BigDecimal(0),
//		                        LineaCreditoMensajes.USO,
//		                        deal.getUsuario());
//		        store(lcl);
//
//    		}else{
//    			throw new SicaException(LineaCreditoMensajes.REBASA_SALDOFV);
//    		}
//    	}
//		
//	}

//	private void consumirLineaCreditoByDetalleMxnPA(Deal deal,DealDetalle detMxn, double montoDetalleUsd) {
//		LineaCambio linea = findLineaCambioParaCliente(deal.getCliente().getIdPersona());
//		boolean isCompraDeal = deal.isCompra();
//    	boolean isCompraDealDetalle = detMxn.isRecibimos();
//    	double factorExposicionPotencial = getFactorRiesgo().doubleValue();
//    	BigDecimal usoPAyTF;
//    	BigDecimal usoFV;
//    	
//		if ((isCompraDeal && isCompraDealDetalle) || (!isCompraDeal && !isCompraDealDetalle)) {
//    		double montoDetallePAyTF = Redondeador.redondear2Dec((montoDetalleUsd + detMxn.getComisionCobradaUsd()));
//    		double montoDetallelFV = Redondeador.redondear2Dec((montoDetalleUsd + detMxn.getComisionCobradaUsd()) * factorExposicionPotencial);
//    		Assert.isTrue(montoDetallePAyTF > 0 , LineaCreditoMensajes.MONTO_DETALLE_CERO);
//    		Assert.isTrue(montoDetallelFV > 0 , LineaCreditoMensajes.MONTO_DETALLE_CERO);
//    		if(linea.getSaldoPAyTF()!= null && linea.getSaldoPAyTF().doubleValue() > montoDetallePAyTF){
//    			
//    			usoPAyTF = linea.getUsoCash().add(new BigDecimal(montoDetallePAyTF));
//    			
//    			if(deal.getTipoValor().equals(Constantes.CASH)){
//        			linea.setUsoCash(usoPAyTF);
//        			linea.setUltimaModificacion(new Date());
//        			update(linea);	
//    		        LineaCambioLog lcl = new LineaCambioLog(linea, LineaCambioLog.OPER_USO,
//    		                        BDUtils.generar2(montoDetallePAyTF), detMxn, new BigDecimal(0),
//    		                        LineaCreditoMensajes.USO,
//    		                        deal.getUsuario());
//    		        store(lcl);
//    			}else if(linea.getSaldoFV()!= null && linea.getSaldoFV().doubleValue() > montoDetallelFV){
//    		        if (deal.getFactorRiesgo() == null || deal.getFactorRiesgo().doubleValue() < 0.000001) {
//    		            deal.setFactorRiesgo(BDUtils.generar6(factorExposicionPotencial));
//    		        }
//    		        
//    		        if(Constantes.TOM.equals(deal.getTipoValor())){
//    		        	usoFV = linea.getUsoTom().add(new BigDecimal(montoDetallelFV));
//    		        	linea.setUsoTom(usoFV);
//    		        }else if(Constantes.SPOT.equals(deal.getTipoValor())){
//    		        	usoFV = linea.getUsoSpot().add(new BigDecimal(montoDetallelFV));
//    		        	linea.setUsoSpot(usoFV);
//    		        }else if(Constantes.HR72.equals(deal.getTipoValor())){
//    		        	usoFV = linea.getUso72Hr().add(new BigDecimal(montoDetallelFV));
//    		        	linea.setUso72Hr(usoFV);
//    		        }else if(Constantes.VFUT.equals(deal.getTipoValor())){
//    		        	usoFV = linea.getUso96Hr().add(new BigDecimal(montoDetallelFV));
//    		        	linea.setUso96Hr(usoFV);
//    		        }
//    		        
//    		        linea.setUsoCash(usoPAyTF);
//    		        linea.setUltimaModificacion(new Date());
//    		        update(linea);
//    		        
//    		        LineaCambioLog lclPAyTF = new LineaCambioLog(linea, LineaCambioLog.OPER_USO,
//    		                        BDUtils.generar2(montoDetallePAyTF), detMxn, new BigDecimal(0),
//    		                        LineaCreditoMensajes.USO,
//    		                        deal.getUsuario());
//    		        LineaCambioLog lclFV = new LineaCambioLog(linea, LineaCambioLog.OPER_USO,
//	                        BDUtils.generar2(montoDetallelFV), detMxn, new BigDecimal(0),
//	                        LineaCreditoMensajes.USO,
//	                        deal.getUsuario());
//    		        store(lclPAyTF);
//    		        store(lclFV);
//    			}else{
//    				throw new SicaException(LineaCreditoMensajes.REBASA_SALDOFV);
//    			}
//    			
//    			if(!deal.isEvento( Deal.EV_IND_GRAL_PAG_ANT, new String[]{Deal.EV_USO})){
//					deal.setEvento(Deal.EV_USO, Deal.EV_IND_GRAL_PAG_ANT);
//				}
//    			
//    		}else{
//    	    	throw new SicaException(LineaCreditoMensajes.REBASA_SALDOPAYTF);
//    		}
//		}	
//	}

	/**
	 * @see LineaCambioServiceData#validarMontoLineaCreditoPA(Integer, Deal, String, double)
	 */
	public void validarMontoLineaCreditoPA(Integer idPersona, 
												  Deal deal , 
										   String fechaValor, 
									  double montoModificado) {
		double montoDealUSD= 0;
		LineaCambio linea   = findLineaCambioParaCliente(idPersona);
		BigDecimal FEP = getFactorRiesgo();
		montoDealUSD = deal.getMontoUSD();
		//se debe validar si el monto es negativo si es asi no se valida LC , solo se libera
		
		if (montoModificado > 0){
			montoDealUSD = montoModificado;
		}
		
		if(linea == null || !LineaCreditoConstantes.STATUS_ACTIVADA.equals(linea.getStatusLinea())){
			throw new SicaException(LineaCreditoMensajes.LINEA_CREDITO_NO_ACTIVA_REVERSO);
		}
		
		if(fechaValor.equals(Constantes.CASH)){
			if(linea.getSaldoCash().doubleValue() >= montoDealUSD){
				return;
			}
		}else{
			if(linea.getSaldoFV().doubleValue() >=  (montoDealUSD * Redondeador.redondear2Dec(FEP.doubleValue()))){
				return;
			}else{
				throw new SicaException(LineaCreditoMensajes.REBASA_SALDOFV);
			}
		}
	}
	
	/**
	 * @see LineaCambioServiceData#validarLineaCredito(Integer, Deal, String, double, boolean, boolean, boolean, boolean)
	 */
	public void validarLineaCreditoReverso(Integer idPersona, 
			Deal deal , 
			String fechaValor, 
			double montoModificado, 
			boolean isCambioCliente, 
			boolean isCambioFechaValor, 
			boolean isCambioMonto, 
			boolean isCambioTC, String ticket) {
		
		if(fechaValor.equals(Constantes.CASH)){
			return;
		}else{
			
			LineaCambio linea   = findLineaCambioParaCliente(idPersona);
			BigDecimal FEP = getFactorRiesgo();
			
			Map montoAplicableLC = getMontoAplicaLCByDeal(deal,ticket);
			double montoDealUSD = ((Double)montoAplicableLC.get(LineaCreditoConstantes.MONTO_FV)).doubleValue();
			if(linea == null || !LineaCreditoConstantes.STATUS_ACTIVADA.equals(linea.getStatusLinea())){
				throw new SicaException(LineaCreditoMensajes.LINEA_CREDITO_NO_ACTIVA_REVERSO);
			}
			
			if(isCambioFechaValor && isCambioMonto && montoModificado < 0){
				return;
			}
			
			if(montoModificado < 0 && !isCambioCliente && !isCambioMonto && !isCambioTC){
				return;
			}
			
			if (montoModificado > 0){
				montoDealUSD = Redondeador.redondear2Dec(montoModificado * FEP.doubleValue()) ;
			}
			
			if(isCambioFechaValor && !isCambioCliente && !isCambioMonto && !isCambioTC ){
				if(!deal.getTipoValor().equals(Constantes.CASH) && !fechaValor.equals(Constantes.CASH)) 
				return;
			}
			
			if(linea.getSaldoFV().doubleValue() >= montoDealUSD){
				return;
			}else{
				throw new SicaException(LineaCreditoMensajes.REBASA_SALDOFV);
			}
		}
	}


	
	private Map getMontoAplicaLCByDeal(Deal deal, String ticket) {
		double montoAplicableLCPAyTF=0.0;
		double montoAplicableLCFV=0.0;
		
		double FEP = getFactorRiesgo().doubleValue();
		Map montosLCMap = new HashMap();
		
		for (Iterator it = deal.getDetalles().iterator(); it.hasNext();) {
			DealDetalle det    = (DealDetalle) it.next();
			
			if((deal.isCompra() && det.isRecibimos()) || 
        			(!deal.isCompra() && !det.isRecibimos())){
			
				double montoDetallePAyTF= 0.0;
				double montoDetalleFV = 0.0;
				
				boolean bMnemonico = validarAplicablesTfPagAnt(ticket, det.getMnemonico(),
	                    det.getClaveFormaLiquidacion(), det.isRecibimos());
				
				if (bMnemonico && !det.isCancelado()){
					if(det.getMontoUSD() == 0.0){
						montoDetallePAyTF = getMontoUsdDealDetalle(det);
						montoDetalleFV = Redondeador.redondear2Dec(getMontoUsdDealDetalle(det) * FEP);
				   }else{
					   montoDetallePAyTF = Redondeador.redondear2Dec(det.getMontoUSD() );
					   montoDetalleFV = Redondeador.redondear2Dec(det.getMontoUSD() * FEP);
				   }
				}
				
				montoAplicableLCFV = montoAplicableLCFV + montoDetalleFV;
				montoAplicableLCPAyTF = montoAplicableLCPAyTF + montoDetallePAyTF;
			}
		}
		montosLCMap.put(LineaCreditoConstantes.MONTO_PAYTF, new Double(montoAplicableLCPAyTF));
		montosLCMap.put(LineaCreditoConstantes.MONTO_FV, new Double(montoAplicableLCFV));
		
		return montosLCMap;
	}

	/**
	 * @see LineaCambioServiceData#liberarLineaCreditoDealDetalle(String, DealDetalle)
	 */
	
	public LineaCambio liberarLineaCreditoDealDetalle(String ticket, DealDetalle det) {
		Deal deal = det.getDeal();
		if(deal != null && deal.getCliente() != null){
			boolean isDetalleaLiberarLC = isDetalleALiberarLC(det);
			if(isDetalleaLiberarLC){
				if (deal.isPagoAnticipado() || deal.isTomaEnFirme()) {
					return liberarDetalleLineaCreditoPA(ticket,det);
				} else {
					return liberarDetalleLineaCredito(ticket, det);
				}
			}
		}
		return null;
	}

	public List consultarExcesosLineasCreditoActivas(final Date fechaInicioTrimestre,
			                                         final Date fechaFinTrimestre)
	{
		List lineasActivasLog = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				String sql = "FROM LineaCambioLog AS lcl INNER JOIN FETCH lcl.lineaCambio AS lc " +
							 "WHERE (lcl.fechaOperacion >= :fechaInicioTrimestre and lcl.fechaOperacion < :fechaFinTrimestre) " +
							 "  and lcl.tipoOper = 'E' " + 
							 "  and lc.statusLinea = :estatus " +
							 "ORDER BY lc.idLineaCambio";
				
				Query q = session.createQuery(sql);
				q.setParameter("estatus", LineaCambio.STATUS_ACTIVADA);
				q.setParameter("fechaInicioTrimestre", fechaInicioTrimestre, new DateType());
				q.setParameter("fechaFinTrimestre", fechaFinTrimestre, new DateType());
				return q.list();
			}
		});
		
		return lineasActivasLog;
	}
	
	public List consultarLineasCreditoActivas()
	{
		List lineasActivas = getHibernateTemplate().
							 find("FROM LineaCambio AS lc " +
								  "WHERE lc.statusLinea = ? " + 
								  "ORDER BY lc.idLineaCambio", LineaCambio.STATUS_ACTIVADA);
		return lineasActivas;
	}
	
	public LineaCreditoReinicioExceso consultarReinicioTrimestralNoEjecutado()
	{
		LineaCreditoReinicioExceso reinicio = null;
		List reinicios = getHibernateTemplate().findByNamedQuery("findReinicioTrimestre");
		
		if(reinicios != null && reinicios.size() > 0)
			reinicio = (LineaCreditoReinicioExceso)reinicios.get(0);
		
		return reinicio;
	}
	
	public Calendar obtenerFechaInicialTrimestre(int trimestre)
	{
		Calendar fechaTrimestreSolicitado = Calendar.getInstance();
		int mes = 0;
		switch(trimestre)
		{
			case 1:
				mes = Calendar.JANUARY;
			break;
			case 2:
				mes = Calendar.APRIL;
			break;
			case 3:
				mes = Calendar.JULY;
			break;
			case 4:
				mes = Calendar.OCTOBER;
			break;
		}
		
		fechaTrimestreSolicitado.set(Calendar.DAY_OF_MONTH, 1);
		fechaTrimestreSolicitado.set(Calendar.MONTH, mes);
		fechaTrimestreSolicitado.set(Calendar.HOUR_OF_DAY, 0);
		fechaTrimestreSolicitado.set(Calendar.MINUTE, 0);
		fechaTrimestreSolicitado.set(Calendar.SECOND, 0);
		fechaTrimestreSolicitado.set(Calendar.MILLISECOND, 0);
		
		return fechaTrimestreSolicitado;
	}
	
	public void reiniciarContadoresExcesosLineasCreditoActivas(StringBuffer logger)
	{
		boolean aplicarReinicio = false;
		Calendar fechaActual = null, temporal = null;
		Calendar fechasInicialesTrimestre[] = null;
		Date fechaInicial = null;
		Date fechaFinal = null;
		Date fechaSiguienteReinicio = null;
		LineaCreditoReinicioExceso reinicioPendiente = null;
		
		try
		{
			reinicioPendiente = consultarReinicioTrimestralNoEjecutado();
			
			if(reinicioPendiente == null)
			{
				logger.append("No existe registro para validar cuando se aplicara el reinicio de contadores de excesos de " +
						      "las lineas de credito. Fin del proceso de reinicio de contadores.\n");
				return;
			}
			else if(!LineaCreditoReinicioExceso.REINICIO_NO_EJECUTADO.equals(reinicioPendiente.getProcesoEjecutado()))
			{
				logger.append("El reinicio de contadores de excesos ya ha sido ejecutado. Fin del proceso de reinicio de contadores." +
						      "Valor actual: " + reinicioPendiente.getProcesoEjecutado() + "\n");
				return;
			}
			
			fechaActual = Calendar.getInstance();
			fechaActual.setTime(DateUtils.inicioDia());
			fechasInicialesTrimestre = new Calendar[]{obtenerFechaInicialTrimestre(1),
													  obtenerFechaInicialTrimestre(2),
													  obtenerFechaInicialTrimestre(3),
													  obtenerFechaInicialTrimestre(4)};
			
			logger.append("Se verificara si es inicio de un trimestre y de ser así, se validara que el reinicio no haya " +
					      "sido ejecutado para aplicarlo.\n");
			
			// Compara si es inicio de un trimestre y de ser así verifica que el reinicio no ha sido ejecutado
			// para aplicarlo
			for(int indice = 0; indice < fechasInicialesTrimestre.length; indice++)
			{
				// Fecha actual es igual a la fecha de inicio del trimestre
				if(fechaActual.getTime().compareTo(fechasInicialesTrimestre[indice].getTime()) == 0)
				{
					logger.append("Se comprobo que es inicio de un trimestre:" + fechasInicialesTrimestre[indice].getTime() + "\n");
					if(LineaCreditoReinicioExceso.REINICIO_NO_EJECUTADO.equals(reinicioPendiente.getProcesoEjecutado()))
					{
						aplicarReinicio = true;
						logger.append("El reinicio de contadores de excesos no ha sido ejecutado \n");
					}
					
					break;
				}
			}
			
			// Como no se va a aplicar el reinicio por la validacion anterior,
			// verifico si la fecha actual es mayor a la fecha del reinicio que esta programada
			// y si no se ha ejecutado, se aplica el cambio 
			if(aplicarReinicio == false)
			{
				logger.append("Se comprobo que no es inicio de un trimestre, ahora se verificara si la fecha " +
						      "actual es mayor a la fecha del reinicio que esta programado.\n");
				
				fechaActual = Calendar.getInstance();
				// la fecha actual es mayor o igual a la fecha del reinicio programado
				if(fechaActual.getTime().compareTo(reinicioPendiente.getFechaEjecucion()) >= 0 &&
				   LineaCreditoReinicioExceso.REINICIO_NO_EJECUTADO.equals(
											  reinicioPendiente.getProcesoEjecutado()))
				{
					logger.append("La fecha actual es mayor o igual a la fecha en la cual se programo el reinicio de contadores de excesos, " +
							      "se aplicara el reinicio de contadores. \n");
					aplicarReinicio = true;
				}
				else
				{
					logger.append("La fecha actual es manor a la fecha en la cual se programo el reinicio de contadores de excesos, " +
							"no se aplicara el reinicio de contadores de excesos de las lineas de credito activas. \n");
				}
			}
			
			if(aplicarReinicio)
			{
				temporal = Calendar.getInstance();
				temporal.setTimeInMillis(reinicioPendiente.getFechaEjecucion().getTime());
				temporal.add(Calendar.MONTH, -3);
				fechaInicial = new Date(temporal.getTimeInMillis());
				fechaFinal = new Date(reinicioPendiente.getFechaEjecucion().getTime());
				
				temporal.setTimeInMillis(reinicioPendiente.getFechaEjecucion().getTime());
				temporal.add(Calendar.MONTH, 3);
				fechaSiguienteReinicio = new Date(temporal.getTimeInMillis());
				
				aplicarReinicioContadoresExcesos(reinicioPendiente, fechaInicial, fechaFinal, fechaSiguienteReinicio, logger);
			}
			
		}
		catch(Exception e)
		{
			logger.append("Ocurrio un error al realizar el reinicio de los contadores de excesos de las lineas de credito\n");
			logger.append(e.getLocalizedMessage() + "\n");
			e.printStackTrace();
		}
	}
	
	public void aplicarReinicioContadoresExcesos(LineaCreditoReinicioExceso reinicio, Date fechaInicioTrimestre, 
			                                     Date fechaFinTrimestre, Date fechaSiguienteReinicio, StringBuffer logger)
	{
		LineaCreditoReinicioExceso siguienteReinicio = null;
		LineaCambio lc = null;
		LineaCambioLog lcl = null;
		List lineasCreditoActivas = null;
		List excesos = null;
		Map lineasCredito = null;
		Iterator it = null;
		int indice = 0;
		
		try
		{
			lineasCredito = new HashMap();
			lineasCreditoActivas = consultarLineasCreditoActivas();
			
			if(lineasCreditoActivas != null && lineasCreditoActivas.size() > 0)
			{
				logger.append("Se encontraron " + lineasCreditoActivas.size() + " lineas de credito activas. \n");
				for(indice = 0; indice < lineasCreditoActivas.size(); indice++)
				{
					lc = (LineaCambio)lineasCreditoActivas.get(indice);
					if(!lineasCredito.containsKey(lc.getIdLineaCambio()))
					{
						lineasCredito.put(lc.getIdLineaCambio(), lc);
						lc.setExcesoFV(new Integer(0));
						lc.setExcesoPAyTF(new Integer(0));
						logger.append("Se pondran en ceros los excesos de la linea " + lc.getIdLineaCambio().longValue() + "\n");
					}	
				}
				
				excesos = consultarExcesosLineasCreditoActivas(fechaInicioTrimestre, fechaFinTrimestre);
				if(excesos != null && excesos.size() > 0)
				{
					for(indice = 0; indice < excesos.size(); indice++)
					{
						lcl = (LineaCambioLog)excesos.get(indice);
						
						if(!lineasCredito.containsKey(lcl.getLineaCambio().getIdLineaCambio()))
						{
							lc = lcl.getLineaCambio();
							lineasCredito.put(lc.getIdLineaCambio(), lc);
							lc.setExcesoFV(new Integer(0));
							lc.setExcesoPAyTF(new Integer(0));
							logger.append("Se pondran en ceros los contadores de excesos de la linea " + lc.getIdLineaCambio().longValue() + "\n");
						}
						else
							lc = (LineaCambio)lineasCredito.get(lcl.getLineaCambio().getIdLineaCambio());
						
						if(LineaCreditoConstantes.EXCESO_PAYTF.equals(lcl.getObservaciones()))
						{
							logger.append("La linea " + lc.getIdLineaCambio().longValue() + " tiene un exceso de PAyTF: " + lcl.getImporte() + "\n");
							
							if(lc.getImportePAyTF() != null)
							{
								logger.append("Monto actual de importePAyTF de la linea " + lc.getIdLineaCambio().longValue() + ": " + 
										      lc.getImportePAyTF().doubleValue() + "\n");
								
								lc.setImportePAyTF(lc.getImportePAyTF().subtract(lcl.getImporte()));
								if(lc.getImportePAyTF().doubleValue() < 0)
									lc.setImportePAyTF(new BigDecimal("0.0"));
							}
							else
							{
								logger.append("La linea " + lc.getIdLineaCambio().longValue() + " tiene el campo importePAyTF en null. \n");
							}
						}
						
						if(LineaCreditoConstantes.EXCESO_FV.equals(lcl.getObservaciones()))
						{
							logger.append("La linea " + lc.getIdLineaCambio().longValue() + " tiene un exceso de FV: " + lcl.getImporte() + "\n");
							
							if(lc.getImporteFV() != null)
							{
								logger.append("Monto actual de importeFV de la linea " + lc.getIdLineaCambio().longValue() + ": " + 
										      lc.getImporteFV().doubleValue() + "\n");
								lc.setImporteFV(lc.getImporteFV().subtract(lcl.getImporte()));
								if(lc.getImporteFV().doubleValue() < 0)
									lc.setImporteFV(new BigDecimal("0.0"));
							}
							else
							{
								logger.append("La linea " + lc.getIdLineaCambio().longValue() + " tiene el campo importeFV en null. \n");
							}
						}
					}
				}
				
				// Actualizar lineas de cambio (credito)
				if(lineasCredito.size() > 0)
				{
					it = lineasCredito.keySet().iterator();
					while(it.hasNext())
					{
						lc = (LineaCambio)lineasCredito.get((Integer)it.next());
						getHibernateTemplate().update(lc);
						logger.append("Se actualizo la linea " + lc.getIdLineaCambio().longValue() + "\n");
					}
				}
			}
			else
				logger.append("No se encontraron lineas de credito activas. \n");
			
			// actualiza reinicio actual con estatus de ejecutado
			reinicio.setProcesoEjecutado("1");
			getHibernateTemplate().update(reinicio);
			logger.append("Se actualizo el registro del reinicio como procesado.\n");
			
			// agrega siguiente fecha de reinicio de contadores de excesos de las lineas de credito activas
			siguienteReinicio = new LineaCreditoReinicioExceso();
			siguienteReinicio.setFechaEjecucion(fechaSiguienteReinicio);
			siguienteReinicio.setProcesoEjecutado(LineaCreditoReinicioExceso.REINICIO_NO_EJECUTADO);
			getHibernateTemplate().save(siguienteReinicio);
			logger.append("Se ha programado el siguiente reinicio para la fecha: " + fechaSiguienteReinicio + "\n");
		}
		catch(Exception e)
		{
			logger.append("Ocurrio un error al realizar el reinicio de los contadores de excesos de las lineas de credito\n");
			logger.append(e.getLocalizedMessage() + "\n");
			e.printStackTrace();
		}
	}

	public List consultarDatosReporteExceso(final String facultadCanal, final Date fechaInicio, final Date fechaFin)
	{
		List logs = null;
		LineaCambioDao lcDao = (LineaCambioDao) _appContext.getBean("lineaCambioDao");
		ISistema sistema = (ISistema) getHibernateTemplate().get(Sistema.class, "SICA");

		logs = lcDao.consultarDatosReporteExceso(sistema.getIdJerarquia(), facultadCanal, fechaInicio, fechaFin);
		return logs;
	}
	
	public int consultarSiUsuarioPerteneceAreaRiesgos(Integer idUsuario, String sistema)
	{
		LineaCambioDao lcDao = (LineaCambioDao) _appContext.getBean("lineaCambioDao");
		return lcDao.consultarUsuarioAreaRiesgos(idUsuario, sistema);
	}

	
	public void aplicarCambioMontoDetalleLC(DealDetalle detalleOriginal, DealDetalle detalleNuevo) {
		
		Deal deal = detalleOriginal.getDeal();
		LineaCambio linea = findLineaCambioParaCliente(deal.getCliente().getIdPersona());
		double montoDetalleOrigPAyTF = 0.0;
		double montoDetalleOrigFV = 0.0;
		double montoDetalleNuevoPAyTF = 0.0;
		
		double montoAplicarLC = 0.0;
		double factorExposicionPotencial = getFactorRiesgo().doubleValue();
		
		if (detalleOriginal.getMontoUSD() == 0.0){
			 montoDetalleOrigPAyTF = Redondeador.redondear2Dec(getMontoUsdDealDetalle(detalleOriginal));
			 montoDetalleOrigFV = Redondeador.redondear2Dec(getMontoUsdDealDetalle(detalleOriginal) * factorExposicionPotencial);
		}else{
			 montoDetalleOrigPAyTF = Redondeador.redondear2Dec(detalleOriginal.getMontoUSD());
			 montoDetalleOrigFV = Redondeador.redondear2Dec(detalleOriginal.getMontoUSD() * factorExposicionPotencial);
		}
		
		if (detalleNuevo.getMontoUSD() == 0.0){
			montoDetalleNuevoPAyTF = Redondeador.redondear2Dec(getMontoUsdDealDetalle(detalleNuevo));
		}else{
			montoDetalleNuevoPAyTF = Redondeador.redondear2Dec(detalleNuevo.getMontoUSD());
			
		}
		
		montoAplicarLC = montoDetalleNuevoPAyTF - montoDetalleOrigPAyTF;
		
		boolean isDetalleaLiberarLC = isDetalleALiberarLC(detalleOriginal);
		if(isDetalleaLiberarLC){
			insertarLiberacionLineaCambioLog(detalleOriginal, deal, linea, montoDetalleOrigPAyTF, montoDetalleOrigFV);
		}
		
		if(deal.isPagoAnticipado() || deal.isTomaEnFirme()){
			consumirLineaCreditoCambioMontoDealDetallePA(deal, detalleNuevo , montoAplicarLC);
		}else {
			consumirLineaCreditoCambioMontoDealDetalle(deal, detalleNuevo, montoAplicarLC);
		}
		
		
		
	}

	/**
	 * @see LineaCambioServiceData#consumirLineaCreditoCambioMontoDealDetallePA(Deal, DealDetalle, double)
	 */
	public void consumirLineaCreditoCambioMontoDealDetallePA(Deal deal, DealDetalle det, double montoAplicarLC) {
		
		LineaCambio	linea = findLineaCambioParaCliente(deal.getCliente().getIdPersona());
		boolean isCompraDeal = deal.isCompra();
    	boolean isCompraDealDetalle = det.isRecibimos();
    	double factorExposicionPotencial = getFactorRiesgo().doubleValue();
    	BigDecimal usoPAyTF;
    	BigDecimal usoFV;
    	
		if ((isCompraDeal && isCompraDealDetalle) || (!isCompraDeal && !isCompraDealDetalle)) {
    		double montoDetallePAyTF = Redondeador.redondear2Dec(det.getMontoUSD());
    		double montoDetallelFV = Redondeador.redondear2Dec(det.getMontoUSD() * factorExposicionPotencial);
    		double montoAplicarLCPAyTF = Redondeador.redondear2Dec(montoAplicarLC);
    		double montoAplicarLClFV = Redondeador.redondear2Dec(montoAplicarLC * factorExposicionPotencial);
    		
    		Assert.isTrue(montoDetallePAyTF > 0 , LineaCreditoMensajes.MONTO_DETALLE_CERO);
    		Assert.isTrue(montoDetallelFV > 0 , LineaCreditoMensajes.MONTO_DETALLE_CERO);
    		if(linea.getSaldoPAyTF()!= null && linea.getSaldoPAyTF().doubleValue() >= montoDetallePAyTF){
    			
    			usoPAyTF = linea.getUsoCash().add(new BigDecimal(montoAplicarLCPAyTF));
    			
    			if(deal.getTipoValor().equals(Constantes.CASH)){
        			linea.setUsoCash(usoPAyTF);
        			linea.setUltimaModificacion(new Date());
        			update(linea);	
    		        LineaCambioLog lcl = new LineaCambioLog(linea, LineaCambioLog.OPER_USO,
    		                        BDUtils.generar2(montoDetallePAyTF), det, BDUtils.generar6(det.getFactorDivisa().doubleValue()),
    		                        LineaCreditoMensajes.USO,
    		                        deal.getUsuario());
    		        store(lcl);
    			}else if(linea.getSaldoFV()!= null && linea.getSaldoFV().doubleValue() >= montoDetallelFV){
    		        if (deal.getFactorRiesgo() == null || deal.getFactorRiesgo().doubleValue() < 0.000001) {
    		            deal.setFactorRiesgo(BDUtils.generar6(factorExposicionPotencial));
    		        }
    		        if(Constantes.TOM.equals(deal.getTipoValor().trim())){
    		        	usoFV = linea.getUsoTom().add(new BigDecimal(montoAplicarLClFV));
    		        	linea.setUsoTom(usoFV);
    		        }else if(Constantes.SPOT.equals(deal.getTipoValor())){
    		        	usoFV = linea.getUsoSpot().add(new BigDecimal(montoAplicarLClFV));
    		        	linea.setUsoSpot(usoFV);
    		        }else if(Constantes.HR72.equals(deal.getTipoValor())){
    		        	usoFV = linea.getUso72Hr().add(new BigDecimal(montoAplicarLClFV));
    		        	linea.setUso72Hr(usoFV);
    		        }else if(Constantes.VFUT.equals(deal.getTipoValor())){
    		        	usoFV = linea.getUso96Hr().add(new BigDecimal(montoAplicarLClFV));
    		        	linea.setUso96Hr(usoFV);
    		        }
    		        
    		        linea.setUsoCash(usoPAyTF);
    		        linea.setUltimaModificacion(new Date());
    		        update(linea);
    		        
    		        LineaCambioLog lclPAyTF = new LineaCambioLog(linea, LineaCambioLog.OPER_USO,
    		                        BDUtils.generar2(montoDetallePAyTF), det, BDUtils.generar6(det.getFactorDivisa().doubleValue()),
    		                        LineaCreditoMensajes.USO,
    		                        deal.getUsuario());
    		        LineaCambioLog lclFV = new LineaCambioLog(linea, LineaCambioLog.OPER_USO,
	                        BDUtils.generar2(montoDetallelFV), det, BDUtils.generar6(det.getFactorDivisa().doubleValue()),
	                        LineaCreditoMensajes.USO,
	                        deal.getUsuario());
    		        store(lclPAyTF);
    		        store(lclFV);
    			}else{
    				throw new SicaException(LineaCreditoMensajes.REBASA_SALDOFV);
    			}
    			
    			if(!deal.isEvento( Deal.EV_IND_GRAL_PAG_ANT, new String[]{Deal.EV_USO})){
					deal.setEvento(Deal.EV_USO, Deal.EV_IND_GRAL_PAG_ANT);
				}
    			
    		}else{
    	    	throw new SicaException(LineaCreditoMensajes.REBASA_SALDOPAYTF);
    		}
    		
		}	
	}

	/**
	 * @see LineaCambioServiceData#consumirLineaCreditoCambioMontoDealDetalle(Deal, DealDetalle, double)
	 */
	public void consumirLineaCreditoCambioMontoDealDetalle(Deal deal, DealDetalle detalleNuevo, double montoAplicarLC) {
		LineaCambio linea = findLineaCambioParaCliente(deal.getCliente().getIdPersona());
      
		if(Constantes.CASH.equals(deal.getTipoValor())){
			return ;
		}else if(linea == null || !linea.getStatusLinea().equals(LineaCreditoConstantes.STATUS_ACTIVADA)) {
			throw new SicaException(LineaCreditoMensajes.OPERACION_NO_CASH);
		}else if(linea != null && linea.getStatusLinea().equals(LineaCreditoConstantes.STATUS_ACTIVADA)){
			boolean isCompraDeal = deal.isCompra();
	    	boolean isCompraDealDetalle = detalleNuevo.isRecibimos();
	    	
	    	BigDecimal uso;
	    	double factorExposicionPotencial = getFactorRiesgo().doubleValue();
	    	
	    	if ((isCompraDeal && isCompraDealDetalle) || (!isCompraDeal && !isCompraDealDetalle)) {
	    		double montoDetalleUsd = Redondeador.redondear2Dec(detalleNuevo.getMontoUSD() * factorExposicionPotencial);
	    		double montoAplicarLCFV = Redondeador.redondear2Dec(montoAplicarLC * factorExposicionPotencial);

	    		if(montoDetalleUsd == 0.0 ){
	    			montoDetalleUsd = Redondeador.redondear2Dec(getMontoUsdDealDetalle(detalleNuevo) * factorExposicionPotencial);
	    		}
	    		Assert.isTrue(montoDetalleUsd > 0 , LineaCreditoMensajes.MONTO_DETALLE_CERO);
	    		if(linea.getSaldoFV()!= null && linea.getSaldoFV().doubleValue() >= montoDetalleUsd){
			        if (deal.getFactorRiesgo() == null || deal.getFactorRiesgo().doubleValue() < 0.000001) {
			            deal.setFactorRiesgo(BDUtils.generar6(factorExposicionPotencial));
			        }
			        
			        if(Constantes.TOM.equals(deal.getTipoValor().trim())){
			        	uso = linea.getUsoTom().add(new BigDecimal(montoAplicarLCFV));
			        	linea.setUsoTom(uso);
			        }else if(Constantes.SPOT.equals(deal.getTipoValor())){
			        	uso = linea.getUsoSpot().add(new BigDecimal(montoAplicarLCFV));
			        	linea.setUsoSpot(uso);
			        }else if(Constantes.HR72.equals(deal.getTipoValor())){
			        	uso = linea.getUso72Hr().add(new BigDecimal(montoAplicarLCFV));
			        	linea.setUso72Hr(uso);
			        }else if(Constantes.VFUT.equals(deal.getTipoValor())){
			        	uso = linea.getUso96Hr().add(new BigDecimal(montoAplicarLCFV));
			        	linea.setUso96Hr(uso);
			        }	
			        linea.setUltimaModificacion(new Date());
			        getHibernateTemplate().update(linea);
			        if(!deal.isEvento( Deal.EV_IND_GRAL_PAG_ANT, new String[]{Deal.EV_USO})){
						deal.setEvento(Deal.EV_USO, Deal.EV_IND_GRAL_PAG_ANT);
					}
			        LineaCambioLog lcl = new LineaCambioLog(linea, LineaCambioLog.OPER_USO,
			                        BDUtils.generar2(montoDetalleUsd), detalleNuevo, BDUtils.generar6(detalleNuevo.getFactorDivisa().doubleValue()),
			                        LineaCreditoMensajes.USO,
			                        detalleNuevo.getDeal().getUsuario());
			        getHibernateTemplate().save(lcl);

	    		}else{
	    			throw new SicaException(LineaCreditoMensajes.REBASA_SALDOFV);
	    		}
	    	}
		}
	}

	public void validarLineaCredito(DealDetalle detalle, String ticket)
	{
		Deal deal = detalle.getDeal();
		
		if(detalle.getSustituyeA() == null)
		{
			boolean bMnemonico = validarAplicablesTfPagAnt(ticket, detalle.getMnemonico(),
			        detalle.getClaveFormaLiquidacion(), detalle.isRecibimos());
			if(bMnemonico && detalle.getMnemonico()!= null){
			
			    if((deal.isPagoAnticipado() || deal.isTomaEnFirme()) && !deal.isDealBalanceo()){
					consumirLineaCreditoPA(deal, detalle);
				}else if(!deal.isDealBalanceo()){
					consumirLineaCredito(deal, detalle);
				}
			}
		}else{
			aplicarUsoLCDetalleSplit(detalle);
		}
		
		update(detalle);
	}
}

