/*
 * $Id: JdbcReporteUtilidadesDaoImpl.java,v 1.3.84.1 2015/10/15 17:59:15 mejiar Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2008 LegoSoft S.C.
 */

package com.ixe.ods.sica.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.object.MappingSqlQuery;

/**
 * Implementaci&oacute;n JDBC para la interfaz ReporteUtilidadesDao.
 *
 * @author Jean C. Favila
 * @version $Revision: 1.3.84.1 $ $Date: 2015/10/15 17:59:15 $
 * @see com.ixe.ods.sica.dao.ReporteUtilidadesDao
 */
public class JdbcReporteUtilidadesDaoImpl extends JdbcDaoSupport implements ReporteUtilidadesDao {

    /**
     * Constructor por default. No hace nada.
     */
    public JdbcReporteUtilidadesDaoImpl() {
        super();
    }

    /**
     * @param de La fecha de inicio de la busqueda.
     * @param hasta La fecha de fin de la busqueda.
     * @param idPersona El id persona del promotor seleccionado.
     * @param idDivisa El id de la divisa seleccionada.
     * @param idCanal El id del canal seleccionado.
     * @param idsPromotores La lista de n&uacute;meros de Promotor (Opcional).
     * @return List
     * @see ReporteUtilidadesDao#findDetallesUtilidades(java.util.Date, java.util.Date, Integer,
     *          String, String, java.util.List)
     */
    public List findDetallesUtilidades(Date de, Date hasta, Integer idPersona, String idDivisa,
                                       String idCanal, List idsPromotores) {
        return findDetallesUtilidades(de, hasta, idPersona, idDivisa, idCanal, idsPromotores,
                false, null, false);
    }

    /**
     * @param de La fecha de inicio de la busqueda.
     * @param hasta La fecha de fin de la busqueda.
     * @param idCanal El id del canal seleccionado.
     * @param mnemonicos La lista de mnem&oacute;nicos a aplicar.
     * @param sucursales True para sucurales, false para sucursal virtual.
     * @return List
     * @see ReporteUtilidadesDao#findDetallesUtilidadesIxeDirecto(java.util.Date, java.util.Date,
     *      String, java.util.List, boolean).
     */ 
    public List findDetallesUtilidadesIxeDirecto(Date de, Date hasta, String idCanal,
                                                 List mnemonicos, boolean sucursales) {
        return findDetallesUtilidades(de, hasta, null, null, idCanal, null, true, mnemonicos,
                sucursales);
    }

    /**
     * @param de La fecha de inicio de la busqueda.
     * @param hasta La fecha de fin de la busqueda.
     * @param idPersona El id persona del promotor seleccionado.
     * @param idDivisa El id de la divisa seleccionada.
     * @param idCanal El id del canal seleccionado.
     * @param idsPromotores La lista de n&uacute;meros de Promotor (Opcional).
     * @param ixeDirecto Si es de IxeDirecto o Promoci&oacute;n.
     * @param mnemonicos La lista de mnem&oacute;nicos a restringir.
     * @param sucursales En caso de IxeDirecto, true para sucurales, false para sucursal virtual.
     * @return List
     */
    public List findDetallesUtilidades(Date de, Date hasta, Integer idPersona, String idDivisa,
                                       String idCanal, List idsPromotores, boolean ixeDirecto,
                                       List mnemonicos, boolean sucursales) {
        StringBuffer sb = new StringBuffer(ixeDirecto ?
                REPORTE_UTILIDADES_IXE_DIR_QUERY : REPORTE_UTILIDADES_QUERY);
        if (ixeDirecto) {
            sb = sb.append(sucursales ? " IN (" : " NOT IN (");
            int i = 0;
            for (Iterator it = mnemonicos.iterator(); it.hasNext(); i++) {
                sb = sb.append("'").append(it.next().toString());
                if (i < mnemonicos.size() - 1) {
                    sb = sb.append("',");
                }
                else {
                    sb = sb.append("')");
                }
            }
        }
        if (idPersona != null) {
            sb = sb.append(" AND d.id_promotor = ").append(idPersona.toString());
        }
        if (StringUtils.isNotEmpty(idDivisa)) {
    		sb = sb.append(" AND dp.id_divisa = '").append(idDivisa).append("' ");
    	}
    	if (StringUtils.isNotEmpty(idCanal)) {
    		sb = sb.append(" AND d.id_canal = '").append(idCanal).append("' ");
    	}
        if (idsPromotores != null && !idsPromotores.isEmpty()) {
            sb = sb.append(" AND d.id_promotor in (").append(getCadenaIdsPromotores(idsPromotores)).
                    append(") ");
        }
        sb = sb.append(" ORDER BY d.id_promotor, dp.id_divisa, d.id_deal");
        return new ReporteUtilidadesQuery(getDataSource(), sb.toString(), sucursales).
                execute(new Object[]{de, hasta});
    }

