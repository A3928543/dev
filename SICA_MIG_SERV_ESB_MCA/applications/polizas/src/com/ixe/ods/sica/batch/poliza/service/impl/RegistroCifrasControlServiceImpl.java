package com.ixe.ods.sica.batch.poliza.service.impl;

import java.util.Date;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ixe.ods.sica.batch.dao.ScCifrasControlDao;
import com.ixe.ods.sica.batch.dao.ScParametroDao;
import com.ixe.ods.sica.batch.dao.TesFacturacionSapDao;
import com.ixe.ods.sica.batch.domain.ScCifrasControlCfdi;
import com.ixe.ods.sica.batch.domain.ScControlProcesoCifras;
import com.ixe.ods.sica.batch.domain.ScParametro;
import com.ixe.ods.sica.batch.domain.TesFacturacionSap;
import com.ixe.ods.sica.batch.error.BusinessException;
import com.ixe.ods.sica.batch.poliza.service.RegistroCifrasControlService;

import static com.ixe.ods.sica.batch.util.CifrasControlConstants.ESTATUS_REG_SAP;
import static com.ixe.ods.sica.batch.util.Utilerias.dateToString;
import static com.ixe.ods.sica.batch.util.Utilerias.reseteaHoraFecha;
import static com.ixe.ods.sica.batch.util.Utilerias.stringToDate;
import static org.springframework.transaction.annotation.Isolation.DEFAULT;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;

/**
 * The Class RegistroCifrasControlServiceImpl.
 */
