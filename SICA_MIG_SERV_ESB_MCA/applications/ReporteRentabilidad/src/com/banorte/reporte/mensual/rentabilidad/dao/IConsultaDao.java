package com.banorte.reporte.mensual.rentabilidad.dao;

import java.util.List;

import com.banorte.reporte.mensual.rentabilidad.dto.DatosOperacion;

public interface IConsultaDao 
{
	String QUERY_REPORTE_MENSUAL_RENTABILIDAD = 	
			
			"SELECT trim(deal.ID_DEAL) || ';' as DEAL," +
			       "trim(deal.id_canal) || ';' as ID_CANAL," +
					"trim(deal_det.FOLIO_DETALLE) || ';' AS FOLIO," +
			        "trim(deal.TIPO_VALOR) || ';' as FECHA_VALOR, " +
					"trim(to_char (deal.FECHA_CAPTURA, 'mm/dd/yyyy hh24:mm')) || ';' as FECHA_CAPTURA, " +
			        "trim(to_char (deal.FECHA_LIQUIDACION,'mm/dd/yyyy hh24:mm')) || ';' AS FECHA_LIQ, " +
					"trim(deal_det.mnemonico)|| ';' AS MNEMONICO," +
				    "trim(DECODE(deal_pos.recibimos, 'S', 'Compra', 'Venta'))|| ';' AS OP, " +
					"trim(replace(replace(cte.NOMBRE_CORTO,',',' '),';','')) || ';' AS CLIENTE, " +
					"trim(deal_det.clave_forma_liquidacion) || ';' AS PRODUCTO, " +
				    "trim(deal_pos.MONTO) || ';' AS MONTO, " +
					"trim(deal_pos.ID_DIVISA) || ';' AS ID_DIVISA, " +
				    "trim(deal_pos.tipo_cambio) || ';' AS TIPO_CAMBIO_CLIENTE, " +
					"trim(deal_det.tipo_cambio_mesa) || ';' AS TIPO_CAMBIO_MESA, " +
				    "trim(deal_pos.MONTO * deal_pos.tipo_cambio) || ';' AS MONTO_EQUIV, " +
				    "trim(DECODE(deal_pos.recibimos, 'S', (deal_det.tipo_cambio_mesa-deal_pos.tipo_cambio)*deal_pos.monto, (deal_pos.tipo_cambio-deal_det.tipo_cambio_mesa)*deal_pos.monto)) || ';' AS UTILIDAD," +
				    "trim(prom.NOMBRE)||' '||trim(prom.PATERNO)||' '||trim(prom.MATERNO) || ';' AS PROMOTOR, " +
				    "trim(deal.id_promotor) || ';' AS ID_PROMOTOR, " +
				    "trim(usu_captura.nombre_corto) || ';' AS NOMBRE_USU_CAPTURA," +
				    "trim(su.id_persona) || ';' AS ID_PROMOTOR_CAPTURA," +
				    "trim(su.clave_usuario) || ';' AS CLAVE_USUARIO_CAPTURA, " +
				    "trim(cta_cont.no_cuenta) || ';' AS CONTRATO_SICA, " +
				    "trim(deal_det.status_detalle_deal) || ';' AS STATUS," +
				    "trim(mesa.nombre) || ';' AS MESA, " +
				    "trim(sec_eco.DESCRIPCION) AS SECTOR_ECONOMICO, " +
				    "trim(sec_eco.DESC_SECTOR_BANXICO) AS SECTOR_BANXICO " +
					"FROM SC_DEAL deal, SC_DEAL_DETALLE deal_det, SC_DEAL_POSICION deal_pos, BUP_CUENTA_CONTRATO cta_cont, " +
					    "BUP_PERSONA_RIM pers_rim, BUP_PERSONA cte, BUP_SECTOR_ECONOMICO sec_eco, BUP_PERSONA_FISICA prom, SC_MESA_CAMBIO mesa, sc_divisa div," +
					    "segu_usuario su, bup_persona usu_captura " +
					"WHERE deal.ID_DEAL = deal_det.ID_DEAL " +
					"AND deal_det.ID_DEAL_POSICION = deal_pos.ID_DEAL_POSICION " +
					"AND deal.NO_CUENTA = cta_cont.NO_CUENTA (+) " +
					"AND cta_cont.RIM_NO = pers_rim.RIM_NO " +
					"AND pers_rim.ID_PERSONA = cte.ID_PERSONA " +
					"AND cte.ID_SECTOR = sec_eco.ID_SECTOR (+) " +
					"and deal.id_usuario = su.id_usuario " +
					"and su.id_persona = usu_captura.id_persona " +
					//"AND trunc(deal.FECHA_CAPTURA) between to_date('27/03/2017','dd/mm/yy hh:mi') and to_date('31/03/2017','dd/mm/yy hh:mi') " +
					"AND trunc(deal.FECHA_CAPTURA) between to_date((select to_date('01/'||to_char(ADD_MONTHS(sysdate,-1),'mm/yyyy'),'dd/mm/yyyy') from dual),'dd/mm/yy hh:mi') and to_date((select to_date('01/'||to_char(sysdate,'mm/yyyy'),'dd/mm/yyyy') -1 from dual),'dd/mm/yy hh:mi')" +
					"AND deal_det.STATUS_DETALLE_DEAL != 'CA' " +
					"AND deal_pos.id_divisa = div.id_divisa " +
					"AND deal.ID_PROMOTOR = prom.ID_PERSONA (+) " +
					"AND mesa.ID_MESA_CAMBIO = deal_pos.id_mesa_cambio " +
					"AND deal_pos.ID_DIVISA not in ('MXN') " +
					"order by deal_pos.RECIBIMOS, deal.ID_DEAL";

int PARAMETRO_PATH_REPORTE = 1;
int PARAMETRO_EMAILS_TO = 2;


List<DatosOperacion> consultarOperacionesReporte();

String consultarParametro(int opcion);


}
