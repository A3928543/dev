/*
 * $Id: Configuraciones.java,v 1.14.24.1.46.2 2016/08/17 15:45:43 mejiar Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2006 - 2009 LegoSoft S.C.
 */
package com.ixe.ods.sica.pages.administracion;

import com.ixe.ods.sica.PlantillaPantallaCache;
import com.ixe.ods.sica.SicaSiteCache;
import com.ixe.ods.sica.model.Estado;
import com.ixe.ods.sica.model.ParametroSica;
import com.ixe.ods.sica.pages.SicaPage;
import com.ixe.ods.sica.services.ValorFuturoService;
import com.ixe.ods.sica.utils.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.form.StringPropertySelectionModel;

/**
 * Clase para Administrar diversas configuraciones del SICA.
 *
 * @author Javier Cordova, Jean C. Favila, Israel Rebollar.
 */
public abstract class Configuraciones extends SicaPage {

    /**
     * Llama a <code>cartaListaParametros()</code>
     * 
     * @param cycle El IRequestCycle.
     */
	public void activate(IRequestCycle cycle) {
		super.activate(getRequestCycle());
		cargaListaParametros();
	}

    /**
     * Limpia la variable global que contiene las formas de liquidaci&oacute;n v&aacute;lidas para
     * el sica para que posteriormente se refresque.
     *
     * @param cycle El IRequestCycle.
     */
    public void limpiarSiteSicaCache(IRequestCycle cycle) {
        setOperacionExitosa(false);
        try {
            SicaSiteCache ssc = getSicaSiteCache();
            ssc.refrescar(getTicket());
            ((PlantillaPantallaCache) getApplicationContext().getBean("plantillaPantallaCache")).
                    limpiar();
            setOperacionExitosa(true);
            getDelegate().record("Se limpi\u00f3 con \u00e9xito el Cach\u00e9 de las Formas de " +
                    "Liquidaci\u00f3n v\u00e1lidas para el Sica.", null);
        }
        catch (Exception e) {
            warn(e.getMessage(), e);
            getDelegate().record("No se pudo realizar la operaci\u00f3n. Favor de intentar de " +
                    "nuevo.", null);
        }
    }

    /**
     * Limpia el cach&eacute; de fechas inh&aacute;biles en Estados Unidos del bean
     * ValorFuturoService.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     * @see com.ixe.ods.sica.services.ValorFuturoService#limpiarCache().
     */
    public void limpiarCacheFechasInhabilesEua(IRequestCycle cycle) {
        try {
            ValorFuturoService vfService = (ValorFuturoService) getApplicationContext().
                    getBean("valorFuturoService");
            vfService.limpiarCache();
            setOperacionExitosa(true);
            getDelegate().record("Se limpi\u00f3 con \u00e9xito el Cach\u00e9 de Fechas " +
                    "inh\u00e1biles en EUA.", null);
        }
        catch (Exception e) {
            setOperacionExitosa(false);
            warn(e.getMessage(), e);
            getDelegate().record(e.getMessage(), null);
        }
    }

    /**
     * Inicializa la lista de par&aacute;metros.
     */
    private void cargaListaParametros() {
        //List parametros = getSicaServiceData().findAll(ParametroSica.class);
        List parametros = getSicaServiceData().findAllParametrosSica();
        setParametros(parametros);
    }

    /**
     * Regresa un StringPropertySelectionModel con los valores 'c' y 'n', se utiliza para el combo
     * de la captura de par&aacute;metros.
     *
     * @return StringPropertySelectionModel.
     */
    public IPropertySelectionModel getModeloTipoValor() {
        return new StringPropertySelectionModel(new String[]{"c", "n"});
    }

