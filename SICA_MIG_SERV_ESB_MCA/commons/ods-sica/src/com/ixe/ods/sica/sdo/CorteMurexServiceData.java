package com.ixe.ods.sica.sdo;

import java.util.Date;
import java.util.List;

import net.sf.hibernate.HibernateException;

import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.model.Corte;
import com.ixe.ods.sica.model.CorteDetalle;
import com.ixe.ods.sica.model.DealReinicioPosicion;
import com.ixe.ods.sica.model.Divisa;
import com.ixe.ods.sica.model.EstatusCorte;
import com.ixe.ods.sica.model.PosicionLog;
import com.ixe.ods.sica.model.BitacoraCorte;


/**
 * Interface que define
 * 	los servicios de Acceso a Datos para 
 *  los cortes de posición que envia SICA a Murex
 *
 * @author Diego Pazarán 
 * 					Banorte
 * @version $Revision: 1.1.2.1.2.2.12.2 $
 */
public interface CorteMurexServiceData {
    /** Estatus Vista Previa * */
    public static final String VISTA_PREVIA = "VP";

    /** Estatus Enviado a Murex* */
    public static final String ENVIADO_MUREX = "EM";

    /** Estatus Error de Transferencia * */
    public static final String ERROR_TRANSFERENCIA = "ET";

    /** Estatus Procesado Murex * */
    public static final String PROCESADO_MUREX = "PM";
    
    /** Estatus ERROR EN GENERACIÓN DE ALGÚN DEAL INTERBANCARIO * */
    public static final String ERROR_DEALINTERBANCARIO = "ED";
    
    /** Estatus ERROR EN GENERACIÓN DE ALGÚN DEAL INTERBANCARIO * */
    public static final String CORTE_PROCESADO = "OK";

    /** Valor por default para Type del corte * */
    public static final String DET_CORTE_TYPE_DEFAULT = "fxspotfwd";

    /** Valor por default para Action del corte* */
    public static final String DET_CORTE_ACTION_DEFAULT = "insert";

    /** Valor por default para Source Module */
    public static final String DET_CORTE_SOURCE_MODULE_DEFAULT = "SICA";

    /** Valor por default para ourName * */
    public static final String DET_CORTE_OURNAME_DEFAULT = "BANORTE";

    /** Valor por default para theirName* */
    public static final String DET_CORTE_THEIRNAME_DEFAULT = "SICA";

    /** Valor por default para ourPortFolio * */
    public static final String DET_CORTE_OURPORTFOLIO_DEFAULT = "FX_SICA";

    /** Valor por default para user * */
    public static final String DET_CORTE_USER_DEFAULT = "MUREXFO";

    /** Valor por default para group * */
    public static final String DET_CORTE_GROUP_DEFAULT = "FO BANORTE";

    /** Valor por default para acountingSectionSource * */
    public static final String DET_CORTE_ACOUNTINGSECTIONSOURCE_DEFAULT = "TRAD";

    /** Valor por default para backToBackPotfolioName * */
    public static final String DET_CORTE_BACKTOBACKPORTFOLIONAME_DEFAULT = "FX_USD_I";
    
    /** Constante para indicar compra en el campo buySell * */
    public static final String DET_CORTE_BUY = "buy";

    /** Constante para indicar venta en el campo buySell * */
    public static final String DET_CORTE_SELL = "sell";

    
    /**
     * Regresa un objeto Corte
     * 	 a partir del dentificador del corte.
     *
     * @param idCorte <code>int</code>
     * 		con el identificador del corte.
     *
     * @return <code>Corte</Code> con la informacion
     * 	del corte solicitado, <code>null</code> en 
     * 	caso de que el corte solicitado no exista. 
     */
    public Corte findCorteById(int idCorte);

    /**
     *  Regresa un registro del catálogod e Estatusd e Corte
     * 	a partir de un identificador de corte, el cual
     * 	puede ser:
     *    
     *    <li>VP</li>	
	 *    <li>EM</li>	
	 *    <li>ET</li>	
	 *    <li>PM</li>	
     *
     * @param estatusCorte el identificador de 
     * 	estatus del corte que se solicita
     *
     * @return <code>EstatusCorte</code> con la informacion
     * 		del catalogo de estatus de corte.
     */
    public EstatusCorte findEstatusCorteByEstatus(String estatusCorte);

    /**
     * Inserta un registro de corte para el día
     * 	de hoy con estatus de VP y registra el evento
     * 	en la bitácora de corte.
     *
     * @param usuario <code>String</code> con el 
     * 	usuario que inserta el corte.
     *
     * @return un objeto <code>Corte</corte> con 
     * 	la información del corte registrado.
     */
    public Corte insertarCorteMurex(String usuario);

