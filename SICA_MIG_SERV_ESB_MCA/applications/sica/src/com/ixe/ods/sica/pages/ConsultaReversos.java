/*
 * $Id: ConsultaReversos.java,v 1.4 2008/12/26 23:17:31 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 - 2008 LegoSoft S.C.
 */

package com.ixe.ods.sica.pages;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.tapestry.IRequestCycle;

import com.ixe.ods.bup.model.EmpleadoIxe;
import com.ixe.ods.sica.FacultySystem;
import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.model.Reverso;
import com.ixe.ods.sica.model.ReversoDetalle;
import com.ixe.ods.sica.sdo.ReversosServiceData;

/**
 * Clase para la Consulta de Reversos capturados durante el d&iacute;a actual.
 *
 * @author Jean C. Favila
 * @version $Revision: 1.4 $ $Date: 2008/12/26 23:17:31 $
 */
public abstract class ConsultaReversos extends SicaPage {

    /**
     * Se invoca cuando el usuario selecciona la opci&oacute;n del men&uacute; para Consulta de
     * Reversos. Llama a <code>refrescar()</code> para llenar la lista de reversos capturados
     * durante la fecha actual del sistema.
     *
     * @param cycle El ciclo de request - response de la p&aacute;gina.
     * @see #refrescar(org.apache.tapestry.IRequestCycle).
     */
    public void activate(IRequestCycle cycle) {
        super.activate(cycle);
        setModoPromocion(FacultySystem.SICA_CONS_REV_PM.equals(cycle.getServiceParameters()[0]));
    }

    /**
     * Llena la lista de reversos capturados durante la fecha actual del sistema, utilizando el
     * servicio <code>findReversosCapturadosHoy</code> de <code>ReversosServiceData</code>.
     *
     * @param cycle El ciclo de request - response de la p&aacute;gina.
     * @see com.ixe.ods.sica.sdo.ReversosServiceData#findReversosCapturados(java.util.Date,
     * java.util.Date).
     * @noinspection UnusedDeclaration
     */
    public void refrescar(IRequestCycle cycle) {
        try {
            if (getFechaInicio() == null) {
                throw new SicaException("Por favor especifique la fecha de inicio para la " +
                        "consulta.");
            }
            if (getFechaFin() == null) {
                throw new SicaException("Por favor especifique la fecha de fin para la consulta.");
            }
            ReversosServiceData rsd = (ReversosServiceData) getApplicationContext().
                    getBean("reversosServiceData");
            List reversos = rsd.findReversosCapturados(getFechaInicio(), getFechaFin());
            if (isModoPromocion()) {
                List promotores = getPromotoresJerarquia();
                for (Iterator it = reversos.iterator(); it.hasNext();) {
                    Reverso reverso = (Reverso) it.next();
                    boolean promotorEncontrado = false;
                    for (Iterator it2 = promotores.iterator(); it2.hasNext();) {
                        EmpleadoIxe emp = (EmpleadoIxe) it2.next();
                        if (emp.getIdPersona().intValue() == reverso.getUsuario().getIdPersona().
                                intValue()) {
                            promotorEncontrado = true;
                            break;
                        }
                    }
                    if (!promotorEncontrado) {
                        it.remove();
                    }
                }
            }
            setReversos(reversos);
        }
        catch (SicaException e) {
            debug(e.getMessage(), e);
            getDelegate().record(e.getMessage(), null);
            setReversos(new ArrayList());
        }
    }

    /**
     * Regresa el n&uacute;mero de deal de balanceo del reverso, o null si &eacute;ste a&uacute;n no
     * est&aacute; definido.
     *
     * @param reverso El reverso a consultar.
     * @return Integer.
     */
    public Integer getIdDealBalanceo(Reverso reverso) {
        if (reverso.getDealBalanceo() != null) {
            return new Integer(reverso.getDealBalanceo().getIdDeal());
        }
        return null;
    }

    /**
     * Regresa el n&uacute;mero de deal de correcci&oacute;n del reverso, o null si &eacute;ste
     * a&uacute;n no est&aacute; definido.
     *
     * @param reverso El reverso a consultar.
     * @return Integer.
     */
    public Integer getIdDealCorreccion(Reverso reverso) {
        if (reverso.getDealCorreccion() != null) {
            return new Integer(reverso.getDealCorreccion().getIdDeal());
        }
        return null;
    }

