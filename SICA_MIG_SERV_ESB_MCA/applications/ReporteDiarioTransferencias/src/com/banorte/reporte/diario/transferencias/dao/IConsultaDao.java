package com.banorte.reporte.diario.transferencias.dao;

import java.util.List;

import com.banorte.reporte.diario.transferencias.dto.DatosTransferencia;

//import com.banorte.contrapartes.dto.OperacionDto;

public interface IConsultaDao 
{
	String QUERY_REPORTE_DIARIO_TRANSFERENCIAS = 		"SELECT TO_CHAR(FECHA_OPERACION,'YYYY/MM/DD') || ';' AS FECHA_OPERACION," +
														"TIPO_OPERACION || ';' AS TIPO_OPERACION," +
														"TIPO_TRANSFERENCIA || ';' AS TIPO_TRANSFERENCIA," +
														"ID_OPERACION || ';' AS ID_OPERACION," +
														"MEDIO || ';' AS MEDIO," +
														"OTRO_MEDIO || ';' AS OTRO_MEDIO, " +
														"FOLIO|| ';' AS FOLIO," +
														"ID_INST_REPORTA || ';' AS ID_INST_REPORTA," +
														"ID_BCO_CLI_ORIGINADOR || ';' AS ID_BCO_CLI_ORIGINADOR," +
														"ID_CLI_ORIGINADOR || ';' AS ID_CLI_ORIGINADOR," +
														"TIPO_CLI_ORIGINADOR || ';' AS TIPO_CLI_ORIGINADOR," + 
														"REPLACE(RAZON_SOCIAL_CLI,';',' ') || ';' AS RAZON_SOCIAL_CLI," + 
														"REPLACE(AP_PATERNO_CLI,';',' ') || ';' AS AP_PATERNO_CLI," +
														"REPLACE(AP_MATERNO_CLI,';',' ') || ';' AS AP_MATERNO_CLI," +
														"REPLACE(AP_NOMBRE_CLI,';',' ') || ';' AS AP_NOMBRE_CLI, " +
														"TO_CHAR(FECHA_NAC,'YYYY/MM/DD') || ';' AS FECHA_NAC," +
														"NACION_CLI_ORIGINADOR || ';' AS NACION_CLI_ORIGINADOR, " +
														"TIPO_ID_ORIGINADOR_EXT || ';' AS TIPO_ID_ORIGINADOR_EXT, " +
														"ID_ORIGINADOR_EXT || ';' AS ID_ORIGINADOR_EXT, " +
														"SEXO_CLI_ORIGINADOR || ';' AS SEXO_CLI_ORIGINADOR, " +
														"EDO_NAC_CLI_ORIGINADOR || ';' AS EDO_NAC_CLI_ORIGINADOR, " +														
														"TIPO_CUENTA_ORD || ';' AS TIPO_CUENTA_ORD," +
														"REPLACE(CUENTA_ORD,';',' ') || ';' AS CUENTA_ORD," +
														"TIPO_ID_INST_RECEPT || ';' AS TIPO_ID_INST_RECEPT," +
														"ID_INST_RECEPT || ';' AS ID_INST_RECEPT," +
														"TIPO_ID_INST_BENEF || ';' AS TIPO_ID_INST_BENEF," +
														"ID_INST_BENEF || ';' AS ID_INST_BENEF," +
														"TIPO_BENEF || ';' AS TIPO_BENEF," +
														"REPLACE(NOMBRE_RAZON_SOC_BENEF,';',' ') || ';' AS NOMBRE_RAZON_SOC_BENEF," +
														"REPLACE(CUENTA_BENEF,';',' ') || ';' AS CUENTA_BENEF," +
														"PAIS_BCO_BENEF || ';' AS PAIS_BCO_BENEF, " +
														"to_char(MONTO_OP, '9999999999999.99') || ';' AS MONTO_OP," +
														"ID_DIVISA || ';' AS ID_DIVISA," +
														"REPLACE(OBSERVACIONES,';',' ') || ';' AS OBSERVACIONES," +
														"FOLIO_TRANS_NACIONAL || ';' AS FOLIO_TRANS_NACIONAL," +
														"TIPO_FONDEO_TRANS || ';' AS TIPO_FONDEO_TRANS, " +
														"decode(substr(fondeo_trans,length(fondeo_trans)-2,2),' /',substr(fondeo_trans,1,length(fondeo_trans)-2),fondeo_trans)||'	' AS FONDEO_TRANS " +
														"FROM SICA_ADMIN.SC_TRAEXT_ENV " +
														"WHERE REPORTADA_BANXICO = '0'";

	
//	String QUERY_REPORTE_DIARIO_TRANSFERENCIAS =		"SELECT NVL(TO_CHAR(FECHA_OPERACION,'YYYY/MM/DD'),RPAD(' ',10,' ')) || ';' AS FECHA_OPERACION," +
//														"NVL (RPAD(TIPO_OPERACION,2,' '),RPAD(' ',2,' ')) || ';' AS TIPO_OPERACION," +
//														"NVL (RPAD(TIPO_TRANSFERENCIA,1,' '),RPAD(' ',1,' ')) || ';' AS TIPO_TRANSFERENCIA," +
//														"NVL (RPAD(ID_OPERACION,20,' '),RPAD(' ',20,' ')) || ';' AS ID_OPERACION," +
//														"NVL (RPAD(MEDIO,1,' '),RPAD(' ',1,' ')) || ';' AS MEDIO," +
//														"NVL (RPAD(FOLIO,20,' '),RPAD(' ',20,' ')) || ';' AS FOLIO," +
//														"NVL (RPAD(ID_INST_REPORTA,6,' '),RPAD(' ',6,' ')) || ';' AS ID_INST_REPORTA," +
//														"NVL (RPAD(ID_BCO_CLI_ORIGINADOR,6,' '),RPAD(' ',6,' ')) || ';' AS ID_BCO_CLI_ORIGINADOR," +
//														"NVL (RPAD(ID_CLI_ORIGINADOR,20,' '),RPAD(' ',20,' ')) || ';' AS ID_CLI_ORIGINADOR," +
//														"NVL (RPAD(TIPO_CLI_ORIGINADOR,1,' '),RPAD(' ',1,' ')) || ';' AS TIPO_CLI_ORIGINADOR, " +
//														"NVL (RPAD(REPLACE(RAZON_SOCIAL_CLI,';',' '),150,' '),RPAD(' ',150,' ')) || ';' AS RAZON_SOCIAL_CLI, " +
//														"NVL (RPAD(REPLACE(AP_PATERNO_CLI,';',' '),50,' '),RPAD(' ',50,' ')) || ';' AS AP_PATERNO_CLI," +
//														"NVL (RPAD(REPLACE(AP_MATERNO_CLI,';',' '),50,' '),RPAD(' ',50,' ')) || ';' AS AP_MATERNO_CLI," +
//														"NVL (RPAD(REPLACE(AP_NOMBRE_CLI,';',' '),50,' '),RPAD(' ',50,' ')) || ';' AS AP_NOMBRE_CLI, " +
//														"NVL (TO_CHAR(FECHA_NAC,'YYYY/MM/DD'),RPAD(' ',10,' ')) || ';' AS FECHA_NAC," +
//														"NVL (RPAD(TIPO_CUENTA_ORD,1,' '),RPAD(' ',1,' ')) || ';' AS TIPO_CUENTA_ORD," +
//														"NVL (RPAD(REPLACE(CUENTA_ORD,';',' '),18,' '),RPAD(' ',18,' ')) || ';' AS CUENTA_ORD," +
//														"NVL (RPAD(TIPO_ID_INST_RECEPT,1,' '),RPAD(' ',1,' ')) || ';' AS TIPO_ID_INST_RECEPT," +
//														"NVL (RPAD(ID_INST_RECEPT,20,' '),RPAD(' ',20,' ')) || ';' AS ID_INST_RECEPT," +
//														"NVL (RPAD(TIPO_ID_INST_BENEF,1,' '),RPAD(' ',1,' ')) || ';' AS TIPO_ID_INST_BENEF," +
//														"NVL (RPAD(ID_INST_BENEF,11,' '),RPAD(' ',11,' ')) || ';' AS ID_INST_BENEF," +
//														"NVL (RPAD(TIPO_BENEF,1,' '),RPAD(' ',1,' ')) || ';' AS TIPO_BENEF," +
//														"NVL (RPAD(REPLACE(NOMBRE_RAZON_SOC_BENEF,';',' '),150,' '),RPAD(' ',150,' ')) || ';' AS NOMBRE_RAZON_SOC_BENEF," +
//														"NVL (RPAD(REPLACE(CUENTA_BENEF,';',' '),35,' '),RPAD(' ',35,' ')) || ';' AS CUENTA_BENEF," +
//														"to_char(MONTO_OP, '9999999999999.99') || ';' AS MONTO_OP," +
//														"NVL (RPAD(ID_DIVISA,3,' '),RPAD(' ',3,' ')) || ';' AS ID_DIVISA," +
//														"NVL (RPAD(REPLACE(OBSERVACIONES,';',' '),100,' '),RPAD(' ',100,' ')) || ';' AS OBSERVACIONES," +
//														"NVL (RPAD(FOLIO_TRANS_NACIONAL,20,' '),RPAD(' ',20,' ')) || ';' AS FOLIO_TRANS_NACIONAL," +
//														"NVL (RPAD(TIPO_FONDEO_TRANS,1,' '),RPAD(' ',1,' ')) || ';' AS TIPO_FONDEO_TRANS, " +
//														"NVL (RPAD(FONDEO_TRANS,100,' '),RPAD(' ',100,' ')) AS FONDEO_TRANS " +
//													"FROM SICA_ADMIN.SC_TRAEXT_ENV " +
//													"WHERE REPORTADA_BANXICO = '0'";
	int PARAMETRO_PATH_REPORTE = 1;
	int PARAMETRO_EMAILS_TO = 2;
	
	
	String UPDATE_BANDERA_ENV_BANXICO = "UPDATE SICA_ADMIN.SC_TRAEXT_ENV SET REPORTADA_BANXICO = 1 WHERE REPORTADA_BANXICO = 0";
	
	List<DatosTransferencia> consultarTransferenciasReporte();
	public void actualizaBanderaEnvBanxico();
	String consultarParametro(int opcion);
	boolean ejecutarStoredProcedure();
}
