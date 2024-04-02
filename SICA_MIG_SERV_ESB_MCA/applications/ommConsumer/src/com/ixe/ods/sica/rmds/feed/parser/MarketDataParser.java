package com.ixe.ods.sica.rmds.feed.parser;

import java.util.Iterator;

import org.apache.log4j.Logger;

import com.ixe.ods.sica.rmds.feed.dto.CurrencyPriceDto;
import com.ixe.ods.sica.rmds.feed.util.Helper;
import com.reuters.rfa.dictionary.FidDef;
import com.reuters.rfa.dictionary.FieldDictionary;
import com.reuters.rfa.omm.OMMAttribInfo;
import com.reuters.rfa.omm.OMMData;
import com.reuters.rfa.omm.OMMEntry;
import com.reuters.rfa.omm.OMMFieldEntry;
import com.reuters.rfa.omm.OMMFieldList;
import com.reuters.rfa.omm.OMMIterable;
import com.reuters.rfa.omm.OMMMsg;
import com.reuters.rfa.omm.OMMNumeric;
import com.reuters.rfa.omm.OMMState;
import com.reuters.rfa.omm.OMMTypes;
import com.reuters.rfa.rdm.RDMMsgTypes;
import static com.ixe.ods.sica.rmds.feed.util.Constantes.*;

/**
 * Clase de utilidad. Implementa m&eacute;todos para la obtenci&oacute;n
 * de informaci&oacute;n en mensajes OMM.
 * 
 * @author Efren Trinidad, Na-at Technologies.
 *
 */
public class MarketDataParser {

	private OMMMsg ommMsg;
	
	private FieldDictionary dictionary;
	
	public static Logger logger = Logger.getLogger(MarketDataParser.class.getName());
	
	/**
	 * Constructor por default, no hace algo.
	 */
	public MarketDataParser(){
	}
	
	/**
	 * Constructor que inicializa el diccionario con el que se deben
	 * interpretar los datos en un mensaje OMM.
	 * 
	 * @param dictionary Dicccionario de datos en mensajes OMM.
	 */
	public MarketDataParser(FieldDictionary dictionary){
		this.dictionary = dictionary;
	}
	
	/**
	 * Revisa la informaci&oacute;n existente en el mensaje y arma una cadena
	 * representativa.
	 * 
	 * @return Informaci&oacute;n acerca del mensaje.
	 */
	public String getMsgInfo(){
		
		StringBuilder sb = new StringBuilder();
		
		//Indentifica el mensaje recibido, contiene practiamente
		//la informacion enviada en el ReqMsg.
		if(ommMsg.has(OMMMsg.HAS_ATTRIB_INFO))
    	{
			OMMAttribInfo attribInfo = ommMsg.getAttribInfo();
			sb.append( "HAS_ATTRIB_INFO = ");
			sb.append( "ServiceName: " + attribInfo.getServiceName());
			sb.append( " RIC: " + attribInfo.getName());
			sb.append( " \n");
    	}
		
		if(logger.isDebugEnabled()){
			if(ommMsg.has(OMMMsg.HAS_STATE))
	    	{
				sb.append( "HAS_STATE= " + ommMsg.getState() );
				sb.append( " | \n");
	    	}
			
			if(ommMsg.getDataType() != OMMTypes.NO_DATA){
				sb.append( "PAYLOAD= " + ommMsg.getPayload().getEncodedLength() + " bytes ");
				
				if(ommMsg.getPayload().getType() == OMMTypes.FIELD_LIST ){
					sb.append( " Dictionary ID: " +((OMMFieldList)ommMsg.getPayload()).getDictId());
				}
			}
		}
		
    	return sb.toString();
	}
	
	/**
	 * Extrae la informaci&oacute;n en un mensaje de respuesta 
	 * a una solicitud de login y retornar una cadena representativa.
	 * 
	 * @param respLoginMsg El mensaje OMM de respuesta a una solicitud de login.
	 * @return Cadena representativa del mensaje.
	 */
	public String getMsgLoginInfo( OMMMsg respLoginMsg ){
		
		StringBuilder sb = new StringBuilder();
		
		sb.append(" MsgType: ").append(OMMMsg.MsgType.toString(respLoginMsg.getMsgType())).append("\n");
		sb.append(" RespTypeNum : ").append(OMMMsg.RespType.toString(respLoginMsg.getRespTypeNum())).append("\n");
		sb.append(" StreamState : ").append(OMMState.Stream.toString(respLoginMsg.getState().getStreamState())).append("\n");
		sb.append(" DataState : ").append(OMMState.Data.toString(respLoginMsg.getState().getDataState())).append("\n");
		sb.append(" State : ").append(respLoginMsg.getState()).append("\n");
		
		return sb.toString();
	}
	
