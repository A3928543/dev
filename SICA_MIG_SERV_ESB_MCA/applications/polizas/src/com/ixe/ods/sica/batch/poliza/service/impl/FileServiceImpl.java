package com.ixe.ods.sica.batch.poliza.service.impl;

import static org.apache.commons.lang.StringUtils.defaultIfEmpty;
import static org.apache.commons.lang.StringUtils.defaultString;
import static org.apache.commons.lang.StringUtils.isEmpty;
import static org.apache.commons.lang.StringUtils.isNotEmpty;
import static org.apache.commons.lang.StringUtils.isNumeric;
import static org.apache.commons.lang.StringUtils.leftPad;
import static org.apache.commons.lang.StringUtils.replace;
import static org.apache.commons.lang.StringUtils.rightPad;
import static org.apache.commons.lang.StringUtils.startsWith;
import static org.apache.commons.lang.StringUtils.substring;
import static org.apache.commons.lang.StringUtils.trim;
import static org.apache.commons.lang.reflect.MethodUtils.invokeMethod;
import static org.apache.commons.lang.time.DateUtils.addDays;
import static org.springframework.transaction.annotation.Isolation.DEFAULT;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ixe.ods.sica.batch.dao.BupPersonaCuentaRolDao;
import com.ixe.ods.sica.batch.dao.PolizaDao;
import com.ixe.ods.sica.batch.dao.SapBitacoraXsDao;
import com.ixe.ods.sica.batch.dao.SapGenpolXsDao;
import com.ixe.ods.sica.batch.dao.ScClienteDao;
import com.ixe.ods.sica.batch.dao.ScDealDao;
import com.ixe.ods.sica.batch.dao.ScDealPosicionDao;
import com.ixe.ods.sica.batch.dao.ScLoteProcesadoDao;
import com.ixe.ods.sica.batch.dao.ScReferenciaCruceDao;
import com.ixe.ods.sica.batch.domain.SapABitacoraXs;
import com.ixe.ods.sica.batch.domain.SapAGenpolXs;
import com.ixe.ods.sica.batch.domain.SapAGenpolXsPK;
import com.ixe.ods.sica.batch.domain.ScCliente;
import com.ixe.ods.sica.batch.domain.ScLoteProcesado;
import com.ixe.ods.sica.batch.domain.ScParametro;
import com.ixe.ods.sica.batch.domain.ScReferenciaCruce;
import com.ixe.ods.sica.batch.error.BusinessException;
import com.ixe.ods.sica.batch.poliza.service.FileService;
import com.ixe.ods.sica.batch.poliza.util.ConfiguracionApp;
import com.ixe.ods.sica.batch.poliza.util.FiltroArchivo;
import com.ixe.ods.sica.batch.util.Archivo;

/**
 * The Class FileServiceImpl.
 */
@Service("fileService")
public class FileServiceImpl extends BaseService implements FileService {
	
	/** The Constant LOG. */
	private static final Logger LOG = LoggerFactory.getLogger(FileServiceImpl.class);
	
	/** The sap genpol xs dao. */
	@Autowired
	private SapGenpolXsDao sapGenpolXsDao;
	
	/** The sap bitacora xs dao. */
	@Autowired
	private SapBitacoraXsDao sapBitacoraXsDao;
	
	/** The sc referencia cruce dao. */
	@Autowired
	private ScReferenciaCruceDao scReferenciaCruceDao;
	
	/** The app. */
	@Autowired
	private ConfiguracionApp app;
	
	/** The poliza dao. */
	@Autowired
	private PolizaDao polizaDao;
	
	/** The bup persona cuenta rol dao. */
	@Autowired
	private BupPersonaCuentaRolDao bupPersonaCuentaRolDao;
	
	/** The sc deal dao. */
	@Autowired
	private ScDealDao scDealDao;
	
	/** The sc cliente dao. */
	@Autowired
	private ScClienteDao scClienteDao;
	
	/** The sc deal posicion dao. */
	@Autowired
	private ScDealPosicionDao scDealPosicionDao;
	
	/** The sc lote procesado dao. */
	@Autowired
	private ScLoteProcesadoDao scLoteProcesadoDao;
	
	/** The messages. */
	@Autowired
	private ReloadableResourceBundleMessageSource messages;
	
	/** The sap A bitacora xs. */
	private SapABitacoraXs sapABitacoraXs;
	
	/** The sap A genpol xs. */
	private SapAGenpolXs sapAGenpolXs;
	
	/** The referencia cruce. */
	private ScReferenciaCruce referenciaCruce;
	
	/** The deal. */
	private Object[] deal;
	
	/** The persona. */
	private Object[] persona;
	
	/** The impdmxp. */
	private BigDecimal impdmxp;
	
	/** The imphmxp. */
	private BigDecimal imphmxp;
	
	/** The impddiv. */
	private BigDecimal impddiv;
	
	/** The imphdiv. */
	private BigDecimal imphdiv;
	
	/** The cliente. */
	private ScCliente cliente;
	
	/** The consecutivo. */
	private Integer consecutivo;
	
	/** The is proceso diario. */
	private boolean isProcesoDiario;
	
