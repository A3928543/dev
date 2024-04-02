package com.ixe.ods.sica.rmds.feed.session.impl;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.ixe.ods.sica.rmds.feed.client.ProcessEventClient;
import com.ixe.ods.sica.rmds.feed.client.impl.LoginProcessEventClient;
import com.ixe.ods.sica.rmds.feed.client.impl.MarketDataProcessEventClient;
import com.ixe.ods.sica.rmds.feed.exception.SicaOmmConsumerException;
import com.ixe.ods.sica.rmds.feed.session.RmdsSession;
import com.ixe.ods.sica.rmds.process.stack.CurrencyPriceStack;
import com.reuters.rfa.common.Context;
import com.reuters.rfa.common.EventQueue;
import com.reuters.rfa.common.EventSource;
import com.reuters.rfa.common.Handle;
import com.reuters.rfa.dictionary.DictionaryException;
import com.reuters.rfa.dictionary.FieldDictionary;
import com.reuters.rfa.omm.OMMElementList;
import com.reuters.rfa.omm.OMMEncoder;
import com.reuters.rfa.omm.OMMMsg;
import com.reuters.rfa.omm.OMMPool;
import com.reuters.rfa.omm.OMMTypes;
import com.reuters.rfa.rdm.RDMInstrument;
import com.reuters.rfa.rdm.RDMMsgTypes;
import com.reuters.rfa.rdm.RDMUser;
import com.reuters.rfa.session.Session;
import com.reuters.rfa.session.omm.OMMConsumer;
import com.reuters.rfa.session.omm.OMMItemIntSpec;

/**
 * @author Efren Trinidad, Na-at Technologies
 *
 */
@Component
public class SicaRmdsSession implements RmdsSession {

	private Handle loginHandle;
	private OMMConsumer ommConEventSource;
	private ProcessEventClient eventClient;
	private Session session;
	private EventQueue eventQueue;
	private LinkedList<Handle> ricHandles;
	private FieldDictionary dictionary;
	
	/**
	 * Fabrica del objetos OMM, almacena objetos omm frecuentemente usados
	 * para efectos de uso de memoria eficiente.
	 */
	private OMMPool ommPool;
	
	/**
	 * La instancia de utilidad para grabar mensajes en el log.
	 */
	private static Logger logger = Logger.getLogger(SicaRmdsSession.class.getName());
	
	/**
	 * Constructor sin par&aacute;metros, no hace nada.
	 */
	public SicaRmdsSession(){
	}
	
	/**
	 * Inicializa las instancias de <code>com.reuters.rfa.session.Session</code>, 
	 * <code>com.reuters.rfa.common.EventSource</code> y <code>com.reuters.rfa.common.EventQueue</code>
	 * que utilizar&aacute; el consumidor que inicia proceso de suscripci&oacute;n al RMDS.
	 
	 * @param sessionName El nombre de la sesi&oacute;n configurada en las Java Preferences.
	 * @return La instancia de <code>com.reuters.rfa.session.omm.OMMConsumer</code> donde se podr&aacute;n
	 * depositar mensajes OMM a enviar el RMDS.
	 * @throws SicaOmmConsumerException Si algo sale mal.
	 */
	private OMMConsumer init(String sessionName) throws SicaOmmConsumerException{
		
		logger.debug("Inicializando el contexto.");
		Context.initialize();
		
		logger.info("Obteniendo una sesion para: " + sessionName);
		session = Session.acquire(sessionName);
			if(session == null){
				throw new SicaOmmConsumerException("No fue posible crear la sesion para: " + sessionName);
			}
		
		logger.debug("Se crea el EventSource OMM");
		//TODO Revisar el uso y los valores validos para el parametro
		//'name' en la creacion del EventSource
		ommConEventSource = (OMMConsumer) session.createEventSource(EventSource.OMM_CONSUMER, "sicaOMMEventSource");
			if(ommConEventSource == null){
				throw new SicaOmmConsumerException("No se pudo crear el EventSource para la sesion: " + session);
			}
			
		//TODO Revisar el uso y los valores valido para el parametro
		//'name' en la creacion del EventQueue
		logger.debug("Creando el EventQueue");
		eventQueue = EventQueue.create("eventQueue");
			if(eventQueue == null){
				throw new SicaOmmConsumerException("No se pudo crear el EventQueue para session: " + session);
			}
			
		return ommConEventSource;
	}
	
	
	
