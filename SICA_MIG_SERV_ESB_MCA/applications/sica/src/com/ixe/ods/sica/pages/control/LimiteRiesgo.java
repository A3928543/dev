/*
 * $Id: LimiteRiesgo.java,v 1.13 2008/05/31 01:20:01 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2004 - 2008 LegoSoft S.C.
 */
package com.ixe.ods.sica.pages.control;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.form.StringPropertySelectionModel;

import com.ixe.isis.tapestry.IxenetValidationDelegate;
import com.ixe.ods.sica.dao.LimiteDao;
import com.ixe.ods.sica.model.Divisa;
import com.ixe.ods.sica.model.Estado;
import com.ixe.ods.sica.model.Limite;
import com.ixe.ods.sica.model.MesaCambio;
import com.ixe.ods.sica.model.ParametroSica;
import com.ixe.ods.sica.model.Posicion;
import com.ixe.ods.sica.model.TipoLimite;
import com.ixe.ods.sica.pages.SicaPage;

/**
 * Cat&aacute;logo que permite modificar el limite de riesgo
 *
 * @author Edgar I Leija
 * @version  $Revision: 1.13 $ $Date: 2008/05/31 01:20:01 $
 */
public abstract class LimiteRiesgo extends SicaPage {
	
	/**
	 * Asigna los par&aacute;metros necesarios al activarse la p&aacute;gina.
	 * 
	 * @param cycle El ciclo de la p&aacute;gina.
	 */
	public void activate(IRequestCycle cycle) {
		super.activate(cycle);
        List mesasCambio = getSicaServiceData().findAll(MesaCambio.class);
        MesaCambio mesaTodas = new MesaCambio();
        mesaTodas.setNombre("**Todas**");
        mesasCambio.add(0, mesaTodas);
        setMesasCambio(mesasCambio);
        setCapitalDelGrupo(Double.parseDouble((getSicaServiceData().
                findParametro(ParametroSica.CAPITAL_DEL_GRUPO)).getValor()));
		setPorcentajeCapital(Double.parseDouble((getSicaServiceData().
                findParametro(ParametroSica.PORCENTAJE_CAPITAL_GRUPO)).getValor()));
		setLimiteRegulatorio((getCapitalDelGrupo()) * (getPorcentajeCapital() / 100));
		setMinutosChecarLimite(Integer.valueOf((getSicaServiceData().
                findParametro(ParametroSica.TIME_OUT_RIESGO)).getValor()).intValue());
		setPorcentajeAvisoGlobal(Integer.valueOf((getSicaServiceData().findParametro(
                ParametroSica.PORC_AVISO_LIMITE_REGULATORIO)).getValor()).intValue());
		setPorcentajeAlarmaGlobal(Integer.valueOf((getSicaServiceData().findParametro(
                ParametroSica.PORC_ALARMA_LIMITE_REGULATORIO)).getValor()).intValue());
		setMontoMaximoDeal(Integer.valueOf((getSicaServiceData().findParametro(
                ParametroSica.MONTO_MAXIMO_DEAL)).getValor()).intValue());
		setNivelDeConfianza(Double.parseDouble((getSicaServiceData().
                findParametro(ParametroSica.NIVEL_CONFIANZA_RIESGO)).getValor()));
		setHorizonteTiempo((getSicaServiceData().
                findParametro(ParametroSica.HORIZONTE_TIEMPO_RIESGO)).getValor());
		MesaCambio mesaOperacion = getSicaServiceData().findMesaCambio(1);
		setMesaCambioSeleccionada(mesaOperacion);
		findLimitesPorMesa(cycle);
		List posicionList = getSicaServiceData().
                findPosicionByIdMesaCambio(mesaOperacion.getIdMesaCambio());
		if (posicionList.size() > 0) {
			Posicion posicion = (Posicion) getSicaServiceData().
                    findPosicionByIdMesaCambio(mesaOperacion.getIdMesaCambio()).get(0);
			setDivisaSeleccionada(posicion.getDivisa());
			limitesPorDivisa(cycle);
		}
	}

