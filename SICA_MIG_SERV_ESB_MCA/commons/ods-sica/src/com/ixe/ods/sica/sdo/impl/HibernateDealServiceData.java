/*
 * $Id: HibernateDealServiceData.java,v 1.5 2008/02/22 18:25:48 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2007 LegoSoft S.C.
 */

package com.ixe.ods.sica.sdo.impl;

import com.ixe.ods.bup.model.Direccion;
import com.ixe.ods.sdo.impl.HibernateBaseServiceData;
import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.model.Deal;
import com.ixe.ods.sica.sdo.DealServiceData;
import com.ixe.ods.sica.utils.DateUtils;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import net.sf.hibernate.Criteria;
import net.sf.hibernate.FetchMode;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.expression.Expression;
import org.springframework.orm.hibernate.HibernateCallback;

/**
 * Implementaci&oacute;n Hibernate de la interfaz DealServiceData.
 *
 * @author Jean C. Favila
 * @version $Revision: 1.5 $ $Date: 2008/02/22 18:25:48 $
 * @see com.ixe.ods.sica.sdo.DealServiceData
 */
public class HibernateDealServiceData extends HibernateBaseServiceData implements DealServiceData {

    /**
     * @see com.ixe.ods.sica.sdo.DealServiceData#findDealsCapturaRapida(java.util.Date,
            java.util.Date, String, Integer, boolean)
     */
    public List findDealsCapturaRapida(final Date fechaInicio, final Date fechaFin,
                                       final String idCanal, final Integer idPromotor,
                                       final boolean cancelados) throws SicaException {
        List deals = getHibernateTemplate().executeFind(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                Criteria criteria = session.createCriteria(Deal.class);
                criteria.add(Expression.ge("fechaCaptura", DateUtils.inicioDia(fechaInicio)));
                criteria.add(Expression.le("fechaCaptura", DateUtils.finDia(fechaFin)));
                if (cancelados) {
                    criteria.add(Expression.eq("statusDeal", Deal.STATUS_DEAL_CANCELADO));
                }
                else {
                    criteria.add(Expression.not(
                            Expression.eq("statusDeal", Deal.STATUS_DEAL_CANCELADO)));                    
                }
                criteria.add(Expression.isNull("contratoSica"));
                criteria.setFetchMode("usuario", FetchMode.EAGER);
                criteria.setFetchMode("usuario.persona", FetchMode.EAGER);
                criteria.setFetchMode("canalMesa.canal", FetchMode.EAGER);
                criteria.setFetchMode("detalles", FetchMode.EAGER);
                criteria.setFetchMode("detalles.divisa", FetchMode.EAGER);
                criteria.setMaxResults(MAX_RESULTS);
                if (idCanal != null) {
                    criteria.add(Expression.eq("canalMesa.canal.idCanal", idCanal));
                }
                return new ArrayList(new HashSet(criteria.list()));
            }
        });
        if (deals.isEmpty()) {
            throw new SicaException("No se encontraron deals con los criterios especificados.");
        }
        if (idPromotor != null) {
            for (Iterator it = deals.iterator(); it.hasNext();) {
                Deal deal = (Deal) it.next();
                if (!deal.getUsuario().getPersona().getIdPersona().equals(idPromotor)) {
                    it.remove();
                }
            }
        }
        Collections.sort(deals, new Comparator() {
            public int compare(Object o1, Object o2) {
                Deal d1 = (Deal) o1;
                Deal d2 = (Deal) o2;
                return new Integer(d1.getIdDeal()).compareTo(new Integer(d2.getIdDeal()));
            }
        });
        return deals;
    }

    /**
     * @see DealServiceData#findDireccionMensajeriaDeal(com.ixe.ods.sica.model.Deal).
     *
     */
    public Direccion findDireccionMensajeriaDeal(Deal deal) {
        Direccion dir = deal.getDireccion();
        if (dir == null) {
            return null;
        }
        dir.getCalleYNumero();
        dir.getColonia();
        dir.getDelegacionMunicipio();
        dir.getCiudad();
        dir.getIdEntFederativa();
        dir.getCodigoPostal();
        return dir;

    }

    /**
     * Constante para el N&uacute;mero M&aacute;ximo de Registros por Query.
     */
    private static final int MAX_RESULTS = 500;    
}
