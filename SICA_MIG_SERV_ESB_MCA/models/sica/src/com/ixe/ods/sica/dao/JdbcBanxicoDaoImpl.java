/* 
 * $Id: JdbcBanxicoDaoImpl.java,v 1.7.42.1 2011/04/25 23:46:59 galiciad Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 - 2007 LegoSoft S.C.
 */
package com.ixe.ods.sica.dao;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.object.MappingSqlQuery;

/**
 * Implementacion de los m&acute;todos para la busqueda de detalles de deal
 * para el Reporte de Banxico.
 * 
 * @author Gustavo Gonzalez (ggonzalez)
 * @version $Revision: 1.7.42.1 $ $Date: 2011/04/25 23:46:59 $
 */
public class JdbcBanxicoDaoImpl extends JdbcDaoSupport implements BanxicoDao {

	 /**
     * Constructor vac&iacute;o.
     */
    public JdbcBanxicoDaoImpl() {
		super();
	}
    
    /**
     * @see BanxicoDao#findDetallesIncluyeStatusAlta(Date, Date).
     */
    public List findDetallesIncluyeStatusAlta(Date incioDia, Date finDia) {
    	return new ParametrosQuery(getDataSource(), false, true).execute(new Object[]{incioDia, finDia});
    }
    
    /**
     * @see BanxicoDao#findDetallesExcluyeStatusAlta(Date, Date).
     */
    public List findDetallesExcluyeStatusAlta(Date incioDia, Date finDia) {
    	return new ParametrosQuery(getDataSource(), false, false).execute(new Object[]{incioDia, finDia});
    }
	
    /**
     * @see BanxicoDao#findDetallesForwards(Integer, Date, Date).
     */
    public List findDetallesForwards(Integer idPersonaForward, Date incioDia, Date finDia) {
    	return new ParametrosQuery(getDataSource(), true, false).execute(new Object[]{idPersonaForward, incioDia, finDia});
    }
     
    /**
     * @see BanxicoDao#findMontoTotalResmerca(Date, Date, String, String, String, String, String). 
     */
    public List findMontoTotalResmerca(Date inicioDia, Date finDia, Integer idPersonaIxeForward, String idDivisa, String sectorCasasDeBolsa,
			String sectorEmpresas, String sectorOtrosIntermediarios, String recibimos) {
    	return new ParametrosQueryResmerca(getDataSource()).execute(new Object[]{inicioDia, finDia, idPersonaIxeForward, idDivisa, sectorCasasDeBolsa,
    			sectorEmpresas, sectorOtrosIntermediarios, recibimos});
    }
    
    /**
     * @see  BanxicoDao#findMontoForwardsEstrategia(Date, Date, String, Integer, String).
     */
    public List findMontoForwardsEstrategia(Date inicioDia, Date finDia, String idDivisa, Integer idPersonaForward,
			String recibimos){
    	return new ParametrosForwardsEstrategia(getDataSource()).execute(new Object[]{inicioDia, finDia, idDivisa, idPersonaForward, recibimos});
    }
    
    /**
     * @see  BanxicoDao#findMontoTotalesResmerca(Date, Date, Integer, String, String).
     */
    public List findMontoTotalesResmerca(Date inicioDia, Date finDia, Integer idPersonaForward, String idDivisa, 
			String recibimos){
    	return new ParametrosTotalesResmerca(getDataSource()).execute(new Object[]{inicioDia, finDia, idPersonaForward, idDivisa, recibimos});
    }
    
    /**
     * @see BanxicoDao#findMontoForwardsEstrategiaSectores(Date, Date, String, Integer).
     */
    public List findMontoForwardsEstrategiaSectores(Date inicioDia, Date finDia, String recibimos, Integer idPersonaForward, String idDivisa) {
    	return new ParametrosTotalesFwEstraSectores(getDataSource()).execute(new Object[]{inicioDia, finDia, recibimos, idPersonaForward, idDivisa});
    }
    
 	/**
     * Clase interna para accesar a las propiedades de los registros obtenidos
     * para el reporte de Banxico.
     * 
     * @author Gustavo Gonzalez (ggonzalez)
     *
     */
    class ParametrosQuery extends MappingSqlQuery {

