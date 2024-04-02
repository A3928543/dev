/*
 * $Id: SicaWorkFlowSessionBean.java,v 1.22.2.2.8.1.2.1 2010/12/24 00:38:57 galiciad Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2007 - 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.SessionContext;

import org.springframework.context.access.ContextSingletonBeanFactoryLocator;
import org.springframework.ejb.support.AbstractStatelessSessionBean;

import com.ixe.ods.seguridad.model.IUsuario;
import com.ixe.ods.seguridad.model.SeguridadException;
import com.ixe.ods.seguridad.model.Usuario;
import com.ixe.ods.sica.model.Actividad;
import com.ixe.ods.sica.model.Deal;
import com.ixe.ods.sica.model.DealDetalle;
import com.ixe.ods.sica.model.Reverso;
import com.ixe.ods.sica.sdo.ReversosServiceData;
import com.ixe.ods.sica.sdo.WorkFlowServiceData;
import com.ixe.ods.sica.vo.ReversoVO;

/**
 * @author Jean C. Favila
 * @version $Revision: 1.22.2.2.8.1.2.1 $ $Date: 2010/12/24 00:38:57 $
 * @ejbgen:session ejb-name = SicaWorkFlowSessionEJB
 * type = Stateless
 * initial-beans-in-free-pool = 5
 * max-beans-in-free-pool = 50
 * default-transaction = Required
 * trans-timeout-seconds = 300
 * enable-call-by-reference = true
 * @ejbgen:jndi-name remote = ejb/sica/SicaWorkFlowHome
 * local = ejb/sica/SicaWorkFlowLocalHome
 */
public class SicaWorkFlowSessionBean extends AbstractStatelessSessionBean {

    /**
     * Constructor por default.
     */
    public SicaWorkFlowSessionBean() {
        super();
    }

    /**
     * Realiza el arranque necesario para proporcionar los servicios.
     *
     * @throws javax.ejb.CreateException Si no se puede crear el ejb.
     */
    protected void onEjbCreate() throws CreateException {
    }

    /**
     * Cambia los valores por omisi&oacute;n de que archivo tomar como configuraci&oacute;n para
     * Spring. Tambien indica que el BeanFactory ser&aacute; compartido (singleton).
     *
     * @param sessionContext El sessionContext del EJB.
     */
    public void setSessionContext(SessionContext sessionContext) {
        super.setSessionContext(sessionContext);
        setBeanFactoryLocator(ContextSingletonBeanFactoryLocator.getInstance());
        setBeanFactoryLocatorKey("sicaApplicationContext");
    }

    /**
     * @param ticket          El ticket de la sesi&ocute;n.
     * @param nombreActividad El nombre de la actividad (opcional, puede ser null).
     * @return String.
     * @throws com.ixe.ods.seguridad.model.SeguridadException
     *          Si el ticket no es v&aacute;lido.
     * @ejbgen:remote-method transaction-attribute = Required
     * @ejbgen:local-method transaction-attribute = Required
     * @see com.ixe.ods.sica.sdo.WorkFlowServiceData#findAllActividadesPendientes(String, String).
     */
    public List findAllActividadesPendientes(String ticket, String nombreActividad)
            throws SeguridadException {
        if ("Reversos".equals(nombreActividad)) {
            return getReversosServiceData().findReversosPendientes();
        }
        else if ("Confirmaciones".equals(nombreActividad)) {
            return getWorkFlowServiceData().findDealsInterbancariosPorConfirmar();
        }
        else if ("Alta Email".equals(nombreActividad)) {
            return getWorkFlowServiceData().findAltaAnticipadaCorreosElectronicosPendientes();
        }
        else {
            return getWorkFlowServiceData().findAllActividadesPendientes(ticket, nombreActividad);
        }
    }

