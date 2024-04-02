/*
 * $Id: JdbcBrokerDao.java,v 1.1 2009/09/23 16:50:51 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2009 LegoSoft S.C.
 */

package com.ixe.ods.sica.dao.impl;

import com.ixe.ods.sica.dao.BrokerDao;
import com.ixe.ods.sica.vo.BrokerVO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.object.MappingSqlQuery;

/**
 * Implementaci&oacute;n JDBC del Data Access Object BrokerDao.
 * 
 * @author Jean C. Favila (jfavila)
 * @version $Revision: 1.1 $ $Date: 2009/09/23 16:50:51 $
 * @see com.ixe.ods.sica.dao.BrokerDao
 */
public class JdbcBrokerDao extends JdbcDaoSupport implements BrokerDao {

    /**
     * Constructor por default.
     */
    public JdbcBrokerDao() {
        super();
    }

    /**
     * @see com.ixe.ods.sica.dao.BrokerDao#getBrokersVOParaOperarSwaps().
     * @return List de BrokerVO.
     */
    public List getBrokersVOParaOperarSwaps() {
        return new BrokerSwapsQuery(getDataSource(), BROKER_SWAPS_QUERY).execute();
    }

    /**
     * Clase que representa el query para extraer los brokers.
     */
    class BrokerSwapsQuery extends MappingSqlQuery {

        /**
         * Constructor por default.
         * @param dataSource El data source por el que se ejecutar&aacute; el query.
         * @param query El query a ejecutar.
         */
        BrokerSwapsQuery(DataSource dataSource, String query) {
            super(dataSource, query);
            compile();
        }

        /**
         * Regresa una instancia de la clase BrokerVO a partir de la informaci&oacute;n del result
         * set.
         *
         * @param rs El result set.
         * @param i El n&uacute;mero del registro.
         * @return BrokerVO.
         * @throws SQLException Si no se puede extraer la informaci&oacute;n.
         */
        protected Object mapRow(ResultSet rs, int i) throws SQLException {
            return new BrokerVO(new Integer(rs.getInt(1)).intValue(),
                    rs.getString(2), rs.getString(3), rs.getString(4),
                    new Integer(rs.getInt(5)).intValue(), rs.getString(6));
        }
    }

    /**
     * La constante del query para extraer los brokers que pueden operar swaps.
     */
    private static final String BROKER_SWAPS_QUERY = "SELECT b.ID_PERSONA, pm.RAZON_SOCIAL, " +
            "b.CLAVE_REUTERS, cs.NO_CUENTA, ce.ID_PERSONA ID_PROMOTOR, p.ID_SECTOR " +
            "FROM SC_BROKER b, BUP_PERSONA_MORAL pm, BUP_PERSONA p, BUP_PERSONA_CUENTA_ROL pcr, " +
            "BUP_CONTRATO_SICA cs, BUP_CUENTA_CONTRATO cc, BUP_CUENTA_EJECUTIVO ce " +
            "WHERE b.ID_PERSONA = pm.ID_PERSONA AND pm.ID_PERSONA = p.ID_PERSONA " +
            "AND b.TIPO_BROKER = 'I' AND b.ID_PERSONA = pcr.ID_PERSONA AND pcr.ID_ROL = 'TIT'  " +
            "AND pcr.NO_CUENTA = cs.NO_CUENTA AND cc.NO_CUENTA = pcr.NO_CUENTA " +
            "AND cc.STATUS_ORIGEN = 'Active' AND cc.STATUS = 'ACTIVA' " +
            "AND cc.NO_CUENTA = ce.NO_CUENTA AND ce.STATUS = 'VIG' ORDER BY pm.RAZON_SOCIAL";
}
