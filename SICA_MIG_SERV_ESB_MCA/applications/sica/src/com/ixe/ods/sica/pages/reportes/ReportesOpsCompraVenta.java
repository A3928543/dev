package com.ixe.ods.sica.pages.reportes;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.form.StringPropertySelectionModel;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.util.JRLoader;

import com.ixe.isis.tapestry.IxenetValidationDelegate;
import com.ixe.ods.bup.model.EmpleadoIxe;
import com.ixe.ods.bup.model.Sucursal;
import com.ixe.ods.jasper.ListDataSource;
import com.ixe.ods.sica.FacultySystem;
import com.ixe.ods.sica.SupportEngine;
import com.ixe.ods.sica.Visit;
import com.ixe.ods.sica.dao.JdbcReportesDaoImpl;
import com.ixe.ods.sica.model.Canal;
import com.ixe.ods.sica.model.Divisa;
import com.ixe.ods.sica.model.Division;
import com.ixe.ods.sica.model.LogAuditoria;
import com.ixe.ods.sica.model.ParametroSica;
import com.ixe.ods.sica.model.Plaza;
import com.ixe.ods.sica.model.SucursalZona;
import com.ixe.ods.sica.model.Zona;
import com.ixe.ods.sica.pages.SicaPage;
import com.ixe.ods.sica.utils.DateUtils;
import com.ixe.ods.tapestry.BaseGlobal;
import com.ixe.ods.tapestry.services.jasper.DataSourceProvider;
import com.legosoft.hibernate.type.SiNoType;
import com.legosoft.tapestry.model.RecordSelectionModel;

/**
 * P&aacute;gina que permite al usuario buscar e imprimir los reportes de
 * diarios
 * 
 * @author Pedro M. Espinosa (espinosapm)
 */
public abstract class ReportesOpsCompraVenta extends SicaPage implements DataSourceProvider {
	
	/**
	 * Asigna los valores de los componentes de la p&aacute;gina.
	 * 
	 * @param cycle El ciclo de la p&acute;gina.
	 */
	public void activate(IRequestCycle cycle) {
		//setDetallesTable(new ArrayList());
		setDetalles(new ArrayList());
		super.activate(cycle);
		setModo((String) cycle.getServiceParameters()[0]);
		List divisasFrecuentes = getSicaServiceData().findDivisasFrecuentes();
		setDivisas(divisasFrecuentes);
		//Combo promotores
		Visit visit = (Visit) getVisit();
		List promotoresTmp = new ArrayList();
		List promotores = new ArrayList();

		/*
		 * Si es un usuario tipo staff, se cargan todos los promotores de la jerarquía del SICA,PersonaFisica
		 * de lo contrario, se cargan solo aquellos promotores pertenecientes a la jerarquía del usuario actual 
		 */
		if(isStaff()) {
			promotoresTmp = getSicaServiceData().findAllPromotoresSICA(((SupportEngine) getEngine()).getApplicationName());
		} else {
			promotoresTmp = getPromotoresJerarquia();
		}
		
		//Llenando el Combo de Promotores
		if (promotoresTmp.size() > 0) {
			HashMap hm = new HashMap();
			hm.put("idPersona", new Integer(0));
			hm.put("nombreCompleto", "TODOS");
			promotores.add(hm);
			for (Iterator it = promotoresTmp.iterator(); it.hasNext();) {
				hm = new HashMap();
				EmpleadoIxe promotor = (EmpleadoIxe) it.next();
				hm.put("idPersona", promotor.getIdPersona());
				hm.put("nombreCompleto", promotor.getNombreCompleto());
				promotores.add(hm);
			}
			setPromotor((HashMap) promotores.get(0));
		}
		setPromotoresList(promotores);
		setProductos(null);
		setDivisaSeleccionada(null);
		setOperacionSeleccionada(null);
		setRegisterDate(null);
		setRegisterDateHasta(null);
		// se inicializa el combo de origen operación
		setOrigenOperacionList(getOrigenOperacionOptionCollection());
		
		// se inicializa el combo de Division, Plaza, Zona y Sucursal
		setDivisionList(getDivisionesOptionCollection());
		HashMap firstDivision = (HashMap) getDivisionList().get(0);
		setDivisionSeleccionada(firstDivision);
		
		setPlazaList(getPlazasOptionCollection(JdbcReportesDaoImpl.DIVISIONES_ALL));
		setZonaList(getZonasOptionCollection(JdbcReportesDaoImpl.PLAZAS_ALL));
		setSucursalList(getSucursalesOptionCollection(JdbcReportesDaoImpl.ZONAS_ALL));
		
		setSucursalSeleccionada((HashMap)getSucursalList().get(0));
		
		// se habilita o inhabilita el filtro "Hasta" del rango de fechas según horario de operación
		setDateRangeFilterAllowed(isDateRangeAllowed());
	}
	
	
	
