/*
 * $Id: LineasCambioServiceDataTest.java,v 1.3 2008/12/26 23:17:28 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2008 LegoSoft S.C.
 */
package test.ods.sica;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.ixe.ods.sica.model.LineaCambio;
import com.ixe.ods.sica.model.LineaCambioLog;
import com.ixe.ods.sica.sdo.LineaCambioServiceData;

import junit.framework.TestCase;

/**
 * @author Jean C. Favila
 * @version $Revision: 1.3 $ $Date: 2008/12/26 23:17:28 $
 */
public class LineasCambioServiceDataTest extends TestCase {

    /**
     * Constructor por default.
     */
    public LineasCambioServiceDataTest() {
        super();
    }

    /**
     * Inicializa el application context.
     *
     * @throws Exception Si ocurre alg&uacute;n error.
     */
	protected void setUp() throws Exception {
        super.setUp();
        String[] paths = {"applicationContext.xml"};
        appContext = new ClassPathXmlApplicationContext(paths);
        sdo = (LineaCambioServiceData) appContext.getBean("lineasCambioServiceData");
    }

    public void testFindDealsLineaCambioCliente() {
        LineaCambio lc = sdo.findLineaCambioParaCliente(new Integer(694713));
        List deals = sdo.findDealsLineaCambioCliente("", new Integer(694713));
        for (Iterator it = deals.iterator(); it.hasNext();) {
            Map deal = (Map) it.next();
            System.out.println(deal);
        }
    }

    public void testFindUsosParaDeal() {
        List usos = sdo.findUsosParaDeal(135269);
        for (Iterator it = usos.iterator(); it.hasNext();) {
            LineaCambioLog lcl = (LineaCambioLog) it.next();
            System.out.println(lcl);
        }

    }

    /**
     * El objeto para logging.
     */
    private final Log logger = LogFactory.getLog(getClass());

    /**
     * El contexto de la aplicaci&oacute;n.
     */
    private ApplicationContext appContext;

    /**
     * Instancia de LineaCambioServiceData.
     */
    private LineaCambioServiceData sdo;
    
}
