/*
 * $Id: HibernateLimiteDao.java,v 1.4.38.1 2011/04/26 01:31:34 galiciad Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2008 LegoSoft S.C.
 */

package com.ixe.ods.sica.dao.impl;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import org.springframework.orm.hibernate.HibernateCallback;
import org.springframework.orm.hibernate.support.HibernateDaoSupport;

import net.sf.hibernate.Criteria;
import net.sf.hibernate.FetchMode;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.expression.Expression;
import net.sf.hibernate.expression.Order;

import com.ixe.ods.sica.DistribucionNormalInversa;
import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.utils.BDUtils;
import com.ixe.ods.sica.dao.LimiteDao;
import com.ixe.ods.sica.model.Divisa;
import com.ixe.ods.sica.model.FactorDivisaActual;
import com.ixe.ods.sica.model.HistoricoTipoCambio;
import com.ixe.ods.sica.model.Limite;
import com.ixe.ods.sica.model.ParametroSica;
import com.ixe.ods.sica.model.Posicion;
import com.ixe.ods.sica.model.PrecioReferenciaActual;
import com.ixe.ods.sica.model.TipoLimite;

/**
 * Implementaci&oacute;n Hibernate de la interfaz LimiteDao. Data Access Object que permite obtener
 * los l&iacute;mites de riesgo para una mesa y divisa determinadas.
 *
 * @author Jean C. Favila
 * @version $Revision: 1.4.38.1 $ $Date: 2011/04/26 01:31:34 $
 * @see LimiteDao
 */
public class HibernateLimiteDao extends HibernateDaoSupport implements LimiteDao {

    /**
     * Constructor vac&iacute;o.
     */
    private HibernateLimiteDao() {
        super();
    }

