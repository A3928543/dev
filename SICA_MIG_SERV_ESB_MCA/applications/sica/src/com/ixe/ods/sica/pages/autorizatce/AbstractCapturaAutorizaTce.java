package com.ixe.ods.sica.pages.autorizatce;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.NumberUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.tapestry.IRequestCycle;
import org.springframework.dao.DataAccessException;

import com.ixe.ods.bup.model.Direccion;
import com.ixe.ods.bup.model.EmpleadoIxe;
import com.ixe.ods.bup.model.Persona;
import com.ixe.ods.seguridad.model.IUsuario;
import com.ixe.ods.seguridad.model.TipoSistema;
import com.ixe.ods.sica.Constantes;
import com.ixe.ods.sica.Num;
import com.ixe.ods.sica.Redondeador;
import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.SupportEngine;
import com.ixe.ods.sica.Visit;
import com.ixe.ods.sica.dto.ClienteContratoInfoPromotorDto;
import com.ixe.ods.sica.dto.DetalleOperacionTceDto;
import com.ixe.ods.sica.dto.OperacionTceDto;
import com.ixe.ods.sica.model.Canal;
import com.ixe.ods.sica.model.CanalMesa;
import com.ixe.ods.sica.model.CodigoPostalListaBlanca;
import com.ixe.ods.sica.model.Deal;
import com.ixe.ods.sica.model.DealDetalle;
import com.ixe.ods.sica.model.Divisa;
import com.ixe.ods.sica.model.Estado;
import com.ixe.ods.sica.model.FactorDivisaActual;
import com.ixe.ods.sica.model.LimiteEfectivo;
import com.ixe.ods.sica.model.LimitesRestriccionOperacion;
import com.ixe.ods.sica.model.LogAuditoria;
import com.ixe.ods.sica.model.ParametroSica;
import com.ixe.ods.sica.model.PersonaListaBlanca;
import com.ixe.ods.sica.model.PrecioReferenciaActual;
import com.ixe.ods.sica.model.Spread;
import com.ixe.ods.sica.pages.SicaPage;
import com.ixe.ods.sica.pizarron.Consts;
import com.ixe.ods.sica.sdo.SicaServiceData;
import com.ixe.ods.sica.services.BusquedaClientesService;
import com.ixe.ods.sica.services.ContratoDireccionesService;
import com.ixe.treasury.dom.common.FormaPagoLiq;
import com.legosoft.hibernate.type.SiNoType;

/**
 * Funcionalidad comun para la pantalla de captura de autorizaciones tipo de cambio especial
 * 
 * @author Cesar Jeronimo Gomez
 */
public abstract class AbstractCapturaAutorizaTce extends SicaPage {
	
	/**
     * Busca el numero de deal proporcionado
     * 
     * @param dealNumber
     * @return
     */
    protected Deal secureFindDeal(String dealNumber) {
    	try {
			if( !StringUtils.isEmpty(dealNumber) && NumberUtils.isDigits(dealNumber) ) {
				Deal deal = getSicaServiceData().findDeal(Integer.parseInt(dealNumber));
				if(deal == null) {
					throw new SicaException("Deal no encontrado: " + dealNumber);
				}
				auditar( new Integer( dealNumber ), LogAuditoria.CONSULTA_DATOS_DEAL + "-BITACORA", null, "INFO", "E");
				return deal;
			} else {
				throw new SicaException("El deal " + dealNumber + " no es valido");
			}
		} catch (DataAccessException e) {
			throw new SicaException("Error al consultar informacion del deal " + dealNumber);
		}
    }
    
    /**
     * Obtiene el timeout para pactar una operacion
     * 
     * @return
     */
    protected int getDealTimeOutParam() {
    	try {
			// TODO: Verificar que timeout se esta utilizando, en desarrollo se usa el NETEO_TIMEOUT para divisas frecuentes
			// y el TIMEOUT_PIZ para divisas no frecuentes y metales amonedados
			// TIMEOUT_PIZ
			// NETEO_TIMEOUT
			ParametroSica parametro = getSicaServiceData().findParametro(ParametroSica.TIMEOUT_PIZ);
			if(parametro == null) {
				throw new SicaException("No se encontro el parametro de timeout para el pizarron");
			}
			String strTimeOut = parametro.getValor();
			return Integer.parseInt(strTimeOut);
		} catch (NumberFormatException e) {
			throw new SicaException("No se encontro el parametro de timeout para el pizarron");
		} catch (DataAccessException e) {
			throw new SicaException("Error al consultar parametro de timeout para el pizarron");
		}
    }

	/**
	 * Redirecciona a una pagina de mensaje de error en caso de haber alguno
	 * 
	 * @param cycle
	 * @param message
	 */
	protected void redirectToMessage(IRequestCycle cycle, String message) {
		AutorizaTceHelper.redirectToMessageWithoutMenu(cycle, message);
	}
	
    /**
     * Evalua si el usuario es de la mesa de cambios
     * @return
     */
    protected boolean isMesaCambios() {
    	Visit visit = (Visit) getVisit();
		return AutorizaTceHelper.isMesaCambios(visit);
    }
    
    /**
	 * Compara si la divisa de referencia es Peso.
	 * 
	 * @return boolean.
	 */
    public boolean isDivisaReferenciaPeso() {
    	Divisa divisa = getDivisaReferencia();
    	if(divisa == null) {
    		return false;
    	} else {
    		return Divisa.PESO.equals(divisa.getIdDivisa());
    	}
    }
    
    /**
     * Evalua si el usuario es un promotor
     * @return
     */
    protected boolean isPromocion() {
    	Visit visit = (Visit) getVisit();
    	return AutorizaTceHelper.isPromocion(visit);
    }
    
    /**
     * Obtiene la lista de divisas a presentar en los combos, dependiendo si se trata de la mesa de cambios o de promocion
     * 
     * @return
     */
    protected List getDivisasPorPerfil() {
    	try {
    		debug("getDivisasPorPerfil - (consulta de divisas a BD), ejecutando...");
			List divisas = new ArrayList();
			
			if(isMesaCambios()) {
				divisas = getDivisasParaMesaCambios();
			} else if (isPromocion()) {
				divisas = getSicaServiceData().findDivisasFrecuentes();
			}
			
			return divisas;
		} catch (DataAccessException e) {
			error("Error al traer las divisas", e);
			throw new SicaException("Error al traer las divisas frecuentes");
		}
    }
    
    /**
     * Obtiene las divisas operables para la mesa de cambios
     * 
     * @return
     */
    protected List getDivisasParaMesaCambios() {
    	List divisas = new ArrayList();
        try {
        	// Obtiene las divisas frecuentes
        	// Obtiene las divisas operables para el canal actual, consulta la tabla sc_spread_actual por
        	// el tipo_pizarron asociado al canal actual
            divisas = getSicaServiceData().findDivisasByCanal(((Visit) getVisit()).getIdCanal());
            
            // Obtiene las divisas no frecuentes que tengan asociado un factor divisa y los agrega a la lista 
            List factoresDivisaNF = getSicaServiceData().findDivisasNoFrecuentesFactor();
            for (Iterator it = factoresDivisaNF.iterator(); it.hasNext();) {
                FactorDivisaActual fd = (FactorDivisaActual) it.next();
                divisas.add(fd.getFacDiv().getToDivisa());
            }
            
            // Obtiene los metales amonedados que tengan asociado un factor divisa y los agrega a la lista
            List factoresDivisaMT = getSicaServiceData().findDivisasMetalesFactor();
            for (Iterator it = factoresDivisaMT.iterator(); it.hasNext();) {
                FactorDivisaActual fd = (FactorDivisaActual) it.next();
                divisas.add(fd.getFacDiv().getToDivisa());
            }
        } catch (DataAccessException e) {
        	error("getDivisasParaMesaCambios - Error al obtener divisas: ", e);
        	throw new SicaException("Error al traer las divisas para mesa de cambios");
        }
        return divisas;
    }
    