	/**
	 * Actualiza los valores de los l&iacute;mites.
	 * 
	 * @param cycle El ciclo de la p&aacute;gina.
	 */
	public void actualizarLimites(IRequestCycle cycle) {
		IxenetValidationDelegate delegate2 = (IxenetValidationDelegate) getBeans().
                getBean("delegate2");
		if (delegate2.getHasErrors()) {
			return;
		}
		List parametros = getSicaServiceData().findAll(ParametroSica.class);
        for (Iterator it = parametros.iterator(); it.hasNext();) {
            ParametroSica par = (ParametroSica) it.next();
            if ((ParametroSica.CAPITAL_DEL_GRUPO).equals(par.getIdParametro())) {
                par.setValor(String.valueOf(getCapitalDelGrupo()));
            }
            else if ((ParametroSica.PORCENTAJE_CAPITAL_GRUPO).equals(par.getIdParametro())) {
                par.setValor(String.valueOf(getPorcentajeCapital()));
            }
            else if ((ParametroSica.TIME_OUT_RIESGO).equals(par.getIdParametro())) {
                par.setValor(String.valueOf(getMinutosChecarLimite()));
            }
            else if ((ParametroSica.PORC_AVISO_LIMITE_REGULATORIO).equals(par.getIdParametro())) {
                par.setValor(String.valueOf(getPorcentajeAvisoGlobal()));
            }
            else if ((ParametroSica.PORC_ALARMA_LIMITE_REGULATORIO).equals(par.getIdParametro())) {
                par.setValor(String.valueOf(getPorcentajeAlarmaGlobal()));
            }
            else if ((ParametroSica.MONTO_MAXIMO_DEAL).equals(par.getIdParametro())) {
                par.setValor(String.valueOf(getMontoMaximoDeal()));
            }
            else if ((ParametroSica.NIVEL_CONFIANZA_RIESGO).equals(par.getIdParametro())) {
                par.setValor(String.valueOf(getNivelDeConfianza()));
            }
            else if ((ParametroSica.HORIZONTE_TIEMPO_RIESGO).equals(par.getIdParametro())) {
                par.setValor(String.valueOf(getHorizonteTiempo()));
            }
        }
        getSicaServiceData().update(parametros);
        setCapitalDelGrupo(Double.parseDouble((getSicaServiceData().
                findParametro(ParametroSica.CAPITAL_DEL_GRUPO)).getValor()));
        setPorcentajeCapital(Double.parseDouble((getSicaServiceData().
                findParametro(ParametroSica.PORCENTAJE_CAPITAL_GRUPO)).getValor()));
        setLimiteRegulatorio((getCapitalDelGrupo()) * (getPorcentajeCapital() / CIEN));
    }
	
	/**
	 * Activa el submit de la forma.
	 * 
	 * @param cycle El ciclo de la p&aacute;gina.
	 */
	public void formSubmit(IRequestCycle cycle) {
	}
	
	/**
	 * Realiza la b&uacute;squeda de los l&iacute;mites dado el Id de la Mesa y la Divisa.
	 * 
	 * @return List
	 */
	public List findLimitesByMesaAndDivisa() {
        MesaCambio mesaCambio = getMesaCambioSeleccionada();
        if (mesaCambio.getIdMesaCambio() == 0) {
            return getSicaServiceData().findDivisasFrecuentes();
        }
        else {    
            List posicion = getSicaServiceData().
                    findPosicionByIdMesaCambio(getMesaCambioSeleccionada().getIdMesaCambio());
            List divisasPortafolioSeleccionado = new ArrayList();
            for (Iterator it = posicion.iterator(); it.hasNext();) {
                Posicion p = (Posicion) it.next();
                divisasPortafolioSeleccionado.add(p.getDivisa());
            }
            return divisasPortafolioSeleccionado;
        }
    }

	/**
	 * Realiza la b&uacute;squeda de los l&iacute;mites dado el Id de la Mesa.
	 * 
	 * @param cycle El ciclo de la p&aacute;gina.
	 */
	public void findLimitesPorMesa(IRequestCycle cycle) {
		IxenetValidationDelegate delegate = (IxenetValidationDelegate) getBeans().
                getBean("delegate");
		if (delegate.getHasErrors()) {
			return;
		}
        Integer idMesaCambio = getMesaCambioSeleccionada().getIdMesaCambio() == 0 ? null :
                new Integer(getMesaCambioSeleccionada().getIdMesaCambio());
        setLimites(getLimiteDao().findLimiteDeRiesgoByMesaDivisa(idMesaCambio, null));
        List posicion = getSicaServiceData().
                findPosicionByIdMesaCambio(getMesaCambioSeleccionada().getIdMesaCambio());
        if (posicion.size() >= 1) {
        	setLimitesPorDivisa(getSicaServiceData().findLimiteByMesaAndDivisa(
                    getMesaCambioSeleccionada().getIdMesaCambio(),
                    ((Posicion) posicion.get(0)).getDivisa().getIdDivisa()));
        }
	}
	
	/**
	 * Actualiza los valores de los l&iacute;mites de la mesa.
	 * 
	 * @param cycle El ciclo de la p&aacute.
	 */
	public void limitesPorMesa(IRequestCycle cycle) {
		for (Iterator it = getLimites().iterator(); it.hasNext();) {
			Limite lim = (Limite) it.next();
			getSicaServiceData().update(lim. getTipoLimite());
			getSicaServiceData().update(lim);
		}
	}
	
