/*
* $Id: HibernatePosicionServiceData.java,v 1.13.28.2.14.4 2011/04/29 00:34:10 galiciad Exp $
* Ixe, Sep 13, 2003
* Copyright (C) 2005 - 2008 Grupo Financiero Ixe
*/

package com.ixe.ods.sica.sdo.impl;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.orm.hibernate.HibernateCallback;
import org.springframework.orm.hibernate.support.HibernateDaoSupport;

import net.sf.hibernate.Criteria;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.expression.Expression;

import com.ixe.ods.sica.model.Canal;
import com.ixe.ods.sica.model.Divisa;
import com.ixe.ods.sica.model.FactorDivisaActual;
import com.ixe.ods.sica.model.HistoricoPosicion;
import com.ixe.ods.sica.model.Posicion;
import com.ixe.ods.sica.posicion.vo.BlotterVO;
import com.ixe.ods.sica.posicion.vo.DivisaVO;
import com.ixe.ods.sica.posicion.vo.MesaVO;
import com.ixe.ods.sica.posicion.vo.PosicionGrupoVO;
import com.ixe.ods.sica.posicion.vo.PosicionVO;
import com.ixe.ods.sica.posicion.vo.ProductoVO;
import com.ixe.ods.sica.posicion.vo.UtilidadDivisaVO;
import com.ixe.ods.sica.posicion.vo.UtilidadGlobalVO;
import com.ixe.ods.sica.sdo.PosicionServiceData;
import com.ixe.ods.sica.utils.DateUtils;
import com.ixe.teller.middleware.ITeller;

/**
 * Clase que implementa los m&eacute;todos para los servicios del monitor de la Posici&oacute;n.
 *
 * @author  Rogelio Chavez, David Solis
 * @version $Revision: 1.13.28.2.14.4 $ $Date: 2011/04/29 00:34:10 $
 */
public class HibernatePosicionServiceData extends HibernateDaoSupport implements PosicionServiceData {

    /**
     * @see PosicionServiceData
     */
    public List findDivisasSpreadActualByMesa(Integer idMesaCambio) {
        List divisas = getHibernateTemplate().find("SELECT new com.ixe.ods.sica.posicion.vo.DivisaVO(d.idDivisa, d.descripcion, d.icono) FROM Divisa d where d.idDivisa in (Select sa.id.divisa.idDivisa From SpreadActual AS sa) ORDER BY d.orden");
        List divisasNF = getHibernateTemplate().findByNamedQuery("findDivisasNoFrecuentesFactor");
        List divisasMT = getHibernateTemplate().findByNamedQuery("findDivisasMetalesFactor");
        for (Iterator it = divisasNF.iterator(); it.hasNext();) {
            FactorDivisaActual fda = (FactorDivisaActual) it.next();
            divisas.add(new DivisaVO(fda.getFacDiv().getToDivisa().getIdDivisa(), fda.getFacDiv().getToDivisa().getDescripcion(), "/sica/images/banderas/world.jpg"));
        }
        for (Iterator it = divisasMT.iterator(); it.hasNext();) {
            FactorDivisaActual fd = (FactorDivisaActual) it.next();
            divisas.add(new DivisaVO(fd.getFacDiv().getToDivisa().getIdDivisa(), fd.getFacDiv().getToDivisa().getDescripcion(), "/sica/images/banderas/metales_amonedados.jpg"));
        }
        return divisas;
    }

    /**
     * @see PosicionServiceData
     */
    public List findProductosSpreadActualByMesa(Integer idMesaCambio) {
        List list = getHibernateTemplate().find("SELECT distinct new com.ixe.ods.sica.posicion.vo.ProductoVO(sa.id.claveFormaLiquidacion, sa.id.claveFormaLiquidacion) FROM SpreadActual AS sa");
        ProductoVO producto = new ProductoVO("INVEFEC", "INVEFEC");
        list.add(producto);
        return list;
    }

    /**
     * @see PosicionServiceData#getBlotter(Integer, java.math.BigDecimal).
     */
    public List getBlotter(Integer idMesaCambio, BigDecimal monto) {
        List plogs = getHibernateTemplate().find("SELECT new com.ixe.ods.sica.posicion.vo.BlotterVO(a.idPosicionLog, a.deal.idDeal, a.tipoValor, a.tipoOperacion, a.monto, a.tipoCambioMesa, a.tipoCambioCliente, c.nombreCorto, a.nombreCliente, a.dealPosicion.idDealPosicion, a.precioReferencia, a.montoMn, a.divisa.idDivisa, a.canalMesa.mesaCambio.nombre) FROM PosicionLog AS a, Usuario AS b, Persona AS c WHERE a.deal.fechaCaptura BETWEEN ? AND ? AND  a.canalMesa.mesaCambio.idMesaCambio = ? AND (a.montoMn >= ? OR a.montoMn <= -(?))AND a.idUsuario = b.idUsuario AND b.idPersona = c.idPersona AND a.tipoOperacion NOT IN ('CT', 'VT') ORDER BY a.deal.idDeal, a.dealPosicion.idDealPosicion, a.idPosicionLog DESC",
                new Object[] { DateUtils.inicioDia(), new Date(), idMesaCambio, monto, monto });
        boolean borrarSiguiente = false;
        int i = 0;
        for (Iterator it = plogs.iterator(); it.hasNext() && i < plogs.size() - 1; i++) {
            BlotterVO blotterVO = (BlotterVO) it.next();
            BlotterVO siguienteVO = (BlotterVO) plogs.get(i + 1);
            if (borrarSiguiente) {
                it.remove();
                i--;
            }
            borrarSiguiente = blotterVO.getIdDealPosicion() == siguienteVO.getIdDealPosicion();
        }
        if (borrarSiguiente && !plogs.isEmpty()) {
            plogs.remove(plogs.size() - 1);
        }
        return plogs;
    }

    /**
     * @see PosicionServiceData
     */
    public PosicionVO getPosicionMesa(Integer idMesaCambio, String idDivisa) {
        List list = getHibernateTemplate().find("SELECT new com.ixe.ods.sica.posicion.vo.PosicionVO(a.cpaVta.compraCash, a.cpaVta.ventaCash, a.cpaVta.compraTom, a.cpaVta.ventaTom, a.cpaVta.compraSpot, a.cpaVta.ventaSpot, a.cpaVta.compra72Hr, a.cpaVta.venta72Hr, a.cpaVta.compraVFut, a.cpaVta.ventaVFut, a.posIni.posicionInicialCash, a.posIni.posicionInicialTom, a.posIni.posicionInicialSpot, a.posIni.posicionInicial72Hr, a.posIni.posicionInicialVFut, a.posIni.posicionInicialMnCash, a.posIni.posicionInicialMnTom, a.posIni.posicionInicialMnSpot, a.posIni.posicionInicialMn72Hr, a.posIni.posicionInicialMnVFut, a.cpaVtaMn.compraMnClienteCash, a.cpaVtaMn.ventaMnClienteCash, a.cpaVtaMn.compraMnClienteTom, a.cpaVtaMn.ventaMnClienteTom, a.cpaVtaMn.compraMnClienteSpot, a.cpaVtaMn.ventaMnClienteSpot, a.cpaVtaMn.compraMnCliente72Hr, a.cpaVtaMn.ventaMnCliente72Hr, a.cpaVtaMn.compraMnClienteVFut, a.cpaVtaMn.ventaMnClienteVFut, a.cpaVtaMn.compraMnPizarronCash, a.cpaVtaMn.ventaMnPizarronCash, a.cpaVtaMn.compraMnPizarronTom, a.cpaVtaMn.ventaMnPizarronTom, a.cpaVtaMn.compraMnPizarronSpot, a.cpaVtaMn.ventaMnPizarronSpot, a.cpaVtaMn.compraMnPizarron72Hr, a.cpaVtaMn.ventaMnPizarron72Hr, a.cpaVtaMn.compraMnPizarronVFut, a.cpaVtaMn.ventaMnPizarronVFut, a.cpaVtaMn.compraMnMesaCash, a.cpaVtaMn.ventaMnMesaCash, a.cpaVtaMn.compraMnMesaTom, a.cpaVtaMn.ventaMnMesaTom, a.cpaVtaMn.compraMnMesaSpot, a.cpaVtaMn.ventaMnMesaSpot, a.cpaVtaMn.compraMnMesa72Hr, a.cpaVtaMn.ventaMnMesa72Hr, a.cpaVtaMn.compraMnMesaVFut, a.cpaVtaMn.ventaMnMesaVFut, a.mesaCambio.idMesaCambio, a.divisa.idDivisa, a.utilidadGlobal, a.utilidadMesa, a.cpaVtaIn.compraInCash, a.cpaVtaIn.ventaInCash, a.cpaVtaIn.compraInTom, a.cpaVtaIn.ventaInTom, a.cpaVtaIn.compraInSpot, a.cpaVtaIn.ventaInSpot, a.cpaVtaIn.compraIn72Hr, a.cpaVtaIn.ventaIn72Hr, a.cpaVtaIn.compraInVFut, a.cpaVtaIn.ventaInVFut) FROM Posicion AS a WHERE a.mesaCambio.idMesaCambio = ? AND a.divisa.idDivisa = ?", new Object[]{idMesaCambio, idDivisa});
        if (!list.isEmpty()) {
            PosicionVO posicion = (PosicionVO) list.get(0);
               posicion.setTipoCambioMercado(getTasaCambioMercado().multiply(new BigDecimal(getFactorDivisaMesa(posicion.getIdDivisa()))));
            return posicion;
        }
        return null;
    }

    /**
     * @see PosicionServiceData
     */
    public BigDecimal getTasaCambioMercado() {
        List cambioMercado = getHibernateTemplate().find("SELECT a.preRef.midSpot FROM PrecioReferenciaActual AS a");
        return new BigDecimal(((Double) cambioMercado.get(0)).doubleValue() + "");
    }