    /**
     * Permite Iniciar Manualmente el D&iacute;a en el Sistema y establecer la Fecha del mismo.
     *
     * @param cycle El IRequestCycle.
     */
    public void iniciarDia(IRequestCycle cycle) {
        setOperacionExitosa(false);
        try {
            Estado actual = getSicaServiceData().findEstadoSistemaActual();

            if (Estado.ESTADO_INICIO_DIA == actual.getIdEstado()) {
                // Se cambia el estado actual del sistema a operacion normal:
                actual.setActual(false);
                getSicaServiceData().update(actual);
                actual.getSiguienteEstado().setActual(true);
                actual.getSiguienteEstado().setHoraInicio(HOUR_FORMAT.format(new Date()));
                getSicaServiceData().update(actual.getSiguienteEstado());
                // Se actualiza la fecha del sistema a hoy:
                ParametroSica paramSica = getSicaServiceData().findParametro(ParametroSica.
                        FECHA_SISTEMA);
                SimpleDateFormat DATE_FORMAT_V = new SimpleDateFormat("dd/MM/yyyy");
                paramSica.setValor(DATE_FORMAT_V.format(new Date()));
                getSicaServiceData().update(paramSica);
                setOperacionExitosa(true);
                getDelegate().record("Se realiz\u00f3 con \u00e9xito el cambio de Estado de " +
                        "Inicio de D\u00eda a Operaci\u00f3n Normal.", null);
            }
            else {
                getDelegate().record("El Estado Actual no es Inicio de D\u00eda. No se pudo " +
                        "realizar la operaci\u00f3n.", null);
            }
        }
        catch (Exception e) {
            warn(e.getMessage(), e);
            getDelegate().record("No se pudo realizar la operaci\u00f3n. Favor de intentar de " +
                    "nuevo.", null);
        }
    }
    
    /**
     * Permite realizar la actualizaci&oacute;n de los par&aacute;metros de la aplicaci&oacute;n
     * SICA.
     *
     * @param cycle El IRequestCycle.
     */
    public void actualizarParametros(IRequestCycle cycle) {
        try {
            getSicaServiceData().update(getParametros());
            debug("----actualizando lista de paremtros en cache");
            cargaListaParametros();
        }
        catch (Exception e) {
            warn(e.getMessage(), e);
            getDelegate().record("No se pudo realizar la operaci\u00f3n. Favor de intentar de " +
                    "nuevo.", null);
        }
    }

    /**
     * Permite insertar un nuevo registro dentro de la tabla de
     * par&aacute;metros de la aplicaci&oacute;n SICA.
     *
     * @param cycle El IRequestCycle.
     */
    public void insertaParametro(IRequestCycle cycle) {
        if (getDelegate().getHasErrors()) {
            return;
        }
        getSicaServiceData().store(getNuevoParametro());
        setNuevoParametro(new ParametroSica());
        cargaListaParametros();
    }

    /**
     * Regresa por default el estado de operacion normal, restringida y vespertina. Las subclases
     * deben sobre escribir para indicar estados adicionales en los que &eacute;stas son
     * v&aacute;lidas.
     *
     * @return int[].
     */
    protected int[] getEstadosValidos() {
        return new int[]{Estado.ESTADO_OPERACION_NORMAL, Estado.ESTADO_OPERACION_RESTRINGIDA,
                Estado.ESTADO_OPERACION_VESPERTINA, Estado.ESTADO_FIN_LIQUIDACIONES,
                Estado.ESTADO_GENERACION_CONTABLE, Estado.ESTADO_INICIO_DIA};
    }

    /**
     * Obtiene una referencia al Bean que mantiene un 'cach&eacute; de las formas de
     * liquidaci&oacute;n' v&aacute;lidas para el sica.
     *
     * @return SicaSiteCache
     */
    private SicaSiteCache getSicaSiteCache() {
        return (SicaSiteCache) getApplicationContext().getBean("sicaSiteCache");
    }

    /**
     * Fija la bandera que le indica al usuario a trav&eacute;s de un mensaje en pantalla
     * si su operaci&oacute;n fue exitosa o no.
     *
     * @param operacionExitosa El valor a asignar.
     */
    public abstract void setOperacionExitosa(boolean operacionExitosa);


    /**
     * Fija la lista de per&aacute;metros de configuraci&ooacute;n del sistema SICA
     * si su operaci&oacute;n fue exitosa o no.
     *
     * @param parametros de par&aacute;metros.
     */
    public abstract void setParametros(List parametros);


    /**
     * Obtiene la Lista de Par&aacute;metros de configuraci&oacute;n para el sistema SICA
     *
     * @return List
     */
    public abstract List getParametros();

    /**
     * Fija la propiedad parametro de ParametroSica
     *
     * @param parametro La propiedad parametro
     */
    public abstract void setNuevoParametro(ParametroSica parametro);

    /**
     * Obtiene el valor de la propiedad parametro de ParametroSica 
     *
     * @return String Valor propiedad parametro
     */
    public abstract ParametroSica getNuevoParametro();
}
