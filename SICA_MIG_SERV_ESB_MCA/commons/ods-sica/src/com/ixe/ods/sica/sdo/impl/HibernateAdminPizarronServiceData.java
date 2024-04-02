/*
 * $Id: HibernateAdminPizarronServiceData.java,v 1.12.42.1 2011/04/29 00:32:56 galiciad Exp $
 * Ixe Grupo Financiero
 * Copyright © 2006 LegoSoft S.C.
 */

package com.ixe.ods.sica.sdo.impl;

import com.ixe.ods.sica.sdo.AdminPizarronServiceData;

import java.util.List;

/**
 * Subclase de <codel>AbstractHibernateSicaServiceData</code> que implementa los servicios
 * para la Administraci&oacute;n del Pizarr&oacute;n.
 * 
 * @author Jean C. Favila
 * @version $Revision: 1.12.42.1 $ $Date: 2011/04/29 00:32:56 $
 */
public class HibernateAdminPizarronServiceData extends AbstractHibernateSicaServiceData implements AdminPizarronServiceData {
	
	/**
	 * Encuentra todos los canales.
	 * 
	 * @return List
	 */
    public List findAllCanales() {
        return getHibernateTemplate().find("SELECT new com.ixe.ods.sica.posicion.vo.CanalVO(c.idCanal, c.nombre) FROM Canal AS c");
    }

    /**
     * Encuentra todas las divisas.
     * 
     * @return List
     */
    public List findAllDivisas() {
        return getHibernateTemplate().find("SELECT new com.ixe.ods.sica.posicion.vo.DivisaVO(d.idDivisa, d.descripcion, d.icono) FROM Divisa AS d WHERE d.tipo = 'F' ORDER BY d.orden");
    }
    
    /**
     * Encuentra los spreads actuales.
     * 
     * @return List
     */
    public List findSpreadsActuales(String idCanal, String idDivisa) {
        return getHibernateTemplate().find("SELECT new com.ixe.ods.sica.posicion.vo.SpreadVO(s.claveFormaLiquidacion, s.cpaVta.compraCash, s.cpaVta.ventaCash, s.cpaVta.compraTom, s.cpaVta.ventaTom, s.cpaVta.compraSpot, s.cpaVta.ventaSpot) FROM Spread AS s WHERE s.idSpread in (SELECT sa.idSpread FROM SpreadActual AS sa WHERE sa.id.canal.idCanal = ? AND sa.id.divisa.idDivisa = ?)",
                new Object[] { idCanal, idDivisa });
    }
    
    /**
     * Encuentra los factores actuales de las divisas.
     * 
     * @return List
     */
    public List findFactoresDivisaActuales() {
        return getHibernateTemplate().find("SELECT new com.ixe.ods.sica.posicion.vo.FactorDivisaVO(fd.facDiv.fromDivisa.idDivisa, fd.facDiv.toDivisa.idDivisa, fd.facDiv.factor, fd.facDiv.spreadReferencia, fd.facDiv.spreadCompra, fd.facDiv.carry, fd.facDiv.ultimaModificacion) FROM FactorDivisaActual AS fd");
    }
}
