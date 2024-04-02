/*
 * $Id: PlantillaTranNacional.java,v 1.15 2010/03/18 17:10:13 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 - 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;

/**
 * Subclase persistente de Plantilla, para la tabla SC_PLANTILLA_TRAN_NACIONAL.
 * Plantillas para transferencias a cuentas nacionales.
 *
 * @hibernate.joined-subclass table="SC_PLANTILLA_TRAN_NACIONAL"
 *
 * @hibernate.joined-subclass-key column="ID_PLANTILLA"
 *
 * @author Jean C. Favila, Javier Cordova
 * @version  $Revision: 1.15 $ $Date: 2010/03/18 17:10:13 $
 */
public class PlantillaTranNacional extends Plantilla implements IPlantillaTranNacional {

    /**
     * Constructor por default.
     */
    public PlantillaTranNacional() {
        super();
        setInternacional("N");
    }

    /**
     * Regresa true si el mnem&oacute;nico contiene la palabra 'SPEI'.
     *
     * @return boolean.
     */
    public boolean isSpei() {
        return isBancoTerceroSpei() || isBancoVostroSpei() || isBancoBancoSpei();
    }

    /**
     * @see com.ixe.ods.sica.model.IPlantillaTranNacional#isBancoVostroSpei().
     * @return boolean.
     */
    public boolean isBancoVostroSpei() {
        return getMnemonico() != null && "EMXNSPEIBCVO".equals(getMnemonico().trim());
    }

    /**
     * @see com.ixe.ods.sica.model.IPlantillaTranNacional#isBancoBancoSpei().
     * @return boolean.
     */
    public boolean isBancoBancoSpei() {
        return getMnemonico() != null && "EMXNSPEIBCBC".equals(getMnemonico().trim());
    }

    /**
     * @see com.ixe.ods.sica.model.IPlantillaTranNacional#isBancoTerceroSpei().
     * @return boolean.
     */
    public boolean isBancoTerceroSpei() {
        return getMnemonico() != null && "EMXNSPEIBCTC".equals(getMnemonico().trim());
    }

    /**
     * Regresa true si la clabe, nombreBanco, divisa y nombreTitular est&aacute;n definidos.
     *
     * @return boolean.
     */
    public boolean puedeProcesarse() {
        if (isBancoBancoSpei()) {
            return _divisa != null && StringUtils.isNotEmpty(getBeneficiario().getNombreCompleto());
        }
        else {
            return StringUtils.isNotEmpty(clabe) && StringUtils.isNotEmpty(nombreBanco) &&
                _divisa != null && StringUtils.isNotEmpty(getBeneficiario().getNombreCompleto());
        }
    }

    /**
     * Regresa el Tipo Plantilla Transferencia Nacional.
     * @return int.
     */
    public int getTipo() {
        return TIPO_TRAN_NAL;
    }

    /**
     * Regresa el nombre del beneficiario, la cuenta del banco pagador y el
     * nombre del banco pagador en una lista de mapas de informaci&oacute;n adicional.
     *
     * @return List.
     */
    public List getInfoAdicional() {
        List list = new ArrayList();
        Map map = new HashMap();
        map.put("nombre", "BENEFICIARIO");
        map.put("valor", getBeneficiario().getNombreCompleto());
        list.add(map);
        map = new HashMap();
        map.put("nombre", "CTA. BANCO PAGADOR");
        map.put("valor", getClabe());
        list.add(map);
        map = new HashMap();
        map.put("nombre", "BANCO PAGADOR");
        map.put("valor", getNombreBanco());
        list.add(map);
        return list;
    }

    /**
     * Regresa el valor de clabe.
     *
     * @hibernate.property column="CLABE"
     * not-null="false"
     * unique="false"
     * @return String.
     */
    public String getClabe() {
        return clabe;
    }

    /**
     * Fija el valor de clabe.
     *
     * @param clabe El valor a asignar.
     */
    public void setClabe(String clabe) {
        this.clabe = clabe;
    }

    /**
     * Regresa el valor de nombreBanco.
     *
     * @hibernate.property column="NOMBRE_BANCO"
     * not-null="false"
     * unique="false"
     * @return String.
     */
    public String getNombreBanco() {
        return nombreBanco;
    }

    /**
     * Fija el valor de nombreBanco.
     *
     * @param nombreBanco El valor a asignar.
     */
    public void setNombreBanco(String nombreBanco) {
        this.nombreBanco = nombreBanco;
    }

    /**
     * Regresa el valor de idBanco.
     *
     * @hibernate.property column="ID_BANCO"
     * not-null="false"
     * unique="false"
     * @return Long.
     */
    public Long getIdBanco() {
        return idBanco;
    }

    /**
     * Fija el valor de idBanco.
     *
     * @param idBanco El valor a asignar.
     */
    public void setIdBanco(Long idBanco) {
        this.idBanco = idBanco;
    }

    /**
     * Regresa el valor de divisa.
     *
     * @hibernate.many-to-one column="ID_DIVISA"
     * cascade="none"
     * class="com.ixe.ods.sica.model.Divisa"
     * outer-join="auto"
     * unique="false"
     * @return Divisa.
     */
    public Divisa getDivisa() {
        return _divisa;
    }

    /**
     * Fija el valor de divisa.
     *
     * @param divisa El valor a asignar.
     */
    public void setDivisa(Divisa divisa) {
        _divisa = divisa;
    }

    /**
     * Regresa true si ambos objetos son iguales.
     *
     * @param other El otro objeto a comparar.
     * @see java.lang.Object#equals(java.lang.Object).
     * @return boolean.
     */
    public boolean equals(Object other) {
        if (!(other instanceof IPlantillaTranNacional)) {
            return false;
        }
        IPlantillaTranNacional castOther = (IPlantillaTranNacional) other;
        return new EqualsBuilder().append(this.getIdPlantilla(),
                castOther.getIdPlantilla()).isEquals();
    }

    /**
     * El n&uacute;mero clabe de la cuenta o el n&uacute;mero de cuenta.
     */
    private String clabe;

    /**
     * El n&uacute;mero de banco.
     */
    private Long idBanco;

    /**
     * El Nombre del Banco.
     */
    private String nombreBanco;

    /**
     * Para el caso de Mex-D&oacute;lares. S&oacute;lo es posible dos divisas.
     * Relaci&oacute;n opcional muchos-a-uno con Divisa.
     */
    private Divisa _divisa;

    /**
     * El UID para serializaci&oacute;n.
     */
    private static final long serialVersionUID = 991603684623371835L;
}
