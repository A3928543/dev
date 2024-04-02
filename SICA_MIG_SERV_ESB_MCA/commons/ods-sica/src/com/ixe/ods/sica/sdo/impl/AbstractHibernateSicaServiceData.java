/*
 * $Id: AbstractHibernateSicaServiceData.java,v 1.29.20.1.4.2.18.1.24.1.8.3 2021/03/18 17:31:36 mejiar Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 - 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica.sdo.impl;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.orm.hibernate.HibernateCallback;
import org.springframework.orm.hibernate.SessionFactoryUtils;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import com.ixe.ods.bup.model.Persona;
import com.ixe.ods.sdo.impl.HibernateBaseServiceData;
import com.ixe.ods.seguridad.model.IUsuario;
import com.ixe.ods.seguridad.services.LoginService;
import com.ixe.ods.sica.Constantes;
import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.SicaSiteCache;
import com.ixe.ods.sica.SicaProperties;
import com.ixe.ods.sica.dao.ExtraDao;
import com.ixe.ods.sica.model.Canal;
import com.ixe.ods.sica.model.Deal;
import com.ixe.ods.sica.model.DealDetalle;
import com.ixe.ods.sica.model.Divisa;
import com.ixe.ods.sica.model.FactorDivisaActual;
import com.ixe.ods.sica.model.LogAuditoria;
import com.ixe.ods.sica.model.PrecioReferenciaActual;
import com.ixe.ods.sica.model.Spread;
import com.ixe.ods.sica.sdo.ServiceData;
import com.ixe.ods.sica.services.H2HService;

/**
 * Superclase que implementa distintos tipos de servicios para el SICA.
 *
 * @author Jean C. Favila
 * @version $Revision: 1.29.20.1.4.2.18.1.24.1.8.3 $ $Date: 2021/03/18 17:31:36 $
 */
