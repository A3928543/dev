/**
 * 
 */
package com.ixe.ods.sica.dao;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import javax.sql.DataSource;

import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.object.MappingSqlQuery;

import com.ixe.ods.sica.dto.OperacionClienteBanorteCrDto;

/**
 * @author PEspinosa, IGODINEZ
 *
 */
public class JdbcReportesDaoImpl extends JdbcDaoSupport implements ReportesDao {

	/**
	 * 
	 */
	public JdbcReportesDaoImpl() {
		super();
	}
	
	/**
	 * Consulta el CR_GENERICO almacenado en SC_PARAMETRO
	 * @return String CR_GENERICO
	 */
	public String findCrGenerico()
	{
		return (String)getJdbcTemplate().queryForObject(QUERY_CR_GENERICO, String.class);
	}
	
	/**
	 * Consulta los deals y sus detalles para generar el reporte 'Clientes Banorte en SICA'. 
	 * Los deals deben tener cr asignado. 25/03/2015 11:48 am
	 * @param fechaInicial 
	 * @param fechaFinal
	 * @return List
	 */
	public List findCrClientesBanorteEnSica(Date fechaInicial, Date fechaFinal)
	{
		List resultado = null;
    	List parametros = new ArrayList();
    	StringBuffer finalQuery = new StringBuffer(QUERY_CR_2); // QUERY_CR_OPERACIONES_CLIENTES_BANORTE_EN_SICA
    	parametros.add(fechaInicial);
    	parametros.add(fechaFinal);
    	parametros.add(fechaInicial);
    	parametros.add(fechaFinal);
    	
    	resultado = new CrOperacionesClientesBanorteEnSicaQuery(getDataSource(), 
    			        finalQuery.toString()).execute(parametros.toArray());
    	
    	return resultado;
	}
	
	/**
     * Clase interna para accesar a las propiedades de los registros obtenidos
     * para el reporte CrClientesBanorteEnSica.
     */
	class CrOperacionesClientesBanorteEnSicaQuery extends MappingSqlQuery {

    	/**
    	 * Constructor de la clase.
    	 * 
    	 *  @param ds El dataSource.
    	 *  @param finalQuery El query armado de acuerdo a las opciones y condiciones seleccionadas
    	 */
    	public CrOperacionesClientesBanorteEnSicaQuery(DataSource ds, String finalQuery) {
    		super(ds,finalQuery);

    		super.declareParameter(new SqlParameter("deal.fecha_liquidacion", Types.DATE));
    		super.declareParameter(new SqlParameter("deal.fecha_liquidacion", Types.DATE));
    		super.declareParameter(new SqlParameter("deal.fecha_liquidacion", Types.DATE));
    		super.declareParameter(new SqlParameter("deal.fecha_liquidacion", Types.DATE));

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
    		BigDecimal monto, tipoCambioMesa, tipoCambioPosicion;

    		monto = rs.getString("monto") != null ? new BigDecimal(rs.getString("monto")) : new BigDecimal("0");
    		tipoCambioMesa = rs.getString("tipo_cambio_mesa") != null ? new BigDecimal(rs.getString("tipo_cambio_mesa")) : new BigDecimal("0");
    		tipoCambioPosicion = rs.getString("tipo_cambio") != null ? new BigDecimal(rs.getString("tipo_cambio")) : new BigDecimal("0");
    		  		
    		return new OperacionClienteBanorteCrDto(
    				new Integer(rs.getInt("id_deal")),
    				rs.getString("no_cuenta"),
    				new Integer(rs.getInt("cr")),
    				rs.getString("id_divisa"),
    				rs.getString("mnemonico"),
    				rs.getString("nombrePromotor"),
    				rs.getString("cliente"),
    				rs.getString("id_canal"),
    				rs.getString("sic"),
    				rs.getString("cuentaAltamira"),
    				monto,
    				tipoCambioMesa,
    				tipoCambioPosicion,
    				(rs.getString("recibimos") != null && "S".equals(rs.getString("recibimos")) ? true : false),
    				rs.getInt("folio_detalle")
    		);
        }
    }
	
	//*************************************************************************************************
	
	/**
	 * Encuentra las polizas contables y el nombre del cliente a quien pertenece
	 * entre el rango de fechas seleccionado.
	 * 
	 * @param diaInicial Dia desde el que se quieren obtener las polizas.
	 * @param diaFinal Dia hasta el que se quieren obtener las polizas.
	 * @return List.
	 */
	
	public List findPolizasContables(Date diaInicial, Date diaFinal) {
		List results = null;
		List res=new LinkedList();
		results = this.getJdbcTemplate().queryForList(QUERY_POLIZAS_CONTABLES, new Object[]{diaInicial, diaFinal});
		/*
		Iterator it=results.iterator();
		while(it.hasNext()){
			LinkedHashMap row=(LinkedHashMap)it.next();
			Object []rowMaped=new Object[]{ row.get("NOMBRE_CORTO")
					,row.get("ID_POLIZA")
					,new Date(((Timestamp)row.get("FECHA_VALOR")).getTime())
					,row.get("CUENTA_CONTABLE")
					,row.get("ENTIDAD")
					,row.get("CARGO_ABONO")
					,row.get("ID_DIVISA")
					,new Double(row.get("IMPORTE").toString())
					,new Integer(row.get("ID_DEAL").toString())
					,row.get("REFERENCIA")
					,new Date(((Timestamp)row.get("FECHA_CREACION")).getTime())
			        ,row.get("TIPO_DEAL")
			        ,row.get("SUCURSAL_ORIGEN")
					,row.get("CLIENTE") != null ? row.get("CLIENTE") : ""
					,row.get("FECHA_PROCESAMIENTO")					
			        ,new Integer(row.get("STATUS_PROCESO").toString())
			        ,new Integer(row.get("FOLIO_DETALLE").toString())
			        ,row.get("NO_CONTRATO_SICA")			
			        ,new Double(row.get("TIPO_CAMBIO").toString())
			        ,row.get("CLAVE_REFERENCIA") == null ?"":row.get("CLAVE_REFERENCIA") 
					};
			res.add(rowMaped);
		}
		*/
		/*
		return new ParametrosPolizasContablesQuery(getDataSource()).execute(new Object[]{diaInicial, diaFinal});
		*/
		return results;
	}
	
	/**
     * Clase interna para accesar a las propiedades de los registros obtenidos
     * para el reporte de Polizas Contables.
     * 
     * @author Pedro M. Espinosa (espinosapm)
     *
     */
    class ParametrosPolizasContablesQuery extends MappingSqlQuery {

    	/**
    	 * Constructor de la clase.
    	 * 
    	 *  @param ds El dataSource.
    	 */
    	public ParametrosPolizasContablesQuery(DataSource ds) {
    		
    		super(ds,QUERY_POLIZAS_CONTABLES);
    		super.declareParameter(new SqlParameter("FECHA_CREACION", java.sql.Types.DATE));
    		super.declareParameter(new SqlParameter("FECHA_CREACION", java.sql.Types.DATE));

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
      
        	return new Object[] { 
        		rs.getString("NOMBRE_CORTO"), 			//Nombre Corto
        		rs.getString("ID_POLIZA"), 			//Id poliza
        		rs.getDate("FECHA_VALOR"),				//Fecha valor
        		rs.getString("CUENTA_CONTABLE"),			//Cuenta contable
        		rs.getString("ENTIDAD"),			//Entidad
        		rs.getString("CARGO_ABONO"),			//Cargo abono
        		rs.getString("ID_DIVISA"),			//Id_divisa
        		new Double(rs.getDouble("IMPORTE")),//Importe
        		new Integer(rs.getInt("ID_DEAL")),	//Id deal
        		rs.getString("REFERENCIA"),			//Referencia
        		rs.getDate("FECHA_CREACION"),				//Fecha creacion
        		rs.getString("TIPO_DEAL"),			//Tipo deal
        		rs.getString("SUCURSAL_ORIGEN"),			//Sucursal Origen
            	rs.getString("CLIENTE") != null ? rs.getString("CLIENTE") : "", 	//Cliente
            	rs.getDate("FECHA_PROCESAMIENTO") != null ? rs.getDate("FECHA_PROCESAMIENTO") :  null,		//Fecha Procesamiento
            	new Integer(rs.getInt("STATUS_PROCESO")),	//Status proceso
            	new Integer(rs.getInt("FOLIO_DETALLE")),	//Folio detalle
            	rs.getString("NO_CONTRATO_SICA"),			//No contrato SICA
            	new Double(rs.getDouble("TIPO_CAMBIO")),//Tipo cambio
            	rs.getString("CLAVE_REFERENCIA") == null ?"":rs.getString("CLAVE_REFERENCIA")            	
            };
        }
    }
    
    /**
     * Constantes para identificar los filtros generales para el origen de la operación (canal) en la generación del
     * reporte de compra venta
     */
    public static final String ORIG_OPER_ALL = "origOperAll";
    public static final Integer DIVISIONES_ALL = new Integer("0");
    public static final Integer PLAZAS_ALL = new Integer("0");
    public static final Integer SUCURSALES_ALL = new Integer("0");
    public static final Integer ZONAS_ALL = new Integer("0");
    public static final String TIPO_CLIENTE_ALL = "TODOS";
    
	public static final String ORIG_OPER_SOLO_CNLS_SUC = "origOperSoloCanalesSucursales";
	public static final String ORIG_OPER_SOLO_CNLS_PRM = "origOperSoloCanalesPromocion";
    
    /**
     * Realiza la b&uacute;squeda para generar el reporte de operaciones de compras/ventas.
     *
     * @param idDivisa El id de la divisa seleccionada.
     * @param formaLiquidacion La forma de liquidacion.
     * @param operacion El tipo de operacion (compra o venta).
     * @param origen El origen de la operacion (teller, resto o todas)
     * @param promotor El nombre del promotor.
     * @param desde La fecha de inicio de la busqueda.
     * @param hasta La fecha de fin de la busqueda.
     * @param montoMinimo El monto minimo de la busqueda.
     * @param montoMaximo El monto maximo de la busqueda.
     * @return List.
     */
    public List findReporteOperacionesComprasVentas(
    	String idDivisa, 
    	String formaLiquidacion, 
    	String operacion, 
    	String origen, 
    	Integer promotor, 
    	Date desde, 
    	Date hasta, 
    	double montoMinimo, 
    	double montoMaximo,
    	Integer division,
        Integer plaza,
    	Integer sucursal,
        Integer zona,
        String contratoSica,
        String tipoCliente,
        boolean esMontoEquivUsd,
        List usuariosNoStaff
    ){
    	List resultado = null;
    	List parametros = new ArrayList();
    	StringBuffer finalQuery = new StringBuffer(QUERY_OPERACIONES_COMPRAS_VENTAS);
    	
    	if(esMontoEquivUsd) {	// ==== EQUIV USD === : Determina el filtro a aplicar para el monto
    		finalQuery.append(QUERY_OCV_CONDICION_MONTO_USD);
    	} else {
    		finalQuery.append(QUERY_OCV_CONDICION_MONTO);
    	}
    	
    	parametros.add(desde);
    	parametros.add(hasta);
    	
    	parametros.add(new Double(montoMinimo));
    	if (montoMaximo != 0){
    	parametros.add(new Double(montoMaximo));
    	}
    	else{
    		parametros.add(new Double(Double.POSITIVE_INFINITY));
    	}
    	
    	List variables = new ArrayList();
    	
    	
    	if(!"0".equals(idDivisa.toString())){
    		finalQuery.append(QUERY_OCV_CONDICION_DIVISA);
    		parametros.add(idDivisa);
    		variables.add(new Object[]{"deal_pos.ID_DIVISA",new Integer(Types.VARCHAR)});
    	}
    	if(!"TODOS".equalsIgnoreCase(formaLiquidacion.toString())){
    		finalQuery.append(QUERY_OCV_CONDICION_PRODUCTO);
    		parametros.add(formaLiquidacion);
    		variables.add(new Object[]{"deal_detalle.clave_forma_liquidacion",new Integer(Types.VARCHAR)});
    	}
    	if(!"TODAS".equalsIgnoreCase(operacion.toString())){
    		finalQuery.append(QUERY_OCV_CONDICION_OPERACION);
    		if("Compra".equals(operacion))
    			parametros.add("S");
    		else
    			parametros.add("N");
    		variables.add(new Object[]{"deal_pos.recibimos",new Integer(Types.VARCHAR)});
    	}
    	if(promotor.intValue()>0){
    		finalQuery.append(QUERY_OCV_CONDICION_PROMOTOR);
    		parametros.add(promotor);
    		variables.add(new Object[]{"deal.ID_PROMOTOR",new Integer(Types.INTEGER)});
    	} else {
    		if(usuariosNoStaff != null) {
	    		if(!usuariosNoStaff.isEmpty()) {
		    		StringBuffer sqlNoStaffAll = new StringBuffer();
		    		sqlNoStaffAll.append(" AND deal.ID_PROMOTOR IN (");
		    		for(Iterator it = usuariosNoStaff.iterator(); it.hasNext();) {
		    			Integer current = (Integer)it.next();
		    			sqlNoStaffAll.append("" + current + ",");
		    		}
		    		String strSqlNoStaffAll = sqlNoStaffAll.toString();
		    		strSqlNoStaffAll = strSqlNoStaffAll.substring(0, strSqlNoStaffAll.length() - 1) + ") ";
		    		finalQuery.append(strSqlNoStaffAll);
    			}
    		}
    	}
    	if(!ORIG_OPER_ALL.equalsIgnoreCase(origen)){ 						
    		if(ORIG_OPER_SOLO_CNLS_SUC.equalsIgnoreCase(origen)){ 			// canales de sucursales (Teller)
    			finalQuery.append(QUERY_OCV_CONDICION_SOLO_TELLER);
    		} else if (ORIG_OPER_SOLO_CNLS_PRM.equalsIgnoreCase(origen)) { 	// canales de promoción (Sin Teller)
    			finalQuery.append(QUERY_OCV_CONDICION_SIN_TELLER);
    		} else {														// canal específico
    			finalQuery.append(QUERY_OCV_CONDICION_CANAL_ESPECIFICO);
    			parametros.add(origen);
        		variables.add(new Object[]{"canal.ID_CANAL", new Integer(Types.VARCHAR)});
    		}
    	}
    	
    	if(division != null) {						
    		if(!DIVISIONES_ALL.equals(division)) {
    			finalQuery.append(QUERY_OCV_CONDICION_DIVISION);
	    		parametros.add(division);
	    		variables.add(new Object[]{"division.ID_DIVISION", new Integer(Types.INTEGER)});
    		}
    	}
    	
    	if(plaza != null) {						
    		if(!PLAZAS_ALL.equals(plaza)) {
    			finalQuery.append(QUERY_OCV_CONDICION_PLAZA);
	    		parametros.add(plaza);
	    		variables.add(new Object[]{"plaza.ID_PLAZA", new Integer(Types.INTEGER)});
    		}
    	}
    	
    	if(zona != null) {						
    		if(!ZONAS_ALL.equals(zona)) {
    			finalQuery.append(QUERY_OCV_CONDICION_ZONA);
	    		parametros.add(zona);
	    		variables.add(new Object[]{"zona.ID_ZONA", new Integer(Types.INTEGER)});
    		}
    	}
    	
    	if(sucursal != null) {										// Filtro de sucursal
	    	if(!SUCURSALES_ALL.equals(sucursal)) {
	    		finalQuery.append(QUERY_OCV_CONDICION_SUCURSAL);
	    		parametros.add(sucursal);
	    		variables.add(new Object[]{"suc.ID_SUCURSAL", new Integer(Types.INTEGER)});
	    	}
    	}
    	
    	if(contratoSica != null) {				// Filtro de Contrato SICA
    		if(! contratoSica.trim().equals("") ) {
    			finalQuery.append(QUERY_OCV_CONDICION_CONTRATO_SICA);
	    		parametros.add(contratoSica);
	    		variables.add(new Object[]{"cta_cont.no_cuenta", new Integer(Types.VARCHAR)});
    		}
    	}
    	if(tipoCliente != null) {										// Filtro de TipoCliente
	    	if(!TIPO_CLIENTE_ALL.equals(tipoCliente)) {
	    		finalQuery.append(QUERY_OCV_CONDICION_TIPO_CLIENTE);
	    		parametros.add(tipoCliente);
	    		variables.add(new Object[]{"deal.ES_CLIENTE", new Integer(Types.CHAR)});
	    	}
    	}
    	
    	finalQuery.append(QUERY_OCV_ORDEN);
    	
    	resultado = new ParametrosOperacionesComprasVentasQuery(getDataSource(), 
    			finalQuery.toString(), variables).execute(parametros.toArray());
    	
    	return resultado;
    }
    
