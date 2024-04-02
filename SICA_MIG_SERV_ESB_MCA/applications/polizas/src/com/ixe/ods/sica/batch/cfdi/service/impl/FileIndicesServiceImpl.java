package com.ixe.ods.sica.batch.cfdi.service.impl;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;

import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.VelocityException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ixe.ods.sica.batch.cfdi.parser.FileIndicesParser;
import com.ixe.ods.sica.batch.cfdi.service.FileIndicesService;
import com.ixe.ods.sica.batch.cfdi.util.CfdiConfiguracionApp;
import com.ixe.ods.sica.batch.dao.ScCifrasControlDao;
import com.ixe.ods.sica.batch.dao.ScDealDetalleDao;
import com.ixe.ods.sica.batch.dao.ScReferenciaCruceDao;
import com.ixe.ods.sica.batch.dao.ScReferenciaCruceDetalleDao;
import com.ixe.ods.sica.batch.dao.TesFacturacionSapDao;
import com.ixe.ods.sica.batch.domain.ScCifrasControlCfdi;
import com.ixe.ods.sica.batch.domain.ScControlProcesoCifras;
import com.ixe.ods.sica.batch.domain.ScDealDetalle;
import com.ixe.ods.sica.batch.domain.ScReferenciaCruce;
import com.ixe.ods.sica.batch.domain.ScReferenciaCruceDetalle;
import com.ixe.ods.sica.batch.domain.TesFacturacionSap;
import com.ixe.ods.sica.batch.error.BusinessException;
import com.ixe.ods.sica.batch.error.FileParserException;
import com.ixe.ods.sica.batch.error.FileReportException;
import com.ixe.ods.sica.batch.error.RowParserException;
import com.ixe.ods.sica.batch.mail.MailSender;
import com.ixe.ods.sica.batch.parser.FileParser;
import com.ixe.ods.sica.batch.service.FileService;
import com.ixe.ods.sica.batch.util.Archivo;
import com.ixe.ods.sica.batch.util.Filtro;
import com.ixe.ods.sica.batch.util.FiltroHistorico;

import static org.apache.commons.io.FileUtils.deleteDirectory;
import static org.apache.commons.io.FileUtils.copyDirectory;
import static org.apache.commons.io.FileUtils.copyFile;
import static org.apache.commons.io.FileUtils.writeStringToFile;
import static org.apache.commons.lang.StringUtils.contains;
import static org.apache.commons.lang.StringUtils.isNotEmpty;
import static org.apache.commons.lang.StringUtils.lastIndexOf;
import static org.apache.commons.lang.StringUtils.replace;
import static org.apache.commons.lang.StringUtils.substring;
import static org.apache.commons.lang.StringUtils.trim;
import static org.apache.commons.lang.StringUtils.upperCase;
import static org.apache.commons.lang.time.DateUtils.isSameDay;
import static org.springframework.transaction.annotation.Isolation.DEFAULT;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;
import static org.springframework.ui.velocity.VelocityEngineUtils.mergeTemplateIntoString;
import static com.ixe.ods.sica.batch.util.ArchivoUtil.comprimir;
import static com.ixe.ods.sica.batch.util.ArchivoUtil.descomprimir;
import static com.ixe.ods.sica.batch.util.ArchivoUtil.sortFilesByName;
import static com.ixe.ods.sica.batch.util.CifrasControlConstants.ESTATUS_PROCESO_FINALIZADO;
import static com.ixe.ods.sica.batch.util.CifrasControlConstants.ESTATUS_REG_INDICES;
import static com.ixe.ods.sica.batch.util.CifrasControlConstants.ESTATUS_REG_SAP;
import static com.ixe.ods.sica.batch.util.CifrasControlConstants.ESTATUS_FIN_REP_CIFRAS;
import static com.ixe.ods.sica.batch.util.CifrasControlConstants.ESTATUS_FIN_REP_DIFERENCIAS;
import static com.ixe.ods.sica.batch.util.Utilerias.dateToString;
import static com.ixe.ods.sica.batch.util.Utilerias.reseteaHoraFecha;

/**
 * The Class FileIndicesServiceImpl.
 */
@Service
public class FileIndicesServiceImpl extends FileService implements FileIndicesService {
	
	/** The log. */
	private static Logger LOG = LoggerFactory.getLogger(FileIndicesServiceImpl.class);
	
	/** The config. */
	@Autowired
	private CfdiConfiguracionApp config;
	
	/** The tes facturacion sap dao. */
	@Autowired
	private TesFacturacionSapDao tesFacturacionSapDao;
	
	/** The sc deal detalle dao. */
	@Autowired
	private ScDealDetalleDao scDealDetalleDao;
	
	/** The sc referencia cruce dao. */
	@Autowired
	private ScReferenciaCruceDao scReferenciaCruceDao;
	
	/** The sc ref cruce detalle dao. */
	@Autowired
	private ScReferenciaCruceDetalleDao scRefCruceDetalleDao;
	
	/** The sc cifras control dao. */
	@Autowired
	private ScCifrasControlDao scCifrasControlDao;
	
	/** The messages. */
	@Autowired
	private ReloadableResourceBundleMessageSource messages;
	
	/** The mail sender. */
	@Autowired
	private MailSender mailSender;
	
	/** The map row. */
	private Map<String, Object> mapRow;
	
	/** The deal detalle. */
	private ScDealDetalle dealDetalle;
	
	/** The referencia cruce. */
	private ScReferenciaCruce referenciaCruce;
	
	/** The detalles. */
	private List<ScReferenciaCruceDetalle> detalles;
	
	/** The folio fiscal factura. */
	private Object[] folioFiscalFactura;
	
	/** The sc cifras control cfdi. */
	private ScCifrasControlCfdi scCifrasControlCfdi;
	
	/** The velocity engine. */
	@Autowired
	private VelocityEngine velocityEngine;
	
	/** The errores cifras. */
	private Archivo erroresCifras;
	
	/** The is errores rep cifras. */
	private boolean isErroresRepCifras;
	
	/**
	 * Instantiates a new file indices service impl.
	 */
	public FileIndicesServiceImpl() {
		super();
	}
	
