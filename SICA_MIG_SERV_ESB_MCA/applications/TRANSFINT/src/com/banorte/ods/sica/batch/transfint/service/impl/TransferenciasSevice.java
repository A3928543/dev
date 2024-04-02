package com.banorte.ods.sica.batch.transfint.service.impl;


import com.banorte.ods.sica.batch.transfint.WriteExcel;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import com.banorte.ods.sica.batch.transfint.dao.ScReporteTransfintDao;
import com.banorte.ods.sica.batch.transfint.dao.TesDetalleLiquidacionDao;
import com.banorte.ods.sica.batch.transfint.dao.ScSpCargaDao;
import com.banorte.ods.sica.batch.transfint.dao.TesLiquidacionDao;
import com.banorte.ods.sica.batch.transfint.domain.ScDetalleRepTransfint;
import com.banorte.ods.sica.batch.transfint.domain.ScParametro;
import com.banorte.ods.sica.batch.transfint.domain.ScReporteTransfint;
import com.banorte.ods.sica.batch.transfint.domain.TesDetalleLiquidacion;
import com.banorte.ods.sica.batch.transfint.domain.TesLiquidacion;
import com.banorte.ods.sica.batch.transfint.error.BusinessException;
import com.banorte.ods.sica.batch.transfint.error.SystemErrorException;
import com.banorte.ods.sica.batch.transfint.util.Archivo;

import static org.apache.commons.lang.StringUtils.replace;
import static org.springframework.transaction.annotation.Isolation.DEFAULT;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;
import static com.banorte.ods.sica.batch.transfint.util.Constantes.FASE_CARGA_TERMINADA;
import static com.banorte.ods.sica.batch.transfint.util.Constantes.FASE_ARCHIVO_GERENERADO;
import static com.banorte.ods.sica.batch.transfint.util.Constantes.RUTA_ARCHIVO_COPIA;
import static com.banorte.ods.sica.batch.transfint.util.Constantes.CORREOS_COPIA_TRANSFINT;

/**
 * The Class TransferenciasSevice.
 */
public class TransferenciasSevice extends BaseService {
	
	/** The Constant LOG. */
	private static final Logger LOG = LoggerFactory.getLogger(TransferenciasSevice.class);
	
	/** The rep transfint ruta archivos. */
	@Value("${param.rep.transfint.ruta.archivos}")
	private String repTransfintRutaArchivos;
	
	/** The rep transfint separador. */
	@Value("${param.rep.transfint.separador}")
	private String repTransfintSeparador;
	
	/** The rep mail titulo. */
	@Value("${mail.rep.transfint.titulo}")
	private String repMailTitulo;
	
	/** The carga dao. */
	@Autowired
	private ScSpCargaDao cargaDao;
	
	/** The reporte transfint dao. */
	@Autowired
	private ScReporteTransfintDao reporteTransfintDao;
	
	/** The tes detalle liquidacion dao. */
	@Autowired
	private TesDetalleLiquidacionDao tesDetalleLiquidacionDao;
	
	/** The tes detalle liquidacion dao. */
	@Autowired
	private TesLiquidacionDao tesLiquidacionDao;

	public WritableWorkbook workbook;
	
	/**
	 * Instantiates a new transferencias sevice.
	 */
	public TransferenciasSevice() {
	}
	
	/**
	 * Carga transferencias.
	 *
	 * @param fechaIni the fecha ini
	 * @param fechaFin the fecha fin
	 * @param tipoReporte the tipo reporte
	 * @return the long
	 */
	Long cargaTransferencias(Date fechaIni, Date fechaFin, Short tipoReporte) {
		Map<String, Object> out = this.cargaDao.ejecutaCarga(fechaIni, fechaFin, tipoReporte);
		LOG.debug("Resultado de la carga: {}", out);
		Integer codigo = (Integer) out.get("p_codigo");
		String msg = "";
		BigDecimal id = null;
		if (codigo == 0) {
			id = (BigDecimal) out.get("p_id_reporte");
		}
		else {
			msg = (String) out.get("p_mensaje");
			throw new SystemErrorException(msg);
		}		
		
		return id.longValue();
	}
	
