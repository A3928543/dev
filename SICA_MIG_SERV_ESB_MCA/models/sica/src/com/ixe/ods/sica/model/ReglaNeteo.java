/*
 * $Id: ReglaNeteo.java,v 1.1.2.1.6.1 2010/09/03 17:26:00 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Clase persistente para la tabla SC_REGLA_NETEO.
 *
 * @hibernate.class table="SC_REGLA_NETEO"
 * mutable="true"
 * proxy="com.ixe.ods.sica.model.ReglaNeteo"
 * dynamic-update="true"
 *
 * @author Abraham L&oacute;pez A.
 * @version $Revision: 1.1.2.1.6.1 $ $Date: 2010/09/03 17:26:00 $
 */
public class ReglaNeteo implements Serializable {

    /**
     * Constructor vac&iacute;o.
     */
    public ReglaNeteo() {
        super();
    }
    
    /**
     * Constructor que inicializa todas las variables de instancia.
     *
     * @param idDivisa Divisa a la cual esta asociada una regla.
     * @param claveFormaLiquidacionCompra La clave de la forma de la liquidaci&oacute;n a la cual 
     * esta asociada la compra.
     * @param claveFormaLiquidacionVenta La clave de la forma de la liquidaci&oacute;n a la cual 
     * esta asociada la venta.
     * @param baseRegla El identificador para la operaci&oacute;n en la regla de neteo.
     * @param difAntesDelHorario El valor a aplicar en la regla de neteo.
     * @param difDespuesDelHorario El valor a aplicar en la regla de neteo.
     * @param montoAntesDelHorario El valor del monto a aplicarse en caso de que la regla caiga 
     * antes del horario establecido.
     * @param montoDespuesDelHorario El valor del monto a aplicarse en caso de que la regla caiga 
     * despues del horario establecido.
     */
    public ReglaNeteo(String idDivisa, String claveFormaLiquidacionCompra, 
    		            String claveFormaLiquidacionVenta, String baseRegla,
    		            BigDecimal difAntesDelHorario, BigDecimal difDespuesDelHorario,
    		            Double montoAntesDelHorario, Double montoDespuesDelHorario) {
        this();
        this.idDivisa = idDivisa;
        this.claveFormaLiquidacionCompra = claveFormaLiquidacionCompra;
        this.claveFormaLiquidacionVenta = claveFormaLiquidacionVenta;
        this.baseRegla = baseRegla;
        this.difAntesDelHorario = difAntesDelHorario;
        this.difDespuesDelHorario = difDespuesDelHorario;
        this.montoAntesDelHorario = montoAntesDelHorario;
        this.montoDespuesDelHorario = montoDespuesDelHorario;
    }    
    
    /**
     * Regresa el valor de idReglaNeteo.
     *
     * @return String.
     * @hibernate.id generator-class="uuid.hex"
     * column="ID_REGLA_NETEO"
     * unsaved-value="null"
     */
    public String getIdReglaNeteo() {
		return idReglaNeteo;
	}

    /**
     * Fija el valor de idReglaNeteo.
     *
     * @param idReglaNeteo El valor a asignar.
     */
	public void setIdReglaNeteo(String idReglaNeteo) {
		this.idReglaNeteo = idReglaNeteo;
	}
    
	/**
     * Regresa el valor de idDivisa.
     *
     * @hibernate.property column="ID_DIVISA"
     * not-null="true"
     * unique="false"
     * @return String.
     */
	public String getIdDivisa() {
		return idDivisa;
	}

	/**
     * Fija el valor de idDivisa.
     *
     * @param idDivisa El valor a asignar.
     */
	public void setIdDivisa(String idDivisa) {
		this.idDivisa = idDivisa;
	}
    
	/**
     * Regresa el valor de claveFormaLiquidacionCompra.
     *
     * @hibernate.property column="CLAVE_FORMA_LIQUIDACION_COMPRA"
     * not-null="true"
     * unique="false"
     * @return String.
     */
	public String getClaveFormaLiquidacionCompra() {
		return claveFormaLiquidacionCompra;
	}

	/**
     * Fija el valor de claveFormaLiquidacionCompra.
     *
     * @param claveFormaLiquidacionCompra El valor a asignar.
     */
	public void setClaveFormaLiquidacionCompra(String claveFormaLiquidacionCompra) {
		this.claveFormaLiquidacionCompra = claveFormaLiquidacionCompra;
	}

	/**
     * Regresa el valor de claveFormaLiquidacionVenta.
     *
     * @hibernate.property column="CLAVE_FORMA_LIQUIDACION_VENTA"
     * not-null="true"
     * unique="false"
     * @return String.
     */
	public String getClaveFormaLiquidacionVenta() {
		return claveFormaLiquidacionVenta;
	}

	/**
     * Fija el valor de claveFormaLiquidacionVenta.
     *
     * @param claveFormaLiquidacionVenta El valor a asignar.
     */
	public void setClaveFormaLiquidacionVenta(String claveFormaLiquidacionVenta) {
		this.claveFormaLiquidacionVenta = claveFormaLiquidacionVenta;
	}
	
	/**
     * Regresa el valor de baseRegla.
     *
     * @hibernate.property column="BASE_REGLA"
     * not-null="true"
     * unique="false"
     * @return String.
     */
	public String getBaseRegla() {
		return baseRegla;
	}

