package com.banorte.h2h.dao;

import com.banorte.h2h.dto.DetalleH2H;

public interface ProductosFXDao 
{
	String TABLA_PRODUCTOS_FX = "PRODUCTOS_FX";
	String FC = "FC";
	String FCC = "FCC";
	String VERSION = "VERSION";
	String TIPO = "TIPO";
	String FECHA = "FECHA";
	String FEFECTIVA = "FEFECTIVA";
	String FVEN = "FVEN";
	String IMPORTE = "IMPORTE";
	String IMPORTEUSD = "IMPORTEUSD";
	String TCDIV = "TCDIV";
	String TC = "TC";
	String DIVISA = "DIVISA";
	String PLAZO = "PLAZO";
	String PRODUCTO = "PRODUCTO";
	String NUM_CTE = "NUM_CTE";
	String NOMBRE = "NOMBRE";
	String RFC = "RFC";
	String SWAP = "SWAP";
	String TIPCTEH2H = "TIPCTEH2H";
	String STATUSH2H = "STATUSH2H";
	String ENVIADA = "ENVIADA";
	String BANH2H = "BANH2H";
	String COERRORH2H = "COERRORH2H";
	String DSERRORH2H = "DSERRORH2H";
	
	String[] COLUMNAS_PRODUCTOS_FX = new String[]{FC,FCC,VERSION,TIPO,FECHA,FEFECTIVA,FVEN,IMPORTE,
			                                      IMPORTEUSD,TCDIV,TC,DIVISA,PLAZO,PRODUCTO,NUM_CTE,
			                                      NOMBRE,RFC,SWAP,TIPCTEH2H,STATUSH2H,ENVIADA,BANH2H,
			                                      COERRORH2H,DSERRORH2H};
	
	void guardarDetalleH2H(DetalleH2H detalleH2H);
}
