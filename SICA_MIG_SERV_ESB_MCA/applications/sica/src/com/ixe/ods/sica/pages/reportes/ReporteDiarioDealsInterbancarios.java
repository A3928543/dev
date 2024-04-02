/*
 * $Id: ReporteDiarioDealsInterbancarios.java,v 1.18 2009/08/06 14:53:11 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 LegoSoft S.C.
 */
package com.ixe.ods.sica.pages.reportes;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IPropertySelectionModel;

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

import com.ixe.ods.jasper.ListDataSource;
import com.ixe.ods.sica.Constantes;
import com.ixe.ods.sica.Visit;
import com.ixe.ods.sica.model.Broker;
import com.ixe.ods.sica.model.Deal;
import com.ixe.ods.sica.model.DealDetalle;
import com.ixe.ods.sica.model.Divisa;
import com.ixe.ods.sica.model.MesaCambio;
import com.ixe.ods.sica.pages.SicaPage;
import com.ixe.ods.sica.utils.DateUtils;
import com.ixe.ods.tapestry.BaseGlobal;
import com.ixe.ods.tapestry.services.jasper.DataSourceProvider;
import com.legosoft.tapestry.model.RecordSelectionModel;

/**
 * Clase para generar el reporte diario de Deals Interbancarios.
 *
 * @author Gustavo Gonzalez
 * @version $Revision: 1.18 $ $Date: 2009/08/06 14:53:11 $
 */
