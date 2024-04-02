package com.ixe.ods.sica.dto;

/**
 * Informacion del cliente mas datos adicionales del promotor asignado
 * 
 * @author Cesar Jeronimo Gomez
 */
public class ClienteContratoInfoPromotorDto extends ClienteContratoDto {

	private static final long serialVersionUID = -8164141798322221744L;
	
	/** Almacena el nombre del ejecutivo asociado al cliente */
	private String nombreEjecutivo;
	
	/** Almacena la clave de usuario del ejecutivo asociado al cliente */
	private String claveUsuarioEjecutivo;
	
	/** Almacena el contrato corto asociado al contrato sica del cliente */
	private Integer contratoCorto;

	public ClienteContratoInfoPromotorDto() {
		super();
	}

	/**
	 * Full constructor
	 * 
	 * @param noCuenta
	 * @param nombreCorto
	 * @param tipoPersona
	 * @param idPersona
	 * @param idEjecutivo
	 * @param statusEjecutivo
	 * @param nombreEjecutivo
	 * @param claveUsuarioEjecutivo
	 * @param contratoCorto
	 */
	public ClienteContratoInfoPromotorDto(
		String noCuenta, 
		String nombreCorto,
		String tipoPersona, 
		Integer idPersona,  
		Integer idEjecutivo, 
		String statusEjecutivo,
		String nombreEjecutivo,
		String claveUsuarioEjecutivo,
		Integer contratoCorto,
		Integer idBloqueo
	) {
		super(
			noCuenta, 
			nombreCorto, 
			tipoPersona, 
			null, //esGrupo, 
			null, //idSector, 
			idPersona, 
			null, //idGrupoEmpresarial, 
			idEjecutivo, 
			null, //idGpoEmpresarialContrato, 
			statusEjecutivo,
			idBloqueo
		);
		this.nombreEjecutivo = nombreEjecutivo;
		this.claveUsuarioEjecutivo = claveUsuarioEjecutivo;
		this.contratoCorto = contratoCorto;
	}

	/**
	 * Retorna {@link #nombreEjecutivo}
	 * @return
	 */
	public String getNombreEjecutivo() {
		if(nombreEjecutivo == null) {
			nombreEjecutivo = "";
		}
		return nombreEjecutivo;
	}
	
	/**
	 * Retorna {@link #claveUsuarioEjecutivo}
	 * @return
	 */
	public String getClaveUsuarioEjecutivo() {
		if(claveUsuarioEjecutivo == null) {
			claveUsuarioEjecutivo = "";
		}
		return claveUsuarioEjecutivo;
	}
	
	/**
	 * Retorna {@link #contratoCorto}
	 * @return
	 */
	public Integer getContratoCorto() {
		return contratoCorto;
	}

	public String toString() {
		return "ClienteContratoInfoPromotorDto [" 
			+ "nombreEjecutivo=" + nombreEjecutivo 
			+ ", claveUsuarioEjecutivo=" + claveUsuarioEjecutivo 
			+ ", getNoCuenta()=" + getNoCuenta()
			+ ", getNombreCorto()=" + getNombreCorto()
			+ ", getTipoPersona()=" + getTipoPersona() 
			+ ", getIdPersona()=" + getIdPersona() 
			+ ", getIdEjec()=" + getIdEjec() 
			+ ", getStatusEjecutivo()=" + getStatusEjecutivo() 
			+ ", getContratoCorto()=" + getContratoCorto()
		+ "]";
	}

}
