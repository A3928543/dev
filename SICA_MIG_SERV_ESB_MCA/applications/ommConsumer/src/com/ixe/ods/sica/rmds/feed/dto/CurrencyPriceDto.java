package com.ixe.ods.sica.rmds.feed.dto;

import java.io.Serializable;

public class CurrencyPriceDto implements Serializable {
	
	private Double ask;
	private Double bid;
	private Double mid;
	private String ric;
	
	public Double getAsk() {
		return ask;
	}

	public void setAsk(Double ask) {
		this.ask = ask;
	}

	public Double getBid() {
		return bid;
	}

	public void setBid(Double bid) {
		this.bid = bid;
	}

	public String getRic() {
		return ric;
	}

	public void setRic(String ric) {
		this.ric = ric;
	}
	
	public Double getMid() {
		if(hasAskPrice() && hasBidPrice())
			return (bid + ask)/2;
		return 0.0;
	}

	public void setMid(Double mid) {
		this.mid = mid;
	}
	
	public boolean hasAskPrice(){
		return (ask != null && ask != 0.0);
	}
	
	public boolean hasBidPrice(){
		return (bid != null && bid != 0.0);
	}
	
	public boolean hasRicName(){
		return (ric != null && !ric.equals(""));
	}
	
	public boolean isProcesable(){
		return hasAskPrice() && hasBidPrice() && hasRicName();
	}

	public int hashCode() {
		return ric.hashCode();
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CurrencyPriceDto other = (CurrencyPriceDto) obj;
		if (ric == null) {
			if (other.ric != null)
				return false;
		} else if (!ric.equals(other.ric))
			return false;
		return true;
	}
	
	/**
	 * 
	 */
	public String toString(){
		
		StringBuilder sb = new StringBuilder();
		
		sb.append( "[").append( this.getRic() )
			.append( " BID: ").append(this.getBid())
				.append( " ASK: ").append(this.getAsk() + "]");	
		
		return sb.toString(); 
		
	}

	/**
	 * Identificador de esta versi&oacute;n de la clase.
	 */
	private static final long serialVersionUID = -6491968499775290112L;
}
