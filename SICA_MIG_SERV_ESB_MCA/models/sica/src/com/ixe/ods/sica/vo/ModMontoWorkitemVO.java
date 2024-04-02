/*
 * $Id: ModMontoWorkitemVO.java,v 1.2.2.2 2010/06/30 23:14:41 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica.vo;

import java.io.Serializable;

/**
 * Value Object utilizado en las autorizaciones en Flex que representa un workitem de 
 * autorizaci&oacute;n para la modificaci&oacute;n de monto de un detalle de deal.
 *
 * @author Abraham L&oacute;pez A.
 * @version $Revision: 1.2.2.2 $ $Date: 2010/06/30 23:14:41 $
 */
public class ModMontoWorkitemVO implements Serializable {

    /**
     * Constructor vac&iacute;o. No hace nada.
     */
    public ModMontoWorkitemVO() {
        super();
    }

    /**
     * Constructor que inicializa las variable de instancia.
     *
     * @param idActividad El n&uacute;mero de actividad.
     * @param actividad El nombre de la actividad.
     * @param idDeal El n&uacute;mero de deal.
     * @param folioDetalle El folio del detalle del deal.
     * @param fechaValor La fecha valor del deal (cash, tom, spot, etc).
     * @param nomCliente El nombre del cliente asignado al contrato sica.
     * @param nomPromotor El nombre del promotor asignado al contrato sica.
     * @param nomUsuario El nombre del usuario que captur&oacute; el deal.
     * @param operacion Compra o Venta.
     * @param fecha La fecha en que se cre&oacute; la actividad.
     * @param idDivisa La clave de la divisa del detalle del deal.
     * @param monto El monto en divisa del detalle del deal.
     * @param tcc El tipo de cambio del cliente del detalle del deal.
     * @param resultado Indica cual fue la modificaci&oacute;n del monto.
     * @param contieneNeteo Indica si el deal contiene detalles capturados por reglas de neteo.
     */
    public ModMontoWorkitemVO(int idActividad, String actividad, int idDeal, int folioDetalle,
                                  String fechaValor, String nomCliente, String nomPromotor,
                                  String nomUsuario, String operacion, String fecha,
                                  String idDivisa, double monto, double tcm, double tcc,
                                  double resultado, boolean contieneNeteo) {
        this();
        this.idActividad = idActividad;
        this.actividad = actividad;
        this.idDeal = idDeal;
        this.folioDetalle = folioDetalle;
        this.fechaValor = fechaValor;
        this.nomCliente = nomCliente;
        this.nomPromotor = nomPromotor;
        this.nomUsuario = nomUsuario;
        this.operacion = operacion;
        this.fecha = fecha;
        this.idDivisa = idDivisa;
        this.monto = monto;
        this.tcm = tcm;
        this.tcc = tcc;
        this.resultado = resultado;
        this.contieneNeteo = contieneNeteo;
    }

    /**
     * Regresa el valor de idActividad.
     *
     * @return int.
     */
    public int getIdActividad() {
        return idActividad;
    }

