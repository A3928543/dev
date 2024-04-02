package com.ixe.ods.sica.lineacredito.service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import org.apache.tapestry.ApplicationRuntimeException;
import org.springframework.dao.DataAccessException;

import com.ixe.ods.seguridad.model.IUsuario;
import com.ixe.ods.sica.lineacredito.service.dto.BitacoraLineaCreditoDTO;
import com.ixe.ods.sica.model.Divisa;
import com.ixe.ods.sica.model.LineaCambio;
import com.ixe.ods.sica.model.LineaCambioLog;
import com.ixe.ods.sica.model.ParametroSica;
import com.ixe.ods.sica.sdo.LineaCambioServiceData;
import com.ixe.ods.sica.sdo.SicaServiceData;
import com.ixe.ods.sica.services.LineaCreditoConstantes;

public class LineaCreditoServiceImpl implements LineaCreditoService {
	
	private SicaServiceData sicaServiceData;
	
	public SicaServiceData getSicaServiceData() {
		return sicaServiceData;
	}

	public void setSicaServiceData(SicaServiceData sicaServiceData) {
		this.sicaServiceData = sicaServiceData;
	}

	private LineaCambioServiceData getLcServiceData() {
		LineaCambioServiceData lcServiceData = (LineaCambioServiceData)getSicaServiceData();
		return lcServiceData;
	}
	
	/**
	 *@see LineaCreditoService#getVencimientoDefault()
	 */
	public Date getVencimientoDefault() {
		Calendar gc = new GregorianCalendar();
		gc.add(Calendar.YEAR, 1);
		return gc.getTime();
	}

	/**
	 * @see LineaCreditoService#crearLineaCredito(LineaCambio)
	 */
	public void crearLineaCredito(LineaCambio lineaCredito) {
//		LineaCambioLog lcl = new LineaCambioLog();
		
		sicaServiceData.store(lineaCredito);
        
//		lcl.setLineaCambio(lineaCredito);
//        lcl.setImporte(getLineaCredito().getImporte());
//        lcl.setFechaOperacion(getLineaCredito().
//                getUltimaModificacion());
//        lcl.setUsuario(visit.getUser());
//        if (getLineaCredito().getImporte().doubleValue() <
//                getImporteInicial().doubleValue()) {
//            lcl.setTipoOper("D");
//        }
//        else if (getLineaCredito().getImporte().doubleValue() >
//                getImporteInicial().doubleValue()) {
//            lcl.setTipoOper("A");
//        }
//        lcl.setFactorDivisa(new BigDecimal("" + 0));
//        lcl.setLineaCambio(getLineaCredito());                    
//        getSicaServiceData().store(lcl);
//        setImporteInicial(getLineaCredito().getImporte());
//        setEtiquetaMonto(getMoneyFormat().format(getMonto()));
//        setEtiquetaMontoRemesa(getMoneyFormat().format(getMontoRemesa()));
		
        }

	/**
	 * @see LineaCreditoService#findLineaCreditoByIdPersona(Integer)
	 */
	public LineaCambio findLineaCreditoByIdPersona(Integer idPersona) {
		LineaCambio lc =  getLcServiceData().findLineaCambioParaCliente(idPersona);
		return lc; 
	}
	
	
	/**
	 * @see LineaCreditoService#findLineaCredito(Integer)
	 * 
	 */
	public LineaCambio findLineaCredito(Integer idLineaCredito) {
		LineaCambio lineaCambio = (LineaCambio) getSicaServiceData().find(LineaCambio.class, idLineaCredito);
		return lineaCambio;
	}
	
	/**
     * Regresa la Descripcion del Status de la Linea de Cr&eacute;dito.
     *
     * @return String.
     */
    public String getDescripcionStatus(String statusLinea) {
        if (LineaCreditoConstantes.STATUS_SOLICITUD.equals(statusLinea)) {
            return "Solicitud";
        }
        else if (LineaCreditoConstantes.STATUS_APROBADA.equals(statusLinea)) {
            return "Aprobada";
        }
        else if (LineaCreditoConstantes.STATUS_ACTIVADA.equals(statusLinea)) {
            return "Activada";
        }
        else if (LineaCreditoConstantes.STATUS_VENCIDA.equals(statusLinea)) {
            return "Vencida";
        }
        else if (LineaCreditoConstantes.STATUS_SUSPENDIDA.equals(statusLinea)) {
            return "Suspendida";
        }
        else if (LineaCreditoConstantes.STATUS_CANCELADA.equals(statusLinea)) {
            return "Cancelada";
        }
        return "Desconocido";
    }

