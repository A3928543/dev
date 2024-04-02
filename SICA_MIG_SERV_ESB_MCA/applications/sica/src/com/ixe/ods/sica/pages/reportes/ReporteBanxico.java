/*
 * $Id: ReporteBanxico.java,v 1.41.26.2 2011/05/03 00:44:29 galiciad Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 - 2009 LegoSoft S.C.
 */
package com.ixe.ods.sica.pages.reportes;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.tapestry.IRequestCycle;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.util.JRLoader;

import com.ixe.isis.tapestry.IxenetValidationDelegate;
import com.ixe.ods.bup.model.ContratoSica;
import com.ixe.ods.jasper.ListDataSource;
import com.ixe.ods.sica.Constantes;
import com.ixe.ods.sica.Redondeador;
import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.dao.BanxicoDao;
import com.ixe.ods.sica.model.ContratoCliente;
import com.ixe.ods.sica.model.DealDetalle;
import com.ixe.ods.sica.model.Divisa;
import com.ixe.ods.sica.model.Estado;
import com.ixe.ods.sica.model.FactorDivisaActual;
import com.ixe.ods.sica.model.HistoricoPosicion;
import com.ixe.ods.sica.model.ParametroSica;
import com.ixe.ods.sica.model.Posicion;
import com.ixe.ods.sica.model.PrecioReferenciaActual;
import com.ixe.ods.sica.model.TcMinMaxTeller;
import com.ixe.ods.sica.pages.SicaPage;
import com.ixe.ods.sica.util.GeneradorTxt;
import com.ixe.ods.sica.utils.DateUtils;
import com.ixe.ods.tapestry.BaseGlobal;
import com.ixe.ods.tapestry.services.jasper.DataSourceProvider;

/**
 * P&aacute;gina que permite al usuario imprimir los reportes de Banxico: <br>
 * <li>Reporte Sumarizado de Operaciones de Compra y Venta sin Swaps formato PDF</li>
 * <li>Reporte Anal&iacute;tico de Operaciones de Compra y Venta sin Swaps formato PDF</li>
 * <li>Reporte Anal&iacute;tico de Operaciones de Compra y Venta con Swaps formato PDF</li>
 * <li>Reporte Anal&iacute;tico de Operaciones de Compra y Venta con Swaps formato Xls</li>
 * <li>Reporte Anal&iacute;tico Seccion VIII del ACLME formato Xls</li> <br>
 * <p/>
 * Se excluyen los detalles que tienen como divisa Pesos.
 *
 * @author Gustavo Gonzalez
 * @version $Revision: 1.41.26.2 $ $Date: 2011/05/03 00:44:29 $
 */
public abstract class ReporteBanxico extends SicaPage implements DataSourceProvider {

	/**
	 * Asigna los valores necesarios para la pagina.
     *
     * @param cycle El ciclo de la p&aacute;gina.
	 */
	public void activate(IRequestCycle cycle) {
		super.activate(cycle);
		setNombreArchivo("ReporteACLMEX");
		asignarConceptos();
	}
	
	/**
	 * Obtiene el DataSource para el reporte Sumarizado de 
	 * Operaciones de Compra y Venta sin Swaps. (Reporte 2)
	 *
	 * @param id El identificador.
	 * @return JRDataSource.
	 * @see com.ixe.ods.tapestry.services.jasper.DataSourceProvider#getDataSource(String id)
	 */
	public JRDataSource getDataSourceInforme(String id) {
		Date inicioDia  = DateUtils.inicioDia(getRegisterDate()); 
		Date finDia  = DateUtils.finDia(getRegisterDate());
		Calendar cal = new GregorianCalendar();
		cal.setTime(finDia);
		cal.add(Calendar.DAY_OF_YEAR, 1);
		java.sql.Date inicioDiaSql =  new java.sql.Date(inicioDia.getTime());
		java.sql.Date finDiaSql =  new java.sql.Date(cal.getTime().getTime());
		BanxicoDao banxicoDao = (BanxicoDao) getApplicationContext().getBean("banxicoDao");
		List detalles = banxicoDao.findDetallesIncluyeStatusAlta(inicioDiaSql, finDiaSql);
		ArrayList posicionUSD = getPosicionPorDivisa(Divisa.DOLAR);
		ArrayList posicionEUR = getPosicionPorDivisa(Divisa.EURO);
		ArrayList posicionCAD = getPosicionPorDivisa(Divisa.DOLAR_CANADIENSE);
		ArrayList posicionGBP = getPosicionPorDivisa(Divisa.LIBRA_ESTERLINA);
		ArrayList posicionCHF = getPosicionPorDivisa(Divisa.FRANCO_SUIZO);
		ArrayList posicionJPY = getPosicionPorDivisa(Divisa.YEN);
		Double [] resmercaUSD = getMontosResmerca(banxicoDao, inicioDiaSql, finDiaSql, 
				Divisa.DOLAR, SECTOR_CASAS_DE_BOLSA, SECTOR_EMPRESAS, SECTOR_OTR_INTERM);
		Double [] resmercaEUR = getMontosResmerca(banxicoDao, inicioDiaSql, finDiaSql, 
				Divisa.EURO, SECTOR_CASAS_DE_BOLSA, SECTOR_EMPRESAS, SECTOR_OTR_INTERM);
		Double [] resmercaCAD = getMontosResmerca(banxicoDao, inicioDiaSql, finDiaSql, 
				Divisa.DOLAR_CANADIENSE, SECTOR_CASAS_DE_BOLSA, SECTOR_EMPRESAS, SECTOR_OTR_INTERM);
		Double [] resmercaGBP = getMontosResmerca(banxicoDao, inicioDiaSql, finDiaSql, 
				Divisa.LIBRA_ESTERLINA, SECTOR_CASAS_DE_BOLSA, SECTOR_EMPRESAS, SECTOR_OTR_INTERM);
		Double [] resmercaCHF = getMontosResmerca(banxicoDao, inicioDiaSql, finDiaSql,
				Divisa.FRANCO_SUIZO, SECTOR_CASAS_DE_BOLSA, SECTOR_EMPRESAS, SECTOR_OTR_INTERM);
		Double [] resmercaJPY = getMontosResmerca(banxicoDao, inicioDiaSql, finDiaSql, 
				Divisa.YEN, SECTOR_CASAS_DE_BOLSA, SECTOR_EMPRESAS, SECTOR_OTR_INTERM);
		List fwEstraSectoresUSD = getMontosFwEstraSectores(banxicoDao, inicioDiaSql, finDiaSql, 
				Divisa.DOLAR);
		List fwEstraSectoresEUR = getMontosFwEstraSectores(banxicoDao, inicioDiaSql, finDiaSql, 
				Divisa.EURO);
		List fwEstraSectoresCAD = getMontosFwEstraSectores(banxicoDao, inicioDiaSql, finDiaSql, 
				Divisa.DOLAR_CANADIENSE);
		List fwEstraSectoresGBP = getMontosFwEstraSectores(banxicoDao, inicioDiaSql, finDiaSql, 
				Divisa.LIBRA_ESTERLINA);
		List fwEstraSectoresCHF = getMontosFwEstraSectores(banxicoDao, inicioDiaSql, finDiaSql, 
				Divisa.FRANCO_SUIZO);
		List fwEstraSectoresJPY = getMontosFwEstraSectores(banxicoDao, inicioDiaSql, finDiaSql, 
				Divisa.YEN);
		List detallesMap = new ArrayList();
		List sectoresResmerca = new ArrayList();
		sectoresResmerca.add(new Integer(9));
		sectoresResmerca.add(new Integer(10));
		sectoresResmerca.add(new Integer(13));
		sectoresResmerca.add(new Integer(33));
		sectoresResmerca.add(new Integer(34));
		ParametroSica parametro = getSicaServiceData().findParametro(
				ParametroSica.ID_PERSONA_IXE_FORWARD);
		Integer idPersonaIxeForward = new Integer(parametro.getValor());
		for (Iterator iter = detalles.iterator(); iter.hasNext();) {
			Object [] element = (Object[]) iter.next();
			HashMap map = new HashMap();
			Double montoElement = (Double) element[7];
			double monto = montoElement.doubleValue() / 1000;
			map.put("fecha", inicioDia);
			String compraVenta  = (String) element[4];
			Boolean recibimos =  new Boolean("Venta".equals(compraVenta) ? false : true );
			boolean rec = "Venta".equals(compraVenta) ? false : true;
			map.put("isRecibimos", recibimos);
			Divisa divisa = getSicaServiceData().findDivisa((String) element[8]);
			map.put("idDivisa", divisa.getDescripcion().toUpperCase());
			String sector = (String) element[17];
			String idSwap = (String)element[20];
			Integer idPersona = (Integer)element[19];
			if (sector != null) {
				map.put("sector",sector);
			}
			else {
				map.put("sector",new String("No existe descripcion del sector."));
			}
			if (idPersonaIxeForward.intValue() == idPersona.intValue() && "0".equals(idSwap)) {
				map.put("isForward", new Boolean(true));
				map.put("montoForward", new Double(monto));
			}
			else {
				map.put("isForward", new Boolean(false));
				map.put("montoForward", new Double(0.0));
			}
			if (!"0".equals(idSwap) && idPersonaIxeForward.intValue() == idPersona.intValue()) {
				map.put("montoForwardEstra", new Double(monto));
				map.put("isForwardEstra", new Boolean(true));
			}
			else {
				map.put("montoForwardEstra", new Double(0.0));
				map.put("isForwardEstra", new Boolean(false));
			}
			String fechaValor = ((String) element[2]).trim();
			Integer idSector = (Integer)element[21];
			if (divisa.isFrecuente()) { 
				if (Divisa.DOLAR.equals(divisa.getIdDivisa())) {
					agregarMontosPosicionMapa(map, divisa.getIdDivisa(), posicionUSD);
					map.put("resmercaCompras", resmercaUSD[0]);
					map.put("remercaVentas", resmercaUSD[1]);
					map.put("resmercaComprasString", convertirFormatoDoubleString(resmercaUSD[0]));
					map.put("remercaVentasString", convertirFormatoDoubleString(resmercaUSD[1]));
					map.put("montoForwardEstraCompras", resmercaUSD[2]);
					map.put("montoForwardEstraVentas", resmercaUSD[3]);
					if (idSector.intValue() != 9 && idSector.intValue() != 33) {
						map.put("isSectorResmerca", new Boolean(true));
						if (rec){
							map.put("montoResmerca", resmercaUSD[0]);
						}
						else {
							map.put("montoResmerca", resmercaUSD[1]);
						}
					}
					else {
						map.put("isSectorResmerca", new Boolean(false));
					}
					double posicionResmerca = resmercaUSD[4].doubleValue() - 
					resmercaUSD[5].doubleValue();
					double posicionFinalResmerca = posicionResmerca + ((Double)map.
							get("posicionInicial")).doubleValue();
					map.put("posicionFinalResmerca",new Double(posicionFinalResmerca));
					asignaMontosFwEstraSectores(map, fwEstraSectoresUSD, sector, rec);
				}
				else {
					if (divisa.getIdDivisa().equals(Divisa.EURO)) {
						agregarMontosPosicionMapa(map, divisa.getIdDivisa(), posicionEUR);
						map.put("resmercaCompras", resmercaEUR[0]);
						map.put("remercaVentas", resmercaEUR[1]);
						map.put("resmercaComprasString", convertirFormatoDoubleString(
								resmercaEUR[0]));
						map.put("remercaVentasString", convertirFormatoDoubleString(
								resmercaEUR[1]));
						map.put("montoForwardEstraCompras", resmercaEUR[2]);
						map.put("montoForwardEstraVentas", resmercaEUR[3]);
						if (idSector.intValue() != 9 && idSector.intValue() != 33) {
							map.put("isSectorResmerca", new Boolean(true));
							if (rec){
								map.put("montoResmerca", resmercaEUR[0]);
							}
							else {
								map.put("montoResmerca", resmercaEUR[1]);
							}
						}
						else {
							map.put("isSectorResmerca", new Boolean(false));
						}
						double posicionResmerca = resmercaEUR[4].doubleValue() - 
						resmercaEUR[5].doubleValue();
						double posicionFinalResmerca = posicionResmerca + ((Double)map.
								get("posicionInicial")).doubleValue();
						map.put("posicionFinalResmerca",new Double(posicionFinalResmerca));
						asignaMontosFwEstraSectores(map, fwEstraSectoresEUR, sector, rec);
					}
					else if (divisa.getIdDivisa().equals(Divisa.DOLAR_CANADIENSE)) {
						agregarMontosPosicionMapa(map, divisa.getIdDivisa(), posicionCAD);
						map.put("resmercaCompras", resmercaCAD[0]);
						map.put("remercaVentas", resmercaCAD[1]);
						map.put("resmercaComprasString", convertirFormatoDoubleString(
								resmercaCAD[0]));
						map.put("remercaVentasString", convertirFormatoDoubleString(
								resmercaCAD[1]));
						map.put("montoForwardEstraCompras", resmercaCAD[2]);
						map.put("montoForwardEstraVentas", resmercaCAD[3]);
						if (idSector.intValue() != 9 && idSector.intValue() != 33) {
							map.put("isSectorResmerca", new Boolean(true));
							if (rec){
								map.put("montoResmerca", resmercaCAD[0]);
							}
							else {
								map.put("montoResmerca", resmercaCAD[1]);
							}
						}
						else {
							map.put("isSectorResmerca", new Boolean(false));
						}
						double posicionResmerca = resmercaCAD[4].doubleValue() - 
						resmercaCAD[5].doubleValue();
						double posicionFinalResmerca = posicionResmerca + ((Double)map.
								get("posicionInicial")).doubleValue();
						map.put("posicionFinalResmerca",new Double(posicionFinalResmerca));
						asignaMontosFwEstraSectores(map, fwEstraSectoresCAD, sector, rec);
					}
					else if (divisa.getIdDivisa().equals(Divisa.LIBRA_ESTERLINA)) {
						agregarMontosPosicionMapa(map, divisa.getIdDivisa(), posicionGBP);
						map.put("resmercaCompras", resmercaGBP[0]);
						map.put("remercaVentas", resmercaGBP[1]);
						map.put("resmercaComprasString", convertirFormatoDoubleString(
								resmercaGBP[0]));
						map.put("remercaVentasString", convertirFormatoDoubleString(
								resmercaGBP[1]));
						map.put("montoForwardEstraCompras", resmercaGBP[2]);
						map.put("montoForwardEstraVentas", resmercaGBP[3]);
						if (idSector.intValue() != 9 && idSector.intValue() != 33) {
							map.put("isSectorResmerca", new Boolean(true));
							if (rec){
								map.put("montoResmerca", resmercaGBP[0]);
							}
							else {
								map.put("montoResmerca", resmercaGBP[1]);
							}
						}
						else {
							map.put("isSectorResmerca", new Boolean(false));
						}
						double posicionResmerca = resmercaGBP[4].doubleValue() - 
						resmercaGBP[5].doubleValue();
						double posicionFinalResmerca = posicionResmerca + ((Double)map.
								get("posicionInicial")).doubleValue();
						map.put("posicionFinalResmerca",new Double(posicionFinalResmerca));
						asignaMontosFwEstraSectores(map, fwEstraSectoresGBP, sector, rec);
					}
					else if (divisa.getIdDivisa().equals(Divisa.FRANCO_SUIZO)) {
						agregarMontosPosicionMapa(map, divisa.getIdDivisa(), posicionCHF);
						map.put("resmercaCompras", resmercaCHF[0]);
						map.put("remercaVentas", resmercaCHF[1]);
						map.put("resmercaComprasString", convertirFormatoDoubleString(
								resmercaCHF[0]));
						map.put("remercaVentasString", convertirFormatoDoubleString(
								resmercaCHF[1]));
						map.put("montoForwardEstraCompras", resmercaCHF[2]);
						map.put("montoForwardEstraVentas", resmercaCHF[3]);
						if (idSector.intValue() != 9 && idSector.intValue() != 33) {
							map.put("isSectorResmerca", new Boolean(true));
							if (rec){
								map.put("montoResmerca", resmercaCHF[0]);
							}
							else {
								map.put("montoResmerca", resmercaCHF[1]);
							}
						}
						else {
							map.put("isSectorResmerca", new Boolean(false));
						}
						double posicionResmerca = resmercaCHF[4].doubleValue() - 
						resmercaCHF[5].doubleValue();
						double posicionFinalResmerca = posicionResmerca + ((Double)map.
								get("posicionInicial")).doubleValue();
						map.put("posicionFinalResmerca",new Double(posicionFinalResmerca));
						asignaMontosFwEstraSectores(map, fwEstraSectoresCHF, sector, rec);
					}
					else if (divisa.getIdDivisa().equals(Divisa.YEN)) {
						agregarMontosPosicionMapa(map, divisa.getIdDivisa(), posicionJPY);
						map.put("resmercaCompras", resmercaJPY[0]);
						map.put("remercaVentas", resmercaJPY[1]);
						map.put("resmercaComprasString", convertirFormatoDoubleString(
								resmercaJPY[0]));
						map.put("remercaVentasString", convertirFormatoDoubleString(
								resmercaJPY[1]));
						map.put("montoForwardEstraCompras", resmercaJPY[2]);
						map.put("montoForwardEstraVentas", resmercaJPY[3]);
						if (idSector.intValue() != 9 && idSector.intValue() != 33) {
							map.put("isSectorResmerca", new Boolean(true));
							if (rec){
								map.put("montoResmerca", resmercaJPY[0]);
							}
							else {
								map.put("montoResmerca", resmercaJPY[1]);
							}
						}
						else {
							map.put("isSectorResmerca", new Boolean(false));
						}
						double posicionResmerca = resmercaJPY[4].doubleValue() - resmercaJPY[5].
						doubleValue();
						double posicionFinalResmerca = posicionResmerca + ((Double)map.
								get("posicionInicial")).doubleValue();
						map.put("posicionFinalResmerca",new Double(posicionFinalResmerca));
						asignaMontosFwEstraSectores(map, fwEstraSectoresJPY, sector, rec);
					}
				}
			}
			else {
				Double [] resmercaNoFrecuente = getMontosResmerca(banxicoDao, inicioDiaSql, 
						finDiaSql, divisa.getIdDivisa(), SECTOR_CASAS_DE_BOLSA, SECTOR_EMPRESAS, 
						SECTOR_OTR_INTERM);
				asignarMontosPosIniFinTipoCambioDivisaNoFrecuente(map, divisa.getIdDivisa());
				map.put("resmercaCompras", resmercaNoFrecuente[0]);
				map.put("remercaVentas", resmercaNoFrecuente[1]);
				map.put("resmercaComprasString", convertirFormatoDoubleString(
						resmercaNoFrecuente[0]));
				map.put("remercaVentasString", convertirFormatoDoubleString(
						resmercaNoFrecuente[1]));
				map.put("montoForwardEstraCompras", resmercaNoFrecuente[2]);
				map.put("montoForwardEstraVentas", resmercaNoFrecuente[3]);
				if (idSector.intValue() != 9 && idSector.intValue() != 33) {
					map.put("isSectorResmerca", new Boolean(true));
					if (rec){
						map.put("montoResmerca", resmercaNoFrecuente[0]);
					}
					else {
						map.put("montoResmerca", resmercaNoFrecuente[1]);
					}
				}
				else {
					map.put("isSectorResmerca", new Boolean(false));
				}
				double posicionResmerca = resmercaNoFrecuente[4].doubleValue() - 
				resmercaNoFrecuente[5].doubleValue();
				double posicionFinalResmerca = posicionResmerca + ((Double)map.
						get("posicionInicial")).doubleValue();
				map.put("posicionFinalResmerca",new Double(posicionFinalResmerca));
				List fwEstraSectoresNofrec = getMontosFwEstraSectores(banxicoDao, inicioDiaSql, 
						finDiaSql,divisa.getIdDivisa());
				asignaMontosFwEstraSectores(map, fwEstraSectoresNofrec, sector, rec);
			}
			if (fechaValor.equals(Constantes.CASH)) {
				map.put("sectorCash", new Double(monto));
				map.put("sectorTom", new Double(0.00));
				map.put("sectorSpot", new Double(0.00));
				map.put("sector72Hr", new Double(0.00));
				map.put("sectorVFut", new Double(0.00));
			}
			else if (fechaValor.equals(Constantes.TOM)) {
				map.put("sectorTom", new Double(monto));
				map.put("sectorCash", new Double(0.0));
				map.put("sectorSpot", new Double(0.00));
				map.put("sector72Hr", new Double(0.00));
				map.put("sectorVFut", new Double(0.00));
			}
			else if (fechaValor.equals(Constantes.SPOT)) {
				map.put("sectorSpot", new Double(monto));
				map.put("sectorCash", new Double(0.0));
				map.put("sectorTom", new Double(0.00));
				map.put("sector72Hr", new Double(0.00));
				map.put("sectorVFut", new Double(0.00));
			}
			else if (fechaValor.equals(Constantes.HR72)) {
				map.put("sector72Hr", new Double(monto));
				map.put("sectorCash", new Double(0.0));
				map.put("sectorTom", new Double(0.00));
				map.put("sectorSpot", new Double(0.00));
				map.put("sectorVFut", new Double(0.00));
			}
			else if (fechaValor.equals(Constantes.VFUT)) {
				map.put("sectorVFut", new Double(monto));
				map.put("sectorCash", new Double(0.0));
				map.put("sectorTom", new Double(0.00));
				map.put("sectorSpot", new Double(0.00));
				map.put("sector72Hr", new Double(0.00));
			}
			double montoSwap = 0;
			String tipoSwap =  (String) element[25];
			if (!"0".equals(idSwap) && "S".equals(tipoSwap)) {
				if (Divisa.DOLAR.equals(divisa.getIdDivisa())){
					montoSwap =  monto;
				}
				else {
					montoSwap =  monto;
				}
				map.put("swap", new Double(monto));
			}
			else {
				map.put("swap", new Double(0.0));
			}
			detallesMap.add(map);
		}
		return new ListDataSource(detallesMap);
	}