    	/**
    	 * Constructor de la clase.
    	 * 
    	 *  @param ds El dataSource.
    	 */
        public ParametrosQuery(DataSource ds, boolean conForwards, boolean incluyeAlta) {
            super(ds, conForwards ? QUERY_DET_INC_ALTA : incluyeAlta ? QUERY_DET_INC_ALTA : QUERY_DET_EXC_ALTA );
            if (!conForwards) {
            	super.declareParameter(new SqlParameter("FECHA_CAPTURA", java.sql.Types.DATE));
                super.declareParameter(new SqlParameter("FECHA_CAPTURA", java.sql.Types.DATE));
            }
            else {
            	super.declareParameter(new SqlParameter("ID_PERSONA", java.sql.Types.INTEGER));
            	super.declareParameter(new SqlParameter("FECHA_CAPTURA", java.sql.Types.DATE));
                super.declareParameter(new SqlParameter("FECHA_CAPTURA", java.sql.Types.DATE));
            }
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
        	return new Object[] { new Integer(rs.getInt(1)), new Integer(rs.getInt(2)), 
        			rs.getString(3) != null ? rs.getString(3) : "",
            		rs.getDate(4), 
            		rs.getString(5) != null ? rs.getString(5) : "", 
            		rs.getString(6) != null ? rs.getString(6) : "", 
            		rs.getString(7) != null ? rs.getString(7) : "" ,
            		new Double(rs.getDouble(8)), 
            		rs.getString(9) != null ? rs.getString(9) : "", new Double(rs.getDouble(10)),
            		new Double(rs.getDouble(11)), 
            		rs.getString(12) != null ? rs.getString(12) : "" , 
       				rs.getString(13) != null ? rs.getString(13) : "", 
       				rs.getString(14) != null ? rs.getString(14) : "" ,
    				rs.getString(15) != null ? rs.getString(15) : "" , 
    				rs.getString(16) != null ? rs.getString(16) : "EL DEAL NO TIENE NOMBRE DE ORIGEN",
            		rs.getString(17) != null ? rs.getString(17) : "EL CLIENTE NO TIENE SECTOR" , 
            		rs.getString(18) != null ? rs.getString(18) : "EL CLIENTE NO TIENE SECTOR" , 
            		rs.getString(19) != null ? rs.getString(19) : "0", 
            		new Integer(rs.getString(20) != null ? rs.getString(20) : "0"),
            		rs.getString(21) != null ? rs.getString(21) : "0", new Integer(rs.getInt(22)),
            		rs.getString(23) != null ? rs.getString(23) : "EL CLIENTE NO TIENE SECTOR" , rs.getDate(24), 
            		rs.getString(25) != null ? rs.getString(25) : "EL CLIENTE NO TIENE SECTOR",
            		rs.getString(26) != null ? rs.getString(26) : ""};
        }
    }
    
    /**
     * Clase interna para accesar a las propiedades de los registros obtenidos
     * para los montos de los sectores del Resmerca para el reporte de Banxico.
     * 
     * @author Gustavo Gonzalez (ggonzalez)
     *
     */
    class ParametrosQueryResmerca extends MappingSqlQuery {

        /**
    	 * Constructor de la clase.
    	 * 
    	 *  @param ds El dataSource.
    	 */
        public ParametrosQueryResmerca(DataSource ds) {
        	super(ds, QUERY_MTO_RESMERCA);
        	super.declareParameter(new SqlParameter("FECHA_CAPTURA", java.sql.Types.DATE));
        	super.declareParameter(new SqlParameter("FECHA_CAPTURA", java.sql.Types.DATE));
        	super.declareParameter(new SqlParameter("ID_PERSONA", java.sql.Types.INTEGER));
        	super.declareParameter(new SqlParameter("ID_DIVISA", java.sql.Types.VARCHAR));
        	super.declareParameter(new SqlParameter("DESC_SECTOR_BANXICO", java.sql.Types.VARCHAR));
        	super.declareParameter(new SqlParameter("DESC_SECTOR_BANXICO", java.sql.Types.VARCHAR));
        	super.declareParameter(new SqlParameter("DESC_SECTOR_BANXICO", java.sql.Types.VARCHAR));
        	super.declareParameter(new SqlParameter("RECIBIMOS", java.sql.Types.VARCHAR));
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
        	return new Object[] {new Double(rs.getDouble(1)) != null ? new Double(rs.getDouble(1)) : new Double(0)};
        }
    
    }
    
