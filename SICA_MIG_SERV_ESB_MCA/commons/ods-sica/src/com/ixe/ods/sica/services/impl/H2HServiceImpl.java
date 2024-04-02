package com.ixe.ods.sica.services.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ixe.ods.sica.Constantes;
import com.ixe.ods.sica.dao.H2HDao;
import com.ixe.ods.sica.model.Deal;
import com.ixe.ods.sica.model.DealDetalle;
import com.ixe.ods.sica.model.DetalleH2H;
import com.ixe.ods.sica.model.RegulatorioDatosPM;
//import com.ixe.ods.sica.model.Divisa;
import com.ixe.ods.sica.services.H2HService;
import com.ixe.ods.sica.utils.DateUtils;

public class H2HServiceImpl implements H2HService 
{
	private static final transient Log log = LogFactory.getLog(H2HServiceImpl.class);
	
	private H2HDao h2hDao;
	
	public void solicitarCancelacionDetalleH2H(DealDetalle detalle, boolean esReverso)
	{
		List resultado = null;
		DetalleH2H detalleH2H = null;
		Date fechaHoy = null;
		Date fechaCaptura = null;
		
		try
		{
			if(detalle != null && !detalle.isPesos() && 
					(detalle.isCancelado() || Deal.REVERSADO == detalle.getReversado() || 
						(detalle.isLiquidado() && esReverso)))
			{
				resultado = h2hDao.findDetalleH2HByIdDealPosicion(detalle.getIdDealPosicion());
				if(resultado != null && !resultado.isEmpty())
				{
					detalleH2H = (DetalleH2H)resultado.get(0);
					fechaHoy = DateUtils.inicioDia();
					fechaCaptura = DateUtils.inicioDia(detalleH2H.getFechaCaptura());
					
					if(fechaHoy.after(fechaCaptura) || fechaHoy.before(fechaCaptura))
					{
						log.error("--> solicitarCancelacionDetalleH2H(" + new Date() + "): El detalle " + detalle.getIdDealPosicion() + 
								  " del deal " + detalle.getDeal().getIdDeal() + " tiene fecha de captura diferente " +
								  " al dia en curso y no es posible solicitar la cancelacion.");
					}
					else
					{
						if(STATUS_NUEVA_H2H.equals(detalleH2H.getStatusH2H())   && 
						   NO_CANCELADO.equals(detalleH2H.getCancelarDetalle()) && 
						   VERSION_1.intValue() == detalleH2H.getVersion().intValue() &&
						   detalleH2H.getFechaCancelacion() == null)
						{
							detalleH2H.setCancelarDetalle(CANCELADO);
							h2hDao.update(detalleH2H);
							log.error("--> solicitarCancelacionDetalleH2H(" + new Date() + "): El detalle " + detalle.getIdDealPosicion() + 
									  " del deal " + detalle.getDeal().getIdDeal() + " ha sido actualizado para " +
									  " para solicitar la cancelacion en H2H.");
						}
						else
						{
							log.error("--> solicitarCancelacionDetalleH2H(" + new Date() + "): El detalle " + detalle.getIdDealPosicion() + 
									  " del deal " + detalle.getDeal().getIdDeal() + " no cumple con los criterios definidos" +
									  " para solicitar la cancelacion en H2H.");
						}
					}
				}
				else
				{
					log.error("--> solicitarCancelacionDetalleH2H(" + new Date() + "): El detalle " + detalle.getIdDealPosicion() + 
							  " del deal " + detalle.getDeal().getIdDeal() + " no ha sido registrado para solicitar " +
							  "la cancelacion en H2H.");
				}
			}
		}
		catch(Exception e)
		{
			log.error("--> solicitarCancelacionDetalleH2H(" + new Date() + "): Ha ocurrido un error al tratar " +
					  "de solicitar la cancelacion del detalle " + detalle.getIdDealPosicion() + 
					  " del deal " + detalle.getDeal().getIdDeal() + " al H2H.");
		}
	}
	
