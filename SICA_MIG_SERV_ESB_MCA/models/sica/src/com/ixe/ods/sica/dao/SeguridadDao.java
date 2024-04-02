package com.ixe.ods.sica.dao;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.ixe.ods.sica.SicaException;

/**
 * 
 * @author igodinez
 *
 * Interface que define los metodos de seguridad dao para el administrador de jerarquias
 * 
 */

public interface SeguridadDao {
	
	public abstract List getRootNodes(Integer paramInteger) throws SicaException;

	/**
	 * Obtiene los nodos hijos para un nodo especifico
	 * @param idNodoJerarquia .
	 * @return List
	 * @throws HibernateException .
	 */
	public List getChildren(Integer idNodoJerarquia)throws SicaException;
	
	/**
	 * Obtiene las jerarquias
	 * @return List
	 * @throws HibernateException .
	 */
	public List getJerarquias()throws SicaException;
	
	/**
	 * Inserat un objeto fuera de la session de hibernate
	 * @param object  .
	 * @return Serializable 
	 * @throws HibernateException .
	 */
	public Serializable save(Object object)throws SicaException;
	
	/**
	 * inserta un objeto fuera de la session de hibernate o actualiza un
	 * objeto persistente de hibernate
	 * @param object .
	 * @throws HibernateException .
	 */
	public void saveOrUpdate(Object object)throws SicaException;
	
	/**
	 * Actualiza un objeto persistente de Hibernate
	 * @param object .
	 * @throws HibernateException .
	 */
	public void update(Object object)throws SicaException;
	
	/**
	 * Carga un objeto persistente de hibernate existente en la base de datos
	 * @param clazz .
	 * @param id  .
	 * @return Object .
	 * @throws HibernateException .
	 */	
	public Object load(Class clazz, Serializable id)throws SicaException;
	
	/**
	 * Intenta obtener un objeto persostente de hibernate de la base de datos
	 * @param clazz .
	 * @param id  .
	 * @return Object .
	 * @throws HibernateException .
	 */	
	public Object get(Class clazz, Serializable id)throws SicaException;
	
	/**
	 * Elimina un nodo con toda su jerrarquia
	 * @param idNode .
	 * @return bolean .
	 * @throws HibernateException .
	 */
	public boolean deleteNode(Integer idNode)throws SicaException;
	
	/**
	 * Busca una persona por medio de su apellido paterno, materno o nombre.
	 * 
	 * @param paterno Apellido paterno de la persona.
	 * @param materno Apellido materno de la persona.
	 * @param nombre Nombre de la persona
	 * @return List Con la lista de personas que cumplen con los criterios especificados.
	 * @throws SicaException
	 */
	public List findPersonByName(String paterno, String materno, String nombre) throws SicaException;
	
	/**
	 * Busca una persona por medio de la clave de usuario.
	 * 
	 * @param cveUsuario la clave de usuario.
	 * @return List Lista de personas que cumplen con los criterios especificados.
	 * @throws SicaException
	 */
	public List findPersonByCveUsr( String cveUsuario ) throws SicaException;

	/**
	 * Buscar personas por medio de su n&uacute;mero de n&oacute;mina.
	 * 
	 * @param noNomina
	 * @return List de <code>PersonaVO</code> que cumplen con el criterio especificado.
	 * @throws SicaException
	 */
	public List findPersonByNoNomina( Integer noNomina ) throws SicaException;
	
	/**
	 * 
	 * @param rfc
	 * @return
	 * @throws SicaException
	 */
	public List findPersonByRFC( String rfc ) throws SicaException;
	
	/**
	 * Mueve un nodo de un nodo padre a otro
	 * @param idNode .
	 * @param idNodeTo .
	 * @throws HibernateException .
	 */
	public void moveNode(Integer idNode, Integer idNodeTo) throws SicaException;
	
	/**
	 * Establece al usuario <code>idAlterno</code> como alterno para el 
	 * usuario identificado con <code>idNodoPersona</code> desde la fecha <code>fechaInicio</code>
	 * hasta la fecha <code>fechaFin</code> 
	 * 
	 * @param idNodoPersona
	 * @param idAlterno
	 * @param fechaInicio
	 * @param fechaFin
	 * @throws SicaException
	 */
	public void definirAlterno(Integer idNodoPersona, Integer idAlterno, Date fechaInicio, Date fechaFin) throws SicaException;
	
	/**
	 * Añade un nodo a la jerarquia con un padre especifico
	 * 
	 * @param idPersona .
	 * @param selectedNode .
	 * @return int
	 * @throws HibernateException .
	 */
	public int addNode(Integer idPersona, Integer selectedNode)throws SicaException;
	
}

