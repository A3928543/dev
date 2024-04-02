/*
 * $Id: CapturaRapidaTask.java,v 1.3 2008/02/22 18:25:40 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2007 LegoSoft S.C.
 */

package com.ixe.ods.sica.util;

import com.ixe.ods.bup.model.MedioContacto;
import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.model.Canal;
import com.ixe.ods.sica.model.Deal;
import com.ixe.ods.sica.sdo.DealServiceData;
import com.ixe.ods.sica.sdo.SicaServiceData;
import com.ixe.ods.sica.services.CapturaRapidaMailSender;
import com.ixe.ods.sica.utils.DateUtils;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Jean C. Favila
 * @version $Revision: 1.3 $ $Date: 2008/02/22 18:25:40 $
 */
public class CapturaRapidaTask extends TimerTask {

    /**
     * Constructor por default.
     */
    public CapturaRapidaTask() {
        super();
    }

    public void run() {
        Date fechaActual = new Date();
        if (ultimaFechaCanales == null
                || ultimaFechaCanales.getTime() + INTERVALO < fechaActual.getTime()) {
            Calendar cal = new GregorianCalendar();
            if (cal.get(Calendar.MINUTE) < 5) {
                notificarCanales();
                ultimaFechaCanales = fechaActual;
            }
        }
        if (ultimaFechaPromotores == null
                || !DateUtils.inicioDia(ultimaFechaPromotores).equals(DateUtils.inicioDia())) {
            Calendar fechaLimite = new GregorianCalendar();
            fechaLimite.set(Calendar.HOUR_OF_DAY, 13);
            fechaLimite.set(Calendar.MINUTE, 45);
            fechaLimite.set(Calendar.SECOND, 0);
            fechaLimite.set(Calendar.MILLISECOND, 0);
            if (fechaActual.getTime() > fechaLimite.getTime().getTime()) {
                notificarPromotores();
                ultimaFechaPromotores = fechaActual;
            }

        }
    }

    private void notificarCanales() {
        logger.warn(new Date() + "\n\n Notificando canales captura r\u00e1pida...");
        try {
            Calendar fechaInicio = new GregorianCalendar();
            fechaInicio.add(Calendar.DAY_OF_MONTH, -3);            
            Date fechaActual = new Date();
            List deals = dealServiceData.findDealsCapturaRapida(fechaInicio.getTime(), fechaActual,
                    null, null, false);
            Map dealsCanales = new HashMap();
            for (Iterator it = deals.iterator(); it.hasNext();) {
                Deal deal = (Deal) it.next();
                String idCanal = deal.getCanalMesa().getCanal().getIdCanal();
                List valorDeals = (List) (dealsCanales.get(idCanal) == null
                        ? new ArrayList() : dealsCanales.get(idCanal));
                valorDeals.add(deal);
                dealsCanales.put(idCanal, valorDeals);
            }
            System.out.println("*** Deals canales: " + dealsCanales);
            // Se iteran los canales y se env&iacute;an los correos:
            for (Iterator it = dealsCanales.keySet().iterator(); it.hasNext();) {
                String idCanal = (String) it.next();
                deals = (List) dealsCanales.get(idCanal);
                Canal canal = ((Deal) deals.get(0)).getCanalMesa().getCanal();
                getMailSender().enviarMailCanales(canal, deals);
            }
        }
        catch (SicaException e) {
            if (logger.isDebugEnabled()) {
                logger.debug(e);
            }
        }
        logger.warn(new Date() + " Canales notificados captura r\u00e1pida.");
    }

    private void notificarPromotores() {
        logger.warn(new Date() + " Notificando promotores captura r\u00e1pida.");
        try {
            Calendar fechaInicio = new GregorianCalendar();
            fechaInicio.add(Calendar.DAY_OF_MONTH, -3);
            Date fechaActual = new Date();
            Map dealsPromotores = new HashMap();
            List deals = dealServiceData.findDealsCapturaRapida(fechaInicio.getTime(), fechaActual,
                    null, null, false);
            for (Iterator it = deals.iterator(); it.hasNext();) {
                Deal deal = (Deal) it.next();
                Integer idPromotor = deal.getUsuario().getIdPersona();
                List valorDeals = (List) (dealsPromotores.get(idPromotor) == null
                        ? new ArrayList() : dealsPromotores.get(idPromotor));
                valorDeals.add(deal);
                dealsPromotores.put(idPromotor, valorDeals);
            }
            System.out.println(dealsPromotores);
            for (Iterator it = dealsPromotores.keySet().iterator(); it.hasNext();) {
                Integer idPromotor = (Integer) it.next();
                deals = (List) dealsPromotores.get(idPromotor);
                System.out.println("Enviando mail al promotor " + idPromotor + " deals: " + deals);
                List mediosContacto = getSicaServiceData().findMedioContactoByIdPersona(idPromotor);
                if (mediosContacto.isEmpty()) {
                    logger.warn("El promotor " + idPromotor + " no tiene email registrado.");
                }
                else {
                    MedioContacto medioContacto = (MedioContacto) mediosContacto.get(0);
                    getMailSender().enviarMailPromotor(medioContacto.getValor(), deals);                    
                }
            }

        }
        catch (SicaException e) {
            if (logger.isDebugEnabled()) {
                logger.debug(e);
            }
        }
        logger.warn(new Date() + " Promotores notificados captura r\u00e1pida.");
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
     * Regresa el valor de dealServiceData.
     *
     * @return DealServiceData.
     */
    public DealServiceData getDealServiceData() {
        return dealServiceData;
    }

    /**
     * Establece el valor de dealServiceData.
     *
     * @param dealServiceData El valor a asignar.
     */
    public void setDealServiceData(DealServiceData dealServiceData) {
        this.dealServiceData = dealServiceData;
    }

    /**
     * Regresa el valor de mailSender.
     *
     * @return CapturaRapidaMailSender.
     */
    public CapturaRapidaMailSender getMailSender() {
        return mailSender;
    }

    /**
     * Establece el valor de mailSender.
     *
     * @param mailSender El valor a asignar.
     */
    public void setMailSender(CapturaRapidaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * La referencia al DealServiceData para realizar operaciones a la base de datos.
     */
    private DealServiceData dealServiceData;

    /**
     * La referencia al SicaServiceData, utilizado para consultar los medios de contacto de los
     * promotores.
     */
    private SicaServiceData sicaServiceData;

    /**
     * La &uacute;ltima fecha en que se notific&oacute; a los canales.
     */
    private Date ultimaFechaCanales;

    /**
     * La &uacute;ltima fecha en que se notific&oacute; a los promotores.
     */
    private Date ultimaFechaPromotores;

    /**
     * El componente para enviar los correos electr&oacute;nicos.
     */
    private CapturaRapidaMailSender mailSender;

    /**
     * El objeto para logging.
     */
    protected final transient Log logger = LogFactory.getLog(getClass());
    
    private static final int INTERVALO = 3600000;
}