	/**
	 * Genera la lista de opciones para el combo de Origen de operaciones agregando canales específicos, separando los de promoción
	 * de los de Sucursales
	 * 
	 * @return Lista de opciones para el combo de Origen de Operaciones
	 */
	private List getOrigenOperacionOptionCollection() { 
		// Lista para contener las opciones finales a retornar
		List canalesModelTmp = new ArrayList();
		
		// Se obtienen todos los canales ordenados por sucursal y nombre de canal
		List canales = getSicaServiceData().findAllCanalesOrdered();
		
		// Opciones generales
		Map optAll = new HashMap();
		optAll.put(CMB_BOX_VAL, JdbcReportesDaoImpl.ORIG_OPER_ALL);
		optAll.put(CMB_BOX_LBL, CMB_OPT_TODAS);
		
		Map optSoloCanalesSucursales = new HashMap();
		optSoloCanalesSucursales.put(CMB_BOX_VAL, JdbcReportesDaoImpl.ORIG_OPER_SOLO_CNLS_SUC);
		optSoloCanalesSucursales.put(CMB_BOX_LBL, ORIG_OP_OPT_LBL_SOLO_CNLS_SUC);
		
		Map optSoloCanalesPromocion = new HashMap();
		optSoloCanalesPromocion.put(CMB_BOX_VAL, JdbcReportesDaoImpl.ORIG_OPER_SOLO_CNLS_PRM);
		optSoloCanalesPromocion.put(CMB_BOX_LBL, ORIG_OP_OPT_LBL_SOLO_CNLS_PRM);
		
		// opciones solo con función de encabezado
		Map pseudoOptCanalesEspecificosSucursales = new HashMap();
		pseudoOptCanalesEspecificosSucursales.put(CMB_BOX_VAL, ORIG_OP_PSD_OPT_CNLS_SPEC_SUC);
		pseudoOptCanalesEspecificosSucursales.put(CMB_BOX_LBL, ORIG_OP_PSD_OPT_LBL_CNLS_SPEC_SUC);
		
		Map pseudoOptCanalesEspecificosPromocion = new HashMap();
		pseudoOptCanalesEspecificosPromocion.put(CMB_BOX_VAL, ORIG_OP_PSD_OPT_CNLS_SPEC_PRM);
		pseudoOptCanalesEspecificosPromocion.put(CMB_BOX_LBL, ORIG_OP_PSD_OPT_LBL_CNLS_SPEC_PRM);
		
		// se agregan opciones generales
		canalesModelTmp.add(optAll);
		canalesModelTmp.add(optSoloCanalesSucursales);
		canalesModelTmp.add(optSoloCanalesPromocion);
		
		// Se separan los canales especificos obtenidos en dos grupos: Sucursales y Promoción 
		List canalesSucursales = new ArrayList();
		List canalesPromocion = new ArrayList();
		for(Iterator it = canales.iterator(); it.hasNext();) {
			Canal currentCanal = (Canal)it.next();
			Map currentOptCanal = new HashMap();
			currentOptCanal.put(CMB_BOX_VAL, currentCanal.getIdCanal());
			currentOptCanal.put(CMB_BOX_LBL, "\t" + currentCanal.getNombre());
			if(currentCanal.getSucursal() != null) {
				canalesSucursales.add(currentOptCanal);
			} else {
				canalesPromocion.add(currentOptCanal);
			}
		}
		
		// se agregan canales especificos anteponiendo el correspondiente encabezado
		canalesModelTmp.add(pseudoOptCanalesEspecificosSucursales);
		canalesModelTmp.addAll(canalesSucursales);
		canalesModelTmp.add(pseudoOptCanalesEspecificosPromocion);
		canalesModelTmp.addAll(canalesPromocion);
		
		return canalesModelTmp;
	}
	
	/**
	 * Obtiene las opciones para llenar el combo de Division
	 * @return
	 */
	private List getDivisionesOptionCollection() {
		List divisionesOptionCollection = new ArrayList();
		
		Map optAll = new HashMap();
		optAll.put(CMB_BOX_VAL, JdbcReportesDaoImpl.DIVISIONES_ALL);
		optAll.put(CMB_BOX_LBL, CMB_OPT_TODAS);
		divisionesOptionCollection.add(optAll);
		
		List divisiones = getSicaServiceData().findDivisiones();
		
		for(Iterator it = divisiones.iterator(); it.hasNext();) {
			Division currentDivision = (Division) it.next();
			Map currentOpt = new HashMap();
			currentOpt.put(CMB_BOX_VAL, currentDivision.getIdDivision());
			currentOpt.put(CMB_BOX_LBL, currentDivision.getNombre());
			divisionesOptionCollection.add(currentOpt);
		}
		
		return divisionesOptionCollection;
	}
	
	/**
	 * Obtiene las opciones para el combo de Plaza a partir de una division dada
	 * @param idDivision
	 * @return
	 */
	private List getPlazasOptionCollection(Integer idDivision) {
		List plazasOptionCollection = new ArrayList();
		
		Map optAll = new HashMap();
		optAll.put(CMB_BOX_VAL, JdbcReportesDaoImpl.PLAZAS_ALL);
		optAll.put(CMB_BOX_LBL, CMB_OPT_TODAS);
		plazasOptionCollection.add(optAll);
		
		if (idDivision.equals(JdbcReportesDaoImpl.DIVISIONES_ALL)) {
			return plazasOptionCollection;
		}
		
		List plazas = getSicaServiceData().findPlazasByDivision(idDivision);
		
		for(Iterator it = plazas.iterator(); it.hasNext();) {
			Plaza currentPlaza = (Plaza) it.next();
			Map currentOpt = new HashMap();
			currentOpt.put(CMB_BOX_VAL, currentPlaza.getIdPlaza());
			currentOpt.put(CMB_BOX_LBL, currentPlaza.getNombre());
			plazasOptionCollection.add(currentOpt);
		}
		
		return plazasOptionCollection;
	}
	
	/**
	 * Obtiene las opciones para el combo de Zona dada una plaza
	 * @param idPlaza
	 * @return
	 */
	private List getZonasOptionCollection(Integer idPlaza) {
		List zonasOptionCollection = new ArrayList();
		Map optAll = new HashMap();
		optAll.put(CMB_BOX_VAL, JdbcReportesDaoImpl.ZONAS_ALL);
		optAll.put(CMB_BOX_LBL, CMB_OPT_TODAS);
		zonasOptionCollection.add(optAll);
		
		if(idPlaza.equals(JdbcReportesDaoImpl.PLAZAS_ALL)) {
			return zonasOptionCollection;
		}
		
		List zonas = getSicaServiceData().findZonasByPlaza(idPlaza);
		
		for(Iterator it = zonas.iterator(); it.hasNext();) {
			Zona currentZona = (Zona) it.next();
			Map currentOpt = new HashMap();
			currentOpt.put(CMB_BOX_VAL, currentZona.getIdZona());
			currentOpt.put(CMB_BOX_LBL, currentZona.getNombre());
			zonasOptionCollection.add(currentOpt);
		}
		
		return zonasOptionCollection;
	}
	
	/**
	 * Obtiene todas las sucursales para el combo de Sucursal
	 * @return
	 */
	private List getSucursalesOptionCollection() {
		List sucursalesOptionCollection = new ArrayList();
		Map optAll = new HashMap();
		optAll.put(CMB_BOX_VAL, JdbcReportesDaoImpl.SUCURSALES_ALL);
		optAll.put(CMB_BOX_LBL, CMB_OPT_TODAS);
		sucursalesOptionCollection.add(optAll);
		
		List sucursalesWithCanal = getSicaServiceData().findAllSucursalesWithCanal();
		for(Iterator it = sucursalesWithCanal.iterator(); it.hasNext();) {
			Sucursal currentSucursal = (Sucursal) it.next();
			Map currentSucursalMap = new HashMap();
			currentSucursalMap.put(CMB_BOX_VAL, currentSucursal.getIdSucursal());
			currentSucursalMap.put(CMB_BOX_LBL, currentSucursal.getNombre());
			sucursalesOptionCollection.add(currentSucursalMap);
		}
		
		return sucursalesOptionCollection;
	}
	
