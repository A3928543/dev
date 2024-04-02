/*
 * $Id: SicaSessionBeanTest.java,v 1.4 2008/02/22 18:25:43 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 LegoSoft S.C.
 */

package test.ods.sica;

import com.ixe.ods.sica.SicaSession;
import com.ixe.ods.sica.SicaSessionHome;
import java.util.Properties;
import javax.naming.Context;
import javax.naming.InitialContext;
import junit.framework.TestCase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Gustavo Gonzalez
 * @version  $Revision: 1.4 $ $Date: 2008/02/22 18:25:43 $
 */
public class SicaSessionBeanTest extends TestCase {

	/**
	 * Levanta el contexto para la prueba.
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
            SicaSessionHome home = (SicaSessionHome) ctx.lookup("ejb/sica/SicaHome");
            ejb = home.create();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Prueba de unidad del EJB para el Sistema de Boveda. Calcula el Tipo de Cambio promedio para
     * un mes de una divisa.
     */
    public void testObtenerTcpDivisaMesAnterior() {
    	try {
    		_logger.info("Tipo de cambio mes anterior: "
                    + ejb.obtenerTcpDivisaMesAnterior(_ticket, new Integer(10), new Integer(2)).doubleValue());
		}
        catch (Exception e) {
			fail(e.getMessage());
		}
    }

    /**
     * Prueba unitaria del servicio revisarRespuestaBanxicoParaDeal().
     */
    public void testRevisarRespuestaBanxicoParaDeal() {
        try {
            ejb.revisarRespuestaBanxicoParaDeal("25400-1", "USD");
        }
        catch (Exception e) {
            fail(e.getMessage());
        }
    }
 
    /**
     * Instancia del EJB del SICA.
     */
    private SicaSession ejb;

    /**
     * El objeto para logging.
     */
    protected final Log _logger = LogFactory.getLog(getClass());

    /**
     * Constante para el valor del ticket de la sesi&oacute;n.
     */
    private String _ticket = "402897c006f1a12f01070155a8bf0180"; 

}
