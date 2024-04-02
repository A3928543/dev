/*
 * $Id: ReportesDiariosFiltrados.java,v 1.1.2.7.2.1.56.1 2020/12/01 04:53:01 mejiar Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 - 2009 LegoSoft S.C.
 */
package com.ixe.ods.sica.pages.reportes;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
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

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.tapestry.IRequestCycle;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.util.JRLoader;

import com.ixe.isis.tapestry.IxenetValidationDelegate;
import com.ixe.ods.bup.model.Direccion;
import com.ixe.ods.bup.model.EmpleadoIxe;
import com.ixe.ods.bup.model.PersonaFisica;
import com.ixe.ods.bup.model.PersonaMoral;
import com.ixe.ods.bup.model.Telefono;
import com.ixe.ods.jasper.ListDataSource;
import com.ixe.ods.sica.Constantes;
import com.ixe.ods.sica.Num;
import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.SupportEngine;
import com.ixe.ods.sica.model.*;
import com.ixe.ods.sica.pages.SicaPage;
import com.ixe.ods.sica.services.impl.XlsServiceImpl;
import com.ixe.ods.sica.services.model.ModeloXls;
import com.ixe.ods.sica.utils.DateUtils;
import com.ixe.ods.tapestry.BaseGlobal;
import com.ixe.ods.tapestry.services.jasper.DataSourceProvider;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import org.apache.tapestry.IRequestCycle;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;

/**
 * P&aacute;gina que permite al usuario buscar e imprimir los reportes de
 * diarios
 * 
 * @author lvillegas
 */
public abstract class ReportesDiariosFiltrados extends SicaPage implements DataSourceProvider {

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
		setDetallesTable(new ArrayList());
        setFechaInicial(new Date());
        setFechaFinal(new Date());
        setMontoMinimo(0.0);
        setMontoMaximo(0.0);
        setFiltrosMinimizados(false);
		super.activate(cycle);

        // Combo canal.
        setCanalesList(getSicaServiceData().findAllCanales());
        setSeleccionaCanal(new boolean[getCanalesList().size()]);
        // Combo promotores
        setPromotoresList(getSicaServiceData().
            findAllPromotoresSICA(((SupportEngine) getEngine()).getApplicationName()));
        setSeleccionaPromotor(new boolean[getPromotoresList().size()]);
        // Combo divisas
		setDivisasFrecuentesList(getSicaServiceData().findDivisasFrecuentes());
        setSeleccionaDivisa(new boolean[getDivisasFrecuentesList().size()]);
        // Combo operaciones
        generaListaOperaciones();
        setSeleccionaOperacion(new boolean[getTipoOperacionesList().size()]);
        // Combo de excepciones
        generaListaExcepciones();
        setSeleccionaExcepcion(new boolean[getExcepcionesList().size()]);
        // Combo de zonas
        generaListaZonas();
        setSeleccionaZona(new boolean[getZonasList().size()]);
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
        setFechaInicial(new Date());
        setFechaFinal(new Date());
        setMontoMinimo(0.0);
        setMontoMaximo(0.0);
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
        
        if (delegate.getHasErrors()) {
        	return;
        	}
        
        if (getFechaInicial() == null) {
            delegate.record("Debe definir la fecha inicial de b\u00fasqueda.", null);
            return;
        }
        if (getFechaFinal() == null) {
            delegate.record("Debe definir la fecha final de b\u00fasqueda.", null);
            return;
        }
        
        if (getFechaFinal().getTime() < getFechaInicial().getTime()) {
            delegate.record("La fecha inicial no puede ser mayor a la fecha final.", null);
            return;
        }

        if((getFechaFinal().getTime() - getFechaInicial().getTime()) > 
        ((long)1000 * 60 * 60 * 24 * 31)){
        	delegate.record("El rango de fechas no puede ser mayor a un mes.", null);
            return;
        }
        
        if (getMontoMinimo() < 0 || getMontoMaximo() < 0) {
            delegate.record("Los rangos de los montos deben ser mayores a cero.", null);
            return;
        }
        if (getMontoMinimo() < 0 || getMontoMaximo() < 0) {
            delegate.record("Los rangos de los montos deben ser mayores a cero.", null);
            return;
        }
        if (getMontoMinimo() > getMontoMaximo()) {
            delegate.record("El monto maximo debe ser mayor al monto minimo.", null);
            return;
        }
        
