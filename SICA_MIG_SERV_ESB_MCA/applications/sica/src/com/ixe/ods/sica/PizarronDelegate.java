/*
 * $Id: PizarronDelegate.java,v 1.12 2008/02/22 18:25:33 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright © 2006 LegoSoft S.C.
 */

package com.ixe.ods.sica;

import com.ixe.ods.sica.sdo.AdminPizarronServiceData;
import com.ixe.ods.springframework.web.context.ServletContextHolder;
import org.springframework.context.ApplicationContext;

import java.util.List;

/**
 * Delegate para comunicaci&oacute;n con la aplicaci&oacute;n del pizarr&oacute;n en Flex.
 * 
 * @author Jean C. Favila
 * @version $Revision: 1.12 $ $Date: 2008/02/22 18:25:33 $
 */
public class PizarronDelegate {
	
	/**
	 * Contructor sin parametros. No hace nada.
	 */
    public PizarronDelegate() {

    }

    /**
     * M&acute;todo ping; no hace nada.
     *
     */
    public void ping() {
    }

    /**
     * Regresa la lista con todas los canales existentes.
     *
     * @return List.
     */
    public List findAllCanales() {
        return getAdminPizarronServiceData().findAllCanales();
    }

    /**
     * Regresa la lista con todas las divisas frecuentes.
     *
     * @return List.
     */
    public List findAllDivisas() {
        return getAdminPizarronServiceData().findAllDivisas();
    }

    /**
     * Regresa la lista de los &uacute;ltimos spreads, para el canal y divisa especificados.
     *
     * @param idCanal El canal a encontrar.
     * @param idDivisa La divisa a encontrar.
     * @return List.
     */
    public List findSpreadsActuales(String idCanal, String idDivisa) {
        return getAdminPizarronServiceData().findSpreadsActuales(idCanal, idDivisa);
    }

    /**
     * Regresa la lista de los &uacute;ltimos factores de conversi&oacute;n.
     *
     * @return List.
     */
    public List findFactoresDivisaActuales() {
        return getAdminPizarronServiceData().findFactoresDivisaActuales();
    }

    /**
     * Obtiene el service data ligado al bean <b>adminPizarronServiceData</b>
     *
     * @return AdminPizarronServiceData.
     */
    protected AdminPizarronServiceData getAdminPizarronServiceData() {
        ApplicationContext applicationContext = ServletContextHolder.getApplicationContext();
        return (AdminPizarronServiceData) applicationContext.getBean("adminPizarronServiceData");
    }
}
