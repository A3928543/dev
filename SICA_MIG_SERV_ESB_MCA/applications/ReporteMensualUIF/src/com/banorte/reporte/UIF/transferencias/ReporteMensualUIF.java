package com.banorte.reporte.UIF.transferencias;

import org.apache.log4j.Logger;
import org.springframework.context.support.FileSystemXmlApplicationContext;
//import com.banorte.reporte.diario.transferencias.services.ITransferenciasService;

import com.banorte.reporte.UIF.transferencias.services.ITransferenciasService;

public class ReporteMensualUIF {
	
	private static final Logger LOG = Logger.getLogger(ReporteMensualUIF.class);
	
	private static final String LOCATION_FILE = "config/applicationContext.xml"; 
	
	private FileSystemXmlApplicationContext contexto;
	
	public ReporteMensualUIF()
	{
		contexto = new FileSystemXmlApplicationContext(LOCATION_FILE);
	}

	public static void main(String[] args) {
		ReporteMensualUIF proceso = new ReporteMensualUIF();
		proceso.iniciar();

	}
	
	private void iniciar() 
	{
		try 
		{
			LOG.warn("**********************************************************************************************************");
			LOG.warn("*****************************          PROCESO MENSUAL DE TRANSFERENCIAS        **************************");
			LOG.warn("**********************************************************************************************************");
			ITransferenciasService service = contexto.getBean("transferenciasService", ITransferenciasService.class);
			service.consultarTransferenciasReporte();
			LOG.warn("**********************************************************************************************************");
			LOG.warn("******************************     FIN DE PROCESO MENSUAL DE TRANSFERENCIAS     **************************");
			LOG.warn("**********************************************************************************************************");
			LOG.warn("\n\n\n");
		}
		catch (Exception ex) 
		{
			LOG.error("Exception ", ex);
		}
	}

}
