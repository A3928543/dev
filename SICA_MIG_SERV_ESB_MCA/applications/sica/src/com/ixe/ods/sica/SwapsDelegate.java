/*
 * $Id: SwapsDelegate.java,v 1.20.62.1 2018/04/24 16:56:53 mejiar Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2006 - 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.ixe.ods.bup.model.ContratoSica;
import com.ixe.ods.sica.dao.BrokerDao;
import com.ixe.ods.sica.model.Divisa;
import com.ixe.ods.sica.model.Spread;
import com.ixe.ods.sica.sdo.AdminPizarronServiceData;
import com.ixe.ods.sica.sdo.PizarronServiceData;
import com.ixe.ods.sica.sdo.SicaServiceData;
import com.ixe.ods.sica.sdo.SwapsServiceData;
import com.ixe.ods.sica.vo.DetalleSwapVO;
import com.ixe.ods.sica.vo.SwapVO;

/**
 * Delegate para comunicaci&oacute;n con la aplicaci&oacute;n de swaps en Flex.
 *
 * @author Jean C. Favila
 * @version $Revision: 1.20.62.1 $ $Date: 2018/04/24 16:56:53 $
 */
public class SwapsDelegate {
	
	/** The Constant log. */
	private static final Logger log = Logger.getLogger(SwapsDelegate.class);

    /**
     * Constructor vac&iacute;o.
     */
    public SwapsDelegate() {
    }

    /**
     * Regresa un arreglo de 5 fechas: Fecha CASH, Fecha TOM, Fecha SPOT, Fecha 72HR y Fecha VFUT.
     *
     * @return Date[].
     */
    public Date[] getFechasValor() {
        PizarronServiceData pizarronServiceData = getPizarronServiceData();
        Date[] fechasValor = new Date[pizarronServiceData.isValorFuturoHabilitado() ? 5 : 4];
        fechasValor[0] = pizarronServiceData.getFechaOperacion();
        fechasValor[1] = pizarronServiceData.getFechaTOM();
        fechasValor[2] = pizarronServiceData.getFechaSPOT();
        fechasValor[3] = pizarronServiceData.getFecha72HR();
        if (pizarronServiceData.isValorFuturoHabilitado()) {
        	fechasValor[4] = pizarronServiceData.getFechaVFUT();
        }
        return fechasValor;
    }

    /**
     * Regresa la lista de contrapartes que son instituciones financieras.
     *
     * @return List.
     * @see com.ixe.ods.sica.dao.BrokerDao#getBrokersVOParaOperarSwaps().
     */
    public List findContrapartes() {
        return getBrokerDao().getBrokersVOParaOperarSwaps();
    }

    /**
     * Regresa la lista con todas las divisas frecuentes.
     *
     * @return List.
     */
    public List findAllDivisas() {
        return getAdminPizarronServiceData().findAllDivisas();
    }

    /**
     * Obtiene la lista de las formas de liquidaci&oacute;n (productos).
     *
     * @param idMesaCambio El id de la mesa.
     * @param idCanal El id del canal.
     * @param idDivisa El id de la divisa.
     * @return List
     */
    public List findProductos(int idMesaCambio, String idCanal, String idDivisa) {
    	List productos = new ArrayList();
    	try {
            List spreadsActuales = getSicaServiceData().
                    findSpreadsActualesByTipoPizarronDivisa(
                            getSicaServiceData().findCanal(idCanal).getTipoPizarron(), idDivisa);
            for (Iterator it = spreadsActuales.iterator(); it.hasNext();) {
                Spread spread = (Spread) it.next();
                productos.add(spread.getClaveFormaLiquidacion());
            }
    	}
    	catch (Exception e) {
    		e.printStackTrace();
    	}
    	return productos;
    }