	/**
	 * Filtra archivos.
	 *
	 * @return the file[]
	 */
	private File[] filtraArchivos() {
		File[] files = null;
		String dirEntrada = this.config.getDirEntrada();
		File dirWork = new File(dirEntrada);
		if (!dirWork.exists()) {
			String msg = getMessage("msg.dir.entrada.doesnotexist", dirEntrada);
			throw new BusinessException(msg);
		}
		else {
			String prefijo = this.config.getPrefijoFile();
			String extFileIn = this.config.getExtFileIn();
			Filtro filtro = new Filtro(prefijo, extFileIn);
			files = dirWork.listFiles(filtro); 
			sortFilesByName(files, true);
		}
		
		return files;
	}
	
	/**
	 * Procesa archivo.
	 */
	@Override
	@Transactional(propagation = REQUIRED, isolation = DEFAULT, readOnly = false)	
	public void procesaArchivo() {
		Map<String, Object> model = new HashMap<String, Object>();
		File[] archivos = filtraArchivos();
		List<File> archivosProcesados = new ArrayList<File>();
		File tmpErrores = creaDirectorioErrores();
		boolean creaZipErrores = false;
		List<Map<String, Object>> procesados = new ArrayList<Map<String,Object>>();
		this.initScCifrasControlCfdi();
		for (File archivo : archivos) {
			LOG.debug("Procesando archivo: {}", archivo.getName());
			String nombreArchivo = creaNombreArchivoSalida(archivo);
			File dirTmpSalida = creaDirTmpSalida(nombreArchivo);
			File tmpEnt = creaSubdirectorio(dirTmpSalida, DIR_TMP_ENTRADA);
			File tmpSal = creaSubdirectorio(dirTmpSalida, DIR_TMP_SALIDA);
			File tmpCor = creaSubdirectorio(tmpSal, DIR_TEMP_CORRECTOS);
			File[] files = descomprimirArchivo(archivo, tmpEnt);
			List<Map<String, Object>> parseados = new ArrayList<Map<String,Object>>();
			for (File file : files) {
				Integer correctos = 0;
				Integer erroneos = 0;
				Archivo salida = creaArchivo(tmpCor, file.getName(), 
						this.config.getReEmplazar());
				Archivo errores = creaArchivo(tmpErrores, file.getName(), 
						this.config.getNombreArchErrores());
				try {
					FileParser parser = 
							new FileIndicesParser(file, config.getParserConfig());
					Integer total = parser.getTotalRows();
					while (parser.hasMoreRows()) {
						try {
							this.mapRow = parser.nextRow();
							setFolioFiscalFactura();
							String body = creaBody();
							guardaReferenciasCruceDetalle();
							salida.agregarLineaConSalto(body);
							correctos++;
						}
						catch (RowParserException ex) {
							LOG.error(ex.getMessage());
							String msj = ex.getMessage() + ":" + ex.getDetail();
							errores.agregarLineaConSalto(msj);
							erroneos++;
							creaZipErrores = true;
						}
						catch (BusinessException ex) {
							String row = (String) this.mapRow.get("row");
							LOG.error(ex.getMessage());
							String msj = ex.getMessage() + ":" + row;
							errores.agregarLineaConSalto(msj);
							erroneos++;
							creaZipErrores = true;
						}
						initAll();
					}
					salida.agregar(this.config.getFinArchivo());
					Map<String, Object> parseado = 
							new HashMap<String, Object>();
					parseado.put("nombre", file.getName());
					parseado.put("correctos", correctos);
					parseado.put("erroneos", erroneos);
					parseado.put("total", total);
					parseados.add(parseado);
				}
				catch (FileParserException ex) {
					LOG.error("FileParserException en procesaArchivo() ", ex);
				}
			}
			if (!parseados.isEmpty()) {
				comprimirArchivo(tmpCor, nombreArchivo);
				Map<String, Object> procesado = 
						new HashMap<String, Object>();
				procesado.put("nombre", archivo.getName());
				procesado.put("parseados", parseados);
				procesados.add(procesado);
			}
			archivosProcesados.add(archivo);
			borrarDirectorio(dirTmpSalida);
		}
		this.setCantidadCfdisRecibidosArchIndices(procesados.size());
		File errores = null;
		model.put("isErrores", creaZipErrores || this.isErroresRepCifras);
		if (creaZipErrores) {
			errores = comprimir(tmpErrores, null, false, 
			this.config.getPasswordFile());
			if (errores != null) {
				model.put("nameAttachment", errores.getName());
				model.put("attachment", errores);
			}
		}
		File repErrores = null;
		File dirRepErrores = this.erroresCifras.getDir();
		if (this.isErroresRepCifras) {
			repErrores = comprimir(dirRepErrores, null, false, 
			this.config.getPasswordFile());
			if (repErrores != null) {
				model.put("nameAttachment0", repErrores.getName());
				model.put("attachment0", repErrores);
			}
		}
		if (!procesados.isEmpty()) { 
			model.put("procesados", procesados);
			mailSender.send(model);
		}
		moverArchivos(archivosProcesados);
		borrarDirectorio(tmpErrores);
		borrarDirectorio(dirRepErrores);
		LOG.info("Se procesaron {} archivo(s)", procesados.size());
	}
	
	/**
	 * Crea nombre archivo salida.
	 *
	 * @param file the file
	 * @return the string
	 */
	private String creaNombreArchivoSalida(File file) {
		String result = file.getName();
		String cadBuscar = this.config.getCadenaBuscar();
		String reemplazo = this.config.getReEmplazar();
		String ext = this.config.getExtFileIn();
		int index = lastIndexOf(upperCase(result), ext);
		result = substring(result, 0, index);
		if (contains(result, cadBuscar)) {
			result = replace(result, cadBuscar, reemplazo);
		}
		
		return result;
	}
	
	/**
	 * Mover archivos.
	 *
	 * @param archivos the archivos
	 */
	private void moverArchivos(List<File> archivos) {
		if (!archivos.isEmpty()) { 
			for (File archivo : archivos) {
				moverArchivoToProcesados(archivo);
			}
		}
	}
	
	/**
	 * Mover archivo to procesados.
	 *
	 * @param archivo the archivo
	 */
	private void moverArchivoToProcesados(File archivo) {
		String rutaArchivo = this.config.getDirProcesados();
		String nombre = archivo.getName();
		File tmp = new File(rutaArchivo + nombre);
		archivo.renameTo(tmp);
	}
	
