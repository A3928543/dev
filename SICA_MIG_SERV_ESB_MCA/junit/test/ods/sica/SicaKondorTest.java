/*
 * $Id: SicaKondorTest.java,v 1.2 2010/04/13 20:32:08 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2009 LegoSoft S.C.
 */

package test.ods.sica;

import com.ixe.ods.sica.SicaKondorSession;
import com.ixe.ods.sica.SicaKondorSessionHome;
import com.ixe.ods.sica.model.BitacoraEnviadas;
import com.ixe.ods.sica.model.BitacoraEnviadasPK;
import com.ixe.ods.sica.model.RespAltaKondor;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import junit.framework.TestCase;

/**
 * Prueba unitaria del EJB SicaKondorSessionBean.
 *
 * @author Jean C. Favila
 * @version $Revision: 1.2 $ $Date: 2010/04/13 20:32:08 $
 */
public class SicaKondorTest extends TestCase {

    /**
     * Constructor por default.
     */
    public SicaKondorTest() {
        super();
    }

    /**
	 * Levanta el contexto para la prueba.
     *
     * @throws Exception Si no hay conexi&oacute;n con el EJB.
	 */
    protected void setUp() throws Exception {
        super.setUp();
        Properties ps = new Properties();
        ps.put(InitialContext.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
        //ps.put(InitialContext.PROVIDER_URL, "t3://10.211.55.3:7001");
        ps.put(InitialContext.PROVIDER_URL, "t3://127.0.0.1:7001");
        logger.info(ps);
        Context ctx = new InitialContext(ps);
        SicaKondorSessionHome home = (SicaKondorSessionHome) ctx.lookup("ejb/sica/SicaKondorHome");
        ejb = home.create();
    }

    /**
     * Prueba del servicio cargarBitacoraEnviadas().
     *
     * @see SicaKondorSession#crearSwapKondor(String, java.util.ArrayList).
     */
    public void testCrearSwapKondor() {        
        try {
            BitacoraEnviadas be = new BitacoraEnviadas();
            BitacoraEnviadasPK bePk = new BitacoraEnviadasPK();
            bePk.setDivisa("USD");
            bePk.setIdConf("K+FXS-484-1");
            be.setId(bePk);
            be.setClaveContraparte("000750");
            be.setAccion(new Integer(71));
            be.setPlazo(new Integer(2));
            be.setTipoOper(new Integer(1));
            be.setMonto(new Double(1000000));
            be.setTipo(new Integer(0));
            be.setFechaConcertacion(new Date());
            be.setFechaLiquidacion(new Date());
            be.setSwap("1");
            be.setError(new Integer(0));
            be.setCreateDt(new Date());
            be.setStatus("ENVIAR");
            be.setStatusEstrategia("PE");
            be.setMontoDivisa(new Double(1000000));
            be.setTipoCambio(new Double(13.71));
            be.setFolderk("MD344");
            be.setComentarios("Los comentarios");
            be.setContraDivisa("MXN");

            BitacoraEnviadas be2 = new BitacoraEnviadas();
            BitacoraEnviadasPK bePk2 = new BitacoraEnviadasPK();
            bePk2.setDivisa("USD");
            bePk2.setIdConf("K+FXS-484-2");
            be2.setId(bePk2);
            be2.setClaveContraparte("900149");
            be2.setAccion(new Integer(71));
            be2.setPlazo(new Integer(2));
            be2.setTipoOper(new Integer(0));
            be2.setMonto(new Double(1000000));
            be2.setTipo(new Integer(0));
            be2.setFechaConcertacion(new Date());
            be2.setFechaLiquidacion(new Date());
            be2.setSwap("1");
            be2.setError(new Integer(0));
            be2.setCreateDt(new Date());
            be2.setStatus("ENVIAR");
            be2.setStatusEstrategia("PE");
            be2.setMontoDivisa(new Double(1000000));
            be2.setTipoCambio(new Double(13.71));
            be2.setFolderk("MD344");
            be2.setComentarios("Los comentarios");
            be2.setContraDivisa("MXN");
            
            ArrayList bes = new ArrayList();
            bes.add(be);
            bes.add(be2);

            RespAltaKondor[] resp = ejb.crearSwapKondor("8a82a7a422ebcc2b0122ec0ef5c20049", bes);

            for (int i = 0; i < resp.length; i++) {
                logger.info("idConf: " + resp[i].getIdConf() + " idDeal: " + resp[i].getIdDeal());
            }
        }
        catch (javax.naming.NamingException e) {
            fail(e.getMessage());
        }
        catch (RemoteException e) {
            fail(e.getMessage());
        }
        catch (javax.ejb.CreateException e) {
            fail(e.getMessage());
        }
    }

    /**
     * Instancia del EJB de Sica-KONDOR.
     */
    private SicaKondorSession ejb;

    /**
     * El objeto para logging.
     */
    private final Log logger = LogFactory.getLog(getClass());
}
