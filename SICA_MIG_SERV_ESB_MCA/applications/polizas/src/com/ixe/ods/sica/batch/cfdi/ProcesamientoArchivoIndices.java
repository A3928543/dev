package com.ixe.ods.sica.batch.cfdi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ixe.ods.sica.batch.cfdi.service.FileIndicesService;

import static com.ixe.ods.sica.batch.util.Utilerias.throwableToString;

/**
 * The Class ProcesamientoArchivoIndices.
 */
public class ProcesamientoArchivoIndices extends Contexto {
	
	/** The Constant LOG. */
	private static final Logger LOG = LoggerFactory.getLogger(ProcesamientoArchivoIndices.class);
	
	/**
	 * Instantiates a new procesamiento archivo indices.
	 */
	public ProcesamientoArchivoIndices() {
		super();
	}
	
	/**
	 * Inicia.
	 */
	public void inicia() {
		try {
			FileIndicesService indicesService = 
					this.getContexto().getBean(FileIndicesService.class);
			LOG.debug("/***********************************************/");
			LOG.debug("/*      INICIA DEPURACION DE ARCHIVOS.         */");
			LOG.debug("/***********************************************/");
			indicesService.depurarHistoricoArchivos();
			LOG.debug("/***********************************************/");
			LOG.debug("/*      TERMINA DEPURACION DE ARCHIVOS.        */");
			LOG.debug("/***********************************************/");
			LOG.debug("/***********************************************/");
			LOG.debug("/*  INICIA PROCESAMIENTO ARCHIVO DE INDICES.   */");
			LOG.debug("/***********************************************/");
			indicesService.procesaArchivo();
			LOG.debug("/***********************************************/");
			LOG.debug("/*  TERMINA PROCESAMIENTO ARCHIVO DE INDICES.  */");
			LOG.debug("/***********************************************/");
		}
		catch (Exception ex) {
			String msg = throwableToString(ex);
			LOG.error("Exception en inicia(): {}", msg);
		}
	}

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		ProcesamientoArchivoIndices archivoIndices = 
						new ProcesamientoArchivoIndices();
		archivoIndices.inicia();
	}
}
