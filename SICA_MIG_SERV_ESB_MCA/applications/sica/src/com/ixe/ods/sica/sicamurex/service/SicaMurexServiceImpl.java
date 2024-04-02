package com.ixe.ods.sica.sicamurex.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;

import com.ixe.ods.sica.PosicionDelegate;
import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.model.Corte;
import com.ixe.ods.sica.model.CorteDetalle;
import com.ixe.ods.sica.model.BitacoraCorte;
import com.ixe.ods.sica.model.DealReinicioPosicion;
import com.ixe.ods.sica.model.Divisa;
import com.ixe.ods.sica.model.PosicionLog;
import com.ixe.ods.sica.posicion.vo.PosicionVO;
import com.ixe.ods.sica.sdo.CorteMurexServiceData;
import com.ixe.ods.sica.sicamurex.dto.MonitorCorteDTO;
import com.ixe.ods.sica.sicamurex.dto.MonitorDIDTO;
import com.ixe.ods.sica.sicamurex.dto.PosicionDTO;
import com.ixe.ods.sica.sicamurex.utils.ConstantesSICAMUREX;

public class SicaMurexServiceImpl implements SicaMurexService,
		ConstantesSICAMUREX {

	private PosicionDelegate posicionDelegate;
	public static final Logger LOGGER = Logger
			.getLogger(SicaMurexServiceImpl.class);
	private boolean isDivisaFrecuente = false;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ixe.ods.sica.sicamurex.service.SicaMurexService#getPosicionDivisas
	 * (java.lang.Integer, java.lang.String, boolean, boolean, boolean)
	 */
	public List getPosicionDivisas(Integer idMesaCambio, String usuario,
			boolean df, boolean dnf, boolean ma) {

		LOGGER.info(">Iniciando creacion de acumulados de compra/venta de divisas para vista previa");

		validarCorteEnviado();

		List divisasFList = getPosicionDelegate()
				.getCatalogoDivisasFrecuentes();
		List divisasNFList = getPosicionDelegate()
				.getCatalogoDivisasNoFrecuentes();
		Map banderaDivisasMap = getBanderaDivisaMap(divisasFList, divisasFList);
		List divisasMAList = getPosicionDelegate().getCatalogoDivisasMetales();
		Date fechaTOM = getPosicionDelegate().getPizarronFechaTOM();
		Date fechaSPOT = getPosicionDelegate().getPizarronFechaSPOT();
		Date fechaVFUT = getPosicionDelegate().getPizarronFechaVFUT();
		Date fecha72HR = getPosicionDelegate().getPizarronFecha72HR();
		BigDecimal tc = new BigDecimal(0);
		BigDecimal tcFondeo = new BigDecimal(0);
		List posicionList = new ArrayList();
		PosicionDTO posicionDTO = new PosicionDTO();
		// List acumuladosCVList =
		// getPosicionDelegate().getPosicionDivisas(idMesaCambio);
		Corte corte = getPosicionDelegate().getCorteMurexServiceData()
				.insertarCorteMurex(usuario);
		List acumuladosCVList = llenarPosicionVO();

		getPosicionDelegate().getCorteMurexServiceData().eliminarCorteDetalle(
				corte.getIdCorte());
		getPosicionDelegate().getCorteMurexServiceData()
				.eliminarDealsReinicioPosicion(corte.getIdCorte());

		for (Iterator iterator = acumuladosCVList.iterator(); iterator
				.hasNext();) {

			PosicionVO posicionVO = (PosicionVO) iterator.next();
			boolean isElegibleVP = validaDivisas(divisasFList, divisasNFList,
					divisasMAList, posicionVO, df, dnf, ma);
			if (isElegibleVP) {
				posicionDTO = new PosicionDTO();
				llenaDatosDealInterbancario(posicionVO, posicionDTO, corte);
				if (!posicionVO.getCompraCash().equals(new BigDecimal(0))) {
					tc = posicionVO.getCompraMnClienteCash().divide(
							posicionVO.getCompraCash(), NDECIMALES_TC,
							TIPO_REDONDEO_TC);
					tcFondeo = posicionVO.getCompraMnPizarronCash().divide(
							posicionVO.getCompraCash(), NDECIMALES_TC,
							TIPO_REDONDEO_TC);
					posicionDTO = new PosicionDTO(posicionVO.getIdDivisa(),
							CASH, COMPRA_STR, posicionVO
									.getCompraMnClienteCash().setScale(
											NDECIMALES_MONTO,
											TIPO_REDONDEO_MONTO), posicionVO
									.getCompraCash().setScale(NDECIMALES_MONTO,
											TIPO_REDONDEO_MONTO), tc, tcFondeo,
							(String) banderaDivisasMap.get(posicionVO
									.getIdDivisa()));
					posicionList.add(posicionDTO);
					insertarCorteVP(posicionDTO, corte, usuario, fechaTOM,
							fechaSPOT, fechaVFUT, fecha72HR);
				}

				if (!posicionVO.getCompraTom().equals(new BigDecimal(0))) {
					tc = posicionVO.getCompraMnClienteTom().divide(
							posicionVO.getCompraTom(), NDECIMALES_TC,
							TIPO_REDONDEO_TC);
					tcFondeo = posicionVO.getCompraMnPizarronTom().divide(
							posicionVO.getCompraTom(), NDECIMALES_TC,
							TIPO_REDONDEO_TC);
					posicionDTO = new PosicionDTO(posicionVO.getIdDivisa(),
							TOM, COMPRA_STR, posicionVO.getCompraMnClienteTom()
									.setScale(NDECIMALES_MONTO,
											TIPO_REDONDEO_MONTO), posicionVO
									.getCompraTom().setScale(NDECIMALES_MONTO,
											TIPO_REDONDEO_MONTO), tc, tcFondeo,
							(String) banderaDivisasMap.get(posicionVO
									.getIdDivisa()));
					posicionList.add(posicionDTO);
					insertarCorteVP(posicionDTO, corte, usuario, fechaTOM,
							fechaSPOT, fechaVFUT, fecha72HR);
				}

				if (!posicionVO.getCompraSpot().equals(new BigDecimal(0))) {
					tc = posicionVO.getCompraMnClienteSpot().divide(
							posicionVO.getCompraSpot(), NDECIMALES_TC,
							TIPO_REDONDEO_TC);
					tcFondeo = posicionVO.getCompraMnPizarronSpot().divide(
							posicionVO.getCompraSpot(), NDECIMALES_TC,
							TIPO_REDONDEO_TC);
					posicionDTO = new PosicionDTO(posicionVO.getIdDivisa(),
							SPOT, COMPRA_STR, posicionVO
									.getCompraMnClienteSpot().setScale(
											NDECIMALES_MONTO,
											TIPO_REDONDEO_MONTO), posicionVO
									.getCompraSpot().setScale(NDECIMALES_MONTO,
											TIPO_REDONDEO_MONTO), tc, tcFondeo,
							(String) banderaDivisasMap.get(posicionVO
									.getIdDivisa()));
					posicionList.add(posicionDTO);
					insertarCorteVP(posicionDTO, corte, usuario, fechaTOM,
							fechaSPOT, fechaVFUT, fecha72HR);
				}

				if (!posicionVO.getCompraVFut().equals(new BigDecimal(0))) {
					tc = posicionVO.getCompraMnClienteVFut().divide(
							posicionVO.getCompraVFut(), NDECIMALES_TC,
							TIPO_REDONDEO_TC);
					tcFondeo = posicionVO.getCompraMnPizarronVFut().divide(
							posicionVO.getCompraVFut(), NDECIMALES_TC,
							TIPO_REDONDEO_TC);
					posicionDTO = new PosicionDTO(posicionVO.getIdDivisa(),
							VFUT, COMPRA_STR, posicionVO
									.getCompraMnClienteVFut().setScale(
											NDECIMALES_MONTO,
											TIPO_REDONDEO_MONTO), posicionVO
									.getCompraVFut().setScale(NDECIMALES_MONTO,
											TIPO_REDONDEO_MONTO), tc, tcFondeo,
							(String) banderaDivisasMap.get(posicionVO
									.getIdDivisa()));
					posicionList.add(posicionDTO);
					insertarCorteVP(posicionDTO, corte, usuario, fechaTOM,
							fechaSPOT, fechaVFUT, fecha72HR);
				}

				if (!posicionVO.getCompra72Hr().equals(new BigDecimal(0))) {
					tc = posicionVO.getCompraMnCliente72Hr().divide(
							posicionVO.getCompra72Hr(), NDECIMALES_TC,
							TIPO_REDONDEO_TC);
					tcFondeo = posicionVO.getCompraMnPizarron72Hr().divide(
							posicionVO.getCompra72Hr(), NDECIMALES_TC,
							TIPO_REDONDEO_TC);
					posicionDTO = new PosicionDTO(posicionVO.getIdDivisa(),
							HR72, COMPRA_STR, posicionVO
									.getCompraMnCliente72Hr().setScale(
											NDECIMALES_MONTO,
											TIPO_REDONDEO_MONTO), posicionVO
									.getCompra72Hr().setScale(NDECIMALES_MONTO,
											TIPO_REDONDEO_MONTO), tc, tcFondeo,
							(String) banderaDivisasMap.get(posicionVO
									.getIdDivisa()));
					posicionList.add(posicionDTO);
					insertarCorteVP(posicionDTO, corte, usuario, fechaTOM,
							fechaSPOT, fechaVFUT, fecha72HR);
				}

				if (!posicionVO.getVentaCash().equals(new BigDecimal(0))) {
					tc = posicionVO.getVentaMnClienteCash().divide(
							posicionVO.getVentaCash(), NDECIMALES_TC,
							TIPO_REDONDEO_TC);
					tcFondeo = posicionVO.getVentaMnPizarronCash().divide(
							posicionVO.getVentaCash(), NDECIMALES_TC,
							TIPO_REDONDEO_TC);
					posicionDTO = new PosicionDTO(posicionVO.getIdDivisa(),
							CASH, VENTA_STR, posicionVO.getVentaMnClienteCash()
									.setScale(NDECIMALES_MONTO,
											TIPO_REDONDEO_MONTO), posicionVO
									.getVentaCash().setScale(NDECIMALES_MONTO,
											TIPO_REDONDEO_MONTO), tc, tcFondeo,
							(String) banderaDivisasMap.get(posicionVO
									.getIdDivisa()));
					posicionList.add(posicionDTO);
					insertarCorteVP(posicionDTO, corte, usuario, fechaTOM,
							fechaSPOT, fechaVFUT, fecha72HR);
				}

				if (!posicionVO.getVentaTom().equals(new BigDecimal(0))) {
					tc = posicionVO.getVentaMnClienteTom().divide(
							posicionVO.getVentaTom(), NDECIMALES_TC,
							TIPO_REDONDEO_TC);
					tcFondeo = posicionVO.getVentaMnPizarronTom().divide(
							posicionVO.getVentaTom(), NDECIMALES_TC,
							TIPO_REDONDEO_TC);
					posicionDTO = new PosicionDTO(posicionVO.getIdDivisa(),
							TOM, VENTA_STR, posicionVO.getVentaMnClienteTom()
									.setScale(NDECIMALES_MONTO,
											TIPO_REDONDEO_MONTO), posicionVO
									.getVentaTom().setScale(NDECIMALES_MONTO,
											TIPO_REDONDEO_MONTO), tc, tcFondeo,
							(String) banderaDivisasMap.get(posicionVO
									.getIdDivisa()));
					posicionList.add(posicionDTO);
					insertarCorteVP(posicionDTO, corte, usuario, fechaTOM,
							fechaSPOT, fechaVFUT, fecha72HR);
				}

				if (!posicionVO.getVentaSpot().equals(new BigDecimal(0))) {
					tc = posicionVO.getVentaMnClienteSpot().divide(
							posicionVO.getVentaSpot(), NDECIMALES_TC,
							TIPO_REDONDEO_TC);
					tcFondeo = posicionVO.getVentaMnPizarronSpot().divide(
							posicionVO.getVentaSpot(), NDECIMALES_TC,
							TIPO_REDONDEO_TC);
					posicionDTO = new PosicionDTO(posicionVO.getIdDivisa(),
							SPOT, VENTA_STR, posicionVO.getVentaMnClienteSpot()
									.setScale(NDECIMALES_MONTO,
											TIPO_REDONDEO_MONTO), posicionVO
									.getVentaSpot().setScale(NDECIMALES_MONTO,
											TIPO_REDONDEO_MONTO), tc, tcFondeo,
							(String) banderaDivisasMap.get(posicionVO
									.getIdDivisa()));
					posicionList.add(posicionDTO);
					insertarCorteVP(posicionDTO, corte, usuario, fechaTOM,
							fechaSPOT, fechaVFUT, fecha72HR);
				}

				if (!posicionVO.getVentaVFut().equals(new BigDecimal(0))) {
					tc = posicionVO.getVentaMnClienteVFut().divide(
							posicionVO.getVentaVFut(), NDECIMALES_TC,
							TIPO_REDONDEO_TC);
					tcFondeo = posicionVO.getVentaMnPizarronVFut().divide(
							posicionVO.getVentaVFut(), NDECIMALES_TC,
							TIPO_REDONDEO_TC);
					posicionDTO = new PosicionDTO(posicionVO.getIdDivisa(),
							VFUT, VENTA_STR, posicionVO.getVentaMnClienteVFut()
									.setScale(NDECIMALES_MONTO,
											TIPO_REDONDEO_MONTO), posicionVO
									.getVentaVFut().setScale(NDECIMALES_MONTO,
											TIPO_REDONDEO_MONTO), tc, tcFondeo,
							(String) banderaDivisasMap.get(posicionVO
									.getIdDivisa()));
					posicionList.add(posicionDTO);
					insertarCorteVP(posicionDTO, corte, usuario, fechaTOM,
							fechaSPOT, fechaVFUT, fecha72HR);
				}

				if (!posicionVO.getVenta72Hr().equals(new BigDecimal(0))) {
					tc = posicionVO.getVentaMnCliente72Hr().divide(
							posicionVO.getVenta72Hr(), NDECIMALES_TC,
							TIPO_REDONDEO_TC);
					tcFondeo = posicionVO.getVentaMnPizarron72Hr().divide(
							posicionVO.getVenta72Hr(), NDECIMALES_TC,
							TIPO_REDONDEO_TC);
					posicionDTO = new PosicionDTO(posicionVO.getIdDivisa(),
							HR72, VENTA_STR, posicionVO.getVentaMnCliente72Hr()
									.setScale(NDECIMALES_MONTO,
											TIPO_REDONDEO_MONTO), posicionVO
									.getVenta72Hr().setScale(NDECIMALES_MONTO,
											TIPO_REDONDEO_MONTO), tc, tcFondeo,
							(String) banderaDivisasMap.get(posicionVO
									.getIdDivisa()));
					posicionList.add(posicionDTO);
					insertarCorteVP(posicionDTO, corte, usuario, fechaTOM,
							fechaSPOT, fechaVFUT, fecha72HR);
				}

				LOGGER.debug("Acumulados de compra/venta agregados: Divisa "
						+ posicionVO.getIdDivisa());
			}
		}
		LOGGER.info("Finalizo la creacion de acumulados de compra/venta");
		return posicionList;
	}

	/****
	 * Metodo que llena los datos del DEAL Interbancario de reinicio de la
	 * posicion de SICA.
	 * 
	 * @param posicionVO
	 * @param posicionDTO
	 * @param corte
	 */
	private void llenaDatosDealInterbancario(PosicionVO posicionVO,
			PosicionDTO posicionDTO, Corte corte) {
		try {
			BigDecimal totalCompras = new BigDecimal(0);
			BigDecimal totalVentas = new BigDecimal(0);
			BigDecimal posicionFinal = new BigDecimal(0);
			BigDecimal totalComprasMn = new BigDecimal(0).setScale(
					NDECIMALES_MONTO, TIPO_REDONDEO_MONTO);
			BigDecimal totalVentasMn = new BigDecimal(0).setScale(
					NDECIMALES_MONTO, TIPO_REDONDEO_MONTO);
			BigDecimal posicionFinalMn = new BigDecimal(0).setScale(
					NDECIMALES_MONTO, TIPO_REDONDEO_MONTO);
			BigDecimal breakEven = new BigDecimal(0);
			DealReinicioPosicion dealRP = new DealReinicioPosicion();
			String operacionDI = "";

			totalCompras = posicionVO.getCompra72Hr()
					.add(posicionVO.getCompraCash())
					.add(posicionVO.getCompraSpot())
					.add(posicionVO.getCompraTom())
					.add(posicionVO.getCompraVFut());
			LOGGER.info("Total de compras: " + totalCompras);
			totalVentas = posicionVO.getVenta72Hr()
					.add(posicionVO.getVentaVFut())
					.add(posicionVO.getVentaCash())
					.add(posicionVO.getVentaTom())
					.add(posicionVO.getVentaSpot());
			LOGGER.info("Total de ventas: " + totalVentas);
			totalVentasMn = posicionVO.getVentaMnPizarron72Hr()
					.add(posicionVO.getVentaMnPizarronVFut())
					.add(posicionVO.getVentaMnPizarronCash())
					.add(posicionVO.getVentaMnPizarronTom())
					.add(posicionVO.getVentaMnPizarronSpot());
			LOGGER.info("Total de VentasMn: " + totalVentasMn);
			totalComprasMn = posicionVO.getCompraMnPizarron72Hr()
					.add(posicionVO.getCompraMnPizarronVFut())
					.add(posicionVO.getCompraMnPizarronCash())
					.add(posicionVO.getCompraMnPizarronTom())
					.add(posicionVO.getCompraMnPizarronSpot());
			LOGGER.info("Total de comprasMn: " + totalComprasMn);
			posicionFinalMn = totalComprasMn.add(
					posicionVO.getPosicionInicialMn()).subtract(totalVentasMn);
			LOGGER.info("Total de posicionFinalMn: " + posicionFinalMn);
			posicionFinal = totalCompras.abs()
					.add(posicionVO.getPosicionInicial())
					.subtract(totalVentas.abs());
			LOGGER.info("Total de posicionFinal: " + posicionFinal);

			if (posicionFinal.doubleValue() == 0) {
				throw new SicaException("La divisa: "
						+ posicionVO.getIdDivisa()
						+ " tiene posicion final cero");
			} else if (posicionFinal.doubleValue() > 0) {
				operacionDI = ConstantesSICAMUREX.VENTA_STR;
			} else if (posicionFinal.doubleValue() < 0) {
				operacionDI = ConstantesSICAMUREX.COMPRA_STR;
			}

			breakEven = posicionFinalMn.abs().divide(posicionFinal.abs(),
					NDECIMALES_TC, TIPO_REDONDEO_TC_DI);
			LOGGER.info("Monto DI : " + posicionFinal + "TC(Breakeven): "
					+ breakEven);
			posicionDTO.setMontoDivisaDI(posicionFinal.abs());
			posicionDTO.setTcDI(breakEven);
			posicionDTO.setOperacionDI(operacionDI);
			insertarDealReinicioPosicion(posicionVO, corte, posicionFinal,
					breakEven, dealRP, operacionDI);
			LOGGER.info("total de Compras " + totalCompras + "Total de ventas"
					+ totalVentas + "Posicion Inicial"
					+ posicionVO.getPosicionInicial());

		} catch (ArithmeticException e) {
			e.printStackTrace();
		} catch (SicaException se) {
			se.printStackTrace();
		}
	}

	/**
	 * Inserta el objeto DEAL_REINICIO_POSICION que sirve para crear del DEAL
	 * Interbancario de reinicio de la posicion.
	 * 
	 * @param posicionVO
	 * @param corte
	 * @param posicionFinal
	 * @param breakEven
	 * @param dealRP
	 * @param operacionDI
	 */
	private void insertarDealReinicioPosicion(PosicionVO posicionVO,
			Corte corte, BigDecimal posicionFinal, BigDecimal breakEven,
			DealReinicioPosicion dealRP, String operacionDI) {
		dealRP.setIdCorte(corte.getIdCorte());
		dealRP.setIdDivisa(posicionVO.getIdDivisa());
		dealRP.setMonto(posicionFinal.abs().doubleValue());
		dealRP.setTipoCambio(breakEven.doubleValue());
		dealRP.setOperacion(operacionDI);
		getPosicionDelegate().getCorteMurexServiceData()
				.insertarDealReinicioPosicion(dealRP);
	}

	/**
	 * Este metodo valida si el archivo ya se ha enviado a MUREX, en ese caso se
	 * debe impedir que se envie de nuevo.
	 */
	private void validarCorteEnviado() {

		Corte corteHoy = posicionDelegate.getCorteMurexServiceData()
				.findCorteByFechaHoy();
		if (corteHoy != null
				&& corteHoy.getEstatusCorte() != null
				&& (corteHoy.getEstatusCorte().equals(
						CorteMurexServiceData.ENVIADO_MUREX) || corteHoy
						.getEstatusCorte().equals(
								CorteMurexServiceData.PROCESADO_MUREX))) {
			throw new SicaException("El corte de hoy, ya se ha enviado a MUREX");
		}
	}

	/**
	 * Este metodo se encarga de insertar los cortes y sus detalles.
	 * 
	 * @param fechaTOM
	 *            Este campo contiene la fecha de liquidacion TOM
	 * @param fechaSPOT
	 *            Este campo contiene la fecha de liquidacion SPOT
	 * @param fechaVFUT
	 *            Este campo contiene la fecha de liquidacion VFUT
	 * @param fecha72HR
	 *            Este campo contiene la fecha de liquidacion 72 HRS
	 * @return ID del Corte.
	 */
	public int insertarCorteVP(PosicionDTO posicionDTO, Corte corte,
			String usuario, Date fechaTOM, Date fechaSPOT, Date fechaVFUT,
			Date fecha72HR) {
		LOGGER.debug("insertando acumulado de compra/venta en BD con usuario"
				+ usuario);
		String buySell = "";
		Date fechaCash = new Date();
		CorteDetalle corteDetalle = new CorteDetalle();
		corteDetalle.setIdCorte(corte.getIdCorte());
		corteDetalle.setTradeDate(new Date());
		if (posicionDTO.getOperacion().equals(COMPRA_STR)) {
			buySell = BUY_STR;
		} else {
			buySell = SELL_STR;
		}
		corteDetalle.setBuySell(buySell);
		if (isDivisaFrecuente) {
			corteDetalle.setContract(posicionDTO.getDivisa() + "/" + MXN);
		}

		if (posicionDTO.getFechaValor().equals(CASH)) {
			corteDetalle.setDeliveryDate(fechaCash);
		} else if (posicionDTO.getFechaValor().equals(TOM)) {
			corteDetalle.setDeliveryDate(fechaTOM);
		} else if (posicionDTO.getFechaValor().equals(SPOT)) {
			corteDetalle.setDeliveryDate(fechaSPOT);
		} else if (posicionDTO.getFechaValor().equals(VFUT)) {
			corteDetalle.setDeliveryDate(fechaVFUT);
		} else if (posicionDTO.getFechaValor().equals(HR72)) {
			corteDetalle.setDeliveryDate(fecha72HR);
		}

		corteDetalle.setAmount(posicionDTO.getMontoDivisa().doubleValue());
		corteDetalle.setPrice(posicionDTO.getTc().doubleValue());
		corteDetalle.setFundingCost(posicionDTO.getTcFondeo().doubleValue());
		corteDetalle.setCcy(posicionDTO.getDivisa());
		corteDetalle.setSourceComments(posicionDTO.getOperacion() + " "
				+ posicionDTO.getDivisa() + " " + posicionDTO.getFechaValor());
		return posicionDelegate.getCorteMurexServiceData()
				.registrarDetalleCorte(corteDetalle, corte.getIdCorte());
	}

	/**
	 * 
	 * @param divisasF
	 *            Lista de divisas frecuentes
	 * @param divisasNF
	 *            Lista de divisas no frecuentes
	 * @param divisasMA
	 *            Lista de metales amonedados
	 * @param posicionVO
	 *            Se validara si el DTO es elegible para la vista previa
	 * @param df
	 *            Bandera que indica si se filtran las divisas frecuentes
	 * @param dnf
	 *            Bandera que indica si se filtran las divisas no frecuentes
	 * @param ma
	 *            Bandera que indica si se filtran los metales amonedados
	 * @return boolean que indica si se agrega el posicionVO a la vista previa.
	 */
	private boolean validaDivisas(List divisasF, List divisasNF,
			List divisasMA, PosicionVO posicionVO, boolean df, boolean dnf,
			boolean ma) {

		boolean result = false;

		if (df) {
			result = buscarDivisa(divisasF, posicionVO);
			if (result == true) {
				isDivisaFrecuente = true;
			}
		}
		if (!result && dnf) {
			result = buscarDivisa(divisasNF, posicionVO);
		}
		if (!result && ma) {
			result = buscarDivisa(divisasMA, posicionVO);
		}
		return result;
	}

	private boolean buscarDivisa(List divisaList, PosicionVO posicionVO) {

		for (Iterator iterator = divisaList.iterator(); iterator.hasNext();) {
			Divisa divisaDto = (Divisa) iterator.next();
			if (divisaDto.getIdDivisa().equals(posicionVO.getIdDivisa())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Obtiene la vista previa del objeto DEAL_REINICIO_POSICION
	 */
	public List getVistaPreviaDealsReinicioPosicion() {

		List dealsReinicioPosicionList = new ArrayList();
		Corte corte = getPosicionDelegate().getCorteMurexServiceData()
				.findCorteByFechaHoy();
		List vpDRP = getPosicionDelegate().getCorteMurexServiceData()
				.findDealsReinicioPosicionByIdCorte(corte.getIdCorte());
		List divisasFList = getPosicionDelegate()
				.getCatalogoDivisasFrecuentes();
		List divisasNFList = getPosicionDelegate()
				.getCatalogoDivisasNoFrecuentes();
		Map banderaDivisasMap = getBanderaDivisaMap(divisasFList, divisasNFList);

		convertirReinicioPosicionDTO(dealsReinicioPosicionList, vpDRP,
				banderaDivisasMap);

		return dealsReinicioPosicionList;
	}

	private void convertirReinicioPosicionDTO(List listaDestino,
			List listaOrigen, Map banderaDivisasMap) {
		PosicionDTO dto;
		if (listaOrigen != null && listaOrigen.size() > 0) {
			for (Iterator iterator = listaOrigen.iterator(); iterator.hasNext();) {
				DealReinicioPosicion drp = (DealReinicioPosicion) iterator
						.next();
				dto = new PosicionDTO();
				dto.setDivisa(drp.getIdDivisa());
				dto.setFechaValor(ConstantesSICAMUREX.CASH);
				dto.setOperacion(drp.getOperacion());
				dto.setMontoDivisaDI(new BigDecimal(drp.getMonto()));
				dto.setIcono((String) banderaDivisasMap.get(drp.getIdDivisa()));
				dto.setTcDI(new BigDecimal(drp.getTipoCambio()));
				if (drp.getIdDeal() != null) {
					dto.setIdDealDI(drp.getIdDeal().intValue());
				}
				listaDestino.add(dto);
			}
		}
	}

	private void convertirPosicionDTO(Corte corte, List listaOrigen,
			List listaDestino, Map banderaDivisasMap) {
		PosicionDTO dto;
		if (listaOrigen != null && listaOrigen.size() > 0) {
			for (Iterator iterator = listaOrigen.iterator(); iterator.hasNext();) {
				CorteDetalle cd = (CorteDetalle) iterator.next();
				BigDecimal montoMn = new BigDecimal(cd.getAmount()
						* cd.getPrice()).setScale(
						ConstantesSICAMUREX.NDECIMALES_MONTO,
						ConstantesSICAMUREX.TIPO_REDONDEO_MONTO);
				dto = new PosicionDTO(
						cd.getCcy(),
						cd.getSourceComments().split(" ")[ConstantesSICAMUREX.FECHA_VALOR],
						cd.getSourceComments().split(" ")[ConstantesSICAMUREX.OPERACION],
						montoMn, new BigDecimal(cd.getAmount()),
						new BigDecimal(cd.getPrice()), new BigDecimal(cd
								.getFundingCost()), (String) banderaDivisasMap
								.get(cd.getCcy()));
				listaDestino.add(dto);
			}
		}
	}

	private Map getBanderaDivisaMap(List divisasFList, List divisasNFList) {
		List catalogoDivisas = new ArrayList();
		catalogoDivisas.addAll(divisasFList);
		catalogoDivisas.addAll(divisasNFList);

		Map banderaMap = new HashMap();
		for (Iterator iterator = catalogoDivisas.iterator(); iterator.hasNext();) {
			Divisa divisa = (Divisa) iterator.next();
			banderaMap.put(divisa.getIdDivisa(), divisa.getIcono());
		}
		return banderaMap;
	}

	public PosicionDelegate getPosicionDelegate() {
		return posicionDelegate;
	}

	public void setPosicionDelegate(PosicionDelegate posicionDelegate) {
		this.posicionDelegate = posicionDelegate;
	}

	public boolean isDivisaFrecuente() {
		return isDivisaFrecuente;
	}

	public void setDivisaFrecuente(boolean isDivisaFrecuente) {
		this.isDivisaFrecuente = isDivisaFrecuente;
	}

	/**
	 * Obtiene una lista de los acumulados de compra venta de divisas de hoy.
	 */
	public List getCorteDetalleByFechaHoy() {
		List posicionDTOLista = new ArrayList();
		PosicionDelegate posicionDelegate = getPosicionDelegate();
		CorteMurexServiceData serviceData = posicionDelegate
				.getCorteMurexServiceData();
		Corte corte = serviceData.findCorteByFechaHoy();
		List corteDetalleLista = serviceData.findDetallesCortebyIdCorte(corte
				.getIdCorte());
		List divisasFList = posicionDelegate.getCatalogoDivisasFrecuentes();
		List divisasNFList = posicionDelegate.getCatalogoDivisasNoFrecuentes();
		Map banderaMapa = getBanderaDivisaMap(divisasFList, divisasNFList);
		convertirPosicionDTO(corte, corteDetalleLista, posicionDTOLista,
				banderaMapa);
		return posicionDTOLista;
	}

	/**
	 * Obtiene una lista de los DEAL_REINICIO_POSICION creados hoy.
	 */
	public List getDealsInterbancariosByFechaHoy() {
		PosicionDelegate posicionDelegate = getPosicionDelegate();
		CorteMurexServiceData serviceData = posicionDelegate
				.getCorteMurexServiceData();
		Corte corte = serviceData.findCorteByFechaHoy();
		List dealsReinicioPosicion = serviceData
				.findDealsReinicioPosicionByIdCorte(corte.getIdCorte());
		List posicionLista = new ArrayList();
		List divisasFList = posicionDelegate.getCatalogoDivisasFrecuentes();
		List divisasNFList = posicionDelegate.getCatalogoDivisasNoFrecuentes();
		Map banderaDivisasMap = getBanderaDivisaMap(divisasFList, divisasNFList);

		convertirReinicioPosicionDTO(posicionLista, dealsReinicioPosicion,
				banderaDivisasMap);

		return posicionLista;
	}

	public Corte findCorteByFechaHoy() {
		return getPosicionDelegate().getCorteMurexServiceData()
				.findCorteByFechaHoy();
	}

	public BitacoraCorte findMaxBitacoraCorteByIdCorte(Corte corte) {
		return getPosicionDelegate().getCorteMurexServiceData()
				.findMaxBitacoraCorteByIdCorte(corte);
	}

	public String getFechaHoy() {
		return getPosicionDelegate().getCorteMurexServiceData().getFechaHoy();
	}

	public Integer findNumCorteByFechaHoy() {
		return getPosicionDelegate().getCorteMurexServiceData()
				.findNumCorteByFechaHoy();
	}

	public List findDatosMonitorDI(Date fecha, Divisa divisa) {

		List corteDetalleList = new ArrayList();
		if (divisa != null && divisa.getDescripcion() != null) {
			if (divisa.getDescripcion().equals(
					ConstantesSICAMUREX.TODAS_LAS_DIVISAS)) {
				corteDetalleList = posicionDelegate.getCorteMurexServiceData()
						.findDealReiniciobyfecha(fecha);
			} else {
				corteDetalleList = posicionDelegate.getCorteMurexServiceData()
						.findDealReiniciobyfechaAndDivisa(fecha, divisa);
			}
		} else {
			throw new SicaException("Favor de escoger una divisa");
		}

		return llenarMonitorDI(corteDetalleList);

	}

	/**
	 * Se actualiza el Estado del SC_CORTE a Procesado
	 */
	public void actualizarEstadoCorteProcesado(String usuario) {
		Corte corte = findCorteByFechaHoy();
		posicionDelegate.getCorteMurexServiceData().actualizarEstatusCorte(
				corte.getIdCorte(), usuario,
				ConstantesSICAMUREX.CORTE_PROCESADO,
				ConstantesSICAMUREX.CORTE_PROCESADO + " " + corte.getIdCorte());

	}

	/**
	 * Se verifica si la operacion es VENTA o COMPRA.
	 */
	public void actualizarReinicioPosicion(String usuario, int resultDeal,
			Divisa divisa, boolean isCompra) {
		String operacion = "";

		if (isCompra) {
			operacion = ConstantesSICAMUREX.COMPRA_STR;
		} else {
			operacion = ConstantesSICAMUREX.VENTA_STR;
		}
		posicionDelegate.getCorteMurexServiceData().actualizarReinicioPosicion(
				usuario, resultDeal, divisa.getIdDivisa(), operacion);

	}

	/**
	 * Metodo que obtiene los Detalles del corte por fecha y divisa.
	 */
	public List getCorteDetalleByFechaAndDivisa(Date fecha, Divisa divisa) {
		List corteDetalleList = new ArrayList();
		if (divisa != null && divisa.getDescripcion() != null) {
			if (divisa.getDescripcion().equals(
					ConstantesSICAMUREX.TODAS_LAS_DIVISAS)) {
				corteDetalleList = posicionDelegate.getCorteMurexServiceData()
						.getCorteDetalleByFecha(fecha);
			} else {
				corteDetalleList = posicionDelegate.getCorteMurexServiceData()
						.getCorteDetalleByFechaAndDivisa(fecha, divisa);
			}
		} else {
			throw new SicaException("Favor de escoger una divisa");
		}

		return llenarMonitorCorte(corteDetalleList);
	}

	/**
	 * Metodo que se encarga de llenar una lista de MonitorCorteDTO para el
	 * Monitor de Cortes para la capa de Vista
	 * 
	 * @param corteDetalleList
	 * @return
	 */
	private List llenarMonitorCorte(List corteDetalleList) {
		List monitorDList = new ArrayList();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");
		List divisasFList = getPosicionDelegate()
				.getCatalogoDivisasFrecuentes();
		Map banderaDivisasMap = getBanderaDivisaMap(divisasFList,
				new ArrayList());
		for (Iterator iterator = corteDetalleList.iterator(); iterator
				.hasNext();) {

			MonitorCorteDTO mCorteDTO = new MonitorCorteDTO();

			CorteDetalle corteDetalle = (CorteDetalle) iterator.next();
			mCorteDTO.setIdCorte(corteDetalle.getIdCorte());
			mCorteDTO.setFechaCorteEM(dateFormat.format(corteDetalle
					.getTradeDate()));
			mCorteDTO.setIcono((String) banderaDivisasMap.get(corteDetalle
					.getCcy()));
			mCorteDTO
					.setFechaValor(corteDetalle.getSourceComments().split(" ")[ConstantesSICAMUREX.FECHA_VALOR]);
			mCorteDTO
					.setOperacion(corteDetalle.getSourceComments().split(" ")[ConstantesSICAMUREX.OPERACION]);
			mCorteDTO.setMontoDivisa(new BigDecimal(corteDetalle.getAmount()));
			mCorteDTO.setTc(new BigDecimal(corteDetalle.getPrice()));
			mCorteDTO
					.setTcFondeo(new BigDecimal(corteDetalle.getFundingCost()));
			mCorteDTO.setDivisa(corteDetalle.getCcy());
			Corte corte = getPosicionDelegate().getCorteMurexServiceData()
					.findCorteById(corteDetalle.getIdCorte());
			mCorteDTO.setStatusCorte(corte.getEstatusCorte());
			monitorDList.add(mCorteDTO);

		}
		return monitorDList;
	}

	/**
	 * Metodo que se encarga de llenar una lista de MonitorCorteDTO para el
	 * Monitor de Cortes para la capa de Vista
	 * 
	 * @param corteDetalleList
	 * @return
	 */
	private List llenarMonitorDI(List reinicioPosicionList) {
		List monitorDIList = new ArrayList();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");
		List divisasFList = getPosicionDelegate()
				.getCatalogoDivisasFrecuentes();
		Map banderaDivisasMap = getBanderaDivisaMap(divisasFList,
				new ArrayList());
		for (Iterator iterator = reinicioPosicionList.iterator(); iterator
				.hasNext();) {

			MonitorDIDTO mCorteDTO = new MonitorDIDTO();

			DealReinicioPosicion reinicioPosicion = (DealReinicioPosicion) iterator
					.next();
			mCorteDTO.setIdCorte(reinicioPosicion.getIdCorte());
			mCorteDTO.setFechaUltMod(dateFormat.format(reinicioPosicion
					.getCorte().getFechaUltMod()));
			mCorteDTO.setIcono((String) banderaDivisasMap.get(reinicioPosicion
					.getIdDivisa()));
			mCorteDTO.setDivisa(reinicioPosicion.getIdDivisa());
			mCorteDTO.setFechaValor(ConstantesSICAMUREX.CASH);
			mCorteDTO.setOperacion(reinicioPosicion.getOperacion());
			mCorteDTO
					.setMontoDivisa(new BigDecimal(reinicioPosicion.getMonto()));
			mCorteDTO.setTc(new BigDecimal(reinicioPosicion.getTipoCambio()));
			if (reinicioPosicion.getIdDeal() != null) {
				mCorteDTO.setIdDeal(reinicioPosicion.getIdDeal().intValue());
			} else {
				mCorteDTO.setIdDeal(0);
			}
			monitorDIList.add(mCorteDTO);

		}
		return monitorDIList;
	}

	/**
	 * Obtiene acumulados de CV para efecto de enviar corte a Murex
	 */
	public void getAcumuladosCV() {

		List posicionLog = new ArrayList();
		LOGGER.info(">Iniciando " + new Date());
		Integer numCortesHoy = findNumCorteByFechaHoy();
		// posicionDelegate.getCorteMurexServiceData().getFechaHoy();

		// if (numCortesHoy.intValue() == 0) {
		// List posicionLog =
		// getSicaServiceData().getPosicionesLogByFecha(posicionDelegate.getCorteMurexServiceData().getFechaHoy());
		// posicionLog =
		// getSicaServiceData().getPosicionesLogByFecha("08/09/2014");
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH, 7);
		cal.set(Calendar.MONTH, 9);
		cal.set(Calendar.YEAR, 2014);
		Date date = new Date(cal.getTimeInMillis());
		LOGGER.info(">Inicia Select " + new Date());
		posicionLog = getPosicionDelegate()
				.getPosicionLogByFecha(getFechaHoy());
		LOGGER.info(">Temina Select " + new Date());
		LOGGER.info(">Esta es la fecha que le pasa " + getFechaHoy());
		LOGGER.info(">Se van a insertar " + posicionLog.size() + "  Registros");

		for (int i = 0; posicionLog.size() > i; i++) {
			posicionDelegate.getCorteMurexServiceData().insertLogCorte(
					(PosicionLog) posicionLog.get(i));
		}
		// }

		// else{
		// List maxPosicionLog =
		// posicionDelegate.getCorteMurexServiceData().findMaxIdPosicionLog();
		// PosicionLogCorte plc = (PosicionLogCorte) maxPosicionLog.get(0);
		//
		// List posicionLog1 =
		// getPosicionDelegate().getPosicionesLogMayoresA(new
		// Integer(plc.getIdPosicionLog()));
		//
		// for (int i=0;posicionLog1.size()>i;i++){
		// posicionDelegate.getCorteMurexServiceData().insertLogCorte((PosicionLog)posicionLog1.get(i));
		// }
		//
		// }
		LOGGER.info(">Fin" + new Date() + "  Registros insertados "
				+ posicionLog.size());
	}

	/**
	 * Obtiene SUMATORIA de acumulados de CV agupados por
	 * DIVISA,TIPO_VALOR,TIPO_OPERACION y ID_CORTE
	 * 
	 * @param string
	 */
	public List getSumatoriaAcumuladosCV(String string) {
		List sumatoriaCV = new ArrayList();

		sumatoriaCV = posicionDelegate.getCorteMurexServiceData()
				.realizaSumatoriaCV(string);

		return sumatoriaCV;

	}

	/**
	 * Actualiza los id_corte de la tabla sc_posicion_log_cortes del ultimo
	 * corte generado ya que fue enviado a MX
	 */
	public void setIdCortePorEnvioMX() {
		posicionDelegate.getCorteMurexServiceData().setIdCortePorEnvioMX();
	}

	public List llenarPosicionVO() {
		List sumatoriaCV = new ArrayList();
		invocarSP();

		List posicionDivisas = new ArrayList();
		// List divisas =
		// posicionDelegate.getCorteMurexServiceData().getDistintasDivisas();
		List divisas = new ArrayList();
		divisas = posicionDelegate.getCatalogoDivisasFrecuentes();

		for (Iterator iterator = divisas.iterator(); iterator.hasNext();) {
			Divisa div = (Divisa) iterator.next();
			sumatoriaCV = getSumatoriaAcumuladosCV(div.getIdDivisa());
			PosicionVO acumpvo = new PosicionVO();
			acumpvo.setIdDivisa(div.getIdDivisa());
			LOGGER.info("La lista de sumatoriaCV tiene " + sumatoriaCV.size()
					+ " elementos");
			// for (Iterator itdiv = divisas.iterator(); itdiv.hasNext();) {
			for (Iterator it = sumatoriaCV.iterator(); it.hasNext();) {

				PosicionVO pvo = (PosicionVO) it.next();
				LOGGER.info("Datos pvo "
						+ ToStringBuilder.reflectionToString(pvo));

				if (pvo.getTipoValor().trim().equals(CASH)) {
					if (pvo.getTipoOperacion().trim().equals(COMPRA)) {
						acumpvo.setCompraCash(pvo.getMonto());
						acumpvo.setCompraMnClienteCash(pvo.getMontoMn());
						acumpvo.setCompraMnPizarronCash(pvo.getMontoMnMesa());
					} else {
						acumpvo.setVentaCash(pvo.getMonto());
						acumpvo.setVentaMnClienteCash(pvo.getMontoMn());
						acumpvo.setVentaMnPizarronCash(pvo.getMontoMnMesa());
					}

				}

				if (pvo.getTipoValor().trim().equals(TOM)) {
					if (pvo.getTipoOperacion().trim().equals(COMPRA)) {
						acumpvo.setCompraTom(pvo.getMonto());
						acumpvo.setCompraMnClienteTom(pvo.getMontoMn());
						acumpvo.setCompraMnPizarronTom(pvo.getMontoMnMesa());
					} else {
						acumpvo.setVentaTom(pvo.getMonto());
						acumpvo.setVentaMnClienteTom(pvo.getMontoMn());
						acumpvo.setVentaMnPizarronTom(pvo.getMontoMnMesa());
					}

				}

				if (pvo.getTipoValor().trim().equals(SPOT)) {
					if (pvo.getTipoOperacion().trim().equals(COMPRA)) {
						acumpvo.setCompraSpot(pvo.getMonto());
						acumpvo.setCompraMnClienteSpot(pvo.getMontoMn());
						acumpvo.setCompraMnPizarronSpot(pvo.getMontoMnMesa());
					} else {
						acumpvo.setVentaSpot(pvo.getMonto());
						acumpvo.setVentaMnClienteSpot(pvo.getMontoMn());
						acumpvo.setVentaMnPizarronSpot(pvo.getMontoMnMesa());
					}

				}

				if (pvo.getTipoValor().trim().equals(VFUT)) {
					if (pvo.getTipoOperacion().trim().equals(COMPRA)) {
						acumpvo.setCompraVFut(pvo.getMonto());
						acumpvo.setCompraMnClienteVFut(pvo.getMontoMn());
						acumpvo.setCompraMnPizarronVFut(pvo.getMontoMnMesa());
					} else {
						acumpvo.setVentaVFut(pvo.getMonto());
						acumpvo.setVentaMnClienteVFut(pvo.getMontoMn());
						acumpvo.setVentaMnPizarronVFut(pvo.getMontoMnMesa());
					}

				}

				if (pvo.getTipoValor().trim().equals(HR72)) {
					if (pvo.getTipoOperacion().trim().equals(COMPRA)) {
						acumpvo.setCompra72Hr(pvo.getMonto());
						acumpvo.setCompraMnCliente72Hr(pvo.getMontoMn());
						acumpvo.setCompraMnPizarron72Hr(pvo.getMontoMnMesa());
					} else {
						acumpvo.setVenta72Hr(pvo.getMonto());
						acumpvo.setVentaMnCliente72Hr(pvo.getMontoMn());
						acumpvo.setVentaMnPizarron72Hr(pvo.getMontoMnMesa());
					}

				}

			}
			LOGGER.info("SE agrego el dato "
					+ ToStringBuilder.reflectionToString(acumpvo));
			posicionDivisas.add(acumpvo);
		}
		LOGGER.info("Los elementos de posicionDivisas "
				+ posicionDivisas.size());
		return posicionDivisas;
	}

	public void invocarSP() {
		posicionDelegate.getCorteMurexServiceData().invocarSP();
	}

}