    /**
     * Regresa una lista de objetos ReversoVO que corresponden a los reversos que se encuentran
     * Pendientes de autorizar.
     *
     * @return List.
     * @ejbgen:remote-method transaction-attribute = Required
     * @ejbgen:local-method transaction-attribute = Required
     */
    public List findReversosPendientes() {
        List reversos = getReversosServiceData().findReversosPendientes();
        List resultados = new ArrayList();
        for (Iterator it = reversos.iterator(); it.hasNext();) {
            Reverso reverso = (Reverso) it.next();
            resultados.add(new ReversoVO(reverso));
        }
        return resultados;
    }

    /**
     * De acuerdo al tipo de actividad, llama alg&uacute;n mensaje del WorkFlowServiceData para
     * terminar la actividad y continuar con el flujo.
     *
     * @param ticket      El ticket de la sesi&oacute;n del usuario.
     * @param idActividad El identificador de la actividad que terminar&aacute;.
     * @param tipo        El tipo de actividad, de acuerdo a las constantes definidas en la clase
     *                    Actividad.
     * @param autorizado  Si el resultado es una autorizaci&oacute;n o una negaci&oacute;n de
     *                    autorizaci&oacute;n.
     * @param idUsuario   El n&uacute;mero de usuario que realiza la autorizaci&oacute;n o su
     *                    negaci&oacute;n.
     * @return List La lista de workitems sin completar.
     * @throws Exception Si ocurre alg&uacute;n error.
     * @ejbgen:remote-method transaction-attribute = Required
     * @ejbgen:local-method transaction-attribute = Required
     * @see com.ixe.ods.sica.sdo.WorkFlowServiceData
     */
    public List completarActividad(String ticket, int idActividad, String tipo, boolean autorizado,
                                   int idUsuario) throws Exception {
        try {
            IUsuario usuario = (IUsuario) getWorkFlowServiceData().find(Usuario.class,
                    new Integer(idUsuario));
            if (Actividad.ACT_DN_HORARIO.equals(tipo)) {
                if (autorizado) {
                    getWorkFlowServiceData().wfNormAutorizarHorario(ticket, idActividad, usuario);
                }
                else {
                    getWorkFlowServiceData().wfNormNegarHorario(ticket, idActividad, usuario);
                }
            }
            else if (Actividad.ACT_DN_MONTO.equals(tipo)) {
                if (autorizado) {
                    getWorkFlowServiceData().wfNormAutorizarMonto(ticket, idActividad, usuario);
                }
                else {
                    getWorkFlowServiceData().wfNormNegarMonto(ticket, idActividad, usuario);
                }
            }
            else if (Actividad.ACT_DN_NOT_DESV_TCC.equals(tipo)) {
                getWorkFlowServiceData().wfNormConfirmarDesvTcc(ticket, idActividad, usuario);
            }
            else if (Actividad.ACT_DN_SOBREPRECIO.equals(tipo)) {
                getWorkFlowServiceData().wfNormConfirmarSobreprecio(ticket, idActividad, usuario);
            }
            else if (Actividad.ACT_DNCA_ESP_AUT_MESA.equals(tipo)) {
                if (autorizado) {
                    getWorkFlowServiceData().wfAutorizarCancelacionParaDeal(ticket, idActividad,
                            usuario);
                }
                else {
                    getWorkFlowServiceData().wfNegarCancelacionParaDeal(ticket, idActividad,
                            usuario);
                }
            }
            else if (Actividad.ACT_DN_CANC_DET.equals(tipo)) {
                if (autorizado) {
                    getWorkFlowServiceData().wfAutorizarCancelacionParaDetalleDeal(ticket,
                            idActividad, usuario);
                }
                else {
                    getWorkFlowServiceData().wfNegarCancelacionParaDetalleDeal(ticket, idActividad,
                            usuario);
                }
            }
            else if (Actividad.ACT_DN_MOD_MONTO.equals(tipo)) {
            	if (autorizado) {
                    getWorkFlowServiceData().actualizarMontoParaDetalle(ticket,
                            idActividad, usuario);
                }
            	else {
            		getWorkFlowServiceData().negarActualizarMontoOProductoParaDetalle(ticket, 
            				idActividad, usuario);
            	}
            }
            else if (Actividad.ACT_DN_MOD_PROD.equals(tipo)) {
            	if (autorizado) {
            		getWorkFlowServiceData().actualizarProductoParaDetalle(ticket,
                            idActividad, usuario);
                }
            	else {
            		getWorkFlowServiceData().negarActualizarMontoOProductoParaDetalle(ticket, idActividad,
                            usuario);
            	}
            }
            else if (Actividad.ACT_DN_NOTIF_LIM.equals(tipo)) {
            	if (autorizado) {
            		getWorkFlowServiceData().actualizarActividadNotificacion(ticket, idActividad, usuario);
            	}
            }
            else if (Actividad.ACT_DN_DOCUMENTACION.equals(tipo) ||
                    Actividad.ACT_DINT_DOCUMENTACION.equals(tipo) ||
                    Actividad.ACT_DN_REC_M_CTRL.equals(tipo) ||
                    Actividad.ACT_DINT_REC_M_CTRL.equals(tipo)) {
                if (autorizado) {
                    getWorkFlowServiceData().wfAutorizarDocumentacion(ticket, idActividad, usuario);
                }
                else {
                    getWorkFlowServiceData().wfNegarDocumentacion(ticket, idActividad, usuario);
                }
            }
            else if (Actividad.ACT_DN_RFC.equals(tipo)) {
                if (autorizado) {
                    getWorkFlowServiceData().wfAutorizarRfc(ticket, idActividad, usuario);
                }
                else {
                    throw new SicaException("No est\u00e1 definida la negaci\u00f3n de la " +
                            "autorizaci\u00f3n por cambio en Correo Electr\u00f3nico.");
                }
            }
            else if (Actividad.ACT_DN_EMAIL.equals(tipo)) {
                if (autorizado) {
                    getWorkFlowServiceData().wfAutorizarEmail(ticket, idActividad, usuario);
                }
                else {
                    throw new SicaException("No est\u00e1 definida la negaci\u00f3n de la " +
                            "autorizaci\u00f3n por cambio en Correo Electr\u00f3nico.");
                }
            }
            else if (Actividad.ACT_DN_PLANTILLA.equals(tipo)){
    			if (autorizado) {
    				getWorkFlowServiceData().wfAutorizarPlantilla(ticket, idActividad, usuario);
    			}
    			else {
    				getWorkFlowServiceData().wfNegarPlantilla(ticket, idActividad, usuario);
    			}    			
    		}    		
            else if (Actividad.ACT_DN_RFC.equals(tipo)) {
                //todo: Implementar esto:
            }
            else if (Actividad.ACT_DN_PAGO_ANTICIPADO.equals(tipo) ||
                    Actividad.ACT_DINT_LINEA_CREDITO.equals(tipo)) {
                if (autorizado) {
                    getWorkFlowServiceData().wfAutorizarPagoAnticipado(ticket, idActividad,
                            usuario);
                }
                else {
                    getWorkFlowServiceData().wfNegarPagoAnticipado(ticket, idActividad, usuario);
                }
            }
            else if (Actividad.ACT_DINT_LINEA_OPERACION.equals(tipo)) {
                if (autorizado) {
                    getWorkFlowServiceData().wfIntAutorizarLineaOperacion(ticket, idActividad,
                            usuario);
                }
                else {
                    getWorkFlowServiceData().wfIntNegarAutorizacionLineaOperacion(ticket,
                            idActividad, usuario);
                }
            }
            else {
                throw new SicaException("No se pudo completar la operaci\u00f3n debido a que el " +
                        "tipo de actividad " + tipo + " es desconocido.");
            }
        }
        catch (SicaException e) {
            warn(e);
            e.printStackTrace();
            try {
            	getSessionContext().setRollbackOnly();
            }
            catch (IllegalStateException ise) {
            	
            }
            throw e;
        }
        catch (Exception e) {
            warn(e);
            e.printStackTrace();
            getSessionContext().setRollbackOnly();
            throw new Exception("Ocurri\u00f3 un error al tratar de completar la operaci\u00f3n. " +
                    "Favor de contactar al Area de Sistemas.");
        }
        return getWorkitems(ticket, tipo);
    }

