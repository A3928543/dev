/*
 * $Id: CondorProcCambiosDaoImpl.java,v 1.1 2009/01/07 17:26:09 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2009 LegoSoft S.C.
 */
package com.ixe.ods.sica.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.ixe.ods.sica.dao.CondorProcCambiosDao;

import oracle.jdbc.internal.OracleTypes;

/**
 * Implementaci&oacute;n del DAO Jdbc que ejecuta el stored procedure PS_CONDOR_CAMBIOS_TEST. Este
 * DAO permite obtener el cuerpo del correo que se env&iacute;a a la direcci&oacute;n
 * siteSistemas@ixe.com.mx durante el cierre vespertino del SICA.
 *
 * @author Jean C. Favila
 * @version $Revision: 1.1 $ $Date: 2009/01/07 17:26:09 $
 * @see com.ixe.ods.sica.dao.CondorProcCambiosDao
 */
public class CondorProcCambiosDaoImpl extends JdbcDaoSupport implements CondorProcCambiosDao {

    /**
     * Constructor por default.
     */
    public CondorProcCambiosDaoImpl() {
        super();
    }

    /**
     * Ejecuta el stored procedure y regresa un mapa con los resultados.
     *
     * @param fechaReporte La fecha que se quiere generar.
     * @return Map.
     */
    public List ejecutar(java.util.Date fechaReporte) {
        Connection conn = null;
        CallableStatement stmt;
        ResultSet rs;
        ArrayList resultadoReporte = new ArrayList();
        try {
            conn = getDataSource().getConnection();
            stmt = conn.prepareCall("{call PS_CONDOR_PROC_CAMBIOS_TEST(?, ?)}");
            stmt.registerOutParameter(1, OracleTypes.CURSOR);
            stmt.setDate(2, new java.sql.Date(fechaReporte.getTime()));
            stmt.execute();
            rs = (ResultSet) stmt.getObject(1);
            while (rs.next()) {
                resultadoReporte.add(rs.getString(1));
            }
            rs.close();
            stmt.close();
        }
        catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                }
                catch (SQLException e1) {
                    if (logger.isDebugEnabled()) {
                        logger.debug(e);
                    }
                }
            }
        }
        finally {
            if (conn != null) {
                try {
                    conn.close();
                }
                catch (SQLException e) {
                    if (logger.isDebugEnabled()) {
                        logger.debug(e);
                    }
                }
            }
        }
        return resultadoReporte;
    }
}