    /**
     * Obtiene la primer divisa del catalogo
     * 
     * @return
     */
    protected Divisa getFirstDivisa(List divisas) {
    	if(divisas == null) return null;
    	if(!divisas.isEmpty()) {
    		return (Divisa) divisas.get(0);
    	} else {
    		return null;
    	}
    }
    
    /**
     * Obtiene la lista de productos a partir de la divisa
     * 
     * @param divisa
     * @return
     */
    protected String[] getProductosByDivisa(Divisa divisa) {
    	if(divisa == null) return new String[] {};
    	try {
			Canal canal = getSicaServiceData().findCanal(((Visit) getVisit()).getIdCanal());
			if (divisa.isFrecuente()) {
			    List spreadsActuales = getSicaServiceData().findSpreadsActualesByTipoPizarronDivisa(canal.getTipoPizarron(), divisa.getIdDivisa());
			    String[] productos = new String[spreadsActuales.size()];
			    int i = 0;
			    for (Iterator it = spreadsActuales.iterator(); it.hasNext(); i++) {
			        Spread spread = (Spread) it.next();
			        productos[i] = spread.getClaveFormaLiquidacion();
			    }
			    return productos;
			} else if (divisa.isMetalAmonedado()) {
			    return new String[] { "METALS" };
			} else {
			    return new String[] { Constantes.TRANSFERENCIA, Constantes.DOCUMENTO, Constantes.EFECTIVO };
			}
		} catch (DataAccessException e) {
			error("Error al traer productos para la divisa " + divisa.getIdDivisa(), e);
			throw new SicaException("Error al traer productos para la divisa " + divisa.getIdDivisa());
		}
    }
    
    /**
     * Obtiene el catalogo de fechas valor
     * 
     * @return
     */
    protected String[] getCatalogoFechasValor() {
    	try {
			if (getPizarronServiceData().isValorFuturoHabilitado()) {
			    return new String[]{Constantes.CASH, Constantes.TOM, Constantes.SPOT, Constantes.HR72, Constantes.VFUT};
			} else {
			    return new String[]{Constantes.CASH, Constantes.TOM, Constantes.SPOT, Constantes.HR72};
			}
		} catch (DataAccessException e) {
			error("Error al traer las fechas valor", e);
			throw new SicaException("Error al traer las fechas valor");
		}
    }
    
    /**
     * Regresa la divisa de referencia de la mesa del deal.
     *
     * @return Divisa.
     */
    protected Divisa getDivisaReferencia() {
    	try {
			Canal canal = getSicaServiceData().findCanal(((Visit) getVisit()).getIdCanal());
			return canal.getMesaCambio().getDivisaReferencia();
		} catch (DataAccessException e) {
			error("Error al obtener divisa de referencia", e);
			throw new SicaException("Error al obtener divisa de referencia");
			//return null;
		}
    }
    
    /**
     * Obtiene el monto dolarizado dada la divisa y el monto en esta divisa
     * 
     * @param divisa
     * @param monto
     * @return
     */
    public double getMontoUsd(Divisa divisa, double monto) {
        try {
			if (Divisa.DOLAR.equals(divisa.getIdDivisa())) {
			    return monto;
			}
			
			FactorDivisaActual fda = null;
			
			// Si la divisa es no frecuente o metal, el monto dolarizado se calcula obteniendo el monto en MXN
			// y multiplicandolo por el precio referencia actual spot
			// TODO: Verificar que sea correcto este calculo
			if (divisa.isMetalAmonedado() || divisa.isNoFrecuente()) {
				fda = getSicaServiceData().findFactorDivisaActualFromTo(Divisa.PESO, divisa.getIdDivisa());
				if(fda == null || fda.getFacDiv() == null) {
					throw new SicaException("No se encontro factor divisa");
				}
				PrecioReferenciaActual pra = getSicaServiceData().findPrecioReferenciaActual();
				if(pra == null || pra.getPreRef() == null || pra.getPreRef().getPrecioSpot() == null) {
					throw new SicaException("No se encontro precio spot");
				}
				// calculo a cuantos MXN equivale el metal o la divisa no frecuente
				double montoMxn = fda.getFacDiv().getFactor() * monto;
				// multiplico el monto mxn con el pr sport para obtener los dolares
			    return Redondeador.redondear2Dec(montoMxn * pra.getPreRef().getPrecioSpot().doubleValue());
			// Si es divisa frecuente, obtener el factor divisa actual y multiplicarlo por el monto
			} else if (divisa.isFrecuente()) {
				fda = getSicaServiceData().findFactorDivisaActualFromTo(divisa.getIdDivisa(), Divisa.DOLAR);
				if(fda == null || fda.getFacDiv() == null) {
					throw new SicaException("No se encontro factor divisa");
				}
				return Redondeador.redondear2Dec((fda.getFacDiv().getFactor() * monto));
			} else {
				throw new SicaException("Error al calcular monto dolarizado");
			}
		} catch (DataAccessException e) {
			error("Error al calcular monto dolarizado", e);
			throw new SicaException("Error al calcular monto dolarizado");
		}
    }
    
    /**
     * Obtiene el tipo de cambio de la mesa
     * 
     * @param tipoOperacion Tipo de operacion (Compra/Venta)
     * @param divisa Divisa 
     * @param producto Clave de Forma de liquidacion
     * @param fechaValor Fecha valor
     * 
     * @return
     */
    protected double getTipoCambioPizarron(String tipoOperacion, Divisa divisa, String producto, String fechaValor) {
    	if( StringUtils.isEmpty(tipoOperacion) || divisa == null || StringUtils.isEmpty(producto)|| StringUtils.isEmpty(fechaValor) ) {
    		return 0.0;
    	}
    	
    	double tcRef;
    	boolean recibimos = OperacionTceDto.OPERACION_COMPRA.equals(tipoOperacion);
    	
    	try {
			SicaServiceData sd = getSicaServiceData();
			//PrecioReferenciaActual pr = sd.findPrecioReferenciaActual();
			FactorDivisaActual fda;
			
			if (divisa.isMetalAmonedado() || divisa.isNoFrecuente()) {
			    fda = sd.findFactorDivisaActualFromTo(Divisa.PESO, divisa.getIdDivisa());
			    double factorVenta = fda.getFacDiv().getFactor();
			    double factor = recibimos ? factorVenta - fda.getFacDiv().getSpreadCompra() : factorVenta;
			    tcRef = Redondeador.redondear6Dec(factor);
			} else {
			    tcRef = getPizarronServiceData().findPrecioPizarronPesos(
					getSicaServiceData().findCanal("PMY").getTipoPizarron(),
			        divisa.getIdDivisa(), 
			        producto, 
			        recibimos, 
			        fechaValor
			    );
			}
			
			return tcRef;
		} catch (DataAccessException e) {
			error("Error al obtener TC: OPER=" + tipoOperacion 
				+ ", DIV=" + (divisa == null ? null : divisa.getIdDivisa())
				+ ", PROD=" + producto
				+ ", FECHA_VAL=" + fechaValor, e);
			throw new SicaException("Error al obtener el tipo de cambio");
		}
    }
    
