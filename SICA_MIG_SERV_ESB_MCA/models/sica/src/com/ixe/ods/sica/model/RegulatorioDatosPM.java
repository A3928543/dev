package com.ixe.ods.sica.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Clase persistente para la tabla SC_REG_PM. 
 *
 * @hibernate.class table="SC_REG_PM"
 * proxy="com.ixe.ods.sica.model.RegulatorioDatosPM"
 * dynamic-update="true"
 */
public class RegulatorioDatosPM implements Serializable
{
	private static final long serialVersionUID = 1L;
	private Integer id;
	private Integer idPersona;
	private String contratoSica;
	private String claveBanxico;
    private String nombreContraparte;
    private RegulatorioSociedadMercantil sociedadMercantil;
    private RegulatorioTipoIdentificador tipoIdentificador;
	private String claveIdentificadorContraparte;
	private RegulatorioActividadEconomica actividadEconomica;
	private String claveLei;
	private RegulatorioSectorIndustrial sectorIndustrial;
	private RegulatorioPais paisResidenciaContraparte;
	private RegulatorioPais paisControlContraparte;
	private String claveLeiMatrizDirecta;
	private String claveLeiMatrizUltimaInstancia;
	private Date fechaContraparte;
	private Date fechaSistema;
	private Integer idPersonaUltMod;
	private String perfilMdd;
	
	public RegulatorioDatosPM() 
	{
		super();
	}
	//--------------------------------------------------------------------------
	/**
	 * Regresa el valor del id del registro.
	 *
	 * @hibernate.id generator-class="sequence"
	 * column="ID_REG_PM"
	 * unsaved-value="null"
	 * @hibernate.generator-param name="sequence"
	 * value="SC_REG_PM_SEQ"
	 * @return Integer.
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Fija el valor del id del registro.
	 *
	 * @param id El valor a asignar.
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	//--------------------------------------------------------------------------
	/**
     * Regresa el valor de idPersona.
     *
     * @hibernate.property column="ID_PERSONA"
     * @return Integer.
     */
    public Integer getIdPersona() {
        return idPersona;
    }

    /**
     * Fija el valor de idPersona.
     *
     * @param idPersona El valor a asignar.
     */
    public void setIdPersona(Integer idPersona) {
        this.idPersona = idPersona;
    }
	//--------------------------------------------------------------------------
	/**
     * Regresa el valor del contrato sica.
     *
     * @hibernate.property column="NO_CUENTA"
     * @return String.
     */
    public String getContratoSica() {
        return contratoSica;
    }

    /**
     * Fija el valor del contrato sica.
     *
     * @param contratoSica El valor a asignar.
     */
    public void setContratoSica(String contratoSica) {
        this.contratoSica = contratoSica;
    }
    //--------------------------------------------------------------------------
	/**
	 * Regresa el valor de la clave BANXICO.
	 * 
	 * @hibernate.property column="CLAVE_BANXICO"
	 * @return String.
	 */
	public String getClaveBanxico() {
		return claveBanxico;
	}
	
	/**
	 * Fija el valor de la clave BANXICO
	 * @param claveBanxico
	 */
	public void setClaveBanxico(String claveBanxico) {
		this.claveBanxico = claveBanxico;
	}
	//--------------------------------------------------------------------------
	/**
	 * Regresa el nombre de la contraparte.
	 * 
	 * @hibernate.property column="NOMBRE_CONTRAPARTE"
	 * @return String.
	 */
	public String getNombreContraparte() {
		return nombreContraparte;
	}
	
	/**
	 * Fija el nombre de la contraparte. 
	 * @param nombreContraparte
	 */
	public void setNombreContraparte(String nombreContraparte) {
		this.nombreContraparte = nombreContraparte;
	}
	//--------------------------------------------------------------------------
	/**
	 * Regresa el tipo de sociedad mercantil.
     * @hibernate.many-to-one column="TIPO_SOC_MERCANTIL"
     * cascade="none"
     * class="com.ixe.ods.sica.model.RegulatorioSociedadMercantil"
     * outer-join="auto"
     * unique="false"
     * @return RegulatorioSociedadMercantil.
     */
	public RegulatorioSociedadMercantil getSociedadMercantil() {
		return sociedadMercantil;
	}
	