	/**
	 * Obtiene las opciones para el combo Sucursal dada una zona
	 * @param idZona
	 * @return
	 */
	private List getSucursalesOptionCollection(Integer idZona) {
		List sucursalesOptionCollection = new ArrayList();
		Map optAll = new HashMap();
		optAll.put(CMB_BOX_VAL, JdbcReportesDaoImpl.SUCURSALES_ALL);
		optAll.put(CMB_BOX_LBL, CMB_OPT_TODAS);
		sucursalesOptionCollection.add(optAll);
		
		if(idZona.equals(JdbcReportesDaoImpl.ZONAS_ALL)) {
			return getSucursalesOptionCollection();
		}
		
		List sucursales = getSicaServiceData().findSucursalesByZona(idZona);
		
		for(Iterator it = sucursales.iterator(); it.hasNext();) {
			SucursalZona currentSucursalZona = (SucursalZona) it.next();
			Map currentOpt = new HashMap();
			currentOpt.put(CMB_BOX_VAL, currentSucursalZona.getIdSucursal());
			currentOpt.put(CMB_BOX_LBL, currentSucursalZona.getSucursal().getNombre());
			sucursalesOptionCollection.add(currentOpt);
		}
		
		return sucursalesOptionCollection;
	}

	/**
	 * Modelo del combo de Formas de Liquidaci&oacute;n (Productos). 
	 * Se incluye una opci&oacute;n para seleccionar en la 
	 * b&uacute;squeda todas las divisas.
	 *
	 * @return StringPropertySelectionModel Las formas de liquidaci&oacute;n.
	 * */
	public StringPropertySelectionModel arregloFormasLiquidacion() {
		String[] cfl = getFormasPagoLiqService().getClavesFormasLiquidacion(
				((Visit) getVisit()).getTicket(), true);
		String[] cfln = new String[cfl.length + 1];
		cfln[0] = "TODOS";
		for (int i = 0; i < cfl.length; i++) {
			cfln[i + 1] = cfl[i];
		}
		return new StringPropertySelectionModel(cfln);
	}

	/**
	 * Modelo del combo de Divisas. Se incluye una opci&oacute;n
	 * para seleccionar en la b&uacute;squeda todas las divisas.
	 *
	 * @return RecordSelectionModel Las divisas
	 */
	public IPropertySelectionModel getDivisasFrecuentes() {
		List divisasFrecuentes = getSicaServiceData().findDivisasFrecuentes();
		Divisa todas = new Divisa();
		todas.setIdDivisa("0");
		todas.setDescripcion("TODAS");
		divisasFrecuentes.add(0,todas);
		return new RecordSelectionModel(divisasFrecuentes, "idDivisa", "descripcion");
	}

	public boolean isDateRangeAllowed() {
		boolean isAllowed = false;
		String strHoraMax = getSicaServiceData().findParametro(ParametroSica.REP_UT_HR_MAX).getValor();
        String strHoraMin = getSicaServiceData().findParametro(ParametroSica.REP_UT_HR_MIN).getValor();
        String strHoraActual = HOUR_FORMAT.format(new Date());
        
        try {
			Date dateHoraMax = HOUR_FORMAT.parse(strHoraMax);
			Date dateHoraMin = HOUR_FORMAT.parse(strHoraMin);
	        Date dateHoraActual = HOUR_FORMAT.parse(strHoraActual);
	        isAllowed = dateHoraActual.before(dateHoraMax) || dateHoraActual.after(dateHoraMin); 
	        debug("horaActual: " + dateHoraActual + ", horaMin: " + dateHoraMin + ", horaMax: " + dateHoraMax + ", isDateRangeAllowed: " + isAllowed);
		} catch (ParseException e) {
			debug("Error al comprobar horario de operación: ", e);
		}
		
		return isAllowed;
	}
	
