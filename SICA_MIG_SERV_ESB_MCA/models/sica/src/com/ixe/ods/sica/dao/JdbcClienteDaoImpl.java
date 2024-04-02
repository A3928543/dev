/*
 * $Id: JdbcClienteDaoImpl.java,v 1.17.30.1.14.1.16.1.10.1 2016/07/13 21:42:36 mejiar Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2006 - 2009 LegoSoft S.C.
 */
package com.ixe.ods.sica.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.object.MappingSqlQuery;

import com.ixe.ods.sica.dto.ClienteContratoDto;
import com.ixe.ods.sica.dto.ClienteContratoInfoPromotorDto;

/**
 * DAO de JDBC que sirve para agilizar la b&uacute;squeda de Clientes y Contratos SICA.
 *
 * @author Jean C. Favila
 * @version $Revision: 1.17.30.1.14.1.16.1.10.1 $ $Date: 2016/07/13 21:42:36 $
 */
public class JdbcClienteDaoImpl extends JdbcDaoSupport implements ClienteDao {

    /**
     * Constructor por default.
     */
    public JdbcClienteDaoImpl() {
        super();
    }

    /**
     * @param idEjecutivo El idPersona del ejecutivo.
     * @param razonSocial La raz&oacute; social a buscar, si se buscan personas morales.
     * @param paterno El apellido paterno a buscar, si se buscan personas f&iacute;sicas.
     * @param materno El apellido materno a buscar, si se buscan personas f&iacute;sicas.
     * @param nombre El nombre a buscar, a buscar, si se buscan personas f&iacute;sicas.
     * @param noCuenta El n&uacute;mero de contrato sica espec&iacute;fico a buscar.
     * @param soloEjecVigentes False si se permiten ejecutivos no vigentes.
     * @return List.
     * @see ClienteDao#findClientes(Integer, String, String, String, String, String, boolean).
     */
    public List findClientes(Integer idEjecutivo, String razonSocial, String paterno,
                             String materno, String nombre, String noCuenta,
                             boolean soloEjecVigentes) {
        List clientes;
        boolean conEjecutivo = idEjecutivo != null;
        if (!StringUtils.isEmpty(razonSocial)) {
            clientes = new PersonaMoralQuery(getDataSource(), conEjecutivo).execute(conEjecutivo ?
                    new Object[] { idEjecutivo, razonSocial + "%" } :
                    new Object[] { razonSocial + "%" });
        }
        else if (!StringUtils.isEmpty(noCuenta)) {
            clientes = new ContratoQuery(getDataSource(), conEjecutivo).execute(conEjecutivo ?
                    new Object[] { idEjecutivo, noCuenta } : new Object[] { noCuenta });
        }
        else {
            String query = conEjecutivo ? QUERY_PF : QUERY_PF_SE;
            boolean conMaterno = !StringUtils.isEmpty(materno);
            query = query.replaceAll("\\|", conMaterno ? " AND t5.materno like ? " : "");
            clientes = new PersonaFisicaQuery(getDataSource(), conEjecutivo, conMaterno, query).
                    execute(conMaterno ? (conEjecutivo ? new Object[]{idEjecutivo, paterno + "%",
                            materno + "%", nombre + "%"} :
                            new Object[]{paterno + "%", materno + "%", nombre + "%"}) :
                            (conEjecutivo ? new Object[]{idEjecutivo, paterno + "%",
                                    nombre + "%"} : new Object[] { paterno + "%", nombre + "%" }));
        }
        return filtrarEjecutivo(clientes, soloEjecVigentes);
    }
    
    /**
     * @param noCuenta El n&uacute;mero de contrato sica o contrato corto espec&iacute;fico a buscar.
     * @return List.
     * @see ClienteDao#findClientesparaBloqueo(String).
     */
    public List findClientesparaBloqueo(String noCuenta) {
        List clientes = null;
        String noCuentasinguion = noCuenta.replaceAll("-","").replaceAll(" ","");
        //Long noCuentasinguionInt = Long.valueOf(noCuentasinguion);
        logger.info("noCuenta ---> " + ToStringBuilder.reflectionToString(noCuenta));
        //logger.info("noCuentasinguion ---> " + ToStringBuilder.reflectionToString(noCuentasinguion));
        

        if (!StringUtils.isEmpty(noCuenta)) {
            clientes = new ClientesparaBloqueoQuery(
            		getDataSource(),
            		QUERY_CTA_BLOQUEO,
            		new SqlParameter("no_cuenta",Types.VARCHAR)
            		//,new SqlParameter("id_contrato",Types.LONGVARCHAR)//cambiar a Int
            		).execute(new Object[]{noCuenta}
            		);
            //logger.info("LO QUE TRAE EL DTO ---> " + ToStringBuilder.reflectionToString((ClienteContratoDto)clientes.get(0)));
        }

        return clientes;
    }
    
