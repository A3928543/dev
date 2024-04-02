/*
 * $Id: SiteService.java,v 1.21 2008/04/22 16:45:40 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 - 2008 LegoSoft S.C.
 */

package com.ixe.ods.sica;

import java.util.List;
import java.util.Map;

import com.ixe.ods.seguridad.model.SeguridadException;
import com.ixe.treasury.dom.common.Banco;
import com.ixe.treasury.dom.common.DetalleLiquidacion;
import com.ixe.treasury.dom.common.ExternalSiteException;
import com.ixe.treasury.dom.common.FoliosDeal;
import com.ixe.treasury.dom.common.Liquidacion;
import com.ixe.treasury.dom.common.Pais;

/**
 * Business Interface para el ejb del SITE.
 *
 * @author Jean C. Favila
 * @version $Revision: 1.21 $ $Date: 2008/04/22 16:45:40 $
 */
public interface SiteService {

    /**
     * Bloquea el deal durante el registro de un reverso, para que ya no se siga liquidando el resto
     * de los detalles.
     *
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param idLiquidacion El n&uacute;mero de liquidaci&oacute;n a bloquear.
     * @param idPersona La persona que solicita el bloqueo del deal.
     * @param motivo El motivo del reverso.
     * @throws SeguridadException Si el ticket no es v&aacute;lido.
     * @throws ExternalSiteException Si no se puede bloquear la liquidaci&oacute;n.
     */
    void bloquearDeal(String ticket, Long idLiquidacion, Long idPersona, String motivo)
            throws SeguridadException, ExternalSiteException;

    /**
     * Desbloquea la liquidaci&oacute;n durante la denegaci&oacute; de autorizaci&oacute;n de un
     * reverso, para que se siga liquidando el resto de los detalles.
     *
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param idLiquidacion El n&uacute;mero de liquidaci&oacute;n a bloquear.
     * @param idPersona La persona que solicita el bloqueo del deal.
     * @param motivo El motivo del reverso.
     * @throws SeguridadException Si el ticket no es v&aacute;lido.
     * @throws ExternalSiteException Si no se puede bloquear la liquidaci&oacute;n.
     */
    void desbloquearDeal(String ticket, Long idLiquidacion, Long idPersona, String motivo)
            throws SeguridadException, ExternalSiteException;

    /**
     * Cancela inmediatamente la liquidaci&oacute;n con sus detalles.
     *
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param idDeal El n&uacute;mero de deal.
     * @throws ExternalSiteException Si no se puede cancelar.
     * @throws SeguridadException Si el ticket no es v&aacute;lido.
     */
    public void cancelarSolicitudConDetalles(String ticket, int idDeal)
            throws ExternalSiteException, SeguridadException;

    /**
	 * Encuentra las distintas formas de liquidaci&oacute;n.
	 *
	 * @param ticket El ticket de la sesi&oacute;n.
	 * @param claveSistema La clave del sistema.
	 * @param claveCanal La clave del canal.
	 * @param idDivisa El id de la divisa.
	 * @param compra Define si la operaci&oacute;n es compra o venta.
	 * @param claveFormaLiquidacion La clave de la forma de liquidaci&oacute;n.
	 * @return Lista
	 */
    List findFormasPagoLiq(String ticket, String claveSistema, String claveCanal, String idDivisa,
                           boolean compra, String claveFormaLiquidacion);

    /**
     * Encuentra la forma liquidacion con base en los par&aacute;metros de b&uacute;squeda.
     *
     * @param ticket El ticket de la sesi&oacute;n.
     * @param claveSistema La clave del sistema.
     * @param claveCanal La clave del canal.
     * @param idDivisa El id de la divisa.
     * @param mnemonico El mnem&oacute;nico de la forma de liquidaci&oacute;n.
     * @return FomraPagoLiq
     */
    com.ixe.treasury.dom.common.FormaPagoLiq findFormaPagoLiq(String ticket, String claveSistema,
                                                              String claveCanal, String idDivisa,
                                                              String mnemonico);

    /**
     * Encuentra las distintas formas de liquidaci&oacute;n.
     *
     * @param ticket El ticket de la sesi&oacute;n.
     * @param claveSistema La clave del sistema.
     * @param claveCanal La clave del canal.
     * @return List
     */
    List findFormaPagoLiq(String ticket, String claveSistema, String claveCanal);

    /**
     * Encuentra los distintos medios de entrega para documentaci&oacute;n, entre ellos:
     * <li>Motocicleta.</li>
     * <li>Blindada.</li>
     * <li>Entrega a caja de cambios.</li>
     * <li>Entrega a cada de fondos.</li>
     *
     * @param ticket El ticket de la sesi&oacute;n.
     * @param claveFormaLiquidacion La clave de la forma de liquidaci&oacute;n.
     * @param claveTipoLiquidacion La clave del tipo de liquidaci&oacute;n.
     * @return List
     */
    List findMediosEntrega(String ticket, String claveFormaLiquidacion,
                           String claveTipoLiquidacion);

