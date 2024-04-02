/*
 * $Id: DealWorkitemVO.java,v 1.15 2009/08/03 21:56:53 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2006 - 2009 LegoSoft S.C.
 */

package com.ixe.ods.sica.vo;

import java.io.Serializable;
import java.util.List;

/**
 * Value Object para la comunicaci&oacute;n del SICA con la aplicaci&oacute;n en Flex
 * de Autorizaciones de deals por parte de la mesa.
 *
 * @author Jean C. Favila
 * @version $Revision: 1.15 $ $Date: 2009/08/03 21:56:53 $
 */
public class DealWorkitemVO implements Serializable {

    /**
	 * Constructor por Default.
	 *
	 */
    public DealWorkitemVO() {
        super();
    }

    /**
     * Constructor para inicializar todas las variables de instancia.
     *
     * @param idActividad El id de la actividad.
     * @param actividad La actividad correspondiente.
     * @param idDeal El id del deal.
     * @param fechaValor La fecha valor del deal.
     * @param operacion El tipo de operaci&oacute;n del deal.
     * @param interbancario Define si el deal es interbancario.
     * @param nomCliente El nombre del cliente.
     * @param nomPromotor El nombre del promotor.
     * @param nomUsuario El nombre del Usuario que captur&oacute; el deal.
     * @param fecha La fecha actual.
     * @param solAutSobreprecio La solicitud de sobre precio para el tipo de cambio.
     * @param datoAdicional Alg&uacute;n valor extra para este registro.
     * @param detalles La lista de detalles para el deal.
     */
    public DealWorkitemVO(int idActividad, String actividad, int idDeal, String fechaValor,
                          String operacion, boolean interbancario, String nomCliente,
                          String nomPromotor, String nomUsuario, String fecha,
                          String solAutSobreprecio, String datoAdicional, List detalles) {
        this.idActividad = idActividad;
        this.actividad = actividad;
        this.idDeal = idDeal;
        this.fechaValor = fechaValor;
        this.operacion = operacion;
        this.interbancario = interbancario;
        this.nomCliente = nomCliente;
        this.nomPromotor = nomPromotor;
        this.nomUsuario = nomUsuario;
        this.fecha = fecha;
        this.solAutSobreprecio = solAutSobreprecio;
        this.datoAdicional = datoAdicional;
        this.detalles = detalles;
    }

    /**
     * Obteiene el valor de idActividad.
     *
     * @return int
     */
    public int getIdActividad() {
        return idActividad;
    }

    /**
     * Asigna el valor para idActividad.
     *
     * @param idActividad El valor para idActividad.
     */
    public void setIdActividad(int idActividad) {
        this.idActividad = idActividad;
    }

    /**
     * Obtiene el valor de actividad.
     *
     * @return String.
     */
    public String getActividad() {
        return actividad;
    }

    /**
     * Asigna el valor para actividad.
     *
     * @param actividad El valor para actividad.
     */
    public void setActividad(String actividad) {
        this.actividad = actividad;
    }

    /**
     * Obtiene el valor de idDeal.
     *
     * @return int.
     */
    public int getIdDeal() {
        return idDeal;
    }

    /**
     * Asigna el valor para idDeal.
     *
     * @param idDeal El valor para idDeal
     */
    public void setIdDeal(int idDeal) {
        this.idDeal = idDeal;
    }

    /**
     * Obtiene el valor de fechaValor.
     *
     * @return String
     */
    public String getFechaValor() {
        return fechaValor;
    }

    /**
     * Asigna el valor para fechaValor.
     *
     * @param fechaValor El valor para fechaValor.
     */
    public void setFechaValor(String fechaValor) {
        this.fechaValor = fechaValor;
    }

    /**
     * Define si el deal es interbancario.
     *
     * @return boolean.
     */
    public boolean isInterbancario() {
        return interbancario;
    }

    /**
     * Asigna el valor para interbancario.
     *
     * @param interbancario El valor para interbancario.
     */
    public void setInterbancario(boolean interbancario) {
        this.interbancario = interbancario;
    }

    /**
     * Obtiene el valor de nomCliente.
     *
     * @return String
     */
    public String getNomCliente() {
        return nomCliente;
    }

