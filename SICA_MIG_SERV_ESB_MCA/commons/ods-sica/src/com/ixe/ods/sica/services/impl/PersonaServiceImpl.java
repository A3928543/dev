package com.ixe.ods.sica.services.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;

import com.ixe.bean.Constantes;
import com.ixe.bean.Usuario;
import com.ixe.bean.bup.Catalogo;
import com.ixe.bean.bup.CriterioBusqueda;
import com.ixe.bean.bup.Direccion;
import com.ixe.bean.bup.Persona;
import com.ixe.bean.bup.PersonaFisica;
import com.ixe.bean.bup.PersonaMoral;
import com.ixe.bean.bup.RelacionPersonas;
import com.ixe.contratacion.ContratacionException;
import com.ixe.ods.bup.model.Pais;
import com.ixe.ods.sica.SicaBusquedaPersonaService;
import com.ixe.ods.sica.SicaConsultaCatalogosService;
import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.SicaRegistroDireccionPersonaService;
import com.ixe.ods.sica.SicaRegistroPersonaService;
import com.ixe.ods.sica.SicaRegistroRelacionContratacionService;
import com.ixe.ods.sica.SicaValidationException;
import com.ixe.ods.sica.dao.PersonaExtranjeraDao;
import com.ixe.ods.sica.model.Deal;
import com.ixe.ods.sica.model.DealDetalle;
import com.ixe.ods.sica.model.ParametroSica;
import com.ixe.ods.sica.model.PersonaExtranjera;
import com.ixe.ods.sica.sdo.SicaServiceData;
import com.ixe.ods.sica.sdo.dto.PersonaDto;
import com.ixe.ods.sica.sdo.dto.RespuestaDto;
import com.ixe.ods.sica.services.GeneralMailSender;
import com.ixe.ods.sica.services.PersonaService;
import com.ixe.ods.sica.util.PersonaUtil;
import com.ixe.ods.sica.utils.DateUtils;

/**
 * The Class PersonaServiceImpl.
 */
public class PersonaServiceImpl implements PersonaService {
	
	/** The Constant log. */
	private static final Logger log = Logger.getLogger(PersonaServiceImpl.class);
	
	/** The Constant MSG_ERROR_CLIENTE_NO_EXISTE. */
	private static final String MSG_ERROR_CLIENTE_NO_EXISTE = "El cliente no existe.";
	
	/** The Constant PARAM_MODULO_CLIENTES_EMAIL_CONTRATOS. */
	private static final String PARAM_MODULO_CLIENTES_EMAIL_CONTRATOS = "PARAM_MD_CLIENTES_CORREO_CONTRATOS";
	
	/** The Constant PARAM_MODULO_CLIENTES_TIT_ERROR_DATOS. */
	private static final String PARAM_MODULO_CLIENTES_TIT_ERROR_DATOS = "PARAM_MD_CLIENTES_TIT_ERROR_DATOS";
	
	/** The Constant PARAM_MODULO_CLIENTES_EMAIL_NORMATIVO. */
	private static final String PARAM_MODULO_CLIENTES_EMAIL_NORMATIVO = "PARAM_MD_CLIENTES_CORREO_NORMATIVO";
	
	/** The Constant PARAM_MD_CLIENTES_CORREO_CONTRATOS_CC. */
	private static final String PARAM_MD_CLIENTES_CORREO_CONTRATOS_CC = "PARAM_MD_CLIENTES_CORREO_CONTRATOS_CC";
	
	/** The Constant PARAM_MD_CLIENTES_OPER_AMC. */
	private static final String PARAM_MD_CLIENTES_OPER_AMC = "PARAM_MD_CLIENTES_OPER_AMC";
	
	/** The Constant PARAM_MODULO_CLIENTES_OPER_PREFIX. */
	private static final String PARAM_MODULO_CLIENTES_OPER_PREFIX = "PARAM_MD_CLIENTES_OPER_";
	
	/** The Constant PARAM_MD_CLIENTES_CORREO_PROMOCION. */
	private static final String PARAM_MD_CLIENTES_CORREO_PROMOCION = "PARAM_MD_CLIENTES_CORREO_PROMOCION";
	
	/** The persona extranjera dao. */
	private PersonaExtranjeraDao personaExtranjeraDao;
	
	/** The busq persona service. */
	private SicaBusquedaPersonaService busqPersonaService;

