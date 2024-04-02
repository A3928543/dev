/*
 * $Id: MailKondorServiceImpl.java,v 1.2 2010/04/13 17:53:48 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2009 - 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica.services.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ixe.ods.sica.model.ParametroSica;
import com.ixe.ods.sica.sdo.SicaServiceData;
import com.ixe.ods.sica.services.GeneralMailSender;
import com.ixe.ods.sica.services.MailKondorService;

/**
 * Implementaci&oacute;n de la interfaz MailKondorService que env&iacute;a el correo a
 * destinatarios de Folders de Kondor.
 *
 * @author Jean C. Favila
 * @version $Revision: 1.2 $ $Date: 2010/04/13 17:53:48 $
 * @see MailKondorService
 */
public class MailKondorServiceImpl implements MailKondorService {

    /**
     * Constructor vac&iacute;o.
     */
    public MailKondorServiceImpl() {
        super();
    }

    /**
     * @see MailKondorService#enviarCorreo(String, int, String, int).
     * @param idConf El n&uacute;mero de confirmaci&oacute;n (de SC_BITACORA_ENVIADAS).
     * @param folio  El n&uacute;mero de operaci&oacute;n.
     * @param folderKondor El nombre del folder de Kondor.
     * @param tipoCorreo TIPO_ALTA_OPERACION | TIPO_CANCELA_OPERACION.
     */
    public void enviarCorreo(String idConf, int folio, String folderKondor, int tipoCorreo) {
        String op = TIPO_ALTA_OPERACION == tipoCorreo ? "registr\u00f3 " : "cancel\u00f3 ";
        String titulo = TIPO_ALTA_OPERACION == tipoCorreo ? "Registro " : "Cancelaci\u00f3n";
        StringBuffer sb = new StringBuffer("Se ").append(op).
                append(" en el SICA la operaci\u00f3n ").append(idConf).
                append(", folio: ").append(folio).append(".\n\nEste correo ha sido generado ").
                append("autom\u00e1ticamente por el sistema SICA. No responda ni env\u00ede ").
                append("correo a esta direcci\u00f3n por favor.");
        ParametroSica p = getSicaServiceData().findParametro("EMAILS_FOLDERK_" +
                folderKondor.trim());
        try {
            getGeneralMailSender().enviarMail("ixecambios@ixe.com.mx", p.getValor().split("\\,"),
                    null, titulo + " de Operaci\u00f3n en SICA", sb.toString());
        }
        catch (Exception e) {
            if (logger.isWarnEnabled()) {
               logger.warn("Error al mandar el correo del folio " + folio, e);
            }
        }
    }

    /**
     * Regresa el valor de generalMailSender.
     *
     * @return GeneralMailSender.
     */
    public GeneralMailSender getGeneralMailSender() {
        return generalMailSender;
    }

    /**
     * Establece el valor de generalMailSender.
     *
     * @param generalMailSender El valor a asignar.
     */
    public void setGeneralMailSender(GeneralMailSender generalMailSender) {
        this.generalMailSender = generalMailSender;
    }

    /**
     * Regresa el valor de sicaServiceData.
     *
     * @return SicaServiceData.
     */
    public SicaServiceData getSicaServiceData() {
        return sicaServiceData;
    }

    /**
     * Establece el valor de sicaServiceData.
     *
     * @param sicaServiceData El valor a asignar.
     */
    public void setSicaServiceData(SicaServiceData sicaServiceData) {
        this.sicaServiceData = sicaServiceData;
    }

    /**
     * Referencia al bean generalMailSender.
     */
    private GeneralMailSender generalMailSender;
    
    /**
     * Referencia al bean sicaServiceData.
     */
    private SicaServiceData sicaServiceData;


    /**
     * El objeto para logging.
     */
    private final transient Log logger = LogFactory.getLog(getClass());
}
