package com.banorte.ods.sica.batch.transfint;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.banorte.ods.sica.batch.transfint.service.ReporteService;

/**
 * The Class ReporteLauncher.
 */
public class ReporteLauncher extends Contexto {
	
	/** The Constant LOG. */
	private static final Logger LOG = LoggerFactory.getLogger(ReporteLauncher.class);

	/**
	 * Instantiates a new reporte launcher.
	 */
	public ReporteLauncher() {
		super();
	}

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		ReporteLauncher launcher = new ReporteLauncher();
		if (args.length == 1) {
			launcher.inicia(args[0]);
		}
		else {
			throw new IllegalArgumentException("No se definieron correctamente " +
								"los parametros de entrada del proceso.");
		}
		
	}
	
	/**
	 * Inicia.
	 *
	 * @param bean the bean
	 * @param fechaIni the fecha ini
	 * @param fechaFin the fecha fin
	 */
	private void inicia(String tipoReporte) {
		ReporteService service = null;
		try {
			service = this.getContexto().getBean(ReporteService.class);
			if ("TRANS_DIARIAS".equals(tipoReporte)) {
				LOG.info("*****************************************************");
				LOG.info("**           INICIA REPORTE TRANSFINT              **");
				LOG.info("*****************************************************");
				service.generaReporte();
				LOG.info("*****************************************************");
				LOG.info("**           TERMINA REPORTE TRANSFINT             **");
				LOG.info("*****************************************************");
			}
			else if ("TRANS_TARDIAS".equals(tipoReporte)) {
				LOG.info("*****************************************************");
				LOG.info("** INICIA REPORTE TRANSFINT DE OPERACIONES TARDIAS **");
				LOG.info("*****************************************************");
				service.generaReporteTardias();
				LOG.info("*****************************************************");
				LOG.info("** TERMINA REPORTE TRANSFINT DE OPERACIONES TARDIAS **");
				LOG.info("*****************************************************");
			}
			else {
				throw new IllegalArgumentException("El tipo de reporte es incorrecto, " +
									"favor de validar.");
			}
		}
		catch (Exception ex) {
			LOG.error("Exception en inicia() ", ex);
			if (service != null) { 
				service.sendErrorEmail(throwableToString(ex));
			}
		}
	}
	
	/**
	 * Throwable to string.
	 *
	 * @param ex the ex
	 * @return the string
	 */
	public static String throwableToString(Throwable ex) {
		String result = "";
		if (ex != null) {
			Writer writer = new StringWriter();
		    PrintWriter printWriter = new PrintWriter(writer);
		    ex.printStackTrace(printWriter);
		    result = writer.toString();
		}
		
		return result;
	}

}