    /**
     * Regresa la lista de objetos DealWorkitemVO que representan actividades pendientes de
     * completar del WorkFlow.
     *
     * @param ticket          El ticket de la sesi&oacute;n del usuario.
     * @param nombreActividad El nombre de la actividad a buscar (puede ser null).
     * @return List
     * @throws SeguridadException Si el ticket no es v&aacute;lido.
     * @ejbgen:remote-method transaction-attribute = Required
     * @ejbgen:local-method transaction-attribute = Required
     */
    public List getWorkitems(String ticket, String nombreActividad) throws SeguridadException {
        if ("Reversos".equals(nombreActividad)) {
            return getReversosServiceData().findReversosPendientes();
        }
        else if ("Confirmaciones".equals(nombreActividad)) {
            return getWorkFlowServiceData().findDealsInterbancariosPorConfirmar();
        }        
        else if ("Alta Email".equals(nombreActividad)) {
            return getWorkFlowServiceData().findAltaAnticipadaCorreosElectronicosPendientes();
        }
        else {
            return getWorkFlowServiceData().findAllActividadesPendientes(ticket, nombreActividad);
        }
    }

    /**
     * Regresa los totales por actividad de las solicitudes de autorizaci&oacute;n pendientes.
     *
     * @return List.
     * @ejbgen:remote-method transaction-attribute = Required
     * @ejbgen:local-method transaction-attribute = Required
     * @see com.ixe.ods.sica.sdo.WorkFlowServiceData#findActividadesPendientesTotales(boolean).
     */
    public List getTotales(boolean incluirConfDeals) {
        return getWorkFlowServiceData().findActividadesPendientesTotales(incluirConfDeals);
    }

