package test.ods.sica.pizarron;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import junit.framework.TestCase;

/**
 * Test para verificar el funcionamiento de la generación de pizarrones insertando registros
 * con valores aleatorios en SICA_VARIACION
 * 
 * @author César Jerónimo Gómez
 *
 */
public class VariacionTest extends TestCase {
	
	/**
	 * Número de inserciones a ejecutar
	 */
	public static final int INSERT_TIMES = 5;
	
	/**
	 * Milisegundos entre inserciones
	 */
	public static final long MILLISECS_TO_DELAY = 10;
	
	/*
	 * Mínimos y máximos (para cada uno de los campos de SICA_VARIACION) entre los cuales se generan los valores a insertar
	 */
	
	public static final double MIN_VARIACION_MID 					= (double)7.07745;
	public static final double MIN_VARIACION_VTA_SPOT 				= (double)1;
	public static final double MIN_VARIACION_VTA_SPOT_ASK 			= (double)9.8575;
	public static final double MIN_MXN_EUR_VARIACION_VTA_SPOT 		= (double)1.38643058;
	public static final double MIN_MXN_EUR_VARIACION_VTA_SPOT_ASK 	= (double)14.631102;
	public static final double MIN_CAD_VARIACION_VTA_SPOT 			= (double)0.9058;
	public static final double MIN_CAD_VARIACION_VTA_SPOT_ASK 		= (double)0.9061;
	public static final double MIN_CHF_VARIACION_VTA_SPOT 			= (double)0.9789;
	public static final double MIN_CHF_VARIACION_VTA_SPOT_ASK 		= (double)0.9793;
	public static final double MIN_EUR_VARIACION_VTA_SPOT 			= (double)1.2354;
	public static final double MIN_EUR_VARIACION_VTA_SPOT_ASK 		= (double)1.2355;
	public static final double MIN_GBP_VARIACION_VTA_SPOT 			= (double)1.3505;
	public static final double MIN_GBP_VARIACION_VTA_SPOT_ASK 		= (double)1.3509;
	public static final double MIN_JPY_VARIACION_VTA_SPOT 			= (double)85.15;
	public static final double MIN_JPY_VARIACION_VTA_SPOT_ASK 		= (double)85.16;
	
	public static final double MAX_VARIACION_MID 					= (double)40.8396;
	public static final double MAX_VARIACION_VTA_SPOT 				= (double)40.8371;
	public static final double MAX_VARIACION_VTA_SPOT_ASK 			= (double)40.8421;
	public static final double MAX_MXN_EUR_VARIACION_VTA_SPOT 		= (double)40.095299;
	public static final double MAX_MXN_EUR_VARIACION_VTA_SPOT_ASK 	= (double)40.15;
	public static final double MAX_CAD_VARIACION_VTA_SPOT 			= (double)40.3062;
	public static final double MAX_CAD_VARIACION_VTA_SPOT_ASK 		= (double)40.3067;
	public static final double MAX_CHF_VARIACION_VTA_SPOT 			= (double)40.2296;
	public static final double MAX_CHF_VARIACION_VTA_SPOT_ASK 		= (double)40.2301;
	public static final double MAX_EUR_VARIACION_VTA_SPOT 			= (double)40.603;
	public static final double MAX_EUR_VARIACION_VTA_SPOT_ASK 		= (double)40.6034;
	public static final double MAX_GBP_VARIACION_VTA_SPOT 			= (double)40.1116;
	public static final double MAX_GBP_VARIACION_VTA_SPOT_ASK 		= (double)40.1119;
	public static final double MAX_JPY_VARIACION_VTA_SPOT 			= (double)123.64;
	public static final double MAX_JPY_VARIACION_VTA_SPOT_ASK 		= (double)123.69;
	
	private ApplicationContext applicationContext;
	private JdbcTemplate jdbcTemplate;
	private VariacionDao variacionDao;
	
	/**
	 * Inicializa el contexto de Spring y los servicios a utilizar para las pruebas
	 */
	protected void setUp() throws Exception {
        super.setUp();
        String[] configLocations = {"test/ods/sica/pizarron/sicaApplicationContextVariacion.xml"};
        applicationContext = new ClassPathXmlApplicationContext(configLocations);
        jdbcTemplate = (JdbcTemplate) applicationContext.getBean("jdbcTemplate");
        variacionDao = (VariacionDao) applicationContext.getBean("variacionDao");
    }
	
