/*
 * $Id: Divisa.java,v 1.15.56.1 2013/12/13 23:22:07 mejiar Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2004 - 2008 LegoSoft S.C.
 */

package com.ixe.ods.sica.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Clase persistente para la tabla SC_DIVISA.
 *
 * @hibernate.class table="SC_DIVISA"
 * mutable="true"
 * proxy="com.ixe.ods.sica.model.Divisa"
 * dynamic-update="true"
 *
 * @hibernate.query name="findAllDivisasByOrdenAlfabetico" query="FROM Divisa AS d ORDER BY d.orden"
 *
 * @hibernate.query name="findDivisasNoFrecuentes" query="FROM Divisa AS d WHERE d.tipo = 'N' ORDER BY d.orden"
 *
 * @hibernate.query name="findDivisasMetales" query="FROM Divisa AS d WHERE d.tipo = 'M' ORDER BY d.orden"
 *
 * @hibernate.query name="findDivisasFrecuentes" query="FROM Divisa AS d WHERE d.tipo = 'F' ORDER BY d.orden"
 *
 * @author Jean C. Favila
 * @version $Revision: 1.15.56.1 $ $Date: 2013/12/13 23:22:07 $
 */
public class Divisa implements Serializable {

    /**
     * Constructor por default, no hace nada.
     */
    public Divisa() {
        super();
    }

    /**
     * Permite sabe si la Divisina es Frecuente.
     *
     * @return True o False.
     */
    public boolean isFrecuente() {
        return FRECUENTE.equals(tipo);
    }

    /**
     * Permite saber si la Divisa es No Frecuente.
     *
     * @return True o False.
     */
    public boolean isNoFrecuente() {
        return NO_FRECUENTE.equals(tipo);
    }

    /**
     * Permite saber si la Divisa es Metal Amonedado.
     *
     * @return True o False.
     */
    public boolean isMetalAmonedado() {
        return METAL_AMONEDADO.equals(tipo);
    }

    /**
     * Regresa el valor de idDivisa.
     *
     * @return String.
     * @hibernate.id column="ID_DIVISA"
     * unsaved-value="0"
     * generator-class="assigned"
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
     * Regresa el valor de descripcion.
     *
     * @return String.
     * @hibernate.property column="DESCRIPCION"
     * not-null="true"
     * unique="false"
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Fija el valor de descripcion.
     *
     * @param descripcion El valor a asignar.
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Regresa el valor de icono.
     *
     * @return String.
     * @hibernate.property column="ICONO_FNAME"
     * not-null="false"
     * unique="false"
     */
    public String getIcono() {
        return icono;
    }

    /**
     * Fija el valor de icono.
     *
     * @param icono El valor a asignar.
     */
    public void setIcono(String icono) {
        this.icono = icono;
    }

    /**
     * Regresa el valor de idMoneda.
     *
     * @return String.
     * @hibernate.property column="ID_MONEDA"
     * not-null="false"
     * unique="false"
     */
    public String getIdMoneda() {
        return idMoneda;
    }

    /**
     * Fija el valor de idMoneda.
     *
     * @param idMoneda El valor a asignar.
     */
    public void setIdMoneda(String idMoneda) {
        this.idMoneda = idMoneda;
    }

    /**
     * Regresa el valor de orden.
     *
     * @return int.
     * @hibernate.property column="ORDEN"
     * not-null="false"
     * unique="false"
     */
    public int getOrden() {
        return orden;
    }

    /**
     * Fija el valor del ordenador de la divisa
     *
     * @param orden El valor a asignar.
     */
    public void setOrden(int orden) {
        this.orden = orden;
    }

    /**
     * Regresa el valor de orden.
     *
     * @return int.
     * @hibernate.property column="TIPO"
     * not-null="true"
     * unique="false"
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * Fija el valor de tipo.
     *
     * @param tipo El valor asignar.
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    /**
     * Regresa el valor de grupo.
     *
     * @return String.
     * @hibernate.property column="GRUPO"
     * not-null="false"
     * unique="false"
     */
    public String getGrupo() {
        return grupo;
    }