    /**
     * Modifica el tipo de cambio de la mesa para el detalle de deal especificado.
     *
     * @param idDealDetalle El identificador del detalle de deal.
     * @param tcm           El tipo de cambio de la mesa que desea asignarse.
     * @throws SicaException Si no se encuentra el detalle del deal.
     * @ejbgen:remote-method transaction-attribute = Required
     * @ejbgen:local-method transaction-attribute = Required
     */
    public void modificarTcmParaDealDetalle(int idDealDetalle, double tcm) throws SicaException {
        if (Double.isNaN(tcm)) {
            throw new SicaException("El tipo de cambio de la mesa debe ser num\u00e9rico.");
        }
        getWorkFlowServiceData().registrarCambioTCMDealDet(idDealDetalle, tcm);
    }

    /**
     * @param ticket    El ticket de la sesi&oacute;n del usuario.
     * @param idReverso El n&uacute;mero del reverso a consultar.
     * @return List.
     * @throws SicaException Si el reverso no existe, no est&aacute; en status pendiente.
     * @ejbgen:remote-method transaction-attribute = Required
     * @ejbgen:local-method transaction-attribute = Required
     * @see ReversosServiceData#prepararDealsReverso(String, int).
     */
    public List prepararDealsReverso(String ticket, int idReverso) throws SicaException {
        try {
            return getReversosServiceData().prepararDealsReverso(ticket, idReverso);
        }
        catch (SicaException e) {
            warn(e);
            e.printStackTrace();
            throw e;
        }
        catch (Exception e) {
            warn(e);
            e.printStackTrace();
            throw new SicaException("No se pudo realizar la operaci\u00f3n. Intente de nuevo y si "
                    + "persiste el problema comun\u00edquese a sistemas por favor.");
        }
    }

    /**
     * @ejbgen:remote-method transaction-attribute = Required
     * @ejbgen:local-method transaction-attribute = Required
     * @see ReversosServiceData#autorizarReverso(String, com.ixe.ods.sica.vo.ReversoVO,
     *      java.util.List, int).
     */
    public String autorizarReverso(String ticket, ReversoVO reversoVO, List deals, int idUsuario)
            throws SicaException {
        try {
            return getReversosServiceData().autorizarReverso(ticket, reversoVO, deals, idUsuario);
        }
        catch (SicaException e) {
            if (logger.isWarnEnabled()) {
                logger.warn("Error al autorizar el reverso", e);
            }
            getSessionContext().setRollbackOnly();
            throw e;
        }
        catch (Exception e) {
            if (logger.isWarnEnabled()) {
                logger.warn("Error al autorizar el reverso", e);
            }
            getSessionContext().setRollbackOnly();
            throw new SicaException("Error al generar el reverso. Consulte a Sistemas por favor.");
        }
    }

