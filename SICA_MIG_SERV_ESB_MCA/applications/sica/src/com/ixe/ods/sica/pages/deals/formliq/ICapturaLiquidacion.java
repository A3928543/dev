/*
 * $Id: ICapturaLiquidacion.java,v 1.10 2008/02/22 18:25:01 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright © 2005 LegoSoft S.C.
 */
package com.ixe.ods.sica.pages.deals.formliq;

import com.ixe.ods.sica.model.DealDetalle;
import com.legosoft.tapestry.IActivate;

/**
 * Interfaz que deben implementar las p&aacute;ginas que permiten al usuario capturar la informaci&oacute;n de un
 * detalle de deal y/o su plantilla.
 *
 * @author Jean C. Favila
 * @version  $Revision: 1.10 $ $Date: 2008/02/22 18:25:01 $
 */
public interface ICapturaLiquidacion extends IActivate {

    /**
     * Debe generar una nueva instancia de la plantilla correspondiente.
     */
    void crearPlantilla();

    /**
     * Debe establecer el valor de la propiedad dealDetalle.
     *
     * @param dealDetalle El valor a asignar.
     */
    void setDealDetalle(DealDetalle dealDetalle);

    /**
     * Debe establecer el valor de la propiedad modo (MODO_CAPTURA | MODO_INSERCION_PLANTILLA).
     *
     * @param modo El valor a asignar.
     */
    void setModo(int modo);
    
    /**
     * Fija si el detalle tiene plantilla o no.
     * 
     * @param tienePlantilla True o False.
     */
    void setTienePlantilla(boolean tienePlantilla);
    
    /**
     * Modo de capttura.
     */
    public final static int MODO_CAPTURA = 0;
    
    /**
     * Modo de inserci&oacute;n de plantilla.
     */
    public final static int MODO_INSERCION_PLANTILLA = 1;
    
}
