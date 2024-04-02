package com.banorte.contrapartes.dao.impl;

//import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.jdbc.object.MappingSqlQuery;

import com.banorte.contrapartes.dao.BaseDao;
import com.banorte.contrapartes.dao.IConsultaDao;
import com.banorte.contrapartes.dto.OperacionDto;

public class ConsultaDaoImpl extends BaseDao implements IConsultaDao 
{
	private static final Logger LOG = Logger.getLogger(ConsultaDaoImpl.class);
	
	public int consultarEstadoSistema()
	{
		return getJdbcTemplate().queryForInt("select id_estado from sc_estado where actual = 'S'");
	}
	
	public List<OperacionDto> consultaDeals()
	{
		List<OperacionDto> operaciones = null;
		LOG.warn("Inicia consulta de operaciones con monto de 1 MDD....");
		ConsultaDatosOperaciones query = new ConsultaDatosOperaciones(getDataSource(), QUERY_DETALLES_MONTO_1_MILLON_USD);
		operaciones = query.execute();
		LOG.warn("Termina consulta de operaciones con monto de 1 MDD.");
		if(operaciones != null)
			LOG.warn("Total de registros encontrados: " + operaciones.size());
		
		return operaciones;
	}
	
	class ConsultaDatosOperaciones extends MappingSqlQuery<OperacionDto> 
	{	
		public ConsultaDatosOperaciones(DataSource ds, String sql) {
			super(ds, sql);
		}

		@Override
		protected OperacionDto mapRow(ResultSet rs, int arg1) throws SQLException 
		{
			// id_deal_original
			Integer idDealOriginal = rs.getString(18) != null && 
					                 "".equals(rs.getString(18).trim()) ? new Integer(rs.getString(18)) : null;
            
			Date fechaCreacion = rs.getDate(14) != null ? new Date(rs.getDate(14).getTime()) : null;
			
			return new OperacionDto(   rs.getString(1),       // cliente
									   rs.getString(2),       // contrato sica
									   rs.getString(3),       // tipo_persona
									   rs.getString(4),       // rfc
									   rs.getInt(5),          // numero de deal
									   rs.getInt(6),          // folio detalle
									   rs.getString(7),       // entregamos o recibimos
									   rs.getString(8),       // id_divisa
									   rs.getBigDecimal(9),   // monto en divisa original
									   rs.getBigDecimal(10),  // monto en usd detalle
									   rs.getString(11),      // STATUS_DETALLE_DEAL
									   rs.getString(12),      // promotor
									   rs.getString(13),      // id_canal
									   fechaCreacion,         // fecha creacion
									   rs.getString(15),      // fecha valor
									   rs.getString(16),      // status_deal
									   rs.getString(17),      // deal correccion
									   idDealOriginal         // id_deal_original	
					);
		}
    }
	
	private String QUERY_DETALLES_MONTO_1_MILLON_USD = 
	   "select EE.nombre_corto as \"Cliente\", " +
		       "BB.no_cuenta as \"Contrato SICA\", " +
		       "EE.Tipo_Persona, " +
		       "EE.RFC, " +
		       "BB.\"Numero de Deal\", " +
		       "FOLIO_DETALLE, " +
		       "\"Entregamos o Recibimos\", " +
		       "id_divisa, " +
		       "\"Monto en Divisa Original\", " +
		       "\"Monto en USD Detalle\", " +
		       "status_detalle_deal, " +
		       "FF.nombre_corto as \"Promotor\", " +
		       "ID_CANAL, " +
		       "\"Fecha Creacion\", " +
		       "\"Fecha Valor\", " +
		       "status_deal, " +
		       "DECODE(CC.id_deal_correccion,null,'NO','SI') AS \"DEAL_CORRECCION\", " +
		       "CC.id_deal_original " +
		" FROM ( " +

