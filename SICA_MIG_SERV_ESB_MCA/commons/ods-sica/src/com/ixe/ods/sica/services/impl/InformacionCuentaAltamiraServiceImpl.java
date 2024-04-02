package com.ixe.ods.sica.services.impl;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.rpc.ServiceException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.banorte.www.ws.ESBRequest.ConsultaInformacionCuenta.ESBRequestType;
import com.banorte.www.ws.ESBRequest.ConsultaInformacionCuenta.HeaderReq;
import com.banorte.www.ws.ESBRequest.ConsultaInformacionCuenta.Request;
import com.banorte.www.ws.ESBRequest.ConsultaInformacionCuenta.RequestAsincrono;
import com.banorte.www.ws.ESBResponse.ConsultaInformacionCuenta.ESBResponseType;
import com.banorte.www.ws.esb.ConsultaInfoRequest.ConsultaInfoRequest;
import com.banorte.www.ws.esb.ConsultaInfoResponse.BGM1622;
import com.banorte.www.ws.esb.ConsultaInformacionCuenta.ConsultaInformacionCuentaServiceLocator;
import com.banorte.www.ws.exception.SicaAltamiraException;

import com.ixe.bean.Usuario;
import com.ixe.bean.bup.Persona;
import com.ixe.bean.bup.PersonaFisica;
import com.ixe.bean.bup.PersonaMoral;

import com.ixe.contratacion.ContratacionException;
import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.SicaProperties;
import com.ixe.ods.sica.SicaRegistroPersonaService;
import com.ixe.ods.sica.dto.DatosBasicosClienteAltamiraDto;
import com.ixe.ods.sica.dto.InfoCuentaAltamiraDto;
import com.ixe.ods.sica.model.CuentaAltamira;
import com.ixe.ods.sica.model.ParametroSica;
import com.ixe.ods.sica.sdo.SicaServiceData;
import com.ixe.ods.sica.services.ConsultaDatosBasicosClienteService;
import com.ixe.ods.sica.services.InformacionCuentaAltamiraService;


/**
 * Clase para consumir el servicio
 *         de consulta de Informacion de cuentas
 *  en Altamira - Ixe.
 *
 * @author Diego Pazaran
 *  Banorte
 *
 * @version $Revision: 1.1.2.1.12.2.2.1.14.1.12.2.4.1 $
 */
public class InformacionCuentaAltamiraServiceImpl implements InformacionCuentaAltamiraService {
    /**
     * El objeto para logging.
     */
    private final transient Log _logger = LogFactory.getLog(getClass());

    /** El locator del servicio de consultaInformacionCuenta */
    private ConsultaInformacionCuentaServiceLocator locatorInformacionCuenta;

    /** El ServiceData de Sica */
    private SicaServiceData sicaServiceData;

    /** El servicio para registro de personas en la bup * */
    private SicaRegistroPersonaService sicaRegistroPersonaService;

    /** El servicio de consulta de datos basicos de cliente. * */
    private ConsultaDatosBasicosClienteService consultaDatosBasicosClienteService;
	
    final String ID_SERVICIO_B162 = "MCA_ID_SERVICIO_B162";
    final String ID_SERVICIO_PE80 = "MCA_ID_SERVICIO_PE80";
    final String ID_USUARIO = "MCA_ID_USUARIO";
	final String IP_CLIENTE = "MCA_IP_CLIENTE";
	final String TIPO_CANAL = "MCA_TIPO_CANAL";
	final String ID_APLICACION = "MCA_ID_APLICACION";
	final String VERSION_SERVICIO_B162 = "MCA_VERSION_SERVICIO_B162";
	final String VERSION_SERVICIO_PE80 = "MCA_VERSION_SERVICIO_PE80";
	final String CODCR = "MCA_CODCR";
	final String IP_SERVER = "MCA_IP_SERVER";
	final String VERSION_ENDPOINT_B162 = "MCA_VERSION_ENDPOINT_B162";
	final String VERSION_ENDPOINT_PE80 = "MCA_VERSION_ENDPOINT_PE80";
	final String URI_LEG = "MCA_URI_LEG";
	final String USER_LEG = "MCA_USER_LEG";
	final String PAS_LEG = "MCA_PWD_LEG";