	public void validarDealsH2HAlCierreVespertino()
	{
		log.error("--> validarDealsH2HAlCierreVespertino(" + new Date() + "): Inicia validacion de deals al cierre vespertino..");
		
		List listaDealsFiltrada = filtrarDealsAltaOconAutorizacionesPendientes();
		if(listaDealsFiltrada != null && !listaDealsFiltrada.isEmpty())
			registrarDetallesH2H(listaDealsFiltrada, H2HService.PROCESAR_TODOS_LOS_DETALLES, H2HService.ES_CIERRE_VESPERTINO);
		else
			log.error("--> validarDealsH2HAlCierreVespertino(" + new Date() + "): " +
					  "No se encontraron deals capturados en este dia con estatus de " +
					  "ALTA o PROCESANDOSE (con autorizaciones pendientes) que deban enviarse al H2H.");
	}
	
	public List filtrarDealsAltaOconAutorizacionesPendientes()
	{
		List listaDeals = h2hDao.findDealsEnAltaProcesandoceCapturadosHoy();
		List listaDealsFiltrada = new ArrayList();
		List detalles = null;
		Deal deal = null;
		DealDetalle detalle = null;
		int indice = -1, indiceDetalle = -1;
		
		if(listaDeals != null && !listaDeals.isEmpty())
		{
			log.warn("--> filtrarDealsAltaOconAutorizacionesPendientes...");
			for(indice = 0; indice < listaDeals.size(); indice++)
			{
				deal = (Deal)listaDeals.get(indice);
				if(!deal.isDealBalanceo())
				{
					log.warn("deal: " + deal.getIdDeal() + ", estatus: " + deal.getStatusDeal() + ", eventos: --> " + deal.getEventosDeal() + "<--");
					
					if(Deal.STATUS_DEAL_PROCESO_CAPTURA.equals(deal.getStatusDeal()))
						listaDealsFiltrada.add(deal);
					else if(Deal.STATUS_DEAL_CAPTURADO.equals(deal.getStatusDeal()))
					{
						if(deal.haySolAut()
						 /*deal.tieneAutPendientesHorario()                                                  || // deal pendiente por Horario
						   deal.tieneAutPendientesMonto()                                                    || // deal pendiente por Monto
						   deal.getBalance() != 0.0                                                          || // deal no balanceado
						   deal.getContratoSica() == null                                                    || // deal pendiente por contrato
						   deal.isEvento(Deal.EV_IND_SOBREPRECIO, new String[] {Deal.EV_SOLICITUD})          || // deal pendiente por sobreprecio
						   Deal.EV_SOLICITUD.equals(deal.decodificarEvento(Deal.EV_IND_DOCUMENTACION))       || // deal pendiente por Documentacion
						   Deal.EV_SOLICITUD.equals(deal.decodificarEvento(Deal.EV_IND_INT_DOCUMENTACION))   || // deal interbancario pendiente por Documentacion
						   Deal.EV_SOLICITUD.equals(deal.decodificarEvento(Deal.EV_IND_PLANTILLA))           || // deal pendiente por plantilla
						   Deal.EV_SOLICITUD.equals(deal.decodificarEvento(Deal.EV_IND_INT_PLANTILLA))       || // deal interbancario pendiente por plantilla
						   Deal.EV_SOLICITUD.equals(deal.decodificarEvento(Deal.EV_IND_GRAL_PAG_ANT))        || // deal pendiente por pago anticipado -- toma en firme
						   Deal.EV_SOLICITUD.equals(deal.decodificarEvento(Deal.EV_IND_INT_LINEA_OPERACION)) || // deal pendiente por linea de operacion - toma en firme
						   Deal.EV_SOLICITUD.equals(deal.getCambioRfc())                                     || // deal pendiente por RFC
						   Deal.EV_SOLICITUD.equals(deal.getCambioEmail())*/
						   )
						{
							listaDealsFiltrada.add(deal);
						}
						else
						{
							if(deal.getDetalles() != null && !deal.getDetalles().isEmpty())
							{
								detalles = deal.getDetalles();
								for(indiceDetalle = 0; indiceDetalle < detalles.size(); indiceDetalle++)
								{
									detalle = (DealDetalle)detalles.get(indiceDetalle);
									
									if(!detalle.isPesos())
									{
										if(detalle.haySolAut())
										{
											//deal con detalles de divisa en alta o procesandose pero atorados en el panel que deben enviarse al H2H
											if(detalle.isAltaOProcesandose() && (!detalle.tieneSolCancPendiente() || !detalle.tieneSolModifPendiente()))
											{
												listaDealsFiltrada.add(deal);
												break;
											}
											else
												continue;
										}
										else if(DealDetalle.STATUS_DET_PROCESO_CAPTURA.equals(detalle.getStatusDetalleDeal())) 
										{
											//deal sin autorizaciones pendientes pero con detalles de divisa en alta deben enviarse al H2H
											listaDealsFiltrada.add(deal);
											break;
										}
									}
								}
								detalles.clear();
								detalles = null;
							}
						}
					}
				}
			}
		}
		
		return listaDealsFiltrada;
	}
	