		        //SOLO DOLARES
		        "select a.id_deal as \"Numero de Deal\",a.no_cuenta,ID_CANAL,FOLIO_DETALLE,id_promotor, " +
		        "monto as \"Monto en USD Detalle\",fecha_captura \"Fecha Creacion\",tipo_valor as \"Fecha Valor\", " +
		        "status_deal,id_divisa,monto as \"Monto en Divisa Original\", " +
		        "DECODE(RECIBIMOS,'S','RECIBIMOS','ENTREGAMOS') AS \"Entregamos o Recibimos\",status_detalle_deal " +
		        "from sc_deal a, sc_deal_detalle b, sc_deal_posicion c " +
		        "where a.id_deal = b.id_deal and b.id_deal_posicion = c.id_deal_posicion " +
		        "and trunc(fecha_captura) = trunc(sysdate) and status_deal != 'CA' AND STATUS_DETALLE_DEAL != 'CA' " + // and STATUS_DETALLE_DEAL != 'AL' 
		        "and id_divisa = 'USD' " +
		        "and simple != 'I' " +
		        "and monto >= 1000000 " +
		        
		        " UNION " +
		        //DIVISAS FRECUENTES DISTINTAS A DOLARES
		        "select a.id_deal as \"Numero de Deal\",a.no_cuenta,ID_CANAL,FOLIO_DETALLE, " +
		        "id_promotor,ROUND((monto * b.factor_divisa),4) as \"Monto en USD Detalle\", " +
		        "fecha_captura \"Fecha Creacion\",tipo_valor as \"Fecha Valor\",status_deal, " +
		        "id_divisa,monto as \"Monto en Divisa Original\", " +
		        "DECODE(RECIBIMOS,'S','RECIBIMOS','ENTREGAMOS') AS \"Entregamos o Recibimos\", " +
		        "status_detalle_deal " +
		        "from sc_deal a, sc_deal_detalle b, sc_deal_posicion c " +
		        "where a.id_deal = b.id_deal and b.id_deal_posicion = c.id_deal_posicion " +
		        "and trunc(fecha_captura) = trunc(sysdate) and status_deal != 'CA' AND STATUS_DETALLE_DEAL != 'CA' " + // and STATUS_DETALLE_DEAL != 'AL' 
		        "and id_divisa IN(SELECT ID_DIVISA FROM SC_DIVISA WHERE TIPO = 'F' AND ID_DIVISA != 'USD') " +
		        "and simple != 'I' " +
		        "and ROUND((monto * b.factor_divisa),4) >= 1000000 " +

