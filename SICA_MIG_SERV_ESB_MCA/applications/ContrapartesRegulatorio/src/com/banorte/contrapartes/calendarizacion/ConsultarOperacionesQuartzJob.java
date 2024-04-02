package com.banorte.contrapartes.calendarizacion;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.banorte.contrapartes.services.IOperacionesDeal;

public class ConsultarOperacionesQuartzJob  extends QuartzJobBean 
{
	private static final Logger LOG = Logger.getLogger(ConsultarOperacionesQuartzJob.class);
	private IOperacionesDeal operacionesDealService;
	public final int OPERACION_VESPERTINA = 3;
	
	public ConsultarOperacionesQuartzJob(){}
	
	@Override
	protected void executeInternal(JobExecutionContext jobExcutionContext) throws JobExecutionException 
	{
		int estadoSica = 0;
		LOG.warn("**********************************************************************************************************");
		LOG.warn("***************************          PROCESO DE CONTRAPARTES REGULATORIO         *************************");
		LOG.warn("**********************************************************************************************************");
		try
		{
			operacionesDealService.consultaDeals();
			estadoSica = operacionesDealService.consultarEstadoSistema();
			if(estadoSica == OPERACION_VESPERTINA)
			{
				LOG.warn("El sistema SICA se encuentra en OPERACION VESPERTINA, se procede a detener el proceso de consulta.");
				jobExcutionContext.getScheduler().shutdown();
				LOG.warn("**********************************************************************************************************");
				LOG.warn("******************************     FIN DE PROCESO CONTRAPARTES REGULATORIO     ***************************");
				LOG.warn("**********************************************************************************************************");
			}
			else
				LOG.warn("El proceso continuará ejecutándose mientras no se encuentre el sistema SICA en OPERACION VESPERTINA....");
			LOG.warn("\n\n\n");
		}
		catch(SchedulerException se)
		{
			se.printStackTrace();
		}
	}

	public IOperacionesDeal getOperacionesDealService() {
		return operacionesDealService;
	}

	public void setOperacionesDealService(IOperacionesDeal operacionesDealService) {
		this.operacionesDealService = operacionesDealService;
	}

}
