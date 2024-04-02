/*
 * $Id: ReportePolizasContables.java,v 1.9.68.1 2015/10/15 18:00:34 mejiar Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2008 - 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica.pages.reportes;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.tapestry.IRequestCycle;

import net.sf.jasperreports.engine.JRException;

import com.ixe.isis.tapestry.IxenetValidationDelegate;
import com.ixe.ods.sica.Visit;
import com.ixe.ods.sica.pages.SicaPage;
import com.ixe.ods.sica.pages.reportes.dto.ReportePolizasDto;
import com.ixe.ods.sica.services.impl.XlsServiceImpl;
import com.ixe.ods.sica.services.model.ModeloXls;
import com.ixe.ods.sica.utils.DateUtils;

/**
 * P&aacute;gina que permite al usuario seleccionar las fechas de las polizas que desea consultar.
 *
 * @author PEspinosa
 * @version $Revision: 1.9.68.1 $ $Date: 2015/10/15 18:00:34 $
 */
public abstract class ReportePolizasContables extends SicaPage {

	/**
     * Asigna los valores de los componentes de la p&aacute;gina.
     *
     * @param cycle El ciclo de la p&acute;gina.
     */
    public void activate(IRequestCycle cycle) {
        //setDetallesTable(new ArrayList());
        setResource("ReportePolizasContablesXls");
        super.activate(cycle);
        setModo((String) cycle.getServiceParameters()[0]);

        Visit visit = (Visit) getVisit();
        setRegisterDate(null);
        setRegisterDateHasta(null);

    }

