/*
 * $Id: JdbcPhoenixDao.java,v 1.1.2.2 2010/11/07 23:43:02 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2009 LegoSoft S.C.
 */

package com.ixe.ods.sica.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.object.MappingSqlQuery;

import com.ixe.ods.sica.Constantes;
import com.ixe.ods.sica.Keys;
import com.ixe.ods.sica.Num;
import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.dao.PhoenixDao;
import com.ixe.ods.sica.model.CodigoPostalListaBlanca;
import com.ixe.ods.sica.model.PersonaListaBlanca;
import com.ixe.ods.sica.sdo.SicaServiceData;
import com.ixe.ods.sica.utils.MapUtils;
import com.legosoft.hibernate.type.SiNoType;

/**
 * Implementaci&oacute;n JDBC de la interfaz PhoenixDao.
 *
 * @author lvillegas
 * @version $Revision: 1.1.2.2 $ $Date: 2010/11/07 23:43:02 $
 * @see com.ixe.ods.sica.dao.PhoenixDao
 */
public class JdbcPhoenixDao extends JdbcDaoSupport implements PhoenixDao {

    /**
     * Constructor por default.
     */
    public JdbcPhoenixDao() {
        super();
    }

    /**
     * M&eacute;todo encargado de actalizar la lista blanca de personas con la persona
     * recibida como par&aacute;metro.
     * @param personaLB PersonaListaBlanca con la informaci&oacute;n a utilizar.
     */
    public void updateListasBlanca(PersonaListaBlanca personaLB) {
        try {
            actualizaListaBlanca(personaLB,
                sicaServiceData.findRimsByContratoSica(personaLB.getNoCuenta()));
        } 
        catch (Exception e) {
            throw new SicaException("No se pudo actualizar la tabla CB_LISTA_BLANCA de Phoenix " +
                "para la cuenta: " + personaLB.getNoCuenta() + ". " + e.getMessage(), e);
        }
    }
    
    /**
     * M&eacute;todo encargado de actualizar la lista blanca de codigos postales con el
     * par&aacute;metro recibido.
     *
     * @param codigosPostalesBup La lista de c&oacute;digos postales.
     * @param regionFronteriza	Si es regi&oacute;n fronteriza.
     * @param zonaTuristica Si es regi&oacute;n tur&iacute;stica.
     * @param fechaUltimaModificacion La fecha de modificaci&oacute;n.
     * @param idPersona El id de la persona que modific&oacute; el registro.
     */
    public void updateListaBlancaCodigosPostales(List codigosPostalesBup, String regionFronteriza,
    		String zonaTuristica, Date fechaUltimaModificacion, Integer idPersona) {
    	String codigoPostal = "";
    	try {
    		List codigosPostalesGuardados = new ConsultaCodigosPostalesListaBlancaQuery(getDataSource(),
    	            QUERY_SELECT_CP.replaceAll(":codigo_postal",
    	            		obtieneEnCadena(codigosPostalesBup, false))).execute();
            List codigosPostalesRegistrados = new ArrayList();
            for (Iterator i = codigosPostalesGuardados.iterator(); i.hasNext();) {
            	codigoPostal = (String) ((Map) i.next()).get(Keys.CODIGO_POSTAL);
            	codigosPostalesRegistrados.add(codigoPostal);
            	getJdbcTemplate().update(QUERY_UPDATE_CP, new Object[] {
            		regionFronteriza, zonaTuristica, fechaUltimaModificacion,
            		"Active", idPersona, codigoPostal
            	});
            }
            for (Iterator i = codigosPostalesBup.iterator(); i.hasNext();) {
            	codigoPostal = (String) i.next();
            	if (codigosPostalesRegistrados.contains(codigoPostal)) {
            		continue;
            	}
            	getJdbcTemplate().update(QUERY_INSERT_CP, new Object[] {
            		codigoPostal, regionFronteriza, zonaTuristica, new Date(), new Date(),
            		"Active", idPersona
            	});
            }
    	}
    	catch (Exception e) {
    		throw new SicaException("No se pudo actualizar la tabla CB_LISTA_BLANCA_CP de " +
    				"Phoenix.", e);
    	}
    }
    
