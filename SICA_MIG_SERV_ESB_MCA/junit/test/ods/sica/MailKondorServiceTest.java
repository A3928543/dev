/*
 * $Id: MailKondorServiceTest.java,v 1.2 2010/04/13 20:32:08 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2009 LegoSoft S.C.
 */

package test.ods.sica;

import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.services.MailKondorService;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import junit.framework.TestCase;

/**
 * Pruebas unitarias para la clase MailKondorService.
 * 
 * @author Jean C. Favila (jfavila)
 * @version $Revision: 1.2 $ $Date: 2010/04/13 20:32:08 $
 * @see MailKondorService
 */
public class MailKondorServiceTest extends TestCase {

    /**
     * Constructor por default.
     */
    public MailKondorServiceTest() {
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
     * Prueba el servicio <code>enviarCorreoCierreVespertino()</code>.
     */
    public void testEnviarCorreo() {
        try {
            MailKondorService mks = (MailKondorService) appContext.getBean("mailKondorService");
            mks.enviarCorreo("idConf", 9344, "MD344", 1);
        }
        catch (SicaException e) {
            fail(e.getMessage());
        }
        catch (BeansException e) {
            fail(e.getMessage());
        }
    }

    /**
     * El contexto de la aplicaci&oacute;n.
     */
    private ApplicationContext appContext;
}