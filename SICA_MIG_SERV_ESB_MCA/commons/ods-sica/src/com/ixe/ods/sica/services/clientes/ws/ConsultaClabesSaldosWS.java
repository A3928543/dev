package com.ixe.ods.sica.services.clientes.ws;

import java.math.BigInteger;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import javax.xml.rpc.ServiceException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.banorte.ws.esb.consultaclabessaldos.ConsultaClabesSaldos;
import com.banorte.ws.esb.consultaclabessaldos.ConsultarClabesSaldosLocator;
import com.banorte.ws.esb.consultaclabessaldos.ConsultarClabesSaldosPeticion;
import com.banorte.ws.esb.consultaclabessaldos.ConsultarClabesSaldosRespuestaDatosPersonales;
import com.banorte.ws.esb.consultaclabessaldos.error.ExcepcionGenericaType;
import com.banorte.ws.esb.consultaclabessaldos.headers.AccesoType;
import com.banorte.ws.esb.consultaclabessaldos.headers.EstadoRespuestaType;
import com.banorte.ws.esb.consultaclabessaldos.headers.HeaderRequestType;
import com.banorte.ws.esb.consultaclabessaldos.headers.PropiedadType;
import com.banorte.ws.esb.consultaclabessaldos.headers.RequestType;
import com.banorte.ws.esb.consultaclabessaldos.headers.holders.HeaderResponseTypeHolder;
import com.banorte.ws.esb.consultaclabessaldos.holders.ConsultarClabesSaldosRespuestaHolder;
import com.banorte.www.ws.exception.SicaAltamiraException;
import com.ixe.ods.sica.SicaException;

/**
 * The Class ConsultaClabesSaldosWS.
 */
public class ConsultaClabesSaldosWS {
	
	/** The Constant log. */
	private static final Logger log = Logger.getLogger(ConsultaClabesSaldosWS.class);
	
	 /** The Constant ID_EXITO. */
 	public static final String ID_EXITO = "1";

	/**
	 * Instantiates a new consulta clabes saldos WS.
	 */
	public ConsultaClabesSaldosWS() {
	}
	
