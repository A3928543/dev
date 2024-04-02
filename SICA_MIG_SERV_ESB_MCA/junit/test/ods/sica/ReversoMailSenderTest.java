/*
 * $Id: ReversoMailSenderTest.java,v 1.13.30.1 2010/06/24 16:39:00 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2007 - 2010 LegoSoft S.C.
 */

package test.ods.sica;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.model.Reverso;
import com.ixe.ods.sica.sdo.ReversosServiceData;
import com.ixe.ods.sica.services.ReversoMailSender;

import junit.framework.TestCase;

/**
 * Pruebas unitarias del componente ReversoMailSender.
 *
 * @author Jean C. Favila
 * @version $Revision: 1.13.30.1 $ $Date: 2010/06/24 16:39:00 $
 */
public class ReversoMailSenderTest extends TestCase {

	/**
	 * Inicia el applicationContext.
	 *
	 * @throws Exception
	 */
    protected void setUp() throws Exception {
        super.setUp();
        String[] paths = {"applicationContext.xml"};
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext(paths);
        sdo = (ReversosServiceData) applicationContext.getBean("reversosServiceData");
        reversoMailSender = (ReversoMailSender) applicationContext.getBean("reversoMailSender");
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
     * Prueba del servicio enviarMailReverso().
     *
     * @see ReversoMailSender#enviarMailReverso(com.ixe.ods.sica.model.Reverso, String[]).
     */
    public void testEnviarMailReverso() {
        try {
            Reverso reverso = sdo.findReverso(630);
            reversoMailSender.enviarMailReverso(reverso, new String[] {"favilaj@legosoft.com.mx"});
        }
        catch (SicaException e) {
            fail(e.getMessage());
        }
    }

    /**
     * El objeto para logging.
     */
    protected final Log logger = LogFactory.getLog(getClass());

    /**
     * El componente de env&iacute;o de reversos por correo electr&oacute;nico.
     */
    private ReversoMailSender reversoMailSender;

    /**
     * El Service Data utilizado para realizar operaciones a a la base de datos.
     */
    private ReversosServiceData sdo;
}