	/** The reg persona service. */
	private SicaRegistroPersonaService regPersonaService;
	
	/** The reg direccion persona service. */
	private SicaRegistroDireccionPersonaService regDireccionPersonaService;
	
	/** The reg relacion service. */
	private SicaRegistroRelacionContratacionService regRelacionService;
	
	/** The service data. */
	private SicaServiceData serviceData;
	
	/** The catalogos service. */
	private SicaConsultaCatalogosService catalogosService;
	
	/** The general mail sender. */
	private GeneralMailSender generalMailSender;
	
	/**
	 * Buscar persona por id.
	 *
	 * @param idPersona the id persona
	 * @return the persona dto
	 */
	public PersonaDto buscarPersonaPorId(Integer idPersona) {
		PersonaDto personaDto = null;
		try {
			log.info("Buscando informacion de la persona: " + idPersona);
			Persona persona = this.busqPersonaService.buscaPersonaPorId(idPersona.intValue());
			if (persona != null) {
				personaDto = new PersonaDto(persona);
				Date fecha = null;
				if (persona instanceof PersonaFisica) {
					com.ixe.ods.bup.model.PersonaFisica pf = 
							this.getServiceData().findPersonaFisicaByIdPersona(idPersona);
					if (pf != null) {
						if (pf.getFechaNacimiento() != null) {
							fecha = new Date(pf.getFechaNacimiento().getTime());
							personaDto.setFecha(fecha);
						}
						if (pf.getPaisNacimiento() != null) {
							personaDto.setPaisNacimiento(
									this.creaPaisNacimiento(pf.getPaisNacimiento()));
						}
						if (pf.getVerificaFe() != null) {
							persona.setVerificaFE(pf.getVerificaFe().intValue());
						}
					}
				}
				else if (persona instanceof PersonaMoral) {
					com.ixe.ods.bup.model.PersonaMoral pm = 
							this.getServiceData().findPersonaMoralByIdPersona(idPersona);
					if (pm != null) {
						if (pm.getVerificaFe() != null) {
							persona.setVerificaFE(pm.getVerificaFe().intValue());
						}
						if (pm.getFechaConsitucion() != null) {
							fecha = new Date(pm.getFechaConsitucion().getTime());
							personaDto.setFecha(fecha);
						}
					}
				}
				personaDto.setFecha(fecha);
				Catalogo giro = this.buscarGiroPorActividad(persona.getIdActividad());
				personaDto.setActividadGiro(giro);
				if (personaDto.isNacionalidadExtranjera()) {
					PersonaExtranjera ext = this.personaExtranjeraDao.buscarPorId(idPersona);
					if (ext != null) {
						personaDto.setIdExtranjero(ext.getIdExtranjero());
					}
				}
			}
			else {
				throw new SicaException("No se encontr\u00f3 informaci\u00f3n registrada de la " + 
											"persona solicitada.");
			}
		}
		catch (Exception ex) {
			log.error("Exception en buscarPersonaPorId()", ex);
			if (ex instanceof ContratacionException) {
				throw new SicaException(ex.getMessage(), ex);
			}
			else {
				throw new SicaException("Error al consultar la informaci\u00f3n de la persona. ", ex);
			}
		}
		
		return personaDto;
	}
		
	/**
	 * Checks if is valida informacion general persona.
	 *
	 * @param idPersona the id persona
	 * @param oper the oper
	 * @return true, if is valida informacion general persona
	 */
	public boolean isValidaInformacionGeneralPersona(Integer idPersona, String oper) {
		PersonaDto persona = this.buscarPersonaPorId(idPersona);
		if (persona != null) {
			String errores = this.validaInformacionGeneralPersonaCorrecta(persona);
			if (StringUtils.isNotEmpty(errores)) {
				if (StringUtils.isNotEmpty(oper)) {
					String tmp = persona.getPersona().getNombreCorto() + ": " + errores;
					log.debug("Error para email: " + tmp);
					this.enviaCorreo(oper, tmp);
				}
				throw new SicaValidationException(errores);
			}
		}
		else {
			throw new SicaValidationException(MSG_ERROR_CLIENTE_NO_EXISTE);
		}
		
		return true;
	}
	
