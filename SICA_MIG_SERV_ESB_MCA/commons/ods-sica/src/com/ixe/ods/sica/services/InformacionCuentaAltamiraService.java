package com.ixe.ods.sica.services;

import com.banorte.www.ws.exception.SicaAltamiraException;

import com.ixe.bean.Usuario;
import com.ixe.ods.sica.dto.InfoCuentaAltamiraDto;


/**
 * Interface del servicio 
 * 	de informacion de cuenta Altamira.
 *
 * @author Diego Pazarán
 * 			Banorte.
 * @version $Revision: 1.1.2.1.12.1.2.1.30.1 $
 */
public interface InformacionCuentaAltamiraService{
	
	/** Identificador de exito en la invocacion del servicio * */
    public static final String ID_EXITO = "1";

    /** Tipo de respueta que se obtiene del servicio. * */
    public static final String TIPO_MOVIMIENTO_DOS = "2";

    /** Estatus de cuentas activas */
    public static final String STATUS_ACTIVA = "ACTIVA";
    
    /** Modo de operacion BUP y ALTAMIRA */
    public final static String BUP_ALTAMIRA = "BUP_ALTAMIRA";
    
    /** Modo Altamira */
    public final static String ALTAMIRA = "ALTAMIRA";

    /**
     * Metodo que obtiene la informacion
     *  de una cuenta en altamira.
     *
     * @param noCuenta el numero de cuenta
     * 		en altamira.
     *
     * @return objeto <code>InfoCuentaAltamiraDto</code>
     * 	 con la informacion de la cuenta.
     *
     * @throws SicaAltamiraException en caso de algun error.
     */
    public InfoCuentaAltamiraDto consultaInformacionCuenta(String noCuenta, String ticket)
        throws SicaAltamiraException;
    
    /**
     * Metodo que obtiene la informacion
     *  de una cuenta en altamira.
     *
     * @param noCuenta el numero de cuenta
     * 		en altamira.
     *
     * @return objeto <code>InfoCuentaAltamiraDto</code>
     * 	 con la informacion de la cuenta.
     *
     * @throws SicaAltamiraException en caso de algun error.
     */
    public InfoCuentaAltamiraDto consultaInformacionCuentaParaReporte(String noCuenta, String ticket)
      throws SicaAltamiraException;
    
    
    /**
     * Realiza la búsqueda de un idPersona a partir de 
     * 	un numero de cuenta en formato Altamira, para
     * 	el modo BUP_ALTAMIRA.
     * @param <code>infoCuentaAltamiraDto</code> objeto
     * 		con los datos de la cuenta.
     * @param usuario con los datos del usuario firmado
     * 		al Sistema de Cambios.
     * @return <code>String</code> con el idPersona encontrado.
     * @throws SicaAltamiraException en caso de algún
     * 		error en la ejecución del servicio.
     */
    public String getIdPersonaModoBupAltamira(InfoCuentaAltamiraDto infoCuentaAltamiraDto, Usuario usuario, String ticket) 
    	throws SicaAltamiraException;
    /**
     * Realiza la búsqueda de un idPersona a partir de 
     * 	un numero de cuenta en formato Altamira, para
     * 	el modo ALTAMIRA.
     * @param <code>infoCuentaAltamiraDto</code> objeto
     * 		con los datos de la cuenta.
     * @param usuario con los datos del usuario firmado
     * 		al Sistema de Cambios.
     * @return <code>String</code> con el idPersona encontrado.
     * @throws SicaAltamiraException en caso de algún
     * 		error en la ejecución del servicio.
     */
    public String getIdPersonaModoAltamira(InfoCuentaAltamiraDto infoCuentaAltamiraDto, Usuario usuario, String ticket) 
	throws SicaAltamiraException;
    
}