	/**
	 * Obtiene un registro de SICA_VARIACION con valores aleatorios
	 * 
	 * @return
	 */
	public VariacionDto getRandomVariacion() {
		return new VariacionDto(
			null,
			new Date(),
			new Double(VariacionUtil.getRandomNum(MIN_VARIACION_MID, MAX_VARIACION_MID)),
			new Double(VariacionUtil.getRandomNum(MIN_VARIACION_VTA_SPOT, MAX_VARIACION_VTA_SPOT)),
			new Double(VariacionUtil.getRandomNum(MIN_VARIACION_VTA_SPOT_ASK, MAX_VARIACION_VTA_SPOT_ASK)),
			new Double(VariacionUtil.getRandomNum(MIN_MXN_EUR_VARIACION_VTA_SPOT, MAX_MXN_EUR_VARIACION_VTA_SPOT)),
			new Double(VariacionUtil.getRandomNum(MIN_MXN_EUR_VARIACION_VTA_SPOT_ASK, MAX_MXN_EUR_VARIACION_VTA_SPOT_ASK)),
			new Double(VariacionUtil.getRandomNum(MIN_CAD_VARIACION_VTA_SPOT, MAX_CAD_VARIACION_VTA_SPOT)),
			new Double(VariacionUtil.getRandomNum(MIN_CAD_VARIACION_VTA_SPOT_ASK, MAX_CAD_VARIACION_VTA_SPOT_ASK)),
			new Double(VariacionUtil.getRandomNum(MIN_CHF_VARIACION_VTA_SPOT, MAX_CHF_VARIACION_VTA_SPOT)),
			new Double(VariacionUtil.getRandomNum(MIN_CHF_VARIACION_VTA_SPOT_ASK, MAX_CHF_VARIACION_VTA_SPOT_ASK)),
			new Double(VariacionUtil.getRandomNum(MIN_EUR_VARIACION_VTA_SPOT, MAX_EUR_VARIACION_VTA_SPOT)),
			new Double(VariacionUtil.getRandomNum(MIN_EUR_VARIACION_VTA_SPOT_ASK, MAX_EUR_VARIACION_VTA_SPOT_ASK)),
			new Double(VariacionUtil.getRandomNum(MIN_GBP_VARIACION_VTA_SPOT, MAX_GBP_VARIACION_VTA_SPOT)),
			new Double(VariacionUtil.getRandomNum(MIN_GBP_VARIACION_VTA_SPOT_ASK, MAX_GBP_VARIACION_VTA_SPOT_ASK)),
			new Double(VariacionUtil.getRandomNum(MIN_JPY_VARIACION_VTA_SPOT, MAX_JPY_VARIACION_VTA_SPOT)),
			new Double(VariacionUtil.getRandomNum(MIN_JPY_VARIACION_VTA_SPOT_ASK, MAX_JPY_VARIACION_VTA_SPOT_ASK))
		);
	}
	
	/**
	 * Test que inserta un solo registro
	 */
	public void testInsertVariacion() {
		System.out.println("Comienza inserción en sica_variacion");
		variacionDao.insert(getRandomVariacion());
		System.out.println("Finaliza inserción en sica_variacion");
	}
	
	/**
	 * Test que inserta las veces definidas en INSERT_TIMES cada MILLISECS_TO_DELAY
	 * 
	 * @throws InterruptedException
	 */
	public void testInsertVariacionXTimes() throws InterruptedException {
		List result = null;
		System.out.println("Comienzan inserciones en SICA_VARIACION");
		for(int i = 0; i < INSERT_TIMES; i ++) {
			variacionDao.insert(getRandomVariacion());
			System.out.println("Variación " + i + " insertada");
			//result = jdbcTemplate.queryForList("SELECT * FROM SC_RENGLON_PIZARRON ORDER BY ID_TIPO_PIZARRON, ID_DIVISA, CLAVE_FORMA_LIQUIDACION");
			//imprimePizarron(result);
			Thread.sleep(MILLISECS_TO_DELAY);
		}
		System.out.println("Finalizan inserciones en SICA_VARIACION");
	}
	
	/**
	 * Metodo auxiliar para imprimir el pizarron
	 * 
	 * @param pizarron
	 */
	private void imprimePizarron(List pizarron) {
		System.out.println("Comienza pizarron");
		for(Iterator it = pizarron.iterator(); it.hasNext();) {
			System.out.println(it.next());
		}
		System.out.println("Termina pizarron");
	}
	
}
