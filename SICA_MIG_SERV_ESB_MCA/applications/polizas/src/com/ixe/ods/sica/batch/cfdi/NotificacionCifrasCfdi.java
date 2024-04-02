package com.ixe.ods.sica.batch.cfdi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ixe.ods.sica.batch.cfdi.service.CifrasControlService;

/**
 * The Class NotificacionCifrasCfdi.
 */
public class NotificacionCifrasCfdi extends Contexto {
	
	/** The Constant LOG. */
	private static final Logger LOG = LoggerFactory.getLogger(NotificacionCifrasCfdi.class);
	

	/**
	 * Instantiates a new notificacion cifras cfdi.
	 */
	public NotificacionCifrasCfdi() {
		super();
	}

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		NotificacionCifrasCfdi notificacion = new NotificacionCifrasCfdi();
		notificacion.inicia();
	}
	
	/**
	 * Inicia.
	 */
	private void inicia() {
		CifrasControlService cifrasService = 
				this.getContexto().getBean(CifrasControlService.class);
		int codigo = cifrasService.enviarNotificaciones();
		LOG.debug("Cdigo: {}", codigo);
		switch (codigo) {
			case 0:
				LOG.info("Las notificaciones fueron enviadas corectamente.");
			break;
			case -1:
				LOG.error("Las fechas no coinciden.");
			break;
			case -2:
				LOG.error("No existe informaci\u00F3n registrada de las cifras de control.");
			break;
			case -3:
				LOG.error("No se encontr\u00F3 informaci\u00F3n de la configuraci\u00F3n " +
							"cifras de control.");
			break;
			case -4:
				LOG.error("Error en la configuraci\u00F3n de las cifras de control, existe " +
							"m\00E1s de un registro.");
			break;
			case -5:
				LOG.error("Error al efectuar el env\00EDo de la notificaci\u00F3n del " +
							"reporte de cifras de control.");
			break;
			case -6:
				LOG.error("Error al efectuar el env\00EDo de la notificaci\u00F3n del " +
							"reporte de detalle de cifras de control.");
			break;
		}
		
	}

}
