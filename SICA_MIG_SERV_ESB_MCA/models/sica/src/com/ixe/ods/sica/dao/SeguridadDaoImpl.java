package com.ixe.ods.sica.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import net.sf.hibernate.HibernateException;

import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate.support.HibernateDaoSupport;

import com.ixe.ods.bup.model.EmpleadoIxe;
import com.ixe.ods.bup.model.Persona;
import com.ixe.ods.seguridad.model.Jerarquia;
import com.ixe.ods.seguridad.model.NodoJerarquia;
import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.model.SicaNodoJerarquia;
import com.ixe.ods.sica.vo.NodoJerarquiaVO;
import com.ixe.ods.sica.vo.PersonaVO;




public class SeguridadDaoImpl extends HibernateDaoSupport implements SeguridadDao {

	/**
	 * @see SeguridadDao#get(Class, Serializable)
	 */
	public Object get(Class clazz, Serializable id) throws SicaException {		
		return this.getHibernateTemplate().get(clazz, id);
	}
	
	
	public List getRootNodes(Integer idJerarquia) throws SicaException {
		
	    String query = "FROM SicaNodoJerarquia snj left join fetch snj.persona where snj.jerarquia.idJerarquia = " + idJerarquia + " " + "AND snj.jefe is null";
	
	    List rootNodes = super.getHibernateTemplate().find(query);
	    List rootNodesList = new ArrayList();
	    Iterator it = rootNodes.iterator();
	
	    while (it.hasNext()) {
	      SicaNodoJerarquia snj = (SicaNodoJerarquia)it.next();
	      NodoJerarquiaVO nodoJerarquiaVo = new NodoJerarquiaVO(snj);
	      nodoJerarquiaVo.setLength(countChildrenNodes(new Integer(snj.getIdNodoJerarquia())));
	      rootNodesList.add(nodoJerarquiaVo);
	    }

    return rootNodesList;
	}
	
	private int countChildrenNodes(Integer idNode) {
	    return DataAccessUtils.intResult(super.getHibernateTemplate().find("select count(*) from SicaNodoJerarquia snj where snj.jefe.idNodoJerarquia  =  ? ", idNode));
	}

	/**
	 * @see SeguridadDao#getChildren(Integer)
	 */
	public List getChildren(Integer idNodoJerarquia) throws SicaException {
		String query = "FROM SicaNodoJerarquia snj left join fetch snj.persona left join fetch snj.alterno where snj.jefe.idNodoJerarquia = ? ";

		List nodosHijo = super.getHibernateTemplate().find(query, idNodoJerarquia);

	    if ((nodosHijo != null) && (nodosHijo.size() == 0)) {
	      return new ArrayList();
	    }

	    Iterator it = nodosHijo.iterator();
	    List nodosJerarquia = new ArrayList();
	    while (it.hasNext()) {
		      SicaNodoJerarquia snj = (SicaNodoJerarquia)it.next();
		      NodoJerarquiaVO njvo = new NodoJerarquiaVO(snj);
		      njvo.setLength(DataAccessUtils.intResult(super.getHibernateTemplate().find("select count(*) from SicaNodoJerarquia snj where snj.jefe.idNodoJerarquia  =  ? ", new Integer(snj.getIdNodoJerarquia()))));
		
		      nodosJerarquia.add(njvo);
	    }
    return nodosJerarquia;
    }

	/**
	 * @see SeguridadDao#load(Class,  Serializable)
	 */
	public Object load(Class clazz,  Serializable id) throws SicaException {		
		return this.getHibernateTemplate().load(clazz,  id);
	}

	/**
	 * @see SeguridadDao#save(Object)
	 */
	public Serializable save(Object object) throws SicaException {		
		return this.getHibernateTemplate().save(object);
	}

	/**
	 * @see SeguridadDao#saveOrUpdate(Object)
	 */
	public void saveOrUpdate(Object object) throws SicaException {
		this.getHibernateTemplate().saveOrUpdate(object);
	}

	/**
	 * @see SeguridadDao#update(Object)
	 */
	public void update(Object object) throws SicaException {
		this.getHibernateTemplate().update(object);
	}


	/**
	 * @see SeguridadDao#getJerarquias()
	 */
	public List getJerarquias() throws SicaException {
		List res  = null;
		String queryString = "FROM Jerarquia ";		
		res = this.getHibernateTemplate().find(queryString);
		return res;
	}


