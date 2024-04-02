/*
 * $Id: LimitesRestriccionOperacion.java,v 1.1.2.2 2010/11/07 23:40:40 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2006 - 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica.model;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

/**
 * Clase auxiliar para mantener guardados los l&iacute;mites que un contrato SICA puede
 * operar en los productos de las divisas en restricci&oacute;n.
 *
 * @author Abraham L&oacute;pez A.
 * @version $Revision: 1.1.2.2 $ $Date: 2010/11/07 23:40:40 $
 */
public class LimitesRestriccionOperacion implements Serializable {
	
	/**
     * Constructor por default.
     */
	public LimitesRestriccionOperacion() {
		super();
	}
	
	/**
	 * Constructor que inicializa los valores para la clase auxiliar.
	 *
	 * @param noCuenta El n&uacute;mero de cuenta del contrato SICA.
	 * @param tipoExcepcion El tipo de excepci&oacute;n de la regla.
	 * @param limiteDiarioIxeCpra El l&iacute;mite diario de ixe en la compra.
	 * @param limiteMensualIxeCpra El l&iacute;mite mensual de ixe en la compra.
	 * @param listaLimitesNormales Lista que contiene todos los l&iacute;mites normales que
	 * est&aacute;n en restricci&oacute;n.
	 * @param cliente Si se trata de un cliente o un usuario.
	 * @param nacionalidad La nacionalidad de la persona que opera el contrato sica.
	 */
	public LimitesRestriccionOperacion(String noCuenta, String tipoExcepcion,
			double limiteDiarioIxeCpra, double limiteMensualIxeCpra, List listaLimitesNormales,
			String cliente, String nacionalidad) {
		super();
		this.noCuenta = noCuenta;
		this.tipoExcepcion = tipoExcepcion;
		this.limiteDiarioIxeCpra = limiteDiarioIxeCpra;
		this.limiteMensualIxeCpra = limiteMensualIxeCpra;
		this.listaLimitesNormales = listaLimitesNormales;
		this.cliente = cliente;
		this.nacionalidad = nacionalidad;
	}
	
	/**
	 * Regresa el valor de noCuenta.
	 *
	 * @return String.
	 */
	public String getNoCuenta() {
		return noCuenta;
	}
	
	/**
	 * Establece el valor de noCuenta.
	 *
	 * @param noCuenta El valor a asignar.
	 */
	public void setNoCuenta(String noCuenta) {
		this.noCuenta = noCuenta;
	}
	
	/**
	 * Regresa el valor de limiteDiarioIxeCpra.
	 *
	 * @return double.
	 */
	public double getLimiteDiarioIxeCpra() {
		return limiteDiarioIxeCpra;
	}
	
	/**
	 * Establece el valor de limiteDiarioIxeCpra.
	 *
	 * @param limiteDiarioIxeCpra El valor a asignar.
	 */
	public void setLimiteDiarioIxeCpra(double limiteDiarioIxeCpra) {
		this.limiteDiarioIxeCpra = limiteDiarioIxeCpra;
	}
	
	/**
	 * Regresa el valor de limiteMensualIxeCpra.
	 *
	 * @return double.
	 */
	public double getLimiteMensualIxeCpra() {
		return limiteMensualIxeCpra;
	}
	
	/**
	 * Establece el valor de limiteMensualIxeCpra.
	 *
	 * @param limiteMensualIxeCpra El valor a asignar.
	 */
	public void setLimiteMensualIxeCpra(double limiteMensualIxeCpra) {
		this.limiteMensualIxeCpra = limiteMensualIxeCpra;
	}
	
	/**
	 * Regresa el valor de listaLimitesNormales.
	 *
	 * @return List.
	 */
	public List getListaLimitesNormales() {
		return listaLimitesNormales;
	}
	
	/**
	 * Establece el valor de listaLimitesNormales.
	 *
	 * @param listaLimitesNormales El valor a asignar.
	 */
	public void setListaLimitesNormales(List listaLimitesNormales) {
		this.listaLimitesNormales = listaLimitesNormales;
	}
	
	/**
	 * Regresa el valor de tipoExcepcion.
	 *
	 * @return String.
	 */
	public String getTipoExcepcion() {
		return tipoExcepcion;
	}
	
	/**
	 * Establece el valor de tipoExcepcion.
	 *
	 * @param tipoExcepcion El valor a asignar.
	 */
	public void setTipoExcepcion(String tipoExcepcion) {
		this.tipoExcepcion = tipoExcepcion;
	}
	
