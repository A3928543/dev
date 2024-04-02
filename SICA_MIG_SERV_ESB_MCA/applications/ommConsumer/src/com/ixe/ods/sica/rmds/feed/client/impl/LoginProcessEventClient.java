package com.ixe.ods.sica.rmds.feed.client.impl;

import com.ixe.ods.sica.rmds.feed.client.ProcessEventClient;
import com.ixe.ods.sica.rmds.feed.parser.MarketDataParser;
import com.reuters.rfa.common.Event;
import com.reuters.rfa.omm.OMMMsg;
import com.reuters.rfa.omm.OMMState;
import com.reuters.rfa.rdm.RDMMsgTypes;
import com.reuters.rfa.session.omm.OMMItemEvent;

/**
 * @author Efren Trinidad, Na-at Technologies.
 *
 */
public class LoginProcessEventClient extends ProcessEventClient{
	
	/**
	 * Constructor por default, establece el tipo de mensajes que escuchar&aacute;
	 * este cliente y timeOut para este cliente
	 * 
	 * @param timeOut La cantidad de milisegundos que este cliente debe esperar
	 * por un mesaje del RMDS.
	 */
	public LoginProcessEventClient(long timeOut){
		this.msgModelType = RDMMsgTypes.LOGIN;
		this.timeOut = timeOut;
	}
	
	/**
	 * Implementaci&oacute;n del m&eacute;todo abstracto de com.reuters.rfa.common.Client
	 * 
	 * Procesa los eventos de respuesta por parte del RMDS a la solicitud de login
	 * enviada.
	 * 
	 * Si la solicitud de login es aceptada, el RMDS debe responder con un
	 * MsgType = OMMMsg.MsgType.REFRESH_RESP, con RespStatus.StreamState = Open,
	 * RespStatus.DataState = Ok, RespStatus.StatusCode = None.
	 * 
	 * @see com.reuters.rfa.common.Client.processEvent(event)
	 */
	public void processEvent(Event event){
		
		MarketDataParser marketDataParser = new MarketDataParser();	
		
		logger.info("Evento recibido de tipo: " + event.toString());
		
        //Solo se procesan eventos que contengan un mensaje OMM.
		if(event.getType() == Event.OMM_ITEM_EVENT){
			
			//Se obtiene le mensaje del wrapper
			OMMItemEvent oOMMItemEvent = (OMMItemEvent)event;
	        OMMMsg respMsg = oOMMItemEvent.getMsg();
	        
	        //msgModelType LOGIN = 1, DIRECTORY = 4, DICTIONARY = 5, MARKET_PRICE = 6
	        logger.debug("Evento OMM de tipo: " + RDMMsgTypes.toString(respMsg.getMsgModelType()));
        		
			//Para la respuesta de lo solicitud de login.
        	if (respMsg.getMsgModelType() == this.msgModelType ) {
        			
        		logger.info("Evento de login " + marketDataParser.getMsgLoginInfo(respMsg) );
        		
        		if ((respMsg.getMsgType() == OMMMsg.MsgType.REFRESH_RESP) && 
        				(respMsg.getRespTypeNum() == OMMMsg.RespType.SOLICITED) &&
	                    	(respMsg.getState().getStreamState() == OMMState.Stream.OPEN) &&
	                    		(respMsg.getState().getDataState() == OMMState.Data.OK) ) {
        			
	                	logger.info("Login Exitoso...conectado");
	                	this.connected = true;
	                	
	                }
        		
        		if (respMsg.isFinal()) {
	                	logger.info("Login no exitoso...desconectado");
	                	this.connected = false;;
	                }
        		
	                completed = true;
        		}else{
        			logger.debug("Este cliente solo puede recibir eventos del modelo: "+
        					RDMMsgTypes.toString(respMsg.getMsgModelType()));
        		}
        		
        		if( !connected ){
        			throw new RuntimeException(marketDataParser.getMsgLoginInfo(respMsg));
        		}
        	
		}else{
			logger.debug("Tipo de evento no manejado: " + event.getType() );
		}
		
	}
}