	public void registrarDetallesH2H(List listaDeals, List detallesAprocesar, boolean esCierreVespertino)
	{
		List listaDetallesH2H = null;
		DetalleH2H detalleH2H = null;
		
		Deal deal = null;
		DealDetalle detalle = null;
		List listaDetallesDeal = null;
		//String claveBanxicoCliente = null;
		RegulatorioDatosPM pm = null;
		int indice = -1, indiceDetalle = -1;
		boolean dealBalanceado = false;
		
		String mensajeNoProcesado = null;
		
		if(listaDeals != null && listaDeals.size() > 0)
		{
			for(indice = 0; indice < listaDeals.size(); indice++)
			{
				try
				{
					deal = (Deal)listaDeals.get(indice);
					
					mensajeNoProcesado = "--> registrarDetallesH2H(" + new Date() + "): El deal " + deal.getIdDeal() + 
                    " no fue procesado para registrar sus detalles que van al H2H porque ";

					log.error("--> registrarDetallesH2H(" + new Date() + "): Inicia el procesamiento del deal " + deal.getIdDeal());
					
					dealBalanceado = deal.getBalance() == 0.00;					
					
					if(!isProcesableByReglasNegocioDeal(deal))
						continue;
					else
					{
						if(deal.getDetalles() == null || deal.getDetalles().size() == 0)
						{
							log.error(mensajeNoProcesado + "no tiene detalles capturados.");
							continue;
						}
						else
						{
							listaDetallesDeal = deal.getDetalles();
							
							if(isProcesablePorExistenciaDePesos(listaDetallesDeal, deal.getIdDeal(), esCierreVespertino,  dealBalanceado))
							{
								listaDetallesH2H = h2hDao.findDetallesH2HDeal(deal.getIdDeal());
								
								if(deal.getContratoSica() != null)
								{
									if(deal.getCliente().getTipoPersona() != null && "PM".equals(deal.getCliente().getTipoPersona()))
									{
										//claveBanxicoCliente = h2hDao.findClaveBanxico(deal.getContratoSica().getNoCuenta(), deal.getCliente().getIdPersona());
										pm = h2hDao.findDatosRegulatoriosPM(deal.getContratoSica().getNoCuenta(), deal.getCliente().getIdPersona());
									}
								}
									
								
								for(indiceDetalle = 0; indiceDetalle < listaDetallesDeal.size(); indiceDetalle++)
								{
									detalle = (DealDetalle)listaDetallesDeal.get(indiceDetalle);
									if(isProcesableByReglasNegocioDetalle(detalle, listaDetallesH2H, esCierreVespertino))
									{
										try
										{
											detalleH2H = crearDetalleH2H(detalle, pm, esCierreVespertino);
											h2hDao.save(detalleH2H);
											log.error("--> registrarDetallesH2H(" + new Date() + "): El detalle  " + detalle.getIdDealPosicion() + 
													  " del deal " + deal.getIdDeal() + " fue guardado correctamente para su envio al H2H.");
										}
										catch(Exception e)
										{
											log.error("--> Ocurrio un error al guardar el detalle " + detalle.getIdDealPosicion() + 
													  " del deal " + deal.getIdDeal(), e);
										}
									}
								}
							}
						}
					}
				}
				catch(Exception e)
				{
					log.error("--> registrarDetallesH2H(" + new Date() + "): A ocurrido un error al procesar los detalles del " +
							  deal.getIdDeal() + " cuando se validaban sus detalles para enviarlos al H2H.", e);
				}
			}
		}
		else
			log.error("--> registrarDetallesH2H(" + new Date() + "): Se envio una lista de deals vacia, termina procesamiento del servicio H2H.");
	}
	
