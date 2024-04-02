/*
 * $Id: HibernateCierreSicaServiceData.java,v 1.31.16.1.6.1.6.5.2.2.2.1.16.1.18.1.2.4 2016/09/06 18:39:23 mejiar Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 - 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica.sdo.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate.HibernateCallback;

import com.ixe.ods.bup.model.ContratoSica;
import com.ixe.ods.seguridad.model.IFacultad;
import com.ixe.ods.seguridad.model.IUsuario;
import com.ixe.ods.seguridad.model.SeguridadException;
import com.ixe.ods.seguridad.sdo.SeguridadServiceData;
import com.ixe.ods.seguridad.services.LoginService;
import com.ixe.ods.sica.SicaAlertasService;
import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.SicaProperties;
import com.ixe.ods.sica.SicaSiteCache;
import com.ixe.ods.sica.dao.CierreDao;
import com.ixe.ods.sica.dao.ExtraDao;
import com.ixe.ods.sica.dao.LimiteDao;
import com.ixe.ods.sica.dao.LineaCambioDao;
import com.ixe.ods.sica.model.Deal;
import com.ixe.ods.sica.model.DealDetalle;
import com.ixe.ods.sica.model.DealDetalleStatusLog;
import com.ixe.ods.sica.model.DealLog;
import com.ixe.ods.sica.model.DealStatusLog;
import com.ixe.ods.sica.model.Divisa;
import com.ixe.ods.sica.model.Estado;
import com.ixe.ods.sica.model.HistoricoDeal;
import com.ixe.ods.sica.model.HistoricoDealDetalle;
import com.ixe.ods.sica.model.HistoricoDealDetalleStatusLog;
import com.ixe.ods.sica.model.HistoricoDealLog;
import com.ixe.ods.sica.model.HistoricoDealPosicion;
import com.ixe.ods.sica.model.HistoricoDealStatusLog;
import com.ixe.ods.sica.model.HistoricoPosicion;
import com.ixe.ods.sica.model.HistoricoPosicionDetalle;
import com.ixe.ods.sica.model.HistoricoSpread;
import com.ixe.ods.sica.model.HistoricoSwap;
import com.ixe.ods.sica.model.HistoricoTipoCambio;
import com.ixe.ods.sica.model.Limite;
import com.ixe.ods.sica.model.LineaCambio;
import com.ixe.ods.sica.model.LineaCambioLog;
import com.ixe.ods.sica.model.MesaCambio;
import com.ixe.ods.sica.model.ParametroSica;
import com.ixe.ods.sica.model.PosIni;
import com.ixe.ods.sica.model.Posicion;
import com.ixe.ods.sica.model.PosicionDetalle;
import com.ixe.ods.sica.model.PrecioReferenciaActual;
import com.ixe.ods.sica.model.Spread;
import com.ixe.ods.sica.model.Swap;
import com.ixe.ods.sica.model.TipoLimite;
import com.ixe.ods.sica.sdo.CierreSicaServiceData;
import com.ixe.ods.sica.sdo.PizarronServiceData;
import com.ixe.ods.sica.sdo.SicaServiceData;
import com.ixe.ods.sica.services.LineaCreditoConstantes;
import com.ixe.ods.sica.services.LineaCreditoMensajes;
import com.ixe.ods.sica.services.RiesgosService;
import com.ixe.ods.sica.utils.DateUtils;

/**
 * <p>
 * Subclase de AbstractHibernateSicaServiceData para realizar el Cierre del
 * SICA.
 * </p>
 * <p/>
 * <p>
 * Los actividades realizadas en el Cierre son las siguientes:
 * <ul>
 * <li>Validar Deals en Cierre del D&iacute;a</li>
 * <li>Limpiar Operaciones Clientes Recurrentes.</li>
 * <li>Revisar Vencimientos Lineas de Cr&eacute;dito</li>
 * <li>Limpiar Excepciones de Lineas de Cr&eacute;dito.</li>
 * <li>Checar L&iacute;mites de Riesgo.</li>
 * <li>Realizar Corrimientos Fecha Valor</li>
 * <li>Generaci&oacute;n de Hist&oacute;ricos</li>
 * </ul>
 * </p>
 * 
 * @author Edgar I. Leija
 * @version $Revision: 1.31.16.1.6.1.6.5.2.2.2.1.16.1.18.1.2.4 $ $Date: 2016/09/06 18:39:23 $
 */
