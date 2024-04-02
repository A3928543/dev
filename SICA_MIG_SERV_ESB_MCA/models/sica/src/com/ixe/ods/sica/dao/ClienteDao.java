/*
 * $Id: ClienteDao.java,v 1.14.30.1.14.1.26.1 2016/07/13 21:42:36 mejiar Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2006 - 2009 LegoSoft S.C.
 */

package com.ixe.ods.sica.dao;

import java.util.List;
import java.util.Map;

import com.ixe.ods.sica.dto.ClienteContratoInfoPromotorDto;

/**
 * Interfaz para el dao de b&uacute;squeda de clientes y contratos. 
 *
 * @author Jean C. Favila
 * @version $Revision: 1.14.30.1.14.1.26.1 $ $Date: 2016/07/13 21:42:36 $
 */
public interface ClienteDao {

    /**
     * Regresa una lista de arreglos de objetos.
     *
     * @param idEjecutivo El idPersona del ejecutivo.
     * @param razonSocial La raz&oacute; social a buscar, si se buscan personas morales.
     * @param paterno El apellido paterno a buscar, si se buscan personas f&iacute;sicas.
     * @param materno El apellido materno a buscar, si se buscan personas f&iacute;sicas.
     * @param nombre El nombre a buscar, a buscar, si se buscan personas f&iacute;sicas.
     * @param noCuenta El n&uacute;mero de contrato sica espec&iacute;fico a buscar.
     * @param soloEjecVigentes False si se permiten ejecutivos no vigentes.
     * @return List.
     */    
    List findClientes(Integer idEjecutivo, String razonSocial, String paterno, String materno,
                      String nombre, String noCuenta, boolean soloEjecVigentes);
    
    /**
     * Busca un cliente por contrato corto
     * 
     * @param contratoCorto
     * @return {@link ClienteContratoInfoPromotorDto}
     */
    ClienteContratoInfoPromotorDto findClienteByContratoCorto(Integer contratoCorto);
    
    /**
     * Busca un cliente con contrato corto, dado el contrato sica
     * 
     * @param contratoSica
     * @return {@link ClienteContratoInfoPromotorDto}
     */
    ClienteContratoInfoPromotorDto findContratoCortoByContratoSica(String contratoSica);

    /**
     * Regresa una lista de arreglos de objetos.
     *
     * @param razonSocial La raz&oacute; social a buscar, si se buscan personas morales.
     * @param noCuenta El n&uacute;mero de contrato sica espec&iacute;fico a buscar.
     * @return List.
     */
    List findClientes(String razonSocial, String noCuenta);

    /**
     * Regresa una lista de arreglos de objetos sin restringir el ejecutivo.
     *
     * @param razonSocial La raz&oacute; social a buscar, si se buscan personas morales.
     * @param paterno El apellido paterno a buscar, si se buscan personas f&iacute;sicas.
     * @param materno El apellido materno a buscar, si se buscan personas f&iacute;sicas.
     * @param nombre El nombre a buscar, a buscar, si se buscan personas f&iacute;sicas.
     * @param noCuenta El n&uacute;mero de contrato sica espec&iacute;fico a buscar.
     * @param soloEjecVigentes False si se permiten ejecutivos no vigentes.
     * @return List.
     */
    List findClientes(String razonSocial, String paterno, String materno, String nombre,
                      String noCuenta, boolean soloEjecVigentes);

    /**
     * Regresa una lista de los clientes que tienen el nombreCorto especificado y corresponden a
     * alguno de los promotores de la lista especificada.
     *
     * @param nombreCorto El nombre corto del cliente.
     * @param idsPromotores La lista con idPersona de los promotores.
     * @return List.
     */
    List findClientes(String nombreCorto, List idsPromotores);
    
    /**
     * Regresa una lista del cliente con su contrtao y estado de bloqueo
     * @param NoCuenta El numero de cuenta o contrato corto.
     * @return List.
     */
    List findClientesparaBloqueo(String NoCuenta);
    
    /**
     * Regresa una lista del cliente con su contrtao y estado de bloqueo
     * @param NoCuenta El numero de cuenta o contrato corto.
     * @return List.
     */
    List findClientesparaBloqueo(int idContrato);

   /**
    * Regresa el Id Persona del Promotor Asociado al idPersonaCliente y el contrato del Cliente
    * @param idPersonaCliente ID persona del Cliente
    * @return Id Persona del Promotor Asociado al cliente
    */
	Map findPromotorAsignadoCliente(Integer idPersonaCliente);
	
	/**
	 * Regresa un usuario en base al idpersona
	 * @param idPersonaCliente
	 */
	Map findUsuarioByIdPersona(Integer idPersona);
    
}



