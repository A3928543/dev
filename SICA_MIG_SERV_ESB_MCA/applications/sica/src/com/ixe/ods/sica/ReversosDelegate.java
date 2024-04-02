/*
 * $Id: ReversosDelegate.java,v 1.17 2010/04/30 16:25:25 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2007 - 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ixe.ods.seguridad.model.IPerfil;
import com.ixe.ods.seguridad.sdo.SeguridadServiceData;
import com.ixe.ods.sica.dao.BrokerDao;
import com.ixe.ods.sica.dto.ClienteContratoDto;
import com.ixe.ods.sica.model.Deal;
import com.ixe.ods.sica.model.DealDetalle;
import com.ixe.ods.sica.model.Swap;
import com.ixe.ods.sica.sdo.PizarronServiceData;
import com.ixe.ods.sica.sdo.ReversosServiceData;
import com.ixe.ods.sica.services.BusquedaClientesService;
import com.ixe.ods.sica.services.FormasPagoLiqService;
import com.ixe.ods.sica.services.impl.DealServiceImpl;
import com.ixe.ods.sica.utils.DateUtils;
import com.ixe.ods.sica.vo.DealDetalleVO;
import com.ixe.ods.sica.vo.DealVO;

/**
 * Clase que funciona como Business Delegate del lado de Java, para la aplicaci&oacute;n de
 * Reversos.
 *
 * @author Jean C. Favila
 * @version  $Revision: 1.17 $ $Date: 2010/04/30 16:25:25 $
 */
public class ReversosDelegate {

    /**
     * Constructor por default. No hace nada.
     */
    public ReversosDelegate() {
		super();
	}

    /**
     * Encuentra y regresa el deal que tiene el n&uacute;mero especificado como par&aacute;metro.
     *
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param idDeal El n&uacute;mero de deal que se desea encontrar.
     * @param ixeDirecto true si no debe validar que el deal corresponda a la jerarqu&iacute;a del
     *      usuario.
     * @param guardia true para el promotor de guardia.
     * @param facultad Permiso para encontrar el deal (interbancarios o promoci&oacute;n).
     * @param idUsuario El usuario que desea consultar el deal para realizar el reverso.
     * @return DealVO.
     * @throws SicaException Si ocurre alg&uacute;n error al ejecutar la b&uacute;squeda.
     */
    public DealVO findDeal(String ticket, int idDeal, boolean ixeDirecto, boolean guardia,
                           String facultad, int idUsuario) throws SicaException {
		try {
            if (!getServiceData().findReversosExistentesParaDeal(idDeal).isEmpty()) {
                throw new SicaException("El deal ya hab\u00eda participado en un reverso.");
            }
            Deal deal = getServiceData().findDeal(idDeal);
            if (Deal.STATUS_DEAL_TOTALMENTE_LIQUIDADO.equals(deal.getStatusDeal())) {
                throw new SicaException("El deal est\u00e1 totalmente liquidado.");
            }
            if (Math.abs(deal.getBalance()) > 0.0) {
                throw new SicaException("El deal debe estar balanceado.");
            }
            if (FacultySystem.SICA_REVERSOS_MC.equals(facultad)) {
                if (!deal.isInterbancario()) {
                    throw new SicaException("La Mesa de Cambios s\u00f3lo puede hacer " +
                            "reversos de deals Interbancarios");
                }
                if (deal.getSwap() != null) {
                    if (Swap.TIPO_SWAP_ESTRATEGIA.equals(deal.getSwap().getTipoSwap())) {
                        throw new SicaException("No se puede hacer un reverso de un Swap de " +
                                "Derivados.");
                    }
                }
            }
            else {
                if (guardia) {
                    if (deal.isInterbancario() ||
                            (deal.getCanalMesa().getCanal().getSucursal() != null)) {
                        throw new SicaException("Promoci\u00f3n no puede hacer reversos de " +
                                "deals Interbancarios ni del canal del deal especificado.");
                    }
                }
                else {
                    if (ixeDirecto) {
                        if (!FacultySystem.SICA_ID_CANAL_IXE_DIRECTO.equals(deal.getCanalMesa().
                                getCanal().getIdCanal())) {
                            throw new SicaException("El deal especificado no corresponde al " +
                                    "canal de Ixe Directo.");
                        }
                    }
                    else {
                        IPerfil perfil = getSeguridadServiceData().
                                findPerfilByUserAndSystem(idUsuario,
                                        FacultySystem.SICA);
                		if (!getSeguridadServiceData().isCuentaAsignadaEjecutivo(
                                perfil, deal.getContratoSica().getNoCuenta().trim()) ||
                                (deal.getCanalMesa().getCanal().getSucursal() != null) ||
                                deal.isInterbancario() ||
                                FacultySystem.SICA_ID_CANAL_IXE_DIRECTO.equals(
                                        deal.getCanalMesa().getCanal().getIdCanal())) {
                            throw new SicaException("El deal especificado no corresponde a " +
                                    "su Jerarqu\u00eda de Promoci\u00f3n, el deal es " +
                                    "Interbancario y no corresponde a Promoci\u00f3n, o " +
                                    "Promoci\u00f3n no puede hacer reversos del canal del deal " +
                                    "especificado.");
                        }
                    }
                }
            }
            if (Deal.STATUS_DEAL_CANCELADO.equals(deal.getStatusDeal())) {
                throw new SicaException("No se puede reversar el deal " + idDeal +
                        " pues se encuentra totalmente cancelado.");
            }
            if (deal.isCancelable()) {
                throw new SicaException("El deal es cancelable, no debe realizarse el reverso.");
            }
            validarMnemonicosCompletos(deal);
            DealVO dealVO = new DealVO(deal, false);
            dealVO.setUtilidad(Redondeador.redondear2Dec(new DealServiceImpl().
                    getUtilidadPromotor(deal)));
            // Se asignan los datos adicionales:
            List detalles = new ArrayList();
            detalles.addAll(dealVO.getDetallesRecibimos());
            detalles.addAll(dealVO.getDetallesEntregamos());
            for (Iterator it = detalles.iterator(); it.hasNext();) {
                DealDetalleVO detalleVO = (DealDetalleVO) it.next();
                if (detalleVO.getMnemonico() != null) {
                    DealDetalle det = deal.getDetalleConFolio(detalleVO.getFolioDetalle());
                    String mnemonico = detalleVO.getMnemonico();
                    detalleVO.setDatosAdicionales(det.getInfoAdicional(
                            formasPagoLiqService.getFormaPagoLiq(ticket, mnemonico),
                            plantillaPantallaCache.getPlantillaPantalla(mnemonico)));
                }
            }
            return dealVO;
        }
		catch (SicaException e) {
            warn("Error al buscar el deal " + idDeal, e);
			throw e;
		}
        catch (Exception e) {
            warn("Error al buscar el deal " + idDeal, e);
            throw new SicaException("Ocurri\u00f3 un error. Favor de intentar de nuevo.");
        }
    }

