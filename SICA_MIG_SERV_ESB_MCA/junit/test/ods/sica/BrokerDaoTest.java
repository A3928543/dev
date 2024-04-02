/*
 * $Id: BrokerDaoTest.java,v 1.1 2009/09/23 16:59:00 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2009 LegoSoft S.C.
 */

package test.ods.sica;

import com.ixe.ods.sica.dao.BrokerDao;
import com.ixe.ods.sica.vo.BrokerVO;

import java.util.Iterator;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import junit.framework.TestCase;

/**
 * Pruebas Unitarias del bean BrokerDao.
 *
 * @author Jean C. Favila (jfavila)
 * @version $Revision: 1.1 $ $Date: 2009/09/23 16:59:00 $
 */
public class BrokerDaoTest extends TestCase {

    /**
     * Inicializa el application context.
     *
     * @throws Exception Si ocurre alg&uacute;n error.
     */
	protected void setUp() throws Exception {
        super.setUp();
        String[] paths = {"junit/jdbcDaoApplicationContext.xml"};
        ApplicationContext appContext = new FileSystemXmlApplicationContext(paths);
        brokerDao = (BrokerDao) appContext.getBean("brokerDao");
    }

    /**
     * Realiza la prueba del servicio getBrokersVOParaOperarSwaps. La prueba es exitosa si la lista
     * de BrokersVO no est&aacute; vac&iacute;a.
     *
     * @see com.ixe.ods.sica.dao.BrokerDao#getBrokersVOParaOperarSwaps().
     */
    public void testGetBrokersVOParaOperarSwaps() {
        List brokersVO = brokerDao.getBrokersVOParaOperarSwaps();
        assertFalse(brokersVO.isEmpty());
        for (Iterator it = brokersVO.iterator(); it.hasNext();) {
            BrokerVO brokerVO = (BrokerVO) it.next();
            logger.info(brokerVO);
        }
    }

    /**
     * El objeto para logging.
     */
    private final Log logger = LogFactory.getLog(getClass());

    /**
     * Referencia al bean brokerDao del ApplicationContext.
     */
    private BrokerDao brokerDao;
}