    /**
     * Crea un objeto {@link DetalleOperacionTceDto} a partir de un {@link DealDetalle}
     */
    protected DetalleOperacionTceDto createDetalleOperacion(DealDetalle det) {
    	// Inicializa detalle de operacion NO arbitraje
    	DetalleOperacionTceDto detOper = new DetalleOperacionTceDto(
    		det.getDivisa(), //divisa, 
    		det.getClaveFormaLiquidacion(), //producto, 
    		det.getMonto(), //monto, 
    		det.getTipoCambio(), //tcCliente, 
    		det.getTipoCambioMesa(), //tcMesa, 
    		calculaContraimporte(det.getMonto(), det.getTipoCambio()), //contraimporte, 
    		calculaUtilidad(
    			det.isRecibimos() ? OperacionTceDto.OPERACION_COMPRA : OperacionTceDto.OPERACION_VENTA, 
    			det.getTipoCambioMesa(), det.getTipoCambio(), det.getMonto()) //utilidad
    	);
    	return detOper;
    }
    
    /**
     * Recupera el detalle pivote o arbitraje de un deal tce
     * 
     * @param deal Deal del que se extrae el detalle
     * @param arbitraje Indica si se va a extraer el detalle de orbitraje
     * 
     * @return
     */
    protected DealDetalle getDetalleTce(Deal deal, boolean arbitraje) {
    	debug("getDetalleTce - arbitraje: " + arbitraje);
    	List detsTmp = new ArrayList();
    	if(!arbitraje) {
	    	if(deal.isCompra()) {
	    		detsTmp = deal.getRecibimos();
	    	} else {
	    		detsTmp = deal.getEntregamos();
	    	}
    	} else {
    		if(deal.isCompra()) {
    			detsTmp = deal.getEntregamos();
	    	} else {
	    		detsTmp = deal.getRecibimos();
	    	}
    	}
    	
    	// Agrega a la lista de detalles solo aquellos que no esten cancelados
    	List dets = new ArrayList();
    	DealDetalle currentDet = null;
    	for(Iterator it = detsTmp.iterator(); it.hasNext();) {
    		currentDet = (DealDetalle) it.next();
    		if(!currentDet.isCancelado()) {
    			dets.add(currentDet);
    		}
    	}
    	
    	debug("getDetalleTce - detalles size: " + (dets == null ? 0 : dets.size()));
		if(dets == null || dets.size() != 1) {
			_logger.error("getDetalleTce - El deal tce no tiene el numero correcto de detalles ent o rec");
			throw new SicaException("Error al recuperar detalle del deal");
		}
		
		DealDetalle det = (DealDetalle) dets.get(0);
		if( Divisa.PESO.equals(det.getDivisa().getIdDivisa()) ) {
			if(arbitraje) {
				// Si la divisa del detalle obtenido es MXN y se intenta obtener el detalle de arbitraje, 
				// retornar null (no hay detalle de arbitraje)
				return null; 
			} else {
				_logger.error("getDetalleTce - El detalle es MXN");
				throw new SicaException("El detalle no puede ser ser MXN");
			}
		}
    	return det;
    }
    
    /**
     * Inicializa un objeto {@link DetalleOperacionTceDto} con valores por default
     * 
     * @param detOper
     */
    protected void initDetalleOperacion(DetalleOperacionTceDto detOper) {
    	detOper.setContraimporte(0.0);
    	detOper.setDivisa(null);
    	detOper.setMonto(0.0);
    	detOper.setProducto(null);
    	detOper.setTcCliente(0.0);
    	detOper.setTcMesa(0.0);
    	detOper.setUtilidad(0.0);
    }
    
    /**
     * Calcula el monto
     * 
     * @param contraimporte
     * @param tipoCambioCliente
     * @return
     */
    protected double calculaMonto(double contraimporte, double tipoCambioCliente) {
    	if(tipoCambioCliente == 0.0) return 0.0;
    	return Redondeador.redondear2Dec(contraimporte / tipoCambioCliente);
    }
    
    /**
     * Calcula el contraimporte
     * 
     * @param monto
     * @param tipoCambioCliente
     * @return
     */
    protected double calculaContraimporte(double monto, double tipoCambioCliente) {
    	return Redondeador.redondear2Dec(monto * tipoCambioCliente);
    }
    
    /**
     * Calcula el tipo de cambio del cliente
     * 
     * @param monto
     * @param contraimporte
     * @return
     */
    protected double calculaTipoCambioCliente(double monto, double contraimporte) {
    	if(monto == 0.0) return 0.0;
    	return Redondeador.redondear6Dec(contraimporte / monto);
    }
    
    /**
     * Calcula la utilidad
     * 
     * @param tipoOperacion
     * @param tcMesa
     * @param tcCliente
     * @param monto
     * @return
     */
    protected double calculaUtilidad(String tipoOperacion, double tcMesa, double tcCliente, double monto) {
    	double utilidad = 0.0;
    	if(OperacionTceDto.OPERACION_COMPRA.equals(tipoOperacion)) {
    		utilidad = Redondeador.redondear2Dec( (tcMesa - tcCliente) * monto );
    	} else if (OperacionTceDto.OPERACION_VENTA.equals(tipoOperacion)) {
    		utilidad = Redondeador.redondear2Dec( (tcCliente - tcMesa) * monto );
    	}
    	return utilidad;
    }
    
    /**
     * Si se recibe compra se devuelve venta y viceversa
     * 
     * @param tipoOperacion
     * @return
     */
    protected String invierteTipoOperacion(String tipoOperacion) {
    	String tipoOperacionInvertido = "";
    	if(OperacionTceDto.OPERACION_COMPRA.equals(tipoOperacion)) {
    		tipoOperacionInvertido = OperacionTceDto.OPERACION_VENTA;
    	} else if (OperacionTceDto.OPERACION_VENTA.equals(tipoOperacion)) {
    		tipoOperacionInvertido = OperacionTceDto.OPERACION_COMPRA;
    	}
    	return tipoOperacionInvertido;
    }
    
    /**
     * Obtiene el usuario actual
     * 
     * @return
     */
    protected IUsuario getUsuarioActualConPersona() {
    	Visit visit = (Visit) getVisit();
		return AutorizaTceHelper.getUsuarioActualConPersona(visit);
    }
    
    /**
     * Obtiene el catalogo de sistemas que operan TCE
     * 
     * @return
     */
    protected List getSistemasTce() {
    	try {
    		debug("getSistemasTce - Consultando sistemas en BD");
			List sistemaList = getSicaServiceData().getAllSistemaTce();
			return sistemaList;
		} catch (DataAccessException e) {
			error("getSistemasTce - Error al cargar catalogo de sistemas", e);
			throw new SicaException("Error al obtener el catalogo de sistemas");
		}
    }
    
    /**
     * Obtiene el nombre del promotor dada su clave de usuario
     * 
     * @param claveUsuario
     * @return
     */
    protected EmpleadoIxe findNombrePromotor(String claveUsuario) {
    	if(claveUsuario == null) return null;
    	try {
			EmpleadoIxe empleadoIxe = getSicaServiceData().findPromotorSicaByClave(claveUsuario);
			return empleadoIxe;
		} catch (DataAccessException e) {
			error("Error al buscar promotor, cveUser: " + claveUsuario, e);
			throw new SicaException("Error al buscar promotor");
		}
    }
    
