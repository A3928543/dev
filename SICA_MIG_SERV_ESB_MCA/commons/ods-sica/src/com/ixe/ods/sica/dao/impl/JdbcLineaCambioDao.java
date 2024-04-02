/*
 * $Id: JdbcLineaCambioDao.java,v 1.3.18.1.50.2 2016/07/13 23:58:47 mejiar Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2009 - 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica.dao.impl;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.object.MappingSqlQuery;

import com.ixe.ods.sica.Constantes;
import com.ixe.ods.sica.dao.LineaCambioDao;
import com.ixe.ods.sica.dto.ExcesoLineaCambioDto;
import com.ixe.ods.sica.sdo.PizarronServiceData;
import com.ixe.ods.sica.utils.DateUtils;

/**
 * Implementaci&oacute;n JDBC de la interfaz LineaCambioDao.
 *
 * @author Jean C. Favila (jfavila)
 * @version $Revision: 1.3.18.1.50.2 $ $Date: 2016/07/13 23:58:47 $
 * @see com.ixe.ods.sica.dao.LineaCambioDao
 */
public class JdbcLineaCambioDao extends JdbcDaoSupport implements LineaCambioDao {

    /**
     * Constructor por default.
     */
    public JdbcLineaCambioDao() {
        super();
    }

    /**
     * Recalcula los usos de todas las l&iacute;neas de cambio de acuerdo a los deals con que
     * est&aacute;n relacionados y que no est&acute; liquidados.
     *
     * @return List de String que describe el n&uacute;mero de registros que fueron actualizados.
     */
    public List cuadrarLineasCambio() {
        List resultados = new ArrayList();
        String[] fechasValor = new String[] {Constantes.CASH, Constantes.TOM, Constantes.SPOT,
                Constantes.HR72, Constantes.VFUT};
        int[] dias = new int[]{0, getDiasDiferencia(pizarronServiceData.getFechaTOM()),
                getDiasDiferencia(pizarronServiceData.getFechaSPOT()),
                getDiasDiferencia(pizarronServiceData.getFecha72HR()),
                getDiasDiferencia(pizarronServiceData.getFechaVFUT())};

        // Se limpian los usos de todas las lineas:
        resultados.add("L\u00edneas reinicializadas: " +
                getJdbcTemplate().update(QUERY_LIMPIAR_USOS));

        // Se recalculan los usos para cada fecha valor:
        for (int i = 0; i < fechasValor.length; i++) {
        	String fechaValor = fechasValor[i];

        	if (Constantes.VFUT.equals(fechaValor)) {
        		fechaValor = "96HR";
        	}
            String queryUsoNormal = QUERY_USO_NORMAL.replaceAll("\\@",
                    Constantes.CASH.equals(fechasValor[i]) ? "<=" : "=");

            queryUsoNormal = queryUsoNormal.replaceAll("\\|", fechaValor);
            resultados.add(fechasValor[i] + ": " + getJdbcTemplate().update(queryUsoNormal,
                    new Object[] {new Integer(dias[i]), new Integer(dias[i])}));
            String queryUsoRem = QUERY_USO_REM.replaceAll("\\@",
                    Constantes.CASH.equals(fechasValor[i]) ? "<=" : "=");
            queryUsoRem = queryUsoRem.replaceAll("\\|", fechaValor);
            resultados.add(fechasValor[i] + " REM: " + getJdbcTemplate().update(queryUsoRem,
                    new Object[] {new Integer(dias[i]), new Integer(dias[i])}));
        }
        return resultados;
    }

    /**
     * @see LineaCambioDao#getDiasAdeudoParaLineaCambio(int).
     * @param idLineaCambio El n&uacute;mero de la l&iacute;nea de cambios a revisar.
     * @return int.
     */
    public int getDiasAdeudoParaLineaCambio(int idLineaCambio) {
        List resultados = new FechaMinimaAdeudoQuery().execute(
                new Object[]{new Integer(idLineaCambio)});
        if (resultados.isEmpty()) {
            return 0;
        }
        else if (resultados.get(0) == null) {
            return 0;
        }
        else {
            return (int) ((new Date().getTime() - ((Date) resultados.get(0)).getTime()) /
                    1000 / 3600 / 24);
        }
    }

