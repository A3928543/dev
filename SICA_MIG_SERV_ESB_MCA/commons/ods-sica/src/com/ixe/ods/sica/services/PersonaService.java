package com.ixe.ods.sica.services;

import com.ixe.bean.bup.Persona;
import com.ixe.ods.sica.model.Deal;
import com.ixe.ods.sica.sdo.dto.PersonaDto;
import com.ixe.ods.sica.sdo.dto.RespuestaDto;

/**
 * The Interface PersonaService.
 */
public interface PersonaService {
	
	/**
	 * Buscar persona por id.
	 *
	 * @param idPersona the id persona
	 * @return the persona dto
	 */
	PersonaDto buscarPersonaPorId(Integer idPersona);
	
	/**
	 * Guardar persona.
	 *
	 * @param perDto the per dto
	 * @param idPersonaUser the id persona user
	 * @return the persona
	 */
	Persona guardarPersona(PersonaDto perDto, Integer idPersonaUser);
	
	/**
	 * Actualizar persona.
	 *
	 * @param dto the dto
	 * @return true, if successful
	 */
	boolean actualizarPersona(PersonaDto dto, Integer idPersonaUser);
	
	/**
	 * Checks if is valida informacion general persona.
	 *
	 * @param idPersona the id persona
	 * @param oper the oper
	 * @return true, if is valida informacion general persona
	 */
	boolean isValidaInformacionGeneralPersona(Integer idPersona, String oper);
	
	/**
	 * Checks if is valida informacion general persona.
	 *
	 * @param idPersona the id persona
	 * @return the respuesta dto
	 */
	RespuestaDto isValidaInformacionGeneralPersona(Integer idPersona);
	
	/**
	 * Checks if is valida informacion general persona.
	 *
	 * @param noCuenta the no cuenta
	 * @return true, if is valida informacion general persona
	 */
	boolean isValidaInformacionGeneralPersona(String noCuenta);
	
	/**
	 * Envia correo.
	 *
	 * @param oper the oper
	 * @param error the error
	 */
	void enviaCorreo(String oper, String error);
	
	/**
	 * Checks if is valida informacion general persona.
	 *
	 * @param deal the deal
	 * @return true, if is valida informacion general persona
	 */
	boolean isValidaInformacionGeneralPersona(Deal deal);

}
