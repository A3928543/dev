/*
 * $Id: SicaServiceData.java,v 1.66.2.3.6.5.6.4.4.4.2.2.6.1.6.3.2.1.2.2.2.1.6.1.2.1.8.1.2.7.6.2 2020/12/03 21:59:07 mejiar Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 - 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica.sdo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.ixe.contratacion.ContratacionException;
import com.ixe.ods.bup.model.ContratoSica;
import com.ixe.ods.bup.model.CuentaEjecutivo;
import com.ixe.ods.bup.model.Direccion;
import com.ixe.ods.bup.model.EjecutivoOrigen;
import com.ixe.ods.bup.model.EmpleadoIxe;
import com.ixe.ods.bup.model.Pais;
import com.ixe.ods.bup.model.Persona;
import com.ixe.ods.bup.model.PersonaFisica;
import com.ixe.ods.bup.model.PersonaMoral;
import com.ixe.ods.seguridad.model.IUsuario;
import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.dto.InfoCuentaAltamiraDto;
import com.ixe.ods.sica.dto.OperacionTceDto;
import com.ixe.ods.sica.model.Actividad;
import com.ixe.ods.sica.model.BupMunicipio;
import com.ixe.ods.sica.model.Canal;
import com.ixe.ods.sica.model.CodigoPostalListaBlanca;
import com.ixe.ods.sica.model.CombinacionDivisa;
import com.ixe.ods.sica.model.ComplementoDatos;
import com.ixe.ods.sica.model.Contrato;
import com.ixe.ods.sica.model.CuentaAltamira;
import com.ixe.ods.sica.model.Deal;
import com.ixe.ods.sica.model.DealDetalle;
import com.ixe.ods.sica.model.Divisa;
import com.ixe.ods.sica.model.Estado;
import com.ixe.ods.sica.model.FactorDivisaActual;
import com.ixe.ods.sica.model.GrupoTrabajo;
import com.ixe.ods.sica.model.GrupoTrabajoPromotor;
import com.ixe.ods.sica.model.IPlantilla;
import com.ixe.ods.sica.model.InfoFactura;
import com.ixe.ods.sica.model.LimiteEfectivo;
import com.ixe.ods.sica.model.LimitesRestriccionOperacion;
import com.ixe.ods.sica.model.LineaOperacion;
import com.ixe.ods.sica.model.MaximoDeal;
import com.ixe.ods.sica.model.MensajeTce;
import com.ixe.ods.sica.model.MesaCambio;
import com.ixe.ods.sica.model.MunicipioListaBlanca;
import com.ixe.ods.sica.model.NitPersona;
import com.ixe.ods.sica.model.ParametroSica;
import com.ixe.ods.sica.model.PersonaListaBlanca;
import com.ixe.ods.sica.model.PlantillaPantalla;
import com.ixe.ods.sica.model.Posicion;
import com.ixe.ods.sica.model.PrecioReferenciaActual;
import com.ixe.ods.sica.model.RecoUtilidad;
import com.ixe.ods.sica.model.Spread;
import com.ixe.ods.sica.model.SpreadActual;
import com.ixe.ods.sica.model.Swap;
import com.ixe.ods.sica.model.TipoPizarron;
import com.ixe.ods.sica.model.TokenTeller;
import com.ixe.ods.sica.model.TraspasoMesa;
import com.ixe.ods.sica.sdo.dto.TipoOperacionDealDto;

/**
 * Interfaz de Servicios del SICA.
 * 
 * @author Jean C. Favila, Javier Cordova (jcordova)
 * @version $Revision: 1.66.2.3.6.5.6.4.4.4.2.2.6.1.6.3.2.1.2.2.2.1.6.1.2.1.8.1.2.7.6.2 $ $Date: 2020/12/03 21:59:07 $
 */
public interface SicaServiceData extends ServiceData {

	/*
	 * Para quitar del cache hibernate de segundo nivel el objeto especificado.
	 */
	void flushEvict(Object object);

	/**
	 * Encuentra la plantilla con el n&uacute;mero proporcionado y la marca como
	 * activa. En caso de que el cliente se trate de una contraparte (deals
	 * interbancarios), se buscan todas sus plantillas del mismo tipo y se
	 * suspenden con el fin de que s&oacute;lo haya una plantilla activa de cada
	 * tipo para esa contraparte.
	 * 
	 * @param idPlantilla
	 *            El n&uacute;mero de plantilla.
	 */
	void activarPlantilla(int idPlantilla);

	void actualizarMontoOperadoSucursal(String idCanal, String idDivisa,
			String claveFormaLiquidacion, boolean recibimos, BigDecimal monto)
			throws SicaException;

	/**
	 * Revisa si el deal interbancario est&aacute; balanceado o no. En caso de
	 * que no, localiza alg&aacute;n detalle en status no cancelado que no haya
	 * llegado al SITE, y que corresponda a la divisa de referencia del deal
	 * (Pesos o D&oacute;lares). Si lo encuentra, modifica el monto de este
	 * detalle para balancear el deal.
	 * 
	 * @param deal
	 *            El deal a revisar.
	 */
	void balancearDealInterbancario(Deal deal);

	/**
	 * Crea un nuevo registro de PosicionLog con la informaci&oacute;n del
	 * detalle del deal y lo inserta en la base de datos. El detalle del deal es
	 * marcado con Afectaci&oacute;n de Posici&oacute;n.
	 * 
	 * @param det
	 *            El detalle del deal.
	 * @throws SicaException
	 *             Si no se puede calcular la fecha valor a afectar.
	 */
	void afectarPosicion(DealDetalle det) throws SicaException;

	/**
	 * Obtiene el estado actual del sistema. Si su hora fin es nula y la hora
	 * actual es mayor a la hora fin, activa el siguiente estado del sistema.
	 * 
	 * @see #findEstadoSistemaActual().
	 */
	void cambiarEstadosSistema();
	
	/**
	 * Cambia el estado de bloqueo del contrato
	 * 
	 * @param _contratoSica
	 * @param _idBloqueoNuevo
	 */
	void cambiarEstadoContrato(String _contratoSica, int _idBloqueoNuevo,int usuarioUltMod);
	

	/**
	 * Crea y almacena un Deal Interbancario
	 * 
	 * @param deal
	 *            El Deal
	 * @param compra
	 *            Define si la operaci&oacute;n es Compra o Venta
	 * @param fechaLiquidacion
	 *            Fecha de Liquidaci&oacute;n del Deal
	 * @param tipoCambio
	 *            Tipo de cambio de la operaci&oacute;n
	 * @param claveFormaLiquidacion
	 *            Forma de Liquidaci&oacute;n del Deal
	 * @param monto
	 *            Monto de la operaci&oacute;n
	 * @param divisa
	 *            Divisa en la que se llev&oacute; acabo la operaci&oacute;n
	 * @param mesaCambio
	 *            Mesa que llev&oacute; acabo la operaci&oacute;n
	 * @param usuario
	 *            Usuario que realiz&oacute; la operaci&oacute;n
	 * @param esComplementoSwap
	 *            Define si es completo o no el swap
	 * @return List.
	 * @throws SicaException
	 *             Si algo sale mal.
	 */
	Deal crearDealInterbancario(Deal deal, boolean compra,
			Date fechaLiquidacion, double tipoCambio,
			String claveFormaLiquidacion, double monto, Divisa divisa,
			MesaCambio mesaCambio, IUsuario usuario, boolean esComplementoSwap)
			throws SicaException;

	/**
	 * Crea un objeto Actividad que servir&aacute; como notificaci&oacute;n para
	 * los l&iacute;mites de compra y venta de efectivo, inicializando el nombre
	 * del proceso y de la actividad, adem&aacute;s del deal al que se refiere.
	 * 
	 * @param proceso
	 *            El nombre del proceso Actividad.
	 * @param nombre
	 *            El nombre de la actividad.
	 * @param deal
	 *            La referencia al deal.
	 * @param comentario
	 *            El comentario de la actividad.
	 * @return Actividad.
	 */
	Actividad crearNotificacion(String proceso, String nombre, Deal deal,
			String comentario);

	/**
	 * Crea y almacena un Deal Interbancario.
	 * 
	 * @param deal
	 *            El Deal
	 * @param compra
	 *            Define si la operaci&oacute;n es Compra o Venta
	 * @param fechaLiquidacion
	 *            Fecha de Liquidaci&oacute;n del Deal
	 * @param tipoCambio
	 *            Tipo de cambio de la operaci&oacute;n
	 * @param claveFormaLiquidacion
	 *            Forma de Liquidaci&oacute;n del Deal
	 * @param monto
	 *            Monto de la operaci&oacute;n
	 * @param divisa
	 *            Divisa en la que se llev&oacute; acabo la operaci&oacute;n
	 * @param mesaCambio
	 *            Mesa que llev&oacute; acabo la operaci&oacute;n
	 * @param usuario
	 *            Usuario que realiz&oacute; la operaci&oacute;n
	 * @param esComplementoSwap
	 *            Define si es completo o no el swap
	 * @param esCobertura
	 *            Define si es o no cobertura
	 * @param montoUsdParaComplCob
	 *            Monto
	 * @param tipoCambioCob
	 *            Tipo de cambio para el monto
	 * @param esNeteo
	 *            Define si es un deal interbancario de neteo o no.
	 * @param montoNeteo
	 *            El monto para el detalle complemento del deal de neteo.
	 * @param tipoCambioNeteo
	 *            El tipo de cambio para el detalle complemento del deal de
	 *            neteo.
	 * @param claveFormaLiqNeteo
	 *            El producto para el detalle complemento del deal de neteo.
	 * @param ticket Ticket
	 * @return Deal.
	 * @throws SicaException
	 *             Si algo sale mal.
	 */
	Deal crearDealInterbancario(Deal deal, boolean compra,
			Date fechaLiquidacion, double tipoCambio,
			String claveFormaLiquidacion, double monto, Divisa divisa,
			MesaCambio mesaCambio, IUsuario usuario, boolean esComplementoSwap,
			boolean esCobertura, double montoUsdParaComplCob,
			double tipoCambioCob, boolean esNeteo, double montoNeteo,
			double tipoCambioNeteo, String claveFormaLiqNeteo, String ticket)
			throws SicaException;

	void desAsignarCuentaEjecutivo(CuentaEjecutivo cuentaEjecutivo);

	/**
	 * Modifica el montoAsignado del swap, disminuy&eacute;ndolo con respecto al
	 * monto del deal. Cambia el status del swap de acuerdo al montoAsignado
	 * final.
	 * 
	 * @param deal
	 *            El deal a afectar.
	 */
	void disminuirDeSwapDeal(Deal deal);

	/**
	 * Genera las Utilidades para el Estado del Sistema Vespertino.
	 * 
	 * @param ticket
	 *            El ticket de la sesi&oacute;n del usuario.
	 * @param precios
	 *            El identificador del Usuario que gener&oacute; las Utilidades.
	 * @throws SicaException
	 *             Si ocurre alg&uacute;n error.
	 */
	void generarUtilidadEstadoVespertino(String ticket, Map precios, String ip, IUsuario usuario)
			throws SicaException;

	/**
	 * Regresa la lista de todos los Brokers.
	 * 
	 * @return List Los Brokers.
	 */
	List findAllBrokers();

	/**
	 * Encuentra los deals que bloquean el precio vespertino
	 * 
	 * @return List.
	 * @throws SicaException
	 *             excepcion en caso de no encontrar registros
	 */
	List findDealsBlockerVespertino() throws SicaException;

	/**
	 * Encuentra los deals pendientes del dia de un promotor.
	 * 
	 * @param idUsuario
	 *            El usuario que los captur&oacute;o.
	 * @return List.
	 * @throws SicaException
	 *             excepcion en caso de no encontrar registros
	 */
	List findDealsPendientesDia(int idUsuario) throws SicaException;

	/**
	 * Regresa la lista de brokers que son una instituci&oacute;n financiera.
	 * 
	 * @return List.
	 */
	List findAllBrokersInstFin();

	/**
	 * Regresa la lista de todos los canales, inicializando su relaci&oacute;n
	 * con su mesa de cambios.
	 * 
	 * @return List.
	 */
	List findAllCanales();

	/**
	 * Regresa la lista de todos los canales, inicializando la relaci&oacute;n
	 * con su mesa de cambios y poniendo al Canal de Promoci&oacute;n Mayoreo al
	 * principio.
	 * 
	 * @return List.
	 */
	List findAllCanalesByPromocionMayoreo();

	/**
	 * Encuentra los Bancos internacionales con base en su nombreBanco y el pais
	 * de origen.
	 * 
	 * @param nombreBanco
	 *            Nombre del Banco a buscar
	 * @param pais
	 *            Pais de orgen del banco
	 * @return List
	 */
	List findBancoInternacionalByNombreAndPais(String nombreBanco, String pais);

	/**
	 * Encuentra todos los Deals con base en su fecha de liquidaci&oacute;n
	 * hasta el d&iacute;a de hoy.
	 * 
	 * @param gc
	 *            Fecha de Hoy
	 * @param fechaLiquidacion
	 *            Fecha de Liquidaci&oacute;n del Detalle
	 * @return List
	 */
	List findAllDealsBySectorAndDate(Date gc, Date fechaLiquidacion);

	/**
	 * Encuentra todos los Detalles de Deal con base en su fecha de
	 * liquidaci&oacute;n hasta el d&iacute;a de hoy
	 * 
	 * @param gc
	 *            Fecha de hoy
	 * @param fechaLiquidacion
	 *            Fecha de Liquidaci&oacute;n del Detalle
	 * @return List
	 */
	List findAllDealDetallesBySectorAndDate(Date gc, Date fechaLiquidacion);

	/**
	 * Encuentra todas las Divisas ordenadas en orden alfabetico
	 * 
	 * @return List
	 */
	List findAllDivisasByOrdenAlfabetico();

	/**
	 * Encuentra todas las Divisas ordenadas en orden alfabetico
	 * 
	 * @return List
	 */
	List findAllEmpresasByOrdenAlfabetico();

	/**
	 * Encuentra un contraro por el id del cliente y el numero de cuenta
	 * 
	 * @param NO_CUENTA
	 * @return List
	 */
	List findContratosByCuenta(String NO_CUENTA);
	
	/**
	 * Encuentra un contraro por el id del cliente y el numero de cuenta
	 * 
	 * @param idContrato
	 * @return Contrato
	 */
	Contrato findContratoByCorto(Integer idContrato);

	/**
     * Encuentra un contraro por el n&uacute;mero de contrato SICA.
     *
     * @param noCuenta El n&uacute;mero de Contrato SICA.
     * @return Contrato.
     */
    Contrato findContratoByNoCuenta(String noCuenta);

	/**
	 * Encuentra todas las Lineas de Credito al Cierre.
	 * 
	 * @return List.
	 */
	List findAllLineaCreditoCierre();

	/**
	 * Regresa todos los paises ordenados alfab&eacute;ticamente por nombre.
	 * 
	 * @return List.
	 */
	List findAllPaises();

	/**
	 * Regresa todas las empresas ordenados alfab&eacute;ticamente por nombre.
	 * 
	 * @return List.
	 */
	List findAllEmpresas();// Hamue 2/8/12

	/**
	 * Retorna el pais identificado con idPais.
	 * 
	 * @param idPais
	 *            El identificador del pais.
	 * @return Un objeto Pais lleno.
	 */
	public Pais findBupPaisXId(String idPais);

