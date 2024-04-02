package com.ixe.ods.sica.vo;

/**
 * Clase para el transporte de datos entre el delegado y la vista en Flex
 * de informaci&oacute;n de negociacia&oacute;n.
 *  
 * @author Efren Trinidad, Na-at Technologies
 *
 */
public class InformacionNegociacionVO {
	
	/**
	 * @return the idContrato
	 */
	public int getIdContrato() {
		return idContrato;
	}

	/**
	 * @param idContrato the idContrato to set
	 */
	public void setIdContrato(int idContrato) {
		this.idContrato = idContrato;
	}

	/**
	 * @return the nombreCompleto
	 */
	public String getNombreCompleto() {
		return nombreCompleto;
	}

	/**
	 * @param nombreCompleto the nombreCompleto to set
	 */
	public void setNombreCompleto(String nombreCompleto) {
		this.nombreCompleto = nombreCompleto;
	}

	/**
	 * @return the noCuenta
	 */
	public String getNoCuenta() {
		return noCuenta;
	}

	/**
	 * @param noCuenta the noCuenta to set
	 */
	public void setNoCuenta(String noCuenta) {
		this.noCuenta = noCuenta;
	}

	/**
	 * @return the telefonoContacto
	 */
	public String getTelefonoContacto() {
		return telefonoContacto;
	}

	/**
	 * @param telefonoContacto the telefonoContacto to set
	 */
	public void setTelefonoContacto(String telefonoContacto) {
		this.telefonoContacto = telefonoContacto;
	}

	/**
	 * @return the nombreContacto
	 */
	public String getNombreContacto() {
		return nombreContacto;
	}

	/**
	 * @param nombreContacto the nombreContacto to set
	 */
	public void setNombreContacto(String nombreContacto) {
		this.nombreContacto = nombreContacto;
	}

	/**
	 * @return the emailContacto
	 */
	public String getEmailContacto() {
		return emailContacto;
	}

	/**
	 * @param emailContacto the emailContacto to set
	 */
	public void setEmailContacto(String emailContacto) {
		this.emailContacto = emailContacto;
	}

	/**
	 * @return the ultimaOperacion
	 */
	public String getUltimaOperacion() {
		return ultimaOperacion;
	}

	/**
	 * @param ultimaOperacion the ultimaOperacion to set
	 */
	public void setUltimaOperacion(String ultimaOperacion) {
		this.ultimaOperacion = ultimaOperacion;
	}
	
	public int getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(int idCliente) {
		this.idCliente = idCliente;
	}

	/**
	 * N&uacute;mero de contrato corto
	 */
	private int idContrato;

	/**
	 * Nombre completo del cliente
	 */
	private String nombreCompleto;
	
	/**
	 * N&uacute;mero de contrato SICA
	 */
	private String noCuenta;
	
	/**
	 * Tel&eacute;fono del cliente
	 */
	private String telefonoContacto;
	
	/**
	 * Nombre del contacto
	 */
	private String nombreContacto;
	
	/**
	 * Direcci&oacute;n de correo electr&oacute;nico del cliente
	 */
	private String emailContacto;
	
	/**
	 * Fecha de su &uacute;ltima operaci&oacute;n
	 */
	private String ultimaOperacion;
	
	private int favorito;
	
	/**
	 * Identificador del cliente
	 */
	private int idCliente;

	/**
	 * @return the favorito
	 */
	public int getFavorito() {
		return favorito;
	}

	/**
	 * @param favorito the favorito to set
	 */
	public void setFavorito(int favorito) {
		this.favorito = favorito;
	}

	public String toString() {
		return "InformacionNegociacionVO [idContrato=" + idContrato
			+ ", nombreCompleto=" + nombreCompleto + ", noCuenta="
			+ noCuenta + ", telefonoContacto=" + telefonoContacto
			+ ", nombreContacto=" + nombreContacto + ", emailContacto="
			+ emailContacto + ", ultimaOperacion=" + ultimaOperacion
			+ ", favorito=" + favorito + ", idCliente=" + idCliente + "]";
	}

}