	/**
	 * Crea dir tmp salida.
	 *
	 * @param nombre the nombre
	 * @return the file
	 */
	private File creaDirTmpSalida(String nombre) {
		File result = null;
		String nombreDir = this.config.getDirEntrada() + nombre + 
				String.valueOf(new Date().getTime());
		result = new File(nombreDir);
		if (!result.exists()) {
			result.mkdir();
			LOG.debug("Se creo el directorio: {}", result.getAbsolutePath());
		}
		
		return result;
	}
	
	/**
	 * Borrar directorio.
	 *
	 * @param dir the dir
	 * @return true, if successful
	 */
	private boolean borrarDirectorio(File dir) {
		boolean result = false;
		try {
			deleteDirectory(dir);
			result = true;
		}
		catch (IOException ex) {
			LOG.error("IOException en borrarDirectorio() ", ex);
		}
		
		return result;
	}

	/**
	 * Inits the all.
	 */
	private void initAll() {
		this.dealDetalle = null;
		this.referenciaCruce = null;
		this.detalles = null;
		this.folioFiscalFactura = null;
	}
	
	/**
	 * Sets the folio fiscal factura.
	 */
	private void setFolioFiscalFactura() {
		String folioFiscal = (String) this.mapRow.get("folioFiscal");
		String msg = "";
		try {
			this.folioFiscalFactura = 
					this.tesFacturacionSapDao.findTesFacturacionSapByFolioFiscal(folioFiscal);
			/* Actualizar si el folio fue informado en el archivo de indices */
			setCfdiRecibidoArchIndices();
		}
		catch (NoResultException ex) {
			LOG.error("NoResultException en setFolioFiscalFactura(): {}", ex.getMessage()); 
			msg = getMessage("msg.tes.fact.sap.noresult.folio", folioFiscal);
			throw new BusinessException(msg);
		}
		catch (NonUniqueResultException ex) {
			LOG.error("NonUniqueResultException en setFolioFiscalFactura(): {}", ex.getMessage());
			msg = getMessage("msg.tes.fact.sap.nonunique.folio", folioFiscal);
			throw new BusinessException(msg);
		}
	}
	
	/**
	 * Sets the cfdi recibido arch indices.
	 */
	private void setCfdiRecibidoArchIndices() {
		Date fechaLecturaSite = (Date) this.folioFiscalFactura[5];
		/* Recibido en el archivo de indices el mismo dia que se solicito a SAP */
		if (this.scCifrasControlCfdi != null && 
				isSameDay(this.scCifrasControlCfdi.getFechaRecepcion(), fechaLecturaSite)) {
			BigInteger id = (BigInteger) this.folioFiscalFactura[3];
			int updatedRows = this.tesFacturacionSapDao.updateCfdiRecibidoArchivoIndices(id);
			LOG.debug("Folios actualizados: {}", updatedRows);
		}
		setDealDetalle();		
	}
	
	/**
	 * Sets the deal detalle.
	 */
	private void setDealDetalle() {
		BigInteger idDeal = (BigInteger) this.folioFiscalFactura[0];
		BigInteger idDetalleLiq = (BigInteger) this.folioFiscalFactura[1];
		try {
			this.dealDetalle = scDealDetalleDao.findScDealDetalle(idDeal, idDetalleLiq);
			setReferenciaCruce();
		}
		catch (NoResultException ex) {
			String msg = this.getMessage("msg.deal.detalle.noresult", 
					String.valueOf(idDeal), String.valueOf(idDetalleLiq));
			throw new BusinessException(msg);
		}
		catch (NonUniqueResultException ex) {
			String msg = this.getMessage("msg.deal.detalle.nonuniqueresult", 
					String.valueOf(idDeal), String.valueOf(idDetalleLiq));
			throw new BusinessException(msg);
		}
	}
	
	/**
	 * Sets the referencia cruce.
	 */
	private void setReferenciaCruce() {
		//Date fechaContable = (Date) this.mapRow.get("fechaContable");
		Date fechaContable = (Date) this.folioFiscalFactura[4];
		String refCruce = dateToString(fechaContable, null);
		try {
			if (this.dealDetalle.getScDealPosicion() != null) {
				String divisa = this.dealDetalle.getScDealPosicion().getIdDivisa();
				refCruce = refCruce + "-" + divisa + "-";
				this.referenciaCruce = scReferenciaCruceDao.
						findScReferenciaCruceWithRefCruce(fechaContable, refCruce);
			}
			else {
				String msg = getMessage("msg.deal.pos.noresult");
				throw new BusinessException(msg);
			}
			setCruceDetalle();
		}
		catch (NoResultException ex) {
			String msg = getMessage("msg.ref.cruce.noresult.like", refCruce);
			throw new BusinessException(msg);
		}
		catch (NonUniqueResultException ex) {
			String msg = getMessage("msg.ref.cruce.nonuniqueresult.like", refCruce);
			throw new BusinessException(msg);
		}
	}
	
