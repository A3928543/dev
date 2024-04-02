/*
 * $Id: ReportesTrimestralesFiltrados.java,v 1.1.2.2.58.1 2020/12/01 04:53:01 mejiar Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 - 2009 LegoSoft S.C.
 */
package com.ixe.ods.sica.pages.reportes;

import java.io.OutputStream;
import java.io.Serializable;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IPropertySelectionModel;

import com.ixe.isis.tapestry.IxenetValidationDelegate;
import com.ixe.ods.bup.model.Direccion;
import com.ixe.ods.bup.model.EmpleadoIxe;
import com.ixe.ods.bup.model.PersonaFisica;
import com.ixe.ods.bup.model.PersonaMoral;
import com.ixe.ods.bup.model.Telefono;
import com.ixe.ods.sica.Constantes;
import com.ixe.ods.sica.Num;
import com.ixe.ods.sica.SupportEngine;
import com.ixe.ods.sica.model.Canal;
import com.ixe.ods.sica.model.DetalleReporteDiario;
import com.ixe.ods.sica.model.Divisa;
import com.ixe.ods.sica.model.ParametroSica;
import com.ixe.ods.sica.model.PersonaListaBlanca;
import com.ixe.ods.sica.pages.SicaPage;
import com.ixe.ods.sica.utils.DateUtils;
import com.legosoft.tapestry.model.RecordSelectionModel;

/**
 * P&aacute;gina que permite al usuario buscar e imprimir los reportes de
 * diarios
 * 
 * @author lvillegas
 */
public abstract class ReportesTrimestralesFiltrados extends SicaPage {

	/**
	 * Asigna los valores de los componentes de la p&aacute;gina.
	 * 
	 * @param cycle El ciclo de la p&acute;gina.
	 */
	public void activate(IRequestCycle cycle) {
		setCanalesList(new ArrayList());
		setPromotoresList(new ArrayList());
		setDivisasFrecuentesList(new ArrayList());
		setTipoOperacionesList(new ArrayList());
		setExcepcionesList(new ArrayList());
		setZonasList(new ArrayList());
		//Combo canal.
        setCanalesList(getSicaServiceData().findAllCanales());
        setSeleccionaCanal(new boolean[getCanalesList().size()]);
        //Combo promotores
        setPromotoresList(getSicaServiceData().
            findAllPromotoresSICA(((SupportEngine) getEngine()).getApplicationName()));
        setSeleccionaPromotor(new boolean[getPromotoresList().size()]);
        //Combo divisas
		setDivisasFrecuentesList(getSicaServiceData().findDivisasFrecuentes());
		setSeleccionaDivisa(new boolean[getDivisasFrecuentesList().size()]);
		//Combo operaciones
        generaListaOperaciones();
        setSeleccionaOperacion(new boolean[getTipoOperacionesList().size()]);
        //Combo de excepciones
        generaListaExcepciones();
        setSeleccionaExcepcion(new boolean[getExcepcionesList().size()]);
        //Combo de zonas
        generaListaZonas();
        setSeleccionaZona(new boolean[getZonasList().size()]);
        inicializarTrimestres();
		setDetallesTable(new ArrayList());
        setMontoAcumuladoClientes(Double.parseDouble(((ParametroSica) getSicaServiceData().
        		find(ParametroSica.class, ParametroSica.MTO_REP_TRIM_CLI)).getValor()));
        setMontoAcumuladoUsuarios(Double.parseDouble(((ParametroSica) getSicaServiceData().
        		find(ParametroSica.class, ParametroSica.MTO_REP_TRIM_USR)).getValor()));
        setFiltrosMinimizados(false);
		super.activate(cycle);
	}

    /**
     * Limpia todos los filtros de b&uacute;squeda de la p&aacute;gina.
     * 
     * @param cycle El ciclo de la p&acute;gina.
     */
    public void limpiarForma(IRequestCycle cycle) {
    	setSeleccionaCanal(new boolean[getCanalesList().size()]);
        setSeleccionaPromotor(new boolean[getPromotoresList().size()]);
        setSeleccionaDivisa(new boolean[getDivisasFrecuentesList().size()]);
        setSeleccionaOperacion(new boolean[getTipoOperacionesList().size()]);
        setSeleccionaExcepcion(new boolean[getExcepcionesList().size()]);
        setSeleccionaZona(new boolean[getZonasList().size()]);
        setContratosSica("");
        setMontoAcumuladoClientes(Double.parseDouble(((ParametroSica) getSicaServiceData().
        		find(ParametroSica.class, ParametroSica.MTO_REP_TRIM_CLI)).getValor()));
        setMontoAcumuladoUsuarios(Double.parseDouble(((ParametroSica) getSicaServiceData().
        		find(ParametroSica.class, ParametroSica.MTO_REP_TRIM_USR)).getValor()));
        setDetallesTable(new ArrayList());
    }
    
