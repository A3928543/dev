/*
 * $Id: ClienteContratoDto.java,v 1.3.40.1.16.1 2015/03/30 18:18:34 mejiar Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2009 LegoSoft S.C.
 */

package com.ixe.ods.sica.dto;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Data Transfer Object utilizado en las consultas de clientes.
 *
 * @author Jean C. Favila
 * @version $Revision: 1.3.40.1.16.1 $ $Date: 2015/03/30 18:18:34 $
 */
public class ClienteContratoDto implements Serializable {

    /**
     * Constructor por default, no hace nada.
     */
    public ClienteContratoDto() {
        super();
    }

    /**
     * Constructor que inicializa todas las variables de instancia.
     *
     * @param noCuenta El n&uacute;mero de contrato sica.
     * @param nombreCorto El nombre del cliente.
     * @param tipoPersona 'PF' para Persona F&iacute;sica, 'PM' para Persona Moral.
     * @param esGrupo Si es o no grupo empresarial.
     * @param idSector El n&uacute;mero de sector econ&oacute;mico.
     * @param idPersona El identificador del cliente como persona de la BUP.
     * @param idGrupoEmpresarial El n&uacute;mero de grupo empresarial.
     * @param idEjecutivo El identificador del ejecutivo asignado al cliente.
     * @param idGpoEmpresarialContrato El n&uacute;mero de grupo empresarial del contrato sica.
     * @param statusEjecutivo El status del ejecutivo.
     */
    public ClienteContratoDto(String noCuenta, String nombreCorto, String tipoPersona,
                              Boolean esGrupo, Integer idSector, Integer idPersona,
                              Integer idGrupoEmpresarial, Integer idEjecutivo,
                              Integer idGpoEmpresarialContrato, String statusEjecutivo,Integer idBloqueo) {
        this();
        this.noCuenta = noCuenta;
        this.nombreCorto = nombreCorto;
        this.tipoPersona = tipoPersona;
        this.esGrupo = esGrupo;
        this.idSector = idSector;
        this.idPersona = idPersona;
        this.idGrupoEmpresarial = idGrupoEmpresarial;
        this.idGpoEmpresarialContrato = idGpoEmpresarialContrato;
        this.idEjec = idEjecutivo;
        this.statusEjecutivo = statusEjecutivo;
        this.idBloqueo = idBloqueo;
    }
    
    /**
     * Constructor que inicializa todas las variables de instancia.
     *
     * @param noCuenta El n&uacute;mero de contrato sica.
     * @param idContrato El n&uacute;mero de contrato corto.
     * @param nombreCorto El nombre corto.
     * @param idBloqueo El status del Bloqueo.
     * @param descripcion El status del Bloqueo.
     * @param fechaUltMod La fecha de &uacute;ltima modificacion.
     * @param usuarioUltMod El usuario de &uacute;ltima modificacion.
     */
    public ClienteContratoDto(String noCuenta, Integer idContrato, String nombreCorto,
                              Integer idBloqueo,String descripcion, String fechaUltMod,String usuarioUltMod) {
        this();
        this.noCuenta = noCuenta;
        this.idContrato = idContrato;
        this.nombreCorto = nombreCorto;
        this.idBloqueo = idBloqueo;
        this.descripcion = descripcion;
        this.fechaUltMod = fechaUltMod;
        this.usuarioUltMod = usuarioUltMod;
    }
    
    /**
     * Constructor que inicializa todas las variables de instancia.
     *
     * @param noCuenta El n&uacute;mero de contrato sica.
     * @param idContrato El n&uacute;mero de contrato corto.
     * @param nombreCorto El nombre corto.
     * @param idBloqueo El status del Bloqueo.
     * @param descripcion El status del Bloqueo.
     * @param fechaUltMod La fecha de &uacute;ltima modificacion.
     * @param usuarioUltMod El usuario de &uacute;ltima modificacion.
     * @param sic El SIC del cliente
     */
    public ClienteContratoDto(String noCuenta, Integer idContrato, String nombreCorto,
                              Integer idBloqueo,String descripcion, String fechaUltMod,
                              String usuarioUltMod, String sic) {
        this();
        this.noCuenta = noCuenta;
        this.idContrato = idContrato;
        this.nombreCorto = nombreCorto;
        this.idBloqueo = idBloqueo;
        this.descripcion = descripcion;
        this.fechaUltMod = fechaUltMod;
        this.usuarioUltMod = usuarioUltMod;
        this.sic = sic;
    }

    /**
     * Regresa el valor de noCuenta.
     *
     * @return String.
     */
    public String getNoCuenta() {
        return noCuenta;
    }

    /**
     * Regresa el valor de nombreCorto.
     *
     * @return String.
     */
    public String getNombreCorto() {
        return nombreCorto;
    }
    
    /**
     * Regresa el valor de tipoPersona.
     *
     * @return String.
     */
    public String getTipoPersona() {
        return tipoPersona;
    }

