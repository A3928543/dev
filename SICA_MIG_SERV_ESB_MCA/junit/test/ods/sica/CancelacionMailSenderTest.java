/*
 * $Id: CancelacionMailSenderTest.java,v 1.1 2008/07/08 04:16:48 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2008 LegoSoft S.C.
 */

package test.ods.sica;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.model.Deal;
import com.ixe.ods.sica.sdo.SicaServiceData;
import com.ixe.ods.sica.services.CancelacionMailSender;

import junit.framework.TestCase;

/**
 * Pruebas unitarias para el Bean CancelacionMailSender.
 *
 * @author Jean C. Favila
 * @version  $Revision: 1.1 $ $Date: 2008/07/08 04:16:48 $
 */
public class CancelacionMailSenderTest extends TestCase {

    /**
     * Constructor por default.
     */
    public CancelacionMailSenderTest() {
        super();
    }

    /**
	 * Inicia el applicationContext.
	 *
	 * @throws Exception Si ocurre alg&uacute;n error.
	 */
    protected void setUp() throws Exception {    
        super.setUp();
        String[] paths = {"applicationContext.xml"};
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext(paths);
        sdo = (SicaServiceData) applicationContext.getBean("sicaServiceData");
        cancelacionMailSender = (CancelacionMailSender) applicationContext.
                getBean("cancelacionMailSender");
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
     * Prueba del servicio enviarMailPromotor().
     *
     * @see com.ixe.ods.sica.services.CancelacionMailSender#enviarMailPromotor(
     *          com.ixe.ods.sica.model.Deal).
     */
    public void testEnviarMailCancelacion() {
        try {
            Deal deal = sdo.findDeal(ID_DEAL);
            cancelacionMailSender.enviarMailPromotor(deal);
        }
        catch (SicaException e) {
            fail(e.getMessage());
        }
    }

    /**
     * El componente de env&iacute;o de cancelaciones por correo electr&oacute;nico.
     */
    private CancelacionMailSender cancelacionMailSender;

    /**
     * El Service Data utilizado para realizar operaciones a a la base de datos.
     */
    private SicaServiceData sdo;

    /**
     * El n&uacute;mero de deal.
     */
    public static final int ID_DEAL = 33333;
}
