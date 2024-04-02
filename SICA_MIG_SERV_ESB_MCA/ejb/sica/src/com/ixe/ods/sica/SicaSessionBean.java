/*
 * $Id: SicaSessionBean.java,v 1.19 2008/12/26 23:17:34 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 - 2008 LegoSoft S.C.
 */
package com.ixe.ods.sica;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Date;
import java.text.SimpleDateFormat;

import javax.ejb.CreateException;
import javax.ejb.SessionContext;

import org.springframework.context.access.ContextSingletonBeanFactoryLocator;
import org.springframework.ejb.support.AbstractStatelessSessionBean;

import com.ixe.ods.seguridad.model.SeguridadException;
import com.ixe.ods.seguridad.services.TicketService;
import com.ixe.ods.sica.dao.BitacoraEnviadasDao;
import com.ixe.ods.sica.model.BitacoraEnviadas;
import com.ixe.ods.sica.model.Divisa;
import com.ixe.ods.sica.model.Estado;
import com.ixe.ods.sica.model.HistoricoPosicion;
import com.ixe.ods.sica.sdo.CierreSicaServiceData;
import com.ixe.ods.sica.sdo.ContabilidadSicaServiceData;
import com.ixe.ods.sica.sdo.SicaServiceData;
import com.ixe.ods.sica.services.GeneralMailSender;
import com.ixe.ods.sica.utils.DateUtils;

/**
 * Bean de Servicios del SICA desde afuera del Entorno Web.
 *
 * @author Jean C. Favila, Javier Cordova
 * @version  $Revision: 1.19 $ $Date: 2008/12/26 23:17:34 $
 * @ejbgen:session ejb-name = SicaSessionEJB
 * type = Stateless
 * initial-beans-in-free-pool = 5
 * max-beans-in-free-pool = 50
 * default-transaction = NotSupported
 * trans-timeout-seconds = 21000
 * enable-call-by-reference = true
 * run-as = middleware
 * run-as-identity-principal = mw
 * @ejbgen:role-mapping role-name = middleware principals = mw
 * @ejbgen:jndi-name remote = ejb/sica/SicaHome
 * local = ejb/sica/SicaLocalHome
 */
public class SicaSessionBean extends AbstractStatelessSessionBean {

    /**
     * Constructor por default.
     */
    public SicaSessionBean() {
        super();
    }

    /**
     * Cambia los valores por omisi&oacute;n de que archivo tomar como configuraci&oacute;n para
     * Spring. Tambien indica que el BeanFactory ser&aacute; compartido (singleton).
     *
     * @param sessionContext El SessionContext.
     */
    public void setSessionContext(SessionContext sessionContext) {
        super.setSessionContext(sessionContext);
        setBeanFactoryLocator(ContextSingletonBeanFactoryLocator.getInstance());
        setBeanFactoryLocatorKey("sicaApplicationContext");
    }

    /**
     * Realiza el arranque necesario para proporcionar los servicios.
     *
     * @throws CreateException Si no se puede crear el ejb.
     */
    protected void onEjbCreate() throws CreateException {
    }

