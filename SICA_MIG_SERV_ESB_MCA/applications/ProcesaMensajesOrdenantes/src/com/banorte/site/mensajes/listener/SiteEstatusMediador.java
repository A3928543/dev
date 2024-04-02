package com.banorte.site.mensajes.listener;

import java.util.Date;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.banorte.site.mensajes.listener.util.CargaConfiguracion;

public class SiteEstatusMediador 
{
	private static Logger logger = Logger.getLogger(SiteEstatusMediador.class);
	
	public static void main(String[] args) 
	{
		Properties log4j = null;
		
		try
		{
			log4j = CargaConfiguracion.cargarLogj4();
			PropertyConfigurator.configure(log4j);
			
			logger.warn("--->>>>>> Inicia procesamiento de mensajes recibidos desde el Mediador....");
			logger.warn(new Date(System.currentTimeMillis()));
			
			ProcesaMensajesMediador proceso = new ProcesaMensajesMediador();
			proceso.iniciarProcesamientoMensajesMediador();
			
			logger.warn(new Date(System.currentTimeMillis()));
			logger.warn("--->>>>>> Finaliza procesamiento de mensajes recibidos desde el Mediador....");
			logger.warn("---------------------------------------------------------------------------------------------------------------");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			logger.error(e);
		}
	}

}
