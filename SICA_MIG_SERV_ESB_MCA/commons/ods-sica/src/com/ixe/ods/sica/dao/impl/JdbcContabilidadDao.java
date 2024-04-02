/*
 * $Id: JdbcContabilidadDao.java,v 1.8.24.1.36.2.16.1 2021/03/18 17:31:36 mejiar Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2009 - 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica.dao.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.ixe.ods.sica.Num;
import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.dao.ContabilidadDao;
import com.ixe.ods.sica.model.Poliza;
import com.ixe.ods.sica.model.LogExpresionContable;

/**
 * Implementaci&oacute;n JDBC del DataAccessObject de Contabilidad.
 *
 * @author Israel J. Rebollar
 * @version $Revision: 1.8.24.1.36.2.16.1 $ $Date: 2021/03/18 17:31:36 $
 */
public class JdbcContabilidadDao extends JdbcDaoSupport implements ContabilidadDao {

	/**
	 * Contructor por default.
	 */
	public JdbcContabilidadDao() {
		super();
	}
	
	/**
	 * Servicio que inicia la búsqueda de operaciones de utilidad/pérdida del registro contable.
	 * Recibe como parámetro el idLoteCarga que el proceso contable de SICA acaba de generar.
	 * @param idLoteCarga
	 */
	private void actualizar_P_And_L_PerdidasGanaciasSica(int idLoteCarga)
	{
		boolean ejecutarServicio_P_AND_L = false, escribirBitacoras_P_AND_L = false;
		
		String queryParametrosPermisos = "SELECT ID_PARAMETRO, VALOR FROM SC_PARAMETRO " +
				                         "WHERE  ID_PARAMETRO LIKE 'P_AND_L%'";
		
		String queryEncabezado_P_AND_L = "select FEC_DOCUMENTO, FEC_GENERACION, FEC_PROCESO, APLICACION, SUBAPLICACION, " +
				                         "       OPERACION, FUENTE, ID_CARGA, REGAPROCESAR, REGPROCESADOS, " +
				                         "       TOT_CARGOS, TOT_ABONOS, TOT_REG_CARGOS, TOT_REG_ABONOS, ESTATUS " +
				                         " from  SAP_A_BITACORA_XS " +
				                         "where  ID_CARGA = " + idLoteCarga;
		
		String queryOperacionesPAndL = "select FECHADOC, FECHACONT, CLASEDOC, SOCIEDAD, MONEDA, TIPOCAMBIO, " +
									   "       PERIODO, REFERENCIA, TEXTOCAB, CLAVECONTA, CUENTACONTA, IMPORTE, " +
									   "       CENTROCOSTOS, CENTROBENEF, INDIMPUESTO, IMPORTEBASEIMPUESTO, " +
									   "       ASIGNACION, TEXTOPOS, FECHAVALOR, SOCIEDADASOC, SEGMENTO, " +
									   "       USUARIO, ID_REGISTRO, IMPMONLOCAL, IND_ORIGEN, FECHA_PROCESO, " +
									   "       STATUS, LOTE_CARGA, CIFRADO, PRODUCTO " +
                                       "from   SAP_A_GENPOL_XS " + 
                                       "where  LOTE_CARGA = " + idLoteCarga + 
                                       "  and  TEXTOPOS like 'UTILIDAD/P%' AND CLASEDOC = 'XS' " +
                                       "order by CLAVECONTA";
		
		Map numerosCuenta = null, centrosBenef = null, centrosBenefTemp = null, numerosCuentaTemp = null;
		List parametros = null, registrosPAndL = null, encabezado_P_AND_L = null;
		Iterator it = null;
		Map fila = null;
		String parametro = null, valor = null, concepto = null;
		
		logger.info("--> actualizar_P_And_L_PerdidasGanaciasSica() -- Se validará si el proceso de P&L debe ejecutarse...");
		
		// el proceso consulta los parámetros 'P_AND_L' de la tabla SC_PARAMETRO para validar si la bandera de ejecución 
		// 'P_AND_L_EXECUTE' existe y tiene el valor 1 para continuar la ejecución de este proceso.
		parametros = getJdbcTemplate().queryForList(queryParametrosPermisos);
		
		if(parametros != null && !parametros.isEmpty())
		{
			numerosCuentaTemp = new HashMap();
			centrosBenefTemp = new HashMap();
			it = parametros.iterator();
			
			/*
			 * El siguiente bloque recorre la lista de parámetros y llena 2 hashMap temporales.
			 * Los parámetros de cuentas y centros de beneficiario tienen el siguiente formato:
			 * 
			 * P_AND_L_CUENTA_DIVISAS y P_AND_L_CENTROBEN_DIVISAS.
			 * 
			 * Dentro del 'while' se poblan dos hashMap: numerosCuentaTemp y centrosBenefTemp
			 * Cada hashMap tiene como clave la última palabra del parámetro, en este caso 'DIVISAS'.
			 * De tal manera que cada hashMap lleva la cuenta que le corresponde a cada concepto y el centroBenef para cada concepto.
			 * 
			 * numerosCuentaTemp: {DIVISAS,520501; METALES,520502; CONTRACUENTAS,800}
			 * 
			 *  centrosBenefTemp: {DIVISAS,5412;   METALES,5412;   CONTRACUENTAS,0226}
			 *  
			 * Ademas se actualiza si el proceso tiene permiso de ejecución y si puede escribir el la bitácora.
			 * */
			while(it.hasNext())
			{
				fila = (Map)it.next();
				if(fila != null)
				{
					parametro = (String)fila.get("ID_PARAMETRO");
					valor = (String)fila.get("VALOR");//P_AND_L_EXECUTE
					
					if(parametro != null && valor != null)
					{
						if("P_AND_L_EXECUTE".equals(parametro) && "1".equals(valor))
							ejecutarServicio_P_AND_L = true;
						else if("P_AND_L_BITACORA".equals(parametro) && "1".equals(valor))
							escribirBitacoras_P_AND_L = true;
						else 
						{
							if(parametro.startsWith("P_AND_L_CUENTA_"))
							{
								concepto = parametro.substring(parametro.lastIndexOf("_") + 1); // ej: DIVISAS
								if(!numerosCuentaTemp.containsKey(concepto))
									numerosCuentaTemp.put(concepto.toUpperCase(), valor); // ej: DIVISAS,cuenta
							}
							else if(parametro.startsWith("P_AND_L_CENTROBEN_"))
							{
								concepto = parametro.substring(parametro.lastIndexOf("_") + 1); // ej: DIVISAS
								if(!centrosBenefTemp.containsKey(concepto))
									centrosBenefTemp.put(concepto.toUpperCase(), valor);  // DIVISAS,centroBenef
							}
						}
					}
				}
			}
			
			// Si no hay ningún parámetro con información de cuentas el proceso temina su ejecución
			if(numerosCuentaTemp.isEmpty())
			{
				logger.info("--> actualizar_P_And_L_PerdidasGanaciasSica() -- " +
						    "No se encontró ninguna cuenta de P&L registrada en el sistema..");
				logger.info("--> Fin de proceso de P&L.");
				return;
			}
			else
			{
				numerosCuenta = new HashMap();
				centrosBenef = new HashMap();
				
				it = numerosCuentaTemp.keySet().iterator();
				
				/*
				 * Se organiza la info de los hashMap para clasificar los datos por número de cuenta, de tal forma
				 * que el hashMap de cuentas tenga conocimiento de los registros a los que se les va a aplicar las reglas
				 * que corresponden a cada número de cuenta.
				 *	          
				 *                numeroCuenta: 520501       centroBenef: 5412  
				 * 
				 * numerosCuenta: {520501, lista de registros de P&L con este numero de cuenta}
				 *  centrosBenef: {520501, 5412}
				 */
				while(it.hasNext())
				{
					concepto = (String)it.next(); // ej: DIVISAS
					valor = (String)numerosCuentaTemp.get(concepto); // Num Cuenta en SC_PARAMETRO
					
					if(!numerosCuenta.containsKey(valor)) // el numero de cuenta
					{
						numerosCuenta.put(valor, new ArrayList()); // numeroCuenta, array para registros con esa cuenta
						centrosBenef.put(valor, (String)centrosBenefTemp.get(concepto)); // numeroCuenta, centroBenef
					}
				}

				numerosCuentaTemp.clear(); 
				numerosCuentaTemp = null;
				centrosBenefTemp.clear();
				centrosBenefTemp = null;
				
				// si la bandera de ejecución está activada, continúa el flujo
				if(ejecutarServicio_P_AND_L)
				{
					logger.info("--> actualizar_P_And_L_PerdidasGanaciasSica() -- Iniciando servicio P_AND_L de SICA...");
					logger.info("--> actualizar_P_And_L_PerdidasGanaciasSica() -- Seleccionar operaciones de la tabla " +
							    "SAP_A_GENPOL_XS en donde el campo TEXTOPOS tiene el valor: 'UTILIDAD/PÉRDIDA %' ");
					
					// se obtiene el encabezado de la bitácora para posteriormente actualizar los totales de SAP_A_BITACORA_XS
					encabezado_P_AND_L = getJdbcTemplate().queryForList(queryEncabezado_P_AND_L);
					
					// se obtienen los registros de P&L asociados al lote de carga del encabezado
					registrosPAndL = getJdbcTemplate().queryForList(queryOperacionesPAndL);
					
					if(encabezado_P_AND_L != null && !encabezado_P_AND_L.isEmpty())
					{
						logger.info("--> actualizar_P_And_L_PerdidasGanaciasSica() -- Encabezado 'SAP_A_BITACORA_XS': " + 
								      ((Map)encabezado_P_AND_L.get(0)).toString());
						if(registrosPAndL != null && !registrosPAndL.isEmpty())
						{
							logger.info("--> actualizar_P_And_L_PerdidasGanaciasSica() -- Se encontraron " + 
									    registrosPAndL.size() + " registro(s) de P&L");
							logger.info("--> actualizar_P_And_L_PerdidasGanaciasSica() -- Se filtraran los registros de " +
									    "P&L (Divisas, Metales y Contrapartes...");
							
							// ahora con los registros consultados, se procede a filtralos según las diferentes cuentas
							// registradas en SC_PARAMETRO y almacenadas temporalmente en el hashMap 'numerosCuenta'
							filtrarCuentas_P_AND_L(registrosPAndL, numerosCuenta);
							
							// si el permiso de escritura en la bitácora está habilitado se graban los registros de P&L
							// antes de ser actualizados
							if(escribirBitacoras_P_AND_L)
								escribirBitacoras(idLoteCarga);
							else
								logger.info("--> actualizar_P_And_L_PerdidasGanaciasSica() -- No se tiene permiso " +
										    "para escribir en las bitácoras de P&L.");
							
							// este método realiza la tarea de actualizar los registros de P&L ya clasificados
							actualizarDatos_P_And_L(idLoteCarga, numerosCuenta, centrosBenef);
							logger.info("--> Fin de proceso de P&L.");
						}
						else
							logger.info("--> actualizar_P_And_L_PerdidasGanaciasSica() -- No encontraron " +
									    "registros de P&L con el lote_carga: " + idLoteCarga);
					}
					else
						logger.info("--> actualizar_P_And_L_PerdidasGanaciasSica() -- No existe ningún registro " +
								    "con el id_carga: " + idLoteCarga);
				}
				else
					logger.info("--> actualizar_P_And_L_PerdidasGanaciasSica() -- El proceso de P&L no " +
							    "tiene permisos de ejecución.");
			}	
		}
		else
			logger.info("--> actualizar_P_And_L_PerdidasGanaciasSica() -- El proceso de P&L no tiene permisos de ejecución. " +
					    "No se encontró la variable P_AND_L_EXECUTE");
	}
	
