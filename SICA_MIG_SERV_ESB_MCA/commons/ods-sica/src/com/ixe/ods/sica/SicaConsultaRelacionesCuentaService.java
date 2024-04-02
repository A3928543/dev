/*
 * $Id: SicaConsultaRelacionesCuentaService.java,v 1.12 2008/02/22 18:25:03 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright © 2005 LegoSoft S.C.
 */

package com.ixe.ods.sica;

import com.ixe.bean.bup.CuentaContrato;

import java.sql.Connection;
import java.util.List;

/**
 * Business interface para el ejb de Contratacion Servicio 
 * <code>SicaConsultaRelacionesCuentaService</code>.
 * 
 * @author Javier Cordova (jcordova)
 * @version $Revision: 1.12 $ $Date: 2008/02/22 18:25:03 $
 */
public interface SicaConsultaRelacionesCuentaService {

    /**
     * Devuelve los beneficiarios de transferencia de una cuenta y la situacion de su documentacion. 
     * Devuelve una lista de HashMap's, donde las llaves son (persona, expediente).
     *  
     * @param cuentaCto La cuenta de la cual se obtendran los beneficiarios. 
     * @return List La lista de beneficiarios.
     */
    List obtenRelacionesCuentaParaRol(CuentaContrato cuenta, String idRol, Connection _conn);
    
    /**
     * Devuelve las Relaciones de Documentacion de una Cuenta Contrato.
     * Se utiliza para ver los pendientes de Documentacion de un Cliente en la Captura de un Deal.
     * 
     * @param cuenta La Cuenta del Cliente
     * @return List  Las Relaciones de Documentacion.
     */
    List obtenRelacionesCtaDocumentacion(CuentaContrato cuenta);
}