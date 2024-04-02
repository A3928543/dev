/*
 * $Id: JdbcDealDao.java,v 1.4.2.3.6.1.4.2.4.1.4.2.18.1.10.1 2014/08/20 18:01:59 mejiar Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2009 - 2013 LegoSoft S.C.
 */

package com.ixe.ods.sica.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.object.MappingSqlQuery;

import com.ixe.ods.sica.Keys;
import com.ixe.ods.sica.dao.DealDao;
import com.ixe.ods.sica.model.Deal;
import com.ixe.ods.sica.sdo.dto.FiltroDealsTceDto;
import com.ixe.ods.sica.utils.DateUtils;
import com.ixe.ods.sica.utils.MapUtils;
import com.legosoft.hibernate.type.SiNoType;



/**
 * Implementaci&oacute;n JDBC de la interfaz DealDao.
 * 
 * @author Jean C. Favila (jfavila)
 * @version $Revision: 1.4.2.3.6.1.4.2.4.1.4.2.18.1.10.1 $ $Date: 2014/08/20 18:01:59 $
 */
public class JdbcDealDao extends JdbcDaoSupport implements DealDao {

    /**
     * Constructor por default.
     */
    public JdbcDealDao() {
        super();
    }

    /**
     * @param interbancario Define si el Deal es o no interbancario
     * @param tipoFecha Si la fecha es de Operaci&oacute;n, Liquidaci&oacute;n o Ninguna. 
     * @param fechaInicio La fecha inicio
     * @param fechaFinal La fecha final
     * @param status Define el status del Deal
     * @param id Id del Deal
     * @param idPromotor Id del promotor que proces&pacute; el Deal
     * @param tipoValor Fecha valor para el Deal
     * @param noCuenta N&uacute;mero de cuenta del cliente
     * @param eventos Define los eventos que marca el Deal
     * @param idCanal Id del Canal (Puede ser null para no restringirlo).
     * @param opcionMensajeria Si el deal es o no con mensajer&iacute;a.
     * @param claveFormaLiquidacion La clave de la forma de liquidaci&oacute;n
     * @param idDivisa El id de la divisa.
     * @param idGrupoTrabajo El ID del Grupo de Trabajo para consulta de deals en guardia.
     * @return List de HashMap.
     * @see DealDao#findDeals(boolean, java.util.Date, java.util.Date, java.util.Date,
     *  java.util.Date, String, Integer, Integer, String, String, String, String, Boolean, String).
     */
    public List findDeals(boolean interbancario, String tipoFecha,
                          Date fechaInicio,
                          Date fechaFinal, String status, Integer id, Integer idPromotor,
                          String tipoValor, String noCuenta, String eventos, String idCanal,
                          Boolean opcionMensajeria, String claveFormaLiquidacion, String idDivisa, String idGrupoTrabajo) {
        StringBuffer sb = new StringBuffer();
        List parametros = new ArrayList();
        if (id != null) {
            appendCondition(sb, "t1.id_deal = ").append(id);
        }
        if (noCuenta != null) {
            appendCondition(sb, "t1.no_cuenta = '").append(noCuenta).append(COMILLA);
        }
        
        if ( tipoFecha != null && !tipoFecha.equals(NINGUNA)){
        	if(tipoFecha.equals(CAPTURA)){
        		if (fechaInicio != null) {
            appendCondition(sb, "t1.fecha_captura >= ?");
                    fechaInicio = DateUtils.inicioDia(fechaInicio);
                    parametros.add(fechaInicio);
        }
        		if (fechaFinal != null) {
            appendCondition(sb, "t1.fecha_captura <= ?");
                    fechaFinal = DateUtils.finDia(fechaFinal);
                    parametros.add(fechaFinal);
                }
        }
        	if(tipoFecha.equals(LIQUIDACION)){
        		if (fechaInicio != null) {
            appendCondition(sb, "t1.fecha_liquidacion >= ?");
                    fechaInicio = DateUtils.inicioDia(fechaInicio);
                    parametros.add(fechaInicio);
        }
        		if (fechaFinal != null) {
            appendCondition(sb, "t1.fecha_liquidacion <= ?");
                    fechaFinal = DateUtils.finDia(fechaFinal);
                    parametros.add(fechaFinal);
                }
        	}
        }else if( id == null) {
        	tipoFecha = CAPTURA;
        	appendCondition(sb, "t1.fecha_captura >= ?");
        	fechaInicio = DateUtils.inicioDia(this.getUnMesAntes());
        	parametros.add(fechaInicio);
        	appendCondition(sb, "t1.fecha_captura <= ?");
        	fechaFinal = DateUtils.finDia(new Date());
            parametros.add(fechaFinal);
        }
        
        if (idPromotor != null) {
            appendCondition(sb, "t1.id_promotor = ").append(idPromotor);
        }
        if (idCanal != null) {
            appendCondition(sb, "t1.id_canal = '").append(idCanal).append(COMILLA);
        }
        if (!StringUtils.isEmpty(status)) {
        	if(Deal.STATUS_ESPECIAL_TC_AUTORIZADO.equals(status)) {
        		appendCondition(sb, "t1.estatus_especial = '").append(Deal.STATUS_ESPECIAL_TC_AUTORIZADO).append(COMILLA);
        	} else {
        		appendCondition(sb, "t1.status_deal = '").append(status).append(COMILLA);
        	}
        }
        if (tipoValor != null) {
            appendCondition(sb, "t1.tipo_valor = '").append(tipoValor).append(COMILLA);
        }
        if (opcionMensajeria != null) {
            appendCondition(sb, "t1.mensajeria = '").
                    append(opcionMensajeria.booleanValue() ? 'S' : 'N').append(COMILLA);
        }
        if (idGrupoTrabajo != null) {
            appendCondition(sb, "t1.id_grupo_trabajo = '").append(idGrupoTrabajo).append(COMILLA);
        }
        
        if ( claveFormaLiquidacion != null && claveFormaLiquidacion.length() >0) {
        	appendCondition(sb, "t8.clave_forma_liquidacion = '").append(claveFormaLiquidacion).append(COMILLA);
        }
        if (idDivisa != null && idDivisa.length() > 0){
        	appendCondition(sb, "t9.id_divisa = '").append(idDivisa).append(COMILLA);
        }
        
        appendCondition(sb, "simple ").append(interbancario ? "= '" : "!= '").
                append(Deal.TIPO_INTERBANCARIO).append(COMILLA);
        return ordenarMapasDeals(new DealsQuery(getDataSource(), 
                DEAL_QUERY.replaceAll("\\@", sb.toString()), tipoFecha,
                fechaInicio, fechaFinal).
                execute(parametros.toArray()));
    }
    
