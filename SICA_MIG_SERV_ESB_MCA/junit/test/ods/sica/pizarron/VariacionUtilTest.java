package test.ods.sica.pizarron;

import junit.framework.TestCase;

/**
 * Test para la clase de utilidades de SICA_VARIACION
 * 
 * @author Cesar Jeronimo Gomez
 *
 */
public class VariacionUtilTest extends TestCase {
	
	/**
	 * Testea el metodo de generacion de numeros aleatorios
	 */
	public void testGetRandomNum() {
		for(int i = 0; i < 100; i ++) {
			System.out.println( VariacionUtil.getRandomNum((double)20, (double)21) );
		}
	}

}
