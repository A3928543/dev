package com.ixe.ods.sica.rmds.feed.client.impl;

import java.util.Date;

import com.ixe.ods.sica.rmds.feed.client.ProcessEventClient;
import com.ixe.ods.sica.rmds.feed.dto.CurrencyPriceDto;
import com.ixe.ods.sica.rmds.feed.parser.MarketDataParser;
import com.ixe.ods.sica.rmds.process.stack.impl.LinkedListCurrencyPriceStack;
import com.reuters.rfa.common.Event;
import com.reuters.rfa.dictionary.FieldDictionary;
import com.reuters.rfa.omm.OMMMsg;
import com.reuters.rfa.rdm.RDMMsgTypes;
import com.reuters.rfa.session.omm.OMMItemEvent;

/**
 * @author Efren Trinidad, Na-at Technologies
 *
 */
public class MarketDataProcessEventClient extends ProcessEventClient {

	private MarketDataParser marketDataParser;
	
	/**
	 * Constructor por default, establece el tipo de mensajes que escuchar&aacute;
	 * este cliente y el timeOut.
	 * 
	 * @param timeOut La cantidad de milisegundos que este cliente debe esperar
	 * por un mesaje del RMDS.
	 */
	public MarketDataProcessEventClient(FieldDictionary dictionary, long timeOut,
			Date marketFeedEndTime){
		this.msgModelType = RDMMsgTypes.MARKET_PRICE;
		this.timeOut = timeOut;
		this.marketFeedEndTime = marketFeedEndTime;
		this.dictionary = dictionary;
		this.marketDataParser = new MarketDataParser(this.dictionary);
		this.currencyPriceStack = new LinkedListCurrencyPriceStack();
	}

	/**
	 * Implementaci&oacute;n del m&eacute;todo abstracto de com.reuters.rfa.common.Client
	 * 
	 * Procesa los eventos de respuesta por parte del RMDS a la solicitud de login
	 * enviada.
	 * 
	 * @see com.reuters.rfa.common.Client.processEvent(event)
	 */
	public void processEvent(Event event) {
		
		logger.debug("* Evento recibido de tipo: " + event.toString() + "*");
		
		if (event.getType() == Event.COMPLETION_EVENT) {
			logger.debug(": Receive a COMPLETION_EVENT, " + event.getHandle());
			return;
		}
		
        //Solo se procesan eventos que contengan un mensaje OMM.
		if(event.getType() == Event.OMM_ITEM_EVENT){
			
			//Se obtiene le mensaje del wrapper
			OMMItemEvent oOMMItemEvent = (OMMItemEvent)event;
	        OMMMsg respMsg = oOMMItemEvent.getMsg();
	        
	        //msgModelType LOGIN = 1, DIRECTORY = 4, DICTIONARY = 5, MARKET_PRICE = 6
	        logger.info("OMM Model Type: " + RDMMsgTypes.toString(respMsg.getMsgModelType()) +
	        		" MsgType: "+ OMMMsg.MsgType.toString( respMsg.getMsgType()));
        		
			//Solo se lee mensaje si corresponde a la solicitud enviada por este cliente.
        	if (respMsg.getMsgModelType() == this.msgModelType ) {
        			
        		logger.debug(" Indication Flags: " + OMMMsg.Indication.indicationString(respMsg));
        		
        		marketDataParser.setOmmMsg(respMsg);
        		
        		logger.debug( marketDataParser.getMsgInfo() );
        		logger.debug( marketDataParser.payloadToString());
        		
        		CurrencyPriceDto currencyPrice = marketDataParser.parsePayload();
        		currencyPrice.setRic(marketDataParser.getMsqRicName());
        		
        		logger.info(currencyPrice.toString());
        		
        		//Solo se procesa un precio si esta completo y si
        		//hay una variacion respecto a los valores anteriormente 
        		//recibidos.
        		if(currencyPrice.isProcesable()){
        			if(currencyPriceStack.push(currencyPrice, respMsg.getMsgType())){
        				logger.info("Precio procesable...");
        			}else{
        				logger.info("Sin variacion en pila");
        			}
        		}
        		else{
        			logger.info("Precio no procesable, no se informaron los parametros " +
        					"completos");
        		}
        		}else{
        			logger.debug("Este cliente solo puede recibir eventos del modelo: "+
        					RDMMsgTypes.toString(respMsg.getMsgModelType()));
        		}
		}else{
			logger.debug("Tipo de evento no manejado: " + event.getType() );
		}
	
	}
}