	/**
	 * Checks if is valida informacion general persona.
	 *
	 * @param deal the deal
	 * @return true, if is valida informacion general persona
	 */
	public boolean isValidaInformacionGeneralPersona(Deal deal) {
		boolean valida = true;
		Integer idPersona = deal.getCliente().getIdPersona();
		log.debug("Validando la informacion de la persona: " + idPersona);
		PersonaDto persona = this.buscarPersonaPorId(idPersona);
		if (persona != null) {
			String errores = this.validaInformacionGeneralPersonaCorrecta(persona);
			if (StringUtils.isNotEmpty(errores) && 
					dealTieneUnaTransferenciaAlExtranjero(deal)) {
				Map param = new HashMap();
				param.put("template", "DatosFaltantesClienteTemplate.vm");
				param.put("deal", deal);
				param.put("datosFaltantes", errores);
				Map parametros = this.cargaParametros();
				if (parametros.containsKey(PARAM_MODULO_CLIENTES_EMAIL_NORMATIVO)) {
					param.put("from", parametros.get(PARAM_MODULO_CLIENTES_EMAIL_NORMATIVO));
				}
				if (parametros.containsKey(PARAM_MD_CLIENTES_CORREO_PROMOCION)) {
					param.put("to", parametros.get(PARAM_MD_CLIENTES_CORREO_PROMOCION));
				}
				if (parametros.containsKey(PARAM_MD_CLIENTES_CORREO_CONTRATOS_CC)) {
					param.put("cc", parametros.get(PARAM_MD_CLIENTES_CORREO_CONTRATOS_CC));
				}
				if (parametros.containsKey(PARAM_MODULO_CLIENTES_TIT_ERROR_DATOS)) {
					param.put("subject", parametros.get(PARAM_MODULO_CLIENTES_TIT_ERROR_DATOS));
				}
				this.generalMailSender.enviarMail(param);
				valida = false;
			}
		}
		else {
			throw new SicaValidationException(MSG_ERROR_CLIENTE_NO_EXISTE);
		}
		
		
		return valida;
	}
	
	/**
     * Tiene una transferencia al extranjero.
     *
     * @param deal the deal
     * @return true, if successful
     */
    private boolean dealTieneUnaTransferenciaAlExtranjero(Deal deal) {
    	boolean tiene = false;
    	if (deal != null && 
    			deal.getCliente() != null && 
    				deal.getEntregamos() != null && 
    					!deal.getEntregamos().isEmpty()) {
    		for (Iterator it = deal.getEntregamos().iterator(); it.hasNext();) {
                DealDetalle det = (DealDetalle) it.next();
                if (com.ixe.ods.sica.Constantes.TRANSFERENCIA.
                		equals(det.getClaveFormaLiquidacion())) {
                	tiene = true;
                	break;
                }
            }
    	}
    	
    	return tiene;
    }
	
	/**
	 * Checks if is valida informacion general persona.
	 *
	 * @param noCuenta the no cuenta
	 * @return true, if is valida informacion general persona
	 */
	public boolean isValidaInformacionGeneralPersona(String noCuenta) {
		log.debug("Numero de cuenta: " + noCuenta);
		try {
			com.ixe.ods.bup.model.Persona odsPersona = 
					getServiceData().findPersonaByContratoSica(noCuenta);
		 	if (odsPersona != null) {
		 		this.isValidaInformacionGeneralPersona(odsPersona.getIdPersona(), null);
		 	}
		 	else {
		 		throw new SicaValidationException(MSG_ERROR_CLIENTE_NO_EXISTE);
		 	}
		}
		catch (DataAccessException ex) {
			log.error("DataAccessException en isValidaInformacionGeneralPersona", ex);
			throw new SicaException("Error al validar la informaci\u00f3n general de la persona.", ex);
		}
	 	
	 	return true;
	}
	
	/**
	 * Crea pais nacimiento.
	 *
	 * @param pais the pais
	 * @return the catalogo
	 */
	private Catalogo creaPaisNacimiento(Pais pais) {
		Catalogo catalogo = null;
		if (pais != null) {
			catalogo = new Catalogo();
			catalogo.setId(pais.getIdPais());
			catalogo.setDescripcion(pais.getDescripcion());
		}
		
		return catalogo;
	}
	
