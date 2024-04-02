/*
 * $Id: CapturaRapidaMailSenderImpl.java,v 1.4 2008/02/22 18:25:26 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2007 LegoSoft S.C.
 */

package com.ixe.ods.sica.services.impl;

import com.ixe.ods.sica.model.Canal;
import com.ixe.ods.sica.model.Deal;
import com.ixe.ods.sica.model.DealDetalle;
import com.ixe.ods.sica.services.CapturaRapidaMailSender;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

/**
 * Clase que env&iacute;a notificaciones v&iacute;a correo electr&oacute;nico sobre deals de captura
 * r&aacute;pida que no tienen contrato sica asignado. Cada hora se informar&aacute; al jefe de
 * canal, y a las 13:45, se informar&aacute; a los promotores que deben asignar el contrato o sus
 * deals ser&aacute;n cancelados por la mesa de cambios.
 *
 * @author Jean C. Favila
 * @version $Revision: 1.4 $ $Date: 2008/02/22 18:25:26 $
 */
public class CapturaRapidaMailSenderImpl implements CapturaRapidaMailSender {

    /**
     * Env&iacute;a un correo electr&oacute;nico al jefe del canal especificado, listando los deals
     * de captura r&aacute;pida que no tienen contrato sica asignado.
     *
     * @param canal El canal al que pertenecen los deals, se informar&aacute; al Jefe de ese canal.
     * @param deals La lista de deals de captura r&aacute;pida.
     */
    public void enviarMailCanales(Canal canal, List deals) {
        SimpleMailMessage msg = new SimpleMailMessage(_message);
        StringBuffer sb = new StringBuffer("A continuaci\u00f3n se listan los deals de captura ").
            append("r\u00e1pida que no tienen un Contrato SICA asignado:\n\n");
        for (Iterator it = deals.iterator(); it.hasNext();) {
            Deal deal = (Deal) it.next();
            sb.append(getCadenaDeal(deal));
            sb.append("\n-------------------------------------------------\n\n");
        }
        sb.append("Este correo ha sido generado autom\u00e1ticamente por el sistema SICA. No ").
                append("responda ni env\u00ede correo a esta direcci\u00f3n por favor.\n\n");
        sb.append("Fecha y hora: ").append(DATE_FORMAT.format(new Date()));
        msg.setText(sb.toString());
        String[] emails = canal.getEmailJefe().split("\\,");
        msg.setTo(emails);
        mailSender.send(msg);
    }

    /**
     * Env&iacute;a un correo electr&oacute;nico al promotor con la direcci&oacute;n especificada,
     * listando sus deals de captura r&aacute;pida que no tienen contrato sica asignado.
     *
     * @param email El email del promotor.
     * @param deals La lista de deals de captura r&aacute;pida.
     */
    public void enviarMailPromotor(String email, List deals) {
        SimpleMailMessage msg = new SimpleMailMessage(_message);
        StringBuffer sb = new StringBuffer("A continuaci\u00f3n se listan los deals de captura ").
            append("r\u00e1pida que no tienen un Contrato SICA asignado, que fueron capturados por "
                    + "usted:\n\n");
        for (Iterator it = deals.iterator(); it.hasNext();) {
            Deal deal = (Deal) it.next();
            sb.append(getCadenaDeal(deal));
            sb.append("\n-------------------------------------------------\n\n");
        }
        sb.append("Este correo ha sido generado autom\u00e1ticamente por el sistema SICA. No ").
                append("responda ni env\u00ede correo a esta direcci\u00f3n por favor.\n\n");
        sb.append("Fecha y hora: ").append(DATE_FORMAT.format(new Date()));
        msg.setText(sb.toString());
        msg.setTo(email);
        mailSender.send(msg);
    }

    /**
     * Regresa la cadena con la informaci&oacute;n completa del deal.
     *
     * @param deal El deal a procesar.
     * @return String.
     */
    private String getCadenaDeal(Deal deal) {
        StringBuffer sb = new StringBuffer("No. Deal:          ").append(deal.getIdDeal());
        sb.append("\nFecha Valor:       ").append(deal.getTipoValor());
        sb.append("\nCaptur\u00f3:           ").append(deal.getUsuario().getPersona().
                getNombreCompleto());
        sb.append("\nFecha Captura:     ").append(DATE_FORMAT.format(deal.getFechaCaptura()));
        sb.append("\nFecha Liquidaci\u00f3n: ").append(DATE_FORMAT.format(deal.
                getFechaLiquidacion()));
        sb.append("\nOperaci\u00f3n:         ").append(deal.isCompra() ? "COMPRA" : "VENTA");
        sb.append("\nDetalles:\n");
        for (Iterator it = deal.getDetalles().iterator(); it.hasNext();) {
            DealDetalle det = (DealDetalle) it.next();
            if (!DealDetalle.STATUS_DET_CANCELADO.equals(det.getStatusDetalleDeal())) {
                sb.append("\t").append(det.getFolioDetalle()).append("\t").
                        append(det.isRecibimos() ? "REC\t": "ENT\t").
                        append(det.getDivisa().getIdDivisa()).append("\t").
                        append(MONEY_FORMAT.format(det.getMonto())).
                        append("\t").append(TC_FORMAT.format(det.getTipoCambio())).append("\t").
                        append(MONEY_FORMAT.format(det.getImporte())).append("\n");
            }
        }
        return sb.toString();
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
        return _message;
    }

    /**
     * Establece el valor de la variable message.
     *
     * @param message El valor a asignar.
     */
    public void setMessage(SimpleMailMessage message) {
        _message = message;
    }

    /**
     * El objeto de spring que permite el env&iacute;o de mensajes de correo electr&oacute;nico.
     */
    private MailSender mailSender;

    /**
     * El template para el mensaje de mail.
     */
    private SimpleMailMessage _message;

    /**
     * El objeto para logging.
     */
    protected final transient Log _logger = LogFactory.getLog(getClass());

    /**
     * El formateador para las cantidades monetarias.
     */
    private static final DecimalFormat MONEY_FORMAT = new DecimalFormat("#,##0.00");

    /**
     * El formateador para las fechas.
     */
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    /**
     * El formateador para los tipos de cambio.
     */
    private static final DecimalFormat TC_FORMAT = new DecimalFormat("0.000000");
}