    /**
     * Obtiene los datos para el reporte de Reversos Capturados.
     *
     * @return List.
     */
    public List getDataSourceReporteReversos() {
        try {
            if (getReversos().isEmpty()) {
                throw new SicaException("No hay reversos capturados hasta el momento.");
            }
            List reversos = new ArrayList();
            for (Iterator it = getReversos().iterator(); it.hasNext();) {
                Reverso reverso = (Reverso) it.next();
                if (reverso.getDetalles().isEmpty()) {
                    HashMap mapaDet = new HashMap();
                    mapaDet.put("idReverso", new Integer(reverso.getIdReverso()));
                    mapaDet.put("fecha", reverso.getFecha());
                    mapaDet.put("solicitante",
                            reverso.getUsuario().getPersona().getNombreCompleto());
                    mapaDet.put("idDealOriginal",
                            new Integer(reverso.getDealOriginal().getIdDeal()));
                    mapaDet.put("idDealBalanceo", reverso.getDealBalanceo() != null ?
                            new Integer(reverso.getDealBalanceo().getIdDeal()) : null);
                    mapaDet.put("idDealCorreccion", reverso.getDealCorreccion() != null ?
                            new Integer(reverso.getDealCorreccion().getIdDeal()) : null);
                    mapaDet.put("status", reverso.getStatusReverso());
                    mapaDet.put("motivo", reverso.getPorContratoSica() != null ?
                            reverso.getPorContratoSica().getNoCuenta() : "");
                    mapaDet.put("fechaValor", reverso.getPorFechaValor() != null ?
                            reverso.getPorFechaValor() : null);
                    mapaDet.put("porCancelacion", reverso.isPorCancelacion() ? "SI" : "");
                    mapaDet.put("folio", null);
                    mapaDet.put("divisa", "");
                    mapaDet.put("deMonto", null);
                    mapaDet.put("aMonto", null);
                    mapaDet.put("deTcc", null);
                    mapaDet.put("aTcc", null);
                    reversos.add(mapaDet);
                }
                else {
                    for (Iterator itDet = reverso.getDetalles().iterator(); itDet.hasNext();) {
                        HashMap mapaDet = new HashMap();
                        ReversoDetalle rd = (ReversoDetalle) itDet.next();
                        mapaDet.put("idReverso", new Integer(reverso.getIdReverso()));
                        mapaDet.put("fecha", reverso.getFecha());
                        mapaDet.put("solicitante",
                                reverso.getUsuario().getPersona().getNombreCompleto());
                        mapaDet.put("idDealOriginal",
                                new Integer(reverso.getDealOriginal().getIdDeal()));
                        mapaDet.put("idDealBalanceo", reverso.getDealBalanceo() != null ?
                                new Integer(reverso.getDealBalanceo().getIdDeal()) : null);
                        mapaDet.put("idDealCorreccion", reverso.getDealCorreccion() != null ?
                                new Integer(reverso.getDealCorreccion().getIdDeal()) : null);
                        mapaDet.put("status", reverso.getStatusReverso());
                        mapaDet.put("motivo", reverso.getPorContratoSica() != null ?
                                reverso.getPorContratoSica().getNoCuenta() : "");
                        mapaDet.put("fechaValor", reverso.getPorFechaValor() != null ?
                                reverso.getPorFechaValor() : null);
                        mapaDet.put("porCancelacion", reverso.isPorCancelacion() ? "SI" : "");
                        mapaDet.put("folio", new Integer(rd.getDealDetalle().getFolioDetalle()));
                        mapaDet.put("divisa", rd.getDealDetalle().getDivisa().getDescripcion());
                        mapaDet.put("deMonto", rd.getDealDetalle() != null ?
                                new Double(rd.getDealDetalle().getMonto()) : null);
                        mapaDet.put("aMonto", rd.getMontoNuevo());
                        mapaDet.put("deTcc", rd.getDealDetalle() != null ?
                                new Double(rd.getDealDetalle().getTipoCambio()) : null);
                        mapaDet.put("aTcc", rd.getTipoCambioNuevo() != null ?
                                new Double(rd.getTipoCambioNuevo().doubleValue()) : null);
                        reversos.add(mapaDet);
                    }
                }
            }
            return reversos;
        }
        catch (SicaException e) {
            debug(e.getMessage(), e);
            getDelegate().record(e.getMessage(), null);
            return new ArrayList();
        }
    }

    /**
     * Regresa el valor de fechaInicio.
     *
     * @return Date,
     */
    public abstract Date getFechaInicio();

    /**
     * Regresa el valor de fechaFin.
     *
     * @return Date.
     */
    public abstract Date getFechaFin();

    /**
     * Regresa el valor de modoPromocion.
     *
     * @return boolean.
     */
    public abstract boolean isModoPromocion();

    /**
     * Establece el valor de modoPromocion.
     *
     * @param modoPromocion El valor a asignar.
     */
    public abstract void setModoPromocion(boolean modoPromocion);

    /**
     * Establece el valor de reversos.
     *
     * @param reversos El valor a asignar.
     */
    public abstract void setReversos(List reversos);

    /**
     * Obtiene la  lista de Reversos Capturados.
     *
     * @return List
     */
    public abstract List getReversos();
}
