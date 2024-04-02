/*
 * $Id: CatFactorDivisa.java,v 1.10.42.1 2011/04/26 02:44:24 galiciad Exp $
 * Ixe Grupo Financiero
 * Copyright © 2004 LegoSoft S.C.
 */
package com.ixe.ods.sica.pages.catalogos;

import com.ixe.ods.sica.model.Divisa;
import com.ixe.ods.sica.model.FactorDivisaActual;
import com.ixe.ods.sica.sdo.SicaServiceData;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.valid.IValidationDelegate;
import org.apache.tapestry.valid.ValidationConstraint;

/**
 * Clase que permite agregar y editar Factores Divisa.
 *
 * @author Jean C. Favila, Javier Cordova
 * @version  $Revision: 1.10.42.1 $ $Date: 2011/04/26 02:44:24 $
 */
public abstract class CatFactorDivisa extends AbstractCatEdicion {

    /**
     * Actualiza o agrega, segun sea el caso, en registro Factor Divisa.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
	public void actualizar(IRequestCycle cycle) {
        IValidationDelegate delegate = (IValidationDelegate) getBeans().getBean("delegate");
        if (delegate != null && delegate.getHasErrors()) {
            return;
        }
        FactorDivisaActual fd = (FactorDivisaActual) getRegistro();
        if (isModoUpdate()) {
            getSicaServiceData().update(fd);
        }
        else {
            if (!existeCombinacion() && esCombinacionValida()) {
                if (fd.getFacDiv().getFromDivisa().isDivide() || fd.getFacDiv().getToDivisa().isDivide()) {
                    fd.getFacDiv().setFactor(1.0 / fd.getFacDiv().getFactor());
                }
                getSicaServiceData().store(fd);
            }
            else if (delegate != null && existeCombinacion()) {
                delegate.record("Ya existe la combinacion seleccionada.", ValidationConstraint.REQUIRED);
                return;
            }
            else if (delegate != null && !esCombinacionValida()) {
                delegate.record("La combinacion seleccionada no es valida.", ValidationConstraint.REQUIRED);
                return;
            }
        }
        cycle.activate(getNombrePaginaConsulta());
    }

    /**
     * Se verifica si el registro a insertar o a actualizar es valido y que no este repetido.
     *
     * @return Si se puede o no insertar/actualizar el registro.
     */
	private boolean existeCombinacion() {
        SicaServiceData sd = getSicaServiceData();
    	FactorDivisaActual fd = (FactorDivisaActual) getRegistro();
        return (sd.findFactorDivisaActualFromTo(fd.getFacDiv().getFromDivisa().getIdDivisa(),
        		fd.getFacDiv().getToDivisa().getIdDivisa()) != null) ||
				(sd.findFactorDivisaActualFromTo(fd.getFacDiv().getToDivisa().getIdDivisa(),
				fd.getFacDiv().getFromDivisa().getIdDivisa()) != null);
    }

    /**
     * Regresa true si la combinaci&oacute;n de ambas divisas (FROM y TO) est&aacute; permitida.
     *
     * @return boolean.
     */
    private boolean esCombinacionValida() {
    	FactorDivisaActual fd = (FactorDivisaActual) getRegistro();
    	boolean respuesta = false;

    	if (fd.getFacDiv().getFromDivisa().getIdDivisa().equals(fd.getFacDiv().getToDivisa().getIdDivisa()) &&
    		Divisa.DOLAR.equals(fd.getFacDiv().getFromDivisa().getIdDivisa())) {
    		respuesta = true;
    	}
    	else if (Divisa.DOLAR.equals(fd.getFacDiv().getFromDivisa().getIdDivisa()) && fd.getFacDiv().getToDivisa().isFrecuente()) {
    		respuesta = true;
    	}
    	else if (Divisa.DOLAR.equals(fd.getFacDiv().getToDivisa().getIdDivisa()) && fd.getFacDiv().getFromDivisa().isFrecuente()) {
    		respuesta = true;
    	}
    	else if (Divisa.PESO.equals(fd.getFacDiv().getFromDivisa().getIdDivisa()) && !fd.getFacDiv().getToDivisa().isFrecuente()) {
    		respuesta = true;
    	}
    	else if (Divisa.PESO.equals(fd.getFacDiv().getToDivisa().getIdDivisa()) && !fd.getFacDiv().getFromDivisa().isFrecuente()) {
    		respuesta = true;
    	}
    	return respuesta;
	}
}