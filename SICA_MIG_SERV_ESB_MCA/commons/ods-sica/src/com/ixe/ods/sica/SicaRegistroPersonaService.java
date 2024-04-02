/*
 * $Id: SicaRegistroPersonaService.java,v 1.12.30.1 2010/08/10 19:29:17 galiciad Exp $
 * Ixe Grupo Financiero
 * Copyright © 2005 LegoSoft S.C.
 */

package com.ixe.ods.sica;

import com.ixe.bean.Usuario;
import com.ixe.bean.bup.Persona;
import com.ixe.contratacion.ContratacionException;


/**
 * Business interface para el ejb de Contratacion Servicio 
 * <code>SicaRegistroPersonaService</code>.
 * 
 * @author Javier Cordova (jcordova)
 * @version $Revision: 1.12.30.1 $ $Date: 2010/08/10 19:29:17 $
 */
public interface SicaRegistroPersonaService {
    
    /**
     * Servicio de Alta Rapida de Clientes.
     * 
     * @param personaFisicaOMoral	El Cliente a dar de Alta.
     * @param usuario				El Usuario que da de Alta al Cliente.
     * @return Persona				La nueva Persona.
     */
    Persona registraPersona(Persona personaFisicaOMoral, Usuario usuario) throws ContratacionException;
}