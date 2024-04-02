package com.banorte.reporte.UIF.transferencias.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.object.MappingSqlQuery;
import org.springframework.jdbc.object.SqlUpdate;
import org.springframework.jdbc.object.StoredProcedure;

import com.banorte.reporte.UIF.transferencias.dao.BaseDao;
import com.banorte.reporte.UIF.transferencias.dao.IConsultaDao;
import com.banorte.reporte.UIF.transferencias.dto.DatosTransferencia;
import com.banorte.reporte.UIF.transferencias.dto.DatosTransferenciaRecibida;


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
	
	public boolean ejecutarStoredProcedure()
	{
		boolean resultado = false;
		LOG.warn("--> Inicia la ejecución del proceso mensual de transferencias para la UIF...");
		StoredProcesaTransferencias stored = new StoredProcesaTransferencias(getDataSource(), nombreStored);
		resultado = stored.execute();
		LOG.warn("--> Finaliza la ejecución del proceso mensual de transferencias para la UIF.\n\n");
		
		return resultado;
	}
	
	public List<DatosTransferencia> consultarTransferenciasReporte()
	{
		List<DatosTransferencia> transferencias = null;
		
		LOG.warn("--> Inicia consulta de transferencias para el reporte mensual para la UIF....");
		ConsultaDatosOperaciones query = new ConsultaDatosOperaciones(getDataSource(), QUERY_REPORTE_MENSUAL_TRANSFERENCIAS);
		transferencias = query.execute();
		
		if(transferencias != null)
			LOG.warn("--> Total de registros encontrados: " + transferencias.size() + "\n");
		
		LOG.warn("--> Termina consulta de transferencias.\n\n");
		
		return transferencias;
	}
	
	public void actualizaBanderaEnvUIF(){
		LOG.warn("--> Inicia actualizacion de bandera que pone a bandera de enviada a la UIF en 1 ....");
		SqlUpdate sqlUpdate = new SqlUpdate(getDataSource(), UPDATE_BANDERA_ENV_UIF);
		sqlUpdate.compile();
		sqlUpdate.update();

	}
	
	public void borrarOperacionesNoReportadas(){
		LOG.warn("--> Inicia borrado de datos que no se pudieron enviar a la UIF ....");
		SqlUpdate sqlUpdate = new SqlUpdate(getDataSource(), BORRAR_REGISTROS_NO_ENV_UIF);
		sqlUpdate.compile();
		sqlUpdate.update();

	}
	
	public List<DatosTransferenciaRecibida> consultarTransferenciasReporteRecibidas()
	{
		List<DatosTransferenciaRecibida> transferencias = null;
		
		LOG.warn("--> Inicia consulta de transferencias Recibidas para el reporte mensual para la UIF....");
		ConsultaDatosOperacionesRec query = new ConsultaDatosOperacionesRec(getDataSource(), QUERY_REPORTE_MENSUAL_TRANSFERENCIAS_REC);
		transferencias = query.execute();
		
		if(transferencias != null)
			LOG.warn("--> Total de registros encontrados: " + transferencias.size() + "\n");
		
		LOG.warn("--> Termina consulta de transferencias Recibidas.\n\n");
		
		return transferencias;
	}
	
	public void actualizaBanderaEnvUIFRec(){
		LOG.warn("--> Inicia actualizacion de bandera que pone a bandera de enviada a la UIF en 1 de OP. Recibidas....");
		SqlUpdate sqlUpdate = new SqlUpdate(getDataSource(), UPDATE_BANDERA_ENV_UIF_REC);
		sqlUpdate.compile();
		sqlUpdate.update();

	}
	
	public void borrarOperacionesNoReportadasRec(){
		LOG.warn("--> Inicia borrado de datos que no se pudieron enviar a la UIF de Operaciones Recibidas ....");
		SqlUpdate sqlUpdate = new SqlUpdate(getDataSource(), BORRAR_REGISTROS_NO_ENV_UIF_REC);
		sqlUpdate.compile();
		sqlUpdate.update();

	}
	
	public List<DatosTransferenciaRecibida> consultarDatosOrdenantesNoReportados()
	{
		List<DatosTransferenciaRecibida> ordenantes = null;
		
		LOG.warn("--> Inicia consulta de ordenantes no reportados ....");
		ConsultaDatosOperacionesRec query = new ConsultaDatosOperacionesRec(getDataSource(), QUERY_CONSULTA_ORDENANTES_NO_REPORTADOS);
		ordenantes = query.execute();
		
		if(ordenantes != null)
			LOG.warn("--> Total de registros encontrados: " + ordenantes.size() + "\n");
		
		LOG.warn("--> Termina consulta de ordenantes no reportados.\n\n");
		
		return ordenantes;
	}

	
	private class StoredProcesaTransferencias extends StoredProcedure
	{
		public StoredProcesaTransferencias(DataSource ds, String nombreStore) 
		{
			super(ds, nombreStore);
			declareParameter(new SqlOutParameter("v_log", Types.VARCHAR));
			setFunction(false);
			compile();
		}
		
		public boolean execute() 
		{
            Map<Object,Object> parametrosIn = Collections.emptyMap();
            String log = (String) this.execute(parametrosIn).get("v_log");
            LOG.info("DBMSOUTPUT: " + log);
            return true;
        }

	}
	
	private class ConsultaDatosOperaciones extends MappingSqlQuery<DatosTransferencia> 
	{	
		public ConsultaDatosOperaciones(DataSource ds, String sql) {
			super(ds, sql);
		}

		@Override
		protected DatosTransferencia mapRow(ResultSet rs, int arg1) throws SQLException 
		{	
			DatosTransferencia datos = new DatosTransferencia();
			//datos.setIdScTraextEnv(rs.getInt(1)); // ID_SC_TRAEXT_ENV NUMBER,
			if(rs.getString(1) != null)
			datos.setFecha(rs.getString(1));//(new Date(rs.getDate(1).getTime()));
			datos.setReferencia(rs.getString(2)); 
		    datos.setDeal(rs.getString(3));
		    datos.setFolioDetalle(rs.getString(4));
		    datos.setNombreCorresponsal(rs.getString(5));
		    datos.setNombreEntidadDestino(rs.getString(6));
		    datos.setBicAba(rs.getString(7));
		    datos.setDatosDestinatario(rs.getString(8));
		    datos.setOperacionOrigen(rs.getString(9));
		    datos.setOperacionDestino(rs.getString(10));
		    datos.setInstrumentoMonetarioOrigen(rs.getString(11));
		    datos.setMonedaOrigen(rs.getString(12));
		    datos.setMontoInstrumentoOrigen(rs.getString(13));
		    datos.setMensaje(rs.getString(14));
		    datos.setTipoPersona(rs.getString(15));       //setFechaNac(rs.getString(15));//(new Date(rs.getDate(15).getTime()));
		    datos.setNombre(rs.getString(16));
		    datos.setApellidoPaterno(rs.getString(17));
		    datos.setApellidoMaterno(rs.getString(18));
		    datos.setFechaNacimiento(rs.getString(19));
		    datos.setCurp(rs.getString(20));
		    datos.setRfc(rs.getString(21));
		    datos.setPaisNacimiento(rs.getString(22));
		    datos.setPaisNacionalidad(rs.getString(23));
		    datos.setActividadEconomica(rs.getString(24));
		    datos.setRazonSocial(rs.getString(25));
		    datos.setFechaConstitucion(rs.getString(26));
		    datos.setRfcPM(rs.getString(27));
		    datos.setPaisNacionalidadPM(rs.getString(28));
		    datos.setGiroMercantil(rs.getString(29));
		    datos.setApoderadoLegal(rs.getString(30));
		    datos.setDomicilioUnificado(rs.getString(31));
		    datos.setCiudadPoblacion(rs.getString(32));
		    datos.setCodigoPostal(rs.getString(33));
		    datos.setEntidadFederativa(rs.getString(34));
		    datos.setTelefono(rs.getString(35));
		    datos.setNumeroTelefono(rs.getString(36));
		    datos.setClavePais(rs.getString(37));
		    datos.setExtension(rs.getString(38));
		    datos.setCorreoElectronico(rs.getString(39));
		    datos.setFiel(rs.getString(40));
		    datos.setMontoUsdMayorIgual1000(rs.getString(41));
		    datos.setPaisBancoBeneficiario(rs.getString(42));
			return datos;   
		}
    }
	
	private class ConsultaDatosOperacionesRec extends MappingSqlQuery<DatosTransferenciaRecibida> 
	{	
		public ConsultaDatosOperacionesRec(DataSource ds, String sql) {
			super(ds, sql);
		}

		@Override
		protected DatosTransferenciaRecibida mapRow(ResultSet rs, int arg1) throws SQLException 
		{	
			DatosTransferenciaRecibida datos = new DatosTransferenciaRecibida();
			//datos.setIdScTraextEnv(rs.getInt(1)); // ID_SC_TRAEXT_ENV NUMBER,
			if(rs.getString(1) != null)
			datos.setFecha(rs.getString(1));//(new Date(rs.getDate(1).getTime()));
			datos.setReferencia(rs.getString(2)); 
		    datos.setDeal(rs.getString(3));
		    datos.setFolioDetalle(rs.getString(4));
		    datos.setNombreCorresponsal(rs.getString(5));
		    datos.setNombreEntidadOrigen(rs.getString(6));
		    datos.setBicAba(rs.getString(7));
		    datos.setDatosOrdenante(rs.getString(8));
		    datos.setOperacionOrigen(rs.getString(9));
		    datos.setNombreEntidadDestino(rs.getString(10));
		    datos.setDatosDestinatario(rs.getString(11));
		    datos.setOperacionDestino(rs.getString(12));
		    datos.setInstrumentoMonetarioDestino(rs.getString(13));
		    datos.setMonedaDestino(rs.getString(14));
		    datos.setMontoInstrumentoDestino(rs.getString(15));       //setFechaNac(rs.getString(15));//(new Date(rs.getDate(15).getTime()));
		    datos.setMensaje(rs.getString(16));
		    datos.setTipoPersona(rs.getString(17));
		    datos.setNombre(rs.getString(18));
		    datos.setApellidoPaterno(rs.getString(19));
		    datos.setApellidoMaterno(rs.getString(20));
		    datos.setFechaNacimiento(rs.getString(21));
		    datos.setCurp(rs.getString(22));
		    datos.setRfc(rs.getString(23));
		    datos.setPaisNacimiento(rs.getString(24));
		    datos.setPaisNacionalidad(rs.getString(25));
		    datos.setActividadEconomica(rs.getString(26));
		    datos.setRazonSocial(rs.getString(27));
		    datos.setFechaConstitucion(rs.getString(28));
		    datos.setGiroMercantil(rs.getString(29));
		    datos.setApoderadoLegal(rs.getString(30));
		    datos.setDomicilioUnificado(rs.getString(31));
		    datos.setCiudadPoblacion(rs.getString(32));
		    datos.setCodigoPostal(rs.getString(33));
		    datos.setTelefono(rs.getString(34));
		    datos.setNumeroTelefono(rs.getString(35));
		    datos.setClavePais(rs.getString(36));
		    datos.setExtension(rs.getString(37));
		    datos.setCorreoElectronico(rs.getString(38));
		    datos.setFiel(rs.getString(39));
		    datos.setMontoUsdMayorIgual1000(rs.getString(40));
		    datos.setPaisClienteOriginador(rs.getString(41));
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