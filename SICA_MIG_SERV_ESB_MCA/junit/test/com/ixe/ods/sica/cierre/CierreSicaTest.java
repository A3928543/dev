package test.com.ixe.ods.sica.cierre;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import test.com.ixe.ods.sica.SicaSession;

public class CierreSicaTest extends TestCase {
	
	/** The ctx. */
	private ClassPathXmlApplicationContext ctx;
	
	/** The Constant log. */
	private static final Log log = LogFactory.getLog(CierreSicaTest.class);
	
	private test.com.ixe.ods.sica.SicaSession ejb;

	/**
	 * (non-Javadoc).
	 * 
	 * @throws Exception
	 *             the exception
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		log.info("SETUP del test: CierreSicaTest");
		String[] paths = {"applicationContextTest.xml"};
		ctx = new ClassPathXmlApplicationContext(paths); //new FileSystemXmlApplicationContext(paths);
		ejb = (SicaSession) ctx.getBean("sicaSession");
	}
	
	//smtp mail3
	public void _testCierreContableyFinal() {
		String[] correos ={"mejiar@personalexterno.ixe.com.mx"};
		log.info("Iniciando cierre contable...");
		try {
			log.info(ejb.iniciarCierreContableYCierreFinal(correos));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("Error:"+e);
		}
		
		log.info("fin cierre contable");
	}
	
	//smtp mail3
	public void testCierreFinal() {
		String[] correos ={"mejiar@personalexterno.ixe.com.mx"};
		log.info("Iniciando cierre contable...");
		try {
			log.info(ejb.iniciarCierreFinal(correos));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("Error:"+e);
		}
		
		log.info("fin cierre contable");
	}

	/**
	 * Tear down.
	 * 
	 * @throws Exception
	 *             the exception
	 * 
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}
}
