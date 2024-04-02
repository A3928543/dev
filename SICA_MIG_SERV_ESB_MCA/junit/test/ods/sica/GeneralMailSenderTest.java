/*
 * $Id: GeneralMailSenderTest.java,v 1.1.28.1 2010/06/24 16:38:05 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2008 - 2010 LegoSoft S.C.
 */

package test.ods.sica;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.ixe.ods.sica.services.GeneralMailSender;

import junit.framework.TestCase;

/**
 * Pruebas del servicio GeneralMailSender.
 *
 * @author Jean C. Favila
 * @version $Revision: 1.1.28.1 $ $Date: 2010/06/24 16:38:05 $
 */
public class GeneralMailSenderTest extends TestCase {

    /**
     * Inicializa el application context.
     *
     * @throws Exception Si ocurre alg&uacute;n error.
     */
	protected void setUp() throws Exception {
        super.setUp();
        String[] paths = {"applicationContext.xml"};
        ApplicationContext appContext = new ClassPathXmlApplicationContext(paths);
        gms = (GeneralMailSender) appContext.getBean("generalMailSender");
    }

    /**
     * Prueba el servicio enviarMail().
     */
    public void testEnviarMail() {
        gms.enviarMail("ixecambios@ixe.com.mx",
                new String[] {"favilaj@ixe.com.mx", "eleija@legosoft.com.mx"},
                new String[] {"ggonzalez@legosoft.com.mx"}, "El titulo",
                "Esta es una prueba del bean GeneralMailSender.");
    }

    /**
     * El bean GeneralMailSender.
     */
    private GeneralMailSender gms;
}
