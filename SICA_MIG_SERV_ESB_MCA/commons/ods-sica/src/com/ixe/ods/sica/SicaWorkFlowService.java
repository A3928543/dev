/*
 * $Id: SicaWorkFlowService.java,v 1.7.12.1 2010/06/16 18:09:09 galiciad Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2007 - 2009 LegoSoft S.C.
 */

package com.ixe.ods.sica;

import com.ixe.ods.seguridad.model.IUsuario;
import com.ixe.ods.seguridad.model.SeguridadException;
import com.ixe.ods.sica.model.Deal;
import com.ixe.ods.sica.model.DealDetalle;
import com.ixe.ods.sica.vo.ReversoVO;
import java.util.List;

/**
 * Business Interface del Session Bean SicaWorkFlowSessionBean.
 *
 * @author Jean C. Favila
 * @version $Revision: 1.7.12.1 $ $Date: 2010/06/16 18:09:09 $
 */
public interface SicaWorkFlowService {

    /**
     * Regresa una lista de objetos DealWorkitemVO con las actividades marcadas como 'Pendientes'.
     * No se incluyen las actividades de liberaci&oacute;n de l&iacute;neas de cr&eacute;dito, ni
     * las autorizaciones de horario o monto.
     *
     * @param ticket El ticket de la sesi&ocute;n.
     * @param nombreActividad El nombre de la actividad (opcional, puede ser null).
     * @return String.
     * @throws com.ixe.ods.seguridad.model.SeguridadException Si el ticket no es v&aacute;lido.
     */
    List findAllActividadesPendientes(String ticket, String nombreActividad)
            throws SeguridadException;

    /**
     * Regresa una lista de objetos ReversoVO que corresponden a los reversos que se encuentran
     * Pendientes de autorizar.
     *
     * @return List.
     */
    List findReversosPendientes();

    /**
     * De acuerdo al tipo de actividad, llama alg&uacute;n mensaje del WorkFlowServiceData para
     * terminar la actividad y continuar con el flujo.
     *
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param idActividad El identificador de la actividad que terminar&aacute;.
     * @param tipo El tipo de actividad, de acuerdo a las constantes definidas en la clase
     *  Actividad.
     * @param autorizado Si el resultado es una autorizaci&oacute;n o una negaci&oacute;n de
     *  autorizaci&oacute;n.
     * @param idUsuario El n&uacute;mero de usuario que realiza la autorizaci&oacute;n o su
     *  negaci&oacute;n.
     * @return List La lista de workitems sin completar.
     * @throws Exception Si ocurre alg&uacute;n error.
     * @see com.ixe.ods.sica.sdo.WorkFlowServiceData
     */
    List completarActividad(String ticket, int idActividad, String tipo, boolean autorizado,
                                   int idUsuario) throws Exception;

    /**
     * Regresa la lista de objetos DealWorkitemVO que representan actividades pendientes de
     * completar del WorkFlow.
     *
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param nombreActividad El nombre de la actividad a buscar (puede ser null).
     * @return List
     * @throws SeguridadException Si el ticket no es v&aacute;lido.
     */
    List getWorkitems(String ticket, String nombreActividad) throws SeguridadException;

    /**
     * Regresa los totales por actividad de las solicitudes de autorizaci&oacute;n pendientes.
     *
     * @return List.
     * @see com.ixe.ods.sica.sdo.WorkFlowServiceData#findActividadesPendientesTotales(boolean).
     */
    List getTotales(boolean incluirConfDeals);

    /**
     * Modifica el tipo de cambio de la mesa para el detalle de deal especificado.
     *
     * @param idDealDetalle El identificador del detalle de deal.
     * @param tcm El tipo de cambio de la mesa que desea asignarse.
     * @throws SicaException Si no se encuentra el detalle del deal.
     */
    void modificarTcmParaDealDetalle(int idDealDetalle, double tcm) throws SicaException;