	/**
	 * Busca los detalles con operaciones de compra y venta a imprimir.
	 * 
	 * @param cycle El ciclo de la p&aacute;gina
	 */
	public void reportes(IRequestCycle cycle) {
        IxenetValidationDelegate delegate = (IxenetValidationDelegate)
                getBeans().getBean("delegate");
        if (getMontoAcumuladoClientes() < 0 || getMontoAcumuladoUsuarios() < 0) {
            delegate.record("Los montos acumulados deben ser mayores a cero.", null);
            return;
        }
        StringBuffer filtros = new StringBuffer();
        boolean aplicaFiltro = false;
        filtros.append("trimestre: " + getTrimestreBase().get(VALUE) + ",");
        setCanalesSeleccionados(new ArrayList());
        for (int i = getSeleccionaCanal().length - 1; i >= 0; i--) {
            if (getSeleccionaCanal()[i]) {
            	if (!aplicaFiltro) {
                    aplicaFiltro = true;
                    filtros.append("Canales:");
                }
            	String idCanal = ((Canal) getCanalesList().get(i)).getIdCanal();
                getCanalesSeleccionados().add(idCanal);
                filtros.append(idCanal + ",");
            }
        }
        aplicaFiltro = false;
        setPromotoresSeleccionados(new ArrayList());
        for (int i = getSeleccionaPromotor().length - 1; i >= 0; i--) {
            if (getSeleccionaPromotor()[i]) {
            	if (!aplicaFiltro) {
                    aplicaFiltro = true;
                    filtros.append("Promotores:");
                }
            	Integer idPersona = ((EmpleadoIxe) getPromotoresList().get(i)).getIdPersona();
                getPromotoresSeleccionados().add(idPersona);
                filtros.append(idPersona + ",");
            }
        }
        aplicaFiltro = false;
        setDivisasSeleccionadas(new ArrayList());
        for (int i = getSeleccionaDivisa().length - 1; i >= 0; i--) {
            if (getSeleccionaDivisa()[i]) {
            	if (!aplicaFiltro) {
                    aplicaFiltro = true;
                    filtros.append("Divisas:");
                }
                String idDivisa = ((Divisa) getDivisasFrecuentesList().get(i)).getIdDivisa();
                getDivisasSeleccionadas().add(idDivisa);
                filtros.append(idDivisa + ",");
            }
        }
        aplicaFiltro = false;
        setOperacionesSeleccionadas(new ArrayList());
        for (int i = getSeleccionaOperacion().length - 1; i >= 0; i--) {
            if (getSeleccionaOperacion()[i]) {
            	if (!aplicaFiltro) {
                    aplicaFiltro = true;
                    filtros.append("Operacion:");
                }
                Object clave = ((MapaPantalla) getTipoOperacionesList().get(i)).getClaveBD();
                getOperacionesSeleccionadas().add(clave);
                filtros.append(clave.toString() + ",");
            }
        }
        aplicaFiltro = false;
        setExcepcionesSeleccionadas(new ArrayList());
        for (int i = getSeleccionaExcepcion().length - 1; i >= 0; i--) {
            if (getSeleccionaExcepcion()[i]) {
            	if (!aplicaFiltro) {
                    aplicaFiltro = true;
                    filtros.append("Excepciones:");
                }
                Object clave = ((MapaPantalla) getExcepcionesList().get(i)).getClaveBD();
                getExcepcionesSeleccionadas().add(clave);
                filtros.append(clave.toString() + ",");
            }
        }
        aplicaFiltro = false;
        setZonasSeleccionadas(new ArrayList());
        for (int i = getSeleccionaZona().length - 1; i >= 0; i--) {
            if (getSeleccionaZona()[i]) {
            	if (!aplicaFiltro) {
                    aplicaFiltro = true;
                    filtros.append("Zonas:");
                }
                Object clave = ((MapaPantalla) getZonasList().get(i)).getClaveBD();
                getZonasSeleccionadas().add(clave);
                filtros.append(clave.toString() + ",");
            }
        }
        Date fechaInicial = new Date();
        Date fechaFinal = new Date();
        if (new Integer(Num.I_1).equals(getTrimestreBase().get(VALUE))) {
        	fechaInicial = DateUtils.inicioDia(DateUtils.primerDiaDeMes(DateUtils.
        			mesDelAnioEnCurso(new Date(), Num.I_0)));
        	fechaFinal = DateUtils.finDia(DateUtils.ultimoDiaDelMes(DateUtils.
        			mesDelAnioEnCurso(new Date(), Num.I_2)));
        }
        else if (new Integer(Num.I_2).equals(getTrimestreBase().get(VALUE))) {
        	fechaInicial = DateUtils.inicioDia(DateUtils.primerDiaDeMes(DateUtils.
        			mesDelAnioEnCurso(new Date(), Num.I_3)));
        	fechaFinal = DateUtils.finDia(DateUtils.ultimoDiaDelMes(DateUtils.
        			mesDelAnioEnCurso(new Date(), Num.I_5)));
        }
        else if (new Integer(Num.I_3).equals(getTrimestreBase().get(VALUE))) {
        	fechaInicial = DateUtils.inicioDia(DateUtils.primerDiaDeMes(DateUtils.
        			mesDelAnioEnCurso(new Date(), Num.I_6)));
        	fechaFinal = DateUtils.finDia(DateUtils.ultimoDiaDelMes(DateUtils.
        			mesDelAnioEnCurso(new Date(), Num.I_8)));
        }
        else if (new Integer(Num.I_4).equals(getTrimestreBase().get(VALUE))) {
        	fechaInicial = DateUtils.inicioDia(DateUtils.primerDiaDeMes(DateUtils.
        			mesDelAnioEnCurso(new Date(), Num.I_9)));
        	fechaFinal = DateUtils.finDia(DateUtils.ultimoDiaDelMes(DateUtils.
        			mesDelAnioEnCurso(new Date(), Num.I_11)));
        }
        if (getContratosSica() != null && getContratosSica().length() > 0) {
            filtros.append("Contratos:" + getContratosSica());
        }
        
        auditar(null, "Reporte Trimestral",
            filtros.length() < Num.I_512 ?
                filtros.toString() :
                filtros.toString().substring(0, Num.I_512), "INFO", "E");
        
        setDetallesTable(getSicaServiceData().findReporteDealsTrimestral(getCanalesSeleccionados(),
        		getPromotoresSeleccionados(), getDivisasSeleccionadas(),
        		getExcepcionesSeleccionadas(), getZonasSeleccionadas(),
        		getOperacionesSeleccionadas().contains("COMPRA"),
        		getOperacionesSeleccionadas().contains("VENTA"), fechaInicial, fechaFinal,
        		getMontoAcumuladoClientes(), getMontoAcumuladoUsuarios(), getContratosSica()
                .replaceAll(" ", "").split(","),
                getSicaServiceData().findParametro(PRODUCTOS_EN_RESTRICCION).getValor()
                .replaceAll(" ", "").split(",")));
        
        if (getDetallesTable() == null || getDetallesTable().isEmpty()) {
            delegate.record("No se encontraron datos con los Criterios de " +
                    "B\u00fasqueda especificados.", null);
            setFiltrosMinimizados(false);
        }
        else {
            setFiltrosMinimizados(true);
        }
    }
	
