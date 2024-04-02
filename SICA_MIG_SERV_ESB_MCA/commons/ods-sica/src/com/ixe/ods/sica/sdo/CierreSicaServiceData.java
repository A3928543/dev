/*
 * $Id: CierreSicaServiceData.java,v 1.16 2008/10/27 23:20:08 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 LegoSoft S.C.
*/

package com.ixe.ods.sica.sdo;

import java.util.Date;

import org.springframework.context.ApplicationContextAware;

import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.SicaSiteCache;

/**
 * Service Data Object para las operaciones a la base de 
 * datos que requiere el Cierre del D&iacute;a.
 * 
 * @author Edgar I. Leija
 * @version $Revision: 1.16 $ $Date: 2008/10/27 23:20:08 $
 */
public interface CierreSicaServiceData extends ServiceData, ApplicationContextAware {

    /**
     * Inicia el cierre del d&iacute;a siempre y cuando el Sistema est&eacute; 
     * en un horario v&aacute;lido.
     *
     * @param ssc				Referencia a Objeto SicaSiteCache
     * @param ticket			Para entrar a Site.
     * @param fechaSistema La fecha del sistema que est&aacute; parametrizada en sc_parametro y
     *      que establece el proceso de cierre de d&iacute;a.
     * @return <code>StringBuffer</code> log de lo que se hizo.
     * @throws SicaException 	Si algo sale mal.
     */
    StringBuffer cerrarDiaSica(SicaSiteCache ssc, String ticket, Date fechaSistema)
            throws SicaException;

    /**
     * Regresa el valor del Par&aacute;metro.
     *
     * @param idParametro El identificador del par&aacute;metro que vamos a obtener
     * @return <code>String</code> Nos regresa el valor del Par&aacute;metro.
     */
    String findParametro(String idParametro);

     /**
     * Checa si los l&iacute;mites est&aacute;n dentro de lo permitido ya sea que no esten sobre 
     * su l&iacute;mite o bajo el mismo.
     * Los l&iacute;mites se checan por Mesa y luego por Divisa.Primero calcula la posici&oacute;n
     * global, despu&eacute;s el stopLoss y por el &uacute;ltimo el vAR. La posici&oacute;n puede 
     * estar larga o corta  y dependiendo de si se hace al cierre del d&iacute puede ser 
     * intrad&iacute;a o normal.Cada l&iacute;mite tiene dos umbrales y para ello se genera un 
     * aviso o una alerta seg&uacute;n sea el caso.De la misma forma, dependiendo de este valor se 
     * da aviso a un grupo diferente de usuarios con una jerarquia ALTA, MEDIA,BAJA que recibe 
     * dichas alertas.
     *
     * @param cierre Nos indica si se checan los l&iacute;mites al cierre o intrad&iacute;a
     * @throws SicaException Si algo sale mal.
     */
    void checarLimites(boolean cierre) throws SicaException;

    /**
     * Nos indica si el estado est&aacute; en Operaci&oacute;n Normal.
     *
     * @return boolean
     */
    boolean isEstadoOperacionNormal();

    /**
     * Cambia el estado del Sistema a cierre del d&iacute;a.
     *
     */
    void cambiarEstadoCierre();
}
