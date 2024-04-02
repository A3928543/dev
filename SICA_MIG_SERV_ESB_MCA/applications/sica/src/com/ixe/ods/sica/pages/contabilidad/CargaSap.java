/*
 * $Id: CargaSap.java,v 1.3 2010/01/27 22:31:44 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2009 - 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica.pages.contabilidad;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.request.IUploadFile;

import com.ixe.ods.sica.Num;
import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.Visit;
import com.ixe.ods.sica.model.Divisa;
import com.ixe.ods.sica.model.Poliza;
import com.ixe.ods.sica.pages.SicaPage;
import com.ixe.ods.sica.sdo.ContabilidadSicaServiceData;

/**
 * P&aacute;gina que permite al usuario subir un archivo Excel con asientos para exportar al Sistema
 * SAP.
 *
 * @author Jean C. Favila (jfavila)
 * @version $Revision: 1.3 $ $Date: 2010/01/27 22:31:44 $
 */
public abstract class CargaSap extends SicaPage {

    /**
     * Recibe el contenido del archivo Excel, genera los registros para la tabla SC_POLIZA y los
     * escribe.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void submit(IRequestCycle cycle) {
        try {
            ContabilidadSicaServiceData cssd = (ContabilidadSicaServiceData)
                    getApplicationContext().getBean("contabilidadServiceData");
            IUploadFile archivo = getArchivo();
            InputStream is = getArchivo().getStream();

            if (is.available() < 1) {
                throw new SicaException("Por favor proporcione una ruta.");
            }
            HSSFWorkbook wb = new HSSFWorkbook(is);
            HSSFSheet sheet = wb.getSheetAt(0);
            int i = 2;
            List asientos = new ArrayList();
            while (sheet.getRow(i) != null) {
                HSSFRow row = sheet.getRow(i);
                Poliza poliza = new Poliza();

                llenarPoliza(row, poliza, i);
                asientos.add(poliza);
                i++;
            }
            cssd.insertarAsientosManualesSap(getTicket(), asientos,
                    getRequestCycle().getRequestContext().getRequest().getRemoteAddr(),
                    ((Visit) getVisit()).getUser());
            getDelegate().record("El archivo fue procesado exitosamente.", null);
            setNivel(1);
        }
        catch (SicaException e) {
            debug(e.getMessage(), e);
            getDelegate().record(e.getMessage(), null);
        }
        catch (IOException e) {
            warn(e.getMessage(), e);
            getDelegate().record("Error al leer el archivo.", null);
        }
    }

    /**
     * Llena en un objeto Poliza los valores del rengl&oacute;n del Excel.
     *
     * @param row El rengl&oacute;n a revisar.
     * @param poliza La p&oacute;liza a llenar.
     * @param renglon El n&uacute;mero de rengl&oacute;n del excel.
     */
    private void llenarPoliza(HSSFRow row, Poliza poliza, int renglon) {
        poliza.setCuentaContable((String) getValor(row, 1, 1, renglon));
        poliza.setFechaValor((Date) getValor(row, 0, 0, renglon));
        poliza.setEntidad((String) getValor(row, 2, 1, renglon));
        poliza.setCargoAbono((String) getValor(row, Num.I_3, 1, renglon));
        Divisa divisa = new Divisa();
        divisa.setIdDivisa((String) getValor(row, Num.I_4, 1, renglon));
        poliza.setDivisa(divisa);
        poliza.setImporte(((Double) getValor(row, Num.I_5, 2, renglon)).doubleValue());
        poliza.setIdDeal(((Double) getValor(row, Num.I_6, 2, renglon)).intValue());
        poliza.setReferencia((String) getValor(row, Num.I_7, 1, renglon));
        poliza.setFechaCreacion((Date) getValor(row, Num.I_8, 0, renglon));
        poliza.setTipoDeal((String) getValor(row, Num.I_9, 1, renglon));
        poliza.setSucursalOrigen((String) getValor(row, Num.I_10, 1, renglon));
        poliza.setFolioDetalle(((Double) getValor(row, Num.I_11, 2, renglon)).intValue());
        poliza.setTipoCambio(((Double) getValor(row, Num.I_12, 2, renglon)).doubleValue());
        poliza.setNoContratoSica((String) getValor(row, Num.I_13, 1, renglon));
        poliza.setIdDealPosicion(((Double) getValor(row, Num.I_14, 2, renglon)).intValue());
        poliza.setStatusProceso("0");
    }

