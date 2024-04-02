/*
 * $Id: PlantillaCuentaIxe.java,v 1.12 2008/02/22 18:25:22 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright © 2005 LegoSoft S.C.
 */

package com.ixe.ods.sica.model;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Subclase persistente de Plantilla, para la tabla SC_PLANTILLA_CUENTA_IXE.
 *
 * @hibernate.joined-subclass table="SC_PLANTILLA_CUENTA_IXE"
 *
 * @hibernate.joined-subclass-key column="ID_PLANTILLA"
 *
 * @author Jean C. Favila, Javier Cordova
 * @version  $Revision: 1.12 $ $Date: 2008/02/22 18:25:22 $
 */
public class PlantillaCuentaIxe extends Plantilla implements IPlantillaCuentaIxe {

    /**
     * Constructor por default que inicializa parametros.
     */
    public PlantillaCuentaIxe() {
        super();
        setInternacional("N");
    }

    /**
     * Regresa true si noCuentaIxe y nombreTitular no est&aacute;n vac&iacute;os.
     *
     * @return boolean.
     */
    public boolean puedeProcesarse() {
        return StringUtils.isNotEmpty(noCuentaIxe) && (getBeneficiario() != null);
    }

    /**
     * Regresa el Tipo de Plantilla, para este caso Nacional.
     */
    public int getTipo() {
        return TIPO_NAL;
    }

    /**
     * Regresa informacion de la Plantilla como HashMap.
     * 
     * @return List El HashMap de Informacion Adicional de la Plantilla.
     */
    public List getInfoAdicional() {
        List list = new ArrayList();
        Map map = new HashMap();
        map.put("nombre", "BENEFICIARIO");
        map.put("valor", getBeneficiario().getNombreCompleto());
        list.add(map);
        map = new HashMap();
        map.put("nombre", "CTA. BENEFICIARIO");
        map.put("valor", noCuentaIxe);
        list.add(map);
        return list;
    }

    /**
     * Regresa el valor de noCuentaIxe.
     *
     * @hibernate.property column="NO_CUENTA_IXE"
     * not-null="true"
     * unique="false"
     * @return String.
     */
    public String getNoCuentaIxe() {
        return noCuentaIxe;
    }

    /**
     * Fija el valor de noCuentaIxe.
     *
     * @param noCuentaIxe El valor a asignar.
     */
    public void setNoCuentaIxe(String noCuentaIxe) {
        this.noCuentaIxe = noCuentaIxe;
    }

    /**
     * Regresa true si ambos objetos son iguales.
     *
     * @param other El otro objeto a comparar.
     * @see java.lang.Object#equals(java.lang.Object).
     * @return boolean.
     */
    public boolean equals(Object other) {
        if (!(other instanceof IPlantillaCuentaIxe)) {
            return false;
        }
        IPlantillaCuentaIxe castOther = (IPlantillaCuentaIxe) other;
        return new EqualsBuilder().append(this.getIdPlantilla(), castOther.getIdPlantilla()).isEquals();
    }

    /**
     * El n&uacute;mero de la cuenta ixe.
     */
    private String noCuentaIxe;

}