	/**
	 * Buscar giro por actividad.
	 *
	 * @param idActividad the id actividad
	 * @return the catalogo
	 */
	private Catalogo buscarGiroPorActividad(String idActividad) {
		Catalogo giro = null;
		log.debug("Actividad Economica: " + idActividad);
		if (StringUtils.isNotEmpty(idActividad)) {
			Integer idGiro = 
					this.getServiceData().
						findGiroEconomicoAsociadoBupActividadEconomica(idActividad);
			log.debug("Giro Economico: " + idGiro);
			if (idGiro != null) {
				LinkedHashMap catGiro = getCatalogosService().obtenCatGiroEconomico();
				for (Iterator i = catGiro.keySet().iterator(); i.hasNext();) {
			        String key = (String) i.next();
			        if (String.valueOf(idGiro).equals(key)) {
			        	giro = new Catalogo();
			        	giro.setId(key);
			        	giro.setDescripcion((String)catGiro.get(key));
			        	break;
			        }
			    }
			}
		}
		
		return giro;
	}
	
	/**
	 * Checks if is valida informacion general persona.
	 *
	 * @param idPersona the id persona
	 * @return the respuesta dto
	 */
	public RespuestaDto isValidaInformacionGeneralPersona(Integer idPersona) {
		RespuestaDto resDto = new RespuestaDto();
		PersonaDto persona = this.buscarPersonaPorId(idPersona);
		if (persona != null) {
			String errores = this.validaInformacionGeneralPersonaCorrecta(persona);
			if (StringUtils.isNotEmpty(errores)) {
				log.debug("Errores: " + errores);
				resDto.setCodigo(RespuestaDto.ERROR_VALIDACION);
				resDto.setMensaje(errores);
			}
			resDto.setObjecto(persona);
		}
		else {
			resDto.setCodigo(RespuestaDto.ERROR_NEGOCIO);
			resDto.setMensaje(MSG_ERROR_CLIENTE_NO_EXISTE);
		}
		
		return resDto;
	}
	
	/**
	 * Valida informacion general persona correcta.
	 *
	 * @param persona the persona
	 * @return the string
	 */
	private String validaInformacionGeneralPersonaCorrecta(PersonaDto persona) {
		PersonaUtil personaUtil = 
				new PersonaUtil(persona, DateUtils.FORMATO_FECHA_CON_DIAGONAL);
		return personaUtil.validaInformacionGeneralPersona();
	}
	
	/**
	 * Checks if is valida informacion general persona.
	 *
	 * @param perDto the per dto
	 * @return true, if is valida informacion general persona
	 */
	private boolean isValidaInformacionGeneralPersona(PersonaDto perDto) {
		String errores = this.validaInformacionGeneralPersonaCorrecta(perDto);
		if (StringUtils.isNotEmpty(errores)) {
			throw new SicaValidationException(errores);
		}
		
		return true;
	}

	/**
	 * Guardar persona.
	 *
	 * @param perDto the per dto
	 * @param idPersonaUser the id persona user
	 * @return the persona
	 */
	public Persona guardarPersona(PersonaDto perDto, Integer idPersonaUser) {
		Persona persona = null;
		try {
			log.debug("Id Persona recibida: " + perDto.getPersona().getIdPersona());
			if (this.isValidaInformacionGeneralPersona(perDto) && this.isCurpValido(perDto) 
					&& this.isIdentificadorExtranjeroValido(perDto)) {
				Usuario usuario = 
						this.getBusqPersonaService().obtenEmpleado(idPersonaUser.intValue());
				if (perDto.getPersona().getIdPersona() == 0) {//Alta
					persona = this.registrarPersona(perDto.getPersona(), usuario);	
					perDto.setPersona(persona);
				}
				else {
					if (perDto.isPersonaMoral()) {
						this.actualizaPersonaMoral(perDto, idPersonaUser);
					}
				}
				if (perDto.isPersonaFisica()) {//Para almacenar el total de informacion.
					this.actualizaPersonaFisica(perDto, idPersonaUser);
				}
				this.guardarExtranjero(perDto);
				this.guardarDireccion(perDto, usuario);
				this.registraApoderado(perDto, usuario);
				this.registrarInformacionRegulatoria(perDto);
				persona = perDto.getPersona();
			}
		}
		catch (DataAccessException ex) {
			log.error("DataAccessException en guardarPersona() ", ex);
			throw new SicaException("Error al la informaci\u00f3n de la persona.", ex);
		}
		
		return persona;
	}
	
