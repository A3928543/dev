package com.ixe.ods.sica.pages.autorizatce;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.NumberUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.tapestry.IExternalPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.form.StringPropertySelectionModel;
import org.apache.tapestry.valid.IValidationDelegate;

import com.ixe.ods.bup.model.ContratoSica;
import com.ixe.ods.bup.model.EmpleadoIxe;
import com.ixe.ods.seguridad.model.IUsuario;
import com.ixe.ods.sica.Redondeador;
import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.Visit;
import com.ixe.ods.sica.dto.ClienteContratoInfoPromotorDto;
import com.ixe.ods.sica.dto.DetalleOperacionTceDto;
import com.ixe.ods.sica.dto.OperacionTceDto;
import com.ixe.ods.sica.model.Deal;
import com.ixe.ods.sica.model.DealDetalle;
import com.ixe.ods.sica.model.Estado;
import com.ixe.ods.sica.model.ParametroSica;
import com.ixe.ods.sica.model.SistemaTce;
import com.ixe.ods.sica.pizarron.Consts;
import com.ixe.treasury.dom.common.FormaPagoLiq;
import com.legosoft.tapestry.model.RecordSelectionModel;
import com.ixe.ods.sica.model.TipoBloqueo;

/**
 * <p>
 * P&aacute;gina para capturar los datos de una Autorizacion de Tipo de Cambio
 * Especial.
 * </p>
 * 
 * @author Cesar Jeronimo Gomez
 */
