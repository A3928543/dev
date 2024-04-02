/*
 * $Id: ReporteDetalleLinCred.java,v 1.10 2008/02/22 18:25:38 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright © 2005 LegoSoft S.C.
 */
package com.ixe.ods.sica.pages.reportes;

import java.util.Date;
import java.util.List;

import org.apache.tapestry.IRequestCycle;

import net.sf.jasperreports.engine.JRDataSource;
import com.ixe.ods.jasper.ListDataSource;
import com.ixe.ods.sica.pages.SicaPage;
import com.ixe.ods.tapestry.services.jasper.DataSourceProvider;

/**
 * P&aacute;gina que permite al usuario imprimir el reporte de los detalles de una 
 * l&iacute;nea de Cr&eacute;dito
 * 
 * @author Gerardo Corzo Herrera
 */
public abstract class ReporteDetalleLinCred extends SicaPage implements DataSourceProvider{
	
	 /**
     * M&eacute;todo que obtiene el DataSource del reporte del detalle de la
     * L&iacute;nea de Cr&eacute;dito
     * 
     * @see com.ixe.ods.tapestry.services.jasper.DataSourceProvider#getDataSource(String id)
     */
	public JRDataSource getDataSource(String id) {
        return new ListDataSource(getLineasCreditoLog());
    }
	
	/**
	 * Env&iacute;a a la pagina de ReporteLinCredAutMC.
	 * 
	 * @param cycle El ciclo de la p&aacute;gina.
	 */
	public void listaPromotores(IRequestCycle cycle){
		ReporteLinCredAutMC prevPage = (ReporteLinCredAutMC)cycle.getPage("ReporteLinCredAutMC");
		prevPage.importarListaPromotores();
		cycle.activate("ReporteLinCredAutMC");
	}
	
    /**
     * Fija el valor de lineasCreditoLog.
     *
     * @param lineasCreditoLog El valor a asignar.
     */
    public abstract void setLineasCreditoLog(List lineasCreditoLog);
                
    /**
     * Regresa el valor de lineasCreditoLog.
     *
     * @return List.
     */ 
    public abstract List getLineasCreditoLog();
    
    /**
     * Fija el valor de idLineaCredito.
     *
     * @param idLineaCredito El valor a asignar.
     */
    public abstract void setIdLineaCredito(Integer idLineaCredito);

    /**
     * Regresa el valor de idLineaCredito.
     *
     * @return Integer.
     */ 
    public abstract Integer getIdLineaCredito();

    /**
     * Fija el valor de corporativo.
     *
     * @param corporativo El valor a asignar.
     */
    public abstract void setCorporativo(String corporativo);

    /**
     * Regresa el valor de corporativo.
     *
     * @return String.
     */
    public abstract String getCorporativo();
    
    /**
     * Fija el valor de tipoLineaCredito.
     *
     * @param tipoLineaCredito El valor a asignar.
     */
    public abstract void setTipoLineaCredito(String tipoLineaCredito);

    /**
     * Regresa el valor de tipoLineaCredito.
     *
     * @return String.
     */
    public abstract String getTipoLineaCredito();
    
    /**
     * Fija el valor de status.
     *
     * @param status El valor a asignar.
     */
    public abstract void setStatus(String status);

    /**
     * Regresa el valor de status.
     *
     * @return String.
     */
    public abstract String getStatus();
    
    /**
     * Fija el valor de ultimaModificacion.
     *
     * @param ultimaModificacion El valor a asignar.
     */
    public abstract void setUltimaModificacion(Date ultimaModificacion);

    /**
     * Regresa el valor de ultimaModificacion.
     *
     * @return Date.
     */
    public abstract Date getUltimaModificacion();
    
    /**
     * Fija el valor de vencimiento.
     *
     * @param vencimiento El valor a asignar.
     */
    public abstract void setVencimiento(Date vencimiento);

    /**
     * Regresa el valor de vencimiento.
     *
     * @return Date.
     */
    public abstract Date getVencimiento();
    
    /**
     * Fija el valor de importeGral.
     *
     * @param importeGral El valor a asignar.
     */
    public abstract void setImporteGral(double importeGral);

    /**
     * Regresa el valor de importeGral.
     *
     * @return double.
     */
    public abstract double getImporteGral();

    /**
     * Fija el valor de numeroExcepciones.
     *
     * @param numeroExcepciones El valor a asignar.
     */
    public abstract void setNumeroExcepciones(int numeroExcepciones);

    /**
     * Regresa el valor de numeroExcepciones.
     *
     * @return int.
     */
    public abstract int getNumeroExcepciones();

    /**
     * Fija el valor de numeroExcepcionesMes.
     *
     * @param numeroExcepcionesMes El valor a asignar.
     */
    public abstract void setNumeroExcepcionesMes(int numeroExcepcionesMes);

    /**
     * Regresa el valor de numeroExcepcionesMes.
     *
     * @return int.
     */
    public abstract int getNumeroExcepcionesMes();
    
    /**
     * Fija el valor de promedioLinea.
     *
     * @param promedioLinea El valor a asignar.
     */
    public abstract void setPromedioLinea(double promedioLinea);

    /**
     * Regresa el valor de promedioLinea.
     *
     * @return double.
     */
    public abstract double getPromedioLinea();
    
    /**
     * Fija el valor de usoLinea.
     *
     * @param usoLinea El valor a asignar.
     */
    public abstract void setUsoLinea(double usoLinea);

    /**
     * Regresa el valor de usoLinea.
     *
     * @return double.
     */
    public abstract double getUsoLinea();
    
    /**
     * Fija el valor de saldoFinal.
     *
     * @param saldoFinal El valor a asignar.
     */
    public abstract void setSaldoFinal(double saldoFinal);

    /**
     * Regresa el valor de saldoFinal.
     *
     * @return double.
     */
    public abstract double getSaldoFinal();
    
    /**
     * Fija el valor de paginaAnterior.
     *
     * @param paginaAnterior El valor a asignar.
     */
    public abstract void setPaginaAnterior(String paginaAnterior);

    /**
     * Regresa el valor de paginaAnterior.
     *
     * @return String.
     */
    public abstract String getPaginaAnterior();
    
    /**
     * Regresa el valor de fechaSeleccionada.
     *
     * @return String.
     */
    public abstract String getFechaSeleccionada();

    /**
     * Fija el valor de fechaSeleccionada.
     *
     * @param fechaSeleccionada El valor a asignar.
     */
    public abstract void setFechaSeleccionada(String fechaSeleccionada);
    
}