    /**
     * Metodo que consulta
     * la información de la cuenta en Altamia
     * haciendo uso de los servicios del ESB
     *
     * @param noCuenta el numero de cuenta a consultar.
     *
     * @return <code>InfoCuentaAltamiraDto</code> con
     *         la informacion de la cuenta.
     */
    public InfoCuentaAltamiraDto consultaInformacionCuenta(String noCuenta, String ticket) throws SicaAltamiraException {
    	InfoCuentaAltamiraDto infoCuentaAltamiraDto = new InfoCuentaAltamiraDto();
    	Map parameters = new HashMap();
    	ConsultaClabesSaldosImpl consultaClabesSaldos = new ConsultaClabesSaldosImpl();
    	
    	parameters.put("URL", getSicaServiceData().findParametro(ParametroSica.URL_ESB_CONSULTA_INFO_CTA).getValor());
    	parameters.put("NO_CUENTA", noCuenta);
    	parameters.put("TIPO_MOV", "2");
    	parameters.put(URI_LEG, getSicaServiceData().findParametro(URI_LEG).getValor());
    	parameters.put(USER_LEG, getSicaServiceData().findParametro(USER_LEG).getValor());
    	parameters.put(PAS_LEG, getSicaServiceData().findParametro(PAS_LEG).getValor());
    	parameters.put(ID_USUARIO, getSicaServiceData().findParametro(ID_USUARIO).getValor());
    	parameters.put(IP_CLIENTE, getSicaServiceData().findParametro(IP_CLIENTE).getValor());
    	parameters.put(TIPO_CANAL, getSicaServiceData().findParametro(TIPO_CANAL).getValor());
    	parameters.put(ID_APLICACION, getSicaServiceData().findParametro(ID_APLICACION).getValor());
    	parameters.put(VERSION_SERVICIO_B162, getSicaServiceData().findParametro(VERSION_SERVICIO_B162).getValor());
    	parameters.put(CODCR, getSicaServiceData().findParametro(CODCR).getValor());
    	parameters.put(IP_SERVER, getSicaServiceData().findParametro(IP_SERVER).getValor());
    	parameters.put(VERSION_ENDPOINT_B162, getSicaServiceData().findParametro(VERSION_ENDPOINT_B162).getValor());
    	parameters.put(ID_SERVICIO_B162, getSicaServiceData().findParametro(ID_SERVICIO_B162).getValor());
    	parameters.put("TICKET", ticket);
    	
    	infoCuentaAltamiraDto = consultaClabesSaldos.consultaInformacionCuenta(parameters);
    	
    	if(!infoCuentaAltamiraDto.getStatusCuenta().equalsIgnoreCase(STATUS_ACTIVA)){
    		throw new SicaAltamiraException("El estatus de la cuenta es: " + infoCuentaAltamiraDto.getStatusCuenta());
        }
        
        return infoCuentaAltamiraDto;
    }
    