	/**
	 * Encuentra todos los Promotores de la Jerarqu&iacute;a del SICA.
	 * 
	 * @param nombreSistema
	 *            El nombre de la aplicaci&oacute;n, para este caso SICA.
	 * @return List Los Promotores.
	 */
	List findAllPromotoresSICA(String nombreSistema);
	
	/**
	 * Encuentra todos los Promotores de la Jerarqu&iacute;a del SICA.
	 *
	 * @return List Descripciones de Bloqueos.
	 */
	List findAllMotivosBloqueo();

	/**
	 * Obtiene el promotor de sica dada su clave de usuario
	 * 
	 * @param claveUsuario
	 * @return
	 */
	EmpleadoIxe findPromotorSicaByClave(String claveUsuario);

	/**
	 * Regresa la lista de sectores econ&oacute;micos que pueden operar 72HR. Se
	 * utilizan los registros de Par&acute;metros SECTOR_72_X para
	 * despu&eacute;s extraer los sectores econ&oacute;micos correspondientes a
	 * estos par&aacute;metros.
	 * 
	 * @return List.
	 */
	List findAllSectoresUltimaFechaValor();

	/**
	 * Encuentra todas las sucursales almacenadas.
	 * 
	 * @return List
	 */
	List findAllSucursales();

	/**
	 * Encuentra todos los diferentes Tipos de Traspaso Producto.
	 * 
	 * @return List.
	 */
	List findAllTiposTraspasoProducto();

	/**
	 * Actualiza las observaciones del deal.
	 * 
	 * @param deal
	 * @return
	 */
	Deal actualizaObservacionesDeal(Deal deal);

	/**
	 * Obtiene los Beneficiarios Ligados a un Contrato Sica y un cierto Rol.
	 * 
	 * @param noCuenta
	 *            El n&uacute;mero de Contrato Sica.
	 * @param idRol
	 *            El Tipo Rol del Beneficiario Sica.
	 * @return List Los Beneficiarios.
	 */
	List findBeneficiariosByNoCuentaByIdRol(String noCuenta, String idRol);

	/**
	 * Obtienen el Beneficiario del id proporcionado, en caso de no existir
	 * lanza una excepci&oacute;n indicando que no existe
	 * 
	 * @param idBeneficiario
	 *            La clave del beneficiario.
	 * @return Persona.
	 */
	Persona findBeneficiarioById(String idBeneficiario);

	/**
	 * Encuentra el IUsuario por su idUsuario.
	 * 
	 * @param idUsuario
	 *            El idUsuario a buscar.
	 * @return IUsuario
	 */
	IUsuario findUsuarioById(Integer idUsuario);

	/**
	 * Encuentra un Broker por su idPersona.
	 * 
	 * @param idBroker
	 *            El identificador de la contraparte.
	 * @return List.
	 */
	List findBrokerByIdPersona(Integer idBroker);

	/**
	 * Encontra el Reconocimiento por Divisa y Fecha.
	 * 
	 * @param gcInicio
	 *            La fecha de inicio.
	 * @param gcFin
	 *            La fecha fin.
	 * @param idDivisa
	 *            La clave de la divisa.
	 * @param idMesaCambio
	 *            La clave de la mesa
	 * @return List.
	 */
	List findReconocimientoByDivisaAndDate(Date gcInicio, Date gcFin,
			String idDivisa, int idMesaCambio);

	/**
	 * Encuentra un Broker por su Razon Social.
	 * 
	 * @param razonSocial
	 *            La raz&oacute;n social a buscar.
	 * @return List.
	 */
	List findBrokersByRazonSocial(String razonSocial);

	/**
	 * Obtiene el ID del Canal al que pertenece una Facultad-Canal del SICA.
	 * 
	 * @param nombreFacultad
	 *            La Facultad.
	 * @return Canal El canal.
	 */
	Canal findCanalByFacultad(String nombreFacultad);

	/**
	 * Encuentra el Canal con base en idOriginal de Sucursal.
	 * 
	 * @param idSucursalOriginal
	 *            Id Original de la Sucursal
	 * @return Canal.
	 */
	Canal findCanalByIdSucursal(String idSucursalOriginal);

	/**
	 * Encuentra todos los canales que pertenecen a Sucursales.
	 * 
	 * @return List
	 */
	List findCanalSucursales();

	/**
	 * Regresa los canales operados seg&uacute;n el criterio de b&uacute;squeda.
	 * 
	 * @param desde
	 *            fecha Inicio
	 * @param hoy
	 *            fecha Fin
	 * @return <code>List</code> de canales.
	 */
	List findCanalesOperadosHoy(Date desde, Date hoy);

	/**
	 * Regresa el contratoSica que tiene el n&uacute;mero de cuenta
	 * especificado.
	 * 
	 * @param noCuenta
	 *            El n&uacute;mero de contrato a buscar.
	 * @return ContratoSica.
	 */
	ContratoSica findContrato(String noCuenta);

	/**
	 * Regresa el contratoSica que tiene el n&uacute;mero de cuenta
	 * especificado. Se inicializan las relaciones que existen hasta el sector
	 * econ&oacute;mico del cliente.
	 * 
	 * @param noCuenta
	 *            El n&uacute;mero de contrato a buscar.
	 * @return ContratoSica.
	 */
	ContratoSica findContratoSicaConSectorEconomico(String noCuenta);

	/**
	 * Regresa el contratoSica que pertenece a la persona con el idPersona
	 * especificado.
	 * 
	 * @param idPersona
	 *            El n&uacute;mero de la persona en la bup.
	 * @return ContratoSica.
	 */
	ContratoSica findContratoSicaByIdPersona(Integer idPersona);

	/**
	 * Obtiene de los Id's de los Sectores Economicos que definen cuales de
	 * estos pueden operar Valor Futuro para Deals Interbancarios (Swaps).
	 * 
	 * @return String[].
	 */
	String[] findIdsSectoresUltimaFechaValor();

	/**
	 * Encuentra todos los Contratos Sica registrados en la BUP est&eacute;n o
	 * no Asignados a un Promotor, de acuerdo a los criterios de b&uacute;squeda
	 * que se pasan por como par&aacute;metros.
	 * 
	 * @param razonSocial
	 *            Si se trata de una Persona Moral.
	 * @param nombre
	 *            Si se trata de una Persona F&iacute;sica;.
	 * @param paterno
	 *            Si se trata de una Persona F&iacute;sica;.
	 * @param materno
	 *            Si se trata de una Persona F&iacute;sica;.
	 * @param noCuenta
	 *            El N&uacute;mero de Contrato Sica.
	 * @return List Los Contratos Sica encontrados.
	 */
	List findContratosSica(String razonSocial, String nombre, String paterno,
			String materno, String noCuenta);

	/**
	 * Encuentra todos los Contratos Sica de Instituciones Financieras (Brokers)
	 * registrados en la BUP est&eacute;n o no Asignados a un Promotor, de
	 * acuerdo a los criterios de b&uacute;squeda que se pasan por como
	 * par&aacute;metros.
	 * 
	 * @param razonSocial
	 *            La Raz&oacute;n Social de la B&uacute;squeda.
	 * @return List Los Contratos Sica encontrados.
	 */
	List findContratosSicaBrokers(String razonSocial);

	/**
	 * Para el caso de una Persona Moral encuentra el idCorporativo. Si no es
	 * una Persona Moral regresa null. Si se trata de un Grupo Empresarial,
	 * regresa su idPersona, si pertenece a un Grupo Empresarial regresa el
	 * idCorporativo. De lo contrario regresa null.
	 * 
	 * @param idPersona
	 *            La Persona Moral a Verificar.
	 * @return PersonaMoral El Corporativo si este existe.
	 */
	PersonaMoral findCorporativo(Integer idPersona);

	/**
	 * Encuentra la Lista de Corporativos de un Grupo Empresarial por su
	 * idPersona Moral.
	 * 
	 * @param idPersona
	 *            El id de la persona.
	 * @return String[]
	 */
	String[] findCorporativosByPersonaMoral(Integer idPersona);

	/**
	 * Encuentra las Cuentas Eje de un Cliente. Filtra de PersonaCuentaRol las
	 * Cuentas cuyo idRol es: TIT, COT, TAH, BENT cuyo status es: VIG Filtra de
	 * CuentaContrato las Cuentas cuyo statusOrigen es: Active y cuyo status es:
	 * ACTIVA.
	 * 
	 * @param idPersona
	 *            El Cliente.
	 * @return String[] Las Cuentas Eje encontradas.
	 */
	String[] findCuentasEje(Integer idPersona);

	/**
	 * Encuentra las Cuentas Eje de un Cliente o bien si es del tipo de origen
	 * 'CCB' para saber si se trata de un Usuario o un Cliente. Filtra de
	 * PersonaCuentaRol las Cuentas cuyo idRol es: TIT, COT, TAH, BENT cuyo
	 * status es: VIG Filtra de CuentaContrato las Cuentas cuyo statusOrigen es:
	 * Active y cuyo status es: ACTIVA.
	 * 
	 * @param idPersona
	 *            El Cliente.
	 * @return boolean True si es Cliente, False si es Usuario.
	 */
	boolean isClienteOrUsuario(Integer idPersona);

	/**
	 * Encuentra los productos que est&aacute;n en restricci&oacute;n.
	 * 
	 * @return List
	 */
	List getProductosEnRestriccion();

	/**
	 * Valida si el monto del detalle sobrepasa la restricci&oacute;n de compra
	 * o venta de efectivo.
	 * 
	 * @param deal
	 *            El deal.
	 * @param det
	 *            El detalle del deal.
	 * @param claveFormaLiquidacion
	 *            La forma de liquidaci&oacute;n del detalle.
	 * @param limRestOper
	 *            La lista de los l&iacute;mites de restricci&oacute;n de
	 *            operaci&oacute;n.
	 * @param modificacion
	 *            Si es modificaci&oacute;n de monto o producto.
	 * @param nvoMonto
	 *            El nuevo monto si es que se trata de una modificaci&oacute;n
	 *            por monto.
	 * @param fechaLiquidacion
	 *            La fecha de liquidaci&oacute;n del deal.
	 * @param isCapturaRapidaNeteo
	 *            Indica si es de captura r&aacute;pida con neteo.
	 */
	void validaLimitesRestriccionOperacion(Deal deal, DealDetalle det,
			String claveFormaLiquidacion,
			LimitesRestriccionOperacion limRestOper, String modificacion,
			Double nvoMonto, Date fechaLiquidacion, boolean isCapturaRapidaNeteo);

	/**
	 * Obtiene la(s) CuentasEjecutivo para un numero de cuenta.
	 * 
	 * @param idTipoEjecutivo
	 *            El tipo de ejectivo para la cuenta.
	 * @param contratoSica
	 *            El numero de cuenta del contrato.
	 * @return List
	 */
	List findCuentaEjecutivoByContratoSica(String idTipoEjecutivo,
			String contratoSica);

	/**
	 * Obtiene todas las CuentaEjecutivo sin importar su estatus.
	 * 
	 * @param idTipoEjecutivo
	 *            El tipo de ejectivo para la cuenta.
	 * @param contratoSica
	 *            El numero de cuenta del contrato.
	 * @return List
	 */
	List findCuentaEjecutivoByContratoSicaExcStatus(String idTipoEjecutivo,
			String contratoSica);

	/**
	 * Obtiene la(s) CuentaEjecutivo por numero de cuenta, estatus y id persona
	 * del ejecutivo.
	 * 
	 * @param idTipoEjecutivo
	 *            El tipo de ejectivo para la cuenta.
	 * @param status
	 *            El estatus de la cuenta.
	 * @param contratoSica
	 *            El numero de cuenta del contrato.
	 * @param idPersona
	 *            El id peresona del promotor.
	 * @return List
	 */
	List findCuentaEjecutivoByContratoSicaAndStatus(String idTipoEjecutivo,
			String status, String contratoSica, Integer idPersona);

	/**
	 * Define si existe una CuentaEjecutivo para un numero de cuenta y un id
	 * persona.
	 * 
	 * @param idTipoEjecutivo
	 *            El tipo de ejecutivo para la cuenta.
	 * @param contratoSica
	 *            El numero de cuenta del contrato.
	 * @param idPersona
	 *            El id persona del promotor.
	 * @return boolean
	 */
	boolean findCuentaEjecutivoByContratoSicaAndIdEjecutivo(
			String idTipoEjecutivo, String contratoSica, Integer idPersona);

	/**
	 * Encuentra las Cuentas de un Promotor de acuerdo a su Jerarquia de
	 * Promocion.
	 * 
	 * @param idTipoEjecutivo
	 *            El Tipo Ejecutivo SICA.
	 * @param idPersona
	 *            El idPersona del Promotor.
	 * @return List Las Cuentas del Ejecutivo.
	 */
	List findCuentasEjecutivo(String idTipoEjecutivo, Integer idPersona);

	/**
	 * Encuentra todas las cuentas que no se encuentran asignadas.
	 * 
	 * @param razonSocial
	 *            Raz&oacute;n Social del cliente
	 * @param nombre
	 *            Nombre del cliente
	 * @param paterno
	 *            Appellido paterno del cliente
	 * @param materno
	 *            Appellido materno del cliente
	 * @param noCuenta
	 *            N&uacute;mero de cuenta del cliente
	 * @return List
	 */
	List findCuentasNoAsignadas(String razonSocial, String nombre,
			String paterno, String materno, String noCuenta);

	/**
	 * Encuentra la Clave Banxico de un Cliente por su idPersona.
	 * 
	 * @param idPersona
	 *            El id de la persona.
	 * @return String La clave Banxico del Cliente.
	 */
	String findCveBanxicoByIdPersona(Integer idPersona);

	/**
	 * Obtiene todas las mesas de la tabla.
	 * 
	 * @return List
	 */
	List findAllMesas();

	/**
	 * Encuentra el Objeto Mesa Cambio de acuerdo a su ID.
	 * 
	 * @param idMesaCambio
	 *            El ID del Objeto a Encontrar.
	 * @return MesaCambio El Objeto Encontrado.
	 */
	MesaCambio findMesaCambio(int idMesaCambio);

	/**
	 * Regresa el monto dolarizado que ha operado un contrato sica en la fecha
	 * de liquidaci&oacute;n en cheques de viajero.
	 * 
	 * @param noCuenta
	 *            El n&uacute;mero de contrato sica.
	 * @param fechaLiquidacion
	 *            La fecha de liquidaci&oacute;n del deal.
	 * @return double.
	 */
	double findMontoChequesViajeroUsdFechaLiquidacion(String noCuenta,
			Date fechaLiquidacion);

	/**
	 * Regresa el monto dolarizado al d&iacute;a o al mes que ha operado un
	 * contrato sica en la fecha de liquidaci&oacute;n tanto en efectivo como en
	 * cheques de viajero para la venta.
	 * 
	 * @param noCuenta
	 *            El n&uacute;mero de contrato sica.
	 * @param fechaLiquidacion
	 *            La fecha de liquidaci&oacute;n del deal.
	 * @param diario
	 *            El tipo de query, diario o mensual.
	 * @param divisa
	 *            La divisa a verificar.
	 * @param productosEnRestriccion
	 *            La lista de productos que est&aacute;n en restricci&oacute;n.
	 * @param recibimos
	 *            El tipo de query, recibimos o entregamos.
	 * @return double.
	 */
	double findAcumuladoUsdCompraVentaDiarioMensual(String noCuenta,
			Date fechaLiquidacion, boolean diario, String divisa,
			List productosEnRestriccion, boolean recibimos);

