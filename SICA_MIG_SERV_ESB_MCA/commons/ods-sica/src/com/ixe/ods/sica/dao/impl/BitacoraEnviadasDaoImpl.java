/*
 * $Id: BitacoraEnviadasDaoImpl.java,v 1.7 2010/04/13 17:33:58 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2008 - 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica.dao.impl;

import java.util.Iterator;
import java.util.List;

import org.springframework.orm.hibernate.support.HibernateDaoSupport;

import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.dao.BitacoraEnviadasDao;
import com.ixe.ods.sica.dao.SwapDao;
import com.ixe.ods.sica.model.BitacoraEnviadas;
import com.ixe.ods.sica.model.Deal;
import com.ixe.ods.sica.model.DealDetalle;
import com.ixe.ods.sica.model.Divisa;

/**
 * Implementaci&oacute;n Hibernate para la interfaz BitacoraEnviadasDao.
 *
 * @author Jean C. Favila
 * @version $Revision: 1.7 $ $Date: 2010/04/13 17:33:58 $
 * @see BitacoraEnviadasDao
 */
public class BitacoraEnviadasDaoImpl extends HibernateDaoSupport implements BitacoraEnviadasDao {

    /**
     * Constructor vac&iacute;o.
     */
    public BitacoraEnviadasDaoImpl() {
        super();
    }

    /**
     * @see com.ixe.ods.sica.dao.BitacoraEnviadasDao#store(com.ixe.ods.sica.model.BitacoraEnviadas).
     */
    public void store(BitacoraEnviadas be) {
        getHibernateTemplate().save(be);
    }

    /**
     * @param be El registro a actualizar.
     * @see com.ixe.ods.sica.dao.BitacoraEnviadasDao#actualizar(
     *          com.ixe.ods.sica.model.BitacoraEnviadas).
     */
    public void actualizar(BitacoraEnviadas be) {
        getHibernateTemplate().update(be);
    }

    /**
     * @param idConf El n&uacute;mero de deal con folio del detalle.
     * @param divisa La clave de la divisa.
     * @return BitacoraEnviadas.
     * @see com.ixe.ods.sica.dao.BitacoraEnviadasDao#findBitacoraEnviadaByIdConfAndDivisa(String,
     *          String).
     */
    public BitacoraEnviadas findBitacoraEnviadaByIdConfAndDivisa(String idConf, String divisa) {
        List lista = getHibernateTemplate().find(
                "FROM BitacoraEnviadas AS be WHERE be.id.idConf = ? And be.id.divisa = ?",
                new Object[]{idConf, divisa});
        for (Iterator it = lista.iterator(); it.hasNext();) {
            return (BitacoraEnviadas) it.next();
        }
        return null;
    }

    /**
     * @deprecated
     * @param folioTas El folio tas.
     * @return BitacoraEnviadas.
     * @throws com.ixe.ods.sica.SicaException Si no se encuentra el registro, o no tiene
     *              statusEstrategia 'PE' (Pendiente).
     * @see com.ixe.ods.sica.dao.BitacoraEnviadasDao#findBitacoraEnviadaByFolioTas(int).
     */
    public BitacoraEnviadas findBitacoraEnviadaByFolioTas(int folioTas) throws SicaException {
        return null;
    }

    /**
     * @param folioTas El folio tas.
     * @return BitacoraEnviadas.
     * @throws com.ixe.ods.sica.SicaException Si no se encuentra el registro, o no tiene
     *              statusEstrategia 'PE' (Pendiente).
     * @see com.ixe.ods.sica.dao.BitacoraEnviadasDao#findBitacoraEnviadaByFolioTas(int).
     */
    public List findBitacorasEnviadasByFolioTas(int folioTas) throws SicaException {
        List bitacoras = getHibernateTemplate().find("FROM BitacoraEnviadas AS be " +
                "WHERE be.folio = ?", new Integer(folioTas));
        if (bitacoras.isEmpty()) {
            throw new SicaException("No existe un registro con el folio " + folioTas +
                    " en la base de datos.");
        }
        for (Iterator it = bitacoras.iterator(); it.hasNext();) {
            BitacoraEnviadas be = (BitacoraEnviadas) it.next();
            if (!BitacoraEnviadas.ST_ESTRAT_PENDIENTE.equals(be.getStatusEstrategia())) {
                throw new SicaException("El folio corresponde a un deal interbancario que ha " +
                        "sido utilizado previamente.");
            }
        }
        return bitacoras;
    }

    /**
     * @param idConf	El identificador de una Operaci&oacute;n de un Deal Reportada a Banxico.
     * 					Se concatena 'idDeal' + 'folioDetalle'.
     * @return BitacoraEnviadas.
     * @see com.ixe.ods.sica.dao.BitacoraEnviadasDao#findBitacoraEnviadasByIdConf(String).
     */
    public BitacoraEnviadas findBitacoraEnviadasByIdConf(String idConf) {
        List bes = getHibernateTemplate().find("FROM BitacoraEnviadas AS be WHERE be.id.idConf = ?",
                idConf);
        return (BitacoraEnviadas) (bes.isEmpty() ? null : bes.get(0));
    }