    /**
     * Regresa el n&uacute;mero de d&iacute;as que hay entre la fecha actual y la fecha
     * especificada.
     *
     * @param fecha La fecha a evaluar.
     * @return int.
     */
    private int getDiasDiferencia(Date fecha) {
        GregorianCalendar fechaActual = new GregorianCalendar();
        fechaActual.setTime(DateUtils.inicioDia());
        fecha = DateUtils.inicioDia(fecha);
        int i = 0;
        while (fechaActual.getTime().getTime() < fecha.getTime()) {
            fechaActual.add(Calendar.DAY_OF_MONTH, 1);
            i++;
        }
        return i;
    }

    /**
     * Regresa el valor de pizarronServiceData.
     *
     * @return PizarronServiceData.
     */
    public PizarronServiceData getPizarronServiceData() {
        return pizarronServiceData;
    }

    /**
     * Establece el valor de pizarronServiceData.
     *
     * @param pizarronServiceData El valor a asignar.
     */
    public void setPizarronServiceData(PizarronServiceData pizarronServiceData) {
        this.pizarronServiceData = pizarronServiceData;
    }

    /**
     * Subclase de MappingSqlQuery para el query QUERY_DEALS_PENDIENTES.
     */
    class FechaMinimaAdeudoQuery extends MappingSqlQuery {

        /**
         * Constructor por default.
         */
        public FechaMinimaAdeudoQuery() {
            super(getDataSource(), QUERY_DEALS_PENDIENTES);
            super.declareParameter(new SqlParameter("idLineaCambio", Types.INTEGER));
            compile();
        }

        /**
         * Regresa la fecha m&iacute;nima de liquidaci&oacute;n.
         *
         * @param resultSet El Result Set.
         * @param i Ignorado.
         * @return Date.
         * @throws SQLException Si ocurre alg&uacute;n problema.
         */
        protected Object mapRow(ResultSet resultSet, int i) throws SQLException {
            Date fechaMinima = resultSet.getDate(1);
            return fechaMinima != null ? new Date(fechaMinima.getTime()) : null;
        }
    }

    public List consultarDatosReporteExceso(Integer idJerarquia, String facultadCanal, Date fechaInicial, Date fechaFinal)
    {
    	List registros = null, listaTmp;
    	List idsPromotores;
    	String query;
    	StringBuffer parametrosPromotores;
    	RegistroExcesoMapper mapper;
    	int contadorElementos = 0;
    	int finLista = 0;
    	
    	if(facultadCanal != null && !"Default".equals(facultadCanal))
    	{
    		idsPromotores = getJdbcTemplate().queryForList(QUERY_PROMOTORES_CANAL, new Object[]{idJerarquia,facultadCanal}, Integer.class);
    		if(idsPromotores != null && idsPromotores.size() > 0)
    		{
    			finLista = idsPromotores.size() - 1;
    			parametrosPromotores = new StringBuffer();
    			registros = new ArrayList();
    			for(int indice = 0; indice < idsPromotores.size(); indice++)
    			{
    				if(contadorElementos < 500)
    				{
    					if(parametrosPromotores.length() > 0)
        					parametrosPromotores.append(", " + ((Integer)idsPromotores.get(indice)).intValue());
        				else
        					parametrosPromotores.append(((Integer)idsPromotores.get(indice)).intValue());
					}	
    				
    				if(indice == finLista || contadorElementos == 500)
    				{
    					query = QUERY_REPORTE_EXCESOS + 
    					        AND_REPORTE_EXCESOS.replaceAll("idsPromotoresCanal", 
    					            		                   parametrosPromotores.toString()) +
    					        ORDER_BY_REPORTE_EXCESOS;
    					mapper = new RegistroExcesoMapper(getDataSource(), query);
    					listaTmp = mapper.execute(new Object[]{fechaInicial, fechaFinal});
    					
    					if(listaTmp != null && !listaTmp.isEmpty())
    						registros.addAll(listaTmp);
    					
    					parametrosPromotores = new StringBuffer();
    					contadorElementos = 0;
    				}
    			}
    		}
    		else
    		{
    			query = QUERY_REPORTE_EXCESOS + ORDER_BY_REPORTE_EXCESOS;
    			mapper = new RegistroExcesoMapper(getDataSource(), query);
    			registros = mapper.execute(new Object[]{fechaInicial, fechaFinal});
    		}
    	}
    	else
		{
			query = QUERY_REPORTE_EXCESOS + ORDER_BY_REPORTE_EXCESOS;
			mapper = new RegistroExcesoMapper(getDataSource(), query);
			registros = mapper.execute(new Object[]{fechaInicial, fechaFinal});
		}
    	
    	return registros;
    }
    
