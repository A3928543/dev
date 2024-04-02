/*
 * $Id: ActividadDaoTest.java,v 1.1 2009/09/23 16:59:00 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2009 LegoSoft S.C.
 */
package test.ods.sica;

import com.ixe.ods.sica.dao.ActividadDao;
import com.ixe.ods.sica.model.Actividad;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import junit.framework.TestCase;

/**
 * Pruebas unitarias para el bean ActividadDao.
 * 
 * @author Jean C. Favila (jfavila)
 * @version $Revision: 1.1 $ $Date: 2009/09/23 16:59:00 $
 */
public class ActividadDaoTest extends TestCase {

    /**
     * Inicializa el application context.
     *
     * @throws Exception Si ocurre alg&uacute;n error.
     */
	protected void setUp() throws Exception {
        super.setUp();
        String[] paths = {"junit/jdbcDaoApplicationContext.xml"};
        ApplicationContext appContext = new FileSystemXmlApplicationContext(paths);
        actividadDao = (ActividadDao) appContext.getBean("actividadDao");
    }

    /**
     * Prueba el servicio getWorkitems() que trae todas las actividades pendientes.
     */
    public void testGetAllWorkitems() {
        List actividades = actividadDao.getWorkitems(TICKET, null);
        for (Iterator it = actividades.iterator(); it.hasNext();) {
            logger.info(it.next());
        }
    }

    /**
     * Prueba el servicio getWorkitems() que trae todas las actividades pendientes por monto. 
     */
    public void testGetMontoWorkitems() {
        List actividades = actividadDao.getWorkitems(TICKET, Actividad.ACT_DN_MONTO);
        for (Iterator it = actividades.iterator(); it.hasNext();) {
            logger.info(it.next());
        }
    }

    /**
     * Prueba el servicio getWorkitems() que trae todas las actividades pendientes por sobreprecio. 
     */
    public void testGetSobreprecioWorkitems() {
        List actividades = actividadDao.getWorkitems(TICKET, Actividad.ACT_DN_SOBREPRECIO);
        for (Iterator it = actividades.iterator(); it.hasNext();) {
            logger.info(it.next());
        }
    }

    /**
     * El objeto para logging.
     */
    private final Log logger = LogFactory.getLog(getClass());

    /**
     * Referencia al bean brokerDao del ApplicationContext.
     */
    private ActividadDao actividadDao;

    /**
     * Constante para el valor del ticket.
     */
    private static final String TICKET = "402897c006f1a12f01070155a8bf0180";
}
