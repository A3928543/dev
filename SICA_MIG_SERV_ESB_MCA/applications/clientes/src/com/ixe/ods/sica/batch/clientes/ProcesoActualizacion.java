package com.ixe.ods.sica.batch.clientes;

import org.apache.log4j.Logger;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.ixe.ods.sica.batch.clientes.service.ClienteService;

/**
 * The Class ProcesoActualizacion.
 */
public class ProcesoActualizacion {
	
	/** The Constant LOG. */
	private static final Logger LOG = Logger.getLogger(ProcesoActualizacion.class);
	
	/** The Constant LOCATION_FILE. */
	private static final String LOCATION_FILE = "archivos/applicationContext.xml";
	
	/** The contexto. */
	private FileSystemXmlApplicationContext contexto;

	/**
	 * Instantiates a new proceso actualizacion.
	 */
	public ProcesoActualizacion() {
		contexto = new FileSystemXmlApplicationContext(LOCATION_FILE);
	}
	
	/**
	 * Inicia.
	 */
	private void inicia() {
		try {
			ClienteService service = 
					contexto.getBean("clienteService", ClienteService.class);
			LOG.info("*****************************************************");
			LOG.info("**           PROCESO DE DEPURACION                 **");
			LOG.info("*****************************************************");
			service.depuraHistoricoArchivosProcesados();
			LOG.info("*****************************************************");
			LOG.info("**         PROCESO DE FUSION DE CLIENTES           **");
			LOG.info("*****************************************************");
			service.procesaArchivos();
		}
		catch (Exception ex) {
			LOG.error("Exception ", ex);
		}
	}

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		ProcesoActualizacion proceso = 
				new ProcesoActualizacion();
		proceso.inicia();
	}

}