    /**
     * Revisa la respuesta por parte de Banxico para el deal enviado y genera la alerta
     * correspondiente con base en la respuesta.
     *
     * @ejbgen:remote-method transaction-attribute = NotSupported
     * @ejbgen:local-method transaction-attribute = NotSupported
     * @param idConf El identificador de la confirmaci&oacute;n.
     * @param divisa La clave de la divisa.
     * @throws Exception Si algo sale mal.
     */
    public void revisarRespuestaBanxicoParaDeal(String idConf, String divisa) throws Exception {
        BitacoraEnviadasDao beDao = (BitacoraEnviadasDao) getBeanFactory().
                getBean("bitacoraEnviadasDao");
        BitacoraEnviadas be = beDao.
                findBitacoraEnviadaByIdConfAndDivisa(idConf.trim(), divisa);
        if (be == null) {
            throw new RuntimeException(
                    "No se encontr\u00d3 el Registro en la Tabla SC_BITACORA_ENVIADAS con " +
                            "idConf: " + idConf.trim() + " y divisa: " + divisa);
        }
        if (be.getStatus() != null) { 
        	if (BitacoraEnviadas.STATUS_ERROR.equals(be.getStatus().toUpperCase().trim())) {
	            int error = be.getError().intValue();
                HashMap contextoDealBanxico = getContextoDealBanxico(be.getId().getIdConf(),
                        be.getId().getDivisa(), be.getError());
                Integer idAdministrador = getSicaServiceData().getIdAdministrador();
                String tipoAlerta = null;
                try {
                    if (error == -100) {
                        tipoAlerta = BitacoraEnviadas.ERR_VERSION;
                    }
                    else if (error == -101) {
                        tipoAlerta = BitacoraEnviadas.ERR_CONEXION_RECHAZADA;
                    }
                    else if (error == -102) {
                        tipoAlerta = BitacoraEnviadas.ERR_PARAMETRO;
                    }
                    else if (error == -103) {
                        tipoAlerta = BitacoraEnviadas.ERR_NUMERO_OP;
                    }
                    else if (error == -104) {
                        tipoAlerta = BitacoraEnviadas.ERR_CONTRAPARTE;
                    }
                    else if (error == -105) {
                        tipoAlerta = BitacoraEnviadas.ERR_OP_INEXISTENTE;
                    }
                    else if (error == -106) {
                        tipoAlerta = BitacoraEnviadas.ERR_FALLA_CONEXION;
                    }
                    else if (error == -107) {
                        tipoAlerta = BitacoraEnviadas.ERR_BANCO_INEXISTENTE;
                    }
                    else if (error == -108) {
                        tipoAlerta = BitacoraEnviadas.ERR_FOLIO_EXISTENTE;
                    }
                    else if (error == -110) {
                        tipoAlerta = BitacoraEnviadas.ERR_CONCIDENCIA_SWAP;
                    }
                    else if (error == -111) {
                        tipoAlerta = BitacoraEnviadas.ERR_OP_DUPLICADA_SWAP;
                    }
                    if (tipoAlerta != null) {
                        getSicaAlertasService().generaAlerta(tipoAlerta, idAdministrador,
                                contextoDealBanxico);
                    }
                }
	            catch (Exception e) {
	                if (logger.isDebugEnabled()) {
	                    logger.debug(e);
                    }
                    throw new RuntimeException("Ocurri\u00d3 un problema al intentar mandarle al " +
                            "Administrador del Sistema la Alerta de Error de Env\u00edo de " +
                            "Operaci\u00d3n a Banxico.\n\n" + e);
                }
            }
        }
    }
    