	/**
	 * Inicializa el combobox con los valores del los trimestres del anio en curso.
	 */
	public void inicializarTrimestres() {
		List modelList = new ArrayList();
        Map pares = new HashMap();
    	pares.put(LABEL, formatFecha.format(DateUtils.primerDiaDeMes(DateUtils.
    			mesDelAnioEnCurso(new Date(), Num.I_0))) + " - " + formatFecha.format(DateUtils.
    			ultimoDiaDelMes(DateUtils.mesDelAnioEnCurso(new Date(), Num.I_2))));
    	pares.put(VALUE, new Integer(Num.I_1));
    	modelList.add(pares);
    	setTrimestreBase(pares);
    	pares = new HashMap();
    	pares.put(LABEL, formatFecha.format(DateUtils.primerDiaDeMes(DateUtils.
    			mesDelAnioEnCurso(new Date(), Num.I_3))) + " - " + formatFecha.format(DateUtils.
    			ultimoDiaDelMes(DateUtils.mesDelAnioEnCurso(new Date(), Num.I_5))));
    	pares.put(VALUE, new Integer(Num.I_2));
    	modelList.add(pares);
    	pares = new HashMap();
    	pares.put(LABEL, formatFecha.format(DateUtils.primerDiaDeMes(DateUtils.
    			mesDelAnioEnCurso(new Date(), Num.I_6))) + " - " + formatFecha.format(DateUtils.
    			ultimoDiaDelMes(DateUtils.mesDelAnioEnCurso(new Date(), Num.I_8))));
    	pares.put(VALUE, new Integer(Num.I_3));
    	modelList.add(pares);
    	pares = new HashMap();
    	pares.put(LABEL, formatFecha.format(DateUtils.primerDiaDeMes(DateUtils.
    			mesDelAnioEnCurso(new Date(), Num.I_9))) + " - " + formatFecha.format(DateUtils.
    			ultimoDiaDelMes(DateUtils.mesDelAnioEnCurso(new Date(), Num.I_11))));
    	pares.put(VALUE, new Integer(Num.I_4));
    	modelList.add(pares);
    	setModelListBase(modelList);
	}
    