    /**
     * Valida que todos los detalles no cancelados del deal tengan un mnem&oacute;nico asignado.
     *
     * @param deal El deal a revisar.
     * @throws SicaException Si hay un detalle no cancelado sin mnem&oacute;nico.
     */
    private void validarMnemonicosCompletos(Deal deal) throws SicaException {
        for (Iterator it = deal.getDetalles().iterator(); it.hasNext();) {
            DealDetalle detalle = (DealDetalle) it.next();
            if (!detalle.isCancelado() && StringUtils.isEmpty(detalle.getMnemonico())) {
                throw new SicaException("Para capturar un reverso es necesario que los detalles " +
                        "que no est\u00e1n cancelado tengan un mnem\u00f3nico asignado.");
            }
        }

    }

    /**
     * Regresa la lista de clientes que cumplen con los atributos de consulta especificados como
     * par&aacute;metros.
     *
     * @param razonSocial Para consultar personas morales (opcional).
     * @param paterno Para consultar personas f&iacute;sicas (opcional).
     * @param materno Para consultar personas f&iacute;sicas (opcional).
     * @param nombre Para consultar personas f&iacute;sicas (opcional).
     * @param noCuenta Para consultar por n&uacute;mero de cuenta (opcional).
     * @param cuentasSubordinados True para buscar en las ramas inferiores de la jerarqu&iacute;a de
     *          promotores.
     * @param idPromotor El id de persona del Promotor que utiliza la aplicaci&oacute;n.
     * @param idUsuario El id de usuario del Promotor que utiliza la aplicaci&oacute;n.
     * @return List.
     * @throws SicaException Si ocurre alg&uacute;n error.
     */
    public List findClientes(String razonSocial, String paterno, String materno, String nombre,
                             String noCuenta, boolean cuentasSubordinados, Integer idPromotor,
                             int idUsuario) throws SicaException {
        try {
            List clientes = busquedaClientesService.findClientes(razonSocial, paterno, materno,
                    nombre, noCuenta, FacultySystem.MODO_DEAL, cuentasSubordinados, idPromotor,
                    idUsuario, false);
            List resultados = new ArrayList();
            for (Iterator it = clientes.iterator(); it.hasNext();) {
                ClienteContratoDto dto = (ClienteContratoDto) it.next();
                Map mapa = new HashMap();
                mapa.put("noCuenta", dto.getNoCuenta());
                mapa.put("razonSocial", dto.getNombreCorto());
                mapa.put("corporativo", "true".equals(dto.getEsGrupo()) ? "S\u00ed" : "No");
                resultados.add(mapa);
            }
            return resultados;
        }
        catch (SicaException e) {
            warn("Error al buscar un cliente",e);
            throw e;
        }
        catch (Exception e) {
            warn("Error al buscar un cliente", e);
            throw new SicaException("Ocurri\u00f3 un error. Favor de intentar de nuevo.");
        }
    }