    /**
     * Clase interna para accesar a las propiedades de los registros obtenidos
     * para los montos de los sectores del Resmerca para el reporte de Banxico.
     * 
     * @author Gustavo Gonzalez (ggonzalez)
     *
     */
    class ParametrosForwardsEstrategia extends MappingSqlQuery {

        /**
    	 * Constructor de la clase.
    	 * 
    	 *  @param ds El dataSource.
    	 */
        public ParametrosForwardsEstrategia(DataSource ds) {
        	super(ds, QUERY_MTO_FORWARDS_ESTRA);
        	super.declareParameter(new SqlParameter("FECHA_CAPTURA", java.sql.Types.DATE));
        	super.declareParameter(new SqlParameter("FECHA_CAPTURA", java.sql.Types.DATE));
        	super.declareParameter(new SqlParameter("ID_DIVISA", java.sql.Types.VARCHAR));
        	super.declareParameter(new SqlParameter("ID_PERSONA", java.sql.Types.INTEGER));
        	super.declareParameter(new SqlParameter("RECIBIMOS", java.sql.Types.VARCHAR));
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
        	return new Object[] {new Double(rs.getDouble(1)) != null ? new Double(rs.getDouble(1)) : new Double(0)};
        }
    
    }
    
    /**
     * Clase interna para accesar a las propiedades de los registros obtenidos
     * para los montos de los sectores del Resmerca para el reporte de Banxico.
     * 
     * @author Gustavo Gonzalez (ggonzalez)
     *
     */
    class ParametrosTotalesResmerca extends MappingSqlQuery {

        /**
    	 * Constructor de la clase.
    	 * 
    	 *  @param ds El dataSource.
    	 */
        public ParametrosTotalesResmerca(DataSource ds) {
        	super(ds, QUERY_MTO_TOT_RESMERCA);
        	super.declareParameter(new SqlParameter("FECHA_CAPTURA", java.sql.Types.DATE));
        	super.declareParameter(new SqlParameter("FECHA_CAPTURA", java.sql.Types.DATE));
        	super.declareParameter(new SqlParameter("ID_PERSONA", java.sql.Types.INTEGER));
        	super.declareParameter(new SqlParameter("ID_DIVISA", java.sql.Types.VARCHAR));
        	super.declareParameter(new SqlParameter("RECIBIMOS", java.sql.Types.VARCHAR));
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
        	return new Object[] {new Double(rs.getDouble(1)) != null ? new Double(rs.getDouble(1)) : new Double(0)};
        }
    
    }
    
    /**
     * Clase interna para accesar a las propiedades de los registros obtenidos
     * para los montos por sectores de deals interbancarios de estrategia.
     * 
     * @author Gustavo Gonzalez (ggonzalez)
     *
     */
    class ParametrosTotalesFwEstraSectores extends MappingSqlQuery {

        /**
    	 * Constructor de la clase.
    	 * 
    	 *  @param ds El dataSource.
    	 */
        public ParametrosTotalesFwEstraSectores(DataSource ds) {
        	super(ds, QUERY_MTO_FORWARDS_ESTRA_SECTORES);
        	super.declareParameter(new SqlParameter("FECHA_CAPTURA", java.sql.Types.DATE));
        	super.declareParameter(new SqlParameter("FECHA_CAPTURA", java.sql.Types.DATE));
        	super.declareParameter(new SqlParameter("RECIBIMOS", java.sql.Types.VARCHAR));
        	super.declareParameter(new SqlParameter("ID_PERSONA", java.sql.Types.INTEGER));
        	super.declareParameter(new SqlParameter("ID_DIVISA", java.sql.Types.VARCHAR));
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
        	return new Object[] {rs.getString(1) != null ? rs.getString(1) : "",
        			rs.getString(2) != null ? "Compra".equals(rs.getString(1)) ? "Venta" : "Compra" : "",
        			new Double(rs.getDouble(3)) != null ? new Double(rs.getDouble(3)) : new Double(0)};
        }
    
    }
    
