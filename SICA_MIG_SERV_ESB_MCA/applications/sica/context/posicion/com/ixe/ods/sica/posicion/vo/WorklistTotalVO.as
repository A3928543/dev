/*
 * WorklistTotalVO.java,v 1.1 2007/06/21 17:15:54 jfavila Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2007 LegoSoft S.C.
 */

/**
 * Value Object que contiene el total de solicitudes de autorizaci&oacute;n para un tipo
 * determinado.
 *
 * @author Jean C. Favila.
 * @version $Revision: 1.2 $ $Date: 2008/02/22 18:25:45 $
 */
class com.ixe.ods.sica.posicion.vo.WorklistTotalVO
{
    /**
     * Clase para el VO de Worklist.
     */
    static var registered = Object.registerClass("com.ixe.ods.sica.vo.WorklistTotalVO",
            WorklistTotalVO);

    /**
     * El tipo de actividad.
     */
    var nombreActividad : String;

    /**
     * El total para el tipo que representa este registro.
     */
    var total : String;
}