    /**
     * Regresa una lista de objetos BrokerVO con las contrapartes para deals interbancarios.
     *
     * @return List.
     * @see com.ixe.ods.sica.dao.BrokerDao#getBrokersVOParaOperarSwaps().
     */
    public List findContrapartes() {
        return getBrokerDao().getBrokersVOParaOperarSwaps();
    }

    /**
     * Regresa la fecha de liquidaci&oacute;n para el deal de correcci&oacute;n en un reverso por
     * error en la fecha valor.
     *
     * @param idDeal El n&uacute;mero del deal original a buscar.
     * @param fechaValor La fecha valor que debi&oacute; tener el deal original.
     * @return String.
     */
    public String fechaLiquidacionParaFechaValor(int idDeal, String fechaValor) {
        Deal deal = serviceData.findDeal(idDeal);
        Date fechaLiquidacion = new Date();
        if (Constantes.CASH.equals(fechaValor)) {
            fechaLiquidacion = pizarronServiceData.getFechaOperacion(deal.getFechaCaptura());
        }
        else if (Constantes.TOM.equals(fechaValor.trim())) {
            fechaLiquidacion = pizarronServiceData.getFechaTOM(deal.getFechaCaptura());
        }
        else if (Constantes.SPOT.equals(fechaValor)) {
            fechaLiquidacion = pizarronServiceData.getFechaSPOT(deal.getFechaCaptura());
        }
        else if (Constantes.HR72.equals(fechaValor)) {
            fechaLiquidacion = pizarronServiceData.getFecha72HR(deal.getFechaCaptura());
        }
        else if (Constantes.VFUT.equals(fechaValor)) {
            fechaLiquidacion = pizarronServiceData.getFechaVFUT(deal.getFechaCaptura());
        }
        if (fechaLiquidacion.getTime() < DateUtils.inicioDia().getTime()) {
            return DATE_FORMAT.format(DateUtils.inicioDia());
        }
        else {
            return DATE_FORMAT.format(fechaLiquidacion);
        }
    }

    /**
     * Escribe el registro del reverso en la base de datos.
     *
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param idDeal El n&uacute;mero del deal original.
     * @param noCuenta Si se trata de un reverso por cliente, el n&uacute;mero del contrato sica
     *      correcto.
     * @param fechaValor Si se trata de un reverso por fecha valor, la fecha valor en la que
     *      debi&oacute; capturarse el deal.
     * @param porCancelacion Si se trata o no de un reverso por cancelaci&oacute;n.
     * @param detallesReverso Lista de objetos DetalleReversoVO que describen las alteraciones en
     *      monto o tipo de cambio para los detalles del deal.
     * @param observaciones El motivo del reverso.
     * @param idUsuario El usuario solicitante del reverso.
     * @throws SicaException Si ocurre alg&uacute;n error.
     */
    public void generarReverso(String ticket, int idDeal, String noCuenta, String fechaValor,
                               boolean porCancelacion, List detallesReverso, String observaciones,
                               int idUsuario)
            throws SicaException {
        try {
            getServiceData().generarReverso(ticket, idDeal, porCancelacion, noCuenta,
                    fechaValor, observaciones, idUsuario, detallesReverso);
        }
        catch (SicaException e) {
            warn("Error al generar un reverso para el deal " + idDeal, e);
            throw e;
        }
        catch (Exception e) {
            warn("Error al generar un reverso para el deal " + idDeal, e);
            throw new SicaException("Error al generar el reverso. Consulte a Sistemas por favor.");
        }
    }

    /**
     * Regresa el valor de brokerDao.
     *
     * @return BrokerDao.
     */
    public BrokerDao getBrokerDao() {
        return brokerDao;
    }

    /**
     * Establece el valor de brokerDao.
     *
     * @param brokerDao El valor a asignar.
     */
    public void setBrokerDao(BrokerDao brokerDao) {
        this.brokerDao = brokerDao;
    }