    /**
     * Regresa el valor de la celda en la columna especificada de un rengl&oacute;n.
     *
     * @param renglon El rengl&oacute;n a revisar.
     * @param columna El n&uacute;mero de la columna a revisar.
     * @param tipo El tipo de dato a leer 0.- Fecha, 1.- Texto, 2.- N&uacute;mero.
     * @param numRenglon El n&uacute;mero de rengl&oacute;n del excel.
     * @return Object El valor de la celda.
     */
    private Object getValor(HSSFRow renglon, int columna, int tipo, int numRenglon) {
        HSSFCell cell = renglon.getCell((short) columna);
        Object valor = null;
        if (tipo == 0) {
            try {
                valor = cell.getDateCellValue();
            }
            catch (Exception e) {
                throw new SicaException(getMensajeColumnaRenglon(columna, numRenglon,
                        " se esperaba una Fecha."));

            }
        }
        else if (tipo == 1) {
            if (HSSFCell.CELL_TYPE_STRING != cell.getCellType()) {
                throw new SicaException(getMensajeColumnaRenglon(columna, numRenglon,
                        " se esperaba una Cadena de Caracteres."));
            }
            valor = cell.getStringCellValue();
        }
        else if (tipo == 2) {
            try {
                valor = new Double(cell.getNumericCellValue());
            }
            catch (Exception e) {
                throw new SicaException(getMensajeColumnaRenglon(columna, numRenglon,
                        " se esperaba un valor num\u00e9rico."));
            }
        }
        if (valor == null) {
            throw new SicaException(getMensajeColumnaRenglon(columna, numRenglon,
                    " se esperaba un valor."));
        }
        return valor;
    }

    /**
     * Regresa un mensaje de error referente a una columna y rengl&oacute;n del excel.
     *
     * @param columna El n&uacute;mero de la columna.
     * @param renglon El n&uacute;mero del rengl&oacute;n.
     * @param postFijo El mensaje espec&iacute;fico.
     * @return String.
     */
    private String getMensajeColumnaRenglon(int columna, int renglon, String postFijo) {
        return "En la columna " + (columna + 1) + ", rengl\u00f3n " + (renglon + 1) + postFijo;
    }

    /**
     * Escribe el contenido del archivo en el Output Stream del Response.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void obtenerArchivo(IRequestCycle cycle) {
        HttpServletResponse response = cycle.getRequestContext().getResponse();
        String nombreArchivo = "archivos/sica/" + DATE_FORMAT.format(getFechaArchivo()) +
                ".xls";
        FileInputStream fis = null;
        ServletOutputStream os = null;

        try {
            File f = new File(nombreArchivo);

            if (!f.exists()) {
                throw new SicaException("No se encontr\u00f3 un archivo de descuadres para la " +
                        "fecha seleccionada.");
            }            
            fis = new FileInputStream(nombreArchivo);
            byte[] bytes = new byte[fis.available()];

            response.setContentType("application/vnd.ms-excel");
            response.setContentLength(fis.read(bytes));
            response.setHeader("Content-disposition", "attachment; filename=\"" +
                    f.getName() + "\"");
            os = response.getOutputStream();
            os.write(bytes, 0, bytes.length);
            os.flush();
        }
        catch (SicaException e) {
            debug(e.getMessage(), e);
            getDelegate().record(e.getMessage(), null);
        }
        catch (FileNotFoundException e) {
            debug(e.getMessage(), e);
            getDelegate().record("No se encontr\u00f3 el archivo " + nombreArchivo + ".",
                    null);
        }
        catch (IOException e) {
            debug(e.getMessage(), e);
            getDelegate().record("Ocurri\u00f3 un error al leer el archivo " + nombreArchivo + ".",
                    null);
        }
        finally {
            if (fis != null) {
                try {
                    fis.close();
                }
                catch (IOException e) {
                    debug(e.getMessage(), e);
                }
            }
            if (os != null) {
                try {
                    os.close();
                }
                catch (IOException e) {
                    debug(e.getMessage(), e);
                }
            }
        }
    }

    /**
     * Regresa el valor de archivo.
     *
     * @return IUploadFile.
     */
    public abstract IUploadFile getArchivo();

    /**
     * Establece el valor de nivel.
     *
     * @param nivel El valor a asignar.
     */
    public abstract void setNivel(int nivel);

    /**
     * Regresa el valor de fechaArchivo.
     *
     * @return Date.
     */
    public abstract Date getFechaArchivo();

    /**
     * Formato de Fecha para los nombres de los archivos excel.
     */
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");  
}