    /**
     * Regresa el valor de esGrupo.
     *
     * @return Boolean.
     */
    public Boolean getEsGrupo() {
        return esGrupo;
    }

    /**
     * Regresa el valor de idSector.
     *
     * @return Integer.
     */
    public Integer getIdSector() {
        return idSector;
    }

    /**
     * Regresa el valor de idPersona.
     *
     * @return Integer.
     */
    public Integer getIdPersona() {
        return idPersona;
    }

    /**
     * Regresa el valor de idGrupoEmpresarial.
     *
     * @return Integer.
     */
    public Integer getIdGrupoEmpresarial() {
        return idGrupoEmpresarial;
    }

    /**
     * Regresa el valor de idGpoEmpresarialContrato.
     *
     * @return Integer.
     */
    public Integer getIdGpoEmpresarialContrato() {
        return idGpoEmpresarialContrato;
    }
    
    /**
     * Regresa el valor de idBloqueo.
     *
     * @return Integer.
     */
    public Integer getIdBloqueo() {
        return idBloqueo;
    }
    
    /**
     * Regresa el valor de idBloqueo.
     *
     * @return Integer.
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Establece el valor de idGpoEmpresarialContrato.
     *
     * @param idGpoEmpresarialContrato El valor a asignar.
     */
    public void setIdGpoEmpresarialContrato(Integer idGpoEmpresarialContrato) {
        this.idGpoEmpresarialContrato = idGpoEmpresarialContrato;
    }

    /**
     * Regresa el valor de idEjec.
     *
     * @return Integer.
     */
    public Integer getIdEjec() {
        return idEjec;
    }

    /**
     * Regresa el valor de statusEjecutivo.
     *
     * @return String.
     */
    public String getStatusEjecutivo() {
        return statusEjecutivo;
    }

    /**
     * Establece el valor de statusEjecutivo.
     *
     * @param statusEjecutivo El valor a asignar.
     */
    public void setStatusEjecutivo(String statusEjecutivo) {
        this.statusEjecutivo = statusEjecutivo;
    }
    
	public Integer getIdContrato() {
		return idContrato;
	}

	public void setIdContrato(Integer idContrato) {
		this.idContrato = idContrato;
	}

	public String getFechaUltMod() {
		return fechaUltMod;
	}

	public void setFechaUltMod(String fechaUltMod) {
		this.fechaUltMod = fechaUltMod;
	}

	public String getUsuarioUltMod() {
		return usuarioUltMod;
	}

	public void setUsuarioUltMod(String usuarioUltMod) {
		this.usuarioUltMod = usuarioUltMod;
	}
	
	public String getSic() {
		return sic;
	}

	public void setSic(String sic) {
		this.sic = sic;
	}


    /**
     * Regresa una cadena con los nombres y valores de las variables de instancia.
     *
     * @return String.
     */
    public String toString() {
        return new ToStringBuilder(this).append("noCuenta", noCuenta).
                append("nombreCorto", nombreCorto).append("tipoPersona", tipoPersona).
                append("esGrupo", esGrupo).append("idSector", idSector).
                append("idPersona", idPersona).append("idGrupoEmpresarial", idGrupoEmpresarial).
                append("idEjec", idEjec).append("statusEjecutivo", statusEjecutivo).append("idBloqueo",idBloqueo).
                append("sic", sic).toString();
    }

    /**
     * El n&uacute;mero de contrato sica.
     */
    private String noCuenta;

    /**
     * El nombre del cliente.
     */
    private String nombreCorto;

    /**
     * 'PF' para Persona F&iacute;sica, 'PM' para Persona Moral.
     */
    private String tipoPersona;

    /**
     * Si es o no grupo empresarial.
     */
    private Boolean esGrupo;

    /**
     * El n&uacute;mero de sector econ&oacute;mico. 
     */
    private Integer idSector;

    /**
     * El identificador del cliente como persona de la BUP.
     */
    private Integer idPersona;

    /**
     * El n&uacute;mero de grupo empresarial. 
     */
    private Integer idGrupoEmpresarial;

    /**
     * El n&uacute;mero de grupo empresarial para el contrato sica.
     */
    private Integer idGpoEmpresarialContrato;

    /**
     * El identificador del ejecutivo asignado al cliente.
     */
    private Integer idEjec;

    /**
     * El status del Ejecutivo (VIG, CANCE, etc).
     */
    private String statusEjecutivo;
    
    /**
     * El identificador del bloqueo que presenta el contrato.
     */
    private Integer idBloqueo;
    
    /**
     * La descripcion del bloqueo que presenta el contrato.
     */
    private String descripcion;
    
    /**
     * El identificador del contrato corto.
     */
    private Integer idContrato;
    
    /**
     * La fecha de la última modificación.
     */
    private String fechaUltMod;
    
    /**
     * El usuario de última modificación.
     */
    private String usuarioUltMod;
    
    /**
     * No SIC del cliente
     */
    private String sic;

    /**
     * UID para serializaci&oacute;n.
     */
    private static final long serialVersionUID = -884481711227106812L;
}
