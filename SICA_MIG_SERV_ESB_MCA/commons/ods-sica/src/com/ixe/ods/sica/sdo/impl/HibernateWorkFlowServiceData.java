/*
 * $Id: HibernateWorkFlowServiceData.java,v 1.110.2.5.4.1.2.1.2.2.4.1.4.2.8.1.20.1.2.1.8.2.2.6 2016/09/27 19:40:05 mejiar Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2006 - 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica.sdo.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.ixe.bean.bup.MedioContacto;
import com.ixe.bean.bup.PersonaFisica;
import com.ixe.contratacion.ContratacionException;
import com.ixe.ods.bup.model.ContratoSica;
import com.ixe.ods.bup.model.Persona;
import com.ixe.ods.seguridad.model.IUsuario;
import com.ixe.ods.seguridad.model.SeguridadException;
import com.ixe.ods.seguridad.model.Usuario;
import com.ixe.ods.seguridad.services.LoginService;
import com.ixe.ods.seguridad.services.TicketService;
import com.ixe.ods.sica.Constantes;
import com.ixe.ods.sica.DealMailSender;
import com.ixe.ods.sica.Mensaje;
import com.ixe.ods.sica.SicaAlertasService;
import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.SicaProperties;
import com.ixe.ods.sica.SicaRegistroMedioContactoPersonaService;
import com.ixe.ods.sica.SicaWorkFlowService;
import com.ixe.ods.sica.SiteService;
import com.ixe.ods.sica.dao.ActividadDao;
import com.ixe.ods.sica.dao.BitacoraEnviadasDao;
import com.ixe.ods.sica.dao.InformacionNegociacionDao;
import com.ixe.ods.sica.model.Actividad;
import com.ixe.ods.sica.model.AutMedioContacto;
import com.ixe.ods.sica.model.BitacoraEnviadas;
import com.ixe.ods.sica.model.BitacoraEnviadasHelper;
import com.ixe.ods.sica.model.Deal;
import com.ixe.ods.sica.model.DealDetalle;
import com.ixe.ods.sica.model.DealHelper;
import com.ixe.ods.sica.model.Divisa;
import com.ixe.ods.sica.model.Estado;
import com.ixe.ods.sica.model.IPlantilla;
import com.ixe.ods.sica.model.LineaCambio;
import com.ixe.ods.sica.model.LineaOperacion;
import com.ixe.ods.sica.model.LineaOperacionLog;
import com.ixe.ods.sica.model.ParametroSica;
import com.ixe.ods.sica.model.Plantilla;
import com.ixe.ods.sica.model.PosicionLog;
import com.ixe.ods.sica.model.Reverso;
import com.ixe.ods.sica.model.Swap;
import com.ixe.ods.sica.sdo.LineaCambioServiceData;
import com.ixe.ods.sica.sdo.PizarronServiceData;
import com.ixe.ods.sica.sdo.SicaServiceData;
import com.ixe.ods.sica.sdo.WorkFlowServiceData;
import com.ixe.ods.sica.sdo.dto.TipoOperacionDealDto;
import com.ixe.ods.sica.services.DealService;
import com.ixe.ods.sica.services.FormasPagoLiqService;
import com.ixe.ods.sica.services.GeneralMailSender;
import com.ixe.ods.sica.services.H2HService;
import com.ixe.ods.sica.services.LineaCreditoConstantes;
import com.ixe.ods.sica.services.LineaCreditoMensajes;
import com.ixe.ods.sica.util.InfoNegociacionUtil;
import com.ixe.ods.sica.utils.DateUtils;
import com.ixe.ods.sica.utils.MapUtils;
import com.ixe.ods.sica.vo.ReversoVO;
import com.ixe.ods.sica.vo.WorklistTotalVO;
import com.ixe.treasury.dom.common.DetalleLiquidacion;
import com.ixe.treasury.dom.common.ExternalSiteException;
import com.ixe.treasury.dom.common.FoliosDeal;
import com.ixe.treasury.dom.common.FormaPagoLiq;
import com.ixe.treasury.dom.common.Liquidacion;
import com.ixe.treasury.dom.common.SiteConstants;

/**
 * Implementaci&oacute;n Hibernate para la interfaz WorkFlowServiceData.
 *
 * @see WorkFlowServiceData
 *
 * @author Jean C. Favila
 * @version $Revision: 1.110.2.5.4.1.2.1.2.2.4.1.4.2.8.1.20.1.2.1.8.2.2.6 $ $Date: 2016/09/27 19:40:05 $
 */
