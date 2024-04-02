/*
 * $Id: CapturaRapidaMailSender.java,v 1.2 2008/02/22 18:25:50 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2007 LegoSoft S.C.
 */

package com.ixe.ods.sica.services;

import com.ixe.ods.sica.model.Canal;
import java.util.List;

/**
 * @author Jean C. Favila
 * @version $Revision: 1.2 $ $Date: 2008/02/22 18:25:50 $
 */
public interface CapturaRapidaMailSender {

    void enviarMailCanales(Canal canal, List deals);

    void enviarMailPromotor(String email, List deals);
}