	/**
	 * Consultar clabes saldos.
	 *
	 * @param param the param
	 * @return the map
	 * @throws SicaAltamiraException the sica altamira exception
	 */
	public Map consultarClabesSaldos(Map param) throws SicaAltamiraException {
		log.debug("consultarClabesSaldos -> " + param);
		Map result = new HashMap();
		try {
			ConsultarClabesSaldosLocator locator = new ConsultarClabesSaldosLocator();
			String url = (String) param.get("URL");
			locator.setconsultarClabesSaldosHttpPortEndpointAddress(url);
			ConsultaClabesSaldos proxy =  locator.getconsultarClabesSaldosHttpPort();
			ConsultarClabesSaldosPeticion peticion = this.creaConsultarClabesSaldosPeticion(param);
			HeaderRequestType resquest = this.creaHeaderRequestType(param);
			ConsultarClabesSaldosRespuestaHolder respuesta = 
					new ConsultarClabesSaldosRespuestaHolder();
			HeaderResponseTypeHolder headerResponse = new HeaderResponseTypeHolder();
			proxy.consultarClabesSaldos(peticion, resquest, respuesta, headerResponse);
			EstadoRespuestaType estado = headerResponse.value.getEstadoRespuesta();
			log.info("estado.getId(): " + estado.getId());
			if (ID_EXITO.equals(estado.getId())) {
				if (respuesta.value.getDatosPersonales() != null) {
					ConsultarClabesSaldosRespuestaDatosPersonales datosPersonales = 
							respuesta.value.getDatosPersonales();
					result.put("BANCO", datosPersonales.getBanco());//String
					result.put("BLOQUEO_ADMON", datosPersonales.getBloqueoAdmon());//String
					result.put("BLOQUEO_JUDICIAL", datosPersonales.getBloqueoJudicial());//String
					result.put("CLABE_CUENTA", datosPersonales.getClabeCuenta());//String
					result.put("CODIGO_BLOQUEO", datosPersonales.getCodigoBloqueo());//String
					result.put("CODIGO_POSTAL", datosPersonales.getCodigoPostal());//String
					result.put("CODIGO_RETENCION", datosPersonales.getCodigoRetencion());//String
					result.put("CR_ORIGEN", datosPersonales.getCrOrigen());//String
					result.put("CUENTA_ASOCIADA", datosPersonales.getCuentaAsociada());//String
					result.put("CVE_REGIMEN", datosPersonales.getCveRegimen());//String
					result.put("DESCRIPCION_SUBPRODU", datosPersonales.getDescripcionSubprodu());//String
					result.put("DESCRIPCION_SUCURSAL", datosPersonales.getDescripcionSucursal());//String
					result.put("ENTIDAD_FEDERATIVA", datosPersonales.getEntidadFederativa());//String
					result.put("ESTATUS_CTA", datosPersonales.getEstatusCta());//String
					result.put("ESTATUS_LIN_PRIV", datosPersonales.getEstatusLinPriv());//String
					result.put("FECHA_APERTURA", datosPersonales.getFechaApertura());//Date
					result.put("FECHA_PROCESO", datosPersonales.getFechaProceso());//Date
					result.put("IND_COMISION", datosPersonales.getIndComision());//String
					result.put("IND_SERVICIO", datosPersonales.getIndServicio());//String
					result.put("LINEA_PRIVILEGIOS", datosPersonales.getLineaPrivilegios());//String
					result.put("LIQUIDACION_INTE", datosPersonales.getLiquidacionInte());//String
					result.put("MONTO_SERVICIO", datosPersonales.getMontoServicio());//BigDecimal
					result.put("NOMBRE_CALLE", datosPersonales.getNombreCalle());//String
					result.put("NOMBRE_CIUDAD", datosPersonales.getNombreCiudad());//String
					result.put("NOMBRE_COLONIA", datosPersonales.getNombreColonia());//String
					result.put("NOMBRE_PROMOTOR", datosPersonales.getNombrePromotor());//String
					result.put("NUMERO_CALLE", datosPersonales.getNumeroCalle());//String
					result.put("REGIMEN", datosPersonales.getRegimen());//String
					result.put("REGIMEN_FIRMAS", datosPersonales.getRegimenFirmas());//String
					result.put("RFC", datosPersonales.getRfc());//String
					result.put("SALDO_PROMEDIO", datosPersonales.getSaldoPromedio());//BigDecimal
					result.put("SEGMENTO_CLIENTE", datosPersonales.getSegmentoCliente());//String
					result.put("SITUACION_CUENTA", datosPersonales.getSituacionCuenta());//String
					result.put("SUCEPTIBILIDAD_PLAST", datosPersonales.getSuceptibilidadPlast());//String
					result.put("TELEFONO", datosPersonales.getTelefono());//String
					result.put("TITULARIDAD", datosPersonales.getTitularidad());//String
					result.put("FECHA_VENCIMIENTO", datosPersonales.getTran_FechaVencimiento());//Date
					result.put("NOMBRE_PERSONA", datosPersonales.getTran_NombrePersona());//String
					result.put("NUMERO_CLIENTE", datosPersonales.getTran_NumeroCliente());//String
					result.put("NUMERO_CUENTA", datosPersonales.getTran_NumeroCuenta());//String
					result.put("SUB_PRODUCTO", datosPersonales.getTran_SubProducto());//String
					result.put("TIPO_DIVISA", datosPersonales.getTran_TipoDivisa());//String
					result.put("TIPO_PRODUCTO", datosPersonales.getTran_TipoProducto());//String				
				}
			}
			else {
				StringBuffer sbError = new StringBuffer(
						"Error en la respuesta del servicio consultarClabesSaldos: ");
				sbError.append("\n mensajeUsuario: "
						+ estado.getMensajeAUsuario());
				sbError.append("\n mensajeDetallado: "
						+ estado.getMensajeDetallado());
				log.debug(sbError.toString());
				throw new SicaAltamiraException(estado.getMensajeAUsuario());
			}
		}
		catch (ServiceException ex) {
			log.error("ServiceException en consultarClabesSaldos() ", ex);
			throw new SicaException("Error en el servicio consultarClabesSaldos: " + ex.getMessage());
		}
		catch (ExcepcionGenericaType ex) {
			log.error("ExcepcionGenericaType en consultarClabesSaldos() ", ex);
			throw new SicaException("Error en el servicio consultarClabesSaldos: " + ex.getDescripcion());			
		}
		catch (RemoteException ex) {
			log.error("RemoteException en consultarClabesSaldos() ", ex);
			throw new SicaException("Error en el servicio consultarClabesSaldos: " + ex.getMessage());
		}
		
		return result;
	}
	
