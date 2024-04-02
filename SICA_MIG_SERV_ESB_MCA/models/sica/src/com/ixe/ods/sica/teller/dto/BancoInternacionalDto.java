/*
 * $Id: BancoInternacionalDto.java,v 1.1.4.2 2010/10/08 01:31:04 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica.teller.dto;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Data Transfer Object utilizado en la consulta de Bancos Internacionales.
 *
 * @author Israel Rebollar
 * @version $Revision: 1.1.4.2 $ $Date: 2010/10/08 01:31:04 $
 */
public class BancoInternacionalDto implements Serializable {
	
	/**
	 * Constructor, no hace nada.
	 */
	public BancoInternacionalDto() {
		super();
	}
	
	/**
	 * Inicializa el objeto BancoInternacionalDto con sus valores.
	 * 
	 * @param claveAba La clave ABA del Banco.
	 * @param claveBanxico La Clave Banxico del Banco.
	 * @param claveChips La Clave CHIPS del Banco.
	 * @param claveRuteoPais La Clave de ruteo del pa&iacute;s del Banco.
	 * @param claveSwift La Clave SWIFT del Banco.
	 * @param bancoPequeno Indica si el banco es pequeno.
	 * @param estado El estado del pa&iacute;s del Banco.
	 * @param idBanco El ID del Banco.
	 * @param idBancoPhoenix El ID de Phoenix del Banco.
	 * @param idPais El ID del pa&iacute; del Banco.
	 * @param nombreCorto El nombre corto del Banco.
	 * @param nombrePais El nombre del pa&iacute;s del Banco.
	 * @param numeroRuteoPais El nombre del ruteo del pa&iacute; del Banco.
	 * @param plaza La plaza del Banco.
	 * @param status El status del Banco.
	 */
	public BancoInternacionalDto(String claveAba, String claveBanxico, String claveChips, 
			String claveRuteoPais, String claveSwift, boolean bancoPequeno, 
			String estado, Long idBanco, String idBancoPhoenix, String idPais, 
			String nombreCorto, String nombrePais, String numeroRuteoPais, 
			String plaza, String status){
		
		this();
		this.claveAba = claveAba;
		this.claveBanxico = claveBanxico;
		this.claveChips = claveChips;
		this.claveRuteoPais = claveRuteoPais;
		this.claveSwift = claveSwift;
		this.bancoPequeno = bancoPequeno;
		this.estado = estado;
		this.idBanco = idBanco;
		this.idBancoPhoenix = idBancoPhoenix;
		this.idPais = idPais;
		this.nombreCorto = nombreCorto;
		this.nombrePais = nombrePais;
		this.numeroRuteoPais = numeroRuteoPais;
		this.plaza = plaza;
		this.status = status;
	}

	/**
	 * @return the claveAba
	 */
	public String getClaveAba() {
		return claveAba;
	}
	
	/**
	 * @param claveAba the claveAba to set
	 */
	public void setClaveAba(String claveAba) {
		this.claveAba = claveAba;
	}

	/**
	 * @return the claveBanxico
	 */
	public String getClaveBanxico() {
		return claveBanxico;
	}

	/**
	 * @param claveBanxico the claveBanxico to set
	 */
	public void setClaveBanxico(String claveBanxico) {
		this.claveBanxico = claveBanxico;
	}

	/**
	 * @return the claveChips
	 */
	public String getClaveChips() {
		return claveChips;
	}

	/**
	 * @param claveChips the claveChips to set
	 */
	public void setClaveChips(String claveChips) {
		this.claveChips = claveChips;
	}

	/**
	 * @return the claveRuteoPais
	 */
	public String getClaveRuteoPais() {
		return claveRuteoPais;
	}

	/**
	 * @param claveRuteoPais the claveRuteoPais to set
	 */
	public void setClaveRuteoPais(String claveRuteoPais) {
		this.claveRuteoPais = claveRuteoPais;
	}

	/**
	 * @return the claveSwift
	 */
	public String getClaveSwift() {
		return claveSwift;
	}

	/**
	 * @param claveSwift the claveSwift to set
	 */
	public void setClaveSwift(String claveSwift) {
		this.claveSwift = claveSwift;
	}

	/**
	 * @return the bancoPequeno
	 */
	public boolean isBancoPequeno() {
		return bancoPequeno;
	}

	/**
	 * @param bancoPequeno the bancoPequeno to set
	 */
	public void setBancoPequeno(boolean bancoPequeno) {
		this.bancoPequeno = bancoPequeno;
	}

	/**
	 * @return the estado
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * @param estado the estado to set
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}

	/**
	 * @return the idBanco
	 */
	public Long getIdBanco() {
		return idBanco;
	}

	/**
	 * @param idBanco the idBanco to set
	 */
	public void setIdBanco(Long idBanco) {
		this.idBanco = idBanco;
	}

	/**
	 * @return the idBancoPhoenix
	 */
	public String getIdBancoPhoenix() {
		return idBancoPhoenix;
	}

