/*
 * $Id: MigracionPlantillasCuentaSeleccionada.java,v 1.10 2008/02/22 18:25:32 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright © 2005 LegoSoft S.C.
 */
package com.ixe.ods.sica.pages.mesa;

import java.util.List;

import com.ixe.ods.sica.pages.SicaPage;

/**
 * P&aacute;gina que muestra los datos de las plantillas aosciadas a una
 * cuenta de cliente. La p&agina se muestra en forma de Pop Up.
 * 
 * @author Gustavo Gonzalez
 * @version  $Revision: 1.10 $ $Date: 2008/02/22 18:25:32 $
 */
public abstract class MigracionPlantillasCuentaSeleccionada extends SicaPage {
	
	/**
	 * Obtiene el valor de CuentaSeleccionada
	 * 
	 * @return List
	 */
    public abstract List getCuentaSeleccionada();
    
    /**
     * Establece el valor para CuentaSeleccionada
     * 
     * @param list Lista plantillas para la cuenta seleccionada.
     */
    public abstract void setCuentaSeleccionada(List list);
}