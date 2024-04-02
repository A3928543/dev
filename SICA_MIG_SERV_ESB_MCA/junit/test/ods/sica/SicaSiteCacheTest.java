package test.ods.sica;

import com.ixe.ods.sica.SicaSiteCache;
import com.ixe.treasury.dom.common.FormaPagoLiq;
import junit.framework.TestCase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Iterator;
import java.util.List;

/**
 * @author Jean C. Favila
 * @version $Revision: 1.12 $ $Date: 2008/02/22 18:25:44 $
 */
public class SicaSiteCacheTest extends TestCase {

    /**
     * Inicializa el application context.
     *
     * @throws Exception Si ocurre alg&uacute;n error.
     */
	protected void setUp() throws Exception {
        super.setUp();
        String[] paths = {"applicationContext.xml"};
        _applicationContext = new ClassPathXmlApplicationContext(paths);
        _sicaSiteCache = (SicaSiteCache) _applicationContext.getBean("sicaSiteCache");
    }

	/**
     * Limpia los recursos ocupados.
     *
     * @throws Exception Si ocurre alg&uacute;n error.
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testObtenerFormasPagoLiq() {
        List fpls = _sicaSiteCache.obtenerFormasPagoLiq(_ticket);
        assertFalse(fpls.isEmpty());
        for (Iterator it = fpls.iterator(); it.hasNext();) {
            FormaPagoLiq fpl = (FormaPagoLiq) it.next();
            System.out.println(fpl.getClaveFormaLiquidacion() + " " + fpl.getMnemonico() + " " + fpl.getIdDivisa() + " " +
                fpl.getMontoMinimo() + " " + fpl.getMultiplo() + " " + fpl.getNombreFormaLiquidacion());
        }
    }

    /**
     * El objeto para logging.
     */
    protected final Log _logger = LogFactory.getLog(getClass());

    /**
     * El contexto de la aplicaci&oacute;n.
     */
    private ApplicationContext _applicationContext;

    /**
     * Instancia de SicaSiteCache.
     */
    private SicaSiteCache _sicaSiteCache;

    /**
     * Constante para el valor del ticket de la sesi&oacute;n.
     */
    private String _ticket = "402897c006f1a12f01070155a8bf0180";
    
}
