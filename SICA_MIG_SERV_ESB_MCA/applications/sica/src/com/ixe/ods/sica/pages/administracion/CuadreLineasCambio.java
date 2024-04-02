/*
 * $Id: CuadreLineasCambio.java,v 1.3.68.1 2016/08/17 15:45:43 mejiar Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2009 - 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica.pages.administracion;

import java.util.List;

import org.apache.tapestry.IRequestCycle;

import com.ixe.ods.sica.dao.LineaCambioDao;
import com.ixe.ods.sica.pages.SicaPage;

/**
 * P&aacute;gina que permite al administrador del sistema recalcular los usos de todas las
 * l&iacute;neas de cambio.
 *
 * @author Jean C. Favila (jfavila)
 * @version $Revision: 1.3.68.1 $ $Date: 2016/08/17 15:45:43 $
 */
public abstract class CuadreLineasCambio extends SicaPage {

    /**
     * Ejecuta el proceso que reinicializa los usos de todas las l&iacute;neas de cr&eacute;dito y
     * los recalcula.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void cuadrarLineasCambio(IRequestCycle cycle) {
        /*try {
            LineaCambioDao lineaCambioDao = (LineaCambioDao) getApplicationContext().
                    getBean("lineaCambioDao");
            setResultados(lineaCambioDao.cuadrarLineasCambio());
        }
        catch (Exception e) {
            warn(e.getMessage(), e);
            getDelegate().record(e.getMessage(), null);
        }*/
    }

    /**
     * Establece el valor de la propiedad resultados.
     *
     * @param resultados El valor a asignar.
     */
    public abstract void setResultados(List resultados);
}