	/**
	 * Crea header request type.
	 *
	 * @param param the param
	 * @return the header request type
	 */
	private HeaderRequestType creaHeaderRequestType(Map param) {
		HeaderRequestType headerType = new HeaderRequestType();
		headerType.setCodIdioma((String) param.get("COD_IDIOMA"));
		headerType.setLocale((String) param.get("LOCALE"));
		headerType.setAcceso(this.creaAccesoType(param));
		headerType.setDatosPeticion(this.creaRequestType(param));
		PropiedadType[] datosVariables = this.creaDatosVariables(param);
		if (datosVariables != null && datosVariables.length > 0) {
			headerType.setDatosVariables(datosVariables);
		}
		
		return headerType;
	}
	
	/**
	 * Crea acceso type.
	 *
	 * @param param the param
	 * @return the acceso type
	 */
	private AccesoType creaAccesoType(Map param) {
		AccesoType acceso = new AccesoType();
		acceso.setClaveAcceso((String) param.get("CLAVE_ACCESO"));
		acceso.setCustomSeg((String) param.get("CUSTOM_SEG"));
		acceso.setIdOperacion((String) param.get("ID_OPERACION"));
		acceso.setIdSesion((String) param.get("ID_SESION"));
		acceso.setIdUsuario((String) param.get("ID_USUARIO"));
		acceso.setIdUsuarioHost((String) param.get("ID_USUARIO_HOST"));
		acceso.setSic((String) param.get("SIC"));
		acceso.setTokenOperacion((String) param.get("TOKEN_OPERACION"));
		
		return acceso;
	}
	
	/**
	 * Crea request type.
	 *
	 * @param param the param
	 * @return the request type
	 */
	private RequestType creaRequestType(Map param) {
		RequestType request = new RequestType();
		request.setCodCR((BigInteger) param.get("COD_CR"));
		request.setCodEmpresa((String) param.get("COD_EMPRESA"));
		request.setIdServicio((String) param.get("ID_SERVICIO"));
		request.setVersionEndpoint((String) param.get("VERSION_ENDPOINT"));
		request.setVersionServicio((String) param.get("VERSION_SERVICIO"));
		
		return request;
	}
	
	/**
	 * Crea datos variables.
	 *
	 * @param param the param
	 * @return the propiedad type[]
	 */
	private PropiedadType[] creaDatosVariables(Map param) {
		PropiedadType[] propiedades = null;
		Properties variables = (Properties) param.get("DATOS_VARIABLES");
		if (variables != null && !variables.isEmpty()) {
			propiedades = new PropiedadType[variables.keySet().size()];
			Iterator it = variables.keySet().iterator();
			int pos = 0;
			while(it.hasNext()) {
				Object key = it.next();
				Object value = variables.get(key);
				PropiedadType tmp = new PropiedadType();
				tmp.setNombre((String) key); 
				tmp.setValor((String) value);
				propiedades[pos] = tmp;
			}
		}
		
		return propiedades;
	}
	
	/**
	 * Crea consultar clabes saldos peticion.
	 *
	 * @param param the param
	 * @return the consultar clabes saldos peticion
	 */
	private ConsultarClabesSaldosPeticion creaConsultarClabesSaldosPeticion(Map param) {
		ConsultarClabesSaldosPeticion peticion = new ConsultarClabesSaldosPeticion();
		String cuenta = (String) param.get("NO_CUENTA");
		if (StringUtils.isNotEmpty(cuenta)) {
			peticion.setTran_NumeroCuenta(cuenta);
		}
		String tipoMov = (String) param.get("TIPO_MOV");
		if (StringUtils.isNotEmpty(tipoMov)) {
			peticion.setTipoMovimiento(tipoMov);
		}
		BigInteger fechaIni = (BigInteger) param.get("FECHA_INI");
		if (fechaIni != null) {
			peticion.setFechaInicio(fechaIni);
		}
		BigInteger fechaFin = (BigInteger) param.get("FECHA_FIN");
		if (fechaFin != null) {
			peticion.setFechaFinal(fechaFin);
		}
		String cveServicioNtf = (String) param.get("CVE_SERVICIO_NTF");
		if (StringUtils.isNotEmpty(cveServicioNtf)) {
			peticion.setClaveServicioNtf(cveServicioNtf);
		}
		
		return peticion;
	}

}
