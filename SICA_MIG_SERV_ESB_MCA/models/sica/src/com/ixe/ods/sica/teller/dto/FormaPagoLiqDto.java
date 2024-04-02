/*
 * $Id: FormaPagoLiqDto.java,v 1.1.4.2 2010/10/08 01:31:04 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica.teller.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * @author Jean C. Favila (jfavila)
 * @version $Revision: 1.1.4.2 $ $Date: 2010/10/08 01:31:04 $
 */
public class FormaPagoLiqDto implements Serializable {

    /**
     * Constructor por default.
     */
    public FormaPagoLiqDto() {
        super();
    }

    /**
     * Constructor que inicializa todas las variables de instancia.
     * 
     * @param claveFormaLiquidacion La clave del producto.
     * @param comision El monto de la comisi&oacute;n.
     * @param descripcion La descripci&oacute;n de la forma de pago y liquidaci&oacute;n.
     * @param idBanco El n&uacute;mero de banco relacionado.
     * @param idDivisa La clave de la divisa.
     * @param mnemonico La clave de la forma de pago o liquidaci&oacute;n.
     * @param montoMaximo El monto m&aacute;ximo a operar.
     * @param montoMinimo El monto m&iacute;nimo a operar.
     * @param multiplo El m&uacute;ltiplo requerido para la operaci&oacute;n.
     * @param nombreBanco El nombre del Banco.
     * @param operacionMinima El monto m&iacute;nimo a operar.
     * @param recibimos Compra o Venta.
     * @param usaPlantilla Si usa o no plantilla.
     */
    public FormaPagoLiqDto(String claveFormaLiquidacion, Double comision, String descripcion,
                           Long idBanco, String idDivisa, String mnemonico, BigDecimal montoMaximo,
                           BigDecimal montoMinimo, Double multiplo, String nombreBanco,
                           BigDecimal operacionMinima, boolean recibimos, boolean usaPlantilla) {
        this();
        this.claveFormaLiquidacion = claveFormaLiquidacion;
        this.comision = comision;
        this.descripcion = descripcion;
        this.idBanco = idBanco;
        this.idDivisa = idDivisa;
        this.mnemonico = mnemonico;
        this.montoMaximo = montoMaximo;
        this.montoMinimo = montoMinimo;
        this.multiplo = multiplo;
        this.nombreBanco = nombreBanco;
        this.operacionMinima = operacionMinima;
        this.recibimos = recibimos;
        this.usaPlantilla = usaPlantilla;
    }

    /**
     * Regresa el valor de claveFormaLiquidacion.
     *
     * @return String.
     */
    public String getClaveFormaLiquidacion() {
        return claveFormaLiquidacion;
    }

    /**
     * Establece el valor de claveFormaLiquidacion.
     *
     * @param claveFormaLiquidacion El valor a asignar.
     */
    public void setClaveFormaLiquidacion(String claveFormaLiquidacion) {
        this.claveFormaLiquidacion = claveFormaLiquidacion;
    }

    /**
     * Regresa el valor de comision.
     *
     * @return Double.
     */
    public Double getComision() {
        return comision;
    }

    /**
     * Establece el valor de comision.
     *
     * @param comision El valor a asignar.
     */
    public void setComision(Double comision) {
        this.comision = comision;
    }

    /**
     * Regresa el valor de descripcion.
     *
     * @return String.
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Establece el valor de descripcion.
     *
     * @param descripcion El valor a asignar.
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Regresa el valor de idBanco.
     *
     * @return Long.
     */
    public Long getIdBanco() {
        return idBanco;
    }

