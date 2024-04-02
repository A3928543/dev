package com.ixe.ods.sica.batch.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author IHernandez
 */
@Entity
@Table(name = "SC_DIVISA")
public class ScDivisa implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Basic(optional = false)
	@Column(name = "ID_DIVISA")
	private String idDivisa;
	
	@Basic(optional = false)
	@Column(name = "DESCRIPCION")
	private String descripcion;
	
	@Column(name = "ICONO_FNAME")
	private String iconoFname;
	
	@Column(name = "ID_MONEDA")
	private String idMoneda;
	
	@Basic(optional = false)
	@Column(name = "TIPO")
	private Character tipo;
	
	@Basic(optional = false)
	@Column(name = "ORDEN")
	private BigInteger orden;
	
	@Basic(optional = false)
	@Column(name = "DIVIDE")
	private Character divide;
	
	@Column(name = "DESCRIPCION_EN")
	private String descripcionEn;
	
	@Column(name = "GRUPO")
	private String grupo;
	
	@Column(name = "ID_MONEDA_MINDS")
	private BigInteger idMonedaMinds;
	
	@Column(name = "EQUIVALENCIA_METAL")
	private BigDecimal equivalenciaMetal;
	
	@Column(name = "ID_CECOBAN")
	private String idCecoban;
	
	@Basic(optional = false)
	@Column(name = "SPREAD_SUCURSALES")
	private BigDecimal spreadSucursales;
	
	@Column(name = "ID_DIVISA_ALTAMIRA")
	private String idDivisaAltamira;

	public ScDivisa() {
	}

	public ScDivisa(String idDivisa) {
		this.idDivisa = idDivisa;
	}

	public ScDivisa(String idDivisa, String descripcion, Character tipo,
			BigInteger orden, Character divide, BigDecimal spreadSucursales) {
		this.idDivisa = idDivisa;
		this.descripcion = descripcion;
		this.tipo = tipo;
		this.orden = orden;
		this.divide = divide;
		this.spreadSucursales = spreadSucursales;
	}

	public String getIdDivisa() {
		return idDivisa;
	}

	public void setIdDivisa(String idDivisa) {
		this.idDivisa = idDivisa;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getIconoFname() {
		return iconoFname;
	}

	public void setIconoFname(String iconoFname) {
		this.iconoFname = iconoFname;
	}

	public String getIdMoneda() {
		return idMoneda;
	}

	public void setIdMoneda(String idMoneda) {
		this.idMoneda = idMoneda;
	}

	public Character getTipo() {
		return tipo;
	}

	public void setTipo(Character tipo) {
		this.tipo = tipo;
	}

	public BigInteger getOrden() {
		return orden;
	}

	public void setOrden(BigInteger orden) {
		this.orden = orden;
	}

	public Character getDivide() {
		return divide;
	}

	public void setDivide(Character divide) {
		this.divide = divide;
	}

	public String getDescripcionEn() {
		return descripcionEn;
	}

	public void setDescripcionEn(String descripcionEn) {
		this.descripcionEn = descripcionEn;
	}

	public String getGrupo() {
		return grupo;
	}

	public void setGrupo(String grupo) {
		this.grupo = grupo;
	}

	public BigInteger getIdMonedaMinds() {
		return idMonedaMinds;
	}

	public void setIdMonedaMinds(BigInteger idMonedaMinds) {
		this.idMonedaMinds = idMonedaMinds;
	}

	public BigDecimal getEquivalenciaMetal() {
		return equivalenciaMetal;
	}

	public void setEquivalenciaMetal(BigDecimal equivalenciaMetal) {
		this.equivalenciaMetal = equivalenciaMetal;
	}

	public String getIdCecoban() {
		return idCecoban;
	}

	public void setIdCecoban(String idCecoban) {
		this.idCecoban = idCecoban;
	}

	public BigDecimal getSpreadSucursales() {
		return spreadSucursales;
	}

	public void setSpreadSucursales(BigDecimal spreadSucursales) {
		this.spreadSucursales = spreadSucursales;
	}

	public String getIdDivisaAltamira() {
		return idDivisaAltamira;
	}

	public void setIdDivisaAltamira(String idDivisaAltamira) {
		this.idDivisaAltamira = idDivisaAltamira;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (idDivisa != null ? idDivisa.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof ScDivisa)) {
			return false;
		}
		ScDivisa other = (ScDivisa) object;
		if ((this.idDivisa == null && other.idDivisa != null)
				|| (this.idDivisa != null && !this.idDivisa
						.equals(other.idDivisa))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.ixe.ods.sica.batch.domain.ScDivisa[ idDivisa=" + idDivisa
				+ " ]";
	}

}
