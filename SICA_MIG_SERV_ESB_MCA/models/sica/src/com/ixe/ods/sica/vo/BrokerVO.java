/*
 * $Id: BrokerVO.java,v 1.11 2008/02/22 18:25:25 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright © 2006 LegoSoft S.C.
 */

package com.ixe.ods.sica.vo;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * Value Object para Broker para la comunicaci&oacute;n del SICA con las applicaciones en Flex (Captura de Swaps).
 * 
 * @author Jean C. Favila
 * @version $Revision: 1.11 $ $Date: 2008/02/22 18:25:25 $
 */
public class BrokerVO implements Serializable {

    /**
     * Constructor vac&iacute;o.
     */
    public BrokerVO() {
        super();
    }

    /**
     * Constructor para inicializar todas las variables de instancia.
     *
     * @param idPersona El n&uacute;mero de persona.
     * @param nombreCompleto El nombre completo de la contraparte.
     * @param claveReuters La clave reuters de la contraparte.
     * @param noCuenta El n&uacute;mero de contrato sica.
     * @param idPromotor El idPersona del Promotor que atiende a esta contraparte.
     */
    public BrokerVO(int idPersona, String nombreCompleto, String claveReuters, String noCuenta, int idPromotor) {
        this();
        this.idPersona = idPersona;
        this.nombreCompleto = nombreCompleto;
        this.claveReuters = claveReuters;
        this.noCuenta = noCuenta;
        this.idPromotor = idPromotor;
    }
    
    /**
     * Constructor para inicializar todas las variables de instancia.
     *
     * @param idPersona El n&uacute;mero de persona.
     * @param nombreCompleto El nombre completo de la contraparte.
     * @param claveReuters La clave reuters de la contraparte.
     * @param noCuenta El n&uacute;mero de contrato sica.
     * @param idPromotor El idPersona del Promotor que atiende a esta contraparte.
     * @param idSector El sector econ&oacute;mico de la contraparte.
     */
    public BrokerVO(int idPersona, String nombreCompleto, String claveReuters, String noCuenta, int idPromotor, String idSector) {
        this();
        this.idPersona = idPersona;
        this.nombreCompleto = nombreCompleto;
        this.claveReuters = claveReuters;
        this.noCuenta = noCuenta;
        this.idPromotor = idPromotor;
        this.idSector = idSector;
    }
    
    /**
     * Obtienen el valor de idPersona.
     * 
     * @return int
     */
    public int getIdPersona() {
        return idPersona;
    }

    /**
     * Asigna el valor para idPersona.
     * 
     * @param idPersona El valor para idPersona
     */
    public void setIdPersona(int idPersona) {
        this.idPersona = idPersona;
    }

    /**
     * Obtiene el valor de nombreCompleto.
     * 
     * @return String
     */
    public String getNombreCompleto() {
        return nombreCompleto;
    }

    /**
     * Asigna el valor para nombreCompleto.
     * 
     * @param nombreCompleto El valor para nombreCompleto
     */
    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    /**
     * Obtiene el valor de idPromotor.
     * 
     * @return int
     */
    public int getIdPromotor() {
        return idPromotor;
    }

    /**
     * Asigna el valor para idPromotor.
     * 
     * @param idPromotor El valor para idPromotor.
     */
    public void setIdPromotor(int idPromotor) {
        this.idPromotor = idPromotor;
    }

    /**
     * Obtiene el valor de noCuenta.
     * 
     * @return String
     */
    public String getNoCuenta() {
        return noCuenta;
    }

    /**
     * Asigna el valor para noCuenta.
     * 
     * @param noCuenta El n&uacute;mero de contrato sica a asignar.
     */
    public void setNoCuenta(String noCuenta) {
        this.noCuenta = noCuenta;
    }

    /**
     * Obtiene el valor de claveReuters. 
     *  
     * @return String.
     */
    public String getClaveReuters() {
        return claveReuters;
    }

    /**
     * Asigna el valor para claveReuters.
     * 
     * @param claveReuters El valor para claveReuters.
     */
    public void setClaveReuters(String claveReuters) {
        this.claveReuters = claveReuters;
    }

    /**
     * Obtiene el valor de idContraparte.
     * 
     * @retur Integer.
     */
    public String getIdSector() {
		return idSector;
	}

    /**
     * Asigna el valor para idContraparte.
     * 
     * @param idContraparte El valor para idContraparte.
     */
	public void setIdSector(String idSector) {
		this.idSector = idSector;
	}

	/**
     * Utiliza ToStringBuilder para regresar una cadena con la descripci&oacute;n de los atributos del objeto.
     *
     * @return String.
     */
    public String toString() {
        return new ToStringBuilder(this).append("idPersona", idPersona).append("nombreCompleto", nombreCompleto).
                append("idPromotor", idPromotor).append("noCuenta", noCuenta).append("claveReuters", claveReuters).
                append("idSector", idSector).toString();
    }
    
    /**
     * El n&uacute;mero de persona.
     */
    private int idPersona;

    /**
     * El nombre completo de la persona.
     */
    private String nombreCompleto;

    /**
     * El n&uacute;mero de promotor asignado al contrato sica.
     */
    private int idPromotor;

    /**
     * El n&uacute;mero de contrato sica.
     */
    private String noCuenta;

    /**
     * La clave reuters de la contrapartes.
     */
    private String claveReuters;
    
    /**
     * El id contraparte del Broker.
     */
    private String idSector;
    
}
