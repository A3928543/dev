/*
 * $Id: ReversoMailSenderImpl.java,v 1.15.32.1 2010/09/14 23:57:23 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2007 LegoSoft S.C.
 */

package com.ixe.ods.sica.services.impl;

import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.model.Deal;
import com.ixe.ods.sica.model.DealDetalle;
import com.ixe.ods.sica.model.Reverso;
import com.ixe.ods.sica.services.ReversoMailSender;
import com.ixe.ods.bup.model.MedioContacto;
import com.ixe.ods.bup.model.Persona;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

/**
 * Implementaci&oacute;n de la interfaz ReversoMailSender.
 *
 * @see ReversoMailSender
 * @author Jean C. Favila
 * @version $Revision: 1.15.32.1 $ $Date: 2010/09/14 23:57:23 $
 */
public class ReversoMailSenderImpl implements ReversoMailSender {

    /**
     * @see ReversoMailSender#enviarMailReversoPendienteDeAutorizar(Reverso, String[]).
     */
    public void enviarMailReversoPendienteDeAutorizar(Reverso reverso, String[] cc) {
        try {
            SimpleMailMessage msg = new SimpleMailMessage(message);
            Persona promotor = reverso.getUsuario().getPersona();
            if (!promotor.getMediosContacto().isEmpty()) {
                MedioContacto mc = (MedioContacto) promotor.getMediosContacto().get(0);
                msg.setTo(mc.getValor());
            }
            StringBuffer sb = new StringBuffer();
            sb.append("Se registr\u00f3 exitosamente el reverso n\u00fam. ").
                    append(reverso.getIdReverso()).append(" correspondiente al deal ").
                    append(reverso.getDealOriginal().getIdDeal()).append(". Se encuentra ").
                    append("actualmente en proceso de autorizaci\u00f3n.");
            msg.setText(sb.toString());
            if (cc != null) {
                msg.setCc(cc);
            }
            mailSender.send(msg);
        }
        catch (MailException e) {
            if (logger.isWarnEnabled()) {
                logger.warn(e);
            }
            throw new SicaException("Error al enviar el correo para el reverso: "
                    + reverso.getIdReverso());
        }
    }

    /**
     * @see ReversoMailSender#enviarMailReverso(com.ixe.ods.sica.model.Reverso, String[]).
     */
    public void enviarMailReverso(Reverso reverso, String[] cc) throws SicaException {
        try {
            SimpleMailMessage msg = new SimpleMailMessage(message);
            Persona promotor = reverso.getDealOriginal().getUsuario().getPersona();
            if (!promotor.getMediosContacto().isEmpty()) {
                MedioContacto mc = (MedioContacto) promotor.getMediosContacto().get(0);
                msg.setTo(mc.getValor());
            }
            StringBuffer sb = new StringBuffer();
            if (Reverso.STATUS_CANCELADO.equals(reverso.getStatusReverso())) {
                sb.append("Fue denegada la autorizaci\u00f3n del reverso: ").
                        append(reverso.getIdReverso()).append(", correspondiente al deal: ").
                        append(reverso.getDealOriginal().getIdDeal()).append(".");
            }
            else {
                if (cc != null) {
                    msg.setCc(cc);
                }
                sb = new StringBuffer("Reverso:\n=======\nN\u00famero:        ").
                        append(reverso.getIdReverso()).
                        append("\nObservaciones: ").append(reverso.getObservaciones()).
                        append("\nCapturado por: ").
                        append(reverso.getUsuario().getPersona().getNombreCompleto()).
                        append(" el: ").append(DATE_FORMAT.format(reverso.getFecha()));
                if (reverso.getUsuarioAutorizacion() != null) {
                    sb.append("\nAutorizado por: ").append(reverso.getUsuarioAutorizacion().
                            getPersona().getNombreCompleto()).
                            append(" el: ").
                            append(DATE_FORMAT.format(reverso.getFechaAutorizacion()));
                }
                sb.append("\nTipo de deal: ").append(reverso.getDealOriginal().isInterbancario()
                                ? "INTERBANCARIO" : "PROMOCION");
                sb.append(".\n\nMotivos:\n-------------------------");
                if (reverso.isPorCancelacion()) {
                    sb.append("\nPor cancelaci\u00f3n.");
                }
                if (reverso.getPorContratoSica() != null) {
                    sb.append("\nPor contrato SICA.");
                }
                if (reverso.getPorFechaValor() != null) {
                    sb.append("\nPor fecha valor.");
                }
                if (reverso.isReversoPorMonto()) {
                    sb.append("\nPor monto.");
                }
                if (reverso.isReversoPorTipoCambio()) {
                    sb.append("\nPor tipo de cambio.");
                }
                sb.append("\n\nInformaci\u00f3n deal original:\n-------------------------\n").
                append(getCadenaDeal(reverso.getDealOriginal())).
                append("\nInformaci\u00f3n deal balanceo:\n-------------------------\n").
                append(getCadenaDeal(reverso.getDealBalanceo()));
                if (reverso.getDealCorreccion() != null) {
                    sb.append("\nInformaci\u00f3n deal correcci\u00f3n:\n").
                            append("---------------------------\n").
                            append(getCadenaDeal(reverso.getDealCorreccion()));
                }
            }
            msg.setText(sb.toString());
            mailSender.send(msg);
        }
        catch (MailException e) {
            if (logger.isWarnEnabled()) {
                logger.warn(e);
            }
            throw new SicaException("Error al enviar el correo para el reverso: "
                    + reverso.getIdReverso());
        }
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
        sb.append("\nContrato Sica:     ").append(deal.getContratoSica().getNoCuenta());
        sb.append("\nCliente:           ").append(deal.getCliente().getNombreCompleto());
        sb.append("\nFecha Captura:     ").append(DATE_FORMAT.format(deal.getFechaCaptura()));
        sb.append("\nFecha Liquidaci\u00f3n: ").
                append(DATE_FORMAT.format(deal.getFechaLiquidacion()));
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
    protected final transient Log logger = LogFactory.getLog(getClass());

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
