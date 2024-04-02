/*
 * $Id: AutMesaCambios.java,v 1.11 2008/02/22 18:25:32 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 - 2008 LegoSoft S.C.
 */

package com.ixe.ods.sica.pages.mesa;

import com.ixe.ods.sica.Visit;
import com.ixe.ods.sica.pages.SicaPage;
import java.util.HashMap;
import java.util.Map;
import org.apache.tapestry.IRequestCycle;

/**
 * Subclase de <code>SicaPage</code>, presenta las listas de deals a autorizar o negar su
 * autorizacion, por monto y por horario.
 *
 * @author Gerardo Corzo, Jean C. Favila
 * @version  $Revision: 1.11 $ $Date: 2008/02/22 18:25:32 $
 */
public abstract class AutMesaCambios extends SicaPage {

    /**
     * Llama a <code>super.activate()</code> y a <code>crearWorkitems()</code>.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void activate(IRequestCycle cycle) {
        super.activate(cycle);
        limpiarTodo();
        Visit visit = (Visit) getVisit();
        javax.servlet.http.HttpSession ses = getRequestCycle().getRequestContext().getSession();
        ses.setAttribute("idUsuario", new Integer(((Visit) getVisit()).getUser().getIdUsuario()));
        ses.setAttribute("SICA_AUT_MESA_CAMB",
                Boolean.valueOf(visit.hasFaculty("SICA_AUT_MESA_CAMB")));
        setVentanaGrande(visit.hasFaculty("SICA_AUT_MESA_CAMB"));
        ses.setAttribute("SICA_AUT_LINCRED_EXC",
                Boolean.valueOf(visit.hasFaculty("SICA_AUT_LINCRED_EXC")));
        ses.setAttribute("SICA_AREADOC", Boolean.valueOf(visit.hasFaculty("SICA_AREADOC")));
    }

    /**
     * Regresa un mapa con la entrada widthHeight, para abrir la ventana a 1000 x 700 o 800 x 600.
     *
     * @return Map.
     */
    public Map getWidthHeightMap() {
        Map mapa = new HashMap();
        mapa.put("widthHeight", isVentanaGrande()
                ? "width=1000,height=700," : "width=800,height=600,");
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