	/**
	 * Obtiene los montos totales para los sectores del Resmerca del reporte de Banxico ademas de los
	 * montos para la seccion de RESMERCA.
	 * 
	 * @param banxicoDao El dao que obtiene los montos para el Resmerca por divisa.
	 * @param inicioDiaSql La fecha de inicio de la busqueda.
	 * @param finDiaSql La fecha de fin de la busqueda.
	 * @param idDivisa La divisa correspondiente.
	 * @param sectorCasasDeBolsa La descripcion del sector economico para Casas de Bolsa.
	 * @param sectorEmpresas La descripcion del sector economico para Empresas.
	 * @param sectorOtrosInterm La descripcion del sector economico para Otros Intermediarios.
	 * @return
	 */
	public Double[] getMontosResmerca(BanxicoDao banxicoDao, java.sql.Date inicioDiaSql, 
			java.sql.Date finDiaSql,
			String idDivisa, String sectorCasasDeBolsa, String sectorEmpresas, String sectorOtrosInterm){
		ParametroSica parametro = getSicaServiceData().findParametro(
				ParametroSica.ID_PERSONA_IXE_FORWARD);
		Integer idPersonaIxeForward = new Integer(parametro.getValor());
		Object [] registroCompra = (Object[]) banxicoDao.findMontoTotalResmerca(inicioDiaSql, 
				finDiaSql, idPersonaIxeForward, idDivisa, sectorCasasDeBolsa, sectorEmpresas, 
				sectorOtrosInterm, "S").get(0);
		Object [] registroVenta = (Object[]) banxicoDao.findMontoTotalResmerca(inicioDiaSql, 
				finDiaSql, idPersonaIxeForward, idDivisa, sectorCasasDeBolsa, sectorEmpresas, 
				sectorOtrosInterm, "N").get(0);
		Object [] registroCompraFwEstra = (Object[]) banxicoDao.findMontoForwardsEstrategia(
				inicioDiaSql, finDiaSql, idDivisa, idPersonaIxeForward, "S").get(0);
		Object [] registroVentaFwEstra = (Object[]) banxicoDao.findMontoForwardsEstrategia(
				inicioDiaSql, finDiaSql, idDivisa, idPersonaIxeForward, "N").get(0);
		Object [] registroTotResMercaCompra = (Object[]) banxicoDao.findMontoTotalesResmerca(
				inicioDiaSql, finDiaSql,idPersonaIxeForward, idDivisa,  "S").get(0);
		Object [] registroTotResMercaVenta = (Object[]) banxicoDao.findMontoTotalesResmerca(
				inicioDiaSql, finDiaSql,idPersonaIxeForward, idDivisa,  "N").get(0);
		Double [] montosComprasVentas = new Double[6];
		montosComprasVentas [0] = new Double(((Double) registroCompra [0]).doubleValue() / 1000);
		montosComprasVentas [1] = new Double(((Double) registroVenta [0]).doubleValue() / 1000); 
		montosComprasVentas [2] = new Double(((Double) registroCompraFwEstra [0]).
				doubleValue() / 1000);
		montosComprasVentas [3] = new Double(((Double) registroVentaFwEstra [0]).
				doubleValue() / 1000);
		montosComprasVentas [4] = new Double(((Double) registroTotResMercaCompra [0]).
				doubleValue() / 1000);
		montosComprasVentas [5] = new Double(((Double) registroTotResMercaVenta [0]).
				doubleValue() / 1000);
		return montosComprasVentas;
	}

	/**
	 * Obtiene los montos de los sectores de la seccion de Resmerca.
	 * 
	 * @param banxicoDao El dao para Banxico.
	 * @param inicioDiaSql La fecha de inicio de la busqueda.
	 * @param finDiaSql La fecha de fin de la busqueda.
	 * @param idDivisa El id de la divisa.
	 * @return List
	 */
	public List getMontosFwEstraSectores(BanxicoDao banxicoDao, java.sql.Date inicioDiaSql, 
			java.sql.Date finDiaSql, String idDivisa) {
		ParametroSica parametro = getSicaServiceData().findParametro(
				ParametroSica.ID_PERSONA_IXE_FORWARD);
		Integer idPersonaIxeForward = new Integer(parametro.getValor());
		List registroCompra = banxicoDao.findMontoForwardsEstrategiaSectores(inicioDiaSql, finDiaSql,
				"S", idPersonaIxeForward, idDivisa);
		List registroVenta = banxicoDao.findMontoForwardsEstrategiaSectores(inicioDiaSql, finDiaSql,
				"N", idPersonaIxeForward, idDivisa);
		List registros = new ArrayList();
		registros.add(registroCompra);
		registros.add(registroVenta);
		return registros;
	}

	/**
	 * Asigna al mapa del DataSource los montos de las operaciones de 
	 * Forwards de Estrategias por sectores que peretencen al Resmerca.
	 * 
	 * @param map El mapa que contiene los datos del detalle.
	 * @param montosPorDivisa La lista de montos por divisa.
	 * @param sector El sector al que pertenece la contraparte.
	 * @param rec Define si es compra o venta.
	 */
	public void asignaMontosFwEstraSectores(HashMap map, List montosPorDivisa, String sector, 
			boolean rec) {
		List fwEstraSectoresCompra = montosPorDivisa.get(0) != null ? (List)montosPorDivisa.get(0) 
				: new ArrayList();
		List fwEstraSectoresVenta = montosPorDivisa.get(1) != null ? (List)montosPorDivisa.get(1) 
				: new ArrayList();
		Double montoFwEstraSectores = null;
		if (!rec) {
			if (fwEstraSectoresCompra.size() > 0) {
				for (Iterator it = fwEstraSectoresCompra.iterator(); it.hasNext();) {
					Object [] fwEstraSecCpa = (Object[]) it.next();
					String secFwEstra = (String) fwEstraSecCpa[0];
					if (sector.equals(secFwEstra)) {
						montoFwEstraSectores = (Double) fwEstraSecCpa[2];
						map.put("montoFwEstraSectores", montoFwEstraSectores);
					}
					else {
						map.put("montoFwEstraSectores", new Double(0));
					}
				}
			}
			else {
				map.put("montoFwEstraSectores", new Double(0));
			}
		}
		else {
			if (fwEstraSectoresVenta.size() > 0) {
				for (Iterator it = fwEstraSectoresVenta.iterator(); it.hasNext();) {
					Object [] fwEstraSecVta = (Object[]) it.next();
					String secFwEstra = (String) fwEstraSecVta[0];
					if (sector.equals(secFwEstra)) {
						montoFwEstraSectores = (Double) fwEstraSecVta[2];
						map.put("montoFwEstraSectores", montoFwEstraSectores);
					}
					else {
						map.put("montoFwEstraSectores", new Double(0));
					}
				}
			}
			else {
				map.put("montoFwEstraSectores", new Double(0));
			}
		}
	}

	/**
	 * Da formato a un valor double y devuelve el valor en String.
	 * 
	 * @param valor El valor Double a convertir.
	 * @return String
	 */
	public String convertirFormatoDoubleString(Double valor){
		DecimalFormat pattern = new DecimalFormat( "###,##0.00" );
		String valorString = pattern.format(valor);
		return valorString;
	}

