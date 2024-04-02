/*
 * $Id: DescuadrePosicionTest.java,v 1.2 2008/02/22 18:25:44 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2008 LegoSoft S.C.
 */
package test.ods.sica;

import com.ixe.ods.sica.dao.DescuadreDao;
import java.util.Iterator;
import java.util.List;
import junit.framework.TestCase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Pruebas de unidad para el DAO de Detecci&oacute;n de Descuadre de la Posici&oacute;n.
 *
 * @author Gustavo Gonzalez (ggonzalez)
 * @version $Revision: 1.2 $ $Date: 2008/02/22 18:25:44 $
 */
public class DescuadrePosicionTest extends TestCase {

	/**
	 * Constructor por default.
	 */
	public DescuadrePosicionTest() {
		super();
	}

	/**
     * Inicializa el application context.
     *
     * @throws Exception Si no se puede inicializar el application context de spring.
     */
    protected void setUp() throws Exception {
        super.setUp();
        String[] paths = {"applicationContext.xml"};
        _applicationContext = new ClassPathXmlApplicationContext(paths);
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
     * Prueba que obtiene los deals no cancelados que afectan la posicion.
     */
    public void testDealsNoCancelados() {
    	try {
    		DescuadreDao dao = (DescuadreDao) _applicationContext.getBean("descuadreDao");
    		List dealsNoCancelados = dao.findDescuadreDetallesDealsNoCancelados();
    		logger.info("Verificando Deals No Cancelados ...");
    		logger.info("Elementos en la lista: " + dealsNoCancelados.size());
    		for (Iterator it = dealsNoCancelados.iterator(); it.hasNext();) {
    			Object [] registro = (Object[]) it.next();
    			logger.info("Id Deal: " + ((Integer) registro[0]).intValue());
    		}
		}
        catch (Exception e) {
			logger.info(e);
            fail(e.getMessage());
		}
    }

    /**
     * Prueba que obtiene los deals cancelados que afectan la posicion.
     */
    public void testDealsCancelados() {
    	try {
    		DescuadreDao dao = (DescuadreDao) _applicationContext.getBean("descuadreDao");
    		List dealsCancelados = dao.findDescuadreDetallesDealsCancelados();
    		logger.info("Verificando Deals Cancelados ...");
    		logger.info("Elementos en la lista: " + dealsCancelados.size());
    		for (Iterator it = dealsCancelados.iterator(); it.hasNext();) {
    			Object [] registro = (Object[]) it.next();
    			logger.info("Id Deal: " + ((Integer) registro[0]).intValue());
    		}
		}
        catch (Exception e) {
			logger.info(e);
            fail(e.getMessage());
		}
    }

    /**
     * El objeto para logging.
     */
    private final Log logger = LogFactory.getLog(getClass());

    /**
     * El contexto de la aplicaci&oacute;n.
     */
    private ApplicationContext _applicationContext;

    /**
     * Constante para el valor del ticket de la sesi&oacute;n.
     */
    private String ticket = "402897c006f1a12f01070155a8bf0180";
}
