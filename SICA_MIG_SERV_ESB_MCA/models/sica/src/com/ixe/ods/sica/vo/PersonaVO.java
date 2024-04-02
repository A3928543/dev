package com.ixe.ods.sica.vo;

import java.io.Serializable;

import com.ixe.ods.bup.model.EmpleadoIxe;
import com.ixe.ods.bup.model.Persona;
import com.ixe.ods.bup.model.PersonaFisica;


/**
 * 
 * @author Isaac
 * 
 * Value Object para transferir datos de una persona a la
 * vista del administrador de jerarquias
 * realizada en flex
 *
 */
public class PersonaVO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7076653766622601447L;

	public PersonaVO(){}
	
	public PersonaVO(EmpleadoIxe persona) {
		this.setIdPersona(persona.getIdPersona());
		this.setNombre( persona.getNombre() + " " + persona.getPaterno() + " " + persona.getMaterno() );
		this.setRfc(persona.getRfc());
		this.setClaveUsuario( persona.getClaveUsuario() );
		this.setNoNomina( persona.getNoEmpleado());
	}
	
	
	public PersonaVO(Persona persona) {
		this.setIdPersona(persona.getIdPersona());
		this.setNombre(persona.getNombreCorto());
		this.setRfc(persona.getRfc());
	}

	public void setIdPersona(Integer idPersona) {
		this.idPersona = idPersona;
	}

	public Integer getIdPersona() {
		return idPersona;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getNombre() {
		return nombre;
	}

	public void setRfc(String rfc) {
		this.rfc = rfc;
	}

	public String getRfc() {
		return rfc;
	}

	/**
	 * @return the claveUsuario
	 */
	public String getClaveUsuario() {
		return claveUsuario;
	}

	/**
	 * @param claveUsuario the claveUsuario to set
	 */
	public void setClaveUsuario(String claveUsuario) {
		this.claveUsuario = claveUsuario;
	}

	/**
	 * @return the noNomina
	 */
	public String getNoNomina() {
		return noNomina;
	}

	/**
	 * @param noNomina the noNomina to set
	 */
	public void setNoNomina(String noNomina) {
		this.noNomina = noNomina;
	}

	/**
	 * identificador de la persona
	 */
	private Integer idPersona;
	
	/**
	 * nombre de la persona
	 */
	private String nombre;
	
	/**
	 * RFC de la persona
	 */
	private String rfc;
	
	/**
	 * La clave de usuario de la persona.
	 */
	private String claveUsuario;
	
	/**
	 * El n&uacute;mero de n&oacute;mina de la persona.
	 */
	private String noNomina;
	
}
