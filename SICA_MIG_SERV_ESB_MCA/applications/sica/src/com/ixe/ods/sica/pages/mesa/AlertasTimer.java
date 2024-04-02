/*
 * $Id: AlertasTimer.java,v 1.10 2008/02/22 18:25:33 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright © 2004 LegoSoft S.C.
 */

package com.ixe.ods.sica.pages.mesa;

import com.ixe.ods.sica.SicaAlertasService;
import com.ixe.ods.sica.sdo.SicaServiceData;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * <p> Clase que permite generar alertas de Deals que necesitan ser atendidos.<p>
 *
 * @author Edgar Leija
 * @version  $Revision: 1.10 $ $Date: 2008/02/22 18:25:33 $
 */
public class AlertasTimer {

    /**
    * Constructor por default.Inicializa el objeto, recibiendo una Lista de par&aacute;metros
    *
    * @param params Lista de parametros que incluyen las referencias a SicaServiceData y SicaAlertasService.
    */
    public AlertasTimer(List params) {
        super();
        this.sicaServiceData = (SicaServiceData) params.get(0);
        this.sicaAlertasService = (SicaAlertasService) params.get(1);
        Timer  timer = new Timer();
        TimerTask checarDeals = new DealTask(sicaServiceData,sicaAlertasService);
        String dealTimeOut = sicaServiceData.findTimeOut();
        Long time = new Long (dealTimeOut);
        long timeMilli = time.longValue() * 60000;
        timer.schedule( checarDeals, 240000, timeMilli);
    }

    /**
    * Objeto sicaServiceData que proporciona principales servicios.
    */
    private SicaServiceData sicaServiceData;

    /**
    * Objeto sicaAlertasService que proporciona el servicio de alertas.
    */
    private SicaAlertasService sicaAlertasService;
}