    class RegistroExcesoMapper extends MappingSqlQuery {
    	public RegistroExcesoMapper(DataSource ds, String finalQuery) {
    		super(ds,finalQuery);

    		super.declareParameter(new SqlParameter("lcl.fecha_operacion", Types.DATE));
    		super.declareParameter(new SqlParameter("lcl.fecha_operacion", Types.DATE));

    		compile();
    	}
    	
		protected Object mapRow(ResultSet rs, int rownum) throws SQLException {
			ExcesoLineaCambioDto dto = new ExcesoLineaCambioDto();
			
			dto.setIdLineaCambio(new Integer(rs.getInt("idLineaCambio")));
			dto.setIdLineaCambioLog(new Integer(rs.getInt("idLineaCambioLog")));
			dto.setFechaOperacionExceso(rs.getDate("fechaExceso"));
			dto.setImporteLineaAutorizada(rs.getBigDecimal("importeLineaAutorizada"));
			dto.setTipoExceso(rs.getString("tipoExceso"));
			dto.setImporteExceso(rs.getBigDecimal("importeExceso"));
			dto.setIdPersonaCliente(new Integer(rs.getInt("idPersonaCliente")));
			dto.setNombreCliente(rs.getString("nombreCliente"));
			dto.setIdPersonaPromotor(new Integer(rs.getInt("idPersonaPromotor")));
			dto.setNombrePromotor(rs.getString("nombrePromotor"));
			
			return dto;
		}
    	
    }

    public int consultarUsuarioAreaRiesgos(Integer idUsuario, String sistema)
    {
    	return getJdbcTemplate().queryForInt(QUERY_USUARIO_RIESGOS, new Object[]{idUsuario,sistema,sistema});
    }
    
    /**
     * Referencia al bean pizarronServiceData.
     */
    private PizarronServiceData pizarronServiceData;

    /**
     * Query para poner todos los usos de las l&iacute;neas en cero.
     */
    private static final String QUERY_LIMPIAR_USOS = "UPDATE sc_linea_cambio SET uso_cash = 0, " +
            "uso_tom = 0, uso_spot = 0, uso_72hr = 0, uso_96hr = 0";

    /**
     * Query para actualizar el uso global de las l&iacute;neas de cambio.
     */
    private static final String QUERY_USO_NORMAL = "update sica_admin.sc_LINEA_CAMBIO t10 " +
            "SET USO_| = (select sum(importe) " +
            "  from sc_deal_detalle t1, sc_linea_cambio_log t2, sc_deal t3, sc_deal_posicion t4" +
            ", tes_matriz t5 " +
            "  where t1.id_deal_posicion = t2.id_deal_detalle " +
            "  and t1.id_deal = t3.id_deal " +
            "  and t1.mnemonico = t5.mnemonico " +
            "  and t1.id_deal_posicion = t4.id_deal_posicion " +
            "  and eventos_deal like '%U%' " +
            "  and status_detalle_deal in ('CO', 'AL') " +
            "  and t1.reversado < 2" +
            "  and recibimos = 'S' " +
            "  and tipo_oper = 'U' " +
            "  and trunc(t3.fecha_liquidacion) @ trunc(sysdate + ?) " +
            "  and t2.id_linea_cambio = t10.id_linea_cambio) " +
            "WHERE ID_LINEA_CAMBIO = ( " +
            "  select distinct(id_linea_cambio) " +
            "  from sc_deal_detalle t1, sc_linea_cambio_log t2, sc_deal t3, sc_deal_posicion t4" +
            ", tes_matriz t5 " +
            "  where t1.id_deal_posicion = t2.id_deal_detalle " +
            "  and t1.id_deal = t3.id_deal " +
            "  and t1.mnemonico = t5.mnemonico " +
            "  and t1.id_deal_posicion = t4.id_deal_posicion " +
            "  and eventos_deal like '%U%' " +
            "  and status_detalle_deal in ('CO', 'AL') " +
            "  and t1.reversado < 2" +
            "  and recibimos = 'S' " +
            "  and tipo_oper = 'U' " +
            "  and trunc(t3.fecha_liquidacion) @ trunc(sysdate + ?) " +
            "  and t2.id_linea_cambio = t10.id_linea_cambio)";
    