    /**
     * @param filtroDealsTce Filtro de la consulta
     * @see DealDao#findDealsTce(FiltroDealsTceDto)
     */
    public List findDealsTce(FiltroDealsTceDto filtroDealsTce) {
        StringBuffer sb = new StringBuffer();
        List parametros = new ArrayList();
        
        // Agregar el filtro de fecha de captura al dia de hoy
        Date fechaCapturaHoy = new Date();
        
        Date inicioDiaHoy = DateUtils.inicioDia(fechaCapturaHoy);
        appendCondition(sb, "t1.fecha_captura >= ?");
        parametros.add(inicioDiaHoy);
        
        Date finDiaHoy = DateUtils.finDia(fechaCapturaHoy);
        appendCondition(sb, "t1.fecha_captura <= ?");
        parametros.add(finDiaHoy);
        
        // agregar filtro para buscar solo deals con tipo de cambio especial
        appendCondition(sb, "t1.especial = '").append(SiNoType.TRUE).append(COMILLA);
        
        // Agregar filtro para para buscar por numero de deal
        if (! filtroDealsTce.isNullDeal()) {
            appendCondition(sb, "t1.id_deal = ").append(filtroDealsTce.getIdDeal());
        }
       
       
        // si capturo la mesa, el status especial es Autorizado TC
        // si capturo el promotor el status especial es  null
        //Si es capturado por cualquiera, solo se agrega el filtro de status si
    	//es diferente de cualquiera
        if( filtroDealsTce.isCapturadoPorCualquiera()) {
        	if(filtroDealsTce.isEstatusAutorizadoTce()) {
        		appendCondition(sb, "t1.estatus_especial = '").append(Deal.STATUS_ESPECIAL_TC_AUTORIZADO).append(COMILLA);
        	// cuando es status pendiente muestra todos los deals menos los que tengan status de totalmente liquidados
        	// no muestra status especial = autorizada tce
        	} else if ( filtroDealsTce.isEstatusPendiente() ) { 
        		appendCondition(sb, "t1.status_deal <> '").append(Deal.STATUS_DEAL_TOTALMENTE_LIQUIDADO).append(COMILLA);
        		//appendCondition(sb, "t1.estatus_especial <> '").append(Deal.STATUS_ESPECIAL_TC_AUTORIZADO).append(COMILLA);
        	}
        // 
        } else if ( filtroDealsTce.isCapturadoPorMesaCambios() ) {
        	if(filtroDealsTce.isEstatusAutorizadoTce()) {
        		appendCondition(sb, "t1.estatus_especial = '").append(Deal.STATUS_ESPECIAL_TC_AUTORIZADO).append(COMILLA);
        	// cuando es status pendiente muestra todos los deals menos los que tengan status de totalmente liquidados
        	// no muestra status especial = autorizada tce
        	} else if ( filtroDealsTce.isEstatusPendiente() ) { 
        		appendCondition(sb, "t1.status_deal <> '").append(Deal.STATUS_DEAL_TOTALMENTE_LIQUIDADO).append(COMILLA);
        		appendCondition(sb, "t1.estatus_especial is not null");
        	}else{
        		appendCondition(sb, "t1.estatus_especial is not null");
        	}
        } else if ( filtroDealsTce.isCapturadoPorPromotor() ) {
        	//Si es capturado por el promotor, no puede tener un status TC Autorizado.
        	if ( filtroDealsTce.isEstatusPendiente() ) { 
        		appendCondition(sb, "t1.status_deal <> '").append(Deal.STATUS_DEAL_TOTALMENTE_LIQUIDADO).append(COMILLA);
        		appendCondition(sb, "t1.estatus_especial is null");
        	}else{
        		appendCondition(sb, "t1.estatus_especial is null");
        	}
        }
        
        
        else if(! filtroDealsTce.isEstatusCualquiera()) { 
        	// Si es estatus autorizado tce se muestran solo los deals con este status especial, hay que verificar que no se haya aplicado ya el
        	// filtro, ya que es el mismo que cuando se filtra capturada por mesa de cambios
        	if(filtroDealsTce.isEstatusAutorizadoTce()) {
        		appendCondition(sb, "t1.estatus_especial = '").append(Deal.STATUS_ESPECIAL_TC_AUTORIZADO).append(COMILLA);
        	// cuando es status pendiente muestra todos los deals menos los que tengan status de totalmente liquidados
        	// no muestra status especial = autorizada tce
        	} else if ( filtroDealsTce.isEstatusPendiente() ) { 
        		appendCondition(sb, "t1.status_deal <> '").append(Deal.STATUS_DEAL_TOTALMENTE_LIQUIDADO).append(COMILLA);
        		appendCondition(sb, "t1.estatus_especial <> '").append(Deal.STATUS_ESPECIAL_TC_AUTORIZADO).append(COMILLA);
        	}
        }
        
        // Si viene el id persona del promotor, agregar el filtro para traer solo los deals capturados a nombre del promotor especificado
        if(!filtroDealsTce.isIdPersonaPromotorNull()) {
        	appendCondition(sb, "t1.id_promotor = ").append(filtroDealsTce.getIdPersonaPromotor());
        }

        return ordenarMapasDeals(
        	new DealsQuery(getDataSource(), 
            DEAL_QUERY.replaceAll("\\@", sb.toString()), CAPTURA, inicioDiaHoy, finDiaHoy).execute(parametros.toArray())
        );
    }