public class HibernateWorkFlowServiceData extends HibernateSicaServiceData
        implements WorkFlowServiceData, ApplicationContextAware {

    /**
     * Constructor por default.
     */
    public HibernateWorkFlowServiceData() {
        super();
    }

    /**
     * @param arg0 La Referencia al applicationContext.
     * @see org.springframework.context.ApplicationContextAware#setApplicationContext(
            org.springframework.context.ApplicationContext)
     * @throws BeansException Si algo sale mal.
     */
    public void setApplicationContext(ApplicationContext arg0) throws BeansException {
        _appContext = arg0;
    }

    /**
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param deal El deal a actualizar.
     * @see com.ixe.ods.sica.sdo.WorkFlowServiceData#actualizarDatosDeal(String,
            com.ixe.ods.sica.model.Deal).
     */
    public void actualizarDatosDeal(String ticket, Deal deal) {
        try {
            update(deal);
            if (deal.getIdLiquidacion() != null) {
                SiteService siteService = (SiteService) _appContext.getBean("siteService");
                siteService.modificaSolicitud(ticket,
                        getDealService().crearLiquidacion(deal, true));
            }
        }
        catch (ExternalSiteException e) {
            throw new SicaException(e.getMessage());
        }
        catch (SeguridadException e) {
            throw new SicaException(e.getMessage());
        }
    }

    /**
     * Regresa el registro de Actividad que corresponde al idActividad proporcionado. Su
     * relaci&oacute;n con el deal es inicializada.
     *
     * @param idActividad El n&uacute;mero de actividad.
     * @return Actividad.
     */
    private Actividad findActividadWithDeal(int idActividad) {
        List acts = getHibernateTemplate().find("FROM Actividad AS a INNER JOIN FETCH a.deal " +
          "LEFT JOIN FETCH a.dealDetalle WHERE a.idActividad = ?", new Integer(idActividad));
        if (acts.isEmpty()) {
            throw new SicaException(Mensaje.NO_SE_ENCONTRO_LA_ACTIVIDAD + idActividad +
                    Mensaje.EN_LA_BASE_DE_DATOS);
        }
        return (Actividad) acts.get(0);
    }

    /**
     * Regresa el registro de Actividad que corresponde al idActividad proporcionado. Sus relaciones
     * con el deal y con dealDetalle son inicializadas.
     *
     * @param idActividad El n&uacute;mero de actividad.
     * @return Actividad.
     */
    private Actividad findActividadWithDealDetalle(int idActividad) {
        List acts = getHibernateTemplate().find("FROM Actividad AS a INNER JOIN FETCH " +
                "a.dealDetalle AS dd INNER JOIN FETCH dd.deal WHERE a.idActividad = ?",
                new Integer(idActividad));
        if (acts.isEmpty()) {
            throw new SicaException(Mensaje.NO_SE_ENCONTRO_LA_ACTIVIDAD + idActividad +
                    Mensaje.EN_LA_BASE_DE_DATOS);
        }
        return (Actividad) acts.get(0);
    }

    /**
     * @param ticket El ticket de la sesi&ocute;n.
     * @return String.
     * @see WorkFlowServiceData#findAllActividadesPendientes(String, String).
     * @throws SeguridadException Si el ticket no es v&aacute;lido.
     */
    public List findAllActividadesPendientes(String ticket, String nombreActividad)
            throws SeguridadException {
        ((TicketService) _appContext.getBean("ticketJtaService")).isTicketValido(ticket);
        ActividadDao actividadDao = (ActividadDao) _appContext.getBean("actividadDao");
        return actividadDao.getWorkitems(ticket, nombreActividad);
    }

    /**
     * @see com.ixe.ods.sica.sdo.WorkFlowServiceData#findDealsInterbancariosPorConfirmar()
     */
    public List findDealsInterbancariosPorConfirmar() {
        List resultados = new ArrayList();
        List dets = new ArrayList(new HashSet(getHibernateTemplate().find("SELECT d.idDeal, " +
                "det.folioDetalle, d.tipoValor, d.fechaLiquidacion, det.recibimos, " +
                "pcr.id.persona.nombreCorto, det.claveFormaLiquidacion, det.divisa.idDivisa, " +
                "det.monto, det.tipoCambio, d.tipoCambioCobUsdDiv, det.idDealPosicion FROM " +
                "DealDetalle AS det INNER JOIN det.deal AS d LEFT JOIN det.plantilla INNER JOIN " +
                "d.contratoSica AS cs INNER JOIN cs.roles AS pcr WHERE d.fechaCaptura > ? AND " +
                "d.tipoDeal = ? AND det.claveFormaLiquidacion is not null AND " +
                "det.datosConfirmacion is null AND det.statusDetalleDeal != ? AND " +
                "pcr.id.rol.idRol = 'TIT'",
                new Object[]{DateUtils.inicioDia(), Deal.TIPO_INTERBANCARIO,
                        DealDetalle.STATUS_DET_CANCELADO})));
        for (Iterator it = dets.iterator(); it.hasNext();) {
            Object[] reg = (Object[]) it.next();
            Map map = MapUtils.generar(new String[]{"idDeal", "fechaValor", "fechaLiquidacion",
                    "compraVenta", "cliente", "claveFormaLiquidacion", "idDivisa", "monto",
                    "tipoCambio", "importe", "tipoCambioInt", "idDealPosicion", "actividad", "confirmado"},
                    new Object[]{reg[0] + "-" + reg[1], reg[2],
                            Constantes.DATE_FORMAT.format((Date) reg[3]),
                            ((Boolean) reg[4]).booleanValue() ? "Compra" : "Venta", reg[5], reg[6],
                            reg[7], Constantes.MONEY_FORMAT.format(reg[8]),
                            Constantes.CURRENCY_FORMAT.format(reg[9]),
                            Constantes.MONEY_FORMAT.format(((Double) reg[8]).doubleValue() *
                                    ((Double) reg[9]).doubleValue()),
                            reg[10] != null ? Constantes.CURRENCY_FORMAT.format(reg[10]) : null,
                            reg[11], "Confirmaciones", Boolean.FALSE});
            resultados.add(map);
        }
        return resultados;
    }

    /**
     * Confirma si un deal ha sido confirmado por la mesa de control.
     * @return True si ya fue confirmado, false de otra manera.
     */
    public boolean isDealIntConfirmado(int idDeal) {
    	
        List dets = getHibernateTemplate().find("SELECT d.idDeal, " +
                "det.folioDetalle, d.tipoValor, d.fechaLiquidacion, det.recibimos, " +
                "pcr.id.persona.nombreCorto, det.claveFormaLiquidacion, det.divisa.idDivisa, " +
                "det.monto, det.tipoCambio, d.tipoCambioCobUsdDiv, det.idDealPosicion FROM " +
                "DealDetalle AS det INNER JOIN det.deal AS d LEFT JOIN det.plantilla INNER JOIN " +
                "d.contratoSica AS cs INNER JOIN cs.roles AS pcr WHERE d.fechaCaptura > ? AND " +
                "d.tipoDeal = ? AND det.claveFormaLiquidacion is not null AND " +
                "det.datosConfirmacion is null AND det.statusDetalleDeal != ? AND " +
                "pcr.id.rol.idRol = 'TIT' " +
                "AND d.idDeal = ?",
                new Object[]{DateUtils.inicioDia(), Deal.TIPO_INTERBANCARIO,
                        DealDetalle.STATUS_DET_CANCELADO, new Integer(idDeal)});
        
        return dets.size() == 0 ? true : false;  
    }

    /**
     * @see WorkFlowServiceData#findAltaAnticipadaCorreosElectronicosPendientes()
     */
    public List findAltaAnticipadaCorreosElectronicosPendientes() {
        List resultados = new ArrayList();
        List regs = getHibernateTemplate().find("FROM AutMedioContacto AS amc INNER JOIN FETCH " +
                "amc.persona INNER JOIN FETCH amc.promotor WHERE amc.status = ?",
                AutMedioContacto.STATUS_PENDIENTE);
        for (Iterator it = regs.iterator(); it.hasNext();) {
            AutMedioContacto amc = (AutMedioContacto) it.next();
            resultados.add(MapUtils.generar(new String[]{"idAutMedioContacto", "email",
                    "nomCliente", "nomPromotor", "fecha", "actividad"},
                    new Object[]{new Integer(amc.getIdAutMedioContacto()), amc.getEmail(),
                            amc.getPersona().getNombreCompleto(),
                            amc.getPromotor().getNombreCompleto(),
                            DATE_HOUR_FORMAT.format(amc.getFechaCreacion()), "Alta Email"}));
        }
        return resultados;
    }

    /**
     * @see com.ixe.ods.sica.sdo.WorkFlowServiceData#confirmarDealInterbancario(String, int, String,
     *  int).
     */
    public List confirmarDealInterbancario(String ticket, int idDealPosicion, String contacto,
                                           int idUsuario) {
        if (StringUtils.isEmpty(contacto)) {
            throw new SicaException("Por favor especifique el nombre del contacto.");
        }
        DealDetalle det = findDealDetalle(idDealPosicion);
        det.setDatosConfirmacion(HOUR_FORMAT.format(new Date()) + "|" + contacto + "|" +
                ((IUsuario) find(Usuario.class, new Integer(idUsuario))).getClave());
        update(det);
        
        //Se verifica si el deal tiene actividades pendientes por completar, de
        //otra manera, se envia a la solicitud para su liquidacion.
        if (findActividadesPendientesDealInt( det.getDeal().getIdDeal()).size() == 0 &&
        		Constantes.CASH.equals(det.getDeal().getTipoValor()) && 
        		det.getDeal().isPagoAnticipado() &&
        		! det.getDeal().getStatusDeal().equals(Deal.STATUS_DEAL_PROCESO_CAPTURA) ){
        	wfIntEvaluarBanxico(ticket, det.getDeal());
        }
        
        return new ArrayList();
    }

    /**
     * @param nombreActividad El nombre de la actividad a buscar.
     * @return List.
     * @see WorkFlowServiceData#findActividadesPendientes(String).
     */
    public List findActividadesPendientes(String nombreActividad) {
        return getHibernateTemplate().find("FROM Actividad as a LEFT JOIN FETCH a.deal AS d "
                + "LEFT JOIN FETCH d.usuario WHERE a.nombreActividad = ? AND a.status = ?",
                new Object[]{nombreActividad, Actividad.STATUS_PENDIENTE});
    }

    /**
     * @see com.ixe.ods.sica.sdo.WorkFlowServiceData#findActividadesPendientesTotales(boolean).
     */
    public List findActividadesPendientesTotales(boolean incluirConfDeals) {
        List resultados = getHibernateTemplate().find("SELECT new "
                + "com.ixe.ods.sica.vo.WorklistTotalVO(a.nombreActividad, count(a.nombreActividad))"
                + " FROM Actividad a WHERE a.status = ? GROUP BY a.nombreActividad",
                Actividad.STATUS_PENDIENTE);
        // Los deals interbancarios por confirmar:
        List totConfs = incluirConfDeals ? findDealsInterbancariosPorConfirmar() : new ArrayList();
        resultados.add(new WorklistTotalVO("Confirmaciones", new Integer(totConfs.size())));
        // Las altas de correos para facturacion electronica:
        List totAltasCorreos = incluirConfDeals ?
                findAltaAnticipadaCorreosElectronicosPendientes() : new ArrayList();
        resultados.add(new WorklistTotalVO("Alta Email", new Integer(totAltasCorreos.size())));
        // Ahora se incluyen los reversos:
        List totRevs = getHibernateTemplate().find("SELECT count(r.idReverso) FROM Reverso AS r "
                + "WHERE r.statusReverso = ?", Reverso.STATUS_PENDIENTE);
        resultados.add(new WorklistTotalVO("Reversos", (Integer) totRevs.get(0)));
        return resultados;
    }

    /**
     * Regresa la actividad cuyo n&uacute;mero de deal y nombre de actividad sean los especificados
     * y se encuentre en status 'Pendiente'.
     *
     * @param nombreActividad El nombre de la actividad a buscar.
     * @param idDeal El n&uacute;mero de deal a buscar.
     * @return Actividad.
     */
    private Actividad findActividadPendienteActividadDeal(String nombreActividad, int idDeal) {
        List acts = getHibernateTemplate().
                find("FROM Actividad as a WHERE a.deal.idDeal = ? AND a.nombreActividad = ?"
                        + " AND a.status = ?", new Object[]{new Integer(idDeal),
                            nombreActividad, Actividad.STATUS_PENDIENTE});
        return (Actividad) (acts.isEmpty() ? null : acts.get(0));
    }

    /**
     * Regresa las actividades pendientes para un Deal Interbancario.
     * @param idDeal El ID de deal para buscar sus actividades pendientes.
     * @return
     */
    private List findActividadesPendientesDealInt( int idDeal) {
        List acts = getHibernateTemplate().
                find("FROM Actividad as a WHERE a.deal.idDeal = ?"
                        + " AND a.status = ?", new Object[]{new Integer(idDeal),
                            Actividad.STATUS_PENDIENTE});
        return acts;
    }

    /**
     * Regresa las actividades pendientes por L&iacute;nea de Cr&eacute;dito (Deal Normal: Pago
     * Anticipado o Toma en Firme; Deal Interbancario: L&iacute;nea Cr&eacute;dito) cuyo
     * n&uacute;mero de deal sea el especificado.
     *
     * @param interbancario Si el Deal es Normal o Interbancario.
     * @param idDeal El N&uacute;mero de Deal a buscar.
     * @return List Las Actividades pendientes encontradas.
     */
    private List findActividadesPendientesLC(boolean interbancario, int idDeal) {
        List acts;
        if (interbancario) {
            acts = getHibernateTemplate().find("FROM Actividad AS a WHERE a.deal.idDeal = ? " +
                    "AND a.nombreActividad = ? AND a.status = ?", new Object[]{new Integer(idDeal),
                    Actividad.ACT_DINT_LINEA_CREDITO, Actividad.STATUS_PENDIENTE});
        }
        else {
            acts = getHibernateTemplate().find("FROM Actividad AS a WHERE a.deal.idDeal = ? " +
                    "AND a.nombreActividad = ? AND a.status = ?", new Object[]{
                    new Integer(idDeal), Actividad.ACT_DN_PAGO_ANTICIPADO,
                    Actividad.STATUS_PENDIENTE});
        }
        return acts;
    }

    /**
     * Si el deal est&aacute; marcado para toma en firme y est&aacute; en evento no determinado,
     * hace uso de las l&iacute;neas de cr&eacute;dito para toma en firme, validando que no se
     * exceda del l&iacute;mite de la l&iacute;nea. Si no tiene toma en firme y tiene pago
     * anticipado con evento no determinado, hace uso de las l&iacute;neas de cr&eacute;dito de pago
     * anticipado validando que no se exceda el l&iacute;mite de la l&iacute;. En estos
     * &uacute;ltimos casos, se solicita autorizaci&oacute;n por excedente de l&iacute;nea de
     * cr&eacute;dito para toma en firme o pago anticipado.
     *
     * Si el deal es reprocesado, no se vuelve a hacer uso de las l&iacute;neas afectadas.
     *
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param deal El deal a revisar.
     * @throws SicaException Si no hay mnem&oacute;nicos aplicables a l&iacute;neas de
     * cr&eacute;dito.
     */
    private void hacerUsoLineasCredito(String ticket, Deal deal) throws SicaException {
        // Se hace uso de las lineas de credito si es necesario:
        if (deal.isTomaEnFirme() || deal.isPagoAnticipado()) {
            getLineaCambioServiceData().usar(ticket, deal, true);
        }
    }

    /**
     * @see WorkFlowServiceData#wfIniciarProcesoDeal(String,int).
     *
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param idDeal El deal a procesar.
     * @throws SicaException Si algo sale mal.
     */
    public void wfIniciarProcesoDeal(String ticket, int idDeal) throws SicaException {
    	debug("Inicia proceso deal:" + idDeal);
        Deal deal = findDeal(idDeal);
        List fpls = getDealService().getFormasPagoLiqService().getFormasTiposLiq(ticket);
        if (!deal.isProcesable(fpls)) {
            throw new SicaException("El deal no es procesable en este momento.");
        }
        if (deal.isDealBalanceo()) {
        	if (deal.getEventosDeal().indexOf('S') >= 0) {
        		deal.getEventosDeal().replaceAll("\\S", " ");
        	}
        }
        if (deal.isDeSucursal()) {
            if (deal.isEvento(Deal.EV_IND_SOBREPRECIO, new String[] {Deal.EV_SOLICITUD})) {
                throw new SicaException("No se puede procesar el deal debido a que tiene " +
                        "una solicitud de autorizaci\u00f3n pendiente");
            }
            Estado edoActual = findEstadoSistemaActual();
            if (!(Estado.ESTADO_OPERACION_NORMAL == edoActual.getIdEstado() ||
                    Estado.ESTADO_OPERACION_RESTRINGIDA == edoActual.getIdEstado())) {
                return;
            }
        }
        if (deal.getCliente().getSectorEconomico() == null) {
        	throw new SicaException("El deal no puede ser procesado ya que el cliente no tiene " +
                    "sector econ\u00f3mico.");
        }
        if (deal.isMensajeria() && deal.getDireccion() == null) {
            throw new SicaException("El deal no tiene direcci\u00f3n de mensajer\u00eda asignada.");
        }
        deal.setStatusDeal(Deal.STATUS_DEAL_CAPTURADO);
        update(deal);
        deal.setEvento(Deal.EV_NORMAL, Deal.EV_IND_GRAL_MODIFICACION);
        for (Iterator it = deal.getDetalles().iterator(); it.hasNext();) {
            DealDetalle det = (DealDetalle) it.next();
            if (DealDetalle.STATUS_DET_PROCESO_CAPTURA.equals(det.getStatusDetalleDeal())) {
                det.setStatusDetalleDeal(DealDetalle.STATUS_DET_COMPLETO);
            }
            det.setEvento(Deal.EV_NORMAL, deal.isInterbancario()
                    ? DealDetalle.EV_IND_INT_MODIFICACION : DealDetalle.EV_IND_MODIFICACION);
            if (deal.isDealBalanceo()) {
            	if (det.getEventosDetalleDeal().indexOf('S') >= 0) {
            		det.getEventosDetalleDeal().replaceAll("\\S", " ");
            	}
            }
            update(det);
        }
        update(deal);
        // Enviar alarma de alta:
        getSicaAlertasService().generaAlerta("SC_ALTA", deal.getCliente().getIdPersona(),
                DealHelper.getContextoAlertas(deal));
        if (deal.isInterbancario()) {
            // Preguntar si es necesaria la autorizacion por documentacion:
            if (requiereAutorizacionDocumentacionParaDeal(deal)) {
                wfCrearActividad(Actividad.PROC_DEAL_INTERBANCARIO,
                        Actividad.ACT_DINT_DOCUMENTACION, deal, null);
            }
            else {
                wfIntEvaluarLinOp(ticket, deal);
            }
        }
        else {
            wfNormEvaluarBanxicoYFrecuente(ticket, deal);
        }
    }

    /**
     * @see WorkFlowServiceData#wfNormAutorizarHorario(String, int,
            com.ixe.ods.seguridad.model.IUsuario).
     * @param ticket El ticket de la sesi&oacute;n.
     * @param idActividad El n&uacute;mero de la actividad de solicitud de autorizaci&oacute;n por
     *      horario.
     * @param usuario El usuario que realiza la autorizaci&oacute;n.
     * @throws SicaException Si algo sale mal.
     * @see #wfNormEvaluarMonto(com.ixe.ods.sica.model.DealDetalle).
     */
    public void wfNormAutorizarHorario(String ticket, int idActividad, IUsuario usuario)
            throws SicaException {
        Actividad actividad = findActividadWithDealDetalle(idActividad);
        DealDetalle det = actividad.getDealDetalle();
        det.setEvento(Deal.EV_APROBACION, DealDetalle.EV_IND_HORARIO);
        update(det);
        actividad.terminar(usuario, Actividad.RES_AUTORIZADO);
        update(actividad);
        wfNormEvaluarMonto(det);
    }

    /**
     * @see WorkFlowServiceData#wfNormNegarHorario(String,int,com.ixe.ods.seguridad.model.IUsuario).
     * @param ticket El ticket de la sesi&oacute;n.
     * @param idActividad El n&uacute;mero de la actividad de solicitud de autorizaci&oacute;n por
     * horario.
     * @param usuario El usuario que realiza la negaci&oacute;n de autorizaci&oacute;n.
     * @throws SicaException Si algo sale mal.
     * @see #marcarDealCancelado(String, int).
     */
    public void wfNormNegarHorario(String ticket, int idActividad, IUsuario usuario)
            throws SicaException {
        Actividad actividad = findActividadWithDealDetalle(idActividad);
        DealDetalle det = actividad.getDealDetalle();
        det.setEvento(Deal.EV_NEGACION, DealDetalle.EV_IND_HORARIO);
        update(det);
        // Si hay algun detalle vivo de divisa no cancelado, solo se cancela este detalle. Si no,
        // debe cancelarse completamente el deal:
        if (det.getDeal().getNumeroDetallesDivisaNoCancelados() > 1) {
            marcarDealDetalleCancelado(ticket, det, false, false);
            actividad.terminar(usuario, Actividad.RES_NO_AUTORIZADO);
            update(actividad);
        }
        else {
            marcarDealCancelado(ticket, det.getDeal().getIdDeal());
            actividad.terminar(usuario, Actividad.RES_NO_AUTORIZADO);
            update(actividad);
        }
    }

    /**
     * Revisa si el detalle deal tiene solicitud de autorizaci&oacute;n por monto. Si es as&iacute;,
     * crea la actividad correspondiente. En caso contrario, llama a wfNormEvaluarDesvTcc() para
     * seguir con el flujo.
     *
     * @see #wfEvaluarAfectarPosicion(com.ixe.ods.sica.model.DealDetalle).
     * @param det El detalle de deal a evaluar.
     * @throws SicaException Si algo sale mal.
     */
    private void wfNormEvaluarMonto(DealDetalle det) throws SicaException {
        if (Deal.EV_SOLICITUD.equals(det.decodificarEvento(DealDetalle.EV_IND_MONTO))) {
            wfCrearActividad(Actividad.PROC_HORARIO_MONTO, Actividad.ACT_DN_MONTO, det.getDeal(),
                    det);
        }
        else {
            wfEvaluarAfectarPosicion(det);
        }
    }

    /**
     * @see WorkFlowServiceData#wfNormAutorizarMonto(String, int,
            com.ixe.ods.seguridad.model.IUsuario).
     * @param ticket El ticket de la sesi&oacute;n.
     * @param idActividad El n&uacute;mero de la actividad de solicitud de autorizaci&oacute;n por
     *  monto.
     * @param usuario El usuario que realiza la autorizaci&oacute;n.
     * @throws SicaException Si algo sale mal.
     * @see #wfEvaluarAfectarPosicion(com.ixe.ods.sica.model.DealDetalle).
     */
    public void wfNormAutorizarMonto(String ticket, int idActividad, IUsuario usuario)
            throws SicaException {
        Actividad actividad = findActividadWithDealDetalle(idActividad);
        DealDetalle det = actividad.getDealDetalle();
        det.setEvento(Deal.EV_APROBACION, DealDetalle.EV_IND_MONTO);
        update(det);
        actividad.terminar(usuario, Actividad.RES_AUTORIZADO);
        update(actividad);
        wfEvaluarAfectarPosicion(det);
    }

    /**
     * Si hubo una autorizacion por horario y/o monto, debe afectarse la posici&oacute;n, siempre y
     * cuando el detalle no este cancelado.
     *
     * @param det El detalle de deal a evaluar.
     * @throws SicaException Si no se puede calcular la fecha valor de la posici&oacute;n a afectar.
     */
    private void wfEvaluarAfectarPosicion(DealDetalle det) throws SicaException {
        if (Deal.EV_APROBACION.equals(det.decodificarEvento(DealDetalle.EV_IND_HORARIO))
                || Deal.EV_APROBACION.equals(det.decodificarEvento(DealDetalle.EV_IND_MONTO))) {
            if (!DealDetalle.STATUS_DET_CANCELADO.equals(det.getStatusDetalleDeal())) {
                afectarPosicion(det);
            }
        }
    }

    /**
     * @see WorkFlowServiceData#wfNormNegarMonto(String,int,com.ixe.ods.seguridad.model.IUsuario).
     * @param ticket El ticket de la sesi&oacute;n.
     * @param idActividad El n&uacute;mero de la actividad de solicitud de autorizaci&oacute;n por
     *  monto.
     * @param usuario El usuario que realiza la negaci&oacute;n de autorizaci&oacute;n.
     * @throws SicaException Si algo sale mal.
     * @see #marcarDealCancelado(String, int).
     */
    public void wfNormNegarMonto(String ticket, int idActividad, IUsuario usuario)
            throws SicaException {
        Actividad actividad = findActividadWithDealDetalle(idActividad);
        DealDetalle det = actividad.getDealDetalle();
        det.setEvento(Deal.EV_NEGACION, DealDetalle.EV_IND_MONTO);
        update(det);
        // Si hay algun detalle vivo de divisa no cancelado, solo se cancela este detalle. Si no,
        // debe cancelarse completamente el deal:
        if (det.getDeal().getNumeroDetallesDivisaNoCancelados() > 1) {
            marcarDealDetalleCancelado(ticket, det, false, false);
            actividad.terminar(usuario, Actividad.RES_NO_AUTORIZADO);
            update(actividad);
        }
        else {
            marcarDealCancelado(ticket, det.getDeal().getIdDeal());
            actividad.terminar(usuario, Actividad.RES_NO_AUTORIZADO);
            update(actividad);
        }
    }

    /**
     * @see WorkFlowServiceData#wfNormConfirmarDesvTcc(String,int,
            com.ixe.ods.seguridad.model.IUsuario).
     * @param ticket El ticket de la sesi&oacute;n.
     * @param idActividad El n&uacute;mero de actividad de la notificaci&oacute;n.
     * @param usuario El usuario que realiza la confirmaci&oacute; de la notificaci&oacute;n.
     * @throws SicaException Si algo sale mal.
     */
    public void wfNormConfirmarDesvTcc(String ticket, int idActividad, IUsuario usuario)
            throws SicaException {
        Actividad actividad = (Actividad) find(Actividad.class, new Integer(idActividad));
        actividad.terminar(usuario, Actividad.RES_CONFIRMADA);
        update(actividad);
    }

    /**
     * Revisa si alg&uacute;n detalle tiene solicitud de autorizaci&oacute;n por desviaci&oacute;n
     * al tipo de cambio, si es as&iacute;, crea la actividad correspondiente.
     *
     * @param idDeal El deal a revisar.
     */
    public void solicitarAutorizacionSobreprecio(int idDeal) {
        Deal deal = findDeal(idDeal);
        
        if (!deal.isDeSucursal() &&
                !Deal.EV_SOLICITUD.equals(deal.decodificarEvento(Deal.EV_IND_SOBREPRECIO))) {
            deal.setEvento(Deal.EV_SOLICITUD, Deal.EV_IND_SOBREPRECIO);
            update(deal);            
            wfCrearActividad(Actividad.PROC_DEAL_NORMAL, Actividad.ACT_DN_SOBREPRECIO, deal, null);
        }
    }

    /**
     * @see WorkFlowServiceData#wfNormConfirmarSobreprecio(String, int,
            com.ixe.ods.seguridad.model.IUsuario).
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param idActividad El n&uacute;mero de la actividad.
     * @param usuario El Usuario que realiza la confimaci&oacute;n.
     * @throws SicaException Si ocurre alg&uacute;n error.
     */
    public void wfNormConfirmarSobreprecio(String ticket, int idActividad, IUsuario usuario)
            throws SicaException {
        Actividad actividad = findActividadWithDeal(idActividad);
        Deal deal = actividad.getDeal();
        deal.setEvento(Deal.EV_APROBACION, Deal.EV_IND_SOBREPRECIO);
        update(deal);
        actividad.terminar(usuario, Actividad.RES_AUTORIZADO);
    }

    /**
     * Si el deal no est&aacute; marcado como pendiente, llama a
     * <code>revisarBanxicoParaDeal()</code>. Posteriormente revisa si el cliente se vuelve
     * frecuente llamando a <code>revisarClienteFrecuente()</code>. Por &uacute;ltimo revisa si el
     * deal requiere autorizaci&oacute;n por documentaci&oacute;n y en este caso, crea la actividad
     * correspondiente. En caso contrario, llama a <code>wfNormEvaluarPagoAnticipado()</code> para
     * continuar con el flujo.
     *
     * @see #revisarBanxicoParaDeal(com.ixe.ods.sica.model.Deal).
     * @see #revisarClienteFrecuente(String).
     * @see #wfNormEvaluarPagoAnticipado(String, com.ixe.ods.sica.model.Deal).
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param deal El deal a revisar.
     * @throws SicaException Si algo sale mal.
     */
    private void wfNormEvaluarBanxicoYFrecuente(String ticket, Deal deal) throws SicaException {
        if (!deal.isPendiente()) {
            // Ahora va banxico si el deal no esta en status Pendiente:
            revisarBanxicoParaDeal(deal);
        }
        debug("Entro correctamente a wfNormEvaluarBanxicoYFrecuente deal:" + deal.getIdDeal());

        // Se evalua cliente frecuente:
        revisarClienteFrecuente(deal.getContratoSica().getNoCuenta());
        // Se revisa si se requiere autorizacion por falta de documentacion:
        boolean requiereAutorizacion = false;
        debug("Se revisa si requiere autorizaci\u00f3n por documentacion para deal:" +
                deal.getIdDeal());
        if (requiereAutorizacionDocumentacionParaDeal(deal)) {
            wfCrearActividad(Actividad.PROC_DEAL_NORMAL, Actividad.ACT_DN_DOCUMENTACION, deal,
                    null);
            requiereAutorizacion = true;
        }
        debug("Se revisa si requiere autorizaci\u00f3n por plantilla para deal:" +
                deal.getIdDeal());
        List detalles = requiereAutorizacionPlantillaParaDeal(deal);
        if (detalles != null && !detalles.isEmpty()) {
            Iterator itDetalles = detalles.iterator();
            while (itDetalles.hasNext()) {
                DealDetalle det = (DealDetalle) itDetalles.next();
                debug("Detalle :" + det.getIdDealPosicion() + " requiere activar plantilla");
                wfCrearActividad(Actividad.PROC_DEAL_NORMAL, Actividad.ACT_DN_PLANTILLA, deal, det);
                debug("Se genero actividad para detalle :" + det.getIdDealPosicion());
            }
            requiereAutorizacion = true;
        }
        if (!requiereAutorizacion) {
            if (Deal.EV_SOLICITUD.equals(deal.decodificarEvento(Deal.EV_IND_PLANTILLA))) {
                deal.setEvento(Deal.EV_APROBACION, Deal.EV_IND_PLANTILLA);
                deal.setEvento(Deal.EV_APROBACION, Deal.EV_IND_DOCUMENTACION);
            }
            update(deal);
            wfNormEvaluarRfc(ticket, deal);
        }
    }

    /**
     * @see WorkFlowServiceData#wfAutorizarDocumentacion(String, int,
            com.ixe.ods.seguridad.model.IUsuario).
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param idActividad El identificador de la actividad.
     * @param usuario El usuario que autoriza la documentaci&oacute;n.
     * @throws SicaException Si ocurre alg&uacute;n error.
     * @see #wfIntEvaluarLinOp(String, com.ixe.ods.sica.model.Deal).
     * @see #wfNormEvaluarPagoAnticipado(String, com.ixe.ods.sica.model.Deal).
     */
    public void wfAutorizarDocumentacion(String ticket, int idActividad, IUsuario usuario)
            throws SicaException {
        Actividad actividad = findActividadWithDeal(idActividad);
        Deal deal = actividad.getDeal();
        autorizarDocumentacionParaDeal(deal, Deal.EV_APROBACION);
        actividad.terminar(usuario, Actividad.RES_AUTORIZADO);
        update(actividad);
        if (!Deal.EV_SOLICITUD.equals(deal.decodificarEvento(Deal.EV_IND_PLANTILLA))) {
            if (deal.isInterbancario()) {
                wfIntEvaluarLinOp(ticket, deal);
            }
            else {
                wfNormEvaluarRfc(ticket, deal);
            }
        }
    }

    /**
     * @see WorkFlowServiceData#wfNegarDocumentacion(String, int,
            com.ixe.ods.seguridad.model.IUsuario).
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param idActividad El identificador de la actividad.
     * @param usuario El usuario que autoriza la documentaci&oacute;n.
     * @throws SicaException Si ocurre alg&uacute;n error.
     * @see #autorizarDocumentacionParaDeal(com.ixe.ods.sica.model.Deal, String).
     */
    public void wfNegarDocumentacion(String ticket, int idActividad, IUsuario usuario)
            throws SicaException {
        Actividad actividad = findActividadWithDeal(idActividad);
        Deal deal = actividad.getDeal();
        autorizarDocumentacionParaDeal(deal, Deal.EV_NEGACION);
        actividad.terminar(usuario, Actividad.RES_NO_AUTORIZADO);
        update(actividad);
    }

    /**
     *  @see WorkFlowServiceData#wfAutorizarPlantilla(String, int, IUsuario)
     *
     */
    public void wfAutorizarPlantilla(String ticket, int idActividad, IUsuario usuario)
            throws SicaException {
        Actividad actividad = findActividadWithDeal(idActividad);
        Deal deal = actividad.getDeal();
        DealDetalle det = actividad.getDealDetalle();
        boolean autorizado = autorizarPlantillaParaDeal(deal, det, Deal.EV_APROBACION);
        actividad.terminar(usuario, Actividad.RES_AUTORIZADO);
        update(actividad);
        if (det.getPlantilla() != null) {
            det.getPlantilla().setStatusPlantilla(Plantilla.STATUS_PLANTILLA_ACTIVA);
            det.getPlantilla().setUltimaModificacion(new Date());
            update(det.getPlantilla());
        }
        if (autorizado &&
                !Deal.EV_SOLICITUD.equals(deal.decodificarEvento(Deal.EV_IND_DOCUMENTACION))) {
            if (deal.isInterbancario()) {
                wfIntEvaluarLinOp(ticket, deal);
            }
            else {
                wfNormEvaluarRfc(ticket, deal);
            }
        }
    }

    /**
     * @see WorkFlowServiceData#wfNegarPlantilla(String, int, IUsuario)
     */
    public void wfNegarPlantilla(String ticket, int idActividad, IUsuario usuario)
            throws SicaException {
        Actividad actividad = findActividadWithDeal(idActividad);
        Deal deal = actividad.getDeal();
        DealDetalle det = actividad.getDealDetalle();
        autorizarPlantillaParaDeal(deal, det, Deal.EV_NEGACION);
        actividad.terminar(usuario, Actividad.RES_NO_AUTORIZADO);
        update(actividad);
    }

    /**
     * Autoriza o niega el deal y acmbia los estatus del deal y sus detalles y crea una actividad de
     * negaci&oacute;n del deal.
     *
     * @param deal El deal a autorizar.
     * @param det El detalle de deal.
     * @param evento El evento a modificar.
     */
    private boolean autorizarPlantillaParaDeal(Deal deal, DealDetalle det, String evento) {
        det.setEvento(evento, DealDetalle.EV_IND_DOCUMENTACION);
        update(det);
        if (Deal.EV_APROBACION.equals(evento)) {
            for (Iterator it = deal.getDetalles().iterator(); it.hasNext();) {
                DealDetalle detalle = (DealDetalle) it.next();
                if (det.getIdDealPosicion() == detalle.getIdDealPosicion()) continue;
                int indEv = deal.isInterbancario()
                        ? DealDetalle.EV_IND_INT_DOCUMENTACION : DealDetalle.EV_IND_DOCUMENTACION;
                // Si no se aprueba, se queda en solicitud de autorizacion:
                if (!Deal.STATUS_DEAL_CANCELADO.equals(detalle.getStatusDetalleDeal()) &&
                        Deal.EV_SOLICITUD.equals(detalle.decodificarEvento(indEv)) &&
                        Deal.EV_APROBACION.equals(evento)) {
                    return false;
                }
            }
            deal.setEvento(evento, Deal.EV_IND_PLANTILLA);
            update(deal);
            return true;
        }
        if (Deal.EV_NEGACION.equals(evento)) {
            wfCrearActividad(deal.isInterbancario() ?
                    Actividad.PROC_DEAL_INTERBANCARIO : Actividad.PROC_DEAL_NORMAL,
                    deal.isInterbancario() ?
                            Actividad.ACT_DINT_REC_M_CTRL : Actividad.ACT_DN_REC_M_CTRL, deal,
                    null);
        }
        return true;
    }

    /**
     * Si el sem&aacute;foro de cambio en RFC est&acute; en solicitud, crea la actividad de
     * autorizaci&oacute;n por este concepto. En caso contrario eval&uacute;a la autorizaci&oacute;n
     * por cambio en email para facturaci&oacute;n electr&oacute;nica.
     *
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param deal El deal a revisar.
     * @throws SicaException Si no se puede crear la actividad.
     * @see #wfNormEvaluarEmail(String, com.ixe.ods.sica.model.Deal).
     */
    private void wfNormEvaluarRfc(String ticket, Deal deal) throws SicaException {
        if (Deal.EV_SOLICITUD.equals(deal.getCambioRfc())) {
            wfCrearActividad(Actividad.PROC_DEAL_NORMAL, Actividad.ACT_DN_RFC, deal, null);
            return;
        }
        // Si no se requiere autorizacion por cambio en rfc, se revisa cambio en correo electronico:
        wfNormEvaluarEmail(ticket, deal);
    }

    /**
     * @see WorkFlowServiceData#wfAutorizarDocumentacion(String, int,
            com.ixe.ods.seguridad.model.IUsuario).
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param idActividad El identificador de la actividad.
     * @param usuario El usuario que autoriza la documentaci&oacute;n.
     * @throws SicaException Si ocurre alg&uacute;n error.
     * @see #wfIntEvaluarLinOp(String, com.ixe.ods.sica.model.Deal).
     * @see #wfNormEvaluarPagoAnticipado(String, com.ixe.ods.sica.model.Deal).
     */
    public void wfAutorizarRfc(String ticket, int idActividad, IUsuario usuario)
            throws SicaException {
        Actividad actividad = findActividadWithDeal(idActividad);
        Deal deal = actividad.getDeal();
        deal.setCambioRfc(Deal.EV_APROBACION);
        update(deal);
        actividad.terminar(usuario, Actividad.RES_AUTORIZADO);
        update(actividad);
        wfNormEvaluarEmail(ticket, deal);
    }

    /**
     * Si el sem&aacute;foro de cambio en email est&acute; en solicitud, crea la actividad de
     * autorizaci&oacute;n por este concepto. En caso contrario eval&uacute;a la autorizaci&oacute;n
     * por pago anticipado.
     *
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param deal El deal a revisar.
     * @throws SicaException Si no se puede crear la actividad.
     * @see #wfNormEvaluarPagoAnticipado(String, com.ixe.ods.sica.model.Deal).
     */
    private void wfNormEvaluarEmail(String ticket, Deal deal) {
        if (Deal.EV_SOLICITUD.equals(deal.getCambioEmail())) {
            wfCrearActividad(Actividad.PROC_DEAL_NORMAL, Actividad.ACT_DN_EMAIL, deal, null);
            return;
        }
        // Si no se require autorizacion por cambio en email, se revisa pago anticipado.
        wfNormEvaluarPagoAnticipado(ticket, deal);
    }

    public void wfAutorizarEmail(String ticket, int idActividad, IUsuario usuario)
            throws SicaException {
        Actividad actividad = findActividadWithDeal(idActividad);
        Deal deal = actividad.getDeal();
        // Se generan los nuevos medios de contacto:
        if (StringUtils.isEmpty(deal.getEmailFacturaOtro())) {
            throw new SicaException("No est\u00e1n definidos los nuevos correos electr\u00f3nicos");
        }
        String[] emails = deal.getEmailFacturaOtro().split("\\;");
        for (int i = 0; i < emails.length; i++) {
            registraMedioDeContacto(deal.getCliente(), emails[i].trim(), deal.getUsuario());
        }
        deal.setCambioEmail(Deal.EV_APROBACION);
        String mailCompleto = (StringUtils.isEmpty(deal.getEmailFactura()) ? "" :
                (deal.getEmailFactura() + ";")) + deal.getEmailFacturaOtro().trim();
        deal.setEmailFactura(mailCompleto);
        deal.setEmailFacturaOtro(" ");
        update(deal);
        actividad.terminar(usuario, Actividad.RES_AUTORIZADO);
        update(actividad);
        wfNormEvaluarPagoAnticipado(ticket, deal);
    }

    /**
     * Utiliza el servicio de contrataci&oacute;n para registrar un medio de contacto para la
     * persona especificada.
     *
     * @param cliente El cliente al que pertenece un deal.
     * @param email La direcci&oacute;n de correo electr&oacute;nico a registrar.
     * @param usuario El usuario del deal.
     * @throws SicaException Si no se puede registrar el correo electr&oacute;nico.
     */
    private void registraMedioDeContacto(Persona cliente, String email, IUsuario usuario)
            throws SicaException {
        try {
            com.ixe.bean.bup.Persona p;
            if ("PM".equals(cliente.getTipoPersona())) {
                p = new com.ixe.bean.bup.PersonaMoral();
            }
            else {
                p = new PersonaFisica();
            }
            p.setIdPersona(cliente.getIdPersona().intValue());
            MedioContacto mc = new MedioContacto();
            mc.setIdTipoMedioContacto("3");
            mc.setMedconFE("S");
            mc.setOrden(0);
            mc.setValor(email);
            com.ixe.bean.Usuario u = new com.ixe.bean.Usuario();
            u.setIdUsuario(String.valueOf(usuario.getIdUsuario()));
            u.setIdPersona(usuario.getIdPersona().intValue());
            getRegistroMedioContactoPersonaService().registraMedioContacto(p, mc, u);
        }
        catch (ContratacionException e) {
            throw new SicaException(e.getMessage(), e);
        }
    }

    /**
     * Si el deal requiere autorizaci&oacute;n por pago anticipado crea la actividad
     * correspondiente, en caso contrario llama a <code>wfNormEvaluarTomaEnFirme()</code> para
     * continuar con el flujo.
     *
     * @param ticket El ticket de la sesi&oacute;n.
     * @param deal El deal a revisar.
     * @throws SicaException Si algo sale mal.
     */
    private void wfNormEvaluarPagoAnticipado(String ticket, Deal deal) throws SicaException {
        if ((deal.isPagoAnticipado() || deal.isTomaEnFirme()) &&
                Deal.EV_SOLICITUD.equals(deal.decodificarEvento(Deal.EV_IND_GRAL_PAG_ANT))) {
            wfCrearActividad(Actividad.PROC_DEAL_NORMAL, Actividad.ACT_DN_PAGO_ANTICIPADO, deal,
                    null);
        }
        else {
            wfSolicitarLiquidacionTesoreria(ticket, deal);
        }
    }

    /**
     * @see WorkFlowServiceData#wfAutorizarPagoAnticipado(String, int,
            com.ixe.ods.seguridad.model.IUsuario).
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param idActividad El id de la actividad.
     * @param usuario El Usuario conectado a la aplicaci&oacute;n.
     * @throws SicaException Si no se puede terminar la actividad.
     * @see #autorizarONegarCreditoDeal(String, com.ixe.ods.sica.model.Deal, boolean).
     * @see #wfIntEvaluarBanxico(String, com.ixe.ods.sica.model.Deal).
     */
    public void wfAutorizarPagoAnticipado(String ticket, int idActividad, IUsuario usuario)
        throws SicaException {
        Actividad actividad = findActividadWithDeal(idActividad);
        actividad.terminar(usuario, Actividad.RES_AUTORIZADO);
        update(actividad);
        Deal deal = actividad.getDeal();
        autorizarONegarCreditoDeal(ticket, deal, true);
        if (deal.isInterbancario()) {
            wfIntEvaluarBanxico(ticket, deal);
        }
        else {
            wfSolicitarLiquidacionTesoreria(ticket, deal);
        }
    }

    /**
     * Autoriza o niega el pago anticipado o la toma en firme de un deal.
     *
     * @param ticket El ticket para revisar las formas de pago.
     * @param deal El deal a autorizar.
     * @param autorizar True para autorizar, False para no autorizar.
     */
    private void autorizarONegarCreditoDeal(String ticket, Deal deal, boolean autorizar) {
        deal.setEvento(autorizar ? Deal.EV_APROBACION : Deal.EV_NEGACION, Deal.EV_IND_GRAL_PAG_ANT);
        if (!autorizar) {
            deal.setPagoAnticipado(false);
            deal.setTomaEnFirme(false);
        }
        update(deal);
        if (autorizar) {
            getLineaCambioServiceData().usar(ticket, deal, false);
        }
    }

    /**
     * @see WorkFlowServiceData#wfNegarPagoAnticipado(String, int,
            com.ixe.ods.seguridad.model.IUsuario).
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param idActividad El id de la actividad.
     * @param usuario El Usuario conectado a la aplicaci&oacute;n.
     * @throws SicaException Si no se puede terminar la actividad.
     * @see #autorizarONegarCreditoDeal(String, com.ixe.ods.sica.model.Deal, boolean).
     * @see #wfIntEvaluarBanxico(String, com.ixe.ods.sica.model.Deal).
     */
    public void wfNegarPagoAnticipado(String ticket, int idActividad, IUsuario usuario)
            throws SicaException {
        Actividad actividad = findActividadWithDeal(idActividad);
        actividad.terminar(usuario, Actividad.RES_NO_AUTORIZADO);
        update(actividad);
        Deal deal = actividad.getDeal();
        autorizarONegarCreditoDeal(ticket, deal, false);
        if (deal.isInterbancario()) {
            wfIntEvaluarBanxico(ticket, deal);
        }
        else {
            wfSolicitarLiquidacionTesoreria(ticket, deal);
        }
    }

    /**
     * Regresa un ticket que se obtiene a partir del usuario que se encuentra configurado en
     * sica.properties.
     *
     * @return String.
     * @throws SicaException Si no se pueden obtener las propiedades de conexi&oacute;n.
     */
    private String obtenerTicket() throws SicaException {
        try {
            String pUsr = SicaProperties.getInstance().getSicaUsr();
            String pPwd = SicaProperties.getInstance().getSicaPwd();
            String pSys = SicaProperties.getInstance().getSicaSys();
            LoginService loginService = (LoginService) _appContext.getBean("loginJtaService");
            return loginService.obtieneTicket(pUsr, pSys, pPwd, pUsr, "SICA", "0.0.0.0");
        }
        catch (SeguridadException e) {
            debug(e);
            throw new SicaException(e.getMessage());
        }
    }

    /**
     * Genera una orden de liquidaci&oacute;n y los detalles necesarios para el deal especificado.
     *
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param deal El deal que solicita la liquidaci&oacute;n.
     * @throws SicaException Si algo sale mal.
     */
    private void wfSolicitarLiquidacionTesoreria(String ticket, Deal deal) throws SicaException {
        if (deal.isEnviarAlCliente()) {
            // Se envia el deal por mail:
            ((DealMailSender) _appContext.getBean("sicaDealMailSender")).
                    enviarDealPorMail(ticket, deal);
        }
        if (getDealService().isLiquidadoPorSite(deal)) {
            debug("Deal " + deal.getIdDeal() + " es liquidado por site");
            // Se envia el deal a tesoreria, siempre y cuando el estado actual sea Operacion Normal,
            // Operacion Restringida u Operacion Vespertina:
            Estado edo = findEstadoSistemaActual();
            switch (edo.getIdEstado()) {
                case Estado.ESTADO_OPERACION_NORMAL:
                case Estado.ESTADO_OPERACION_RESTRINGIDA:
                case Estado.ESTADO_OPERACION_VESPERTINA:
                    try {
                        Liquidacion liq = getDealService().crearLiquidacion(deal, false);
                        if (deal.getIdLiquidacion() != null) {
                            warn("El deal " + deal.getIdDeal() + " ya corresponde a la "
                                    + "liquidaci\u00f3n " + deal.getIdLiquidacion());
                            getSiteService().modificaSolicitud(ticket, liq);
                            for (Iterator it = deal.getDetalles().iterator(); it.hasNext();) {
                                DealDetalle detalle = (DealDetalle) it.next();
                                if (getDealService().isLiquidadoPorSite(detalle)) {
                                    if (detalle.getIdLiquidacionDetalle() == null) {
                                        solicitarLiquidacionParaDealDetalle(ticket, detalle);                                        
                                    }
                                }
                                else if (!DealDetalle.STATUS_DET_CANCELADO.equals(detalle.
                                        getStatusDetalleDeal()) && Deal.REVERSADO != detalle.getReversado()) {
                                    detalle.setStatusDetalleDeal(DealDetalle.
                                            STATUS_DET_TOTALMENTE_PAG_LIQ);
                                    update(detalle);
                                }
                            }
                        }
                        else {
                            Set detallesLiquidacion = new HashSet();
                            if (deal.getDetalles() != null) {
                                for (Iterator it = deal.getDetalles().iterator(); it.hasNext();) {
                                    DealDetalle detalle = (DealDetalle) it.next();
                                    if (getDealService().isLiquidadoPorSite(detalle)) {
                                        if (detalle.getIdLiquidacionDetalle() == null) {
                                            if (!detalle.isCancelado()
                                                    && !detalle.isStatusIn(new String[]{
                                                    DealDetalle.STATUS_DET_PARCIALMENTE_PAG_LIQ,
                                                    DealDetalle.STATUS_DET_TOTALMENTE_PAG_LIQ})) {
                                                FormaPagoLiq fpl = getSicaSiteCache().
                                                        getFormaPagoLiq(ticket,
                                                                detalle.getMnemonico());
                                                DealService dealService = (DealService) _appContext.
                                                        getBean("dealService");
                                                DetalleLiquidacion detLiq = dealService.
                                                        crearDetalleLiquidacion(detalle, fpl);
                                                detallesLiquidacion.add(detLiq);
                                            }
                                        }
                                    }
                                    else if (!DealDetalle.STATUS_DET_CANCELADO.equals(detalle.
                                            getStatusDetalleDeal()) && Deal.REVERSADO != detalle.getReversado()) {
                                        detalle.setStatusDetalleDeal(DealDetalle.
                                                STATUS_DET_TOTALMENTE_PAG_LIQ);
                                        update(detalle);
                                    }
                                }
                            }
                            liq.setDetallesLiquidacion(detallesLiquidacion);
                            FoliosDeal foliosDeal = getSiteService().agregarSolicitud(ticket, liq);
                            Map mapaDetalles = foliosDeal.getFolios();
                            deal.setIdLiquidacion(new Integer(foliosDeal.
                                    getIdLiquidacion().intValue()));
                            for (Iterator it = mapaDetalles.keySet().iterator(); it.hasNext();) {
                                Integer folioDet = (Integer) it.next();
                                DealDetalle det = deal.getDetalleConFolio(folioDet.intValue());
                                det.setIdLiquidacionDetalle(new Integer(
                                        ((Long) mapaDetalles.get(folioDet)).intValue()));
                                update(det);
                            }
                            update(deal);
                        }
                        
                        ArrayList listaDeals = new ArrayList();
                        listaDeals.add(deal);
                        getH2hService().registrarDetallesH2H(listaDeals, H2HService.PROCESAR_TODOS_LOS_DETALLES, H2HService.NO_ES_CIERRE_VESPERTINO);
                    }
                    catch (ExternalSiteException e) {
                        debug(e);
                        // NO CAMBIAR A SicaException!:
                        throw new RuntimeException( "ExternalSiteException" + e.getMessage());
                    }
                    catch (SeguridadException e) {
                        debug(e);
                        // NO CAMBIAR A SicaException!:
                        throw new RuntimeException(e.getMessage());
                    }
                    break;
                default:
                    warn("El deal " + deal.getIdDeal() + " est\u00e1 pendiente de ser "
                            + "liquidado por el site, debido a que el estado del sistema se "
                            + "encuentra en " + edo.getIdEstado());
            }
        }
        else {
            warn("Deal " + deal.getIdDeal() + " no es liquidado por site");
            deal.setStatusDeal(Deal.STATUS_DEAL_TOTALMENTE_LIQUIDADO);
            update(deal);
            for (Iterator it = deal.getDetalles().iterator(); it.hasNext();) {
                DealDetalle detalle = (DealDetalle) it.next();
                if (!DealDetalle.STATUS_DET_CANCELADO.equals(detalle.getStatusDetalleDeal()) &&
                        Deal.REVERSADO != detalle.getReversado()) {
                    detalle.setStatusDetalleDeal(DealDetalle.STATUS_DET_TOTALMENTE_PAG_LIQ);
                    update(detalle);
                }
            }
        }
    }

    /**
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param idDeal El n&uacute;mero de deal del que se solicita la cancelaci&oacute;n.
     * @param usuario El usuario que solicita la cancelaci&oacute;n del deal.
     * @throws SicaException Si ocurre alg&uacute;n error.
     * @see WorkFlowServiceData#wfSolicitarCancelacionDeal(String, int,
            com.ixe.ods.seguridad.model.IUsuario)
     */
    public void wfSolicitarCancelacionDeal(String ticket, int idDeal, IUsuario usuario)
            throws SicaException {
        try {
            validarHorarioParaModificaciones();
            Deal deal = findDeal(idDeal);
            if (!deal.isCancelable()) {
                throw new SicaException("El deal no es cancelable.");
            }
            deal.setEvento(Deal.EV_SOLICITUD, Deal.EV_IND_GRAL_CANCELACION);
            update(deal);
            // Obtengo los workitems para el process id del deal y los cancelamos:
            terminarActividadesParaDeal(deal, Actividad.RES_SOL_CANC, usuario);
            // Si revisa si se ha solicitado la liquidacion o si se liquida en un dia posterior:
            if (deal.getIdLiquidacion() != null && deal.getIdLiquidacion().intValue() > 0
                    && !deal.isLiquidableEnElFuturo()) {
                debug("Se solicita a tesorer\u00eda la cancelaci\u00f3n del deal " + idDeal + ".");
                getSiteService().solicitarCancelacion(ticket, deal.getIdLiquidacion().intValue());
                // Se crean actividades para poder recuperar los detalles en caso de que tesoreria
                // niegue la cancelacion de alguno de los detalles:
                for (Iterator it = deal.getDetalles().iterator(); it.hasNext();) {
                    DealDetalle det = (DealDetalle) it.next();
                    if (det.isAltaOProcesandose() && det.getIdLiquidacionDetalle() != null) {
                        wfCrearActividad(Actividad.PROC_CANCELACION_DEAL,
                                Actividad.ACT_DNCA_RECUP_DET, deal, det);
                    }
                }
            }
            else {
                // Se marca como autorizado por tesoreria:
                debug("Se solicita a la mesa directamente la cancelaci\u00f3n del deal " + idDeal
                        + ".");
                deal.setEvento(Deal.EV_APROBACION_TESORERIA, Deal.EV_IND_GRAL_CANCELACION);
                update(deal);
                wfCanAutorizadaPorTesoreria(deal.getIdDeal());
            }
        }
        catch (ExternalSiteException e) {
            throw new SicaException(e.getMessage());
        }
    }

    /**
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param det El detalle de deal del que se solicita cancelaci&oacute;n.
     * @param usuario El usuario que solicita la cancelaci&oacute;n del deal.
     * @throws SicaException Si ocurre alg&uacute;n error.
     * @see WorkFlowServiceData#wfSolicitarCancelacionDetalleDeal(String,
     * com.ixe.ods.sica.model.DealDetalle, com.ixe.ods.seguridad.model.IUsuario)
     */
    public void wfSolicitarCancelacionDetalleDeal(String ticket, DealDetalle det, IUsuario usuario)
            throws SicaException {
            validarHorarioParaModificaciones();
            if (!det.isCancelacionLiqPermitida()) {
                throw new SicaException("El detalle de deal no es cancelable.");
            }
            det.setEvento(Deal.EV_SOLICITUD, DealDetalle.EV_IND_GRAL_CANC_DET);
            update(det);
            wfCrearActividad(Actividad.PROC_CANCELACION_DET, Actividad.ACT_DN_CANC_DET,
                    det.getDeal(), det);
    }

    /**
     * @see WorkFlowServiceData#wfCancelarDirectamenteDeal(String, int, int).
     * @param ticket El ticket de la sesi&oacute;n
     * @param idDeal El id del deal.
     * @param idUsuario El id del usuario que hace la petici&oacute;n.
     * @throws SicaException Si ocurre alg&uacute;n problema.
     */
    public void wfCancelarDirectamenteDeal(String ticket, int idDeal, int idUsuario)
            throws SicaException {
        Deal deal = findDeal(idDeal);
        if (!deal.isCancelable()) {
            throw new SicaException("El deal " + idDeal + " no es cancelable.");
        }
        IUsuario usuario = (IUsuario) find(Usuario.class, new Integer(idUsuario));
        // Obtengo los workitems para el process id del deal y los cancelamos:
        terminarActividadesParaDeal(deal, Actividad.RES_SOL_CANC, usuario);
        marcarDealCancelado(ticket, idDeal);
    }

    /**
     * <p>Itera todos los detalles del deal y llama a marcarDealDetalleCancelado(). Si el deal es
     * interbancario, y tiene un swap asignado:</p>
     * <ul>
     *  <li>Es inicio del swap.- Se marca como cancelado el swap.</li>
     *  <li>Es contraparte del swap.- Actualiza el registro relacionado de BitacoraEnviadas. Se
     *  decrementa el monto asignado y si este se vuelve cero, pone en <i>IN</i> el status del swap,
     *  en caso contrario, pone el status en <i>PA</i>.
     *  </li>
     * </ul>
     *
     * @param idDeal El deal a cancelar.
     * @see #marcarDealDetalleCancelado(String, com.ixe.ods.sica.model.DealDetalle, boolean,
     *          boolean).
     * @throws SicaException Si no se puede calcular la fecha valor de la cancelaci&ocute;n.
     */
    public void marcarDealCancelado(int idDeal) throws SicaException {
        marcarDealCancelado(obtenerTicket(), idDeal);
    }

    /**
     * Si el deal interbancario tiene una solicitud de autorizaci&oacute;n de l&iacute;nea de
     * operaci&oacute;n, crea la actividad correspondiente. En caso contrario, llama a
     * <code>wfIntEvaluarLinCred()</code> para continuar con el flujo y evaluar la l&iacute;nea de
     * cr&eacute;dito.
     *
     * @param ticket El ticket de la sesi&oacute;n.
     * @param deal El deal interbancario al que se eval&uacute;a la l&iacute;nea de
     *  operaci&oacute;n.
     * @see #wfIntEvaluarLinCred(String, com.ixe.ods.sica.model.Deal).
     * @throws SicaException Si algo sale mal.
     */
    private void wfIntEvaluarLinOp(String ticket, Deal deal) throws SicaException {
        if (Deal.EV_SOLICITUD.equals(deal.decodificarEvento(Deal.EV_IND_INT_LINEA_OPERACION))) {
            wfCrearActividad(Actividad.PROC_DEAL_INTERBANCARIO, Actividad.ACT_DINT_LINEA_OPERACION,
                    deal, null);
        }
        else {
            wfIntEvaluarLinCred(ticket, deal);
        }
    }

    /**
     * @see WorkFlowServiceData#wfIntAutorizarLineaOperacion(String, int,
            com.ixe.ods.seguridad.model.IUsuario).
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param idActividad El id de la actividad.
     * @param usuario El Usuario Logeado a la Aplicacion.
     * @see com.ixe.ods.sica.sdo.SicaServiceData#usoLiberacionLineaOperacion(
            com.ixe.ods.sica.model.LineaOperacion, String, com.ixe.ods.sica.model.DealDetalle,
                    com.ixe.ods.seguridad.model.IUsuario, boolean).
     * @throws SicaException Si ocurre alg&uacute;n error.
     */
    public void wfIntAutorizarLineaOperacion(String ticket, int idActividad, IUsuario usuario)
            throws SicaException {
        Actividad actividad = findActividadWithDeal(idActividad);
        actividad.terminar(usuario, Actividad.RES_AUTORIZADO);
        update(actividad);
        Deal deal = actividad.getDeal();
        deal.setEvento(Deal.EV_APROBACION, Deal.EV_IND_INT_LINEA_OPERACION);
        update(deal);
        LineaOperacion lo = findLineaOperacionByBroker(deal.getBroker().getId().
                getPersonaMoral().getIdPersona().intValue());
        if (lo != null) {
            usoLiberacionLineaOperacion(lo, LineaOperacionLog.TIPO_OPER_USO,
                    (DealDetalle) (deal.isCompra() ? deal.getRecibimos().get(0)
                            : deal.getEntregamos().get(0)), usuario, true);
        }
        wfIntEvaluarLinCred(ticket, deal);
    }

    /**
     * @see WorkFlowServiceData#wfIntNegarAutorizacionLineaOperacion(String, int,
            com.ixe.ods.seguridad.model.IUsuario).
     * @param ticket El ticket de la sesi&oacute;n
     * @param idActividad El id de la actividad
     * @param usuario El usuario que hace la petici&oacute;.
     * @throws SicaException Si ocurre alg&uacute;n problema.
     */
    public void wfIntNegarAutorizacionLineaOperacion(String ticket, int idActividad,
                                                     IUsuario usuario) throws SicaException {
        Actividad actividad = findActividadWithDeal(idActividad);
        actividad.terminar(usuario, Actividad.RES_NO_AUTORIZADO);
        update(actividad);
        Deal deal = actividad.getDeal();
        deal.setEvento(Deal.EV_NEGACION, Deal.EV_IND_INT_LINEA_OPERACION);
        update(deal);
        marcarDealCancelado(ticket, deal.getIdDeal());
    }

    /**
     * Si el deal interbancario tiene una solicitud de autorizaci&oacute;n de l&iacute;nea de
     * cr&eacute;dito, crea la actividad correspondiente. En caso contrario, llama a
     * <code>wfIntEvaluarBanxico()</code> para continuar con el flujo y evaluar si es necesario o no
     * notificar a Banxico.
     *
     * @param ticket El ticket de la sesi&oacute;n.
     * @param deal El deal interbancario al que se eval&uacute;a la l&iacute;nea de operaci&oacute;n
     * @see #wfIntEvaluarBanxico(String, com.ixe.ods.sica.model.Deal).
     * @throws SicaException Si algo sale mal.
     */
    private void wfIntEvaluarLinCred(String ticket, Deal deal) throws SicaException {
        if (Deal.EV_SOLICITUD.equals(deal.decodificarEvento(Deal.EV_IND_GRAL_PAG_ANT))) {
            wfCrearActividad(Actividad.PROC_DEAL_INTERBANCARIO, Actividad.ACT_DINT_LINEA_CREDITO,
                    deal, null);
        }
        else {
            wfIntEvaluarBanxico(ticket, deal);
        }
    }

    /**
     * Llama a revisarBanxicoParaDeal() para realizar eso, y posteriormente llama a
     * wfSolicitarLiquidacionTesoreria() para mandar el deal al sistema SITE.
     *
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param deal El deal a evaluar.
     * @see #revisarBanxicoParaDeal(com.ixe.ods.sica.model.Deal).
     * @throws SicaException Si algo sale mal.
     */
    private void wfIntEvaluarBanxico(String ticket, Deal deal) throws SicaException {
    	
    	if (Constantes.CASH.equals(deal.getTipoValor()) && deal.isPagoAnticipado()){
    			if(isDealIntConfirmado(deal.getIdDeal())){
        revisarBanxicoParaDeal(deal);
        wfSolicitarLiquidacionTesoreria(ticket, deal);
    }
    	}else{
        revisarBanxicoParaDeal(deal);
        wfSolicitarLiquidacionTesoreria(ticket, deal);
    }
    }

    /**
     * Realiza la cancelaci&oacute;n del deal aprobada por tesoreria.
     *
     * @param idDeal El id del deal.
     * @throws SicaException Si ocurre alg&uacute;n problema.
     */
    private void wfCanAutorizadaPorTesoreria(int idDeal) throws SicaException {
        Deal deal = (Deal) find(Deal.class, new Integer(idDeal));
        deal.setEvento(Deal.EV_APROBACION_TESORERIA, Deal.EV_IND_GRAL_CANCELACION);
        update(deal);
        // Si es de sucursales, el deal se cancela directamente, en caso contrario, debe solicitarse
        // la autorizaci&oacute;n de la cancelaci&oacute;n a la mesa de cambios:
        if (deal.isDeSucursal()) {
            marcarDealCancelado(obtenerTicket(), idDeal);
        }
        else {
            wfCrearActividad(Actividad.PROC_CANCELACION_DEAL, Actividad.ACT_DNCA_ESP_AUT_MESA, deal,
                null);
        }
    }

    /**
     * @see WorkFlowServiceData#wfAutorizarCancelacionParaDeal(String, int,
            com.ixe.ods.seguridad.model.IUsuario).
     * @param ticket El ticket para llamar a SicaServiceData.
     * @param idActividad El n&uacute;mero de actividad de cancelaci&oacute;n del deal.
     * @param usuario El usuario que autoriza la cancelaci&oacute;n.
     * @throws SicaException Si ocurre alg&uacute;n error.
     */
    public void wfAutorizarCancelacionParaDeal(String ticket, int idActividad, IUsuario usuario)
            throws SicaException {
        try {
            Actividad actividad = findActividadWithDeal(idActividad);
            Deal deal = actividad.getDeal();
            deal.setEvento(Deal.EV_APROBACION, Deal.EV_IND_GRAL_CANCELACION);
            marcarDealCancelado(ticket, deal.getIdDeal());
            actividad.terminar(usuario, Actividad.RES_AUTORIZADO);
            update(actividad);
            if (deal.getIdLiquidacion() != null && deal.isLiquidableEnElFuturo()) {
                getSiteService().cancelarSolicitudConDetalles(ticket,
                        deal.getIdLiquidacion().intValue());
            }
            sendAlerta(DealHelper.getContextoAlertas(deal));
        }
        catch (ExternalSiteException e) {
            throw new SicaException(e.getMessage());
        }
        catch (SeguridadException e) {
            throw new SicaException(e.getMessage());
        }
    }
    
    /**
     * @see WorkFlowServiceData#actualizarActividadNotificacion(String, int,
            com.ixe.ods.seguridad.model.IUsuario).
     * @param ticket El ticket para llamar a SicaServiceData.
     * @param idActividad El n&uacute;mero de actividad de cancelaci&oacute;n del deal.
     * @param usuario El usuario que autoriza la cancelaci&oacute;n.
     * @throws SicaException Si ocurre alg&uacute;n error.
     */
    public void actualizarActividadNotificacion(String ticket, int idActividad, IUsuario usuario)
            throws SicaException {
            Actividad actividad = findActividadWithDeal(idActividad);
            actividad.terminar(usuario, Actividad.RES_AUTORIZADO);
            update(actividad);
    }

    /**
     * Regresa la referencia al bean jtaBitacoraEnviadasDao.
     *
     * @return BitacoraEnviadasDao.
     */
    protected BitacoraEnviadasDao getBitacoraEnviadasDao() {
    	//Para la migracion de SC_BITACORA_ENVIADAS a la BD del SICA
        return (BitacoraEnviadasDao) _appContext.getBean("jtaBitacoraEnviadasDao");
    }
    
    /**
     * Obtiene el DAO de informacion de negociacion
     * 
     * @return
     */
    protected InformacionNegociacionDao getInformacionNegociacionDao() {
    	return (InformacionNegociacionDao) _appContext.getBean("informacionNegociacionDao");
    }

    /**
     * @see WorkFlowServiceData#wfAutorizarCancelacionParaDetalleDeal(String, int,
            com.ixe.ods.seguridad.model.IUsuario).
     * @param ticket El ticket para llamar a SicaServiceData.
     * @param idActividad El n&uacute;mero de actividad de cancelaci&oacute;n del deal.
     * @param usuario El usuario que autoriza la cancelaci&oacute;n.
     * @throws SicaException Si ocurre alg&uacute;n error.
     */
    public void wfAutorizarCancelacionParaDetalleDeal(String ticket, int idActividad,
                                                      IUsuario usuario) throws SicaException {
        Actividad actividad = findActividadWithDeal(idActividad);
        actividad.terminar(usuario, Actividad.RES_AUTORIZADO);
        update(actividad);
        DealDetalle det = actividad.getDealDetalle();
        String statusAnterior = det.getStatusDetalleDeal();
        det.setEvento(Deal.EV_APROBACION, DealDetalle.EV_IND_GRAL_CANC_DET);
        det.setStatusDetalleDeal(Deal.STATUS_DEAL_CANCELADO);
        if (Deal.EV_POSICION.equals(det.decodificarEvento(
                DealDetalle.EV_IND_GRAL_AFECTADA_POSICION))) {
            String fechaValor = ((PizarronServiceData) _appContext.getBean("jtaPizarronServiceData")).
                    fechaValorParaCancelacion(det.getDeal().getFechaCaptura(),
                            det.getDeal().getTipoValor(), false);
            PosicionLog pos = new PosicionLog(det, det.getStatusPosLogParaCancelacion(),
                    findPrecioReferenciaActual().getPreRef().getPrecioSpot().doubleValue(),
                    fechaValor);
            store(pos);
        }
        update(det);

        getLineaCambioServiceData().liberarLineaCreditoDealDetalle(ticket, det);
        
        validarInformacionRegulatoria(det.getDeal(), det.getDivisa(), det, statusAnterior);
        
        getH2hService().solicitarCancelacionDetalleH2H(det, H2HService.NO_ES_REVERSO);
    }

   

	/**
     * @see WorkFlowServiceData#wfNegarCancelacionParaDeal(String, int,
            com.ixe.ods.seguridad.model.IUsuario).
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param idActividad El id de la actividad de autorizaci&oacute;n de cancelaci&oacute;n.
     * @param usuario El usuario que neg&oacute; la cancelaci&oacute;n del deal.
     * @throws SicaException Si no se puede terminar la actividad, o si no se puede iniciar de nuevo
     *  el workflow.
     */
    public void wfNegarCancelacionParaDeal(String ticket, int idActividad, IUsuario usuario)
            throws SicaException {
        Actividad actividad = findActividadWithDeal(idActividad);
        Deal deal = actividad.getDeal();
        if (deal.isLiquidableEnElFuturo()) {
            // Por instruccion de German Angulo 2007-09-14:
            throw new SicaException("No se puede denegar la cancelaci\u00f3n de un deal TOM, "
                    + "SPOT o posterior.");
        }
        actividad.terminar(usuario, Actividad.RES_NO_AUTORIZADO);
        update(actividad);
        deal.setIdLiquidacion(null);
        deal.setEvento(Deal.EV_NEGACION, Deal.EV_IND_GRAL_CANCELACION);
        deal.setStatusDeal(Deal.STATUS_DEAL_PROCESO_CAPTURA);
        update(deal);
        // Limpiamos los detalles de liquidacion para que se pueda reprocesar:
        for (Iterator it = deal.getDetalles().iterator(); it.hasNext();) {
            DealDetalle detalle = (DealDetalle) it.next();
            detalle.setIdLiquidacionDetalle(null);
            detalle.setFolioDetalle(DealHelper.getSiguienteFolioDetalle(deal));
            update(detalle);
        }
        if (Deal.STATUS_DEAL_PROCESO_CAPTURA.equals(deal.getStatusDeal())) {
            if (!deal.isInterbancario()
                    && (deal.tieneAutPendientesHorario() || deal.tieneAutPendientesMonto())) {
                for (Iterator it = deal.getDetalles().iterator(); it.hasNext();) {
                    DealDetalle det = (DealDetalle) it.next();
                    if (det.tieneAutPendientesHorario()) {
                        wfCrearActividad(Actividad.PROC_HORARIO_MONTO, Actividad.ACT_DN_HORARIO,
                                deal, det);
                    }
                    else if (det.tieneAutPendientesMonto()) {
                        wfCrearActividad(Actividad.PROC_HORARIO_MONTO, Actividad.ACT_DN_MONTO,
                                deal, det);
                    }
                }
            }
        }
        else {
            wfIniciarProcesoDeal(ticket, deal.getIdDeal());
        }
    }

    /**
     * @see WorkFlowServiceData#wfNegarCancelacionParaDetalleDeal(String, int,
     * com.ixe.ods.seguridad.model.IUsuario).
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param idActividad El id de la actividad de autorizaci&oacute;n de cancelaci&oacute;n.
     * @param usuario El usuario que neg&oacute; la cancelaci&oacute;n del deal.
     * @throws SicaException Si no se puede terminar la actividad, o si no se puede iniciar de nuevo
     *  el workflow.
     */
    public void wfNegarCancelacionParaDetalleDeal(String ticket, int idActividad, IUsuario usuario)
            throws SicaException {
        Actividad actividad = findActividadWithDeal(idActividad);
        actividad.terminar(usuario, Actividad.RES_NO_AUTORIZADO);
        update(actividad);
        Deal deal = actividad.getDeal();
        deal.setStatusDeal(Deal.STATUS_DEAL_PROCESO_CAPTURA);
        update(deal);
        DealDetalle det = actividad.getDealDetalle();
        det.setEvento(Deal.EV_NEGACION, DealDetalle.EV_IND_GRAL_CANC_DET);
        update(det);
    }

    /**
     * Realiza la notificaci&oacute;n de deals a banxico cuando as&iacute; se requiera.
     *
     * @param deal El deal a revisar.
     */
    private void revisarBanxicoParaDeal(Deal deal) {
        debug("Revisando Banxico...");
        List bes = new ArrayList();
        String evBanxicoCpa = deal.decodificarEvento(Deal.EV_IND_GRAL_BANXICO_C);
        String evBanxicoVta = deal.decodificarEvento(Deal.EV_IND_GRAL_BANXICO_V);
        if (!Deal.EV_NO_DETERMINADO.equals(evBanxicoCpa)
                && !Deal.EV_NO_DETERMINADO.equals(evBanxicoVta)) {
            debug("El deal " + deal.getIdDeal() + " ya hab\u00eda sido revisado para Banxico.");
            return;
        }
        else {
            deal.setEvento(Deal.EV_NORMAL, Deal.EV_IND_GRAL_BANXICO_C);
            deal.setEvento(Deal.EV_NORMAL, Deal.EV_IND_GRAL_BANXICO_V);
            update(deal);
        }
        DealDetalle det;
        double montoUSDRecibimos;
        double montoUSDEntregamos;
        String swap;
        BitacoraEnviadas be;
        String divisa;
        Map detallesDivisas = new HashMap();
        detallesDivisas.putAll(getDealService().getDetallesDivisas(deal));
        ParametroSica par = findParametro(ParametroSica.MONTO_MAXIMO_DEAL);
        double montoMaximoDeal = Double.valueOf(par.getValor()).doubleValue();
        Divisa divisaObj;
        int folioDetalle;
        boolean isMenorMontoMaximoDeal;
        par = findParametro(ParametroSica.ID_PERSONA_IXE_FORWARD);
        int idPersonaIxeForward = Integer.valueOf(par.getValor()).intValue();
        String divisaTmp;

        //Si el Cliente es IXE FORWARD no se Reporta la Operacion a Banxico.
        if (deal.getCliente().getIdPersona().intValue() != idPersonaIxeForward) {
            //Iterando los Detalles Agrupados por Divisa para ver que Detalle se Reporta a Banxico
            //y cual solo se escribe en SC_BITACORA_ENVIADAS
            for (Iterator iterator = detallesDivisas.keySet().iterator(); iterator.hasNext(); ) {
                montoUSDRecibimos = 0.0;
                montoUSDEntregamos = 0.0;
                divisa = (String) iterator.next();
                // Obteniendo el Monto del Detalle ya sea de Recibimos (Compras) o de Entregamos
                // (Ventas)
                for (Iterator it = ((List) detallesDivisas.get(divisa)).iterator();
                     it.hasNext(); ) {
                    det = (DealDetalle) it.next();
                    if (det.isCancelado()) {
                        continue;
                    }
                    folioDetalle = det.getFolioDetalle();
                    // Si la Divisa es No Frecuente para Banxico hay que Reportarla como "OTD"
                    // (Otras Divisas)
                    divisaObj = findDivisa(divisa);
                    if (divisaObj.isNoFrecuente()) {
                    	divisaTmp = Divisa.OTRAS_DIVISAS;
                    	if (det.isRecibimos()) {
                        	montoUSDRecibimos = det.getImporte() / findPrecioReferenciaActual().
                                    getPreRef().getMidSpot().doubleValue();
                        }
                        else {
                            montoUSDEntregamos = det.getImporte() / findPrecioReferenciaActual().
                                    getPreRef().getMidSpot().doubleValue();
                        }
                    }
                    else {
                    	divisaTmp = divisa;
                    	if (divisaObj.isMetalAmonedado()) {
                    		if (det.isRecibimos()) {
                            	montoUSDRecibimos = det.getImporte() / findPrecioReferenciaActual().
                                        getPreRef().getMidSpot().doubleValue();
                            }
                            else {
                                montoUSDEntregamos = det.getImporte()
                                        / findPrecioReferenciaActual().getPreRef().getMidSpot().
                                                doubleValue();
                            }
                    	}
                    	else {
	                    	if (det.isRecibimos()) {
	                        	montoUSDRecibimos = det.getMontoUSD();
	                        }
	                        else {
	                            montoUSDEntregamos = det.getMontoUSD();
	                        }
                    	}
                    }
                    getHibernateTemplate().refresh(deal.getCliente().getSectorEconomico());
                    //Si se trata de un Swap se Reporta la Operacion sin importar el Monto.
                    if (deal.getSwap() != null) {
                    	swap = "1";
                        isMenorMontoMaximoDeal = false;
                        //Enviar notificacion a Banxico.
                        logger.info("Notificando a Banxico sobre " +
                                (det.isRecibimos() ? "compra " : "venta ") + "en el deal " +
                                deal.getIdDeal());
                        be = BitacoraEnviadasHelper.crearBitacoraEnviadas(deal, folioDetalle,
                                det.isRecibimos() ? BitacoraEnviadas.TIPO_OPE_COMPRA :
                                        BitacoraEnviadas.TIPO_OPE_VENTA,
                                det.isRecibimos() ? new Double(montoUSDRecibimos) :
                                    new Double(montoUSDEntregamos),
                                swap, divisaTmp, new Integer(deal.getSwap().getIdFolioSwap()),
                                BitacoraEnviadas.STATUS_ENVIAR);
                        be.setModoCreacionFolioSwap(BitacoraEnviadas.MODO_CREACION_FOLIO_SWAP);
                    }
                    // Si el Cliente es un Banco, Casa de Cambios o Casa de Bolsa se Reporta la
                    // Operacion sin importar el Monto.
                    else if (StringUtils.isEmpty(deal.getCliente().getSectorEconomico().
                            getCveContraparteAgpda())) {
                        swap = "0";
                        isMenorMontoMaximoDeal = false;
                        //Enviar notificacion a Banxico.
                            logger.info("Notificando a Banxico sobre " +
                                    (det.isRecibimos() ? "compra " : "venta ") + "en el deal " +
                                    deal.getIdDeal());
                            be = BitacoraEnviadasHelper.crearBitacoraEnviadas(deal, folioDetalle,
                                    det.isRecibimos() ? BitacoraEnviadas.TIPO_OPE_COMPRA :
                                            BitacoraEnviadas.TIPO_OPE_VENTA,
                                    det.isRecibimos() ? new Double(montoUSDRecibimos) :
                                        new Double(montoUSDEntregamos),
                                    swap, divisaTmp, null, BitacoraEnviadas.STATUS_ENVIAR);
                            be.setModoCreacionFolioSwap(
                                    BitacoraEnviadas.MODO_CREACION_FOLIO_SEQUENCE);
                    }
                    //Si el Cliente no es Banco, no se trata de un Swap y el Monto es mayor
                    //o menor a $ 1,000,000.00 (ParametroSica Monto Maximo Deal).
                    else {
                        swap = "0";
                        //Enviar notificacion a Banxico.
                        if (det.isRecibimos()) {
                            logger.info("\n\n\nNotificando a Banxico sobre compra en el deal "
                                    + deal.getIdDeal());
                            if (montoUSDRecibimos >= montoMaximoDeal) {
                                isMenorMontoMaximoDeal = false;
                                be = BitacoraEnviadasHelper.crearBitacoraEnviadas(deal,
                                        folioDetalle, BitacoraEnviadas.TIPO_OPE_COMPRA,
                                     new Double(montoUSDRecibimos), swap, divisaTmp, null,
                                     BitacoraEnviadas.STATUS_ENVIAR);
                                be.setModoCreacionFolioSwap(
                                        BitacoraEnviadas.MODO_CREACION_FOLIO_SEQUENCE);
                            }
                            else {
                                isMenorMontoMaximoDeal = true;
                                be = BitacoraEnviadasHelper.crearBitacoraEnviadas(deal,
                                        folioDetalle, BitacoraEnviadas.TIPO_OPE_COMPRA,
                                     new Double(montoUSDRecibimos), swap, divisaTmp, null,
                                     BitacoraEnviadas.STATUS_ENVIAR);
                                be.setModoCreacionFolioSwap(
                                        BitacoraEnviadas.MODO_CREACION_FOLIO_NULL);
                            }
                        }
                        else {
                            logger.info("\n\n\nNotificando a Banxico sobre venta en el deal "
                                    + deal.getIdDeal());
                            if (montoUSDEntregamos >= montoMaximoDeal) {
                                isMenorMontoMaximoDeal = false;
                                be = BitacoraEnviadasHelper.crearBitacoraEnviadas(deal,
                                        folioDetalle, BitacoraEnviadas.TIPO_OPE_VENTA,
                                        new Double(montoUSDEntregamos), swap, divisaTmp, null,
                                     BitacoraEnviadas.STATUS_ENVIAR);
                                be.setModoCreacionFolioSwap(
                                        BitacoraEnviadas.MODO_CREACION_FOLIO_SEQUENCE);
                            }
                            else {
                                isMenorMontoMaximoDeal = true;
                                be = BitacoraEnviadasHelper.crearBitacoraEnviadas(deal,
                                        folioDetalle,
                                        BitacoraEnviadas.TIPO_OPE_VENTA,
                                     new Double(montoUSDEntregamos), swap, divisaTmp, null,
                                     BitacoraEnviadas.STATUS_ENVIAR);
                                be.setModoCreacionFolioSwap(
                                        BitacoraEnviadas.MODO_CREACION_FOLIO_NULL);
                            }
                        }
                    }
                    be.setContraparte(findCveBanxicoByIdPersona(deal.getCliente().getIdPersona()),
                    		deal.getCliente().getNombreCompleto(), deal.getCliente().getRfc(),
                            deal.getTipoContraparte(), isMenorMontoMaximoDeal);
                    bes.add(be);
                }
            }
            //Para la migracion de SC_BITACORA_ENVIADAS a la BD del SICA
            BitacoraEnviadasDao beDao = (BitacoraEnviadasDao) _appContext.
                    getBean("bitacoraEnviadasDao");
            beDao.insertarBitacorasEnviadas(bes);
        }
        debug("Banxico revisado.");
    }

    /**
     * Recibe un n&uacute;mero de contrato sica y revisa que el n&uacute;mero y monto de operaciones
     * en el mes para ese contrato no exceda de l&iacute;mite permitido
     * (ParametroSica.MONTO_MAXIMO_RECURRENTE y MONTO_MAXIMO_RECURRENTE_OP).
     * Si es as&iacute;n, marca el contrato sica como 'recurrente'.
     *
     * @param noCuenta El n&uacute;mero de contrato sica.
     */
    private void revisarClienteFrecuente(String noCuenta) {
        ParametroSica pmmr = (ParametroSica) find(ParametroSica.class,
                ParametroSica.MONTO_MAXIMO_RECURRENTE);
        ParametroSica pmmrop = (ParametroSica) find(ParametroSica.class,
                ParametroSica.MONTO_MAXIMO_RECURRENTE_OP);
        double montoMaxRec = Double.valueOf(pmmr.getValor()).doubleValue();
        double montoMaxOper = Double.valueOf(pmmrop.getValor()).doubleValue();
        ContratoSica cs = (ContratoSica) find(ContratoSica.class, noCuenta);
        if (cs.getMontoOperacionesMes() > montoMaxRec
                || (cs.getNumeroOperacionesMes() > 3
                && cs.getMontoOperacionesMes() > montoMaxOper)) {
            cs.setRecurrente("S");
            update(cs);
        }
    }

    /**
     * Regresa true si el deal requiere autorizaci&oacute;n por documentaci&oacute;n. Los deals de
     * sucursales regresan siempre false y se limpia el evento de documentaci&oacute;n de encabezado
     * de deal y de sus detalles.
     *
     * @param deal El deal a revisar.
     * @return boolean.
     */
    private boolean requiereAutorizacionDocumentacionParaDeal(Deal deal) {
        if (!deal.isInterbancario() && deal.isDeSucursal()) {
            deal.setEvento(Deal.EV_NORMAL, Deal.EV_IND_DOCUMENTACION);
            update(deal);
            return false;
        }
        // Se revisa el deal si no es de sucursales:
        if (Deal.EV_SOLICITUD.equals(deal.decodificarEvento(deal.isInterbancario()
                ? Deal.EV_IND_INT_DOCUMENTACION : Deal.EV_IND_DOCUMENTACION))) {
            return true;
        }
        return false;
    }

    /**
     * Regresa true si el deal requiere autorizaci&oacute;n por documentaci&oacute;n. Los deals de
     * sucursales regresan siempre false y se limpia el evento de documentaci&oacute;n de encabezado
     * de deal y de sus detalles. Sustituye al metodo anterior
     *
     * @param deal El deal a revisar.
     * @return boolean.
     */
    private List requiereAutorizacionPlantillaParaDeal(Deal deal) {
        List detalles = new LinkedList();
        if (!deal.isInterbancario() && deal.isDeSucursal()) {
            deal.setEvento(Deal.EV_APROBACION, Deal.EV_IND_PLANTILLA);
            update(deal);
            for (Iterator it = deal.getDetalles().iterator(); it.hasNext();) {
                DealDetalle detalle = (DealDetalle) it.next();
                detalle.setEvento(Deal.EV_APROBACION, DealDetalle.EV_IND_DOCUMENTACION);
                update(detalle);
            }
            return detalles;
        }
        for (Iterator it = deal.getDetalles().iterator(); it.hasNext();) {
            DealDetalle det = (DealDetalle) it.next();
            if (!det.isCancelado() && Deal.EV_SOLICITUD.equals(det.decodificarEvento(
                    deal.isInterbancario() ? DealDetalle.EV_IND_INT_DOCUMENTACION
                            : DealDetalle.EV_IND_DOCUMENTACION))) {
                detalles.add(det);
            }
        }
        if (!detalles.isEmpty()) {
            deal.setEvento(Deal.EV_SOLICITUD, Deal.EV_IND_PLANTILLA);
            update(deal);
        }
        return detalles;
    }

    /**
	 * Crea una nueva actividad para un deal.
     *
	 * @param proceso el estado de proceso del deal
	 * @param nombre el nombre de la actividad
     * @param deal el deal al que se agrega la actividad
     * @param det el detalle que genera la actividad
     */
    public void lfCrearActividad(String proceso, String nombre, Deal deal,
                                 DealDetalle det) {
        Actividad actividad = new Actividad(proceso, nombre, deal, det);
        actividad.setStatus(Actividad.STATUS_PENDIENTE);
        this.getHibernateTemplate().save(actividad);
    }

    /**
     * <p>Actualiza el status de documentaci&oacute;n del deal (interbancario o normal), de acuerdo
     * al evento recibido, al igual que el status de documentaci&oacute;n de los detalles de deal
     * con solicitud de autorizaci&oacute;n por falta de documentaci&oacute;n.</p>
     * <p>Si el status es no autorizado, crea la actividad de rechazado por mesa de control.
     *
     * @param deal El deal a aceptar o negar la autorizaci&oacute;n por documentaci&oacute;n.
     * @param evento Deal.EV_APROBACION | Deal.EV_NEGACION.
     * @see #marcarDealCancelado(int).
     */
    private void autorizarDocumentacionParaDeal(Deal deal, String evento) {
        deal.setEvento(evento, deal.isInterbancario() ?
                Deal.EV_IND_INT_DOCUMENTACION : Deal.EV_IND_DOCUMENTACION);
        update(deal);
        /*
        for (Iterator it = deal.getDetalles().iterator(); it.hasNext();) {
            DealDetalle detalle = (DealDetalle) it.next();
            int indEv = deal.isInterbancario()
                    ? DealDetalle.EV_IND_INT_DOCUMENTACION : DealDetalle.EV_IND_DOCUMENTACION;
            // Si no se aprueba, se queda en solicitud de autorizacion:
            if (Deal.EV_SOLICITUD.equals(detalle.decodificarEvento(indEv))
                    && Deal.EV_APROBACION.equals(evento)) {
                detalle.setEvento(evento, indEv);
                update(detalle);
            }
        }*/
        if (Deal.EV_NEGACION.equals(evento)) {
            wfCrearActividad(deal.isInterbancario() ?
                    Actividad.PROC_DEAL_INTERBANCARIO : Actividad.PROC_DEAL_NORMAL,
                    deal.isInterbancario() ?
                            Actividad.ACT_DINT_REC_M_CTRL : Actividad.ACT_DN_REC_M_CTRL, deal,
                    null);
        }
    }

	/**
     * <p>Actualiza el status de p del deal (interbancario o normal), de acuerdo
     * al evento recibido, al igual que el status de documentaci&oacute;n de los detalles de deal
     * con solicitud de autorizaci&oacute;n por falta de documentaci&oacute;n.</p>
     * <p>Si el status es no autorizado, crea la actividad de rechazado por mesa de control.
     *
     * @param deal El deal a aceptar o negar la autorizaci&oacute;n por documentaci&oacute;n.
     * @param evento Deal.EV_APROBACION | Deal.EV_NEGACION.
     *

	public void wfAutorizarPlantilla(String ticket, int idActividad,
			IUsuario usuario) throws SicaException {
		 deal.setEvento(evento, deal.isInterbancario() ?
	                Deal.EV_IND_INT_DOCUMENTACION : Deal.EV_IND_DOCUMENTACION);

	        update(deal);

	}*/

    /**
     * Si el detalle de deal no tiene asignado un detalle de n&uacute;mero de liquidaci&ocute;n y
     * adem&aacute;s no est&aacute; cancelado, llama al servicio agregarDetalleSolicitud() del SITE
     * para crear un nuevo detalle de liquidaci&oacute;n para este detalle de deal.
     *
     * @param ticket El ticket de la sesi&oacute;n.
     * @param det El detalle de deal.
     * @see SiteService#agregarDetalleSolicitud(String, Long, DetalleLiquidacion)
     * @throws SicaException Si algo sale mal.
     */
    private void solicitarLiquidacionParaDealDetalle(String ticket, DealDetalle det)
            throws SicaException {
        try {
            if (det.getIdLiquidacionDetalle() != null) {
                warn("El detalle del deal " + det.getIdDealPosicion() + " ya corresponde al "
                        + "detalle de  liquidaci\u00f3n " + det.getIdLiquidacionDetalle());
            }
            if (! det.isCancelado() && !det.isStatusIn(new String[] {
                    DealDetalle.STATUS_DET_PARCIALMENTE_PAG_LIQ,
                    DealDetalle.STATUS_DET_TOTALMENTE_PAG_LIQ })) {
                FormaPagoLiq fpl = getSicaSiteCache().getFormaPagoLiq(ticket, det.getMnemonico());
                DealService dealService = (DealService) _appContext.getBean("dealService");
                DetalleLiquidacion detLiq = dealService.crearDetalleLiquidacion(det, fpl);
                if (det.getDeal().getIdLiquidacion() == null) {
                    throw new SicaException("No puede crearse un detalle de liquidaci\u00f3n para "
                            + "el detalle " + det.getIdDealPosicion() + ", pues el deal "
                        + det.getDeal().getIdDeal() + " no tiene liquidaci\u00f3n asignada.");
                }
                det.setIdLiquidacionDetalle(
                        new Integer(getSiteService().agregarDetalleSolicitud(ticket,
                                new Long(det.getDeal().getIdLiquidacion().intValue()), detLiq).
                                intValue()));
                update(det);
            }
        }
        catch (ExternalSiteException e) {
            warn(e.getMessage(), e);
            throw new SicaException(e.getMessage());
        }
        catch (SeguridadException e) {
            warn(e.getMessage(), e);
            throw new SicaException(e.getMessage());
        }
    }

    /**
     * Regresa el detalle de deal que corresponde al n&uacute;mero de detalle de liquidaci&acute;n
     * especificado.
     *
     * @param idLiquidacionDetalle El n&uacute;mero de detalle de liquidaci&oacute;n a buscar.
     * @return DealDetalle.
     * @throws SicaException Si algo sale mal.
     */
    private DealDetalle getDealDetalleParaLiqDet(int idLiquidacionDetalle) throws SicaException {
        List dets = getHibernateTemplate().
                find("FROM DealDetalle AS dd WHERE dd.idLiquidacionDetalle = ?",
                        new Integer(idLiquidacionDetalle));
        if (dets.isEmpty()) {
            throw new SicaException("No se encontr\u00f3 el detalle de deal para el detalle de "
                    + "liquidaci\u00f3n " + idLiquidacionDetalle);
        }
        return (DealDetalle) dets.get(0);
    }

    /**
     * @see WorkFlowServiceData#recibirEntregarCompleto(Long, Long, String).
     * @param idDetalleLiq El detalle de liquidaci&oacute;n.
     * @param idDealDetalle El id del detalle de deal.
     * @throws SicaException Si algo sale mal.
     */
    public void recibirEntregarCompleto(Long idDetalleLiq, Long idDealDetalle, String fed, String msgMt)
            throws SicaException {
    	super.logger.info("* ENTRO A recibirEntregarCompleto  ");
        DealDetalle det;
        if (idDealDetalle == null) {
            det = getDealDetalleParaLiqDet(idDetalleLiq.intValue());
        }
        else {
            det = findDealDetalle(idDealDetalle.intValue());
            if (det.getIdLiquidacionDetalle() == null) {
                det.setIdLiquidacionDetalle(new Integer(idDetalleLiq.intValue()));
                update(det);
            }
        }
        det.setFed(fed);
        det.setMsgMt(msgMt);
        if (det.isRecibimos()) {
            recibirCompleto(det);
        }
        else {
            entregarCompleto(det);
        }
    }

    /**
     * Asigna el detalle de deal el estatus 'Totalmente Pagado y Liquidado'.
     *
     * @param det El detalle de deal.
     * @throws SicaException Si ocurre alg&uacute;n problema.
     */
    private void recibirCompleto(DealDetalle det) throws SicaException {
    	super.logger.info("* ENTRO A recibirCompleto  ");
        det.setStatusDetalleDeal(DealDetalle.STATUS_DET_TOTALMENTE_PAG_LIQ);
        boolean dealTotalmenteLiquidado = true;
        boolean dealTotalmentePagado = true;
        boolean interbancario = det.getDeal().isInterbancario();
        for (Iterator it = det.getDeal().getDetalles().iterator(); it.hasNext(); ) {
            DealDetalle det2 = (DealDetalle) it.next();
            if (!DealDetalle.STATUS_DET_TOTALMENTE_PAG_LIQ.equals(det2.getStatusDetalleDeal()) &&
                    !DealDetalle.STATUS_DET_CANCELADO.equals(det2.getStatusDetalleDeal()) &&
                    Deal.REVERSADO != det2.getReversado()) {
                if (det2.isRecibimos()) {
                    dealTotalmentePagado = false;
                }
                else {
                    dealTotalmenteLiquidado = false;
                }
            }
        }
        Deal deal = det.getDeal();
        String statusDeal = deal.getStatusDeal();
        if (dealTotalmentePagado && dealTotalmenteLiquidado) {
            if (deal.getContrarioDe() != null && deal.getContrarioDe().intValue() > 0) {
                deal.setReversado(Deal.REVERSADO);
                deal.setStatusDeal(Deal.STATUS_DEAL_TOTALMENTE_LIQUIDADO);
            }
            else {
                deal.setStatusDeal(Deal.STATUS_DEAL_TOTALMENTE_LIQUIDADO);
            }
            List actsPendsLC;
            actsPendsLC = findActividadesPendientesLC(interbancario, deal.getIdDeal());
            for (Iterator it2 = actsPendsLC.iterator(); it2.hasNext(); ) {
                Actividad act = (Actividad) it2.next();
                act.terminar(deal.getUsuario(), Actividad.RES_SOL_TERM_LP);
                super.logger.info("* ENTRO A APAGAR EL SEMAFORO DE CREDITO PARA EL DEAL:  " + deal.getIdDeal());
                deal.setEvento(Deal.EV_NO_DETERMINADO, Deal.EV_IND_GRAL_PAG_ANT); // se agrego para apagar semáforo de crédito JDCH 24/04/2013
            }
            if (interbancario) {
                // Liberar lineas de operacion:
                liberarLineasOperacion(deal);
            }
        }
        else if (!Deal.STATUS_DEAL_PROCESO_CAPTURA.equals(statusDeal)) {
            if (dealTotalmentePagado) {
                deal.setStatusDeal(Deal.STATUS_DEAL_TOTALMENTE_PAGADO);
            }
            else {
                deal.setStatusDeal(Deal.STATUS_DEAL_PARCIAL_PAGADO_LIQ);
            }
        }
        List updates = new ArrayList();
        updates.add(det);
        updates.add(det.getDeal());
        update(updates);
       
        getLineaCambioServiceData().liberarLineaCreditoDealDetalle(null, det);
        
        if (dealTotalmentePagado && dealTotalmenteLiquidado) {
        	updateClientLastFiveDeals(det.getDeal());
        }
    }

    /**
     * Marca el detalle del deal como totalmente liquidado. Completa la actividad de entrega de
     * tesorer&iacute;a en la que est&aacute; detenido el detalle en el BPM.
     *
     * @param det El detalle del deal.
     * @throws SicaException Si ocurre alg&uacute;n problema.
     */
    private void entregarCompleto(DealDetalle det) throws SicaException {
    	super.logger.info("* ENTRO A entregarCompleto  ");
        boolean interbancario = det.getDeal().isInterbancario();
        det.setStatusDetalleDeal(DealDetalle.STATUS_DET_TOTALMENTE_PAG_LIQ);
        boolean dealTotalmenteLiquidado = true;
        boolean dealTotalmentePagado = true;
        for (Iterator it = det.getDeal().getDetalles().iterator(); it.hasNext();) {
            DealDetalle det2 = (DealDetalle) it.next();
            if (!DealDetalle.STATUS_DET_TOTALMENTE_PAG_LIQ.equals(det2.getStatusDetalleDeal()) &&
                    !DealDetalle.STATUS_DET_CANCELADO.equals(det2.getStatusDetalleDeal()) &&
                    Deal.REVERSADO != det2.getReversado()) {
                if (det2.isRecibimos()) {
                    dealTotalmentePagado = false;
                }
                else {
                    dealTotalmenteLiquidado = false;
                }
            }
        }
        if (dealTotalmentePagado && dealTotalmenteLiquidado) {
            if (det.getDeal().getContrarioDe() != null &&
                    det.getDeal().getContrarioDe().intValue() > 0) {
                det.getDeal().setReversado(Deal.REVERSADO);
                det.getDeal().setStatusDeal(Deal.STATUS_DEAL_TOTALMENTE_LIQUIDADO);
            }
            else {
                det.getDeal().setStatusDeal(Deal.STATUS_DEAL_TOTALMENTE_LIQUIDADO);
            }
            List actsPendsLC;
            actsPendsLC = findActividadesPendientesLC(interbancario, det.getDeal().getIdDeal());
            for (Iterator it2 = actsPendsLC.iterator(); it2.hasNext(); ) {
                Actividad act = (Actividad) it2.next();
                act.terminar(det.getDeal().getUsuario(), Actividad.RES_SOL_TERM_LP);
                super.logger.info("* ENTRO A APAGAR EL SEMAFORO DE CREDITO PARA EL DEAL:  " + det.getDeal().getIdDeal());
                det.getDeal().setEvento(Deal.EV_NO_DETERMINADO, Deal.EV_IND_GRAL_PAG_ANT);// se agrego para apagar semáforo de crédito JDCH 24/04/2013
            }
            if (interbancario) {
                // Liberar lineas de operacion:
                liberarLineasOperacion(det.getDeal());
            }
        }
        else if (!Deal.STATUS_DEAL_PROCESO_CAPTURA.equals(det.getDeal().getStatusDeal())) {
            det.getDeal().setStatusDeal(Deal.STATUS_DEAL_PARCIAL_PAGADO_LIQ);
        }
        List updates = new ArrayList();
        updates.add(det);
        updates.add(det.getDeal());
        update(updates);
       
        getLineaCambioServiceData().liberarLineaCreditoDealDetalle(null, det);

        if (dealTotalmentePagado && dealTotalmenteLiquidado) {
        	updateClientLastFiveDeals(det.getDeal());
        }
    }
    
    /**
     * Actualiza las 5 ultimas operaciones del cliente correspondiente al contrato sica que viene en el deal recibido
     * 
     * @param deal
     */
    private void updateClientLastFiveDeals(Deal deal) {
    	try {
			LOGGER.info("updateClientLastFiveDeals - Inicia ...");
			if(deal == null) {
				LOGGER.error("updateClientLastFiveDeals - Deal nulo");
				return;
			}
			if(deal.getContratoSica() == null) {
				LOGGER.error("updateClientLastFiveDeals - Contrato sica nulo");
				return;
			}
			String noContratoSica = deal.getContratoSica().getNoCuenta();
			if(StringUtils.isEmpty(noContratoSica)) {
				LOGGER.error("updateClientLastFiveDeals - Contrato sica vacio");
				return;
			}
			String lastFiveDealsCsv = getInformacionNegociacionDao().getLastFiveDealsCsv(noContratoSica);
			LOGGER.info("updateClientLastFiveDeals - Ultimas 5 operaciones: " + lastFiveDealsCsv + ", contratoSica: " + noContratoSica);
			
			String strIdDeal = String.valueOf(deal.getIdDeal());
			if(! InfoNegociacionUtil.isDealInCsv(strIdDeal, lastFiveDealsCsv)) {
				lastFiveDealsCsv = InfoNegociacionUtil.pushIdDealInCsv(strIdDeal, lastFiveDealsCsv);
				LOGGER.info("updateClientLastFiveDeals - Ultimas operacion incluida: " + lastFiveDealsCsv);
			}
			
			Integer idCliente = getInformacionNegociacionDao().getIdClienteByContratoSica(noContratoSica);
			if(idCliente == null) {
				LOGGER.error("updateClientLastFiveDeals - No se encontro idCliente para el contrato: " + noContratoSica);
				return;
			} else {
				boolean updated = getInformacionNegociacionDao().updateClienteLastFiveDeals(idCliente, lastFiveDealsCsv);
				if(updated) {
					LOGGER.info("updateClientLastFiveDeals - Ultimas operaciones actualizadas con exito - " +
						"contratoSica=" + noContratoSica + ", idCliente=" + idCliente + ", lastFiveDeals=" + lastFiveDealsCsv);
				} else {
					LOGGER.error("updateClientLastFiveDeals - Ultimas operaciones NO actualizadas - " +
						"contratoSica=" + noContratoSica + ", idCliente=" + idCliente + ", lastFiveDeals=" + lastFiveDealsCsv);
				}
			}
			LOGGER.info("updateClientLastFiveDeals - FIN.");
		} catch (Exception e) {
			LOGGER.error("updateClientLastFiveDeals - Error al actualizar", e);
		}
    }

    /**
     * @see com.ixe.ods.sica.sdo.WorkFlowServiceData#solicitarModificacionDetalle(String,
            DealDetalle,String,com.ixe.ods.seguridad.model.IUsuario).
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param detOrig El detalle del deal a modificar.
     * @param mnemonicoReemplazo El mnem&oacute;nico a asignar al nuevo detalle.
     * @param usuario El usuario que solicita la modificaci&oacute;n del detalle.
     * @throws SicaException Si el deal ya estaba cancelado o no, si no se puede crear el nuevo
     *              detalle de deal (si es necesario) o si no se pueden terminar las actividades
     * @see #insertarDetalleModificado(String, com.ixe.ods.sica.model.Deal,
            com.ixe.ods.sica.model.DealDetalle, String, com.ixe.ods.sica.model.IPlantilla, TipoOperacionDealDto)
     */
    public void solicitarModificacionDetalle(String ticket, DealDetalle detOrig,
                                             String mnemonicoReemplazo, IUsuario usuario)
            throws SicaException {
        try {
            validarHorarioModificacionesMnemonico();
            debug("*** Solicitando modif Det: " + detOrig.getIdDealPosicion() + " deal:"
                    + detOrig.getDeal().getIdDeal());
            if (DealDetalle.STATUS_DET_CANCELADO.equals(detOrig.getStatusDetalleDeal())) {
                throw new SicaException("El detalle ya hab\u00eda sido cancelado con anterioridad");
            }
            boolean enviadoTes = detOrig.getIdLiquidacionDetalle() != null;
            Deal deal = detOrig.getDeal();
            terminarActividadesParaDeal(deal, Actividad.RES_SOL_MODIF, usuario);
            if (enviadoTes) {
                debug("***** ModifDet: " + detOrig.getIdDealPosicion() + " ya se habia enviado a "
                        + "Tes.");
                int indEvModDet = deal.isInterbancario() ?
                        DealDetalle.EV_IND_INT_MODIFICACION : DealDetalle.EV_IND_MODIFICACION;
                deal.setEvento(Deal.EV_SOLICITUD, Deal.EV_IND_GRAL_MODIFICACION);
                update(deal);
                detOrig.setEvento(Deal.EV_SOLICITUD, indEvModDet);
                update(detOrig);
                wfCrearActividad(Actividad.PROC_MODIF_DETALLE, Actividad.ACT_MOD_DET_ESP_TES, deal,
                        detOrig, mnemonicoReemplazo);
                getSiteService().solicitarCancelacionDetalle(ticket,
                        detOrig.getIdLiquidacionDetalle().intValue(),
                        new Integer(SiteConstants.DET_MARCADO_MODIFICACION));
                debug("***** ModifDet: " + detOrig.getIdDealPosicion() + " se envio sol canc det.");
            }
            else {
                debug("***** ModifDet: " + detOrig.getIdDealPosicion() + " no se habia enviado a "
                        + "Tes. insertando det modificado.");
                TipoOperacionDealDto operacionDeal= new TipoOperacionDealDto();
                operacionDeal.setGoma(true);
                insertarDetalleModificado(ticket, deal, detOrig, mnemonicoReemplazo, null, operacionDeal);
                debug("***** ModifDet: " + detOrig.getIdDealPosicion() +
                        "insert\u00f3 det. modif.");
            }
        }
        catch (ExternalSiteException e) {
            warn(e.getMessage(), e);
            throw new SicaException(e.getMessage());
        }
        catch (SeguridadException e) {
            warn(e.getMessage(), e);
            throw new SicaException(e.getMessage());
        }
        debug("*** Termina Sol. modif Det: " + detOrig.getIdDealPosicion() + " deal:"
                + detOrig.getDeal().getIdDeal());
    }
    
    /**
     * @see com.ixe.ods.sica.sdo.WorkFlowServiceData#solicitarModificacionDetallePorMonto(String, 
     * DealDetalle, double, IUsuario).
     */
    public void solicitarModificacionDetallePorMonto(String ticket, DealDetalle detOrig,
            double nuevoMonto, IUsuario usuario) throws SicaException {
		
		validarHorarioModificacionesMnemonico();
		debug("*** Solicitando modif Det: " + detOrig.getIdDealPosicion() + " deal:" 
				+ detOrig.getDeal().getIdDeal());
		if (DealDetalle.STATUS_DET_CANCELADO.equals(detOrig.getStatusDetalleDeal())) {
			throw new SicaException("El detalle ya hab\u00eda sido cancelado con anterioridad");
		}
		boolean enviadoTes = detOrig.getIdLiquidacionDetalle() != null;
		Deal deal = detOrig.getDeal();
		terminarActividadesParaDeal(deal, Actividad.RES_SOL_MODIF, usuario);
		if (!enviadoTes) {
			debug("***** ModifDet: " + detOrig.getIdDealPosicion() + " no se habia enviado a "
					+ "Tes. insertando det modificado.");
			int indEvModDet = deal.isInterbancario() ?
                    DealDetalle.EV_IND_INT_MODIFICACION : DealDetalle.EV_IND_MODIFICACION;
			deal.setEvento(Deal.EV_SOLICITUD, Deal.EV_IND_GRAL_MODIFICACION);
			update(deal);
			detOrig.setEvento(Deal.EV_SOLICITUD, indEvModDet);
			update(detOrig);
			wfCrearActividad(Actividad.PROC_MODIF_DETALLE, Actividad.ACT_DN_MOD_MONTO, deal, 
					detOrig, "" + nuevoMonto);
			debug("***** ModifDet: " + detOrig.getIdDealPosicion() + " se envio solicitud.");
		}
		else {
			String msg = "***** ModifDet: " + detOrig.getIdDealPosicion() + " ya se habia " +
				"enviado a Tes.";
			debug(msg);
			throw new SicaException("El deal ya hab\u00eda sido enviado a tesorer\u00eda.");
		}
		
		debug("*** Termina Sol. modif Det: " + detOrig.getIdDealPosicion() + " deal:"
				+ detOrig.getDeal().getIdDeal());
	}
    
    /**
     * @see com.ixe.ods.sica.sdo.WorkFlowServiceData#solicitarModificacionDetallePorProducto(String, 
     * DealDetalle, String, double,  double, IUsuario).
     */
    public void solicitarModificacionDetallePorProducto(String ticket, DealDetalle detOrig,
        String nvaFormaLiq, double nvoTipoCambioMesa, double nvoTipoCambioCliente, IUsuario usuario) throws SicaException {
    	
    	validarHorarioModificacionesMnemonico();
		debug("*** Solicitando modif Det: " + detOrig.getIdDealPosicion() + " deal:" 
				+ detOrig.getDeal().getIdDeal());
		if (DealDetalle.STATUS_DET_CANCELADO.equals(detOrig.getStatusDetalleDeal())) {
			throw new SicaException("El detalle ya hab\u00eda sido cancelado con anterioridad");
		}
		boolean enviadoTes = detOrig.getIdLiquidacionDetalle() != null;
		Deal deal = detOrig.getDeal();
		terminarActividadesParaDeal(deal, Actividad.RES_SOL_MODIF, usuario);
		if (!enviadoTes) {
			debug("***** ModifDet: " + detOrig.getIdDealPosicion() + " no se habia enviado a "
					+ "Tes. insertando det modificado.");
			int indEvModDet = deal.isInterbancario() ?
                    DealDetalle.EV_IND_INT_MODIFICACION : DealDetalle.EV_IND_MODIFICACION;
			deal.setEvento(Deal.EV_SOLICITUD, Deal.EV_IND_GRAL_MODIFICACION);
			update(deal);
			detOrig.setEvento(Deal.EV_SOLICITUD, indEvModDet);
			update(detOrig);
			wfCrearActividad(Actividad.PROC_MODIF_DETALLE, Actividad.ACT_DN_MOD_PROD, deal, 
					detOrig, nvaFormaLiq + "|" + nvoTipoCambioMesa + "|" + nvoTipoCambioCliente);
			debug("***** ModifDet: " + detOrig.getIdDealPosicion() + " se envio solicitud.");
		}
		else {
			String msg = "***** ModifDet: " + detOrig.getIdDealPosicion() + " ya se habia " +
				"enviado a Tes.";
			debug(msg);
			throw new SicaException("El deal ya hab\u00eda sido enviado a tesorer\u00eda.");
		}
		
		debug("*** Termina Sol. modif Det: " + detOrig.getIdDealPosicion() + " deal:"
				+ detOrig.getDeal().getIdDeal());
    }

    /**
     *
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param deal El encabezado del deal.
     * @param detOrig El detalle que se cancelar&aacute;.
     * @param mnemonicoReemplazo El mnem&oacute;nico que ser&aacute; asignado al nuevo detalle.
     * @param plantilla La plantilla a asignar.
     * @param operacion TODO
     */
    private void insertarDetalleModificado(String ticket, Deal deal, DealDetalle detOrig,
                                           String mnemonicoReemplazo, IPlantilla plantilla, TipoOperacionDealDto operacion) {
    	
    	String statusAnterior = detOrig.getStatusDetalleDeal();
        detOrig.setStatusDetalleDeal(DealDetalle.STATUS_DET_CANCELADO);
        update(detOrig);
        getLineaCambioServiceData().insertarLiberacionLineaCambioLog(detOrig);
        
        DealDetalle detNvo;
        if (isDivisaReferencia(detOrig)) {
            debug("******* InsDetMod: " + detOrig.getIdDealPosicion() + " creando det de " +
                    detOrig.getDivisa().getIdDivisa() + "...");
            detNvo = insertarMxn(ticket, deal, detOrig.isRecibimos(), mnemonicoReemplazo,
                    detOrig.getMonto());
            debug("******* InsDetMod: " + detOrig.getIdDealPosicion() + " " +
                    detOrig.getDivisa().getIdDivisa() + " creado.");
        }
        else {
            debug("******* InsDetMod: " + detOrig.getIdDealPosicion() +
                    " creando det Divisa...");
            detNvo = insertarDivisa(ticket, deal, detOrig.isRecibimos(),
                    detOrig.getDeal().getTipoValor(), detOrig.getClaveFormaLiquidacion(),
                    detOrig.getDivisa().getIdDivisa(), detOrig.getTipoCambioMesa(),
                    detOrig.getMonto(), detOrig.getTipoCambio(),
                    detOrig.getPrecioReferenciaMidSpot(), detOrig.getPrecioReferenciaSpot(), 
                    detOrig.getFactorDivisa(),
                    detOrig.getIdSpread(), detOrig.getIdPrecioReferencia(), detOrig.getIdFactorDivisa(),
                    mnemonicoReemplazo, false, plantilla, null, operacion);
            debug("******* InsDetMod: " + detOrig.getIdDealPosicion() +
                    " det de Divisa creado.");
            // Debe localizarse el registro de posicion log para el detalle original de manera
            // que ahora apunte al nuevo detalle de deal:
            List plogs = getHibernateTemplate().find("FROM PosicionLog AS pl WHERE "
                    + "pl.dealPosicion.idDealPosicion = ? ORDER BY pl.idPosicionLog DESC",
                    new Integer(detOrig.getIdDealPosicion()));
            if (!plogs.isEmpty()) {
                PosicionLog pl = (PosicionLog) plogs.get(0);
                debug("********* InsDetMod: " + detOrig.getIdDealPosicion() +
                        " modificando posLog " + pl.getIdPosicionLog() + "...");
                pl.setDealPosicion(detNvo);
                update(pl);
                debug("********* InsDetMod: " + detOrig.getIdDealPosicion() + " posLog "
                        + pl.getIdPosicionLog() + " modificado.");
                if (detOrig.isEvento(DealDetalle.EV_IND_GRAL_AFECTADA_POSICION,
                        new String[]{Deal.EV_POSICION})) {
                    debug("*********** InsDetMod: " + detOrig.getIdDealPosicion() +
                            " intercambiando eventos con " + detNvo.getIdDealPosicion() +
                            "...");
                    detOrig.setEvento(Deal.EV_NEGACION,
                            DealDetalle.EV_IND_GRAL_AFECTADA_POSICION);
                    update(detOrig);
                    detNvo.setEvento(Deal.EV_POSICION,
                            DealDetalle.EV_IND_GRAL_AFECTADA_POSICION);
                    update(detNvo);
                    debug("*********** InsDetMod: " + detOrig.getIdDealPosicion() +
                            "eventos con " + detNvo.getIdDealPosicion() + " intercambiados.");
                }
                else {
                    String msg = "El detalle original (" + detOrig.getIdDealPosicion()
                            + ")no hab\u00f3a afectado la posici\u00f3n";
                    debug(msg);
                    throw new SicaException(msg);
                }
            }
            else {
                String msg = "No se encontr\u00f3 el registro de posici\u00f3n log para el "
                        + "detalle de deal " + detOrig.getIdDealPosicion();
                debug(msg);
                throw new SicaException(msg);
            }
        }
        // Se asignan las referencias mutuas:
        detNvo.setSustituyeA(new Integer(detOrig.getIdDealPosicion()));
        update(detNvo);
        detOrig.setReemplazadoPorA(new Integer(detNvo.getIdDealPosicion()));
        update(detOrig);
        // Se actualiza el encabezado del deal con status de alta para que se pueda reprocesar:
        deal.setStatusDeal(Deal.STATUS_DEAL_PROCESO_CAPTURA);
        update(deal);
        
        validarInformacionRegulatoria(deal, detOrig.getDivisa(), detOrig, statusAnterior); // detalle que se va a cancelar
        
        getH2hService().solicitarCancelacionDetalleH2H(detOrig, H2HService.NO_ES_REVERSO);
        
        debug("******* InsDetMod: " + detOrig.getIdDealPosicion() + " deal en alta.");
    }    
    
    /**
     * Autoriza la solicitud por monto para el detalle de deal proporcionado.
     * 
     * @param ticket EL ticket de la sesi&oacute;n del usuario.
     * @param idActividad El n&uacute;mero de la actividad.
     * @param usuario El usuario.
     */
    public void actualizarMontoParaDetalle(String ticket, int idActividad, IUsuario usuario) {
    	Actividad actividad = findActividadWithDeal(idActividad);
    	double nuevoMonto = Double.valueOf(actividad.getResultado()).doubleValue();
        actividad.terminar(usuario, Actividad.RES_AUTORIZADO);
        update(actividad);
        DealDetalle detOrig = actividad.getDealDetalle();
        DealDetalle detNvo = detOrig;
        TipoOperacionDealDto operacionDeal = new TipoOperacionDealDto();
    	operacionDeal.setLapiz(true);
    	operacionDeal.setDealDetalleOriginal(detOrig);
        debug("Actualizando el nuevo monto para el detalle: " + detOrig.getIdDealPosicion());
        Deal deal = actividad.getDeal();
        deal.setEvento(Deal.EV_NORMAL, Deal.EV_IND_GRAL_MODIFICACION);
        deal.setStatusDeal(Deal.STATUS_DEAL_PROCESO_CAPTURA);
        update(deal);
        detOrig.setEvento(Deal.EV_APROBACION, DealDetalle.EV_IND_MODIFICACION);
        marcarDealDetalleCancelado(ticket, detOrig, false , false);
        insertarDivisa(ticket, deal, detNvo.isRecibimos(),
        		detNvo.getDeal().getTipoValor(), detNvo.getClaveFormaLiquidacion(),
        		detNvo.getDivisa().getIdDivisa(), detNvo.getTipoCambioMesa(),
                nuevoMonto, detNvo.getTipoCambio(),
                detNvo.getPrecioReferenciaMidSpot(),detNvo.getPrecioReferenciaSpot(),
                detNvo.getFactorDivisa(),
                    detNvo.getIdSpread(), detNvo.getIdPrecioReferencia(), detNvo.getIdFactorDivisa(),
                    null, true, null, null, operacionDeal);
   }
    
    /**
     * Autoriza la solicitud por producto para el detalle de deal proporcionado.
     * 
     * @param ticket EL ticket de la sesi&oacute;n del usuario.
     * @param idActividad El id de la actividad para encontrar el detalle deal.
     * @param usuario El usuario que autoriza.
     */
    public void actualizarProductoParaDetalle(String ticket, int idActividad, IUsuario usuario)
    	throws SicaException{
    	Actividad actividad = findActividadWithDeal(idActividad);
    	String[] valores = actividad.getResultado().split("\\|");
    	String nuevoProducto = valores[0].trim();
    	double nuevoTcm = Double.parseDouble(valores[1].trim());
    	double nuevoTcc = Double.parseDouble(valores[2].trim());
    	TipoOperacionDealDto tipoOperacionDealDto = new TipoOperacionDealDto();
    	tipoOperacionDealDto.setCambioProducto(true);
        actividad.terminar(usuario, Actividad.RES_AUTORIZADO);
        update(actividad);
        DealDetalle detOrig = actividad.getDealDetalle();
        DealDetalle detNvo = detOrig;
        boolean liberaLC= false;
        debug("Actualizando el nuevo producto para el detalle: " + detOrig.getIdDealPosicion());
        Deal deal = actividad.getDeal();
        deal.setEvento(Deal.EV_NORMAL, Deal.EV_IND_GRAL_MODIFICACION);
        deal.setStatusDeal(Deal.STATUS_DEAL_PROCESO_CAPTURA);
        update(deal);
        detOrig.setEvento(Deal.EV_APROBACION, DealDetalle.EV_IND_MODIFICACION);
        
        DealDetalle ddNuevo =  insertarDivisa(ticket, deal, detNvo.isRecibimos(),
        		detNvo.getDeal().getTipoValor(), nuevoProducto,
        		detNvo.getDivisa().getIdDivisa(), nuevoTcm,
                detNvo.getMonto(), nuevoTcc,
                detNvo.getPrecioReferenciaMidSpot(),detNvo.getPrecioReferenciaSpot(),
                detNvo.getFactorDivisa(),
                    detNvo.getIdSpread(), detNvo.getIdPrecioReferencia(), detNvo.getIdFactorDivisa(),
                    null, true, null, null, tipoOperacionDealDto);
        
        if (ddNuevo != null && ddNuevo.getMnemonico() == null){
        	liberaLC=true;
        }else{
        	getLineaCambioServiceData().insertarLiberacionLineaCambioLog(detOrig);
        }
        
        marcarDealDetalleCancelado(ticket, detOrig, liberaLC, false);
   }
    
    /**
     * Niega la solicitud por monto para el detalle de deal proporcionado.
     * 
     * @param ticket EL ticket de la sesi&oacute;n del usuario.
     * @param idActividad El n&uacute;mero de actividad.
     * @param usuario El usuario que deniega la autorizaci&oacute;n.
     */
    public void negarActualizarMontoOProductoParaDetalle(String ticket, int idActividad, 
    		IUsuario usuario) throws SicaException {
        Actividad actividad = findActividadWithDeal(idActividad);
        actividad.terminar(usuario, Actividad.RES_NO_AUTORIZADO);
        update(actividad);
        Deal deal = actividad.getDeal();
        deal.setEvento(Deal.EV_NORMAL, Deal.EV_IND_GRAL_MODIFICACION);
        deal.setStatusDeal(Deal.STATUS_DEAL_PROCESO_CAPTURA);
        update(deal);
        DealDetalle det = actividad.getDealDetalle();
        det.setEvento(Deal.EV_NEGACION, DealDetalle.EV_IND_MODIFICACION);
        update(det);
    }

    public void hacerSplitDetalle(String ticket, DealDetalle detOrig, String mnemonicoSegundo,
                                  double montoSegundo, IUsuario usuario) throws SicaException {
        try {
        	
        	boolean lineaActiva=false;
        	boolean aplicaLC = getLineaCambioServiceData().validarAplicablesTfPagAnt(ticket, mnemonicoSegundo, detOrig.getClaveFormaLiquidacion(), detOrig.isRecibimos());
        	if (detOrig.getDeal().getCliente() != null){
        		LineaCambio linea =  getLineaCambioServiceData().findLineaCambioParaCliente(detOrig.getDeal().getCliente().getIdPersona());
        		if(linea != null && linea.getStatusLinea().equals(LineaCreditoConstantes.STATUS_ACTIVADA)){
            		lineaActiva = true;
            	}
        	}
        	
        	if(detOrig.getMnemonico()== null && aplicaLC && !lineaActiva){
        		throw new SicaException(LineaCreditoMensajes.OPERACION_NO_CASH);
        	}    	
        	
        	validarHorarioParaModificacionesSplitPesos(detOrig);
            debug("*** Solicitando split Det: " + detOrig.getIdDealPosicion() + " deal:"
                    + detOrig.getDeal().getIdDeal());
            if (DealDetalle.STATUS_DET_CANCELADO.equals(detOrig.getStatusDetalleDeal())) {
                throw new SicaException("El detalle ya hab\u00eda sido cancelado con anterioridad");
            }
            boolean enviadoTes = detOrig.getIdLiquidacionDetalle() != null;
            Deal deal = detOrig.getDeal();
            terminarActividadesParaDeal(deal, Actividad.RES_SOL_MODIF, usuario);
            if (enviadoTes) {
                debug("***** SplitDet: " + detOrig.getIdDealPosicion() + " ya se habia enviado a "
                        + "Tes.");
                int indEvModDet = deal.isInterbancario() ?
                        DealDetalle.EV_IND_INT_MODIFICACION : DealDetalle.EV_IND_MODIFICACION;
                deal.setEvento(Deal.EV_SOLICITUD, Deal.EV_IND_GRAL_MODIFICACION);
                update(deal);
                detOrig.setEvento(Deal.EV_SOLICITUD, indEvModDet);
                update(detOrig);
                wfCrearActividad(Actividad.PROC_MODIF_DETALLE, Actividad.ACT_SPLIT_ESP_TES, deal,
                        detOrig, mnemonicoSegundo + "|" + montoSegundo);
                getSiteService().solicitarCancelacionDetalle(ticket,
                        detOrig.getIdLiquidacionDetalle().intValue(),
                        new Integer(SiteConstants.DET_MARCADO_MODIFICACION));
                debug("***** SplitDet: " + detOrig.getIdDealPosicion() + " se envio sol canc det.");
            }
            else {
                insertarDetallesSplit(ticket, deal, detOrig, mnemonicoSegundo, montoSegundo);
            }
        }
        catch (ExternalSiteException e) {
            warn(e.getMessage(), e);
            throw new SicaException(e.getMessage());
        }
        catch (SeguridadException e) {
            warn(e.getMessage(), e);
            throw new SicaException(e.getMessage());
        }
        debug("*** Termina Split Det: " + detOrig.getIdDealPosicion() + " deal:"
                + detOrig.getDeal().getIdDeal());
    }

    private void insertarDetallesSplit(String ticket, Deal deal, DealDetalle detOrig,
                                      String mnemonico2, double monto2) throws SicaException {
        DealDetalle det1;
        DealDetalle det2;
        TipoOperacionDealDto operacionDeal = new TipoOperacionDealDto();
        operacionDeal.setSplit(true);
        if (isDivisaReferencia(detOrig)) {
            det1 = insertarMxn(ticket, deal, detOrig.isRecibimos(), null,
                    detOrig.getMonto() - monto2);
            det2 = insertarMxn(ticket, deal, detOrig.isRecibimos(), mnemonico2, monto2);
        }
        else {
        	getLineaCambioServiceData().insertarLiberacionLineaCambioLog(detOrig);
            det1 = insertarDivisa(ticket, deal, detOrig.isRecibimos(), deal.getTipoValor(),
                    detOrig.getClaveFormaLiquidacion(), detOrig.getDivisa().getIdDivisa(),
                    detOrig.getTipoCambioMesa(), detOrig.getMonto() - monto2,
                    detOrig.getTipoCambio(), detOrig.getPrecioReferenciaMidSpot(), detOrig.getPrecioReferenciaSpot(),
                    detOrig.getFactorDivisa(),
                    detOrig.getIdSpread(), detOrig.getIdPrecioReferencia(), detOrig.getIdFactorDivisa(),
                    null, true, null, null, operacionDeal);
            det2 = insertarDivisa(ticket, deal, detOrig.isRecibimos(), deal.getTipoValor(),
                    detOrig.getClaveFormaLiquidacion(), detOrig.getDivisa().getIdDivisa(),
                    detOrig.getTipoCambioMesa(), monto2, detOrig.getTipoCambio(),
                    detOrig.getPrecioReferenciaMidSpot(), detOrig.getPrecioReferenciaSpot(),
                    detOrig.getFactorDivisa(),
                    detOrig.getIdSpread(), detOrig.getIdPrecioReferencia(), detOrig.getIdFactorDivisa(),
                    mnemonico2, true, null, null, operacionDeal);
            
        }
        // Se asignan las referencias:
        detOrig.setReemplazadoPorA(new Integer(det1.getIdDealPosicion()));
        detOrig.setReemplazadoPorB(new Integer(det2.getIdDealPosicion()));
        det1.setSustituyeA(new Integer(detOrig.getIdDealPosicion()));
        update(det1);
        det2.setSustituyeA(new Integer(detOrig.getIdDealPosicion()));
        update(det2);
        // Se marca el detalle como cancelado:
        marcarDealDetalleCancelado(ticket, detOrig, false, false);
        if (deal.isPagoAnticipado()) {
            deal.setEvento(Deal.EV_NO_DETERMINADO, Deal.EV_IND_GRAL_PAG_ANT);
        }
        deal.setStatusDeal(Deal.STATUS_DEAL_PROCESO_CAPTURA);
        update(deal);
        if (deal.isInterbancario()) {
            balancearDealInterbancario(deal);
        }
    }

    /**
     * @see WorkFlowServiceData#cambiarFormaLiquidacion(Long, Long, String).
     * @param idLiquidacionDetalle El n&uacute;mero de detalle de liquidaci&oacute;n.
     * @param idDealDetalle El id del detalle de deal.
     * @param mnemonico El mnem&oacute;nico a asignar al detalle de deal.
     * @throws SicaException Si no existe un detalle de deal con el id de detalle de
     *  liquidaci&oacute;n especificado.
     * @throws SeguridadException Si no se puede obtener el ticket por alg&uacute;n error en la
     *  seguridad.
     */
    public void cambiarFormaLiquidacion(Long idLiquidacionDetalle, Long idDealDetalle,
                                        String mnemonico)
            throws SicaException, SeguridadException {
        DealDetalle dealDet;
        if (idDealDetalle == null) {
            dealDet = getDealDetalleParaLiqDet(idLiquidacionDetalle.intValue());
        }
        else {
            dealDet = findDealDetalle(idDealDetalle.intValue());
            if (dealDet.getIdLiquidacionDetalle() == null) {
                dealDet.setIdLiquidacionDetalle(new Integer(idLiquidacionDetalle.intValue()));
                update(dealDet);
            }
        }
        FormasPagoLiqService fplService = (FormasPagoLiqService) _appContext.
                getBean("formasPagoLiqService");
        String ticket = obtenerTicket();
        FormaPagoLiq fpl = fplService.getFormaPagoLiq(ticket, dealDet.getMnemonico());
        FormaPagoLiq fplNvo = fplService.getFormaPagoLiq(ticket, mnemonico);
        if (fpl != null && fplNvo != null) {
        	if ((!fpl.isRecibimos().booleanValue() && !fplNvo.isRecibimos().booleanValue()
                    && (Constantes.TRANSFERENCIA.equals(fpl.getClaveTipoLiquidacion()))
                    && (Constantes.TRANSFERENCIA.equals(fplNvo.getClaveTipoLiquidacion())))
                || (fpl.isRecibimos().booleanValue() && fplNvo.isRecibimos().booleanValue()
                    && (Constantes.CHEQUE.equals(fpl.getClaveTipoLiquidacion()))
                    && (Constantes.LINEA_BANCARIA.equals(fplNvo.getClaveTipoLiquidacion())))) {
        		dealDet.setMnemonico(mnemonico);
                update(dealDet);
        	}
            else {
                warn("No se permite cambiar el Mnemonico del detalle de " +
                        dealDet.getMnemonico() + " a " + fplNvo.getMnemonico() + ".");
            }
        }
        else {
        	warn("El Mnemonico original a cambiar y/o el Mnemonico nuevo es/son null.");
        }
    }

    /**
     * @see WorkFlowServiceData#siteAutorizoONegoCancelacionDetalle(int,boolean,Long).
     * @param idDeal El identificador del deal.
     * @param autorizar true si se autoriza.
     * @param idDetLiq Para identificar el detalle del Deal que se cancela y reportarlo a Banxico.
     * @throws SicaException Si ocurre alg&uacute;n error.
     * @throws SeguridadException Si no se puede obtener el ticket por alg&uacute;n error en la
     *  seguridad.
     */
    public void siteAutorizoONegoCancelacionDetalle(int idDeal, boolean autorizar, Long idDetLiq)
            throws SicaException, SeguridadException {
        debug("* SiteAutONegCanDet deal: " + idDeal + " aut: " + autorizar + " detLiq " + idDetLiq);
        Deal deal = findDeal(idDeal);
        // Revisamos si es cancelaci&oacute;n parcial (modificacion):
        Actividad act = findActividadPendienteActividadDeal(Actividad.ACT_MOD_DET_ESP_TES,
                deal.getIdDeal());
        char tipoMod = ' ';
        if (act != null) {
            tipoMod = 'M'; // Es por modificacion
            debug("*** SiteAutONegCanDet es modif deal: " + idDeal + " detLiq " + idDetLiq);
        }
        else {
            act = findActividadPendienteActividadDeal(Actividad.ACT_SPLIT_ESP_TES,
                    deal.getIdDeal());
            if (act != null) {
                tipoMod = 'S'; // Es por split
                debug("*** SiteAutONegCanDet es split deal: " + idDeal + " detLiq " + idDetLiq);
            }
        }
        if (tipoMod == 'M' || tipoMod == 'S') {
            int indEvModDet = deal.isInterbancario() ?
                    DealDetalle.EV_IND_INT_MODIFICACION : DealDetalle.EV_IND_MODIFICACION;
            DealDetalle detOrig = act.getDealDetalle();
            detOrig.setEvento(autorizar ? Deal.EV_APROBACION : Deal.EV_NEGACION, indEvModDet);
            update(detOrig);
            deal.setEvento(autorizar ?
                    Deal.EV_APROBACION : Deal.EV_NEGACION, Deal.EV_IND_GRAL_MODIFICACION);
            deal.setStatusDeal(Deal.STATUS_DEAL_PROCESO_CAPTURA);
            if (!autorizar) {
                update(deal);
            }
            else {
                String ticket = obtenerTicket();
                if (tipoMod == 'M') {
                    // El Update del encabezado de deal se hace aqui:
                    insertarDetalleModificado(ticket, deal, detOrig, act.getResultado(), null, null);
                }
                else {
                    String mnemonico2 = act.getResultado().split("\\|")[0];
                    double monto2 = Double.valueOf(act.getResultado().split("\\|")[1]).
                            doubleValue();
                    insertarDetallesSplit(ticket, deal, detOrig, mnemonico2, monto2);
                }
            }
            act.terminar(deal.getUsuario(),
                    autorizar ? Actividad.RES_AUTORIZADO : Actividad.RES_NO_AUTORIZADO);
            update(act);
        }
        else {
            debug("*** SiteAutONegCanDet es cancelacion total deal: " + idDeal + " detLiq " +
                    idDetLiq);
            prepararAutNegCancelacion(obtenerTicket(), deal, idDetLiq, autorizar);
        }
        debug("* Termina SiteAutONegCanDet deal: " + idDeal + " detLiq " + idDetLiq);
    }

    private List findActividadesDeRecuperacionDeDetallesNoTerminados(int idDeal) {
        return getHibernateTemplate().find("FROM Actividad as a LEFT JOIN FETCH " +
                "a.deal AS d LEFT JOIN FETCH d.usuario WHERE a.nombreActividad = ? AND " +
                "a.deal.idDeal = ? AND a.status != ?",
                new Object[]{Actividad.ACT_DNCA_RECUP_DET, new Integer(idDeal),
                        Actividad.STATUS_TERMINADO});
    }

    private void prepararAutNegCancelacion(String ticket, Deal deal, Long idDetLiq,
                                           boolean autorizar) throws SicaException {
        debug("***** PrepAutNegCan deal: " + deal.getIdDeal() + " idDetLiq: " + idDetLiq +
            " aut: " + autorizar);
        // Se trata de una solicitud de cancelacion total, esta llegando el mensaje para uno de
        // los detalles:
        List actsRecup = findActividadesDeRecuperacionDeDetallesNoTerminados(deal.getIdDeal());
        debug("***** PrepAutNegCan deal: " + deal.getIdDeal() + " idDetLiq: " + idDetLiq +
                " no terminados: " + actsRecup.size());
        Actividad actRecup = null;
        for (Iterator it = actsRecup.iterator(); it.hasNext();) {
            Actividad act2 = (Actividad) it.next();
            DealDetalle detDeal = act2.getDealDetalle();
            if (idDetLiq != null && detDeal.getIdLiquidacionDetalle() != null &&
                    idDetLiq.intValue() == detDeal.getIdLiquidacionDetalle().intValue()) {
                actRecup = act2;
                break;
            }
        }
        if (actRecup == null) {
            warn("No se encontr\u00f3 el detalle de liquidaci\u00f3n "
                    + idDetLiq + " en los detalles del deal " + deal.getIdDeal());
            GeneralMailSender gms = (GeneralMailSender) _appContext.getBean("generalMailSender");
//            gms.enviarMail("ixecambios@ixe.com.mx", new String[] {"ccovian@ixe.com.mx",
//                    "favilaj@ixe.com.mx", "jlarechiga@ixe.com.mx", "serviciosweb@ixe.com.mx",
//                    "SoporteAplicacionesDesarrolloBco@ixe.com.mx" }, null,
//                    "Error al preparar la autorizacion o negacion de cancelacion",
//                    "idDetLiq: " + idDetLiq + " deal: " + deal.getIdDeal());
            
            /*JDCH 25/05/2015   Se sustituyen los correos hardcodeados por un parametro en SICA*/
            ParametroSica parametroSica;
            parametroSica = findParametro(ParametroSica.EMAIL_ADMINISTRADORES);
            
            String correos = parametroSica.getValor().trim();
            String[] arrayCorreos = correos.split(",");

            gms.enviarMail("ixecambios@ixe.com.mx", arrayCorreos, null,
                    "Error al preparar la autorizacion o negacion de cancelacion",
                    "idDetLiq: " + idDetLiq + " deal: " + deal.getIdDeal());
            /**/
            return;
        }
        if (autorizar) {
            debug("******* PrepAutNegCan deal: " + deal.getIdDeal() + " idDetLiq: " + idDetLiq
                    + " marcando por recuperar act: " + actRecup.getIdActividad() + " del det "
                    + actRecup.getDealDetalle().getIdDealPosicion() + " "
                    + actRecup.getDealDetalle().getFolioDetalle());
            actRecup.setResultado(Actividad.RES_RECUPERAR);
            update(actRecup);
        }
        else {
            debug("******* PrepAutNegCan deal: " + deal.getIdDeal() + " idDetLiq: " + idDetLiq
                    + " no es necesario recuperar " + actRecup.getDealDetalle().getIdDealPosicion()
                    + " " + actRecup.getDealDetalle().getFolioDetalle());
            deal.setEvento(Deal.EV_CANCELACION, Deal.EV_IND_GRAL_CANCELACION);
            update(deal);
            actRecup.terminar(deal.getUsuario(), Actividad.RES_NO_AUTORIZADO);
            update(actRecup);
        }
        if (deal.isEvento(Deal.EV_IND_GRAL_CANCELACION, new String[] {Deal.EV_CANCELACION})) {
            debug("******* PrepAutNegCan deal: " + deal.getIdDeal() + " idDetLiq: " + idDetLiq
                    + " recuperando actividades...");
            // Revisamos si hay actividades no terminadas:
            actsRecup = findActividadesDeRecuperacionDeDetallesNoTerminados(deal.getIdDeal());
            // Se recorren los detalles que esten en status 'Recuperar':
            for (Iterator it = actsRecup.iterator(); it.hasNext();) {
                Actividad act2 = (Actividad) it.next();
                if (Actividad.RES_RECUPERAR.equals(act2.getResultado())) {
                    insertarDetalleModificado(ticket, deal, act2.getDealDetalle(),
                            act2.getDealDetalle().getMnemonico(),
                            act2.getDealDetalle().getPlantilla(), null);
                    act2.terminar(deal.getUsuario(), Actividad.RES_AUTORIZADO);
                    update(actRecup);
                    debug("****** PrepAutNegCan deal: " + deal.getIdDeal() + " idDetLiq: "
                            + idDetLiq + " actividad recuperada.");
                }
            }
        }
        debug("****** PrepAutNegCan deal: " + deal.getIdDeal() + " idDetLiq: " + idDetLiq
                + " actividades recuperadas.");
        // Revisamos si hay actividades no terminadas:
        actsRecup = findActividadesDeRecuperacionDeDetallesNoTerminados(deal.getIdDeal());
        if (actsRecup.isEmpty()) {
            if (deal.isEvento(Deal.EV_IND_GRAL_CANCELACION, new String[]{Deal.EV_CANCELACION})) {
                debug("******* PrepAutNegCan deal: " + deal.getIdDeal() + " idDetLiq: " + idDetLiq
                        + " LA CANCELACION TOTAL FUE DENEGADA.");
                // Se neg&oacute; la cancelaci&oacute;n total del deal por Tesorer&iacute;a,
                // Se pone en alta el deal para ser reprocesado.
                deal.setEvento(Deal.EV_NEGACION_TESORERIA, Deal.EV_IND_GRAL_CANCELACION);
                deal.setStatusDeal(Deal.STATUS_DEAL_PROCESO_CAPTURA);
                update(deal);
            }
            else {
                // Tesorer&iacute;a autoriz&oacute; la cancelaci&oacute;n total, la mandamos a la
                // mesa:
                debug("******* PrepAutNegCan deal: " + deal.getIdDeal() + " idDetLiq: " + idDetLiq
                        + " SOLICITANDO CANCELACION TOTAL A LA MESA.");
                wfCanAutorizadaPorTesoreria(deal.getIdDeal());
            }
        }
        else {
            // Si el todas las actividades est&aacute;n pendientes de recuperar, entonces el SITE
            // autorizo la cancelacion total:
            List activsRecupPorTerminar = new ArrayList();
            for (Iterator it = actsRecup.iterator(); it.hasNext();) {
                Actividad actividad = (Actividad) it.next();
                if (Actividad.RES_RECUPERAR.equals(actividad.getResultado())) {
                    activsRecupPorTerminar.add(actividad);
                }
            }
            if (activsRecupPorTerminar.size() == actsRecup.size()) {
                debug("***** PrepAutNegCan deal: " + deal.getIdDeal() + " idDetLiq: " + idDetLiq
                        + " todas estan pendientes de recuperar.");
                for (Iterator it = activsRecupPorTerminar.iterator(); it.hasNext();) {
                     Actividad act = (Actividad) it.next();
                    act.terminar(deal.getUsuario(), Actividad.RES_AUTORIZADO);
                    update(act);
                }
                wfCanAutorizadaPorTesoreria(deal.getIdDeal());
            }
        }
        debug("***** PrepAutNegCan deal: " + deal.getIdDeal() + " idDetLiq: " + idDetLiq + " FIN");
    }

    /**
     * Libera el uso de las l&iacute;neas de cr&eacute;dito que fueron afectadas por el deal.
     *
     * @param deal El deal con el que se trabajar&aacute;.
     */
    private void liberarLineasOperacion(Deal deal) {
        if (deal.isInterbancario()) {
            LineaOperacion lo = findLineaOperacionByBroker(
                    deal.getBroker().getId().getPersonaMoral().getIdPersona().intValue());
            if (lo != null) {
                usoLiberacionLineaOperacion(lo, LineaOperacionLog.TIPO_OPER_LIBERACION,
                        (DealDetalle) (deal.isCompra() ?
                                deal.getRecibimos().get(0) : deal.getEntregamos().get(0)),
                        deal.getUsuario(), false);
            }
        }
    }

    /**
     * Envia la alerta correspondiente.
     *
     * @param context El contexto de la alerta.
     */
    private void sendAlerta(HashMap context) {
        try {
            SicaAlertasService alertasService = (SicaAlertasService) _appContext.
                    getBean("sicaAlertasService");
            generaAlerta(alertasService, Constantes.SC_CAN,
                    Integer.valueOf((String) context.get("ID_PERSONA")), context);
            generaAlerta(alertasService, Constantes.SC_CAN,
                    Integer.valueOf((String) context.get("PROMOTOR")), context);
        }
        catch (Exception e) {
            debug(e);
        }
    }

    /**
     * Genera la alerta con los parametros definidos.
     *
     * @param alertasService El servicio de alertas.
     * @param tipoAlerta El tipo de alerta.
     * @param idPersona El id de la persona.
     * @param ctx El contexto de la alerta.
     */
    private void generaAlerta(SicaAlertasService alertasService, String tipoAlerta,
                              Integer idPersona, HashMap ctx) {
        try {
            alertasService.generaAlerta(tipoAlerta, idPersona, ctx);
        }
        catch (Exception e) {
            debug(e);
        }
    }

    /**
     * Asigna el valor de la mascara de los eventos.
     *
     * @param vespertina Define si el evento es un evento vespertino.
     * @return String
     */
    private String getMascaraEventos(boolean vespertina) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < Deal.EV_IND_TIPO_CAPTURA; i++) {
            sb.append("_");
        }
        sb.append(vespertina ? Deal.EV_VESPERTINO : Deal.EV_NOCTURNO);
        sb.append("%");
        return sb.toString();
    }

    /**
     * @see WorkFlowServiceData#wfIniciarProcesoDealsPendientes(String).
     * @param ticket El ticket de la sesi&oacute;n.
     * @throws SicaException Si el estado del sistema no es v&aacute;lido.
     */
    public void wfIniciarProcesoDealsPendientes(String ticket) throws SicaException {
        // Se cambia el estado actual del sistema a operacion normal:
        Estado actual = findEstadoSistemaActual();
        actual.setActual(false);
        update(actual);
        actual.getSiguienteEstado().setActual(true);
        actual.getSiguienteEstado().setHoraInicio(HOUR_FORMAT.format(new Date()));
        update(actual.getSiguienteEstado());
        // Se procesan los deals pendientes:
        List deals = getHibernateTemplate().find("FROM Deal AS d WHERE (d.eventosDeal like ? or " +
                "d.eventosDeal like ?) AND d.statusDeal != ? ORDER BY d.idDeal",
                new Object[]{getMascaraEventos(true), getMascaraEventos(false),
                        Deal.STATUS_DEAL_CANCELADO});
        if (logger.isInfoEnabled()) {
            logger.info("Procesando " + deals.size() + " pendientes...");
        }
        PizarronServiceData psd = (PizarronServiceData) _appContext.getBean("jtaPizarronServiceData");
        for (Iterator it = deals.iterator(); it.hasNext();) {
            Deal deal = (Deal) it.next();
            // Debe asignarse la fecha de captura y de liquidacion correctas, para poder afectar la
            // posicion.
            String tpVal = deal.getTipoValor();
            deal.setFechaCaptura(new Date());
            deal.setFechaLiquidacion(
                    tpVal.equals(Constantes.CASH) ? new Date()
                            : tpVal.equals(Constantes.TOM) ? psd.getFechaTOM()
                                : tpVal.equals(Constantes.SPOT) ? psd.getFechaSPOT()
                                    : tpVal.equals(Constantes.HR72) ? psd.getFecha72HR()
                                        : psd.getFechaVFUT());
            update(deal);
            // Se afecta la posici&oacute;n:
            for (Iterator it2 = deal.getDetalles().iterator(); it2.hasNext();) {
                DealDetalle det = (DealDetalle) it2.next();
                if (!DealDetalle.STATUS_DET_CANCELADO.equals(det.getStatusDetalleDeal())) {
                    debug("Detalle " + det.getIdDealPosicion() + " del deal "
                            + det.getDeal().getIdDeal() + " afecta la posici\u00f3n.");
                    afectarPosicion(det);
                }
            }
            deal.setEvento(Deal.EV_NORMAL, Deal.EV_IND_TIPO_CAPTURA);
            update(deal);
            wfIniciarProcesoDeal(ticket, deal.getIdDeal());
        }
    }

    /**
     * Revisa el estado actual del sistema y levanta una excepci&oacute;n si el horario no es de
     * Operaci&oacute;n Normal ni es Operaci&oacute;n Restringida.
     *
     * @throws SicaException si el horario no es de Operaci&oacute;n Normal ni es
     * Operaci&oacute;n Restringida.
     */
    private void validarHorarioParaModificaciones() throws SicaException {
        Estado estadoActual = findEstadoSistemaActual();
        if (!estadoActual.isEstadoNormalRestringido()) {
            throw new SicaException("s\u00f3lo se pueden realizar modificaciones al deal durante "
                    + "el horario de operaci\u00f3n normal y el horario de operaci\u00f3n "
                    + "restringida.");
        }
    }

    /**
     * Revisa el estado actual del sistema y levanta una excepci&oacute;n si el horario no es de
     * Operaci&oacute;n Normal, Restringida ni Vespertina.
     *
     * @throws SicaException Si no se est&aacute; en horario v&aacute;lido.
     */
    private void validarHorarioModificacionesMnemonico() throws SicaException {
    	Estado estadoActual = findEstadoSistemaActual();
    	if (Estado.ESTADO_OPERACION_NORMAL != estadoActual.getIdEstado() &&
                Estado.ESTADO_OPERACION_RESTRINGIDA != estadoActual.getIdEstado() &&
                Estado.ESTADO_OPERACION_VESPERTINA != estadoActual.getIdEstado()) {
    		throw new SicaException("s\u00f3lo se pueden realizar modificaciones a la Forma de "
                    + "Liquidaci\u00f3n del deal en el horario normal, vespertino o "
                    + "restringido.");
    	}
    }

    /**
     * Revisa el estado actual del sistema y levanta una excepci&oacute;n si el horario no es de
     * Operaci&oacute;n Normal, Restringida ni Vespertina y que la divisa no sea Pesos para
     * realizar un Split.
     *
     * @param det El detalle de Deal.
     * @throws SicaException Si no se est&aacute; en horario v&aacute;lido.
     */
    private void validarHorarioParaModificacionesSplitPesos(DealDetalle det) throws SicaException {
        Estado estadoActual = findEstadoSistemaActual();
        if (Estado.ESTADO_OPERACION_NORMAL != estadoActual.getIdEstado() &&
                Estado.ESTADO_OPERACION_RESTRINGIDA != estadoActual.getIdEstado() &&
                Estado.ESTADO_OPERACION_VESPERTINA != estadoActual.getIdEstado() &&
                !det.isPesos()) {
            throw new SicaException("s\u00f3lo se pueden realizar modificaciones al deal durante "
                    + "el horario de operaci\u00f3n normal y el horario de operaci\u00f3n "
                    + "restringida.");
        }
    }

    
