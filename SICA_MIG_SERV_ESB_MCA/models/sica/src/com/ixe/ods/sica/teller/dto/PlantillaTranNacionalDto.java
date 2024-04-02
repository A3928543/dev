/*
 * $Id: PlantillaTranNacionalDto.java,v 1.1.4.2 2010/10/08 01:31:04 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica.teller.dto;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 *
 * @author Jean C. Favila (jfavila)
 * @version $Revision: 1.1.4.2 $ $Date: 2010/10/08 01:31:04 $
 */
public class PlantillaTranNacionalDto implements Serializable {

    /**
     * Constructor por default.
     */
    public PlantillaTranNacionalDto() {
        super();
    }

    /**
     * Constructor que inicializa todas las variables de instancia.
     *
     * @param idPlantilla El n&uacute;mero de plantilla.
     * @param clavePlantilla La clave de plantilla (opcional).
     * @param noContratoSica El n&uacute;mero de contrato sica.
     * @param mnemonico La clave de la forma de pago o liquidaci&oacute;n.
     * @param ultimaModificacion La fecha de &uacute;ltima modificaci&oacute;n.
     * @param statusPlantilla El status de la plantilla.
     * @param idBeneficiario El n&uacute;mero de persona del beneficiario.
     * @param nombreBeneficiario El nombre completo del beneficiario.
     * @param clabe La clabe o cuenta.
     * @param idBanco El n&uacute;mero de banco seg&uacute;n la BUP.
     * @param nombreBanco El nombre del banco nacional.
     * @param idDivisa La clave de la divisa (MXN, USD, EUR).
     */
    public PlantillaTranNacionalDto(int idPlantilla, String clavePlantilla, String noContratoSica,
                                    String mnemonico, Date ultimaModificacion,
                                    String statusPlantilla, int idBeneficiario,
                                    String nombreBeneficiario, String clabe, Long idBanco,
                                    String nombreBanco, String idDivisa) {
        this();
        this.idPlantilla = idPlantilla;
        this.clavePlantilla = clavePlantilla;
        this.noContratoSica = noContratoSica;
        this.mnemonico = mnemonico;
        this.ultimaModificacion = ultimaModificacion;
        this.statusPlantilla = statusPlantilla;
        this.idBeneficiario = idBeneficiario;
        this.nombreBeneficiario = nombreBeneficiario;
        this.clabe = clabe;
        this.idBanco = idBanco;
        this.nombreBanco = nombreBanco;
        this.idDivisa = idDivisa;
    }

    /**
     * Regresa el valor de idPlantilla.
     *
     * @return int.
     */
    public int getIdPlantilla() {
        return idPlantilla;
    }

    /**
     * Establece el valor de idPlantilla.
     *
     * @param idPlantilla El valor a asignar.
     */
    public void setIdPlantilla(int idPlantilla) {
        this.idPlantilla = idPlantilla;
    }

    /**
     * Regresa el valor de clavePlantilla.
     *
     * @return String.
     */
    public String getClavePlantilla() {
        return clavePlantilla;
    }

    /**
     * Establece el valor de clavePlantilla.
     *
     * @param clavePlantilla El valor a asignar.
     */
    public void setClavePlantilla(String clavePlantilla) {
        this.clavePlantilla = clavePlantilla;
    }

    /**
     * Regresa el valor de noContratoSica.
     *
     * @return String.
     */
    public String getNoContratoSica() {
        return noContratoSica;
    }