	/**
	 * Depura historico archivos detalle polizas.
	 */
	@Override
	public void depuraHistoricoArchivosDetallePolizas() {
		String prefArchDepurar = app.getPreArchDepurar();
		Short diasHistorico = app.getDiasHistorico();
		FiltroArchivo filtroArchivo = 
				new FiltroArchivo(prefArchDepurar, diasHistorico);
		File dir = new File(app.getDirDepuracion());
		if(dir.isDirectory()) {
			File[] archivos = dir.listFiles(filtroArchivo);
			int cont = 0;
			for (File archivo : archivos) {
				archivo.delete();
				cont++;
			}
			LOG.debug("Se eliminaron {} archivo(s)", cont);
		}
		else {
			LOG.error("Error al leer la carpeta {}.", app.getDirDepuracion());
		}
	}
	
	/**
	 * Generar archivo detalle polizas.
	 *
	 * @return true, if successful
	 */
	@Override
	@Transactional(propagation = REQUIRED, isolation = DEFAULT, readOnly = false)	
	public boolean generarArchivoDetallePolizas() {
		boolean procesoOk = false;
		List<Map<String, Object>> lLotes = new ArrayList<Map<String, Object>>();
		Map<String, Object> model = new HashMap<String, Object> ();
		Archivo archivo = null;
		this.consecutivo = new Integer("1");
		String ruta = app.getDirSalida();
		List<SapABitacoraXs> lotes = findSapABitacoraXs();
		for (SapABitacoraXs bit : lotes) {
			String nombreArchivo = creaNombreArchivo();
			archivo = new Archivo(ruta, nombreArchivo, true);
			setSapABitacoraXs(bit);
			String header = creaHeader();
			archivo.agregarLineaConSalto(header);
			LOG.debug("header: {}", header);
			List<SapAGenpolXsPK> pks = 
					sapGenpolXsDao.findSapAGenpolXsIdsByLote(bit.getIdCarga());
			LOG.debug("idCarga[{}] = {}", this.sapABitacoraXs.getIdCarga(), pks.size());
			initImportes();
			for (SapAGenpolXsPK pk : pks) {
				setSapAGenpolXs(pk);
				String body = creaBody();
				LOG.debug("body: {}", body);
				archivo.agregarLineaConSalto(body);
				this.sapAGenpolXs  = null;
				this.deal          = null;
				this.persona       = null;
				this.cliente       = null;
				this.guardaReferenciaCruce();
			}
			String trailer = creaTrailer();
			LOG.debug("trailer: {}", trailer);
			archivo.agregar(trailer);
			lLotes.add(creaLoteAsMap(nombreArchivo));
			this.guardaLoteProcesado(nombreArchivo);
			this.consecutivo++;
			this.sapABitacoraXs = null;
		}
		if (!lLotes.isEmpty()) {//Para envio de correo
			model.put("lotes", lLotes);
			getMailSender().send(model);
			procesoOk = true;
		}
			
		return procesoOk && this.isProcesoDiario;
	}
	
	/**
	 * Guarda lote procesado.
	 *
	 * @param nombreArchivo the nombre archivo
	 */
	private void guardaLoteProcesado(String nombreArchivo) {
		ScLoteProcesado loteProcesado = new ScLoteProcesado();
		loteProcesado.setIdCarga(this.sapABitacoraXs.getIdCarga());
		loteProcesado.setTotCargosMxn(this.impdmxp);
		loteProcesado.setTotAbonosMxn(this.imphmxp);
		loteProcesado.setTotCargosDiv(this.impddiv);
		loteProcesado.setTotAbonosDiv(this.imphdiv);
		loteProcesado.setArchivo(nombreArchivo);
		loteProcesado.setFechaProceso(new Date());
		scLoteProcesadoDao.saveScLoteProcesado(loteProcesado);
		LOG.debug("idLoteProcesado nuevo: {}", 
				loteProcesado.getIdLoteProcesado());
	}
	
	/**
	 * Guarda referencia cruce.
	 */
	private void guardaReferenciaCruce() {
		if (this.referenciaCruce != null) {
			Date fechaCont = this.referenciaCruce.getFechaCont();
			String refCruce = this.referenciaCruce.getReferenciaCruce();
			try {
				ScReferenciaCruce ref = this.scReferenciaCruceDao.
						findScReferenciaCruceByReferencia(fechaCont, refCruce);
				LOG.debug("ref.getIdReferenciaCruce(): {}", ref.getIdReferenciaCruce());
				this.referenciaCruce = null;
			}
			catch (NoResultException ex) {
				LOG.error(ex.getMessage());
				this.scReferenciaCruceDao.saveScReferenciaCruce(referenciaCruce);
			}
			catch (NonUniqueResultException ex) {
				LOG.error("NonUniqueResultException en guardaReferenciaCruce() ", ex);
			}
		}
	}
	
	/**
	 * Crea lote as map.
	 *
	 * @param archivo the archivo
	 * @return the map
	 */
	private Map<String, Object> creaLoteAsMap(String archivo) {
		Map<String, Object> lote = new HashMap<String, Object>();
		lote.put("idLote", this.sapABitacoraXs.getIdCarga());
		lote.put("registros", this.getTotregs());
		lote.put("archivo", archivo);
		
		return lote;
	}
	
	
	