	public DetalleH2H crearDetalleH2H(DealDetalle detalle, RegulatorioDatosPM pm, boolean esCierreVespertino)
	{
		double factorDivisaUsd = 1.0;
		String plazoProducto = null, nombreCliente = null;
		String claveBanxico = null, nombreClienteRegulatorio = null, rfc = null;
		List divisas = new ArrayList();
		divisas.add(USD); divisas.add(CAD); divisas.add(EUR); divisas.add(GBP);
		divisas.add(BRL); divisas.add(JPY); divisas.add(AUD); divisas.add(NZD);
		divisas.add(CHF);

		DetalleH2H detalleH2H = new DetalleH2H();
		
		detalleH2H.setIdDealPosicion(new Long(detalle.getIdDealPosicion()));
		detalleH2H.setIdDeal(new Long(detalle.getDeal().getIdDeal()));
		detalleH2H.setFechaEnvio(new Date());
		detalleH2H.setVersion(VERSION_1);
		detalleH2H.setTipoOperacion(detalle.isRecibimos() ? COMPRA : VENTA); // Buy:B  Sell:S
		detalleH2H.setFechaCaptura(detalle.getDeal().getFechaCaptura());
		detalleH2H.setFechaEfectiva(detalleH2H.getFechaCaptura());
		detalleH2H.setFechaLiquidacion(detalle.getDeal().getFechaLiquidacion());
		detalleH2H.setImporte(new BigDecimal(detalle.getMonto()));
		
		if(pm != null)
		{
			claveBanxico = pm.getClaveBanxico();
			nombreClienteRegulatorio = pm.getNombreContraparte();
			if(pm.getTipoIdentificador() != null && pm.getTipoIdentificador().getId().intValue() == 1) // RFC
				rfc = pm.getClaveIdentificadorContraparte();
		}
		
		if(EUR.equals(detalle.getDivisa().getIdDivisa()) || GBP.equals(detalle.getDivisa().getIdDivisa()) ||
		   USD.equals(detalle.getDivisa().getIdDivisa()))
		{
			if(detalle.getFactorDivisa() != null)
			{
				detalleH2H.setMontoDolarizado(new BigDecimal(detalle.getMontoUSD()));
				detalleH2H.setFactorDivisaUsd(new BigDecimal(detalle.getFactorDivisa().doubleValue()));
			}
			else
			{
				factorDivisaUsd = detalle.getTipoCambio() / detalle.getPrecioReferenciaMidSpot().doubleValue();
				detalleH2H.setMontoDolarizado(new BigDecimal(detalle.getMonto() * factorDivisaUsd));	
				detalleH2H.setFactorDivisaUsd(new BigDecimal(factorDivisaUsd));
			}
		}
		else
		{
			if(detalle.getDivisa().isFrecuente() && detalle.getFactorDivisa() != null)
				factorDivisaUsd = detalle.getFactorDivisa().doubleValue();
			else
				factorDivisaUsd = detalle.getTipoCambio() / detalle.getPrecioReferenciaMidSpot().doubleValue();
			
			detalleH2H.setMontoDolarizado(new BigDecimal(detalle.getMonto() * factorDivisaUsd));	
			detalleH2H.setFactorDivisaUsd(new BigDecimal(1 / factorDivisaUsd));	
		}
		
		if(detalle.isDolar())
			detalleH2H.setFactorDivisaUsd(new BigDecimal(detalle.getTipoCambio()));
		
		detalleH2H.setTipoCambio(detalleH2H.getFactorDivisaUsd());
		detalleH2H.setIdDivisa(divisas.contains(detalle.getDivisa().getIdDivisa()) ? detalle.getDivisa().getIdDivisa() : OTD);
		
		if(Constantes.CASH.equals(detalle.getDeal().getTipoValor().trim()))
			plazoProducto = FX_V_HOY;
		else if(Constantes.TOM.equals(detalle.getDeal().getTipoValor().trim()))
			plazoProducto = FX_V_24HRS;
		else if(Constantes.SPOT.equals(detalle.getDeal().getTipoValor().trim()))
			plazoProducto = FX_V_48HRS;
		else
			plazoProducto = FX_FORWARD; // para un periodo de liquidacion mayor a 48 horas
		
		detalleH2H.setPlazo(plazoProducto);
		detalleH2H.setProducto(plazoProducto);
		
		detalleH2H.setClaveContraparte(claveBanxico);
		
		if(nombreClienteRegulatorio != null)
		{
			if(nombreClienteRegulatorio.trim().length() > 50)
				detalleH2H.setNombreCliente(nombreClienteRegulatorio.trim().substring(0, 50));
			else
				detalleH2H.setNombreCliente(nombreClienteRegulatorio.trim());	
		}
		else if(detalle.getDeal().getCliente() != null && !"PM".equals(detalle.getDeal().getCliente().getTipoPersona()))
		{
			if(detalleH2H.getMontoDolarizado().compareTo(MILLON_DOLARES_USD) == MENOR_MILLON_DOLARES)
			{
				detalleH2H.setNombreCliente(null);
				detalleH2H.setClaveContraparte(null);
			}
			else
			{
				if(detalle.getDeal().getCliente().getNombreCorto() != null)
					nombreCliente = detalle.getDeal().getCliente().getNombreCorto().trim();
				else if(detalle.getDeal().getCliente().getNombreComercial() != null)
					nombreCliente = detalle.getDeal().getCliente().getNombreComercial().trim();
				else if(detalle.getDeal().getCliente().getNombreCompleto() != null)
					nombreCliente = detalle.getDeal().getCliente().getNombreCompleto().trim();
				
				if(nombreCliente != null && !"".equals(nombreCliente.trim()))
				{
					nombreCliente = nombreCliente.trim();
					
					if(nombreCliente.length() > 50)
						detalleH2H.setNombreCliente(nombreCliente.substring(0, 50));
					else
						detalleH2H.setNombreCliente(nombreCliente);
				}
			}
		}
		else
			detalleH2H.setNombreCliente(null);
		
		/*if(detalleH2H.getMontoDolarizado().compareTo(MILLON_DOLARES_USD) == MENOR_MILLON_DOLARES)
		{
			detalleH2H.setClaveContraparte(null);
			detalleH2H.setNombreCliente(null);
		}
		else
		{
			detalleH2H.setClaveContraparte(claveBanxico); 
			
			if(nombreClienteRegulatorio != null)
			{
				if(nombreClienteRegulatorio.trim().length() > 50)
					detalleH2H.setNombreCliente(nombreClienteRegulatorio.trim().substring(0, 50));
				else
					detalleH2H.setNombreCliente(nombreClienteRegulatorio.trim());	
			}
			else 
			{
				if(detalle.getDeal().getCliente() != null)
				{
					if(detalle.getDeal().getCliente().getNombreCorto() != null)
						nombreCliente = detalle.getDeal().getCliente().getNombreCorto().trim();
					else if(detalle.getDeal().getCliente().getNombreComercial() != null)
						nombreCliente = detalle.getDeal().getCliente().getNombreComercial().trim();
					else if(detalle.getDeal().getCliente().getNombreCompleto() != null)
						nombreCliente = detalle.getDeal().getCliente().getNombreCompleto().trim();
					
					if(nombreCliente != null && !"".equals(nombreCliente.trim()))
					{
						nombreCliente = nombreCliente.trim();
						
						if(nombreCliente.length() > 50)
							detalleH2H.setNombreCliente(nombreCliente.substring(0, 50));
						else
							detalleH2H.setNombreCliente(nombreCliente);
					}
				}
				else
				{
					detalleH2H.setNombreCliente(null);
				}
			}
		}*/
		
		if(rfc != null)
			detalleH2H.setRfc(rfc.trim());
		else if(detalle.getDeal().getCliente() != null)
		{
			detalleH2H.setRfc(detalle.getDeal().getCliente().getRfc() != null ? detalle.getDeal().getCliente().getRfc().trim() : null);
			
			//if(detalle.getDeal().getCliente().getSectorEconomico() != null)
			//	detalleH2H.setTipoCliente(detalle.getDeal().getCliente().getSectorEconomico().getSectorBanxico());
			//else
			//	detalleH2H.setTipoCliente(null);
		}
		else
		{
			detalleH2H.setRfc(null);
			//detalleH2H.setTipoCliente(null);
		}	
		
		detalleH2H.setTipoCliente(null);
		detalleH2H.setStatusH2H(STATUS_NUEVA_H2H);
		detalleH2H.setEnviada(NO_ENVIADA);
		
		if(detalle.getDeal().getPromotor() != null)
			detalleH2H.setIdPersonaPromotor(new Long(detalle.getDeal().getPromotor().getIdPersona().longValue()));
		else
			detalleH2H.setIdPersonaPromotor(new Long(0)); // el deal no tiene promotor asignado 
		
		detalleH2H.setCierreVespertino(esCierreVespertino ? PROCESADO_CIERRE_VESPERTINO : PROCESADO_INTRADIA);
		detalleH2H.setFechaCancelacion(null);
		detalleH2H.setCancelarDetalle(NO_CANCELADO);
		
		return detalleH2H;
	}
	
