package com.ixe.ods.sica.model;


import java.io.Serializable;
import java.util.Date;

/**
 * Clase persistente para la tabla SC_CLIENTE.
 *
 * @hibernate.class table="SC_CLIENTE"
 * mutable="true"
 * proxy="com.ixe.ods.sica.model.Cliente"
 * dynamic-update="true"
 *
 * @hibernate.query name="findClienteByIdPersona" query="FROM Cliente as c where c.idPersona = ?"
 *
 * @author Eduardo Hamue Moral
 * @version $Revision: 1.1.2.4 $ $Date: 2012/08/10 01:52:23 $
 */
public class Cliente implements Serializable
{
	public Cliente(){
		super();
	}
	
	/**
     * Regresa el valor de idCliente.
     *
     * @hibernate.id generator-class="sequence"
     * column="ID_CLIENTE"
     * unsaved-value="null"
     * @hibernate.generator-param name="sequence"
     * value="SC_CLIENTE_SEQ"
     * @return Integer.
     */
	public Integer getIdCliente() {
		return idCliente;
	}
	
	/**
     * Fija el valor de idCliente.
     *
     * @param ID_CLIENTE El valor a asignar.
     */
	public void setIdCliente(Integer _idCliente) {
		idCliente = _idCliente;
	}
	
	/**
     * Regresa el valor de idEmpresa.
     *
     * @return String.
     * @hibernate.property column="ID_EMPRESA"
     * not-null="true"
     * unique="false"
     */
	public String getIdEmpresa() {
		return idEmpresa;
	}
	
	/**
     * Fija el valor de idEmpresa.
     *
     * @param ID_EMPRESA El valor a asignar.
     */
	public void setIdEmpresa(String _idEmpresa) {
		idEmpresa = _idEmpresa;
	}
	
	/**
     * Regresa el valor de idPersona.
     *
     * @return Integer.
     * @hibernate.property column="ID_PERSONA"
     * not-null="true"
     * unique="false"
     */
	public Integer getIdPersona() {
		return idPersona;
	}
	
	/**
     * Fija el valor de idPersona.
     *
     * @param ID_PERSONA El valor a asignar.
     */
	public void setIdPersona(Integer _idPersona) {
		idPersona= _idPersona;
	}
	
	/**
     * Regresa el valor de sic.
     *
     * @return String.
     * @hibernate.property column="SIC"
     * not-null="false"
     * unique="false"
     */
	public String getSic() {
		return sic;
	}
	
	/**
     * Fija el valor de sic.
     *
     * @param SIC El valor a asignar.
     */
	public void setSic(String _sic) {
		sic = _sic;
	}
	
	/**
     * Regresa el valor de cuentaCheques.
     *
     * @return String.
     * @hibernate.property column="CUENTA_CHEQUES"
     * not-null="false"
     * unique="false"
     */
	public String getCuentaCheques() {
		return cuentaCheques;
	}
	
	/**
     * Fija el valor de cuentaCheques.
     *
     * @param CUENTA_CHEQUES El valor a asignar.
     */
	public void setCuentaCheques(String _cuentaCheques) {
		cuentaCheques = _cuentaCheques;
	}
	
	/**
     * Regresa el valor de sucursalOrigen.
     *
     * @return String.
     * @hibernate.property column="SUCURSAL_ORIGEN"
     * not-null="false"
     * unique="false"
     */
	public String getSucursalOrigen() {
		return sucursalOrigen;
	}
	
	/**
     * Fija el valor de sucursalOrigen.
     *
     * @param SUCURSAL_ORIGEN El valor a asignar.
     */
	public void setSucursalOrigen(String _sucursalOrigen) {
		sucursalOrigen = _sucursalOrigen;
	}
	
	/**
     * Regresa el valor de sucursalOperacion.
     *
     * @return String.
     * @hibernate.property column="SUCURSAL_OPERACION"
     * not-null="false"
     * unique="false"
     */
	public String getSucursalOperacion() {
		return sucursalOperacion;
	}
	
	/**
     * Fija el valor de sucursalOperacion.
     *
     * @param SUCURSAL_OPERACION El valor a asignar.
     */
	public void setSucursalOperacion(String _sucursalOperacion) {
		sucursalOperacion = _sucursalOperacion;
	}
	
	/**
     * Regresa el valor de nombreContacto.
     *
     * @return String.
     * @hibernate.property column="NOMBRE_CONTACTO"
     * not-null="false"
     * unique="false"
     */
	public String getNombreContacto() {
		return nombreContacto;
	}
	
	/**
     * Fija el valor de nombreContacto.
     *
     * @param NOMBRE_CONTACTO El valor a asignar.
     */
	public void setNombreContacto(String _nombreContacto) {
		nombreContacto = _nombreContacto;
	}
	
	/**
     * Regresa el valor de emailContacto.
     *
     * @return String.
     * @hibernate.property column="EMAIL_CONTACTO"
     * not-null="false"
     * unique="false"
     */
	public String getEmailContacto() {
		return emailContacto;
	}
	
