/*
 * $Id: DealTask.java,v 1.10 2008/02/22 18:25:33 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright © 2004 LegoSoft S.C.
 */

package com.ixe.ods.sica.pages.mesa;

import com.ixe.ods.sica.Constantes;
import com.ixe.ods.sica.SicaAlertasService;
import com.ixe.ods.sica.model.Deal;
import com.ixe.ods.sica.model.DealHelper;
import com.ixe.ods.sica.sdo.SicaServiceData;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TimerTask;

/**
 * <p> Clase que checa si un Deal necesita ser atendido y manda alerta si se requiere <p>.
 *
 * @author Edgar Leija
 * @version $Revision: 1.10 $ $Date: 2008/02/22 18:25:33 $
 */
public class DealTask extends TimerTask {

    /**
     * Constructor por default. No hace nada.
     */
    public DealTask() {
        super();
    }

    /**
     * Inicializa el objeto con dos par&aacute;metros.Estos p&aacute;rametros son referencias a dos beans uno de ellos
     * permite usar todos los servicios del sistema y otro permite mandar alertas.
     *
     * @param sicaServiceData El servicio sicaServiceData.
     * @param _sicaAlertasService El servicio _sicaAlertasService.
     */
    public DealTask(SicaServiceData sicaServiceData, SicaAlertasService _sicaAlertasService) {
        this();
        this.sicaServiceData = sicaServiceData;
        this._sicaAlertasService = _sicaAlertasService;
    }

    /**
     * Llamado por el Timer de AlertasTimer el c&uacute;al manda una alerta para un Deal que necesita ser atendido. De
     * la misma manera aumenta su TimeOut.
     */
    public void run() {
        Date fechaHoy = new Date();
        List deals = sicaServiceData.findDealsAlertas();
        for (Iterator it = deals.iterator(); it.hasNext();) {
            try {
                Deal deal = (Deal) it.next();
                Integer timeo = new Integer(deal.getTimeout());
                if (timeo.intValue() < (fechaHoy.getTime() - deal.getFechaCaptura().getTime()) / 3600000) {
                    deal.setTimeout(timeo.intValue() + 1);
                    sicaServiceData.update(deal);
                    deal = sicaServiceData.findDeal(deal.getIdDeal());
                    enviarAlertaDeal(Constantes.SC_TIMEO, deal);
                }
            }
            catch (Exception e) {
                if (_logger.isDebugEnabled()) {
                    _logger.debug(e);
                }
            }
        }
    }

    /**
     * Recibe el tipo de alerta y el objeto Deal y construye la alerta.
     *
     * @param tipo El tipo de la alerta.
     * @param deal El deal.
     */
    public void enviarAlertaDeal(String tipo, Deal deal) {
        _sicaAlertasService.generaAlerta(tipo, deal.getCliente().getIdPersona(), DealHelper.getContextoAlertas(deal));
    }
    
    /**
     * Referencia a bean de SicaServiceData
     */
    private SicaServiceData sicaServiceData;

    /**
     * Referencia a bean SicaAlertasService.
     */
    private SicaAlertasService _sicaAlertasService;

    /**
     * El objeto para logging.
     */
    protected final transient Log _logger = LogFactory.getLog(getClass());
}
