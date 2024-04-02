package com.banorte.reporte.mensual.rentabilidad.services.mail;

public interface GeneralMailSender {
	

    /**
     * Env&iacute;a un mail a trav&eacute;s del mail sender configurado en el application context,
     * incluye un attachment.
     *
     * @param titulo El t&iacute;tulo para el correo.
     * @param mensaje El cuerpo del correo.
     * @param fileName El nombre del archivo del attachment.
     * @param contentType El MIME Type del attachment.
     * @param bytes El arreglo de bytes del attachment.
     */
    void enviarMail( String titulo,
                           String mensaje, String fileName, final String contentType,
                           final byte[] bytes);

}
