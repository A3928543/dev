/*
 * $Id: PizarronServiceDataTest.java,v 1.19 2008/10/27 23:20:09 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2007 - 2008 LegoSoft S.C.
 */

package test.ods.sica;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.ixe.ods.sica.Constantes;
import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.model.Deal;
import com.ixe.ods.sica.model.Divisa;
import com.ixe.ods.sica.model.PrecioReferencia;
import com.ixe.ods.sica.model.RenglonPizarron;
import com.ixe.ods.sica.model.SpreadActual;
import com.ixe.ods.sica.model.TipoPizarron;
import com.ixe.ods.sica.model.Variacion;
import com.ixe.ods.sica.sdo.PizarronServiceData;
import com.ixe.ods.sica.sdo.SicaServiceData;

import junit.framework.TestCase;

/**
 * TestCase para probar los servicios de PizarronServiceData.
 *
 * @author Jean C. Favila
 * @version $Revision: 1.19 $ $Date: 2008/10/27 23:20:09 $
 */
public class PizarronServiceDataTest extends TestCase {

    /**
     * Constructor por default.
     */
    public PizarronServiceDataTest() {
        super();
    }

    /**
	 * Inicia el applicationContext.
	 * 
	 * @throws Exception Si ocurre alg&uacute;n error.
	 */
    protected void setUp() throws Exception {
        super.setUp();
        String[] paths = {"applicationContext.xml"};
        appContext = new ClassPathXmlApplicationContext(paths);
        sdo = (PizarronServiceData) appContext.getBean("pizarronServiceData");
    }
    
    /**
     * Limpia los recursos ocupados.
     *
     * @throws Exception Si ocurre alg&uacute;n error.
     */
    protected void tearDown() throws Exception {
    	super.tearDown();
    }
    
    /**
     * Prueba el servicio obtenerTipoCambioPorDivisa.
     *
     * @see com.ixe.ods.sica.sdo.PizarronServiceData#obtenerTipoCambioPorDivisa(String, String,
     *          int, int).
     */
    public void testObtenerTipoCambioPorDivisa() {
        try {
            SicaServiceData ssd = (SicaServiceData) appContext.getBean("sicaServiceData");
            TipoPizarron tp = new TipoPizarron();
            tp.setIdTipoPizarron(new Integer(1));
            List spreadsActuales = ssd.findSpreadsActualesByTipoPizarronDivisaFormaLiquidacion(
                Divisa.DOLAR_CANADIENSE, Constantes.TRANSFERENCIA, tp);
            for (Iterator it = spreadsActuales.iterator(); it.hasNext();) {
                SpreadActual spreadActual = (SpreadActual) it.next();
                logger.info(sdo.obtenerTipoCambioPorDivisa(
                    Divisa.DOLAR_CANADIENSE, Constantes.TRANSFERENCIA, 1,
                    spreadActual.getIdSpread()));

            }
        }
        catch (SicaException e) {
            fail(e.getMessage()); 
        }
    }

    /**
     * Prueba el servicio findVariacionActual.
     *
     * @see com.ixe.ods.sica.sdo.PizarronServiceData#findVariacionActual().
     */
    public void testPrecioReferenciaActual() {
        PrecioReferencia pr = sdo.findPrecioReferenciaActual();
        if (pr == null) {
            fail("No se encontr\u00f3 el precio de referencia Actual");
        }
        else {
            logger.info("El precio de referencia es el " + pr.getIdPrecioReferencia());
        }
    }

    /**
     * Prueba el servicio findVariacionActual.
     *
     * @see com.ixe.ods.sica.sdo.PizarronServiceData#findVariacionActual().
     */
    public void testFindVariacionActual() {
        Variacion var = sdo.findVariacionActual();
        if (var == null) {
            fail("No se encontr\u00f3 el regritro de SICA_VARIACION");
        }
    }

    /**
     * Prueba los servicios getFechaOperacion(), getFechaTom(), getFechaSpot(), getFecha72HR() y
     * getFechaVFUT().
     */
    public void testGetFechasValor() {
        GregorianCalendar gc = new GregorianCalendar(2007, 2, 28, 10, 20, 29);
        Date fechaCash = gc.getTime();
        logger.info("CASH: " + sdo.getFechaOperacion(fechaCash));
        logger.info("TOM: " + sdo.getFechaTOM(fechaCash));
        logger.info("SPOT: " + sdo.getFechaSPOT(fechaCash));
        logger.info("72HR: " + sdo.getFecha72HR(fechaCash));
        logger.info("96HR: " + sdo.getFechaVFUT(fechaCash));
    }

    /**
     * Prueba del servicio getFechaValorParaCancelacion.
     *
     * @see PizarronServiceData#fechaValorParaCancelacion(java.util.Date, String, boolean).
     */
    public void testFechaValorParaCancelacion() {
        try {
            GregorianCalendar diasAtras = new GregorianCalendar();
            diasAtras.add(Calendar.DAY_OF_MONTH, - 6);
            logger.info(diasAtras.getTime() + " VFUT a " + sdo.fechaValorParaCancelacion(
                    diasAtras.getTime(), Deal.VFUT, false));
            logger.info(diasAtras.getTime() + " 72HR a " + sdo.fechaValorParaCancelacion(
                    diasAtras.getTime(), Deal.HR72, false));
            logger.info(diasAtras.getTime() + " SPOT a " + sdo.fechaValorParaCancelacion(
                    diasAtras.getTime(), Deal.SPOT, false));
            logger.info(diasAtras.getTime() + " TOM a " + sdo.fechaValorParaCancelacion(
                    diasAtras.getTime(), Deal.TOM, false));
            logger.info(diasAtras.getTime() + " CASH a " + sdo.fechaValorParaCancelacion(
                    diasAtras.getTime(), Deal.CASH, false));
        }
        catch (SicaException e) {
            fail(e.getMessage());
        }
    }

    /**
     * Prueba del servicio getRenglonesPizarron().
     *
     * @see com.ixe.ods.sica.sdo.PizarronServiceData#getRenglonesPizarron(String, Integer, boolean).
     */
    public void testGetRenglonesPizarron() {
        try {
            List renglones = sdo.getRenglonesPizarron(ticket, new Integer(1), false);
            assertFalse("No se encontraron renglones para el pizarr\u00f3n: ", renglones.isEmpty());
            for (Iterator it = renglones.iterator(); it.hasNext();) {
                RenglonPizarron rp = (RenglonPizarron) it.next();
                logger.info(rp);
            }
        }
        catch (SicaException e) {
            fail(e.getMessage());
        }
    }

    /**
     * Preuba el servicio findPrecioPizarronPesos.
     *
     * @see com.ixe.ods.sica.sdo.PizarronServiceData#findPrecioPizarronPesos(String, String, String,
            boolean, String).
     */
    public void testFindPrecioPizarronPesos() {
        try {
            logger.info("" + sdo.findPrecioPizarronPesos("PMY", "USD", "TRAEXT", false, Deal.CASH));
        }
        catch (SicaException e) {
            fail(e.getMessage());
        }
    }

    /**
     * El objeto para logging.
     */
    private final Log logger = LogFactory.getLog(getClass());

    /**
     * Instancia del application context de spring.
     */
    private ApplicationContext appContext;
        
    /**
     * Instancia de los servicios del pizarr&oacute;n.
     */
    private PizarronServiceData sdo;

    /**
     * Constante para el valor del ticket de la sesi&oacute;n
     */
    private String ticket = "402897c006f1a12f01070155a8bf0180";
}
