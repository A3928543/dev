/*
 * $Id: AutorizacionesDelegate.java,v 1.29.2.2 2010/06/18 23:10:51 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2006 - 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.remoting.RemoteAccessException;

import com.ixe.ods.seguridad.model.SeguridadException;
import com.ixe.ods.sica.sdo.SicaServiceData;
import com.ixe.ods.sica.vo.DealDetalleVO;
import com.ixe.ods.sica.vo.ReversoVO;

/**
 * La clase con la que se comunica el m&oacute;dulo de autorizaciones en flex, para llamar los
 * servicios del middleware del SICA.
 *
 * @author Jean C. Favila
 * @version $Revision: 1.29.2.2 $ $Date: 2010/06/18 23:10:51 $
 */
public class AutorizacionesDelegate {

    /**
     * Constructor por default, no hace nada.
     */
    public AutorizacionesDelegate() {
        super();
    }

    /**
     * Permite probar la comunicaci&oacute;n desde flex con este delegado.
     */
    public void ping() {
    }

    /**
     * Regresa la fecha actual del servidor.
     *
     * @return Date.
     */
    public Date getServerDate() {
        return new Date();
    }

    /**
     * Regresa la lista de objetos DealWorkitemVO que representan actividades pendientes de
     * completar del WorkFlow.
     *
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param nombreActividad El nombre de la actividad a buscar (puede ser null).
     * @return List
     * @throws SeguridadException Si el ticket no es v&aacute;lido.
     */
    public List getWorkitems(String ticket, String nombreActividad) throws SeguridadException {
        try {
            return getSicaWorkFlowService().getWorkitems(ticket, nombreActividad);
        }
        catch (RemoteAccessException e) {
            throw SicaExceptionHelper.transformar(e);
        }
    }

    /**
     * Regresa una lista de objetos ReversoVO que corresponden a los reversos que se encuentran
     * Pendientes de autorizar.
     *
     * @return List.
     */
    public List findReversosPendientes() {
        try {
            return getSicaWorkFlowService().findReversosPendientes();
        }
        catch (RemoteAccessException e) {
            throw SicaExceptionHelper.transformar(e);
        }
    }

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
    public List completarActividad(String ticket, int idActividad, String tipo, boolean autorizado,
                                   int idUsuario) throws Exception {
        try {
            return getSicaWorkFlowService().completarActividad(ticket, idActividad, tipo,
                    autorizado, idUsuario);
        }
        catch (RemoteAccessException e) {
            throw SicaExceptionHelper.transformar(e);
        }
    }

    /**
     * Modifica el tipo de cambio de la actividad que corresponde a la autorizaci&oacte;n
     * por modificaci&oacte;n de producto.
     * 
     * @param idActividad
     * @param tcm
     */
    public List completarActividadParaModifProd(String ticket, int idActividad, String tipo, 
    		boolean autorizado, int idUsuario, double tcm) throws Exception {
    	getSicaServiceData().modificarTcmEnActividadModProducto(idActividad, tcm);
    	return completarActividad(ticket, idActividad, tipo, autorizado, idUsuario);
    }

    /**
     * Regresa los totales por actividad de las solicitudes de autorizaci&oacute;n pendientes.
     *
     * @param incluirConfDeals Si se debe incluir o no los elementos de Confirmaci&oacute;n de Deal.
     * @return List.
     * @see com.ixe.ods.sica.sdo.WorkFlowServiceData#findActividadesPendientesTotales(boolean).
     */
    public List getTotales(boolean incluirConfDeals) {
        try {
            return getSicaWorkFlowService().getTotales(incluirConfDeals);
        }
        catch (RemoteAccessException e) {
            throw SicaExceptionHelper.transformar(e);
        }
    }