public abstract class CapturaAutorizaTce extends AbstractCapturaAutorizaTce
		implements IExternalPage {

	/*---------------------------------------------------------------------------------------------------------
	 * Metodos sobreescritos/implementados
	 *---------------------------------------------------------------------------------------------------------*/

	/**
	 * Regresa el arreglo con los estados de operaci&oacute;n normal y
	 * operaci&oacute;n restringida.
	 * 
	 * @return int[].
	 */
	protected int[] getEstadosValidos() {
		return new int[] { Estado.ESTADO_OPERACION_NORMAL,
				Estado.ESTADO_OPERACION_RESTRINGIDA,
				Estado.ESTADO_OPERACION_VESPERTINA };
	}

	/**
	 * Inicializa los datos de la pantalla
	 */
	public void activateExternalPage(Object[] params, IRequestCycle cycle) {
		activate(cycle);
		debug("activateExternalPage - params=" + params);

		try {
			// Validar horario vespertino
			if (isHorarioVespertino()) {
				throw new SicaException(
						"No es posible capturar un deal en Horario Vespertino");
			}

			// Valida la divisa de referencia, siempre debe ser peso
			if (!isDivisaReferenciaPeso()) {
				throw new SicaException("La divisa de referencia debe ser MXN");
			}

			// Inicializa el objeto que contiene los datos de captura de la
			// operacion
			setOperacionTce(new OperacionTceDto());

			// Establece el modo edicion y el no de deal a editar, si es que
			// existe, de lo contrario, establece el modo captura
			getOperacionTce().setDealNumber("");
			if (params != null && params.length > 0) {
				if (params[0] != null && params[0] instanceof Integer) {
					getOperacionTce().setDealNumber(
							String.valueOf(((Integer) params[0]).intValue()));
					debug("activateExternalPage - Se ha recibido un numero de deal: "
							+ params[0]);
				}
			}
			if (StringUtils.isEmpty(getOperacionTce().getDealNumber())) {
				setModo(MODO_CAPTURA);
			} else {
				setModo(MODO_EDICION);
				//Avisa que el deal no se podrá modificar
				Deal deal  = secureFindDeal(getOperacionTce().getDealNumber());
				if(!Deal.STATUS_ESPECIAL_TC_AUTORIZADO.equals(deal.getEstatusEspecial())) {
					getDelegate().record(
						"El deal no se puede modificar por que ya ha sido tomado por el promotor", null);
				}
			}
			debug("activateExternalPage - modo: " + getModo());

			initCatalogos();
			initSessionConstants();
			initFields();
		} catch (SicaException e) {
			redirectToMessage(cycle, e.getMessage());
		}
	}

	/*---------------------------------------------------------------------------------------------------------
	 * Listeners
	 *---------------------------------------------------------------------------------------------------------*/

	/**
	 * Acciones a ejecutar para un submit
	 * 
	 * Inicia procesamiento de la operacion
	 */
	public void submit(IRequestCycle cycle) {

		try {
			setMensajeConfirmDesvTc("");
			IValidationDelegate delegate = getDelegate();

			debug("getModoSubmit()=" + getModoSubmit());

			if (MODO_SUBMIT_SAVE != getModoSubmit()) {
				if (delegate.getHasErrors()) {
					delegate.clearErrors();
				}
			}
			String dealAnterior = null;
			boolean cancelado = false;
			if (isModoSubmitUpdateTc()) {
				actualizaTipoCambioPorModoSubmit();

			} else if (isModoSubmitUpdateCalculos()) {
				actualizaCalculosPorModoSubmit();

			} else if (isModoSubmitUpdateCalculosLiq()) {
				actualizaCalculosLiqPorModoSubmit();

			} else if (MODO_SUBMIT_UPDATE_BY_ARBITRAJE_CHANGE == getModoSubmit()) {
				debug("Modo Submit: arbitraje: "
						+ getOperacionTce().getDetalleOperacionArbitraje());
				if (getOperacionTce().isArbitraje()) {
					initDetalleOperacion(getOperacionTce()
							.getDetalleOperacionArbitraje(), true);
				} else {
					initDetalleOperacion(getOperacionTce()
							.getDetalleOperacionArbitraje());
					setProductosLiq(new String[] {});
				}
			} else if (MODO_SUBMIT_BUSCA_NOMBRE_PROMOTOR == getModoSubmit()) {
				buscarNombreUsuarioPromotor();

			} else if (MODO_SUBMIT_BUSCA_CONTRATO_CORTO == getModoSubmit()) {
				debug("Cambio por contrato");
				buscarContratoCorto();

			} else if (MODO_SUBMIT_CANCELAR == getModoSubmit()) {
				cancelar(cycle);

			} else if (MODO_SUBMIT_SAVE == getModoSubmit()) {
				debug("submit(IRequestCycle cycle): Se ha hecho submit para guardar datos");
				debug("Datos: " + "\ngetOperacionTce()=" + getOperacionTce()
						+ "\ngetModoSubmit()=" + getModoSubmit());
				Deal deal = null;
				if (isModoCaptura()) {
					deal = procesarAltaDeal(getOperacionTce());
				} else if (isModoEdicion()) {
					OperacionTceDto operacionTceDto = this
							.initFieldsOperacionConsulta();
					cancelado = this.validaCancelacionDeal(getOperacionTce(),
							operacionTceDto);
					if (cancelado) {
						dealAnterior = operacionTceDto.getDealNumber();
						deal = procesarAltaDealCancelado(getOperacionTce());
						if (deal != null) {
							this.cancelarDeal(operacionTceDto.getDealNumber());
							getOperacionTce().setDealNumber(
									String.valueOf(deal.getIdDeal()));
						}
					} else {
						if (validaCancelacionDetalleDeal(getOperacionTce(),
								operacionTceDto)) {
							debug("Monto del deal:"
									+ getOperacionTce().getDetalleOperacion()
											.getMonto());
							deal = procesarModificacionDeal(getOperacionTce());
						} else {
							deal = secureFindDeal(getOperacionTce()
									.getDealNumber());
							deal.setObservaciones(getOperacionTce()
									.getObservaciones());
							getSicaServiceData().actualizaObservacionesDeal(
									deal);
						}
					}
				}
				if (deal != null) {
					initFields();
					setLevel(1);
					if (isModoCaptura()) {
						getDelegate().record(
								"Deal generado con \u00E9xito: "
										+ deal.getIdDeal(), null);
					} else {
						if (isModoEdicion()) {
							if (cancelado) {
								getDelegate()
										.record(
												"Se ha cancelado el deal numero: "
														+ dealAnterior
														+ " y se ha generado el deal numero: "
														+ deal.getIdDeal(),
												null);
							} else {
								getDelegate().record(
										"Deal modificado con \u00E9xito", null);
							}
							this.initFieldsModoEdicionSubmit();
						}
					}
				}
			}
			setModoSubmit(MODO_SUBMIT_SAVE);
		} catch (SicaException e) {
			getDelegate().record(e.getMessage(), null);
		}
	}

	private boolean validaCancelacionDeal(OperacionTceDto operacionTceDtoNueva,
			OperacionTceDto operacionTceDto) {
		if (!operacionTceDtoNueva.getContratoCorto().equals(
				operacionTceDto.getContratoCorto())
				|| !operacionTceDtoNueva.getUsuarioPromotor().equals(
						operacionTceDto.getUsuarioPromotor())
				|| !operacionTceDtoNueva.getTipoOperacion().equals(
						operacionTceDto.getTipoOperacion())
				|| !operacionTceDtoNueva.getFechaValor().trim().equals(
						operacionTceDto.getFechaValor().trim())) {
			return true;
		} else {
			return false;
		}
	}

	private boolean validaCancelacionDetalleDeal(
			OperacionTceDto operacionTceDtoNueva,
			OperacionTceDto operacionTceDto) {
		boolean cancela = false;
		if (operacionTceDtoNueva != null && operacionTceDto != null) {
			if (validaCancelacionPorDetalle(operacionTceDtoNueva,
					operacionTceDto)
					|| validaCancelacionPorArbitraje(operacionTceDtoNueva,
							operacionTceDto)) {
				cancela = true;
			}
		}
		return cancela;
	}

	private boolean validaCancelacionPorDetalle(
			OperacionTceDto operacionTceDtoNueva,
			OperacionTceDto operacionTceDto) {
		boolean cancela = false;
		if (((operacionTceDtoNueva.getDetalleOperacion() != null && operacionTceDto
				.getDetalleOperacion() != null)
				&& (operacionTceDtoNueva.getDetalleOperacion().getDivisa() != null && operacionTceDto
						.getDetalleOperacion().getDivisa() != null) && (!operacionTceDtoNueva
				.getDetalleOperacion().getDivisa().equals(
						operacionTceDto.getDetalleOperacion().getDivisa())))
				|| ((operacionTceDtoNueva.getDetalleOperacion().getProducto() != null && operacionTceDto
						.getDetalleOperacion().getProducto() != null) && !operacionTceDtoNueva
						.getDetalleOperacion().getProducto().equals(
								operacionTceDto.getDetalleOperacion()
										.getProducto()))
				|| ((operacionTceDtoNueva.getDetalleOperacion().getMonto() != operacionTceDto
						.getDetalleOperacion().getMonto()))
				|| (operacionTceDtoNueva.getDetalleOperacion().getTcCliente() != operacionTceDto
						.getDetalleOperacion().getTcCliente())
				|| (operacionTceDtoNueva.getDetalleOperacion().getTcMesa() != operacionTceDto
						.getDetalleOperacion().getTcMesa())
				|| (operacionTceDtoNueva.getDetalleOperacion()
						.getContraimporte() != operacionTceDto
						.getDetalleOperacion().getContraimporte())) {
			cancela = true;
		}
		return cancela;
	}

	private boolean validaCancelacionPorArbitraje(
			OperacionTceDto operacionTceDtoNueva,
			OperacionTceDto operacionTceDto) {
		boolean cancela = false;
		if (((operacionTceDtoNueva.getDetalleOperacionArbitraje() != null && operacionTceDto
				.getDetalleOperacionArbitraje() != null)
				&& (operacionTceDtoNueva.getDetalleOperacionArbitraje()
						.getDivisa() != null && operacionTceDto
						.getDetalleOperacionArbitraje().getDivisa() != null) && (!operacionTceDtoNueva
				.getDetalleOperacionArbitraje().getDivisa().equals(
						operacionTceDto.getDetalleOperacionArbitraje()
								.getDivisa())))
				|| ((operacionTceDtoNueva.getDetalleOperacionArbitraje()
						.getProducto() != null && operacionTceDto
						.getDetalleOperacionArbitraje().getProducto() != null) && !operacionTceDtoNueva
						.getDetalleOperacionArbitraje().getProducto().equals(
								operacionTceDto.getDetalleOperacionArbitraje()
										.getProducto()))
				|| ((operacionTceDtoNueva.getDetalleOperacionArbitraje()
						.getMonto() != operacionTceDto
						.getDetalleOperacionArbitraje().getMonto()))
				|| (operacionTceDtoNueva.getDetalleOperacionArbitraje()
						.getTcCliente() != operacionTceDto
						.getDetalleOperacionArbitraje().getTcCliente())
				|| (operacionTceDtoNueva.getDetalleOperacionArbitraje()
						.getTcMesa() != operacionTceDto
						.getDetalleOperacionArbitraje().getTcMesa())
				|| (operacionTceDtoNueva.getDetalleOperacionArbitraje()
						.getContraimporte() != operacionTceDto
						.getDetalleOperacionArbitraje().getContraimporte())) {
			cancela = true;
		}
		return cancela;
	}

	private OperacionTceDto initFieldsOperacionConsulta() {
		OperacionTceDto operacionTceDto = new OperacionTceDto();
		if (getOperacionTce() != null) {
			Deal deal = secureFindDeal(getOperacionTce().getDealNumber());
			//Si el deal ya cambio de estatus especial no podrá ser modificado y lanza la excepcion.
			if(!Deal.STATUS_ESPECIAL_TC_AUTORIZADO.equals(deal.getEstatusEspecial())) {
				throw new SicaException(
						"El deal no se puede modificar por que ya ha sido tomado por el promotor");
			}
			
			operacionTceDto.setDealNumber(getOperacionTce().getDealNumber());
			operacionTceDto.setFechaOperacion(deal.getFechaCaptura());
			operacionTceDto.setSistema(deal.getSistemaTce());
			if (deal.getContratoSica() != null) {
				ContratoSica contratoSica = deal.getContratoSica();
				if (contratoSica != null) {
					ClienteContratoInfoPromotorDto contratoCorto = findContratoCortoByContratoSica(contratoSica
							.getNoCuenta());
					if (contratoCorto == null) {
						throw new SicaException(
								"No se encontr\u00F3 el contrato corto para el contrato sica "
										+ contratoSica.getNoCuenta());
					}
					operacionTceDto.setContratoCorto(String
							.valueOf(contratoCorto.getContratoCorto()));
				}

				if (deal.getCliente() != null) {
					operacionTceDto.setNombreClienteContratoCorto(deal
							.getCliente().getNombreCompleto());
				}
			} else {
				operacionTceDto.setContratoCorto("");
				operacionTceDto.setNombreClienteContratoCorto("");
			}

			if (deal.getPromotor() != null) {
				operacionTceDto.setUsuarioPromotor(deal.getPromotor()
						.getClaveUsuario());
				operacionTceDto.setNombreUsuarioPromotor(deal.getPromotor()
						.getNombreCompleto());
			} else {
				operacionTceDto.setUsuarioPromotor("");
				operacionTceDto.setNombreUsuarioPromotor("");
			}

			operacionTceDto
					.setTipoOperacion(deal.isCompra() ? OperacionTceDto.OPERACION_COMPRA
							: OperacionTceDto.OPERACION_VENTA);
			operacionTceDto.setFechaValor(deal.getTipoValor());
			operacionTceDto.setFechaLiquidacion(deal.getFechaLiquidacion());
			operacionTceDto.setObservaciones(deal.getObservaciones());

			// Inicializa detalle de operacion NO arbitraje
			DealDetalle det = getDetalleTce(deal, false);
			// hay que inicializar el catalogo de productos para que se pueda
			// mostrar el producto del detalle encontrado
			setProductos(getProductosByDivisa(det.getDivisa()));
			operacionTceDto.setDetalleOperacion(createDetalleOperacion(det));

			// Inicializa detalle de operacion arbitraje
			operacionTceDto.setArbitraje(false);
			DealDetalle detArb = getDetalleTce(deal, true);
			if (detArb != null) {
				operacionTceDto.setArbitraje(true);
				setProductosLiq(getProductosByDivisa(detArb.getDivisa()));
				operacionTceDto
						.setDetalleOperacionArbitraje(createDetalleOperacion(detArb));
			}
		}
		return operacionTceDto;
	}

	public void cancelarDeal(String strIdDeal) {
		debug("listener: cancelarDeal ...");
		try {
			debug("cancelarDeal - Deal recibido: " + strIdDeal);
			if (NumberUtils.isDigits(strIdDeal)) {
				Deal foundDeal = secureFindDeal(strIdDeal);
				debug("cancelarDeal - Deal encontrado: "
						+ foundDeal.getIdDeal());
				if (!Deal.STATUS_ESPECIAL_TC_AUTORIZADO.equals(foundDeal
						.getEstatusEspecial())) {
					throw new SicaException(
							"El deal no se puede cancelar por que ya ha sido tomado por el promotor");
				}
				if (Deal.STATUS_DEAL_CANCELADO
						.equals(foundDeal.getStatusDeal())) {
					throw new SicaException(
							"El deal no se puede cancelar por que ya ha sido cancelado");
				}
				debug("Cancelando deal 2");
				int idDeal = Integer.parseInt(strIdDeal);
				cancelarDealSafely(idDeal);
			} else {
				throw new SicaException(
						"No se recibi\u00F3 el n\u00FAmero de deal");
			}
		} catch (SicaException e) {
			getDelegate().record(e.getMessage(), null);
		}
	}

	private void cancelarDealSafely(int idDeal) {
		try {
			getWorkFlowServiceData().wfCancelarDirectamenteDeal(getTicket(),
					idDeal, ((Visit) getVisit()).getUser().getIdUsuario());
		} catch (RuntimeException e) {
			throw new SicaException("Error al cancelar deal: " + e.getMessage());
		}
	}

	public long getTcTimeOut() {
		if (isPromocion()) {
			if (getOperacionTce().getDealInputIniTime() == null) {
				return 0;
			}
			long maxTime = getDealTimeOutParam() * 1000;
			if (getOperacionTce().isArbitraje()) {
				maxTime = maxTime * 2;
			}
			long limitTime = getOperacionTce().getDealInputIniTime().getTime()
					+ maxTime;
			return limitTime - new Date().getTime();
		}
		return 0;
	}

	public void setTcTimeOut(long tcTimeOut) {
	}

	/**
	 * Listener para limpiar los campos o para redirigir a la pantalla de
	 * consulta si es que es modo edicion
	 * 
	 * @param cycle
	 */
	private void cancelar(IRequestCycle cycle) {
		try {
			debug("listener: cancelar");
			setMensajeConfirmDesvTc("");
			if (isModoCaptura()) {
				getOperacionTce().setDealNumber("");
				initFields();
			} else if (isModoEdicion()) {
				ConsultaAutorizaTce nextPage = (ConsultaAutorizaTce) getRequestCycle()
						.getPage("ConsultaAutorizaTce");
				nextPage.initFields();
				cycle.activate(nextPage);
			}
		} catch (SicaException e) {
			getDelegate().record(e.getMessage(), null);
		}
	}

	/**
	 * Listener para refrescar los mensajes del banner
	 * 
	 * @param cycle
	 */
	public void refreshBanner(IRequestCycle cycle) {
		try {
			debug("Refrescando mensajes en banner");
			setMensajeConfirmDesvTc("");
			setListaMensajes(getSicaServiceData().findMensajesTceHoy());
		} catch (SicaException e) {
			getDelegate().record(e.getMessage(), null);
		}
	}

	/*---------------------------------------------------------------------------------------------------------
	 * Models para los DropDown
	 *---------------------------------------------------------------------------------------------------------*/

	/**
	 * Modelo del campo Sistema
	 * 
	 * @return
	 */
	public IPropertySelectionModel getSistemaModel() {
		return new RecordSelectionModel(getSistemas(), "idSistema",
				"description");
	}

	/**
	 * Modelo que contiene las fechas valor.
	 * 
	 * @return IPropertySelectionModel
	 */
	public IPropertySelectionModel getFechaValorModel() {
		return new StringPropertySelectionModel(getFechasValor());
	}

	/**
	 * Modelo que contiene las divisas
	 * 
	 * @return
	 */
	public IPropertySelectionModel getDivisaModel() {
		return new DivisaSelectionModel(getDivisas());
	}

	/**
	 * Modelo que contiene los productos
	 * 
	 * @return
	 */
	public IPropertySelectionModel getProductoModel() {
		return new StringPropertySelectionModel(getProductos());
	}

	/**
	 * Modelo que contiene el catalogo de divisas de liquidacion
	 * 
	 * @return
	 */
	public IPropertySelectionModel getDivisaLiqModel() {
		if (getOperacionTce().isArbitraje()) {
			return new DivisaSelectionModel(getDivisas());
		} else {
			return new DivisaSelectionModel(new ArrayList());
		}
	}

	/**
	 * Modelo que contiene los productos de liquidacion
	 * 
	 * @return
	 */
	public IPropertySelectionModel getProductoLiqModel() {
		if (getOperacionTce().isArbitraje()) {
			return new StringPropertySelectionModel(getProductosLiq());
		} else {
			return new StringPropertySelectionModel(new String[] {});
		}
	}

	/*---------------------------------------------------------------------------------------------------------
	 * Metodos auxiliares
	 *---------------------------------------------------------------------------------------------------------*/

	/**
	 * Genera y da de alta el deal a partir de los datos de captura, se debe
	 * llamar solo si {@link #sonCamposValidos(OperacionTceDto)} regresa true
	 * 
	 * @param operacionTce
	 */
	protected Deal procesarAltaDeal(OperacionTceDto operacionTce) {
		// Solo si los campos son validos, hacer el guardado
		if (sonCamposValidos(operacionTce)) {
			debug("procesarAltaDeal - validacion de campos exitosa");
			Deal deal = null;
			deal = inicializaDeal(operacionTce);
			if (!esTiempoCapturaValido(operacionTce)) {
				actualizaAmbosTiposDeCambio();
				operacionTce.setDealInputIniTime(new Date());
				getDelegate()
						.record(
								"Se ha alcanzado el tiempo m\u00E1ximo permitido para pactar la operaci\u00F3n, los precios se han actualizado",
								null);
				return null;
			}
			return getSicaServiceData().insertarDealTce(deal, operacionTce,
					((Visit) getVisit()).getTicket(),
					getLimitesActualizados(deal),
					((Visit) getVisit()).getIdCanal());
		} else {
			return null;
		}
	}

	protected Deal procesarAltaDealCancelado(OperacionTceDto operacionTce) {
		// Solo si los campos son validos, hacer el guardado
		if (sonCamposValidos(operacionTce)) {
			operacionTce.setDealNumber(null);
			debug("procesarAltaDeal - validacion de campos exitosa");
			Deal deal = null;
			deal = inicializaDeal(operacionTce);
			if (!esTiempoCapturaValido(operacionTce)) {
				actualizaAmbosTiposDeCambio();
				operacionTce.setDealInputIniTime(new Date());
				getDelegate()
						.record(
								"Se ha alcanzado el tiempo m\u00E1ximo permitido para pactar la operaci\u00F3n, los precios se han actualizado",
								null);
				return null;
			}
			
					debug("operacionTce " + operacionTce.getDealNumber());
			return getSicaServiceData().insertarDealTce(deal, operacionTce,
					((Visit) getVisit()).getTicket(),
					getLimitesActualizados(deal),
					((Visit) getVisit()).getIdCanal());
		} else {
			return null;
		}
	}

	protected Deal procesarModificacionDeal(OperacionTceDto operacionTce) {
		// Solo si los campos son validos, hacer el guardado
		if (sonCamposValidos(operacionTce)) {
			debug("procesarAltaDeal - validacion de campos exitosa");
			Deal deal = null;
			deal = secureFindDeal(operacionTce.getDealNumber());
			debug("procesarAltaDeal - Deal inicializado para edicion");
			if (!esTiempoCapturaValido(operacionTce)) {
				actualizaAmbosTiposDeCambio();
				operacionTce.setDealInputIniTime(new Date());
				getDelegate()
						.record(
								"Se ha alcanzado el tiempo m\u00E1ximo permitido para pactar la operaci\u00F3n, los precios se han actualizado",
								null);
				return null;
			}
			if (!Deal.STATUS_ESPECIAL_TC_AUTORIZADO.equals(deal
					.getEstatusEspecial())) {
				throw new SicaException(
						"El deal no se puede modificar por que ya ha sido tomado por el promotor");
			}
			if (Deal.STATUS_DEAL_CANCELADO.equals(deal.getStatusDeal())) {
				throw new SicaException(
						"El deal no se puede modificar por que ya ha sido cancelado");
			}
			return getSicaServiceData().insertarDealTce(deal, operacionTce,
					((Visit) getVisit()).getTicket(),
					getLimitesActualizados(deal),
					((Visit) getVisit()).getIdCanal());
		} else {
			return null;
		}
	}

	/**
	 * Valida los campos capturados y realiza otras validaciones de estado del
	 * sistema, debe llamarse antes de hacer el guadado del deal
	 * 
	 * @return
	 */
	protected boolean sonCamposValidos(OperacionTceDto operTce) {
		if (operTce == null)
			return false;

		// Validar horario vespertino
		if (isHorarioVespertino()) {
			throw new SicaException(
					"No es posible capturar un deal en Horario Vespertino");
		}

		// Valida el campo contrato corto
		if (!StringUtils.isEmpty(operTce.getContratoCorto())) {
			if (!esContratoCortoValido(operTce.getContratoCorto(), operTce
					.getUsuarioPromotor())) {
				return false;
			}
		}

		// El campo promotor es el unico requerido, llenando este, se limpian
		// los datos del cliente
		// llenando cliente, se llena promotor
		if (!esPromotorValido(operTce.getUsuarioPromotor())) {
			return false;
		}

		if (operTce.getFechaValor() != null) {
			operTce.setFechaValor(operTce.getFechaValor().trim());
		} else {
			return false;
		}

		if (StringUtils.isEmpty(operTce.getTipoOperacion())) {
			getDelegate().record("Capture el campo Operaci\u00F3n", null);
			return false;
		} else if (!(operTce.isCompra() || operTce.isVenta())) {
			debug("sonCamposValidos - No es compra ni venta");
			return false;
		}

		// Valida TCM, TCC y Monto (No arbitraje)
		if (!sonCamposDetalleValidos(operTce.getTipoOperacion(), operTce
				.getFechaValor(), operTce.getDetalleOperacion(), false, operTce
				.getContratoCorto())) {
			return false;
		}

		// Valida TCM, TCC, Monto y Desbalanceo (arbitraje)
		if (operTce.isArbitraje()) {
			if (!sonCamposDetalleValidos(operTce.getTipoOperacion(), operTce
					.getFechaValor(), operTce.getDetalleOperacionArbitraje(),
					true, operTce.getContratoCorto())) {
				return false;
			}

			// Valida que no se exeda el desbalanceo configurado, contraimporte
			// vs contraimporte liq
			if (!esDesbalanceValido(operTce.getDetalleOperacion(), operTce
					.getDetalleOperacionArbitraje())) {
				return false;
			}

			if (!sonDivisasYProductosValidos(operTce.getDetalleOperacion(),
					operTce.getDetalleOperacionArbitraje())) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Valida los campos de un detalle de operacion
	 * 
	 * @param tipoOperacion
	 * @param fechaValor
	 * @param detOper
	 * @param isArbitraje
	 * @param contratoCorto
	 * @return
	 */
	protected boolean sonCamposDetalleValidos(String tipoOperacion,
			String fechaValor, DetalleOperacionTceDto detOper,
			boolean isArbitraje, String contratoCorto) {
		if (detOper.getDivisa() == null) {
			getDelegate().record("La divisa es requerida", null);
			return false;
		}
		if (StringUtils.isEmpty(detOper.getProducto())) {
			getDelegate().record("El producto es requerido", null);
			return false;
		}

		// Valida TCM, TCC y Monto
		if (!esTcmValido(tipoOperacion, fechaValor, detOper, isArbitraje)) {
			return false;
		}
		if (!esTccValido(detOper, isArbitraje)) {
			return false;
		}
		if (!esMontoValido(detOper, isArbitraje)) {
			return false;
		}

		if (StringUtils.isEmpty(contratoCorto)) {
			if (esProductoEnRestriccion(detOper.getProducto())) {
				return false;
			}
			if (esVentaChviaj(
					isArbitraje ? invierteTipoOperacion(tipoOperacion)
							: tipoOperacion, detOper.getProducto())) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Valida el monto
	 * 
	 * @param detOper
	 * @return
	 */
	protected boolean esMontoValido(DetalleOperacionTceDto detOper,
			boolean arbitraje) {
		debug("esMontoValido - detOper=" + detOper + ", arbitraje=" + arbitraje);
		if (detOper.getMonto() <= 0.0) {
			getDelegate().record("El monto debe ser mayor a cero.", null);
			return false;
		} else {
			if (isPromocion()) { // Solo si es promocion valida el monto maximo
				// para promotores
				double montoUsd = getMontoUsd(detOper.getDivisa(), detOper
						.getMonto());
				// El monto maximo para promotores es en dolares
				double montoMaximoPromotores = Redondeador
						.redondear2Dec(getParametroDouble(ParametroSica.TCE_MONTO_MAXIMO_PROMOTORES));
				debug("esMontoValido - montoUsd=" + montoUsd
						+ ", montoMaximoPromotores=" + montoMaximoPromotores);
				if (montoUsd > montoMaximoPromotores) {
					String montoMaximoPromotoresFormatted = getMoneyFormat()
							.format(montoMaximoPromotores);
					String msgArbitraje = " ";
					if (arbitraje) {
						msgArbitraje = " en la secci\u00F3n de arbitraje ";
					}
					getDelegate()
							.record(
									MessageFormat
											.format(
													"El monto m\u00E1ximo de operaci\u00F3n para promotores"
															+ msgArbitraje
															+ "es de {0} USD",
													new Object[] { montoMaximoPromotoresFormatted }),
									null);
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Valida el desbalance de los contraimportes
	 * 
	 * @param contraimporte
	 * @param contraimporteLiq
	 * @return
	 */
	protected boolean esDesbalanceValido(DetalleOperacionTceDto detOper,
			DetalleOperacionTceDto detOperArb) {
		double contraimporte = calculaContraimporte(detOper.getMonto(), detOper
				.getTcCliente());
		double contraimporteLiq = calculaContraimporte(detOperArb.getMonto(),
				detOperArb.getTcCliente());

		if (contraimporte == contraimporteLiq) {
			return true; // Si no hay desbalance, regresar true y no validar
			// desbalance maximo
		}
		debug("esDesbalanceValido - contraimporte=" + contraimporte
				+ ", contraimporteLiq=" + contraimporteLiq);
		FormaPagoLiq formaLiqBalneteo = getFormaLiqBalneteo(true);
		if (formaLiqBalneteo == null
				|| formaLiqBalneteo.getMontoMaximo() == null) {
			getDelegate()
					.record(
							"No ha sido posible obtener el limite m\u00E1ximo de desbalanceo (BALNETEO)",
							null);
			return false;
		}
		double desbalanceMaximo = Redondeador.redondear2Dec(formaLiqBalneteo
				.getMontoMaximo().doubleValue());
		double desbalanceActual = Math.abs(contraimporte - contraimporteLiq);
		debug("esDesbalanceValido - desbalanceMaximo=" + desbalanceMaximo
				+ ", desbalanceActual=" + desbalanceActual);

		if (desbalanceActual > desbalanceMaximo) {
			getDelegate().record(
					MessageFormat.format(
							"Ha superado el desbalanceo maximo de {0}",
							new Object[] { new Double(desbalanceMaximo) }),
					null);
			return false;
		}

		return true;
	}

	/**
	 * Valida el tipo de cambio de cliente
	 * 
	 * @param detOper
	 * @param arbitraje
	 * @return
	 */
	protected boolean esTccValido(DetalleOperacionTceDto detOper,
			boolean esArbitraje) {
		debug("esTccValido - detOper=" + detOper + ", arbitraje=" + esArbitraje);

		if (detOper.getTcCliente() <= 0.0) {
			getDelegate().record(
					"El Tipo de Cambio Cliente debe ser mayor a cero.", null);
			return false;
		}
         /**
          * 2012-02-26 -DJPR: se valida únicamente en base a DESV_MONTO_LIM
          **/
		
		double desvPorcLim = Redondeador
				.redondear6Dec(getParametroDouble(ParametroSica.DESV_PORC_LIM));
		
		double limiteDesviacion = 0.0;
		
		limiteDesviacion = desvPorcLim;
		

		boolean esDesvValida = esDesviacionTcValida(detOper.getTcCliente(),
				detOper.getTcMesa(), limiteDesviacion, esArbitraje, true,
				detOper.getDesvTccConfirmStatus());

		// Si la desviacion no es valida, se setea el estatus para pedir
		// confirmacion al usuario de mesa de cambios
		if (isMesaCambios()) {
			if (!esDesvValida) {
				debug("esTccValido - Es mesa cambios, seteando desviacion detectada de TCC");
				detOper
						.setDesvTccConfirmStatus(DetalleOperacionTceDto.DESV_TC_DETECTED);
			}
		}

		return esDesvValida;
	}

	/**
	 * Valida la desviacion del tipo de cambio especificado, enviando los
	 * mensajes correspondientes de error de ser necesario
	 * 
	 * @param tc
	 *            Tipo de cambio a validar
	 * @param tcBase
	 *            Tipo de cambio base contra el que se valida el porcentaje de
	 *            desviacion
	 * @param porcDesv
	 *            Procentaje de desviacion limite a usar
	 * @param esArbitraje
	 *            Indica si es un tc de la seccion de arbitraje (true)
	 * @param esTcc
	 *            Indica si es un tcc (true) o si es un tcm (false)
	 * 
	 * @return true si tc exede el porcentaje maximo de desviacion contra el
	 *         tcBase, false de otra forma
	 */
	protected boolean esDesviacionTcValida(double tc, double tcBase,
			double porcDesv, boolean esArbitraje, boolean esTcc,
			int desvTcStatus) {
		debug("esDesviacionTcValida - tc=" + tc + ", tcBase=" + tcBase
				+ ", porcDesv=" + porcDesv + ", esArbitraje=" + esArbitraje
				+ ", esTcc=" + esTcc);
		double limiteDesviacion = porcDesv;
		double tccLimInferior = 0;
		double tccLimSuperior = 0;
		
		/**
         * 2012-02-26 -DJPR: se valida únicamente en base a DESV_MONTO_LIM
         **/
		
        limiteDesviacion = Redondeador.redondear6Dec(limiteDesviacion);
        debug("2. limiteDesviacion: " + limiteDesviacion);
        
        // Si es TCC, se utiliza directamente el limite de desviacion.
        if(esTcc){
        
        	debug("a. " + Redondeador.redondear6Dec(tcBase * (1.0 - limiteDesviacion)));
            debug("b. " + Redondeador.redondear6Dec(tcBase * (1.0 + limiteDesviacion)));
            
    		 tccLimInferior = Redondeador.redondear6Dec(tcBase * (1.0 - limiteDesviacion));
    		 tccLimSuperior = Redondeador.redondear6Dec(tcBase * (1.0 + limiteDesviacion));

        }
        else {  // Si est TCM se divide por 100
        	debug("a. " + Redondeador.redondear6Dec(tcBase -  (tcBase * limiteDesviacion / NUMD_100)));
            debug("b. " + Redondeador.redondear6Dec(tcBase +  (tcBase * limiteDesviacion / NUMD_100)));
            
    		 tccLimInferior = Redondeador.redondear6Dec(tcBase -  (tcBase * limiteDesviacion / NUMD_100));
    		 tccLimSuperior = Redondeador.redondear6Dec(tcBase +  (tcBase * limiteDesviacion / NUMD_100));
        }
		
		if (tc < tccLimInferior || tc > tccLimSuperior) {
			String msgArbitraje = " ";
			if (esArbitraje) {
				msgArbitraje = " en la secci\u00F3n de arbitraje ";
			}
			String msgTccOTcm = "";
			if (esTcc) {
				msgTccOTcm = " tipo de cambio de cliente";
			} else {
				msgTccOTcm = " tipo de cambio de mesa";
			}
	
			String mensaje = MessageFormat.format("El" + msgTccOTcm
					+ msgArbitraje + "debe estar entre {0} y {1}",
					new Object[] {
							getCurrencyFormatSix().format(tccLimInferior),
							getCurrencyFormatSix().format(tccLimSuperior) });

			/*
			 * Se comenta este IF ya que aunque sea un usuario de la mesa no
			 * debe permitir pactar el deal con una desviación no permitida JDCH
			 * 26/10/2012 // Si es mesa de cambios, pide confirmacion para
			 * utilizar el tc a pesar de la desviacion // si ya se ha aceptado
			 * la confirmacion, retorna true para que quede como valido el tc
			 * if(isMesaCambios()) {debug(
			 * "esDesviacionTcValida - Es mesa cambios validando confirmacion de desviacion tc, desvTcStatus: "
			 * + desvTcStatus); if(desvTcStatus ==
			 * DetalleOperacionTceDto.DESV_TC_CONFIRMED) {debug(
			 * "esDesviacionTcValida - La desviacion del TC ha sido confirmada"
			 * ); return true; } else {debug(
			 * "esDesviacionTcValida - La desviacion del TC NO ha sido confirmada"
			 * ); mensaje = mensaje +
			 * "\n\u00BFDesea continuar con el tipo de cambio capturado?";
			 * setMensajeConfirmDesvTc(mensaje);
			 * debug("esDesviacionTcValida - Seteado mensaje de confirmacion: "
			 * + mensaje); return false; } }
			 */

			getDelegate().record(mensaje, null);
			return false;
		}

		return true;
	}

	/**
	 * Valida el tipo de cambio de mesa
	 * 
	 * @param tipoOperacion
	 * @param fechaValor
	 * @param detOper
	 * @param arbitraje
	 * @return
	 */
	protected boolean esTcmValido(String tipoOperacion, String fechaValor,
			DetalleOperacionTceDto detOper, boolean esArbitraje) {
		debug("esTcmValido - tipoOper=" + tipoOperacion + ", fechaValor="
				+ fechaValor + ", detOper=" + detOper + ", esArbitraje="
				+ esArbitraje);
		if (detOper.getTcMesa() <= 0.0) {
			getDelegate().record(
					"El Tipo de Cambio Mesa debe ser mayor a cero.", null);
			return false;
		}
		String lTipoOperacion = tipoOperacion;
		if (esArbitraje) {
			lTipoOperacion = invierteTipoOperacion(tipoOperacion);
		}
		double tipoCambioPizarron = getTipoCambioPizarron(lTipoOperacion,
				detOper.getDivisa(), detOper.getProducto(), fechaValor);
		boolean esDesvTcmValido = esDesviacionTcValida(detOper.getTcMesa(),
				tipoCambioPizarron, ParametroSica.TCE_PORCENT_VAR_TCM_VS_TCR,
				esArbitraje, false, detOper.getDesvTcmConfirmStatus());

		if (isMesaCambios()) {
			if (!esDesvTcmValido) {
				debug("esTcmValido - Es mesa cambios, seteando desviacion detectada de TCM");
				detOper
						.setDesvTcmConfirmStatus(DetalleOperacionTceDto.DESV_TC_DETECTED);
			}
		}

		return esDesvTcmValido;
	}

	/**
	 * Valida la desviacion del tipo de cambio especificado, enviando los
	 * mensajes correspondientes de error de ser necesario
	 * 
	 * @param tc
	 *            Tipo de cambio a validar
	 * @param tcBase
	 *            Tipo de cambio base contra el que se valida el porcentaje de
	 *            desviacion
	 * @param porcDesvParamName
	 *            Nombre del parametro que guarda el porcentaje de desviacion
	 * @param esArbitraje
	 *            Indica si es un tc de la seccion de arbitraje (true)
	 * @param esTcc
	 *            Indica si es un tcc (true) o si es un tcm (false)
	 * 
	 * @return true si tc exede el porcentaje maximo de desviacion contra el
	 *         tcBase, false de otra forma
	 */
	protected boolean esDesviacionTcValida(double tc, double tcBase,
			String porcDesvParamName, boolean esArbitraje, boolean esTcc,
			int desvTcStatus) {
		debug("esDesviacionTcValida - tc=" + tc + ", tcBase=" + tcBase
				+ ", porcDesvParamName=" + porcDesvParamName + ", esArbitraje="
				+ esArbitraje + ", esTcc=" + esTcc);
		double porcentVar = Redondeador
				.redondear6Dec(getParametroDouble(porcDesvParamName));
		return esDesviacionTcValida(tc, tcBase, porcentVar, esArbitraje, esTcc,
				desvTcStatus);
	}

	/**
	 * Inicializa un detalle de la operacion ya sea arbitraje o no con valores
	 * por default
	 * 
	 * @param detOper
	 * @param isArbitraje
	 */
	private void initDetalleOperacion(DetalleOperacionTceDto detOper,
			boolean isArbitraje) {
		// detOper.setDivisa( getFirstDivisa(getDivisas()) );
		detOper.setDivisa(null);

		// Si es arbitraje, actualiza el catalogo de productos de arbitraje, de
		// lo contrario, actualiza el catalogo de productos no arbitraje
		String[] arregloProductos = null;
		if (isArbitraje) {
			setProductosLiq(getProductosByDivisa(detOper.getDivisa()));
			arregloProductos = getProductosLiq();
		} else {
			setProductos(getProductosByDivisa(detOper.getDivisa()));
			arregloProductos = getProductos();
		}

		if (arregloProductos != null && arregloProductos.length > 0) {
			detOper.setProducto(arregloProductos[0]);
		}
		actualizaTipoCambio(getOperacionTce().getTipoOperacion(),
				getOperacionTce().getFechaValor(), detOper);
	}

	/**
	 * Inicializa los catalogos de la pantalla, divisas, sistemas, fechas valor
	 * y productos
	 */
	private void initCatalogos() {
		// Inicializa los catalogos con valores por default
		setSistemas(new ArrayList());
		setDivisas(new ArrayList());
		setFechasValor(new String[] {});
		setProductos(new String[] {});
		setProductosLiq(new String[] {});

		// Inicializa catalogo de sistemas
		setSistemas(getSistemasTce());

		// Inicializa catalogos de divisas
		setDivisas(getDivisasPorPerfil());

		// Inicializa catalogo de fechas valor
		setFechasValor(getCatalogoFechasValor());
	}

	/**
	 * Inicializa las propiedades definidas como persistent
	 */
	private void initSessionConstants() {
		setDealTimeout(getDealTimeOutParam());
		setCurrentField(0);
		setActivoArbitraje(0);
	}

	/**
	 * Inicializa los valores de los campos
	 */
	private void initFields() {
		setListaMensajes(getSicaServiceData().findMensajesTceHoy());
		setMensajeConfirmDesvTc("");
		setModoSubmit(MODO_SUBMIT_SAVE);
		if (getModo() == MODO_CAPTURA) {
			initFieldsModoCaptura();
		} else if (getModo() == MODO_EDICION) {
			initFieldsModoEdicion();
		}
	}

	/**
	 * Inicializa los campos en modo captura
	 */
	private void initFieldsModoCaptura() {
		debug("inicializa modo captura");

		// Inicializa datos generales de la operacion
		getOperacionTce().setFechaOperacion(getFechaOperacion());
		if (getSistemas() != null && !getSistemas().isEmpty()) {
			getOperacionTce().setSistema((SistemaTce) getSistemas().get(0));
		}
		getOperacionTce().setContratoCorto("");
		getOperacionTce().setNombreClienteContratoCorto("");

		if (isMesaCambios()) { // Si es mesa de cambios los campos del promotor
			// aparecen vacios
			debug("Es mesa cambios");
			getOperacionTce().setUsuarioPromotor("");
			getOperacionTce().setNombreUsuarioPromotor("");
		} else if (isPromocion()) { // Si es promocion, los campos del promotor
			// se pueblan con el usuario logeado
			debug("Es promocion");
			IUsuario user = getUsuarioActualConPersona();
			if (user != null) {
				getOperacionTce().setUsuarioPromotor(user.getClave());
				getOperacionTce().setNombreUsuarioPromotor(
						user.getPersona().getNombreCompleto());
			}
		}
		getOperacionTce().setTipoOperacion(OperacionTceDto.OPERACION_COMPRA);
		if (getFechasValor() != null && getFechasValor().length > 0) {
			getOperacionTce().setFechaValor(getFechasValor()[0]);
		}

		getOperacionTce().setFechaLiquidacion(
				calculaFechaLiquidacion(getOperacionTce().getFechaValor()));
		getOperacionTce().setObservaciones("");

		// Inicializa detalle de operacion NO arbitraje con valores por default
		// en divisa, producto, tcc y tcm
		DetalleOperacionTceDto detOper = new DetalleOperacionTceDto();
		initDetalleOperacion(detOper, false);
		getOperacionTce().setDetalleOperacion(detOper);

		// Inicializa detalle de operacion arbitraje por default inabilitados
		getOperacionTce().setArbitraje(false);
		DetalleOperacionTceDto detalleOperacionLiq = new DetalleOperacionTceDto();
		initDetalleOperacion(detalleOperacionLiq);
		getOperacionTce().setDetalleOperacionArbitraje(detalleOperacionLiq);
		getOperacionTce().setDealInputIniTime(null);

		setTiempoRestante(0);
	}

	private void initFieldsModoEdicionSubmit() {
		debug("inicializa modo captura");

		// Inicializa datos generales de la operacion
		getOperacionTce().setFechaOperacion(getFechaOperacion());
		if (getSistemas() != null && !getSistemas().isEmpty()) {
			getOperacionTce().setSistema((SistemaTce) getSistemas().get(0));
		}
		getOperacionTce().setContratoCorto("");
		getOperacionTce().setNombreClienteContratoCorto("");

		if (isMesaCambios()) { // Si es mesa de cambios los campos del promotor
			// aparecen vacios
			debug("Es mesa cambios");
			getOperacionTce().setUsuarioPromotor("");
			getOperacionTce().setNombreUsuarioPromotor("");
		} else if (isPromocion()) { // Si es promocion, los campos del promotor
			// se pueblan con el usuario logeado
			debug("Es promocion");
			IUsuario user = getUsuarioActualConPersona();
			if (user != null) {
				getOperacionTce().setUsuarioPromotor(user.getClave());
				getOperacionTce().setNombreUsuarioPromotor(
						user.getPersona().getNombreCompleto());
			}
		}
		getOperacionTce().setTipoOperacion(OperacionTceDto.OPERACION_COMPRA);
		if (getFechasValor() != null && getFechasValor().length > 0) {
			getOperacionTce().setFechaValor(getFechasValor()[0]);
		}

		getOperacionTce().setFechaLiquidacion(
				calculaFechaLiquidacion(getOperacionTce().getFechaValor()));
		getOperacionTce().setObservaciones("");

		// Inicializa detalle de operacion NO arbitraje con valores por default
		// en divisa, producto, tcc y tcm
		DetalleOperacionTceDto detOper = new DetalleOperacionTceDto();
		initDetalleOperacion(detOper, false);
		getOperacionTce().setDetalleOperacion(detOper);

		// Inicializa detalle de operacion arbitraje por default inabilitados
		getOperacionTce().setArbitraje(false);
		DetalleOperacionTceDto detalleOperacionLiq = new DetalleOperacionTceDto();
		initDetalleOperacion(detalleOperacionLiq);
		getOperacionTce().setDetalleOperacionArbitraje(detalleOperacionLiq);
		getOperacionTce().setDealInputIniTime(null);

		setTiempoRestante(0);
	}

	/**
	 * Inicializa los campos en modo edicion, ejecuta una consulta para
	 * encontrar el Deal recibido como parametro desde la pantalla de consulta
	 * TCE, una vez encontrado, los campos son poblados con los datos del mismo
	 */
	private void initFieldsModoEdicion() {
		debug("inicializa modo edicion");
		if (getOperacionTce() != null) {
			Deal deal = secureFindDeal(getOperacionTce().getDealNumber());

			getOperacionTce().setFechaOperacion(deal.getFechaCaptura());
			getOperacionTce().setSistema(deal.getSistemaTce());

			if (deal.getContratoSica() != null) {
				ContratoSica contratoSica = deal.getContratoSica();
				if (contratoSica != null) {
					ClienteContratoInfoPromotorDto contratoCorto = findContratoCortoByContratoSica(contratoSica
							.getNoCuenta());
					if (contratoCorto == null) {
						throw new SicaException(
								"No se encontr\u00F3 el contrato corto para el contrato sica "
										+ contratoSica.getNoCuenta());
					}
					getOperacionTce().setContratoCorto(
							String.valueOf(contratoCorto.getContratoCorto()));
				}

				if (deal.getCliente() != null) {
					getOperacionTce().setNombreClienteContratoCorto(
							deal.getCliente().getNombreCompleto());
				}
			} else {
				getOperacionTce().setContratoCorto("");
				getOperacionTce().setNombreClienteContratoCorto("");
			}

			if (deal.getPromotor() != null) {
				getOperacionTce().setUsuarioPromotor(
						deal.getPromotor().getClaveUsuario());
				getOperacionTce().setNombreUsuarioPromotor(
						deal.getPromotor().getNombreCompleto());
			} else {
				getOperacionTce().setUsuarioPromotor("");
				getOperacionTce().setNombreUsuarioPromotor("");
			}

			getOperacionTce().setTipoOperacion(
					deal.isCompra() ? OperacionTceDto.OPERACION_COMPRA
							: OperacionTceDto.OPERACION_VENTA);
			getOperacionTce().setFechaValor(deal.getTipoValor().trim());
			getOperacionTce().setFechaLiquidacion(deal.getFechaLiquidacion());
			getOperacionTce().setObservaciones(deal.getObservaciones());

			// Inicializa detalle de operacion NO arbitraje
			DealDetalle det = getDetalleTce(deal, false);
			// hay que inicializar el catalogo de productos para que se pueda
			// mostrar el producto del detalle encontrado
			setProductos(getProductosByDivisa(det.getDivisa()));
			getOperacionTce().setDetalleOperacion(createDetalleOperacion(det));

			// Inicializa detalle de operacion arbitraje
			getOperacionTce().setArbitraje(false);
			DealDetalle detArb = getDetalleTce(deal, true);
			if (detArb != null) {
				getOperacionTce().setArbitraje(true);
				setProductosLiq(getProductosByDivisa(detArb.getDivisa()));
				getOperacionTce().setDetalleOperacionArbitraje(
						createDetalleOperacion(detArb));
			}
		}
	}

	/**
	 * Actualiza el tipo de cambio de la mesa, desencadenando la actualizacion
	 * de otros campos que dependen de esta (calculos)
	 * 
	 * Se ejecuta cuando ha cambiado Operacion, Fecha Valor, Divisa o Producto
	 */
	private void actualizaTipoCambio() {
		actualizaTipoCambio(getOperacionTce().getTipoOperacion(),
				getOperacionTce().getFechaValor(), getOperacionTce()
						.getDetalleOperacion());

		// Al actualizarse el tipo de cambio se resetea el tiempo de inicio de
		// captura de la operacion
		getOperacionTce().setDealInputIniTime(new Date());
		debug("actualizaTipoCambio - Ha cambiado el tcm no ARB, seteando tiempo de inicio de captura: "
				+ getFullDateFormat().format(
						getOperacionTce().getDealInputIniTime()));

		setModoSubmit(MODO_SUBMIT_UPDATE_BY_TCC_CHANGE); // despues de
		// actualizar el tc,
		// se mandan a
		// actualizar los
		// calculos por
		// cambio en TCC
		actualizaCalculosPorModoSubmit();
	}

	/**
	 * Actualiza el tipo de cambio de la mesa para la seccion de arbitraje y
	 * actualiza los calculos de esta seccion
	 * 
	 * Se ejecuta cuando ha cambiado Operacion, Fecha Valor, Divisa Liq o
	 * Producto Liq
	 */
	private void actualizaTipoCambioLiq() {
		if (getOperacionTce().isArbitraje()) {
			// Se obtiene el tipo de cambio para la operacion contraria a la
			// actual
			String tipoOperacionArbitraje = invierteTipoOperacion(getOperacionTce()
					.getTipoOperacion());
			actualizaTipoCambio(tipoOperacionArbitraje, getOperacionTce()
					.getFechaValor(), getOperacionTce()
					.getDetalleOperacionArbitraje());
			setModoSubmit(MODO_SUBMIT_UPDATE_BY_TCC_LIQ_CHANGE); // despues de
			// actualizar
			// el TC, se
			// mandan a
			// actualizar
			// los
			// calculos
			// de arbitraje por cambio en TCC liq
			actualizaCalculosLiqPorModoSubmit();
		}
	}

	/**
	 * Actualiza los calculos de la seccion de no arbitraje
	 */
	private void actualizaCalculosPorModoSubmit() {
		if (getModoSubmit() == MODO_SUBMIT_UPDATE_BY_AMOUNT_CHANGE) {
			debug("actualizaCalculos - Submit por cambio en Monto: "
					+ getOperacionTce().getDetalleOperacion().getMonto());
			actualizaCalculos(ACTUALIZA_POR_MONTO, getOperacionTce()
					.getDetalleOperacion(), getOperacionTce()
					.getTipoOperacion());

		} else if (getModoSubmit() == MODO_SUBMIT_UPDATE_BY_AGAINST_AMOUNT_CHANGE) {
			debug("actualizaCalculos - Submit por cambio en Contraimporte: "
					+ getOperacionTce().getDetalleOperacion()
							.getContraimporte());
			actualizaCalculos(ACTUALIZA_POR_CONTRAIMPORTE, getOperacionTce()
					.getDetalleOperacion(), getOperacionTce()
					.getTipoOperacion());

		} else if (getModoSubmit() == MODO_SUBMIT_UPDATE_BY_TCC_CHANGE) {
			debug("actualizaCalculos - Submit por cambio en TCC: "
					+ getOperacionTce().getDetalleOperacion().getTcCliente());
			actualizaCalculos(ACTUALIZA_POR_TCC, getOperacionTce()
					.getDetalleOperacion(), getOperacionTce()
					.getTipoOperacion());
			// Si cambia el tcc, se resetea el estatus de confirmacion de
			// desviacion
			getOperacionTce().getDetalleOperacion().setDesvTccConfirmStatus(
					DetalleOperacionTceDto.DESV_TC_NOT_DETECTED);

		} else if (getModoSubmit() == MODO_SUBMIT_UPDATE_BY_TCM_CHANGE) {
			debug("actualizaCalculos - Submit por cambio en TCM: "
					+ getOperacionTce().getDetalleOperacion().getTcMesa());
			actualizaCalculos(ACTUALIZA_POR_TCM, getOperacionTce()
					.getDetalleOperacion(), getOperacionTce()
					.getTipoOperacion());
			// Si cambia el tcm, se resetea el estatus de confirmacion de
			// desviacion
			getOperacionTce().getDetalleOperacion().setDesvTcmConfirmStatus(
					DetalleOperacionTceDto.DESV_TC_NOT_DETECTED);
			return;
		}

		if (getOperacionTce().isArbitraje()) {
			// Solo si ha cambiado contraimporte: (No tcm) // TODO: Revisar
			// Actualizar calculos de arbitraje
			getOperacionTce().getDetalleOperacionArbitraje().setContraimporte(
					getOperacionTce().getDetalleOperacion().getContraimporte()); // se
			// copia
			// el
			// valor
			// del
			// contraimporte
			// al
			// contraimporte
			// de
			// arbitraje
			setModoSubmit(MODO_SUBMIT_UPDATE_BY_AGAINST_AMOUNT_LIQ_CHANGE); // setea
			// el
			// modo
			// de
			// submit
			// para
			// cambio
			// en
			// contraimporte
			// de
			// arbitraje

			actualizaCalculosLiqPorModoSubmit(); // actualiza calculos en
			// arbitraje
		}
	}

	/**
	 * Actualiza los calculos de la seccion de arbitraje
	 */
	private void actualizaCalculosLiqPorModoSubmit() {
		if (getOperacionTce().isArbitraje()) {
			String tipoOperacion = invierteTipoOperacion(getOperacionTce()
					.getTipoOperacion());
			if (getModoSubmit() == MODO_SUBMIT_UPDATE_BY_AMOUNT_LIQ_CHANGE) {
				debug("actualizaCalculosLiq - Submit por cambio en Monto ARB"
						+ getOperacionTce().getDetalleOperacionArbitraje()
								.getMonto());
				actualizaCalculos(ACTUALIZA_POR_MONTO, getOperacionTce()
						.getDetalleOperacionArbitraje(), tipoOperacion);

			} else if (getModoSubmit() == MODO_SUBMIT_UPDATE_BY_AGAINST_AMOUNT_LIQ_CHANGE) {
				debug("actualizaCalculosLiq - Submit por cambio en Contraimporte ARB"
						+ getOperacionTce().getDetalleOperacionArbitraje()
								.getContraimporte());
				actualizaCalculos(ACTUALIZA_POR_CONTRAIMPORTE,
						getOperacionTce().getDetalleOperacionArbitraje(),
						tipoOperacion);

			} else if (getModoSubmit() == MODO_SUBMIT_UPDATE_BY_TCC_LIQ_CHANGE) {
				debug("actualizaCalculosLiq - Submit por cambio en TCC ARB"
						+ getOperacionTce().getDetalleOperacionArbitraje()
								.getTcCliente());
				actualizaCalculos(ACTUALIZA_POR_TCC, getOperacionTce()
						.getDetalleOperacionArbitraje(), tipoOperacion);
				// Si cambia el tcc, se resetea el estatus de confirmacion de
				// desviacion
				getOperacionTce().getDetalleOperacionArbitraje()
						.setDesvTccConfirmStatus(
								DetalleOperacionTceDto.DESV_TC_NOT_DETECTED);

			} else if (getModoSubmit() == MODO_SUBMIT_UPDATE_BY_TCM_LIQ_CHANGE) {
				debug("actualizaCalculosLiq - Submit por cambio en TCM ARB"
						+ getOperacionTce().getDetalleOperacionArbitraje()
								.getTcMesa());
				actualizaCalculos(ACTUALIZA_POR_TCM, getOperacionTce()
						.getDetalleOperacionArbitraje(), tipoOperacion);
				// Si cambia el tcm, se resetea el estatus de confirmacion de
				// desviacion
				getOperacionTce().getDetalleOperacionArbitraje()
						.setDesvTcmConfirmStatus(
								DetalleOperacionTceDto.DESV_TC_NOT_DETECTED);

			}
		}
	}

	/**
	 * Actualiza los tipos de cambio segun el valor de modo submit
	 */
	private void actualizaTipoCambioPorModoSubmit() {
		if (MODO_SUBMIT_UPDATE_BY_TIPO_OPER_CHANGE == getModoSubmit()) {
			debug("actualizaTipoCambioPorModoSubmit - Submit por cambio en Tipo Operacion: "
					+ getOperacionTce().getTipoOperacion());
			actualizaAmbosTiposDeCambio();
		} else if (MODO_SUBMIT_UPDATE_BY_FECHA_VALOR_CHANGE == getModoSubmit()) {
			debug("actualizaTipoCambioPorModoSubmit - Submit por cambio en Fecha Valor: "
					+ getOperacionTce().getFechaValor());
			getOperacionTce().setFechaLiquidacion(
					calculaFechaLiquidacion(getOperacionTce().getFechaValor()));
			actualizaAmbosTiposDeCambio();
		} else if (MODO_SUBMIT_UPDATE_BY_DIVISA_CHANGE == getModoSubmit()) {
			debug("actualizaTipoCambioPorModoSubmit - Submit por cambio en Divisa: "
					+ getOperacionTce().getDetalleOperacion().safeGetIdDivisa());
			// Actualiza los productos, setea uno por default y actualiza los
			// tipos de cambio
			setProductos(getProductosByDivisa(getOperacionTce()
					.getDetalleOperacion().getDivisa()));
			if (getProductos() != null && getProductos().length > 0) {
				getOperacionTce().getDetalleOperacion().setProducto(
						getProductos()[0]);
			}
			actualizaTipoCambio();
		} else if (MODO_SUBMIT_UPDATE_BY_DIVISA_LIQ_CHANGE == getModoSubmit()) {
			debug("actualizaTipoCambioPorModoSubmit - Submit por cambio en Divisa Arb: "
					+ getOperacionTce().getDetalleOperacionArbitraje()
							.safeGetIdDivisa());
			// Actualiza los productos de arbitraje, setea uno por default y
			// actualiza los tipos de cambio de arbitraje
			setProductosLiq(getProductosByDivisa(getOperacionTce()
					.getDetalleOperacionArbitraje().getDivisa()));
			if (getProductosLiq() != null && getProductosLiq().length > 0) {
				getOperacionTce().getDetalleOperacionArbitraje().setProducto(
						getProductosLiq()[0]);
			}
			// Al cambiar la divisa liq se setea el contraimporte liq con el
			// contraimporte No liq
			// para que se calcule el monto liq en automatico
			if (getOperacionTce().isArbitraje()) {
				getOperacionTce().getDetalleOperacionArbitraje()
						.setContraimporte(
								getOperacionTce().getDetalleOperacion()
										.getContraimporte());
			}
			actualizaTipoCambioLiq();
		} else if (MODO_SUBMIT_UPDATE_BY_PRODUCTO_CHANGE == getModoSubmit()) {
			debug("actualizaTipoCambioPorModoSubmit - Submit por cambio en Producto: "
					+ getOperacionTce().getDetalleOperacion().getProducto());
			actualizaTipoCambio();
		} else if (MODO_SUBMIT_UPDATE_BY_PRODUCTO_LIQ_CHANGE == getModoSubmit()) {
			debug("actualizaTipoCambioPorModoSubmit - Submit por cambio en Producto Arb: "
					+ getOperacionTce().getDetalleOperacionArbitraje()
							.getProducto());
			actualizaTipoCambioLiq();
		}
	}

	/**
	 * Actualiza los tipos de cambio tanto de arbiraje como de no arbitraje y
	 * los calculos correspondientes de ser necesario
	 */
	private void actualizaAmbosTiposDeCambio() {
		actualizaTipoCambio();
		actualizaTipoCambioLiq();
	}

	/**
	 * Evalua si el submit es para actualizar tipos de cambio
	 * 
	 * @return
	 */
	private boolean isModoSubmitUpdateTc() {
		return MODO_SUBMIT_UPDATE_BY_TIPO_OPER_CHANGE == getModoSubmit()
				|| MODO_SUBMIT_UPDATE_BY_FECHA_VALOR_CHANGE == getModoSubmit()
				|| MODO_SUBMIT_UPDATE_BY_DIVISA_CHANGE == getModoSubmit()
				|| MODO_SUBMIT_UPDATE_BY_DIVISA_LIQ_CHANGE == getModoSubmit()
				|| MODO_SUBMIT_UPDATE_BY_PRODUCTO_CHANGE == getModoSubmit()
				|| MODO_SUBMIT_UPDATE_BY_PRODUCTO_LIQ_CHANGE == getModoSubmit();
	}

	/**
	 * Evalua si el modo submit es para actualizar calculos
	 * 
	 * @return
	 */
	private boolean isModoSubmitUpdateCalculos() {
		return MODO_SUBMIT_UPDATE_BY_AMOUNT_CHANGE == getModoSubmit()
				|| MODO_SUBMIT_UPDATE_BY_AGAINST_AMOUNT_CHANGE == getModoSubmit()
				|| MODO_SUBMIT_UPDATE_BY_TCC_CHANGE == getModoSubmit()
				|| MODO_SUBMIT_UPDATE_BY_TCM_CHANGE == getModoSubmit();
	}

	/**
	 * Evalua si el modo submit es para actualizar calculos de arbitraje
	 * 
	 * @return
	 */
	private boolean isModoSubmitUpdateCalculosLiq() {
		return MODO_SUBMIT_UPDATE_BY_AMOUNT_LIQ_CHANGE == getModoSubmit()
				|| MODO_SUBMIT_UPDATE_BY_AGAINST_AMOUNT_LIQ_CHANGE == getModoSubmit()
				|| MODO_SUBMIT_UPDATE_BY_TCC_LIQ_CHANGE == getModoSubmit()
				|| MODO_SUBMIT_UPDATE_BY_TCM_LIQ_CHANGE == getModoSubmit();
	}

	/**
	 * Retorna true solo si la pantalla esta en modo captura
	 * 
	 * @return
	 */
	private boolean isModoCaptura() {
		return MODO_CAPTURA == getModo();
	}

	/**
	 * Retorna true solo si la pantalla esta en modo edicion
	 * 
	 * @return
	 */
	private boolean isModoEdicion() {
		return MODO_EDICION == getModo();
	}

	/**
	 * Buscar el nombre de usuario del promotor dado
	 * 
	 * @param cycle
	 */
	private void buscarNombreUsuarioPromotor() {
		debug("listener: buscarNombreUsuarioPromotor");
		if (getOperacionTce().getUsuarioPromotor() != null
				&& !StringUtils.isEmpty(getOperacionTce().getUsuarioPromotor())) {
			if (isMesaCambios() && isModoCaptura()) {
				EmpleadoIxe lPromotor = findNombrePromotor(getOperacionTce()
						.getUsuarioPromotor());
				if (lPromotor != null) {
					getOperacionTce().setNombreUsuarioPromotor(
							lPromotor.getNombreCompleto());
				} else {
					getDelegate()
							.record(
									"Promotor no encontrado, por favor reintente la busqueda",
									null);
				}
			}
		}
	}

	/**
	 * Listener para buscar el contrato corto
	 * 
	 * @param cycle
	 */
	private void buscarContratoCorto() {
		debug("listener: buscarContratoCorto");
		String contratoCorto = getOperacionTce().getContratoCorto();
		if (StringUtils.isEmpty(contratoCorto)) {
			getOperacionTce().setNombreClienteContratoCorto("");
			return;
		}
		if (!NumberUtils.isDigits(contratoCorto)) {
			getDelegate().record("El contrato corto debe ser num\u00E9rico",
					null);
			return;
		}
		ClienteContratoInfoPromotorDto clienteContrato = findClienteByContratoCorto(contratoCorto);
		if (clienteContrato == null) {
			getDelegate().record("Contrato corto no encontrado, reintente",
					null);
		}
		if (clienteContrato.getIdBloqueo() != null && clienteContrato.getIdBloqueo().intValue() != TipoBloqueo.SIN_BLOQUEO){
			String descripcion = "";
			descripcion = getSicaServiceData().obtenerDescripcionBloqueo(clienteContrato.getIdBloqueo());
			getDelegate().record(
					"El Contrato esta bloqueado por " + descripcion + ". Favor de comunicarse al area de Contratos. ", null);
			initFields();
			//return;
		}
		  else {	
		  
			if (isMesaCambios()) {
				// Si encuentra el contrato corto, llena los campos: nombre del
				// cliente, nombre promotor y usuario promotor
				// TODO: Que hacer cuando el cliente no tiene asignado un
				// promotor (1)
				// (1) Si el cliente no tiene asignado un promotor, simplemente
				// el query no va a regresar nada y va a mostrar el error de
				// contrato corto no encontrado
				getOperacionTce().setNombreClienteContratoCorto(
						clienteContrato.getNombreCorto());
				getOperacionTce().setNombreUsuarioPromotor(
						clienteContrato.getNombreEjecutivo());
				getOperacionTce().setUsuarioPromotor(
						clienteContrato.getClaveUsuarioEjecutivo());
			} else if (isPromocion()) {
				// Si encuentra el contrato corto, verifica que la clave del
				// promotor del contrato encontrado sea la misma
				// que la del promotor que esta capturando, si es asi, llena el
				// campo: nombre del cliente
				// TODO: verificar si este flujo es correcto
				if (StringUtils.isEmpty(getOperacionTce().getUsuarioPromotor())) {
					getDelegate().record(
							"Clave de usuario del promotor requerida", null);
					return;
				}

				if (getOperacionTce().getUsuarioPromotor().equals(
						clienteContrato.getClaveUsuarioEjecutivo())) {
					getOperacionTce().setNombreClienteContratoCorto(
							clienteContrato.getNombreCorto());
				} else {
					getDelegate().record(
							"El cliente " + clienteContrato.getNombreCorto()
									+ " con contrato corto " + contratoCorto
									+ " no esta asignado al promotor actual",
							null);
				}
			}
		}
	}

	/*---------------------------------------------------------------------------------------------------------
	 * Campos para la pantalla de tapestry
	 *---------------------------------------------------------------------------------------------------------*/

	/** Obtiene los datos de la operacion */
	public abstract OperacionTceDto getOperacionTce();

	/** Establece los datos de la operacion */
	public abstract void setOperacionTce(OperacionTceDto operacionTce);

	/*---------------------------------------------------------------------------------------------------------
	 * Otros campos/propiedades
	 *---------------------------------------------------------------------------------------------------------*/

	/**
	 * Obtiene el modo en el que se interpreta un submit
	 * {@link #MODO_SUBMIT_SAVE} o {@link #MODO_SUBMIT_UPDATE_TC}
	 */
	public abstract int getModoSubmit();

	/** Establece el modo en el que se interpreta un submit */
	public abstract void setModoSubmit(int modoSubmit);

	/**
	 * Obtiene el modo en que opera la pantalla {@link #MODO_CAPTURA} o
	 * {@link #MODO_EDICION}
	 */
	public abstract int getModo();

	/** Establece el modo de operacion de la pantalla */
	public abstract void setModo(int modo);

	/** Obtiene el timeout para pactar una operacion */
	public abstract int getDealTimeout();

	/** Establece el timeout para pactar una operacion */
	public abstract void setDealTimeout(int dealTimeout);

	/** Obtiene el id del campo actual */
	public abstract int getCurrentField();

	/** Establece el id del campo actual */
	public abstract void setCurrentField(int currentField);

	/** Obtiene fecha en la que se refresco la pantalla */
	public abstract String getCurrentDate();

	/** Establece la fecha en la que se refresco la pantalla */
	public abstract void setCurrentDate(String currentDate);

	/** habilita o deshabilita el check box arbitraje */
	public abstract int getActivoArbitraje();

	/** habilita o deshabilita el check box arbitraje */
	public abstract void setActivoArbitraje(int activoArbitraje);

	/** JMPS Obtiene el tiempoRestante para pactar una operacion */
	public abstract int getTiempoRestante();

	/** JMPS Establece el tiempoRestante para pactar una operacion */
	public abstract void setTiempoRestante(int tiempoRestante);

	/** Obtiene el mensaje de confirmacion de desviacion de TC */
	public abstract String getMensajeConfirmDesvTc();

	/** Establece el mensaje de confirmacion de desviacion de TC */
	public abstract void setMensajeConfirmDesvTc(String mensajeConfirmDesvTc);

	/**
	 * Nivel de los mensajes al usuario: -1 Error, 0 Warning, 1 Exito.
	 * 
	 * @param level
	 *            El valor a asignar.
	 */
	public abstract void setLevel(int level);

	/*---------------------------------------------------------------------------------------------------------
	 * Listas para los catalogos
	 *---------------------------------------------------------------------------------------------------------*/

	/** Obtiene el catalogo de divisas para la sesion (solo se consulta una vez) */
	public abstract List getDivisas();

	/** Establece el catalogo de divisas para la sesion */
	public abstract void setDivisas(List divisas);

	/** Obtiene la lista de sistemas a cargar en el combo */
	public abstract List getSistemas();

	/** Establece la lista de sistemas a cargar en el combo */
	public abstract void setSistemas(List sistemas);

	/** Retorna la lista de tipo valor para el combo de fechas valor */
	public abstract String[] getFechasValor();

	/** Establece la lista de fechas valor */
	public abstract void setFechasValor(String[] fechasValor);

	/**
	 * Retorna la lista de productos para la divisa seleccionada en la seccion
	 * de no arbitraje
	 */
	public abstract String[] getProductos();

	/**
	 * Establece la lista de productos para la divisa seleccionada en la seccion
	 * de no arbitraje
	 */
	public abstract void setProductos(String[] productos);

	/**
	 * Retorna la lista de productos para la divisa seleccionada en la seccion
	 * de arbitraje
	 */
	public abstract String[] getProductosLiq();

	/**
	 * Establece la lista de productos para la divisa seleccionada en la seccion
	 * de arbitraje
	 */
	public abstract void setProductosLiq(String[] productosLiq);

	/** Obtiene la lista de mensajes que se presenta en pantalla */
	public abstract List getListaMensajes();

	/*---------------------------------------------------------------------------------------------------------
	 * Listas para los mensajes del banner
	 *---------------------------------------------------------------------------------------------------------*/

	/** Establece la lista de mensajes que se presentan */
	public abstract void setListaMensajes(List listaMensajes);

	/*---------------------------------------------------------------------------------------------------------
	 * MODOS CAPTURA: Modos en los que opera la pantalla, puede ser para dar de alta o para modificar una
	 * operacion de TCE
	 *---------------------------------------------------------------------------------------------------------*/

	/** Modo de operacion de la pantalla para dar de alta */
	public static final int MODO_CAPTURA = 0;

	/**
	 * Modo de operacion de la pantalla para presentar un deal encontrado en la
	 * pantalla de consulta
	 */
	public static final int MODO_EDICION = 1;

	/*---------------------------------------------------------------------------------------------------------
	 * MODOS SUBMIT: Modos en los que se interpreta un submit para esta pantalla
	 *---------------------------------------------------------------------------------------------------------*/

	/** Modo en el que se interpreta un submit para guardar la informacion */
	public static final int MODO_SUBMIT_SAVE = 10;

	// ------------

	/** Modo submit en caso de que haya cambiado el campo tipo operacion */
	public static final int MODO_SUBMIT_UPDATE_BY_TIPO_OPER_CHANGE = 21;

	/** Modo submit en caso de que haya cambiado el campo fecha valor */
	public static final int MODO_SUBMIT_UPDATE_BY_FECHA_VALOR_CHANGE = 22;

	/** Modo submit en caso de que haya cambiado el campo divisa */
	public static final int MODO_SUBMIT_UPDATE_BY_DIVISA_CHANGE = 23;

	/** Modo submit en caso de que haya cambiado el campo divisa arbitraje */
	public static final int MODO_SUBMIT_UPDATE_BY_DIVISA_LIQ_CHANGE = 24;

	/** Modo submit en caso de que haya cambiado el campo producto */
	public static final int MODO_SUBMIT_UPDATE_BY_PRODUCTO_CHANGE = 25;

	/** Modo submit en caso de que haya cambiado el campo producto arbitraje */
	public static final int MODO_SUBMIT_UPDATE_BY_PRODUCTO_LIQ_CHANGE = 26;

	// ------------

	/**
	 * Modo en el que se interpreta un submit para actualizar los calculos
	 * basados en monto
	 */
	public static final int MODO_SUBMIT_UPDATE_BY_AMOUNT_CHANGE = 50;

	/**
	 * Modo en el que se interpreta un submit para actualizar los calculos
	 * basados en contraimporte
	 */
	public static final int MODO_SUBMIT_UPDATE_BY_AGAINST_AMOUNT_CHANGE = 60;

	/**
	 * Modo en el que se interpreta un submit para actualizar los calculos
	 * basados en tipo de cambio cliente
	 */
	public static final int MODO_SUBMIT_UPDATE_BY_TCC_CHANGE = 70;

	/**
	 * Modo en el que se interpreta un submit para actualizar los calculos
	 * basados en tipo de cambio mesa
	 */
	public static final int MODO_SUBMIT_UPDATE_BY_TCM_CHANGE = 71;

	/**
	 * Modo en el que se interpreta un submit para actualizar los calculos
	 * basados en monto de arbitraje
	 */
	public static final int MODO_SUBMIT_UPDATE_BY_AMOUNT_LIQ_CHANGE = 80;

	/**
	 * Modo en el que se interpreta un submit para actualizar los calculos
	 * basados en contraimporte de arbitraje
	 */
	public static final int MODO_SUBMIT_UPDATE_BY_AGAINST_AMOUNT_LIQ_CHANGE = 90;

	/**
	 * Modo en el que se interpreta un submit para actualizar los calculos
	 * basados en tipo de cambio cliente de arbitraje
	 */
	public static final int MODO_SUBMIT_UPDATE_BY_TCC_LIQ_CHANGE = 100;

	/**
	 * Modo en el que se interpreta un submit para actualizar los calculos
	 * basados en tipo de cambio mesa de arbitraje
	 */
	public static final int MODO_SUBMIT_UPDATE_BY_TCM_LIQ_CHANGE = 110;

	// ---------

	/** Modo submit para inhabilitar cambiar el valor de arbitraje */
	public static final int MODO_SUBMIT_UPDATE_BY_ARBITRAJE_CHANGE = 120;

	/** Modo submit para buscar el nombre de un promotor */
	public static final int MODO_SUBMIT_BUSCA_NOMBRE_PROMOTOR = 130;

	/** Modo submit para buscar el nombre de un promotor */
	public static final int MODO_SUBMIT_BUSCA_CONTRATO_CORTO = 140;

	/** Modo submit para buscar el nombre de un promotor */
	public static final int MODO_SUBMIT_CANCELAR = 150;
	
	/** Constante para 100.0.  */
    public static final double NUMD_100 = 100.0;

}