public class HibernateCierreSicaServiceData extends
		AbstractHibernateSicaServiceData implements CierreSicaServiceData {

	/**
	 * Constructor vac&iaoacute;.
	 */
	public HibernateCierreSicaServiceData() {
		super();
	}

	/**
	 * Regresa un ticket que se obtiene a partir del usuario que se encuentra
	 * configurado en sica.properties.
	 * 
	 * @return String.
	 * @throws SicaException
	 *             Si no se pueden obtener las propiedades de conexi&oacute;n.
	 */
	private String obtenerTicket() throws SicaException {
		try {
			String pUsr = SicaProperties.getInstance().getSicaUsr();
			String pPwd = SicaProperties.getInstance().getSicaPwd();
			String pSys = SicaProperties.getInstance().getSicaSys();
			LoginService loginService = (LoginService) _appContext
					.getBean("loginService");
			return loginService.obtieneTicket(pUsr, pSys, pPwd, pUsr, "SICA",
					"0.0.0.0");
		} catch (SeguridadException e) {
			debug(e);
			throw new SicaException(e.getMessage());
		}
	}

	/**
	 * @param ssc
	 *            Una referencia al SicaSiteCache.
	 * @param ticket
	 *            El ticket de la sesi&oacute;n del usuario.
	 * @param fechaSistema
	 *            La fecha actual del sistema.
	 * @see com.ixe.ods.sica.sdo.CierreSicaServiceData#cerrarDiaSica(SicaSiteCache,String,Date)
	 * @throws SicaException
	 *             Si ocurre alg&uacute;n problema.
	 */
	public StringBuffer cerrarDiaSica(SicaSiteCache ssc, String ticket,
			Date fechaSistema) throws SicaException {
		if (ticket == null) {
			ticket = obtenerTicket();
		}
		setFechaSistema(fechaSistema);
		StringBuffer logger = new StringBuffer();
		logger.append(Calendar.getInstance().getTime()).append(
				" *** Inicia cierre de dia ").append(fechaSistema).append("\n");
		boolean estadoSistema = ((Estado) getHibernateTemplate()
				.findByNamedQuery("findEstadoActual").get(0)).getIdEstado() == Estado.ESTADO_CIERRE_DIA;
		logger = estadoSistema ? hacerCierre(logger) : errorCierre(logger);
		hacerUtilidadCero();
		return logger;
	}

	/**
	 * Permite hacer todas nuestras utilidades cero despu&eacute;s de haber sido
	 * realizado el Cierre.
	 * 
	 */
	private void hacerUtilidadCero() {
		try {
			List posicion = findAll(Posicion.class);
			for (Iterator it = posicion.iterator(); it.hasNext();) {
				Posicion p = (Posicion) it.next();
				if (p.getMesaCambio().getIdMesaCambio() == 1) {
					p.setUtilidadGlobal(0);
					p.setUtilidadMesa(0);
				}
			}
			update(posicion);
		} catch (Exception e) {
			if (logger.isDebugEnabled()) {
				logger.debug(e);
			}
		}
	}

	/**
	 * Permite Bloquear o Desbloquear la Facultad de Promotor de Guardia.
	 * 
	 * @param isBloquear
	 *            Si de Bloquea o Desbloquea la Facultad.
	 */
	private void bloquearFacultadGuardia(boolean isBloquear) {
		IFacultad fac = getSeguridadServiceData().getFacultadSimple(
				SICA_GUARDIA);
		if (isBloquear) {
			fac.setBloqueada(true);
		} else {
			fac.setBloqueada(false);
		}
		update(fac);
	}

	/**
	 * @see com.ixe.ods.sica.sdo.CierreSicaServiceData#checarLimites(boolean)
	 */
	public void checarLimites(boolean cierre) throws SicaException {
		List mesasCambio = findAll(MesaCambio.class);
		// Primero todas las divisas para cada mesa:
		for (Iterator it = mesasCambio.iterator(); it.hasNext();) {
			MesaCambio mesaCambio = (MesaCambio) it.next();
			List limites = limiteDao.getLimitesParaMesaDivisa(new Integer(
					mesaCambio.getIdMesaCambio()), null, null);
			revisarLimitesDeRiesgo(limites, null, mesaCambio, cierre);
		}
		// Ahora mesa por mesa, divisa por divisa:
		for (Iterator it = mesasCambio.iterator(); it.hasNext();) {
			MesaCambio mesaCambio = (MesaCambio) it.next();
			List posiciones = getHibernateTemplate().findByNamedQuery(
					"findPosicionByIdMesaCambio",
					new Integer(mesaCambio.getIdMesaCambio()));
			for (Iterator itPos = posiciones.iterator(); itPos.hasNext();) {
				Posicion posicion = (Posicion) itPos.next();
				List limites = limiteDao.getLimitesParaMesaDivisa(new Integer(
						mesaCambio.getIdMesaCambio()), posicion.getDivisa()
						.getIdDivisa(), null);
				revisarLimitesDeRiesgo(limites, posicion.getDivisa()
						.getIdDivisa(), mesaCambio, cierre);
			}
		}
		// Ahora la del cierre:
		if (cierre) {
			RiesgosService riesgosService = (RiesgosService) _appContext
					.getBean("riesgosService");
			Map posiciones = riesgosService.obtenerMonitorRiesgos();
			double posicionGlobal = ((Double) posiciones.get("nivel"))
					.doubleValue();
			double limiteRegulatorio = ((Double) posiciones
					.get("limiteRegulatorio")).doubleValue();
			if (Math.abs(posicionGlobal) > Math.abs(limiteRegulatorio)) {
				sendAlertaRiesgo("total", posicionGlobal, "Limite Regulatorio",
						"todas", "todas");
			}
		}
	}

	/**
	 * Manda alerta que existi&oacute; error en el Cierre y escribe en el Log
	 * 
	 * @param logger
	 *            <cod e>StringBuffer</code> log de actividad del Cierre
	 * @return StringBuffer
	 */
	private StringBuffer errorCierre(StringBuffer logger) {
		HashMap contextoAlarma = new HashMap();
		contextoAlarma.put("", "");
		generaAlerta("SC_CR_ST", getIdAdministrador(), contextoAlarma);
		logger.append(Calendar.getInstance().getTime()).append(
				" *** Estado inconsistente no se " + "hizo Cierre \n");
		return logger;
	}

	/**
	 * Se limpian las tablas necesarias para dejar el inicio del d&iacute; y en
	 * caso de que sea necesario se generan hist&oacute;ricos de las
	 * operaciones.
	 * 
	 * @param logger
	 *            StringBuffer
	 * @return StringBuffer.
	 */
	private StringBuffer limpiarTablas(StringBuffer logger) {
		logger
				.append(Calendar.getInstance().getTime())
				.append(
						" *** Se borran los registros y se "
								+ "pasan a las tablas con informaci&oacute;n hist&oacute;rica. \n");

		List dealHis = new ArrayList();
		List dealDetalleHis = new ArrayList();
		List dealLogHis = new ArrayList();
		List dealStatusLogHis = new ArrayList();
		List dealDetalleStatusLogHis = new ArrayList();
		List posicionHis = new ArrayList();
		List posicionDetalleHis = new ArrayList();
		List swapHis = new ArrayList();
		List spreadHis = new ArrayList();
		Calendar fechaTresMesesAtras = new GregorianCalendar();
		fechaTresMesesAtras.setTime(getFechaSistema());
		fechaTresMesesAtras = setFecha(fechaTresMesesAtras);
		ParametroSica p = (ParametroSica) find(ParametroSica.class,
				ParametroSica.MESES_PARA_HISTORICOS);
		int mesesHistorico = Integer.parseInt(p.getValor());
		fechaTresMesesAtras.add(Calendar.MONTH, -(mesesHistorico));
		fechaTresMesesAtras.add(Calendar.DAY_OF_MONTH, 1);
		List deals = findAllDealsTresMeses(fechaTresMesesAtras.getTime());
		// Se checan asociaciones de los deals candidatos a historicos con swaps
		// Solo cuando todos los deals de un mismo grupo de swaps se pueden
		// mover a Historicos se
		// mueven junto con su swap, precio referencia, spread.
		for (Iterator iterator = deals.iterator(); iterator.hasNext();) {
			Deal deal = (Deal) iterator.next();
			if (deal.getSwap() != null) {
				boolean swa = false;
				List dealsSwap = getHibernateTemplate()
						.find(
								"SELECT d FROM Deal AS d WHERE d.swap.idFolioSwap = ? ",
								new Integer(deal.getSwap().getIdFolioSwap()));
				for (Iterator iterator1 = dealsSwap.iterator(); iterator1
						.hasNext();) {
					Deal dw = (Deal) iterator1.next();
					swa = deals.contains(dw);
				}
				if (!swa) {
					iterator.remove();
				}
			}
		}
		for (Iterator iterator = deals.iterator(); iterator.hasNext();) {
			Deal deal = (Deal) iterator.next();
			inicializarDeal(deal);
			// escribe historicos de encabezados
			HistoricoDeal hisDeal = new HistoricoDeal(deal);
			store(hisDeal);
			dealHis.add(deal);
			for (Iterator iterator1 = deal.getDetalles().iterator(); iterator1
					.hasNext();) {
				DealDetalle dd = (DealDetalle) iterator1.next();
				// escribe historicos de detalles
				HistoricoDealDetalle hdd = new HistoricoDealDetalle(dd);
				store(hdd);
				// escribe historicos de posicion
				HistoricoDealPosicion hdp = new HistoricoDealPosicion(dd);
				store(hdp);
				dealDetalleHis.add(dd);
				// No se respalda el precio referencia debido a que
				// el detalle tiene el valor directo precioReferenciaMidSpot
				// y precioReferenciaSpot
				if (dd.getIdSpread() > 0) {
					List spreadRef = getHibernateTemplate().find(
							"FROM Spread AS s WHERE s.idSpread like ?",
							new Integer(dd.getIdSpread()));
					if (spreadRef.size() > 0) {
						if (!spreadHis.contains(spreadRef.get(0))) {
							spreadHis.add(spreadRef.get(0));
						}
					}
				}
				// se mueven todos los logs que ese deal-detalle pueda tener
				List dealDetallesStatusLog = getHibernateTemplate()
						.find(
								"FROM DealDetalleStatusLog"
										+ " AS ddsl WHERE ddsl.dealDetalle.idDealPosicion like ?",
								new Integer(dd.getIdDealPosicion()));
				for (Iterator iterator2 = dealDetallesStatusLog.iterator(); iterator2
						.hasNext();) {
					DealDetalleStatusLog ddsl = (DealDetalleStatusLog) iterator2
							.next();
					HistoricoDealDetalleStatusLog hddsl = new HistoricoDealDetalleStatusLog(
							ddsl);
					store(hddsl);
					dealDetalleStatusLogHis.add(ddsl);
				}
			}
			// se mueven todos los logs que ese deal pueda tener
			List dealLog = findLogByDealTodos(deal.getIdDeal());
			for (Iterator iterator2 = dealLog.iterator(); iterator2.hasNext();) {
				DealLog dl = (DealLog) iterator2.next();
				HistoricoDealLog hdLog = new HistoricoDealLog(dl);
				store(hdLog);
				dealLogHis.add(dl);
			}
			// se mueven todos los cambios en el estatus (log de Deal) que un
			// deal haya tenido
			List dealsStatusLog = getHibernateTemplate().find(
					"FROM DealStatusLog AS dsl WHERE "
							+ "dsl.deal.idDeal like ?",
					new Integer(deal.getIdDeal()));
			for (Iterator iterator3 = dealsStatusLog.iterator(); iterator3
					.hasNext();) {
				DealStatusLog dsl = (DealStatusLog) iterator3.next();
				HistoricoDealStatusLog hdsl = new HistoricoDealStatusLog(dsl);
				store(hdsl);
				dealStatusLogHis.add(dsl);

			}
			// se hace una lista con los swaps que pueden moverse
			if (deal.getSwap() != null
					&& !(swapHis.contains(new Integer((deal.getSwap()
							.getIdFolioSwap()))))) {
				swapHis.add(new Integer(deal.getSwap().getIdFolioSwap()));
			}
		}
		// Se mueven los swaps.
		for (Iterator iterator3 = swapHis.iterator(); iterator3.hasNext();) {
			Integer i = (Integer) iterator3.next();
			List swapsBorrar = getHibernateTemplate().find(
					"FROM Swap AS s WHERE s.idFolioSwap like ?", i);
			HistoricoSwap hs = new HistoricoSwap((Swap) swapsBorrar.get(0));
			store(hs);
		}

		// Se mueven los spreads
		for (Iterator iterator3 = spreadHis.iterator(); iterator3.hasNext();) {
			HistoricoSpread hs = new HistoricoSpread((Spread) iterator3.next());
			store(hs);
		}
		// Generacion de Historicos de Posicion
		List posicion = findAll(Posicion.class);
		for (Iterator iterator = posicion.iterator(); iterator.hasNext();) {
			Posicion pos = (Posicion) iterator.next();
			HistoricoPosicion hs = new HistoricoPosicion(pos);
			store(hs);
			for (Iterator iterator1 = pos.getDetalles().iterator(); iterator1
					.hasNext();) {
				PosicionDetalle pd = (PosicionDetalle) iterator1.next();
				HistoricoPosicionDetalle hpd = new HistoricoPosicionDetalle(pd);
				store(hpd);
				posicionDetalleHis.add(pd);
			}
			posicionHis.add(pos);
		}
		for (Iterator iterator = dealLogHis.iterator(); iterator.hasNext();) {
			DealLog dl = (DealLog) iterator.next();
			delete(dl);
		}
		for (Iterator iterator = dealStatusLogHis.iterator(); iterator
				.hasNext();) {
			DealStatusLog dsl = (DealStatusLog) iterator.next();
			delete(dsl);
		}
		for (Iterator iterator = dealDetalleStatusLogHis.iterator(); iterator
				.hasNext();) {
			DealDetalleStatusLog ddsl = (DealDetalleStatusLog) iterator.next();
			delete(ddsl);
		}
		for (Iterator iterator = dealDetalleHis.iterator(); iterator.hasNext();) {
			DealDetalle dd = (DealDetalle) iterator.next();
			delete(dd);
		}
		for (Iterator iterator = dealHis.iterator(); iterator.hasNext();) {
			Deal d = (Deal) iterator.next();
			delete(d);
		}

		return logger;
	}

	/**
	 * @see com.ixe.ods.sica.sdo.CierreSicaServiceData#isEstadoOperacionNormal()
	 */
	public boolean isEstadoOperacionNormal() {
		Estado estadoActual = (Estado) getHibernateTemplate().findByNamedQuery(
				"findEstadoActual").get(0);
		if (Estado.ESTADO_OPERACION_NORMAL == estadoActual.getIdEstado()) {
			return true;
		} else {
			return Estado.ESTADO_OPERACION_RESTRINGIDA == estadoActual
					.getIdEstado();
		}
	}

	/**
	 * @see com.ixe.ods.sica.sdo.SicaServiceData#findParametro(java.lang.String)
	 */
	public String findParametro(String idParametro) {
		return ((ParametroSica) find(ParametroSica.class, idParametro))
				.getValor();
	}

	/**
	 * @see com.ixe.ods.sica.sdo.ServiceData#findPrecioReferenciaActual()
	 */
	public PrecioReferenciaActual findPrecioReferenciaActual() {
		PrecioReferenciaActual pra = (PrecioReferenciaActual) getHibernateTemplate()
				.loadAll(PrecioReferenciaActual.class).get(0);
		return pra;
	}

	/**
	 * Obtiene el Id del Administrador del Sistema.
	 * 
	 * @return Integer Id del Administrador del Sistema.
	 */
	public Integer getIdAdministrador() {
		ParametroSica idUsuario = (ParametroSica) find(ParametroSica.class,
				ParametroSica.ADMIN_ID_USUARIO);
		return new Integer(idUsuario.getValor());
	}

	/**
	 * Obtiene las personas seg&uacute;n la Facultad.
	 * 
	 * @param facultad
	 *            La Facultad por la cual se hace la busqueda.
	 * @param sistema
	 *            El Sistema al cual la facultad pertenece.
	 * @return Un <code> List </code> con Personas que pertenecen a la Facultad
	 *         Consultada.
	 */
	private List getPersonasPorFacultad(String facultad, String sistema) {
		if ("SICA_RSG_ALRM_ALTA".equals(facultad)) {
			return getExtraDao().findPersonasWithFacultyAndSystem(
					"SICA_RSG_ALRM_ALTA", sistema);
		} else if ("SICA_RSG_ALRM_MEDIA".equals(facultad)) {
			return getExtraDao().findPersonasWithFacultyAndSystem(
					"SICA_RSG_ALRM_MEDIA", sistema);
		} else if ("SICA_RSG_ALRM_BAJA".equals(facultad)) {
			return getExtraDao().findPersonasWithFacultyAndSystem(
					"SICA_RSG_ALRM_BAJA", sistema);
		}
		return null;
	}

	/**
	 * Obtiene la referencia al bean del Sistemas de Alarmas.
	 * 
	 * @return SicaAlertasService
	 */
	public SicaAlertasService getSicaAlertasService() {
		return _sicaAlertasService;
	}

	/**
	 * Obtiene referencia al bean de Seguridad para obtener Facultades
	 * 
	 * @return ExtraDao
	 */
	public ExtraDao getExtraDao() {
		return _extraDao;
	}

	/**
	 * Obtiene referencia al bean CierreDao.
	 * 
	 * @return CierreDao
	 */
	public CierreDao getCierreDao() {
		return _cierreDao;
	}

	/**
	 * Obtiene referencia al bean de PizarronServiceData
	 * 
	 * @return PizarronServiceData
	 */
	public PizarronServiceData getPizarronServiceData() {
		return _pizarronServiceData;
	}

	/** Apagado de Phoenix */
//	/**
//	 * Obtiene referencia al bean PhoenixDao.
//	 * 
//	 * @return PhoenixDao.
//	 */
//	public PhoenixDao getPhoenixDao() {
//		return _phoenixDao;
//	}

	/**
	 * Obtiene los Promotores seg&uacute;n la l&iacute;nea de Cr&eacute;dito
	 * 
	 * @param lineaCredito
	 *            La linea de Cr&eacute;dito por la cual se obtienen los
	 *            promotores.
	 * @return Un <code>List</code> de Promotores con la l&iacute;nea de
	 *         cr&eacute;dito que se consult&oacute;.
	 */
	public List getPromotoresPorLineaDeCredito(LineaCambio lineaCredito) {
		List idPersonas = new ArrayList();
		List idPromotores = new ArrayList();
		idPersonas.add(lineaCredito.getCorporativo().getIdPersona());
		if (!("PM".equals(lineaCredito.getCorporativo().getTipoPersona()))) {
			return idPromotores;
		}
		List l = getCierreDao().findPromotoresPorLineaDeCredito(
				lineaCredito.getCorporativo().getIdPersona());
		for (Iterator it = l.iterator(); it.hasNext();) {
			Object[] obj = (Object[]) it.next();
			idPromotores.add(obj[0]);
		}
		return idPromotores;
	}

	/**
     * 
     */
	public void cambiarEstadoCierre() {
		Estado cierre = (Estado) getHibernateTemplate().findByNamedQuery(
				"findEstadoActual").get(0);
		cierre.setActual(false);
		update(cierre);
		List estados = findAll(Estado.class);
		for (Iterator it = estados.iterator(); it.hasNext();) {
			Estado estado = (Estado) it.next();
			if (estado.getIdEstado() == Estado.ESTADO_CIERRE_DIA) {
				estado.setActual(true);
				estado.setHoraInicio(HOUR_FORMAT.format(new Date()));
			}
		}
		update(estados);
	}

	/**
	 * Realiza el Cierre y escribe en el logger.
	 * 
	 * @param logger
	 *            <code>String</code> con la informaci&oacute;n del log del
	 *            cierre.
	 * @return StringBuffer El log del Cierre.
	 * @throws SicaException
	 *             Si algo sale mal.
	 */
	private StringBuffer hacerCierre(StringBuffer logger) throws SicaException {
		int numWarnings = 0;
		int numErrors = 0;
		
		numErrors = validarDealsCierreDelDia(numErrors, logger);
		if (numErrors == 0) {
			limpiarOperacionesClientes(logger);
			numWarnings = revisarVencimientosLineaCredito(numWarnings, logger);
			realizarCorrimientoLineasCambio();
			limpiarExcepcionesLineasCredito(logger);
			logger.append(Calendar.getInstance().getTime()).append(
					" *** Revision de los limites " + "de riesgo \n");
			checarLimites(true);
			realizarCorrimientoFechaValor(logger);
			limpiarTablas(logger);
			terminarCierreSica();
			// Se Agrega el Bloqueo de la Facultad de Promotor de Guardia
			bloquearFacultadGuardia(true);
			// inactivarContratosSicaSinDeal(logger);
			// Fin Bloqueo Facultad Promotor de Guardia.
			// Verifica consistencias de listas blancas ISIS-Phoenix
			//corrigeInconsistenciasIsisPhoenix(logger); /** Apagado de Phoenix */

			// Copia los mensajes banner al histórico
			actualizaHistoricosMensaje(logger); //2012-1226 Se comenta por error en cierre final.
			
			generarBackupReportesRegulatorios(logger); // 24/sep/2015
			
			reiniciarContadoresExcesosLineasCreditoActivas(logger); // 16/08/2016
		} else {
			HashMap contextoAlarma = new HashMap();
			contextoAlarma.put("NUM_ERRORS", String.valueOf(numErrors));
			generaAlerta("SC_CR_ER", getIdAdministrador(), contextoAlarma);
		}
		logger.append(Calendar.getInstance().getTime()).append(
				" *** Se termino cierre de dia \n");
		return logger;
	}
	
	private void reiniciarContadoresExcesosLineasCreditoActivas(StringBuffer logger)
	{
		logger.append("Inicia validacion de 'reiniciarContadoresExcesosLineasCreditoActivas' \n");
		SicaServiceData sicaServiceData = null;
		try
		{
			sicaServiceData = (SicaServiceData)_appContext.getBean("sicaServiceData");
			sicaServiceData.reiniciarContadoresExcesosLineasCreditoActivas(logger);
			logger.append("---> Se valido correctamente el reinicio de contadores de excesos.\n");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			logger.append("---> el reinicio de contadores de excesos termino con errores.\n");
			logger.append("Ocurrio un error al realizar el reinicio de los contadores de excesos de las lineas de credito\n");
			logger.append(e.getLocalizedMessage() + "\n");
		}
		logger.append("Fin de validacion de 'reiniciarContadoresExcesosLineasCreditoActivas' \n");
		warn(logger.toString());
	}
	
	private void generarBackupReportesRegulatorios(StringBuffer logger)
	{
		SicaServiceData sicaServiceData = null;
		try
		{
			sicaServiceData = (SicaServiceData)_appContext.getBean("sicaServiceData");
			sicaServiceData.generarBackupReportesRegulatorios(logger);
			warn(logger.toString());
			logger.append("---> Ha finalizado el backup de reportes regulatorios.\n");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			logger.append("---> Ha finalizado el backup de reportes regulatorios con errores.\n");
		}
	}

	/**
	 * Limpia las excepciones al cambio de Mes de todas la Lineas de
	 * Cr&eacute;dito.
	 * 
	 * @param logger
	 *            El StringBuffer del Log.
	 * @throws SicaException
	 *             Si ocurre un error al calcular la diferencia en d&iacute;as
	 *             de fechas valor.
	 */
	private void limpiarExcepcionesLineasCredito(StringBuffer logger)
			throws SicaException {
		Calendar logTime = Calendar.getInstance();
		Calendar diaTom = new GregorianCalendar();
		Calendar diaCash = new GregorianCalendar();
		diaCash.setTime(getFechaSistema());
		diaTom.setTime(getPizarronServiceData().getFechaTOM(getFechaSistema()));
		if ((diaTom.get(Calendar.MONTH) != diaCash.get(Calendar.MONTH))) {
			logger.append(logTime.getTime()).append(
					" *** Se borra el numero de excepciones de " + "lineas\n");
			List lcs = findAll(LineaCambio.class);
			for (Iterator it = lcs.iterator(); it.hasNext();) {
				LineaCambio lc = (LineaCambio) it.next();
				lc.setNumeroExcepcionesMes(0);
				update(lc);
			}
		}
	}

	/**
	 * Obtiene todas las l&iacute;neas de cr&eacute;dito. Si la l&iacute;nea
	 * tiene un uso no en remesas mayor a 10000 USD, se suspende la l&iacute;nea
	 * de cr&eacute;dito. Posteriormente se realiza el corrimiento de saldos por
	 * fecha valor.
	 */
	private void realizarCorrimientoLineasCambio() {
		LineaCambioDao lcDao = (LineaCambioDao) _appContext
				.getBean("lineaCambioDao");
		/*warn("Cuadrando l\u00edneas de cambio...");
		List totalesCuadre = lcDao.cuadrarLineasCambio();
		for (Iterator it = totalesCuadre.iterator(); it.hasNext();) {
			warn(it.next());
		}
		warn("L\u00edneas de cambio cuadradas.");*/
		List lineasCambio = getHibernateTemplate().find("FROM LineaCambio");
		int diasPermitidos = Integer.valueOf(
				findParametro(ParametroSica.DIAS_EXCEPCION_LC)).intValue();
		ParametroSica param = (ParametroSica) find(ParametroSica.class,
				ParametroSica.LIM_ADEUDO_LIN_CAM);
		double limiteAdeudo = Double.valueOf(param.getValor()).doubleValue();

		for (Iterator it = lineasCambio.iterator(); it.hasNext();) {
			LineaCambio lineaCambio = (LineaCambio) it.next();
			/**
			 * Se elimina resta con UsoRemCash lineaCambio.getUsoCash().subtract(lineaCambio.getUsoRemCash())
			 */
			if (lineaCambio.getUsoCash()
					.doubleValue() > limiteAdeudo) {
				if (lineaCambio.isCasoExcepcion()) {
					// Se obtiene la fecha minima en que tiene adeudo el
					// cliente:
					int diasAdeudo = lcDao
							.getDiasAdeudoParaLineaCambio(lineaCambio
									.getIdLineaCambio().intValue());
					debug("L\u00ednea " + lineaCambio.getIdLineaCambio()
							+ " con " + diasAdeudo + " d\u00edas de adeudo.");
					if (diasAdeudo > diasPermitidos) {
						lineaCambio
								.setStatusLinea(LineaCambio.STATUS_SUSPENDIDA);
						debug("L\u00ednea " + lineaCambio.getIdLineaCambio()
								+ " con excepcion suspendida.");
					}
				} else {
					lineaCambio.setStatusLinea(LineaCambio.STATUS_SUSPENDIDA);
					debug("L\u00ednea " + lineaCambio.getIdLineaCambio()
							+ " suspendida.");
				}
			}
			lineaCambio.realizarCorrimiento();
			update(lineaCambio);
		}
	}

	/**
	 * Limpiar el monto de Operaciones y el n&uacute;mero de Operaciones de
	 * Clientes Recurrentes al fin de Mes.
	 * 
	 * @param logger
	 *            El objeto para logging.
	 * @throws SicaException
	 *             Si ocurre un error al calcular la diferencia en d&iacute;as
	 *             de fechas valor.
	 */
	private void limpiarOperacionesClientes(StringBuffer logger)
			throws SicaException {
		Calendar diaCash = new GregorianCalendar();
		diaCash.setTime(getFechaSistema());
		Calendar diaTom = Calendar.getInstance();
		diaTom.setTime(getPizarronServiceData().getFechaTOM(getFechaSistema()));
		Calendar logTime = Calendar.getInstance();
		if ((diaTom.get(Calendar.MONTH) != diaCash.get(Calendar.MONTH))) {
			StringBuffer sb = new StringBuffer(
					"FROM ContratoSica AS cs WHERE "
							+ "(cs.montoOperacionesMes > 0 OR cs.numeroOperacionesMes > 0)");
			List cs = getHibernateTemplate().find(sb.toString());
			logger.append(logTime.getTime()).append(
					" *** Se borran las operaciones del mes de "
							+ "Clientes no recurrentes\n");
			for (Iterator it = cs.iterator(); it.hasNext();) {
				ContratoSica cs1 = (ContratoSica) it.next();
				cs1.setMontoOperacionesMes(0);
				cs1.setNumeroOperacionesMes(0);
				update(cs1);
			}
		}
	}

	/**
	 * Seg&uacute;n los p&aacute;rametros recibidos compara los valores
	 * permitidos para cada l&iacute;mite env&iacute;a una alerta o una alarma
	 * seg&uacute; sea el caso.
	 * 
	 * @param limite
	 *            El L&iacute;mite por el cual se hacen los c&aacute;lculos.
	 * @param posicionGlobal
	 *            El valor de la posici&oacute; para ese l&iacute;mite.
	 * @param divisa
	 *            La divisa por la cual se va a checar.
	 * @param mesaCambio
	 *            El Nombre de la Mesa.
	 * @param corta
	 *            Nos dice si este limite pertenece a Posici&oacute; corta.
	 */
	private void logicaAlarmaAviso(Limite limite, double posicionGlobal,
			String divisa, String mesaCambio, boolean corta) {
		String tipoDeAlarma;
		TipoLimite tipoLimite = limite.getTipoLimite();
		double limiteAlarma = limite.getLimite()
				* (limite.getPorcentajeAlarma() / 100);
		double limiteAviso = limite.getLimite()
				* (limite.getPorcentajeAviso() / 100);
		if (!corta) {
			if (posicionGlobal > limite.getLimite()) {
				tipoDeAlarma = "total";
				sendAlertaRiesgo(tipoDeAlarma, posicionGlobal, tipoLimite
						.getNombre(), divisa, mesaCambio);
				return;
			}
			if (posicionGlobal > limiteAlarma) {
				tipoDeAlarma = "alarma";
				sendAlertaRiesgo(tipoDeAlarma, posicionGlobal, tipoLimite
						.getNombre(), divisa, mesaCambio);
				return;
			}
			if (posicionGlobal > limiteAviso) {
				tipoDeAlarma = "aviso";
				sendAlertaRiesgo(tipoDeAlarma, posicionGlobal, tipoLimite
						.getNombre(), divisa, mesaCambio);
			}
		} else {
			if (posicionGlobal < limite.getLimite()) {
				tipoDeAlarma = "total";
				sendAlertaRiesgo(tipoDeAlarma, posicionGlobal, tipoLimite
						.getNombre(), divisa, mesaCambio);
				return;
			}
			if (posicionGlobal < limiteAlarma) {
				tipoDeAlarma = "alarma";
				sendAlertaRiesgo(tipoDeAlarma, posicionGlobal, tipoLimite
						.getNombre(), divisa, mesaCambio);
				return;
			}
			if (posicionGlobal < limiteAviso) {
				tipoDeAlarma = "aviso";
				sendAlertaRiesgo(tipoDeAlarma, posicionGlobal, tipoLimite
						.getNombre(), divisa, mesaCambio);
			}
		}
	}

	/**
	 * Escribe en PosicionDetalle y mueve los valores Spot a Tom y Tom a Cash y
	 * escribe cero en Spot. Escribe los Hist&oacute;ricos por cada Divisa
	 * Frecuente.
	 * 
	 * @param logger
	 *            El StringBuffer del log.
	 * @throws SicaException
	 *             Si algo sale mal.
	 */
	private void realizarCorrimientoFechaValor(StringBuffer logger)
			throws SicaException {
		Calendar logTime = Calendar.getInstance();
		Calendar fechaHoy = new GregorianCalendar();
		fechaHoy.setTime(getFechaSistema());
		// Le quitamos los milisegundos,segundos y minutos.
		fechaHoy = setFecha(fechaHoy);
		logger.append(logTime.getTime()).append(
				" *** Se realiza corrimiento fecha valor \n");
		List posicionesDetalles = findAll(PosicionDetalle.class);
		for (Iterator it = posicionesDetalles.iterator(); it.hasNext();) {
			PosicionDetalle pd = (PosicionDetalle) it.next();
			PosIni posIni = pd.getPosIni();

			posIni.setPosicionInicialCash(posIni.getPosicionInicialCash()
					+ pd.getCpaVta().getCompraCash()
					- pd.getCpaVta().getVentaCash()
					+ posIni.getPosicionInicialTom()
					+ pd.getCpaVta().getCompraTom()
					- pd.getCpaVta().getVentaTom());
			posIni.setPosicionInicialTom(posIni.getPosicionInicialSpot()
					+ pd.getCpaVta().getCompraSpot()
					- pd.getCpaVta().getVentaSpot());
			// Se anade recorrimiento de 72Hr.
			posIni.setPosicionInicialSpot(posIni.getPosicionInicial72Hr()
					.doubleValue()
					+ pd.getCpaVta().getCompra72Hr().doubleValue()
					- pd.getCpaVta().getVenta72Hr().doubleValue());
			// Se anade recorrimiento de VFut.
			posIni.setPosicionInicial72Hr(new Double(posIni
					.getPosicionInicialVFut().doubleValue()
					+ pd.getCpaVta().getCompraVFut().doubleValue()
					- pd.getCpaVta().getVentaVFut().doubleValue()));
			posIni.setPosicionInicialVFut(new Double(0));
			posIni.setPosicionInicialMnCash(posIni.getPosicionInicialMnCash()
					+ pd.getCpaVtaMn().getCompraMnClienteCash()
					- pd.getCpaVtaMn().getVentaMnClienteCash()
					+ posIni.getPosicionInicialMnTom()
					+ pd.getCpaVtaMn().getCompraMnClienteTom()
					- pd.getCpaVtaMn().getVentaMnClienteTom());
			posIni.setPosicionInicialMnTom(posIni.getPosicionInicialMnSpot()
					+ pd.getCpaVtaMn().getCompraMnClienteSpot()
					- pd.getCpaVtaMn().getVentaMnClienteSpot());
			// Se anade recorrimiento de 72Hr.
			posIni.setPosicionInicialMnSpot(posIni.getPosicionInicialMn72Hr()
					.doubleValue()
					+ pd.getCpaVtaMn().getCompraMnCliente72Hr().doubleValue()
					- pd.getCpaVtaMn().getVentaMnCliente72Hr().doubleValue());
			// Se anade recorrimiento de VFut.
			posIni.setPosicionInicialMn72Hr(new Double(posIni
					.getPosicionInicialMnVFut().doubleValue()
					+ pd.getCpaVtaMn().getCompraMnClienteVFut().doubleValue()
					- pd.getCpaVtaMn().getVentaMnClienteVFut().doubleValue()));
			posIni.setPosicionInicialMnVFut(new Double(0));
			pd.getCpaVta().setCompraCash(0.0);
			pd.getCpaVta().setCompraTom(0.0);
			pd.getCpaVta().setCompraSpot(0.0);
			// Se anade recorrimiento de 72Hr.
			pd.getCpaVta().setCompra72Hr(new Double(0));
			// Se anade recorrimiento de VFut.
			pd.getCpaVta().setCompraVFut(new Double(0));
			pd.getCpaVtaIn().setCompraInCash(pd.getCpaVtaIn().getCompraInTom());
			pd.getCpaVtaIn().setCompraInTom(pd.getCpaVtaIn().getCompraInSpot());
			pd.getCpaVtaIn().setCompraInSpot(
					pd.getCpaVtaIn().getCompraIn72Hr().doubleValue());
			// Se anade recorrimiento de 72Hr.
			pd.getCpaVtaIn()
					.setCompraIn72Hr(pd.getCpaVtaIn().getCompraInVFut());
			// Se anade recorrimiento de VFut.
			pd.getCpaVtaIn().setCompraInVFut(new Double(0));
			pd.getCpaVtaMn().setCompraMnClienteCash(0);
			pd.getCpaVtaMn().setCompraMnClienteTom(0);
			pd.getCpaVtaMn().setCompraMnClienteSpot(0);
			// Se anade recorrimiento de 72Hr.
			pd.getCpaVtaMn().setCompraMnCliente72Hr(new Double(0));
			// Se anade recorrimiento de VFut.
			pd.getCpaVtaMn().setCompraMnClienteVFut(new Double(0));
			pd.getCpaVtaMn().setCompraMnPizarronCash(0);
			pd.getCpaVtaMn().setCompraMnPizarronTom(0);
			pd.getCpaVtaMn().setCompraMnPizarronSpot(0);
			// Se anade recorrimiento de 72Hr.
			pd.getCpaVtaMn().setCompraMnPizarron72Hr(new Double(0));
			// Se anade recorrimiento de VFut.
			pd.getCpaVtaMn().setCompraMnPizarronVFut(new Double(0));
			pd.getCpaVtaMn().setCompraMnMesaCash(0.00);
			pd.getCpaVtaMn().setCompraMnMesaTom(0.0);
			pd.getCpaVtaMn().setCompraMnMesaSpot(0.0);
			// Se anade recorrimiento de 72Hr.
			pd.getCpaVtaMn().setCompraMnMesa72Hr(new Double(0));
			// Se anade recorrimiento de VFut.
			pd.getCpaVtaMn().setCompraMnMesaVFut(new Double(0));
			pd.getCpaVta().setVentaCash(0.00);
			pd.getCpaVta().setVentaTom(0.00);
			pd.getCpaVta().setVentaSpot(0.0);
			// Se anade recorrimiento de 72Hr.
			pd.getCpaVta().setVenta72Hr(new Double(0));
			// Se anade recorrimiento de VFut.
			pd.getCpaVta().setVentaVFut(new Double(0));
			pd.getCpaVtaIn().setVentaInCash(pd.getCpaVtaIn().getVentaInTom());
			pd.getCpaVtaIn().setVentaInTom(pd.getCpaVtaIn().getVentaInSpot());
			pd.getCpaVtaIn().setVentaInSpot(
					pd.getCpaVtaIn().getVentaIn72Hr().doubleValue());
			// Se anade recorrimiento de 72Hr.
			pd.getCpaVtaIn().setVentaIn72Hr(pd.getCpaVtaIn().getVentaInVFut());
			// Se anade recorrimiento de VFut.
			pd.getCpaVtaIn().setVentaInVFut(new Double(0));
			pd.getCpaVtaMn().setVentaMnClienteCash(0.00);
			pd.getCpaVtaMn().setVentaMnClienteTom(0.0);
			pd.getCpaVtaMn().setVentaMnClienteSpot(0.0);
			// Se anade recorrimiento de 72Hr.
			pd.getCpaVtaMn().setVentaMnCliente72Hr(new Double(0));
			// Se anade recorrimiento de VFut.
			pd.getCpaVtaMn().setVentaMnClienteVFut(new Double(0));
			pd.getCpaVtaMn().setVentaMnPizarronCash(0.00);
			pd.getCpaVtaMn().setVentaMnPizarronTom(0.0);
			pd.getCpaVtaMn().setVentaMnPizarronSpot(0.0);
			// Se anade recorrimiento de 72Hr.
			pd.getCpaVtaMn().setVentaMnPizarron72Hr(new Double(0));
			// Se anade recorrimiento de VFut.
			pd.getCpaVtaMn().setVentaMnPizarronVFut(new Double(0));
			pd.getCpaVtaMn().setVentaMnMesaCash(0.00);
			pd.getCpaVtaMn().setVentaMnMesaTom(0.0);
			pd.getCpaVtaMn().setVentaMnMesaSpot(0.0);
			// Se anade recorrimiento de 72Hr.
			pd.getCpaVtaMn().setVentaMnMesa72Hr(new Double(0));
			// Se anade recorrimiento de VFut.
			pd.getCpaVtaMn().setVentaMnMesaVFut(new Double(0));
		}
		update(posicionesDetalles);
		Calendar gc = new GregorianCalendar();
		gc.add(Calendar.DAY_OF_MONTH, -101);
		gc.add(Calendar.HOUR_OF_DAY, -gc.get(Calendar.HOUR_OF_DAY));
		gc.add(Calendar.MINUTE, -gc.get(Calendar.MINUTE));
		gc.add(Calendar.SECOND, -gc.get(Calendar.SECOND));
		Calendar gc2 = new GregorianCalendar();
		gc2.setTime(gc.getTime());
		gc2.add(Calendar.DAY_OF_MONTH, 1);
		gc2.add(Calendar.SECOND, -1);
		List tcc = getHibernateTemplate().findByNamedQuery(
				"findTipoCambioCierreCienUltimosDias",
				new Object[] { gc.getTime(), gc2.getTime() });
		for (Iterator it = tcc.iterator(); it.hasNext();) {
			HistoricoTipoCambio hcc = (HistoricoTipoCambio) it.next();
			delete(hcc);
		}
		logger.append(logTime.getTime()).append(
				" *** Se actualizan historicos tipo de cambio\n");
		List div = getHibernateTemplate().findByNamedQuery(
				"findDivisasFrecuentes");
		PrecioReferenciaActual pr = findPrecioReferenciaActual();
		Double precioSpot = pr.getPreRef().getMidSpot();
		for (Iterator it = div.iterator(); it.hasNext();) {
			Divisa divisa = (Divisa) it.next();
			HistoricoTipoCambio hcc = new HistoricoTipoCambio();
			hcc.setDivisa(divisa);
			hcc.setFecha(fechaHoy.getTime());
			hcc.setTipoCambio(getFactorConversionFromTo(divisa.getIdDivisa(),
					Divisa.DOLAR)
					* precioSpot.doubleValue());
			store(hcc);
		}
	}

	/**
	 * Revisa el nivel de los l&iacute;mites de riesgo.
	 * 
	 * @param limites
	 *            La lista de l&iacute;mites.
	 * @param idDivisa
	 *            La clave de la divisa o null.
	 * @param mesaCambio
	 *            La mesa de cambio.
	 * @param cierre
	 *            Si es para el cierre o para el timer de notificaci&oacute;n.
	 * @throws SicaException
	 *             Si algo sale mal.
	 */
	private void revisarLimitesDeRiesgo(List limites, String idDivisa,
			MesaCambio mesaCambio, boolean cierre) throws SicaException {
		if (idDivisa == null) {
			idDivisa = "todas";
		}
		boolean corta = false;
		if (!limites.isEmpty()) {
			for (Iterator it = limites.iterator(); it.hasNext();) {
				Limite lm = (Limite) it.next();
				TipoLimite tipl = lm.getTipoLimite();
				if ((TipoLimite.PL == tipl.getIdTipoLimite()) && cierre
						&& tipl.isAlCierre() && lm.getPorcentajeAlarma() > 0.0
						&& lm.getPorcentajeAviso() > 0.0) {
					logicaAlarmaAviso(lm, lm.getNivel(), idDivisa, mesaCambio
							.getNombre(), corta);
				}
				if ((TipoLimite.PC == tipl.getIdTipoLimite())
						&& lm.getNivel() < 0.0 && cierre && tipl.isAlCierre()
						&& lm.getPorcentajeAlarma() > 0.0
						&& lm.getPorcentajeAviso() > 0.0) {
					corta = true;
					logicaAlarmaAviso(lm, lm.getNivel(), idDivisa, mesaCambio
							.getNombre(), corta);
					corta = false;
				}
				if ((TipoLimite.SL == tipl.getIdTipoLimite())
						&& lm.getNivel() != 0 && !tipl.isAlCierre()
						&& lm.getPorcentajeAlarma() > 0.0
						&& lm.getPorcentajeAviso() > 0.0) {
					logicaAlarmaAviso(lm, lm.getNivel(), idDivisa, mesaCambio
							.getNombre(), corta);
				}
				if ((TipoLimite.V == tipl.getIdTipoLimite())
						&& !tipl.isAlCierre() && lm.getPorcentajeAlarma() > 0.0
						&& lm.getPorcentajeAviso() > 0.0) {
					logicaAlarmaAviso(lm, lm.getNivel(), idDivisa, mesaCambio
							.getNombre(), corta);
				}
				if ((TipoLimite.PLI == tipl.getIdTipoLimite()) && !cierre
						&& lm.getPorcentajeAlarma() > 0.0
						&& lm.getPorcentajeAviso() > 0.0) {
					logicaAlarmaAviso(lm, lm.getNivel(), idDivisa, mesaCambio
							.getNombre(), corta);
				}
				if ((TipoLimite.PCI == tipl.getIdTipoLimite())
						&& lm.getNivel() < 0.0 && !cierre
						&& lm.getPorcentajeAlarma() > 0.0
						&& lm.getPorcentajeAviso() > 0.0) {
					corta = true;
					logicaAlarmaAviso(lm, lm.getNivel(), idDivisa, mesaCambio
							.getNombre(), corta);
					corta = false;
				}
			}
		}
	}

	/**
	 * Avisa si alguna de las Lineas de Cr&eacute;dito ha vencido o est&aacute;
	 * por vencer.
	 * 
	 * @param numWarnings
	 *            El n&uacute;mero de warnings actuales.
	 * @param logger
	 *            El StringBuffer para el log.
	 * @return int n&uacute;mero de warnings generados
	 */
	private int revisarVencimientosLineaCredito(int numWarnings,
			StringBuffer logger) {
		Calendar fechaHoy = new GregorianCalendar();
		Calendar logTime = Calendar.getInstance();
		IUsuario usuario =  getSeguridadServiceData().getUsuario(getIdAdministrador());
		fechaHoy.setTime(getFechaSistema());
		fechaHoy = setFecha(fechaHoy);
		List lc = findAll(LineaCambio.class);
		ParametroSica diasVencimiento = (ParametroSica) getHibernateTemplate()
				.load(ParametroSica.class, ParametroSica.DIAS_VENCIMIENTO_LINEA);
		logger.append(logTime.getTime()).append(
				" *** Generacion de avisos de lineas de credito\n");
		for (Iterator it = lc.iterator(); it.hasNext();) {
				LineaCambio l = (LineaCambio) it.next();
				Calendar fechaVencimiento = new GregorianCalendar();
				fechaVencimiento.setTime(l.getVencimiento());
				fechaVencimiento = setFecha(fechaVencimiento);
				fechaVencimiento.add(Calendar.DAY_OF_WEEK, -(Integer
						.parseInt(diasVencimiento.getValor())));
				if (fechaHoy.equals(fechaVencimiento)
						|| fechaHoy.after(fechaVencimiento)) {
					logger.append(logTime.getTime()).append(
							"La Linea de Credito de ").append(
							l.getCorporativo().getNombreCorto()).append(
							" ha vencido o est&aacute; por vencer ").append("\n");
					List idPromotores = getPromotoresPorLineaDeCredito(l);
					for (Iterator it3 = idPromotores.iterator(); it3.hasNext();) {
						Integer idPersona = (Integer) it3.next();
						HashMap contexto = new HashMap();
						contexto.put("NOMBRE_CORPORATIVO", l.getCorporativo()
								.getNombreCorto());
						generaAlerta("SC_WN_LI", idPersona, contexto);
					}
				}
				fechaVencimiento.setTime(l.getVencimiento());
				fechaVencimiento = setFecha(fechaVencimiento);
				if (fechaHoy.after(fechaVencimiento) && 
						(l.getStatusLinea().equals(LineaCambio.STATUS_ACTIVADA) || l.getStatusLinea().equals(LineaCambio.STATUS_SUSPENDIDA))) {
					if (l.getUsoTotal().doubleValue() > 0.0) {
						List idPromotores1 = getPromotoresPorLineaDeCredito(l);
						for (Iterator it2 = idPromotores1.iterator(); it2.hasNext();) {
							Integer idPersona = (Integer) it2.next();
							HashMap contexto = new HashMap();
							contexto.put("NOMBRE_CORPORATIVO", l.getCorporativo()
									.getNombreCorto());
							generaAlerta("SC_VE_LI", idPersona, contexto);
						}
						l.setStatusLinea(LineaCambio.STATUS_SUSPENDIDA);
						numWarnings++;
					} else {
						l.setStatusLinea(LineaCambio.STATUS_VENCIDA);
						LineaCambioLog vencimientoLog = new LineaCambioLog();
						vencimientoLog.setLineaCambio(l);				
						vencimientoLog.setImporte(new BigDecimal(0));
						vencimientoLog.setFechaOperacion(new Date());
						vencimientoLog.setTipoOper(LineaCambioLog.OPER_VENCIMIENTO);
						vencimientoLog.setUsuario(usuario);
						store(vencimientoLog);
					}
				}
				update(l);
		}
		return numWarnings;
	}

	/**
	 * Inactiva los contratos SICA que no hayan operado en un tiempo
	 * determinado.
	 * 
	 * @param logger
	 *            El logger de la aplicaci&oacute;n
	 */
	private void inactivarContratosSicaSinDeal(StringBuffer logger) {
		int diasAtras = Integer
				.parseInt(findParametro(FECHA_RANGO_CONTRATO_SICA));
		int regMod = getCierreDao().inactivaContratosSica(diasAtras);
		logger.append(Calendar.getInstance().getTime()).append(
				" *** Contratos SICA modificados: " + regMod);
	}

	/** Apagado de Phoenix */
//	/**
//	 * Verifica las tablas de phoenix para que sean consistentes con las del
//	 * SICA.
//	 * 
//	 * @param logger
//	 *            El StringBuffer del log.
//	 * @throws SicaException
//	 *             Si algo sale mal.
//	 */
//	private void corrigeInconsistenciasIsisPhoenix(StringBuffer logger)
//			throws SicaException {
//		Calendar logTime = Calendar.getInstance();
//		Calendar fechaHoy = new GregorianCalendar();
//		fechaHoy.setTime(getFechaSistema());
//		// Le quitamos los milisegundos,segundos y minutos.
//		fechaHoy = setFecha(fechaHoy);
//		logger.append(logTime.getTime()).append(
//				" *** Se revisan registros en ISIS y Phoenix \n");
//		getPhoenixDao().corrigeInconsistenciasIsisPhoenix();
//	}

	/**
	 * Establece el valor de sicaAlertasService.
	 * 
	 * @param sicaAlertasService
	 *            El valor a asignar.
	 */
	public void setSicaAlertasService(SicaAlertasService sicaAlertasService) {
		_sicaAlertasService = sicaAlertasService;
	}

	/**
	 * Fija el valor de extraDao.
	 * 
	 * @param extraDao
	 *            El valor a asignar.
	 */
	public void setExtraDao(ExtraDao extraDao) {
		_extraDao = extraDao;
	}

	/**
	 * Fija el valor de cierreDao.
	 * 
	 * @param cierreDao
	 *            El valor a asignar.
	 */
	public void setCierreDao(CierreDao cierreDao) {
		_cierreDao = cierreDao;
	}

	/**
	 * Fija el valor de pizarronServiceData.
	 * 
	 * @param pizarronServiceData
	 *            El valor a asignar.
	 */
	public void setPizarronServiceData(PizarronServiceData pizarronServiceData) {
		_pizarronServiceData = pizarronServiceData;
	}
/** Apagado de Phoenix */
//	/**
//	 * Fija el valor de phoenixDao.
//	 * 
//	 * @param phoenixDao
//	 *            El valor a asignar.
//	 */
//	public void setPhoenixDao(PhoenixDao phoenixDao) {
//		_phoenixDao = phoenixDao;
//	}

	/**
	 * Dependiendo el tipo de alerta llamamos a Personas por facultad .
	 * 
	 * @param tipo
	 *            El tipo de de alerta que se genera.
	 * @param monto
	 *            El monto por el cual se genera la alerta.
	 * @param nombreLimite
	 *            El nombre del L&iacute;mite
	 * @param divisa
	 *            La divisa que excedi&oacute; el l&iacute;mite.
	 * @param mesaCambio
	 *            La Mesa a la cual pertenece la divisa que excedi&oacute; el
	 *            l&iacute;mite.
	 */
	private void sendAlertaRiesgo(String tipo, double monto,
			String nombreLimite, String divisa, String mesaCambio) {
		HashMap context = new HashMap();
		List personasPorTipoDeFacultad = new ArrayList();
		if ("total".equals(tipo)) {
			personasPorTipoDeFacultad = getPersonasPorFacultad(
					"SICA_RSG_ALRM_ALTA", "SICA");
		} else if ("alarma".equals(tipo)) {
			personasPorTipoDeFacultad = getPersonasPorFacultad(
					"SICA_RSG_ALRM_MEDIA", "SICA");
		} else if ("aviso".equals(tipo)) {
			personasPorTipoDeFacultad = getPersonasPorFacultad(
					"SICA_RSG_ALRM_BAJA", "SICA");
		}
		context.put("MONTO_EXCEDIDO", String.valueOf(new Integer(new Double(
				monto).intValue()).intValue()));
		context.put("RIESGO_TYPE", nombreLimite);
		context.put("MESA", mesaCambio);
		context.put("DIVISA", divisa);
		for (Iterator it = personasPorTipoDeFacultad.iterator(); it.hasNext();) {
			Integer idPersona = (Integer) it.next();
			generaAlerta("SC_EX_LR", idPersona, context);
		}
	}

	/**
	 * Quita los minutos y los segundos a una fecha determinada para realizar
	 * c&aacute;lculos exactos entre fechas.
	 * 
	 * @param fecha
	 *            La fecha a la cual se le quitar&aacute;n los minutos y los
	 *            segundos.
	 * @return Calendar La fecha sin minutos ni segundos
	 */
	private Calendar setFecha(Calendar fecha) {
		fecha.add(Calendar.HOUR_OF_DAY, -fecha.get(Calendar.HOUR_OF_DAY));
		fecha.add(Calendar.MINUTE, -fecha.get(Calendar.MINUTE));
		fecha.add(Calendar.SECOND, -fecha.get(Calendar.SECOND));
		fecha.add(Calendar.MILLISECOND, -fecha.get(Calendar.MILLISECOND));
		return fecha;
	}

	/**
	 * Guarda la informacion de la tabla de SC_MENSAJE en SC_MENSAJE_HIST, Borra
	 * la informacion de SC_MENSJAE Borra los mensajes mayores a 30 días
	 */
	public void actualizaHistoricosMensaje(StringBuffer logger) {
		
		logger.append("Actualiza histórico mensajes banner");
		
		try {
			getHibernateTemplate().execute(new HibernateCallback() {
				public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
					
					Connection connection = null;
					PreparedStatement statement = null;
					String sqlMueveMensajes = "INSERT INTO SC_MENSAJE_HIST "
						+ " (ID_MENSAJE, PRIORIDAD, MENSAJE, USUARIO_CREACION, FECHA_CREACION, USUARIO_ULT_MODIF, FECHA_ULT_MODIF) "
						+ " (SELECT "
						+ " ID_MENSAJE, PRIORIDAD, MENSAJE, USUARIO_CREACION, FECHA_CREACION, USUARIO_ULT_MODIF, FECHA_ULT_MODIF "
						+ " FROM SICA_ADMIN.SC_MENSAJE) ";

				    String sqlBorraMensajes = " DELETE FROM SC_MENSAJE ";

				    String sqlBorraMensajes30Dias = "DELETE FROM SC_MENSAJE_HIST "
						+ " WHERE to_date(FECHA_CREACION,'dd/mm/yyyy') <= (SELECT to_date(current_date-30,'dd/mm/yyyy') FROM DUAL) ";
									
					connection = session.connection();

					// copia los mensajes de la tabla SC_MENSAJE a
					// SC_MENSAJE_HIST
					statement = connection.prepareStatement(sqlMueveMensajes);
					statement.executeUpdate();

					// borra los mensajes de la tabla SC_MENSAJE
					statement = connection.prepareStatement(sqlBorraMensajes);
					statement.executeUpdate();

					// borra los mensajes de la tabla SC_MENSAJE_HIST mayores a
					// 30 días
					statement = connection.prepareStatement(sqlBorraMensajes30Dias);
					statement.executeUpdate();
					return null;
				} 
			});
		}
		catch (DataAccessException e) {
			logger.append("ocurrió un error al actualizar el histórico de mensajes banner: " + e.getMessage());
		} 
		
		logger.append("Termina actualización de histórico de mensajes banner");
	}

	/**
	 * Cambia el Estado del Sistema Operaci&oacute;n Nocturna.
	 */
	private void terminarCierreSica() {
		Estado cierre = (Estado) getHibernateTemplate().findByNamedQuery(
				"findEstadoActual").get(0);
		cierre.setActual(false);
		update(cierre);
		List estados = findAll(Estado.class);
		for (Iterator it = estados.iterator(); it.hasNext();) {
			Estado estado = (Estado) it.next();
			if (estado.getIdEstado() == Estado.ESTADO_OPERACION_NOCTURNA) {
				estado.setActual(true);
				estado.setHoraInicio(HOUR_FORMAT.format(getFechaSistema()));
			}
		}
		update(estados);
	}

	/**
	 * Valida los Deals existentes. Dependiendo su status genera alertas o
	 * acumula errores. En caso de generar un error se cancela el Cierre.
	 * primero checa Deals Normales, despu&eacute;s Interbancarios.
	 * 
	 * @param numErrors
	 *            N&uacute;mero de errores encontrados en el Cierre.
	 * @param logger
	 *            El String Buffer del log.
	 * @return int
	 */
	private int validarDealsCierreDelDia(int numErrors, StringBuffer logger) {
		List deals = getHibernateTemplate().findByNamedQuery(
				"findDealByStatusByFecha",
				new Object[] { getFechaHoy12(), getFechaHoy() });
		logger.append(Calendar.getInstance().getTime()).append(
				" *** Validacion de deals...\n");
		for (Iterator it = deals.iterator(); it.hasNext();) {
			Deal deal = (Deal) it.next();
			if (!deal.isInterbancario()) {
				if (Deal.EV_SOLICITUD.equals(deal
						.decodificarEvento(Deal.EV_IND_GRAL_MODIFICACION))
						|| Deal.EV_SOLICITUD
								.equals(deal
										.decodificarEvento(Deal.EV_IND_GRAL_CANCELACION))) {
					logger
							.append(Calendar.getInstance().getTime())
							.append(
									" Validacion de "
											+ "autorizaciones de modificacion y/o cancelacion pendientes...\n");
					deal.setEvento(Deal.EV_NEGACION,
							Deal.EV_IND_GRAL_MODIFICACION);
					deal.setEvento(Deal.EV_NEGACION,
							Deal.EV_IND_GRAL_CANCELACION);
					update(deal);
				}
			}
			if (deal.isInterbancario()) {
				if (Deal.EV_SOLICITUD.equals(deal
						.decodificarEvento(Deal.EV_IND_GRAL_MODIFICACION))
						|| Deal.EV_SOLICITUD
								.equals(deal
										.decodificarEvento(Deal.EV_IND_GRAL_CANCELACION))
						|| Deal.EV_APROBACION_TESORERIA
								.equals(deal
										.decodificarEvento(Deal.EV_IND_GRAL_MODIFICACION))
						|| Deal.EV_APROBACION_TESORERIA
								.equals(deal
										.decodificarEvento(Deal.EV_IND_GRAL_CANCELACION))) {
					logger
							.append(Calendar.getInstance().getTime())
							.append(
									" Validacion de "
											+ "autorizaciones de modificacion y/o cancelacion pendientes...\n");
					deal.setEvento(Deal.EV_NEGACION,
							Deal.EV_IND_GRAL_MODIFICACION);
					deal.setEvento(Deal.EV_NEGACION,
							Deal.EV_IND_GRAL_CANCELACION);
					update(deal);
				}
			}
		}
		return numErrors;
	}

	/**
	 * Genera la alerta con los par&aacute;metros especificados.
	 * 
	 * @param tipoAlerta
	 *            El tipo de la alerta
	 * @param idPersona
	 *            El id de la persona.
	 * @param contexto
	 *            El contexto de la alerta.
	 */
	private void generaAlerta(String tipoAlerta, Integer idPersona,
			HashMap contexto) {
		try {
			getSicaAlertasService().generaAlerta(tipoAlerta, idPersona,
					contexto);
		} catch (Exception e) {
			if (logger.isDebugEnabled()) {
				logger.debug(e);
			}
		}
	}

	/**
	 * Obtiene el valor de la fecha del sistema mas 12 hrs. para asignar el
	 * horario de inicio del d&iacute;a.
	 * 
	 * @return Calendar.
	 */
	private Calendar getFechaHoy12() {
		Calendar fecha = new GregorianCalendar();
		fecha.setTime(DateUtils.inicioDia(getFechaSistema()));
		return fecha;
	}

	/**
	 * Obtiene la fecha actual.
	 * 
	 * @return Calendar.
	 */
	private Calendar getFechaHoy() {
		Calendar fecha = new GregorianCalendar();
		fecha.setTime(DateUtils.finDia(getFechaSistema()));
		return fecha;
	}

	/**
	 * Obtiene una referencia al Bean de Servicios de Seguridad.
	 * 
	 * @return SeguridadServiceData El Bean de Servicios de Seguridad.
	 */
	private SeguridadServiceData getSeguridadServiceData() {
		return (SeguridadServiceData) _appContext
				.getBean("securityServiceData");
	}

	/**
	 * Componente a servicios de alertas.
	 */
	private SicaAlertasService _sicaAlertasService;

	/**
	 * Regresa el valor de cont.
	 * 
	 * @return int.
	 */
	public int getCont() {
		return _cont;
	}

	/**
	 * Fija el valor de cont.
	 * 
	 * @param cont
	 *            El valor a asignar.
	 */
	public void setCont(int cont) {
		_cont = cont;
	}

	/**
	 * Regresa la fecha del sistema.
	 * 
	 * @return Date.
	 */
	public Date getFechaSistema() {
		return _fechaSistema;
	}

	/**
	 * Fija la fecha del sistema.
	 * 
	 * @param fechaSistema
	 *            El valor a asignar.
	 */
	public void setFechaSistema(Date fechaSistema) {
		_fechaSistema = fechaSistema;
	}

	/**
	 * Regresa el valor de limiteDao.
	 * 
	 * @return LimiteDao.
	 */
	public LimiteDao getLimiteDao() {
		return limiteDao;
	}

	/**
	 * Establece el valor de limiteDao.
	 * 
	 * @param limiteDao
	 *            El valor a asignar.
	 */
	public void setLimiteDao(LimiteDao limiteDao) {
		this.limiteDao = limiteDao;
	}

	/**
	 * Contador que nos dice cuantos Deals se procesan al cierre del d&iacute;a
	 */
	private int _cont;

	/**
	 * La fecha del sistema que est&aacute; parametrizada en sc_parametro y que
	 * establece el proceso de cierre de d&iacute;a.
	 */
	private Date _fechaSistema;

	/**
	 * Componente a un servicio que nos trae Objetos Personas a trav&eacute; de
	 * su idPersona.s
	 */
	private ExtraDao _extraDao;

	/**
	 * Componente a un servicio que nos trae ids ejecutivos de lineas de credito
	 * para determinado grupo empresarial.
	 */
	private CierreDao _cierreDao;

	/**
	 * Componente a un servicio que nos trae la referencia a los Servicios del
	 * Pizarron
	 */
	private PizarronServiceData _pizarronServiceData;

	/**
	 * La referencia al bean LimiteDao.
	 */
	private LimiteDao limiteDao;

	/** Apagado de Phoenix */
//	/**
//	 * La referencia al bean PhoenixDao.
//	 */
//	private PhoenixDao _phoenixDao;

	/**
	 * Constante con el formato de horas y minutos.
	 */
	private static final SimpleDateFormat HOUR_FORMAT = new SimpleDateFormat(
			"HH:mm");

	/**
	 * Facultad para el promotor de guardia.
	 */
	public static final String SICA_GUARDIA = "SICA_GUARDIA";

	/**
	 * Constante id_parametro
	 */
	public static final String FECHA_RANGO_CONTRATO_SICA = "FECHA_RANGO_CONTRATO_SICA";
}