	/**
	 * Retorna el nombre del RIC al que corresponde el mensaje tomandolo
	 * de OMMAttribInfo.name
	 * 
	 * @return El nombre del RIC
	 */
	public String getMsqRicName(){
		
		if(ommMsg.has(OMMMsg.HAS_ATTRIB_INFO))
    	{
			OMMAttribInfo attribInfo = ommMsg.getAttribInfo();
			return attribInfo.getName();
    	}
		
		return "";
		
	}
	
	
	/**
	 * Obtiene una cadena representativa de los datos contenidos en un
	 * mensaje (payload).
	 * 
	 * @return
	 */
	public String payloadToString(){
		
		StringBuilder sb = new StringBuilder();
		
		//Se verifica si el tipo de datos contenido en el mensaje
		//se puede tratar, es uno de FIELD_LIST,ELEMENT_LIST, VECTOR, FILTER_LIST, SERIES, MAP, and ARRAY
		if(ommMsg.getDataType() != OMMTypes.NO_DATA && OMMTypes.isAggregate(ommMsg.getPayload().getType())){
			
			OMMData ommData = ommMsg.getPayload();
			
			if(ommData.getType() == OMMTypes.FIELD_LIST){
				
				sb.append("\t FIELD_LIST \n");
				
				//Si la estructura de datos en el mensaje es de tipo FIELD_LIST, 
				//se itera para obtener cada entrada
				for (Iterator iter = ((OMMIterable)ommData).iterator(); iter.hasNext();)
		        {
					//Se obtiene la forma generica de cada elemento en la lista.
		            OMMEntry entry = (OMMEntry) iter.next();
		            	
		            if(entry.getType() == OMMTypes.FIELD_ENTRY){
		            	//Se obtiene el tipo especifico del elemento, un OMMFieldEntry
		            	//es identificado por un Field ID, que debe ser buscado en el diccionario
		            	//para verificar el tipo de dato del elemento.
		            	OMMFieldEntry fe = (OMMFieldEntry) entry;
		            	
		            	//fiddef - Definicion del tipo de dato y longitud
		            	FidDef fiddef = dictionary.getFidDef(fe.getFieldId());
		        	   
		            	if (fiddef != null){
		            		
		            		sb.append("\t" + fe.getFieldId() + "/" + fiddef.getName() + " " + fe.getData(fiddef.getOMMType()) + "\n");
		            		
		            	}
		            }
		        }	
			}
		}
		return sb.toString();
	}
	
	/**
	 * Obtiene los valores BID y ASK de determinada divisa en un mensaje OMM
	 * y los deposita en el objeto que retorna.
	 * 
	 * @return  DTO con los valores BID y ASK de determinada divisa. 
	 */
	public CurrencyPriceDto parsePayload(){
		
		logger.debug("Se parsea en contenido del mensaje " + RDMMsgTypes.toString(ommMsg.getMsgModelType()));
		
		CurrencyPriceDto currencyPrice = new CurrencyPriceDto();
		
		//Se evalua si el mensaje contiene paquete de datos
		if (this.ommMsg.getDataType() != OMMTypes.NO_DATA)
        {
			
			OMMData msgPayload = ommMsg.getPayload();
			OMMData fieldEntryData;
			
			//OMMMsg's Data is a simple list of field-value pairs. 
			//This is typically a FieldList.
			if(OMMTypes.isAggregate(msgPayload.getType()) && 
					msgPayload.getType() == OMMTypes.FIELD_LIST ){
				
				logger.debug("Mensaje con datos, msgPayload.type: " + OMMTypes.FIELD_LIST);
				
				//Si la estructura de datos en el mensaje es de tipo FIELD_LIST, 
				//se itera para obtener cada entrada
				for (Iterator iter = ((OMMIterable)msgPayload).iterator(); iter.hasNext();)
		        {
					//Se obtiene la forma generica de cada elemento en la lista.
					OMMEntry entry = (OMMEntry) iter.next();
		            	
		            if(entry.getType() == OMMTypes.FIELD_ENTRY){
		            	//Se obtiene el tipo especifico del elemento, un OMMFieldEntry
		            	//es identificado por un Field ID, que debe ser buscado en el diccionario
		            	//para verificar el tipo de dato del elemento.
		            	OMMFieldEntry fe = (OMMFieldEntry) entry;
		            	
		            	//fiddef - Definicion del tipo de dato y longitud
		            	FidDef fiddef = dictionary.getFidDef(fe.getFieldId());
		            	
		            	if (fiddef != null){
		            		
		            		fieldEntryData = fe.getData(fiddef.getOMMType());
		            		
		            		switch(fe.getFieldId()){
		            		
		            			case BID_FID:
		            				logger.debug("Leyendo precio BID " + fieldEntryData);
		            				currencyPrice.setBid(Helper.redondear6Dec((((OMMNumeric)fieldEntryData).toDouble())));
		            				break;
		            			case ASK_FID:
		            				logger.debug("Leyendo precio ASK " + fieldEntryData);
		            				currencyPrice.setAsk(Helper.redondear6Dec(((OMMNumeric)fieldEntryData).toDouble()));
		            				break;
		            			default:
		            				break;
		            		}
		            	}
		            }
		        }	
			}
		}else{
			logger.debug("Mensaje sin datos");
		}
		return currencyPrice;
	}
	
	
	public Double getBid(){
		return new Double(0);
	}
	
	public Double getAsk(){
		return new Double(0);
	}
	
	public OMMMsg getOmmMsg() {
		return ommMsg;
	}

	public void setOmmMsg(OMMMsg ommMsg) {
		this.ommMsg = ommMsg;
	}
}
