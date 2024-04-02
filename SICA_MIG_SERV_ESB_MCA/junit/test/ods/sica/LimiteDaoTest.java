/*
 * $Id: LimiteDaoTest.java,v 1.4 2008/12/26 23:17:28 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2008 LegoSoft S.C.
 */

package test.ods.sica;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.ixe.ods.sica.dao.LimiteDao;
import com.ixe.ods.sica.model.Divisa;
import com.ixe.ods.sica.model.Limite;

import junit.framework.TestCase;

/**
 * Prueba unitaria de los servicios de la clase LimiteDao.
 *
 * @author Jean C. Favila
 * @version $Revision: 1.4 $ $Date: 2008/12/26 23:17:28 $
 * @see LimiteDao
 */
public class LimiteDaoTest extends TestCase {

    /**
     * Constructor por default.
     */
    public LimiteDaoTest() {
        super();
    }

    /**
     * Inicializa el limiteDao a partir del application context. 
     *
     * @throws Exception Si ocurre alg&uacute;n error.
     */
    protected void setUp() throws Exception {
        super.setUp();
        String[] paths = {"applicationContext.xml"};
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext(paths);
        limiteDao = (LimiteDao) applicationContext.getBean("limiteDao");
    }

    /**
     * Prueba el servicio testFindLimiteDeRiesgoByMesaDivisa(), pasando null en ambos parametros.
     *
     * @see LimiteDao#findLimiteDeRiesgoByMesaDivisa(Integer, String).
     */
    public void testFindLimiteDeRiesgoByMesaDivisa() {
        List limites = limiteDao.findLimiteDeRiesgoByMesaDivisa(null, null);
        for (Iterator it = limites.iterator(); it.hasNext();) {
            Limite lim = (Limite) it.next();
            logger.info(lim.getTipoLimite().getNombre() + TAB + lim.getLimite());            
        }
    }

    /**
     * Prueba el servicio getLimitesParaMesaDivisa(), pasando null en ambos parametros.
     *
     * @see LimiteDao#getLimitesParaMesaDivisa(Integer, String, Integer).
     */
    public void testSinMesaSinDivisa() {
        for (Iterator it = limiteDao.getLimitesParaMesaDivisa(null, null, null).iterator();
             it.hasNext();) {
            Limite l = (Limite) it.next();
            logger.info(l.getTipoLimite().getNombre() + TAB + l.getLimite() +
                    TAB + l.getNivel() + TAB + l.getPorcentajeConsumo() + TAB +
                    l.getEstado());
        }
    }

    /**
     * Prueba el servicio getLimitesParaMesaDivisa(), pasando la mesa 1 y la divisa null.
     *
     * @see LimiteDao#getLimitesParaMesaDivisa(Integer, String, Integer).
     */
    public void testConMesaSinDivisa() {
        for (Iterator it = limiteDao.getLimitesParaMesaDivisa(new Integer(1), null, null).iterator();
             it.hasNext();) {
            Limite l = (Limite) it.next();
            logger.info(l.getTipoLimite().getNombre() + TAB + l.getLimite() +
                    TAB + l.getNivel() + TAB + l.getPorcentajeConsumo() + TAB +
                    l.getEstado());
        }
    }

    /**
     * Prueba el servicio getLimitesParaMesaDivisa(), pasando la mesa 1 y la divisa USD.
     *
     * @see LimiteDao#getLimitesParaMesaDivisa(Integer, String, Integer).
     */
    public void testConMesaConDivisa() {
        for (Iterator it = limiteDao.getLimitesParaMesaDivisa(new Integer(1), Divisa.DOLAR, null).
                iterator(); it.hasNext();) {
            Limite l = (Limite) it.next();
            logger.info(l.getTipoLimite().getNombre() + TAB + l.getLimite() +
                    TAB + l.getNivel() + TAB + l.getPorcentajeConsumo() + TAB +
                    l.getEstado());
        }
    }

    /**
     * Prueba el servicio getLimitesParaMesaDivisa(), pasando la mesa null y la divisa USD.
     *
     * @see LimiteDao#getLimitesParaMesaDivisa(Integer, String, Integer).
     */
    public void testSinMesaConDivisa() {
        for (Iterator it = limiteDao.getLimitesParaMesaDivisa(null, Divisa.DOLAR, null).iterator();
             it.hasNext();) {
            Limite l = (Limite) it.next();
            logger.info(l.getIdLimite() + TAB + l.getLimite() +
                    TAB + l.getNivel() + TAB + l.getPorcentajeConsumo() + TAB +
                    l.getEstado());
        }
    }

    /**
     * La referencia al bean limiteDao.
     */
    private LimiteDao limiteDao;

    /**
     * El objeto para logging.
     */
    private final Log logger = LogFactory.getLog(getClass());

    /**
     * Constante para la cadena "\t\t".
     */
    private static final String TAB = "\t\t";
}
