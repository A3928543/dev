/*
 * $Id: MailVespertinoServiceTest.java,v 1.3 2008/12/26 23:17:27 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2008 LegoSoft S.C.
 */

package test.ods.sica;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.ixe.ods.sica.services.MailVespertinoService;

import junit.framework.TestCase;

/**
 * Prueba unitaria para el bean MailVespertinoServiceTest.
 *
 * @author Jean C. Favila
 * @version  $Revision: 1.3 $ $Date: 2008/12/26 23:17:27 $
 */
public class MailVespertinoServiceTest extends TestCase {

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

    public void testEnviarCorreoCierreVespertino() {
        MailVespertinoService mvs = (MailVespertinoService) appContext.
                getBean("mailVespertinoService");
        mvs.enviarCorreoCierreVespertino(null);
    }

    /**
     * El contexto de la aplicaci&oacute;n.
     */
    private ApplicationContext appContext;
}