    /**
     * @ejbgen:remote-method transaction-attribute = Required
     * @ejbgen:local-method transaction-attribute = Required
     * @see com.ixe.ods.sica.sdo.ReversosServiceData#negarReverso(String,
     *      com.ixe.ods.sica.vo.ReversoVO, int, boolean).
     */
    public String negarReverso(String ticket, ReversoVO reversoVO, int idUsuario,
                               boolean deniegaMesa) throws SicaException {
        try {
            return getReversosServiceData().negarReverso(ticket, reversoVO, idUsuario, deniegaMesa);
        }
        catch (SicaException e) {
            if (logger.isWarnEnabled()) {
                logger.warn("Error al negar la autorizaci\u00f3n del reverso", e);
            }
            getSessionContext().setRollbackOnly();
            throw e;
        }
        catch (Exception e) {
            if (logger.isWarnEnabled()) {
                logger.warn("Error al negar la autorizaci\u00f3n del reverso", e);
            }
            getSessionContext().setRollbackOnly();
            throw new SicaException("No se pudo realizar la operaci\u00f3n. Intente de nuevo y si" +
                    "persiste el problema comun\u00edquese a sistemas por favor.");
        }
    }

    /**
     * @param ticket             El ticket de la sesi&oacute;n del usuario.
     * @param detOrig            El detalle del deal a modificar.
     * @param mnemonicoReemplazo El mnem&oacute;nico a asignar al nuevo detalle.
     * @param usuario            El usuario que solicita la modificaci&oacute;n del detalle.
     * @throws SicaException Si el deal ya estaba cancelado o no, si no se puede crear el nuevo
     *      detalle de deal (si es necesario) o si no se pueden terminar las actividades
     * @ejbgen:remote-method transaction-attribute = Required
     * @ejbgen:local-method transaction-attribute = Required
     */
    public void solicitarModificacionDetalle(String ticket, DealDetalle detOrig,
                                             String mnemonicoReemplazo, IUsuario usuario)
            throws SicaException {
        try {
            getWorkFlowServiceData().solicitarModificacionDetalle(ticket, detOrig,
                    mnemonicoReemplazo, usuario);
        }
        catch (SicaException e) {
            warn(e);
            getSessionContext().setRollbackOnly();
            throw e;
        }
        catch (Exception e) {
            warn(e);
            getSessionContext().setRollbackOnly();
            throw new SicaException("Error al solicitar la modificaci\u00f3n del detalle "
                    + detOrig.getIdDealPosicion() + " del deal " + detOrig.getDeal().getIdDeal());
        }
    }