    /**
     * Modifica el tipo de cambio de la mesa para el detalle de deal especificado.
     *
     * @param idDealDetalle El identificador del detalle de deal.
     * @param tcm El tipo de cambio de la mesa que desea asignarse.
     */
    public void modificarTcmParaDealDetalle(int idDealDetalle, double tcm) {
        try {
            getSicaWorkFlowService().modificarTcmParaDealDetalle(idDealDetalle, tcm);
        }
        catch (RemoteAccessException e) {
            throw SicaExceptionHelper.transformar(e);
        }
    }

    /**
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param idReverso El n&uacute;mero de reverso que se desea revisar.
     * @return List.
     */
    public List prepararDealsReverso(String ticket, int idReverso) {
        try {
            return getSicaWorkFlowService().prepararDealsReverso(ticket, idReverso);
        }
        catch (RemoteAccessException e) {
            throw SicaExceptionHelper.transformar(e);
        }
    }

    /**
     * Marca el reverso como autorizado y crea los deals correspondientes.
     *
     * @param ticket El ticket de la sesio&oacute;n del usuario.
     * @param reversoVO El DTO del reverso a autorizar.
     * @param deals La lista de deals que se generar&aacute;n.
     * @param idUsuario El usuario que autoriza el reverso.
     * @return String El mensaje que se presenta al usuario para informarle sobre el resultado.
     */
    public String autorizarReverso(String ticket, ReversoVO reversoVO, List deals, int idUsuario) {
        try {
        	return getSicaWorkFlowService().autorizarReverso(ticket, reversoVO, deals, idUsuario);
        }
        catch (RemoteAccessException e) {
            throw SicaExceptionHelper.transformar(e);
        }
    }

    /**
     * Marca el reverso como no autorizado y desbloquea la liquidaci&uacute;n del deal original.
     *
     * @param ticket El ticket de la sesio&oacute;n del usuario.
     * @param reversoVO El DTO del reverso que no se autoriz&oacute;.
     * @param idUsuario El usuario que autoriza el reverso.
     * @return String El mensaje que se presenta al usuario para informarle sobre el resultado.
     */    
    public String negarReverso(String ticket, ReversoVO reversoVO, int idUsuario) {
        try {
            return getSicaWorkFlowService().negarReverso(ticket, reversoVO, idUsuario, true);
        }
        catch (RemoteAccessException e) {
            throw SicaExceptionHelper.transformar(e);
        }
    }

    /**
     * Llama al servicio configurarDatosAdicionalesReverso() del EJB para definir los tipos de
     * cambio de la mesa que fueron editados por el usuario autorizador del reverso.
     *
     * @param idReverso El n&uacute;mero del reverso.
     * @param datosAdicionales La cadena codificada.
     */
    public void configurarDatosAdicionalesReverso(int idReverso, String datosAdicionales) {
        try {
            getSicaWorkFlowService().configurarDatosAdicionalesReverso(idReverso, datosAdicionales);
        }
        catch (RemoteAccessException e) {
            throw SicaExceptionHelper.transformar(e);
        }
    }

    /**
     * Marca los deals interbancarios como confirmados, grabando la hora, contacto y usuario.
     *
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param arrDealPosicion Arreglo con los n&uacute;meros de detalle de deal a confirmar.
     * @param contacto El contacto con quien se confirm&oacute;.
     * @param idUsuario El usuario del SICA que confirm&oacute; la operaci&oacute;n.
     * @return List.
     */
    public List confirmarDealsInterbancarios(String ticket, Integer[] arrDealPosicion, String contacto,
                                           int idUsuario) {
        try {
			return getSicaWorkFlowService().confirmarDealsInterbancarios(ticket, arrDealPosicion,
                    contacto, idUsuario);
        }
        catch (RemoteAccessException e) {
            throw SicaExceptionHelper.transformar(e);
        }
    }

