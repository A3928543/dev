/*
 * $Id: PosicionDelegate.java,v 1.12.30.2.10.1.22.1.12.1 2014/10/29 16:44:40 mejiar Exp $
 * Ixe, Jun 30, 2005
 * Copyright (C) 2001-2004 Grupo Financiero Ixe, S.A.
 */
package com.ixe.ods.sica;

import com.ixe.ods.sica.model.Divisa;
import com.ixe.ods.sica.posicion.vo.MesaVO;
import com.ixe.ods.sica.posicion.vo.ParametrosPizarronVO;
import com.ixe.ods.sica.posicion.vo.PosicionVO;
import com.ixe.ods.sica.posicion.vo.UtilidadGlobalVO;
import com.ixe.ods.sica.sdo.CorteMurexServiceData;
import com.ixe.ods.sica.sdo.PizarronServiceData;
import com.ixe.ods.sica.sdo.PosicionServiceData;
import com.ixe.ods.sica.sdo.SicaServiceData;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author David Solis (dsolis)
 * @version $Revision: 1.12.30.2.10.1.22.1.12.1 $ $Date: 2014/10/29 16:44:40 $
 */
public class PosicionDelegate {

	/**
	 * Constructor de la clase.
	 */
    public PosicionDelegate() {
    }

    /**
     * M&eacute;todo ping; no hace nada.
     *
     */
    public void ping() {
    }

    /**
     * Forma el arbol del menu de la aplicacion de Posicion
     *
     * @return List
     */
    public List getArbol() {
        List mesas = new ArrayList();
        List list = getMesasCambio();
        for (int i = 0; i < list.size(); i++) {
            MesaVO mesa = (MesaVO) list.get(i);
            mesas.add(mesa);
            Integer idMesaCambio = mesa.getIdMesaCambio();
            mesa.getCanales().addAll(getCanales(idMesaCambio));
            mesa.getSucursales().addAll(getSucursales(idMesaCambio));
            mesa.getProductos().addAll(getProductos(idMesaCambio));
        }
        return mesas;
    }

    /**
     * @see PosicionServiceData
     */
    public List getDivisas(Integer idMesaCambio) {
        return getServiceData().findDivisasSpreadActualByMesa(idMesaCambio);
    }

    /**
     * @see PosicionServiceData
     */
    public List getProductos(Integer idMesaCambio) {
        return getServiceData().findProductosSpreadActualByMesa(idMesaCambio);
    }

    /**
     * @see PosicionServiceData
     */
    public List getMesasCambio() {
        return getServiceData().getMesasCambio();
    }

    /**
     * @see PosicionServiceData
     */
    public List getCanales(Integer idMesaCambio) {
        return getServiceData().getCanales(idMesaCambio);
    }

    /**
     * @see PosicionServiceData
     */
    public List getSucursales(Integer idMesaCambio) {
        return getServiceData().getSucursales(idMesaCambio);
    }

    /**
     * @see PosicionServiceData
     */
    public List getBlotter(Integer idMesaCambio, BigDecimal monto) {
    	return getServiceData().getBlotter(idMesaCambio, monto);
    }

    /**
     * @see PosicionServiceData
     */
    public PosicionVO getPosicionMesa(Integer idMesaCambio, String idDivisa) {
    	PosicionVO posicion = getServiceData().getPosicionMesa(idMesaCambio, idDivisa); 
    	Divisa divisa = getSicaServiceData().findDivisa(posicion.getIdDivisa());
    	posicion.setDivideDivisa( divisa.isDivide());
    	return posicion;
    }

    /**
     *
     */
    public List  getCatalogoDivisasFrecuentes() {
    	List divisaList = new ArrayList();
    	divisaList = getSicaServiceData().findDivisasFrecuentes();
    	return divisaList;
    }
    
    /**
    *
    */
    public List  getCatalogoDivisasNoFrecuentes() {
    	List divisaList = new ArrayList();
    	divisaList = getSicaServiceData().findDivisasNoFrecuentes();
    	return divisaList;
    }

    /**
    *
    */
    
    public List  getCatalogoDivisasMetales() {
    	List divisaList = new ArrayList();
    	divisaList = getSicaServiceData().findDivisasMetales();
    	return divisaList;
    }
    
    /**
     * @see PosicionServiceData
     */
    public PosicionVO getPosicionCanal(Integer idMesaCambio, String idDivisa, String idCanal) {
        return getServiceData().getPosicionCanal(idMesaCambio, idDivisa, idCanal);
    }

    /**
     * @see PosicionServiceData
     */
    public PosicionVO getPosicionProducto(Integer idMesaCanal, String idDivisa, String idProducto) {
        return getServiceData().getPosicionProducto(idMesaCanal, idDivisa, idProducto);
    }

    /**
     * @see PosicionServiceData
     */
    public List getPosicionCanales(Integer idMesaCambio, String idDivisa) {
        return getServiceData().getPosicionCanales(idMesaCambio, idDivisa);
    }

    /**
     * @see PosicionServiceData
     */
    public List getPosicionProductos(Integer idMesaCambio, String idDivisa) {
        return getServiceData().getPosicionProductos(idMesaCambio, idDivisa);
    }

    /**
     * @see PosicionServiceData
     */
    public PosicionVO getPosicionSucursales(Integer idMesaCambio, String idDivisa) {
        return getServiceData().getPosicionSucursales(idMesaCambio, idDivisa);
    }