    /**
     * Imprime el reporte Diario en formato de Excel
     *
     * @param cycle El ciclo de la P&aacute;gina
     */
    public void imprimirReporteDiario(IRequestCycle cycle) {
        Object parameters[] = cycle.getServiceParameters();
        String name = (String) parameters[0];
		String resource = (String) parameters[1];
		String reportOutParams = (String) parameters[2];
        if (name.toUpperCase().equals("SSV")) {
            imprimirReporteDiarioSsv(cycle, resource, reportOutParams);
        }
    }
    
    /**
     * Genera el reporte en formato de Excel.
     * @param cycle de la petici&oacute;n.
     * @param resource que se va a utilizar.
     * @param reportOutParams par&aacute;metros.
     */
    private void imprimirReporteDiarioSsv(
            IRequestCycle cycle, String resource, String reportOutParams) {
        StringBuffer contentDisposition = new StringBuffer();
        contentDisposition.append("attachment; filename=SSV");
        contentDisposition.append(formateaFechaSSV(new Date(), TIPO_REPORTE)).append(".ssv");
        try {
            String cadena = generaCadena();
            HttpServletResponse response = cycle.getRequestContext().getResponse(); 
            response.setContentType("text/ssv");
            response.setHeader("Content-Disposition", contentDisposition.toString());
            response.setContentLength(cadena.getBytes().length);
            OutputStream out = response.getOutputStream();
            out.write(cadena.getBytes(), 0, cadena.getBytes().length);
            out.flush();
            out.close();
        }
        catch (Exception e) {
            getDelegate().record("No se pudo generar el archivo correctamente.", null);
        }
    }
    
    /**
     * M&eacute;todo encargado de generar la cadena que se guardar&aacute; en el archivo
     * SSV.
     * @return cadena.
     */
    private String generaCadena() {
        StringBuffer cadena = new StringBuffer();
        for (Iterator i = getDetallesTable().iterator(); i.hasNext();) {
            DetalleReporteDiario detalle = (DetalleReporteDiario) i.next();
            cadena.append(generaHeader());
            cadena.append(SEPARADOR_SSV);
            cadena.append(generaDetalle(detalle));
            cadena.append(SEPARADOR_SSV);
        }
        return cadena.toString();
    }
    
    /**
     * M&eacute;todo encargado de generar la cadena en SSV de la parte del header
     * de un rengl&oacute;n.
     * @return cadena.
     */
    private StringBuffer generaHeader() {
        StringBuffer header = new StringBuffer();
        header.append(TIPO_REPORTE);
        header.append(SEPARADOR_SSV);
        header.append(formateaFechaSSV(new Date(), TIPO_REPORTE));
        header.append(SEPARADOR_SSV);
        header.append("000001");
        header.append(SEPARADOR_SSV);
        header.append(ORGANO_SUPERVISOR);
        header.append(SEPARADOR_SSV);
        header.append(SUJETO_OBLIGADO);
        return header;
    }
    
    /**
     * M&eacute;todo encargado de generar la lista de tipos de operaciones.
     */
    public void generaListaOperaciones() {
        List lista = new ArrayList();
        for (int i = 0; i < 2; i++) {
            switch (i) {
                case 0:
                    lista.add(new MapaPantalla("COMPRA", "COMPRA"));
                    break;
                case 1:
                    lista.add(new MapaPantalla("VENTA", "VENTA"));
                    break;
                default:
                    break;
            }
        }
        setTipoOperacionesList(lista);
    }
    
    /**
     * M&eacute;todo encargado de generar la lista de excepciones.
     */
    public void generaListaExcepciones() {
        List lista = new ArrayList();
        for (int i = 0; i < 3; i++) {
            switch (i) {
                case 0:
                    lista.add(new MapaPantalla("SIN EXCEPCION", PersonaListaBlanca.SIN_EXCEPCION));
                    break;
                case 1:
                    lista.add(new MapaPantalla("SHCP", PersonaListaBlanca.EXCEPCION_SHCP));
                    break;
                case 2:
                    lista.add(new MapaPantalla("IXE", PersonaListaBlanca.EXCEPCION_IXE));
                    break;
                default:
                    break;
            }
        }
        setExcepcionesList(lista);
    }
    
    /**
     * M&eacute;todo encargado de generar la lista de zonas geogr&aacute;ficas..
     */
    public void generaListaZonas() {
        List lista = new ArrayList();
        for (int i = 0; i < 3; i++) {
            switch (i) {
                case 0:
                    lista.add(new MapaPantalla("ZONA TURISTICA", Constantes.ZONA_TURISTICA));
                    break;
                case 1:
                    lista.add(new MapaPantalla("ZONA FRONTERIZA", Constantes.ZONA_FRONTERIZA));
                    break;
                case 2:
                    lista.add(new MapaPantalla("ZONA NORMAL", Constantes.ZONA_NORMAL));
                    break;
                default:
                    break;
            }
        }
        setZonasList(lista);
    }
    