    /**
     * @see PosicionServiceData
     */
    public PosicionVO getPosicionCanal(Integer idMesaCambio, String idDivisa, String idCanal) {
        List list = getHibernateTemplate().find("SELECT new com.ixe.ods.sica.posicion.vo.PosicionVO(a.cpaVta.compraCash, a.cpaVta.ventaCash, a.cpaVta.compraTom, a.cpaVta.ventaTom, a.cpaVta.compraSpot, a.cpaVta.ventaSpot, a.cpaVta.compra72Hr, a.cpaVta.venta72Hr, a.cpaVta.compraVFut, a.cpaVta.ventaVFut, a.posIni.posicionInicialCash, a.posIni.posicionInicialTom, a.posIni.posicionInicialSpot, a.posIni.posicionInicial72Hr, a.posIni.posicionInicialVFut, a.posIni.posicionInicialMnCash, a.posIni.posicionInicialMnTom, a.posIni.posicionInicialMnSpot, a.posIni.posicionInicialMn72Hr, a.posIni.posicionInicialMnVFut, a.cpaVtaMn.compraMnClienteCash, a.cpaVtaMn.ventaMnClienteCash, a.cpaVtaMn.compraMnClienteTom, a.cpaVtaMn.ventaMnClienteTom, a.cpaVtaMn.compraMnClienteSpot, a.cpaVtaMn.ventaMnClienteSpot, a.cpaVtaMn.compraMnCliente72Hr, a.cpaVtaMn.ventaMnCliente72Hr, a.cpaVtaMn.compraMnClienteVFut, a.cpaVtaMn.ventaMnClienteVFut, a.cpaVtaMn.compraMnPizarronCash, a.cpaVtaMn.ventaMnPizarronCash, a.cpaVtaMn.compraMnPizarronTom, a.cpaVtaMn.ventaMnPizarronTom, a.cpaVtaMn.compraMnPizarronSpot, a.cpaVtaMn.ventaMnPizarronSpot, a.cpaVtaMn.compraMnPizarron72Hr, a.cpaVtaMn.ventaMnPizarron72Hr, a.cpaVtaMn.compraMnPizarronVFut, a.cpaVtaMn.ventaMnPizarronVFut, a.cpaVtaMn.compraMnMesaCash, a.cpaVtaMn.ventaMnMesaCash, a.cpaVtaMn.compraMnMesaTom, a.cpaVtaMn.ventaMnMesaTom, a.cpaVtaMn.compraMnMesaSpot, a.cpaVtaMn.ventaMnMesaSpot, a.cpaVtaMn.compraMnMesa72Hr, a.cpaVtaMn.ventaMnMesa72Hr, a.cpaVtaMn.compraMnMesaVFut, a.cpaVtaMn.ventaMnMesaVFut, a.cpaVtaIn.compraInCash, a.cpaVtaIn.ventaInCash, a.cpaVtaIn.compraInTom, a.cpaVtaIn.ventaInTom, a.cpaVtaIn.compraInSpot, a.cpaVtaIn.ventaInSpot, a.cpaVtaIn.compraIn72Hr, a.cpaVtaIn.ventaIn72Hr, a.cpaVtaIn.compraInVFut, a.cpaVtaIn.ventaInVFut) FROM PosicionDetalle AS a WHERE a.mesaCambio.idMesaCambio = ? AND a.divisa.idDivisa = ? AND a.canal.idCanal = ?", new Object[]{idMesaCambio, idDivisa, idCanal});

        if (!list.isEmpty()) {
            PosicionVO posicion = sumatoriaPosicion(list);
            posicion.setTipoCambioMercado(getTasaCambioMercado());
            return posicion;
        }
        return null;
    }

    /**
     * @see PosicionServiceData
     */
    public PosicionVO getPosicionProducto(Integer idMesaCambio, String idDivisa, String idProducto) {
        List list = getHibernateTemplate().find("SELECT new com.ixe.ods.sica.posicion.vo.PosicionVO(a.cpaVta.compraCash, a.cpaVta.ventaCash, a.cpaVta.compraTom, a.cpaVta.ventaTom, a.cpaVta.compraSpot, a.cpaVta.ventaSpot, a.cpaVta.compra72Hr, a.cpaVta.venta72Hr, a.cpaVta.compraVFut, a.cpaVta.ventaVFut, a.posIni.posicionInicialCash, a.posIni.posicionInicialTom, a.posIni.posicionInicialSpot, a.posIni.posicionInicial72Hr, a.posIni.posicionInicialVFut, a.posIni.posicionInicialMnCash, a.posIni.posicionInicialMnTom, a.posIni.posicionInicialMnSpot, a.posIni.posicionInicialMn72Hr, a.posIni.posicionInicialMnVFut, a.cpaVtaMn.compraMnClienteCash, a.cpaVtaMn.ventaMnClienteCash, a.cpaVtaMn.compraMnClienteTom, a.cpaVtaMn.ventaMnClienteTom, a.cpaVtaMn.compraMnClienteSpot, a.cpaVtaMn.ventaMnClienteSpot, a.cpaVtaMn.compraMnCliente72Hr, a.cpaVtaMn.ventaMnCliente72Hr, a.cpaVtaMn.compraMnClienteVFut, a.cpaVtaMn.ventaMnClienteVFut, a.cpaVtaMn.compraMnPizarronCash, a.cpaVtaMn.ventaMnPizarronCash, a.cpaVtaMn.compraMnPizarronTom, a.cpaVtaMn.ventaMnPizarronTom, a.cpaVtaMn.compraMnPizarronSpot, a.cpaVtaMn.ventaMnPizarronSpot, a.cpaVtaMn.compraMnPizarron72Hr, a.cpaVtaMn.ventaMnPizarron72Hr, a.cpaVtaMn.compraMnPizarronVFut, a.cpaVtaMn.ventaMnPizarronVFut, a.cpaVtaMn.compraMnMesaCash, a.cpaVtaMn.ventaMnMesaCash, a.cpaVtaMn.compraMnMesaTom, a.cpaVtaMn.ventaMnMesaTom, a.cpaVtaMn.compraMnMesaSpot, a.cpaVtaMn.ventaMnMesaSpot, a.cpaVtaMn.compraMnMesa72Hr, a.cpaVtaMn.ventaMnMesa72Hr, a.cpaVtaMn.compraMnMesaVFut, a.cpaVtaMn.ventaMnMesaVFut, a.cpaVtaIn.compraInCash, a.cpaVtaIn.ventaInCash, a.cpaVtaIn.compraInTom, a.cpaVtaIn.ventaInTom, a.cpaVtaIn.compraInSpot, a.cpaVtaIn.ventaInSpot, a.cpaVtaIn.compraIn72Hr, a.cpaVtaIn.ventaIn72Hr, a.cpaVtaIn.compraInVFut, a.cpaVtaIn.ventaInVFut) FROM PosicionDetalle AS a WHERE a.mesaCambio.idMesaCambio = ? AND a.divisa.idDivisa = ? AND a.claveFormaLiquidacion = ?", new Object[]{idMesaCambio, idDivisa, idProducto});
        if (!list.isEmpty()) {
            return sumatoriaPosicion(list);
        }
        return null;
    }

    /**
     * @see PosicionServiceData
     */
    public List getPosicionCanales(Integer idMesaCambio, String idDivisa) {
        List list = getHibernateTemplate().find("SELECT new com.ixe.ods.sica.posicion.vo.PosicionGrupoVO(a.canal.nombre, a.cpaVta.compraCash, a.cpaVta.ventaCash, a.cpaVta.compraTom, a.cpaVta.ventaTom, a.cpaVta.compraSpot, a.cpaVta.ventaSpot, a.cpaVta.compra72Hr, a.cpaVta.venta72Hr, a.cpaVta.compraVFut, a.cpaVta.ventaVFut, a.cpaVtaMn.compraMnClienteCash, a.cpaVtaMn.ventaMnClienteCash, a.cpaVtaMn.compraMnClienteTom, a.cpaVtaMn.ventaMnClienteTom, a.cpaVtaMn.compraMnClienteSpot, a.cpaVtaMn.ventaMnClienteSpot, a.cpaVtaMn.compraMnCliente72Hr, a.cpaVtaMn.ventaMnCliente72Hr, a.cpaVtaMn.compraMnClienteVFut, a.cpaVtaMn.ventaMnClienteVFut, a.cpaVtaIn.compraInCash, a.cpaVtaIn.ventaInCash, a.cpaVtaIn.compraInTom, a.cpaVtaIn.ventaInTom, a.cpaVtaIn.compraInSpot, a.cpaVtaIn.ventaInSpot, a.cpaVtaIn.compraIn72Hr, a.cpaVtaIn.ventaIn72Hr, a.cpaVtaIn.compraInVFut, a.cpaVtaIn.ventaInVFut) FROM PosicionDetalle AS a WHERE a.mesaCambio.idMesaCambio = ? AND a.divisa.idDivisa = ?", new Object[]{idMesaCambio, idDivisa});
        if (!list.isEmpty()) {
            return posicionGrupo(list);
        }
        return null;
    }

    /**
     * @see PosicionServiceData
     */
    public List getPosicionProductos(Integer idMesaCambio, String idDivisa) {
        List list = getHibernateTemplate().find("SELECT new com.ixe.ods.sica.posicion.vo.PosicionGrupoVO(a.claveFormaLiquidacion, a.cpaVta.compraCash, a.cpaVta.ventaCash, a.cpaVta.compraTom, a.cpaVta.ventaTom, a.cpaVta.compraSpot, a.cpaVta.ventaSpot, a.cpaVta.compra72Hr, a.cpaVta.venta72Hr, a.cpaVta.compraVFut, a.cpaVta.ventaVFut, a.cpaVtaMn.compraMnClienteCash, a.cpaVtaMn.ventaMnClienteCash, a.cpaVtaMn.compraMnClienteTom, a.cpaVtaMn.ventaMnClienteTom, a.cpaVtaMn.compraMnClienteSpot, a.cpaVtaMn.ventaMnClienteSpot, a.cpaVtaMn.compraMnCliente72Hr, a.cpaVtaMn.ventaMnCliente72Hr, a.cpaVtaMn.compraMnClienteVFut, a.cpaVtaMn.ventaMnClienteVFut, a.cpaVtaIn.compraInCash, a.cpaVtaIn.ventaInCash, a.cpaVtaIn.compraInTom, a.cpaVtaIn.ventaInTom, a.cpaVtaIn.compraInSpot, a.cpaVtaIn.ventaInSpot, a.cpaVtaIn.compraIn72Hr, a.cpaVtaIn.ventaIn72Hr, a.cpaVtaIn.compraInVFut, a.cpaVtaIn.ventaInVFut) FROM PosicionDetalle AS a WHERE a.mesaCambio.idMesaCambio = ? AND a.divisa.idDivisa = ?", new Object[]{idMesaCambio, idDivisa});
        if (!list.isEmpty()) {
            return posicionGrupo(list);
        }
        return null;
    }