    /**
     * Metodo que consulta
     * la información de la cuenta en Altamia
     * haciendo uso de los servicios del ESB
     *
     * @param noCuenta el numero de cuenta a consultar.
     *
     * @return <code>InfoCuentaAltamiraDto</code> con
     *         la informacion de la cuenta.
     */
    public InfoCuentaAltamiraDto consultaInformacionCuentaParaReporte(String noCuenta, String ticket) throws SicaAltamiraException {
    	
    	InfoCuentaAltamiraDto infoCuentaAltamiraDto = new InfoCuentaAltamiraDto();
    	ConsultaClabesSaldosImpl consultaClabesSaldos = new ConsultaClabesSaldosImpl();
    	Map parameters = new HashMap();
    	
    	
    	parameters.put("URL", getSicaServiceData().findParametro(ParametroSica.URL_ESB_CONSULTA_INFO_CTA).getValor());
    	parameters.put("NO_CUENTA", noCuenta);
    	parameters.put("TIPO_MOV", "2");
    	parameters.put(URI_LEG, getSicaServiceData().findParametro(URI_LEG).getValor());
    	parameters.put(USER_LEG, getSicaServiceData().findParametro(USER_LEG).getValor());
    	parameters.put(PAS_LEG, getSicaServiceData().findParametro(PAS_LEG).getValor());
    	parameters.put(ID_USUARIO, getSicaServiceData().findParametro(ID_USUARIO).getValor());
    	parameters.put(IP_CLIENTE, getSicaServiceData().findParametro(IP_CLIENTE).getValor());
    	parameters.put(TIPO_CANAL, getSicaServiceData().findParametro(TIPO_CANAL).getValor());
    	parameters.put(ID_APLICACION, getSicaServiceData().findParametro(ID_APLICACION).getValor());
    	parameters.put(VERSION_SERVICIO_B162, getSicaServiceData().findParametro(VERSION_SERVICIO_B162).getValor());
    	parameters.put(CODCR, getSicaServiceData().findParametro(CODCR).getValor());
    	parameters.put(IP_SERVER, getSicaServiceData().findParametro(IP_SERVER).getValor());
    	parameters.put(VERSION_ENDPOINT_B162, getSicaServiceData().findParametro(VERSION_ENDPOINT_B162).getValor());
    	parameters.put(ID_SERVICIO_B162, getSicaServiceData().findParametro(ID_SERVICIO_B162).getValor());
    	parameters.put("TICKET", ticket);
    	
    	infoCuentaAltamiraDto = consultaClabesSaldos.consultaInformacionCuenta(parameters);
    	
    	if(!infoCuentaAltamiraDto.getStatusCuenta().equalsIgnoreCase(STATUS_ACTIVA)){
    		throw new SicaAltamiraException("El estatus de la cuenta es: " + infoCuentaAltamiraDto.getStatusCuenta());
        }
        
        return infoCuentaAltamiraDto;
    }


    /**
     * Construye un objeto ESBRequestType.
     *
     * @param numeroCuenta el numero de
     *         cuenta del que se desea consultar la informacion.
     *
     * @param asincrono true si se requiere modo asincrono
     *
     * @return un objeto <code>ESBRequestType</code>
     */
    private ESBRequestType getESBRequest(String noCuenta, boolean asincrono) {
        ESBRequestType ESBRequest = new ESBRequestType();

        //Se asigna el header
        ESBRequest.setHeader(getHeaderRequest());

        //Se asigna el request
        ESBRequest.setRequest(getRequest(noCuenta, asincrono));

        return ESBRequest;
    }

    /**
     * Obtiene un nuevo header
     *
     * @return <code>Header</code>
     */
    private HeaderReq getHeaderRequest() {
        HeaderReq header = new HeaderReq();

        return header;
    }

    /**
     * Construye un objeto Request.
     *
     * @param numeroCuenta el numero de
     *         cuenta del que se desea consultar la informacion.
     *
     * @param asincrono true si se requiere modo asincrono
     *
     * @return un objeto <code>Request</code>
     */
    private Request getRequest(String numeroCuenta, boolean asincrono) {
        Request req = new Request();
        ConsultaInfoRequest consultaInfoRequest = new ConsultaInfoRequest();

        consultaInfoRequest.setNumeroCuenta(numeroCuenta);
        consultaInfoRequest.setTipoMovimiento(TIPO_MOVIMIENTO_DOS);

        req.setConsultaInformacionCuenta(consultaInfoRequest);

        if (!asincrono) {
            req.setAsincrono(RequestAsincrono.S);
        }
        else {
            req.setAsincrono(RequestAsincrono.N);
        }

        return req;
    }

