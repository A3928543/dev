/*
 * $Id: GrupoTrabajoPromotor.java,v 1.2 2010/04/30 17:40:12 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica.model;

import java.io.Serializable;

/**
 * Entity class that represents a grupoTrabajoPromotor.
 * It is mapped to the <code>SC_GRUPO_TRABAJO_PROMOTOR</code> table.
 * 
 * @hibernate.class table="SC_GRUPO_TRABAJO_PROMOTOR"
 * mutable="true"
 * proxy="com.ixe.ods.sica.model.GrupoTrabajoPromotor"
 * dynamic-update="true"
 * 
 * @author Israel Rebollar
 * @version 1.0 
 */
public class GrupoTrabajoPromotor implements Serializable {
	
	/**
	 * Constructor vac&iacute;o. No hace nada.
	 */
	public GrupoTrabajoPromotor() {
		super();
	}
	
	/**
	 * @hibernate.id
	 * @return the id
	 */
	public GrupoTrabajoPromotorPK getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(GrupoTrabajoPromotorPK id) {
		this.id = id;
	}


	/**
	 * El objeto que representa la llave primaria del registro.
	 */
	private GrupoTrabajoPromotorPK id = new GrupoTrabajoPromotorPK();
	
	
	/**
	 * El UID para serializaci&oacute;n.
	 */
	private static final long serialVersionUID = 8703437081529947662L;

}