    /**
     * Constante para el valor de QUERY_DET_INC_ALTA.
     */
    private static String QUERY_DET_INC_ALTA = "SELECT deal.ID_DEAL, deal_det.FOLIO_DETALLE, deal.TIPO_VALOR, deal.FECHA_LIQUIDACION, "  
    		+ "DECODE(deal_pos.recibimos, 'S', 'Compra', 'Venta'), cte.NOMBRE_CORTO, deal_det.clave_forma_liquidacion, " 
    		+ "deal_pos.MONTO, deal_pos.ID_DIVISA, deal_pos.tipo_cambio,  deal_pos.MONTO * deal_pos.tipo_cambio, " 
    		+ "prom.NOMBRE||' '||prom.PATERNO||' '||prom.MATERNO, CASE WHEN INSTR(deal_det.EVENTOS_DETALLE_DEAL, 'P') > 0 THEN 'Sí' else 'No' end case, "
    		+ "cta_cont.no_cuenta, deal_det.status_detalle_deal, mesa.nombre, sec_eco.DESCRIPCION, sec_eco.DESC_SECTOR_BANXICO, deal_det.FACTOR_DIVISA, "
    		+ "cte.ID_PERSONA, deal.id_folio_swap, sec_eco.ID_SECTOR, sec_eco.SECTOR_BANXICO, deal.FECHA_CAPTURA, sec_eco.CVE_CONTRAPARTE_AGPDA , swap.tipo_swap "
    		+ "FROM SC_DEAL deal, SC_DEAL_DETALLE deal_det, SC_DEAL_POSICION deal_pos, BUP_CUENTA_CONTRATO cta_cont, "
    		+ "BUP_PERSONA_RIM pers_rim, BUP_PERSONA cte, BUP_SECTOR_ECONOMICO sec_eco, BUP_PERSONA_FISICA prom, SC_MESA_CAMBIO mesa, SC_SWAP swap "
    		+ "WHERE deal.ID_DEAL = deal_det.ID_DEAL "
    		+ "AND deal_det.ID_DEAL_POSICION = deal_pos.ID_DEAL_POSICION "
    		+ "AND deal.NO_CUENTA = cta_cont.NO_CUENTA (+) "
    		+ "AND cta_cont.RIM_NO = pers_rim.RIM_NO "
    		+ "AND pers_rim.ID_PERSONA = cte.ID_PERSONA "
    		+ "AND cte.ID_SECTOR = sec_eco.ID_SECTOR (+) "
    		+ "AND deal.id_folio_swap = swap.id_folio_swap (+) "
    		+ "AND deal.FECHA_CAPTURA BETWEEN ? and ? "
    		+ "AND deal_det.STATUS_DETALLE_DEAL != 'CA' "
    		+ "AND deal.ID_PROMOTOR = prom.ID_PERSONA (+) "
    		+ "AND mesa.ID_MESA_CAMBIO = deal_pos.id_mesa_cambio "
    		+ "AND deal_pos.id_divisa != 'MXN' "
    		+ "AND deal_pos.id_divisa NOT LIKE 'X%' "
    		+ "ORDER BY deal_pos.id_divisa, deal_pos.RECIBIMOS, sec_eco.DESC_SECTOR_BANXICO";
    		   
