/*
 * $Id: ContabilidadServiceDataTest.java,v 1.1 2008/06/19 21:01:00 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2008 LegoSoft S.C.
 */

package test.ods.sica;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.ixe.ods.sica.sdo.ContabilidadSicaServiceData;

import junit.framework.TestCase;

/**
 * Pruebas unitarias para el Bean ContabilidadServiceData.
 *
 * @author Jean C. Favila
 * @version  $Revision: 1.1 $ $Date: 2008/06/19 21:01:00 $
 */
public class ContabilidadServiceDataTest extends TestCase {

    /**
     * Constructor vac&iacute;o.
     */
    public ContabilidadServiceDataTest() {
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
        sdo = (ContabilidadSicaServiceData) applicationContext.getBean("contabilidadServiceData");
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
     * Prueba el servicio generarUtilidadesIxeForwards() de ContabilidadSicaServiceData.
     *
     * @see ContabilidadSicaServiceData#generarUtilidadesIxeForwards(int, java.util.Date).
     */
    public void testGenerarUtilidadesIxeForwards() {
        try {
            sdo.generarUtilidadesIxeForwards(1, DATE_FORMAT.parse("16/06/2008"));
        }
        catch (ParseException e) {
            fail(e.getMessage());
        }
    }

    /**
     * Prueba el servicio findUtilidadForwardByFecha() de ContabilidadSicaServiceData.
     *
     * @see com.ixe.ods.sica.sdo.ContabilidadSicaServiceData#findUtilidadForwardByFecha(
     *      java.util.Date). 
     */
    public void testReporteUtilidadesForwards() {
        try {
            logger.info(sdo.findUtilidadForwardByFecha(DATE_FORMAT.parse("14/05/2008")));
        }
        catch (ParseException e) {
            fail(e.getMessage());
        }
    }

    /**
     * El dao de generaci&oacute;n de Movimientos Contables.
     */
    private ContabilidadSicaServiceData sdo;

    /**
     * El objeto para logging.
     */
    private final Log logger = LogFactory.getLog(getClass());

    /**
     * El formateador de fechas.
     */
    private static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy"); 
}
