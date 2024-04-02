package com.ixe.ods.sica.rmds;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.ixe.ods.sica.rmds.feed.SicaFeedDataService;
import com.ixe.ods.sica.rmds.process.service.DepuracionVariacionesService;

/**
 * Main-Class. 
 * 
 * Implementaci&oacute;n RFA para la alimentaci&oacute;n de de tipos cambio
 * al SICA. Este programa realiza las tareas de Login, Carga de Directorio, 
 * Carga de Diccionario de Datos y Suscripci&oacute;n a Instrumentos de Reuters
 * (RIC's), para posteriormente dirigir las variaciones recibidas a la entidad
 * de Base de datos SICA_ADMIN.SICA_VARIACION donde ser&aacute;n escuchadas por el 
 * proceso de genraci&oacute;n de pizarrones. 
 * 
 * @author Efren Trinidad, Na-at Technologies
 * 
 */
public class OmmConsumer {
	
	/**
	 * La instancia de utilidad para grabar mensajes en el log.
	 */
	public static Logger logger = Logger.getLogger("com.ixe.ods.sica.rmds.feed");
	
	/**
	 * Main-method
	 * 
	 * Levanta el contexto de Spring Framework, obtiene un bean de servicio
	 * de suscripci&oacute;n al RMDS e inicia su ejecuci&oacute;n.
	 * 
	 * @param args No se utiliza.
	 */
	public static void main(String args[]){
		
		ApplicationContext applicationContext = 
			new FileSystemXmlApplicationContext("resources/applicationContext.xml");
		
		try{
			
			if(args == null ||  args.length != 1){
				throw new NumberFormatException();
			}
			
			int opc = new Integer(args[0]);
			
			if(opc == 1){
				
				logger.info("******************************************************************");
				logger.info("* Inica ejecucion del proceso de alimentacion de precios al SICA *");
				logger.info("******************************************************************");
				
				SicaFeedDataService feedDataService = applicationContext.getBean(SicaFeedDataService.class);
				
				feedDataService.startFeedData();
				
				logger.info("-------------------------------------");
				logger.info("- Termina ejecucion de OMM Consumer -");
				logger.info("-------------------------------------");
				
			}else if( opc == 2){
				
				logger.info("******* Inicia depuracion de precios al SICA ******");
				
				DepuracionVariacionesService depuracionVariacionesService = 
					applicationContext.getBean(DepuracionVariacionesService.class);
				
				depuracionVariacionesService.depurarVariacionPrecios();
				
				logger.info("------- Termina depuracion de precios SICA ---------");
				
			}else{
				logger.error("Opcion no soportada");
				throw new NumberFormatException();
			}
		
		}catch(NumberFormatException nfe){
			logger.info("Use: sh SicaOMMConsumer.sh opc, Donde opc=1 Para iniciar " +
					"el proceso de actualizacion de precios y opc=2 para iniciar el " +
					"proceso de depuracion de tablas en BD.");
			logger.error(nfe.getMessage(), nfe);
		}catch(Exception er){
			logger.error(er.getMessage(), er);
		}
	
	}
}
