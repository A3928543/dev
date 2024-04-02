package com.ixe.ods.sica;

import com.ixe.bean.Usuario;
import com.ixe.bean.bup.RelacionPersonas;
import com.ixe.contratacion.ContratacionException;

/**
 * Business interface para el Session Bean del M&oacute;dulo de Contrataci&oacute;n
 * RegistroRelacionPersonaSessionBean que se encuentra en el JNDI:
 * ejb.RegistroRelacionPersonaSessionRemoteHome.
 */
public interface SicaRegistroRelacionContratacionService 
{
	/**
	 * 
	 * @param relacion -- Relaci�n que mantiene la persona moral y su apoderado.
	 * @param usuario -- Usuario que realiz� la petici�n
	 * @throws ContratacionException
	 */
	public void registraRelacion(RelacionPersonas relacion, Usuario usuario) throws ContratacionException;
}