	/**
	 * Sets the cruce detalle.
	 */
	private void setCruceDetalle() {
		String folio = (String) this.mapRow.get("folioFiscal");
		Long idRefCruce = null;
		ScReferenciaCruceDetalle detalle = null;
		ScReferenciaCruceDetalle detalleAnt  = null;
		String msg = null;
		try {
			this.detalles = new ArrayList<ScReferenciaCruceDetalle>();
			if (this.referenciaCruce != null) {
				idRefCruce = this.referenciaCruce.getIdReferenciaCruce();
				detalle = this.scRefCruceDetalleDao.
						findScReferenciaCruceDetalleByIdRefCruceAndFolio(idRefCruce, folio);
				if (ScReferenciaCruceDetalle.STATUS_INACTIVO.equals(detalle.getStatus())) {
					String des = this.config.getDesStatusFactura(TesFacturacionSap.STATUS_CANC);
					msg = getMessage("msg.ref.cruce.det.invalid.status", folio, des);
					throw new BusinessException(msg);
				}
				else {
					detalle.setFechaUltMod(new Date());
					String statusFactura = (String) this.folioFiscalFactura[2];
					if (TesFacturacionSap.STATUS_FACT.equals(statusFactura)) {
						String reproc = config.getIndReproceso(REPROCESO_OK);
						if (reproc.equals(detalle.getReproceso())) {//Es un reproceso
							Long id = detalle.getIdDetalleAnt();
							if (id > 0) {
								detalleAnt = scRefCruceDetalleDao.
										findScReferenciaCruceDetalleById(id);
								if (detalleAnt != null) {
									detalle.setFolioFiscalAnt(detalleAnt.getFolioFiscal());
								}
							}
						}
					}
					else {
						String des = 
								this.config.getDesStatusFactura(TesFacturacionSap.STATUS_FACT);
						msg = getMessage("msg.tes.fact.sap.incorrect.status", folio, des);
						throw new BusinessException(msg);
					}
				}
				this.detalles.add(detalle);
			}
		}
		catch (NoResultException ex) {
			LOG.error(ex.getMessage());
			setFacturacionSap();
		}
		catch (NonUniqueResultException ex) {
			msg = getMessage("msg.ref.cruce.det.nonuniq.folio", 
					String.valueOf(idRefCruce), folio);
			throw new BusinessException(msg);
		}
	}
	
	/**
	 * Sets the facturacion sap.
	 */
	private void setFacturacionSap() {
		String folioFiscal = (String) this.mapRow.get("folioFiscal");
		BigInteger idDeal = (BigInteger) this.folioFiscalFactura[0];
		BigInteger idDetLiq = (BigInteger) this.folioFiscalFactura[1];
		String msg = null;
		ScReferenciaCruceDetalle detalle = null;
		List<TesFacturacionSap> facturas = 
				this.tesFacturacionSapDao.findTesFacturacionSap(idDeal, idDetLiq);
		if (facturas == null || facturas.isEmpty() ) {
			msg = getMessage("msg.tes.fact.sap.noresult.deal.det", 
					String.valueOf(idDeal), String.valueOf(idDetLiq));
			throw new BusinessException(msg);
		}
		else {
			boolean encontrado = false;
			List<String> folios = new ArrayList<String>();
			for (TesFacturacionSap factura : facturas) {
				if (isNotEmpty(factura.getFolioFiscalFactura())) {
					if (trim(factura.getFolioFiscalFactura()).equals(folioFiscal)) {
						encontrado = true;
						LOG.debug("Factura encontrada: {}", factura.getIdMovimiento());
					}
					else if (TesFacturacionSap.STATUS_CANC.
							equals(trim(factura.getEstatusFactura()))) {
						folios.add(factura.getFolioFiscalFactura());
					}
				}
			}
			String statusFactura = (String) this.folioFiscalFactura[2]; 
			if (!encontrado) {
				msg = getMessage("msg.tes.fact.sap.noresult.folio", folioFiscal);
				throw new BusinessException(msg);
			}
			else if (!TesFacturacionSap.STATUS_FACT.equals(trim(statusFactura))) {
				String des = 
						this.config.getDesStatusFactura(TesFacturacionSap.STATUS_FACT);
				msg = getMessage("msg.tes.fact.sap.incorrect.status", folioFiscal, des);
				throw new BusinessException(msg);
			}
			Long idDetalleAnt = new Long("0");
			Short status = ScReferenciaCruceDetalle.STATUS_ACTIVO;
			if (folios.isEmpty()) {//Solo hay un folio fiscal
				detalle = new ScReferenciaCruceDetalle();
				detalle.setScReferenciaCruce(this.referenciaCruce);
				detalle.setFolioFiscal(folioFiscal);
				String indReproceso = config.getIndReproceso(REPROCESO_KO);
				detalle.setReproceso(indReproceso);
				detalle.setFechaAlta(new Date());
				detalle.setFechaUltMod(new Date());
				detalle.setIdDetalleAnt(idDetalleAnt);
				detalle.setStatus(status);
				this.detalles.add(detalle);
			}
			else {
				Long idRefCruce = this.referenciaCruce.getIdReferenciaCruce();
				List<ScReferenciaCruceDetalle> tmpDetalles = 
						this.scRefCruceDetalleDao.findFoliosByIdRefCruce(idRefCruce, folios);
				LOG.debug("Numero de registros encontrados: {}", tmpDetalles.size());
				if (tmpDetalles.isEmpty()) {
					detalle = new ScReferenciaCruceDetalle();
					detalle.setScReferenciaCruce(this.referenciaCruce);
					detalle.setFolioFiscal(folioFiscal);
					String indReproceso = config.getIndReproceso(REPROCESO_KO);
					detalle.setReproceso(indReproceso);
					detalle.setFechaAlta(new Date());
					detalle.setFechaUltMod(new Date());
					detalle.setIdDetalleAnt(idDetalleAnt);
					detalle.setStatus(status);
					this.detalles.add(detalle);
				}
				else if (tmpDetalles.size() == 1) {
					detalle = tmpDetalles.get(0);
					detalle.setFechaUltMod(new Date());
					detalle.setStatus(ScReferenciaCruceDetalle.STATUS_INACTIVO);
					idDetalleAnt = detalle.getIdRefCruceDetalle();
					String folioAnt = 
							new String(detalle.getFolioFiscal());
					String indReproceso = config.getIndReproceso(REPROCESO_OK);
					ScReferenciaCruceDetalle tmpDetalle = new ScReferenciaCruceDetalle();
					tmpDetalle.setScReferenciaCruce(this.referenciaCruce);
					tmpDetalle.setFolioFiscal(folioFiscal);
					tmpDetalle.setFolioFiscalAnt(folioAnt);
					tmpDetalle.setReproceso(indReproceso);
					tmpDetalle.setFechaAlta(new Date());
					tmpDetalle.setFechaUltMod(new Date());
					tmpDetalle.setIdDetalleAnt(idDetalleAnt);
					tmpDetalle.setStatus(status);
					this.detalles.add(tmpDetalle);
					this.detalles.add(detalle);
				}
				else {
					msg = getMessage("msg.ref.cruce.det.nonuniq.folios", idRefCruce, detalles);
					throw new BusinessException(msg);
				}
				
			}
		}
	}
	
