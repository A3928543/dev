/*
 * $Id: BitacoraEnviadasDaoTest.java,v 1.2 2010/04/13 20:30:10 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2009 - 2010 LegoSoft S.C.
 */

package test.ods.sica;

import java.util.Iterator;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.ixe.ods.bup.model.ContratoSica;
import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.dao.BitacoraEnviadasDao;
import com.ixe.ods.sica.model.BitacoraEnviadas;
import com.ixe.ods.sica.model.Broker;
import com.ixe.ods.sica.model.ContraparteBanxico;
import com.ixe.ods.sica.sdo.SicaServiceData;
import com.ixe.ods.sica.sdo.SwapsServiceData;

import junit.framework.TestCase;

/**
 * Prueba del Data Access Object de SC_BITACORA_ENVIADAS.
 *
 * @author Jean C. Favila
 * @version $Revision: 1.2 $ $Date: 2010/04/13 20:30:10 $
 */
public class BitacoraEnviadasDaoTest extends TestCase {

    /**
     * Constructor por default.
     */
    public BitacoraEnviadasDaoTest() {
        super();
    }

    /**
     * Inicializa el application context.
     *
     * @throws Exception Si ocurre alg&uacute;n error.
     */
	protected void setUp() throws Exception {
        super.setUp();
        String[] paths = {"./applicationContext.xml"};
        appContext = new ClassPathXmlApplicationContext(paths);
        dao = (BitacoraEnviadasDao) appContext.getBean("bitacoraEnviadasDao");
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
     * Prueba el servicio <code>findBitacorasEnviadasByFolioTas()</code>.
     */
    public void testFindBitacorasEnviadasByFolioTas() {
        List bes = dao.findBitacorasEnviadasByFolioTas(21094);

        for (Iterator it = bes.iterator(); it.hasNext();) {
            BitacoraEnviadas be = (BitacoraEnviadas) it.next();
            SwapsServiceData ssd = (SwapsServiceData) appContext.getBean("swapsServiceData");
            SicaServiceData sicaServiceData = (SicaServiceData) appContext.getBean("sicaServiceData");
            ContraparteBanxico cb = ssd.findContraparteBanxico(be.getClaveContraparte());

            System.out.println(cb.getIdSaif() + " " + cb.getPersonaSica().getIdPersona());
            Integer idPersonaContraparte = cb.getPersonaSica().getIdPersona();
            ContratoSica cs = sicaServiceData.findContratoSicaByIdPersona(idPersonaContraparte);
            if (cs == null) {
                throw new SicaException("No se encontr\u00f3 el Contrato SICA n\u00famero " +
                        be.getNoCuenta());
            }
            List brokers = sicaServiceData.findBrokerByIdPersona(idPersonaContraparte);
            if (brokers.isEmpty()) {
                throw new SicaException("No se encontr\u00f3 la contraparte correspondiente a la " +
                        "persona " + cb.getPersonaSica().getIdPersona());
            }
            Broker broker = (Broker) brokers.get(0);
            //resultados.add(ssd.crearBitacoraEnviadasVO(folioTas, broker, be, cs.getNoCuenta()));
        }
        //return resultados;
        /*for (Iterator it = bes.iterator(); it.hasNext();) {
            BitacoraEnviadas bitacoraEnviadas = (BitacoraEnviadas) it.next();
            System.out.println(bitacoraEnviadas);
        } */
    }

    private ApplicationContext appContext;
    private BitacoraEnviadasDao dao;
}
