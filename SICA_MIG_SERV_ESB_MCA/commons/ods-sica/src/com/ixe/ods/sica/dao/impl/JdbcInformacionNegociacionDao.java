package com.ixe.ods.sica.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.ixe.ods.sica.dao.InformacionNegociacionDao;
import com.ixe.ods.sica.vo.InfoNegociacionDealVO;
import com.ixe.ods.sica.vo.InformacionNegociacionVO;

public class JdbcInformacionNegociacionDao extends JdbcTemplate implements
		InformacionNegociacionDao {

	/**
	 * Sentencia SQL que recupera los clientes de un promotor junto con la
	 * informaci&oacute;n de negociaci&oacute;n asociada
	 */
	private static final String CLIENTES_INFO_NEG_QUERY = "SELECT cta.id_contrato, "
			+ "p.nombre_corto, "
			+ "bcc.no_cuenta, "
			+ "cte.telefono_contacto, "
			+ "cte.nombre_contacto, "
			+ "cte.email_contacto, "
			+ "cte.favorito, "
			+ "cte.id_cliente, "
			+ "TO_CHAR(d.fecha_captura, 'DD/MM/YYYY HH24:MI') fecha_captura "
			+ "FROM bup_cuenta_ejecutivo bce "
			+ "INNER JOIN bup_cuenta_contrato bcc "
			+ "ON bce.no_cuenta = bcc.no_cuenta "
			+ "INNER JOIN bup_persona_cuenta_rol pcr "
			+ "ON bcc.no_cuenta = pcr.no_cuenta "
			+ "INNER JOIN bup_persona p "
			+ "ON pcr.id_persona = p.id_persona "
			+ "LEFT OUTER JOIN sc_contrato cta "
			+ "ON bcc.no_cuenta = cta.no_cuenta "
			+ "LEFT OUTER JOIN sc_cliente cte "
			+ "ON cta.id_cliente           = cte.id_cliente "
			+ "LEFT OUTER JOIN sc_deal d "
			+ "ON d.id_deal = ( "
			+ "CASE WHEN INSTR(cte.ultimas_operaciones, ',') = 0 THEN "
			+ "TO_NUMBER(cte.ultimas_operaciones) "
			+ "ELSE "
			+ "TO_NUMBER( SUBSTR( cte.ultimas_operaciones, 0, INSTR(cte.ultimas_operaciones, ',') - 1 ) ) "
			+ "END "
			+ ") "
			+ "WHERE bce.id_tipo_ejecutivo = 'EJEBAN' "
			+ "AND bce.status              = 'VIG' "
			+ "AND bce.id_persona          = ? "
			+ "AND bcc.id_tipo_cuenta      = 'CAM10' "
			+ "AND bcc.status              = 'ACTIVA' "
			+ "AND pcr.id_rol              = 'TIT' "
			+ "AND (pcr.status             = 'VIG' "
			+ "OR pcr.status               = 'ACTIVA')";

	private static final String CLIENTE_FAVORITO_QUERY = "UPDATE sc_cliente "
			+ "SET favorito     = ? " + "WHERE ID_CLIENTE = "
			+ "(SELECT ID_CLIENTE FROM sc_contrato WHERE NO_CUENTA = ? " + ") ";

	/**
	 * Consulta para obtener las ultimas 5 operaciones de un cliente en forma de
	 * un string tipo csv
	 */
	private static final String LAST_FIVE_DEALS_CSV_QUERY = "SELECT "
			+ "		ultimas_operaciones " + "FROM " + "		sc_cliente " + "WHERE "
			+ "		id_cliente = ? ";

	/**
	 * Consulta para obtener las ultimas 5 operaciones de un cliente
	 */
	/*private static final String LAST_FIVE_DEALS_QUERY = "SELECT "
			+ "		d.id_deal "
			+ "		, TO_CHAR(d.fecha_captura, 'DD/MM/YYYY HH24:MI') fecha_captura "
			+ "		, CASE WHEN dp.recibimos = 'S' THEN 'Compra' ELSE 'Venta' END tipo_operacion "
			+ "		, d.tipo_valor "
			+ "		, sum(pl.monto) monto"
			+ "		, pl.id_divisa "
			+ "		, pl.tipo_cambio_cliente "
			+ "		, ABS(pl.tipo_cambio_cliente - pl.tipo_cambio_mesa) spread "
			+ "FROM "
			+ "		sc_cliente cli "
			+ "		, sc_contrato con "
			+ "		, sc_deal d "
			+ "		, sc_posicion_log pl "
			+ "		, sc_deal_detalle dd "
			+ "		, sc_deal_posicion dp "
			+ "WHERE "
			+ "		con.id_cliente = cli.id_cliente "
			+ "		AND con.no_cuenta = d.no_cuenta "
			+ "		AND pl.id_deal = d.id_deal "
			+ "		AND dd.id_deal_posicion = pl.id_deal_posicion "
			+ "		AND dp.id_deal_posicion = dd.id_deal_posicion "
			+ " 	AND pl.tipo_operacion NOT IN ('CT', 'VT') "
			+ "		AND d.id_deal IN (@) "
			+ "		AND cli.id_cliente = ? "
			+ "		AND dd.status_detalle_deal <> 'CA' "
			+ "		group by d.id_deal, d.fecha_captura, dp.recibimos, tipo_operacion,d.tipo_valor,pl.id_divisa, pl.tipo_cambio_cliente, ABS(pl.tipo_cambio_cliente - pl.tipo_cambio_mesa)"
			+ "ORDER BY " + "		d.fecha_captura DESC ";*/
	
	/**
	 * Consulta para obtener las ultimas 5 operaciones de un cliente
	 */
	//QUERY MODIFICADO PARA QUE NO VAYA A LA TABLA SC_POSICION_LOG Y DUPLIQUE REGISTROS JDCH 06/12/2012
	private static final String LAST_FIVE_DEALS_QUERY = "SELECT "
			+ "		d.id_deal "
			+ "		, TO_CHAR(d.fecha_captura, 'DD/MM/YYYY HH24:MI') fecha_captura "
			+ "		, CASE WHEN dp.recibimos = 'S' THEN 'Compra' ELSE 'Venta' END tipo_operacion "
			+ "		, d.tipo_valor "
			+ "		, sum(dp.monto) monto"
			+ "		, dp.id_divisa "
			+ "		, dp.tipo_cambio "
			+ "		, ABS(dp.tipo_cambio - dd.tipo_cambio_mesa) spread "
			+ "FROM "
			+ "		sc_cliente cli "
			+ "		, sc_contrato con "
			+ "		, sc_deal d "
			+ "		, sc_deal_detalle dd "
			+ "		, sc_deal_posicion dp "
			+ "WHERE "
			+ "		con.id_cliente = cli.id_cliente "
			+ "		AND d.id_deal = dd.id_deal"
			+ "		AND con.no_cuenta = d.no_cuenta "
			+ "		AND dp.id_divisa != 'MXN' "
			+ "		AND dp.id_deal_posicion = dd.id_deal_posicion "
			+ "		AND d.id_deal IN (@) "
			+ "		AND cli.id_cliente = ? "
			+ "		AND dd.status_detalle_deal = 'TT' "
			+ "		group by d.id_deal, d.fecha_captura, dp.recibimos,tipo_valor,dp.id_divisa, dp.tipo_cambio, ABS(dp.tipo_cambio - dd.tipo_cambio_mesa)"
			+ "ORDER BY " + "		d.fecha_captura DESC ";//termina modificación JDCH

	/**
	 * Sentencia SQL para actualizar los datos del contacto
	 */
	private static final String UPDATE_CONTACT_DATA_QUERY = "UPDATE sc_cliente "
			+ "SET "
			+ "		telefono_contacto = ? "
			+ "		, nombre_contacto = ? "
			+ "		, email_contacto = ? " + "WHERE id_cliente = ? ";

	/**
	 * Obtiene las ultimas 5 operaciones TL para un cliente consultando la tabla
	 * SC_DEAL
	 */
	private static final String LAST_FIVE_DEALS_FROM_SC_DEAL_QUERY = "SELECT d.id_deal ID_DEAL "
			+ "FROM ( "
			+ "SELECT id_deal "
			+ "FROM sc_deal "
			+ "WHERE no_cuenta = ? AND status_deal = 'TL' "
			+ "AND reversado = 0 ORDER BY fecha_captura DESC ) d "
			+ "WHERE ROWNUM <= 5 " + "ORDER BY ROWNUM ";

	/**
	 * Obtiene el id de cliente dado el contrato sica
	 */
	private static final String FIND_CLIENTE_ID_BY_CONTRATO_SICA_QUERY = "SELECT cli.id_cliente "
			+ "FROM "
			+ "sc_cliente cli, "
			+ "sc_contrato con "
			+ "WHERE "
			+ "con.id_cliente = cli.id_cliente " + "AND con.no_cuenta = ? ";

	/**
	 * Actualiza las ultimas operaciones del cliente
	 */
	private static final String UPDATE_CLIENTE_LAST_FIVE_DEALS_QUERY = "UPDATE sc_cliente "
			+ "	SET ultimas_operaciones = ? " + "WHERE id_cliente = ? ";

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.ixe.ods.sica.dao.impl.InformacionNegociacionDao#
	 * findClienteEjecutivoConInfoNegociacion(java.lang.Integer)
	 */
	public List findClienteEjecutivoConInfoNegociacion(
			Integer idPersonaEjecutivo, boolean favoritos) {

		return query(favoritos ? CLIENTES_INFO_NEG_QUERY
				+ " AND cte.favorito = 1" : CLIENTES_INFO_NEG_QUERY,
				new Object[] { idPersonaEjecutivo }, new RowMapper() {

					public Object mapRow(ResultSet rs, int rowNum) {
						InformacionNegociacionVO cliente = new InformacionNegociacionVO();

						try {
							cliente.setIdContrato(rs.getInt(1));
							cliente.setNombreCompleto(rs.getString(2));
							cliente.setNoCuenta(rs.getString(3));
							cliente.setTelefonoContacto(rs.getString(4));
							cliente.setNombreContacto(rs.getString(5));
							cliente.setEmailContacto(rs.getString(6));
							cliente.setFavorito(rs.getInt(7));
							cliente.setIdCliente(rs.getInt(8));
							cliente.setUltimaOperacion(rs
									.getString("fecha_captura"));
						} catch (SQLException sqle) {
							logger.error(
									"Error al obtener el modelo de tipos de inversion"
											+ sqle.getMessage(), sqle);
						}

						return cliente;
					}
				});
	}

	public boolean updateClienteFavorito(String noCuenta, boolean favorito) {

		int rowsUpdated = update(CLIENTE_FAVORITO_QUERY, new Object[] {
				favorito ? new Integer(1) : new Integer(0), noCuenta });

		return rowsUpdated == 1 ? true : false;
	}

	/**
	 * RowMapper para los resultados de las ultimas cinco operaciones
	 * 
	 * @author Cesar Jeronimo Gomez
	 */
	private class InfoNegociacionDealRowMapper implements RowMapper {

		DecimalFormat format = new DecimalFormat("$#,##0.000000;-$#,##0.000000");
		DecimalFormat dosDecFormat = new DecimalFormat("$#,##0.00;-$#,##0.00"); 
		
		public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
			InfoNegociacionDealVO deal = new InfoNegociacionDealVO(rs
					.getInt("id_deal"), // idDeal,
					rs.getString("fecha_captura"), // fechaCaptura,
					rs.getString("tipo_operacion"), // tipoOperacion,
					rs.getString("tipo_valor"), // fechaValor
					dosDecFormat.format(rs.getBigDecimal("monto")), //
					rs.getString("id_divisa"), format.format(rs
							.getBigDecimal("tipo_cambio")), format
							.format(rs.getBigDecimal("spread")));
			return deal;
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ixe.ods.sica.dao.InformacionNegociacionDao#findLastFiveDeals(java
	 * .lang.Integer)
	 */
	public List findLastFiveDeals(Integer idCliente) {
		// Primero se obtienen las ultimas 5 operaciones concatenadas
		String lastFiveDealsCsv = "";
		try {
			lastFiveDealsCsv = (String) queryForObject(
					LAST_FIVE_DEALS_CSV_QUERY, new Object[] { idCliente },
					String.class);
		} catch (IncorrectResultSizeDataAccessException e) {
			;
		}
		// Luego se consultan una a una con un IN
		if (!StringUtils.isEmpty(lastFiveDealsCsv)) {
			List lastFiveDeals = new ArrayList();
			lastFiveDeals = query(LAST_FIVE_DEALS_QUERY.replaceAll("\\@",
					lastFiveDealsCsv), new Object[] { idCliente },
					new InfoNegociacionDealRowMapper());
			return lastFiveDeals;
		} else {
			return new ArrayList();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ixe.ods.sica.dao.InformacionNegociacionDao#editContactInfo(java.lang
	 * .Integer, java.lang.String, java.lang.String, java.lang.String)
	 */
	public boolean editContactInfo(Integer idCliente, String phoneNumber,
			String name, String email) {
		int rowsUpdated = update(UPDATE_CONTACT_DATA_QUERY, new Object[] {
				phoneNumber, name, email, idCliente });
		if (rowsUpdated == 1) {
			return true;
		} else {
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ixe.ods.sica.dao.InformacionNegociacionDao#getLastFiveDealsCsv(java
	 * .lang.String)
	 */
	public String getLastFiveDealsCsv(String contratoSica) {
		List lastFiveDeals = queryForList(LAST_FIVE_DEALS_FROM_SC_DEAL_QUERY,
				new Object[] { contratoSica });
		StringBuffer lastFiveDealsCsv = new StringBuffer("");
		int i = 1;
		for (Iterator it = lastFiveDeals.iterator(); it.hasNext();) {
			Map current = (Map) it.next();
			lastFiveDealsCsv.append(current.get("ID_DEAL"));
			if (i < lastFiveDeals.size()) {
				lastFiveDealsCsv.append(",");
			}
			i++;
		}
		return lastFiveDealsCsv.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ixe.ods.sica.dao.InformacionNegociacionDao#getIdClienteByContratoSica
	 * (java.lang.String)
	 */
	public Integer getIdClienteByContratoSica(String contratoSica) {
		try {
			int idCliente = queryForInt(FIND_CLIENTE_ID_BY_CONTRATO_SICA_QUERY,
					new Object[] { contratoSica });
			return new Integer(idCliente);
		} catch (IncorrectResultSizeDataAccessException e) {
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ixe.ods.sica.dao.InformacionNegociacionDao#updateClienteLastFiveDeals
	 * (java.lang.Integer, java.lang.String)
	 */
	public boolean updateClienteLastFiveDeals(Integer idCliente,
			String lastFiveDealsCsv) {
		int rowsUpdated = update(UPDATE_CLIENTE_LAST_FIVE_DEALS_QUERY,
				new Object[] { lastFiveDealsCsv, idCliente });
		if (rowsUpdated == 1) {
			return true;
		} else {
			return false;
		}
	}

}