    /**
     * @see PosicionServiceData
     */
    public PosicionVO getPosicionSucursales(Integer idMesaCambio, String idDivisa) {
        List list = getHibernateTemplate().find("SELECT new com.ixe.ods.sica.posicion.vo.PosicionVO(a.cpaVta.compraCash, a.cpaVta.ventaCash, a.cpaVta.compraTom, a.cpaVta.ventaTom, a.cpaVta.compraSpot, a.cpaVta.ventaSpot, a.cpaVta.compra72Hr, a.cpaVta.venta72Hr, a.cpaVta.compraVFut, a.cpaVta.ventaVFut, a.posIni.posicionInicialCash, a.posIni.posicionInicialTom, a.posIni.posicionInicialSpot, a.posIni.posicionInicial72Hr, a.posIni.posicionInicialVFut, a.posIni.posicionInicialMnCash, a.posIni.posicionInicialMnTom, a.posIni.posicionInicialMnSpot, a.posIni.posicionInicialMn72Hr, a.posIni.posicionInicialMnVFut, a.cpaVtaMn.compraMnClienteCash, a.cpaVtaMn.ventaMnClienteCash, a.cpaVtaMn.compraMnClienteTom, a.cpaVtaMn.ventaMnClienteTom, a.cpaVtaMn.compraMnClienteSpot, a.cpaVtaMn.ventaMnClienteSpot, a.cpaVtaMn.compraMnCliente72Hr, a.cpaVtaMn.ventaMnCliente72Hr, a.cpaVtaMn.compraMnClienteVFut, a.cpaVtaMn.ventaMnClienteVFut, a.cpaVtaMn.compraMnPizarronCash, a.cpaVtaMn.ventaMnPizarronCash, a.cpaVtaMn.compraMnPizarronTom, a.cpaVtaMn.ventaMnPizarronTom, a.cpaVtaMn.compraMnPizarronSpot, a.cpaVtaMn.ventaMnPizarronSpot, a.cpaVtaMn.compraMnPizarron72Hr, a.cpaVtaMn.ventaMnPizarron72Hr, a.cpaVtaMn.compraMnPizarronVFut, a.cpaVtaMn.ventaMnPizarronVFut, a.cpaVtaMn.compraMnMesaCash, a.cpaVtaMn.ventaMnMesaCash, a.cpaVtaMn.compraMnMesaTom, a.cpaVtaMn.ventaMnMesaTom, a.cpaVtaMn.compraMnMesaSpot, a.cpaVtaMn.ventaMnMesaSpot, a.cpaVtaMn.compraMnMesa72Hr, a.cpaVtaMn.ventaMnMesa72Hr, a.cpaVtaMn.compraMnMesaVFut, a.cpaVtaMn.ventaMnMesaVFut, a.cpaVtaIn.compraInCash, a.cpaVtaIn.ventaInCash, a.cpaVtaIn.compraInTom, a.cpaVtaIn.ventaInTom, a.cpaVtaIn.compraInSpot, a.cpaVtaIn.ventaInSpot, a.cpaVtaIn.compraIn72Hr, a.cpaVtaIn.ventaIn72Hr, a.cpaVtaIn.compraInVFut, a.cpaVtaIn.ventaInVFut) FROM PosicionDetalle AS a WHERE a.mesaCambio.idMesaCambio = ? AND a.divisa.idDivisa = ? AND a.canal.sucursal IS NOT NULL", new Object[]{idMesaCambio, idDivisa});
        if (!list.isEmpty()) {
            PosicionVO posicion = sumatoriaPosicion(list);
            posicion.setTipoCambioMercado(getTasaCambioMercado());
            return posicion;
        }
        return null;
    }

    /**
     * @see PosicionServiceData
     */
    public PosicionVO getPosicionEfectivo(Integer idMesaCambio, String idDivisa) {
        List list = getHibernateTemplate().find("SELECT new com.ixe.ods.sica.posicion.vo.PosicionVO(a.cpaVta.compraCash, a.cpaVta.ventaCash, a.cpaVta.compraTom, a.cpaVta.ventaTom, a.cpaVta.compraSpot, a.cpaVta.ventaSpot, a.cpaVta.compra72Hr, a.cpaVta.venta72Hr, a.cpaVta.compraVFut, a.cpaVta.ventaVFut, a.posIni.posicionInicialCash, a.posIni.posicionInicialTom, a.posIni.posicionInicialSpot, a.posIni.posicionInicial72Hr, a.posIni.posicionInicialVFut, a.posIni.posicionInicialMnCash, a.posIni.posicionInicialMnTom, a.posIni.posicionInicialMnSpot, a.posIni.posicionInicialMn72Hr, a.posIni.posicionInicialMnVFut, a.cpaVtaMn.compraMnClienteCash, a.cpaVtaMn.ventaMnClienteCash, a.cpaVtaMn.compraMnClienteTom, a.cpaVtaMn.ventaMnClienteTom, a.cpaVtaMn.compraMnClienteSpot, a.cpaVtaMn.ventaMnClienteSpot, a.cpaVtaMn.compraMnCliente72Hr, a.cpaVtaMn.ventaMnCliente72Hr, a.cpaVtaMn.compraMnClienteVFut, a.cpaVtaMn.ventaMnClienteVFut, a.cpaVtaMn.compraMnPizarronCash, a.cpaVtaMn.ventaMnPizarronCash, a.cpaVtaMn.compraMnPizarronTom, a.cpaVtaMn.ventaMnPizarronTom, a.cpaVtaMn.compraMnPizarronSpot, a.cpaVtaMn.ventaMnPizarronSpot, a.cpaVtaMn.compraMnPizarron72Hr, a.cpaVtaMn.ventaMnPizarron72Hr, a.cpaVtaMn.compraMnPizarronVFut, a.cpaVtaMn.ventaMnPizarronVFut, a.cpaVtaMn.compraMnMesaCash, a.cpaVtaMn.ventaMnMesaCash, a.cpaVtaMn.compraMnMesaTom, a.cpaVtaMn.ventaMnMesaTom, a.cpaVtaMn.compraMnMesaSpot, a.cpaVtaMn.ventaMnMesaSpot, a.cpaVtaMn.compraMnMesa72Hr, a.cpaVtaMn.ventaMnMesa72Hr, a.cpaVtaMn.compraMnMesaVFut, a.cpaVtaMn.ventaMnMesaVFut, a.cpaVtaIn.compraInCash, a.cpaVtaIn.ventaInCash, a.cpaVtaIn.compraInTom, a.cpaVtaIn.ventaInTom, a.cpaVtaIn.compraInSpot, a.cpaVtaIn.ventaInSpot, a.cpaVtaIn.compraIn72Hr, a.cpaVtaIn.ventaIn72Hr, a.cpaVtaIn.compraInVFut, a.cpaVtaIn.ventaInVFut) FROM PosicionDetalle AS a WHERE a.mesaCambio.idMesaCambio = ? AND a.divisa.idDivisa = ? AND a.claveFormaLiquidacion = 'EFECTI'", new Object[]{idMesaCambio, idDivisa});
        if (!list.isEmpty()) {
            return sumatoriaPosicion(list);
        }
        return null;
    }

    /**
     * @see PosicionServiceData
     */
    public PosicionVO getQueryPosicionCanales(Integer idMesaCambio, String idDivisa, List canales) {
        PosicionVO posicion;
        List list = getHibernateTemplate().findByNamedParam(
                "SELECT new com.ixe.ods.sica.posicion.vo.PosicionVO(a.cpaVta.compraCash, a.cpaVta.ventaCash, a.cpaVta.compraTom, a.cpaVta.ventaTom, a.cpaVta.compraSpot, a.cpaVta.ventaSpot, a.cpaVta.compra72Hr, a.cpaVta.venta72Hr, a.cpaVta.compraVFut, a.cpaVta.ventaVFut, a.cpaVtaMn.compraMnClienteCash, a.cpaVtaMn.ventaMnClienteCash, a.cpaVtaMn.compraMnClienteTom, a.cpaVtaMn.ventaMnClienteTom, a.cpaVtaMn.compraMnClienteSpot, a.cpaVtaMn.ventaMnClienteSpot, a.cpaVtaMn.compraMnCliente72Hr, a.cpaVtaMn.ventaMnCliente72Hr, a.cpaVtaMn.compraMnClienteVFut, a.cpaVtaMn.ventaMnClienteVFut, a.cpaVtaMn.compraMnPizarronCash, a.cpaVtaMn.ventaMnPizarronCash, a.cpaVtaMn.compraMnPizarronTom, a.cpaVtaMn.ventaMnPizarronTom, a.cpaVtaMn.compraMnPizarronSpot, a.cpaVtaMn.ventaMnPizarronSpot, a.cpaVtaMn.compraMnPizarron72Hr, a.cpaVtaMn.ventaMnPizarron72Hr, a.cpaVtaMn.compraMnPizarronVFut, a.cpaVtaMn.ventaMnPizarronVFut, a.cpaVtaMn.compraMnMesaCash, a.cpaVtaMn.ventaMnMesaCash, a.cpaVtaMn.compraMnMesaTom, a.cpaVtaMn.ventaMnMesaTom, a.cpaVtaMn.compraMnMesaSpot, a.cpaVtaMn.ventaMnMesaSpot, a.cpaVtaMn.compraMnMesa72Hr, a.cpaVtaMn.ventaMnMesa72Hr, a.cpaVtaMn.compraMnMesaVFut, a.cpaVtaMn.ventaMnMesaVFut, a.cpaVtaIn.compraInCash, a.cpaVtaIn.ventaInCash, a.cpaVtaIn.compraInTom, a.cpaVtaIn.ventaInTom, a.cpaVtaIn.compraInSpot, a.cpaVtaIn.ventaInSpot, a.cpaVtaIn.compraIn72Hr, a.cpaVtaIn.ventaIn72Hr, a.cpaVtaIn.compraInVFut, a.cpaVtaIn.ventaInVFut) FROM PosicionDetalle AS a WHERE a.mesaCambio.idMesaCambio = :idMesaCambio AND a.divisa.idDivisa = :idDivisa AND a.canal.idCanal IN (:canales)",
                new String[] {"idMesaCambio", "idDivisa", "canales"},
                        new Object[] {idMesaCambio, idDivisa, canales});
        if (!list.isEmpty()) {
            posicion = sumatoriaPosicion(list);
            return posicion;
        }
        return null;
    }

    /**
     * @see PosicionServiceData
     */
    public PosicionVO getQueryPosicionProductos(Integer idMesaCambio, String idDivisa, List productos) {
        PosicionVO posicion;
        List list = getHibernateTemplate().findByNamedParam(
                "SELECT new com.ixe.ods.sica.posicion.vo.PosicionVO(a.cpaVta.compraCash, a.cpaVta.ventaCash, a.cpaVta.compraTom, a.cpaVta.ventaTom, a.cpaVta.compraSpot, a.cpaVta.ventaSpot, a.cpaVta.compra72Hr, a.cpaVta.venta72Hr, a.cpaVta.compraVFut, a.cpaVta.ventaVFut, a.cpaVtaMn.compraMnClienteCash, a.cpaVtaMn.ventaMnClienteCash, a.cpaVtaMn.compraMnClienteTom, a.cpaVtaMn.ventaMnClienteTom, a.cpaVtaMn.compraMnClienteSpot, a.cpaVtaMn.ventaMnClienteSpot, a.cpaVtaMn.compraMnCliente72Hr, a.cpaVtaMn.ventaMnCliente72Hr, a.cpaVtaMn.compraMnClienteVFut, a.cpaVtaMn.ventaMnClienteVFut, a.cpaVtaMn.compraMnPizarronCash, a.cpaVtaMn.ventaMnPizarronCash, a.cpaVtaMn.compraMnPizarronTom, a.cpaVtaMn.ventaMnPizarronTom, a.cpaVtaMn.compraMnPizarronSpot, a.cpaVtaMn.ventaMnPizarronSpot, a.cpaVtaMn.compraMnPizarron72Hr, a.cpaVtaMn.ventaMnPizarron72Hr, a.cpaVtaMn.compraMnPizarronVFut, a.cpaVtaMn.ventaMnPizarronVFut, a.cpaVtaMn.compraMnMesaCash, a.cpaVtaMn.ventaMnMesaCash, a.cpaVtaMn.compraMnMesaTom, a.cpaVtaMn.ventaMnMesaTom, a.cpaVtaMn.compraMnMesaSpot, a.cpaVtaMn.ventaMnMesaSpot, a.cpaVtaMn.compraMnMesa72Hr, a.cpaVtaMn.ventaMnMesa72Hr, a.cpaVtaMn.compraMnMesaVFut, a.cpaVtaMn.ventaMnMesaVFut, a.cpaVtaIn.compraInCash, a.cpaVtaIn.ventaInCash, a.cpaVtaIn.compraInTom, a.cpaVtaIn.ventaInTom, a.cpaVtaIn.compraInSpot, a.cpaVtaIn.ventaInSpot, a.cpaVtaIn.compraIn72Hr, a.cpaVtaIn.ventaIn72Hr, a.cpaVtaIn.compraInVFut, a.cpaVtaIn.ventaInVFut) FROM PosicionDetalle AS a WHERE a.mesaCambio.idMesaCambio = :idMesaCambio AND a.divisa.idDivisa = :idDivisa AND a.claveFormaLiquidacion IN (:productos)",
                new String[] {"idMesaCambio", "idDivisa", "productos"},
                new Object[] {idMesaCambio, idDivisa, productos});
        if (!list.isEmpty()) {
            posicion = sumatoriaPosicion(list);
            return posicion;
        }
        return null;
    }