	/**
     * Constante para el valor de QUERY_DET_EXC_ALTA.
     */
    private static String QUERY_DET_EXC_ALTA = "SELECT deal.ID_DEAL, deal_det.FOLIO_DETALLE, deal.TIPO_VALOR, deal.FECHA_LIQUIDACION, "  
    		+ "DECODE(deal_pos.recibimos, 'S', 'Compra', 'Venta'), cte.NOMBRE_CORTO, deal_det.clave_forma_liquidacion, " 
    		+ "deal_pos.MONTO, deal_pos.ID_DIVISA, deal_pos.tipo_cambio,  deal_pos.MONTO * deal_pos.tipo_cambio, " 
    		+ "prom.NOMBRE||' '||prom.PATERNO||' '||prom.MATERNO, CASE WHEN INSTR(deal_det.EVENTOS_DETALLE_DEAL, 'P') > 0 THEN 'Sí' else 'No' end case, "
    		+ "cta_cont.no_cuenta, deal_det.status_detalle_deal, mesa.nombre, sec_eco.DESCRIPCION, sec_eco.DESC_SECTOR_BANXICO, deal_det.FACTOR_DIVISA, "
    		+ "cte.ID_PERSONA, deal.id_folio_swap, sec_eco.ID_SECTOR, sec_eco.SECTOR_BANXICO, deal.FECHA_CAPTURA, sec_eco.CVE_CONTRAPARTE_AGPDA, swap.tipo_swap "
    		+ "FROM SC_DEAL deal, SC_DEAL_DETALLE deal_det, SC_DEAL_POSICION deal_pos, BUP_CUENTA_CONTRATO cta_cont, "
    		+ "BUP_PERSONA_RIM pers_rim, BUP_PERSONA cte, BUP_SECTOR_ECONOMICO sec_eco, BUP_PERSONA_FISICA prom, SC_MESA_CAMBIO mesa, SC_SWAP swap "
    		+ "WHERE deal.ID_DEAL = deal_det.ID_DEAL "
    		+ "AND deal_det.ID_DEAL_POSICION = deal_pos.ID_DEAL_POSICION "
    		+ "AND deal.NO_CUENTA = cta_cont.NO_CUENTA (+) "
    		+ "AND cta_cont.RIM_NO = pers_rim.RIM_NO "
    		+ "AND pers_rim.ID_PERSONA = cte.ID_PERSONA "
    		+ "AND cte.ID_SECTOR = sec_eco.ID_SECTOR (+) "
    		+ "AND deal.id_folio_swap = swap.id_folio_swap (+) "
    		+ "AND deal.FECHA_CAPTURA BETWEEN ? and ? "
    		+ "AND deal_det.STATUS_DETALLE_DEAL != 'CA' "
    		+ "AND deal_det.STATUS_DETALLE_DEAL != 'AL' "
    		+ "AND deal.ID_PROMOTOR = prom.ID_PERSONA (+) "
    		+ "AND mesa.ID_MESA_CAMBIO = deal_pos.id_mesa_cambio "
    		+ "AND deal_pos.id_divisa != 'MXN' "
    		+ "AND deal_pos.id_divisa NOT LIKE 'X%' "
    		+ "ORDER BY deal_pos.id_divisa, deal_pos.RECIBIMOS, sec_eco.DESC_SECTOR_BANXICO";
    
	/**
     * Constante para el valor de QUERY_DET_FWD.
     */
    private static String QUERY_DET_FWD = "SELECT SUM(deal_pos.MONTO) FROM SC_DEAL deal, SC_DEAL_DETALLE deal_det, SC_DEAL_POSICION deal_pos, BUP_CUENTA_CONTRATO cta_cont, "
    		+ "BUP_PERSONA_RIM pers_rim, BUP_PERSONA cte, BUP_SECTOR_ECONOMICO sec_eco, BUP_PERSONA_FISICA prom, SC_MESA_CAMBIO mesa "
    		+ "WHERE deal.ID_DEAL = deal_det.ID_DEAL "
    		+ "AND deal_det.ID_DEAL_POSICION = deal_pos.ID_DEAL_POSICION "
    		+ "AND deal.NO_CUENTA = cta_cont.NO_CUENTA (+) "
    		+ "AND cta_cont.RIM_NO = pers_rim.RIM_NO "
    		+ "AND pers_rim.ID_PERSONA = cte.ID_PERSONA "
    		+ "AND cte.ID_PERSONA = ? "
    		+"AND deal.id_folio_swap is not null "
    		+ "AND cte.ID_SECTOR = sec_eco.ID_SECTOR (+) "
    		+ "AND deal.FECHA_CAPTURA between ? and ? "
    		+ "AND deal_det.STATUS_DETALLE_DEAL != 'CA' "
    		+ "AND deal.ID_PROMOTOR = prom.ID_PERSONA (+) "
    		+ "AND mesa.ID_MESA_CAMBIO = deal_pos.id_mesa_cambio "
    		+ "AND deal_pos.id_divisa != 'MXN' "
    		+ "AND deal_pos.id_divisa NOT LIKE 'X%' "
    		+ "ORDER BY deal_pos.id_divisa, deal_pos.RECIBIMOS, sec_eco.DESC_SECTOR_BANXICO";
    
