/*
 * $Id: ConsultaBancoInt.java,v 1.11 2008/02/22 18:25:10 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 -2007 LegoSoft S.C.
 */
package com.ixe.ods.sica.pages;

import com.ixe.isis.tapestry.IxenetValidationDelegate;
import com.ixe.ods.seguridad.model.SeguridadException;
import com.ixe.ods.sica.SicaException;
import com.ixe.treasury.dom.common.ExternalSiteException;
import com.legosoft.tapestry.model.RecordSelectionModel;
import org.apache.commons.lang.StringUtils;
import org.apache.tapestry.IExternalPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IPropertySelectionModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase para la Consulta de Bancos Internacionales.
 *
 * @author Edgar I. Leija.
 * @version $Revision: 1.11 $ $Date: 2008/02/22 18:25:10 $
 */
public abstract class ConsultaBancoInt extends SicaPage implements IExternalPage {

    /**
     * Activa el modo de operaci&oacute;n as&iacute; como el t&iacute;tulo del Portlet. Pone en
     * false la propiedad menuDisabled para que el men&uacute; sea visible por default. 
     *
     * @param cycle El objeto controlador de Tapestry que administra el ciclo de cada
     * petici&oacute;n (request) en el servidor web.
     */
    public void activate(IRequestCycle cycle) {
        super.activate(cycle);
        setListaPaises(obtenerPaises());
        setTituloActionPB("Consulta de Bancos");
        setMenuDisabled(false);
    }

    /**
     * Llama a activate() para inicializar las variables de instancia y deshabilita el men&uacute;.
     *
     * @param params El arreglo de par&aacute;metros (no utilizado).
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void activateExternalPage(Object[] params, IRequestCycle cycle) {
        activate(cycle);
        setMenuDisabled(true);
    }

    /**
     * Realiza las operaciones de b&uacute;squeda por Clientes, por Empresas o por Contratos Sica
     * para los Canales de Sucursales y Mayoreo. Para este &uacute;ltimo Canal, hay dos casos;
     * cuando el ejecutivo que opera el SICA quiere consultar sus Cuentas Propias Asignadas y el
     * caso para cuando requiere consultar adem&aacute;s los Contratos Sica de sus Subordinados.
     *
     * @param cycle El objeto controlador de Tapestry que administra el ciclo de cada
     * petici&oacute;n (request) en el servidor web.
     */
    public void fetch(IRequestCycle cycle) {
        IxenetValidationDelegate delegate = (IxenetValidationDelegate) getBeans().
                getBean("delegate");
        convertirAUpperCase();
        try {
            // Validaciones de Campos:
            if ((getNombreBanco()).indexOf("%") >= 0) {
                throw new SicaException("No se permite utilizar el caracter '%' en los criterios "
                        + "de consulta.");
            }
            if (StringUtils.isEmpty(getNombreBanco().trim())) {
                throw new SicaException("Debe ingresar al menos 3 caracteres del nombre "
                        + "del banco.");
            }
            else if (StringUtils.isNotEmpty(getNombreBanco()) && getNombreBanco().length() < 3) {
                throw new SicaException("Los criterios de b\u00fasqueda deben tener m\u00e1s de 2"
                        + " caracteres.");
            }
            // Logica de Negocio
            List bancosInternacionales = getSicaServiceData().findBancoInternacionalByNombreAndPais(
                    getNombreBanco().trim(), getPaisSeleccionado().getClaveISO());
            setBancos(bancosInternacionales);
            if (bancosInternacionales.isEmpty()) {
                throw new SicaException("Los criterios de b\u00fasqueda especificados no "
                        + "arrojaron resultados.");
            }
        }
        catch (SicaException e) {
            if (_logger.isDebugEnabled()) {
                _logger.debug(e);
            }
            delegate.record(e.getMessage(), null);
        }
    }

    /**
     * Convierte lo escrito en los Criterios de Busqueda a Uppercase para evitar fallas en las
     * busquedas de la Base de Datos.
     */
    public void convertirAUpperCase() {
        if (StringUtils.isNotEmpty(getNombreBanco())) {
            setNombreBanco(getNombreBanco().toUpperCase());
        }
    }

    /**
     * Obtiene de la base de datos la lista de paises
     *
     * @return List Paises
     */
    public List obtenerPaises() {
        try {
            return getSiteService().getPaises(getTicket());
        }
        catch (ExternalSiteException e) {
            if (_logger.isDebugEnabled()) {
                    _logger.debug(e);
            }
        }
        catch (SeguridadException e) {
            if (_logger.isDebugEnabled()) {
                    _logger.debug(e);
            }
        }
        return new ArrayList();
    }

    /**
     * Modelo para el combo de paises
     *
     * @return RecordSelectionModel
     */
    public IPropertySelectionModel getPaisesModel() {
        return new RecordSelectionModel(getListaPaises(), "claveISO", "nombrePais");
    }

    /**
     * Asigna la lista de paises
     *
     * @param listaPaises Lista de paises
     */
    public abstract void setListaPaises(List listaPaises);

    /**
     * Obtiene la lista de paises
     *
     * @return List
     */
    public abstract List getListaPaises();

    /**
     * Asigna el valor del pais seleccionado
     *
     * @param paisSeleccionado El nombre del pais
     */
    public abstract void setPaisSeleccionado(com.ixe.treasury.dom.common.Pais paisSeleccionado);

    /**
     * Obtiene el valor del pais seleccionado
     *
     * @return TesPais
     */
    public abstract com.ixe.treasury.dom.common.Pais getPaisSeleccionado();

    /**
     * Establece el T&iacute;tulo del Portlet de B&uacute;squedas de la P&aacute;gina de
     * acuerdo al modo de operaci&oacute;n de la misma.
     *
     * @param tituloActionPB El T&iacute;tulo del Portlet de B&uacute;squedas de la P&aacute;gina.
     */
    public abstract void setTituloActionPB(String tituloActionPB);

    /**
     * Obtiene lo establecido como criterio de b&uacute;squeda Nombre.
     *
     * @return String NombreBanco. Regresa el nombre del Banco.
     */
    public abstract String getNombreBanco();

    /**
     * Fija el valor de nombreBanco.
     *
     * @param nombreBanco El nombre del banco a asignar.
     */
    public abstract void setNombreBanco(String nombreBanco);

    /**
     * Regresa el valor de bancos.
     *
     * @return Una <code>List</code> que contiene los resultados de la busqueda.
     */
    public abstract List getBancos();

    /**
     * Fija el valor mediante una lista que contiene bancos
     *
     * @param bancos Un <code>List</code> de bancos que se van a signar.
     */
    public abstract void setBancos(List bancos);

    /**
     * Establece el valor de la propiedad menuDisabled.
     *
     * @param menuDisabled El valor a asignar.
     */
    public abstract void setMenuDisabled(boolean menuDisabled);
}
