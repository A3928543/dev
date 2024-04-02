/*
 * $Id: LineaCambioDaoTest.java,v 1.3 2010/02/26 01:02:24 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2009 - 2010 LegoSoft S.C.
 */

package test.ods.sica;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.ixe.ods.sica.dao.LineaCambioDao;

import junit.framework.TestCase;

/**
 * Pruebas unitarias del bean LineaCambioDao.
 *
 * @author Jean C. Favila (jfavila)
 * @version $Revision: 1.3 $ $Date: 2010/02/26 01:02:24 $
 */
public class LineaCambioDaoTest extends TestCase {

    /**
     * Constructor por default.
     */
    public LineaCambioDaoTest() {
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
        lineaCambioDao = (LineaCambioDao) appContext.getBean("lineaCambioDao");
    }

    /**
     * Prueba el servicio <code>cuadrarLineasCambio()</code>.
     */
    public void testCuadrarLineasCambio() {
        try {
            List resultados = lineaCambioDao.cuadrarLineasCambio();
            for (Iterator it = resultados.iterator(); it.hasNext();) {
                logger.info(it.next());                
            }
        }
        catch (Exception e) {
            fail(e.getMessage());
        }
    }

    public void testGetFechaMinimaParaLineaCambio() {
        logger.info("" + lineaCambioDao.getDiasAdeudoParaLineaCambio(1221));
    }

    public void testGetFechaMinimaParaLineaCambioSinLinea() {
        logger.info("" + lineaCambioDao.getDiasAdeudoParaLineaCambio(-2332));
    }

    /**
     * El objeto para logging.
     */
    private final Log logger = LogFactory.getLog(getClass());

    /**
     * Referencia al bean <code>lineaCambioDao</code> del applicationContext.
     */
    private LineaCambioDao lineaCambioDao;
}
