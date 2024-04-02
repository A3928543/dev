package com.ixe.ods.sica.rmds.process.service;

import java.util.Map;

import com.ixe.ods.sica.rmds.feed.dto.CurrencyPriceDto;

public interface OmmConsumerNotificationService {

	/**
	 * Env&iacute;a una notificaci&oacute;n v&iacute;a email sobre el 
	 * inicio del proceso de alimentaci&oacute;n de TC al SICA.
	 * 
	 * @param sicaVariacionCache Informaci&oacute;n sobre el TC de inicio
	 * de d&iacute;a
	 * @return true si se env&iacute;a el mail, false de otra manera.
	 */
	public boolean sendInitNotification(
			Map<String, CurrencyPriceDto> sicaVariacionCache);

	/**
	 * Env&iacute;a una notificaci&oacute;n v&iacute;a email sobre el 
	 * fin del proceso de alimentaci&oacute;n de TC al SICA.
	 * 
	 * @param sicaVariacionCache Informaci&oacute;n sobre el TC de fin
	 * de d&iacute;a
	 * @return true si se env&iacute;a el mail, false de otra manera.
	 */
	public boolean sendEndNotification(
			Map<String, CurrencyPriceDto> sicaVariacionCache);

	/**
	 * Env&iacute;a una notificaci&oacute;n v&iacute;a email sobre al&uacute;n  
	 * error ocurrido en proceso de alimentaci&oacute;n de TC al SICA.
	 * 
	 * @param err La exception generada.
	 * @return true si se env&iacute;a el mail, false de otra manera. 
	 */
	public boolean sendErrNotification(Throwable err);

}