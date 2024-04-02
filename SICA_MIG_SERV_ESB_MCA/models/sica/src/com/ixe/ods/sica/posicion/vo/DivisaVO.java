/*
 * $Id: DivisaVO.java,v 1.12 2008/02/22 18:25:26 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2006 - 2007 LegoSoft S.C.
 */
package com.ixe.ods.sica.posicion.vo;

import com.ixe.ods.sica.model.Divisa;
import java.io.Serializable;

/**
 * Value Object para Divisa para la comunicaci&oacute;n del SICA con las applicaciones en Flex
 * (Monitor de la Posici&oacute;n).
 * 
 * @author Rogelio Chavez
 * @version $Revision: 1.12 $ $Date: 2008/02/22 18:25:26 $
 */
public class DivisaVO implements Serializable {

    /**
	 * Constructor que inicializa los valores para VO.
	 * 
	 * @param idDivisa El id de la divisa.
	 * @param descripcion La descripci&oacute;n de la divisa.
	 * @param icono La imagen de la divisa.
	 */
	public DivisaVO(String idDivisa, String descripcion, String icono) {
		this.idDivisa = idDivisa;
		this.descripcion = descripcion;
		this.icono = icono;
    }

    /**
     * Construye el objeto DivisaVO a partir de un objeto Divisa.
     *
     * @param divisa La instancia de la clase Divisa.
     */
    public DivisaVO(Divisa divisa) {
        this(divisa.getIdDivisa(), divisa.getDescripcion(), divisa.getIcono());
        this.tipo = divisa.getTipo();
    }

    /**
	 * Obtiene el identificador de la divisa
	 * 
	 * @return String
	 */
	public String getIdDivisa() {
		return idDivisa;
	}

	/**
	 * Obtiene la descripcion de la divisa
	 * 
	 * @return String
	 */
	public String getDescripcion() {
		return descripcion;
	}

	/**
	 * Obtiene la ruta donde se encuentra el icono de la divisa
	 * 
	 * @return String
	 */
	public String getIcono() {
		return icono;
	}

    /**
     * Establece el valor de icono.
     *
     * @param icono El valor a asignar.
     */
    public void setIcono(String icono) {
        this.icono = icono;
    }

    /**
     * Regresa el valor de tipo.
     *
     * @return String.
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * Establece el valor de tipo.
     *
     * @param tipo El valor a asignar.
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    /**
	 * Identificador de la divisa
	 */
	private String idDivisa;
    
    /**
	 * Descripcion de la divisa
	 */
	private String descripcion;

    /**
	 * Icono para representar a la divisa
	 */
	private String icono;

    /**
     * F)recuente, N)o frecuente, M)etal amonedado.
     */
    private String tipo;
    
    /**
     * El uid de serializaci&oacute;n.
     */
    private static final long serialVersionUID = 6221539238289098562L;
}