    /**
     * @see PosicionServiceData
     */
    public PosicionVO getQueryPosicionCanalesProductos(Integer idMesaCambio, String idDivisa, List canales, List productos) {
        PosicionVO posicion;
        List list = getHibernateTemplate().findByNamedParam("SELECT new com.ixe.ods.sica.posicion.vo.PosicionVO(a.cpaVta.compraCash, a.cpaVta.ventaCash, a.cpaVta.compraTom, a.cpaVta.ventaTom, a.cpaVta.compraSpot, a.cpaVta.ventaSpot, a.cpaVta.compra72Hr, a.cpaVta.venta72Hr, a.cpaVta.compraVFut, a.cpaVta.ventaVFut, a.cpaVtaMn.compraMnClienteCash, a.cpaVtaMn.ventaMnClienteCash, a.cpaVtaMn.compraMnClienteTom, a.cpaVtaMn.ventaMnClienteTom, a.cpaVtaMn.compraMnClienteSpot, a.cpaVtaMn.ventaMnClienteSpot, a.cpaVtaMn.compraMnCliente72Hr, a.cpaVtaMn.ventaMnCliente72Hr, a.cpaVtaMn.compraMnClienteVFut, a.cpaVtaMn.ventaMnClienteVFut, a.cpaVtaMn.compraMnPizarronCash, a.cpaVtaMn.ventaMnPizarronCash, a.cpaVtaMn.compraMnPizarronTom, a.cpaVtaMn.ventaMnPizarronTom, a.cpaVtaMn.compraMnPizarronSpot, a.cpaVtaMn.ventaMnPizarronSpot, a.cpaVtaMn.compraMnPizarron72Hr, a.cpaVtaMn.ventaMnPizarron72Hr, a.cpaVtaMn.compraMnPizarronVFut, a.cpaVtaMn.ventaMnPizarronVFut, a.cpaVtaMn.compraMnMesaCash, a.cpaVtaMn.ventaMnMesaCash, a.cpaVtaMn.compraMnMesaTom, a.cpaVtaMn.ventaMnMesaTom, a.cpaVtaMn.compraMnMesaSpot, a.cpaVtaMn.ventaMnMesaSpot, a.cpaVtaMn.compraMnMesa72Hr, a.cpaVtaMn.ventaMnMesa72Hr, a.cpaVtaMn.compraMnMesaVFut, a.cpaVtaMn.ventaMnMesaVFut, a.cpaVtaIn.compraInCash, a.cpaVtaIn.ventaInCash, a.cpaVtaIn.compraInTom, a.cpaVtaIn.ventaInTom, a.cpaVtaIn.compraInSpot, a.cpaVtaIn.ventaInSpot, a.cpaVtaIn.compraIn72Hr, a.cpaVtaIn.ventaIn72Hr, a.cpaVtaIn.compraInVFut, a.cpaVtaIn.ventaInVFut) FROM PosicionDetalle AS a WHERE a.mesaCambio.idMesaCambio = :idMesaCambio AND a.divisa.idDivisa = :idDivisa AND a.canal.idCanal IN (:canales) AND a.claveFormaLiquidacion IN (:productos)",
                new String[] {"idMesaCambio", "idDivisa", "canales", "productos"},
                new Object[] {idMesaCambio, idDivisa, canales, productos});
        if (!list.isEmpty()) {
            posicion = sumatoriaPosicion(list);
            return posicion;
        }
        return null;
    }

    /**
     * @see PosicionServiceData
     */
    public List getMesasCambio() {
        return getHibernateTemplate().find("SELECT new com.ixe.ods.sica.posicion.vo.MesaVO(a.idMesaCambio, a.nombre, a.divisaReferencia.idDivisa) FROM MesaCambio AS a ORDER BY a.idMesaCambio");
    }

    /**
     * @see PosicionServiceData
     */
    public List getCanales(Integer idMesaCambio) {
        return getHibernateTemplate().find("SELECT new com.ixe.ods.sica.posicion.vo.CanalVO(a.idCanal, a.nombre, a.tipoPizarron.idTipoPizarron) FROM Canal AS a WHERE a.sucursal IS NULL AND a.idCanal != 'SUC' AND a.mesaCambio.idMesaCambio = ?", new Object[]{idMesaCambio});
    }

    /**
     * @see PosicionServiceData
     */
    public List getSucursales(Integer idMesaCambio) {
        return getHibernateTemplate().find("SELECT new com.ixe.ods.sica.posicion.vo.CanalVO(a.idCanal, a.nombre, a.tipoPizarron.idTipoPizarron) FROM Canal AS a WHERE a.sucursal IS NOT NULL AND a.mesaCambio.idMesaCambio = ?", new Object[]{idMesaCambio});
    }

    /**
     * @see PosicionServiceData
     */
    public UtilidadGlobalVO getUtilidadGlobal() {
        List mesasCambio = getMesasCambio();
        double tasaCambioMercado = getTasaCambioMercado().doubleValue();
        double utilidadGlobal = 0;
        String idDivisa;
        UtilidadGlobalVO utilidadGlobalVO = new UtilidadGlobalVO();
        for (int cont = 0; cont < mesasCambio.size(); cont++) {
            double utilidadMesa = 0;
            MesaVO mesa = (MesaVO) mesasCambio.get(cont);
            idDivisa = mesa.getIdDivisa();
            List posiciones = getHibernateTemplate().find("SELECT new com.ixe.ods.sica.posicion.vo.PosicionVO(a.cpaVta.compraCash, a.cpaVta.ventaCash, a.cpaVta.compraTom, a.cpaVta.ventaTom, a.cpaVta.compraSpot, a.cpaVta.ventaSpot, a.cpaVta.compra72Hr, a.cpaVta.venta72Hr, a.cpaVta.compraVFut, a.cpaVta.ventaVFut, a.posIni.posicionInicialCash, a.posIni.posicionInicialTom, a.posIni.posicionInicialSpot, a.posIni.posicionInicial72Hr, a.posIni.posicionInicialVFut, a.posIni.posicionInicialMnCash, a.posIni.posicionInicialMnTom, a.posIni.posicionInicialMnSpot, a.posIni.posicionInicialMn72Hr, a.posIni.posicionInicialMnVFut, a.cpaVtaMn.compraMnPizarronCash, a.cpaVtaMn.ventaMnPizarronCash, a.cpaVtaMn.compraMnPizarronTom, a.cpaVtaMn.ventaMnPizarronTom, a.cpaVtaMn.compraMnPizarronSpot, a.cpaVtaMn.ventaMnPizarronSpot, a.cpaVtaMn.compraMnPizarron72Hr, a.cpaVtaMn.ventaMnPizarron72Hr, a.cpaVtaMn.compraMnPizarronVFut, a.cpaVtaMn.ventaMnPizarronVFut, a.cpaVtaIn.compraInCash, a.cpaVtaIn.ventaInCash, a.cpaVtaIn.compraInTom, a.cpaVtaIn.ventaInTom, a.cpaVtaIn.compraInSpot, a.cpaVtaIn.ventaInSpot, a.cpaVtaIn.compraIn72Hr, a.cpaVtaIn.ventaIn72Hr, a.cpaVtaIn.compraInVFut, a.cpaVtaIn.ventaInVFut, a.divisa.idDivisa) FROM Posicion AS a WHERE a.mesaCambio.idMesaCambio = ?", mesa.getIdMesaCambio());
            for (int cont2 = 0; cont2 < posiciones.size(); cont2++) {
                PosicionVO posicion = (PosicionVO) posiciones.get(cont2);
                double utilidad;
                double posicionInicialMn = posicion.getPosicionInicialMn().doubleValue();
                double comprasMn = posicion.getCompraMnPizarronCash().doubleValue() + posicion.getCompraMnPizarronTom().doubleValue() + posicion.getCompraMnPizarronSpot().doubleValue();
                double ventasMn = posicion.getVentaMnPizarronCash().doubleValue() + posicion.getVentaMnPizarronTom().doubleValue() + posicion.getVentaMnPizarronSpot().doubleValue();
                double posicionFinalMn = (posicionInicialMn + comprasMn) - ventasMn;
                double posicionInicialDivisa = posicion.getPosicionInicial().doubleValue();
                double comprasDivisa = posicion.getCompraCash().doubleValue() + posicion.getCompraTom().doubleValue() + posicion.getCompraSpot().doubleValue();
                double ventasDivisa = posicion.getVentaCash().doubleValue() + posicion.getVentaTom().doubleValue() + posicion.getVentaSpot().doubleValue();
                double posicionFinalDivisa = (posicionInicialDivisa + comprasDivisa) - ventasDivisa;
                double tipoCambioPromedio;
                if (posicionFinalMn == 0) {
                    tipoCambioPromedio = 0;
                }
                else {
                    tipoCambioPromedio = posicionFinalMn / posicionFinalDivisa;
                }
                if ("USD".equals(idDivisa)) {
                    utilidad = ((tasaCambioMercado * getFactorDivisaUtilidad(mesa.getIdMesaCambio(), posicion.getIdDivisa())) - tipoCambioPromedio) * posicionFinalDivisa;
                }
                else {
                    utilidad = ((tasaCambioMercado * getFactorDivisaMesa(posicion.getIdDivisa())) - tipoCambioPromedio) * posicionFinalDivisa;
                }
                UtilidadDivisaVO utilidadDivisaVO = new UtilidadDivisaVO(posicion.getIdDivisa(), new BigDecimal(utilidad + ""));
                utilidadGlobalVO.getDivisas().add(utilidadDivisaVO);
                utilidadMesa += utilidad;
            }
            if (idDivisa != null && !"MXN".equals(idDivisa)) {
                utilidadMesa = utilidadMesa * tasaCambioMercado;
            }
            utilidadGlobal = utilidadGlobal + utilidadMesa;
        }
        if (utilidadGlobal != Double.NaN) {
            utilidadGlobalVO.setUtilidadGlobalMn(new BigDecimal(utilidadGlobal + ""));
        }
        else {
            utilidadGlobalVO.setUtilidadGlobalMn(new BigDecimal("0"));
        }
        return utilidadGlobalVO;
    }

