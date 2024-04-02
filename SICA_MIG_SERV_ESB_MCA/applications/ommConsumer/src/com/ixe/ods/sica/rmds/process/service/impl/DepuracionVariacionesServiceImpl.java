package com.ixe.ods.sica.rmds.process.service.impl;

import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ixe.ods.sica.rmds.feed.util.Helper;
import com.ixe.ods.sica.rmds.process.dao.DepuracionTablasPreciosDao;
import com.ixe.ods.sica.rmds.process.service.DepuracionVariacionesService;

/**
 * Proporciona funcionalidades para la depuraci&oacute;n de registros
 * generados en BD derivados de la actualizaci&oacute;n de precios de
 * divisas en el SICA.
 * 
 * @author Efren Trinidad, Na-at Technologies
 *
 * Jul 25, 2011 1:03:33 PM
 */
@Service
public class DepuracionVariacionesServiceImpl implements DepuracionVariacionesService {
	
	/**
	 * La instancia de utilidad para grabar mensajes en el log.
	 */
	public static Logger logger = Logger.getLogger(DepuracionVariacionesServiceImpl.class);
	
	
	/**
	 * Instancia DAO para la comunicaci&oacute;n con la BD. Instanciada por
	 * Spring Framework.
	 */
	@Autowired
	DepuracionTablasPreciosDao depuracionTablasPrecios;
	
	/**
	 * @see com.ixe.ods.sica.rmds.process.service.DepuracionVariacionesService#depurarVariacionPrecios()
	 */
	@Transactional
	public boolean depurarVariacionPrecios(){
		
		Date inicioDia = Helper.inicioDia(new Date());
		Date finDia = Helper.finDia(new Date());
		logger.info("elimnando precios referencia.............");
		depuracionTablasPrecios.limpiarPreciosReferencia(inicioDia, finDia);
		logger.info("elimnando precios Factores Divisa.............");
		depuracionTablasPrecios.limpiarFactoresDivisa(inicioDia, finDia);
		logger.info("elimnando precios Variaciones Divisa.............");
		depuracionTablasPrecios.limpiarVariacionesDivisa(inicioDia, finDia);
		
		return true;
	}

}