    /**
     * Retorna el cliente correspondiente al contrato corto
     * 
     * @param contratoCorto
     * @return
     */
    protected ClienteContratoInfoPromotorDto findClienteByContratoCorto(String contratoCorto) {
    	try {
			BusquedaClientesService busquedaService = (BusquedaClientesService) getApplicationContext().getBean("busquedaClientesService");
			if(NumberUtils.isDigits(contratoCorto)) {
				return busquedaService.findClienteByContratoCorto(NumberUtils.createInteger(contratoCorto));
			} else {
				return null;
			}
		} catch (DataAccessException e) {
			error("findClienteByContratoCorto - Error al buscar contrato corto " + contratoCorto, e);
			throw new SicaException("Error al buscar contrato corto");
		}
    }
    
    /**
     * Busca el contrato corto a partir del contrato sica
     * 
     * @param contratoSica
     * @return
     */
    protected ClienteContratoInfoPromotorDto findContratoCortoByContratoSica(String contratoSica) {
    	try {
			BusquedaClientesService busquedaService = (BusquedaClientesService) getApplicationContext().getBean("busquedaClientesService");
			if(StringUtils.isEmpty(contratoSica)) {
				return null;
			} else {
				return busquedaService.findContratoCortoByContratoSica(contratoSica);
			}
		} catch (DataAccessException e) {
			error("Error al buscar el contrato corto por contrato sica " + contratoSica, e);
			throw new SicaException("Error al buscar contrato corto mediante el contrato sica");
		}
    }
    
    /**
     * Calcula la fecha de liquidacion a partir del tipoValor
     * 
     * @param tipoValor
     * @return
     */
    protected Date calculaFechaLiquidacion(String tipoValor) {
    	if(StringUtils.isEmpty(tipoValor)) return null;
    	try {
			if(Constantes.CASH.equals(tipoValor)) {
				return getFechaOperacion();
			} else if (Constantes.TOM.equals(tipoValor)) {
				return getFechaTOM();
			} else if (Constantes.SPOT.equals(tipoValor)) {
				return getFechaSPOT();
			} else if (Constantes.HR72.equals(tipoValor)) {
				return getFecha72HR();
			} else if (Constantes.VFUT.equals(tipoValor)) {
				return getFechaVFUT();
			}
			return null;
		} catch (DataAccessException e) {
			error("Error al calcular fecha de liquidacion", e);
			throw new SicaException("Error al calcular fecha de liquidacion");
			//return null;
		}
    }
 
    /**
     * Actualiza en la seccion de NO arbitraje, los campos monto, contraimporte y/o tipo de cambio cliente depeniendo del que haya cambiado
     * 
     * @param actualizaPor Especificar si la actualizacion es por monto, contraimporte o tipo operacion
     * @param detOper Detalle de la operacion a actualizar
     * @param tipoOperacion Tipo de operacion
     * 
     * Si cambia monto
     * 		recalcula contraimporte
     * Si cambia contraimporte
     * 		recalcula monto
     * 		copia contraimporte a contraimporte liq y actualiza calculos arbitraje
     * Si cambia tcc
     * 		recalcula contraimporte
     * Si cambia tcm
     * 		recalcula utilidad
     */
    protected void actualizaCalculos(int actualizaPor, DetalleOperacionTceDto detOper, String tipoOperacion) {
    	if( actualizaPor == ACTUALIZA_POR_MONTO ) { // Actualizar por cambio en monto
    		if(detOper.getTcCliente() != 0.0) { // si tengo tcc calcula contraimp 
    			detOper.setContraimporte(calculaContraimporte(detOper.getMonto(), detOper.getTcCliente()));
    		} else if(detOper.getContraimporte() != 0.0) { // si no tengo tcc pero si tengo contraimp, calcula tcc
    			actualizaCalculoTcCliente(detOper);
    		}
        	debug("Recalcula por cambio MONTO: [MONTO=" + detOper.getMonto() 
        		+ ", CONTRAIMP=" + detOper.getContraimporte() + ", TCC=" + detOper.getTcCliente() + "]");
    	} else if ( actualizaPor == ACTUALIZA_POR_CONTRAIMPORTE ) { // Actualizar por cambio en contraimporte
    		if(detOper.getTcCliente() != 0.0) { // si tengo tcc calcula monto
    			detOper.setMonto(calculaMonto(detOper.getContraimporte(), detOper.getTcCliente()));
    		} else if (detOper.getMonto() != 0.0) { // si no tengo tcc per si tengo monto, calcula tcc
    			actualizaCalculoTcCliente(detOper);
    		}
    		debug("Recalcula por cambio CONTRAIMP: [MONTO=" + detOper.getMonto() 
    			+ ", CONTRAIMP=" + detOper.getContraimporte() + ", TCC=" + detOper.getTcCliente() + "]");
    	} else if ( actualizaPor == ACTUALIZA_POR_TCC ) { // Actualizar por cambio en tipo de cambio cliente
    		if(detOper.getMonto() != 0.0) { // si tengo monto, calcula contraimporte
    			detOper.setContraimporte(calculaContraimporte(detOper.getMonto(), detOper.getTcCliente()));
    		} else if (detOper.getContraimporte() != 0.0) { // si no tengo monto, pero si tengo contraimp, calcula monto
    			detOper.setMonto(calculaMonto(detOper.getContraimporte(), detOper.getTcCliente()));
    		}
    		debug("Recalcula por cambio TCC: [MONTO=" + detOper.getMonto() 
    			+ ", CONTRAIMP=" + detOper.getContraimporte() + ", TCC=" + detOper.getTcCliente() + "]");
    	}
    	
    	// actualiza la utilidad
    	detOper.setUtilidad( calculaUtilidad(tipoOperacion, detOper.getTcMesa(), detOper.getTcCliente(), detOper.getMonto()) );
    	debug("Utilidad calculada: " + detOper.getUtilidad() + "[" + tipoOperacion 
    		+ ", TCM=" + detOper.getTcMesa() + ", TCC=" + detOper.getTcCliente() + ", MNT=" + detOper.getMonto() + "]");
    	
    	if ( actualizaPor == ACTUALIZA_POR_TCM ) { // Actualiza por tcm
    		// Solo actualiza la utilidad (ya se hizo arriba) y sale
    		debug("Actualiza calculos por cambio en TC Mesa ...");
    		return;
    	}
    }
    
    /**
     * Actualiza el tipo de cambio de la mesa, y tc cliente
     * 
     * Se ejecuta cuando ha cambiado Operacion, Fecha Valor, Divisa o Producto
     */
    protected void actualizaTipoCambio(String tipoOperacion, String fechaValor, DetalleOperacionTceDto detOper) {
    	debug("submit(): Actualizar TC: [" 
    		+ tipoOperacion + ", " 
    		+ fechaValor + ", " 
    		+ (detOper.getDivisa() == null ? null : detOper.getDivisa().getIdDivisa()) + ", "
    		+ detOper.getProducto() + "]"
    	);
    	
    	double tcMesa = getTipoCambioPizarron(
    		tipoOperacion, 
    		detOper.getDivisa(), 
    		detOper.getProducto(), 
    		fechaValor
    	);
    	
    	detOper.setTcMesa(tcMesa);
    	
    	//Se elimina la actualizacion del TC Cliente, se debe conservar el capturado por el usuario
    	//detOper.setTcCliente(tcMesa);
    	
    	// Al actualizar los tipos de cambio, se resetea el estatus de la confirmacion de desviacion de tc
    	detOper.setDesvTccConfirmStatus(DetalleOperacionTceDto.DESV_TC_NOT_DETECTED);
    	detOper.setDesvTcmConfirmStatus(DetalleOperacionTceDto.DESV_TC_NOT_DETECTED);
    }

