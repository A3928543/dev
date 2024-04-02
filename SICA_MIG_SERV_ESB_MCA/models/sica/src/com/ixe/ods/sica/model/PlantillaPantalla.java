/*
 * $Id: PlantillaPantalla.java,v 1.12 2008/02/22 18:25:19 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright © 2005 LegoSoft S.C.
 */

package com.ixe.ods.sica.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.io.Serializable;

/**
 * Clase persistente para la tabla SC_PLANTILLA_PANTALLA.
 *
 * @hibernate.class table="SC_PLANTILLA_PANTALLA"
 * mutable="true"
 * proxy="com.ixe.ods.sica.model.PlantillaPantalla"
 * dynamic-update="true"
 *
 * @hibernate.query name="findPlantillaPantallaByMnemonico"
 * query="FROM PlantillaPantalla AS pp WHERE pp.mnemonico like ?"
 * 
 * @hibernate.query name="findMnemonicosByPlantillaPantalla"
 * query="FROM PlantillaPantalla AS pp WHERE pp.nombrePantalla like ?"
 *
 * @author Jean C. Favila
 * @version $Revision: 1.12 $ $Date: 2008/02/22 18:25:19 $
 */
public class PlantillaPantalla implements Serializable {

    /**
     * Constructor vac&iacute;o, no hace nada.
     */
    public PlantillaPantalla() {
        super();
    }

    /**
     * Regresa el valor de mnemonico.
     *
     * @hibernate.id generator-class="assigned"
     * column="MNEMONICO"
     * unsaved-value="null"
     * @return String.
     */
    public String getMnemonico() {
        return mnemonico;
    }

    /**
     * Fija el valor de mnemonico.
     *
     * @param mnemonico El valor a asignar.
     */
    public void setMnemonico(String mnemonico) {
        this.mnemonico = mnemonico;
    }

    /**
     * Regresa el valor de metodoReporte.
     *
     * @hibernate.property column="METODO_REPORTE"
     * not-null="false"
     * unique="false"
     * @return String.
     */
    public String getMetodoReporte() {
        return metodoReporte;
    }

    /**
     * Fija el valor de metodoReporte.
     *
     * @param metodoReporte El valor a asignar.
     */
    public void setMetodoReporte(String metodoReporte) {
        this.metodoReporte = metodoReporte;
    }

    /**
     * Regresa el valor de nombrePantalla.
     *
     * @hibernate.property column="NOMBRE_PANTALLA"
     * not-null="true"
     * unique="false"
     * @return String.
     */
    public String getNombrePantalla() {
        return nombrePantalla;
    }

    /**
     * Fija el valor de nombrePantalla.
     *
     * @param nombrePantalla El valor a asignar.
     */
    public void setNombrePantalla(String nombrePantalla) {
        this.nombrePantalla = nombrePantalla;
    }

    /**
     * Regresa true si ambos objetos son iguales.
     *
     * @param other El otro objeto a comparar.
     * @see java.lang.Object#equals(java.lang.Object).
     * @return boolean.
     */
    public boolean equals(Object other) {
        if (!(other instanceof PlantillaPantalla)) {
            return false;
        }
        PlantillaPantalla castOther = (PlantillaPantalla) other;
        return new EqualsBuilder().append(this.getMnemonico(), castOther.getMnemonico()).isEquals();
    }

    /**
     * Regresa el hashCode.
     *
     * @see java.lang.Object#hashCode()
     * @return <code>int</code>
     */
    public int hashCode() {
        return new HashCodeBuilder().append(getMnemonico()).toHashCode();
    }

    /**
     * La clave de la forma de liquidaci&oacute;n.
     */
    private String mnemonico;

    /**
     * Utilizado para generar los datos adicionales de la impresi&oacute;n del
     * deal.
     */
    private String metodoReporte;

    /**
     * El nombre de la p&aacute;gina de Tapestry a desplegar para el
     * mnem&oacute;nico.
     */
    private String nombrePantalla;
    
    /**
     * Constante Banco Tes Matriz.
     */
    public final static String TIPO_REP_BANCO_TES_MATRIZ = "BancoTesMatriz";
    
    /**
     * Constante Denominaci&oacute;n
     */
    public final static String TIPO_REP_DENOMINACION = "Denominacion";
    
    /**
     * Constante Plantilla.
     */
    public final static String TIPO_REP_PLANTILLA = "Plantilla";

    /**
     * Constante Captura Plantilla Nacional.
     */
    public static final String CAPTURA_NACIONAL = "CapturaNacional";

    /**
     * Constante Captura Plantilla Transferencia Nacional.
     */
    public static final String CAPTURA_TRANSFERENCIA_NAL = "CapturaTransferenciaNal";

    /**
     * Constante Captura Plantilla Cuenta Ixe.
     */
    public static final String CAPTURA_CUENTA_IXE = "CapturaCuentaIxe";

}