    /**
     * M&eacute;todo encargado de generar la cadena en SSV del detalle recibido
     * como par&aacute;metro.
     * @param detalle que se convertir&aacute; en cadena.
     * @return cadena
     */
    private StringBuffer generaDetalle(DetalleReporteDiario detalle) {
        StringBuffer cadena = new StringBuffer();
        if (detalle.getDealDetalle().getDeal().getCanalMesa().getCanal().getSucursal() != null) {
            cadena.append(detalle.getDealDetalle().getDeal().getCanalMesa().getCanal().getSucursal().getIdSucursal());
        }
        cadena.append(SEPARADOR_SSV);
        if (detalle.getDealDetalle().getDeal().getCanalMesa().getCanal().getSucursal() != null) {
            cadena.append(detalle.getDealDetalle().getDeal().getCanalMesa().getCanal().getSucursal().getIdSucursal());
        }
        cadena.append(SEPARADOR_SSV);
        if (detalle.getDealDetalle().isRecibimos()) {
            cadena.append("03");
        }
        else {
            cadena.append("04");
        }
        cadena.append(SEPARADOR_SSV);
        if (detalle.getDealDetalle().getClaveFormaLiquidacion().equals(Constantes.EFECTIVO)) {
            cadena.append("01");
        }
        else if (detalle.getDealDetalle().getClaveFormaLiquidacion().equals(Constantes.TRANSFERENCIA) ||
                detalle.getDealDetalle().getClaveFormaLiquidacion().equals(Constantes.TRANSFERENCIA_NACIONAL) ) {
            cadena.append("03");
        }
        else if (detalle.getDealDetalle().getClaveFormaLiquidacion().equals(Constantes.TRAVELER_CHECKS)) {
            cadena.append("04");
        }
        cadena.append(detalle.getDealDetalle().getClaveFormaLiquidacion());
        cadena.append(SEPARADOR_SSV);
        cadena.append(detalle.getDealDetalle().getDeal().getContratoSica().getNoContrato());
        cadena.append(SEPARADOR_SSV);
        cadena.append(detalle.getDealDetalle().getMonto());
        cadena.append(SEPARADOR_SSV);
        cadena.append(detalle.getDealDetalle().getDivisa().getIdDivisa());
        cadena.append(SEPARADOR_SSV);
        cadena.append(formateaFechaSSV(detalle.getDealDetalle().getDeal().getFechaLiquidacion(), REPORTE_RELEVANTE));
        cadena.append(SEPARADOR_SSV);
        cadena.append(formateaFechaSSV(detalle.getDealDetalle().getDeal().getFechaCaptura(), REPORTE_RELEVANTE));
        cadena.append(SEPARADOR_SSV);
        if (detalle.getPersona().getNacionalidad() == null || detalle.getPersona().getNacionalidad().equals("")) {
            cadena.append("0");
        }
        else if (detalle.getPersona().getNacionalidad().equals("MEXICANA")) {
            cadena.append("1");
        }
        else {
            cadena.append("2");
        }
        cadena.append(SEPARADOR_SSV);
        if (detalle.getPersona().getTipoPersona().equals(Constantes.PERSONA_FISICA)) {
            cadena.append("1");
        }
        else {
            cadena.append("2");
        }
        cadena.append(SEPARADOR_SSV);
        if (detalle.getPersona() instanceof PersonaMoral) {
            cadena.append(((PersonaMoral) detalle.getPersona()).getRazonSocial());
        }
        cadena.append(SEPARADOR_SSV);
        if (detalle.getPersona() instanceof PersonaFisica) {
            cadena.append(((PersonaFisica) detalle.getPersona()).getNombre());
        }
        cadena.append(SEPARADOR_SSV);
        if (detalle.getPersona() instanceof PersonaFisica) {
            cadena.append(((PersonaFisica) detalle.getPersona()).getPaterno());
        }
        cadena.append(SEPARADOR_SSV);
        if (detalle.getPersona() instanceof PersonaFisica) {
            cadena.append(((PersonaFisica) detalle.getPersona()).getMaterno());
        }
        cadena.append(SEPARADOR_SSV);
        cadena.append(detalle.getPersona().getRfc());
        cadena.append(SEPARADOR_SSV);
        if (detalle.getPersona() instanceof PersonaFisica) {
            cadena.append(((PersonaFisica) detalle.getPersona()).getCurp());
        }
        cadena.append(SEPARADOR_SSV);
        cadena.append(SEPARADOR_SSV);
        if (detalle.getPersona().getDirecciones() != null &&
            detalle.getPersona().getDirecciones().size() > 0 &&
                detalle.getPersona().getDirecciones().get(0) != null) {
            Direccion direccion = (Direccion) detalle.getPersona().getDirecciones().get(0);
            cadena.append(direccion.getCalleYNumero() + ",");
            if (direccion.getNumExterior() != null &&
                    !direccion.getNumExterior().equals("")) {
                cadena.append("Ext:" + direccion.getNumExterior() + ",");
            }
            if (direccion.getNumExterior() != null &&
                    !direccion.getNumExterior().equals("")) {
                cadena.append("Int:" + direccion.getNumInterior() + ",");
            }
            cadena.append(direccion.getCodigoPostal());
            cadena.append(SEPARADOR_SSV);
            cadena.append(direccion.getColonia());
            cadena.append(SEPARADOR_SSV);
            cadena.append(direccion.getCiudad());
        }
        else {
            cadena.append(SEPARADOR_SSV);
            cadena.append(SEPARADOR_SSV);
        }
        cadena.append(SEPARADOR_SSV);
        if (detalle.getPersona().getTelefonos() != null &&
                detalle.getPersona().getTelefonos().size() > 0 &&
                    detalle.getPersona().getTelefonos().get(0) != null) {
            cadena.append(((Telefono) detalle.getPersona().getTelefonos().get(0)).getNumeroLocal());
        }
        cadena.append(SEPARADOR_SSV);
        cadena.append(SEPARADOR_SSV);
        cadena.append(SEPARADOR_SSV);
        cadena.append(SEPARADOR_SSV);
        cadena.append(SEPARADOR_SSV);
        cadena.append(SEPARADOR_SSV);
        cadena.append(SEPARADOR_SSV);
        cadena.append(SEPARADOR_SSV);
        cadena.append(SEPARADOR_SSV);
        cadena.append(SEPARADOR_SSV);
        cadena.append(SEPARADOR_SSV);
        cadena.append(SEPARADOR_SSV);
        cadena.append(SEPARADOR_SSV);
        cadena.append(SEPARADOR_SSV);
        cadena.append(SEPARADOR_SSV);
        return cadena;
    }
    
