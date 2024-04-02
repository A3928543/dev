/*
 * $Id: WorkFlowServiceData.java,v 1.42.2.3.6.1.38.1.12.1 2016/07/13 21:43:04 mejiar Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2006 - 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica.sdo;

import java.util.List;

import com.ixe.ods.seguridad.model.IUsuario;
import com.ixe.ods.seguridad.model.SeguridadException;
import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.model.Deal;
import com.ixe.ods.sica.model.DealDetalle;
import com.ixe.ods.sica.model.Swap;

/**
 * Service data para las operaciones que tienen que tienen que ver con el workflow de procesamiento
 * y cancelaci&oacute;n de deals tanto normales, como interbancarios.
 *
 * @author Jean C. Favila
 * @version $Revision: 1.42.2.3.6.1.38.1.12.1 $ $Date: 2016/07/13 21:43:04 $
 */
public interface WorkFlowServiceData extends SicaServiceData {

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

    /**
     * Regresa una lista de objetos DealWorkitemVO con las actividades marcadas como 'Pendientes'.
     * No se incluyen las actividades de liberaci&oacute;n de l&iacute;neas de cr&eacute;dito, ni
     * las autorizaciones de horario o monto.
     *
     * @param ticket El ticket de la sesi&ocute;n.
     * @param nombreActividad El nombre de la actividad (opcional, puede ser null).
     * @return String.
     * @throws SeguridadException Si el ticket no es v&aacute;lido.
     */
    List findAllActividadesPendientes(String ticket, String nombreActividad)
            throws SeguridadException;

    /**
     * Regresa los registros de la tabla SC_ACTIVIDAD que corresponden al nombre de actividad
     * especificado y se encuentran en status 'Pendiente'. Se inicializan las relaciones con deal y
     * usuario.
     *
     * @param nombreActividad El nombre de la actividad a buscar.
     * @return List.
     */
    List findActividadesPendientes(String nombreActividad);

    /**
     * Regresa una lista de objetos WorklistTotalVO con el total de elementos de cada solicitud de
     * autorizaci&oacute;n.
     *
     * @param incluirConfDeals Si se desea incluir la lista de deals interb. por confirmar.
     * @return List.
     * @see com.ixe.ods.sica.vo.WorklistTotalVO
     */
    List findActividadesPendientesTotales(boolean incluirConfDeals);

    List findDealsInterbancariosPorConfirmar();
    
    /**
     * Confirma un deal interbancario por parte de la
     * mesa de control registrado el contacto con el cual se
     * confirmo la operaci&oacute;n as&iacute; como el usuario
     * que realiza la confirmaci&oacute;n.
     * 
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param idDealPosicion El id del detalle por confirmar
     * @param contacto El nombre del contacto con el que se verifico la operaci&oacute;n
     * @param idUsuario El usuario que confirma el deal.
     * @return
     * @throws SicaException
     */
    List confirmarDealInterbancario(String ticket, int idDealPosicion, String contacto,
                                           int idUsuario) throws SicaException;

    List findAltaAnticipadaCorreosElectronicosPendientes();

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
     * Permite marcar como autorizada la actividad de autorizaci&oacute;n por horario de un deal
     * normal. Termina la actividad y debe determinar si es o no necesaria la autorizaci&oacute;n
     * por monto.
     *
     * @param ticket El ticket de la sesi&oacute;n.
     * @param idActividad El n&uacute;mero de la actividad de solicitud de autorizaci&oacute;n por
     * horario.
     * @param usuario El usuario que realiza la autorizaci&oacute;n.
     * @throws SicaException Si algo sale mal.
     */
    void wfNormAutorizarHorario(String ticket, int idActividad, IUsuario usuario)
            throws SicaException;

