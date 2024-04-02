package com.ixe.ods.sica.lineacredito.service;

import java.util.Date;
import java.util.List;

import com.ixe.ods.seguridad.model.IUsuario;
import com.ixe.ods.sica.model.LineaCambio;

/**
 * Servicio para el acceso a toda la funcionalidad del Seguimiento y Control de las 
 * Lineas de Credito
 * @author HMASG771
 *
 */
public interface LineaCreditoService {
	
	public static final String LINEA_CREDITO_SERVICE= "lineaCreditoService";
	    /**
     * Nombre de campo Descripcion para catalogos: Tipo Autorizacion, Formalizacion, Instancia Facultada  
     */
	public static final String DESCRIPCION = "descripcion";
	
	/**
	 * Identificador de Tipo de Autorizacion
	 */
	public static final String ID_TIPO_AUTORIZACION = "idTipoAutorizacion";
	
	/**
	 * Identificador de Formalizacion
	 */
	public static final String ID_FORMALIZACION = "idFormalizacion";
	
	/**
	 * Identificador de la instancia facultada
	 */
	public static final String ID_INSTANCIA_FACULTADA = "idInstanciaFacultada";
	
	/**
	 * Cadena inicial para el combo 
	 */
	public static final String SELECCIONAR = "Seleccionar una opcion";
	
	/**
	 * Cadena para correo de Activacion /Suspension de Linea de Credito
	 */
	public static final String SUSPENDIDA = "SUSPENDIDA";
	
	/**
	 * Cadena para correo de Activacion /Suspension de Linea de Credito
	 */
	public static final String AUTORIZADA = "AUTORIZADA";
	
	/**
	 * Obtiene la fecha de vencimiento por defecto para una linea de credito
	 * @return java.util.Date
	 */
	public Date getVencimientoDefault();

	/**
	 * Permite crear una linea de credito asignada a un Cliente
	 * @param com.ixe.ods.sica.model.LineaCambio 
	 */
	public void crearLineaCredito(LineaCambio lineaCredito);

	/**
	 * Busca una Linea de Credito en base a ID BUP_PERSONA
	 * @param integer id BUP_PERSONA
	 * @return LineaCambio
	 */
	public LineaCambio findLineaCreditoByIdPersona(Integer idPersona);

	/**
	 * Obtiene el numero total de Excesos de una Linea de Credito 
	 * @param lcConstanteExcesos
	 * @return
	 */
	public Integer getNumeroTotalExcesos(String lcConstanteExcesos);

	/**
	 * Obtiene el catalogo de tipo de Autorizacion
	 * @return List. 
	 */
	public List getCatalogoTipoAutorizacion();

	/**
	 * Obtiene el catalogo de Formalizacion
	 * @return List.
	 */
	public List  getCatalogoFormalizacion();

	/**
	 * Obtiene el catalogo de Instancia Facultada
	 * @return List
	 */
	public List getCatalogoInstanciaFacultada();

	/**
	 * Apoya a los m&eacute;todos <code>operacionAprobar</code>, <code>operacionActivar</code> y
     * <code>operacionSuspender</code> para cambiar el Status de las L&iacute;neas de Cr&eacute;dito
     * y hacer un LOG de la operaci&oacute;n efectuada en la tabla <code>LineaCreditoLog</code>.
	 * @param estatus El Status a modificar de la L&iacute;nea de Cr&eacute;dito.
     * @param operacion La Operaci&oacute;n efectuada para guardar en el LOG.
     * @param idLineaCredito La L&iacute;nea de Cr&eacute;dito sobre la que se efectu&oacute; la
     * Operaci&oacute;n.
	 * @param user Usuario que efectua la operaci&oacute;n
	 */
	public void cambiarEstatusLineaCredito(String statusAprobada,
			String operacion, Integer idLineaCredito, IUsuario user,String observaciones);

	/**
	 * Obtiene una L&iacute;nea de Cr&eacute;dito por su ID
	 * @param idLineaCredito Identificador de la linea de Credito
	 * @return LineaCambio 
	 */
	public LineaCambio findLineaCredito(Integer idLineaCredito);

	/**
	 * Permite actualizar la informacion de la linea de credito
	 * @param lineaCredito La linea de credito asociada al Cliente
	 * @param java.util.List Lista de las modificaciones efectuadas a la Linea de Credito   
	 */
	public void actualizarLineaCredito(LineaCambio lineaCredito, List listaModificaciones);

	
		
	
	

}
