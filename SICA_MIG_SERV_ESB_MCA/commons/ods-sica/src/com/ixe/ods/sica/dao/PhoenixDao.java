/*
 * $Id: PhoenixDao.java,v 1.1.2.1 2010/10/08 01:15:27 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2008 - 2009 LegoSoft S.C.
 */

package com.ixe.ods.sica.dao;

import java.util.Date;
import java.util.List;

import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.model.PersonaListaBlanca;

/**
 * Data Access Object que permite actualizar la lista blanca en Phoenix.
 *
 * @author lvillegas
 * @version $Revision: 1.1.2.1 $ $Date: 2010/10/08 01:15:27 $
 */
public interface PhoenixDao {
    /**
     * Realiza la actualizaci&oacute;n de las listas blancas de la persona
     * recibida como par&qaacute;metro.
     * @param personaLB PersonaListaBlanca que se desea actualizar.
     */
    void updateListasBlanca(PersonaListaBlanca personaLB);
    
    /**
     * M&eacute;todo encargado de actualizar la lista blanca de codigos postales con el
     * par&aacute;metro recibido.
     *
     * @param codigosPostalesBup La lista de c&oacute;digos postales.
     * @param regionFronteriza	Si es regi&oacute;n fronteriza.
     * @param zonaTuristica Si es regi&oacute;n tur&iacute;stica.
     * @param fechaUltimaModificacion La fecha de modificaci&oacute;n.
     * @param idPersona El id de la persona que modific&oacute; el registro.
     */
    void updateListaBlancaCodigosPostales(List codigosPostalesBup, String regionFronteriza,
    		String zonaTuristica, Date fechaUltimaModificacion, Integer idPersona);
    
    /**
     * Elimina de Phoenix la lista de c&oacute;digos postales recibidos como par&aacute;metro.
     * 
     * @param codigosPostalesBup La lista de c&oacute;digos postales.
     */
    void eliminarListaBlancaCodigosPostales(List codigosPostalesBup);
    
    /**
     * Verifica las tablas de phoenix para que sean consistentes con las
     * del SICA.
     */
    void corrigeInconsistenciasIsisPhoenix();
}