    /**
     * Permite marcar como no autorizada la actividad de autorizaci&oacute;n por horario de un deal
     * normal. Termina la actividad y debe marcar como cancelados el deal completo y liberar las
     * l&iacute;neas de cr&eacute;dito utilizadas.
     *
     * @param ticket El ticket de la sesi&oacute;n.
     * @param idActividad El n&uacute;mero de la actividad de solicitud de autorizaci&oacute;n por
     *  horario.
     * @param usuario El usuario que realiza la negaci&oacute;n de autorizaci&oacute;n.
     * @throws SicaException Si algo sale mal.
     */
    void wfNormNegarHorario(String ticket, int idActividad, IUsuario usuario) throws SicaException;

    /**
     * Permite marcar como autorizada la actividad de autorizaci&oacute;n por monto de un deal
     * normal. Termina la actividad y debe determinar si es o no necesaria la autorizaci&oacute;n
     * por desviaci&oacute;n en el tipo de cambio al cliente.
     *
     * @param ticket El ticket de la sesi&oacute;n.
     * @param idActividad El n&uacute;mero de la actividad de solicitud de autorizaci&oacute;n por
     *  monto.
     * @param usuario El usuario que realiza la autorizaci&oacute;n.
     * @throws SicaException Si algo sale mal.
     */
    void wfNormAutorizarMonto(String ticket, int idActividad, IUsuario usuario)
            throws SicaException;

    /**
     * Permite marcar como no autorizada la actividad de autorizaci&oacute;n por monto de un deal
     * normal. Termina la actividad y debe marcar como cancelados el deal completo y liberar las
     * l&iacute;neas de cr&eacute;dito utilizadas.
     *
     * @param ticket El ticket de la sesi&oacute;n.
     * @param idActividad El n&uacute;mero de la actividad de solicitud de autorizaci&oacute;n por
     *  monto.
     * @param usuario El usuario que realiza la negaci&oacute;n de autorizaci&oacute;n.
     * @throws SicaException Si algo sale mal.
     */
    void wfNormNegarMonto(String ticket, int idActividad, IUsuario usuario) throws SicaException;

    /**
     * Permite al usuario de la mesa marcar como notificada la desviaci&oacute;n al tipo de cambio
     * del cliente para un deal normal. Termina la actividad y debe revisar si es o no necesario
     * notificar a banxico sobre este deal, y revisar si el cliente se ha vuelto frecuente o no.
     *
     * @param ticket El ticket de la sesi&oacute;n.
     * @param idActividad El n&uacute;mero de actividad de la notificaci&oacute;n.
     * @param usuario El usuario que realiza la confirmaci&oacute; de la notificaci&oacute;n.
     * @throws SicaException Si algo sale mal.
     */
    void wfNormConfirmarDesvTcc(String ticket, int idActividad, IUsuario usuario)
            throws SicaException;

    /**
     * Marca como aprobado el evento de sobreprecio del deal que corresponde a la actividad
     * especificada.
     *
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param idActividad El n&uacute;mero de la actividad.
     * @param usuario El Usuario que realiza la confimaci&oacute;n.
     * @throws SicaException Si ocurre alg&uacute;n error.
     */
    void wfNormConfirmarSobreprecio(String ticket, int idActividad, IUsuario usuario)
            throws SicaException;

    /**
     * Marca el encabezado del deal como aprobado en el &iacute;ndice de documentaci&oacute;n y
     * termina la actividad. El flujo debe continuar con la evaluaci&oacute;n de l&iacute;nea de
     * operaci&oacute;n en el caso de deals interbancarios, o con la evaluaci&oacute;n de pago
     * anticipado en el caso de deals normales.
     *
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param idActividad El identificador de la actividad.
     * @param usuario El usuario que autoriza la documentaci&oacute;n.
     * @throws SicaException Si ocurre alg&uacute;n error.
     */
    void wfAutorizarDocumentacion(String ticket, int idActividad, IUsuario usuario)
            throws SicaException;

    /**
     * Marca el encabezado del deal como aprobado en el &iacute;ndice de plantilla y
     * termina la actividad. El flujo debe continuar con la evaluaci&oacute;n de l&iacute;nea de
     * operaci&oacute;n en el caso de deals interbancarios, o con la evaluaci&oacute;n de pago
     * anticipado en el caso de deals normales.
     *
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param idActividad El identificador de la actividad.
     * @param usuario El usuario que autoriza la documentaci&oacute;n.
     * @throws SicaException Si ocurre alg&uacute;n error.
     */
    void wfAutorizarPlantilla(String ticket, int idActividad, IUsuario usuario)
    throws SicaException;

