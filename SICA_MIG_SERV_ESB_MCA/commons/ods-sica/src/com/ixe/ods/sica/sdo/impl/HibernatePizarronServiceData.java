/*
 * $Id: HibernatePizarronServiceData.java,v 1.34.10.1.14.4.52.2 2020/12/03 21:59:07 mejiar Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 - 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica.sdo.impl;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate.HibernateCallback;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

import com.ixe.ods.bup.model.FechaNoLaboral;
import com.ixe.ods.seguridad.model.IUsuario;
import com.ixe.ods.sica.Constantes;
import com.ixe.ods.sica.Num;
import com.ixe.ods.sica.Redondeador;
import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.SicaFechaValorException;
import com.ixe.ods.sica.SicaSiteCache;
import com.ixe.ods.sica.model.Canal;
import com.ixe.ods.sica.model.Divisa;
import com.ixe.ods.sica.model.FactorDivisaActual;
import com.ixe.ods.sica.model.ParametroSica;
import com.ixe.ods.sica.model.PrecioReferenciaActual;
import com.ixe.ods.sica.model.RenglonPizarron;
import com.ixe.ods.sica.model.Spread;
import com.ixe.ods.sica.model.SpreadActual;
import com.ixe.ods.sica.model.TipoPizarron;
import com.ixe.ods.sica.model.Variacion;
import com.ixe.ods.sica.posicion.vo.ParametrosPizarronVO;
import com.ixe.ods.sica.sdo.PizarronServiceData;
import com.ixe.ods.sica.sdo.SicaServiceData;
import com.ixe.ods.sica.services.ValorFuturoService;
import com.ixe.ods.sica.utils.DateUtils;
import com.ixe.treasury.dom.common.FormaPagoLiq;

/**
 * <p>Subclase de AbstractHibernateSicaServiceData que implementa los servicios del
 * Pizarr&oacute;n.<p/>
 *
 * @author Jean C. Favila
 * @version $Revision: 1.34.10.1.14.4.52.2 $ $Date: 2020/12/03 21:59:07 $
 */
