package com.banorte.ods.sica.batch.transfint.service.impl;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.banorte.ods.sica.batch.transfint.dao.BupFechaInhabilDao;
import com.banorte.ods.sica.batch.transfint.domain.BupFechaInhabil;
import com.banorte.ods.sica.batch.transfint.domain.ScParametro;
import com.banorte.ods.sica.batch.transfint.error.BusinessException;
import com.banorte.ods.sica.batch.transfint.service.ReporteTransfintService;
import com.banorte.ods.sica.batch.transfint.util.Archivo;

import static org.apache.commons.lang.StringUtils.replace;
import static org.apache.commons.lang.time.DateUtils.addDays;

import static com.banorte.ods.sica.batch.transfint.util.Constantes.TIPO_REPORTE_REPROCESO;
import static com.banorte.ods.sica.batch.transfint.util.Constantes.TIPO_REPORTE_TRANSFERENCIAS_DIARIAS;
import static com.banorte.ods.sica.batch.transfint.util.Constantes.TIPO_REPORTE_TRANSFERENCIAS_TARDIAS;
import static com.banorte.ods.sica.batch.transfint.util.Utilerias.isFinDeSemana;

/**
 * The Class ReporteTransfintServiceImpl.
 */
@Service(value = "reporteTransfintService")
public class ReporteTransfintServiceImpl extends TransferenciasSevice implements
		ReporteTransfintService {
	
	/** The log. */
	private Logger LOG = LoggerFactory.getLogger(ReporteTransfintServiceImpl.class);
		
	/** The fecha sistema. */
	@Value("${param.fecha.sistema}")
	private String fechaSistema;
	
	/** The rep transfint hora ini. */
	@Value("${param.rep.transfint.hora.ini}")
	private String repTransfintHoraIni;
	
	/** The rep transfint hora fin. */
	@Value("${param.rep.transfint.hora.fin}")
	private String repTransfintHoraFin;
	
	/** The rep transfint fmto fecha full. */
	@Value("${param.rep.transfint.fmto.fecha.full}")
	private String repTransfintFmtoFechaFull;
	
	/** The rep transfint nombre reporte. */
	@Value("${param.rep.transfint.nombre.reporte}")
	private String repTransfintNombreReporte;
	
	/** The rep transfint nombre reporte copia. */
	@Value("${param.rep.transfint.nombre.reporte.copia}")
	private String repTransfintNombreReporteCopia;
	
	
	/** The tipo fecha. */
	@Value("${param.rep.transfint.tipo.reporte}")
	private String tipoReporte;
	
	/** The formato fecha sistema. */
	@Value("${param.formato.fecha.sistema}")
	private String formatoFechaSistema;
	
	/** The tardias hora fin. */
	@Value("${param.rep.transfint.t.hora.fin}")
	private String tardiasHoraFin;
	
	/** The nombre rep transfint tardias. */
	@Value("${param.rep.transfint.nombre.rep.t}")
	private String nombreRepTransfintTardias;
	
	/** The nombre rep transfint reproceso. */
	@Value("${param.rep.transfint.nombre.rep.r}")
	private String nombreRepTransfintReproceso;
	
	/** The fecha ini. */
	@Value("${param.rep.transfint.fecha.ini}")
	private String fechaIni;
	
	/** The fecha fin. */
	@Value("${param.rep.transfint.fecha.fin}")
	private String fechaFin;
	
	/** The formato fecha reporte. */
	@Value("${param.rep.transfint.fmto.fecha.rep}")
	private String formatoFechaReporte;
	
	/** The bup fecha inhabil dao. */
	@Autowired
	private BupFechaInhabilDao bupFechaInhabilDao;

	/**
	 * Instantiates a new reporte transfint service impl.
	 */
	public ReporteTransfintServiceImpl() {
	}

	/**
	 * Genera reporte.
	 *
	 * @return true, if successful
	 */
	@Override
	public void generaReporte() {
		try {
			ScParametro paramFechaSist = 
					this.getScParametroDao().findById(this.fechaSistema);
			ScParametro paramHoraIni = 
					this.getScParametroDao().findById(this.repTransfintHoraIni);
			ScParametro paramHoraFin = 
					this.getScParametroDao().findById(this.repTransfintHoraFin);
			ScParametro paramFormatoFecha = 
					this.getScParametroDao().findById(this.repTransfintFmtoFechaFull);
			if (paramFechaSist != null && paramHoraIni != null && 
					paramHoraFin != null && paramFormatoFecha != null) {
				DateFormat df = new SimpleDateFormat(paramFormatoFecha.getValor());
				String strFechaIni = paramFechaSist.getValor() + " " + paramHoraIni.getValor(); 
				String strFechaFin = paramFechaSist.getValor() + " " + paramHoraFin.getValor();
				Date fechaIni = df.parse(strFechaIni);
				Date fechaFin = df.parse(strFechaFin);
				df = new SimpleDateFormat(this.formatoFechaSistema);
				Date fechaSistema = df.parse(paramFechaSist.getValor());
				LOG.debug("Generando reporte para la fecha: {}", fechaSistema);
				df = new SimpleDateFormat(this.formatoFechaReporte);
				String nombre = df.format(fechaSistema);	
				nombre = replace(this.repTransfintNombreReporte, "YYYYMMDD", nombre);
				String nombreArchivoCopia = df.format(fechaSistema);
				nombreArchivoCopia = replace(this.repTransfintNombreReporteCopia, "YYYYMMDD", nombreArchivoCopia);
				Archivo reporte = this.generaReporte(fechaIni, fechaFin, 
										TIPO_REPORTE_TRANSFERENCIAS_DIARIAS, nombre);
				LOG.debug("Reporte generado: {}", reporte.getContent().getName());
				LOG.debug("** Generando Reporte Copia ** Paso COPIA nombreArchivoCopia " + nombreArchivoCopia);
				this.generaCopiaReporte(fechaIni, fechaFin, 
						TIPO_REPORTE_TRANSFERENCIAS_DIARIAS, nombreArchivoCopia);
			}
			else {
				throw new BusinessException(this.getMessage("msg.env.param.transfint.empty"));
			}
		}
		catch (ParseException ex) {
			LOG.error("ParseException en generaReporte() ", ex);
			throw new BusinessException(this.getMessage("msg.env.param.transfint.error"));
		}
	}
	
	/**
	 * Genera reporte tardias.
	 */
	@Override
	public void generaReporteTardias() {
		Archivo reporte = null;
		try {
			ScParametro paramTipoReporte = this.getScParametroDao().findById(this.tipoReporte);
			ScParametro paramFormatoFecha = 
					this.getScParametroDao().findById(this.repTransfintFmtoFechaFull);
			if (paramTipoReporte != null) {
				if ("T".equals(paramTipoReporte.getValor())) {//Operaciones tardias
					ScParametro paramFechaSist = 
							this.getScParametroDao().findById(this.fechaSistema);
					DateFormat df = new SimpleDateFormat(this.formatoFechaSistema);
					Date fechaSistema = df.parse(paramFechaSist.getValor());
					Date tmpDate = this.calculaFechaHabilAnterior(fechaSistema);
					String strDate = df.format(tmpDate);
					ScParametro paramHoraFin = 
							this.getScParametroDao().findById(this.repTransfintHoraFin);
					String strFechaIni = strDate + " " + paramHoraFin.getValor(); 
					String strFechaFin = strDate + " " + this.tardiasHoraFin;
					df = new SimpleDateFormat(paramFormatoFecha.getValor());
					Date fechaIni = df.parse(strFechaIni);
					Date fechaFin = df.parse(strFechaFin);
					df = new SimpleDateFormat(this.formatoFechaReporte);
					String nombre = df.format(tmpDate);
					nombre = replace(this.nombreRepTransfintTardias, "DDMMAAAA", nombre);
					reporte = this.generaReporte(fechaIni, fechaFin, 
									TIPO_REPORTE_TRANSFERENCIAS_TARDIAS, nombre);
				}
				else {//Reproceso de una fecha en especifico
					Date hoy = new Date();
					DateFormat df = new SimpleDateFormat(this.formatoFechaSistema);
					ScParametro paramFechaIni = 
							this.getScParametroDao().findById(this.fechaIni);
					ScParametro paramFechaFin = 
							this.getScParametroDao().findById(this.fechaFin);
					df = new SimpleDateFormat(paramFormatoFecha.getValor());
					Date fechaIni = df.parse(paramFechaIni.getValor());
					Date fechaFin = df.parse(paramFechaFin.getValor());
					df = new SimpleDateFormat(this.formatoFechaReporte);
					String nombre = df.format(hoy);
					nombre = replace(this.nombreRepTransfintTardias, "DDMMAAAA", nombre);
					reporte = this.generaReporte(fechaIni, fechaFin, 
											TIPO_REPORTE_REPROCESO, nombre);
				}
				LOG.debug("Reporte generado: {}", reporte.getContent().getName());
			}
			else {
				throw new BusinessException(this.getMessage("msg.env.param.transfint.empty"));
			}
		}
		catch (ParseException ex) {
			LOG.error("ParseException en generaReporte() ", ex);
			throw new BusinessException(this.getMessage("msg.env.param.transfint.error"));
		}
	}
	
	/**
	 * Calcula fecha habil anterior.
	 *
	 * @param fecha the fecha
	 * @return the date
	 */
	private Date calculaFechaHabilAnterior(Date fecha) {
		LOG.debug("Fecha recibida: {}", fecha);
		boolean encontrado = false;
		Date nueva = fecha;
		while (!encontrado) {
			nueva = addDays(nueva, -1);
			if (!isFinDeSemana(nueva)) {
				BupFechaInhabil inhabil = this.bupFechaInhabilDao.findFechaInhabil(nueva);
				if (inhabil == null) {
					encontrado = true;
				}
			}
		}
		
		return nueva;
	}

}