    /**
     * Autoriza o niega el alta anticipada de correos para facturaci&oacute;n anticipada.
     *
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param idAutMedioContacto El n&uacute;mero de la solicitud.
     * @param idUsuario El identificador del usuario que autoriza o niega.
     * @param autorizar True para autorizar, False para negar.
     * @return List vac&iacute;o.
     */
    public List completarAltaAntEmail(String ticket, int idAutMedioContacto, int idUsuario,
                                      boolean autorizar) {
        try {
            return getSicaWorkFlowService().completarAltaAntEmail(ticket, idAutMedioContacto,
                    idUsuario, autorizar);
        }
        catch (RemoteAccessException e) {
            e.printStackTrace();
            throw SicaExceptionHelper.transformar(e);
        }
    }

    /**
     * Regresa el valor de sicaWorkFlowService.
     *
     * @return SicaWorkFlowService.
     */
    public SicaWorkFlowService getSicaWorkFlowService() {
        return sicaWorkFlowService;
    }

    /**
     * Establece el valor de sicaWorkFlowService.
     *
     * @param sicaWorkFlowService El valor a asignar.
     */
    public void setSicaWorkFlowService(SicaWorkFlowService sicaWorkFlowService) {
        this.sicaWorkFlowService = sicaWorkFlowService;
    }

    /**
     * Regresa la lista de objetos que, tanto la que representa los DealDetalleVO clasificados
     * seg&uacute;n el tipo de operaci&oacute;n, la divisa y la forma de liquidaci&oacute;n.
     * As&iacute; como la lista de los mapas que representan que lista se ha modificado.
     * 
     * @param lista La lista de los detalles que se tienen que clasificar.
     * @return List.
     */
    public List getItemsForAutorizacionMult(ArrayList lista) {
        Map cabeceraCompraVenta = new HashMap();
        Map cabeceraDivisas;
        Map cabeceraFormaLiq;
        Map lables;
        List listaCompraVenta = new ArrayList();
        List listaDetalles;
        List listaPorDivisa;
        List listaPorLiq;
        Set titlesDetalles = new HashSet();
        List returnList = new ArrayList();
        for (Iterator i = lista.iterator(); i.hasNext();) {

            DealDetalleVO dealDetalleVO = (DealDetalleVO) i.next();

            if (!cabeceraCompraVenta.containsKey(dealDetalleVO.isRecibimos() ?
                    "COMPRA" : "VENTA")) {
                listaDetalles = new ArrayList();
                listaDetalles.add(dealDetalleVO);
                cabeceraFormaLiq = new HashMap();
                cabeceraFormaLiq.put(dealDetalleVO.getClaveFormaLiquidacion(), listaDetalles);
                cabeceraDivisas = new HashMap();
                cabeceraDivisas.put(dealDetalleVO.getIdDivisa(), cabeceraFormaLiq);
                cabeceraCompraVenta.put(dealDetalleVO.isRecibimos() ? "COMPRA" : "VENTA",
                        cabeceraDivisas);
            }
            else {
                cabeceraDivisas = (Map) cabeceraCompraVenta.get(dealDetalleVO.isRecibimos() ?
                        "COMPRA" : "VENTA");
                if (!cabeceraDivisas.containsKey(dealDetalleVO.getIdDivisa())) {
                    listaDetalles = new ArrayList();
                    listaDetalles.add(dealDetalleVO);
                    cabeceraFormaLiq = new HashMap();
                    cabeceraFormaLiq.put(dealDetalleVO.getClaveFormaLiquidacion(), listaDetalles);
                    cabeceraDivisas.put(dealDetalleVO.getIdDivisa(), cabeceraFormaLiq);
                }
                else {
                    cabeceraFormaLiq = (Map) cabeceraDivisas.get(dealDetalleVO.getIdDivisa());
                    if (!cabeceraFormaLiq.containsKey(dealDetalleVO.getClaveFormaLiquidacion())) {
                        listaDetalles = new ArrayList();
                        listaDetalles.add(dealDetalleVO);
                        cabeceraFormaLiq.put(dealDetalleVO.getClaveFormaLiquidacion(),
                                listaDetalles);
                    }
                    else {
                        listaDetalles = (List) cabeceraFormaLiq.get(
                                dealDetalleVO.getClaveFormaLiquidacion());
                        listaDetalles.add(dealDetalleVO);
                        cabeceraFormaLiq.put(dealDetalleVO.getClaveFormaLiquidacion(),
                                listaDetalles);
                    }
                    cabeceraDivisas.put(dealDetalleVO.getIdDivisa(), cabeceraFormaLiq);
                }
                cabeceraCompraVenta.put(dealDetalleVO.isRecibimos() ? "COMPRA" : "VENTA",
                        cabeceraDivisas);
            }
            lables = new HashMap();
            lables.put("label", (dealDetalleVO.isRecibimos() ? "COMPRA" : "VENTA") +
                    " " + dealDetalleVO.getIdDivisa() + " " +
                    dealDetalleVO.getClaveFormaLiquidacion());
            lables.put("modificado", Boolean.valueOf(dealDetalleVO.isTipoCambioModificado()));
            titlesDetalles.add(lables);
        }
        for (Iterator iter = cabeceraCompraVenta.values().iterator(); iter.hasNext();) {
            Map divisas = (Map) iter.next();
            listaPorDivisa = new ArrayList();
            for (Iterator iter2 = divisas.values().iterator(); iter2.hasNext();) {
                Map liquidaciones = (Map) iter2.next();
                listaPorLiq = new ArrayList(liquidaciones.values());
                listaPorDivisa.add(listaPorLiq);
            }
            listaCompraVenta.add(listaPorDivisa);
        }

        List temp = new ArrayList(titlesDetalles);
        Collections.sort(temp, new Comparator() {
            public int compare(Object o1, Object o2) {
                Map obj1 = (Map) o1;
                Map obj2 = (Map) o2;
                String lb1 = (String) obj1.get("label");
                String lb2 = (String) obj2.get("label");
                return lb1.compareTo(lb2);
            }
        });
        returnList.add(listaCompraVenta);
        returnList.add(temp);
        return returnList;
    }

