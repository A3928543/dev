/*
 * $Id: ReversosServiceDataTest.java,v 1.2 2008/02/22 18:25:43 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2007 LegoSoft S.C.
 */

package test.ods.sica;

import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.model.Reverso;
import com.ixe.ods.sica.sdo.ReversosServiceData;
import com.ixe.ods.sica.vo.DealVO;
import java.util.Iterator;
import java.util.List;
import junit.framework.TestCase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Pruebas de unidad para los servicios de ReversosServiceData
 *
 * @author Jean C. Favila
 * @version $Revision: 1.2 $ $Date: 2008/02/22 18:25:43 $
 */
public class ReversosServiceDataTest extends TestCase {

    /**
     * Constructor por default.
     */
    public ReversosServiceDataTest() {
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
        appContext = new ClassPathXmlApplicationContext(paths);
        sdo = (ReversosServiceData) appContext.getBean("reversosServiceData");
    }

    
    public void testFindReversosPendientes() {
        List reversos = sdo.findReversosPendientes();
        for (Iterator it = reversos.iterator(); it.hasNext();) {
            Reverso reverso = (Reverso) it.next();
            System.out.println(reverso.getIdReverso() + " "
                    + reverso.getDealOriginal().getTipoValor());
        }
    }

    public void testPrepararDealsReverso() {
        try {
            List dealVos = sdo.prepararDealsReverso(ticket, 501);
            for (Iterator it = dealVos.iterator(); it.hasNext();) {
                DealVO dealVO = (DealVO) it.next();
                System.out.println(dealVO.getIdDeal());
            }
        }
        catch (SicaException e) {
            logger.info(e);
            fail(e.getMessage());
        }
    }

    /**
     * El objeto para logging.
     */
    private final Log logger = LogFactory.getLog(getClass());

    /**
     * El contexto de la aplicaci&oacute;n.
     */
    private ApplicationContext appContext;

    /**
     * Instancia de ReversosServiceData.
     */
    private ReversosServiceData sdo;

    /**
     * Constante para el valor del ticket de la sesi&oacute;n.
     */
    private String ticket = "402897c006f1a12f01070155a8bf0180";
    
}
