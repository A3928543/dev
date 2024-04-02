/*
 * $Id: EncabezadoDeal.java,v 1.14 2009/08/03 21:58:38 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 - 2009 LegoSoft S.C.
 */

package com.ixe.ods.sica.components;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry.BaseComponent;

import com.ixe.ods.bup.model.Direccion;
import com.ixe.ods.sica.Global;
import com.ixe.ods.sica.model.Deal;
import com.ixe.ods.sica.sdo.DealServiceData;

/**
 * Componente que despliega los datos del encabezado de un deal normal. Declara constantes para los
 * distintos tipos de submit disponibles.
 *
 * @author Jean C. Favila
 * @version  $Revision: 1.14 $ $Date: 2009/08/03 21:58:38 $
 */
public abstract class EncabezadoDeal extends BaseComponent {

    /**
     * Regresa la direcci&oacute;n de mensajer&iacute;a ligada al deal.
     *
     * @return Direccion.
     */
    public Direccion getDireccionMensajeria() {
        DealServiceData dsd = (DealServiceData) ((Global) getPage().getGlobal()).
                getApplicationContext().getBean("dealServiceData");
        return dsd.findDireccionMensajeriaDeal(getDeal());
    }

    /**
     * Regresa la concatencaci&oacute;n de correos de facturaci&oacute;n electr&oacute;nica,
     * separados por ';'.
     *
     * @return String.
     */
    public String getEmailsFactura() {
        Deal deal = getDeal();
        StringBuffer sb = new StringBuffer(StringUtils.isEmpty(deal.getEmailFactura()) ?
                "" : deal.getEmailFactura().replaceAll("\\;", "; "));
        if (!StringUtils.isEmpty(deal.getEmailFacturaOtro())) {
            if (!StringUtils.isEmpty(sb.toString())) {
                sb = sb.append("; ");
            }
            sb = sb.append(deal.getEmailFacturaOtro().replaceAll("\\;", "; "));
        }
        return sb.toString();
    }

    /**
     * Regresa el valor de la propiedad deal.
     *
     * @return Deal.
     */
    public abstract Deal getDeal();

    /**
     * Constante para el modo de submit de factura.
     */
    public static final String SUBMIT_MODE_FACTURA = "factura";

    /**
     * Constante para el modo de submit de pago anticipado.
     */
    public static final String SUBMIT_MODE_PAGO_ANTICIPADO = "pagoAnticipado";

    /**
     * Constante para el modo de submit de mensajeria.
     */
    public static final String SUBMIT_MODE_MENSAJERIA = "mensajeria";

    /**
     * Constante para el modo de submit de salvar.
     */
    public static final String SUBMIT_MODE_SALVAR = "salvar";

    /**
     * Constante para el modo de submit de procesar.
     */
    public static final String SUBMIT_MODE_PROCESAR = "procesar";

    /**
     * Constante para el modo de submit de toma en firme.
     */
    public static final String SUBMIT_MODE_TOMA_EN_FIRME = "tomaEnFirme";

    /**
     * Constante para el modo de submit de cancelar.
     */
    public static final String SUBMIT_MODE_CANCELAR = "cancelar";

    /**
     * Constante para el modo de submit de enviar al cliente.
     */
    public static final String SUBMIT_MODE_ENVIAR_CLIENTE = "enviarAlCliente";
}