    /**
     * Elimina de Phoenix la lista de c&oacute;digos postales recibidos como par&aacute;metro.
     * 
     * @param codigosPostalesBup La lista de c&oacute;digos postales.
     */
    public void eliminarListaBlancaCodigosPostales(List codigosPostalesBup) {
    	try {
    		getJdbcTemplate().update(QUERY_DELETE_CP.replaceAll(":codigo_postal",
    				obtieneEnCadena(codigosPostalesBup, false)));
    	}
    	catch (Exception e) {
    		throw new SicaException("No se pudier\u00f3n eliminar los registros de la tabla " +
    				"CB_LISTA_BLANCA_CP de Phoenix.", e);
    	}
    }
    
    /**
     * Actualiza la los rims para guardar/actualizar en la lista blanca con la
     * informaci&oacute;n recibida como par&aacute;metro.
     * @param persona con la informaci&oacute;n que se va a actualizar.
     * @param rims que se van a guardar/actualizar.
     */
    private void actualizaListaBlanca(PersonaListaBlanca persona, List rims) {
        List rimsGuardados = new ConsultaPersonasListaBlancaQuery(getDataSource(),
            QUERY_SELECT_LB.replaceAll(":rim_no", obtieneEnCadena(rims, true))).execute();
        Map mapa;
        List rimsRegistrados = new ArrayList();
        for (Iterator i = rimsGuardados.iterator(); i.hasNext();) {
            mapa = (Map) i.next();
            rimsRegistrados.add((mapa.get(Keys.RIM_NO)));
            getJdbcTemplate().update(QUERY_UPDATE_LB, new Object[] {
                persona.getTipoExcepcion().equals(PersonaListaBlanca.EXCEPCION_SHCP)
                    ? Constantes.SI : Constantes.NO,
                persona.getTipoExcepcion().equals(PersonaListaBlanca.EXCEPCION_IXE)
                    ? Constantes.SI : Constantes.NO,
                persona.getObservaciones(),
                persona.getFechaUltimaModificacion(),
                "Active",
                persona.getUsuarioModificacion(),
                mapa.get(Keys.RIM_NO)
            });
        }
        for (Iterator i = rims.iterator(); i.hasNext();) {
            Integer rimActual = (Integer) i.next();
            if (rimsRegistrados.contains(rimActual)) {
                continue;
            }
            getJdbcTemplate().update(QUERY_INSERT_LB, new Object[] {
                rimActual,
                persona.getTipoExcepcion().equals(PersonaListaBlanca.EXCEPCION_SHCP)
                    ? Constantes.SI : Constantes.NO,
                persona.getTipoExcepcion().equals(PersonaListaBlanca.EXCEPCION_IXE)
                    ? Constantes.SI : Constantes.NO,
                persona.getObservaciones(),
                persona.getFechaCreacion(),
                persona.getFechaUltimaModificacion(),
                "Active",
                persona.getUsuarioModificacion()
            });
        }
    }
    
    /**
     * Genera una cadena de elementos separado por comas en base a la lista
     * recibida.
     * @param lista  de elementos.
     * @param numerico  identifica si es num&eacute;rico o cadena
     * @return cadena.
     */
    private String obtieneEnCadena(List lista, boolean numerico) {
        StringBuffer cadena = new StringBuffer();
        if (!lista.isEmpty() && !numerico) {
        	cadena.append("'");
        }
        for (Iterator i = lista.iterator(); i.hasNext(); ) {
            cadena.append(i.next());
            if (i.hasNext()) {
            	if (numerico) {
            		cadena.append(", ");
            	}
            	else {
            		cadena.append("', '");
            	}
            }
            else if (!numerico) {
            	cadena.append("'");
            }
        }
        return cadena.toString();
    }
    