	/**
	 * Obtiene una lista con los valores de la Posici&oacute;n Inicial para la Divisa
	 * que se recibe como parametro. Tambien se obtienen los montos para la Posici&oacute;n
	 * Incial de la Divisa para cada fecha valor para el reporte Sumarizado de Operaciones.
	 * La lista almacena los valores en el siguiente orden:<br>
	 * <li>Monto de la Posici&oacute;n Inicial.</li>
	 * <li>Monto de la Posici&oacute;n para Cash</li>
	 * <li>Monto de la Posici&oacute;n para Tom</li>
	 * <li>Monto de la Posici&oacute;n para Spot</li>
	 * <li>Monto de la Posici&oacute;n para 72Hr</li>
	 * <li>Monto de la Posici&oacute;n para VFut</li>
	 * 
	 * Si la fecha seleccionada es anterior a la fecha de operaci&oacute;n, los montos de la
	 * Posici&oacute;n se buscan en el Hist&oacute;rico.<br>
	 * 
	 * @param idDivisa El id de la Divisa para obtener los montos de su Posici&oacute;n Incial.
	 * @return List
	 */
	public ArrayList getPosicionPorDivisa(String idDivisa) {
		IxenetValidationDelegate delegate = (IxenetValidationDelegate) getBeans().
		getBean("delegate");
		ArrayList pos = new ArrayList();
		Date fechaActual = DateUtils.inicioDia(new Date());
		List listaPosicion = new ArrayList();
		Date fechaSeleccionada = DateUtils.inicioDia(getRegisterDate());
		if (!fechaActual.equals(fechaSeleccionada)) {
			listaPosicion = getSicaServiceData().findHistoricoPosicion(1, idDivisa, DateUtils.
					inicioDia(getRegisterDate()), DateUtils.finDia(getRegisterDate()));
			if (!listaPosicion.isEmpty()) {
				//Posicion Incial
				int ultimo = listaPosicion.size();
				HistoricoPosicion posicion = (HistoricoPosicion)listaPosicion.get(ultimo-1);
				pos.add(new Double(Redondeador.redondear2Dec(posicion.getPosIni().
						getPosicionInicial())));
				pos.add(new Double(Redondeador.redondear2Dec(posicion.getPosIni().
						getPosicionInicialCash())));
				pos.add(new Double(Redondeador.redondear2Dec(posicion.getPosIni().
						getPosicionInicialTom())));
				pos.add(new Double(Redondeador.redondear2Dec(posicion.getPosIni().
						getPosicionInicialSpot())));
				pos.add(new Double(Redondeador.redondear2Dec(posicion.getPosIni().
						getPosicionInicial72Hr().doubleValue())));
				pos.add(new Double(Redondeador.redondear2Dec(posicion.getPosIni().
						getPosicionInicialVFut().doubleValue())));
				calcularTipoCambioPromedio(pos, posicion);
				//PosicionFinal
				pos.add(new Double(Redondeador.redondear2Dec(posicion.getPosIni().
						getPosicionInicial()
						+ posicion.getCpaVta().getTom()
						+ posicion.getCpaVta().getCash()
						+ posicion.getCpaVta().getSpot()
						+ posicion.getCpaVta().get72Hr()
						+ posicion.getCpaVta().getVFut())));
				//Posicion Final por fecha valor
				pos.add(new Double(Redondeador.redondear2Dec(posicion.getPosIni().
						getPosicionInicialCash() + posicion.getCpaVta().getCompraCash() - 
						posicion.getCpaVta().getVentaCash())));
				pos.add(new Double(Redondeador.redondear2Dec(posicion.getPosIni().
						getPosicionInicialTom() + posicion.getCpaVta().getCompraTom() - 
						posicion.getCpaVta().getVentaTom())));
				pos.add(new Double(Redondeador.redondear2Dec(posicion.getPosIni().
						getPosicionInicialSpot() + posicion.getCpaVta().getCompraSpot() - 
						posicion.getCpaVta().getVentaSpot())));
				pos.add(new Double(Redondeador.redondear2Dec(posicion.getPosIni().
						getPosicionInicial72Hr().doubleValue() + posicion.getCpaVta().
						getCompra72Hr().doubleValue() - posicion.getCpaVta().
						getVenta72Hr().doubleValue())));
				pos.add(new Double(Redondeador.redondear2Dec(posicion.getPosIni().
						getPosicionInicialVFut().doubleValue() + posicion.getCpaVta().
						getCompraVFut().doubleValue() - posicion.getCpaVta().
						getVentaVFut().doubleValue())));
				//Posicion Final Compras
				pos.add(new Double(Redondeador.redondear2Dec(posicion.getCpaVta().getCompraCash()
						+ posicion.getCpaVta().getCompraTom()
						+ posicion.getCpaVta().getCompraSpot()
						+ posicion.getCpaVta().getCompra72Hr().doubleValue()
						+ posicion.getCpaVta().getCompraVFut().doubleValue())));
				pos.add(new Double(Redondeador.redondear2Dec(posicion.getCpaVta().getCompraCash())));
				pos.add(new Double(Redondeador.redondear2Dec(posicion.getCpaVta().getCompraTom())));
				pos.add(new Double(Redondeador.redondear2Dec(posicion.getCpaVta().getCompraSpot())));
				pos.add(new Double(Redondeador.redondear2Dec(posicion.getCpaVta().getCompra72Hr().
						doubleValue())));
				pos.add(new Double(Redondeador.redondear2Dec(posicion.getCpaVta().getCompraVFut().
						doubleValue())));
				//Posicion Final Ventas
				pos.add(new Double(Redondeador.redondear2Dec(posicion.getCpaVta().getVentaCash()
						+ posicion.getCpaVta().getVentaTom()
						+ posicion.getCpaVta().getVentaSpot()
						+ posicion.getCpaVta().getVenta72Hr().doubleValue()
						+ posicion.getCpaVta().getVentaVFut().doubleValue())));
				pos.add(new Double(Redondeador.redondear2Dec(posicion.getCpaVta().getVentaCash())));
				pos.add(new Double(Redondeador.redondear2Dec(posicion.getCpaVta().getVentaTom())));
				pos.add(new Double(Redondeador.redondear2Dec(posicion.getCpaVta().getVentaSpot())));
				pos.add(new Double(Redondeador.redondear2Dec(posicion.getCpaVta().getVenta72Hr().
						doubleValue())));
				pos.add(new Double(Redondeador.redondear2Dec(posicion.getCpaVta().getVentaVFut().
						doubleValue())));
			}
			else {
				//HISTORICO VACIO
				delegate.record("No existen montos para la Posicion de la fecha seleccionada.", 
						null);
			}
		}
		else {
			listaPosicion = getSicaServiceData().findPosicionByIdMesaCambioAndIdDivisa(1, idDivisa);
			if (!listaPosicion.isEmpty()) {
				Posicion posicion = (Posicion)listaPosicion.get(0);
				//Posicion Incial
				pos.add(new Double(Redondeador.redondear2Dec(posicion.getPosIni().
						getPosicionInicial())));
				pos.add(new Double(Redondeador.redondear2Dec(posicion.getPosIni().
						getPosicionInicialCash())));
				pos.add(new Double(Redondeador.redondear2Dec(posicion.getPosIni().
						getPosicionInicialTom())));
				pos.add(new Double(Redondeador.redondear2Dec(posicion.getPosIni().
						getPosicionInicialSpot())));
				pos.add(new Double(Redondeador.redondear2Dec(posicion.getPosIni().
						getPosicionInicial72Hr().doubleValue())));
				pos.add(new Double(Redondeador.redondear2Dec(posicion.getPosIni().
						getPosicionInicialVFut().doubleValue())));
				calcularTipoCambioPromedio(pos, posicion);
				//POSICION FINAL
				pos.add(new Double(Redondeador.redondear2Dec(posicion.getPosIni().
						getPosicionInicial()
						+ posicion.getCpaVta().getTom()
						+ posicion.getCpaVta().getCash()
						+ posicion.getCpaVta().getSpot()
						+ posicion.getCpaVta().get72Hr()
						+ posicion.getCpaVta().getVFut())));
				//Posicion Final por fecha valor
				pos.add(new Double(Redondeador.redondear2Dec(posicion.getPosIni().
						getPosicionInicialCash() + posicion.getCpaVta().getCompraCash() - 
						posicion.getCpaVta().getVentaCash())));
				pos.add(new Double(Redondeador.redondear2Dec(posicion.getPosIni().
						getPosicionInicialTom() + posicion.getCpaVta().getCompraTom() - 
						posicion.getCpaVta().getVentaTom())));
				pos.add(new Double(Redondeador.redondear2Dec(posicion.getPosIni().
						getPosicionInicialSpot() + posicion.getCpaVta().getCompraSpot() - 
						posicion.getCpaVta().getVentaSpot())));
				pos.add(new Double(Redondeador.redondear2Dec(posicion.getPosIni().
						getPosicionInicial72Hr().doubleValue() + posicion.getCpaVta().
						getCompra72Hr().doubleValue() - posicion.getCpaVta().getVenta72Hr().
						doubleValue())));
				pos.add(new Double(Redondeador.redondear2Dec(posicion.getPosIni().
						getPosicionInicialVFut().doubleValue() + posicion.getCpaVta().
						getCompraVFut().doubleValue() - 
						posicion.getCpaVta().getVentaVFut().doubleValue())));
				//Posicion Final Compras
				pos.add(new Double(Redondeador.redondear2Dec(posicion.getCpaVta().getCompraCash()
						+ posicion.getCpaVta().getCompraTom()
						+ posicion.getCpaVta().getCompraSpot()
						+ posicion.getCpaVta().getCompra72Hr().doubleValue()
						+ posicion.getCpaVta().getCompraVFut().doubleValue())));
				pos.add(new Double(Redondeador.redondear2Dec(posicion.getCpaVta().
						getCompraCash())));
				pos.add(new Double(Redondeador.redondear2Dec(posicion.getCpaVta().
						getCompraTom())));
				pos.add(new Double(Redondeador.redondear2Dec(posicion.getCpaVta().
						getCompraSpot())));
				pos.add(new Double(Redondeador.redondear2Dec(posicion.getCpaVta().
						getCompra72Hr().doubleValue())));
				pos.add(new Double(Redondeador.redondear2Dec(posicion.getCpaVta().
						getCompraVFut().doubleValue())));
				//Posicion Final Ventas
				pos.add(new Double(Redondeador.redondear2Dec(posicion.getCpaVta().getVentaCash()
						+ posicion.getCpaVta().getVentaTom()
						+ posicion.getCpaVta().getVentaSpot()
						+ posicion.getCpaVta().getVenta72Hr().doubleValue()
						+ posicion.getCpaVta().getVentaVFut().doubleValue())));
				pos.add(new Double(Redondeador.redondear2Dec(posicion.getCpaVta().getVentaCash())));
				pos.add(new Double(Redondeador.redondear2Dec(posicion.getCpaVta().getVentaTom())));
				pos.add(new Double(Redondeador.redondear2Dec(posicion.getCpaVta().getVentaSpot())));
				pos.add(new Double(Redondeador.redondear2Dec(posicion.getCpaVta().
						getVenta72Hr().doubleValue())));
				pos.add(new Double(Redondeador.redondear2Dec(posicion.getCpaVta().
						getVentaVFut().doubleValue())));
			}
			else {
				delegate.record("No existen montos para la Posicion de la fecha seleccionada."
						, null);
			}
		}
		return pos;
	}

	/**
	 * Almacena en el mapa los montos de la Posici&oacute;n Inicial para cada fecha valor
	 * para el reporte Sumarizado de Operaciones.
	 * 
	 * @param map El mapa que contiene los datos del reporte.
	 * @param posicionInicial La lista con los montos de la Posici&oacute;n Inicial de la divisa.
	 */
	public void asignarMontosPosicionTipoCambioDivisaFrecuente(Map map, ArrayList posicion){
		if (!posicion.isEmpty()) {
			//Posicion Inicial
			map.put("posicionInicial",posicion.get(0) != null ? new Double(((Double)
					posicion.get(0)).doubleValue()/1000) : new Double(0.0));
			map.put("posicionInicialCash",posicion.get(1) != null ? new Double(((Double)
					posicion.get(1)).doubleValue()/1000) : new Double(0.0));
			map.put("posicionInicialTom", posicion.get(2) != null ? new Double(((Double)
					posicion.get(2)).doubleValue()/1000) : new Double(0.0));
			map.put("posicionInicialSpot",posicion.get(3) != null ? new Double(((Double)
					posicion.get(3)).doubleValue()/1000) : new Double(0.0));
			map.put("posicionInicial72Hr",posicion.get(4) != null ? new Double(((Double)
					posicion.get(4)).doubleValue()/1000) : new Double(0.0));
			map.put("posicionInicialVFut",posicion.get(5) != null ? new Double(((Double)
					posicion.get(5)).doubleValue()/1000) : new Double(0.0));
			map.put("tipoCambioCompra",posicion.get(6) != null ? (Double)posicion.get(6) : 
				new Double(0.0));
			map.put("tipoCambioCompraCash",posicion.get(7) != null ? (Double)posicion.get(7) : 
				new Double(0.0));
			map.put("tipoCambioCompraTom",posicion.get(8) != null ? (Double)posicion.get(8) : 
				new Double(0.0));
			map.put("tipoCambioCompraSpot",posicion.get(9) != null ? (Double)posicion.get(9) : 
				new Double(0.0));
			map.put("tipoCambioCompra72Hr",posicion.get(10) != null ? (Double)posicion.get(10) : 
				new Double(0.0));
			map.put("tipoCambioCompraVFut",posicion.get(11) != null ? (Double)posicion.get(11) : 
				new Double(0.0));
			map.put("tipoCambioVenta",posicion.get(12) != null ? (Double)posicion.get(12) : 
				new Double(0.0));
			map.put("tipoCambioVentaCash",posicion.get(13) != null ? (Double)posicion.get(13) : 
				new Double(0.0));
			map.put("tipoCambioVentaTom",posicion.get(14) != null ? (Double)posicion.get(14) : 
				new Double(0.0));
			map.put("tipoCambioVentaSpot",posicion.get(15) != null ? (Double)posicion.get(15) : 
				new Double(0.0));
			map.put("tipoCambioVenta72Hr",posicion.get(16) != null ? (Double)posicion.get(16) : 
				new Double(0.0));
			map.put("tipoCambioVentaVFut",posicion.get(17) != null ? (Double)posicion.get(17) : 
				new Double(0.0));
			//Posicion Final
			map.put("posicionFinal",posicion.get(18) != null ? new Double(((Double)
					posicion.get(18)).doubleValue()/1000) : new Double(0.0));
			map.put("posicionFinalCash",posicion.get(19) != null ? new Double(((Double)
					posicion.get(19)).doubleValue()/1000) : new Double(0.0));
			map.put("posicionFinalTom", posicion.get(20) != null ? new Double(((Double)
					posicion.get(20)).doubleValue()/1000) : new Double(0.0));
			map.put("posicionFinalSpot",posicion.get(21) != null ? new Double(((Double)
					posicion.get(21)).doubleValue()/1000) : new Double(0.0));
			map.put("posicionFinal72Hr",posicion.get(22) != null ? new Double(((Double)
					posicion.get(22)).doubleValue()/1000) : new Double(0.0));
			map.put("posicionFinalVFut",posicion.get(23) != null ? new Double(((Double)
					posicion.get(23)).doubleValue()/1000) : new Double(0.0));
			//Posicion Final Compras
			map.put("posicionFinalCompras",posicion.get(24) != null ? new Double(((Double)
					posicion.get(24)).doubleValue()/1000) : new Double(0.0));
			map.put("posicionFinalComprasCash",posicion.get(25) != null ? new Double(((Double)
					posicion.get(25)).doubleValue()/1000) : new Double(0.0));
			map.put("posicionFinalComprasTom", posicion.get(26) != null ? new Double(((Double)
					posicion.get(26)).doubleValue()/1000) : new Double(0.0));
			map.put("posicionFinalComprasSpot",posicion.get(27) != null ? new Double(((Double)
					posicion.get(27)).doubleValue()/1000) : new Double(0.0));
			map.put("posicionFinalCompras72Hr",posicion.get(28) != null ? new Double(((Double)
					posicion.get(28)).doubleValue()/1000) : new Double(0.0));
			map.put("posicionFinalComprasVFut",posicion.get(29) != null ? new Double(((Double)
					posicion.get(29)).doubleValue()/1000) : new Double(0.0));
			//Posicion Final Ventas
			map.put("posicionFinalVentas",posicion.get(30) != null ? new Double(((Double)
					posicion.get(30)).doubleValue()/1000) : new Double(0.0));
			map.put("posicionFinalVentasCash",posicion.get(31) != null ? new Double(((Double)
					posicion.get(31)).doubleValue()/1000) : new Double(0.0));
			map.put("posicionFinalVentasTom", posicion.get(32) != null ? new Double(((Double)
					posicion.get(32)).doubleValue()/1000) : new Double(0.0));
			map.put("posicionFinalVentasSpot",posicion.get(33) != null ? new Double(((Double)
					posicion.get(33)).doubleValue()/1000) : new Double(0.0));
			map.put("posicionFinalVentas72Hr",posicion.get(34) != null ? new Double(((Double)
					posicion.get(34)).doubleValue()/1000) : new Double(0.0));
			map.put("posicionFinalVentasVFut",posicion.get(35) != null ? new Double(((Double)
					posicion.get(35)).doubleValue()/1000) : new Double(0.0));
		}
		else {
			//Posicion Incial
			map.put("posicionInicial",new Double(0.0));
			map.put("posicionInicialCash",new Double(0.0));
			map.put("posicionInicialTom",new Double(0.0));
			map.put("posicionInicialSpot",new Double(0.0));
			map.put("posicionInicial72Hr",new Double(0.0));
			map.put("posicionInicialVFut",new Double(0.0));
			map.put("tipoCambioCompra",new Double(0.0));
			map.put("tipoCambioCompraCash",new Double(0.0));
			map.put("tipoCambioCompraTom",new Double(0.0));
			map.put("tipoCambioCompraSpot",new Double(0.0));
			map.put("tipoCambioCompra72Hr",new Double(0.0));
			map.put("tipoCambioCompraVFut",new Double(0.0));
			map.put("tipoCambioVenta",new Double(0.0));
			map.put("tipoCambioVentaCash",new Double(0.0));
			map.put("tipoCambioVentaTom",new Double(0.0));
			map.put("tipoCambioVentaSpot",new Double(0.0));
			map.put("tipoCambioVenta72Hr",new Double(0.0));
			map.put("tipoCambioVentaVFut",new Double(0.0));
			//Posicion Final
			map.put("posicionFinal",new Double(0.0));
			map.put("posicionFinalCash",new Double(0.0));
			map.put("posicionFinalTom",new Double(0.0));
			map.put("posicionFinalSpot",new Double(0.0));
			map.put("posicionFinal72Hr",new Double(0.0));
			map.put("posicionFinalVFut",new Double(0.0));
			//Posicion Final Compras
			map.put("posicionFinalCompras",new Double(0.0));
			map.put("posicionFinalComprasCash",new Double(0.0));
			map.put("posicionFinalComprasTom", new Double(0.0));
			map.put("posicionFinalComprasSpot",new Double(0.0));
			map.put("posicionFinalCompras72Hr",new Double(0.0));
			map.put("posicionFinalComprasVFut",new Double(0.0));
			//Posicion Final Ventas
			map.put("posicionFinalVentas",new Double(0.0));
			map.put("posicionFinalVentasCash",new Double(0.0));
			map.put("posicionFinalVentasTom", new Double(0.0));
			map.put("posicionFinalVentasSpot",new Double(0.0));
			map.put("posicionFinalVentas72Hr",new Double(0.0));
			map.put("posicionFinalVentasVFut",new Double(0.0));
		}
	}