    /**
     *  Construye un objeto dto con la
     *  información de la cuenta recuperada.
     *
     * @param ESBResponse la respuesta
     *          del servicio.
     *
     * @return InfoCuentaAltamiraDto
     *          con los datos de la respuesta del WS
     */
    private InfoCuentaAltamiraDto getInfoCuentaAltamira(ESBResponseType ESBResponse) {
        InfoCuentaAltamiraDto infoCuentaDto = new InfoCuentaAltamiraDto();

        BGM1622 bgm1622 = ESBResponse.getResponse().getConsultaInfoCuentaResponse().getBgm1622();

        infoCuentaDto.setNombreCliente(bgm1622.getNombreCliente().trim());
        infoCuentaDto.setNumeroCliente(bgm1622.getNumeroCliente().trim());
        infoCuentaDto.setStatusCuenta(bgm1622.getStatusCuenta().trim());
        infoCuentaDto.setNumeroCuenta(bgm1622.getNumeroCuenta().trim());
        infoCuentaDto.setTipoDivisa(bgm1622.getTipoDivisa().trim());
        infoCuentaDto.setCr(bgm1622.getCR().trim());
		infoCuentaDto.setDescSucursal(bgm1622.getDescSucursal());

        return infoCuentaDto;
    }

    /**
     * Realiza la búsqueda de un idPersona a partir de
     *         un numero de cuenta en formato Altamira, para
     *         el modo BUP_ALTAMIRA.
     * @param <code>infoCuentaAltamiraDto</code> objeto
     *                 con los datos de la cuenta.
     * @param usuario con los datos del usuario firmado
     *                 al Sistema de Cambios.
     * @return <code>String</code> con el idPersona encontrado.
     * @throws SicaAltamiraException en caso de algún
     *                 error en la ejecución del servicio.
     */
    public String getIdPersonaModoBupAltamira(InfoCuentaAltamiraDto infoCuentaAltamiraDto,
        Usuario usuario, String ticket) throws SicaAltamiraException {
        String idPersona = null;
        List cuentaAltamiraList = null;
        CuentaAltamira cuentaAltamira = null;
        Persona persona = null;

        //se busca la cuenta en sc_cuenta_altamira
        cuentaAltamiraList = getSicaServiceData().findCuentaAltamiraByNoCuenta(infoCuentaAltamiraDto.getNumeroCuenta());

        //se encontró un registro para la cuenta.
        if ((cuentaAltamiraList != null) && (cuentaAltamiraList.size() > 0)) {
            //se verifica si la cuenta tiene idPersona (bup) relacionado
            cuentaAltamira = (CuentaAltamira) cuentaAltamiraList.get(0);

            _logger.debug("Se encuentra en SC_CUENTA_ALTAMIRA: " + cuentaAltamira.toString());

            if ((cuentaAltamira.getIdPersona() != null) &&
                    (cuentaAltamira.getIdPersona().intValue() > 0)) {
                //recuperar idPersona
                idPersona = cuentaAltamira.getIdPersona().toString();
                _logger.debug("Se encontró idPersona: " + idPersona);

                return idPersona;
            }

            //La cuenta existe en SC_CUENTA_ALTAMIRA, pero no se obtuvo idPersona
            //se verifica si fue dada de alta con anterioridad
            if (isCuentaRegistradaEnFechaAnterior(cuentaAltamira)) {
                _logger.debug("Cuenta: " + cuentaAltamira.getCuentaAltamira() +
                    " registrada en SC_CUENTA_ALTAMIRA en fecha anterior y " +
                    " no ha sido espejeada, se da de alta persona: ");

                // se crea la Persona en la BUP
                persona = registrarPersonaEnBUP(infoCuentaAltamiraDto, usuario, ticket);

                //se asigna idPersona al registro de sc_cuenta_altamira.
                getSicaServiceData().updateCuentaAltamira(cuentaAltamira, persona.getIdPersona(),
                    usuario.getIdUsuario());

                //se regresa el idPersona
                idPersona = String.valueOf(persona.getIdPersona());

                return idPersona;
            }
            else {
                //La cuenta se registró en sc_cuenta_altamira el mismo día.
                _logger.debug("La cuenta fue registrada en SC_CUENTA_ALTAMIRA el mismo " +
                    "día y no tiene idPersona relacionado");
                throw new SicaAltamiraException("La cuenta no ha sido espejeada a la BUP");
            }
        }
        else {
            //no se encontró la cuenta, se registra en SC_CUENTA_ALTAMIRA
            _logger.debug("La cuenta: " + infoCuentaAltamiraDto.getNumeroCuenta() +
                " no se encuentra en SC_CUENTA_ALTAMIRA, se da de alta...");

            getSicaServiceData().storeCuentaAltamira(infoCuentaAltamiraDto, usuario.getIdUsuario());

            throw new SicaAltamiraException("La cuenta No ha sido espejeada a la BUP");
        }
    }