	/**
	 * Find sap A bitacora xs.
	 *
	 * @return the list
	 */
	private List<SapABitacoraXs> findSapABitacoraXs() {
		List<SapABitacoraXs> result = new ArrayList<SapABitacoraXs>();
		ScParametro scParam = null;
			Map<String, String> param = app.getParametros();
		String msg = null;
		Long loteIni = null;
		Long loteFin = null;
		LOG.info("Parametros: {}", param);
		String tipoProc = param.get("tipoProc");
		if (isNotEmpty(tipoProc)) {
			String rango = param.get("rango");
			String diario = param.get("diario");
			scParam = this.getScParametroDao().findScParametroById(tipoProc);
			if (scParam != null && diario.equals(trim(scParam.getValor()))) {
				Long idLoteProcesado = scLoteProcesadoDao.findMaxLoteProcesado();
				result = sapBitacoraXsDao.findSapABitacoraXs(idLoteProcesado);
				this.isProcesoDiario = true;
				if (result.isEmpty()) {
					LOG.info("No se encontraron registros para el lote mayor a {}.", idLoteProcesado);
				}
			}
			else if (scParam != null && rango.equals(trim(scParam.getValor()))) {
					String keyIni = param.get("loteIni");
					String keyFin = param.get("loteFin");
					ScParametro pLoteIni = this.getScParametroDao().findScParametroById(keyIni);
				if (pLoteIni == null) {
					msg = this.getMessage("msg.lote.ini.undefined");
					throw new BusinessException(msg);
				}
				ScParametro pLoteFin = this.getScParametroDao().findScParametroById(keyFin);
				if (pLoteFin == null) {
					msg = getMessage("msg.lote.fin.undefined");
					throw new BusinessException(msg);
				}
				if (!isNumeric(trim(pLoteIni.getValor()))) {
					msg = getMessage("msg.lote.ini.notnumeric", trim(pLoteIni.getValor()));
					throw new BusinessException(msg);
				}
				if (!isNumeric(trim(pLoteFin.getValor()))) {
					msg = getMessage("msg.lote.fin.notnumeric", trim(pLoteFin.getValor()));
					throw new BusinessException(msg);
				}
				loteIni = new Long(trim(pLoteIni.getValor()));
				loteFin = new Long(trim(pLoteFin.getValor()));
				result = sapBitacoraXsDao.findSapABitacoraXs(loteIni, loteFin);
				if (result.isEmpty()) {
					LOG.info("No se encontraron registros para el rango de lotes [{},{}].", 
										loteIni, loteFin);
				}		
			}
			else {
				msg = getMessage("msg.tipo.proc.undefined");
				throw new BusinessException(msg);
			}
		}
		
		return result;
	}
	
	/**
	 * Crea header.
	 *
	 * @return the string
	 */
	private String creaHeader() {
		Map<String, Map<String, Object>> header = app.getHeader();
		return creaRenglon(header);
	}
	
	/**
	 * Crea body.
	 *
	 * @return the string
	 */
	private String creaBody() {
		Map<String, Map<String, Object>> body = app.getBody();	
		return creaRenglon(body);
	}
	
	/**
	 * Crea trailer.
	 *
	 * @return the string
	 */
	private String creaTrailer() {
		Map<String, Map<String, Object>> trailer = app.getTrailer();
		
		return creaRenglon(trailer);
	}
	
	/**
	 * Crea nombre archivo.
	 *
	 * @return the string
	 */
	private String creaNombreArchivo() {
		Map<String, Map<String, Object>> nombre = app.getNombreArchivo();
		
		return creaRenglon(nombre);
	}
	
	/**
	 * Inits the importes.
	 */
	private void initImportes() {
		String cero = "0.00";
		this.impdmxp = new BigDecimal(cero);
		this.imphmxp = new BigDecimal(cero);
		this.impddiv = new BigDecimal(cero);
		this.imphdiv = new BigDecimal(cero);
	}
	
	/**
	 * Crea renglon.
	 *
	 * @param part the part
	 * @return the string
	 */
	private String creaRenglon(Map<String, Map<String, Object>> part) {
		String result = "";
		for (String field : part.keySet()) {
			String temp = "";
			Map<String, Object> env = part.get(field);
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
			Integer lon = (Integer) env.get("long");
			String type = (String) env.get("type");
			temp = defaultIfEmpty(temp, "");
			temp = trim(temp);
			Map<String, String> envFiller = app.getEnvFiller(type);
			String filler = envFiller.get("filler");
			String method = envFiller.get("method");
			temp = invokeCode(method, false, temp, filler, lon);
			if (temp.length() > lon) {
				temp = substring(temp, 0, lon);
			}
			result = result + temp;
		}
			
		return result;
	}
	
	/**
	 * Sets the sap A bitacora xs.
	 *
	 * @param bit the new sap A bitacora xs
	 */
	private void setSapABitacoraXs(SapABitacoraXs bit) {
		this.sapABitacoraXs = bit;
	}
	
	/**
	 * Sets the sap A genpol xs.
	 *
	 * @param pk the new sap A genpol xs
	 */
	private void setSapAGenpolXs(SapAGenpolXsPK pk) {
		this.sapAGenpolXs = sapGenpolXsDao.findSapAGenpolXsById(pk);
		if (this.sapAGenpolXs != null) {
			LOG.debug("Utilidad: {}", this.sapAGenpolXs.isUtilidad());
			setDeal();
		}
		else {
			LOG.error("No se encontro informaci\u00F3n para el registro en SAP_A_GENPOL_XS: {}", pk);
		}
	}
	