    /**
     * Actualiza el calculo de TC cliente NO arbitraje solo si monto y contra importe son distintos de 0, esto para evitar que se
     * borre un valor previamente obtenido del pizarron, por cambio en valores de monto o contraimporte irrelevantes
     */
    private void actualizaCalculoTcCliente(DetalleOperacionTceDto detOper) {
    	if(detOper.getMonto() == 0.0 || detOper.getContraimporte() == 0.0) {
    		return;
    	}
    	detOper.setTcCliente(calculaTipoCambioCliente(detOper.getMonto(), detOper.getContraimporte()));
    	// Al actualizar el tcc, se resetea el estatus de la confirmacion de desviacion de tc
    	detOper.setDesvTccConfirmStatus(DetalleOperacionTceDto.DESV_TC_NOT_DETECTED);
    }
    
    /**
     * Obtiene un parametro tipo double para tipo de cambio especial, redondea a 2 decimales
     * 
     * @param idParametro
     * @return
     */
    protected double getParametroDouble(String idParametro) { 
    	try {
			ParametroSica parametro = getSicaServiceData().findParametro(idParametro);
			return Double.parseDouble(parametro.getValor());
		} catch (NumberFormatException e) {
			error("getTceParametro - Error al parsear parametro double", e);
			throw new SicaException("Error al obtener parametro");
		} catch (DataAccessException e) {
			error("getTceParametro - Error al obtener parametro", e);
			throw new SicaException("Error al obtener parametro");
		}
    }
    
    /**
     * Regresa el logaritmo base 10 de x.
     *
     * @param x El valor a calcular.
     * @return double.
     */
    protected double log(double x) {
        return Math.log(x) / Math.log(Consts.NUMD_10);
    }

	/**
     * Regresa la lista de formas de liquidaci&oacute;n para recibimos o para entregamos, ordenadas por
     * mnem&oacute;nico. Filtra las formas que no sean desplegables en el sica y las de SPEI para Deals interbancarios.
     *
     * @return List.
     */
    private List obtenerFormasLiquidacion(boolean isRecibimos) {
		List formasLiquidacion = getFormasPagoLiqService().getProductosNoPizarron(getTicket(), isRecibimos, null, null);
		for (Iterator it = formasLiquidacion.iterator(); it.hasNext();) {
		    FormaPagoLiq fpl = (FormaPagoLiq) it.next();
		    if (!fpl.getDesplegableSica().booleanValue()
		            || !Constantes.MNEMONICO_ACTIVO.equals(fpl.getStatus())
		            || ("SPEI".equals(fpl.getClaveTipoLiquidacion().trim())
		                && (!(fpl.getMnemonico().indexOf("MXNSPEIIXEB") >= 0)
		                && !(fpl.getMnemonico().indexOf("BCTC") >= 0)))) {
		        it.remove();
		    }
		}
		return formasLiquidacion;
    }
    
    /**
     * Retorna la forma de liquidacion tipo BALNETEO para usar su monto maximo en la validacion de desbalanceo
     * 
     * @param isRecibimos
     * @return
     */
    protected FormaPagoLiq getFormaLiqBalneteo(boolean isRecibimos) {
    	try {
			List formasLiq = new ArrayList();
			List formasLiquidacionTmp = obtenerFormasLiquidacion(isRecibimos);
			for (Iterator it = formasLiquidacionTmp.iterator(); it.hasNext();) {
				FormaPagoLiq fpl = (FormaPagoLiq) it.next();
				if (fpl.getNombreTipoLiquidacion().trim().equals(TIPO_LIQ_NETEO)) {
					formasLiq.add(fpl);
				}
			}
			if(_logger.isDebugEnabled()) {
				debug("getFormaLiqBalneteo - Formas liquidacion obtenidas: " + (formasLiq == null ? 0 : formasLiq.size()) );
				for(Iterator it = formasLiq.iterator(); it.hasNext();) {
					FormaPagoLiq currentFormaPagoLiq = (FormaPagoLiq) it.next();
					debug("getFormaLiqBalneteo - [cveFormaLiq=" + currentFormaPagoLiq.getClaveFormaLiquidacion() 
						+ "cveTipoLiq=" + currentFormaPagoLiq.getClaveTipoLiquidacion()
						+ "descripcion=" + currentFormaPagoLiq.getDescripcion()
						+ "mnemonico=" + currentFormaPagoLiq.getMnemonico()
						+ "montoMax=" + currentFormaPagoLiq.getMontoMaximo()
					);
				}
			}
			
			if(formasLiq != null && !formasLiq.isEmpty()) {
				FormaPagoLiq formaPagoLiq = (FormaPagoLiq) formasLiq.get(0);
				return formaPagoLiq;
			} else {
				return null;
			}
		} catch (RuntimeException e) {
			error("getFormaLiqBalneteo - Error al obtener mnemonico de balneteo para validar desbalanceo", e);
			return null;
		}
    }
    
    /**
     * Levanta una excepci&oacute;n Si el contrato no tiene asignado un ejecutivo o si el promotor
     * asignado se encuentra en la jerarqu&iacute;a de usuarios del sica y el promotor asignado no
     * pertenece al &aacute;rea de Ixe Directo.
     *
     * @param params Los par&aacute;metros del servicio de la petici&oacute;n.
     */
    protected boolean esIxeDirectoValido(String noCta, String nombreCliente) {
		try {
			SicaServiceData ssd = getSicaServiceData();
			EmpleadoIxe promotor = ssd.findPromotorByContratoSica(noCta);
			List promotoresIxeDirecto = ssd.findEjecutivosIxeDirecto();
			for (Iterator it = promotoresIxeDirecto.iterator(); it.hasNext();) {
			    Persona empleadoIxe = (Persona) it.next();
			    if (promotor.getIdPersona().equals(empleadoIxe.getIdPersona())) {
			        return true;
			    }
			}
			List promotoresJerarquia = getSicaServiceData().findAllPromotoresSICA(
			        ((SupportEngine) getEngine()).getApplicationName());
			for (Iterator it = promotoresJerarquia.iterator(); it.hasNext();) {
			    EmpleadoIxe empleadoIxe = (EmpleadoIxe) it.next();
			    if (empleadoIxe.getIdPersona().equals(promotor.getIdPersona())) {
			    	getDelegate().record("Por favor contacte al promotor " + promotor.getNombreCompleto() 
			    		+ " para que sea \u00e9l quien atienda al cliente " + nombreCliente + " con Contrato SICA " 
			    		+ noCta + ".", null);
			        return false;
			    }
			}
			// Si llega a este punto, el contrato est&aacute; asignado a alguien que no opera el sica,
			// entonces el ejecutivo de ixe directo puede operar el contrato.
			return true;
		} catch (DataAccessException e) {
			error("Error al validar ixe directo", e);
			throw new SicaException("Error al validar IXE Directo");
            //return false;
		}
    }

