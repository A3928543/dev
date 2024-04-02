package com.banorte.reporte.diario.transferencias.services.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.*;

import org.apache.log4j.Logger;

import com.banorte.reporte.diario.transferencias.dao.IConsultaDao;
import com.banorte.reporte.diario.transferencias.dto.DatosTransferencia;
import com.banorte.reporte.diario.transferencias.dto.MensajesVo;
import com.banorte.reporte.diario.transferencias.services.IReporteService;
import com.banorte.reporte.diario.transferencias.services.ITransferenciasService;
import com.banorte.reporte.diario.transferencias.services.mail.MailSender;

public class TransferenciasServiceImpl implements ITransferenciasService 
{
	private static final Logger LOG = Logger.getLogger(TransferenciasServiceImpl.class);
	
	private IConsultaDao consultaDao;
	private MailSender mailSender;
	private MensajesVo mensajesVo;
	private IReporteService reporteService;
	
	@Override
	public void consultarTransferenciasReporte() 
	{
		FileOutputStream fileOut = null;
		PrintWriter wr = null;
		BufferedWriter bw = null;
		
		List<DatosTransferencia> transferencias = null;
		
		Map<String, Object> datos = null;
		
		String pathReporte = consultaDao.consultarParametro(IConsultaDao.PARAMETRO_PATH_REPORTE);
		String emailsTo = consultaDao.consultarParametro(IConsultaDao.PARAMETRO_EMAILS_TO);
		
		boolean seTienenEmailsDestinatarios = true;
		boolean realizarConsultaTransferencias = true;
		
		if(emailsTo == null || "".equals(emailsTo.trim()))
		{
			seTienenEmailsDestinatarios = false;
			LOG.warn("--> " + mensajesVo.getMensajeErrorEmailsTo());
		}
		
		if(pathReporte == null || "".equals(pathReporte.trim()))
		{
			LOG.error("--> " + mensajesVo.getMensajeErrorPath());
			
			datos = new HashMap<String, Object>();
			datos.put("mensaje", mensajesVo.getMensajeErrorPathEmail());
			enviarEmail(seTienenEmailsDestinatarios, emailsTo, datos);
		}
		else
		{
			try
			{
				datos = new HashMap<String, Object>();
				datos.put("mensaje", mensajesVo.getMensajeInicioproceso());
				enviarEmail(seTienenEmailsDestinatarios, emailsTo, datos);
				
				consultaDao.ejecutarStoredProcedure();
			}
			catch(Exception e)
			{
				realizarConsultaTransferencias = false;
				LOG.error("-->" + mensajesVo.getMensajeErrorStoredProcedure(), e);
				datos = new HashMap<String, Object>();
				datos.put("mensaje", mensajesVo.getMensajeErrorStoredProcedure() +  e.getLocalizedMessage());
				enviarEmail(seTienenEmailsDestinatarios, emailsTo, datos);
			}
			
			try
			{
				if(realizarConsultaTransferencias)
				{
					transferencias = consultaDao.consultarTransferenciasReporte();
					if(transferencias != null && transferencias.size() > 0)
					{
						SimpleDateFormat d = new SimpleDateFormat("yyyyMMdd");
						
						// construir archivo reporte -- excel
						ByteArrayOutputStream baos = reporteService.crearReporteExcel(transferencias);
						LOG.warn("Registros encontrados: " + transferencias.size());
						
						// escribirlo en el directorio especificado en pathReporte
						fileOut = new FileOutputStream(pathReporte + File.separator + "SICA_TR_DIARIO_" +
								                   d.format(new Date()) + ".xls");

						fileOut.write(baos.toByteArray());
						fileOut.flush();
			            fileOut.close();
			            fileOut = null;
			            
			            //creamos archivo .txt
			            int indice = 0;
			            StringBuffer sb = new StringBuffer();
			            File f;
			            f = new File(pathReporte + File.separator + "SICA_TR_DIARIO_" +
				                   d.format(new Date()) + ".txt");
			            FileWriter w = new FileWriter(f);
			            bw = new BufferedWriter(w);
			            wr = new PrintWriter(bw);  
			            for (DatosTransferencia datosTransferencia : transferencias) {
			            	sb.append(datosTransferencia.getFechaOperacion());
			            	sb.append(datosTransferencia.getTipoOperacion());
			            	sb.append(datosTransferencia.getTipoTransferencia());
			            	sb.append(datosTransferencia.getIdOperacion());
			            	sb.append(datosTransferencia.getMedio());
			            	sb.append(datosTransferencia.getOtroMedio());			            	
			            	sb.append(datosTransferencia.getFolio());
			            	sb.append(datosTransferencia.getIdInstReporta());
			            	sb.append(datosTransferencia.getIdBcoCliOriginador());
			            	sb.append(datosTransferencia.getIdCliOriginador());
			            	sb.append(datosTransferencia.getTipoCliOriginador());
			            	sb.append(datosTransferencia.getRazonSocialCli());
			            	sb.append(datosTransferencia.getApPaternoCli());
			            	sb.append(datosTransferencia.getApMaternoCli());
			            	sb.append(datosTransferencia.getApNombreCli());
			            	sb.append(datosTransferencia.getFechaNac());
			            	sb.append(datosTransferencia.getNacionCliOriginador());
			            	sb.append(datosTransferencia.getTipoIdOriginadorExt());
			            	sb.append(datosTransferencia.getIdOriginadorExt());
			            	sb.append(datosTransferencia.getSexoCliOriginador());
			            	sb.append(datosTransferencia.getEdoNacCliOriginador());
			            	sb.append(datosTransferencia.getTipoCuentaOrd());
			            	sb.append(datosTransferencia.getCuentaOrd());
			            	sb.append(datosTransferencia.getTipoIdInstRecept());
			            	sb.append(datosTransferencia.getIdInstRecept());
			            	sb.append(datosTransferencia.getTipoIdInstBenef());
			            	sb.append(datosTransferencia.getIdInstBenef());
			            	sb.append(datosTransferencia.getTipoBenef());
			            	sb.append(datosTransferencia.getNombreRazonSocBenef());
			            	sb.append(datosTransferencia.getCuentaBenef());
			            	sb.append(datosTransferencia.getPaisBcoBenef());
			            	sb.append(datosTransferencia.getMontoOp());
			            	sb.append(datosTransferencia.getIdDivisa());
			            	sb.append(datosTransferencia.getObservaciones());
			            	sb.append(datosTransferencia.getFolioTransNacional());
			            	sb.append(datosTransferencia.getTipoFondeoTrans());
			            	sb.append(datosTransferencia.getFondeoTrans());
			            	sb.append("\n");			            	 
						}
			            wr.write(sb.toString());
			            wr.flush();
			            wr.close();
			            bw.close();
			            
			            
			            
					}
				}
				consultaDao.actualizaBanderaEnvBanxico();
			}
			catch(Exception e)
			{
				LOG.error("-->" + mensajesVo.getMensajeErrorTransferencias(), e);
				datos = new HashMap<String, Object>();
				datos.put("mensaje", mensajesVo.getMensajeErrorTransferencias() +  e.getLocalizedMessage());
				enviarEmail(seTienenEmailsDestinatarios, emailsTo, datos);
			}
			
			finally{
				if (fileOut != null){
					try {
						fileOut.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				if (wr != null){
					wr.close();
				}
				if (bw != null){
					try {
						bw.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
			LOG.warn("-->" + mensajesVo.getMensajeFinProceso());
			datos = new HashMap<String, Object>();
			datos.put("mensaje", mensajesVo.getMensajeFinProceso());
			enviarEmail(seTienenEmailsDestinatarios, emailsTo, datos);
		}
	}
	
	public void enviarEmail(boolean seTienenEmailsDestinatarios, String emailsTo, Map<String, Object> datos)
	{
		if(seTienenEmailsDestinatarios)
		{
			mailSender.setTo(emailsTo);
			mailSender.send(datos);
		}
	}

	public IConsultaDao getConsultaDao() { return consultaDao; }
	public void setConsultaDao(IConsultaDao consultaDao) { this.consultaDao = consultaDao; }

	public MailSender getMailSender() { return mailSender; }
	public void setMailSender(MailSender mailSender) { this.mailSender = mailSender; }

	public MensajesVo getMensajesVo() { return mensajesVo; }
	public void setMensajesVo(MensajesVo mensajesVo) { this.mensajesVo = mensajesVo; }

	public IReporteService getReporteService() {
		return reporteService;
	}

	public void setReporteService(IReporteService reporteService) {
		this.reporteService = reporteService;
	}

}
