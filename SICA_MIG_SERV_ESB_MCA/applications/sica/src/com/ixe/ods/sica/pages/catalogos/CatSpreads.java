/*
 * $Id: CatSpreads.java,v 1.10 2008/02/22 18:25:39 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2004 - 2008 LegoSoft S.C.
 */

package com.ixe.ods.sica.pages.catalogos;

import java.util.List;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.valid.IValidationDelegate;

import com.ixe.ods.sica.model.Divisa;
import com.ixe.ods.sica.model.Spread;
import com.ixe.ods.sica.model.SpreadActual;
import com.ixe.ods.sica.model.TipoPizarron;
import com.legosoft.tapestry.model.RecordSelectionModel;

/**
 * Permite visualizar los spreads actuales e insertar un nuevo spread.
 *
 * @author Jean C. Favila
 * @version  $Revision: 1.10 $ $Date: 2008/02/22 18:25:39 $
 */
public abstract class CatSpreads extends AbstractCatConsulta {

	/**
	 * Asigna los valores necesarios para la pagina. Obtiene de la base de datos,
	 * los tipos de pizarron dados de alta.
	 *
	 * @param cycle El ciclo de la pagina.
	 */
	public void activate(IRequestCycle cycle) {
		setTiposPizarron(getSicaServiceData().findAll(TipoPizarron.class));
    	TipoPizarron primerElemento = new TipoPizarron();
    	primerElemento.setIdTipoPizarron(new Integer(-1));
    	primerElemento.setDescripcion("Seleccione un Tipo de Pizarr\u00f3n");
    	getTiposPizarron().add(0, primerElemento);
		super.activate(cycle);
	}

    /**
     * Asigna la lista de spreads a desplegar, mostrando &uacute;nicamente los spreads actuales.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     * @see com.ixe.ods.sica.sdo.SicaServiceData#findSpreadsActualesByMesaCanalDivisa(int, String,
            String).
     */
	public void listarSpreads(IRequestCycle cycle) {
		IValidationDelegate delegate = (IValidationDelegate) getBeans().getBean("delegate");
		if (getTipoPizarron().getIdTipoPizarron().intValue() < 1) {
            delegate.record("Debe seleccionar un tipo de Pizarr\u00f3n para la busqueda del " +
                    "Spread.", null);
        }
		else {
			List spreads = getSicaServiceData().findSpreadsActualesByTipoPizarronDivisa(
					getTipoPizarron(), getDivisaSeleccionada().getIdDivisa());
			if (spreads.isEmpty()) {
                delegate.record("No hay Spreads para el Pizarron seleccionado.", null);
            }
			else {
				setSpreads(spreads);
			}
		}
	}

	/**
	 * Inicializa un nuevo objeto Spread y redirige a la p&aacute;gina.
	 *
	 * @param cycle El ciclo de la p&aacute;gina.
	 */
    public void insertar(IRequestCycle cycle) {
        IValidationDelegate delegate = (IValidationDelegate) getBeans().getBean("delegate");
        if (getTipoPizarron().getIdTipoPizarron().intValue() > 0) {
            super.insertar(cycle);
            Spread r = (Spread) getPaginaEdicion().getRegistro();
            r.setDivisa(getDivisaSeleccionada());
            r.setTipoPizarron(getTipoPizarron());
        }
        else {
            delegate.record("Debe seleccionar un tipo de Pizarr\u00f3n para el Spread.", null);
        }
    }

    /**
     * Elimina el spread seleccionado.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void eliminar(IRequestCycle cycle) {
        SpreadActual sa = getSicaServiceData().findSpreadActual(
                new Integer(cycle.getServiceParameters()[0].toString()));
        if (sa != null) {
            getSicaServiceData().delete(sa);
        }
    }

    /**
     * Modelo para la lista de Tipos de Pizarron.
     *
     * @return RecordSelectionModel
     */
    public IPropertySelectionModel getTiposPizarronModel() {
    	return new RecordSelectionModel(getTiposPizarron(), "idTipoPizarron", "descripcion");
    }

    /**
     * Regresa el valor de divisaSeleccionada.
     *
     * @return Divisa.
     */
    public abstract Divisa getDivisaSeleccionada();

    /**
     * Regresa el valor de spreads.
     *
     * @return List.
     */
    public abstract List getSpreads();

    /**
     * Asigna el valor para spreads.
     *
     * @param spreads El valor para spreads.
     */
    public abstract void setSpreads(List spreads);

    /**
     * Regresa el valor de TipoPizarron.
     *
     * @return TipoPizarron.
     */
    public abstract TipoPizarron getTipoPizarron();

    /**
     * Asigna el valor para TipoPizarron.
     *
     * @param tipoPizarron El valor para tipoPizarron
     */
    public abstract void setTipoPizarron(TipoPizarron tipoPizarron);

    /**
     * Regresa el valor de tiposPizarron.
     *
     * @return List.
     */
    public abstract List getTiposPizarron();

    /**
     * Asigna el valor para tiposPizarron.
     *
     * @param tiposPizarron El valor para tiposPizarron.
     */
    public abstract void setTiposPizarron(List tiposPizarron);
}