    /**
     * Agrega una condici&oacute;n al query.
     *
     * @param sb El string buffer que contiene el query.
     * @param condition La condici&oacute;n a agregar.
     * @return StringBuffer.
     */
    private StringBuffer appendCondition(StringBuffer sb, String condition) {
        if (sb.length() != 0) {
            sb.append(" and");
        }
        sb.append(" ").append(condition);
        return sb;
    }

    /**
     * Obtiene la fecha de un mes antes a
     * la decha actual.
     * 
     * @return Date
     */
    private Date getUnMesAntes(){
    	Calendar calendar = Calendar.getInstance();
    	calendar.get(Calendar.MONTH);
    	calendar.add(Calendar.MONTH, -1);
    	return calendar.getTime();
    }

    /**
     * Genera una lista de mapas con los resultados. La llave 'detalles' corresponde a la lista de
     * detalles del deal (tambi&eacute;n son mapas).
     *
     * @param resultados Los resultados de la ejecuci&oacute;n del query.
     * @return List de Map.
     */
    private List ordenarMapasDeals(List resultados) {
        List deals = new ArrayList();
        for (Iterator it = resultados.iterator(); it.hasNext();) {
            Map res = (Map) it.next();
            Map deal = null;
            for (Iterator it2 = deals.iterator(); it2.hasNext();) {
                Map tmp = (Map) it2.next();
                if (res.get(Keys.ID_DEAL).equals(tmp.get(Keys.ID_DEAL))) {
                    deal = tmp;
                    break;
                }
            }
            if (deal == null) {
                deal = MapUtils.copiar(res);
                deal.put(Keys.DETALLES, new ArrayList());
                deals.add(deal);
                deal.remove(Keys.CLAVE_FORMA_LIQUIDACION);
                deal.remove(Keys.RECIBIMOS);
                deal.remove(Keys.ID_DIVISA);
                deal.remove(Keys.MONTO);
                deal.remove(Keys.TIPO_CAMBIO);
                
                deal.remove(Keys.DIVISA_REFERENCIA);
                deal.remove(Keys.DIVISA_DIVIDE);
                
            }
            List dets = (List) deal.get("detalles");
            dets.add(MapUtils.generar(new String[]{Keys.CLAVE_FORMA_LIQUIDACION, Keys.RECIBIMOS,
                    Keys.ID_DIVISA, Keys.MONTO, Keys.STATUS_DETALLE_DEAL, Keys.TIPO_CAMBIO,
                    Keys.DIVISA_REFERENCIA, Keys.DIVISA_DIVIDE, Keys.ID_DEAL_POSICION, Keys.FED},
                    new Object[]{res.get(Keys.CLAVE_FORMA_LIQUIDACION),
                            "S".equals(res.get(Keys.RECIBIMOS)) ?
                                    Boolean.TRUE : Boolean.FALSE,
                            res.get(Keys.ID_DIVISA), res.get(Keys.MONTO),
                            res.get(Keys.STATUS_DETALLE_DEAL),
                            res.get(Keys.TIPO_CAMBIO),
                            res.get(Keys.DIVISA_REFERENCIA),
                            res.get(Keys.DIVISA_DIVIDE), res.get(Keys.ID_DEAL_POSICION),
                            res.get(Keys.FED)
                    }));
        }
        return deals;
    }