    /**
     * Realiza la búsqueda de un idPersona a partir de
     *         un numero de cuenta en formato Altamira, para
     *         el modo ALTAMIRA.
     * @param <code>infoCuentaAltamiraDto</code> objeto
     *                 con los datos de la cuenta.
     * @param usuario con los datos del usuario firmado
     *                 al Sistema de Cambios.
     * @return <code>String</code> con el idPersona encontrado.
     * @throws SicaAltamiraException en caso de algún
     *                 error en la ejecución del servicio.
     */
    public String getIdPersonaModoAltamira(InfoCuentaAltamiraDto infoCuentaAltamiraDto,
        Usuario usuario, String ticket ) throws SicaAltamiraException {
        String idPersona = null;
        List cuentaAltamiraList = null;
        CuentaAltamira cuentaAltamira = null;
        Persona persona = null;

        //se busca la cuenta en sc_cuenta_altamira
        cuentaAltamiraList = getSicaServiceData().findCuentaAltamiraByNoCuenta(infoCuentaAltamiraDto.getNumeroCuenta());

        //se encontró un registro para la cuenta.
        if ((cuentaAltamiraList != null) && (cuentaAltamiraList.size() > 0)) {
            //se verifica si la cuenta tiene idPersona (bup) relacionado
            cuentaAltamira = (CuentaAltamira) cuentaAltamiraList.get(0);
            
            _logger.debug("cr del cliente: " + infoCuentaAltamiraDto.getCr());
            if(!StringUtils.isEmpty(infoCuentaAltamiraDto.getCr()))
            	cuentaAltamira.setCr(infoCuentaAltamiraDto.getCr());	
            
            _logger.debug("Se encuentra en SC_CUENTA_ALTAMIRA: " + cuentaAltamira.toString());

    		//F74380 - Validación de Liquidación de Cuentas Banorte en Panel de Autorización Plantillas
            //Siempre se debe tener actualizado el número SIC con base en el recién obtenido de Altamira
            
            if(infoCuentaAltamiraDto.getNumeroCliente() == null || infoCuentaAltamiraDto.getNumeroCliente().trim().equalsIgnoreCase("")) {
            	throw new SicaAltamiraException("N\u00famero de cliente no v\u00e1lido");
            }else {
            	cuentaAltamira.setSic(new Integer(infoCuentaAltamiraDto.getNumeroCliente()));
            }

            if ((cuentaAltamira.getIdPersona() != null) &&
                    (cuentaAltamira.getIdPersona().intValue() > 0)) {
                //recuperar idPersona
                idPersona = cuentaAltamira.getIdPersona().toString();
                _logger.debug("Se encontró cuenta y idPersona en SC_CUENTA_ALTAMIRA: " + idPersona);
                
                _logger.debug("Actualiza el cr del cliente");
                getSicaServiceData().updateCuentaAltamira(cuentaAltamira, cuentaAltamira.getIdPersona().intValue(),
                                                          usuario.getIdUsuario());
            }
            else {
                //La cuenta existe en SC_CUENTA_ALTAMIRA, pero no se obtuvo idPersona
                _logger.debug("Cuenta: " + cuentaAltamira.getCuentaAltamira() +
                    " registrada en SC_CUENTA_ALTAMIRA en fecha anterior y " +
                    " no ha sido espejeada, se da de alta persona: ");

                // se crea la Persona en la BUP
                persona = registrarPersonaEnBUP(infoCuentaAltamiraDto, usuario, ticket);

                //se asigna idPersona al registro de sc_cuenta_altamira.
                getSicaServiceData().updateCuentaAltamira(cuentaAltamira, persona.getIdPersona(),
                    usuario.getIdUsuario());

                //se regresa el idPersona
                idPersona = String.valueOf(persona.getIdPersona());
            }
        }
        else {
            //no se encontró la cuenta, se crea persona y se registra
            // cuenta en SC_CUENTA_ALTAMIRA
            _logger.debug("La cuenta: " + infoCuentaAltamiraDto.getNumeroCuenta() +
                " no se encuentra en SC_CUENTA_ALTAMIRA, se da de alta persona y cuenta...");

            // se crea la Persona en la BUP
            persona = registrarPersonaEnBUP(infoCuentaAltamiraDto, usuario, ticket);

            //se registra idPersona y cuenta en SC_CUENTA_ALTAMIRA
            idPersona = String.valueOf(persona.getIdPersona());
            infoCuentaAltamiraDto.setIdPersona(idPersona);
            getSicaServiceData().storeCuentaAltamira(infoCuentaAltamiraDto, usuario.getIdUsuario());
        }

        //se regresa el idPersona
        _logger.debug("Se regresa idPersona: ");

        return idPersona;
    }

