package com.ixe.ods.sica.dao;

import java.sql.Date;
import java.util.List;

/**
 * Interfaz para el dao de reportes ARP
 * 
 * @author Pedro M. Espinosa (espinosapm)
 * @version $Revision: 1.1.34.2.10.1.18.1.14.3.6.1 $ $Date: 2015/09/28 18:37:23 $
 */
public interface ReportesDao {
	
	/**
	 * Consulta el CR_GENERICO almacenado en SC_PARAMETRO
	 * @return String CR_GENERICO
	 */
	public String findCrGenerico();
	
	/**
	 * Consulta los deals y sus detalles para generar el reporte 'Clientes Banorte en SICA'. 
	 * Los deals deben tener cr asignado. 25/03/2015 11:48 am
	 * @param fechaInicial 
	 * @param fechaFinal
	 * @return List
	 */
	List findCrClientesBanorteEnSica(Date fechaInicial, Date fechaFinal);
	
	/**
	 * Encuentra las polizas contables y el nombre del cliente a quien pertenece
	 * entre el rango de fechas seleccionado.
	 * 
	 * @param diaInicial Dia desde el que se quieren obtener las polizas.
	 * @param diaFinal Dia hasta el que se quieren obtener las polizas.
	 * @return List.
	 */
	List findPolizasContables(Date diaInicial, Date diaFinal);
	
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
     * @param sucursal 
     * @param zona
     * @param contratoSica
     * @return List.
     */
    List findReporteOperacionesComprasVentas(
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
    );
    
    /**
     * 
     * @param fecha
     * @return List
     */
    List findReporteOperacionesDiarias(Date desde, Date hasta);
    
    /**
     * 
     * @param fecha
     * @return List
     */
    List findReporteOperacionesDiariasIxedir(Date desde, Date hasta);
    
    /**
     * 
     * @param fechaProceso
     * @return List
     */
    List findAllClientes(Date fechaProceso);
    
    /**
     * 
     * @param fechaProceso
     * @return List
     */
    List findAllContratos(Date fechaProceso);
    
    /**
     * 
     * @param fechaProceso
     * @return List
     */
    List findAllTransacciones(Date fechaProceso);
    
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
    List findReporteAutorizacionesMesaControl(String idCanal, String status, String tipoAutorizacion,
    							Date desde, Date hasta);

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
    List findReporteUtilidadPromotres(Integer promotor, Date desde, Date hasta, String operacion,
    		String idCanal);

    /**
     * Realiza la b&uacute;squeda para generar el reporte de documentacib&oacute;n pendiente
     * 
     * @author PEspinosa
     * @param nombreCorto - El nombre corto del cliente
     * @param noContrato - El nb&uacute;mero de contrato SICA
     * 
     * @return List
     */
    List findReporteDocumentacionFaltante(String nombreCorto, String noContrato);
}
