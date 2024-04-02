/*
 * $Id: EventosSelectionModel.java,v 1.11 2008/02/22 18:25:35 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright © 2005 LegoSoft S.C.
 */

package com.ixe.ods.sica.components;

import com.ixe.ods.sica.model.Deal;
import org.apache.tapestry.form.IPropertySelectionModel;

/**
 * <p>IPropertySelectionModel para desplegar opciones de los status de los eventos un deal para
 * poder realizar consultas de deals.</p>
 *
 * <p>Se definen 4 configuraciones por default: documentaci&oacute;n, cancelaci&oacute;n, l&inea de
 * cr&eacute;dito y l&iacute;nea de operaci&oacute;n.</p>
 *
 * @author Jean C. Favila
 * @version $Revision: 1.11 $ $Date: 2008/02/22 18:25:35 $
 */
public class EventosSelectionModel implements IPropertySelectionModel {

    /**
     * Constructor por default. Inicia los tipos de status a desplegar en el combo box.
     *
     * @param eventos El arreglo de status.
     */
    public EventosSelectionModel(String[] eventos) {
        super();
        _eventos = eventos;
    }

    /**
     * Regresa una instancia de EventosSelectionModel con las opciones: TODOS, NORMAL, SOLICITUD,
     * APROBACION y NEGACION.
     *
     * @return EventosSelectionModel.
     */
    public static IPropertySelectionModel getDocumentacionSelModel() {
        return new EventosSelectionModel(new String[] { "_", Deal.EV_NORMAL, Deal.EV_SOLICITUD,
                Deal.EV_APROBACION, Deal.EV_NEGACION });
    }

    /**
     * Regresa una instancia de EventosSelectionModel con las opciones: TODOS, NORMAL, SOLICITUD,
     * APROBACION y NEGACION.
     *
     * @return EventosSelectionModel.
     */
    public static IPropertySelectionModel getCancelacionSelModel() {
        return new EventosSelectionModel(new String[] { "_", Deal.EV_NORMAL, Deal.EV_SOLICITUD,
                Deal.EV_APROBACION_TESORERIA, Deal.EV_APROBACION, Deal.EV_NEGACION_TESORERIA,
                Deal.EV_NEGACION, Deal.EV_CANCELACION });
    }

    /**
     * Regresa una instancia de EventosSelectionModel con las opciones: TODOS, NORMAL, SOLICITUD,
     * APROBACION, NEGACION y CANCELACION.
     *
     * @return EventosSelectionModel.
     */
    public static IPropertySelectionModel getLineaCreditoSelModel() {
        return new EventosSelectionModel(new String[] { "_", Deal.EV_NORMAL, Deal.EV_SOLICITUD,
                Deal.EV_APROBACION, Deal.EV_NEGACION, Deal.EV_CANCELACION });
    }

    /**
     * Regresa una instancia de EventosSelectionModel con las opciones: TODOS, NORMAL, SOLICITUD,
     * APROBACION, NEGACION y CANCELACION.
     *
     * @return EventosSelectionModel.
     */
    public static IPropertySelectionModel getLineaOperacionSelModel() {
        return new EventosSelectionModel(new String[] { "_", Deal.EV_NORMAL, Deal.EV_SOLICITUD,
                Deal.EV_APROBACION, Deal.EV_NEGACION, Deal.EV_CANCELACION });
    }

    /**
     * Regresa la longitud del arreglo de eventos.
     *
     * @return int.
     */
    public int getOptionCount() {
        return _eventos.length;
    }

    /**
     * Regresa el objeto que se encuentra en el &i&acute;ndice i del arreglo de eventos.
     *
     * @param i El indice a buscar.
     * @return Object.
     */
    public Object getOption(int i) {
        return _eventos[i];
    }

    /**
     * Regresa una etiqueta adecuada para el status del evento en el &iacute;ndice i.
     *
     * @param i El &iacute;ndice a buscar.
     * @return String.
     */
    public String getLabel(int i) {
        if (Deal.EV_APROBACION.equals(_eventos[i])) {
            return "APROBADO";
        }
        else if (Deal.EV_APROBACION_TESORERIA.equals(_eventos[i])) {
            return "APROBADO TESORERIA";
        }
        else if (Deal.EV_CANCELACION.equals(_eventos[i])) {
            return "CANCELADO";
        }
        else if (Deal.EV_ENVIAR.equals(_eventos[i])) {
            return "ENVIAR";
        }
        else if (Deal.EV_NEGACION.equals(_eventos[i])) {
            return "NEGADO";
        }
        else if (Deal.EV_NEGACION_TESORERIA.equals(_eventos[i])) {
            return "NEGADO TESORERIA";
        }
        else if (Deal.EV_NORMAL.equals(_eventos[i])) {
            return "NORMAL";
        }
        else if ("_".equals(_eventos[i])) {
            return "CUALQUIERA";
        }
        else if (Deal.EV_SOLICITUD.equals(_eventos[i])) {
            return "SOLICITUD";
        }
        else if (Deal.EV_USO.equals(_eventos[i])) {
            return "USO";
        }
        return null;
    }

    /**
     * Regresa el objeto que se encuentra en el &iacute;ndice i del arreglo de eventos.
     *
     * @param i El &iacute;ndice a buscar.
     * @return String.
     */
    public String getValue(int i) {
        return _eventos[i];
    }

    /**
     * Regresa el mismo par&aacute;metro que recibe.
     *
     * @param string El valor a traducir.
     * @return Object.
     */
    public Object translateValue(String string) {
        return string;
    }

    /**
     * El arreglo de posibles status para el evento deseado.
     */
    private String[] _eventos;
}