    /**
     * Realiza la solicitud de liquidaci&oacute;n de un deal.
     *
     * @param ticket El ticket de la sesi&oacute;n
     * @param liquidacion La liquidaci&oacute;n para el deal.
     * @return FoliosDeal
     * @throws ExternalSiteException Si ocurre alg&uacute;n error.
     */
    FoliosDeal agregarSolicitud(String ticket, com.ixe.treasury.dom.common.Liquidacion liquidacion)
            throws ExternalSiteException;

    /**
     * Realiza la solicitud de liquidaci&oacute;n para un detalle de deal.
     *
     * @param ticket El ticket de la sesi&oacute;n.
     * @param idLiquidacion El n&uacute;mero de liquidaci&oacute;n a la que pertenece el deal.
     * @param detalleLiquidacion El detalle de deal para liquidaci&oacute;n.
     * @return Long
     * @throws com.ixe.ods.seguridad.model.SeguridadException Si el ticket no es v&aacute;lido.
     * @throws com.ixe.treasury.dom.common.ExternalSiteException Si no se puede crear el detalle.
     */
    Long agregarDetalleSolicitud(String ticket, Long idLiquidacion,
                                 DetalleLiquidacion detalleLiquidacion)
            throws ExternalSiteException, SeguridadException;

    /**
     * Encuentra la liquidaci&oacute;n solicitada con base en su id.
     *
     * @param ticket El ticket de la sesi&oacute;n.
     * @param idLiquidacion El id de la liquidacion.
     * @return Liquidacion
     */
    Liquidacion findTesLiquidacion(String ticket, int idLiquidacion);

    /**
     * Encuentra el estatus actual de la solicitud de liquidaci&oacute;n de
     * un detalle de deal.
     *
     * @param ticket El ticket de la sesi&oacute;n.
     * @param idDetalleLiquidacion El id del detalle de la liquidaci&oacute;n.
     * @return String
     */
    String getStatusSolicitud(String ticket, int idDetalleLiquidacion);

    /**
     * Envia la solicitud para cancelaci&oacute;n de una deal.
     *
     * @param ticket El ticket de la sesi&oacute;n.
     * @param idLiquidacion El id de la liquidaci&oacute;n
     * @throws ExternalSiteException Si no se puede cancelar pues hay una liquidaci&oacute;n
     * parcial.
     */
    void solicitarCancelacion(String ticket, int idLiquidacion) throws ExternalSiteException;

    /**
     * Envia la solicitud para la cancelaci&oacute;n de liquidaci&oacute;n de un detalle de deal.
     *
     * @param ticket El ticket de la sesi&oacute;n.
     * @param idDetalleSolicitud El id del detalle de la solicitud.
     * @param idTipoCanc 4 para Modificaci&oacute;n, 1 para cancelaci&oacute;n total.
     * @throws com.ixe.treasury.dom.common.ExternalSiteException Error de l&oacute;gica del SITE.
     * @throws com.ixe.ods.seguridad.model.SeguridadException Si el ticket no es v&aacute;lido.
     */
    void solicitarCancelacionDetalle(String ticket, int idDetalleSolicitud, Integer idTipoCanc)
            throws ExternalSiteException, SeguridadException;

    void modificaSolicitud(java.lang.String s, com.ixe.treasury.dom.common.Liquidacion liquidacion)
            throws ExternalSiteException, SeguridadException;

    /**
     * Agregaun banco a la tabla.
     *
     * @param ticket El ticket de la sesi&oacute;n.
     * @param banco El banco a insertar.
     * @return Long.
     * @throws ExternalSiteException Si no se puede agregar el banco en la base de datos.
     * @throws SeguridadException Si el ticket no es v&aacute;lido.
     */
    Long agregaBancoPequeno(String ticket, Banco banco)
            throws ExternalSiteException, SeguridadException;

    /**
     * Encuentra un Banco internacional con base en su clave Swift.
     *
     * @param ticket El ticket de la sesi&oacute;n.
     * @param claveSwift La clave Swift del Banco.
     * @return BupBanco
     */
    com.ixe.treasury.integration.model.BupBanco findBupBancoXSwift(String ticket,
                                                                   String claveSwift);

    /**
     * Encuentra un Banco internacional con base en su clave ABA.
     *
     * @param ticket El ticket de la sesi&oacute;n.
     * @param claveAba La clave ABA del Banco.
     * @return BupBanco
     */
    com.ixe.treasury.integration.model.BupBanco findBupBancoXABA(String ticket, String claveAba);

    /**
     * Encuentra un Banco internacional con base en su clave Chip.
     *
     * @param ticket El ticket de la sesi&oacute;n.
     * @param claveChips La clave Chip del Banco.
     * @return BupBanco
     */
    com.ixe.treasury.integration.model.BupBanco findBupBancoXChips(String ticket,
                                                                   String claveChips);

    /**
     * Encuentra un Banco internacional con base en el id del Banco.
     *
     * @param ticket El ticket de la sesi&oacute;n.
     * @param idBancoInternacional El id del banco.
     * @return Banco
     */
    Banco findBancoInternacionalById(String ticket, Long idBancoInternacional);

