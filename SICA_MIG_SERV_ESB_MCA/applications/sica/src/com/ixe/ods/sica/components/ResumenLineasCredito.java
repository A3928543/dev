/*
 * $Id: ResumenLineasCredito.java,v 1.3.84.1 2016/07/13 21:45:26 mejiar Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2008 LegoSoft S.C.
 */

package com.ixe.ods.sica.components;

import org.apache.tapestry.BaseComponent;

import com.ixe.ods.sica.lineacredito.service.LineaCreditoService;
import com.ixe.ods.sica.model.LineaCambio;
import com.ixe.ods.sica.model.ParametroSica;
import com.ixe.ods.sica.pages.SicaPage;
import com.ixe.ods.sica.sdo.LineaCambioServiceData;

/**
 * Componente para desplegar el status y saldo de la l&iacute;nea de cambios de un cliente.
 *
 * @author Jean C. Favila
 * @version $Revision: 1.3.84.1 $ $Date: 2016/07/13 21:45:26 $
 */

public abstract class ResumenLineasCredito extends BaseComponent {

    /**
     * Regresa la l&iacute;nea de cambios del cliente.
     *
     * @return LineaCambio.
     */
    public LineaCambio getLinea() {
    	 LineaCreditoService lcService = getLineaCreditoService();
        if (getLineaCredito() == null || 
        	getLineaCredito().getCorporativo().getIdPersona().intValue() != getIdCliente()) {
        	setLineaCredito(lcService.findLineaCreditoByIdPersona(new Integer(getIdCliente())));
            setNumeroTotalExcesos(lcService.getNumeroTotalExcesos(ParametroSica.LC_CONSTANTE_EXCESOS));
        }
        return getLineaCredito();
    }
    
    
    
	/**
     * Obtiene el servicio que atiende el seguimiento y control de las lineas de credito.
     * @return LineaCreditoService 
     */
    private LineaCreditoService getLineaCreditoService(){
    	LineaCreditoService service = (LineaCreditoService) ((SicaPage) getPage()).
                 getApplicationContext().getBean(LineaCreditoService.LINEA_CREDITO_SERVICE);
    	return service;
    }

    /**
     * Regresa el valor del par&aacute;metro idCliente.
     *
     * @return int.
     */
    public abstract int getIdCliente();

    /**
     * Regresa el valor de la propiedad lineaCredito.
     *
     * @return LineaCambio.
     */
    public abstract LineaCambio getLineaCredito();

    /**
     * Establece el valor de la propiedad lineaCredito.
     *
     * @param lineaCambio El valor a asignar.
     */
    public abstract void setLineaCredito(LineaCambio lineaCambio);
    
    /**
     * Establece el numero de excesos permitidos a las lineas de credito
     */
    public abstract  void setNumeroTotalExcesos(Integer numeroTotalExcesos);
    
    /**
     * Regresa el valor  de la propiedad numero de Excesos  
     * @return numeroTotalExcesos
     */
    public abstract Integer getNumeroTotalExcesos();

}