	/**
	 * Obtiene los montos totales de la seccion del Resmerca para el Reporte de Banxico.
	 * 
	 * @param map El mapa del reporte.
	 * @param resmerca El arreglo de los montos del Resmerca. 
	 * @param divisa El id de la divisa.
	 * @param factorDivisa El valor del factor divisa si la divisa no es dolar.
	 */
	public void agregarTotalResmercaAgrupado(Map map, Object[] resmerca, String divisa, 
			double factorDivisa) {
		String montoResmercaVta = "";
		String montoResmercaCpa = "";
		if (Divisa.DOLAR.equals(divisa)) {
			montoResmercaVta = getMoneyFormat().format(new Double(Redondeador.redondear2Dec(((
					(Double) resmerca [0]).doubleValue()) / 1000)));
			montoResmercaCpa = getMoneyFormat().format(new Double(Redondeador.redondear2Dec(((
					(Double) resmerca [1]).doubleValue()) / 1000)));	
		}
		else {
			montoResmercaVta = getMoneyFormat().format(new Double(Redondeador.redondear2Dec(((
					(Double) resmerca [0]).doubleValue() * factorDivisa) / 1000)));
			montoResmercaCpa = getMoneyFormat().format(new Double(Redondeador.redondear2Dec(((
					(Double) resmerca [1]).doubleValue() * factorDivisa) / 1000)));
		}
		map.put("totalResmercaAgrupadoCpa", montoResmercaCpa);
		map.put("totalResmercaAgrupadoVta", montoResmercaVta);
	}

	/**
	 * Agrega al mapa del reporte, lo valores para posicion incial, final y por fechas valor.
	 * 
	 * @param mapa El mapa para el reporte.
	 * @param idDivisa La divisa a consultar su posicion.
	 * @param posicion La lista con los datos de la posicion.
	 */
	public void agregarMontosPosicionMapa(Map mapa, String idDivisa, ArrayList posicion) {
		asignarMontosPosicionTipoCambioDivisaFrecuente(mapa, posicion);
	}


	/**
	 * Asigna en los montos de la Posici&oacute;n Incial para Divisas no Frecuentes
	 * para el reporte Sumarizado de Operaciones.
	 * 
	 * @param map El mapa que contiene los datos del reporte.
	 * @param idDivisa La clave de la divisa.
	 */
	public void asignarMontosPosIniFinTipoCambioDivisaNoFrecuente(Map map, String idDivisa){
		ArrayList posicionDivisaNoFrecuente = getPosicionPorDivisa(idDivisa);
		asignarMontosPosicionTipoCambioDivisaFrecuente(map, posicionDivisaNoFrecuente);
	}

	/**
	 * Obtiene y calcula los montos para el Tipo de Cambio Promedio Ponderado Global y
	 * para cada fecha valor. Almacena los valores en la lista que contiene los montos
	 * para posicion Incial y Final de la divisa correspondiente.
	 * 
	 * @param listaPosicion La lista que almacenar&aacute; 
	 * los montos para tipos de cambio de cada fecha valor.
	 * @param posicion La Posici&oacute;n para una divisa.
	 */
	public void calcularTipoCambioPromedio(ArrayList listaPosicion, Posicion posicion){
		double compra   = posicion.getCpaVta().getCompraCash() + 
		posicion.getCpaVta().getCompraTom() + 
		posicion.getCpaVta().getCompraSpot() + 
		posicion.getCpaVta().getCompra72Hr().doubleValue() + 
		posicion.getCpaVta().getCompraVFut().doubleValue();
		double compraMn = posicion.getCpaVtaMn().getCompraMnClienteCash() + 
		posicion.getCpaVtaMn().getCompraMnClienteTom() + 
		posicion.getCpaVtaMn().getCompraMnClienteSpot() + 
		posicion.getCpaVtaMn().getCompraMnCliente72Hr().doubleValue() +
		posicion.getCpaVtaMn().getCompraMnClienteVFut().doubleValue();
		double venta   = posicion.getCpaVta().getVentaCash() + 
		posicion.getCpaVta().getVentaTom() + 
		posicion.getCpaVta().getVentaSpot() + 
		posicion.getCpaVta().getVenta72Hr().doubleValue() + 
		posicion.getCpaVta().getVentaVFut().doubleValue();
		double ventaMn = posicion.getCpaVtaMn().getVentaMnClienteCash() + 
		posicion.getCpaVtaMn().getVentaMnClienteTom() + 
		posicion.getCpaVtaMn().getVentaMnClienteSpot() + 
		posicion.getCpaVtaMn().getVentaMnCliente72Hr().doubleValue() +
		posicion.getCpaVtaMn().getVentaMnClienteVFut().doubleValue();
		Double tipoCambioCompra = new Double (compra == 0 ? 0 : compraMn / compra);
		Double tipoCambioCompraCash = new Double(posicion.getCpaVta().getCompraCash() == 0 ? 0 : 
			posicion.getCpaVtaMn().getCompraMnClienteCash() / posicion.getCpaVta().getCompraCash());
		Double tipoCambioCompraTom = new Double(posicion.getCpaVta().getCompraTom() == 0 ? 0 : 
			posicion.getCpaVtaMn().getCompraMnClienteTom() / posicion.getCpaVta().getCompraTom());
		Double tipoCambioCompraSpot = new Double(posicion.getCpaVta().getCompraSpot() == 0 ? 0 : 
			posicion.getCpaVtaMn().getCompraMnClienteSpot() / posicion.getCpaVta().getCompraSpot());
		Double tipoCambioCompra72Hr = new Double(posicion.getCpaVta().getCompra72Hr().doubleValue() 
				== 0 ? 0 : posicion.getCpaVtaMn().getCompraMnCliente72Hr().doubleValue() / 
						posicion.getCpaVta().getCompra72Hr().doubleValue());
		Double tipoCambioCompraVFut = new Double(posicion.getCpaVta().getCompraVFut().doubleValue() 
				== 0 ? 0 : posicion.getCpaVtaMn().getCompraMnClienteVFut().doubleValue() / 
						posicion.getCpaVta().getCompraVFut().doubleValue());
		Double tipoCambioVenta = new Double (venta == 0 ? 0 : ventaMn / venta);
		Double tipoCambioVentaCash = new Double(posicion.getCpaVta().getVentaCash() == 0 ? 0 : 
			posicion.getCpaVtaMn().getVentaMnClienteCash() / posicion.getCpaVta().getVentaCash());
		Double tipoCambioVentaTom = new Double(posicion.getCpaVta().getVentaTom() == 0 ? 0 : 
			posicion.getCpaVtaMn().getVentaMnClienteTom() / posicion.getCpaVta().getVentaTom());
		Double tipoCambioVentaSpot = new Double(posicion.getCpaVta().getVentaSpot() == 0 ? 0 : 
			posicion.getCpaVtaMn().getVentaMnClienteSpot() / posicion.getCpaVta().getVentaSpot());
		Double tipoCambioVenta72Hr = new Double(posicion.getCpaVta().getVenta72Hr().doubleValue() 
				== 0 ? 0 : posicion.getCpaVtaMn().getVentaMnCliente72Hr().doubleValue() / 
						posicion.getCpaVta().getVenta72Hr().doubleValue());
		Double tipoCambioVentaVFut = new Double(posicion.getCpaVta().getVentaVFut().doubleValue() 
				== 0 ? 0 : posicion.getCpaVtaMn().getVentaMnClienteVFut().doubleValue() / 
						posicion.getCpaVta().getVentaVFut().doubleValue());
		listaPosicion.add(tipoCambioCompra);
		listaPosicion.add(tipoCambioCompraCash);
		listaPosicion.add(tipoCambioCompraTom);
		listaPosicion.add(tipoCambioCompraSpot);
		listaPosicion.add(tipoCambioCompra72Hr);
		listaPosicion.add(tipoCambioCompraVFut);
		listaPosicion.add(tipoCambioVenta);
		listaPosicion.add(tipoCambioVentaCash);
		listaPosicion.add(tipoCambioVentaTom);
		listaPosicion.add(tipoCambioVentaSpot);
		listaPosicion.add(tipoCambioVenta72Hr);
		listaPosicion.add(tipoCambioVentaVFut);
	}

	/**
	 * Obtiene y calcula los montos para el Tipo de Cambio Promedio Ponderado Global y
	 * para cada fecha valor. Almacena los valores en la lista que contiene los montos
	 * para posicion Incial y Final de la divisa correspondiente.
	 * 
	 * @param listaPosicion La lista que almacenar&aacute; los montos para tipos de 
	 * cambio de cada fecha valor.
	 * @param posicion El Hist&oacute;rico de la Posici&oacute;n para una divisa.
	 */
	public void calcularTipoCambioPromedio(ArrayList listaPosicion, HistoricoPosicion posicion){
		double compra   = posicion.getCpaVta().getCompraCash() + 
		posicion.getCpaVta().getCompraTom() + 
		posicion.getCpaVta().getCompraSpot() + 
		posicion.getCpaVta().getCompra72Hr().doubleValue() + 
		posicion.getCpaVta().getCompraVFut().doubleValue();
		double compraMn = posicion.getCpaVtaMn().getCompraMnClienteCash() + 
		posicion.getCpaVtaMn().getCompraMnClienteTom() + 
		posicion.getCpaVtaMn().getCompraMnClienteSpot() + 
		posicion.getCpaVtaMn().getCompraMnCliente72Hr().doubleValue() +
		posicion.getCpaVtaMn().getCompraMnClienteVFut().doubleValue();
		double venta   = posicion.getCpaVta().getVentaCash() + 
		posicion.getCpaVta().getVentaTom() + 
		posicion.getCpaVta().getVentaSpot() + 
		posicion.getCpaVta().getVenta72Hr().doubleValue() + 
		posicion.getCpaVta().getVentaVFut().doubleValue();
		double ventaMn = posicion.getCpaVtaMn().getVentaMnClienteCash() + 
		posicion.getCpaVtaMn().getVentaMnClienteTom() + 
		posicion.getCpaVtaMn().getVentaMnClienteSpot() + 
		posicion.getCpaVtaMn().getVentaMnCliente72Hr().doubleValue() +
		posicion.getCpaVtaMn().getVentaMnClienteVFut().doubleValue();
		Double tipoCambioCompra = new Double (compra == 0 ? 0 : compraMn / compra);
		Double tipoCambioCompraCash = new Double(posicion.getCpaVta().getCompraCash() == 0 ? 0 : 
			posicion.getCpaVtaMn().getCompraMnClienteCash() / posicion.getCpaVta().getCompraCash());
		Double tipoCambioCompraTom = new Double(posicion.getCpaVta().getCompraTom() == 0 ? 0 : 
			posicion.getCpaVtaMn().getCompraMnClienteTom() / posicion.getCpaVta().getCompraTom());
		Double tipoCambioCompraSpot = new Double(posicion.getCpaVta().getCompraSpot() == 0 ? 0 : 
			posicion.getCpaVtaMn().getCompraMnClienteSpot() / posicion.getCpaVta().getCompraSpot());
		Double tipoCambioCompra72Hr = new Double(posicion.getCpaVta().getCompra72Hr().doubleValue()
				== 0 ? 0 : posicion.getCpaVtaMn().getCompraMnCliente72Hr().doubleValue() / 
						posicion.getCpaVta().getCompra72Hr().doubleValue());
		Double tipoCambioCompraVFut = new Double(posicion.getCpaVta().getCompraVFut().doubleValue() 
				== 0 ? 0 : posicion.getCpaVtaMn().getCompraMnClienteVFut().doubleValue() / 
						posicion.getCpaVta().getCompraVFut().doubleValue());
		Double tipoCambioVenta = new Double (venta == 0 ? 0 : ventaMn / venta);
		Double tipoCambioVentaCash = new Double(posicion.getCpaVta().getVentaCash() == 0 ? 0 : 
			posicion.getCpaVtaMn().getVentaMnClienteCash() / posicion.getCpaVta().getVentaCash());
		Double tipoCambioVentaTom = new Double(posicion.getCpaVta().getVentaTom() == 0 ? 0 : 
			posicion.getCpaVtaMn().getVentaMnClienteTom() / posicion.getCpaVta().getVentaTom());
		Double tipoCambioVentaSpot = new Double(posicion.getCpaVta().getVentaSpot() == 0 ? 0 : 
			posicion.getCpaVtaMn().getVentaMnClienteSpot() / posicion.getCpaVta().getVentaSpot());
		Double tipoCambioVenta72Hr = new Double(posicion.getCpaVta().getVenta72Hr().doubleValue() 
				== 0 ? 0 : posicion.getCpaVtaMn().getVentaMnCliente72Hr().doubleValue() / 
						posicion.getCpaVta().getVenta72Hr().doubleValue());
		Double tipoCambioVentaVFut = new Double(posicion.getCpaVta().getVentaVFut().doubleValue() 
				== 0 ? 0 : posicion.getCpaVtaMn().getVentaMnClienteVFut().doubleValue() / 
						posicion.getCpaVta().getVentaVFut().doubleValue());
		listaPosicion.add(tipoCambioCompra);
		listaPosicion.add(tipoCambioCompraCash);
		listaPosicion.add(tipoCambioCompraTom);
		listaPosicion.add(tipoCambioCompraSpot);
		listaPosicion.add(tipoCambioCompra72Hr);
		listaPosicion.add(tipoCambioCompraVFut);
		listaPosicion.add(tipoCambioVenta);
		listaPosicion.add(tipoCambioVentaCash);
		listaPosicion.add(tipoCambioVentaTom);
		listaPosicion.add(tipoCambioVentaSpot);
		listaPosicion.add(tipoCambioVenta72Hr);
		listaPosicion.add(tipoCambioVentaVFut);
	}

