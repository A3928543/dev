package com.ixe.ods.sica.components;

import java.text.ParseException;

import org.apache.tapestry.form.TextField;

import com.ixe.ods.sica.Constantes;

/**
 * Text Field para desplegar montos
 * 
 * @author Cesar Jeronimo Gomez
 */
public abstract class CurrencyTextField extends TextField {

	public String readValue() {
		try {
			return Constantes.MONEY_FORMAT.format(getValueBinding().getDouble());
		} catch (RuntimeException e) {
			return "0.00";
		}
	}

	public void updateValue(String value) {
		try {
			Number number = Constantes.MONEY_FORMAT.parse(value);
			getValueBinding().setDouble(number.doubleValue());
		} catch (RuntimeException e) {
			getValueBinding().setDouble(0);
		} catch (ParseException e) {
			getValueBinding().setDouble(0);
		}
	}

}