    /**
     * Asigna el valor para nomCliente
     *
     * @param nomCliente El valor para nomCliente.
     */
    public void setNomCliente(String nomCliente) {
        this.nomCliente = nomCliente;
    }

    /**
     * Obtiene el valor de nomPromotor.
     *
     * @return String
     */
    public String getNomPromotor() {
        return nomPromotor;
    }

    /**
     * Asigna el valor para nomPromotor.
     *
     * @param nomPromotor El valor para nomPromotor.
     */
    public void setNomPromotor(String nomPromotor) {
        this.nomPromotor = nomPromotor;
    }

    /**
     * Regresa el valor de nomUsuario.
     *
     * @return String.
     */
    public String getNomUsuario() {
        return nomUsuario;
    }

    /**
     * Establece el valor de nomUsuario.
     *
     * @param nomUsuario El valor a asignar.
     */
    public void setNomUsuario(String nomUsuario) {
        this.nomUsuario = nomUsuario;
    }

    /**
     * Obtiene el valor de operacion.
     *
     * @return String.
     */
    public String getOperacion() {
        return operacion;
    }

    /**
     * Asigna el valor para operacion.
     *
     * @param operacion El valor para operacion.
     */
    public void setOperacion(String operacion) {
        this.operacion = operacion;
    }

    /**
     * Obtiene el valor de fecha.
     *
     * @return String.
     */
    public String getFecha() {
        return fecha;
    }

    /**
     * Asigna el valor para fecha.
     *
     * @param fecha El valor para fecha.
     */
    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    /**
     * Obtiene el valor de solAutSobreprecio.
     *
     * @return String
     */
    public String getSolAutSobreprecio() {
        return solAutSobreprecio;
    }

    /**
     * Asigna el valor para solAutSobreprecio.
     *
     * @param solAutSobreprecio El valor para solAutSobreprecio.
     */
    public void setSolAutSobreprecio(String solAutSobreprecio) {
        this.solAutSobreprecio = solAutSobreprecio;
    }

    /**
     * Regresa el valor de datoAdicional.
     *
     * @return String.
     */
    public String getDatoAdicional() {
        return datoAdicional;
    }

    /**
     * Establece el valor para datoAdicional.
     *
     * @param datoAdicional El valor a asignar.
     */
    public void setDatoAdicional(String datoAdicional) {
        this.datoAdicional = datoAdicional;
    }

    /**
     * Obtiene el valor de detalles.
     *
     * @return List
     */
    public List getDetalles() {
        return detalles;
    }

    /**
     * Asigna el valor para detalles.
     *
     * @param detalles El valor para detalles.
     */
    public void setDetalles(List detalles) {
        this.detalles = detalles;
    }

    public void setFolioDetalle(String folioDetalle) {
		this.folioDetalle = folioDetalle;
	}

	public String getFolioDetalle() {
		return folioDetalle;
	}

	/**
     * El id de la actividad.
     */
    private int idActividad;

    /**
     * La actividad.
     */
    private String actividad;

    /**
     * El id del deal.
     */
    private int idDeal;

    /**
     * La fecha valor del deal.
     */
    private String fechaValor;

    /**
     * Define si el deal es interbancario.
     */
    private boolean interbancario;

    /**
     * El nombre del cliente.
     */
    private String nomCliente;

    /**
     * El nombre del promotor que es due&ntilde;o del contrato sica.
     */
    private String nomPromotor;

    /**
     * El nombre del usuario que captur&oacute; el deal.
     */
    private String nomUsuario;

    /**
     * La operacion del deal.
     */
    private String operacion;

    /**
     * La fecha de la actividad, formateada.
     */
    private String fecha;

    /**
     * La solicitud de sobreprecio.
     */
    private String solAutSobreprecio;
    
    /**
     * Folio del detalle
     */
    private String folioDetalle;

    /**
     * Alg&uacute;n dato adicional para el registro.
     */
    private String datoAdicional;

    /**
     * Los detalles del deal.
     */
    private List detalles;

    /**
     * El UID para serializaci&oacute;n.
     */
    private static final long serialVersionUID = 1506072220212100523L;
}
