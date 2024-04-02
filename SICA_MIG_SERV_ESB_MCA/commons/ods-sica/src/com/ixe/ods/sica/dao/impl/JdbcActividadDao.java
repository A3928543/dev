/*
 * $Id: JdbcActividadDao.java,v 1.4.2.3.8.1 2010/10/08 01:19:08 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2009 LegoSoft S.C.
 */

package com.ixe.ods.sica.dao.impl;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.object.MappingSqlQuery;

import com.ixe.ods.sica.Constantes;
import com.ixe.ods.sica.Keys;
import com.ixe.ods.sica.Num;
import com.ixe.ods.sica.dao.ActividadDao;
import com.ixe.ods.sica.model.Actividad;
import com.ixe.ods.sica.model.Deal;
import com.ixe.ods.sica.model.DealDetalle;
import com.ixe.ods.sica.utils.MapUtils;
import com.ixe.ods.sica.vo.DealDetWorkitemVO;
import com.ixe.ods.sica.vo.DealWorkitemVO;
import com.ixe.ods.sica.vo.HorarioMontoWorkitemVO;
import com.ixe.ods.sica.vo.ModMontoWorkitemVO;
import com.ixe.ods.sica.vo.ModProductoWorkitemVO;

/**
 * Implementaci&oacute;n JDBC para el Data Access Object para la entidad SC_ACTIVIDAD.
 *
 * @author Jean C. Favila (jfavila)
 * @version $Revision: 1.4.2.3.8.1 $ $Date: 2010/10/08 01:19:08 $
 */
public class JdbcActividadDao extends JdbcDaoSupport implements ActividadDao {

    /**
     * Constructor por default.
     */
    public JdbcActividadDao() {
        super();
    }

    /**
     * Regresa una lista de objetos DealWorkitemVO y/o DealDetWorkitemVO que corresponden a las
     * actividades de autorizaci&oacute;n pendientes.
     * 
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param nombreActividad El nombre de la actividad a buscar (puede ser null).
     * @return List de DealWorkitemVO y/o DealDetWorkitemVO.
     */
    public List getWorkitems(String ticket, String nombreActividad) {
        String query = (nombreActividad == null ? ACTIVIDADES_PENDIENTES_QUERY :
                ACTIVIDADES_PENDIENTES_QUERY + " AND a.NOMBRE_ACTIVIDAD = '" + nombreActividad +
                        "'") + " ORDER BY d.ID_DEAL";
        List results = new ActividadesPendientesQuery(getDataSource(), query).execute();
        List mapas = new ArrayList();
        for (Iterator it = results.iterator(); it.hasNext();) {
            Map res = (Map) it.next();
            Map mapaActividad = getMapaParaActividad(
                    ((Integer) res.get(Keys.ID_ACTIVIDAD)).intValue(), mapas);
            if (mapaActividad == null) {
                mapaActividad = MapUtils.copiar(res);
                MapUtils.eliminarLlaves(mapaActividad, new String[]{Keys.ID_DEAL_DETALLE,
                        Keys.RECIBIMOS, Keys.CLAVE_FORMA_LIQUIDACION, Keys.ID_DIVISA, Keys.MONTO,
                        Keys.TIPO_CAMBIO, Keys.TIPO_CAMBIO_MESA});
                mapas.add(mapaActividad);
                mapaActividad.put(Keys.DETALLES, new ArrayList());
            }
            ((List) mapaActividad.get(Keys.DETALLES)).add(MapUtils.generar(
                    new String[]{Keys.ID_DEAL_DETALLE, Keys.RECIBIMOS,
                            Keys.CLAVE_FORMA_LIQUIDACION, Keys.ID_DIVISA,
                            Keys.MONTO, Keys.TIPO_CAMBIO,
                            Keys.TIPO_CAMBIO_MESA, Keys.STATUS_DETALLE_DEAL,
                            Keys.FOLIO_DETALLE, Keys.RESULTADO, Keys.CONTIENE_NETEO},
                    new Object[]{res.get(Keys.ID_DEAL_DETALLE), res.get(Keys.RECIBIMOS),
                            res.get(Keys.CLAVE_FORMA_LIQUIDACION), res.get(Keys.ID_DIVISA),
                            res.get(Keys.MONTO), res.get(Keys.TIPO_CAMBIO),
                            res.get(Keys.TIPO_CAMBIO_MESA), res.get(Keys.STATUS_DETALLE_DEAL),
                            res.get(Keys.FOLIO_DETALLE), res.get(Keys.RESULTADO),
                            res.get(Keys.CONTIENE_NETEO)
                    }));
        }
        return getResultados(mapas);
    }

