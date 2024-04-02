/*
 * $Id: JdbcDescuadreDaoImpl.java,v 1.2 2008/02/22 18:25:32 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 - 2007 LegoSoft S.C.
 */
package com.ixe.ods.sica.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.object.MappingSqlQuery;

/**
 * Implementacion de los m&acute;todos para el dao que identifica si
 * existe un Descuadre de la Posicion.
 *
 * @author Gustavo Gonzalez (ggonzalez)
 * @version $Revision: 1.2 $ $Date: 2008/02/22 18:25:32 $
 */
public class JdbcDescuadreDaoImpl extends JdbcDaoSupport implements DescuadreDao {

	/**
	 * Constructor vacio.
	 *
	 */
	public JdbcDescuadreDaoImpl() {
		super();
	}

	/**
	 * @see DescuadreDao#findDescuadreDetallesDealsCancelados()
	 */
	public List findDescuadreDetallesDealsCancelados(){
		return new QueryDescuadre(getDataSource(), true).execute();
	}

	/**
	 * @see DescuadreDao#findDescuadreDetallesDealsNoCancelados()
	 */
	public List findDescuadreDetallesDealsNoCancelados(){
		return new QueryDescuadre(getDataSource(), false).execute();
	}

	/**
     * Clase interna para accesar a las propiedades de los registros obtenidos
     * para el reporte de Banxico.
     *
     * @author Gustavo Gonzalez (ggonzalez)
     *
     */
    class QueryDescuadre extends MappingSqlQuery {

    	 /**
    	  * Constructor de la Clase.
    	  *
    	  * @param ds El Data Source
    	  * @param cancelados Define si se buscan deals cancelados.
    	  */
    	 public QueryDescuadre(DataSource ds, boolean cancelados) {
    		 super(ds, cancelados ? QUERY_CANCELADOS : QUERY_NO_CANCELADOS);
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
         	return new Object[] {new Integer(rs.getInt(1)),
         			new Integer(rs.getInt(2)),
         			new Integer(rs.getInt(3)),
         			new Double(rs.getDouble(4)),
         			new Double(rs.getDouble(5)),
         			rs.getString(6) != null ? rs.getString(6) : "",
         			rs.getString(7) != null ? rs.getString(7) : "",
         			rs.getString(8) != null ? rs.getString(8) : "",
         			rs.getString(9) != null ? rs.getString(9) : "",
         			new Double(rs.getDouble(10)),
         			new Double(rs.getDouble(11))};
         }
    }

	/**
	 * Constante que define el Query para obtener los Detalles de Deals
	 * no cancelados que descuadran la posicion.
	 */
    private static String QUERY_NO_CANCELADOS= "select t1.id_deal, t1.folio_detalle, "
            + "t1.id_deal_posicion, t3.monto, (t3.monto * t3.tipo_cambio), t1.status_detalle_deal, "
            + "t3.recibimos, t3.id_divisa, t2.tipo_valor, t3.tipo_cambio, t1.tipo_cambio_mesa from "
            + "sc_deal_detalle t1, sc_deal t2, sc_deal_posicion t3 where "
            + "(t1.status_detalle_deal <> 'CA')  and (t3.monto <> (select sum(monto) from "
            + "sc_posicion_log t4 where t4.id_deal_posicion = t1.id_deal_posicion)) and "
            + "(t1.id_deal = t2.id_deal) and (t1.id_deal_posicion = t3.id_deal_posicion)";

	/**
	 * Constante que define el Query para obtener los Detalles de Deals
	 * cancelados que descuadran la posicion.
	 */
	private static String QUERY_CANCELADOS = "select t1.id_deal, t1.folio_detalle, "
            + "t1.id_deal_posicion, t3.monto, (t3.monto * t3.tipo_cambio), t1.status_detalle_deal, "
            + "t3.recibimos, t3.id_divisa, t2.tipo_valor, t3.tipo_cambio, t1.tipo_cambio_mesa from "
            + "sc_deal_detalle t1, sc_deal t2, sc_deal_posicion t3 where "
            + "(t1.status_detalle_deal = 'CA') and (0 <> (select sum(monto) from "
            + "sc_posicion_log t4 where t4.id_deal_posicion = t1.id_deal_posicion)) and "
            + "(t1.id_deal = t2.id_deal) and (t1.id_deal_posicion = t3.id_deal_posicion)";
}
