package com.ixe.ods.sica.rmds.process.dao.impl;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.incrementer.OracleSequenceMaxValueIncrementer;
import org.springframework.stereotype.Repository;

import com.ixe.ods.sica.rmds.feed.dto.CurrencyPriceDto;
import static com.ixe.ods.sica.rmds.feed.util.Constantes.*;
import com.ixe.ods.sica.rmds.process.dao.SicaVariacionDao;
import com.ixe.ods.sica.rmds.process.service.impl.SicaVariacionServiceImpl;

@Repository
public class JdbcMapSicaVariacionDao implements SicaVariacionDao {
	
	/**
	 * La instancia de utilidad para grabar mensajes en el log.
	 */
	private Logger logger = Logger.getLogger(JdbcMapSicaVariacionDao.class.getName());
	
	@Autowired
	private DataSource dataSource;
	
	@Autowired
	private OracleSequenceMaxValueIncrementer variacionIncrementer;

	public boolean storeVariacion(Map<String, CurrencyPriceDto> sicaVariacionCache){
		
		SimpleJdbcInsert sicaVariacionInsert = new SimpleJdbcInsert(dataSource)
			.withTableName(VARIACION_TAB_NAME);
		
		sicaVariacionInsert.includeSynonymsForTableColumnMetaData();
		
		Map<String, Object> sicaVariacionRow = getMapSicaVariacioRow(sicaVariacionCache);
		
		sicaVariacionInsert.execute(sicaVariacionRow);
		
		logger.info("Registro insertado en " + VARIACION_TAB_NAME + "id: " + sicaVariacionRow.get(ID_VARIACION_COLUMN));
		
		return true;
	}
	
	
	private Map<String, Object> getMapSicaVariacioRow(Map<String, CurrencyPriceDto> sicaVariacionCache){
		
		Map<String, Object> sicaVariacionRow = new LinkedHashMap<String, Object>();
		
		for(String ric : sicaVariacionCache.keySet()){
			
			CurrencyPriceDto priceDto = sicaVariacionCache.get(ric);
			
			if(ric.equals(USD_RIC)){
				sicaVariacionRow.put(VARIACION_MID_COLUMN, priceDto.getMid() );
				sicaVariacionRow.put(VARIACION_VTA_SPOT_COLUMN, priceDto.getBid() );
				sicaVariacionRow.put(VARIACION_VTA_SPOT_ASK_COLUMN, priceDto.getAsk() );
			}else if(ric.equals(EUR_MXN_RIC)){
				sicaVariacionRow.put(MXN_EUR_VARIACION_VTA_SPOT_COLUMN, priceDto.getBid() );
				sicaVariacionRow.put(MXN_EUR_VARIACION_VTA_SPOT_ASK_COLUMN, priceDto.getAsk() );
			}else if(ric.equals(CAD_RIC)){
				sicaVariacionRow.put(CAD_VARIACION_VTA_SPOT_COLUMN, priceDto.getBid() );
				sicaVariacionRow.put(CAD_VARIACION_VTA_SPOT_ASK_COLUMN, priceDto.getAsk() );
			}else if(ric.equals(CHF_RIC)){
				sicaVariacionRow.put(CHF_VARIACION_VTA_SPOT_COLUMN, priceDto.getBid() );
				sicaVariacionRow.put(CHF_VARIACION_VTA_SPOT_ASK_COLUMN, priceDto.getAsk() );
			}else if(ric.equals(EUR_RIC)){
				sicaVariacionRow.put(EUR_VARIACION_VTA_SPOT_COLUMN, priceDto.getBid() );
				sicaVariacionRow.put(EUR_VARIACION_VTA_SPOT_ASK_COLUMN, priceDto.getAsk() );
			}else if(ric.equals(GBP_RIC)){
				sicaVariacionRow.put(GBP_VARIACION_VTA_SPOT_COLUMN, priceDto.getBid() );
				sicaVariacionRow.put(GBP_VARIACION_VTA_SPOT_ASK_COLUMN, priceDto.getAsk() );
			}else if(ric.equals(JPY_RIC)){
				sicaVariacionRow.put(JPY_VARIACION_VTA_SPOT_COLUMN, priceDto.getBid() );
				sicaVariacionRow.put(JPY_VARIACION_VTA_SPOT_ASK_COLUMN, priceDto.getAsk() );
			}
		}
		
		sicaVariacionRow.put(ID_VARIACION_COLUMN, variacionIncrementer.nextLongValue());
		//TODO: Revisar el parametro para identificar el OMM
		sicaVariacionRow.put(ORIGEN_COLUMN, OMMCONS_ORIGEN);
		sicaVariacionRow.put(FECHA_COLUMN, new Date() );
		
		return sicaVariacionRow;
	}
	
	public DataSource getDatasource() {
		return dataSource;
	}


	public void setDatasource(DataSource datasource) {
		this.dataSource = datasource;
	}
	
	private static final String VARIACION_TAB_NAME = "SICA_VARIACION";
	
	private static final String OMMCONS_ORIGEN = "R";
	
	private static final String FECHA_COLUMN = "FECHA";
	
	private static final String ID_VARIACION_COLUMN = "ID_VARIACION";
	
	private static final String VARIACION_MID_COLUMN = "VARIACION_MID";
	
	private static final String VARIACION_VTA_SPOT_COLUMN = "VARIACION_VTA_SPOT";
	
	private static final String VARIACION_VTA_SPOT_ASK_COLUMN = "VARIACION_VTA_SPOT_ASK";
	
	private static final String MXN_EUR_VARIACION_VTA_SPOT_COLUMN = "MXN_EUR_VARIACION_VTA_SPOT";
	
	private static final String MXN_EUR_VARIACION_VTA_SPOT_ASK_COLUMN = "MXN_EUR_VARIACION_VTA_SPOT_ASK";
	
	private static final String CAD_VARIACION_VTA_SPOT_COLUMN = "CAD_VARIACION_VTA_SPOT";
	
	private static final String CAD_VARIACION_VTA_SPOT_ASK_COLUMN = "CAD_VARIACION_VTA_SPOT_ASK";
	
	private static final String CHF_VARIACION_VTA_SPOT_COLUMN = "CHF_VARIACION_VTA_SPOT";
	
	private static final String CHF_VARIACION_VTA_SPOT_ASK_COLUMN = "CHF_VARIACION_VTA_SPOT_ASK";
	
	private static final String EUR_VARIACION_VTA_SPOT_COLUMN = "EUR_VARIACION_VTA_SPOT";
	
	private static final String EUR_VARIACION_VTA_SPOT_ASK_COLUMN = "EUR_VARIACION_VTA_SPOT_ASK";
	
	private static final String GBP_VARIACION_VTA_SPOT_COLUMN = "GBP_VARIACION_VTA_SPOT";
	
	private static final String GBP_VARIACION_VTA_SPOT_ASK_COLUMN = "GBP_VARIACION_VTA_SPOT_ASK";
	
	private static final String JPY_VARIACION_VTA_SPOT_COLUMN = "JPY_VARIACION_VTA_SPOT";
	
	private static final String JPY_VARIACION_VTA_SPOT_ASK_COLUMN = "JPY_VARIACION_VTA_SPOT_ASK";
	
	private static final String ORIGEN_COLUMN = "ORIGEN";

}
