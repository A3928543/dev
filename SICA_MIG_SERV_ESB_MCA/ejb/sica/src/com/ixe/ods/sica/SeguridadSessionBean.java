package com.ixe.ods.sica;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.SessionContext;

import org.springframework.context.access.ContextSingletonBeanFactoryLocator;
import org.springframework.ejb.support.AbstractStatelessSessionBean;

import net.sf.hibernate.HibernateException;

import com.ixe.ods.bup.model.Persona;
import com.ixe.ods.seguridad.model.SeguridadException;
import com.ixe.ods.sica.dao.JerarquiaDao;
import com.ixe.ods.sica.dao.SeguridadDao;

/**
 * 
 * @author Isaac Godinez
 *
 * @ejbgen:session ejb-name = SeguridadSessionEJB
 * type = Stateless
 * initial-beans-in-free-pool = 5
 * max-beans-in-free-pool = 50
 * default-transaction = Required
 * trans-timeout-seconds = 40
 * enable-call-by-reference = true
 * @ejbgen:jndi-name remote = ejb/sica/SeguridadHome
 * local = ejb/sica/SeguridadLocalHome
 */

public class SeguridadSessionBean extends AbstractStatelessSessionBean implements Serializable,SeguridadServices{

	/**
	 * Metodo de creacion del EJB
	 */
	protected void onEjbCreate() throws CreateException {
		
	}
	

    /**
     * Cambia los valores por omisi&oacute;n de que archivo tomar como configuraci&oacute;n para
     * Spring. Tambien indica que el BeanFactory ser&aacute; compartido (singleton).
     *
     * @param sessionContext El sessionContext del EJB.
     */
    public void setSessionContext(SessionContext sessionContext) {
        try{
    	super.setSessionContext(sessionContext);
        setBeanFactoryLocator(ContextSingletonBeanFactoryLocator.getInstance());
        setBeanFactoryLocatorKey("sicaApplicationContext");
        }catch (Exception e) {
			e.printStackTrace();
		}
    }
	
	
    /**
     * Si el logger tiene habilitado la opci&oacute;n de WARN, hace el warn del objeto.
     *
     * @param o El objeto a loggear.
     */
    public void warn(Object o) {
        if (logger.isWarnEnabled()) {
            logger.warn(o);
        }
    }
    
    /**
     * @see SeguridadServices#deleteNode(Integer) 
     *  
     * @param idNode
     * @throws SeguridadException
     * @ejbgen:remote-method transaction-attribute = Required
     * @ejbgen:local-method transaction-attribute = Required
     */	
	public void deleteNode(Integer idNode) throws SeguridadException {
		 try {
	            getSeguridadServiceData().deleteNode(idNode);
	        }
	        catch (SicaException e) {
	            warn(e);
	            e.printStackTrace();
	            throw new SeguridadException(e.getMessage());
	        }
	}

	/**
	 * @see com.ixe.ods.sica.SeguridadServices#findPersonByNombre(String, String, String)
	 * 
	 * @param paterno Apellido paterno de la persona.
	 * @param materno Apellido materno de la persona.
	 * @param nombre Nombre de la persona
	 * @throws SeguridadException
	 * @ejbgen:remote-method transaction-attribute = Required
     * @ejbgen:local-method transaction-attribute = Required
	 */
	public List findPersonByName(String paterno, String materno, String nombre) throws SeguridadException {
		try {
            return getSeguridadServiceData().findPersonByName( paterno, materno, nombre );
        }
        catch (SicaException e) {
            warn(e);
            e.printStackTrace();
            throw new SeguridadException(e.getMessage());
        }	
        }

	/**
	 * @see SeguridadServices#findPersonByCveUsr(String)
	 * 
	 * @param cveUsuario la clave de usuario.
	 * @return List Lista de personas que cumplen con los criterios especificados.
	 * @throws SeguridadException
	 * @ejbgen:remote-method transaction-attribute = Required
     * @ejbgen:local-method transaction-attribute = Required
	 */
	public List findPersonByCveUsr(String cveUsuario) throws SeguridadException {
		try {
            return getSeguridadServiceData().findPersonByCveUsr( cveUsuario );
		}
        catch (SicaException e) {
            warn(e);
            e.printStackTrace();
            throw new SeguridadException(e.getMessage());
        }		
	}

	/**
	 * @see SeguridadServices#findPersonByNoNomina(Integer)
	 * 
	 * @param noNomina
	 * @return
	 * @throws SeguridadException
	 * @ejbgen:remote-method transaction-attribute = Required
     * @ejbgen:local-method transaction-attribute = Required
	 */
	public List findPersonByNoNomina(Integer noNomina) throws SeguridadException {
		try {
            return getSeguridadServiceData().findPersonByNoNomina( noNomina );
        }
        catch (SicaException e) {
            warn(e);
            e.printStackTrace();
            throw new SeguridadException(e.getMessage());
        }	
    }
	
	/**
	 * @see SeguridadServices#findPersonByRFC(String)
	 * 
	 * @param rfc
	 * @return
	 * @throws SeguridadException
	 * @ejbgen:remote-method transaction-attribute = Required
     * @ejbgen:local-method transaction-attribute = Required
	 */
	public List findPersonByRFC(String rfc) throws SeguridadException {
		try {
            return getSeguridadServiceData().findPersonByRFC( rfc );
		}
        catch (SicaException e) {
            warn(e);
            e.printStackTrace();
            throw new SeguridadException(e.getMessage());
        }		
	}


