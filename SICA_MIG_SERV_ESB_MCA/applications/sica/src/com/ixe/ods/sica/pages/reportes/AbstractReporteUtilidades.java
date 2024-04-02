/*
 * $Id: AbstractReporteUtilidades.java,v 1.1 2008/08/04 20:47:35 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2008 LegoSoft S.C.
 */

package com.ixe.ods.sica.pages.reportes;

import java.util.Date;

import com.ixe.ods.sica.model.ParametroSica;
import com.ixe.ods.sica.pages.SicaPage;

/**
 * Superclase de las p&aacute;ginas de Reporte de Utilidades de Promoci&oacute;n y de Ixe Directo.
 * Implementa la funcionalidad com&uacute;n de ambos reportes.
 * 
 * @author Jean C. Favila
 * @version $Revision: 1.1 $ $Date: 2008/08/04 20:47:35 $
 */
public abstract class AbstractReporteUtilidades extends SicaPage {

    /**
     * Regresa true si el horario del sistema es menor al m&aacute;ximo o mayor al m&iacute;nimo
     * permitidos para la consulta por rango de fechas. Los l&iacute;mites est&aacute;n definidos.
     * por los Par&aacute;metros REP_UT_HR_MAX y REP_UT_HR_MIN respectivamente.
     *
     * @return boolean.
     */
    public boolean isRangoFechasPermitido() {
        String horaMax = getSicaServiceData().findParametro(ParametroSica.REP_UT_HR_MAX).getValor();
        String horaMin = getSicaServiceData().findParametro(ParametroSica.REP_UT_HR_MIN).getValor();
        String horaActual = HOUR_FORMAT.format(new Date());
        return horaActual.compareTo(horaMax) < 0 || horaActual.compareTo(horaMin) > 0;
    }

    /**
     * Regresa el valor de registerDateDe.
     *
     * @return Date.
     */
    public abstract Date getRegisterDateDe();

    /**
     * Fija el valor de registerDateDe.
     *
     * @param registerDateDe El valor a asignar.
     */
    public abstract void setRegisterDateDe(Date registerDateDe);

    /**
     * Regresa el valor de registerDateHasta.
     *
     * @return Date.
     */
    public abstract Date getRegisterDateHasta();

    /**
     * Fija el valor de registerDateHasta.
     *
     * @param registerDateHasta El valor a asignar.
     */
    public abstract void setRegisterDateHasta(Date registerDateHasta);
    
    /**
     * Regresa el valor de tipoReporte.
     *
     * @return String.
     */
    public abstract String getTipoReporte();

    /**
     * Constante para la opci&oacute;n del Reporte Completo en PDF.
     */
    public static final String COMPLETO_PDF = "Reporte Completo (PDF)";

    /**
     * Constante para la opci&oacute;n del Reporte Completo en XLS.
     */
    public static final String COMPLETO_XLS = "Reporte Completo (Excel)";    
}
