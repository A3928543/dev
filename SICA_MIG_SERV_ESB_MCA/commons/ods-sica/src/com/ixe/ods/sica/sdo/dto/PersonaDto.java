package com.ixe.ods.sica.sdo.dto;

import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.ixe.bean.Constantes;
import com.ixe.bean.bup.Catalogo;
import com.ixe.bean.bup.Direccion;
import com.ixe.bean.bup.Persona;
import com.ixe.bean.bup.PersonaFisica;
import com.ixe.bean.bup.PersonaMoral;
import com.ixe.ods.sica.model.RegulatorioDatosPM;
import com.ixe.ods.sica.model.RegulatorioInstitucion;
import com.ixe.ods.sica.model.RegulatorioPerfil;

/**
 * The Class PersonaDto.
 */
public class PersonaDto {
	
	/** The persona. */
	private Persona persona;
	
	/** The id extranjero. */
	private String idExtranjero;
	
	/** The direccion. */
	private Direccion direccion;
	
	/** The apoderado. */
	private Persona apoderado;
	
	/** The regulatorio perfil. */
	private RegulatorioPerfil regulatorioPerfil;
	
	/** The regulatorio datos PM. */
	private RegulatorioDatosPM regulatorioDatosPM;
	
	/** The regulatorio institucion. */
	private RegulatorioInstitucion regulatorioInstitucion;
	
	/** The fecha. */
	private Date fecha;
	
	/** The pais nacimiento. */
	private Catalogo paisNacimiento;
	
	/** The actividad giro. */
	private Catalogo actividadGiro;
	
	/**
	 * Instantiates a new persona dto.
	 */
	public PersonaDto() {
	}

	/**
	 * Instantiates a new persona dto.
	 *
	 * @param persona the persona
	 */
	public PersonaDto(Persona persona) {
		this.persona = persona;
	}

	/**
	 * Gets the persona.
	 *
	 * @return the persona
	 */
	public Persona getPersona() {
		return persona;
	}

	/**
	 * Sets the persona.
	 *
	 * @param persona the new persona
	 */
	public void setPersona(Persona persona) {
		this.persona = persona;
	}

	/**
	 * Gets the id extranjero.
	 *
	 * @return the id extranjero
	 */
	public String getIdExtranjero() {
		return idExtranjero;
	}

	/**
	 * Sets the id extranjero.
	 *
	 * @param idExtranjero the new id extranjero
	 */
	public void setIdExtranjero(String idExtranjero) {
		this.idExtranjero = idExtranjero;
	}
	
	/**
	 * Checks if is persona moral.
	 *
	 * @return true, if is persona moral
	 */
	public boolean isPersonaMoral() {
		return (StringUtils.isNotEmpty(persona.getTipoPersona()) &&
				Constantes.ID_TIPO_PERSONA_MORAL.equals(persona.getTipoPersona())) || 
				persona instanceof PersonaMoral;
	}
	
	/**
	 * Checks if is persona fisica.
	 *
	 * @return true, if is persona fisica
	 */
	public boolean isPersonaFisica() {
		boolean personaFisica = (StringUtils.isNotEmpty(persona.getTipoPersona()) &&
					(Constantes.ID_TIPO_PERSONA_FISICA.equals(persona.getTipoPersona()) ||
					 Constantes.ID_TIPO_PERSONA_FISICA_AE.equals(persona.getTipoPersona()))) ||
					 persona instanceof PersonaFisica;
		return personaFisica;
	}
	
	/**
	 * Checks if is nacionalidad mexicana.
	 *
	 * @return true, if is nacionalidad mexicana
	 */
	public boolean isNacionalidadMexicana() {
		return StringUtils.isNotEmpty(persona.getIdPais())  && 
				Constantes.ID_PAIS_MEXICO.equals(persona.getIdPais());
	}
	
	/**
	 * Checks if is nacionalidad extranjera.
	 *
	 * @return true, if is nacionalidad extranjera
	 */
	public boolean isNacionalidadExtranjera() {
		return StringUtils.isNotEmpty(persona.getIdPais())  && 
				!Constantes.ID_PAIS_MEXICO.equals(persona.getIdPais());
	}
	
	/**
	 * Checks if is persona nueva.
	 *
	 * @return true, if is persona nueva
	 */
	public boolean isPersonaNueva() {
		return persona.getIdPersona() == 0;
	}

	/**
	 * Gets the direccion.
	 *
	 * @return the direccion
	 */
	public Direccion getDireccion() {
		return direccion;
	}

	/**
	 * Sets the direccion.
	 *
	 * @param direccion the new direccion
	 */
	public void setDireccion(Direccion direccion) {
		this.direccion = direccion;
	}

	/**
	 * Gets the apoderado.
	 *
	 * @return the apoderado
	 */
	public Persona getApoderado() {
		return apoderado;
	}

	/**
	 * Sets the apoderado.
	 *
	 * @param apoderado the new apoderado
	 */
	public void setApoderado(Persona apoderado) {
		this.apoderado = apoderado;
	}

	/**
	 * Gets the regulatorio perfil.
	 *
	 * @return the regulatorio perfil
	 */
	public RegulatorioPerfil getRegulatorioPerfil() {
		return regulatorioPerfil;
	}

	/**
	 * Sets the regulatorio perfil.
	 *
	 * @param regulatorioPerfil the new regulatorio perfil
	 */
	public void setRegulatorioPerfil(RegulatorioPerfil regulatorioPerfil) {
		this.regulatorioPerfil = regulatorioPerfil;
	}

	/**
	 * Gets the regulatorio datos PM.
	 *
	 * @return the regulatorio datos PM
	 */
	public RegulatorioDatosPM getRegulatorioDatosPM() {
		return regulatorioDatosPM;
	}

	/**
	 * Sets the regulatorio datos PM.
	 *
	 * @param regulatorioDatosPM the new regulatorio datos PM
	 */
	public void setRegulatorioDatosPM(RegulatorioDatosPM regulatorioDatosPM) {
		this.regulatorioDatosPM = regulatorioDatosPM;
	}

	/**
	 * Gets the regulatorio institucion.
	 *
	 * @return the regulatorio institucion
	 */
	public RegulatorioInstitucion getRegulatorioInstitucion() {
		return regulatorioInstitucion;
	}

	/**
	 * Sets the regulatorio institucion.
	 *
	 * @param regulatorioInstitucion the new regulatorio institucion
	 */
	public void setRegulatorioInstitucion(RegulatorioInstitucion regulatorioInstitucion) {
		this.regulatorioInstitucion = regulatorioInstitucion;
	}

	/**
	 * Gets the fecha.
	 *
	 * @return the fecha
	 */
	public Date getFecha() {
		return fecha;
	}

	/**
	 * Sets the fecha.
	 *
	 * @param fecha the new fecha
	 */
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	/**
	 * Gets the pais nacimiento.
	 *
	 * @return the pais nacimiento
	 */
	public Catalogo getPaisNacimiento() {
		return paisNacimiento;
	}

	/**
	 * Sets the pais nacimiento.
	 *
	 * @param paisNacimiento the new pais nacimiento
	 */
	public void setPaisNacimiento(Catalogo paisNacimiento) {
		this.paisNacimiento = paisNacimiento;
	}

	/**
	 * Gets the actividad giro.
	 *
	 * @return the actividad giro
	 */
	public Catalogo getActividadGiro() {
		return actividadGiro;
	}

	/**
	 * Sets the actividad giro.
	 *
	 * @param actividadGiro the new actividad giro
	 */
	public void setActividadGiro(Catalogo actividadGiro) {
		this.actividadGiro = actividadGiro;
	}

}
