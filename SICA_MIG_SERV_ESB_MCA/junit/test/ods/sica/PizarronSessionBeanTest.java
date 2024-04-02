/*
 * $Id: PizarronSessionBeanTest.java,v 1.12 2008/02/22 18:25:43 ccovian Exp $
 * Copyright (c) 2006
 */

package test.ods.sica;

import com.ixe.ods.sica.SicaPizarronSession;
import com.ixe.ods.sica.SicaPizarronSessionHome;
import junit.framework.TestCase;

import javax.naming.Context;
import javax.naming.InitialContext;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Pruebas de unidad para el EJB SicaPizarronSessionBean.
 *
 * @author Jean C. Favila
 * @version $Revision: 1.12 $ $Date: 2008/02/22 18:25:43 $
 */
public class PizarronSessionBeanTest extends TestCase {

    /**
     * Inicializa el application context.
     *
     * @throws Exception
     */
    protected void setUp() throws Exception {
        super.setUp();
        Properties ps = new Properties();
        ps.put(InitialContext.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
        ps.put(InitialContext.PROVIDER_URL, "t3://localhost:7001");
        System.out.println(ps);
        Context ctx;
        try {
            ctx = new InitialContext(ps);
            SicaPizarronSessionHome home = (SicaPizarronSessionHome) ctx.lookup("ejb/sica/SicaPizarronHome");
            _ejb = home.create();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
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
     * Prueba el servicio getDivisasFrecuentes.
     *
     * @see com.ixe.ods.sica.SicaPizarronSession#getDivisasFrecuentes(String).
     */
    public void testGetDivisasFrecuentes() {
        try {
            List divisas = _ejb.getDivisasFrecuentes(_ticket);
            assertTrue("No se encontraron divisas.", divisas.size() > 0);
        }
        catch (RemoteException e) {
            fail("Error de conexi\u00f3n con el EJB");
        }
    }

    /**
     * Prueba el servicio getFormasPagoLiq.
     *
     * @see com.ixe.ods.sica.SicaPizarronSession#getFormasPagoLiq(String).
     */
    public void testGetFormasPagoLiq() {
        try {
            List fpls = _ejb.getFormasPagoLiq(_ticket);
            assertTrue("No se encontraron formas de pago y liquidaci\u00f3n.", fpls.size() > 0);
        }
        catch (RemoteException e) {
            fail("Error de conexi\u00f3n con el EJB");
        }
    }

    /**
     * Prueba el servicio crearPizarron.
     *
     * @see com.ixe.ods.sica.SicaPizarronSession#crearPizarron(String, String).
     */
    public void testCrearPizarron() {
        try {
            Map pizarron = _ejb.crearPizarron(_ticket, "PMY");
            assertTrue("No se cre\u00f3 el pizarr\u00f3n", ((List) pizarron.get("renglones")).size() > 0);
        }
        catch (Exception e) {
            fail("Error de conexi\u00f3n con el EJB");
        }
    }

    /**
     * Prueba el servicio obtenerPizarronOtrasDivisas para Divisas no frecuentes.
     *
     * @see com.ixe.ods.sica.SicaPizarronSession#obtenerPizarronOtrasDivisas(boolean).
     */
    public void testDivisasNoFrecuentes() {
        try {
            List divisas = _ejb.obtenerPizarronOtrasDivisas(false);
            assertTrue("No se encontraron divisas no frecuentes.", divisas.size() > 0);
        }
        catch (RemoteException e) {
            fail("Error de conexi\u00f3n con el EJB");
        }
    }

    /**
     * Prueba el servicio obtenerPizarronOtrasDivisas para Metales amonedados.
     *
     * @see com.ixe.ods.sica.SicaPizarronSession#obtenerPizarronOtrasDivisas(boolean).
     */
    public void testMetales() {
        try {
            List divisas = _ejb.obtenerPizarronOtrasDivisas(true);
            assertTrue("No se encontraron metales amonedados.", divisas.size() > 0);
        }
        catch (RemoteException e) {
            fail("Error de conexi\u00f3n con el EJB");
        }
    }

    /**
     * Instancia del EJB SicaPizarronSession.
     */
    private SicaPizarronSession _ejb;

    /**
     * Constante para el valor del ticket de la sesi&oacute;n
     */
    private String _ticket = "402897c006f1a12f01070155a8bf0180";
}