	/*private String findFolioAnterior() {
		String folioAnterior = "";
		BigInteger idDeal = (BigInteger) this.mapRow.get("idDeal");
		BigInteger idDetLiq = (BigInteger) this.mapRow.get("idDetalleLiq");
		String status = STATUS_CANC;
		BigInteger idMov = this.tesFacturacionSapDao.
				findMaxMovimientoByDealDetalleStatus(idDeal, idDetLiq, status);
		LOG.info("IdMovimiento: {}", idMov);
		if (idMov != null) {
			TesFacturacionSap sap = 
					this.tesFacturacionSapDao.findTesFacturacionSapById(idMov);
			if (sap != null) {
				folioAnterior = sap.getFolioFiscalFactura(); 
			}
		}
		
		return folioAnterior;
	}*/
	
	/**
	 * Crea subdirectorio.
	 *
	 * @param padre the padre
	 * @param nombre the nombre
	 * @return the file
	 */
	private File creaSubdirectorio(File padre, String nombre) {
		File result = null;
		
		String path = padre.getAbsolutePath();
		String separador = File.separator;
		String nombreDir = path + separador + nombre;
		result = new File(nombreDir);
		if (!result.exists()) {
			result.mkdir();
		}
		
		return result;
	}
	
	/**
	 * Descomprimir archivo.
	 *
	 * @param archivo the archivo
	 * @param dirDest the dir dest
	 * @return the file[]
	 */
	private File[] descomprimirArchivo(File archivo, File dirDest) {
		File[] result = null;
		String password = this.config.getPasswordFile();
		String dirTemp = dirDest.getAbsolutePath();
		boolean descomprimido = descomprimir(archivo, null, dirTemp, password);
		if (!descomprimido) {
			if (copiarArchivo(archivo, dirDest)) {
				result = dirDest.listFiles();
			}
		}
		else {
			result = dirDest.listFiles();
		}
		sortFilesByName(result, true);
		
		return result;
	}
	
	/**
	 * Comprimir archivo.
	 *
	 * @param dir the dir
	 * @param nombreArchivo the nombre archivo
	 */
	private void comprimirArchivo(File dir, String nombreArchivo) {
		String dest =  this.config.getDirSalida();
		String password = this.config.getPasswordFile();
		if (this.config.isZipOut()) {
			dest = dest + nombreArchivo + ".ZIP";
			comprimir(dir, dest, false, password);
		}
		else {
			copiarDirectorio(dir, dest);
		}
	}
	
	/**
	 * Copiar directorio.
	 *
	 * @param srcDir the src dir
	 * @param destDir the dest dir
	 * @return true, if successful
	 */
	private boolean copiarDirectorio(File srcDir, String destDir) {
		File dest = new File(destDir);
		
		return copiarDirectorio(srcDir, dest);
	}
	
	/**
	 * Copiar directorio.
	 *
	 * @param srcDir the src dir
	 * @param destDir the dest dir
	 * @return true, if successful
	 */
	private boolean copiarDirectorio(File srcDir, File destDir) {
		boolean result = false;
		try {
			copyDirectory(srcDir, destDir);
			result = true;
		}
		catch (IOException ex) {
			LOG.error("IOException en copiarDirectorio() ", ex);
		}
		
		return result;
	}	
	
	/**
	 * Copiar archivo.
	 *
	 * @param archivo the archivo
	 * @param dir the dir
	 * @return true, if successful
	 */
	private boolean copiarArchivo(File archivo, File dir) {
		boolean result = false;
		String rutaArchivo = dir.getAbsolutePath() + File.separator + archivo.getName();
		try {
			File nuevo = new File(rutaArchivo);
			copyFile(archivo, nuevo);
			result = true;
		}
		catch (IOException ex) {
			LOG.error("IOException en copiarArchivo() ", ex);
		}
		
		return result;
	}
	
	/**
	 * Guarda referencias cruce detalle.
	 */
	private void guardaReferenciasCruceDetalle() {
		if (this.detalles != null) {
			for (ScReferenciaCruceDetalle detalle : this.detalles) {
				if (detalle.getIdRefCruceDetalle() != null) {
					this.scRefCruceDetalleDao.update(detalle);
					LOG.debug("Se actualizo el detalle: {}", 
							detalle.getIdRefCruceDetalle());
				}
				else {
					this.scRefCruceDetalleDao.saveScReferenciaCruceDetalle(detalle);
					LOG.debug("Se registro el detalle: {}", 
							detalle.getIdRefCruceDetalle());
				}
			}
		}
		
	}
	
	/**
	 * Crea archivo.
	 *
	 * @param dir the dir
	 * @param nombre the nombre
	 * @param reemplazo the reemplazo
	 * @return the archivo
	 */
	private Archivo creaArchivo(File dir, String nombre, String reemplazo) {
		String cadBuscar = this.config.getCadenaBuscar();
		String padre = dir.getAbsolutePath();
		String separador = File.separator;
		String nombreArch = nombre;
		if (contains(nombreArch, cadBuscar)) {
			nombreArch = replace(nombreArch, cadBuscar, reemplazo);
		}

		return new Archivo(padre + separador, nombreArch, true);
	}
	
	/*private Archivo  creaArchivoSalida() {
		Map<String, Map<String, Object>> config = this.config.getNomArchSalida();
		return creaArchivo(config);
	}
	
	private Archivo creaArchivoErrores() {
		Map<String, Map<String, Object>> config = this.config.getNomArchErrores();
		return creaArchivo(config);
	}
	
	private Archivo creaArchivo(Map<String, Map<String, Object>> config) {
		String nombre = getNombreArchivo(config);
		LOG.debug("Nombre del archivo: {}", nombre);
		String dir = this.config.getDirSalida();
		LOG.debug("Directorio de salida: {}", dir);
		Archivo archivo = new Archivo(dir, nombre, true);
		
		return archivo;
	}
	
	private String getNombreArchivo(Map<String, Map<String, Object>> config) {
		String result = "";
		for (String field : config.keySet()) {
			String temp = "";
			Map<String, Object> env = config.get(field);
			String from = (String) env.get("from");
			if ("FILE".equals(from)) {
				temp = (String) env.get("value");
			}
			else if ("CODE".equals(from)) {
				Object params = env.get("params");
				LOG.debug("params: {}", params);
				if (params == null) {//Llamada sin parametros.
					temp = invokeCode(field, true);
				}
				else if (params instanceof Object[]) {//Llmada con n parametros.
					temp = invokeCode(field, true, (Object[]) params);
				}
				else {//Llamada con un solo parametro
					temp = invokeCode(field, true, params);
				}
			}
			result = result + temp;
		}
			
		return result;
	}*/
	
	
	/**
	 * Gets the fecha file.
	 *
	 * @param pattern the pattern
	 * @return the fecha file
	 */
	public String getFechaFile(String pattern) {
		return dateToString(null, pattern);
	}
	