    /**
     * @see PosicionServiceData
     */
    public double getFactorDivisaUtilidad(Integer idMesaCambio, String toIdDivisa) {
        List factores = getHibernateTemplate().find("SELECT b.facDiv.factor FROM FactorDivisaActual AS b, MesaCambio AS c WHERE c.divisaReferencia.idDivisa = b.facDiv.toDivisa AND b.facDiv.fromDivisa = ? AND c.idMesaCambio = ?", new Object[]{toIdDivisa, idMesaCambio});
        if (!factores.isEmpty()) {
            return ((Double) factores.get(0)).doubleValue();
        }
        return 0;
    }

    /**
     * @see PosicionServiceData
     */
    public double getFactorDivisaMesa(String toIdDivisa) {
        List factores = getHibernateTemplate().find("SELECT b.facDiv.factor FROM FactorDivisaActual AS b WHERE b.facDiv.toDivisa = 'USD' AND b.facDiv.fromDivisa = ?", new Object[]{toIdDivisa});
        if (!factores.isEmpty()) {
            return ((Double) factores.get(0)).doubleValue();
        }
        return 0;
    }

    /**
     * Servicio que regresa una lista de divisas con su posicion, donde trae el detalle de la parte que conforma el efectivo y las otras formas de liquidacion.
     * @param idMesaCambio El id de la mesa de cambio.
     * @return Una lista con objetos PosicionVO, que tienen ademas el detalle de la posicion de Efectivo y el detalle del resto de las formas de liquidacion.
     */
    public List getPosicionDivisas( Integer idMesaCambio ) {
    	
        List divisas = getHibernateTemplate().find("SELECT distinct p.divisa.idDivisa FROM Posicion AS p");
        List posicionNormalDivisas = getHibernateTemplate().findByNamedParam(
                "SELECT new com.ixe.ods.sica.posicion.vo.PosicionVO( sum(pt.cpaVta.compraCash), " +
                "sum(pt.cpaVta.ventaCash), sum(pt.cpaVta.compraTom), sum(pt.cpaVta.ventaTom), " +
                "sum(pt.cpaVta.compraSpot), sum(pt.cpaVta.ventaSpot), sum(pt.cpaVta.compra72Hr), " +
                "sum(pt.cpaVta.venta72Hr), sum(pt.cpaVta.compraVFut), sum(pt.cpaVta.ventaVFut), " +
                "sum(pt.posIni.posicionInicialCash), sum(pt.posIni.posicionInicialTom), " +
                "sum(pt.posIni.posicionInicialSpot), sum(pt.posIni.posicionInicial72Hr), " +
                "sum(pt.posIni.posicionInicialVFut), sum(pt.posIni.posicionInicialMnCash), " +
                "sum(pt.posIni.posicionInicialMnTom), sum(pt.posIni.posicionInicialMnSpot), " +
                "sum(pt.posIni.posicionInicialMn72Hr), sum(pt.posIni.posicionInicialMnVFut), " +
                "sum(pt.cpaVtaMn.compraMnClienteCash), sum(pt.cpaVtaMn.ventaMnClienteCash), " +
                "sum(pt.cpaVtaMn.compraMnClienteTom), sum(pt.cpaVtaMn.ventaMnClienteTom), " +
                "sum(pt.cpaVtaMn.compraMnClienteSpot), sum(pt.cpaVtaMn.ventaMnClienteSpot), " +
                "sum(pt.cpaVtaMn.compraMnCliente72Hr), sum(pt.cpaVtaMn.ventaMnCliente72Hr), " +
                "sum(pt.cpaVtaMn.compraMnClienteVFut), sum(pt.cpaVtaMn.ventaMnClienteVFut), " +
                "sum(pt.cpaVtaMn.compraMnPizarronCash), sum(pt.cpaVtaMn.ventaMnPizarronCash), " +
                "sum(pt.cpaVtaMn.compraMnPizarronTom), sum(pt.cpaVtaMn.ventaMnPizarronTom), " +
                "sum(pt.cpaVtaMn.compraMnPizarronSpot), sum(pt.cpaVtaMn.ventaMnPizarronSpot), " +
                "sum(pt.cpaVtaMn.compraMnPizarron72Hr), sum(pt.cpaVtaMn.ventaMnPizarron72Hr), " +
                "sum(pt.cpaVtaMn.compraMnPizarronVFut), sum(pt.cpaVtaMn.ventaMnPizarronVFut), " +
                "sum(pt.cpaVtaMn.compraMnMesaCash), sum(pt.cpaVtaMn.ventaMnMesaCash), " +
                "sum(pt.cpaVtaMn.compraMnMesaTom), sum(pt.cpaVtaMn.ventaMnMesaTom), " +
                "sum(pt.cpaVtaMn.compraMnMesaSpot), sum(pt.cpaVtaMn.ventaMnMesaSpot), " +
                "sum(pt.cpaVtaMn.compraMnMesa72Hr), sum(pt.cpaVtaMn.ventaMnMesa72Hr), " +
                "sum(pt.cpaVtaMn.compraMnMesaVFut), sum(pt.cpaVtaMn.ventaMnMesaVFut), " +
                "sum(pt.cpaVtaIn.compraInCash), sum(pt.cpaVtaIn.ventaInCash), " +
                "sum(pt.cpaVtaIn.compraInTom), sum(pt.cpaVtaIn.ventaInTom), " +
                "sum(pt.cpaVtaIn.compraInSpot), sum(pt.cpaVtaIn.ventaInSpot), " +
                "sum(pt.cpaVtaIn.compraIn72Hr), sum(pt.cpaVtaIn.ventaIn72Hr), " +
                "sum(pt.cpaVtaIn.compraInVFut), sum(pt.cpaVtaIn.ventaInVFut), pt.divisa.idDivisa) " +
                "FROM Posicion AS pt " +
                "WHERE pt.mesaCambio = :idMesaCambio " +
                "GROUP BY pt.divisa.idDivisa",
                "idMesaCambio", idMesaCambio);
        List posicionDivisas = new ArrayList();
        for (int posiciones = 0; posiciones < posicionNormalDivisas.size(); posiciones++) {
            PosicionVO posicion = (PosicionVO) posicionNormalDivisas.get(posiciones);
            List posicionEfectivo = getHibernateTemplate().find("SELECT new com.ixe.ods.sica.posicion.vo.PosicionVO(sum(pe.cpaVta.compraCash), " +
            		"sum(pe.cpaVta.ventaCash), sum(pe.cpaVta.compraTom), sum(pe.cpaVta.ventaTom), " +
            		"sum(pe.cpaVta.compraSpot), sum(pe.cpaVta.ventaSpot), sum(pe.cpaVta.compra72Hr), " +
            		"sum(pe.cpaVta.venta72Hr), sum(pe.cpaVta.compraVFut), sum(pe.cpaVta.ventaVFut), " +
            		"sum(pe.posIni.posicionInicialCash), sum(pe.posIni.posicionInicialTom), sum(pe.posIni.posicionInicialSpot), " +
            		"sum(pe.posIni.posicionInicial72Hr), sum(pe.posIni.posicionInicialVFut), sum(pe.posIni.posicionInicialMnCash), " +
            		"sum(pe.posIni.posicionInicialMnTom), sum(pe.posIni.posicionInicialMnSpot), sum(pe.posIni.posicionInicialMn72Hr), " +
            		"sum(pe.posIni.posicionInicialMnVFut), sum(pe.cpaVtaMn.compraMnClienteCash), sum(pe.cpaVtaMn.ventaMnClienteCash), " +
            		"sum(pe.cpaVtaMn.compraMnClienteTom), sum(pe.cpaVtaMn.ventaMnClienteTom), sum(pe.cpaVtaMn.compraMnClienteSpot), " +
            		"sum(pe.cpaVtaMn.ventaMnClienteSpot), sum(pe.cpaVtaMn.compraMnCliente72Hr), sum(pe.cpaVtaMn.ventaMnCliente72Hr), " +
            		"sum(pe.cpaVtaMn.compraMnClienteVFut), sum(pe.cpaVtaMn.ventaMnClienteVFut), sum(pe.cpaVtaMn.compraMnPizarronCash), " +
            		"sum(pe.cpaVtaMn.ventaMnPizarronCash), sum(pe.cpaVtaMn.compraMnPizarronTom), sum(pe.cpaVtaMn.ventaMnPizarronTom), " +
            		"sum(pe.cpaVtaMn.compraMnPizarronSpot), sum(pe.cpaVtaMn.ventaMnPizarronSpot), sum(pe.cpaVtaMn.compraMnPizarron72Hr), " +
            		"sum(pe.cpaVtaMn.ventaMnPizarron72Hr), sum(pe.cpaVtaMn.compraMnPizarronVFut), sum(pe.cpaVtaMn.ventaMnPizarronVFut), " +
            		"sum(pe.cpaVtaMn.compraMnMesaCash), sum(pe.cpaVtaMn.ventaMnMesaCash), sum(pe.cpaVtaMn.compraMnMesaTom), " +
            		"sum(pe.cpaVtaMn.ventaMnMesaTom), sum(pe.cpaVtaMn.compraMnMesaSpot), sum(pe.cpaVtaMn.ventaMnMesaSpot), " +
            		"sum(pe.cpaVtaMn.compraMnMesa72Hr), sum(pe.cpaVtaMn.ventaMnMesa72Hr), sum(pe.cpaVtaMn.compraMnMesaVFut), " +
            		"sum(pe.cpaVtaMn.ventaMnMesaVFut), sum(pe.cpaVtaIn.compraInCash), sum(pe.cpaVtaIn.ventaInCash), " +
            		"sum(pe.cpaVtaIn.compraInTom), sum(pe.cpaVtaIn.ventaInTom), sum(pe.cpaVtaIn.compraInSpot), " +
            		"sum(pe.cpaVtaIn.ventaInSpot), sum(pe.cpaVtaIn.compraIn72Hr), sum(pe.cpaVtaIn.ventaIn72Hr), " +
            		"sum(pe.cpaVtaIn.compraInVFut), sum(pe.cpaVtaIn.ventaInVFut)) " +
            		"FROM PosicionDetalle AS pe WHERE pe.divisa.idDivisa = ? " +
            		"AND pe.claveFormaLiquidacion = 'EFECTI' " +
            		"AND pe.mesaCambio = ? "+
            		"GROUP BY pe.divisa.idDivisa", new Object[]{posicion.getIdDivisa(), idMesaCambio});
            PosicionVO efectivo;
            if (posicionEfectivo == null || posicionEfectivo.isEmpty()) {
                efectivo = null;
            }
            else {
                efectivo = (PosicionVO) posicionEfectivo.get(0);
            }
            boolean efectivoNoNulo = efectivo != null;
            BigDecimal compras = efectivoNoNulo ? efectivo.getCompraCash().add(efectivo.getCompraSpot()).add(efectivo.getCompraTom()) : new BigDecimal(0);
            BigDecimal ventas = efectivoNoNulo ? efectivo.getVentaCash().add(efectivo.getVentaSpot()).add(efectivo.getVentaTom()) : new BigDecimal(0);
            BigDecimal comprasCliente = efectivoNoNulo ? efectivo.getCompraMnClienteCash().add(efectivo.getCompraMnClienteSpot()).add(efectivo.getCompraMnClienteTom()) : new BigDecimal(0);
            BigDecimal ventasCliente = efectivoNoNulo ? efectivo.getVentaMnClienteCash().add(efectivo.getVentaMnClienteSpot()).add(efectivo.getVentaMnClienteTom()) : new BigDecimal(0);
            BigDecimal comprasPizarron = efectivoNoNulo ? efectivo.getCompraMnPizarronCash().add(efectivo.getCompraMnPizarronSpot()).add(efectivo.getCompraMnPizarronTom()) : new BigDecimal(0);
            BigDecimal ventasPizarron = efectivoNoNulo ? efectivo.getVentaMnPizarronCash().add(efectivo.getVentaMnPizarronSpot()).add(efectivo.getVentaMnPizarronTom()) : new BigDecimal(0);
            BigDecimal comprasMesa = efectivoNoNulo ? efectivo.getCompraMnMesaCash().add(efectivo.getCompraMnMesaSpot()).add(efectivo.getCompraMnMesaTom()) : new BigDecimal(0);
            BigDecimal ventasMesa = efectivoNoNulo ? efectivo.getVentaMnMesaCash().add(efectivo.getVentaMnMesaSpot()).add(efectivo.getVentaMnMesaTom()) : new BigDecimal(0);
            BigDecimal efectivoPF = efectivoNoNulo ? efectivo.getPosicionInicial().add(compras.add((new BigDecimal(-1)).multiply(ventas))) : new BigDecimal(0);
            BigDecimal efectivoClienteMN = efectivoNoNulo ? efectivo.getPosicionInicialMn().add(comprasCliente.add(ventasCliente.negate())) : new BigDecimal(0);
            BigDecimal efectivoPizarronMN = efectivoNoNulo ? efectivo.getPosicionInicialMn().add(comprasPizarron.add(ventasPizarron.negate())) : new BigDecimal(0);
            BigDecimal efectivoMesaMN = efectivoNoNulo ? efectivo.getPosicionInicialMn().add(comprasMesa.add(ventasMesa.negate())) : new BigDecimal(0);
            BigDecimal posicionInicialOtros = posicion.getPosicionInicial().add(efectivoNoNulo ? efectivo.getPosicionInicial().negate() : new BigDecimal(0));
            BigDecimal compraOtrosCash = posicion.getCompraCash().add(efectivoNoNulo ? efectivo.getCompraCash().negate() : new BigDecimal(0));
            BigDecimal compraOtrosTom = posicion.getCompraTom().add(efectivoNoNulo ? efectivo.getCompraTom().negate() : new BigDecimal(0));
            BigDecimal compraOtrosSpot = posicion.getCompraSpot().add(efectivoNoNulo ? efectivo.getCompraSpot().negate() : new BigDecimal(0));
            BigDecimal ventaOtrosCash = posicion.getVentaCash().add(efectivoNoNulo ? efectivo.getVentaCash().negate() : new BigDecimal(0));
            BigDecimal ventaOtrosTom = posicion.getVentaTom().add(efectivoNoNulo ? efectivo.getVentaTom().negate() : new BigDecimal(0));
            BigDecimal ventaOtrosSpot = posicion.getVentaSpot().add(efectivoNoNulo ? efectivo.getVentaSpot().negate() : new BigDecimal(0));
            BigDecimal posicionInicialOtrosClienteMN = posicion.getPosicionInicialMn().add(efectivoNoNulo ? efectivo.getPosicionInicialMn().negate() : new BigDecimal(0));
            BigDecimal posicionInicialOtrosMesaMN = posicion.getPosicionInicialMn().add(efectivoNoNulo ? efectivo.getPosicionInicialMn().negate() : new BigDecimal(0));
            BigDecimal posicionInicialOtrosPizarronMN = posicion.getPosicionInicialMn().add(efectivoNoNulo ? efectivo.getPosicionInicialMn().negate() : new BigDecimal(0));
            BigDecimal compraOtrosMnClienteCashMN = posicion.getCompraMnClienteCash().add(efectivoNoNulo ? efectivo.getCompraMnClienteCash().negate() : new BigDecimal(0));
            BigDecimal compraOtrosMnClienteTomMN = posicion.getCompraMnClienteTom().add(efectivoNoNulo ? efectivo.getCompraMnClienteTom().negate() : new BigDecimal(0));
            BigDecimal compraOtrosMnClienteSpotMN = posicion.getCompraMnClienteSpot().add(efectivoNoNulo ? efectivo.getCompraMnClienteSpot().negate() : new BigDecimal(0));
            BigDecimal ventaOtrosMnClienteCashMN = posicion.getVentaMnClienteCash().add(efectivoNoNulo ? efectivo.getVentaMnClienteCash().negate() : new BigDecimal(0));
            BigDecimal ventaOtrosMnClienteTomMN = posicion.getVentaMnClienteTom().add(efectivoNoNulo ? efectivo.getVentaMnClienteTom().negate() : new BigDecimal(0));
            BigDecimal ventaOtrosMnClienteSpotMN = posicion.getVentaMnClienteSpot().add(efectivoNoNulo ? efectivo.getVentaMnClienteSpot().negate() : new BigDecimal(0));
            BigDecimal compraOtrosMnMesaCashMN = posicion.getCompraMnMesaCash().add(efectivoNoNulo ? efectivo.getCompraMnMesaCash().negate() : new BigDecimal(0));
            BigDecimal compraOtrosMnMesaTomMN = posicion.getCompraMnMesaTom().add(efectivoNoNulo ? efectivo.getCompraMnMesaTom().negate() : new BigDecimal(0));
            BigDecimal compraOtrosMnMesaSpotMN = posicion.getCompraMnMesaSpot().add(efectivoNoNulo ? efectivo.getCompraMnMesaSpot().negate() : new BigDecimal(0));
            BigDecimal ventaOtrosMnMesaCashMN = posicion.getVentaMnMesaCash().add(efectivoNoNulo ? efectivo.getVentaMnMesaCash().negate() : new BigDecimal(0));
            BigDecimal ventaOtrosMnMesaTomMN = posicion.getVentaMnMesaTom().add(efectivoNoNulo ? efectivo.getVentaMnMesaTom().negate() : new BigDecimal(0));
            BigDecimal ventaOtrosMnMesaSpotMN = posicion.getVentaMnMesaSpot().add(efectivoNoNulo ? efectivo.getVentaMnMesaSpot().negate() : new BigDecimal(0));
            BigDecimal compraOtrosMnPizarronCashMN = posicion.getCompraMnPizarronCash().add(efectivoNoNulo ? efectivo.getCompraMnPizarronCash().negate() : new BigDecimal(0));
            BigDecimal compraOtrosMnPizarronTomMN = posicion.getCompraMnPizarronTom().add(efectivoNoNulo ? efectivo.getCompraMnPizarronTom().negate() : new BigDecimal(0));
            BigDecimal compraOtrosMnPizarronSpotMN = posicion.getCompraMnPizarronSpot().add(efectivoNoNulo ? efectivo.getCompraMnPizarronSpot().negate() : new BigDecimal(0));
            BigDecimal ventaOtrosMnPizarronCashMN = posicion.getVentaMnPizarronCash().add(efectivoNoNulo ? efectivo.getVentaMnPizarronCash().negate() : new BigDecimal(0));
            BigDecimal ventaOtrosMnPizarronTomMN = posicion.getVentaMnPizarronTom().add(efectivoNoNulo ? efectivo.getVentaMnPizarronTom().negate() : new BigDecimal(0));
            BigDecimal ventaOtrosMnPizarronSpotMN = posicion.getVentaMnPizarronSpot().add(efectivoNoNulo ? efectivo.getVentaMnPizarronSpot().negate() : new BigDecimal(0));
            BigDecimal otrosPF = posicionInicialOtros.add((compraOtrosCash.add(compraOtrosTom).add(compraOtrosSpot))).add((ventaOtrosCash.add(ventaOtrosTom).add(ventaOtrosSpot)).negate());
            BigDecimal otrosPFClienteMN = posicionInicialOtrosClienteMN.add(compraOtrosMnClienteCashMN.add(compraOtrosMnClienteTomMN).add(compraOtrosMnClienteSpotMN)).add(ventaOtrosMnClienteCashMN.add(ventaOtrosMnClienteTomMN).add(ventaOtrosMnClienteSpotMN).negate());
            BigDecimal otrosPFPizarronMN = posicionInicialOtrosPizarronMN.add(compraOtrosMnPizarronCashMN.add(compraOtrosMnPizarronTomMN).add(compraOtrosMnPizarronSpotMN)).add(ventaOtrosMnPizarronCashMN.add(ventaOtrosMnPizarronTomMN).add(ventaOtrosMnPizarronSpotMN).negate());
            BigDecimal otrosPFMesaMN = posicionInicialOtrosMesaMN.add(compraOtrosMnMesaCashMN.add(compraOtrosMnMesaTomMN).add(compraOtrosMnMesaSpotMN)).add(ventaOtrosMnMesaCashMN.add(ventaOtrosMnMesaTomMN).add(ventaOtrosMnMesaSpotMN).negate());
            posicion.completarInformacionDivisa(efectivoPF, otrosPF, otrosPFClienteMN, otrosPFPizarronMN, otrosPFMesaMN, efectivoClienteMN, efectivoPizarronMN, efectivoMesaMN);
            posicionDivisas.add(posicion);
        }
        List divisasExistentes = getHibernateTemplate().findByNamedParam("FROM Divisa AS d WHERE d.idDivisa in (:divisas) ORDER BY d.orden", "divisas", divisas);
        List posicionDivisasOrdenada = new ArrayList(posicionDivisas.size());
        for (int i = 0; i < divisasExistentes.size(); i++) {
            Divisa div = (Divisa) divisasExistentes.get(i);
            for (int j = 0; j < posicionDivisas.size(); j++) {
                PosicionVO pos = (PosicionVO) posicionDivisas.get(j);
                if (pos.getIdDivisa().equals(div.getIdDivisa())) {
                    posicionDivisasOrdenada.add(i, pos);
                }
            }
        }
        return posicionDivisasOrdenada;
    }