    /**
     * Constante para el valor de QUERY_MTO_RESMERCA.
     */
    private static String QUERY_MTO_RESMERCA = "SELECT SUM(deal_pos.MONTO) FROM SC_DEAL deal, SC_DEAL_DETALLE deal_det, SC_DEAL_POSICION deal_pos, BUP_CUENTA_CONTRATO cta_cont, " 
    		+ "BUP_PERSONA_RIM pers_rim, BUP_PERSONA cte, BUP_SECTOR_ECONOMICO sec_eco, BUP_PERSONA_FISICA prom, SC_MESA_CAMBIO mesa "
    		+ "WHERE deal.ID_DEAL = deal_det.ID_DEAL "
    		+ "AND deal_det.ID_DEAL_POSICION = deal_pos.ID_DEAL_POSICION " 
    		+ "AND deal.NO_CUENTA = cta_cont.NO_CUENTA (+) "
    		+ "AND cta_cont.RIM_NO = pers_rim.RIM_NO " 
    		+ "AND pers_rim.ID_PERSONA = cte.ID_PERSONA " 
    		+ "AND cte.ID_SECTOR = sec_eco.ID_SECTOR (+) "
    		+ "AND deal.FECHA_CAPTURA BETWEEN ? AND ? "
    		+ "AND deal_det.STATUS_DETALLE_DEAL != 'CA' " 
    		+ "AND deal.ID_PROMOTOR = prom.ID_PERSONA (+) " 
    		+ "AND cte.ID_PERSONA  != ? "
            + "AND mesa.ID_MESA_CAMBIO = deal_pos.id_mesa_cambio " 
    		+ "AND deal_pos.ID_DIVISA = ? " 
    		+ "AND (sec_eco.DESC_SECTOR_BANXICO = ? "
    		+ "OR sec_eco.DESC_SECTOR_BANXICO = ? "
    		+ "OR sec_eco.DESC_SECTOR_BANXICO = ? ) "
    		+ "AND deal_pos.RECIBIMOS = ? "
    		+ "ORDER BY deal_pos.id_divisa, deal_pos.RECIBIMOS, sec_eco.DESC_SECTOR_BANXICO";
    
    /**
     * Constante para el valor de QUERY_MTO_FORWARDS_ESTRA.
     */
    private static String QUERY_MTO_FORWARDS_ESTRA = "SELECT SUM(deal_pos.MONTO) FROM SC_DEAL deal, SC_DEAL_DETALLE deal_det, SC_DEAL_POSICION deal_pos, BUP_CUENTA_CONTRATO cta_cont, " 
    		+ "BUP_PERSONA_RIM pers_rim, BUP_PERSONA cte, BUP_SECTOR_ECONOMICO sec_eco, BUP_PERSONA_FISICA prom, SC_MESA_CAMBIO mesa "
    		+ "WHERE deal.ID_DEAL = deal_det.ID_DEAL "
    		+ "AND deal_det.ID_DEAL_POSICION = deal_pos.ID_DEAL_POSICION " 
    		+ "AND deal.NO_CUENTA = cta_cont.NO_CUENTA (+) "
    		+ "AND cta_cont.RIM_NO = pers_rim.RIM_NO "
    		+ "AND pers_rim.ID_PERSONA = cte.ID_PERSONA " 
    		+ "AND cte.ID_SECTOR = sec_eco.ID_SECTOR (+)	" 
    		+ "AND deal.FECHA_CAPTURA BETWEEN ? and ? "
    		+ "AND deal_pos.ID_DIVISA = ? "
    		+ "AND deal_det.STATUS_DETALLE_DEAL != 'CA' "
    		+ "AND deal.ID_PROMOTOR = prom.ID_PERSONA (+) " 
    		+ "AND cte.ID_PERSONA  = ? "
    		+ "AND deal.id_folio_swap is not null "
    		+ "AND mesa.ID_MESA_CAMBIO = deal_pos.id_mesa_cambio " 
    		+ "AND deal_pos.recibimos = ? "
    		+ "AND deal_pos.id_divisa != 'MXN' "
    		+ "AND deal_pos.id_divisa NOT LIKE 'X%' "
    		+ "ORDER BY deal_pos.id_divisa, deal_pos.RECIBIMOS, sec_eco.DESC_SECTOR_BANXICO";
    
