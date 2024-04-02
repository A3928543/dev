/*
 * $Id: DistribucionNormalInversaTest.java,v 1.2 2009/09/25 15:55:55 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2008 LegoSoft S.C.
 */

package test.ods.sica;

import com.ixe.ods.sica.DistribucionNormalInversa;

import junit.framework.TestCase;

/**
 * Prueba unitaria para la clase DistribucionNormalInversa.
 *
 * @author Jean C. Favila
 * @version $Revision: 1.2 $ $Date: 2009/09/25 15:55:55 $
 */
public class DistribucionNormalInversaTest extends TestCase {

    /**
     * Constructor vac&iacute;o por default.
     */
    public DistribucionNormalInversaTest() {
        super();
    }

    /**
     * Inicializa el application context.
     *
     * @throws Exception Si ocurre alg&uacute;n error.
     */
    protected void setUp() throws Exception {
        super.setUp();
    }

    /**
     * Prueba el servicio calcular(p). Con un valor p de 0.95, debe dar 1.644853625133699, y para el
     * valor de 0.99 debe dar 2.326347874388028.
     */
    public void testCalcular() {
        assertTrue(DistribucionNormalInversa.calcular(PROB_95) == DIST_95);
        assertTrue(DistribucionNormalInversa.calcular(PROB_99) == DIST_99);
    }

    /**
     * Constante para la probabilidad 0.95.
     */
    private static final double PROB_95 = 0.95;

    /**
     * Constante para la probabilidad 0.99.
     */
    private static final double PROB_99 = 0.99;

    /**
     * Constante para el valor de la distribuci&oacute;n normal inversa de la probabilidad 0.95.
     */
    private static final double DIST_95 = 1.644853625133699;

    /**
     * Constante para el valor de la distribuci&oacute;n normal inversa de la probabilidad 0.99.
     */
    private static final double DIST_99 = 2.326347874388028;
}