	/**
	 * Genera reporte.
	 *
	 * @param fechaIni the fecha ini
	 * @param fechaFin the fecha fin
	 * @param tipoReporte the tipo reporte
	 * @param nombre the nombre
	 * @return the archivo
	 */
	@Transactional(propagation = REQUIRED, isolation = DEFAULT, readOnly = false)
	public Archivo generaReporte(Date fechaIni, Date fechaFin, Short tipoReporte, String nombre) {
		Archivo archivo = null;
		LOG.debug("Rango de fechas: [{}, {}]", fechaIni, fechaFin);
		Long idReporte = this.cargaTransferencias(fechaIni, fechaFin, tipoReporte);
		ScReporteTransfint reporte = this.reporteTransfintDao.findById(idReporte);
		LOG.debug("reporte encontrado: {}", reporte);
		if (reporte != null) {
			this.actualizaScReporteTransfint(reporte, FASE_CARGA_TERMINADA);
			ScParametro ruta = this.getScParametroDao().findById(this.repTransfintRutaArchivos);
			if (ruta != null) {
				archivo = new Archivo(ruta.getValor(), nombre, true);
				if (reporte.getDetalles() != null && 
						!reporte.getDetalles().isEmpty()) {
					for (ScDetalleRepTransfint detalle : reporte.getDetalles()) {
						String content = detalle.toString();
						content = replace(content, "|", this.repTransfintSeparador);
						archivo.agregarLineaConSalto(content);
					}
					LOG.debug("Archivo generado: {}", archivo.getContent().getAbsolutePath());
				}
				reporte.setNombreArchivo(nombre);
				this.enviaCorreo(reporte);
				this.actualizaScReporteTransfint(reporte, FASE_ARCHIVO_GERENERADO);
			}
			else {
				throw new BusinessException(this.getMessage("msg.env.param.transfint.empty"));
			}
		}
		
		return archivo;
	}
	