    /**
     * Marca el encabezado del deal como negada la autorizaci&oacute;n por falta de
     * documentaci&oacute;n y crea una actividad de deals rechazados por falta de
     * documentaci&oacute;n. Termina la actividad actual.
     *
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param idActividad El identificador de la actividad.
     * @param usuario El usuario que autoriza la documentaci&oacute;n.
     * @throws SicaException Si ocurre alg&uacute;n error.
     */
    void wfNegarDocumentacion(String ticket, int idActividad, IUsuario usuario)
            throws SicaException;

    /**
     * Marca el encabezado del deal como negada la autorizaci&oacute;n por pantilla
     * pendiente de autorizacion y crea una actividad de deals rechazados por
	 * pantilla. Termina la actividad actual.
     *
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param idActividad El identificador de la actividad.
     * @param usuario El usuario que autoriza la documentaci&oacute;n.
     * @throws SicaException Si ocurre alg&uacute;n error.
     */
    void wfNegarPlantilla(String ticket, int idActividad, IUsuario usuario)
    throws SicaException;

    void wfAutorizarRfc(String ticket, int idActividad, IUsuario usuario)
            throws SicaException;

    void wfAutorizarEmail(String ticket, int idActividad, IUsuario usuario)
            throws SicaException;
    
    /**
     * Autoriza el pago anticipado del deal, realizando el uso de la l&iacute;nea de cr&eacute;dito.
     * Marca como terminada la actividad de autorizaci&oacute;n de pago anticipado. Si el deal es
     * interbancario, debe proseguir con la evaluaci&oacute;n de banxico. En caso contrario, debe
     * evaluarse si el deal requiere autorizaci&oacute;n por toma en firme.
     *
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param idActividad El id de la actividad.
     * @param usuario El Usuario conectado a la aplicaci&oacute;n.
     * @throws SicaException Si no se puede terminar la actividad.
     */
    void wfAutorizarPagoAnticipado(String ticket, int idActividad, IUsuario usuario)
            throws SicaException;

    /**
     * Niega el pago anticipado del deal y marca como terminada la actividad de autorizaci&oacute;n
     * de pago anticipado. Si el deal es interbancario, debe proseguir con la evaluaci&oacute;n de
     * banxico. En caso contrario, debe evaluarse si el deal requiere autorizaci&oacute;n por toma
     * en firme.
     *
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param idActividad El id de la actividad.
     * @param usuario El Usuario conectado a la aplicaci&oacute;n.
     * @throws SicaException Si no se puede terminar la actividad.
     */
    void wfNegarPagoAnticipado(String ticket, int idActividad, IUsuario usuario)
            throws SicaException;

    /**
     * Autoriza la L&iacute;nea de Operaci&oacute;n de un Deal.
     *
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param idActividad El id de la actividad.
     * @param usuario El Usuario Logeado a la Aplicacion.
     * @see SicaServiceData#usoLiberacionLineaOperacion(com.ixe.ods.sica.model.LineaOperacion,
            String, com.ixe.ods.sica.model.DealDetalle, com.ixe.ods.seguridad.model.IUsuario,
            boolean).
     * @throws SicaException Si ocurre alg&uacute;n error.
     */
    void wfIntAutorizarLineaOperacion(String ticket, int idActividad, IUsuario usuario)
            throws SicaException;