    /**
     * Encuentra un Banco internacional con base en la clave de ruteo del pa&iacute;s.
     *
     * @param ticket El ticket de la sesi&ocaute;n.
     * @param claveRuteo La clave de ruteo del pa&iacute;s.
     * @param numeroRuteo El numero de ruteo.
     * @return Banco
     */
    Banco findBancoInternacionalByClaveRuteoPais(String ticket, String claveRuteo,
                                                 String numeroRuteo);

    /**
     * Encuentra un Banco internacional con base su nombre corto.
     *
     * @param ticket El ticket de la sesi&oacute;n.
     * @param nombreCorto El nombre corto del Banco.
     * @return Banco
     */
    Banco findBancoInternacionalByNombreCorto(String ticket, String nombreCorto);

    /**
     * Encuentra un Banco internacional con base en su clave Chips
     * @param ticket El ticket de la sesi&oacute;n.
     * @param claveChips La clave Chips del Banco.
     * @return Banco
     */
    Banco findBancoInternacionalByChips(String ticket, String claveChips);

    /**
     * Encuentra un banco internacional con base en su clave Swift.
     *
     * @param ticket El ticket de la sesi&oacute;n.
     * @param claveSwift La clave Swift del banco.
     * @return Banco
     */
    Banco findBancoInternacionalBySwift(String ticket, String claveSwift);

    /**
     * Encuentra un Banco internacional con base en el n&uacute;mero de ruteo.
     *
     * @param ticket El ticket de la sesi&oacute;n.
     * @param numeroRuteo El n&uacute;mero de ruteo.
     * @return Banco
     */
    Banco findBancoInternacionalByABA(String ticket, String numeroRuteo);

    /**
     * Obtiene la lista de paises con bancos registrados.
     *
     * @param ticket El ticket de la sesi&oacute;n.
     * @return List
     * @throws ExternalSiteException Si ocurre un error en el site.
     * @throws SeguridadException Si el ticket no es v&aacute;lido
     */
    List getPaises(String ticket) throws ExternalSiteException, SeguridadException;

    /**
     * Obtiene las claves de ruteo de un Pa&iacute;s.
     *
     * @param ticket El ticket de la sesi&oacute;n
     * @param idPais El id del pa&iacute;s.
     * @return List
     */
    List getClavesRuteoXPais(String ticket, String idPais);

    /**
     * Encuentra todas las claves de ruteo.
     *
     * @param ticket El ticket de la sesi&oacute;n.
     * @return List
     */
    List getClavesRuteo(String ticket);

    /**
     * Encuentra un Pa&iacute;s von base en su id.
     * @param ticket El ticket de la sesi&oacute;n.
     * @param idPais El id del pa&iacute;s.
     * @return Pais
     */
    Pais getPaisXClaveISO(String ticket, String idPais);

    /**
     * Encuentra todos los Bancos nacionales que realizan operaciones
     * por SPEI.
     *
     * @param ticket El ticket de la sesi&oacute;n.
     * @return List
     */
    List getBancosNacionalesXOperaSpei(String ticket);

    /**
     * Obtiene las liquidaciones de deal con base en el n&uacute;mero de orden.
     *
     * @param ticket El ticket de la sesi&oacute;n.
     * @param numeroOrden El n&uacte;mero de orden de la liquidaci&oacute;n.
     * @return List
     */
    List getLiquidacionesXNumeroOrden(String ticket, String numeroOrden);

    /**
     * Obtiene los motivos por lo que fue cancelada la liquidaci&oacute;n de un detalle de deal.
     *
     * @param ticket El ticket de la sesi&oacute;n.
     * @param idDetalleLiquidacion El id del detalle de la liquidaci&oacute;n.
     * @return List.
     */
    List getMotivosCancelacionXIdDetalleLiquidacion(String ticket, Long idDetalleLiquidacion);

    /**
     * @see com.ixe.treasury.middleware.ejb.ExternalSiteSessionBean#findBancosInternacionales(
            String, java.util.Map).
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param parametros Los parametros de b&uacute;squeda.
     * @return List.
     * @throws ExternalSiteException Si no ocurre alg&uacute;n error.
     * @throws SeguridadException Si el ticket no es v&aacute;lido.
     */
    List findBancosInternacionales(String ticket, Map parametros)
            throws ExternalSiteException, SeguridadException;
    
    /**
     * Metodo que solicita el bloqueo de una operacion al SITE para capturar
     * el reverso de la misma.
     * 
     * @param ticket El ticket de la sesion.
     * @param idLiquidacion El identificador SITE
     * @param idPersona El identificador de empleado que solicita el bloqueo
     * @param observaciones Las Observaciones referentes a la razón de la solicitud de bloqueo
     * @throws ExternalSiteException Si ocurre alg&uacute;n problema.
     * @throws SeguridadException Si ocurre alg&uacute;n problema.
     */
   public void solicitarBloqueo(String ticket, Long idLiquidacion, Long idPersona, 
		   String observaciones) throws ExternalSiteException, SeguridadException;
}
