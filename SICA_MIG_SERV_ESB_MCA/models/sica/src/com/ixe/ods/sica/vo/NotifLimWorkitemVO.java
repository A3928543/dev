/*
 * $Id: NotifLimWorkitemVO.java,v 1.1.2.1 2010/10/08 01:31:04 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica.vo;

import java.io.Serializable;

/**
 * Value Object utilizado en las autorizaciones en Flex que representa un workitem 
 * de notificaci&oacute;n para los detalles de deal que excedieron el l&iacute;mite normal,
 * pero se encuentran dentro del l&iacute;mite Ixe.
 *
 * @author Abraham L&oacute;pez A.
 * @version $Revision: 1.1.2.1 $ $Date: 2010/10/08 01:31:04 $
 */
public class NotifLimWorkitemVO implements Serializable {

    /**
     * Constructor vac&iacute;o. No hace nada.
     */
    public NotifLimWorkitemVO() {
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
     * @param producto El producto seleccionado originalmente.
     * @param tcc El tipo de cambio del cliente seleccionado originalmente.
     * @param tcm El tipo de cambio de la mesa seleccionado originalmente.
     * @param limiteDiario El l&iacute;mite diario normal.
     * @param acumDiario El monto diario acumulado en ese producto, de esa divisa.
     * @param limiteDiarioIxe El l&iacute;mite diario de Ixe.
     * @param limiteMensual El l&iacute;mite mensual normal.
     * @param acumMensual El monto mensual acumulado en ese producto, de esa divisa.
     * @param limiteMensualIxe El l&iacute;mite mensual de Ixe.
     */
    public NotifLimWorkitemVO(int idActividad, String actividad, int idDeal, int idDealPosicion, 
    							int folioDetalle, String fechaValor, String nomCliente, 
    							String nomPromotor, String nomUsuario, String operacion, 
    							String fecha, String idDivisa, String producto, double tcc,
    							double tcm, double limiteDiario, double acumDiario,
    							double limiteDiarioIxe, double limiteMensual, double acumMensual,
    							double limiteMensualIxe) {
        this();
        this.idActividad = idActividad;
        this.actividad = actividad;
        this.idDeal = idDeal;
        this.idDealPosicion = idDealPosicion;
        this.folioDetalle = folioDetalle;
        this.fechaValor = fechaValor;
        this.nomCliente = nomCliente;
        this.nomPromotor = nomPromotor;
        this.nomUsuario = nomUsuario;
        this.operacion = operacion;
        this.fecha = fecha;
        this.idDivisa = idDivisa;
        this.producto = producto;
        this.tcc = tcc;
        this.tcm = tcm;
        this.limiteDiario = limiteDiario;
        this.acumDiario = acumDiario;
        this.limiteDiarioIxe = limiteDiarioIxe;
        this.limiteMensual = limiteMensual;
        this.acumMensual = acumMensual;
        this.limiteMensualIxe = limiteMensualIxe;
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
     * Regresa el valor de idDealPosicion
     * 
     * @return idDealPosicion
     */
    public int getIdDealPosicion() {
		return idDealPosicion;
	}

    /**
     * Establece el valor de idDealPosicion
     * 
     * @param idDealPosicion
     */
	public void setIdDealPosicion(int idDealPosicion) {
		this.idDealPosicion = idDealPosicion;
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
     * Regresa el valor del producto seleccionado originalmente.
     * 
     * @return String.
     */
    public String getProducto() {
		return producto;
	}

    /**
     * Establece el valor del producto seleccionado originalmente.
     * 
     * @param producto El valor a asignar.
     */
	public void setProducto(String producto) {
		this.producto = producto;
	}
	
	/**
     * Regresa el valor de tcm.
     *
     * @return double.
     */
    public double getTcm() {
		return tcm;
	}

    /**
     * Establece el valor de tcm.
     *
     * @param tcm El valor a asignar.
     */
	public void setTcm(double tcm) {
		this.tcm = tcm;
	}
	
	/**
	 * Regresa el valor de limiteDiario.
	 *
	 * @return double.
	 */
	public double getLimiteDiario() {
		return limiteDiario;
	}
    
	/**
	 * Establece el valor de limiteDiario.
	 *
	 * @param limiteDiario El valor a asignar.
	 */
	public void setLimiteDiario(double limiteDiario) {
		this.limiteDiario = limiteDiario;
	}
	
	/**
	 * Regresa el valor de acumDiario.
	 *
	 * @return double.
	 */
	public double getAcumDiario() {
		return acumDiario;
	}
	
	/**
	 * Establece el valor de acumDiario.
	 *
	 * @param acumDiario El valor a asignar.
	 */
	public void setAcumDiario(double acumDiario) {
		this.acumDiario = acumDiario;
	}
	
	/**
	 * Regresa el valor de limiteDiarioIxe.
	 *
	 * @return double.
	 */
	public double getLimiteDiarioIxe() {
		return limiteDiarioIxe;
	}
	
	/**
	 * Establece el valor de limiteDiarioIxe.
	 *
	 * @param limiteDiarioIxe El valor a asignar.
	 */
	public void setLimiteDiarioIxe(double limiteDiarioIxe) {
		this.limiteDiarioIxe = limiteDiarioIxe;
	}
	
	/**
	 * Regresa el valor de limiteMensual.
	 *
	 * @return double.
	 */
	public double getLimiteMensual() {
		return limiteMensual;
	}
	
	/**
	 * Establece el valor de limiteMensual.
	 *
	 * @param limiteMensual El valor a asignar.
	 */
	public void setLimiteMensual(double limiteMensual) {
		this.limiteMensual = limiteMensual;
	}
	
	/**
	 * Regresa el valor de acumMensual.
	 *
	 * @return double.
	 */
	public double getAcumMensual() {
		return acumMensual;
	}
	
	/**
	 * Establece el valor de acumMensual.
	 *
	 * @param acumMensual El valor a asignar.
	 */
	public void setAcumMensual(double acumMensual) {
		this.acumMensual = acumMensual;
	}
	
	/**
	 * Regresa el valor de limiteMensualIxe.
	 *
	 * @return double.
	 */
	public double getLimiteMensualIxe() {
		return limiteMensualIxe;
	}
	
	/**
	 * Establece el valor de limiteMensualIxe.
	 *
	 * @param limiteMensualIxe
	 */
	public void setLimiteMensualIxe(double limiteMensualIxe) {
		this.limiteMensualIxe = limiteMensualIxe;
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
     * El id del detalle.
     */
    private int idDealPosicion;

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
     * El tipo de cambio dado al cliente originalmente.
     */
    private double tcc;
    
    /**
     * El tipo de cambio de la mesa original.
     */
    private double tcm;
    
    /**
     * El producto capturado originalmente.
     */
    private String producto;
    
    /**
     * El l&iacute;mite diario normal.
     */
    private double limiteDiario;
    
    /**
     * El monto diario acumulado en ese producto, de esa divisa.
     */
    private double acumDiario;
    
    /**
     * El l&iacute;mite diario Ixe.
     */
    private double limiteDiarioIxe;
    
    /**
     * El l&iacute;mite mensual normal.
     */
    private double limiteMensual;
    
    /**
     * El monto mensual acumulado en ese producto, de esa divisa.
     */
    private double acumMensual;
    
    /**
     * El l&iacute;mite mensual Ixe.
     */
    private double limiteMensualIxe;

    /**
     * El UID para serializaci&oacute;n.
     */
    private static final long serialVersionUID = 594927043629758813L;
}