    /**
     * Valida el contrato corto
     * 
     * @param contratoCorto
     * @param clavePromotor
     * @return
     */
    protected boolean esContratoCortoValido(String contratoCorto, String clavePromotor) {
    	debug("esContratoCortoValido: contratoCorto=" + contratoCorto);
    	if(!NumberUtils.isDigits(contratoCorto)) {
    		getDelegate().record("El contrato conrto debe ser num\u00E9rico", null);
    		return false;
    	}
    	ClienteContratoInfoPromotorDto clienteContrato = findClienteByContratoCorto(contratoCorto);
    	if(clienteContrato == null) {
    		getDelegate().record("Contrato corto no encontrado", null);
    		return false;
    	}
    	if(!clienteContrato.getClaveUsuarioEjecutivo().equals(clavePromotor)) {
    		getDelegate().record("El contrato corto no esta asignado al promotor", null);
    		return false;
    	}
    	
    	String contratoSica = clienteContrato.getNoCuenta();
    	String nombreCliente = clienteContrato.getNombreCorto();
    	debug("esContratoCortoValido: contratoSica=" + contratoSica + ", nombreCliente=" + nombreCliente);
    	
    	// Si es de ixe directo, el cliente esta asignado a un ejecutivo y este se encuentra
        // en la Jerarquia del SICA, no debe poder operarlo:
        if (((Visit)getVisit()).isIxeDirecto()) {
        	if( !esIxeDirectoValido(contratoSica, nombreCliente) ) {
        		return false;
        	}
        }
    	
    	return true;
    }

    /**
     * Valida el promotor
     * 
     * @param claveUsuarioPromotor
     * @return
     */
    protected boolean esPromotorValido(String claveUsuarioPromotor) {
    	// El campo promotor es el unico requerido, llenando este, se limpian los datos del cliente
    	// llenando cliente, se llena promotor
    	if( StringUtils.isEmpty(claveUsuarioPromotor) ) {
    		getDelegate().record("El promotor es requerido", null);
    		return false;
    	} else {
    		EmpleadoIxe lPromotor = findNombrePromotor(claveUsuarioPromotor);
    		if(lPromotor == null) {
    			getDelegate().record("Promotor no encontrado", null);
    			return false;
    		}
    	}
    	return true;
    }
    
    /**
     * Valida productos en restriccion, usar solo si no se especifico contrato corto
     * 
     * @param producto
     * @return
     */
    protected boolean esProductoEnRestriccion(String producto) {
    	try {
			for (Iterator it = getSicaServiceData().getProductosEnRestriccion().iterator();it.hasNext();) {
				String productoEnRestriccion = (String) it.next();
				if (productoEnRestriccion.equals(producto)) {
					throw new SicaException("No es posible capturar " +
						productoEnRestriccion + " hasta que el deal cuente con un contrato asociado.");
				}
			}
			return false;
		} catch (DataAccessException e) {
			error("esProductoEnRestriccion, error al validar producto: " + producto, e);
			throw new SicaException("Error al validar productos en restriccion");
			//return true;
		}
    }
    
    /**
     * Valida la restriccion de venta de cheques de viajero, se debe llamar solo si no se asigno contrato corto
     * 
     * @param tipoOperacion
     * @param producto
     * @return
     */
    protected boolean esVentaChviaj(String tipoOperacion, String producto) {
    	if (OperacionTceDto.OPERACION_VENTA.equals(tipoOperacion) && Constantes.TRAVELER_CHECKS.equals(producto)) {
            throw new SicaException("No es posible capturar Venta de Cheques de " +
                    "Viajero hasta que el deal cuente con un contrato asociado.");
        }
    	return false;
    }
    
    /**
     * Valida el timeout de pactacion de un deal solo si es promocion
     * 
     * @param dealInputIniTime momento en el que empezo la captura
     * @param esArbitraje Es arbitraje o no
     * @return
     */
    public boolean esTiempoCapturaValido(OperacionTceDto operTce) {
    	if(isPromocion()) {
    		if(operTce.getDealInputIniTime() == null) {
    			return false;
    		}
    		long maxTime = getDealTimeOutParam()*1000;
    		if(operTce.isArbitraje()) {
    			maxTime = maxTime * 2;
    		}
	    	long usedTime = new Date().getTime() - operTce.getDealInputIniTime().getTime();
	    	// Si se alcanzo timeout retorna falso
	    	if( usedTime > maxTime ) {
	    		return false; 
	    	}
    	}
    	return true;
    }
    
    public boolean tcTimeOut(OperacionTceDto operTce) {
    	if(isPromocion()) {
    		if(operTce.getDealInputIniTime() == null) {
    			return false;
    		}
    		long maxTime = getDealTimeOutParam()*1000;
    		if(operTce.isArbitraje()) {
    			maxTime = maxTime * 2;
    		}
	    	long usedTime = new Date().getTime() - operTce.getDealInputIniTime().getTime();
	    	// Si se alcanzo timeout retorna falso
	    	if( usedTime > maxTime ) {
	    		return false; 
	    	}
    	}
    	return true;
    }
    
    /**
     * Valida un detalle pivote contra un detalle de arbitraje, si las divisas son iguales y los productos son iguales, la combinacion
     * no es valida
     * 
     * @param detPiv
     * @param detArb
     * @return
     */
    protected boolean sonDivisasYProductosValidos(DetalleOperacionTceDto detPiv, DetalleOperacionTceDto detArb) {
    	if(detPiv == null || detArb == null) return false;
    	if(detPiv.getDivisa() == null || detPiv.getDivisa().getIdDivisa() == null || detArb.getDivisa() == null 
    	|| detPiv.getProducto() == null || detArb.getProducto() == null) {
    		return false;
    	}
    	if(detPiv.getDivisa().getIdDivisa().equals(detArb.getDivisa().getIdDivisa())) {
    		if(detPiv.getProducto().equals(detArb.getProducto())) {
    			getDelegate().record("Las divisas son iguales, los productos deben ser distintos", null);
    			return false;
    		}
    	}
    	return true;
    }
    
    /**
     * Define si el sistema se encuentra en Horario Vespertino.
     *
     * @return boolean
     */
    protected boolean isHorarioVespertino() {
        return getEstadoActual().getIdEstado() == Estado.ESTADO_OPERACION_VESPERTINA;
    }
    
    /**
     * Inicializa los datos generales del deal, usuario, tipoDeal, canalMesa, grupoTrabajo y asigna contrato sica
     * 
     * @param operacionTce
     */
    protected Deal inicializaDeal(OperacionTceDto operacionTce) {
    	try {
			Deal deal = new Deal();
			deal.setUsuario(((Visit) getVisit()).getUser());
			deal.setTipoDeal(Deal.TIPO_SIMPLE);
			
			//El deal se asigna al canal del promotor para el cual se hace la captura	
			asignarCanalPromotorADeal(deal, operacionTce);
		
			deal.setGrupoTrabajo(((Visit) getVisit()).getGrupoTrabajo());
			
			// Si se ha capturado el contrato corto, asignar cliente al deal
        	if( !StringUtils.isEmpty(operacionTce.getContratoCorto()) ) {
        		asignaContratoDeal(operacionTce.getContratoCorto(), deal);
        	} else if ( !StringUtils.isEmpty(operacionTce.getUsuarioPromotor()) ) {
        		deal.setPromotor(getSicaServiceData().findPromotorSicaByClave(operacionTce.getUsuarioPromotor()));
        	}
        	
        	deal.setSistemaTce(operacionTce.getSistema());
        	deal.setEspecial(SiNoType.TRUE);
        	
        	// Si es la mesa de cambios insertar con estatus especial TC Autorizado
        	// si es promocion, no hay estatus especial
        	if(isMesaCambios()) {
        		deal.setEstatusEspecial(Deal.STATUS_ESPECIAL_TC_AUTORIZADO);
        	} else if (isPromocion()) {
        		deal.setEstatusEspecial(null);
        	}
        	
        	deal.setObservaciones(operacionTce.getObservaciones());
			
        	debug("inicializaDeal - Deal inicializado");
			return deal;
		} catch (DataAccessException e) {
			error("Error al inicializar el deal", e);
			throw new SicaException("Error al inicializar deal");
		}
    }
    