    /**
     * Inserta una operaci&oacute;n swap.
     *
     * @param swapVO El swap a insertar.
     * @return Map
     * @throws Exception Si ocurre alg&uacute;n error.
     */
    public Map insertarSwap(SwapVO swapVO) throws Exception {
        // Revisamos si uno de los detalles del swap viene con fecha valor 96HR, para cambiarlo por
        // VFUT:
        for (Iterator it = swapVO.getDetalles().iterator(); it.hasNext();) {
            DetalleSwapVO det = (DetalleSwapVO) it.next();
            log.debug("Detalle SWAP: " + det);
            if (Divisa.DOLAR.equals(swapVO.getIdDivisa()) && 
            		Constantes.DOCUMENTO.equals(det.getClaveFormaLiquidacion()) && 
            		!det.isCompra()) {
            	throw new SicaException("El producto DOCEXT USD no se encuentra disponible.");
            }
            if ("96HR".equals(det.getFechaValor())) {
                det.setFechaValor(Constantes.VFUT);
            }
        }
        return getSwapsServiceData().insertarSwap(swapVO);
    }

    /**
     * Obtiene el arreglo con los sectores que pueden operar Valor Futuro.
     *
     * @return String[].
     */
    public String[] findIdsSectoresUltimaFechaValor() {
        return getSicaServiceData().findIdsSectoresUltimaFechaValor();
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
     * Obtiene el service data ligado al bean <b>adminPizarronServiceData</b>
     *
     * @return AdminPizarronServiceData.
     */
    private AdminPizarronServiceData getAdminPizarronServiceData() {
        return _adminPizarronServiceData;
    }

    /**
     * Regresa el valor de pizarronServiceData.
     *
     * @return PizarronServiceData.
     */
    public PizarronServiceData getPizarronServiceData() {
        return _pizarronServiceData;
    }

    /**
     * Obtiene el service data ligado al bean <b>sicaServiceData</b>
     *
     * @return SicaServiceData.
     */
    private SicaServiceData getSicaServiceData() {
        return _sicaServiceData;
    }

    /**
     * Obtiene el service data ligado al bean <b>swapsServiceData</b>
     *
     * @return SwapsServiceData.
     */
    private SwapsServiceData getSwapsServiceData() {
        return _swapsServiceData;
    }

    /**
     * Establece el valor de adminPizarronServiceData.
     *
     * @param adminPizarronServiceData El valor a asignar.
     */
    public void setAdminPizarronServiceData(AdminPizarronServiceData adminPizarronServiceData) {
        _adminPizarronServiceData = adminPizarronServiceData;
    }

    /**
     * Establece el valor de pizarronServiceData.
     *
     * @param pizarronServiceData El valor a asignar.
     */
    public void setPizarronServiceData(PizarronServiceData pizarronServiceData) {
        _pizarronServiceData = pizarronServiceData;
    }

    /**
     * Establece el valor de sicaServiceData.
     *
     * @param sicaServiceData El valor a asignar.
     */
    public void setSicaServiceData(SicaServiceData sicaServiceData) {
        _sicaServiceData = sicaServiceData;
    }

    /**
     * Establece el valor de swapsServiceData.
     *
     * @param swapsServiceData El valor a asignar.
     */
    public void setSwapsServiceData(SwapsServiceData swapsServiceData) {
        _swapsServiceData = swapsServiceData;
    }

    /**
     * Instancia de JdbcBrokerDao para obtener los brokers que pueden operar swaps.
     */
    private BrokerDao brokerDao;
    
    /**
     * Referencia al bean de adminPizarronServiceData.
     */
    private AdminPizarronServiceData _adminPizarronServiceData;

    /**
     * Referencia al bean pizarronServiceData.
     */
    private PizarronServiceData _pizarronServiceData;

    /**
     * Referencia al bean sicaServiceData.
     */
    private SicaServiceData _sicaServiceData;

    /**
     * Referencia al bean swapsServiceData.
     */
    private SwapsServiceData _swapsServiceData;
}
