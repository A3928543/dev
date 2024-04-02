package com.ixe.ods.sica.pages;

import java.util.HashMap;
import java.util.Map;

import org.apache.tapestry.IRequestCycle;

import com.ixe.ods.sica.Visit;

/**
 * Subclase de <code>SicaPage</code>, presenta la informaci&oacute;n 
 * de los clientes de un promotor para efectos de negociaci&oacute;n
 *
 * @author Efren Trinidad, Na-at Technologies
 * @version  $Revision: 1.1.2.2 $ $Date: 2012/09/01 21:53:22 $
 */
public abstract class InformacionNegociacion extends SicaPage {

    /**
     * Abre la pantalla de informaci&oacute;n de negociaci&oacute;n
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void activate(IRequestCycle cycle) {
        super.activate(cycle);
        limpiarTodo();
        Visit visit = (Visit) getVisit();
        javax.servlet.http.HttpSession ses = getRequestCycle().getRequestContext().getSession();
        //Se envia como parametro el id del promotor en sesion
        ses.setAttribute("idPersona", ((Visit) getVisit()).getUser().getPersona().getIdPersona());
        setVentanaGrande(true);
    }

    /**
     * Regresa un mapa con la entrada widthHeight, para abrir la ventana a 1000 x 700 o 800 x 600.
     *
     * @return Map.
     */
    public Map getWidthHeightMap() {
        Map mapa = new HashMap();
        mapa.put("widthHeight", isVentanaGrande()
                ? "width=1000, height=600," : "width=800, height=600,");
        return mapa;
    }

    /**
     * Establece el valor de ventanaGrande.
     *
     * @param ventanaGrande El valor a asignar.
     */
    public abstract void setVentanaGrande(boolean ventanaGrande);

    /**
     * Regresa el valor de ventanaGrande.
     *
     * @return boolean.
     */
    public abstract boolean isVentanaGrande();
}