	/**
	 * Almacena, actualiza o elimina los valores de los l&iacute;mites por Divisa.
	 * 
	 * @param cycle El ciclo de la p&aacute;gina.
	 */
	public void limitesPorDivisa(IRequestCycle cycle) {
        MesaCambio mc = getMesaCambioSeleccionada();
        if (getModo() == BORRAR) {
            List limiteDivisa = getSicaServiceData().findLimiteByMesaAndDivisa(mc.getIdMesaCambio(),
                    getDivisaSeleccionada().getIdDivisa());
            for (Iterator it = limiteDivisa.iterator(); it.hasNext();) {
                Limite limite = (Limite) it.next();
                getSicaServiceData().delete(limite);
			}
        }
		if (getModo() == ACTUALIZAR) {
			for (Iterator it = getLimitesPorDivisa().iterator(); it.hasNext();) {
				Limite lim = (Limite) it.next();
				getSicaServiceData().update(lim. getTipoLimite());
				getSicaServiceData().update(lim);
			}
        }
		if (getModo() == AGREGAR) {
			List limiteDivisa = getSicaServiceData().findLimiteByMesaAndDivisa(
                    mc.getIdMesaCambio(), getDivisaSeleccionada().getIdDivisa());
			for (Iterator it = limiteDivisa.iterator(); it.hasNext();) {
				Limite limite = (Limite) it.next();
				getSicaServiceData().delete(limite);
			}
            List tiposLimites = getSicaServiceData().findAll(TipoLimite.class);
            for (Iterator it = tiposLimites.iterator(); it.hasNext();) {
                TipoLimite tipoLimite = (TipoLimite) it.next();
                if (!(TipoLimite.RIESGO_REGULATORIO.equals(tipoLimite.getNombre()))) {
                    Limite limite = new Limite();
                    limite.setTipoLimite(tipoLimite);
                    limite.setMesaCambio(getMesaCambioSeleccionada());
                    limite.setDivisa(getDivisaSeleccionada());
                    getSicaServiceData().store(limite);
                }
            }
        }
        setLimitesPorDivisa(getLimiteDao().findLimiteDeRiesgoByMesaDivisa(
                mc.getIdMesaCambio() == 0 ? null : new Integer(mc.getIdMesaCambio()),
                getDivisaSeleccionada().getIdDivisa()));
	}
	
	/**
     * Regresa el arreglo con los estados de operaci&oacute;n normal y operaci&oacute;n restringida.
     *
     * @return int[].
     */
    protected int[] getEstadosValidos() {
        return new int[] {Estado.ESTADO_OPERACION_NORMAL, Estado.ESTADO_OPERACION_RESTRINGIDA,
        		Estado.ESTADO_OPERACION_VESPERTINA, Estado.ESTADO_OPERACION_NOCTURNA,
        		Estado.ESTADO_GENERACION_CONTABLE, Estado.ESTADO_FIN_LIQUIDACIONES};
    }

    /**
     * Regresa la referencia al bean limiteDao.
     *
     * @return LimiteDao.
     */
    private LimiteDao getLimiteDao() {
        return (LimiteDao) getApplicationContext().getBean("limiteDao");
    }
    
    /**
     * Regresa el valor de parametro Capital Del Grupo.
     *
     * @return double
     */
    public abstract double getCapitalDelGrupo();

    /**
     * Fija el valor de Capital del Grupo
     *
     * @param capitalDelGrupo La cantidad de capital del grupo.
     */
    public abstract void setCapitalDelGrupo(double capitalDelGrupo);

    /**
     * Regresa el valor de parametro Porcentaje de Capital.
     *
     * @return double
     */
    public abstract double getPorcentajeCapital();

    /**
     * Fija el valor de MontoDeal
     *
     * @param porcentajeCapital La cantidad de Porcentaje del Capital.
     */
    public abstract void setPorcentajeCapital(double porcentajeCapital);

    /**
     * Regresa el valor del Limite Regulatorio.
     *
     * @return double
     */
    public abstract double getLimiteRegulatorio();

    /**
     * Fija el valor de Limite Regulatorio
     *
     * @param limiteRegulatorio La cantidad de Limite Regulatorio.
     */
    public abstract void setLimiteRegulatorio(double limiteRegulatorio);

    /**
     * Regresa el valor de Minutos Checar Limite.
     *
     * @return int
     */
    public abstract int getMinutosChecarLimite();

    /**
     * Fija el valor de Minutos Checar Limite
     *
     * @param minutosChecarLimite La cantidad de Minutos para Checar Limite.
     */
    public abstract void setMinutosChecarLimite(int minutosChecarLimite);

    /**
     * Regresa el valor del Porcentaje Aviso Global
     *
     * @return int
     */
    public abstract int getPorcentajeAvisoGlobal();