    /**
     * Realiza el formateo de una fecha para utilizarlo en el archivo SSV.
     * @param fecha que se desea formatear.
     * @return fecha en forma de cadena en formato yyyyMMdd.
     */
    private String formateaFechaSSV(Date fecha, int tipoReporte) {
        Format formatter;
        if (tipoReporte == REPORTE_RELEVANTE) {
            formatter = new SimpleDateFormat("yyyyMM");
        }
        else {
            formatter = new SimpleDateFormat("yyyyMMdd");
        }
        return formatter.format(fecha);
    }
    
    /**
     * Regresa el valor de currentIndex.
     * 
     * @return int.
     */
    public abstract int getCurrentIndex();
    
    /**
     * Fija el valor de currentIndex.
     *
     * @param index El valor a asignar.
     */
    public abstract void setCurrentIndex(int index);
    
    /**
     * Obtiene el monto minimo para la busqueda.
     * 
     * @return double
     */
    public abstract double getMontoAcumuladoUsuarios();

    /**
     * Asigna el valor para el monto acumulado de usuarios.
     * 
     * @param montoAcumulado El valor para el monto acumulado de usuarios.
     */
    public abstract void setMontoAcumuladoUsuarios(double montoAcumulado);

    /**
     * Obtiene el valor maximo para la busqueda
     * 
     * @return double 
     */
    public abstract double getMontoAcumuladoClientes();

    /**
     * Asigna el valor para el monto acumulado de clientes.
     * 
     * @param montoAcumulado El valor para el monto acumulado de clientes.
     */
    public abstract void setMontoAcumuladoClientes(double montoAcumulado);

    /**
     * Regresa el valor de detallesTable.
     *
     * @return List.
     */  
    public abstract List getDetallesTable();

    /**
     * Fija el valor de detallesTable.
     *
     * @param detallesTable El valor a asignar.
     */
    public abstract void setDetallesTable(List detallesTable);
    
    /**
     * Regresa el valor de filtrosMinimizados.
     *
     * @return List.
     */  
    public abstract boolean getFiltrosMinimizados();
    
    /**
     * Fija el valor de filtrosMinimizados.
     *
     * @param filtrosMinimizados El valor a asignar.
     */
    public abstract void setFiltrosMinimizados(boolean filtrosMinimizados);
    
    /**
     * Regresa el valor de canalesList.
     *
     * @return List.
     */
    public abstract List getCanalesList();
    
    /**
     * Fija el valor de canalesList.
     *
     * @param lista El valor a asignar.
     */
    public abstract void setCanalesList(List lista);
    
