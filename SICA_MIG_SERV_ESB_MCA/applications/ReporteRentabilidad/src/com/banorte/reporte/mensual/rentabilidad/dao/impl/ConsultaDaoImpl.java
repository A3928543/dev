package com.banorte.reporte.mensual.rentabilidad.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.jdbc.object.MappingSqlQuery;

import com.banorte.reporte.mensual.rentabilidad.dao.BaseDao;
import com.banorte.reporte.mensual.rentabilidad.dao.IConsultaDao;
import com.banorte.reporte.mensual.rentabilidad.dto.DatosOperacion;


public class ConsultaDaoImpl extends BaseDao implements IConsultaDao
{

	private static final Logger LOG = Logger.getLogger(ConsultaDaoImpl.class);
	
	private String nombreStored;
	private String parametroPathReporte;
	private String parametroEmailsTo;

	public String consultarParametro(int opcion)
	{
		String valor = null;
		String sql = "select VALOR from SC_PARAMETRO where ID_PARAMETRO = ?";
		String parametro = null;
		List<Map<String, Object>> resultado = null;
		Map<String, Object> datos = null;
		
		switch(opcion)
		{
			case PARAMETRO_PATH_REPORTE:
				parametro = parametroPathReporte;
				break;
			case PARAMETRO_EMAILS_TO:
				parametro = parametroEmailsTo;
				break;
			default:
				parametro = "INCORRECTO";
		}
		
		resultado = getJdbcTemplate().queryForList(sql, new Object[] { parametro });
		
		if(resultado != null && resultado.size() > 0)
		{
			datos = (Map<String, Object>) resultado.get(0);
			valor = (String)datos.get("VALOR");
		}
		
		//valor = (String)getJdbcTemplate().queryForObject(sql, new Object[] { parametro }, String.class);
		
		return valor;
	}
	
	
	public List<DatosOperacion> consultarOperacionesReporte()
	{
		List<DatosOperacion> operaciones = null;
		
		LOG.warn("--> Inicia consulta de operaciones pactadas en SICA para el reporte mensual de Rentabilidad....");
		ConsultaDatosOperaciones query = new ConsultaDatosOperaciones(getDataSource(), QUERY_REPORTE_MENSUAL_RENTABILIDAD);
		operaciones = query.execute();
		
		if(operaciones != null)
			LOG.warn("--> Total de registros encontrados: " + operaciones.size() + "\n");
		
		LOG.warn("--> Termina consulta de operaciones del reporte de rentbilidad.\n\n");
		
		return operaciones;
	}
	
	
	
	
	
	private class ConsultaDatosOperaciones extends MappingSqlQuery<DatosOperacion> 
	{	
		public ConsultaDatosOperaciones(DataSource ds, String sql) {
			super(ds, sql);
		}

		@Override
		protected DatosOperacion mapRow(ResultSet rs, int arg1) throws SQLException 
		{	
			DatosOperacion datos = new DatosOperacion();

			if(rs.getString(1) != null)
			datos.setIdDeal(rs.getString(1));//(new Date(rs.getDate(1).getTime()));
			datos.setIdCanal(rs.getString(2)); 
		    datos.setFolio(rs.getString(3));
		    datos.setFechaValor(rs.getString(4));
		    datos.setFechaCaptura(rs.getString(5));
		    datos.setFechaLiq(rs.getString(6));
		    datos.setMnemonico(rs.getString(7));
		    datos.setOperacion(rs.getString(8));
		    datos.setCliente(rs.getString(9));
		    datos.setProducto(rs.getString(10));
		    datos.setMonto(rs.getString(11));
		    datos.setIdDivisa(rs.getString(12));
		    datos.setTipoCambioCli(rs.getString(13));
		    datos.setTipoCambioMesa(rs.getString(14));
		    datos.setMontoEquiv(rs.getString(15));
		    datos.setUtilidad(rs.getString(16));
		    datos.setPromotor(rs.getString(17));
		    datos.setIdPromotor(rs.getString(18));
		    datos.setNombreUsuCaptura(rs.getString(19));
		    datos.setIdUsuCaptura(rs.getString(20));
		    datos.setClaveUsuCaptura(rs.getString(21));
		    datos.setContratoSica(rs.getString(22));
		    datos.setStatus(rs.getString(23));
		    datos.setMesa(rs.getString(24));
		    datos.setSectorEconomico(rs.getString(25));
		    datos.setSectorBanxico(rs.getString(26));

			return datos;   
		}
    }
	

	

	public String getNombreStored() 
	{ 
		return nombreStored; 
	}
	
	public void setNombreStored(String nombreStored) 
	{ 
		this.nombreStored = nombreStored; 
	}

	
	public String getParametroPathReporte() 
	{ 
		return parametroPathReporte; 
	}

	public void setParametroPathReporte(String parametroPathReporte) 
	{ 
		this.parametroPathReporte = parametroPathReporte; 
	}

	
	public String getParametroEmailsTo() 
	{ 
		return parametroEmailsTo;
	}

	public void setParametroEmailsTo(String parametroEmailsTo) 
	{ 
		this.parametroEmailsTo = parametroEmailsTo; 
	}

}