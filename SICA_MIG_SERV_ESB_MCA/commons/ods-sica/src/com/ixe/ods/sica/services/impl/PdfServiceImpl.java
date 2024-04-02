/*
 * $Id: PdfServiceImpl.java,v 1.3 2008/12/26 23:17:29 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2008 LegoSoft S.C.
 */

package com.ixe.ods.sica.services.impl;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ixe.ods.sica.services.PdfService;
import com.ixe.ods.sica.services.model.ModeloPdf;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Jpeg;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import ognl.Ognl;
import ognl.OgnlException;

/**
 * Generador de Reportes simples en formato PDF.
 *
 * @author Jean C. Favila
 * @version  $Revision: 1.3 $ $Date: 2008/12/26 23:17:29 $
 */
public class PdfServiceImpl implements PdfService {

    /**
     * Constructor por default.
     */
    public PdfServiceImpl() {
        super();
    }

    /**
     * Escribe por medio de POI en una nueva hoja de calculo el nuevo archivo en excel; se
     * pasan como parametros los titulos de las columnas y los renglones correspondientes.
     *
     * @param response      La respuesta de la pagina.
     * @param nombreArchivo El nombre del archivo.
     * @param logo          El URL para el logo en el reporte (opcional).
     * @param titulo        El encabezado para el pdf.
     * @param modelo        Una instancia de ModeloXls.
     */
    public void generarEscribir(HttpServletResponse response, String nombreArchivo,
                                       URL logo, String titulo, ModeloPdf modelo) {
        List modelos = new ArrayList();
        modelos.add(modelo);
        generarEscribir(response, nombreArchivo, logo, titulo, modelos);
    }