@Service("registroCifrasControlService")
public class RegistroCifrasControlServiceImpl extends BaseService 
								implements RegistroCifrasControlService {
	
	/** The Constant LOG. */
	private static final Logger LOG = LoggerFactory.getLogger(RegistroCifrasControlServiceImpl.class);
	
	/** The facturacion sap dao. */
	@Autowired
	private TesFacturacionSapDao facturacionSapDao;
	
	/** The cifras control dao. */
	@Autowired
	private ScCifrasControlDao cifrasControlDao;
	
	/** The sc parametro dao. */
	@Autowired
	private ScParametroDao scParametroDao;
	
	/** The pattern fecha recepcion. */
	@Value("${date.pattern.recepcion}")
	private String patternFechaRecepcion;
	
	
	/** The id param fecha reproceso. */
	@Value("${param.fecha.reproceso}")
	private String idParamFechaReproceso;
	
	/**
	 * Instantiates a new registro cifras control service impl.
	 */
	public RegistroCifrasControlServiceImpl() {
	}

	/**
	 * Registra cifras control.
	 *
	 */
	@Override
	@Transactional(propagation = REQUIRED, isolation = DEFAULT, readOnly = false)
	public void registraCifrasControl() {
		Date fechaProceso = this.calculaFechaProceso();
		String fechaProcesoStr = dateToString(fechaProceso, this.patternFechaRecepcion);
		ScCifrasControlCfdi cifrasCtrl = this.crearScCifrasControlCfdi(fechaProcesoStr);
		cifrasCtrl.setFechaRecepcion(fechaProceso);
		cifrasCtrl.setCantidadRecibidosSap(this.calcularCantidadCfdisRecibidosSAP(fechaProceso));
		this.guardaScCifrasControlCfdi(cifrasCtrl);
	}
	
	/**
	 * Calcula fecha proceso.
	 *
	 * @return the date
	 */
	private Date calculaFechaProceso() {
		Date fechaProceso = null;
		//String estatus = TesFacturacionSap.STATUS_FACT;
		ScParametro parametro = 
				this.scParametroDao.findScParametroById(this.idParamFechaReproceso);
		/* Se ejecuta para una fecha registrada en SC_PARAMETRO con formato ddMMyyyy */
		/* El nombre del parametro es CIFRAS_CONTROL_FECHA_REPROCESO                 */
		if (parametro != null) {
			fechaProceso = stringToDate(parametro.getValor(), patternFechaRecepcion);
		}
		if (fechaProceso == null) {//Se toma la fecha del ultimo CFDI recibido en SAP.
			//fechaProceso = this.facturacionSapDao.findFechaUltimoCfdiRecibidoSAP(estatus);
			fechaProceso = this.getFechaSistema();
		}
		LOG.debug("Fecha de proceso: {}", fechaProceso);
		
		return fechaProceso;
	}
	
	/**
	 * Crear sc cifras control cfdi.
	 *
	 * @param fechaProceso the fecha proceso
	 * @return the sc cifras control cfdi
	 */
	private ScCifrasControlCfdi crearScCifrasControlCfdi(String fechaProceso) {
		ScCifrasControlCfdi cifras = null;
		Integer reproceso = 0;
		try {
			cifras = this.cifrasControlDao.findScCifrasControlByFecha(fechaProceso);
			reproceso = cifras.getReproceso() + 1;
		}
		catch (NoResultException ex) {
			LOG.error("NoResultException en crearScCifrasControlCfdi() ", ex);
			cifras = new ScCifrasControlCfdi();
			cifras.setFechaRecepcionStr(fechaProceso);
			cifras.setFechaCreacion(new Date());
		}
		catch (NonUniqueResultException ex) {
			LOG.error("NonUniqueResultException en crearScCifrasControlCfdi() ", ex);
			String msg = this.getMessage("msg.cifras.ctrl.nonunique");
			throw new BusinessException(msg);
		}
		cifras.setReproceso(reproceso);
		cifras.setFechaUltMod(new Date());
		cifras.setEstatus(ESTATUS_REG_SAP);
		
		return cifras;
	}
	
	/**
	 * Calcular cantidad cfdis recibidos SAP.
	 *
	 * @param fechaRecepcion the fecha recepcion
	 * @return the integer
	 */
	private Integer calcularCantidadCfdisRecibidosSAP(Date fechaRecepcion) {
		Long cantidad = 0L;
		Long sinFolios = new Long("0");
		String estatus = TesFacturacionSap.STATUS_FACT;
		Date ini = reseteaHoraFecha(fechaRecepcion);
		Date fin = reseteaHoraFecha(fechaRecepcion, 23, 59, 59);
		try {
			cantidad = 
				this.facturacionSapDao.findCantidadCfdisRecibidosSAP(ini, fin, estatus);
			LOG.debug("Cantidad de CFDIS recibidos SAP: {}", cantidad);
			if (sinFolios.equals(cantidad)) {
				this.sendErrorEmail(fechaRecepcion);
			}
		}
		catch (NoResultException ex) {
			LOG.error("No se recibieron CFDIS para la fecha dada.");
			this.sendErrorEmail(fechaRecepcion);
		}
		
		return cantidad.intValue();
	}
	
	/**
	 * Guarda sc cifras control cfdi.
	 *
	 * @param cifras the cifras
	 */
	private void guardaScCifrasControlCfdi(ScCifrasControlCfdi cifras) {
		if (cifras.getIdCifrasControl() != null) {
			cifras.setArchivoCifrasControl(null);
			cifras.setArchivosCfdisFaltantes(null);
			cifras.setCantidadRecibidosIndices(null);
			this.cifrasControlDao.updateScCifrasControl(cifras);
		}
		else {
			cifras.setIdCifrasControl(cifras.getIdCifrasControl());
			this.cifrasControlDao.saveScCifrasControl(cifras);
		}
		this.guardaScControlProcesoCifras(cifras);
	}
	
	/**
	 * Guarda sc control proceso cifras.
	 *
	 * @param cifras the cifras
	 */
	private void guardaScControlProcesoCifras(ScCifrasControlCfdi cifras) {
		this.cifrasControlDao.deleteScControlProcesoCifras();
		ScControlProcesoCifras control = new ScControlProcesoCifras();
		control.setIdCifrasControl(cifras.getIdCifrasControl());
		control.setFechaRecepcionStr(cifras.getFechaRecepcionStr());
		control.setFechaCreacion(new Date());
		control.setFechaUltMod(new Date());
		this.cifrasControlDao.saveScControlProcesoCifras(control);
	}
	
	/**
	 * Send error email.
	 *
	 * @param fecha the fecha
	 */
	private void sendErrorEmail(Date fecha) {
		String fechaStr = dateToString(fecha, "dd/MM/yyyy");
		String error = this.getMessage("msg.cifras.proceso.ctrl.nocfdis", fechaStr);
		this.sendErrorEmail(error);
	}

}
