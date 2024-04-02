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
import com.ixe.ods.sica.batch.clientes.dao.ClienteDao;

/**
 * The Class ClienteDaoImpl.
 */
public class ClienteDaoImpl extends BaseDao implements ClienteDao {
	
	/** The Constant LOG. */
	private static final Logger LOG = Logger.getLogger(ClienteDaoImpl.class);

	/**
	 * Instantiates a new cliente dao impl.
	 */
	public ClienteDaoImpl() {
	}

	
	/**
	 * Get clientes.
	 *
	 * @param sic the sic
	 * @return the list
	 */
	@Override
	public List<Map<String, Object>> getClientes(String sic) {
		StringBuffer sql = new StringBuffer("select ID_CLIENTE, ID_EMPRESA, ID_PERSONA, SIC, ");
		sql.append("CUENTA_CHEQUES, SUCURSAL_ORIGEN, SUCURSAL_OPERACION, NOMBRE_CONTACTO, ");
		sql.append("EMAIL_CONTACTO, TELEFONO_CONTACTO, FAVORITO, USUARIO_CREACION, ");
		sql.append("FECHA_CREACION, USUARIO_ULT_MOD, FECHA_ULT_MOD, ULTIMAS_OPERACIONES ");
		sql.append("from SC_CLIENTE where SIC = ?");
		if (LOG.isDebugEnabled()) {
			LOG.debug("sic: " + sic);
		}
		ClienteSqlQuery query = new ClienteSqlQuery(getDataSource(), sql.toString());
		query.declareParameter(new SqlParameter("SIC", Types.VARCHAR));
		query.compile();
		List<Map<String, Object>> result = query.execute(sic);
		LOG.info("Numero de registros recuperados: " + result.size());
		
		return result;
	}
	
	/**
	 * Actualiza cliente.
	 *
	 * @param datos the datos
	 * @return the int
	 */
	public int actualizaCliente(Map<String, Object> datos) {
		int result = 0;
		StringBuffer sql = new StringBuffer("update SC_CLIENTE set SIC = ?, ");
		sql.append("SUCURSAL_ORIGEN = ?, SUCURSAL_OPERACION = ?, USUARIO_ULT_MOD = ?, ");
		sql.append("FECHA_ULT_MOD = ? where ID_CLIENTE = ?");
		Object[] params = new Object[]{datos.get("sic"), datos.get("sucursalOri"), 
				                       datos.get("sicursalOper"), datos.get("usuarioUltMod"), 
				                       datos.get("fechaUltMod"), datos.get("idCliente")};
		if (LOG.isDebugEnabled()) {
			LOG.debug("datos: " + datos);
		}
		SqlUpdate sqlUpdate = new SqlUpdate(getDataSource(), sql.toString());
		sqlUpdate.declareParameter(new SqlParameter("SIC", Types.VARCHAR));
		sqlUpdate.declareParameter(new SqlParameter("SUCURSAL_ORIGEN", Types.VARCHAR));
		sqlUpdate.declareParameter(new SqlParameter("SUCURSAL_OPERACION", Types.VARCHAR));
		sqlUpdate.declareParameter(new SqlParameter("USUARIO_ULT_MOD", Types.NUMERIC));
		sqlUpdate.declareParameter(new SqlParameter("FECHA_ULT_MOD", Types.DATE));
		sqlUpdate.declareParameter(new SqlParameter("ID_CLIENTE", Types.INTEGER));
		sqlUpdate.compile();
		result = sqlUpdate.update(params);
		LOG.info("result: " + result);
		
		return result;
	}
	
	/**
	 * The Class ClienteSqlQuery.
	 */
	class ClienteSqlQuery extends MappingSqlQuery<Map<String, Object>> {

		/**
		 * Instantiates a new cliente sql query.
		 *
		 * @param ds the ds
		 * @param sql the sql
		 */
		public ClienteSqlQuery(DataSource ds, String sql) {
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
			Map<String, Object> result = new HashMap<String, Object> ();
			result.put("idCliente", rs.getInt("ID_CLIENTE"));
			result.put("idEmpresa", rs.getString("ID_EMPRESA"));
			result.put("idPersona", rs.getLong("ID_PERSONA"));
			result.put("sic", rs.getString("SIC"));
			result.put("noCuenta", rs.getString("CUENTA_CHEQUES"));
			result.put("sicursalOri", rs.getString("SUCURSAL_ORIGEN"));
			result.put("sicursalOper", rs.getString("SUCURSAL_OPERACION"));
			result.put("nombreContacto", rs.getString("NOMBRE_CONTACTO"));
			result.put("emailContacto", rs.getString("EMAIL_CONTACTO"));
			result.put("telContacto", rs.getString("TELEFONO_CONTACTO"));
			result.put("favorito", rs.getShort("FAVORITO"));
			result.put("usuarioCrea", rs.getLong("USUARIO_CREACION"));
			result.put("fechaCrea", rs.getDate("FECHA_CREACION"));
			result.put("usuarioUltMod", rs.getLong("USUARIO_ULT_MOD"));
			result.put("fechaUltMod", rs.getDate("FECHA_ULT_MOD"));
			result.put("ultimasOpers", rs.getString("ULTIMAS_OPERACIONES"));
			
			return result;
		}
	}

}