	/**
	 * Gets the ref unica.
	 *
	 * @return the ref unica
	 */
	public String getRefUnica() {
		String refUnica = "";
		if (this.referenciaCruce != null) {
			refUnica = this.referenciaCruce.getReferenciaCruce();
		}
		
		return refUnica;
	}
	
	/**
	 * Gets the folio fiscal.
	 *
	 * @return the folio fiscal
	 */
	public String getFolioFiscal() {
		String folioFiscal = "";
		if (this.detalles != null && !this.detalles.isEmpty()) {
			folioFiscal = this.detalles.get(0).getFolioFiscal();
		}
		
		return folioFiscal;
	}
	
	/**
	 * Gets the folio fiscal ant.
	 *
	 * @return the folio fiscal ant
	 */
	public String getFolioFiscalAnt() {
		String folioFiscalAnt = "";
		if (this.detalles != null && !this.detalles.isEmpty()) {
			folioFiscalAnt = this.detalles.get(0).getFolioFiscalAnt();
		}
		
		return folioFiscalAnt;
	}
	
	/**
	 * Gets the fecha proceso.
	 *
	 * @param pattern the pattern
	 * @return the fecha proceso
	 */
	public String getFechaProceso(String pattern) {
		return dateToString(null, pattern);
	}
	
	/**
	 * Gets the fecha timbrado.
	 *
	 * @param pattern the pattern
	 * @return the fecha timbrado
	 */
	public String getFechaTimbrado(String pattern) {
		Date fecha = (Date) mapRow.get("fechaTimbrado");
		
		return dateToString(fecha, pattern); 
	}
	
	/**
	 * Gets the fecha contable.
	 *
	 * @param pattern the pattern
	 * @return the fecha contable
	 */
	public String getFechaContable(String pattern) {
		Date fecha = (Date) this.folioFiscalFactura[4];
		
		return dateToString(fecha, pattern); 
	}
	
	/**
	 * Gets the ind reproceso.
	 *
	 * @return the ind reproceso
	 */
	public String getIndReproceso() {
		String indReproceso = "";
		if (this.detalles != null && !this.detalles.isEmpty()) {
			indReproceso = this.detalles.get(0).getReproceso();
		}
		
		return indReproceso;
	}

	/**
	 * Gets the env filler.
	 *
	 * @param type the type
	 * @return the env filler
	 */
	protected Map<String, String> getEnvFiller(String type) {
		return this.config.getEnvFiller(type);
	}

	/**
	 * Gets the body.
	 *
	 * @return the body
	 */
	protected Map<String, Map<String, Object>> getBody() {
		return this.config.getBody();
	}
	
	/**
	 * Gets the message.
	 *
	 * @param key the key
	 * @param param the param
	 * @return the message
	 */
	private String getMessage(String key, Object... param) {
		String mensaje = null;
		if (messages != null) {
			mensaje = messages.getMessage(key, param, DEFAULT_MSG, LOCALE_ES);
		}
		
		return mensaje;
	}
	
	/**
	 * Crea directorio errores.
	 *
	 * @return the file
	 */
	private File creaDirectorioErrores() {
		File erroresDir = null;
		String dirEntrada = this.config.getDirEntrada();
		String dirErrores = this.config.getNombreArchErrores();
		String fecha = String.valueOf(new Date().getTime());
		String rutaDirectorio = dirEntrada + dirErrores + fecha;
		erroresDir = new File(rutaDirectorio);
		if (!erroresDir.exists()) {
			erroresDir.mkdir();
		}
		
		return erroresDir;
	}
	
	/**
	 * Crea errores reportes cifras.
	 *
	 * @return the archivo
	 */
	private Archivo creaErroresReportesCifras() {
		String dirEntrada = this.config.getDirRepCfdiPorenviar();
		String dirErrores = this.config.getNombreArchErrores();
		String fecha = String.valueOf(new Date().getTime());
		String rutaDirectorio = dirEntrada + dirErrores + fecha;
		String nombreRepErrores = this.config.getNombreRepCifrasError();
		File tmpDir= new File(rutaDirectorio);
		if (!tmpDir.exists()) {
			tmpDir.mkdir();
		}
		
		return this.creaArchivo(tmpDir, nombreRepErrores, "");
	} 
	
	/**
	 * Depurar historico archivos.
	 */
	@Override
	public void depurarHistoricoArchivos() {
		Short diasHistorico = this.config.getDiasHistorico();
		FiltroHistorico filtro = new FiltroHistorico(diasHistorico);
		depurarDirProcesados(filtro);
		depurarDirEnviados(filtro);
		depurarDirRreportesCfdi(filtro);
	}
	
	/**
	 * Depurar dir procesados.
	 *
	 * @param filtro the filtro
	 */
	private void depurarDirProcesados(FiltroHistorico filtro) {
		String dirDepurar = this.config.getDirProcesados();
		depurarDirectorio(filtro, dirDepurar);
	}
	
	/**
	 * Depurar dir rreportes cfdi.
	 *
	 * @param filtro the filtro
	 */
	private void depurarDirRreportesCfdi(FiltroHistorico filtro) {
		String dirReportes = this.config.getDirRepCfdiEnviados();
		depurarDirectorio(filtro, dirReportes);
	}
	
	/**
	 * Depurar dir enviados.
	 *
	 * @param filtro the filtro
	 */
	private void depurarDirEnviados(FiltroHistorico filtro) {
		String dirEnviados = this.config.getDirEnviados();
		depurarDirectorio(filtro, dirEnviados);
	}
	
