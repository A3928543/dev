package com.ixe.ods.sica;

import com.ixe.contratacion.ContratacionException;

/**
 * Business interface para el Session Bean del M&oacute;dulo de Contrataci&oacute;n
 * RegistroRelacionPersonaSessionBean que se encuentra en el JNDI:
 * ejb.RegistroRelacionPersonaSessionRemoteHome.
 */
public interface SicaConsultaRelacionContratacionService 
{
	/**
	 * 
	 * @param idPersona Persona 1
	 * @param tipoRes Bandera que indica el tipo de objeto de respuesta Constantes.ID_RES_ARRAY_LIST
	 * @return Arreglo de objetos de tipo RelacionPersonas
	 * @throws ContratacionException
	 */
	public Object obtenRelaciones(int idPersona, int tipoRes) throws ContratacionException;
}
