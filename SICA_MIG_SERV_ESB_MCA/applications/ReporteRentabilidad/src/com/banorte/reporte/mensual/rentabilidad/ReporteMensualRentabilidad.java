package com.banorte.reporte.mensual.rentabilidad;

import org.apache.log4j.Logger;
import org.springframework.context.support.FileSystemXmlApplicationContext;
//import com.banorte.reporte.diario.transferencias.services.ITransferenciasService;


import com.banorte.reporte.mensual.rentabilidad.services.ITransferenciasService;

public class ReporteMensualRentabilidad {
	
	private static final Logger LOG = Logger.getLogger(ReporteMensualRentabilidad.class);
	
	private static final String LOCATION_FILE = "config/applicationContext.xml"; 
	
	private FileSystemXmlApplicationContext contexto;
	
	public ReporteMensualRentabilidad()
	{
		contexto = new FileSystemXmlApplicationContext(LOCATION_FILE);
	}

	public static void main(String[] args) {
		ReporteMensualRentabilidad proceso = new ReporteMensualRentabilidad();
		proceso.iniciar();

	}
	
	private void iniciar() 
	{
		try 
		{
			LOG.warn("**********************************************************************************************************");
			LOG.warn("*****************************          PROCESO MENSUAL GENERADOR DEL REPORTE DE RENTABILIDAD   ***********");
			LOG.warn("**********************************************************************************************************");
			ITransferenciasService service = contexto.getBean("transferenciasService", ITransferenciasService.class);
			service.consultarReporteRentabilidad();
			LOG.warn("**********************************************************************************************************");
			LOG.warn("******************************     FIN DE PROCESO MENSUAL DE RENTABILIDAD     **************************");
			LOG.warn("**********************************************************************************************************");
			LOG.warn("\n\n\n");
		}
		catch (Exception ex) 
		{
			LOG.error("Exception ", ex);
		}
	}

}