    /**
     * Determina el canal del promotor mediante sus facultades y lo asigna
     * al Deal. Si el promotor tiene m&aacute;n de un canal, se toma el primero
     * o el correspondiente a la mesa de operaci&oacute;n
     * 
     * @param deal
     * @param operacionTce
     */
    private void asignarCanalPromotorADeal(Deal deal, OperacionTceDto operacionTce){
    	
    	IUsuario usuarioSeguridad = getSeguridadServiceData().
    			findIdUsuarioByUserNameAndTypeSystem(operacionTce.getUsuarioPromotor(), TipoSistema.INTERNO);
		
		List facultadesUsuario = getSeguridadServiceData().findFacultadesSimplesByUserAndSystem(
				usuarioSeguridad.getIdUsuario(), getApplicationName());
		
        List canales = new ArrayList();
        CanalMesa cm = null; 
        ParametroSica psMO = (ParametroSica) getSicaServiceData().find(ParametroSica.class,
                ParametroSica.MESA_OPERACION);
        for (Iterator iterator = facultadesUsuario.iterator(); iterator.hasNext();) {
            String nombreFacultad = (String) iterator.next();
            if (nombreFacultad.startsWith("SICA_CAN")) {
                Canal canal = getSicaServiceData().findCanalByFacultad(nombreFacultad.trim());
                if (canal != null) {
                    canales.add(canal);
                    if (canales.size() == 1) {     	
                    	cm = new CanalMesa(canal, canal.getMesaCambio());
                    }
                    else {
                        if (Integer.parseInt(psMO.getValor()) ==
                                canal.getMesaCambio().getIdMesaCambio()) {
                        	cm = new CanalMesa(canal, canal.getMesaCambio());
                        }
                    }
                }
            }
        }
        
        if(cm == null){
        	throw new SicaException("No es posible determinar el canal del promotor para generar el Deal.");
        }
		
		deal.setCanalMesa(cm);
    }
    
    /**
     * Asigna el cliente al deal
     * 
     * @param contratoCorto
     * @param deal
     */
    protected void asignaContratoDeal(String contratoCorto, Deal deal) {
    	try {
			ClienteContratoInfoPromotorDto clienteContrato = findClienteByContratoCorto(contratoCorto);
			if(clienteContrato != null) {
				ContratoDireccionesService cds = (ContratoDireccionesService) getApplicationContext().getBean("contratoDireccionesService");
				cds.asignarContrato(clienteContrato.getNoCuenta(), deal, getPizarronServiceData().isValorFuturoHabilitado());
			}
		} catch (DataAccessException e) {
			error("Error al asignar cliente", e);
			throw new SicaException("Error al asignar cliente al deal");
		}
    }
    
