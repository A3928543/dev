/*
 * $Id: GeneralMailSender.java,v 1.4.84.1 2017/10/20 15:57:30 mejiar Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2008 LegoSoft S.C.
 */

package com.ixe.ods.sica.services;

import java.util.Map;

/**
 * Interfaz para el servicio gen&eacute;rico de env&iacute;o de correos.
 *
 * @author Jean C. Favila
 * @version $Revision: 1.4.84.1 $ $Date: 2017/10/20 15:57:30 $
 */
public interface GeneralMailSender {

    /**
     * Env&iacute;a un mail a trav&eacute;s del mail sender configurado en el application context.
     *
     * @param de El remitente.
     * @param para El arreglo de destinatarios.
     * @param copia El arreglo de copias (opcional).
     * @param titulo El t&iacute;tulo para el correo.
     * @param mensaje El cuerpo del correo.
     */
    void enviarMail(String de, String[] para, String[] copia, String titulo, String mensaje);

    /**
     * Env&iacute;a un mail a trav&eacute;s del mail sender configurado en el application context,
     * incluye un attachment.
     *
     * @param de El remitente.
     * @param para El arreglo de destinatarios.
     * @param copia El arreglo de copias (opcional).
     * @param titulo El t&iacute;tulo para el correo.
     * @param mensaje El cuerpo del correo.
     * @param fileName El nombre del archivo del attachment.
     * @param contentType El MIME Type del attachment.
     * @param bytes El arreglo de bytes del attachment.
     */
    void enviarMail(String de, String[] para, String[] copia, String titulo,
                           String mensaje, String fileName, final String contentType,
                           final byte[] bytes);
    /**
     * Enviar mail.
     *
     * @param param the param
     */
    void enviarMail(Map param);
}