	/**
	 * Encuentra el Nombre Banxico de un Cliente por su idPersona.
	 * 
	 * @param idPersona
	 *            El Cliente.
	 * @return String el nombre Banxico del Cliente.
	 */
	String findNombreBanxicoByIdPersona(Integer idPersona);

	/**
	 * Encuentra el Detalle de Deal con base a su idDealPosicion
	 * 
	 * @param idDealPosicion
	 *            Id de la Posicion del Detalle
	 * @return DealDetalle.
	 * @throws SicaException
	 *             Si no existe el detalle con ese n&uacute;mero.
	 */
	DealDetalle findDealDetalle(int idDealPosicion) throws SicaException;

	/**
	 * Encuentra el Detalle de Deal con base a su idDealPosicion incializando
	 * sus relaciones con Divsa y FactorDivisa.
	 * 
	 * @param idDealPosicion
	 *            Id de la posici&oacute;n del detalle.
	 * @return DealDetalle.
	 * @throws SicaException
	 *             Si no existe el detalle con ese n&uacute;mero.
	 */
	DealDetalle findDealDetalleConDivisaFactorDivisa(int idDealPosicion)
			throws SicaException;

	/**
	 * Encuentra los Deals que no tengan status Cancelado
	 * 
	 * @return List
	 */
	List findDealsNoCancel();

	/**
	 * Encuentra los Deals que no tienen Contrato SICA y que no est&aacute;n
	 * Cancelados para ser Renovada su Fecha L&iacute;mite Captura Contrato.
	 * 
	 * @return Los Deals a Renovar.
	 */
	List findDealsNoCancelYSinContrato();

	/**
	 * @param fechaInicio
	 *            La fecha de inicio.
	 * @param fechaLiquidacion
	 *            La fecha de liquidaci&oacute;n.
	 * @return List.
	 */
	List findDealsPendientes(Date fechaInicio, Date fechaLiquidacion);

	/**
	 * Regresa dos detalles de los deals pendiente por liquidar a una fecha.
	 * 
	 * @param gc
	 *            La fecha.
	 * @param fechaLiquidacion
	 *            La fecha de liquidaci&oacute;n.
	 * @return List.
	 */
	List findDealsDetallesPendientes(final Date gc, final Date fechaLiquidacion);

	/**
	 * Encuentra los deals con estatus distintos a AL, TL y CO para el reporte
	 * de Deals Pendientes por Liquidar.
	 * 
	 * @param fechaInicioDia
	 *            La fecha de incio del d&iacute;a de la b&uacute;squeda.
	 * @param fechaFinDia
	 *            La fecha de fin del d&iacute;a de la b&uacute;squeda.
	 * @return List.
	 */
	List findDealsPendientesPorLiquidar(Date fechaInicioDia, Date fechaFinDia);

	/**
	 * @param fechaInicio
	 *            La fecha de inicio.
	 * @param fechaLiquidacion
	 *            La fecha de liquidaci&oacute;n.
	 * @return List.
	 */
	List findDealsPendientesTomSpot(Date fechaInicio, Date fechaLiquidacion);

	/**
	 * Encuentra las polizas escritas en el la fecha especificada.
	 * 
	 * @param fechaHoy
	 *            La fecha de inicio.
	 * @param fechaHoy2
	 *            La fecha fin.
	 * @return List.
	 */
	List findPolizaPorFecha(Date fechaHoy, Date fechaHoy2);

	/**
	 * Obtiene la Delegaci&oacute;n y Municipio con base en idDireccion.
	 * 
	 * @param idDireccion
	 *            Id de la Direcci&oacutern a encontrar
	 * @return Direccion.
	 */
	Direccion findDireccion(int idDireccion);

	/**
	 * Encuentra la divisa con base en un idDivisa
	 * 
	 * @param idDivisa
	 *            Id de la Divisa a encontrar
	 * @return Divisa
	 */
	Divisa findDivisa(String idDivisa);

	/**
	 * Encuentra la moneda (MXN:peso o USD:dolar) de una Cuenta Contrato.
	 * 
	 * @param noCuenta
	 *            La Cuenta Contrato.
	 * @return String La moneda.
	 * @throws SicaException
	 *             Si no se encuentra la cuenta.
	 */
	String findDivisaCuenta(String noCuenta) throws SicaException;
	
	/**
	 * Encuentra la descripcion del bloqueo que tenga el contrato
	 * 
	 * @param noCuenta
	 *            La Cuenta Contrato.
	 * @return String La descripcion del bloqueo.
	 * @throws SicaException
	 *             Si no se encuentra la descripcion.
	 */
	String obtenerDescripcionBloqueo(Integer idBloqueo) throws SicaException;

	/**
	 * Regresa true si la clave de la divisa del detalle es la Divisa Referencia
	 * de la Mesa del Deal al que pertenece el Detalle.
	 * 
	 * @param dealDetalle
	 *            El Detalle de Deal.
	 * @return boolean.
	 */
	boolean isDivisaReferencia(DealDetalle dealDetalle);

	/**
	 * Regresa los Deals que tienen los Status "C": CANCELADO, "T": TOTALMENTE
	 * LIQUIDADO y A: PROCESO CAPTURA.
	 * 
	 * @return List.
	 */
	List findDealsAlertas();

	/**
	 * Otiene los Detalles de Deal con base en la Fecha, Divisa y Forma de
	 * Liquidaci&oacute;n.
	 * 
	 * @param fechaCaptura
	 *            Fecha de captura del Deal
	 * @param gc
	 *            Fecha del d&iacute;a de hoy
	 * @param idDivisa
	 *            Id de la Divisa
	 * @param claveFormaLiquidacion
	 *            Fecha de liquidaci&oacute;n
	 * @return List
	 */
	List findDealsDetallesByDateAndDivisaAndFormaLiquidacion(Date fechaCaptura,
			Date gc, String idDivisa, String claveFormaLiquidacion);

	/**
	 * Obtiene los Detalles de Deal con base en la Fecha, Divisa, Forma de
	 * Liquidaci&oacute;n y Operaci&oacute;n (Compra o Venta)
	 * 
	 * @param fechaCaptura
	 *            Fecha de captura del Deal
	 * @param gc
	 *            Fecha del d&iacute;a de hoy
	 * @param idDivisa
	 *            Id de la Divisa
	 * @param claveFormaLiquidacion
	 *            Fecha de liquidaci&oacute;n
	 * @param operacion
	 *            Compra o Venta
	 * @return List.
	 */
	List findDealsDetallesByDateAndDivisaAndFormaLiquidacionAndOperacion(
			Date fechaCaptura, Date gc, String idDivisa,
			String claveFormaLiquidacion, String operacion);

	/**
	 * Otiene los Detalles de Deal con base en el Id Persona y una Fecha
	 * 
	 * @param de
	 *            Deal procesados desde esta fecha
	 * @param hasta
	 *            Deal procesados hasta esta fecha
	 * @param idPersona
	 *            Id Persona que proces&oacute; el Deal
	 * @return List
	 */
	List findDealsDetallesByIdPersonaAndFechas(Date de, Date hasta,
			Integer idPersona);

	/**
	 * Obtiene los Detalles de Deal que requieren de Pago Anticipado con base en
	 * la una Fecha
	 * 
	 * @param fechaCaptura
	 *            Fecha de Captura del Deal
	 * @param gc
	 *            Fecha del d&iacute;a de hoy
	 * @param orden
	 *            Orden del Deal
	 * @return List
	 */
	List findDealsDetallesPagoAnticipadoByDate(Date fechaCaptura, Date gc,
			String orden);

	/**
	 * Encuentra los Deals Pendientes por asignar Swap con base en el
	 * n&uacute;mero de cuenta.
	 * 
	 * @param noCuenta
	 *            N&uacute;mero de cuenta del Deal
	 * @return List
	 */
	List findDealsPendientesAsignarASwap(String noCuenta);

	/**
	 * Encuentra las Divisas que maneja cada Canal,.
	 * 
	 * @param idCanal
	 *            Id del Canal
	 * @return List
	 */
	List findDivisasByCanal(String idCanal);

	/**
	 * Encuentra las Divisas que corresponden a una Mesa por el Tipo de
	 * Pizarron.
	 * 
	 * @param idTipoPizarron
	 *            Id del Tipo de Pizarron de la Mesa
	 * @return List
	 */
	List findDivisasByTipoPizarron(int idTipoPizarron);

	/**
	 * Obtiene la lista de las divisas mas Frecuentes
	 * 
	 * @return List
	 */
	List findDivisasFrecuentes();

	/**
	 * Obtiene la lista de las divisas Frecuentes m&aacute;s Pesos Mexicanos.
	 * 
	 * @return List
	 */
	List findDivisasFrecuentesMasPesos();

	/**
	 * Otiene la lista de los Metales Amonedados
	 * 
	 * @return List
	 */
	List findDivisasMetales();

	/**
	 * Otiene el Factor Divisa de los Metales Amonedados
	 * 
	 * @return List
	 */
	List findDivisasMetalesFactor();

	/**
	 * Obtiene la lista de las divisas menos Frecuentes
	 * 
	 * @return List
	 */
	List findDivisasNoFrecuentes();

	/**
	 * Otiene el Factor Divisa de las divisas no frecuentes
	 * 
	 * @return List
	 */
	List findDivisasNoFrecuentesFactor();

	/**
	 * Encuentra el Estado Actual del Sistema.
	 * 
	 * @return Estado.
	 * @throws SicaException
	 *             Si no se encuentra el estado actual del sistema.
	 */
	Estado findEstadoSistemaActual() throws SicaException;

	/**
	 * Regresa todos los Estados del Sistema ordenados por Id.
	 * 
	 * @return List.
	 */
	List findAllEstadoOrdenado();

	/**
	 * Obtiene el valor del Factor Divisa dado su idFactorDivisa
	 * 
	 * @deprecated Se debe utlizar el valor directo del Factor Divisa.
	 * @param idFactorDivisa
	 *            Id del Factor que ser requiere
	 * @return FactorDivisa
	 */
	FactorDivisaActual findFactorDivisa(int idFactorDivisa);

	/**
	 * Otiene el valor del Factor Divisa actual con base en su idFactorDivisa
	 * 
	 * @param idFactorDivisa
	 *            Id del Factor DivisaAcutal
	 * @return FactorDivisaActual
	 */
	FactorDivisaActual findFactorDivisaActualByID(Integer idFactorDivisa);

	/**
	 * Obtiene el valor del Factor Divisa actual entre dos Divisas
	 * 
	 * @param fromDivisa
	 *            De la Divisa
	 * @param toDivisa
	 *            A la Divisa
	 * @return FactorDivisaActual
	 */
	FactorDivisaActual findFactorDivisaActualFromTo(String fromDivisa,
			String toDivisa);

	/**
	 * Obtiene una lista de los Factores de Divisa que son marcados como
	 * actuales.
	 * 
	 * @return List
	 */
	List findFactoresDivisaActuales();
	
	/**
	 * Obtiene el tipo de bloqueo que tiene el contrato
	 * query="FROM TipoBloqueo AS tb WHERE tb.idBloqueo=?"
	 * 
	 * @return List
	 */
	List findBloqueoByidBloqueo(Integer id_bloqueo);
	
	/**
	 * Obtiene los PosicionesLog del día
	 * 
	 * @return List
	 */
	List getPosicionesLogByFecha(String _fecha);

	
	/**
	 * Obtiene los PosicionesLog mayores a in idPosicionLog
	 * 
	 * @return List
	 */
	List getPosicionesLogMayoresA(Integer idPosicionLog);
	
	/**
	 * Encuentra las Facultades de los Canales del SICA.
	 * 
	 * @return List.
	 */
	List findFacultadesCanales();

	/**
	 * Regresa la lista de fechas inh&aacute;biles en Estados Unidos a partir de
	 * la fecha actual del sistema.
	 * 
	 * @return List de FechaInhabilEua.
	 */
	List findFechasInhabilesEua();

	/**
	 * Encuentra el Historial de una Linea de Operacion por su ID.
	 * 
	 * @param idLineaOperacion
	 *            El ID de la Linea de Operacion.
	 * @return List El Historial de la Linea de Operacion.
	 */
	List findHistorialLineaOperacionLogByIdLineaOperacion(
			Integer idLineaOperacion);

	/**
	 * Regresa el ID de la Jerarqu&iacute;a utilizada para el Tipo Ejecutivo del
	 * SICA.
	 * 
	 * @param idTipoEjecutivo
	 *            El Tipo Ejecutivo de la Jerarqu&iacute;a del SICA.
	 * @return int El ID de la Jerarqu&iacute;a.
	 */
	int findIdJerarquiaSICA(String idTipoEjecutivo);

	/**
	 * Encuentra el L&iacute;mite de Riesgo por Mesa de Cambio y Divisa.
	 * 
	 * @param idMesaCambio
	 *            El Id de la Mesa de Cambio
	 * @param idDivisa
	 *            El Id Divisa
	 * @return List El L&iacute;mite de Riesgo
	 */
	List findLimiteByMesaAndDivisa(int idMesaCambio, String idDivisa);

	/**
	 * Encuentra Las L&iacute;neas de Cr&eacute;dito de un Corporativo por
	 * Estatus.
	 * 
	 * @param idCorporativo
	 *            El Corporativo de la L&iacute;nea.
	 * @param status
	 *            El status seleccionado.
	 * @return List Las L&iacute;neas de Cr&eacute;dito.
	 */
	List findLineaCreditoByCorporativoAndStatusLinea(Integer idCorporativo,
			String status);

	/**
	 * Encuentra la Linea de Operacion de un Broker.
	 * 
	 * @param idBroker
	 *            El id del broker.
	 * @return List.
	 */
	LineaOperacion findLineaOperacionByBroker(int idBroker);

	/**
	 * Encuentra las Lineas de Credito por Status.
	 * 
	 * @param status
	 *            El Status seleccionado.
	 * @return List Las Lineas de Credito Encontradas.
	 */
	List findLineasDeCreditoByStatus(String status);

	/**
	 * Encuentra las Lineas de Operacion de una Lista de Brokers y por Status de
	 * la Linea.
	 * 
	 * @param brokers
	 *            La lista de brokers.
	 * @param status
	 *            El status a buscar.
	 * @return List.
	 */
	List findLineasOperacionByBrokersAndEstatus(List brokers, String status);

	/**
	 * Encuentra las Lineas de Operacion por Status.
	 * 
	 * @param status
	 *            El status a buscar.
	 * @return List.
	 */
	List findLineasOperacionByStatus(String status);

	/**
	 * Obtiene el Log de un Deal dado su idDeal.
	 * 
	 * @param idDeal
	 *            Id del Deal a encontar su log
	 * @return List
	 */
	List findLogByDeal(Integer idDeal);

	/**
	 * Obtiene el Log de un Detalle de Deal dado su idDeal.
	 * 
	 * @param idDeal
	 *            Id del Deal
	 * @return List
	 */
	List findLogByDetalle(Integer idDeal);

	/**
	 * Encuentra los Montos M&aacuteximo;s para un Deal por Mesa de Cambio.
	 * 
	 * @param idMesaCambio
	 *            El Id de la Mesa de Cambio.
	 * @return List los Montos M&aacuteximo;s
	 */
	List findMaximoDealByIdMesaCambio(int idMesaCambio);

