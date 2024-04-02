package com.ixe.ods.sica.vo;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.ixe.ods.seguridad.model.NodoJerarquia;
import com.ixe.ods.sica.model.SicaNodoJerarquia;

/**
 * 
 * @author Isaac
 * 
 * Value object para transferir datos de un nodo de la jerarquia
 *  a la vista del administrador de jerarquias realizada en flex
 *
 */
public class NodoJerarquiaVO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8040268043360371894L;
	
	
	public NodoJerarquiaVO(){}
	
	public NodoJerarquiaVO(NodoJerarquia  nj){
		this.setIdNodo(nj.getId());
		this.setIdAlterno(nj.getIdAlterno());
		if(nj.getAlterno() != null){
			this.setNombreAlterno( nj.getAlterno().getNombreCorto());
		}
		this.setIdJefe(100);
		this.setIdJerarquia(nj.getIdJerarquia());
		this.setPersona(new PersonaVO(nj.getPersona()));
	}

	/**
	 * Contructor para NodoJerarquiaVO a partir de un SicaNodoJerarquia
	 * 
	 * @param nodoJerarquia
	 */
	public NodoJerarquiaVO(SicaNodoJerarquia  nodoJerarquia){
		
		this.setIdNodo(nodoJerarquia.getIdNodoJerarquia());
		if(nodoJerarquia.getAlterno() != null){
			this.setIdAlterno(nodoJerarquia.getAlterno().getIdPersona());
			this.setNombreAlterno( nodoJerarquia.getAlterno().getNombre() + " " +
					nodoJerarquia.getAlterno().getPaterno() + " " + nodoJerarquia.getAlterno().getMaterno());
			this.setFechaInicioAlterno( nodoJerarquia.getFechaInicioAlterno() );
			this.setFechaFinalAlterno( nodoJerarquia.getFechaFinAlterno() );
		}
		this.setIdJerarquia(nodoJerarquia.getJerarquia().getIdJerarquia());
		this.setPersona(new PersonaVO(nodoJerarquia.getPersona()));
	}

	/**
	 * @return the id
	 */
	public int getIdNodo() {
		return idNodo;
	}
	
	/**
	 * @param id the id to set
	 */
	public void setIdNodo(int idNodo) {
		this.idNodo = idNodo;
	}
	
	/**
	 * @return the idAlterno
	 */
	public Integer getIdAlterno() {
		return idAlterno;
	}
	
	/**
	 * @param idAlterno the idAlterno to set
	 */
	public void setIdAlterno(Integer idAlterno) {
		this.idAlterno = idAlterno;
	}
	
	/**
	 * @return the nombreAlterno
	 */
	public String getNombreAlterno() {
		return nombreAlterno;
	}
	
	/**
	 * @param nombreAlterno the nombreAlterno to set
	 */
	public void setNombreAlterno(String nombreAlterno) {
		this.nombreAlterno = nombreAlterno;
	}
	
	/**
	 * @return the idJerarquia
	 */
	public int getIdJerarquia() {
		return idJerarquia;
	}
	
	/**
	 * @param idJerarquia the idJerarquia to set
	 */
	public void setIdJerarquia(int idJerarquia) {
		this.idJerarquia = idJerarquia;
	}
	
	/**
	 * @return the persona
	 */
	public PersonaVO getPersona() {
		return persona;
	}
	
	/**
	 * @param persona the persona to set
	 */
	public void setPersona(PersonaVO persona) {
		this.persona = persona;
	}
	
	/**
	 * @return the idJefe
	 */
	public int getIdJefe() {
		return idJefe;
	}
	
	/**
	 * @param idJefe the idJefe to set
	 */
	public void setIdJefe(int idJefe) {
		this.idJefe = idJefe;
	}
	
	/**
	 * @return the length
	 */
	public int getLength() {
		return length;
	}
	
	/**
	 * @param length the length to set
	 */
	public void setLength(int length) {
		this.length = length;
	}
	
	/**
	 * @return the fechaInicioAlterno
	 */
	public String getFechaInicioAlterno() {
		return fechaInicioAlterno;
	}

	/**
	 * @param fechaInicioAlterno the fechaInicioAlterno to set
	 */
	public void setFechaInicioAlterno(String fechaInicioAlterno) {
		this.fechaInicioAlterno = fechaInicioAlterno;
	}
	
	/**
	 * Establece la fecha de inicio del Alterno con la cadena
	 * representativa del objeto Date.
	 * @param fechaInicioAlterno
	 */
	public void setFechaInicioAlterno(Date fechaInicioAlterno) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		try{
			this.fechaInicioAlterno = dateFormat.format(fechaInicioAlterno);
		}catch( Exception er){
			this.fechaInicioAlterno = "";
		}
	}

	/**
	 * @return the fechaFinalAlterno
	 */
	public String getFechaFinalAlterno() {
		return fechaFinalAlterno;
	}

	/**
	 * @param fechaFinalAlterno the fechaFinalAlterno to set
	 */
	public void setFechaFinalAlterno(String fechaFinalAlterno) {
		this.fechaFinalAlterno = fechaFinalAlterno;
	}
	
	/**
	 * Establece la fecha final del Alterno con la cadena
	 * representativa del objeto Date.
	 * @param fechaFinAlterno
	 */
	public void setFechaFinalAlterno(Date fechaFinalAlterno) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		try{
			this.fechaFinalAlterno = dateFormat.format(fechaFinalAlterno);
		}catch( Exception er){
			this.fechaFinalAlterno = "";
		}
	}
	
	/**
	 * Identificador del objeto NodoJerarquia
	 */
	private int idNodo;
	
	/**
	 * Objeto persona vo contenido por el nodo de la jerarquia
	 */
	private PersonaVO persona;
	
	/**
	 * Identificador alterno
	 */
	private Integer idAlterno;
	
	/**
	 * Nombre del alterno
	 */
	private String nombreAlterno;
	
	/**
	 * Identificador de la jerarquia
	 */
	private int idJerarquia;
	
	/**
	 * Identificador del nodo que hace referencia al jefe
	 * dentro de la jerarquia
	 */
	private int idJefe;
	
	/**
	 * Longitud del subarbol del nodo
	 */
	private int length;
	
	/**
	 * La fecha de inicio del usuario alterno.
	 */
	private String fechaInicioAlterno;
	
	/**
	 * La fecha final del usuario alterno. 
	 */
	private String fechaFinalAlterno;


	
	
	
}