    /**
     * Obtiene la posicion de efectivo de una lista de sucursales (puede
     * ser solo una)
     *
     * @param idMesaCambio El identificador de la mesa de cambio.
     * @param idDivisa La clave de la divisa.
     * @param sucursales La lista de sucursales.
     * @return PosicionVO
     */
    public PosicionVO getQueryPosicionInventarioEfectivo(Integer idMesaCambio, String idDivisa, List sucursales) {
        PosicionVO posicionVO;
        List listaSucursales = new ArrayList();
        //Obtenemos la lista de identificadores de las sucursales
        for(int cont = 0; cont < sucursales.size(); cont++) {
            String idCanal = (String)sucursales.get(cont);
            //Obtenemos la posicion de la sucursal seleccionada
            PosicionVO posicionVOTmp;
            posicionVOTmp = getPosicionCanal(idMesaCambio, idDivisa, idCanal);
            if(posicionVOTmp != null) {
                listaSucursales.add(posicionVOTmp);
            }
        }
        posicionVO = sumatoriaPosicion(listaSucursales);
        BigDecimal sumaCompras = posicionVO.getCompraCash().add(posicionVO.getCompraTom().add(posicionVO.getCompraSpot()));
        BigDecimal sumaVentas = posicionVO.getVentaCash().add(posicionVO.getVentaTom().add(posicionVO.getVentaSpot()));
        //Cargamos la divisa para obtener el identificador de la moneda
        Divisa divisa = (Divisa)getHibernateTemplate().get(Divisa.class, idDivisa);
        Map totalServicioTeller = new HashMap();
        for(int cont = 0; cont < sucursales.size(); cont++) {
        	//Cargamos el canal para obtener el identificador de la sucursal
        	String idSucursal = (String)sucursales.get(cont);
        	Canal canal = (Canal)getHibernateTemplate().get(Canal.class, idSucursal);
        	Map map = getClienteServiceData().getPosicionSucursal(canal.getSucursal().getIdSucursal(), Integer.valueOf(divisa.getIdMoneda()).intValue());
        	System.out.println("\n\nServicio Gabriel para sucursal:" + canal.getSucursal().getIdSucursal() + "  y el idMoneda:" + divisa.getIdMoneda() + "      Los resultados son:" + map + "\n");
        	//Sumatoria del servicio de Gabriel
        	BigDecimal posicionInicial = totalServicioTeller.get("posicionInicial") == null ? (BigDecimal)map.get("posicionInicial") : ((BigDecimal)totalServicioTeller.get("posicionInicial")).add((BigDecimal)map.get("posicionInicial"));
            BigDecimal depositosEfectivo = totalServicioTeller.get("depositosEfectivo") == null ? (BigDecimal)map.get("depositosEfectivo") : ((BigDecimal)totalServicioTeller.get("depositosEfectivo")).add((BigDecimal)map.get("depositosEfectivo"));
            BigDecimal depositosBoveda = totalServicioTeller.get("depositosBoveda") == null ? (BigDecimal)map.get("depositosBoveda") : ((BigDecimal)totalServicioTeller.get("depositosBoveda")).add((BigDecimal)map.get("depositosBoveda"));
            BigDecimal retirosEfectivo = totalServicioTeller.get("retirosEfectivo") == null ? (BigDecimal)map.get("retirosEfectivo") : ((BigDecimal)totalServicioTeller.get("retirosEfectivo")).add((BigDecimal)map.get("retirosEfectivo"));
            BigDecimal retirosBoveda = totalServicioTeller.get("retirosBoveda") == null ? (BigDecimal)map.get("retirosBoveda") : ((BigDecimal)totalServicioTeller.get("retirosBoveda")).add((BigDecimal)map.get("retirosBoveda"));
            totalServicioTeller.put("posicionInicial", posicionInicial);
            totalServicioTeller.put("depositosEfectivo", depositosEfectivo);
            totalServicioTeller.put("depositosBoveda", depositosBoveda);
            totalServicioTeller.put("retirosEfectivo", retirosEfectivo);
            totalServicioTeller.put("retirosBoveda", retirosBoveda);
        }
        BigDecimal depositosEfectivo = (BigDecimal)totalServicioTeller.get("depositosEfectivo");
        BigDecimal retirosEfectivo = (BigDecimal)totalServicioTeller.get("retirosEfectivo");
        //Si la suma de las compras del SICA (cambiarios) es mayor que el total de
        //despositos en efectivo (bancarios + cambiarios), existe un error
        if(sumaCompras.compareTo(depositosEfectivo) > 0) {

        }
        //Si la suma de las ventas del SICA (cambiarios) es mayor que el total de
        //retiros en efectivo (cambiarios + bancarios) existe un error
        if(sumaVentas.compareTo(retirosEfectivo) > 0) {

        }
        posicionVO.setPosicionInicialTeller((BigDecimal)totalServicioTeller.get("posicionInicial"));
        posicionVO.setDepositosEfectivoTeller((BigDecimal)totalServicioTeller.get("depositosEfectivo"));
        posicionVO.setDepositosBovedaTeller((BigDecimal)totalServicioTeller.get("depositosBoveda"));
        posicionVO.setRetirosEfectivoTeller((BigDecimal)totalServicioTeller.get("retirosEfectivo"));
        posicionVO.setRetirosBovedaTeller((BigDecimal)totalServicioTeller.get("retirosBoveda"));
        return posicionVO;
    }

