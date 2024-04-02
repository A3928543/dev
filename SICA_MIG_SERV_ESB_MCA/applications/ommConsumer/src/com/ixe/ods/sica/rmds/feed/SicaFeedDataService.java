package com.ixe.ods.sica.rmds.feed;

import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ixe.ods.sica.rmds.feed.config.ConnectionPreferences;
import com.ixe.ods.sica.rmds.feed.exception.SicaOmmConsumerException;
import com.ixe.ods.sica.rmds.feed.session.RmdsSession;
import com.ixe.ods.sica.rmds.feed.util.Helper;

import static com.ixe.ods.sica.rmds.feed.util.Constantes.*;
import com.ixe.ods.sica.rmds.process.dao.SicaParametroOMMDao;
import com.ixe.ods.sica.rmds.process.service.OmmConsumerNotificationService;
import com.ixe.ods.sica.rmds.process.service.SicaVariacionService;
import com.ixe.ods.sica.rmds.process.stack.CurrencyPriceStack;

/**
* Inicia la ejecuci&oacute; del cliente OmmConsumer, iniciando 4 principales
* acciones:
* <ul>
* 	<li>Cargar las preferencias para la conexi&oacute;al RMDS.</li>
* 	<li>Enviar el mensaaje de solicitud de login.</li>
* 	<li>Enviar el mensaje de solicitud de Market Data.</li>
* 	<li>Terminar la sesi&oacute;n con el RMDS</li>
* </ul>
* 
* @author Efren Trinidad, Na-at Technologies
* 
*/
@Service
public class SicaFeedDataService {
	
	
	@Autowired
	private SicaParametroOMMDao parametroDao;
	
	@Autowired
	private RmdsSession rmdsSession;
	
	@Autowired
	private SicaVariacionService sicaVariacionService;
	
	@Autowired
	OmmConsumerNotificationService notificacionService;
	
	/**
	 * La instancia de utilidad para grabar mensajes en el log.
	 */
	public static Logger logger = Logger.getLogger(SicaFeedDataService.class.getName());
	
	/**
	 * Constructor sin parametros, inicializa la variable rmdsSession.
	 */
	public SicaFeedDataService(){
	}
	
	/**
	 * Inicializa las dependencias para el inicio de sesi&oacute;n con
	 * el RMDS.
	 * 
	 * @param propertiesFileName
	 * @return 
	 */
	public boolean init(String propertiesFileName) throws SicaOmmConsumerException{
		return ConnectionPreferences.load(propertiesFileName);
	}
	
	/**
	 * 
	 * @param usr
	 * @return
	 * @throws SicaOmmConsumerException
	 */
	public boolean login( String sessionName, String usr, long loginTimeOut ) throws SicaOmmConsumerException{
		return rmdsSession.login(sessionName, usr, loginTimeOut);
	}
	
	public boolean loadDictionary() throws SicaOmmConsumerException{
		return rmdsSession.loadDictionary();
		
	}
	
	public void requestMarketData(String serviceName, String[] rics, String[] interestView, 
			long marketFeedTimeOut, Date marketFeedEndTime ) 
			throws SicaOmmConsumerException{
		
		CurrencyPriceStack currencyPriceStack = rmdsSession.sendRequestMarketData(serviceName, rics, interestView, 
				marketFeedTimeOut, marketFeedEndTime);
		
		sicaVariacionService.senseVariation(currencyPriceStack, true);
		
	}
	
	/**
	 * 
	 */
	public void shutdown() {
		rmdsSession.shutdown();
	}
	
	
	public void startFeedData(){
		
		/*String propertiesFileName = "resources/SICA_RMDSConnectionProperties.xml"; //Connection Properties
		//String dacsUserName = "60_MCA_IDN"; //UserName
		String dacsUserName = "sica"; //UserName
		//String serviceName = "IDN_SELECTFEED"; //Nombre del servicio a conectarse
		String serviceName = "IDN_INTERVAL"; //Nombre del servicio a conectarse
		String rics[] = {"MXN=D2", "EUR=", "JPY=", "GBP=", "CAD=", "CHF="}; //RICs
		//String rics[] = {"EUR="}; //RICs
		long loginTimeOut = 30;
		String interestView[] = {"22","25"}; //RICs
		
		//TODO: Revisar la forma en que se detendra el proceso de alimentacion
		long marketFeedTimeOut = 60;
		String sessionName = "sica::rmdsSession";*/
		try{
		
			String stringType = new String();
			Long longType = new Long("0");
			
			logger.info("Leyendo parametros para conexion al RMDS.");
			
			String propertiesFileName = parametroDao.findPametroOMMById(OMM_CONNECTION_PROPERTIES_FILE_NAME, stringType);
			String dacsUserName = parametroDao.findPametroOMMById(OMM_DACS_USERNAME, stringType);
			String sessionName = parametroDao.findPametroOMMById(OMM_SESSION_NAME, stringType);
			Long loginTimeOut = parametroDao.findPametroOMMById(OMM_LOGIN_TIMEOUT, longType);
			Long marketFeedTimeOut = parametroDao.findPametroOMMById(OMM_MARKETFEED_TIMEOUT, longType);
			String serviceName = parametroDao.findPametroOMMById(OMM_SERVICE_NAME, stringType);
			String[] rics = parametroDao.findPametroOMMById(OMM_RIC_LIST, stringType).split(",");
			String interestView[] = parametroDao.findPametroOMMById(OMM_INTERESTVIEW_LIST, stringType).split(",");
			Date marketFeedEndTime = Helper.hourStringToDate(parametroDao.findPametroOMMById(OMM_MARKETFEED_ENDTIME, stringType));
			
			//Se cargan las propiedades de conexion a las preferencias.
			init(propertiesFileName);
				
			if( ! login(sessionName, dacsUserName, loginTimeOut )){
				logger.error("No se pudo hacer login.");
				return;
			}
			
			if( ! loadDictionary()){
				logger.error("No se pudo cargar el diccionario.");
				return;
			}
			
			requestMarketData(serviceName, rics, interestView, marketFeedTimeOut, marketFeedEndTime);
			
		}catch(SicaOmmConsumerException soce){
			logger.error(soce.getMessage(), soce);
			notificacionService.sendErrNotification(soce);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			notificacionService.sendErrNotification(e);
		}finally{
			shutdown();
		}
		
	}

}
