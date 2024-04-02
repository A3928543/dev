/*
 * $Id: RegistroExpedienteService.java,v 1.12 2008/02/22 18:25:33 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright © 2005 LegoSoft S.C.
 */

package com.ixe.ods.sica;

import com.ixe.ods.bup.model.Persona;
import com.ixe.ods.seguridad.model.Usuario;

import java.util.List;

/**
 * Business interface para el EJB de los servicios de  contrataci&oacute;n
 * 
 * @author Jean C. Favila
 * @version  $Revision: 1.12 $ $Date: 2008/02/22 18:25:33 $
 */
public interface RegistroExpedienteService {
	
	/**
	 * Obtiene el estatus de una cuenta.
	 *  
	 * @param cta La cuenta.
	 * @param tipoCta El tipo de la cuenta.
	 * @param persona La persona.
	 * @param usuario EL usuario.
	 * @return List
	 */
    List obtieneSituacionExpedienteCta(String cta, String tipoCta, Persona persona, Usuario usuario);
}
