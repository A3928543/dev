package com.ixe.ods.sica.batch.poliza;

import static com.ixe.ods.sica.batch.util.Utilerias.throwableToString;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ixe.ods.sica.batch.poliza.service.RegistroCifrasControlService;

/**
 * The Class CifrasControlLauncher.
 */
public class CifrasControlLauncher extends Contexto {
	
	/** The Constant LOG. */
	private static final Logger LOG = LoggerFactory.getLogger(CifrasControlLauncher.class);

	/**
	 * Instantiates a new cifras control launcher.
	 */
	public CifrasControlLauncher() {
		super();
	}
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		CifrasControlLauncher launcher = new CifrasControlLauncher();
		launcher.inicia();
	}
	
	/**
	 * Inicia.
	 */
	private void inicia() {
		RegistroCifrasControlService service = null;
		try {
			service = this.getContexto().getBean(RegistroCifrasControlService.class);
			LOG.info("*****************************************************");
			LOG.info("**  INICIA REGISTRO DE LAS CIFRAS DE CONTROL       **");
			LOG.info("*****************************************************");
			service.registraCifrasControl();
			LOG.info("*****************************************************");
			LOG.info("**  TERMINA REGISTRO DE LAS CIFRAS DE CONTROL      **");
			LOG.info("*****************************************************");
		}
		catch (Exception ex) {
			LOG.error("Exception en inicia() ", ex);
			if (service != null) { 
				service.sendErrorEmail(throwableToString(ex));
			}
		}
	}
}
