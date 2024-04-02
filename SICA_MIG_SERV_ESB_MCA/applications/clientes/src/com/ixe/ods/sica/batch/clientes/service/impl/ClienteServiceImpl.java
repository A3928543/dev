package com.ixe.ods.sica.batch.clientes.service.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;

import com.ixe.ods.sica.batch.clientes.dao.BitacoraDao;
import com.ixe.ods.sica.batch.clientes.dao.ClienteDao;
import com.ixe.ods.sica.batch.clientes.dao.CuentaAltamiraDao;
import com.ixe.ods.sica.batch.clientes.mail.MailSender;
import com.ixe.ods.sica.batch.clientes.parser.ParserFile;
import com.ixe.ods.sica.batch.clientes.parser.error.ParserFileException;
import com.ixe.ods.sica.batch.clientes.parser.error.ParserRowException;
import com.ixe.ods.sica.batch.clientes.parser.util.ParserConfig;
import com.ixe.ods.sica.batch.clientes.service.ClienteService;
import com.ixe.ods.sica.batch.clientes.util.FiltroArchivo;

import static org.apache.commons.lang.StringUtils.isNotEmpty;

/**
 * The Class ClienteServiceImpl.
 */
public class ClienteServiceImpl implements ClienteService {
	
	/** The Constant LOG. */
	private static final Logger LOG = Logger.getLogger(ClienteServiceImpl.class);
	
	/** The cliente dao. */
	private ClienteDao clienteDao;
	
	/** The cuenta altamira dao. */
	private CuentaAltamiraDao cuentaAltamiraDao;
	
	/** The bitacora dao. */
	private BitacoraDao bitacoraDao;
	
	/** The dir procesa. */
	private String dirProcesa;
	
	/** The dir depura. */
	private String dirDepura;
	
	/** The tipo archivo. */
	private String tipoArchivo;
	
	/** The nombre archivo valido. */
	private String nombreArchivoValido;
	
	/** The parser config. */
	private ParserConfig parserConfig;
	
	/** The id usuario mod. */
	private Long idUsuarioMod;
	
	/** The cve usuario mod. */
	private String cveUsuarioMod;
	
	/** The mail sender. */
	private MailSender mailSender;
	
	/** The arch errores. */
	private File archErrores;
	
	/** The nombre archivo errores. */
	private String nombreArchivoErrores;
	
	/** The dias historico. */
	private Integer diasHistorico;

	/**
	 * Instantiates a new cliente service impl.
	 */
	public ClienteServiceImpl() {
	}
	
	/**
	 * Procesa archivos.
	 */
	public void procesaArchivos() {
		File dir = new File(dirProcesa);
		if (dir.isDirectory()) {
			FiltroArchivo filtro = 
					new FiltroArchivo(tipoArchivo, nombreArchivoValido);
			File[] archivos = dir.listFiles(filtro);
			if (archivos.length > 0) {
				List<Map<String, Object>> lArchivos = 
						new ArrayList<Map<String, Object>>();
				Map<String, Object> model = new HashMap<String, Object>();
				int errTotal = 0;
				creaArchivoErrores();
				for (int i = 0; i < archivos.length; i++) {
					try {
						File archivo = archivos[i];
						ParserFile parserFile = new ParserFile(archivo, parserConfig);
						int erroneos = 0;
						int correctos = 0;
						while (parserFile.hasMoreRows()) {
							try {
								Map<String, String> row = 
										parserFile.nextRow();
								int clientesAct = actualizaCliente(row);
								int cuentasAct = actualizaCuenta(row);
								String sic = row.get("NUMCLREF");
								imprimeErrores(clientesAct, cuentasAct, sic);
								registraBitacora(row, clientesAct, cuentasAct);
								correctos++;
							}
							catch (ParserRowException ex) {
								LOG.error("ParserRowException ", ex);
								agregaError(ex.getMessage());
								erroneos++;
							}
						}
						Map<String, Object> mArchivo = 
								new HashMap<String, Object>();
						mArchivo.put("nombre", archivo.getName());
						mArchivo.put("correctos", correctos);
						errTotal = errTotal + erroneos;
						mArchivo.put("erroneos", erroneos);
						mArchivo.put("total", parserFile.getTotalRows());
						mArchivo.put("archivoProc", archivo);
						lArchivos.add(mArchivo);
					}
					catch (ParserFileException ex) {
						LOG.error("ParserFileException ", ex);
					}
				}
				if (!lArchivos.isEmpty()) {
					model.put("archivos", lArchivos);
					Boolean isErrores = false;
					if (errTotal > 0 && archErrores != null) { 
						isErrores = true;
						model.put("nameAttachment", archErrores.getName());
						model.put("attachment", archErrores);
					}
					model.put("isErrores", isErrores);
					moverArchivos(lArchivos);
					mailSender.send(model);
				}
			}
			else {
				LOG.error("No existen archivos listos para realizar procesamiento.");
			}
		}
		else {
			LOG.error("La carpeta no existe en el sistema de archivos del servidor.");
		}
	}
	
