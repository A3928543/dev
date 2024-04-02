package com.ixe.ods.sica.batch.cfdi.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ixe.ods.sica.batch.cfdi.service.CifrasControlService;
import com.ixe.ods.sica.batch.dao.ScCifrasControlDao;
import com.ixe.ods.sica.batch.domain.ScCifrasControlCfdi;
import com.ixe.ods.sica.batch.domain.ScControlProcesoCifras;
import com.ixe.ods.sica.batch.mail.MailSender;

import static org.apache.commons.lang.StringUtils.isNotEmpty;
import static org.apache.commons.lang.StringUtils.replace;

import static com.ixe.ods.sica.batch.util.Utilerias.dateToString;

/**
 * The Class CifrasControlServiceImpl.
 */
@Service
public class CifrasControlServiceImpl implements CifrasControlService {
	
	/** The Constant LOG. */
	private static final Logger LOG = LoggerFactory.getLogger(CifrasControlServiceImpl.class); 
	
	/** The sc cifras control dao. */
	@Autowired
	private ScCifrasControlDao scCifrasControlDao;
	
	/** The mail sender. */
	@Autowired
	private MailSender mailSender;
	
	/** The de. */
	@Value("${rep.cifras.correo.de}")
	private String de;
	
	/** The para. */
	@Value("${rep.cifras.correo.para}")
	private String para;
	
	/** The asunto reporte cifras. */
	@Value("${rep.cifras.asunto}")
	private String asuntoReporteCifras;
	
	/** The asunto reporte cifras det. */
	@Value("${rep.cifras.asunto.det}")
	private String asuntoReporteCifrasDet;
	
	/** The texto reporte cifras. */
	@Value("${rep.cifras.texto.correo}")
	private String textoReporteCifras;
	
	/** The cc. */
	@Value("${rep.cifras.corre.cc}")
	private String cc;
	
	/** The formato fecha reporte. */
	@Value("${rep.pattern.date}")
	private String formatoFechaReporte;
	
	/**
	 * Instantiates a new cifras control service impl.
	 */
	public CifrasControlServiceImpl() {
	}
	
	/**
	 * Enviar notificaciones.
	 *
	 * @return the int
	 */
	@Override
	public int enviarNotificaciones() {
		int codigo = 0;
		try {
			ScControlProcesoCifras control = 
					this.scCifrasControlDao.findScControlProcesoCifras();
			Long id = control.getIdCifrasControl();
			ScCifrasControlCfdi cifras = 
					this.scCifrasControlDao.findScCifrasControlById(id);
			if (cifras != null) {
				if (cifras.getFechaRecepcionStr().equals(control.getFechaRecepcionStr())) {
					codigo = this.enviarCorreos(cifras);
				}
				else {
					codigo = -1;
				}
			}
			else {
				codigo = -2;
			}
			
		}
		catch (NoResultException ex) {
			LOG.error("NoResultException en enviarNotificacion() ", ex);
			codigo = -3;
		}
		catch (NonUniqueResultException ex) {
			LOG.error("NonUniqueResultException en enviarNotificacion() ", ex);
			codigo = -4;
		}
		
		return codigo;
	}
	
	/**
	 * Enviar correos.
	 *
	 * @param cifras the cifras
	 * @return the int
	 */
	private int enviarCorreos(ScCifrasControlCfdi cifras) {
		int codigo = 0;
		String texto = null;
		String asunto = null;
		String fechaRecepcion = 
				dateToString(cifras.getFechaRecepcion(), this.formatoFechaReporte);
		Map<String, Object> param = this.inicializaCorreo();
		if (isNotEmpty(cifras.getArchivoCifrasControl())) {
			asunto = replace(this.asuntoReporteCifras, "fecha", fechaRecepcion);
			param.put("subject", asunto);
			texto = replace(this.textoReporteCifras, "nombre", cifras.getArchivoCifrasControl());
			param.put("text", texto);
			if (!this.mailSender.enviarCorreo(param)) {
				codigo = -5;
			}
		}
		if (isNotEmpty(cifras.getArchivosCfdisFaltantes()) && codigo == 0) {
			asunto = replace(this.asuntoReporteCifrasDet, "fecha", fechaRecepcion);
			param.put("subject", asunto);
			texto = replace(this.textoReporteCifras, "nombre", cifras.getArchivosCfdisFaltantes());
			param.put("text", texto);
			if (!this.mailSender.enviarCorreo(param)) {
				codigo = -6;
			}
		}
		
		return codigo;
		
	}
	
	/**
	 * Inicializa correo.
	 *
	 * @return the map
	 */
	private Map<String, Object> inicializaCorreo() {
		Map<String, Object> param = new HashMap<String, Object> ();
		param.put("from", this.de);
		param.put("to", this.para);
		if (isNotEmpty(this.cc)) {
			param.put("cc", this.cc);
		}
		
		return param;
	}

}