    /**
     * @return List.
     * @see com.ixe.ods.sica.dao.BitacoraEnviadasDao#findBitacorasEnviadasConError().
     */
    public List findBitacorasEnviadasConError() {
        return getHibernateTemplate().find("FROM BitacoraEnviadas AS be WHERE " +
                "UPPER(be.status) = 'ERROR' AND be.error in " +
                "(-100,-101,-102,-103,-104,-105,-106,-107,-108,-110,-111)");
    }

    /**
     * @see com.ixe.ods.sica.dao.BitacoraEnviadasDao#insertarBitacorasEnviadas(java.util.List).
     */
    public void insertarBitacorasEnviadas(List bitacorasEnviadas) {
        for (Iterator it = bitacorasEnviadas.iterator(); it.hasNext();) {
            BitacoraEnviadas be = (BitacoraEnviadas) it.next();
            if (logger.isWarnEnabled()) {
                logger.warn("Insertando BitacoraEnviadas " + be.getId().getIdConf());
            }
            List besTmp = getHibernateTemplate().find("SELECT be.id.idConf, be.id.divisa FROM " +
                    "BitacoraEnviadas AS be WHERE be.id.idConf = ? AND be.id.divisa = ?",
                    new Object[]{be.getId().getIdConf(), be.getId().getDivisa()});
            if (besTmp == null || besTmp.isEmpty()) {
                if (be.getModoCreacionFolioSwap().equals(
                        BitacoraEnviadas.MODO_CREACION_FOLIO_SEQUENCE)) {
                    be.setFolio(new Integer(getSwapDao().crearFolioSwap()));
                }
                be.setProgressRecid(new Integer(getSwapDao().crearFolioProgressRecid()));
                insertarBitacoraEnviadas(be);
            }
        }
    }

    /**
     * Inserta el objeto en la tabla SC_BITACORA_ENVIADAS.
     *
     * @param be El objeto a insertar.
     */
    public void insertarBitacoraEnviadas(BitacoraEnviadas be) {
        getHibernateTemplate().save(be);
    }

    /**
     * @param deal		El deal de la operaci&oacute;n.
     * @param dealDet	El detalle del deal de la operaci&oacute;n, si es que solo se cancela un
     * 					detalle y no todos los detalles del deal.
     * @see com.ixe.ods.sica.dao.BitacoraEnviadasDao#revisarReportadoBanxico(
     *          com.ixe.ods.sica.model.Deal , com.ixe.ods.sica.model.DealDetalle).
     */
    public void revisarReportadoBanxico(Deal deal, DealDetalle dealDet) {
        BitacoraEnviadas be;
        if (dealDet == null) {
            for (Iterator i = deal.getDetalles().iterator(); i.hasNext(); ) {
                DealDetalle det = (DealDetalle) i.next();
                if (!Divisa.PESO.equals(det.getDivisa().getIdDivisa())) {
                    be = findBitacoraEnviadasByIdConf(Integer.toString(deal.getIdDeal()) + GUION +
                            Integer.toString(det.getFolioDetalle()));
                    if (be != null) {
                    	if (!BitacoraEnviadas.STATUS_CANCELADA.equals(
                                be.getStatus().trim().toUpperCase())) {
                            if (BitacoraEnviadas.STATUS_ENVIAR.equals(
                                    be.getStatus().trim().toUpperCase()) ||
                                    BitacoraEnviadas.STATUS_ERROR.equals(
                                            be.getStatus().trim().toUpperCase())) {
                                be.setStatus(BitacoraEnviadas.STATUS_CANCELADA);
                                getHibernateTemplate().update(be);
                            }
                            else if (BitacoraEnviadas.STATUS_ENVIADA.equals(
                                    be.getStatus().trim().toUpperCase())) {
                                be.setStatus(BitacoraEnviadas.STATUS_ENVIAR1);
                                be.setAccion(BitacoraEnviadas.C_CANCELA_OP);
                                getHibernateTemplate().update(be);
                            }
                        }
                    }
                }
            }
        }
        else {
            be = findBitacoraEnviadasByIdConf(Integer.toString(deal.getIdDeal()) + GUION +
                    Integer.toString(dealDet.getFolioDetalle()));
            if (be != null) {
                if (!BitacoraEnviadas.STATUS_CANCELADA.equals(be.getStatus().trim().
                        toUpperCase())) {
                    if (BitacoraEnviadas.STATUS_ENVIAR.equals(
                            be.getStatus().trim().toUpperCase()) ||
                            BitacoraEnviadas.STATUS_ERROR.equals(
                                    be.getStatus().trim().toUpperCase())) {
                        be.setStatus(BitacoraEnviadas.STATUS_CANCELADA);
                        getHibernateTemplate().update(be);
                    }
                    else if (BitacoraEnviadas.STATUS_ENVIADA.equals(be.getStatus().trim().
                            toUpperCase())) {
                        be.setStatus(BitacoraEnviadas.STATUS_ENVIAR1);
                        be.setAccion(BitacoraEnviadas.C_CANCELA_OP);
                        getHibernateTemplate().update(be);
                    }
                }
            }
        }
    }

    /**
     * Regresa el valor de swapDao.
     *
     * @return SwapDao.
     */
    public SwapDao getSwapDao() {
        return swapDao;
    }

    /**
     * Establece el valor de swapDao.
     *
     * @param swapDao El valor a asignar.
     */
    public void setSwapDao(SwapDao swapDao) {
        this.swapDao = swapDao;
    }

    private SwapDao swapDao;

    /**
     * Constante para el caracter '-'.
     */
    private static final String GUION = "-";
}