    public List findReporteOperacionesDiarias(Date desde,Date hasta) {
    	List resultado = null;
    	List parametros = new ArrayList();
    	StringBuffer finalQuery = new StringBuffer(QUERY_OPERACIONES_DIARIAS);
    	parametros.add(desde);
    	parametros.add(hasta);
    	List variables = new ArrayList();
    	
    	resultado = new ParametrosOperacionesDiariasQuery(getDataSource(), 
    			finalQuery.toString(), variables).execute(parametros.toArray());
    	
    	return resultado;
	}
    
    public List findReporteOperacionesDiariasIxedir(Date desde,Date hasta) {
    	List resultado = null;
    	List parametros = new ArrayList();
    	StringBuffer finalQuery = new StringBuffer(QUERY_OPERACIONES_DIARIAS_IXEDIR);
    	parametros.add(desde);
    	parametros.add(hasta);
    	List variables = new ArrayList();
    	
    	resultado = new ParametrosOperacionesDiariasIxedirQuery(getDataSource(), 
    			finalQuery.toString(), variables).execute(parametros.toArray());
    	
    	return resultado;
	}
    
    public List findAllClientes(Date fechaProceso) {
    	List resultado = null;
    	List parametros = new ArrayList();
    	StringBuffer finalQuery = new StringBuffer(QUERY_LAYOUT_CLIENTES);
    	parametros.add(fechaProceso);
    	List variables = new ArrayList();
    	
    	resultado = new ParametrosAllClientesQuery(getDataSource(), 
    			finalQuery.toString(), variables).execute(parametros.toArray());
    	
    	return resultado;
	}
    
    public List findAllContratos(Date fechaProceso) {
    	List resultado = null;
    	List parametros = new ArrayList();
    	StringBuffer finalQuery = new StringBuffer(QUERY_LAYOUT_CONTRATOS);
    	parametros.add(fechaProceso);
    	List variables = new ArrayList();
    	
    	resultado = new ParametrosAllContratosQuery(getDataSource(), 
    			finalQuery.toString(), variables).execute(parametros.toArray());
    	
    	return resultado;
	}
    
    public List findAllTransacciones(Date fechaProceso) {
    	List resultado = null;
    	List parametros = new ArrayList();
    	StringBuffer finalQuery = new StringBuffer(QUERY_LAYOUT_TRANSACCIONES);
    	parametros.add(fechaProceso);
    	List variables = new ArrayList();
    	
    	resultado = new ParametrosAllTransaccionesQuery(getDataSource(), 
    			finalQuery.toString(), variables).execute(parametros.toArray());
    	
    	return resultado;
	}

    class ParametrosOperacionesDiariasQuery extends MappingSqlQuery {

    	/**
    	 * Constructor de la clase.
    	 * 
    	 *  @param ds El dataSource.
    	 *  @param finalQuery El query armado de acuerdo a las opciones y condiciones seleccionadas
    	 */
    	public ParametrosOperacionesDiariasQuery(DataSource ds, String finalQuery, List variables) {
    		super(ds,finalQuery);

    		super.declareParameter(new SqlParameter("deal.FECHA_CAPTURA", Types.DATE));
    		super.declareParameter(new SqlParameter("deal.FECHA_CAPTURA", Types.DATE));
    		
    		for(Iterator it = variables.iterator();it.hasNext();){
    			Object[] pareja = (Object[])it.next();
    			super.declareParameter(new SqlParameter((String)pareja[0],((Integer)pareja[1]).intValue()));
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
    		return new Object[] { 
    				rs.getDate("Fecha"),
    				new Integer(rs.getInt("No Deal")),
    				rs.getString("Operacion"),
    				new Double(rs.getDouble("Importe")),
    				rs.getString("Divisa"),
    				new Double(rs.getDouble("Tipo de Cambio Cliente")),
    				new Double(rs.getDouble("Tipo Cambio Mesa")),
    				new Double(rs.getDouble("UTILIDAD")),
    				rs.getString("Contrato"),
    				rs.getString("Contr. Sica"),
    				rs.getString("Cuenta de Cheques"),
    				rs.getString("Nombre del Cliente"),
    				rs.getString("Promotor"),
    				rs.getString("Canal"),
    				rs.getString("Cliente Banorte"),
    				rs.getString("SIC"),
    				rs.getString("No de Sucursal"),			
    		};
        }
    }
    
    class ParametrosOperacionesDiariasIxedirQuery extends MappingSqlQuery {

    	/**
    	 * Constructor de la clase.
    	 * 
    	 *  @param ds El dataSource.
    	 *  @param finalQuery El query armado de acuerdo a las opciones y condiciones seleccionadas
    	 */
    	public ParametrosOperacionesDiariasIxedirQuery(DataSource ds, String finalQuery, List variables) {
    		super(ds,finalQuery);

    		super.declareParameter(new SqlParameter("deal.FECHA_CAPTURA", Types.DATE));
    		super.declareParameter(new SqlParameter("deal.FECHA_CAPTURA", Types.DATE));
    		
    		for(Iterator it = variables.iterator();it.hasNext();){
    			Object[] pareja = (Object[])it.next();
    			super.declareParameter(new SqlParameter((String)pareja[0],((Integer)pareja[1]).intValue()));
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
    		return new Object[] { 
    				new Integer(rs.getInt("Deal")),
    				rs.getString("mnemonico"),
    				rs.getString("Contr. Sica"),
    				rs.getDate("Fecha Captura"),
    				rs.getString("Promotor Sica"),
    				new Double(rs.getDouble("UTILIDAD")),
    				rs.getString("Usuario captura"),
    				new Integer(rs.getInt("rim_no")),
    				rs.getString("ID_PERSONA"),
    				rs.getString("Sucursal"),
    				rs.getString("No Sucursal"),
    				new Double(rs.getDouble("Monto")),
    				rs.getString("ID_DIVISA"),
    				rs.getString("TIPO_PERSONA"),
    				new Double(rs.getDouble("Tipo Cambio Cliente")),
    				rs.getString("Op."),
    				rs.getString("Producto"),
    				rs.getString("cuenta de cheques"),
    				rs.getString("Confirma"),
    				rs.getString("Zona"),
    				rs.getString("Year"),
    				rs.getString("Mes"),
    				rs.getString("SemanaDia"),
    				rs.getString("Semana"),
    				new Integer(rs.getInt("DiaNumero")),
    				rs.getString("Direccion"),
    				rs.getString("Nombre del Cliente"),    				    				
    				new Double(rs.getDouble("tipo_cambio_mesa"))    							
    		};
        }
    }
    
    class ParametrosAllClientesQuery extends MappingSqlQuery {

    	/**
    	 * Constructor de la clase.
    	 * 
    	 *  @param ds El dataSource.
    	 *  @param finalQuery El query armado de acuerdo a las opciones y condiciones seleccionadas
    	 */
    	public ParametrosAllClientesQuery(DataSource ds, String finalQuery, List variables) {
    		super(ds,finalQuery);

//    		super.declareParameter(new SqlParameter("deal.FECHA_CAPTURA", Types.DATE));
    		super.declareParameter(new SqlParameter("contrato.FECHA_APERTURA", Types.DATE));
    		
    		for(Iterator it = variables.iterator();it.hasNext();){
    			Object[] pareja = (Object[])it.next();
    			super.declareParameter(new SqlParameter((String)pareja[0],((Integer)pareja[1]).intValue()));
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
    		return new Object[] { 
    				rs.getString("NUMCLIEN"),
    				rs.getString("CODIDENT"),
    				rs.getString("CLAIDENT"),
    				rs.getString("PRIAPE"),
    				rs.getString("SEGAPE"),
    				rs.getString("NOMBRE"),
    				rs.getString("SEXO"),
    				rs.getString("ESTCIVIL"),
    				rs.getString("TITULO"),
    				rs.getString("TRATAMIE"),
    				rs.getString("PETDOMIC"),
    				rs.getString("IDEDIREC"),
    				rs.getString("NOMCALLE"),
    				rs.getString("NUMCALLE"),
    				rs.getString("TIPOVIVI"),
    				rs.getString("ENTRADA"),
    				rs.getString("PISO"),
    				rs.getString("DEPARTAM"),    	
    				rs.getString("COLONIA"),
    				rs.getString("DELMUNI"),
    				rs.getString("DESDELMUNI"),
    				rs.getString("ESTADO"),
    				rs.getString("DESESTADO"),
    				rs.getString("CODPOST"),
    				rs.getString("CODPAIS"),
    				rs.getString("CURP"),
    				rs.getString("RFC"),
    				rs.getString("IMSS"),
    				rs.getString("INFONAVI"),
    				rs.getString("SUJGRUP"),
    				rs.getString("SUJSUBG1"),
    				rs.getString("FENACIMI"),
    				rs.getString("CNAEG"),
    				rs.getString("CNO"),
    				rs.getString("SEGMENTO"),
    				rs.getString("NUMDOCLI"),
    				rs.getString("INDGRANC"),
    				rs.getString("CLATELEF1"),
    				rs.getString("NUMTELEF1"),
    				rs.getString("CLATELEF2"),
    				rs.getString("NUMTELEF2"),
    				rs.getString("CLATELEF3"),
    				rs.getString("NUMTELEF3"),
    				rs.getString("CRGESTOR"),    				    				
    				rs.getString("FUNCIONA"),
    				rs.getString("AREANEG"),
    				rs.getString("INDBAJA"),
    				rs.getString("FEALTCLI"),
    				rs.getString("PEHSTAMP"),
    				rs.getString("PECNACIO"),
    				rs.getString("LUGANACI"),
    				rs.getString("PECNARES"),
    				rs.getString("EMAIL"),
    				rs.getString("WEB2"),
    				rs.getString("CENTRABA"),
    				rs.getString("FECHINGR"),    				    				
    				rs.getString("REGIMENM"),
    				rs.getString("CODESTUD"), 
    				rs.getString("EMPLEADO"),  
    				rs.getString("ENTINACI"),  
    				rs.getString("FECNACHI"),  
    				rs.getString("NUMAUTOS"),  
    				rs.getString("NUMHIJOS"),  
    				rs.getString("NUMBIENE"),  
    				rs.getString("ORIGEN"),  
    				rs.getString("TIEMPRES"),  
    				rs.getString("CODAFICI"),  
    				rs.getString("OTROSBAN"),  
    				rs.getString("COBGEOG"),  
    				rs.getString("NUMSUC"),  
    				rs.getString("NUMEMPLE"),  
    				rs.getString("FUENTEIN"),  
    				rs.getString("INGMENS"),  
    				rs.getString("IMPVTAS"),  
    				rs.getString("ACTIVO"),  
    				rs.getString("PASIVO"),  
    				rs.getString("CAPITAL"),  
    				rs.getString("IMPEXP"),  
    				rs.getString("FEALTCLI2"),  
    				rs.getString("FECPROC"),  
    				rs.getString("RIESGO"),  
    				rs.getString("FIEL"),  
    				rs.getString("ART10"),  
    				rs.getString("VARIOS")
    		};
        }
    }
    
    class ParametrosAllContratosQuery extends MappingSqlQuery {

    	/**
    	 * Constructor de la clase.
    	 * 
    	 *  @param ds El dataSource.
    	 *  @param finalQuery El query armado de acuerdo a las opciones y condiciones seleccionadas
    	 */
    	public ParametrosAllContratosQuery(DataSource ds, String finalQuery, List variables) {
    		super(ds,finalQuery);

    		super.declareParameter(new SqlParameter("contrato.FECHA_APERTURA", Types.DATE));
    		
    		for(Iterator it = variables.iterator();it.hasNext();){
    			Object[] pareja = (Object[])it.next();
    			super.declareParameter(new SqlParameter((String)pareja[0],((Integer)pareja[1]).intValue()));
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
    		return new Object[] { 
    				rs.getString("NUMCLIEN"),
    				rs.getString("PECENTID"),
    				rs.getString("OFIAPE"),
    				rs.getString("CODISER"),
    				rs.getString("CTALTA"),
    				rs.getString("CLAINTER"),
    				rs.getString("SECINTER"),
    				rs.getString("NUMDOMIC12"),
    				rs.getString("FEALRELA"),
    				rs.getString("FECHAPE"),
    				rs.getString("PEHSTAMP"),
    				rs.getString("INDBAJA"),
    				rs.getString("PRODUCTO"),
    				rs.getString("SUBPRO"),
    				rs.getString("FECHCANCEL"),
    				rs.getString("FECHAUMO"),
    				rs.getString("SECTOR"),
    				rs.getString("TASAINT"),    	
    				rs.getString("SDOPROMDOCE"),
    				rs.getString("INDESTA"),
    				rs.getString("CENTRO-CONTAB"),
    				rs.getString("FECHA-ALTA"),
    				rs.getString("INDICADOR"),
    				rs.getString("DIVISA"),
    				rs.getString("BALANCE"),
    				rs.getString("EMPLEADO"),
    				rs.getString("CODEMPLEADO")
    		};
        }
    }
    
    class ParametrosAllTransaccionesQuery extends MappingSqlQuery {

    	/**
    	 * Constructor de la clase.
    	 * 
    	 *  @param ds El dataSource.
    	 *  @param finalQuery El query armado de acuerdo a las opciones y condiciones seleccionadas
    	 */
    	public ParametrosAllTransaccionesQuery(DataSource ds, String finalQuery, List variables) {
    		super(ds,finalQuery);

    		super.declareParameter(new SqlParameter("detalleLiquidacion.FECHA_LIQUIDADO", Types.DATE));
    		
    		for(Iterator it = variables.iterator();it.hasNext();){
    			Object[] pareja = (Object[])it.next();
    			super.declareParameter(new SqlParameter((String)pareja[0],((Integer)pareja[1]).intValue()));
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
    		return new Object[] { 
    				rs.getString("Account_Key"),
    				rs.getString("Acct_Open_Date"),
    				rs.getString("ROW_UPDATE_DATE"),
    				rs.getString("TRANS_TYPE_CD"),
    				rs.getString("Valor"),
    				rs.getString("TRANSACTION_TYPE_CD"),
    				rs.getString("TRANSACTION_CODE_CD"),
    				rs.getString("Trans_Type_Category"),
    				rs.getString("Trans_Type_Cd"),
    				rs.getString("Account_Key"),
    				rs.getString("Acct_Currency_Amount"),
    				rs.getString("ID_DIVISA"),
    				rs.getString("MONTO_MONEDA_NACIONAL"),
    				rs.getString("Payee_id_No"),
    				rs.getString("Transaction_Key"),
    				rs.getString("Opp_Account_Key"),
    				rs.getString("Opp_Country_Cd"),
    				rs.getString("Opp_Org_key"),    	
    				rs.getString("Cod1"),
    				rs.getString("Cod2")    		};
        }
    }


