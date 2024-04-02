/*
 * $Id: CondorProcCambiosDaoTest.java,v 1.1 2009/01/07 17:26:09 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2009 LegoSoft S.C.
 */
package test.ods.sica;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.ixe.ods.sica.dao.CondorProcCambiosDao;

import junit.framework.TestCase;

/**
 * Prueba Unitaria para el DAO CondorProcCambiosDao.
 *
 * @author Jean C. Favila
 * @version $Revision: 1.1 $ $Date: 2009/01/07 17:26:09 $
 */
public class CondorProcCambiosDaoTest extends TestCase {

    /**
     * Constructor por default.
     */
    public CondorProcCambiosDaoTest() {
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
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext(paths);
        dao = (CondorProcCambiosDao) applicationContext.getBean("condorProcCambiosDao");
    }

    /**
     * Prueba el servicio que ejecuta el stored procedure.
     */
    public void testEjecutar() {
        GregorianCalendar gc = new GregorianCalendar();
        gc.add(Calendar.MONTH, -7);
        gc.add(Calendar.DAY_OF_MONTH, -7);
        System.out.println(gc.getTime());
        for (Iterator it = dao.ejecutar(gc.getTime()).iterator(); it.hasNext();) {
            String s = (String) it.next();
            System.out.println(s);
        }
    }

    /**
     * El DAO a probar.
     */
    private CondorProcCambiosDao dao;
}
