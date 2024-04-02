package test.ods.sica.pizarron;

import java.util.Random;

/**
 * Utilidades para el test en SICA_VARIACION
 * 
 * @author Cesar Jeronimo Gomez
 *
 */
public class VariacionUtil {
	
	private static Random random = new Random();
	
	/**
	 * Genera un numero aleatorio para un rango dado
	 * 
	 * @param fromNum
	 * @param toNum
	 * @return
	 */
	public static double getRandomNum(double fromNum, double toNum) {
		return fromNum + ( random.nextDouble() * (toNum - fromNum) );
	}

}
