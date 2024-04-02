package com.banorte.contrapartes.services.impl;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;

import com.banorte.contrapartes.dao.IConsultaDao;
import com.banorte.contrapartes.dto.OperacionDto;
import com.banorte.contrapartes.services.IOperacionesDeal;
import com.banorte.contrapartes.services.IReporteOperaciones;
import com.banorte.contrapartes.services.mail.MailSender;

public class OperacionesDealImpl implements IOperacionesDeal {

	private IConsultaDao consultaDao;
	private MailSender mailSender;
	private IReporteOperaciones reporteService;
	
	@Override
	public int consultarEstadoSistema() 
	{
		return consultaDao.consultarEstadoSistema();
	}
	
	public List<OperacionDto> consultaDeals()
	{
		List<OperacionDto> operaciones = consultaDao.consultaDeals();
		HashMap<String , Object> datosReporte = null;
		
		if(operaciones != null && operaciones.size() > 0)
		{
			ByteArrayOutputStream datos = reporteService.crearReporteExcel(operaciones);
			if(datos != null)
			{
				datosReporte = new HashMap<String, Object>();
				datosReporte.put("nameAttachment", "OperacionesRegulatorio.xlsx");
				datosReporte.put("attachment", datos);
				enviarEmail(datosReporte);
			}
			
		}
		
		return operaciones;
		
	}
	
	public void enviarEmail(HashMap<String , Object> datosReporte)
	{
		mailSender.send(datosReporte);
	}
	
	public void consultarDeal()
	{
		consultaDao.consultaDeals();
	}
	
	public IConsultaDao getConsultaDao() {
		return consultaDao;
	}
	public void setConsultaDao(IConsultaDao consultaDao) {
		this.consultaDao = consultaDao;
	}
	public MailSender getMailSender() {
		return mailSender;
	}
	public void setMailSender(MailSender mailSender) {
		this.mailSender = mailSender;
	}

	public IReporteOperaciones getReporteService() {
		return reporteService;
	}

	public void setReporteService(IReporteOperaciones reporteService) {
		this.reporteService = reporteService;
	}
}
