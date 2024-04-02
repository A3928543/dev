package com.ixe.ods.sica.rmds.process.dao.impl;

import java.util.Date;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.ixe.ods.sica.rmds.process.dao.DepuracionTablasPreciosDao;

/**
 * Implementa m&eacute;todos para enviar a hist&oacute;ricos
 * los registros generados por el proceso de generaci&oacute;n 
 * de pizarrones en el SICA y de esta manera evitar que las tablas
 * en BD crescan de manera desmedida.
 * 
 * @author Efren Trinidad, Na-at Technologies
 *
 * Jul 21, 2011 6:47:22 PM
 */
@Repository
public class JdbcDepuracionTablasPreciosDao implements DepuracionTablasPreciosDao{
	
	/**
	 * Instancia de utilidad para ejecuci&oacute;n de sentencias
	 * SQL a la BD.
	 */
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate; 
	
	/**
	 * Query que migra los registros en SC_PRECIO_REFERENCIA que tienen
	 * alguna relaci&oacute;n con un detalle de Deal en SC_DEAL_DETALLE a
	 * la tabla de hist&oacute;ricos SC_H_PRECIO_REFERENCIA. 
	 */
	public static final String RESP_PR_QUERY = "INSERT "
												+"INTO SC_H_PRECIO_REFERENCIA "
												+"( "
													+"ID_PRECIO_REFERENCIA, "
													+" METODO_ACTUALIZACION, "
													+"PRECIO_SPOT, "
													+"PRECIO_COMPRA, "
													+"PRECIO_VENTA, "
													+"ULTIMA_MODIFICACION, "
													+"MID_SPOT "
												+") "
												+"SELECT DISTINCT pr.ID_PRECIO_REFERENCIA, "
													+"pr.METODO_ACTUALIZACION, "
													+"pr.PRECIO_SPOT, "
													+"pr.PRECIO_COMPRA, "
													+"pr.PRECIO_VENTA, "
													+"pr.ULTIMA_MODIFICACION, "
													+"pr.MID_SPOT "
												+"FROM sc_precio_referencia pr "
													+"INNER JOIN sc_deal_detalle dd "
													+"ON pr.id_precio_referencia = dd.id_precio_referencia "
													+"INNER JOIN sc_deal d "
													+"ON dd.id_deal = d.id_deal "
												+"WHERE d.fecha_captura BETWEEN :fechaInicio "
													+"AND :fechaFin "
													+"AND pr.ultima_modificacion BETWEEN :fechaInicio " 
													+"AND :fechaFin";
	
	/**
	 * Query que elimina los registros en SC_PRECIO_REFERENCIA que 
	 * fueron generados en determinado d&iacute;a.
	 */
	public static final String LIMPIAR_PR_QUERY = "DELETE "
													+"FROM SC_PRECIO_REFERENCIA pr "
													+"WHERE pr.ULTIMA_MODIFICACION BETWEEN :fechaInicio "
													+"AND :fechaFin";
	
	/**
	 * Query que migra los registros en SC_FACTOR_DIVISA que tienen
	 * alguna relaci&oacute;n con un detalle de Deal en SC_DEAL_DETALLE a
	 * la tabla de hist&oacute;ricos SC_H_FACTOR_DIVISA. 
	 */
	public static final String RESP_FD_QUERY = "INSERT "
												+"INTO sc_h_factor_divisa "
												+"( "
													+"ID_FACTOR_DIVISA, "
													+"FROM_ID_DIVISA, "
													+"TO_ID_DIVISA, "
													+"FACTOR, "
													+"METODO_ACTUALIZACION, "
													+"SPREAD_REFERENCIA, "
													+"SPREAD_COMPRA, "
													+"CARRY, "
													+"ULTIMA_MODIFICACION, "
													+"FACTOR_COMPRA, "
													+"SLACK "
												+") "
												+"SELECT DISTINCT fd.ID_FACTOR_DIVISA, "
												+"fd.FROM_ID_DIVISA, "
												+"fd.TO_ID_DIVISA, "
												+"fd.FACTOR, "
												+"fd.METODO_ACTUALIZACION, "
												+"fd.SPREAD_REFERENCIA, "
												+"fd.SPREAD_COMPRA, "
												+"fd.CARRY, "
												+"fd.ULTIMA_MODIFICACION, "
												+"fd.FACTOR_COMPRA, "
												+"fd.SLACK "
												+"FROM sc_factor_divisa fd "
													+"INNER JOIN sc_deal_detalle dd "
													+"ON fd.id_factor_divisa = dd.id_factor_divisa "
													+"INNER JOIN sc_deal d "
													+"ON dd.id_deal = d.id_deal "
												+"WHERE d.fecha_captura BETWEEN :fechaInicio "
													+"AND :fechaFin "
													+"AND fd.ultima_modificacion BETWEEN :fechaInicio " 
													+"AND :fechaFin";
	