	/**
	 * Encuentra los Montos M&aacuteximo;s para un Deal por Mesa de Cambio y
	 * Divisa.
	 * 
	 * @param idMesaCambio
	 *            El Id de la Mesa de Cambio.
	 * @param idDivisa
	 *            El Id de la Divisa.
	 * @return List los Montos M&aacuteximo;s
	 */
	MaximoDeal findMaximoDealByIdMesaCambioAndIdDivisa(int idMesaCambio,
			String idDivisa);

	/**
	 * Regresa la lista de medios de contacto para la persona especificada.
	 * 
	 * @param idPersona
	 *            El id de la persona a buscar.
	 * @return List.
	 */
	List findMedioContactoByIdPersona(Integer idPersona);

	List findAutMediosContactoPersona(int idPersona);

	/**
	 * Encuentra los Mnem&oacute;nicos a mostrar en una cierta Pantalla de
	 * Plantilla.
	 * 
	 * @param plantillaPantalla
	 *            La Pantalla de Plantilla.
	 * @return List Los Mnem&oacute;nicos.
	 */
	List findMnemonicosByPlantillaPantalla(String plantillaPantalla);

	/**
	 * Encuentra el Objeto Par&aacute;metro por si Identificador.
	 * 
	 * @param idParametro
	 *            El Identificador del Par&aacute;metro.
	 * @return ParametroSica.
	 */
	ParametroSica findParametro(String idParametro);

	/**
	 * Encuentra la Persona de un Contrato Sica.
	 * 
	 * @param noCuenta
	 *            El n&uacute;mero del Contrato SICA.
	 * @return Persona La Persona del Contrato SICA.
	 */
	Persona findPersonaByContratoSica(String noCuenta);

	/**
	 * Encuentra la Persona que tiene el rfc especificado.
	 * 
	 * @param rfc
	 *            El rfc a localizar.
	 * @return Persona.
	 */
	Persona findPersonaByRfc(String rfc);

	/**
	 * Encuentra la Personas beneficiarias de una Cuenta Ixe.
	 * 
	 * @param cuentaIxe
	 *            El n&uacute;mero de cuenta.
	 * 
	 * @return Lista de Personas que cumplan con las condiciones
	 */
	List findPersonasByCuentaEje(String cuentaIxe);
	
	/**
	 * Encuenta el identificador de persona de una cuenta
	 * Nativa de Altamira que ha sido espejeada.
	 * @param noCuentaAltamira el numero de cuenta formato Altamira
	 * @return <code>String</code> con el identificador de la persona.
	 */
	String findIdPersonaCuentaAltamiraEspejeada(String noCuentaAltamira);

	/**
	 * Encuentra la Persona Moral a la que le corresponde un cierto idPersona.
	 * 
	 * @param idPersona
	 *            El id de la Persona Moral a buscar.
	 * @return PersonaMoral.
	 */
	PersonaMoral findPersonaMoralByIdPersona(Integer idPersona);
	
	/**
	 * Encuentra la Persona Fisica a la que le corresponde un cierto idPersona
	 * @param idPersona
	 * @return PersonaFisica
	 */
	PersonaFisica findPersonaFisicaByIdPersona(Integer idPersona);

	/**
	 * Encuentra las Personas Morales que no son Brokers, por Razon Social.
	 * 
	 * @param razonSocial
	 *            La raz&oacute;n social a buscar.
	 * @return Lista de personas que cumplan con las condiciones.
	 */
	List findPersonaMoralNotBrokerByRazonSocial(String razonSocial);

	/**
	 * Encuentra la PlantillaPantalla que le corresponde a un mnen&oacute;nico.
	 * 
	 * @param mnemonico
	 *            El mnem&oacute;nico a buscar.
	 * @return PlantillaPantalla.
	 * @throws SicaException
	 *             Si no existe una plantillaPantalla para el mnen&oacute;nico
	 *             especificado.
	 */
	PlantillaPantalla findPlantillaPantallaByMnemonico(String mnemonico)
			throws SicaException;

	/**
	 * Manda a llamar
	 * <code>findPlantillas(String plantilla, String noCuenta)</code> para
	 * encontrar todas las Plantillas de un Tipo (Nacionales, Transferencias
	 * Nacionales, Internacionales, Internacionales Intermediarios) para un
	 * Contrato SICA.
	 * 
	 * Pero primero inicializa los Objetos Hibernate correspondientes.
	 * 
	 * @param plantilla
	 *            El tipo de Plantilla a buscar.
	 * @param contratoSica
	 *            El Contrato SICA.
	 * @return List Las Plantillas encontradas.
	 */
	List findPlantillas(String plantilla, ContratoSica contratoSica);

	/**
	 * Encuentra todas las Plantillas de un Tipo (Nacionales, Transferencias
	 * Nacionales, Internacionales, Internacionales Intermediarios) para un
	 * Contrato SICA.
	 * 
	 * @param plantilla
	 *            El tipo de Plantilla a buscar.
	 * @param noCuenta
	 *            El Contrato SICA.
	 * @return List Las Plantillas encontradas.
	 */
	List findPlantillas(String plantilla, String noCuenta);

	/**
	 * Encuentra todas las plantillas de un Tipo (Nacionales, Transeferencuas
	 * Nacionales, Internacionales, Internacionales Intermediarioas) para un
	 * contrato SICA.
	 * 
	 * @param plantilla
	 *            El tipo de Plantilla a buscar.
	 * @param noCuenta
	 *            El Contrato SICA.
	 * 
	 * @return List Las Plantillas encontradas.
	 */
	List findPlantillasWithContratoSicaAndBeneficiario(String plantilla,
			String noCuenta);

	/**
	 * Encuentra todas las Plantillas de un Tipo (Nacionales, Transferencias
	 * Nacionales, Internacionales, Internacionales Intermediarios) para un
	 * Contrato SICA.
	 * 
	 * @param plantilla
	 *            El tipo de Plantilla a buscar.
	 * @param noCuenta
	 *            El Contrato SICA.
	 * @param mnemonico
	 *            Mnemonico de la Plantilla
	 * @return List
	 */
	List findPlantillas(String plantilla, String noCuenta, String mnemonico);

	/**
	 * Encuentra las Plantillas para un Contrato y un Mnemonico
	 * 
	 * @param noCuentaContrato
	 *            N&uacute;mero de cuenta para el Contrato SICA
	 * @param mnemonicos
	 *            Mnemonico de la plantilla
	 * @return List
	 * @throws SicaException
	 *             Si no se encuentran plantillas con estas
	 *             caracter&iacute;sticas.
	 */
	List findPlantillasByContratoMnemonicos(String noCuentaContrato,
			List mnemonicos) throws SicaException;

	/**
	 * Regresa las plantillas del contrato, mnem&oacute;nico, clave de producto
	 * y divisa especificados.
	 * 
	 * @param ticket
	 *            El ticket de la sesi&oacute;n del usuario.
	 * @param noCuenta
	 *            El contrato sica.
	 * @param mnemonico
	 *            El mnem&oacute;nico (opcional, puede ser null).
	 * @param cveFormLiq
	 *            La clave de producto (opcional, puede ser null).
	 * @param idDivisa
	 *            La clave de la divisa.
	 * @param recibimos
	 *            True para recibimos (compra), False para entregamos (venta).
	 * @return List.
	 */
	List findPlantillasByContratoMnemonicos(String ticket, String noCuenta,
			String mnemonico, String cveFormLiq, String idDivisa,
			boolean recibimos);

	/**
	 * Regresa la plantilla en base al id especificado.
	 * 
	 * @param idPlantilla
	 *            id de la plantilla deseada.
	 * @return Plantilla.
	 */
	IPlantilla findPlantillaById(int idPlantilla);

	/**
	 * Encuentra las plantillas que se encuentran pendientes.
	 * 
	 * @return List.
	 */
	List findPlantillasPendientes();

	/**
	 * Encuentra la Posicion con base en el id de la Mesa de Cambio y la Divisa
	 * 
	 * @param idMesaCambio
	 *            Id de la Mesa de Cambio
	 * @param idDivisa
	 *            Id de la Divisa
	 * @return List.
	 */
	List findPosicionByIdMesaCambioAndIdDivisa(int idMesaCambio, String idDivisa);

	/**
	 * Encuentra el Hist&oacute;rico para la Posic&oacute;n con base en el Id de
	 * la Mesa, la Divisa y que se encuentre entre el rango de fechas definidas.
	 * 
	 * @param idMesaCambio
	 *            El id de la mesa.
	 * @param idDivisa
	 *            La divisa para la posici&oacute;n.
	 * @param incioDia
	 *            La fecha de inicio de la b&uacute;squeda.
	 * @param finDia
	 *            La fecha de fin de la b&uacute;squeda.
	 * @return List
	 */
	List findHistoricoPosicion(int idMesaCambio, String idDivisa,
			Date incioDia, Date finDia);

	/**
	 * Encuentra la Posicion con base en el id de la Mesa de Cambio
	 * 
	 * @param idMesaCambio
	 *            Id de la Mesa de Cambio
	 * @return List
	 */
	List findPosicionByIdMesaCambio(int idMesaCambio);

	/**
	 * Regresa todos los registros de la tabla de posici&oacute;n, que
	 * corresponden a divisas frecuentes.
	 * 
	 * @return List de objetos Posicion.
	 */
	List findPosicionDivisasFrecuentes();

	/**
	 * Encuentra el Promotor que tiene asignado un cierto Contrato SICA.
	 * 
	 * @param noCuenta
	 *            El Numero de Cuenta del Contrato SICA.
	 * @return EmpleadoIxe El Promotor.
	 * @throws SicaException
	 *             Si no hay promotor SICA asignado a este contrato.
	 */
	EmpleadoIxe findPromotorByContratoSica(String noCuenta)
			throws SicaException;

	/**
	 * Encuentra Promotores por su idJerarquia.
	 * 
	 * @param idEjecutivo
	 *            El id del ejecutivo.
	 * @return List Los Promotores.
	 */
	List findPromotoresSica(String idEjecutivo);

	/**
	 * Regresa la lista de Empleados que se encuentran registrados en el grupo
	 * de trabajo especificado.
	 * 
	 * @param idGrupoTrabajo
	 *            la clave del grupo de trabajo.
	 * @return List de EmpleadoIxe.
	 */
	List findPromotoresByGrupoTrabajo(String idGrupoTrabajo);

	/**
	 * Obtiene la propiedad dado su nombre
	 * 
	 * @param propiedad
	 *            Nombre de la propiedad
	 * @return String
	 */
	String findPropiedad(String propiedad);

	/**
	 * Encuentra el Objeto Spread Actual por su Identificador.
	 * 
	 * @param idSpread
	 *            El identificador
	 * @return SpreadActual.
	 */
	SpreadActual findSpreadActual(Integer idSpread);

	/**
	 * Encuentra los Spreads Actuales por Mesa-Canal-Divisa.
	 * 
	 * @param idMesaCambio
	 *            El identificador de la Mesa de Cambio.
	 * @param idCanal
	 *            El identificador del Canal.
	 * @param idDivisa
	 *            El identificador de la Divisa.
	 * @return List Los Spreads Actuales.
	 */
	List findSpreadsActualesByMesaCanalDivisa(int idMesaCambio, String idCanal,
			String idDivisa);

	/**
	 * Encuentra los Spreads Actuales por TipoPizarron - Divisa
	 * 
	 * @param tipoPizarron
	 *            El tipo de pizarron.
	 * @param idDivisa
	 *            El id de la divisa.
	 * @return List
	 */
	List findSpreadsActualesByTipoPizarronDivisa(TipoPizarron tipoPizarron,
			String idDivisa);

	/**
	 * Encuentra los Spreads Actuales por Mesa-Canal-Divisa-Forma
	 * Liquidaci&oacute;n.
	 * 
	 * @param idMesaCambio
	 *            El identificador de la Mesa de Cambio.
	 * @param idCanal
	 *            El identificador del Canal.
	 * @param idDivisa
	 *            El identificador de la Divisa.
	 * @param claveFormaLiquidacion
	 *            La Clave Forma Liquidaci&oacute;n.
	 * @return List Los Spreads Actuales.
	 */
	List findSpreadsActualesByMesaCanalDivisaFormaLiquidacion(int idMesaCambio,
			String idCanal, String idDivisa, String claveFormaLiquidacion);

	/**
	 * Ecuentra los spreads actuales por TipoPizarron-Divisa-Forma de
	 * Liquidacion.
	 * 
	 * @param idDivisa
	 *            El id de la divisa.
	 * @param claveFormaLiquidacion
	 *            La clave de la forma de liquidacion.
	 * @param tipoPizarron
	 *            El Tipo de Pizarron.
	 * @return List.
	 */
	List findSpreadsActualesByTipoPizarronDivisaFormaLiquidacion(
			String idDivisa, String claveFormaLiquidacion,
			TipoPizarron tipoPizarron);

	/**
	 * Encuentra los Spreads Actuales por Mesa-Sucursal-Divisa-Forma
	 * Liquidaci&oacute;n.
	 * 
	 * @param idMesaCambio
	 *            El Identificador de la Mesa de Cambio.
	 * @param idDivisa
	 *            El Identificador de la Divisa.
	 * @param claveFormaLiquidacion
	 *            La Clave Forma Liquidaci&oacute;n.
	 * @return List Los Spreads Actuales.
	 */
	List findSpreadsActualesByMesaSucursalDivisaFormaLiquidacion(
			int idMesaCambio, String idDivisa, String claveFormaLiquidacion);

	/**
	 * Encuentra el status de un Deal dado su idDeal
	 * 
	 * @param idDeal
	 *            Id del Deal a encontrar su estatus
	 * @return List
	 */
	List findStatusByDeal(Integer idDeal);

	/**
	 * Encuentra el estatus de un Detalle de Deal dado su idDeal al que
	 * pertence.
	 * 
	 * @param idDeal
	 *            Id del Deal
	 * @return List
	 */
	List findStatusByDetalle(Integer idDeal);

	/**
	 * Encuentra los SWAPS cuyo Status es "COMPLETAMENTE_ASIGNADO" y CANCELADO
	 * para procesarlos en el Cierre de D&iacute;a.
	 * 
	 * @return List Los Swaps.
	 */
	List findSwapCierreDelDia();

	/**
	 * Obtiene la lista de subsidiarios para una persona moral dado su idPersona
	 * 
	 * @param idPersona
	 *            Id de la Pesona Moral
	 * @return List
	 */
	List findSubsidiariosByPersonaMoral(Integer idPersona);

	/**
	 * Obtiene el TimeOut para un Deal
	 * 
	 * @return String
	 */
	String findTimeOut();

	/**
	 * Regresa la persona que es titular de la Cuenta Ixe especificada. Levanta
	 * una excepci&oacute;n si no la encuentra.
	 * 
	 * @param noCuentaIxe
	 *            El n&uacute;mero de cuenta de cheques.
	 * @return Persona.
	 */
	Persona findTitularCuentaEje(String noCuentaIxe);

	/**
	 * Regresa true si existe la cuenta, de otro modo lanza una excepci&oacute;n
	 * indicando que el n&uacute;mero de cuenta no existe.
	 * 
	 * @param noCuentaIxe
	 *            El n&uacute;mero de cuenta de cheques.
	 */
	void validaCuentaEje(String noCuentaIxe);