    /**
     * @see PosicionServiceData
     */
    public PosicionVO getPosicionEfectivo(Integer idMesaCambio, String idDivisa) {
        return getServiceData().getPosicionEfectivo(idMesaCambio, idDivisa);
    }

    /**
     * @see PosicionServiceData
     */
    public PosicionVO getQueryPosicionCanales(Integer idMesaCambio, String idDivisa, List canales) {
        return getServiceData().getQueryPosicionCanales(idMesaCambio, idDivisa, canales);
    }

    /**
     * @see PosicionServiceData
     */
    public PosicionVO getQueryPosicionProductos(Integer idMesaCambio, String idDivisa,
                                                List productos) {
        return getServiceData().getQueryPosicionProductos(idMesaCambio, idDivisa, productos);
    }

    /**
     * @see PosicionServiceData
     */
    public PosicionVO getQueryPosicionCanalesProductos(Integer idMesaCambio, String idDivisa,
                                                       List canales, List productos) {
        return getServiceData().getQueryPosicionCanalesProductos(idMesaCambio, idDivisa, canales,
                productos);
    }

    /**
     * @see PosicionServiceData
     */
    public UtilidadGlobalVO getUtilidadGlobal() {
        return getServiceData().getUtilidadGlobal();
    }

    /**
     * Obtiene los datos a desplegar en el pizarron de la aplicacion de posicion
     *
     * @return ParametrosPizarronVO.
     * @throws SicaException Si algo sale mal.
     */
    public ParametrosPizarronVO getParametrosPizarron() throws SicaException {
    	return getPizarronServiceData().getParametrosPizarron();
    }

    /**
     * @see PosicionServiceData
     * @return List
     */
    public List getPosicionDivisas( Integer idMesaCambio ) {
        return getServiceData().getPosicionDivisas(idMesaCambio);
    }

    /**
     * Obtiene el service data ligado al bean <b>posicionServiceData</b>
     * @return PosicionServiceData.
     */
    protected PosicionServiceData getServiceData() {
        return _serviceData;
    }

    /**
     * Obtiene el service data ligado al bean sicaServiceData
     *  @return SicaServiceData.
     */
	protected SicaServiceData getSicaServiceData() {
		return _sicaServiceData;
	}
	
	/**
	 * Estabece el service bean del tipo SicaServiceData
	 * @param serviceData
	 */
	public void setSicaServiceData(SicaServiceData serviceData) {
		_sicaServiceData = serviceData;
	}

    /**
     * Obtiene el service data ligado al bean <b>pizarronServiceData</b>
     *
     * @return PizarronServiceData.
     */
    protected PizarronServiceData getPizarronServiceData() {
        return _pizarronServiceData;
    }

    /**
     * @see PosicionServiceData#getPosicionInventarioEfectivo(Integer, String)
     */
    public PosicionVO getPosicionInventarioEfectivo(Integer idMesaCambio, String idDivisa) {
        return getServiceData().getPosicionInventarioEfectivo(idMesaCambio, idDivisa);
    }

    /**
     * @see PosicionServiceData#getQueryPosicionInventarioEfectivo(Integer, String, java.util.List).
     */
    public PosicionVO getQueryPosicionInventarioEfectivo(Integer idMesaCambio, String idDivisa,
                                                         List sucursales) {
        return getServiceData().getQueryPosicionInventarioEfectivo(idMesaCambio, idDivisa,
                sucursales);
    }

    /**
     * Regresa true si la fecha TOM o SPOT son festivas en Estados Unidos.
     *
     * @see com.ixe.ods.sica.sdo.PizarronServiceData#isValorFuturoHabilitado().
     * @return boolean.
     */
    public boolean getValorFuturo() {
        return getPizarronServiceData().isValorFuturoHabilitado();
    }

    /**
     * Establece el valor de la variable pizarronServiceData.
     *
     * @param pizarronServiceData El valor a asignar.
     */
    public void setPizarronServiceData(PizarronServiceData pizarronServiceData) {
        _pizarronServiceData = pizarronServiceData;
    }

    /**
     * Establece el valor de la variable serviceData.
     *
     * @param serviceData El valor a asignar.
     */
    public void setServiceData(PosicionServiceData serviceData) {
        _serviceData = serviceData;
    }

    public CorteMurexServiceData getCorteMurexServiceData() {
		return _corteMurexServiceData;
	}

	public void setCorteMurexServiceData(
			CorteMurexServiceData _corteMurexServiceData) {
		this._corteMurexServiceData = _corteMurexServiceData;
	}

	/**
     * La referencia al bean pizarronServiceData.
     */
    private PizarronServiceData _pizarronServiceData;

    /**
     * La referencia al bean posicionServiceData.
     */
    private PosicionServiceData _serviceData;
    
    /**
     * La referencia al bean sicaServiceData
     */
    private SicaServiceData _sicaServiceData;
    
    /**
     * La referencia al bean corteMurexServiceData
     */
    private CorteMurexServiceData _corteMurexServiceData;

    
    
	public Date getPizarronFechaTOM() {
		
		return getPizarronServiceData().getFechaTOM();
	}

	public Date getPizarronFechaSPOT() {
		return getPizarronServiceData().getFechaSPOT();
	}

	public Date getPizarronFechaVFUT() {
		
		return getPizarronServiceData().getFechaVFUT();
	}

	public Date getPizarronFecha72HR() {
		return getPizarronServiceData().getFecha72HR();
	}
    
	public List getPosicionLogByFecha(String date){
		return getSicaServiceData().getPosicionesLogByFecha(date);
	}

	public List getPosicionesLogMayoresA(Integer integer) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
