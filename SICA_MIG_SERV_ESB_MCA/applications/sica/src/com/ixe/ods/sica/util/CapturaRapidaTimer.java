/*
 * $Id: CapturaRapidaTimer.java,v 1.2 2008/02/22 18:25:40 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2007 LegoSoft S.C.
 */

package com.ixe.ods.sica.util;

import com.ixe.ods.sica.sdo.DealServiceData;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Jean C. Favila
 * @version $Revision: 1.2 $ $Date: 2008/02/22 18:25:40 $
 */
public class CapturaRapidaTimer {

    /**
     * Constructor por default.
     */
    public CapturaRapidaTimer() {
        super();
    }

    public void setTask(CapturaRapidaTask task) {
        Timer timer = new Timer();
        timer.schedule(task, 5000, 60000);

    }
}
