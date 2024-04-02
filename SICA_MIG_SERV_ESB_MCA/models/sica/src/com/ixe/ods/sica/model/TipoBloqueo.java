package com.ixe.ods.sica.model;

/**
 * Clase persistente para la tabla TipoBloqueo.
 *
 * @hibernate.class table="SC_TIPO_BLOQUEO"
 * mutable="true"
 * proxy="com.ixe.ods.sica.model.TipoBloqueo"
 * dynamic-update="true"
 *
 * @hibernate.query name="findBloqueoByidBloqueo" query="FROM TipoBloqueo AS tb WHERE tb.idBloqueo=?"
 *
 * @author Jesus Cortes Hernandez
 * @version $Revision: 1.1.6.2 $ $Date: 2013/08/28 23:08:11 $
 */

public class TipoBloqueo {

	public TipoBloqueo() {
		super();
	}


	/**
     * Regresa el valor de idBloqueo.
     * @hibernate.id generator-class="sequence"
     * column="ID_BLOQUEO"
     * unsaved-value="null"
     * @hibernate.generator-param name="sequence
     * value="SC_TIPO_BLOQUEO_SEQ"
     * @return int.
     */
	public int getIdBloqueo() {
		return idBloqueo;
	}

	/**
     * Fija el valor de idBloqueo.
     *
     * @param ID_BLOQUEO El valor a asignar.
     */
	public void setIdBloqueo(int idBloqueo) {
		this.idBloqueo = idBloqueo;
	}

	/**
     * Regresa el valor de descripcion.
     *
     * @return String.
     * @hibernate.property column="DESCRIPCION"
     * not-null="true"
     * unique="false"
     */
	public String getDescripcion() {
		return descripcion;
	}
	
	/**
     * Fija el valor de descripcion.
     *
     * @param DESCRIPCION El valor a asignar.
     */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	/**
	 * El id del tipo de bloqueo
	 */
	private int idBloqueo;

	/**
	 * La descripcion del tipo de Bloqueo
	 */
	private String descripcion;
	
    /**
     * Constante para identificar el tipo de bloqueo con el que cuenta el contrato.
     */
    public static final int SIN_BLOQUEO = 0;
	
    /**
     * Constante para identificar el tipo de bloqueo con el que cuenta el contrato.
     */
    public static final int RESTRINGIDO_POR_APERTURA = 1;
}
