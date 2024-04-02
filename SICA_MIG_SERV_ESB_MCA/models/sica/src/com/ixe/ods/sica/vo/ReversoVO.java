/*
 * $Id: ReversoVO.java,v 1.3 2008/02/22 18:25:24 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2007 LegoSoft S.C.
 */

package com.ixe.ods.sica.vo;

import com.ixe.ods.sica.model.Reverso;
import com.ixe.ods.sica.model.ReversoDetalle;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Value Object para el model de Hibernate com.ixe.ods.sica.model.Reverso.
 *
 * @author Jean C. Favila
 * @version $Revision: 1.3 $ $Date: 2008/02/22 18:25:24 $
 */
public class ReversoVO implements Serializable {

    /**
     * Constructor vac&iacute;o.
     */
    public ReversoVO() {
        super();
    }

    /**
     * Constructor para inicializar las variables de instancia a partir del modelo de Hibernate.
     *
     * @param rev El modelo de Hibernate para la tabla SC_REVERSO.
     */
    public ReversoVO(Reverso rev) {
        this();
        idReverso = rev.getIdReverso();
        idDealOriginal = rev.getDealOriginal().getIdDeal();
        fecha = DATE_FORMAT.format(rev.getFecha());
        fechaOperacion = DATE_FORMAT.format(rev.getDealOriginal().getFechaCaptura());
        fechaValorDeal = rev.getDealOriginal().getTipoValor();
        nombreUsuarioSolicitante = rev.getUsuario().getPersona().getNombreCompleto();
        observaciones = rev.getObservaciones();
        porCancelacion = rev.isPorCancelacion();
        porContratoSica = rev.getPorContratoSica() != null
                ? rev.getPorContratoSica().getNoCuenta() : null;
        porFechaValor = rev.getPorFechaValor();
        statusReverso = rev.getStatusReverso();
        for (Iterator it = rev.getDetalles().iterator(); it.hasNext();) {
            detalles.add(new ReversoDetalleVO((ReversoDetalle) it.next()));
        }
    }

    /**
     * Regresa el valor de idReverso.
     *
     * @return int.
     */
    public int getIdReverso() {
        return idReverso;
    }

    /**
     * Establece el valor de idReverso.
     *
     * @param idReverso El valor a asignar.
     */
    public void setIdReverso(int idReverso) {
        this.idReverso = idReverso;
    }

    /**
     * Regresa el valor de actividad.
     *
     * @return String.
     */
    public String getActividad() {
        return actividad;
    }

    /**
     * Establece el valor de actividad.
     *
     * @param actividad El valor a asignar.
     */
    public void setActividad(String actividad) {
        this.actividad = actividad;
    }

    /**
     * Regresa el valor de idDealOriginal.
     *
     * @return int.
     */
    public int getIdDealOriginal() {
        return idDealOriginal;
    }

    /**
     * Establece el valor de idOriginal.
     *
     * @param idDealOriginal El valor a asignar.
     */
    public void setIdDealOriginal(int idDealOriginal) {
        this.idDealOriginal = idDealOriginal;
    }

    /**
     * Regresa el valor de fecha.
     *
     * @return String.
     */
    public String getFecha() {
        return fecha;
    }

    /**
     * Establece el valor de fecha.
     *
     * @param fecha El valor a asignar.
     */
    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    /**
     * Regresa el valor de fechaOperacion.
     *
     * @return String.
     */
    public String getFechaOperacion() {
        return fechaOperacion;
    }

    /**
     * Establece el valor de fechaOperacion.
     *
     * @param fechaOperacion El valor a asignar.
     */
    public void setFechaOperacion(String fechaOperacion) {
        this.fechaOperacion = fechaOperacion;
    }

    /**
     * Regresa el valor de fechaValorDeal.
     *
     * @return String.
     */
    public String getFechaValorDeal() {
        return fechaValorDeal;
    }

    /**
     * Establece el valor de fechaValorDeal.
     *
     * @param fechaValorDeal El valor a asignar.
     */
    public void setFechaValorDeal(String fechaValorDeal) {
        this.fechaValorDeal = fechaValorDeal;
    }