    /**
     * 
     */
    private static String QUERY_MTO_TOT_RESMERCA = "SELECT SUM(deal_pos.MONTO) FROM SC_DEAL deal, SC_DEAL_DETALLE deal_det, SC_DEAL_POSICION deal_pos, BUP_CUENTA_CONTRATO cta_cont, " 
    	+ "BUP_PERSONA_RIM pers_rim, BUP_PERSONA cte, BUP_SECTOR_ECONOMICO sec_eco, BUP_PERSONA_FISICA prom, SC_MESA_CAMBIO mesa "
    	+ "WHERE deal.ID_DEAL = deal_det.ID_DEAL " 
    	+ "AND deal_det.ID_DEAL_POSICION = deal_pos.ID_DEAL_POSICION " 
    	+ "AND deal.NO_CUENTA = cta_cont.NO_CUENTA (+) "
    	+ "AND cta_cont.RIM_NO = pers_rim.RIM_NO  "
    	+ "AND pers_rim.ID_PERSONA = cte.ID_PERSONA " 
    	+ "AND cte.ID_SECTOR = sec_eco.ID_SECTOR (+)  "
    	+ "AND deal.FECHA_CAPTURA BETWEEN ? AND ? "
    	+ "AND deal_det.STATUS_DETALLE_DEAL != 'CA' " 
    	+ "AND deal.ID_PROMOTOR = prom.ID_PERSONA (+) " 
    	+ "and cte.ID_PERSONA  != ? "
    	+ "AND mesa.ID_MESA_CAMBIO = deal_pos.id_mesa_cambio " 
    	+ "AND deal_pos.id_divisa = ? "
    	+ "AND deal_pos.recibimos = ? "
    	+ "AND deal_pos.id_divisa != 'MXN' "
    	+ "AND deal_pos.id_divisa NOT LIKE 'X%' "
    	+ "ORDER BY deal_pos.id_divisa, deal_pos.RECIBIMOS, sec_eco.DESC_SECTOR_BANXICO ";

    /**
     * Obtiene el  monto por sector economico de los deals 
     * interbancarios que pertenecen a una estrategia y no
     * son de la contraparte Ixe Forward.
     */
    private static String QUERY_MTO_FORWARDS_ESTRA_SECTORES = "SELECT sec_eco.DESC_SECTOR_BANXICO, DECODE(deal_pos.recibimos, 'S', 'Compra', 'Venta'), SUM(deal_pos.MONTO) " 
    	+ "FROM SC_DEAL deal, SC_DEAL_DETALLE deal_det, SC_DEAL_POSICION deal_pos, BUP_CUENTA_CONTRATO cta_cont, " 
    	+ "BUP_PERSONA_RIM pers_rim, BUP_PERSONA cte, BUP_SECTOR_ECONOMICO sec_eco, BUP_PERSONA_FISICA prom, SC_MESA_CAMBIO mesa, SC_SWAP swap "
    	+ "WHERE deal.ID_DEAL = deal_det.ID_DEAL "
    	+ "AND deal_det.ID_DEAL_POSICION = deal_pos.ID_DEAL_POSICION " 
    	+ "AND deal.NO_CUENTA = cta_cont.NO_CUENTA (+) "
    	+ "AND cta_cont.RIM_NO = pers_rim.RIM_NO "
    	+ "AND pers_rim.ID_PERSONA = cte.ID_PERSONA " 
    	+ "AND cte.ID_SECTOR = sec_eco.ID_SECTOR (+) "
    	+ "AND deal.FECHA_CAPTURA BETWEEN ? and ? "
    	+ "AND deal_det.STATUS_DETALLE_DEAL != 'CA' "
    	+ "AND deal.ID_PROMOTOR = prom.ID_PERSONA (+) " 
    	+ "AND mesa.ID_MESA_CAMBIO = deal_pos.id_mesa_cambio "
    	+ "AND deal.id_folio_swap = swap.id_folio_swap (+) "
    	+ "AND swap.tipo_swap = 'E' "
    	+ "AND deal.simple = 'I' "
    	+ "AND deal_pos.id_divisa != 'MXN' "
    	+ "AND deal_pos.id_divisa NOT LIKE 'X%' " 
    	+ "AND deal_pos.recibimos = ? "
    	+ "AND cte.ID_PERSONA =  ? "
    	+ "AND deal_pos.id_divisa = ? "
    	+ "GROUP BY sec_eco.DESC_SECTOR_BANXICO, deal_pos.RECIBIMOS ";
      
}	























