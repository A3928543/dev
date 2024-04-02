package com.banorte.ods.sica.batch.transfint.dao.impl;

import java.sql.Types;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;
import org.springframework.stereotype.Repository;

import com.banorte.ods.sica.batch.transfint.dao.ScSpCargaDao;

/**
 * The Class ScSpCargaDaoImpl.
 */
@Repository("scSpCargaDao")
public class ScSpCargaDaoImpl implements ScSpCargaDao {
	
	/** The Constant LOG. */
	private static final Logger LOG = LoggerFactory.getLogger(ScSpCargaDaoImpl.class);
		
	/** The data source. */
	private DataSource dataSource;
	   
	/**
	 * Sets the data source.
	 *
	 * @param dataSource the new data source
	 */
	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	   
	/**
	 * Ejecuta carga.
	 *
	 * @param fechaIni the fecha ini
	 * @param fechaFin the fecha fin
	 * @param tipoReporte the tipo reporte
	 * @return the map
	 */	
	public Map<String, Object> ejecutaCarga(Date fechaIni, Date fechaFin, Short tipoReporte) {
		StoredProcesaTransferencias stored = new StoredProcesaTransferencias(dataSource);
		
		return stored.execute(fechaIni, fechaFin, tipoReporte);
	}
	
	
	/**
	 * The Class StoredProcesaTransferencias.
	 */
	private class StoredProcesaTransferencias extends StoredProcedure {
		
		/**
		 * Instantiates a new stored procesa transferencias.
		 *
		 * @param ds the ds
		 */
		public StoredProcesaTransferencias(DataSource ds) {
			super(ds, SP_CARGA_TRANSFINT);
			declareParameter(new SqlParameter("p_fecha_ini", Types.TIMESTAMP));
			declareParameter(new SqlParameter("p_fecha_fin", Types.TIMESTAMP));
			declareParameter(new SqlParameter("p_tipo_reporte", Types.INTEGER));
			declareParameter(new SqlOutParameter("p_id_reporte", Types.NUMERIC));
			declareParameter(new SqlOutParameter("p_codigo", Types.INTEGER));
			declareParameter(new SqlOutParameter("p_mensaje", Types.VARCHAR));
			setFunction(false);
			compile();
		}
		
		/**
		 * Execute.
		 *
		 * @param fechaIni the fecha ini
		 * @param fechaFin the fecha fin
		 * @param tipoReporte the tipo reporte
		 * @return the map
		 */
		public Map<String, Object> execute(Date fechaIni, Date fechaFin, Short tipoReporte) {
            Map<String, Object> parametrosIn = new HashMap<String, Object>();
            parametrosIn.put("p_fecha_ini", fechaIni);
            parametrosIn.put("p_fecha_fin", fechaFin);
            parametrosIn.put("p_tipo_reporte", tipoReporte);
            LOG.debug("Parametros de SP: {}", parametrosIn);
            
            return this.execute(parametrosIn);
        }

	}
}