    /**
     * Regresa el valor de promotoresList.
     *
     * @return List.
     */
    public abstract List getPromotoresList();
    
    /**
     * Fija el valor de promotoresList.
     *
     * @param lista El valor a asignar.
     */
    public abstract void setPromotoresList(List lista);
    
    /**
     * Regresa el valor de divisasFrecuentesList.
     *
     * @return List.
     */
    public abstract List getDivisasFrecuentesList();
    
    /**
     * Fija el valor de divisasFrecuentesList.
     *
     * @param lista El valor a asignar.
     */
    public abstract void setDivisasFrecuentesList(List lista);
    
    /**
     * Regresa el valor de tipoOperacionesList.
     *
     * @return List.
     */
    public abstract List getTipoOperacionesList();
    
    /**
     * Fija el valor de tipoOperacionesList.
     *
     * @param lista El valor a asignar.
     */
    public abstract void setTipoOperacionesList(List lista);
    
    /**
     * Regresa el valor de excepcionesList.
     *
     * @return List.
     */
    public abstract List getExcepcionesList();
    
    /**
     * Fija el valor de excepcionesList.
     *
     * @param lista El valor a asignar.
     */
    public abstract void setExcepcionesList(List lista);
    
    /**
     * Regresa el valor de seleccionaExcepcion.
     * 
     * @return boolean[].
     */
    public abstract boolean[] getSeleccionaExcepcion();
    
    /**
     * Fija el valor de seleccionaExcepcion.
     *
     * @param arreglo El valor a asignar.
     */
    public abstract void setSeleccionaExcepcion(boolean[] arreglo);
    
    /**
     * Regresa el valor de zonasList.
     *
     * @return List.
     */
    public abstract List getZonasList();
    
    /**
     * Fija el valor de zonasList.
     *
     * @param lista El valor a asignar.
     */
    public abstract void setZonasList(List lista);
    
    /**
     * Regresa el valor de seleccionaZona.
     * 
     * @return boolean[].
     */
    public abstract boolean[] getSeleccionaZona();
    
    /**
     * Fija el valor de seleccionaZona.
     *
     * @param arreglo El valor a asignar.
     */
    public abstract void setSeleccionaZona(boolean[] arreglo);
    
    /**
     * Regresa el valor de seleccionaCanal.
     * 
     * @return boolean[].
     */
    public abstract boolean[] getSeleccionaCanal();
    
    /**
     * Fija el valor de seleccionaCanal.
     *
     * @param arreglo El valor a asignar.
     */
    public abstract void setSeleccionaCanal(boolean[] arreglo);
    
    /**
     * Regresa el valor de seleccionaPromotor.
     * 
     * @return boolean[].
     */
    public abstract boolean[] getSeleccionaPromotor();
    
    /**
     * Fija el valor de seleccionaPromotor.
     *
     * @param arreglo El valor a asignar.
     */
    public abstract void setSeleccionaPromotor(boolean[] arreglo);
    
    /**
     * Regresa el valor de seleccionaDivisa.
     * 
     * @return boolean[].
     */
    public abstract boolean[] getSeleccionaDivisa();
    
    /**
     * Fija el valor de seleccionaDivisa.
     *
     * @param arreglo El valor a asignar.
     */
    public abstract void setSeleccionaDivisa(boolean[] arreglo);
    
    /**
     * Regresa el valor de seleccionaOperacion.
     * 
     * @return boolean[].
     */
    public abstract boolean[] getSeleccionaOperacion();
    
    /**
     * Fija el valor de seleccionaOperacion.
     *
     * @param arreglo El valor a asignar.
     */
    public abstract void setSeleccionaOperacion(boolean[] arreglo);
    
    /**
	 * Regresa el valor de contratosSica.
	 *
	 * @return String.
	 */  
	public abstract String getContratosSica();

	/**
	 * Fija el valor de contratosSica.
	 *
	 * @param cadena El valor a asignar.
	 */
	public abstract void setContratosSica(String cadena);
	
	/**
     * Regresa el valor de canalesSeleccionados.
     *
     * @return List.
     */
    public abstract List getCanalesSeleccionados();

    /**
     * Fija el valor de canalesSeleccionados.
     *
     * @param lista El valor a asignar.
     */
    public abstract void setCanalesSeleccionados(List lista);
    
    /**
     * Regresa el valor de promotoresSeleccionados.
     *
     * @return List.
     */
    public abstract List getPromotoresSeleccionados();

    /**
     * Fija el valor de promotoresSeleccionados.
     *
     * @param lista El valor a asignar.
     */
    public abstract void setPromotoresSeleccionados(List lista);
    
    /**
     * Regresa el valor de divisasSeleccionadas.
     *
     * @return List.
     */
    public abstract List getDivisasSeleccionadas();