    /**
     * Clase que representa el query para extraer los deals.
     */
    class DealsQuery extends MappingSqlQuery {

        /**
         * Constructor por default.
         *
         * @param dataSource El dataSource.
         * @param query El string del query a ejecutar.
         * @param fechaInicioOperacion La fecha de inicio de captura (opcional).
         * @param fechaFinOperacion La fecha fin de captura (opcional).
         * @param fechaInicioLiquidacion La fecha de inicio de vencimiento (opcional).
         * @param fechaFinLiquidacion La fecha de fin de vencimiento (opcional).
         */
        DealsQuery(DataSource dataSource, String query, String tipoFecha,
                   Date fechaInicio, Date fechaFin) {
            super(dataSource, query);
            if ( tipoFecha != null && !tipoFecha.equals(NINGUNA)) {
            	if(tipoFecha.equals(CAPTURA)){
            		if (fechaInicio != null) {
            			super.declareParameter(new SqlParameter(Keys.FECHA_CAPTURA, Types.TIMESTAMP));
            		}
            		if (fechaFin != null) {
            			super.declareParameter(new SqlParameter("fechaCapturaFin", Types.TIMESTAMP));
            		}
            	}
            	if(tipoFecha.equals(LIQUIDACION)){
            		if (fechaInicio != null) {
            			super.declareParameter(new SqlParameter(Keys.FECHA_LIQUIDACION, Types.TIMESTAMP));
            		}
            		if (fechaFin != null) {
            			super.declareParameter(new SqlParameter("fechaLiquidacionFin", Types.TIMESTAMP));
            		}
            	}
                
            }
           
            compile();
        }