public abstract class ReporteDiarioDealsInterbancarios extends SicaPage
        implements DataSourceProvider {

	/**
	 * Activate de la p&aacute;gina. Genera el reporte al momento de entrar a la pagina.
	 *
	 * @param cycle El ciclo de la p&aacute;gina.
	 */
	public void activate(IRequestCycle cycle) {
		super.activate(cycle);
    	setRegisterDateDesde(null);
    	setRegisterDateHasta(null);
		setDivisaSeleccionada(null);
		setIdPersonaBroker(null);
		setBrokerSeleccionado(null);
	}

	/**
     * Busca los detalles con operaciones de compra y venta a imprimir.
     */
    public void busquedaDeals() {
		if (getRegisterDateDesde() == null) {
			getDelegate().record("Debe definir la fecha de b\u00fasqueda. ", null);
		}
		if (getRegisterDateHasta() == null) {
			getDelegate().record("Debe definir la fecha de b\u00fasqueda.", null);
		}
        setIdPersonaBroker((Integer) getBrokerSeleccionado().get("id"));
        setMesaSeleccionada(getMesaSeleccionada());
        List resultadoBusqueda = getSicaServiceData().findAllDealsInterbancariosReporte(
                DateUtils.inicioDia(getRegisterDateDesde()),
                DateUtils.finDia(getRegisterDateHasta()), getDivisaSeleccionada().getIdDivisa(),
                getMesaSeleccionada().getIdMesaCambio(), getIdPersonaBroker());
        setListaDetalles(resultadoBusqueda);
        if (getListaDetalles().isEmpty()) {
            getDelegate().record("No se encontraron datos con los Criterios de B\u00fasqueda " +
                    "especificados.", null);
        }
        setListaDetalles(sortDealDetallesByCliente(getListaDetalles()));

    }

   	/**
	 * Obtiene la lista con los datos necesarios para el reporte.
	 *
	 * @param id El ParametroSica necesario para el Data Source.
	 * @see com.ixe.ods.tapestry.services.jasper.DataSourceProvider#getDataSource(String id)
     * @return JRDataSource.
	 */
	public JRDataSource getDataSource(String id) {
		List detallesMap = new ArrayList();
        String ticket = ((Visit) getVisit()).getTicket();
        for (Iterator iter = getListaDetalles().iterator(); iter.hasNext();) {
			DealDetalle det = (DealDetalle) iter.next();
            Map map = new HashMap();
            map.put("fechaOperacion", getRegisterDateDesde());
            map.put("divisa", det.getDivisa().getDescripcion());
            map.put("idDealInteger", new Integer(det.getDeal().getIdDeal()));
            map.put("folioDetalle", new Integer(det.getFolioDetalle()));
            map.put("hora", det.getDeal().getFechaCaptura());
            if (Constantes.CASH.equals(det.getDeal().getTipoValor())) {
                map.put("fechaValor", "HOY");
            }
            else {
                map.put("fechaValor", det.getDeal().getTipoValor());
            }
            map.put("fecha", det.getDeal().getFechaLiquidacion());
            if (det.isRecibimos()) {
                map.put("operacion", "Compra");
            }
            else {
                map.put("operacion", "Venta");
			}
			map.put("cliente", det.getDeal().getCliente().getNombreCompleto());
			if (det.getClaveFormaLiquidacion() != null) {
				map.put("producto", getFormasPagoLiqService().getNombreFormaLiquidacion(ticket,
                        det.getClaveFormaLiquidacion()));
			}
			else {
				map.put("producto", "");
			}
			map.put("monto", new Double(det.getMonto()));
            map.put("tipoCambio", new Double(det.getTipoCambio()));
            if (det.getDeal().getTipoCambioCobUsdDiv() != null &&
                    !Divisa.DOLAR.equals(det.getDivisa().getIdDivisa())) {
                map.put("tipoCambioInt",
                        Double.valueOf(det.getDeal().getTipoCambioCobUsdDiv().toString()));
            }
            else {
                map.put("tipoCambioInt", new Double(0));
            }
            map.put("montoMN", new Double(det.getMonto() * det.getTipoCambio()));
            String[] datosConf = det.getDatosConfirmacion() != null ?
                    det.getDatosConfirmacion().split("\\|") : new String[]{"", "", ""};
            map.put("horaConf", datosConf[0]);
            map.put("contacto", datosConf[1]);
            map.put("usuario", datosConf[2]);
			detallesMap.add(map);
		}
		return new ListDataSource(detallesMap);
    }

	/**
     * Ordena los Detalles de Deal alamcenados en la
     * lista por nombre del Cliente.
     *
     * @param dd La lista de Detalles de Deal.
     * @return List.
     */
    private List sortDealDetallesByCliente(List dd) {
        Collections.sort(dd, new Comparator() {
            public int compare(Object o1, Object o2) {
            	DealDetalle dd1 = (DealDetalle) o1;
    			DealDetalle dd2 = (DealDetalle) o2;
            	int comp = dd1.getDivisa().getIdDivisa().compareTo(dd2.getDivisa().getIdDivisa());
            	if (comp != 0) {
        			//Devuelve el valor de la comprarcion por Id Divisa
        			return comp;
        		}
        		else {
        			return dd1.getDeal().getCliente().getNombreCompleto().
                            compareTo(dd2.getDeal().getCliente().getNombreCompleto());
        		}
            }
        });
        return dd;
    }

    /**
     * Obtiene le data source del reporte; genera el documento en formato Pdf.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void imprimirReporte(IRequestCycle cycle) {
        try {
            setDescargando(true);
            busquedaDeals();
            Object parameters[] = new Object[]{ "WEB-INF/jasper/" +
                    "ReporteDiarioDealsInterbancarios.jasper", "ReporteDiarioDealsInterbancarios",
                    "0"};
            String resource = "WEB-INF/jasper/ReporteDiarioDealsInterbancarios.jasper";
            String name = "ReporteDiarioDealsInterbancarios";
            String id = "0";
            Map reportOutParams = null;
            if (parameters.length > 3) {
                reportOutParams = (Map) parameters[3];
            }
            JasperPrint jasperPrint;
            JRDataSource dataSource = getDataSource(id);
            BaseGlobal global = (BaseGlobal) getGlobal();
            InputStream inputStream = global.getApplicationContext().
                    getResource(resource).getInputStream();
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(inputStream);
            jasperPrint = JasperFillManager.fillReport(jasperReport, reportOutParams, dataSource);
            byte[] bytes = JasperExportManager.exportReportToPdf(jasperPrint);
            HttpServletResponse response = cycle.getRequestContext().getResponse();
            response.setContentType("application/pdf");
            response.setContentLength(bytes.length);
            response.setHeader("Content-Disposition", "attachment;filename=\"" + name + ".pdf" +
                    "\"");
            ServletOutputStream ouputStream = response.getOutputStream();
            ouputStream.write(bytes, 0, bytes.length);
            ouputStream.flush();
            ouputStream.close();
        }
        catch (IOException ioex) {
            warn(ioex.getMessage(), ioex);
        }
        catch (JRException e) {
            warn(e.getMessage(), e);
        }
        finally {
            setDescargando(false);
        }
    }

    /**
     * Obtiene le data source del reporte; genera el documento en formato Xls.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void imprimirReporteXls(IRequestCycle cycle) {
        try {
            setDescargando(true);
            busquedaDeals();
            Object parameters[] = new Object[]{"WEB-INF/jasper/" +
                    "ReporteDiarioDealsInterbXls.jasper", "ReporteDiarioDealsInterbXls", "0"};
            String resource = "WEB-INF/jasper/ReporteDiarioDealsInterbXls.jasper";
            String name = "ReporteDiarioDealsInterbXls";
            Map reportOutParams = null;
            if (parameters.length > 3) {
                reportOutParams = (Map) parameters[3];
            }
            JasperPrint jasperPrint;
            JRDataSource dataSource = getDataSource("");
            BaseGlobal global = (BaseGlobal) getGlobal();
            InputStream inputStream = global.getApplicationContext().getResource(resource).
                    getInputStream();
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
        catch (IOException ioex) {
            warn(ioex.getMessage(), ioex);
        }
        catch (JRException e) {
            warn(e.getMessage(), e);
        }
        finally {
            setDescargando(false);
        }
    }

    /**
     * Regresa un RecordSelectionModel con todas las divisas.
     *
     * @return IPropertySelectionModel
     */
    public IPropertySelectionModel getDivisasModel() {
    	List divisas = getSicaServiceData().findAllDivisasByOrdenAlfabetico();
    	Divisa primerElemento = new Divisa();
    	primerElemento.setIdDivisa("0");
    	primerElemento.setDescripcion("TODAS");
    	divisas.add(0, primerElemento);
        return new RecordSelectionModel(divisas, "idDivisa", "descripcion");
    }

    /**
     * Regresa un RecordSelectionModel con todas las mesas.
     *
     * @return IPropertySelectionModel
     */
    public IPropertySelectionModel getMesasModel() {
    	List mesas = getSicaServiceData().findAllMesas();
    	MesaCambio primerElemento = new MesaCambio();
    	primerElemento.setIdMesaCambio(0);
    	primerElemento.setNombre("TODAS");
    	mesas.add(0, primerElemento);
        return new RecordSelectionModel(mesas, "idMesaCambio", "nombre");
    }

    /**
     * Regresa un RecordSelectionModel con los clientes para
     * deals interbancarios.
     *
     * @return IPropertySelectionModel
     */
    public IPropertySelectionModel getBrokersModel() {
    	List brokers = getSicaServiceData().findAllBrokers();
    	List brokersList = new ArrayList();
    	for (Iterator it = brokers.iterator(); it.hasNext();) {
    		Broker b = (Broker) it.next();
    		HashMap map = new HashMap();
    		map.put("id", b.getId().getPersonaMoral().getIdPersona());
    		map.put("razonSocial", b.getId().getPersonaMoral().getRazonSocial());
    		brokersList.add(map);
    	}
    	HashMap primerElemento = new HashMap();
    	primerElemento.put("id", new Integer(0));
		primerElemento.put("razonSocial", "TODOS");
    	brokersList.add(0, primerElemento);
    	return new RecordSelectionModel(brokersList, "id", "razonSocial");
    }

    /**
     * Regresa el valor de gc.
     *
     * @return Date
     */
    public abstract Date getGc();

    /**
     * Fija el gc de registerdate.
     *
     * @param gc El valor a asignar.
     */
    public abstract void setGc(Date gc);

    /**
     * Obtiene el valor de la lista de los detalles de deal interbancarios.
     *
     * @return List
     */
    public abstract List getListaDetalles();

    /**
     * Asigna el valor de la lista de detalles de deal interbancarios.
     *
     * @param listaDetalles El valor a asignar.
     */
    public abstract void setListaDetalles(List listaDetalles);

    /**
     * Regresa el valor de registerDateDesde.
     *
     * @return Date.
     */
    public abstract Date getRegisterDateDesde();

    /**
     * Fija el valor de registerDateDesde.
     *
     * @param registerDateDesde El valor a asignar.
     */
    public abstract void setRegisterDateDesde(Date registerDateDesde);

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
     * Obtiene el Broker seleccionado
     *
     * @return Broker
     */
    public abstract Integer getIdPersonaBroker();

    /**
     * Asigna el valor para el Broker.
     *
     * @param idPersonaBroker El valor para el Broker.
     */
    public abstract void setIdPersonaBroker(Integer idPersonaBroker);

    /**
     * Obtiene el valor del Broker Seleccionado.
     *
     * @return HashMap
     */
    public abstract HashMap getBrokerSeleccionado();

    /**
     * Fija el valor de brokerSeleccionado.
     *
     * @param brokerSeleccionado El valor a asignar.
     */
    public abstract void setBrokerSeleccionado(HashMap brokerSeleccionado);

    /**
     * Obtiene la mesa seleccionada para la consulta.
     *
     * @return MesaCambio
     */
    public abstract MesaCambio getMesaSeleccionada();

    /**
     * Asigna el valor para la mesa seleccionada.
     *
     * @param mesaSeleccionada La mesa seleccionada.
     */
    public abstract void setMesaSeleccionada(MesaCambio mesaSeleccionada);
}