    /**
     * Clase interna para accesar a las propiedades de los registros obtenidos
     * para el reporte de Polizas Contables.
     * 
     * @author Pedro M. Espinosa (espinosapm)
     *
     */
    class ParametrosOperacionesComprasVentasQuery extends MappingSqlQuery {

    	/**
    	 * Constructor de la clase.
    	 * 
    	 *  @param ds El dataSource.
    	 *  @param finalQuery El query armado de acuerdo a las opciones y condiciones seleccionadas
    	 */
    	public ParametrosOperacionesComprasVentasQuery(DataSource ds, String finalQuery, List variables) {
    		super(ds,finalQuery);

    		super.declareParameter(new SqlParameter("deal.FECHA_CAPTURA", Types.DATE));
    		super.declareParameter(new SqlParameter("deal.FECHA_CAPTURA", Types.DATE));
    		super.declareParameter(new SqlParameter("deal_pos.MONTO", Types.INTEGER));
    		super.declareParameter(new SqlParameter("deal_pos.MONTO", Types.INTEGER));
    		
    		for(Iterator it = variables.iterator();it.hasNext();){
    			Object[] pareja = (Object[])it.next();
    			super.declareParameter(new SqlParameter((String)pareja[0],((Integer)pareja[1]).intValue()));
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
    		return new Object[] { 
    				new Integer(rs.getInt("Deal")),
    				rs.getString("Canal"),
    				new Integer(rs.getInt("FOLIO")),
    				rs.getString("Fecha Valor"),
    				rs.getDate("Fecha Captura"),
    				rs.getDate("Fecha Liq."),
    				rs.getString("MNEMONICO"),
    				rs.getString("Op."),
    				rs.getString("Cliente"),
    				rs.getString("Producto"),
    				new Double(rs.getDouble("Monto")),
    				rs.getString("ID_DIVISA"),
    				new Double(rs.getDouble("Tipo Cambio Cliente")),
    				new Double(rs.getDouble("Tipo Cambio Mesa")),
    				new Double(rs.getDouble("UTILIDAD")),
    				new Double(rs.getDouble("Monto Equiv.")),
    				rs.getString("Promotor"),
    				rs.getString("CASE"),
    				rs.getString("Contr. Sica"),
    				rs.getString("Status"),
    				rs.getString("Mesa"),
    				rs.getString("Sector Econ."),
    				rs.getString("Sector Banxico"),
    				rs.getString("Desde Teller"),
    				rs.getString("Nombre Sucursal"),
    				
    				rs.getString("Nombre Zona"),
    				rs.getString("Nombre Plaza"),
    				rs.getString("Nombre Division"),
    				rs.getString("Es CLiente")
    		};
        }
    }
    
    /**
     * Realiza la b&uacute;squeda para generar el reporte de autorizaciones de mesa de cambios.
     *
     * @param idCanal El id del canal seleccionado.
     * @param status El status del detalle de deal.
     * @param tipoAutorizacion El filtro para autorizaciones por plantilla o por falta de documentos.
     * @param desde La fecha de inicio de la busqueda.
     * @param hasta La fecha de fin de la busqueda.
     * @return List.
     */
    public List findReporteAutorizacionesMesaControl(String idCanal, String status, String tipoAutorizacion,
    							Date desde, Date hasta){
    	
    	List resultado = null;
    	List parametros = new ArrayList();
    	StringBuffer finalQuery = new StringBuffer(QUERY_AUTORIZACIONES_MESA_CONTROL);
    	finalQuery.append(tipoAutorizacion).append(") ");
    	parametros.add(desde);
    	parametros.add(hasta);
    	
    	List variables = new ArrayList();
    	
    	if(!"0".equalsIgnoreCase(idCanal)){
    		finalQuery.append(QUERY_AMC_CONDICION_CANAL);
    		parametros.add(idCanal);
    		variables.add(new Object[]{"canal.ID_CANAL",new Integer(Types.VARCHAR)});
    	}
    	if(!"TODAS".equalsIgnoreCase(status)){
    		finalQuery.append(QUERY_AMC_CONDICION_STATUS);
    		parametros.add(status);
    		variables.add(new Object[]{"act.resultado", new Integer(Types.VARCHAR)});
    	}
    	finalQuery.append(QUERY_AMC_ORDEN);
    	resultado = new ParametrosAutorizacionesMesaCambiosQuery(getDataSource(), 
    			finalQuery.toString(), variables).execute(parametros.toArray());
    	
    	return resultado;
    }

    /**
     * Clase interna para accesar a las propiedades de los registros obtenidos
     * para el reporte de Polizas Contables.
     * 
     * @author Pedro M. Espinosa (espinosapm)
     *
     */
    class ParametrosAutorizacionesMesaCambiosQuery extends MappingSqlQuery {

    	/**
    	 * Constructor de la clase.
    	 * 
    	 *  @param ds El dataSource.
    	 *  @param finalQuery El query armado de acuerdo a las opciones y condiciones seleccionadas
    	 */
    	public ParametrosAutorizacionesMesaCambiosQuery(DataSource ds, String finalQuery, List variables) {
    		super(ds,finalQuery);

    		super.declareParameter(new SqlParameter("deal.FECHA_CAPTURA", Types.DATE));
    		super.declareParameter(new SqlParameter("deal.FECHA_CAPTURA", Types.DATE));
    		
    		for(Iterator it = variables.iterator();it.hasNext();){
    			Object[] pareja = (Object[])it.next();
    			super.declareParameter(new SqlParameter((String)pareja[0],((Integer)pareja[1]).intValue()));
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
    		return new Object[] { 
    				rs.getDate("Fecha Captura"),
    				rs.getString("Cliente"),
    				rs.getString("Contr. Sica"),
    				rs.getString("Promotor"),
    				rs.getString("Canal"),
    				rs.getString("Tipo Persona"),
    				new Integer(rs.getInt("Deal")),
    				rs.getString("Op."),
    				rs.getString("ID_DIVISA"),
    				new Double(rs.getDouble("Monto")),
    				rs.getDate("Fecha Liq."),
    				rs.getString("Producto"),
    				rs.getString("Status"),
    				rs.getString("Origen Deal"),
    				rs.getString("Resultado"),
    				rs.getString("Autorizado Por"),
    				rs.getDate("fecha_terminacion")
    		};
        }
    }
    
    /**
     * Realiza la b&uacute;squeda para generar el reporte de operaciones de compras/ventas.
     *
     * @param operacion El tipo de operacion (compra o venta).
     * @param promotor El id del promotor.
     * @param desde La fecha de inicio de la busqueda.
     * @param hasta La fecha de fin de la busqueda.
     * @param idCanal El identificador del canal sobre el cual se van a calcular las utilidades
     * @return List.
     */
	public List findReporteUtilidadPromotres(Integer promotor, Date desde,
			Date hasta, String operacion, String idCanal) {
		
		List resultado = null;
    	List parametros = new ArrayList();
    	List parametrosUnion = new ArrayList();
    	
    	StringBuffer finalQuery = new StringBuffer(QUERY_UTILIDADES_PROMOTOR);
    	StringBuffer finalQueryUnion = new StringBuffer(QUERY_UTILIDADES_PROMOTOR_2);
    	
    	parametros.add(desde);
    	parametros.add(hasta);
    	parametrosUnion.add(desde);
    	parametrosUnion.add(hasta);
    	
    	List variables = new ArrayList();
    	List variablesUnion = new ArrayList();
    	
    	if(promotor.intValue() != 0){
    		finalQuery.append(QUERY_UP_CONDICION_PROMOTOR);
    		parametros.add(promotor);
    		variables.add(new Object[]{"p.id_persona",new Integer(Types.INTEGER)});
    		
    		finalQueryUnion.append(QUERY_UP_CONDICION_PROMOTOR);
    		parametrosUnion.add(promotor);
    		variablesUnion.add(new Object[]{"p.id_persona",new Integer(Types.INTEGER)});
    	}
    	if(!"Todas".equalsIgnoreCase(operacion)){
    		String operacionQry = "N";
    		if("Compra".equals(operacion))
    			operacionQry = "S";
    		
    		finalQuery.append(QUERY_UP_CONDICION_OPERACION);
    		parametros.add(operacionQry);
    		variables.add(new Object[]{"dp.recibimos",new Integer(Types.VARCHAR)});
    		
    		finalQueryUnion.append(QUERY_UP_CONDICION_OPERACION);
    		parametrosUnion.add(operacionQry);
    		variablesUnion.add(new Object[]{"dp.recibimos",new Integer(Types.VARCHAR)});
    	}
    	if(!"0".equalsIgnoreCase(idCanal)){
    		finalQuery.append(QUERY_UP_CONDICION_CANAL);
    		parametros.add(idCanal);
    		variables.add(new Object[]{"d.ID_CANAL",new Integer(Types.VARCHAR)});
    		
    		finalQueryUnion.append(QUERY_UP_CONDICION_CANAL);
    		parametrosUnion.add(idCanal);
    		variablesUnion.add(new Object[]{"d.ID_CANAL",new Integer(Types.VARCHAR)});
    	}
    	finalQuery.append(QUERY_UP_GROUP_BY);
    	finalQueryUnion.append(QUERY_UP_GROUP_BY);
    	
    	finalQuery.append(" UNION ").append(finalQueryUnion.toString()).append(QUERY_UP_ORDER_BY);
    	parametros.addAll(parametrosUnion);
    	
    	variables.add(new Object[]{"d.FECHA_CAPTURA",new Integer(Types.DATE)});
    	variables.add(new Object[]{"d.FECHA_CAPTURA",new Integer(Types.DATE)});
    	variables.addAll(variablesUnion);
    	
    	resultado = new ParametrosUtilidadesPromotorQuery(getDataSource(), 
    			finalQuery.toString(), variables).execute(parametros.toArray());
    	
    	return resultado;
	}
	
    /**
     * Clase interna para accesar a las propiedades de los registros obtenidos
     * para el reporte de Polizas Contables.
     * 
     * @author Pedro M. Espinosa (espinosapm)
     *
     */
    class ParametrosUtilidadesPromotorQuery extends MappingSqlQuery {

    	/**
    	 * Constructor de la clase.
    	 * 
    	 *  @param ds El dataSource.
    	 *  @param finalQuery El query armado de acuerdo a las opciones y condiciones seleccionadas
    	 */
    	public ParametrosUtilidadesPromotorQuery(DataSource ds, String finalQuery, List variables) {
    		super(ds,finalQuery);

    		super.declareParameter(new SqlParameter("d.FECHA_CAPTURA", Types.DATE));
    		super.declareParameter(new SqlParameter("d.FECHA_CAPTURA", Types.DATE));
    		
    		for(Iterator it = variables.iterator();it.hasNext();){
    			Object[] pareja = (Object[])it.next();
    			super.declareParameter(new SqlParameter((String)pareja[0],((Integer)pareja[1]).intValue()));
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
    		return new Object[] { 
    				new Integer(rs.getInt("ID_PERSONA")),
    				rs.getString("PROMOTOR"),
    				rs.getString("OPERACION"),
    				new Double(rs.getDouble("UTILIDAD")),
    				rs.getString("Canal")
    		};
        }
    }
    
