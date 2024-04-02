package com.banorte.reporte.diario.transferencias;

import org.apache.log4j.Logger;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.banorte.reporte.diario.transferencias.services.ITransferenciasService;


public class ReporteDiarioTransferencias 
{
	private static final Logger LOG = Logger.getLogger(ReporteDiarioTransferencias.class);
	
	private static final String LOCATION_FILE = "config/applicationContext.xml"; 
	
	private FileSystemXmlApplicationContext contexto;

	public ReporteDiarioTransferencias()
	{
		contexto = new FileSystemXmlApplicationContext(LOCATION_FILE);
	}
	
	public static void main(String[] args) 
	{
		ReporteDiarioTransferencias proceso = new ReporteDiarioTransferencias();
		proceso.iniciar();
	} 
	
	private void iniciar() 
	{
		try 
		{
			LOG.warn("**********************************************************************************************************");
			LOG.warn("*****************************          PROCESO DIARIO DE TRANSFERENCIAS         **************************");
			LOG.warn("**********************************************************************************************************");
			ITransferenciasService service = contexto.getBean("transferenciasService", ITransferenciasService.class);
			service.consultarTransferenciasReporte();
			LOG.warn("**********************************************************************************************************");
			LOG.warn("******************************     FIN DE PROCESO DIARIO DE TRANSFERENCIAS     ***************************");
			LOG.warn("**********************************************************************************************************");
			LOG.warn("\n\n\n");
		}
		catch (Exception ex) 
		{
			LOG.error("Exception ", ex);
		}
	}
}
