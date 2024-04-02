/*
 * $Id: ContabilidadSicaServiceData.java,v 1.16 2009/12/22 17:21:13 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 - 2009 LegoSoft S.C.
*/

package com.ixe.ods.sica.sdo;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.ixe.ods.seguridad.model.IUsuario;
import com.ixe.ods.sica.SicaException;

/**
 * Interfaz del Service Data Object que realiza la explosi&oacute;n contable del SICA.
 *
 * @author Edgar I. Leija
 * @version $Revision: 1.16 $ $Date: 2009/12/22 17:21:13 $
 */
public interface ContabilidadSicaServiceData extends ServiceData {

    /**
     * Genera la Contabilidad y la Explosi&oacute;n Contable del Sistema
     *
     * @throws SicaException manda excepci&oacute;n para cancelar toda la transacci&oacute;n
     */
    void generacionMovimientosContables() throws SicaException;

    /**
     * Regresa un mapa con la informaci&oacute;n de la tabla SC_UTILIDAD_FORWARD para la fecha
     * especificada cada llave corresponde a la Mesa de Cambios y el valor es la lista de registros
     * UtilidadForward para esa mesa de cambio.
     *
     * @param fecha La fecha a revisar.
     * @return Map.
     */
    Map findUtilidadForwardByFecha(Date fecha);

    /**
     * Inserta los registros de la tabla SC_UTILIDAD_FORWARD despu&eacute;s de evaluar la utilidad
     * para cada una de las operaciones de forwards.
     *
     * @param idMesaCambio El n&uacute;mero de la mesa de cambio.
     * @param fecha        La fecha a generar.
     */
    void generarUtilidadesIxeForwards(int idMesaCambio, Date fecha);

    /**
     * Regresa El monto de utilidad de los forwards en la divisa y mesa especificados.
     *
     * @param idMesaCambio El n&uacute;mero de la mesa de cambio.
     * @param idDivisa La clave de la divisa.
     * @param fecha La fecha a consultar
     * @return double.
     */
    double findUtilidadForward(int idMesaCambio, String idDivisa, Date fecha);

    /**
     * Reprocesa la generaci&oacute;n de los movimientos contables.
     *
     * @param fecha La fecha de reprocesamiento.
     */
    void reprocesoGeneracionMovimientosContables(Date fecha);

    /**
     * Escribe en SC_POLIZA el listado de p&oacute;lizas proporcionado, generando la
     * auditor&iacute;a correspondiente.
     *
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param asientos La lista de objetos com.ixe.ods.sica.model.Poliza a insertar.
     * @param ip La direcci&oacute;n IP del usuario que realiza la operaci&oacute;n.
     * @param us El usuario que realiza la operaci&oacute;n.
     */
    void insertarAsientosManualesSap(String ticket, List asientos, String ip, IUsuario us);
}
