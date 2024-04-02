/*
 * $Id: PlantillaInternacionalDto.java,v 1.1.4.2 2010/10/08 01:31:04 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica.teller.dto;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * @author Jean C. Favila (jfavila)
 * @version $Revision: 1.1.4.2 $ $Date: 2010/10/08 01:31:04 $
 */
public class PlantillaInternacionalDto implements Serializable {

    /**
     * Constructor por default.
     */
    public PlantillaInternacionalDto() {
        super();
    }

    /**
     * Constructor que inicializa todas las variables de instacia.
     *
     * @param idPlantilla El n&uacute;mero de plantilla.
     * @param clavePlantilla La clave de plantilla (opcional).
     * @param noContratoSica El n&uacute;mero de contrato sica.
     * @param mnemonico La clave de la forma de pago o liquidaci&oacute;n.
     * @param ultimaModificacion La fecha de &uacute;ltima modificaci&oacute;n.
     * @param statusPlantilla El status de la plantilla.
     * @param idBeneficiario El n&uacute;mero de persona del beneficiario.
     * @param nombreBeneficiario El nombre completo del beneficiario.
     * @param idDivisa La clave de la divisa.
     * @param tipoCuentaBanco ABA|SWFT|CHIP.
     * @param tipoCuentaBancoInterm ABA|SWFT|CHIP.
     * @param claveBanco La clave de ruteo del banco beneficiario.
     * @param claveBancoInterm La clave de ruteo del banco intermediario.
     * @param idPais El n&uacute;mero de pa&iacute;s del banco beneficiario.
     * @param idPaisInterm El n&uacute;mero de pa&iacute;s del banco intermediario.
     * @param noCuentaBeneficiario El n&uacute;mero de cuenta.
     * @param nombreBanco El nombre del banco beneficiario.
     * @param cuentaBancoInterm La cuenta del banco intermediario en el banco intermediario.
     * @param nombreBancoInterm El nombre debl banco intermediario.
     * @param instrBcoBeneficiario Instrucciones al banco beneficiario.
     * @param instrBcoIntermediario Instrucciones al banco intermediario.
     */
    public PlantillaInternacionalDto(int idPlantilla, String clavePlantilla, String noContratoSica,
                                     String mnemonico, Date ultimaModificacion,
                                     String statusPlantilla, int idBeneficiario,
                                     String nombreBeneficiario, String idDivisa,
                                     String tipoCuentaBanco, String tipoCuentaBancoInterm,
                                     String claveBanco, String claveBancoInterm, String idPais,
                                     String idPaisInterm, String noCuentaBeneficiario,
                                     String nombreBanco, String cuentaBancoInterm,
                                     String nombreBancoInterm, String instrBcoBeneficiario,
                                     String instrBcoIntermediario) {
        this();
        this.idPlantilla = idPlantilla;
        this.clavePlantilla = clavePlantilla;
        this.noContratoSica = noContratoSica;
        this.mnemonico = mnemonico;
        this.ultimaModificacion = ultimaModificacion;
        this.statusPlantilla = statusPlantilla;
        this.idBeneficiario = idBeneficiario;
        this.nombreBeneficiario = nombreBeneficiario;
        this.idDivisa = idDivisa;
        this.tipoCuentaBanco = tipoCuentaBanco;
        this.tipoCuentaBancoInterm = tipoCuentaBancoInterm;
        this.claveBanco = claveBanco;
        this.claveBancoInterm = claveBancoInterm;
        this.idPais = idPais;
        this.idPaisInterm = idPaisInterm;
        this.noCuentaBeneficiario = noCuentaBeneficiario;
        this.nombreBanco = nombreBanco;
        this.cuentaBancoInterm = cuentaBancoInterm;
        this.nombreBancoInterm = nombreBancoInterm;
        this.instrBcoBeneficiario = instrBcoBeneficiario;
        this.instrBcoIntermediario = instrBcoIntermediario;
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
     * Regresa el valor de tipoCuentaBanco.
     *
     * @return String.
     */
    public String getTipoCuentaBanco() {
        return tipoCuentaBanco;
    }

    /**
     * Establece el valor de tipoCuentaBanco.
     *
     * @param tipoCuentaBanco El valor a asignar.
     */
    public void setTipoCuentaBanco(String tipoCuentaBanco) {
        this.tipoCuentaBanco = tipoCuentaBanco;
    }

    /**
     * Regresa el valor de tipoCuentaBancoInterm.
     *
     * @return String.
     */
    public String getTipoCuentaBancoInterm() {
        return tipoCuentaBancoInterm;
    }

    /**
     * Establece el valor de tipoCuentaBancoInterm.
     *
     * @param tipoCuentaBancoInterm El valor a asignar.
     */
    public void setTipoCuentaBancoInterm(String tipoCuentaBancoInterm) {
        this.tipoCuentaBancoInterm = tipoCuentaBancoInterm;
    }

    /**
     * Regresa el valor de claveBanco.
     *
     * @return String.
     */
    public String getClaveBanco() {
        return claveBanco;
    }

    /**
     * Establece el valor de claveBanco.
     *
     * @param claveBanco El valor a asignar.
     */
    public void setClaveBanco(String claveBanco) {
        this.claveBanco = claveBanco;
    }

    /**
     * Regresa el valor de claveBancoInterm.
     *
     * @return String.
     */
    public String getClaveBancoInterm() {
        return claveBancoInterm;
    }

    /**
     * Establece el valor de claveBancoInterm.
     *
     * @param claveBancoInterm El valor a asignar.
     */
    public void setClaveBancoInterm(String claveBancoInterm) {
        this.claveBancoInterm = claveBancoInterm;
    }

    /**
     * Regresa el valor de idPais.
     *
     * @return String.
     */
    public String getIdPais() {
        return idPais;
    }

    /**
     * Establece el valor de idPais.
     *
     * @param idPais El valor a asignar.
     */
    public void setIdPais(String idPais) {
        this.idPais = idPais;
    }

    /**
     * Regresa el valor de idPaisInterm.
     *
     * @return String.
     */
    public String getIdPaisInterm() {
        return idPaisInterm;
    }

    /**
     * Establece el valor de idPaisInterm.
     *
     * @param idPaisInterm El valor a asignar.
     */
    public void setIdPaisInterm(String idPaisInterm) {
        this.idPaisInterm = idPaisInterm;
    }

    /**
     * Regresa el valor de noCuentaBeneficiario.
     *
     * @return String.
     */
    public String getNoCuentaBeneficiario() {
        return noCuentaBeneficiario;
    }

    /**
     * Establece el valor de noCuentaBeneficiario.
     *
     * @param noCuentaBeneficiario El valor a asignar.
     */
    public void setNoCuentaBeneficiario(String noCuentaBeneficiario) {
        this.noCuentaBeneficiario = noCuentaBeneficiario;
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
     * Regresa el valor de cuentaBanoInterm.
     *
     * @return String.
     */
    public String getCuentaBancoInterm() {
        return cuentaBancoInterm;
    }

    /**
     * Establece el valor de cuentaBancoInterm.
     *
     * @param cuentaBancoInterm El valor a asignar.
     */
    public void setCuentaBancoInterm(String cuentaBancoInterm) {
        this.cuentaBancoInterm = cuentaBancoInterm;
    }

    /**
     * Regresa el valor de nombreBancoInterm.
     *
     * @return String.
     */
    public String getNombreBancoInterm() {
        return nombreBancoInterm;
    }

    /**
     * Establece el valor de nombreBancoInterm.
     *
     * @param nombreBancoInterm El valor a asignar.
     */
    public void setNombreBancoInterm(String nombreBancoInterm) {
        this.nombreBancoInterm = nombreBancoInterm;
    }

    /**
     * Regresa el valor de instrBcoBeneficiario.
     *
     * @return String.
     */
    public String getInstrBcoBeneficiario() {
        return instrBcoBeneficiario;
    }

    /**
     * Establece el valor de instrBcoBeneficiario.
     *
     * @param instrBcoBeneficiario El valor a asignar.
     */
    public void setInstrBcoBeneficiario(String instrBcoBeneficiario) {
        this.instrBcoBeneficiario = instrBcoBeneficiario;
    }

    /**
     * Regresa el valor de instrBcoIntermediario.
     *
     * @return String.
     */
    public String getInstrBcoIntermediario() {
        return instrBcoIntermediario;
    }

    /**
     * Establece el valor de instrBcoIntermediario.
     *
     * @param instrBcoIntermediario El valor a asignar.
     */
    public void setInstrBcoIntermediario(String instrBcoIntermediario) {
        this.instrBcoIntermediario = instrBcoIntermediario;
    }

    /**
     * Regresa una cadena que describe los campos de la plantilla.
     *
     * @return String.
     */
    public String toString() {
        StringBuffer sb = new StringBuffer("idPlantilla: ").append(idPlantilla).
                append(", mnemonico: ").append(mnemonico).append(", noContratoSica: ").
                append(noContratoSica);
        return sb.toString();
    }
    
    /**
     * Regresa el hashCode.
     *
     * @see java.lang.Object#hashCode()
     * @return <code>int</code>
     */
    public int hashCode() {
    	return new HashCodeBuilder().append(idPlantilla).append(mnemonico).append(noContratoSica).
    		toHashCode();
    }
    
    /**
     * Regresa true si ambos objetos son iguales.
     *
     * @param other El otro objeto a comparar.
     * @see java.lang.Object#equals(java.lang.Object).
     * @return boolean.
     */
    public boolean equals(Object other) {
    	if (!(other instanceof PlantillaInternacionalDto)) {
            return false;
        }
    	PlantillaInternacionalDto castOther = (PlantillaInternacionalDto) other;
    	return new EqualsBuilder().append(this.getIdPlantilla(), castOther.getIdPlantilla())
    		.append(getMnemonico(), castOther.getMnemonico()).isEquals();
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
     * La clave de la divisa.
     */
    private String idDivisa;

    /**
     * ABA | SWIFT | CHIPS | DD.
     */
    private String tipoCuentaBanco;

    /**
     * ABA | SWIFT | CHIPS | DD.
     */
    private String tipoCuentaBancoInterm;

    /**
     * Clave ABA, SWIFT, CHIPS o DD del banco que se trate. El formato depende de tipoCuentaBanco.
     */
    private String claveBanco;

    /**
     * Clave ABA, SWIFT, CHIPS o DD del banco intermediario que se trate. El formato depende de
     * tipoCuentaBancoInterm.
     */
    private String claveBancoInterm;

    /**
     * Clave iso del pa&iacute;s.
     */
    private String idPais;
    
    /**
     * Clave iso del pa&iacute; del banco intermediario.
     */
    private String idPaisInterm;

    /**
     * El n&uacute;mero de cuenta del beneficiario.
     */
    private String noCuentaBeneficiario;

    /**
     * El Nombre del Banco.
     */
    private String nombreBanco;

    /**
     * El n&uacute;mero de cuenta del banco intermediario. (No se encuentra en BancoIntl).
     */
    private String cuentaBancoInterm;

    /**
     * El nombre del banco intermediario. (No se encuentra en BancoIntl).
     */
    private String nombreBancoInterm;

    /**
     * Las instrucciones al banco receptor.
     */
    private String instrBcoBeneficiario;

    /**
     * Las instrucciones al banco intermediario.
     */
    private String instrBcoIntermediario;

    /**
     * El UID para Serializaci&oacute;n.
     */
    private static final long serialVersionUID = -8403927318071785353L;
}
