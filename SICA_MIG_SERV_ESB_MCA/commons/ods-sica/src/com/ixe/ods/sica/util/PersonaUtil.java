package com.ixe.ods.sica.util;

import java.text.MessageFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.ixe.bean.bup.PersonaFisica;
import com.ixe.bean.bup.PersonaMoral;
import com.ixe.ods.sica.sdo.dto.PersonaDto;
import com.ixe.ods.sica.utils.DateUtils;
import com.ixe.util.FormatoInvalidoException;
import com.ixe.util.Utilerias;

/**
 * The Class PersonaUtil.
 */
public class PersonaUtil {
	
	/** The Constant log. */
	private static final Logger log = Logger.getLogger(PersonaUtil.class);
	
	/** The Constant MSG_ERROR_TIPO_PERSONA. */
	private static final String MSG_ERROR_TIPO_PERSONA = "El tipo de persona es incorrecto.";
	
	/** The Constant MSG_ERROR_DATOS_FALTANTES_CLIENTE. */
	private static final String MSG_ERROR_DATOS_FALTANTES_CLIENTE = "Datos faltantes del " + 
			"cliente: ";
	
	/** The Constant MSG_ERROR_PERSONA_NULA. */
	private static final String MSG_ERROR_PERSONA_NULA = "Error al validar los datos " + 
			"de la persona.";
	
	/** The Constant MSG_ERROR_LONG_CURP. */
	private static final String MSG_ERROR_CURP_LONG = "La longitud de la clave CURP es " + 
			"incorrecta (18 posiciones).";
	
	/** The Constant MSG_ERROR_ID_EXT_LONG. */
	private static final String MSG_ERROR_ID_EXT_LONG = "La longitud del identificador " + 
			"de exranjero es incorrecto ({0} posiciones).";
	
	/** The Constant MSG_ERROR_CURP_PREFIJO. */
	private static final String MSG_ERROR_CURP_PREFIJO = "Las primeras 4 posiciones de la " +
			"clave CURP deben ser letras.";
	
	/** The Constant MSG_ERROR_ID_EXT_PREFIJO. */
	private static final String MSG_ERROR_ID_EXT_PREFIJO = "Las primeras {0} posiciones del " +
			"identificador de extranjero deben ser letras. ";
	
	/** The Constant MSG_ERROR_CURP_FECHA_NO_NUMERICA. */
	private static final String MSG_ERROR_CURP_FECHA_NO_NUMERICA = "De la posici\u00f3n " +
			"5 a 10 solo debe contener d\u00edgitos la clave CURP.";
	
	/** The Constant MSG_ERROR_ID_EXT_FECHA_NO_NUMERICA. */
	private static final String MSG_ERROR_ID_EXT_FECHA_NO_NUMERICA = "De la posici\u00f3n " +
			"{0} a {1} solo debe contener d\u00edgitos el identificador de extranjero. ";
	
	/** The Constant MSG_ERROR_FECHAS_CURP_DIFERENTES. */
	private static final String MSG_ERROR_FECHAS_CURP_DIFERENTES = "La fecha contenida en la " + 
			"clave CURP no coincide con la fecha de nacimiento del cliente.";
	
	/** The Constant MSG_ERROR_FECHAS_ID_EXT_DIFERENTES. */
	private static final String MSG_ERROR_FECHAS_ID_EXT_DIFERENTES = "La fecha contenida en el " +
			"identificador de extranjero es diferente a la fecha de {0}. ";
	
	/** The Constant MSG_ERROR_MAYOR_EDAD. */
	private static final String MSG_ERROR_MAYOR_EDAD = "Solo se permite el registro de clientes " +
			"mayores de edad (>= 18 a\u00f1os). \n";
	
	/** The persona dto. */
	private PersonaDto personaDto;
	
	/** The formato fecha. */
	private String formatoFecha;

