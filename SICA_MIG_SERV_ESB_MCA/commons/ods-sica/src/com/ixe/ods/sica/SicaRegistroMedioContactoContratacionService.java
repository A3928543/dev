package com.ixe.ods.sica;

import com.ixe.bean.bup.Persona;
import com.ixe.bean.bup.MedioContacto;
import com.ixe.bean.Usuario;
import com.ixe.contratacion.ContratacionException;

/**
 * Business interface para el Session Bean del M&oacute;dulo de Contrataci&oacute;n
 * RegistroMedioContactoPersonaSession que se encuentra en el JNDI:
 * ejb.RegistroMedioContactoPersonaSessionHome
 */
public interface SicaRegistroMedioContactoContratacionService 
{
	/**
	 * Registrar el medio de contacto.
     * @param Persona.  Persona cuyo medio de contacto se registrará.
     *        MedioContacto. Medio de contacto que se registrará.
     *        Usuario.  Usuario firmado en el sistema.
	*/
	public void registraMedioContacto(Persona personaActual, MedioContacto medioContacto, Usuario usuario) throws ContratacionException;
	
	/**
	 * 	Este método deberá hacer lo siguiente:
	 * 	- Modificar el medio de contacto ya existente.
	 *
     * @param MedioContacto. Medio de contacto a modificar.
     *        Usuario. Usuario firmado en el sistema.
     * @exception ContratacionException
	*/
	public void modificaMedioContacto(MedioContacto medioContacto, Usuario usuario, MedioContacto medioContactoOriginal) throws ContratacionException;
	
	/**
	 *  Elimina (físicamente) un medio de contacto.
     *  
     * @param MedioContacto. Medio de contacto a eliminar.
     *        Usuario.  Usuario firmado en el sistema.
     * @exception ContratacionException
	 */
	public void borraMedioContacto(MedioContacto medioContacto, Usuario usuario) throws ContratacionException;
}