	/**
	 * Registrar persona.
	 *
	 * @param persona the persona
	 * @param usuario the usuario
	 * @return the persona
	 */
	private Persona registrarPersona(Persona persona, Usuario usuario) {
		Persona result = null;
		try {
			result = this.regPersonaService.registraPersona(persona, usuario);
			log.debug("Se registro al persona: " + result.getIdPersona());
		}
		catch (ContratacionException ex) {
			log.error("ContratacionException en registrarPersona() ", ex);
			throw new SicaException("Error al almacenar la informaci\u00f3n de la persona: " + 
										ex.getMessage(), ex);
		}
		
		return result;
	}
	
	/**
	 * Guardar direccion.
	 *
	 * @param perDto the per dto
	 * @param usuario the usuario
	 */
	private void guardarDireccion(PersonaDto perDto, Usuario usuario) {
		Persona persona = perDto.getPersona();
		Direccion direccion = perDto.getDireccion(); 
		try {
			if (direccion != null) {
				this.getRegDireccionPersonaService().
					registraDireccion(persona, direccion, usuario);
			}
		}
		catch (ContratacionException ex) {
			log.error("ContratacionException en guardarDireccion() ", ex);
			throw new SicaException("Error al registrar la Direcci\u00f3n: " + ex.getMessage());
		}
	}
	
	/**
	 * Registra apoderado.
	 *
	 * @param personaDto the persona dto
	 * @param usuario the usuario
	 */
	private void registraApoderado(PersonaDto personaDto, Usuario usuario) {
		try {
			if (personaDto.getApoderado() != null && personaDto.isPersonaMoral()) {
				Persona perReg = this.registrarPersona(personaDto.getApoderado(), usuario);
				RelacionPersonas relacion = new RelacionPersonas();
	        	relacion.setIdPersona1(personaDto.getPersona().getIdPersona());
	        	relacion.setIdPersona2(perReg.getIdPersona());  //DESCRIPCION_ATRIBUTO_APODERADO
	        	relacion.setIdTipoRelacion("6"); 
	        	relacion.setStatus(Constantes.STATUS_RELACION_VIGENTE);
	        	this.regRelacionService.registraRelacion(relacion, usuario);
			}
		}
		catch (ContratacionException ex) {
			log.error("ContratacionException en registraApoderado() ", ex);
			String mensaje = "Error al registrar la relaci\u00f3n entre " + 
							 "el cliente y su apoderado legal: " + ex.getMessage();
			throw new SicaException(mensaje, ex);
		}
	}
	
	/**
	 * Registrar informacion regulatoria.
	 *
	 * @param perDto the per dto
	 */
	private void registrarInformacionRegulatoria(PersonaDto perDto) {
		Integer idPersona = new Integer(perDto.getPersona().getIdPersona());
		try {
			if (perDto.isPersonaMoral()) {
				if (perDto.getRegulatorioPerfil() != null) {
					perDto.getRegulatorioPerfil().setIdPersona(idPersona);
					this.serviceData.store(perDto.getRegulatorioPerfil());
				}
				if (perDto.getRegulatorioDatosPM() != null) {
					perDto.getRegulatorioDatosPM().setIdPersona(idPersona);
					this.serviceData.store(perDto.getRegulatorioDatosPM());
				}
				if (perDto.getRegulatorioInstitucion() != null) {
					perDto.getRegulatorioInstitucion().setIdPersona(idPersona);
					this.serviceData.store(perDto.getRegulatorioInstitucion());
				}
			}
		}
		catch (DataAccessException ex) {
			log.error("DataAccessException en registrarInformacionRegulatoria() ", ex);
			String msj = "Error al almacenar la informaci\u00f3n regulatoria de la persona.";
			throw new SicaException(msj, ex);
		}
	}
	
	/**
	 * Checks if is identificador extranjero valido.
	 *
	 * @param perDto the per dto
	 * @return true, if is identificador extranjero valido
	 */
	private boolean isIdentificadorExtranjeroValido(PersonaDto perDto) {
		if (perDto.isNacionalidadExtranjera()) {
			log.debug("IdExtranjero: " + perDto.getIdExtranjero());
			PersonaExtranjera persExt = 
					this.personaExtranjeraDao.buscarPorIdExtranjero(perDto.getIdExtranjero());
			if (persExt != null) {
				Integer idPersona = new Integer(perDto.getPersona().getIdPersona());
				if (perDto.isPersonaNueva() || !idPersona.equals(persExt.getIdPersona())) {
					throw new SicaValidationException("El identificador de extranjero ya se " + 
																"encuentra registrado.");
				}
			}
		}
		
		return true;		
	}
	
