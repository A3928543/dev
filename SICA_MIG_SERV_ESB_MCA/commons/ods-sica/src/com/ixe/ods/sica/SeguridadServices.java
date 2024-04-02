package com.ixe.ods.sica;

import java.util.List;

import net.sf.hibernate.HibernateException;

import com.ixe.ods.seguridad.model.SeguridadException;
import com.ixe.ods.sica.vo.PersonaVO;

/**
 * 
 * @author Isaac Godinez
 *
 */

public interface SeguridadServices {
	
	/**
	 * Obtiene las distintas jerarquias de lso sistemas
	 * @return List con las jerarquias de uno o mas sistemas
	 * @throws SeguridadException excepcion de seguridad
	 */
	public List getJerarquias()throws SeguridadException;
	
	/**
	 * Obtiene los nodos raiz de la jerarquia especificada
	 * 
	 * @param idJerarquia identificador de la jerarquia
	 * @return List con los nodos raiz
	 * @throws SeguridadException excepcion de seguridad
	 */
	public List getRootNodes(Integer idJerarquia)throws SeguridadException;
	
	/**
	 * Obtiene los nodos hijo del nodo especificado
	 * @param idNodo identificador del nodo
	 * @return List con los nodos hijo
	 * @throws SeguridadException excepcion de seguridad
	 */
	public List getChildren(Integer idNodo)throws SeguridadException;
	
	
	/**
	 * Muevo un nodo con toda la jerarquia inferior hacia otro nodo
	 * 
	 * @param idPersona Id de de la persona que se desea mover.
	 * @param idNode id del nodo que se desea mover.
	 * @param idPersonaTo Id de la persona que sera padre del nodo que se desea mover. 
	 * @param idNodeTo id del nodo que sera padre del nodo que se desea mover.
	 * @return int con el estatus del cambio, 0 exitoso , -1 para un cambio no exitoso 
	 * @throws SeguridadException excepcion de seguridad
	 */
	
	
	public int moveNode(Integer idPersona, Integer idNode, Integer idPersonaTo, Integer idNodeTo )
		throws SeguridadException, HibernateException;

	/**
	 * Elimina a la persona con el <code>idNode</code> de la jerarquia.
	 * El nodo a eliminar no debe tener nodos hijos.
	 * 
	 * @param idNode El id del nodo que se deaea eliminar
	 * @throws SeguridadException excepcion de seguridad
	 */
	public void deleteNode(Integer idNode) throws SeguridadException;
	
	/**
	 * Busca una persona por el Apellido paterno, materno y nombre.
	 * 
	 * @param paterno Apellido paterno de la persona.
	 * @param materno Apellido materno de la persona.
	 * @param nombre Nombre de la persona
	 * @return List Lista de personas que cumplen con los criterios especificados.
	 * @throws SeguridadException excepcion de seguridad
	 */
	public List findPersonByName( String paterno, String materno, String nombre ) throws SeguridadException;
	
	/**
	 * Busca una persona por medio de la clave de usuario
	 * 
	 * @param cveUsuario la clave de usuario.
	 * @return List Lista de personas que cumplen con los criterios especificados.
	 * @throws SeguridadException excepcion de seguridad
	 */
	public List findPersonByCveUsr(String cveUsuario) throws SeguridadException;
	
	/**
	 * Buscar personas por medio de su n&uacute;mero de n&oacute;mina.
	 * 
	 * @param noNomina
	 * @return
	 * @throws SeguridadException
	 */
	public List findPersonByNoNomina(Integer noNomina) throws SeguridadException;
	
	/**
	 * Busca personas por medio de su RFC.
	 * 
	 * @param rfc
	 * @return
	 * @throws SeguridadException
	 */
	public List findPersonByRFC(String rfc) throws SeguridadException;
	
	/**
	 * A&ntilde;ade un nuevo nodo a la jerarquia
	 * 
	 * @param idPersona el id de la persona que se agregará al nodo
	 * @param selectedNode id del nodo al que se desea acgregar un nodo hijo
	 * @throws SeguridadException excepcion de seguridad
	 */
	public int addNode(Integer idPersona,Integer selectedNode)throws SeguridadException;
	
	/**
	 * Asigna usuario idAlterno como alterno a la persona identificada por
	 * idNodoPersona de la fecha inicio a la fin que recibe como pr&aacute;metro.
	 * 
	 * @param idNodoPersona
	 * @param idAlterno
	 * @param fechaInicio
	 * @param fechaFin
	 * @throws SeguridadException
	 */
	public void definirAlterno(Integer idNodoPersona,Integer idAlterno, 
			String fechaInicio, String fechaFin)throws SeguridadException;

}
