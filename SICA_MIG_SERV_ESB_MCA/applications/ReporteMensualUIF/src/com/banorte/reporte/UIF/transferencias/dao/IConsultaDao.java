package com.banorte.reporte.UIF.transferencias.dao;

import java.util.List;

import com.banorte.reporte.UIF.transferencias.dto.DatosTransferencia;
import com.banorte.reporte.UIF.transferencias.dto.DatosTransferenciaRecibida;

public interface IConsultaDao 
{
	
	String QUERY_REPORTE_MENSUAL_TRANSFERENCIAS = 	"select " +
		      "TO_CHAR(FECHA,'YYYYMMDD') || '|'," +
		      "referencia || '|' , " +
		      "deal || '|' , " +
		      "folio_detalle || '|', " +
		      "trim(nombre_corresponsal) || '|', " +
		      "trim(nombre_entidad_destino) || '|', " +
		      "trim(bic_aba) || '|', " +
		      "trim(datos_destinatario) || '|', " +
		      "trim(operacion_origen) || '|', " +
		      "trim(operacion_destino) || '|', " +
		      "trim(lpad(instrumento_monetario_origen,2,'0')) || '|', " +
		      "trim(moneda_origen) || '|', " +
		      "trim(monto_instrumento_origen) || '|', " +
		      "trim(mensaje) || '|', " +
		      "trim(tipo_persona) || '|', " +
		      "trim(nombre) || '|', " +
		      "trim(apellido_paterno) || '|', " +
		      "trim(apellido_materno) || '|', " +
		      "trim(to_char(fecha_nacimiento,'yyyymmdd')) || '|', " +
		      "trim(curp) || '|', " +
		      "trim(rfc) || '|', " +
		      "trim(pais_nacimiento) || '|', " +
		      "trim(pais_nacionalidad) || '|', " +
		      "trim(actividad_economica) || '|', " +
		      "trim(razon_social) || '|', " +
		      "trim(fecha_constitucion) || '|', " +
		      "trim(rfc_PM) || '|', " +
		      "trim(pais_nacionalidad_PM) || '|', " +
		      "trim(giro_mercantil) || '|', " +
		      "trim(apoderado_legal) || '|', " +
		      "trim(domicilio_unificado) || '|', " +
		      "trim(ciudad_poblacion) || '|', " +
		      "trim(codigo_postal) || '|', " +
		      "trim(entidad_federativa) || '|', " +
		      "trim(telefono) || '|', " +
		      "trim(numero_telefono) || '|', " +
		      "trim(clave_pais) || '|', " +
		      "trim(extension) || '|', " +
		      "trim(correo_electronico) || '|', " +
		      "trim(fiel) || '|', " +
		      "trim(monto_usd_mayor_igual_1000) || '|', " +
		      "trim(pais_banco_beneficiario) || '|' " +
		"from SC_OP_ENV_UIF " +
		"where REPORTADA_UIF = '0' ";
	
	String QUERY_REPORTE_MENSUAL_TRANSFERENCIAS_REC = 	"select " +
		      "TO_CHAR(FECHA,'YYYYMMDD') || '|'," +
		      "referencia || '|' , " +
		      "deal || '|' , " +
		      "folio_detalle || '|', " +
		      "trim(nombre_corresponsal) || '|', " +
		      "trim(nombre_entidad_origen) || '|', " +
		      "trim(bic_aba) || '|', " +
		      "trim(datos_ordenante) || '|', " +
		      "trim(operacion_origen) || '|', " +
		      "trim(nombre_entidad_destino) || '|', " +
		      "trim(datos_destinatario) || '|', " +
		      "trim(operacion_destino) || '|', " +
		      "trim(lpad(instrumento_monetario_destino,2,'0')) || '|', " +
		      "trim(moneda_destino) || '|', " +
		      "trim(monto_instrumento_destino) || '|', " +
		      "trim(mensaje) || '|', " +
		      "trim(tipo_persona) || '|', " +
		      "trim(nombre) || '|', " +
		      "trim(apellido_paterno) || '|', " +
		      "trim(apellido_materno) || '|', " +
		      "trim(to_char(fecha_nacimiento,'yyyymmdd')) || '|', " +
		      "trim(curp) || '|', " +
		      "trim(rfc) || '|', " +
		      "trim(pais_nacimiento) || '|', " +
		      "trim(pais_nacionalidad) || '|', " +
		      "trim(actividad_economica) || '|', " +
		      "trim(razon_social) || '|', " +
		      "trim(fecha_constitucion) || '|', " +
		      "trim(giro_mercantil) || '|', " +
		      "trim(apoderado_legal) || '|', " +
		      "trim(domicilio_unificado) || '|', " +
		      "trim(ciudad_poblacion) || '|', " +
		      "trim(codigo_postal) || '|', " +
		      "trim(telefono) || '|', " +
		      "trim(numero_telefono) || '|', " +
		      "trim(clave_pais) || '|', " +
		      "trim(extension) || '|', " +
		      "trim(correo_electronico) || '|', " +
		      "trim(fiel) || '|', " +
		      "trim(monto_usd_mayor_igual_1000) || '|', " +
		      "trim(pais_cliente_originador) || '|' " +
		"from SC_OP_REC_UIF " +
		"where REPORTADA_UIF = '0' ";
	