    /**
     * <ul>
     * <li>Pone en solicitud el evento de cancelaci&oacute;n del deal.</li>
     * <li>Si el deal no se encuentra en proceso de captura, llama a terminarActividadesParaDeal()
     * para interrumpir el workflow actual del deal.</li>
     * <li>Si ya se ha solicitado la liquidaci&oacute;n al &aacute;rea de tesorer&iacute;a, se
     * solicita la cancelaci&oacute;n total al SITE y se crea la actividad para esperar la respuesta
     * de tesorer&iacute;a.</li>
     * <li>Si no se ha solicitado a&uacute;n la liquidaci&oacute;n, se marca el evento del deal de
     * cancelaci&oacute;n como autorizado por tesorer&iacute;a y se llama a
     * <code>wfCanAutorizadaPorTesoreria()</code> que genera la actividad de autorizaci&oacute;n de
     * la solicitud de cancelaci&oacute;n a la mesa de cambios.
     * </ul>
     *
     * @param ticket  El ticket de la sesi&oacute;n del usuario.
     * @param idDeal  El n&uacute;mero de deal del que se solicita la cancelaci&oacute;n.
     * @param usuario El usuario que solicita la cancelaci&oacute;n del deal.
     * @throws SicaException Si ocurre alg&uacute;n error.
     * @ejbgen:remote-method transaction-attribute = Required
     * @ejbgen:local-method transaction-attribute = Required
     * @see com.ixe.ods.sica.sdo.WorkFlowServiceData#terminarActividadesParaDeal(
     *com.ixe.ods.sica.model.Deal, String, com.ixe.ods.seguridad.model.IUsuario).
     */
    public void wfSolicitarCancelacionDeal(String ticket, int idDeal, IUsuario usuario)
            throws SicaException {
        try {
            getWorkFlowServiceData().wfSolicitarCancelacionDeal(ticket, idDeal, usuario);
        }
        catch (SicaException e) {
            warn(e);
            getSessionContext().setRollbackOnly();
            throw e;
        }
        catch (Exception e) {
            warn(e);
            getSessionContext().setRollbackOnly();
            throw new SicaException("Error al solicitar la cancelaci\u00f3n del deal " + idDeal);
        }
    }

    /**
     * Actualiza el deal en la base de datos y llama al servicio modificaSolicitud() del EJB del
     * SITE para reflejar los cambios a las observaciones, pago anticipado, toma en firme,
     * facturaci&oacute;n y mensajer&iacute;a en el sistema de tesorer&iacute;a.
     *
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param deal   El deal a actualizar.
     * @throws SicaException Si no se pueden actualizar los datos en el SITE.
     * @ejbgen:remote-method transaction-attribute = Required
     * @ejbgen:local-method transaction-attribute = Required
     */
    public void actualizarDatosDeal(String ticket, Deal deal) throws SicaException {
        try {
            getWorkFlowServiceData().actualizarDatosDeal(ticket, deal);
        }
        catch (SicaException e) {
            warn(e);
            getSessionContext().setRollbackOnly();
            throw e;
        }
        catch (Exception e) {
            warn(e);
            getSessionContext().setRollbackOnly();
            throw new SicaException("Error al actualizar los datos del deal " + deal.getIdDeal());
        }
    }

    /**
     * @ejbgen:remote-method transaction-attribute = Required
     * @ejbgen:local-method transaction-attribute = Required
     */
    public void hacerSplitDetalle(String ticket, DealDetalle detOrig, String mnemonicoSegundo,
                                  double montoSegundo, IUsuario usuario) throws SicaException {
        try {
            getWorkFlowServiceData().hacerSplitDetalle(ticket, detOrig, mnemonicoSegundo,
                    montoSegundo, usuario);
        }
        catch (SicaException e) {
            warn(e);
            getSessionContext().setRollbackOnly();
            throw e;
        }
        catch (Exception e) {
            warn(e);
            getSessionContext().setRollbackOnly();
            throw new SicaException("Error al realizar el split del detalle "
                    + detOrig.getIdDealPosicion() + " del deal " + detOrig.getDeal().getIdDeal());
        }
    }

    /**
     * Debe llamarse cuando en el front-end modifican los checkboxes de pago anticipado o toma en
     * firme. El deal no puede tener una negaci&oacute;n de autorizaci&oacute;n previa por ese mismo
     * concepto.
     * Si lo que se desea es quitar la opci&oacute;n de pago anticipado o toma en firme, no puede
     * haber ning&uacute;n detalle que haya hecho uso de una l&iacute;nea de cr&eacute;dito.
     *
     * @param ticket  El ticket de la sesi&oacute;n del usuario.
     * @param deal    El deal a marcar con pago anticipado o toma en firme.
     * @param usuario El usuario que solicita la operaci&oacute;n.
     * @param pagAnt  True para pago anticipado, False para toma en firme.
     * @throws SicaException Si no se puede marcar el deal.
     * @ejbgen:remote-method transaction-attribute = Required
     * @ejbgen:local-method transaction-attribute = Required
     */
    public void marcarPagAntTomaEnFirme(String ticket, Deal deal, IUsuario usuario, boolean pagAnt)
            throws SicaException {
        try {
            getWorkFlowServiceData().marcarPagAntTomaEnFirme(ticket, deal, usuario, pagAnt);
        }
        catch (SicaException e) {
            warn(e);
            getSessionContext().setRollbackOnly();
            throw e;
        }
        catch (Exception e) {
            warn(e);
            getSessionContext().setRollbackOnly();
            throw new SicaException("Error al marcar pago anticipado o toma en firme al deal "
                    + deal.getIdDeal());
        }
    }

