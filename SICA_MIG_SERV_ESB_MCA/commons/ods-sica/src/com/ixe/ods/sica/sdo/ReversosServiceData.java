/*
 * $Id: ReversosServiceData.java,v 1.8 2008/12/26 23:17:35 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2007 - 2008 LegoSoft S.C.
 */

package com.ixe.ods.sica.sdo;

import java.util.List;
import java.util.Date;

import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.model.Reverso;
import com.ixe.ods.sica.vo.ReversoVO;

/**
 * ServiceDataObject para los m&oacute;dulos de generaci&oacute;n y autorizaci&oacute;n de reversos.
 *
 * @author Jean C. Favila
 * @version  $Revision: 1.8 $ $Date: 2008/12/26 23:17:35 $
 */
public interface ReversosServiceData extends WorkFlowServiceData {

    /**
     * Regresa una lista de Reversos cuyo deal original o deal de balanceo corresponda al
     * n&uacute;mero de deal especificado como par&aacute;metro.
     *
     * @param idDeal El n&uacute;mero de deal.
     * @return List.
     */
    List findReversosExistentesParaDeal(int idDeal);

    /**
     * Regresa una lista de objetos ReversoVO que se encuentran pendientes de autorizar.
     *
     * @return List.
     */
    List findReversosPendientes();

    /**
     * Regresa el reverso con el n&uacute;mero especificado.
     *
     * @param idReverso El n&uacute;mero de reverso a encontrar.
     * @return Reverso.
     * @throws SicaException Si no se encuentra dicho reverso en la base de datos.
     */
    Reverso findReverso(int idReverso) throws SicaException;

    /**
     * Regresa una Lista de objetos DealVO. Dos en caso de ser un reverso por cancelaci&oacute;n y
     * tres en caso de no ser el reverso por cancelaci&oacute;n. El primer elemento corresponde al
     * deal original, el segundo al deal contrario (o de balanceo) y el tercero al deal de
     * correcci&oacute;n. Se utiliza este servicio para presentar al autorizador de la mesa la
     * manera en que se generar&aacute;n los deals en caso de autorizar el reverso.
     *
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param idReverso El n&uacute;mero del reverso a consultar.
     * @return List.
     * @throws SicaException Si el reverso no existe, no est&aacute; en status pendiente.
     */
    List prepararDealsReverso(String ticket, int idReverso) throws SicaException;

    void generarReverso(String ticket, int idDeal, boolean porCancelacion, String porNoCuenta,
                        String porFechaValor, String observaciones, int idUsuario,
                        List detallesReverso) throws SicaException;

    String autorizarReverso(String ticket, ReversoVO reverso, List deals, int idUsuario)
            throws SicaException;

    String negarReverso(String ticket, ReversoVO reverso, int idUsuario , boolean deniegaMesa) 
    		throws SicaException;

    /**
     * Regresa una lista de objetos de la clase Reverso que corresponden a los reversos capturados
     * durante la fecha actual del sistema.
     *
     * @param fechaInicio El inicio para la consulta.
     * @param fechaFin El fin para la consulta.
     * @return List.
     * @throws SicaException Si no hay reversos capturados durante la fecha actual del sistema.
     */
    List findReversosCapturados(Date fechaInicio, Date fechaFin) throws SicaException;
}
