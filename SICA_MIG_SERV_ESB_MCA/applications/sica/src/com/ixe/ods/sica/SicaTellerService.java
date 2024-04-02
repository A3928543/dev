/*
 * $Id: SicaTellerService.java,v 1.12 2008/02/22 18:25:33 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright © 2004 LegoSoft S.C.
 */
package com.ixe.ods.sica;

import com.ixe.ods.sica.model.Canal;

/**
 * Business interface para el EJB SicaSessionBean.
 *
 * @author gcorzo
 * @version  $Revision: 1.12 $ $Date: 2008/02/22 18:25:33 $
 */
public interface SicaTellerService {
	
	/**
	 * Define el monto m&aacute;ximo vespertino
	 * 
	 * @param canal El canal.
	 * @param monto El valor del monto.
	 * @return boolean
	 */
    boolean validarMontoMaximoVespertino(Canal canal, double monto);
}
