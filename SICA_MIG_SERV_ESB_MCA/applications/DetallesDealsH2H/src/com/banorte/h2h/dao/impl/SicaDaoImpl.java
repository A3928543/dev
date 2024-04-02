package com.banorte.h2h.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.object.BatchSqlUpdate;
import org.springframework.jdbc.object.MappingSqlQuery;

import com.banorte.h2h.dao.SicaDao;
//import com.banorte.h2h.dao.BaseDao;
import com.banorte.h2h.dto.DetalleH2H;

public class SicaDaoImpl extends JdbcDaoSupport implements SicaDao 
{
	
	public String consultaParametro(String parametro)
	{
		String valor = null;
		List<Map<String, Object>> resultado = null;
		Map<String, Object> datos = null;
		String sql = null;
		
		if(PARAMETRO_INDICE_H2H.equals(parametro))
			sql = CONSULTA_PARAMETRO;
		
		resultado = getJdbcTemplate().queryForList(sql, new Object[] { parametro });
		if(resultado != null && resultado.size() > 0)
		{
			datos = (Map<String, Object>) resultado.get(0);
			valor = (String)datos.get("VALOR");
		}
		
		return valor;
	}
	
	public List<DetalleH2H> consultarDetallesPorCancelar()
	{
		List<DetalleH2H> detalles = null;
		ConsultaDetallesH2H consulta = new ConsultaDetallesH2H(getDataSource(), CONSULTA_DETALLES_POR_CANCELAR);
		detalles = consulta.execute();
		
		return detalles;
	}
	
	public List<DetalleH2H> consultarNuevosDetallesDealEnviarH2H()
	{
		List<DetalleH2H> detalles = null;
		ConsultaDetallesH2H consulta = new ConsultaDetallesH2H(getDataSource(), CONSULTA_NUEVOS_DETALLES_H2H);
		detalles = consulta.execute();
		return detalles;
	}
	
	public List<DetalleH2H> consultarFoliosBanxicoDetallesDealEnviadosH2H()
	{
		List<DetalleH2H> detalles = null;
		ConsultaDetallesH2H consulta = new ConsultaDetallesH2H(getDataSource(), CONSULTA_DETALLES_SIN_FOLIO_BANXICO);
		detalles = consulta.execute();
		return detalles;
	}
	
	public void actualizarDetallesSica(List<DetalleH2H> detallesH2H, int opcion)
	{
		int registros = 0;
		int types[] = null;
		String query = null;
		BatchSqlUpdate batchSqlUpdate = null;
		
		if(!detallesH2H.isEmpty())
		{
			if(opcion == OPCION_DETALLE_REPORTADO)
			{
				query = ACTUALIZA_DETALLE_H2H_SICA;
				types = BATCH_TYPES_DETALLE_REPORTADO;
			}
			else if(opcion == OPCION_FOLIO_BANXICO)
			{
				query = ACTUALIZA_FOLIO_BANXICO_DETALLE_SICA;
				types = BATCH_TYPES_FOLIOS_BANXICO;
			}
			else // OPCION_CANCELAR_DETALLE
			{
				query = ACTUALIZA_DETALLE_CANCELADO_SICA;
				types = BATCH_TYPES_DETALLE_CANCELADO;
			}
			
			batchSqlUpdate = new BatchSqlUpdate(getDataSource(), query, types, BatchSqlUpdate.DEFAULT_BATCH_SIZE);
			
			for(DetalleH2H detalleH2H : detallesH2H)
			{
				if(detalleH2H.isProcesadoOk())
				{
					if(opcion == OPCION_DETALLE_REPORTADO || opcion == OPCION_CANCELAR_DETALLE)
						batchSqlUpdate.update(detalleH2H.getIdDealPosicion());
					else if(opcion == OPCION_FOLIO_BANXICO)
					{
						batchSqlUpdate.update(detalleH2H.getFolioBanxico(), detalleH2H.getCodigoError(), detalleH2H.getIdDealPosicion());
					}
					
					registros++;
					
					if(registros == BatchSqlUpdate.DEFAULT_BATCH_SIZE)
					{
						registros = 0;
						batchSqlUpdate.flush();
					}
				}
			}
			
			if(registros > 0 && registros <= BatchSqlUpdate.DEFAULT_BATCH_SIZE)
				batchSqlUpdate.flush();
		}
	}
	
	private class ConsultaDetallesH2H extends MappingSqlQuery<DetalleH2H> 
	{	
		public ConsultaDetallesH2H(DataSource ds, String sql) {
			super(ds, sql);
		}

		@Override
		protected DetalleH2H mapRow(ResultSet rs, int arg1) throws SQLException 
		{	
			DetalleH2H detalleH2H = new DetalleH2H(); 
			detalleH2H.setIdDealPosicion(new Long(rs.getLong(1)));
			detalleH2H.setIdDeal(new Long(rs.getLong(2)));
			detalleH2H.setFechaEnvio(rs.getDate(3));
			detalleH2H.setVersion(new Integer(rs.getInt(4)));
			detalleH2H.setTipoOperacion(rs.getString(5));  // Buy:B  Sell:S
			detalleH2H.setFechaCaptura(rs.getDate(6));
			detalleH2H.setFechaEfectiva(rs.getDate(7));
			detalleH2H.setFechaLiquidacion(rs.getDate(8));
			detalleH2H.setImporte(rs.getBigDecimal(9));
			detalleH2H.setMontoDolarizado(rs.getBigDecimal(10));
			detalleH2H.setFactorDivisaUsd(rs.getBigDecimal(11));
			detalleH2H.setTipoCambio(rs.getBigDecimal(12));
			detalleH2H.setIdDivisa(rs.getString(13));
			detalleH2H.setPlazo(rs.getString(14));
			detalleH2H.setProducto(rs.getString(15));
			detalleH2H.setClaveContraparte(rs.getString(16));
			detalleH2H.setNombreCliente(rs.getString(17));
			detalleH2H.setRfc(rs.getString(18));
			detalleH2H.setTipoCliente(rs.getString(19));
			detalleH2H.setStatusH2H(rs.getString(20));
			detalleH2H.setEnviada(rs.getString(21));
			detalleH2H.setFolioBanxico(rs.getString(22));
			detalleH2H.setCodigoError(rs.getInt(23));
			detalleH2H.setDescripcionError(rs.getString(24));
			detalleH2H.setIdPersonaPromotor(rs.getLong(25));
			detalleH2H.setCierreVespertino(rs.getInt(26));
			detalleH2H.setFechaCancelacion(rs.getDate(27));
			detalleH2H.setCancelarDetalle(rs.getString(28));
			
			return detalleH2H;   
		}
    }

}