	/**
	 * Encuentra los Traspasos Producto por un rango de Fechas Operacion.
	 * 
	 * @param fechaOperIni
	 *            La fecha de inicio del rango.
	 * @param fechaOperFin
	 *            La fecha final del rango.
	 * @return List La Lista de Traspasos.
	 */
	List findTraspasosByFechaOper(Date fechaOperIni, Date fechaOperFin);

	/**
	 * Encuentra todos los Traspasos del Portafolios almacenados.
	 * 
	 * @return List Lista de todos los traspasos.
	 */
	List findAllTraspasosPortafolios();

	/**
	 * Encuentra los Traspasos del Portafolios del dia de hoy.
	 * 
	 * @param today
	 *            fecha del dia de hoy
	 * @return List Lista de los traspasos realizados del dia de hoy
	 */
	List findTraspasosPortafoliosDiaActual(Date today);

	/**
	 * Encuentra los Traspasos del Portafolios en un rango de fechas
	 * 
	 * @param fechaOperIni
	 *            fecha de inicio de la busqueda
	 * @param fechaOperFin
	 *            fecha del fin de la busqueda
	 * 
	 * @return List lista de los traspasos encontrados.
	 */
	List findTraspasosPortafoliosByFechaOperacion(Date fechaOperIni,
			Date fechaOperFin);

	/**
	 * Regresa la lista de registros de utilidades globales, ordenados por fecha
	 * y divisa.
	 * 
	 * @param fechaInicio
	 *            La fecha de inicio de la b&uacute;squeda.
	 * @param fechaFin
	 *            La fecha final de la b&uacute;squeda.
	 * @return List.
	 */
	List findUtilidadesGlobales(Date fechaInicio, Date fechaFin);

	/**
	 * Regresa la lista de registros del tipo de cambio de alguna divisa de
	 * acuerdo a un rango de fechas.
	 * 
	 * @param idDivisa
	 *            El id de la divisa para la b&uacute;squeda.
	 * @param fechaInicio
	 *            La fecha de inicio de la b&uacute;squeda.
	 * @param fechaFinal
	 *            La fecha final de la b&uacute;squeda.
	 * @return List.
	 */
	List findHistoricoTipoCambio(String idDivisa, Date fechaInicio,
			Date fechaFinal);

	/**
	 * Regresa el n&uacute;mero de cuenta ABA de BOFA de la tabla SC_PARAMETRO.
	 * 
	 * @return El ABA de BOFA.
	 */
	String getABABOFA();

	/**
	 * Obtiene la fecha actual del sistema
	 * 
	 * @return Date fechaActual del Sistema
	 */
	Date getFechaSistema();

	/**
	 * Regresa el valor del Par&aacute;metro FECHA_LIM_CAPT_CONT.
	 * 
	 * @return int.
	 */
	int getFechaLimiteCapturaDeal();

	/**
	 * Regresa el ID del Administrador del Sistema. Este se emplea para generar
	 * las alertas de respuesta de error de Banxico.
	 * 
	 * @return El Id del Administrador.
	 */
	Integer getIdAdministrador();

	/**
	 * Regresa los Par&aacute;metros necesarios para el c&aacutelculo del
	 * l&iacute;mite de desviaci&oacute;n.
	 * 
	 * @return Map.
	 */
	Map getParsLimDesv();

	/**
	 * Inserta el detalle de venta y le asigna la plantilla especificada. Si la
	 * plantilla no est&aacute;a activa, marca el detalle con solicitud de
	 * autorizaci&oacute;n por falta de documentaci&oacute;n.
	 * 
	 * @param deal
	 *            El encabezado del deal interbancario.
	 * @param importe
	 *            El importe para el detalle de venta.
	 * @param plantilla
	 *            La plantilla a asignar.
	 * @param cobertura
	 *            true para cobertura, false para no cobertura.
	 * @param tcCobertura
	 *            El tipo de cambio de la cobertura.
	 * @return DealDetalle.
	 */
	DealDetalle insertarEntregamosDealIntConPlantilla(Deal deal,
			double importe, IPlantilla plantilla, boolean cobertura,
			double tcCobertura);

	/**
	 * Da de alta una operacion de tipo de cambio especial
	 * 
	 * @param deal
	 *            Deal inicializado
	 * @param operacionTce
	 *            Datos de la operacion de tipo de cambio especial
	 * @param ticket
	 *            ticket de la sesion
	 * @param limitesRestriccionOperacion
	 * @param idCanal
	 * 
	 * @return Deal
	 */
	Deal insertarDealTce(Deal deal, OperacionTceDto operacionTce,
			String ticket, LimitesRestriccionOperacion limRestOper,
			String idCanal);

	/**
	 * Inserta un nuevo detalle al deal que representa la compra o venta de un
	 * producto en particular.
	 * 
	 * @param ticket
	 *            El ticket de la sesi&oacute;n del usuario.
	 * @param deal
	 *            El deal al que se le insertar&aacute; el detalle de divisa.
	 * @param recibimos
	 *            True para Compras, False para Ventas.
	 * @param fechaValor
	 *            (CASH | TOM | SPOT).
	 * @param claveFormaLiquidacion
	 *            La clave de la forma de liquidaci&oacute;n (TRAEXT, CHVIAJ,
	 *            etc)
	 * @param idDivisa
	 *            La clave de la divisa.
	 * @param tipoCambioMesa
	 *            El tipo de cambio de la mesa.
	 * @param monto
	 *            El monto en la divisa.
	 * @param tipoCambio
	 *            El tipo de cambio al cliente.
	 * @param precioReferenciaMidSpot
	 *            El precio de referencia Mid Spot para la captura del detalle.
	 * @param precioReferenciaSpot
	 *            El precio de referencia Spot para la captura del detalle.
	 * @param factorDivisa
	 *            El factor divisa actual.
	 * @param idSpread
	 *            El identificador del spread actual aplicable.
	 * @param idPrecioReferencia
	 *            El identificador del precio de referencia actual.
	 * @param idFactorDivisa
	 *            El identificador del factor divisa actual.
	 * @param mnemonico
	 *            El mnem&oacute;nico a asignar (opcional, puede ser null).
	 * @param afectarPosicion
	 *            Si el detalle debe o no afectar la posici&oacute;n.
	 * @param plantilla
	 *            La plantilla a asignar (opcional).
	 * @param limRestOper
	 *            Los l&iacute;mites de restricci&oacute;n de operaci&oacute;n.
	 * @param operacion Flujo desde donde se esta efectuando la insercion (SPLIT, GOMA, etc..)
	 * @return DealDetalle El detalle insertado.
	 * @throws SicaException
	 *             Si no se puede insertar el detalle.
	 */
	DealDetalle insertarDivisa(String ticket, Deal deal, boolean recibimos,
			String fechaValor, String claveFormaLiquidacion, String idDivisa,
			double tipoCambioMesa, double monto, double tipoCambio,
			Double precioReferenciaMidSpot, Double precioReferenciaSpot,
			Double factorDivisa, int idSpread, Integer idPrecioReferencia,
			Integer idFactorDivisa, String mnemonico, boolean afectarPosicion,
			IPlantilla plantilla, LimitesRestriccionOperacion limRestOper, TipoOperacionDealDto operacion)
			throws SicaException;

	/**
	 * Inserta un detalle del deal que no tiene producto y con la divisa de
	 * referencia de la mesa del deal al que pertenece el detalle.
	 * 
	 * @param ticket
	 *            El ticket de la sesi&oacute;n del usuario.
	 * @param deal
	 *            El encabezado de deal.
	 * @param recibimos
	 *            true para recibimos y false para entregamos.
	 * @param mnemonico
	 *            El mnem&oacute;nico a asignar. No debe ser nulo.
	 * @param cantidad
	 *            El monto en pesos.
	 * @return DealDetalle.
	 * @throws com.ixe.ods.sica.SicaException
	 *             Si la cantidad es menor a 1 centavo o no se cumplen con las
	 *             restricciones del mnem&oacute;nico.
	 */
	DealDetalle insertarMxn(String ticket, Deal deal, boolean recibimos,
			String mnemonico, double cantidad) throws SicaException;

	/**
	 * Inicializa las relaciones de deal con Canal, Mesa, contratoSica, cliente
	 * y de cada uno de sus detalles: factorDivisa y divisa.
	 * 
	 * @param deal
	 *            El deal a inicializar.
	 */
	void inicializarDeal(Deal deal);

	/**
	 * Inicializa todas las relaciones del objeto plantilla.
	 * 
	 * @param plantilla
	 *            La plantilla a inicializar.
	 */
	void inicializarPlantilla(IPlantilla plantilla);

	/**
	 * Regresa false si la divisa del detalle es no frecuente. Regresa true si
	 * el monto m&aacute;ximo de la divisa es menor al monto del detalle del
	 * deal especificado. Usado para determinar si el detalle requiere
	 * autorizaci&oacute;n por monto m&aacute;ximo excedido.
	 * 
	 * @param idMesaCambio
	 *            La mesa de cambio a la que pertenece el deal.
	 * @param divisa
	 *            La divisa del detalle del deal.
	 * @param monto
	 *            El monto del detalle del deal.
	 * @return boolean.
	 */
	boolean montoMaximoExcedido(int idMesaCambio, Divisa divisa, double monto);

	List obtenerMontosMaximosByCanalProducto(Canal canal,
			String claveFormaLiquidacion);

	/**
	 * Permite reasignar la cuenta a otro ejecutivo.
	 * 
	 * @param cta
	 *            La cuentaEjecutivo a borrar.
	 * @param nvaCta
	 *            La cuentaEjecutivo a insertar.
	 */
	void reasignarCuentaEjecutivo(CuentaEjecutivo cta, CuentaEjecutivo nvaCta);

	/**
	 * Registra la modificaci&oacute;n del Tipo de Cambio Mesa en el Deal.
	 * 
	 * @param deal
	 *            El deal.
	 * @throws SicaException
	 *             Si algo sale mal.
	 */
	void registrarCambioTCM(Deal deal) throws SicaException;

	/**
	 * Registra la modificaci&oacute;n del Tipo de Cambio Mesa en un Detalle de
	 * Deal en espec&iacute;fico.
	 * 
	 * @param dealDet
	 *            El objeto detalle de deal.
	 * @param tcm
	 *            El Tipo Cambio Mesa a modificar.
	 * @throws SicaException
	 *             Si algo sale mal.
	 */
	void registrarCambioTCMDealDet(DealDetalle dealDet, double tcm)
			throws SicaException;

	/**
	 * Registra la modificaci&oacute;n del Tipo de Cambio Mesa en un Detalle de
	 * Deal en espec&iacute;fico.
	 * 
	 * @param idDealDet
	 *            El id del detalle de deal.
	 * @param tcm
	 *            El Tipo Cambio Mesa a modificar.
	 * @throws SicaException
	 *             Si algo sale mal.
	 */
	void registrarCambioTCMDealDet(int idDealDet, double tcm)
			throws SicaException;

	/**
	 * Revisa la l&iacute;nea de operaci&oacute;n de un Deal y actualiza su
	 * estado.
	 * 
	 * @param deal
	 *            Deal a actualizar
	 * @param usuario
	 *            Usuario que realiza la operaci&oacute;
	 * @throws SicaException
	 *             Si algo sale mal.
	 */
	void revisarLineaOperacionYActualizarDeal(Deal deal, IUsuario usuario)
			throws SicaException;

	/**
	 * Almacena en la base de datos un Deal
	 * 
	 * @param deal
	 *            Deal a almacenar
	 * @throws SicaException
	 *             Si no se puede calcular la fecha valor de la posici&oacute;n
	 *             que debe afectarse.
	 */
	void storeDeal(Deal deal) throws SicaException;

	/**
	 * Actualiza los factores divisa actuales contenidos en una lista
	 * 
	 * @param factoresDivisa
	 *            Lista de factores divisa
	 */
	void storeFactoresDivisa(List factoresDivisa);

	/**
	 * Hace store a la plantilla recibida y update al detalle del deal en una
	 * sola transacci&oacute;n. Utilizado en la asignaci&oacute;n de nuevas
	 * plantillas a los detalles de deal.
	 * 
	 * @param det
	 *            El detalle del deal a actualizar.
	 * @param plantilla
	 *            La plantilla a insertar.
	 */
	void storePlantillaParaDealDetalle(DealDetalle det, IPlantilla plantilla);

	/**
	 * Actualiza el Precio de Referencia Actual
	 * 
	 * @param pr
	 *            Precio de Referencia a almacenar
	 */
	void storePrecioReferencia(PrecioReferenciaActual pr);

	/**
	 * Obtiene el registro espec&iacute;fico de la Posici&oacute;n de acuerdo a
	 * la Mesa de Cambio y la Divisa.
	 * 
	 * @param idMesaCambio
	 *            La Mesa de Cambio.
	 * @param idDivisa
	 *            La Divisa.
	 * @return Posicion El registro encontrado.
	 * @throws SicaException
	 *             Si algo sale mal.
	 */
	Posicion findRegistroPosicionByIdMesaCambioAndIdDivisa(int idMesaCambio,
			String idDivisa) throws SicaException;

	/**
	 * Regresa la lista de Reversos que estan en status pendiente
	 * 
	 * @return List La Lista de Reversos
	 */
	List findReversoByStatusPendiente();

	/**
	 * Regresa una lista de instancias SapGenPolXs que corresponden al deal
	 * especificado.
	 * 
	 * @param idDeal
	 *            El n&uacute;mero de deal.
	 * @return List de SapGenPolXs.
	 */
	List findSapGenPolXsByIdDeal(int idDeal);

	/**
	 * Regresa la Lista de Reconocimientos de Utilidades del D&iacute;a de Hoy.
	 * 
	 * @return List La Lista de Reconocimientos.
	 */
	List findReconocimientos();

	/**
	 * Regresa el Reconocimiento de Utilidades del D&iacute;a de Hoy para unas
	 * ciertas Mesa-Divisa.
	 * 
	 * @param idMesaCambio
	 *            La Mesa de Cambio para la cual se quiere obtener el Registro
	 *            en SC_RECO_UTILIDAD.
	 * @param idDivisa
	 *            La Divisa para la cual se quiere obtener el Registro en
	 *            SC_RECO_UTILIDAD.
	 * @return RecoUtilidad El Registro SC_RECO_UTILIDAD.
	 */
	RecoUtilidad findReconocimientoByMesaAndDivisa(int idMesaCambio,
			String idDivisa);

	/**
	 * Regresa las remesas pendientes de confirmar que se capturaron en la fecha
	 * especificada.
	 * 
	 * @param fechaCaptura
	 *            La fecha a consultar.
	 * @return List de Actividad.
	 */
	List findRemesasPendientes(Date fechaCaptura);

	/**
	 * Calcula y asigna comisionCobradaMxn de acuerdo al tipo de cambio de la
	 * mesa y el factor divisa en el caso de divisas frecuentes; o con base al
	 * precio de referencia para los dem&aacute;s casos.
	 * 
	 * @param det
	 *            El detalle de deal.
	 * @param precioReferenciaSpot
	 *            El precio de referencia Spot en el momento de la captura.
	 */
	public void calcularComisionMxn(DealDetalle det, double precioReferenciaSpot);

