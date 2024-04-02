/*
 * $Id: PlantillaNacionalDto.java,v 1.1.4.2 2010/10/08 01:31:04 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica.teller.dto;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Jean C. Favila (jfavila)
 * @version $Revision: 1.1.4.2 $ $Date: 2010/10/08 01:31:04 $
 */
public class PlantillaNacionalDto implements Serializable {

    /**
     * Constructor por default.
     */
    public PlantillaNacionalDto() {
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
     */
    public PlantillaNacionalDto(int idPlantilla, String clavePlantilla, String noContratoSica,
                                String mnemonico, Date ultimaModificacion, String statusPlantilla,
                                int idBeneficiario, String nombreBeneficiario) {
        this();
        this.idPlantilla = idPlantilla;
        this.clavePlantilla = clavePlantilla;
        this.noContratoSica = noContratoSica;
        this.mnemonico = mnemonico;
        this.ultimaModificacion = ultimaModificacion;
        this.statusPlantilla = statusPlantilla;
        this.idBeneficiario = idBeneficiario;
        this.nombreBeneficiario = nombreBeneficiario;
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
     * El UID para serializaci&oacute;n.
     */
    private static final long serialVersionUID = -534335475515034476L;
}