	/**
	 * @see com.ixe.ods.sica.rmds.feed.session.RmdsSession#login(java.lang.String, java.lang.String, long)
	 */
	public boolean login(String sessionName, String usr, long loginTimeOut ) throws SicaOmmConsumerException{
		
		logger.info("Haciendo login con usuario " + usr + " por " + loginTimeOut + "seg." );
		
		eventClient = new LoginProcessEventClient(loginTimeOut); 
			
		//Se crean las intancias de Session, EventSource y EvenQueue.
		ommConEventSource = init(sessionName);
		
		OMMMsg loginMsg = createLoginReqMsg(usr);
		OMMItemIntSpec ommItemIntSpec = new OMMItemIntSpec();
		ommItemIntSpec.setMsg(loginMsg);
		
		logger.debug("Enviando solicitud de login");
		loginHandle = ommConEventSource.registerClient(this.eventQueue, ommItemIntSpec, eventClient, null);
		
		//Se establece el Queue en el se depositaran los eventos de login
		//y a la cual debe escuchar el cliente.
		eventClient.setEventQueue(this.eventQueue);
		
		logger.debug("Se inicia el depachador de eventos de login");
		eventClient.start();
		
		//Se espera hasta el que hilo que esta escuchando los
		//eventos de login notifique que ha recibido una respuesta 
		//a la solicitud enviada.
		try {
            synchronized (eventClient) {
            	while(!eventClient.isCompleted()){
            		eventClient.wait(500);
            	}
            }
        } catch (InterruptedException ie) {
        	throw new SicaOmmConsumerException("Error esperando resolver la solicitud de login: " + ie);
        }
        
        //Si no se logro la conexion, se genera una exception para enviar
        //una notificacion.
        if( !eventClient.isConnected()){
        	throw new SicaOmmConsumerException( eventClient.getErrMsg().getMessage(), 
        			eventClient.getErrMsg() );
        }
        
        return eventClient.isConnected();
	}
	
	
	/** 
	 * @see com.ixe.ods.sica.rmds.feed.session.RmdsSession#loadDictionary()
	 */
	public boolean loadDictionary() throws SicaOmmConsumerException{
		
		//TODO Modificar esta implemtacion para 
		//cargar el Dictionary desde el RMDS.
		
		String fieldDictionaryFilename = "resources/RDM/RDMFieldDictionary";
    	String enumDictionaryFilename = "resources/RDM/enumtype.def";
    	
    	try {
    		dictionary = FieldDictionary.create();
                FieldDictionary.readRDMFieldDictionary(dictionary, fieldDictionaryFilename);
            	System.out.println("field dictionary read from RDMFieldDictionary file");

                FieldDictionary.readEnumTypeDef(dictionary, enumDictionaryFilename);
                System.out.println("enum dictionary read from enumtype.def file");
                
    	}
    	catch (DictionaryException ex) {
    		throw new SicaOmmConsumerException("ERROR: Unable to initialize dictionaries: " + ex.getMessage(), ex);
    	}
    	
    	return true;
	}
	
	
	/* (non-Javadoc)
	 * @see com.ixe.ods.sica.rmds.feed.session.RmdsSession#sendRequestMarketData(java.lang.String, java.lang.String[], 
	 *  java.lang.String[], long, long)
	 */
	public CurrencyPriceStack sendRequestMarketData(String serviceName, String[] rics, 
			String[] interestView, long marketFeedTimeOut, Date marketFeedEndTime) 
				throws SicaOmmConsumerException{
		logger.info("Enviando solicitud de datos para serviceName=" + serviceName);
		
		//Se inicializa la lista que contendra en Handle para la suscripcion
		//a cada ric.
		ricHandles = new LinkedList<Handle>();
		
		if(this.loginHandle == null){
			throw new SicaOmmConsumerException("Antes de suscribirse a un RIC se debe hacer login correctamente");
		}
		
		//Se crea el mensaje con los valores comunes para la suscripcion cada uno de los
		//rics
		OMMMsg ommMsgMarketDataReq = null;
		OMMItemIntSpec ommItemIntSpec = null;
		
		//
		this.eventClient = new MarketDataProcessEventClient(dictionary, marketFeedTimeOut, marketFeedEndTime);
		
		//Se itera la lista de rics para configurar el mensaje y hacer la suscripcion
		//a cada uno.
		for(String ric : rics){
			ommMsgMarketDataReq = createMarketDataRequestMsg(interestView, serviceName, ric);
			ommItemIntSpec = new OMMItemIntSpec();
			ommItemIntSpec.setMsg(ommMsgMarketDataReq);
			logger.info("Enviando suscripcion a serviceName: " + serviceName + " ric: " + ric
					+ " viewData: " + Arrays.toString(interestView));
			ricHandles.add( ommConEventSource.registerClient(eventQueue, ommItemIntSpec, eventClient, null));
			//Se regresa el mensaje al pool
			ommPool.releaseMsg(ommMsgMarketDataReq);
		}
		
		eventClient.setEventQueue(eventQueue);
		
		logger.debug("Se inicia el depachador de eventos de Market Data");
		eventClient.start();
		
		//Se retorna el Stack donde se depositaran los precios por el cliente.
		return eventClient.getCurrencyPriceStack();
		
	}
	