	/**
	 * @see SeguridadServices#getChildren(Integer)
	 * @param idNodo
	 * @return
	 * @throws SeguridadException
	 * 
	 * @ejbgen:remote-method transaction-attribute = Required
     * @ejbgen:local-method transaction-attribute = Required
	 */
	public List getChildren(Integer idNodo) throws SeguridadException {
		try {
            return getSeguridadServiceData().getChildren(idNodo);
        }
        catch (SicaException e) {
            warn(e);
            e.printStackTrace();
            throw new SeguridadException(e.getMessage());
        }		
	}

	
	/**
	 * @see SeguridadServices#getJerarquias()
	 * @return
	 * @throws SeguridadException
	 * 
	 * @ejbgen:remote-method transaction-attribute = Required
     * @ejbgen:local-method transaction-attribute = Required
	 */
	
	public List getJerarquias() throws SeguridadException {

		try {
            return getSeguridadServiceData().getJerarquias();
        }
        catch (SicaException e) {
            warn(e);
            e.printStackTrace();
            throw new SeguridadException(e.getMessage());
        }	
	}


	/**
	 * @see SeguridadServices#getRootNodes(Integer)
	 * @param idJerarquia
	 * @return
	 * @throws SeguridadException
	 * 
	 * @ejbgen:remote-method transaction-attribute = Required
     * @ejbgen:local-method transaction-attribute = Required
	 */
	public List getRootNodes(Integer idJerarquia) throws SeguridadException {

		try {
            return getSeguridadServiceData().getRootNodes(idJerarquia);
        }
        catch (SicaException e) {
            warn(e);
            e.printStackTrace();
            throw new SeguridadException(e.getMessage());
        }	
	}

	/**
	 * @see SeguridadServices#moveNode(Integer, Integer, Integer, Integer)
	 * 
	 * @param idPersona
	 * @param idNode
	 * @param idPersonaTo
	 * @param idNodeTo
	 * @return
	 * @throws SeguridadException
	 * @throws HibernateException 
	 * 
	 * @ejbgen:remote-method transaction-attribute = Required
     * @ejbgen:local-method transaction-attribute = Required
	 */
	public int moveNode(Integer idPersona, Integer idNode, Integer idPersonaTo, Integer idNodeTo)
			throws SeguridadException, HibernateException {
		try {
			
			List subordinados  = getJerarquiaDao().findPromotoresJerarquia(idPersona);
			
			for (Iterator iterator = subordinados.iterator(); iterator.hasNext();) {
				Persona persona = (Persona) iterator.next();
				
				if(persona.getIdPersona().equals(idPersonaTo)){
			    	throw new SicaException("El usuario destino es subordinado del usuario a reasignarse, no se permite el movimiento.");
			    }
			}
		    
            this.getSeguridadServiceData().moveNode(idNode,idNodeTo);;
            return 0;
        }
        catch (SicaException e) {
            warn(e);
            e.printStackTrace();
            throw new SeguridadException(e.getMessage());
        }	
	}

	/**
	 * @see SeguridadServices#addNode(Integer)
	 * @param idPersona
	 * @param selectedNode
	 * @throws SeguridadException
	 * 
	 * @ejbgen:remote-method transaction-attribute = Required
     * @ejbgen:local-method transaction-attribute = Required
	 */
	public int addNode(Integer idPersona,Integer selectedNode)throws SeguridadException{
		try {
            return this.getSeguridadServiceData().addNode(idPersona,selectedNode);
        }
        catch (SicaException e) {
            warn(e);
            e.printStackTrace();
            throw new SeguridadException(e.getMessage());
        }
        }
	
	/**
	 * @see SeguridadServices#definirAlterno(Integer, Integer)
	 * @param idNodoPersona
	 * @param idAlterno
	 * @param fechaInicio
	 * @param fechaFin
	 * @throws SeguridadException
	 * 
	 * @ejbgen:remote-method transaction-attribute = Required
     * @ejbgen:local-method transaction-attribute = Required
	 */
	public void definirAlterno(Integer idNodoPersona,Integer idAlterno,
			String fechaInicio, String fechaFin) throws SeguridadException {
		
			Date inicioDate = null;
			Date finDate = null;
			try {
				SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
				try{
					inicioDate = dateFormat.parse(fechaInicio);
					finDate = dateFormat.parse(fechaFin);
					
					if (inicioDate.after(finDate)) {
						throw new SicaException(
							"La fecha de fin debe ser posterior a la fecha de inicio");
					}
					
				}catch( ParseException pe){
					inicioDate = null;
					finDate = null;
				}
			
            this.getSeguridadServiceData().definirAlterno(idNodoPersona, idAlterno, inicioDate, finDate);
		}
        catch (SicaException e) {
            warn(e);
            e.printStackTrace();
            throw new SeguridadException(e.getMessage());
        }	
	}
	
	/**
     * Regresa el valor de SeguridadServiceData.
     *
     * @return SeguridadDao.
     */
    public SeguridadDao getSeguridadServiceData() {
        return (SeguridadDao) getBeanFactory().getBean("seguridadServiceDao");
    }
    
    public JerarquiaDao getJerarquiaDao() {
    	 return (JerarquiaDao) getBeanFactory().getBean("jerarquiaDao");
    }
    
	
	/**
     * El UID para serializaci&oacute;n.
     */
    private static final long serialVersionUID = 1861012115229806267L;

}
