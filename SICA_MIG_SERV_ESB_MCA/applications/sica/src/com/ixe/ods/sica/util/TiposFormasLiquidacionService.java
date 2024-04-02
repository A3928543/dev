/*
 * $Id: TiposFormasLiquidacionService.java,v 1.1.2.1 2010/10/08 01:10:59 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica.util;

import java.util.List;

import com.ixe.ods.sica.model.DealDetalle;

/**
 * @author Jean C. Favila (jfavila)
 * @version $Revision: 1.1.2.1 $ $Date: 2010/10/08 01:10:59 $
 */
public interface TiposFormasLiquidacionService {

    /**
	 * Obtiene los Tipos de Liquidaci&oacute;n.
	 *
	 * @return String[].
	 */
    String[] getTiposLiquidacion(String ticket, DealDetalle det);
    
   	/**
	 * Lista de Mnemm&oacute;nicos.
	 *
	 * @return List
	 */
    List getMnemonicos(String ticket, DealDetalle det, String tipoLiquidacion);
}