	/**
	 * Genera reporte.
	 *
	 * @param fechaIni the fecha ini
	 * @param fechaFin the fecha fin
	 * @param tipoReporte the tipo reporte
	 * @param nombre the nombre
	 * @return the archivo
	 * @throws IOException 
	 */
	@Transactional(propagation = REQUIRED, isolation = DEFAULT, readOnly = false)
	public Archivo generaCopiaReporte(Date fechaIni, Date fechaFin, Short tipoReporte, String nombre) {
		Archivo archivo = null;
		int contadorColumnas = 41;
		int contadorRenglones = 0;
		String content;
		
		File file = new File("" + RUTA_ARCHIVO_COPIA + nombre);
		
	    WorkbookSettings wbSettings = new WorkbookSettings();

	    wbSettings.setLocale(new Locale("en", "EN"));
	    try {
			workbook = Workbook.createWorkbook(file, wbSettings);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	    workbook.createSheet("Reporte TANSFINT", 0);
	    
	
		LOG.debug("Rango de fechas: [{}, {}]", fechaIni, fechaFin);
		Long idReporte = this.cargaTransferencias(fechaIni, fechaFin, tipoReporte);
		ScReporteTransfint reporte = this.reporteTransfintDao.findById(idReporte);
		LOG.debug("reporte encontrado: {}", reporte);

		if (reporte != null) {
			this.actualizaScReporteTransfint(reporte, FASE_CARGA_TERMINADA);
			ScParametro ruta = this.getScParametroDao().findById(this.repTransfintRutaArchivos);
			ScParametro correosCopia = this.getScParametroDao().findById(CORREOS_COPIA_TRANSFINT);
			if (ruta != null) {

			    WriteExcel generarExcel = new WriteExcel();
			    WritableSheet excelSheet = workbook.getSheet(0);
			    try {
					generarExcel.createLabel(excelSheet);
				} catch (WriteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
					
				if (reporte.getDetalles() != null && 
						!reporte.getDetalles().isEmpty()) {
					for (ScDetalleRepTransfint detalle : reporte.getDetalles()) {
						content = detalle.toStringCopia();
						content = replace(content, "|", this.repTransfintSeparador);
						contadorRenglones = contadorRenglones + 1;
											
						String[] arrayCampos = content.split(";");
						
						LOG.debug("arrayCampos[arrayCampos.length]" + arrayCampos.length);
						
						
						TesDetalleLiquidacion detalleLiq = this.tesDetalleLiquidacionDao.findById(Long.parseLong(arrayCampos[arrayCampos.length - 1]));
						LOG.debug("El ID_Detalle_Liquidacion es: " + detalleLiq.getIdDetalleLiquidacion());
						LOG.debug("El ID_Liquidacion es: " + detalleLiq.getIdLiquidacion());
						TesLiquidacion liquidacion = this.tesLiquidacionDao.findById(detalleLiq.getIdLiquidacion());
						LOG.debug("El NUMERO DE DEAL es: " + liquidacion.getNumeroOrden());
						LOG.debug("Detalle Liquidación: "+ arrayCampos[arrayCampos.length - 1] + " se remplaza por numero de deal: " + arrayCampos[arrayCampos.length - 1].replace(arrayCampos[arrayCampos.length - 1],Long.toString(liquidacion.getNumeroOrden())));
						content = content.replace(arrayCampos[arrayCampos.length - 1],arrayCampos[arrayCampos.length - 1].replace(arrayCampos[arrayCampos.length - 1],Long.toString(liquidacion.getNumeroOrden())));
						//archivo.agregarLineaConSalto(content);

					    try {
							generarExcel.write(content,contadorColumnas,contadorRenglones,file,workbook,excelSheet);
							generarExcel.sheetAutoFitColumns(excelSheet);

						} catch (WriteException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						
					}

					try {
						workbook.write();
						workbook.close();
					} catch (WriteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				    
				}
				
				reporte.setNombreArchivo(nombre);
				this.enviaCorreoCopia(reporte,file,correosCopia.getValor());
				//this.actualizaScReporteTransfint(reporte, FASE_ARCHIVO_GERENERADO);
			}
			else {
				throw new BusinessException(this.getMessage("msg.env.param.transfint.empty"));
			}
		}
		
		return archivo;
	}

	
	public WritableWorkbook getWorkbook() {
		return workbook;
	}

	public void setWorkbook(WritableWorkbook workbook) {
		this.workbook = workbook;
	}

	/**
	 * Envia correo.
	 *
	 * @param reporte the reporte
	 */
	private void enviaCorreo(ScReporteTransfint reporte) {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("nombreArchivo", reporte.getNombreArchivo());
		model.put("numRegistros", reporte.getNumOperaciones());
		model.put("titulo", this.repMailTitulo);
		
		this.getMailSender().send(model);
	}
	
	/**
	 * Envia correo de la copia del TRANSFINT.
	 *
	 * @param reporte the reporte
	 */
	private void enviaCorreoCopia(ScReporteTransfint reporte,File file,String correosCopia) {
		//Archivo archivo = new Archivo("archivos/Errores/", "SICA_TR_DIARIO_20200724.xls", true);
		//archivo.agregar("Envio TRANSFINT");
		
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("nombreArchivo", reporte.getNombreArchivo());
		model.put("numRegistros", reporte.getNumOperaciones());
		model.put("titulo", this.repMailTitulo);
		model.put("nameAttachment", reporte.getNombreArchivo());
		model.put("attachment", file);
		
		this.getMailSender().send(model,correosCopia);
	}
	
	/**
	 * Actualiza sc reporte transfint.
	 *
	 * @param reporte the reporte
	 * @param archivo the archivo
	 * @param fase the fase
	 */
	private void actualizaScReporteTransfint (ScReporteTransfint reporte, Short fase) {
		if (reporte != null) {
			reporte.setFechaUltMod(new Date());
			reporte.setFase(fase);
			LOG.debug("Actualizando reporte: {}", reporte);
			this.reporteTransfintDao.update(reporte);
			LOG.debug("Reporte actualizado.");
		}
	}

}