    /**
     * Calcula el Tipo de Cambio Promedio para un mes de operaciones de una divisa
     * definida. 
     * 
     * @ejbgen:remote-method transaction-attribute = NotSupported
     * @ejbgen:local-method transaction-attribute = NotSupported
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param idMoneda El id moneda para la divisa en phoenix.
     * @param mes El numero de mes que se desea obtener el tcp basado en 1.
     * @return Double.
     * @throws Exception Si
     */
    public Double obtenerTcpDivisaMesAnterior(String ticket, Integer idMoneda, Integer mes)
            throws Exception {
    	validarTicket(ticket);
    	Double tcpd;
    	int mesAnterior;    	
    	if (mes.intValue() < 1 || mes.intValue() > 12) {
            throw new Exception("El n\u00faumero de mes debe estar entre 1 y 12. Verifique los " +
                    "datos.");
    	}
    	else {
    		mesAnterior = mes.intValue() - 1;
    	}
    	Calendar inicioMes = new GregorianCalendar();
        inicioMes.setTime(DateUtils.inicioDia());
        inicioMes.set(Calendar.MONTH, mesAnterior);
        inicioMes.set(Calendar.DAY_OF_MONTH, 1);
        Calendar finMes = new GregorianCalendar();
        finMes.setTime(inicioMes.getTime());
        finMes.add(Calendar.MONTH, 1);
        finMes.add(Calendar.MILLISECOND, -1);
        double tcp = 0;
    	double diasOperados = 0;
        Divisa divisa = getSicaServiceData().findDivisaByIdMoneda(idMoneda.toString());
        List posicion = getSicaServiceData().findHistoricoPosicion(1, divisa.getIdDivisa(),
                inicioMes.getTime(), finMes.getTime());
        if (posicion.isEmpty()) {
            throw new Exception("No existen registros de Hist\u00f3rico de Posici\u00f3n para el " +
                    "mes especificado.");
        }
        else {
            for (Iterator it = posicion.iterator(); it.hasNext();) {
                HistoricoPosicion hPos = (HistoricoPosicion) it.next();
                if (hPos.getPosIni().getPosicionInicial() + hPos.getCpaVta().getCash() +
                        hPos.getCpaVta().getTom() +
                        hPos.getCpaVta().getSpot() + hPos.getCpaVta().get72Hr() +
                        hPos.getCpaVta().getVFut() > 0) {
                    double posIni  = hPos.getPosIni().getPosicionInicial();
                    double posIniMn  = hPos.getPosIni().getPosicionInicialMn();
                    double mn = (hPos.getCpaVtaMn().getCompraMnClienteCash() +
                            hPos.getCpaVtaMn().getCompraMnClienteTom() +
                            hPos.getCpaVtaMn().getCompraMnClienteSpot() +
                            hPos.getCpaVtaMn().getCompraMnCliente72Hr().doubleValue() +
                            hPos.getCpaVtaMn().getCompraMnClienteVFut().doubleValue()) -
                            (hPos.getCpaVtaMn().getVentaMnClienteCash() +
                                    hPos.getCpaVtaMn().getVentaMnClienteTom() +
                                    hPos.getCpaVtaMn().getVentaMnClienteSpot() +
                                    hPos.getCpaVtaMn().getVentaMnCliente72Hr().doubleValue() +
                                    hPos.getCpaVtaMn().getVentaMnClienteVFut().doubleValue());
                    double div = hPos.getCpaVta().getCash() + hPos.getCpaVta().getTom() +
                            hPos.getCpaVta().getSpot() + hPos.getCpaVta().get72Hr() +
                            hPos.getCpaVta().getVFut();
                    double tmp = (posIniMn + mn) / (posIni + div);
                    tcp += tmp;
                    diasOperados ++;
                }
                else if (((hPos.getCpaVta().getCompraCash() + hPos.getCpaVta().getCompraTom() +
                        hPos.getCpaVta().getCompraSpot() +
                        hPos.getCpaVta().getCompra72Hr().doubleValue() +
                        hPos.getCpaVta().getCompraVFut().doubleValue()) -
                        (hPos.getCpaVta().getVentaCash() + hPos.getCpaVta().getVentaTom() +
                                hPos.getCpaVta().getVentaSpot() +
                                hPos.getCpaVta().getVenta72Hr().doubleValue() +
                                hPos.getCpaVta().getVentaVFut().doubleValue())) == 0) {
                    tcp += 0;
                }
                else {
                    double posIni  = hPos.getPosIni().getPosicionInicial();
                    double posIniMn  = hPos.getPosIni().getPosicionInicialMn();
                    double mn = (hPos.getCpaVtaMn().getCompraMnClienteCash() +
                            hPos.getCpaVtaMn().getCompraMnClienteTom() +
                            hPos.getCpaVtaMn().getCompraMnClienteSpot() +
                            hPos.getCpaVtaMn().getCompraMnCliente72Hr().doubleValue() +
                            hPos.getCpaVtaMn().getCompraMnClienteVFut().doubleValue()) -
                            (hPos.getCpaVtaMn().getVentaMnClienteCash() +
                                    hPos.getCpaVtaMn().getVentaMnClienteTom() +
                                    hPos.getCpaVtaMn().getVentaMnClienteSpot() +
                                    hPos.getCpaVtaMn().getVentaMnCliente72Hr().doubleValue() +
                                    hPos.getCpaVtaMn().getVentaMnClienteVFut().doubleValue());
                    double div = hPos.getCpaVta().getCash() + hPos.getCpaVta().getTom() +
                            hPos.getCpaVta().getSpot() + hPos.getCpaVta().get72Hr() +
                            hPos.getCpaVta().getVFut();
                    double tmp = (posIniMn + mn) / (posIni + div);
                    tcp += tmp;
                    diasOperados ++;
                }
            }
            double total = 0;
            if (diasOperados > 0) {
            	total = tcp / diasOperados;
            }
            else {
            	total = tcp;
            }
            tcpd =  new Double(total);
            return tcpd;
        }
    }