	/**
	 * Instantiates a new persona util.
	 *
	 * @param personaDto the persona dto
	 * @param formatoFecha the formato fecha
	 */
	public PersonaUtil(PersonaDto personaDto, String formatoFecha) {
		this.personaDto = personaDto;
		this.formatoFecha = formatoFecha;
	}

	/**
	 * Checks if is completa informacion general persona.
	 *
	 * @return the string
	 */
	private String isCompletaInformacionGeneralPersona() {
		String resultado = "";
		if (personaDto.isPersonaFisica()) {
			resultado = this.isCompletaInformacionGeneralPersonaFisica();
		}
		else if (personaDto.isPersonaMoral()) {
			resultado = this.isCompletaInformacionGeneralPersonaMoral();
		}
		else {
			resultado = MSG_ERROR_TIPO_PERSONA;
		}
		
		return resultado;
	}
	
	/**
	 * Checks if is completa informacion general persona fisica.
	 *
	 * @return the string
	 */
	private String isCompletaInformacionGeneralPersonaFisica() {
		String resultado = "";
		PersonaFisica fisica = (PersonaFisica) this.personaDto.getPersona();
		boolean hayErrores = false;
		String msg = MSG_ERROR_DATOS_FALTANTES_CLIENTE;
		
		if (StringUtils.isEmpty(fisica.getNombre())) {
			msg += "Nombre, ";
			hayErrores = true;
		}
		if (StringUtils.isEmpty(fisica.getPaterno())) {
			msg += "Apellido Paterno, ";
			hayErrores = true;
		}
		if (personaDto.isNacionalidadMexicana()) {
			if (StringUtils.isEmpty(fisica.getRfc())) {
				msg += "RFC, ";
				hayErrores = true;
			}
			if (StringUtils.isEmpty(fisica.getCurp())) {
				msg += "CURP, ";
				hayErrores = true;
			}
		}
		else if (personaDto.isNacionalidadExtranjera()) {
			if (StringUtils.isEmpty(personaDto.getIdExtranjero())) {
				msg += "Identificador de Extranjero, ";
				hayErrores = true;
			}
		}
		else {
			msg += "Nacionalidad, ";
			hayErrores = true;
		}
		//Falta de validar el pais de nacimiento
		if (StringUtils.isEmpty(fisica.getFechaNacimiento())) {
			msg += "Fecha de nacimiento, ";
			hayErrores = true;
		}
		if (StringUtils.isEmpty(fisica.getSexo())) {
			msg += "Sexo, ";
			hayErrores = true;
		}
		if (StringUtils.isEmpty(fisica.getIdTipoIdentifica())) {
			msg += "Tipo de Identificaci\u00f3n, ";
			hayErrores = true;
		}
		if (StringUtils.isEmpty(fisica.getNoIdentificacion())) {
			msg += "N\u00famero de Identificaci\u00f3n, ";
			hayErrores = true;
		}
		if (hayErrores) {
			resultado = msg;
		}
		
		return resultado;
	}
	
	/**
	 * Checks if is completa informacion general persona moral.
	 *
	 * @return the string
	 */
	private String isCompletaInformacionGeneralPersonaMoral() {
		String resultado = "";
		PersonaMoral moral = (PersonaMoral) personaDto.getPersona();
		boolean hayErrores = false;
		String msg = MSG_ERROR_DATOS_FALTANTES_CLIENTE;
		
		if (StringUtils.isEmpty(moral.getRazonSocial())) {
			msg += "Raz\u00f3n Social, ";
			hayErrores = true;
		}
		if (moral.getIdSector() == 0) {
			msg += "Sector Econ\u00f3mico, ";
			hayErrores = true;
		}
		if (StringUtils.isEmpty(moral.getIdPais())) {
			msg += "Nacionalidad, ";
			hayErrores = true;
		}
		else if (personaDto.isNacionalidadMexicana()) {
			if (StringUtils.isEmpty(moral.getRfc())) {
				msg += "RFC, ";
				hayErrores = true;
			}
		}
		else if (personaDto.isNacionalidadExtranjera()) { 
			if (StringUtils.isEmpty(personaDto.getIdExtranjero())) {
				msg += "Identificador de Extranjero, ";
				hayErrores = true;
			}
		}
		if (StringUtils.isEmpty(moral.getFechaConstitucion())) {
			msg += "Fecha de constituci\u00f3n, ";
			hayErrores = true;
		}
		if (hayErrores) {
			resultado = msg;
		}
		
		return resultado;
	}
	