        /**
         * Regresa un Mapa con el contenido del result set.
         *
         * @param rs El resultset.
         * @param i El &iacute;ndice del registro.
         * @return Map.
         * @throws SQLException Si ocurre alg&uacute;n error.
         */
        protected Object mapRow(ResultSet rs, int i) throws SQLException {
            return MapUtils.generar(new String[]{Keys.ID_DEAL, Keys.NO_CUENTA, Keys.ID_CANAL,
                    Keys.ID_PROMOTOR, "idPersonaUsuario", Keys.CLIENTE, "usuario", "tipoOperacion",
                    "tipoValor", Keys.FECHA_CAPTURA, Keys.FECHA_LIQUIDACION, Keys.STATUS_DEAL,
                    Keys.BROKER, Keys.REVERSADO, Keys.CLAVE_FORMA_LIQUIDACION, Keys.RECIBIMOS,
                    Keys.ID_DIVISA, Keys.MONTO, Keys.STATUS_DETALLE_DEAL, Keys.TIPO_CAMBIO, 
                    Keys.SIMPLE, Keys.DIVISA_REFERENCIA, Keys.DIVISA_DIVIDE, Keys.ESTATUS_ESPECIAL,
                    Keys.ID_DEAL_POSICION, Keys.FED},
                    new Object[]{new Integer(rs.getInt("id_deal")), rs.getString("no_cuenta"),
                            rs.getString("id_canal"), rs.getObject("id_promotor") == null ? null :
                            	new Integer(rs.getInt("id_promotor")),
                            new Integer(rs.getInt("id_persona_usuario")),
                            rs.getString(Keys.CLIENTE), rs.getString("nombre_usuario"),
                            "S".equals(rs.getString("compra")) ? "Compra" : "Venta",
                            rs.getString("tipo_valor"), rs.getTimestamp("fecha_captura"),
                            rs.getDate("fecha_liquidacion"), rs.getString("status_deal"),
                            rs.getString(Keys.BROKER), new Integer(rs.getInt(Keys.REVERSADO)),
                            rs.getString("clave_forma_liquidacion"), rs.getString(Keys.RECIBIMOS),
                            rs.getString("id_divisa"), rs.getBigDecimal("monto"),
                            rs.getString("status_detalle_deal"),
                            rs.getBigDecimal("tipo_cambio"),
                            rs.getString("simple"),
                            rs.getString("ID_DIVISA_REFERENCIA"),
                            rs.getString("DIVIDE"),
                            rs.getString("ESTATUS_ESPECIAL"),
                            new Integer(rs.getInt("ID_DEAL_POSICION")),
                            rs.getString("fed")
                    });
        }
    }

    /**
     * Constante que define el Query para la consulta general de deals.
     */
    private static final String DEAL_QUERY = 
    	"SELECT DISTINCT " 
    		+ "t1.id_deal " 
    		+ ", t1.no_cuenta " 
            + ", t1.id_canal " 
            + ", t1.id_promotor " 
            + ", t2.id_persona id_persona_usuario " 
            + ", t6.nombre_corto cliente " 
            + ", t3.nombre||' ' ||t3.paterno||' '||t3.materno nombre_usuario " 
            + ", compra " 
            + ", tipo_valor " 
            + ", fecha_captura " 
            + ", fecha_liquidacion " 
            + ", status_deal " 
            + ", t9.recibimos " 
            + ", t7.nombre_corto broker "
            + ", t1.reversado " 
            + ", clave_forma_liquidacion "
            + ", t9.id_divisa "
            + ", monto " 
            + ", status_detalle_deal "
            + ", t9.tipo_cambio "
            + ", t1.simple "
            + ", mc.ID_DIVISA_REFERENCIA "
            + ", div.DIVIDE "
            + ", t9.id_deal_posicion "
            + ", t1.estatus_especial "
            + ", t8.fed "
        + "FROM " 
        	+ "sc_deal t1 " 
        	+ ", segu_usuario t2 " 
        	+ ", bup_persona_fisica t3 " 
        	+ ", bup_contrato_sica t4 " 
        	+ ", bup_persona_cuenta_rol t5 " 
            + ", bup_persona t6 " 
            + ", bup_persona t7 " 
            + ", sc_deal_detalle t8 " 
            + ", sc_deal_posicion t9 " 
            + ", sc_mesa_cambio mc "
            + ", sc_divisa div "
        + "WHERE " 
        	+ "@ " 
        	+ "and t1.id_deal = t8.id_deal " 
        	+ "and t8.id_deal_posicion = t9.id_deal_posicion " 
            + "and t1.id_usuario = t2.id_usuario " 
            + "and t2.id_persona = t3.id_persona " 
            + "and t1.no_cuenta = t4.no_cuenta(+) " 
            + "and t4.no_cuenta = t5.no_cuenta(+) " 
            + "and t5.id_rol(+) = 'TIT' " 
            + "and t5.id_persona = t6.id_persona(+) " 
            + "and t1.id_broker = t7.id_persona(+) " 
            + "and mc.ID_MESA_CAMBIO = t9.ID_MESA_CAMBIO "
            + "and div.ID_DIVISA = t9.ID_DIVISA "
            + "and rownum < 2500 ";

    /**
     * Constante para la cadena "'".
     */
    private static final String COMILLA = "'";
    
    /**
     * Constante para la cadena "Ninguna"
     */
    private static final String NINGUNA = "Ninguna";
    
    /**
     * Constante para la cadena "Liquidaci\u00f3n"
     */
    private static final String LIQUIDACION = "Liquidaci\u00f3n";
    
    /**
     * Constante para la cadena "Captura"
     */
    private static final String CAPTURA = "Captura";
}
