/*
 * $Id: SicaService.java,v 1.12 2008/02/22 18:25:34 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright © 2004 LegoSoft S.C.
 */

package com.ixe.ods.sica;

import com.ixe.ods.sica.model.Canal;

/**
 * Business interface para el EJB SicaSessionBean.
 *
 * @author Jean C. Favila, Javier Cordova (jcordova)
 * @version  $Revision: 1.12 $ $Date: 2008/02/22 18:25:34 $
 */
public interface SicaService {

    /**
     * Env&iacute;a el alerta de alta de deal.
     *
     * @param tipo El tipo de alerta.
     * @param idDeal El n&uacute;mero de Deal.
     */
    void enviarAlertaAltaDeal(String tipo, Long idDeal);

    /**
     * Env&iacute;a la informaci&oacute;n deal por mail.
     *
     * @param idDeal El n&uacute;mero de deal.
     */
    void enviarDealPorMail(Long idDeal);
    
    /**
     * Define el monto m&aacute;ximo vespertino 
     * 
     * @return boolean.
     */
    boolean validarMontoMaximoVespertino();

}