    /**
     * Verifica las tablas de phoenix para que sean consistentes con las
     * del SICA.
     */
    public void corrigeInconsistenciasIsisPhoenix() {
        Long checkSumPhoenix = obtieneCheckSumCodigosPostalesListBlanca();
        Long checkSumIsis = getSicaServiceData().findCheckSumCodigosPostalesListBlanca();
        if (checkSumPhoenix.compareTo(checkSumIsis) != 0) {
            // Se borra toda la informaci&oacute;n de Phoenix.
            getJdbcTemplate().update(QUERY_DELETE_ALL_CP);
            // Se obtiene la informaci&oacute;n de los c&oacute;digos postales.
            List codigosPostales = getSicaServiceData().findAllCodigoPostalListaBlanca();
            // Se inserta cada c&oacute;digo postal en la lista.
            if (codigosPostales != null && !codigosPostales.isEmpty()) {
                for (Iterator i = codigosPostales.iterator(); i.hasNext();) {
                    CodigoPostalListaBlanca codigoPostal =
                        (CodigoPostalListaBlanca) i.next();
                    try {
                        getJdbcTemplate().update(QUERY_INSERT_CP, new Object[] {
                            codigoPostal.getCodigoPostal(),
                            codigoPostal.getZonaFronteriza().booleanValue()
                                ? SiNoType.TRUE : SiNoType.FALSE,
                            codigoPostal.getZonaTuristica().booleanValue()
                                ? SiNoType.TRUE : SiNoType.FALSE,
                            new Date(),
                            new Date(),
                            "Active",
                            new Integer(0)
                        });
                    } 
                    catch (Exception e) {
                    }
                }
            }
        }
    }
    
    /**
     * Obtiene el checksum de los c&oacute;digos postales registrados
     * en la lista blanca.
     * @return Long n&uacute;mero.
     */
    private Long obtieneCheckSumCodigosPostalesListBlanca() {
        List checkSum = new ConsultaCheckSumQuery(getDataSource(),
            QUERY_CHECK_SUM_LB_CP).execute();
        Long resultado = new Long(0);
        if (checkSum != null && !checkSum.isEmpty()) {
            resultado = (Long) ((Map) checkSum.get(0)).get(Keys.CHECK_SUM);
        }
        return resultado;
    }

    /**
     * Subclase de MappingSql que representa el query para obtener los registros
     *  de la lista blanca de personas.
     */
    class ConsultaPersonasListaBlancaQuery extends MappingSqlQuery {
        /**
         * Constructor por default.
         * 
         * @param dataSource El data source a trav&eacute;s del cual se ejecutar&aacute; el query.
         * @param query La cadena con el query a ejecutar.
         */
        ConsultaPersonasListaBlancaQuery(DataSource dataSource, String query) {
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
            return MapUtils.generar(new String[]{Keys.RIM_NO, Keys.EXCEPCION_SHCP,
                    Keys.EXCEPCION_IXE, Keys.OBSERVACIONES, Keys.FECHA_CREACION,
                    Keys.FECHA_CAPTURA, Keys.STATUS, Keys.ID_PERSONA_USUARIO},
                    new Object[]{new Integer(rs.getInt(1)),
                            Boolean.valueOf(rs.getString(2) == null ?
                                false : Constantes.SI.equals(rs.getString(2))),
                            Boolean.valueOf(rs.getString(Num.I_3) == null ?
                                false : Constantes.SI.equals(rs.getString(Num.I_3))),
                            rs.getString(Num.I_4),
                            rs.getDate(Num.I_5),
                            rs.getDate(Num.I_6),
                            rs.getString(Num.I_7),
                            new Integer(rs.getInt(Num.I_8))
                    });
        }
    }
    
    /**
     * Subclase de MappingSql que representa el query para obtener los registros
     *  de la lista blanca de c&oacute;digos postales.
     */
    class ConsultaCodigosPostalesListaBlancaQuery extends MappingSqlQuery {
        /**
         * Constructor por default.
         * 
         * @param dataSource El data source a trav&eacute;s del cual se ejecutar&aacute; el query.
         * @param query La cadena con el query a ejecutar.
         */
        ConsultaCodigosPostalesListaBlancaQuery(DataSource dataSource, String query) {
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
            return MapUtils.generar(new String[]{Keys.CODIGO_POSTAL},
                    new Object[]{new String(rs.getString(1))});
        }
    }
    