public class HibernatePizarronServiceData extends AbstractHibernateSicaServiceData
        implements PizarronServiceData {
	
    /**
     * Constructor por default.
     */
    public HibernatePizarronServiceData() {
        super();
    }

    /**
     * @param idCanal La clave del canal.
     * @param idDivisa La clave de la divisa.
     * @param idProducto La clave de la forma de liquidaci&oacute;n.
     * @param recibimos True para compra, False para venta.
     * @param fechaValor La fecha valor a consultar.
     * @return double.
     * @see com.ixe.ods.sica.sdo.PizarronServiceData#findPrecioPizarronPesos(String, String,
     * String, boolean, String).
     */
    public double findPrecioPizarronPesos(String idCanal, String idDivisa, String idProducto,
                                          boolean recibimos, String fechaValor) {
        List rps = getHibernateTemplate().
        	findByNamedQuery("findRenglonPizarronByCanalDivisaProducto",
                new Object[] { idCanal, idDivisa, idProducto });
        if (rps.isEmpty()) {
            throw new SicaException("No se encontr\u00f3 informaci\u00f3n del pizarr\u00f3n para " +
                    "el canal: " + idCanal + ", divisa: " + idDivisa + " y producto: " +
                    idProducto);
        }
        RenglonPizarron rp = (RenglonPizarron) rps.get(0);
        if (recibimos) {
            if (Constantes.CASH.equals(fechaValor)) {
                return Redondeador.redondear6Dec(rp.getCompraCash().doubleValue());
            }
            else if (Constantes.TOM.equals(fechaValor.trim())) {
                return Redondeador.redondear6Dec(rp.getCompraTom().doubleValue());
            }
            else if (Constantes.SPOT.equals(fechaValor)) {
                return Redondeador.redondear6Dec(rp.getCompraSpot().doubleValue());
            }
            else if (Constantes.HR72.equals(fechaValor)) {
                return Redondeador.redondear6Dec(rp.getCompra72Hr().doubleValue());
            }
            else if (Constantes.VFUT.equals(fechaValor)) {
                return Redondeador.redondear6Dec(rp.getCompraVFut().doubleValue());
            }
        }
        else {
            if (Constantes.CASH.equals(fechaValor)) {
                return Redondeador.redondear6Dec(rp.getVentaCash().doubleValue());
            }
            else if (Constantes.TOM.equals(fechaValor.trim())) {
                return Redondeador.redondear6Dec(rp.getVentaTom().doubleValue());
            }
            else if (Constantes.SPOT.equals(fechaValor)) {
                return Redondeador.redondear6Dec(rp.getVentaSpot().doubleValue());
            }
            else if (Constantes.HR72.equals(fechaValor)) {
                return Redondeador.redondear6Dec(rp.getVenta72Hr().doubleValue());
            }
            else if (Constantes.VFUT.equals(fechaValor)) {
                return Redondeador.redondear6Dec(rp.getVentaVFut().doubleValue());
            }
        }
        throw new SicaException("No se encontr\u00f3 un precio para la fecha valor: "
        		+ fechaValor);
    }
    
    /**
     * @param tipoPizarron El tipo de pizarron para el canal.
     * @param idDivisa La clave de la divisa.
     * @param idProducto La clave de la forma de liquidaci&oacute;n.
     * @param recibimos True para compra, False para venta.
     * @param fechaValor La fecha valor a consultar.
     * @return double.
     * @see com.ixe.ods.sica.sdo.PizarronServiceData#findPrecioPizarronPesos(TipoPizarron, 
     * String, String, boolean, String).
     */
    public double findPrecioPizarronPesos(TipoPizarron tipoPizarron, String idDivisa, 
    		String idProducto, boolean recibimos, String fechaValor) {
        List rps = getHibernateTemplate().findByNamedQuery(
        		"findRenglonPizarronByTipoPizarronDivisaProducto",
                new Object[] { tipoPizarron.getIdTipoPizarron(), idDivisa, idProducto });
        if (rps.isEmpty()) {
            throw new SicaException("No se encontr\u00f3 informaci\u00f3n del pizarr\u00f3n para " +
                    "el pizarron: " + tipoPizarron.getIdTipoPizarron().intValue() + ", divisa: " +
                    idDivisa + " y producto: " + idProducto);
        }
        RenglonPizarron rp = (RenglonPizarron) rps.get(0);
        if (recibimos) {
            if (Constantes.CASH.equals(fechaValor)) {
                return Redondeador.redondear6Dec(rp.getCompraCash().doubleValue());
            }
            else if (Constantes.TOM.equals(fechaValor.trim())) {
                return Redondeador.redondear6Dec(rp.getCompraTom().doubleValue());
            }
            else if (Constantes.SPOT.equals(fechaValor)) {
                return Redondeador.redondear6Dec(rp.getCompraSpot().doubleValue());
            }
            else if (Constantes.HR72.equals(fechaValor)) {
                return Redondeador.redondear6Dec(rp.getCompra72Hr().doubleValue());
            }
            else if (Constantes.VFUT.equals(fechaValor)) {
                return Redondeador.redondear6Dec(rp.getCompraVFut().doubleValue());
            }
        }
        else {
            if (Constantes.CASH.equals(fechaValor)) {
                return Redondeador.redondear6Dec(rp.getVentaCash().doubleValue());
            }
            else if (Constantes.TOM.equals(fechaValor.trim())) {
                return Redondeador.redondear6Dec(rp.getVentaTom().doubleValue());
            }
            else if (Constantes.SPOT.equals(fechaValor)) {
                return Redondeador.redondear6Dec(rp.getVentaSpot().doubleValue());
            }
            else if (Constantes.HR72.equals(fechaValor)) {
                return Redondeador.redondear6Dec(rp.getVenta72Hr().doubleValue());
            }
            else if (Constantes.VFUT.equals(fechaValor)) {
                return Redondeador.redondear6Dec(rp.getVentaVFut().doubleValue());
            }
        }
        throw new SicaException("No se encontr\u00f3 un precio para la fecha valor: " + fechaValor);
    }

    /**
     * @see com.ixe.ods.sica.sdo.PizarronServiceData#findRenglonPizarron(TipoPizarron, String, 
     * 		String)
     */
    public RenglonPizarron findRenglonPizarron(TipoPizarron tipoPizarron, String idDivisa,
    		String idProducto)
            throws SicaException {
        List rps = getHibernateTemplate().findByNamedQuery(
        		"findRenglonPizarronByTipoPizarronDivisaProducto",
                new Object[] { tipoPizarron.getIdTipoPizarron(), idDivisa, idProducto });
        if (rps.isEmpty()) {
            throw new SicaException("No se encontr\u00f3 informaci\u00f3n del pizarr\u00f3n para "
                    + "el pizarron: " + tipoPizarron.getIdTipoPizarron().intValue() + ", divisa: "
                    + idDivisa + " y producto: "
                    + idProducto);
        }
        return (RenglonPizarron) rps.get(0);
    }

    /**
     * Permite saber si una fecha es festiva.
     *
     * @param dfs La lista de fechas no laboradas
     * @param f La fecha que se consulta
     * @return Regresa <code>true</code> si la fecha es festiva.
     */
    private boolean isFechaFestiva(List dfs, Date f) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(f);
        int diaDeLaSemana = gc.get(Calendar.DAY_OF_WEEK);
        if (diaDeLaSemana == Calendar.SATURDAY || diaDeLaSemana == Calendar.SUNDAY) {
            return true;
        }
        for (Iterator it = dfs.iterator(); it.hasNext();) {
            FechaNoLaboral df = (FechaNoLaboral) it.next();
            if (DATE_FORMAT.format(f).equals(DATE_FORMAT.format(df.getFecha()))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Obtiene el carry entre dos Fechas. Levanta SicaException si la fecha a es menor a la fecha
     * de.
     *
     * @param de La fecha desde donde se calcula
     * @param a La fecha hasta donde se calcula
     * @return int El <code>int</code> que representa al carry
     */
    private int getCarry(Date a, Date de) {
        if (a.getTime() > de.getTime()) {
            Calendar de2 = new GregorianCalendar();
            de2.setTime(de);
            int i = 0;
            for (; de2.getTime().getTime() < a.getTime(); i++) {
                de2.add(Calendar.DAY_OF_MONTH, 1);
            }
            return i;
        }
        else {
            throw new SicaException("Rango de fechas no valido.");
        }
    }

    /**
     * @param fechaCash La fecha cash de referencia.
     * @return el <code>Map</code> que contiene los carries para una fecha determinada
     * @see com.ixe.ods.sica.sdo.PizarronServiceData#getDifCarryMap(java.util.Date).
     */
    public Map getDifCarryMap(Date fechaCash) {
        List dfs = proximosDiasFestivos(fechaCash);
        Calendar gc = new GregorianCalendar();
        gc.setTime(DateUtils.inicioDia(fechaCash));
        List fechas = new ArrayList();
        for (int i = 0; i < Num.I_20 && fechas.size() < Num.I_5; i++) {
            if (!isFechaFestiva(dfs, gc.getTime())) {
                fechas.add(gc.getTime());
            }
            gc.add(Calendar.DAY_OF_MONTH, 1);
        }
        Map map = new HashMap();
        map.put(Constantes.DIF_VFUT_72, new Integer(getCarry((Date) fechas.get(Num.I_4),
                (Date) fechas.get(Num.I_3))));
        map.put(Constantes.DIF_72_SPOT, new Integer(getCarry((Date) fechas.get(Num.I_3),
                (Date) fechas.get(Num.I_2))));
        map.put(Constantes.DIF_SPOT_TOM, new Integer(getCarry((Date) fechas.get(Num.I_2),
                (Date) fechas.get(Num.I_1))));
        map.put(Constantes.DIF_TOM_CASH, new Integer(getCarry((Date) fechas.get(Num.I_1),
                (Date) fechas.get(Num.I_0))));
        return map;
    }

    /**
     * @deprecated Funcionalidad eliminada en favor de la generaci&oacute;n de pizarrones mediante el Stored Procedure
     * SC_SP_GENERAR_PIZARRONES_SICA
     * 
     * Regresa una lista con los valores que representar&aacute;n los renglones en el
     * pizarr&oacute;n.
     * 
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param spreads La lista con los valores de spreads.
     * @param vos <code>true</code> si quieres la versi&oacute;n de value objects.
     * @param insertar True para guardar los registros de RenglonPizarron.
     * @param sucursal True para sucursales, False para promoci&oacute;n mayoreo.
     * @return List Contiene la representaci&oacute;n de los renglones.
     * @throws SicaException Si algo sale mal.
     */
    private List crearRenglones(String ticket, List spreads, boolean vos, boolean insertar, 
    		boolean sucursal)
            throws SicaException {
        PrecioReferenciaActual pr = findPrecioReferenciaActual();
        Map difCarryMap = getDifCarryMap(new Date());
        pr.setDiasDifSpotTom(((Integer) difCarryMap.get("difSpotTom")).intValue());
        pr.setDiasDifTomCash(((Integer) difCarryMap.get("difTomCash")).intValue());
        pr.setDiasDif72HrSpot(((Integer) difCarryMap.get("dif72HrSpot")).intValue());
        pr.setDiasDifVFut72Hr(((Integer) difCarryMap.get("difVFut72Hr")).intValue());
        List renglones = new ArrayList();
        List fdas = findFactoresDivisaActuales();
        for (Iterator it = spreads.iterator(); it.hasNext();) {
            Spread s = (Spread) it.next();
            FactorDivisaActual fd = getFactorDivisas(fdas, s.getDivisa().getIdDivisa(),
                    s.getDivisa().getDescripcion());
            _precioReferencia = pr;
            _factorDivisa = fd;            
            double spreadSucursales = sucursal ? s.getDivisa().getSpreadSucursales() : 0;
            if (! vos) {
                RenglonPizarron rp = new RenglonPizarron();
                rp.setIdPrecioReferencia(_precioReferencia.getIdPrecioReferencia());
                rp.setPrecioReferenciaMidSpot(_precioReferencia.getPreRef().getMidSpot());
                rp.setPrecioReferenciaSpot(_precioReferencia.getPreRef().getMidSpot());
                rp.setIdTipoPizarron(s.getTipoPizarron().getIdTipoPizarron());
                rp.setIdSpread(s.getIdSpread());
                rp.setFactorDivisa(new Double(_factorDivisa.getFacDiv().getFactor()));
                rp.setIdFactorDivisa(_factorDivisa.getIdFactorDivisa());
                rp.setClaveFormaLiquidacion(s.getClaveFormaLiquidacion());
                rp.setNombreFormaLiquidacion(getDescFormaLiq(ticket, s.getClaveFormaLiquidacion(),
                        sucursal));
                rp.setIdDivisa(s.getDivisa().getIdDivisa());
                rp.setFactorDivisa(new Double(_factorDivisa.getFacDiv().getFactor()));
                double precio = getPrecioCCash();
                if (sucursal) {
                    rp.setMaximoCompraCash(new Double(precio > 0 ?
                            precio + s.getCpaVta().getCompraCash() : 0));
                }
                rp.setCompraCash(new Double(precio > 0 ?
                        precio + s.getCpaVta().getCompraCash() - spreadSucursales : 0));
                precio = getPrecioVCash();
                if (sucursal) {
                    rp.setMinimoVentaCash(new Double(precio > 0 ?
                            precio + s.getCpaVta().getVentaCash() : 0));
                }
                rp.setVentaCash(new Double(precio > 0 ? precio + s.getCpaVta().getVentaCash() +
                        spreadSucursales : 0));
                precio = getPrecioCTom();
                rp.setCompraTom(new Double(precio > 0 ? precio + s.getCpaVta().getCompraTom() -
                        spreadSucursales : 0));
                precio = getPrecioVTom();
                rp.setVentaTom(new Double(precio > 0 ? precio + s.getCpaVta().getVentaTom() +
                        spreadSucursales : 0));
                precio = getPrecioCSpot();
                rp.setCompraSpot(new Double(precio > 0 ?
                        precio + s.getCpaVta().getCompraSpot() - spreadSucursales : 0));
                precio = getPrecioVSpot();
                rp.setVentaSpot(new Double(precio > 0 ? precio + s.getCpaVta().getVentaSpot() +
                        spreadSucursales : 0));
                precio = getPrecioC72Hr();
                rp.setCompra72Hr(new Double(precio > 0 ?
                        precio + s.getCpaVta().getCompra72Hr().doubleValue() - spreadSucursales :
                        0));
                precio = getPrecioV72Hr();
                rp.setVenta72Hr(new Double(precio > 0 ?
                        precio + s.getCpaVta().getVenta72Hr().doubleValue() + spreadSucursales :
                        0));
                precio = getPrecioCVFut();
                rp.setCompraVFut(new Double(precio > 0 ?
                        precio + s.getCpaVta().getCompraVFut().doubleValue() - spreadSucursales :
                        0));
                precio = getPrecioVVFut();
                rp.setVentaVFut(new Double(precio > 0 ?
                        precio + s.getCpaVta().getVentaVFut().doubleValue() + spreadSucursales :
                        0));
                renglones.add(rp);
                if (insertar) {
                    store(rp);
                }
            }
            else {
                renglones.add(new ParametrosPizarronVO(pr.getPreRef().getPrecioSpot().doubleValue(),
                        _factorDivisa.getFacDiv().getSpreadCompra(),
                        _factorDivisa.getFacDiv().getCarry(),
                        getPrecioCCash() > 0 ? getPrecioCCash() + s.getCpaVta().getCompraCash() : 0,
                        getPrecioVCash() > 0 ? getPrecioVCash() + s.getCpaVta().getVentaCash() : 0,
                        getPrecioCTom() > 0 ? getPrecioCTom() + s.getCpaVta().getCompraTom() : 0,
                        getPrecioVTom() > 0 ? getPrecioVTom() + s.getCpaVta().getVentaTom() : 0,
                        getPrecioCSpot() > 0 ? getPrecioCSpot() + s.getCpaVta().getCompraSpot() : 0,
                        getPrecioVSpot() > 0 ? getPrecioVSpot() + s.getCpaVta().getVentaSpot() : 0,
                        getPrecioC72Hr() > 0 ? getPrecioC72Hr() +
                                s.getCpaVta().getCompra72Hr().doubleValue() : 0,
                        getPrecioV72Hr() > 0 ?
                                getPrecioV72Hr() + s.getCpaVta().getVenta72Hr().doubleValue() : 0,
                        getPrecioCVFut(),
                        getPrecioVVFut()));
            }
        }
        return renglones;
    }

    /**
     * Regresa una lista con los valores que representar&aacute;n los renglones en el
     * pizarr&oacute;n.
     * 
     * @param spreads La lista con los valores de spreads.
     * @return List Contiene la representaci&oacute;n de los renglones.
     */
    private List getRenglonesVo(List spreads) {
    	PrecioReferenciaActual pra = findPrecioReferenciaActual();
        Map difCarryMap = getDifCarryMap(new Date());
        pra.setDiasDifSpotTom(((Integer) difCarryMap.get("difSpotTom")).intValue());
        pra.setDiasDifTomCash(((Integer) difCarryMap.get("difTomCash")).intValue());
        pra.setDiasDif72HrSpot(((Integer) difCarryMap.get("dif72HrSpot")).intValue());
        pra.setDiasDifVFut72Hr(((Integer) difCarryMap.get("difVFut72Hr")).intValue());
        List renglones = new ArrayList();
        List fdas = findFactoresDivisaActuales();
        for (Iterator it = spreads.iterator(); it.hasNext();) {
            Spread s = (Spread) it.next();
            FactorDivisaActual fda = getFactorDivisas(fdas, s.getDivisa().getIdDivisa(),
                    s.getDivisa().getDescripcion());
            _precioReferencia = pra;
            _factorDivisa = fda;            
            renglones.add(new ParametrosPizarronVO(
    			pra.getPreRef().getPrecioSpot().doubleValue(),
                _factorDivisa.getFacDiv().getSpreadCompra(),
                _factorDivisa.getFacDiv().getCarry(),
                getPrecioCCash() > 0 ? getPrecioCCash() + s.getCpaVta().getCompraCash() : 0,
                getPrecioVCash() > 0 ? getPrecioVCash() + s.getCpaVta().getVentaCash() : 0,
                getPrecioCTom() > 0 ? getPrecioCTom() + s.getCpaVta().getCompraTom() : 0,
                getPrecioVTom() > 0 ? getPrecioVTom() + s.getCpaVta().getVentaTom() : 0,
                getPrecioCSpot() > 0 ? getPrecioCSpot() + s.getCpaVta().getCompraSpot() : 0,
                getPrecioVSpot() > 0 ? getPrecioVSpot() + s.getCpaVta().getVentaSpot() : 0,
                getPrecioC72Hr() > 0 ? getPrecioC72Hr() +
                        s.getCpaVta().getCompra72Hr().doubleValue() : 0,
                getPrecioV72Hr() > 0 ?
                        getPrecioV72Hr() + s.getCpaVta().getVenta72Hr().doubleValue() : 0,
                getPrecioCVFut(),
                getPrecioVVFut()
            ));
        }
        return renglones;
    }
    

    /**
     * Regresa la descripci&oacute;n del producto con la clave de forma de liquidaci&oacute;n
     * proporcionada.
     *
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param cfl La clave forma liquidaci&oacute;n.
     * @param sucursal Si es para sucursales o no.
     * @return String.
     */
    private String getDescFormaLiq(String ticket, String cfl, boolean sucursal) {
        if (sucursal) {
            if (Constantes.DOCUMENTO.equals(cfl)) {
                return "REMESA / GIRO";
            }
            else if (Constantes.MEXDOLAR.equals(cfl)) {
                return "CHEQUE DE CAJA USD";
            }
        }
        SicaSiteCache ssc = (SicaSiteCache) _appContext.getBean("sicaSiteCache");
        for (Iterator it = ssc.obtenerFormasPagoLiq(ticket).iterator(); it.hasNext();) {
            FormaPagoLiq fpl = (FormaPagoLiq) it.next();
            if (cfl.equals(fpl.getClaveFormaLiquidacion())) {
                return fpl.getNombreFormaLiquidacion();
            }
        }
        return cfl;
    }

    /**
     * @param idDivisa el id de la Divisa
     * @param claveInstrum el <code>String</code> que representa la clave Instrumento.
     * @param idMesaCambio el id de la Mesa de Cambio
     * @param idSpread	el id del Spread
     * @return el <code>Map</code> con los m&aacute;ximos y m&iacute;nimos para &eacute;sta divisa
     * @see com.ixe.ods.sica.sdo.PizarronServiceData#obtenerTipoCambioPorDivisa(java.lang.String,
            java.lang.String, int, int)
     */
    public Map obtenerTipoCambioPorDivisa(String idDivisa, String claveInstrum, int idMesaCambio,
                                          int idSpread) {
        _variacionActual = findVariacionActual();
        Map elementos = new HashMap();
        PrecioReferenciaActual pr = findPrecioReferenciaActual();
        _precioReferencia = pr;
        Divisa d = (Divisa) find(Divisa.class, idDivisa);
        double spreadOp = d.getSpreadSucursales();
        if (d.isFrecuente()) {
            Map difCarryMap = getDifCarryMap(new Date());
            pr.setDiasDifSpotTom(((Integer) difCarryMap.get("difSpotTom")).intValue());
            pr.setDiasDifTomCash(((Integer) difCarryMap.get("difTomCash")).intValue());
            _precioReferencia = pr;
            List fdas = findFactoresDivisaActuales();
            FactorDivisaActual fd = getFactorDivisas(fdas, idDivisa, " ");
            _factorDivisa = fd;
            Spread s = (Spread) find(Spread.class, new Integer(idSpread));
            if (fd != null && s != null) {
                double tCOficial = getPrecioCCash() > 0 ?
                        getPrecioCCash() + s.getCpaVta().getCompraCash() : 0;
                double tVOficial = getPrecioVCash() > 0 ?
                        getPrecioVCash() + s.getCpaVta().getVentaCash() : 0;
                elementos.put("minimoCompra",
                        new Double(Redondeador.redondear6Dec(tCOficial - spreadOp)));
                elementos.put("maximoCompra", new Double(Redondeador.redondear6Dec(tCOficial)));
                elementos.put("minimoVenta", new Double(Redondeador.redondear6Dec(tVOficial)));
                elementos.put("maximoVenta",
                        new Double(Redondeador.redondear6Dec(tVOficial + spreadOp)));
                elementos.put("idSpread", new Integer(idSpread));
                elementos.put("idFactorDivisa", new Integer(fd.getIdFactorDivisa()));
                elementos.put("idPrecioReferencia", new Integer(pr.getIdPrecioReferencia()));
                return elementos;
            }
            else {
            throw new SicaException("No est\u00e1n definidos los valores necesarios para " +
                    "realizar la consulta");
        }
        }
        else {
            List l = getHibernateTemplate().findByNamedQuery("findFactorDivisaActualCualquierOrden",
                new Object[] { Divisa.PESO, idDivisa, idDivisa, Divisa.PESO });
            if (l.size() > 0) {
                FactorDivisaActual fd = (FactorDivisaActual) l.get(0);
                double spCpra = fd.getFacDiv().getSpreadCompra();
                elementos.put("minimoCompra", new Double(Redondeador.redondear6Dec(
                        fd.getFacDiv().getFactor() - spCpra - spreadOp)));
                elementos.put("maximoCompra", new Double(Redondeador.redondear6Dec(
                        fd.getFacDiv().getFactor() - spCpra)));
                elementos.put("minimoVenta", new Double(Redondeador.redondear6Dec(
                        fd.getFacDiv().getFactor())));
                elementos.put("maximoVenta", new Double(Redondeador.redondear6Dec(
                        fd.getFacDiv().getFactor() + spreadOp)));
                elementos.put("idSpread", new Integer(idSpread));
                elementos.put("idFactorDivisa", new Integer(fd.getIdFactorDivisa()));
                elementos.put("idPrecioReferencia", new Integer(pr.getIdPrecioReferencia()));
            }
            return elementos;
        }
    }

    /**
     * @return un objeto <code>ParametrosPizarronVO</code> que contiene los renglones para el
     * pizarr&oacute;n
     *  @see com.ixe.ods.sica.sdo.PizarronServiceData#getParametrosPizarron()
     */    
    public ParametrosPizarronVO getParametrosPizarron() {
    	List spreads = findSpreadsActualesRefByTipoPizarron(
    			findCanal("PMY").getTipoPizarron().getIdTipoPizarron().intValue());
    	//List renglones = crearRenglones(null, spreads, true, false, false);
    	List renglones = getRenglonesVo(spreads);
    	if (!renglones.isEmpty()) {
    		return (ParametrosPizarronVO) renglones.get(0);
    	}
        throw new SicaException("No est\u00e1 definido el pizarr\u00f3n para la mesa 1 y el " +
                "canal PMY");
    }

    /**
     * @deprecated
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param tipoPizarron El tipo de pizarron del canal.
     * @param sucursal Si es o no para sucursales.
     * @return un <code>Map</code> con los valores de la Pizarra.
     * @see com.ixe.ods.sica.sdo.PizarronServiceData#crearPizarron(String,
     *          com.ixe.ods.sica.model.TipoPizarron, boolean)
     */
    public Map crearPizarron(String ticket, TipoPizarron tipoPizarron, boolean sucursal) {
    	/*_variacionActual = findVariacionActual();
        List spreads = findSpreadsActualesByTipoPizarron(tipoPizarron.getIdTipoPizarron());
        List renglones = crearRenglones(ticket, spreads, false, !sucursal, sucursal);*/
        Map map = new HashMap();
        /*map.put("idTipoPizarron", tipoPizarron.getIdTipoPizarron());
        map.put("fechaActualizacion", new Date());
        map.put("idPrecioReferenciaActual", new Integer(_precioReferencia.getIdPrecioReferencia()));
        map.put("vFut", isValorFuturoHabilitado() ? new Integer(1) : new Integer(0));
        map.put("renglones", renglones);*/
        return map;
    }

     /**
      * Regresa el Factor Divisa seleccionado y si para su calculo se hace lo siguiente:
      * PR * FD; PR * (1/FD).
      *
      * @param fdas La lista de factores de divisa actuales (todos).
      * @param idDivisa El String ID de la Divisa.
      * @param descripcionDivisa El String Descripcion de la Divisa.
      * @return El FactorDivisaActual buscado.
      */
    private FactorDivisaActual getFactorDivisas(List fdas, String idDivisa, String descripcionDivisa) {
         for (Iterator it = fdas.iterator(); it.hasNext();) {
             FactorDivisaActual fd = (FactorDivisaActual) it.next();
             if (Divisa.DOLAR.equals(fd.getFacDiv().getToDivisa().getIdDivisa()) &&
                     idDivisa.equals(fd.getFacDiv().getFromDivisa().getIdDivisa())) {
                 return fd;
             }
             else if (Divisa.DOLAR.equals(fd.getFacDiv().getFromDivisa().getIdDivisa()) &&
                     idDivisa.equals(fd.getFacDiv().getToDivisa().getIdDivisa())) {
                 return fd;
             }
        }
        throw new SicaException("No est\u00e1 configurado el Factor Divisa para " +
                descripcionDivisa + " (" + idDivisa + ")");
    }
    
    /**
     * Obtiene el valor inverso de un n&uacte;mero.
     * 
     * @param valor El valor num&eacute;rico.
     * @return double
     */
    private double getInverso(double valor) {
        return Redondeador.redondear6Dec(1.0 / valor);
    }

    /**
     * Regresa el valor del factor dividido si asi aplica.
     *
     * @param compra Si se desea para compra o venta.
     * @return double el <code>double</code> con el valor del factor.
     */
    private Map factorDivisaCorrecto(boolean compra) {
        Map resultado = new HashMap();
        resultado.put("spreadCompra", new Double(0));
        resultado.put("spreadReferencia",
                new Double(_factorDivisa.getFacDiv().getSpreadReferencia()));
        double factor = 0;
        // Si es dolar dolar, regresa directamente el factor (1) pues el spread de compra ya fue
        // asignado:
        if (Divisa.DOLAR.equals(_factorDivisa.getFacDiv().getFromDivisa().getIdDivisa()) &&
                Divisa.DOLAR.equals(_factorDivisa.getFacDiv().getToDivisa().getIdDivisa())) {
            factor =  _factorDivisa.getFacDiv().getFactor();
            resultado.put("spreadCompra", new Double(_factorDivisa.getFacDiv().getSpreadCompra()));
        }
        else {
            boolean seDivide = _factorDivisa.getFacDiv().getFromDivisa().isDivide() ||
                    _factorDivisa.getFacDiv().getToDivisa().isDivide();
            if (isFactoresAutomaticos()) {
                Divisa div = _factorDivisa.getOtraDivisaNoUsdNoMxn();
                if (Divisa.DOLAR_CANADIENSE.equals(div.getIdDivisa())) {
                    if (seDivide) {
                        if (compra) {
                            factor = getInverso(_variacionActual.getFactorCadCompra() +
                                    _factorDivisa.getFacDiv().getSpreadCompra() / 2.0);
                        }
                        else {
                            factor = getInverso(_variacionActual.getFactorCadVenta() -
                                    _factorDivisa.getFacDiv().getSpreadCompra() / 2.0);
                        }
                    }
                    else {
                        if (compra) {
                            factor = Redondeador.redondear6Dec(
                                    _variacionActual.getFactorCadCompra() -
                                            _factorDivisa.getFacDiv().getSpreadCompra() / 2.0);
                        }
                        else {
                            factor = Redondeador.redondear6Dec(
                                    _variacionActual.getFactorCadVenta() +
                                            _factorDivisa.getFacDiv().getSpreadCompra() / 2.0);
                        }
                    }
                }
                else if (Divisa.EURO.equals(div.getIdDivisa())) {
                    if (seDivide) {
                        if (compra) {
                            factor = getInverso(_variacionActual.getFactorEurCompra() +
                                    _factorDivisa.getFacDiv().getSpreadCompra() / 2.0);
                        }
                        else {
                            factor = getInverso(_variacionActual.getFactorEurVenta() -
                                    _factorDivisa.getFacDiv().getSpreadCompra() / 2.0);
                        }
                    }
                    else {
                        if (compra) {
                            factor = Redondeador.redondear6Dec(
                                    _variacionActual.getFactorEurCompra() -
                                            _factorDivisa.getFacDiv().getSpreadCompra() / 2.0);
                        }
                        else {
                            factor = Redondeador.redondear6Dec(
                                    _variacionActual.getFactorEurVenta() +
                                            _factorDivisa.getFacDiv().getSpreadCompra() / 2.0);
                        }
                    }
                }
                else if (Divisa.LIBRA_ESTERLINA.equals(div.getIdDivisa())) {
                    if (seDivide) {
                        if (compra) {
                            factor = getInverso(_variacionActual.getFactorGbpCompra() +
                                    _factorDivisa.getFacDiv().getSpreadCompra() / 2.0);
                        }
                        else {
                            factor = getInverso(_variacionActual.getFactorGbpVenta() -
                                    _factorDivisa.getFacDiv().getSpreadCompra() / 2.0);
                        }
                    }
                    else {
                        if (compra) {
                            factor = Redondeador.redondear6Dec(
                                    _variacionActual.getFactorGbpCompra() -
                                            _factorDivisa.getFacDiv().getSpreadCompra() / 2.0);
                        }
                        else {
                            factor = Redondeador.redondear6Dec(
                                    _variacionActual.getFactorGbpVenta() +
                                            _factorDivisa.getFacDiv().getSpreadCompra() / 2.0);
                        }
                    }
                }
                else if (Divisa.FRANCO_SUIZO.equals(div.getIdDivisa())) {
                    if (seDivide) {
                        if (compra) {
                            factor = getInverso(_variacionActual.getFactorChfCompra() +
                                    _factorDivisa.getFacDiv().getSpreadCompra() / 2.0);
                        }
                        else {
                            factor = getInverso(_variacionActual.getFactorChfVenta() -
                                    _factorDivisa.getFacDiv().getSpreadCompra() / 2.0);
                        }
                    }
                    else {
                        if (compra) {
                            factor = Redondeador.redondear6Dec(
                                    _variacionActual.getFactorChfCompra() -
                                            _factorDivisa.getFacDiv().getSpreadCompra() / 2.0);
                        }
                        else {
                            factor = Redondeador.redondear6Dec(
                                    _variacionActual.getFactorChfVenta() +
                                            _factorDivisa.getFacDiv().getSpreadCompra() / 2.0);
                        }
                    }
                }
                else if (Divisa.YEN.equals(div.getIdDivisa())) {
                    if (seDivide) {
                        if (compra) {
                            factor = getInverso(_variacionActual.getFactorJpyCompra() +
                                    _factorDivisa.getFacDiv().getSpreadCompra() / 2.0);
                        }
                        else {
                            factor = getInverso(_variacionActual.getFactorJpyVenta() -
                                    _factorDivisa.getFacDiv().getSpreadCompra() / 2.0);
                        }
                    }
                    else {
                        if (compra) {
                            factor = Redondeador.redondear6Dec(
                                    _variacionActual.getFactorJpyCompra() -
                                            _factorDivisa.getFacDiv().getSpreadCompra() / 2.0);
                        }
                        else {
                            factor = Redondeador.redondear6Dec(
                                    _variacionActual.getFactorJpyVenta() +
                                            _factorDivisa.getFacDiv().getSpreadCompra() / 2.0);
                        }
                    }
                }
            }
            else {
                if (seDivide) {
                    if (compra) {
                        factor = getInverso(getInverso(_factorDivisa.getFacDiv().getFactor()) +
                                _factorDivisa.getFacDiv().getSpreadCompra() / 2.0);
                    }
                    else {
                        factor = getInverso(getInverso(_factorDivisa.getFacDiv().getFactor()) -
                                _factorDivisa.getFacDiv().getSpreadCompra() / 2.0);
                    }
                }
                else {
                    if (compra) {
                        factor = Redondeador.redondear6Dec(_factorDivisa.getFacDiv().getFactor() -
                                _factorDivisa.getFacDiv().getSpreadCompra() / 2.0);
                    }
                    else {
                        factor = Redondeador.redondear6Dec(_factorDivisa.getFacDiv().getFactor() +
                                _factorDivisa.getFacDiv().getSpreadCompra() / 2.0);
                    }
                }
            }
        }
        resultado.put("factor", new Double(factor));
        return resultado;
    }

    /**
     * Regresa precioVCash - spreadCompra.
     *
     * @return double El Precio Compra Cash.
     */
    public double getPrecioCCash() {
        if (getPrecioCTom() > 0) {
            return getPrecioCTom() - (_factorDivisa.getFacDiv().getCarry() *
                    _precioReferencia.getDiasDifTomCash());
        }
        else {
            return 0;
        }
    }

    /**
     * Regresa precioVTom - (carry * diasDifTomCash).
     *
     * @return double El Precio Venta Cash.
     */
    public double getPrecioVCash() {
        if (getPrecioVTom() > 0) {
            return getPrecioVTom() - (_factorDivisa.getFacDiv().getCarry() *
                    _precioReferencia.getDiasDifTomCash());
        }
        else {
            return 0;
        }
    }

    /**
     * Regresa precioVTom - spreadCompra.
     *
     * @return double El Precio Compra Tom.
     */
    public double getPrecioCTom() {
        if (getPrecioCSpot() > 0) {
            return getPrecioCSpot() - (_factorDivisa.getFacDiv().getCarry() *
                    _precioReferencia.getDiasDifSpotTom());
        }
        else {
            return 0;
        }
    }

    /**
     * Regresa precioVSpot - (carry * _diasDifSpotTom).
     *
     * @return double El Precio Venta Tom.
     */
    public double getPrecioVTom() {
        if (getPrecioVSpot() > 0) {
            return getPrecioVSpot() - (_factorDivisa.getFacDiv().getCarry() *
                    _precioReferencia.getDiasDifSpotTom());
        }
        else {
            return 0;
        }
    }

    /**
     * Regresa el precioVSpot - spreadCompra.
     *
     * @return double El Precio Compra Spot.
     */
    public double getPrecioCSpot() {
        Map mapaFactor = factorDivisaCorrecto(COMPRA);
        double factor = ((Double) mapaFactor.get("factor")).doubleValue();
        double spreadCompra = ((Double) mapaFactor.get("spreadCompra")).doubleValue();
        double spreadReferencia = ((Double) mapaFactor.get("spreadReferencia")).doubleValue();
        if (_precioReferencia.isActualizacionAutomatica() || _precioReferencia.isMidSpot()) {
            return (_precioReferencia.getPreRef().getPrecioCompra().doubleValue() * factor) +
                    spreadReferencia;
        }
        else if (_precioReferencia.isActualizacionManual()) {
            return ((_precioReferencia.getPreRef().getPrecioSpot().doubleValue() * factor) -
                    spreadCompra) + spreadReferencia;
        }
        else if (_precioReferencia.isVespertino()) {
            return ((_precioReferencia.getPreRef().getPrecioSpot().doubleValue() -
                    Double.parseDouble(((ParametroSica) find(ParametroSica.class,
                            ParametroSica.SPREAD_VESPERTINO)).getValor())) * factor) +
                    spreadReferencia;
        }
        throw new SicaException("No est\u00e1 definido el precio de compra spot " +
        		"para el m\u00e9todo de actualizaci\u00f3n " + 
        		_precioReferencia.getPreRef().getMetodoActualizacion());
    }

    /**
     * Regresa precioSpot + spreadReferencia.
     *
     * @return double El Precio Venta Spot.
     */
    public double getPrecioVSpot() {
        Map mapaFactor = factorDivisaCorrecto(VENTA);
        double factor = ((Double) mapaFactor.get("factor")).doubleValue();
        double spreadReferencia = ((Double) mapaFactor.get("spreadReferencia")).doubleValue();
        if (_precioReferencia.isActualizacionAutomatica() || _precioReferencia.isMidSpot()) {
            return (_precioReferencia.getPreRef().getPrecioVenta().doubleValue() * factor) +
                    spreadReferencia;
        }
        else if (_precioReferencia.isActualizacionManual()) {
            return (_precioReferencia.getPreRef().getPrecioSpot().doubleValue() * factor) +
                    spreadReferencia;
        }
        else if (_precioReferencia.isVespertino()) {
            return (_precioReferencia.getPreRef().getPrecioSpot().doubleValue() * factor) +
                    spreadReferencia;
        }
        throw new SicaException("No est\u00e1 definido el precio de compra spot para el " +
        		"m\u00e9todo de actualizaci\u00f3n " + 
        		_precioReferencia.getPreRef().getMetodoActualizacion());
    }

    /**
     * Calcula el precio de compra fecha Valor 72Hr a partir del carry y el precio Referencia.
     *
     * @return un <code>double</code>  que representa el valor calculado
     */
    public double getPrecioC72Hr() {
        if (getPrecioCSpot() > 0) {
            return getPrecioCSpot() + (_factorDivisa.getFacDiv().getCarry() *
                    _precioReferencia.getDiasDif72HrSpot());
        }
        else {
            return 0;
        }
    }

    /**
     * Calcula el precio de venta fecha Valor 72Hr a partir del carry y el precio Referencia.
     *
     * @return un <code>double</code>  que representa el valor calculado
     */
    public double getPrecioV72Hr() {
        if (getPrecioVSpot() > 0) {
            return getPrecioVSpot() + (_factorDivisa.getFacDiv().getCarry() *
                    _precioReferencia.getDiasDif72HrSpot());
        }
        else {
            return 0;
        }
    }

    /**
     * Calcula el precio de compra fecha Valor vFut a partir del carry y el precio Referencia.
     *
     * @return un <code>double</code>  que representa el valor calculado
     */
    public double getPrecioCVFut() {
        if (getPrecioC72Hr() > 0) {
            return getPrecioC72Hr() + (_factorDivisa.getFacDiv().getCarry() *
                    _precioReferencia.getDiasDifVFut72Hr());
        }
        else {
            return 0;
        }

    }

    /**
     * Calcula el precio de venta fecha Valor vFut a partir del carry y el precio Referencia.
     *
     * @return un <code>double</code>  que representa el valor calculado
     */
    public double getPrecioVVFut() {
        if (getPrecioV72Hr() > 0) {
            return getPrecioV72Hr() + (_factorDivisa.getFacDiv().getCarry() *
                    _precioReferencia.getDiasDifVFut72Hr());
        }
        else {
            return 0;
        }
    }
        
    /**
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param tipoPizarron El tipo de pizarron del canal.
     * @param sucursales Si es o no para sucursales.
     * @return List.
     * @see com.ixe.ods.sica.sdo.PizarronServiceData#getRenglonesPizarron(String, Integer, boolean)
     */
    public List getRenglonesPizarron(String ticket, Integer tipoPizarron, boolean sucursales) {
    	//if (!sucursales) {		// No se usa la bandera de sucursales
    		List rengs = getHibernateTemplate().
                    findByNamedQuery("findRenglonesPizarronTipoPizarron", tipoPizarron);
    		Collections.sort(rengs, new Comparator() {
    			public int compare(Object rp1, Object rp2) {
    				String cfl1 = ((RenglonPizarron) rp1).getClaveFormaLiquidacion();
    				String cfl2 = ((RenglonPizarron) rp2).getClaveFormaLiquidacion();
    				if (cfl1.equals(cfl2)) {
    					return 0;
    				}
    				else if (Constantes.TRANSFERENCIA.equals(cfl1) ||
    						(Constantes.DOCUMENTO.equals(cfl1) && !esAlgunoDe(cfl2,
                                    new String[]{Constantes.TRANSFERENCIA})) ||
                            (Constantes.MEXDOLAR.equals(cfl1) && !esAlgunoDe(cfl2, new String[]{
                                    Constantes.TRANSFERENCIA, Constantes.DOCUMENTO})) ||
                            (Constantes.TRAVELER_CHECKS.equals(cfl1) &&
                                    !esAlgunoDe(cfl2, new String[]{Constantes.TRANSFERENCIA,
                                            Constantes.DOCUMENTO, Constantes.MEXDOLAR}))) {
    					return -1;
    				}
    				return 1;
    			}
    		});
    		return rengs;
    	//}							// No se usa la bandera de sucursales
    	/*else {
            Map mapa = crearPizarron(ticket, (TipoPizarron) find(TipoPizarron.class, tipoPizarron),
                    sucursales);
            return (List) mapa.get("renglones");
        }*/
    }
 
    /**
     * Regresa true si claveFormaLiq se encuentra en el arreglo cves.
     *
     * @param claveFormaLiq La clave forma de liquidaci&oacute;n.
     * @param cves El arreglo de claves.
     * @return boolean.
     */
    private boolean esAlgunoDe(String claveFormaLiq, String[] cves) {
        for (int i = 0; i < cves.length; i++) {
            if (claveFormaLiq.equals(cves[i])) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Llama al stored procedure encargado en actualizar los pizarrones SC_SP_GENERAR_PIZARRONES_SICA
     */
    private void callSpGenerarPizarrones(String ticket, String ip, IUsuario usuario) {
    	try {
			getHibernateTemplate().execute(new HibernateCallback() {
				public Object doInHibernate(Session session) throws HibernateException, SQLException {
					int index = 0;
					CallableStatement cs = session.connection().prepareCall(
						"{ call SICA_ADMIN.SC_SP_GENERAR_PIZARRONES_SICA(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) }"
					);
					cs.setInt(index + 1, 2); // 2 = Identificador del origen de la llamada para SICA
					cs.setDate(index + 2, null);
					cs.setFloat(index + 3, (float)0.0);
					cs.setFloat(index + 4, (float)0.0);
					cs.setFloat(index + 5, (float)0.0);
					cs.setFloat(index + 6, (float)0.0);
					cs.setFloat(index + 7, (float)0.0);
					cs.setFloat(index + 8, (float)0.0);
					cs.setFloat(index + 9, (float)0.0);
					cs.setFloat(index + 10, (float)0.0);
					cs.setFloat(index + 11, (float)0.0);
					cs.setFloat(index + 12, (float)0.0);
					cs.setFloat(index + 13, (float)0.0);
					cs.setFloat(index + 14, (float)0.0);
					cs.setFloat(index + 15, (float)0.0);
					cs.executeUpdate();
					return null;
				}
			});
		} catch (DataAccessException e) {
			if (logger.isWarnEnabled()) {
                logger.warn(e);
            }
            throw new SicaException("No fue posible refrescar el pizarr\u00f3n, por favor " +
                    "intente de nuevo la operaci\u00f3n");
		}
    	 
    	auditar(ticket, ip, new Integer (0), usuario, "Feed de Datos", "BITACORA", "INFO", "E");
    	
    }
    
    /**
     * @see com.ixe.ods.sica.sdo.PizarronServiceData#refrescarPizarrones(String).
     */
    public synchronized void refrescarPizarrones(String ticket, String ip, IUsuario usuario) {
    	callSpGenerarPizarrones(ticket, ip, usuario);
    }
   
    /**
     * @see com.ixe.ods.sica.sdo.PizarronServiceData#refrescarPizarron(String, com.ixe.ods.sica.model.TipoPizarron)
     */
    public synchronized void refrescarPizarron(String ticket, TipoPizarron tipoPizaron, String ip, IUsuario usuario) {
    	callSpGenerarPizarrones(ticket, ip, usuario);
    }
    
    /**
     * Regresa las Divisas no frecuentes activas.
     *
     * @param metales Si es para metales.
     * @return List de Divisas.
     */
    public List findDivisasNoFrecuentesPizarron(boolean metales){
    	List resultados = new ArrayList();
    	
    	List factores = getHibernateTemplate().find("SELECT fda FROM FactorDivisaActual as fda, " +
                "WHERE fda.facDiv.fromDivisa = ? AND fda.facDiv.toDivisa.tipo = ? " +
                "ORDER BY fda.facDiv.toDivisa.idDivisa",
                new Object[]{Divisa.PESO, metales ? Divisa.METAL_AMONEDADO : Divisa.NO_FRECUENTE});
        for (Iterator it = factores.iterator(); it.hasNext();) {
            FactorDivisaActual factorDivisa = (FactorDivisaActual) it.next();
            resultados.add(factorDivisa.getFacDiv().getToDivisa());
        }
        return resultados;
    }

    /**
     * @param metales	<code>true</code> si es para metales amonedados
     * @return Una <code>List</code> con los resultados para pintar el pizarr&oacute;n
     * @see com.ixe.ods.sica.sdo.PizarronServiceData#obtenerPizarronOtrasDivisas(boolean)
     */
    public List obtenerPizarronOtrasDivisas(boolean metales) {
        List resultados = new ArrayList();
        Map fechasCarry = getDifCarryMap(getFechaOperacion());
        List factores = getHibernateTemplate().find("SELECT fda FROM FactorDivisaActual AS fda " +
                "WHERE fda.facDiv.fromDivisa = ? AND fda.facDiv.toDivisa.tipo = ? " +
                "ORDER BY fda.facDiv.toDivisa.idDivisa",
                new Object[]{Divisa.PESO, metales ? Divisa.METAL_AMONEDADO : Divisa.NO_FRECUENTE});
        PrecioReferenciaActual pr = findPrecioReferenciaActual();
        for (Iterator it = factores.iterator(); it.hasNext();) {
            FactorDivisaActual factorDivisa = (FactorDivisaActual) it.next();
            Map mapa = new HashMap();
            if (metales) {
                mapa.put("id", factorDivisa.getFacDiv().getToDivisa().getIdDivisa());
                mapa.put("descripcion", factorDivisa.getFacDiv().getToDivisa().getDescripcion());
                mapa.put("compraSPOT", new Double(Redondeador.redondear6Dec(
                        factorDivisa.getFacDiv().getFactor() -
                                factorDivisa.getFacDiv().getSpreadCompra())));
                mapa.put("ventaSPOT", new Double(Redondeador.redondear6Dec(factorDivisa.getFacDiv().
                        getFactor())));
                mapa.put("compraTOM", new Double(Redondeador.redondear6Dec(factorDivisa.getFacDiv().
                        getFactor() - factorDivisa.getFacDiv().getSpreadCompra() -
                        factorDivisa.getFacDiv().getCarry() *
                                ((Integer) fechasCarry.get(Constantes.DIF_TOM_CASH)).intValue())));
                mapa.put("ventaTOM", new Double(Redondeador.redondear6Dec(factorDivisa.getFacDiv().
                        getFactor() - factorDivisa.getFacDiv().getCarry() *
                        ((Integer) fechasCarry.get(Constantes.DIF_TOM_CASH)).intValue())));
                mapa.put("compraCash", new Double(Redondeador.redondear6Dec(
                        factorDivisa.getFacDiv().getFactor() -
                                factorDivisa.getFacDiv().getSpreadCompra() -
                        factorDivisa.getFacDiv().getCarry() *
                                (((Integer) fechasCarry.get(Constantes.DIF_TOM_CASH)).intValue() -
                                        ((Integer) fechasCarry.get(Constantes.DIF_SPOT_TOM)).
                                                intValue()))));
                mapa.put("ventaCash", new Double(Redondeador.redondear6Dec(factorDivisa.getFacDiv().
                        getFactor() - factorDivisa.getFacDiv().getCarry() *
                        (((Integer) fechasCarry.get(Constantes.DIF_TOM_CASH)).intValue() -
                                ((Integer) fechasCarry.get(Constantes.DIF_SPOT_TOM)).intValue()))));
                mapa.put("factorDivisa", new Double(factorDivisa.getFacDiv().getFactor()));
                mapa.put("precioReferenciaMidSpot", pr.getPreRef().getMidSpot());
                mapa.put("precioReferenciaSpot", pr.getPreRef().getPrecioSpot());
                mapa.put("idFactorDivisa", new Integer(factorDivisa.getIdFactorDivisa()));
                mapa.put("idPrecioReferencia", new Integer(pr.getIdPrecioReferencia()));
                resultados.add(mapa);
            }
            else {
                mapa.put("id", factorDivisa.getFacDiv().getToDivisa().getIdDivisa());
                mapa.put("descripcion", factorDivisa.getFacDiv().getToDivisa().getDescripcion());
                mapa.put("compra", new Double(Redondeador.redondear6Dec(factorDivisa.getFacDiv().
                        getFactor() - factorDivisa.getFacDiv().getSpreadCompra())));
                mapa.put("venta", new Double(Redondeador.redondear6Dec(factorDivisa.getFacDiv().
                        getFactor())));
                mapa.put("factorDivisa", new Double(factorDivisa.getFacDiv().getFactor()));
                mapa.put("precioReferenciaMidSpot", pr.getPreRef().getMidSpot());
                mapa.put("precioReferenciaSpot", pr.getPreRef().getPrecioSpot());
                mapa.put("idFactorDivisa", new Integer(factorDivisa.getIdFactorDivisa()));
                mapa.put("idPrecioReferencia", new Integer(pr.getIdPrecioReferencia()));
                resultados.add(mapa);
            }
        }
        return resultados;
    }

    /**
     * @return un objeto <code>Date</code> con la fecha actual.
     * @see com.ixe.ods.sica.sdo.PizarronServiceData#getFechaOperacion().
     */
    public Date getFechaOperacion() {
        return getFechaOperacion(new Date());
    }

    /**
     * @param fechaCash La fecha cash de referencia.
     * @return un objeto <code>Date</code> con la fecha cash.
     * @see com.ixe.ods.sica.sdo.PizarronServiceData#getFechaOperacion(java.util.Date).
     */
    public Date getFechaOperacion(Date fechaCash) {
        return DateUtils.inicioDia(fechaCash);
    }

    /**
     * @return Date La fecha TOM del sistema.
     * @see com.ixe.ods.sica.sdo.PizarronServiceData#getFechaTOM().
     */
    public Date getFechaTOM() {
        return getFechaTOM(getFechaOperacion());
    }

    /**
     * @param fechaCash La fecha cash de referencia.
     * @return Date La fecha TOM del sistema.
     * @see com.ixe.ods.sica.sdo.PizarronServiceData#getFechaTOM(java.util.Date).
     */
    public Date getFechaTOM(Date fechaCash) {
        try {
            Calendar gc = new GregorianCalendar();
            gc.setTime(DateUtils.inicioDia(fechaCash));
            Map map = getDifCarryMap(fechaCash);
            gc.add(Calendar.DAY_OF_MONTH, Integer.parseInt(map.get("difTomCash").toString()));
            return gc.getTime();
        }
        catch (SicaException e) {
            if (logger.isDebugEnabled()) {
                logger.debug(e);
            }
            return null;
        }
    }

    /**
     * @return Date La fecha SPOT del sistema.
     * @see com.ixe.ods.sica.sdo.PizarronServiceData#getFechaSPOT().
     */
    public Date getFechaSPOT() {
        return getFechaSPOT(new Date());
    }

    /**
     * @param fechaCash La fecha cash de referencia.
     * @return Date La fecha SPOT del sistema.
     * @see com.ixe.ods.sica.sdo.PizarronServiceData#getFechaSPOT(java.util.Date).
     */
    public Date getFechaSPOT(Date fechaCash) {
        try {
            Calendar gc = new GregorianCalendar();
            gc.setTime(com.ixe.ods.sica.utils.DateUtils.inicioDia(fechaCash));
            Map map = getDifCarryMap(fechaCash);
            gc.add(Calendar.DAY_OF_MONTH, Integer.parseInt(map.get("difTomCash").toString()) +
                    Integer.parseInt(map.get("difSpotTom").toString()));
            return gc.getTime();
        }
        catch (SicaException e) {
            if (logger.isDebugEnabled()) {
                logger.debug(e);
            }
            return null;
        }
    }

    /**
     * @param fechaCash La fecha cash de referencia
     * @return Date
     * @see com.ixe.ods.sica.sdo.PizarronServiceData#getFechaSPOTSiar(Date).
     */
    public Date getFechaSPOTSiar(Date fechaCash) {
    	List dfs = proximosDiasFestivos(fechaCash);
        try {
            Calendar gc = new GregorianCalendar();
            gc.setTime(com.ixe.ods.sica.utils.DateUtils.inicioDia(fechaCash));
            Map map = getDifCarryMap(fechaCash);
            gc.add(Calendar.DAY_OF_MONTH, Integer.parseInt(map.get("difTomCash").toString()) +
                    Integer.parseInt(map.get("difSpotTom").toString()));
            if (getValorFuturoService().esFestivaEnEua(gc.getTime()) || 
            		isFechaFestiva(dfs, gc.getTime())) {
            	gc.add(Calendar.DAY_OF_MONTH, 1);
            }
            return gc.getTime();
        }
        catch (SicaException e) {
            if (logger.isDebugEnabled()) {
                logger.debug(e);
            }
            return null;
        }
    }

    /**
     * @return Date La fecha 72HR del sistema.
     * @see com.ixe.ods.sica.sdo.PizarronServiceData#getFecha72HR().
     */
    public Date getFecha72HR() {
        return getFecha72HR(new Date());
    }

    /**
     * @param fechaCash La fecha cash de referencia.
     * @return Date La fecha 72HR del sistema.
     * @see com.ixe.ods.sica.sdo.PizarronServiceData#getFecha72HR(java.util.Date).
     */
    public Date getFecha72HR(Date fechaCash) {
        try {
            Calendar gc = new GregorianCalendar();
            gc.setTime(com.ixe.ods.sica.utils.DateUtils.inicioDia(fechaCash));
            Map map = getDifCarryMap(fechaCash);
            gc.add(Calendar.DAY_OF_MONTH, Integer.parseInt(map.get("difTomCash").toString()) +
                    Integer.parseInt(map.get("difSpotTom").toString()) +
                    Integer.parseInt(map.get("dif72HrSpot").toString()));
            return gc.getTime();
        }
        catch (SicaException e) {
            if (logger.isDebugEnabled()) {
                logger.debug(e);
            }
            return null;
        }
    }

    /**
     * @return Date La fecha VFUT del sistema.
     * @see com.ixe.ods.sica.sdo.PizarronServiceData#getFechaVFUT().
     */
    public Date getFechaVFUT() {
        return getFechaVFUT(new Date());
    }

    /**
     * @param fechaCash La fecha cash de referencia.
     * @return Date La fecha VFUT del sistema.
     * @see com.ixe.ods.sica.sdo.PizarronServiceData#getFechaVFUT(java.util.Date).
     */
    public Date getFechaVFUT(Date fechaCash) {
        try {
            Calendar gc = new GregorianCalendar();
            gc.setTime(com.ixe.ods.sica.utils.DateUtils.inicioDia(fechaCash));
            Map map = getDifCarryMap(fechaCash);
            gc.add(Calendar.DAY_OF_MONTH, Integer.parseInt(map.get("difTomCash").toString()) +
                    Integer.parseInt(map.get("difSpotTom").toString()) +
                    Integer.parseInt(map.get("dif72HrSpot").toString()) +
                    Integer.parseInt(map.get("difVFut72Hr").toString()));
            return gc.getTime();
        }
        catch (SicaException e) {
            if (logger.isDebugEnabled()) {
                logger.debug(e);
            }
            return null;
        }
    }
    
    /**
     * Otiene la variaci&oacute;n acutal de los tipos de cambio.
     * 
     * @return Variacion
     */
    public Variacion findVariacionActual() {
        List variaciones = getHibernateTemplate().find("FROM Variacion AS v WHERE v.idVariacion " +
                "= (SELECT max(v2.idVariacion) FROM Variacion AS v2)");
        return (Variacion) (variaciones.isEmpty() ? null : variaciones.get(0));
    }
    
    /**
     * Define si los factores se encuentran en modo autom&aacute;tico.
     * 
     * @return boolean
     */
    private boolean isFactoresAutomaticos() {
        ParametroSica p = (ParametroSica) find(ParametroSica.class,
                ParametroSica.FACTOR_AUTOMATICO);
        return p != null && "S".equals(p.getValor());
    }

    /**
     * @return true;
     * @see com.ixe.ods.sica.sdo.PizarronServiceData#isValorFuturoHabilitado().
     */
    public boolean isValorFuturoHabilitado() {
    	return getValorFuturoService().esFestivaEnEua(getFechaTOM()) ||
                getValorFuturoService().esFestivaEnEua(getFechaSPOT()) ||
                getValorFuturoService().esFestivaEnEua(getFecha72HR());
    }

    /**
     * Regresa la referencia al valorFuturoService.
     *
     * @return ValorFuturoService.
     */
    private ValorFuturoService getValorFuturoService() {
        return (ValorFuturoService) _appContext.getBean("valorFuturoService");
    }

    /**
     * Regresa la fecha valor que corresponde a la base de la fecha cash proporcionada. Levanta
     * SicaException Si no se puede calcular por ser una fecha muy anterior.
     *
     * @param fechaCash La fecha de referencia.
     * @param regresarCashEnError Si se desea que regrese 'CASH' si no se encuentra la fecha valor.
     * @return String.
     */
    private String fechaValorActualConRespectoAFechaCash(Date fechaCash,
    		boolean regresarCashEnError) {
        Date fechaActual = DateUtils.inicioDia();
        if (fechaActual.equals(getFechaOperacion(fechaCash))) {
            return Constantes.CASH;
        }
        if (fechaActual.equals(getFechaTOM(fechaCash))) {
            return Constantes.TOM;
        }
        else if (fechaActual.equals(getFechaSPOT(fechaCash))) {
            return Constantes.SPOT;
        }
        else if (fechaActual.equals(getFecha72HR(fechaCash))) {
            return Constantes.HR72;
        }
        else if (fechaActual.equals(getFechaVFUT(fechaCash))) {
            return Constantes.VFUT;
        }
        if (regresarCashEnError) {
            return Constantes.CASH;
        }
        else {
            throw new SicaFechaValorException("No se puede calcular la fecha valor " +
                    "correspondiente.");
        }
    }

    /**
     * @param fechaCaptura La fecha de captura del deal.
     * @param fechaValorCaptura La fecha valor que tiene el deal.
     * @param regresarCashEnError Si debe regresar 'CASH' si no se puede calcular, o levantar una
     *          excepci&oacute;n.
     * @return String.
     * @see com.ixe.ods.sica.sdo.PizarronServiceData#fechaValorParaCancelacion(java.util.Date,
     *          String, boolean).
     */
    public String fechaValorParaCancelacion(Date fechaCaptura, String fechaValorCaptura,
                                            boolean regresarCashEnError) {
        String[] fechasValor = new String[]{Constantes.CASH, Constantes.TOM, Constantes.SPOT,
                Constantes.HR72, Constantes.VFUT};
        String fechaValorActual = fechaValorActualConRespectoAFechaCash(fechaCaptura,
                regresarCashEnError);
        int indice = 0;
        for (int i = 0; i < fechasValor.length; i++) {
            if (fechasValor[i].equals(fechaValorCaptura.trim())) {
                indice = i;
                break;
            }
        }
        int restar = Num.I_10;
        if (Constantes.CASH.equals(fechaValorActual)) {
            restar = Num.I_0;
        }
        else if (Constantes.TOM.equals(fechaValorActual)) {
            restar = Num.I_1;
        }
        else if (Constantes.SPOT.equals(fechaValorActual)) {
            restar = Num.I_2;
        }
        else if (Constantes.HR72.equals(fechaValorActual)) {
            restar = Num.I_3;
        }
        else if (Constantes.VFUT.equals(fechaValorActual)) {
            restar = Num.I_4;
        }
        try {
            return fechasValor[indice - restar];
        }
        catch (ArrayIndexOutOfBoundsException e) {
            if (regresarCashEnError) {
                return Constantes.CASH;
            }
            else {
                throw new SicaFechaValorException("No se puede calcular la fecha valor " +
                        "correspondiente.");
            }
        }
    }

    /**
     * Calcula la fecha valor adecuada con respecto a una fecha de captura y liquidaci&oacute;n
     * determinadas.
     *
     * @param fechaCaptura     La fecha de captura para un deal.
     * @param fechaLiquidacion La fecha de liquidaci&oacute;n de un deal.
     * @return String.
     */
    public String calcularFechaValorParaFechasCapturaLiquidacion(Date fechaCaptura,
                                                                  Date fechaLiquidacion) {
        fechaCaptura = DateUtils.inicioDia(fechaCaptura);
        fechaLiquidacion = DateUtils.inicioDia(fechaLiquidacion);
        if (fechaCaptura.equals(fechaLiquidacion)) {
            return Constantes.CASH;
        }
        else if (fechaLiquidacion.equals(getFechaTOM())) {
            return Constantes.TOM;
        }
        else if (fechaLiquidacion.equals(getFechaSPOT())) {
            return Constantes.SPOT;
        }
        else if (fechaLiquidacion.equals(getFecha72HR())) {
            return Constantes.HR72;
        }
        else if (fechaLiquidacion.equals(getFechaVFUT())) {
            return Constantes.VFUT;
        }
        throw new SicaFechaValorException("No se puede calcular la fecha valor para la fecha de " +
                "captura " + Constantes.DATE_FORMAT.format(fechaCaptura) + " y fecha de " +
                "liquidaci\u00f3n " + Constantes.DATE_FORMAT.format(fechaLiquidacion));
    }


    /**
     * Encuentra los pr&oacute;ximos d&iacute;as festivos a partir de la fecha cash y hasta una
     * semana posterior.
     *
     * @param fechaCash La fecha Cash de referencia.
     * @return List.
     */
    private List proximosDiasFestivos(Date fechaCash) {
        Calendar gc = new GregorianCalendar();
        gc.setTime(fechaCash);
        gc.add(Calendar.DAY_OF_WEEK, Num.I_7);
        return getHibernateTemplate().find("FROM FechaNoLaboral AS df WHERE df.fecha BETWEEN ? " +
                "AND ? ORDER BY df.fecha", new Object[]{fechaCash, gc.getTime()});
    }

    /**
     * @param canal El canal de operacion.
     * @param idDivisa El id de la divisa
     * @param claveFormaFormaLiquidacion La forma de liquidacion (efectivo.).
     * @return Map
     * @see PizarronServiceData#obtenerTipoDeCambioPorDivisa(com.ixe.ods.sica.model.Canal , String,
     * String).
     */
    public Map obtenerTipoDeCambioPorDivisa(Canal canal, String idDivisa,
    		String claveFormaFormaLiquidacion) {
    	try {
    		if (StringUtils.isEmpty(idDivisa) || StringUtils.isEmpty(claveFormaFormaLiquidacion)) {
    			throw new RuntimeException("Favor de proporcionar el idDivisa, " +
    			"claveFormaFormaLiquidacion , idCanal");
    		}
    		if (canal == null) {
    			throw new RuntimeException("No se pudo crear el deal debido a que no se " +
                        "encontr\u00f3 un canal para la sucursal.");
    		}
    		Divisa d = (Divisa)find(Divisa.class, idDivisa);
    		if (d.isFrecuente()) {
    			List spreads = getSicaServiceData().
    				findSpreadsActualesByTipoPizarronDivisaFormaLiquidacion(idDivisa,
    					claveFormaFormaLiquidacion, canal.getTipoPizarron());
    			if (!spreads.isEmpty()) {
    				SpreadActual sp = (SpreadActual) spreads.get(0);
    				return obtenerTipoCambioPorDivisa(idDivisa, claveFormaFormaLiquidacion,
    						canal.getMesaCambio().getIdMesaCambio(), sp.getIdSpread());
    			}
    		}
    		else {
    			return obtenerTipoCambioPorDivisa(idDivisa, claveFormaFormaLiquidacion,
    					canal.getMesaCambio().getIdMesaCambio(), 0);
    		}
    		throw new RuntimeException("No se encontr\u00f3 el spread actual para la divisa " +
    				idDivisa);
    	}
    	catch (Exception e) {
    		e.printStackTrace();
    		throw new RuntimeException(e.getMessage(), e);
    	}
    }
    
    /**
	 * Obtiene una referencia al Bean de Servicios de Seguridad.
	 *
	 * @return SeguridadServiceData El Bean de Servicios de Seguridad.
	 */
	private SicaServiceData getSicaServiceData() {
		return (SicaServiceData) _appContext.getBean("sicaServiceData");
	}
	
    /**
     * El precio de referencia (No existe relaci&oacute;n en la base de datos).
     */
    private PrecioReferenciaActual _precioReferencia;

    /**
     * El registro de SICA_VARIACION m&aacute;s reciente.
     */
    private Variacion _variacionActual;

    /**
     * El factor de divisa (No existe relaci&oacute;n en la base de datos).
     */
    private FactorDivisaActual _factorDivisa = new FactorDivisaActual();

    /**
     * Constante para el valor de COMPRA
     */
    private static final boolean COMPRA = true;

    /**
     * Constante para el valor de VENTA
     */
    private static final boolean VENTA = false;

    /**
     * Constante para formatear las fechas de la forma yyyyMMdd.
     */
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");
}