	/**
	 * Obtiene el DataSource para generar el reporte  Anal&iacute;tico de 
	 * Operaciones de Compra y Venta con Swaps en formato Xls.
	 *
	 * @param id El identificador.
	 * @return JRDataSource
	 * @see com.ixe.ods.tapestry.services.jasper.DataSourceProvider#getDataSource(String id)
	 */
	public JRDataSource getDataSourceXls(String id) {
		List l = getSicaServiceData().
		findAllDealDetallesBySectorAndDateOrderByDivisaAndRecibimosAndNoCuenta(
				DateUtils.inicioDia(getRegisterDate()), DateUtils.finDia(getRegisterDate()));
		List detallesMap = new ArrayList();
		PrecioReferenciaActual precioReferencia = getSicaServiceData().findPrecioReferenciaActual();
		for (Iterator iter = l.iterator(); iter.hasNext();) {
			DealDetalle element = (DealDetalle) iter.next();
			if (!DealDetalle.STATUS_DET_CANCELADO.equals(element.getStatusDetalleDeal()) &&
					!DealDetalle.STATUS_DET_PROCESO_CAPTURA.equals(element.getStatusDetalleDeal())
					&& element.getDeal().getCliente() != null) {
				HashMap map = new HashMap();
				map.put("tipoContraparte", element.getDeal().getTipoContraparte());
				double tipoCambio;
				if (element.getFactorDivisa() != null) {
					tipoCambio = element.getFactorDivisa().doubleValue();
				}
				else {
					tipoCambio = precioReferencia.getPreRef().getMidSpot().doubleValue();
				}
				double monto = 0;
				if (Divisa.DOLAR.equals(element.getDivisa().getIdDivisa())){
					monto  = element.getMonto() / 1000;
				}
				else {
					monto = (element.getMonto() * tipoCambio) / 1000;
				}
				if (element.isRecibimos()) {
					map.put("comprado", new Double(Redondeador.redondear2Dec(monto)));
					map.put("vendido", new Double(0.00));
				}
				else {
					map.put("vendido", new Double(Redondeador.redondear2Dec(monto)));
					map.put("comprado", new Double(0.00));
				}
				map.put("factorDivisa", new Double(tipoCambio));
				map.put("fechaValor", DateUtils.inicioDia(element.getDeal().getFechaLiquidacion()));
				map.put("divisa", element.getDivisa().getIdDivisa().toUpperCase());
				map.put("folioInterno", element.getDeal().getIdDeal() + "-" 
						+ element.getFolioDetalle());
				if (element.getDeal().getSwap() != null) {
					Integer estrategia = new Integer(element.getDeal().getSwap().getIdFolioSwap());
					map.put("estrategia", "1");
					map.put("folioEstrategia", estrategia.toString());
				}
				else {
					map.put("estrategia", "0");
					map.put("folioEstrategia", "");
				}
				if (element.getDeal().getContratoSica() != null) {
					if (element.getDeal().getCliente().getIdPersona() != null) {
						String cveContraparte = getSicaServiceData().findCveBanxicoByIdPersona(
								element.getDeal().getCliente().getIdPersona());
						String nombreContraparte = getSicaServiceData().
						findNombreBanxicoByIdPersona(element.getDeal().getCliente().getIdPersona());
						if (cveContraparte != null) {
							map.put("claveContraparte", cveContraparte);
							map.put("cliente", nombreContraparte);
							map.put("codigoContraparte", element.getDeal().getTipoContraparte());
							map.put("tipoContraparte", element.getDeal().getCliente().
									getSectorEconomico() != null ? element.getDeal().getCliente().
											getSectorEconomico().getDescSectorBanxico() : 
							"El cliente no tiene sector economico" );
						}
						else {
							map.put("claveContraparte", "");
							map.put("cliente", element.getDeal().getCliente().getNombreCompleto());
							map.put("codigoContraparte", element.getDeal().getTipoContraparte());
							if (element.getDeal().getCliente().getSectorEconomico() != null) {
								map.put("tipoContraparte", element.getDeal().getCliente().
										getSectorEconomico().getDescSectorBanxico());
							}
							else {
								map.put("tipoContraparte", "");
							}
						}
					}
					else {
						map.put("claveContraparte", "");
						map.put("cliente", element.getDeal().getCliente().getNombreCompleto());
						map.put("codigoContraparte", element.getDeal().getCliente().
								getSectorEconomico() != null ? 
										element.getDeal().getCliente().getSectorEconomico().
										getSectorBanxico(): "El cliente no tiene sector economico");
						if (element.getDeal().getCliente().getSectorEconomico() != null) {
							map.put("tipoContraparte", element.getDeal().getCliente().
									getSectorEconomico().getDescSectorBanxico());
						}
						else {
							map.put("tipoContraparte", "El cliente no tiene sector economico");
						}
					}
				}
				else {
					// Este codigo inserta cadenas vacias en los valores de
					// las llaves correspondientes, en caso de
					// que el cliente no tenga una instancia de ContratoSica:
					map.put("claveContraparte", "");
					map.put("cliente", "");
					map.put("codigoContraparte", "");
					map.put("tipoContraparte", "");
				}
				if (element.getDeal().isNeteo()) {
					map.put("dealDivisaDivisa", "1");
				}
				else {
					map.put("dealDivisaDivisa", "0");
				}
				detallesMap.add(map);
			}
		}
		return new ListDataSource(detallesMap);
	}

	/**
	 * Obtiene el Factor Divisa actual con para la divisa definida en el par&aacute;metro.
	 * 
	 * @param idDivisa El id de la Divisa.
	 * @return double
	 */
	public double obtenerFactorByIdDivisa(String idDivisa){
		List factoresDivisaActuales = getSicaServiceData().findFactoresDivisaActuales();
		double fd = 0;
		for (Iterator it  = factoresDivisaActuales.iterator();it.hasNext();){
			FactorDivisaActual factorDivisa = (FactorDivisaActual) it.next();
			if (factorDivisa.getFacDiv().getFromDivisa().getIdDivisa().equals(idDivisa)){
				fd = factorDivisa.getFacDiv().getFactor();
				break;
			}
		}
		return fd;
	}

	/**
	 * Obtiene el DataSource para generar el Reporte de Posicion del D&iacute;a.
	 *
	 * @param id El identificador.
	 * @return JRDataSource
	 * @see com.ixe.ods.tapestry.services.jasper.DataSourceProvider#getDataSource(String id)
	 */
	public JRDataSource getDataSourceSecccion(String id) {
		Date inicioDia  = DateUtils.inicioDia(getRegisterDate()); 
		Date finDia  = DateUtils.finDia(getRegisterDate());
		Calendar cal = new GregorianCalendar();
		cal.setTime(finDia);
		cal.add(Calendar.DAY_OF_YEAR, 1);
		java.sql.Date inicioDiaSql =  new java.sql.Date(inicioDia.getTime());
		java.sql.Date finDiaSql =  new java.sql.Date(cal.getTime().getTime());
		BanxicoDao banxicoDao = (BanxicoDao) getApplicationContext().getBean("banxicoDao");
		List l = banxicoDao.findDetallesIncluyeStatusAlta(inicioDiaSql, finDiaSql);
		List detallesMap = new ArrayList();
		List factoresDivisa = getSicaServiceData().findFactoresDivisaActuales();
		for (Iterator iter = l.iterator(); iter.hasNext();) {
			String claveIXE = "040032" + new Date();
			Object [] element = (Object[]) iter.next();
			HashMap map = new HashMap();
			map.put("claveGfIXE", claveIXE);
			String compraVenta  = (String) element[4];
			Boolean recibimos =  new Boolean("Venta".equals(compraVenta) ? false : true );
			if (recibimos.booleanValue()) {
				map.put("compra_venta", "COMPRA");
			}
			else {
				map.put("compra_venta", "VENTA");
			}
			Divisa divisa = getSicaServiceData().findDivisa((String) element[8]);
			map.put("divisa", divisa.getIdDivisa());
			map.put("descripcionDivisa", divisa.getDescripcion());
			map.put("folioInterno", new String(((Integer)element[0]).toString() + "-" 
					+ ((Integer)element[1]).toString()));
			map.put("cliente", (String) element[5]);
			map.put("status", obtenerDescipcionStatusPorClave((String) element[14]));
			map.put("claveSector", (String)element[22] != null ? (String)element[22] 
			                                                                     : "El cliente no tiene sector asignado");
			map.put("descripcionClaveSector", (String) element[17]);
			map.put("contratoSica", (String) element[13]);
			map.put("claveContraparte", (String) element[24] != null ? (String) element[24] 
			                                                                            : "El cliente no tiene clave asignada");
			String idSwap = (String)element[20];
			if (!"0".equals(idSwap)) {
				map.put("estrategia", "1");
				map.put("folioEstrategia", idSwap.toString());
			}
			else {
				map.put("estrategia", "0");
				map.put("folioEstrategia", "");
			}
			map.put("fechaPacto", (Date)element[23]);
			map.put("fechaLiquidacion", element[3]);
			Double factorDivisa = element[18] != null ? new Double((String)element[18]) : new Double("0.0");
			double factor = asignarFactorDivisa(factoresDivisa, divisa, factorDivisa);
			map.put("tipoCambio", new Double(factor));
			map.put("monto", (Double) element[7]);
			detallesMap.add(map);
			//}
		}
		return new ListDataSource(detallesMap);
	}

	/**
	 * Regresa la descripcion del estatus del detalle de deal dada su clave
	 * de estatus.
	 * 
	 * @param clave La clave del estatus del detalle de  deal.
	 * @return String
	 */
	public String obtenerDescipcionStatusPorClave(String clave) {
		String descripcion = "";
		if (DealDetalle.STATUS_DET_CANCELADO.equals(clave)) {
			descripcion = "Cancelado";
		} 
		else if (DealDetalle.STATUS_DET_COMPLETO.equals(clave)) { 
			descripcion = "Completo";
		}
		else if (DealDetalle.STATUS_DET_PARCIALMENTE_PAG_LIQ.equals(clave)) { 
			descripcion = "Parcialmente Liquidaddo";
		}
		else if (DealDetalle.STATUS_DET_PROCESO_CAPTURA.equals(clave)) { 
			descripcion = "Alta";
		}
		else if (DealDetalle.STATUS_DET_TOTALMENTE_PAG_LIQ.equals(clave)) { 
			descripcion = "Totalmente Liquidado";
		}
		return descripcion;
	}

	/**
	 * Asigna el valor del Factor Divisa para la divisa definida; el valor se obtiene de la consulta
	 * a los valores de FactorDivisaActual. En caso de no tener el Factor Divisa actual para la
	 * divisa, se obtiene el factor que se definio para el Deal.
	 * 
	 * @param factoresDivisa La lista con los factores divisa actuales.
	 * @param divisa La divisa definida.
	 * @param factorDivisa  El Factor Divisa utilizado en el Deal.
	 * @return double
	 */
	public double asignarFactorDivisa(List factoresDivisa, Divisa divisa, Double factorDivisa) {
		double factor = 0;
		if (!factoresDivisa.isEmpty()) {
			for (Iterator ite = factoresDivisa.iterator();ite.hasNext();) {
				FactorDivisaActual fd = (FactorDivisaActual) ite.next();
				if ((Divisa.DOLAR.equals(fd.getFacDiv().getToDivisa().getIdDivisa())
						&& divisa.getIdDivisa().equals(fd.getFacDiv().getFromDivisa().
								getIdDivisa())) ) {
					factor = fd.getFacDiv().getFactor();
				}
			}
			if (factor < 1) {
				if (factorDivisa != null) {
					factor = factorDivisa.doubleValue();
				}
				else {
					PrecioReferenciaActual pr = getSicaServiceData().findPrecioReferenciaActual();
					factor = pr.getPreRef().getMidSpot().doubleValue();
				}
			}
		}
		else {
			if (factorDivisa != null) {
				factor = factorDivisa.doubleValue();
			}
			else {
				PrecioReferenciaActual pr = getSicaServiceData().findPrecioReferenciaActual();
				factor = pr.getPreRef().getMidSpot().doubleValue();
			}
		}
		return factor;
	}

	/**
	 * Obtiene el DataSource para generar el Reporte de la Secci&oacute;n VIII del ACLME
	 * en formato Xls para Banxico.
	 *
	 * @param id El identificador.
	 * @return JRDataSource
	 * @see com.ixe.ods.tapestry.services.jasper.DataSourceProvider#getDataSource(String id)
	 */
	public JRDataSource getDataSourceAclmeBanxico(String id) {
		List detallesMap = new ArrayList();
		String claveIXE = "040032";
		Date inicioDia  = DateUtils.inicioDia(getRegisterDate()); 
		Date finDia  = DateUtils.finDia(getRegisterDate());
		Calendar cal = new GregorianCalendar();
		cal.setTime(finDia);
		cal.add(Calendar.DAY_OF_YEAR, 1);
		java.sql.Date inicioDiaSql =  new java.sql.Date(inicioDia.getTime());
		java.sql.Date finDiaSql =  new java.sql.Date(cal.getTime().getTime());
		BanxicoDao banxicoDao = (BanxicoDao) getApplicationContext().getBean("banxicoDao");
		List l = banxicoDao.findDetallesIncluyeStatusAlta(inicioDiaSql, finDiaSql);
		for (Iterator iter = l.iterator(); iter.hasNext();) {
			Object [] element = (Object[]) iter.next();
			HashMap map = new HashMap();
			map.put("claveGfIXE", claveIXE + new Date());
			String compraVenta  = (String) element[4];
			Boolean recibimos =  new Boolean("Venta".equals(compraVenta) ? false : true );
			if (recibimos.booleanValue()) {
				map.put("compra_venta", "COMPRA");
			}
			else {
				map.put("compra_venta", "VENTA");
			}
			Divisa divisa = getSicaServiceData().findDivisa((String) element[8]);
			map.put("divisa", divisa.getIdDivisa());

			map.put("folioInterno", new String(((Integer)element[0]).toString() + "-" 
					+ ((Integer)element[1]).toString()));
			map.put("contratoSica", (String) element[13]);
			map.put("claveContraparte", ((String) element[24]) != null ? (String) element[24] : 
			"El cliente no tiene clave asignada." );
			String idSwap = (String) element[20];
			if (!"0".equals(idSwap)) {
				map.put("estrategia", "1");
				map.put("folioEstrategia", idSwap.toString());
			}
			else {
				map.put("estrategia", "0");
				map.put("folioEstrategia", "");
			}
			map.put("fechaPacto", (Date) element[23]);
			map.put("fechaLiquidacion", element[3]);
			map.put("monto", (Double) element[7]);
			detallesMap.add(map);
		}
		return new ListDataSource(detallesMap);
	}