    /**
     * @param idMesaCambio El Identificador de la Mesa de Cambio (puede ser null).
     * @param idDivisa El Identificador de la Divisa (puede ser null).
     * @return List El L&iacute;mite de Riesgo.
     * @see com.ixe.ods.sica.dao.LimiteDao#findLimiteDeRiesgoByMesaDivisa(Integer, String).
     */
    public List findLimiteDeRiesgoByMesaDivisa(final Integer idMesaCambio, final String idDivisa) {
        return getHibernateTemplate().executeFind(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                Criteria c = session.createCriteria(Limite.class);
                c.add(idMesaCambio == null ? Expression.isNull("mesaCambio") :
                        Expression.eq("mesaCambio.idMesaCambio", idMesaCambio));
                c.add(idDivisa == null ? Expression.isNull("divisa") :
                        Expression.eq("divisa.idDivisa", idDivisa));
                c.setFetchMode("mesaCambio", FetchMode.EAGER);
                c.setFetchMode("divisa", FetchMode.EAGER);
                c.setFetchMode("tipoLimite", FetchMode.EAGER);                
                c.addOrder(Order.asc("tipoLimite.idTipoLimite"));
                return c.list();
            }
        });
    }
    
    /**
     * Regresa una lista de objetos Limite con los niveles correspondientes.
     *
     * @param idMesaCambio La mesa de cambios o todas (null).
     * @param idDivisa La divisa o todas (null).
     * @param horizonteTiempo El horizonte de tiempo (opcional).
     * @return List de Limite.
     */
    public List getLimitesParaMesaDivisa(final Integer idMesaCambio, final String idDivisa,
                                         final Integer horizonteTiempo) {
        return getHibernateTemplate().executeFind(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                // Se obtienen los registros de posicion de interes:
                Criteria posicionCriteria = session.createCriteria(Posicion.class);
                if (idMesaCambio != null) {
                    posicionCriteria = posicionCriteria.add(
                            Expression.eq("mesaCambio.idMesaCambio", idMesaCambio));
                }
                if (idDivisa != null) {
                    posicionCriteria = posicionCriteria.add(
                            Expression.eq("divisa.idDivisa", idDivisa));
                }
                posicionCriteria.setFetchMode("mesaCambio", FetchMode.EAGER);
                posicionCriteria.setFetchMode("divisa", FetchMode.EAGER);
                List posiciones = posicionCriteria.list();
                for (Iterator it = posiciones.iterator(); it.hasNext();) {
                    Posicion pos = (Posicion) it.next();
                    if (!Divisa.FRECUENTE.equals(pos.getDivisa().getTipo())) {
                        it.remove();
                    }                    
                }
                // Se obtiene el precio de referencia actual:
                PrecioReferenciaActual pr = (PrecioReferenciaActual) session.createQuery(
                        "FROM PrecioReferenciaActual").
                        list().get(0);
                // Se obtienen los factores de divisa vigentes:
                List factoresDivisa = session.createQuery("SELECT fda FROM FactorDivisaActual AS fda " +
                        "INNER JOIN FETCH fda.facDiv.fromDivisa INNER JOIN FETCH " +
                        "fda.facDiv.toDivisa").list();
                // Se obtienen los limites relevantes:
                Criteria limiteCriteria = session.createCriteria(Limite.class);
                limiteCriteria.add(idMesaCambio != null ?
                        Expression.eq("mesaCambio.idMesaCambio", idMesaCambio) :
                        Expression.isNull("mesaCambio"));
                limiteCriteria.add(idDivisa != null ?
                        Expression.eq("divisa.idDivisa", idDivisa) :
                        Expression.isNull("divisa"));
                limiteCriteria.setFetchMode("mesaCambio", FetchMode.EAGER);
                limiteCriteria.setFetchMode("divisa", FetchMode.EAGER);
                limiteCriteria.setFetchMode("tipoLimite", FetchMode.EAGER);
                limiteCriteria.addOrder(Order.asc("tipoLimite.idTipoLimite"));
                List limites = limiteCriteria.list();
                int horizonTiempo = 0;
                if (horizonteTiempo == null) {
                    ParametroSica pHorizonte = (ParametroSica) getHibernateTemplate().
                            get(ParametroSica.class, ParametroSica.HORIZONTE_TIEMPO_RIESGO);
                    if (pHorizonte.getValor() != null) {
                        horizonTiempo = Integer.parseInt(pHorizonte.getValor());
                    }
                }
                else {
                    horizonTiempo = horizonteTiempo.intValue();
                }
                // Se configura el nivel de cada l&iacute;mite:
                configurarLimiteVar(limites, posiciones, factoresDivisa, pr, horizonTiempo);
                configurarLimitePosicionLarga(limites, posiciones, factoresDivisa);
                configurarLimitePosicionCorta(limites, posiciones, factoresDivisa);
                configurarLimiteStopLoss(limites, posiciones, pr);
                configurarLimitePosicionLargaIntradia(limites, posiciones, factoresDivisa);
                configurarLimitePosicionCortaIntradia(limites, posiciones, factoresDivisa);
                return limites;
            }
        });
    }

    /**
     * Asigna el nivel de Posici&oacute; Larga dolarizada, cuando la posici&oacute;n inicial del
     * d&iacute;a es larga, o cero si es corta.
     *
     * @param limites La lista de l&iacute;mites para la mesa a revisar.
     * @param posiciones La lista de posiciones de la mesa.
     * @param factoresDivisa La lista de factores divisa actuales utilizada para dolarizar.
     */
    private void configurarLimitePosicionLarga(List limites, List posiciones, List factoresDivisa) {
        Limite limite = getLimiteConTipo(limites, TipoLimite.PL);
        double nivelPosicion = 0;
        for (Iterator it = posiciones.iterator(); it.hasNext();) {
            Posicion pos = (Posicion) it.next();
            double factorDivisa = getFactorDivisa(factoresDivisa, pos.getDivisa().getIdDivisa());
            nivelPosicion += pos.getPosIni().getPosicionInicial() * factorDivisa;
        }
        limite.setNivel(Math.abs(nivelPosicion > 0 ? nivelPosicion : 0));
    }

    /**
     * Asigna el nivel de Posici&oacute; Corta dolarizada, cuando la posici&oacute;n inicial del
     * d&iacute;a es corta, o cero si es larga.
     *
     * @param limites La lista de l&iacute;mites para la mesa a revisar.
     * @param posiciones La lista de posiciones de la mesa.
     * @param factoresDivisa La lista de factores divisa actuales utilizada para dolarizar.
     */
    private void configurarLimitePosicionCorta(List limites, List posiciones, List factoresDivisa) {
        Limite limite = getLimiteConTipo(limites, TipoLimite.PC);
        double nivelPosicion = 0;
        for (Iterator it = posiciones.iterator(); it.hasNext();) {
            Posicion pos = (Posicion) it.next();
            double factorDivisa = getFactorDivisa(factoresDivisa, pos.getDivisa().getIdDivisa());
            nivelPosicion += pos.getPosIni().getPosicionInicial() * factorDivisa;
        }
        limite.setNivel(Math.abs(nivelPosicion < 0 ? nivelPosicion : 0));
    }

    /**
     * Asigna el nivel de Posici&oacute; Larga dolarizada, cuando la posici&oacute;n final del
     * d&iacute;a es larga, o cero si es corta.
     *
     * @param limites La lista de l&iacute;mites para la mesa a revisar.
     * @param posiciones La lista de posiciones de la mesa.
     * @param factoresDivisa La lista de factores divisa actuales utilizada para dolarizar.
     */    
    private void configurarLimitePosicionLargaIntradia(List limites, List posiciones,
                                                       List factoresDivisa) {
        Limite limite = getLimiteConTipo(limites, TipoLimite.PLI);
        double nivelPosicion = 0;
        for (Iterator it = posiciones.iterator(); it.hasNext();) {
            Posicion pos = (Posicion) it.next();
            double factorDivisa = getFactorDivisa(factoresDivisa, pos.getDivisa().getIdDivisa());
            nivelPosicion += pos.getPosicionFinal() * factorDivisa;
        }
        limite.setNivel(Math.abs(nivelPosicion > 0 ? nivelPosicion : 0));
    }

    /**
     * Asigna el nivel de Posici&oacute; Corta dolarizada, cuando la posici&oacute;n final del
     * d&iacute;a es corta, o cero si es larga.
     *
     * @param limites La lista de l&iacute;mites para la mesa a revisar.
     * @param posiciones La lista de posiciones de la mesa.
     * @param factoresDivisa La lista de factores divisa actuales utilizada para dolarizar.
     */
    private void configurarLimitePosicionCortaIntradia(List limites, List posiciones,
                                                       List factoresDivisa) {
        Limite limite = getLimiteConTipo(limites, TipoLimite.PCI);
        double nivelPosicion = 0;
        for (Iterator it = posiciones.iterator(); it.hasNext();) {
            Posicion pos = (Posicion) it.next();
            double factorDivisa = getFactorDivisa(factoresDivisa, pos.getDivisa().getIdDivisa());
            nivelPosicion += pos.getPosicionFinal() * factorDivisa;
        }
        limite.setNivel(Math.abs(nivelPosicion < 0 ? nivelPosicion : 0));
    }

    /**
     * Asigna el nivel de Stop Loss, si existe p&eacuter;dida en la operaci&oacute;n.
     *
     * @param limites La lista de l&iacute;mites para la mesa a revisar.
     * @param posiciones La lista de posiciones de la mesa.
     * @param pr El precio de referencia actual.
     */
    private void configurarLimiteStopLoss(List limites, List posiciones, PrecioReferenciaActual pr) {
        Limite limite = getLimiteConTipo(limites, TipoLimite.SL);
        double utilidadUsd = 0;
        for (Iterator it = posiciones.iterator(); it.hasNext();) {
            Posicion pos = (Posicion) it.next();
            if (Divisa.PESO.equals(pos.getMesaCambio().getDivisaReferencia().getIdDivisa())) {
                utilidadUsd += pos.getUtilidadGlobal() / pr.getPreRef().getMidSpot().doubleValue();
            }
            else {
                utilidadUsd += pos.getUtilidadGlobal();
            }
        }
        limite.setNivel(utilidadUsd < 0 ? utilidadUsd : 0);
    }

    /**
     * Regresa el objeto de la lista de l&iacute;mites que corresponde al tipo
     * <code>idTipoLimite</code>.
     *
     * @param limites La lista de l&iacute;mites a revisar.
     * @param idTipoLimite El concepto del tipo de l&iacute;mite que se desea encontrar.
     * @return Limite.
     * @throws SicaException Si no se encuentra en la lista alg&uacute;n objeto para el tipo.
     */
    private Limite getLimiteConTipo(List limites, int idTipoLimite) throws SicaException {
        for (Iterator it = limites.iterator(); it.hasNext();) {
            Limite lim = (Limite) it.next();
            if (lim.getTipoLimite().getIdTipoLimite() == idTipoLimite) {
                return lim;
            }
        }
        throw new SicaException("No est\u00e1 configurado el tipo de l\u00edmite " + idTipoLimite);
    }

    /**
     * Regresa el factor divisa correspondiente al <code>idDivisa</code> proporcionado.
     *
     * @param factoresDivisa La lista de factores de divisa actuales.
     * @param idDivisa La clave de la divisa a buscar.
     * @return double.
     * @throws SicaException Si no se encuentra la divisa entre los factores proporcionados.
     */
    private double getFactorDivisa(List factoresDivisa, String idDivisa) throws SicaException {
        for (Iterator itFd = factoresDivisa.iterator(); itFd.hasNext();) {
            FactorDivisaActual factorDivisa = (FactorDivisaActual) itFd.next();
            if (factorDivisa.getFacDiv().getFromDivisa().getIdDivisa().equals(idDivisa)) {
                return factorDivisa.getFacDiv().getFactor();
            }
        }
        throw new SicaException("No se encontr\u00f3 el factor para la divisa " + idDivisa);
    }

    private void configurarLimiteVar(List limites, List posiciones, List factoresDivisa,
                                     PrecioReferenciaActual pr, int horizonTiempo) {
        Limite limite = getLimiteConTipo(limites, TipoLimite.V);
        double nivelVar = 0;
        for (Iterator it = posiciones.iterator(); it.hasNext();) {
            Posicion pos = (Posicion) it.next();
            List histTiposCambio = findTipoCambioCierreCienUltimosDiasByIdDivisa(
                    pos.getDivisa().getIdDivisa());
            BigDecimal volatilidad = HistoricoTipoCambio.getVolatilidad(histTiposCambio);
            double tipoCambioSpot = getFactorDivisa(factoresDivisa, pos.getDivisa().getIdDivisa()) *
                    pr.getPreRef().getMidSpot().doubleValue();
            BigDecimal tipoCambioVolatilizado;
            if (pos.getPosicionFinal() < 0) {
                BigDecimal porcVolatilidad = BDUtils.generar6(1).add(volatilidad);
                tipoCambioVolatilizado = BDUtils.generar6(tipoCambioSpot).
                        multiply(porcVolatilidad);
            }
            else {
                BigDecimal porcVolatilidad = BDUtils.generar6(1).subtract(volatilidad);
                tipoCambioVolatilizado = BDUtils.generar6(tipoCambioSpot).
                        multiply(porcVolatilidad);
            }
            ParametroSica pNivel = (ParametroSica) getHibernateTemplate().
                    get(ParametroSica.class, ParametroSica.NIVEL_CONFIANZA_RIESGO);
            double nivConf = 0.0;
            if (pNivel.getValor() != null) {
                nivConf = Double.parseDouble(pNivel.getValor());
            }
            // La constante de “1.645” se refiere al nivel de confianza que se tiene sobre la curva,
            // que en este caso se refiere al 95%. Ahora bien pueden existir otros niveles de
            // confianza como es el caso del un 99% que representa un valor de constante igual al
            // “2.33”. Para tener flexibilidad en el sistema, ese valor se define por medio de un
            // parámetro llamado “nivel de confianza”. El parámetro que define el usuario es el
            // porcentaje de la confianza del cual se deriva el cálculo de 1.645 o 2.33
            // respectivamente.
            BigDecimal var = (tipoCambioVolatilizado.
                    subtract(BDUtils.generar6(tipoCambioSpot))).
                    multiply(BDUtils.generar6(pos.getPosicionFinal()).multiply(
                            BDUtils.generar6(DistribucionNormalInversa.calcular(nivConf))));
            nivelVar += Math.abs(var.doubleValue() * Math.sqrt(horizonTiempo));
        }
        limite.setNivel(nivelVar);
    }
    
    /**
     * Encuentra el hist&oacute;rico del tipo de cambio para los utlimos cien dias para cierta
     * divisa.
     *
     * @param idDivisa El id de la divisa.
     * @return List
     * @throws SicaException Si no hay por lo menos 100 registros.
     */
    private List findTipoCambioCierreCienUltimosDiasByIdDivisa(String idDivisa)
            throws SicaException {
        Calendar gc = new GregorianCalendar();
        gc.add(Calendar.MONTH, -6);
        List l = getHibernateTemplate().find("FROM HistoricoTipoCambio AS htc " +
                "WHERE htc.fecha > ? AND htc.divisa.idDivisa = ? ORDER BY htc.fecha DESC",
                new Object[]{gc, idDivisa});
        if (l.size() < 100) {
            throw new SicaException("Debe haber por lo menos 100 registros para el hist\u00f3rico "
                    + "de tipos de cambio para la divisa " + idDivisa);
        }
        return l.subList(0, 100);
    }
}