	public boolean isProcesableByReglasNegocioDetalle(DealDetalle detalle, List listaDetallesH2H, boolean esCierreVespertino)
	{
		int indice = -1;
		boolean respuesta = true;
		boolean detalleRegistrado = false;
		DetalleH2H detalleH2H = null;
		Date fechaHoy = DateUtils.inicioDia();
		
		String mensajeNoProcesado = "--> isProcesableByReglasNegocioDetalle(" + new Date() + "): El detalle " + detalle.getIdDealPosicion() + 
		                            " con folio " + detalle.getFolioDetalle() + " del deal " + detalle.getDeal().getIdDeal() + 
		                            " no fue procesado para el envio al H2H porque ";
		
		if(detalle.isPesos())
		{
			log.error(mensajeNoProcesado + " su divisa es MXN (Pesos mexicanos).");
			respuesta = false;
		}
		else if(detalle.isReversadoProcesoReverso())
		{
			log.error(mensajeNoProcesado + " esta reversado o se encuentra en proceso de reverso.");
			respuesta = false;
		}
		else if(detalle.isCancelado())
		{
			log.error(mensajeNoProcesado + " esta cancelado.");
			respuesta = false;
		}
		else if(fechaHoy.after(DateUtils.inicioDia(detalle.getDeal().getFechaCaptura())))
		{
			log.error(mensajeNoProcesado + " no se reportan operaciones de dias anteriores a la fecha actual.");
			respuesta = false;
		}
		else if(detalle.tieneSolCancPendiente() || detalle.tieneSolModifPendiente())
		{
			log.error(mensajeNoProcesado + " esta pendiente por cancelarce o modificarse.");
			respuesta = false;
		}
		else
		{
			if(listaDetallesH2H == null || listaDetallesH2H.isEmpty())
				detalleRegistrado = false;
			else
			{
				for(indice = 0; indice < listaDetallesH2H.size(); indice++)
				{
					detalleH2H = (DetalleH2H)listaDetallesH2H.get(indice);
					if(detalle.getIdDealPosicion() == detalleH2H.getIdDealPosicion().intValue())
					{
						detalleRegistrado = true;
						break;
					}
				}
			}
			
			if(detalleRegistrado)
			{
				log.error(mensajeNoProcesado + " ya fue registrado.");
				respuesta = false;
			}
			else
				respuesta = true;
		}
		
		return respuesta;
	}
	