    /**
     * Arma la lista de objetos DealWorkitemVO o DealDetWorkitemVO.
     *
     * @param actividades La lista de mapas que representan las actividades pendientes.
     * @return List de DealWorkitemVO y/o DealDetWorkitemVO.
     */
    private List getResultados(List actividades) {
        List resultados = new ArrayList();
        for (Iterator it = actividades.iterator(); it.hasNext();) {
            Map act = (Map) it.next();
            String nombreActividad = (String) act.get(Keys.ACTIVIDAD);
            String proceso = (String) act.get(Keys.PROCESO);
            boolean interbancario = "I".equals(act.get(Keys.SIMPLE));
            String datoAdicional = (String) (
                    Actividad.ACT_DN_EMAIL.equals(nombreActividad) ?
                            act.get(Keys.EMAIL_FACTURA_OTRO) :
                            Actividad.ACT_DN_RFC.equals(nombreActividad) ?
                                    act.get(Keys.RFC_FACTURA) : null);
            if (Actividad.PROC_HORARIO_MONTO.equals(proceso) ||
                    Actividad.PROC_CANCELACION_DET.equals(proceso)) {
                Map det = getMapaDetalleConId((Integer) act.get(Keys.ID_DEAL_POSICION),
                        (List) act.get(Keys.DETALLES));
                resultados.add(new HorarioMontoWorkitemVO(
                        ((Integer) act.get(Keys.ID_ACTIVIDAD)).intValue(),
                        nombreActividad, ((Integer) act.get(Keys.ID_DEAL)).intValue(),
                        ((Integer) det.get(Keys.FOLIO_DETALLE)).intValue(),
                        (String) act.get(Keys.FECHA_VALOR),
                        act.get(Keys.NOM_CLIENTE) != null ? (String) act.get(Keys.NOM_CLIENTE) : "",
                        act.get(Keys.NOM_PROMOTOR) != null ?
                                (String) act.get(Keys.NOM_PROMOTOR) : "",
                        act.get(Keys.NOM_USUARIO) != null ? (String) act.get(Keys.NOM_USUARIO) : "",
                        ((Boolean) det.get(Keys.RECIBIMOS)).booleanValue() ?
                                Constantes.COMPRA : Constantes.VENTA,
                        act.get(Keys.FECHA_CREACION) != null ? (String) act.get(Keys.FECHA_CREACION) : "",
                        (String) det.get(Keys.ID_DIVISA),
                        ((Double) det.get(Keys.MONTO)).doubleValue(),
                        ((Double) det.get(Keys.TIPO_CAMBIO)).doubleValue(),
                        ((Boolean) det.get(Keys.CONTIENE_NETEO)).booleanValue()));
            }
            else if (Actividad.ACT_DN_MOD_MONTO.equals(nombreActividad)) {
            	Map det = getMapaDetalleConId((Integer) act.get(Keys.ID_DEAL_POSICION),
                        (List) act.get(Keys.DETALLES));
                resultados.add(new ModMontoWorkitemVO(
                        ((Integer) act.get(Keys.ID_ACTIVIDAD)).intValue(),
                        nombreActividad, ((Integer) act.get(Keys.ID_DEAL)).intValue(),
                        ((Integer) det.get(Keys.FOLIO_DETALLE)).intValue(),
                        (String) act.get(Keys.FECHA_VALOR),
                        act.get(Keys.NOM_CLIENTE) != null ? (String) act.get(Keys.NOM_CLIENTE) : "",
                        act.get(Keys.NOM_PROMOTOR) != null ?
                                (String) act.get(Keys.NOM_PROMOTOR) : "",
                        act.get(Keys.NOM_USUARIO) != null ? (String) act.get(Keys.NOM_USUARIO) : "",
                        ((Boolean) det.get(Keys.RECIBIMOS)).booleanValue() ?
                                Constantes.COMPRA : Constantes.VENTA,
                        act.get(Keys.FECHA_CREACION) != null ? (String) act.get(Keys.FECHA_CREACION) : "",
                        (String) det.get(Keys.ID_DIVISA),
                        ((Double) det.get(Keys.MONTO)).doubleValue(),
                        ((Double) det.get(Keys.TIPO_CAMBIO_MESA)).doubleValue(),
                        ((Double) det.get(Keys.TIPO_CAMBIO)).doubleValue(), 
                        (Double.parseDouble((String) det.get(Keys.RESULTADO))),
                        ((Boolean) det.get(Keys.CONTIENE_NETEO)).booleanValue()));
            }
            else if (Actividad.ACT_DN_MOD_PROD.equals(nombreActividad)) {
            	Map det = getMapaDetalleConId((Integer) act.get(Keys.ID_DEAL_POSICION),
                        (List) act.get(Keys.DETALLES));
            	String[] valores = ((String)det.get(Keys.RESULTADO)).split("\\|");
                resultados.add(new ModProductoWorkitemVO(
                        ((Integer) act.get(Keys.ID_ACTIVIDAD)).intValue(),
                        nombreActividad, ((Integer) act.get(Keys.ID_DEAL)).intValue(),
                        ((Integer) act.get(Keys.ID_DEAL_POSICION)).intValue(),
                        ((Integer) det.get(Keys.FOLIO_DETALLE)).intValue(),
                        (String) act.get(Keys.FECHA_VALOR),
                        act.get(Keys.NOM_CLIENTE) != null ? (String) act.get(Keys.NOM_CLIENTE) : "",
                        act.get(Keys.NOM_PROMOTOR) != null ?
                                (String) act.get(Keys.NOM_PROMOTOR) : "",
                        act.get(Keys.NOM_USUARIO) != null ? (String) act.get(Keys.NOM_USUARIO) : "",
                        ((Boolean) det.get(Keys.RECIBIMOS)).booleanValue() ?
                                Constantes.COMPRA : Constantes.VENTA,
                        act.get(Keys.FECHA_CREACION) != null ? (String) act.get(Keys.FECHA_CREACION) : "",
                        (String) det.get(Keys.ID_DIVISA),
                        ((String) det.get(Keys.CLAVE_FORMA_LIQUIDACION)),
                        ((Double) det.get(Keys.TIPO_CAMBIO)).doubleValue(), 
                        valores[0], Double.parseDouble(valores[2]), ((Double) det.get(Keys.
                        TIPO_CAMBIO_MESA)).doubleValue(), Double.parseDouble(valores[1]),
                        ((Boolean) det.get(Keys.CONTIENE_NETEO)).booleanValue()));
            }
            else if (Actividad.ACT_DN_NOTIF_LIM.equals(nombreActividad)) {
            	List dets = new ArrayList();
            	String[] valores = ((String)act.get(Keys.RESULTADO)).split("\\|");
            	for (int x=0; x<valores.length; x++) {
    				String[] limNotif = valores[x].trim().split("\\:");
    			    String[] recProds = limNotif[1].trim().split("\\-");
    			    boolean recibimos = recProds[0].trim().equals("R") ? true : false;
    			    String[] prodsLims = recProds[1].trim().split("\\;");
    			    String[] prods = prodsLims[0].trim().split("\\,");
    			    String[] lims = prodsLims[1].trim().split("\\,");
    			    for (Iterator it2 = ((List) act.get(Keys.DETALLES)).iterator(); it2.hasNext();) {
	                    Map detalle = (Map) it2.next();
	                    if (detalle.get(Keys.CLAVE_FORMA_LIQUIDACION) != null) {
	                    	for (int i=0; i<prods.length; i++) {
	                    		if (detalle.get(Keys.ID_DIVISA).equals(limNotif[0].trim()) &&
	                    				detalle.get(Keys.CLAVE_FORMA_LIQUIDACION).equals(prods[i].
	                    						trim()) && detalle.get(Keys.RECIBIMOS).
	                    						equals(new Boolean(recibimos)) && !Deal.
	                    						STATUS_DEAL_CANCELADO.equals(act.get(Keys.
	                    								STATUS_DEAL)) && !DealDetalle.
	                    								STATUS_DET_CANCELADO.equals(detalle.
	                    										get(Keys.STATUS_DETALLE_DEAL))) {
	                    			dets.add(new DealDetWorkitemVO(((Integer) detalle.get(Keys.
	                    					ID_DEAL_DETALLE)).intValue(), ((Boolean) detalle.
	                    					get(Keys.RECIBIMOS)).booleanValue() ? REC : ENT,
	                    					(String) detalle.get(Keys.CLAVE_FORMA_LIQUIDACION),
	                    					(String) detalle.get(Keys.ID_DIVISA), ((Double) detalle.
	                    					get(Keys.MONTO)).doubleValue(), ((Double) detalle.
	                    					get(Keys.TIPO_CAMBIO)).doubleValue(), ((Double) detalle.
	                    					get(Keys.TIPO_CAMBIO_MESA)).doubleValue(), Double.
	                    					parseDouble(lims[0]), Double.parseDouble(lims[1]),
	                    					Double.parseDouble(lims[2]), Double.
	                    					parseDouble(lims[3]), Double.parseDouble(lims[4]),
	                    					Double.parseDouble(lims[5])));
	                    		}
	                    	}
	                    }
	                }
            	}
                DealWorkitemVO dealVO = new DealWorkitemVO(
                        ((Integer) act.get(Keys.ID_ACTIVIDAD)).intValue(),
                        nombreActividad.trim(), ((Integer) act.get(Keys.ID_DEAL)).intValue(),
                        (String) act.get(Keys.FECHA_VALOR),
                        Constantes.SI.equals(act.get(Keys.COMPRA)) ? "Compra" : "Venta",
                        interbancario,
                        act.get(Keys.NOM_CLIENTE) != null ? (String) act.get(Keys.NOM_CLIENTE) : "",
                        act.get(Keys.NOM_PROMOTOR) != null ?
                                (String) act.get(Keys.NOM_PROMOTOR) : "",
                        act.get(Keys.NOM_USUARIO) != null ? (String) act.get(Keys.NOM_USUARIO) : "",
                        act.get(Keys.FECHA_CREACION) != null ? (String) act.get(Keys.FECHA_CREACION) : "",
                        (!interbancario && Deal.EV_SOLICITUD.charAt(0) == (((String)
                                act.get(Keys.EVENTOS_DEAL)).charAt(Deal.EV_IND_SOBREPRECIO))) ?
                                Constantes.SI : Constantes.NO, datoAdicional, dets);
                resultados.add(dealVO);
            }
            else if (Actividad.ACT_DN_PLANTILLA.equals(nombreActividad)) {
                List dets = new ArrayList();
                Map detalle = getMapaDetalleConId((Integer) act.get(Keys.ID_DEAL_POSICION),
                        (List) act.get(Keys.DETALLES));
                if (detalle != null) {
                    dets.add(new DealDetWorkitemVO(
                            ((Integer) detalle.get(Keys.ID_DEAL_DETALLE)).intValue(),
                            ((Boolean) detalle.get(Keys.RECIBIMOS)).booleanValue() ? REC : ENT,
                            (String) detalle.get(Keys.CLAVE_FORMA_LIQUIDACION),
                            (String) detalle.get(Keys.ID_DIVISA),
                            ((Double) detalle.get(Keys.MONTO)).doubleValue(),
                            ((Double) detalle.get(Keys.TIPO_CAMBIO)).doubleValue(),
                            ((Double) detalle.get(Keys.TIPO_CAMBIO_MESA)).doubleValue()));
                }
                else {
                    dets.add(new DealDetWorkitemVO(0, ENT, "HC", "USD", Num.D_1000_0, Num.D_10_0,
                            Num.D_10_01));
                }
                DealWorkitemVO dealVO = new DealWorkitemVO(
                        ((Integer) act.get(Keys.ID_ACTIVIDAD)).intValue(),
                        nombreActividad.trim(), ((Integer) act.get(Keys.ID_DEAL)).intValue(),
                        (String) act.get(Keys.FECHA_VALOR),
                        Constantes.SI.equals(act.get(Keys.COMPRA)) ?
                                Constantes.COMPRA : Constantes.VENTA, interbancario,
                        act.get(Keys.NOM_CLIENTE) != null ? (String) act.get(Keys.NOM_CLIENTE) : "",
                        act.get(Keys.NOM_PROMOTOR) != null ?
                                (String) act.get(Keys.NOM_PROMOTOR) : "",
                        act.get(Keys.NOM_USUARIO) != null ? (String) act.get(Keys.NOM_USUARIO) : "",
                        act.get(Keys.FECHA_CREACION) != null ? (String) act.get(Keys.FECHA_CREACION) : "",		
                        (!interbancario && Deal.EV_SOLICITUD.charAt(0) ==
                                (((String) act.get(Keys.EVENTOS_DEAL)).
                                        charAt(Deal.EV_IND_SOBREPRECIO))) ?
                                Constantes.SI : Constantes.NO, null, dets);
                if (detalle != null) {
                    dealVO.setFolioDetalle(detalle.get(Keys.FOLIO_DETALLE) + "");
                }
                else {
                    dealVO.setFolioDetalle("00");
                }
                resultados.add(dealVO);
            }
            else {
                List dets = new ArrayList();
                for (Iterator it2 = ((List) act.get(Keys.DETALLES)).iterator(); it2.hasNext();) {
                    Map detalle = (Map) it2.next();
                    if (detalle.get(Keys.CLAVE_FORMA_LIQUIDACION) != null) {
                        if (Deal.STATUS_DEAL_CANCELADO.equals(act.get(Keys.STATUS_DEAL)) &&
                                DealDetalle.STATUS_DET_CANCELADO.equals(
                                        detalle.get(Keys.STATUS_DETALLE_DEAL)) ||
                                !DealDetalle.STATUS_DET_CANCELADO.equals(
                                        detalle.get(Keys.STATUS_DETALLE_DEAL))) {
                            dets.add(new DealDetWorkitemVO(
                                    ((Integer) detalle.get(Keys.ID_DEAL_DETALLE)).intValue(),
                                    ((Boolean) detalle.get(Keys.RECIBIMOS)).booleanValue() ?
                                            REC : ENT,
                                    (String) detalle.get(Keys.CLAVE_FORMA_LIQUIDACION),
                                    (String) detalle.get(Keys.ID_DIVISA),
                                    ((Double) detalle.get(Keys.MONTO)).doubleValue(),
                                    ((Double) detalle.get(Keys.TIPO_CAMBIO)).doubleValue(),
                                    ((Double) detalle.get(Keys.TIPO_CAMBIO_MESA)).doubleValue()));
                        }
                    }
                }
                DealWorkitemVO dealVO = new DealWorkitemVO(
                        ((Integer) act.get(Keys.ID_ACTIVIDAD)).intValue(),
                        nombreActividad.trim(), ((Integer) act.get(Keys.ID_DEAL)).intValue(),
                        (String) act.get(Keys.FECHA_VALOR),
                        Constantes.SI.equals(act.get(Keys.COMPRA)) ? "Compra" : "Venta",
                        interbancario,
                        act.get(Keys.NOM_CLIENTE) != null ? (String) act.get(Keys.NOM_CLIENTE) : "",
                        act.get(Keys.NOM_PROMOTOR) != null ?
                                (String) act.get(Keys.NOM_PROMOTOR) : "",
                        act.get(Keys.NOM_USUARIO) != null ? (String) act.get(Keys.NOM_USUARIO) : "",
                        act.get(Keys.FECHA_CREACION) != null ? (String) act.get(Keys.FECHA_CREACION) : "",
                        (!interbancario && Deal.EV_SOLICITUD.charAt(0) == (((String)
                                act.get(Keys.EVENTOS_DEAL)).charAt(Deal.EV_IND_SOBREPRECIO))) ?
                                Constantes.SI : Constantes.NO, datoAdicional, dets);
                resultados.add(dealVO);
            }
        }
        return resultados;
    }

