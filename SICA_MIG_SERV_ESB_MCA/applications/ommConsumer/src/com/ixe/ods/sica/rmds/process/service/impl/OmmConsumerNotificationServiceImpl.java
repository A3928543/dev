package com.ixe.ods.sica.rmds.process.service.impl;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import com.ixe.ods.sica.rmds.feed.dto.CurrencyPriceDto;
import com.ixe.ods.sica.rmds.process.service.OmmConsumerNotificationService;

@Service
public class OmmConsumerNotificationServiceImpl implements OmmConsumerNotificationService {
	
	@Autowired
	MailSender mailSender;
	
	@Autowired
	private SimpleMailMessage templateMessage;
	
	/**
	 * La instancia de utilidad para grabar mensajes en el log.
	 */
	private Logger logger = Logger.getLogger(OmmConsumerNotificationServiceImpl.class.getName());

	/**
	 * @see com.ixe.ods.sica.rmds.process.service.impl.OmmConsumerNotificationService#sendInitNotification(java.util.Map)
	 */
	public boolean sendInitNotification(Map<String, CurrencyPriceDto> sicaVariacionCache){
		
		try{
			SimpleMailMessage mail = new SimpleMailMessage(templateMessage);
			StringBuilder mailText = new StringBuilder();
			
			mailText.append("Inicio del proceso de alimentacion de TC al SICA. \n\n");
			
			for(CurrencyPriceDto currencyPrice : sicaVariacionCache.values()){
				mailText.append("\t").append(currencyPrice.toString()).append("\n");
			}
			
			mail.setText(mailText.toString());
			mailSender.send(mail);
		
		}catch(MailException me){
			logger.warn(me.getMessage(), me);
			return false;
		}
		return true;
	}
	
	/**
	 * @see com.ixe.ods.sica.rmds.process.service.impl.OmmConsumerNotificationService#sendEndNotification(java.util.Map)
	 */
	public boolean sendEndNotification(Map<String, CurrencyPriceDto> sicaVariacionCache){
		
		try{
			SimpleMailMessage mail = new SimpleMailMessage(templateMessage);
			StringBuilder mailText = new StringBuilder();
			
			mailText.append("Fin del proceso de alimentacion de TC al SICA. \n\n");
			
			for(CurrencyPriceDto currencyPrice : sicaVariacionCache.values()){
				mailText.append("\t").append(currencyPrice.toString()).append("\n");
			}
			
			mail.setText(mailText.toString());
			mailSender.send(mail);
		
		}catch(MailException me){
			logger.warn(me.getMessage(), me);
			return false;
		}
		return true;
		
	}
	
	/** 
	 * @see com.ixe.ods.sica.rmds.process.service.impl.OmmConsumerNotificationService#sendErrNotification(java.lang.Exception)
	 */
	public boolean sendErrNotification(Throwable err){
		try{
			SimpleMailMessage mail = new SimpleMailMessage(templateMessage);
			StringBuilder mailText = new StringBuilder();
			
			mailText.append("Error en OMMConsumer ");
			mailText.append(err.getMessage());
			for(StackTraceElement traceElement : err.getStackTrace()){
				mailText.append(traceElement).append("\n");
			}
			
			mail.setText(mailText.toString());
			mailSender.send(mail);
		
		}catch(MailException me){
			logger.warn(me.getMessage(), me);
			return false;
		}
		return true;
	}
}
