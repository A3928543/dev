/*
 * $Id: PdfService.java,v 1.3 2008/12/26 23:17:33 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2008 LegoSoft S.C.
 */

package com.ixe.ods.sica.services;

import java.util.List;
import java.io.OutputStream;
import java.net.URL;

import com.ixe.ods.sica.services.model.ModeloPdf;

/**
 * Interfaz para la generaci&oacute;n de documentos en formato PDF.
 *
 * @author Jean C. Favila
 * @version  $Revision: 1.3 $ $Date: 2008/12/26 23:17:33 $
 */
public interface PdfService {

   /**
     * Escribe en el OutputStream especificado el contenido del archivo excel.
     *
     * @param logo El URL para el logo en el reporte (opcional).
     * @param titulo El encabezado del archivo.
     * @param modelo Un solo modelo PDF.
     * @param os El OutputStream donde se escribir&aacute;n los bytes del excel.
     * @throws Exception Si ocurre alg&uacute;n problema.
     */
    public void escribirPdf(URL logo, String titulo, ModeloPdf modelo, OutputStream os)
           throws Exception;

    /**
     * Escribe en el OutputStream especificado el contenido del archivo excel.
     *
     * @param logo El URL para el logo en el reporte (opcional).
     * @param titulo El encabezado del archivo Pdf.
     * @param modelos Una lista de objetos ModeloPdf.
     * @param os El OutputStream donde se escribir&aacute;n los bytes del excel.
     * @throws Exception Si ocurre alg&uacute;n problema.
     */
    public void escribirPdf(URL logo, String titulo, List modelos, OutputStream os)
            throws Exception;
}