    /**
     * Inicia un nuevo 'workflow' para procesar deals interbancarios o normales. Cambia el status
     * del deal y sus detalles a Completo y limpia el status del evento de modificaci&oacute;n.
     * Env&iacute; la alerta de Alta de deal.
     * Si el deal es interbancario:
     * <li>Si se requiere autorizaci&oacute;n por documentaci&oacute;n, crea la actividad
     * correspondiente, en caso contrario debe continuar el flujo, evaluando si se requiere o no
     * autorizaci&oacute;n por l&iacute;nea de operaci&oacute;n.</li>
     * Si el deal es normal:
     * <li>Si se requiere autorizaci&oacute;n por horario, crea la actividad correspondiente, en
     * caso contrario debe continuar el flujo, evaluando si se requiere o no autorizaci&oacute;n por
     * monto.</li>
     *
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param idDeal El deal a procesar.
     * @throws SicaException Si ocurre alg&uacute;n error.
     * @ejbgen:remote-method transaction-attribute = Required
     * @ejbgen:local-method transaction-attribute = Required
     */
    public void wfIniciarProcesoDeal(String ticket, int idDeal) throws SicaException {
        try {
            getWorkFlowServiceData().wfIniciarProcesoDeal(ticket, idDeal);
        }
        catch (SicaException e) {
            warn(e);
            /*if (!getSessionContext().getRollbackOnly()) {
                getSessionContext().setRollbackOnly();
            } */
            throw e;
        }
        catch (Exception e) {
            e.printStackTrace();
            warn(e);
            /*if (!getSessionContext().getRollbackOnly()) {
                getSessionContext().setRollbackOnly();
            } */
            String mensaje = "";
            if(e.getMessage().startsWith("ExternalSiteException")){
            	mensaje = e.getMessage().substring(21);
            }
            throw new SicaException("Error al iniciar el procesamiento del deal " + idDeal + " " + mensaje);
        }
    }

    /**
     * Extrae los deals en status 'Pendiente'. Para cada uno afecta la posici&oacute;n, revisa si es
     * necesario enviar a banxico y posteriormente genera una orden de liquidaci&oacute;n para cada
     * uno de los deals, tambi&eacute;n en caso de ser necesario.
     *
     * @param ticket El ticket de la sesi&oacute;n.
     * @throws SicaException Si el estado del sistema no es v&aacute;lido.
     * @ejbgen:remote-method transaction-attribute = Required
     * @ejbgen:local-method transaction-attribute = Required
     */
    public void wfIniciarProcesoDealsPendientes(String ticket) throws SicaException {
        try {
            getWorkFlowServiceData().wfIniciarProcesoDealsPendientes(ticket);
        }
        catch (SicaException e) {
            warn(e);
            if (!getSessionContext().getRollbackOnly()) {
                getSessionContext().setRollbackOnly();
            }
            throw e;
        }
        catch (Exception e) {
            warn(e);
            if (!getSessionContext().getRollbackOnly()) {
                getSessionContext().setRollbackOnly();
            }
            throw new SicaException("Error al iniciar el procesamiento de deals pendientes.");
        }
    }