    /**
     * Crea el Contexto con los Par&aacute;metros necesarios para la Alerta
     * correspondiente al Error indicado por Banxico como respuesta al intento de
     * Reporte de Operaci&oacute;n o de Cancelaci&oacute;n.
     *
     * @param idConf	EL idConf llave primaria del Registro con Error.
     * @param divisa	La divisa llave primaria del Registro con Error.
     * @param error		EL numero de Error.
     * @return HashMap con el Contexto para las Alertas de Banxico.
     */
    private HashMap getContextoDealBanxico(String idConf, String divisa, Integer error) {
        HashMap contexto = new HashMap();
        contexto.put("ID_CONF", idConf);
        contexto.put("DIVISA", divisa);
        contexto.put("ERROR", error.toString());
        return contexto;
    }
    
    /**
     * Valida el ticket de la sesi&oacute;n.
     * 
     * @param ticket El ticket de la sesi&oacute;n.
     * @throws SeguridadException Se dispara en caso de ocurrir algun error.
     */
    private void validarTicket(String ticket) throws SeguridadException {
        ((TicketService) getBeanFactory().getBean("ticketService")).isTicketValido(ticket);
    }
    
    /**
     * @ejbgen:remote-method transaction-attribute = NotSupported
     * @ejbgen:local-method transaction-attribute = NotSupported
     * @throws Exception Si ocurre alg&uacute;n error.
     */
    public String iniciarCierreContableYCierreFinal(String[] para) throws Exception {
        iniciarCierreContable(para);
        return iniciarCierreFinal(para);
    }

    /**
     * @ejbgen:remote-method transaction-attribute = NotSupported
     * @ejbgen:local-method transaction-attribute = NotSupported
     * @throws Exception Si ocurre alg&uacute;n error durante el cierre.
     * @see com.ixe.ods.sica.sdo.ContabilidadSicaServiceData#generacionMovimientosContables()
     */
    public void iniciarCierreContable(String[] para) throws Exception {
        enviarCorreo(para, "Ha iniciado el cierre contable del SICA.", "");
        String titulo = "El cierre contable del SICA ha sido exitoso.";
        String mensaje = "";
        try {
        Estado estadoActual = getSicaServiceData().findEstadoSistemaActual();
        if (Estado.ESTADO_FIN_LIQUIDACIONES != estadoActual.getIdEstado()) {
            throw new SicaException("Para correr el cierre contable, el sistema debe encontrarse " +
                "en estado de Fin de Liquidaciones.");
        }
        ContabilidadSicaServiceData cssd = (ContabilidadSicaServiceData) getBeanFactory().
                getBean("contabilidadServiceData");
        cssd.generacionMovimientosContables();
            List estados = getSicaServiceData().findAll(Estado.class);
            for (Iterator it = estados.iterator(); it.hasNext();) {
                Estado estado = (Estado) it.next();
                    if (estado.isActual()) {
                        estado.setActual(false);
                    }
                    if (estado.getIdEstado() == Estado.ESTADO_GENERACION_CONTABLE) {
                        estado.setHoraInicio(HOUR_FORMAT.format(new Date()));
                        estado.setActual(true);
                    }
            }
            getSicaServiceData().update(estados);

        }
        catch (Exception e) {
            e.printStackTrace();
            titulo = "El cierre contable del SICA ha fallado.";
            mensaje = obtenerStackTrace(e);
            throw e;
        }
        finally {
            enviarCorreo(para, titulo, mensaje);
        }
    }