    /**
     * Establece el valor de idBanco.
     *
     * @param idBanco El valor a asignar.
     */
    public void setIdBanco(Long idBanco) {
        this.idBanco = idBanco;
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
     * Regresa el valor de mnemonico.
     *
     * @return String.
     */
    public String getMnemonico() {
        return mnemonico;
    }

    /**
     * Establece el valor de mnemonico.
     *
     * @param mnemonico El valor a asignar.
     */
    public void setMnemonico(String mnemonico) {
        this.mnemonico = mnemonico;
    }

    /**
     * Regresa el valor de montoMaximo.
     *
     * @return BigDecimal.
     */
    public BigDecimal getMontoMaximo() {
        return montoMaximo;
    }

    /**
     * Establece el valor de montoMaximo.
     *
     * @param montoMaximo El valor a asignar.
     */
    public void setMontoMaximo(BigDecimal montoMaximo) {
        this.montoMaximo = montoMaximo;
    }

    /**
     * Regresa el valor de minimo.
     *
     * @return BigDecimal.
     */
    public BigDecimal getMontoMinimo() {
        return montoMinimo;
    }

    /**
     * Establece el valor de minimo.
     *
     * @param montoMinimo El valor a asignar.
     */
    public void setMontoMinimo(BigDecimal montoMinimo) {
        this.montoMinimo = montoMinimo;
    }

    /**
     * Regresa el valor de multiplo.
     *
     * @return Double.
     */
    public Double getMultiplo() {
        return multiplo;
    }

    /**
     * Establece el valor de multiplo.
     *
     * @param multiplo El valor a asignar.
     */
    public void setMultiplo(Double multiplo) {
        this.multiplo = multiplo;
    }

    /**
     * Regresa el valor de nombreBanco.
     *
     * @return String.
     */
    public String getNombreBanco() {
        return nombreBanco;
    }

    /**
     * Establece el valor de nombreBanco.
     *
     * @param nombreBanco El valor a asignar.
     */
    public void setNombreBanco(String nombreBanco) {
        this.nombreBanco = nombreBanco;
    }

    /**
     * Regresa el valor de operacionMinima.
     *
     * @return BigDecimal.
     */
    public BigDecimal getOperacionMinima() {
        return operacionMinima;
    }

    /**
     * Establece el valor de operacionMinima.
     *
     * @param operacionMinima El valor a asignar.
     */
    public void setOperacionMinima(BigDecimal operacionMinima) {
        this.operacionMinima = operacionMinima;
    }

    /**
     * Regresa el valor de recibimos.
     *
     * @return boolean.
     */
    public boolean isRecibimos() {
        return recibimos;
    }

    /**
     * Establece el valor de recibimos.
     *
     * @param recibimos El valor a asignar.
     */
    public void setRecibimos(boolean recibimos) {
        this.recibimos = recibimos;
    }

    /**
     * Regresa el valor de usaPlantilla.
     *
     * @return boolean.
     */
    public boolean isUsaPlantilla() {
        return usaPlantilla;
    }

    /**
     * Establece el valor de usaPlantilla.
     *
     * @param usaPlantilla El valor a asignar.
     */
    public void setUsaPlantilla(boolean usaPlantilla) {
        this.usaPlantilla = usaPlantilla;
    }

    /**
     * Regresa una cadena que describe los campo de la Forma de Cobro o Pago.
     *
     * @return String.
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        
        sb.append("claveFormaLiquidacion: ").append(claveFormaLiquidacion).append(", mnemonico: ").
                append(mnemonico).append(", descripcion: ").append(descripcion).
                append(", recibimos: ").append(recibimos).append(", usaPlantilla: ").
                append(usaPlantilla);
        return sb.toString();
    }
    
    /**
     * Regresa true si ambos objetos son iguales.
     *
     * @param other El otro objeto a comparar.
     * @see java.lang.Object#equals(java.lang.Object).
     * @return boolean.
     */
    public boolean equals(Object other) {
    	if (!(other instanceof FormaPagoLiqDto)) {
            return false;
        }
    	FormaPagoLiqDto castOther = (FormaPagoLiqDto) other;
    	return new EqualsBuilder().append(this.getClaveFormaLiquidacion(), 
    			castOther.getClaveFormaLiquidacion()).append(this.getMnemonico(), 
    			castOther.getMnemonico()).isEquals();
    }
    
    /**
     * Regresa el hashCode.
     *
     * @see java.lang.Object#hashCode()
     * @return <code>int</code>
     */
    public int hashCode() {
    	return new HashCodeBuilder().append(getClaveFormaLiquidacion()).append(getMnemonico())
    		.toHashCode();
    }

    /**
     * La clave del producto.
     */
    private String claveFormaLiquidacion;

    /**
     * El monto de la comisi&oacute;n.
     */
    private Double comision;

    /**
     * La descripci&oacute;n de la forma de pago y liquidaci&oacute;n.
     */
    private String descripcion;

    /**
     * El n&uacute;mero de banco relacionado.
     */
    private Long idBanco;

    /**
     * La clave de la divisa.
     */
    private String idDivisa;

    /**
     * La clave de la forma de pago o cobro espec&iacute;fica.
     */
    private String mnemonico;

    /**
     * El monto m&aacute;ximo de operaci&oacute;n.
     */
    private BigDecimal montoMaximo;

    /**
     * El m&iacute;nimo operable.
     */
    private BigDecimal montoMinimo;

    /**
     * El m&uacute;ltiplo.
     */
    private Double multiplo;

    /**
     * El nombre del banco.
     */
    private String nombreBanco;

    /**
     * El valor del m&iacute;nimo operable.
     */
    private BigDecimal operacionMinima;

    /**
     * True recibimos, false entregamos.
     */
    private boolean recibimos;

    /**
     * True si usa plantilla, false si no.
     */
    private boolean usaPlantilla;

    /**
     * El UID para serializaci&oacute;n.
     */
    private static final long serialVersionUID = -2101162167874859469L;
}
