/*
 * $Id: RiesgosTimer.java,v 1.10 2008/02/22 18:25:32 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright © 2004 LegoSoft S.C.
 */

package com.ixe.ods.sica.pages.mesa;

import com.ixe.ods.sica.model.ParametroSica;
import com.ixe.ods.sica.sdo.CierreSicaServiceData;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Clase que permite verificar los montos l&iacute;mites de la mesa durante el d&iacute;a
 * y al cierre del mismo, y en caso de ser necesario, generar las alertas correspondientes
 * seg&uacute;n sea el caso.
 * 
 * @author Edgar Leija
 * @version  $Revision: 1.10 $ $Date: 2008/02/22 18:25:32 $
 */
public class RiesgosTimer {

    /**
     * Constructor por default.No hace nada.
     */
    public RiesgosTimer() {
        super();
    }

    /**
     * Fija la referencia al objeto que proporciona los servicios del cierre, dentro del cu&aacute;l
     * se encuentra la l&oacute;gica que checa si hay excedentes o si se est&aacute; corto en los l&iacute;mites de Riesgo en
     * la Mesa.
     * Se crea una instancia de TimerTask la cu&aacute;l con una frecuencia definida en Par&aacute;metros
     * lo checa y manda una alerta as&iacute; sea el caso.
     *
     * @param cierreServiceData El servicio para el cierre del d&iacute;a.
     */ 
    public void setCierreServiceData(CierreSicaServiceData cierreServiceData) {
        this.cierreServiceData = cierreServiceData;
        Timer  timer = new Timer();
        TimerTask checarRiesgo = new RiesgoTask(cierreServiceData);
        String riesgoTimeOut= cierreServiceData.findParametro(ParametroSica.TIME_OUT_RIESGO);
        Long time = new Long (riesgoTimeOut);
        long timeMilli = time.longValue() * 60000;
        timer.schedule( checarRiesgo, 30000, timeMilli);
    }
    /**
     * Referencia a objeto que proporciona los servicios del cierre de d&iacute;a
     */
    private CierreSicaServiceData cierreServiceData;
}
