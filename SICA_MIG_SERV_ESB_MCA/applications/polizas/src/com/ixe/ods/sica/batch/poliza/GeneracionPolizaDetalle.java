package com.ixe.ods.sica.batch.poliza;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.ixe.ods.sica.batch.poliza.service.FileService;

import static com.ixe.ods.sica.batch.util.Utilerias.throwableToString;

/**
 * The Class GeneracionPolizaDetalle.
 */
public class GeneracionPolizaDetalle {
	
	/** The Constant LOG. */
	private static final Logger LOG = LoggerFactory.getLogger(GeneracionPolizaDetalle.class);
	
	/** The Constant LOCATION_FILE. */
	private static final String LOCATION_FILE = "archivos/applicationContext.xml";
	
	/** The contexto. */
	private ApplicationContext contexto;

	
	/**
	 * Instantiates a new generacion poliza detalle.
	 */
	public GeneracionPolizaDetalle() {
		contexto = new FileSystemXmlApplicationContext(LOCATION_FILE);
	}
	
	/**
	 * Inicia.
	 */
	private void inicia() {
		FileService service = null;
		try {
			service = contexto.getBean("fileService", FileService.class);
			LOG.info("*****************************************************");
			LOG.info("**  INICIA PROCESO DE DEPURACION DE ARCHIVOS       **");
			LOG.info("*****************************************************");
			service.depuraHistoricoArchivosDetallePolizas();
			LOG.info("*****************************************************");
			LOG.info("**  TERMINA PROCESO DE DEPURACION DE ARCHIVOS      **");
			LOG.info("*****************************************************");
			LOG.info("*****************************************************");
			LOG.info("**  INICIA GENERACION DE MOVIMIENTOS A DETALLE     **");
			LOG.info("*****************************************************");
			boolean procesoOk = service.generarArchivoDetallePolizas();
			LOG.info("procesoOK: {}", procesoOk);
			if (procesoOk) {
				service.creaArchivoBandera();
			}
			LOG.info("*****************************************************");
			LOG.info("**  TERMINA GENERACION DE MOVIMIENTOS A DETALLE     *");
			LOG.info("*****************************************************");
		}
		catch (Exception ex) {
			LOG.error("Exception ", ex);
			if (service != null) { 
				service.sendErrorEmail(throwableToString(ex));
			}
		}
	}

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		GeneracionPolizaDetalle generacionPolizaDetalle = 
				new GeneracionPolizaDetalle();
		generacionPolizaDetalle.inicia();
	}

}