    /**
     * @param idContrato El n&uacute;mero de contrato sica o contrato corto espec&iacute;fico a buscar.
     * @return List.
     * @see ClienteDao#findClientesparaBloqueo(String).
     */
    public List findClientesparaBloqueo(int idContrato) {
        List clientes = null;
        //String noCuentasinguion = idContrato.replaceAll("-","").replaceAll(" ","");
        //Long noCuentasinguionInt = Long.valueOf(noCuentasinguion);
        logger.info("idContrato ---> " + idContrato);
        //logger.info("noCuentasinguion ---> " + ToStringBuilder.reflectionToString(noCuentasinguion));
        

        
            clientes = new ClientesparaBloqueoQuery(
            		getDataSource(),
            		QUERY_CTA_BLOQUEO_CORTO,
            		new SqlParameter("id_contrato",Types.NUMERIC)
            		//,new SqlParameter("id_contrato",Types.LONGVARCHAR)//cambiar a Int
            		).execute(idContrato);//(new Object[]{idContrato}
            		//);
            //logger.info("LO QUE TRAE EL DTO ---> " + ToStringBuilder.reflectionToString((ClienteContratoDto)clientes.get(0)));
        

        return clientes;
    }
    
    /**
     * @see ClienteDao#findClienteByContratoCorto(Integer)
     * 
     * @param contratoCorto
     * @return {@link ClienteContratoInfoPromotorDto}
     */
    public ClienteContratoInfoPromotorDto findClienteByContratoCorto(Integer contratoCorto) {
        List clientes = new ContratoCortoQuery(
        	getDataSource(), 
        	QUERY_CTA_BY_CONTRATO_CORTO + " AND t7.id_contrato = ? ",
        	new SqlParameter("contrato_corto", Types.INTEGER)
        ).execute(new Object[] { contratoCorto });
        
        if( clientes != null && (!clientes.isEmpty()) ) {
        	return (ClienteContratoInfoPromotorDto) clientes.get(0);
        } else {
        	return null;
        }
    }
    
    /**
     * @see ClienteDao#findContratoCortoByContratoSica(String)
     * 
     * @param contratoSica
     * @return {@link ClienteContratoInfoPromotorDto}
     */
    public ClienteContratoInfoPromotorDto findContratoCortoByContratoSica(String contratoSica) {
    	 List clientes = new ContratoCortoQuery(
        	getDataSource(), 
        	QUERY_CTA_BY_CONTRATO_CORTO + " AND t2.no_cuenta = ? ",
        	new SqlParameter("no_cuenta", Types.VARCHAR)
        ).execute(new Object[] { contratoSica });
        
        if( clientes != null && (!clientes.isEmpty()) ) {
        	return (ClienteContratoInfoPromotorDto) clientes.get(0);
        } else {
        	return null;
        }
    }

    private List filtrarEjecutivo(List clientes, boolean soloEjecVigentes) {
        List resultados = new ArrayList();
        // Se agregan los vigentes a los resultados:
        for (Iterator it = clientes.iterator(); it.hasNext();) {
            ClienteContratoDto dto = (ClienteContratoDto) it.next();
            if ("VIG".equals(dto.getStatusEjecutivo())) {                
                boolean encontrado = false;
                for (Iterator it2 = resultados.iterator(); it2.hasNext() && !encontrado;) {
                    ClienteContratoDto dtoResultado = (ClienteContratoDto) it2.next();
                    if (dtoResultado.getNoCuenta().equals(dto.getNoCuenta())) {
                        encontrado = true;
                    }
                }
                if (!encontrado) {
                    resultados.add(dto);
                }
                // Lo quitamos de la lista para que el arreglo clientes quede solo con los no
                // vigentes:
                it.remove();
            }
        }
        // Si acepta ejecutivos no vigentes, los agrega si no existe el contrato sica en el arreglo
        // de resultados:
        if (!soloEjecVigentes) {
            for (Iterator it = clientes.iterator(); it.hasNext();) {
                ClienteContratoDto dto = (ClienteContratoDto) it.next();
                boolean encontrado = false;
                for (Iterator it2 = resultados.iterator(); it2.hasNext() && !encontrado;) {
                    ClienteContratoDto dtoResultado = (ClienteContratoDto) it2.next();
                    if (dtoResultado.getNoCuenta().equals(dto.getNoCuenta())) {
                        encontrado = true;
                    }
                }
                if (!encontrado) {
                    resultados.add(dto);
                }
            }
        }
        return resultados;
    }

