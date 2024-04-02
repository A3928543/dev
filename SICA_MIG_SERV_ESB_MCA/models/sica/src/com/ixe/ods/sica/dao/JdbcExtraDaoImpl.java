/*
 * $Id: JdbcExtraDaoImpl.java,v 1.11.60.1.4.1 2014/03/27 15:55:59 mejiar Exp $
 * Ixe Grupo Financiero
 * Copyright © 2005 LegoSoft S.C.
 */
package com.ixe.ods.sica.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.object.MappingSqlQuery;

/**
 * Implementacion de los m&acute;todos para la busqueda de personas y sus facultades.
 * 
 * @author David Solis (dsolis)
 * @version $Revision: 1.11.60.1.4.1 $ $Date: 2014/03/27 15:55:59 $
 */
public class JdbcExtraDaoImpl extends JdbcDaoSupport implements ExtraDao {

	/**
	 * @see com.ixe.ods.sica.dao.ExtraDao#findPersonasWithFacultyAndSystem(String, String)
	 */
    public List findPersonasWithFacultyAndSystem(String facultad, String sistema) {
        return new IdPersonaQuery(getDataSource()).execute(new Object[]{sistema, facultad, facultad});
    }

    private static String UPDATE_BUP_PERSONA = "UPDATE BUP_PERSONA SET B_ILYTICS = ? WHERE ID_PERSONA=? " ;
    
    /**
     * 
     * @param b_ilytics Valor que debe tomar el proceso de estandarizacion de la BUP.
     * @param idPersona Perona a actualizar.
     * @return
     */
    public boolean updateProcesoEstandarizacionBupPersona(Integer b_ilytics,  Integer idPersona){
    	getJdbcTemplate().update(UPDATE_BUP_PERSONA, new Object[]{b_ilytics, idPersona});
    	return true;
    }
    
    /**
     * Constante para el valor de QUERY.
     */
    private static String QUERY = "SELECT DISTINCT P.ID_PERSONA FROM BUP_PERSONA P, SEGU_USUARIO U, SEGU_PERFIL PF WHERE P.ID_PERSONA = U.ID_PERSONA AND U.ID_USUARIO = PF.ID_USUARIO AND PF.SISTEMA = ?" +
    " AND PF.ID_PERFIL IN (SELECT ID_PERFIL FROM SEGU_PERFIL_ROL WHERE ID_ROL IN (SELECT ID_COL from SEGU_ASOCIACION_FACULTAD  WHERE FACULTAD = ?)" +
    " UNION ALL SELECT ID_PERFIL FROM SEGU_PERFIL WHERE ID_PERFIL IN (SELECT ID_COL from SEGU_ASOCIACION_FACULTAD  WHERE FACULTAD = ?))";

    /**
     * Clase interna para accesar a las propiedades 
     * 
     * @author David Solis (dsolis)
     *
     */
    class IdPersonaQuery extends MappingSqlQuery {

    	/**
    	 * Constructor de la clase.
    	 * 
    	 *  @param ds El dataSource.
    	 */
        public IdPersonaQuery(DataSource ds) {
            super(ds, QUERY);
            super.declareParameter(new SqlParameter("SISTEMA", Types.VARCHAR));
            super.declareParameter(new SqlParameter("FACULTAD", Types.VARCHAR));
            super.declareParameter(new SqlParameter("FACULTAD", Types.VARCHAR));
            compile();
        }
        
        /**
         * Obtiene el id de la persona dado su idPersona.
         * 
         * @param rs El ResultSet obtenido del query.
         * @param rowNum El &iacute;ndice de rengl&oacuten.
         * @return Object 
         * @throws SQLException
         */
        protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Integer(rs.getInt("ID_PERSONA"));
        }
    }
    
    /**
     * Query para validar la existencia de entidad federativa en M\u00E9xico.
     */
    private static final String QRY_ENTIDAD_MEXICO =  
    	"select CLAVE_REGION from bup_entidad_federativa bef, tes_entidad_federativa tef " +
    	"where  (bef.abre_entidad_federativa = ? or bef.nombre_ent_federativa = ?) " +
    	"  and   bef.id_entidad_federativa = tef.id_entidad_federativa";
    
    /**
     * Query para validar la existencia de una entidad (estado, ciudad) extranjera.
     */
    private static final String QRY_ENTIDAD_EXTRANJERA = 
    	"SELECT CLAVE_PAIS, CLAVE_REGION FROM tes_entidad_federativa " +
    	"WHERE upper(entidad_federativa) = upper(?)";
    
    /**
     * Metodo que valida si existe una entidad federativa de M\u00E9xico y el extranjero
     * @param entidad la entidad que se desea validar
     * @param pais el pais al que pertenece la entidad.
     * @return <code>true</code> en caso de que la entidad sea v\u00E1lida,
     * 		   <code>false</code> en cualquier otro caso.
     */
    public boolean isEntidadValida(String entidad, String pais) {
    	
    	List resultado = null;
    	boolean entidadValida = false;
    	
    	if ("MX".equals(pais)) {
    		resultado = getJdbcTemplate().queryForList(QRY_ENTIDAD_MEXICO, 
        			new Object[] {entidad, entidad}, new int[]{Types.VARCHAR, Types.VARCHAR});
    	}
    	else {
    		resultado = getJdbcTemplate().queryForList(QRY_ENTIDAD_EXTRANJERA,
    				new Object[] {entidad}, new int[]{Types.VARCHAR});
    	}
    	
    	if(resultado != null && resultado.size() > 0){
    		entidadValida = true;
    	}
    	
    	return entidadValida;
    }
}