	/**
	 * Genera el reporte Secci&oacute;n VIII del ACLME en formato Xls.
	 *
	 * @param cycle El ciclo de la p&acute;gina.
	 */
	public void imprimirXlsSeccion(IRequestCycle cycle) {
		Object parameters[] = cycle.getServiceParameters();
		String resource = (String) parameters[0];
		String name = (String) parameters[1];
		Map reportOutParams = null;
		if (parameters.length > 3) {
			reportOutParams = (Map) parameters[3];
		}
		JasperPrint jasperPrint = null;
		InputStream inputStream = null;
		try {
			JRDataSource dataSource = getDataSourceSecccion("DIVISAS");
			BaseGlobal global = (BaseGlobal) getGlobal();
			inputStream = global.getApplicationContext().getResource(resource).getInputStream();
			JasperReport jasperReport = (JasperReport) JRLoader.loadObject(inputStream);
			jasperPrint = JasperFillManager.fillReport(jasperReport, reportOutParams, dataSource);
		}
		catch (JRException e) {
			if (_logger.isDebugEnabled()) {
				_logger.debug(e);
			}
		}
		catch (IOException e) {
			if (_logger.isDebugEnabled()) {
				_logger.debug(e);
			}
		}
		try {
			HttpServletResponse response = cycle.getRequestContext()
			.getResponse();
			JRXlsExporter exporter = new JRXlsExporter();
			ByteArrayOutputStream xlsReport = new ByteArrayOutputStream();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRXlsExporterParameter.IS_AUTO_DETECT_CELL_TYPE, Boolean.TRUE);
			exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, 
					Boolean.TRUE);
			exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, xlsReport);
			exporter.exportReport();
			byte[] bytes2 = xlsReport.toByteArray();
			response.setContentType("application/vnd.ms-excel");
			response.setContentLength(bytes2.length);
			response.setHeader("Content-disposition", "attachment; filename=\""
					+ name + ".xls\"");
			try {
				ServletOutputStream ouputStream = response.getOutputStream();
				ouputStream.write(bytes2, 0, bytes2.length);
				ouputStream.flush();
				ouputStream.close();
			}
			catch (IOException ioex) {
				if (_logger.isDebugEnabled()) {
					_logger.debug(ioex);
				}
				ioex.printStackTrace();
			}
		}
		catch (JRException e) {
			if (_logger.isDebugEnabled()) {
				_logger.debug(e);
			}
		}
		finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			}
			catch(IOException e) {
				if (_logger.isDebugEnabled()) {
					_logger.debug(e);
				}
				IxenetValidationDelegate delegate = (IxenetValidationDelegate) getBeans().
				getBean("delegate");
				delegate.record(e.getMessage(), null);
				return;
			}
		}
	}

	/**
	 * Obtiene el DataSource para generar el Reporte Anal&iacute;tico de Compra y Venta
	 * Sin Swaps en formato Pdf (Reporte 1).
	 *
	 * @param id El identificador.
	 * @return JRDataSource
	 * @see com.ixe.ods.tapestry.services.jasper.DataSourceProvider#getDataSource(String
	 */
	public JRDataSource getDataSource(String id) {
		List detallesMap = new ArrayList();
		List lista = new ArrayList();
		List l = getSicaServiceData().
		findAllDealDetallesBySectorAndDateOrderByDivisaAndRecibimosAndNoCuenta(
				DateUtils.inicioDia(getRegisterDate()), DateUtils.finDia(getRegisterDate()));
		// lista de clientes
		ParametroSica parametro = getSicaServiceData().findParametro(
				ParametroSica.ID_PERSONA_IXE_FORWARD);
		Integer idPersonaIxeForward = new Integer(parametro.getValor());
		for (Iterator iter = getSector().iterator(); iter.hasNext();) {
			DealDetalle element = (DealDetalle) iter.next();
			if (!lista.contains(element.getDeal().getCliente().getIdPersona())) {
				if (!lista.contains(element.getDeal().getFechaLiquidacion())) {
					lista.add(element.getDeal().getCliente().getIdPersona());
					lista.add(element.getDeal().getFechaLiquidacion());
				}
			}
		}
		Collections.sort(l, new Comparator() {
			public int compare(Object o1, Object o2) {
				DealDetalle dd1 = (DealDetalle) o1;
				DealDetalle dd2 = (DealDetalle) o2;
				ContratoSica cs1 = dd1.getDeal().getContratoSica();
				ContratoSica cs2 = dd2.getDeal().getContratoSica();
				//Devuelve el valor de la comprarcion por Divisa
				int comp = dd1.getDivisa().getIdDivisa().compareTo(dd2.getDivisa().getIdDivisa());
				if (comp != 0) {
					return comp;
				}
				else {
					//Devuelve el valor de la comprarcion por Sector Economico
					comp = ContratoCliente.clienteParaContratoSica(cs1).
					getSectorEconomicoDescripcion().
					compareTo(ContratoCliente.clienteParaContratoSica(cs2).
							getSectorEconomicoDescripcion());
					if (comp != 0) {
						return comp;
					}
					else {
						//Devuelve el valor de la comprarcion por 
						//Nombre Completo (Tomado de Contrato Sica)
						return ContratoCliente.clienteParaContratoSica(cs1).getNombreCompleto().
						compareTo(ContratoCliente.clienteParaContratoSica(cs2).getNombreCompleto());
					}
				}
			}
		});
		setSector(l);
		PrecioReferenciaActual precioReferencia = getSicaServiceData().findPrecioReferenciaActual();
		for (Iterator iter = getSector().iterator(); iter.hasNext();) {
			DealDetalle element = (DealDetalle) iter.next();
			if (!DealDetalle.STATUS_DET_CANCELADO.equals(element.getStatusDetalleDeal()) &&
					!DealDetalle.STATUS_DET_PROCESO_CAPTURA.equals(element.getStatusDetalleDeal())
					&& element.getDeal().getCliente() != null) {
				if (element.getDeal().getCliente().getIdPersona().intValue() != idPersonaIxeForward.
						intValue()) {
					HashMap map = new HashMap();
					map.put("sector", element.getDeal().getCliente().getSectorEconomico() != null ? 
							element.getDeal().getCliente().getSectorEconomico().
							getDescSectorBanxico() : "El cliente no tiene sector economico");
					map.put("cliente", element.getDeal().getCliente().getNombreCompleto());
					double tipoCambio;
					if (element.getFactorDivisa() != null) {
						tipoCambio = element.getFactorDivisa().doubleValue();
					}
					else {
						tipoCambio = precioReferencia.getPreRef().getPrecioCompra().doubleValue();
					}
					double monto = 0 ;
					if (Divisa.DOLAR.equals(element.getDivisa().getIdDivisa())){
						monto = element.getMonto() / 1000;
					}
					else {
						monto = (element.getMonto() * tipoCambio ) / 1000;
					}
					if (element.isRecibimos()) {
						map.put("comprado", new Double(Redondeador.redondear2Dec(monto)));
						map.put("vendido", new Double(0.00));
					}
					else {
						map.put("vendido", new Double(Redondeador.redondear2Dec(monto)));
						map.put("comprado", new Double(0.00));
					}
					map.put("fechaValor", DateUtils.inicioDia(element.getDeal().
							getFechaLiquidacion()));
					map.put("divisa", element.getDivisa().getDescripcion().toUpperCase());
					detallesMap.add(map);
				}
			}
		}
		return new ListDataSource(detallesMap);
	}

	/**
	 * Genera el Informe Anal&iacute;tico con swaps en formato Xls.
	 *
	 * @param cycle El ciclo de la p&aacute;gina.
	 */
	public void imprimirXlsDolares(IRequestCycle cycle) {
		Object parameters[] = cycle.getServiceParameters();
		String resource = (String) parameters[0];
		String name = (String) parameters[1];
		Map reportOutParams = null;
		if (parameters.length > 3) {
			reportOutParams = (Map) parameters[3];
		}
		JasperPrint jasperPrint = null;
		InputStream inputStream = null;
		try {
			JRDataSource dataSource = getDataSourceXls("DOLAR");
			BaseGlobal global = (BaseGlobal) getGlobal();
			inputStream = global.getApplicationContext().getResource(resource).getInputStream();
			JasperReport jasperReport = (JasperReport) JRLoader.loadObject(inputStream);
			jasperPrint = JasperFillManager.fillReport(jasperReport, reportOutParams, dataSource);
		}
		catch (JRException e) {
			if (_logger.isDebugEnabled()) {
				_logger.debug(e);
			}
		}
		catch (IOException e) {
			if (_logger.isDebugEnabled()) {
				_logger.debug(e);
			}
		}
		try {
			HttpServletResponse response = cycle.getRequestContext().getResponse();
			JRXlsExporter exporter = new JRXlsExporter();
			ByteArrayOutputStream xlsReport = new ByteArrayOutputStream();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRXlsExporterParameter.IS_AUTO_DETECT_CELL_TYPE, Boolean.TRUE);
			exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, 
					Boolean.TRUE);
			exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, xlsReport);
			exporter.exportReport();
			byte[] bytes2 = xlsReport.toByteArray();
			response.setContentType("application/vnd.ms-excel");
			response.setContentLength(bytes2.length);
			response.setHeader("Content-disposition", "attachment; filename=\""
					+ name + ".xls\"");
			try {
				ServletOutputStream ouputStream = response.getOutputStream();
				ouputStream.write(bytes2, 0, bytes2.length);
				ouputStream.flush();
				ouputStream.close();
			}
			catch (IOException ioex) {
				if (_logger.isDebugEnabled()) {
					_logger.debug(ioex);
				}
			}
		}
		catch (JRException e) {
			if (_logger.isDebugEnabled()) {
				_logger.debug(e);
			}
		}
		finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			}
			catch(IOException e) {
				if (_logger.isDebugEnabled()) {
					_logger.debug(e);
				}
				IxenetValidationDelegate delegate = (IxenetValidationDelegate) getBeans().
				getBean("delegate");
				delegate.record(e.getMessage(), null);
				return;
			}
		}
	}

	/**
	 * Imprime el reporte de Banxico de las dem&aacute;s divisas a excepci&oacute;n
	 * de las divisas de d&oacute;lares en
	 * excel.
	 *
	 * @param cycle El ciclo de la p&aacute;gina.
	 */
	public void imprimirXlsDivisas(IRequestCycle cycle) {
		Object parameters[] = cycle.getServiceParameters();
		String resource = (String) parameters[0];
		String name = (String) parameters[1];
		Map reportOutParams = null;
		if (parameters.length > 3) {
			reportOutParams = (Map) parameters[3];
		}
		JasperPrint jasperPrint = null;
		InputStream inputStream = null;
		try {
			JRDataSource dataSource = getDataSourceAclmeBanxico("DIVISAS");
			BaseGlobal global = (BaseGlobal) getGlobal();
			inputStream = global.getApplicationContext().getResource(resource).getInputStream();
			JasperReport jasperReport = (JasperReport) JRLoader.loadObject(inputStream);
			jasperPrint = JasperFillManager.fillReport(jasperReport, reportOutParams, dataSource);
		}
		catch (JRException e) {
			if (_logger.isDebugEnabled()) {
				_logger.debug(e);
			}
		}
		catch (IOException e) {
			if (_logger.isDebugEnabled()) {
				_logger.debug(e);
			}
			e.printStackTrace();
		}
		try {
			HttpServletResponse response = cycle.getRequestContext().getResponse();
			JRXlsExporter exporter = new JRXlsExporter();
			ByteArrayOutputStream xlsReport = new ByteArrayOutputStream();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRXlsExporterParameter.IS_AUTO_DETECT_CELL_TYPE, Boolean.TRUE);
			exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, 
					Boolean.TRUE);
			exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, xlsReport);
			exporter.exportReport();
			byte[] bytes2 = xlsReport.toByteArray();
			response.setContentType("application/vnd.ms-excel");
			response.setContentLength(bytes2.length);
			response.setHeader("Content-disposition", "attachment; filename=\""
					+ name + ".xls\"");
			try {
				ServletOutputStream ouputStream = response.getOutputStream();
				ouputStream.write(bytes2, 0, bytes2.length);
				ouputStream.flush();
				ouputStream.close();
			}
			catch (IOException ioex) {
				if (_logger.isDebugEnabled()) {
					_logger.debug(ioex);
				}
			}
		}
		catch (JRException e) {
			if (_logger.isDebugEnabled()) {
				_logger.debug(e);
			}
		}
		finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			}
			catch(IOException e) {
				if (_logger.isDebugEnabled()) {
					_logger.debug(e);
				}
				IxenetValidationDelegate delegate = (IxenetValidationDelegate) getBeans().
				getBean("delegate");
				delegate.record(e.getMessage(), null);
				return;
			}
		}
	}

	/**
	 * Realiza la b&uacute;squeda de Deals por sector y fecha. 
	 * Se valida que la b&uacute;squeda de reportes con fecha
	 * anterior a la actual, sea dentro de los &uacute;ltimos 
	 * 20 dias h&aacute;biles.
	 *
	 * @param cycle El ciclo de la p&aacute;gina.
	 */
	public void search(IRequestCycle cycle) {
		IxenetValidationDelegate delegate = (IxenetValidationDelegate) 
		getBeans().getBean("delegate");
		List l =  new ArrayList();
		Date fechaActual = getFechaOperacion();
		Calendar gc1  = new GregorianCalendar();
		gc1.setTime(fechaActual);
		Date fechaSeleccionada = DateUtils.inicioDia(getRegisterDate());
		Calendar gc2 = new GregorianCalendar();
		gc2.setTime(fechaSeleccionada);
		Date inicioDia  = DateUtils.inicioDia(getRegisterDate()); 
		Date finDia  = DateUtils.finDia(getRegisterDate());
		Calendar cal = new GregorianCalendar();
		cal.setTime(finDia);
		cal.add(Calendar.DAY_OF_YEAR, 1);
		java.sql.Date inicioDiaSql =  new java.sql.Date(inicioDia.getTime());
		java.sql.Date finDiaSql =  new java.sql.Date(cal.getTime().getTime());
		BanxicoDao banxicoDao = (BanxicoDao) getApplicationContext().getBean("banxicoDao");
		if (getRegisterDate() == null) {
			delegate.record("Debe definir la fecha de b\u00fasqueda.", null);
		}
		else {
			l = banxicoDao.findDetallesIncluyeStatusAlta(inicioDiaSql, finDiaSql);
		}
		if (l.isEmpty()) {
			delegate.record("Los criterios de b\u00fasqueda especificados" + 
					" no arrojaron resultados.", null);
		}
		setSector(l);
	}

	/**
	 * Imprime el Informe Sumarizado de Operaciones de Compra Venta en formato Pdf.
	 *
	 * @param cycle El ciclo de la p&aacute;gina.
	 */
	public void imprimirInforme(IRequestCycle cycle) {
		Object parameters[] = cycle.getServiceParameters();
		String resource = (String) parameters[0];
		String name = (String) parameters[1];
		String id = (String) parameters[2];
		Map reportOutParams = null;
		if (parameters.length > 3) {
			reportOutParams = (Map) parameters[3];
		}
		JasperPrint jasperPrint = null;
		InputStream inputStream = null;
		InputStream inputStreamImagen = null;
		try {
			BaseGlobal global = (BaseGlobal) getGlobal();
			JRDataSource dataSource = getDataSourceInforme(id);
			inputStream = global.getApplicationContext().getResource(resource).getInputStream();
			JasperReport jasperReport = (JasperReport) JRLoader.loadObject(inputStream);
			jasperPrint = JasperFillManager.fillReport(jasperReport, reportOutParams, dataSource);
			byte[] bytes = JasperExportManager.exportReportToPdf(jasperPrint);
			HttpServletResponse response = cycle.getRequestContext().getResponse();
			response.setContentType("application/pdf");
			response.setContentLength(bytes.length);
			response.setHeader("Content-Disposition", "attachment;filename=\"" + name 
					+ ".pdf" + "\"");
			ServletOutputStream ouputStream = response.getOutputStream();
			ouputStream.write(bytes, 0, bytes.length);
			ouputStream.flush();
			ouputStream.close();
		}
		catch (JRException e) {
			if (_logger.isDebugEnabled()) {
				_logger.debug(e);
			}
		}
		catch (IOException e) {
			if (_logger.isDebugEnabled()) {
				_logger.debug(e);
			}
		}
		finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
				if (inputStreamImagen != null) {
					inputStreamImagen.close();
				}
			}
			catch(IOException e) {
				if (_logger.isDebugEnabled()) {
					_logger.debug(e);
				}
			}
		}
	}

	/**
	 * Obtiene el DataSource para el Informe Anal&iacute;tico de 
	 * Operaciones de Compra Venta con Swaps.
	 *
	 * @see com.ixe.ods.tapestry.services.jasper.DataSourceProvider#getDataSource(String id)
	 * @param id El identificador.
	 * @return JRDataSource.
	 */
	public JRDataSource getDataSourceInformeAnalitico(String id) {
		List lista = new ArrayList();
		List lDetalle = getSicaServiceData().findAllDealDetallesBySectorAndDate(DateUtils.
				inicioDia(getRegisterDate()), DateUtils.finDia(getRegisterDate()));
		Posicion posicion = (Posicion) getSicaServiceData().findPosicionByIdMesaCambio(1).get(0);
		Date fechaOperacion = DateUtils.inicioDia(getRegisterDate());
		Date fechaTOM = getPizarronServiceData().getFechaTOM(fechaOperacion);
		Date fechaSPOT = getPizarronServiceData().getFechaSPOT(fechaOperacion);
		Date fecha72HR = getPizarronServiceData().getFecha72HR(fechaOperacion);
		Date fechaVFUT = getPizarronServiceData().getFechaVFUT(fechaOperacion);
		ParametroSica parametro = getSicaServiceData().findParametro(
				ParametroSica.ID_PERSONA_IXE_FORWARD);
		Integer idPersonaIxeForward = new Integer(parametro.getValor());
		/*
		 * En estas lineas de codigo se realiza un ordenamiento sobre la lista que se obtiene
		 * de getSicaServiceData.findAllDealDetallesBySectorAndDate(...).
		 *
		 * El ordenamiento toma los siguientes criterios:
		 * 1. Divisa
		 * 2. Sector Economico o Contraparte Banxico
		 * 3. Fecha Valor de la operacion
		 * 4. Nombre Corto (Tomado de Contrato Sica)
		 *
		 * */
		Collections.sort(lDetalle, new Comparator() {
			public int compare(Object o1, Object o2) {
				DealDetalle dd1 = (DealDetalle) o1;
				DealDetalle dd2 = (DealDetalle) o2;
				ContratoSica cs1 = dd1.getDeal().getContratoSica();
				ContratoSica cs2 = dd2.getDeal().getContratoSica();
				//Devuelve el valor de la comprarcion por Divisa
				int comp = dd1.getDivisa().getIdDivisa().compareTo(dd2.getDivisa().getIdDivisa());
				if (comp != 0) {
					return comp;
				}
				else {
					//Devuelve el valor de la comprarcion por Sector Economico
					comp = ContratoCliente.clienteParaContratoSica(cs1).
					getSectorEconomicoDescripcion().compareTo(ContratoCliente.
							clienteParaContratoSica(cs2).getSectorEconomicoDescripcion());
					if (comp != 0) {
						return comp;
					}
					else {
						//Devuelve el valor de la comprarcion por 
						//Nombre Corto (Tomado de Contrato Sica)
						comp = dd1.getDeal().getTipoValor().compareTo(dd2.getDeal().getTipoValor());
						if (comp != 0) {
							return comp;
						}
						else {
							//Devuelve el valor de la comprarcion por Fecha Valor
							return ContratoCliente.clienteParaContratoSica(cs1).
							getNombreCompleto().
							compareTo(ContratoCliente.clienteParaContratoSica(cs2).
									getNombreCompleto());
						}
					}
				}
			}
		});
		setSector(lDetalle);
		List detallesMap = new ArrayList();
		for (Iterator iter = lDetalle.iterator(); iter.hasNext();) {
			DealDetalle element = (DealDetalle) iter.next();
			lista.add(element);
		}
		PrecioReferenciaActual pr = getSicaServiceData().findPrecioReferenciaActual();
		for (Iterator iter = lista.iterator(); iter.hasNext();) {
			DealDetalle element = (DealDetalle) iter.next();
			setRegisterDate(DateUtils.inicioDia(getRegisterDate()));
			element.getDeal().setFechaLiquidacion(DateUtils.inicioDia(element.getDeal().
					getFechaLiquidacion()));
			if (!DealDetalle.STATUS_DET_CANCELADO.equals(element.getStatusDetalleDeal()) &&
					!DealDetalle.STATUS_DET_PROCESO_CAPTURA.equals(element.
							getStatusDetalleDeal()) &&
							element.getDeal().getCliente() != null) {
				if (element.getDeal().getCliente().getIdPersona().intValue() != 
					idPersonaIxeForward.intValue()) {
					HashMap map = new HashMap();
					map.put("idDivisa", element.getDivisa().getDescripcion().toUpperCase());
					map.put("cliente", element.getDeal().getCliente().getNombreCompleto());
					map.put("fecha", DateUtils.inicioDia(element.getDeal().getFechaCaptura()));
					map.put("fechaValor", element.getDeal().getFechaLiquidacion());
					map.put("isRecibimos", Boolean.valueOf(element.isRecibimos()));
					map.put("posicionInicial", new Double(posicion.getPosIni().
							getPosicionInicial()));
					map.put("sector", element.getDeal().getCliente().getSectorEconomico() != null ?
							" " + element.getDeal().getCliente().getSectorEconomico().
							getDescSectorBanxico() : "El cliente no tiene sector economico");
					double tipoCambio;
					if (element.getFactorDivisa() != null) {
						tipoCambio = element.getFactorDivisa().doubleValue();
					}
					else {
						tipoCambio = pr.getPreRef().getMidSpot().doubleValue();
					}
					double monto = 0;
					if (element.getDeal().getSwap() != null) {
						if (Divisa.DOLAR.equals(element.getDivisa().getIdDivisa())){
							monto  = element.getDeal().getSwap().getMonto() / 1000;
						}
						else {
							monto = (element.getMonto() * tipoCambio) / 1000;
						}
						map.put("swap", new Double(Redondeador.redondear2Dec(monto)));
						map.put("sectorCash", new Double(0.0));
						map.put("sectorTom", new Double(0.00));
						map.put("sectorSpot", new Double(0.00));
						map.put("sector72Hr", new Double(0.00));
						map.put("sectorVFut", new Double(0.00));
					}
					else {
						map.put("swap", new Double(0.0));
						if (Divisa.DOLAR.equals(element.getDivisa().getIdDivisa())){
							monto  = element.getMonto() / 1000;
						}
						else {
							monto = (element.getMonto() * tipoCambio) / 1000;
						}
						Date fechaLiquidacionDeal = DateUtils.inicioDia(element.getDeal().
								getFechaLiquidacion());
						if (fechaLiquidacionDeal.compareTo(fechaOperacion) == 0) {
							map.put("sectorCash", new Double(Redondeador.redondear2Dec(monto)));
							map.put("sectorTom", new Double(0.00));
							map.put("sectorSpot", new Double(0.00));
							map.put("sector72Hr", new Double(0.00));
							map.put("sectorVFut", new Double(0.00));
						}
						else if (fechaLiquidacionDeal.compareTo(fechaTOM) == 0) {
							map.put("sectorTom", new Double(Redondeador.redondear2Dec(monto)));
							map.put("sectorCash", new Double(0.00));
							map.put("sectorSpot", new Double(0.00));
							map.put("sector72Hr", new Double(0.00));
							map.put("sectorVFut", new Double(0.00));
						}
						else if (fechaLiquidacionDeal.compareTo(fechaSPOT) == 0) {
							map.put("sectorSpot", new Double(Redondeador.redondear2Dec(monto)));
							map.put("sectorCash", new Double(0.00));
							map.put("sectorTom", new Double(0.00));
							map.put("sector72Hr", new Double(0.00));
							map.put("sectorVFut", new Double(0.00));
						}
						else if (fechaLiquidacionDeal.compareTo(fecha72HR) == 0) {
							map.put("sector72Hr", new Double(Redondeador.redondear2Dec(monto)));
							map.put("sectorCash", new Double(0.00));
							map.put("sectorTom", new Double(0.00));
							map.put("sectorSpot", new Double(0.00));
							map.put("sectorVFut", new Double(0.00));
						}
						else if (fechaLiquidacionDeal.compareTo(fechaVFUT) == 0) {
							map.put("sectorVFut", new Double(Redondeador.redondear2Dec(monto)));
							map.put("sectorCash", new Double(0.00));
							map.put("sectorTom", new Double(0.00));
							map.put("sectorSpot", new Double(0.00));
							map.put("sector72Hr", new Double(0.00));
						}
					}
					detallesMap.add(map);
				}
			}
		}
		return new ListDataSource(detallesMap);
	}

	/**
	 * Imprime el reporte de Banxico en PDF.
	 *
	 * @param cycle El ciclo de la p&aacute;gina.
	 */
	public void imprimirInformeAnalitico(IRequestCycle cycle) {
		Object parameters[] = cycle.getServiceParameters();
		String resource = (String) parameters[0];
		String name = (String) parameters[1];
		String id = (String) parameters[2];
		Map reportOutParams = null;
		if (parameters.length > 3) {
			reportOutParams = (Map) parameters[3];
		}
		JasperPrint jasperPrint = null;
		InputStream inputStream = null;
		try {
			JRDataSource dataSource = getDataSourceInformeAnalitico(id);
			BaseGlobal global = (BaseGlobal) getGlobal();
			inputStream = global.getApplicationContext().getResource(resource).getInputStream();
			JasperReport jasperReport = (JasperReport) JRLoader.loadObject(inputStream);
			jasperPrint = JasperFillManager.fillReport(jasperReport, reportOutParams, dataSource);
		}
		catch (JRException e) {
			if (_logger.isDebugEnabled()) {
				_logger.debug(e);
			}
		}
		catch (IOException e) {
			if (_logger.isDebugEnabled()) {
				_logger.debug(e);
			}
		}
		try {
			byte[] bytes = JasperExportManager.exportReportToPdf(jasperPrint);
			HttpServletResponse response = cycle.getRequestContext()
			.getResponse();
			response.setContentType("application/pdf");
			response.setContentLength(bytes.length);
			response.setHeader("Content-Disposition", "attachment;filename=\"" + name 
			+ ".pdf" + "\"");
			try {
				ServletOutputStream ouputStream = response.getOutputStream();
				ouputStream.write(bytes, 0, bytes.length);
				ouputStream.flush();
				ouputStream.close();
			}
			catch (IOException ioex) {
				if (_logger.isDebugEnabled()) {
					_logger.debug(ioex);
				}
			}
		}
		catch (JRException e) {
			if (_logger.isDebugEnabled()) {
				_logger.debug(e);
			}
		}
		finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			}
			catch(IOException e) {
				if (_logger.isDebugEnabled()) {
					_logger.debug(e);
				}
				IxenetValidationDelegate delegate = (IxenetValidationDelegate) getBeans().
				getBean("delegate");
				delegate.record(e.getMessage(), null);
				return;
			}
		}
	}

	/**
	 * Imprime el reporte Anal&iacute;tico de Operaciones sin Swaps en formato PDF.
	 *
	 * @param cycle El ciclo de la p&aacute;gina.
	 */
	public void imprimirReporteAnaliticoSinSwapsPdf(IRequestCycle cycle) {
		Object parameters[] = cycle.getServiceParameters();
		String resource = (String) parameters[0];
		String name = (String) parameters[1];
		Map reportOutParams = null;
		if (parameters.length > 3) {
			reportOutParams = (Map) parameters[3];
		}
		JasperPrint jasperPrint = null;
		InputStream inputStream = null;
		try {
			JRDataSource dataSource = getDataSource("");
			BaseGlobal global = (BaseGlobal) getGlobal();
			inputStream = global.getApplicationContext().getResource(resource).getInputStream();
			JasperReport jasperReport = (JasperReport) JRLoader.loadObject(inputStream);
			jasperPrint = JasperFillManager.fillReport(jasperReport, reportOutParams, dataSource);
		}
		catch (JRException e) {
			if (_logger.isDebugEnabled()) {
				_logger.debug(e);
			}
		}
		catch (IOException e) {
			if (_logger.isDebugEnabled()) {
				_logger.debug(e);
			}
		}
		try {
			byte[] bytes = JasperExportManager.exportReportToPdf(jasperPrint);
			HttpServletResponse response = cycle.getRequestContext()
			.getResponse();
			response.setContentType("application/pdf");
			response.setContentLength(bytes.length);
			response.setHeader("Content-Disposition", "attachment;filename=\"" + name 
					+ ".pdf" + "\"");
			try {
				ServletOutputStream ouputStream = response.getOutputStream();
				ouputStream.write(bytes, 0, bytes.length);
				ouputStream.flush();
				ouputStream.close();
			}
			catch (IOException ioex) {
				if (_logger.isDebugEnabled()) {
					_logger.debug(ioex);
				}
			}
		}
		catch (JRException e) {
			if (_logger.isDebugEnabled()) {
				_logger.debug(e);
			}
		}
		finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			}
			catch(IOException e) {
				if (_logger.isDebugEnabled()) {
					_logger.debug(e);
				}
				IxenetValidationDelegate delegate = (IxenetValidationDelegate) getBeans().
				getBean("delegate");
				delegate.record(e.getMessage(), null);
				return;
			}
		}
	}

	/**
	 * Imprime el reporte Anal&iacute;tico Sin Swaps en formato de Excel.
	 *
	 * @param cycle El ciclo de la p&aacute;gina
	 */
	public void imprimirReporteAnaliticoSinSwapsXls(IRequestCycle cycle) {
		Object parameters[] = cycle.getServiceParameters();
		String resource = (String) parameters[0];
		String name = (String) parameters[1];
		Map reportOutParams = null;
		if (parameters.length > 3) {
			reportOutParams = (Map) parameters[3];
		}
		JasperPrint jasperPrint = null;
		InputStream inputStream = null;
		try {
			JRDataSource dataSource = getDataSource("");
			BaseGlobal global = (BaseGlobal) getGlobal();
			inputStream = global.getApplicationContext().getResource(resource).getInputStream();
			JasperReport jasperReport = (JasperReport) JRLoader.loadObject(inputStream);
			jasperPrint = JasperFillManager.fillReport(jasperReport, reportOutParams, dataSource);
		}
		catch (JRException e) {
			if (_logger.isDebugEnabled()) {
				_logger.debug(e);
			}
		}
		catch (IOException e) {
			if (_logger.isDebugEnabled()) {
				_logger.debug(e);
			}
		}
		try {
			HttpServletResponse response = cycle.getRequestContext().getResponse();
			JRXlsExporter exporter = new JRXlsExporter();
			ByteArrayOutputStream xlsReport = new ByteArrayOutputStream();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRXlsExporterParameter.IS_AUTO_DETECT_CELL_TYPE, Boolean.TRUE);
			exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, 
					Boolean.TRUE);
			exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, xlsReport);
			exporter.exportReport();
			byte[] bytes2 = xlsReport.toByteArray();
			response.setContentType("application/vnd.ms-excel");
			response.setContentLength(bytes2.length);
			response.setHeader("Content-disposition", "attachment; filename=\"" + name + ".xls\"");
			try {
				ServletOutputStream ouputStream = response.getOutputStream();
				ouputStream.write(bytes2, 0, bytes2.length);
				ouputStream.flush();
				ouputStream.close();
			}
			catch (IOException ioex) {
				if (_logger.isDebugEnabled()) {
					_logger.debug(ioex);
				}
			}
		}
		catch (JRException e) {
			if (_logger.isDebugEnabled()) {
				_logger.debug(e);
			}
		}
		finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			}
			catch(IOException e) {
				if (_logger.isDebugEnabled()) {
					_logger.debug(e);
				}
				IxenetValidationDelegate delegate = (IxenetValidationDelegate) getBeans().
				getBean("delegate");
				delegate.record(e.getMessage(), null);
				return;
			}
		}
	}

	/**
	 * Imprime el reporte Sumarizado de Compras y Ventas con Swaps
	 * en formato de Excel.
	 *
	 * @param cycle El ciclo de la p&aacute;gina
	 */
	public void imprimirInformeComprasVentasXls(IRequestCycle cycle) {
		Object parameters[] = cycle.getServiceParameters();
		String resource = (String) parameters[0];
		String name = (String) parameters[1];
		String id = (String) parameters[2];
		Map reportOutParams = null;
		if (parameters.length > 3) {
			reportOutParams = (Map) parameters[3];
		}
		JasperPrint jasperPrint = null;
		InputStream inputStream = null;
		InputStream inputStreamImagen = null;
		try {
			BaseGlobal global = (BaseGlobal) getGlobal();
			JRDataSource dataSource = getDataSourceInforme(id);
			inputStream = global.getApplicationContext().getResource(resource).getInputStream();
			JasperReport jasperReport = (JasperReport) JRLoader.loadObject(inputStream);
			jasperPrint = JasperFillManager.fillReport(jasperReport, reportOutParams, dataSource);
			HttpServletResponse response = cycle.getRequestContext().getResponse();
			JRXlsExporter exporter = new JRXlsExporter();
			ByteArrayOutputStream xlsReport = new ByteArrayOutputStream();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRXlsExporterParameter.IS_AUTO_DETECT_CELL_TYPE, Boolean.TRUE);
			exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, 
					Boolean.TRUE);
			exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, xlsReport);
			exporter.exportReport();
			byte[] bytes2 = xlsReport.toByteArray();
			response.setContentType("application/vnd.ms-excel");
			response.setContentLength(bytes2.length);
			response.setHeader("Content-disposition", "attachment; filename=\"" + name + ".xls\"");
			ServletOutputStream ouputStream = response.getOutputStream();
			ouputStream.write(bytes2, 0, bytes2.length);
			ouputStream.flush();
			ouputStream.close();
		}
		catch (JRException e) {
			if (_logger.isDebugEnabled()) {
				_logger.debug(e);
			}
		}
		catch (IOException e) {
			if (_logger.isDebugEnabled()) {
				_logger.debug(e);
			}
		}
		finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
				if (inputStreamImagen != null) {
					inputStreamImagen.close();
				}
			}
			catch(IOException e) {
				if (_logger.isDebugEnabled()) {
					_logger.debug(e);
				}
				IxenetValidationDelegate delegate = (IxenetValidationDelegate) getBeans().
				getBean("delegate");
				delegate.record(e.getMessage(), null);
				return;
			}
		}
	}
	
	/**
	 * Listener que genera el archivo en excel del Reporte Aclme X
	 * con base en los registros obtenidos despues de la consulta
	 * de los tipos de cambio para el Teller.
	 * 
	 * @param cycle El ciclo de la pagina.
	 */
	public void generarAclmeX(IRequestCycle cycle) {
		IxenetValidationDelegate delegate = (IxenetValidationDelegate) 
		getBeans().getBean("delegate");
		try {
			String registros = generarListaAclmeX();
			getTitulos().add("");
			getExpresiones().add("registro");
			GeneradorTxt.generarEscribir(cycle.getRequestContext().getResponse(), 
					getNombreArchivo(), registros);
		}
		catch (SicaException e) {
			delegate.record(e.getMessage(), null);
			if (_logger.isDebugEnabled()) {
				_logger.debug(e);
			}
		}
	}
	
	/**
	 * Obtiene la lista de registros para el reporte Aclme X. Los registros se componen de
	 * una cadena con los siguientes criterios:<br> 
	 * 
	 * <li>Clave Banxico para Ixe.</li>
	 * <li>Clave Filial.</li>
	 * <li>Fecha.</li>
	 * <li>Concepto.</li>
	 * <li>Fecha Valor (Todos son CASH para el Teller por lo que siempre es 000).</li>
	 * <li>Tipo de Cambio.</li>
	 * 
	 * @return List
	 */
	public String generarListaAclmeX() throws SicaException {
		SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");
		List tct = getSicaServiceData().findTiposCambioTellerByFecha(getRegisterDate());
		obtenerConceptos(tct);
		StringBuffer buffer = new StringBuffer();
		for (Iterator it = getConceptosAclmeX().keySet().iterator(); it.hasNext(); ) {
			String contenido ="";
			String llave = (String) it.next();
			contenido += "040032000000";
			contenido += DATE_FORMAT.format(getRegisterDate());
			contenido += llave;
			contenido += "000";
			String tc = "" + getCurrencyFormat().format(((Double) 
					getConceptosAclmeX().get(llave)).doubleValue());
			contenido += tc + "\r\n";
			buffer.append(contenido);
		}
		if (buffer.toString().length() < 1) {
			throw new SicaException("No hay registros de tipos de cambio de sucursales " +
					"para la fecha seleccionada. No es posible generar el reporte ACLME X.",  
					null);
		}
    	return buffer.toString();
	}	
	
	/**
	 * Calcula y define los tipos de cambio para los conceptos del
	 * reporte Aclme X.
	 * 
	 * @param tiposCambioTeller Lista con los tipos de cambio del teller.
	 */
	public void obtenerConceptos(List tiposCambioTeller) {
		double mayDifInicioCpa = 0;
		double mayDifInicioVta = 0;
		double menDifInicioCpa = 0;
		double menDifInicioVta = 0;
		double tcMinInicioCpa = 0;
		double tcMinInicioVta = 0;
		double tcMaxInicioCpa = 0;
		double tcMaxInicioVta = 0;
		double mayDifCierreCpa = 0;
		double mayDifCierreVta = 0;
		double menDifCierreCpa = 0;
		double menDifCierreVta = 0;
		double tcMinCierreCpa = 0;
		double tcMinCierreVta = 0;
		double tcMaxCierreCpa = 0;
		double tcMaxCierreVta = 0;
		for (Iterator it = tiposCambioTeller.iterator(); it.hasNext(); ) {
			TcMinMaxTeller tcTeller = (TcMinMaxTeller) it.next();
			if (!tcTeller.isCierre()) {
				double mayorDifTeller = tcTeller.mayorDiferencialCpaVta();
				double menorDifTeller = tcTeller.menorDiferencialCpaVta();
				if (mayorDifTeller > mayDifInicioCpa) {
					getConceptosAclmeX().put("30001", new Double(Redondeador.redondear6Dec(
							tcTeller.getTipoCambioMinCpa())));
					mayDifInicioCpa = mayorDifTeller;
				}
				if (mayorDifTeller > mayDifInicioVta) {
					getConceptosAclmeX().put("30011", new Double(Redondeador.redondear6Dec(
							tcTeller.getTipoCambioMaxVta())));
					mayDifInicioVta = mayorDifTeller;
				}
				if (menorDifTeller < menDifInicioCpa || menDifInicioCpa == 0) {
					getConceptosAclmeX().put("30021", new Double(Redondeador.redondear6Dec(
							tcTeller.getTipoCambioMaxCpa())));
					menDifInicioCpa = menorDifTeller; 
				}
				if (menorDifTeller < menDifInicioVta || menDifInicioVta == 0) {
					getConceptosAclmeX().put("30031", new Double(Redondeador.redondear6Dec(
							tcTeller.getTipoCambioMinVta())));
					menDifInicioVta = menorDifTeller; 
				}
				if (tcTeller.getTipoCambioMinCpa() < tcMinInicioCpa || tcMinInicioCpa == 0) {
					getConceptosAclmeX().put("30041", new Double(Redondeador.redondear6Dec(
							tcTeller.getTipoCambioMinCpa())));
					tcMinInicioCpa = tcTeller.getTipoCambioMinCpa();
				}
				if (tcTeller.getTipoCambioMaxCpa() > tcMaxInicioCpa) {
					getConceptosAclmeX().put("30051", new Double(Redondeador.redondear6Dec(
							tcTeller.getTipoCambioMaxCpa())));
					tcMaxInicioCpa = tcTeller.getTipoCambioMaxCpa();
				}
				if (tcTeller.getTipoCambioMinVta() < tcMinInicioVta || tcMinInicioVta == 0) {
					getConceptosAclmeX().put("30061", new Double(Redondeador.redondear6Dec(
							tcTeller.getTipoCambioMinVta())));
					tcMinInicioVta = tcTeller.getTipoCambioMinVta();
				}
				if (tcTeller.getTipoCambioMaxVta() > tcMaxInicioVta) {
					getConceptosAclmeX().put("30071", new Double(Redondeador.redondear6Dec(
							tcTeller.getTipoCambioMaxVta())));
					tcMaxInicioVta = tcTeller.getTipoCambioMaxVta();
				}
			}
			else {
				double mayorDifTeller = tcTeller.mayorDiferencialCpaVta();
				double menorDifTeller = tcTeller.menorDiferencialCpaVta();
				if (mayorDifTeller > mayDifCierreCpa) {
					getConceptosAclmeX().put("30081", new Double(Redondeador.redondear6Dec(
							tcTeller.getTipoCambioMinCpa())));
					mayDifCierreCpa = mayorDifTeller;
				}
				if (mayorDifTeller > mayDifCierreVta) {
					getConceptosAclmeX().put("30091", new Double(Redondeador.redondear6Dec(
							tcTeller.getTipoCambioMaxVta())));
					mayDifCierreVta = mayorDifTeller;
				}
				if (menorDifTeller < menDifCierreCpa || menDifCierreCpa == 0) {
					getConceptosAclmeX().put("30101", new Double(Redondeador.redondear6Dec(
							tcTeller.getTipoCambioMaxCpa())));
					menDifCierreCpa = menorDifTeller;
				}
				if (menorDifTeller < menDifCierreVta || menDifCierreVta == 0) {
					getConceptosAclmeX().put("30111", new Double(Redondeador.redondear6Dec(
							tcTeller.getTipoCambioMinVta())));
					menDifCierreVta = menorDifTeller;
				}
				if (tcTeller.getTipoCambioMinCpa() < tcMinCierreCpa || tcMinCierreCpa == 0) {
					getConceptosAclmeX().put("30121", new Double(Redondeador.redondear6Dec(
							tcTeller.getTipoCambioMinCpa())));
					tcMinCierreCpa = tcTeller.getTipoCambioMinCpa();
				}
				if (tcTeller.getTipoCambioMaxCpa() > tcMaxCierreCpa) {
					getConceptosAclmeX().put("30131", new Double(Redondeador.redondear6Dec(
							tcTeller.getTipoCambioMaxCpa())));
					tcMaxCierreCpa = tcTeller.getTipoCambioMaxCpa();
				}
				if (tcTeller.getTipoCambioMinVta() < tcMinCierreVta || tcMinCierreVta == 0) {
					getConceptosAclmeX().put("30141", new Double(Redondeador.redondear6Dec(
							tcTeller.getTipoCambioMinVta())));
					tcMinCierreVta = tcTeller.getTipoCambioMinVta();
				}
				if (tcTeller.getTipoCambioMaxVta() > tcMaxCierreVta) {
					getConceptosAclmeX().put("30151", new Double(Redondeador.redondear6Dec(
							tcTeller.getTipoCambioMaxVta())));
					tcMaxCierreVta = tcTeller.getTipoCambioMaxVta();
				}
			}
		}

	}
	
	/**
	 * Asigna los conceptos para los tipos de cambio para el reporte Aclme X. En total
	 * son 16 conceptos los cuales son: <br>
	 * 
	 * <li>Tipos de cambio para compra y venta a la apertura con mayor spread entre el tipo de 
	 * cambio de Compra y venta.</li>
	 * <li>Tipos de cambio para compra y venta a la apertura con menor spread entre el tipo de 
	 * cambio de Compra y venta.</li>
	 * <li>Tipos de cambio minimo y maximo para compra a la apertura.</li>
	 * <li>Tipos de cambio minimo y maximo para venta a la apertura.</li>
	 * 
	 * <li>Tipos de cambio para compra y venta al cierre con mayor spread entre el tipo de 
	 * cambio de Compra y venta.</li>
	 * <li>Tipos de cambio para compra y venta al cierre con menor spread entre el tipo de 
	 * cambio de Compra y venta.</li>
	 * <li>Tipos de cambio minimo y maximo para compra al cierre.</li>
	 * <li>Tipos de cambio minimo y maximo para venta al cierre.</li>
	 * 
	 */
	public void asignarConceptos() {
		getConceptosAclmeX().put("30001", new Double(0.0));
		getConceptosAclmeX().put("30011", new Double(0.0));
		getConceptosAclmeX().put("30021", new Double(0.0));
		getConceptosAclmeX().put("30031", new Double(0.0));
		getConceptosAclmeX().put("30041", new Double(0.0));
		getConceptosAclmeX().put("30051", new Double(0.0));
		getConceptosAclmeX().put("30061", new Double(0.0));
		getConceptosAclmeX().put("30071", new Double(0.0));
		getConceptosAclmeX().put("30081", new Double(0.0));
		getConceptosAclmeX().put("30091", new Double(0.0));
		getConceptosAclmeX().put("30101", new Double(0.0));
		getConceptosAclmeX().put("30111", new Double(0.0));
		getConceptosAclmeX().put("30121", new Double(0.0));
		getConceptosAclmeX().put("30131", new Double(0.0));
		getConceptosAclmeX().put("30141", new Double(0.0));
		getConceptosAclmeX().put("30151", new Double(0.0));
	}
	
	/**
	 * Regresa el arreglo con los estados de operaci&oacute;n 
	 * normal y operaci&oacute;n restringida.
	 *
	 * @return int[].
	 */
	protected int[] getEstadosValidos() {
		return new int[] {Estado.ESTADO_OPERACION_NORMAL, Estado.ESTADO_OPERACION_RESTRINGIDA,
				Estado.ESTADO_OPERACION_VESPERTINA, Estado.ESTADO_OPERACION_NOCTURNA,
				Estado.ESTADO_GENERACION_CONTABLE, Estado.ESTADO_FIN_LIQUIDACIONES};
	}
	
	/**
	 * Mapa con los conceptos del reporte.
	 * 
	 * @return LinkedHashMap
	 */
	public abstract LinkedHashMap getConceptosAclmeX();
	
	
	/**
	 * Regresa el valor de registerDate.
	 *
	 * @return Date.
	 */
	public abstract Date getRegisterDate();

	/**
	 * Fija el valor de registerdate.
	 *
	 * @param registerdate El valor a asignar.
	 */
	public abstract void setRegisterDate(Date registerdate);

	/**
	 * Regresa el valor de sector.
	 *
	 * @return List.
	 */
	public abstract List getSector();

	/**
	 * Fija el valor de sector.
	 *
	 * @param sector El valor a asignar.
	 */
	public abstract void setSector(List sector);
	
	/**
	 * Obtiene el valor para el dataSource
	 * 
	 * @return List
	 */
	public abstract List getDataSource();

	/**
	 * Asinga el valor para el DataSource.
	 * 
	 * @param dataSource El valor para el DataSource.
	 */
	public abstract void setDataSource(List dataSource);

	/**
	 * Obtiene el nombre del archivo.
	 * 
	 * @return String
	 */
	public abstract String getNombreArchivo();
	
	/**
	 * Asigna el valor para el nombre del archivo.
	 * 
	 * @param nombreArchivo El valor para el nombre del archivo
	 */
	public abstract void setNombreArchivo(String nombreArchivo);
	
	/**
	 * Obtiene la lista de titulos.
	 * 
	 * @return List
	 */
	public abstract List getTitulos();

	/**
	 * Obtiene la lista de expresiones.
	 * 
	 * @return List
	 */
	public abstract List getExpresiones();

	/**
	 * Constante que define el valor para el sector economico para Casas de Bolsa.
	 */
	private static final String SECTOR_CASAS_DE_BOLSA = "CASAS DE BOLSA";

	/**
	 * Constante que define el valor para el sector economico para Empresas.
	 */
	private static final String SECTOR_EMPRESAS = "EMPRESAS";

	/**
	 * Constante que define el valor para el sector economico para Otros Intermediarios.
	 */
	private static final String SECTOR_OTR_INTERM = "OTROS INTERMEDIARIOS";

}