	public boolean isProcesableByReglasNegocioDeal(Deal deal)
	{
		boolean respuesta = true;
		Date fechaHoy = DateUtils.inicioDia();
		
		String mensajeNoProcesado = "--> registrarDetallesH2H(" + new Date() + "): El deal " + deal.getIdDeal() + 
        						    " no fue procesado para registrar sus detalles que van al H2H porque ";

		if(deal.isDealBalanceo())
		{
			log.error(mensajeNoProcesado + " es de balanceo.");
			respuesta = false;
		}	
		else if(deal.isReversadoProcesoReverso())
		{
			log.error(mensajeNoProcesado + " tiene estatus Reversado o esta en proceso de reverso.");
			respuesta = false;
		}
		else if(Deal.STATUS_DEAL_CANCELADO.equals(deal.getStatusDeal()) || 
				Deal.STATUS_DEAL_TOTALMENTE_LIQUIDADO.equals(deal.getStatusDeal()))
		{
			log.error(mensajeNoProcesado + " tiene estatus " + deal.getStatusDeal() + ".");
			respuesta = false;
		}
		else if(!isDealCapturadoHoy(fechaHoy, deal.getFechaCaptura()))
		{
			log.error(mensajeNoProcesado + " no fue capturado el dia de hoy y no se reportan " +
			                      "detalles de deal de dias previos. Fecha captura deal: " + 
			                      deal.getFechaCaptura());
			respuesta = false;
		}
		
		return respuesta;
	}
	
