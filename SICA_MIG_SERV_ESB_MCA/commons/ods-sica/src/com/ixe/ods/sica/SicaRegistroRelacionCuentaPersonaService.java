/*
 * $Id: SicaRegistroRelacionCuentaPersonaService.java,v 1.12 2008/02/22 18:25:03 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright © 2005 LegoSoft S.C.
 */

package com.ixe.ods.sica;

import com.ixe.bean.bup.RelacionCuentaPersona;
import com.ixe.bean.Usuario;


/**
 * Business interface para el ejb de Contratacion Servicio 
 * <code>SicaRegistroRelacionCuentaPersonaService</code>.
 * 
 * @author Javier Cordova (jcordova)
 * @version $Revision: 1.12 $ $Date: 2008/02/22 18:25:03 $
 */
public interface SicaRegistroRelacionCuentaPersonaService {

    /**
     * Da de Alta un Beneficiario de Transferencia de una Cuenta.
     * 
     * @param relacionCuenta	La relacion Cuenta del Beneficiario.
     * @param personaTit		El ID Persona del Titular de la Cuenta.
     * @param usuario			El Usuario que da de Alta el Beneficiario.
     */
    void registraBeneficiarioTrans(RelacionCuentaPersona relacionCuenta, int personaTit, Usuario usuario);
}