	/**
	 * @param idBancoPhoenix the idBancoPhoenix to set
	 */
	public void setIdBancoPhoenix(String idBancoPhoenix) {
		this.idBancoPhoenix = idBancoPhoenix;
	}

	/**
	 * @return the idPais
	 */
	public String getIdPais() {
		return idPais;
	}

	/**
	 * @param idPais the idPais to set
	 */
	public void setIdPais(String idPais) {
		this.idPais = idPais;
	}

	/**
	 * @return the nombreCorto
	 */
	public String getNombreCorto() {
		return nombreCorto;
	}

	/**
	 * @param nombreCorto the nombreCorto to set
	 */
	public void setNombreCorto(String nombreCorto) {
		this.nombreCorto = nombreCorto;
	}

	/**
	 * @return the nombrePais
	 */
	public String getNombrePais() {
		return nombrePais;
	}

	/**
	 * @param nombrePais the nombrePais to set
	 */
	public void setNombrePais(String nombrePais) {
		this.nombrePais = nombrePais;
	}

	/**
	 * @return the nombreRuteoPais
	 */
	public String getNumeroRuteoPais() {
		return numeroRuteoPais;
	}

	/**
	 * @param numeroRuteoPais the nombreRuteoPais to set
	 */
	public void setNombreRuteoPais(String numeroRuteoPais) {
		this.numeroRuteoPais = numeroRuteoPais;
	}

	/**
	 * @return the plaza
	 */
	public String getPlaza() {
		return plaza;
	}

	/**
	 * @param plaza the plaza to set
	 */
	public void setPlaza(String plaza) {
		this.plaza = plaza;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	
	/**
     * Regresa una cadena con los nombres y valores de las variables de instancia.
     *
     * @return String.
     */
	public String toString() {
		return new ToStringBuilder(this).append("claveAba", claveAba).
        append("claveBanxico", claveBanxico).append("claveChips", claveChips).
        append("claveRuteoPais", claveRuteoPais).append("claveSwift", claveSwift).
        append("bancoPequeno", bancoPequeno).append("estado", estado).
        append("idBanco", idBanco).append("idBancoPhoenix", idBancoPhoenix).
        append("idPais", idPais).append("nombreCorto", nombreCorto).
        append("nombrePais", nombrePais).append("numeroRuteoPais", numeroRuteoPais).
        append("plaza", plaza).append("status", status).toString();
	}
	
	/**
     * Regresa el hashCode.
     * 
     * @see java.lang.Object#hashCode()
     * @return <code>int</code>
     */
	public int hashCode() {
		return new HashCodeBuilder().append(claveAba).append(claveBanxico).append(claveChips).
        append(claveRuteoPais).append(claveSwift).append(bancoPequeno).append(estado).
        append(idBanco).append(idBancoPhoenix).append(idPais).append(nombreCorto).
        append(nombrePais).append(numeroRuteoPais).append(plaza).append(status).toHashCode();
	}
	
	/**
     * Regresa true si ambos objetos son iguales.
     *
     * @param other El otro objeto a comparar.
     * @see java.lang.Object#equals(java.lang.Object).
     * @return boolean.
     */
	public boolean equals(Object other) {
		if (!(other instanceof BancoInternacionalDto)) {
            return false;
        }
		BancoInternacionalDto castOther = (BancoInternacionalDto) other;
        return new EqualsBuilder().append(this.getIdBanco(), castOther.getIdBanco()).isEquals();
	}

	/**
	 * La clave ABA del banco.
	 */
	private String claveAba;
	
	/**
	 * La clave de Banxico del Banco.
	 */
	private String claveBanxico;
	
	/**
	 * La clave CHIPS del Banco.
	 */
	private String claveChips;
	
	/**
	 * La clave del ruteo del pa&iacute;s del banco.
	 */
	private String claveRuteoPais;
	
	/**
	 * La clave SWIFT del Banco.
	 */
	private String claveSwift;
	
	/**
	 * Indica si es banco pequeno.
	 */
	private  boolean bancoPequeno;
	
	/**
	 * El estado del pa&iacute;s donde se encuentra el Banco.
	 */
	private String estado;
	
	/**
	 * El ID del Banco.
	 */
	private Long idBanco;
	
	/**
	 * El ID de Phoenix del Banco.
	 */
	private String idBancoPhoenix;
	
	/**
	 * El ID del pa&iacute;s donde se encuentra el Banco.
	 */
	private String idPais;
	
	/**
	 * El nombre corto del Banco.
	 */
	private String nombreCorto;
	
	/**
	 * El nombre del pa&acute;s del Banco.
	 */
	private String nombrePais;
	
	/**
	 * El nombre del ruteo del pa&iacute;s del Banco.
	 */
	private String numeroRuteoPais;
	
	/**
	 * La plaza del Banco.
	 */
	private String plaza;
	
	/**
	 * El status del Banco.
	 */
	private String status;
	
	/**
     * UID para serializaci&oacute;n.
     */
	private static final long serialVersionUID = 2138424921337464946L;

}