    /**
     *
     *
     * @param razonSocial La raz&oacute; social a buscar, si se buscan personas morales.
     * @param noCuenta El n&uacute;mero de contrato sica espec&iacute;fico a buscar.
     * @return List.
     * @see ClienteDao#findClientes(String, String).
     */
    public List findClientes(String razonSocial, String noCuenta) {
        List clientes;
        if (!StringUtils.isEmpty(razonSocial)) {
            clientes = new PersonaMoralQuery(getDataSource(), false).execute(
                    new Object[]{razonSocial + "%"});
        }
        else {
            clientes = new ContratoAsignacionSubsidiariosQuery(getDataSource()).execute(
                    new Object[]{noCuenta});
        }
        return filtrarEjecutivo(clientes, true);
    }

    /**
     * @param razonSocial La raz&oacute; social a buscar, si se buscan personas morales.
     * @param paterno El apellido paterno a buscar, si se buscan personas f&iacute;sicas.
     * @param materno El apellido materno a buscar, si se buscan personas f&iacute;sicas.
     * @param nombre El nombre a buscar, a buscar, si se buscan personas f&iacute;sicas.
     * @param noCuenta El n&uacute;mero de contrato sica espec&iacute;fico a buscar.
     * @param soloEjecVigentes False si se permiten ejecutivos no vigentes.
     * @return List.
     * @see com.ixe.ods.sica.dao.ClienteDao#findClientes(String, String, String, String, String,
     *          boolean).
     */
    public List findClientes(String razonSocial, String paterno, String materno, String nombre,
                             String noCuenta, boolean soloEjecVigentes) {
        return findClientes(null, razonSocial, paterno, materno, nombre, noCuenta,
                soloEjecVigentes);
    }

    /**
     * @param nombreCorto El nombre corto del cliente.
     * @param idsPromotores La lista con idPersona de los promotores.
     * @return List.
     * @see ClienteDao#findClientes(String, java.util.List).
     */
    public List findClientes(String nombreCorto, List idsPromotores) {
        String query = QUERY_JERARQ;
        StringBuffer ids = new StringBuffer();
        for (int i = 0; i < idsPromotores.size(); i++) {
            ids.append(idsPromotores.get(i));
            if (i < idsPromotores.size() - 1) {
                ids.append(",");
            }
        }
        query = query.replaceAll("\\|", ids.toString());
        query = query.replaceAll("\\@", nombreCorto);
        return new JerarquiaQuery(getDataSource(), query).execute();
    }

    /**
     * Clase interna para generar el query para Personas Morales.
     */
    class PersonaMoralQuery extends MappingSqlQuery {

        /**
         * Constructor por default.
         *
         * @param ds El dataSource con el que se va a trabajar.
         * @param conEjecutivo True para restringir el ejecutivo o false para no restringirlo.
         */
        public PersonaMoralQuery(DataSource ds, boolean conEjecutivo) {
            super(ds, conEjecutivo ? QUERY_PM : QUERY_PM_SE);
            if (conEjecutivo) {
                super.declareParameter(new SqlParameter("id_persona", Types.INTEGER));
            }
            super.declareParameter(new SqlParameter("razon_social", Types.VARCHAR));
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
            return new ClienteContratoDto(rs.getString("no_cuenta"), rs.getString("razon_social"),
                    "PM", Boolean.valueOf("S".equals(rs.getString("es_grupo"))),
                    new Integer(rs.getInt("id_sector")), new Integer(rs.getInt("id_persona")),
                    rs.getObject("id_grupo_empresarial") == null ?
                            null : new Integer(rs.getInt("id_grupo_empresarial")),
                    new Integer(rs.getInt("id_ejec")),
                    rs.getObject("id_gpo_empresarial") == null ?
                            null : new Integer(rs.getInt("id_gpo_empresarial")),
                    rs.getString("statusEjecutivo"),
                    rs.getObject("id_Bloqueo") == null ?
                    		null : new Integer (rs.getInt("id_Bloqueo")));
        }
    }

    /**
     * Clase interna para generar el query para Personas F&iacute;sicas.
     */
    class PersonaFisicaQuery extends MappingSqlQuery {

        /**
         * Constructor por default.
         *
         * @param ds El dataSource con el que se va a trabajar.
         * @param conEjecutivo True para restringir el ejecutivo o false para no restringirlo.
         * @param conMaterno True para incluir la restricci&oacute;n por apellido materno.
         * @param query El query a ejecutar.
         */
        public PersonaFisicaQuery(DataSource ds, boolean conEjecutivo, boolean conMaterno,
                                  String query) {
            super(ds, query);
            if (conEjecutivo) {
                super.declareParameter(new SqlParameter("id_persona", Types.INTEGER));
            }
            super.declareParameter(new SqlParameter("paterno", Types.VARCHAR));
            if (conMaterno) {
                super.declareParameter(new SqlParameter("materno", Types.VARCHAR));
            }
            super.declareParameter(new SqlParameter("nombre", Types.VARCHAR));
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
            return new ClienteContratoDto(rs.getString("no_cuenta"), rs.getString("nombre") + " " +
                    rs.getString("paterno") + " " + rs.getString("materno"), "PF", Boolean.FALSE,
                    new Integer(rs.getInt("id_sector")), new Integer(rs.getInt("id_persona")), null,
                    new Integer(rs.getInt("id_ejec")), null, rs.getString("statusEjecutivo"),new Integer(rs.getInt("id_Bloqueo")));

        }
    }