    /**
     * Regresa la cadena con los id promotor separados por comas.
     *
     * @param idsPromotores La lista de n&uacute;meros de Promotor.
     * @return String.
     */
    private String getCadenaIdsPromotores(List idsPromotores) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < idsPromotores.size(); i++) {
            if (i > 0) {
                sb = sb.append(",");
            }
            sb = sb.append(idsPromotores.get(i).toString());
        }
        return sb.toString();
    }

    /**
     * Clase para ejecutar el query del Reporte de Utilidades.
     *
     * @author Jean C. Favila
     */
    class ReporteUtilidadesQuery extends MappingSqlQuery {

        /**
         * Constructor por default.
         *
         * @param dataSource El dataSource con el que se ejecutar&aacute; el query.
         * @param s La cadena del query a ejecutar.
         * @param sucursales True para sucursales, False para IxeDirecto.
         */
        ReporteUtilidadesQuery(DataSource dataSource, String s, boolean sucursales) {
            super(dataSource, s);
            super.declareParameter(new SqlParameter("fecha_inicio", Types.TIMESTAMP));
            super.declareParameter(new SqlParameter("fecha_fin", Types.TIMESTAMP));
            this.sucursales = sucursales;
            compile();
        }

        /**
         * Regresa un HashMap con las siguientes llaves: "idDealPosicion", "idCanal", "idPromotor",
         * "promotor", "divisa", "idDeal", "folioDetalle", "fechaCaptura", "recibimos",
         * "tipoCambio", "tipoCambioMesa", "claveFormaLiquidacion", "cliente", "monto",
         * "comisionCobradaUsd", "comisionOficialUsd", "idDivisa".
         *
         * @param rs El ResultSet a convertir.
         * @param i El n&uacute;mero de registro.
         * @return HashMap.
         * @throws SQLException Si no se puede convertir alguno de los valores.
         */
        protected Object mapRow(ResultSet rs, int i) throws SQLException {
            Map row = new HashMap();
            row.put("idDealPosicion", new Integer(rs.getInt(1)));
            row.put("idCanal", rs.getString(2));
            row.put("idPromotor", new Integer(rs.getInt(3)));
            row.put("promotor", rs.getString(4));
            row.put("divisa", rs.getString(5));
            row.put("idDeal", new Integer(rs.getInt(6)));
            row.put("folioDetalle", new Integer(rs.getInt(7)));
            row.put("fechaCaptura", rs.getDate(8));
            row.put("recibimos", "S".equals(rs.getString(9)) ? Boolean.TRUE : Boolean.FALSE);
            row.put("tipoCambio", new Double(rs.getDouble(10)));
            row.put("tipoCambioMesa", new Double(rs.getDouble(11)));
            row.put("claveFormaLiquidacion", rs.getString(12));
            row.put("cliente", rs.getString(13));
            row.put("monto", new Double(rs.getDouble(14)));
            row.put("comisionCobradaUsd", new Double(rs.getDouble(15)));
            row.put("comisionOficialUsd", new Double(rs.getDouble(16)));
            row.put("idDivisa", rs.getString(17));
            if (sucursales) {
                row.put("idSucursal", new Integer(rs.getInt(18)));
                row.put("nombreSucursal", rs.getString(19));
            }
            return row;
        }

        /**
         * Si es para sucursales o Ixe Directo.
         */
        private boolean sucursales;
    }

    /**
     * La constante con el query b&aacute;sico.
     */
    private static String REPORTE_UTILIDADES_QUERY = "SELECT distinct dd.id_deal_posicion, " +
            "d.id_canal canal, d.id_promotor idPromotor, pf.nombre||pf.paterno||pf.materno " +
            "promotor,div.descripcion divisa, d.id_deal idDeal, " +
            "dd.folio_detalle folioDetalle, d.fecha_captura fechaCaptura, dp.recibimos, " +
            "dp.tipo_cambio tipoCambio, dd.tipo_cambio_mesa tipoCambioMesa, " +
            "dd.clave_forma_liquidacion claveFormaLiquidacion, p.nombre_corto cliente, " +
            "dp.monto, dd.comision_cobrada_usd, dd.comision_oficial_usd, dp.id_divisa idDivisa " +
            "FROM sc_deal_detalle dd, sc_deal_posicion dp, sc_deal d, sc_divisa div, " +
            "bup_persona_fisica pf, bup_persona_cuenta_rol pcr, bup_persona p " +
            "WHERE dd.id_deal_posicion = dp.id_deal_posicion AND dp.id_divisa = div.id_divisa " +
            "AND dd.id_deal = d.id_deal AND d.id_promotor = pf.id_persona AND d.no_cuenta = " +
            "pcr.no_cuenta AND pcr.id_rol = 'TIT' AND pcr.status = 'VIG' AND pcr.id_persona = " +
            "p.id_persona AND d.status_deal != 'CA' AND dd.status_detalle_deal != 'CA' AND " +
            "dp.id_divisa != 'MXN' AND d.id_canal != 'DIR' AND fecha_captura BETWEEN ? AND ?";

    /**
     * La constante con el query b&aacute;sico.
     */
    private static String REPORTE_UTILIDADES_IXE_DIR_QUERY = "SELECT distinct " +
            "dd.id_deal_posicion, d.id_canal canal, d.id_promotor idPromotor, " +
            "pf.nombre||pf.paterno||pf.materno promotor, div.descripcion divisa, " +
            "d.id_deal idDeal, dd.folio_detalle folioDetalle, d.fecha_captura fechaCaptura, " +
            "dp.recibimos, dp.tipo_cambio tipoCambio, dd.tipo_cambio_mesa tipoCambioMesa, " +
            "dd.clave_forma_liquidacion claveFormaLiquidacion, p.nombre_corto cliente, dp.monto, " +
            "dd.comision_cobrada_usd, dd.comision_oficial_usd, dp.id_divisa idDivisa, " +
            "s.id_sucursal, s.nombre  FROM sc_deal_detalle dd, sc_deal_posicion dp, sc_deal d, " +
            "sc_divisa div, bup_persona_fisica pf, bup_persona_cuenta_rol pcr, bup_persona p, " +
            "bup_cuenta_contrato cc, bup_sucursal s WHERE dd.id_deal_posicion = " +
            "dp.id_deal_posicion AND dp.id_divisa = div.id_divisa AND " +
            "dd.id_deal = d.id_deal AND d.id_promotor = pf.id_persona AND pcr.no_cuenta = " +
            "cc.no_cuenta AND cc.id_sucursal = s.id_sucursal(+) AND d.no_cuenta = pcr.no_cuenta " +
            "AND pcr.id_rol = 'TIT' AND pcr.status = 'VIG' AND pcr.id_persona = p.id_persona AND " +
            "d.status_deal != 'CA' AND dd.status_detalle_deal != 'CA' AND dp.id_divisa != 'MXN' " +
            "AND fecha_captura BETWEEN ? AND ? AND div.tipo != 'M' AND " +
            "dd.mnemonico ";
}