	/**
	 * Depura historico archivos procesados.
	 */
	public void depuraHistoricoArchivosProcesados() {
		File dir = new File(dirDepura);
		if (dir.isDirectory()) {
			FiltroArchivo filtro = new FiltroArchivo(tipoArchivo, 
					                                 nombreArchivoValido, 
					                                 true, 
					                                 nombreArchivoErrores, 
					                                 diasHistorico);
			File[] archivos = dir.listFiles(filtro);
			for (File archivo:archivos) {
				LOG.debug("Archivo a eliminar: " + archivo.getName());
				archivo.delete();
			}
		}
	}
	
	/**
	 * Imprime errores.
	 *
	 * @param clientes the clientes
	 * @param cuentas the cuentas
	 * @param sic the sic
	 */
	private void imprimeErrores(int clientes, int cuentas, String sic) {
		if (LOG.isInfoEnabled()) {
			LOG.info("clientes: " + clientes);
			LOG.info("cuentas: " + cuentas);
		}
		if (clientes == 0 ) {
			LOG.error("No se encontraron clientes registrados " +
							"identificador (SIC): " + sic);
		}
		if (cuentas == 0) {
			LOG.error("No se encontraron cuentas registradas para " +
							"el cliente con identificador (SIC): " + sic);
		}
	}

	
	/**
	 * Actualiza cliente.
	 *
	 * @param datosCliente the datos cliente
	 * @return the int
	 */
	private int actualizaCliente(Map<String, String> datosCliente) {
		int result = 0;
		
		String sic = datosCliente.get("NUMCLREF");
		List<Map<String, Object>> clientes = 
				getClienteDao().getClientes(sic);
		for(Map<String, Object> cliente: clientes) {
			String newSic = datosCliente.get("NUMCLIEN");
			cliente.put("sic", newSic);
			String sucursal = datosCliente.get("SUCURSALCLIENTE");
			cliente.put("sucursalOri", sucursal);
			cliente.put("sicursalOper", sucursal);
			cliente.put("usuarioUltMod", idUsuarioMod);
			cliente.put("fechaUltMod", new Date());
			result = result + getClienteDao().actualizaCliente(cliente);
		}
		
		return result;
	}
	
	/**
	 * Actualiza cuenta.
	 *
	 * @param datosCuenta the datos cuenta
	 * @return the int
	 */
	private int actualizaCuenta(Map<String, String> datosCuenta) {
		int result = 0;
		
		Long sic = new Long(datosCuenta.get("NUMCLREF"));
		List<Map<String, Object>> cuentas = 
				cuentaAltamiraDao.getCuentas(sic);
		for (Map<String, Object> cuenta : cuentas) {
			cuenta.put("sic", new Long(datosCuenta.get("NUMCLIEN")));
			cuenta.put("cr", datosCuenta.get("SUCURSALCLIENTE"));
			cuenta.put("usuario", cveUsuarioMod);
			cuenta.put("fechaUltMod", new Date());
			result = result + cuentaAltamiraDao.actualizaCuenta(cuenta);
		}
		
		return result;
	}
	
	/**
	 * Mover archivos.
	 *
	 * @param archivos the archivos
	 */
	private void moverArchivos(List<Map<String, Object>> archivos) {
		for (Map<String, Object> archivo:archivos) {
			String llave = "archivoProc";
			File archivoProc = (File) archivo.get(llave);
			archivo.remove(llave);
			moverArchivo(archivoProc);
		}
	}
	