	/**
	 * @see SeguridadDao#addNode(Integer,  Integer)
	 */
	public int addNode(Integer idPersona, Integer selectedNode) throws SicaException {
	    SicaNodoJerarquia nodoHijo = new SicaNodoJerarquia();
	
	    List nodoExistente = super.getHibernateTemplate().find("FROM SicaNodoJerarquia snj  where snj.persona.idPersona  =  ? ", idPersona);
	
	    if ((nodoExistente != null) && (nodoExistente.size() > 0)) {
	      throw new SicaException(((SicaNodoJerarquia)nodoExistente.get(0)).getPersona().getNombreCorto() + " ya se encuentra asignado a otra persona. ");
	    }
	
	    SicaNodoJerarquia nodoPadre = null;
	    
	    if( selectedNode != null ){
	    	nodoPadre = (SicaNodoJerarquia)super.getHibernateTemplate().get(SicaNodoJerarquia.class, selectedNode);
	    }
	    EmpleadoIxe personaNodo = (EmpleadoIxe)super.getHibernateTemplate().get(EmpleadoIxe.class, idPersona);
	    nodoHijo.setJerarquia((Jerarquia)get(Jerarquia.class, ID_JERARQUIA_SICA));
	    nodoHijo.setJefe(nodoPadre);
	    nodoHijo.setPersona(personaNodo);
	
	    super.getHibernateTemplate().save(nodoHijo);

	    return nodoHijo.getIdNodoJerarquia();
  }
	
	/**
	 * Elimina un nodo que ya no tiene dependencias
	 * @param idNode
	 * @throws HibernateException
	 */
	public void deleteAnyNode(Integer idNode) throws HibernateException {
	}
	
	/**
	 * @see SeguridadDao#deleteNode(Integer)
	 */
	public boolean deleteNode(Integer idNode) throws SicaException {
		
	    if (countChildrenNodes(idNode) != 0) {
	      throw new SicaException("El usuario no puede ser eliminado puesto que aún tiene usuarios en su jerarquia.");
	    }
	    
		SicaNodoJerarquia nodoJerarquia = (SicaNodoJerarquia)get(SicaNodoJerarquia.class, idNode);
		super.getHibernateTemplate().delete(nodoJerarquia);
	
	    return true;
	}