        StringBuffer filtros = new StringBuffer();
        boolean aplicaFiltro = false;
        filtros.append("fechaInicial:" + getFechaInicial() + ",");
        filtros.append("fechaFinal:" + getFechaFinal() + ",");
        if (getMontoMaximo() > 0) {
            filtros.append("montoMinimo:" + getMontoMinimo() + ",");
            filtros.append("montoMaximo:" + getMontoMaximo() + ",");
        }
        
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
        
        if (getContratosSica() != null && getContratosSica().length() > 0) {
            filtros.append("Contratos:" + getContratosSica());
        }
        
        auditar(null, "Reporte Diario",
            filtros.length() < Num.I_512 ?
                filtros.toString() :
                filtros.toString().substring(0, Num.I_512), "INFO", "E");
        
        setDetallesTable(
            getSicaServiceData().findReporteDealsDiarios(
                getCanalesSeleccionados(),
                getPromotoresSeleccionados(),
                getDivisasSeleccionadas(),
                getExcepcionesSeleccionadas(),
                getZonasSeleccionadas(),
                getOperacionesSeleccionadas().contains("COMPRA"),
                getOperacionesSeleccionadas().contains("VENTA"),
                DateUtils.inicioDia(getFechaInicial()),
                DateUtils.finDia(getFechaFinal()),
                new Double(getMontoMinimo()),
                new Double(getMontoMaximo()),
                getContratosSica()
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
     * Obtiene el DataSource del reporte.
     * @param id del reporte para crear el DataSource adecuado.
     *
     * @return JRDataSource.
     * @see com.ixe.ods.tapestry.services.jasper.DataSourceProvider#getDataSource(String id)
     */
	public JRDataSource getDataSource(String id) {
        List resultado = new ArrayList();
        if (id.equals("0")) {
            resultado = getDataSourceReporteDiario();
        }
        else if (id.equals("1")) {
            resultado.add(new HashMap());
        }
        return new ListDataSource(resultado);
    }
    
    /**
     * Obtiene el Map del reporte.
     * @param id del reporte para crear el mapa adecuado.
     * 
     * @return Map
     */
    public Map getParametros(String id) {
        Map mapa = null;
        try {
            if (id.equals("1")) {
                mapa = getParametrosReporteSemanal();
            }
        }
        catch (JRException e) {
            debug(e.getMessage(), e);
        }
        catch (IOException e) {
            debug(e.getMessage(), e);
        }
        return mapa;
    }
    
    /**
     * Obtiene el DataSource del reporte de Diarios.
     *
     * @return List.
     */
    private List getDataSourceReporteDiario() {
        List dealDetallesMap = new ArrayList();
        for (Iterator it = getDetallesTable().iterator(); it.hasNext();) {
            DetalleReporteDiario element = (DetalleReporteDiario) it.next();
            Map map = new HashMap();
            map.put("divisa", element.getDealDetalle().getDivisa().getIdDivisa());
            map.put("claveFormaLiquidacion", element.getDealDetalle().getClaveFormaLiquidacion());
            map.put("isRecibimos", element.getDealDetalle().isRecibimos() ? "Compra" : "Venta");
            map.put("canal", element.getDealDetalle().getDeal().getCanalMesa().getCanal().getIdCanal());
            String pat = "";
            String mat = "";
            String nom = "";
            if (element.getDealDetalle().getDeal().getPromotor().getPaterno() != null) {
                pat = element.getDealDetalle().getDeal().getPromotor().getPaterno();
            }
            if (element.getDealDetalle().getDeal().getPromotor().getMaterno() != null) {
                mat = element.getDealDetalle().getDeal().getPromotor().getMaterno();
            }
            if (element.getDealDetalle().getDeal().getPromotor().getNombre() != null) {
                nom = element.getDealDetalle().getDeal().getPromotor().getNombre();
            }
            map.put("promotor", pat + " " + mat + " " + nom);
            map.put("idDeal", new Integer(element.getDealDetalle().getDeal().getIdDeal()));
            map.put("folioDetalle", new Integer(element.getDealDetalle().getFolioDetalle()));
            map.put("tipoCambio", new Double(element.getDealDetalle().getTipoCambio()));
            map.put("monto", new Double(element.getDealDetalle().getMonto()));
            map.put("importe", new Double(element.getDealDetalle().getImporte()));
            if (element.getDealDetalle().getDeal().getContratoSica() != null) {
            	map.put("contrato_SICA", element.getDealDetalle().getDeal().getContratoSica().
            			getNoCuenta());
                map.put("cliente", element.getDealDetalle().getDeal().getCliente().
                		getNombreCompleto());
                map.put("tipoPersona", element.getDealDetalle().getDeal().getCliente().
                		getTipoPersona());
                map.put("nacionalidad", element.getDealDetalle().getDeal().getCliente().
                		getNacionalidad());
                map.put("usuarioCliente", element.getDealDetalle().getDeal().getEsCliente());
                if (element.getDealDetalle().getDeal().getDirFactura() != null) {
                	map.put("codigoPostal", element.getDealDetalle().getDeal().getDirFactura().
                			getCodigoPostal());
                	map.put("municipio", element.getDealDetalle().getDeal().getDirFactura().
                			getDelegacionMunicipio());
                	map.put("estado", element.getDealDetalle().getDeal().getDirFactura().
                			getIdEntFederativa());
                }
            }
            else {
            	map.put("contrato_SICA","");
                map.put("cliente", "");
                map.put("tipoPersona", "");
                map.put("nacionalidad", "");
                map.put("usuarioCliente", "");
                map.put("codigoPostal", "");
                map.put("estado", "");
            }
            map.put("fechaCaptura", element.getDealDetalle().getDeal().getFechaCaptura());
            map.put("fechaLiquidacion", element.getDealDetalle().getDeal().getFechaLiquidacion());
            map.put("status", element.getDealDetalle().getDescripcionStatus());
            map.put("tipoZona", element.getDealDetalle().getDeal().getTipoZona());
            dealDetallesMap.add(map);
        }
        return dealDetallesMap;
    }

    /**
     * Obtiene el DataSource del reporte semanal.
     *
     * @return List.
     * @throws IOException si se produce un error al cargar el reporte.
     * @throws JRException  si se produce un error al crear el reporte.
     */
    private Map getParametrosReporteSemanal() throws JRException, IOException {
        Map map = new HashMap();
        Map centrosTuristicos = new HashMap();
        Map centrosFronterizos = new HashMap();
        int totalOperacionesPF = 0;
        double totalImporteOperacionesPF = 0;
        int totalOperacionesPM = 0;
        double totalImporteOperacionesPM = 0;
        int totalOperacionesTotal = 0;
        double totalImporteOperacionesTotal = 0;
        for (Iterator it = getDetallesTable().iterator(); it.hasNext();) {
            DetalleReporteDiario detalle = (DetalleReporteDiario) it.next();
            DetalleReporteOperacionSucursal reporte;
            String sucursal = detalle.getDealDetalle().getDeal().
                getCanalMesa().getCanal().getIdCanal();
            String zona = detalle.getDealDetalle().getDeal().getTipoZona();
            String tipoPersona = detalle.getDealDetalle().getDeal().getCliente().getTipoPersona();
            double importe = detalle.getDealDetalle().getImporte();
            if (zona != null && zona.equals(Constantes.ZONA_TURISTICA)) {
                if (centrosTuristicos.containsKey(sucursal)) {
                    reporte = (DetalleReporteOperacionSucursal) centrosTuristicos.get(sucursal);
                }
                else {
                    reporte = new DetalleReporteOperacionSucursal();
                }
                centrosTuristicos.put(sucursal,
                    actualizaDetalleReporteOperacionSucursal(
                        reporte,
                        sucursal,
                        tipoPersona,
                        detalle.getDealDetalle().getImporte()));
            }
            else if (zona != null && zona.equals(Constantes.ZONA_FRONTERIZA)) {
                if (centrosFronterizos.containsKey(sucursal)) {
                    reporte = (DetalleReporteOperacionSucursal) centrosFronterizos.get(sucursal);
                }
                else {
                    reporte = new DetalleReporteOperacionSucursal();
                }
                centrosFronterizos.put(sucursal,
                    actualizaDetalleReporteOperacionSucursal(
                        reporte,
                        sucursal,
                        tipoPersona,
                        detalle.getDealDetalle().getImporte()));
            }
            else {
                continue;
            }
            if (tipoPersona.equals(Constantes.PERSONA_FISICA)) {
                totalOperacionesPF++;
                totalImporteOperacionesPF += importe;
            }
            else {
                totalOperacionesPM++;
                totalImporteOperacionesPM += importe;
            }
        }
        totalOperacionesTotal = totalOperacionesPF + totalOperacionesPM;
        totalImporteOperacionesTotal = totalImporteOperacionesPF + totalImporteOperacionesPM;
        
        List listaTuristicos = new ArrayList();
        for (Iterator it = centrosTuristicos.keySet().iterator(); it.hasNext();) {
            listaTuristicos.add(detalleReporteOperacionSucursalToMap(
                (DetalleReporteOperacionSucursal) centrosTuristicos.get(it.next())));
        }
        map.put("subreporteTuristicosDS", new JRBeanCollectionDataSource(listaTuristicos));
        map.put("subreporteTuristicosParam", new HashMap());
        map.put("subreporteTuristicosRef",
            JRLoader.loadObject(((BaseGlobal) getGlobal()).getApplicationContext().getResource(
                "WEB-INF/jasper/SubreporteSeguimientoSemanalXls.jasper").getInputStream()));
        
        List listaFronterizos = new ArrayList();
        for (Iterator it = centrosFronterizos.keySet().iterator(); it.hasNext();) {
            listaFronterizos.add(detalleReporteOperacionSucursalToMap(
                (DetalleReporteOperacionSucursal) centrosFronterizos.get(it.next())));
        }
        map.put("subreporteFronterizasDS", new JRBeanCollectionDataSource(listaFronterizos));
        map.put("subreporteFronterizasParam", new HashMap());
        map.put("subreporteFronterizasRef",
            JRLoader.loadObject(((BaseGlobal) getGlobal()).getApplicationContext().getResource(
                "WEB-INF/jasper/SubreporteSeguimientoSemanalXls.jasper").getInputStream()));
        
        if (listaTuristicos.size() == 0 && listaFronterizos.size() == 0) {
            throw new SicaException("El reporte que desea generar no tiene informacion " +
                "relacionada a zonas turisticas y fronterizas.");
        }
        
        map.put("totalOperacionesPF", new Integer(totalOperacionesPF));
        map.put("totalImporteOperacionesPF", new Double(totalImporteOperacionesPF));
        map.put("totalOperacionesPM", new Integer(totalOperacionesPM));
        map.put("totalImporteOperacionesPM", new Double(totalImporteOperacionesPM));
        map.put("totalOperacionesTotal", new Integer(totalOperacionesTotal));
        map.put("totalImporteOperacionesTotal", new Double(totalImporteOperacionesTotal));
        return map;
    }
    
    /**
     * Actualiza el objeto DetalleReporteOperacionSucursal seg&uacute;n el tipo de persona
     * que se est&eacute; operando.
     * @param reporte objeto que se va a actualizar.
     * @param sucursal que se est&aacute; utilizando.
     * @param tipoPersona tipo de persona de la operaci&oacute;n.
     * @param importe de la operaci&oacute;n.
     * @return DetalleReporteOperacionSucursal.
     */
    private DetalleReporteOperacionSucursal actualizaDetalleReporteOperacionSucursal(
            DetalleReporteOperacionSucursal reporte, String sucursal,
            String tipoPersona, double importe) {
        reporte.setNombre(sucursal);
        if (tipoPersona.equals(Constantes.PERSONA_FISICA)) {
            reporte.setNumeroOperacionesPF(
                new Integer(reporte.getNumeroOperacionesPF().intValue() + 1));
            reporte.setImporteOperacionesPF(
                new Double(reporte.getImporteOperacionesPF().doubleValue() + importe));
        }
        else {
            reporte.setNumeroOperacionesPM(
                new Integer(reporte.getNumeroOperacionesPM().intValue() + 1));
            reporte.setImporteOperacionesPM(
                new Double(reporte.getImporteOperacionesPM().doubleValue() + importe));
        }
        reporte.setNumeroOperacionesTotal(
            new Integer(reporte.getNumeroOperacionesPF().intValue() +
                reporte.getNumeroOperacionesPM().intValue()));
        reporte.setImporteOperacionesTotal(
            new Double(reporte.getImporteOperacionesPF().doubleValue() +
                reporte.getImporteOperacionesPM().doubleValue()));
        return reporte;
    }

    /**
     * Convierte un objeto de tipo DetalleReporteOperacionSucursal a un mapa para su uso
     * en los reportes.
     * @param detalle que contiene la informaci&oacute;n a usar.
     * @return mapa.
     */
    private Map detalleReporteOperacionSucursalToMap(
            DetalleReporteOperacionSucursal detalle) {
        Map mapa = new HashMap();
        mapa.put("nombre", detalle.getNombre());
        mapa.put("numeroOperacionesPF", detalle.getNumeroOperacionesPF());
        mapa.put("importeOperacionesPF", detalle.getImporteOperacionesPF());
        mapa.put("numeroOperacionesPM", detalle.getNumeroOperacionesPM());
        mapa.put("importeOperacionesPM", detalle.getImporteOperacionesPM());
        mapa.put("numeroOperacionesTotal", detalle.getNumeroOperacionesTotal());
        mapa.put("importeOperacionesTotal", detalle.getImporteOperacionesTotal());
        return mapa;
    }
    
    /**
     * Imprime el reporte Diario en formato de Excel
     *
     * @param cycle El ciclo de la P&aacute;gina
     */
    public void imprimirReporteDiario(IRequestCycle cycle) {
        List modelosXls = new ArrayList();

        modelosXls.add(new ModeloXls("Operaciones de Efectivo:", new String[]{"Canal", "C.P.",
        		"Municipio", "Estado", "Divisa",
        		"Producto", "Operaci\u00f3n", "Promotor", "No. Deal", "Folio", "Tipo de Cambio", 
        		"Monto", "Importe", "Contrato SICA", "Cliente", "Fecha Captura", 
        		"Fecha Liquidaci\u00f3n", "Status",
        		"Tipo de Zona", "Tipo de Persona", "Nacionalidad", "Es Cliente" },
        		new String[] { "canal", "codigoPostal", "municipio", "estado", "divisa",
        		"claveFormaLiquidacion", "isRecibimos", "promotor", "idDeal", "folioDetalle",
        		"tipoCambio", "monto", "importe", "contrato_SICA", "cliente", "fechaCaptura", 
        		"fechaLiquidacion","status", "tipoZona", "tipoPersona", "nacionalidad", 
        		"usuarioCliente" }, getDataSourceReporteDiario()));
        XlsServiceImpl.generarEscribir(cycle.getRequestContext().getResponse(),
                "ReporteDiario" + DATE_FORMAT.format(new Date()) + ".xls", modelosXls);
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
     * Genera el reporte en formato de Excel.
     * @param cycle de la petici&oacute;n.
     * @param resource que se va a utilizar.
     * @param reportOutParams par&aacute;metros.
     */
    private void imprimirReporteDiarioXls(
            IRequestCycle cycle, String resource, String reportOutParams) {
        JasperPrint jasperPrint = null;
        try {
            JRDataSource dataSource = getDataSource(reportOutParams);
            Map params = getParametros(reportOutParams);
            BaseGlobal global = (BaseGlobal) getGlobal();
            InputStream inputStream = global.getApplicationContext().
                    getResource(resource).getInputStream();
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(inputStream);
            jasperPrint = JasperFillManager.fillReport(jasperReport, params, dataSource);
              
            HttpServletResponse response = cycle.getRequestContext().getResponse();
            JRXlsExporter exporter = new JRXlsExporter();
            ByteArrayOutputStream xlsReport = new ByteArrayOutputStream();
            exporter.setParameter(
                JRExporterParameter.JASPER_PRINT,
                jasperPrint);
            exporter.setParameter(
                JRXlsExporterParameter.IS_AUTO_DETECT_CELL_TYPE,
                Boolean.TRUE);
            exporter.setParameter(
                JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,
                Boolean.TRUE);
            exporter.setParameter(
                JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND,
                Boolean.FALSE);
            exporter.setParameter(
                JRExporterParameter.OUTPUT_STREAM,
                xlsReport);
            exporter.exportReport();
            byte[] bytes2 = xlsReport.toByteArray();
            response.setContentType("application/vnd.ms-excel");
            response.setContentLength(bytes2.length);
            response.setHeader("Content-disposition",
                "attachment; filename=\"" +
                "ReporteDiarioXls.xls\"");
            
                ServletOutputStream ouputStream = response.getOutputStream();
                ouputStream.write(bytes2, 0, bytes2.length);
                ouputStream.flush();
                ouputStream.close();
           
        }
        catch (JRException e) {
        	_logger.error(e.getMessage(), e);
        	getDelegate().record("No se pudo generar el archivo de reporte.", null);
           // debug(e.getMessage(), e);
        }
        catch (IOException e) {
        	getDelegate().record("Error al generar el archivo de reporte.", null);
        	_logger.error(e.getMessage(), e);
            //debug(e.getMessage(), e);
        }
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
	 * Regresa el valor de fechaInicial.
	 *
	 * @return Date.
	 */  
	public abstract Date getFechaInicial();

	/**
	 * Fija el valor de fechaInicial.
	 *
	 * @param fecha El valor a asignar.
	 */
	public abstract void setFechaInicial(Date fecha);

    /**
     * Regresa el valor de fechaFinal.
     *
     * @return Date.
     */  
    public abstract Date getFechaFinal();

    /**
     * Fija el valor de fechaFinal.
     *
     * @param fecha El valor a asignar.
     */
    public abstract void setFechaFinal(Date fecha);
    
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
     * Reporte inusual.
     */
    private static final int REPORTE_INUSUAL = 2;
    /**
     * Reporte preocupante.
     */
    private static final int REPORTE_PREOCUPANTE = 3;
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
}