	/**
	 * Busca los detalles con operaciones de compra y venta a imprimir.
	 * 
	 * @param cycle El ciclo de la p&aacute;gina
	 */
	public void reportes(IRequestCycle cycle) {
		IxenetValidationDelegate delegate = (IxenetValidationDelegate) 
		getBeans().getBean("delegate");
		
		// habilita el filtro de sucursales solo si el origen de la operacion es de solo canales de sucursales
		if (SUBMIT_MODE_UPDATE_SUCURSALES_X_ORIG_OP.equals(getModoSubmit())) {
			String origenOperacionSeleccionada = (String) getOrigenOperacionSeleccionada().get(CMB_BOX_VAL);
			if(JdbcReportesDaoImpl.ORIG_OPER_SOLO_CNLS_SUC.equals(origenOperacionSeleccionada)) {
				setSoloCanalesSucursalesSelected(true);
			} else {
				setSucursalSeleccionada(null);
				setSoloCanalesSucursalesSelected(false);
			}
			//setSoloCanalesSucursalesSelected( JdbcReportesDaoImpl.ORIG_OPER_SOLO_CNLS_SUC.equals(origenOperacionSeleccionada) );
			setModoSubmit(null);
			return;
		}
		
		// llena el combo de plazas de acuerdo a la division seleccionada
		if (SUBMIT_MODE_UPDATE_PLAZAS.equals(getModoSubmit())) {
			Integer divisionSel = (Integer) getDivisionSeleccionada().get(CMB_BOX_VAL);
			setPlazaList(getPlazasOptionCollection(divisionSel));
			setZonaList(getZonasOptionCollection(JdbcReportesDaoImpl.PLAZAS_ALL));
			setSucursalList(getSucursalesOptionCollection(JdbcReportesDaoImpl.ZONAS_ALL));
			setModoSubmit(null);
			return;
		}
		
		// llena el combo de zonas de acuerdo a la plaza seleccionada
		if (SUBMIT_MODE_UPDATE_ZONAS.equals(getModoSubmit())) {
			Integer plazaSel = (Integer) getPlazaSeleccionada().get(CMB_BOX_VAL);
			setZonaList(getZonasOptionCollection(plazaSel));
			setSucursalList(getSucursalesOptionCollection(JdbcReportesDaoImpl.ZONAS_ALL));
			setModoSubmit(null);
			return;
		}
		
		// Llena el combo de sucursales de acuerdo a la zona seleccionada
		if (SUBMIT_MODE_UPDATE_SUCURSALES_X_ZONA.equals(getModoSubmit()) ) {
			Integer zonaSel = (Integer) getZonaSeleccionada().get(CMB_BOX_VAL);
			setSucursalList(getSucursalesOptionCollection(zonaSel));
			setModoSubmit(null);
			return;
		} 
		
		// activar/desactivar rango de fechas
		setDateRangeFilterAllowed(isDateRangeAllowed());
		
		if (getRegisterDate() == null){
			delegate.record("Debe definir una fecha de inicio válida.", null);
			return;
		}
		if (isDateRangeFilterAllowed()) {
			if (getRegisterDateHasta() == null){
				delegate.record("Debe definir la fecha de término válida.", null);
				return;
			} else {
				// Validación de rango de fechas negativo
				if ( getRegisterDate().after(getRegisterDateHasta()) ) {
					delegate.record("La fecha de inicio debe ser menor a la fecha de término del rango.", null);
					return;
				}
				// Validación del límite máximo de días para el rango de fechas
				long maxDiasRangoFechas = Long.parseLong( getSicaServiceData().findParametro(ParametroSica.MAX_DIAS_RANGO_FECHAS).getValor() );
				long diferenceInDays = DateUtils.diferenceInDays(getRegisterDate(), getRegisterDateHasta()); 
				if ( diferenceInDays > maxDiasRangoFechas) {
					delegate.record("El rango de fechas no debe exceder de " + maxDiasRangoFechas + " días.", null);
					return;
				}
			}
		}
		
		if (getMontoMinimo() < 0 || getMontoMaximo() < 0) {
			delegate.record("Los montos deben ser mayores a cero.", null);
			return;
		}
		if (getMontoMinimo() > getMontoMaximo()) {
			delegate.record("El monto maximo debe ser mayor al monto minimo.", null);
			return;
		}
		if ( ((String)getOrigenOperacionSeleccionada().get(CMB_BOX_VAL)).equals(ORIG_OP_PSD_OPT_CNLS_SPEC_SUC) ) {
			delegate.record("La opción seleccionada no es válida para el campo <Origen Operación>.", null);
			return;
		}
		if ( ((String)getOrigenOperacionSeleccionada().get(CMB_BOX_VAL)).equals(ORIG_OP_PSD_OPT_CNLS_SPEC_PRM) ) {
			delegate.record("La opción seleccionada no es válida para el campo <Origen Operación>.", null);
			return;
		}
		if(getContratoSica() != null) {
			if(getContratoSica().length() > CONTR_SICA_MAX_LENGTH) {
				delegate.record("El campo <Contrato SICA> excede la logitud máxima (" + CONTR_SICA_MAX_LENGTH + ").", null);
				return;
			}
		}
		
		debug(
			"==========>>> Filtros seleccionados <<<=========="
			+ "\n\tOrigen operación: " + getOrigenOperacionSeleccionada()
			+ "\n\tSucursal: " + getSucursalSeleccionada()
			+ "\n\tFecha desde: " + getRegisterDate()
			+ "\n\tFecha hasta:" + getRegisterDateHasta()
		);
		
		Calendar gc = new GregorianCalendar();
		gc.setTime(getRegisterDate());
		gc.add(Calendar.DAY_OF_YEAR,1);
		gc.add(Calendar.SECOND, -1);
		setGc(gc.getTime());     		
		setRegisterDate(getRegisterDate());
		setDivisaSeleccionada(getDivisaSeleccionada());
		setOperacionSeleccionada(getOperacionSeleccionada());
		setClaveFormaLiquidacion(getClaveFormaLiquidacion());
		setPromotorSeleccionado((Integer)getPromotorHashMap().get("idPersona"));
		
		Date inicioDia  = DateUtils.inicioDia(getRegisterDate()); 
		
		// si el rango de fechas es permitido: se considera el campo Hasta, de lo contrario, no
		Date finDia = null;
		if(isDateRangeFilterAllowed()) { 
			finDia = DateUtils.finDia(getRegisterDateHasta());
			debug("El rango de fechas es permitido, finDía: " + finDia);
		} else {
			finDia = DateUtils.finDia(getRegisterDate());
			debug("El rango de fechas No es permitido, finDía: " + finDia);
		}
		
		Calendar cal = new GregorianCalendar();
		cal.setTime(finDia);
		cal.add(Calendar.DAY_OF_YEAR, 1);
		java.sql.Date inicioDiaSql =  new java.sql.Date(inicioDia.getTime());
		java.sql.Date finDiaSql =  new java.sql.Date(cal.getTime().getTime());
		debug("Rango de fechas sql: " + inicioDiaSql + " - " + finDiaSql);

		// Auditoría
		String datosAdicionalesAuditoria = getDatosAdicionales();
		if(datosAdicionalesAuditoria.length() > LogAuditoria.NUM_512) {
			datosAdicionalesAuditoria = datosAdicionalesAuditoria.substring(0, LogAuditoria.NUM_512 - 1);
		}
		
		// Si el usuario actual no es Staff y se eligieron TODOS los promotores en el filtro, se obtienen los id's de estas personas
		// para mostrar unicamente sus operaciones en el reporte ya que los usuarios de este tipo solo pueden ver su jerarquía 
		List usuariosNoStaff = new ArrayList();
		if((!isStaff()) && getPromotorSeleccionado().intValue() == 0) {
			List promotores = getPromotoresList();
			for(Iterator it = promotores.iterator(); it.hasNext();) {
				Map current = (HashMap)it.next();
				if( ((Integer)current.get("idPersona")).intValue() != 0 ) {
					usuariosNoStaff.add(current.get("idPersona"));
				}
			}
		}
		
		auditar(null, LogAuditoria.REPORTE_OPS_COMPRA_VENTA, datosAdicionalesAuditoria, "INFO", "E");
		
		List resultadoBusqueda = getReportesServiceData().findReporteOperacionesComprasVentas(
			getDivisaSeleccionada().getIdDivisa(),
			getClaveFormaLiquidacion(), 
			getOperacionSeleccionada(), 
			getOrigenOperacionSeleccionada() == null 
				? JdbcReportesDaoImpl.ORIG_OPER_ALL 
				: (String)getOrigenOperacionSeleccionada().get(CMB_BOX_VAL),
			getPromotorSeleccionado(), 
			inicioDiaSql, 
			finDiaSql, 
			getMontoMinimo(), 
			getMontoMaximo(),
			(Integer) getDivisionSeleccionada().get(CMB_BOX_VAL),
			(Integer) getPlazaSeleccionada().get(CMB_BOX_VAL),
			getSucursalSeleccionada() == null ? null : (Integer) getSucursalSeleccionada().get(CMB_BOX_VAL),
			getZonaSeleccionada() == null ? null : (Integer) getZonaSeleccionada().get(CMB_BOX_VAL), // Verificar el tipo de la zona
			getContratoSica(),
			getTipoClienteSeleccionado() == null ? null : (String) getTipoClienteSeleccionado().get(CMB_BOX_VAL),
			isMontoEquivUsd(),
			usuariosNoStaff
		);
		
		//setDetallesTable(resultadoBusqueda);
		setDetalles(resultadoBusqueda);
		
		if (getDetalles().isEmpty()) {
			delegate.record("No se encontraron datos con los Criterios de " +
					"B\u00fasqueda especificados.", null);
		}else{	
			Object parameters[] = new Object[3];
			parameters[0] = "WEB-INF/jasper/RepOpsCompraVenta.jasper";
			parameters[1] = "ReporteOpsCompraVenta";
			parameters[2] = "directo";
			cycle.setServiceParameters(parameters);
		
			this.imprimirReporteOperacionesCompraVentaXls(cycle);
		}
		
		
	}