	public Integer getNumeroTotalExcesos(String lcConstanteExcesos) {
		return new Integer (getParametroSica(lcConstanteExcesos));
	}

	private String  getParametroSica(String parametro){
	return 	((ParametroSica) getSicaServiceData().
                find(ParametroSica.class, parametro)).getValor();
		
	}

	public List getCatalogoTipoAutorizacion() {
		List tipoAutorizacionList = getLcServiceData().findCatalogoTipoAutorizacion();
		return tipoAutorizacionList;
	}

	public List getCatalogoFormalizacion() {
		List tipoAutorizacionList = getLcServiceData().findCatalogoFormalizacion();
		return tipoAutorizacionList;
	}

	public List getCatalogoInstanciaFacultada() {
		List tipoAutorizacionList = getLcServiceData().findCatalogoInstanciaFacultada();
		return tipoAutorizacionList;
	}

	
	public void cambiarEstatusLineaCredito(String estatus,
			String operacion, Integer idLineaCredito, IUsuario user,String observaciones) {
		try {
			LineaCambio lineaCredito = findLineaCredito(idLineaCredito);
			lineaCredito.setUltimaModificacion(getFechaActual());
			lineaCredito.setStatusLinea(estatus);
			getSicaServiceData().update(lineaCredito);
			crearBitacoraLineaCredito(lineaCredito,user,operacion,observaciones, new BigDecimal("0"));
		} catch (DataAccessException e) {
			e.printStackTrace();		
			throw new ApplicationRuntimeException(
					"Hubo un error al intentar efectuar la "
							+ "operaci\u00f3n sobre la l\u00ednea de cr\u00e9dito.");
		}
	}

	private void crearBitacoraLineaCredito(LineaCambio lineaCredito, IUsuario usuario, String operacion,String observaciones, BigDecimal importe) {
		LineaCambioLog lineaCreditoLog = new LineaCambioLog();
		lineaCreditoLog.setLineaCambio(lineaCredito);
		lineaCreditoLog.setImporte(importe);
		lineaCreditoLog.setFechaOperacion(getFechaActual());
		lineaCreditoLog.setUsuario(usuario);
		lineaCreditoLog.setTipoOper(operacion);
		lineaCreditoLog.setObservaciones(observaciones);
		getSicaServiceData().store(lineaCreditoLog);
	}

	private void crearBitacoraExcesoLineaCredito(LineaCambio lineaCredito, IUsuario usuario, String operacion,String observaciones, BigDecimal importe) {
		LineaCambioLog lineaCreditoLog = new LineaCambioLog();
		lineaCreditoLog.setLineaCambio(lineaCredito);
		lineaCreditoLog.setImporte(importe);
		lineaCreditoLog.setFechaOperacion(getFechaActual());
		lineaCreditoLog.setUsuario(usuario);
		lineaCreditoLog.setTipoOper(operacion);
		lineaCreditoLog.setObservaciones(observaciones);
		lineaCreditoLog.setDivisa((Divisa)getSicaServiceData().find(Divisa.class, "USD"));
		getSicaServiceData().store(lineaCreditoLog);
	}

	

	/**
	 * Obtiene la Fecha Actual del Sistema.
	 *
	 * @return Date La Fecha.
	 */
	public Date getFechaActual() {
		Calendar gc = new GregorianCalendar();
	    return gc.getTime();
	}

	/**
	 * @see LineaCreditoService#actualizarLineaCredito(LineaCambio, List)
	 */
	public void actualizarLineaCredito(LineaCambio lineaCredito, List listaCambios) {
		sicaServiceData.update(lineaCredito);
		for (Iterator iterator = listaCambios.iterator(); iterator.hasNext();) {
			BitacoraLineaCreditoDTO bitacora = (BitacoraLineaCreditoDTO) iterator.next();
			if(TipoOperacion.EXCESO.equals(bitacora.getTipoOperacion()))
				crearBitacoraExcesoLineaCredito(lineaCredito, bitacora.getUsuario(), bitacora.getTipoOperacion(), bitacora.getObservaciones(), bitacora.getImporte());
			else
			crearBitacoraLineaCredito(lineaCredito, bitacora.getUsuario(), bitacora.getTipoOperacion(), bitacora.getObservaciones(), bitacora.getImporte());
		}
	}

	
}


	
	