	/**
	 * Checks if is curp valido.
	 *
	 * @param perDto the per dto
	 * @return true, if is curp valido
	 */
	private boolean isCurpValido(PersonaDto perDto) {
		boolean curpValido = true;
		if (perDto.isPersonaFisica() && perDto.isNacionalidadMexicana()) {
			String curp = ((PersonaFisica) perDto.getPersona()).getCurp();
			int idPersona = perDto.getPersona().getIdPersona();
			CriterioBusqueda criterio = new CriterioBusqueda();
			criterio.setCurp(curp);
			List personas = this.busqPersonaService.buscaPersona(criterio);
			if (personas != null && !personas.isEmpty()) {
				log.debug("Personas encontradas con CURP(" + curp + " ): " + personas.size());
				if (idPersona > 0) {//Persona existente
					boolean encontrado = false;
					for (int i = 0; i < personas.size(); i ++) {
						PersonaFisica pf = (PersonaFisica) personas.get(i);
						if (pf.getIdPersona() == idPersona) {
							encontrado = true;
							break;
						}
					}
					curpValido = encontrado;
				}
				else {
					curpValido = false;
				}	
			}
			if (!curpValido) {
				throw new SicaValidationException("La clave CURP ya se encuentra registrada.");
			}
		}
	
		return curpValido;
			
	}
	
	/**
	 * Actualizar persona.
	 *
	 * @param dto the dto
	 * @param idPersonaUser the id persona user
	 * @return true, if successful
	 */
	public boolean actualizarPersona(PersonaDto dto, Integer idPersonaUser) {
		try {
			if (this.isValidaInformacionGeneralPersona(dto) && this.isCurpValido(dto) &&
					this.isIdentificadorExtranjeroValido(dto)) {
				boolean actualizarOk = false;
				if (dto.isPersonaFisica()) {
					actualizarOk = this.actualizaPersonaFisica(dto, idPersonaUser);
				}
				else {
					actualizarOk = this.actualizaPersonaMoral(dto, idPersonaUser);
				}
				log.debug("actualizarOk: " + actualizarOk);
				if (actualizarOk) {
					this.guardarExtranjero(dto);
				}
				else {
					throw new SicaException("DataAccessException en " +
									"actualiizar los datos de la persona.");
				}
			}
		}
		catch (DataAccessException ex) {
			log.error("DataAccessException en actualizarPersona: ", ex);
			throw new SicaException("Ocurri\u00f3 un error al actualizar los datos del cliente. ", ex);
		}
		catch (SicaValidationException ex) {
			throw ex; 
		}
		catch (Exception ex) {
			log.error("Exception en actualizarPersona()", ex);
			if (ex instanceof ContratacionException) {
				throw new SicaException(ex.getMessage(), ex);
			}
			else {
				throw new SicaException("Error al actualizar la informaci\u00f3n del cliente. ", ex);
			}
		}
		
		return true;
	}
	
	/**
	 * Actualiza persona fisica.
	 *
	 * @param datos the datos
	 * @param idPersonaUser the id persona user
	 * @return true, if successful
	 */
	private boolean actualizaPersonaFisica(PersonaDto datos, Integer idPersonaUser) {
		Persona persona = datos.getPersona();
		Map param = this.creaParametrosPersona(persona, idPersonaUser);
		PersonaFisica pf = (PersonaFisica) persona;
		param.put("nombre", pf.getNombre());
		param.put("paterno", pf.getPaterno());
		param.put("materno", pf.getMaterno());
		param.put("fechaNacimiento", pf.getFechaNacimiento());
		if (datos.getPaisNacimiento() != null) {
			param.put("paisNacimiento", datos.getPaisNacimiento().getId());
		}
		param.put("fechaNacimiento", pf.getFechaNacimiento());
		param.put("lugarNacimiento", pf.getLugarNacimiento());
		param.put("estadoCivil", pf.getIdEstadoCivil());
		param.put("sexo", pf.getSexo());
		if (datos.isNacionalidadMexicana()) {
			param.put("curp", pf.getCurp());
		}
		param.put("tipoIdentifica", pf.getIdTipoIdentifica());
		param.put("noIdentificacion", pf.getNoIdentificacion());
		log.debug("param: " + param);
		return this.getServiceData().actualizarPersonaFisica(param);
	}