    /**
     * Realiza el registro de una persona en la BUP
     *                         haciendo uso del servicio de consulta de
     *                         datos básicos de cliente en Altamira.
     * @param infoCuentaAltamiraDto con el numero
     *                    de SIC de la persona a registrar.
     * @param usuario firmado al SICA que
     *                         realiza el registro de la persona en la BUP
     * @return <code>Persona</code> con los datos
     *                         de la persona registrada
     * @throws SicaAltamiraException en caso de algún
     *                 error en la ejecución del servicio.
     */
    private Persona registrarPersonaEnBUP(InfoCuentaAltamiraDto infoCuentaAltamiraDto,
        Usuario usuario, String ticket) throws SicaAltamiraException {
        Persona persona = null;
        Integer sic = null;
        DatosBasicosClienteAltamiraDto clienteAltamira = null;
        String tipoPersona = null;
        Map parameters = new HashMap();

        if (StringUtils.isNotEmpty(infoCuentaAltamiraDto.getNumeroCliente())) {
            sic = new Integer(infoCuentaAltamiraDto.getNumeroCliente());
        }
        else {
            _logger.debug("Se requiere numero SIC: " + infoCuentaAltamiraDto.getNumeroCliente());
            throw new SicaAltamiraException("Se requiere el Numero SIC");
        }
        
        parameters.put("URL", getSicaServiceData().findParametro(ParametroSica.URL_ESB_DATOS_BASICOS_CLIENTE).getValor());
		parameters.put("NO_CLIENTE", sic);
		parameters.put(URI_LEG, getSicaServiceData().findParametro(URI_LEG).getValor());
    	parameters.put(USER_LEG, getSicaServiceData().findParametro(USER_LEG).getValor());
    	parameters.put(PAS_LEG, getSicaServiceData().findParametro(PAS_LEG).getValor());
    	parameters.put(ID_USUARIO, getSicaServiceData().findParametro(ID_USUARIO).getValor());
    	parameters.put(IP_CLIENTE, getSicaServiceData().findParametro(IP_CLIENTE).getValor());
    	parameters.put(TIPO_CANAL, getSicaServiceData().findParametro(TIPO_CANAL).getValor());
    	parameters.put(ID_APLICACION, getSicaServiceData().findParametro(ID_APLICACION).getValor());
    	parameters.put(VERSION_SERVICIO_PE80, getSicaServiceData().findParametro(VERSION_SERVICIO_PE80).getValor());
    	parameters.put(CODCR, getSicaServiceData().findParametro(CODCR).getValor());
    	parameters.put(IP_SERVER, getSicaServiceData().findParametro(IP_SERVER).getValor());
    	parameters.put(VERSION_ENDPOINT_PE80, getSicaServiceData().findParametro(VERSION_ENDPOINT_PE80).getValor());
    	parameters.put(ID_SERVICIO_PE80, getSicaServiceData().findParametro(ID_SERVICIO_PE80).getValor());
    	parameters.put("TICKET", ticket);
        
        MantenerDatosBasicosServiceImpl consultaDatosBasicosCliente = new MantenerDatosBasicosServiceImpl();
        clienteAltamira = consultaDatosBasicosCliente.getDatosBasicosClienteAltamira(parameters);
        _logger.debug("Inicia Cliente Altamira");
        tipoPersona = clienteAltamira.getTipoPersona(); //se obtiene tipo persona.clienteAltamira = consultaDatosBasicosCliente.getDatosBasicosClienteAltamira(parameters);
		
        if(clienteAltamira.getTipoPersona().equalsIgnoreCase(DatosBasicosClienteAltamiraDto.TIPO_PF) ||
				clienteAltamira.getTipoPersona().equalsIgnoreCase(DatosBasicosClienteAltamiraDto.TIPO_AE)) {
			if(StringUtils.isEmpty(clienteAltamira.getPaterno()) || StringUtils.isEmpty(clienteAltamira.getMaterno())) {
				throw new SicaAltamiraException("Nombre y Apellido Paterno no deben ser campos vac\u00edos.");
			}
		}else {
			if (StringUtils.isEmpty(clienteAltamira.getRazonSocial())) {
                throw new SicaAltamiraException("Se requiere razón social");
            }
		}
        /* */

        try {
            if (DatosBasicosClienteAltamiraDto.TIPO_PF.equals(tipoPersona) ||
                    DatosBasicosClienteAltamiraDto.TIPO_AE.equals(tipoPersona)) {
                PersonaFisica personaFisica = new PersonaFisica();
                personaFisica.setNombre(clienteAltamira.getNombre());
                personaFisica.setPaterno(clienteAltamira.getPaterno());
                personaFisica.setMaterno(clienteAltamira.getMaterno());
                personaFisica.setTipoPersona(tipoPersona);
                personaFisica.setRfc(clienteAltamira.getRfc());

                persona = getSicaRegistroPersonaService().registraPersona(personaFisica, usuario);
            }
            else if (DatosBasicosClienteAltamiraDto.TIPO_PM.equals(tipoPersona)) {
                PersonaMoral personaMoral = new PersonaMoral();
                personaMoral.setRazonSocial(clienteAltamira.getRazonSocial());
                personaMoral.setTipoPersona(tipoPersona);
                personaMoral.setRfc(clienteAltamira.getRfc());

                persona = getSicaRegistroPersonaService().registraPersona(personaMoral, usuario);
            }
            else {
                throw new SicaAltamiraException("Tipo de Persona No valido: " + tipoPersona);
            }
        }
        catch (ContratacionException ce) {
            _logger.debug("Error al registrar la persona en la bup: ", ce);
            throw new SicaAltamiraException("Error al registrar la persona en la bup: " +
                ce.getCause());
        }
        _logger.debug("Persona: " + persona);
        return persona;
    }