    /**
     * Mover archivo.
     *
     * @param archOrigen the arch origen
     */
    private void moverArchivo(File archOrigen){
    	InputStream in = null;
    	OutputStream out = null;
    	if (archOrigen != null && isNotEmpty(dirDepura)) {
    		LOG.debug("moverArchivo: " + archOrigen);
    		String nombreArchOrigen = archOrigen.getName();
    		File archDestino = new File(dirDepura + nombreArchOrigen);
    		try {
    			in = new FileInputStream(archOrigen);
    			out = new FileOutputStream(archDestino);
    			byte[] buf = new byte[1024];
    			int len;
    			while ((len = in.read(buf)) > 0) {
    				out.write(buf, 0, len);
    			}
    		}
    		catch (IOException ex) {
    			LOG.error("IOException ", ex);
    		}
    		finally {
    			try {
	    			if (in != null) {
	    				in.close();
	    			}
	    			if (out != null) {
	    				out.close();
	    			}
	    			archOrigen.delete();
    			}
    			catch (IOException ex) {
    				LOG.error("IOException ", ex);
    			}
    		}
    	}
    }
	
	/**
	 * Agrega error.
	 *
	 * @param error the error
	 */
	private void agregaError(String error) {
		String lineSeparator = System.getProperty("line.separator");
		Writer writer = null;
    	try {
    		if (archErrores != null) {
    			writer = new BufferedWriter(new FileWriter(archErrores, true));
    			writer.write(error + lineSeparator);
    		}
    	}
    	catch (IOException ex) {
    		LOG.error("IOException ", ex);
    	}
    	finally {
    		try {
        		if (writer != null) {
    				writer.close();
    				writer = null;
    			}
    		}
    		catch (IOException ex) {
    			LOG.error("IOException ", ex);
    		}
    	}
    }
	
	/**
	 * Crea archivo errores.
	 */
	private void creaArchivoErrores() {
		DateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String pathname = dirDepura + nombreArchivoErrores + 
				sdf.format(new Date()) + ".txt";
		archErrores = new File(pathname);
	}
	
	/**
	 * Registra bitacora.
	 *
	 * @param datos the datos
	 */
	private void registraBitacora(Map<String, String> datos, int clientesAct, int cuentasAct) {
		Map<String, Object> bitacora = new HashMap<String, Object>();
		bitacora.putAll(datos);
		bitacora.put("FECHAALTA", new Date());
		bitacora.put("USUARIO", getCveUsuarioMod());
		try {
			/* Solo se registran en bitacora clientes o cuentas encontradas en SICA */
			if (clientesAct > 0 || cuentasAct > 0) {
				int registros = bitacoraDao.registrarBitacora(bitacora);
				if (LOG.isDebugEnabled()) {
					LOG.debug("Registros en bitacora: " + registros);
				}
			}
		}
		catch (DataAccessException ex) {
			LOG.error("DataAccessException ", ex);
		}
	}

	/**
	 * Gets the cliente dao.
	 *
	 * @return the cliente dao
	 */
	public ClienteDao getClienteDao() {
		return clienteDao;
	}

	/**
	 * Sets the cliente dao.
	 *
	 * @param clienteDao the new cliente dao
	 */
	public void setClienteDao(ClienteDao clienteDao) {
		this.clienteDao = clienteDao;
	}

	/**
	 * Gets the cuenta altamira dao.
	 *
	 * @return the cuenta altamira dao
	 */
	public CuentaAltamiraDao getCuentaAltamiraDao() {
		return cuentaAltamiraDao;
	}

	/**
	 * Sets the cuenta altamira dao.
	 *
	 * @param cuentaAltamiraDao the new cuenta altamira dao
	 */
	public void setCuentaAltamiraDao(CuentaAltamiraDao cuentaAltamiraDao) {
		this.cuentaAltamiraDao = cuentaAltamiraDao;
	}

	/**
	 * Gets the dir procesa.
	 *
	 * @return the dir procesa
	 */
	public String getDirProcesa() {
		return dirProcesa;
	}

	/**
	 * Sets the dir procesa.
	 *
	 * @param dirProcesa the new dir procesa
	 */
	public void setDirProcesa(String dirProcesa) {
		this.dirProcesa = dirProcesa;
	}