    /**
     * Fija el valor de descripcion.
     *
     * @param grupo El valor a asignar.
     */
    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }
    
    /**
     * Regresa el valor de equivalenciaMetal.
     *
     * @return Double.
     * @hibernate.property column="EQUIVALENCIA_METAL"
     * not-null="false"
     * unique="false"
     */
    public Double getEquivalenciaMetal() {
        return equivalenciaMetal;
    }

    /**
     * Fija el valor de equivalenciaMetal.
     *
     * @param equivalenciaMetal El valor a asignar.
     */
    public void setEquivalenciaMetal(Double equivalenciaMetal) {
        this.equivalenciaMetal = equivalenciaMetal;
    }

    /**
     * Regresa true si ambos objetos son iguales.
     *
     * @param other El otro objeto a comparar.
     * @return boolean.
     * @see java.lang.Object#equals(java.lang.Object).
     * @see java.lang.Object#equals(java.lang.Object).
     */
    public boolean equals(Object other) {
        if (!(other instanceof Divisa)) {
            return false;
        }
        Divisa castOther = (Divisa) other;
        return new EqualsBuilder().append(this.getIdDivisa(),
                castOther.getIdDivisa()).isEquals();
    }

    /**
     * Regresa el hashCode.
     *
     * @return <code>int</code>
     * @see java.lang.Object#hashCode()
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return new HashCodeBuilder().append(getIdDivisa()).toHashCode();
    }

    /**
     * Obtiene si la divisa divide o no
     *
     * @return boolean
     * @hibernate.property column="DIVIDE"
     * type="com.legosoft.hibernate.type.SiNoType"
     * not-null="true"
     * update="true"
     * insert="true"
     * unique="false"
     */
    public boolean isDivide() {
        return divide;
    }

    /**
     * Asigna si la divisa divide.
     *
     * @param divide El valor a asignar.
     */
    public void setDivide(boolean divide) {
        this.divide = divide;
    }

    /**
     * Regresa el valor de spreadSucursales.
     *
     * @return double.
     * @hibernate.property column="SPREAD_SUCURSALES"
     * not-null="true"
     * update="true"
     * insert="true"
     * unique="false"
     */
    public double getSpreadSucursales() {
        return spreadSucursales;
    }

    /**
     * Establece el valor de spreadSucursales.
     *
     * @param spreadSucursales El valor a asignar.
     */
    public void setSpreadSucursales(double spreadSucursales) {
        this.spreadSucursales = spreadSucursales;
    }

    /**
     * El identificador del registro.
     */
    private String idDivisa;

    /**
     * El nombre de la divisa.
     */
    private String descripcion;

    /**
     * El path de la imagen para las divisas frecuentes.
     */
    private String icono;

    /**
     * El identificador de la moneda en el sistema Phoenix.
     */
    private String idMoneda;

    /**
     * F)recuente, N)o frecuente, M)etal amonedado.
     */
    private String tipo;

    /**
     * El identificador que permite saber si es Oro, Plata o ninguno de los dos
     */
    private String grupo;
    
    /**
     * El identificador que permite saber la equivalencia en Pesos Oro o Pesos Plata de un Metal 
     * Amonedado.
     */
    private Double equivalenciaMetal;

    /**
     * Es un ordenador de la forma en que se presentaran las divisas
     */
    private int orden;

    /**
     * Identificador que determina si divide o no la divisa.
     */
    private boolean divide;

    /**
     * El spread de sucursales para esta divisa.
     */
    private double spreadSucursales;

    /**
     * Constante que representa cuando la Divisa es frecuente
     */
    public static final String FRECUENTE = "F";

    /**
     * Constante que representa cuando la Divisa es no frecuente
     */
    public static final String NO_FRECUENTE = "N";

    /**
     * Constante que representa si es metal amonedado
     */
    public static final String METAL_AMONEDADO = "M";

    /**
     * Constante para el Campo Moneda que representa la Divisa Peso
     */
    public static final String PESO = "MXN";

    /**
     * Constante para el Campo Moneda que representa la Divisa Dolar Americano
     */
    public static final String DOLAR = "USD";

    /**
     * Constante para el Campo Moneda que representa la Divisa Dolar Canadiense
     */
    public static final String DOLAR_CANADIENSE = "CAD";

    /**
     * Constante para el Campo Moneda que representa la Divisa Euro
     */
    public static final String EURO = "EUR";

    /**
     * Constante para el Campo Moneda que representa la Divisa Libra Esterlina
     */
    public static final String LIBRA_ESTERLINA = "GBP";

    /**
     * Constante para el Campo Moneda que representa la Divisa Franco Suizo
     */
    public static final String FRANCO_SUIZO = "CHF";

    /**
     * Constante para el Campo Moneda que representa la Divisa Yen
     */
    public static final String YEN = "JPY";

    /**
     * Constante para el Campo Moneda que Representa a Otras Divisas en el Reporte de Operaciones a Banxico.
     */
    public static final String OTRAS_DIVISAS = "OTD";
    
    /**
     * Constante para el campo moneda que representa la divisa Dolar en el Sistema Altamira
     */
    public static final String DOLAR_ALTAMIRA ="USDS";
    
    /**
     * Constante para el campo moneda que representa al peso mexicano en el Sistema Altamira
     */
    public static final String MXN_ALTAMIRA   ="MXPS";
    
    /**
     * El UID para serializaci&oacute;n.
     */
    private static final long serialVersionUID = 7184225766796198244L;
}