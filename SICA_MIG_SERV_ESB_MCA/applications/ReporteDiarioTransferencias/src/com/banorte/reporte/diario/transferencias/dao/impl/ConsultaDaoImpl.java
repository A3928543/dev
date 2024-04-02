package com.banorte.reporte.diario.transferencias.dao.impl;

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

import com.banorte.reporte.diario.transferencias.dao.BaseDao;
import com.banorte.reporte.diario.transferencias.dao.IConsultaDao;
import com.banorte.reporte.diario.transferencias.dto.DatosTransferencia;

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
		LOG.warn("--> Inicia la ejecución del proceso de transferencias...");
		StoredProcesaTransferencias stored = new StoredProcesaTransferencias(getDataSource(), nombreStored);
		resultado = stored.execute();
		LOG.warn("--> Finaliza la ejecución del proceso de transferencias.\n\n");
		
		return resultado;
	}
	
	public List<DatosTransferencia> consultarTransferenciasReporte()
	{
		List<DatosTransferencia> transferencias = null;
		
		LOG.warn("--> Inicia consulta de transferencias para el reporte diario....");
		ConsultaDatosOperaciones query = new ConsultaDatosOperaciones(getDataSource(), QUERY_REPORTE_DIARIO_TRANSFERENCIAS);
		transferencias = query.execute();
		
		if(transferencias != null)
			LOG.warn("--> Total de registros encontrados: " + transferencias.size() + "\n");
		
		LOG.warn("--> Termina consulta de transferencias.\n\n");
		
		return transferencias;
	}
	
	public void actualizaBanderaEnvBanxico(){
		LOG.warn("--> Inicia actualizacion de bandera que pone a bandera de enviada a Banxico en 1 ....");
		SqlUpdate sqlUpdate = new SqlUpdate(getDataSource(), UPDATE_BANDERA_ENV_BANXICO);
		sqlUpdate.compile();
		sqlUpdate.update();

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
            String log = (String)this.execute(parametrosIn).get("v_log");
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
			datos.setFechaOperacion(rs.getString(1));//(new Date(rs.getDate(1).getTime())); // FECHA_OPERACION DATE,
			datos.setTipoOperacion(rs.getString(2)); // TIPO_OPERACION VARCHAR(2),
		    datos.setTipoTransferencia(rs.getString(3)); // TIPO_TRANSFERENCIA VARCHAR(1),
		    datos.setIdOperacion(rs.getString(4)); // ID_OPERACION VARCHAR(20),
		    datos.setMedio(rs.getString(5)); // MEDIO VARCHAR(1),
		    datos.setOtroMedio(rs.getString(6)); // MEDIO VARCHAR(1),
		    datos.setFolio(rs.getString(7)); // FOLIO VARCHAR(20),
		    datos.setIdInstReporta(rs.getString(8)); // ID_INST_REPORTA VARCHAR(6),
		    datos.setIdBcoCliOriginador(rs.getString(9)); // ID_BCO_CLI_ORIGINADOR VARCHAR(6),
		    datos.setIdCliOriginador(rs.getString(10)); // ID_CLI_ORIGINADOR VARCHAR(20),
		    datos.setTipoCliOriginador(rs.getString(11)); // TIPO_CLI_ORIGINADOR VARCHAR(20),
		    datos.setRazonSocialCli(rs.getString(12)); // RAZON_SOCIAL_CLI VARCHAR(150),
		    datos.setApPaternoCli(rs.getString(13)); // AP_PATERNO_CLI VARCHAR(50),
		    datos.setApMaternoCli(rs.getString(14)); // AP_MATERNO_CLI VARCHAR(50),
		    datos.setApNombreCli(rs.getString(15)); // AP_NOMBRE_CLI VARCHAR(50),
		    if (rs.getString(16) != null)
		    datos.setFechaNac(rs.getString(16));       //setFechaNac(rs.getString(15));//(new Date(rs.getDate(15).getTime())); // FECHA_NAC DATE,
		    datos.setNacionCliOriginador(rs.getString(17));
		    datos.setTipoIdOriginadorExt(rs.getString(18));
		    datos.setIdOriginadorExt(rs.getString(19));
		    datos.setSexoCliOriginador(rs.getString(20));
		    datos.setEdoNacCliOriginador(rs.getString(21));
		    datos.setTipoCuentaOrd(rs.getString(22)); // TIPO_CUENTA_ORD VARCHAR(1),
		    datos.setCuentaOrd(rs.getString(23)); // CUENTA_ORD VARCHAR(18),
		    datos.setTipoIdInstRecept(rs.getString(24)); // TIPO_ID_INST_RECEPT VARCHAR(1),
		    datos.setIdInstRecept(rs.getString(25)); // ID_INST_RECEPT VARCHAR(20),
		    datos.setTipoIdInstBenef(rs.getString(26)); // TIPO_ID_INST_BENEF VARCHAR(1),
		    datos.setIdInstBenef(rs.getString(27)); // ID_INST_BENEF VARCHAR(11),
		    datos.setTipoBenef(rs.getString(28)); // TIPO_BENEF VARCHAR(1),
		    datos.setNombreRazonSocBenef(rs.getString(29)); // NOMBRE_RAZON_SOC_BENEF VARCHAR(150),
		    datos.setCuentaBenef(rs.getString(30)); // CUENTA_BENEF VARCHAR(35),
		    //datos.setMontoOp(rs.getBigDecimal(25)); // MONTO_OP NUMBER(16,2),
		    datos.setPaisBcoBenef(rs.getString(31));
		    datos.setMontoOp(rs.getString(32)); // MONTO_OP NUMBER(16,2),
		    datos.setIdDivisa(rs.getString(33)); // ID_DIVISA VARCHAR(3),
		    datos.setObservaciones(rs.getString(34)); // OBSERVACIONES VARCHAR(100),
		    datos.setFolioTransNacional(rs.getString(35)); // FOLIO_TRANS_NACIONAL VARCHAR(20),
		    datos.setTipoFondeoTrans(rs.getString(36)); // TIPO_FONDEO_TRANS VARCHAR(1),
		    datos.setFondeoTrans(rs.getString(37)); // FONDEO_TRANS VARCHAR(100),
		    //datos.setReportadaBanxico(rs.getString(32)); // REPORTADA_BANXICO CHAR(1)

			return datos;   
		}
    }

	public String getNombreStored() { return nombreStored; }
	public void setNombreStored(String nombreStored) { this.nombreStored = nombreStored; }

	
	public String getParametroPathReporte() { return parametroPathReporte; }

	public void setParametroPathReporte(String parametroPathReporte) { this.parametroPathReporte = parametroPathReporte; }

	
	public String getParametroEmailsTo() { return parametroEmailsTo; }

	public void setParametroEmailsTo(String parametroEmailsTo) { this.parametroEmailsTo = parametroEmailsTo; }
}
