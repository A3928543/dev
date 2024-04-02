/*
 * $Id: FakePizarronServiceData.java,v 1.3 2010/02/26 01:01:08 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2009 - 2010 LegoSoft S.C.
 */

package test.ods.sica.impostores;

import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.model.Canal;
import com.ixe.ods.sica.model.PrecioReferencia;
import com.ixe.ods.sica.model.TipoPizarron;
import com.ixe.ods.sica.model.Variacion;
import com.ixe.ods.sica.posicion.vo.ParametrosPizarronVO;
import com.ixe.ods.sica.sdo.PizarronServiceData;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

/**
 * Implementaci&oacute;n de prueba para el bean PizarronServiceData.
 * 
 * @author Jean C. Favila (jfavila)
 * @version $Revision: 1.3 $ $Date: 2010/02/26 01:01:08 $
 */
public class FakePizarronServiceData implements PizarronServiceData {
    
    public double findPrecioPizarronPesos(String idCanal, String idDivisa, String idProducto,
                                          boolean recibimos, String fechaValor)
            throws SicaException {
        return 0;
    }

    public double findPrecioPizarronPesos(TipoPizarron tipoPizarron, String idDivisa,
                                          String idProducto, boolean recibimos, String fechaValor)
            throws SicaException {
        return 0;
    }

    public PrecioReferencia findPrecioReferenciaActual() {
        return null;
    }

    public ParametrosPizarronVO getParametrosPizarron() throws SicaException {
        return null;
    }

    public Map crearPizarron(String ticket, TipoPizarron tipoPizarron, boolean sucursal)
            throws SicaException {
        return null;
    }

    public Map getDifCarryMap(Date fechaCash) throws SicaException {
        return null;
    }

    public List getRenglonesPizarron(String ticket, Integer tipoPizarron, boolean sucursales)
            throws SicaException {
        return null;
    }

    public void refrescarPizarrones(String ticket) throws SicaException {

    }

    public void refrescarPizarron(String ticket, TipoPizarron tipoPizarron) throws SicaException {

    }

    public Map obtenerTipoCambioPorDivisa(String idDivisa, String claveInstrum, int idMesaCambio,
                                          int idSpread) throws SicaException {
        return null;
    }

    public List obtenerPizarronOtrasDivisas(boolean metales) {
        return null;
    }

    public Date getFechaOperacion() {
        return new Date();
    }

    public Date getFechaOperacion(Date fechaCash) {
        return null;
    }

    public Date getFechaTOM() {
        return crearFecha(3);
    }

    public Date getFechaTOM(Date fechaCash) {
        return null;
    }

    public Date getFechaSPOT() {
        return crearFecha(4);
    }

    public Date getFechaSPOT(Date fechaCash) {
        return null;
    }

    public Date getFechaSPOTSiar(Date fechaCash) {
        return null;
    }

    public Date getFecha72HR() {
        return crearFecha(5);
    }

    public Date getFecha72HR(Date fechaCash) {
        return null;
    }

    public Date getFechaVFUT() {
        return crearFecha(6);
    }

    public Date getFechaVFUT(Date fechaCash) {
        return null;
    }

    public boolean isValorFuturoHabilitado() {
        return false;
    }

    public Variacion findVariacionActual() {
        return null;
    }

    public String fechaValorParaCancelacion(Date fechaCaptura, String fechaValorCaptura,
                                            boolean regresarCashEnError) throws SicaException {
        return null;
    }

    public String calcularFechaValorParaFechasCapturaLiquidacion(Date fechaCaptura,
                                                                 Date fechaLiquidacion) {
        return null;
    }

    public Map obtenerTipoDeCambioPorDivisa(Canal canal, String idDivisa, 
                                            String claveFormaFormaLiquidacion) {
        return null;
    }

    public List findDivisasNoFrecuentesPizarron(boolean metales) {
        return null;
    }

    private Date crearFecha(int dias) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.add(Calendar.DAY_OF_WEEK, dias);
        return gc.getTime();
    }
}