	/**
	 * Regresa el valor de tipoOperacion.
	 *
	 * @return StringPropertySelectionModel
	 */
	public StringPropertySelectionModel getTipoOperacion() {	
		return new StringPropertySelectionModel(new String[] {"TODAS", "Compra", "Venta"});
	}
	
	/**
	 * Regresa el modelo del combo de origen operación
	 *
	 * @return StringPropertySelectionModel
	 */
	public IPropertySelectionModel getOrigenOperacionModel() {	
		return new RecordSelectionModel(getOrigenOperacionList(), CMB_BOX_VAL, CMB_BOX_LBL);
	}
	
	/**
	 * Verifica si la opción de solo canales sucursales esta seleccionada en el combo de Origen Operación
	 * 
	 * @return
	 */
	
	
	public abstract boolean isSoloCanalesSucursalesSelected();
	public abstract void setSoloCanalesSucursalesSelected(boolean soloCanalesSucursalesSelected);

	/**
	 * Obtiene el DataSource del reporte de Diarios.
	 * 
	 * @return JRDataSource.
	 * @see com.ixe.ods.tapestry.services.jasper.DataSourceProvider#getDataSource(String id)
	 */
	public JRDataSource getDataSource(String id) {
		List dealDetallesMap = new ArrayList();
		for (Iterator it = getDetalles().iterator(); it.hasNext();) {
			Object[] tupla = (Object[]) it.next();
			HashMap map = new HashMap();
			map.put("idDeal", (Integer)tupla[0]);
			map.put("canal", (String)tupla[1]);
			map.put("folio", (Integer)tupla[2]);
			map.put("fechaValor", (String)tupla[3]);
			map.put("fechaCaptura", (Date)tupla[4]);
			map.put("fechaLiquidacion", (Date)tupla[5]);
			map.put("mnemonico", (String)tupla[6]);
			map.put("operacion", (String)tupla[7]);
			map.put("cliente", (String)tupla[8]);
			map.put("producto", (String)tupla[9]);
			map.put("monto", (Double)tupla[10]);
			map.put("divisa", (String)tupla[11]);
			map.put("tipoDeCambioCliente", (Double)tupla[12]);
			map.put("tipoDeCambioMesa", (Double)tupla[13]);
			map.put("utilidad", (Double)tupla[14]);
			map.put("montoEquivalente", (Double)tupla[15]);
			map.put("promotor", (String)tupla[16]);
			map.put("afectaPosicion", (String)tupla[17]);
			map.put("noContratoSica", (String)tupla[18]);
			map.put("status", (String)tupla[19]);
			map.put("mesa", (String)tupla[20]);
			map.put("sectorEconomico", (String)tupla[21]);
			map.put("sectorBanxico", (String)tupla[22]);
			
			map.put("desdeTeller", (String)tupla[23]);
			map.put("sucursal", (String)tupla[24]);
			
			map.put("zona", (String)tupla[25]);
			map.put("plaza", (String)tupla[26]);
			map.put("division", (String)tupla[27]);
			map.put("esCliente", (String)tupla[28]);
			
			dealDetallesMap.add(map);
		}
		return new ListDataSource(dealDetallesMap);
	}