    /**
     * Niega la autorizaci&oacute;n de la l&iacute;nea de operaci&oacute;n
     *
     * @param ticket El ticket de la sesi&oacute;n
     * @param idActividad El id de la actividad
     * @param usuario El usuario que hace la petici&oacute;.
     * @throws SicaException Si ocurre alg&uacute;n problema.
     */
    void wfIntNegarAutorizacionLineaOperacion(String ticket, int idActividad, IUsuario usuario)
            throws SicaException;

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
     * @see #terminarActividadesParaDeal(com.ixe.ods.sica.model.Deal, String,
            com.ixe.ods.seguridad.model.IUsuario).
     */
    void wfSolicitarCancelacionDeal(String ticket, int idDeal, IUsuario usuario)
            throws SicaException;

    /**
     * <ul>
     *   <li>Pone en solicitud el evento de cancelaci&oacute;n de detalle del deal.</li>
     *   <li>Se marca el evento del detalle de deal de cancelaci&oacute;n y se genera la actividad
     * de autorizaci&oacute;n de la solicitud de cancelaci&oacute;n del detalle de deal a la mesa de
     * cambios.
     * </ul>
     *
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param det El detalle de deal del que se solicita cancelaci&oacute;n.
     * @param usuario El usuario que solicita la cancelaci&oacute;n del deal.
     * @throws SicaException Si ocurre alg&uacute;n error.
     */
    void wfSolicitarCancelacionDetalleDeal(String ticket, DealDetalle det, IUsuario usuario)
            throws SicaException;

    /**
     * Cancela directamente el deal.
     *
     * @param ticket El ticket de la sesi&oacute;n
     * @param idDeal El id del deal.
     * @param idUsuario El id del usuario que hace la petici&oacute;n.
     * @throws SicaException Si ocurre alg&uacute;n problema.
     */
    void wfCancelarDirectamenteDeal(String ticket, int idDeal, int idUsuario) throws SicaException;

    /**
     * Pone en estado de aprobaci&oacute;n el status del evento de cancelaci&oacute;n del deal.
     * Llama a <code>marcarDealCancelado()</code> para realizar la cancelaci&oacute;n y por
     * &uacute;ltimo.
     *
     * @param ticket El ticket para llamar a SicaServiceData.
     * @param idActividad El n&uacute;mero de actividad de cancelaci&oacute;n del deal.
     * @param usuario El usuario que autoriza la cancelaci&oacute;n.
     * @throws SicaException Si ocurre alg&uacute;n error.
     */
    void wfAutorizarCancelacionParaDeal(String ticket, int idActividad, IUsuario usuario)
            throws SicaException;
    
    /**
     * Pone en estado de autorizado la Actividad.
     *
     * @param ticket El ticket para llamar a SicaServiceData.
     * @param idActividad El n&uacute;mero de actividad de cancelaci&oacute;n del deal.
     * @param usuario El usuario que autoriza la cancelaci&oacute;n.
     * @throws SicaException Si ocurre alg&uacute;n error.
     */
    void actualizarActividadNotificacion(String ticket, int idActividad, IUsuario usuario)
            throws SicaException;

    /**
     * Pone en estado de aprobaci&oacute;n el status del evento de cancelaci&oacute;n de detalle de
     * deal y afecta l aposici&oacute;n.
     *
     * @param ticket El ticket para llamar a SicaServiceData.
     * @param idActividad El n&uacute;mero de actividad de cancelaci&oacute;n del deal.
     * @param usuario El usuario que autoriza la cancelaci&oacute;n.
     * @throws SicaException Si ocurre alg&uacute;n error.
     */
    void wfAutorizarCancelacionParaDetalleDeal(String ticket, int idActividad, IUsuario usuario)
            throws SicaException;

    /**
     * Termina la actividad de la autorizaci&oacute;n de cancelaci&oacute;n con el resultado de no
     * autorizado. Actualiza el evento del deal de cancelaci&oacute;n como negado. Si el deal no se
     * encuentra en proceso de captura (status 'AL'), llama a <code>wfIniciarProcesoDeal()</code>
     * para reiniciar el workflow.
     *
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param idActividad El id de la actividad de autorizaci&oacute;n de cancelaci&oacute;n.
     * @param usuario El usuario que neg&oacute; la cancelaci&oacute;n del deal.
     * @throws SicaException Si no se puede terminar la actividad, o si no se puede iniciar de nuevo
     *  el workflow.
     * @see #wfIniciarProcesoDeal(String, int).
     */
    void wfNegarCancelacionParaDeal(String ticket, int idActividad, IUsuario usuario)
            throws SicaException;
    
    /**
     * Pone en estado de aprobaci&oacute;n el status del evento de modificaci&oacute;n del monto
     * de detalle de deal y afecta la posici&oacute;n.
     *
     * @param ticket El ticket para llamar a SicaServiceData.
     * @param idActividad El n&uacute;mero de actividad de cancelaci&oacute;n del deal.
     * @param usuario El usuario que autoriza la cancelaci&oacute;n.
     */
    void actualizarMontoParaDetalle(String ticket, int idActividad, IUsuario usuario)
    		throws SicaException;
    
    /**
     * Pone en estado de aprobaci&oacute;n el status del evento de modificaci&oacute;n del producto
     * de detalle de deal y afecta la posici&oacute;n.
     *
     * @param ticket El ticket para llamar a SicaServiceData.
     * @param idActividad El n&uacute;mero de actividad de cancelaci&oacute;n del deal.
     * @param usuario El usuario que autoriza la cancelaci&oacute;n.
     */
    void actualizarProductoParaDetalle(String ticket, int idActividad, IUsuario usuario)
    		throws SicaException;
    
    /**
     * No autoriza la modificaci&oacute;n para el detalle del deal, ya sea por producto o monto y 
     * adem&aacute;s cambia el status en el evento de modificaci&oacute;n  a rojo, indicando 
     * que no fue autorizado por la mesa de cambios.
     * 
     * @param ticket         El ticket de la sesi&oacute;n del usuario.
     * @param idActividad    El id de la actividad
     * @param usuario        El usuario que neg&oacute; la cancelaci&oacute;n del deal.
     * @throws SicaException Si no se puede terminar la actividad, o si no se puede inciar de nuevo
     *   el workflow
     */
    void negarActualizarMontoOProductoParaDetalle(String ticket, int idActividad, IUsuario usuario)
            throws SicaException;

    /**
     * Termina la actividad de la autorizaci&oacute;n de cancelaci&oacute;n de detalle de deal con
     * el resultado de no autorizado. Actualiza el evento del detalle de deal de cancelaci&oacute;n
     * como negado. Actualiza el status del deal a 'AL' (Alta).
     *
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param idActividad El id de la actividad de autorizaci&oacute;n de cancelaci&oacute;n.
     * @param usuario El usuario que neg&oacute; la cancelaci&oacute;n del deal.
     * @throws SicaException Si no se puede terminar la actividad, o si no se puede iniciar de nuevo
     *  el workflow.
     */
    void wfNegarCancelacionParaDetalleDeal(String ticket, int idActividad, IUsuario usuario)
            throws SicaException;

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
     * @see #marcarDealDetalleCancelado(String, com.ixe.ods.sica.model.DealDetalle, boolean, boolean).
     * @throws SicaException Si no se puede calcular la fecha valor de la cancelaci&ocute;n.
     */
    void marcarDealCancelado(int idDeal) throws SicaException;

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
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param idDeal El deal a cancelar.
     * @see #marcarDealDetalleCancelado(String, com.ixe.ods.sica.model.DealDetalle, boolean, boolean).
     * @throws SicaException Si no se puede calcular la fecha valor de la cancelaci&ocute;n.
     */
    void marcarDealCancelado(String ticket, int idDeal) throws SicaException;

    /**
     * Marca el detalle del deal como totalmente liquidado. Completa la actividad de
     * recepci&oacute;n de tesorer&iacute;a en la que est&aacute; detenido el detalle en el BPM.
     *
     * @param idDetalleLiq El detalle de liquidaci&oacute;n.
     * @param idDealDetalle El id del detalle de deal.
     * @throws SicaException Si algo sale mal.
     */
    void recibirEntregarCompleto(Long idDetalleLiq, Long idDealDetalle, String fed, String msgMt) throws SicaException;

    /**
     *
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param detOrig El detalle del deal a modificar.
     * @param mnemonicoReemplazo El mnem&oacute;nico a asignar al nuevo detalle.
     * @param usuario El usuario que solicita la modificaci&oacute;n del detalle.
     * @throws SicaException Si el deal ya estaba cancelado o no, si no se puede crear el nuevo
     *              detalle de deal (si es necesario) o si no se pueden terminar las actividades
     */
    public void solicitarModificacionDetalle(String ticket, DealDetalle detOrig,
                                             String mnemonicoReemplazo, IUsuario usuario)
            throws SicaException;
    
    /**
     * Marca Deal y detalle con solicitud de autorizaci&oacute;n por modificiaci&oacute;n de
     * monto
     * 
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param detOrig El detallde del deal a modificar.
     * @param nuevoMonto El monto a asignar a nuevo detalle.
     * @param usuario El usuario que solicita la modificaci&oacute;n del detalle
     * @throws SicaException Si el deal ya estaba cancelado o no.
     */
    void solicitarModificacionDetallePorMonto(String ticket, DealDetalle detOrig,
            double nuevoMonto, IUsuario usuario) throws SicaException; 

    /**
     * Marca Deal y detalle con solicitud de autorizaci&oacute;n por modificiaci&oacute;n de
     * producto
     * 
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param detOrig El detallde del deal a modificar.
     * @param nvaFormaLiq La nueva forma de liquidaci&oacute;n.
     * @param nvoTipoCambioMesa El nuevo tipo de cambio de la mesa.
     * @param nvoTipoCambioCliente El nuevo tipo de cambio para el cliente.
     * @param usuario El usuario que solicita la modificaci&oacute;n del detalle.
     */
    void solicitarModificacionDetallePorProducto(String ticket, DealDetalle detOrig,
            String nvaFormaLiq, double nvoTipoCambioMesa, double nvoTipoCambioCliente, 
            IUsuario usuario);
    
    /**
     * Modifica el mnem&oacute;nico del detalle de deal que tiene el idLiquidacionDetalle
     * proporcionado.
     *
     * @param idLiquidacionDetalle El n&uacute;mero de detalle de liquidaci&oacute;n.
     * @param idDealDetalle El id del detalle de deal.
     * @param mnemonico El mnem&oacute;nico a asignar al detalle de deal.
     * @throws SicaException Si no existe un detalle de deal con el id de detalle de
     *  liquidaci&oacute;n especificado.
     * @throws SeguridadException Si no se puede obtener el ticket por alg&uacute;n error en la
     *  seguridad.
     */
    void cambiarFormaLiquidacion(Long idLiquidacionDetalle, Long idDealDetalle, String mnemonico)
            throws SicaException, SeguridadException;

    /**
     * <p>El SITE env&iacute;a notificaci&oacute;n de cancelaci&oacute;n por detalle, aunque la
     * solicitud de cancelaci&oacute;n haya sido global. Si el evento de cancelaci&oacute;n del
     * encabezado del deal es una solicitud de cancelaci&oacute;n, debe ignorarse este mensaje.</p>
     * <p>En caso contrario, marca como autorizado o negado el evento de modificaci&oacute;n para el
     * deal que tiene un detalle con el n&uacute;mero de detalle de liquidaci&oacute;n
     * proporcionado.</p>
     * <p>Le avisa a Banxico que un Detalle de un Deal se cancela</p>.
     *
     * @param idDeal El identificador del deal.
     * @param autorizar true si se autoriza.
     * @param idDetalleLiquidacion Para identificar el detalle del Deal que se cancela y reportarlo
     * a Banxico.
     * @throws SicaException Si ocurre alg&uacute;n error.
     * @throws SeguridadException Si no se puede obtener el ticket por alg&uacute;n error en la
     *  seguridad.
     */
    void siteAutorizoONegoCancelacionDetalle(int idDeal, boolean autorizar,
                                             Long idDetalleLiquidacion)
            throws SicaException, SeguridadException;

    void hacerSplitDetalle(String ticket, DealDetalle detOrig, String mnemonicoSegundo,
                           double montoSegundo, IUsuario usuario) throws SicaException;

    /**
     * Permite interrumpir el workflow de un deal. Encuentra todas las actividades pendientes del
     * proceso de deals normales o interbancarios, de acuerdo al tipo de deal y las marca como
     * canceladas.
     *
     * @param deal El deal al cual se le interrumpir&aacute; su workflow.
     * @param resultado El comentario que se le asignar&aacute; a las actividades canceladas.
     * @param usuario El usuario que solicita la interrupci&oacute;n del workflow.
     * @return boolean True si se encontraron actividades.
     */
    boolean terminarActividadesParaDeal(Deal deal, String resultado, IUsuario usuario);

    /**
     * Permite interrumpir el workflow de un deal. Encuentra todas las actividades pendientes del
     * proceso de deals normales o interbancarios, de acuerdo al tipo de deal y las marca como
     * canceladas.
     *
     * @param det El detalle del deal al cual se le interrumpir&aacute; su workflow.
     * @param resultado El comentario que se le asignar&aacute; a las actividades canceladas.
     * @param usuario El usuario que solicita la interrupci&oacute;n del workflow.
     */
    void terminarActividadesParaDealDetalle(DealDetalle det, String resultado, IUsuario usuario);

    /**
     * Si el detallle afect&oacute; la posici&oacute;n y no est&aacute; cancelado, inserta el
     * registro de PosicionLog para la cancelaci&oacute;n., si el deal tiene pago anticipado o
     * toma en firme, llama a <code>SicaServiceData.liberarLineasCreditoParaDealDetalle(ticket,
     *  detalle.getIdDealPosicion())</code> para liberar las l&iacute;neas de cr&eacute;dito
     * afectadas.Le asigna el status de Cancelado.
     *
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param det El detalle del deal a cancelar.
     * @param liberarLinea Si se desea o no liberar la l&iacute;nea de cambios.
     * @param liberarRemesas Si se desea o no liberar el saldo de remesas de la l&iacute;nea.
     * @throws SicaException Si no se puede calcular la fecha valor de la cancelaci&ocute;n.
     */
    void marcarDealDetalleCancelado(String ticket, DealDetalle det, boolean liberarLinea,
                                    boolean liberarRemesas) throws SicaException;

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
     * Debe llamarse cuando en el front-end modifican los checkboxes de pago anticipado o toma en
     * firme.  
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
     * Asigna los datosAdicionales al reverso especificado y actualiza el registro.
     *
     * @param idReverso El n&uacute;mero de reverso a buscar.
     * @param datosAdicionales El valor de los datos adicionales a asignar al reverso.
     */
    void configurarDatosAdicionalesReverso(int idReverso, String datosAdicionales);

    /**
     * Modifica el swap con el tipo de modificaci&oacute;n especificada.
     *
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param swap El swap a modificar.
     * @param tipoModificacion El tipo de modificaci&oacute;n.
     * @param usuario El usuario que solicita la modificaci&oacute;n.
     * @throws SicaException Si no se puede cancelar alguno de los deals.
     */
    void modificarSwap(String ticket, Swap swap, int tipoModificacion, IUsuario usuario)
            throws SicaException;
    
    /**
     * Define si el reverso fue autorizado o negado por el SITE. Si fue autorizado, se asigna
     * el status PE (Pendiente) para que la mesa autorize el Reverso, de lo contario se asigna 
     * el status CA (Cancelado) al Revers.
     * 
     * @param reversoAutorizado Define si el reverso fue autorizado.
     * @param idDealOriginal El id del deal original del reverso.
     */
    void asignarStatusReverso(boolean reversoAutorizado, Integer idDealOriginal);

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

    /**
     * Revisa si alg&uacute;n detalle tiene solicitud de autorizaci&oacute;n por desviaci&oacute;n
     * al tipo de cambio, si es as&iacute;, crea la actividad correspondiente.
     *
     * @param idDeal El deal a revisar.
     */
    public void solicitarAutorizacionSobreprecio(int idDeal);
}