    /**
     * Modifica el tipo de cambio de la mesa de la lista enviada y regresa la lista ya modificada
     * 
     * @param detalles La lista de los detalles a modificar.
     * @param nvoTCM El nuevo tipo de cambio de la mesa a asignar.
     * @return List
     */
    public List modTcmParaDetalles(List detalles, double nvoTCM) {
        ArrayList detallesModificados = new ArrayList();

        try {
            for (Iterator iter = detalles.iterator(); iter.hasNext();) {
                DealDetalleVO det = (DealDetalleVO) iter.next();

                getSicaWorkFlowService().modificarTcmParaDealDetalle(det.getIdDealPosicion(),
                        nvoTCM);
                det.setTipoCambioMesa(nvoTCM);
                det.setTipoCambioModificado(true);
                detallesModificados.add(det);
            }
        }
        catch (RemoteAccessException e) {
            throw SicaExceptionHelper.transformar(e);
        }
        return detallesModificados;
    }

    /**
     * Regresa el valor de sicaServiceData
     * 
     * @return SicaServiceData
     */
	public SicaServiceData getSicaServiceData() {
		return sicaServiceData;
	}

	/**
	 * Establece el valor de sicaServiceData.
	 * 
	 * @param sicaServiceData
	 */
	public void setSicaServiceData(SicaServiceData sicaServiceData) {
		this.sicaServiceData = sicaServiceData;
	}

	/**
     * La referencia al WorkFlowServiceData que se encuentra configurada en el application context
     * de spring.
     */
    private SicaWorkFlowService sicaWorkFlowService;
    
    /**
     * La referencia la SicaServiceData que se encuentra configurado en el application contex
     * de spring.
     */
    private SicaServiceData sicaServiceData;
}
