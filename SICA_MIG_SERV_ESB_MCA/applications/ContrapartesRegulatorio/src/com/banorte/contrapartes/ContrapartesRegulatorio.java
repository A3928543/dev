package com.banorte.contrapartes;

//import java.util.HashMap;

import org.apache.log4j.Logger;
//import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.banorte.contrapartes.services.IOperacionesDeal;
//import com.banorte.contrapartes.services.mail.MailSender;

public class ContrapartesRegulatorio 
{
	
	/** The Constant LOG. */
	private static final Logger LOG = Logger.getLogger(ContrapartesRegulatorio.class);
	
	/** The Constant LOCATION_FILE. */
	private static final String LOCATION_FILE = "config/applicationContext.xml"; 
	
	/** The contexto. */
	private FileSystemXmlApplicationContext contexto;

	/**
	 * Instantiates a new proceso actualizacion.
	 */
	public ContrapartesRegulatorio() 
	{
		LOG.warn("**********************************************************************************************************");
		LOG.warn("----> Se carga el contexto de la aplicación...");
		contexto = new FileSystemXmlApplicationContext(LOCATION_FILE);
	}
	
	public static void main(String[] args) 
	{
		ContrapartesRegulatorio proceso = new ContrapartesRegulatorio();
		//proceso.iniciar();
	}
	
	private void iniciar() 
	{
		try 
		{
			//MailSender mail =  contexto.getBean("mailSender", MailSender.class);
			IOperacionesDeal deal = contexto.getBean("operacionesDealService", IOperacionesDeal.class);
			LOG.warn("**********************************************************************************************************");
			LOG.warn("***************************          PROCESO DE CONTRAPARTES REGULATORIO         *************************");
			LOG.warn("**********************************************************************************************************");
			//mail.send(new HashMap<String, Object>());
			deal.consultaDeals();
			//deal.enviarEmail();
			LOG.warn("**********************************************************************************************************");
			LOG.warn("******************************     FIN DE PROCESO CONTRAPARTES REGULATORIO     ***************************");
			LOG.warn("**********************************************************************************************************");
			LOG.warn("\n\n\n");
		}
		catch (Exception ex) 
		{
			LOG.error("Exception ", ex);
		}
	}
}