    /**
     * Verifica si una cuenta Altamira
     * 	fue registrada en sc_cuenta_altamira en una
     * 	fecha Anterior al día de hoy.
     *
     * @param cuentaAltamira con la informacion de
     * 	 la cuenta Altamira.
     *
     * @return <code>true</code> si la cuenta fue registrada
     * 		en sc_cuenta_altamira en una fecha anterior al
     * 	    dia de hoy. <code>false</code> en cualquier otro caso.
     */
    private boolean isCuentaRegistradaEnFechaAnterior(CuentaAltamira cuentaAltamira) {
        boolean isRegistradaEnFechaAnterior = false;

        Calendar calAltaCuenta = Calendar.getInstance();
        Calendar calHoy = Calendar.getInstance();

        calAltaCuenta.setTime(cuentaAltamira.getFechaAlta());
        calHoy.setTime(getFechaHoy());

        _logger.debug("FechaAltaCuenta: " + calAltaCuenta.get(Calendar.YEAR) + "/" +
            calAltaCuenta.get(Calendar.MONTH) + "/" + calAltaCuenta.get(Calendar.DAY_OF_MONTH) +
            " \n");

        _logger.debug("FechaHoy: " + calHoy.get(Calendar.YEAR) + "/" + calHoy.get(Calendar.MONTH) +
            "/" + calHoy.get(Calendar.DAY_OF_MONTH) + " ");

        if ((calAltaCuenta.getTimeInMillis() < calHoy.getTimeInMillis()) &&
                (calAltaCuenta.get(Calendar.DAY_OF_YEAR) != calHoy.get(Calendar.DAY_OF_YEAR))) {
            isRegistradaEnFechaAnterior = true;
        }

        return isRegistradaEnFechaAnterior;
    }