	/**
	 * Gets the fecon.
	 *
	 * @return the fecon
	 */
	public String getFecon() {
		String fecon = "";
		if (this.sapAGenpolXs.getFechacont() != null) {
			fecon = app.getDateAsString(this.sapAGenpolXs.getFechacont(), null);
		}
		
		return fecon; 
	}
	
	/**
	 * Gets the fecop.
	 *
	 * @param pattern the pattern
	 * @return the fecop
	 */
	public String getFecop(String pattern) {
		String fecop = "";
		if (this.sapAGenpolXs != null) {
			Date fecha = null;
			if (this.sapAGenpolXs.isUtilidad()) {
				fecha = this.sapAGenpolXs.getFechacont();
			}
			else if (this.deal != null) {
				fecha = (Date) this.deal[1];
			}
			fecop = app.getDateAsString(fecha, pattern);
		}
		
		return fecop;
	}
	
	/**
	 * Gets the fechproc.
	 *
	 * @param pattern the pattern
	 * @return the fechproc
	 */
	public String getFechproc(String pattern) {
		return app.getDateAsString(null, pattern);
	}
	
	
	/**
	 * Gets the secinterf.
	 *
	 * @param pattern the pattern
	 * @return the secinterf
	 */
	public String getSecinterf(String pattern) {
		return app.getDateAsString(null, pattern);
	}
	
	/**
	 * Invoke code.
	 *
	 * @param field the field
	 * @param agrega the agrega
	 * @param param the param
	 * @return the string
	 */
	public String invokeCode(String field, boolean agrega, Object...param) {
		String result = "";
		String metodo = field;
		try {
			if (agrega) {
				metodo = "get" + field;
			}
			Object ret = invokeMethod(this, metodo, param);
			if (ret != null) {
				result = ret.toString();
			}
		}
		catch (InvocationTargetException ex) {
			LOG.error("InvocationTargetException en invokeCode() ", ex);
		}
		catch (IllegalAccessException ex) {
			LOG.error("IllegalAccessException en invokeCode() ", ex);
		}
		catch (NoSuchMethodException ex) {
			LOG.error("NoSuchMethodException en invokeCode() ", ex);
		}
		
		return result;
	}
	
	/**
	 * Gets the numdo.
	 *
	 * @return the numdo
	 */
	public String getNumdo() {
		String numdo = "";
		if (this.sapAGenpolXs != null) { 
			numdo = String.valueOf(this.sapAGenpolXs.getSapAGenpolXsPK().getIdRegistro());
		}
		
		return numdo;
	}
	
	/**
	 * Gets the descon.
	 *
	 * @return the descon
	 */
	public String getDescon() {
		String descon = "";
		if (this.sapAGenpolXs != null) {
			descon = this.sapAGenpolXs.getTextocab();
		}
		
		return descon;
	}
	
	/**
	 * Gets the observ 1.
	 *
	 * @param param the param
	 * @return the observ 1
	 */
	public String getObserv1(Map<String, Object> param) {
		return formateaTextopos(param);
	}
	
	/**
	 * Gets the codtran.
	 *
	 * @return the codtran
	 */
	public String getCodtran() {
		String codtran = "";
		if (this.sapAGenpolXs != null) {
			codtran = this.sapAGenpolXs.getProducto();
		}
		
		return codtran;
	}
	
	/**
	 * Gets the usuario.
	 *
	 * @return the usuario
	 */
	public String getUsuario() {
		String usuario = "";
		if (this.sapAGenpolXs != null) {
			usuario = this.sapAGenpolXs.getTextocab();;
		}
		
		return usuario;
	}
	
	
	/**
	 * Sets the deal.
	 */
	private void setDeal() {
		try {
			if (this.sapAGenpolXs != null && 
					!this.sapAGenpolXs.isUtilidad() && 
					isNumeric(trim(this.sapAGenpolXs.getProducto()))) {
				BigInteger idDeal = new BigInteger(trim(this.sapAGenpolXs.getProducto()));
				this.deal = scDealDao.findSpecificInfoScDealbyId(idDeal);
				setPersona();
			}
		}
		catch (NoResultException ex) {
			LOG.error(ex.getMessage());
		}
	}
	
	/**
	 * Gets the observ.
	 *
	 * @param param the param
	 * @return the observ
	 */
	public String getObserv(Map<String, Object> param) {
		return formateaTextopos(param);
		}
		
	/**
	 * Gets the desope.
	 *
	 * @param param the param
	 * @return the desope
	 */
	public String getDesope(Map<String, Object> param) {		
		return formateaTextopos(param);
	}
	
	/**
	 * Sets the persona.
	 */
	private void setPersona() {
		try {
			String noCuenta = (String) this.deal[0];
			this.persona = 
					bupPersonaCuentaRolDao.findInfoPersonaTitularByCuenta(noCuenta);
			LOG.debug("TipoPersona[{}]: {}", this.persona[0], this.persona[1]);
			setCliente();
		}
		catch (NoResultException ex) {
			LOG.error(ex.getMessage());
		}
	}
	