    /**
     * Obtiene la posicion de efectivo de todas las sucursales
     *
     * @param idMesaCambio El identificador de la mesa de cambios.
     * @param idDivisa La clave de la divisa.
     * @return PosicionVO.
     */
    public PosicionVO getPosicionInventarioEfectivo(Integer idMesaCambio, String idDivisa) {
        PosicionVO posicionVO = getPosicionSucursales(idMesaCambio, idDivisa);
        BigDecimal sumaCompras = posicionVO.getCompraCash().add(posicionVO.getCompraTom().add(posicionVO.getCompraSpot()));
        BigDecimal sumaVentas = posicionVO.getVentaCash().add(posicionVO.getVentaTom().add(posicionVO.getVentaSpot()));
        //Cargamos la divisa para obtener el identificador de la moneda
        Divisa divisa = (Divisa)getHibernateTemplate().get(Divisa.class, idDivisa);
        //Simulacion del servicio de Gabriel
        Map map = getClienteServiceData().getPosicionSucursal(null, Integer.valueOf(divisa.getIdMoneda()).intValue());
        System.out.println("\n\n\nServicio Gabriel para todas las sucursales y el idMoneda:" + divisa.getIdMoneda() + "      Los resultados son:" + map + "\n\n\n");
        BigDecimal depositosEfectivo = (BigDecimal)map.get("depositosEfectivo");
        BigDecimal retirosEfectivo = (BigDecimal)map.get("retirosEfectivo");
        //Si la suma de las compras del SICA (cambiarios) es mayor que el total de
        //despositos en efectivo (bancarios + cambiarios), existe un error
        if(sumaCompras.compareTo(depositosEfectivo) > 0) {
        	
        }
        //Si la suma de las ventas del SICA (cambiarios) es mayor que el total de
        //retiros en efectivo (cambiarios + bancarios) existe un error
        if(sumaVentas.compareTo(retirosEfectivo) > 0) {

        }
        posicionVO.setPosicionInicialTeller((BigDecimal)map.get("posicionInicial"));
        posicionVO.setDepositosEfectivoTeller((BigDecimal)map.get("depositosEfectivo"));
        posicionVO.setDepositosBovedaTeller((BigDecimal)map.get("depositosBoveda"));
        posicionVO.setRetirosEfectivoTeller((BigDecimal)map.get("retirosEfectivo"));
        posicionVO.setRetirosBovedaTeller((BigDecimal)map.get("retirosBoveda"));
        return posicionVO;
    }

    /**
     * Obtiene la sumatoria de los valores usados en getPosicionCanal
     *
     * @param list La lista de PosicionVO a sumar.
     * @return PosicionVO.
     */
    private PosicionVO sumatoriaPosicion(List list) {
        PosicionVO posicion = new PosicionVO();
        for (int cont = 0; cont < list.size(); cont++) {
            PosicionVO posicionTmp = (PosicionVO) list.get(cont);
            posicion = sumaPosicion(posicion, posicionTmp);
        }
        return posicion;
    }

    /**
     * Obtiene la suma de los valores usados en getPosicionProducto
     *
     * @param list La lista de PosicionGrupoVO.
     * @return List.
     */
    private List posicionGrupo(List list) {
        HashMap canales = new HashMap();
        List nombreCanales = new ArrayList();
        List posicionGrupos = new ArrayList();
        for (int cont = 0; cont < list.size(); cont++) {
            PosicionGrupoVO posicionGrupoTmp = (PosicionGrupoVO) list.get(cont);
            //Si no se ha guardado ningun objeto del canal especificado
            if (!canales.containsKey(posicionGrupoTmp.getNombre())) {
                canales.put(posicionGrupoTmp.getNombre(), posicionGrupoTmp);
                nombreCanales.add(posicionGrupoTmp.getNombre());
            }
            else {
                PosicionGrupoVO posicionGrupo = (PosicionGrupoVO) canales.get(posicionGrupoTmp.getNombre());
                posicionGrupo = sumaPosicionGrupo(posicionGrupo, posicionGrupoTmp);
                canales.put(posicionGrupo.getNombre(), posicionGrupo);
            }
        }
        for (int cont = 0; cont < nombreCanales.size(); cont++) {
            String nombreCanal = (String) nombreCanales.get(cont);
            PosicionGrupoVO posicionGrupo = (PosicionGrupoVO) canales.get(nombreCanal);
            posicionGrupos.add(posicionGrupo);
        }
        return posicionGrupos;
    }