	/**
	 * Query que elimina los registros en SICA_VARIACION que 
	 * fueron generados en determinado d&iacute;a.
	 */
	public static final String LIMPIAR_FD_QUERY = "DELETE "
													+"FROM SC_FACTOR_DIVISA fd "
													+"WHERE fd.ULTIMA_MODIFICACION BETWEEN :fechaInicio "
													+"AND :fechaFin ";
	
	/**
	 * Query que elimina los registros en SC_FACTOR_DIVISA que 
	 * fueron generados en determinado d&iacute;a.
	 */
	public static final String LIMPIAR_VAR_QUERY = "DELETE "
													+"FROM sica_variacion v "
													+"WHERE v.fecha BETWEEN :fechaInicio "
													+"AND :fechaFin ";
	
	/**
	 * @see com.ixe.ods.sica.rmds.process.dao.DepuracionTablasPreciosDao#limpiarPreciosReferencia(java.util.Date, java.util.Date)
	 */
	public boolean limpiarPreciosReferencia(Date fechaInicio, Date fechaFin){
		
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("fechaInicio", fechaInicio);
		namedParameters.addValue("fechaFin", fechaFin);
		
		//Se migran los registros en SC_PRECIO_REFERENCIA
		namedParameterJdbcTemplate.update(RESP_PR_QUERY, namedParameters );
		
		//Se eliminan los registos en SC_PRECIO_REFERENCIA
		namedParameterJdbcTemplate.update(LIMPIAR_PR_QUERY, namedParameters);
		
		return true;
	}
	
	/**
	 * @see com.ixe.ods.sica.rmds.process.dao.DepuracionTablasPreciosDao#limpiarFactoresDivisa(java.util.Date, java.util.Date)
	 */
	public boolean limpiarFactoresDivisa(Date fechaInicio, Date fechaFin){
		
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("fechaInicio", fechaInicio);
		namedParameters.addValue("fechaFin", fechaFin);
		
		//Se migran los registros en SC_FACTOR_DIVISA
		namedParameterJdbcTemplate.update(RESP_FD_QUERY, namedParameters );
		
		//Se eliminan los registos en SC_FACTOR_DIVISA
		namedParameterJdbcTemplate.update(LIMPIAR_FD_QUERY, namedParameters);
		
		return true;
	}
	
	/**
	 * @see com.ixe.ods.sica.rmds.process.dao.DepuracionTablasPreciosDao#limpiarVariacionesDivisa(java.util.Date, java.util.Date)
	 */
	public boolean limpiarVariacionesDivisa(Date fechaInicio, Date fechaFin){
		
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("fechaInicio", fechaInicio);
		namedParameters.addValue("fechaFin", fechaFin);
		
		//Se eliminan los registos en SICA_VARIACION
		namedParameterJdbcTemplate.update(LIMPIAR_VAR_QUERY, namedParameters);
		
		return true;
	}
	
	/**
	 * Inyecta el DataSource de donde se obtendran las conexiones a la BD.
	 * 
	 * @param datasource DataSource configurado en el contexto de Spring
	 */
	@Autowired
	public void setDatasource(DataSource datasource){
		namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(datasource);
	}

}
