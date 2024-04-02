/*
 * $Id: ExcelExport.java,v 1.5 2008/12/26 23:17:33 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2008 LegoSoft S.C.
 */

package com.ixe.ods.sica.components;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.IRequestCycle;

import com.ixe.ods.sica.services.impl.XlsServiceImpl;
import com.ixe.ods.sica.services.model.ModeloXls;

/**
 * Componente que dibuja el bot&oacute;n para exportar un listado a una hoja excel y escribe en el
 * outputstream de la respuesta, el contenido del archivo.
 *
 * @author Jean C. Favila
 * @version  $Revision: 1.5 $ $Date: 2008/12/26 23:17:33 $
 */
public abstract class ExcelExport extends BaseComponent {

    /**
     * Escribe en el output stream el contenido del excel.
     *
     * @param cycle El ciclo de request - response de la p&aacute;gina.
     */
    public void exportar(IRequestCycle cycle) {
        HttpServletResponse response = cycle.getRequestContext().getResponse();
        ModeloXls modelo = new ModeloXls(getBinding("titulo").getString(),
                (List) getBinding("titulos").getObject(),
                (List) getBinding("expresiones").getObject(),
                (List) getBinding("coleccion").getObject());
        XlsServiceImpl.generarEscribir(response, getBinding("nombreArchivo").getString(),
                modelo);
    }
}
