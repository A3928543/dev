package com.banorte.h2h.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.object.MappingSqlQuery;

//import com.banorte.h2h.dao.BaseDao;
import com.banorte.h2h.dao.EnlaceUsdDao;
import com.banorte.h2h.dto.DetalleH2H;

public class EnlaceUsdDaoImpl extends JdbcDaoSupport implements EnlaceUsdDao 
{
	
	public List<DetalleH2H> consultarFoliosBanxicoDetallesSica(List<DetalleH2H> detallesProcesar)
	{
		List<DetalleH2H> detallesEncontrados = new ArrayList<DetalleH2H>();
		List<DetalleH2H> listaFinal = new ArrayList<DetalleH2H>();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		Calendar hoy = Calendar.getInstance();
		String query = null;
		String fechaNum = format.format(hoy.getTime());
		StringBuilder folios = new StringBuilder();
		ConsultaFoliosBanxicoDlOperacion consulta = null;
		DetalleH2H d = null;
		int contador = 0, finDeLista = detallesProcesar.size() - 1;
		
		for(int indice = 0; indice < detallesProcesar.size(); indice++)
		{
			d = detallesProcesar.get(indice);
			
			if(contador >= 1)
				folios.append("," + d.getIndiceParaH2H().toString());
			else
				folios.append(d.getIndiceParaH2H().toString());

			contador++;
				
			if(contador == 100 || indice == finDeLista)
			{
				query = CONSULTA_FOLIOS_BANXICO.replaceFirst(FECHA_ACTUAL, fechaNum).replaceFirst(IDS, folios.toString());
				consulta = new ConsultaFoliosBanxicoDlOperacion(getDataSource(), query);
				detallesEncontrados.addAll(consulta.execute());
				contador = 0;
				folios = new StringBuilder(); 
			}
		}
		
		folios = null;
		
		if(detallesEncontrados != null && !detallesEncontrados.isEmpty())
		{
			for(DetalleH2H detalleEncontrado : detallesEncontrados)
			{
				for(DetalleH2H detalleH2H : detallesProcesar)
				{
					if(detalleEncontrado.getIdDealPosicion().longValue() == detalleH2H.getIndiceParaH2H().longValue())
					{
						detalleH2H.setFolioBanxico(detalleEncontrado.getFolioBanxico());
						detalleH2H.setCodigoError(detalleEncontrado.getCodigoError());
						detalleH2H.setProcesadoOk(true);
						listaFinal.add(detalleH2H);
						break;
					}
				}
			}
				
		}
		
		return listaFinal;
	}
	
	private class ConsultaFoliosBanxicoDlOperacion extends MappingSqlQuery<DetalleH2H> 
	{	
		public ConsultaFoliosBanxicoDlOperacion(DataSource ds, String sql) {
			super(ds, sql);
		}

		@Override
		protected DetalleH2H mapRow(ResultSet rs, int arg1) throws SQLException 
		{	
			DetalleH2H detalleH2H = new DetalleH2H();
			String folio = null;
			int numFolio = -1;
			
			detalleH2H.setIdDealPosicion(new Long(rs.getLong(1)));
			
			folio = rs.getString(2);
			if(folio != null && !"".equals(folio.trim()))
			{
				try
				{
					numFolio = Integer.parseInt(folio.trim());
					if(numFolio < 0)
					{
						detalleH2H.setCodigoError(new Integer(numFolio));  
						detalleH2H.setFolioBanxico(null);
					}
					else
					{
						detalleH2H.setCodigoError(null);  
						detalleH2H.setFolioBanxico(folio);
					}
				}
				catch(NumberFormatException e){}
			}
			
			detalleH2H.setProcesadoOk(true);
			
			return detalleH2H;   
		}
    }
}