	/**
	 * Fija el valor del tipo de sociedad mercantil. 
	 * @param sociedadMercantil
	 */
	public void setSociedadMercantil(RegulatorioSociedadMercantil sociedadMercantil) {
		this.sociedadMercantil = sociedadMercantil;
	}
	//--------------------------------------------------------------------------
	/**
	 * Regresa el tipo de identificador de la contraparte: RFC, TIN, Social Security, etc.
     * @hibernate.many-to-one column="TIPO_IDENT_CONTRAPARTE"
     * cascade="none"
     * class="com.ixe.ods.sica.model.RegulatorioTipoIdentificador"
     * outer-join="auto"
     * unique="false"
     * @return RegulatorioTipoIdentificador.
     */
	public RegulatorioTipoIdentificador getTipoIdentificador() {
		return tipoIdentificador;
	}
	
	/**
	 * Fija el valor del tipo de sociedad mercantil. 
	 * @param tipoIdentificador
	 */
	public void setTipoIdentificador(RegulatorioTipoIdentificador tipoIdentificador) {
		this.tipoIdentificador = tipoIdentificador;
	}
	//--------------------------------------------------------------------------
	/**
	 * Regresa el valor de la clave de identificacion de la contraparte.
	 * 
	 * @hibernate.property column="CLAVE_IDENT_CONTRAPARTE"
	 * @return String.
	 */
	public String getClaveIdentificadorContraparte() {
		return claveIdentificadorContraparte;
	}
	
	/**
	 * Fija el valor de la clave de identificacion de la contraparte. 
	 * @param claveIdentificadorContraparte
	 */
	public void setClaveIdentificadorContraparte(String claveIdentificadorContraparte) {
		this.claveIdentificadorContraparte = claveIdentificadorContraparte;
	}
	//--------------------------------------------------------------------------
	/**
	 * Regresa la actividad economica de la contraparte.
     * @hibernate.many-to-one column="ACTIVIDAD_ECONOMICA"
     * cascade="none"
     * class="com.ixe.ods.sica.model.RegulatorioActividadEconomica"
     * outer-join="auto"
     * unique="false"
     * @return RegulatorioActividadEconomica.
     */
	public RegulatorioActividadEconomica getActividadEconomica() {
		return actividadEconomica;
	}
	
	/**
	 * Fija la actividad economica de la contraparte. 
	 * @param actividadEconomica
	 */
	public void setActividadEconomica(RegulatorioActividadEconomica actividadEconomica) {
		this.actividadEconomica = actividadEconomica;
	}
	//--------------------------------------------------------------------------
	/**
	 * Regresa el valor de la clave LEI de la contraparte.
	 * 
	 * @hibernate.property column="CLAVE_LEI"
	 * @return String.
	 */
	public String getClaveLei() {
		return claveLei;
	}
	
	/**
	 * Fija el valor de la clave LEI de la contraparte. 
	 * @param claveLei
	 */
	public void setClaveLei(String claveLei) {
		this.claveLei = claveLei;
	}
	//--------------------------------------------------------------------------
	/**
	 * Regresa el sector industrial de la contraparte.
     * @hibernate.many-to-one column="SECTOR_INDUSTRIAL"
     * cascade="none"
     * class="com.ixe.ods.sica.model.RegulatorioSectorIndustrial"
     * outer-join="auto"
     * unique="false"
     * @return RegulatorioSectorIndustrial.
     */
	public RegulatorioSectorIndustrial getSectorIndustrial() {
		return sectorIndustrial;
	}
	
	/**
	 * Fija el sector industrial de la contraparte. 
	 * @param sectorIndustrial
	 */
	public void setSectorIndustrial(RegulatorioSectorIndustrial sectorIndustrial) {
		this.sectorIndustrial = sectorIndustrial;
	}
	//--------------------------------------------------------------------------
	/**
	 * Regresa el pais de residencia de la contraparte.
     * @hibernate.many-to-one column="PAIS_CONTRAPARTE"
     * cascade="none"
     * class="com.ixe.ods.sica.model.RegulatorioPais"
     * outer-join="auto"
     * unique="false"
     * @return RegulatorioPais.
     */
	public RegulatorioPais getPaisResidenciaContraparte() {
		return paisResidenciaContraparte;
	}
	
	/**
	 * Fija el pais de residencia de la contraparte de la contraparte. 
	 * @param paisResidenciaContraparte
	 */
	public void setPaisResidenciaContraparte(RegulatorioPais paisResidenciaContraparte) {
		this.paisResidenciaContraparte = paisResidenciaContraparte;
	}
	//--------------------------------------------------------------------------	
	/**
	 * Regresa el pais de residencia de quien tiene el control de la contraparte.
     * @hibernate.many-to-one column="PAIS_CONTROL_CONTRAPARTE"
     * cascade="none"
     * class="com.ixe.ods.sica.model.RegulatorioPais"
     * outer-join="auto"
     * unique="false"
     * @return RegulatorioPais.
     */
	public RegulatorioPais getPaisControlContraparte() {
		return paisControlContraparte;
	}
	
