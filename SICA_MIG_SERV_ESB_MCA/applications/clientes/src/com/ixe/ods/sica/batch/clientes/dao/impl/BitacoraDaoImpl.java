package com.ixe.ods.sica.batch.clientes.dao.impl;

import java.sql.Types;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.SqlUpdate;

import com.ixe.ods.sica.batch.clientes.dao.BaseDao;
import com.ixe.ods.sica.batch.clientes.dao.BitacoraDao;

/**
 * The Class BitacoraDaoImpl.
 */
public class BitacoraDaoImpl extends BaseDao implements BitacoraDao {
	
	/** The Constant LOG. */
	private static final Logger LOG = Logger.getLogger(BitacoraDaoImpl.class);

	/**
	 * Instantiates a new bitacora dao impl.
	 */
	public BitacoraDaoImpl() {
	}
    
	
	/**
	 * Registrar bitacora.
	 *
	 * @param datos the datos
	 * @return the int
	 */
	public int registrarBitacora(Map<String, Object> datos) {
		int result = 0;
		StringBuffer sql = new StringBuffer("INSERT INTO SC_BITACORA_CTE_FUSIONADO ");
		sql.append("(ARCHIVO, SIC_ANT, SIC_NUEVO, FECHA_ALTA, CVE_USUARIO, DETALLE) ");
		sql.append("VALUES (?, ?, ?, sysdate, ?, ?)");
		if (LOG.isDebugEnabled()) {
			LOG.debug("datos: " + datos);
		}
		Object[] param = new Object[]{datos.get("ARCHIVO"), datos.get("NUMCLREF"), 
				                      datos.get("NUMCLIEN"), datos.get("USUARIO"), 
				                      datos.get("DETALLE")};
		SqlUpdate sqlUpdate = new SqlUpdate(getDataSource(), sql.toString());
		sqlUpdate.declareParameter(new SqlParameter("ARCHIVO", Types.CHAR));
		sqlUpdate.declareParameter(new SqlParameter("SIC_ANT", Types.VARCHAR));
		sqlUpdate.declareParameter(new SqlParameter("SIC_NUEVO", Types.VARCHAR));
		sqlUpdate.declareParameter(new SqlParameter("CVE_USUARIO", Types.VARCHAR));
		sqlUpdate.declareParameter(new SqlParameter("DETALLE", Types.VARCHAR));
		sqlUpdate.compile();
		result = sqlUpdate.update(param);
		
		return result;
	}

}