	/**
	 * Sets the cliente.
	 */
	private void setCliente() {
		try {
			if (this.persona != null) {
				BigInteger idPersona = (BigInteger) this.persona[0];
				cliente = scClienteDao.findScClienteByIdPersona(idPersona);
			}
		}
		catch (NoResultException ex) {
			LOG.error(ex.getMessage());
		}
	}
	
	/**
	 * Gets the hacsbsec.
	 *
	 * @return the hacsbsec
	 */
	public String getHacsbsec() {
		String hacsbsec = "";
		if (this.persona != null && this.persona[1] != null) {
			hacsbsec = app.getSector((String) this.persona[1]);
		}
		
		return hacsbsec;
	}
	
	/**
	 * Gets the haccnsub.
	 *
	 * @return the haccnsub
	 */
	public String getHaccnsub() {
		String haccnsub = "";
		if (this.persona != null && this.persona[1] != null) {
			haccnsub = app.getSubsector((String) this.persona[1]);
		}
		
		return haccnsub;
	}
	
	/**
	 * Gets the hacdvisa.
	 *
	 * @param divisa the divisa
	 * @return the hacdvisa
	 */
	public String getHacdvisa(String divisa) {
		String hacdvisa = "";
		if (this.sapAGenpolXs != null && 
				isNotEmpty(trim(this.sapAGenpolXs.getMoneda()))) {
			hacdvisa = getDivisa();
			hacdvisa = replace(hacdvisa, divisa, "");
		}
		
		return hacdvisa;
	}
	
	/**
	 * Gets the divisa.
	 *
	 * @return the divisa
	 */
	private String getDivisa() {
		String divisa = trim(this.sapAGenpolXs.getMoneda());
		LOG.debug("this.sapAGenpolXs.getMoneda(): {}", this.sapAGenpolXs.getMoneda());
		if (app.getExcepciones().containsKey(divisa)) {
			divisa = trim(substring(this.sapAGenpolXs.getTextocab(), 7));
			divisa = app.getCveMetal(divisa);
			LOG.debug("divisa: {}", divisa);
		}
		
		return divisa;
	}
	
	/**
	 * Gets the varios.
	 *
	 * @return the varios
	 */
	public String getVarios() {
		String varios = "";
		if (this.sapAGenpolXs != null && 
				this.sapAGenpolXs.getCuentaconta() != null) {
			varios = String.valueOf(this.sapAGenpolXs.getCuentaconta());
		}
		
		return varios;
	}
	
	/**
	 * Gets the numod.
	 *
	 * @param cveConta the cve conta
	 * @return the numod
	 */
	public String getNumod(Short cveConta) {		
		return getCveContable(cveConta);
	}
	
	/**
	 * Gets the numoh.
	 *
	 * @param cveConta the cve conta
	 * @return the numoh
	 */
	public String getNumoh(Short cveConta) {
		return getCveContable(cveConta);
	}
	
	/**
	 * Gets the cve contable.
	 *
	 * @param cveConta the cve conta
	 * @return the cve contable
	 */
	private String getCveContable(Short cveConta) {
		String cveContable = "0";
		if (this.sapAGenpolXs != null && 
				this.sapAGenpolXs.getClaveconta() != null) {
			if (cveConta.equals(this.sapAGenpolXs.getClaveconta())) {
				cveContable = "1";
			}
		}
			
		return cveContable;
	}
	
	/**
	 * Gets the impd.
	 *
	 * @param cveCont the cve cont
	 * @param divMex the div mex
	 * @param metodo the metodo
	 * @return the impd
	 */
	public String getImpd(Short cveCont, String divMex, String metodo) {
		return getImporteMxn(cveCont, divMex, metodo);
	}
	
	/**
	 * Gets the imph.
	 *
	 * @param cveCont the cve cont
	 * @param divMex the div mex
	 * @param metodo the metodo
	 * @return the imph
	 */
	public String getImph(Short cveCont, String divMex, String metodo) {
		return getImporteMxn(cveCont, divMex, metodo);
	}
	
	/**
	 * Gets the impddiv.
	 *
	 * @param cveCont the cve cont
	 * @param divMex the div mex
	 * @param metodo the metodo
	 * @return the impddiv
	 */
	public String getImpddiv(Short cveCont, String divMex, String metodo) {
		return getImporteExt(cveCont, divMex, metodo);
	}
	
	/**
	 * Gets the imphdiv.
	 *
	 * @param cveCont the cve cont
	 * @param divMex the div mex
	 * @param metodo the metodo
	 * @return the imphdiv
	 */
	public String getImphdiv(Short cveCont, String divMex, String metodo) {
		return getImporteExt(cveCont, divMex, metodo);
	}
	
	/**
	 * Gets the importe mxn.
	 *
	 * @param cveCont the cve cont
	 * @param divMex the div mex
	 * @param metodo the metodo
	 * @return the importe mxn
	 */
	private String getImporteMxn(Short cveCont, String divMex, String metodo) {
		String importe = "0";
		if (this.sapAGenpolXs != null && 
				this.sapAGenpolXs.getClaveconta() != null && 
				this.sapAGenpolXs.getMoneda() != null && 
				this.sapAGenpolXs.getImporte() != null) {
			if (cveCont.equals(this.sapAGenpolXs.getClaveconta())) {
				BigDecimal cien = new BigDecimal("100");
				if (divMex.equals(trim(this.sapAGenpolXs.getMoneda()))) {
					BigDecimal bdImporte = this.sapAGenpolXs.getImporte().multiply(cien);
					importe = String.valueOf(bdImporte.longValue());
					invokeCode(metodo, false);
				}
			}
		}
		
		return importe;
	}
	