	/**
	 * Crea el objeto Omm que representa el mensaje de login.
	 * 
	 * @param usr El nombre de usuario con el cual se har&aacute; login en el RMDS.
	 * @return El objeto Omm del mensaje de login.
	 */
	private OMMMsg createLoginReqMsg(String usr){
		
		logger.debug("Creando mensaje de login para usuario: " + usr);
		
		ommPool = OMMPool.create();
		OMMMsg loginMsg = ommPool.acquireMsg();
		//InteractionType: Streaming request is required before any other requests.
		loginMsg.setMsgType(OMMMsg.MsgType.STREAMING_REQ);
		loginMsg.setMsgModelType(RDMMsgTypes.LOGIN);
		loginMsg.setAttribInfo(null, usr, RDMUser.NameType.USER_NAME);
		
		OMMEncoder ommEncoder = ommPool.acquireEncoder();
		
		//Se define el tipo de mensaje a OMMMsg y el tamano
		//del bufer payload
		ommEncoder.initialize(OMMTypes.MSG, 500);
		ommEncoder.encodeMsgInit(loginMsg, OMMTypes.ELEMENT_LIST, OMMTypes.NO_DATA);
		ommEncoder.encodeElementListInit(OMMElementList.HAS_STANDARD_DATA, (short)0, (short) 0);
		ommEncoder.encodeElementEntryInit(RDMUser.Attrib.ApplicationId, OMMTypes.ASCII_STRING);
		//TODO CommandLine.variable( "application")
		//Revisar el uso y los valores permitidos para este parametro
		ommEncoder.encodeString("256", OMMTypes.ASCII_STRING);
		ommEncoder.encodeElementEntryInit(RDMUser.Attrib.Position, OMMTypes.ASCII_STRING);
		//TODO CommandLine.variable("position")
		//Revisar el uso y los valores permitidos para este parametro
		
		String position = null;
		try {
			position = InetAddress.getLocalHost().getHostAddress()
			+ "/" + InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			logger.error(e);
		}
		
		ommEncoder.encodeString(position, OMMTypes.ASCII_STRING);
		ommEncoder.encodeAggregateComplete();
		//Get the encoded message from the _ommEncoder
		OMMMsg encMsg = (OMMMsg)ommEncoder.getEncodedObject();
		//Release the message that is owned by the application
		ommPool.releaseMsg(loginMsg);
		
		return encMsg; //return the encoded message
	}
	
	
	private OMMMsg createMarketDataRequestMsg( String[] viewData, String serviceName, String ric){
		
		logger.debug("Se crea el ReqMsg para serviceName: " + serviceName + " ric: " + ric
				+ " viewData: " + Arrays.toString(viewData));
		
		OMMMsg ommmsg = ommPool.acquireMsg();
		ommmsg.setMsgType(OMMMsg.MsgType.STREAMING_REQ);
		ommmsg.setMsgModelType(RDMMsgTypes.MARKET_PRICE);
		ommmsg.setPriority((byte) 1, 1);
		ommmsg.setAssociatedMetaInfo(this.loginHandle);
		ommmsg.setAttribInfo(serviceName, ric, RDMInstrument.NameType.RIC);
		ommmsg.setIndicationFlags(OMMMsg.Indication.ATTRIB_INFO_IN_UPDATES | OMMMsg.Indication.VIEW);
		
		OMMEncoder ommEncoder = ommPool.acquireEncoder();
		
		ommEncoder.initialize(OMMTypes.MSG, 500);
		ommEncoder.encodeMsgInit(ommmsg, OMMTypes.NO_DATA, OMMTypes.ELEMENT_LIST);
		ommEncoder.encodeElementListInit( OMMElementList.HAS_STANDARD_DATA, (short)0, (short)0);
		//Encode View Request
		//Por default el viewType se codifica como RDMUser.View.FIELD_ID_LIST
		ommEncoder.encodeElementEntryInit(RDMUser.View.ViewType, OMMTypes.UINT32);
		ommEncoder.encodeUInt64(RDMUser.View.FIELD_ID_LIST);
		ommEncoder.encodeElementEntryInit(RDMUser.View.ViewData, OMMTypes.ARRAY);
		//As type for FID is short, size of 2 for array entry is sufficient for FIELD_ID_LIST view data
		ommEncoder.encodeArrayInit(OMMTypes.UINT32, 2);
		
		for(String fldId : viewData)
		{
			ommEncoder.encodeArrayEntryInit();
			ommEncoder.encodeUInt64(new Integer(fldId).intValue());
		}
		ommEncoder.encodeAggregateComplete(); //completes the array
		
		ommEncoder.encodeAggregateComplete(); //completes the element list
		
		logger.debug("Mensaje creado...");
		
		return (OMMMsg)ommEncoder.getEncodedObject();
		
	}
	
	
	/*private OMMMsg createMarketDataRequestMsg( int[] interestView ){
		
		OMMMsg ommmsg = ommPool.acquireMsg();
		OMMEncoder ommEncoder = ommPool.acquireEncoder();

        ommmsg.setMsgType(OMMMsg.MsgType.STREAMING_REQ);
        ommmsg.setMsgModelType(RDMMsgTypes.MARKET_PRICE);
        ommmsg.setPriority((byte) 1, 1);
        ommmsg.setAssociatedMetaInfo(this.loginHandle);
        //Para recibir Attr Info en cada update Msg Type, no enviar en produccion
        int indicationFlags = OMMMsg.Indication.ATTRIB_INFO_IN_UPDATES;
        
        //Solo si se informa los id para una vista se agrega el payload
        //al mensaje
        if(interestView != null && interestView.length > 0){
        
        	indicationFlags |= OMMMsg.Indication.VIEW;
        	
        	//Se define el tipo de mensaje y tamano del buffer
        	//payload
        	ommEncoder.initialize(OMMTypes.MSG, 500);
        	//TODO: Revisar por que este punto se configura asi.
        	ommEncoder.encodeMsgInit(ommmsg, OMMTypes.NO_DATA, OMMTypes.ELEMENT_LIST);
        	ommEncoder.encodeElementListInit( OMMElementList.HAS_STANDARD_DATA, (short)0, (short)0);
        
        	
        	for(int fid : interestView){
        	
        	}
        }
        ommmsg.setIndicationFlags();
        
        return ommmsg;
		
	}*/
	
	
	/* (non-Javadoc)
	 * @see com.ixe.ods.sica.rmds.feed.session.RmdsSession#shutdown()
	 */
	public void shutdown(){
		logger.info("Finalizando la session");
		
		if(eventClient != null){
			this.eventClient.shutdown();
		}
		
		if(ommConEventSource != null){
			 if (loginHandle == null) {
				 	ommConEventSource.unregisterClient(loginHandle);
	                loginHandle = null;
	            }
			 
			 if(ricHandles != null){
				 for( Handle ric : ricHandles){
					 ommConEventSource.unregisterClient(ric);
					 ric = null;
				 }
			 }
			 ommConEventSource.destroy();
			 ommConEventSource = null;
		}
		if (session != null) {
            session.release();
            session = null;
        }
		logger.info("Session finalizada.");
	}
	
}
