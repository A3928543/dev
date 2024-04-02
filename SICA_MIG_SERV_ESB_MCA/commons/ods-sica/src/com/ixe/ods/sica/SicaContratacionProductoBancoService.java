/*
 * $Id: SicaContratacionProductoBancoService.java,v 1.12.62.1 2013/09/19 21:46:30 mejiar Exp $
 * Ixe Grupo Financiero
 * Copyright © 2005 LegoSoft S.C.
 */

package com.ixe.ods.sica;

import com.ixe.bean.bup.Persona;
import com.ixe.bean.Usuario;
import com.ixe.contratacion.ContratacionException;


/**
 * Business interface para el ejb de Contratacion Servicio 
 * <code>SicaContratacionProductoBancoService</code>.
 * 
 * @author Javier Cordova (jcordova)
 * @version $Revision: 1.12.62.1 $ $Date: 2013/09/19 21:46:30 $
 */
public interface SicaContratacionProductoBancoService {
    
    /**
     * Servicio de Alta Rapida de Contratos SICA.
     * 
     * @param persona	El Cliente.
     * @param usuario	El Usuario que Registra la Cuenta.
     * @return String 	El Numero de Cuenta Contrato SICA dado de Alta.
     */
    String registraCuentaCambios  (Persona persona, Usuario usuario) throws ContratacionException;//JDCH SE AGUREGO PARA QUE LANCE EXCEPCION DE CONTRATACION
    
}