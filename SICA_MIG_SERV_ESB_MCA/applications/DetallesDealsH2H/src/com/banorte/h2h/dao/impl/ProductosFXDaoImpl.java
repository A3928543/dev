package com.banorte.h2h.dao.impl;

import java.util.HashMap;
import java.util.Map;


//import org.apache.log4j.Logger;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

//import com.banorte.h2h.dao.BaseDao;
import com.banorte.h2h.dao.ProductosFXDao;
import com.banorte.h2h.dto.DetalleH2H;

public class ProductosFXDaoImpl extends JdbcDaoSupport implements ProductosFXDao 
{
	//private static final Logger log = Logger.getLogger(ProductosFXDaoImpl.class);
	
	private SimpleJdbcInsert insertarDetalleH2H = null;
	
	public void guardarDetalleH2H(DetalleH2H detalleH2H)
	{
		insertarDetalleH2H = new SimpleJdbcInsert(getDataSource());
		insertarDetalleH2H.withTableName(TABLA_PRODUCTOS_FX);
		insertarDetalleH2H.usingColumns(COLUMNAS_PRODUCTOS_FX);
		insertarDetalleH2H.setAccessTableColumnMetaData(false);
		insertarDetalleH2H.compile();
		
		Map<String, Object> datos = new HashMap<String, Object>();
		datos.put(FC, detalleH2H.getIndiceParaH2H());
		datos.put(FCC, detalleH2H.getIndiceParaH2H());
		datos.put(VERSION, detalleH2H.getVersion());
		datos.put(TIPO, detalleH2H.getTipoOperacion());
		datos.put(FECHA, detalleH2H.getFechaCaptura());
		datos.put(FEFECTIVA, detalleH2H.getFechaEfectiva());
		datos.put(FVEN, detalleH2H.getFechaLiquidacion());
		datos.put(IMPORTE, detalleH2H.getImporte().doubleValue());
		datos.put(IMPORTEUSD, detalleH2H.getMontoDolarizado().doubleValue());
		datos.put(TCDIV, detalleH2H.getFactorDivisaUsd().doubleValue());
		datos.put(TC, detalleH2H.getTipoCambio().doubleValue());
		datos.put(DIVISA, detalleH2H.getIdDivisa());
		datos.put(PLAZO,detalleH2H.getPlazo());
		datos.put(PRODUCTO,detalleH2H.getProducto());
		datos.put(NUM_CTE, detalleH2H.getClaveContraparte());
		datos.put(NOMBRE, detalleH2H.getNombreCliente());
		datos.put(RFC, detalleH2H.getRfc());
		datos.put(SWAP, new Integer(0));
		datos.put(TIPCTEH2H, detalleH2H.getTipoCliente());
		datos.put(STATUSH2H, detalleH2H.getStatusH2H());
		datos.put(ENVIADA, detalleH2H.getEnviada());
		datos.put(BANH2H, null);
		datos.put(COERRORH2H, null);
		datos.put(DSERRORH2H, null);
		
		insertarDetalleH2H.execute(datos);
		
		detalleH2H.setProcesadoOk(true);
	}
}