	/**
	 * Gets the dir depura.
	 *
	 * @return the dir depura
	 */
	public String getDirDepura() {
		return dirDepura;
	}

	/**
	 * Sets the dir depura.
	 *
	 * @param dirDepura the new dir depura
	 */
	public void setDirDepura(String dirDepura) {
		this.dirDepura = dirDepura;
	}

	/**
	 * Gets the tipo archivo.
	 *
	 * @return the tipo archivo
	 */
	public String getTipoArchivo() {
		return tipoArchivo;
	}

	/**
	 * Sets the tipo archivo.
	 *
	 * @param tipoArchivo the new tipo archivo
	 */
	public void setTipoArchivo(String tipoArchivo) {
		this.tipoArchivo = tipoArchivo;
	}

	/**
	 * Gets the nombre archivo valido.
	 *
	 * @return the nombre archivo valido
	 */
	public String getNombreArchivoValido() {
		return nombreArchivoValido;
	}

	/**
	 * Sets the nombre archivo valido.
	 *
	 * @param nombreArchivoValido the new nombre archivo valido
	 */
	public void setNombreArchivoValido(String nombreArchivoValido) {
		this.nombreArchivoValido = nombreArchivoValido;
	}

	/**
	 * Gets the parser config.
	 *
	 * @return the parser config
	 */
	public ParserConfig getParserConfig() {
		return parserConfig;
	}

	/**
	 * Sets the parser config.
	 *
	 * @param parserConfig the new parser config
	 */
	public void setParserConfig(ParserConfig parserConfig) {
		this.parserConfig = parserConfig;
	}

	/**
	 * Gets the id usuario mod.
	 *
	 * @return the id usuario mod
	 */
	public Long getIdUsuarioMod() {
		return idUsuarioMod;
	}

	/**
	 * Sets the id usuario mod.
	 *
	 * @param idUsuarioMod the new id usuario mod
	 */
	public void setIdUsuarioMod(Long idUsuarioMod) {
		this.idUsuarioMod = idUsuarioMod;
	}

	/**
	 * Gets the cve usuario mod.
	 *
	 * @return the cve usuario mod
	 */
	public String getCveUsuarioMod() {
		return cveUsuarioMod;
	}

	/**
	 * Sets the cve usuario mod.
	 *
	 * @param cveUsuarioMod the new cve usuario mod
	 */
	public void setCveUsuarioMod(String cveUsuarioMod) {
		this.cveUsuarioMod = cveUsuarioMod;
	}

	/**
	 * Gets the mail sender.
	 *
	 * @return the mail sender
	 */
	public MailSender getMailSender() {
		return mailSender;
	}

	/**
	 * Sets the mail sender.
	 *
	 * @param mailSender the new mail sender
	 */
	public void setMailSender(MailSender mailSender) {
		this.mailSender = mailSender;
	}

	/**
	 * Gets the nombre archivo errores.
	 *
	 * @return the nombre archivo errores
	 */
	public String getNombreArchivoErrores() {
		return nombreArchivoErrores;
	}

	/**
	 * Sets the nombre archivo errores.
	 *
	 * @param nombreArchivoErrores the new nombre archivo errores
	 */
	public void setNombreArchivoErrores(String nombreArchivoErrores) {
		this.nombreArchivoErrores = nombreArchivoErrores;
	}

	/**
	 * Gets the bitacora dao.
	 *
	 * @return the bitacora dao
	 */
	public BitacoraDao getBitacoraDao() {
		return bitacoraDao;
	}

	/**
	 * Sets the bitacora dao.
	 *
	 * @param bitacoraDao the new bitacora dao
	 */
	public void setBitacoraDao(BitacoraDao bitacoraDao) {
		this.bitacoraDao = bitacoraDao;
	}

	/**
	 * Gets the dias historico.
	 *
	 * @return the dias historico
	 */
	public Integer getDiasHistorico() {
		return diasHistorico;
	}

	/**
	 * Sets the dias historico.
	 *
	 * @param diasHistorico the new dias historico
	 */
	public void setDiasHistorico(Integer diasHistorico) {
		this.diasHistorico = diasHistorico;
	}

}
