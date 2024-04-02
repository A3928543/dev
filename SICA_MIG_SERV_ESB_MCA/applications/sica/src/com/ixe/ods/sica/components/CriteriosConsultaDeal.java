/*
 * $Id: CriteriosConsultaDeal.java,v 1.1.28.1 2010/06/24 17:08:42 galiciad Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2008 LegoSoft S.C.
 */

package com.ixe.ods.sica.components;

import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.form.StringPropertySelectionModel;

/**
 * Componente que contiene una forma para capturar los criterios de consulta de deals.
 *
 * @author Jean C. Favila
 * @version $Revision: 1.1.28.1 $ $Date: 2010/06/24 17:08:42 $
 */
public class CriteriosConsultaDeal extends BaseComponent {

    /**
     * Constructor por default.
     */
    public CriteriosConsultaDeal() {
        super();
    }

    /**
     * Regresa un StringPropertySelectionModel con las opciones de mensajer&iacute; (Cualquiera, Sin
     * y Con).
     *
     * @return IPropertySelectionModel.
     */
    public IPropertySelectionModel getModeloOpcionesMensajeria() {
        return new StringPropertySelectionModel(new String[]{
                CON_SIN_MENSAJERIA, CON_MENSAJERIA, SIN_MENSAJERIA
        });
    }

    /**
     * Regresa un StringPropertySelectionModel con las opciones de fecha como filtro de
     * b&uacute;squeda de deals (Captura, Liquidaci&oacute;n o ninguna).
     * 
     * @return
     */
    public IPropertySelectionModel getModeloFechaFiltroBusqueda() {
        return new StringPropertySelectionModel(new String[]{
        		FECHA_NINGUNA, FECHA_CAPTURA, FECHA_LIQUIDACION
        });
    }

    /**
     * Constante para la opci&oacute;n 'Cualquiera' de Mensajer&iacute;a.
     */
    public static final String CON_SIN_MENSAJERIA = "CUALQUIERA";

    /**
     * Constante para la opci&oacute;n 'Con Mensajer&iacute;a'.
     */
    public static final String CON_MENSAJERIA = "Con Mensajer\u00eda";

    /**
     * Constante para la opci&oacute;n 'Sin Mensajer&iacute;a'.
     */
    public static final String SIN_MENSAJERIA = "Sin Mensajer\u00eda";
    
    /**
     * Constante para la opci&oacute;n 'Ninguna' de fecha filtro de b&uacute;squeda.
     */
    public static final String FECHA_NINGUNA = "Ninguna";

    /**
     * Constante para la opci&oacute;n 'Captura' de fecha filtro de busqueda.
     */
    public static final String FECHA_CAPTURA = "Captura";

    /**
     * Constante para la opci&oacute;n 'Sin Mensajer&iacute;a'.
     */
    public static final String FECHA_LIQUIDACION = "Liquidaci\u00f3n";
}