    /**
     * @see com.ixe.ods.sica.sdo.ReversosServiceData#prepararDealsReverso(String, int).
     */
    List prepararDealsReverso(String ticket, int idReverso) throws SicaException;


    /**
     * Marca el reverso como autorizado, genera los deals de balanceo y correcci&oacute;n y
     * env&iacute;a un correo electr&oacute;nico a los involucrados con la informaci&oacute;n del
     * reverso.
     *
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param reversoVO El value object del reverso a autorizar.
     * @param deals La lista de deals (original, balanceo y correcci&oacute;n.
     * @param idUsuario El usuario que autoriza el reverso.
     * @return String El mensaje con el resultado de la autorizaci&oacute;n.
     * @throws SicaException Si ocurre alg&uacute;n error.
     */
    String autorizarReverso(String ticket, ReversoVO reversoVO, List deals, int idUsuario)
            throws SicaException;

    /**
     * Marca el reverso como no autorizado, desbloquea la liquidaci&oacute;n del deal original y
     * env&iacute;a un correo electr&oacute;nico al promotor informando sobre la cancelaci&oacute;n
     * del reverso.
     *
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param reversoVO El value object del reverso a autorizar.
     * @param idUsuario El usuario que autoriza el reverso.
     * @return String El mensaje con el resultado de la autorizaci&oacute;n.
     * @throws SicaException Si ocurre alg&uacute;n error.
     */
    String negarReverso(String ticket, ReversoVO reversoVO, int idUsuario, boolean deniegaMesa) 
    		throws SicaException;

    /**
     *
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param detOrig El detalle del deal a modificar.
     * @param mnemonicoReemplazo El mnem&oacute;nico a asignar al nuevo detalle.
     * @param usuario El usuario que solicita la modificaci&oacute;n del detalle.
     * @throws SicaException Si el deal ya estaba cancelado o no, si no se puede crear el nuevo
     *              detalle de deal (si es necesario) o si no se pueden terminar las actividades
     */
    void solicitarModificacionDetalle(String ticket, DealDetalle detOrig, String mnemonicoReemplazo,
                                      IUsuario usuario) throws SicaException;

    /**
     * <ul>
     *   <li>Pone en solicitud el evento de cancelaci&oacute;n del deal.</li>
     *   <li>Si el deal no se encuentra en proceso de captura, llama a terminarActividadesParaDeal()
     * para interrumpir el workflow actual del deal.</li>
     *   <li>Si ya se ha solicitado la liquidaci&oacute;n al &aacute;rea de tesorer&iacute;a, se
     * solicita la cancelaci&oacute;n total al SITE y se crea la actividad para esperar la respuesta
     * de tesorer&iacute;a.</li>
     *   <li>Si no se ha solicitado a&uacute;n la liquidaci&oacute;n, se marca el evento del deal de
     * cancelaci&oacute;n como autorizado por tesorer&iacute;a y se llama a
     * <code>wfCanAutorizadaPorTesoreria()</code> que genera la actividad de autorizaci&oacute;n de
     * la solicitud de cancelaci&oacute;n a la mesa de cambios.
     * </ul>
     *
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param idDeal El n&uacute;mero de deal del que se solicita la cancelaci&oacute;n.
     * @param usuario El usuario que solicita la cancelaci&oacute;n del deal.
     * @throws SicaException Si ocurre alg&uacute;n error.
     * @see com.ixe.ods.sica.sdo.WorkFlowServiceData#terminarActividadesParaDeal(
                com.ixe.ods.sica.model.Deal, String, com.ixe.ods.seguridad.model.IUsuario).
     */
    void wfSolicitarCancelacionDeal(String ticket, int idDeal, IUsuario usuario)
            throws SicaException;

    /**
     * Actualiza el deal en la base de datos y llama al servicio modificaSolicitud() del EJB del
     * SITE para reflejar los cambios a las observaciones, pago anticipado, toma en firme,
     * facturaci&oacute;n y mensajer&iacute;a en el sistema de tesorer&iacute;a.
     *
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param deal El deal a actualizar.
     * @throws SicaException Si no se pueden actualizar los datos en el SITE.
     */
    void actualizarDatosDeal(String ticket, Deal deal) throws SicaException;

