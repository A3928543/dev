/*
 * $Id: DealDaoTest.java,v 1.1 2009/09/25 15:56:13 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2009 LegoSoft S.C.
 */

package test.ods.sica;

import com.ixe.ods.sica.dao.DealDao;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * Pruebas unitarias del bean DealDao.
 * 
 * @author Jean C. Favila (jfavila)
 * @version $Revision: 1.1 $ $Date: 2009/09/25 15:56:13 $
 */
public class DealDaoTest extends TestCase {

    /**
     * Constructor por default.
     */
    public DealDaoTest() {
        super();
    }

    /**
     * Inicializa el application context.
     *
     * @throws Exception Si ocurre alg&uacute;n error.
     */
	protected void setUp() throws Exception {
        super.setUp();
        String[] paths = {"junit/jdbcDaoApplicationContext.xml"};
        ApplicationContext appContext = new FileSystemXmlApplicationContext(paths);
        dealDao = (DealDao) appContext.getBean("dealDao");
    }

    /**
     * Prueba del servicio findDeals por un n&uacute;mero de deal en particular.
     */
    public void testFindDealsById() {
        List deals = dealDao.findDeals(false, null, null, null, null, null, new Integer(200000),
                null, null, null, null, null, null);
        for (Iterator it = deals.iterator(); it.hasNext();) {
            logger.info(it.next());
        }
    }

    /**
     * Prueba del servicio findDeals por un rango de fechas de captura.
     */
    public void testFindDealsByFechaOperacion() {
        GregorianCalendar gc = new GregorianCalendar();
        gc.set(Calendar.DAY_OF_MONTH, 10);
        gc.set(Calendar.MONTH, 8);
        gc.set(Calendar.YEAR, 2007);

        GregorianCalendar gc2 = new GregorianCalendar();
        gc2.set(Calendar.DAY_OF_MONTH, 10);
        gc2.set(Calendar.MONTH, 8);
        gc2.set(Calendar.YEAR, 2007);
        List deals = dealDao.findDeals(false, gc.getTime(), gc2.getTime(), null, null, null, null,
                null, null, null, null, null, null);
        for (Iterator it = deals.iterator(); it.hasNext();) {
            logger.info(it.next());
        }
    }

    /**
     * Prueba del servicio findDeals por un rango de fechas de liquidaci&oacute;n.
     */
    public void testFindDealsByFechaLiquidacion() {
        GregorianCalendar gc = new GregorianCalendar();
        gc.set(Calendar.DAY_OF_MONTH, 10);
        gc.set(Calendar.MONTH, 8);
        gc.set(Calendar.YEAR, 2007);

        GregorianCalendar gc2 = new GregorianCalendar();
        gc2.set(Calendar.DAY_OF_MONTH, 10);
        gc2.set(Calendar.MONTH, 8);
        gc2.set(Calendar.YEAR, 2007);
        List deals = dealDao.findDeals(false, null, null, gc.getTime(), gc2.getTime(), null, null,
                null, null, null, null, null, null);
        for (Iterator it = deals.iterator(); it.hasNext();) {
            logger.info(it.next());
        }
    }

    /**
     * Prueba del servicio findDeals por un canal en particular.
     */
    public void testFindDealsByIdCanal() {
        List deals = dealDao.findDeals(false, null, null, null, null, null, null,
                null, null, null, null, "MTY", null);
        for (Iterator it = deals.iterator(); it.hasNext();) {
            logger.info(it.next());
        }
    }

    /**
     * Regresa el valor de dealDao.
     *
     * @return DealDao.
     */
    public DealDao getDealDao() {
        return dealDao;
    }

    /**
     * Establece el valor de dealDao.
     *
     * @param dealDao El valor a asignar.
     */
    public void setDealDao(DealDao dealDao) {
        this.dealDao = dealDao;
    }

    /**
     * El objeto para logging.
     */
    private final Log logger = LogFactory.getLog(getClass());

    /**
     * Referencia al bean <code>dealDao</code> del applicationContext.
     */
    private DealDao dealDao;
}