	/**
	 * Calcula la Utilidad por Neteo de Operaciones.
	 * 
	 * @param pos
	 *            La Posici&oacute;n de donde se obtendr&aacute;n valores para
	 *            los c&aacute;lculos de la Utilidad por Neteo.
	 * @return double El Monto Calculado por Neteo.
	 */
	double calculaMontoUtilidadesNeteo(Posicion pos);

	/**
	 * Calcula la Utilidad o P&eacute;rdida a reconocer.
	 * 
	 * @param pos
	 *            La Posici&oacute;n de donde se obtendr&aacute;n valores para
	 *            los c&aacute;lculos de la Utilidad.
	 * @param tcAmaneceMesa
	 *            El tipo de cambio al que el usuario desea que amanezca la
	 *            mesa.
	 * @return double El Monto Calculado de Utilidad o P&eacute;rdida.
	 */
	double calculaMontoReconocimientoUtilidades(Posicion pos,
			double tcAmaneceMesa);

	/**
	 * Servicio para generar un Reconocimiento de Utilidades.
	 * 
	 * @param idUsuario
	 *            El Usuario que efectu&oacute; la operaci&oacute;n desde el
	 *            sistema.
	 * @param idCanalDel
	 *            El Canal "Del" que se retiran Utilidades o que se nececita
	 *            cubrir un Corto.
	 * @param idCanalAl
	 *            El Canal "Al" que se le Depositan las Utilidades o "Al" que se
	 *            le hace un Retiro para cubrir la P&eacute;rdida de otro Canal.
	 * @param portafolio
	 *            El Portafolio del cual se quieren Reconocer las Utilidades.
	 * @param alPortafolio
	 *            En caso de Divisa Referencia USD, el Portafolio al cual se
	 *            quiere Comprar Utilidades o Vender P&eacute;rdidas.
	 * @param divisaOper
	 *            La Divisa en que se efect&uacute;a la Operaci&oacute;n de
	 *            Reconocimiento. Diferente a la Divisa de Referencia del
	 *            Portafolio del que se Reconocen las Utilidades.
	 * @param valor
	 *            La Fecha Valor de la Operaci&oacute;n. POr default es CASH.
	 * @param tipoCambio
	 *            El Tipo de Cambio de la Operaci&oacute;n.
	 * @param observaciones
	 *            Las Observaciones de la Operaci&oacute;n.
	 * @param isDivisaMXN
	 *            Si el Reconocimiento de Utilidades es en MXN.
	 * @param tcAmaneceMesa
	 *            El Tipo de Cambio al que desea el usuario que Amanezca la
	 *            Mesa.
	 * @param tcUsdMxn
	 *            Tipo de cambio USD/MXN para las mesas que no es MXN su divisa
	 *            de referencia.
	 * @return double EL Monto Calculado de la Operaci&oacute;n. Si es negativo
	 *         representa P&eacute;rdida, si es positivo representa Utilidad.
	 * @throws SicaException
	 *             Si algo sale mal.
	 */
	double storeReconocimientoUtilidades(int idUsuario, String idCanalDel,
			String idCanalAl, MesaCambio portafolio, MesaCambio alPortafolio,
			Divisa divisaOper, String valor, double tipoCambio,
			String observaciones, boolean isDivisaMXN, double tcAmaneceMesa,
			double tcUsdMxn) throws SicaException;

	/**
	 * Inserta un nuevo registro de la tabla SC_SPREAD.
	 * 
	 * @param spread
	 *            El spread a insertar.
	 */
	void storeSpread(Spread spread);

	/**
	 * Inserta un nuevo spread actual en la tabla correspondiente.
	 * 
	 * @param spread
	 *            El spread a insertar.
	 */
	void storeSpreadActual(Spread spread);

	/**
	 * Inserta la lista de spreads proporcionada.
	 * 
	 * @param spreads
	 *            La lista de spreads a insertar.
	 */
	void storeSpreads(List spreads);

	/**
	 * Inserta el swap en la base de datos, asigna las relaciones entre el swap
	 * y el deal y actualiza el deal en la base de datos.
	 * 
	 * @param swap
	 *            El swap.
	 * @param deal
	 *            El deal que pertenece al swap.
	 */
	void storeSwapParaDeal(Swap swap, Deal deal);

	/**
	 * Servicio para insertar un Traspaso enter Portafolios.
	 * 
	 * @param traspasoMesa
	 *            El Traspaso a guardar.
	 * @param idUsuario
	 *            El Usuario que efectu&oacute; la operaci&oacute;n desde el
	 *            sistema.
	 * @param monto
	 *            EL Monto del Traspaso.
	 * @param valor
	 *            La Fecha Valor de la operaci&oacute;n.
	 * @param idCanalAl
	 *            EL Canal del Portafolio "Al" que se hace el Traspaso.
	 * @param idCanalDel
	 *            EL Canal del Portafolio "Del" que se hace el Traspaso.
	 * @throws SicaException
	 *             Si algo sale mal.
	 */
	void storeTraspasoPortafolios(TraspasoMesa traspasoMesa, int idUsuario,
			double monto, String valor, String idCanalAl, String idCanalDel)
			throws SicaException;

	/**
	 * Servicio para el Uso y Liberaci&oacute;n de L&iacute;neas de
	 * Operaci&oacute;n desde Deals Interbancarios.
	 * 
	 * @param lineaOperacion
	 *            La lineaOperacion en uso del Broker.
	 * @param tipoOper
	 *            Si la Operaci&oacute;n es "DA" (Uso) o "DL"
	 *            (Liberaci&oacute;n).
	 * @param dealDetalle
	 *            El DealDetalle.
	 * @param usuario
	 *            El Usuario logeado al sistema.
	 * @param seAutorizaExcedente
	 *            Si se Autoriza Exceder la L&iacute;nea de Operaci&oacute;n o
	 *            no.
	 * @return double 0: Operaci&oacute;n Normal, 0<: El Excedente de la
	 *         L&iacute;nea de Operaci&oacute;n.
	 */
	double usoLiberacionLineaOperacion(LineaOperacion lineaOperacion,
			String tipoOper, DealDetalle dealDetalle, IUsuario usuario,
			boolean seAutorizaExcedente);

	/**
	 * Encuentra la Fecha valor de un <code>DealPosicion</code> de acuerdo a su
	 * <code>idPosicionLog</code>.
	 * 
	 * @param idDealPosicion
	 *            El identificador del detalle.
	 * @return String La Fecha Valor.
	 */
	String findTipoValorByIdDealPosicion(int idDealPosicion);

	/**
	 * Realiza la b&uacute;squeda de Deals para el el reporte diario de
	 * operaciones.
	 * 
	 * @param idDivisa
	 *            Deals con la divisa seleccionada.
	 * @param formaLiquidacion
	 *            Tipo de liquidaci&oacute;n.
	 * @param operacion
	 *            Operaci&oacute;n (Compra o Venta).
	 * @param promotor
	 *            Nombre del promotor
	 * @param desde
	 *            Fecha apartir de la cual se realizara la b&uacute;squeda de
	 *            los deals
	 * @param hoy
	 *            Fecha del d&ioacute;a de hoy
	 * @return List
	 */
	List findReporteDealsDiarios(String idDivisa, String formaLiquidacion,
			String operacion, Integer promotor, Date desde, Date hoy);

	/**
	 * Realiza la b&uacute;squeda de Deals para el el reporte diario de
	 * operaciones con los criterios de busqueda por monto.
	 * 
	 * @param idDivisa
	 *            El id de la divisa seleccionada.
	 * @param formaLiquidacion
	 *            La forma de liquidacion.
	 * @param operacion
	 *            El tipo de operacion (compra o venta).
	 * @param promotor
	 *            El nombre del promotor.
	 * @param desde
	 *            La fecha de inicio de la busqueda.
	 * @param hoy
	 *            La fecha de fin de la busqueda.
	 * @param montoMinimo
	 *            El monto minimo de la busqueda.
	 * @param montoMaximo
	 *            El monto maximo de la busqueda.
	 * @return List.
	 */
	List findReporteDealsDiarios(String idDivisa, String formaLiquidacion,
			String operacion, Integer promotor, Date desde, Date hoy,
			double montoMinimo, double montoMaximo);

	/**
	 * Realiza la b&uacute;squeda de Deals para el el reporte diario de
	 * operaciones con los criterios de busqueda varios
	 * 
	 * @param canales
	 *            lista de los ids canales de tipo String.
	 * @param promotores
	 *            lista de ids de los promotores de tipo Integer.
	 * @param divisas
	 *            lista de los ids de divisas de tipo String.
	 * @param tipoExcepciones
	 *            lista de excepciones de tipo String.
	 * @param tipoZonas
	 *            lista de zonas de tipo String.
	 * @param compra
	 *            <code>true</code> si se desean buscar compras.
	 * @param venta
	 *            <code>true</code> si se desean buscar ventas.
	 * @param fechaInicial
	 *            la fecha inicial para el reporte.
	 * @param fechaFinal
	 *            la fecha final para el reporte.
	 * @param montoMinimo
	 *            El monto minimo de la busqueda.
	 * @param montoMaximo
	 *            El monto maximo de la busqueda.
	 * @param contratos
	 *            lista de contratos a utilizar de tipo String.
	 * @param productos
	 *            lista de productos a utilizar de tipo String.
	 * @return List.
	 */
	List findReporteDealsDiarios(List canales, List promotores, List divisas,
			List tipoExcepciones, List tipoZonas, boolean compra,
			boolean venta, Date fechaInicial, Date fechaFinal,
			Double montoMinimo, Double montoMaximo, String[] contratos,
			String[] productos);

	/**
	 * Realiza la b&uacute;squeda de Deals para el reporte trimestral de
	 * operaciones con los criterios de busqueda varios.
	 * 
	 * @param canales
	 *            Los canales seleccionados.
	 * @param promotores
	 *            La lista de promotores seleccionados.
	 * @param divisas
	 *            La lista de divisas seleccinadas.
	 * @param tipoExcepciones
	 *            La lista de excepciones seleccionadas.
	 * @param tipoZonas
	 *            La lista de zonas seleccionadas.
	 * @param compra
	 *            Si requiere de operaciones de compra.
	 * @param venta
	 *            Si requiere de operaciones de venta.
	 * @param fechaInicial
	 *            La fecha inicial del trimestre.
	 * @param fechaFinal
	 *            La fecha final del trimestre.
	 * @param montoTrimCte
	 *            El monto acumulado para los clientes.
	 * @param montoTrimUsr
	 *            El monto acumulado para los usuarios.
	 * @param contratos
	 *            La lista de contratos que escribie&oacute;n.
	 * @param productos
	 *            La lista de productos seleccionados.
	 * 
	 * @return List
	 * @deprecated No funciona este m&eacute;todo.
	 */
	List findReporteDealsTrimestral(List canales, List promotores,
			List divisas, List tipoExcepciones, List tipoZonas, boolean compra,
			boolean venta, Date fechaInicial, Date fechaFinal,
			double montoTrimCte, double montoTrimUsr, String[] contratos,
			String[] productos);

	/**
	 * Realiza la busqueda de Detalles de Deals para los reportes de Banxico. La
	 * b&uacute;squeda se realiza por Sector Econ&oacute;mico y Fecha; es
	 * ordenada por Divisa y Compra-Venta.
	 * 
	 * @param gc
	 *            Fecha del d&iacute;a de hoy.
	 * @param fechaLiquidacion
	 *            Fecha de liquidaci&oacute;n del Deal
	 * @return List.
	 */
	List findAllDealDetallesBySectorAndDateOrderByDivisaAndRecibimos(Date gc,
			Date fechaLiquidacion);

	/**
	 * Realiza la busqueda de Detalles de Deals para los reportes de Banxico. La
	 * b&uacute;squeda se realiza por Sector Econ&oacute;mico y Fecha; es
	 * ordenada por Compra-Venta y Divisa.
	 * 
	 * @param gc
	 *            Fecha del d&iacute;a de hoy.
	 * @param fechaLiquidacion
	 *            Fecha de liquidaci&oacute;n del Deal
	 * @return List
	 */
	List findAllDealDetallesBySectorAndDateOrderByRecibimosAndDivisa(Date gc,
			Date fechaLiquidacion);

	/**
	 * Realiza la busqueda de Detalles de Deals para los reportes de Banxico. La
	 * b&uacute;squeda se realiza por Sector Econ&oacute;mico y Fecha; es
	 * ordenada por Divisa, Compra-Venta y N&uacute;mero de cuenta del cliente.
	 * 
	 * @param gc
	 *            Fecha del d&iacute;a de hoy.
	 * @param fechaLiquidacion
	 *            Fecha de liquidaci&oacute;n del Deal
	 * @return List
	 */
	List findAllDealDetallesBySectorAndDateOrderByDivisaAndRecibimosAndNoCuenta(
			Date gc, Date fechaLiquidacion);

	/**
	 * Encuentra los Deals Pendientes de Reconocimiento de Utilidad.
	 * 
	 * @return List.
	 */
	List findDealsPendientesReconocimiento();

	/**
	 * Obtiene el tipo de dato EjecutivoOrigen de la BUP, con base en su
	 * idPersona.
	 * 
	 * @param idPersona
	 *            IdPersona a econtar en la BUP
	 * @return EjecutivoOrigen.
	 */
	EjecutivoOrigen findEjecutivoOrigenByIdPersona(Integer idPersona);

	/**
	 * Regresa una lista de objetos Persona que corresponden a los ejecutivos de
	 * Ixe Directo, es decir, aquellos usuarios que en la tabla
	 * SEGU_ASOCIACION_FACULTAD tengan la facultad SICA_CAN_DIR.
	 * 
	 * @return List de Persona.
	 */
	List findEjecutivosIxeDirecto();

	/**
	 * Encuentra una Divisa dado el idMoneda de Phoenix
	 * 
	 * @param idMoneda
	 *            Clave de la divisa en Phoenix
	 * @return Divisa.
	 */
	Divisa findDivisaByIdMoneda(String idMoneda);

	/**
	 * Encuentra los Deals Interbancarios para el reporte diario.
	 * 
	 * @param desde
	 *            Fecha de hoy a las 0.00.00
	 * @param hasta
	 *            Fecha actual.
	 * @return List.
	 */
	List findAllDealsInterbancariosReporte(Date desde, Date hasta);

	/**
	 * Encuentra los Deals Interbancarios para el reporte diario.
	 * 
	 * @param desde
	 *            Fecha de hoy a las 0.00.00
	 * @param hasta
	 *            Fecha actual.
	 * @param idDivisa
	 *            El id de la divisa.
	 * @param idMesaCambio
	 *            El id de la mesa de operacion.
	 * @param idPersona
	 *            El id persona del cliente.
	 * @return List.
	 */
	List findAllDealsInterbancariosReporte(Date desde, Date hasta,
			String idDivisa, int idMesaCambio, Integer idPersona);

	/**
	 * Encuentra las polizas contables generadas entre las fechas especificadas
	 * para el reporte de auxiliares contables.
	 * 
	 * @param desde
	 *            La fecha de inicio.
	 * @param hasta
	 *            La fecha de fin.
	 * @return List
	 */
	List findAllDealsReporteAuxiliaresContables(Date desde, Date hasta);

	/**
	 * Encuentra las polizas contables generadas entre las fechas especificadas
	 * para el reporte de polizas contables.
	 * 
	 * @param desde
	 *            La fecha de inicio.
	 * @param hasta
	 *            La fecha de fin.
	 * @return LIst
	 */
	List findAllDealsReportePolizasContables(Date desde, Date hasta);