    /**
     * Regresa el Mapa de la lista de actividades que corresponde al idActividad especificado.
     *
     * @param idActividad La llave primaria de la actividad.
     * @param mapas La lista de actividades.
     * @return HashMap.
     */
    private Map getMapaParaActividad(int idActividad, List mapas) {
        for (Iterator it = mapas.iterator(); it.hasNext();) {
            Map mapa = (Map) it.next();
            if (idActividad == ((Integer) mapa.get(Keys.ID_ACTIVIDAD)).intValue()) {
                return mapa;
            }
        }
        return null;
    }

    /**
     * Regresa el Mapa de la lista de detalles que corresponde al idDealDetalle especificado.
     *
     * @param idDealDetalle La llave primaria del detalle a localizar.
     * @param detalles La lista de detalles de un deal.
     * @return HashMap.
     */
    private Map getMapaDetalleConId(Integer idDealDetalle, List detalles) {
        for (Iterator it = detalles.iterator(); it.hasNext();) {
            Map detMap = (Map) it.next();
            if (detMap.get(Keys.ID_DEAL_DETALLE).equals(idDealDetalle)) {
                return detMap;
            }
        }
        return null;
    }

    /**
     * Subclase de MappingSql que representa el query para obtener las actividades de
     * autorizaci&oacute;n pendientes.
     */
    class ActividadesPendientesQuery extends MappingSqlQuery {