    /**
     * @ejbgen:remote-method transaction-attribute = NotSupported
     * @ejbgen:local-method transaction-attribute = NotSupported
     * @throws Exception Si ocurre alg&uacute;n error durante el cierre.
     * @see CierreSicaServiceData#cerrarDiaSica(SicaSiteCache, String, java.util.Date).
     * @return String.
     */
    public String iniciarCierreFinal(String[] para) throws Exception {
        enviarCorreo(para, "Ha iniciado el cierre Final del SICA.", "");
        String titulo = "El cierre final del SICA ha sido exitoso.";
        String mensaje = "";
        try {
        Estado estadoActual = getSicaServiceData().findEstadoSistemaActual();
        if (Estado.ESTADO_GENERACION_CONTABLE != estadoActual.getIdEstado()) {
            throw new SicaException("Para correr el cierre final, el sistema debe encontrarse " +
                "en estado de Generaci\u00f3n de movimientos Contables.");
        }
        CierreSicaServiceData cierreServiceData = (CierreSicaServiceData)
            getBeanFactory().getBean("cierreServiceData");
        cierreServiceData.cambiarEstadoCierre();
        SicaSiteCache ssc = (SicaSiteCache) (getBeanFactory().getBean("sicaSiteCache"));
        return cierreServiceData.cerrarDiaSica(ssc, null,
                getSicaServiceData().getFechaSistema()).toString();
    }
        catch (Exception e) {
            e.printStackTrace();
            titulo = "El cierre final del SICA ha fallado.";
            mensaje = obtenerStackTrace(e);
            throw e;
        }
        finally {
            enviarCorreo(para, titulo, mensaje);
        }
    }

    private String obtenerStackTrace(Exception e) {
        StackTraceElement[] stes = e.getStackTrace();
        StringBuffer sb = new StringBuffer(e.getMessage());
        if (stes != null) {
            for (int i = 0; i < stes.length; i++) {
                sb = sb.append(stes[i].toString()).append("\n\n");
            }
        }
        return sb.toString();
    }

    /**
     * Env&iacute;a un correo para indicar el status fin del cierre. 
     */
    private void enviarCorreo(String[] para, String titulo, String mensaje) {
        try {
            GeneralMailSender gms = (GeneralMailSender) getBeanFactory().getBean("generalMailSender");
            gms.enviarMail("ixecambios@ixe.com.mx", para, null, titulo, mensaje);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Regresa el valor de sicaServiceData.
     *
     * @return SicaServiceData.
     */
    private SicaServiceData getSicaServiceData() {
        return (SicaServiceData) getBeanFactory().getBean("sicaServiceData");
    }

    /**
     * Regresa el valor de sicaAlertasService.
     *
     * @return SicaAlertasService.
     */
    private SicaAlertasService getSicaAlertasService() {
        return (SicaAlertasService) getBeanFactory().getBean("sicaAlertasService");
    }

    /**
     * El UID para serializaci&oacute;n.
     */
    private static final long serialVersionUID = 1372174799997117163L;

    /**
     * Constante con el formato de horas y minutos.
     */
    public static final SimpleDateFormat HOUR_FORMAT = new SimpleDateFormat("HH:mm");    
}