    /**
     * Establece el valor de idActividad.
     *
     * @param idActividad El valor a asignar.
     */
    public void setIdActividad(int idActividad) {
        this.idActividad = idActividad;
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
     * Regresa el valor de idDeal.
     *
     * @return int.
     */
    public int getIdDeal() {
        return idDeal;
    }

    /**
     * Establece el valor de idDeal.
     * 
     * @param idDeal El valor a asignar.
     */
    public void setIdDeal(int idDeal) {
        this.idDeal = idDeal;
    }

    /**
     * Regresa el valor de folioDetalle.
     *
     * @return int.
     */
    public int getFolioDetalle() {
        return folioDetalle;
    }

    /**
     * Establece el valor de folioDetalle.
     * 
     * @param folioDetalle El valor a asignar.
     */
    public void setFolioDetalle(int folioDetalle) {
        this.folioDetalle = folioDetalle;
    }

    /**
     * Regresa el valor de fechaValor.
     *
     * @return String.
     */
    public String getFechaValor() {
        return fechaValor;
    }

    /**
     * Establece el valor de fechaValor.
     *
     * @param fechaValor El valor a asignar.
     */
    public void setFechaValor(String fechaValor) {
        this.fechaValor = fechaValor;
    }

    /**
     * Regresa el valor de nomCliente.
     *
     * @return String.
     */
    public String getNomCliente() {
        return nomCliente;
    }

    /**
     * Establece el valor de nomCliente.
     *
     * @param nomCliente El valor a asignar.
     */
    public void setNomCliente(String nomCliente) {
        this.nomCliente = nomCliente;
    }

    /**
     * Regresa el valor de nomPromotor.
     *
     * @return String.
     */
    public String getNomPromotor() {
        return nomPromotor;
    }

    /**
     * Establece el valor de nomPromotor.
     * 
     * @param nomPromotor El valor a asignar.
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
     * Regresa el valor de operacion.
     *
     * @return String.
     */
    public String getOperacion() {
        return operacion;
    }

    /**
     * Establece el valor de operacion.
     *
     * @param operacion El valor a asignar.
     */
    public void setOperacion(String operacion) {
        this.operacion = operacion;
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
     * Regresa el valor de idDivisa.
     *
     * @return String.
     */
    public String getIdDivisa() {
        return idDivisa;
    }

    /**
     * Establece el valor de idDivisa.
     * 
     * @param idDivisa El valor a asignar.
     */
    public void setIdDivisa(String idDivisa) {
        this.idDivisa = idDivisa;
    }

    /**
     * Regresa el valor de monto.
     *
     * @return double.
     */
    public double getMonto() {
        return monto;
    }

    /**
     * Establece el valor de monto.
     *
     * @param monto El valor a asignar.
     */
    public void setMonto(double monto) {
        this.monto = monto;
    }
    
    /**
     * Regresa el valor de tcm.
     * 
     * @return
     */
    public double getTcm() {
    	return tcm;
    }
    
    /**
     * Establece el valor de tcm
     * 
     * @param tcm El valor de tcm.
     */
    public void setTcm(double tcm) {
    	this.tcm = tcm;
    }

    /**
     * Regresa el valor de tcc.
     *
     * @return double.
     */
    public double getTcc() {
        return tcc;
    }

    /**
     * Establece el valor de tcc.
     *
     * @param tcc El valor a asignar.
     */
    public void setTcc(double tcc) {
        this.tcc = tcc;
    }
    /**
     * Regresa el valor de resultado.
     *
     * @return double.
     */
    public double getResultado() {
        return resultado;
    }
    
    /**
     * Regresa el valor de contieneNeteo.
     *
     * @return boolean.
     */
    public boolean isContieneNeteo() {
    	return contieneNeteo;
    }

    /**
     * Establece el valor de resultado.
     *
     * @param resultado El valor a asignar.
     */
    public void setResultado(double resultado) {
        this.resultado = resultado;
    }
    
    /**
     * Establece el valor de contieneNeteo.
     *
     * @param contieneNeteo	El valor a asignar.
     */
    public void setContieneNeteo(boolean contieneNeteo) {
    	this.contieneNeteo = contieneNeteo;
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
     * El folio del detalle del deal.
     */
    private int folioDetalle;

    /**
     * La fecha valor del deal.
     */
    private String fechaValor;

    /**
     * El nombre del cliente.
     */
    private String nomCliente;

    /**
     * El nombre del promotor.
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
     * La clave de la divisa.
     */
    private String idDivisa;

    /**
     * El monto de la operaci&oacute;n.
     */
    private double monto;
    
    /**
     * El tipo de cambio de la mesa
     */
    private double tcm;

    /**
     * El tipo de cambio dado al cliente.
     */
    private double tcc;
    
    /**
     * Indica de cuanto fue la modificaci&oacute;n de monto del detalle de un deal.
     */
    private double resultado;
    
    /**
     * Indica si el deal cuenta con detalles capturados por reglas de neteo.
     */
    private boolean contieneNeteo;

    /**
     * El UID para serializaci&oacute;n.
     */
    private static final long serialVersionUID = 584927045724758813L;
}