        /**
         * Constructor por default.
         * 
         * @param dataSource El data source a trav&eacute;s del cual se ejecutar&aacute; el query.
         * @param query La cadena con el query a ejecutar.
         */
        ActividadesPendientesQuery(DataSource dataSource, String query) {
            super(dataSource, query);
            compile();
        }

        /**
         * Regresa un HashMap que contiene cada una de los atributos de la selecci&oacute;n.
         *
         * @param rs El result set del que se extrae un registro.
         * @param i Ignorado.
         * @return HashMap.
         * @throws SQLException Si ocurre alg&uacute;n error.
         */
        protected Object mapRow(ResultSet rs, int i) throws SQLException {
            return MapUtils.generar(new String[]{Keys.ID_ACTIVIDAD, Keys.ACTIVIDAD, Keys.PROCESO,
                    Keys.ID_DEAL, Keys.ID_DEAL_POSICION, Keys.FECHA_VALOR,
                    Keys.SIMPLE, Keys.NOM_CLIENTE, Keys.NOM_PROMOTOR, Keys.NOM_USUARIO,
                    Keys.COMPRA, Keys.FECHA_CREACION, Keys.EVENTOS_DEAL, Keys.STATUS_DEAL,
                    Keys.EMAIL_FACTURA_OTRO, Keys.RFC_FACTURA,
                    Keys.ID_DEAL_DETALLE, Keys.FOLIO_DETALLE,
                    Keys.RECIBIMOS, Keys.CLAVE_FORMA_LIQUIDACION,
                    Keys.ID_DIVISA, Keys.MONTO,
                    Keys.TIPO_CAMBIO, Keys.TIPO_CAMBIO_MESA,
                    Keys.STATUS_DETALLE_DEAL, Keys.RESULTADO, Keys.CONTIENE_NETEO},
                    new Object[]{new Integer(rs.getInt(1)), rs.getString(2), rs.getString(Num.I_3),
                            new Integer(rs.getInt(Num.I_4)), new Integer(rs.getInt(Num.I_5)),
                            rs.getString(Num.I_6), rs.getString(Num.I_7), rs.getString(Num.I_8),
                            rs.getString(Num.I_9), rs.getString(Num.I_10), rs.getString(Num.I_11),
                            rs.getString(Num.I_12), rs.getString(Num.I_13), rs.getString(Num.I_14),
                            rs.getString(Num.I_15), rs.getString(Num.I_16),
                            new Integer(rs.getInt(Num.I_17)), new Integer(rs.getInt(Num.I_18)),
                            Boolean.valueOf(Constantes.SI.equals(rs.getString(Num.I_19))),
                            rs.getString(Num.I_20), rs.getString(Num.I_21),
                            new Double(rs.getDouble(Num.I_22)), new Double(rs.getDouble(Num.I_23)),
                            new Double(rs.getDouble(Num.I_24)), rs.getString(Num.I_25), 
                            rs.getString(Num.I_26), Boolean.valueOf(rs.getString(Num.I_27) == null ?
                            false : Constantes.SI.equals(rs.getString(Num.I_27)))
                    });
        }
    }