    /**
     * Clase interna para generar el query para Contratos Sica.
     */
    class ContratoQuery extends MappingSqlQuery {

        /**
         * Constructor por default.
         *
         * @param ds El dataSource con el que se va a trabajar.
         * @param conEjecutivo True para restringir el ejecutivo o false para no restringirlo.
         */
        public ContratoQuery(DataSource ds, boolean conEjecutivo) {
            super(ds, conEjecutivo ? QUERY_CTA : QUERY_CTA_SE);
            if (conEjecutivo) {
                super.declareParameter(new SqlParameter("id_persona", Types.INTEGER));
            }
            super.declareParameter(new SqlParameter("no_cuenta", Types.VARCHAR));
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
            return new ClienteContratoDto(rs.getString("no_cuenta"), rs.getString("nombre_corto"),
                    rs.getString("tipo_persona"),
                    Boolean.valueOf("S".equals(rs.getString("es_grupo"))),
                    new Integer(rs.getInt("id_sector")), new Integer(rs.getInt("id_persona")), null,
                    new Integer(rs.getInt("id_ejec")), null, rs.getString("statusEjecutivo"),new Integer(rs.getInt("id_Bloqueo")));
        }
    }
    
    /**
     * Clase interna para generar el query para buscar contratos cortos.
     */
    class ContratoCortoQuery extends MappingSqlQuery {

        /**
         * Constructor por default.
         *
         * @param ds El dataSource con el que se va a trabajar.
         * @param conEjecutivo True para restringir el ejecutivo o false para no restringirlo.
         */
        public ContratoCortoQuery(DataSource ds, String sqlQuery, SqlParameter sqlParam) {
            super(ds, sqlQuery);
            super.declareParameter(sqlParam);
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
            return new ClienteContratoInfoPromotorDto(
            	rs.getString("no_cuenta"), 
            	rs.getString("nombre_corto"),
                rs.getString("tipo_persona"), 
                new Integer(rs.getInt("id_persona")), 
                new Integer(rs.getInt("id_ejec")), 
                rs.getString("statusEjecutivo"), 
                rs.getString("nombre_promotor"), 
                rs.getString("clave_usuario_promotor"),
                new Integer(rs.getInt("contrato_corto")),
                new Integer (rs.getInt("id_Bloqueo"))
            );
        }
    }
    
    /**
     * Clase interna para generar el query para buscar contratos cortos.
     */
    class ClientesparaBloqueoQuery extends MappingSqlQuery {
    /**
     * Constructor por default.
     *
     * @param ds El dataSource con el que se va a trabajar.
     * @param el query para extraer los clientes por contrato o contrato corto
     * @param la cuenta
     * @param el contrato corto
     */
    public ClientesparaBloqueoQuery(DataSource ds, String sqlQuery, SqlParameter sqlParam){//,SqlParameter sqlParam1) {
        super(ds,sqlQuery);
        super.declareParameter(sqlParam);
        //super.declareParameter(sqlParam1);
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
        return new ClienteContratoDto(rs.getString("no_cuenta"),
        		new Integer (rs.getInt("id_contrato")),
                rs.getString("nombre_corto"),
                new Integer(rs.getInt("id_bloqueo")),rs.getString("descripcion"), rs.getString("fecha_ult_mod"),
                rs.getString("usuario_ult_mod"), rs.getString("sic"));
    }
    }

    /**
     * Clase interna para generar el query para Contratos Sica en Asignaci&oacute;n de Subsidiarios.
     */
    class ContratoAsignacionSubsidiariosQuery extends MappingSqlQuery {

        /**
         * Constructor por default.
         *
         * @param ds El dataSource con el que se va a trabajar.
         */
        public ContratoAsignacionSubsidiariosQuery(DataSource ds) {
            super(ds, QUERY_CTA_SE_AS);
            super.declareParameter(new SqlParameter("no_cuenta", Types.VARCHAR));
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
            return new ClienteContratoDto(rs.getString("no_cuenta"), rs.getString("razon_social"),
                    "PM", Boolean.valueOf("S".equals(rs.getString("es_grupo"))),
                    new Integer(rs.getInt("id_sector")), new Integer(rs.getInt("id_persona")),
                    rs.getObject("id_grupo_empresarial") == null ?
                            null : new Integer(rs.getInt("id_grupo_empresarial")),
                    null, rs.getObject("id_gpo_empresarial") == null ?
                            null : new Integer(rs.getInt("id_gpo_empresarial")),
                    rs.getString("statusEjecutivo"),new Integer(rs.getInt("id_Bloqueo")));


        }
    }

/**
     * Clase interna para generar el query de clientes por Jerarqu&iacute;a.
     */
    class JerarquiaQuery extends MappingSqlQuery {