	/**
	 * Fija el pais de residencia de quien tiene el control de la contraparte. 
	 * @param paisControlContraparte
	 */
	public void setPaisControlContraparte(RegulatorioPais paisControlContraparte) {
		this.paisControlContraparte = paisControlContraparte;
	}
	//--------------------------------------------------------------------------
	/**
	 * Regresa el valor de la clave LEI de la matriz directa de la contraparte.
	 * 
	 * @hibernate.property column="LEI_MATRIZ_DIRECTA"
	 * @return String.
	 */
	public String getClaveLeiMatrizDirecta() {
		return claveLeiMatrizDirecta;
	}
	
	/**
	 * Fija el valor de la clave LEI de la matriz directa de la contraparte. 
	 * @param claveLeiMatrizDirecta
	 */
	public void setClaveLeiMatrizDirecta(String claveLeiMatrizDirecta) {
		this.claveLeiMatrizDirecta = claveLeiMatrizDirecta;
	}
	//--------------------------------------------------------------------------
	/**
	 * Regresa el valor de la clave LEI de la matriz en ultima instancia de la contraparte.
	 * 
	 * @hibernate.property column="LEI_ULT_MATRIZ"
	 * @return String.
	 */
	public String getClaveLeiMatrizUltimaInstancia() {
		return claveLeiMatrizUltimaInstancia;
	}
	
	/**
	 * Fija el valor de la clave LEI de la matriz en ultima instancia de la contraparte. 
	 * @param claveLeiMatrizUltimaInstancia
	 */
	public void setClaveLeiMatrizUltimaInstancia(String claveLeiMatrizUltimaInstancia) {
		this.claveLeiMatrizUltimaInstancia = claveLeiMatrizUltimaInstancia;
	}
	//--------------------------------------------------------------------------
	/**
     * Regresa el valor de la fecha capturada en el alta o modificación de la contraparte.
     *
     * @hibernate.property column="FECHA_CONTRAPARTE"
     * unique="false"
     * @return Date.
     */
    public Date getFechaContraparte() {
        return fechaContraparte;
    }

    /**
     * Fija el valor de la fecha capturada en el alta o modificación de la contraparte.
     *
     * @param fechaUltMod El valor a asignar.
     */
    public void setFechaContraparte(Date fechaContraparte) {
        this.fechaContraparte = fechaContraparte;
    }
  //--------------------------------------------------------------------------
    /**
     * Regresa el valor de la fecha del sistema de cuando se registraron los datos o se modificaron.
     *
     * @hibernate.property column="FECHA_ALTA"
     * unique="false"
     * @return Date.
     */
    public Date getFechaSistema() {
        return fechaSistema;
    }

    /**
     * Fija el valor de la fecha del sistema de cuando se registraron los datos o se modificaron.
     *
     * @param fechaSistema El valor a asignar.
     */
    public void setFechaSistema(Date fechaSistema) {
        this.fechaSistema = fechaSistema;
    }
  //--------------------------------------------------------------------------
    /**
	 * Regresa el idPersona que hizo la ultima modificacion de datos.
	 * 
	 * @hibernate.property column="USUARIO_ALTA"
	 * @return Integer.
	 */
    public Integer getIdPersonaUltimaMod()
    {
    	return idPersonaUltMod;
    }
    
    /**
	 * Fija el idPersona que hizo la ultima modificacion de datos
	 * @param idPersonaUltMod
	 */
    public void setIdPersonaUltimaMod(Integer idPersonaUltMod)
    {
    	this.idPersonaUltMod = idPersonaUltMod;
    } 
  //--------------------------------------------------------------------------
	/**
	 * Regresa el valor para determinar si la contraparte operara 1 millon de dolares americanos.
	 * 
	 * @hibernate.property column="PERFIL_MDD"
	 * @return String.
	 */
	public String getPerfilMdd() {
		return perfilMdd;
	}
	
	/**
	 * Fija el valor del perfil de cliente para operar 1 millon de dolares americanos
	 * @param perfilMdd
	 */
	public void setPerfilMdd(String perfilMdd) {
		this.perfilMdd = perfilMdd;
	}
}