    /**
     * Regresa el valor de serviceData.
     *
     * @return ReversosServiceData.
     */
    public ReversosServiceData getServiceData() {
		return serviceData;
	}

    /**
     * Establece el valor de serviceData.
     *
     * @param serviceData El valor a asignar.
     */
    public void setServiceData(ReversosServiceData serviceData) {
		this.serviceData = serviceData;
	}

    /**
     * Regresa el valor de formasPagoLiqService.
     *
     * @return FormasPagoLiqService.
     */
    public FormasPagoLiqService getFormasPagoLiqService() {
        return formasPagoLiqService;
    }

    /**
     * Establece el valor de formasPagoLiqService.
     *
     * @param formasPagoLiqService El valor a asignar.
     */
    public void setFormasPagoLiqService(FormasPagoLiqService formasPagoLiqService) {
        this.formasPagoLiqService = formasPagoLiqService;
    }

    /**
     * Regresa el valor de pizarronServiceData.
     *
     * @return PizarronServiceData.
     */
    public PizarronServiceData getPizarronServiceData() {
        return pizarronServiceData;
    }

    /**
     * Establece el valor de pizarronServiceData.
     *
     * @param pizarronServiceData El valor a asignar.
     */
    public void setPizarronServiceData(PizarronServiceData pizarronServiceData) {
        this.pizarronServiceData = pizarronServiceData;
    }

    /**
     * Regresa el valor de plantillaPantallaCache.
     *
     * @return PlantillaPantallaCache.
     */
    public PlantillaPantallaCache getPlantillaPantallaCache() {
        return plantillaPantallaCache;
    }

    /**
     * Establece el valor de plantillaPantallaCache.
     *
     * @param plantillaPantallaCache El valor a asignar.
     */
    public void setPlantillaPantallaCache(PlantillaPantallaCache plantillaPantallaCache) {
        this.plantillaPantallaCache = plantillaPantallaCache;
    }

    /**
     * Regresa el valor de busquedaClientesService.
     *
     * @return BusquedaClientesService.
     */
    public BusquedaClientesService getBusquedaClientesService() {
        return busquedaClientesService;
    }

    /**
     * Establece el valor de busquedaClientesService.
     *
     * @param busquedaClientesService El valor a asignar.
     */
    public void setBusquedaClientesService(BusquedaClientesService busquedaClientesService) {
        this.busquedaClientesService = busquedaClientesService;
    }

    /**
     * Regresa el valor de seguridadServiceData.
     *
     * @return SeguridadServiceData.
     */
    public SeguridadServiceData getSeguridadServiceData() {
        return seguridadServiceData;
    }

    /**
     * Establece el valor de seguridadServiceData.
     *
     * @param seguridadServiceData El valor a asignar.
     */
    public void setSeguridadServiceData(SeguridadServiceData seguridadServiceData) {
        this.seguridadServiceData = seguridadServiceData;
    }

    /**
     * Si el logger tiene habilitado la opci&oacute;n de WARN, hace el warn del objeto.
     *
     * @param o El objeto a loggear.
     * @param t La excepci&oacute;n a loggear.
     */
    public void warn(Object o, Throwable t) {
        if (logger.isWarnEnabled()) {
            logger.warn(o, t);
        }
    }

    /**
     * Instancia de JdbcBrokerDao para obtener los brokers que pueden operar swaps.
     */
    private BrokerDao brokerDao;

    /**
     * Instancia de ReversosServiceData para realizar operaciones a la base de datos.
     */
    private ReversosServiceData serviceData;

    /**
     * Bean que contiene las formas de pago y liquidaci&oacute;n.
     */
    private FormasPagoLiqService formasPagoLiqService;

    /**
     * Referencia a PizarronServiceData, para realizar c&aacute;lculos sobre fechas valor.
     */
    private PizarronServiceData pizarronServiceData;

    /**
     * Bean que contiene las reglas de pantallas para capturar los detalles de acuerdo a su
     * mnem&oacute;nico.
     */
    private PlantillaPantallaCache plantillaPantallaCache;

    /**
     * Bean que permite consultar clientes y contratos SICA.
     */
    private BusquedaClientesService busquedaClientesService;

    /**
     * El bean de seguridad con el que se verifican perfiles y la jerarqu&iacute;a.
     */
    private SeguridadServiceData seguridadServiceData;

    /**
     * El objeto para logging.
     */
    protected final transient Log logger = LogFactory.getLog(getClass());

    /**
     * El formateador para las fechas de liquidaci&oacute;n.
     */
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
}