	/**
	 * Actualiza persona moral.
	 *
	 * @param datos the datos
	 * @param idPersonaUser the id persona user
	 * @return true, if successful
	 */
	private boolean actualizaPersonaMoral(PersonaDto datos, Integer idPersonaUser) {
		Persona persona = datos.getPersona();
		Map param = this.creaParametrosPersona(persona, idPersonaUser);
		PersonaMoral pm = (PersonaMoral) persona;
		param.put("razonSocial", pm.getRazonSocial());
		param.put("fechaNacimiento", pm.getFechaConstitucion());
		log.debug("param: " + param);
		return this.getServiceData().actualizarPersonaMoral(param);
	}
	
	/**
	 * Crea parametros persona.
	 *
	 * @param persona the persona
	 * @param idPersonaUser the id persona user
	 * @return the map
	 */
	private Map creaParametrosPersona(Persona persona, Integer idPersonaUser) {
		Map param = new HashMap();
		param.put("idPersona", new Integer(persona.getIdPersona()));
		param.put("usuario", idPersonaUser);
		param.put("sectorEconomico", String.valueOf(persona.getIdSector()));
		param.put("actividadEconomica", persona.getIdActividad());
		param.put("giroEconomico", persona.getActividadGiro());
		param.put("nacionalidad", persona.getIdPais());
		param.put("rfc", persona.getRfc());
		param.put("facturacionElectronica", new Integer(persona.getVerificaFE()));
		
		return param;
	}
	
	/**
	 * Guardar extranjero.
	 *
	 * @param dto the dto
	 */
	private void guardarExtranjero(PersonaDto dto) {
		PersonaExtranjera ext = null;
		if (dto.isNacionalidadExtranjera()) {
			Integer idPersona = new Integer(dto.getPersona().getIdPersona());
			log.debug("Guardar Id Extranjero: " + dto.getIdExtranjero());
			PersonaExtranjera tmp = 
					this.personaExtranjeraDao.buscarPorId(idPersona);
			ext = new PersonaExtranjera(idPersona, dto.getIdExtranjero());
			if (tmp != null) {
				this.personaExtranjeraDao.actualizar(ext);
				//this.personaExtranjeraDao.removeEntidadSesion(ext);
			}
			else {
				this.personaExtranjeraDao.registrar(ext);
			}
		}
	}
	
	/**
	 * Envia correo.
	 *
	 * @param oper the oper
	 * @param error the error
	 */
	public void enviaCorreo(String oper, String error) {
		log.debug("oper: " + oper);
		log.debug("error: " + error);
		try {
			Map parametros = this.cargaParametros();
			if (!parametros.isEmpty()) {
				String de = (String) parametros.get(PARAM_MODULO_CLIENTES_EMAIL_NORMATIVO);
				String[] para = 
						new String[]{(String) parametros.get(PARAM_MODULO_CLIENTES_EMAIL_CONTRATOS)};
				String titulo = (String) parametros.get(PARAM_MODULO_CLIENTES_TIT_ERROR_DATOS);
				String operParam = PARAM_MODULO_CLIENTES_OPER_PREFIX + oper;
				String mensaje = (String) parametros.get(operParam) + error;
				this.generalMailSender.enviarMail(de, para, null, titulo, mensaje);
				log.debug("Se envio correo correctamente.");
			}
		}
		catch (Exception ex) {
			log.error("Exception en enviaCorreo() ", ex);
		}
	}
	
	/**
	 * Carga parametros.
	 *
	 * @return the map
	 */
	private Map cargaParametros() {
		Map parametros = new HashMap();
		ParametroSica temp = 
				this.getServiceData().findParametro(PARAM_MODULO_CLIENTES_EMAIL_NORMATIVO);
		if (temp != null) {
			parametros.put(PARAM_MODULO_CLIENTES_EMAIL_NORMATIVO, temp.getValor());
		}
		temp = this.getServiceData().findParametro(PARAM_MODULO_CLIENTES_EMAIL_CONTRATOS);
		if (temp != null) {
			parametros.put(PARAM_MODULO_CLIENTES_EMAIL_CONTRATOS, temp.getValor());
		}
		temp = this.getServiceData().findParametro(PARAM_MD_CLIENTES_CORREO_CONTRATOS_CC);
		if (temp != null) {
			parametros.put(PARAM_MD_CLIENTES_CORREO_CONTRATOS_CC, temp.getValor());
		}
		temp = this.getServiceData().findParametro(PARAM_MODULO_CLIENTES_TIT_ERROR_DATOS);
		if (temp != null) {
			parametros.put(PARAM_MODULO_CLIENTES_TIT_ERROR_DATOS, temp.getValor());
		}
		temp = this.getServiceData().findParametro(PARAM_MD_CLIENTES_OPER_AMC);
		if (temp != null) {
			parametros.put(PARAM_MD_CLIENTES_OPER_AMC, temp.getValor());
		}
		temp = this.getServiceData().findParametro(PARAM_MD_CLIENTES_CORREO_PROMOCION);
		if (temp != null) {
			parametros.put(PARAM_MD_CLIENTES_CORREO_PROMOCION, temp.getValor());
		}
		log.debug("Parametros: " + parametros);
			
		return parametros;
	}

