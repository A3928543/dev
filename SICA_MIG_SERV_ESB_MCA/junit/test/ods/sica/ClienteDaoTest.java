/*
 * $Id: ClienteDaoTest.java,v 1.14 2009/06/17 20:23:19 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2007 - 2009 LegoSoft S.C.
 */

package test.ods.sica;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.ixe.ods.sica.dao.ClienteDao;

import junit.framework.TestCase;

/**
 * Pruebas unitarias para los el servicio ClienteDao.
 *
 * @author Jean C. Favila
 * @version $Revision: 1.14 $ $Date: 2009/06/17 20:23:19 $
 */
public class ClienteDaoTest extends TestCase {

    /**
     * Constructor por default.
     */
    public ClienteDaoTest() {
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
        _clienteDao = (ClienteDao) applicationContext.getBean("clienteDao");
    }

    /**
     * Prueba la b&uacute;squeda de personas f&iacute;sicas.
     *
     * @see com.ixe.ods.sica.dao.ClienteDao#findClientes(String,String,String,String,String,boolean)
     */
    public void testFindClientePersonaFisica() {
        Date fechaInicio = new Date();
        List ctes = _clienteDao.findClientes(new Integer(1087956), null, "", "", "ENRI", null, true);
        logger.info("*** Tiempo: " + ((new Date().getTime() - fechaInicio.getTime()) / 1000)
                + " segs.");
        for (Iterator it = ctes.iterator(); it.hasNext();) {
            logger.info(it.next());
        }
        assertTrue("No se encontraron clientes", !ctes.isEmpty());
    }

    /**
     * Prueba la b&uacute;squeda de personas f&iacute;sicas.
     *
     * @see com.ixe.ods.sica.dao.ClienteDao#findClientes(Integer,String,String,String,String,String,boolean)
     */
    public void testFindClientePersonaMoral() {
        Date fechaInicio = new Date();
        List ctes = _clienteDao.findClientes(new Integer(1087956), "GRUPO", "", "", "", "", true);
        logger.info("*** Tiempo: " + ((new Date().getTime() - fechaInicio.getTime()) / 1000)
                + " segs.");
        logger.info(String.valueOf(ctes.size()));
        for (Iterator it = ctes.iterator(); it.hasNext();) {
            logger.info(it.next());
        }
        assertTrue("No se encontraron clientes", !ctes.isEmpty());
    }

    /**
     * Prueba la b&uacute;squeda de personas por numero de cuenta.
     *
     * @see com.ixe.ods.sica.dao.ClienteDao#findClientes(Integer,String,String,String,String,String,boolean)
     */
    public void testFindClientePersonaCuenta() {
        Date fechaInicio = new Date();
        List ctes = _clienteDao.findClientes(new Integer(1087956), null, null, null, null,
                "2000441066-1", true);
        logger.info("*** Tiempo: " + ((new Date().getTime() - fechaInicio.getTime()) / 1000)
                + " segs.");
        for (Iterator it = ctes.iterator(); it.hasNext();) {
            logger.info(it.next());

        }
        assertTrue("No se encontraron clientes", !ctes.isEmpty());
    }

    /**
     * Prueba la b&uacute;squeda de personas f&iacute;sicas.
     *
     * @see com.ixe.ods.sica.dao.ClienteDao#findClientes(String,String,String,String,String,boolean)
     */
    public void testFindClientePersonaFisicaSinEjecutivo() {
        Date fechaInicio = new Date();
        List ctes = _clienteDao.findClientes(null, "COVIAN", "", "CARLOS", null, true);
        logger.info("*** Tiempo: " + ((new Date().getTime() - fechaInicio.getTime()) / 1000)
                + " segs.");
        for (Iterator it = ctes.iterator(); it.hasNext();) {
            logger.info(it.next());
        }
        assertTrue(!ctes.isEmpty());
    }

    /**
     * Prueba la b&uacute;squeda de personas f&iacute;sicas.
     *
     * @see com.ixe.ods.sica.dao.ClienteDao#findClientes(Integer,String,String,String,String,String,boolean)
     */
    public void testFindClientePersonaMoralSinEjecutivo() {
        Date fechaInicio = new Date();
        List ctes = _clienteDao.findClientes("GRUPO", "", "", "", "", false);
        logger.info("*** Tiempo: " + ((new Date().getTime() - fechaInicio.getTime()) / 1000)
                + " segs.");
        logger.info("" + ctes.size());
        for (Iterator it = ctes.iterator(); it.hasNext();) {
            logger.info(it.next());
        }
        assertTrue(!ctes.isEmpty());
    }

    /**
     * Prueba la b&uacute;squeda de personas por numero de cuenta.
     *
     * @see com.ixe.ods.sica.dao.ClienteDao#findClientes(Integer,String,String,String,String,String,boolean)
     */
    public void testFindClientePersonaCuentaSinEjecutivo() {
        Date fechaInicio = new Date();
        List ctes = _clienteDao.findClientes(null, null, null, null, "2000441066-1", true);
        logger.info("*** Tiempo: " + ((new Date().getTime() - fechaInicio.getTime()) / 1000)
                + " segs.");
        for (Iterator it = ctes.iterator(); it.hasNext();) {
            logger.info(it.next());
        }
        assertTrue(!ctes.isEmpty());
    }


    /**
     * Prueba el servicio findClientes() para Raz&oacute;n Social.
     *
     * @see ClienteDao#findClientes(String, String).
     */
    public void testFindClientesRazonSocialCuenta() {
        Date fechaInicio = new Date();
        List ctes = _clienteDao.findClientes("GRUPO", (String) null);
        logger.info("*** Tiempo: " + ((new Date().getTime() - fechaInicio.getTime()) / 1000)
                + " segs.");
        for (Iterator it = ctes.iterator(); it.hasNext();) {
            logger.info(it.next());
        }
        assertTrue(!ctes.isEmpty());
    }

    /**
     * Prueba el servicio findClientes() para noCuenta.
     *
     * @see ClienteDao#findClientes(String, String).
     */
    public void testFindClientesRazonSocialCuenta2() {
        Date fechaInicio = new Date();
        List ctes = _clienteDao.findClientes(null, "2000443283-5");
        logger.info("*** Tiempo: " + ((new Date().getTime() - fechaInicio.getTime()) / 1000)
                + " segs.");
        for (Iterator it = ctes.iterator(); it.hasNext();) {
            logger.info(it.next());
        }
        assertTrue(!ctes.isEmpty());
    }
    
    /**
     * Instancia de SicaServieData.
     */
    private ClienteDao _clienteDao;

    /**
     * El objeto para logging.
     */
    private final transient Log logger = LogFactory.getLog(getClass());    
}