	/**
	 * El identificador para la operaci&oacute;n en la regla de neteo.
	 * 
	 * @param baseRegla
	 */
	public void setBaseRegla(String baseRegla) {
		this.baseRegla = baseRegla;
	}

	/**
     * Regresa el valor del diferencial antes del horario.
     *
     * @hibernate.property column="DIF_ANTES_DEL_HORARIO"
     * not-null="false"
     * unique="false"
     * @return BigDecimal.
     */
	public BigDecimal getDifAntesDelHorario() {
		return difAntesDelHorario;
	}

	/**
     * Fija el valor del diferencial antes del horario.
     *
     * @param difAntesDelHorario El valor a asignar.
     */
	public void setDifAntesDelHorario(BigDecimal difAntesDelHorario) {
		this.difAntesDelHorario = difAntesDelHorario;
	}
	
	/**
     * Regresa el valor del diferencial despu&eacute;s del horario.
     *
     * @hibernate.property column="DIF_DESPUES_DEL_HORARIO"
     * not-null="false"
     * unique="false"
     * @return BigDecimal.
     */
	public BigDecimal getDifDespuesDelHorario() {
		return difDespuesDelHorario;
	}

	/**
     * Fija el valor del diferencial despu&eacute;s del horario.
     *
     * @param difDespuesDelHorario El valor a asignar.
     */
	public void setDifDespuesDelHorario(BigDecimal difDespuesDelHorario) {
		this.difDespuesDelHorario = difDespuesDelHorario;
	}

	/**
     * Regresa el valor del montoAntesDelHorario.
     *
     * @hibernate.property column="MONTO_ANTES_DEL_HORARIO"
     * not-null="false"
     * unique="false"
     * @return Double.
     */
	public Double getMontoAntesDelHorario() {
		return montoAntesDelHorario;
	}

	/**
	 * El valor del monto a aplicarse en caso de que la regla caiga antes del horario establecido.
	 * 
	 * @param montoAntesDelHorario
	 */
	public void setMontoAntesDelHorario(Double montoAntesDelHorario) {
		this.montoAntesDelHorario = montoAntesDelHorario;
	}

	/**
     * Regresa el valor del montoDespuesDelHorario.
     *
     * @hibernate.property column="MONTO_DESPUES_DEL_HORARIO"
     * not-null="false"
     * unique="false"
     * @return Double.
     */
	public Double getMontoDespuesDelHorario() {
		return montoDespuesDelHorario;
	}

	/**
	 * El valor del monto a aplicarse en caso de que la regla caiga despues del horario establecido.
	 * 
	 * @param montoDespuesDelHorario
	 */
	public void setMontoDespuesDelHorario(Double montoDespuesDelHorario) {
		this.montoDespuesDelHorario = montoDespuesDelHorario;
	}

	/**
     * Regresa true si ambos objetos son iguales.
     *
     * @param other El otro objeto a comparar.
     * @return boolean.
     * @see java.lang.Object#equals(java.lang.Object).
     */
    public boolean equals(Object other) {
        if (!(other instanceof ReglaNeteo)) {
            return false;
        }
        ReglaNeteo castOther = (ReglaNeteo) other;
        return new EqualsBuilder().append(this.getIdReglaNeteo(),
                castOther.getIdReglaNeteo()).isEquals();
    }

    /**
     * Regresa el hashCode.
     *
     * @return <code>int</code>
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return new HashCodeBuilder().append(getIdReglaNeteo()).toHashCode();
    }

    /**
     * Regresa una cadena con la descripci&oacute;n de los campos de esta instancia.
     *
     * @return String.
     */
    public String toString() {
        return new ToStringBuilder(this).append("idReglaNeteo", idReglaNeteo).
                append("idDivisa", idDivisa).append("claveFormaLiquidacionCompra", 
                        claveFormaLiquidacionCompra).append("claveFormaLiquidacionVenta", 
                		claveFormaLiquidacionVenta).append("baseRegla", baseRegla).
                append("difAntesDelHorario", difAntesDelHorario).
                append("difDespuesDelHorario", difDespuesDelHorario).
                append("montoAntesDelHorario", montoAntesDelHorario).
                append("montoDespuesDelHorario", montoDespuesDelHorario).toString();
    }

    /**
	 * El UID para serializaci&oacute;n.
	 */
	private static final long serialVersionUID = -3396023294493354396L;

	/**
     * La llave primaria del registro.
     */
    private String idReglaNeteo;
    
    /**
     * Divisa a la cual esta asociada una regla.
     */
    private String idDivisa;
    
    /**
     * La clave de la forma de la liquidaci&oacute;n a la cual esta asociada la compra.
     */
    private String claveFormaLiquidacionCompra;
    
    /**
     * La clave de la forma de la liquidaci&oacute;n a la cual esta asociada la venta.
     */
    private String claveFormaLiquidacionVenta;
    
    /**
     * El identificador para la operaci&oacute;n en la regla de neteo.
     */
    private String baseRegla;
    
    /**
     * El valor a aplicar en la regla de neteo.
     */
    private BigDecimal difAntesDelHorario;
    
    /**
     * El valor a aplicar en la regla de neteo.
     */
    private BigDecimal difDespuesDelHorario;
    
   /**
     * El valor del monto a aplicarse en caso de que la regla caiga antes del horario establecido.
     */
    private Double montoAntesDelHorario;
    
    /**
     * El valor del monto a aplicarse en caso de que la regla caiga despu&eacute;s del horario 
     * establecido.
     */
    private Double montoDespuesDelHorario;
}
