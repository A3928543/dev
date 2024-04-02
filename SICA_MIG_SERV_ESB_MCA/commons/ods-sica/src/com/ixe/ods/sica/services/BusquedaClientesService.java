/*
 * $Id: BusquedaClientesService.java,v 1.2.46.1.14.1 2013/08/28 23:55:56 mejiar Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2007 LegoSoft S.C.
 */

package com.ixe.ods.sica.services;

import com.ixe.ods.sica.SicaValidationException;
import com.ixe.ods.sica.dto.ClienteContratoInfoPromotorDto;

import java.util.List;

/**
 * Interfaz para el servicio de b&uacute;squeda de clientes.
 * 
 * @author Jean C. Favila
 * @version $Revision: 1.2.46.1.14.1 $ $Date: 2013/08/28 23:55:56 $
 */
public interface BusquedaClientesService {

    /**
     * Regresa la lista de clientes que cumplen con los atributos de consulta especificados como
     * par&aacute;metros.
     *
     * @param razonSocial Para consultar personas morales (opcional).
     * @param paterno Para consultar personas f&iacute;sicas (opcional).
     * @param materno Para consultar personas f&iacute;sicas (opcional).
     * @param nombre Para consultar personas f&iacute;sicas (opcional).
     * @param noCuenta Para consultar por n&uacute;mero de cuenta (opcional).
     * @param modo Para consulta general o por jerarqu&iacute;a.
     * @param cuentasSubordinados True para buscar en las ramas inferiores de la jerarqu&iacute;a de
     *          promotores.
     * @param idPromotor El id de persona del Promotor que utiliza la aplicaci&oacute;n.
     * @param idUsuario El id de usuario del Promotor que utiliza la aplicaci&oacute;n.
     * @param ixeDirecto True para poder consultar fuera de la jerarqu&iacute;a.
     * @return List.
     * @throws SicaValidationException Si ocurre alg&uacute;n error.
     */
    List findClientes(String razonSocial, String paterno, String materno, String nombre,
                      String noCuenta, String modo, boolean cuentasSubordinados, Integer idPromotor,
                      int idUsuario, boolean ixeDirecto) throws SicaValidationException;
    
    /**
     * Busca un cliente por contrato corto
     * 
     * @param contratoCorto
     * @return
     */
    ClienteContratoInfoPromotorDto findClienteByContratoCorto(Integer contratoCorto);
    
    /**
     * Busca un cliente por contrato corto
     * 
     * @param contrato o Contrato Corto
     * @return
     */
    List findClientesparaBloqueo (String contrato);
    
    /**
     * Busca un cliente por contrato corto
     * 
     * @param contrato o Contrato Corto
     * @return
     */
    List findClientesparaBloqueo (int idContrato);
    /**
     * Busca un contrato corto a partir del contrato sica
     * 
     * @param contratoSica
     * @return
     */
    ClienteContratoInfoPromotorDto findContratoCortoByContratoSica(String contratoSica);
    
}