        /**
         * Constructor por default.
         *
         * @param ds El dataSource con el que se va a trabajar.
         * @param query El Query a utilizar.
         */
        public JerarquiaQuery(DataSource ds, String query) {
            super(ds, query);
        }

        /**
         * Regresa un arreglo de dos posiciones, en la primera se encuentra el n&uacute;mero de
         * contrato sica, y en la segunda, el nombre corto del cliente.
         *
         * @param rs El result set.
         * @param i El n&uacute;mero de registro.
         * @return Object.
         * @throws SQLException Si no se puede leer alg&uacute;n valor.
         */
        protected Object mapRow(ResultSet rs, int i) throws SQLException {
            return new Object[]{rs.getString("no_cuenta"), rs.getString("nombre_corto"),
                    new Integer(rs.getInt("id_persona"))};
        }
    }

    
    public Map findUsuarioByIdPersona(Integer idPersona) {
		return getJdbcTemplate().queryForMap(QUERY_USUARIO_BY_ID_PERSONA, new Object[]{idPersona});
	}
    
    public Map findPromotorAsignadoCliente(Integer idPersonaCliente) {
		Map personaContrato = getJdbcTemplate().queryForMap(QUERY_PROMOTOR_ASOCIADO_CLIENTE, new Object[]{idPersonaCliente});
		
		return personaContrato;
	}
    
    private static String QUERY_USUARIO_BY_ID_PERSONA = "SELECT SU.* FROM BUPIXE.SEGU_USUARIO SU , SEGU_PERFIL SP "+ 
    		"WHERE SU.ID_USUARIO=SP.ID_USUARIO AND ID_PERSONA=? AND SP.SISTEMA='SICA'";
    
    private static String QUERY_PROMOTOR_ASOCIADO_CLIENTE = "SELECT BCE.ID_PERSONA, BCC.NO_CUENTA FROM BUPIXE.BUP_PERSONA_CUENTA_ROL BCR "
    		+ ",BUPIXE.BUP_CUENTA_EJECUTIVO BCE, BUPIXE.BUP_CUENTA_CONTRATO BCC "
    		+ "WHERE BCR.ID_PERSONA = ? AND BCR.NO_CUENTA=BCE.NO_CUENTA AND BCR.STATUS= BCE.STATUS AND "
    		+ "BCR.NO_CUENTA=BCC.NO_CUENTA AND BCC.ID_TIPO_CUENTA='CAM10' AND BCR.STATUS='VIG' AND BCR.ID_ROL='TIT' ";
    
    /**
     * El query para personas morales.
     */
    private static String QUERY_PM = "SELECT t1.no_cuenta, t5.razon_social, " +
            "t5.es_grupo, t6.id_sector, t6.id_persona, t5.id_grupo_empresarial, " +
            "t2.id_grupo_empresarial id_gpo_empresarial, t1.id_persona id_ejec, " +
            "t1.status statusEjecutivo, t7.id_bloqueo FROM bup_cuenta_ejecutivo t1, " +
            "bup_contrato_sica t2, bup_cuenta_contrato t3, bup_persona_cuenta_rol t4, " +
            "bup_persona_moral t5, bup_persona t6, sc_contrato t7 WHERE t1.id_persona = ? AND t1.status = " +
            "'VIG' AND t2.no_cuenta = t1.no_cuenta AND t3.no_cuenta = t2.no_cuenta AND " +
            "t3.status_origen = 'Active' AND t3.status = 'ACTIVA' AND t4.no_cuenta = " +
            "t2.no_cuenta AND t4.id_rol = 'TIT' AND t4.status = 'VIG' AND t4.id_persona = " +
            "t5.id_persona AND t5.razon_social like ? AND t5.id_persona = t6.id_persona AND t3.no_cuenta = t7.no_cuenta(+) " +
            "AND ROWNUM < 51 ORDER BY t5.razon_social";