    /**
     * Regresa el valor de nombreUsuarioSolicitante.
     *
     * @return String.
     */
    public String getNombreUsuarioSolicitante() {
        return nombreUsuarioSolicitante;
    }

    /**
     * Establece el valor de nombreUsuarioSolicitante.
     *
     * @param nombreUsuarioSolicitante El valor a asignar.
     */
    public void setNombreUsuarioSolicitante(String nombreUsuarioSolicitante) {
        this.nombreUsuarioSolicitante = nombreUsuarioSolicitante;
    }

    /**
     * Regresa el valor de observaciones.
     *
     * @return String.
     */
    public String getObservaciones() {
        return observaciones;
    }

    /**
     * Establece el valor de observaciones.
     *
     * @param observaciones El valor a asignar.
     */
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    /**
     * Regresa el valor de porCancelacion.
     *
     * @return boolean.
     */
    public boolean isPorCancelacion() {
        return porCancelacion;
    }

    /**
     * Establece el valor de porCancelacion.
     *
     * @param porCancelacion El valor a asignar.
     */
    public void setPorCancelacion(boolean porCancelacion) {
        this.porCancelacion = porCancelacion;
    }

    /**
     * Regresa el valor de porContratoSica.
     *
     * @return String.
     */
    public String getPorContratoSica() {
        return porContratoSica;
    }

    /**
     * Establece el valor de porContratoSica.
     *
     * @param porContratoSica El valor a asignar.
     */
    public void setPorContratoSica(String porContratoSica) {
        this.porContratoSica = porContratoSica;
    }

    /**
     * Regresa el valor de porFechaValor.
     *
     * @return String.
     */
    public String getPorFechaValor() {
        return porFechaValor;
    }

    /**
     * Establece el valor de porFechaValor.
     *
     * @param porFechaValor El valor a asignar.
     */
    public void setPorFechaValor(String porFechaValor) {
        this.porFechaValor = porFechaValor;
    }

    /**
     * Regresa el valor de statusReverso.
     *
     * @return String.
     */
    public String getStatusReverso() {
        return statusReverso;
    }

    /**
     * Establece el valor de statusReverso.
     *
     * @param statusReverso El valor a asignar.
     */
    public void setStatusReverso(String statusReverso) {
        this.statusReverso = statusReverso;
    }

    /**
     * Regresa el valor de detalles.
     *
     * @return List.
     */
    public List getDetalles() {
        return detalles;
    }

    /**
     * Establece el valor de detalles.
     *
     * @param detalles El valor a asignar.
     */
    public void setDetalles(List detalles) {
        this.detalles = detalles;
    }

    /**
     * El n&uacute;mero de reverso.
     */
    private int idReverso;

    /**
     * El nombre de la actividad
     */
    private String actividad = "Reversos";

    /**
     * El n&uacute;mero del deal que se va a reversar.
     */
    private int idDealOriginal;

    /**
     * La fecha en que se solicit&oacute; el reverso por el promotor.
     */
    private String fecha;

    /**
     * La fecha en que se captur&oacute; el deal.
     */
    private String fechaOperacion;

    /**
     * La fecha valor del deal original.
     */
    private String fechaValorDeal;

    /**
     * El nombre del usuario que solicit&oacute; el reverso.
     */
    private String nombreUsuarioSolicitante;

    /**
     * Las observaciones del reverso.
     */
    private String observaciones;

    /**
     * Si se trata o no de un reverso por cancelaci&oacute;n.
     */
    private boolean porCancelacion;

    /**
     * El n&uacute;mero de cuenta que debi&oacute; capturarse en el deal original.
     */
    private String porContratoSica;

    /**
     * La fecha valor a la que debi&oacute; capturarse el deal original.
     */
    private String porFechaValor;

    /**
     * El status del Reverso.
     */
    private String statusReverso;

    /**
     * Relaci&oacute;n uno-a-muchos con ReversoDetalle.
     */
    private List detalles = new ArrayList();

    /**
     * Formateador para fechas.
     */
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    /**
     * El UID para serializaci&oacute;n.
     */
    private static final long serialVersionUID = -2283495284270302439L;
}
