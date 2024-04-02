/*
 * $Id: Visit.java,v 1.14 2010/04/30 16:29:22 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 - 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRender;
import org.apache.tapestry.IRequestCycle;

import com.ixe.ods.seguridad.model.IUsuario;
import com.ixe.ods.sica.model.GrupoTrabajo;
import com.ixe.ods.tapestry.BaseVisit;
import com.ixe.ods.tapestry.components.menu.IMenu;
import com.ixe.ods.tapestry.components.menu.IMenuDelegate;
import com.ixe.ods.tapestry.components.menu.IMenuListener;
import com.ixe.ods.tapestry.components.menu.SoThinkMenuFactory;
import com.legosoft.tapestry.IActivate;
import com.legosoft.tapestry.browsersniffer.BrowserInfo;

/**
 * Subclase de BaseVisit. Funciona como objeto Visit para la aplicaci&oacute;n del SICA.
 *
 * @author Jean C. Favila, Javier Cordova (jcordova)
 * @version  $Revision: 1.14 $ $Date: 2010/04/30 16:29:22 $
 */
public class Visit extends BaseVisit
        implements Serializable, IMenuDelegate, IMenuListener, IRender {

    /**
     * Constructor por default.
     */
    public Visit() {
        super();
    }

    /**
     * Regresa el valor de descargando.
     *
     * @return boolean.
     */
    public boolean isDescargando() {
        return descargando;
    }

    /**
     * Establece el valor de descargando.
     *
     * @param descargando El valor a asignar.
     */
    public void setDescargando(boolean descargando) {
        this.descargando = descargando;
    }

    /**
     * Regresa el usuario del sistema a nivel sesi&oacute;n.
     *
     * @return el usuario del sistema.
     */
    public IUsuario getUser() {
        return this.user;
    }

    /**
     * Establece el usuario del sistema a nivel sesi&oacute;n.
     *
     * @param user - el usuario del sistema.
     */
    public void setUser(IUsuario user) {
        this.user = user;
    }

    /**
     * Obtiene la informacion del Browser.
     * 
     * @return La informacion del Browser.
     */
    public BrowserInfo getBrowserInfo() {
        return browserInfo;
    }

    /**
     * Establece la informacion del Browser.
     * 
     * @param browserInfo La informacion del Browser.
     */
    public void setBrowserInfo(BrowserInfo browserInfo) {
        this.browserInfo = browserInfo;
    }

    /**
     * Verifica si la facultad del rol es v&aacute;lida o no.
     *
     * @param faculty - el String facultad a verificar.
     * @return boolean si la facultad es v&aacute;lida o no.
     */
    public boolean hasFaculty(String faculty) {
        return hasPermission(faculty);
    }

    /**
     * Regresa las propiedades del Menu.
     *
     * @return IMenu Las propiedades del Menu.
     */
    public IMenu getMenu() {
        if (menu == null) {
            SoThinkMenuFactory menuFactory = new SoThinkMenuFactory();
            menu = menuFactory.getMenuFromResource("com/ixe/ods/sica/sica-menu.xml");
            menu.setDelegate(this);
        }
        return menu;
    }

    /**
     * Convinience method to use from an OGNL expression.
     *
     * @return IMenuListener
     */
    public IMenuListener getMenuListener() {
        return this;
    }

    /**
     * Agrega el script para cargar el so think menu.
     *
     * @param writer El IMarkupWriter
     * @param cycle El IRequestCycle
     */
    public void render(IMarkupWriter writer, IRequestCycle cycle) {
        writer.begin("script");
        writer.attribute("src", "scripts/sothink/stm31.js");
        writer.attribute("type", "text/javascript");
        writer.end("script");
    }

    /**
     * Distribuidor de todas las p&aacute;ginas.
     *
     * @param cycle - el IRequestCycle
     * @param menuName - el String nombre del menu seleccionado
     * @param permission - el String permiso para accesar la p&aacute;gina deseada.
     * @param data String - el String p&aacute;gina deseada
     */
    public void menuItemSelected(IRequestCycle cycle, String menuName, String permission,
                                 String data) {
        if (StringUtils.isNotEmpty(data)) {
            IActivate nextPage = (IActivate) cycle.getPage(data);

            if (nextPage != null) {
                nextPage.activate(cycle);
            }
        }
    }
    
    /**
     * Obtiene el ID de la Mesa de Cambio del Canal seleccionado del Usuario Loggeado al sistema.
     * 
     * @return El ID de la Mesa.
     */
    public int getIdMesaCambio() {
        return idMesaCambio;
    }

    /**
     * Establece el ID de la Mesa de Cambio del Usuario Loggeado al sistema.
     * 
     * @param idMesaCambio El ID de la Mesa.
     */
    public void setIdMesaCambio(int idMesaCambio) {
        this.idMesaCambio = idMesaCambio;
    }

    /**
     * Obtiene el ID del Canal seleccionado del Usuario Loggeado al sistema.
     * 
     * @return El ID del Canal.
     */
    public String getIdCanal() {
        return idCanal;
    }

    /**
     * Establece el ID del Canal del Usuario Loggeado al sistema.
     * 
     * @param idCanal El ID del Canal.
     */
    public void setIdCanal(String idCanal) {
        this.idCanal = idCanal;
    }

    /**
     * Obtiene la lista de Canales del Usuario Loggeado al sistema.
     * 
     * @return List Los Canales.
     */
    public List getCanales() {
        return canales;
    }

    /**
     * Establece la lista de Canales del Usuario Loggeado al sistema.
     * 
     * @param canales Los Canales.
     */
    public void setCanales(List canales) {
        this.canales = canales;
    }

    /**
     * Regresa true si el usuario tiene la facultad SICA_MOSTRAR_PIZARRA.
     *
     * @return boolean.
     */
    public boolean isMostrarPizarron() {
        return hasFaculty(FacultySystem.SICA_MOSTRAR_PIZARRA);
    }

    /**
     * Regresa true si el usuario tiene la facultad SICA_GUARDIA.
     *
     * @return boolean.
     */
    public boolean isGuardia() {
        return hasFaculty(FacultySystem.SICA_GUARDIA);
    }

    /**
     * Regresa true, si el usuario tiene la facultad del canal de ixe directo.
     *
     * @return boolean.
     */
    public boolean isIxeDirecto() {
        return hasFaculty(FacultySystem.SICA_CAN_IXEDIRECTO);
    }

    /**
	 * @return the GrupoTrabajo
	 */
	public GrupoTrabajo getGrupoTrabajo() {
		return grupoTrabajo;
	}

	/**
	 * @param idGrupoTrabajo the idGrupoTrabajo to set
	 */
	public void setGrupoTrabajo(GrupoTrabajo grupoTrabajo) {
		this.grupoTrabajo = grupoTrabajo;
	}

	/**
     * Bandera para indicar si el usuario est&aacute; o no descargando alg&uacute;n archivo PDF o
     * Excel.
     */
    private boolean descargando;

    /**
     * La informacion del Browser.
     */
    private BrowserInfo browserInfo;

    /**
     * Las propiedades del Menu.
     */
    private IMenu menu;

    /**
     * El ID de la Mesa de Cambio.
     */
    private int idMesaCambio;

    /**
     * El ID del Canal.
     */
    private String idCanal;
    
    /**
     * Los Canales de las Mesas que puede ver el Usuario.
     */
    private List canales;

    /**
     * El usuario del sistema.
     */
    private IUsuario user;
    
    /**
     * El GrupoTrabajo del grupo de trabajo al que pertenece.
     */
    private GrupoTrabajo grupoTrabajo;

    /**
     * El UID para serializaci&oacute;n.
     */
    private static final long serialVersionUID = 4440846716177056519L;
}