    void hacerSplitDetalle(String ticket, DealDetalle detOrig, String mnemonicoSegundo,
                           double montoSegundo, IUsuario usuario) throws SicaException;

    /**
     * Debe llamarse cuando en el front-end modifican los checkboxes de pago anticipado o toma en
     * firme. El deal no puede tener una negaci&oacute;n de autorizaci&oacute;n previa por ese mismo
     * concepto.
     * Si lo que se desea es quitar la opci&oacute;n de pago anticipado o toma en firme, no puede
     * haber ning&uacute;n detalle que haya hecho uso de una l&iacute;nea de cr&eacute;dito.
     *
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param deal El deal a marcar con pago anticipado o toma en firme.
     * @param usuario El usuario que solicita la operaci&oacute;n.
     * @param pagAnt True para pago anticipado, False para toma en firme.
     * @throws SicaException Si no se puede marcar el deal.
     */
    void marcarPagAntTomaEnFirme(String ticket, Deal deal, IUsuario usuario, boolean pagAnt)
            throws SicaException;

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
     */
    void wfIniciarProcesoDeal(String ticket, int idDeal) throws SicaException;

    /**
     * Extrae los deals en status 'Pendiente'. Para cada uno afecta la posici&oacute;n, revisa si es
     * necesario enviar a banxico y posteriormente genera una orden de liquidaci&oacute;n para cada
     * uno de los deals, tambi&eacute;n en caso de ser necesario.
     *
     * @param ticket El ticket de la sesi&oacute;n.
     * @throws SicaException Si el estado del sistema no es v&aacute;lido.
     */
    void wfIniciarProcesoDealsPendientes(String ticket) throws SicaException;

    /**
     * <p>Itera todos los detalles del deal y llama a marcarDealDetalleCancelado(). Si el deal es
     * interbancario, y tiene un swap asignado:</p>
     * <ul>
     *  <li>Es inicio del swap.- Se marca como cancelado el swap.</li>
     *  <li>Es contraparte del swap.- Actualiza el registro relacionado de BitacoraEnviadas. Se
     *      decrementa el monto asignado y si este se vuelve cero, pone en <i>IN</i> el status del
     *      swap, en caso contrario, pone el status en <i>PA</i>.
     *  </li>
     * </ul>
     *
     * @param idDeal El deal a cancelar.
     * @see com.ixe.ods.sica.sdo.WorkFlowServiceData#marcarDealDetalleCancelado(
        com.ixe.ods.sica.model.DealDetalle).
     * @throws SicaException Si no se puede calcular la fecha valor de la cancelaci&ocute;n.
     */
    void marcarDealCancelado(int idDeal) throws SicaException;

    /**
     * Asigna los datosAdicionales al reverso especificado y actualiza el registro.
     *
     * @param idReverso El n&uacute;mero de reverso a buscar.
     * @param datosAdicionales El valor de los datos adicionales a asignar al reverso.
     */
    void configurarDatosAdicionalesReverso(int idReverso, String datosAdicionales);

    /**
     * Marca los detalles de los deals interbancarios como confirmado por la mesa de control.
     *
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param arrDealPosicion Arreglo de los n&uacute;meros de detalles de deals.
     * @param contacto El nombre de la persona a quien se contact&oacute;n.
     * @param idUsuario El identificador del usuario que confirm&oacute; la operaci&oacute;n.
     * @return List de workitems para la misma actividad.
     * @throws SicaException Si ocurre alg&uacute;n problema.
     */
    List confirmarDealsInterbancarios(String ticket, Integer[] arrDealPosicion, String contacto,
                                    int idUsuario) throws SicaException;

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
     */
    List completarAltaAntEmail(String ticket, int idAutMedioContacto, int idUsuario,
                               boolean autorizar) throws SicaException;
}
