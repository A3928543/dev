/*
 * $Id: CsvExportPage.java,v 1.10 2008/02/22 18:25:51 ccovian Exp $ 
 * Ixe, Apr 14, 2003
 * Copyright © 2001-2005 Grupo Financiero Ixe
 * 
 */
package com.ixe.ods.sica.pages.contabilidad;

import com.ixe.ods.sica.util.CsvWriter;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageRenderListener;
import org.apache.tapestry.html.BasePage;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * P&acute;gina que permite generar el archivo CSV para el sistema SOLOMON
 * que contiene el resumen de los movimientos de polizas. 
 * 
 * @author Jesus Ramos
 * @version $Revision: 1.10 $ $Date: 2008/02/22 18:25:51 $
 */
public abstract class CsvExportPage extends BasePage implements PageRenderListener {

    /**
     * El constructor por default de la p&aacute;gina.
     */
    public CsvExportPage() {
    }

    /**
     * M&eacute;todo llamado cuando la p&aacute;gina efect&uacute;a el
     * "render" de la misma.
     *
     * @param event el evento de la p&aacute;gina.
     */
    public void pageBeginRender(PageEvent event) {
        IRequestCycle cycle = event.getRequestCycle();
        StringBuffer content_disposition = new StringBuffer();
        content_disposition.append("attachment; filename=BCS");
        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MMddyyyy");
        content_disposition.append(DATE_FORMAT.format(new Date())).append(".txt");
        cycle.getRequestContext().getResponse().setHeader("Content-Type", "text/plain");
        cycle.getRequestContext().getResponse().setHeader("Content-Disposition", content_disposition.toString());
    }

    /**
     * @see org.apache.tapestry.IPage#getResponseWriter(java.io.OutputStream)
     */
    public IMarkupWriter getResponseWriter(OutputStream out) {
        return new CsvWriter(out);
    }

    /**
     * Regresa la variable de instancia <code>list</code>.
     *
     * @return la variable de instancia <code>list</code>
     */
    public List getList() {
        return list;
    }

    /**
     * Establece la variable de instancia <code>list</code>.
     *
     * @param list la variable de instancia <code>list</code>
     */
    public void setList(List list) {
        this.list = list;
    }
    
    /**
     * Regresa la variable de instancia <code>list</code>.
     *
     * @return la variable de instancia <code>list</code>
     */
    public StringBuffer getSB() {
        return sb;
    }

    /**
     * Establece la variable de instancia <code>list</code>.
     *
     * @param sb variable de instancia <code>list</code>
     */
    public void setSB(StringBuffer sb) {
        this.sb = sb;
    }
    

    /**
     * Crea un <code>StringBuffer</code> el cual contiene el reporte CSV por medio de una lista
     * que contiene los reportes.
     *
     * @param list contiene el reporte completo para ser transformado en un <code>String</code> CSV
     * @return StringBuffer contiene el reporte CSV por medio de una lista que contiene los reportes
     */
    public StringBuffer createOutputBuffer(List list) {
    	return getSB();
    }

    /**
     * Regresa el contenido CSV.
     *
     * @return el contenido CSV
     */
    public String getContent() {
        return createOutputBuffer(getList()).toString();
    }

    /**
     * Establece el m&eacute;todo para la variable de instancia <code>keys</code>.
     *
     * @param keys la variable de instancia <code>keys</code>
     */
    public void setKeys(String[] keys) {
        this.keys = keys;
    }

    /**
     * Establece el m&eacute;todo para la variable de instancia <code>titles</code>.
     *
     * @param titles la variable de instancia <code>titles</code>
     */
    public void setTitles(String[] titles) {
        this.titles = titles;
    }

    /**
     * Limpia todas las variables de instancia.
     */
    public void detach() {
        titles = null;
        list = null;
        keys = null;
        super.detach();
    }
    
    /**
     * Lista. 
     */
    private List list = null;
    
    /**
     * Buffer para lectura y escritura.
     */
    private StringBuffer sb = new StringBuffer();
    
    /**
     * Arreglo de llaves.
     */
    private String[] keys = null;
    
    /**
     * Arreglo de titulos.
     */
    private String[] titles = null;
}