    /**
     * Obtiene la fecha hoy.
     *
     * @return <code>Date</code>
     *  	con la fecha de hoy.
     */
    private Date getFechaHoy() {
        String fechaSistema = null;
        Date fechaHoy = null;
        Date fechaTmp = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        ParametroSica p = (ParametroSica) getSicaServiceData().find(ParametroSica.class,
                ParametroSica.FECHA_SISTEMA);

        try {
            if (fechaSistema != null) {
                fechaHoy = sdf.parse(p.getValor());
            }
            else {
                fechaTmp = Calendar.getInstance().getTime();
                fechaSistema = sdf.format(fechaTmp);
                fechaHoy = sdf.parse(fechaSistema);
            }
        }
        catch (ParseException pe) {
            _logger.debug("No se puede obtener la fecha del sistema", pe);
            throw new SicaException("No se puede obtener la fecha del sistema");
        }

        _logger.debug("Fecha Hoy: " + fechaHoy);

        return fechaHoy;
    }

    /**
     * getter SicaServiceData.
     *
     * @return una referencia
     *  al objeto de acceso a datos.
     */
    public SicaServiceData getSicaServiceData() {
        return sicaServiceData;
    }

    /**
     * Seter  del objeto sicaServicedata
     *
     * @param sicaServiceData con la referencia
     *         al objeto de acceso a datos.
     */
    public void setSicaServiceData(SicaServiceData sicaServiceData) {
        this.sicaServiceData = sicaServiceData;
    }

    /**
     * Getter locatorInformacionCuenta
     *
     * @return un objeto ConsultaInformacionCuentaServiceLocator
     */
    public ConsultaInformacionCuentaServiceLocator getLocatorInformacionCuenta() {
        return locatorInformacionCuenta;
    }

    /**
     * Setter locatorInformacionCuenta
     *
     * @param locatorInformacionCuenta con la referencia
     *         al objeto.
     */
    public void setLocatorInformacionCuenta(
        ConsultaInformacionCuentaServiceLocator locatorInformacionCuenta) {
        this.locatorInformacionCuenta = locatorInformacionCuenta;
    }

    /**
     * Getter sicaRegistroPersonaService
     *
     * @return sicaRegistroPersonaService con la 
     * 		referencia al objeto.
     */
    public SicaRegistroPersonaService getSicaRegistroPersonaService() {
        return sicaRegistroPersonaService;
    }

    /**
     * Setter sicaRegistroPersonaService
     *
     * @param sicaRegistroPersonaService con el 
     * 		valor a asignar.
     */
    public void setSicaRegistroPersonaService(SicaRegistroPersonaService sicaRegistroPersonaService) {
        this.sicaRegistroPersonaService = sicaRegistroPersonaService;
    }

    /**
     * @return the consultaDatosBasicosClienteService
     */
    public ConsultaDatosBasicosClienteService getConsultaDatosBasicosClienteService() {
        return consultaDatosBasicosClienteService;
    }

    /**
     * @param consultaDatosBasicosClienteService the consultaDatosBasicosClienteService to set
     */
    public void setConsultaDatosBasicosClienteService(
        ConsultaDatosBasicosClienteService consultaDatosBasicosClienteService) {
        this.consultaDatosBasicosClienteService = consultaDatosBasicosClienteService;
    }
}