    /**
     * Subclase de MappingSql que representa el query para obtener los registros
     *  de la lista blanca.
     */
    class ConsultaCheckSumQuery extends MappingSqlQuery {
        /**
         * Constructor por default.
         * 
         * @param dataSource El data source a trav&eacute;s del cual se ejecutar&aacute; el query.
         * @param query La cadena con el query a ejecutar.
         */
        ConsultaCheckSumQuery(DataSource dataSource, String query) {
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
            return MapUtils.generar(new String[]{Keys.CHECK_SUM},
                    new Object[]{new Long(rs.getLong(1))});
        }
    }
    
    /**
     * Query utilizado para hacer un select sobre CB_LISTA_BLANCA.
     */
    private static final String QUERY_SELECT_LB =
        "select * from CB_LISTA_BLANCA " +
        "where rim_no in (:rim_no)";
    
    /**
     * Query utilizado para hacer un select sobre CB_LISTA_BLANCA_CP.
     */
    private static final String QUERY_SELECT_CP =
        "select * from CB_LISTA_BLANCA_CP " +
        "where codigo_postal in (:codigo_postal)";
    
    /**
     * Query utilizado para hacer insert sobre CB_LISTA_BLANCA_LB.
     */
    private static final String QUERY_INSERT_LB =
        "insert into CB_LISTA_BLANCA (rim_no, excepcion_gob, excepcion_ixe, " +
        "observaciones, create_dt, effective_dt, status, empl_id) values " + 
        "(?, ?, ?, ?, ?, ?, ?, ?)";
    
    /**
     * Query utilizado para hacer insert sobre CB_LISTA_BLANCA_CP.
     */
    private static final String QUERY_INSERT_CP =
        "insert into CB_LISTA_BLANCA_CP (codigo_postal, es_rf, es_zt, " +
        "create_dt, effective_dt, status, empl_id) values " + 
        "(?, ?, ?, ?, ?, ?, ?)";
    
    /**
     * Query utilizado para borrar sobre CB_LISTA_BLANCA_CP.
     */
    private static final String QUERY_DELETE_CP =
    	"delete from CB_LISTA_BLANCA_CP where codigo_postal in (:codigo_postal)";
    
    /**
     * Query utilizado para borrar todo sobre CB_LISTA_BLANCA_CP.
     */
    private static final String QUERY_DELETE_ALL_CP =
        "delete from CB_LISTA_BLANCA_CP";
    
    /**
     * Query utilizado para hacer update sobre CB_LISTA_BLANCA_LB.
     */
    private static final String QUERY_UPDATE_LB =
        "update CB_LISTA_BLANCA set excepcion_gob = ?, " +
        "excepcion_ixe = ?, observaciones = ?, " +
        "effective_dt = ?, status = ?, empl_id = ? " +
        "where rim_no = ?";
    
    /**
     * Query utilizado para hacer update sobre CB_LISTA_BLANCA_CP.
     */
    private static final String QUERY_UPDATE_CP =
        "update CB_LISTA_BLANCA_CP set es_rf = ?, " +
        "es_zt = ?, effective_dt = ?, status = ?, empl_id = ? " +
        "where codigo_postal = ?";
    
    /**
     * Query que obtiene el n&uacute;mero m&aacute;gico de los c&oacute;digos postales
     */
    private static final String QUERY_CHECK_SUM_LB_CP =
        "select SUM(case when ES_RF = 'S' then CONVERT(INT,CODIGO_POSTAL) else 0 end) " +
        "* 10 + SUM(case when ES_ZT = 'S' then CONVERT(INT,CODIGO_POSTAL) else 0 end) " +
        "from CB_LISTA_BLANCA_CP";

    /**
     * Regresa el valor de sicaServiceData.
     *
     * @return SicaServiceData.
     */
    public SicaServiceData getSicaServiceData() {
        return sicaServiceData;
    }

    /**
     * Establece el valor de sicaServiceData.
     *
     * @param sicaServiceData El valor a asignar.
     */
    public void setSicaServiceData(SicaServiceData sicaServiceData) {
        this.sicaServiceData = sicaServiceData;
    }

    /**
     * Referencia al bean sicaServiceData.
     */
    private SicaServiceData sicaServiceData;
}
