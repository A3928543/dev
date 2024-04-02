/*
 * $Id: SicaPizarronService.java,v 1.11 2008/02/22 18:25:11 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 - 2008 LegoSoft S.C.
 */

package com.ixe.ods.sica.pizarron;

import java.util.List;
import java.util.Map;

import com.ixe.ods.sica.model.TipoPizarron;

/**
 * Interfaz para el servicio que utiliza el pizarr&oacute;n.
 * 
 * @author Jean C. Favila
 * @version  $Revision: 1.11 $ $Date: 2008/02/22 18:25:11 $
 */
public interface SicaPizarronService {
    
	/**
	 * Obtiene la lista de divisas no frecuentes o metales.
	 * 
	 * @param metales Define si es metales o divisas no frecuentes.
	 * @return List
	 */
    List obtenerPizarronOtrasDivisas(boolean metales);

    /**
     * Crea el mapa con los datos del pizarr&oacute;n
     * 
     * @param ticket El ticket de la sesi&oacute;n.
     * @param tipoPizarron El tipo de pizarron.
     * @param sucursal Define si el pizarron es para sucursales.
     * @return Map
     */
    Map crearPizarron(String ticket, TipoPizarron tipoPizarron, boolean sucursal);
}