		        " UNION " +
		        //DIVISAS NO FRECUENTES
		        "select a.id_deal as \"Numero de Deal\",a.no_cuenta,ID_CANAL,FOLIO_DETALLE, " +
		        "id_promotor,ROUND((MONTO * tipo_cambio) / (select mid_spot from sc_precio_referencia_actual),4) as \"Monto en USD Detalle\", " +
		        "fecha_captura \"Fecha Creacion\",tipo_valor as \"Fecha Valor\",status_deal,id_divisa, " +
		        "monto as \"Monto en Divisa Original\",DECODE(RECIBIMOS,'S','RECIBIMOS','ENTREGAMOS') AS \"Entregamos o Recibimos\", " +
		        "status_detalle_deal " +
		        "from sc_deal a, sc_deal_detalle b, sc_deal_posicion c " +
		        "where a.id_deal = b.id_deal and b.id_deal_posicion = c.id_deal_posicion " +
		        "and trunc(fecha_captura) = trunc(sysdate) and status_deal != 'CA' AND STATUS_DETALLE_DEAL != 'CA' " + // and STATUS_DETALLE_DEAL != 'AL' 
		        "and id_divisa IN(SELECT ID_DIVISA FROM SC_DIVISA WHERE TIPO in ('N') AND ID_DIVISA != 'MXN') " +
		        "and simple != 'I' " +
		        "and ROUND((MONTO * tipo_cambio) / (select mid_spot from sc_precio_referencia_actual),4) >= 1000000 " +
		")BB, " +
		"sc_reverso CC,bup_persona_cuenta_rol DD,bup_persona EE,bup_persona FF " +
		"WHERE BB.\"Numero de Deal\" = CC.id_deal_correccion(+) and BB.no_cuenta = DD.no_cuenta and " +
		      "DD.id_persona = EE.id_persona and BB.id_promotor = FF.id_persona " +
		"and id_rol = 'TIT' "; //+
		/*"AND not exists ( " +  //Para no contemplar los deals que son de neteo
		                    "select * " +
		                    "from sc_reverso rev " + 
		                    "where rev.id_deal_balanceo = BB.\"Numero de Deal\" " +
		                ")";*/
	/*
	private String QUERY_DEALS_MONTO_1MDD =
			"select EE.nombre_corto as \"Cliente\", " +
		           "BB.no_cuenta as \"Contrato SICA\", " +
			       "EE.Tipo_Persona, " +
			       "EE.RFC, " +
			       "AA.\"Número de Deal\", " +
			       " \"Monto en USD Deal\", " +
			       "status_deal, " +
			       "DECODE(CC.id_deal_correccion,null,'NO','SI') AS \"DEAL_CORRECCION ?\", " +
			       "CC.id_deal_original, " +
			       " \"Fecha Creación\", " +
			       " \"Fecha Valor\", " +
			       "FF.nombre_corto as \"Promotor\", " +
			       "ID_CANAL, " +
			       "FOLIO_DETALLE, " +
			       " \"Entregamos o Recibimos\", " +
			       "id_divisa, " + 
			       " \"Monto en Divisa Original\", " +
			       " \"Monto en USD Detalle\" " +
		"FROM " +
		"( " +
		        "SELECT \"Número de Deal\",ROUND((SUM(\"Monto en USD\") / 2),4) AS \"Monto en USD Deal\" " +
		        "FROM " +  
		        "( " +    
		                //SOLO DOLARES
		                "select a.id_deal as \"Número de Deal\", " +
						       "a.no_cuenta,ID_CANAL,FOLIO_DETALLE, " + 
							   "monto as \"Monto en USD\", " +
							   "fecha_captura \"Fecha Creación\", " +
							   "tipo_valor as \"Fecha Valor\", " +
							   "status_deal, " +
							   "DECODE(SIMPLE,'I','SI','NO') AS \"INTERBANCARIO ?\" " +
		                "from sc_deal a, sc_deal_detalle b, sc_deal_posicion c " +
		                "where a.id_deal = b.id_deal and b.id_deal_posicion = c.id_deal_posicion " +
		                "and trunc(fecha_captura) = trunc(sysdate) and status_deal != 'CA' AND STATUS_DETALLE_DEAL != 'CA' " +
		                "and id_divisa = 'USD' " +
		                "and simple != 'I' " +
		        
		                " UNION " + 
		                //DIVISAS FRECUENTES DISTINTAS A DOLARES
		                "select a.id_deal as \"Número de Deal\", " +
						       "a.no_cuenta, " +
							   "ID_CANAL,FOLIO_DETALLE, " +
							   "ROUND((monto * b.factor_divisa),4) as \"Monto en USD\", " +
							   "fecha_captura \"Fecha Creación\", " +
							   "tipo_valor as \"Fecha Valor\", " +
							   "status_deal, " +
							   "DECODE(SIMPLE,'I','SI','NO') AS \"INTERBANCARIO ?\" " +
		                "from sc_deal a, sc_deal_detalle b, sc_deal_posicion c " +
		                "where a.id_deal = b.id_deal and b.id_deal_posicion = c.id_deal_posicion " +
		                "and trunc(fecha_captura) = trunc(sysdate - 4) and status_deal != 'CA' AND STATUS_DETALLE_DEAL != 'CA' " +
		                "and id_divisa IN(SELECT ID_DIVISA FROM SC_DIVISA WHERE TIPO = 'F' AND ID_DIVISA != 'USD') " +
		                "and simple != 'I' " +

		                " UNION " +
		                //DIVISAS NO FRECUENTES Y METALES AMONEDADOS
		                "select a.id_deal as \"Número de Deal\", " +
						       "a.no_cuenta,ID_CANAL,FOLIO_DETALLE, " +
							   "ROUND((MONTO * tipo_cambio) / (select mid_spot from sc_precio_referencia_actual),4) as \"Monto en USD\", " +
							   "fecha_captura \"Fecha Creación\", " +
							   "tipo_valor as \"Fecha Valor\", " +
							   "status_deal, " +
							   "DECODE(SIMPLE,'I','SI','NO') AS \"INTERBANCARIO ?\" " +
		                "from sc_deal a, sc_deal_detalle b, sc_deal_posicion c " +
		                "where a.id_deal = b.id_deal and b.id_deal_posicion = c.id_deal_posicion " + 
		                "and trunc(fecha_captura) = trunc(sysdate - 4) and status_deal != 'CA' AND STATUS_DETALLE_DEAL != 'CA' " +
		                "and id_divisa IN(SELECT ID_DIVISA FROM SC_DIVISA WHERE TIPO in ('M','N') AND ID_DIVISA != 'MXN') " +
		                "and simple != 'I' " + 

		                " UNION " +
		                //PESOS DOLARIZADOS
		                "select a.id_deal as \"Número de Deal\", " +
						       "a.no_cuenta, " +
							   "ID_CANAL,FOLIO_DETALLE, " +
							   "ROUND((MONTO / (select mid_spot from sc_precio_referencia_actual)),4) as \"Monto en USD\", " +
							   "fecha_captura \"Fecha Creación\", " +
							   "tipo_valor as \"Fecha Valor\", " +
							   "status_deal, " +
							   "DECODE(SIMPLE,'I','SI','NO') AS \"INTERBANCARIO ?\" " +
		                "from sc_deal a, sc_deal_detalle b, sc_deal_posicion c " + 
		                "where a.id_deal = b.id_deal and b.id_deal_posicion = c.id_deal_posicion " +  
		                "and trunc(fecha_captura) = trunc(sysdate - 4) and status_deal != 'CA' AND STATUS_DETALLE_DEAL != 'CA' " + 
		                "and id_divisa = 'MXN' " +
		                "and simple != 'I' " +
		        ") A " +
		"GROUP BY \"Número de Deal\" " +
		"HAVING SUM(\"Monto en USD\") / 2 >= 1000000 " + //EN ESTE PUNTO YA ESTA TODO DOLARIZADO 
		"ORDER BY \"Número de Deal\" " +
		") AA, " +
		"( " +
		        //SOLO DOLARES
		        "select a.id_deal as \"Número de Deal\", " +
				       "a.no_cuenta, " +
					   "ID_CANAL,FOLIO_DETALLE, " +
					   "id_promotor, " +
					   "monto as \"Monto en USD Detalle\", " +
					   "fecha_captura \"Fecha Creación\", " +
					   "tipo_valor as \"Fecha Valor\", " +
					   "status_deal, " +
					   "id_divisa, " +
					   "monto as \"Monto en Divisa Original\", " +
					   "DECODE(RECIBIMOS,'S','RECIBIMOS','ENTREGAMOS') AS \"Entregamos o Recibimos\" " +
		        "from sc_deal a, sc_deal_detalle b, sc_deal_posicion c " +
		        "where a.id_deal = b.id_deal and b.id_deal_posicion = c.id_deal_posicion " +
		        "and trunc(fecha_captura) = trunc(sysdate - 4) and status_deal != 'CA' AND STATUS_DETALLE_DEAL != 'CA' " +
		        "and id_divisa = 'USD' " +
		        "and simple != 'I' " +
		        
		        " UNION " +
		        //DIVISAS FRECUENTES DISTINTAS A DOLARES
		        "select a.id_deal as \"Número de Deal\", " +
				       "a.no_cuenta, " +
					   "ID_CANAL, " +
					   "FOLIO_DETALLE, " +
					   "id_promotor, " +
					   "ROUND((monto * b.factor_divisa),4) as \"Monto en USD Detalle\", " +
					   "fecha_captura \"Fecha Creación\", " +
					   "tipo_valor as \"Fecha Valor\", " +
					   "status_deal, " +
					   "id_divisa, " +
					   "monto as \"Monto en Divisa Original\", " +
					   "DECODE(RECIBIMOS,'S','RECIBIMOS','ENTREGAMOS') AS \"Entregamos o Recibimos\" " +
		        "from sc_deal a, sc_deal_detalle b, sc_deal_posicion c " +
		        "where a.id_deal = b.id_deal and b.id_deal_posicion = c.id_deal_posicion " +
		        "and trunc(fecha_captura) = trunc(sysdate - 4) and status_deal != 'CA' AND STATUS_DETALLE_DEAL != 'CA' " +
		        "and id_divisa IN(SELECT ID_DIVISA FROM SC_DIVISA WHERE TIPO = 'F' AND ID_DIVISA != 'USD') " +
		        "and simple != 'I' " +

		        " UNION " +
		        //DIVISAS NO FRECUENTES Y METALES AMONEDADOS
		        "select a.id_deal as \"Número de Deal\", " +
				       "a.no_cuenta,ID_CANAL,FOLIO_DETALLE, " +
					   "id_promotor, " +
					   "ROUND((MONTO * tipo_cambio) / (select mid_spot from sc_precio_referencia_actual),4) as \"Monto en USD Detalle\", " +
					   "fecha_captura \"Fecha Creación\", " +
					   "tipo_valor as \"Fecha Valor\", " +
					   "status_deal, " +
					   "id_divisa, " +
					   "monto as \"Monto en Divisa Original\", " +
					   "DECODE(RECIBIMOS,'S','RECIBIMOS','ENTREGAMOS') AS \"Entregamos o Recibimos\" " +
		        "from sc_deal a, sc_deal_detalle b, sc_deal_posicion c " +
		        "where a.id_deal = b.id_deal and b.id_deal_posicion = c.id_deal_posicion " +
		        "and trunc(fecha_captura) = trunc(sysdate - 4) and status_deal != 'CA' AND STATUS_DETALLE_DEAL != 'CA' " +
		        "and id_divisa IN(SELECT ID_DIVISA FROM SC_DIVISA WHERE TIPO in ('M','N') AND ID_DIVISA != 'MXN') " +
		        "and simple != 'I' " +  

		        " UNION " +
		        //PESOS DOLARIZADOS
		        "select a.id_deal as \"Número de Deal\", " +
				       "a.no_cuenta, " +
					   "ID_CANAL,FOLIO_DETALLE, " +
					   "id_promotor, " +
					   "ROUND((MONTO / (select mid_spot from sc_precio_referencia_actual)),4) as \"Monto en USD Detalle\", " +
					   "fecha_captura \"Fecha Creación\", " +
					   "tipo_valor as \"Fecha Valor\", " +
					   "status_deal, " +
					   "id_divisa, " +
					   "monto as \"Monto en Divisa Original\", " +
					   "DECODE(RECIBIMOS,'S','RECIBIMOS','ENTREGAMOS') AS \"Entregamos o Recibimos\" " +
		        "from sc_deal a, sc_deal_detalle b, sc_deal_posicion c " +
		        "where a.id_deal = b.id_deal and b.id_deal_posicion = c.id_deal_posicion " +
		        "and trunc(fecha_captura) = trunc(sysdate - 4) and status_deal != 'CA' AND STATUS_DETALLE_DEAL != 'CA' " +
		        "and id_divisa = 'MXN' " +
		        "and simple != 'I' " +
		")BB, " +
		"sc_reverso CC,bup_persona_cuenta_rol DD,bup_persona EE,bup_persona FF " +
		"WHERE AA.\"Número de Deal\" = BB.\"Número de Deal\" and " + 
		      "AA.\"Número de Deal\" = CC.id_deal_correccion(+) and " + 
			  "BB.no_cuenta = DD.no_cuenta and " + 
			  "DD.id_persona = EE.id_persona and " + 
			  "BB.id_promotor = FF.id_persona " +
		"and id_rol = 'TIT' " +
		"AND not exists ( " + //Para no contemplar los deals que son de neteo
		                    "select * " +
		                    "from sc_reverso rev " +
		                    "where rev.id_deal_balanceo = AA.\"Número de Deal\" " +
		                ")";*/
}