	/**
	 * Encuentra la posicion final para todas las mesas.
	 * 
	 * @return List
	 */
	List findPosicionAllMesas();

	/**
	 * Encuentra las polizas generadas el dia actual.
	 * 
	 * @param fechaHoy
	 *            La fecha de hoy a las 0:00:00
	 * @param fechaHoy2
	 *            La fecha de hoy a las 11:59:59
	 * @return List.
	 */
	List findDetallesUtilidadesMayoreo(Date fechaHoy, Date fechaHoy2);

	/**
	 * Encuentra los detalles de deal capturados por el canal de
	 * operaci&oacute;n Ixe Directo.
	 * 
	 * @param inicio
	 *            La fecha de inicio de la b&uacute;squeda.
	 * @param fin
	 *            La fecha de fin de la b&uacute;squeda.
	 * @param canal
	 *            El canal de operaci&oacute;n.
	 * @param mnemonicos
	 *            La lista con los mnemonicos para utilidades de sucursales
	 * @return List
	 */
	List findDetallesUtilidadesIxeDirectoSucursales(Date inicio, Date fin,
			String canal, List mnemonicos);

	/**
	 * Encuentra los detalles de deal capturados por el canal de
	 * operaci&oacute;n Ixe Directo.
	 * 
	 * @param inicio
	 *            La fecha de inicio de la b&uacute;squeda.
	 * @param fin
	 *            La fecha de fin de la b&uacute;squeda.
	 * @param canal
	 *            El canal de operaci&oacute;n.
	 * @param mnemonicos
	 *            La lista de mnemonicos.
	 * @return List
	 */
	List findDetallesUtilidadesIxeDirecto(Date inicio, Date fin, String canal,
			List mnemonicos);

	/**
	 * Encuentra los mnemonicos para las utilidades de Ixe Directo.
	 * 
	 * @param mnemonicos
	 *            La lista de mnemonicos.
	 * @return List.
	 */
	List findMnemonicosUtilidadesIxeDirecto(List mnemonicos);

	/**
	 * Permite validar si el Cliente de un Deal puede hacer operaciones de
	 * m&aacute;s de $ 3,000.00 USD (en caso de que la sumatoria dolarizada de
	 * los montos de todos los detalles del deal, m&aacute;s el monto
	 * tambi&eacute;n dolarizado del nuevo detalle que se pretende insertar, es
	 * mayor a $ 3,000.00 USD)
	 * 
	 * @param deal
	 *            El deal del cual se quiere validar al Cliente por Monto.
	 * @param nuevoCliente
	 *            Si el nuevo detalle de deal a insertar es recibimos (compra) o
	 *            entregamos (venta).
	 * @throws SicaException
	 *             Si no se puede realizar la operaci&oacute;n.
	 */
	void validarClientePorMontoMayorATresMilUSD(Deal deal, Persona nuevoCliente)
			throws SicaException;

	/**
	 * Permite validar si el Cliente de un Deal puede hacer operaciones de
	 * m&aacute;s de $ 3,000.00 USD (en caso de que la sumatoria dolarizada de
	 * los montos de todos los detalles del deal, m&aacute;s el monto
	 * tambi&eacute;n dolarizado del nuevo detalle que se pretende insertar, es
	 * mayor a $ 3,000.00 USD)
	 * 
	 * @param deal
	 *            El deal del cual se quiere validar al Cliente por Monto.
	 * @param montoNvoDetUSD
	 *            El monto dolarizado del nuevo detalle a insertar.
	 * @throws SicaException
	 *             En caso de que la sumatoria dolarizada de los montos de todos
	 *             los detalles del deal, m&aacute;s el monto del nuevo detalle
	 *             a insertar, sea mayor a $3,000.00 y el cliente no pueda
	 *             operar ese monto por falta de informaci&oacute;n en la BUP.
	 */
	void validarClientePorMontoMayorATresMilUSD(Deal deal, double montoNvoDetUSD)
			throws SicaException;

	/**
	 * Obtiene los Deals caputrados por el Teller, es decir, aquellos deals que
	 * no se encuentren cancelados y hayan sido capturados en horario nocturno o
	 * vespertino.
	 * 
	 * @return List.
	 */
	List findDealsTeller();

	/**
	 * Crea una actividad de sobreprecio para un deal de sucursales.
	 * 
	 * @param deal
	 *            El deal para el que se solicitar&aacute; la
	 *            autorizaci&oacute;n.
	 */
	void generarAutorizacionSobreprecioSucursales(Deal deal);

	/**
	 * Regresa un lista de objetos ParametroSica ordenados
	 * alfab&eacute;ticamente.
	 * 
	 * @return List de ParametroSica.
	 */
	List findAllParametrosSica();
	
	/**
	 * Find parametros by prefix.
	 *
	 * @param prefix the prefix
	 * @return the map
	 */
	Map findParametrosByPrefix(final String prefix);

	/**
	 * Encuentra los tipos de cambio generardos para el teller.
	 * 
	 * @param fecha
	 *            La fecha de los tipos de cambio.
	 * @return List.
	 */
	List findTiposCambioTellerByFecha(Date fecha);

	/**
	 * 
	 * Valida la documentacion de los clientes que vienen en forma de Map ademas
	 * de los datos de la BUP. Devuelve una lista de mapas donde cada uno de
	 * estos tiene los datos de cada cliente Llaves del MAP: (Integer)
	 * personaMap.get("idPersona"); (String) personaMap.get("tipoPersona"); PM o
	 * PF (String) personaMap.get("noCuenta"); cuenta de cambios (CAM10)
	 * (String) personaMap.get("mensajeError"); String donde se encuentra la
	 * documentacion faltante del cliente o la informacion erronea que esta
	 * registrada en la BUP
	 * 
	 * @param mapaPersonasList
	 *            List de objetos map con los datos del cliente
	 * @return List - lista de mapas con la informacion de cada cliente invalido
	 * @throws SicaException
	 */
	List validarDocumentacionClientes(List mapaPersonasList)
			throws SicaException;

	/**
	 * La funcion de este metodo es la de validar que un cliente tenga
	 * registrada la informacion necesaria fin de poder realizar operaciones en
	 * el Sistema de Cambios(SICA).
	 * 
	 * @param idPersona
	 *            Integer - Identificador del cliente.
	 * @param isPersonaFisica
	 *            boolean - Si es true el cliente es una persona fisica, false
	 *            indica persona moral.
	 * @param noCuenta
	 *            - Cuenta CAM10 del cliente
	 * @throws ContratacionException
	 *             en caso de que al cliente le falte tener registrada alguna
	 *             informacion.
	 * 
	 */
	void isValidoClienteSica(Integer idPersona, boolean isPersonaFisica,
			String noCuenta) throws ContratacionException;

	/**
	 * La funcion de este metodo es la de validar que un cliente tenga
	 * registrada la informacion necesaria fin de poder realizar operaciones en
	 * el Sistema de Cambios(SICA).
	 * 
	 * @param idPersona
	 *            Integer - Identificador del cliente.
	 * @param isPersonaFisica
	 *            boolean - Si es true el cliente es una persona fisica, false
	 *            indica persona moral.
	 * @return String - La informacion erronea o faltante que tiene el cliente
	 *         en la BUP
	 * @throws ContratacionException
	 *             en caso de que al cliente le falte tener registrada alguna
	 *             informacion.
	 * 
	 */
	String validaInformacionBUPcliente(Integer idPersona,
			boolean isPersonaFisica) throws SicaException;

	/**
	 * Encuentra un registro espec&iacute;fico Combinaci&oacute;n de Divisas de
	 * acuerdo a la Divisa y Contra Divisa.
	 * 
	 * @param idDivisa
	 *            La clave de la divisa.
	 * @param idContraDivisa
	 *            La clave de la contra divisa.
	 * @return CombinacionDivisa.
	 */
	CombinacionDivisa findCombinacionDivisa(String idDivisa,
			String idContraDivisa);

	/**
	 * Devuelve la fecha Compromiso que tiene el cliente para entregar toda la
	 * documentacion requerida para operar
	 * 
	 * @return Date fecha Compromiso
	 * @param noCuenta
	 *            Numero de cuenta del cliente
	 * @throws ContratacionException
	 *             -Error generico del EJB de contratacion
	 */
	Map fechaEntregaDocumentacionCAM10(String noCuenta)
			throws ContratacionException;

	/**
	 * Encuentra un registro espec&iacute;fico de acuerdo al id.
	 * 
	 * @param idPromotor
	 *            El n&uacute;mero de promotor.
	 * @param idGrupoTrabajo
	 *            El n&uacute;mero de Grupo de Trabajo.
	 * @return GrupoTrabajoPromotor.
	 */
	GrupoTrabajoPromotor findCombinacionGrupoTrabajo(String idPromotor,
			String idGrupoTrabajo);

	/**
	 * Encuentra el el grupo de trabajo del usuario por su id.
	 * 
	 * @param idUsuario
	 *            El n&uacute;mero de usuario.
	 * @return String.
	 */
	GrupoTrabajo findIdGrupoTrabajoByIdUsuario(Integer idUsuario);

	/**
	 * Encuentra los datos complementarios del cliente por n&uacute;mero de
	 * cuenta.
	 * 
	 * @param noCuenta
	 *            El n&uacute;mero de cuenta.
	 * @return ComplementoDatos
	 */
	ComplementoDatos findComplementoDatosByNoCuenta(String noCuenta);

	/**
	 * Encuentra los datos complementarios del cliente por medio del folio del
	 * IFE.
	 * 
	 * @param ifeFolio
	 *            El n&uacute;mero del folio de su credencial del ife.
	 * @return ComplementoDatos
	 */
	ComplementoDatos findComplementoDatosByIfe(String ifeFolio);

	/**
	 * Encuentra los datos complementarios del cliente por medio del
	 * n&uacute;mero de pasaporte y el identificador del pa&iacute;s..
	 * 
	 * @param pasaporte
	 *            El n&uacute;mero de pasaporte.
	 * @param idBupPais
	 *            El id del pa&iacute;s.
	 * @return ComplementoDatos
	 */
	ComplementoDatos findComplementoDatosByPasaporteAndIdBupPais(
			String pasaporte, String idBupPais);

	/**
	 * Encuentra los deal pendientes por liquidar T+4 de acuerdo a la fecha
	 * dada.
	 * 
	 * @param fechaLiquidacion
	 *            La fecha de liquidaci&oacute;n.
	 * @return List.
	 */
	List findAllDealsPendientesLiquidarT4(Date fechaLiquidacion);

	/**
	 * Encuentra todas las fechas inh&aacute;biles de EUA.
	 * 
	 * @return List de FechaInhabilEua.
	 */
	List findAllFechasInhabilesEua();

	/**
	 * Encuentra el token por el ID.
	 * 
	 * @param token
	 *            El token.
	 * @return TokenTeller.
	 */
	TokenTeller findTokenTellerById(String token);

	/**
	 * Utilizado para construir un detalle de contraparte de un deal. Para
	 * transacciones JTA.
	 * 
	 * @param deal
	 *            El encabezado del deal.
	 * @param recibimos
	 *            true para recibimos, false para entregamos.
	 * @param fechaValor
	 *            CASH | TOM | SPOT | 72HR.
	 * @param claveFormaLiquidacion
	 *            La clave del producto.
	 * @param div
	 *            El objeto Divisa.
	 * @param tipoCambioMesa
	 *            El tipo de cambio del pizarr&oacute;n.
	 * @param monto
	 *            El monto en la divisa.
	 * @param tipoCambio
	 *            El tipo de cambio dado al cliente.
	 * @param precioReferenciaMidSpot
	 *            El precio de referencia Mid Spot para la captura del detalle.
	 * @param precioReferenciaSpot
	 *            El precio de referencia Spot para la captura del detalle.
	 * @param factorDivisa
	 *            El factor divisa actual.
	 * @param idSpread
	 *            El identificador del spread actual.
	 * @param idPrecioReferencia
	 *            El identificador del precio de referencia actual.
	 * @param idFactorDivisa
	 *            El identificador del Factor Divisa utilizado.
	 * @param mnemonico
	 *            El mnem&oacute;nico a aplicar.
	 * @param fechasValor
	 *            El arreglo de fechas valor (h&aacute;biles).
	 * @param montoMaximoExcedido
	 *            Si el montoMaximo ha sido excedido o no.
	 * @param estado
	 *            El estado actual del sistema.
	 * @param limRestOper
	 *            Los l&iacute;mites de restricci&oacute;n de operaci&oacute;n.
	 * @return DealDetalle.
	 * @throws SicaException
	 *             Si algo sale mal.
	 */
	DealDetalle crearDealDetalle(Deal deal, boolean recibimos,
			String fechaValor, String claveFormaLiquidacion, Divisa div,
			double tipoCambioMesa, double monto, double tipoCambio,
			Double precioReferenciaMidSpot, Double precioReferenciaSpot,
			Double factorDivisa, int idSpread, Integer idPrecioReferencia,
			Integer idFactorDivisa, String mnemonico, Date[] fechasValor,
			boolean montoMaximoExcedido, Estado estado,
			LimitesRestriccionOperacion limRestOper) throws SicaException;

	/**
	 * Utilizado para buscar una actividad en base a su idDealPosicion
	 * 
	 * @param idDealPosicion
	 *            El n&uacute;mero de detalle de deal.
	 * @param nombreActividad
	 *            El nombre de la actividad.
	 * @return Actividad
	 */
	Actividad findActividadByIdDealPosicion(int idDealPosicion,
			String nombreActividad);

	/**
	 * Se modifica la actividad que contiene un cambio de producto, a este
	 * registro se le actualiza el tipo de cambio de la mesa.
	 * 
	 * @param idActividad
	 *            El n&uacute;mero de la actividad.
	 * @param tcm
	 *            El tipo de cambio de la mesa.
	 */
	void modificarTcmEnActividadModProducto(int idActividad, double tcm);

	/**
	 * Encuentra el l&iacute;mite de restricci&oacute;n de compra y venta
	 * dependiendo de los par&aacute;metros enviados.
	 * 
	 * @param div
	 *            divisa.
	 * @param compra
	 *            <code>true</code> si se trata de compra, <code>false</code> si
	 *            se trata de una venta.
	 * @param tipoPersona
	 *            tipo de persona.
	 * @param cliente
	 *            <code>true</code> si se trata de un cliente,
	 *            <code>false</code> si se trata de un usuario.
	 * @param tipoZona
	 *            tipo de zona geogr&aacute;fica.
	 * @param mexicano
	 *            <code>true</code> si se trata de un mexicano,
	 *            <code>false</code> si se trata de un extranjero.
	 * @return LimiteEfectivo con el li&iacute;mite.
	 */
	LimiteEfectivo findLimitesByCriteria(Divisa div, Boolean compra,
			String tipoPersona, Boolean cliente, String tipoZona,
			Boolean mexicano);

	/**
	 * Encuentra los l&iacute;mites de restricci&oacute;n de operaci&oacute;n.
	 * 
	 * @param tipoPersona
	 *            tipo de persona.
	 * @param cliente
	 *            <code>true</code> si se trata de un cliente,
	 *            <code>false</code> si se trata de un usuario.
	 * @param tipoZona
	 *            tipo de zona geogr&aacute;fica.
	 * @param mexicano
	 *            <code>true</code> si se trata de un mexicano,
	 *            <code>false</code> si se trata de un extranjero.
	 * @return List con los l&iacute;mites encontrados.
	 */
	List findLimitesRestriccionOperacion(final String tipoPersona,
			final Boolean cliente, final String tipoZona, final Boolean mexicano);