	/**
	 * Gets the persona extranjera dao.
	 *
	 * @return the persona extranjera dao
	 */
	public PersonaExtranjeraDao getPersonaExtranjeraDao() {
		return personaExtranjeraDao;
	}

	/**
	 * Sets the persona extranjera dao.
	 *
	 * @param personaExtranjeraDao the new persona extranjera dao
	 */
	public void setPersonaExtranjeraDao(PersonaExtranjeraDao personaExtranjeraDao) {
		this.personaExtranjeraDao = personaExtranjeraDao;
	}

	/**
	 * Gets the busq persona service.
	 *
	 * @return the busq persona service
	 */
	public SicaBusquedaPersonaService getBusqPersonaService() {
		return busqPersonaService;
	}

	/**
	 * Sets the busq persona service.
	 *
	 * @param busqPersonaService the new busq persona service
	 */
	public void setBusqPersonaService(SicaBusquedaPersonaService busqPersonaService) {
		this.busqPersonaService = busqPersonaService;
	}

	/**
	 * Gets the reg persona service.
	 *
	 * @return the reg persona service
	 */
	public SicaRegistroPersonaService getRegPersonaService() {
		return regPersonaService;
	}

	/**
	 * Sets the reg persona service.
	 *
	 * @param regPersonaService the new reg persona service
	 */
	public void setRegPersonaService(SicaRegistroPersonaService regPersonaService) {
		this.regPersonaService = regPersonaService;
	}

	/**
	 * Gets the reg direccion persona service.
	 *
	 * @return the reg direccion persona service
	 */
	public SicaRegistroDireccionPersonaService getRegDireccionPersonaService() {
		return regDireccionPersonaService;
	}

	/**
	 * Sets the reg direccion persona service.
	 *
	 * @param regDireccionPersonaService the new reg direccion persona service
	 */
	public void setRegDireccionPersonaService(
			SicaRegistroDireccionPersonaService regDireccionPersonaService) {
		this.regDireccionPersonaService = regDireccionPersonaService;
	}

	/**
	 * Gets the reg relacion service.
	 *
	 * @return the reg relacion service
	 */
	public SicaRegistroRelacionContratacionService getRegRelacionService() {
		return regRelacionService;
	}

	/**
	 * Sets the reg relacion service.
	 *
	 * @param regRelacionService the new reg relacion service
	 */
	public void setRegRelacionService(
			SicaRegistroRelacionContratacionService regRelacionService) {
		this.regRelacionService = regRelacionService;
	}

	/**
	 * Gets the service data.
	 *
	 * @return the service data
	 */
	public SicaServiceData getServiceData() {
		return serviceData;
	}

	/**
	 * Sets the service data.
	 *
	 * @param serviceData the new service data
	 */
	public void setServiceData(SicaServiceData serviceData) {
		this.serviceData = serviceData;
	}

	/**
	 * Gets the catalogos service.
	 *
	 * @return the catalogos service
	 */
	public SicaConsultaCatalogosService getCatalogosService() {
		return catalogosService;
	}

	/**
	 * Sets the catalogos service.
	 *
	 * @param catalogosService the new catalogos service
	 */
	public void setCatalogosService(SicaConsultaCatalogosService catalogosService) {
		this.catalogosService = catalogosService;
	}

	/**
	 * Gets the general mail sender.
	 *
	 * @return the general mail sender
	 */
	public GeneralMailSender getGeneralMailSender() {
		return generalMailSender;
	}

	/**
	 * Sets the general mail sender.
	 *
	 * @param generalMailSender the new general mail sender
	 */
	public void setGeneralMailSender(GeneralMailSender generalMailSender) {
		this.generalMailSender = generalMailSender;
	}

}
