/*
 * $Id: PlantillaNacional.java,v 1.12 2008/02/22 18:25:20 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright © 2004 LegoSoft S.C.
 */
package com.ixe.ods.sica.model;

import org.apache.commons.lang.builder.EqualsBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Subclase persistente de Plantilla, para la tabla SC_PLANTILLA_NACIONAL.
 * Plantillas simples en Moneda Nacional.
 *
 * @hibernate.joined-subclass table="SC_PLANTILLA_NACIONAL"
 *
 * @hibernate.joined-subclass-key column="ID_PLANTILLA"
 *
 * @author Jean C. Favila
 * @version  $Revision: 1.12 $ $Date: 2008/02/22 18:25:20 $
 */
public class PlantillaNacional extends Plantilla implements IPlantillaNacional {

    /**
     * Constructor por default. Inicializa parametros.
     */
    public PlantillaNacional() {
        super();
        setInternacional("N");
    }

    /**
     * Regresa true si el beneficiario est&aacute; definido.
     *
     * @return boolean.
     */
    public boolean puedeProcesarse() {
        return getBeneficiario() != null;
    }

    /**
     * Regresa el Tipo Plantilla Nacional
     * @return int.
     */
    public int getTipo() {
        return TIPO_NAL;
    }

    /**
     * Regresa el nombre del beneficiario en el mapa de informaci&oacute;n
     * adicional.
     *
     * @return List.
     */
    public List getInfoAdicional() {
        List list = new ArrayList();
        Map map = new HashMap();
        map.put("nombre", "BENEFICIARIO");
        map.put("valor", getBeneficiario().getNombreCompleto());
        list.add(map);
        return list;
    }

    /**
     * Regresa true si ambos objetos son iguales.
     *
     * @param other El otro objeto a comparar.
     * @see java.lang.Object#equals(java.lang.Object).
     * @return boolean.
     */
    public boolean equals(Object other) {
        if (!(other instanceof IPlantillaNacional)) {
            return false;
        }
        IPlantillaNacional castOther = (IPlantillaNacional) other;
        return new EqualsBuilder().append(this.getIdPlantilla(), castOther.getIdPlantilla()).isEquals();
    }

}