    /**
     * Suma los valores de dos objetos del tipo PosicionVO
     *
     * @param posicion La posicion original.
     * @param posicionTmp La posicionVO temporal.
     * @return PosicionVO
     */
    private PosicionVO sumaPosicion(PosicionVO posicion, PosicionVO posicionTmp) {
        posicion.setCompraCash(posicion.getCompraCash().add(posicionTmp.getCompraCash()));
        posicion.setVentaCash(posicion.getVentaCash().add(posicionTmp.getVentaCash()));
        posicion.setCompraTom(posicion.getCompraTom().add(posicionTmp.getCompraTom()));
        posicion.setVentaTom(posicion.getVentaTom().add(posicionTmp.getVentaTom()));
        posicion.setCompraSpot(posicion.getCompraSpot().add(posicionTmp.getCompraSpot()));
        posicion.setVentaSpot(posicion.getVentaSpot().add(posicionTmp.getVentaSpot()));
        posicion.setCompra72Hr(posicion.getCompra72Hr().add(posicionTmp.getCompra72Hr()));
        posicion.setVenta72Hr(posicion.getVenta72Hr().add(posicionTmp.getVenta72Hr()));
        posicion.setCompraVFut(posicion.getCompraVFut().add(posicionTmp.getCompraVFut()));
        posicion.setVentaVFut(posicion.getVentaVFut().add(posicionTmp.getVentaVFut()));
        posicion.setPosicionInicialCash(posicion.getPosicionInicialCash().add(posicionTmp.getPosicionInicialCash()));
        posicion.setPosicionInicialTom(posicion.getPosicionInicialTom().add(posicionTmp.getPosicionInicialTom()));
        posicion.setPosicionInicialSpot(posicion.getPosicionInicialSpot().add(posicionTmp.getPosicionInicialSpot()));
        posicion.setPosicionInicial72Hr(posicion.getPosicionInicial72Hr().add(posicionTmp.getPosicionInicial72Hr()));
        posicion.setPosicionInicialVFut(posicion.getPosicionInicialVFut().add(posicionTmp.getPosicionInicialVFut()));
        posicion.setPosicionInicialMnCash(posicion.getPosicionInicialMnCash().add(posicionTmp.getPosicionInicialMnCash()));
        posicion.setPosicionInicialMnTom(posicion.getPosicionInicialMnTom().add(posicionTmp.getPosicionInicialMnTom()));
        posicion.setPosicionInicialMnSpot(posicion.getPosicionInicialMnSpot().add(posicionTmp.getPosicionInicialMnSpot()));
        posicion.setPosicionInicialMn72Hr(posicion.getPosicionInicialMn72Hr().add(posicionTmp.getPosicionInicialMn72Hr()));
        posicion.setPosicionInicialMnVFut(posicion.getPosicionInicialMnVFut().add(posicionTmp.getPosicionInicialMnVFut()));
        posicion.setCompraMnClienteCash(posicion.getCompraMnClienteCash().add(posicionTmp.getCompraMnClienteCash()));
        posicion.setVentaMnClienteCash(posicion.getVentaMnClienteCash().add(posicionTmp.getVentaMnClienteCash()));
        posicion.setCompraMnClienteTom(posicion.getCompraMnClienteTom().add(posicionTmp.getCompraMnClienteTom()));
        posicion.setVentaMnClienteTom(posicion.getVentaMnClienteTom().add(posicionTmp.getVentaMnClienteTom()));
        posicion.setCompraMnClienteSpot(posicion.getCompraMnClienteSpot().add(posicionTmp.getCompraMnClienteSpot()));
        posicion.setVentaMnClienteSpot(posicion.getVentaMnClienteSpot().add(posicionTmp.getVentaMnClienteSpot()));
        posicion.setCompraMnCliente72Hr(posicion.getCompraMnCliente72Hr().add(posicionTmp.getCompraMnCliente72Hr()));
        posicion.setVentaMnCliente72Hr(posicion.getVentaMnCliente72Hr().add(posicionTmp.getVentaMnCliente72Hr()));
        posicion.setCompraMnClienteVFut(posicion.getCompraMnClienteVFut().add(posicionTmp.getCompraMnClienteVFut()));
        posicion.setVentaMnClienteVFut(posicion.getVentaMnClienteVFut().add(posicionTmp.getVentaMnClienteVFut()));
        posicion.setCompraMnPizarronCash(posicion.getCompraMnPizarronCash().add(posicionTmp.getCompraMnPizarronCash()));
        posicion.setVentaMnPizarronCash(posicion.getVentaMnPizarronCash().add(posicionTmp.getVentaMnPizarronCash()));
        posicion.setCompraMnPizarronTom(posicion.getCompraMnPizarronTom().add(posicionTmp.getCompraMnPizarronTom()));
        posicion.setVentaMnPizarronTom(posicion.getVentaMnPizarronTom().add(posicionTmp.getVentaMnPizarronTom()));
        posicion.setCompraMnPizarronSpot(posicion.getCompraMnPizarronSpot().add(posicionTmp.getCompraMnPizarronSpot()));
        posicion.setVentaMnPizarronSpot(posicion.getVentaMnPizarronSpot().add(posicionTmp.getVentaMnPizarronSpot()));
        posicion.setCompraMnPizarron72Hr(posicion.getCompraMnPizarron72Hr().add(posicionTmp.getCompraMnPizarron72Hr()));
        posicion.setVentaMnPizarron72Hr(posicion.getVentaMnPizarron72Hr().add(posicionTmp.getVentaMnPizarron72Hr()));
        posicion.setCompraMnPizarronVFut(posicion.getCompraMnPizarronVFut().add(posicionTmp.getCompraMnPizarronVFut()));
        posicion.setVentaMnPizarronVFut(posicion.getVentaMnPizarronVFut().add(posicionTmp.getVentaMnPizarronVFut()));
        posicion.setCompraMnMesaCash(posicion.getCompraMnMesaCash().add(posicionTmp.getCompraMnMesaCash()));
        posicion.setVentaMnMesaCash(posicion.getVentaMnMesaCash().add(posicionTmp.getVentaMnMesaCash()));
        posicion.setCompraMnMesaTom(posicion.getCompraMnMesaTom().add(posicionTmp.getCompraMnMesaTom()));
        posicion.setVentaMnMesaTom(posicion.getVentaMnMesaTom().add(posicionTmp.getVentaMnMesaTom()));
        posicion.setCompraMnMesaSpot(posicion.getCompraMnMesaSpot().add(posicionTmp.getCompraMnMesaSpot()));
        posicion.setVentaMnMesaSpot(posicion.getVentaMnMesaSpot().add(posicionTmp.getVentaMnMesaSpot()));
        posicion.setCompraMnMesa72Hr(posicion.getCompraMnMesa72Hr().add(posicionTmp.getCompraMnMesa72Hr()));
        posicion.setVentaMnMesa72Hr(posicion.getVentaMnMesa72Hr().add(posicionTmp.getVentaMnMesa72Hr()));
        posicion.setCompraMnMesaVFut(posicion.getCompraMnMesaVFut().add(posicionTmp.getCompraMnMesaVFut()));
        posicion.setVentaMnMesaVFut(posicion.getVentaMnMesaVFut().add(posicionTmp.getVentaMnMesaVFut()));
        posicion.setCompraInCash(posicion.getCompraInCash().add(posicionTmp.getCompraInCash()));
        posicion.setCompraInTom(posicion.getCompraInTom().add(posicionTmp.getCompraInTom()));
        posicion.setCompraInSpot(posicion.getCompraInSpot().add(posicionTmp.getCompraInSpot()));
        posicion.setCompraIn72Hr(posicion.getCompraIn72Hr().add(posicionTmp.getCompraIn72Hr()));
        posicion.setCompraInVFut(posicion.getCompraInVFut().add(posicionTmp.getCompraInVFut()));
        posicion.setVentaInCash(posicion.getVentaInCash().add(posicionTmp.getVentaInCash()));
        posicion.setVentaInTom(posicion.getVentaInTom().add(posicionTmp.getVentaInTom()));
        posicion.setVentaInSpot(posicion.getVentaInSpot().add(posicionTmp.getVentaInSpot()));
        posicion.setVentaIn72Hr(posicion.getVentaIn72Hr().add(posicionTmp.getVentaIn72Hr()));
        posicion.setVentaInVFut(posicion.getVentaInVFut().add(posicionTmp.getVentaInVFut()));
        return posicion;
    }

    /**
     * Suma los valores de dos objetos del tipo PosicionGrupoVO
     *
     * @param posicionGrupo La posici&oacute;n original.
     * @param posicionGrupoTmp La posici&oacute;n temporal.
     * @return PosicionGrupoVO.
     */
    private PosicionGrupoVO sumaPosicionGrupo(PosicionGrupoVO posicionGrupo, PosicionGrupoVO posicionGrupoTmp) {
        posicionGrupo.setMontoCash(posicionGrupo.getMontoCash().add(posicionGrupoTmp.getMontoCash()));
        posicionGrupo.setMontoMnCash(posicionGrupo.getMontoMnCash().add(posicionGrupoTmp.getMontoMnCash()));
        posicionGrupo.setMontoTom(posicionGrupo.getMontoTom().add(posicionGrupoTmp.getMontoTom()));
        posicionGrupo.setMontoMnTom(posicionGrupo.getMontoMnTom().add(posicionGrupoTmp.getMontoMnTom()));
        posicionGrupo.setMontoSpot(posicionGrupo.getMontoSpot().add(posicionGrupoTmp.getMontoSpot()));
        posicionGrupo.setMontoMnSpot(posicionGrupo.getMontoMnSpot().add(posicionGrupoTmp.getMontoMnSpot()));
        posicionGrupo.setMonto72Hr(posicionGrupo.getMonto72Hr().add(posicionGrupoTmp.getMonto72Hr()));
        posicionGrupo.setMontoMn72Hr(posicionGrupo.getMontoMn72Hr().add(posicionGrupoTmp.getMontoMn72Hr()));
        posicionGrupo.setMontoVFut(posicionGrupo.getMontoVFut().add(posicionGrupoTmp.getMontoVFut()));
        posicionGrupo.setMontoMnVFut(posicionGrupo.getMontoMnVFut().add(posicionGrupoTmp.getMontoMnVFut()));
        posicionGrupo.setMontoInCash(posicionGrupo.getMontoInCash().add(posicionGrupoTmp.getMontoInCash()));
        posicionGrupo.setMontoInTom(posicionGrupo.getMontoInTom().add(posicionGrupoTmp.getMontoInTom()));
        posicionGrupo.setMontoInSpot(posicionGrupo.getMontoInSpot().add(posicionGrupoTmp.getMontoInSpot()));
        posicionGrupo.setMontoIn72Hr(posicionGrupo.getMontoIn72Hr().add(posicionGrupoTmp.getMontoIn72Hr()));
        posicionGrupo.setMontoInVFut(posicionGrupo.getMontoInVFut().add(posicionGrupoTmp.getMontoInVFut()));
        return posicionGrupo;
    }
    
    /**
     * @see PosicionServiceData#findPosicionesParaSiar(String, Date, Integer, boolean).
     */
    public List findPosicionesParaSiar(final String idDivisa, final Date dia, 
    		final Integer idMesaCambio, final boolean historico) {
    	return getHibernateTemplate().executeFind(new HibernateCallback() {
    		public Object doInHibernate(Session session) throws HibernateException, SQLException {
    			Criteria criteria; 
    			if (historico) {
    				criteria = session.createCriteria(HistoricoPosicion.class);
    				criteria.add(Expression.eq("mesaCambio.idMesaCambio", idMesaCambio));
        			if (idDivisa != null) {
        				criteria.add(Expression.eq("divisa.idDivisa", idDivisa));
        			}
        			criteria.add(Expression.between("ultimaModificacion", DateUtils.inicioDia(dia), 
    						DateUtils.finDia(dia)));
    			}
    			else {
    				criteria = session.createCriteria(Posicion.class);
        			criteria.add(Expression.eq("mesaCambio.idMesaCambio", idMesaCambio));
        			if (idDivisa != null) {
        				criteria.add(Expression.eq("divisa.idDivisa", idDivisa));
        			}
    			}
    			return criteria.list();
    		}     
    	});
    }
    
    /**
     * Obtiene el valor de clienteServiceData
     * 
     * @return ITeller
     */
    public ITeller getClienteServiceData() {
        return clienteServiceData;
    }

    /**
     * Asigna el valor para clienteServiceData.
     * 
     * @param clienteServiceData El valor para clienteServiceData.
     */
    public void setClienteServiceData(ITeller clienteServiceData) {
        this.clienteServiceData = clienteServiceData;
    }

    /**
     * La interfaz para el TELLER.
     */
    private ITeller clienteServiceData;
}