//    /**
//     * @see WorkFlowServiceData#marcarPagAntTomaEnFirme(String, com.ixe.ods.sica.model.Deal,
//            com.ixe.ods.seguridad.model.IUsuario, boolean).
//     */
//    public void marcarPagAntTomaEnFirme(String ticket, Deal deal, IUsuario usuario, boolean pagAnt)
//        throws SicaException {
//    se selecciono PA o toma en firme 
//        if ((pagAnt && deal.isPagoAnticipado()) || (!pagAnt && deal.isTomaEnFirme())) {
//            if (pagAnt && deal.isPagoAnticipado()
//                    && deal.isEvento(Deal.EV_IND_GRAL_PAG_ANT, new String[]{Deal.EV_NEGACION})) {
//                deal.setPagoAnticipado(false);
//                throw new SicaException("No se puede aplicar el pago anticipado pues previamente se"
//                        + " ha negado la autorizaci\u00f3n.");
//            }
//            else if (!pagAnt && deal.isTomaEnFirme()
//                    && deal.isEvento(Deal.EV_IND_GRAL_PAG_ANT, new String[]{Deal.EV_NEGACION})) {
//                deal.setTomaEnFirme(false);
//                throw new SicaException("No se puede aplicar la toma en firme pues previamente se"
//                        + " ha negado la autorizaci\u00f3n.");
//            }
//            deal.setEvento(Deal.EV_NO_DETERMINADO, Deal.EV_IND_GRAL_PAG_ANT);
//            getLineaCambioServiceData().revisarAplicacionLineaCredito(ticket, deal);
//        }
//        else {
//    se quito la opcion de PA o TF
//            if (deal.isEvento(Deal.EV_IND_GRAL_PAG_ANT, new String[]{Deal.EV_USO})) {
//                if (pagAnt) {
//                    deal.setPagoAnticipado(true);
//                }
//                else {
//                    deal.setTomaEnFirme(true);
//                }
//                throw new SicaException("El deal tiene un detalle que tiene uso de "
//                                + "l\u00ednea de cambios, no se puede quitar el pago "
//                                + "anticipado, ni la toma en firme.");
//            }
//            // Se apagan los sem&aacute;foros de pago anticipado o toma en firme:
//            deal.setEvento(Deal.EV_NO_DETERMINADO, Deal.EV_IND_GRAL_PAG_ANT);
//        }
//        if (deal.getIdDeal() > 0) {
//            terminarActividadesParaDeal(deal, Actividad.RES_CAMB_CRED, usuario);
//            deal.setStatusDeal(Deal.STATUS_DEAL_PROCESO_CAPTURA);
//            update(deal);
//        }
//    }    
    
    
    /**
     * @see WorkFlowServiceData#marcarPagAntTomaEnFirme(String, com.ixe.ods.sica.model.Deal,
            com.ixe.ods.seguridad.model.IUsuario, boolean).
     */
    public void marcarPagAntTomaEnFirme(String ticket, Deal deal, IUsuario usuario, boolean pagAnt)
        throws SicaException {
    	if ((pagAnt && deal.isTomaEnFirme())
				|| (!pagAnt && deal.isPagoAnticipado())) {
    		deal.setStatusDeal(Deal.STATUS_DEAL_PROCESO_CAPTURA);
    		update(deal);
			return;
		}
    	validarUsoLineaCredito(deal, pagAnt);
    	LineaCambio linea =  getLineaCambioServiceData().findLineaCambioParaCliente(deal.getCliente().getIdPersona());
        getLineaCambioServiceData().consumirLineaCreditoByDeal(ticket, linea, deal);
        if (deal.getIdDeal() > 0) {
            terminarActividadesParaDeal(deal, Actividad.RES_CAMB_CRED, usuario);
            deal.setStatusDeal(Deal.STATUS_DEAL_PROCESO_CAPTURA);
            update(deal);
        }
    }

	private void validarUsoLineaCredito(Deal deal, boolean pagAnt) {
		
		if (deal.getDetalles() == null || deal.getDetalles().size() == 0) {
			deal.setTomaEnFirme(false);
			deal.setPagoAnticipado(false);
			throw new SicaException(
					"No se encontraron formas de pago o cobro aplicables a Pago Anticipado y/o Toma en Firme");
		}
		
		if (deal.isEvento(Deal.EV_IND_GRAL_PAG_ANT, new String[]{Deal.EV_USO})) {
          if (pagAnt && !deal.isPagoAnticipado() ) {
              deal.setPagoAnticipado(true);
              throw new SicaException("El deal tiene un detalle que tiene uso de "
                      + "l\u00ednea de cambios, no se puede quitar el pago "
                      + "anticipado, ni la toma en firme.");
              
          }
          if (!pagAnt && !deal.isTomaEnFirme()) {
              deal.setTomaEnFirme(true);
              throw new SicaException("El deal tiene un detalle que tiene uso de "
                      + "l\u00ednea de cambios, no se puede quitar el pago "
                      + "anticipado, ni la toma en firme.");
          }
    	}
	}

    /**
     * @see WorkFlowServiceData#configurarDatosAdicionalesReverso(int, String).
     */
    public void configurarDatosAdicionalesReverso(int idReverso, String datosAdicionales)
            throws SicaException {
        Reverso rev = (Reverso) find(Reverso.class, new Integer(idReverso));
        rev.setDatosAdicionales(datosAdicionales);
        update(rev);
    }

    /**
     * @see com.ixe.ods.sica.sdo.WorkFlowServiceData#modificarSwap(String,
     *      com.ixe.ods.sica.model.Swap, int, com.ixe.ods.seguridad.model.IUsuario).
     */
    public void modificarSwap(String ticket, Swap swap, int tipoModificacion, IUsuario usuario)
            throws SicaException {
        // Si no se quieren cancelar solo los deals, se marca el swap con status cancelado:
        if (tipoModificacion != SicaServiceData.TIPO_MOD_SWAP_CAN_DEALS) {
            swap.setStatus(Swap.STATUS_CANCELADO);
            update(swap);
        }
        // Si no es cancelacion total, se valida que por lo menos un deal est&eacute; marcado.
        if (tipoModificacion != SicaServiceData.TIPO_MOD_SWAP_CAN_TOT) {
            List dealsSeleccionados = new ArrayList();
            for (Iterator it = swap.getDeals().iterator(); it.hasNext();) {
                Deal deal = (Deal) it.next();
                if (deal.isSeleccionado()) {
                    dealsSeleccionados.add(deal);
                }
            }
            if (dealsSeleccionados.isEmpty()) {
                throw new SicaException("Debe seleccionar por lo menos uno de los deals listados.");
            }
        }
        for (Iterator it = swap.getDeals().iterator(); it.hasNext();) {
            Deal deal = (Deal) it.next();
            switch (tipoModificacion) {
                case SicaServiceData.TIPO_MOD_SWAP_CAN_DEALS:
                    if (deal.isSeleccionado() && deal.isCancelable()) {
                        disminuirDeSwapDeal(deal);
                        wfSolicitarCancelacionDeal(ticket, deal.getIdDeal(), usuario);
                    }
                    break;
                case SicaServiceData.TIPO_MOD_SWAP_CAN_SWAP:
                    if (deal.isSeleccionado() && deal.isCancelable()) {
                        disminuirDeSwapDeal(deal);
                    }
                    else {
                        wfSolicitarCancelacionDeal(ticket, deal.getIdDeal(), usuario);
                        deal.setEvento(Deal.EV_SOLICITUD, Deal.EV_IND_INT_ASIGN_SWAP);
                        update(deal);
                    }
                    break;
                case SicaServiceData.TIPO_MOD_SWAP_CAN_TOT:
                    if (deal.isCancelable()) {
                        disminuirDeSwapDeal(deal);
                        wfSolicitarCancelacionDeal(ticket, deal.getIdDeal(), usuario);
                    }
                    else {
                        deal.setEvento(Deal.EV_SOLICITUD, Deal.EV_IND_INT_ASIGN_SWAP);
                        update(deal);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * @see WorkFlowServiceData#asignarStatusReverso(boolean, Integer).
     */
    public void asignarStatusReverso(boolean reversoAutorizado, Integer idDealOriginal) {
    	Reverso reverso = (Reverso) getHibernateTemplate().find("FROM Reverso AS r WHERE " +
    			"r.dealOriginal.idDeal = ?", idDealOriginal).get(0);
    	if (reversoAutorizado) {
    		reverso.setStatusReverso(Reverso.STATUS_PENDIENTE);
        }
    	else {
    		SicaWorkFlowService ejb = (SicaWorkFlowService)
    			_appContext.getBean("sicaWorkFlowService");
    		ReversoVO reversoVO = new ReversoVO();
    		reversoVO.setIdReverso(reverso.getIdReverso());
    		String mensaje = ejb.negarReverso(obtenerTicket(), reversoVO,
    				reverso.getUsuario().getIdUsuario(), false);
    		reverso.setStatusReverso(Reverso.STATUS_CANCELADO);
    		debug(mensaje);
    	}
        update(reverso);
    }

    /**
     * @see com.ixe.ods.sica.sdo.WorkFlowServiceData#completarAltaAntEmail(String, int, int,
     *              boolean).
     */
    public List completarAltaAntEmail(String ticket, int idAutMedioContacto, int idUsuario,
                                      boolean autorizar) throws SicaException {
        AutMedioContacto amc = (AutMedioContacto) find(AutMedioContacto.class,
                new Integer(idAutMedioContacto));
        amc.setUsuario((IUsuario) find(Usuario.class, new Integer(idUsuario)));
        amc.setFechaTerminacion(new Date());
        amc.setStatus(autorizar ? AutMedioContacto.STATUS_AUTORIZADO :
                AutMedioContacto.STATUS_NO_AUTORIZADO);
        update(amc);
        if (autorizar) {
            // Se genera el medio de contacto en la bup:
            registraMedioDeContacto(amc.getPersona(), amc.getEmail(), amc.getUsuario());
        }
        return new ArrayList();
    }

    /**
     * Regresa el valor del bean 'siteService' del application context.
     *
     * @return SiteService.
     */
    private SiteService getSiteService() {
        return (SiteService) _appContext.getBean("siteService");
    }

    /**
     * Regresa el valor de lineasCambioJtaServiceData.
     *
     * @return LineaCambioServiceData.
     */
    protected LineaCambioServiceData getLineaCambioServiceData() {
        return (LineaCambioServiceData) _appContext.getBean("lineasCambioJtaServiceData");
    }

    /**
     * Regresa el valor de registroMedioContactoPersonaService.
     *
     * @return SicaRegistroMedioContactoPersonaService.
     */
    public SicaRegistroMedioContactoPersonaService getRegistroMedioContactoPersonaService() {
        return registroMedioContactoPersonaService;
    }

    /**
     * Establece el valor de registroMedioContactoPersonaService.
     *
     * @param registroMedioContactoPersonaService El valor a asignar.
     */
    public void setRegistroMedioContactoPersonaService(
            SicaRegistroMedioContactoPersonaService registroMedioContactoPersonaService) {
        this.registroMedioContactoPersonaService = registroMedioContactoPersonaService;
    }

    /**
     * Referencia al EJB de contrataci&oacute;n que permite registrar medios de contacto en la bup.
     */
    private SicaRegistroMedioContactoPersonaService registroMedioContactoPersonaService;
    
    //private H2HService h2hService;

    /**
     * Constante con el formato para fecha y hora.
     */
    public static final SimpleDateFormat DATE_HOUR_FORMAT =
            new SimpleDateFormat("dd/MM/yyyy HH:mm");
    
    public static final Logger LOGGER = Logger.getLogger(HibernateWorkFlowServiceData.class);

	/*public H2HService getH2hService() {
		return h2hService;
	}

	public void setH2hService(H2HService service) {
		h2hService = service;
	}*/
    
}