	public boolean isProcesablePorExistenciaDePesos(List listaDetallesDeal, int idDeal, boolean esCierreVespertino,  boolean dealBalanceado)
	{
		boolean respuesta = true;
		boolean hayDetalleEnPesos = false;
		String mensajeNoProcesado = "--> registrarDetallesH2H(" + new Date() + "): El deal " + idDeal + 
	    							" no fue procesado para registrar sus detalles que van al H2H porque ";
		
		hayDetalleEnPesos = hayAlgunDetalleEnPesosMexicanos(listaDetallesDeal);
		
		if(hayDetalleEnPesos)
			respuesta = true;
		else
		{
			if(esCierreVespertino)
			{
				if(dealBalanceado)
				{
					// A continuación la minuta de la sesión de Peer Review para la Baja del canal H2H IXE, la cual tuvo lugar el 
					// día 14 de Marzo de 2016 de 12:00 a 13:00 hrs. 
					// Se puntualiza que para el caso de operaciones con Fecha-Valor distintas a CASH que se encuentren en estatus Alta 
					// al cierre de la operación de SICA:
					// 		Si el deal está completo y solo participan divisa vs divisa este no se informa a Banxico.
					log.error(mensajeNoProcesado + "no tiene ningun detalle en Pesos Mexicanos y el deal esta balanceado." +
							  "Es un deal divisa VS divisa. El deal se proceso en el cierre vespertino.");
					respuesta = false;
				}
				else
				{
					//		Si tienen un solo detalle donde participe al menos una divisa, se asume que esta será liquidada vs MXN, 
					//      por lo que este deal se tomará en cuenta para informarse a Banxico.
					
					// continuar con las validaciones. Los detalles de deal se pueden enviar al H2H.
					
					//                  Reunión peer review para baja de H2H de IXE:
					// Para los deals en alta que tienen capturado un detalle de divisa en la parte 'entregamos'
					// y no tiene detalle en la parte 'recibimos', se asume que son pesos lo que se 
					// va a capturar, por tanto se debe enviar el detalle de divisa: Cesar Elizalde, 14 de marzo de 2016
					respuesta = true;
				}
			}
			else
			{
				log.error(mensajeNoProcesado + "no tiene ningun detalle en Pesos Mexicanos.");
				respuesta = false;
			}
		}
		
		return respuesta;
	}
	
	public boolean hayAlgunDetalleEnPesosMexicanos(List listaDetallesDeal)
	{
		boolean respuesta = true;
		DealDetalle detalle = null;
		
		if(listaDetallesDeal == null || listaDetallesDeal.size() == 0)
			respuesta = false;
		else
		{
			for(int indice = 0; indice < listaDetallesDeal.size(); indice++)
			{
				detalle = (DealDetalle)listaDetallesDeal.get(indice);
				if(detalle.isPesos() && detalle.isAltaOProcesandoseOLiquidado() && !detalle.isReversadoProcesoReverso())
				{
					respuesta = true;
					break;
				}
				else
					respuesta = false;
			}
		}
		
		return respuesta;
	}
	
	public boolean isDealCapturadoHoy(Date fechaHoy, Date fechaCaptura)
	{
		boolean respuesta = true;
		
		if(fechaCaptura == null || fechaHoy.after(DateUtils.inicioDia(fechaCaptura)))
			respuesta = false;
		
		return respuesta;
	}

	public H2HDao getH2hDao() 
	{
		return h2hDao;
	}

	public void setH2hDao(H2HDao dao) 
	{
		h2hDao = dao;
	}
}