    /**
     * Establece el valor de noContratoSica.
     *
     * @param noContratoSica El valor a asignar.
     */
    public void setNoContratoSica(String noContratoSica) {
        this.noContratoSica = noContratoSica;
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
     * Regresa el valor de ultimaModificacion.
     *
     * @return Date.
     */
    public Date getUltimaModificacion() {
        return ultimaModificacion;
    }

    /**
     * Establece el valor de ultimaModificacion.
     *
     * @param ultimaModificacion El valor a asignar.
     */
    public void setUltimaModificacion(Date ultimaModificacion) {
        this.ultimaModificacion = ultimaModificacion;
    }

    /**
     * Regresa el valor de statusPlantilla.
     *
     * @return String.
     */
    public String getStatusPlantilla() {
        return statusPlantilla;
    }

    /**
     * Establece el valor de statusPlantilla.
     *
     * @param statusPlantilla El valor a asignar.
     */
    public void setStatusPlantilla(String statusPlantilla) {
        this.statusPlantilla = statusPlantilla;
    }

    /**
     * Regresa el valor de idBeneficiario.
     *
     * @return int.
     */
    public int getIdBeneficiario() {
        return idBeneficiario;
    }

    /**
     * Establece el valor de idBeneficiario.
     *
     * @param idBeneficiario El valor a asignar.
     */
    public void setIdBeneficiario(int idBeneficiario) {
        this.idBeneficiario = idBeneficiario;
    }

    /**
     * Regresa el valor de nombreBeneficiario.
     *
     * @return String.
     */
    public String getNombreBeneficiario() {
        return nombreBeneficiario;
    }

    /**
     * Establece el valor de nombreBeneficiario.
     *
     * @param nombreBeneficiario El valor a asignar.
     */
    public void setNombreBeneficiario(String nombreBeneficiario) {
        this.nombreBeneficiario = nombreBeneficiario;
    }

    /**
     * Regresa el valor de clabe.
     *
     * @return String.
     */
    public String getClabe() {
        return clabe;
    }

    /**
     * Establece el valor de clabe.
     *
     * @param clabe El valor a asignar.
     */
    public void setClabe(String clabe) {
        this.clabe = clabe;
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
     * Regresa una cadena que describe los campos de la plantilla.
     *
     * @return String.
     */
    public String toString() {
        StringBuffer sb = new StringBuffer("idPlantilla: ").append(idPlantilla).
                append(", mnemonico: ").append(mnemonico).append(", noContratoSica: ").
                append(noContratoSica).append(", clabe: ").append(clabe).append(", nombreBanco: ").
                append(nombreBanco);
        return sb.toString();
    }
    
    /**
     * Regresa el hashCode.
     *
     * @see java.lang.Object#hashCode()
     * @return <code>int</code>
     */
    public int hashCode() {
    	return new HashCodeBuilder().append(idPlantilla).append(mnemonico).append(mnemonico).
    		append(noContratoSica).append(clabe).append(nombreBanco).toHashCode();
    }
    
    /**
     * Regresa true si ambos objetos son iguales.
     *
     * @param other El otro objeto a comparar.
     * @see java.lang.Object#equals(java.lang.Object).
     * @return boolean.
     */
    public boolean equals(Object other) {
    	if (!(other instanceof PlantillaTranNacionalDto)) {
            return false;
        }
    	PlantillaTranNacionalDto castOther = (PlantillaTranNacionalDto) other;
    	return new EqualsBuilder().append(this.getIdPlantilla(), castOther.getIdPlantilla())
    		.append(getMnemonico(), castOther.getMnemonico()).append(getNoContratoSica(), 
    				castOther.getNoContratoSica()).append(getClabe(), castOther.getClabe())
    				.isEquals();
    }

    /**
     * El n&uacute;mero de plantilla.
     */
    private int idPlantilla;

    /**
     * La clave de la plantilla.
     */
    private String clavePlantilla;

    /**
     * El n&uacute;mero de Contrato Sica.
     */
    private String noContratoSica;

    /**
     * La clave de la forma de liquidaci&oacute;n.
     */
    private String mnemonico;

    /**
     * La fecha de &uacute;ltima modificaci&oacute;n de la plantilla.
     */
    private Date ultimaModificacion;

    /**
     * El status de la plantilla.
     */
    private String statusPlantilla;

    /**
     * El n&uacute;mero de persona del beneficiario.
     */
    private int idBeneficiario;

    /**
     * El nombre del beneficiario.
     */
    private String nombreBeneficiario;

    /**
     * La CLABE o Cuenta.
     */
    private String clabe;

    /**
     * El n&uacute;mero de banco seg&uacute;n BUP_BANCO.
     */
    private Long idBanco;

    /**
     * El nombre del banco nacional.
     */
    private String nombreBanco;

    /**
     * La divisa de la cuenta.
     */
    private String idDivisa;

    /**
     * El UID para Serializaci&oacute;n.
     */    
    private static final long serialVersionUID = -3084097212576841011L;
}
