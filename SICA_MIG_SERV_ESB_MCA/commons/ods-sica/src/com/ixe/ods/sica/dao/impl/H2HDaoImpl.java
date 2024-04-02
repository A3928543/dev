package com.ixe.ods.sica.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.orm.hibernate.support.HibernateDaoSupport;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;

import com.ixe.ods.sica.dao.H2HDao;
import com.ixe.ods.sica.model.Deal;
import com.ixe.ods.sica.model.RegulatorioDatosPM;
import com.ixe.ods.sica.utils.DateUtils;

public class H2HDaoImpl extends HibernateDaoSupport implements H2HDao 
{
	public H2HDaoImpl()
	{
		super();
	}
	
	public List findDetalleH2HByIdDealPosicion(int idDealPosicion )
	{
		String query = "FROM DetalleH2H AS d WHERE d.idDealPosicion = ?";
		List detallesH2H = getHibernateTemplate().find(query, new Long(idDealPosicion));
		return detallesH2H;
	}
	
	public List findDealsEnAltaProcesandoceCapturadosHoy()
	{
		Date fechaCapturaHoy = DateUtils.inicioDia(new Date());
		Deal deal = null;
		Set deals = null;
		List listaDeals = null;
		Iterator it = null;
		
		try
		{
			deals = new HashSet( getHibernateTemplate().find("FROM Deal AS d " +
															 "LEFT JOIN FETCH d.detalles AS det " +
															 "INNER JOIN FETCH det.divisa " +
															 "LEFT JOIN FETCH d.contratoSica " +
															 "WHERE (d.statusDeal = 'AL' OR d.statusDeal = 'CO') " + 
															 "  AND d.fechaCaptura >= ? AND d.reversado = 0 AND d.tipoValor != 'CASH'",
															 fechaCapturaHoy));
			if(deals != null && !deals.isEmpty())
			{
				it = deals.iterator();
				while(it.hasNext())
				{
					try
					{
						deal = (Deal)it.next();
						if(deal.getCliente() != null)
							Hibernate.initialize(deal.getCliente());
						if(deal.getPromotor() != null)
							Hibernate.initialize(deal.getPromotor()); 
					}
					catch(HibernateException e) {e.printStackTrace();}
				}
				
				listaDeals = new ArrayList(deals);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return listaDeals;
	}
	
	public List findDetallesH2HDeal(int idDeal)
	{
		String query = "FROM DetalleH2H AS d WHERE d.idDeal = ?";
		List detallesH2H = getHibernateTemplate().find(query, new Long(idDeal));
		return detallesH2H;
	}
	
	public RegulatorioDatosPM findDatosRegulatoriosPM(String contratoSica, Integer idPersonaCliente)
	{
		RegulatorioDatosPM pm = null;
		String query = "FROM RegulatorioDatosPM as r WHERE (r.idPersona = ? AND r.contratoSica = ?) OR " +
                       "r.contratoSica = ? ORDER BY r.id DESC";
		List lista = getHibernateTemplate().find(query, new Object[]{idPersonaCliente,contratoSica,contratoSica});
		if(lista != null && !lista.isEmpty())
			pm = (RegulatorioDatosPM)lista.get(0);
		return pm;
	}
	
	public String findClaveBanxico(String contratoSica, Integer idPersonaCliente)
	{
		String claveBanxico = null;
		RegulatorioDatosPM pm = null;
		String query = "FROM RegulatorioDatosPM as r WHERE (r.idPersona = ? AND r.contratoSica = ?) OR " +
		               "r.contratoSica = ? ORDER BY r.id DESC";
		List lista = getHibernateTemplate().find(query, new Object[]{idPersonaCliente,contratoSica,contratoSica});
		if(lista != null && !lista.isEmpty())
		{
			for(int indice = 0; indice < lista.size(); indice++)
			{
				pm = (RegulatorioDatosPM)lista.get(indice);
				if(pm.getClaveBanxico() != null)
				{
					claveBanxico = pm.getClaveBanxico();
					break;
				}
			}
			
			lista.clear();
			lista = null;
		}
		
		return claveBanxico;
	}
	
	public void save(Object object)
	{
		getHibernateTemplate().save(object);
	}
	
	public void update(Object object)
	{
		getHibernateTemplate().update(object);
	}
}