    /**
     * <p>Itera todos los detalles del deal y llama a marcarDealDetalleCancelado(). Si el deal es
     * interbancario, y tiene un swap asignado:</p>
     * <ul>
     * <li>Es inicio del swap.- Se marca como cancelado el swap.</li>
     * <li>Es contraparte del swap.- Actualiza el registro relacionado de BitacoraEnviadas. Se
     * decrementa el monto asignado y si este se vuelve cero, pone en <i>IN</i> el status del
     * swap, en caso contrario, pone el status en <i>PA</i>.
     * </li>
     * </ul>
     *
     * @param idDeal El deal a cancelar.
     * @throws SicaException Si no se puede calcular la fecha valor de la cancelaci&ocute;n.
     * @ejbgen:remote-method transaction-attribute = Required
     * @ejbgen:local-method transaction-attribute = Required
     * @see com.ixe.ods.sica.sdo.WorkFlowServiceData#marcarDealCancelado(int).
     */
    public void marcarDealCancelado(int idDeal) throws SicaException {
        try {
            getWorkFlowServiceData().marcarDealCancelado(idDeal);
        }
        catch (SicaException e) {
            warn(e);
            getSessionContext().setRollbackOnly();
            throw e;
        }
        catch (Exception e) {
            warn(e);
            getSessionContext().setRollbackOnly();
            throw new SicaException("Error al marcar el deal " + idDeal + " como cancelado.");
        }
    }

    /**
     * Asigna los datosAdicionales al reverso especificado y actualiza el registro.
     *
     * @param idReverso        El n&uacute;mero de reverso a buscar.
     * @param datosAdicionales El valor de los datos adicionales a asignar al reverso.
     * @ejbgen:remote-method transaction-attribute = Required
     * @ejbgen:local-method transaction-attribute = Required
     */
    public void configurarDatosAdicionalesReverso(int idReverso, String datosAdicionales) {
        getWorkFlowServiceData().configurarDatosAdicionalesReverso(idReverso, datosAdicionales);
    }

    /**
     * Autoriza o niega el alta del nuevo correo para facturaci&oacute;n electr&oacute;nica, creando
     * el registro en la BUP para el caso de la autorizaci&oacute;n.
     *
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param idAutMedioContacto El n&uacute;mero de solicitud de alta de Medio Contacto.
     * @param idUsuario El identificador del usuario que autoriza o niega.
     * @param autorizar True para autorizar, False para negar la autorizaci&oacute;n.
     * @return List de workitems para la misma actividad.
     * @throws SicaException Si ocurre alg&uacute;n problema.
     * @ejbgen:remote-method transaction-attribute = Required
     * @ejbgen:local-method transaction-attribute = Required
     */
    public List completarAltaAntEmail(String ticket, int idAutMedioContacto, int idUsuario,
                               boolean autorizar) throws SicaException {
        return getWorkFlowServiceData().completarAltaAntEmail(ticket, idAutMedioContacto, idUsuario,
                autorizar);
    }

    /**
     *
     * @ejbgen:remote-method transaction-attribute = Required
     * @ejbgen:local-method transaction-attribute = Required
     */
    public List confirmarDealsInterbancarios(String ticket, Integer[] arrDealPosicion, String contacto,
                                           int idUsuario) throws SicaException {
    	
    	for(int i = 0; i < arrDealPosicion.length; i++){
    		getWorkFlowServiceData().confirmarDealInterbancario(ticket, arrDealPosicion[i].intValue(), contacto,
                idUsuario);
    }

        return new ArrayList();
    }

    /**
     * Regresa el valor de reversosServiceData.
     *
     * @return ReversosServiceData.
     */
    private ReversosServiceData getReversosServiceData() {
        return (ReversosServiceData) getBeanFactory().getBean("reversosServiceData");
    }

    /**
     * Regresa el valor de workFlowServiceData.
     *
     * @return WorkFlowServiceData.
     */
    private WorkFlowServiceData getWorkFlowServiceData() {
        return (WorkFlowServiceData) getBeanFactory().getBean("sicaWorkFlowServiceData");
    }

    /**
     * Si el logger tiene habilitado la opci&oacute;n de WARN, hace el warn del objeto.
     *
     * @param o El objeto a loggear.
     */
    public void warn(Object o) {
        if (logger.isWarnEnabled()) {
            logger.warn(o);
        }
    }

    /**
     * El UID para serializaci&oacute;n.
     */
    private static final long serialVersionUID = 1861012005229806267L;
}