    /**
     * Fija el valor de divisasSeleccionadas.
     *
     * @param lista El valor a asignar.
     */
    public abstract void setDivisasSeleccionadas(List lista);
    
    /**
     * Regresa el valor de operacionesSeleccionadas.
     *
     * @return List.
     */
    public abstract List getOperacionesSeleccionadas();

    /**
     * Fija el valor de operacionesSeleccionadas.
     *
     * @param lista El valor a asignar.
     */
    public abstract void setOperacionesSeleccionadas(List lista);
    
    /**
     * Regresa el valor de excepcionesSeleccionadas.
     *
     * @return List.
     */
    public abstract List getExcepcionesSeleccionadas();

    /**
     * Fija el valor de excepcionesSeleccionadas.
     *
     * @param lista El valor a asignar.
     */
    public abstract void setExcepcionesSeleccionadas(List lista);
    
    /**
     * Regresa el valor de zonasSeleccionadas.
     *
     * @return List.
     */
    public abstract List getZonasSeleccionadas();

    /**
     * Fija el valor de zonasSeleccionadas.
     *
     * @param lista El valor a asignar.
     */
    public abstract void setZonasSeleccionadas(List lista);
	
	/**
	 * Regresa el valor del trimestreBase.
	 *
	 * @return Map	El valor del trimestreBase.
	 */
	public abstract Map getTrimestreBase();
	
	/**
	 * Asigna el valor de trimestreBase.
	 *
	 * @param trimestreBase	El valor a asignar.
	 */
	public abstract void setTrimestreBase(Map trimestreBase);
	
	/**
     * Modelo para la lista de trimestres.
     *
     * @return StringPropertySelectionModel	El modelo del trimestre seleccionado.
     */
    public IPropertySelectionModel getBaseModel() {
    	return new RecordSelectionModel(getModelListBase(), VALUE, LABEL);
    }
    
    /**
	 * Constante  value.
	 */
	private static final String VALUE = "value";
	
	/**
	 * Constante label.
	 */
	private static final String LABEL = "label";
	
	/**
	 * Regresa la lista del modelo.
	 *
	 * @return List	La lista del modelo.
	 */
	public abstract List getModelListBase();
	
	/**
	 * Asigna la lista del modelo.
	 *
	 * @param modelLitBase	El valor a asignar.
	 */
	public abstract void setModelListBase(List modelLitBase);
    
    /**
     * @author lvillegas
     */
    private class MapaPantalla implements Serializable {
        /**
         * Serializador.
         */
        private static final long serialVersionUID = 1L;

        /**
         * Constructor por default.
         */
        public MapaPantalla() {
            textoPantalla = null;
            claveBD = null;
        }
        
        /**
         * Constructor con par&aacute;metros.
         * @param texto a desplegar en pantalla.
         * @param clave de la base de datos.
         */
        public MapaPantalla(String texto, Object clave) {
            textoPantalla = texto;
            claveBD = clave;
        }
        
        /**
         * Texto que se utiliza en pantalla.
         */
        private String textoPantalla;
        /**
         * Clave que se utiliza en base de datos.
         */
        private Object claveBD;

        /**
         * @return the claveBD
         */
        public final Object getClaveBD() {
            return claveBD;
        }

        /**
         * @param claveBD the claveBD to set
         */
        public final void setClaveBD(Object claveBD) {
            this.claveBD = claveBD;
        }

        /**
         * @return the textoPantalla
         */
        public final String getTextoPantalla() {
            return textoPantalla;
        }

        /**
         * @param textoPantalla the textoPantalla to set
         */
        public final void setTextoPantalla(String textoPantalla) {
            this.textoPantalla = textoPantalla;
        }
    }
    
    /**
     * Caracter separador para documentos SSV.
     */
    private static final String SEPARADOR_SSV = ";";
    /**
     * Par&aacute;metro que hace referencia a los productos en restricci&oacute;n.
     */
    private static final String PRODUCTOS_EN_RESTRICCION = "PRODUCTOS_EN_RESTRICCION";
    
    /**
     * Reporte relevante.
     */
    private static final int REPORTE_RELEVANTE = 1;
    
    /**
     * Tipo de reporte de acuerdo al siguiente:
     * 1 = Relevante.
     * 2 = Inusual.
     * 3 = Preocupante.
     */
    private static final int TIPO_REPORTE = REPORTE_RELEVANTE;
    /**
     * Clave del &oacute;rgano supervisor.
     */
    private static final String ORGANO_SUPERVISOR = "40-002";
    /**
     * Clave del sujeto obligado.
     */
    private static final String SUJETO_OBLIGADO = "40-002";
    
    /**
     * Formateador de la fecha para los combos trimestrales.
     */
    private static final SimpleDateFormat formatFecha = new SimpleDateFormat("dd-MM-yyyy");
}