    /**
     * Busca los detalles con operaciones de compra y venta a imprimir.
     *
     * @param cycle El ciclo de la p&aacute;gina
     * @throws Exception
     * @throws IOException
     */
    public void reportes(IRequestCycle cycle) throws IOException, Exception {
        IxenetValidationDelegate delegate = (IxenetValidationDelegate)
                getBeans().getBean("delegate");
        if (getRegisterDate() == null) {
            delegate.record("Debe definir la fecha de b\u00fasqueda.", null);
            return;
        }
        if (getRegisterDateHasta() == null) {
            delegate.record("Debe definir la fecha de b\u00fasqueda.", null);
            return;
        }

        Calendar gc = new GregorianCalendar();
        gc.setTime(getRegisterDate());
        gc.add(Calendar.DAY_OF_YEAR, 1);
        gc.add(Calendar.SECOND, -1);
        setGc(gc.getTime());
        setRegisterDate(getRegisterDate());

        Date inicioDia = DateUtils.inicioDia(getRegisterDate());
        Date finDia = DateUtils.finDia(getRegisterDateHasta());
        Calendar cal = new GregorianCalendar();
        cal.setTime(finDia);
        cal.add(Calendar.DAY_OF_YEAR, 1);
        java.sql.Date inicioDiaSql = new java.sql.Date(inicioDia.getTime());
        java.sql.Date finDiaSql = new java.sql.Date(cal.getTime().getTime());
        List resultadoBusqueda = getReportesServiceData().findPolizasContables(
                inicioDiaSql, finDiaSql);
        setDetalles(resultadoBusqueda);

        if (getDetalles().isEmpty()) {
            delegate.record("No se encontraron datos con los Criterios de " +
                    "B\u00fasqueda especificados.", null);
            return;
        }
        else {
            delegate.clearErrors();//reset();
        }
        Object parameters[] = new Object[3];
        parameters[0] = "WEB-INF/jasper/RepPolizasContables.jasper";
        parameters[1] = "SReportePolizasContablesXls";
        parameters[2] = "directo";
        cycle.setServiceParameters(parameters);

        //Object parameters[] = cycle.getServiceParameters();
        String resource = (String) parameters[0];
        String name = (String) parameters[1];
        Map reportOutParams = null;
        if (parameters.length > 3) {
            reportOutParams = (Map) parameters[3];
        }
        try {
            HttpServletResponse response = cycle.getRequestContext().getResponse();

            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment; filename=\"" + name + ".xls\"");
            XlsServiceImpl generador = new XlsServiceImpl(new ModeloXls("Reporte Polizas Contables",
                    getTitulos(0),
                    getPropiedades(), getDataSource()));
            generador.escribirXls(response.getOutputStream());
        }
        catch (JRException e) {
            warn(e.getMessage(), e);
        }
        //Aqui se inserta en session el attributo que avisa que el reporte termino de ser procesado
		cycle.getRequestContext().getRequest().getSession().setAttribute(this.getResource(),"ok");
		//setDetallesTable(resultadoBusqueda);
		//setDetalles(resultadoBusqueda);
    }

    /**
     * Lista con las expresiones para las propiedades del dto
     *
     * @return List.
     */
    private List getPropiedades() {
        List propiedades = new ArrayList();
        propiedades.add("idPoliza");
        propiedades.add("fechaValor");
        propiedades.add("cuentaContable");
        propiedades.add("entidad");
        propiedades.add("cargoAbono");
        propiedades.add("idDivisa");
        propiedades.add("importe");
		propiedades.add("idDeal");
		propiedades.add("referencia");
		propiedades.add("fechaCreacion");
		propiedades.add("tipoDeal");
		propiedades.add("sucursalOrigen");	
		propiedades.add("cliente");
		propiedades.add("fechaProcesamiento");
		propiedades.add("statusProceso");
		propiedades.add("folioDetalle");
		propiedades.add("nombreCliente");
		propiedades.add("noContratoSica");
		propiedades.add("tipoDeCambio");
		propiedades.add("claveReferencia");
		return propiedades;
	}
	
	/**
	 * Lista con los titlos de los Headers
     * @param i
     * @return List.
     */
    private List getTitulos(int i) {
        List titulos = new ArrayList();
        titulos.add("ID Poliza");
        titulos.add("Fecha Valor");
        titulos.add("Cuenta Contable");
        titulos.add("Entidad");
        titulos.add("Cargo Abono");
        titulos.add("Id Divisa");
        titulos.add("Importe");
        titulos.add("Id Deal");
        titulos.add("Referencia");
        titulos.add("Fecha Creacion");
        titulos.add("Tipo Deal");
        titulos.add("Sucursal Origen");
        titulos.add("Cliente");
        titulos.add("Fecha Procesamiento");
        titulos.add("Status Proceso");
        titulos.add("Folio Detalle");
        titulos.add("Nombre Cliente");
        titulos.add("No ContratoSica");
        titulos.add("Tipo De Cambio");
        titulos.add("Clave Referencia");
        return titulos;
	}

    /**
     * Regresa el valor de resource.
     *
     * @return String.
     */
	public abstract String getResource();

    /**
     * Establece el valor de resource.
     *
     * @param resource El valor a asignar.
     */
	public abstract void setResource(String resource);

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
	 * Regresa el valor de registerDateHasta.
	 *
	 * @return Date.
	 */
	public abstract Date getRegisterDateHasta();

	/**
	 * Fija el valor de registerDateHasta.
	 *
	 * @param registerDateHasta El valor a asignar.
	 */
	public abstract void setRegisterDateHasta(Date registerDateHasta);

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
    public List getDetalles() {
        return this.detalles;
    }

    /**
     * Fija el valor de detalles.
     *
     * @param detalles El valor a asignar.
     */
    public void setDetalles(List detalles) {
        this.detalles = detalles;
    }

    /**
     * Establece el valor de modo.
     *
     * @param modo El valor a asignar.
     */
    public abstract void setModo(String modo);

    /**
	 * Obtiene el DataSource del reporte de Polizas.
	 * 
	 * @return List
     */
    public List getDataSource(String arg0) {
        List dealDetallesMap = new ArrayList();
        for (Iterator it = detalles.iterator(); it.hasNext();) {
            Map row = (Map) it.next();
            //Object[] tupla = (Object[]) it.next();
            HashMap map = new HashMap();
            map.put("nombreCliente", row.get("NOMBRE_CORTO"));
            map.put("idPoliza", row.get("ID_POLIZA"));
            map.put("fechaValor", new Date(((Timestamp) row.get("FECHA_VALOR")).getTime()));
            map.put("cuentaContable", row.get("CUENTA_CONTABLE"));
            map.put("entidad", row.get("ENTIDAD"));
            map.put("cargoAbono", row.get("CARGO_ABONO"));
            map.put("idDivisa", row.get("ID_DIVISA"));
            map.put("importe", new Double(row.get("IMPORTE").toString()));
            map.put("idDeal", new Integer(row.get("ID_DEAL").toString()));
            map.put("referencia", row.get("REFERENCIA"));
            map.put("fechaCreacion", new Date(((Timestamp) row.get("FECHA_CREACION")).getTime()));
            map.put("tipoDeal", row.get("TIPO_DEAL"));
            map.put("sucursalOrigen", row.get("SUCURSAL_ORIGEN"));
            map.put("cliente", row.get("CLIENTE") != null ? row.get("CLIENTE") : "");
            map.put("fechaProcesamiento", row.get("FECHA_PROCESAMIENTO"));
            map.put("statusProceso", (new Integer(row.get("STATUS_PROCESO").toString())));
            map.put("folioDetalle", new Integer(row.get("FOLIO_DETALLE").toString()));
            map.put("noContratoSica", row.get("NO_CONTRATO_SICA"));
            map.put("tipoDeCambio", new Double(row.get("TIPO_CAMBIO").toString()));
            map.put("claveReferencia", row.get("CLAVE_REFERENCIA") == null ?
                    "" : row.get("CLAVE_REFERENCIA"));
            dealDetallesMap.add(map);
        }
        return dealDetallesMap;
	}
	
	/**
     * Obtiene el DataSource del reporte de Polizas.
     *
     * @return list
     */
    public List getDataSource() {
        List dealDetallesMap = new ArrayList();
        for (Iterator it = detalles.iterator(); it.hasNext();) {
            Map row = (Map) it.next();

            ReportePolizasDto rowObj = new ReportePolizasDto(
                    (String) row.get("NOMBRE_CORTO"),
                    "" + row.get("ID_POLIZA"),
                    new Date(((Timestamp) row.get("FECHA_VALOR")).getTime()),
                    (String) row.get("CUENTA_CONTABLE"),
                    (String) row.get("ENTIDAD"),
                    (String) row.get("CARGO_ABONO"),
					(String)row.get("ID_DIVISA"),
					new Double(row.get("IMPORTE").toString()),
					new Integer(row.get("ID_DEAL").toString()),
					(String)row.get("REFERENCIA"),
					new Date(((Timestamp)row.get("FECHA_CREACION")).getTime()),
					(String)row.get("TIPO_DEAL"),
					(String)row.get("SUCURSAL_ORIGEN"),
					(String)(row.get("CLIENTE") != null ? row.get("CLIENTE") : ""),
					(String)row.get("FECHA_PROCESAMIENTO"),
					(new Integer(row.get("STATUS_PROCESO").toString())),
					new Integer(row.get("FOLIO_DETALLE").toString()),
					(String)row.get("NO_CONTRATO_SICA"),
					 new Double(row.get("TIPO_CAMBIO").toString()),
                    (String) (row.get("CLAVE_REFERENCIA") == null
                            ? "" : row.get("CLAVE_REFERENCIA")));
			dealDetallesMap.add(rowObj);			
			
		}
		return dealDetallesMap;
	}
	
    List detalles = null;
}