public abstract class AbstractHibernateSicaServiceData extends HibernateBaseServiceData
        implements ServiceData, ApplicationContextAware {

    /**
     * @see com.ixe.ods.sica.sdo.ServiceData#auditar(String, String, Integer,
     *          com.ixe.ods.seguridad.model.IUsuario, String, String).
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param ip La direcci&oacute;n IP del usuario.
     * @param idDeal El n&uacute;mero de deal relacionado (opcional).
     * @param usuario El usuario que realiza la operaci&oacute;n.
     * @param tipoOperacion El tipo de operaci&oacute;n realizada.
     * @param datosAdicionales Descripci&oacute;n adicional de la operaci&oacute;n.
     */
	private final transient Log _logger = LogFactory.getLog(AbstractHibernateSicaServiceData.class);
	
	public void auditar(String ticket, String ip, Integer idDeal,
			IUsuario usuario, String tipoOperacion, String datosAdicionales, String tipoEvento,
			String indicador) {
		
		Date fecha = new Date();
		DateFormat Formato = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
		String fechaLog = Formato.format(fecha);
		
		String log = tipoEvento + "|" + fechaLog + "|" + indicador + "|" + usuario.getClave() + "|" +
				ip + "|" + ticket + "|" + tipoOperacion;
		
		if(datosAdicionales == null) {
			datosAdicionales = "NULL";
		}
		
		if(tipoOperacion.indexOf("-BITACORA") > 0) {
			int ind = tipoOperacion.indexOf("-BITACORA");
			tipoOperacion = tipoOperacion.substring(0, ind);
			
		}else {
			_logger.fatal(log);
		}
		
		if(!datosAdicionales.equalsIgnoreCase("BITACORA"))
		{
			LogAuditoria logAudit = new LogAuditoria(ticket, ip, idDeal, usuario,
					tipoOperacion, datosAdicionales);
			store(logAudit);
		}
    }

    /**
     * @see com.ixe.ods.sica.sdo.ServiceData#find(Class, Serializable).
     */
    public Object find(Class clase, Serializable id) {
        return this.getHibernateTemplate().load(clase, id);
    }

    /**
	 * Regresa un usuario que se obtiene a partir del usuario que se encuentra
	 * configurado en sica.properties.
	 * 
	 * @return IUsuario.
	 * @throws SicaException Si no se pueden obtener las propiedades de conexi&oacute;n.
	 */
	public IUsuario findUsuario() throws SicaException {
		String pUsr = SicaProperties.getInstance().getSicaUsr();
		List usuarios = getHibernateTemplate().find("FROM Usuario AS u WHERE u.clave = ?", pUsr);
		return (IUsuario) usuarios.get(0);
	}

	/**
     * @see com.ixe.ods.sica.sdo.ServiceData#findAll(Class).
     */
    public List findAll(Class clase) {
        return getHibernateTemplate().loadAll(clase);
    }

    /**
     * @see ServiceData#findDeal(int).
     */
    public Deal findDeal(int idDeal) throws SicaException {
        try {
            List deals = getHibernateTemplate().find("SELECT DISTINCT d FROM Deal AS d LEFT JOIN " +
                    "FETCH d.contratoSica LEFT JOIN FETCH d.promotor LEFT JOIN FETCH d.usuario " +
                    "AS u LEFT JOIN FETCH u.persona LEFT JOIN FETCH d.dirFactura LEFT JOIN FETCH " +
                    "d.dirFacturaMensajeria LEFT JOIN FETCH d.direccion LEFT JOIN FETCH " +
                    "d.detalles AS det LEFT JOIN FETCH " +
                    "det.divisa LEFT JOIN FETCH det.plantilla LEFT JOIN FETCH det.mesaCambio WHERE d.idDeal = ?",
                    new Integer(idDeal));
            if (deals.isEmpty()) {
                throw new SicaException("No se encontr\u00f3 el deal con el n\u00famero " + idDeal
                        + ".");
            }
            Deal deal = (Deal) deals.get(0);
            try {
                Hibernate.initialize(deal.getCanalMesa());
                if (deal.getCliente() != null) {
                    Hibernate.initialize(deal.getCliente());
                }
                if (deal.isInterbancario()) {
                    if (deal.getBroker() != null) {
                        Hibernate.initialize(deal.getBroker());
                        deal.getBroker().getClaveReuters();
                        deal.getBroker().getId().getPersonaMoral().getNombreCompleto();
                    }
                }
            }
            catch (HibernateException e) {
                throw SessionFactoryUtils.convertHibernateAccessException(e);
            }
            return deal;
        }
        catch (SicaException e) {
            if (logger.isDebugEnabled()) {
                logger.debug(e);
            }
            throw e;
        }
        catch (Exception e) {
            if (logger.isDebugEnabled()) {
                logger.debug(e);
            }
            throw new RuntimeException(e);
        }
    }

    /**
     * @see ServiceData#findDealConCanal(int).
     */
    public Deal findDealConCanal(int idDeal) throws SicaException {
        try {
            List deals = getHibernateTemplate().find("SELECT DISTINCT d FROM Deal AS d LEFT JOIN " +
                    "FETCH d.contratoSica LEFT JOIN FETCH d.promotor LEFT JOIN FETCH d.usuario " +
                    "AS u LEFT JOIN FETCH u.persona LEFT JOIN FETCH d.dirFactura LEFT JOIN " +
                    "FETCH d.dirFacturaMensajeria LEFT JOIN FETCH d.direccion LEFT JOIN FETCH " +
                    "d.detalles AS det LEFT JOIN FETCH " +
                    "det.divisa LEFT JOIN FETCH det.plantilla WHERE d.idDeal = ?",
                    new Integer(idDeal));
            if (deals.isEmpty()) {
                throw new SicaException("No se encontr\u00f3 el deal con el n\u00famero " + idDeal
                        + ".");
            }
            Deal deal = (Deal) deals.get(0);
            try {
                Hibernate.initialize(deal.getCanalMesa());
                Canal c = deal.getCanalMesa().getCanal();
                c.getNombre();
                if (c.getSucursal() != null) {
                    c.getSucursal().getNombre();
                }
                if (deal.getCliente() != null) {
                    Hibernate.initialize(deal.getCliente());
                }
                if (deal.isInterbancario()) {
                    if (deal.getBroker() != null) {
                        Hibernate.initialize(deal.getBroker());
                        deal.getBroker().getClaveReuters();
                        deal.getBroker().getId().getPersonaMoral().getNombreCompleto();
                    }
                }
            }
            catch (HibernateException e) {
                throw SessionFactoryUtils.convertHibernateAccessException(e);
            }
            return deal;
        }
        catch (SicaException e) {
            if (logger.isDebugEnabled()) {
                logger.debug(e);
            }
            throw e;
        }
        catch (Exception e) {
            if (logger.isDebugEnabled()) {
                logger.debug(e);
            }
            throw new RuntimeException(e);
        }
    }
    
    /**
     * @see ServiceData#findUtilidadCierre(Date, Date).
     */
    public List findUtilidadCierre(Date fechaHoy , Date fecha) {
        return getHibernateTemplate().findByNamedQuery("findUtilidadCierre",
                new Object[] {fechaHoy, fecha});
    }

    /**
     * @see ServiceData#findUtilidadCierre(Date, Date).
     */
    public List findUtilidadCierreVespertino(Date fechaHoy , Date fecha) {
        return getHibernateTemplate().findByNamedQuery("findUtilidadCierreVespertino",
                new Object[] {fechaHoy, fecha});
    }

    /**
     * @see ServiceData#findCanal(String).
     */
    public Canal findCanal(String idCanal) throws SicaException {
        List canales = getHibernateTemplate().find("FROM Canal AS c INNER JOIN FETCH c.mesaCambio "
                + "AS m INNER JOIN FETCH m.divisaReferencia LEFT JOIN FETCH c.sucursal "
                + "WHERE c.idCanal = ?", idCanal);
        if (canales.isEmpty()) {
            throw new SicaException("No se encontr\u00f3 el canal con clave " + idCanal + ".");
        }
        Canal canal = (Canal) canales.get(0);
        canal.getNombre();
        if (canal.getSucursal() != null) {
            canal.getSucursal().getNombre();
        }                    
        return canal;
    }

    /**
     * @see com.ixe.ods.sica.sdo.SicaServiceData#findLogByDealTodos(int)
     */
    public List findLogByDealTodos(int idDeal) {
        return getHibernateTemplate().findByNamedQuery("findLogByDealTodos", new Integer (idDeal));
    }

    /**
     * @see com.ixe.ods.sica.sdo.ServiceData#findPersonaContratoSica(String)
     */
    public Persona findPersonaContratoSica(String noCuenta) {
        List list = getHibernateTemplate().findByNamedQuery("findPersonaContratoSica", noCuenta);
        if (list.size() > 0) {
            Object[] obj = (Object[]) list.get(0);
            return (Persona) obj[1];
        }
        return null;
    }

    /**
     * @see com.ixe.ods.sica.sdo.SicaServiceData#findFactoresDivisaActuales().
     */
    public List findFactoresDivisaActuales() {
        return getHibernateTemplate().find("SELECT fda FROM FactorDivisaActual AS fda INNER JOIN FETCH "
                + "fda.facDiv.fromDivisa INNER JOIN FETCH fda.facDiv.toDivisa");
    }

    /**
     *@see ServiceData#findPrecioReferenciaActual()
     */
    public PrecioReferenciaActual findPrecioReferenciaActual() {
    	PrecioReferenciaActual pra = (PrecioReferenciaActual) getHibernateTemplate().
        loadAll(PrecioReferenciaActual.class).get(0);
    	return pra;
    }

    /**
     * @see com.ixe.ods.sica.sdo.ServiceData#findSpreadsActualesRefByMesaCanal(int, String).
     */
    public List findSpreadsActualesRefByMesaCanal(int idMesaCambio, String idCanal) {
        return getHibernateTemplate().
                findByNamedQuery("findSpreadsActualesByMesaCanalFormaLiquidacionDivisa",
                        new Object[] { new Integer(idMesaCambio), idCanal, Constantes.TRANSFERENCIA,
                                Divisa.DOLAR });
    }
        
    /**
     * @see com.ixe.ods.sica.sdo.ServiceData#findSpreadsActualesRefByTipoPizarron(int).
     */
    public List findSpreadsActualesRefByTipoPizarron(int idTipoPizarron) {
        return getHibernateTemplate().
                findByNamedQuery("findSpreadsActualesByTipoPizarronFormaLiquidacionDivisa",
                        new Object[] { new Integer(idTipoPizarron), 
                		Constantes.TRANSFERENCIA,
                        Divisa.DOLAR });
    }    

    /**
     * @see com.ixe.ods.sica.sdo.ServiceData#findSpreadsActualesByTipoPizarron(Integer).
     */
    public List findSpreadsActualesByTipoPizarron(int idTipoPizarron) {
        List spreads = getHibernateTemplate().
        	findByNamedQuery("findSpreadsActualesByMesaTipoPizarron",
                new Integer(idTipoPizarron));
        ordenarSpreads(spreads);
        return spreads;
    }
    
    /**
     * @see com.ixe.ods.sica.sdo.ServiceData#findSpreadsActualesByCanal(String).
     */
    public List findSpreadsActualesByCanal(String idCanal) {
        List spreads = getHibernateTemplate().findByNamedQuery("findSpreadsActualesByCanal",
                idCanal);
        ordenarSpreads(spreads);
        return spreads;
    }
    
    /**
     * @see com.ixe.ods.sica.sdo.ServiceData#findSpreadsActualesByTipoPizarron(Integer)
     */
    public List findSpreadsActualesByTipoPizarron(Integer idTipoPizarron) {
        List spreads = getHibernateTemplate().findByNamedQuery("findSpreadsActualesByTipoPizarron",
                idTipoPizarron);
        ordenarSpreads(spreads);
        return spreads;
    }
    
    /**
     *@see ServiceData#getFactorConversionFromTo(String, String)
     */
    public double getFactorConversionFromTo(String idFromDivisa, String idToDivisa)
            throws SicaException {
        List l = getHibernateTemplate().findByNamedQuery("findFactorDivisaActualCualquierOrden",
        		new Object[] { idFromDivisa, idToDivisa, idToDivisa, idFromDivisa });
        if (l.size() > 0) {
            FactorDivisaActual fd = (FactorDivisaActual) l.get(0);
            return idFromDivisa.equals(fd.getFacDiv().getFromDivisa().getIdDivisa()) ?
                    fd.getFacDiv().getFactor() : 1.0 / fd.getFacDiv().getFactor();
        }
        throw new SicaException("No se encuentra declarado el factor divisa de " + idFromDivisa
                + " a " + idToDivisa);
    }

    /**
     * @see ServiceData#findAllDealsTresMeses(java.util.Date).
     */
    public List findAllDealsTresMeses(final Date de) {
    	return getHibernateTemplate().executeFind(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException {
            	Query q = session.createQuery("FROM Deal AS d WHERE d.fechaCaptura < :fInicio "
                        + "ORDER BY d.swap.idFolioSwap");
            	q.setParameter("fInicio", de);
                q.setMaxResults(30);
                return q.list();
            }
        });
    }

    /**
     * @see com.ixe.ods.sica.sdo.ServiceData#inicializarDeal(com.ixe.ods.sica.model.Deal).
     */
    public void inicializarDeal(Deal deal) {
        try {
        	Hibernate.initialize(deal.getCanalMesa());
            Hibernate.initialize(deal.getContratoSica());
            if (deal.getPromotor() != null) {
                deal.getPromotor().getNombreCompleto();
            }
            if (deal.getUsuario() != null) {
                deal.getUsuario().getClave();
            }
            if (deal.getCliente() != null) {
                Hibernate.initialize(deal.getCliente());
            }
            if (deal.isInterbancario()) {
            	if (deal.getBroker() != null) {
            		Hibernate.initialize(deal.getBroker());
            		deal.getBroker().getClaveReuters();
            		deal.getBroker().getId().getPersonaMoral().getNombreCompleto();
                }
            }
            if (deal.getDirFactura() != null) {
                deal.getDirFactura().getDelegacionMunicipio();
            }
            for (Iterator it = deal.getDetalles().iterator(); it.hasNext();) {
                DealDetalle det = (DealDetalle) it.next();
                Hibernate.initialize(det.getFactorDivisa());
                Hibernate.initialize(det.getDivisa());
            }
        }
        catch (HibernateException e) {
            throw SessionFactoryUtils.convertHibernateAccessException(e);
        }
    }

    /**
     * Regresa true si claveFormaLiq se encuentra en el arreglo cves.
     *
     * @param claveFormaLiq La clave forma de liquidaci&oacute;n.
     * @param cves El arreglo de claves.
     * @return boolean.
     */
    private boolean esAlgunoDe(String claveFormaLiq, String[] cves) {
        for (int i = 0; i < cves.length; i++) {
            if (claveFormaLiq.equals(cves[i])) {
                return true;
            }
        }
        return false;
    }

    /**
     * Ordena la lista de spreads de acuerdo a  TRAEXT < DOCEXT < MEXDOL < CHVIAJ < EFECTI.
     *
     * @param spreads La lista de spreads a ordenar.
     */
    protected void ordenarSpreads(List spreads) {
        Collections.sort(spreads, new Comparator() {
            public int compare(Object sp1, Object sp2) {
                String cfl1 = ((Spread) sp1).getClaveFormaLiquidacion();
                String cfl2 = ((Spread) sp2).getClaveFormaLiquidacion();
                if (cfl1.equals(cfl2)) {
                    return 0;
                }
                else if (Constantes.TRANSFERENCIA.equals(cfl1) ||
                        (Constantes.DOCUMENTO.equals(cfl1)
                                && !esAlgunoDe(cfl2, new String[]{Constantes.TRANSFERENCIA}))
                        || (Constantes.MEXDOLAR.equals(cfl1)
                        && !esAlgunoDe(cfl2, new String[]{Constantes.TRANSFERENCIA,
                        Constantes.DOCUMENTO}))
                        || (Constantes.TRAVELER_CHECKS.equals(cfl1)
                        && !esAlgunoDe(cfl2,
                        new String[]{Constantes.TRANSFERENCIA, Constantes.DOCUMENTO,
                                Constantes.MEXDOLAR}))) {
                    return -1;
                }
                return 1;
            }
        });
    }

    /**
     * @see ServiceData#update(java.util.List).
     */
    public void update(List registros) {
        for (Iterator it = registros.iterator(); it.hasNext();) {
            update(it.next());
        }
    }

    /**
     * Si el logger tiene habilitado la opci&oacute;n de DEBUG, hace el debug del objeto.
     *
     * @param o El objeto a loggear.
     */
    public void debug(Object o) {
        if (logger.isDebugEnabled()) {
            logger.debug(o);
        }
    }

    /**
     * Si el logger tiene habilitado la opci&oacute;n de DEBUG, hace el debug del objeto.
     *
     * @param o El objeto a loggear.
     * @param t La excepci&oacute;n a loggear.
     */
    public void debug(Object o, Throwable t) {
        if (logger.isDebugEnabled()) {
            logger.debug(o, t);
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
     * Si el logger tiene habilitado la opci&oacute;n de WARN, hace el warn del objeto.
     *
     * @param o El objeto a loggear.
     * @param t La excepci&oacute;n a loggear. 
     */
    public void warn(Object o, Throwable t) {
        if (logger.isWarnEnabled()) {
            logger.warn(o, t);
        }
    }
    
    public ExtraDao getExtraDao() {
		return _extraDao;
	}

	public void setExtraDao(ExtraDao _extraDao) {
		this._extraDao = _extraDao;
	}

	/**
     * Regresa el valor de loginService
     *
     * @return  LoginService
     */
    public LoginService getLoginService() {
        return _loginService;
    }

    /**
     * Establece el valor de loginService.
     *
     * @param loginService El valor a asignar.
     */
    public void setLoginService(LoginService loginService) {
        _loginService = loginService;
    }
    
    public H2HService getH2hService() {
		return h2hService;
	}

	public void setH2hService(H2HService service) {
		h2hService = service;
	}

	/**
     * @see org.springframework.context.ApplicationContextAware#setApplicationContext(
            org.springframework.context.ApplicationContext)
     */
    public void setApplicationContext(ApplicationContext arg0) throws BeansException {
        _appContext = arg0;
    }

    /**
     * Regresa el valor del bean 'sicaSiteCache' del application context.
     *
     * @return SicaSiteCache.
     */
    protected SicaSiteCache getSicaSiteCache() {
        return (SicaSiteCache) _appContext.getBean("sicaSiteCache");
    }

    /**
     * Variable que contiene el Contexto de la Aplicacion.
     */
    protected ApplicationContext _appContext;

    /**
     * Referencia a LoginService.
     */
    private LoginService _loginService;
    
    /**
     * Referencia a extraDao.
     */
    private ExtraDao _extraDao;
    
    private H2HService h2hService;
    
    /**
     * Constante Tipo Persona Moral.
     */
    public static final String TIPO_PERSONA_MORAL = "PM";
}