	/**
	 * Gets the importe ext.
	 *
	 * @param cveCont the cve cont
	 * @param divMex the div mex
	 * @param metodo the metodo
	 * @return the importe ext
	 */
	private String getImporteExt(Short cveCont, String divMex, String metodo) {
		String importe = "0";
		if (this.sapAGenpolXs != null && 
				this.sapAGenpolXs.getClaveconta() != null && 
				this.sapAGenpolXs.getMoneda() != null && 
				this.sapAGenpolXs.getImpmonlocal() != null) {
			if (cveCont.equals(this.sapAGenpolXs.getClaveconta())) {
				BigDecimal cien = new BigDecimal("100");
				if (!divMex.equals(trim(this.sapAGenpolXs.getMoneda()))) {
					BigDecimal bdImporte = this.sapAGenpolXs.getImporte().multiply(cien);
					importe = String.valueOf(bdImporte.longValue());
					invokeCode(metodo, false);
				}
			}
		}
		
		return importe;
	}
	
	/**
	 * Gets the cdivisa.
	 *
	 * @return the cdivisa
	 */
	public String getCdivisa() {
		String cdivisa = "";
		if (this.sapAGenpolXs != null) {
			cdivisa = getDivisa();
		}
		return cdivisa;
	}
	
	/**
	 * Gets the timestamp.
	 *
	 * @param pattern the pattern
	 * @return the timestamp
	 */
	public String getTimestamp(String pattern) {
		String timestamp = "";
		if (deal != null) {
			Date fecha = (Date) deal[1];
			timestamp = app.getDateAsString(fecha, pattern);
		}
		
		return timestamp;
	}
	
	/**
	 * Gets the numcta.
	 *
	 * @return the numcta
	 */
	public String getNumcta() {
		String numcta = "";
		if (this.deal != null) {
			numcta = (String) this.deal[0];
		}
		
		return numcta;
	}
	
	/**
	 * Gets the divlocal.
	 *
	 * @return the divlocal
	 */
	public String getDivlocal() {
		String divlocal = "";
		if (this.sapAGenpolXs != null) {
			divlocal = getDivisa();
		}
		
		return divlocal;
	}
	
	/**
	 * Gets the refcruce.
	 *
	 * @param pattern the pattern
	 * @return the refcruce
	 */
	public String getRefcruce(String pattern) {
		String refcruce = "";
		if (this.sapAGenpolXs != null && 
				this.sapAGenpolXs.isUtilidad()) {
			refcruce = app.getDateAsString(this.sapAGenpolXs.getFechacont(), pattern) + 
					"-" +
					trim(substring(this.sapAGenpolXs.getTextopos(), 17)) + 
					"-" + 
					String.valueOf(this.sapAGenpolXs.getLoteCarga());
			this.referenciaCruce = new ScReferenciaCruce();
			this.referenciaCruce.setFechaCont(this.sapAGenpolXs.getFechacont());
			this.referenciaCruce.setReferenciaCruce(refcruce);
		}
		
		return refcruce;
	}
	
	/**
	 * Gets the cecosto.
	 *
	 * @return the cecosto
	 */
	public String getCecosto() {
		String cecosto = "";
		
		return cecosto;
	}
	
	/**
	 * Gets the fechaval.
	 *
	 * @param pattern the pattern
	 * @return the fechaval
	 */
	public String getFechaval(String pattern) {
		String fechaval = "";
		if (this.sapAGenpolXs != null && 
				this.sapAGenpolXs.getFechacont() != null) {
			fechaval = app.
					getDateAsString(this.sapAGenpolXs.getFechacont(), pattern);
		}
		
		return fechaval;
	}
	
	/**
	 * Gets the cliente.
	 *
	 * @return the cliente
	 */
	public String getCliente() {
		String cliente = "";
		if (this.cliente != null) {
			cliente = this.cliente.getSic();
		}
		
		return cliente;
	}
	
	/**
	 * Gets the contrato 2.
	 *
	 * @return the contrato 2
	 */
	public String getContrato2() {
		String contrato2 = "";
		if (this.cliente != null) {
			contrato2 = this.cliente.getCuentaCheques();
		}
		
		return contrato2;
	}
	
	/**
	 * Gets the tipocamb.
	 *
	 * @param divisa the divisa
	 * @return the tipocamb
	 */
	public String getTipocamb(String divisa) {
		String tipocamb = "0";
		if (this.sapAGenpolXs != null && 
				isNotEmpty(this.sapAGenpolXs.getMoneda())) {
			if (divisa.equals(trim(this.sapAGenpolXs.getMoneda()))) {//Moneda nacional
				tipocamb = "100000";
			}
			else if (this.sapAGenpolXs.getImpmonlocal() != null && 
					this.sapAGenpolXs.getImporte() != null) { 
				BigDecimal cienmil = new BigDecimal("100000");
				BigDecimal tc = this.sapAGenpolXs.getImpmonlocal().
						divide(this.sapAGenpolXs.
								getImporte(), 5, BigDecimal.ROUND_DOWN);
				tc = tc.multiply(cienmil);
				tipocamb = String.valueOf(tc.longValue());
			}
		}
		
		return tipocamb;
	}
	