	/**
	 * Servicio que se encarga de aplicar las reglas de P&L para cada cuenta en SC_PARAMETRO y actualiza 
	 * las tablas SAP_A_GENPOL_XS y SAP_A_BITACORA_XS
	 * @param loteCarga
	 * @param cuentasFiltradas
	 * @param centrosBenef
	 */
	private void actualizarDatos_P_And_L(int loteCarga, Map cuentasFiltradas, Map centrosBenef)
	{
		//String prefijoContraparte = "800", prefijoDivisas = "520501", prefijoMetales = "520502";
		String prefijoOtrasCuentas = "otrasCuentas"; 
		
		//String centrobenef_div_metal = "5412", centrobenef_contrapartes = "0226";
		String updateOperacion = "UPDATE SAP_A_GENPOL_XS SET MONEDA = 'MXN', TEXTOCAB = 'BATCHXSMXN', " +
				                                           " IMPORTE = IMPMONLOCAL, IMPMONLOCAL = NULL, CENTROBENEF = ";
		String whereOperacion = " WHERE ID_REGISTRO = ";
		
		String updateEncabezado = "UPDATE SAP_A_BITACORA_XS SET TOT_CARGOS = ?, TOT_ABONOS = ? WHERE ID_CARGA = ?";
		String querysPorEjecutar[] = null;
		int datosInsertados[] = null;
		StringBuffer updates = new StringBuffer();
		List operacionesOtrasCuentas = null;
		Iterator it = null;
		Map fila = null;
		
		try
		{	
			complementarQueryUpdate(updateOperacion, whereOperacion, updates, cuentasFiltradas, centrosBenef);
			
			if(updates.length() > 0)
			{
				// se divide la cadena de querys para obtener un arreglo de updates
				querysPorEjecutar = updates.toString().split(";;;");
		   		
		   		if(querysPorEjecutar != null && querysPorEjecutar.length > 0)
		   		{
		   			// Se actualizan los registros de la tabla SAP_A_GENPOL_XS
		   			datosInsertados = getJdbcTemplate().batchUpdate(querysPorEjecutar);
			   		
		   			// se calculan los nuevos totales
			   		Map cargosMap = getJdbcTemplate().queryForMap(GENPOL_XS_CARGOS_QUERY, new Object[]{new Integer(loteCarga)});
					Map abonosMap = getJdbcTemplate().queryForMap(GENPOL_XS_ABONOS_QUERY, new Object[]{new Integer(loteCarga)});
			        BigDecimal totalCargos = cargosMap.get(TOTALCARGOS) != null ?
			   		                                       new BigDecimal(cargosMap.get(TOTALCARGOS).toString().trim()) :
			                                               new BigDecimal(Num.S_0_0);
			   		                                       
			   		BigDecimal totalAbonos = abonosMap.get(TOTALABONOS) != null ?
			   											   new BigDecimal(abonosMap.get(TOTALABONOS).toString().trim()) :
			   											   new BigDecimal(Num.S_0_0);
			   		
			   		logger.info(" --> actualizarDatos_P_And_L() -- Se actualizan los totales de SAP_A_BITACORA_XS: " +
			   				    "Total Cargos: " + totalCargos + ", Total Abonos: " + totalAbonos + ", ID_CARGA: " + loteCarga);
			   		
			   		// se actualizan los totales en la tabla SAP_A_BITACORA_XS
			   		getJdbcTemplate().update(updateEncabezado, new Object[]{totalCargos, totalAbonos, new Integer(loteCarga)});
		   		}
		   		else
		   			logger.info(" --> actualizarDatos_P_And_L() -- No hay registros para actualizar de divisas, metales y contrapartes..");
				
		   		// si hay operaciones de cuentas desconocidas, solo se muestran en el log
		   		operacionesOtrasCuentas = (List)cuentasFiltradas.get(prefijoOtrasCuentas);
		   		if(operacionesOtrasCuentas != null && !operacionesOtrasCuentas.isEmpty())
		   		{
		   			logger.info(" --> actualizarDatos_P_And_L() -- otras cuentas, numero de registros: " + operacionesOtrasCuentas.size());
		   			it = operacionesOtrasCuentas.iterator();
		   			while(it.hasNext())
		   			{
		   				fila = (Map)it.next();
		   				logger.info(" --> actualizarDatos_P_And_L() -- fila: " + fila.toString());
		   			}
		   		}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		} 
	}
	
	/**
	 * Este servicio se encarga de poblar un StringBuffer con las instrucciones de actualización de los
	 * registros filtrados previamente.
	 * la instrucción de actualización se ve complementada con el id del registro de P&L que se va a modificar. 
	 * @param update
	 * @param Where
	 * @param acumulador
	 * @param cuentasFiltradas
	 * @param centrosBenef
	 */
	private void complementarQueryUpdate(String update, String Where, StringBuffer acumulador, 
			                             Map cuentasFiltradas, Map centrosBenef)
	{
		Map fila = null;
		String cuenta, centroBenef, queryUpdate;
		List registros = null;
		Iterator it = cuentasFiltradas.keySet().iterator();
		String prefijoOtrasCuentas = "otrasCuentas";
		
		while(it.hasNext())
		{
			cuenta = (String)it.next();
			
			if(!cuenta.equals(prefijoOtrasCuentas))
			{
				registros = (List)cuentasFiltradas.get(cuenta);
				centroBenef = (String)centrosBenef.get(cuenta);
				
				logger.info("--> complementarQueryUpdate() -- Cuenta: " + cuenta + ", total de registros: " + registros.size());
				for(int indice = 0; indice < registros.size(); indice++)
				{
					fila = (Map)registros.get(indice);
					
					if(acumulador.length() > 0)
						acumulador.append(";;;");
					
					// se agrega el ID_REGISTRO de la fila que va a actualizarse
					queryUpdate = update + "'" + centroBenef + "' " + Where + fila.get("ID_REGISTRO").toString();
					acumulador.append(queryUpdate);
					logger.info("--> complementarQueryUpdate() -- query: " + queryUpdate);
				}
			}
		}
	}
	
	/**
	 * Servicio que actualiza la bitácora de P&L. 
	 * @param idLoteCarga
	 */
	private void escribirBitacoras(int idLoteCarga)
	{
		logger.info(" --> escribirBitacoras() -- Se procede a escribir la bitácora del lote de carga: " + idLoteCarga);
		String insertEncabezado = "INSERT INTO SICA_ADMIN.SC_BITACORA_PL (" +
				                  "           ID_BITACORA, FEC_DOCUMENTO, FEC_GENERACION, " +
								  "           FEC_PROCESO, APLICACION, SUBAPLICACION, OPERACION, FUENTE, " + 
                                  "           ID_CARGA, REGAPROCESAR, REGPROCESADOS,TOT_CARGOS, TOT_ABONOS, " +
                                  "           TOT_REG_CARGOS, TOT_REG_ABONOS, ESTATUS ) " +
                                  "SELECT     SC_BITACORA_PL_SEQ.NEXTVAL, FEC_DOCUMENTO, FEC_GENERACION, FEC_PROCESO, APLICACION, " +
                                  "           SUBAPLICACION, OPERACION, FUENTE, ID_CARGA, REGAPROCESAR, " +
                                  "           REGPROCESADOS, TOT_CARGOS, TOT_ABONOS, TOT_REG_CARGOS, " +
                                  "           TOT_REG_ABONOS, ESTATUS " +
                                  "FROM       SAP_A_BITACORA_XS " +
                                  "WHERE      ID_CARGA = " + idLoteCarga;
		
		
		String insertOperacion = "INSERT INTO SICA_ADMIN.SC_BITACORA_PL_GENPOL (" +
				                 "             ID_GENPOL, FECHADOC, FECHACONT, CLASEDOC, SOCIEDAD, MONEDA, TIPOCAMBIO, " +
				                 "             PERIODO, REFERENCIA, TEXTOCAB, CLAVECONTA, CUENTACONTA, IMPORTE, " +
				                 "             CENTROCOSTOS, CENTROBENEF, INDIMPUESTO, IMPORTEBASEIMPUESTO, " +
				                 "             ASIGNACION, TEXTOPOS, FECHAVALOR, SOCIEDADASOC, SEGMENTO, " +
				                 "             USUARIO, ID_REGISTRO, IMPMONLOCAL, IND_ORIGEN, FECHA_PROCESO, STATUS, " +
                                 "             LOTE_CARGA, CIFRADO, PRODUCTO) " +
                                 "SELECT       SC_BITACORA_PL_GENPOL_SEQ.NEXTVAL, FECHADOC, FECHACONT, CLASEDOC, " +
                                 "             SOCIEDAD, MONEDA, TIPOCAMBIO, PERIODO, REFERENCIA, TEXTOCAB, " +
                                 "             CLAVECONTA, CUENTACONTA, IMPORTE, CENTROCOSTOS, CENTROBENEF, " +
                                 "             INDIMPUESTO, IMPORTEBASEIMPUESTO, ASIGNACION, TEXTOPOS, " +
                                 "             FECHAVALOR, SOCIEDADASOC, SEGMENTO, USUARIO, ID_REGISTRO, " +
                                 "             IMPMONLOCAL, IND_ORIGEN, FECHA_PROCESO, STATUS, " +
                                 "             LOTE_CARGA, CIFRADO, PRODUCTO " +
                                 "FROM         SAP_A_GENPOL_XS " +
                                 "WHERE        LOTE_CARGA = " + idLoteCarga + " AND TEXTOPOS LIKE 'UTILIDAD/PÉRDIDA %' " +
                                 "  AND        CLASEDOC = 'XS'";
		
		try
		{
			int datosInsertados[] = getJdbcTemplate().batchUpdate(new String[]{insertEncabezado, insertOperacion});
			for(int indice = 0; indice < datosInsertados.length; indice++)
				logger.info(" --> escribirBitacoras() -- Num de registros insertados: " + datosInsertados[indice]);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			logger.info(" --> escribirBitacoras() -- Ocurrió un error al escribir en la bitácora de P&L, lote_carga: " + idLoteCarga);
		}
		
		logger.info(" --> escribirBitacoras() -- Fin de la escritura de la bitácora para el lote de carga: " + idLoteCarga);
	}
	
	/***
	 * Este método clasifica los registros de P&L según la cuenta que tengan almacenada en el campo
	 * 'CUENTACONTA' y los almacena en el hashMap 'numerosCuenta'
	 * @param registros
	 * @param numerosCuenta
	 */
	private void filtrarCuentas_P_AND_L(List registros, Map numerosCuenta)
	{
		List otrasCuentas = null;
		Iterator it = null, itCuentas = null;
		Map fila = null;
		String prefijoOtrasCuentas = "otrasCuentas", cuentaScParametro = null;
		String mensaje = null;
		BigDecimal cuentaConta = null;
		boolean registroEncontrado = false; 
		
		if(registros == null || registros.isEmpty() || numerosCuenta == null || numerosCuenta.isEmpty())
			return;
		
		otrasCuentas = new ArrayList();
		
		// se crea una entra para los registro con cuenta desconocida.
		// si las cuentas están correctamente configuradas en la base de datos, esta lista debe estar vacía
		numerosCuenta.put(prefijoOtrasCuentas, otrasCuentas);
		
		it = registros.iterator();
		
		// se realiza el filtro de los registros. Se poblan las listas que existen en el hashMap
		while(it.hasNext())
		{
			fila = (Map)it.next();
			registroEncontrado = false;
			logger.info(" --> filtrarCuentas_P_AND_L() -- fila: " + fila.toString());
			
			if(fila.containsKey("CUENTACONTA"))
			{
				cuentaConta = (BigDecimal)fila.get("CUENTACONTA");
				if(cuentaConta != null)
				{
					itCuentas = numerosCuenta.keySet().iterator();
					while(itCuentas.hasNext())
					{
						cuentaScParametro = (String)itCuentas.next();
						if(!cuentaScParametro.equals(prefijoOtrasCuentas) && cuentaConta.toString().startsWith(cuentaScParametro))
						{
							((List)numerosCuenta.get(cuentaScParametro)).add(fila);
							registroEncontrado = true;
							mensaje = cuentaScParametro;
							break;
						}
					}
					
					if(!registroEncontrado)
					{
						otrasCuentas.add(fila);
						mensaje = "otras cuentas - (no se modifica este registro)";
					}
					
					logger.info("--> filtrarCuentas_P_AND_L() -- El registro anterior se agregó a la " +
							    "lista que tiene el num de cuenta: " + mensaje);
					mensaje = null;
				}
				else
					logger.info("--> filtrarCuentas_P_AND_L() -- El registro anterior tiene un valor null " +
							    "en el campo CUENTACONTA, no se procesa.");
			}
		}
		
		itCuentas = numerosCuenta.keySet().iterator();
		while(itCuentas.hasNext())
		{
			cuentaScParametro = (String)itCuentas.next();
			logger.info(" --> filtrarCuentas_P_AND_L() -- Número de registros para la lista con numero " +
					    "de cuenta '" + cuentaScParametro + "': " + ((List)numerosCuenta.get(cuentaScParametro)).size());
		}
	}
	
    /**
     * @param fechaSistema La fecha actual de sistema.
     * @param fechaProceso La fecha seleccionada para procesar la contabilidad.
     * @param contratosLiquidacionEspecial La lista de contratos de liquidaci&oacute;n especial.
     * @see ContabilidadDao#generarRegistosContablesSAP(Date, Date, String).
     */
    public void generarRegistosContablesSAP(Date fechaSistema, Date fechaProceso,
                                           String contratosLiquidacionEspecial) {
        warn("Iniciando proceso.- Generaci\u0021n Registros Contables para SAP");
        int idLoteCarga = iniciarBitacora(fechaSistema, fechaProceso);
        insertarRegistosContablesSAP(fechaSistema, fechaProceso, idLoteCarga, new StringBuffer(""),
                contratosLiquidacionEspecial);
        vincularTransaccionRegistro(fechaProceso, new StringBuffer(""));
        finalizarBitacora(idLoteCarga);
        warn("Finalizando proceso.- Generaci\u0021n Registros Contables para SAP");
        actualizar_P_And_L_PerdidasGanaciasSica(idLoteCarga);
    }

    /**
     * @param polizas La lista de polizas a insertar
     * @param fechaSistema La fecha actual de sistema.
     * @param fechaProceso La fecha seleccionada para procesar la contabilidad.
     * @param contratosLiquidacionEspecial La lista de contratos de liquidaci&oacute;n especial.
     * @see ContabilidadDao#generarRegistosContablesSAP(List, Date, Date, String).
     */
    public void generarRegistosContablesSAP(List polizas, Date fechaSistema, Date fechaProceso,
                                           String contratosLiquidacionEspecial) {
        logger.info("Iniciando proceso - Generaci\u0021n Registros Contables para SAP con lista " +
                "de polizas");
		int idLoteCarga = iniciarBitacora(fechaSistema, fechaProceso);
        StringBuffer sb = new StringBuffer("");
        if (!polizas.isEmpty()) {
            sb.append("AND t1.ID_DEAL IN ( ");
            for (Iterator iterator = polizas.iterator(); iterator.hasNext();) {
                Poliza pol = (Poliza) iterator.next();
                if (iterator.hasNext()) {
                    sb.append(pol.getIdDeal()).append(STR_COMA);
                }
                else {
                    sb.append(pol.getIdDeal());
                }
            }
            sb.append(STR_CIERRE_PARENT);
        }
        insertarRegistosContablesSAP(fechaSistema, fechaProceso, idLoteCarga, sb,
                contratosLiquidacionEspecial);
        vincularTransaccionRegistro(fechaProceso, sb);
        finalizarBitacora(idLoteCarga);
        logger.warn("Finalizando proceso - Generaci\u0021n Registros Contables para SAP");
        actualizar_P_And_L_PerdidasGanaciasSica(idLoteCarga);
    }

    /**
     * @param fechaSistema La fecha actual de sistema.
     * @param fechaProceso La fecha seleccionada para procesar la contabilidad.
     * @param contratosLiquidacionEspecial La lista de contratos de liquidaci&oacute;n especial.
     * @see com.ixe.ods.sica.dao.ContabilidadDao#reprocesarRegistrosContablesSAP(Date, Date, String)
     */
    public void reprocesarRegistrosContablesSAP(Date fechaSistema, Date fechaProceso,
                                               String contratosLiquidacionEspecial) {
        logger.info("Iniciando borrado de registros SAP");
        int idLoteCarga = getJdbcTemplate().queryForInt(SEL_ID_LOTE_CARGA_SAP_BITACORA_XS,
                new Object[]{fechaProceso});
        logger.info("\tIdLoteCarga : " + idLoteCarga);
        int elementosEliminados = getJdbcTemplate().update(DEL_SAP_GELPOL_XS_BY_FEC, new Object[]{
                new Integer(idLoteCarga)});
        logger.info(elementosEliminados + "elementos elimidados de SAP_A_GENPOL_XS");
        logger.info("Terminando borrado de registros SAP");
        logger.info("Iniciando reproceso - Generaci\u0021n Registros Contables para SAP");
        actualizarBitacoraInicioReproceso(fechaProceso, idLoteCarga);
        insertarRegistosContablesSAP(fechaSistema, fechaProceso, idLoteCarga, new StringBuffer(""),
                contratosLiquidacionEspecial);
        vincularTransaccionRegistro(fechaProceso, new StringBuffer(""));
        finalizarBitacora(idLoteCarga);
        logger.info("Finalizando reproceso - Generaci\u0021n Registros Contables para SAP");
        actualizar_P_And_L_PerdidasGanaciasSica(idLoteCarga);
    }

    /**
     * @param fechaSistema La fecha a revisar.
     * @see com.ixe.ods.sica.dao.ContabilidadDao#actualizarEstatusDealsSicaVsSite(Date).
     */
    public void actualizarEstatusDealsSicaVsSite(Date fechaSistema) {
        int noReg = getJdbcTemplate().update(UPD_SC_DEAL_STATUS_TL);
        logger.info("Deal(Encabezados) liquidados en SITE pero no en SICA que han " +
                "sido actualizados: " + noReg + " regs.");
        noReg = getJdbcTemplate().update(UPD_SC_DEAL_DETALLE_STATUS_TT);
        logger.info("Deal(Detalles) liquidados en SITE pero no en SICA que han sido " +
                "actualizados: " + noReg + " registros.");
    }

    /**
     * @param fechaSistema La fecha a revisar.
     * @see com.ixe.ods.sica.dao.ContabilidadDao#obtenerDealsCuentanDetallesDuplicados(Date).
     */
    public List obtenerDealsCuentanDetallesDuplicados(Date fechaSistema){
    	return getJdbcTemplate().queryForList(QRY_DEAL_DETALLE_DUPLICADO,
                new Object[]{fechaSistema});
    }

    /**
     *
     * @param fechaSistema La fecha actual del sistema.
     * @param fechaProceso La fecha del proceso contable.
     * @param idLoteCarga El n&uacute;mero de lote de carga.
     * @param sb Complemento del query.
     * @param contratosLiquidacionEspecial La lista de contratos de liquidaci&oacute;n especial.
     */
    private void insertarRegistosContablesSAP(Date fechaSistema, Date fechaProceso, int idLoteCarga,
                                             StringBuffer sb, String contratosLiquidacionEspecial) {
        logger.info("\tInsertando registros contables para SAP");
        String querySelect = SEL_GENPOL_XS;
        querySelect = querySelect.replaceAll("\\@\\@", contratosLiquidacionEspecial);
        querySelect = querySelect.replaceAll(STR_ARROBA_EXPR, sb.toString());
        List registros = getJdbcTemplate().queryForList(querySelect, new Object[]{fechaProceso,
                fechaProceso});
        ejecutarInsertsSAP(registros, fechaSistema, fechaProceso, idLoteCarga);
        actualizarTextoPos(idLoteCarga, sb);
    }

    /**
     * Genera los registros para la tabla SAP_A_GENPOL_XS.
     *
     * @param registros La lista de registros.
     * @param fechaSistema La fecha actual del sistema.
     * @param fechaProceso La fecha contable.
     * @param idLoteCarga El n&uacute;mero de lote de carga para SAP.
     */
    private void ejecutarInsertsSAP(List registros, Date fechaSistema, Date fechaProceso,
                                   int idLoteCarga) {
        Map cabeceras = new HashMap();
        StringBuffer nombreCol;
        StringBuffer valores;

        warn("Insertando " + registros.size() + ".");
        for (Iterator it = registros.iterator(); it.hasNext();) {
            Map row = (Map) it.next();
            nombreCol = new StringBuffer("");
            valores = new StringBuffer("");
            if (row.get(CENTROCOSTOS) != null) {
                nombreCol.append(", CENTROCOSTOS ");
                valores.append(STR_COMA_COMILLA).append(row.get(CENTROCOSTOS)).append(STR_COMILLA);
            }
            if (row.get(CENTROBENEF) != null) {
                nombreCol.append(", CENTROBENEF ");
                valores.append(STR_COMA_COMILLA).append(((String) row.get(CENTROBENEF)).trim()).
                        append(STR_COMILLA);
            }
            if (row.get(INDIMPUESTO) != null) {
                nombreCol.append(", INDIMPUESTO");
                valores.append(STR_COMA_COMILLA).append(row.get(INDIMPUESTO)).append(STR_COMILLA);
            }
            if (row.get(IMPORTEBASEIMPUESTO) != null) {
                nombreCol.append(", IMPORTEBASEIMPUESTO");
                valores.append(STR_COMA).append(row.get(IMPORTEBASEIMPUESTO));
            }
            if (row.get(IMPMONLOCAL) != null) {
                nombreCol.append(", IMPMONLOCAL");
                valores.append(STR_COMA).append(row.get(IMPMONLOCAL));
            }
            if (row.get(ASIGNACION) != null) {
            	nombreCol.append(", ASIGNACION");
                valores.append(STR_COMA_COMILLA).append(row.get(ASIGNACION)).append(STR_COMILLA);
            }
            completarQueryInsSap(cabeceras, nombreCol, valores, row);
        }
        int _inserts = 0;
        for (Iterator iter2 = cabeceras.values().iterator(); iter2.hasNext();) {
            List inserts = (List) iter2.next();

            for (Iterator iter4 = inserts.iterator(); iter4.hasNext();) {
                Map _row = (Map) iter4.next();

                getJdbcTemplate().update(_row.get(QUERY).toString(),
                        new Object[]{fechaSistema, fechaProceso, _row.get(MONEDA),
                                _row.get(TEXTOCAB), _row.get("CLAVECONTA"),
                                _row.get("CUENTACONTA"), _row.get("IMPORTE"),
                                new Integer(idLoteCarga), _row.get("PRODUCTO"),
                                (Integer) _row.get(REFERENCIA), _row.get("TEXTOPOS")});
                _inserts++;
                if (_inserts % 10 == 0) {
                    warn("Registro " + _inserts + 1 + " / " + cabeceras.size());
                }
            }
        }
    }

    /**
     * Completa el query de inserci&oacute;n en la tabla SAP_A_GENPOL_XS.
     *
     * @param cabeceras El mapa de cabeceras.
     * @param nombreCol La cadena con los nombres de las columnas.
     * @param valores La cadena con los valores para las columnas.
     * @param row El mapa del rengl&oacute;n.
     */
    private void completarQueryInsSap(Map cabeceras, StringBuffer nombreCol, StringBuffer valores,
                                Map row) {
        String queryString;
        List querysList;
        int idReferencia;
        queryString = IN_A_GENPOL_XS.replaceAll(STR_ARROBA_EXPR, nombreCol.toString());
        queryString = queryString.replaceAll("\\$", valores.toString());
        if (!cabeceras.containsKey(row.get(TEXTOCAB))) {
            querysList = new ArrayList();
            idReferencia = getJdbcTemplate().queryForInt(
                    "SELECT SAP_REFERENCIA_XS_SEQ.NEXTVAL FROM DUAL");
            row.put(QUERY, queryString);
            row.put(REFERENCIA, new Integer(idReferencia));
            querysList.add(row);
        }
        else {
            querysList = (List) cabeceras.get(row.get(TEXTOCAB));
            idReferencia = ((Integer) (((Map) querysList.get(0)).get(REFERENCIA))).intValue();
            row.put(QUERY, queryString);
            row.put(REFERENCIA, new Integer(idReferencia));
            querysList.add(row);
            cabeceras.remove(row.get(MONEDA));
        }
        cabeceras.put(row.get(TEXTOCAB), querysList);
    }

    /**
     * Inicializa la bit&aacute;cora de SAP
     *
     * @param fechaSistema La fecha actual.
     * @param fechaGeneracion La fecha a regenerar.
     * @return int.
     */
    private int iniciarBitacora(Date fechaSistema, Date fechaGeneracion) {
        int idCarga = getJdbcTemplate().queryForInt("SELECT SAP_BITACORA_XS_SEQ.NEXTVAL FROM DUAL");
        logger.info("" + idCarga);
        logger.info("\tIniciando en bit\u00e1cora");
        getJdbcTemplate().update(INS_INICIO_BITACORA_XS, new Object[]{fechaSistema, fechaGeneracion,
                new Integer(idCarga)});
        logger.info("Finalizando B\u00e1tacora");
        return idCarga;
    }

    /**
     * Actualiza la bit&aacute; de SAP.
     *
     * @param fechaProceso La fecha a procesar.
	 * @param idLoteCarga El n&uacute;mero de lote de carga.
	 */
	private void actualizarBitacoraInicioReproceso(Date fechaProceso, int idLoteCarga) {
		logger.info("Actualizando Bitacora");
		getJdbcTemplate().update(UPD_REPROCESO_SAP_BITACORA_XS, new Object[]{
				fechaProceso, new Integer(idLoteCarga)});
		logger.info("Finalizando actualizacion Bitacora");
	}

    /**
     * Finaliza la bit&aacute;cora de SAP
     *
     * @param loteCarga El n&uacute;mero de lote de carga.
     */
	private void finalizarBitacora(int loteCarga) {
        logger.info("\tFinalizando bit\u00e1cora");
        Map cargosMap = getJdbcTemplate().queryForMap(GENPOL_XS_CARGOS_QUERY,
                new Object[]{new Integer(loteCarga)});
        Map abonosMap = getJdbcTemplate().queryForMap(GENPOL_XS_ABONOS_QUERY,
                new Object[]{new Integer(loteCarga)});
        BigDecimal totalCargos = cargosMap.get(TOTALCARGOS) != null ?
        		new BigDecimal(cargosMap.get(TOTALCARGOS).toString().trim()) :
                new BigDecimal(Num.S_0_0);
		BigDecimal totalAbonos = abonosMap.get(TOTALABONOS) != null ?
        		new BigDecimal(abonosMap.get(TOTALABONOS).toString().trim()) :
                new BigDecimal(Num.S_0_0);
        Integer totalRegistrossCargos = cargosMap.get(TOTALREGISTROSCARGOS) != null ?
                new Integer(cargosMap.get(TOTALREGISTROSCARGOS).toString()) : new Integer(0);
        Integer totalRegistrosCargos = abonosMap.get(TOTALREGISTROSABONOS) != null ?
                new Integer(abonosMap.get(TOTALREGISTROSABONOS).toString()) : new Integer(0);
        BigDecimal v = totalCargos.subtract(totalAbonos);
        logger.warn("Cargos: " + totalCargos);
        logger.warn("Abonos: " + totalAbonos);
        double dif = Math.abs(v.doubleValue());
        logger.info("V: " + v);
        logger.info("Diferencia: " + dif);
        if (dif >= Num.D_0_01) {
            logger.warn("finalizaBitacora() - La suma de cargos es diferente a la suma de abonos");
            throw new SicaException("La suma de cargos es diferente a la suma de abonos");
        }
        getJdbcTemplate().update(UPD_FINALIZA_BITACORA_XS, new Object[]{totalCargos, totalAbonos,
                totalRegistrossCargos, totalRegistrosCargos,
                new Double(totalRegistrossCargos.intValue() + totalRegistrosCargos.intValue()),
                new Integer(loteCarga)});
    }

    /**
     * Realiza el proceso de vinculaci&oacute; SICA - SAP
     *
     * @param fechaProceso La fecha a procesar.
     * @param sb Complemento del query.
     */
    private void vincularTransaccionRegistro(Date fechaProceso, StringBuffer sb) {
		logger.info("Iniciando generacion vinculo");
		String query = UPD_SC_POLIZA_SAP_REFERENCIA;
		query = query.replaceAll(STR_ARROBA_EXPR, sb.toString());
        query = query.replaceAll("t1", "sp");
		getJdbcTemplate().update(query,
                new Object[]{fechaProceso, fechaProceso});
		logger.info("Terminando vinvulaci\u00f3n");
	}

    /**
     * Actualiza el campo TEXTOPOS una vez que tenemos datos en la tabla SAP_A_GENPOL_XS.
     * 
     * @param idLoteCarga La fecha a procesar.
     * @param strBuff El complemento del query.
     */
    private void actualizarTextoPos(int idLoteCarga, StringBuffer strBuff) {
        logger.info("Iniciando actualizaci\u00f3n de TEXTOPOS");
        logger.info("Comenzado con registros de Deals");
        //RMC: se corregie la consulta 
        String query = UPD_GENPOL_XS_TEXTOPOS_DEALS;
        query = query.replaceAll(STR_ARROBA_EXPR, strBuff.toString());
        query = query.replaceAll(STR_T1_ID_DEAL, STR_X1_PRODUCTO);
        logger.info("Consulta despues de actualizaciones [" + query + "]");
        // RMC: se agrega una vez mas como parametro el lote de carga
        getJdbcTemplate().update(query,
                new Object[]{new Integer(idLoteCarga), new Integer(idLoteCarga), new Integer(idLoteCarga)});
        logger.info("Terminan actualizaci\u00f3n registros Deal");
        logger.info("Comenzado con registros Utilidades");
        query = UPD_GENPOL_XS_TEXTOPOS_UTILIDADES;
        getJdbcTemplate().update(query,
                new Object[]{new Integer(idLoteCarga)});
        logger.info("Terminan actualizaci\u00f3n registros Utilidades");
        logger.info("Terminan actualizaci\u00f3n de TEXTOPOS");
    }
    
    /**
     * Manda el objeto al log de expresi&oacute;n contable y llama al log4j.
     *
     * @param a el objeto a loggear.
     */
    public void warn(Object a) {
        LogExpresionContable log = getLogExpresionContable();
        
        log.agregarALogContabilidad(a.toString());
        if (logger.isWarnEnabled()) {
            logger.warn(a);
        }
    }

    /**
     * Regresa el valor de logExpresionContable.
     *
     * @return LogExpresionContable.
     */
    public LogExpresionContable getLogExpresionContable() {
        return logExpresionContable;
    }

    /**
     * Establece el valor de logExpresionContable.
     *
     * @param logExpresionContable El valor a asignar.
     */
    public void setLogExpresionContable(LogExpresionContable logExpresionContable) {
        this.logExpresionContable = logExpresionContable;
    }

    /**
     * Bean que mantiene el log de operaciones de contabilidad.
     */
    private LogExpresionContable logExpresionContable;
    
    /**
     * Query que actualiza la bit/aacute;cora en reproceso.
     */
    private static final String UPD_REPROCESO_SAP_BITACORA_XS = "UPDATE SAP_A_BITACORA_XS SET " +
            " FEC_PROCESO = SYSDATE, FEC_GENERACION = ?, ESTATUS = 'I' WHERE ID_CARGA = ?";

    /**
     * Variable que contiene el query para seleccionar el ID de la carga.
     */
	private static final String SEL_ID_LOTE_CARGA_SAP_BITACORA_XS = "SELECT ID_CARGA " +
		" FROM SAP_A_BITACORA_XS WHERE TRUNC(FEC_GENERACION) = TRUNC(?)";

	/**
	 * Variable del query que borra las polizas de SAP
	 */
	private static final String DEL_SAP_GELPOL_XS_BY_FEC = "DELETE SAP_A_GENPOL_XS " +
            "WHERE LOTE_CARGA = ?";

	/**
	 * Variable del query que inserta el registro en la bit&aacute;cora.
	 */
	private static final String INS_INICIO_BITACORA_XS = "" +
		"INSERT INTO SAP_A_BITACORA_XS " +
		"(FEC_DOCUMENTO, FEC_GENERACION,  APLICACION, " +
		"OPERACION, FUENTE, ID_CARGA, ESTATUS) " +
		"VALUES (?, ?, 'XS', 'N', 'SICA', ?, 'P')";

	/**
	 * Variable del query para actualizar la bit/aacute;cora.
	 */
	private static final String UPD_FINALIZA_BITACORA_XS = "" +
		"UPDATE SAP_A_BITACORA_XS " +
		"SET TOT_CARGOS = ?, " +
        "ESTATUS = 'I', " +
		"TOT_ABONOS = ?, " +
		"TOT_REG_CARGOS = ?, " +
		"TOT_REG_ABONOS = ?, " +
		"REGAPROCESAR = ? " +
		"WHERE ID_CARGA = ?";

	/**
	 * Variable del query para obtener los cargos.
     */
    private static final String GENPOL_XS_CARGOS_QUERY =
            "SELECT SUM(IMPORTE) TOTALCARGOS, COUNT(ID_REGISTRO) TOTALREGISTROSCARGOS " +
            "FROM SAP_A_GENPOL_XS WHERE CLAVECONTA = 40 AND LOTE_CARGA = ?";

    /**
     * Varibale del query para obtener los abonos.
     */
    private static final String GENPOL_XS_ABONOS_QUERY =
            "SELECT SUM(IMPORTE) TOTALABONOS, COUNT(ID_REGISTRO) TOTALREGISTROSABONOS " +
            "FROM SAP_A_GENPOL_XS WHERE CLAVECONTA = 50 AND LOTE_CARGA = ?";

    /**
     * Query que inserta las polizas a SAP.
	 */
	private static final String IN_A_GENPOL_XS = "INSERT INTO SAP_A_GENPOL_XS " +
            " (ID_REGISTRO, FECHADOC, FECHACONT, CLASEDOC, SOCIEDAD, MONEDA, TEXTOCAB, " +
            "  CLAVECONTA, CUENTACONTA , IMPORTE, USUARIO, IND_ORIGEN, FECHA_PROCESO, STATUS, " +
            "  LOTE_CARGA, PRODUCTO, REFERENCIA, TEXTOPOS @) " +
            " VALUES (SAP_GENPOL_XS_SEQ.NEXTVAL, ?, ? , 'XS', '1101', ?, ?, ?, ?, TO_NUMBER(?), " +
            " 'SICA_APPS', 'AO', SYSDATE, 'I', ?, ?, ?, ? $)";

	/**
	 * Query que actualiza las referencias vs. SC_POLIZA
	 */
	private static final String UPD_SC_POLIZA_SAP_REFERENCIA =
		"UPDATE SC_POLIZA sp " +
		"SET SAP_REFERENCIA = ( " +
		"     SELECT DISTINCT xs.REFERENCIA " +
		"     FROM SAP_A_GENPOL_XS xs " +
		"     WHERE " +
		"     xs.PRODUCTO = sp.ID_DEAL AND" +
		"     xs.MONEDA = sp.ID_DIVISA AND " +
		"     TRUNC(FECHACONT) = TRUNC(?)) WHERE TRUNC(sp.FECHA_CREACION) = TRUNC(?) @";

    /**
     * Query actualiza TEXTOPOS de los Deals normales.
     */
    private static final String UPD_GENPOL_XS_TEXTOPOS_DEALS = "" +
            "UPDATE SAP_A_GENPOL_XS xs SET TEXTOPOS = " +
            "	(SELECT DISTINCT SUBSTR(xs.PRODUCTO||' ' ||bp.NOMBRE_CORTO, 1, 50) " +
            "	FROM SC_DEAL sd, BUP_PERSONA_CUENTA_ROL br, BUP_PERSONA bp " +
            "	WHERE xs.PRODUCTO = sd.ID_DEAL AND " +
            "	  sd.NO_CUENTA = br.NO_CUENTA AND br.ID_ROL = 'TIT' AND " +
            "     bp.ID_PERSONA = br.ID_PERSONA AND LOTE_CARGA = ?) " + 
            " WHERE xs.ID_REGISTRO IN (SELECT DISTINCT ID_REGISTRO " +
            			"	FROM SAP_A_GENPOL_XS t1, SC_DEAL t2 " +
            			"	WHERE t1.PRODUCTO = t2.ID_DEAL AND t1.LOTE_CARGA = ?) " +
            "AND LOTE_CARGA = ? @";

    /**
     * Query que actualiza TEXTOPOS de los registros de utilidades.
     */
    private static final String UPD_GENPOL_XS_TEXTOPOS_UTILIDADES = "UPDATE SAP_A_GENPOL_XS xs " +
            " SET TEXTOPOS = SUBSTR('UTILIDAD/P\u00c9RDIDA '||xs.MONEDA, 1, 50) " +
            " WHERE xs.LOTE_CARGA = ? AND xs.TEXTOPOS LIKE 'U%'";

    /**
     * Query que obtiene los deals que tienen detalles duplicados.
     */
    private static final String QRY_DEAL_DETALLE_DUPLICADO = "SELECT ID_DEAL, FOLIO_DETALLE " +
    		"FROM ( " +
    		"		SELECT ID_DEAL, FOLIO_DETALLE, COUNT(FOLIO_DETALLE) COUNTFOLIODETALLE " +
    		"		FROM SC_DEAL_DETALLE " +
    		"		WHERE ID_DEAL IN ( " +
    		"				SELECT ID_DEAL " +
    		"				FROM SC_POLIZA " +
    		"				WHERE TRUNC(FECHA_CREACION) = TRUNC(?) " +
    		"			) " +
    		"		GROUP BY FOLIO_DETALLE, ID_DEAL " +
    		"	) " +
    		"WHERE COUNTFOLIODETALLE > 1";

    /**
     * Query que actualiza los Deal(ENCABEZADO) a liquidados que est&aacute;n en el SITE pero no en
     * SICA
     */
    private static final String UPD_SC_DEAL_STATUS_TL = "UPDATE SC_DEAL SET STATUS_DEAL = 'TL' " +
            "WHERE ID_DEAL IN (" +
            "  SELECT d.ID_DEAL FROM TES_LIQUIDACION l, SC_DEAL d " +
            "  WHERE l.NUMERO_ORDEN = d.ID_DEAL AND l.STATUS = 'LIQD' AND d.STATUS_DEAL != 'TL' " +
            "    AND d.REVERSADO = 0 AND D.STATUS_DEAL != 'CA')";

    /**
     * Query que actualiza los Deal(Detalles) a liquidados que est&aacute;n en el SITE pero no en
     * SICA
     */
    private static final String UPD_SC_DEAL_DETALLE_STATUS_TT = "UPDATE SC_DEAL_DETALLE " +
            "SET STATUS_DETALLE_DEAL = 'TT' " +
            "WHERE ID_DEAL_POSICION IN ( " +
            "    SELECT dd.ID_DEAL_POSICION " +
            "    FROM TES_DETALLE_LIQUIDACION ld, SC_DEAL_DETALLE dd " +
            "    WHERE ld.ID_DETALLE_EXT = dd.ID_DEAL_POSICION " +
            "        AND ld.STATUS = 'LIQD' AND dd.STATUS_DETALLE_DEAL != 'TT' " +
            "        AND dd.REVERSADO = 0 AND dd.STATUS_DETALLE_DEAL != 'CA' " +
            ")";

    /**
     * Query que obtiene los registros para las polizas de SAP
     */
    private static final String SEL_GENPOL_XS = "SELECT " +
            " CASE WHEN t6.GRUPO IS NULL THEN t1.ID_DIVISA " +
            "   ELSE t6.GRUPO " +
            " END MONEDA, " +
            " CASE WHEN t1.CARGO_ABONO = 'ABONO' " +
            "     THEN CONCAT(CONCAT(t1.ID_DEAL, 'R'), t5.FOLIO_DETALLE) " +
            "   ELSE CONCAT(CONCAT(t1.ID_DEAL, 'E'), t5.FOLIO_DETALLE) " +
            " END TEXTOPOS, " +
            " CONCAT('BATCHXS', t1.ID_DIVISA) TEXTOCAB, " +
            " CASE WHEN t1.CARGO_ABONO = 'CARGO' THEN  40 WHEN t1.CARGO_ABONO = 'ABONO' THEN 50 " +
            " END CLAVECONTA, " +
            " t1.CUENTA_CONTABLE CUENTACONTA, " +
            " ROUND(SUM(t1.IMPORTE), 2) IMPORTE, " +
            " CASE WHEN t1.CUENTA_CONTABLE LIKE '6%' AND t1.ENTIDAD = '0000-00000' THEN " +
            "     '13750001' " +
            "   WHEN t1.CUENTA_CONTABLE LIKE '6%' THEN t1.ENTIDAD " +
            "   ELSE NULL END CENTROCOSTOS, " +
            " CASE WHEN t2.NO_CUENTA IN (@@) THEN '54250002' " +
            "   WHEN t1.CUENTA_CONTABLE LIKE '5%' AND t1.ENTIDAD = '0000-00000' THEN '13750001' " +
            "   WHEN t1.CUENTA_CONTABLE LIKE '5%' THEN t1.ENTIDAD " +
            "   ELSE NULL END CENTROBENEF, " +
            " CASE WHEN t1.CUENTA_CONTABLE LIKE '5%' AND t4.CLAVE_TIPO_IVA  = 'NOR' THEN  'A2' " +
            "   WHEN t1.CUENTA_CONTABLE LIKE '5%' AND t4.CLAVE_TIPO_IVA  = 'FRT' THEN  'A1' " +
            "   WHEN t1.CUENTA_CONTABLE LIKE '6%' AND t4.CLAVE_TIPO_IVA  = 'NOR' THEN  'V2' " +
            "   WHEN t1.CUENTA_CONTABLE LIKE '6%' AND t4.CLAVE_TIPO_IVA  = 'FRT' THEN  'V1' " +
            "   WHEN t1.CUENTA_CONTABLE = '2401090400' AND t4.CLAVE_TIPO_IVA  = 'NOR' THEN  'A2' " +
            "   WHEN t1.CUENTA_CONTABLE = '2401090400' AND t4.CLAVE_TIPO_IVA  = 'FRT' THEN  'A1' " +
            "   ELSE NULL " +
            " END INDIMPUESTO, " +
            " CASE WHEN t1.CUENTA_CONTABLE LIKE '6%' OR t1.CUENTA_CONTABLE LIKE '5%' OR " +
            "       t1.CUENTA_CONTABLE = '2401090400' " +
            "     THEN ROUND(((SUM(t1.IMPORTE) * ((t4.PORC_IVA)/100)) / " +
            "       (1 + (t4.PORC_IVA/100))), 2) " +
            "   ELSE NULL END IMPORTEBASEIMPUESTO, " +
            " CASE WHEN t1.ID_DIVISA != 'MXN' THEN ROUND(SUM(t1.IMPORTE * t1.TIPO_CAMBIO), 2) " +
            "	ELSE NULL END IMPMONLOCAL, " +
            " t1.id_deal PRODUCTO, " +
            " t1.ENTIDAD ENTIDAD, " +
            " t1.CLAVE_REFERENCIA ASIGNACION " +
            " FROM SC_POLIZA t1, SC_DEAL t2, SC_CANAL t3, BUP_TIPO_IVA t4, SC_DEAL_DETALLE t5, " +
            "   SC_DIVISA t6 " +
            " WHERE t1.ID_DEAL = t2.ID_DEAL AND t2.ID_CANAL = t3.ID_CANAL AND " +
            "   t2.ID_DEAL = t5.ID_DEAL AND t3.CLAVE_TIPO_IVA = t4.CLAVE_TIPO_IVA AND " +
            "   t1.FOLIO_DETALLE = t5.FOLIO_DETALLE AND t1.ID_DIVISA = t6.ID_DIVISA AND " +
            "   TRUNC(t1.FECHA_CREACION) = TRUNC(?) @ " +
            " GROUP BY t1.ID_DIVISA, t1.CARGO_ABONO, t1.CUENTA_CONTABLE, t1.ENTIDAD, " +
            "   t1.ID_DEAL, t5.FOLIO_DETALLE, t4.CLAVE_TIPO_IVA, t4.PORC_IVA, t6.GRUPO, " +
            "   t2.NO_CUENTA, t1.CLAVE_REFERENCIA " +
            " UNION " +
            " SELECT CASE WHEN t3.GRUPO IS NULL THEN t1.ID_DIVISA ELSE t3.GRUPO END MONEDA, " +
            "   CASE WHEN t1.CARGO_ABONO = 'ABONO' THEN " +
            "       CONCAT(CONCAT('UR', t1.ID_DEAL), t1.FOLIO_DETALLE) " +
            "     ELSE CONCAT(CONCAT('UE', t1.ID_DEAL), t1.FOLIO_DETALLE) " +
            "   END TEXTOPOS, " +
            "   CONCAT('BATCHXS', t1.ID_DIVISA) TEXTOCAB, " +
            "   CASE WHEN t1.CARGO_ABONO = 'CARGO' THEN 40 " +
            "     WHEN t1.CARGO_ABONO = 'ABONO' THEN 50 " +
            "   END CLAVECONTA, " +
            "    t1.CUENTA_CONTABLE CUENTACONTA, " +
            "    0 IMPORTE, " +
            "    CASE WHEN t1.CUENTA_CONTABLE LIKE '6%' AND " +
            "        t1.ENTIDAD = '0000-00000' THEN '13750001' " +
            "      WHEN t1.CUENTA_CONTABLE LIKE '6%' THEN t1.ENTIDAD " +
            "      ELSE NULL END CENTROCOSTOS, " +
            "    CASE WHEN t1.CUENTA_CONTABLE LIKE '5%' AND t1.ENTIDAD = '0000-00000' " +
            "        THEN '13750001' " +
            "      WHEN t1.CUENTA_CONTABLE LIKE '5%' THEN t1.ENTIDAD " +
            "      ELSE NULL " +
            "    END CENTROBENEF, " +
            "    NULL INDIMPUESTO, " +
            "    NULL IMPORTEBASEIMPUESTO, " +
            "    ROUND(SUM(t1.IMPORTE), 2) IMPMONLOCAL, " +
            "    t1.id_deal PRODUCTO, " +
            "	 t1.ENTIDAD ENTIDAD, " +
            "    t1.CLAVE_REFERENCIA ASIGNACION " +
            " FROM SC_POLIZA t1, SC_RECO_UTILIDAD t2, SC_DIVISA t3 " +
            " WHERE t1.ID_DEAL = t2.ID_DEAL_POSICION AND t1.ID_DIVISA = t3.ID_DIVISA AND " +
            "   TRUNC(t2.fecha_operacion) = TRUNC(?) @ " +
            " GROUP BY t1.ID_DIVISA, t1.CARGO_ABONO, t1.CUENTA_CONTABLE, t1.ENTIDAD, t1.ID_DEAL, " +
            "   t1.FOLIO_DETALLE, t3.GRUPO, t1.CLAVE_REFERENCIA ";

    /**
     * Constante para la cadena "CENTROBENEF".
     */
    private static final String CENTROBENEF = "CENTROBENEF";

    /**
     * Constante para la cadena "CENTROCOSTOS".
     */
    private static final String CENTROCOSTOS = "CENTROCOSTOS";

    /**
     * Constante para la cadena "INDIMPUESTO".
     */
    private static final String INDIMPUESTO = "INDIMPUESTO";

    /**
     * Constante para la cadena "IMPMONLOCAL".
     */
    private static final String IMPMONLOCAL = "IMPMONLOCAL";

    /**
     * Constante para la cadena "IMPORTEBASEIMPUESTO".
     */
    private static final String IMPORTEBASEIMPUESTO = "IMPORTEBASEIMPUESTO";

    /**
     * Constante para la cadena "MONEDA".
     */
    private static final String MONEDA = "MONEDA";

    /**
     * Constante para la cadena "QUERY".
     */
    private static final String QUERY = "QUERY";

    /**
     * Constante para la cadena "REFERENCIA".
     */
    private static final String REFERENCIA = "REFERENCIA";

    /**
     * Constante para la cadena "TEXTOCAB".
     */
    private static final String TEXTOCAB = "TEXTOCAB";

    /**
     * Constante para la cadena "TOTALABONOS".
     */
    private static final String TOTALABONOS = "TOTALABONOS";

    /**
     * Constante para la cadena "TOTALCARGOS".
     */
    private static final String TOTALCARGOS = "TOTALCARGOS";

    /**
     * Constante para la cadena "TOTALREGISTROSABONOS".
     */
    private static final String TOTALREGISTROSABONOS = "TOTALREGISTROSABONOS";

    /**
     * Constante para la cadena "TOTALREGISTROSCARGOS".
     */
    private static final String TOTALREGISTROSCARGOS = "TOTALREGISTROSCARGOS";

    /**
     * Constante para la cadena "ASIGNACION".
     */
    private static final String ASIGNACION = "ASIGNACION";

    /**
     * Constante para la cadena ') '.
     */
    private static final String STR_CIERRE_PARENT = ") ";

    /**
     * Constante para la cadena ', '.
     */
    private static final String STR_COMA = ", ";

    /**
     * Constante para la cadena ", '".
     */
    private static final String STR_COMA_COMILLA = ", '";

    /**
     * Constante para la cadena "'".
     */
    private static final String STR_COMILLA = "'";

    /**
     * Constante para la cadena 't1.ID_DEAL'.
     */
    private static final String STR_T1_ID_DEAL = "t1.ID_DEAL";

    /**
     * Constante para la cadena 'xs.PRODUCTO'.
     */
    private static final String STR_X1_PRODUCTO = "xs.PRODUCTO";

    /**
     * Constante para la cadena '\\@'.
     */
    private static final String STR_ARROBA_EXPR = "\\@";
 }

