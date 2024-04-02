/*
 * $Id: JdbcSwapDaoImpl.java,v 1.11 2008/02/22 18:25:31 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright © 2005 LegoSoft S.C.
 */

package com.ixe.ods.sica.dao;

import org.springframework.jdbc.core.support.JdbcDaoSupport;

import java.sql.SQLException;

/**
 * Implementaci&oacute;n de los m&eacute;todos para las operaciones con Swaps.
 * 
 * @author Jean C. Favila, Javier Cordova
 * @version $Revision: 1.11 $ $Date: 2008/02/22 18:25:31 $
 */
public class JdbcSwapDaoImpl extends JdbcDaoSupport implements SwapDao {
	
	/**
	 * @see com.ixe.ods.sica.dao.SwapDao#crearFolioSwap() 
	 */
    public int crearFolioSwap() {
        return getJdbcTemplate().queryForInt(QUERY);
    }
    
    /**
     * @see com.ixe.ods.sica.dao.SwapDao#crearFolioProgressRecid()
     */
    public int crearFolioProgressRecid() {
        try {
            boolean ac = this.getConnection().getAutoCommit();
            this.getConnection().setAutoCommit(false);
            int res = getJdbcTemplate().queryForInt(QUERY_PROGRESS_RECID);
            this.getConnection().setAutoCommit(ac);
            return res;
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * Constante para el valor de QUERY.
     */
    private static String QUERY = "SELECT SC_FOLIO_SWAP_SEQ.nextval FROM dual";
    
    /**
     * Constante para el valor de QUERY_PROGRESS_RECID
     */
    private static String QUERY_PROGRESS_RECID = "SELECT SC_BITACORA_ENVIADAS_SEQ.nextval FROM dual";

}