	/**
	 * Imprime el reporte Diario en formato de Excel
	 * 
	 * @param cycle El ciclo de la P&aacute;gina
	 */
	public void imprimirReporteOperacionesCompraVentaXls(IRequestCycle cycle) {
		Object parameters[] = cycle.getServiceParameters();
		
		String resource = (String) parameters[0];
		String name = (String) parameters[1];
		Map reportOutParams = null;
		if (parameters.length > 3) {
			reportOutParams = (Map) parameters[3];
		}
		
		
		JasperPrint jasperPrint = null;
		try {
			JRDataSource dataSource = getDataSource("");
			BaseGlobal global = (BaseGlobal) getGlobal();
			InputStream inputStream = global.getApplicationContext().
			getResource(resource).getInputStream();
			JasperReport jasperReport = (JasperReport) JRLoader.loadObject(inputStream);
			jasperPrint = JasperFillManager.fillReport(jasperReport, 
					reportOutParams, dataSource);
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
			exporter.setParameter(JRXlsExporterParameter.IS_AUTO_DETECT_CELL_TYPE, 
					Boolean.TRUE);
			exporter.setParameter(
					JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, 
					Boolean.TRUE);
			exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, 
					Boolean.FALSE);
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
				System.err.println(ioex.getMessage());
				ioex.printStackTrace();
			}
		}
		catch (JRException e) {
			if (_logger.isDebugEnabled()) {
				_logger.debug(e);
			}
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * Arma la cadena para poblar el campo datosAdicionales en la entidad LogAuditoría
	 * 
	 * @return String
	 */
	private String getDatosAdicionales() {
		StringBuffer sb = new StringBuffer();

		sb.append("div:").append(getDivisaSeleccionada().getIdDivisa());
		sb.append(" prod:").append(getClaveFormaLiquidacion());
		sb.append(" topr:").append(getOperacionSeleccionada());
		sb.append(" oopr:").append(getOrigenOperacionSeleccionada().get(CMB_BOX_VAL));
		sb.append(" divsn:").append(getDivisionSeleccionada() == null ? "0" : getDivisionSeleccionada().get(CMB_BOX_VAL));
		sb.append(" plaza:").append(getPlazaSeleccionada() == null ? "0" : getPlazaSeleccionada().get(CMB_BOX_VAL));
		sb.append(" zona:").append(getZonaSeleccionada() == null ? "0" : getZonaSeleccionada().get(CMB_BOX_VAL));
		sb.append(" suc:").append(getSucursalSeleccionada() == null ? "0" : getSucursalSeleccionada().get(CMB_BOX_VAL));
		sb.append(" prom:").append(getPromotorHashMap().get("idPersona"));
		sb.append(" fmin:").append(DATE_FORMAT.format(getRegisterDate()));
		sb.append(" fmax:").append(getRegisterDateHasta() == null ? "" : DATE_FORMAT.format(getRegisterDateHasta()));
		sb.append(" ctto:").append(getContratoSica());
		sb.append(" tcli:").append(getTipoClienteSeleccionado() == null ? "" : getTipoClienteSeleccionado().get(CMB_BOX_VAL));
		sb.append(" mmin:").append(getMontoMinimo());
		sb.append(" mmax:").append(getMontoMaximo());
		sb.append(" musd:").append(isMontoEquivUsd());
		
		return sb.toString();
	}
	
	/**
	 * Evalúa si el usuario actual tiene un perfil tipo "staff", es decir, si tiene acceso a todos los promotores de la jerarquía
	 * del SICA
	 */
	private boolean isStaff() {
		Visit visit = (Visit) getVisit();
		if ( visit.hasPermission(FacultySystem.SICA_MESACAMBIOS)
			|| visit.hasPermission(FacultySystem.SICA_MESA_CONTROL)
			|| visit.hasPermission(FacultySystem.SICA_CASETACRED)
			|| visit.hasPermission(FacultySystem.ADM_SICA_LIM)
			|| visit.hasPermission(FacultySystem.SICA_TESORERIA)
			|| visit.hasPermission(FacultySystem.SICA_CONTABILIDAD)
		) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Regresa el valor de divisaSeleccionada.
	 *
	 * @return Divisa.
	 */  
	public abstract Divisa getDivisaSeleccionada();

	/**
	 * Fija el valor de divisaSeleccionada.
	 *
	 * @param divisaSeleccionada El valor a asignar.
	 */
	public abstract void setDivisaSeleccionada(Divisa divisaSeleccionada);

	/**
	 * Regresa el valor de operacionSeleccionada.
	 *
	 * @return String.
	 */  
	public abstract String getOperacionSeleccionada();

	/**
	 * Fija el valor de operacionSeleccionada.
	 *
	 * @param operacionSeleccionada El valor a asignar.
	 */
	public abstract void setOperacionSeleccionada(String operacionSeleccionada);    

	/**
	 * Regresa el valor de origenOperacionSeleccionada.
	 *
	 * @return String.
	 */  
	public abstract HashMap getOrigenOperacionSeleccionada();

	/**
	 * Fija el valor de origenOperacionSeleccionada.
	 *
	 * @param origenOperacionSeleccionada El valor a asignar.
	 */
	public abstract void setOrigenOperacionSeleccionada(HashMap origenOperacionSeleccionada);   
	
	/**
	 * Regresa el valor de registerDate.
	 *
	 * @return Date.
	 */  
	public abstract Date getRegisterDate();

	/**
	 * Fija el valor de registerDate.
	 *
	 * @param registerDate El valor a asignar.
	 */
	public abstract void setRegisterDate(Date registerDate);

	/**
	 * Regresa el valor de registerDate.
	 *
	 * @return Date.
	 */  
	public abstract Date getRegisterDateHasta();

	/**
	 * Fija el valor de registerDate.
	 *
	 * @param registerDateHasta El valor a asignar.
	 */
	public abstract void setRegisterDateHasta(Date registerDateHasta);

	/**
	 * Regresa el valor de claveFormaLiquidacion.
	 *
	 * @return String.
	 */  
	public abstract String getClaveFormaLiquidacion();

	/**
	 * Fija el valor de claveFormaLiquidacion.
	 *
	 * @param claveFormaLiquidacion El valor a asignar.
	 */
	public abstract void setClaveFormaLiquidacion(String claveFormaLiquidacion);

	/**
	 * Regresa el valor de gc.
	 *
	 * @return Date.
	 */  
	public abstract Date getGc();

	/**
	 * Fija el gc de registerdate.
	 *
	 * @param gc El valor a asignar.
	 */
	public abstract void setGc(Date gc);

	/**
	 * Regresa el valor de detalles.
	 *
	 * @return List.
	 */  
	public List getDetalles(){
		return this.detalles;
	}

	/**
	 * Fija el valor de detalles.
	 *
	 * @param detalles El valor a asignar.
	 */
	public void setDetalles(List detalles){
		this.detalles=detalles;
	}

	

	/**
	 * Obtiene el arreglo de los distintos productos para liquidaci&oacute;n,
	 * incluyendo la opci&oacute;n de elegir todos los productos.
	 * 
	 * @return String [] arreglo de productos para liquidaci&oacute;n
	 * */
	public abstract String[] getProductos();

	/**
	 * Asigna el arreglo de los distintos productos para liquidaci&oacute;n,
	 * 
	 * @param productos para liquidaci&oacute;n
	 * */
	public abstract void setProductos(String [] productos);

	/**
	 * Obtiene la lista de las distintas divisas frecuentes
	 * incluyendo la opci&oacute;n de elegir todas.
	 * 
	 * @return List lista de divisas frecuentes
	 * */
	public abstract List getDivisas();

	/**
	 * Asigna la lista de las distintas divisas frecuentes
	 * 
	 * @param divisas frecuentes
	 * */
	public abstract void setDivisas(List divisas);

	/**
	 * Modelo del combo de Promotores.
	 *
	 * @return RecordSelectionModel 
	 */
	public IPropertySelectionModel getPromotoresModel() {
		return new RecordSelectionModel(getPromotoresList(), "idPersona", "nombreCompleto");
	}

	/**
	 * Obtiene la Lista de Promotores a popular en el combo de los mismos,
	 * de acuerdo a las Jerarqu&iacute;as de Seguridad.
	 *
	 * @return List Los Promotores.
	 */
	public abstract List getPromotoresList();

	/**
	 * Fija la Lista de Promotores a popular en el combo de los mismos,
	 * de acuerdo a las Jerarqu&iacute;as de Seguridad.
	 *
	 * @param listaPromotores Los Promotores.
	 */
	public abstract void setPromotoresList(List listaPromotores);
	
	/**
	 * Fija la lista de Origenes de operaciones para el combo de los mismos
	 * 
	 * @param listaOrigenesOperaciones
	 */
	public abstract void setOrigenOperacionList(List listaOrigenOperacion);
	
	/**
	 * Obtiene la lista de los origenes de operaciones para poblar el combo de los mismos 
	 * 
	 * @return Lista de los origenes de las operaciones
	 */
	public abstract List getOrigenOperacionList();
	

	/**
	 * Regresa el valor de modo.
	 *
	 * @return String.
	 */
	public abstract String getModo();

	/**
	 * Establece el valor de modo.
	 *
	 * @param modo El valor a asignar.
	 */
	public abstract void setModo(String modo);

	/**
	 * Obtiene el Promotor seleccionado del combo de Promotores.
	 *
	 * @return HasMap El Promotor seleccionado.
	 */
	public HashMap getPromotor() {
		return null;
	}

	/**
	 * No hace nada.
	 *
	 * @param promotor El Promotor.
	 */
	public void setPromotor(HashMap promotor) {

	}

	/**
	 * Obtiene el Promotor seleccionado del combo de Promotores.
	 *
	 * @return Integer El Promotor seleccionado.
	 */
	public abstract Integer getPromotorSeleccionado();

	/**
	 * Asigna el nombre del promotor seleccionado.
	 *
	 * @param promotorSeleccionado Integer el Promotor.
	 */
	public abstract void setPromotorSeleccionado(Integer promotorSeleccionado);

	/**
	 * Obtiene un HashMap con los promotores existentes
	 * 
	 * @return HashMap de Promotores
	 */
	public abstract HashMap getPromotorHashMap();

	/**
	 * Asigna el HashMap de los promotores existentes
	 * 
	 * @param promotorHashMap Promotores.
	 */
	public abstract void setPromotorHashMap(HashMap promotorHashMap);

	/**
	 * Obtiene el monto minimo para la busqueda.
	 * 
	 * @return double
	 */
	public abstract double getMontoMinimo();

	/**
	 * Asigna el valor para el monto minimo de la busqueda
	 * 
	 * @param montoMinimo El valor para el monto minimo
	 */
	public abstract void setMontoMinimo(double montoMinimo);

	/**
	 * Obtiene el valor maximo para la busqueda
	 * 
	 * @return double 
	 */
	public abstract double getMontoMaximo();

	/**
	 * Asigna el valor para el monto minimo de la busqueda
	 * 
	 * @param montoMaximo El valor para el monto maximo.
	 */
	public abstract void setMontoMaximo(double montoMaximo);

	/**
	 * Asigna el valor para la propiedad que determina si es permitido un rango de fechas 
	 * 
	 * @param dateRangeFilterAllowed
	 */
	public abstract void setDateRangeFilterAllowed(boolean dateRangeFilterAllowed);
	
	/**
	 * Obtien el valor para la propiedad que determina si es permitido un rango de fechas
	 * 
	 * @return true o false
	 */
	public abstract boolean isDateRangeFilterAllowed();
	
	/*=======================================================================================================
	 * METODOS PARA EL COMBO DE DIVISION
	 =======================================================================================================*/
	/**
	 * Fija el valor para la lista de divisiones
	 */
	public abstract void setDivisionList(List divisionList);
	
	/**
	 * Retorna la lista de divisiones a cargar
	 * 
	 * @return Lista de divisiones
	 */
	public abstract List getDivisionList();
	
	/**
	 * Fija el valor de la division seleccionada
	 * 
	 * @param divisionSeleccionada
	 */
	public abstract void setDivisionSeleccionada(HashMap divisionSeleccionada);
	
	/**
	 * Retorna la division seleccionada
	 * 
	 * @return division seleccionada
	 */
	public abstract HashMap getDivisionSeleccionada();
	
	/**
	 * Retorna el modelo para el combo de divisiones
	 * 
	 * @return Modelo
	 */
	public IPropertySelectionModel getDivisionModel() {
		return new RecordSelectionModel(getDivisionList(), CMB_BOX_VAL, CMB_BOX_LBL);
	}
	
	/*=======================================================================================================
	 * METODOS PARA EL COMBO DE PLAZAS
	 =======================================================================================================*/
	/**
	 * Fija el valor para la lista de plazas
	 */
	public abstract void setPlazaList(List plazaList);
	
	/**
	 * Retorna la lista de plazas a cargar
	 * 
	 * @return Lista de plazas
	 */
	public abstract List getPlazaList();
	
	/**
	 * Fija el valor de la plaza seleccionada
	 * 
	 * @param plazaSeleccionada
	 */
	public abstract void setPlazaSeleccionada(HashMap plazaSeleccionada);
	
	/**
	 * Retorna la plaza seleccionada
	 * 
	 * @return plaza seleccionada
	 */
	public abstract HashMap getPlazaSeleccionada();
	
	/**
	 * Retorna el modelo para el combo de plazas
	 * 
	 * @return Modelo
	 */
	public IPropertySelectionModel getPlazaModel() {
		return new RecordSelectionModel(getPlazaList(), CMB_BOX_VAL, CMB_BOX_LBL);
	}
	
	/*=======================================================================================================
	 * METODOS PARA EL COMBO DE SUCURSALES
	 =======================================================================================================*/
	/**
	 * Fija el valor para la lista de sucursales
	 */
	public abstract void setSucursalList(List sucursalList);
	
	/**
	 * Retorna la lista de sucursales a cargar
	 * 
	 * @return Lista de sucursales
	 */
	public abstract List getSucursalList();
	
	/**
	 * Fija el valor de la sucursal seleccionada
	 * 
	 * @param sucursalSeleccionada
	 */
	public abstract void setSucursalSeleccionada(HashMap sucursalSeleccionada);
	
	/**
	 * Retorna la sucursal seleccionada
	 * 
	 * @return Sucursal seleccionada
	 */
	public abstract HashMap getSucursalSeleccionada();
	
	/**
	 * Retorna el modelo para el combo de sucursales
	 * 
	 * @return Modelo
	 */
	public IPropertySelectionModel getSucursalModel() {
		return new RecordSelectionModel(getSucursalList(), CMB_BOX_VAL, CMB_BOX_LBL);
	}
	
	/*=======================================================================================================
	 * METODOS PARA EL COMBO DE ZONAS
	 =======================================================================================================*/
	
	/**
	 * Fija la lista de zonas
	 *   
	 */
	public abstract void setZonaList(List zonaList);
	
	/**
	 * Obtiene la lista de zonas
	 * 
	 * @return
	 */
	public abstract List getZonaList();
	
	/**
	 * Fija la zona seleccionada
	 * 
	 * @param zonaSeleccionada
	 */
	public abstract void setZonaSeleccionada(HashMap zonaSeleccionada);
	
	/**
	 * Retorna la zona seleccionada
	 * 
	 * @return
	 */
	public abstract HashMap getZonaSeleccionada();
	
	/**
	 * Retorna el modelo para el combo de zonas
	 * 
	 * @return
	 */
	public IPropertySelectionModel getZonaModel() {
		return new RecordSelectionModel(getZonaList(), CMB_BOX_VAL, CMB_BOX_LBL);
	}
	
	/**
	 * Fija el valor para el modo de submit
	 * 
	 * @param submitMode
	 */
	public abstract void setModoSubmit(String modoSubmit);
	
	/**
	 * Obtiene el modo de submit
	 * 
	 * @return
	 */
	public abstract String getModoSubmit();
	
	/**
	 * Obtiene el valor del campo Contrato SICA
	 * 
	 * @return
	 */
	public abstract String getContratoSica();
	
	/**
	 * Fija el valor del campo Contrato SICA
	 * 
	 * @param contratoSica
	 */
	public abstract void setContratoSica(String contratoSica);
	
	/**
	 * Retorna el modelo para el combo Tipo Cliente
	 * 
	 * @return
	 */
	public IPropertySelectionModel getTipoClienteModel() {
		List tipoClienteModelList = new ArrayList();
		
		Map optAll = new HashMap();
		optAll.put(CMB_BOX_VAL, JdbcReportesDaoImpl.TIPO_CLIENTE_ALL);
		optAll.put(CMB_BOX_LBL, "TODOS");
		Map optCliente = new HashMap();
		optCliente.put(CMB_BOX_VAL, SiNoType.TRUE);
		optCliente.put(CMB_BOX_LBL, "Cliente");
		Map optUsuario = new HashMap();
		optUsuario.put(CMB_BOX_VAL, SiNoType.FALSE);
		optUsuario.put(CMB_BOX_LBL, "Usuario");
		
		tipoClienteModelList.add(optAll);
		tipoClienteModelList.add(optCliente);
		tipoClienteModelList.add(optUsuario);
		
		return new RecordSelectionModel(tipoClienteModelList, CMB_BOX_VAL, CMB_BOX_LBL);
	}
	
	/**
	 * Establece el valor para el campo Tipo Cliente
	 * 
	 * @param tipoClienteSeleccionado
	 */
	public abstract void setTipoClienteSeleccionado(HashMap tipoClienteSeleccionado);
	
	/**
	 * Retorna el valor para el campo Tipo Cliente
	 * 
	 * @return
	 */
	public abstract HashMap getTipoClienteSeleccionado();
	
	/**
	 * Obtiene el valor del checkbox para equivalencia USD del filtro de monto
	 * 
	 * @return
	 */
	public abstract boolean isMontoEquivUsd();
	
	/**
	 * Establece el valor del checkbox para equivalencia USD del filtro de monto
	 * 
	 * @param montoEquivUsd
	 */
	public abstract void setMontoEquivUsd(boolean montoEquivUsd);
	
	private List detalles=null;
	
	/**
	 * Modo de submit para habilitar/inhabilitar el combo de sucursales seleccionando el origen de la operación como "Solo canales sucursales"
	 */
	private static final String SUBMIT_MODE_UPDATE_SUCURSALES_X_ORIG_OP = "_SUBMIT_MODE_UPDATE_SUCURSALES_X_ORIG_OP";
	
	/**
	 * Modo de submit para actualizar el combo de plazas
	 */
	private static final String SUBMIT_MODE_UPDATE_PLAZAS = "_SUBMIT_MODE_UPDATE_PLAZAS";
	
	/**
	 * Valor del modo de submit para actualizar el combo de zonas
	 */
	private static final String SUBMIT_MODE_UPDATE_ZONAS = "_SUBMIT_MODE_UPDATE_ZONAS";
	
	/**
	 * Valor del modo de submit para actualizar el como de sucursales a partir de una zona seleccionada
	 */
	private static final String SUBMIT_MODE_UPDATE_SUCURSALES_X_ZONA = "_SUBMIT_MODE_UPDATE_SUCURSALES_X_ZONA";
	
	/**
	 * Constante para identificar los valorer de las opciones de un combo
	 */
	private static final String CMB_BOX_VAL = "cmbBoxVal";
	
	/**
	 * Constante para identificar las etiquetas de las opciones de un combo
	 */
	private static final String CMB_BOX_LBL = "cmbBoxLbl";
	
	/**
	 * Constantes para identificar las opciones de encabezado del combo de Origen Operación 
	 */
	private static final String ORIG_OP_PSD_OPT_CNLS_SPEC_SUC = "pseudoOptCanalesEspecificosSucursales";
	private static final String ORIG_OP_PSD_OPT_LBL_CNLS_SPEC_SUC = "==== Canales de sucursales ====";
	private static final String ORIG_OP_PSD_OPT_CNLS_SPEC_PRM = "pseudoOptCanalesEspecificosPromocion";
	private static final String ORIG_OP_PSD_OPT_LBL_CNLS_SPEC_PRM = "==== Canales de promoción ====";
	
	private static final String CMB_OPT_TODAS = "TODAS";
	private static final String ORIG_OP_OPT_LBL_SOLO_CNLS_SUC = "Solo canales de sucursales";
	private static final String ORIG_OP_OPT_LBL_SOLO_CNLS_PRM = "Solo canales de promoción";
	
	/**
	 * Longitud máxima para el campo <Contrato SICA>
	 */
	public static final int CONTR_SICA_MAX_LENGTH = 33;
}

