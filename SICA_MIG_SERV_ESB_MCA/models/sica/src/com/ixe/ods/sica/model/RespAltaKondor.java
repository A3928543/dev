/*
 * $Id: RespAltaKondor.java,v 1.2 2010/04/13 20:18:35 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2009 - 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica.model;

import java.io.Serializable;

/**
 * DTO que define la respuesta del alta de un swap que proviene de Kondor.
 *
 * @author Jean C. Favila (jfavila)
 * @version $Revision: 1.2 $ $Date: 2010/04/13 20:18:35 $
 */
public class RespAltaKondor implements Serializable {

    /**
     * Constructor vac&iacute;o.
     */
    public RespAltaKondor() {
        super();
    }

    /**
     * Constructor que inicializa todas las variables de instancia.
     *
     * @param idConf El n&uacute;mero de operaci&oacute;n en KONDOR.
     * @param idDeal El n&uacute;mero de deal en SICA.
     */
    public RespAltaKondor(String idConf, String idDeal) {
        this();
        this.idConf = idConf;
        this.idDeal = idDeal;
    }

    /**
     * Regresa el valor de idConf.
     *
     * @return String.
     */
    public String getIdConf() {
        return idConf;
    }

    /**
     * Establece el valor de idConf.
     *
     * @param idConf El valor a asignar.
     */
    public void setIdConf(String idConf) {
        this.idConf = idConf;
    }

    /**
     * Regresa el valor de idDeal.
     *
     * @return String.
     */
    public String getIdDeal() {
        return idDeal;
    }

    /**
     * Establece el valor de idDeal.
     *
     * @param idDeal El valor a asignar.
     */
    public void setIdDeal(String idDeal) {
        this.idDeal = idDeal;
    }

    /**
     * El n&uacute;mero de operaci&oacute;n en KONDOR.
     */
    private String idConf;

    /**
     * El n&uacute;mero de operaci&oacute;n en SICA.
     */
    private String idDeal;

    /**
     * El UID para serializaci&oacute;n.
     */
    private static final long serialVersionUID = 4692117898754395461L;
}
