package test.com.ixe.ods.sica;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.ixe.ods.sica.SicaWorkFlowService;
import com.ixe.ods.sica.model.Actividad;

public class AutorizacionesDelegateTest  extends TestCase { 

	
	/** The ctx. */
	private ClassPathXmlApplicationContext ctx;
	
	/** The Constant log. */
	private static final Log log = LogFactory.getLog(AutorizacionesDelegateTest.class);
	
	private SicaWorkFlowService workFlow;
	
	  private String ticket = "402897c006f1a12f01070155a8bf0180";
	  private int idUsuario = 23232;

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
		log.info("SETUP.....");
		String[] paths = {"applicationContextTest.xml"};
		ctx = new ClassPathXmlApplicationContext(paths); //new FileSystemXmlApplicationContext(paths);
		workFlow = (SicaWorkFlowService) ctx.getBean("sicaWorkFlowService");
	}
	
	public void testConfirmar() {
		log.info("inicio");
		String tipo = Actividad.ACT_DN_PAGO_ANTICIPADO;
		boolean autorizado = true;
		int idActividad = 1066001;
		try {
			workFlow.completarActividad(ticket, idActividad, tipo, autorizado, idUsuario);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log.info("Fin");
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