	/**
	 * Valida informacion general persona correcta.
	 *
	 * @return the string
	 */
	private String validaInformacionGeneralPersonaCorrecta() {
		String errores = "";
		if (this.personaDto.isPersonaFisica()) {
			errores = this.validaInformacionGeneralPersonaFisicaCorrecta();
		}
		else if (this.personaDto.isPersonaMoral()) {
			errores = this.validaInformacionGeneralPersonaMoralCorrecta();
		}
		else {
			errores = MSG_ERROR_TIPO_PERSONA;
		}
		
		return errores;
	}
	
	/**
	 * Valida informacion general persona fisica correcta.
	 *
	 * @return the string
	 */
	private String validaInformacionGeneralPersonaFisicaCorrecta() {
		String errores= "";
		PersonaFisica fisica = (PersonaFisica) this.personaDto.getPersona();
		errores = this.validaMayorEdad();
		if (this.personaDto.isNacionalidadMexicana()) {
			errores += this.validaCurp(fisica);
		}
		else if (this.personaDto.isNacionalidadExtranjera()) {
			errores += this.validaIdentificadorExtranjero();
		}
		
		return errores;
	}
	
	/**
	 * Valida curp.
	 *
	 * @param fisica the fisica
	 * @return the string
	 */
	private String validaCurp(PersonaFisica fisica) {
		String errores = "";
		log.debug("Validando la clave CURP: '" + fisica.getCurp() + "'");
		if (fisica.getCurp().length() != 18) {
			errores = MSG_ERROR_CURP_LONG;
		}
		else {
			String temp = fisica.getCurp().substring(0, 4);
			if (!StringUtils.isAlpha(temp)) {
				errores = MSG_ERROR_CURP_PREFIJO + "\n";
			}
			temp = fisica.getCurp().substring(4, 10);
			if (StringUtils.isNumeric(temp)) {
				if (!validaFechasIguales(fisica.getFechaNacimiento(), temp)) {
					errores += MSG_ERROR_FECHAS_CURP_DIFERENTES;
				}
			}
			else {
				errores += MSG_ERROR_CURP_FECHA_NO_NUMERICA;
			}
		}
		
		return errores;
	}
	
	/**
	 * Valida identificador extranjero.
	 *
	 * @return the string
	 */
	private String validaIdentificadorExtranjero() {
		String errores = "";
		int longId = 0;
		String fecha = null;
		String tipoFecha = null;
		int indice = 4;
		if (this.personaDto.isPersonaFisica()) {
			longId = 15;
			fecha = ((PersonaFisica) this.personaDto.getPersona()).getFechaNacimiento();
			tipoFecha = "nacimiento";
		}
		else {
			longId = 14;
			fecha = ((PersonaMoral) this.personaDto.getPersona()).getFechaConstitucion();
			tipoFecha = "constituci\u00f3n";
			indice--;
		}
		if (this.personaDto.getIdExtranjero().length() != longId) {
			Object param[] = new Object[]{new Integer(longId)};
			errores = MessageFormat.format(MSG_ERROR_ID_EXT_LONG, param);
		}
		else {
			String temp = this.personaDto.getIdExtranjero().substring(0, indice);
			log.debug("Validando prefijo idExtranjero: " + temp);
			if (!StringUtils.isAlpha(temp)) {
				Object[] param = new Object[]{new Integer(indice)};
				errores += MessageFormat.format(MSG_ERROR_ID_EXT_PREFIJO, param) + "\n";
			}
			temp = this.personaDto.getIdExtranjero().substring(indice, indice + 6);
			log.debug("Fecha en el identificador de extranjero: " + temp);
			if (StringUtils.isNumeric(temp)) {
				if (!validaFechasIguales(fecha, temp)) {
					Object param[] = new Object[]{tipoFecha};
					errores += MessageFormat.format(MSG_ERROR_FECHAS_ID_EXT_DIFERENTES, param);
				}
			}
			else {
				Object[] param = {new Integer(indice + 1), new Integer(indice + 5)};
				errores += MessageFormat.format(MSG_ERROR_ID_EXT_FECHA_NO_NUMERICA, param);
			}
		}
		
		return errores;
	}
	
