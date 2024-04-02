/*
 * $Id: SicaConsultaProductosPersonaService.java,v 1.12 2008/02/22 18:25:02 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright © 2005 LegoSoft S.C.
 */

package com.ixe.ods.sica;

import com.ixe.bean.bup.CuentaContrato;


/**
 * Business interface para el ejb de Contratacion Servicio 
 * <code>SicaConsultaProductosPersonaService</code>.
 * 
 * @author Javier Cordova (jcordova)
 * @version $Revision: 1.12 $ $Date: 2008/02/22 18:25:02 $
 */
public interface SicaConsultaProductosPersonaService {

    /**
     * Encuentra el Objeto Cuenta Contrato dado su Id.
     * 
     * @param NoCuenta El Id de la Cuenta.
     * @return CuentaContrato.
     */
    CuentaContrato obtenCuentaContrato(String NoCuenta);
}