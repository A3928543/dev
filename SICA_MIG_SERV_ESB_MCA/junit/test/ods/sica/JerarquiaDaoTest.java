/*
 * $Id: JerarquiaDaoTest.java,v 1.1 2009/04/27 15:59:49 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2009 LegoSoft S.C.
 */

package test.ods.sica;

import java.util.Iterator;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ixe.ods.bup.model.Persona;
import com.ixe.ods.sica.dao.JerarquiaDao;

import junit.framework.TestCase;

/**
 * Prueba unitaria de los servicios de la clase JerarquiaDao.
 *
 * @author Jean C. Favila
 * @version $Revision: 1.1 $ $Date: 2009/04/27 15:59:49 $
 * @see com.ixe.ods.sica.dao.LimiteDao
 */
public class JerarquiaDaoTest extends TestCase {

    /**
     * Inicializa el limiteDao a partir del application context.
     *
     * @throws Exception Si ocurre alg&uacute;n error.
     */
    protected void setUp() throws Exception {
        super.setUp();
        String[] paths = {"applicationContext.xml"};
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext(paths);
        jerarquiaDao = (JerarquiaDao) applicationContext.getBean("jerarquiaDao");
    }

    /**
     * Prueba el servicio <code>findPromotoresJerarquia()</code> de JerarquiaDao.
     */
    public void testFindPromotoresJerarquia() {
        List resultados = jerarquiaDao.findPromotoresJerarquia(new Integer(1085300));
        for (Iterator it = resultados.iterator(); it.hasNext();) {
            Persona persona = (Persona) it.next();
            logger.info(persona.getNombreCompleto());
        }
    }

    /**
     * Referencia al bean 'jerarquiaDao' del application Context de Spring.
     */
    private JerarquiaDao jerarquiaDao;

    /**
     * El objeto para logging.
     */
    private final Log logger = LogFactory.getLog(getClass());    
}