	/**
     * Regresa el valor de cliente.
     *
     * @return String.
     */
	public String getCliente() {
		return cliente;
	}
	
	/**
     * Establece el valor de cliente.
     *
     * @param cliente El valor a asignar.
     */
	public void setCliente(String cliente) {
		this.cliente = cliente;
	}
	
	/**
     * Regresa el valor de nacionalidad.
     *
     * @return String.
     */
	public String getNacionalidad() {
		return nacionalidad;
	}
	
	/**
     * Establece el valor de nacionalidad.
     *
     * @param nacionalidad El valor a asignar.
     */
	public void setNacionalidad(String nacionalidad) {
		this.nacionalidad = nacionalidad;
	}
	
	/**
	 * Regresa el l&iacute;mite m&aacute;ximo mensual entre el l&iacute;mite normal y el
	 * l&iacute;mite ixe.
	 *
	 * @param idDivisa La divisa a comparar.
	 * @param compra Si se trata de una compra o una venta.
	 * @return Double.
	 */
	public Double getLimiteMaximoMensual(String idDivisa, boolean compra) {
		Double limiteMaximoMensual = null;
		for (Iterator it = getListaLimitesNormales().iterator(); it.hasNext();) {
			LimiteEfectivo limEfecti = (LimiteEfectivo) it.next();
			if (limEfecti.getDivisa().equals(idDivisa) && limEfecti.getCompra().
					equals(new Boolean(compra))) {
				if (getTipoExcepcion().equals(PersonaListaBlanca.SIN_EXCEPCION)) {
					limiteMaximoMensual = limEfecti.getLimiteMensual();
				}
				else if (getTipoExcepcion().equals(PersonaListaBlanca.EXCEPCION_IXE)) {
					limiteMaximoMensual = new Double(limEfecti.getLimiteMensual().doubleValue() >
						getLimiteMensualIxeCpra() ? limEfecti.getLimiteMensual().doubleValue() :
							getLimiteMensualIxeCpra());
				}
			}
		}
		return limiteMaximoMensual;
	}
	
	/**
	 * Regresa el l&iacute;mite m&aacute;ximo diario entre el l&iacute;mite normal y el
	 * l&iacute;mite ixe.
	 *
	 * @param idDivisa La divisa a comparar
	 * @param compra Si se trata de una compra o una venta.
	 * @return Double.
	 */
	public Double getLimiteMaximoDiario(String idDivisa, boolean compra) {
		Double limiteMaximoDiario = null;
		for (Iterator it = getListaLimitesNormales().iterator(); it.hasNext();) {
			LimiteEfectivo limEfecti = (LimiteEfectivo) it.next();
			if (limEfecti.getDivisa().equals(idDivisa) && limEfecti.getCompra().
					equals(new Boolean(compra))) {
				if (getTipoExcepcion().equals(PersonaListaBlanca.SIN_EXCEPCION)) {
					limiteMaximoDiario = limEfecti.getLimiteDiario();
				}
				else if (getTipoExcepcion().equals(PersonaListaBlanca.EXCEPCION_IXE)) {
					limiteMaximoDiario = new Double(limEfecti.getLimiteDiario().doubleValue() >
						getLimiteDiarioIxeCpra() ? limEfecti.getLimiteDiario().doubleValue() :
							getLimiteDiarioIxeCpra());
				}
			}
		}
		return limiteMaximoDiario;
	}
	
	/**
	 * El UID para serializaci&oacute;n.
	 */
	private static final long serialVersionUID = 3522639667670812443L;
	
	/**
	 * El n&uacute;mero de cuenta del contrato SICA.
	 */
	private String noCuenta;
	
	/**
	 * El tipo de excepci&oacute;n de la regla.
	 */
	private String tipoExcepcion;
	
	/**
	 * El l&iacute;mite diario de ixe en la compra.
	 */
	private double limiteDiarioIxeCpra;
	
	/**
	 * El l&iacute;mite mensual de ixe en la compra.
	 */
	private double limiteMensualIxeCpra;
	
	/**
	 * Lista que contiene todos los l&iacute;mites normales que est&aacute;n en restricci&oacute;n.
	 */
	private List listaLimitesNormales;

	/**
     * Si se trata de un cliente o un usuario.
     */
    private String cliente;
    
    /**
	 * La nacionalidad de la persona que opera el contrato sica.
	 */
    private String nacionalidad;

}