	/**
	 * Gets the saccrorigen.
	 *
	 * @param param the param
	 * @return the saccrorigen
	 */
	public String getSaccrorigen(Map<String, Object> param) {
		return getValorCampo(param);
	}
	
	/**
	 * Gets the saccrafrecta.
	 *
	 * @param param the param
	 * @return the saccrafrecta
	 */
	public String getSaccrafrecta(Map<String, Object> param) {
		return getValorCampo(param);
	}
	
	/**
	 * Gets the valor campo.
	 *
	 * @param param the param
	 * @return the valor campo
	 */
	private String getValorCampo(Map<String, Object> param) {
		String lastPart = (String) param.get("lastPart");
		Long cuentaContable = (Long) param.get("cuentaContable");
		Long cuentaContable2 = (Long) param.get("cuentaContable2");
		String uno = (String) param.get("uno");
		String dos = (String) param.get("dos");
		String fill = (String) param.get("fill");
		short size = (Short) param.get("long");
		String result = lastPart;
		if (this.sapAGenpolXs != null) {
			if (this.sapAGenpolXs.isUtilidad()) {
				result = defaultIfEmpty(this.sapAGenpolXs.getCentrobenef(), "") + 
						result;
			}
			else if (!this.sapAGenpolXs.getCuentaconta().equals(cuentaContable)  &&
					 !this.sapAGenpolXs.getCuentaconta().equals(cuentaContable2)) {
				result = dos + result;
			}
			else {
				result = uno + result;
			}
		}
		result = leftPad(result, size, fill);
		
		return result;
	}
	
	/**
	 * Formatea textopos.
	 *
	 * @param param the param
	 * @return the string
	 */
	private String formateaTextopos(Map<String, Object> param) {
		String result = "";
		short tam = (Short) param.get("tamNormal");
		//LOG.debug("formateaTextopos({})", param);
		String leyenda = "";
		if (this.sapAGenpolXs != null) {
			result = this.sapAGenpolXs.getTextopos();
			if (this.sapAGenpolXs.getCuentaconta() != null) {
				String cuentaConta = String.
						valueOf(this.sapAGenpolXs.getCuentaconta());
				String centroCostos = this.sapAGenpolXs.getCentrocostos();
				String centroBenef = this.sapAGenpolXs.getCentrobenef();
				if (startsWith(cuentaConta, (String) param.get("startCostos"))) {
					if (isEmpty(centroCostos)) {
						tam = (Short) param.get("tamFormat");
					}
				}
				else if (startsWith(cuentaConta, (String) param.get("startBenef"))) {
					if (isEmpty(centroBenef)) {
						tam = (Short) param.get("tamFormat");
						leyenda = (String) param.get("leyenda");
					}
				}
				result = defaultString(trim(substring(result, 0, tam)));
				result = trim(result + " " + leyenda);
				if (isNotEmpty(this.sapAGenpolXs.getSapAGenpolXsPK().getReferencia())) {
					result = trim(this.sapAGenpolXs.getSapAGenpolXsPK().getReferencia()) + 
							" " + result;
				}
			}
		}
		
		return result;
	}
	
	/**
	 * Gets the totregs.
	 *
	 * @return the totregs
	 */
	public String getTotregs() {
		String totRegs = "0";
		if (this.sapABitacoraXs.getRegaprocesar() != null) {
			totRegs = String.valueOf(this.sapABitacoraXs.getRegaprocesar());
		}
		
		return totRegs;
	}
	
	/**
	 * Gets the numtd.
	 *
	 * @return the numtd
	 */
	public String getNumtd() {
		String numtd = "0";
		if (this.sapABitacoraXs.getTotRegCargos() != null) {
			numtd = String.valueOf(this.sapABitacoraXs.getTotRegCargos());
		}
		
		return numtd;
	}
	
	/**
	 * Gets the numth.
	 *
	 * @return the numth
	 */
	public String getNumth() {
		String numth = "0";
		if (this.sapABitacoraXs.getTotRegAbonos() != null) {
			numth = String.valueOf(this.sapABitacoraXs.getTotRegAbonos());
		}
		
		return numth;
	}
	
	/**
	 * Gets the impdmxp.
	 *
	 * @param mult the mult
	 * @return the impdmxp
	 */
	public String getImpdmxp(BigDecimal mult) {
		String impdmxp = "0";
		if (this.impdmxp != null) {
			BigDecimal importe = this.impdmxp.multiply(mult);
			impdmxp = String.valueOf(importe.longValue());
		}
		
		return impdmxp;
	}
	
	/**
	 * Gets the imphmxp.
	 *
	 * @param mult the mult
	 * @return the imphmxp
	 */
	public String getImphmxp(BigDecimal mult) {
		String imphmxp = "0";
		if (this.imphmxp != null) {
			BigDecimal importe = this.imphmxp.multiply(mult);
			imphmxp = String.valueOf(importe.longValue());
		}
		
		return imphmxp;
	}
	
	/**
	 * Gets the impddiv.
	 *
	 * @param mult the mult
	 * @return the impddiv
	 */
	public String getImpddiv(BigDecimal mult) {
		String impddiv = "0";
		if (this.impddiv != null) {
			BigDecimal importe = this.impddiv.multiply(mult);
			impddiv = String.valueOf(importe.longValue());
		}
		
		return impddiv;
	}
	