	/**
     * Fija el valor de emailContacto.
     *
     * @param EMAIL_CONTACTO El valor a asignar.
     */
	public void setEmailContacto(String _emailContacto) {
		emailContacto = _emailContacto;
	}
	
	/**
     * Regresa el valor de telefonoContacto.
     *
     * @return String.
     * @hibernate.property column="TELEFONO_CONTACTO"
     * not-null="false"
     * unique="false"
     */
	public String getTelefonoContacto() {
		return telefonoContacto;
	}
	
	/**
     * Fija el valor de telefonoContacto.
     *
     * @param TELEFONO_CONTACTO El valor a asignar.
     */
	public void setTelefonoContacto(String _telefonoContacto) {
		telefonoContacto = _telefonoContacto;
	}
	
	/**
     * Regresa el valor de favorito.
     *
     * @return Integer.
     * @hibernate.property column="FAVORITO"
     * not-null="false"
     * unique="false"
     */
	public Integer getFavorito() {
		return favorito;
	}
	
	/**
     * Fija el valor de favorito.
     *
     * @param FAVORITO El valor a asignar.
     */
	public void setFavorito(Integer _favorito) {
		favorito = _favorito;
	}

	/**
     * Regresa el valor de usuarioCreacion.
     *
     * @return int.
     * @hibernate.property column="USUARIO_CREACION"
     * not-null="true"
     * unique="false"
     */
	public int getUsuarioCreacion() {
		return usuarioCreacion;
	}
	
	/**
     * Fija el valor de usuarioCreacion.
     *
     * @param USUARIO_CREACION El valor a asignar.
     */
	public void setUsuarioCreacion(int _usuarioCreacion) {
		usuarioCreacion = _usuarioCreacion;
	}

	/**
     * Regresa el valor de fechaCreacion.
     *
     * @return Date.
     * @hibernate.property column="FECHA_CREACION"
     * not-null="true"
     * unique="false"
     */
	public Date getFechaCreacion() {
		return fechaCreacion;
	}
	
	/**
     * Fija el valor de fechaCreacion.
     *
     * @param FECHA_CREACION El valor a asignar.
     */
	public void setFechaCreacion(Date _fechaCreacion) {
		fechaCreacion = _fechaCreacion;
	}

	/**
     * Regresa el valor de usuarioUltMod.
     *
     * @return Integer.
     * @hibernate.property column="USUARIO_ULT_MOD"
     * not-null="false"
     * unique="false"
     */
	public Integer getUsuarioUltMod() {
		return usuarioUltMod;
	}
	
	/**
     * Fija el valor de usuarioUltMod.
     *
     * @param USUARIO_ULT_MOD El valor a asignar.
     */
	public void setUsuarioUltMod(Integer _usuarioUltMod) {
		usuarioUltMod = _usuarioUltMod;
	}

	/**
     * Regresa el valor de fechaUltMod.
     *
     * @return Date.
     * @hibernate.property column="FECHA_ULT_MOD"
     * not-null="false"
     * unique="false"
     */
	public Date getFechaUltMod() {
		return fechaUltMod;
	}
	
	/**
     * Fija el valor de fechaUltMod.
     *
     * @param FECHA_ULT_MOD El valor a asignar.
     */
	public void setFechaUltMod(Date _fechaUltMod) {
		fechaUltMod = _fechaUltMod;
	}
	
	/**
	 * Identificador del cliente
	 */
	private Integer idCliente; //NUMBER(7) NOT NULL,
	
	/**
	 * Valor asociado a la empresa a la que el cliente pertenece
	 */
	private String idEmpresa;// NUMBER(4) NOT NULL, 
	
	/**
	 * Clave del cliente como persona
	 */
	private Integer idPersona;// NUMBER(22) NOT NULL, 
	
	/**
	 * Clave del contrato sic
	 */
	private String sic;// VARCHAR(8 BYTE),
	
	/**
	 *numero cuenta de cheques del cliente 
	 */
	private String cuentaCheques;// VARCHAR(11 BYTE) NOT NULL,
	
	/**
	 * Sucursal donde el cliente procede
	 */
	private String sucursalOrigen;// VARCHAR(4 BYTE),
	
	/**
	 * Sucursal donde el cliente opera
	 */
	private String sucursalOperacion;// VARCHAR(4 BYTE),
	
	/**
	 * 
	 */
	private String nombreContacto;// VARCHAR(50 BYTE),
	
	/**
	 * 
	 */
	private String emailContacto;// VARCHAR(50 BYTE),
	
	/**
	 * 
	 */
	private String telefonoContacto;// VARCHAR(50 BYTE),
	
	/**
	 * 
	 */
	private Integer favorito;// NUMBER(1),
	
	/**
	 * Persona que registro al cliente
	 */
	private int usuarioCreacion;// NUMBER(22) NOT NULL,
	
	/**
	 * Fecha en la que el cliente fue registrdo
	 */
	private Date fechaCreacion;// DATE NOT NULL,
	
	/**
	 * 
	 */
	private Integer usuarioUltMod;// NUMBER(22),
	
	/**
	 * 
	 */
	private Date fechaUltMod;// DATE
}