	String QUERY_CONSULTA_ORDENANTES_NO_REPORTADOS = 
			"select  " +
		      "TO_CHAR(FECHA,'YYYYMMDD') ," +
		      "referencia , " +
		      "deal , " +
		      "folio_detalle , " +
		      "trim(nombre_corresponsal) , " +
		      "trim(nombre_entidad_origen) , " +
		      "trim(bic_aba) , " +
		      "trim(datos_ordenante) , " +
		      "trim(operacion_origen) , " +
		      "trim(nombre_entidad_destino) , " +
		      "trim(datos_destinatario) , " +
		      "trim(operacion_destino) , " +
		      "trim(lpad(instrumento_monetario_destino,2,'0')) , " +
		      "trim(moneda_destino) , " +
		      "trim(monto_instrumento_destino) , " +
		      "trim(mensaje) , " +
		      "trim(tipo_persona) , " +
		      "trim(nombre) , " +
		      "trim(apellido_paterno) , " +
		      "trim(apellido_materno) , " +
		      "trim(fecha_nacimiento) , " +
		      "trim(curp) , " +
		      "trim(rfc) , " +
		      "trim(pais_nacimiento) , " +
		      "trim(pais_nacionalidad) , " +
		      "trim(actividad_economica) , " +
		      "trim(razon_social) , " +
		      "trim(fecha_constitucion) , " +
		      "trim(giro_mercantil) , " +
		      "trim(apoderado_legal) , " +
		      "trim(domicilio_unificado) , " +
		      "trim(ciudad_poblacion) , " +
		      "trim(codigo_postal) , " +
		      "trim(telefono) , " +
		      "trim(numero_telefono) , " +
		      "trim(clave_pais) , " +
		      "trim(extension) , " +
		      "trim(correo_electronico) , " +
		      "trim(fiel) , " +
		      "trim(monto_usd_mayor_igual_1000) , " +
		      "trim(pais_cliente_originador) " +
			"from sc_op_rec_uif " +
			"where datos_ordenante in ('MAS DE UN ORDENANTE REPORTADO','ORDENANTE NO REPORTADO') " +
			"and trunc(fecha) >= (select to_date('01/'||to_char(ADD_MONTHS(sysdate,-1),'mm/yyyy'),'dd/mm/yyyy') from dual)";


int PARAMETRO_PATH_REPORTE = 1;
int PARAMETRO_EMAILS_TO = 2;


String UPDATE_BANDERA_ENV_UIF = "UPDATE SICA_ADMIN.SC_OP_ENV_UIF SET REPORTADA_UIF = 1 WHERE REPORTADA_UIF = 0";
String BORRAR_REGISTROS_NO_ENV_UIF = "DELETE FROM SICA_ADMIN.SC_OP_ENV_UIF WHERE REPORTADA_UIF = 0";
String UPDATE_BANDERA_ENV_UIF_REC = "UPDATE SICA_ADMIN.SC_OP_REC_UIF SET REPORTADA_UIF = 1 WHERE REPORTADA_UIF = 0";
String BORRAR_REGISTROS_NO_ENV_UIF_REC = "DELETE FROM SICA_ADMIN.SC_OP_REC_UIF WHERE REPORTADA_UIF = 0";

List<DatosTransferencia> consultarTransferenciasReporte();
List<DatosTransferenciaRecibida> consultarTransferenciasReporteRecibidas();
List<DatosTransferenciaRecibida> consultarDatosOrdenantesNoReportados();

public void actualizaBanderaEnvUIF();
public void borrarOperacionesNoReportadas();
public void actualizaBanderaEnvUIFRec();
public void borrarOperacionesNoReportadasRec();
String consultarParametro(int opcion);
boolean ejecutarStoredProcedure();

}
