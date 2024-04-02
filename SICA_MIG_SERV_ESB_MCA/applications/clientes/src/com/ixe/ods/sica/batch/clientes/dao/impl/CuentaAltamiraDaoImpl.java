package com.ixe.ods.sica.batch.clientes.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.MappingSqlQuery;
import org.springframework.jdbc.object.SqlUpdate;

import com.ixe.ods.sica.batch.clientes.dao.BaseDao;
import com.ixe.ods.sica.batch.clientes.dao.CuentaAltamiraDao;

/**
 * The Class CuentaAltamiraDaoImpl.
 */
public class CuentaAltamiraDaoImpl extends BaseDao implements CuentaAltamiraDao {
	
	/** The Constant LOG. */
	private static final Logger LOG = Logger.getLogger(CuentaAltamiraDaoImpl.class);

	/**
	 * Gets the cuentas.
	 *
	 * @param sic the sic
	 * @return the cuentas
	 */
	public List<Map<String, Object>> getCuentas(Long sic) {
		StringBuffer sql = new StringBuffer("select CUENTA_ALTAMIRA, ID_PERSONA, SIC, CR, ");
		sql.append("USUARIO, FECHA_ALTA, FECHA_ULT_MOD from SC_CUENTA_ALTAMIRA where SIC = ?");
		if (LOG.isDebugEnabled()) {
			LOG.debug("sic: " + sic);
		}
		CuentaAltamiraMappingSqlQuery query = 
				new CuentaAltamiraMappingSqlQuery(getDataSource(), sql.toString());
		query.declareParameter(new SqlParameter("SIC", Types.NUMERIC));
		query.compile();
		List<Map<String, Object>> result = query.execute(sic);
		LOG.info("Registros obtenidos: " + result.size());
		
		return result;
	}
	
	/**
	 * Actualiza cuenta.
	 *
	 * @param datos the datos
	 * @return the int
	 */
	public int actualizaCuenta(Map<String, Object> datos) {
		int result = 0;
		StringBuffer sql = new StringBuffer("update SC_CUENTA_ALTAMIRA set SIC = ?, CR = ?, ");
		sql.append("USUARIO = ?, FECHA_ULT_MOD = ? where CUENTA_ALTAMIRA = ?");
		if (LOG.isDebugEnabled()) {
			LOG.debug("datos: " + datos);
		}
		Object[] params = new Object[]{datos.get("sic"), datos.get("cr"), 
				                       datos.get("usuario"), datos.get("fechaUltMod"), 
				                       datos.get("noCuenta")};
		SqlUpdate sqlUpdate = new SqlUpdate(getDataSource(), sql.toString());
		sqlUpdate.declareParameter(new SqlParameter("SIC", Types.NUMERIC));
		sqlUpdate.declareParameter(new SqlParameter("CR", Types.VARCHAR));
		sqlUpdate.declareParameter(new SqlParameter("USUARIO", Types.VARCHAR));
		sqlUpdate.declareParameter(new SqlParameter("FECHA_ULT_MOD", Types.DATE));
		sqlUpdate.declareParameter(new SqlParameter("CUENTA_ALTAMIRA", Types.VARCHAR));
		sqlUpdate.compile();
		result = sqlUpdate.update(params);
		LOG.info("result: " + result);
		
		return result;
	}
	
	/**
	 * The Class CuentaAltamiraMappingSqlQuery.
	 */
	class CuentaAltamiraMappingSqlQuery extends MappingSqlQuery<Map<String, Object>> {

		/**
		 * Instantiates a new cuenta altamira mapping sql query.
		 *
		 * @param ds the ds
		 * @param sql the sql
		 */
		public CuentaAltamiraMappingSqlQuery(DataSource ds, String sql) {
			super(ds, sql);
		}
		
		/**
		 * Map row.
		 *
		 * @param rs the rs
		 * @param row the row
		 * @return the map
		 * @throws SQLException the sQL exception
		 */
		@Override
		protected Map<String, Object> mapRow(ResultSet rs, int row)
				throws SQLException {
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("noCuenta", rs.getString("CUENTA_ALTAMIRA"));
			result.put("idPersona", rs.getLong("ID_PERSONA"));
			result.put("sic", rs.getLong("SIC"));
			result.put("cr", rs.getString("CR"));
			result.put("usuario", rs.getString("USUARIO"));
			result.put("fechaAlta", rs.getDate("FECHA_ALTA"));
			result.put("fechaUltMod", rs.getString("FECHA_ULT_MOD"));
			
			return result;
		}
	}

}