    /**
     * Actualiza el estatus de un corte 
     * en particular y registra el evento en
     * la bitácora de corte. 
     * 
     *
     * @param idCorte <code>int</code> con el identificador
     * 			del coerte a actualizar.
     * @param usuario <code>String</code> con el usuario
     * 		que actualiza el corte.
     * @param estatusCorte <code>String</code>
     * 	 con el estatus al que se desa actualizar el 
     * 	 corte, el cual puede ser:
     * 
     *    <li>VP</li>	
	 *    <li>EM</li>	
	 *    <li>ET</li>	
	 *    <li>PM</li>
     * 		
     * @param comentarios <code>String</code> con los
     * 	comentarios de la actualización del estatus
     * 	del corte.
     * 	
     *
     * @return <code>Corte</code> con la información 
     * del registro actualizado. 
     */
    public Corte actualizarEstatusCorte(int idCorte, String usuario, String estatusCorte,
        String comentarios);

    /**
     * En caso de existir regresa
     * 		el corte del día.
     *
     * @return <code>Corte</corte> con la informacion
     * 		del corte del día.
     */
    public Corte findCorteByFechaHoy();
    
    /**
     * En caso de existir regresa
     * 		el último BitacoraCorte del día.
     *
     * @return <code>BitacoraCorte</corte> con la informacion
     * 		de la ultima bitacora corte del día.
     */
    public BitacoraCorte findMaxBitacoraCorteByIdCorte(Corte corte);
    
    
    /**
     * En caso de existir regresa
     * 		el corte del día.
     *
     * @return <code>Corte</corte> con la informacion
     * 		del corte del día.
     */
    public String getFechaHoy();
    /**
     * En caso de existir regresa
     * 		el numero de cortes del día.
     *
     * @return <code>numero de Cortes del dia</corte>
     * 		
     */
    public Integer findNumCorteByFechaHoy();
    
    public List findDatosMonitor() throws SicaException;
    

    /**
     * Registra el detalle de un corte en
     * 		particular.
     * 
     * En caso de que los campos que manejan valor
     * 	constante sean <code>null</code> se les asignan
     * 	los valores definidos por default.
     *
     * @param corteDetalle con la información
     * 		del detalle a registrar.
     * @param idCorte <code>int</code> con el identificador
     * 	del corte al que se asocia el detalle a registrar.
     *
     * @return <code>int</code> con el identificador
     * 	del detalle de cote registrado.
     */
    public int registrarDetalleCorte(CorteDetalle corteDetalle, int idCorte);

    /**
     * Recupera los detalles de un corte de posicion
     * 	en particular.
     *
     * @param idCorte <code>int</code> con el 
     * 		identificador del corte.
     *
     * @return <code>List</code> de objetos <code>CorteDetalle</code>
     * 	con los cortes encontrados 
     */
    public List findDetallesCortebyIdCorte(int idCorte);

    /**
     * Permite eliminar los detalles de un corte
     * @param usuario
     * @param idCorte
     * @return
     */
	public boolean eliminarCorteDetalle(int idCorte);
	
	/**
	 * Inserta los registros que serviran para
	 * crear los DEALS Interbancarios
	 * @param dealReinicioPosicion registro a insertar
	 * @return Integer.
	 */
	public Integer insertarDealReinicioPosicion(DealReinicioPosicion dealReinicioPosicion);

	/**
	 * Elimina los DEALS Interbancarios que hacen el reinicio de la posicion.
	 * @param idCorte
	 */
	public boolean eliminarDealsReinicioPosicion(int idCorte);

	
	public List findDealsReinicioPosicionByIdCorte(int idCorte);

	public DealReinicioPosicion actualizarReinicioPosicion(String usuario, int resultDeal, String idDivisa,
			String operacion);  
	/**
	 * Obtiene Corte Detalle por fecha y divisa.
	 * @param fecha
	 * @param divisa
	 * @return
	 */
	public List getCorteDetalleByFechaAndDivisa(Date fecha, Divisa divisa);
	
	/**
	 * Obtiene Corte Detalle por fecha de captura
	 * @param fecha
	 * @param divisa
	 * @return
	 */
	public List getCorteDetalleByFecha(Date fecha);
	
	public void insertLogCorte(PosicionLog posicionLog);
	
	public List realizaSumatoriaCV(String string);
	
	public List findMaxIdPosicionLog();
	
	public List getDistintasDivisas();	
	
	public List getDivisas(List divisas);
	
	public void invocarSP();
	
	public void setIdCortePorEnvioMX();
	
	public List findDealReiniciobyfechaAndDivisa(Date fecha,Divisa divisa);
	
	public List findDealReiniciobyfecha(Date fecha);
}
