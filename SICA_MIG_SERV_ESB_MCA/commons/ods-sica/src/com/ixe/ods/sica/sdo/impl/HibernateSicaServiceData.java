/*
 * $Id: HibernateSicaServiceData.java,v 1.102.2.5.6.7.2.1.4.5.2.2.2.6.2.2.6.2.6.4.2.1.2.2.2.1.6.1.2.2.8.6.2.11.6.2 2020/12/03 21:59:07 mejiar Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 - 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica.sdo.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.hibernate.Criteria;
import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.expression.Expression;
import net.sf.hibernate.expression.MatchMode;
import net.sf.hibernate.type.IntegerType;
import net.sf.hibernate.type.StringType;
import net.sf.hibernate.type.Type;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate.HibernateCallback;
import org.springframework.orm.hibernate.SessionFactoryUtils;

import com.ixe.contratacion.ContratacionException;
import com.ixe.ods.bup.model.ContratoSica;
import com.ixe.ods.bup.model.CuentaContrato;
import com.ixe.ods.bup.model.CuentaEjecutivo;
import com.ixe.ods.bup.model.Direccion;
import com.ixe.ods.bup.model.EjecutivoOrigen;
import com.ixe.ods.bup.model.EmpleadoIxe;
import com.ixe.ods.bup.model.FechaNoLaboral;
import com.ixe.ods.bup.model.Pais;
import com.ixe.ods.bup.model.Persona;
import com.ixe.ods.bup.model.PersonaCuentaRol;
import com.ixe.ods.bup.model.PersonaFisica;
import com.ixe.ods.bup.model.PersonaMoral;
import com.ixe.ods.bup.model.SectorEconomico;
import com.ixe.ods.bup.model.TipoIva;
import com.ixe.ods.seguridad.model.AsociacionFacultad;
import com.ixe.ods.seguridad.model.ISistema;
import com.ixe.ods.seguridad.model.IUsuario;
import com.ixe.ods.seguridad.model.Jerarquia;
import com.ixe.ods.seguridad.model.NodoJerarquia;
import com.ixe.ods.seguridad.model.Sistema;
import com.ixe.ods.sica.Constantes;
import com.ixe.ods.sica.FacultySystem;
import com.ixe.ods.sica.Num;
import com.ixe.ods.sica.Redondeador;
import com.ixe.ods.sica.SicaAlertasService;
import com.ixe.ods.sica.SicaConsultaDocumentacionService;
import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.SicaValidaClienteMontoTresMilUSDService;
import com.ixe.ods.sica.SicaValidationException;
import com.ixe.ods.sica.dao.BitacoraEnviadasDao;
import com.ixe.ods.sica.dao.ExtraDao;
import com.ixe.ods.sica.dto.DetalleOperacionTceDto;
import com.ixe.ods.sica.dto.InfoCuentaAltamiraDto;
import com.ixe.ods.sica.dto.OperacionTceDto;
import com.ixe.ods.sica.model.Actividad;
import com.ixe.ods.sica.model.Broker;
import com.ixe.ods.sica.model.BupActividadEconomica;
import com.ixe.ods.sica.model.BupMunicipio;
import com.ixe.ods.sica.model.Canal;
import com.ixe.ods.sica.model.CanalMesa;
import com.ixe.ods.sica.model.Cliente;
import com.ixe.ods.sica.model.CodigoPostalListaBlanca;
import com.ixe.ods.sica.model.CombinacionDivisa;
import com.ixe.ods.sica.model.ComplementoDatos;
import com.ixe.ods.sica.model.ContraparteBanxico;
import com.ixe.ods.sica.model.Contrato;
import com.ixe.ods.sica.model.CuentaAltamira;
import com.ixe.ods.sica.model.Deal;
import com.ixe.ods.sica.model.DealDetalle;
import com.ixe.ods.sica.model.DealHelper;
import com.ixe.ods.sica.model.DealPosicion;
import com.ixe.ods.sica.model.DetalleReporteDiario;
import com.ixe.ods.sica.model.Divisa;
import com.ixe.ods.sica.model.EmpresaCambios;
import com.ixe.ods.sica.model.Estado;
import com.ixe.ods.sica.model.FactorDivisaActual;
import com.ixe.ods.sica.model.GrupoTrabajo;
import com.ixe.ods.sica.model.GrupoTrabajoPromotor;
import com.ixe.ods.sica.model.IPlantilla;
import com.ixe.ods.sica.model.IPlantillaCuentaIxe;
import com.ixe.ods.sica.model.IPlantillaIntl;
import com.ixe.ods.sica.model.IPlantillaNacional;
import com.ixe.ods.sica.model.IPlantillaTranNacional;
import com.ixe.ods.sica.model.InfoFactura;
import com.ixe.ods.sica.model.LimiteEfectivo;
import com.ixe.ods.sica.model.LimitesRestriccionOperacion;
import com.ixe.ods.sica.model.LineaCambio;
import com.ixe.ods.sica.model.LineaOperacion;
import com.ixe.ods.sica.model.LineaOperacionLog;
import com.ixe.ods.sica.model.MaximoDeal;
import com.ixe.ods.sica.model.MensajeTce;
import com.ixe.ods.sica.model.MesaCambio;
import com.ixe.ods.sica.model.MontoMaximoSucursal;
import com.ixe.ods.sica.model.MunicipioListaBlanca;
import com.ixe.ods.sica.model.NitPersona;
import com.ixe.ods.sica.model.ParametroSica;
import com.ixe.ods.sica.model.PersonaListaBlanca;
import com.ixe.ods.sica.model.Plantilla;
import com.ixe.ods.sica.model.PlantillaCuentaIxe;
import com.ixe.ods.sica.model.PlantillaIntl;
import com.ixe.ods.sica.model.PlantillaNacional;
import com.ixe.ods.sica.model.PlantillaPantalla;
import com.ixe.ods.sica.model.PlantillaTranNacional;
import com.ixe.ods.sica.model.Posicion;
import com.ixe.ods.sica.model.PosicionDetalle;
import com.ixe.ods.sica.model.PosicionLog;
import com.ixe.ods.sica.model.PrecioReferenciaActual;
import com.ixe.ods.sica.model.Propiedad;
import com.ixe.ods.sica.model.RecoUtilidad;
import com.ixe.ods.sica.model.RegulatorioDatosPM;
import com.ixe.ods.sica.model.RegulatorioInstitucion;
import com.ixe.ods.sica.model.RegulatorioPerfil;
import com.ixe.ods.sica.model.RenglonPizarron;
import com.ixe.ods.sica.model.Reverso;
import com.ixe.ods.sica.model.Spread;
import com.ixe.ods.sica.model.SpreadActual;
import com.ixe.ods.sica.model.SpreadActualPK;
import com.ixe.ods.sica.model.Swap;
import com.ixe.ods.sica.model.TipoBloqueo;
import com.ixe.ods.sica.model.TipoPizarron;
import com.ixe.ods.sica.model.TokenTeller;
import com.ixe.ods.sica.model.TraspasoMesa;
//import com.ixe.ods.sica.pages.BloqueaContrato;
import com.ixe.ods.sica.sdo.LineaCambioServiceData;
import com.ixe.ods.sica.sdo.PizarronServiceData;
import com.ixe.ods.sica.sdo.SicaServiceData;
import com.ixe.ods.sica.sdo.dto.TipoOperacionDealDto;
import com.ixe.ods.sica.services.DealService;
import com.ixe.ods.sica.services.GeneralMailSender;
import com.ixe.ods.sica.services.H2HService;
import com.ixe.ods.sica.services.LineaCreditoConstantes;
import com.ixe.ods.sica.services.LineaCreditoMensajes;
import com.ixe.ods.sica.services.ValorFuturoService;
import com.ixe.ods.sica.services.impl.GeneradorReportesRegulatoriosImpl;
import com.ixe.ods.sica.utils.DateUtils;
import com.ixe.treasury.dom.common.FormaPagoLiq;
import com.legosoft.hibernate.type.SiNoType;

/**
 * Implementaci&oacute;n de los Servicios del SICA.
 * 
 * @author Jean C. Favila, Javier Cordova (jcordova)
 * @version $Revision: 1.102.2.5.6.7.2.1.4.5.2.2.2.6.2.2.6.2.6.4.2.1.2.2.2.1.6.1.2.2.8.6.2.11.6.2 $ $Date: 2012/08/26
 *          00:07:22 $
 */
public class HibernateSicaServiceData extends AbstractHibernateSicaServiceData
		implements SicaServiceData {
	
	/** The Constant log. */
	private static final Logger log = Logger.getLogger(HibernateSicaServiceData.class);

	/**
	 * Constructor por default.
	 */
	public HibernateSicaServiceData() {
		super();
	}

	/**
	 * @see SicaServiceData#activarPlantilla(int).
	 */
	public void activarPlantilla(int idPlantilla) {
		IPlantilla plantilla = (IPlantilla) find(Plantilla.class, new Integer(
				idPlantilla));
		if (plantilla instanceof IPlantillaTranNacional) {
			IPlantillaTranNacional plantillaTranNal = (IPlantillaTranNacional) plantilla;
			plantillaTranNal
					.setStatusPlantilla(PlantillaTranNacional.STATUS_PLANTILLA_ACTIVA);
			plantillaTranNal.setUltimaModificacion(new Date());
			update(plantillaTranNal);
		} else if (plantilla instanceof IPlantillaIntl) {
			IPlantillaIntl plantillaInt = (IPlantillaIntl) plantilla;
			plantillaInt
					.setStatusPlantilla(PlantillaIntl.STATUS_PLANTILLA_ACTIVA);
			plantillaInt.setUltimaModificacion(new Date());
			update(plantillaInt);
		} else {
			plantilla.setStatusPlantilla(Plantilla.STATUS_PLANTILLA_ACTIVA);
			plantilla.setUltimaModificacion(new Date());
			update(plantilla);
		}
		List detallesDeals = getHibernateTemplate().find(
				"FROM DealDetalle AS dd WHERE "
						+ "dd.plantilla.idPlantilla = ?",
				new Integer(idPlantilla));
		for (Iterator it = detallesDeals.iterator(); it.hasNext();) {
			DealDetalle detalle = (DealDetalle) it.next();
			int indice = detalle.getDeal().isInterbancario() ? DealDetalle.EV_IND_INT_DOCUMENTACION
					: DealDetalle.EV_IND_DOCUMENTACION;
			if (!detalle.isCancelado()
					&& Deal.EV_SOLICITUD.equals(detalle
							.decodificarEvento(indice))) {
				detalle.setEvento(Deal.EV_APROBACION, indice);
				update(detalle);
			}
		}
	}

	/**
	 * @see SicaServiceData#actualizarMontoOperadoSucursal(String, String,
	 *      String, boolean, java.math.BigDecimal).
	 */
	public void actualizarMontoOperadoSucursal(String idCanal, String idDivisa,
			String claveFormaLiquidacion, boolean recibimos, BigDecimal monto) {
		List posiciones = getHibernateTemplate()
				.find(
						"FROM MontoMaximoSucursal AS mms WHERE "
								+ "mms.canal.idCanal = ? AND mms.divisa.idDivisa = ? AND "
								+ "mms.claveFormaLiquidacion = ?",
						new Object[] { idCanal, idDivisa, claveFormaLiquidacion });
		if (posiciones.isEmpty()) {
			throw new SicaException(
					"No se encontr\u00f3 el registro de montos m\u00e1ximos para "
							+ "el canal " + idCanal + " y divisa " + idDivisa
							+ ".");
		}
		MontoMaximoSucursal mms = (MontoMaximoSucursal) posiciones.get(0);
		BigDecimal nuevoMontoOperado = recibimos ? mms.getMontoOperado().add(
				monto) : mms.getMontoOperado().subtract(monto);
		if (nuevoMontoOperado.doubleValue() > mms.getMontoMaximo()
				.doubleValue()) {
			throw new SicaException(
					"La operaci\u00f3n excede el monto m\u00e1ximo autorizado "
							+ "para la sucursal.");
		}
		mms.setMontoOperado(nuevoMontoOperado);
		update(mms);
		revisarAlertasSucursales(mms);
	}

	/**
	 * @see SicaServiceData#balancearDealInterbancario(com.ixe.ods.sica.model.Deal)
	 *      .
	 * 
	 * @param deal
	 *            El deal a revisar.
	 */
	public void balancearDealInterbancario(Deal deal) {
		if (!deal.isInterbancario()) {
			return;
		}

		double diferencia = Math.abs(deal.getBalance());

		debug("diferencia " + diferencia);
		if (diferencia >= 0.01) {
			debug("balanceando...");
			boolean esMayorRecibimos = deal.getTotalRecibimos() > deal
					.getTotalEntregamos();
			DealDetalle detDivRef = null;

			// Encontramos un detalle de la divisa de referencia que no haya
			// llegado al SITE:
			for (Iterator it = deal.getDetalles().iterator(); it.hasNext();) {
				DealDetalle det = (DealDetalle) it.next();

				if (det.getDivisa().getIdDivisa().equals(
						deal.getCanalMesa().getMesaCambio()
								.getDivisaReferencia().getIdDivisa())
						&& !DealDetalle.STATUS_DET_CANCELADO.equals(det
								.getStatusDetalleDeal())
						&& det.getIdLiquidacionDetalle() == null) {
					detDivRef = det;
					break;
				}
			}
			debug("DetDivRef " + detDivRef);
			if (detDivRef != null) {
				if (detDivRef.isRecibimos()) {
					detDivRef.setMonto(esMayorRecibimos ? detDivRef.getMonto()
							- diferencia : detDivRef.getMonto() + diferencia);
				} else {
					detDivRef.setMonto(esMayorRecibimos ? detDivRef.getMonto()
							+ diferencia : detDivRef.getMonto() - diferencia);
				}
				update(detDivRef);
			} else {
				warn("\n\n\nNo se puede balancear el deal interbancario no. "
						+ deal.getIdDeal());
			}
		}
	}

	private void revisarAlertasSucursales(MontoMaximoSucursal mms) {
		ParametroSica paramAviso = findParametro(ParametroSica.PORC_AVISO_SUCURSALES);
		ParametroSica paramAlerta = findParametro(ParametroSica.PORC_ALERTA_SUCURSALES);
		List personas = new ArrayList();
		ExtraDao extraDao = (ExtraDao) _appContext.getBean("extraDao");
		// Alarma:
		if (mms.getPorcentaje() > Double.valueOf(paramAlerta.getValor())
				.doubleValue()) {
			personas.addAll(extraDao.findPersonasWithFacultyAndSystem(
					"SICA_SUC_ALRM_ALTA", "SICA"));
		}
		// Solo aviso:
		if (mms.getPorcentaje() > Double.valueOf(paramAviso.getValor())
				.doubleValue()) {
			personas.addAll(extraDao.findPersonasWithFacultyAndSystem(
					"SICA_SUC_ALRM_MEDIA", "SICA"));
		}
		if (!personas.isEmpty()) {
			for (Iterator it = personas.iterator(); it.hasNext();) {
				Integer idPersona = (Integer) it.next();
				getSicaAlertasService().generaAlerta("SC_SUCS", idPersona,
						mms.getContextoAlertas());
			}
		}
	}

	/**
	 * @see SicaServiceData#afectarPosicion(com.ixe.ods.sica.model.DealDetalle).
	 */
	public void afectarPosicion(DealDetalle det) {
		if (det.isEvento(DealDetalle.EV_IND_GRAL_AFECTADA_POSICION,
				new String[] { Deal.EV_NEGACION })) {
			return;
		}
		Divisa div = det.getDivisa();
		if (!Deal.EV_POSICION.equals(det
				.decodificarEvento(DealDetalle.EV_IND_GRAL_AFECTADA_POSICION))
				&& !Divisa.PESO.equals(div.getIdDivisa())) {
			String fechaValor = ((PizarronServiceData) _appContext
					.getBean("pizarronServiceData")).fechaValorParaCancelacion(
					det.getDeal().getFechaCaptura(), det.getDeal()
							.getTipoValor(), false);
			PrecioReferenciaActual pr = findPrecioReferenciaActual();
			PosicionLog posicionLog = new PosicionLog(det, pr.getPreRef()
					.getPrecioSpot().doubleValue(), fechaValor);
			det.setEvento(Deal.EV_POSICION,
					DealDetalle.EV_IND_GRAL_AFECTADA_POSICION);
			store(posicionLog);
			update(det);
		}
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#cambiarEstadosSistema().
	 */
	public void cambiarEstadosSistema() {
		Estado actual = findEstadoSistemaActual();
		if (actual.getHoraFin() != null
				&& actual.getHoraFin()
						.compareTo(HOUR_FORMAT.format(new Date())) < 0) {
			actual.setActual(false);
			update(actual);
			actual.getSiguienteEstado().setActual(true);
			actual.getSiguienteEstado().setHoraInicio(
					HOUR_FORMAT.format(new Date()));
			update(actual.getSiguienteEstado());
		}
	}

	/**
	 * Crea un objeto Actividad, inicializando el nombre del proceso y de la
	 * actividad, el deal y el detalle de deal al que se refiere.
	 * 
	 * @param proceso
	 *            El nombre del proceso Actividad.PROC_DEAL_NORMAL |
	 *            Actividad.PROC_DEAL_INTERBANCARIO |
	 *            Actividad.PROC_CANCELACION_DEAL.
	 * @param nombre
	 *            El nombre de la actividad.
	 * @param deal
	 *            La referencia al deal.
	 * @param det
	 *            La referencia a un detalle del deal (puede ser null).
	 * @return Actividad.
	 */
	protected Actividad wfCrearActividad(String proceso, String nombre,
			Deal deal, DealDetalle det) {
		return wfCrearActividad(proceso, nombre, deal, det, null);
	}

	/**
	 * Crea un objeto Actividad, inicializando el nombre del proceso y de la
	 * actividad, el deal y el detalle de deal al que se refiere. El comentario
	 * ser&aacute; almacenado en la columna de resultado de la actividad.
	 * 
	 * @param proceso
	 *            El nombre del proceso Actividad.PROC_DEAL_NORMAL |
	 *            Actividad.PROC_DEAL_INTERBANCARIO |
	 *            Actividad.PROC_CANCELACION_DEAL.
	 * @param nombre
	 *            El nombre de la actividad.
	 * @param deal
	 *            La referencia al deal.
	 * @param det
	 *            La referencia a un detalle del deal (puede ser null).
	 * @param comentario
	 *            Informaci&oacute;n adicional.
	 * @return Actividad.
	 */
	protected Actividad wfCrearActividad(String proceso, String nombre,
			Deal deal, DealDetalle det, String comentario) {
		Actividad actividad = new Actividad(proceso, nombre, deal, det);
		actividad.setResultado(comentario);
		store(actividad);
		return actividad;
	}

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
	 * @return Actividad.
	 */
	public Actividad crearNotificacion(String proceso, String nombre,
			Deal deal, String comentario) {
		return wfCrearActividad(proceso, nombre, deal, null, comentario);
	}

	/**
	 * @see SicaServiceData#crearDealInterbancario(com.ixe.ods.sica.model.Deal,
	 *      boolean, java.util.Date, double, String, double,
	 *      com.ixe.ods.sica.model.Divisa, com.ixe.ods.sica.model.MesaCambio,
	 *      com.ixe.ods.seguridad.model.IUsuario, boolean).
	 */
	public Deal crearDealInterbancario(Deal deal, boolean compra,
			Date fechaLiquidacion, double tipoCambio,
			String claveFormaLiquidacion, double monto, Divisa divisa,
			MesaCambio mesaCambio, IUsuario usuario, boolean esComplementoSwap) {
		return crearDealInterbancario(deal, compra, fechaLiquidacion,
				tipoCambio, claveFormaLiquidacion, monto, divisa, mesaCambio,
				usuario, esComplementoSwap, false, 0, 0, false, 0, 0, null,null);
	}

	/**
	 * @see SicaServiceData#crearDealInterbancario(com.ixe.ods.sica.model.Deal,
	 *      boolean, java.util.Date, double, String, double,
	 *      com.ixe.ods.sica.model.Divisa, com.ixe.ods.sica.model.MesaCambio,
	 *      com.ixe.ods.seguridad.model.IUsuario, boolean, boolean, double,
	 *      double, boolean, double, double, String)
	 */
	public Deal crearDealInterbancario(Deal deal, boolean compra,
			Date fechaLiquidacion, double tipoCambio,
			String claveFormaLiquidacion, double monto, Divisa divisa,
			MesaCambio mesaCambio, IUsuario usuario, boolean esComplementoSwap,
			boolean esCobertura, double montoUsdParaComplCob,
			double tipoCambioCob, boolean esNeteo, double montoNeteo,
			double tipoCambioNeteo, String claveFormaLiqNeteo, String ticket) {
		if (esCobertura && esNeteo) {
			throw new SicaException(
					"El deal no puede ser de cobertura y de neteo a la vez.");
		}
		deal.setCompra(compra);
		deal.setFechaLiquidacion(fechaLiquidacion);
		if (Divisa.DOLAR.equals(mesaCambio.getDivisaReferencia().getIdDivisa())
				&& divisa.isDivide()) {
			tipoCambio = 1.0 / tipoCambio;
		}
		PrecioReferenciaActual pr = findPrecioReferenciaActual();
		// Se crea el detalle de la operacion:
		DealDetalle det = new DealDetalle(claveFormaLiquidacion, deal, divisa,
				usuario.getIdUsuario(), mesaCambio, monto, deal.isCompra(),
				tipoCambio, tipoCambio, DealPosicion.TIPO_DEAL_INTERBANCARIO,
				pr.getPreRef().getMidSpot(), pr.getPreRef().getPrecioSpot(),
				new Integer(pr.getIdPrecioReferencia()));
		FactorDivisaActual fda;
		if (det.getDivisa().isFrecuente()) {
			fda = findFactorDivisaActualFromTo(divisa.getIdDivisa(),
					Divisa.DOLAR);
		} else {
			fda = findFactorDivisaActualFromTo(Divisa.PESO, divisa
					.getIdDivisa());
		}
		det.setFactorDivisa(new Double(fda.getFacDiv().getFactor()));
		if (esNeteo) {
			deal.getDetalles().add(det);
			// Se crea el detalle contrario de la operacion:
			det = new DealDetalle(claveFormaLiqNeteo, deal, divisa, usuario
					.getIdUsuario(), mesaCambio, montoNeteo, !deal.isCompra(),
					tipoCambioNeteo, tipoCambioNeteo,
					DealPosicion.TIPO_DEAL_INTERBANCARIO, pr.getPreRef()
							.getMidSpot(), pr.getPreRef().getPrecioSpot(),
					new Integer(pr.getIdPrecioReferencia()));
			det.setFolioDetalle(DealHelper.getSiguienteFolioDetalle(deal));
			det.setFactorDivisa(new Double(fda.getFacDiv().getFactor()));
			deal.getDetalles().add(det);
			revisarBalanceo(deal, false);
		} else { // el problema del nullpointer es el orden en que asocia los
			// detalles,
			if (deal.isCompra()) { // si es compra primero asocia al deal el
				// detalle de entregamos (folio 1) y luego
				// el de recibimos que creo arriba
				if (deal.getContratoSica() != null) { // cuando hibernate los
					// crea, inserta primero
					// el de entregamos que
					// es en pesos por eso
					// tiene null en los
					// campos
					configurarDetalleCompraDealIntEInsertarEntregamos(deal,
							esCobertura ? montoUsdParaComplCob : det
									.getImporte(), esCobertura,
							esCobertura ? tipoCambioCob : 1);
				}
				deal.getDetalles().add(det); // aqui se asocia el de recibimos
				// que esta correcto
			} else { // si es venta primero asocia el detalle de entregamos que
				// tiene folio 0 y tiene los datos completos
				deal.getDetalles().add(det); // en la siguiente linea crea el de
				// recibimos (en pesos) que
				// tiene folio 1 y datos
				// incompletos
				insertarRecibimosDealInt(deal,
						esCobertura ? Constantes.TRANSFERENCIA : null,
						esCobertura ? montoUsdParaComplCob : det.getImporte(),
						esCobertura ? tipoCambioCob : 1.0, esCobertura);
			}
			revisarBalanceo(deal, true);
		}
		if (deal.getContratoSica() != null) {
			if (deal.getBroker() != null && deal.getCliente()!=null){
					
					LineaCambio linea = getLineaCambioServiceData().findLineaCambioParaCliente(deal.getCliente().getIdPersona());
								
					if(linea==null || !linea.getStatusLinea().equals(LineaCreditoConstantes.STATUS_ACTIVADA)){
										
						if(!deal.getTipoValor().equals(Constantes.CASH)){
							deal.setDetalles(new ArrayList());
							deal.setFechaLiquidacion(null);
							throw new SicaException(LineaCreditoMensajes.OPERACION_NO_CASH);
						}
						deal.setPagoAnticipado(false);	
						
					}else if(linea!=null && linea.getStatusLinea().equals(LineaCreditoConstantes.STATUS_ACTIVADA)){
						ParametroSica parametroSica = findParametro(ParametroSica.BROKER_DEAL_REINICIO_POSICION);
						ContratoSica contratoCliente = findContratoSicaByIdPersona(deal.getCliente().getIdPersona());
						System.out.println("Contrato SICA" + contratoCliente.getNoCuenta() + "  +++++  parametroSICa " + parametroSica.getValor());
						
						if (contratoCliente != null && !contratoCliente.getNoCuenta().trim().equals(parametroSica.getValor().trim())){
							boolean bAplicaMnemonicoLC = getLineaCambioServiceData().validarAplicablesTfPagAnt(ticket, det.getMnemonico(),
			                    det.getClaveFormaLiquidacion(), det.isRecibimos());	
							if (bAplicaMnemonicoLC && det.getMnemonico()!= null && ((deal.isCompra() && det.isRecibimos()) || (!deal.isCompra() && !det.isRecibimos()))){
								try{
									if(deal.getBroker().isPagoAnticipado()){
										getLineaCambioServiceData().consumirLineaCreditoPA(deal,det);
									}else{
										getLineaCambioServiceData().consumirLineaCredito(deal, det);
									}
								}catch(SicaException se){
									deal.setDetalles(new ArrayList());
									throw new SicaException(se.getMessage());
								} 
							}
						}
					}
			}
			revisarLineaOperacionYActualizarDeal(deal, usuario);
			
			
			
		} else {
			storeDeal(deal); // cuando hibernate guarda, lo hace en el orden de
			// asociacion al deal sin importar el folio,
			// crea el que se asocia primero.
		}
		// Para cuando Deals Interbancarios con Cobertura tengan un detalle de
		// RMXNBALNETEO o
		// EMXNBALNETEO y la Generacion de Movimientos Contables los pueda ver
		// en Deal Detalle
		// Status Log
		for (Iterator iterator = deal.getDetalles().iterator(); iterator
				.hasNext();) {
			DealDetalle detalle = (DealDetalle) iterator.next();
			if (Constantes.EMXNBALNETEO.equals(detalle.getMnemonico())
					|| Constantes.RMXNBALNETEO.equals(detalle.getMnemonico())) {
				detalle
						.setStatusDetalleDeal(DealDetalle.STATUS_DET_TOTALMENTE_PAG_LIQ);
				update(detalle);
			}
		}
		ContratoSica cs = findContratoSicaByIdPersona(deal.getBroker().getId()
				.getPersonaMoral().getIdPersona());
		EmpleadoIxe promotor = findPromotorByContratoSica(cs.getNoCuenta());
		deal.setPromotor(promotor);
		Broker contraparte = deal.getBroker();
		// Verifica, si el deal es sujeto a pago anticipado por default de
		// acuerdo a las propiedades
		// del broker.
		if (contraparte != null
				&& Broker.INSTITUCION_FINANCIERA.equals(contraparte
						.getTipoBroker())) {
			
			if (deal.getIdDeal() > 0) {
				update(deal);
			}
		}
		return deal;
	}

	/**
	 * Permite saber si un Deal est&aacute; Balanceado.
	 * 
	 * @param deal
	 *            El Deal a Revisar.
	 * @param balNeteo
	 *            Si se desea agregar el mnem&oacute;nico de BALNETEO en caso de
	 *            ser necesario.
	 */
	private void revisarBalanceo(Deal deal, boolean balNeteo) {
		if (deal.getBalance() != 0) {
			DealDetalle det = new DealDetalle();
			if (deal.getTotalEntregamos() > deal.getTotalRecibimos()) {
				det.setRecibimos(true);
				if (balNeteo) {
					det.setMnemonico(Constantes.RMXNBALNETEO);
					// Para cuando Deals Interbancarios con Cobertura tengan un
					// detalle de
					// RMXNBALNETEO o EMXNBALNETEO y la Generacion de
					// Movimientos Contables los
					// pueda ver en Deal Detalle Status Log
					det
							.setStatusDetalleDeal(DealDetalle.STATUS_DET_PROCESO_CAPTURA);
				}
			} else {
				det.setRecibimos(false);
				if (balNeteo) {
					det.setMnemonico(Constantes.EMXNBALNETEO);
					// Para cuando Deals Interbancarios con Cobertura tengan un
					// detalle de
					// RMXNBALNETEO o EMXNBALNETEO y la Generacion de
					// Movimientos Contables los
					// pueda ver en Deal Detalle Status Log
					det
							.setStatusDetalleDeal(DealDetalle.STATUS_DET_PROCESO_CAPTURA);
				}
			}
			det.setFactorDivisa(null);
			det.setDeal(deal);
			det.setMesaCambio(deal.getCanalMesa().getMesaCambio());
			det.setIdUsuario(deal.getUsuario().getIdUsuario());
			det.setFolioDetalle(DealHelper.getSiguienteFolioDetalle(deal));
			det.setTipoDeal(DealDetalle.TIPO_DEAL_INTERBANCARIO);
			det.setMonto(Math.abs(deal.getBalance()));
			det.setDivisa((Divisa) find(Divisa.class, deal.getCanalMesa()
					.getMesaCambio().getDivisaReferencia().getIdDivisa()));
			det.setTipoCambio(1);
			det.setTipoCambioMesa(1);
			deal.getDetalles().add(det);
		}
	}

	/**
	 * Configura e inserta el Deal a la plantilla.
	 * 
	 * @param deal
	 *            Deal a insertar
	 * @param importe
	 *            Importe de la operaci&oacute;n
	 * @param cobertura
	 *            Cobertura de la operaci&oacute;n
	 * @param tipoCambio
	 *            Tipo de cambio de la operaci&oacute;n
	 */
	private void configurarDetalleCompraDealIntEInsertarEntregamos(Deal deal,
			double importe, boolean cobertura, double tipoCambio) {
		// Obtengo la plantilla para el detalle del complemento (entregamos):
		insertarEntregamosDealIntConPlantilla(deal, importe, null, cobertura,
				tipoCambio);
	}

	/**
	 * Inserta un Deal Interbancario para la operaci&oacute;n compra.
	 * 
	 * @param deal
	 *            Deal a insertar.
	 * @param claveFormaLiquidacion
	 *            Clave de la forma de Liquidacii&oacute; del Deal.
	 * @param importe
	 *            Importe de la operaci&acute;n.
	 * @param tipoCambio
	 *            Tipo de cambio de la operaci&oacute;n.
	 * @param cobertura
	 *            Cobertura de la operaci&oacute;n
	 */
	private void insertarRecibimosDealInt(Deal deal,
			String claveFormaLiquidacion, double importe, double tipoCambio,
			boolean cobertura) {
		Divisa div = cobertura ? findDivisa(Divisa.DOLAR) : deal.getCanalMesa()
				.getMesaCambio().getDivisaReferencia();
		DealDetalle detCtpt = new DealDetalle(deal, true,
				claveFormaLiquidacion, div, importe, tipoCambio, DealHelper
						.getSiguienteFolioDetalle(deal));
		if (cobertura && detCtpt.getFactorDivisa() == null) {
			detCtpt.setFactorDivisa(new Double(findFactorDivisa(
					findFactorDivisaActualFromTo(Divisa.DOLAR, Divisa.DOLAR)
							.getIdFactorDivisa()).getFacDiv().getFactor()));
		}
		// Si es Trader USD, se le asigna el idPrecioReferencia al detalle
		// insertado
		if (Divisa.DOLAR.equals(deal.getCanalMesa().getMesaCambio()
				.getDivisaReferencia().getIdDivisa())) {
			PrecioReferenciaActual pr = findPrecioReferenciaActual();
			detCtpt.setPrecioReferenciaMidSpot(pr.getPreRef().getMidSpot());
			detCtpt.setPrecioReferenciaSpot(pr.getPreRef().getPrecioSpot());
		}
		detCtpt.setTipoDeal(DealPosicion.TIPO_DEAL_INTERBANCARIO);
		detCtpt.setDeal(deal);
	}

	/**
	 * Revisa que el cliente pueda operar remesas cuando la operaci&oacute;n
	 * contiene alguna. Si es un deal de balanceo de un reverso, no se realiza
	 * validaci&oacute;n alguna.
	 * 
	 * @param ticket
	 *            El ticket de la sesi&oacute;n del usuario.
	 * @param deal
	 *            El deal a revisar.
	 * @param claveFormaLiquidacion
	 *            El producto a revisar.
	 * @param mnemonico
	 *            El mnem&oacute;nico de la operaci&oacute;n.
	 * @param recibimos
	 *            True para compra, False para venta.
	 */
	private void validarOperacionRemesas(String ticket, Deal deal,
			String claveFormaLiquidacion, String mnemonico, boolean recibimos) {
		if (deal.isDealBalanceo()) {
			return;
		}
		if (getDealService().getFormasPagoLiqService().isRemesa(ticket,
				claveFormaLiquidacion, mnemonico, recibimos)) {
			if (deal.getContratoSica() == null) {
				throw new SicaException(
						"No se pueden capturar remesas si no se conoce el cliente");
			}
		}
	}

	/**
	 * Inserta un deal de tipo de cambio especial
	 * 
	 * @param deal
	 * @param operacionTce
	 * @param ticket
	 * @param limitesRestriccionOperacion
	 * @param idCanal
	 *            identificador del canal que opera
	 * 
	 * @return Deal
	 */
	public Deal insertarDealTce(Deal deal, OperacionTceDto operacionTce,
			String ticket, LimitesRestriccionOperacion limRestOper,
			String idCanal) {
		// Si no hay numero de deal, se trata de una edicion, se cancelan todos
		// los detalles
		if (deal.getIdDeal() != 0) {
			DealDetalle currentDealDet = null;
			for (Iterator it = deal.getDetalles().iterator(); it.hasNext();) {
				currentDealDet = (DealDetalle) it.next();
				if (!currentDealDet.isCancelado()) {
					marcarDealDetalleCancelado(ticket, currentDealDet, false,
							false);
				}
			}
			update(deal);
		}

		// Se inserta el detalle pivote y el deal si es necesario
		insertarDetalleDivisa(deal, operacionTce.isCompra(), operacionTce
				.getFechaValor(), operacionTce.getDetalleOperacion(), ticket,
				limRestOper, idCanal);

		if (operacionTce.isArbitraje()) {
			debug("insertarDealTce - Es arbitraje");
			// Se inserta el detalle de arbitraje
			insertarDetalleDivisa(deal, operacionTce.isVenta(), operacionTce
					.getFechaValor(), operacionTce
					.getDetalleOperacionArbitraje(), ticket, limRestOper,
					idCanal);
		} else {
			// Se inserta el detalle de liquidacion
			insertarDetalleMxn(deal, operacionTce.isVenta(), operacionTce
					.getDetalleOperacion(), ticket);
		}
		debug("insertarDealTce - Deal insertado con exito, numero de deal generado = "
				+ deal.getIdDeal());

		return deal;
	}

	/**
	 * Actualiza las observaciones del Deal unicamente
	 * 
	 * @param deal
	 * @return
	 */
	public Deal actualizaObservacionesDeal(Deal deal) {
		if (deal.getIdDeal() != 0) {
			update(deal);
		}
		return deal;
	}

	/**
	 * Inserta un detalle de operacion para una divisa
	 * 
	 * @param deal
	 * @param isRecibimos
	 * @param fechaValor
	 * @param detOper
	 * @param ticket
	 * @param limRestOper
	 * @param idCanal
	 */
	private void insertarDetalleDivisa(Deal deal, boolean isRecibimos,
			String fechaValor, DetalleOperacionTceDto detOper, String ticket,
			LimitesRestriccionOperacion limRestOper, String idCanal) {
		try {
			debug("insertarDetalleDivisa - Iniciando insercion");

			RenglonPizarron renglonPizarron = getRenglonPizarronByDivisaProducto(
					detOper.getDivisa(), detOper.getProducto(), idCanal);
			if (renglonPizarron == null) {
				throw new SicaException(
						"No se encontro precio para la divisa y producto");
			}

			insertarDivisa(ticket, // ticket,
					deal, // deal,
					isRecibimos, // recibimos,
					fechaValor, // fechaValor,
					detOper.getProducto(), // claveFormaLiquidacion,
					detOper.getDivisa().getIdDivisa(), // idDivisa,
					detOper.getTcMesa(), // tipoCambioMesa,
					detOper.getMonto(), // monto,
					detOper.getTcCliente(), // tipoCambio,
					renglonPizarron.getPrecioReferenciaMidSpot(), // precioReferenciaMidSpot,
					renglonPizarron.getPrecioReferenciaSpot(), // precioReferenciaSpot,
					renglonPizarron.getFactorDivisa(), // factorDivisa,
					renglonPizarron.getIdSpread(), // idSpread,
					new Integer(renglonPizarron.getIdPrecioReferencia()), // idPrecioReferencia,
					new Integer(renglonPizarron.getIdFactorDivisa()), // idFactorDivisa,
					null, // mnemonico,
					true, // afectarPosicion,
					null, // plantilla,
					limRestOper // limRestOper
					, null);
			debug("insertarDetalleDivisa - Detalle divisa insertado: recibimos="
					+ isRecibimos
					+ ", fechaValor="
					+ fechaValor
					+ ", detOper="
					+ detOper);
		} catch (DataAccessException e) {
			logger.error(
					"Error al insertar el detalle de la operacion: recibimos="
							+ isRecibimos + ", fechaValor=" + fechaValor
							+ ", detalle" + detOper, e);
			throw new SicaException(
					"Error al insertar el datalle de la operacion");
		}
	}

	/**
	 * Obtiene un renglon pizarron con la informacion de precio para la divisa y
	 * producto especificados. Para las divisa frecuentes obtiene toda la
	 * informacion de renglon pizarron, para las no frecuentes y metales obtiene
	 * solo la informacion de factor divisa y precio
	 * 
	 * @param divisa
	 * @param producto
	 * @return
	 */
	private RenglonPizarron getRenglonPizarronByDivisaProducto(Divisa divisa,
			String producto, String idCanal) {
		if (divisa == null || StringUtils.isEmpty(producto)
				|| StringUtils.isEmpty(idCanal))
			return null;
		try {
			RenglonPizarron renglonPizarron = null;
			Canal canal = findCanal(idCanal);

			if (divisa.isFrecuente()) {
				renglonPizarron = getPizarronServiceData()
						.findRenglonPizarron(canal.getTipoPizarron(),
								divisa.getIdDivisa(), producto);
			} else if (divisa.isNoFrecuente() || divisa.isMetalAmonedado()) {
				PrecioReferenciaActual pra = findPrecioReferenciaActual();
				FactorDivisaActual fda = findFactorDivisaActualFromTo(
						Divisa.PESO, divisa.getIdDivisa());
				renglonPizarron = getRenglonPizarronConFactorDivPrecioRef(fda,
						pra);
			}

			return renglonPizarron;
		} catch (DataAccessException e) {
			logger
					.error(
							"Error al obtener factor divisa actual y/o precio referencia actual para la divisa="
									+ divisa.getIdDivisa()
									+ ", producto="
									+ producto, e);
			throw new SicaException("No se encontro precio para la divisa "
					+ divisa.getIdDivisa() + " y el producto " + producto);
		}
	}

	/**
	 * Retorna un renglon pizarron poblado solo con los valores de factor divisa
	 * actual y precio referencia actual
	 * 
	 * @param fda
	 * @param pra
	 * 
	 * @return RenglonPizarron
	 */
	private RenglonPizarron getRenglonPizarronConFactorDivPrecioRef(
			FactorDivisaActual fda, PrecioReferenciaActual pra) {
		if (fda == null || pra == null)
			return null;
		RenglonPizarron renglonPizarron = new RenglonPizarron();
		renglonPizarron
				.setPrecioReferenciaMidSpot(pra.getPreRef().getMidSpot());
		renglonPizarron
				.setPrecioReferenciaSpot(pra.getPreRef().getPrecioSpot());
		renglonPizarron
				.setFactorDivisa(new Double(fda.getFacDiv().getFactor()));
		renglonPizarron.setIdSpread(0);
		renglonPizarron.setIdPrecioReferencia(pra.getIdPrecioReferencia());
		renglonPizarron.setIdFactorDivisa(fda.getIdFactorDivisa());
		return renglonPizarron;
	}

	/**
	 * Inserta un detalle en pesos
	 * 
	 * @param deal
	 * @param isRecibimos
	 * @param detOper
	 * @param ticket
	 */
	private void insertarDetalleMxn(Deal deal, boolean isRecibimos,
			DetalleOperacionTceDto detOper, String ticket) {
		try {
			debug("insertarDetalleMxn - Iniciando insercion");
			double cantidad = Redondeador.redondear2Dec(detOper.getMonto()
					* detOper.getTcCliente());

			// Se inserta el detalle de liquidacion MXN
			insertarMxn(ticket, deal, isRecibimos, // recibimos,
					null, // mnemonico,
					cantidad // cantidad
			);
			debug("insertarDetalleMxn - detalle MXN insertado: recibimos="
					+ isRecibimos + ", ticket=" + ticket + ", cantidad="
					+ cantidad);
		} catch (DataAccessException e) {
			logger.error("Error al insertar detalle MXN", e);
			throw new SicaException("Error al insertar detalle en pesos");
		}
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#insertarDivisa(String ticket,
	 *      Deal deal, boolean recibimos, String fechaValor, String
	 *      claveFormaLiquidacion, String idDivisa, double tipoCambioMesa,
	 *      double monto, double tipoCambio, Double precioReferenciaMidSpot,
	 *      Double precioReferenciaSpot, Double factorDivisa, int idSpread, int
	 *      idPrecioReferencia, int idFactorDivisa, String mnemonico, boolean
	 *      afectarPosicion, IPlantilla plantilla, LimitesRestriccionOperacion
	 *      limRestOper, OperacionDeal operacion)
	 */
	public DealDetalle insertarDivisa(String ticket, Deal deal,
			boolean recibimos, String fechaValor, String claveFormaLiquidacion,
			String idDivisa, double tipoCambioMesa, double monto,
			double tipoCambio, Double precioReferenciaMidSpot,
			Double precioReferenciaSpot, Double factorDivisa, int idSpread,
			Integer idPrecioReferencia, Integer idFactorDivisa,
			String mnemonico, boolean afectarPosicion, IPlantilla plantilla,
			LimitesRestriccionOperacion limRestOper, TipoOperacionDealDto operacion) {
		// Validamos la operaci&oacute;n con remesas:
		validarOperacionRemesas(ticket, deal, claveFormaLiquidacion, mnemonico,
				recibimos);
		PizarronServiceData psd = getPizarronServiceData();
		ValorFuturoService vfs = getDealService().getValorFuturoService();
		vfs.validarSectorEconomicoPorFechaValor(fechaValor, deal,
				getPizarronServiceData().isValorFuturoHabilitado());
		Divisa div = (Divisa) find(Divisa.class, idDivisa);

		boolean montoExcedido = montoMaximoExcedido(deal.getCanalMesa()
				.getMesaCambio().getIdMesaCambio(), div, monto);
		Date[] fechasValor = new Date[] { psd.getFechaOperacion(),
				psd.getFechaTOM(), psd.getFechaSPOT(), psd.getFecha72HR(),
				psd.getFechaVFUT() };
		
		DealDetalle det = crearDealDetalle(deal, recibimos, fechaValor,
				claveFormaLiquidacion, div, tipoCambioMesa, monto, tipoCambio,
				precioReferenciaMidSpot, precioReferenciaSpot, factorDivisa,
				idSpread, idPrecioReferencia, idFactorDivisa, mnemonico,
				fechasValor, montoExcedido, findEstadoSistemaActual(),
				limRestOper);
		
		det.setPlantilla(plantilla);
		// Marca el detalle para que no afecte la posici&oacute;n al momento de
		// storeDeal():
		if (!afectarPosicion) {
			det.setEvento(Deal.EV_NEGACION,
					DealDetalle.EV_IND_GRAL_AFECTADA_POSICION);
		}
		// Se establece el tiempo que tiene el promotor para capturar el
		// contrato sica.
		if (deal.getFechaLimiteCapturaContrato() == null) {
			deal.setFechaLimiteCapturaContrato(getDealService()
					.getFechaLimiteCapturaContrato());
		}
		// Si solo hay un mnemonico posible, se le asigna:
		List mnemonicosPosibles = getDealService().getFormasPagoLiqService()
				.getMnemonicosPosiblesParaDealDetalle(ticket, det);
		if (mnemonicosPosibles != null && mnemonicosPosibles.size() == 1) {
			det.setMnemonico((String) mnemonicosPosibles.get(0));
		}

		
		// a.Se movio este limite para pasar primero por limite normativo
		if (div.isFrecuente() && factorDivisa != null && deal.getContratoSica() != null) {
			// Si la Divisa es Frecuente y existe Factor Divisa se valida el
			// Cliente por Detalle
			// con Monto Mayor a $ 3,000.00 USD

			
			List cuenta = findFechaAperturaByContratoSica(deal.getContratoSica());
			CuentaContrato cc = (CuentaContrato) cuenta.get(0);
			Date fecha = (Date) cc.getFechaApertura();
			
			super.logger.info("******************* VALOR DE FECHA_VALIDA_DOCUMENTACION --> :  " + getFechaValidaDocumentacion());
			super.logger.info("******************* FECHA APERTURA DEL CONTRATO--> :  " + " >> "+ fecha);

			if (!deal.isInterbancario() && fecha.before(getFechaValidaDocumentacion()) )
			{
				super.logger.info("******************* Si la fecha de apertura es anterior al parametro definido va al MOC a validar Doc.  ");
				try {
					super.logger.info("******************* entra a validar documentacion al MOC  ");
					
					validarClientePorMontoMayorATresMilUSD(deal, monto
							* factorDivisa.doubleValue());
				} catch (Exception e) {
					det.getDeal().getDetalles().remove(det);
					det.setDeal(null);
					throw new SicaException(e.getMessage());
				}

			}
		}// fin a.
		// Si la Divisa es No Frecuente y No es MXN, se toma el Precio de
		// Referencia MidSpot y se
		// valida el Cliente por Detalle con Monto Mayor a $ 3,000.00 USD
		else if (!deal.isInterbancario() && deal.getContratoSica() != null) {
			
			List cuenta = findFechaAperturaByContratoSica(deal.getContratoSica());
			CuentaContrato cc = (CuentaContrato) cuenta.get(0);
			Date fecha = (Date) cc.getFechaApertura();
			
			if (!div.isFrecuente() && !Divisa.PESO.equals(div.getIdDivisa()) && fecha.before(getFechaValidaDocumentacion())) {
				double precioRefActual = findPrecioReferenciaActual()
						.getPreRef().getMidSpot().doubleValue();
				
				super.logger.info("******************* VALOR DE FECHA_VALIDA_DOCUMENTACION --> :  " + getFechaValidaDocumentacion());
				super.logger.info("******************* FECHA APERTURA DEL CONTRATO--> :  " +  " >> "+ fecha);
				super.logger.info("******************* Si la fecha de apertura es anterior al parametro definido va al MOC a validar Doc.  ");

				try {
					super.logger.info("******************* entra a validar documentacion al MOC  ");
					validarClientePorMontoMayorATresMilUSD(deal, monto
							/ precioRefActual);
				} catch (Exception e) {
					det.getDeal().getDetalles().remove(det);
					det.setDeal(null);
					throw new SicaException(e.getMessage());
				}

			}
		}
		try{
			
			boolean bMnemonicoAplicaLC = getLineaCambioServiceData().validarAplicablesTfPagAnt(ticket, det.getMnemonico(),
                    det.getClaveFormaLiquidacion(), det.isRecibimos());
			
			if(bMnemonicoAplicaLC && deal.getCliente()!=null && det.getMnemonico()!= null){
				if(operacion != null){
					if( operacion.isGoma()|| operacion.isCambioProducto()){
						getLineaCambioServiceData().aplicarUsoLCDetalleGoma(det);
					}else if( operacion.isSplit()){
						getLineaCambioServiceData().aplicarUsoLCDetalleSplit(det);
					}else if(operacion.isLapiz()){
						getLineaCambioServiceData().aplicarCambioMontoDetalleLC(operacion.getDealDetalleOriginal(),det);
					}else if(deal.isPagoAnticipado() || deal.isTomaEnFirme() && !deal.isDealBalanceo()){
						getLineaCambioServiceData().consumirLineaCreditoPA(deal, det);
					}else if(!deal.isDealBalanceo()){
						getLineaCambioServiceData().consumirLineaCredito(deal, det);
					}
				}else{
					if((deal.isPagoAnticipado() || deal.isTomaEnFirme()) && !deal.isDealBalanceo()){
						getLineaCambioServiceData().consumirLineaCreditoPA(deal, det);
					}else if(!deal.isDealBalanceo()){
						getLineaCambioServiceData().consumirLineaCredito(deal, det);
					}
				}
			}else if(det.getMnemonico() == null && operacion != null && operacion.isLapiz()){
				getLineaCambioServiceData().liberarLineaCreditoDealDetalle(ticket, operacion.getDealDetalleOriginal());
			} 
		}catch(SicaException se){
			det.getDeal().getDetalles().remove(det);
			det.getDeal().setTipoValor(null);
			det.setDeal(null);
			se.printStackTrace();
			throw new SicaException(se.getMessage());
		}
		storeDeal(deal);
		// Si es una remesa, creamos la actividad de Liquidacion de remesas:
		if (getDealService().getFormasPagoLiqService().isRemesa(ticket,
				det.getClaveFormaLiquidacion(), det.getMnemonico(),
				det.isRecibimos())) {
			store(new Actividad(Actividad.PROC_LIQ_REMESAS,
					Actividad.ACT_TES_REMESA, deal, det));
		}
		
		validarInformacionRegulatoria(deal, div, det, Deal.STATUS_DEAL_PROCESO_CAPTURA);
		
		return det;
	}

	/**
	 * @see SicaServiceData#insertarMxn(String, com.ixe.ods.sica.model.Deal,
	 *      boolean, String, double)
	 */
	public DealDetalle insertarMxn(String ticket, Deal deal, boolean recibimos,
			String mnemonico, double cantidad) {
		if (cantidad < 0.01) {
			throw new SicaValidationException(
					"La cantidad no puede ser menor a 0.01.");
		}
		// Si es BALNETEO, reviso que no haya otro registrado en el deal, que no
		// este cancelado:
		if (Constantes.EMXNBALNETEO.equals(mnemonico)
				|| Constantes.RMXNBALNETEO.equals(mnemonico)) {
			for (Iterator it = deal.getDetalles().iterator(); it.hasNext();) {
				DealDetalle det = (DealDetalle) it.next();
				if (!DealDetalle.STATUS_DET_CANCELADO.equals(det
						.getStatusDetalleDeal())
						&& (Constantes.EMXNBALNETEO.equals(det.getMnemonico()) || Constantes.RMXNBALNETEO
								.equals(det.getMnemonico()))) {
					throw new SicaValidationException(
							"S\u00f3lo se permite un detalle de "
									+ "Balanceo de Neteo por deal.");
				}
			}
		}
		MesaCambio mesaCambio = findMesaCambio(deal.getCanalMesa()
				.getMesaCambio().getIdMesaCambio());
		Divisa div = mesaCambio.getDivisaReferencia();
		DealDetalle det = new DealDetalle(deal, recibimos, null, div, cantidad,
				1.0, DealHelper.getSiguienteFolioDetalle(deal));
//		getLineaCambioServiceData().aplicarUsoLCDetalleMxn(deal,det);
		
		
		
		if (mnemonico != null) {
			FormaPagoLiq fpl = getDealService().getFormaPagoLiq(ticket,
					mnemonico);
			if (det.aceptaFormaPagoLiq(fpl)) {
				det.setDeal(deal);
				det.setMnemonico(mnemonico);
				getDealService()
						.calcularComisionDealDetalle(ticket, det, false);
				storeDeal(det.getDeal());
				return det;
			} else {
				deal.getDetalles().remove(det);
				double montoMinimo = fpl.getMontoMinimo().doubleValue();
				double montoMaximo = fpl.getMontoMaximo().doubleValue();
				throw new SicaValidationException(
						"El detalle no fue insertado pues la forma de "
								+ "liquidaci\u00f3n debe ser m\u00faltiplo de "
								+ fpl.getMultiplo()
								+ ", mayor a "
								+ MONEY_FORMAT.format(montoMinimo)
								+ (montoMaximo >= 0.01 ? " y menor a "
										+ MONEY_FORMAT.format(montoMaximo) : ""));
			}
		} else {
			det.setDeal(deal);
			storeDeal(det.getDeal());
			return det;
		}
	}
	
	/**
	 * Valida si el detalle de deal tiene un monto de 1 millón de dolares o mas y 
	 * verifica si el cliente cuenta con la información regulatoria requerida 
	 */
	public void validarInformacionRegulatoria(Deal deal, Divisa div, DealDetalle det, String statusAnterior)
	{
		Integer idPersona = null;
		Persona persona = null;
		String noCuentaSica = null;
		String nombreCliente = null;
		List infoRegulatoria = null;
		double _1MDD = 1000000.0;
		double montoDolarizadoDetalle = 0.0;
		
		String emailFrom = "ixecambios@ixe.com.mx";
		String emailTo[] = null;
		String emailAsunto = "de detalle de deal con monto mayor que o igual 1 MDD.";
		String faltaInfoRegulatoria = "";
		String clienteNoPerfilado = "";
		StringBuffer emailMensaje = null;
		ParametroSica param = null;
		boolean enviarEmail = true, clienteSinInfoRegulatoria = false; 
		
		try
		{
			if(!Deal.STATUS_DEAL_CANCELADO.equals(statusAnterior) && !Divisa.PESO.equals(div.getIdDivisa()) && (div.isFrecuente() || div.isNoFrecuente()))
			{
				param = findParametro(ParametroSica.EMAILS_REPORTES_REGULATORIOS);
				if(param == null || StringUtils.isEmpty(param.getValor()))
					enviarEmail = false;
				else
					emailTo = param.getValor().split(",");
				
				if(div.isFrecuente())
					montoDolarizadoDetalle = det.getMontoUSD();
				else
					montoDolarizadoDetalle = (det.getMonto() * det.getTipoCambio()) / det.getPrecioReferenciaMidSpot().doubleValue();
				
				if(montoDolarizadoDetalle >= _1MDD)
				{
					if(deal != null && !deal.isInterbancario())
					{
						if(deal.getCliente() != null && deal.getCliente().getIdPersona() != null &&
						   deal.getCliente().getTipoPersona() != null && "PM".equals(deal.getCliente().getTipoPersona()))
						{
							idPersona = deal.getCliente().getIdPersona();
							nombreCliente = deal.getCliente().getNombreCorto();
						}
						else if(deal.getContratoSica() != null && deal.getContratoSica().getNoCuenta() != null)
						{
							noCuentaSica = deal.getContratoSica().getNoCuenta();
							if(StringUtils.isNotEmpty(noCuentaSica))
								persona = findPersonaByContratoSica(noCuentaSica);
							if(persona != null && "PM".equals(persona.getTipoPersona()))
							{
								idPersona = persona.getIdPersona();
								nombreCliente = persona.getNombreCorto();
							}
						}

						if(idPersona != null)
						{
							infoRegulatoria = findRegulatorioDatosPM(idPersona);
							emailMensaje = new StringBuffer();
							
							if(infoRegulatoria == null || infoRegulatoria.size() == 0)
							{
								clienteSinInfoRegulatoria = true;
								emailMensaje.append("El sistema SICA detect\u00f3 un detalle deal con monto mayor que o igual a " +
													"un mill\u00f3n de d\u00f3lares americanos y el cliente no tiene " +
													"la informaci\u00f3n regulatoria requerida por BANXICO."); 
							}
							else
							{
								RegulatorioDatosPM pm = (RegulatorioDatosPM)infoRegulatoria.get(0);
								if(pm != null && (StringUtils.isEmpty(pm.getPerfilMdd()) || "N".equals(pm.getPerfilMdd())))
								{
									clienteSinInfoRegulatoria = true;
									emailMensaje.append("El sistema SICA detect\u00f3 que el cliente no est\u00E1 perfilado para operar " +
														"un monto mayor que o igual a un mill\u00f3n de dolares americanos.");
								}
							}
								
							if(clienteSinInfoRegulatoria)
							{
								GeneralMailSender gms = (GeneralMailSender) _appContext.getBean("generalMailSender");
								
								if(Deal.STATUS_DEAL_CANCELADO.equals(det.getStatusDetalleDeal()) || Deal.REVERSADO == det.getReversado())
									emailAsunto = "Cancelaci\u00f3n " + emailAsunto; 
								else
									emailAsunto = "Alta " + emailAsunto;
								
								emailMensaje.append("\n\nCliente: " + nombreCliente + "\n\n");
								emailMensaje.append("No. de deal: " + deal.getIdDeal() + "\t No. de detalle: " + det.getFolioDetalle() + "\t\n");
								emailMensaje.append("Producto: " + det.getClaveFormaLiquidacion() + "\t Divisa: " + div.getIdDivisa() +
										            "\t Monto: " + MONEY_FORMAT.format(det.getMonto()) + 
										            "\t Monto Dolarizado: " + MONEY_FORMAT.format(montoDolarizadoDetalle));
								emailMensaje.append("\n\nEste mensaje es generado por el sistema SICA, no responda este email.");	 
								if(enviarEmail && emailTo != null && emailTo.length > 0)
									gms.enviarMail(emailFrom, emailTo, null, emailAsunto, emailMensaje.toString());
							}	
						}
					}
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * @see SicaServiceData#disminuirDeSwapDeal(com.ixe.ods.sica.model.Deal).
	 */
	public void disminuirDeSwapDeal(Deal deal) {
		if (deal.isInterbancario()) {
			// Se revisa el swap si existe:
			if (deal.isInicioSwap()) {
				Swap swap = deal.getSwap();
				swap.setStatus(Swap.STATUS_CANCELADO);
				update(swap);
			} else if (deal.isContraparteSwap()
					&& Swap.STATUS_COMPLETAMENTE_ASIGNADO.equals(deal.getSwap()
							.getStatus())) {
				// Se debe modificar el monto asignado del swap:
				DealDetalle det = (DealDetalle) (deal.isCompra() ? deal
						.getRecibimos() : deal.getEntregamos()).get(0);
				Swap swap = deal.getSwap();
				swap.setMontoAsignado(swap.getMontoAsignado() - det.getMonto());
				if (swap.getMontoAsignado() < 0.01) {
					swap.setStatus(Swap.STATUS_INICIO);
				} else {
					swap.setStatus(Swap.STATUS_PARCIALMENTE_ASIGNADO);
				}
				update(swap);
			}
		}
	}

	/**
	 * Regresa una lista de objetos Actividad cuyo n&uacute;mero de deal y
	 * nombre de proceso sean los especificados, y su status sea 'Pendiente'.
	 * 
	 * @param proceso
	 *            El nombre del proeso a buscar.
	 * @param idDeal
	 *            El n&uacute;mero de deal a buscar.
	 * @return List.
	 */
	private List findActividadesPendientesProcesoDeal(String proceso, int idDeal) {
		return getHibernateTemplate().find(
				"FROM Actividad as a WHERE a.deal.idDeal = ? AND "
						+ "a.proceso = ? AND a.status = ?",
				new Object[] { new Integer(idDeal), proceso,
						Actividad.STATUS_PENDIENTE });
	}

	/**
	 * Regresa un lista de objetos ParametroSica ordenados
	 * alfab&eacute;ticamente.
	 * 
	 * @return List.
	 */
	public List findAllParametrosSica() {
		List parametroslist = getHibernateTemplate().find(
				"FROM ParametroSica AS a ORDER BY a.idParametro");
		parametrosCacheList = new ArrayList();
		logger.debug("----------------- refrescando cache de parametros");
		parametrosCacheList.addAll(parametroslist);
		return parametroslist;
	}	
	
	/**
	 * Find parametros by prefix.
	 *
	 * @param prefix the prefix
	 * @return the map
	 */
	public Map findParametrosByPrefix1(final String prefix) {
		Map params = new HashMap();
    	List lista = getHibernateTemplate().executeFind(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException {
            	Criteria c = session.createCriteria(ParametroSica.class);
            	c.add(Expression.like("idParametro", prefix, MatchMode.END));
                return c.list();
            }
        });
    	if (lista != null && !lista.isEmpty()) {
    		logger.debug("Numero de parametros encontrados: " + lista.size());
    		for (int i = 0; i < lista.size(); i++) {
				ParametroSica param = (ParametroSica) lista.get(i);
				params.put(param.getIdParametro(), param);
			}
    	}
    	
    	return params;
    }
	
	/**
	 * Find parametros by prefix.
	 *
	 * @param prefix the prefix
	 * @return the map
	 */
	public Map findParametrosByPrefix(final String prefix) {
		Map param = new HashMap();
		if (parametrosCacheList == null || parametrosCacheList.isEmpty()) {
			findAllParametrosSica();
		}
		if (!parametrosCacheList.isEmpty()) {
			log.debug("Total de parametros en cache: " + parametrosCacheList.size());
			for (int i = 0; i < parametrosCacheList.size(); i++) {
				ParametroSica tmp = (ParametroSica) parametrosCacheList.get(i);
				String idParametro = tmp.getIdParametro();
				if (idParametro.startsWith(prefix)) {
					param.put(idParametro, tmp);
				}
			}
		}
		
		return param;
    }

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findAllBrokers().
	 */
	public List findAllBrokers() {
		List brokers = getHibernateTemplate()
				.find(
						"FROM Broker AS b LEFT JOIN FETCH "
								+ "b.id.personaMoral p LEFT JOIN FETCH p.sectorEconomico ORDER BY "
								+ "b.id.personaMoral.razonSocial");
		// No quitar este ciclo pues garantiza que la relacion con PersonaMoral
		// se inicialice
		// sin hacer un query:
		for (Iterator it = brokers.iterator(); it.hasNext();) {
			Broker b = (Broker) it.next();
			b.getId().getPersonaMoral().getRazonSocial();
		}
		return brokers;
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findAllBrokersInstFin().
	 */
	public List findAllBrokersInstFin() {
		return getHibernateTemplate().find(
				"FROM Broker AS b WHERE b.tipoBroker = ?",
				Broker.INSTITUCION_FINANCIERA);
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findAllCanales().
	 */
	public List findAllCanales() {
		return getHibernateTemplate().find(
				"FROM Canal AS c INNER JOIN FETCH c.mesaCambio");
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findAllCanalesByPromocionMayoreo()
	 *      .
	 */
	public List findAllCanalesByPromocionMayoreo() {
		List canales = getHibernateTemplate().find(
				"FROM Canal AS c INNER JOIN FETCH "
						+ "c.mesaCambio order by c.nombre");
		List canalesSUC = new ArrayList();
		Canal canalPMY = null;
		for (Iterator it = canales.iterator(); it.hasNext();) {
			Canal c = (Canal) it.next();
			if ("PMY".equals(c.getIdCanal())) {
				canalPMY = c;
				it.remove();
			}
			if (c.getSucursal() != null) {
				canalesSUC.add(c);
				it.remove();
			}
		}
		if (canalPMY != null) {
			canales.add(0, canalPMY);
		}
		if (canalesSUC.size() > 0) {
			canales.addAll(canalesSUC);
		}
		return canales;
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findAllDealsBySectorAndDate(java.util.Date,
	 *      java.util.Date).
	 */
	public List findAllDealsBySectorAndDate(Date gc, Date fechaLiquidacion) {
		return getHibernateTemplate().findByNamedQuery(
				"findAllDealsBySectorAndDate",
				new Object[] { gc, fechaLiquidacion });
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findAllDealsBySectorAndDate(java.util.Date,
	 *      java.util.Date).
	 */
	public List findAllDealDetallesBySectorAndDate(Date gc,
			Date fechaLiquidacion) {
		return getHibernateTemplate().findByNamedQuery(
				"findAllDealDetallesBySectorAndDate",
				new Object[] { gc, fechaLiquidacion });
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findAllDivisasByOrdenAlfabetico()
	 *      .
	 */
	public List findAllDivisasByOrdenAlfabetico() {
		return getHibernateTemplate().findByNamedQuery(
				"findAllDivisasByOrdenAlfabetico");
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findAllEmpresasByOrdenAlfabetico()
	 *      .
	 */
	public List findAllEmpresasByOrdenAlfabetico() {
		return getHibernateTemplate().findByNamedQuery(
				"findAllEmpresasByOrdenAlfabetico");
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findContratosByCuenta(String).
	 */
	public List findContratosByCuenta(String NO_CUENTA) {
		return getHibernateTemplate().findByNamedQuery("findContratosByCuenta",
				NO_CUENTA);
	}
	
	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findContratosByCuenta(String).
	 */
	public Contrato findContratoByCorto(Integer idContrato) {
		List corto = getHibernateTemplate().find(
				"FROM Contrato AS c WHERE c.idContrato = ?",
				new Object[] { idContrato });
		return corto.isEmpty() ? null : (Contrato) corto.get(0);

	}

    /**
     * @see com.ixe.ods.sica.sdo.SicaServiceData#findContratoByNoCuenta(String).
     */
    public Contrato findContratoByNoCuenta(String noCuenta) {
        List contrato = getHibernateTemplate().find(
                "FROM Contrato AS c WHERE c.noCuenta = ?",
                new Object[] { noCuenta });
        return contrato.isEmpty() ? null : (Contrato) contrato.get(0);
    }

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findAllLineaCreditoCierre()
	 */
	public List findAllLineaCreditoCierre() {
		return getHibernateTemplate().find(
				"SELECT lc FROM LineaCambio AS lc LEFT JOIN FETCH "
						+ "lc.corporativo");
	}

	/**
	 * @see SicaServiceData#findAllPaises()
	 */
	public List findAllPaises() {
		return getHibernateTemplate().find(
				"FROM com.ixe.sica.model.TesPais AS p ORDER BY "
						+ "p.nombreLargo");
	}

	/**
	 * @see SicaServiceData#findAllEmpresas()
	 */
	public List findAllEmpresas() {// Hamue 2/8/12
		return getHibernateTemplate().find(
				"FROM com.ixe.sica.model.TesEmpresa AS p ORDER BY "
						+ "p.nombreLargo");
	}

	/**
	 * @see SicaServiceData#findBupPaisXId(String idPais )
	 */
	public Pais findBupPaisXId(String idPais) {
		List paises = getHibernateTemplate()
				.find(
						"FROM com.ixe.ods.bup.model.Pais AS p "
								+ "WHERE p.idPais = ? ", idPais);
		return paises.size() > 0 ? (Pais) paises.get(0) : null;
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findAllPromotoresSICA(String)
	 */
	public List findAllPromotoresSICA(String nombreSistema) {
		final ISistema sistema = (ISistema) getHibernateTemplate().get(
				Sistema.class, nombreSistema);
		List nodos = getHibernateTemplate().executeFind(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException {
						String sql = "SELECT {n.*} FROM SEGU_NODO_JERARQUIA n WHERE "
								+ "ID_JERARQUIA = :idJerarquia CONNECT BY PRIOR "
								+ "ID_NODO_JERARQUIA = ID_JEFE START WITH ID_JEFE IS NULL";
						return session.createSQLQuery(sql, "n",
								NodoJerarquia.class).setInteger("idJerarquia",
								sistema.getIdJerarquia().intValue()).list();
					}
				});
		List list = new ArrayList();
		for (int i = 0; i < nodos.size(); i++) {
			NodoJerarquia node = (NodoJerarquia) nodos.get(i);
			list.add(node.getIdPersona());
		}
		final List promotores = new ArrayList(list);
		return getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException {
				String sql = "FROM EmpleadoIxe e where e.idPersona in (:listaEmpleados) "
						+ "ORDER BY e.nombre, e.paterno, e.materno";
				return session.createQuery(sql).setParameterList(
						"listaEmpleados", promotores, new IntegerType()).list();
			}
		});
	}
	
	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findAllMotivosBloqueo
	 */
	public List findAllMotivosBloqueo (){
		return getHibernateTemplate().find("FROM TipoBloqueo as tb WHERE tb.idBloqueo != 0 ORDER BY tb.descripcion");		
	}
				

	/**
	 * @see SicaServiceData#findPromotorSicaByClave(String)
	 */
	public EmpleadoIxe findPromotorSicaByClave(final String claveUsuario) {
		final ISistema sistema = (ISistema) getHibernateTemplate().get(
				Sistema.class, "SICA");
		List nodos = getHibernateTemplate().executeFind(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException {
						String sql = "SELECT {n.*} FROM SEGU_NODO_JERARQUIA n WHERE "
								+ "ID_JERARQUIA = :idJerarquia CONNECT BY PRIOR "
								+ "ID_NODO_JERARQUIA = ID_JEFE START WITH ID_JEFE IS NULL";
						return session.createSQLQuery(sql, "n",
								NodoJerarquia.class).setInteger("idJerarquia",
								sistema.getIdJerarquia().intValue()).list();
					}
				});
		List list = new ArrayList();
		for (int i = 0; i < nodos.size(); i++) {
			NodoJerarquia node = (NodoJerarquia) nodos.get(i);
			list.add(node.getIdPersona());
		}
		final List promotores = new ArrayList(list);
		List result = getHibernateTemplate().executeFind(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException {
						String sql = "FROM EmpleadoIxe e WHERE e.idPersona in (:listaEmpleados) "
								+ "AND e.claveUsuario = (:claveUsuario)"
								+ "ORDER BY e.nombre, e.paterno, e.materno";
						return session.createQuery(sql)
								.setParameterList("listaEmpleados", promotores,
										new IntegerType()).setParameter(
										"claveUsuario", claveUsuario).list();
					}
				});
		if (result != null && !result.isEmpty()) {
			return (EmpleadoIxe) result.get(0);
		} else {
			return null;
		}
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findAllSectoresUltimaFechaValor()
	 *      .
	 */
	public List findAllSectoresUltimaFechaValor() {
		List params = getHibernateTemplate().find(
				"FROM ParametroSica AS p WHERE p.idParametro " + "like ?",
				ParametroSica.PREFIJO_SECTOR_72 + "%");
		final List idsSectores = new ArrayList();
		for (Iterator it = params.iterator(); it.hasNext();) {
			ParametroSica parametro = (ParametroSica) it.next();
			idsSectores.add(Integer.valueOf(parametro.getValor()));
		}
		return getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				Criteria c = session.createCriteria(SectorEconomico.class);
				c.add(Expression.in("idSector", idsSectores));
				return c.list();
			}
		});
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findAllSucursales().
	 */
	public List findAllSucursales() {
		return getHibernateTemplate().find(
				"FROM Sucursal AS s ORDER BY s.nombre");
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findAllTiposTraspasoProducto().
	 */
	public List findAllTiposTraspasoProducto() {
		return getHibernateTemplate().findByNamedQuery(
				"findAllTiposTraspasoProducto");
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findBeneficiariosByNoCuentaByIdRol(String,
	 *      String)
	 */
	public List findBeneficiariosByNoCuentaByIdRol(String noCuenta, String idRol) {
		List beneficiarioTmp = getHibernateTemplate()
				.find(
						"SELECT p FROM ContratoSica AS cs LEFT "
								+ "JOIN cs.roles AS pcr, com.ixe.ods.bup.model.Persona AS p WHERE "
								+ "pcr.id.persona.idPersona = p.idPersona AND cs.noCuenta = ? "
								+ "AND pcr.id.rol IN ('TIT', ?)",
						new Object[] { noCuenta, idRol });
		Collections.sort(beneficiarioTmp, new Comparator() {
			public int compare(Object o1, Object o2) {
				Persona e1 = (Persona) o1;
				Persona e2 = (Persona) o2;
				return e1.getNombreCompleto().compareTo(e2.getNombreCompleto());
			}
		});
		return beneficiarioTmp;
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findBeneficiarioById(java.lang.String)
	 * @param idBeneficiario
	 *            La clave del beneficiario
	 */
	public Persona findBeneficiarioById(String idBeneficiario) {
		List beneficiario = getHibernateTemplate().find(
				"FROM Persona AS p " + "WHERE p.idPersona = ?",
				new Object[] { idBeneficiario });
		return beneficiario.isEmpty() ? null : (Persona) beneficiario.get(0);
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findUsuarioById(Integer)
	 * @param idUsuario
	 *            El id del usuario.
	 */
	public IUsuario findUsuarioById(Integer idUsuario) {
		List usuario = getHibernateTemplate().find(
				"FROM Usuario AS u WHERE u.idUsuario = ?",
				new Object[] { idUsuario });
		return usuario.isEmpty() ? null : (IUsuario) usuario.get(0);
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findBrokerByIdPersona(Integer)
	 */
	public List findBrokerByIdPersona(Integer idBroker) {
		return getHibernateTemplate().findByNamedQuery("findBrokerByIdPersona",
				idBroker);
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findBrokersByRazonSocial(String)
	 */
	public List findBrokersByRazonSocial(String razonSocial) {
		return getHibernateTemplate().findByNamedQuery(
				"findBrokersByRazonSocial", razonSocial + "%");
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findCanalByFacultad(String)
	 */
	public Canal findCanalByFacultad(String nombreFacultad) {
		List list = getHibernateTemplate().findByNamedQuery(
				"findCanalByFacultad", nombreFacultad);
		return !list.isEmpty() ? (Canal) list.get(0) : null;
	}

	/**
	 * @see SicaServiceData#findCanalesOperadosHoy(Date desde, Date hoy) .
	 */
	public List findCanalesOperadosHoy(Date desde, Date hoy) {
		StringBuffer sb = new StringBuffer(
				"SELECT distinct d.canalMesa.canal FROM Deal AS d WHERE d.fechaCaptura BETWEEN ? AND ?");
		return getHibernateTemplate().find(sb.toString(),
				new Object[] { desde, hoy });
	}

	/**
	 * @see SicaServiceData#findCanalByIdSucursal(String).
	 */
	public Canal findCanalByIdSucursal(String idSucursalOriginal) {
		List canales = getHibernateTemplate()
				.find(
						"FROM Canal AS c INNER JOIN FETCH "
								+ "c.mesaCambio INNER JOIN FETCH c.sucursal WHERE c.sucursal.idOriginal = ?",
						idSucursalOriginal);
		return canales.isEmpty() ? null : (Canal) canales.get(0);
	}

	/**
	 * @see SicaServiceData#findCanalSucursales().
	 */
	public List findCanalSucursales() {
		return getHibernateTemplate().find(
				"FROM Canal AS c WHERE c.sucursal IS NOT NULL");
	}

	/**
	 * @see SicaServiceData#findContrato(String).
	 */
	public ContratoSica findContrato(String noCuenta) {
		List contratos = getHibernateTemplate().findByNamedQuery(
				"findContrato", noCuenta);
		return contratos.isEmpty() ? null : (ContratoSica) contratos.get(0);
	}

	/**
	 * @see SicaServiceData#findContratoSicaConSectorEconomico(String).
	 */
	public ContratoSica findContratoSicaConSectorEconomico(String noCuenta) {
		StringBuffer sb = new StringBuffer(
				"FROM ContratoSica AS cs LEFT JOIN FETCH cs.roles AS pcr LEFT JOIN FETCH ")
				.append(
						"pcr.id.persona AS p LEFT JOIN FETCH p.sectorEconomico WHERE cs.noCuenta= ? AND ")
				.append(
						"cs.statusOrigen = 'Active' AND cs.status.estatus = 'ACTIVA' AND ")
				.append("pcr.status.estatus = 'VIG' AND pcr.id.rol = 'TIT'");
		List contratos = getHibernateTemplate().find(sb.toString(), noCuenta);
		ContratoSica cs = contratos.isEmpty() ? null : (ContratoSica) contratos
				.get(0);
		if (cs != null) {
			for (Iterator it = cs.getRoles().iterator(); it.hasNext();) {
				PersonaCuentaRol pcr = (PersonaCuentaRol) it.next();
				pcr.getParticipacion();
				if (pcr.getId().getPersona().getSectorEconomico() != null) {
					pcr.getId().getPersona().getSectorEconomico()
							.getDescripcion();
				}
			}
		}
		return contratos.isEmpty() ? null : (ContratoSica) contratos.get(0);
	}

	/**
	 * @see SicaServiceData#findBancoInternacionalByNombreAndPais(String,
	 *      String)
	 */
	public List findBancoInternacionalByNombreAndPais(String nombreBanco,
			String pais) {
		return getHibernateTemplate().findByNamedQuery(
				"findBancoInternacionalByNombreAndPais",
				new Object[] { nombreBanco + "%", pais });
	}

	/**
	 * @see SicaServiceData#findContratoSicaByIdPersona(Integer).
	 */
	public ContratoSica findContratoSicaByIdPersona(final Integer idPersona) {
		StringBuffer sb = new StringBuffer(
				"FROM ContratoSica AS cs LEFT JOIN FETCH cs.roles AS pcr LEFT JOIN FETCH ")
				.append(
						"pcr.id.persona AS p LEFT JOIN FETCH p.sectorEconomico WHERE p.idPersona = ? AND ")
				.append(
						"cs.statusOrigen = 'Active' AND cs.status.estatus = 'ACTIVA' AND ")
				.append("pcr.status.estatus = 'VIG' AND pcr.id.rol = 'TIT'");
		List contratos = getHibernateTemplate().find(sb.toString(), idPersona);
		// Forzamos la inicializacion del sector economico:
		for (Iterator it = contratos.iterator(); it.hasNext();) {
			ContratoSica cs = (ContratoSica) it.next();
			for (Iterator it2 = cs.getRoles().iterator(); it2.hasNext();) {
				PersonaCuentaRol pcr = (PersonaCuentaRol) it2.next();
				if (pcr.getId().getPersona().getSectorEconomico() != null) {
					pcr.getId().getPersona().getSectorEconomico()
							.getDescripcion();
				}
			}
		}
		return (contratos.isEmpty() ? null : (ContratoSica) contratos.get(0));
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findIdsSectoresUltimaFechaValor()
	 *      .
	 */
	public String[] findIdsSectoresUltimaFechaValor() {
		List sectores = findAllSectoresUltimaFechaValor();
		String[] idsSectores = new String[sectores.size()];
		int i = 0;
		for (Iterator it = sectores.iterator(); it.hasNext(); i++) {
			SectorEconomico se = (SectorEconomico) it.next();
			idsSectores[i] = se.getIdSector().toString();
		}
		return idsSectores;
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#getFechaLimiteCapturaDeal().
	 */
	public Date getFechaSistema() {
		try {
			ParametroSica p = (ParametroSica) find(ParametroSica.class,
					ParametroSica.FECHA_SISTEMA);
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			return dateFormat.parse(p.getValor());
		} catch (ParseException e) {
			warn("No se pudo calcular la fecha actual. Se regresa new Date()",
					e);
		}
		return new Date();
	}
	
	/**
	 * obtiene fecha a partir del cual ya no se validara Documentacion en el MOC
	 */
	public Date getFechaValidaDocumentacion() {
		try {
			ParametroSica p = (ParametroSica) find(ParametroSica.class,
					ParametroSica.FECHA_VALIDA_DOCUMENTACION);
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			return dateFormat.parse(p.getValor());
		} catch (ParseException e) {
			warn("No se pudo calcular la fecha para validar documentacion. Se regresa new Date()",
					e);
		}
		return new Date();
	}
	
	/**
	 * obtiene la bandera para saber si valida posicion o no
	 */
	public String getBanderaValidaPosicionCierre() {
		ParametroSica p = (ParametroSica) find(ParametroSica.class,
				ParametroSica.BANDERA_VALIDA_POSICION_CIERRE);
		return p.getValor();

	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findContratosSica(String,
	 *      String, String, String, String)
	 */
	public List findContratosSica(String razonSocial, String nombre,
			String paterno, String materno, String noCuenta) {
		StringBuffer sb = new StringBuffer(
				"SELECT distinct cs, p FROM ContratoSica AS cs LEFT JOIN cs.roles AS pcr");
		List params = new ArrayList();
		if (StringUtils.isNotEmpty(razonSocial)) {
			sb
					.append(", com.ixe.ods.bup.model.PersonaMoral AS p WHERE pcr.id.persona.idPersona = p.idPersona AND p.razonSocial like ?");
			params.add(razonSocial + "%");
		} else {
			if (StringUtils.isNotEmpty(noCuenta)) {
				sb
						.append(
								", com.ixe.ods.bup.model.Persona AS p WHERE pcr.id.persona.idPersona = p.idPersona")
						.append(" AND cs.noCuenta = ?");
				params.add(noCuenta);
			} else {
				sb
						.append(", com.ixe.ods.bup.model.PersonaFisica AS p WHERE pcr.id.persona.idPersona = p.idPersona ");
				if (StringUtils.isNotEmpty(nombre)) {
					sb.append(" AND p.nombre like ?");
					params.add(nombre + "%");
				}
				if (StringUtils.isNotEmpty(paterno)) {
					sb.append(" AND p.paterno like ?");
					params.add(paterno + "%");
				}
				if (StringUtils.isNotEmpty(materno)) {
					sb.append(" AND p.materno like ?");
					params.add(materno + "%");
				}
			}
		}
		sb
				.append(" AND cs.statusOrigen = 'Active' AND cs.status.estatus = 'ACTIVA' AND pcr.status.estatus = 'VIG' AND pcr.id.rol = 'TIT' AND rownum < 51");
		return getHibernateTemplate().find(sb.toString(), params.toArray());
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findContratosSicaBrokers(String)
	 */
	public List findContratosSicaBrokers(String razonSocial) {
		List params = new ArrayList();
		StringBuffer sb = new StringBuffer(
				"SELECT distinct pm FROM ContratoSica AS cs, com.ixe.ods.bup.model.PersonaCuentaRol AS pcr, ");
		sb
				.append("com.ixe.ods.bup.model.PersonaMoral AS pm WHERE cs.noCuenta = pcr.id.cuenta.noCuenta AND pcr.id.persona.idPersona = pm.idPersona ");
		sb.append("AND pm.razonSocial like ?");
		params.add(razonSocial + "%");
		sb
				.append(" AND pm.idPersona IN (SELECT b.id.personaMoral.idPersona FROM Broker as b WHERE b.tipoBroker = ?) ");
		params.add(Broker.INSTITUCION_FINANCIERA);
		sb
				.append("AND cs.statusOrigen = 'Active' AND cs.status.estatus = 'ACTIVA' AND pcr.status.estatus = 'VIG' AND pcr.id.rol = 'TIT'");
		return getHibernateTemplate().find(sb.toString(), params.toArray());
	}

	/**
	 *@see com.ixe.ods.sica.sdo.SicaServiceData#findCorporativo(Integer)
	 */
	public PersonaMoral findCorporativo(Integer idPersona) {
		List personasMorales = getHibernateTemplate()
				.find(
						"FROM com.ixe.ods.bup.model.PersonaMoral AS p WHERE p.idPersona = ?",
						idPersona);
		if (personasMorales.size() > 0) {
			PersonaMoral personaMoral = (PersonaMoral) personasMorales.get(0);
			if (personaMoral.getEsGrupo() != null) {
				if (personaMoral.getEsGrupo().booleanValue()) {
					ContratoSica cs = findContratoSicaByIdPersona(personaMoral
							.getIdPersona());
					if (cs != null
							&& personaMoral.getIdPersona().equals(
									cs.getIdGrupoEmpresarial())) {
						return personaMoral;
					}
				} else if (!personaMoral.getEsGrupo().booleanValue()
						&& personaMoral.getGrupoEmpresarial() != null) {
					ContratoSica cs = findContratoSicaByIdPersona(personaMoral
							.getIdPersona());
					if (cs != null
							&& personaMoral.getGrupoEmpresarial()
									.getIdPersona().equals(
											cs.getIdGrupoEmpresarial())) {
						return personaMoral.getGrupoEmpresarial();
					}
				}
			}
		}
		return null;
	}

	/**
	 * @see SicaServiceData#findCorporativosByPersonaMoral(Integer).
	 */
	public String[] findCorporativosByPersonaMoral(Integer idPersona) {
		List corporativosObj = getHibernateTemplate()
				.find(
						"FROM com.ixe.ods.bup.model.PersonaMoral AS p WHERE p.grupoEmpresarial.idPersona = ?",
						idPersona);
		Persona p = (Persona) find(Persona.class, idPersona);
		corporativosObj.add(0, p);
		String[] corporativos = new String[corporativosObj.size()];
		int i = 0;
		for (Iterator it = corporativosObj.iterator(); it.hasNext(); i++) {
			Persona persona = (Persona) it.next();
			corporativos[i] = persona.getNombreCompleto();
		}
		return corporativos;
	}

	/**
	 * @see SicaServiceData#findSubsidiariosByPersonaMoral(Integer).
	 */
	public List findSubsidiariosByPersonaMoral(Integer idPersona) {
		List subsidiarios = new ArrayList();
		List subsidiariosTmp = getHibernateTemplate()
				.find(
						"FROM com.ixe.ods.bup.model.PersonaMoral AS pm WHERE "
								+ "pm.esGrupo = ? AND pm.grupoEmpresarial.idPersona = ?",
						new Object[] { "N", idPersona });
		for (Iterator it = subsidiariosTmp.iterator(); it.hasNext();) {
			PersonaMoral pm = (PersonaMoral) it.next();
			ContratoSica cs = findContratoSicaByIdPersona(pm.getIdPersona());
			if (cs != null) {
				if (idPersona.equals(cs.getIdGrupoEmpresarial())) {
					subsidiarios.add(pm);
				}
			}
		}
		return subsidiarios;
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findCuentasEje(Integer)
	 */
	public String[] findCuentasEje(Integer idPersona) {
		List cuentasEjeTmp = getHibernateTemplate()
				.find(
						"SELECT pcr.id.cuenta.noCuenta FROM "
								+ "PersonaCuentaRol AS pcr WHERE pcr.id.persona.idPersona = ? AND pcr.id.rol.idRol "
								+ "IN ('TIT', 'COT', 'TAH') AND (pcr.id.cuenta.statusOrigen = 'Active' OR "
								+ "pcr.id.cuenta.statusOrigen = 'Unfunded' OR pcr.id.cuenta.statusOrigen = 'Dormant') "
								+ "AND pcr.id.cuenta.status.estatus = 'ACTIVA' AND pcr.status.estatus = 'VIG' AND "
								+ "pcr.id.cuenta.tipoCuentaContrato.eje = 'S'",
						new Object[] { idPersona });
		String[] noCuentas = new String[] { "" };
		if (cuentasEjeTmp.size() > 0) {
			noCuentas = new String[cuentasEjeTmp.size()];
			int j = 0;
			for (Iterator it = cuentasEjeTmp.iterator(); it.hasNext(); j++) {
				noCuentas[j] = (String) it.next();
			}
		}
		return noCuentas;
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findCuentasEjeOrIdTipoOrigen(Integer)
	 */
	public boolean isClienteOrUsuario(Integer idPersona) {
		ParametroSica prodsCliente = (ParametroSica) find(ParametroSica.class,
				ParametroSica.PRODUCTOS_CLIENTE);

		List cuentasEjeTmp = getHibernateTemplate()
				.find(
						"SELECT pcr.id.cuenta.noCuenta FROM "
								+ "PersonaCuentaRol AS pcr WHERE pcr.id.persona.idPersona = ? AND pcr.id.rol.idRol "
								+ "IN ('TIT', 'COT') AND pcr.id.cuenta.status.estatus = 'ACTIVA' "
								+ "AND pcr.status.estatus = 'VIG' AND (pcr.id.cuenta.tipoCuentaContrato.eje = 'S' "
								+ "OR pcr.id.cuenta.tipoCuentaContrato.idTipoCuenta in ('"
								+ prodsCliente.getValor()
										.replaceAll(",", "','") + "'))",
						new Object[] { idPersona });
		boolean cliente = false;
		if (cuentasEjeTmp.size() > 0) {
			cliente = true;
		}
		return cliente;
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findPersonasByCuentaEje(String)
	 */
	public List findPersonasByCuentaEje(String cuentaIxe) {
		return getHibernateTemplate()
				.find(
						"SELECT pcr FROM PersonaCuentaRol AS pcr "
								+ "WHERE pcr.id.cuenta.noCuenta = ? AND pcr.id.rol.idRol IN ('TIT', 'COT', 'TAH') "
								+ "AND (pcr.id.cuenta.statusOrigen = 'Active' OR "
								+ "pcr.id.cuenta.statusOrigen = 'Unfunded' OR pcr.id.cuenta.statusOrigen = 'Dormant') "
								+ "AND pcr.id.cuenta.status.estatus = 'ACTIVA' And pcr.status.estatus = 'VIG' "
								+ "AND pcr.id.cuenta.tipoCuentaContrato.eje = 'S'",
						new Object[] { cuentaIxe });
	}
	
	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findIdPersonaCuentaAltamiraEspejeada(String)
	 */
	public String findIdPersonaCuentaAltamiraEspejeada(String noCuentaAltamira) {
		
		String idPersona = null;
		List pcrList = getHibernateTemplate()
				.find("SELECT pcr FROM PersonaCuentaRol AS pcr " 
						       + "WHERE pcr.id.cuenta.noCuenta = ? " 
						       + "AND   pcr.id.rol.idRol = 'TIT'", new Object[] { noCuentaAltamira});
		
			if (pcrList != null && pcrList.size() > 0) {
				final PersonaCuentaRol pcr = (PersonaCuentaRol) pcrList.get(0);
				
				idPersona = pcr.getId().getPersona().getIdPersona().toString();
			}
			return idPersona;
	}

	/**
	 * @see SicaServiceData#findCuentaEjecutivoByContratoSica(String, String).
	 */
	public List findCuentaEjecutivoByContratoSica(String idTipoEjecutivo,
			String contratoSica) {
		return getHibernateTemplate()
				.find(
						"SELECT ce FROM com.ixe.ods.bup.model.CuentaEjecutivo AS ce, "
								+ "com.ixe.ods.bup.model.CuentaContrato AS cc WHERE ce.id.noCuenta = cc.noCuenta AND "
								+ "ce.id.tipo = ? AND ce.status.estatus = 'VIG' AND ce.id.noCuenta = ? "
								+ "AND cc.tipoCuentaContrato.idTipoCuenta = 'CAM10'",
						new Object[] { idTipoEjecutivo, contratoSica });
	}
	
    /**
	 * @see SicaServiceData#findCuentaContratoByContratoSica(String).
	 */
	public List findFechaAperturaByContratoSica(ContratoSica no_cuenta)  {
		return getHibernateTemplate()
				.find(
						"SELECT cc FROM com.ixe.ods.bup.model.CuentaContrato AS cc WHERE cc.noCuenta = ?",new Object[]{no_cuenta});

	}

	/**
	 * @see SicaServiceData#findCuentaEjecutivoByContratoSicaExcStatus(String,
	 *      String).
	 */
	public List findCuentaEjecutivoByContratoSicaExcStatus(
			String idTipoEjecutivo, String contratoSica) {
		return getHibernateTemplate()
				.find(
						"SELECT ce FROM com.ixe.ods.bup.model.CuentaEjecutivo AS ce, "
								+ "com.ixe.ods.bup.model.CuentaContrato AS cc WHERE ce.id.noCuenta = cc.noCuenta AND "
								+ "ce.id.tipo = ? AND ce.id.noCuenta = ? AND cc.tipoCuentaContrato.idTipoCuenta = 'CAM10'",
						new Object[] { idTipoEjecutivo, contratoSica });
	}

	/**
	 * @see SicaServiceData#findCuentaEjecutivoByContratoSicaAndStatus(String,
	 *      String, String, Integer).
	 */
	public List findCuentaEjecutivoByContratoSicaAndStatus(
			String idTipoEjecutivo, String status, String contratoSica,
			Integer idPersona) {
		return getHibernateTemplate()
				.find(
						"SELECT ce FROM com.ixe.ods.bup.model.CuentaEjecutivo AS ce, "
								+ "com.ixe.ods.bup.model.CuentaContrato AS cc WHERE ce.id.noCuenta = cc.noCuenta AND "
								+ "ce.id.tipo = ? AND ce.status.estatus = ? AND ce.id.noCuenta = ? "
								+ "AND cc.tipoCuentaContrato.idTipoCuenta = 'CAM10' AND ce.id.ejecutivo.idPersona = ?",
						new Object[] { idTipoEjecutivo, status, contratoSica,
								idPersona });
	}

	/*
	 * Para liberar el cach&eacute; de segundo nivel de hibernate
	 * 
	 * @see
	 * com.ixe.ods.sica.sdo.SicaServiceData#flushEvict(com.ixe.ods.bup.model
	 * .CuentaEjecutivo)
	 */
	public void flushEvict(Object object) {
		getHibernateTemplate().flush();
		getHibernateTemplate().evict(object);
		getHibernateTemplate().clear();
	}

	/**
	 * @see SicaServiceData#findCuentaEjecutivoByContratoSicaAndIdEjecutivo(String,
	 *      String, Integer)
	 */
	public boolean findCuentaEjecutivoByContratoSicaAndIdEjecutivo(
			String idTipoEjecutivo, String contratoSica, Integer idPersona) {
		List l = getHibernateTemplate()
				.find(
						"SELECT ce FROM com.ixe.ods.bup.model.CuentaEjecutivo AS ce, "
								+ "com.ixe.ods.bup.model.CuentaContrato AS cc WHERE ce.id.noCuenta = cc.noCuenta AND "
								+ "ce.id.tipo = ?  AND ce.id.noCuenta = ? AND cc.tipoCuentaContrato.idTipoCuenta = 'CAM10' "
								+ "AND ce.id.ejecutivo.idPersona = ?",
						new Object[] { idTipoEjecutivo, contratoSica, idPersona });
		return l.isEmpty();
	}

	/**
	 * @see SicaServiceData#findCuentasEjecutivo(String, Integer).
	 */
	public List findCuentasEjecutivo(String idTipoEjecutivo, Integer idPersona) {
		return getHibernateTemplate()
				.find(
						"SELECT ce FROM com.ixe.ods.bup.model.CuentaEjecutivo AS ce, "
								+ "com.ixe.ods.bup.model.CuentaContrato AS cc WHERE ce.id.noCuenta = cc.noCuenta AND "
								+ "ce.id.tipo = ? AND ce.status.estatus = 'VIG' AND ce.id.ejecutivo.idPersona = ? "
								+ "AND cc.tipoCuentaContrato.idTipoCuenta = 'CAM10'",
						new Object[] { idTipoEjecutivo, idPersona });
	}

	/**
	 * @see SicaServiceData#findCuentasNoAsignadas(String, String, String,
	 *      String, String).
	 */
	public List findCuentasNoAsignadas(String razonSocial, String nombre,
			String paterno, String materno, String noCuenta) {
		List params = new ArrayList();
		StringBuffer sb = new StringBuffer(
				"SELECT cs FROM com.ixe.ods.bup.model.ContratoSica AS cs LEFT JOIN cs.roles AS pcr");

		if (StringUtils.isNotEmpty(razonSocial)) {
			sb
					.append(
							", com.ixe.ods.bup.model.PersonaMoral AS p WHERE pcr.id.persona.idPersona = p.idPersona AND ")
					.append(
							"cs.noCuenta not in (SELECT cc.noCuenta FROM com.ixe.ods.bup.model.CuentaEjecutivo AS ce, com.ixe.ods.bup.model.ContratoSica AS cc WHERE ce.id.noCuenta = cc.noCuenta AND ce.id.tipo.idTipoEjecutivo = '"
									+ ID_TIPO_EJECUTIVO
									+ "' AND ce.status.estatus = 'VIG') ")
					.append(" AND p.razonSocial like ?");
			params.add(razonSocial.trim() + "%");
		} else {
			if (StringUtils.isNotEmpty(noCuenta)) {
				sb
						.append(
								", com.ixe.ods.bup.model.Persona AS p WHERE pcr.id.persona.idPersona = p.idPersona AND ")
						.append(
								"cs.noCuenta not in (SELECT cc.noCuenta FROM com.ixe.ods.bup.model.CuentaEjecutivo AS ce, com.ixe.ods.bup.model.ContratoSica AS cc WHERE ce.id.noCuenta = cc.noCuenta AND ce.id.tipo.idTipoEjecutivo = '"
										+ ID_TIPO_EJECUTIVO
										+ "' AND ce.status.estatus = 'VIG') ")
						.append(" AND cs.noCuenta = ?");
				params.add(noCuenta.trim());
			} else {
				sb
						.append(
								", com.ixe.ods.bup.model.PersonaFisica AS p WHERE pcr.id.persona.idPersona = p.idPersona AND ")
						.append(
								"cs.noCuenta not in (SELECT cc.noCuenta FROM com.ixe.ods.bup.model.CuentaEjecutivo AS ")
						.append(
								"ce, com.ixe.ods.bup.model.ContratoSica AS cc WHERE ce.id.noCuenta = cc.noCuenta AND ce.id.tipo.idTipoEjecutivo = '"
										+ ID_TIPO_EJECUTIVO
										+ "' AND ce.status.estatus = 'VIG') ");
				if (StringUtils.isNotEmpty(nombre)) {
					sb.append(" AND p.nombre like ?");
					params.add(nombre.trim() + "%");
				}
				if (StringUtils.isNotEmpty(paterno)) {
					sb.append(" AND p.paterno like ?");
					params.add(paterno.trim() + "%");
				}
				if (StringUtils.isNotEmpty(materno)) {
					sb.append(" AND p.materno like ?");
					params.add(materno.trim() + "%");
				}
			}
		}
		sb
				.append(" AND cs.statusOrigen = 'Active' AND cs.status.estatus = 'ACTIVA' AND cs.tipoCuentaContrato.idTipoCuenta = 'CAM10'");
		sb.append(" AND pcr.id.rol = 'TIT' AND pcr.status.estatus = 'VIG'");
		return getHibernateTemplate().find(sb.toString(), params.toArray());
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findCveBanxicoByIdPersona(Integer)
	 */
	public String findCveBanxicoByIdPersona(Integer idPersona) {
		List list = getHibernateTemplate().findByNamedQuery(
				"findCveBanxicoByIdPersona", idPersona);
		String resultado = null;
		if (list.size() > 0) {
			resultado = ((ContraparteBanxico) list.get(0)).getIdSaif();
		}
		return resultado;
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findAllMesas().
	 */
	public List findAllMesas() {
		return getHibernateTemplate().find("FROM MesaCambio AS m");
	}

	/**
	 * @see SicaServiceData#obtenerMontosMaximosByCanalProducto(com.ixe.ods.sica.model.Canal,
	 *      String).
	 */
	public List obtenerMontosMaximosByCanalProducto(Canal canal,
			String claveFormaLiquidacion) {
		List montosMaximos = getHibernateTemplate().findByNamedQuery(
				"findMontosMaximosByCanalProducto",
				new Object[] { canal.getIdCanal(), claveFormaLiquidacion });
		if (montosMaximos.isEmpty()) {
			for (Iterator it = findDivisasFrecuentes().iterator(); it.hasNext();) {
				Divisa div = (Divisa) it.next();
				MontoMaximoSucursal mms = new MontoMaximoSucursal(canal, div,
						claveFormaLiquidacion);
				montosMaximos.add(mms);
				store(mms);
			}
		}
		return montosMaximos;
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findMesaCambio(int)
	 */
	public MesaCambio findMesaCambio(int idMesaCambio) {
		List mesas = getHibernateTemplate()
				.find(
						"FROM MesaCambio AS m INNER JOIN FETCH m.divisaReferencia WHERE m.idMesaCambio = ?",
						new Integer(idMesaCambio));
		return (MesaCambio) (mesas.isEmpty() ? null : mesas.get(0));
	}

	/**
	 * @see SicaServiceData#findMontoChequesViajeroUsdFechaLiquidacion(String,
	 *      java.util.Date).
	 */
	public double findMontoChequesViajeroUsdFechaLiquidacion(String noCuenta,
			Date fechaLiquidacion) {
		List dets = getHibernateTemplate()
				.find(
						"SELECT det.divisa.idDivisa, det.monto, "
								+ "det.tipoCambio FROM DealDetalle as det WHERE det.deal.fechaLiquidacion "
								+ "BETWEEN ? AND ? AND det.deal.contratoSica.noCuenta = ? AND "
								+ "det.claveFormaLiquidacion = ? AND det.statusDetalleDeal != 'CA' AND "
								+ "det.recibimos = 'N'",
						new Object[] { DateUtils.inicioDia(fechaLiquidacion),
								DateUtils.finDia(fechaLiquidacion), noCuenta,
								Constantes.TRAVELER_CHECKS });
		double montoUsd = 0;
		PrecioReferenciaActual pr = null;
		for (Iterator it = dets.iterator(); it.hasNext();) {
			Object[] valores = (Object[]) it.next();
			String idDivisa = (String) valores[0];
			if (Divisa.DOLAR.equals(idDivisa)) {
				montoUsd += ((Double) valores[1]).doubleValue();
			} else {
				if (pr == null) {
					pr = findPrecioReferenciaActual();
				}
				montoUsd += ((Double) valores[1]).doubleValue()
						* ((Double) valores[2]).doubleValue()
						/ pr.getPreRef().getMidSpot().doubleValue();
			}
		}
		return montoUsd;
	}

	/**
	 * @see SicaServiceData#findAcumuladoUsdCompraVentaDiarioMensual(String,
	 *      java.util.Date, boolean, String, List, boolean).
	 */
	public double findAcumuladoUsdCompraVentaDiarioMensual(String noCuenta,
			Date fechaLiquidacion, boolean diario, String divisa,
			List productosEnRestriccion, boolean recibimos) {
		Date fechaInicial = fechaLiquidacion;
		Date fechaFinal = fechaLiquidacion;
		if (!diario) {
			fechaInicial = DateUtils.primerDiaDeMes(fechaLiquidacion);
			fechaFinal = DateUtils.finDia(DateUtils
					.ultimoDiaDelMes(fechaLiquidacion));
		} else {
			fechaFinal = DateUtils.finDia(fechaLiquidacion);
		}
		StringBuffer query = new StringBuffer(
				"SELECT det.divisa.idDivisa, det.monto, "
						+ "det.tipoCambio FROM DealDetalle as det WHERE det.deal.fechaLiquidacion "
						+ "BETWEEN ? AND ? AND det.deal.contratoSica.noCuenta = ? AND "
						+ "det.statusDetalleDeal != 'CA' AND det.reversado = 0 AND det.recibimos = ");
		if (recibimos) {
			query.append("'S'");
		} else {
			query.append("'N'");
		}
		query.append(" AND (");
		for (Iterator it = productosEnRestriccion.iterator(); it.hasNext();) {
			String producto = (String) it.next();
			query.append("det.claveFormaLiquidacion = ");
			query.append("'");
			query.append(producto);
			query.append("'");
			query.append(" OR ");
		}
		query = new StringBuffer(query.substring(0, query.length() - 4));
		query.append(")");
		query.append(" AND det.divisa.idDivisa = '");
		query.append(divisa);
		query.append("'");
		List dets = getHibernateTemplate().find(
				query.toString(),
				new Object[] { DateUtils.inicioDia(fechaInicial), fechaFinal,
						noCuenta });
		double montoUsd = 0;
		PrecioReferenciaActual pr = null;
		for (Iterator it = dets.iterator(); it.hasNext();) {
			Object[] valores = (Object[]) it.next();
			String idDivisa = (String) valores[0];
			if (Divisa.DOLAR.equals(idDivisa)) {
				montoUsd += ((Double) valores[1]).doubleValue();
			} else if (divisa.equals(idDivisa)) {
				if (pr == null) {
					pr = findPrecioReferenciaActual();
				}
				montoUsd += ((Double) valores[1]).doubleValue()
						* ((Double) valores[2]).doubleValue()
						/ pr.getPreRef().getMidSpot().doubleValue();
			}
		}
		return montoUsd;
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findNombreBanxicoByIdPersona(Integer)
	 */
	public String findNombreBanxicoByIdPersona(Integer idPersona) {
		List list = getHibernateTemplate().findByNamedQuery(
				"findCveBanxicoByIdPersona", idPersona);
		return !list.isEmpty() ? ((ContraparteBanxico) list.get(0)).getNombre()
				: null;
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findDeal(int).
	 */
	public List findDealsPendientes(final Date gc, final Date fechaLiquidacion) {
		return getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException {
				Query q = session
						.createQuery("FROM Deal AS d WHERE (d.statusDeal = 'AL' OR d.statusDeal = 'PE') AND d.fechaLiquidacion BETWEEN :fInicio AND :fFin ORDER BY d.idDeal");
				q.setParameter("fInicio", gc);
				q.setParameter("fFin", fechaLiquidacion);
				q.setMaxResults(MAX_RESULTS_VESPERTINO);
				return q.list();
			}
		});
	}

	/**
	 * 
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findDealsDetallesPendientes(Date,
	 *      Date).
	 */
	public List findDealsDetallesPendientes(final Date gc,
			final Date fechaLiquidacion) {
		return getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException {
				Query q = session
						.createQuery("FROM DealDetalle AS det "
								+ "WHERE det.deal.statusDeal != 'CA' "
								+ "AND det.deal.tipoValor != 'CASH' "
								+ "AND det.deal.fechaLiquidacion BETWEEN :fInicio AND :fFin "
								+ "ORDER BY det.divisa.idDivisa, det.recibimos, det.deal.fechaLiquidacion");
				q.setParameter("fInicio", gc);
				q.setParameter("fFin", fechaLiquidacion);
				q.setMaxResults(MAX_RESULTS_VESPERTINO);
				return q.list();
			}
		});
	}

	/**
	 * @see SicaServiceData#findDealsBlockerVespertino().
	 */
	public List findDealsBlockerVespertino() {
		Date fechaInicioDia = DateUtils.inicioDia(getFechaSistema());
		Set deals = new HashSet(
				getHibernateTemplate()
						.find(
								"FROM Deal AS d LEFT JOIN FETCH "
										+ "d.detalles AS det LEFT JOIN FETCH d.contratoSica LEFT JOIN FETCH "
										+ "d.canalMesa.canal c LEFT JOIN FETCH c.sucursal WHERE d.statusDeal != 'CA' AND "
										+ "d.reversado = 0 AND d.fechaLiquidacion >= ?",
								fechaInicioDia));
		if (deals.isEmpty()) {
			throw new SicaException(
					"No se encontraron deals que bloqueen el cierre Vespertino");
		}
		try {
			for (Iterator it = deals.iterator(); it.hasNext();) {
				Deal deal = (Deal) it.next();
				if (deal.getCliente() != null) {
					Hibernate.initialize(deal.getCliente());
					if (deal.isInterbancario()) {
						if (deal.getBroker() != null) {
							deal.getBroker().getId().getPersonaMoral()
									.getNombreCompleto();
						}
					}
				}
			}
		} catch (HibernateException e) {
			throw SessionFactoryUtils.convertHibernateAccessException(e);
		}
		return new ArrayList(deals);
	}

	/**
	 * @see SicaServiceData#findDealsPendientesDia(int).
	 */
	public List findDealsPendientesDia(int idUsuario) {
		Date fechaDiasAnteriores = DateUtils
				.diasPrevios(getFechaSistema(), -10);
		Date fechaFinDia = DateUtils.finDia(getFechaSistema());
		Set deals = new HashSet(
				getHibernateTemplate()
						.find(
								"FROM Deal AS d LEFT JOIN FETCH "
										+ "d.detalles AS det LEFT JOIN FETCH d.contratoSica WHERE (d.statusDeal != 'CA' "
										+ "AND d.statusDeal != 'RE') AND (d.usuario.idUsuario= ?)  AND (d.fechaCaptura "
										+ "BETWEEN ? AND ? OR d.fechaLiquidacion BETWEEN ? AND ?)",
								new Object[] { new Integer(idUsuario),
										fechaDiasAnteriores, fechaFinDia,
										fechaDiasAnteriores, fechaFinDia }));
		if (deals.isEmpty()) {
			throw new SicaException("No se encontraron deals pendientes.");
		}
		try {
			for (Iterator it = deals.iterator(); it.hasNext();) {
				Deal deal = (Deal) it.next();
				if (deal.getCliente() != null) {
					Hibernate.initialize(deal.getCliente());
					if (deal.isInterbancario()) {
						if (deal.getBroker() != null) {
							deal.getBroker().getId().getPersonaMoral()
									.getNombreCompleto();
						}
					}
				}
			}
		} catch (HibernateException e) {
			throw SessionFactoryUtils.convertHibernateAccessException(e);
		}
		return new ArrayList(deals);
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findDealsPendientesPorLiquidar(Date,
	 *      Date);
	 */
	public List findDealsPendientesPorLiquidar(Date fechaInicioDia,
			Date fechaFinDia) {
		return getHibernateTemplate()
				.find(
						"FROM DealDetalle AS dd WHERE "
								+ "(dd.deal.statusDeal = 'CO' OR dd.deal.statusDeal = 'PL' OR "
								+ "dd.deal.statusDeal = 'TP') AND dd.deal.fechaCaptura BETWEEN ? AND ? "
								+ "ORDER BY dd.deal.idDeal ASC",
						new Object[] { fechaInicioDia, fechaFinDia });
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findDeal(int).
	 */
	public List findDealsNoCancel() {
		return getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException {
				Query q = session
						.createQuery("FROM Deal AS d WHERE d.statusDeal != 'CA' ORDER BY d.idDeal DESC");
				q.setMaxResults(MAX_RESULTS_VESPERTINO);
				return q.list();
			}
		});
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findDeal(int).
	 */
	public List findDealsPendientesTomSpot(final Date gc,
			final Date fechaLiquidacion) {
		return getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException {
				Query q = session
						.createQuery("FROM Deal AS d WHERE d.tipoValor !='CASH' "
								+ "AND d.fechaLiquidacion BETWEEN :fInicio AND :fFin ORDER BY d.idDeal");
				q.setParameter("fInicio", gc);
				q.setParameter("fFin", fechaLiquidacion);
				q.setMaxResults(MAX_RESULTS_VESPERTINO);
				return q.list();
			}
		});
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findPolizaPorFecha(Date,Date)
	 */
	public List findPolizaPorFecha(Date fechaHoy, Date fechaHoy2) {
		return getHibernateTemplate().findByNamedQuery("findPolizaPorFecha",
				new Object[] { fechaHoy, fechaHoy2 });
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findDireccion(int)
	 */
	public Direccion findDireccion(int idDireccion) {
		List dirs = getHibernateTemplate()
				.find(
						"FROM Direccion AS d LEFT JOIN "
								+ "FETCH d.pais LEFT JOIN FETCH d.tipoDireccion WHERE d.idDireccion = ?",
						new Integer(idDireccion));
		if (dirs.isEmpty()) {
			return null;
		}
		Direccion dir = (Direccion) dirs.get(0);
		dir.getDelegacionMunicipio();
		return dir;
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findDivisa(String).
	 */
	public Divisa findDivisa(String idDivisa) {
		try {
			Divisa divisa = (Divisa) find(Divisa.class, idDivisa);
			if (divisa != null && !Hibernate.isInitialized(divisa)) {
				Hibernate.initialize(divisa);
			}
			return divisa;
		} catch (HibernateException e) {
			throw SessionFactoryUtils.convertHibernateAccessException(e);
		}
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findDivisaCuenta(String).
	 */
	public String findDivisaCuenta(String noCuenta) {
		List cuentas = getHibernateTemplate()
				.find(
						"SELECT cc FROM CuentaContrato AS cc LEFT "
								+ "JOIN FETCH cc.tipoCuentaContrato AS tcc WHERE cc.noCuenta = ?",
						noCuenta);
		if (cuentas.isEmpty()) {
			throw new SicaException(
					"No se encontr\u00f3 la cuenta n\u00famero " + noCuenta
							+ ".");
		}
		CuentaContrato cuentaContrato = (CuentaContrato) cuentas.get(0);
		if (cuentaContrato != null
				&& ("DLS".equals(cuentaContrato.getTipoCuentaContrato()
						.getIdTipoOrigen())
						|| "PAY".equals(cuentaContrato.getTipoCuentaContrato()
								.getIdTipoOrigen()) || "DPD"
						.equals(cuentaContrato.getTipoCuentaContrato()
								.getIdTipoOrigen()))) {
			return Divisa.DOLAR;
		} else if (cuentaContrato != null
				&& "EUR".equals(cuentaContrato.getTipoCuentaContrato()
						.getIdTipoOrigen())) {
			return Divisa.EURO;
		}
		return Divisa.PESO;
	}
	
	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#obtenerDescripcionBloqueo(String).
	 */
	
	public String obtenerDescripcionBloqueo(Integer idBloqueo){
		String descripcionRetornar = "";
		List motivo = getHibernateTemplate()
		.find(
				"SELECT tb FROM TipoBloqueo AS tb WHERE tb.idBloqueo = ?",
				idBloqueo);
		
		if (motivo.isEmpty()) {
			throw new SicaException(
					"No se encontr\u00f3 la descripcion para el id " + idBloqueo
							+ ".");
		}
		
		TipoBloqueo descripcion = (TipoBloqueo) motivo.get(0);
		
		
		descripcionRetornar =  descripcion.getDescripcion();
		
		return descripcionRetornar;
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#isDivisaReferencia(com.ixe.ods.sica.model.DealDetalle)
	 *      .
	 */
	public boolean isDivisaReferencia(DealDetalle dealDetalle) {
		MesaCambio mc = findMesaCambio(dealDetalle.getDeal().getCanalMesa()
				.getMesaCambio().getIdMesaCambio());
		return mc.getDivisaReferencia().getIdDivisa().equals(
				dealDetalle.getDivisa().getIdDivisa());
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findDealsNoCancel().
	 */
	public List findDealsPendientesReconocimiento() {
		return getHibernateTemplate().findByNamedQuery(
				"findDealsPendientesReconocimiento");
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findDealsNoCancelYSinContrato()
	 *      .
	 */
	public List findDealsNoCancelYSinContrato() {
		return getHibernateTemplate().find(
				"FROM Deal AS d WHERE d.statusDeal != 'CA' AND "
						+ "d.contratoSica.noCuenta is null ORDER BY d.idDeal");
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findDealDetalle(int).
	 */
	public DealDetalle findDealDetalle(int idDealPosicion) {
		List dets = getHibernateTemplate().find(
				"SELECT dd FROM DealDetalle AS dd LEFT JOIN FETCH "
						+ "dd.deal WHERE dd.idDealPosicion = ?",
				new Integer(idDealPosicion));
		if (dets.isEmpty()) {
			throw new SicaException(
					"No se encontr\u00f3 el detalle n\u00famero "
							+ idDealPosicion);
		}
		return (DealDetalle) dets.get(0);
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findDealDetalleConDivisaFactorDivisa(int)
	 *      .
	 */
	public DealDetalle findDealDetalleConDivisaFactorDivisa(int idDealPosicion) {
		List dets = getHibernateTemplate()
				.find(
						"SELECT dd FROM DealDetalle AS dd LEFT JOIN FETCH "
								+ "dd.deal.contratoSica.roles LEFT JOIN FETCH dd.deal.canalMesa.canal "
								+ "LEFT JOIN FETCH dd.divisa "
								+ "WHERE dd.idDealPosicion = ?",
						new Integer(idDealPosicion));
		if (dets.isEmpty()) {
			throw new SicaException(
					"No se encontr\u00f3 el detalle n\u00famero "
							+ idDealPosicion);
		}
		return (DealDetalle) dets.get(0);
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findDealsAlertas().
	 */
	public List findDealsAlertas() {
		return getHibernateTemplate()
				.find(
						"FROM Deal AS d WHERE d.statusDeal != ? AND "
								+ "d.statusDeal != ? AND d.statusDeal != ? AND d.tipoDeal != 'I'",
						new Object[] { Deal.STATUS_DEAL_CANCELADO,
								Deal.STATUS_DEAL_TOTALMENTE_LIQUIDADO,
								Deal.STATUS_DEAL_PROCESO_CAPTURA });
	}

	/**
	 * @see SicaServiceData#findDealsDetallesByDateAndDivisaAndFormaLiquidacion(java.util.Date,
	 *      java.util.Date, String, String).
	 */
	public List findDealsDetallesByDateAndDivisaAndFormaLiquidacion(
			Date fechaCaptura, Date gc, String idDivisa,
			String claveFormaLiquidacion) {
		return getHibernateTemplate().findByNamedQuery(
				"findDealsDetallesByDateAndDivisaAndFormaLiquidacion",
				new Object[] { fechaCaptura, gc, idDivisa,
						claveFormaLiquidacion });
	}

	/**
	 * @see SicaServiceData#findDealsDetallesByDateAndDivisaAndFormaLiquidacionAndOperacion(java.util.Date,
	 *      java.util.Date, String, String, String)
	 */
	public List findDealsDetallesByDateAndDivisaAndFormaLiquidacionAndOperacion(
			Date fechaCaptura, Date gc, String idDivisa, String cfl,
			String operacion) {
		return getHibernateTemplate()
				.findByNamedQuery(
						"findDealsDetallesByDateAndDivisaAndFormaLiquidacionAndOperacion",
						new Object[] { fechaCaptura, gc, idDivisa, cfl,
								operacion });
	}

	/**
	 * @see SicaServiceData#findDealsDetallesByIdPersonaAndFechas(java.util.Date,
	 *      java.util.Date, Integer).
	 */
	public List findDealsDetallesByIdPersonaAndFechas(Date de, Date hasta,
			Integer idPersona) {
		return getHibernateTemplate().findByNamedQuery(
				"findDealsDetallesByIdPersonaAndFechas",
				new Object[] { de, hasta, idPersona });
	}

	/**
	 * @see SicaServiceData#findDealsDetallesPagoAnticipadoByDate(java.util.Date,
	 *      java.util.Date, String).
	 */
	public List findDealsDetallesPagoAnticipadoByDate(Date fechaCaptura,
			Date gc, String orden) {
		String q = "SELECT dd FROM DealDetalle AS dd WHERE dd.deal.pagoAnticipado = 'S' AND dd.deal.fechaCaptura BETWEEN ? AND ? ORDER BY "
				+ orden;
		List params = new ArrayList();
		params.add(fechaCaptura);
		params.add(gc);
		return getHibernateTemplate().find(q, params.toArray());
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findDealsPendientesAsignarASwap(String)
	 *      .
	 */
	public List findDealsPendientesAsignarASwap(String noCuenta) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < Deal.EV_IND_INT_ASIGN_SWAP; i++) {
			sb.append("_");
		}
		sb.append("S%");
		return getHibernateTemplate()
				.find(
						"FROM Deal AS d WHERE d.contratoSica.noCuenta = ? AND d.statusDeal != ? AND d.eventosDeal like ?",
						new Object[] { noCuenta, Deal.STATUS_DEAL_CANCELADO,
								sb.toString() });
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findDivisasByCanal(String).
	 */
	public List findDivisasByCanal(String idCanal) {
		List divisas = new ArrayList();
		try {
			Canal canal = findCanal(idCanal);
			List spreads = findSpreadsActualesByTipoPizarron(canal
					.getTipoPizarron().getIdTipoPizarron());
			for (Iterator it = spreads.iterator(); it.hasNext();) {
				Spread spread = (Spread) it.next();
				if (!divisas.contains(spread.getDivisa())) {
					divisas.add(spread.getDivisa());
				}
			}
			Collections.sort(divisas, new Comparator() {
				public int compare(Object o1, Object o2) {
					Divisa d1 = (Divisa) o1;
					Divisa d2 = (Divisa) o2;
					return d1.getOrden() < d2.getOrden() ? -1
							: d1.getOrden() == d2.getOrden() ? 0 : 1;

				}
			});
		} catch (SicaException e) {
			throw new SicaException(
					"No se encontraron divisas para operar el canal: "
							+ idCanal + ".");
		}
		return divisas;
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findDivisasByTipoPizarron(int).
	 */
	public List findDivisasByTipoPizarron(int idTipoPizarron) {
		List spreads = findSpreadsActualesByTipoPizarron(idTipoPizarron);
		List divisas = new ArrayList();
		for (Iterator it = spreads.iterator(); it.hasNext();) {
			Spread spread = (Spread) it.next();
			if (!divisas.contains(spread.getDivisa())) {
				divisas.add(spread.getDivisa());
			}
		}
		Collections.sort(divisas, new Comparator() {
			public int compare(Object o1, Object o2) {
				Divisa d1 = (Divisa) o1;
				Divisa d2 = (Divisa) o2;
				return d1.getOrden() < d2.getOrden() ? -1 : d1.getOrden() == d2
						.getOrden() ? 0 : 1;

			}
		});
		return divisas;
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findDivisasFrecuentes().
	 */
	public List findDivisasFrecuentes() {
		return getHibernateTemplate().findByNamedQuery("findDivisasFrecuentes");
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findDivisasFrecuentesMasPesos()
	 *      .
	 */
	public List findDivisasFrecuentesMasPesos() {
		List divisas = new ArrayList();
		divisas.addAll(getHibernateTemplate().findByNamedQuery(
				"findDivisasFrecuentes"));
		Divisa pesos = findDivisa(Divisa.PESO);
		divisas.add(pesos);
		return divisas;
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findDivisasMetales().
	 */
	public List findDivisasMetales() {
		return getHibernateTemplate().findByNamedQuery("findDivisasMetales");
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findDivisasMetalesFactor().
	 */
	public List findDivisasMetalesFactor() {
		return getHibernateTemplate().findByNamedQuery(
				"findDivisasMetalesFactor");
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findDivisasNoFrecuentes().
	 */
	public List findDivisasNoFrecuentes() {
		return getHibernateTemplate().findByNamedQuery(
				"findDivisasNoFrecuentes");
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findDivisasNoFrecuentesFactor()
	 *      .
	 */
	public List findDivisasNoFrecuentesFactor() {
		return getHibernateTemplate().findByNamedQuery(
				"findDivisasNoFrecuentesFactor");
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findEstadoSistemaActual().
	 */
	public Estado findEstadoSistemaActual() {
		List estados = getHibernateTemplate().findByNamedQuery(
				"findEstadoActual");
		if (estados.size() != 1) {
			throw new SicaException(
					"No se pudo encontrar el estado actual del sistema.");
		}
		return (Estado) estados.get(0);
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findEstadoSistemaActual().
	 */
	public List findAllEstadoOrdenado() {
		return getHibernateTemplate().findByNamedQuery("findAllEstadoOrdenado");
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findFactorDivisa(int).
	 * 
	 * @deprecated Ya no ser&aacute; necesario este m&eacute;todo ya que se
	 *             utiliza el valor directo del Factor Divisa.
	 */
	public FactorDivisaActual findFactorDivisa(int idFactorDivisa) {
		List fds = getHibernateTemplate()
				.find(
						"FROM FactorDivisaActual AS fda INNER JOIN FETCH fda.facDiv.fromDivisa INNER JOIN FETCH "
								+ "fda.facDiv.toDivisa WHERE fda.idFactorDivisa = ?",
						new Integer(idFactorDivisa));
		return (FactorDivisaActual) (fds.isEmpty() ? null : fds.get(0));
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findFactorDivisaActualByID(Integer)
	 *      .
	 */
	public FactorDivisaActual findFactorDivisaActualByID(Integer idFactorDivisa) {
		List lista = getHibernateTemplate().findByNamedQuery(
				"findFactorDivisaActualByID", idFactorDivisa);
		return lista.isEmpty() ? null : (FactorDivisaActual) lista.get(0);
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findFactorDivisaActualFromTo(String,
	 *      String).
	 */
	public FactorDivisaActual findFactorDivisaActualFromTo(String fromDivisa,
			String toDivisa) {
		List fdas = getHibernateTemplate()
				.findByNamedQuery("findFactorDivisaActual",
						new Object[] { fromDivisa, toDivisa });
		if (fdas.size() > 0) {
			return (FactorDivisaActual) fdas.get(0);
		}
		return null;
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findFacultadesCanales().
	 */
	public List findFacultadesCanales() {
		return getHibernateTemplate().find(
				"FROM com.ixe.ods.seguridad.model.Facultad AS f WHERE "
						+ "f.nombre like ?", "SICA_CAN_%");
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findFechasInhabilesEua().
	 * @return List de FechaInhabilEua.
	 */
	public List findFechasInhabilesEua() {
		return getHibernateTemplate()
				.find(
						"FROM FechaInhabilEua AS f "
								+ "WHERE f.idFechaInhabilEua.fecha >= ? AND f.status = ? "
								+ "ORDER BY f.idFechaInhabilEua.fecha",
						new Object[] { DateUtils.inicioDia(), "AC" });
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findHistorialLineaOperacionLogByIdLineaOperacion(Integer)
	 */
	public List findHistorialLineaOperacionLogByIdLineaOperacion(
			Integer idLineaOperacion) {
		GregorianCalendar fechaInicio = new GregorianCalendar();
		fechaInicio.add(Calendar.MONTH, -1);
		return getHibernateTemplate().findByNamedQuery(
				"findHistorialLineaOperacionLogByIdLineaOperacion",
				new Object[] { idLineaOperacion,
						DateUtils.inicioDia(fechaInicio.getTime()) });
	}

	/**
	 * @see SicaServiceData#findHistoricoTipoCambio(String, java.util.Date,
	 *      java.util.Date).
	 */
	public List findHistoricoTipoCambio(String idDivisa, Date fechaInicio,
			Date fechaFinal) {
		return getHibernateTemplate()
				.find(
						"FROM HistoricoTipoCambio AS thtc WHERE "
								+ "thtc.divisa.idDivisa = ? AND thtc.fecha BETWEEN ? AND ? ORDER BY thtc.fecha",
						new Object[] { idDivisa,
								DateUtils.inicioDia(fechaInicio),
								DateUtils.finDia(fechaFinal) });
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findIdJerarquiaSICA(String)
	 */
	public int findIdJerarquiaSICA(String idTipoEjecutivo) {
		List listJerarquias = getHibernateTemplate()
				.find(
						"SELECT j FROM "
								+ "com.ixe.ods.seguridad.model.Jerarquia AS j WHERE j.idTipoEjecutivo = ?",
						idTipoEjecutivo);
		return listJerarquias.size() > 0 ? ((Jerarquia) listJerarquias.get(0))
				.getIdJerarquia() : 0;
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findLimiteByMesaAndDivisa(int,
	 *      String).
	 */
	public List findLimiteByMesaAndDivisa(int idMesaCambio, String idDivisa) {
		return getHibernateTemplate().findByNamedQuery(
				"findLimiteByMesaAndDivisa",
				new Object[] { new Integer(idMesaCambio), idDivisa });
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findLineaCreditoByCorporativoAndStatusLinea(Integer,
	 *      String)
	 */
	public List findLineaCreditoByCorporativoAndStatusLinea(
			Integer idCorporativo, String status) {
		return getHibernateTemplate().findByNamedQuery(
				"findLineaCreditoByCorporativoAndStatusLinea",
				new Object[] { idCorporativo, status.trim() });
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findLineaOperacionByBroker(int)
	 */
	public LineaOperacion findLineaOperacionByBroker(int idBroker) {
		List list = getHibernateTemplate().findByNamedQuery(
				"findLineaOperacionByBroker", new Integer(idBroker));
		return (list.size() > 0 ? (LineaOperacion) list.get(0) : null);
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findLineasDeCreditoByStatus(String)
	 */
	public List findLineasDeCreditoByStatus(String status) {
		return getHibernateTemplate().findByNamedQuery(
				"findLineasDeCreditoByStatus", status.trim());
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findLineasOperacionByBrokersAndEstatus(List,
	 *      String)
	 */
	public List findLineasOperacionByBrokersAndEstatus(List brokers,
			String estatus) {
		List lineasOperacion = new ArrayList();
		for (Iterator it = brokers.iterator(); it.hasNext();) {
			PersonaMoral broker = (PersonaMoral) it.next();
			List lops = getHibernateTemplate().findByNamedQuery(
					"findLineaOperacionByBrokerAndStatus",
					new Object[] { broker.getIdPersona(), estatus });
			LineaOperacion lineaOperacion = lops.size() > 0 ? (LineaOperacion) lops
					.get(0)
					: null;
			if (lineaOperacion != null) {
				lineasOperacion.add(lineaOperacion);
			}
		}
		return lineasOperacion;
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findLineasOperacionByStatus(String)
	 */
	public List findLineasOperacionByStatus(String status) {
		return getHibernateTemplate().findByNamedQuery(
				"findLineasOperacionByStatus", status);
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findLogByDeal(Integer).
	 */
	public List findLogByDeal(Integer idDeal) {
		return getHibernateTemplate().findByNamedQuery("findLogByDeal", idDeal);
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findLogByDetalle(Integer).
	 */
	public List findLogByDetalle(Integer idDeal) {
		return getHibernateTemplate().findByNamedQuery("findLogByDetalle",
				idDeal);
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findMaximoDealByIdMesaCambio(int)
	 *      .
	 */
	public List findMaximoDealByIdMesaCambio(int idMesaCambio) {
		return getHibernateTemplate().findByNamedQuery(
				"findMaximoDealByIdMesaCambio", new Integer(idMesaCambio));
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findMaximoDealByIdMesaCambioAndIdDivisa(int,
	 *      String).
	 */
	public MaximoDeal findMaximoDealByIdMesaCambioAndIdDivisa(int idMesaCambio,
			String idDivisa) {
		List l = getHibernateTemplate().findByNamedQuery(
				"findMaximoDealByIdMesaCambioAndIdDivisa",
				new Object[] { new Integer(idMesaCambio), idDivisa });
		return l.isEmpty() ? null : (MaximoDeal) l.get(0);
	}

	/**
	 * Regresa la lista de medios de contacto para la persona especificada.
	 * 
	 * @return List.
	 */
	public List findMedioContactoByIdPersona(Integer idPersona) {
		return getHibernateTemplate().find(
				"FROM MedioContacto AS mc WHERE mc.idPersona = ?", idPersona);
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findAutMediosContactoPersona(int)
	 *      .
	 */
	public List findAutMediosContactoPersona(int idPersona) {
		return getHibernateTemplate()
				.find(
						"FROM AutMedioContacto AS amc WHERE "
								+ "amc.persona.idPersona = ? ORDER BY amc.fechaCreacion",
						new Integer(idPersona));
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findMnemonicosByPlantillaPantalla(String)
	 */
	public List findMnemonicosByPlantillaPantalla(String plantillaPantalla) {
		List mnemonicos = getHibernateTemplate().findByNamedQuery(
				"findMnemonicosByPlantillaPantalla", plantillaPantalla + "%");
		return mnemonicos.size() > 0 ? mnemonicos : null;
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findParametro(String).
	 */
	public ParametroSica findParametro(String idParametro) {
		ParametroSica resultado = null;
		for (Iterator it = parametrosCacheList.iterator(); it.hasNext();) {
			resultado = (ParametroSica) it.next();
			if (resultado.getIdParametro().equalsIgnoreCase(idParametro)) {
				logger.info("----------------- parametro en cache:"
						+ idParametro);
				return resultado;
			}
		}
		List parametros = getHibernateTemplate().find(
				"FROM ParametroSica AS p WHERE " + "p.idParametro = ?",
				idParametro);
		if (parametros == null || parametros.isEmpty()) {
			return null;
		}
		resultado = (ParametroSica) parametros.get(0);
		logger.info("------- agregando parametro al cache:"
				+ resultado.getIdParametro());
		parametrosCacheList.add(resultado);
		logger.info("-------  tam cache:" + parametrosCacheList.size());
		return resultado;
	}
	
	

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findPersonaByContratoSica(String)
	 */
	public Persona findPersonaByContratoSica(String noCuenta) {
		List list = getHibernateTemplate().findByNamedQuery(
				"findPersonaContratoSica", noCuenta);
		if (list.size() > 0) {
			Object[] obj = (Object[]) list.get(0);
			return (Persona) obj[1];
		}
		return null;
	}

	/**
	 * @see SicaServiceData#findPersonaByRfc(String).
	 * @param rfc
	 *            El rfc a localizar.
	 * @return Persona.
	 */
	public Persona findPersonaByRfc(String rfc) {
		List personas = getHibernateTemplate().find(
				"FROM Persona AS p WHERE p.rfc like ?", rfc + "%");

		if (personas.isEmpty()) {
			return null;
		}
		return (Persona) personas.get(0);
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findPersonaMoralByIdPersona(Integer)
	 *      .
	 */
	public PersonaMoral findPersonaMoralByIdPersona(Integer idPersona) {
		List list = getHibernateTemplate().find(
				"SELECT pm FROM com.ixe.ods.bup.model.PersonaMoral AS pm LEFT JOIN FETCH "
						+ "pm.sectorEconomico WHERE pm.idPersona = ?",
				idPersona);
		return list.size() > 0 ? (PersonaMoral) list.get(0) : null;
	}
	
	public PersonaFisica findPersonaFisicaByIdPersona(Integer idPersona) {
		List list = getHibernateTemplate().find(
				"SELECT pf FROM com.ixe.ods.bup.model.PersonaFisica AS pf LEFT JOIN FETCH "
						+ "pf.sectorEconomico WHERE pf.idPersona = ?",
				idPersona);
		return list.size() > 0 ? (PersonaFisica) list.get(0) : null;
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findPersonaMoralNotBrokerByRazonSocial(String)
	 */
	public List findPersonaMoralNotBrokerByRazonSocial(String razonSocial) {
		return getHibernateTemplate()
				.find(
						"FROM com.ixe.ods.bup.model.PersonaMoral as pm LEFT "
								+ "JOIN FETCH pm.sectorEconomico LEFT JOIN FETCH pm.actividadEconomica WHERE "
								+ "pm.razonSocial like ? AND pm.idPersona NOT IN(SELECT "
								+ "bk.id.personaMoral.idPersona FROM Broker as bk)",
						razonSocial + "%");
	}	

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findPlantillaPantallaByMnemonico(String)
	 *      .
	 */
	public PlantillaPantalla findPlantillaPantallaByMnemonico(String mnemonico) {
		List pps = getHibernateTemplate().findByNamedQuery(
				"findPlantillaPantallaByMnemonico", mnemonico + "%");
		for (Iterator it = pps.iterator(); it.hasNext();) {
			PlantillaPantalla plantillaPantalla = (PlantillaPantalla) it.next();
			plantillaPantalla.getNombrePantalla();
		}
		if (pps.size() == 0) {
			throw new SicaException(
					"No se encontr\u00f3 la configuraci\u00f3n para el "
							+ "mnem\u00f3nico " + mnemonico);
		}
		return (PlantillaPantalla) pps.get(0);
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findPlantillas(String,
	 *      ContratoSica)
	 */
	public List findPlantillas(String plantilla, ContratoSica contratoSica) {
		try {
			Hibernate.initialize(contratoSica);
			return findPlantillas(plantilla, contratoSica.getNoCuenta());
		} catch (HibernateException e) {
			throw SessionFactoryUtils.convertHibernateAccessException(e);
		}
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findPlantillas(String, String)
	 */
	public List findPlantillas(String plantilla, String noCuenta) {
		if (plantilla.equals("Nacionales")) {
			return getHibernateTemplate().findByNamedQuery(
					"findPlantillasNacionales", noCuenta);
		} else if (plantilla.equals("TranNacionales")) {
			return getHibernateTemplate().findByNamedQuery(
					"findPlantillasTranNacionales", noCuenta);
		} else if (plantilla.equals("Intls")) {
			return getHibernateTemplate().findByNamedQuery(
					"findPlantillasIntls", noCuenta);
		} else if (plantilla.equals("CuentaIxe")) {
			return getHibernateTemplate().findByNamedQuery(
					"findPlantillasCuentaIxe", noCuenta);
		}
		return null;
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findPlantillasWithBeneficiario(String,
	 *      String).
	 */
	public List findPlantillasWithContratoSicaAndBeneficiario(String plantilla,
			String noCuenta) {
		if (plantilla.equals("Nacionales")) {
			List plantillas = getHibernateTemplate()
					.find(
							"FROM PlantillaNacional as pn WHERE "
									+ "pn.contratoSica.noCuenta = ? Order By pn.idPlantilla",
							noCuenta);
			for (Iterator it = plantillas.iterator(); it.hasNext();) {
				PlantillaNacional plantillaObj = (PlantillaNacional) it.next();
				try {
					Hibernate.initialize(plantillaObj.getBeneficiario());
					Hibernate.initialize(plantillaObj.getContratoSica());
				} catch (HibernateException e) {
					throw SessionFactoryUtils
							.convertHibernateAccessException(e);
				}
			}
			return plantillas;
		} else if (plantilla.equals("TranNacionales")) {
			List plantillas = getHibernateTemplate()
					.find(
							"FROM PlantillaTranNacional as ptn "
									+ "WHERE ptn.contratoSica.noCuenta = ? Order By ptn.idPlantilla",
							noCuenta);
			for (Iterator it = plantillas.iterator(); it.hasNext();) {
				PlantillaTranNacional plantillaObj = (PlantillaTranNacional) it
						.next();
				try {
					Hibernate.initialize(plantillaObj.getBeneficiario());
					Hibernate.initialize(plantillaObj.getContratoSica());
				} catch (HibernateException e) {
					throw SessionFactoryUtils
							.convertHibernateAccessException(e);
				}
			}
			return plantillas;
		} else if (plantilla.equals("Intls")) {
			List plantillas = getHibernateTemplate()
					.find(
							"FROM PlantillaIntl as pi "
									+ "WHERE pi.contratoSica.noCuenta = ? Order By pi.idPlantilla",
							noCuenta);
			for (Iterator it = plantillas.iterator(); it.hasNext();) {
				PlantillaIntl plantillaObj = (PlantillaIntl) it.next();
				try {
					Hibernate.initialize(plantillaObj.getBeneficiario());
					Hibernate.initialize(plantillaObj.getContratoSica());
				} catch (HibernateException e) {
					throw SessionFactoryUtils
							.convertHibernateAccessException(e);
				}
			}
			return plantillas;
		} else if (plantilla.equals("CuentaIxe")) {
			List plantillas = getHibernateTemplate()
					.find(
							"FROM PlantillaCuentaIxe as pci "
									+ "WHERE pci.contratoSica.noCuenta = ? Order By pci.idPlantilla",
							noCuenta);
			for (Iterator it = plantillas.iterator(); it.hasNext();) {
				PlantillaCuentaIxe plantillaObj = (PlantillaCuentaIxe) it
						.next();
				try {
					Hibernate.initialize(plantillaObj.getBeneficiario());
					Hibernate.initialize(plantillaObj.getContratoSica());
				} catch (HibernateException e) {
					throw SessionFactoryUtils
							.convertHibernateAccessException(e);
				}
			}
			return plantillas;
		}
		return null;
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findPlantillas(String, String,
	 *      String)
	 */
	public List findPlantillas(String plantilla, String noCuenta,
			String mnemonico) {
		if (plantilla.equals("Nacionales")) {
			return getHibernateTemplate().findByNamedQuery(
					"findPlantillasNacionalesMnemonico",
					new Object[] { noCuenta, mnemonico });
		} else if (plantilla.equals("TranNacionales")) {
			return getHibernateTemplate().findByNamedQuery(
					"findPlantillasTranNacionalesMnemonico",
					new Object[] { noCuenta, mnemonico });
		} else if (plantilla.equals("Intls")) {
			return getHibernateTemplate().findByNamedQuery(
					"findPlantillasIntlsMnemonico",
					new Object[] { noCuenta, mnemonico });
		} else if (plantilla.equals("CuentaIxe")) {
			return getHibernateTemplate().findByNamedQuery(
					"findPlantillasCuentaIxeMnemonico",
					new Object[] { noCuenta, mnemonico });
		}
		return null;
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findPlantillasByContratoMnemonicos(String,
	 *      java.util.List).
	 */
	public List findPlantillasByContratoMnemonicos(String noCuentaContrato,
			List mnemonicos) {
		if (mnemonicos.isEmpty()) {
			String msg = "No se encontraron plantillas pues no hay mnem\u00f3nicos aplicables";
			warn(msg);
			throw new SicaException(msg);
		}
		StringBuffer sb = new StringBuffer(
				"FROM Plantilla as p WHERE p.contratoSica.noCuenta = ? AND ")
				.append("p.statusPlantilla != ? AND p.mnemonico in (");
		int i = 0;
		for (Iterator it = mnemonicos.iterator(); it.hasNext(); i++) {
			if (i > 0) {
				sb.append(", ");
			}
			sb.append("'").append(it.next()).append("'");
		}
		sb.append(")");
		return getHibernateTemplate().find(
				sb.toString(),
				new Object[] { noCuentaContrato,
						Plantilla.STATUS_PLANTILLA_SUSPENDIDA });
	}

	/**
	 * @see SicaServiceData#findPlantillasByContratoMnemonicos(String, String,
	 *      String, String, String, boolean).
	 */
	public List findPlantillasByContratoMnemonicos(String ticket,
			final String noCuenta, final String mnemonico, String cveFormLiq,
			String idDivisa, boolean recibimos) {
		List plantillas = getHibernateTemplate().executeFind(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException {
						Criteria criteria = session
								.createCriteria(Plantilla.class);
						criteria.add(Expression.eq("contratoSica.noCuenta",
								noCuenta));
						if (mnemonico != null) {
							criteria.add(Expression.eq("mnemonico", mnemonico));
						}
						criteria.add(Expression.not(Expression.eq(
								"statusPlantilla",
								Plantilla.STATUS_PLANTILLA_SUSPENDIDA)));
						return criteria.list();
					}
				});
		for (Iterator it = plantillas.iterator(); it.hasNext();) {
			IPlantilla plantilla = (IPlantilla) it.next();
			FormaPagoLiq fpl = getSicaSiteCache().getFormaPagoLiq(ticket,
					plantilla.getMnemonico());
			if ((cveFormLiq != null && !fpl.getClaveFormaLiquidacion().equals(
					cveFormLiq))
					|| (idDivisa != null && !fpl.getIdDivisa().equals(idDivisa))
					|| recibimos != fpl.isRecibimos().booleanValue()) {
				it.remove();
			}
		}
		return plantillas;
	}

	/**
	 * @see SicaServiceData#findPlantillaById(int).
	 */
	public IPlantilla findPlantillaById(int idPlantilla) {
		return (IPlantilla) getHibernateTemplate().find(
				"FROM Plantilla as p WHERE p.idPlantilla = ?",
				new Integer(idPlantilla)).get(0);
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findPlantillasPendientes().
	 */
	public List findPlantillasPendientes() {
		return getHibernateTemplate()
				.find(
						"FROM Plantilla as p WHERE p.statusPlantilla = ? "
								+ "ORDER BY p.contratoSica.noCuenta, p.beneficiario.nombreCorto",
						Plantilla.STATUS_PLANTILLA_PENDIENTE);
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findPosicionByIdMesaCambio(int)
	 *      .
	 */
	public List findPosicionByIdMesaCambio(int idMesaCambio) {
		return getHibernateTemplate().findByNamedQuery(
				"findPosicionByIdMesaCambio", new Integer(idMesaCambio));
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findPosicionDivisasFrecuentes()
	 *      .
	 */
	public List findPosicionDivisasFrecuentes() {
		return getHibernateTemplate().find(
				"FROM Posicion AS p INNER JOIN FETCH p.divisa div WHERE div.tipo = 'F' "
						+ "ORDER BY p.mesaCambio.idMesaCambio, div.orden");
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findPosicionByIdMesaCambioAndIdDivisa(int,
	 *      String).
	 */
	public List findPosicionByIdMesaCambioAndIdDivisa(int idMesaCambio,
			String idDivisa) {
		return getHibernateTemplate().findByNamedQuery(
				"findPosicionByIdMesaCambioAndIdDivisa",
				new Object[] { new Integer(idMesaCambio), idDivisa });
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findHistoricoPosicion(int,
	 *      String, Date, Date).
	 */
	public List findHistoricoPosicion(int idMesaCambio, String idDivisa,
			Date incioDia, Date finDia) {
		return getHibernateTemplate()
				.find(
						"FROM HistoricoPosicion AS p WHERE p.divisa.tipo != 'M' AND p.mesaCambio.idMesaCambio = ? AND p.divisa.idDivisa = ? AND p.ultimaModificacion BETWEEN ? AND ? ORDER BY p.idPosicion",
						new Object[] { new Integer(idMesaCambio), idDivisa,
								incioDia, finDia });
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findPromotorByContratoSica(String)
	 */
	public EmpleadoIxe findPromotorByContratoSica(String noCuenta) {
		List proms = getHibernateTemplate()
				.find(
						"SELECT DISTINCT ei FROM com.ixe.ods.bup.model.EmpleadoIxe AS ei, "
								+ "com.ixe.ods.bup.model.CuentaEjecutivo ce, com.ixe.ods.bup.model.CuentaContrato cc WHERE "
								+ "ei.idPersona = ce.id.ejecutivo.idPersona AND ce.id.noCuenta = cc.noCuenta AND ce.id.noCuenta = ? "
								+ "AND ce.status.estatus = 'VIG' AND ce.id.tipo.idTipoEjecutivo = '"
								+ ID_TIPO_EJECUTIVO
								+ "' AND "
								+ "cc.tipoCuentaContrato.idTipoCuenta = 'CAM10'",
						noCuenta);
		if (proms.isEmpty()) {
			throw new SicaException(
					"El contrato SICA no tiene un promotor asignado.");
		}
		EmpleadoIxe promotor = (EmpleadoIxe) proms.get(0);
		promotor.getNombreCompleto();
		return promotor;
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findPromotoresSica(String).
	 */
	public List findPromotoresSica(String idEjecutivo) {
		List promotores = new ArrayList();
		int IdJerarquia = findIdJerarquiaSICA(ID_TIPO_EJECUTIVO);
		List promoJerarquia = getHibernateTemplate()
				.find(
						"SELECT nj FROM com.ixe.ods.seguridad.model.NodoJerarquia AS nj WHERE nj.idJerarquia = ?",
						new Integer(IdJerarquia));
		for (Iterator iter = promoJerarquia.iterator(); iter.hasNext();) {
			NodoJerarquia element = (NodoJerarquia) iter.next();
			List prom = getHibernateTemplate()
					.find(
							"SELECT e FROM com.ixe.ods.bup.model.EmpleadoIxe AS e WHERE e.idPersona = ?",
							element.getIdPersona());
			if (prom.size() > 0) {
				promotores.add(prom.get(0));
			}
		}
		return promotores;
	}

	/**
	 * Regresa la lista de Empleados que se encuentran registrados en el grupo
	 * de trabajo especificado.
	 * 
	 * @param idGrupoTrabajo
	 *            la clave del grupo de trabajo.
	 * @return List de EmpleadoIxe.
	 */
	public List findPromotoresByGrupoTrabajo(String idGrupoTrabajo) {
		return getHibernateTemplate()
				.find(
						"SELECT gtp.id.promotor FROM "
								+ "GrupoTrabajoPromotor AS gtp WHERE gtp.id.grupoTrabajo.idGrupoTrabajo = ? "
								+ "ORDER BY gtp.id.promotor.nombreCorto",
						idGrupoTrabajo);
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findPropiedad
	 */
	public String findPropiedad(String propiedad) {
		Propiedad prop = (Propiedad) find(Propiedad.class, propiedad);
		return prop.getValor();
	}

	/**
	 * @see SicaServiceData#findReversoByStatusPendiente().
	 */
	public List findReversoByStatusPendiente() {
		List reversos = getHibernateTemplate().findByNamedQuery(
				"findReversoByStatus");
		for (Iterator it = reversos.iterator(); it.hasNext();) {
			Reverso rev = (Reverso) it.next();
			if (!Hibernate.isInitialized(rev.getUsuario().getPersona())) {
				try {
					Hibernate.initialize(rev.getUsuario().getPersona());
				} catch (HibernateException e) {
					throw SessionFactoryUtils
							.convertHibernateAccessException(e);
				}
			}
		}
		return reversos;
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findRegistroPosicionByIdMesaCambioAndIdDivisa(int,
	 *      String).
	 */
	public Posicion findRegistroPosicionByIdMesaCambioAndIdDivisa(
			int idMesaCambio, String idDivisa) {
		List list = findPosicionByIdMesaCambioAndIdDivisa(idMesaCambio,
				idDivisa);
		if (list.size() > 0) {
			return (Posicion) list.get(0);
		} else {
			throw new SicaException(
					"No se encontr\u00f3 el registro de la posici\u00f3n con idMesaCambio: "
							+ idMesaCambio + " y idDivisa: " + idDivisa + ".");
		}
	}

	/**
	 * Regresa todos los registros de la tabla SAP_A_GENPOL_XS que corresponden
	 * al deal especificado.
	 * 
	 * @param idDeal
	 *            El n&uacute;mero de deal.
	 * @return List de SapGenPolXs.
	 */
	public List findSapGenPolXsByIdDeal(int idDeal) {
		return getHibernateTemplate().find(
				"FROM SapGenPolXs AS sap WHERE sap.producto = ?", "" + idDeal);
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findSpreadActual(Integer).
	 */
	public SpreadActual findSpreadActual(Integer idSpread) {
		List lista = (getHibernateTemplate().findByNamedQuery(
				"findSpreadActual", idSpread));
		return lista.size() == 0 ? null : (SpreadActual) lista.get(0);
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findSpreadsActualesByMesaCanalDivisa(int,
	 *      String, String).
	 */
	public List findSpreadsActualesByMesaCanalDivisa(int idMesaCambio,
			String idCanal, String idDivisa) {
		List spreads = getHibernateTemplate().findByNamedQuery(
				"findSpreadsActualesByMesaCanalDivisa",
				new Object[] { new Integer(idMesaCambio), idCanal, idDivisa });
		ordenarSpreads(spreads);
		return spreads;
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findSpreadsActualesByTipoPizarronDivisa(TipoPizarron,
	 *      String).
	 */
	public List findSpreadsActualesByTipoPizarronDivisa(
			TipoPizarron tipoPizarron, String idDivisa) {
		List spreads = getHibernateTemplate().findByNamedQuery(
				"findSpreadsActualesByTipoPizarronDivisa",
				new Object[] { tipoPizarron.getIdTipoPizarron(), idDivisa });
		ordenarSpreads(spreads);
		return spreads;
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findSpreadsActualesByMesaCanalDivisaFormaLiquidacion(int,
	 *      String, String, String).
	 */
	public List findSpreadsActualesByMesaCanalDivisaFormaLiquidacion(
			int idMesaCambio, String idCanal, String idDivisa,
			String claveFormaLiquidacion) {
		return getHibernateTemplate().findByNamedQuery(
				"findSpreadActualByMesaCanalFormaLiquidacionDivisa",
				new Object[] { new Integer(idMesaCambio), idCanal,
						claveFormaLiquidacion, idDivisa });
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findSpreadsActualesByTipoPizarronDivisaFormaLiquidacion(String,
	 *      String, TipoPizarron)
	 */
	public List findSpreadsActualesByTipoPizarronDivisaFormaLiquidacion(
			String idDivisa, String claveFormaLiquidacion,
			TipoPizarron tipoPizarron) {
		return getHibernateTemplate().findByNamedQuery(
				"findSpreadActualByTipoPizarronFormaLiquidacionDivisa",
				new Object[] { tipoPizarron.getIdTipoPizarron(),
						claveFormaLiquidacion, idDivisa });
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findSpreadsActualesByMesaSucursalDivisaFormaLiquidacion(int,
	 *      String, String).
	 */
	public List findSpreadsActualesByMesaSucursalDivisaFormaLiquidacion(
			int idMesaCambio, String idDivisa, String claveFormaLiquidacion) {
		return getHibernateTemplate().findByNamedQuery(
				"findSpreadsActualesByMesaSucursalDivisaFormaLiquidacion",
				new Object[] { new Integer(idMesaCambio), idDivisa,
						claveFormaLiquidacion });
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findStatusByDeal(Integer).
	 */
	public List findStatusByDeal(Integer idDeal) {
		return getHibernateTemplate().findByNamedQuery("findStatusLogByDeal",
				idDeal);
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findStatusByDetalle(Integer).
	 */
	public List findStatusByDetalle(Integer idDeal) {
		return getHibernateTemplate().findByNamedQuery(
				"findStatusLogByDetalle", idDeal);
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findTimeOut()
	 */
	public List findSwapCierreDelDia() {
		return getHibernateTemplate().findByNamedQuery("findSwapsCierreDelDia");
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findTimeOut()
	 */
	public String findTimeOut() {
		return ((ParametroSica) find(ParametroSica.class,
				ParametroSica.DEAL_TIMEOUT)).getValor();
	}

	/**
	 * @see SicaServiceData#findTitularCuentaEje(String).
	 * @param noCuentaIxe
	 *            El n&uacute;mero de cuenta de cheques.
	 * @return Persona.
	 */
	public Persona findTitularCuentaEje(String noCuentaIxe) {
		List pcrs = getHibernateTemplate()
				.find(
						"SELECT pcr.id.persona FROM PersonaCuentaRol AS "
								+ "pcr WHERE pcr.id.cuenta.noCuenta = ? AND pcr.id.rol.idRol = 'TIT' AND "
								+ "pcr.status.estatus = 'VIG' AND pcr.id.cuenta.tipoCuentaContrato.eje = 'S'",
						new Object[] { noCuentaIxe });

		if (pcrs.isEmpty()) {
			throw new SicaException(
					"No se encontr\u00f3 el titular de la cuenta "
							+ noCuentaIxe);
		}
		return (Persona) pcrs.get(0);
	}

	/**
	 * @see SicaServiceData#validaCuentaEje(String).
	 * @param noCuentaIxe
	 *            El n&uacute;mero de cuenta Ixe.
	 */
	public void validaCuentaEje(String noCuentaIxe) {
		List noCuentaValido = getHibernateTemplate()
				.find(
						"SELECT pcr.id.cuenta.noCuenta "
								+ "FROM PersonaCuentaRol AS pcr WHERE pcr.id.cuenta.noCuenta = ? AND "
								+ "pcr.id.rol.idRol = 'TIT' AND pcr.status.estatus = 'VIG' AND "
								+ "pcr.id.cuenta.tipoCuentaContrato.eje = 'S'",
						new Object[] { noCuentaIxe });
		if (noCuentaValido.isEmpty()) {
			throw new SicaException("No se encontr\u00f3 la cuenta "
					+ noCuentaIxe);
		}
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findTraspasosByFechaOper(Date,
	 *      Date).
	 */
	public List findTraspasosByFechaOper(Date fechaOperIni, Date fechaOperFin) {
		return getHibernateTemplate().findByNamedQuery(
				"findTraspasosByFechaOper",
				new Object[] { fechaOperIni, fechaOperFin });
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findAllTraspasosPortafolios().
	 */
	public List findAllTraspasosPortafolios() {
		return getHibernateTemplate().findByNamedQuery(
				"findAllTraspasosPortafolios");
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findTraspasosPortafoliosDiaActual(Date)
	 */
	public List findTraspasosPortafoliosDiaActual(Date fechaOperacionHoy) {
		return getHibernateTemplate().findByNamedQuery(
				"findTraspasosPortafoliosDiaActual", fechaOperacionHoy);
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findTraspasosPortafoliosByFechaOperacion(Date,
	 *      Date)
	 */
	public List findTraspasosPortafoliosByFechaOperacion(Date fechaOperIni,
			Date fechaOperFin) {
		return getHibernateTemplate().findByNamedQuery(
				"findTraspasosPortafoliosByFechaOperacion",
				new Object[] { fechaOperIni, fechaOperFin });
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findUtilidadesGlobales(java.util.Date,
	 *      java.util.Date).
	 */
	public List findUtilidadesGlobales(Date fechaInicio, Date fechaFin) {
		return getHibernateTemplate().find(
				"FROM RecoUtilidad AS ru INNER JOIN FETCH ru.divisa "
						+ "WHERE ru.fechaOperacion BETWEEN ? AND ? "
						+ "ORDER BY ru.fechaOperacion, ru.divisa.idDivisa",
				new Object[] { DateUtils.inicioDia(fechaInicio),
						DateUtils.finDia(fechaFin) });
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#getABABOFA().
	 */
	public String getABABOFA() {
		ParametroSica idUsuario = (ParametroSica) find(ParametroSica.class,
				ParametroSica.ABA_BANK_OF_AMERICA);
		return idUsuario != null ? idUsuario.getValor() : "";
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#getFechaLimiteCapturaDeal().
	 */
	public int getFechaLimiteCapturaDeal() {
		ParametroSica p = (ParametroSica) find(ParametroSica.class,
				ParametroSica.FECHA_LIM_CAPT_CONT);
		return Integer.parseInt(p.getValor());
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#generarUtilidadEstadoVespertino(String,
	 *      java.util.Map).
	 */
	public void generarUtilidadEstadoVespertino(String ticket, Map precios, String ip, IUsuario usuario) {
		// Obteniendo el Precio de Referencia MID SPOT
		double precioReferencia = findPrecioReferenciaActual().getPreRef()
				.getMidSpot().doubleValue();
		int idUsuario = ((Integer) precios.get("idUsuario")).intValue();
		PrecioReferenciaActual preRef = (PrecioReferenciaActual) precios
				.get("preRef");
		ParametroSica param = (ParametroSica) precios.get("param");
		ParametroSica tipCambVesp = (ParametroSica) precios.get("tipCambVesp");
		Posicion posicion;
		double monto;
		List posiciones = findAll(Posicion.class);
		for (Iterator it1 = posiciones.iterator(); it1.hasNext();) {
			posicion = (Posicion) it1.next();
			// Si se trata de PESOS
			if (Divisa.PESO.equals(posicion.getMesaCambio()
					.getDivisaReferencia().getIdDivisa())) {
				// Calculando el Monto de la Perdida o Utilidad.
				monto = calculaMontoReconocimientoUtilidadesAut(posicion);
				boolean isUtilidad = monto >= 0;
				// Si se trata de UTILIDAD
				if (isUtilidad && posicion.getUtilidadGlobal() != 0
						&& posicion.getUtilidadMesa() != 0) {
					// Venta (Utilidad)
					// Insertando en SC_RECO_UTILIDAD QUE HEREDA DE
					// SC_DEAL_POSICION
					RecoUtilidad dpVenta = new RecoUtilidad();
					dpVenta.setDivisa(posicion.getDivisa()); // Divisa
					// Operacion.
					dpVenta.setMesaCambio(posicion.getMesaCambio()); // "Del Portafolio".
					dpVenta.setIdUsuario(idUsuario); // El Usuario que efectuo
					// la Operacion.
					dpVenta.setMonto(monto);
					dpVenta.setRecibimos(false); // Entregamos.
					dpVenta.setTipoCambio(1); // Desde donde se captura el tipo
					// de cambie con el que se
					// quiere queamanezca la Mesa.
					dpVenta.setTipoDeal(DealPosicion.TIPO_DEAL_UTILIDADES); // Tipo
					// de
					// Operacion
					// "U".
					dpVenta.setUtilidadGlobal(posicion.getUtilidadGlobal()); // Valor
					// de
					// Utilidad
					// Global
					// "Del Portafolio"
					// y
					// la
					// Divisa
					// Operacion.
					dpVenta.setUtilidadMesa(posicion.getUtilidadMesa()); // Valor
					// de
					// Utilidad
					// Mesa
					// "Del Portafolio"
					// y
					// la
					// Divisa
					// Operacion.
					dpVenta.setTipoReco(RecoUtilidad.AUTOMATICO); // Tipo de
					// Reconocimiento
					// Manual
					// "A".
					store(dpVenta);
				}
				// Si se trata de PERDIDA
				else if (!isUtilidad && posicion.getUtilidadGlobal() != 0
						&& posicion.getUtilidadMesa() != 0) {
					// Compra (Perdida)
					// Insertando en SC_RECO_UTILIDAD QUE HEREDA DE
					// SC_DEAL_POSICION
					RecoUtilidad dpCompra = new RecoUtilidad();
					dpCompra.setDivisa(posicion.getDivisa()); // Divisa
					// Operacion.
					dpCompra.setMesaCambio(posicion.getMesaCambio()); // "Del Portafolio".
					dpCompra.setIdUsuario(idUsuario); // El Usuario que efectuo
					// la Operacion.
					dpCompra.setMonto(Math.abs(monto)); // Se pone 0.0 porque no
					// es un monto
					// dolarizado.
					dpCompra.setRecibimos(true); // Recibimos
					dpCompra.setTipoCambio(1); // En este caso tipoCambio = 0.0
					// porque se trata de PESOS.
					dpCompra.setTipoDeal(DealPosicion.TIPO_DEAL_UTILIDADES); // Tipo
					// de
					// Operacion
					// "U".
					dpCompra.setUtilidadGlobal(posicion.getUtilidadGlobal()); // Valor
					// de
					// Utilidad
					// Global
					// "Del Portafolio"
					// y
					// la
					// Divisa
					// Operacion.
					dpCompra.setUtilidadMesa(posicion.getUtilidadMesa()); // Valor
					// de
					// Utilidad
					// Mesa
					// "Del Portafolio"
					// y
					// la
					// Divisa
					// Operacion.
					dpCompra.setTipoReco(RecoUtilidad.AUTOMATICO); // Tipo de
					// Reconocimiento
					// Manual
					// "M".
					store(dpCompra);
				}
			}
		}
		List utilidades = findUtilidadCierreVespertino(DateUtils.inicioDia(),
				new Date());
		for (Iterator iterator = utilidades.iterator(); iterator.hasNext();) {
			RecoUtilidad ru = (RecoUtilidad) iterator.next();
			if (ru.isRecibimos()) {
				PosicionLog plCompra = new PosicionLog();
				plCompra.setTipoValor(Constantes.CASH);
				ParametroSica idCanal = (ParametroSica) find(
						ParametroSica.class, "CANAL_MESA_"
								+ ru.getMesaCambio().getNombre().trim());
				plCompra.setCanalMesa(new CanalMesa(findCanal(idCanal
						.getValor()), ru.getMesaCambio()));
				plCompra
						.setDealPosicion((DealPosicion) find(
								DealPosicion.class, new Integer(ru
										.getIdDealPosicion())));
				plCompra.setDivisa(ru.getDivisa());
				plCompra.setClaveFormaLiquidacion(Constantes.TRANSFERENCIA);
				plCompra.setIdUsuario(ru.getIdUsuario());
				plCompra.setMonto(0.00);
				plCompra.setMontoMn(Math.abs(ru.getMonto()));
				plCompra
						.setNombreCliente(HibernateSicaServiceData.CLIENTE_IXE_DIVISAS);
				plCompra.setPrecioReferencia(precioReferencia);
				plCompra.setTipoCambioCliente(ru.getTipoCambio());
				plCompra.setTipoCambioMesa(ru.getTipoCambio());
				plCompra.setTipoOperacion(PosicionLog.PESOS_PERDIDA);
				verificarPosicionDetalle(plCompra);
				// aqui verifica si se puede insertar registro
				store(plCompra);
			} else {
				PosicionLog plVenta = new PosicionLog();
				plVenta.setTipoValor(Constantes.CASH);
				ParametroSica idCanal = (ParametroSica) find(
						ParametroSica.class, "CANAL_MESA_"
								+ ru.getMesaCambio().getNombre().trim());
				plVenta.setCanalMesa(new CanalMesa(
						findCanal(idCanal.getValor()), ru.getMesaCambio()));
				plVenta.setDealPosicion((DealPosicion) find(DealPosicion.class,
						new Integer(ru.getIdDealPosicion())));
				plVenta.setDivisa(ru.getDivisa());
				plVenta.setClaveFormaLiquidacion(Constantes.TRANSFERENCIA);
				plVenta.setIdUsuario(ru.getIdUsuario());
				plVenta.setMonto(0.00);
				plVenta.setMontoMn(ru.getMonto());
				plVenta
						.setNombreCliente(HibernateSicaServiceData.CLIENTE_IXE_DIVISAS);
				plVenta.setPrecioReferencia(precioReferencia);
				plVenta.setTipoCambioCliente(ru.getTipoCambio());
				plVenta.setTipoCambioMesa(ru.getTipoCambio());
				plVenta.setTipoOperacion(PosicionLog.PESOS_UTILIDADES);
				// aqui verifica si se puede insertar registro
				verificarPosicionDetalle(plVenta);
				store(plVenta);
			}
		}
		storePrecioReferencia(preRef);
		update(param);
		update(tipCambVesp);
		getPizarronServiceData().refrescarPizarrones(ticket, ip, usuario);
		List estados = findAll(Estado.class);
		for (Iterator it = estados.iterator(); it.hasNext();) {
			Estado estado = (Estado) it.next();
			if (estado.isActual()) {
				estado.setActual(false);
			}
			if (estado.getIdEstado() == Estado.ESTADO_OPERACION_VESPERTINA) {
				estado.setActual(true);
				estado.setHoraInicio(HOUR_FORMAT.format(new Date()));
			}
		}
		update(estados);
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#validarClientePorMontoMayorATresMilUSD(com.ixe.ods.sica.model.Deal,
	 *      com.ixe.ods.bup.model.Persona).
	 */
	public void validarClientePorMontoMayorATresMilUSD(Deal deal, Persona nuevoCliente)
    throws SicaException
{
	// obtenemos el monto Limite para solicitar documentacion	
    double montoLimite = getLimiteNormativoUSD(nuevoCliente);
    super.logger.info("---- montoLimite:" + montoLimite);
    super.logger.info("---- nuevoCliente:" + nuevoCliente.getIdPersona());
    ContratoSica nuevoContratoSica = findContratoSicaByIdPersona(nuevoCliente.getIdPersona());
    super.logger.info("---- contrato a validar:" + nuevoContratoSica.getNoCuenta());
    boolean isCliente = isClienteOrUsuario(nuevoCliente.getIdPersona());
    if(!isCliente)
    {
        String idDivisa = null;
        String idProducto = null;
        double montoUSDRecibimos = 0.0D;
        super.logger.info("******************* isCliente:  " + isCliente);
        super.logger.info("******************* Mensage de chuy entra a validar usuario para el reverso ****" +
"**************"
);
        for(Iterator it = deal.getDetalles().iterator(); it.hasNext();)
        {
            DealDetalle det = (DealDetalle)it.next();
            idDivisa = det.getDivisa().getIdDivisa();
            idProducto = det.getClaveFormaLiquidacion();
            if(idProducto == null)
            {
                idProducto = "";
            }
            boolean isRecibimos = det.isRecibimos();
            super.logger.info("************************ entro al for por detalle de divisa ********************" +
"**** "
);
            super.logger.info("Valor de idDivisa -----> " + idDivisa + " Valor de idProducto -----> " + idProducto + " isRecibimos -----> " + isRecibimos + " folio detalle -----> " + det.getFolioDetalle());
            if(!DealDetalle.STATUS_DET_CANCELADO.equals(det.getStatusDetalleDeal()) && idDivisa.equals(Divisa.DOLAR) && det.getClaveFormaLiquidacion().equals(Constantes.EFECTIVO) && isRecibimos)
            {
                montoUSDRecibimos += det.getMontoUSD();
            }
            super.logger.info("--- validando monto total de sumarizado de COMPRA de dolares:" + montoUSDRecibimos);
            if(montoUSDRecibimos > montoLimite)
            {
                super.logger.info("Se supera el monto limite se de bebe validar la documentaicon entregada");
                try
                {
                    String noCuenta = nuevoContratoSica.getNoCuenta();
                    Integer idPersona = nuevoCliente.getIdPersona();
                    boolean isPersonaFisica = (Constantes.TP_ACTIVIDAD_EMPRESARIAL.equals(nuevoCliente.getTipoPersona()) || Constantes.PERSONA_FISICA.equals(nuevoCliente.getTipoPersona()));
                    isValidoClienteSica(idPersona, isPersonaFisica, noCuenta);
                }
                catch(ContratacionException e)
                {
                    String message = e.getMessage();
                    throw new SicaException("No se puede realizar la operaci\363n, por falta de documentaci\363n requerida en" +
" el M\363dulo de Contrataci\363n.\n"
+ message);
                }
                catch(Exception e)
                {
                    throw new SicaException("Hubo un error en la obtenci\363n de los datos del cliente. Favor de contactar a " +
"sistemas."
);
                }
            } else
            {
                super.logger.info("************* Mensage de chuy entra a validar usuario (para el reverso) para las" +
" demas divisas y demas productos **************"
);
                Map detallesDivisas = new HashMap();
                detallesDivisas.putAll(getDealService().getDetallesDivisas(deal));
                double montoTotal = 0.0D;
                Iterator iterator = detallesDivisas.keySet().iterator();
                while(iterator.hasNext()) 
                {
                    String idDivisa1 = (String)iterator.next();
                    Divisa divisa1 = (Divisa)find(Divisa.class, idDivisa1);
                    double montoUSDRecibimos1 = 0.0D;
                    double montoUSDEntregamos = 0.0D;
                    Iterator it1 = ((List)detallesDivisas.get(idDivisa1)).iterator();
                    do
                    {
                        if(!it1.hasNext())
                        {
                            break;
                        }
                        DealDetalle det2 = (DealDetalle)it1.next();
                        if(!DealDetalle.STATUS_DET_CANCELADO.equals(det2.getStatusDetalleDeal()))
                        {
                            if(det2.isRecibimos())
                            {
                                montoUSDRecibimos1 += getMontoUSD(det2, divisa1);
                            } else
                            {
                                montoUSDEntregamos += getMontoUSD(det2, divisa1);
                            }
                        }
                    } while(true);
                    montoTotal += montoUSDEntregamos + montoUSDRecibimos1;
                    double montousuario = 0.0D;
                    ParametroSica usuarioParam = findParametro("LIM_DIV_DOC_US");
                    montousuario = Double.parseDouble(usuarioParam.getValor());
                    super.logger.info("--- validando monto total dolarizado: " + montoTotal);
                    super.logger.info("--- limite permitido para usuarios: " + montousuario);
                    if(montoTotal > montousuario)
                    {
                        super.logger.info("Se supera el monto limite se de bebe validar la documentaicon entregada");
                        try
                        {
                            String noCuenta = nuevoContratoSica.getNoCuenta();
                            Integer idPersona = nuevoCliente.getIdPersona();
                            boolean isPersonaFisica = (Constantes.TP_ACTIVIDAD_EMPRESARIAL.equals(nuevoCliente.getTipoPersona()) || Constantes.PERSONA_FISICA.equals(nuevoCliente.getTipoPersona()));
                            isValidoClienteSica(idPersona, isPersonaFisica, noCuenta);
                        }
                        catch(ContratacionException e)
                        {
                            String message = e.getMessage();
                            throw new SicaException("No se puede realizar la operaci\363n, por falta de documentaci\363n requerida en" +
" el M\363dulo de Contrataci\363n.\n"
+ message);
                        }
                        catch(Exception e)
                        {
                            throw new SicaException("Hubo un error en la obtenci\363n de los datos del cliente. Favor de contactar a " +
"sistemas."
);
                        }
                    }
                }
            }
        }

    } else
    {
        Map detallesDivisas = new HashMap();
        detallesDivisas.putAll(getDealService().getDetallesDivisas(deal));
        double montoTotal = 0.0D;
        Iterator iterator = detallesDivisas.keySet().iterator();
        do
        {
            if(!iterator.hasNext())
            {
                break;
            }
            String idDivisa = (String)iterator.next();
            Divisa divisa = (Divisa)find(com.ixe.ods.sica.model.Divisa.class, idDivisa);
            double montoUSDRecibimos = 0.0D;
            double montoUSDEntregamos = 0.0D;
            Iterator it = ((List)detallesDivisas.get(idDivisa)).iterator();
            do
            {
                if(!it.hasNext())
                {
                    break;
                }
                DealDetalle det = (DealDetalle)it.next();
                if(!DealDetalle.STATUS_DET_CANCELADO.equals(det.getStatusDetalleDeal()))
                {
                    if(det.isRecibimos())
                    {
                        montoUSDRecibimos += getMontoUSD(det, divisa);
                    } else
                    {
                        montoUSDEntregamos += getMontoUSD(det, divisa);
                    }
                }
            } while(true);
            montoTotal += montoUSDEntregamos + montoUSDRecibimos;
            super.logger.info("Monto total: " + montoTotal + "montoLimite: " + montoLimite);
            if(montoTotal > montoLimite)
            {
                super.logger.info("Se supera el monto limite, entramos a validar documentacion");
                try
                {
                    String noCuenta = nuevoContratoSica.getNoCuenta();
                    Integer idPersona = nuevoCliente.getIdPersona();
                    boolean isPersonaFisica = (Constantes.TP_ACTIVIDAD_EMPRESARIAL.equals(nuevoCliente.getTipoPersona()) || Constantes.PERSONA_FISICA.equals(nuevoCliente.getTipoPersona()));
                    isValidoClienteSica(idPersona, isPersonaFisica, noCuenta);
                }
                catch(ContratacionException e)
                {
                    String message = e.getMessage();
                    throw new SicaException("No se puede realizar la operaci\363n, por falta de documentaci\363n requerida en" +
" el M\363dulo de Contrataci\363n.\n"
+ message);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                    throw new SicaException("Hubo un error en la obtenci\363n de los datos del cliente. Favor de contactar a " +
"sistemas."
);
                }
            }
        } while(true);
    }
}

	/**
	 * RMC: Aqui se valida al cliente o al usuario para documentacion. el monto
	 * ya esta dolarizado
	 * 
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#validarClientePorMontoMayorATresMilUSD(Deal,
	 *      double).
	 */
    public void validarClientePorMontoMayorATresMilUSD(Deal deal, double montoNvoDetUSD)
    throws SicaException
{
    double montoLimite = getLimiteNormativoUSD(deal);
    boolean isCliente = isClienteOrUsuario(deal.getCliente().getIdPersona());
    double montousuario = 0.0D;
    ParametroSica usuarioParam = findParametro("LIM_DIV_DOC_US");
    montousuario = Double.parseDouble(usuarioParam.getValor());
    if(!isCliente)
    {
        String idDivisa = null;
        String idProducto = null;
        double montoUSDRecibimos = 0.0D;
        super.logger.info("******************* isCliente:  " + isCliente);
        super.logger.info("******************* Mensage de chuy entra a validar usuario ******************");
        super.logger.info("Monto nuevo detalle: " + montoNvoDetUSD + " montoLimite:" + montoLimite);
        for(Iterator it = deal.getDetalles().iterator(); it.hasNext();)
        {
            DealDetalle det = (DealDetalle)it.next();
            idDivisa = det.getDivisa().getIdDivisa();
            idProducto = det.getClaveFormaLiquidacion();
            if(idProducto == null)
            {
                idProducto = "";
            }
            boolean isRecibimos = det.isRecibimos();
            super.logger.info("************************ entro al for por detalle de divisa ********************" +
"**** "
);
            super.logger.info("Valor de idDivisa -----> " + idDivisa + " Valor de idProducto -----> " + idProducto + " isRecibimos -----> " + isRecibimos + " folio detalle -----> " + det.getFolioDetalle());
            if(!DealDetalle.STATUS_DET_CANCELADO.equals(det.getStatusDetalleDeal()) && idDivisa.equals(Divisa.DOLAR) && det.getClaveFormaLiquidacion().equals(Constantes.EFECTIVO) && isRecibimos)
            {
                montoUSDRecibimos += det.getMontoUSD();
            }
            super.logger.info("--- validando monto total de sumarizado de COMPRA de dolares:" + montoUSDRecibimos);
            if(montoUSDRecibimos > montoLimite)
            {
                super.logger.info("Se supera el monto limite se de bebe validar la documentaicon entregada");
                try
                {
                    String noCuenta = deal.getContratoSica().getNoCuenta();
                    Integer idPersona = deal.getCliente().getIdPersona();
                    boolean isPersonaFisica = (Constantes.TP_ACTIVIDAD_EMPRESARIAL.equals(deal.getCliente().getTipoPersona()) || Constantes.PERSONA_FISICA.equals(deal.getCliente().getTipoPersona()));
                    isValidoClienteSica(idPersona, isPersonaFisica, noCuenta);
                }
                catch(ContratacionException e)
                {
                    String message = e.getMessage();
                    throw new SicaException("No se puede realizar la operaci\363n, por falta de documentaci\363n requerida en" +
" el M\363dulo de Contrataci\363n.\n"
+ message);
                }
                catch(Exception e)
                {
                    throw new SicaException("Hubo un error en la obtenci\363n de los datos del cliente. Favor de contactar a " +
"sistemas."
);
                }
            }
            super.logger.info("************* Mensage de chuy entra a validar usuario para las demas divisas y d" +
"emas productos **************"
);
            super.logger.info("Monto nuevo detalle: " + montoNvoDetUSD + " montoLimite:" + montousuario);
            if(montoNvoDetUSD > montousuario)
            {
                super.logger.info("Se supera el monto limite se debe validar la documentaicon entregada");
                try
                {
                    String noCuenta = deal.getContratoSica().getNoCuenta();
                    Integer idPersona = deal.getCliente().getIdPersona();
                    boolean isPersonaFisica = (Constantes.TP_ACTIVIDAD_EMPRESARIAL.equals(deal.getCliente().getTipoPersona()) || Constantes.PERSONA_FISICA.equals(deal.getCliente().getTipoPersona()));
                    isValidoClienteSica(idPersona, isPersonaFisica, noCuenta);
                }
                catch(ContratacionException e)
                {
                    throw new SicaException("No se puede realizar la operaci\363n, por falta de documentaci\363n requerida en" +
" el M\363dulo de Contrataci\363n.\n"
+ e.getMessage());
                }
                catch(Exception e)
                {
                    throw new SicaException("Hubo un error en la obtenci\363n de los datos del cliente. Favor de contactar a " +
"sistemas."
);
                }
            } else
            {
                Map detallesDivisas = new HashMap();
                detallesDivisas.putAll(getDealService().getDetallesDivisas(deal));
                double montoTotal = 0.0D;
                Iterator iterator = detallesDivisas.keySet().iterator();
                while(iterator.hasNext()) 
                {
                    String idDivisa1 = (String)iterator.next();
                    Divisa divisa1 = (Divisa)find(Divisa.class, idDivisa1);
                    double montoUSDRecibimos1 = 0.0D;
                    double montoUSDEntregamos = 0.0D;
                    Iterator it1 = ((List)detallesDivisas.get(idDivisa1)).iterator();
                    do
                    {
                        if(!it1.hasNext())
                        {
                            break;
                        }
                        DealDetalle det2 = (DealDetalle)it1.next();
                        if(!DealDetalle.STATUS_DET_CANCELADO.equals(det2.getStatusDetalleDeal()))
                        {
                            if(det2.isRecibimos())
                            {
                                montoUSDRecibimos1 += getMontoUSD(det2, divisa1);
                            } else
                            {
                                montoUSDEntregamos += getMontoUSD(det2, divisa1);
                            }
                        }
                    } while(true);
                    montoTotal += montoUSDEntregamos + montoUSDRecibimos1;
                    super.logger.info("--- validando monto total:" + montoTotal);
                    if(montoTotal > montousuario)
                    {
                        super.logger.info("Se supera el monto limite se de bebe validar la documentaicon entregada");
                        try
                        {
                            String noCuenta = deal.getContratoSica().getNoCuenta();
                            Integer idPersona = deal.getCliente().getIdPersona();
                            boolean isPersonaFisica = (Constantes.TP_ACTIVIDAD_EMPRESARIAL.equals(deal.getCliente().getTipoPersona()) || Constantes.PERSONA_FISICA.equals(deal.getCliente().getTipoPersona()));
                            isValidoClienteSica(idPersona, isPersonaFisica, noCuenta);
                        }
                        catch(ContratacionException e)
                        {
                            String message = e.getMessage();
                            throw new SicaException("No se puede realizar la operaci\363n, por falta de documentaci\363n requerida en" +
" el M\363dulo de Contrataci\363n.\n"
+ message);
                        }
                        catch(Exception e)
                        {
                            throw new SicaException("Hubo un error en la obtenci\363n de los datos del cliente. Favor de contactar a " +
"sistemas."
);
                        }
                    }
                }
            }
        }

    } else
    {
        super.logger.info("******************* isCliente:  " + isCliente);
        super.logger.info("******************* Mensage de chuy entra a validar cliente ******************");
        super.logger.info("Monto nuevo detalle: " + montoNvoDetUSD + " montoLimite:" + montoLimite);
        if(montoNvoDetUSD > montoLimite)
        {
            super.logger.info("Se supera el monto limite se de bebe validar la documentaicon entregada");
            try
            {
                String noCuenta = deal.getContratoSica().getNoCuenta();
                Integer idPersona = deal.getCliente().getIdPersona();
                boolean isPersonaFisica = (Constantes.TP_ACTIVIDAD_EMPRESARIAL.equals(deal.getCliente().getTipoPersona()) || Constantes.PERSONA_FISICA.equals(deal.getCliente().getTipoPersona()));
                isValidoClienteSica(idPersona, isPersonaFisica, noCuenta);
            }
            catch(ContratacionException e)
            {
                throw new SicaException("No se puede realizar la operaci\363n, por falta de documentaci\363n requerida en" +
" el M\363dulo de Contrataci\363n.\n"
+ e.getMessage());
            }
            catch(Exception e)
            {
                throw new SicaException("Hubo un error en la obtenci\363n de los datos del cliente. Favor de contactar a " +
"sistemas."
);
            }
        } else
        {
            Map detallesDivisas = new HashMap();
            detallesDivisas.putAll(getDealService().getDetallesDivisas(deal));
            double montoTotal = 0.0D;
            Iterator iterator = detallesDivisas.keySet().iterator();
            do
            {
                if(!iterator.hasNext())
                {
                    break;
                }
                String idDivisa1 = (String)iterator.next();
                Divisa divisa1 = (Divisa)find(Divisa.class, idDivisa1);
                double montoUSDRecibimos = 0.0D;
                double montoUSDEntregamos = 0.0D;
                Iterator it = ((List)detallesDivisas.get(idDivisa1)).iterator();
                do
                {
                    if(!it.hasNext())
                    {
                        break;
                    }
                    DealDetalle det2 = (DealDetalle)it.next();
                    if(!DealDetalle.STATUS_DET_CANCELADO.equals(det2.getStatusDetalleDeal()))
                    {
                        if(det2.isRecibimos())
                        {
                            montoUSDRecibimos += getMontoUSD(det2, divisa1);
                        } else
                        {
                            montoUSDEntregamos += getMontoUSD(det2, divisa1);
                        }
                    }
                } while(true);
                montoTotal += montoUSDEntregamos + montoUSDRecibimos;
                super.logger.info("--- validando monto total:" + montoTotal);
                if(montoTotal > montoLimite)
                {
                    super.logger.info("Se supera el monto limite se de bebe validar la documentaicon entregada");
                    try
                    {
                        String noCuenta = deal.getContratoSica().getNoCuenta();
                        Integer idPersona = deal.getCliente().getIdPersona();
                        boolean isPersonaFisica = (Constantes.TP_ACTIVIDAD_EMPRESARIAL.equals(deal.getCliente().getTipoPersona()) || Constantes.PERSONA_FISICA.equals(deal.getCliente().getTipoPersona()));
                        isValidoClienteSica(idPersona, isPersonaFisica, noCuenta);
                    }
                    catch(ContratacionException e)
                    {
                        String message = e.getMessage();
                        throw new SicaException("No se puede realizar la operaci\363n, por falta de documentaci\363n requerida en" +
" el M\363dulo de Contrataci\363n.\n"
+ message);
                    }
                    catch(Exception e)
                    {
                        throw new SicaException("Hubo un error en la obtenci\363n de los datos del cliente. Favor de contactar a " +
"sistemas."
);
                    }
                }
            } while(true);
        }
    }
}


	/**
	 * regresa el limite normativo para validar el control documental
	 * 
	 * @param deal
	 * @return el monto asociado al cliente o usuario
	 */
	private double getLimiteNormativoUSD(Deal deal) {
		double montoUsuario = 300d;
		double montoCliente = 3000d;

		ParametroSica clienteParam = findParametro(LIM_USD_DOC_CL);
		ParametroSica usuarioParam = findParametro(LIM_USD_DOC_US);

		if (clienteParam == null || usuarioParam == null) {
			logger
					.error("No se encuentran los parametros de operacion, se trabaja con constantes");
			logger
					.error("No se encuentran los parametros de operacion LIM_USD_DOC_CL, LIM_USD_DOC_US");
		} else {

			montoCliente = Double.parseDouble(clienteParam.getValor());
			montoUsuario = Double.parseDouble(usuarioParam.getValor());
		}
		logger.debug("idPersona:" + deal.getCliente().getIdPersona());
		// Actualizacion del limite de control documental, si es cliete o
		// usuario.
		boolean isCliente = isClienteOrUsuario(deal.getCliente().getIdPersona());
		double montoLimite = montoCliente;
		if (!isCliente) {
			montoLimite = montoUsuario;
		}
		// if (logger.isDebugEnabled()) {
		logger.info("-----Limite de control documental:" + montoLimite
				+ " isCliente:" + isCliente);
		// }
		return montoLimite;
	}
	
	/**
	 * regresa el limite normativo para validar el control documental
	 * 
	 * @param nuevoCliente
	 * @return el monto asociado al cliente o usuario
	 */
	
	private double getLimiteNormativoUSD(Persona nuevoCliente)
    {
        double montoUsuario = 300D;
        double montoCliente = 3000D;
        ParametroSica clienteParam = findParametro("LIM_USD_DOC_CL");
        ParametroSica usuarioParam = findParametro("LIM_USD_DOC_US");
        if(clienteParam == null || usuarioParam == null)
        {
            super.logger.error("No se encuentran los parametros de operacion, se trabaja con constantes");
            super.logger.error("No se encuentran los parametros de operacion LIM_USD_DOC_CL, LIM_USD_DOC_US");
        } else
        {
            montoCliente = Double.parseDouble(clienteParam.getValor());
            montoUsuario = Double.parseDouble(usuarioParam.getValor());
        }
        super.logger.debug("idPersona:" + nuevoCliente.getIdPersona());
        boolean isCliente = isClienteOrUsuario(nuevoCliente.getIdPersona());
        double montoLimite = montoCliente;
        if(!isCliente)
        {
            montoLimite = montoUsuario;
        }
        super.logger.info("-----Limite de control documental:" + montoLimite + " isCliente:" + isCliente);
        return montoLimite;
    }


	/**
	 * @see 
	 *      com.ixe.ods.sica.sdo.SicaServiceData#validarDocumentacionClientes((List
	 *      mapaPersonasList)
	 */
	public List validarDocumentacionClientes(List mapaPersonasList) {

		try {
			SicaValidaClienteMontoTresMilUSDService bean = (SicaValidaClienteMontoTresMilUSDService) _appContext
					.getBean("sicaValidaClienteMontoTresMilUSDService");
			return bean.validarDocumentacionClientes(mapaPersonasList);

		} catch (ContratacionException e) {
			e.printStackTrace();
			SicaException se = new SicaException(""
					+ "Hubo un error en la obtenci\u00f3n de los datos "
					+ "del cliente. Favor de contactar a sistemas."
					+ e.getMessage());
			se.setStackTrace(e.getStackTrace());
			throw se;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SicaException(
					"Hubo un error en la obtenci\u00f3n de los datos "
							+ "del cliente. Favor de contactar a sistemas."
							+ e.getMessage());
		}
	}

	/**
	 * @see SicaServiceData#isValidoClienteSica(Integer, boolean, String)
	 */
	public void isValidoClienteSica(Integer idPersona, boolean isPersonaFisica,
			String noCuenta) throws ContratacionException {
		String informacionFaltanteBup = "";
		String documentacionFaltante = "";
		boolean isVencidaFechaCompromiso = false;

		try {
			SicaConsultaDocumentacionService docBean = (SicaConsultaDocumentacionService) _appContext
					.getBean("SicaConsultaDocumentacionService");

			SicaValidaClienteMontoTresMilUSDService validaBean = (SicaValidaClienteMontoTresMilUSDService) _appContext
					.getBean("sicaValidaClienteMontoTresMilUSDService");

			isVencidaFechaCompromiso = docBean
					.isVencidaFechaEntregaDocumentacionCAM10(noCuenta);

			if (isVencidaFechaCompromiso) {
				documentacionFaltante = validaBean
						.obtenDocumentacionFaltanteCliente(
								idPersona.intValue(), noCuenta);
			}

			informacionFaltanteBup = validaBean.validaClienteSica(idPersona,
					isPersonaFisica);

		} catch (ContratacionException e) {
			e.printStackTrace();
			SicaException se = new SicaException(
					"Hubo un error en la obtenci\u00f3n de los datos "
							+ "del cliente. Favor de contactar a sistemas."
							+ e.getMessage());
			se.setStackTrace(e.getStackTrace());
			throw se;
		} catch (RemoteException e) {
			e.printStackTrace();
			SicaException se = new SicaException(
					"Hubo un error en la obtenci\u00f3n de los datos "
							+ "del cliente. Favor de contactar a sistemas."
							+ e.getMessage());
			se.setStackTrace(e.getStackTrace());
			throw se;
		}

		String error = informacionFaltanteBup + documentacionFaltante;
		if (error.length() > 0 && isVencidaFechaCompromiso) {
			throw new ContratacionException(error);
		}
	}

	/**
	 * @see SicaServiceData#validaInformacionBUPcliente(Integer, boolean)
	 */
	public String validaInformacionBUPcliente(Integer idPersona,
			boolean isPersonaFisica) throws SicaException {
		String informacionFaltanteBup = null;
		try {
			SicaValidaClienteMontoTresMilUSDService validaBean = (SicaValidaClienteMontoTresMilUSDService) _appContext
					.getBean("sicaValidaClienteMontoTresMilUSDService");

			informacionFaltanteBup = validaBean.validaClienteSica(idPersona,
					isPersonaFisica);
		} catch (ContratacionException e) {
			e.printStackTrace();
			SicaException se = new SicaException(
					"Hubo un error en la obtenci\u00f3n de los datos "
							+ "del cliente. Favor de contactar a sistemas."
							+ e.getMessage());
			se.setStackTrace(e.getStackTrace());
			throw se;
		} catch (BeansException e) {
			e.printStackTrace();
			SicaException se = new SicaException(
					"Hubo un error en la obtenci\u00f3n de los datos "
							+ "del cliente. Favor de contactar a sistemas."
							+ e.getMessage());
			se.setStackTrace(e.getStackTrace());
			throw se;
		} catch (RemoteException e) {
			e.printStackTrace();
			SicaException se = new SicaException(
					"Hubo un error en la obtenci\u00f3n de los datos "
							+ "del cliente. Favor de contactar a sistemas."
							+ e.getMessage());
			se.setStackTrace(e.getStackTrace());
			throw se;
		}
		return informacionFaltanteBup;
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#isValidoClienteSica(Persona)
	 */
	public Map fechaEntregaDocumentacionCAM10(String noCuenta)
			throws ContratacionException {
		try {
			SicaConsultaDocumentacionService bean = (SicaConsultaDocumentacionService) _appContext
					.getBean("SicaConsultaDocumentacionService");

			return bean.fechaEntregaDocumentacionCAM10(noCuenta);
		} catch (ContratacionException e) {
			e.printStackTrace();
			throw new SicaException(
					"Hubo un error en la obtenci\u00f3n de los datos "
							+ "del cliente. Favor de contactar a sistemas."
							+ e.getMessage());
		} catch (RemoteException e) {
			e.printStackTrace();
			throw new SicaException(
					"Hubo un error en la obtenci\u00f3n de los datos "
							+ "del cliente. Favor de contactar a sistemas."
							+ e.getMessage());
		}
	}

	/**
	 * Permite obtener el monto dolarizado de un detalle de deal sin importar la
	 * divisa.
	 * 
	 * @param det
	 *            El detalle de deal.
	 * @param divisa
	 *            La divisa del detalle de deal.
	 * @return double El monto dolarizado del detalle de deal.
	 */
	private double getMontoUSD(DealDetalle det, Divisa divisa) {
		if (divisa.isFrecuente()) {
			return det.getMontoUSD();
		} else {
			return det.getMonto()
					/ findPrecioReferenciaActual().getPreRef().getMidSpot()
							.doubleValue();
		}
	}

	/**
	 * Verifica si existe el registro en Posicion detalle, si no, lo inserta.
	 * 
	 * @param posicionLog
	 *            El registro de PosicionLog a revisar.
	 */
	private void verificarPosicionDetalle(PosicionLog posicionLog) {
		List posicionDetalle = getHibernateTemplate()
				.find(
						"FROM PosicionDetalle as pd WHERE pd.divisa.idDivisa = ? AND pd.mesaCambio.idMesaCambio = ? AND "
								+ "pd.canal.idCanal = ? AND pd.claveFormaLiquidacion = ?",
						new Object[] {
								posicionLog.getDivisa().getIdDivisa(),
								new Integer(posicionLog.getCanalMesa()
										.getMesaCambio().getIdMesaCambio()),
								posicionLog.getCanalMesa().getCanal()
										.getIdCanal(),
								posicionLog.getClaveFormaLiquidacion() });
		if (posicionDetalle.size() == 0) {
			// encontrar registro de Posicion
			List posicion = getHibernateTemplate()
					.find(
							"FROM Posicion AS p WHERE p.mesaCambio.idMesaCambio = ? AND p.divisa.idDivisa = ?",
							new Object[] {
									new Integer(posicionLog.getCanalMesa()
											.getMesaCambio().getIdMesaCambio()),
									posicionLog.getDivisa().getIdDivisa() });
			PosicionDetalle pd = new PosicionDetalle(posicionLog.getCanalMesa()
					.getCanal(), posicionLog.getDivisa(), posicionLog
					.getCanalMesa().getMesaCambio(), posicionLog
					.getClaveFormaLiquidacion(), (Posicion) posicion.get(0));
			store(pd);
		}
	}

	/**
	 * Calcula el monto del reconocimiento de utilidades.
	 * 
	 * @param pos
	 *            La posicion final
	 * @return double.
	 */
	private double calculaMontoReconocimientoUtilidadesAut(Posicion pos) {
		// Calculos en Divisa
		double compras = pos.getCpaVta().getCompraCash()
				+ pos.getCpaVta().getCompraTom()
				+ pos.getCpaVta().getCompraSpot()
				+ pos.getCpaVta().getCompra72Hr().doubleValue()
				+ pos.getCpaVta().getCompraVFut().doubleValue();
		double ventas = pos.getCpaVta().getVentaCash()
				+ pos.getCpaVta().getVentaTom()
				+ pos.getCpaVta().getVentaSpot()
				+ pos.getCpaVta().getVenta72Hr().doubleValue()
				+ pos.getCpaVta().getVentaVFut().doubleValue();
		// Calculos en MN
		double comprasMN = pos.getCpaVtaMn().getCompraMnClienteCash()
				+ pos.getCpaVtaMn().getCompraMnClienteTom()
				+ pos.getCpaVtaMn().getCompraMnClienteSpot()
				+ pos.getCpaVtaMn().getCompraMnCliente72Hr().doubleValue()
				+ pos.getCpaVtaMn().getCompraMnClienteVFut().doubleValue();
		double ventasMN = pos.getCpaVtaMn().getVentaMnClienteCash()
				+ pos.getCpaVtaMn().getVentaMnClienteTom()
				+ pos.getCpaVtaMn().getVentaMnClienteSpot()
				+ pos.getCpaVtaMn().getVentaMnCliente72Hr().doubleValue()
				+ pos.getCpaVtaMn().getVentaMnClienteVFut().doubleValue();
		// Calculando los Tipos de Cambio
		if (pos.getPosIni().getPosicionInicial() > 0) {
			compras = compras + pos.getPosIni().getPosicionInicial();
			comprasMN = comprasMN + pos.getPosIni().getPosicionInicialMn();
		} else {
			ventas = ventas - pos.getPosIni().getPosicionInicial();
			ventasMN = ventasMN - pos.getPosIni().getPosicionInicialMn();
		}
		double tipoCambioPromedioCompras = 0.0;
		if (compras != 0.0) {
			tipoCambioPromedioCompras = comprasMN / compras;
		}
		double tipoCambioPromedioVentas = 0.0;
		if (ventas != 0.0) {
			tipoCambioPromedioVentas = ventasMN / ventas;
		}
		double mul = ventas < compras ? ventas : compras;
		return (tipoCambioPromedioVentas - tipoCambioPromedioCompras) * mul;
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#getIdAdministrador().
	 */
	public Integer getIdAdministrador() {
		ParametroSica idUsuario = (ParametroSica) find(ParametroSica.class,
				ParametroSica.ADMIN_ID_USUARIO);
		return new Integer(idUsuario.getValor());
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#getParsLimDesv().
	 */
	public Map getParsLimDesv() {
		Map map = new HashMap();
		map.put("DESV_MONTO_LIM", Double.valueOf(findParametro(
				ParametroSica.DESV_MONTO_LIM).getValor()));
		map.put("DESV_PORC_LIM", Double.valueOf(findParametro(
				ParametroSica.DESV_PORC_LIM).getValor()));
		map.put("DESV_FACT_1", Double.valueOf(findParametro(
				ParametroSica.DESV_FACT_1).getValor()));
		map.put("DESV_FACT_2", Double.valueOf(findParametro(
				ParametroSica.DESV_FACT_2).getValor()));
		map.put("DESV_PORC_MAX", Double.valueOf(findParametro(
				ParametroSica.DESV_PORC_MAX).getValor()));
		return map;
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#inicializarPlantilla(com.ixe.ods.sica.model.IPlantilla)
	 *      .
	 */
	public void inicializarPlantilla(IPlantilla plantilla) {
		try {
			getHibernateTemplate().refresh(plantilla);
			if (!Hibernate.isInitialized(plantilla)) {
				Hibernate.initialize(plantilla);
			}
			if (plantilla instanceof IPlantillaNacional) {
				IPlantillaNacional plt = (IPlantillaNacional) plantilla;
				if (!Hibernate.isInitialized(plt.getBeneficiario())) {
					Hibernate.initialize(plt.getBeneficiario());
				}
			} else if (plantilla instanceof IPlantillaIntl) {
				IPlantillaIntl plt = (IPlantillaIntl) plantilla;
				if (!Hibernate.isInitialized(plt.getBeneficiario())) {
					Hibernate.initialize(plt.getBeneficiario());
				}
				if (!Hibernate.isInitialized(plt.getDivisa())) {
					Hibernate.initialize(plt.getDivisa());
				}
			} else if (plantilla instanceof IPlantillaTranNacional) {
				IPlantillaTranNacional plt = (IPlantillaTranNacional) plantilla;
				if (!Hibernate.isInitialized(plt.getDivisa())) {
					Hibernate.initialize(plt.getDivisa());
				}
			}
			else if(plantilla instanceof IPlantillaCuentaIxe)
			{
				IPlantillaCuentaIxe plt = (IPlantillaCuentaIxe)plantilla;
				//if(!Hibernate.isInitialized(plt.getBeneficiario()))
					Hibernate.initialize(plt.getBeneficiario());
			}
		} catch (HibernateException e) {
			throw SessionFactoryUtils.convertHibernateAccessException(e);
		}
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#insertarEntregamosDealIntConPlantilla(com.ixe.ods.sica.model.Deal,
	 *      double, com.ixe.ods.sica.model.IPlantilla, boolean, double).
	 */
	public DealDetalle insertarEntregamosDealIntConPlantilla(Deal deal,
			double importe, IPlantilla plantilla, boolean cobertura,
			double tcCobertura) {
		Divisa divisa = cobertura ? findDivisa(Divisa.DOLAR) : deal
				.getCanalMesa().getMesaCambio().getDivisaReferencia();
		double tc = cobertura ? tcCobertura : 1.0;
		String cfl = cobertura ? Constantes.TRANSFERENCIA : null;
		// Inserto el registro de recibimos:
		DealDetalle detCtpt = new DealDetalle(deal, false, cfl, divisa,
				importe, tc, DealHelper.getSiguienteFolioDetalle(deal));
		// Si es Trader USD, se le asigna el idPrecioReferencia al detalle
		// insertado
		if (Divisa.DOLAR.equals(deal.getCanalMesa().getMesaCambio()
				.getDivisaReferencia().getIdDivisa())) {
			PrecioReferenciaActual pr = findPrecioReferenciaActual();
			detCtpt.setPrecioReferenciaMidSpot(pr.getPreRef().getMidSpot());
			detCtpt.setPrecioReferenciaSpot(pr.getPreRef().getPrecioSpot());
		}
		if (cobertura && detCtpt.getFactorDivisa() == null) {
			detCtpt.setFactorDivisa(new Double(findFactorDivisa(
					findFactorDivisaActualFromTo(Divisa.DOLAR, Divisa.DOLAR)
							.getIdFactorDivisa()).getFacDiv().getFactor()));
		}
		detCtpt.setDeal(deal);
		return detCtpt;
	}

	/**
	 * <p>
	 * Itera todos los detalles del deal y llama a marcarDealDetalleCancelado().
	 * Si el deal es interbancario, y tiene un swap asignado:
	 * </p>
	 * <ul>
	 * <li>Es inicio del swap.- Se marca como cancelado el swap.</li>
	 * <li>Es contraparte del swap.- Actualiza el registro relacionado de
	 * BitacoraEnviadas. Se decrementa el monto asignado y si este se vuelve
	 * cero, pone en <i>IN</i> el status del swap, en caso contrario, pone el
	 * status en <i>PA</i>.</li>
	 * </ul>
	 * 
	 * @param ticket
	 *            El ticket de la sesi&oacute;n del usuario.
	 * @param idDeal
	 *            El deal a cancelar.
	 * @see #marcarDealDetalleCancelado(String,
	 *      com.ixe.ods.sica.model.DealDetalle, boolean, boolean).
	 */
	public void marcarDealCancelado(String ticket, int idDeal) {
		Deal deal = (Deal) find(Deal.class, new Integer(idDeal));
		for (Iterator it = deal.getDetalles().iterator(); it.hasNext();) {
			marcarDealDetalleCancelado(ticket, (DealDetalle) it.next(), true,
					true);
		}
		deal.setStatusDeal(Deal.STATUS_DEAL_CANCELADO);
		update(deal);
		// Revisando si el Deal se reporto a Banxico para compras y ventas:
		getBitacoraEnviadasDao().revisarReportadoBanxico(deal, null);
	}

	/**
	 * @param det
	 *            El detalle a marcar como cancelado.
	 * @see com.ixe.ods.sica.sdo.WorkFlowServiceData#marcarDealDetalleCancelado(String,
	 *      com.ixe.ods.sica.model.DealDetalle, boolean, boolean).
	 */
	public void marcarDealDetalleCancelado(String ticket, DealDetalle det,
			boolean liberarLinea, boolean liberarRemesas) {
		
		if (!DealDetalle.STATUS_DET_CANCELADO
				.equals(det.getStatusDetalleDeal())
				&& Deal.EV_POSICION
						.equals(det
								.decodificarEvento(DealDetalle.EV_IND_GRAL_AFECTADA_POSICION))) {
			String fechaValor = ((PizarronServiceData) _appContext
					.getBean("pizarronServiceData")).fechaValorParaCancelacion(
					det.getDeal().getFechaCaptura(), det.getDeal()
							.getTipoValor(), false);
			PosicionLog pos = new PosicionLog(det, det
					.getStatusPosLogParaCancelacion(),
					findPrecioReferenciaActual().getPreRef().getPrecioSpot()
							.doubleValue(), fechaValor);
			store(pos);
			
			if (liberarLinea) {
		        	getLineaCambioServiceData().liberarLineaCreditoDealDetalle(ticket, det);
			}
		}
		
		String statusAnterior = det.getStatusDetalleDeal();
		det.setStatusDetalleDeal(Deal.STATUS_DEAL_CANCELADO);
		update(det);
		
		validarInformacionRegulatoria(det.getDeal(), det.getDivisa(), det, statusAnterior);
		getH2hService().solicitarCancelacionDetalleH2H(det, H2HService.NO_ES_REVERSO);
	}
	

	
	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#montoMaximoExcedido(int,
	 *      Divisa, double).
	 */
	public boolean montoMaximoExcedido(int idMesaCambio, Divisa divisa,
			double monto) {
		if (divisa.isFrecuente() && MesaCambio.OPERACION == idMesaCambio) {
			MaximoDeal md = findMaximoDealByIdMesaCambioAndIdDivisa(
					idMesaCambio, divisa.getIdDivisa());
			if (md != null) {
				return monto > md.getMontoMaximo();
			}
		}
		return false;
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#reasignarCuentaEjecutivo(com.ixe.ods.bup.model.CuentaEjecutivo,
	 *      com.ixe.ods.bup.model.CuentaEjecutivo).
	 */
	public void reasignarCuentaEjecutivo(CuentaEjecutivo cta,
			CuentaEjecutivo nvaCta) {
		delete(cta);
		store(nvaCta);
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#reasignarCuentaEjecutivo(com.ixe.ods.bup.model.CuentaEjecutivo,
	 *      com.ixe.ods.bup.model.CuentaEjecutivo).
	 */
	public void desAsignarCuentaEjecutivo(CuentaEjecutivo cuentaEjecutivo) {
		update(cuentaEjecutivo);
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#registrarCambioTCM(com.ixe.ods.sica.model.Deal)
	 *      .
	 */
	public void registrarCambioTCM(Deal deal) {
		for (Iterator it = deal.getDetalles().iterator(); it.hasNext();) {
			DealDetalle det = (DealDetalle) it.next();
			if (det.getTmpTcc() != det.getTipoCambioMesa()) {
				registrarCambioTCMDealDet(det, det.getTmpTcc());
			}
		}
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#registrarCambioTCMDealDet(com.ixe.ods.sica.model.DealDetalle,
	 *      double).
	 */
	public void registrarCambioTCMDealDet(DealDetalle dealDet, double tcm) {
		if (Deal.EV_POSICION.equals(dealDet
				.decodificarEvento(DealDetalle.EV_IND_GRAL_AFECTADA_POSICION))) {
			String fechaValor = ((PizarronServiceData) _appContext
					.getBean("pizarronServiceData")).fechaValorParaCancelacion(
					dealDet.getDeal().getFechaCaptura(), dealDet.getDeal()
							.getTipoValor(), false);
			PosicionLog posLog = new PosicionLog(dealDet, dealDet
					.getStatusPosLogParaCancelacion(), dealDet
					.getPrecioReferenciaSpot().doubleValue(), fechaValor);
			store(posLog);
			dealDet.setTipoCambioMesa(tcm);
			posLog = new PosicionLog(dealDet,
					dealDet.isRecibimos() ? PosicionLog.COMPRA
							: PosicionLog.VENTA, dealDet
							.getPrecioReferenciaSpot().doubleValue(),
					fechaValor);
			store(posLog);
		} else {
			dealDet.setTipoCambioMesa(tcm);
		}
		update(dealDet);
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#registrarCambioTCMDealDet(int,
	 *      double).
	 */
	public void registrarCambioTCMDealDet(int idDealDet, double tcm) {
		registrarCambioTCMDealDet(findDealDetalle(idDealDet), tcm);
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#revisarLineaOperacionYActualizarDeal(Deal,IUsuario)
	 */
	public void revisarLineaOperacionYActualizarDeal(Deal deal, IUsuario usuario) {
		storeDeal(deal);
		deal = findDeal(deal.getIdDeal());
		LineaOperacion lineaOp = findLineaOperacionByBroker(deal.getBroker()
				.getId().getPersonaMoral().getIdPersona().intValue());
		ParametroSica par = findParametro(ParametroSica.ID_PERSONA_IXE_FORWARD);
		int idPersonaIxeForward = Integer.valueOf(par.getValor()).intValue();
		if (deal.getCliente().getIdPersona().intValue() != idPersonaIxeForward) {
			if (lineaOp == null) {
				deal.setEvento(Deal.EV_SOLICITUD,
						Deal.EV_IND_INT_LINEA_OPERACION);
				update(deal);
			} else {
				double excedente = usoLiberacionLineaOperacion(lineaOp,
						LineaOperacionLog.TIPO_OPER_USO, (DealDetalle) (deal
								.isCompra() ? deal.getRecibimos().get(0) : deal
								.getEntregamos().get(0)), usuario, false);
				if (excedente > 0.0) {
					deal.setEvento(Deal.EV_SOLICITUD,
							Deal.EV_IND_INT_LINEA_OPERACION);
					update(deal);
				}
			}
		}
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#storeDeal(com.ixe.ods.sica.model.Deal)
	 *      .
	 */
	public void storeDeal(Deal deal) {
		boolean insertarDeal = deal.getIdDeal() == 0;
		ContratoSica cs = deal.getContratoSica();
		if (insertarDeal) {
			deal.setEvento(Deal.EV_NO_DETERMINADO, Deal.EV_IND_GRAL_BANXICO_C);
			deal.setEvento(Deal.EV_NO_DETERMINADO, Deal.EV_IND_GRAL_BANXICO_V);
			store(deal);
		} else {
			update(deal);
		}
		for (Iterator it = deal.getDetalles().iterator(); it.hasNext();) {
			DealDetalle det = (DealDetalle) it.next();
			boolean insertarDet = det.getIdDealPosicion() == 0;
			if (insertarDet) {
				store(det);
			} else {
				update(det);
			}
			// SOLO SI NO hay pendiente autorizacion por horario o por monto, o
			// el deal esta
			// pendiente (Teller), no se afecta la posicion
			if (insertarDet && det.getClaveFormaLiquidacion() != null
					&& !deal.isPendiente()) {
				if (deal.isInterbancario()
						&& det.isEvento(DealDetalle.EV_IND_INT_MODIFICACION,
								new String[] { Deal.EV_NORMAL,
										Deal.EV_APROBACION })) {
					if (!det.isEvento(
							DealDetalle.EV_IND_GRAL_AFECTADA_POSICION,
							new String[] { Deal.EV_NEGACION })) {
						afectarPosicion(det);
					}
				} else if (!deal.isInterbancario()) {
					if (!Deal.EV_SOLICITUD.equals(det
							.decodificarEvento(DealDetalle.EV_IND_MONTO))
							&& !Deal.EV_SOLICITUD
									.equals(det
											.decodificarEvento(DealDetalle.EV_IND_HORARIO))
							&& det.isEvento(DealDetalle.EV_IND_MODIFICACION,
									new String[] { Deal.EV_NORMAL,
											Deal.EV_APROBACION })) {
						if (!det.isEvento(
								DealDetalle.EV_IND_GRAL_AFECTADA_POSICION,
								new String[] { Deal.EV_NEGACION })) {
							afectarPosicion(det);
						}
					}
					// Se inserta la solicitud de autorizacion de horario o
					// monto, si es que es
					// necesario.
					if (Deal.EV_SOLICITUD.equals(det
							.decodificarEvento(DealDetalle.EV_IND_HORARIO))) {
						wfCrearActividad(Actividad.PROC_HORARIO_MONTO,
								Actividad.ACT_DN_HORARIO, deal, det);
					} else if (Deal.EV_SOLICITUD.equals(det
							.decodificarEvento(DealDetalle.EV_IND_MONTO))) {
						wfCrearActividad(Actividad.PROC_HORARIO_MONTO,
								Actividad.ACT_DN_MONTO, deal, det);
					}
				}
			}
		}
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#storeFactoresDivisa(java.util.List)
	 *      .
	 */
	public void storeFactoresDivisa(List factoresDivisa) {
		for (Iterator it = factoresDivisa.iterator(); it.hasNext();) {
			FactorDivisaActual fdOriginal = (FactorDivisaActual) it.next();
			if (fdOriginal.isModificado()) {
				fdOriginal.setModificado(false);
				FactorDivisaActual fd = new FactorDivisaActual(fdOriginal);
				getHibernateTemplate()
						.delete(
								"from com.ixe.ods.sica.model.FactorDivisaActual fda where fda.facDiv.fromDivisa.idDivisa = ? and fda.facDiv.toDivisa.idDivisa = ?",
								new Object[] {
										fd.getFacDiv().getFromDivisa()
												.getIdDivisa(),
										fd.getFacDiv().getToDivisa()
												.getIdDivisa() },
								new Type[] { new StringType(), new StringType() });
				store(fd);
			}
		}
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#storePlantillaParaDealDetalle(com.ixe.ods.sica.model.DealDetalle,
	 *      com.ixe.ods.sica.model.IPlantilla).
	 */
	public void storePlantillaParaDealDetalle(DealDetalle det,
			IPlantilla plantilla) {
		store(plantilla);
		update(det);
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#storePrecioReferencia(com.ixe.ods.sica.model.PrecioReferenciaActual)
	 *      .
	 */
	public void storePrecioReferencia(PrecioReferenciaActual pr) {
		pr.getPreRef().setUltimaModificacion(new Date());
		getHibernateTemplate().delete(
				"from com.ixe.ods.sica.model.PrecioReferenciaActual");
		store(pr);
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findReconocimientos().
	 */
	public List findReconocimientos() {
		return getHibernateTemplate().findByNamedQuery("findReconocimientos",
				DateUtils.inicioDia());
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findAllDealsBySectorAndDate(java.util.Date,
	 *      java.util.Date).
	 */
	public List findReconocimientoByDivisaAndDate(Date gcInicio, Date gcFin,
			String idDivisa, int idMesaCambio) {
		return getHibernateTemplate().findByNamedQuery(
				"findUtilidadGlobales",
				new Object[] { gcInicio, gcFin, idDivisa,
						new Integer(idMesaCambio) });
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findReconocimientoByMesaAndDivisa(int,
	 *      String).
	 */
	public RecoUtilidad findReconocimientoByMesaAndDivisa(int idMesaCambio,
			String idDivisa) {
		List list = getHibernateTemplate().findByNamedQuery(
				"findReconocimientoByMesaAndDivisa",
				new Object[] { DateUtils.inicioDia(),
						new Integer(idMesaCambio), idDivisa });
		return list.size() > 0 ? (RecoUtilidad) list.get(0) : null;
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findRemesasPendientes(java.util.Date)
	 */
	public List findRemesasPendientes(Date fechaCaptura) {
		return getHibernateTemplate()
				.find(
						"FROM Actividad AS a INNER JOIN FETCH a.dealDetalle "
								+ "AS det INNER JOIN FETCH a.deal AS deal LEFT JOIN FETCH det.plantilla INNER JOIN "
								+ "FETCH det.divisa WHERE a.nombreActividad = ? AND a.status = ? AND "
								+ "det.statusDetalleDeal = ? AND det.reversado = ? AND a.deal.fechaCaptura BETWEEN "
								+ "? AND ? ORDER BY deal.fechaLiquidacion",
						new Object[] { Actividad.ACT_TES_REMESA,
								Actividad.STATUS_PENDIENTE,
								DealDetalle.STATUS_DET_TOTALMENTE_PAG_LIQ,
								new Integer(Deal.NO_REVERSADO),
								DateUtils.inicioDia(fechaCaptura),
								DateUtils.finDia(fechaCaptura) });
	}

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
	public void calcularComisionMxn(DealDetalle det, double precioReferenciaSpot) {
		if (det.getComisionCobradaUsd() > 0.0) {
			Double fd = det.getFactorDivisa();
			Canal c = det.getDeal().getCanalMesa().getCanal();
			TipoIva ti = (TipoIva) find(TipoIva.class, c.getTipoIva()
					.getClaveTipoIva());
			double porcIva = ti.getPorcIva() / 100.0;
			if (det.isDolar()) {
				double comision = det.getComisionCobradaUsd()
						* det.getTipoCambio();
				det.setComisionCobradaMxn(Redondeador.redondear2Dec(comision
						* (1 + porcIva)));
			} else {
				if (fd != null) {
					if (det.getDivisa().isFrecuente() && !det.isPesos()) {
						double comision = det.getComisionCobradaUsd()
								* det.getTipoCambio() / fd.doubleValue();
						det.setComisionCobradaMxn(Redondeador
								.redondear2Dec(comision * (1 + porcIva)));
					}
				}
				if (det.getComisionCobradaMxn() == 0) {
					double comision = det.getComisionCobradaUsd()
							* precioReferenciaSpot;
					det.setComisionCobradaMxn(Redondeador
							.redondear2Dec(comision * (1 + porcIva)));
				}
			}
		} else {
			det.setComisionCobradaMxn(0.0);
		}
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#calculaMontoReconocimientoUtilidades(com.ixe.ods.sica.model.Posicion,
	 *      double).
	 */
	public double calculaMontoReconocimientoUtilidades(Posicion pos,
			double tcAmaneceMesa) {
		// Calculos en Divisa
		double compras = pos.getCpaVta().getCompraCash()
				+ pos.getCpaVta().getCompraTom()
				+ pos.getCpaVta().getCompraSpot()
				+ pos.getCpaVta().getCompra72Hr().doubleValue()
				+ pos.getCpaVta().getCompraVFut().doubleValue();
		double ventas = pos.getCpaVta().getVentaCash()
				+ pos.getCpaVta().getVentaTom()
				+ pos.getCpaVta().getVentaSpot()
				+ pos.getCpaVta().getVenta72Hr().doubleValue()
				+ pos.getCpaVta().getVentaVFut().doubleValue();
		double posicionFinal = pos.getPosIni().getPosicionInicial() + compras
				- ventas;
		// Calculos en MN
		double comprasMN = pos.getCpaVtaMn().getCompraMnClienteCash()
				+ pos.getCpaVtaMn().getCompraMnClienteTom()
				+ pos.getCpaVtaMn().getCompraMnClienteSpot()
				+ pos.getCpaVtaMn().getCompraMnCliente72Hr().doubleValue()
				+ pos.getCpaVtaMn().getCompraMnClienteVFut().doubleValue();
		double ventasMN = pos.getCpaVtaMn().getVentaMnClienteCash()
				+ pos.getCpaVtaMn().getVentaMnClienteTom()
				+ pos.getCpaVtaMn().getVentaMnClienteSpot()
				+ pos.getCpaVtaMn().getVentaMnCliente72Hr().doubleValue()
				+ pos.getCpaVtaMn().getVentaMnClienteVFut().doubleValue();
		if (pos.getPosIni().getPosicionInicial() > 0) {
			compras = compras + pos.getPosIni().getPosicionInicial();
			comprasMN = comprasMN + pos.getPosIni().getPosicionInicialMn();
		} else {
			ventas = ventas - pos.getPosIni().getPosicionInicial();
			ventasMN = ventasMN - pos.getPosIni().getPosicionInicialMn();
		}
		// Calculando los Tipos de Cambio
		double tipoCambioCompras = 0.0;
		if (compras != 0.0) {
			tipoCambioCompras = comprasMN / compras;
		}
		double tipoCambioVentas = 0.0;
		if (ventas != 0.0) {
			tipoCambioVentas = ventasMN / ventas;
		}
		// Calculando el Tipo Cambio Posicion Final MN y la Posicion Final MN
		double tipoCambioPosicionFinal;
		if (posicionFinal < 0) {
			tipoCambioPosicionFinal = tipoCambioVentas;
		} else {
			tipoCambioPosicionFinal = tipoCambioCompras;
		}
		// Calculando los valores resultado de la Utilidad
		double tipoCambioUtilidad = tcAmaneceMesa - tipoCambioPosicionFinal;
		double utilidad = posicionFinal * tipoCambioUtilidad;
		// Sumando la Utilidad Acumulada
		if (pos.getPosIni().getPosicionInicial() == 0 && compras == 0
				&& ventas == 0) {
			utilidad -= pos.getPosIni().getPosicionInicialMn();
		}
		return Redondeador.redondear2Dec(utilidad);
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#calculaMontoUtilidadesNeteo(com.ixe.ods.sica.model.Posicion)
	 *      .
	 */
	public double calculaMontoUtilidadesNeteo(Posicion pos) {
		// Calculos en Divisa.
		double compras = pos.getCpaVta().getCompraCash()
				+ pos.getCpaVta().getCompraTom()
				+ pos.getCpaVta().getCompraSpot()
				+ pos.getCpaVta().getCompra72Hr().doubleValue()
				+ pos.getCpaVta().getCompraVFut().doubleValue();
		double ventas = pos.getCpaVta().getVentaCash()
				+ pos.getCpaVta().getVentaTom()
				+ pos.getCpaVta().getVentaSpot()
				+ pos.getCpaVta().getVenta72Hr().doubleValue()
				+ pos.getCpaVta().getVentaVFut().doubleValue();
		double posicionFinal = pos.getPosIni().getPosicionInicial() + compras
				- ventas;
		// Calculos en MN
		double comprasMN = pos.getCpaVtaMn().getCompraMnClienteCash()
				+ pos.getCpaVtaMn().getCompraMnClienteTom()
				+ pos.getCpaVtaMn().getCompraMnClienteSpot()
				+ pos.getCpaVtaMn().getCompraMnCliente72Hr().doubleValue()
				+ pos.getCpaVtaMn().getCompraMnClienteVFut().doubleValue();
		double ventasMN = pos.getCpaVtaMn().getVentaMnClienteCash()
				+ pos.getCpaVtaMn().getVentaMnClienteTom()
				+ pos.getCpaVtaMn().getVentaMnClienteSpot()
				+ pos.getCpaVtaMn().getVentaMnCliente72Hr().doubleValue()
				+ pos.getCpaVtaMn().getVentaMnClienteVFut().doubleValue();
		// Calculando los Tipos de Cambio
		double aux1 = pos.getPosIni().getPosicionInicialMn() + comprasMN;
		double aux2 = pos.getPosIni().getPosicionInicial() + compras;
		double tipoCambioCompras = 0.0;
		if (aux2 != 0) {
			tipoCambioCompras = aux1 / aux2;
		}
		double tipoCambioVentas = 0.0;
		if (ventas != 0) {
			tipoCambioVentas = ventasMN / ventas;
		}
		// Calculando el Tipo Cambio Posicion Final MN y la Posicion Final MN
		double montoUtilidadNeteo;
		if (posicionFinal < 0) {
			montoUtilidadNeteo = (tipoCambioVentas - tipoCambioCompras)
					* compras;
		} else {
			montoUtilidadNeteo = (tipoCambioVentas - tipoCambioCompras)
					* ventas;
		}
		return Redondeador.redondear2Dec(montoUtilidadNeteo);
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#storeReconocimientoUtilidades(int,
	 *      String, String, com.ixe.ods.sica.model.MesaCambio,
	 *      com.ixe.ods.sica.model.MesaCambio, com.ixe.ods.sica.model.Divisa,
	 *      String, double, String, boolean, double, double)
	 */
	public double storeReconocimientoUtilidades(int idUsuario,
			String idCanalDel, String idCanalAl, MesaCambio portafolio,
			MesaCambio alPortafolio, Divisa divisaOper, String valor,
			double tipoCambio, String observaciones, boolean isDivisaMXN,
			double tcAmaneceMesa, double tcUsdMxn) {
		// Obteniendo el Precio de Referencia MID SPOT
		double precioReferencia = findPrecioReferenciaActual().getPreRef()
				.getMidSpot().doubleValue();
		// Obteniendo la Posicion con los valores "Del Portafolio" y la Divisa
		// Operacion
		// (Divisa Seleccionada por el Usuario en el Combo Box).
		Posicion posicion = findRegistroPosicionByIdMesaCambioAndIdDivisa(
				portafolio.getIdMesaCambio(), divisaOper.getIdDivisa());
		// Calculando el Monto de la Perdida o Utilidad.
		double monto = calculaMontoReconocimientoUtilidades(posicion,
				tcAmaneceMesa);
		double montoNeteoTmp;
		if (!isDivisaMXN) {
			montoNeteoTmp = calculaMontoUtilidadesNeteo(posicion);
		} else {
			montoNeteoTmp = 0.0;
		}
		boolean isUtilidad;
		if (!(Math.abs(monto + montoNeteoTmp) < 1.0)) {
			// Si se trata de PESOS
			if (isDivisaMXN) {
				// Para saber si se trata de Utilidad o Perdida
				isUtilidad = monto >= 0;
				// Se verifica si ya existe un registro previo de la misma
				// Mesa-Divisa-Fecha de Hoy,
				// en SC_RECO_UTILIDAD.
				RecoUtilidad ru = findReconocimientoByMesaAndDivisa(portafolio
						.getIdMesaCambio(), divisaOper.getIdDivisa());
				if (ru != null) {
					debug("***1. RU id: " + ru.getIdDealPosicion());
				}
				// Si se trata de UTILIDAD
				if (isUtilidad) {
					RecoUtilidad dpVenta;
					if (ru == null) {
						dpVenta = new RecoUtilidad();
						dpVenta.setDivisa(divisaOper); // Divisa Operacion.
						dpVenta.setMesaCambio(portafolio); // "Del Portafolio".
						dpVenta.setTipoDeal(DealPosicion.TIPO_DEAL_UTILIDADES); // Tipo
						// de
						// Operacion
						// "U".
						dpVenta.setTipoReco(RecoUtilidad.MANUAL); // Tipo de
						// Reconocimiento
						// Manual
						// "M".
					} else {
						dpVenta = ru;
					}
					dpVenta.setIdUsuario(idUsuario); // El Usuario que efectuo
					// la Operacion.
					dpVenta.setMonto(monto); // El Monto en PESOS Calculado de
					// Utilidad.
					dpVenta.setRecibimos(false); // Entregamos.
					dpVenta.setTipoCambio(tcAmaneceMesa); // El Tipo de Cambio
					// al que el Usuario
					// desea que
					// Amanezca la Mesa.
					dpVenta.setUtilidadGlobal(posicion.getUtilidadGlobal()); // Valor
					// de
					// Utilidad
					// Global
					// "Del Portafolio"
					// y
					// la
					// Divisa
					// Operacion.
					dpVenta.setUtilidadMesa(posicion.getUtilidadMesa()); // Valor
					// de
					// Utilidad
					// Mesa
					// "Del Portafolio"
					// y
					// la
					// Divisa
					// Operacion.
					// Venta (Utilidad)
					// Insertando en SC_RECO_UTILIDAD QUE HEREDA DE
					// SC_DEAL_POSICION
					if (ru == null) {
						store(dpVenta); // Si No existe registro en
						// SC_RECO_UTILIDAD, se inserta.
					} else {
						update(dpVenta); // Si ya existe registro en
						// SC_RECO_UTILIDAD, se actualiza.
					}
					// TODO Se le informa al SITE para realizar el retiro
					// (utilidad)
					// para que se realicen los cambios en los flujos bancarios.
				}
				// Si se trata de PERDIDA
				else {
					RecoUtilidad dpCompra;
					if (ru == null) {
						dpCompra = new RecoUtilidad();
						dpCompra.setDivisa(divisaOper); // Divisa Operacion.
						dpCompra.setMesaCambio(portafolio); // "Del Portafolio".
						dpCompra.setTipoDeal(DealPosicion.TIPO_DEAL_UTILIDADES); // Tipo
						// de
						// Operacion
						// "U".
						dpCompra.setTipoReco(RecoUtilidad.MANUAL); // Tipo de
						// Reconocimiento
						// Manual
						// "M".
					} else {
						dpCompra = ru;
					}
					dpCompra.setIdUsuario(idUsuario); // El Usuario que efectuo
					// la Operacion.
					dpCompra.setMonto(Math.abs(monto)); // El Monto en PESOS
					// Calculado de
					// Utilidad.
					dpCompra.setRecibimos(true); // Recibimos
					dpCompra.setTipoCambio(tcAmaneceMesa); // El Tipo de Cambio
					// al que el Usuario
					// desea que
					// Amanezca la Mesa.
					dpCompra.setUtilidadGlobal(posicion.getUtilidadGlobal()); // Valor
					// de
					// Utilidad
					// Global
					// "Del Portafolio"
					// y
					// la
					// Divisa
					// Operacion.
					dpCompra.setUtilidadMesa(posicion.getUtilidadMesa()); // Valor
					// de
					// Utilidad
					// Mesa
					// "Del Portafolio"
					// y
					// la
					// Divisa
					// Operacion.
					// Compra (Perdida)
					// Insertando en SC_RECO_UTILIDAD QUE HEREDA DE
					// SC_DEAL_POSICION
					if (ru == null) {
						store(dpCompra); // Si No existe registro en
						// SC_RECO_UTILIDAD, se inserta.
					} else {
						update(dpCompra); // Si ya existe registro en
						// SC_RECO_UTILIDAD, se actualiza.
					}
					// TODO Se le informa al SITE para realizar el deposito
					// (perdida)
					// para que se realicen los cambios en los flujos bancarios.
				}
			}
			// Si se trata de DOLARES
			else {
				// Si se trata de UTILIDAD
				double montoNeteo = calculaMontoUtilidadesNeteo(posicion);
				monto = Redondeador.redondear2Dec(monto + montoNeteo);
				isUtilidad = monto >= 0;
				if (isUtilidad) {
					// Venta (Utilidad)
					// El Portafolio Internacional Traspasa su Utilidad
					// Insertando en SC_RECO_UTILIDAD QUE HEREDA DE
					// SC_DEAL_POSICION
					RecoUtilidad dpVenta = new RecoUtilidad();
					dpVenta.setDivisa(divisaOper); // Divisa Operacion.
					dpVenta.setMesaCambio(portafolio); // "Del Portafolio".
					dpVenta.setIdUsuario(idUsuario); // El Usuario que efectuo
					// la Operacion.
					dpVenta.setMonto(monto); // El Monto en DOLARES Calculado de
					// Utilidad.
					dpVenta.setRecibimos(false); // Entregamos
					dpVenta.setTipoCambio(tcAmaneceMesa); // En Tipo de Cambio
					// al que el Usuario
					// desea que
					// Amanezca la Mesa.
					dpVenta.setTipoDeal(DealPosicion.TIPO_DEAL_UTILIDADES); // Tipo
					// de
					// Operacion
					// "U".
					dpVenta.setUtilidadGlobal(posicion.getUtilidadGlobal()); // Valor
					// de
					// Utilidad
					// Global
					// "Del Portafolio"
					// y
					// la
					// Divisa
					// Operacion.
					dpVenta.setUtilidadMesa(posicion.getUtilidadMesa()); // Valor
					// de
					// Utilidad
					// Mesa
					// "Del Portafolio"
					// y
					// la
					// Divisa
					// Operacion.
					dpVenta.setTipoReco(RecoUtilidad.MANUAL); // Tipo de
					// Reconocimiento
					// Manual "M".
					if (!Divisa.PESO.equals(portafolio.getDivisaReferencia()
							.getIdDivisa())) {
						dpVenta.setTipoCambioOtraDivRef(tcUsdMxn); // Tipo de
						// Cambio
						// para mesa
						// de Guillo
					}
					store(dpVenta);
					// Insertando en SC_POSICION_LOG
					PosicionLog plVenta = new PosicionLog();
					plVenta.setTipoValor(valor); // La fecha valor, para este
					// caso CASH.
					plVenta.setCanalMesa(new CanalMesa(findCanal(idCanalDel),
							portafolio)); // El Canal "Del Portafolio".
					plVenta.setDealPosicion(dpVenta); // El Objeto
					// SC_RECO_UTILIDAD
					// capturado
					// anteriormente.
					plVenta.setDivisa(divisaOper); // La Divisa Operacion.
					plVenta.setClaveFormaLiquidacion(Constantes.TRANSFERENCIA); // La
					// Clave
					// Forma
					// Liquidacion
					// TRANSFERENCIA
					// =
					// "TRAEXT".
					plVenta.setIdUsuario(idUsuario); // El Usuario que efectuo
					// la Operacion.
					plVenta.setMonto(0.0); // Se pone 0.0 porque este campo no
					// representa, para
					// Operaciones en DOLARES, el Monto en DOLARES.
					plVenta.setMontoMn(monto); // El Monto en DOLARES Calculado
					// de Utilidad.
					plVenta.setNombreCliente(CLIENTE_IXE_DIVISAS); // El Nombre
					// del
					// Cliente
					// es por
					// Default
					// "Cliente Ixe Divisas"
					plVenta.setPrecioReferencia(precioReferencia); // El Precio
					// de
					// Referencia
					// Mid Spot.
					plVenta.setTipoCambioCliente(tcAmaneceMesa); // En Tipo de
					// Cambio al
					// que el
					// Usuario
					// desea que
					// Amanezca
					// la Mesa.
					plVenta.setTipoCambioMesa(tcAmaneceMesa); // En Tipo de
					// Cambio al que
					// el Usuario
					// desea que
					// Amanezca la
					// Mesa.
					plVenta.setTipoOperacion(PosicionLog.DOLARES_UTILIDADES); // Tipo
					// de
					// Operacion
					// "DU".
					store(plVenta);
					// El Portafolio Operacion Recibe la Utilidad
					// Insertando en SC_DEAL_POSICION
					DealPosicion dpCompra = new DealPosicion();
					dpCompra.setDivisa(portafolio.getDivisaReferencia()); // Divisa
					// de
					// Referencia
					// de
					// la
					// Mesa
					// "Del Portafolio"
					dpCompra.setMesaCambio(alPortafolio); // "Al Portafolio"
					dpCompra.setIdUsuario(idUsuario); // El Usuario que efectuo
					// la Operacion.
					dpCompra.setMonto(monto); // El Monto en DOLARES Calculado
					// de Utilidad.
					dpCompra.setRecibimos(true); // Recibimos
					dpCompra.setTipoCambio(tipoCambio); // El Tipo de Cambio de
					// la Operacion.
					dpCompra.setTipoDeal(DealPosicion.TIPO_DEAL_INTERBANCARIO); // Tipo
					// de
					// Operacion
					// "I".
					store(dpCompra);
					// Insertando en SC_POSICION_LOG
					PosicionLog plCompra = new PosicionLog();
					plCompra.setTipoValor(valor); // La fecha valor, para este
					// caso CASH.
					plCompra.setCanalMesa(new CanalMesa(findCanal(idCanalAl),
							alPortafolio)); // El Canal "Al Portafolio".
					plCompra.setDealPosicion(dpCompra); // El Objeto
					// SC_DEAL_POSICION
					// capturado
					// anteriormente.
					plCompra.setDivisa(portafolio.getDivisaReferencia()); // La
					// Divisa
					// de
					// Referencia
					// "Del Portafolio".
					plCompra.setClaveFormaLiquidacion(Constantes.TRANSFERENCIA); // La
					// Clave
					// Forma
					// Liquidacion
					// TRANSFERENCIA
					// =
					// "TRAEXT".
					plCompra.setIdUsuario(idUsuario); // El Usuario que efectuo
					// la Operacion.
					plCompra.setMonto(monto); // El Monto en DOLARES Calculado
					// de Utilidad.
					plCompra.setMontoMn(Redondeador.redondear2Dec(monto
							* tipoCambio)); // El Monto en DOLARES Calculado de
					// Utilidad por
					// el Tipo de Cambio de la Operacion en la Divisa
					// de Operacion.
					plCompra.setNombreCliente(CLIENTE_IXE_DIVISAS); // El Nombre
					// del
					// Cliente
					// es por
					// Default
					// "Cliente Ixe Divisas".
					plCompra.setPrecioReferencia(precioReferencia); // El Precio
					// de
					// Referencia
					// Mid Spot.
					plCompra.setTipoCambioCliente(tipoCambio); // El Tipo de
					// Cambio de la
					// Operacion.
					plCompra.setTipoCambioMesa(tipoCambio); // El Tipo de Cambio
					// de la Operacion.
					plCompra.setTipoOperacion(PosicionLog.COMPRA_INTERBANCARIA); // Tipo
					// de
					// Operacion
					// "CI".
					store(plCompra);
					// TODO Se le informa al SITE para realizar el deposito
					// (perdida)
					// para que se realicen los cambios en los flujos bancarios.
				}
				// Si se trata de Perdida
				else {
					// Compra (Perdida)
					// El Portafolio Internacional Comprensa su Perdida
					// Insertando en SC_RECO_UTILIDAD QUE HEREDA DE
					// SC_DEAL_POSICION
					RecoUtilidad dpCompra = new RecoUtilidad();
					dpCompra.setDivisa(divisaOper); // Divisa Operacion.
					dpCompra.setMesaCambio(portafolio); // "Del Portafolio".
					dpCompra.setIdUsuario(idUsuario); // El Usuario que efectuo
					// la Operacion.
					dpCompra.setMonto(Math.abs(monto)); // El Monto en DOLARES
					// Calculado de
					// Utilidad.
					dpCompra.setRecibimos(true); // Recibimos
					dpCompra.setTipoCambio(tcAmaneceMesa); // En Tipo de Cambio
					// al que el Usuario
					// desea que
					// Amanezca la Mesa.
					dpCompra.setTipoDeal(DealPosicion.TIPO_DEAL_UTILIDADES); // Tipo
					// de
					// Operacion
					// "U".
					dpCompra.setUtilidadGlobal(posicion.getUtilidadGlobal()); // Valor
					// de
					// Utilidad
					// Global
					// "Del Portafolio"
					// y
					// la
					// Divisa
					// Operacion.
					dpCompra.setUtilidadMesa(posicion.getUtilidadMesa()); // Valor
					// de
					// Utilidad
					// Mesa
					// "Del Portafolio"
					// y
					// la
					// Divisa
					// Operacion.
					dpCompra.setTipoReco(RecoUtilidad.MANUAL); // Tipo de
					// Reconocimiento
					// Manual "M".
					if (!Divisa.PESO.equals(portafolio.getDivisaReferencia()
							.getIdDivisa())) {
						dpCompra.setTipoCambioOtraDivRef(tcUsdMxn); // Tipo de
						// Cambio
						// para mesa
						// de Guillo
					}
					store(dpCompra);
					// Insertando en SC_POSICION_LOG
					PosicionLog plCompra = new PosicionLog();
					plCompra.setTipoValor(valor); // La fecha valor, para este
					// caso CASH.
					plCompra.setCanalMesa(new CanalMesa(findCanal(idCanalDel),
							portafolio)); // El Canal "Del Portafolio".
					plCompra.setDealPosicion(dpCompra); // El Objeto
					// SC_RECO_UTILIDAD
					// capturado
					// anteriormente.
					plCompra.setDivisa(divisaOper); // La Divisa Operacion.
					plCompra.setClaveFormaLiquidacion(Constantes.TRANSFERENCIA); // La
					// Clave
					// Forma
					// Liquidacion
					// TRANSFERENCIA
					// =
					// "TRAEXT".
					plCompra.setIdUsuario(idUsuario); // El Usuario que efectuo
					// la Operacion.
					plCompra.setMonto(0.0); // Se pone 0.0 porque este campo no
					// representa, para
					// Operaciones en DOLARES, el Monto en DOLARES.
					plCompra.setMontoMn(Math.abs(monto)); // El Monto en DOLARES
					// Calculado de
					// Utilidad.
					plCompra.setNombreCliente(CLIENTE_IXE_DIVISAS); // El Nombre
					// del
					// Cliente
					// es por
					// Default
					// "Cliente Ixe Divisas"
					plCompra.setPrecioReferencia(precioReferencia); // El Precio
					// de
					// Referencia
					// Mid Spot.
					plCompra.setTipoCambioCliente(tcAmaneceMesa); // En Tipo de
					// Cambio al
					// que el
					// Usuario
					// desea que
					// Amanezca
					// la Mesa.
					plCompra.setTipoCambioMesa(tcAmaneceMesa); // En Tipo de
					// Cambio al que
					// el Usuario
					// desea que
					// Amanezca la
					// Mesa.
					plCompra.setTipoOperacion(PosicionLog.DOLARES_PERDIDA); // Tipo
					// de
					// Operacion
					// "DP"
					store(plCompra);
					// El Portafolio Operacion Traspasa al Internacional para
					// cubrirle su Corto
					// Insertando en SC_DEAL_POSICION
					DealPosicion dpVenta = new DealPosicion();
					dpVenta.setDivisa(portafolio.getDivisaReferencia()); // Divisa
					// de
					// Referencia
					// de
					// la
					// Mesa
					// "Del Portafolio"
					dpVenta.setMesaCambio(alPortafolio); // "Al Portafolio".
					dpVenta.setIdUsuario(idUsuario); // El Usuario que efectuo
					// la Operacion.
					dpVenta.setMonto(Math.abs(monto)); // El Monto en DOLARES
					// Calculado de
					// Utilidad.
					dpVenta.setRecibimos(false); // Entregamos.
					dpVenta.setTipoCambio(tipoCambio); // El Tipo de Cambio de
					// la Operacion.
					dpVenta.setTipoDeal(DealPosicion.TIPO_DEAL_INTERBANCARIO); // Tipo
					// de
					// Operacion
					// "I".
					store(dpVenta);
					// Insertando en SC_POSICION_LOG
					PosicionLog plVenta = new PosicionLog();
					plVenta.setTipoValor(valor); // La fecha valor, para este
					// caso CASH.
					plVenta.setCanalMesa(new CanalMesa(findCanal(idCanalAl),
							alPortafolio)); // El Canal "Al Portafolio".
					plVenta.setDealPosicion(dpVenta); // El Objeto
					// SC_DEAL_POSICION
					// capturado
					// anteriormente.
					plVenta.setDivisa(portafolio.getDivisaReferencia()); // La
					// Divisa
					// de
					// Referencia
					// "Del Portafolio".
					plVenta.setClaveFormaLiquidacion(Constantes.TRANSFERENCIA); // La
					// Clave
					// Forma
					// Liquidacion
					// TRANSFERENCIA
					// =
					// "TRAEXT".
					plVenta.setIdUsuario(idUsuario); // El Usuario que efectuo
					// la Operacion.
					plVenta.setMonto(Math.abs(monto)); // El Monto en DOLARES
					// Calculado de
					// Utilidad.
					plVenta.setMontoMn(Redondeador.redondear2Dec(Math
							.abs(monto)
							* tipoCambio));// El Monto en DOLARES Calculado de
					// Utilidad por
					// el Tipo de Cambio de la Operacion en la Divisa
					// de Operacion.
					plVenta.setNombreCliente(CLIENTE_IXE_DIVISAS); // El Nombre
					// del
					// Cliente
					// es por
					// Default
					// "Cliente Ixe Divisas".
					plVenta.setPrecioReferencia(precioReferencia); // El Precio
					// de
					// Referencia
					// Mid Spot
					plVenta.setTipoCambioCliente(tipoCambio); // El Tipo de
					// Cambio de la
					// Operacion.
					plVenta.setTipoCambioMesa(tipoCambio); // El Tipo de Cambio
					// de la Operacion.
					plVenta.setTipoOperacion(PosicionLog.VENTA_INTERBANCARIA); // Tipo
					// de
					// Operacion
					// "VI".
					store(plVenta);
					// TODO Se le informa al SITE para realizar el deposito
					// (perdida)
					// para que se realicen los cambios en los flujos bancarios.
				}
			}
		}
		// Le regreso el Monto Calculado de Utilidad a la Pagina de
		// Reconocimiento de Utilidades
		// para formar el Mensaje de Operacion Exitosa o con Error.
		return monto;
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#storeSpread(com.ixe.ods.sica.model.Spread)
	 *      .
	 */
	public void storeSpread(Spread spread) {
		store(spread);
		storeSpreadActual(spread);
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#storeSpreadActual(com.ixe.ods.sica.model.Spread)
	 *      .
	 */
	public void storeSpreadActual(Spread sOriginal) {
		Spread spr = new Spread(sOriginal);
		List spas = findSpreadsActualesByTipoPizarronDivisaFormaLiquidacion(
				sOriginal.getDivisa().getIdDivisa(), sOriginal
						.getClaveFormaLiquidacion(), sOriginal
						.getTipoPizarron());
		if (spas.size() > 0) {
			SpreadActual spra = (SpreadActual) spas.get(0);
			spra.setIdSpread(spr.getIdSpread());
			spra.setUltimaModificacion(new Date());
			update(spra);
		} else {
			SpreadActual spra = new SpreadActual();
			SpreadActualPK spraPK = new SpreadActualPK(sOriginal
					.getClaveFormaLiquidacion(), sOriginal.getDivisa(),
					sOriginal.getTipoPizarron());
			spra.setId(spraPK);
			spra.setIdSpread(sOriginal.getIdSpread());
			spra.setUltimaModificacion(new Date());
			store(spra);
		}
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#storeSpreads(java.util.List).
	 */
	public void storeSpreads(List spreads) {
		for (Iterator it = spreads.iterator(); it.hasNext();) {
			Spread sOriginal = (Spread) it.next();
			if (sOriginal.isModificado()) {
				sOriginal.setModificado(false);
				Spread spr = new Spread(sOriginal);
				store(spr);
				List spas = findSpreadsActualesByTipoPizarronDivisaFormaLiquidacion(
						sOriginal.getDivisa().getIdDivisa(), sOriginal
								.getClaveFormaLiquidacion(), sOriginal
								.getTipoPizarron());
				if (spas.size() > 0) {
					SpreadActual spra = (SpreadActual) spas.get(0);
					spra.setIdSpread(spr.getIdSpread());
					spra.setUltimaModificacion(new Date());
					update(spra);
				} else {
					SpreadActual spra = new SpreadActual();
					SpreadActualPK spraPK = new SpreadActualPK(sOriginal
							.getClaveFormaLiquidacion(), sOriginal.getDivisa(),
							sOriginal.getTipoPizarron());
					spra.setId(spraPK);
					spra.setIdSpread(sOriginal.getIdSpread());
					spra.setUltimaModificacion(new Date());
					store(spra);
				}
			}
		}
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#storeSwapParaDeal(com.ixe.ods.sica.model.Swap,
	 *      com.ixe.ods.sica.model.Deal).
	 */
	public void storeSwapParaDeal(Swap swap, Deal deal) {
		store(swap);
		deal.setSwap(swap);
		if (!swap.getDeals().contains(deal)) {
			swap.getDeals().add(deal);
		}
		update(deal);
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#storeTraspasoPortafolios(com.ixe.ods.sica.model.TraspasoMesa,
	 *      int, double, String, String, String).
	 */
	public void storeTraspasoPortafolios(TraspasoMesa traspasoMesa,
			int idUsuario, double monto, String valor, String idCanalAl,
			String idCanalDel) {
		double precioReferencia = findPrecioReferenciaActual().getPreRef()
				.getMidSpot().doubleValue();
		// Venta
		// Insertando en SC_DEAL_POSICION
		DealPosicion dpVenta = new DealPosicion();
		dpVenta.setDivisa(traspasoMesa.getDivisa());
		dpVenta.setMesaCambio(traspasoMesa.getDeMesaCambio());
		dpVenta.setIdUsuario(idUsuario);
		dpVenta.setMonto(monto);
		dpVenta.setRecibimos(false);
		dpVenta.setTipoCambio(traspasoMesa.getTipoCambioDivisaReferencia());
		dpVenta.setTipoDeal(DealPosicion.TIPO_DEAL_TRASPASO);
		store(dpVenta);
		// Insertando en SC_POSICION_LOG
		PosicionLog plVenta = new PosicionLog();
		plVenta.setTipoValor(valor);
		plVenta.setCanalMesa(new CanalMesa(findCanal(idCanalDel), traspasoMesa
				.getDeMesaCambio()));
		plVenta.setDealPosicion(dpVenta);
		plVenta.setDivisa(traspasoMesa.getDivisa());
		plVenta.setClaveFormaLiquidacion(Constantes.TRANSFERENCIA);
		plVenta.setIdUsuario(idUsuario);
		plVenta.setMonto(monto);
		plVenta
				.setMontoMn(monto
						* traspasoMesa.getTipoCambioDivisaReferencia());
		plVenta.setPrecioReferencia(precioReferencia);
		plVenta.setTipoCambioCliente(traspasoMesa
				.getTipoCambioDivisaReferencia());
		plVenta.setTipoCambioMesa(traspasoMesa.getTipoCambioDivisaReferencia());
		plVenta.setTipoOperacion(PosicionLog.VENTA_TRASPASO_PORTAFOLIOS);
		store(plVenta);
		// Compra
		// Insertando en SC_DEAL_POSICION
		DealPosicion dpCompra = new DealPosicion();
		dpCompra.setDivisa(traspasoMesa.getDivisa());
		dpCompra.setMesaCambio(traspasoMesa.getAMesaCambio());
		dpCompra.setIdUsuario(idUsuario);
		dpCompra.setMonto(monto);
		dpCompra.setRecibimos(true);
		dpCompra.setTipoCambio(traspasoMesa.getTipoCambio());
		dpCompra.setTipoDeal(DealPosicion.TIPO_DEAL_TRASPASO);
		store(dpCompra);
		// Insertando en SC_POSICION_LOG
		PosicionLog plCompra = new PosicionLog();
		plCompra.setTipoValor(valor);
		plCompra.setCanalMesa(new CanalMesa(findCanal(idCanalAl), traspasoMesa
				.getAMesaCambio()));
		plCompra.setDealPosicion(dpCompra);
		plCompra.setDivisa(traspasoMesa.getDivisa());
		plCompra.setClaveFormaLiquidacion(Constantes.TRANSFERENCIA);
		plCompra.setIdUsuario(idUsuario);
		plCompra.setMonto(monto);
		plCompra.setMontoMn(monto * traspasoMesa.getTipoCambio());
		plCompra.setPrecioReferencia(precioReferencia);
		plCompra.setTipoCambioCliente(traspasoMesa.getTipoCambio());
		plCompra.setTipoCambioMesa(traspasoMesa.getTipoCambio());
		plCompra.setTipoOperacion(PosicionLog.COMPRA_TRASPASO_PORTAFOLIOS);
		store(plCompra);
		// Insertando el Traspaso
		traspasoMesa.setDeIdDealPosicion(dpVenta);
		traspasoMesa.setAIdDealPosicion(dpCompra);
		traspasoMesa.setFechaOperacion(new Date());
		store(traspasoMesa);
	}

	/**
	 * @see com.ixe.ods.sica.sdo.WorkFlowServiceData#terminarActividadesParaDeal(com.ixe.ods.sica.model.Deal,
	 *      String, com.ixe.ods.seguridad.model.IUsuario).
	 * @param deal
	 *            El deal al cual se le interrumpir&aacute; su workflow.
	 * @param resultado
	 *            El comentario que se le asignar&aacute; a las actividades
	 *            canceladas.
	 * @param usuario
	 *            El usuario que solicita la interrupci&oacute;n del workflow.
	 * @return boolean True si se encontraron actividades.
	 */
	public boolean terminarActividadesParaDeal(Deal deal, String resultado,
			IUsuario usuario) {
		List actividades = findActividadesPendientesProcesoDeal(deal
				.isInterbancario() ? Actividad.PROC_DEAL_INTERBANCARIO
				: Actividad.PROC_DEAL_NORMAL, deal.getIdDeal());
		terminar(actividades, resultado, usuario);
		return actividades.size() > 0;
	}

	/**
	 * @see com.ixe.ods.sica.sdo.WorkFlowServiceData#terminarActividadesParaDealDetalle(com.ixe.ods.sica.model.DealDetalle,
	 *      String, com.ixe.ods.seguridad.model.IUsuario)
	 * @param det
	 *            El detalle de deal al cual se le interrumpir&aacute; su
	 *            workflow.
	 * @param resultado
	 *            El comentario que se le asignar&aacute; a las actividades
	 *            canceladas.
	 * @param usuario
	 *            El usuario que solicita la interrupci&oacute;n del workflow.
	 */
	public void terminarActividadesParaDealDetalle(DealDetalle det,
			String resultado, IUsuario usuario) {
		List actividades = getHibernateTemplate()
				.find(
						"FROM Actividad as a WHERE "
								+ "a.dealDetalle.idDealPosicion = ? AND a.proceso = ? AND a.status = ?",
						new Object[] { new Integer(det.getIdDealPosicion()),
								Actividad.PROC_HORARIO_MONTO,
								Actividad.STATUS_PENDIENTE });
		terminar(actividades, resultado, usuario);
	}

	/**
	 * Cancela las actividades proporcionadas.
	 * 
	 * @param actividades
	 *            La lista de actividades.
	 * @param resultado
	 *            El motivo de cancelaci&oacute;n de la actividad.
	 * @param usuario
	 *            El usuario que solicit&oacute; la terminaci&oacute;n de las
	 *            actividades.
	 */
	private void terminar(List actividades, String resultado, IUsuario usuario) {
		for (Iterator it = actividades.iterator(); it.hasNext();) {
			Actividad actividad = (Actividad) it.next();
			actividad.cancelar(usuario, resultado);
			update(actividad);
		}
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#usoLiberacionLineaOperacion(LineaOperacion,
	 *      String, DealDetalle, IUsuario, boolean)
	 */
	public double usoLiberacionLineaOperacion(LineaOperacion lineaOperacion,
			String tipoOper, DealDetalle dealDetalle, IUsuario usuario,
			boolean seAutorizaExcedente) {
		double montoUSD = dealDetalle.getMontoUSD();
		if (LineaOperacionLog.TIPO_OPER_USO.equals(tipoOper)) {
			double excedente = montoUSD - lineaOperacion.getSaldo();
			if (excedente <= 0) {
				lineaOperacion.setUsoLinea(lineaOperacion.getUsoLinea()
						+ montoUSD);
				update(lineaOperacion);
				store(new LineaOperacionLog(lineaOperacion, tipoOper,
						dealDetalle, usuario));
			} else {
				if (seAutorizaExcedente) {
					lineaOperacion.setImporte(lineaOperacion.getImporte()
							+ excedente);
					lineaOperacion.setUsoLinea(lineaOperacion.getUsoLinea()
							+ montoUSD);
					update(lineaOperacion);
					store(new LineaOperacionLog(lineaOperacion,
							LineaOperacionLog.TIPO_OPER_EXCEDENTE, dealDetalle,
							usuario));
					return excedente;
				} else {
					return excedente;
				}
			}
		} else if (LineaOperacionLog.TIPO_OPER_LIBERACION.equals(tipoOper)) {
			lineaOperacion.setUsoLinea(lineaOperacion.getUsoLinea() - montoUSD);
			update(lineaOperacion);
			store(new LineaOperacionLog(lineaOperacion, tipoOper, dealDetalle,
					usuario));
		}
		return 0.0;
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findTipoValorByIdDealPosicion(int)
	 */
	public String findTipoValorByIdDealPosicion(int idPosicionLog) {
		List pls = getHibernateTemplate().findByNamedQuery(
				"findTipoValorByIdDealPosicion", new Integer(idPosicionLog));
		PosicionLog pl = (PosicionLog) pls.get(0);
		return pl.getTipoValor();
	}

	/**
	 * Obtiene una referencia al Bean de Servicios de Seguridad.
	 * 
	 * @return PizarronServiceData El Bean de Servicios de Pizarron.
	 */
	protected PizarronServiceData getPizarronServiceData() {
		return (PizarronServiceData) _appContext.getBean("pizarronServiceData");
	}

	/**
	 * Regresa el valor de sicaAlertasService.
	 * 
	 * @return SicaAlertasService.
	 */
	protected SicaAlertasService getSicaAlertasService() {
		return (SicaAlertasService) _appContext.getBean("sicaAlertasService");
	}

	/**
	 * Regresa el valor de lineasCambioServiceData.
	 * 
	 * @return LineaCambioServiceData.
	 */
	protected LineaCambioServiceData getLineaCambioServiceData() {
		return (LineaCambioServiceData) _appContext
				.getBean("lineasCambioServiceData");
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findReporteDealsDiarios(String,
	 *      String, String, Integer, Date, Date)
	 */
	public List findReporteDealsDiarios(String idDivisa,
			String formaLiquidacion, String operacion, Integer promotor,
			Date desde, Date hoy) {
		StringBuffer sb = new StringBuffer(
				"FROM DealDetalle AS dd WHERE dd.deal.fechaCaptura "
						+ "BETWEEN ? AND ? ");
		List parametersList = new ArrayList();
		parametersList.add(desde);
		parametersList.add(hoy);
		if (!idDivisa.equals("0")) {
			sb.append("AND dd.divisa.idDivisa = ? ");
			parametersList.add(idDivisa);
		} else {
			sb.append("AND dd.divisa.idDivisa != 'MXN' ");
		}
		if (!formaLiquidacion.equals("TODOS")) {
			sb.append("AND dd.claveFormaLiquidacion = ? ");
			parametersList.add(formaLiquidacion);
		}
		if (!operacion.equals("Compra-Venta")) {
			if (operacion.equals("Compra")) {
				sb.append("AND dd.recibimos = 'S' ");
			} else if (operacion.equals("Venta")) {
				sb.append("AND dd.recibimos = 'N' ");
			}
		}
		if (promotor.intValue() > 0) {
			sb.append("AND dd.deal.promotor.idPersona = ? ");
			parametersList.add(promotor);
		}
		sb
				.append("ORDER BY dd.divisa,dd.recibimos,dd.claveFormaLiquidacion, "
						+ "dd.deal.promotor.paterno,dd.deal.promotor.materno,dd.deal.promotor.nombre, "
						+ "dd.deal.idDeal");
		Object[] parametersArray = new Object[parametersList.size()];
		int i = 0;
		for (Iterator it = parametersList.iterator(); it.hasNext(); i++) {
			parametersArray[i] = it.next();
		}
		return getHibernateTemplate().find(sb.toString(), parametersArray);
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findReporteDealsDiarios(String,
	 *      String, String, Integer, Date, Date, double, double).
	 */
	public List findReporteDealsDiarios(String idDivisa,
			String formaLiquidacion, String operacion, Integer promotor,
			Date desde, Date hoy, double montoMinimo, double montoMaximo) {
		StringBuffer sb = new StringBuffer(
				"FROM DealDetalle AS dd WHERE dd.deal.fechaCaptura "
						+ "BETWEEN ? AND ? ");
		List parametersList = new ArrayList();
		parametersList.add(desde);
		parametersList.add(hoy);
		if (!idDivisa.equals("0")) {
			sb.append("AND dd.divisa.idDivisa = ? ");
			parametersList.add(idDivisa);
		} else {
			sb.append("AND dd.divisa.idDivisa != 'MXN' ");
		}
		if (!formaLiquidacion.equals("TODOS")) {
			sb.append("AND dd.claveFormaLiquidacion = ? ");
			parametersList.add(formaLiquidacion);
		}
		if (!operacion.equals("Compra-Venta")) {
			if (operacion.equals("Compra")) {
				sb.append("AND dd.recibimos = 'S' ");
			} else if (operacion.equals("Venta")) {
				sb.append("AND dd.recibimos = 'N' ");
			}
		}
		if (promotor.intValue() > 0) {
			sb.append("AND dd.deal.promotor.idPersona = ? ");
			parametersList.add(promotor);
		}
		if (montoMaximo > 0) {
			sb.append("AND dd.monto BETWEEN ? AND ? ");
			parametersList.add(new Double(montoMinimo));
			parametersList.add(new Double(montoMaximo));
		}
		sb
				.append("AND dd.deal.statusDeal != 'CA' AND dd.statusDetalleDeal != 'CA' AND "
						+ "dd.divisa.idDivisa != 'MXN' ");
		sb
				.append("ORDER BY dd.divisa,dd.recibimos,dd.claveFormaLiquidacion, "
						+ "dd.deal.promotor.paterno,dd.deal.promotor.materno,dd.deal.promotor.nombre,"
						+ "dd.deal.idDeal");
		return getHibernateTemplate().find(sb.toString(),
				parametersList.toArray());
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findReporteDealsDiarios(List,
	 *      List, List, List, List, boolean, boolean, Date, Date, Double,
	 *      Double, String[])
	 */
	public List findReporteDealsDiarios(List canales, List promotores,
			List divisas, List tipoExcepciones, List tipoZonas, boolean compra,
			boolean venta, Date fechaInicial, Date fechaFinal,
			Double montoMinimo, Double montoMaximo, String[] contratos,
			String[] productos) {
		StringBuffer sb = new StringBuffer(
				"select new com.ixe.ods.sica.model.DetalleReporteDiario("
						+ "dd, "
						+ "pcr.id.persona"
						+ ") FROM DealDetalle AS dd, PersonaCuentaRol AS pcr "
						+ "WHERE dd.deal.contratoSica.noCuenta = pcr.id.cuenta "
						+ "AND pcr.id.rol.idRol = 'TIT' "
						+ "AND dd.deal.fechaCaptura BETWEEN ? AND ? ");
		List parametersList = new ArrayList();
		parametersList.add(fechaInicial);
		parametersList.add(fechaFinal);
		if (canales != null && canales.size() > 0) {
			sb.append("AND dd.deal.canalMesa.canal.idCanal in (");
			for (Iterator i = canales.iterator(); i.hasNext();) {
				sb.append("'" + i.next() + "'");
				if (i.hasNext()) {
					sb.append(",");
				}
			}
			sb.append(")");
		}
		if (promotores != null && promotores.size() > 0) {
			sb.append("AND dd.deal.promotor.idPersona in (");
			for (Iterator i = promotores.iterator(); i.hasNext();) {
				sb.append(i.next());
				if (i.hasNext()) {
					sb.append(",");
				}
			}
			sb.append(")");
		}
		if (divisas != null && divisas.size() > 0) {
			sb.append("AND dd.divisa.idDivisa in (");
			for (Iterator i = divisas.iterator(); i.hasNext();) {
				sb.append("'" + i.next() + "'");
				if (i.hasNext()) {
					sb.append(",");
				}
			}
			sb.append(") ");
		}
		if (tipoExcepciones != null && tipoExcepciones.size() > 0) {
			sb.append("AND dd.deal.tipoExcepcion in (");
			for (Iterator i = tipoExcepciones.iterator(); i.hasNext();) {
				sb.append("'" + i.next() + "'");
				if (i.hasNext()) {
					sb.append(",");
				}
			}
			sb.append(") ");
			if (tipoExcepciones.contains(PersonaListaBlanca.SIN_EXCEPCION)) {
				sb.append("OR dd.deal.tipoExcepcion is null) ");
			}
		}
		if (tipoZonas != null && tipoZonas.size() > 0) {
			sb.append("AND dd.deal.tipoZona in (");
			for (Iterator i = tipoZonas.iterator(); i.hasNext();) {
				sb.append("'" + i.next() + "'");
				if (i.hasNext()) {
					sb.append(",");
				}
			}
			sb.append(") ");
			if (tipoZonas.contains(Constantes.ZONA_NORMAL)) {
				sb.append("OR dd.deal.tipoZona is null) ");
			}
		}
		if (compra && !venta) {
			sb.append("AND dd.recibimos = 'S' ");
		} else if (venta && !compra) {
			sb.append("AND dd.recibimos = 'N' ");
		}
		if (contratos != null && contratos.length > 0
				&& !contratos[0].equals("")) {
			sb.append("AND dd.deal.contratoSica.noCuenta in (");
			for (int i = 0; i < contratos.length;) {
				sb.append("'" + contratos[i++] + "'");
				if (i != contratos.length) {
					sb.append(",");
				}
			}
			sb.append(") ");
		}
		if (productos != null && productos.length > 0
				&& !productos[0].equals("")) {
			sb.append("AND dd.claveFormaLiquidacion in (");
			for (int i = 0; i < productos.length;) {
				sb.append("'" + productos[i++] + "'");
				if (i != productos.length) {
					sb.append(",");
				}
			}
			sb.append(") ");
		}
		sb
				.append("AND dd.deal.statusDeal != 'CA' AND dd.statusDetalleDeal != 'CA' AND "
						+ "dd.divisa.idDivisa != 'MXN' ");
		sb
				.append("ORDER BY dd.divisa,dd.recibimos,dd.claveFormaLiquidacion, "
						+ "dd.deal.promotor.paterno,dd.deal.promotor.materno,dd.deal.promotor.nombre,"
						+ "dd.deal.idDeal");
		List temp = getHibernateTemplate().find(sb.toString(),
				parametersList.toArray());
		List resultado = new ArrayList();
		for (Iterator i = temp.iterator(); i.hasNext();) {
			DetalleReporteDiario detalle = (DetalleReporteDiario) i.next();
			try {
				Hibernate.initialize(detalle.getPersona().getPais());
				Hibernate.initialize(detalle.getPersona().getDirecciones());
				Hibernate.initialize(detalle.getPersona().getTelefonos());
				Hibernate.initialize(detalle.getDealDetalle().getDivisa());
				Hibernate.initialize(detalle.getDealDetalle().getDeal());
				Hibernate.initialize(detalle.getDealDetalle().getDeal()
						.getPromotor());
				Hibernate.initialize(detalle.getDealDetalle().getDeal()
						.getContratoSica());
				Hibernate.initialize(detalle.getDealDetalle().getDeal()
						.getContratoSica().getRoles());
				Hibernate.initialize(detalle.getDealDetalle().getDeal()
						.getCanalMesa());
				Hibernate.initialize(detalle.getDealDetalle().getDeal()
						.getCanalMesa().getCanal());
				Hibernate.initialize(detalle.getDealDetalle().getDeal()
						.getCanalMesa().getCanal().getSucursal());
			} catch (HibernateException e) {
			}

			boolean agregar = true;

			if (montoMaximo.doubleValue() > 0
					&& detalle.getDealDetalle().getMontoUSD() > montoMaximo
							.doubleValue()) {
				agregar = false;
			}
			if (detalle.getDealDetalle().getMontoUSD() < montoMinimo
					.doubleValue()) {
				agregar = false;
			}
			if (agregar) {
				resultado.add(detalle);
			}
		}
		return resultado;
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findReporteDealsTrimestral(List,
	 *      List, List, List, List, boolean, boolean, Date, Date, double,
	 *      double, String[], String[])
	 * 
	 * @deprecated No funciona este m&eacute;todo.
	 */
	public List findReporteDealsTrimestral(List canales, List promotores,
			List divisas, List tipoExcepciones, List tipoZonas, boolean compra,
			boolean venta, Date fechaInicial, Date fechaFinal,
			double montoTrimCte, double montoTrimUsr, String[] contratos,
			String[] productos) {
		StringBuffer sbInterno = new StringBuffer();
		sbInterno
				.append("SELECT d2.contratoSica.noCuenta "
						+ "FROM Deal AS d2, DealDetalle AS dd2, DealPosicion AS dp2, "
						+ "ComplementoDatos AS cd2, FactorDivisa AS fd2 "
						+ "WHERE d2.idDeal = dd2.deal.idDeal "
						+ "AND dd2.factorDivisa.idFactorDivisa = fd2.idFactorDivisa "
						+ "AND dd2.idDealPosicion = dp2.idDealPosicion "
						+ "AND d2.contratoSica.noCuenta = cd2.noCuenta "
						+ "AND dd2.deal.statusDeal != 'CA' AND dd2.statusDetalleDeal != 'CA' "
						+ "AND dd2.divisa.idDivisa = dd.divisa.idDivisa ");
		if (canales != null && canales.size() > 0) {
			sbInterno.append("AND dd2.deal.canalMesa.canal.idCanal in (");
			for (Iterator i = canales.iterator(); i.hasNext();) {
				sbInterno.append("'" + i.next() + "'");
				if (i.hasNext()) {
					sbInterno.append(",");
				}
			}
			sbInterno.append(")");
		}
		if (promotores != null && promotores.size() > 0) {
			sbInterno.append("AND dd2.deal.promotor.idPersona in (");
			for (Iterator i = promotores.iterator(); i.hasNext();) {
				sbInterno.append(i.next());
				if (i.hasNext()) {
					sbInterno.append(",");
				}
			}
			sbInterno.append(")");
		}
		if (divisas != null && divisas.size() > 0) {
			sbInterno.append("AND dd2.divisa.idDivisa in (");
			for (Iterator i = divisas.iterator(); i.hasNext();) {
				sbInterno.append("'" + i.next() + "'");
				if (i.hasNext()) {
					sbInterno.append(",");
				}
			}
			sbInterno.append(")");
		}
		if (tipoExcepciones != null && tipoExcepciones.size() > 0) {
			sbInterno.append("AND (dd2.deal.tipoExcepcion in (");
			for (Iterator i = tipoExcepciones.iterator(); i.hasNext();) {
				sbInterno.append("'" + i.next() + "'");
				if (i.hasNext()) {
					sbInterno.append(",");
				}
			}
			sbInterno.append(") ");
			if (tipoExcepciones.contains(PersonaListaBlanca.SIN_EXCEPCION)) {
				sbInterno.append("OR dd2.deal.tipoExcepcion is null) ");
			}
		}
		if (tipoZonas != null && tipoZonas.size() > 0) {
			sbInterno.append("AND (dd2.deal.tipoZona in (");
			for (Iterator i = tipoZonas.iterator(); i.hasNext();) {
				sbInterno.append("'" + i.next() + "'");
				if (i.hasNext()) {
					sbInterno.append(",");
				}
			}
			sbInterno.append(") ");
			if (tipoZonas.equals(Constantes.ZONA_NORMAL)) {
				sbInterno.append("OR dd2.deal.tipoZona is null) ");
			}
		}
		if (compra && !venta) {
			sbInterno.append("AND dd2.recibimos = 'S' ");
		} else if (venta && !compra) {
			sbInterno.append("AND dd2.recibimos = 'N' ");
		}
		if (contratos != null && contratos.length > 0
				&& !contratos[0].equals("")) {
			sbInterno.append("AND dd2.deal.contratoSica.noCuenta in (");
			for (int i = 0; i < contratos.length;) {
				sbInterno.append("'" + contratos[i++] + "'");
				if (i != contratos.length) {
					sbInterno.append(",");
				}
			}
			sbInterno.append(") ");
		}
		if (productos != null && productos.length > 0
				&& !productos[0].equals("")) {
			sbInterno.append("AND dd2.claveFormaLiquidacion in (");
			for (int i = 0; i < productos.length;) {
				sbInterno.append("'" + productos[i++] + "'");
				if (i != productos.length) {
					sbInterno.append(",");
				}
			}
			sbInterno.append(") ");
		}

		sbInterno
				.append("AND dd2.deal.fechaCaptura BETWEEN ? AND ? "
						+ "GROUP BY d2.contratoSica.noCuenta, cd2.cliente "
						+ "HAVING (cd2.cliente = 'S' AND SUM(dp2.monto * fd2.facDiv.factor) >= "
						+ montoTrimCte
						+ ") "
						+ "OR (cd2.cliente = 'N' AND SUM(dp2.monto * fd2.facDiv.factor) >= "
						+ montoTrimUsr + ")))");

		StringBuffer sb = new StringBuffer(
				"select new com.ixe.ods.sica.model.DetalleReporteDiario("
						+ "dd, "
						+ "cd, "
						+ "pcr.id.persona "
						+ ") FROM DealDetalle AS dd, Deal AS d, DealPosicion AS dp, "
						+ "ComplementoDatos AS cd, PersonaCuentaRol AS pcr "
						+ "WHERE dd.deal.idDeal = d.idDeal "
						+ "AND dd.idDealPosicion = dp.idDealPosicion "
						+ "AND d.contratoSica.noCuenta = cd.noCuenta "
						+ "AND d.contratoSica.noCuenta = pcr.id.cuenta "
						+ "AND dd.deal.statusDeal != 'CA' AND dd.statusDetalleDeal != 'CA' "
						+ "AND dd.divisa.idDivisa != 'MXN' "
						+ "AND pcr.id.rol.idRol = 'TIT' ");
		if (divisas != null && divisas.size() > 0) {
			sb.append("AND dd.divisa.idDivisa in (");
			for (Iterator i = divisas.iterator(); i.hasNext();) {
				sb.append("'" + i.next() + "'");
				if (i.hasNext()) {
					sb.append(",");
				}
			}
			sb.append(")");
		}
		if (compra && !venta) {
			sb.append("AND dd.recibimos = 'S' ");
		} else if (venta && !compra) {
			sb.append("AND dd.recibimos = 'N' ");
		}
		sb.append("AND dd.deal.fechaCaptura BETWEEN ? AND ? "
				+ "AND d.contratoSica.noCuenta in ((");
		sb.append(sbInterno);
		List parametersList = new ArrayList();
		parametersList.add(fechaInicial);
		parametersList.add(fechaFinal);
		parametersList.add(fechaInicial);
		parametersList.add(fechaFinal);
		sb
				.append("ORDER BY dd.divisa,dd.recibimos,dd.claveFormaLiquidacion, "
						+ "dd.deal.promotor.paterno,dd.deal.promotor.materno,dd.deal.promotor.nombre,"
						+ "dd.deal.idDeal");
		List resultado = getHibernateTemplate().find(sb.toString(),
				parametersList.toArray());
		for (Iterator i = resultado.iterator(); i.hasNext();) {
			DetalleReporteDiario detalle = (DetalleReporteDiario) i.next();
			try {
				Hibernate.initialize(detalle.getPersona().getPais());
				Hibernate.initialize(detalle.getPersona().getDirecciones());
				Hibernate.initialize(detalle.getPersona().getTelefonos());
				Hibernate.initialize(detalle.getDealDetalle().getDivisa());
				Hibernate.initialize(detalle.getDealDetalle().getDeal());
				Hibernate.initialize(detalle.getDealDetalle().getDeal()
						.getPromotor());
				Hibernate.initialize(detalle.getDealDetalle().getDeal()
						.getContratoSica());
				Hibernate.initialize(detalle.getDealDetalle().getDeal()
						.getContratoSica().getRoles());
				Hibernate.initialize(detalle.getDealDetalle().getDeal()
						.getCanalMesa());
				Hibernate.initialize(detalle.getDealDetalle().getDeal()
						.getCanalMesa().getCanal());
				Hibernate.initialize(detalle.getDealDetalle().getDeal()
						.getCanalMesa().getCanal().getSucursal());
			} catch (HibernateException e) {
			}
		}
		return resultado;
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findAllDealDetallesBySectorAndDateOrderByDivisaAndRecibimos(Date,
	 *      Date).
	 */
	public List findAllDealDetallesBySectorAndDateOrderByDivisaAndRecibimos(
			Date gc, Date fechaLiquidacion) {
		return getHibernateTemplate().findByNamedQuery(
				"findAllDealDetallesBySectorAndDateOrderByDivisaAndRecibimos",
				new Object[] { gc, fechaLiquidacion });
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findAllDealDetallesBySectorAndDateOrderByDivisaAndRecibimosAndNoCuenta(Date,
	 *      Date).
	 */
	public List findAllDealDetallesBySectorAndDateOrderByDivisaAndRecibimosAndNoCuenta(
			Date gc, Date fechaLiquidacion) {
		return getHibernateTemplate()
				.findByNamedQuery(
						"findAllDealDetallesBySectorAndDateOrderByDivisaAndRecibimosAndNoCuenta",
						new Object[] { gc, fechaLiquidacion });
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findAllDealDetallesBySectorAndDateOrderByRecibimosAndDivisa(Date,
	 *      Date)
	 */
	public List findAllDealDetallesBySectorAndDateOrderByRecibimosAndDivisa(
			Date gc, Date fechaLiquidacion) {
		return getHibernateTemplate().findByNamedQuery(
				"findAllDealDetallesBySectorAndDateOrderByRecibimosAndDivisa",
				new Object[] { gc, fechaLiquidacion });
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findEjecutivoOrigenByIdPersona(Integer)
	 */
	public EjecutivoOrigen findEjecutivoOrigenByIdPersona(Integer idPersona) {
		List l = getHibernateTemplate()
				.find(
						"FROM EjecutivoOrigen AS eo WHERE eo.id.idPersona = ? and eo.status='ACTIVO' AND eo.id.sistemaOrigen = ?",
						new Object[] { idPersona, "PHO" });
		return !l.isEmpty() ? (EjecutivoOrigen) l.get(0) : null;
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findEjecutivosIxeDirecto().
	 */
	public List findEjecutivosIxeDirecto() {
		final List idsPerfiles = new ArrayList();
		List asocsFacs = getHibernateTemplate().find(
				"FROM AsociacionFacultad AS af WHERE "
						+ "af.facultad.nombre = ?",
				FacultySystem.SICA_CAN_IXEDIRECTO);
		if (asocsFacs.isEmpty()) {
			return new ArrayList();
		}
		for (Iterator it = asocsFacs.iterator(); it.hasNext();) {
			AsociacionFacultad asocFac = (AsociacionFacultad) it.next();
			idsPerfiles.add(new Integer(asocFac.getIdCol()));
		}
		return getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException {
				String sql = "SELECT p.usuario.persona FROM Perfil AS p where p.idCol in "
						+ "(:listaIdsPerfiles)";
				return session.createQuery(sql).setParameterList(
						"listaIdsPerfiles", idsPerfiles, new IntegerType())
						.list();
			}
		});
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findDivisaByIdMoneda(String)
	 */
	public Divisa findDivisaByIdMoneda(String idMoneda) {
		List divisas = getHibernateTemplate().find(
				"FROM Divisa AS d WHERE d.idMoneda  = ?", idMoneda);
		return divisas.isEmpty() ? null : (Divisa) divisas.get(0);
	}

	/**
	 * @see SicaServiceData@findAllDealsInterbancariosReporte(Date, Date)
	 */
	public List findAllDealsInterbancariosReporte(Date desde, Date hasta) {
		List dd = getHibernateTemplate().findByNamedQuery(
				"findAllDealsInterbancariosReporte",
				new Object[] { desde, hasta });
		for (Iterator it = dd.iterator(); it.hasNext();) {
			DealDetalle det = (DealDetalle) it.next();
			det.getDivisa().getDescripcion();
			det.getDeal().getTipoValor();
			det.getDeal().getCliente().getNombreCompleto();
			det.getFactorDivisa();
			if (det.getFactorDivisa() != null) {
				det.setFactorDivisa(new Double(0.0));
			}
		}
		Collections.sort(dd, new Comparator() {
			public int compare(Object o1, Object o2) {
				return ((DealDetalle) o1).getDeal().getCliente()
						.getNombreCompleto().compareTo(
								((DealDetalle) o2).getDeal().getCliente()
										.getNombreCompleto());
			}
		});
		return dd;
	}

	/**
	 * @see SicaServiceData@findAllDealsInterbancariosReporte(desde, hasta,
	 *      idDivisa, portafolio, idPersona)
	 */
	public List findAllDealsInterbancariosReporte(Date desde, Date hasta,
			String idDivisa, int idMesaCambio, Integer idPersona) {
		List params = new ArrayList();
		params.add(desde);
		params.add(hasta);
		StringBuffer sb = new StringBuffer(
				"SELECT dd FROM DealDetalle AS dd INNER JOIN FETCH dd.deal AS d ");
		if (idPersona.intValue() > 0) {
			sb
					.append("INNER JOIN FETCH d.contratoSica AS cs LEFT JOIN cs.roles AS pcr ");
			sb.append("INNER JOIN FETCH pcr.id.persona AS p ");
			sb.append("WHERE d.fechaCaptura BETWEEN ? AND ? ");
			sb.append("AND p.idPersona = ? ");
			params.add(idPersona);
		} else {
			sb.append("WHERE d.fechaCaptura BETWEEN ? AND ? ");
		}
		if (!"0".equals(idDivisa)) {
			sb.append("AND dd.divisa.idDivisa = ? ");
			params.add(idDivisa);
		}
		if (idMesaCambio > 0) {
			sb.append("AND d.canalMesa.mesaCambio.idMesaCambio = ? ");
			params.add(new Integer(idMesaCambio));
		}
		sb
				.append("AND d.tipoDeal = 'I' AND dd.divisa.idDivisa != 'MXN' AND d.statusDeal != 'CA' AND dd.statusDetalleDeal != 'CA' ");
		sb.append("ORDER BY d.fechaLiquidacion, dd.divisa.idDivisa");
		List dds = new ArrayList(new HashSet(getHibernateTemplate().find(
				sb.toString(), params.toArray())));
		Collections.sort(dds, new Comparator() {
			public int compare(Object o1, Object o2) {
				return ((DealDetalle) o1).getDivisa().getIdDivisa().compareTo(
						((DealDetalle) o2).getDivisa().getIdDivisa());
			}

		});
		Collections.sort(dds, new Comparator() {
			public int compare(Object o1, Object o2) {
				return ((DealDetalle) o1).getDeal().getFechaLiquidacion()
						.compareTo(
								((DealDetalle) o2).getDeal()
										.getFechaLiquidacion());
			}

		});
		for (Iterator it = dds.iterator(); it.hasNext();) {
			DealDetalle det = (DealDetalle) it.next();
			det.getDivisa().getDescripcion();
			det.getDeal().getTipoValor();
		}
		return dds;
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findAllDealsReporteAuxiliaresContables(Date,
	 *      Date)
	 */
	public List findAllDealsReporteAuxiliaresContables(Date desde, Date hasta) {
		return getHibernateTemplate()
				.find(
						"SELECT p.divisa.idDivisa, p.cuentaContable, p.entidad, p.idDeal, p.cargoAbono, SUM(p.importe), p.noContratoSica, p.cliente, p.referencia "
								+ "FROM Poliza as p WHERE p.fechaValor BETWEEN ? AND ? "
								+ "GROUP BY p.divisa.idDivisa, p.cuentaContable, p.entidad, p.idDeal, p.cargoAbono, p.noContratoSica, p.cliente, p.referencia "
								+ "ORDER BY p.divisa.idDivisa, p.cuentaContable, p.entidad, p.idDeal, p.cargoAbono",
						new Object[] { desde, hasta });
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findAllDealsReportePolizasContables(Date,
	 *      Date)
	 */
	public List findAllDealsReportePolizasContables(Date desde, Date hasta) {
		return getHibernateTemplate()
				.find(
						"SELECT p.divisa.idDivisa, p.cuentaContable, p.entidad, p.cargoAbono, SUM(p.importe), p.referencia "
								+ "FROM Poliza as p WHERE p.fechaValor BETWEEN ? AND ? "
								+ "GROUP BY p.divisa.idDivisa, p.cuentaContable, p.entidad, p.cargoAbono, p.importe, p.referencia "
								+ "ORDER BY p.divisa.idDivisa, p.cuentaContable, p.entidad, p.cargoAbono",
						new Object[] { desde, hasta });
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findPosicionAllMesas()
	 */
	public List findPosicionAllMesas() {
		return getHibernateTemplate()
				.find(
						"FROM Posicion AS p ORDER BY p.mesaCambio.idMesaCambio, p.divisa.idDivisa");
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findDetallesUtilidadesMayoreo(Date,
	 *      Date).
	 */
	public List findDetallesUtilidadesMayoreo(Date fechaHoy, Date fechaHoy2) {
		List dets = getHibernateTemplate().findByNamedQuery(
				"findDetallesUtilidadesMayoreo",
				new Object[] { fechaHoy, fechaHoy2 });
		for (Iterator it = dets.iterator(); it.hasNext();) {
			DealDetalle det = (DealDetalle) it.next();
			if (!Hibernate.isInitialized(det.getDeal())) {
				try {
					Hibernate.initialize(det.getDeal());
				} catch (HibernateException e) {
					throw SessionFactoryUtils
							.convertHibernateAccessException(e);
				}
			}
		}
		return dets;
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findDetallesUtilidadesIxeDirecto(java.util.Date,
	 *      java.util.Date, String, java.util.List).
	 */
	public List findDetallesUtilidadesIxeDirectoSucursales(Date inicio,
			Date fin, String canal, List mnemonicos) {
		final Date fInicio = inicio;
		final Date fFin = fin;
		final String idCanal = canal;
		final List listaMnemonicos = mnemonicos;
		return getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException {
				Query q = session
						.createQuery("FROM DealDetalleSinPlantilla AS dd WHERE "
								+ "dd.deal.fechaCaptura BETWEEN :fInicio AND :fFin AND "
								+ "dd.deal.canalMesa.canal.idCanal = :canal AND dd.deal.statusDeal != 'CA' "
								+ "AND dd.statusDetalleDeal != 'CA' AND dd.mnemonico IN (:mnemonicos) "
								+ "AND dd.divisa.idDivisa != 'MXN' AND dd.divisa.tipo != 'M'");
				q.setParameter("fInicio", fInicio);
				q.setParameter("fFin", fFin);
				q.setParameter("canal", idCanal);
				q.setParameterList("mnemonicos", listaMnemonicos,
						new StringType());
				return q.list();
			}
		});
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findDetallesUtilidadesIxeDirecto(Date,
	 *      Date, String, List).
	 */
	public List findDetallesUtilidadesIxeDirecto(Date inicio, Date fin,
			String canal, List mnemonicos) {
		final Date fInicio = inicio;
		final Date fFin = fin;
		final String idCanal = canal;
		final List listaMnemonicos = mnemonicos;
		return getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException {
				Query q = session
						.createQuery("FROM DealDetalleSinPlantilla AS dd WHERE "
								+ "dd.deal.fechaCaptura BETWEEN :fInicio AND :fFin AND "
								+ "dd.deal.canalMesa.canal.idCanal = :canal AND dd.deal.statusDeal != 'CA' "
								+ "AND dd.statusDetalleDeal != 'CA' AND dd.mnemonico NOT IN (:mnemonicos) "
								+ "AND dd.divisa.idDivisa != 'MXN' "
								+ "AND dd.divisa.tipo != 'M'");
				q.setParameter("fInicio", fInicio);
				q.setParameter("fFin", fFin);
				q.setParameter("canal", idCanal);
				q.setParameterList("mnemonicos", listaMnemonicos,
						new StringType());
				return q.list();
			}
		});
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findMnemonicosUtilidadesIxeDirecto(List)
	 *      .
	 */
	public List findMnemonicosUtilidadesIxeDirecto(List mnemonicos) {
		final List listaMnemonicos = mnemonicos;
		return getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException {
				Query q = session
						.createQuery("SELECT p.valor FROM ParametroSica AS p "
								+ "WHERE p.idParametro IN (:mnemonicos)");
				q.setParameterList("mnemonicos", listaMnemonicos,
						new StringType());
				return q.list();
			}
		});
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findDealsTeller().
	 */
	public List findDealsTeller() {
		return getHibernateTemplate()
				.find(
						"FROM Deal AS d WHERE (d.eventosDeal like ? OR "
								+ "d.eventosDeal LIKE ?) AND d.statusDeal != ? ORDER BY d.idDeal",
						new Object[] { getMascaraEventos(true),
								getMascaraEventos(false),
								Deal.STATUS_DEAL_CANCELADO });
	}

	/**
	 * Asigna el valor de la mascara de los eventos.
	 * 
	 * @param vespertina
	 *            Define si el evento es un evento vespertino.
	 * @return String
	 */
	private String getMascaraEventos(boolean vespertina) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < Deal.EV_IND_TIPO_CAPTURA; i++) {
			sb.append("_");
		}
		sb.append(vespertina ? Deal.EV_VESPERTINO : Deal.EV_NOCTURNO);
		sb.append("%");
		return sb.toString();
	}

	/**
	 * @see SicaServiceData#generarAutorizacionSobreprecioSucursales(com.ixe.ods.sica.model.Deal)
	 *      .
	 */
	public void generarAutorizacionSobreprecioSucursales(Deal deal) {
		wfCrearActividad(Actividad.PROC_DEAL_NORMAL,
				Actividad.ACT_DN_SOBREPRECIO, deal, null);
	}

	/**
	 * Regresa la referencia al bean dealService, deal application context.
	 * 
	 * @return DealService.
	 */
	protected DealService getDealService() {
		return (DealService) _appContext.getBean("dealService");
	}

	/**
	 * @see SicaServiceData#findTiposCambioTellerByFecha(Date).
	 */
	public List findTiposCambioTellerByFecha(Date fecha) {
		return getHibernateTemplate().find(
				"FROM TcMinMaxTeller AS tct WHERE tct.fecha "
						+ "BETWEEN ? AND ? ORDER BY tct.cierre, tct.fecha",
				new Object[] { DateUtils.inicioDia(fecha),
						DateUtils.finDia(fecha) });
	}

	/**
	 * @see SicaServiceData#findCombinacionDivisa(String, String).
	 */
	public CombinacionDivisa findCombinacionDivisa(String idDivisa,
			String idContraDivisa) {
		List lista = getHibernateTemplate()
				.find(
						"FROM CombinacionDivisa AS tcd "
								+ "WHERE tcd.id.divisa.idDivisa = ? and tcd.id.contraDivisa.idDivisa = ?",
						new String[] { idDivisa, idContraDivisa });
		return lista.size() == 0 ? null : (CombinacionDivisa) lista.get(0);
	}

	/**
	 * @see SicaServiceData#findIdGrupoTrabajoByIdUsuario(Integer);
	 */
	public GrupoTrabajo findIdGrupoTrabajoByIdUsuario(Integer idUsuario) {
		List lista = getHibernateTemplate().find(
				"FROM GrupoTrabajoPromotor AS gpoTra "
						+ "WHERE gpoTra.id.promotor.idPersona = ?",
				new String[] { idUsuario.toString() });
		return lista.size() == 0 ? null : ((GrupoTrabajoPromotor) lista.get(0))
				.getId().getGrupoTrabajo();
	}

	/**
	 * @see SicaServiceData#findComplementoDatosByNoCuenta(String);
	 */
	public ComplementoDatos findComplementoDatosByNoCuenta(String noCuenta) {
		List lista = getHibernateTemplate().find(
				"FROM ComplementoDatos AS comDat "
						+ "WHERE comDat.noCuenta = ?",
				new String[] { noCuenta });
		return lista.size() == 0 ? null : (ComplementoDatos) lista.get(0);
	}

	/**
	 * @see SicaServiceData#findComplementoDatosByIfe(String, String, String,
	 *      String);
	 */
	public ComplementoDatos findComplementoDatosByIfe(String ifeFolio) {
		List lista = getHibernateTemplate().find(
				"FROM ComplementoDatos AS comDat "
						+ "WHERE comDat.folioIfe = ?",
				new String[] { ifeFolio });
		return lista.size() == 0 ? null : (ComplementoDatos) lista.get(0);
	}

	/**
	 * @see SicaServiceData#findComplementoDatosByPasaporteAndIdBupPais(String,
	 *      String);
	 */
	public ComplementoDatos findComplementoDatosByPasaporteAndIdBupPais(
			String pasaporte, String idBupPais) {
		List lista = getHibernateTemplate()
				.find(
						"FROM ComplementoDatos AS comDat "
								+ "WHERE comDat.noPasaporte = ? AND comDat.nacionalidad = ?",
						new String[] { pasaporte, idBupPais });
		return lista.size() == 0 ? null : (ComplementoDatos) lista.get(0);
	}

	/**
	 * @see SicaServiceData#findCombinacionGrupoTrabajo(String, String).
	 */
	public GrupoTrabajoPromotor findCombinacionGrupoTrabajo(String idPromotor,
			String idGrupoTrabajo) {
		List lista = getHibernateTemplate()
				.find(
						"FROM GrupoTrabajoPromotor AS gpoTra WHERE "
								+ "gpoTra.id.promotor.idPersona = ? and gpoTra.id.grupoTrabajo.idGrupoTrabajo = ?",
						new String[] { idPromotor, idGrupoTrabajo });
		return lista.size() == 0 ? null : (GrupoTrabajoPromotor) lista.get(0);
	}

	/**
	 * @see SicaServiceData#findAllDealsPendientesLiquidarT4(Date).
	 */
	public List findAllDealsPendientesLiquidarT4(Date fechaLiquidacion) {
		Map fechas = getFechasFuturo(fechaLiquidacion, STR_96HRS);
		return findDealsDetallesPendientes((Date) fechas.get(Constantes.CASH),
				DateUtils.finDia((Date) fechas.get(STR_96HRS)));
	}

	/**
	 * Obtiene las fechas futuro TOM, SPOT, 72HRS, 96HRS verificando d&iacute;as
	 * festivos y fines de semana de acuerdo a la fecha que se proporcione e
	 * indicando hasta que fecha se desea obtener.
	 * 
	 * @param fechaCash
	 * @param fechaFuturo
	 * @return
	 */
	public Map getFechasFuturo(Date fechaCash, String fechaFuturo) {
		Map map = new HashMap();
		List dfs = proximosDiasFestivos(fechaCash);
		Calendar gc = new GregorianCalendar();
		gc.setTime(DateUtils.inicioDia(fechaCash));
		List fechas = new ArrayList();
		if (Constantes.TOM.equals(fechaFuturo)) {
			fechas = obtieneFechas(dfs, gc, 2);
			map.put(Constantes.CASH, fechas.get(0));
			map.put(Constantes.TOM, fechas.get(1));
		} else if (Constantes.SPOT.equals(fechaFuturo)) {
			fechas = obtieneFechas(dfs, gc, 3);
			map.put(Constantes.CASH, fechas.get(0));
			map.put(Constantes.TOM, fechas.get(1));
			map.put(Constantes.SPOT, fechas.get(2));
		} else if (STR_72HRS.equals(fechaFuturo)) {
			fechas = obtieneFechas(dfs, gc, 4);
			map.put(Constantes.CASH, fechas.get(0));
			map.put(Constantes.TOM, fechas.get(1));
			map.put(Constantes.SPOT, fechas.get(2));
			map.put(STR_72HRS, fechas.get(3));
		} else if (STR_96HRS.equals(fechaFuturo)) {
			fechas = obtieneFechas(dfs, gc, 5);
			map.put(Constantes.CASH, fechas.get(0));
			map.put(Constantes.TOM, fechas.get(1));
			map.put(Constantes.SPOT, fechas.get(2));
			map.put(STR_72HRS, fechas.get(3));
			map.put(STR_96HRS, fechas.get(4));
		}
		return map;
	}

	/**
	 * Obtiene la lista de fechas futuras de acuerdo al caledario proporcionado,
	 * los dias festivos y el n&uacute;mero de fechas que se desea obtener.
	 * 
	 * @param diasFestivos
	 * @param gc
	 * @param numFechas
	 * @return List
	 */
	private List obtieneFechas(List diasFestivos, Calendar gc, int numFechas) {
		List fechas = new ArrayList();
		for (int i = 0; fechas.size() < numFechas; i++) {
			if (!isFechaFestiva(diasFestivos, gc.getTime())) {
				gc.setTime(DateUtils.finDia(gc.getTime()));
				fechas.add(gc.getTime());
			}
			gc.add(Calendar.DAY_OF_MONTH, 1);
		}
		return fechas;
	}

	/**
	 * Encuentra los pr&oacute;ximos d&iacute;as festivos a partir de la fecha
	 * cash y hasta una semana posterior.
	 * 
	 * @param fechaCash
	 *            La fecha Cash de referencia.
	 * @return List.
	 */
	private List proximosDiasFestivos(Date fechaCash) {
		Calendar gc = new GregorianCalendar();
		gc.setTime(fechaCash);
		gc.add(Calendar.DAY_OF_WEEK, 7);
		return getHibernateTemplate().find(
				"FROM FechaNoLaboral AS df WHERE df.fecha BETWEEN ? "
						+ "AND ? ORDER BY df.fecha",
				new Object[] { fechaCash, gc.getTime() });
	}

	/**
	 * Permite saber si una fecha es festiva.
	 * 
	 * @param dfs
	 *            La lista de fechas no laboradas
	 * @param f
	 *            La fecha que se consulta
	 * @return Regresa <code>true</code> si la fecha es festiva.
	 */
	private boolean isFechaFestiva(List dfs, Date f) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(f);
		int diaDeLaSemana = gc.get(Calendar.DAY_OF_WEEK);
		if (diaDeLaSemana == Calendar.SATURDAY
				|| diaDeLaSemana == Calendar.SUNDAY) {
			return true;
		}
		for (Iterator it = dfs.iterator(); it.hasNext();) {
			FechaNoLaboral df = (FechaNoLaboral) it.next();
			if (DATE_FORMAT.format(f).equals(DATE_FORMAT.format(df.getFecha()))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @see SicaServiceData#findAllFechasInhabilesEua().
	 */
	public List findAllFechasInhabilesEua() {
		return getHibernateTemplate().find(
				"FROM FechaInhabilEua AS fi "
						+ "ORDER BY fi.idFechaInhabilEua.fecha");
	}

	/**
	 * @see SicaServiceData#findTokenTellerById(String).
	 */
	public TokenTeller findTokenTellerById(String token) throws SicaException {
		List tokenList = getHibernateTemplate()
				.find("FROM TokenTeller AS tt " + "WHERE tt.idTokenTeller = ?",
						token);

		return tokenList.isEmpty() ? null : (TokenTeller) tokenList.get(0);
	}

	/**
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
	 * @see com.ixe.ods.sica.services.DealService#crearDealDetalle(com.ixe.ods.sica.model.Deal,
	 *      boolean, String, String, com.ixe.ods.sica.model.Divisa, double,
	 *      double, double, int, com.ixe.ods.sica.model.FactorDivisa, int,
	 *      String, java.util.Date[], boolean, com.ixe.ods.sica.model.Estado,
	 *      com.ixe.ods.sica.model.LimitesRestriccionOperacion).
	 * @return DealDetalle.
	 * @throws SicaException
	 *             Si algo sale mal.
	 */
	public DealDetalle crearDealDetalle(Deal deal, boolean recibimos,
			String fechaValor, String claveFormaLiquidacion, Divisa div,
			double tipoCambioMesa, double monto, double tipoCambio,
			Double precioReferenciaMidSpot, Double precioReferenciaSpot,
			Double factorDivisa, int idSpread, Integer idPrecioReferencia,
			Integer idFactorDivisa, String mnemonico, Date[] fechasValor,
			boolean montoMaximoExcedido, Estado estado,
			LimitesRestriccionOperacion limRestOper) throws SicaException {
		String tv = deal.getTipoValor() != null ? deal.getTipoValor().trim()
				: deal.getTipoValor();
		fechaValor = fechaValor.trim();
		if (monto < 0.01) {
			throw new SicaException("El monto no puede ser menor a 0.01.");
		}
		List detsContraparte = recibimos ? deal.getEntregamos() : deal
				.getRecibimos();
		if (Constantes.EFECTIVO.equals(claveFormaLiquidacion)) {
			DealHelper.validarEfectivo(detsContraparte, div.getIdDivisa());
		}
		if (tv == null || fechaValor.equals(tv)) {
			deal.setTipoValor(fechaValor);
			if (deal.getFechaLiquidacion() == null) {
				String tpVal = deal.getTipoValor();
				deal
						.setFechaLiquidacion(tpVal.equals(Constantes.VFUT) ? fechasValor[Num.I_4]
								: tpVal.equals(Constantes.HR72) ? fechasValor[Num.I_3]
										: tpVal.equals(Constantes.SPOT) ? fechasValor[2]
												: tpVal.equals(Constantes.TOM) ? fechasValor[1]
														: fechasValor[0]);
			}
			DealDetalle det = new DealDetalle(deal, recibimos,
					claveFormaLiquidacion, div, monto, tipoCambio, DealHelper
							.getSiguienteFolioDetalle(deal));
			det.setMnemonico(mnemonico);
			det.setDeal(deal);
			det.setPrecioReferenciaMidSpot(precioReferenciaMidSpot);
			det.setPrecioReferenciaSpot(precioReferenciaSpot);
			det.setFactorDivisa(factorDivisa);
			det.setIdSpread(idSpread);
			det.setIdPrecioReferencia(idPrecioReferencia);
			det.setIdFactorDivisa(idFactorDivisa);
			det.setTipoCambioMesa(tipoCambioMesa);
			det.setTipoCambio(tipoCambio);
			det.setMesaCambio(deal.getCanalMesa().getMesaCambio());
			if (!deal.isInterbancario() && !deal.isDeSucursal()
					&& !deal.isDealBalanceo() && !deal.isAutorizadoTce()) {
				if (montoMaximoExcedido) {
					det.setEvento(Deal.EV_SOLICITUD, DealDetalle.EV_IND_MONTO);
				}
				if (estado.getIdEstado() == Estado.ESTADO_OPERACION_RESTRINGIDA) {
					det
							.setEvento(Deal.EV_SOLICITUD,
									DealDetalle.EV_IND_HORARIO);
				}
			}
			if (limRestOper != null
					&& !limRestOper.getTipoExcepcion().equals(
							PersonaListaBlanca.EXCEPCION_SHCP)) {
				validaLimitesRestriccionOperacion(deal, det,
						claveFormaLiquidacion, limRestOper, null, null, deal
								.getFechaLiquidacion(), false);
			}
			if (!deal.isInterbancario()
					&& Constantes.TRAVELER_CHECKS.equals(claveFormaLiquidacion)
					&& deal.getContratoSica() != null && !det.isRecibimos()) {
				double montoChviaj = findMontoChequesViajeroUsdFechaLiquidacion(
						deal.getContratoSica().getNoCuenta(), deal
								.getFechaLiquidacion());
				if (det.getMontoUSD() + montoChviaj > 25000) {
					det.getDeal().getDetalles().remove(det);
					det.setDeal(null);
					if ("0.00".equals(Constantes.MONEY_FORMAT
							.format(25000 - montoChviaj))) {
						throw new SicaException(
								"El cliente ya no puede operar Cheques de "
										+ "Viajero durante el resto del d\u00eda.");
					} else {
						throw new SicaException(
								"El cliente solo puede operar "
										+ Constantes.MONEY_FORMAT
												.format(25000 - montoChviaj)
										+ " USD en Cheques de Viajero durante el resto del d\u00eda.");
					}
				}
			}
			return det;
		} else {
			throw new SicaException(
					"El primer detalle fue definido Fecha Valor "
							+ tv
							+ ". No se permiten detalles con diferentes Fecha Valor.");
		}
	}

	/**
	 * @see SicaServiceData#getProductosEnRestriccion().
	 */
	public List getProductosEnRestriccion() {
		List productosEnRestriccion = new ArrayList();
		ParametroSica psProdsRest = (ParametroSica) find(ParametroSica.class,
				ParametroSica.PRODUCTOS_EN_RESTRICCION);
		if (psProdsRest != null) {
			String[] valores = psProdsRest.getValor().split("\\,");
			for (int x = 0; x < valores.length; x++) {
				productosEnRestriccion.add(valores[x]);
			}
		}
		return productosEnRestriccion;
	}

	/**
	 * @see SicaServiceData#validaLimitesRestriccionOperacion(Deal, DealDetalle,
	 *      String, LimitesRestriccionOperacion, String, Double, String, String,
	 *      IUsuario).
	 */
	public void validaLimitesRestriccionOperacion(Deal deal, DealDetalle det,
			String claveFormaLiquidacion,
			LimitesRestriccionOperacion limRestOper, String modificacion,
			Double nvoMonto, Date fechaLiquidacion, boolean isCapturaRapidaNeteo) {
		List productosEnRestriccion = getProductosEnRestriccion();

		// Si existen productos en restriccion
		if (limRestOper != null && productosEnRestriccion != null
				&& limRestOper.getListaLimitesNormales() != null
				&& !productosEnRestriccion.isEmpty()
				&& !limRestOper.getListaLimitesNormales().isEmpty()) {

			// Si no es modificacion de deal
			if (modificacion == null || modificacion.equals("")) {
				// Por cada limite que se tenga
				for (Iterator it1 = limRestOper.getListaLimitesNormales()
						.iterator(); it1.hasNext();) {
					String divisaEnRestriccion = ((LimiteEfectivo) it1.next())
							.getDivisa();

					// Por cada producto en restriccion
					for (Iterator it2 = productosEnRestriccion.iterator(); it2
							.hasNext();) {
						String productoEnRestriccion = (String) it2.next();

						// Si es COMPRA, no interbancario, es producto en
						// retriccion, tiene contrato
						// SICA y es divisa en restriccion
						if (!deal.isInterbancario()
								&& productoEnRestriccion
										.equals(claveFormaLiquidacion)
								&& deal.getContratoSica() != null
								&& det.isRecibimos()
								&& det.getDivisa().getIdDivisa().equals(
										divisaEnRestriccion)) {

							// Se calculan los acumulados diarios y mensuales
							double acumDiarioProdsRestCpra = findAcumuladoUsdCompraVentaDiarioMensual(
									deal.getContratoSica().getNoCuenta(),
									fechaLiquidacion, true,
									divisaEnRestriccion,
									productosEnRestriccion, true);
							double acumMensualProdsRestCpra = findAcumuladoUsdCompraVentaDiarioMensual(
									deal.getContratoSica().getNoCuenta(),
									fechaLiquidacion, false,
									divisaEnRestriccion,
									productosEnRestriccion, true);

							// Si el limite maximo mensual es diferente de null
							// y la suma del
							// acumulado mensual + la compra actual es mayor al
							// limite mensual
							if ((limRestOper.getLimiteMaximoMensual(
									divisaEnRestriccion, true) != null)
									&& ((det.getMontoUSD() + acumMensualProdsRestCpra) > limRestOper
											.getLimiteMaximoMensual(
													divisaEnRestriccion, true)
											.doubleValue())) {
								if (!isCapturaRapidaNeteo) {
									det.getDeal().getDetalles().remove(det);
									det.setDeal(null);
								}

								// Si el limite maximo menusal - acumulado
								// mensual es menor a 0 o
								// es igual a 0.00
								if (((limRestOper.getLimiteMaximoMensual(
										divisaEnRestriccion, true)
										.doubleValue() - acumMensualProdsRestCpra) < 0.0)
										|| ("0.00"
												.equals(Constantes.MONEY_FORMAT
														.format(limRestOper
																.getLimiteMaximoMensual(
																		divisaEnRestriccion,
																		true)
																.doubleValue()
																- acumMensualProdsRestCpra)))) {

									// Si limite maximo mensual es igual a 0
									if (limRestOper.getLimiteMaximoMensual(
											divisaEnRestriccion, true)
											.doubleValue() == 0.0) {
										throw new SicaException(
												"LimEfect: De acuerdo a la"
														+ " normatividad este cliente no puede"
														+ " operar "
														+ productosEnRestriccion
																.toString()
																.substring(
																		1,
																		productosEnRestriccion
																				.toString()
																				.length() - 1)
														+ " en la compra para la divisa "
														+ divisaEnRestriccion
														+ ".");
									} else {
										throw new SicaException(
												"LimEfect: De acuerdo a la"
														+ " normatividad el cliente ya no puede"
														+ " operar "
														+ productosEnRestriccion
																.toString()
																.substring(
																		1,
																		productosEnRestriccion
																				.toString()
																				.length() - 1)
														+ " durante el resto del mes en la"
														+ " compra para la divisa "
														+ divisaEnRestriccion
														+ ".");
									}
								} else {
									throw new SicaException(
											"LimEfect: De acuerdo a la "
													+ "normatividad vigente, se puede operar "
													+ Constantes.MONEY_FORMAT
															.format(limRestOper
																	.getLimiteMaximoDiario(
																			divisaEnRestriccion,
																			true))
													+ " "
													+ divisaEnRestriccion
													+ " Diarios "
													+ "y "
													+ Constantes.MONEY_FORMAT
															.format(limRestOper
																	.getLimiteMaximoMensual(
																			divisaEnRestriccion,
																			true))
													+ " "
													+ divisaEnRestriccion
													+ " Mensuales "
													+ "como m\u00e1ximo en "
													+ productosEnRestriccion
															.toString()
															.substring(
																	1,
																	productosEnRestriccion
																			.toString()
																			.length() - 1)
													+ " para la COMPRA de "
													+ "divisa "
													+ divisaEnRestriccion
													+ ". \n Este cliente "
													+ "s\u00f3lo puede operar "
													+ Constantes.MONEY_FORMAT
															.format(limRestOper
																	.getLimiteMaximoMensual(
																			divisaEnRestriccion,
																			true)
																	.doubleValue()
																	- acumMensualProdsRestCpra)
													+ " durante el resto del mes.");
									// throw new
									// SicaException("LimEfect: El cliente solo puede"
									// +
									// " operar " +
									// Constantes.MONEY_FORMAT.format(limRestOper.
									// getLimiteMaximoMensual(divisaEnRestriccion,
									// true).
									// doubleValue() - acumMensualProdsRestCpra)
									// + " en " +
									// productosEnRestriccion.toString().substring(1,
									// productosEnRestriccion.toString().length()-1)
									// +
									// " durante el resto del mes en la compra para"
									// +
									// " la divisa " + divisaEnRestriccion +
									// ".");
								}
							}
							// Si el limite maximo diario es diferente de null y
							// la suma del
							// acumulado diario + la compra actual es mayor al
							// limite diario
							else if ((limRestOper.getLimiteMaximoDiario(
									divisaEnRestriccion, true) != null)
									&& ((det.getMontoUSD() + acumDiarioProdsRestCpra) > limRestOper
											.getLimiteMaximoDiario(
													divisaEnRestriccion, true)
											.doubleValue())) {
								if (!isCapturaRapidaNeteo) {
									det.getDeal().getDetalles().remove(det);
									det.setDeal(null);
								}
								if (((limRestOper.getLimiteMaximoDiario(
										divisaEnRestriccion, true)
										.doubleValue() - acumDiarioProdsRestCpra) < 0.0)
										|| ("0.00"
												.equals(Constantes.MONEY_FORMAT
														.format(limRestOper
																.getLimiteMaximoDiario(
																		divisaEnRestriccion,
																		true)
																.doubleValue()
																- acumDiarioProdsRestCpra)))) {
									if (limRestOper.getLimiteMaximoDiario(
											divisaEnRestriccion, true)
											.doubleValue() == 0.0) {
										throw new SicaException(
												"LimEfect: De acuerdo a la"
														+ " normatividad este cliente no puede"
														+ " operar "
														+ productosEnRestriccion
																.toString()
																.substring(
																		1,
																		productosEnRestriccion
																				.toString()
																				.length() - 1)
														+ " en la compra para la divisa "
														+ divisaEnRestriccion
														+ ".");
									} else {
										throw new SicaException(
												"LimEfect: De acuerdo a la"
														+ " normatividad el cliente ya no puede"
														+ " operar "
														+ productosEnRestriccion
																.toString()
																.substring(
																		1,
																		productosEnRestriccion
																				.toString()
																				.length() - 1)
														+ " durante el resto del d\u00eda en"
														+ " la compra para la divisa "
														+ divisaEnRestriccion
														+ ".");
									}
								} else {
									throw new SicaException(
											"LimEfect: De acuerdo a la "
													+ "normatividad vigente, se puede operar "
													+ Constantes.MONEY_FORMAT
															.format(limRestOper
																	.getLimiteMaximoDiario(
																			divisaEnRestriccion,
																			true))
													+ " "
													+ divisaEnRestriccion
													+ " Diarios "
													+ "y "
													+ Constantes.MONEY_FORMAT
															.format(limRestOper
																	.getLimiteMaximoMensual(
																			divisaEnRestriccion,
																			true))
													+ " "
													+ divisaEnRestriccion
													+ " Mensuales "
													+ " como m\u00e1ximo en "
													+ productosEnRestriccion
															.toString()
															.substring(
																	1,
																	productosEnRestriccion
																			.toString()
																			.length() - 1)
													+ " para la COMPRA de "
													+ "divisa "
													+ divisaEnRestriccion
													+ ". \n Este cliente "
													+ "s\u00f3lo puede operar "
													+ Constantes.MONEY_FORMAT
															.format(limRestOper
																	.getLimiteMaximoDiario(
																			divisaEnRestriccion,
																			true)
																	.doubleValue()
																	- acumDiarioProdsRestCpra)
													+ " durante el resto del d\u00eda.");
									// throw new
									// SicaException("LimEfect: El cliente solo puede"
									// +
									// " operar " +
									// Constantes.MONEY_FORMAT.format(limRestOper.
									// getLimiteMaximoDiario(divisaEnRestriccion,
									// true).
									// doubleValue() - acumDiarioProdsRestCpra)
									// + " en " +
									// productosEnRestriccion.toString().substring(1,
									// productosEnRestriccion.toString().length()-1)
									// +
									// " durante el resto del d\u00eda en la compra para"
									// +
									// " la divisa " + divisaEnRestriccion +
									// ".");
								}
							}
						}
						// Si es VENTA, no interbancario, es producto en
						// retriccion, tiene contrato
						// SICA y es divisa en restriccion
						else if (!deal.isInterbancario()
								&& productoEnRestriccion
										.equals(claveFormaLiquidacion)
								&& deal.getContratoSica() != null
								&& !det.isRecibimos()
								&& det.getDivisa().getIdDivisa().equals(
										divisaEnRestriccion)) {
							double acumDiarioProdsRestVta = findAcumuladoUsdCompraVentaDiarioMensual(
									deal.getContratoSica().getNoCuenta(),
									fechaLiquidacion, true,
									divisaEnRestriccion,
									productosEnRestriccion, false);
							double acumMensualProdsRestVta = findAcumuladoUsdCompraVentaDiarioMensual(
									deal.getContratoSica().getNoCuenta(),
									fechaLiquidacion, false,
									divisaEnRestriccion,
									productosEnRestriccion, false);
							if ((limRestOper.getLimiteMaximoMensual(
									divisaEnRestriccion, false) != null)
									&& ((det.getMontoUSD() + acumMensualProdsRestVta) > limRestOper
											.getLimiteMaximoMensual(
													divisaEnRestriccion, false)
											.doubleValue())) {
								if (!isCapturaRapidaNeteo) {
									det.getDeal().getDetalles().remove(det);
									det.setDeal(null);
								}
								if (((limRestOper.getLimiteMaximoMensual(
										divisaEnRestriccion, false)
										.doubleValue() - acumMensualProdsRestVta) < 0.0)
										|| ("0.00"
												.equals(Constantes.MONEY_FORMAT
														.format(limRestOper
																.getLimiteMaximoMensual(
																		divisaEnRestriccion,
																		false)
																.doubleValue()
																- acumMensualProdsRestVta)))) {
									if (limRestOper.getLimiteMaximoMensual(
											divisaEnRestriccion, false)
											.doubleValue() == 0.0) {
										throw new SicaException(
												"LimEfect: De acuerdo a la"
														+ " normatividad este cliente no puede"
														+ " operar "
														+ productosEnRestriccion
																.toString()
																.substring(
																		1,
																		productosEnRestriccion
																				.toString()
																				.length() - 1)
														+ " en la venta para la divisa "
														+ divisaEnRestriccion
														+ ".");
									} else {
										throw new SicaException(
												"LimEfect: De acuerdo a la"
														+ " normatividad el cliente ya no puede"
														+ " operar "
														+ productosEnRestriccion
																.toString()
																.substring(
																		1,
																		productosEnRestriccion
																				.toString()
																				.length() - 1)
														+ " durante el resto del mes en la"
														+ " venta para la divisa "
														+ divisaEnRestriccion
														+ ".");
									}
								} else {
									throw new SicaException(
											"LimEfect: De acuerdo a la "
													+ "normatividad vigente, se puede operar "
													+ Constantes.MONEY_FORMAT
															.format(limRestOper
																	.getLimiteMaximoDiario(
																			divisaEnRestriccion,
																			false))
													+ " "
													+ divisaEnRestriccion
													+ " Diarios "
													+ "y "
													+ Constantes.MONEY_FORMAT
															.format(limRestOper
																	.getLimiteMaximoMensual(
																			divisaEnRestriccion,
																			false))
													+ " "
													+ divisaEnRestriccion
													+ " Mensuales "
													+ "como m\u00e1ximo en "
													+ productosEnRestriccion
															.toString()
															.substring(
																	1,
																	productosEnRestriccion
																			.toString()
																			.length() - 1)
													+ " para la VENTA de "
													+ "divisa "
													+ divisaEnRestriccion
													+ ". \n Este cliente "
													+ "s\u00f3lo puede operar "
													+ Constantes.MONEY_FORMAT
															.format(limRestOper
																	.getLimiteMaximoMensual(
																			divisaEnRestriccion,
																			false)
																	.doubleValue()
																	- acumMensualProdsRestVta)
													+ " durante el resto del mes.");
									// throw new
									// SicaException("LimEfect: El cliente solo puede"
									// +
									// " operar " +
									// Constantes.MONEY_FORMAT.format(limRestOper.
									// getLimiteMaximoMensual(divisaEnRestriccion,
									// false).
									// doubleValue() - acumMensualProdsRestVta)
									// + " en " +
									// productosEnRestriccion.toString().substring(1,
									// productosEnRestriccion.toString().length()-1)
									// +
									// " durante el resto del mes en la venta para"
									// +
									// " la divisa " + divisaEnRestriccion +
									// ".");
								}
							} else if ((limRestOper.getLimiteMaximoDiario(
									divisaEnRestriccion, false) != null)
									&& ((det.getMontoUSD() + acumDiarioProdsRestVta) > limRestOper
											.getLimiteMaximoDiario(
													divisaEnRestriccion, false)
											.doubleValue())) {
								if (!isCapturaRapidaNeteo) {
									det.getDeal().getDetalles().remove(det);
									det.setDeal(null);
								}
								if (((limRestOper.getLimiteMaximoDiario(
										divisaEnRestriccion, false)
										.doubleValue() - acumDiarioProdsRestVta) < 0.0)
										|| ("0.00"
												.equals(Constantes.MONEY_FORMAT
														.format(limRestOper
																.getLimiteMaximoDiario(
																		divisaEnRestriccion,
																		false)
																.doubleValue()
																- acumDiarioProdsRestVta)))) {
									if (limRestOper.getLimiteMaximoDiario(
											divisaEnRestriccion, false)
											.doubleValue() == 0.0) {
										throw new SicaException(
												"LimEfect: De acuerdo a la"
														+ " normatividad este cliente no puede"
														+ " operar "
														+ productosEnRestriccion
																.toString()
																.substring(
																		1,
																		productosEnRestriccion
																				.toString()
																				.length() - 1)
														+ " en la venta para la divisa "
														+ divisaEnRestriccion
														+ ".");
									} else {
										throw new SicaException(
												"LimEfect: De acuerdo a la"
														+ " normatividad el cliente ya no puede"
														+ " operar "
														+ productosEnRestriccion
																.toString()
																.substring(
																		1,
																		productosEnRestriccion
																				.toString()
																				.length() - 1)
														+ " durante el resto del d\u00eda en"
														+ " la venta para la divisa "
														+ divisaEnRestriccion
														+ ".");
									}
								} else {
									throw new SicaException(
											"LimEfect: De acuerdo a la"
													+ " normatividad vigente, se puede operar "
													+ Constantes.MONEY_FORMAT
															.format(limRestOper
																	.getLimiteMaximoDiario(
																			divisaEnRestriccion,
																			false))
													+ " "
													+ divisaEnRestriccion
													+ " Diarios y "
													+ Constantes.MONEY_FORMAT
															.format(limRestOper
																	.getLimiteMaximoMensual(
																			divisaEnRestriccion,
																			false))
													+ " "
													+ divisaEnRestriccion
													+ " Mensuales"
													+ " como m\u00e1ximo en "
													+ productosEnRestriccion
															.toString()
															.substring(
																	1,
																	productosEnRestriccion
																			.toString()
																			.length() - 1)
													+ " para la VENTA de divisa "
													+ divisaEnRestriccion
													+ ". \n Este cliente s\u00f3lo"
													+ " puede operar "
													+ Constantes.MONEY_FORMAT
															.format(limRestOper
																	.getLimiteMaximoDiario(
																			divisaEnRestriccion,
																			false)
																	.doubleValue()
																	- acumDiarioProdsRestVta)
													+ " durante el resto del d\u00eda.");

									// throw new
									// SicaException("LimEfect: El cliente solo puede"
									// +
									// " operar " +
									// Constantes.MONEY_FORMAT.format(limRestOper.
									// getLimiteMaximoDiario(divisaEnRestriccion,
									// false).
									// doubleValue() - acumDiarioProdsRestVta) +
									// " en " +
									// productosEnRestriccion.toString().substring(1,
									// productosEnRestriccion.toString().length()-1)
									// +
									// " durante el resto del d\u00eda en la venta para"
									// +
									// " la divisa " + divisaEnRestriccion +
									// ".");
								}
							}
						}
					}
				}
			} else if (modificacion != null && !modificacion.equals("")) {
				double monto = 0.0;
				if (modificacion.equals(Constantes.MODIF_MTO)) {
					monto = nvoMonto.doubleValue();
				} else {
					monto = det.getMontoUSD();
				}
				for (Iterator it1 = limRestOper.getListaLimitesNormales()
						.iterator(); it1.hasNext();) {
					String divisaEnRestriccion = ((LimiteEfectivo) it1.next())
							.getDivisa();
					for (Iterator it2 = productosEnRestriccion.iterator(); it2
							.hasNext();) {
						String productoEnRestriccion = (String) it2.next();
						if (!deal.isInterbancario()
								&& productoEnRestriccion
										.equals(claveFormaLiquidacion)
								&& deal.getContratoSica() != null
								&& det.isRecibimos()
								&& det.getDivisa().getIdDivisa().equals(
										divisaEnRestriccion)) {
							double acumDiarioProdsRestCpra = findAcumuladoUsdCompraVentaDiarioMensual(
									deal.getContratoSica().getNoCuenta(),
									fechaLiquidacion, true,
									divisaEnRestriccion,
									productosEnRestriccion, true);
							double acumMensualProdsRestCpra = findAcumuladoUsdCompraVentaDiarioMensual(
									deal.getContratoSica().getNoCuenta(),
									fechaLiquidacion, false,
									divisaEnRestriccion,
									productosEnRestriccion, true);
							if ((limRestOper.getLimiteMaximoMensual(
									divisaEnRestriccion, true) != null)
									&& ((monto + acumMensualProdsRestCpra) > limRestOper
											.getLimiteMaximoMensual(
													divisaEnRestriccion, true)
											.doubleValue())) {
								if (((limRestOper.getLimiteMaximoMensual(
										divisaEnRestriccion, true)
										.doubleValue() - acumMensualProdsRestCpra) < 0.0)
										|| ("0.00"
												.equals(Constantes.MONEY_FORMAT
														.format(limRestOper
																.getLimiteMaximoMensual(
																		divisaEnRestriccion,
																		true)
																.doubleValue()
																- acumMensualProdsRestCpra)))) {
									throw new SicaException(
											"LimEfect: De acuerdo a la"
													+ " normatividad el cliente ya no puede"
													+ " operar "
													+ productosEnRestriccion
															.toString()
															.substring(
																	1,
																	productosEnRestriccion
																			.toString()
																			.length() - 1)
													+ " durante el resto del mes en la compra"
													+ " para la divisa "
													+ divisaEnRestriccion + ".");
								} else {
									throw new SicaException(
											"LimEfect: De acuerdo a la "
													+ "normatividad vigente, se puede operar "
													+ Constantes.MONEY_FORMAT
															.format(limRestOper
																	.getLimiteMaximoDiario(
																			divisaEnRestriccion,
																			true))
													+ " "
													+ divisaEnRestriccion
													+ " Diarios "
													+ "y "
													+ Constantes.MONEY_FORMAT
															.format(limRestOper
																	.getLimiteMaximoMensual(
																			divisaEnRestriccion,
																			true))
													+ " "
													+ divisaEnRestriccion
													+ " Mensuales "
													+ "como m\u00e1ximo en "
													+ productosEnRestriccion
															.toString()
															.substring(
																	1,
																	productosEnRestriccion
																			.toString()
																			.length() - 1)
													+ " para la COMPRA de "
													+ "divisa "
													+ divisaEnRestriccion
													+ ". \n Este cliente "
													+ "s\u00f3lo puede operar "
													+ Constantes.MONEY_FORMAT
															.format(limRestOper
																	.getLimiteMaximoMensual(
																			divisaEnRestriccion,
																			true)
																	.doubleValue()
																	- acumMensualProdsRestCpra)
													+ " durante el resto del mes.");
									// throw new
									// SicaException("LimEfect: El cliente solo puede"
									// +
									// " operar " +
									// Constantes.MONEY_FORMAT.format(limRestOper.
									// getLimiteMaximoMensual(divisaEnRestriccion,
									// true).
									// doubleValue() - acumMensualProdsRestCpra)
									// + " en " +
									// productosEnRestriccion.toString().substring(1,
									// productosEnRestriccion.toString().length()-1)
									// +
									// " durante el resto del mes en la compra para"
									// +
									// " la divisa " + divisaEnRestriccion +
									// ".");

								}
							} else if ((limRestOper.getLimiteMaximoDiario(
									divisaEnRestriccion, true) != null)
									&& ((monto + acumDiarioProdsRestCpra) > limRestOper
											.getLimiteMaximoDiario(
													divisaEnRestriccion, true)
											.doubleValue())) {
								if (((limRestOper.getLimiteMaximoDiario(
										divisaEnRestriccion, true)
										.doubleValue() - acumDiarioProdsRestCpra) < 0.0)
										|| ("0.00"
												.equals(Constantes.MONEY_FORMAT
														.format(limRestOper
																.getLimiteMaximoDiario(
																		divisaEnRestriccion,
																		true)
																.doubleValue()
																- acumDiarioProdsRestCpra)))) {
									throw new SicaException(
											"LimEfect: De acuerdo a la"
													+ " normatividad el cliente ya no puede"
													+ " operar "
													+ productosEnRestriccion
															.toString()
															.substring(
																	1,
																	productosEnRestriccion
																			.toString()
																			.length() - 1)
													+ " durante el resto del d\u00eda en la"
													+ " compra para"
													+ " la divisa "
													+ divisaEnRestriccion + ".");
								} else {
									throw new SicaException(
											"LimEfect: De acuerdo a la "
													+ "normatividad vigente, se puede operar "
													+ Constantes.MONEY_FORMAT
															.format(limRestOper
																	.getLimiteMaximoDiario(
																			divisaEnRestriccion,
																			true))
													+ " "
													+ divisaEnRestriccion
													+ " Diarios "
													+ "y "
													+ Constantes.MONEY_FORMAT
															.format(limRestOper
																	.getLimiteMaximoMensual(
																			divisaEnRestriccion,
																			true))
													+ " "
													+ divisaEnRestriccion
													+ " Mensuales "
													+ " como m\u00e1ximo en "
													+ productosEnRestriccion
															.toString()
															.substring(
																	1,
																	productosEnRestriccion
																			.toString()
																			.length() - 1)
													+ " para la COMPRA de "
													+ "divisa "
													+ divisaEnRestriccion
													+ ". \n Este cliente "
													+ "s\u00f3lo puede operar "
													+ Constantes.MONEY_FORMAT
															.format(limRestOper
																	.getLimiteMaximoDiario(
																			divisaEnRestriccion,
																			true)
																	.doubleValue()
																	- acumDiarioProdsRestCpra)
													+ " durante el resto del d\u00eda.");
									// throw new
									// SicaException("LimEfect: El cliente solo puede"
									// +
									// " operar " +
									// Constantes.MONEY_FORMAT.format(limRestOper.
									// getLimiteMaximoDiario(divisaEnRestriccion,
									// true).
									// doubleValue() - acumDiarioProdsRestCpra)
									// + " en " +
									// productosEnRestriccion.toString().substring(1,
									// productosEnRestriccion.toString().length()-1)
									// +
									// " durante el resto del d\u00eda en la compra para"
									// +
									// " la divisa " + divisaEnRestriccion +
									// ".");
								}
							}
						} else if (!deal.isInterbancario()
								&& productoEnRestriccion
										.equals(claveFormaLiquidacion)
								&& deal.getContratoSica() != null
								&& !det.isRecibimos()
								&& det.getDivisa().getIdDivisa().equals(
										divisaEnRestriccion)) {
							double acumDiarioProdsRestVta = findAcumuladoUsdCompraVentaDiarioMensual(
									deal.getContratoSica().getNoCuenta(),
									fechaLiquidacion, true,
									divisaEnRestriccion,
									productosEnRestriccion, false);
							double acumMensualProdsRestVta = findAcumuladoUsdCompraVentaDiarioMensual(
									deal.getContratoSica().getNoCuenta(),
									fechaLiquidacion, false,
									divisaEnRestriccion,
									productosEnRestriccion, false);
							if ((limRestOper.getLimiteMaximoMensual(
									divisaEnRestriccion, false) != null)
									&& ((monto + acumMensualProdsRestVta) > limRestOper
											.getLimiteMaximoMensual(
													divisaEnRestriccion, false)
											.doubleValue())) {
								if (((limRestOper.getLimiteMaximoMensual(
										divisaEnRestriccion, false)
										.doubleValue() - acumMensualProdsRestVta) < 0.0)
										|| ("0.00"
												.equals(Constantes.MONEY_FORMAT
														.format(limRestOper
																.getLimiteMaximoMensual(
																		divisaEnRestriccion,
																		false)
																.doubleValue()
																- acumMensualProdsRestVta)))) {
									throw new SicaException(
											"LimEfect: De acuerdo a la"
													+ " normatividad el cliente ya no puede"
													+ " operar "
													+ productosEnRestriccion
															.toString()
															.substring(
																	1,
																	productosEnRestriccion
																			.toString()
																			.length() - 1)
													+ " durante el resto del mes en la venta"
													+ " para la divisa "
													+ divisaEnRestriccion + ".");
								} else {
									throw new SicaException(
											"LimEfect: De acuerdo a la "
													+ "normatividad vigente, se puede operar "
													+ Constantes.MONEY_FORMAT
															.format(limRestOper
																	.getLimiteMaximoDiario(
																			divisaEnRestriccion,
																			false))
													+ " "
													+ divisaEnRestriccion
													+ " Diarios "
													+ "y "
													+ Constantes.MONEY_FORMAT
															.format(limRestOper
																	.getLimiteMaximoMensual(
																			divisaEnRestriccion,
																			false))
													+ " "
													+ divisaEnRestriccion
													+ " Mensuales "
													+ " como m\u00e1ximo en "
													+ productosEnRestriccion
															.toString()
															.substring(
																	1,
																	productosEnRestriccion
																			.toString()
																			.length() - 1)
													+ " para la VENTA de "
													+ "divisa "
													+ divisaEnRestriccion
													+ ". \n Este cliente "
													+ "s\u00f3lo puede operar "
													+ Constantes.MONEY_FORMAT
															.format(limRestOper
																	.getLimiteMaximoMensual(
																			divisaEnRestriccion,
																			false)
																	.doubleValue()
																	- acumMensualProdsRestVta)
													+ " durante el resto del mes.");
									// throw new
									// SicaException("LimEfect: El cliente solo puede"
									// +
									// " operar " +
									// Constantes.MONEY_FORMAT.format(limRestOper.
									// getLimiteMaximoMensual(divisaEnRestriccion,
									// false).
									// doubleValue() - acumMensualProdsRestVta)
									// + " en " +
									// productosEnRestriccion.toString().substring(1,
									// productosEnRestriccion.toString().length()-1)
									// +
									// " durante el resto del mes en la venta para"
									// +
									// " la divisa " + divisaEnRestriccion +
									// ".");

								}
							} else if ((limRestOper.getLimiteMaximoDiario(
									divisaEnRestriccion, false) != null)
									&& ((monto + acumDiarioProdsRestVta) > limRestOper
											.getLimiteMaximoDiario(
													divisaEnRestriccion, false)
											.doubleValue())) {
								if (((limRestOper.getLimiteMaximoDiario(
										divisaEnRestriccion, false)
										.doubleValue() - acumDiarioProdsRestVta) < 0.0)
										|| ("0.00"
												.equals(Constantes.MONEY_FORMAT
														.format(limRestOper
																.getLimiteMaximoDiario(
																		divisaEnRestriccion,
																		false)
																.doubleValue()
																- acumDiarioProdsRestVta)))) {
									throw new SicaException(
											"LimEfect: De acuerdo a la"
													+ " normatividad el cliente ya no puede"
													+ " operar "
													+ productosEnRestriccion
															.toString()
															.substring(
																	1,
																	productosEnRestriccion
																			.toString()
																			.length() - 1)
													+ " durante el resto del d\u00eda en la"
													+ " venta para la divisa "
													+ divisaEnRestriccion + ".");
								} else {
									throw new SicaException(
											"LimEfect: De acuerdo a la"
													+ " normatividad vigente, se puede operar "
													+ Constantes.MONEY_FORMAT
															.format(limRestOper
																	.getLimiteMaximoDiario(
																			divisaEnRestriccion,
																			false))
													+ " "
													+ divisaEnRestriccion
													+ " Diarios y "
													+ Constantes.MONEY_FORMAT
															.format(limRestOper
																	.getLimiteMaximoMensual(
																			divisaEnRestriccion,
																			false))
													+ " "
													+ divisaEnRestriccion
													+ " Mensuales"
													+ " como m\u00e1ximo en "
													+ productosEnRestriccion
															.toString()
															.substring(
																	1,
																	productosEnRestriccion
																			.toString()
																			.length() - 1)
													+ " para la VENTA de divisa "
													+ divisaEnRestriccion
													+ ". \n Este cliente s\u00f3lo"
													+ " puede operar "
													+ Constantes.MONEY_FORMAT
															.format(limRestOper
																	.getLimiteMaximoDiario(
																			divisaEnRestriccion,
																			false)
																	.doubleValue()
																	- acumDiarioProdsRestVta)
													+ " durante el resto del d\u00eda.");

									// throw new
									// SicaException("LimEfect: El cliente solo puede"
									// +
									// " operar " +
									// Constantes.MONEY_FORMAT.format(limRestOper.
									// getLimiteMaximoDiario(divisaEnRestriccion,
									// false).
									// doubleValue() - acumDiarioProdsRestVta) +
									// " en " +
									// productosEnRestriccion.toString().substring(1,
									// productosEnRestriccion.toString().length()-1)
									// +
									// " durante el resto del d\u00eda en la venta para"
									// +
									// " la divisa " + divisaEnRestriccion +
									// ".");
								}
							}
						}
					}
				}
			}
		}
	}

	/**
	 * @see SicaServiceData#findActividadByIdDealPosicion(int, String).
	 */
	public Actividad findActividadByIdDealPosicion(int idDealPosicion,
			String nombreActividad) {
		List list = getHibernateTemplate()
				.find(
						"FROM Actividad AS a INNER JOIN FETCH "
								+ "a.dealDetalle AS dd INNER JOIN FETCH dd.deal AS d WHERE dd.idDealPosicion = ? "
								+ "AND a.status = ? AND a.nombreActividad = ?",
						new Object[] { new Integer(idDealPosicion),
								Actividad.STATUS_PENDIENTE, nombreActividad });
		return list.isEmpty() ? null : (Actividad) list.get(0);
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#modificarTcmEnActividadModProducto(int,
	 *      double)
	 */
	public void modificarTcmEnActividadModProducto(int idActividad, double tcm) {
		if (Double.isNaN(tcm)) {
			throw new SicaException(
					"El valor del tipo de cambio debe ser num\u00e9rico");
		}
		Actividad act = (Actividad) find(Actividad.class, new Integer(
				idActividad));
		String resultado = act.getResultado();
		String[] items = resultado.split("\\|");
		items[1] = "" + tcm;
		StringBuffer strBff = new StringBuffer();
		for (int i = 0; i < items.length; i++) {
			strBff.append(items[i]).append("|");
		}
		act.setResultado(strBff.toString());
		update(act);
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findLimitesByCriteria(Divisa,Boolean,String,Boolean,String,Boolean)
	 */
	public LimiteEfectivo findLimitesByCriteria(final Divisa div,
			final Boolean compra, final String tipoPersona,
			final Boolean cliente, final String tipoZona, final Boolean mexicano) {
		List resultado = getHibernateTemplate().executeFind(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException {
						StringBuffer query = new StringBuffer();
						query.append("FROM LimiteEfectivo AS le ");
						query.append("WHERE le.divisa = :divisa ");
						query.append("AND le.compra = :compra ");
						query.append("AND le.tipoPersona = :tipoPersona ");
						if (cliente != null) {
							query.append("AND (le.cliente = :cliente ");
							query.append("OR le.cliente is null) ");
						}
						if (tipoZona != null) {
							query.append("AND (le.tipoZona = :tipoZona ");
							query.append("OR le.tipoZona is null) ");
						}
						if (mexicano != null) {
							query.append("AND (le.mexicano = :mexicano ");
							query.append("OR le.mexicano is null) ");
						}
						Query q = session.createQuery(query.toString());
						q.setParameter("divisa", div.getIdDivisa());
						q.setParameter("compra",
								compra.booleanValue() ? SiNoType.TRUE
										: SiNoType.FALSE);
						q.setParameter("tipoPersona", tipoPersona);
						if (cliente != null) {
							q.setParameter("cliente",
									cliente.booleanValue() ? SiNoType.TRUE
											: SiNoType.FALSE);
						}
						if (tipoZona != null) {
							q.setParameter("tipoZona", tipoZona);
						}
						if (mexicano != null) {
							q.setParameter("mexicano",
									mexicano.booleanValue() ? SiNoType.TRUE
											: SiNoType.FALSE);
						}
						return q.list();
					}
				});
		return resultado != null && resultado.size() > 0 ? (LimiteEfectivo) resultado
				.get(0)
				: null;
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findLimitesRestriccionOperacion(String,
	 *      Boolean, String, Boolean)
	 */
	public List findLimitesRestriccionOperacion(final String tipoPersona,
			final Boolean cliente, final String tipoZona, final Boolean mexicano) {
		List resultado = getHibernateTemplate().executeFind(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException {
						StringBuffer query = new StringBuffer();
						query.append("FROM LimiteEfectivo AS le ");
						query.append("WHERE le.tipoPersona = :tipoPersona ");
						if (cliente != null) {
							query.append("AND (le.cliente = :cliente ");
							query.append("OR le.cliente is null) ");
						}
						if (tipoZona != null) {
							query.append("AND (le.tipoZona = :tipoZona ");
							query.append("OR le.tipoZona is null) ");
						}
						if (mexicano != null) {
							query.append("AND (le.mexicano = :mexicano ");
							query.append("OR le.mexicano is null) ");
						}
						Query q = session.createQuery(query.toString());
						q.setParameter("tipoPersona", tipoPersona);
						if (cliente != null) {
							q.setParameter("cliente",
									cliente.booleanValue() ? SiNoType.TRUE
											: SiNoType.FALSE);
						}
						if (tipoZona != null) {
							q.setParameter("tipoZona", tipoZona);
						}
						if (mexicano != null) {
							q.setParameter("mexicano",
									mexicano.booleanValue() ? SiNoType.TRUE
											: SiNoType.FALSE);
						}
						return q.list();
					}
				});
		return resultado;
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findPersonasListaBlanca()
	 */
	public List findPersonasListaBlanca() {
		return getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException {
				Query q = session
						.createQuery(""
								+ "select new com.ixe.ods.sica.model.PersonaListaBlanca("
								+ "p.noCuenta, p.tipoExcepcion, "
								+ "p.limiteDiario, p.limiteMensual, p.observaciones, "
								+ "p.fechaCreacion, p.fechaUltimaModificacion, "
								+ "p.usuarioModificacion, c.id.persona.nombreCorto) "
								+ "FROM PersonaListaBlanca AS p, PersonaCuentaRol AS c "
								+ "WHERE p.noCuenta = c.id.cuenta.noCuenta "
								+ "AND c.id.rol.idRol = 'TIT' AND c.status.estatus = 'VIG'");
				return q.list();
			}
		});
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findPersonaListaBlanca(String)
	 */
	public PersonaListaBlanca findPersonaListaBlanca(final String contrato) {
		List resultado = getHibernateTemplate().executeFind(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException {
						Query q = session
								.createQuery(""
										+ "select new com.ixe.ods.sica.model.PersonaListaBlanca("
										+ "p.noCuenta, p.tipoExcepcion, "
										+ "p.limiteDiario, p.limiteMensual, p.observaciones, "
										+ "p.fechaCreacion, p.fechaUltimaModificacion, "
										+ "p.usuarioModificacion, c.id.persona.nombreCorto) "
										+ "FROM PersonaListaBlanca AS p, PersonaCuentaRol AS c "
										+ "WHERE p.noCuenta = :noCuenta "
										+ "AND p.noCuenta = c.id.cuenta.noCuenta "
										+ "AND c.id.rol.idRol = 'TIT' AND c.status.estatus = 'VIG'");
						q.setParameter("noCuenta", contrato);
						return q.list();
					}
				});
		return resultado != null && resultado.size() > 0 ? (PersonaListaBlanca) resultado
				.get(0)
				: null;
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findRimsByContratoSica(String)
	 */
	public List findRimsByContratoSica(final String contrato) {
		List resultado = getHibernateTemplate().executeFind(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException {
						Query q = session
								.createQuery(""
										+ "SELECT pr.rimNo "
										+ "FROM PersonaRim AS pr, PersonaCuentaRol AS c "
										+ "WHERE c.id.cuenta.noCuenta = :noCuenta "
										+ "AND pr.persona.id = c.id.persona.id "
										+ "AND c.id.rol.idRol = 'TIT' AND c.status.estatus = 'VIG'");
						q.setParameter("noCuenta", contrato);
						return q.list();
					}
				});
		return resultado != null && resultado.size() > 0 ? resultado : null;
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findMunicipioEstado(String,String)
	 */
	public BupMunicipio findMunicipioEstado(final String idMunicipio,
			final String idEstado) {
		List resultado = getHibernateTemplate().executeFind(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException {
						Query q = session.createQuery("FROM BupMunicipio AS m "
								+ "WHERE m.id.idMunicipio = :idMunicipio "
								+ "AND m.id.estado.idEstado = :idEstado");
						q.setParameter("idMunicipio", idMunicipio);
						q.setParameter("idEstado", idEstado);
						return q.list();
					}
				});
		return resultado != null && resultado.size() > 0 ? (BupMunicipio) resultado
				.get(0)
				: null;
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findCodigosPostalesBup(String,String)
	 */
	public List findCodigosPostalesBup(final String idMunicipio,
			final String idEstado) {
		List resultado = getHibernateTemplate().executeFind(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException {
						Query q = session
								.createQuery("SELECT bcp.id.codigoPostal "
										+ "FROM BupCodigoPostal AS bcp "
										+ "WHERE bcp.id.municipio.id.idMunicipio =:idMunicipio "
										+ "AND bcp.id.municipio.id.estado.idEstado =:idEstado "
										+ "AND bcp.id.codigoPostal IS NOT NULL "
										+ "GROUP BY bcp.id.municipio.id.idMunicipio, "
										+ "bcp.id.municipio.id.estado.idEstado, bcp.id.codigoPostal");
						q.setParameter("idMunicipio", idMunicipio);
						q.setParameter("idEstado", idEstado);
						return q.list();
					}
				});
		return resultado != null ? resultado : null;
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findMunicipioListaBlanca(String,String)
	 */
	public MunicipioListaBlanca findMunicipioListaBlanca(
			final String idMunicipio, final String idEstado) {
		List resultado = getHibernateTemplate().executeFind(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException {
						Query q = session
								.createQuery("FROM MunicipioListaBlanca AS m "
										+ "WHERE m.id.idMunicipio = :idMunicipio "
										+ "AND m.id.idEstado = :idEstado");
						q.setParameter("idMunicipio", idMunicipio);
						q.setParameter("idEstado", idEstado);
						return q.list();
					}
				});
		return resultado != null && resultado.size() > 0 ? (MunicipioListaBlanca) resultado
				.get(0)
				: null;
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findCodigoPostalListaBlanca(String)
	 */
	public CodigoPostalListaBlanca findCodigoPostalListaBlanca(final String cp,
			boolean clienteOrSucursal) {
		List resultado = getHibernateTemplate().executeFind(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException {
						Query q = session
								.createQuery(QUERY_CODIGOS_POSTALES_SELECT
										+ "AND bcp.id.codigoPostal =:codigoPostal "
										+ QUERY_CODIGOS_POSTALES_GROUPBY);
						q.setParameter("codigoPostal", cp);
						return q.list();
					}
				});
		if (resultado.size() == 0) {
			return null;
		}
		if (resultado.size() > 1) {
			throw new SicaException(
					"No se puede realizar la operaci\u00f3n, porque el "
							+ "c\u00f3digo postal "
							+ (clienteOrSucursal ? "del cliente "
									: "de la sucursal ")
							+ "se encuentra duplicado.");
		}
		return (CodigoPostalListaBlanca) resultado.get(0);
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findCodigoPostalListaBlanca(String,
	 *      String)
	 */
	public CodigoPostalListaBlanca findCodigoPostalListaBlanca(
			final String idMunicipio, final String idEstado) {
		List resultado = getHibernateTemplate().executeFind(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException {
						Query q = session
								.createQuery(QUERY_CODIGOS_POSTALES_SELECT
										+ "AND mlb.id.idMunicipio =:idMunicipio "
										+ "AND mlb.id.idEstado =:idEstado "
										+ QUERY_CODIGOS_POSTALES_GROUPBY);
						q.setParameter("idMunicipio", idMunicipio);
						q.setParameter("idEstado", idEstado);
						return q.list();
					}
				});
		return resultado != null && resultado.size() > 0 ? (CodigoPostalListaBlanca) resultado
				.get(0)
				: null;
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findallCodigoPostalListaBlanca()
	 */
	public List findAllCodigoPostalListaBlanca() {
		List resultado = getHibernateTemplate().executeFind(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException {
						Query q = session
								.createQuery(QUERY_CODIGOS_POSTALES_SELECT
										+ QUERY_CODIGOS_POSTALES_GROUPBY);
						return q.list();
					}
				});
		return resultado.isEmpty() ? null : resultado;
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findCheckSumCodigosPostalesListBlanca()
	 */
	public Long findCheckSumCodigosPostalesListBlanca() {
		try {
			String queryFronteriza = "SELECT sum(distinct cp.id.codigoPostal) "
					+ "FROM MunicipioListaBlanca mlb, BupCodigoPostal cp "
					+ "WHERE mlb.id.idEstado = cp.id.municipio.id.estado.idEstado "
					+ "AND mlb.id.idMunicipio = cp.id.municipio.id.idMunicipio "
					+ "AND mlb.zonaFronteriza = 'S'";
			String resultadoA = (String) getHibernateTemplate().find(
					queryFronteriza).get(0);
			String queryTuristica = "SELECT sum(distinct cp.id.codigoPostal) "
					+ "FROM MunicipioListaBlanca mlb, BupCodigoPostal cp "
					+ "WHERE mlb.id.idEstado = cp.id.municipio.id.estado.idEstado "
					+ "AND mlb.id.idMunicipio = cp.id.municipio.id.idMunicipio "
					+ "AND mlb.zonaTuristica = 'S'";
			String resultadoB = (String) getHibernateTemplate().find(
					queryTuristica).get(0);
			BigDecimal sumaFronteriza = new BigDecimal(resultadoA);
			BigDecimal sumaTuristica = new BigDecimal(resultadoB);
			return new Long(sumaFronteriza.longValue() * 10
					+ sumaTuristica.longValue());
		} catch (Exception e) {
			return new Long(0);
		}
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findAllCanalesOrdered().
	 */
	public List findAllCanalesOrdered() {
		return getHibernateTemplate().find(
				"FROM Canal AS c ORDER BY c.sucursal.idSucursal, c.nombre");
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findAllSucursalesWithCanal()
	 */
	public List findAllSucursalesWithCanal() {
		return getHibernateTemplate().find(
				"FROM Sucursal AS s " + "WHERE s IN (" + "SELECT c.sucursal "
						+ "FROM Canal AS c " + "WHERE c.sucursal IS NOT NULL"
						+ ")");
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findDivisiones()
	 */
	public List findDivisiones() {
		return getHibernateTemplate().findByNamedQuery("findDivisiones");
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findPlazasByDivision()
	 */
	public List findPlazasByDivision(Integer idDivision) {
		return getHibernateTemplate().findByNamedQuery("findPlazasByDivision",
				new Object[] { idDivision });
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findZonasByPlaza()
	 */
	public List findZonasByPlaza(Integer idPlaza) {
		return getHibernateTemplate().findByNamedQuery("findZonasByPlaza",
				new Object[] { idPlaza });
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findSucursalesByZona()
	 */
	public List findSucursalesByZona(Integer idZona) {
		return getHibernateTemplate().findByNamedQuery("findSucursalesByZona",
				new Object[] { idZona });
	}

	/**
	 * Regresa la referencia al bean bitacoraEnviadasDao.
	 * 
	 * @return BitacoraEnviadasDao.
	 */
	protected BitacoraEnviadasDao getBitacoraEnviadasDao() {
		return (BitacoraEnviadasDao) _appContext.getBean("bitacoraEnviadasDao");
	}

	/**
	 * Regresa las diferentes divisas y si es compre o venta (compra = S)
	 * 
	 * @return una lista 0 = divisa, 1= compra
	 */
	public List getAllDivisasFromLimitesEfectivo() {

		String query = "SELECT DISTINCT le.divisa, le.compra "
				+ "FROM LimiteEfectivo le ";

		return getHibernateTemplate().find(query);
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#getAllSistemaTce()
	 */
	public List getAllSistemaTce() {
		List sistemaList = getHibernateTemplate().findByNamedQuery(
				"findAllSistemas");
		if (sistemaList == null)
			sistemaList = new ArrayList();
		return sistemaList;
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findMensajesTceHoy()
	 */
	public List findMensajesTceHoy() {
		try {
			Date fechaHoy = new Date();
			Date fechaInicio = DateUtils.inicioDia(fechaHoy);
			Date fechaFin = DateUtils.finDia(fechaHoy);
			List mensajesList = getHibernateTemplate().findByNamedQuery(
					"findMensajesTceByFechaCreacion",
					new Object[] { fechaInicio, fechaFin });
			if (mensajesList == null)
				mensajesList = new ArrayList();
			return mensajesList;
		} catch (DataAccessException e) {
			logger.error("Error al obtener mensajes de hoy", e);
			throw new SicaException("Error al obtener mensajes de hoy");
		}
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#saveMensajeTce(MensajeTce)
	 */
	public void saveMensajeTce(MensajeTce mensaje) {
		try {
			getHibernateTemplate().save(mensaje);
		} catch (DataAccessException e) {
			logger.error("Error al guardar mensaje", e);
			throw new SicaException("Error al guardar mensaje");
		}
	}

	public List findClienteByIdPersona(Integer idPersona)
	{
		return getHibernateTemplate().findByNamedQuery("findClienteByIdPersona", new Object[] { idPersona });
	}
	
	public List findClienteByIdPersonaAndIdCliente(Integer idPersona, Integer idCliente)
	{
		return getHibernateTemplate().find("SELECT c FROM Cliente AS c WHERE c.idCliente = ? AND c.idPersona = ? ", 
				                       new Object[] { idCliente, idPersona });
	}
	
	/**
	 * Guarda un cliente con sus respectivos datos y regresa el id del cliente
	 * recien creado
	 */
	public Integer storeDatosClienteBanorte(Integer idPersona, String empresa,
			String sic, String origen, String opera, String cuentaCheques,
			int usuarioCreacion) {

		Cliente cliente = new Cliente();
		cliente.setIdPersona(idPersona);
		cliente.setIdEmpresa(empresa);
		cliente.setSic(sic);
		cliente.setSucursalOrigen(origen);
		cliente.setSucursalOperacion(opera);
		cliente.setCuentaCheques(cuentaCheques);
		cliente.setUsuarioCreacion(usuarioCreacion);
		cliente.setFechaCreacion(new Date());

		return (Integer) getHibernateTemplate().save(cliente);

	}
	
	/**
	 * Busca un cliente por su clave de idPersona
	 * 
	 * @param idPersona
	 * @return
	 */
	public List findIdClienteByIdPersona(Integer idPersona) {
		return getHibernateTemplate().findByNamedQuery(
				"findClienteByIdPersona", new Object[] { idPersona });

	}

	/**
	 * Guarda un contrato con sus respectivos datos y regresa el id del contrato
	 * recien creado
	 */
	public Integer storeDatosContrato(Integer idPersona, String cuenta,
			int usuarioCreacion) {
		// Se revisa si ya se creo un cliente en sica para esa persona
		List clientePersona = findIdClienteByIdPersona(idPersona);
		Integer idCliente = null;
		if (clientePersona == null || clientePersona.size() != 1) {
			idCliente = storeDatosClienteBanorte(idPersona,
					EmpresaCambios.ID_EMPRESA_IXE, "", "", "", "",
					usuarioCreacion);
		} else {
			idCliente = ((Cliente) clientePersona.get(0)).getIdCliente();
		}
		Contrato contrato = new Contrato();
		contrato.setIdCliente(idCliente);
		contrato.setNoCuenta(cuenta);
		contrato.setUsuarioCreacion(usuarioCreacion);
		contrato.setFechaCreacion(new Date());
		contrato.setIdBloqueo(new Integer (TipoBloqueo.RESTRINGIDO_POR_APERTURA));//JDCH 29/05/2013 Se pasa el parametro en 1 ya que todos se deben aperturar con status Bloqueado.
		return (Integer) getHibernateTemplate().save(contrato);
	}
	
	/**
	 * Busca en sc_cuenta_altamira
	 * 		a partir de un numero de cuenta.
	 * 
	 * @param cuentaAltamira el numero de cuenta Altamira
	 * @return <code>List</code> con objetos 
	 * 		   <code>CuentaAltamira</code>
	 */
	public List findCuentaAltamiraByNoCuenta (String cuentaAltamira) {
		
		return getHibernateTemplate().findByNamedQuery(
				"findCuentaAltamiraByNoCuenta", new Object[]{ cuentaAltamira }); 
	}
	
	/**
	 * Inserta un registro en sc_cuenta_altamira
	 * 
	 * @param infoCuentaAltamiraDto con los 
	 * 	 datos del registro a dar de alta.
	 * @param claveUsuario  el usuario firmado al 
	 * 		sistema.
	 */
	public void storeCuentaAltamira(InfoCuentaAltamiraDto infoCuentaAltamiraDto, String claveUsuario) {
		
		CuentaAltamira datosCuenta = new CuentaAltamira();
		
		datosCuenta.setCuentaAltamira(infoCuentaAltamiraDto.getNumeroCuenta());
		datosCuenta.setIdPersona(infoCuentaAltamiraDto.getIdPersona());
		datosCuenta.setSic(new Integer(infoCuentaAltamiraDto.getNumeroCliente()));
		datosCuenta.setCr(infoCuentaAltamiraDto.getCr());
		datosCuenta.setUsuario(claveUsuario);
		datosCuenta.setFechaAlta(new Date());
		datosCuenta.setFechaUltMod(new Date());
		
		getHibernateTemplate().save(datosCuenta);
	}
	
	/**
	 *  Actualiza un registro de sc_cuenta_altamira
	 * @param cuentaAltamira los datos del registro 
	 * 		a actualizar.
	 * @param idPersona el identificador de la persona
	 * 		asociado a la cuenta.
	 * @param claveUsuario el usuario que realiza la 
	 * 		actualizaciďż˝n del registro.
	 */
	public void updateCuentaAltamira(CuentaAltamira cuentaAltamira, int idPersona, String claveUsuario) {
		
		cuentaAltamira.setIdPersona(new Integer(idPersona));
		cuentaAltamira.setUsuario(claveUsuario);
		
		getHibernateTemplate().update(cuentaAltamira);
	}

    /**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#cambiarEstadoContrato(). JDCH 30/05/2013
	 */
	public void cambiarEstadoContrato(String _contratoSica, int _idBloqueoNuevo, int usuarioUltMod) {
		Contrato contrato = getContratoCorto(_contratoSica);
		contrato.setIdBloqueo(new Integer(_idBloqueoNuevo));
		contrato.setFechaUltMod(new Timestamp(new Date().getTime()));
		contrato.setUsuarioUltMod(usuarioUltMod);
		update(contrato);
	}
	
	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#getContratoCorto(). JDCH 30/05/2013
	 */
	public Contrato getContratoCorto(String _contratoSica) {
		List contrato = getHibernateTemplate().findByNamedQuery(
				"findContratosByCuenta",_contratoSica);
		if (contrato.size() == 0){
			contrato = getHibernateTemplate().findByNamedQuery(
					"findContratosByCorto",_contratoSica);
			
		}
		if (contrato.size() != 1) {
			throw new SicaException(
					"No se pudo encontrar el contrato.");
		}	
		return (Contrato) contrato.get(0);
	}
	
	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#getContratoCorto(). JDCH 30/05/2013
	 */
	public Contrato getContratoCortoXCuenta(String _contratoSica) {
		List contrato = getHibernateTemplate().findByNamedQuery(
				"findContratosByCuenta",_contratoSica);
		
		if (contrato.size() == 0) return null;
		
		return (Contrato) contrato.get(0);
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findBloqueoByidBloqueo(int).
	 */
	public List findBloqueoByidBloqueo(Integer id_bloqueo) {
		return getHibernateTemplate().findByNamedQuery("findBloqueoByidBloqueo",
				id_bloqueo);
	}
	
	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#getPosicionesLogByFecha(). JDCH 22/09/2014
	 */
	public List getPosicionesLogByFecha(String _fecha) {
		
		
		List posicionLog = getHibernateTemplate().findByNamedQuery(
				"findPosicionesLogByFecha",new Object[] { _fecha });
		return posicionLog;
	}
	
	public List getPosicionesLogMayoresA(Integer idPosicionLog) {
		List posicionLog = getHibernateTemplate().findByNamedQuery(
				"findPosicionesLogMayoresA",idPosicionLog);
		return posicionLog;
	}
	
	
	
	/*
	 * (non-Javadoc)
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#actualizarProcesoEstandarizacionBUP(java.lang.Integer, java.lang.Integer)
	 */
	public boolean actualizarProcesoEstandarizacionBUP(Integer b_ilytics, Integer idPersona){
		return getExtraDao().updateProcesoEstandarizacionBupPersona(b_ilytics, idPersona);
	} 

	/**
     * @see SicaServiceData#isEntidadValida(String, String)
     */
    public boolean isEntidadValida(String entidad, String pais) {
    	return getExtraDao().isEntidadValida(entidad, pais);
    }
    
    /**
     * @see SicaServiceData#findPaisDireccion(String)
     */
    public String findPaisDireccion(Integer idDireccion) {
    	List direcciones = getHibernateTemplate().find("FROM Direccion d " +
    			"WHERE d.idDireccion = ? ", idDireccion);
    	
    	Direccion direccion = null;
    	
    	if(!direcciones.isEmpty()) {
    		direccion =  (Direccion) direcciones.get(0);
    		return direccion.getPaisAsString();
    	}
    	return null;
    }
	
    /**
     * @see SicaServiceData#findInfoFactura(int).
     */
    public InfoFactura findInfoFactura(int idDealDetalle) {
        List infoFacturas = getHibernateTemplate().find("FROM InfoFactura ifact " +
                "WHERE ifact.idDealDetalle = ?", new Integer(idDealDetalle));

        if (!infoFacturas.isEmpty()) {
            return (InfoFactura) infoFacturas.get(0);
        }
        return null;
    }
    
    /**
     * Consulta el catalogo 'sociedad mercantil'
     */ 
    public List findCatalogoSociedadMercantil()
    {
    	String query = "FROM RegulatorioSociedadMercantil sm order by sm.id";
    	List catalogo = getHibernateTemplate().find(query);
    	return catalogo;
    }
    
    /**
     * Consulta el catalogo 'Tipo identificador'
     */
    public List findCatalogoIdentificadorContraparte()
    {
    	String query = "FROM RegulatorioTipoIdentificador ti order by ti.id";
    	List catalogo = getHibernateTemplate().find(query);
    	return catalogo;
    }
    
    /**
     * Consulta el catalogo 'Actividad economica' (Tipo de contraparte)
     */
    public List findCatalogoActividadEconomica()
    {
    	String query = "FROM RegulatorioActividadEconomica ae order by ae.descripcion";
    	List catalogo = getHibernateTemplate().find(query);
    	return catalogo;
    }
    
    /**
     * Consulta el catalogo 'Sector industrial'
     */
    public List findCatalogoSectorIndustrial()
    {
    	String query = "FROM RegulatorioSectorIndustrial si order by si.descripcion";
    	List catalogo = getHibernateTemplate().find(query);
    	return catalogo;
    }
    
    /**
     * Consulta el catalogo 'Pais'
     */
    public List findCatalogoPais()
    {
    	String query = "FROM RegulatorioPais p order by p.nombrePais";
    	List catalogo = getHibernateTemplate().find(query);
    	return catalogo;
    }
    
    /**
     * Consulta el catalogo 'Tipo Relacion'
     */
    public List findCatalogoTipoRelacion()
    {
    	String query = "FROM RegulatorioTipoRelacion tp order by tp.descripcion";
    	List catalogo = getHibernateTemplate().find(query);
    	return catalogo;
    }
    
    /**
     * Consulta el catalogo 'Evento Relacion'
     */
    public List findCatalogoEventoRelacion()
    {
    	String query = "FROM RegulatorioEventoRelacion er order by er.descripcion";
    	List catalogo = getHibernateTemplate().find(query);
    	return catalogo;
    }
    
    public List findRegulatorioDatosPM(final Integer idPersona)
    {
    	String q = "SELECT MAX(pm.id) FROM RegulatorioDatosPM AS pm where pm.idPersona = ?";
    	List catalogo = null;
    	//List catalogo = getHibernateTemplate().find(ultimoRegistro,idPersona);
    	/*List catalogo = getHibernateTemplate().executeFind(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException {
						// "SELECT {n.*} FROM sc_reg_pm n WHERE ID_PERSONA = :idPersona"
						String sql = "select max(t.id) from RegulatorioDatosPM as t where t.idPersona = :IdPersona";
						return session.createQuery(sql).setParameter("IdPersona", idPersona).list();
					}
				});*/
    	
    	catalogo = getHibernateTemplate().find(q, idPersona);

        if(catalogo != null && catalogo.size() > 0)
        {
        	//RegulatorioDatosPM temp = (RegulatorioDatosPM)catalogo.get(0);
        	final Integer id = (Integer)catalogo.get(0);
        	if(id != null)
        	{
        		//catalogo = getHibernateTemplate().find(query, new Object[]{id});
        		catalogo = getHibernateTemplate().executeFind(
        				new HibernateCallback() {
        					public Object doInHibernate(Session session)
        							throws HibernateException {
        						String query = "FROM RegulatorioDatosPM as dpm " +
                				"LEFT JOIN FETCH dpm.sociedadMercantil " +
                				"LEFT JOIN FETCH dpm.tipoIdentificador " +
                				"LEFT JOIN FETCH dpm.actividadEconomica " +
                				//"LEFT JOIN FECTH dpm.sectorIndustrial " +
                				//"LEFT JOIN FECTH dpm.paisResidenciaContraparte " + 
                				//"LEFT JOIN FECTH dpm.paisControlContraparte " +
                		        "WHERE dpm.id = :idRegistro"; 
        						
        						return session.createQuery(query).setParameter("idRegistro", id).list();
        					}
        				});
        	}
        	else
            	catalogo = null;
        }
        
    	return catalogo;
    }
    
    public List findRegulatorioInstitucion(final Integer idPersona)
    {
    	String q = "select max(dri2.id) FROM RegulatorioInstitucion AS dri2 where dri2.idPersona = ?";
    	List catalogo = null;
    	//String ultimoRegistro = "select max(dri2.id) from RegulatorioInstitucion as dri2 where dri2.idPersona = ?";
    	//List catalogo = getHibernateTemplate().find(ultimoRegistro,idPersona);
    	/*List catalogo = getHibernateTemplate().executeFind(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException {
						// SELECT {n.*} FROM sc_reg_institucion n WHERE ID_PERSONA = :idPersona
						String sql = "select max(dri2.id) from RegulatorioInstitucion as dri2 where dri2.idPersona = :IdPersona";
						return session.createQuery(sql).setParameter("IdPersona", idPersona).list();
					}
				});*/
    	catalogo = getHibernateTemplate().find(q, idPersona);
    
        if(catalogo != null && catalogo.size() > 0)
        {
        	//RegulatorioInstitucion inst = (RegulatorioInstitucion)catalogo.get(0);
        	final Integer id = (Integer)catalogo.get(0);
        	if(id != null)
        	{
        		//catalogo = getHibernateTemplate().find(query, new Object[]{id});

        		catalogo = getHibernateTemplate().executeFind(
        				new HibernateCallback() {
        					public Object doInHibernate(Session session)
        							throws HibernateException {
        						String query = "FROM RegulatorioInstitucion as dri " +
        						"LEFT JOIN FETCH dri.tipoRelacion " +
        						"LEFT JOIN FETCH dri.eventoRelacion " +
        				        "WHERE dri.id = :idRegistro"; 
        						return session.createQuery(query).setParameter("idRegistro", id).list();
        					}
        				});
        	}
        	else 
        		catalogo = null;
        }
        
    	return catalogo;
    }
    
    public List findRegulatorioPerfil(final Integer idPersona)
    {
        String query = "FROM RegulatorioPerfil as p " +
        		        "where p.idPersona = ?"; // contratoSica = ?
        
        List catalogo = getHibernateTemplate().find(query, idPersona);
    	return catalogo;
    }
    
    /**
     * Actualiza el campo faltante (contrato sica) de la información regulatoria del cliente 
     * @param contratoSica
     */
    public void actualizarInfoRegulatoria(Integer idPersona, String contratoSica)
    {
    	if(idPersona != null && StringUtils.isNotEmpty(contratoSica))
    	{
    		RegulatorioPerfil perfil = null;
    		RegulatorioDatosPM pm = null;
    		RegulatorioInstitucion inst = null; 
    		List catalogo = null;
    		
    		String queryPM = "FROM RegulatorioDatosPM as p where p.idPersona = ?";
    		String queryPerfil = "FROM RegulatorioPerfil as p where p.idPersona = ?";
    		String queryInstitucion = "FROM RegulatorioInstitucion as p where p.idPersona = ?";
    		
    		try
    		{
    			 catalogo = getHibernateTemplate().find(queryPerfil, idPersona);
	       		 if(catalogo != null && catalogo.size() > 0)
	       		 {
	       			 perfil = (RegulatorioPerfil)catalogo.get(0);
	       			 perfil.setContratoSica(contratoSica);
	       			 update(perfil);
	       		 }
	       		 
	       		 catalogo = getHibernateTemplate().find(queryPM, idPersona);
	       		 if(catalogo != null && catalogo.size() > 0)
	       		 {
	       			 pm = (RegulatorioDatosPM)catalogo.get(0);
	       			 pm.setContratoSica(contratoSica);
	       			 update(pm);
	       		 }
	       		 
	       		 catalogo = getHibernateTemplate().find(queryInstitucion, idPersona);
	       		 if(catalogo != null && catalogo.size() > 0)
	       		 {
	       			 inst = (RegulatorioInstitucion)catalogo.get(0);
	       			 inst.setContratoSica(contratoSica);
	       			 update(inst);
	       		 }
    		}
    		catch(Exception e)
    		{
    			e.printStackTrace();
    		}
    	}
    }
    
    /**
     * Selecciona los contratos SICA de la información regulatoria de los clientes para incluirlos en los reportes regulatorios de BANXICO.
     * @param opcionBusqueda Constante que identifica que tipo de informacióon regulatoria debe consultarse:
     * BUSCAR_REGULATORIOS_PM o BUSCAR_REGULATORIOS_PARTE_RELACIONADA
     * @return List Con objetos de tipo Perfil
     */
    public List criteriosInclusionDatosRegulatorios(int opcionBusqueda)
    {
    	
    	String queryTodosLosCandidatos = null;
    	
    	if(opcionBusqueda == BUSCAR_REGULATORIOS_PM)
    		queryTodosLosCandidatos = "FROM RegulatorioPerfil as p where p.datosRegulatoriosActualizados = 'S' AND p.contratoSica IS NOT NULL";
    	else
    		queryTodosLosCandidatos = "FROM RegulatorioPerfil as p where p.regInstitucionActualizados = 'S' AND p.contratoSica IS NOT NULL";
    	
    	/*String queryFiltroContratoDesbloqueado = "SELECT p FROM RegulatorioPerfil as p, Contrato AS c  " +
    			                                 "where (p.regContraparteActualizado = 'S' OR p.regInstitucionActualizado = 'S') AND " +
    			                                 "      (p.contratoSica is not null AND p.contratoSica = c.noCuenta) AND " +
    			                                 "      (c.idBloqueo is null OR c.idBloqueo = ?)";
         //clientesConContratoCorto = getHibernateTemplate().find(queryFiltroContratoDesbloqueado, SIN_BLOQUEO);*/
    	List todosCandidatos = null;
    	List contratosIncluidos = null;
    	RegulatorioPerfil perfil = null;
    	Contrato contratoCorto = null;
        Integer idBloqueo = null;
    	
    	try
    	{
    		 // los registros de la información regulatoria deben haber sido dados de alta o actualizados
    		 todosCandidatos = getHibernateTemplate().find(queryTodosLosCandidatos);

	  		 if(todosCandidatos != null && todosCandidatos.size() > 0)
	  		 {
	  			contratosIncluidos = new ArrayList();
	  			 for(int indice = 0; indice < todosCandidatos.size(); indice++)
	  			 {
	  				 idBloqueo = null;
	  				 perfil = (RegulatorioPerfil)todosCandidatos.get(indice);
	  				 if(perfil != null)
	  				 {
	  					contratoCorto = getContratoCortoXCuenta(perfil.getContratoSica());
		  				 if(contratoCorto != null) // si el cliente tiene contrato corto
		  				 {
		  					 idBloqueo = contratoCorto.getIdBloqueo();
		  					 if(idBloqueo == null || idBloqueo.intValue() == TipoBloqueo.SIN_BLOQUEO) // y no está bloqueado
		  						contratosIncluidos.add(perfil); // se incluye en la lista de contratos para que se agreguen al reporte que corresponda
		  				 }
		  				 else
		  					contratosIncluidos.add(perfil); // el cliente no tiene contrato corto, se agrega por default
	  				 }
	  			 }
	  		 }
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    	
    	return contratosIncluidos;
    }
    
    /**
     * Servicio que consulta la información regulatoria especifica de los clientes
     * @param opcion Valor que indica que tipo de informacion regulatoria se consultará
     * @param contratosSica List de objetos String que representan los contratos SICA de los clientes
     * @return
     */
    public List consultarDatosRegulatorios(int opcion, final List contratosSica)
    {
    	List idsMaximos = new ArrayList();
    	List resultado = null;
    	String queryPM = "FROM RegulatorioDatosPM as p " +
                				"LEFT JOIN FETCH p.sociedadMercantil " +
                				"LEFT JOIN FETCH p.tipoIdentificador " +
                				"LEFT JOIN FETCH p.actividadEconomica " +
                				//"LEFT JOIN FECTH p.sectorIndustrial " +
                				//"LEFT JOIN FECTH p.paisResidenciaContraparte " + 
                				//"LEFT JOIN FECTH p.paisControlContraparte " +
                				"where p.id in (select max(t.id) from RegulatorioDatosPM as t where t.contratoSica in (:ContratosSica) group by t.contratoSica)";
    	
		String queryInstitucion = "FROM RegulatorioInstitucion as p " +
								  "LEFT JOIN FETCH p.tipoRelacion " +
								  "LEFT JOIN FETCH p.eventoRelacion " +
								  "where p.id in (select max(dri2.id) from RegulatorioInstitucion as dri2 where dri2.contratoSica in (:ContratosSica) group by dri2.contratoSica)";
		final String sql;
		
		if(opcion == BUSCAR_REGULATORIOS_PM)
			sql = queryPM;
		else if(opcion == BUSCAR_REGULATORIOS_PARTE_RELACIONADA)
			sql = queryInstitucion;
		else
			return resultado;
		
    	resultado = getHibernateTemplate().executeFind(new HibernateCallback() {
						public Object doInHibernate(Session session)
						throws HibernateException 
						{
							return session.createQuery(sql).setParameterList(
									"ContratosSica", contratosSica, new StringType()).list();
						}
					});
    	
    	return resultado;
    }
    
    /**
     * Servicio encargado de generar el backup de los reportes regulatorios de baxico para la fecha en curso
     * @param logger StringBuffer para almacenar las descripciones de los errores encontrados
     */
    public void generarBackupReportesRegulatorios(StringBuffer logger)
    {
    	List perfilesPersonasMorales = null;
		List perfilesParteRelacionada = null;
		RegulatorioPerfil perfil = null;
		List contratosSicaPM = null, contratosSicaPR = null;
		List clientesFiltradosPM = null;
		List clientesFiltradosPR = null;
		StringBuffer datosReporte = null, errores = new StringBuffer();
		ByteArrayOutputStream outputStreamReporte = null;
		GeneradorReportesRegulatoriosImpl generadorReportes = null;
		ParametroSica param = null;
		GeneralMailSender gms = null;
		String emailFrom = "ixecambios@ixe.com.mx";
		String emailTo[] = null;
		String emailAsunto = null;
		String emailMensaje = null;
		String sinDatosSeccion1 = null;
		String sinDatosSeccion4 = null;
		boolean enviarEmail = true; 
		
		int indice = 0;
		
		try
		{
			param = findParametro(ParametroSica.EMAILS_BACKUP_REGULATORIOS);
			if(param == null || StringUtils.isEmpty(param.getValor()))
				enviarEmail = false;
			else
				emailTo = param.getValor().split(",");
				
			emailAsunto = "Inicio de backup de reportes regulatorios.";
			emailMensaje = "Ha iniciado el proceso que generará el backup de reportes regulatorios de banxico. \n" +
					       "Este email es enviado por el Sistema SICA, por favor, no responda este email.";
			gms = (GeneralMailSender) _appContext.getBean("generalMailSender");
			
			if(enviarEmail)
			{
				try
				{
					gms.enviarMail(emailFrom, emailTo, null, emailAsunto, emailMensaje);
				}
				catch(Exception et)
				{
					et.printStackTrace();
					logger.append("---> No fue posible enviar el email de inicio de backup de reportes regulatorios..\n");
				}
			}
				
			
			logger.append("---> Inicia backup de reportes regulatorios..\n");
			//---------------------------------------------------------------------------------------------------------------
			logger.append("---> Buscando path del directorio de reportes regulatorios..\n");
			param = findParametro(ParametroSica.RUTA_REPORTES_REGULATORIOS);
			if(param == null || StringUtils.isEmpty(param.getValor()))
			{
				logger.append("---> El proceso de backup de reportes regulatorios ha terminado con errores debido " +
						      "a que no se encuentra el path del directorio para almacenar los reportes regulatorios...\n");
				return;
			}
			//---------------------------------------------------------------------------------------------------------------
			logger.append("---> Buscando informacion para la seccion 1 Personas Morales...\n");
			perfilesPersonasMorales  = criteriosInclusionDatosRegulatorios(BUSCAR_REGULATORIOS_PM);
			
			generadorReportes = new GeneradorReportesRegulatoriosImpl();
			
			if(perfilesPersonasMorales != null && perfilesPersonasMorales.size() > 0)
			{
				contratosSicaPM = new ArrayList();
				for(indice = 0; indice < perfilesPersonasMorales.size(); indice++)
				{
					perfil = (RegulatorioPerfil)perfilesPersonasMorales.get(indice);
					contratosSicaPM.add(perfil.getContratoSica());
					System.out.println("contratosSicaPM: " + perfil.getContratoSica());
				}
				
				clientesFiltradosPM = consultarDatosRegulatorios(BUSCAR_REGULATORIOS_PM, contratosSicaPM);
				logger.append("--> Se encontraron " + clientesFiltradosPM.size() + " registros para la seccion 1 Personas Morales.\n");
				System.out.println("clientesFiltradosPM.size(): " + clientesFiltradosPM.size());
				datosReporte = prepararDatosReporte(BUSCAR_REGULATORIOS_PM, clientesFiltradosPM, errores);
				
				outputStreamReporte = new ByteArrayOutputStream();
				generadorReportes.escribirXls(outputStreamReporte, datosReporte, BUSCAR_REGULATORIOS_PM);
				almacenarReporteRegulatorio(param, ".xls", logger, outputStreamReporte.toByteArray(), BUSCAR_REGULATORIOS_PM);
				logger.append("--> Se ha almacenado el reporte  'para la Seccion I Personas Morales' en formato XLS.\n");
				outputStreamReporte = null;
				
				outputStreamReporte = new ByteArrayOutputStream();
				generadorReportes.excribirCSV(outputStreamReporte, datosReporte);
				almacenarReporteRegulatorio(param, ".csv", logger, outputStreamReporte.toByteArray(), BUSCAR_REGULATORIOS_PM);
				logger.append("--> Se ha almacenado el reporte  'para la Seccion I Personas Morales' en formato CSV.\n");
				outputStreamReporte = null;
				
				actualizarBanderasPerfilesRegulatorios(contratosSicaPM, logger, BUSCAR_REGULATORIOS_PM);
			}
			else
			{
				sinDatosSeccion1 = "No se encontraron contrapartes que cumplan los criterios para ser" +
			      				   " incluidos en el reporte regulatorio 'Seccion I Personas Morales'. \n\n";
				logger.append(sinDatosSeccion1);
			}
			//---------------------------------------------------------------------------------------------------------------
			logger.append("---> Buscando informacion para la seccion 4 Parte relacionada...\n");
			perfilesParteRelacionada = criteriosInclusionDatosRegulatorios(BUSCAR_REGULATORIOS_PARTE_RELACIONADA);
			if(perfilesParteRelacionada != null && perfilesParteRelacionada.size() > 0)
			{
				perfilesParteRelacionada = criteriosInclusionDatosRegulatorios(BUSCAR_REGULATORIOS_PARTE_RELACIONADA);
				contratosSicaPR = new ArrayList();
				
				for(indice = 0; indice < perfilesParteRelacionada.size(); indice++)
				{
					perfil = (RegulatorioPerfil)perfilesParteRelacionada.get(indice);
					contratosSicaPR.add(perfil.getContratoSica());
					System.out.println("contratosSicaPR: " + perfil.getContratoSica());
				}
				
				errores = new StringBuffer();
				clientesFiltradosPR = consultarDatosRegulatorios(BUSCAR_REGULATORIOS_PARTE_RELACIONADA, contratosSicaPR);
				logger.append("--> Se encontraron " + clientesFiltradosPR.size() + " registros para la seccion 4 Parte Relacionada.\n");
				System.out.println("clientesFiltradosPR.size(): " + clientesFiltradosPR.size());
				datosReporte = prepararDatosReporte(BUSCAR_REGULATORIOS_PARTE_RELACIONADA, clientesFiltradosPR, errores);
				
				outputStreamReporte = new ByteArrayOutputStream();
				generadorReportes.escribirXls(outputStreamReporte, datosReporte, BUSCAR_REGULATORIOS_PARTE_RELACIONADA);
				almacenarReporteRegulatorio(param, ".xls", logger, outputStreamReporte.toByteArray(), BUSCAR_REGULATORIOS_PARTE_RELACIONADA);
				logger.append("--> Se ha almacenado el reporte  'para la Seccion IV Parte Relacionada' en formato XLS.\n");
				outputStreamReporte = null;
				
				outputStreamReporte = new ByteArrayOutputStream();
				generadorReportes.excribirCSV(outputStreamReporte, datosReporte);
				almacenarReporteRegulatorio(param, ".csv", logger, outputStreamReporte.toByteArray(), BUSCAR_REGULATORIOS_PARTE_RELACIONADA);
				logger.append("--> Se ha almacenado el reporte  'para la Seccion IV Parte Relacionada' en formato CVS.\n");
				outputStreamReporte = null;
				
				actualizarBanderasPerfilesRegulatorios(contratosSicaPR, logger, BUSCAR_REGULATORIOS_PARTE_RELACIONADA);
			}
			else
			{
				sinDatosSeccion4 = "No se encontraron contrapartes que cumplan los criterios para ser" +
			      				   " incluidos en el reporte regulatorio 'Seccion IV Parte Relacionada'. \n\n";
				logger.append(sinDatosSeccion4);
			}
			
			emailAsunto = "Finalización de backup de reportes regulatorios.";
			emailMensaje = "Ha finalizado el proceso que genera el backup de reportes regulatorios de banxico. \n" +
						   (sinDatosSeccion1 != null ? sinDatosSeccion1 : "") +
						   (sinDatosSeccion4 != null ? sinDatosSeccion4 : "") +
					       "Este email es enviado por el Sistema SICA, por favor, no responda este email.";
			if(enviarEmail)
			{
				try
				{
					gms.enviarMail(emailFrom, emailTo, null, emailAsunto, emailMensaje);
				}
				catch(Exception ew)
				{
					ew.printStackTrace();
					logger.append("---> No fue posible enviar el email de finalización de backup de reportes regulatorios..\n");
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			logger.append("Ha ocurrido un error inesperado al ejecutarse el backup de reportes regulatorios. \n\n");
			
			emailAsunto = "Fin de backup de reportes regulatorios. El proceso ha terminado con errores.";
			emailMensaje = "Ha finalizado el proceso que genera el backup de reportes regulatorios de banxico. " +
					       "El proceso terminó con errores. \n" +
					       "Este email es enviado por el Sistema SICA, por favor, no responda este email.";
			if(enviarEmail)
			{
				try
				{
					gms.enviarMail(emailFrom, emailTo, null, emailAsunto, emailMensaje);
				}
				catch(Exception em)
				{
					e.printStackTrace();
					logger.append("---> No fue posible enviar el email de finalización de backup de reportes regulatorios." +
							      "El proceso terminó con errores..\n");
				}
			}
		}
    }
    
    private void actualizarBanderasPerfilesRegulatorios(List contratosSica, StringBuffer logger, int opcionBusqueda)
    {
    	String queryPerfil = "FROM RegulatorioPerfil as p where p.contratoSica = ?";
    	List catalogo = null;
    	String texto = null;
    	RegulatorioPerfil perfil = null;
    	
    	for(int indice = 0; indice < contratosSica.size(); indice++)
    	{
    		catalogo = getHibernateTemplate().find(queryPerfil, contratosSica.get(indice));
        	
    		if(catalogo != null && catalogo.size() > 0)
    		{
    			perfil = (RegulatorioPerfil)catalogo.get(0);
    			if(opcionBusqueda == BUSCAR_REGULATORIOS_PM)
    			{
    				perfil.setDatosRegulatoriosActualizados("N");
    				texto = ". La bandera 'datos regulatorios actualizados' de la persona moral ha cambiado a 'N'.\n";
    			}
    			else // if(opcionBusqueda == BUSCAR_REGULATORIOS_PARTE_RELACIONADA)
    			{
    				perfil.setRegInstitucionActualizados("N");
    				texto = ". La bandera 'datos regulatorios actualizados' de la parte relacionada ha cambiado a 'N'.\n";
    			}
    			
    			update(perfil);	
    			logger.append("Se ha actualizado el perfil regulatorio del contrato SICA " + 
    					       (String)contratosSica.get(indice) + texto);
    		}
    		
    	}
    }
    
    /**
     * Servicio encargado de crear el archivo del reporte regulatorio y almacenarlo en el directorio especificado
     * @param parametroDirectorio ParametroSica que indica el path del directorio de reportes regulatorios 
     * @param extension Extensión del archivo que se va a crear '.xls' o '.cvs'
     * @param logger StringBuffer para almacenar las descripciones de los errores encontrados
     * @param bytesReporte Arreglo de bytes que contienen la información del reporte regulatorio
     * @param opcionBusqueda Constante que indica el tipo de informacion que manipulará: 
     * BUSCAR_REGULATORIOS_PM o BUSCAR_REGULATORIOS_PARTE_RELACIONADA
     */
    private void almacenarReporteRegulatorio(ParametroSica parametroDirectorio, String extension,
    										 StringBuffer logger, byte bytesReporte[], int opcionBusqueda)
    {
    	String nombreReporte = null;
    	File directorio = null;
		File fileReporte = null;
		FileOutputStream fos = null;
		SimpleDateFormat dateFormat = null;
		
		try
		{
			if(opcionBusqueda == SicaServiceData.BUSCAR_REGULATORIOS_PM)
				nombreReporte = "ReporteSeccion1PersonasMorales";
			else
				nombreReporte = "ReporteSeccion4ParteRelacionada";
			
			dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			directorio = new File(parametroDirectorio.getValor());
			
			if(directorio.exists() && directorio.canWrite())
			{
				nombreReporte += "-" + dateFormat.format(new Date()) + extension;
				fileReporte = new File(directorio.getAbsolutePath() + File.separator + nombreReporte);
				if(!fileReporte.exists())
				{
					if(fileReporte.createNewFile())
					{
						fos = new FileOutputStream(fileReporte);
						fos.write(bytesReporte);
						fos.close();
						fos = null;
					}
					else
					{
						logger.append("--> No fue posible crear el archivo " + nombreReporte + " para almacenar el reporte. \n");
					}
				}
				else
				{
					logger.append("--> El archivo " + nombreReporte + " existe. No se sobreescribira el reporte. \n");
				}
			}
			else
			{
				logger.append("--> Se ha comprobado que el directorio no existe y/o no tiene permisos de escritura. \n");
			}
		}
		catch(FileNotFoundException e)
		{
			e.printStackTrace();
			logger.append("--> No fue posible escribir los datos del reporte del reporte regulatorio: " + nombreReporte + ". \n");
		}
		catch(IOException e)
		{
			e.printStackTrace();
			logger.append("--> No fue posible crear el archivo del reporte en el directorio de reportes regulatorios: " + nombreReporte + ". \n");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			logger.append("--> Ha ocurrido un error inesperado en el método almacenarReporteRegulatorio(). \n");
		}finally{
			if(fos != null)
			{
				safeClose(fos);
			}
		}
    }
    
    /**
     * Servicio encargado de preparar los datos del reporte regulatorio
     * @param opcionBusqueda Constante que indica el tipo de informacion que manipulará: 
     * BUSCAR_REGULATORIOS_PM o BUSCAR_REGULATORIOS_PARTE_RELACIONADA
     * @param registros Lista de registros de tipo RegulatorioDatosPM o RegulatorioInstitucion
     * @param errores StringBuffer utilizado para almacenar las descripcuiones de los errores encontrados
     * @return StringBuffer con los campos de los registros separados por ';'. Los registros se separan por un '|'.
     */
    public StringBuffer prepararDatosReporte(int opcionBusqueda, List registros, StringBuffer errores)
	{
		StringBuffer datos = new StringBuffer();
		RegulatorioDatosPM datosPersonaMoral = null;
		RegulatorioInstitucion datosParteRelacionada = null;
		SimpleDateFormat dateFormatRegulatorio = new SimpleDateFormat("yyyy/MM/dd");
		List resultado = null, contrapartesSinClaveBanxico = new ArrayList();
		boolean datosValidos = false;
		
		for(int indice = 0; indice < registros.size(); indice++)
		{
			if(opcionBusqueda == BUSCAR_REGULATORIOS_PM)
			{
				datosPersonaMoral = (RegulatorioDatosPM)registros.get(indice);
				datos.append((StringUtils.isNotEmpty(datosPersonaMoral.getClaveBanxico()) ? datosPersonaMoral.getClaveBanxico() : "EMPTY") + ";");
				datos.append(datosPersonaMoral.getNombreContraparte() + ";");
				datos.append(datosPersonaMoral.getSociedadMercantil().getId().intValue() + ";");
				datos.append(datosPersonaMoral.getTipoIdentificador().getId().intValue() + ";");
				datos.append(datosPersonaMoral.getClaveIdentificadorContraparte() + ";");
				datos.append(datosPersonaMoral.getActividadEconomica().getId().intValue() + ";");
				datos.append(datosPersonaMoral.getClaveLei() + ";");
				datos.append(datosPersonaMoral.getSectorIndustrial().getId().intValue() + ";");
				datos.append(datosPersonaMoral.getPaisControlContraparte().getClaveIso() + ";");
				datos.append(datosPersonaMoral.getPaisResidenciaContraparte().getClaveIso() + ";");
				datos.append(datosPersonaMoral.getClaveLeiMatrizDirecta() + ";");
				datos.append(datosPersonaMoral.getClaveLeiMatrizUltimaInstancia() + ";");
				datos.append(dateFormatRegulatorio.format(datosPersonaMoral.getFechaContraparte()));
				datosValidos = true;
			}
			else if(opcionBusqueda == BUSCAR_REGULATORIOS_PARTE_RELACIONADA)
			{
				datosParteRelacionada = (RegulatorioInstitucion)registros.get(indice);
				System.out.println("datosParteRelacionada is null: " + (datosParteRelacionada == null));
				resultado = findRegulatorioDatosPM(datosParteRelacionada.getIdPersona());
				System.out.println("resultado is null: " + (resultado == null));
				System.out.println("resultado size: " + (resultado != null ? resultado.size() : -1));
				datosValidos = false;
				if(resultado != null && resultado.size() > 0)
				{
					datosPersonaMoral = (RegulatorioDatosPM)resultado.get(0);
					if(StringUtils.isNotEmpty(datosPersonaMoral.getClaveBanxico()))
					{
						datos.append(datosPersonaMoral.getClaveBanxico() + ";");
						datos.append(datosParteRelacionada.getTipoRelacion().getId().intValue() + ";");
						datos.append(datosParteRelacionada.getPerteneceGrupoFinanciero().intValue() + ";");
						if(datosParteRelacionada.getTipoRelacion().getId().intValue() != 10) // la contraparte si está relacionada
						{
							datos.append(datosParteRelacionada.getEventoRelacion().getId().intValue() + ";");
							datos.append(dateFormatRegulatorio.format(datosParteRelacionada.getFechaEvento()) + ";");
						}
						else
						{
							datos.append("0;");    // la contraparte no está relacionada, no hay evento de relación
							datos.append("EMPTY"); // no hay fecha de relación
						}
						
						datosValidos = true;
					}
					else
					{
						contrapartesSinClaveBanxico.add(datosPersonaMoral.getNombreContraparte());
						datosValidos = false;
					}
				}
			}
			
			if(datosValidos && indice < (registros.size() - 1))
				datos.append("|");
		}
		
		if(contrapartesSinClaveBanxico != null && contrapartesSinClaveBanxico.size() > 0)
		{
			if(datos == null || datos.length() == 0)
			{
				errores.append("El sistema encontr\u00F3 que todas las contrapartes que se mencionan a continuaci\u00F3n no tiene " +
						       "clave BANXICO y por ello no se cre\u00F3 el reporte de la 'SECCION 4 Parte Relacionada': " +
					           (String)contrapartesSinClaveBanxico.get(0));
			}
			else
			{
				errores.append("La contrapartes que se mencionan a continuaci\u00F3n no fueron agregadas al reporte 'SECCION 4 Parte Relacionada'" +
					       " porque no tiene clave BANXICO: " + (String)contrapartesSinClaveBanxico.get(0));
			}
			
			for(int ind = 1; ind < contrapartesSinClaveBanxico.size(); ind++)
				errores.append(", " + (String)contrapartesSinClaveBanxico.get(ind));
		}
		
		warn("datos reporte: " + (datos != null ? datos.toString() : "no hay datos.."));
		return datos;
	}
    
    public Integer findGiroEconomicoAsociadoBupActividadEconomica(String idActividadEconomica)
    {
    	BupActividadEconomica a = null;
    	Integer idGiroEconomico = null;
    	
    	a = (BupActividadEconomica)find(BupActividadEconomica.class, idActividadEconomica);
    	if(a != null)
    		idGiroEconomico = a.getIdGiroEconomico();
    	
    	return idGiroEconomico;
    }
    
    public boolean actualizarPersonaFisica(final Map datosPf)
    {
    	boolean actualizacion = true;
    	try 
    	{
			getHibernateTemplate().execute(new HibernateCallback() 
			{
				public Object doInHibernate(Session session) throws HibernateException, SQLException 
				{
					Connection connection = null;
					PreparedStatement statement = null;
					String sqlPersona = "UPDATE BUP_PERSONA SET RFC = '" + datosPf.get("rfc").toString() + "', " +
					                    "NOMBRE_CORTO = '" + datosPf.get("nombre").toString() + " " + datosPf.get("paterno").toString() + 
					                                        (datosPf.get("materno") == null ? "', " : " " + datosPf.get("materno").toString() + "', ") +
					                    "ID_PAIS = '" + datosPf.get("nacionalidad").toString() + "', " +
					                    "ID_SECTOR = " + datosPf.get("sectorEconomico").toString() + ", " +
					                    "ID_ACTIVIDAD = " + ((datosPf.get("actividadEconomica") == null) ?
				    		                     "null, " : "'" + datosPf.get("actividadEconomica").toString() + "', ") +
					                    "ACTIVIDAD_GIRO = " + ((datosPf.get("giroEconomico") == null) ?
				    		                     "null, " : "'" + datosPf.get("giroEconomico").toString() + "', ") +
					                    "USUARIO_ULT_MOD = " + ((Integer)datosPf.get("usuario")).intValue() + ", " + 
					                    "FECHA_ULT_MOD = sysdate, " +
					                    "VERIFICA_FE = " + datosPf.get("facturacionElectronica").toString() + 
							            " WHERE ID_PERSONA = " + ((Integer)datosPf.get("idPersona")).intValue();
					log.debug("sqlPersona: " + sqlPersona);
	
				    String sqlPersonaFisica = "UPDATE BUP_PERSONA_FISICA SET PATERNO = '" + datosPf.get("paterno").toString() + "', " +
				    						  "MATERNO = " + (datosPf.get("materno") == null ? "null, " : " '" + 
				    								           datosPf.get("materno").toString() + "', ") +
				    						  "NOMBRE = '" + datosPf.get("nombre").toString() + "', " +
				    						  "SEXO = " + (datosPf.get("sexo") == null ? "null, " : "'" + 
				    								         datosPf.get("sexo").toString() + "', " ) +
				    						  "ID_ESTADO_CIVIL = " + (datosPf.get("estadoCivil") == null ? "null, " : 
				    							                  "'" + datosPf.get("estadoCivil").toString() + "', ") + 
				    					      "FECHA_NACIMIENTO = " + (datosPf.get("fechaNacimiento") == null ?
				    					    		                    "null, " : "to_date('" + datosPf.get("fechaNacimiento").toString() + "', 'dd/mm/yyyy'), ") +
				    					      "LUGAR_NACIMIENTO = " + (datosPf.get("lugarNacimiento") == null ? " null, " : 
				    					    	                       "'" + datosPf.get("lugarNacimiento").toString() + "', ") + 
				    					      "ID_PAIS_NACIMIENTO = " + (datosPf.get("paisNacimiento") == null ?
				    					    		                     "null, " : "'" + datosPf.get("paisNacimiento").toString() + "', ") +
				    					      "CURP = " + (datosPf.get("curp") == null ?
				    					    		                     "null, " : "'" + datosPf.get("curp").toString() + "', ") +
				    					      "ID_TIPO_IDENTIFICA = " + (datosPf.get("tipoIdentifica") == null ?
				    					    		                     "null, " : "'" + datosPf.get("tipoIdentifica").toString() + "', ") +
				    					      "NO_IDENTIFICACION = " + (datosPf.get("noIdentificacion") == null ?
				    					    		                     "null " : "'" + datosPf.get("noIdentificacion").toString() + "'") +	
				    					    		                     
				    		                  " WHERE ID_PERSONA = " + ((Integer)datosPf.get("idPersona")).intValue();
				    log.debug("sqlPersonaFisica: " + sqlPersonaFisica);
									
					connection = session.connection();
	
					// Actualiza BUP_PERSONA
					statement = connection.prepareStatement(sqlPersona);
					statement.executeUpdate();
					statement.close();
					statement = null;
	
					// Actualiza BUP_PERSONA_FISICA
					statement = connection.prepareStatement(sqlPersonaFisica);
					statement.executeUpdate();
					statement.close();
					statement = null;
					
					return null;
				} 
			});
		}
		catch (DataAccessException ex) {
			log.error("DataAccessException en actualizarPersonaFisica() ", ex);
			actualizacion = false;
		} 
		
		return actualizacion;
    }
    
    public boolean actualizarPersonaMoral(final Map datosPf)
    {
    	boolean actualizacion = true;
    	try 
    	{
			getHibernateTemplate().execute(new HibernateCallback() 
			{
				public Object doInHibernate(Session session) throws HibernateException, SQLException 
				{
					Connection connection = null;
					PreparedStatement statement = null;
					String sqlPersona = "UPDATE BUP_PERSONA SET RFC = '" + datosPf.get("rfc").toString() + "', " +
					                    "NOMBRE_CORTO = '" + datosPf.get("razonSocial").toString() + "', " +
					                    "ID_PAIS = '" + datosPf.get("nacionalidad").toString() + "', " +
					                    "ID_SECTOR = " + datosPf.get("sectorEconomico").toString() + ", " +
					                    "USUARIO_ULT_MOD = " + ((Integer)datosPf.get("usuario")).intValue() + ", " + 
					                    "FECHA_ULT_MOD = sysdate, " +
					                    "VERIFICA_FE = " + datosPf.get("facturacionElectronica").toString() + 
							            " WHERE ID_PERSONA = " + ((Integer)datosPf.get("idPersona")).intValue();
					log.debug("sqlPersona: " + sqlPersona);
	
				    String sqlPersonaFisica = "UPDATE BUP_PERSONA_MORAL SET RAZON_SOCIAL = '" + datosPf.get("razonSocial").toString() + "', " +
				    					      "FECHA_CONSTITUCION = " + (datosPf.get("fechaNacimiento") == null ?
				    					    		                    "null, " : "to_date('" + datosPf.get("fechaNacimiento").toString() + "', 'dd/mm/yyyy') ") +
				    		                  " WHERE ID_PERSONA = " + ((Integer)datosPf.get("idPersona")).intValue();
				    log.debug("sqlPersonaMoral: " + sqlPersonaFisica);
									
					connection = session.connection();
	
					// Actualiza BUP_PERSONA
					statement = connection.prepareStatement(sqlPersona);
					statement.executeUpdate();
					statement.close();
					statement = null;
	
					// Actualiza BUP_PERSONA_MORAL
					statement = connection.prepareStatement(sqlPersonaFisica);
					statement.executeUpdate();
					statement.close();
					statement = null;
					
					return null;
				} 
			});
		}
		catch (DataAccessException ex) {
			log.error("DataAccessException en actualizarPersonaMoral() ", ex);
			actualizacion = false;
		} 
		
		return actualizacion;
    }
    
    public boolean actualizarDireccion(final Map datos)
    {
    	boolean actualizacion = true;
    	try 
    	{
			getHibernateTemplate().execute(new HibernateCallback() 
			{
				public Object doInHibernate(Session session) throws HibernateException, SQLException 
				{
					Connection connection = null;
					PreparedStatement statement = null;
					String sqlDireccion = "UPDATE BUP_DIRECCION SET ID_PAIS = '" + datos.get("pais").toString() + "', " +
					                    "CALLE_Y_NUMERO = '" + datos.get("calle").toString() + "', " +
					                    "NUMERO_EXT = '" + datos.get("noExterior").toString() + "', " +
					                    (StringUtils.isNotEmpty(datos.get("noInterior").toString()) ? 
					                      "NUMERO_INT = '" + datos.get("noInterior").toString() + "', " : "") +
					                    "ID_TIPO_DIRECCION = '" + datos.get("esPrimaria").toString() + "', " +
					                    "CODIGO_POSTAL = '" + datos.get("codigoPostal").toString() + "', " +
					                    "COLONIA = '" + datos.get("colonia").toString() + "', " +
					                    "CIUDAD = '" + datos.get("ciudad").toString() + "', " +
					                    "DELEGACION_MUNICIPIO = '" + datos.get("delMunicipio").toString() + "', " +
					                    "ENTIDAD_FEDERATIVA = '" + datos.get("estado").toString() + "', " +
					                    "ES_FISCAL = '" + datos.get("esFiscal").toString() + "', " +
					                    "VERIFICADO_FE = " + datos.get("facturacionElectronica").toString() + ", " +
					                    (datos.get("tipoComprobante") != null && !"".equals(datos.get("tipoComprobante").toString().trim()) ? 
					                     "ID_TIPO_COMPROBANTE = '" + datos.get("tipoComprobante").toString() + "', " : "") +
					                      (datos.get("numComprobante") != null && !"".equals(datos.get("numComprobante").toString()) ? 
							               "NO_COMPROBANTE = '" + datos.get("numComprobante").toString() + "', " : "") +
				                        (datos.get("fechaComprobante") != null && !"".equals(datos.get("fechaComprobante").toString()) ? 
						                      "FECHA_EXPIDE_COMP = to_date('" + datos.get("fechaComprobante").toString() + "', 'dd/mm/yyyy'), " : "") +
					                    "USUARIO_ULT_MOD = " + ((Integer)datos.get("usuario")).intValue() + ", " + 
					                    "FECHA_ULT_MOD = sysdate " +
							            " WHERE ID_DIRECCION = " + ((Integer)datos.get("idDireccion")).intValue();
									
					connection = session.connection();
	
					// Actualiza BUP_PERSONA
					statement = connection.prepareStatement(sqlDireccion);
					statement.executeUpdate();
					statement.close();
					statement = null;

					return null;
				} 
			});
		}
		catch (DataAccessException e) 
		{
			actualizacion = false;
			e.printStackTrace();
		} 
		
		return actualizacion;
    }
    
    /**
     * La eliminacion consiste en cambiar el estatus de la direccion. La eliminacion es logica.
     * @param datos
     * @return
     */
    public boolean eliminarDireccion(final Map datos)
    {
    	boolean actualizacion = true;
    	try 
    	{
			getHibernateTemplate().execute(new HibernateCallback() 
			{
				public Object doInHibernate(Session session) throws HibernateException, SQLException 
				{
					Connection connection = null;
					PreparedStatement statement = null;
					String sqlDireccion = "UPDATE BUP_DIRECCION SET STATUS = '" + datos.get("status").toString() + "', " +
					                    "USUARIO_ULT_MOD = " + ((Integer)datos.get("usuario")).intValue() + ", " + 
					                    "FECHA_ULT_MOD = sysdate " +
							            " WHERE ID_DIRECCION = " + ((Integer)datos.get("idDireccion")).intValue();
									
					connection = session.connection();

					statement = connection.prepareStatement(sqlDireccion);
					statement.executeUpdate();
					statement.close();
					statement = null;

					return null;
				} 
			});
		}
		catch (DataAccessException e) 
		{
			actualizacion = false;
			e.printStackTrace();
		} 
		
		return actualizacion;
    }
    
    /**
     * Inserta la clave referencia del cliente.
     * @param datos
     * @return boolean
     */
    public boolean insertarClaveReferenciaCliente(final Map datos)
    {
    	boolean actualizacion = true;
    	try 
    	{
			getHibernateTemplate().execute(new HibernateCallback() 
			{
				public Object doInHibernate(Session session) throws HibernateException, SQLException 
				{
					Connection connection = null;
					PreparedStatement statement = null;
					String sqlInsert = "insert into BUP_PERSONA_CLAVE_REFERENCIA" +
							"(ID_PERSONA, ID_PERSONA_ANTERIOR,CLAVE_REFERENCIA,STATUS,USUARIO_ULT_MOD, FECHA_ULT_MOD) " +
							"values (" + datos.get("idPersona").toString() + "," +
							             datos.get("idPersona").toString() + "," +
							       "'" + datos.get("claveReferencia").toString() + "'," +
							       "'" + datos.get("status").toString() + "'," +
							             datos.get("idUsuario").toString() + "," +
							             "sysdate)";
									
					connection = session.connection();

					statement = connection.prepareStatement(sqlInsert);
					statement.executeUpdate();
					statement.close();
					statement = null;

					return null;
				} 
			});
		}
		catch (DataAccessException e) 
		{
			actualizacion = false;
			e.printStackTrace();
		} 
		
		return actualizacion;
    }
    
    /**
     * Reinicia a cero los contadores de excesos delas lineas de credito activas. 
     */
    public void reiniciarContadoresExcesosLineasCreditoActivas(StringBuffer logger)
    {
    	getLineaCambioServiceData().reiniciarContadoresExcesosLineasCreditoActivas(logger);
    }
    
    public static void safeClose(FileOutputStream fos)
    {
    	if (fos != null) 
    	{
	    	try 
	    	{
	    		fos.close();
	    	} 
	    	catch (IOException e) 
	    	{
	    		e.printStackTrace();

	    	}
    	}
    }
    
    //60057-CFDI 3.3
	/**
	 * Registra un NIT de una Persona
	 * 
	 * @param persona
	 * @param nit
	 * @return Integer
	 */
	public Integer storeNit(Integer idPersona, String nit) {
		NitPersona nitPersona = new NitPersona();
		nitPersona.setIdPersona(idPersona);
		nitPersona.setNit(nit);
		return (Integer) getHibernateTemplate().save(nitPersona);
	}

	//60057-CFDI 3.3
	/**
	 * Busca un NIT con base en un Id de Persona
	 * 
	 * @param persona
	 * @return NitPersona
	 */
	public NitPersona findNitByIdPersona(Integer idPersona) {
		NitPersona nit = null;
		List lNit = getHibernateTemplate().findByNamedQuery("findNitByIdPersona", new Object[] { idPersona });
		if(lNit != null && !lNit.isEmpty()) {
    		nit = nit = (NitPersona)lNit.get(0);
		}
		return nit;
	}
	
      
	/**
	 * Cache de parametros almacena la lista de los parametros consultados por
	 * el sistema.
	 */
	private List parametrosCacheList = new ArrayList();
	/**
	 * Constante con el formato de horas y minutos.
	 */
	public static final SimpleDateFormat HOUR_FORMAT = new SimpleDateFormat(
			"HH:mm");

	/**
	 * Constante para el N&uacute;mero M&aacute;ximo de Registros por Query.
	 */
	private static final int MAX_RESULTS_VESPERTINO = 500;

	/**
	 * Constante Cliente Ixe Divisas para el Reconocimiento de Utilidades MXN.
	 */
	public static final String CLIENTE_IXE_DIVISAS = "Cliente Ixe Divisas";

	/**
	 * El ID Tipo Ejecutivo de todos los Promotores (Ejecutivos) que operan el
	 * SICA.
	 */
	public static final String ID_TIPO_EJECUTIVO = "EJEBAN";

	/**
	 * Nombre Corto del Sistema de Cambios dado de alta en la Administracion de
	 * Seguridad.
	 */
	public static final String SICA = "SICA";

	/**
	 * El formato para las cantidades monetarias.
	 */
	private static final DecimalFormat MONEY_FORMAT = new DecimalFormat(
			"#,##0.00");

	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(
			"yyyyMMdd");

	public static final String STR_72HRS = "72HRS";

	public static final String STR_96HRS = "96HRS";

	/** Paremetro para el limite en USD de control documental de clientes */
	public static final String LIM_USD_DOC_CL = "LIM_USD_DOC_CL";

	/** Paremetro para el limite de en USD de control documental de clientes */
	public static final String LIM_USD_DOC_US = "LIM_USD_DOC_US";
	
	/** Paremetro para el limite de las divisas distintas de USD control documental de usuarios JDCH 08/03/2013*/
	public static final String LIM_DIV_DOC_US = "LIM_DIV_DOC_US";

	/**
	 * Query para obtener los c&oacute;digos postales en objeto
	 * CodigoPostalListaBlanca.
	 */
	private static final String QUERY_CODIGOS_POSTALES_SELECT = "SELECT new com.ixe.ods.sica.model."
			+ "CodigoPostalListaBlanca(bcp.id.codigoPostal, mlb.zonaFronteriza, "
			+ "mlb.zonaTuristica, mlb.id.idEstado, mlb.id.idMunicipio) "
			+ "FROM MunicipioListaBlanca AS mlb, BupCodigoPostal AS bcp "
			+ "WHERE mlb.id.idEstado = bcp.id.municipio.id.estado.idEstado "
			+ "AND mlb.id.idMunicipio = bcp.id.municipio.id.idMunicipio ";
	/**
	 * Groupby del query para obtener los c&oacute;digos postales.
	 */
	private static final String QUERY_CODIGOS_POSTALES_GROUPBY = "GROUP BY bcp.id.codigoPostal, mlb.zonaFronteriza, "
			+ "mlb.zonaTuristica, mlb.id.idEstado, mlb.id.idMunicipio";


}