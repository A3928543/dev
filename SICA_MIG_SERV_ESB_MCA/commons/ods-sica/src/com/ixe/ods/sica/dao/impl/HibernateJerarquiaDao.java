/*
 * $Id: HibernateJerarquiaDao.java,v 1.1 2009/04/27 15:59:34 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2009 LegoSoft S.C.
 */

package com.ixe.ods.sica.dao.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import org.springframework.orm.hibernate.support.HibernateDaoSupport;

import com.ixe.ods.bup.model.Persona;
import com.ixe.ods.seguridad.model.NodoJerarquia;
import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.dao.JerarquiaDao;
import com.ixe.ods.sica.utils.DateUtils;

/**
 * Data Access Object para las tablas SEGU_JERARQUIA y SEGU_NODO_JERARQUIA.
 *
 * @author Jean C. Favila
 * @version $Revision: 1.1 $ $Date: 2009/04/27 15:59:34 $
 */
public class HibernateJerarquiaDao extends HibernateDaoSupport implements JerarquiaDao {

    /**
     * Regresa una lista de personas que corresponden a la jerarqu&iacute; de la persona
     * especificada, junto con la jerarqu&iacute;a de su promotor alterno, si es que est&aacute;
     * definido.
     *
     * @param idPersona El n&uacute;mero de persona del promotor a revisar.
     * @return List de EmpleadoIxe.
     */
    public List findPromotoresJerarquia(Integer idPersona) {
        List todosNodos = getHibernateTemplate().find("FROM NodoJerarquia AS nj INNER JOIN FETCH " +
                "nj.persona LEFT JOIN FETCH nj.alterno WHERE nj.idJerarquia = ?", new Integer(1));
        // Se encuentra el nodo deseado:
        NodoJerarquia nodoPrincipal = null;
        List susAlternos = new ArrayList();
        for (Iterator it = todosNodos.iterator(); it.hasNext();) {
            NodoJerarquia nodoJerarquia = (NodoJerarquia) it.next();
            if (nodoJerarquia.getIdPersona().equals(idPersona)) {
                nodoPrincipal = nodoJerarquia;
            }
            // Se revisa si la persona es alterno de alguien mas:
            if (nodoJerarquia.getIdAlterno() != null &&
                    nodoJerarquia.getIdAlterno().equals(idPersona)) {
                Calendar unAnioDespues = new GregorianCalendar();
                unAnioDespues.add(Calendar.YEAR, 1);
                Date fechaInicio = nodoJerarquia.getFechaInicioAlterno() != null ?
                        nodoJerarquia.getFechaInicioAlterno() :
                        new GregorianCalendar(2007, 7, 1).getTime();
                Date fechaFin = nodoJerarquia.getFechaFinAlterno() != null ?
                        nodoJerarquia.getFechaFinAlterno() : unAnioDespues.getTime();
                if (DateUtils.finDia().getTime() > fechaInicio.getTime() &&
                        DateUtils.inicioDia().getTime() < fechaFin.getTime()) {
                    susAlternos.add(nodoJerarquia);
                }
            }
        }
        if (nodoPrincipal == null) {
            throw new SicaException("No se encontr\u00f3 el promotor " + idPersona +
                    " en la jerarqu\u00eda.");
        }
        List resultados = new ArrayList();
        agregarSubordinados(nodoPrincipal, resultados, todosNodos);
        for (Iterator it = susAlternos.iterator(); it.hasNext();) {
            NodoJerarquia nodoJerarquia = (NodoJerarquia) it.next();
            agregarSubordinados(nodoJerarquia, resultados, todosNodos);
        }
        List promotores = new ArrayList();
        for (Iterator it = resultados.iterator(); it.hasNext();) {
            NodoJerarquia nodoJerarquia = (NodoJerarquia) it.next();
            promotores.add(nodoJerarquia.getPersona());
        }
        Collections.sort(promotores, new Comparator() {
            public int compare(Object o, Object o1) {
                return  ((Persona) o).getNombreCorto().compareTo(((Persona) o1).getNombreCorto());
            }
        });
        return promotores;
    }

    /**
     * Agrega los subordinados a la lista de resultados en forma recursiva.
     *
     * @param nodoInicial El nodo a evaluar.
     * @param resultados La lista de nodos final.
     * @param nodos La lista de todos los nodos de la jerarqu&iacute;a.
     */
    private void agregarSubordinados(NodoJerarquia nodoInicial, List resultados, List nodos) {
        if (!resultados.contains(nodoInicial)) {
            resultados.add(nodoInicial);
        }
        for (Iterator it = nodos.iterator(); it.hasNext();) {
            NodoJerarquia nodo = (NodoJerarquia) it.next();
            if (nodo.getJefe() != null && nodo.getJefe().getId() == nodoInicial.getId()) {
                if (!resultados.contains(nodo)) {
                    resultados.add(nodo);
                }
                agregarSubordinados(nodo, resultados, nodos);
            }
        }
    }
}