    /**
     * Realiza la b&uacute;squeda para generar el reporte de documentacib&oacute;n pendiente
     * 
     * @author PEspinosa
     * @param nombreCorto - El nombre corto del cliente
     * @param noContrato - El nb&uacute;mero de contrato SICA
     * 
     * @return List
     */
	public List findReporteDocumentacionFaltante(String nombreCorto,
			String noContrato) {
		List resultado = new ArrayList();
		String resultadoString = null;
		
		try {
			CallableStatement spDocFaltante = getDataSource().getConnection().prepareCall(COMMAND_DOCUMENTACION_FALTANTE);
			spDocFaltante.registerOutParameter(1, Types.VARCHAR);
			spDocFaltante.setString(2, noContrato);
			if(noContrato == null || noContrato.trim().equals("")){
				spDocFaltante.setString(2, "%");
			}
			spDocFaltante.execute();
			
			resultadoString = spDocFaltante.getString(1);
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		
		if(resultadoString != null){
			StringTokenizer registros = new StringTokenizer(resultadoString,"^");
			StringTokenizer registro;
			
			StringTokenizer faltantesST;
			char doc;
			StringTokenizer infoDummyST;
			while(registros.hasMoreElements()){
				registro = new StringTokenizer(registros.nextToken(),"|");
				Object[] tupla = new Object[12];
				tupla[0] = registro.nextElement(); //Contrato
				tupla[1] = registro.nextElement(); //Fecha Apertura
				tupla[2] = registro.nextElement(); //Cliente
				String tipoPersona = registro.nextToken();
				if(tipoPersona.trim().equalsIgnoreCase("PM")){
					tupla[3] = "Moral";				//Tipo de Persona
				}else if(tipoPersona.trim().equalsIgnoreCase("AE")){
					tupla[3] = "Actividad Empresarial";
				}else{
					tupla[3] = "F\u00EDsica";
				}
				tupla[4] = registro.nextElement();	//Estado
				faltantesST = new StringTokenizer(registro.nextToken(),",");
				StringBuffer faltantes = new StringBuffer();
				if(faltantesST.hasMoreElements()){
					doc = ((String)faltantesST.nextElement()).charAt(0);
					faltantes.append(documentoFaltante(doc));
					while(faltantesST.hasMoreElements()){
						faltantes.append(", ");
						doc = ((String)faltantesST.nextElement()).charAt(0);
						faltantes.append(documentoFaltante(doc));
					}
				}
				tupla[5] = faltantes.toString(); //Faltantes
				
				infoDummyST = new StringTokenizer((String)registro.nextElement(),",");
				StringBuffer infoDummy = new StringBuffer();
				if(infoDummyST.hasMoreElements()){
					doc = ((String)infoDummyST.nextElement()).charAt(0);
					infoDummy.append(documentoFaltante(doc));
					while(infoDummyST.hasMoreElements()){
						infoDummy.append(", ");
						doc = ((String)infoDummyST.nextElement()).charAt(0);
						infoDummy.append(documentoFaltante(doc));
					}
				}
				tupla[6] = infoDummy.toString(); //Info Dummy
				tupla[7] = registro.nextToken(); //Relacion con Banco
				tupla[8] = registro.nextToken(); //Promotor
				tupla[9] = registro.nextToken(); //Canal
				tupla[10] = registro.nextToken(); //Holding
				tupla[11] = registro.nextToken(); //Credito
				
				resultado.add(tupla);
			}
		}
    	
    	return resultado;
	}
	
	/**
     * Realiza la b&uacute;squeda del documento faltante
     * 
     * @author PEspinosa
     * @param doc - La clave del documento faltante
     * 
     * @return String el nombre del documento faltante
     */
	private String documentoFaltante(char doc){
		switch (doc) {
		case 'A':
			return "RFC";
		case 'B':
			return "Comprobante de identificaci\u00F3n";

		case 'C':
			return "N\u00FAmero de identificaci\u00F3n";

		case 'D':
			return "Fecha de expedici\u00F3n de identificaci\u00F3n";

		case 'E':
			return "Registro p\u00FAblico de Identificaci\u00F3n";

		case 'F':
			return "N\u00FAmero de acta constitutiva";

		case 'G':
			return "Nombre Notario";

		case 'H':
			return "N\u00FAmero de notar\u00EDa";

		case 'I':
			return "Plaza Notar\u00EDa";

		case 'J':
			return "Comprobante de domicilio";

		case 'K':
			return "Sin direcci\u00F3n fiscal";

		case 'L':
			return "Varias direcciones fiscales";

		default:
			return "";
		}
	}
    /**
     * Constante para la consulta de polizas contables
     */
    private static String QUERY_POLIZAS_CONTABLES = "SELECT d.NOMBRE_CORTO, a.* " 
    	+"FROM SC_POLIZA a, BUP_CUENTA_CONTRATO b, BUP_PERSONA_RIM c, BUP_PERSONA d "
    	+"WHERE a.NO_CONTRATO_SICA  = b.NO_CUENTA (+) "
    	+"AND b.RIM_NO = c.RIM_NO (+) "
    	+"AND c.ID_PERSONA = d.ID_PERSONA (+) "
    	+"AND FECHA_CREACION BETWEEN ? AND ?";
    
    private static String QUERY_CR_2 =
    	"select d.id_deal, " +
        	   "d.no_cuenta, " + 
        	   "d.cr, " + 
        	   "p.id_divisa, " + 
        	   "det.mnemonico, " +
        	   "(promotor.nombre || ' ' || promotor.paterno || ' ' || promotor.materno) nombrePromotor, " +
        	   "cte_persona.NOMBRE_CORTO cliente, " + 
        	   "d.id_canal, " + 
        	   "cliente.sic, " + 
        	   "cliente.cuenta_cheques cuentaAltamira, " + 
        	   "cliente.sucursal_origen clienteCr, " +
        	   "p.monto, " + 
        	   "det.tipo_cambio_mesa, " + 
        	   "p.tipo_cambio, " + 
        	   "p.recibimos, " +
        	   "det.folio_detalle " +
        "from sc_deal d, " +
             "sc_deal_detalle det, " +
             "sc_deal_posicion p, " +
             "BUP_PERSONA_FISICA promotor, " +
             "BUP_PERSONA cte_persona,  " +
             "BUP_CUENTA_CONTRATO cta_cont, " +
             "SC_CONTRATO contrato, " +
             "sc_cliente cliente " +
        "where (d.fecha_liquidacion >= ? and d.fecha_liquidacion <= ?) and " +
              "(d.status_deal = 'TL' and (d.reversado is null or d.reversado = 0)) and " +
              "(d.cr is not null) and " +
              "(d.id_promotor = promotor.id_persona(+)) and " +
              "(d.no_cuenta = cta_cont.no_cuenta and cta_cont.no_cuenta = contrato.no_cuenta and " +
              " contrato.id_cliente = cliente.id_cliente and cliente.id_persona = cte_persona.id_persona) and " +
              "(d.id_deal = det.id_deal and (det.status_detalle_deal = 'TT' and (det.reversado is null or det.reversado = 0)) and " +
              "det.id_deal_posicion = p.id_deal_posicion and p.id_divisa != 'MXN' and det.mnemonico not in ('RMXNCARGIXEB','RUSDCARGIXEB')) " +
        " union " +      
        "select d.id_deal, " +
               "d.no_cuenta, " +
               "d.cr, " + 
               "p.id_divisa, " +
               "det.mnemonico, " +
               "(promotor.nombre || ' ' || promotor.paterno || ' ' || promotor.materno) nombrePromotor, " +
               "cte_persona.NOMBRE_CORTO cliente, " +
               "d.id_canal, " + 
               "to_char(ca.sic) sic, " + 
               "pci.no_cuenta_ixe cuentaAltamira, " + 
               "ca.cr clienteCr, " +
               "p.monto, " + 
               "det.tipo_cambio_mesa, " + 
               "p.tipo_cambio, " + 
               "p.recibimos, " +
               "det.folio_detalle " +
        "from sc_deal d, " +
             "sc_deal_detalle det, " +
             "sc_plantilla_cuenta_ixe pci, " +
             "sc_cuenta_altamira ca, " +
             "BUP_PERSONA_FISICA promotor, " +
             "BUP_PERSONA cte_persona, " +
             "sc_deal_posicion p, " +
             "bup_persona_cuenta_rol bpcr " +
        "where (d.fecha_liquidacion >= ? and d.fecha_liquidacion <= ?) and " +
              "(d.status_deal = 'TL' and (d.reversado is null or d.reversado = 0)) and " + 
              "(d.cr is not null) and " + 
              "(d.id_promotor = promotor.id_persona(+)) and " +
              "(d.no_cuenta = bpcr.no_cuenta and bpcr.id_persona = cte_persona.id_persona and bpcr.id_rol = 'TIT') and " +
              "(d.id_deal = det.id_deal and (det.status_detalle_deal = 'TT' and (det.reversado is null or det.reversado = 0)) and " + 
              " det.id_deal_posicion = p.id_deal_posicion and " +
              "( " +
              "	(det.mnemonico in ('RMXNCARGIXEB','RUSDCARGIXEB') and det.id_plantilla = pci.id_plantilla and " +
              "  pci.no_cuenta_ixe = ca.cuenta_altamira) " +
              " )) " +
              "order by cr, id_deal, folio_detalle";
    
    	// -------------------------------------------------------------------------------------------
    private static String QUERY_CR_OPERACIONES_CLIENTES_BANORTE_EN_SICA =
    	"select deal.no_cuenta, " +
        "       cte_persona.NOMBRE_CORTO cliente, " +
        "       (promotor.nombre || ' ' || promotor.paterno || ' ' || promotor.materno) nombrePromotor, " +
        "       cliente.sic, " +
        "       deal.cr SucursalOrigen, " +
        "       cliente.cuenta_cheques, " + 
        "       deal.id_canal, " +
        "       deal.id_deal IdDeal, " +
        //"       det.folio_detalle, " +
        //"       posicion.id_deal_posicion, " +
        "       posicion.id_divisa, " +
        "       posicion.monto, " +
        "       det.tipo_cambio_mesa, " +
        "       posicion.tipo_cambio, " +
        "       posicion.recibimos " +
        "from SC_DEAL deal, " +
        "     BUP_PERSONA_FISICA promotor, " +
        "     BUP_CUENTA_CONTRATO cta_cont, " +
        "     SC_CONTRATO contrato, " +
        "     sc_cliente cliente, " +
        "     BUP_PERSONA cte_persona, " +
        "     SC_deal_detalle det, " +
        "     sc_deal_posicion posicion " +
        "where (deal.fecha_liquidacion >= ? and deal.fecha_liquidacion <= ?) and " +
        "      (deal.status_deal = 'TL' and deal.reversado != 2) and " +
        "      (deal.cr is not null) and " +
        "      (deal.id_promotor = promotor.id_persona(+)) and " +
        "      (deal.no_cuenta = cta_cont.no_cuenta and cta_cont.no_cuenta = contrato.no_cuenta and " +
        "       contrato.id_cliente = cliente.id_cliente and cliente.id_persona = cte_persona.id_persona) and " +
        "      (deal.id_deal = det.id_deal and det.status_detalle_deal = 'TT' and det.reversado != 2 and " +
        "       det.id_deal_posicion = posicion.id_deal_posicion and posicion.id_divisa != 'MXN') " +
        "order by deal.cr, cliente.sic";
    
    private static String QUERY_CR_GENERICO = "select valor from sc_parametro where id_parametro = 'CR_GENERICO'";
    
    private static String QUERY_DEALS_CARGO_CUENTA =  "select det.id_deal IdDeal, ca.cuenta_altamira cuentaAltamira, ca.sic, ca.cr SucursalOrigen " + 
													  "from sc_deal_detalle det " +
												      "left join SC_PLANTILLA_CUENTA_IXE pci on det.id_plantilla = pci.id_plantilla " +
												      "left join sc_cuenta_altamira ca on pci.no_cuenta_ixe = ca.cuenta_altamira " +
												      "where det.id_deal in (idDeals) and det.mnemonico in ('RUSDCARGIXEB','RMXNCARGIXEB') " +
												      "order by det.id_deal, det.folio_detalle";
    
    /**
     * Constante para la consulta de operaciones compra venta
     */
    private static String QUERY_OPERACIONES_DIARIAS =
    	"SELECT deal.FECHA_CAPTURA Fecha,"
        +"deal.ID_DEAL  \"No Deal\","
        +"DECODE(deal_pos.recibimos, 'S', 'Compra', 'Venta') Operacion," 
      +"deal_pos.MONTO Importe,"
    	+"deal_pos.ID_DIVISA \"Divisa\"," 
    	+"deal_pos.tipo_cambio \"Tipo de Cambio Cliente\"," 
    	+"deal_det.tipo_cambio_mesa \"Tipo Cambio Mesa\","
    	+"DECODE(deal_pos.recibimos, 'S', "
		+"(deal_det.tipo_cambio_mesa - deal_pos.tipo_cambio) * deal_pos.monto, "
		+"(deal_pos.tipo_cambio - deal_det.tipo_cambio_mesa) * deal_pos.monto "
		+") UTILIDAD,"  
		+"contrato.id_contrato Contrato,"
		+"cta_cont.no_cuenta \"Contr. Sica\"," 
		+"cliente.cuenta_cheques \"Cuenta de Cheques\","
		+"cte.NOMBRE_CORTO \"Nombre del Cliente\","
		+"prom.NOMBRE || ' ' || prom.PATERNO || ' ' || prom.MATERNO Promotor,  "
		+"deal.id_canal Canal, "
		+"DECODE(cliente.id_empresa, 'BNTE', 'Verdadero', 'Falso') \"Cliente Banorte\","
		+"cliente.sic SIC,"
		+"cliente.sucursal_operacion \"No de Sucursal\" "
		
		+"FROM "
		+"SC_DEAL deal," 
		+"SC_DEAL_DETALLE deal_det, "
		+"SC_DEAL_POSICION deal_pos," 
		+"BUP_CUENTA_CONTRATO cta_cont, "
		+"BUP_PERSONA_RIM pers_rim, "
		+"BUP_PERSONA cte," 
		+"BUP_SECTOR_ECONOMICO sec_eco, "
		+"BUP_PERSONA_FISICA prom, "
		+"SC_MESA_CAMBIO mesa, "
		+"SC_CANAL canal, "
		+"BUP_SUCURSAL suc , "
		+"SC_PRECIO_REFERENCIA precio , "
		+"SC_SUCURSAL_ZONA suc_zona , "
		+"SC_ZONA zona , SC_PLAZA plaza , "
		+"SC_DIVISION division,"
		+"SC_CONTRATO contrato,"
		+"sc_cliente cliente "
    
		+"WHERE "
		+"deal.ID_DEAL = deal_det.ID_DEAL "
		+"AND deal_det.ID_DEAL_POSICION = deal_pos.ID_DEAL_POSICION "
		+" AND deal.NO_CUENTA = cta_cont.NO_CUENTA (+) "
		+" AND cta_cont.RIM_NO = pers_rim.RIM_NO "
		+" AND pers_rim.ID_PERSONA = cte.ID_PERSONA "
		+" AND cte.ID_SECTOR = sec_eco.ID_SECTOR (+) "
		+ "AND deal.FECHA_CAPTURA BETWEEN ? AND ? "

		+" AND deal_det.STATUS_DETALLE_DEAL != 'CA' "
		+" AND deal.ID_PROMOTOR = prom.ID_PERSONA (+) "
		+" AND mesa.ID_MESA_CAMBIO = deal_pos.id_mesa_cambio "
		+" AND canal.ID_CANAL = deal.ID_CANAL "
		+" AND suc.ID_SUCURSAL(+) = canal.ID_SUCURSAL "
		+" AND deal_det.ID_PRECIO_REFERENCIA = precio.ID_PRECIO_REFERENCIA (+) "
		+" AND suc.ID_SUCURSAL = suc_zona.ID_SUCURSAL(+) "
		+" AND suc_zona.ID_ZONA = zona.ID_ZONA(+) "
		+" AND zona.ID_PLAZA = plaza.ID_PLAZA(+) "
		+" AND plaza.ID_DIVISION = division.ID_DIVISION(+) "
		+" and contrato.no_cuenta(+)=cta_cont.no_cuenta"
		+" and cte.id_persona=cliente.id_persona"
    ;
    
    
    /**
     * Constante para la consulta de operaciones compra venta
     */
    private static String QUERY_OPERACIONES_DIARIAS_IXEDIR =
        " SELECT deal.ID_DEAL Deal, "
        +" deal_det.mnemonico, "
        +" cta_cont.no_cuenta \"Contr. Sica\", " 
        +" to_date(deal.FECHA_CAPTURA,'dd/mm/yyyy') \"Fecha Captura\", "
        +" (select nombre_corto from bup_persona p where p.id_persona = deal.id_promotor) \"Promotor Sica\", "
        +" DECODE(deal_pos.recibimos, 'S', (deal_det.tipo_cambio_mesa-deal_pos.tipo_cambio)*deal_pos.monto, (deal_pos.tipo_cambio-deal_det.tipo_cambio_mesa)*deal_pos.monto) \"UTILIDAD\","
        +" userp.nombre_corto \"Usuario captura\", "
        +" cta_cont.rim_no, "
        +" CTE.ID_PERSONA, "
        +" '' \"Sucursal\", "
        +" '' \"No Sucursal\", "
        +" deal_pos.MONTO \"Monto\", "
        +" deal_pos.ID_DIVISA, "
        +" CTE.TIPO_PERSONA, "
        +" deal_pos.tipo_cambio \"Tipo Cambio Cliente\", "
        +" DECODE(deal_pos.recibimos, 'S', 'Compra', 'Venta') \"Op.\", "
        +" deal_det.clave_forma_liquidacion \"Producto\", "
        +" plci.no_cuenta_ixe \"cuenta de cheques\", "
        +" '' \"Confirma\", "
        +" '' \"Zona\", "
        +" extract(year from deal.FECHA_CAPTURA) \"Year\", "
        +" decode(extract(month from deal.FECHA_CAPTURA),1,'Enero',2,'Febrero',3,'Marzo',4,'Abril',5,'Mayo',6,'Junio',7,'Julio',8,'Agosto',9,'Septiembre', "
        +" 10,'Octubre',11,'Noviembre',12,'Diciembre') \"Mes\", "
        +" '' \"SemanaDia\", "
        +" '' \"Semana\", "
        +" extract(day from deal.FECHA_CAPTURA) \"DiaNumero\", "
        +" '' \"Direccion\", "
        +" cte.nombre_corto \"Nombre del Cliente\"," 
        +" tipo_cambio_mesa "

        +" FROM SC_DEAL deal, "
        +" SC_DEAL_DETALLE deal_det, "
        +" SC_DEAL_POSICION deal_pos, "
        +" BUP_CUENTA_CONTRATO cta_cont, "
        +" BUP_PERSONA_RIM pers_rim, "
        +" BUP_PERSONA cte, "
        +" BUP_SECTOR_ECONOMICO sec_eco, "
        +" BUP_PERSONA_FISICA prom, "
        +" SC_MESA_CAMBIO mesa, "
        +" sc_divisa div, "
        +" SEGU_USUARIO users, "
        +" BUP_PERSONA userp, "
        +" sc_plantilla pl, "
        +" sc_plantilla_cuenta_ixe plci "

        +" WHERE "
        +" deal.ID_DEAL = deal_det.ID_DEAL "
        +" AND deal_det.ID_DEAL_POSICION = deal_pos.ID_DEAL_POSICION "
        +" AND deal.NO_CUENTA = cta_cont.NO_CUENTA (+) "
        +" AND cta_cont.RIM_NO = pers_rim.RIM_NO "
        +" AND pers_rim.ID_PERSONA = cte.ID_PERSONA "
        +" AND cte.ID_SECTOR = sec_eco.ID_SECTOR (+) "
        +" AND users.id_usuario = deal.id_usuario "
        +" AND users.id_persona = userp.id_persona "
        +" AND deal_det.id_plantilla = pl.id_plantilla (+) "
        +" AND pl.id_plantilla = plci.id_plantilla(+) "
        + "AND deal.FECHA_CAPTURA BETWEEN ? AND ? "

        +" AND deal_det.STATUS_DETALLE_DEAL != 'CA' "
        +" AND deal.id_canal = 'DIR' "
        +" AND deal_pos.id_divisa = div.id_divisa "
        +" AND deal.ID_PROMOTOR = prom.ID_PERSONA (+) "
        +" AND mesa.ID_MESA_CAMBIO = deal_pos.id_mesa_cambio "
        +" order by deal.ID_DEAL,deal_pos.RECIBIMOS "
        ;

   /**
    * Constante para la consulta de clientes dados de alta
    */
   private static String QUERY_LAYOUT_CLIENTES =
		   "select RPAD('S'||a.id_persona,9,' ') as NUMCLIEN, " +
				   "NVL(RPAD(tipo_persona,2,' '),RPAD(' ',2,' ')) as CODIDENT, " +
				   "RPAD(' ',18,' ') as CLAIDENT, " +
				   "NVL(RPAD(paterno,80,' '),RPAD(' ',80,' ')) as PRIAPE, " +
				   "NVL(RPAD(materno,80,' '),RPAD(' ',80,' ')) as SEGAPE, " +
				   "NVL(RPAD(nombre,80,' '),NVL(RPAD(RAZON_SOCIAL,80,' '),RPAD(' ',80,' '))) as NOMBRE, " +
				   "NVL(RPAD(sexo,1,' '),RPAD(' ',1,' ')) as SEXO, " +
				   "NVL(RPAD(id_estado_civil,2,' '),RPAD(' ',2,' ')) as ESTCIVIL, " +
				   "NVL(RPAD(titulo,8,' '),RPAD(' ',8,' ')) as TITULO, " +
				   "RPAD(' ',7,' ') as TRATAMIE, " +
				   "RPAD(' ',1,' ') as PETDOMIC, " +
				   "RPAD(' ',2,' ') as IDEDIREC, " +
				   "case when (select count(*) from bup_direccion dir where dir.id_persona = a.id_persona and es_fiscal = 'S' ) = 0 " +
				   "     then  " +
				   "         case when (select count(*) from bup_direccion dir where dir.id_persona = a.id_persona and id_tipo_direccion = 1) > 0 " +
				   "         then (select NVL(RPAD(calle_y_numero,100,' '),RPAD(' ',100,' ')) from bup_direccion dir where dir.id_persona = a.id_persona and id_tipo_direccion = 1 and rownum = 1) " +
				   "         else  " +
				   "			  CASE WHEN (select count(*) from bup_direccion dir where dir.id_persona = a.id_persona and rownum = 1) > 0 THEN " +
				   "              	(select NVL(RPAD(calle_y_numero,100,' '),RPAD(' ',100,' ')) from bup_direccion dir where dir.id_persona = a.id_persona and rownum = 1) " +
				   "			  ELSE " +
				   "				  RPAD(' ',100,' ') " +
				   "			  END " +		
				   "         end " +
				   "     else (select NVL(RPAD(calle_y_numero,100,' '),RPAD(' ',100,' ')) from bup_direccion dir where dir.id_persona = a.id_persona and es_fiscal = 'S' and rownum = 1) " +
				   "end as NOMCALLE, " +
				   "case when (select count(*) from bup_direccion dir where dir.id_persona = a.id_persona and es_fiscal = 'S' ) = 0 " +
				   "     then  " +
				   "         case when (select count(*) from bup_direccion dir where dir.id_persona = a.id_persona and id_tipo_direccion = 1) > 0 " +
				   "         then (select NVL(RPAD(numero_ext,100,' '),RPAD(' ',100,' ')) from bup_direccion dir where dir.id_persona = a.id_persona and id_tipo_direccion = 1 and rownum = 1) " +
				   "         else  " +
				   "			  CASE WHEN (select count(*) from bup_direccion dir where dir.id_persona = a.id_persona and rownum = 1) > 0 THEN " +
				   "              	(select NVL(RPAD(numero_ext,100,' '),RPAD(' ',100,' ')) from bup_direccion dir where dir.id_persona = a.id_persona and rownum = 1) " +
				   "			  ELSE " +
				   "				  RPAD(' ',100,' ') " +
				   "			  END " +
				   "         end " +
				   "     else (select NVL(RPAD(numero_ext,100,' '),RPAD(' ',100,' ')) from bup_direccion dir where dir.id_persona = a.id_persona and es_fiscal = 'S' and rownum = 1) " +
				   "end as NUMCALLE, " +
				   "RPAD(' ',2,' ') as TIPOVIVI, " +
				   "RPAD(' ',2,' ') as ENTRADA, " +
				   "RPAD(' ',2,' ') as PISO, " +
				   "RPAD(' ',4,' ') as DEPARTAM, " +
				   "case when (select count(*) from bup_direccion dir where dir.id_persona = a.id_persona and es_fiscal = 'S' ) = 0 " +
				   "     then  " +
				   "         case when (select count(*) from bup_direccion dir where dir.id_persona = a.id_persona and id_tipo_direccion = 1) > 0 " +
				   "         then (select NVL(RPAD(colonia,100,' '),RPAD(' ',100,' ')) from bup_direccion dir where dir.id_persona = a.id_persona and id_tipo_direccion = 1 and rownum = 1) " +
				   "         else  " +
		           "   				CASE WHEN (select count(*) from bup_direccion dir where dir.id_persona = a.id_persona and rownum = 1) > 0 THEN " +
	               "   					(select NVL(RPAD(colonia,100,' '),RPAD(' ',100,' ')) from bup_direccion dir where dir.id_persona = a.id_persona and rownum = 1) " +
	               "				ELSE " +
	               "    				RPAD(' ',100,' ') " +
	               "				END " +  
				   "         end " +
				   "     else (select NVL(RPAD(colonia,100,' '),RPAD(' ',100,' ')) from bup_direccion dir where dir.id_persona = a.id_persona and es_fiscal = 'S' and rownum = 1)" +
				   "end as COLONIA," +
				   "case when (select count(*) from bup_direccion dir where dir.id_persona = a.id_persona and es_fiscal = 'S' ) = 0 " +
				   "     then  " +
				   "         case when (select count(*) from bup_direccion dir where dir.id_persona = a.id_persona and id_tipo_direccion = 1) > 0 " +
				   "         then (select NVL(RPAD(delegacion_municipio,80,' '),RPAD(' ',80,' ')) from bup_direccion dir where dir.id_persona = a.id_persona and id_tipo_direccion = 1 and rownum = 1) " +
				   "         else  " +
		           "		    CASE WHEN (select count(*) from bup_direccion dir where dir.id_persona = a.id_persona and rownum = 1) > 0 THEN " +
	               "    			(select NVL(RPAD(delegacion_municipio,80,' '),RPAD(' ',80,' ')) from bup_direccion dir where dir.id_persona = a.id_persona and rownum = 1) " +
	               "			ELSE " +
	               "    			RPAD(' ',80,' ') " +
	               "			END " + 
				   "         end " +
				   "     else (select NVL(RPAD(delegacion_municipio,80,' '),RPAD(' ',80,' ')) from bup_direccion dir where dir.id_persona = a.id_persona and es_fiscal = 'S' and rownum = 1) " +
				   "end as DELMUNI, " +
				   "case when (select count(*) from bup_direccion dir where dir.id_persona = a.id_persona and es_fiscal = 'S' ) = 0 " +
				   "     then  " +
				   "         case when (select count(*) from bup_direccion dir where dir.id_persona = a.id_persona and id_tipo_direccion = 1) > 0 " +
				   "         then (select NVL(RPAD( delegacion_municipio,80,' '),RPAD(' ',80,' ')) from bup_direccion dir where dir.id_persona = a.id_persona and id_tipo_direccion = 1 and rownum = 1) " +
				   "         else  " +
		           "		    CASE WHEN (select count(*) from bup_direccion dir where dir.id_persona = a.id_persona and rownum = 1) > 0 THEN " +
	               "    			(select NVL(RPAD(delegacion_municipio,80,' '),RPAD(' ',80,' ')) from bup_direccion dir where dir.id_persona = a.id_persona and rownum = 1) " +
	               "			ELSE " +
	               "    			RPAD(' ',80,' ') " +
	               "			END " + 
				   "         end " +
				   "     else (select NVL(RPAD( delegacion_municipio,80,' '),RPAD(' ',80,' ')) from bup_direccion dir where dir.id_persona = a.id_persona and es_fiscal = 'S' and rownum = 1) " +
				   "end as DESDELMUNI, " +
				   "case when (select count(*) from bup_direccion dir where dir.id_persona = a.id_persona and es_fiscal = 'S' ) = 0 " +
				   "     then  " +
				   "         case when (select count(*) from bup_direccion dir where dir.id_persona = a.id_persona and id_tipo_direccion = 1) > 0 " +
				   "         then (select NVL(RPAD(entidad_federativa,50,' '),RPAD(' ',50,' ')) from bup_direccion dir where dir.id_persona = a.id_persona and id_tipo_direccion = 1 and rownum = 1) " +
				   "         else  " +
 		           "			    CASE WHEN (select count(*) from bup_direccion dir where dir.id_persona = a.id_persona and rownum = 1) > 0 THEN " +
	               "    				(select NVL(RPAD(entidad_federativa,50,' '),RPAD(' ',50,' ')) from bup_direccion dir where dir.id_persona = a.id_persona and rownum = 1) " +
	               "				ELSE " +
	               "    				RPAD(' ',50,' ') " +
	               "				END " + 
				   "         end " +
				   "     else (select NVL(RPAD(entidad_federativa,50,' '),RPAD(' ',50,' ')) from bup_direccion dir where dir.id_persona = a.id_persona and es_fiscal = 'S' and rownum = 1) " +
				   "end as ESTADO, " +
				   "case when (select count(*) from bup_direccion dir where dir.id_persona = a.id_persona and es_fiscal = 'S' ) = 0 " +
				   "     then  " +
				   "         case when (select count(*) from bup_direccion dir where dir.id_persona = a.id_persona and id_tipo_direccion = 1) > 0 " +
				   "         then (select NVL(RPAD(entidad_federativa,50,' '),RPAD(' ',50,' ')) from bup_direccion dir where dir.id_persona = a.id_persona and id_tipo_direccion = 1 and rownum = 1) " +
				   "         else  " +
 		           "			    CASE WHEN (select count(*) from bup_direccion dir where dir.id_persona = a.id_persona and rownum = 1) > 0 THEN " +
	               "    				(select NVL(RPAD(entidad_federativa,50,' '),RPAD(' ',50,' ')) from bup_direccion dir where dir.id_persona = a.id_persona and rownum = 1) " +
	               "				ELSE " +
	               "    				RPAD(' ',50,' ') " +
	               "				END " + 
				   "         end " +
				   "     else (select NVL(RPAD(entidad_federativa,50,' '),RPAD(' ',50,' ')) from bup_direccion dir where dir.id_persona = a.id_persona and es_fiscal = 'S' and rownum = 1) " +
				   "end as DESESTADO, " +
				   "case when (select count(*) from bup_direccion dir where dir.id_persona = a.id_persona and es_fiscal = 'S' ) = 0 " +
				   "     then  " +
				   "         case when (select count(*) from bup_direccion dir where dir.id_persona = a.id_persona and id_tipo_direccion = 1) > 0 " +
				   "         then (select NVL(RPAD(codigo_postal,5,' '),RPAD(' ',5,' ')) from bup_direccion dir where dir.id_persona = a.id_persona and id_tipo_direccion = 1 and rownum = 1) " +
				   "         else  " +
		           "  			CASE WHEN (select count(*) from bup_direccion dir where dir.id_persona = a.id_persona and rownum = 1) > 0 THEN " +
	               "   				(select NVL(RPAD(codigo_postal,5,' '),RPAD(' ',5,' ')) from bup_direccion dir where dir.id_persona = a.id_persona and rownum = 1) " +
	               "			ELSE " +    
	               "   				RPAD(' ',5,' ') " +
	               "			END " +
				   "         end " +
				   "     else (select NVL(RPAD(codigo_postal,5,' '),RPAD(' ',5,' ')) from bup_direccion dir where dir.id_persona = a.id_persona and es_fiscal = 'S' and rownum = 1) " +
				   "end as CODPOST, " +
				   "case when (select count(*) from bup_direccion dir where dir.id_persona = a.id_persona and es_fiscal = 'S' ) = 0 " +
				   "     then  " +
				   "         case when (select count(*) from bup_direccion dir where dir.id_persona = a.id_persona and id_tipo_direccion = 1) > 0 " +
				   "         then (select NVL(RPAD(id_pais,8,' '),RPAD(' ',8,' ')) from bup_direccion dir where dir.id_persona = a.id_persona and id_tipo_direccion = 1 and rownum = 1) " +
				   "         else  " +
		           "		   	CASE WHEN (select count(*) from bup_direccion dir where dir.id_persona = a.id_persona and rownum = 1) > 0 THEN " +
	               "   				(select NVL(RPAD(id_pais,8,' '),RPAD(' ',8,' ')) from bup_direccion dir where dir.id_persona = a.id_persona and rownum = 1) " +
	               "			ELSE " +
	               "   				RPAD(' ',8,' ') " +
	               "			END " + 
				   "         end " +
				   "     else (select NVL(RPAD(id_pais,8,' '),RPAD(' ',8,' ')) from bup_direccion dir where dir.id_persona = a.id_persona and es_fiscal = 'S' and rownum = 1) " +
				   "end as CODPAIS, " +
				   "NVL(RPAD(CURP,20,' '),RPAD(' ',20,' ')) as CURP,    " +
				   "NVL(RPAD(RFC,13,' '),RPAD(' ',13,' ')) as RFC,    " +
				   "NVL(RPAD(nss,11,' '),RPAD(' ',11,' ')) as IMSS,    " +
				   "RPAD(' ',12,' ') as INFONAVI, " +
				   "RPAD(' ',1,' ') as SUJGRUP,    " + 
				   "RPAD(' ',2,' ') as SUJSUBG1,    " +
				   "NVL(RPAD(to_char(fecha_nacimiento,'yyyy-mm-dd'),10,' '),RPAD(' ',10,' ')) FENACIMI, " +
				   "NVL(RPAD(id_sector,6,' '),RPAD(' ',6,' ')) as CNAEG,  " +
				   "NVL(RPAD(id_sector,2,' '),RPAD(' ',2,' ')) as CNO,  " +
				   "NVL(RPAD(id_sector,3,' '),RPAD(' ',3,' ')) as SEGMENTO,  " +
				   "RPAD(' ',3,' ') as NUMDOCLI, " +
				   "RPAD(' ',1,' ') as INDGRANC, " +
				   "RPAD(' ',1,' ') as CLATELEF1, " +
				   "case when (select count(*) FROM BUP_TELEFONO BT WHERE BT.ID_PERSONA = A.ID_PERSONA and id_tipo_telefono = 0) > 0 " +
				   "     then (SELECT NVL(RPAD(NUMERO_LOCAL,15,' '),RPAD(' ',15,' ')) FROM BUP_TELEFONO BT WHERE BT.ID_PERSONA = A.ID_PERSONA and id_tipo_telefono = 0 and rownum = 1 ) " +
				   "     else case when (select count(*) FROM BUP_TELEFONO BT WHERE BT.ID_PERSONA = A.ID_PERSONA and id_tipo_telefono = 1) > 0 " +
				   "               then (SELECT NVL(RPAD(NUMERO_LOCAL,15,' '),RPAD(' ',15,' ')) FROM BUP_TELEFONO BT WHERE BT.ID_PERSONA = A.ID_PERSONA and id_tipo_telefono = 1 and rownum = 1 ) " +
				   "               else case when (select count(*) FROM BUP_TELEFONO BT WHERE BT.ID_PERSONA = A.ID_PERSONA and id_tipo_telefono = 2) > 0 " +
				   "               then (SELECT NVL(RPAD(NUMERO_LOCAL,15,' '),RPAD(' ',15,' ')) FROM BUP_TELEFONO BT WHERE BT.ID_PERSONA = A.ID_PERSONA and id_tipo_telefono = 2 and rownum = 1 ) " +
				   "               else case when (select count(*) FROM BUP_TELEFONO BT WHERE BT.ID_PERSONA = A.ID_PERSONA and id_tipo_telefono = 3) > 0 " +
				   "               then (SELECT NVL(RPAD(NUMERO_LOCAL,15,' '),RPAD(' ',15,' ')) FROM BUP_TELEFONO BT WHERE BT.ID_PERSONA = A.ID_PERSONA and id_tipo_telefono = 3 and rownum = 1 ) " +
				   "               else case when (select count(*) FROM BUP_TELEFONO BT WHERE BT.ID_PERSONA = A.ID_PERSONA and id_tipo_telefono = 4) > 0 " +
				   "               then (SELECT NVL(RPAD(NUMERO_LOCAL,15,' '),RPAD(' ',15,' ')) FROM BUP_TELEFONO BT WHERE BT.ID_PERSONA = A.ID_PERSONA and id_tipo_telefono = 4 and rownum = 1 ) " +
				   "               else case when (select count(*) FROM BUP_TELEFONO BT WHERE BT.ID_PERSONA = A.ID_PERSONA and id_tipo_telefono = 5) > 0 " +
				   "               then (SELECT NVL(RPAD(NUMERO_LOCAL,15,' '),RPAD(' ',15,' ')) FROM BUP_TELEFONO BT WHERE BT.ID_PERSONA = A.ID_PERSONA and id_tipo_telefono = 5 and rownum = 1 ) " +
				   "               else case when (select count(*) FROM BUP_TELEFONO BT WHERE BT.ID_PERSONA = A.ID_PERSONA and id_tipo_telefono = 6) > 0 " +
				   "               then (SELECT NVL(RPAD(NUMERO_LOCAL,15,' '),RPAD(' ',15,' ')) FROM BUP_TELEFONO BT WHERE BT.ID_PERSONA = A.ID_PERSONA and id_tipo_telefono = 6 and rownum = 1 ) " +
				   "               else case when (select count(*) FROM BUP_TELEFONO BT WHERE BT.ID_PERSONA = A.ID_PERSONA and id_tipo_telefono = 7) > 0 " +
				   "               then (SELECT NVL(RPAD(NUMERO_LOCAL,15,' '),RPAD(' ',15,' '))L FROM BUP_TELEFONO BT WHERE BT.ID_PERSONA = A.ID_PERSONA and id_tipo_telefono = 7 and rownum = 1 ) " +
				   "               else case when (select count(*) FROM BUP_TELEFONO BT WHERE BT.ID_PERSONA = A.ID_PERSONA and id_tipo_telefono = 8) > 0 " +
				   "               then (SELECT NVL(RPAD(NUMERO_LOCAL,15,' '),RPAD(' ',15,' ')) FROM BUP_TELEFONO BT WHERE BT.ID_PERSONA = A.ID_PERSONA and id_tipo_telefono = 8 and rownum = 1 ) " +
				   "               else case when (select count(*) FROM BUP_TELEFONO BT WHERE BT.ID_PERSONA = A.ID_PERSONA and id_tipo_telefono = 10) > 0 " +
				   "               then (SELECT NVL(RPAD(NUMERO_LOCAL,15,' '),RPAD(' ',15,' ')) FROM BUP_TELEFONO BT WHERE BT.ID_PERSONA = A.ID_PERSONA and id_tipo_telefono = 10 and rownum = 1 ) " +
				   "               else case when (select count(*) FROM BUP_TELEFONO BT WHERE BT.ID_PERSONA = A.ID_PERSONA and id_tipo_telefono = 11) > 0 " +
				   "               then (SELECT NVL(RPAD(NUMERO_LOCAL,15,' '),RPAD(' ',15,' ')) FROM BUP_TELEFONO BT WHERE BT.ID_PERSONA = A.ID_PERSONA and id_tipo_telefono = 11 and rownum = 1 ) " +
				   "               else case when (select count(*) FROM BUP_TELEFONO BT WHERE BT.ID_PERSONA = A.ID_PERSONA and id_tipo_telefono = 12) > 0 " +
				   "               then (SELECT NVL(RPAD(NUMERO_LOCAL,15,' '),RPAD(' ',15,' ')) FROM BUP_TELEFONO BT WHERE BT.ID_PERSONA = A.ID_PERSONA and id_tipo_telefono = 12 and rownum = 1 ) " +
				   "               else case when (select count(*) FROM BUP_TELEFONO BT WHERE BT.ID_PERSONA = A.ID_PERSONA and id_tipo_telefono = 14) > 0 " +
				   "               then (SELECT NVL(RPAD(NUMERO_LOCAL,15,' '),RPAD(' ',15,' ')) FROM BUP_TELEFONO BT WHERE BT.ID_PERSONA = A.ID_PERSONA and id_tipo_telefono = 14 and rownum = 1 ) " +
				   "               else case when (select count(*) FROM BUP_TELEFONO BT WHERE BT.ID_PERSONA = A.ID_PERSONA and id_tipo_telefono = 15) > 0 " +
				   "               then (SELECT NVL(RPAD(NUMERO_LOCAL,15,' '),RPAD(' ',15,' ')) FROM BUP_TELEFONO BT WHERE BT.ID_PERSONA = A.ID_PERSONA and id_tipo_telefono = 15 and rownum = 1 ) " +
				   "               else case when (select count(*) FROM BUP_TELEFONO BT WHERE BT.ID_PERSONA = A.ID_PERSONA and id_tipo_telefono = 16) > 0 " +
				   "               then (SELECT NVL(RPAD(NUMERO_LOCAL,15,' '),RPAD(' ',15,' ')) FROM BUP_TELEFONO BT WHERE BT.ID_PERSONA = A.ID_PERSONA and id_tipo_telefono = 16 and rownum = 1 ) " +
				   "               else case when (select count(*) FROM BUP_TELEFONO BT WHERE BT.ID_PERSONA = A.ID_PERSONA and id_tipo_telefono = 17) > 0 " +
				   "               then (SELECT NVL(RPAD(NUMERO_LOCAL,15,' '),RPAD(' ',15,' ')) FROM BUP_TELEFONO BT WHERE BT.ID_PERSONA = A.ID_PERSONA and id_tipo_telefono = 17 and rownum = 1 ) " +
				   "               else case when (select count(*) FROM BUP_TELEFONO BT WHERE BT.ID_PERSONA = A.ID_PERSONA and id_tipo_telefono = 18) > 0 " +
				   "               then (SELECT NVL(RPAD(NUMERO_LOCAL,15,' '),RPAD(' ',15,' ')) FROM BUP_TELEFONO BT WHERE BT.ID_PERSONA = A.ID_PERSONA and id_tipo_telefono = 18 and rownum = 1 ) " +
				   "               else case when (select count(*) FROM BUP_TELEFONO BT WHERE BT.ID_PERSONA = A.ID_PERSONA and id_tipo_telefono = 41) > 0 " +
				   "               then (SELECT NVL(RPAD(NUMERO_LOCAL,15,' '),RPAD(' ',15,' ')) FROM BUP_TELEFONO BT WHERE BT.ID_PERSONA = A.ID_PERSONA and id_tipo_telefono = 41 and rownum = 1 ) " +
				   "else (select (rpad(' ',15,' ')) from dual) " +
				   "               end  " +
				   "               end  " +
				   "               end  " +
				   "               end  " +
				   "               end " +
				   "               end " +
				   "               end " +
				   "               end " +
				   "               end  " +
				   "               end  " +
				   "               end  " +
				   "               end  " +
				   "               end  " +
				   "               end " +
				   "               end " +
				   "               end " +
				   "               end " +
				   "end  AS NUMTELEF1, " +
				   "RPAD(' ',1,' ') as CLATELEF2, " +
				   "RPAD(' ',17,' ') as NUMTELEF2, " +
				   "RPAD(' ',1,' ') as CLATELEF3, " +
				   "RPAD(' ',17,' ') as NUMTELEF3, " +
				   "RPAD(' ',4,' ') as CRGESTOR, " +
				   "RPAD(' ',10,' ') as FUNCIONA, " +
				   "RPAD(' ',2,' ') as AREANEG, " +
				   "RPAD(' ',1,' ') as INDBAJA, " +
				   "NVL(RPAD(to_char(a.fecha_alta,'yyyy-mm-dd'),10,' '),RPAD(' ',10,' ')) FEALTCLI, " +
				   "NVL(RPAD(to_char(a.fecha_ult_mod,'yyyy-mm-dd hh:mi:ss AM'),22,' '),RPAD(' ',22,' ')) PEHSTAMP, " +
				   "NVL(RPAD(nacionalidad,80,' '),RPAD(' ',80,' ')) as PECNACIO,  " +
				   "NVL(RPAD(c.descripcion,80,' '),RPAD(' ',80,' ')) as LUGANACI,  " +
				   "RPAD(' ',3,' ') as PECNARES, " +
				   "case when (select count(*) from bup_medio_contacto bmc where bmc.id_persona = a.id_persona and id_tipo_medcon = 0 and rownum = 1 ) > 0 " +
				   "     then (SELECT NVL(RPAD(valor,80,' '),RPAD(' ',80,' ')) from bup_medio_contacto bmc where bmc.id_persona = a.id_persona and id_tipo_medcon = 0 and rownum = 1 ) " +
				   "     else case when (select count(*) from bup_medio_contacto bmc where bmc.id_persona = a.id_persona and id_tipo_medcon = 1 and rownum = 1 ) > 0 " +
				   "               then (SELECT NVL(RPAD(valor,80,' '),RPAD(' ',80,' ')) from bup_medio_contacto bmc where bmc.id_persona = a.id_persona and id_tipo_medcon = 1 and rownum = 1) " +
				   "               else case when (select count(*) from bup_medio_contacto bmc where bmc.id_persona = a.id_persona and id_tipo_medcon = 3 and rownum = 1 ) > 0 " +
				   "               then (SELECT NVL(RPAD(valor,80,' '),RPAD(' ',80,' ')) from bup_medio_contacto bmc where bmc.id_persona = a.id_persona and id_tipo_medcon = 3 and rownum = 1) " +
				   "               else case when (select count(*) from bup_medio_contacto bmc where bmc.id_persona = a.id_persona and id_tipo_medcon = 4 and rownum = 1 ) > 0 " +
				   "               then (SELECT NVL(RPAD(valor,80,' '),RPAD(' ',80,' ')) from bup_medio_contacto bmc where bmc.id_persona = a.id_persona and id_tipo_medcon = 4 and rownum = 1) " +
				   "               else case when (select count(*) from bup_medio_contacto bmc where bmc.id_persona = a.id_persona and id_tipo_medcon = 5 and rownum = 1 ) > 0 " +
				   "               then (SELECT NVL(RPAD(valor,80,' '),RPAD(' ',80,' ')) from bup_medio_contacto bmc where bmc.id_persona = a.id_persona and id_tipo_medcon = 5 and rownum = 1) " +
				   "               else case when (select count(*) from bup_medio_contacto bmc where bmc.id_persona = a.id_persona and id_tipo_medcon = 15 and rownum = 1 ) > 0 " +
				   "               then (SELECT NVL(RPAD(valor,80,' '),RPAD(' ',80,' ')) from bup_medio_contacto bmc where bmc.id_persona = a.id_persona and id_tipo_medcon = 15 and rownum = 1) " +
				   "               else case when (select count(*) from bup_medio_contacto bmc where bmc.id_persona = a.id_persona and id_tipo_medcon = 16 and rownum = 1 ) > 0 " +
				   "               then (SELECT NVL(RPAD(valor,80,' '),RPAD(' ',80,' ')) from bup_medio_contacto bmc where bmc.id_persona = a.id_persona and id_tipo_medcon = 16 and rownum = 1) " +
				   "               else case when (select count(*) from bup_medio_contacto bmc where bmc.id_persona = a.id_persona and id_tipo_medcon = 17 and rownum = 1 ) > 0 " +
				   "               then (SELECT NVL(RPAD(valor,80,' '),RPAD(' ',80,' ')) from bup_medio_contacto bmc where bmc.id_persona = a.id_persona and id_tipo_medcon = 17 and rownum = 1) " +
				   "               else case when (select count(*) from bup_medio_contacto bmc where bmc.id_persona = a.id_persona and id_tipo_medcon = 700 and rownum = 1 ) > 0 " +
				   "               then (SELECT NVL(RPAD(valor,80,' '),RPAD(' ',80,' ')) from bup_medio_contacto bmc where bmc.id_persona = a.id_persona and id_tipo_medcon = 700 and rownum = 1) " +
				   "               else case when (select count(*) from bup_medio_contacto bmc where bmc.id_persona = a.id_persona and id_tipo_medcon = 710 and rownum = 1 ) > 0 " +
				   "               then (SELECT NVL(RPAD(valor,80,' '),RPAD(' ',80,' ')) from bup_medio_contacto bmc where bmc.id_persona = a.id_persona and id_tipo_medcon = 710 and rownum = 1) " +
				   "               else case when (select count(*) from bup_medio_contacto bmc where bmc.id_persona = a.id_persona and id_tipo_medcon = 800 and rownum = 1 ) > 0 " +
				   "               then (SELECT NVL(RPAD(valor,80,' '),RPAD(' ',80,' ')) from bup_medio_contacto bmc where bmc.id_persona = a.id_persona and id_tipo_medcon = 800 and rownum = 1) " +
				   "               else case when (select count(*) from bup_medio_contacto bmc where bmc.id_persona = a.id_persona and id_tipo_medcon = 810 and rownum = 1 ) > 0 " +
				   "               then (SELECT NVL(RPAD(valor,80,' '),RPAD(' ',80,' ')) from bup_medio_contacto bmc where bmc.id_persona = a.id_persona and id_tipo_medcon = 810 and rownum = 1) " +
				   "               else (select (rpad(' ',80,' ')) from dual) " +
				   "               end  " +
				   "               end  " +
				   "               end  " +
				   "               end  " +
				   "               end " +
				   "               end " +
				   "               end " + 
				   "               end " +
				   "               end  " +
				   "               end  " +
				   "          end     " +   
				   "end as EMAIL, " +
				   "RPAD(' ',28,' ') as WEB2, " +
				   "RPAD(' ',50,' ') as CENTRABA, " +
				   "RPAD(' ',10,' ') as FECHINGR, " +
				   "RPAD(' ',1,' ') as REGIMENM, " +
				   "RPAD(' ',3,' ') as CODESTUD, " +
				   "RPAD(' ',1,' ') as EMPLEADO, " +
				   "RPAD(' ',2,' ') as ENTINACI, " +
				   "RPAD(' ',10,' ') as FECNACHI, " +
				   "RPAD(' ',2,' ') as NUMAUTOS, " +
				   "RPAD(' ',2,' ') as NUMHIJOS, " +
				   "RPAD(' ',2,' ') as NUMBIENE, " +
				   "RPAD(' ',2,' ') as ORIGEN, " +
				   "RPAD(' ',4,' ') as TIEMPRES, " +
				   "RPAD(' ',1,' ') as CODAFICI, " +
				   "RPAD(' ',4,' ') as OTROSBAN, " +
				   "RPAD(' ',2,' ') as COBGEOG, " +
				   "RPAD(' ',4,' ') as NUMSUC, " +
				   "RPAD(' ',2,' ') as NUMEMPLE, " +
				   "RPAD(' ',1,' ') as FUENTEIN, " +
				   "RPAD(' ',1,' ') as INGMENS, " +
				   "RPAD(' ',1,' ') as IMPVTAS, " +
				   "RPAD(' ',1,' ') as ACTIVO, " +
				   "RPAD(' ',1,' ') as PASIVO, " +
				   "RPAD(' ',1,' ') as CAPITAL, " +
				   "RPAD(' ',1,' ') as IMPEXP, " +
				   "RPAD(' ',10,' ') as FEALTCLI2, " +
				   "RPAD(' ',10,' ') as FECPROC, " +
				   "RPAD(' ',1,' ') as RIESGO, " +
				   "RPAD(' ',20,' ') as FIEL, " +
				   "RPAD(' ',1,' ') as ART10, " +
				   "RPAD(' ',30,' ') as VARIOS " +
				   "from bup_persona a,bup_persona_fisica b,bup_pais c,bup_persona_cuenta_rol d,bup_cuenta_contrato e,bup_persona_moral f " +
				   "where a.id_persona = b.id_persona (+) " +
				   "and a.id_persona = f.id_persona (+) " +
				   "and a.id_pais = c.id_pais(+) " +
				   "and d.id_persona = a.id_persona " +
				   "and d.no_cuenta = e.no_cuenta " +
				   "and id_rol = 'TIT' " +
				   "and id_tipo_cuenta = 'CAM10' " +
				   "and trunc(e.fecha_apertura) = ? " 
				   ;
    
   /**
    * Constante para la consulta de contratos dados de alta
    */
   private static String QUERY_LAYOUT_CONTRATOS =
		   "select RPAD('S'||c.id_persona,9,' ') as NUMCLIEN, " +
				   "'SICA' as PECENTID, " +
				   "NVL (RPAD(id_sucursal,4,' '),RPAD(' ',4,' ')) as OFIAPE, " +
				   "'CV' as CODISER, " +
				   "NVL (RPAD(replace(a.no_cuenta,'-',''),33,' '),RPAD(' ',33,' ')) as CTALTA," +
				   "'T' as CLAINTER, " +
				   "'01' as SECINTER, " +
				   "'000' as NUMDOMIC12, " +
				   "NVL(RPAD(to_char(fecha_apertura,'yyyy-mm-dd'),10,' '),RPAD(' ',10,' ')) FEALRELA, " +
				   "NVL(RPAD(to_char(fecha_apertura,'yyyy-mm-dd'),10,' '),RPAD(' ',10,' ')) FECHAPE, " +
				   "RPAD(' ',26,' ') as PEHSTAMP, " +
				   "NVL (RPAD(decode(a.status,'ACTIVA',1,0),1,' '),RPAD(' ',1,' ')) as INDBAJA, " +
				   "NVL (RPAD(id_tipo_cuenta,5,' '),RPAD(' ',5,' ')) as PRODUCTO, " +
				   "'0000' as SUBPRO, " +
				   "NVL(RPAD(to_char(fecha_cierre,'yyyy-mm-dd'),10,' '),RPAD(' ',10,' ')) FECHCANCEL, " +
				   "NVL(RPAD(to_char(fecha_cierre,'yyyy-mm-dd hh:mi:ss AM'),22,' '),RPAD(' ',22,' ')) FECHAUMO, " +
				   "NVL(RPAD(id_sector,6,' '),RPAD(' ',6,' ')) as SECTOR,  " +
				   "'0' as TASAINT, " +
				   "'0' as SDOPROMDOCE, " +
				   "NVL (RPAD(decode(a.status,'ACTIVA',1,0),1,' '),RPAD(' ',1,' ')) as INDESTA, " +
				   "'0000' as \"CENTRO-CONTAB\", " +
				   "NVL(RPAD(to_char(fecha_apertura,'yyyy-mm-dd'),10,' '),RPAD(' ',10,' ')) as \"FECHA-ALTA\", " +
				   "'A' as INDICADOR, " +
				   "'   ' as DIVISA, " +
				   "'0' as BALANCE, " +
				   "decode (d.no_empleado,null,'0','1') as EMPLEADO, " +
				   "NVL(RPAD(d.no_empleado,20,' '),RPAD(' ',20,' ')) as CODEMPLEADO " +
				   "from bup_cuenta_contrato a, bup_persona_cuenta_rol b, bup_persona c,bup_empleado_ixe d " +
				   "where a.no_cuenta = b.no_cuenta  " +
				   "and b.id_persona = c.id_persona  " +
				   "and c.id_persona = d.id_persona (+) " +
				   "and id_tipo_cuenta = 'CAM10' " +
				   "and id_rol = 'TIT' " +
				   "and trunc(a.fecha_apertura) = ?"
				   ;

   /**
    * Constante para la consulta de contratos dados de alta
    */
   private static String QUERY_LAYOUT_TRANSACCIONES =

		   "select NVL (RPAD(replace(a.no_cuenta,'-',''),33,' '),RPAD(' ',33,' ')) as Account_Key, " +
				   "NVL(RPAD(to_char(b.fecha_apertura,'yyyy-mm-dd'),10,' '),RPAD(' ',10,' ')) Acct_Open_Date, " +
				   "NVL(RPAD(to_char(fecha_liquidado,'yyyy-mm-dd'),10,' '),RPAD(' ',10,' ')) ROW_UPDATE_DATE, " +
				   "NVL(RPAD(c.mnemonico,15,' '),RPAD(' ',15,' ')) as TRANS_TYPE_CD, " +
				   "'1' as Valor, " +
				   "' ' as TRANSACTION_TYPE_CD, " +
				   "RPAD(a.id_deal||'-'||c.folio_detalle,15,' ') as TRANSACTION_CODE_CD, " +
				   "decode (recibimos,'S','C','V') as Trans_Type_Category, " +
				   "'SICA' as Trans_Type_Cd, " +
				   "NVL (RPAD(replace(a.no_cuenta,'-',''),33,' '),RPAD(' ',33,' ')) as Account_Key, " +
				   "RPAD(decode(instr(f.monto,'.'),0,f.monto*100,decode(length(substr(f.monto,instr(f.monto,'.'),3)),2,f.monto*100,3,replace(f.monto,'.',''),f.monto)),20,' ') as Acct_Currency_Amount, " +
				   "NVL(RPAD(f.ID_DIVISA,3,' '),RPAD(' ',3,' ')) as ID_DIVISA,  " +
				   "RPAD(decode(instr(MONTO_MONEDA_NACIONAL,'.'),0,MONTO_MONEDA_NACIONAL*100,decode(length(substr(MONTO_MONEDA_NACIONAL,instr(MONTO_MONEDA_NACIONAL,'.'),3)),2,MONTO_MONEDA_NACIONAL*100,3,replace(MONTO_MONEDA_NACIONAL,'.',''),MONTO_MONEDA_NACIONAL)),20,' ') as MONTO_MONEDA_NACIONAL, " +
				   "NVL(RPAD(no_cuenta_beneficiario,34,' '),NVL((RPAD(K.CLABE,34,' ')),RPAD(' ',34,' '))) as Payee_id_No, " +
				   "' ' as Transaction_Key, " +
				   "NVL(RPAD(no_cuenta_beneficiario,34,' '),NVL((RPAD(K.CLABE,34,' ')),RPAD(' ',34,' '))) as Opp_Account_Key, " +
				   "case when h.id_pais is not null " +
				   "then RPAD(h.id_pais,8,' ') " +
				   "else case when k.id_banco is not null " +
				   "then 'MX      ' " +
				   "else RPAD(' ',8,' ') end end as Opp_Country_Cd, " +
				   "NVL(RPAD(decode(j.id_banco,1,nvl(k.id_banco,j.id_banco),j.id_banco),9,' '),RPAD(' ',9,' ')) as Opp_Org_key, " +
				   "'1' as Cod1, " +
				   "'1' as Cod2 " +
				   "from sc_deal a, bup_cuenta_contrato b,sc_deal_detalle c,tes_detalle_liquidacion e,sc_deal_posicion f,sc_plantilla g,sc_plantilla_intl h,tes_matriz i, bup_banco j ,sc_plantilla_tran_nacional k " +
				   "where a.no_cuenta = b.no_cuenta " +
				   "and a.id_deal = c.id_deal " +
				   "and c.id_deal_posicion = e.id_detalle_ext(+) " +
				   "and c.id_deal_posicion = f.id_deal_posicion " +
				   "and c.id_plantilla = g.id_plantilla (+) " +
				   "and g.id_plantilla = h.id_plantilla(+) " +
				   "and c.mnemonico = i.mnemonico (+) " +
				   "and i.id_banco = j.id_banco(+) " +
				   "and g.id_plantilla = k.id_plantilla (+) " +
				   "and status_detalle_deal = 'TT' " +
				   "and trunc(fecha_liquidado) = ? "
				   ;
		   

   
   /**
     * Constante para la consulta de operaciones compra venta
     */
    private static String QUERY_OPERACIONES_COMPRAS_VENTAS = 
    	"SELECT "
    		+ "deal.ID_DEAL as \"Deal\", "
    		+ "deal.id_canal Canal, "
    		+ "deal_det.FOLIO_DETALLE Folio, "
    		+ "deal.TIPO_VALOR \"Fecha Valor\", "
    		+ "deal.FECHA_CAPTURA \"Fecha Captura\", " 
    		+ "deal.FECHA_LIQUIDACION \"Fecha Liq.\", "
    		+ "deal_det.mnemonico, " 
    		+ "DECODE(deal_pos.recibimos, 'S', 'Compra', 'Venta') \"Op.\", "
    		+ "cte.NOMBRE_CORTO \"Cliente\", "
    		+ "deal_det.clave_forma_liquidacion \"Producto\", " 
    		+ "deal_pos.MONTO \"Monto\", " 
    		+ "deal_pos.ID_DIVISA, "
    		+ "deal_pos.tipo_cambio \"Tipo Cambio Cliente\", " 
    		+ "deal_det.tipo_cambio_mesa \"Tipo Cambio Mesa\", "
    		+ "deal_pos.MONTO * deal_pos.tipo_cambio \"Monto Equiv.\", "
    		+ "DECODE(deal_pos.recibimos, 'S', "
    			+ "(deal_det.tipo_cambio_mesa - deal_pos.tipo_cambio) * deal_pos.monto, "
    			+ "(deal_pos.tipo_cambio - deal_det.tipo_cambio_mesa) * deal_pos.monto "
    		+ ") \"UTILIDAD\", " 
    		+ "prom.NOMBRE || ' ' || prom.PATERNO || ' ' || prom.MATERNO \"Promotor\", " 
    		+ "CASE WHEN INSTR(deal_det.EVENTOS_DETALLE_DEAL, 'P') > 0 THEN 'S' else 'No' end case, "
    		+ "cta_cont.no_cuenta \"Contr. Sica\", "
    		+ "deal_det.status_detalle_deal \"Status\", "
    		+ "mesa.nombre \"Mesa\", "
    		+ "sec_eco.DESCRIPCION \"Sector Econ.\", "
    		+ "sec_eco.DESC_SECTOR_BANXICO \"Sector Banxico\", "
    		+ "CASE WHEN canal.ID_SUCURSAL IS NOT NULL THEN 'SI' ELSE 'NO' END \"Desde Teller\", " 
        	+ "suc.NOMBRE \"Nombre Sucursal\" "
        	
        	+ ", zona.NOMBRE \"Nombre Zona\" "
        	+ ", plaza.NOMBRE \"Nombre Plaza\" "
        	+ ", division.NOMBRE \"Nombre Division\" "
        	+ ", deal.ES_CLIENTE \"Es CLiente\" "
        	
    	+ "FROM "
    		+ "SC_DEAL deal, " 
    		+ "SC_DEAL_DETALLE deal_det, " 
    		+ "SC_DEAL_POSICION deal_pos, "
    		+ "BUP_CUENTA_CONTRATO cta_cont, "
    		+ "BUP_PERSONA_RIM pers_rim, " 
    		+ "BUP_PERSONA cte, " 
    		+ "BUP_SECTOR_ECONOMICO sec_eco, " 
    		+ "BUP_PERSONA_FISICA prom, "
    		+ "SC_MESA_CAMBIO mesa, "
    		+ "SC_CANAL canal, "
    		+ "BUP_SUCURSAL suc " // agregado para obtener el nombre de la sucursal
    		+ ", SC_PRECIO_REFERENCIA precio " // ==== EQUIV USD === : Tabla para obtener el precio de compra/venta (factor para conv a USD)
    		
    		+ ", SC_SUCURSAL_ZONA suc_zona "
    	    + ", SC_ZONA zona "
    	    + ", SC_PLAZA plaza "
    	    + ", SC_DIVISION division "
    	
    	+ "WHERE "
    		+ "deal.ID_DEAL = deal_det.ID_DEAL "
    		+ "AND deal_det.ID_DEAL_POSICION = deal_pos.ID_DEAL_POSICION "
    		+ "AND deal.NO_CUENTA = cta_cont.NO_CUENTA (+) "
    		+ "AND cta_cont.RIM_NO = pers_rim.RIM_NO "
    		+ "AND pers_rim.ID_PERSONA = cte.ID_PERSONA "
    		+ "AND cte.ID_SECTOR = sec_eco.ID_SECTOR (+) "
    		+ "AND deal.FECHA_CAPTURA BETWEEN ? AND ? "			//Fechas
    		+ "AND deal_det.STATUS_DETALLE_DEAL != 'CA' "
    		+ "AND deal.ID_PROMOTOR = prom.ID_PERSONA (+) "
    		+ "AND mesa.ID_MESA_CAMBIO = deal_pos.id_mesa_cambio "
    		// + "AND deal_pos.MONTO BETWEEN ? AND ? " // ==== EQUIV USD === : El filtro de monto será definido por la bandera esMontoEquivUsd
    		+ "AND canal.ID_CANAL = deal.ID_CANAL "
    		+ "AND suc.ID_SUCURSAL(+) = canal.ID_SUCURSAL " // relación para obtener el nombre de la sucursal
    		+ "AND deal_det.ID_PRECIO_REFERENCIA = precio.ID_PRECIO_REFERENCIA (+) " // ==== EQUIV USD === : Relación para obtener el Precio de referencia (factor para conv a USD)
    
    		+ "AND suc.ID_SUCURSAL = suc_zona.ID_SUCURSAL(+) " 
    	    + "AND suc_zona.ID_ZONA = zona.ID_ZONA(+) "
    	    + "AND zona.ID_PLAZA = plaza.ID_PLAZA(+) "
    	    + "AND plaza.ID_DIVISION = division.ID_DIVISION(+) ";
    
    /**
     * Constante para la condicion de la consulta de operaciones compra venta 
     */
    private static String QUERY_OCV_CONDICION_PROMOTOR = "AND deal.ID_PROMOTOR = ? ";		//Id Promotor
    
    /**
     * Constante para la filtrar operaciones de usuarios no Staff
     */
    //private static String QUERY_OCV_CONDICION_USUARIOS_NO_STAFF = "AND deal.ID_PROMOTOR IN (?) ";		//Id Promotor
    
    /**
     * Constante para la condicion de la consulta de operaciones compra venta 
     */
    private static String QUERY_OCV_CONDICION_OPERACION = "AND deal_pos.recibimos = ? ";	//Tipo de operacion
    
    /**
     * Constante para la condicion de la consulta de operaciones compra venta 
     */
    private static String QUERY_OCV_CONDICION_DIVISA = "AND deal_pos.ID_DIVISA = ? ";		//Id divisa
    
    /**
     * Constante para la condicion de la consulta de operaciones compra venta 
     */
    private static String QUERY_OCV_CONDICION_PRODUCTO = "AND deal_det.clave_forma_liquidacion = ? ";  //Producto
    
    /**
     * Constante para la condicion de la consulta de operaciones compra venta 
     */
    private static String QUERY_OCV_CONDICION_SIN_TELLER = "AND canal.ID_CANAL NOT IN "
    	+"(SELECT DISTINCT ID_CANAL FROM SC_CANAL WHERE ID_SUCURSAL > 0) ";
    
    /**
     * Constante para la condicion de la consulta de operaciones compra venta 
     */
    private static String QUERY_OCV_CONDICION_SOLO_TELLER = "AND canal.ID_SUCURSAL > 0 ";
    
    
    private static String QUERY_OCV_CONDICION_CANAL_ESPECIFICO = "AND canal.ID_CANAL = ? ";
    
    /**
     * Constante para el orden de la consulta de operaciones compra venta 
     */
    private static String QUERY_OCV_ORDEN = "order by deal_pos.RECIBIMOS, deal.ID_DEAL ";
    
    /**
     * Constante para filtrar por sucursal
     */
    private static String QUERY_OCV_CONDICION_SUCURSAL = "AND suc.ID_SUCURSAL = ? ";
    
    /**
     * Constante para filtrar por zona
     */
    private static String QUERY_OCV_CONDICION_ZONA = "AND zona.ID_ZONA = ? ";
    
    /**
     * Constante para filtrar por Plaza
     */
    private static String QUERY_OCV_CONDICION_PLAZA = "AND plaza.ID_PLAZA = ? ";
    
    /**
     * Constante para filtrar por División
     */
    private static String QUERY_OCV_CONDICION_DIVISION = "AND division.ID_DIVISION = ? ";
    
    /**
     * Constante para filtrar por contrato SICA
     */
    private static String QUERY_OCV_CONDICION_CONTRATO_SICA = "AND cta_cont.no_cuenta = ? ";
    
    /**
     * Constante para filtrar por Tipo Cliente
     */
    private static String QUERY_OCV_CONDICION_TIPO_CLIENTE = "AND deal.ES_CLIENTE = ? ";
    
    /**
     * Constante para filtrar por monto
     */
    private static String QUERY_OCV_CONDICION_MONTO = "AND deal_pos.MONTO BETWEEN ? AND ? "; // ==== EQUIV USD === : Filtro para monto normal
    
    /**
     * Constante para filtrar por monto equivalente USD
     */
    private static String QUERY_OCV_CONDICION_MONTO_USD = "" 
    	+ "AND deal_pos.MONTO * deal_pos.TIPO_CAMBIO / DECODE("
    		+ "DECODE(deal_pos.RECIBIMOS, 'S', precio.PRECIO_COMPRA, precio.PRECIO_VENTA), "
    		+ "0, "
    		+ "NULL, "
    		+ "DECODE(deal_pos.RECIBIMOS, 'S', precio.PRECIO_COMPRA, precio.PRECIO_VENTA)"
    	+ ") BETWEEN ? AND ? "; // ==== EQUIV USD === : Filtro para monto equiv USD

    /**
     * Constante para la consulta de autorizaciones de mesa de control 
     */
    private static String QUERY_AUTORIZACIONES_MESA_CONTROL = "SELECT deal.FECHA_CAPTURA \"Fecha Captura\", "
    	+"cte.NOMBRE_CORTO \"Cliente\", cta_cont.no_cuenta \"Contr. Sica\", " 
    	+"prom.NOMBRE||' '||prom.PATERNO||' '||prom.MATERNO \"Promotor\", "
    	+"SUBSTR(fac.descripcion,20) \"Canal\", DECODE(cte.tipo_persona, 'PM', 'Persona Moral', 'AE', " +
    			"'Persona con Actividad Empresarial',  'Persona F\u00EDsica') \"Tipo Persona\", "
    	+"deal.ID_DEAL \"Deal\", " 
    	+"DECODE(deal_pos.recibimos, 'S', 'Compra', 'Venta') \"Op.\", " 
    	+"deal_pos.ID_DIVISA, deal_pos.MONTO \"Monto\", deal.FECHA_LIQUIDACION \"Fecha Liq.\", "
    	+"deal_det.clave_forma_liquidacion \"Producto\", " 
    	+"DECODE(deal_det.status_detalle_deal, 'TT', 'Liquidada', 'CO', 'Procesandose', 'Cancelada') \"Status\", "
    	+"decode(act.NOMBRE_ACTIVIDAD, "
    	+"'DInt Documento', 'Interbancario', "
    	+"'DN Rec. M. Ctrl.', 'Rechazado Promoci\u00F3n', "
    	+"'DInt Rec. M. Ctrl.', 'Rechazado Interbancario', "
    	+"'Promoci\u00F3n') \"Origen Deal\", "
    	+"act.resultado \"Resultado\", usuario.clave_usuario \"Autorizado Por\", act.fecha_terminacion "
    	+"FROM SC_DEAL deal, SC_DEAL_DETALLE deal_det, SC_DEAL_POSICION deal_pos, BUP_CUENTA_CONTRATO cta_cont, "
    	+"BUP_PERSONA_RIM pers_rim, BUP_PERSONA cte, BUP_SECTOR_ECONOMICO sec_eco, BUP_PERSONA_FISICA prom, "
    	+"SC_MESA_CAMBIO mesa, SC_ACTIVIDAD act, SEGU_USUARIO usuario, SEGU_USUARIO usuarioPromotor, SEGU_PERFIL perfil, "
    	+"SEGU_ASOCIACION_FACULTAD asoc, SEGU_FACULTAD fac "
    	+"WHERE deal.ID_DEAL = deal_det.ID_DEAL "
    	+"AND deal_det.ID_DEAL_POSICION = deal_pos.ID_DEAL_POSICION "
    	+"AND deal.NO_CUENTA = cta_cont.NO_CUENTA (+) "
    	+"AND cta_cont.RIM_NO = pers_rim.RIM_NO "
    	+"AND pers_rim.ID_PERSONA = cte.ID_PERSONA "
    	+"AND cte.ID_SECTOR = sec_eco.ID_SECTOR (+) "
    	+"AND deal.id_usuario = usuarioPromotor.id_usuario "
    	+"AND usuarioPromotor.id_usuario = perfil.id_usuario (+) "
    	+"AND asoc.id_col = perfil.id_perfil "
    	+"AND asoc.facultad = fac.nombre "
    	+"AND perfil.sistema = 'SICA' "
    	+"AND fac.nombre not in ('SICA_CAN_EST','SICA_CAN_TDL','SICA_CAN_TPS') "
    	+"AND deal.FECHA_CAPTURA between ?  and ? "
    	+"AND deal_det.STATUS_DETALLE_DEAL != 'CA' "
    	+"AND deal.ID_PROMOTOR = prom.ID_PERSONA (+) "
    	+"AND mesa.ID_MESA_CAMBIO = deal_pos.id_mesa_cambio "
    	+"AND deal.id_deal = act.id_deal "
    	+"AND act.status != 'Cancelado' "
    	+"AND act.id_usuario = usuario.id_usuario "
    	+"AND act.nombre_actividad in (";
    
    //'DN Documento', 'DInt Documento', 'DN Rec. M. Ctrl.', 'DInt Rec. M. Ctrl.'
    
    /**
     * Constante para la condicion de la consulta de autorizaciones de mesa de control 
     */
    private static String QUERY_AMC_CONDICION_STATUS = "AND act.resultado = ? ";
    
    /**
     * Constante para la condicion de la consulta de autorizaciones de mesa de control 
     */
    private static String QUERY_AMC_CONDICION_CANAL = "AND  fac.descripcion = ? ";
    
    /**
     * Constante para el orden de la consulta de autorizaciones de mesa de control 
     */
    private static String QUERY_AMC_ORDEN = "order by deal_pos.RECIBIMOS, deal.ID_DEAL";

    /**
     * Constante para la consulta de autorizaciones de las utilidades del promotor 
     */
    private static String QUERY_UTILIDADES_PROMOTOR = "SELECT p.id_persona, p.nombre_corto \"PROMOTOR\", "
    	+"ca.NOMBRE Canal, "
    	+"DECODE(dp.recibimos, 'S', 'Compras', 'Ventas') \"OPERACION\", "
    	+"DECODE(dp.recibimos, 'S', SUM((dd.tipo_cambio_mesa-dp.tipo_cambio)*dp.monto), SUM((dp.tipo_cambio-dd.tipo_cambio_mesa)*dp.monto)) \"UTILIDAD\" "
    	+"FROM SC_DEAL d, SC_DEAL_DETALLE dd, SC_DEAL_POSICION dp, BUP_PERSONA p, SC_CANAL ca "
    	+"WHERE d.id_deal = dd.id_deal "
    	+"AND d.id_canal = ca.id_canal "
    	+"AND dd.id_deal_posicion = dp.id_deal_posicion "
    	+"AND d.status_deal != 'CA' "
    	+"AND dp.id_divisa != 'MXN' "
    	+"AND dd.status_detalle_deal != 'CA' "
    	+"AND d.FECHA_CAPTURA BETWEEN ? AND ? "
    	+"AND d.id_promotor = p.id_persona " 
    	+"AND ID_PROMOTOR IN ( "
    	+"  SELECT f.id_persona "
    	+"  FROM SEGU_PERFIL D, SEGU_USUARIO E, BUP_PERSONA F, SEGU_ASOCIACION_FACULTAD G, SEGU_FACULTAD H "
    	+"  WHERE D.ID_USUARIO = E.ID_USUARIO AND E.ID_PERSONA = F.ID_PERSONA AND G.FACULTAD = H.NOMBRE "
    	+"  AND G.ID_COL = D.ID_PERFIL AND D.ID_PERFIL IN ( "
    	+"      SELECT ID_PERFIL FROM SEGU_PERFIL WHERE SISTEMA = 'SICA' "
    	+"  )) "
    	+"AND ca.tipo_calculo = 'DU' ";
    
    /**
     * Constante para la union a la consulta de autorizaciones de las utilidades del promotor 
     */
    private static String QUERY_UTILIDADES_PROMOTOR_2 = "SELECT p.id_persona, trim(p.nombre_corto) PROMOTOR, "
    	+"ca.NOMBRE Canal, "
    	+"DECODE(dp.recibimos, 'S', 'Compras', 'Ventas') \"OPERACION\", "
    	+"DECODE(dp.recibimos, 'S', SUM((dd.tipo_cambio_mesa-dp.tipo_cambio)*dp.monto), SUM((dp.tipo_cambio-dd.tipo_cambio_mesa)*dp.monto)) \"UTILIDAD\" "
    	+"FROM SC_DEAL d, SC_DEAL_DETALLE dd, SC_DEAL_POSICION dp, BUP_PERSONA p, SC_CANAL ca, SEGU_USUARIO su "
    	+"WHERE d.id_deal = dd.id_deal "
    	+"AND d.id_canal = ca.id_canal "
    	+"AND dd.id_deal_posicion = dp.id_deal_posicion "
    	+"AND d.status_deal != 'CA' "
    	+"AND dp.id_divisa != 'MXN' "
    	+"AND dd.status_detalle_deal != 'CA' "
    	+"AND d.FECHA_CAPTURA BETWEEN ? AND ? "
    	+"AND d.id_usuario = su.id_usuario "
    	+"AND su.id_persona = p.id_persona "
    	+"AND ID_PROMOTOR IN ( "
    	+"  SELECT f.id_persona "
    	+"  FROM SEGU_PERFIL D, SEGU_USUARIO E, BUP_PERSONA F, SEGU_ASOCIACION_FACULTAD G, SEGU_FACULTAD H "
    	+"  WHERE D.ID_USUARIO = E.ID_USUARIO AND E.ID_PERSONA = F.ID_PERSONA AND G.FACULTAD = H.NOMBRE "
    	+"  AND G.ID_COL = D.ID_PERFIL AND D.ID_PERFIL IN ( "
    	+"      SELECT ID_PERFIL FROM SEGU_PERFIL WHERE SISTEMA = 'SICA' "
    	+"  )) "
    	+"AND ca.tipo_calculo = 'CA' ";
    
    /**
     * Constante para la condicion de la consulta de autorizaciones de las utilidades del promotor 
     */
    private static String QUERY_UP_CONDICION_PROMOTOR = "AND p.id_persona = ? ";
    
    /**
     * Constante para la condicion de la consulta de autorizaciones de las utilidades del promotor 
     */
    private static String QUERY_UP_CONDICION_OPERACION = "AND dp.recibimos = ? ";
    
    /**
     * Constante para la condicion de la consulta de autorizaciones de las utilidades del promotor 
     */
    private static String QUERY_UP_CONDICION_CANAL = "AND d.id_canal = ? ";
    
    /**
     * Constante para el agrupamiento de la consulta de autorizaciones de las utilidades del promotor 
     */
    private static String QUERY_UP_GROUP_BY = "GROUP BY  p.id_persona, p.nombre_corto, dp.recibimos, ca.nombre ";
    
    /**
     * Constante para el orden de la consulta de autorizaciones de las utilidades del promotor 
     */
    private static String QUERY_UP_ORDER_BY = "ORDER BY p.nombre_corto";
    
    /**
     * Constante para la invocacion del SP para Documentacion Faltante 
     */
    private static String COMMAND_DOCUMENTACION_FALTANTE = "{call SC_REP_DOC_FALTANTE(?,?)}";

}