    /**
     * Constante para el query que extrae la informaci&oacute;n de las actividades pendientes.
     */
    private static final String ACTIVIDADES_PENDIENTES_QUERY = "SELECT a.ID_ACTIVIDAD, " +
            "a.NOMBRE_ACTIVIDAD actividad, a.PROCESO, a.ID_DEAL, a.ID_DEAL_POSICION, " +
            "d.TIPO_VALOR FECHA_VALOR, d.SIMPLE, p.NOMBRE_CORTO NOM_CLIENTE, " +
            "emp.NOMBRE||' '||emp.PATERNO||' '||emp.MATERNO NOM_PROMOTOR, " +
            "uemp.NOMBRE_CORTO NOM_USUARIO, d.COMPRA, TO_CHAR(a.fecha_creacion,'dd/mm/yyyy HH24:MI' ), d.EVENTOS_DEAL, " +
            "d.STATUS_DEAL, d.EMAIL_FACTURA_OTRO, d.RFC_FACTURA, " +
            "dd.ID_DEAL_POSICION ID_DEAL_DETALLE, dd.FOLIO_DETALLE, dp.RECIBIMOS, " +
            "dd.CLAVE_FORMA_LIQUIDACION, dp.ID_DIVISA, dp.MONTO, dp.TIPO_CAMBIO, " +
            "dd.TIPO_CAMBIO_MESA, dd.STATUS_DETALLE_DEAL, a.RESULTADO, d.NETEO CONTIENE_NETEO " +
            "FROM SC_ACTIVIDAD a, SC_DEAL d, SC_DEAL_DETALLE dd, SC_DEAL_POSICION dp, " +
            "BUP_PERSONA_CUENTA_ROL pcr, BUP_PERSONA p, BUP_PERSONA_FISICA emp, SEGU_USUARIO u, " +
            "BUP_PERSONA uemp " +
            "WHERE a.ID_DEAL = d.ID_DEAL AND d.ID_DEAL = dd.ID_DEAL " +
            "AND dd.ID_DEAL_POSICION = dp.ID_DEAL_POSICION AND d.NO_CUENTA = pcr.NO_CUENTA(+) " +
            "AND pcr.ID_ROL(+) = 'TIT' AND pcr.ID_PERSONA = p.ID_PERSONA(+) " +
            "AND d.ID_PROMOTOR = emp.ID_PERSONA(+) AND d.ID_USUARIO = u.ID_USUARIO " +
            "AND u.ID_PERSONA = uemp.ID_PERSONA AND a.STATUS = 'Pendiente'";

    /**
     * Constante para la cadena 'ENT'.
     */
    private static final String ENT = "ENT";

    /**
     * Constante para la cadena 'REC'.
     */
    private static final String REC = "REC";
}
