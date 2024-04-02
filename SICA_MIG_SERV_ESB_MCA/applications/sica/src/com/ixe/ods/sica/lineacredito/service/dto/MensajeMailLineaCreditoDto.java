package com.ixe.ods.sica.lineacredito.service.dto;

import java.io.Serializable;

public class MensajeMailLineaCreditoDto implements Serializable 
{
	private static final long serialVersionUID = 1L;
	private String excesoAutorizacionSubject;
	private String excesoAutorizacionBody;
	private String excesoPosteriorSubject;
	private String excesoPosteriorBody;
	private String excesoCuarto;
	private String excesoSexto;
	private String from;
	
	public String getExcesoAutorizacionSubject() {
		return excesoAutorizacionSubject;
	}
	public void setExcesoAutorizacionSubject(String excesoAutorizacionSubject) {
		this.excesoAutorizacionSubject = excesoAutorizacionSubject;
	}
	public String getExcesoAutorizacionBody() {
		return excesoAutorizacionBody;
	}
	public void setExcesoAutorizacionBody(String excesoAutorizacionBody) {
		this.excesoAutorizacionBody = excesoAutorizacionBody;
	}
	public String getExcesoPosteriorSubject() {
		return excesoPosteriorSubject;
	}
	public void setExcesoPosteriorSubject(String excesoPosteriorSubject) {
		this.excesoPosteriorSubject = excesoPosteriorSubject;
	}
	public String getExcesoPosteriorBody() {
		return excesoPosteriorBody;
	}
	public void setExcesoPosteriorBody(String excesoPosteriorBody) {
		this.excesoPosteriorBody = excesoPosteriorBody;
	}
	public String getExcesoCuarto() {
		return excesoCuarto;
	}
	public void setExcesoCuarto(String excesoCuarto) {
		this.excesoCuarto = excesoCuarto;
	}
	public String getExcesoSexto() {
		return excesoSexto;
	}
	public void setExcesoSexto(String excesoSexto) {
		this.excesoSexto = excesoSexto;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
}