    /**
     * El query para personas morales sin ejecutivo.
     */
    private static String QUERY_PM_SE = "SELECT t2.no_cuenta, t5.razon_social, t5.es_grupo," +
            " t6.id_sector, t6.id_persona, t5.id_grupo_empresarial, t2.id_grupo_empresarial " +
            "id_gpo_empresarial, t1.id_persona id_ejec, t1.status statusEjecutivo, t7.id_bloqueo FROM " +
            "bup_cuenta_ejecutivo t1, bup_contrato_sica t2, bup_cuenta_contrato t3," +
            " bup_persona_cuenta_rol t4, bup_persona_moral t5, bup_persona t6, sc_contrato t7 WHERE " +
            "t5.razon_social like ? AND t3.no_cuenta = t2.no_cuenta AND t3.status_origen = " +
            "'Active' AND t3.status = 'ACTIVA' AND t4.no_cuenta = t2.no_cuenta AND t4.id_rol = " +
            "'TIT' AND t4.status = 'VIG' AND t4.id_persona = t5.id_persona AND " +
            "t5.id_persona = t6.id_persona AND t2.no_cuenta = t1.no_cuenta(+) AND t3.no_cuenta = t7.no_cuenta(+) AND ROWNUM < 51 " +
            "ORDER BY t5.razon_social";

    /**
     * El query para b&uacute;squeda por n&uacute;mero de cuenta.
     */
    private static String QUERY_CTA = "SELECT t1.no_cuenta, t5.nombre_corto, t5.tipo_persona," +
            " t5.id_sector, CASE WHEN t6.id_persona IS NULL THEN NULL ELSE t6.es_grupo END " +
            "es_grupo, t5.id_persona, t1.id_persona id_ejec, t1.status statusEjecutivo,t7.id_bloqueo FROM " +
            "bup_cuenta_ejecutivo t1, bup_contrato_sica t2, " +
            "bup_cuenta_contrato t3, bup_persona_cuenta_rol t4, bup_persona t5, " +
            "bup_persona_moral t6, sc_contrato t7 WHERE t1.id_persona = ? AND t1.status = 'VIG' " +
            "AND t1.no_cuenta = ? AND t2.no_cuenta = t1.no_cuenta AND t3.no_cuenta = " +
            "t2.no_cuenta AND t3.status_origen = 'Active' AND t3.status = 'ACTIVA'" +
            " AND t4.no_cuenta = t2.no_cuenta AND t4.id_rol = 'TIT' AND t4.status = 'VIG' " +
            "AND t4.id_persona = t5.id_persona AND t5.id_persona = t6.id_persona(+) AND t3.no_cuenta = t7.no_cuenta(+)" +
            "AND ROWNUM < 51 ORDER BY t5.nombre_corto";
    
    /**
     * Query para buscar por contrato corto
     */
    private static String QUERY_CTA_BY_CONTRATO_CORTO = 
    	  "SELECT "
    	+ "    	t2.no_cuenta, " 
    	+ "		t5.nombre_corto, " 
    	+ "		t5.tipo_persona, "
    	+ "		t5.id_persona, " 
    	+ "		t1.id_persona id_ejec, " 
    	+ "		t1.status statusEjecutivo, "
    	+ "    	t8.nombre_corto nombre_promotor, " 
    	+ "		t9.clave_usuario clave_usuario_promotor, "
    	+ "		t7.id_contrato contrato_corto, "
    	+ "		t7.id_bloqueo "
    	+ " FROM "
    	+ "    	bup_cuenta_ejecutivo t1, " 
    	+ "		bup_contrato_sica t2, " 
    	+ "		bup_cuenta_contrato t3, "
    	+ "    	bup_persona_cuenta_rol t4, " 
    	+ "		bup_persona t5, " 
    	+ "		bup_persona_moral t6, " 
    	+ "		sc_contrato t7, "
    	+ "	   	bup_persona t8, " 
    	+ "		bup_empleado_ixe t9 "
    	+ " WHERE "
    	+ "		t3.no_cuenta = t2.no_cuenta "
    	+ "		AND t4.no_cuenta = t2.no_cuenta "
    	+ "		AND t3.no_cuenta = t1.no_cuenta " // Debe tener un ejecutivo asignado, tenia (+)
    	+ "		AND t4.id_persona = t5.id_persona " 
    	+ "		AND t5.id_persona = t6.id_persona(+) "
    	+ "    	AND t7.no_cuenta = t2.no_cuenta " // realcion contrato corto, contrato sica
    	+ "	   	AND t1.id_persona = t8.id_persona " // para saber nombre del promotor
    	+ "    	AND t1.id_persona = t9.id_persona " // para saber clave de usuario del promotor 
    	+ "		AND t3.status_origen = 'Active' "
    	+ "    	AND t3.status = 'ACTIVA' "
    	+ "		AND t1.status = 'VIG'"
    	+ "		AND t4.id_rol = 'TIT' " // TODO: Verificar que significa
    	+ "    	AND t4.status = 'VIG' "; // TODO: Verificar que significa
    
