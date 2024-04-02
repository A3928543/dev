package test.ods.sica;

import com.ixe.ods.sica.sdo.WorkFlowServiceData;
import junit.framework.TestCase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Jean C. Favila
 * @version $Revision: 1.11.42.1 $ $Date: 2012/03/13 02:28:41 $
 */
public class WorkFlowServiceDataTest extends TestCase {

    /**
     * Inicializa el application context.
     *
     * @throws Exception Si ocurre alg&uacute;n error.
     */
    protected void setUp() throws Exception {
        super.setUp();
        String[] paths = {"applicationContext.xml"};
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext(paths);
        _sdo = (WorkFlowServiceData) applicationContext.getBean("sicaServiceData");
    }

    /**
     * Limpia los recursos ocupados.
     *
     * @throws Exception Si ocurre alg&uacute;n error.
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }
    
    public void testPagoAnticipado() {
    	logger.info("incio del test");
    }

    /**
     * El objeto para logging.
     */
    protected final Log logger = LogFactory.getLog(getClass());

    /**
     * Instancia de WorkFlowServiceData
     */
    private WorkFlowServiceData _sdo;

    /**
     * Constante para el valor del ticket de la sesi&oacute;n.
     */
    private String _ticket = "402897c006f1a12f01070155a8bf0180";
    
}
