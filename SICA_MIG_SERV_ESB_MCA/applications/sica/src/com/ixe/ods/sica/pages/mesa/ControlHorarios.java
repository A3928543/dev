/*
 * $Id: ControlHorarios.java,v 1.14 2008/02/22 18:25:33 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 LegoSoft S.C.
 */

package com.ixe.ods.sica.pages.mesa;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.SicaSiteCache;
import com.ixe.ods.sica.model.Estado;
import com.ixe.ods.sica.model.ParametroSica;
import com.ixe.ods.sica.pages.SicaPage;
import com.ixe.ods.sica.sdo.CierreSicaServiceData;
import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.valid.IValidationDelegate;

/**
 * <p>P&aacute;gina que muestra y permite al usuario modificar los parametros de Horarios del
 * Sistema.
 *
 * @author Edgar Leija
 * @version  $Revision: 1.14 $ $Date: 2008/02/22 18:25:33 $
 */
public abstract class ControlHorarios extends SicaPage {

    /**
     * Coordina el despliegue de los diferentes estados posibles del sistema.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void activate(IRequestCycle cycle) {
        super.activate(cycle);
        try {
            setEstados(getSicaServiceData().findAllEstadoOrdenado());
            setCierre(Estado.ESTADO_GENERACION_CONTABLE == getSicaServiceData().
                    findEstadoSistemaActual().getIdEstado());
        }
        catch (SicaException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Regresa el arreglo con todos los estados posibles del sistema.
     *
     * @return int[].
     */
    protected int[] getEstadosValidos() {
        return new int[] {
            Estado.ESTADO_INICIO_DIA, Estado.ESTADO_OPERACION_NORMAL,
            Estado.ESTADO_OPERACION_RESTRINGIDA, Estado.ESTADO_FIN_LIQUIDACIONES,
            Estado.ESTADO_OPERACION_VESPERTINA, Estado.ESTADO_GENERACION_CONTABLE,
            Estado.ESTADO_CIERRE_DIA, Estado.ESTADO_OPERACION_NOCTURNA,
            Estado.ESTADO_BLOQUEADO };
    }

    /**
     * Regresa el estado actual del sistema
     *
     * @return estado.
     */
    public Estado getActual() {
         for (Iterator it = getEstados().iterator(); it.hasNext();) {
             Estado estado = (Estado) it.next();
             if (estado.isActual()) {
                 return estado;
             }
        }
        throw new ApplicationRuntimeException("No se encontr\u00f3 el estado actual.");
    }

    /**
     * Guarda las modificaciones hechas a los horarios del sistema.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void actualizarHoraFin(IRequestCycle cycle) {
    	List estados = getSicaServiceData().findAllEstadoOrdenado();
    	for (Iterator it = estados.iterator(); it.hasNext();) {
    		Estado estadoNormal = (Estado) it.next();
    		if (estadoNormal.getHoraFin()!= null) {
    			for (Iterator it1 = getEstados().iterator(); it1.hasNext();) {
		    		Estado estado = (Estado) it1.next();
		            if (estado.getHoraFin() != null) {
		            	estadoNormal.setHoraFin(estado.getHoraFin());
		            }
		    	}
    		}
    	}
    	getSicaServiceData().update(estados);
    }
    
    /**
     * Refresca la p&aacute;gina de horarios.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void refrescar(IRequestCycle cycle) {
    		activate(cycle);
    }

    /**
     * Permite bloquear o desbloquear el sistema.
     * 
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void bloquearDesbloquear(IRequestCycle cycle) {
        Estado estadotemp = null;
        for (Iterator it = getEstados().iterator(); it.hasNext();) {
            Estado estado = (Estado) it.next();
            if (estado.isActual()) {
                estado.setActual(false);
                estadotemp = estado;
            }
            if (estado.getIdEstado() == Estado.ESTADO_BLOQUEADO) {
                estado.setActual(true);
                estado.setSiguienteEstado(estadotemp);
            }
        }
        getSicaServiceData().update(getEstados());
    }

    /**
     * Permite hacer el cierre del d&iacute;a del Sistema.
     * 
     * @param cycle El ciclo de la p&aacute;gina.
     * @throws Exception 
     */
    public void cerrarSica(IRequestCycle cycle) throws SicaException {
    	IValidationDelegate delegate = getDelegate();
    	Estado estadoActual = getSicaServiceData().findEstadoSistemaActual();
    	try {
    		if (Estado.ESTADO_GENERACION_CONTABLE == estadoActual.getIdEstado()) {
    			CierreSicaServiceData cierreServiceData = (CierreSicaServiceData)
    				getApplicationContext().getBean("cierreServiceData");
    			cierreServiceData.cambiarEstadoCierre();
    			LogCierre nextPage = (LogCierre) cycle.getPage("LogCierre");
    			SicaSiteCache ssc = (SicaSiteCache) (getApplicationContext().getBean("sicaSiteCache"));
    			nextPage.setLog(cierreServiceData.cerrarDiaSica(ssc, getTicket(), getSicaServiceData().getFechaSistema()));
    			cycle.activate(nextPage);
    		}
    		else {
    			throw new SicaException("No se puede realizar la operaci\u00f3n debido a que el cierre del d\u00EDa ya est\u00e1 en proceso.");
    		}
    	}
    	catch (Exception e) {
            delegate.record(e.getMessage(), null);
    		warn(e);
    		activate(cycle);
    	}
    }