    /**
     * El query para b&uacute;squeda por n&uacute;mero de cuenta.
     */
    private static String QUERY_CTA_BLOQUEO = 
  	  "SELECT "
    	+ "    	d.no_cuenta, " 
    	+ "		a.id_contrato, " 
    	+ "		nombre_corto, "
    	+ "		a.id_bloqueo, "
    	+ "		e.descripcion, "
    	+ "		a.fecha_ult_mod, " 
    	+ "		decode(a.usuario_ult_mod,null,null,(select nombre_corto from segu_usuario su,bup_persona per where su.id_usuario = a.usuario_ult_mod and su.id_persona = per.id_persona)) usuario_ult_mod, "
    	+ "     cte.sic "
    	+ " FROM "
    	+ "    	sc_contrato a, " 
    	+ "		bup_persona_cuenta_rol b, " 
    	+ "		bup_persona c, "
    	+ "    	bup_cuenta_contrato d, "
    	+ "    	sc_tipo_bloqueo e, "
    	+ "     sc_cliente cte "
    	+ " WHERE "
    	+ "		a.no_cuenta (+) = b.no_cuenta "
    	+ "		AND b.id_persona = c.id_persona "
    	+ "     AND b.id_persona = cte.id_persona " 
        + "     AND a.id_cliente = cte.id_cliente "
    	+ "		AND b.no_cuenta = d.no_cuenta " 
    	+ "		AND a.id_bloqueo = e.id_bloqueo(+) "
    	+ "		AND d.status_origen = 'Active' "
    	+ "    	AND d.status = 'ACTIVA' "
    	+ "		AND b.status = 'VIG' "
    	+ "		AND b.id_rol = 'TIT' "
       	+ "AND  d.no_cuenta = ? ";
    
    /**
     * El query para b&uacute;squeda por contrato corto.
     */
    private static String QUERY_CTA_BLOQUEO_CORTO = 
  	  "SELECT "
    	+ "    	d.no_cuenta, " 
    	+ "		a.id_contrato, " 
    	+ "		nombre_corto, "
    	+ "		a.id_bloqueo, "
    	+ "		e.descripcion, "
    	+ "		a.fecha_ult_mod, " 
    	+ "		decode(a.usuario_ult_mod,null,null,(select nombre_corto from segu_usuario su,bup_persona per where su.id_usuario = a.usuario_ult_mod and su.id_persona = per.id_persona)) usuario_ult_mod "
    	+ " FROM "
    	+ "    	sc_contrato a, " 
    	+ "		bup_persona_cuenta_rol b, " 
    	+ "		bup_persona c, "
    	+ "    	bup_cuenta_contrato d, "
    	+ "    	sc_tipo_bloqueo e "
    	+ " WHERE "
    	+ "		a.no_cuenta (+) = b.no_cuenta "
    	+ "		AND b.id_persona = c.id_persona "
    	+ "		AND b.no_cuenta = d.no_cuenta " 
    	+ "		AND a.id_bloqueo = e.id_bloqueo(+) "
    	+ "		AND d.status_origen = 'Active' "
    	+ "    	AND d.status = 'ACTIVA' "
    	+ "		AND b.status = 'VIG' "
    	+ "		AND b.id_rol = 'TIT' "
       	+ "     AND a.id_contrato = ? ";

    /**
     * El query para b&uacute;squeda por n&uacute;mero de cuenta.
     */
    private static String QUERY_CTA_SE = "SELECT t2.no_cuenta, t5.nombre_corto, t5.tipo_persona, " +
            "t5.id_sector, CASE WHEN t6.id_persona IS NULL THEN NULL ELSE t6.es_grupo END " +
            "es_grupo, t5.id_persona, t1.id_persona id_ejec, t1.status statusEjecutivo, t7.id_bloqueo FROM " +
            "bup_cuenta_ejecutivo t1, bup_contrato_sica t2, bup_cuenta_contrato t3, " +
            "bup_persona_cuenta_rol t4, bup_persona t5, bup_persona_moral t6, sc_contrato t7 WHERE" +
            " t2.no_cuenta = ? AND t3.no_cuenta = t2.no_cuenta AND t3.status_origen = 'Active' " +
            "AND t3.status = 'ACTIVA' AND t4.no_cuenta = t2.no_cuenta AND t4.id_rol = 'TIT' " +
            "AND t4.status = 'VIG' AND t4.id_persona = t5.id_persona AND " +
            "t3.no_cuenta = t1.no_cuenta(+) AND " +
            "t5.id_persona = t6.id_persona(+) AND t3.no_cuenta = t7.no_cuenta(+) AND ROWNUM < 51 ORDER BY t5.nombre_corto";