    /**
     * Escribe por medio de iText en un nuevo archivo en pdf; se pasan como parametros los titulos
     * de las columnas y los renglones correspondientes.
     *
     * @param response      La respuesta de la pagina.
     * @param nombreArchivo El nombre del archivo.
     * @param logo          El URL para el logo en el reporte (opcional).
     * @param titulo        El t&iacute;tulo para el documento pdf.
     * @param modelos       Una lista de instancias de ModeloXls.
     */
    public void generarEscribir(HttpServletResponse response, String nombreArchivo, URL logo,
                                       String titulo, List modelos) {
        ByteArrayOutputStream pdfReport = new ByteArrayOutputStream();
        try {
            escribirPdf(logo, titulo, modelos, pdfReport);
            response.setContentType("application/pdf");
            response.setContentLength(pdfReport.toByteArray().length);
            response.setHeader("Content-disposition", "attachment; filename=\"" +
                    nombreArchivo + ".pdf\"");
            byte[] bytes2 = pdfReport.toByteArray();
            ServletOutputStream ouputStream = response.getOutputStream();
            ouputStream.write(bytes2, 0, bytes2.length);
            ouputStream.flush();
            ouputStream.close();
        }
        catch (Exception e) {
            if (logger.isWarnEnabled()) {
                logger.warn("No se pudo crear el pdf", e);
            }
        }
        finally {
            try {
                pdfReport.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Escribe en el OutputStream especificado el contenido del archivo excel.
     *
     * @param titulo El encabezado del archivo.
     * @param modelo Un solo modelo PDF.
     * @param os El OutputStream donde se escribir&aacute;n los bytes del excel.
     * @throws Exception Si ocurre alg&uacute;n problema.
     */
    public void escribirPdf(URL logo, String titulo, ModeloPdf modelo, OutputStream os)
            throws Exception {
        List modelos = new ArrayList();
        modelos.add(modelo);
        escribirPdf(logo, titulo, modelos, os);
    }

    /**
     * Escribe en el OutputStream especificado el contenido del archivo excel.
     *
     * @param os El OutputStream donde se escribir&aacute;n los bytes del excel.
     * @throws Exception Si ocurre alg&uacute;n problema.
     */
    public void escribirPdf(URL logo, String titulo, List modelos, OutputStream os)
            throws Exception {
        Document documento = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(documento, os);
        Font boldFont = FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLDITALIC, Color.BLACK);
        Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 6, Font.NORMAL, Color.BLACK);
        if (logo == null) {
            HeaderFooter hf = new HeaderFooter(new Phrase(titulo, boldFont), false);
            documento.setHeader(hf);
        }
        documento.open();
        if (logo != null) {
            PdfPTable t = new PdfPTable(2);
            Jpeg logoImg = new Jpeg(logo);
            t.setWidths(new float[] {logoImg.width(), 500.0f});
            t.setWidthPercentage(100.0f);
            PdfPCell celda = new PdfPCell(logoImg);
            celda.setBorder(0);
            t.addCell(celda);
            celda = new PdfPCell(new Phrase(titulo));
            celda.setBorder(0);
            t.addCell(celda);
            documento.add(t);
        }
        for (Iterator it = modelos.iterator(); it.hasNext();) {
            ModeloPdf modeloPdf = (ModeloPdf) it.next();
            Paragraph encabezado = new Paragraph(modeloPdf.getTitulo(), boldFont);
            documento.add(encabezado);
            PdfPTable t = new PdfPTable(modeloPdf.getSubtitulos().size());
            t.setWidthPercentage(100.0f);
            // Subtitulos:
            for (int j = 0; j < modeloPdf.getSubtitulos().size(); j++) {
                PdfPCell celda = new PdfPCell(new Phrase((String) modeloPdf.getSubtitulos().get(j),
                        boldFont));
                t.addCell(celda);
            }
            crearRenglones(t, modeloPdf, normalFont);
            documento.add(t);
        }
        documento.close();
    }

    /**
     * Genera la tabla con el modelo especificado.
     *
     * @param t La tabla a llenar.
     * @param modeloPdf El modelo de donde se tomar&aacute;n los datos.
     * @param normalFont El font normal.
     * @throws OgnlException Si existe alg&uacute;n error en una expresi&oacute;n ognl.
     */
    private void crearRenglones(PdfPTable t, ModeloPdf modeloPdf, Font normalFont)
            throws OgnlException {
        for (Iterator it2 = modeloPdf.getDatos().iterator(); it2.hasNext(); ) {
            Object objeto = it2.next();
            Object[] valores = getValores(modeloPdf.getPropiedades(), objeto);
            String[] formatos = modeloPdf.getFormatos();
            for (int j = 0; j < valores.length; j++) {
                PdfPCell celda;
                char alineacion = formatos[j].charAt(0);
                char tipoDato = formatos[j].charAt(1);
                String valorStr = "";
                Object valor = valores[j];
                if (valor != null) {
                    if (valor instanceof Integer || valor instanceof Double ||
                            valor instanceof Long || valor instanceof BigDecimal) {
                        int decimales = Integer.valueOf("" + formatos[j].charAt(2)).intValue();
                        valorStr = getDecimalFormat(decimales).
                                format(Double.valueOf(valor.toString()).doubleValue());
                    }
                    else if (valor instanceof Date || valor instanceof Calendar) {
                        valorStr = tipoDato == 't' ? TIME_FORMAT.format(valores[j]) :
                                DATE_FORMAT.format(valores[j]);
                    }
                    else {
                        valorStr = valores[j].toString();
                    }
                }
                celda = new PdfPCell(new Phrase(valorStr, normalFont));
                celda.setHorizontalAlignment(alineacion == 'l' ? PdfPCell.ALIGN_LEFT :
                        alineacion == 'r' ? PdfPCell.ALIGN_RIGHT : PdfPCell.ALIGN_CENTER);
                t.addCell(celda);
            }
        }
    }

    /**
     * Formato para decimales.
     *
     * @param decimales El formato para decimales.
     * @return DecimalFormat.
     */
    private DecimalFormat getDecimalFormat(int decimales) {
        StringBuffer sb = new StringBuffer("#,##0");
        if (decimales > 0) {
            sb = sb.append(".");
        }
        for (int i = 0; i < decimales; i++) {
            sb = sb.append("0");
        }
        return new DecimalFormat(sb.toString());
    }

    /**
     * Regresa un arreglo de valores para un registro, los valores se obtienen evaluando las
     * expresiones ognl que corresponden a la variable nombresPropiedades.
     *
     * @param propiedades La lista de expresiones OGNL a evaluar.
     * @param renglon El registro a pintar en un rengl&oacute;n de la hoja de excel.
     * @return Object[].
     * @throws OgnlException Si no se puede parsear la excepci&oacute;n.
     */
    private Object[] getValores(List propiedades, Object renglon) throws OgnlException {
        Object[] valores = new Object[propiedades.size()];
        for (int i = 0; i < propiedades.size(); i++) {
            valores[i] = Ognl.getValue(propiedades.get(i).toString(), renglon);
        }
        return valores;
    }

    /**
     * El objeto para logging.
     */
    private final transient Log logger = LogFactory.getLog(getClass());

    /**
     * La constante para el formato de celdas de fecha.
     */
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");

    /**
     * La constante para el formato de celdas de tiempo.
     */
    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
}
