/*
 * $Id: ResultadoOperacionDto.java,v 1.1.4.2 2010/10/08 01:31:04 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica.teller.dto;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Data Transfer Object utilizado en el resultado de las operaciones.
 *
 * @author Israel Rebollar
 * @version $Revision: 1.1.4.2 $ $Date: 2010/10/08 01:31:04 $
 */
public class ResultadoOperacionDto implements Serializable {
	
	/**
     * Constructor por default, no hace nada.
     */
	public ResultadoOperacionDto() {
		super();
	}
	
	/**
	 * Inicializa el objeto con el resultado de la operaci&oacute;n y el codigo al que
     * hace referencia (error, confirmaci&oacute;n, etc.).
     * 
	 * @param codigo El c&oacute;digo de la acci&oacute;n.
	 * @param descripcion La descripci&oacute;n de la acci&oacute;n.
	 */
	public ResultadoOperacionDto(int codigo, String descripcion){
		this();
		this.codigo = codigo;
		this.descripcion = descripcion;
	}

	/**
	 * @return El c&oacutedigo de la acci&oacute;n.
	 */
	public int getCodigo() {
		return codigo;
	}

	/**
	 * @param codigo the codigo to set.
	 */
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	/**
	 * @return La descripci&oacute;n de la acci&oacute;n.
	 */
	public String getDescripcion() {
		return descripcion;
	}

	/**
	 * @param descripcion the descripcion to set
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	/**
	 * @return the objeto
	 */
	public Object getObjeto() {
		return objeto;
	}

	/**
	 * @param objeto the objeto to set
	 */
	public void setObjeto(Object objeto) {
		this.objeto = objeto;
	}

	/**
     * Regresa una cadena con los nombres y valores de las variables de instancia.
     *
     * @return String.
     */
    public String toString() {
        return new ToStringBuilder(this).append("codigo", codigo).
                append("descripcion", descripcion).
                append("objeto", objeto).toString();
    }
    
    /**
	 * El c&oacute;digo de la acci&oacute;n.
	 */
	private int codigo;
	
	/**
	 * La descipci&oacute;n de la acci&oacute;n.
	 */
	private String descripcion;
	
	/**
	 * La lista de beneficiarios (Obtetos Tipo Persona).
	 */
	private Object objeto;
		
	/**
     * UID para serializaci&oacute;n.
     */
	private static final long serialVersionUID = 8085161563048066527L;

}