    /**
     * El query para b&uacute;squeda por n&uacute;mero de cuenta para la Asignaci&oacute;n de
     * Subsidiarios.
     */
    private static String QUERY_CTA_SE_AS = "SELECT t2.no_cuenta, t5.razon_social, t5.es_grupo, " +
            "t6.id_sector, t6.id_persona, t5.id_grupo_empresarial, t2.id_grupo_empresarial " +
            "id_gpo_empresarial, t1.status statusEjecutivo, t7.id_bloqueo FROM bup_cuenta_ejecutivo t1, " +
            "bup_contrato_sica t2, bup_cuenta_contrato t3, " +
            "bup_persona_cuenta_rol t4, bup_persona_moral t5, bup_persona t6, sc_contrato t7 WHERE " +
            "t2.no_cuenta = ? AND t3.no_cuenta = t2.no_cuenta AND t3.status_origen = 'Active' " +
            "AND t3.status = 'ACTIVA' AND t4.no_cuenta = t2.no_cuenta AND t4.id_rol = 'TIT' AND " +
            "t4.status = 'VIG' AND t3.no_cuenta = t1.no_cuenta(+) AND t4.id_persona = " +
            "t5.id_persona AND t5.id_persona = " +
            "t6.id_persona AND t3.no_cuenta = t7.no_cuenta(+) AND ROWNUM < 51 ORDER BY t5.razon_social";

    /**
     * El query para personas f&iacute;sicas.
     */
    private static String QUERY_PF = "SELECT t1.no_cuenta, t5.paterno, t5.materno, t5.nombre, " +
            "t6.id_sector, t6.id_persona, t1.id_persona id_ejec, t1.status statusEjecutivo, t7.id_bloqueo FROM " +
            "bup_cuenta_ejecutivo t1, bup_contrato_sica t2, " +
            "bup_cuenta_contrato t3, bup_persona_cuenta_rol t4, bup_persona_fisica t5, " +
            "bup_persona t6, sc_contrato t7 WHERE t1.id_persona = ? AND t1.status = 'VIG' AND " +
            "t2.no_cuenta = t1.no_cuenta AND t3.no_cuenta = t2.no_cuenta AND " +
            "t3.status_origen = 'Active' AND t3.status = 'ACTIVA' AND" +
            " t4.no_cuenta = t2.no_cuenta AND t4.id_rol = 'TIT' AND t4.status = 'VIG' " +
            "AND t4.id_persona = t5.id_persona AND t5.paterno like ? | AND t5.nombre like ? " +
            "AND t5.id_persona = t6.id_persona AND t3.no_cuenta = t7.no_cuenta(+) AND ROWNUM < 51 ORDER BY t5.nombre, t5.paterno, " +
            "t5.materno";

    /**
     * El query para personas f&iacute;sicas sin ejecutivo.
     */
    private static String QUERY_PF_SE = "SELECT t2.no_cuenta, t5.paterno, t5.materno, t5.nombre, " +
            "t6.id_sector, t6.id_persona, t1.id_persona id_ejec, t1.status statusEjecutivo, t7.id_bloqueo FROM " +
            "bup_persona_fisica t5, bup_persona t6, bup_persona_cuenta_rol t4, " +
            "bup_cuenta_contrato t3, bup_contrato_sica t2, bup_cuenta_ejecutivo t1, sc_contrato t7 " +
            "WHERE t5.paterno like ? | AND t5.nombre like ? AND t5.id_persona = t6.id_persona " +
            "AND t5.id_persona = t4.id_persona AND t3.no_cuenta = t2.no_cuenta " +
            "AND t4.no_cuenta = t2.no_cuenta AND t4.no_cuenta = t1.no_cuenta(+) AND " +
            "t4.id_rol = 'TIT' AND t4.status = 'VIG' AND t3.status_origen = 'Active' AND " +
            "t3.status = 'ACTIVA' AND t3.no_cuenta = t7.no_cuenta(+) AND ROWNUM < 51 ORDER BY t5.nombre, t5.paterno, t5.materno";

    /**
     * El query para b&uacute;squeda por jerarquia.
     */
    private static String QUERY_JERARQ = "SELECT t1.no_cuenta, t5.nombre_corto, t5.id_persona " +
            "FROM bup_cuenta_ejecutivo t1, bup_contrato_sica t2, " +
            "bup_cuenta_contrato t3, bup_persona_cuenta_rol t4, bup_persona t5 " +
            "WHERE t1.id_persona in (|) AND t1.status = 'VIG' " +
            "AND t2.no_cuenta = t1.no_cuenta AND t3.no_cuenta = t2.no_cuenta " +
            "AND t3.status_origen = 'Active' AND t3.status = 'ACTIVA' AND " +
            "t4.no_cuenta = t2.no_cuenta AND t4.id_rol = 'TIT' AND t4.status = 'VIG' " +
            "AND t4.id_persona = t5.id_persona AND t5.nombre_corto like '@%' " +
            "ORDER BY t5.nombre_corto";

	

	
}