	/*
	 * (non-Javadoc)
	 * @see com.ixe.ods.sica.dao.SeguridadDao#findPersonByName(java.lang.String, java.lang.String, java.lang.String)
	 */
	public List findPersonByName(String paterno, String materno, String nombre) throws SicaException {
	    List personas = new ArrayList();
	    List usuarios = super.getHibernateTemplate().find(QUERY_BUSQUEDA_PERSONAS + "AND pf.paterno LIKE ? AND pf.materno LIKE ? AND pf.nombre LIKE ? ", new Object[] { paterno + "%", materno + "%", nombre + "%" });
	
	    if (usuarios.size() <= 0) {
	      throw new SicaException("No se encontraron personas con los parametros especificados");
	    }
	
	    Iterator it = usuarios.iterator();
	
	    while (it.hasNext()) {
	      EmpleadoIxe empleadoIxe = (EmpleadoIxe)it.next();
	      PersonaVO personaVo = new PersonaVO(empleadoIxe);
	      personas.add(personaVo);
	    }
	
	    return personas;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.ixe.ods.sica.dao.SeguridadDao#findPersonByCveUsr(java.lang.String)
	 */
	public List findPersonByCveUsr(String cveUsuario) throws SicaException {
	    List personas = new ArrayList();
	    List usuarios = super.getHibernateTemplate().find(QUERY_BUSQUEDA_PERSONAS + "AND usuario.clave LIKE ? ", cveUsuario + "%");
	
	    if (usuarios.size() <= 0) {
	      throw new SicaException("No se encontraron personas con los parametros especificados");
	    }
	
	    Iterator it = usuarios.iterator();
	
	    while (it.hasNext()) {
	      EmpleadoIxe empleadoIxe = (EmpleadoIxe)it.next();
	      PersonaVO personaVo = new PersonaVO(empleadoIxe);
	      personas.add(personaVo);
	    }
	
	    return personas;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.ixe.ods.sica.dao.SeguridadDao#findPersonByNoNomina(java.lang.Integer)
	 */
	public List findPersonByNoNomina(Integer noNomina) throws SicaException {
	    List personas = new ArrayList();
	    List usuarios = super.getHibernateTemplate().find(QUERY_BUSQUEDA_PERSONAS + "and ei.noEmpleado LIKE ? ", noNomina + "%");
	
	    if (usuarios.size() <= 0) {
	      throw new SicaException("No se encontraron personas con los parametros especificados");
	    }
	
	    Iterator it = usuarios.iterator();
	
	    while (it.hasNext()) {
	      EmpleadoIxe empleadoIxe = (EmpleadoIxe)it.next();
	      PersonaVO personaVo = new PersonaVO(empleadoIxe);
	      personas.add(personaVo);
	    }
	
	    return personas;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.ixe.ods.sica.dao.SeguridadDao#findPersonByRFC(java.lang.String)
	 */
	public List findPersonByRFC(String rfc) throws SicaException {
	    List personas = new ArrayList();
	    List usuarios = super.getHibernateTemplate().find(QUERY_BUSQUEDA_PERSONAS + "and pf.rfc LIKE ? ", rfc + "%");
	
	    if (usuarios.size() <= 0) {
	      throw new SicaException("No se encontraron personas con los parametros especificados");
	    }
	
	    Iterator it = usuarios.iterator();
	
	    while (it.hasNext()) {
	      EmpleadoIxe empleadoIxe = (EmpleadoIxe)it.next();
	      PersonaVO personaVo = new PersonaVO(empleadoIxe);
	      personas.add(personaVo);
	    }
	
	    return personas;
	}
	
	/**
	 * @see SeguridadDao#moveNode(Integer,  Integer)
	 */
	public void moveNode(Integer idNode, Integer idNodeTo) throws SicaException {
		
		SicaNodoJerarquia nodo = (SicaNodoJerarquia) get(SicaNodoJerarquia.class, idNode);
	    SicaNodoJerarquia nodoDestino = (SicaNodoJerarquia) get(SicaNodoJerarquia.class, idNodeTo);
	    
	    nodo.setJefe(nodoDestino);
	    update(nodo);
	    
	}
	
	public void definirAlterno(Integer idNodoPersona, Integer idAlterno, Date fechaInicio, Date fechaFin) throws SicaException {
		
		    NodoJerarquia persona = (NodoJerarquia)get(NodoJerarquia.class, idNodoPersona);
		    
		    if(persona.getIdPersona().intValue() == idAlterno.intValue() ){
		    	throw new SicaException("Un usuario no puede ser alterno de si mismo.");
		    }
		    
		    Persona alterno = (Persona)get(Persona.class, idAlterno);
		    /*NodoJerarquia alternoJerarquia = (NodoJerarquia)get(NodoJerarquia.class, alterno.getIdPersona());
		    
		    if( alternoJerarquia == null){
		    	throw new SicaException("El usuario seleccionado no puede ser alterno ya que no se encuentra dentro de la Jerarquia.");
		    }*/
		    
		    persona.setIdAlterno(idAlterno);
		    persona.setAlterno(alterno);
		    persona.setFechaInicioAlterno(fechaInicio);
		    persona.setFechaFinAlterno(fechaFin);
		    update(persona);
		    
	}


	/**
	 * Sets jdbcTemplate object
	 * @param jdbcTemplate Instancia de JdbcTemplate de spring
	 */
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	/**
	 * Gets jdbcTemplate object
	 * @return  JdbcTemplate
	 */
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}
	
	/**
	 * JdbcTemplate de spring
	 */
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * Query para busqueda de personas.
	 */
	private static String QUERY_BUSQUEDA_PERSONAS = "SELECT ei " +
													"FROM Usuario usuario, EmpleadoIxe ei, PersonaFisica pf " +
													"WHERE usuario.idPersona = ei.idPersona " +
													"and usuario.clave = ei.claveUsuario " +
													"and ei.idPersona = pf.idPersona " +
													"and usuario.tipo = 0 " +
													"and usuario.status = 1 and ei.status = 'ACTIVO' ";
	
	public static Integer ID_JERARQUIA_SICA = new Integer(1);

}