    /**
     * Query para actualizar el uso global de las l&iacute;neas de cambio.
     */
    private static final String QUERY_USO_REM = "update sica_admin.sc_LINEA_CAMBIO t10 " +
            "SET USO_REM_| = ( " +
            "  select sum(importe) " +
            "  from sc_deal_detalle t1, sc_linea_cambio_log t2, sc_deal t3, sc_deal_posicion t4" +
            ", tes_matriz t5 " +
            "  where t1.id_deal_posicion = t2.id_deal_detalle " +
            "  and t1.id_deal = t3.id_deal " +
            "  and t1.mnemonico = t5.mnemonico " +
            "  and t1.id_deal_posicion = t4.id_deal_posicion " +
            "  and eventos_deal like '%U%' " +
            "  and status_detalle_deal in ('CO', 'AL') " +
            "  and t1.reversado < 2" +
            "  and recibimos = 'S' " +
            "  and tipo_oper = 'U' " +
            "  and usa_linea_credito = 2 " +
            "  and trunc(t3.fecha_liquidacion) @ trunc(sysdate + ?) " +
            "  and t2.id_linea_cambio = t10.id_linea_cambio) " +
            "WHERE ID_LINEA_CAMBIO = ( " +
            "  select distinct(id_linea_cambio) " +
            "  from sc_deal_detalle t1, sc_linea_cambio_log t2, sc_deal t3, sc_deal_posicion t4" +
            ", tes_matriz t5 " +
            "  where t1.id_deal_posicion = t2.id_deal_detalle " +
            "  and t1.id_deal = t3.id_deal " +
            "  and t1.mnemonico = t5.mnemonico " +
            "  and t1.id_deal_posicion = t4.id_deal_posicion " +
            "  and eventos_deal like '%U%' " +
            "  and status_detalle_deal in ('CO', 'AL') " +
            "  and t1.reversado < 2" +
            "  and recibimos = 'S' " +
            "  and tipo_oper = 'U' " +
            "  and usa_linea_credito = 2 " +
            "  and trunc(t3.fecha_liquidacion) @ trunc(sysdate + ?) " +
            "  and t2.id_linea_cambio = t10.id_linea_cambio)";

    /**
     * Query que regresa la fecha de liquidaci&oacute;n m&aacute;s antigua de los deals con adeudo
     * que corresponden a una l&iacute;nea de cambios en particular.
     */
    private static final String QUERY_DEALS_PENDIENTES = "SELECT min(fecha_liquidacion)  " +
            "FROM sc_deal_detalle dd, sc_deal_posicion dp, sc_deal d, sc_linea_cambio_log lcl " +
            "WHERE dd.id_deal_posicion = lcl.id_deal_detalle " +
            "AND dd.id_deal_posicion = dp.id_deal_posicion " +
            "AND dd.id_deal = d.id_deal " +
            "AND dd.status_detalle_deal in ('CO', 'AL') " +
            "AND dp.recibimos = 'S' " +
            "AND lcl.tipo_oper = 'U' " +
            "AND trunc(d.fecha_liquidacion) <= trunc(sysdate) " +
            "AND id_linea_cambio = ?";
    
