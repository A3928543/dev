/*
 * $Id: XlsServiceImpl.java,v 1.3 2008/12/26 23:17:30 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2008 LegoSoft S.C.
 */

package com.ixe.ods.sica.services.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.ixe.ods.sica.services.model.ModeloXls;

import ognl.Ognl;
import ognl.OgnlException;

/**
 * Controlador que utiliza las bibliotecas de Poi y OGNL para generar un archivo excel a partir de
 * una lista de objetos (registros).
 *
 * @author Jean C. Favila
 * @version  $Revision: 1.3 $ $Date: 2008/12/26 23:17:30 $
 */
public class XlsServiceImpl {

    /**
     * Constructor vac&iacute;o.
     */
    private XlsServiceImpl() {
        super();
    }

    /**
     * Inicializa la configuraci&oacute;n del contenido del excel.
     *
     * @param modelos La lista de objetos ModeloXls.
     */
    public XlsServiceImpl(List modelos) {
        super();
        this.modelos = modelos;
    }

    /**
     * Constructor para utilizar un solo modelo.
     *
     * @param modelo Instancia de ModeloXls.
     */
    public XlsServiceImpl(ModeloXls modelo) {
        this();
        modelos = new ArrayList();
        modelos.add(modelo);
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
     * Escribe por medio de POI en una nueva hoja de calculo el nuevo archivo en excel; se
     * pasan como parametros los titulos de las columnas y los renglones correspondientes.
     *
     * @param response La respuesta de la pagina.
     * @param nombreArchivo El nombre del archivo.
     * @param modelo Una instancia de ModeloXls.
     */
    public static void generarEscribir(HttpServletResponse response, String nombreArchivo,
                                       ModeloXls modelo) {
        List modelos = new ArrayList();
        modelos.add(modelo);
        generarEscribir(response, nombreArchivo, modelos);
    }

    /**
     * Escribe por medio de POI en una nueva hoja de calculo el nuevo archivo en excel; se
     * pasan como parametros los titulos de las columnas y los renglones correspondientes.
     *
     * @param response La respuesta de la pagina.
     * @param nombreArchivo El nombre del archivo.
     * @param modelos Una lista de instancias de ModeloXls.
     */
    public static void generarEscribir(HttpServletResponse response, String nombreArchivo,
                                       List modelos) {
        ByteArrayOutputStream xlsReport = new ByteArrayOutputStream();
        XlsServiceImpl generador = new XlsServiceImpl(modelos);
        try {
            generador.escribirXls(xlsReport);
            response.setContentType("application/vnd.ms-excel");
            response.setContentLength(xlsReport.toByteArray().length);
            response.setHeader("Content-disposition", "attachment; filename=\"" +
                    nombreArchivo + ".xls\"");
            byte[] bytes2 = xlsReport.toByteArray();
            ServletOutputStream ouputStream = response.getOutputStream();
            ouputStream.write(bytes2, 0, bytes2.length);
            ouputStream.flush();
            ouputStream.close();
        }
        catch (Exception e) {
            if (generador.logger.isWarnEnabled()) {
                generador.logger.warn("No se pudo crear el excel", e);
            }
        }
        finally {
            try {
                xlsReport.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Escribe en el OutputStream especificado el contenido del archivo excel.
     *
     * @param os El OutputStream donde se escribir&aacute;n los bytes del excel.
     * @throws Exception Si ocurre alg&uacute;n problema.
     */
    public void escribirXls(OutputStream os) throws Exception {
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sh = wb.createSheet("resultados");
        int renglonActual = 0;
        HSSFCellStyle csFecha = wb.createCellStyle();
        csFecha.setDataFormat((short) DATE_DATA_FORMAT);
        HSSFFont boldFont = wb.createFont();
        boldFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        HSSFCellStyle csBold = wb.createCellStyle();
        csBold.setFont(boldFont);
        for (Iterator it = modelos.iterator(); it.hasNext();) {
            ModeloXls modeloXls = (ModeloXls) it.next();

            // titulo:
            HSSFRow renglon = sh.createRow(renglonActual);
            HSSFCell celda = renglon.createCell((short) 0);
            celda.setCellValue(modeloXls.getTitulo());
            celda.setCellStyle(csBold);
            renglonActual++;

            // Subtitulos:
            renglon = sh.createRow(renglonActual);
            for (int j = 0; j < modeloXls.getSubtitulos().size(); j++) {
                celda = renglon.createCell((short) j);
                celda.setCellValue(modeloXls.getSubtitulos().get(j).toString());
                celda.setCellStyle(csBold);
            }
            renglonActual++;
            renglonActual = crearRenglones(sh, renglonActual, csFecha, modeloXls);
            renglonActual++;
        }
        wb.write(os);
    }

    private int crearRenglones(HSSFSheet sh, int renglonActual, HSSFCellStyle csFecha,
                               ModeloXls modeloXls) throws OgnlException {
        HSSFRow renglon;
        HSSFCell celda;
        for (Iterator it2 = modeloXls.getDatos().iterator(); it2.hasNext(); renglonActual++) {
            Object objeto = it2.next();
            Object[] valores = getValores(modeloXls.getPropiedades(), objeto);
            renglon = sh.createRow(renglonActual);
            for (int j = 0; j < valores.length; j++) {
                celda = renglon.createCell((short) j);
                Object valor = valores[j];
                if (valor != null) {
                    if (valor.toString().indexOf("=") == 0) {
                        celda.setCellFormula(valor.toString().substring(1,
                                valor.toString().length()));
                    }
                    else if (valor instanceof Integer || valor instanceof Double ||
                            valor instanceof Long || valor instanceof BigDecimal) {
                        celda.setCellValue(Double.valueOf(valor.toString()).doubleValue());
                    }
                    else if (valor instanceof Date) {
                        celda.setCellValue((Date) valores[j]);
                        celda.setCellStyle(csFecha);
                    }
                    else if (valor instanceof Calendar) {
                        celda.setCellValue((Calendar) valores[j]);
                    }
                    else {
                        celda.setCellValue(valores[j].toString());
                    }
                }
            }
        }
        return renglonActual;
    }

    /**
     * La lista de objetos ModeloXls.
     */
    private List modelos;

    /**
     * El objeto para logging.
     */
    private final transient Log logger = LogFactory.getLog(getClass());

    /**
     * La constante para el formato de celdas de fecha.
     */
    private static final short DATE_DATA_FORMAT = 15;
}