	/**
	 * Gets the imphdiv.
	 *
	 * @param mult the mult
	 * @return the imphdiv
	 */
	public String getImphdiv(BigDecimal mult) {
		String imphdiv = "0";
		if (this.imphdiv != null) {
			BigDecimal importe = this.imphdiv.multiply(mult);
			imphdiv = String.valueOf(importe.longValue());
		}
		
		return imphdiv;
	}
	
	/**
	 * Gets the consecutivo.
	 *
	 * @return the consecutivo
	 */
	private String getConsecutivo() {
		String consecutivo = "0";
		if (this.consecutivo != null) {
			consecutivo = String.valueOf(this.consecutivo);
		}
		consecutivo = leftPad(consecutivo, 3, "0") + ".";
		
		return consecutivo;
	}
	
	/**
	 * Gets the fecha archivo.
	 *
	 * @param pattern the pattern
	 * @return the fecha archivo
	 */
	private String getFechaArchivo(String pattern) {
		String fechaArchivo = "D";
		Date fecha = addDays(this.getFechaSistema(), -1);
		fechaArchivo = fechaArchivo + 
				app.getDateAsString(fecha, pattern);
		
		return fechaArchivo;
	}
	
	/**
	 * Gets the nominterf.
	 *
	 * @param prefijo the prefijo
	 * @param pattern the pattern
	 * @param preReproceso the preReproceso
	 * @return the nominterf
	 */
	public String getNominterf(String prefijo, String pattern, String preReproceso) {
		String nominterf = "";
		if (this.isProcesoDiario()) {
			nominterf = prefijo;
		}
		else {
			nominterf = preReproceso;
		}
		nominterf = nominterf + getConsecutivo() + getFechaArchivo(pattern);
		
		return nominterf;
	}
	
	/**
	 * Incrementa impdmxp.
	 */
	public void incrementaImpdmxp() {
		this.impdmxp = this.impdmxp.add(this.sapAGenpolXs.getImporte());
	}
	
	/**
	 * Incrementa imphmxp.
	 */
	public void incrementaImphmxp() {
		this.imphmxp = this.imphmxp.add(this.sapAGenpolXs.getImporte());
	}
	
	/**
	 * Incrementa impddiv.
	 */
	public void incrementaImpddiv() {
		this.impddiv = this.impddiv.add(this.sapAGenpolXs.getImpmonlocal());
	}
	
	/**
	 * Incrementa imphdiv.
	 */
	public void incrementaImphdiv() {
		this.imphdiv = this.imphdiv.add(this.sapAGenpolXs.getImpmonlocal());
	}
	
	/**
	 * Right.
	 *
	 * @param value the value
	 * @param filler the filler
	 * @param lon the lon
	 * @return the string
	 */
	public String right(String value, String filler, Integer lon) {
		String rightpad = rightPad(value, lon, filler);
		rightpad = tuncString(rightpad, lon);
		
		return rightpad;
	}
	
	/**
	 * Left.
	 *
	 * @param value the value
	 * @param filler the filler
	 * @param lon the lon
	 * @return the string
	 */
	public String left(String value, String filler, Integer lon) {
		String leftpad = leftPad(value, lon, filler);
		leftpad = tuncString(leftpad, lon);
		
		return leftpad;
	}
	
	/**
	 * Tunc string.
	 *
	 * @param value the value
	 * @param lon the lon
	 * @return the string
	 */
	private String tuncString(String value, Integer lon) {
		if (value.length() > lon) {
			value = substring(value, 0, lon);
		}
		
		return value;
	}
	
	/**
	 * Crea archivo bandera.
	 */
	@Override
	public void creaArchivoBandera() {
		Map<String, Map<String, Object>> nombreEnv = app.getNombreArchivoBandera();
		String dirArchivo = app.getDirArchivoBandera();
		String nombreArchivo = creaRenglon(nombreEnv);
		Map<String, Map<String, Object>> headerEnv = app.getHeaderArchivoBandera();
		String header = creaRenglon(headerEnv);
		Archivo archivo = new Archivo(dirArchivo, nombreArchivo, true);
		archivo.agregar(header);
		File content = archivo.getContent();
		LOG.info("Se creo el archivo bandera {}.", content.getAbsolutePath());
	}
	
	/**
	 * Gets the fecha archivo bandera.
	 *
	 * @param params the params
	 * @return the fecha archivo bandera
	 */
	public String getFechaArchivoBandera(Map<String, String> params) {
		String result = "";
		String separator = params.get("separator");
		String timeSystem = params.get("timeSystem");
		String dateFilePattern = params.get("dateFilePattern");
		String timeFilePattern = params.get("timeFilePattern");
		if (isEmpty(timeSystem)) {
			timeSystem = this.app.getDateAsString(null, timeFilePattern);
		}
		result = this.app.getDateAsString(this.getFechaSistema(), dateFilePattern) + 
				separator + timeSystem;
				
		return result;		
	}

	/**
	 * Checks if is proceso diario.
	 *
	 * @return true, if is proceso diario
	 */
	public boolean isProcesoDiario() {
		return isProcesoDiario;
	}
	
}
