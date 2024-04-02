/*
 * $Id: CancelacionMailSenderImpl.java,v 1.1 2008/07/08 04:16:49 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2008 LegoSoft S.C.
 */

package com.ixe.ods.sica.services.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

import com.ixe.ods.bup.model.MedioContacto;
import com.ixe.ods.bup.model.Persona;
import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.model.Deal;
import com.ixe.ods.sica.services.CancelacionMailSender;

/**
 * Clase que env&iacute;a notificaciones v&iacute;a correo electr&oacute;nico sobre deals cancelados
 * que no fueron procesados antes del cierre vespertino.
 *
 * @author Jean C. Favila
 * @version $Revision: 1.1 $ $Date: 2008/07/08 04:16:49 $
 */
public class CancelacionMailSenderImpl implements CancelacionMailSender {

    /**
     * Constructor por default.
     */
    public CancelacionMailSenderImpl() {
        super();
    }

    /**
     * @see com.ixe.ods.sica.services.CancelacionMailSender#enviarMailPromotor(
     *          com.ixe.ods.sica.model.Deal).
     * @param deal El deal que se cancel&oacute;.
     * @throws SicaException Si no se puede enviar el correo al promotor.
     */
    public void enviarMailPromotor(Deal deal) throws SicaException {
        Persona promotor = deal.getUsuario().getPersona();
        try {
            SimpleMailMessage msg = new SimpleMailMessage(message);
            if (!promotor.getMediosContacto().isEmpty()) {
                MedioContacto mc = (MedioContacto) promotor.getMediosContacto().get(0);
                msg.setTo(mc.getValor());
            }
            else {
                throw new SicaException("No se pudo mandar el correo de cancelaci\u00f3n al " +
                        "promotor " + promotor.getNombreCompleto() + " pues no tiene configurada " +
                        "una direcci&oacute;n de correo electr\u00f3nico.");
            }
            msg.setText("El deal " + deal.getIdDeal() + " fue cancelado debido a que no fue " +
                    "procesado antes del cierre de la Mesa de Cambios.\n\nEste correo ha sido " +
                    "generado autom\u00e1ticamente por el sistema SICA. No responda ni " +
                    "env\u00ede correo a esta direcci\u00f3n por favor.\n\n");
            mailSender.send(msg);
        }
        catch (MailException e) {
            if (logger.isWarnEnabled()) {
                logger.warn(e);
            }
            throw new SicaException("No se pudo mandar el correo de cancelaci\u00f3n al promotor " +
                    promotor.getNombreCompleto() + ".");
        }
    }

    /**
     * Regresa el valor de la variable mailSender.
     *
     * @return MailSender.
     */
    public MailSender getMailSender() {
        return mailSender;
    }

    /**
     * Establece el valor de la variable mailSender.
     *
     * @param mailSender El valor a asignar.
     */
    public void setMailSender(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * Regresa el valor de la variable message.
     *
     * @return SimpleMailMessage.
     */
    public SimpleMailMessage getMessage() {
        return message;
    }

    /**
     * Establece el valor de la variable message.
     *
     * @param message El valor a asignar.
     */
    public void setMessage(SimpleMailMessage message) {
        this.message = message;
    }

    /**
     * El objeto de spring que permite el env&iacute;o de mensajes de correo electr&oacute;nico.
     */
    private MailSender mailSender;

    /**
     * El template para el mensaje de mail.
     */
    private SimpleMailMessage message;

    /**
     * El objeto para logging.
     */
    private final transient Log logger = LogFactory.getLog(getClass());
}