    /**
     * Regresa los l&iacute;mites de restricci&oacute;n de operaci&oacute;n del contrato sica
     * que se encuentra operando.
     *
     * @param deal El deal a operar.
     * @return LimitesRestriccionOperacion.
     */
    protected LimitesRestriccionOperacion getLimitesActualizados(Deal deal) {
    	LimitesRestriccionOperacion limitesRestriccionOperacion = null;
    	String noCuenta = deal.getContratoSica() != null ? deal.getContratoSica().getNoCuenta() :
    		null;
    	if (noCuenta == null) {
    		return null;
    	}
    	else if (limitesRestriccionOperacion == null ||
    			!limitesRestriccionOperacion.getNoCuenta().equals(noCuenta)) {
    		//ComplementoDatos complDatos = getSicaServiceData().
    				//findComplementoDatosByNoCuenta(noCuenta);
    		LimitesRestriccionOperacion limRestOper = new LimitesRestriccionOperacion();
    		if (deal.getCliente().getPais() == null) {
				throw new SicaException("No se puede realizar la operaci\u00f3n, por " +
    					"falta de documentaci\u00f3n requerida en el M\u00f3dulo de " +
    					"Contrataci\u00f3n.\n" + "No se puede identificar la nacionalidad del" +
    					" cliente.");
			}
    		
   		limRestOper.setCliente(getSicaServiceData().isClienteOrUsuario(deal.
    				getCliente().getIdPersona()) ? SiNoType.TRUE : SiNoType.FALSE);

   		//limRestOper.setCliente(SiNoType.TRUE);
    				
    		limRestOper.setNacionalidad(deal.getCliente().getPais().getIdPais());
    		limRestOper.setNoCuenta(noCuenta);
    		
    		PersonaListaBlanca plb = getSicaServiceData().findPersonaListaBlanca(noCuenta);
    			
    		ContratoDireccionesService cds = (com.ixe.ods.sica.services.ContratoDireccionesService)
    				getApplicationContext().getBean("contratoDireccionesService");
    		if (deal.getCliente().getTipoPersona().equals("PM")) {
    					
    			if(limRestOper.getCliente().equals(SiNoType.FALSE)){

        			limRestOper.setListaLimitesNormales(getSicaServiceData().
        						findLimitesRestriccionOperacion(deal.getCliente().getTipoPersona(),
        								new Boolean(limRestOper.getCliente().equals(SiNoType.TRUE)),
        								null,
        								new Boolean(limRestOper.getNacionalidad().equals(String.
        										valueOf(Num.I_48)))));
        		    deal.setTipoZona(null);
    			} 
    			//Es Cliente
    			else{ 

    			Direccion dirFiscalPersona = cds.getDireccionFiscalParaPersona(deal.getCliente().
    					getIdPersona().intValue());
    			if (dirFiscalPersona == null) {
    				throw new SicaException("No se puede realizar la operaci\u00f3n, por " +
    						"falta de documentaci\u00f3n requerida en el M\u00f3dulo de " +
    						"Contrataci\u00f3n.\n" + "No se puede identificar la direcci\u00f3n " +
    						"fiscal del cliente.");
    			}
    			String codigoPostalCliente = dirFiscalPersona.getCodigoPostal();
    			
    			
    			if (codigoPostalCliente == null || codigoPostalCliente.equals("")) {
    				throw new SicaException("No se puede realizar la operaci\u00f3n, por " +
    						"falta de documentaci\u00f3n requerida en el M\u00f3dulo de " +
    						"Contrataci\u00f3n.\n" + "El cliente no cuenta con un c\u00f3digo postal" +
    				" asociado.");
    			}
    			String codigoPostalSucursal = "";
    			if (!(deal.getCanalMesa().getCanal().getSucursal() != null && deal.getCanalMesa().
    					getCanal().getSucursal().getAddress() != null && (deal.getCanalMesa().
    							getCanal().getSucursal().getAddress().getCodigoPostal() != null ||
    							!deal.getCanalMesa().getCanal().getSucursal().getAddress().
    							getCodigoPostal().equals("")))) {
    				codigoPostalSucursal = deal.getCanalMesa().getCanal().getCodigoPostal();
    			}
    			else {
    				codigoPostalSucursal = deal.getCanalMesa().getCanal().getSucursal().getAddress().
    				getCodigoPostal();
    			}
    			if (codigoPostalSucursal == null || codigoPostalSucursal.equals("")) {
    				throw new SicaException("No se puede realizar la operaci\u00f3n, por " +
    						"falta de documentaci\u00f3n requerida en el M\u00f3dulo de " +
    						"Contrataci\u00f3n.\n" + "La sucursal no cuenta con un c\u00f3digo postal" +
    				" asociado.");
    			}
    			CodigoPostalListaBlanca codPostLBCliente = getSicaServiceData().
    			findCodigoPostalListaBlanca(codigoPostalCliente, true);
    			CodigoPostalListaBlanca codPostLBSucursal = getSicaServiceData().
    			findCodigoPostalListaBlanca(codigoPostalSucursal, false);
    			boolean zonaTuristica = false;
    			boolean zonaFronteriza = false;
    			if (codPostLBCliente != null && codPostLBSucursal != null &&
    					!codPostLBCliente.equals("") && !codPostLBSucursal.equals("")) {
    				zonaTuristica = codPostLBCliente.getZonaTuristica().booleanValue() &&
    				codPostLBSucursal.getZonaTuristica().booleanValue();
    				zonaFronteriza = codPostLBCliente.getZonaFronteriza().booleanValue() &&
    				codPostLBSucursal.getZonaFronteriza().booleanValue();
    			}
    			// Se obtiene la lista para las zonas que aplique
    			String zona = Constantes.ZONA_NORMAL;
    			if (!zonaTuristica && ! zonaFronteriza) {
    				// Zona Normal
    				limRestOper.setListaLimitesNormales(getSicaServiceData().
    						findLimitesRestriccionOperacion(deal.getCliente().getTipoPersona(),
    								new Boolean(limRestOper.getCliente().equals(SiNoType.TRUE)),
    								Constantes.ZONA_NORMAL,
    								new Boolean(limRestOper.getNacionalidad().equals(String.
    										valueOf(Num.I_48)))));
    			} else {
    				List listaZonaTuristica = null;
    				List listaZonaFronteriza = null;
    				if (zonaTuristica) {
    					// Zona Turistica
    					listaZonaTuristica =
    						getSicaServiceData().
    						findLimitesRestriccionOperacion(
    								deal.getCliente().getTipoPersona(),
    								new Boolean(limRestOper.getCliente().equals(SiNoType.TRUE)),
    								Constantes.ZONA_TURISTICA,
    								new Boolean(limRestOper.getNacionalidad().equals(String.
    										valueOf(Num.I_48))));
    				}
    				if (zonaFronteriza) {
    					// Zona Fronteriza
    					listaZonaFronteriza =
    						getSicaServiceData().
    						findLimitesRestriccionOperacion(
    								deal.getCliente().getTipoPersona(),
    								new Boolean(limRestOper.getCliente().equals(SiNoType.TRUE)),
    								Constantes.ZONA_FRONTERIZA,
    								new Boolean(limRestOper.getNacionalidad().equals(String.
    										valueOf(Num.I_48))));
    				}
    				if (listaZonaTuristica != null && listaZonaFronteriza != null) {
    					// Se toma el limite mayor.
    					for (Iterator i = listaZonaTuristica.iterator(); i.hasNext(); ) {
    						LimiteEfectivo limiteTur = (LimiteEfectivo) i.next();
    						for (Iterator j = listaZonaFronteriza.iterator(); j.hasNext(); ) {
    							LimiteEfectivo limiteFro = (LimiteEfectivo) j.next();
    							if (limiteTur.getDivisa().equals(limiteFro.getDivisa())) {
    								if (limiteTur.getLimiteMensual().doubleValue() >
    								limiteFro.getLimiteMensual().doubleValue()) {
    									// Zona turistica es mayor
    									limRestOper.setListaLimitesNormales(listaZonaTuristica);
    									zona = Constantes.ZONA_TURISTICA;
    								}
    								else if (limiteTur.getLimiteMensual().doubleValue() <
    										limiteFro.getLimiteMensual().doubleValue()) {
    									// zona fronteriza es mayor
    									limRestOper.setListaLimitesNormales(listaZonaFronteriza);
    									zona = Constantes.ZONA_FRONTERIZA;
    								}
    							}
    						}
    					}
    					if (limRestOper.getListaLimitesNormales() == null) {
    						// Si todas las reglas son iguales, se toma zona turistica
    						limRestOper.setListaLimitesNormales(listaZonaTuristica);
    						zona = Constantes.ZONA_TURISTICA;
    					}
    				}
    				else if (listaZonaTuristica != null) {
    					// Se toma el limite de zona turistica
    					limRestOper.setListaLimitesNormales(listaZonaTuristica);
    					zona = Constantes.ZONA_TURISTICA;
    				}
    				else if (listaZonaFronteriza != null) {
    					// Se toma el limite se zona fronteriza
    					limRestOper.setListaLimitesNormales(listaZonaFronteriza);
    					zona = Constantes.ZONA_FRONTERIZA;
    				}
    			}
    			
    			deal.setTipoZona(zona);

    		 }//Es Cliente
    		}
    		else {
    			//INICIA PF
    			limRestOper.setListaLimitesNormales(getSicaServiceData().
    						findLimitesRestriccionOperacion(deal.getCliente().getTipoPersona(),
    								new Boolean(limRestOper.getCliente().equals(SiNoType.TRUE)),
    								null,
    								new Boolean(limRestOper.getNacionalidad().equals(String.
    										valueOf(Num.I_48)))));
    		    deal.setTipoZona(null);
    			//TERMINA
    		}
    		
    		
			if (plb != null) {
				limRestOper.setLimiteDiarioIxeCpra(plb.getLimiteDiario().doubleValue());
				limRestOper.setLimiteMensualIxeCpra(plb.getLimiteMensual().doubleValue());
				limRestOper.setTipoExcepcion(plb.getTipoExcepcion());
			} 
			else {
				limRestOper.setLimiteDiarioIxeCpra(0);
				limRestOper.setLimiteMensualIxeCpra(0);
				limRestOper.setTipoExcepcion(PersonaListaBlanca.SIN_EXCEPCION);
			}
			deal.setTipoExcepcion(limRestOper.getTipoExcepcion());
			deal.setEsCliente(limRestOper.getCliente());
			limitesRestriccionOperacion = limRestOper;
		}
		return limitesRestriccionOperacion;
	}
    
    /** Identifica una actualizacion de calculos por cambio en monto */
    public static final int ACTUALIZA_POR_MONTO = 10;
    
    /** Identifica una actualizacion de calculos por cambio en contraimporte */
    public static final int ACTUALIZA_POR_CONTRAIMPORTE = 20;
    
    /** Identifica una actualizacion de calculos por cambio en tcc */
    public static final int ACTUALIZA_POR_TCC = 30;
    
    /** Identifica una actualizacion de calculos por cambio en tcm */
    public static final int ACTUALIZA_POR_TCM = 40;
    
    /** Tipo de liq para filtrar los productos de manera que se obtengan los mnemonicos de BALNETEO */
    public static final String TIPO_LIQ_NETEO = "NETEO M.N.";

}