	/**
	 * Valida fechas iguales.
	 *
	 * @param strFecha1 the str fecha 1
	 * @param strFecha2 the str fecha 2
	 * @return true, if successful
	 */
	private boolean validaFechasIguales(String strFecha1, String strFecha2) {
		boolean fechaValida = false;
		try {
			log.debug("strFecha1 " + strFecha1);
			log.debug("strFecha2 " + strFecha2);
			Date fecha = (Date) Utilerias.
					convierteCadenaAFecha(strFecha1,
										  Utilerias.JAVA_UTIL_DATE);
			String strFecha = DateUtils.dateToString(fecha, DateUtils.FORMATO_FECHA_YYMMDD);
			log.debug("Fecha a validar: " + strFecha);
			if (strFecha2.equals(strFecha)) {
				fechaValida = true;
			}
		}
		catch (FormatoInvalidoException ex) {
			log.error(ex.getMessage());
		}
		
		return fechaValida;
	}
	
	/**
	 * Valida informacion general persona moral correcta.
	 *
	 * @return the string
	 */
	private String validaInformacionGeneralPersonaMoralCorrecta() {
		String errores = null;
		if (this.personaDto.isNacionalidadExtranjera()) {
			errores = this.validaIdentificadorExtranjero();
		}
		
		return errores;
	}
	
	/**
	 * Valida informacion general persona.
	 *
	 * @return the string
	 */
	public String validaInformacionGeneralPersona() {
		String errores = "";
		log.debug("Validando la informacion de la persona: " + 
				this.personaDto.getPersona().getIdPersona());
		if (this.personaDto == null) {
			throw new IllegalArgumentException(MSG_ERROR_PERSONA_NULA);
		}
		errores = this.isCompletaInformacionGeneralPersona();
		if (StringUtils.isEmpty(errores)) {
			errores = this.validaInformacionGeneralPersonaCorrecta();
		}
		else {
			String tmp = StringUtils.reverse(errores);
			tmp = StringUtils.replaceOnce(tmp, " ,", ".");
			errores = StringUtils.reverse(tmp);
		}
		
		return errores;
	}
	
	/**
	 * Valida persona mayor edad.
	 *
	 * @return the string
	 */
	private String validaMayorEdad() {
		String errores = "";
		
		Date hoy = new Date();
		String fecha = 
				((PersonaFisica) this.personaDto.getPersona()).getFechaNacimiento();
		Date fechaNacimiento = DateUtils.stringToDate(fecha, formatoFecha);
		if (fechaNacimiento != null) {
			int anios = DateUtils.diferenciaAnios(fechaNacimiento, hoy);
			if (anios < 18) {
				errores = MSG_ERROR_MAYOR_EDAD;
			}
		}
		
		return errores;
	}
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		//System.out.println("IsNumeric: " + StringUtils.isNumeric("020101"));
		//Object param[] = new Object[]{new Integer(10)};
		//System.out.println("Mensaje: " + MessageFormat.format(MSG_ERROR_ID_EXT_LONG, param));
	}

}