	/**
	 * Depurar directorio.
	 *
	 * @param filtro the filtro
	 * @param dir the dir
	 */
	private void depurarDirectorio(FiltroHistorico filtro, String dir) {
		File temp = new File(dir);
		if (temp.exists()) {
			File[] archivos = null;
			if (filtro != null) {
				archivos = temp.listFiles(filtro);
			}
			else {
				archivos = temp.listFiles();
			}
			eliminarArchivos(archivos);
		}
		else {
			LOG.error("La carpeta {} no existe.", dir);
		}
	}
	
	/**
	 * Eliminar archivos.
	 *
	 * @param archivos the archivos
	 */
	private void eliminarArchivos(File[] archivos) {
		Map<String, String> eliminados = new HashMap<String, String>(); 
		
		for (File archivo : archivos) {
			if (archivo.delete()) {
				eliminados.put(archivo.getName(), archivo.getName());
			}
		}
		
		LOG.debug("Archivos eliminados[{}]: {}", eliminados.size(), eliminados);
	}
	
	
	/**
	 * Inits the sc cifras control cfdi.
	 */
	private void initScCifrasControlCfdi() {
		this.erroresCifras = this.creaErroresReportesCifras();
		try {
			ScControlProcesoCifras control = 
					this.scCifrasControlDao.findScControlProcesoCifras();
			Long id = control.getIdCifrasControl();
			ScCifrasControlCfdi cifras = 
					this.scCifrasControlDao.findScCifrasControlById(id);
			if (cifras != null) {
				Short estatus = new Short(cifras.getEstatus());
				if (cifras.getFechaRecepcionStr().equals(control.getFechaRecepcionStr()) &&
						estatus.equals(ESTATUS_REG_SAP)) {
					this.scCifrasControlCfdi = cifras;
					this.actualizaCfdisNoRecibidos();
				}
				else {
					this.agregaErrorReportesCifras("msg.rep.cifras.config.invalid", "");
				}
				
			}
		}
		catch (NoResultException ex) {
			LOG.error("NoResultException en setScCifrasControlCfdi() ", ex);
			this.agregaErrorReportesCifras("msg.rep.cifras.config.empty", ex.getMessage());
		}
		catch (NonUniqueResultException ex) {
			LOG.error("NonUniqueResultException en setScCifrasControlCfdi() ", ex);
			this.agregaErrorReportesCifras("msg.rep.cifras.config.nonuniq", ex.getMessage());
		}
	}
	
	/**
	 * Actualiza cfdis no recibidos.
	 */
	private void actualizaCfdisNoRecibidos() {
		if (this.scCifrasControlCfdi.isReproceso()) {
			Date ini = reseteaHoraFecha(this.scCifrasControlCfdi.getFechaRecepcion());
			Date fin = 
					reseteaHoraFecha(this.scCifrasControlCfdi.getFechaRecepcion(), 23, 59, 59);
			int registros = this.tesFacturacionSapDao.
					updateNoRecibidosCfdisArchIndices(ini, fin);
			LOG.debug("Registros actualizados: {}", registros);
		}
	}
	
	/**
	 * Agrega error reportes cifras.
	 *
	 * @param key the key
	 * @param detalle the detalle
	 */
	private void agregaErrorReportesCifras(String key, String detalle) {
		if (this.erroresCifras != null) {
			String msg = null;
			if (isNotEmpty(key)) {
				msg = this.getMessage(key);
				if (isNotEmpty(detalle)) {
					msg = msg + " " + detalle;
				}
			}
			else {
				msg = detalle;
			}
			this.isErroresRepCifras = true;
			this.erroresCifras.agregarLineaConSalto(msg);
		}
	}
	
	/**
	 * Sets the cantidad cfdis recibidos arch indices.
	 *
	 * @param cantProcesados the new cantidad cfdis recibidos arch indices
	 */
	private void setCantidadCfdisRecibidosArchIndices(int cantProcesados) {
		if (cantProcesados > 0 && this.scCifrasControlCfdi != null) {
			String estatus = TesFacturacionSap.STATUS_FACT;
			Date ini = reseteaHoraFecha(this.scCifrasControlCfdi.getFechaRecepcion());
			Date fin = reseteaHoraFecha(this.scCifrasControlCfdi.getFechaRecepcion(), 
					                    23, 59, 59);
			Long cantidadCfdisRecibidos = 
					this.tesFacturacionSapDao.findCantidadCfdisRecibidosArchIndices(ini,
																					fin, 
																					estatus);
			LOG.debug("cantidadCfdisRecibidos: {}", cantidadCfdisRecibidos);
			if (cantidadCfdisRecibidos == null) {
				cantidadCfdisRecibidos = 0L;
			}
			this.scCifrasControlCfdi.
					setCantidadRecibidosIndices(cantidadCfdisRecibidos.intValue());
			this.scCifrasControlCfdi.setEstatus(ESTATUS_REG_INDICES);
			this.generaReportesCfdis();
		}
	}
	
	/**
	 * Genera reportes cifras control.
	 */
	private void generaReportesCfdis() {
		String reporteCifras = null;
		String reporteCfdis = null;
		Short estatus = this.scCifrasControlCfdi.getEstatus();
		try {
			reporteCifras = this.generaReporteCifrasControl();
			estatus = ESTATUS_FIN_REP_CIFRAS;
		}
		catch (FileReportException ex) {
			this.agregaErrorReportesCifras(null, ex.getMessage());
		}
		try {
			reporteCfdis = this.generaReportesCfdisFaltantes();
			estatus = ESTATUS_FIN_REP_DIFERENCIAS;
		}
		catch (FileReportException ex) {
			this.agregaErrorReportesCifras(null, ex.getMessage());
		}
		if (reporteCifras != null && reporteCfdis != null) {
			estatus = ESTATUS_PROCESO_FINALIZADO;
		}
		this.scCifrasControlCfdi.setArchivoCifrasControl(reporteCifras);
		this.scCifrasControlCfdi.setArchivosCfdisFaltantes(reporteCfdis);
		this.scCifrasControlCfdi.setEstatus(estatus);
		this.scCifrasControlCfdi.setFechaUltMod(new Date());
		this.scCifrasControlDao.updateScCifrasControl(this.scCifrasControlCfdi);
	}
	
