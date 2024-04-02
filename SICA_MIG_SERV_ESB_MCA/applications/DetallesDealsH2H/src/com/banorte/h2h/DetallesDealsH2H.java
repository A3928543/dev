package com.banorte.h2h;

import org.apache.log4j.Logger;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.banorte.h2h.services.H2hService;

public class DetallesDealsH2H 
{

	private static final Logger LOG = Logger.getLogger(DetallesDealsH2H.class);
	private static final String LOCATION_FILE = "config/applicationContext.xml";
	private final String H2H_SERVICE = "h2hService";
	
	private FileSystemXmlApplicationContext contexto = null;
	
	public DetallesDealsH2H()
	{
		contexto = new FileSystemXmlApplicationContext(LOCATION_FILE);
	}
	
	public static void main(String[] args) 
	{
		DetallesDealsH2H h2h = new DetallesDealsH2H();
		h2h.iniciar();
	}
	
	private void iniciar() 
	{
		H2hService h2hService = null;
		
		try 
		{
			LOG.warn("**********************************************************************************************************");
			LOG.warn("*****************************        PROCESO ENVIO DE OPERACIONES A H2H         **************************");
			LOG.warn("**********************************************************************************************************");
			h2hService = contexto.getBean(H2H_SERVICE, H2hService.class);
			h2hService.iniciarProcesamientoDetallesDeal();
			LOG.warn("**********************************************************************************************************");
			LOG.warn("********************     FIN DE PROCESO PROCESO ENVIO DE OPERACIONES A H2H     ***************************");
			LOG.warn("**********************************************************************************************************");
			LOG.warn("\n\n\n");
		}
		catch (Exception ex) 
		{
			LOG.error("Exception ", ex);
		}
	}
}
