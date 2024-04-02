package com.ixe.ods.sica.rmds.process.dao.impl;


import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.ixe.ods.sica.rmds.process.dao.SicaParametroOMMDao;

import static com.ixe.ods.sica.rmds.feed.util.Constantes.OMM_CONNECTION_PROPERTIES_FILE_NAME;
import static java.sql.Types.*;

@Repository
public class JdbcSicaParametroOMM extends JdbcTemplate 
	implements SicaParametroOMMDao {
	
	/**
	 * La instancia de utilidad para grabar mensajes en el log.
	 */
	public static Logger logger = Logger.getLogger(JdbcSicaParametroOMM.class.getName());
	
	
	private static final String PARAMETRO_QUERY = "SELECT VALOR FROM sc_parametro WHERE id_parametro = ?";
	
	@Override
	public <T> T findPametroOMMById(String idParametro, T requiredType) {
		
		T valor = null;
		
		try{
			valor = (T) queryForObject(PARAMETRO_QUERY, new Object[]{idParametro},
					requiredType.getClass() );
		}catch(EmptyResultDataAccessException erdae){
			logger.error("El parametro " + idParametro + " no se encuentra configurado.");
			throw erdae;
		}
		
		return valor;
	}
	
	@Autowired
	public void setDatasource(DataSource datasource){
		setDataSource(datasource);
	}

}