    /**
     * Permite cambiar de un estado a otro ya sea manual o automaticamente.
     * 
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void activarPagina(IRequestCycle cycle) {
        Estado estadoTemp = null;
        if (cycle.getServiceParameters()[0].toString().equals("DesbloquearSistema")
                || cycle.getServiceParameters()[0].toString().equals("FinOperacionNocturna")) {
            for (Iterator it = getEstados().iterator(); it.hasNext();) {
                Estado estado = (Estado) it.next();
                if (Estado.ESTADO_BLOQUEADO == estado.getIdEstado()
                        || Estado.ESTADO_OPERACION_NOCTURNA == estado.getIdEstado()) {
                    if (estado.isActual()) {
                        estado.setActual(false);
                        estadoTemp = estado.getSiguienteEstado();
                    }
                }
            }
            for (Iterator it = getEstados().iterator(); it.hasNext();) {
                Estado estado = (Estado) it.next();
                if (estadoTemp != null) {
                    if (estado.getIdEstado() == estadoTemp.getIdEstado()) {
                        estado.setActual(true);
                        estado.setHoraInicio(HOUR_FORMAT.format(new Date()));
                    }
                }
            }
            getSicaServiceData().update(getEstados());
            if (cycle.getServiceParameters()[0].toString().equals("FinOperacionNocturna")) {
                ParametroSica conf = getSicaServiceData().findParametro(ParametroSica.CONF_SPREADS);
                conf.setValor("1");
                ParametroSica precio = getSicaServiceData().
                        findParametro(ParametroSica.CONF_PRECIO_REF);
                precio.setValor("1");
                ParametroSica factores = getSicaServiceData().
                        findParametro(ParametroSica.CONF_FACTORES_DIV);
                factores.setValor("1");
                getSicaServiceData().update(conf);
                getSicaServiceData().update(precio);
                getSicaServiceData().update(factores);
                getSicaServiceData().update(getEstados());
            }
        }
        else {
            SicaPage nextPage = (SicaPage) cycle.
                    getPage(cycle.getServiceParameters()[0].toString());
            nextPage.activate(cycle);
        }
    }

    /**
     * Regresa la Lista de Estados
     *
     * @return List
     */
    public abstract List getEstados();

    /**
     * Fija los Estados
     *
     * @param estados El valor a asignar.
     */
    public abstract void setEstados(List estados);

     /**
     * Regresa el valor de cierre
     *
     * @return boolean
     */
    public abstract boolean getCierre();

    /**
     * Fija variable cierre
     *
     * @param cierre El valor a asignar.
     */
    public abstract void setCierre(boolean cierre);
    
    /**
     * Regresa el modo de ejecuci&oacute;n del bot&oacute;n
     *
     * @return int
     */
    public abstract int getModoRefresh();

    /**
     * Fija el modo de ejecuci&oacute;n del bot&oacute;n
     *
     * @param modoRefresh El valor a asignar.
     */
    public abstract void setModoRefresh(int modoRefresh);
}