    // ******************************************* Reporte Excesos Linea Cambio ****************************
    private static final String QUERY_USUARIO_RIESGOS = "select count(r.id_rol) " +
    		                                            "from segu_perfil p, segu_perfil_rol pr, segu_rol r " +
    													"where p.id_usuario = ? and p.sistema = ? " +
    													"  and p.id_perfil = pr.id_perfil " +
    													"  and pr.id_rol = r.id_rol " + 
    													"  and r.sistema = ? " +
    													"  and r.nombre in ('DIRECCIONRIESGO','ADMINRIESGO','USUARIORIESGO')";
    
    private static final String QUERY_PROMOTORES_CANAL =
    	"select distinct j.id_persona " +  
        "  from SEGU_NODO_JERARQUIA j " +
        "left join bup_persona_fisica bpf on j.id_persona = bpf.id_persona " +
        "left join BUP_EMPLEADO_IXE bei on j.id_persona = bei.id_persona " +
        "left join SEGU_USUARIO su on bei.clave_usuario = su.clave_usuario " +
        "left join SEGU_PERFIL sp on su.id_usuario = sp.id_usuario " +
        "left join SEGU_PERFIL_ROL spr on sp.id_perfil = spr.id_perfil " +
        "left join SEGU_ASOCIACION_FACULTAD saf on sp.id_perfil = saf.id_col or saf.id_col = spr.id_rol " +
        "where j.id_jerarquia = ? " +
        "  and sp.sistema = 'SICA' " + 
        "  and saf.facultad = ?"; // 'SICA_CAN_BPT'
    
    private static final String QUERY_REPORTE_EXCESOS = 
    	"select lc.id_linea_cambio idLineaCambio, " +
    	"       lcl.id_linea_cambio_log idLineaCambioLog, " +
    	"       lcl.fecha_operacion fechaExceso, " +
        "      (lc.importe_fecha_valor + lc.importe_paytf) importeLineaAutorizada, " +
        "       lcl.observaciones tipoExceso, " +
       //--DECODE(lcl.observaciones,'Exceso FV', lc.importe_fecha_valor , lc.importe_paytf)  importeLinea,
        "       lcl.importe importeExceso, " +
        "       lc.id_corporativo idPersonaCliente, " +
        "       bp.nombre_corto nombreCliente, " +
        "       bce.id_persona idPersonaPromotor, " +
        "       bpfp.nombre || ' ' || bpfp.paterno || ' ' || bpfp.materno nombrePromotor " +
        "from   sc_linea_cambio_log lcl " +
        "left join sc_linea_cambio lc on lcl.id_linea_cambio = lc.id_linea_cambio " +
        "left join bup_persona_cuenta_rol bpcr on lc.id_corporativo = bpcr.id_persona " +
        "left join bup_cuenta_contrato bcc on bpcr.no_cuenta = bcc.no_cuenta " +
        "left join bup_cuenta_ejecutivo bce on bpcr.no_cuenta = bce.no_cuenta " +
        "left join bup_persona bp on lc.id_corporativo = bp.id_persona " +
        "left join bup_persona_fisica bpfp on bce.id_persona = bpfp.id_persona " +
        "where (lcl.fecha_operacion >= ? and lcl.fecha_operacion < ?) " +
        "  and lcl.tipo_oper = 'E' " +
        "  and lc.status_linea = 'OK' " +
        "  and bpcr.id_rol = 'TIT' and bpcr.status = 'VIG' " +
        "  and bcc.status_origen = 'Active' and bcc.status = 'ACTIVA' and bcc.id_tipo_cuenta = 'CAM10' " +
        "  and bce.status = 'VIG' ";
    
    private static final String AND_REPORTE_EXCESOS = " and bce.id_persona in (idsPromotoresCanal) ";
    private static final String ORDER_BY_REPORTE_EXCESOS = " order by lcl.id_linea_cambio, lcl.observaciones, lcl.fecha_operacion";
    //******************************************************************************************************
}