	/**
	 * Encuentra toda la lista de personas dadas de alta en la lista blanca.
	 * 
	 * @return List con la lista de PersonaListaBlanca.
	 */
	List findPersonasListaBlanca();

	/**
	 * Encuentra si la persona se encuentra dada de alta en la lista blanca, en
	 * caso de ser as&iacute; se regresa el registro con la informaci&oacute;n
	 * del tipo de excepci&oacute;n y l&iacute;mites en caso de ser necesario.
	 * 
	 * @param contrato
	 *            contrato SICA.
	 * @return PersonaListaBlanca con la informaci&oacute;n.
	 */
	PersonaListaBlanca findPersonaListaBlanca(String contrato);

	/**
	 * Encuentra la lista de n&uacute;meros rim asociados a un contrato sica.
	 * 
	 * @param contrato
	 *            asociado a los rims.
	 * @return List de Integer.
	 */
	List findRimsByContratoSica(String contrato);

	/**
	 * Encuentra el municipio.
	 * 
	 * @param idMunicipio
	 *            id del municipio.
	 * @param idEstado
	 *            id del estado.
	 * @return BupMunicipio con la informaci&oacute;n.
	 */
	BupMunicipio findMunicipioEstado(String idMunicipio, String idEstado);

	/**
	 * Encuentra la lista de c&oacute;digos postales de la bup asociados a ese
	 * idMunicipio y idEstado.
	 * 
	 * @param idMunicipio
	 *            El id del municipio.
	 * @param idEstado
	 *            El id del estado.
	 * @return List con la informaci&oacute;n.
	 */
	List findCodigosPostalesBup(String idMunicipio, String idEstado);

	/**
	 * Encuentra el municipio y sus car&aacute;teristicas de la zona asociados
	 * al mismo.
	 * 
	 * @param idMunicipio
	 *            id del municipio.
	 * @param idEstado
	 *            id del estado.
	 * @return MunicipioListaBlanca con la informaci&oacute;n.
	 */
	MunicipioListaBlanca findMunicipioListaBlanca(String idMunicipio,
			String idEstado);

	/**
	 * Encuentra el c&oacute;digo postal y sus car&aacute;teristicas de la zona
	 * asociados al mismo.
	 * 
	 * @param cp
	 *            c&oacute;digo postal.
	 * @param clienteOrSucursal
	 * @return PersonaListaBlanca con la informaci&oacute;n.
	 */
	CodigoPostalListaBlanca findCodigoPostalListaBlanca(String cp,
			boolean clienteOrSucursal);

	/**
	 * Encuentra el c&oacute;digo postal y sus car&aacute;teristicas de la zona
	 * asociados al mismo.
	 * 
	 * @param idMunicipio
	 *            id del municipio.
	 * @param idEstado
	 *            id del estado.
	 * @return CodigoPostalListaBlanca con la informaci&oacute;n.
	 */
	CodigoPostalListaBlanca findCodigoPostalListaBlanca(String idMunicipio,
			String idEstado);

	/**
	 * Encuentra todos los registros de los c&oacute;digos postales en lista
	 * blanca.
	 * 
	 * @return List Lista con la informaci&oacute;n de los codigos postales en
	 *         lista blanca.
	 */
	List findAllCodigoPostalListaBlanca();

	/**
	 * Regresa las diferentes divisas y si es compre o venta (compra = S)
	 * 
	 * @return una lista 0 = divisa, 1= compra
	 */
	List getAllDivisasFromLimitesEfectivo();

	/**
	 * Obtiene el checksum de los c&oacute;digos postales registrados en la
	 * lista blanca.
	 * 
	 * @return Long n&uacute;mero.
	 */
	Long findCheckSumCodigosPostalesListBlanca();

	/**
	 * Regresa la lista de todos los canales, ordenados por idSucursal y nombre
	 * 
	 * @return List
	 */
	List findAllCanalesOrdered();

	/**
	 * Busca todas las sucursales que esten asociadas a un canal
	 * 
	 * @return List
	 */
	List findAllSucursalesWithCanal();

	/**
	 * Busca todas las divisiones existentes
	 * 
	 * @return
	 */
	List findDivisiones();

	/**
	 * Busca todas las plazas pertenecientes a una divisi&oacute;n dada
	 * 
	 * @param idDivision
	 * @return
	 */
	List findPlazasByDivision(Integer idDivision);

	/**
	 * Busca todas las zonas pertenecientes a una plaza dada
	 * 
	 * @param idPlaza
	 * @return
	 */
	List findZonasByPlaza(Integer idPlaza);

	/**
	 * Busca todas las sucursales pertenecientes a una zona dada
	 * 
	 * @param idZona
	 * @return
	 */
	List findSucursalesByZona(Integer idZona);

	/**
	 * Registra un cliente banorte
	 * 
	 * @param persona
	 * @param empresa
	 * @param SIC
	 * @param origen
	 * @param opera
	 * @param cuentaCheques
	 * @param usuarioCreacion
	 * @return
	 */
	public Integer storeDatosClienteBanorte(Integer idPersona, String empresa,
			String SIC, String origen, String opera, String cuentaCheques,
			int usuarioCreacion);
	
	public List findClienteByIdPersona(Integer idPersona);
	
	public List findClienteByIdPersonaAndIdCliente(Integer idPersona, Integer idCliente);
	
	public Integer storeDatosContrato(Integer cliente, String cuenta,
			int usuarioCreacion);
	
	/**
	 * Obtiene los datos del contrtao Corto JDCH
	 * 
	 * @param contrato SICA
	 * @return
	 */
	public Contrato getContratoCorto(String _contratoSica)
	throws SicaException;
	
	/**
	 * Obtiene los datos del contrtao Corto JDCH
	 * 
	 * @param contrato SICA
	 * @return
	 */
	public Contrato getContratoCortoXCuenta(String _contratoSica)
	throws SicaException;
	/**
	 * Obtiene la fecha de apertura del contrtao
	 * @param no_cuenta
	 * 
	 * @return fecha de apertura
	 */
	
	public List findFechaAperturaByContratoSica(ContratoSica no_cuenta);
	
	/**
	 * Obtiene la fecha parametro a partir de la cual se valida la documentacion en MOC
	 * @param 
	 * @return fecha a partir se validara documentacion en MOC
	 */
	public Date getFechaValidaDocumentacion();

	/**
	 * Obtiene la bandera para determinar si valida posicion a cero en el cierre de dia
	 * @param 
	 * @return bandera en parametros del SICA
	 */
	public String getBanderaValidaPosicionCierre();
	
	/**
	 * Obtiene todos los sistemas disponibles para operacion con tipo de cambio
	 * especial
	 * 
	 * @return
	 */
	List getAllSistemaTce();

	/**
	 * Obtiene todos los mensajes capturados en la interfaz de Tipo de cambio
	 * especial al dia de hoy
	 * 
	 * @return
	 */
	List findMensajesTceHoy();

	/**
	 * Almacena un mensaje capturado desde la interfaz de Tipo de cambio
	 * especial
	 * 
	 * @param mensaje
	 */
	void saveMensajeTce(MensajeTce mensaje);

	
	/**
	 * Busca en sc_cuenta_altamira
	 * 		a partir de un numero de cuenta.
	 * 
	 * @param cuentaAltamira el numero de cuenta Altamira
	 * @return <code>List</code> con objetos 
	 * 		   <code>CuentaAltamira</code>
	 */
	public List findCuentaAltamiraByNoCuenta (String cuentaAltamira);
	
	/**
	 * Inserta un registro en sc_cuenta_altamira
	 * 
	 * @param infoCuentaAltamiraDto con los 
	 * 	 datos del registro a dar de alta.
	 * @param claveUsuario  el usuario firmado al 
	 * 		sistema.
	 */
	public void storeCuentaAltamira(InfoCuentaAltamiraDto infoCuentaAltamiraDto, String claveUsuario);
	
	/**
	 *  Actualiza un registro de sc_cuenta_altamira
	 * @param cuentaAltamira los datos del registro 
	 * 		a actualizar.
	 * @param idPersona el identificador de la persona
	 * 		asociado a la cuenta.
	 * @param claveUsuario el usuario que realiza la 
	 * 		actualizaciï¿³n del registro.
	 */
    public void updateCuentaAltamira(CuentaAltamira cuentaAltamira, int idPersona, String claveUsuario);

    /**
     * Actualiza Bup_persona campo b_ilytics, para el proceso de estandarizacion y limpieza de la BUP
     * @param b_ilytics
     * @param idPersona
     * @return
     */
    public boolean actualizarProcesoEstandarizacionBUP(Integer b_ilytics ,Integer idPersona);
    
    /**
     * Metodo que valida si existe una entidad federativa de M\u00E9xico y el extranjero
     * @param entidad la entidad que se desea validar
     * @param pais el pais al que pertenece la entidad.
     * @return <code>true</code> en caso de que la entidad sea v\u00E1lida,
     * 		   <code>false</code> en cualquier otro caso.
     */
    public boolean isEntidadValida(String entidad, String pais); 
    
    /**
     * Encuentra la descripción de un Pais
     * 	 asociado a un registro de direccion en 
     * 	 particular
     * @param idDireccion con el identificador 
     *      del registro de dirección.
     * 
     * @return <code>String</code> con la 
     * 	 descripcion del pais asociado a la 
     *   dirección.
     */
    public String findPaisDireccion(Integer idDireccion);
    
 
        InfoFactura findInfoFactura(int idDealDetalle);
        
    /**
     * Consulta el catalogo 'sociedad mercantil'
     */ 
    public List findCatalogoSociedadMercantil(); 
    
    /**
     * Consulta el catalogo 'Tipo identificador'
     */
    public List findCatalogoIdentificadorContraparte();
    
    /**
     * Consulta el catalogo 'Actividad economica' (Tipo de contraparte)
     */
    public List findCatalogoActividadEconomica();
    
    /**
     * Consulta el catalogo 'Sector industrial'
     */
    public List findCatalogoSectorIndustrial();
    
    /**
     * Consulta el catalogo 'Pais'
     */
    public List findCatalogoPais();
    
    /**
     * Consulta el catalogo 'Tipo Relacion'
     */
    public List findCatalogoTipoRelacion();
    
    /**
     * Consulta el catalogo 'Evento Relacion'
     */
    public List findCatalogoEventoRelacion();
    
    /**
     * Consulta los datos regulatorios de la persona moral mediante su idPersona
     */
    public List findRegulatorioDatosPM(Integer idPersona);
    
    /**
     * Consulta los datos regulatorios de la relacion de la persona moral con la institucion mediante su idPersona
     */
    public List findRegulatorioInstitucion(Integer idPersona);
    
    /**
     * Consulta el perfil de la persona moral mediante su idPersona
     */
    public List findRegulatorioPerfil(Integer idPersona);
    
    /**
	 * Valida si el detalle de deal tiene un monto de 1 millón de dolares o mas y 
	 * verifica si el cliente cuenta con la información regulatoria requerida
	 */
	public void validarInformacionRegulatoria(Deal deal, Divisa div, DealDetalle det, String statusAnterior);
	
	/**
     * Actualiza el campo faltante (contrato sica) de la información regulatoria del cliente 
     * @param contratoSica
     */
    public void actualizarInfoRegulatoria(Integer idPersona, String contratoSica);
    
    /**
     * Selecciona los contratos SICA de la información regulatoria de los clientes para incluirlos en los reportes regulatorios de BANXICO.
     * @param opcionBusqueda Constante que identifica que tipo de informacióon regulatoria debe consultarse:
     * BUSCAR_REGULATORIOS_PM o BUSCAR_REGULATORIOS_PM
     * @return List Con objetos de tipo Perfil
     */
    public List criteriosInclusionDatosRegulatorios(int opcionBusqueda);
    
    /**
     * Servicio que consulta la información regulatoria especifica de los clientes
     * @param opcion Valor que indica que tipo de informacion regulatoria se consultará
     * @param contratosSica List de objetos String que representan los contratos SICA de los clientes
     * @return
     */
    public List consultarDatosRegulatorios(int opcion, final List contratosSica);
    
    /**
     * Servicio encargado de generar el backup de los reportes regulatorios de baxico para la fecha en curso
     * @param logger StringBuffer para almacenar las descripciones de los errores encontrados
     */
    public void generarBackupReportesRegulatorios(StringBuffer logger);
    
    /**
     * Servicio encargado de preparar los datos del reporte regulatorio
     * @param opcionBusqueda Constante que indica el tipo de informacion que manipulará: 
     * BUSCAR_REGULATORIOS_PM o BUSCAR_REGULATORIOS_PARTE_RELACIONADA
     * @param registros Lista de registros de tipo RegulatorioDatosPM o RegulatorioInstitucion
     * @param errores StringBuffer utilizado para almacenar las descripcuiones de los errores encontrados
     * @return StringBuffer con los campos de los registros separados por ';'. Los registros se separan por un '|'.
     */
    public StringBuffer prepararDatosReporte(int opcionBusqueda, List registros, StringBuffer errores);
    
    /**
     * Reinicia a cero los contadores de excesos delas lineas de credito activas. 
     */
    public void reiniciarContadoresExcesosLineasCreditoActivas(StringBuffer logger);
    
    public Integer findGiroEconomicoAsociadoBupActividadEconomica(String idActividadEconomica);
    
    public boolean actualizarPersonaFisica(final Map datosPf);
    
    public boolean actualizarPersonaMoral(final Map datosPf);
    
    public boolean actualizarDireccion(final Map datos);
    
    /**
     * Inserta la clave referencia del cliente.
     * @param datos
     * @return boolean
     */
    public boolean insertarClaveReferenciaCliente(final Map datos);
    
    /**
     * La eliminacion consiste en cambiar el estatus de la direccion. La eliminacion es logica.
     * @param datos
     * @return
     */
    public boolean eliminarDireccion(final Map datos);
    
	//60057-CFDI 3.3
	/**
	 * Registra un NIT de una Persona
	 * 
	 * @param persona
	 * @param nit
	 * @return Integer
	 */
	public Integer storeNit(Integer idPersona, String nit);
	
	//60057-CFDI 3.3
	/**
	 * Busca un NIT con base en un Id de Persona
	 * 
	 * @param persona
	 * @return NitPersona
	 */
	public NitPersona findNitByIdPersona(Integer idPersona);

    /**
     * Constante para indicar que se desea consultar la información regulatoria (Datos generales) de la Persona moral
     */
    public static final int BUSCAR_REGULATORIOS_PM = 1;
    
    /**
     * Constante para indicar que se desea consultar la información regulatoria (relación con la institución) del cliente
     */
    public static final int BUSCAR_REGULATORIOS_PARTE_RELACIONADA = 2;
        
	/**
	 * Constante para Cancelar Deals en Modo Swap
	 */
	public static final int TIPO_MOD_SWAP_CAN_DEALS = 0;

	/**
	 * Constante para Cancelar Swaps en Modo Swap
	 */
	public static final int TIPO_MOD_SWAP_CAN_SWAP = 1;

	/**
	 * Constante para Cancelar Deals y Swaps en Modo Swap
	 */
	public static final int TIPO_MOD_SWAP_CAN_TOT = 2;
}