    /**
     * Fija el valor de Porcentaje Aviso Global
     *
     * @param porcentajeAvisoGlobal La cantidad de Porcentaje Aviso Global
     */
    public abstract void setPorcentajeAvisoGlobal(int porcentajeAvisoGlobal);

    /**
     * Regresa el valor de Porcentaje Alarma Global.
     *
     * @return int
     */
    public abstract int getPorcentajeAlarmaGlobal();

    /**
     * Fija el valor de Porcentaje Alarma Global
     *
     * @param porcentajeAlarmaGlobal La cantidad de Porcentaje Alarma Global.
     */
    public abstract void setPorcentajeAlarmaGlobal(int porcentajeAlarmaGlobal);

    /**
     * Regresa el valor de Monto Maximo Deal.
     *
     * @return int
     */
    public abstract int getMontoMaximoDeal();

    /**
     * Fija el valor de Monto Maximo Deal
     *
     * @param montoMaximoDeal La cantidad de Monto Maximo Deal.
     */
    public abstract void setMontoMaximoDeal(int montoMaximoDeal);

    /**
     * Regresa el valor del Nivel de Confianza.
     *
     * @return double
     */
    public abstract double getNivelDeConfianza();

    /**
     * Fija el valor de Nivel de Confianza..
     *
     * @param nivelDeConfianza La cantidad de Nivel de Confianza.
     */

    public abstract void setNivelDeConfianza(double nivelDeConfianza);

    /**
     * Regresa el valor del Horizonte de Tiempo.
     *
     * @return double
     */
    public abstract String getHorizonteTiempo();

    /**
     * Fija el valor de Horizonte de Tiempo
     *
     * @param horizonteTiempo La cantidad de Horizonte de Tiempo.
     */
    public abstract void setHorizonteTiempo(String horizonteTiempo);

    /**
     * Regresa La Mesa de Cambio Seleccionada.
     *
     * @return MesaCambio
     */
    public abstract MesaCambio getMesaCambioSeleccionada();

    /**
     * Fija La Mesa de Cambio Seleccionada.
     *
     * @param mesaCambioSeleccionada La Mesa de Cambio Seleccionada..
     */

    public abstract void setMesaCambioSeleccionada(MesaCambio mesaCambioSeleccionada);

    /**
	 * Regresa el valores del Limites.
	 *
	 * @return List
	 */

    public abstract List getLimites();

	/**
	 * Fija el valores de Limites.
	 *
	 * @param limites El valor a asignar.
	 */
	public abstract void setLimites(List limites);

	/**
	 * Regresa el valores del Limites Por Divisa.
	 *
	 * @return List.
	 */
    public abstract List getLimitesPorDivisa();

	/**
	 * Fija el valores de Limites por Divisa.
	 *
	 * @param limitesPorDivisa El valor a asignar.
	 */
	public abstract void setLimitesPorDivisa(List limitesPorDivisa);

	/**
     * Regresa La Divisa seleccionada.
     *
     * @return Divisa
     */
    public abstract Divisa getDivisaSeleccionada();

    /**
     * Fija La Divisa Seleccionada.
     *
     * @param divisaSeleccionada La Divisa Seleccionada..
     */

    public abstract void setDivisaSeleccionada(Divisa divisaSeleccionada);

    /**
     * Regresa el valor de mesasCambio.
     *
     * @return List de objetos MesaCambio.
     */
    public abstract List getMesasCambio();

    /**
     * Establece el valor de mesasCambio.
     *
     * @param mesasCambio El valor a asignar.
     */
    public abstract void setMesasCambio(List mesasCambio);

    /**
     * Obtiene el modo para asignar tipo de evento a bot&oacute;n.
     *
     * @return int
     */
    public abstract int getModo();

    /**
     * Establece el valor de tieneLimites.
     *
     * @param tieneLimites El valor a asignar.
     */
    public abstract void setTieneLimites(boolean tieneLimites);

    /**
     * Model de Horizonte de Tiempo con los valores:
     * diario : 1 d&iacute;a
     * semanal : 5 dias
     * quincenal : 10 dias
     * mensual : 22 dias
     */
    public static final IPropertySelectionModel HORIZONTE_DE_TIEMPO_MODEL =
        new StringPropertySelectionModel(new String[] { "1", "5", "10", "22" });

    /**
     * Constante para representar evento de actualizar.
     */
    public static final int ACTUALIZAR = 2;

    /**
     * Constante para representar evento de de agregar.
     */
    public static final int AGREGAR = 3;

    /**
     * Constante para representar evento de borrar.
     */
    public static final int BORRAR = 4;

    /**
     * Constante para el valor 100.
     */
    public static final double CIEN = 100.0;
}
