/*
 * $Id: GeneracionContable.java,v 1.16 2009/12/28 16:08:28 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 - 2009 LegoSoft S.C.
 */
package com.ixe.ods.sica.pages.contabilidad;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.valid.IValidationDelegate;

import com.ixe.ods.sica.model.Estado;
import com.ixe.ods.sica.pages.SicaPage;
import com.ixe.ods.sica.pages.mesa.ControlHorarios;
import com.ixe.ods.sica.sdo.ContabilidadSicaServiceData;

/**
 * P&aacute;gina que permite al usuario generar los movimientos contables.
 *
 * @author Edgar Leija
 * @version  $Revision: 1.16 $ $Date: 2009/12/28 16:08:28 $
 */
public abstract class GeneracionContable extends SicaPage {

    /**
     * M&eacut;etodo que manda a llamar a la parte de contabilidad dentro del cierre del d&iacute;a
     * 
     * @param cycle El ciclo de la p&acute;gina.
     *
     */
    public void activate(IRequestCycle cycle) {
        super.activate(cycle);
    }
    
    /**
     * Activa el submit de la p&aacute;gina.
     * 
     * @param cycle El ciclo de la p&acute;gina.
     */
    public void formSubmit(IRequestCycle cycle) {
        generarContabilidad(cycle);
    }

    /**
     * Genera el ciclo de generaci&oacute;n de la contabilidad.
     * 
     * @param cycle El ciclo de la p&acute;gina.
     */
    public void generarContabilidad(IRequestCycle cycle) {
        ContabilidadSicaServiceData contabilidadServiceData = (ContabilidadSicaServiceData)
                getApplicationContext().getBean("contabilidadServiceData");
        IValidationDelegate delegate = (IValidationDelegate) getBeans().getBean("delegate");
        try {
			contabilidadServiceData.generacionMovimientosContables();
			List estados = getSicaServiceData().findAll(Estado.class);
	        for (Iterator it = estados.iterator(); it.hasNext();) {
	            Estado estado = (Estado) it.next();
	                if (estado.isActual()) {
	                    estado.setActual(false);
	                }
	                if (estado.getIdEstado() == Estado.ESTADO_GENERACION_CONTABLE) {
	                	estado.setHoraInicio(HOUR_FORMAT.format(new Date()));
	                    estado.setActual(true);
	                }
	        }
	        getSicaServiceData().update(estados);
	        ControlHorarios nextPage = (ControlHorarios) cycle.getPage("ControlHorarios");
	        nextPage.activate(cycle);
		} 
        catch (Exception e) {
            warn(e.getMessage(), e);
            delegate.record(e.getMessage(), null);
        }
    }

    /**
    * Fija el valor del resultado.
     *
     * @param s El valor a asignar.
     */
    public abstract void setResult(String s);

    /**
     * Regresa el arreglo con los estados de operaci&oacute;n normal y operaci&oacute;n restringida.
     *
     * @return int[].
     */
    protected int[] getEstadosValidos() {
        return new int[] { Estado.ESTADO_FIN_LIQUIDACIONES };
    }

    /**
     * Regresa el string del resultado.
     *
     * @return String
     */
    public abstract String getResult();
}
