/*
 * $Id: ExternalSiteSessionBeanTest.java,v 1.14 2008/02/26 18:34:52 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 - 2007 LegoSoft S.C.
 */

package test.ods.sica;

import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.ejb.CreateException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ixe.ods.seguridad.model.SeguridadException;
import com.ixe.treasury.dom.common.Banco;
import com.ixe.treasury.dom.common.ExternalSiteException;
import com.ixe.treasury.middleware.ejb.ExternalSiteSession;
import com.ixe.treasury.middleware.ejb.ExternalSiteSessionHome;

import junit.framework.TestCase;

/**
 * Pruebas unitarias para los servicios del SITE.
 *
 * @author Jean C. Favila
 * @version $Revision: 1.14 $ $Date: 2008/02/26 18:34:52 $
 */
public class ExternalSiteSessionBeanTest extends TestCase {

    /**
     * Constructor vac&iacute;o.
     */
    public ExternalSiteSessionBeanTest() {
        super();
    }

    /**
     * Levanta el contexto para la prueba.
     *
     * @throws Exception Si ocurre un error.
     */
    protected void setUp() throws Exception {
        super.setUp();
        Properties ps = new Properties();
        ps.put(InitialContext.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
        ps.put(InitialContext.PROVIDER_URL, "t3://localhost:7001");
        logger.info(ps);
        Context ctx;
        try {
            ctx = new InitialContext(ps);
            ExternalSiteSessionHome home = (ExternalSiteSessionHome) ctx.
                    lookup("ejb.site.ExternalSiteHome");
            ejb = home.create();
        }
        catch (NamingException e) {
            e.printStackTrace();
            throw e;
        }
        catch (CreateException e) {
            e.printStackTrace();
            throw e;
        }
        catch (RemoteException e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Prueba el servicio findBancosInternacionales().
     */
    public void testFindBancosInternacionales() {
        try {
            Map parametros = new LinkedHashMap();
            parametros.put("tipoClave", "SWIFT");
            parametros.put("claveSwift", "DANBDK21ARR");
            parametros.put("claveChips", "");
            parametros.put("claveRuteo", "DKRN");
            parametros.put("numeroRuteo", "5950");
            parametros.put("nombreBanco", "ANDELSKASSE");
            parametros.put("pais", "denmark");
            parametros.put("ciudad", "aarre");
            List bancos = ejb.findBancosInternacionales(ticket, parametros);
            for (Iterator it = bancos.iterator(); it.hasNext();) {
                Banco banco = (Banco) it.next();
                logger.info(banco);
            }
        }
        catch (RemoteException e) {
            fail(e.getMessage());
        }
        catch (ExternalSiteException e) {
            fail(e.getMessage());
        }
        catch (SeguridadException e) {
            fail(e.getMessage());
        }
    }

    /**
     * Prueba el servicio findBancoInternacionalById().
     */
    public void testFindBancoInternacionalById() {
        try {
            ejb.findBancoInternacionalById(ticket, new Long(ID_BANCO));
        }
        catch (RemoteException e) {
            fail(e.getMessage());
        }
        catch (ExternalSiteException e) {
            fail(e.getMessage());
        }
        catch (SeguridadException e) {
            fail(e.getMessage());
        }
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
     * El id del banco de prueba.
     */
    private static final int ID_BANCO = 26;

    /**
     * Instancia del EJB del TELLER.
     */
    private ExternalSiteSession ejb;

    /**
     * El objeto para logging.
     */
    private final Log logger = LogFactory.getLog(getClass());

    /**
     * Constante para el valor del ticket.
     */
    private static final String ticket = "402897c006f1a12f01070155a8bf0180";

}
