/* 
 * $Id: JdbcCierreDaoImpl.java,v 1.5 2010/02/26 00:49:45 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 - 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.object.MappingSqlQuery;

/**
 * Implementacion de los m&acute;todos para el dao del Cierre del dia del SICA.
 * 
 * @author Gustavo Gonzalez (ggonzalez)
 * @version $Revision: 1.5 $ $Date: 2010/02/26 00:49:45 $
 */
public class JdbcCierreDaoImpl extends JdbcDaoSupport implements CierreDao {
	
	/**
	 * Constructor vac&iacute;o.
	 */
	public JdbcCierreDaoImpl() {
		super();
	}
	
	/**
     * @param idGrupoEmpresarial El n&uacute;mero de grupo empresarial a revisar.
     * @return List.
	 * @see CierreDao#findPromotoresPorLineaDeCredito(Integer).
	 */
	public List findPromotoresPorLineaDeCredito(Integer idGrupoEmpresarial){
        return new ParametroEjecutivosPorLineasCredito(getDataSource()).execute(
                new Object[]{idGrupoEmpresarial});
    }

    /**
     * Inactiva los contratos que no han tenido operaciones.
     *
     * @return int El n&uacute;mero de contratos actualizados.
     */
    public int inactivaContratosSica(int diasAtras) {
    	String query = UPDATE_CONTRATOS_SICA_SIN_DEAL.replaceAll("\\@", String.valueOf(diasAtras));
        return getJdbcTemplate().update(query);
	}
	
	/**
     * Clase interna para accesar a las propiedades de los registros obtenidos
     * para los montos por sectores de deals interbancarios de estrategia.
     * 
     * @author Gustavo Gonzalez (ggonzalez)
     */
    class ParametroEjecutivosPorLineasCredito extends MappingSqlQuery {

        /**
    	 * Constructor de la clase.
    	 * 
    	 *  @param ds El dataSource.
    	 */
        public ParametroEjecutivosPorLineasCredito(DataSource ds) {
        	super(ds, QUERY_EJECUTIVOS);
        	super.declareParameter(new SqlParameter("ID_GRUPO_EMPRESARIAL",
                    java.sql.Types.INTEGER));
        	compile();
        }
        
        /**
         * Regresa el arreglo de valores para el registro especificado.
         *
         * @param rs El resultSet.
         * @param rowNum El n&uacute;mero de rengl&oacute;n.
         * @return Object[].
         * @throws SQLException Si ocurre un error.
         */
        protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        	return new Object[] {new Integer(rs.getInt(1))};
        }
    }
    
	 /**
     * Obtiene los ids de los ejecutivos para las lineas de credito de
     * determinado grupo empresarial.
     */
    private static String QUERY_EJECUTIVOS = "SELECT DISTINCT ce.id_persona, pcr.no_cuenta, " +
             "p.id_persona FROM bup_cuenta_ejecutivo ce, bup_contrato_sica cs, " +
             "bup_cuenta_contrato cc, bup_persona_cuenta_rol pcr, bup_persona_moral pm, " +
             "bup_persona p " +
             "WHERE cs.ID_GRUPO_EMPRESARIAL = ? AND ce.status = 'VIG' " +
             "AND cs.no_cuenta = ce.no_cuenta AND cc.no_cuenta = cs.no_cuenta " +
             "AND cc.status_origen = 'Active' AND cc.status = 'ACTIVA' AND " +
             "pcr.no_cuenta = cs.no_cuenta AND pcr.status = 'VIG' AND pcr.id_persona = " +
             "pm.id_persona AND pm.id_persona = p.id_persona";
	
    /**
     * Query para inactivar los contratos que no han
     */
    private static String UPDATE_CONTRATOS_SICA_SIN_DEAL = "" +
            "UPDATE bup_cuenta_contrato t1 " +
            "SET t1.STATUS_ORIGEN = 'Closed', t1.STATUS = 'CERRADA', t1.FECHA_ULT_MOD = SYSDATE, t1.USUARIO_ULT_MOD = 'SICA' " +
            "WHERE " +
            "    t1.NO_CUENTA IN ( " +
            "        SELECT t2.NO_CUENTA FROM BUP_CONTRATO_SICA t2 " +
            "        WHERE NOT EXISTS ( " +
            "            SELECT DISTINCT t2.NO_CUENTA FROM SC_DEAL t3 " +
            "            WHERE t2.NO_CUENTA = t3.NO_CUENTA AND " +
            "            t3.FECHA_CAPTURA >= SYSDATE - @ " +
            "        ) " +
            "    )";
}