	/**
	 * Genera reporte cifras control.
	 *
	 * @return the string
	 */
	private String generaReporteCifrasControl() {
		String reporte = null;
		try {
			String content = this.creaContenidoReporteCifrasControl();
			Map<String, String> env = this.config.getConfigRepCifras();
			String repCifrasNombre = env.get("reporte");
			String encoding = env.get("encoding");
			reporte = replace(repCifrasNombre, "fecha", 
					this.scCifrasControlCfdi.getFechaRecepcionStr());
			LOG.debug("Se va a generar el reporte: {}", 
					this.config.getDirRepCfdiPorenviar() + reporte);
			File fileReport = new File(this.config.getDirRepCfdiPorenviar() + reporte);
			writeStringToFile(fileReport, content, encoding);
		}
		catch (VelocityException ex) {
			LOG.error("VelocityException en generaReporteCifrasControl() ", ex);
			String msg = this.getMessage("msg.rep.cifras.control.create") + " " + ex.getMessage();
			throw new FileReportException(msg);
		}
		catch (IOException ex) {
			LOG.error("IOException en generaReporteCifrasControl() ", ex);
			String msg = this.getMessage("msg.rep.cifras.control.create") + " " + ex.getMessage();
			throw new FileReportException(msg);
		}
		
		return reporte;
	}
	
	/**
	 * Genera reportes cfdis faltantes.
	 *
	 * @return the string
	 */
	private String generaReportesCfdisFaltantes() {
		String reporte = null;
		try {
			String content = this.creaContenidoReporteCfdisFaltantes();
			Map<String, String> env = this.config.getConfigRepCfdiDet();
			String repCfdisDetNombre = env.get("reporte");
			String encoding = env.get("encoding");
			reporte = replace(repCfdisDetNombre, "fecha", 
					this.scCifrasControlCfdi.getFechaRecepcionStr());
			LOG.debug("Se va a generar el reporte: {}", 
					this.config.getDirRepCfdiPorenviar() + reporte);
			File fileReport = new File(this.config.getDirRepCfdiPorenviar() + reporte);
			writeStringToFile(fileReport, content, encoding);
		}
		catch (VelocityException ex) {
			LOG.error("VelocityException en generaReportesCfdisFaltantes() ", ex);
			String msg = this.getMessage("msg.rep.cifras.detalle.create") + " " + ex.getMessage();
			throw new FileReportException(msg);
		}
		catch (IOException ex) {
			LOG.error("IOException en generaReportesCfdisFaltantes() ", ex);
			String msg = this.getMessage("msg.rep.cifras.detalle.create") + " " + ex.getMessage();
			throw new FileReportException(msg);
		}
		
		return reporte;
	}
	
	/**
	 * Crea contenido reporte cifras control.
	 *
	 * @return the string
	 * @throws VelocityException the velocity exception
	 */
	private String creaContenidoReporteCifrasControl() 
			throws VelocityException {
		Map<String, String> env = this.config.getConfigRepCifras();
		String reporte = env.get("reporte");
		String pattern = env.get("pattern");
		String template = env.get("template");;
		reporte = replace(reporte, "fecha", this.scCifrasControlCfdi.getFechaRecepcionStr());
		Map<String, Object> model = new HashMap<String, Object>(env);
		model.put("reporte", reporte);
		model.put("fecha", dateToString(this.scCifrasControlCfdi.getFechaRecepcion(), pattern));
		model.put("recibidosSAP", this.scCifrasControlCfdi.getCantidadRecibidosSap());
		model.put("recibidosIndices", this.scCifrasControlCfdi.getCantidadRecibidosIndices());
		Integer diff = this.scCifrasControlCfdi.getCantidadRecibidosSap() - 
				this.scCifrasControlCfdi.getCantidadRecibidosIndices();
		LOG.debug("Cantidad de CFDIS no recibidos: {}", diff);
		model.put("diferencia", diff);
		LOG.debug("model: {}", model);
		
		return mergeTemplateIntoString(this.velocityEngine, template, model);
	}
	
	/**
	 * Crea contenido reporte cfdis faltantes.
	 *
	 * @return the string
	 * @throws VelocityException the velocity exception
	 */
	private String creaContenidoReporteCfdisFaltantes() 
			throws VelocityException {
		Map<String, String> env = this.config.getConfigRepCfdiDet();
		String reporte = env.get("reporte");
		String pattern = env.get("pattern");
		String template = env.get("template");;
		reporte = replace(reporte, "fecha", this.scCifrasControlCfdi.getFechaRecepcionStr());
		Map<String, Object> model = new HashMap<String, Object>(env);
		model.put("reporte", reporte);
		model.put("fecha", dateToString(this.scCifrasControlCfdi.getFechaRecepcion(), pattern));
		Integer diff = this.scCifrasControlCfdi.getCantidadRecibidosSap() - 
				this.scCifrasControlCfdi.getCantidadRecibidosIndices();
		List<Object[]> cfdisFaltantes = new ArrayList<Object[]>();
		Integer cantFaltantes = 0;
		if (diff > 0) {
			Date ini = 
					reseteaHoraFecha(this.scCifrasControlCfdi.getFechaRecepcion());
			Date fin = 
					reseteaHoraFecha(this.scCifrasControlCfdi.getFechaRecepcion(), 23, 59, 59);
			String estatus = TesFacturacionSap.STATUS_FACT;
			cfdisFaltantes = 
					this.tesFacturacionSapDao.findCfdisNoRecibidosArchIndices(ini, fin, estatus);
			List<Map<String, Object>> cfdis = new ArrayList<Map<String,Object>>();
			for (Object[] cfdiObj : cfdisFaltantes) {
				Map<String, Object> cfdi = new HashMap<String, Object>();
				cfdi.put("idLiq", cfdiObj[0]);
				cfdi.put("idDeal", cfdiObj[1]);
				cfdi.put("folio", cfdiObj[2]);
				cfdis.add(cfdi);
			}
			model.put("cfdis", cfdis);
			cantFaltantes = cfdis.size();
		}
		model.put("cantFaltantes", cantFaltantes);
		LOG.debug("model: {}", model);
		
		return mergeTemplateIntoString(this.velocityEngine, template, model);
	}
}

