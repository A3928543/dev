/*
 * $Id: SicaTellerSessionBean.java,v 1.27.10.3.6.1 2011/04/26 02:34:18 galiciad Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 - 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.SessionContext;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.access.ContextSingletonBeanFactoryLocator;
import org.springframework.ejb.support.AbstractStatelessSessionBean;

import com.ixe.bean.bup.RelacionCuentaPersona;
import com.ixe.contratacion.ContratacionException;
import com.ixe.ods.bup.model.ContratoSica;
import com.ixe.ods.bup.model.EmpleadoIxe;
import com.ixe.ods.bup.model.Persona;
import com.ixe.ods.seguridad.model.IUsuario;
import com.ixe.ods.seguridad.model.SeguridadException;
import com.ixe.ods.seguridad.model.Usuario;
import com.ixe.ods.seguridad.services.TicketService;
import com.ixe.ods.sica.model.Canal;
import com.ixe.ods.sica.model.CanalMesa;
import com.ixe.ods.sica.model.CodigoPostalListaBlanca;
import com.ixe.ods.sica.model.ComplementoDatos;
import com.ixe.ods.sica.model.Deal;
import com.ixe.ods.sica.model.DealDetalle;
import com.ixe.ods.sica.model.Divisa;
import com.ixe.ods.sica.model.Estado;
import com.ixe.ods.sica.model.FactorDivisaActual;
import com.ixe.ods.sica.model.IPlantilla;
import com.ixe.ods.sica.model.IPlantillaCuentaIxe;
import com.ixe.ods.sica.model.IPlantillaIntl;
import com.ixe.ods.sica.model.IPlantillaNacional;
import com.ixe.ods.sica.model.LimiteEfectivo;
import com.ixe.ods.sica.model.LimitesRestriccionOperacion;
import com.ixe.ods.sica.model.ParametroSica;
import com.ixe.ods.sica.model.PersonaListaBlanca;
import com.ixe.ods.sica.model.Plantilla;
import com.ixe.ods.sica.model.PlantillaCuentaIxe;
import com.ixe.ods.sica.model.PlantillaIntl;
import com.ixe.ods.sica.model.PlantillaNacional;
import com.ixe.ods.sica.model.PlantillaTranNacional;
import com.ixe.ods.sica.model.SpreadActual;
import com.ixe.ods.sica.model.TokenTeller;
import com.ixe.ods.sica.sdo.PizarronServiceData;
import com.ixe.ods.sica.sdo.SicaServiceData;
import com.ixe.ods.sica.sdo.WorkFlowServiceData;
import com.ixe.ods.sica.services.ContratoDireccionesService;
import com.ixe.ods.sica.services.DealService;
import com.ixe.ods.sica.services.EsbService;
import com.ixe.ods.sica.services.RegistroClienteContratoService;
import com.ixe.ods.sica.teller.dto.BancoInternacionalDto;
import com.ixe.ods.sica.teller.dto.ContratoSicaDto;
import com.ixe.ods.sica.teller.dto.DealDetalleDto;
import com.ixe.ods.sica.teller.dto.DealDto;
import com.ixe.ods.sica.teller.dto.DivisaDto;
import com.ixe.ods.sica.teller.dto.FormaPagoLiqDto;
import com.ixe.ods.sica.teller.dto.LimiteOperacionDto;
import com.ixe.ods.sica.teller.dto.PersonaDto;
import com.ixe.ods.sica.teller.dto.PlantillaCuentaIxeDto;
import com.ixe.ods.sica.teller.dto.PlantillaInternacionalDto;
import com.ixe.ods.sica.teller.dto.PlantillaNacionalDto;
import com.ixe.ods.sica.teller.dto.PlantillaTranNacionalDto;
import com.ixe.ods.sica.teller.dto.ResultadoOperacionDto;
import com.ixe.ods.sica.teller.dto.TipoCambioDto;
import com.ixe.treasury.dom.common.Banco;
import com.ixe.treasury.dom.common.ExternalSiteException;
import com.ixe.treasury.dom.common.FormaPagoLiq;
import com.ixe.treasury.dom.common.Pais;
import com.legosoft.hibernate.type.SiNoType;

/**
 * Componente para realizar operaciones al Sistema SICA desde el Teller.
 *
 * <p>Eventos del encabezado de deal:</p>
 * <ul>
 * <li>Indice 0.- Horario (No aplica).</li>
 * <li>Indice 1.- Monto: S)olicitada Aut. | A)utorizado | N)egado.</li>
 * <li>Indice 2.- Documentaci&oacute;n del cliente: S)olicitada Aut. | A)utorizado | N)egado.</li>
 * <li>Indice 3.- Modificaci&oacute;n: S)olicitada Aut. | A)utorizado | N)egado. (No aplica).</li>
 * <li>Indice 4.- Cancelaci&oacute;n: S)olicitada Aut. | A)utorizado | N)egado |
 *              L)Aprobaci&oacute;n tesorer&iacute;a | M)Negaci&oacute;n tesorer&iacute;a.</li>
 * <li>Indice 5.- Facturaci&oacute;n (No aplica).</li>
 * <li>Indice 6.- Banxico Compras (No aplica). </li>
 * <li>Indice 7.- Banxico Ventas (No aplica). </li>
 * <li>Indice 8.- L&iacute;nea de cr&eacute;dito: S)olicitada Aut. | A)utorizado | N)egado | U)so.
 * </li>
 * <li>Indice 9.- Toma en firme: S)olicitada Aut. | A)utorizado | N)egado | U)so.</li>
 * <li>Indice 10, 11.- No. de Timeouts.</li>
 * </ul>
 * <p>Eventos del detalle de deal:</p>
 * <ul>
 * <li>Indice 0.- Documentaci&oacute;n del detalle: S)olicitada Aut. | A)utorizado | N)egado.</li>
 * <li>Indice 1.- Desviaci&oacute;n en el tipo de cambio al cliente: S)olicitada Aut. | A)utorizado
 *              | N)egado.</li>
 * <li>Indice 2.- Afectaci&oacute;n de posici&oacute;n: P)Afectada <espacio>)No afectada.</li>
 * <li>Indice 3.- L&iacute;nea de Cr&eacute;dito: S)olicitada Aut. | A)utorizado | N)egado | U)so.
 * </li>
 * <li>Indice 4.- Toma en firme: S)olicitada Aut. | A)utorizado | N)egado | U)so.</li>
 * <li>Indice 5.- Modificaci&oacute;n (No aplica).</li>
 * </ul>
 *
 * @author Jean C. Favila
 * @version $Revision: 1.27.10.3.6.1 $ $Date: 2011/04/26 02:34:18 $
 * @ejbgen:session ejb-name = SicaTellerSessionEJB
 * type = Stateless
 * initial-beans-in-free-pool = 5
 * max-beans-in-free-pool = 50
 * default-transaction = Required
 * trans-timeout-seconds = 30
 * enable-call-by-reference = true
 * @ejbgen:jndi-name remote = ejb/sica/SicaTellerHome
 * local = ejb/sica/SicaTellerLocalHome
 */
public class SicaTellerSessionBean extends AbstractStatelessSessionBean {

    /**
     * Constructor vac&iacute;o.
     */
    public SicaTellerSessionBean() {
        super();
    }

    /**
     * Cambia los valores por omisi&oacute;n de que archivo tomar como configuraci&oacute;n para
     * Spring.
     * Tambien indica que el BeanFactory ser&aacute; compartido (singleton).
     *
     * @param sessionContext El sessionContext del EJB.
     */
    public void setSessionContext(SessionContext sessionContext) {
        super.setSessionContext(sessionContext);
        setBeanFactoryLocator(ContextSingletonBeanFactoryLocator.getInstance());
        setBeanFactoryLocatorKey("sicaApplicationContext");
    }
    
    /**
     * Sin implementaci&oacute;n alguna.
     *
     * @throws CreateException Si no se puede crear el EJB.
     */
    protected void onEjbCreate() throws CreateException {
    }

    /**
     * 
     * @ejbgen:remote-method transaction-attribute = Required
     * @ejbgen:local-method transaction-attribute = Required
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param noContratoSica El n&uacute;mero de Contrato SICA.
     * @param noCuentaIxe El n&uacute;mero de cuenta de cheques.
     * @param rfc El rfc del cliente.
     * @param nombre El nombre del cliente.
     * @param paterno El apellido paterno.
     * @param materno El apellido materno.
     * @param razonSocial La raz&oacute;n social del cliente.
     * @param idPersonaUsuario El id de persona de la bup del cajero.
     * @return ResultadoOperacionDto.
     */
    public ResultadoOperacionDto getContratoSica(String ticket, String noContratoSica,
                                            String noCuentaIxe, String rfc, String nombre,
                                            String paterno, String materno, String razonSocial,
                                            int idPersonaUsuario, String nacionalidad,
                                            String pasaporte, String ifeFolio,
                                            String ifeAnioRegistro, String ifeOcr,
                                            String ifeNumeroEmision, int idSucursal,
                                            String idDivisa, String claveFormaLiquidacion,
                                            Boolean recibimos, Integer idUsuario) {
    	debug("getContratoSica " + ticket + " " + noContratoSica + " " +
    			noCuentaIxe + " " + rfc + " " + ifeFolio + " " + ifeAnioRegistro + " " + ifeOcr +
    			" " + ifeNumeroEmision + " " + idSucursal + " " + idDivisa + " " +
    			claveFormaLiquidacion + " " + recibimos + " " + pasaporte + " " + nombre + " " +
    			paterno + " " + materno + " " + razonSocial + " " + idPersonaUsuario, null);
        ResultadoOperacionDto resultadoDto;
        try {
        	validarTicket(ticket);
            ContratoSica cs = null;
            Persona persona = null;
            boolean docCompleta = false;
            IUsuario usuario = (IUsuario) getSicaServiceData().find(Usuario.class, idUsuario);
            // Si se proporciona contrato SICA, se busca para ver si existe.
            if (noContratoSica != null) {
                cs = getSicaServiceData().findContrato(noContratoSica);
                persona = getSicaServiceData().findPersonaByContratoSica(noContratoSica);
            }
            // Si no hay contrato sica, y se proporciona cuenta ixe, se localiza esta.
            if (cs == null && noCuentaIxe != null) {
                persona = getSicaServiceData().findTitularCuentaEje(noCuentaIxe);
                cs = getContratoSicaByPersona(persona, idPersonaUsuario);
            }
            // Si no se ha encontrado el contrato, se busca la persona por RFC:
            if (cs == null && rfc != null) {
                if (rfc.trim().length() < Num.I_10 || rfc.trim().length() > Num.I_13) {
                    throw new SicaException("El R.F.C. " + rfc + " no es v\u00e1lido.");
                }
                docCompleta = true;
                persona = getSicaServiceData().findPersonaByRfc(rfc);
                if (persona != null) {
                	cs = getContratoSicaByPersona(persona, idPersonaUsuario);
                }
            }
            Pais pais = null;
            if (nacionalidad != null) {
                pais = ((SiteService) getBeanFactory().getBean("siteService")).
                    getPaisXClaveISO(ticket, nacionalidad);
            }
            if (cs == null && (nacionalidad == null ||
                    pais == null || pais.getIdBupPais() == null)) {
                throw new SicaException("No se puede realizar la operaci\u00f3n, por " +
                    "falta de documentaci\u00f3n requerida en el M\u00f3dulo de " +
                    "Contrataci\u00f3n.\n" + "No se puede identificar la nacionalidad del" +
                    " cliente.");
            }
            // si no se ha encontrado el contrato, se busca la persona por Pasaporte y Nacionalidad
            if (cs == null && pasaporte != null) {
            	docCompleta = true;
            	ComplementoDatos comDat = getSicaServiceData().
            		findComplementoDatosByPasaporteAndIdBupPais(pasaporte, pais.getIdBupPais());
            	if (comDat != null) {
            		cs = getSicaServiceData().findContrato(comDat.getNoCuenta());
            		persona = getSicaServiceData().findPersonaByContratoSica(comDat.getNoCuenta());
            	}
            }
            // si no se ha encontrado el contrato, se busca la persona por IFE
            if (cs == null && pais.getClaveISO().equals("MX") && ifeFolio != null &&
                    ifeAnioRegistro != null && ifeOcr != null && ifeNumeroEmision != null) {
            	docCompleta = true;
            	ComplementoDatos comDat = getSicaServiceData().findComplementoDatosByIfe(ifeFolio);
            	if (comDat != null) {
            		getSicaEsbService().validaCredenciaIfe(FacultySystem.SICA, usuario.getClave(),
            				ticket, ifeFolio, ifeAnioRegistro, ifeNumeroEmision, ifeOcr);
            		cs = getSicaServiceData().findContrato(comDat.getNoCuenta());
                	persona = getSicaServiceData().findPersonaByContratoSica(comDat.getNoCuenta());
            	}
            }
            if ((cs == null) && (!docCompleta)) {
            	throw new SicaException("Debe de proporcionar al menos alguno de los " +
                        "siguientes documentos: R.F.C., pasaporte o " +
                        "los datos completos del IFE. La nacionalidad es requerida en caso " +
                        "de proporcionar pasaporte o IFE.");
            }
            else if ((cs == null) && (docCompleta)) {
                if (pais.getClaveISO().equals("MX") && ifeFolio != null &&
                        ifeAnioRegistro != null && ifeOcr != null && ifeNumeroEmision != null) {
                    getSicaEsbService().validaCredenciaIfe(FacultySystem.SICA, usuario.getClave(),
                        ticket, ifeFolio, ifeAnioRegistro, ifeNumeroEmision, ifeOcr);
                }
            	RegistroClienteContratoService registroService =
                    (RegistroClienteContratoService) getBeanFactory().
                            getBean("registroClienteContratoService");
            	persona = new Persona();
            	persona.setNombreCorto(nombre + " " + paterno + " "+ materno + " " + razonSocial);
            	persona.setIdPersona(new Integer(registroService.registrarPersona(nombre, paterno,
            			materno, razonSocial, rfc, idPersonaUsuario, 1)));
            	cs = getContratoSicaByPersona(persona, idPersonaUsuario);
            }
            resultadoDto = crearResultadoOperacionDto(0, RESULTADO_OK);
            resultadoDto.setObjeto(new ContratoSicaDto(cs.getNoCuenta(),
                    persona.getIdPersona().intValue(),
                    persona.getNombreCompleto() != null ?
                            persona.getNombreCompleto() : persona.getNombreCorto(),
                    (List) getBeneficiariosParaContrato(ticket, cs.getNoCuenta()).getObjeto(),
                    getPlantillaTraNalPorNoCuenta(cs.getNoCuenta()), 
                    getPlantillaNacionalPorNoCuenta(cs.getNoCuenta()), 
                    getPlantillaInternacionalPorNoCuenta(cs.getNoCuenta()), 
                    getPlantillaCuentaIxePorNoCuenta(cs.getNoCuenta()),
                    getLimitesMaximosDeOperacion(cs.getNoCuenta(), idDivisa, claveFormaLiquidacion,
                    		recibimos, persona, pasaporte, ifeFolio, ifeAnioRegistro,
                            ifeNumeroEmision, ifeOcr, idSucursal, nacionalidad, ticket, usuario),
                    getSicaServiceData().isClienteOrUsuario(persona.getIdPersona())));
        }
        catch (SicaException e) {
            warn(e.getMessage(), e);
            resultadoDto = crearResultadoOperacionDto(-1, e.getMessage());
        }
        catch (SeguridadException e) {
        	warn(e.getMessage(), e);
            resultadoDto = crearResultadoOperacionDto(-1, e.getMessage());
        }
        catch (Exception e) {
    		warn("Error no clasificado: " + e.getMessage(), e);
    		resultadoDto = crearResultadoOperacionDto(-1, "Error no clasificado: " + e.getMessage());
    	}
        return resultadoDto;
    }

	/**
     * Regresa un mapa con la informaci&oacute;n del tipo de cambio para la divisa, producto y canal
     * especificados en un DTO [TipoCambioDto].
     *
     *
     * @ejbgen:remote-method transaction-attribute = Required
     * @ejbgen:local-method transaction-attribute = Required
     * @param ticket El ticket de la sesi&oacute;n.
     * @param idDivisa La clave de la divisa.
     * @param claveFormaFormaLiquidacion , idCanal
     * @param idSucursalOriginal El n&uacute;mero de sucursal.
     * @return ResultadoOperacionDto.
     */
    public ResultadoOperacionDto obtenerTipoDeCambioPorDivisa(String ticket, String idDivisa,
                                                      String claveFormaFormaLiquidacion,
                                                      String idSucursalOriginal) {
        ResultadoOperacionDto resultadoDto;
        
        debug("obtenerTipoDeCambioPorDivisa " + ticket + " " + idDivisa, null);
        try {
            validarTicket(ticket);
            if (StringUtils.isEmpty(idDivisa) || StringUtils.isEmpty(claveFormaFormaLiquidacion)) {
                throw new RuntimeException("Favor de proporcionar el idDivisa, " +
                        "claveFormaFormaLiquidacion , idCanal");
            }
            PizarronServiceData pd = getPizarronServiceData();
            Canal canal = getSicaServiceData().findCanalByIdSucursal(idSucursalOriginal);
            if (canal == null) {
                throw new RuntimeException("No se pudo crear el deal debido a que no " +
                        "se encontr\u00f3 un canal para la sucursal " + idSucursalOriginal);
            }
            Divisa d = getSicaServiceData().findDivisa(idDivisa);
            if (d.isFrecuente()) {
                List spreads = getSicaServiceData().
                        findSpreadsActualesByTipoPizarronDivisaFormaLiquidacion(idDivisa,
                                claveFormaFormaLiquidacion, canal.getTipoPizarron());

                if (!spreads.isEmpty()) {
                    SpreadActual sp = (SpreadActual) spreads.get(0);

                    resultadoDto = crearResultadoOperacionDto(0, RESULTADO_OK);
                    resultadoDto.setObjeto(new TipoCambioDto(pd.obtenerTipoCambioPorDivisa(idDivisa,
                            claveFormaFormaLiquidacion, canal.getMesaCambio().getIdMesaCambio(),
                            sp.getIdSpread())));
                    return resultadoDto;
                }
            }
            else {
                resultadoDto = crearResultadoOperacionDto(0, RESULTADO_OK);
                resultadoDto.setObjeto(new TipoCambioDto(pd.obtenerTipoCambioPorDivisa(idDivisa,
                        claveFormaFormaLiquidacion, canal.getMesaCambio().getIdMesaCambio(), 0)));
                return resultadoDto;
            }
            throw new SicaException("No se encontr\u00f3 el spread actual para la divisa " +
                    idDivisa);
        }
        catch (SicaException e) {
            warn(e.getMessage(), e);
            resultadoDto = crearResultadoOperacionDto(-1, e.getMessage());
        }
        catch (SeguridadException e) {
            warn(e.getMessage(), e);
            resultadoDto = crearResultadoOperacionDto(-1, e.getMessage());
        }
    	catch (Exception e) {
    		warn("Error no clasificado: " + e.getMessage(), e);
            resultadoDto = crearResultadoOperacionDto(-1, "Error no clasificado: " + e.getMessage());
    	}
        return resultadoDto;
    }
    
    /**
     * Crea crea y procesa un nuevo deal con detalles.
     * Regresa el resultado de la operaci&oacute;n el cual contiene un mapa con los datos del deal.
     * Keys del mapa: deal[DealDto], eventosDeal[String], detalles[List].
     * 
     * @ejbgen:remote-method transaction-attribute = Required
     * @ejbgen:local-method transaction-attribute = Required
     * @param ticket El ticket de la sesi&oacute;n del Usuario.
     * @param noCuenta El n&uacute;mero de contrato sica.
     * @param idSucursal  El n&uacute;mero de sucursal, seg&uacute;n la bup.
     * @param idUsuario El idUsuario del usuario que captura el deal.
     * @param detalles La lista de detalles del Deal.
     * @param ip La ip del request.
     * @return ResultadoOperacionDto.
     */
    public ResultadoOperacionDto crearDealParaTeller(String ticket, String noCuenta,
                                                     String idSucursal, int idUsuario, 
                                                     List detalles, String ip) {
        debug("*** crearDealParaTeller " + noCuenta + " " + idUsuario, null);
        if (detalles.size() < 1) {
        	throw new SicaException("El deal no cuenta con detalles para poder crearlo");
        }
        ResultadoOperacionDto resultado;
        SicaServiceData sd = getSicaServiceData();
        Deal deal = new Deal();
        List resultadosDetalles = new ArrayList();
        try {
        	validarTicket(ticket);
            //*** Comienza creacion del Deal.
            Estado edoActual = validarEstadoSistema();

            if (isVespertinoNocturno()) {
                deal.setEvento(Deal.EV_VESPERTINO, Deal.EV_IND_TIPO_CAPTURA);                
    		}
    		switch (edoActual.getIdEstado()) {
    		case Estado.ESTADO_OPERACION_VESPERTINA:
    			deal.setEvento(Deal.EV_VESPERTINO, Deal.EV_IND_TIPO_CAPTURA);
    			break;
    		case Estado.ESTADO_OPERACION_NOCTURNA:
    			deal.setEvento(Deal.EV_NOCTURNO, Deal.EV_IND_TIPO_CAPTURA);
    			break;
    		}
    		
    		Canal canal = sd.findCanalByIdSucursal(idSucursal);
    		if (canal == null) {
    			throw new SicaException("No se pudo crear el deal debido a que no se " +
    					"encontr\u00f3 un canal para la sucursal " + idSucursal);
    		}
    		deal.setCanalMesa(new CanalMesa(canal, canal.getMesaCambio()));
    		ContratoSica cs = sd.findContrato(noCuenta);
    		if (cs == null) {
    			throw new SicaException("No se pudo crear el deal debido a que no se " +
    					"encontr\u00f3 el contrato sica n\u00fam. " + noCuenta);
            }
            deal.setContratoSica(cs);
            EmpleadoIxe promotor = getSicaServiceData().findPromotorByContratoSica(noCuenta);
            deal.setPromotor(promotor);
            deal.setTipoDeal(detalles.size() == 1 ? "S" : "C");
            IUsuario usuario = (IUsuario) getSicaServiceData().findUsuarioById(new Integer(idUsuario));
            deal.setUsuario(usuario);
            if (deal.isNocturno() && deal.haySolAut()) {
                throw new SicaException("No se puede crear el deal pues el sistema se encuentra " +
                        "en estado nocturno y se requerir\u00eda pedir una o m\u00e1s " +
                        "autorizaciones.");
            }
            deal.setTipoValor(Constantes.CASH);
            //Termina Creacion del deal
            
            //Guardamos el deal
            Date[] fechasVal = getFechasValor();
            if (deal.getFechaLiquidacion() == null) {
                String tpVal = deal.getTipoValor();
                deal.setFechaLiquidacion(tpVal.equals(Constantes.VFUT) ? fechasVal[Num.I_4] :
                        tpVal.equals(Constantes.HR72) ? fechasVal[Num.I_3] :
                                tpVal.equals(Constantes.SPOT) ? fechasVal[2] :
                                        tpVal.equals(Constantes.TOM) ?
                                        		fechasVal[1] : fechasVal[0]);
            }
            sd.storeDeal(deal);
            deal = sd.findDeal(deal.getIdDeal());
            //Termina Guardar deal
            
            //*** Comienza creacion de los detalles
            int count = 0;

            for (Iterator iterator = detalles.iterator(); iterator.hasNext();) {
                DealDetalleDto detDto = (DealDetalleDto) iterator.next();
                detDto = agregarDetalleDeal(ticket, deal, detDto, usuario, ip);
                resultadosDetalles.add(detDto);
                count++;
    		}
    		//Termina creacion de los detalles

    		//*** Comienza procesamiento de Deal.
    		procesarDeal(ticket, deal.getIdDeal());
    		resultado = crearResultadoOperacionDto(0, RESULTADO_OK);

    	}
    	catch (SicaException e) {
    		warn(e.getMessage(), e);
    		return crearResultadoOperacionDto(-1, e.getMessage());
    	}
    	catch (SeguridadException se) {
    		warn(se.getMessage(), se);
    		return crearResultadoOperacionDto(-1, se.getMessage());
    	}
    	catch (Exception e) {
    		warn("Error no clasificado: " + e.getMessage(), e);
    		return crearResultadoOperacionDto(-1, "Error no clasificado: " + e.getMessage());
    	}
    	DealDto dealDto = getDealDto(deal);
    	dealDto.setDetalles(resultadosDetalles);
    	resultado.setObjeto(dealDto);
    	return resultado;
    }
    
    /**
     * Crea e inserta en la base de datos una nueva plantilla de cuentas ixe, con la
     * informaci&oacute;n proporcionada. Regresa en el mapa de resultados, la llave idPlantilla con
     * el n&uacute;mero de plantilla que le fue asignado.
     *
     * @ejbgen:remote-method transaction-attribute = Required
     * @ejbgen:local-method transaction-attribute = Required
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param noCuenta El n&uacute;mero de contrato SICA del cliente.
     * @param mnemonico El mnem&oacute;nico a asignar a la plantilla.
     * @param clavePlantilla Una clave de hasta 4 caracteres para identificar la plantilla
     *      (opcional).
     * @param idBeneficiario El idPersona del beneficiario de la plantilla.
     * @param noCuentaIxe El n&uacute;mero de cuenta ixe.
     * @return ResultadoOperacionDto.
     */
    public ResultadoOperacionDto crearPlantillaCuentaIxe(String ticket, String noCuenta,
                                                         String mnemonico, String clavePlantilla,
                                                         int idBeneficiario, String noCuentaIxe) {
        try {
        	debug("crearPlantillaCuentaIxe " + ticket + " " + noCuenta, null);
            validarTicket(ticket);
            SicaServiceData sd = getSicaServiceData();
            sd.validaCuentaEje(noCuentaIxe);
            IPlantillaCuentaIxe plantilla = new PlantillaCuentaIxe();
            llenarPlantilla(plantilla, noCuenta, mnemonico, clavePlantilla, idBeneficiario);
            plantilla.setNoCuentaIxe(noCuentaIxe);
            sd.store(plantilla);
            ResultadoOperacionDto resultadoDto = crearResultadoOperacionDto(0, RESULTADO_OK);
            resultadoDto.setObjeto(new Integer(plantilla.getIdPlantilla()));
            return resultadoDto;
        }
        catch (SeguridadException e) {
        	
        	warn(e.getMessage(), e);
            return crearResultadoOperacionDto(-1, e.getMessage());
        }
        catch (SicaException e) {
        	warn(e.getMessage(), e);
            return crearResultadoOperacionDto(-1, e.getMessage());
        }
    	catch (Exception e) {
    		warn("Error no clasificado: " + e.getMessage(), e);
    		return crearResultadoOperacionDto(-1, "Error no clasificado: " + e.getMessage());
    	}
    }
    
    /**
     * Crea e inserta en la base de datos una nueva plantilla de beneficiarios, con la
     * informaci&oacute;n proporcionada. Regresa en el mapa de resultados, la llave idPlantilla con
     * el n&uacute;mero de plantilla que le fue asignado.
     *
     * @ejbgen:remote-method transaction-attribute = Required
     * @ejbgen:local-method transaction-attribute = Required
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param noCuenta El n&uacute;mero de contrato SICA del cliente.
     * @param mnemonico El mnem&oacute;nico a asignar a la plantilla.
     * @param clavePlantilla Una clave de hasta 4 caracteres para identificar la plantilla (opc.).
     * @param idBeneficiario El idPersona del beneficiario de la plantilla.
     * @return ResultadoOperacionDto.
     */
    public ResultadoOperacionDto crearPlantillaNacional(String ticket, String noCuenta,
                                                        String mnemonico, String clavePlantilla,
                                                        int idBeneficiario) {
    	debug("crearPlantillaNacional " + ticket + " " + noCuenta, null);
        try {
            validarTicket(ticket);
            IPlantillaNacional plantilla = new PlantillaNacional();
            SicaServiceData sd = getSicaServiceData();
            llenarPlantilla(plantilla, noCuenta, mnemonico, clavePlantilla, idBeneficiario);
            sd.store(plantilla);
            ResultadoOperacionDto resultado = crearResultadoOperacionDto(0, RESULTADO_OK);
            resultado.setObjeto(new Integer(plantilla.getIdPlantilla()));
            return resultado;
        }
        catch (SeguridadException e) {
        	warn(e.getMessage(), e);
            return crearResultadoOperacionDto(-1, e.getMessage());
        }
        catch (SicaException e) {
        	warn(e.getMessage(), e);
            return crearResultadoOperacionDto(-1, e.getMessage());
        }
    	catch (Exception e) {
    		warn("Error no clasificado: " + e.getMessage(), e);
    		return crearResultadoOperacionDto(-1, "Error no clasificado: " + e.getMessage());
    	}
    }
    
    /**
     * Crea e inserta en la base de datos una nueva plantilla de cuentas internacional con
     * intermediario, con la informaci&oacute;n proporcionada. Regresa en el mapa de resultados, la
     * llave idPlantilla con el n&uacute;mero de plantilla que le fue asignado.
     *
     * @ejbgen:remote-method transaction-attribute = Required
     * @ejbgen:local-method transaction-attribute = Required
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param noCuenta El n&uacute;mero de contrato SICA del cliente.
     * @param mnemonico El mnem&oacute;nico a asignar a la plantilla.
     * @param clavePlantilla Una clave de hasta 4 caracteres para identificar la plantilla (opc.).
     * @param idBeneficiario El idPersona del beneficiario de la plantilla.
     * @param idDivisa La divisa para la plantilla.
     * @param noCuentaBeneficiario El n&uacute;mero de cuenta del beneficiario.
     * @param tipoCuentaBanco El tipo de sistema utilizado ABA | SWFT | CHIP.
     * @param claveBanco La clave ABA, SWIFT o CHIPS del banco.
     * @param nombreBanco El nombre del banco al que va la transferencia.
     * @param claveIsoPais La clave del pa&iacute;s del banco beneficiario.
     * @param tipoCuentaBancoInterm El tipo de sistema utilizado ABA | SWFT | CHIP para el banco
     *          intermediario.
     * @param claveBancoInterm La clave ABA, SWIFT o CHIPS del banco intermediario
     * @param nombreBancoInterm El nombre del banco intermediario de la transferencia.
     * @param cuentaBancoInterm La cuenta conocida del banco intermediario.
     * @param instruccionesBeneficiario Las istrucciones para el beneficiario (opcional)
     * @param instruccionesIntermediario Las instrucciones para el intermediario (opcional)
     * @return ResultadoOperacionDto.
     */
    public ResultadoOperacionDto crearPlantillaInternacional(String ticket,
                                                             String mnemonico,
                                                             String noCuenta,
                                                             String noCuentaBeneficiario,
                                                             int idBeneficiario,
                                                             String idDivisa,
                                                             String tipoCuentaBanco,
                                                             String claveBanco,
                                                             String nombreBanco,
                                                             String claveIsoPais,
                                                             String clavePlantilla,
                                                             String tipoCuentaBancoInterm,
                                                             String claveBancoInterm,
                                                             String nombreBancoInterm,
                                                             String cuentaBancoInterm,
                                                             String instruccionesBeneficiario,
                                                             String instruccionesIntermediario) {
        debug("*** crearPlantillaInternacional 11" + ticket + " " + noCuenta, null);
        try {
        	boolean bancoIntermediario;
        	validarTicket(ticket);
        	if (tipoCuentaBancoInterm != null && claveBancoInterm != null && 
        			nombreBancoInterm != null && cuentaBancoInterm != null &&
        			instruccionesBeneficiario != null && instruccionesIntermediario != null) {
        		bancoIntermediario = true;
        	} else {
        		bancoIntermediario = false;
        	}
            IPlantillaIntl plantilla = llenarPlantillaInternacional(noCuenta, mnemonico,
                    clavePlantilla, idBeneficiario, idDivisa, noCuentaBeneficiario, tipoCuentaBanco,
                    claveBanco, nombreBanco, claveIsoPais);
            if (bancoIntermediario) {
	            plantilla.setTipoCuentaBancoInterm(tipoCuentaBancoInterm);
	            plantilla.setClaveBancoInterm(claveBancoInterm);
	            plantilla.setCuentaBancoInterm(cuentaBancoInterm);
	            plantilla.setNombreBancoInterm(nombreBancoInterm);
	            plantilla.setInstrBcoBeneficiario(instruccionesBeneficiario);
	            plantilla.setInstrBcoIntermediario(instruccionesIntermediario);
            }
            getSicaServiceData().store(plantilla);
            ResultadoOperacionDto resultado = crearResultadoOperacionDto(0, RESULTADO_OK);
            resultado.setObjeto(new Integer(plantilla.getIdPlantilla()));
            return resultado;
        }
        catch (SeguridadException e) {
        	warn(e.getMessage(), e);
            return crearResultadoOperacionDto(-1, e.getMessage());
        }
        catch (SicaException e) {
        	warn(e.getMessage(), e);
            return crearResultadoOperacionDto(-1, e.getMessage());
        }
    	catch (Exception e) {
    		warn("Error no clasificado: " + e.getMessage(), e);
    		return crearResultadoOperacionDto(-1, "Error no clasificado: " + e.getMessage());
    	}
    }

    /**
     * Regresa la informaci&oacute;n del banco internacional que tiene los datos proporcionados en
     * la llave 'banco' del mapa resultado.
     *
     * @ejbgen:remote-method transaction-attribute = Required
     * @ejbgen:local-method transaction-attribute = Required
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param tipoCuentaBanco El tipo de cuenta (ABA | SWFT o CHIP).
     * @param claveBanco La clave ABA, SWIFT o CHIP del banco.
     * @param idDivisa La clave de la divisa para validar el tipoCuentaBanco.
     * @return ResultadoOperacionDto.
     */
    public ResultadoOperacionDto getBancoInternacionalByClave(String ticket, String tipoCuentaBanco,
                                                       String claveBanco, String idDivisa) {
        debug("*** getBancoInternacional " + ticket +  " " + claveBanco, null);
        try {
        	validarTicket(ticket);
            Banco banco = ((SicaSiteCache) getBeanFactory().getBean("sicaSiteCache")).
                    encontrarBanco(ticket, tipoCuentaBanco, claveBanco, idDivisa);
            if (banco == null) {
                throw new SicaException("No se encontr\u00f3 el banco especificado.");
            }
            BancoInternacionalDto bancoDto = new BancoInternacionalDto(banco.getClaveABA(),
                    banco.getClaveBanxico(), banco.getClaveCHIPS(), banco.getClaveRuteoPais(),
                    banco.getClaveSWIFT(), banco.getEsBancoPequeno().booleanValue(),
                    banco.getEstado(), banco.getIdBanco(), banco.getIdBancoPhoenix(),
                    banco.getIdPais(), banco.getNombreCorto(), banco.getNombrePais(),
                    banco.getNumeroRuteoPais(), banco.getPlaza(), banco.getStatus());
            ResultadoOperacionDto resultado = crearResultadoOperacionDto(0, RESULTADO_OK);
            resultado.setObjeto(bancoDto);
            return resultado;
        }
        catch (SicaException e) {
        	warn(e.getMessage(), e);
            return crearResultadoOperacionDto(-1, e.getMessage());
        }
        catch (SeguridadException se) {
        	warn(se.getMessage(), se);
            return crearResultadoOperacionDto(-1, se.getMessage());
        }
    	catch (Exception e) {
    		warn("Error no clasificado: " + e.getMessage(), e);
    		return crearResultadoOperacionDto(-1, "Error no clasificado: " + e.getMessage());
    	}
    }

    /**
     * Regresa en la llave 'bancos' del mapa resultado. la informaci&oacute;n de los bancos
     * internacionales que tiene el nombre y clave de pa&iacute;s especificados.
     *
     * @ejbgen:remote-method transaction-attribute = Required
     * @ejbgen:local-method transaction-attribute = Required
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param nombreBanco El inicio del nombre del banco
     * @param clavePais La clave iso del pa&iacute;s a donde pertenece el banco.
     * @return ResultadoOperacionDto.
     */
    public ResultadoOperacionDto getBancosInternacionalesByNombrePais(String ticket,
                                                                      String nombreBanco,
                                                                      String clavePais) {
        debug("*** getBancosInternacionalesByNombrePais " + ticket +  " " + nombreBanco, null);
        try {
            validarTicket(ticket);
            List bancos = getSicaServiceData().findBancoInternacionalByNombreAndPais(nombreBanco,
                    clavePais);
            List resultados = new ArrayList();
            int count = 0;
            for (Iterator it = bancos.iterator(); it.hasNext(); count++) {
                Object[] banco = (Object[]) it.next();
                BancoInternacionalDto bancoDto = new BancoInternacionalDto(null, null,
                        (String) banco[Num.I_6], (String) banco[Num.I_3], (String) banco[Num.I_5],
                        false, (String) banco[Num.I_2],
                        banco[Num.I_9] != null ? new Long((banco[Num.I_9]).toString()) : null,
                        null, (String) banco[Num.I_8], (String) banco[Num.I_0],
                        (String) banco[Num.I_1], (String) banco[Num.I_4], (String) banco[Num.I_2],
                        (String) banco[Num.I_7]);
                resultados.add(bancoDto);
            }
            ResultadoOperacionDto resultado = crearResultadoOperacionDto(0, RESULTADO_OK);
            resultado.setObjeto(resultados);
            return resultado;
        }
        catch (SeguridadException e) {
        	
        	warn(e.getMessage(), e);
            return crearResultadoOperacionDto(-1, e.getMessage());
        }
        catch (SicaException e) {
        	
        	warn(e.getMessage(), e);
            return crearResultadoOperacionDto(-1, e.getMessage());
        }
    	catch (Exception e) {
    		warn("Error no clasificado: " + e.getMessage(), e);
    		return crearResultadoOperacionDto(-1, "Error no clasificado: " + e.getMessage());
    	}
    }
    
    /**
     * Permite consultar bancos internacionales por criterios variables a partir de un map:
     * <li>tipoClave.- que puede ser 'SWIFT', 'CHIPS'.</li>
     * <li>claveSwift.- para el caso en que tipoClave es 'SWIFT' se necesitara esta clave.</li>
     * <li>claveChips.- para el caso en que tipoClave es 'CHIPS' se necesitara esta clave.</li>
     * <li>claveRuteo.- si tipo clave no es 'SWIFT' o 'CHIPS' se debe indicar la clave ruteo que
     *  puede ser 'ABA', 'NIB', 'BLZ', ETC....</li>
     * <li>numeroRuteo.- si tipo clave no es 'SWIFT' o 'CHIPS' se debe indicar el numero ruteo
     *  correspondiente a la clave ruteo enviada.</li>
     * <li>nombreBanco.- nombre del banco.</li>
     * <li>pais.- pais.</li>
     * <li>ciudad.- ciudad.</li>
     * @ejbgen:remote-method transaction-attribute = Required
     * @ejbgen:local-method transaction-attribute = Required
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param parametros Los parametros de b&uacute;squeda.
     * @return ResultadoOperacionDto.
     */
    public ResultadoOperacionDto getBancosInternacionales(String ticket, Map parametros) {
        SiteService siteService = (SiteService) getBeanFactory().getBean("siteService");
        debug("*** getBancosInternacionales " + ticket +  " " + parametros, null);
        try {
        	validarTicket(ticket);
            List bancos = siteService.findBancosInternacionales(ticket, parametros);
            if (bancos.isEmpty()) {
                throw new SicaException("No se encontraron bancos con los criterios " +
                        "especificados.");
            }
            List resultados = new ArrayList();
            for (Iterator it = bancos.iterator(); it.hasNext();) {
                Banco banco = (Banco) it.next();
                BancoInternacionalDto bancoDto = new BancoInternacionalDto(banco.getClaveABA(),
                        banco.getClaveBanxico(), banco.getClaveCHIPS(), banco.getClaveRuteoPais(),
                        banco.getClaveSWIFT(), banco.getEsBancoPequeno().booleanValue(),
                        banco.getEstado(), banco.getIdBanco(), banco.getIdBancoPhoenix(),
                        banco.getIdPais(), banco.getNombreCorto(), banco.getNombrePais(),
                        banco.getNumeroRuteoPais(), banco.getPlaza(), banco.getStatus());
                resultados.add(bancoDto);
            }
            ResultadoOperacionDto resultado = crearResultadoOperacionDto(0, RESULTADO_OK);
            resultado.setObjeto(resultados);
            return resultado;
        }
        catch (SeguridadException e) {
        	warn(e.getMessage(), e);
            return crearResultadoOperacionDto(-1, e.getMessage());
        }
        catch (ExternalSiteException e) {
        	warn(e.getMessage(), e);
            return crearResultadoOperacionDto(-1, e.getMessage());
        }
        catch (SicaException e) {
        	warn(e.getMessage(), e);
            return crearResultadoOperacionDto(-1, e.getMessage());
        }
    	catch (Exception e) {
    		warn("Error no clasificado: " + e.getMessage(), e);
    		return crearResultadoOperacionDto(-1, "Error no clasificado: " + e.getMessage());
    	}
    }
    
    /**
     * Regresa las divisas de acuerdo al cat&aacute;logo que cuenta
     * el SICA
     *
     * @ejbgen:remote-method transaction-attribute = Required
     * @ejbgen:local-method transaction-attribute = Required
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param tipoDivisa El tipo de divisa que se quiere buscar: }
     * 		  F) Frecuente / N) No Frecuente / M) Metal Amonendado / T) Todas 
     * @return ResultadoOperacionDto.
     */
    public ResultadoOperacionDto getDivisas(String ticket, String tipoDivisa) {
        
    	debug("**** get Divisas " + tipoDivisa, null);
    	ResultadoOperacionDto resultado;
    	try {
	    	validarTicket(ticket);
	        List div;
	        if (FRECUENTE.equals(tipoDivisa)) {
	        	div = getDivisasFrecuentes();
	        }
	        else if (NO_FRECUENTE.equals(tipoDivisa)) {
	        	div = getDivisasNoFrecuentes(false); 
	        }
	        else if (METAL_AMONEDADO.equals(tipoDivisa)) {
	        	div = getDivisasNoFrecuentes(true);
	        }
	        else if (TODAS.equals(tipoDivisa)) {
	            
	        	div = getDivisasFrecuentes();
	            div.addAll(getDivisasNoFrecuentes(false));
	            div.addAll(getDivisasNoFrecuentes(true));
	        }
	        else {
	            return crearResultadoOperacionDto(-1, "Tipo de divisa erroneo - El tipo de " +
	                    "divisa solo puede ser F) Frecuente / N) No Frecuente / M) Metal Amonendado " +
	                    "/ T) Todas");
	        }
	        resultado = crearResultadoOperacionDto(0, RESULTADO_OK);
	        resultado.setObjeto(div);
    	}
    	catch (SicaException e) {
        	warn(e.getMessage(), e);
        	resultado = crearResultadoOperacionDto(-1, e.getMessage());
    	}
    	catch (SeguridadException e) {
        	warn(e.getMessage(), e);
        	resultado = crearResultadoOperacionDto(-1, e.getMessage());
    	}
    	catch (Exception e) {
    		warn("Error no clasificado: " + e.getMessage(), e);
    		return crearResultadoOperacionDto(-1, "Error no clasificado: " + e.getMessage());
    	}
        return resultado;
    }
    
    /**
     * Regresa la lista de DTOs<FormaPagoLiqDto> de las formas de pago y liquidaci&oacute;n.
     *
     * @ejbgen:remote-method transaction-attribute = Required
     * @ejbgen:local-method transaction-attribute = Required
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @return ResultadoOperacionDto.
     */
    public ResultadoOperacionDto getFormasPagoLiq(String ticket) {
    	debug("*** getMapasFormasPagoLiq " + ticket, null);
        List resultados = new ArrayList();
        try {
            validarTicket(ticket);
            List fpls = ((SicaSiteCache) getBeanFactory().getBean("sicaSiteCache")).
                    obtenerFormasPagoLiq(ticket);
            for (Iterator it = fpls.iterator(); it.hasNext();) {
                FormaPagoLiq fpl = (FormaPagoLiq) it.next();

                if (fpl.getDesplegableTeller() != null &&
                        fpl.getDesplegableTeller().booleanValue()) {
                    FormaPagoLiqDto dto = new FormaPagoLiqDto(fpl.getClaveFormaLiquidacion(),
                            fpl.getComision(), fpl.getDescripcion(), fpl.getIdBanco(),
                            fpl.getIdDivisa(), fpl.getMnemonico(), fpl.getMontoMaximo(),
                            fpl.getMontoMinimo(), fpl.getMultiplo(), fpl.getNombreBanco(),
                            fpl.getOperacionMinima(),
                            fpl.getRecibimos() != null && fpl.getRecibimos().booleanValue(),
                            fpl.getUsaPlantilla() != null && fpl.getUsaPlantilla().booleanValue());
                    
                    resultados.add(dto);
	            }
	        }
	        ResultadoOperacionDto resultado = crearResultadoOperacionDto(0, RESULTADO_OK);
            resultado.setObjeto(resultados);
            return resultado;
        }
        catch (SeguridadException e) {
        	warn(e.getMessage(), e);
            return crearResultadoOperacionDto(-1, e.getMessage());
		}
    	catch (Exception e) {
    		warn("Error no clasificado: " + e.getMessage(), e);
    		return crearResultadoOperacionDto(-1, "Error no clasificado: " + e.getMessage());
    	}
    }
    
    /**
     * Regresa en la llave 'paises' del mapa resultado, la lista de paises existentes.
     *
     * @ejbgen:remote-method transaction-attribute = Required
     * @ejbgen:local-method transaction-attribute = Required
     * @param ticket El ticket de la sesi&oacute;n.
     * @return ResultadoOperacionDto.
     */
    public ResultadoOperacionDto getPaises(String ticket) {
        debug("*** getPaises " + ticket, null);
        try {
            validarTicket(ticket);
            List paises = ((SiteService) getBeanFactory().getBean("siteService")).getPaises(ticket);
            List mapasPaises = new ArrayList();
            for (Iterator it = paises.iterator(); it.hasNext();) {
                Pais pais = (Pais) it.next();
                Map paisMap = new HashMap();
                paisMap.put("claveISO", pais.getClaveISO());
                paisMap.put("nombrePais", pais.getNombrePais());
                mapasPaises.add(paisMap);
            }
            ResultadoOperacionDto resultado = crearResultadoOperacionDto(0, RESULTADO_OK);
            resultado.setObjeto(mapasPaises);
            return resultado;
        }
        catch (SeguridadException e) {
        	warn(e.getMessage(), e);
            return crearResultadoOperacionDto(-1, e.getMessage());
        }
        catch (ExternalSiteException e) {
        	warn(e.getMessage(), e);
            return crearResultadoOperacionDto(-1, e.getMessage());
        }
    	catch (Exception e) {
    		warn("Error no clasificado: " + e.getMessage(), e);
    		return crearResultadoOperacionDto(-1, "Error no clasificado: " + e.getMessage());
    	}
    }
    
    /**
     * Arranca el proceso de cancelaci&oacute;n de un deal del Teller. Si el deal no tiene un
     * detalle de transferencia extranjera registrada en el SITE, se puede cancelar directamente.
     * En caso contrario, el &acute;rea de tesorer&iacute;a debe autorizar la cancelaci&oacute;n.
     *
     * @ejbgen:remote-method transaction-attribute = Required
     * @ejbgen:local-method transaction-attribute = Required
     * @param ticket El ticket de la sesi&oacute;n.
     * @param idDeal El n&uacute;mero del deal a cancelar.
     * @param idUsuario El n&uacute;mero de usuario de la sesi&oacute;n.
     * @return ResultadoOperacionDto.
     */
    public ResultadoOperacionDto marcarDealCancelado(String ticket, int idDeal, int idUsuario) {
        debug("*** marcarDealCancelado " + ticket + " " + idDeal + " " + idUsuario, null);
        try {
            validarTicket(ticket);
            WorkFlowServiceData wfsd = (WorkFlowServiceData) getBeanFactory().
                    getBean("sicaWorkFlowServiceData");
            Deal deal = wfsd.findDealConCanal(idDeal);
            if (!deal.isDeSucursal()) {
                throw new SicaException("El deal " + idDeal + " no puede ser cancelado por esta " +
                        "v&iacute;a, debido a que no corresponde a sucursales.");
            }
            int transferenciasEnSite = 0;
            // Revisamos el n&uacute;mero de transferencias que est&aacute;n registradas en el SITE:
            for (Iterator it = deal.getDetalles().iterator(); it.hasNext();) {
                DealDetalle det = (DealDetalle) it.next();
                if (Constantes.TRANSFERENCIA.equals(det.getClaveFormaLiquidacion()) && !(
                        det.isReversadoProcesoReverso() || det.isCancelado()) &&
                        det.getIdLiquidacionDetalle() != null) {
                    transferenciasEnSite++;
                }
            }
            // Si no hay transferencias en el SITE, podemos cancelar directamente.
            if (transferenciasEnSite == 0) {
            	wfsd.wfCancelarDirectamenteDeal(ticket, idDeal, idUsuario);
            }
            else {
                IUsuario usuario = (IUsuario) getSicaServiceData().find(Usuario.class,
                        new Integer(idUsuario));
                wfsd.wfSolicitarCancelacionDeal(ticket, idDeal, usuario);
            }
            // regresar mensaje de OK
            return crearResultadoOperacionDto(0, RESULTADO_OK);
            
        }
        catch (SeguridadException e) {
        	warn(e.getMessage(), e);
            return crearResultadoOperacionDto(-1, e.getMessage());
        }
        catch (SicaException e) {
        	warn(e.getMessage(), e);
            return crearResultadoOperacionDto(-1, e.getMessage());
        }
    	catch (Exception e) {
    		warn("Error no clasificado: " + e.getMessage(), e);
    		return crearResultadoOperacionDto(-1, "Error no clasificado: " + e.getMessage());
    	}
    }
    
    /**
     * Permite el registro de beneficiario para un contrato SICA.
     * 
     * @ejbgen:remote-method transaction-attribute = Required
     * @ejbgen:local-method transaction-attribute = Required 
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param noContratoSica El n&uacute;mero de Contrato SICA.
     * @param idBeneficiario El n&uacute;mero de Persona del Beneficiario.
     * @return ResultadoOperacionDto.
     */
    public ResultadoOperacionDto registrarBeneficiarioParaContratoSica(String ticket,
                                                                       String noContratoSica,
                                                                       int idBeneficiario,
                                                                       int idCliente,
                                                                       int idUsuarioTeller) {
    	ResultadoOperacionDto resultadoDto;
    	debug("registrarBeneficiarioParaContratoSica " + ticket + " " + noContratoSica + 
    			" " + idBeneficiario + " " + idCliente + " " + idUsuarioTeller, null);
    	try {
    		validarTicket(ticket);
    		ContratoSica cs = null;
    		Persona beneficiario = null;
    		
    		if (noContratoSica == null) {
        		throw new SicaException("Por favor proporcione el contrato SICA");
        	}
	    	cs = getSicaServiceData().findContrato(noContratoSica);
	    	if (cs == null) {
                throw new SicaException("No se puedo registrar el Beneficiario para " +
                        "la cuenta SICA debido a que no se encotr\u00f3 el contrato SICA " +
                        noContratoSica);
            }

            beneficiario = (Persona) getSicaServiceData().find(
                    Persona.class, new Integer(idBeneficiario));
            if (beneficiario == null) {
                throw new SicaException("No se puedo registrar el Beneficiario para la cuenta " +
                        "SICA debido a que no se encotr\u00f3 el Beneficiario con el ID: " +
                        idBeneficiario);
            }
            com.ixe.bean.Usuario usrTeller = getSicaBusquedaPersonaService().
                    obtenEmpleado(idUsuarioTeller);
            RelacionCuentaPersona relacionCuentaPersona = new RelacionCuentaPersona();
            relacionCuentaPersona.setIdPersona(beneficiario.getIdPersona().intValue());
            relacionCuentaPersona.setCuenta(getSicaConsultaProductosPersonaService().
                    obtenCuentaContrato(cs.getNoCuenta()));
            relacionCuentaPersona.setIdRol(FacultySystem.BENEFICIARIO_SICA_ROL);
            getSicaRegistroRelacionCuentaPersonaService().
                    registraBeneficiarioTrans(relacionCuentaPersona, idCliente, usrTeller);
	    	//Agregar mensaje de OK
	    	resultadoDto = crearResultadoOperacionDto(0, RESULTADO_OK);
    	}
        catch (SicaException e) {
    		warn(e.getMessage(), e);
    		resultadoDto = crearResultadoOperacionDto(-1, e.getMessage());
    	}
        catch (Exception e) {
    		warn("Error no clasificado: " + e.getMessage(), e);
            resultadoDto = crearResultadoOperacionDto(-1, "Error no clasificado: " + e.getMessage());
    	}
    	
    	return resultadoDto;
    }
    
    /**
     * Crea ResultadoOperacionDto con el resultado de la operaci&oacute;n y el codigo al que
     * hace referencia (error, confirmaci&oacute;n, etc.).
     *
     * @param codigo El codigo de la acci&oacute;n.
     * @param descripcion La descripcion de la acci&oacute;n
     * @return ResultadoOperacionDto..
     */
    private ResultadoOperacionDto crearResultadoOperacionDto(int codigo, String descripcion) {
    	return new ResultadoOperacionDto(codigo, descripcion);
    }

    /**
     * Regresa el Contrato SICA asignado a la persona especificada. Si no encuentra uno, se solicita
     * al m&oacute;dulo de Contrataci&oacute;n su creaci&oacute;n en la BUP.
     *
     * @param persona La persona a buscar.
     * @param idPersonaUsuario El identificador del usuario en la bup.
     * @return ContratoSica.
     */
    private ContratoSica getContratoSicaByPersona(Persona persona, int idPersonaUsuario) {
        ContratoSica cs = getSicaServiceData().findContratoSicaByIdPersona(persona.getIdPersona());

        // Si no se encuentra el contrato, se registra en la BUP:
        if (cs == null) {
            try {
                RegistroClienteContratoService registroService = (RegistroClienteContratoService)
                        getBeanFactory().getBean("registroClienteContratoService");
                String noContratoSica = registroService.registrarContratoSica(
                        persona.getIdPersona().intValue(), idPersonaUsuario);

                cs = getSicaServiceData().findContrato(noContratoSica);
            }
            catch (ContratacionException e) {
            	warn(e.getMessage(), e);
            	
                throw new SicaException(e.getMessage());
            }
        }
        return cs;
    }

    /**
     * Define si un detalle de deal tiene desviaci&oacute;n en el monto del tipo de cambio para el
     * cliente.
     *
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param recibimos True para compra, false para venta.
     * @param idDivisa La clave de la divisa, seg&uacute;n la tabla SC_DIVISA.
     * @param claveFormaLiquidacion La clave del producto.
     * @param tipoCambio El tipo de cambio al cliente.
     * @param canal El canal de la sucursal que est&aacute; operando.
     * @return boolean.
     */
    private boolean isDetalleDesviadoTcc(String ticket, boolean recibimos, String idDivisa,
                                         String claveFormaLiquidacion, double tipoCambio,
                                         Canal canal) {
        TipoCambioDto tc = (TipoCambioDto) obtenerTipoDeCambioPorDivisa(ticket, idDivisa,
                claveFormaLiquidacion, canal.getSucursal().getIdOriginal()).getObjeto();
        
        double minimo = Redondeador.redondear6Dec((recibimos ?
                        tc.getMinimoCompra() : tc.getMaximoVenta()).doubleValue());
        double maximo = Redondeador.redondear6Dec((recibimos ?
                        tc.getMaximoCompra() : tc.getMaximoVenta()).doubleValue());
        return tipoCambio < minimo || tipoCambio > maximo;
    }

    /**
     * 
     * <p>Crea y graba un nuevo detalle de deal. El nuevo detalle afecta la posici&oacute;n
     * inmediatamente, a menos que se requiera una autorizaci&oacute;n por monto. El mapa de retorno
     * contiene informaci&oacute;n sobre las formas de pago y liquidaci&oacute;n aplicables, y las
     * plantillas registradas con anterioridad para &eacute;stas.
     * 
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param deal El modelo de hibernate del deal.
     * @param detalleDto El detalle a agregar.
     * @param usuario El usuario que realiza la operaci&oacute;n.
     * @param ip La direcci&oacute;n IP del usuario que realiza la operaci&oacute;n.
     * @return DealDetalleDto.
     */
    private DealDetalleDto agregarDetalleDeal(String ticket, Deal deal, DealDetalleDto detalleDto,
    		IUsuario usuario, String ip) {

        debug("*** agregarDetalleDeal " + deal + " " + detalleDto.isRecibimos() + " CASH " +
                detalleDto.getClaveFormaLiquidacion(), null);

        Estado estadoActual = validarEstadoSistema();
        SicaServiceData sd = getSicaServiceData();
        TokenTeller tt = (TokenTeller) sd.findTokenTellerById(detalleDto.getToken());
        if (!tt.isVigente()) {
            throw new SicaException("La vigencia del token ha caducado.\nFavor de solicitar " +
                    "un nuevo tipo de cambio. " + tt.getIdTokenTeller());
        }
        Divisa div = sd.findDivisa(detalleDto.getIdDivisa());
        boolean montoExcedido = sd.montoMaximoExcedido(deal.getCanalMesa().getMesaCambio().
                getIdMesaCambio(), div, detalleDto.getMonto().doubleValue());
        DealService dealService = (DealService) getBeanFactory().getBean("dealService");
        
        DealDetalle det = dealService.crearDealDetalle(deal,
                detalleDto.isRecibimos(), Constantes.CASH,
                detalleDto.getClaveFormaLiquidacion(), div, detalleDto.getTcm().doubleValue(),
                detalleDto.getMonto().doubleValue(), detalleDto.getTcc().doubleValue(),
                detalleDto.getPrecioReferenciaMidSpot(), detalleDto.getPrecioReferenciaSpot(), detalleDto.getFactorDivisa(),
                detalleDto.getIdPrecioReferencia(), detalleDto.getIdFactorDivisa(),
                detalleDto.getIdSpread().intValue(), null, getFechasValor(),
                montoExcedido, estadoActual, getLimitesActualizados(deal, usuario), ticket, ip,
                usuario);
        /*boolean requiereSobreprecio = false;
        if (!detalleDto.isALaPar()) {
			requiereSobreprecio = validarSobreprecio(ticket, det, estadoActual);
		}*/
		// Si solo hay un mnemonico disponible, se asigna y se calcula la comision:
		List fpls = getFormasPagoLiqAplicablesParaDetalle(ticket, det.isRecibimos(),
				det.getDivisa().getIdDivisa(), det.getClaveFormaLiquidacion());
		if (fpls.size() == 1) {
            FormaPagoLiqDto mapaFpl = (FormaPagoLiqDto) fpls.get(0);
            det.setMnemonico(mapaFpl.getMnemonico());
            Double comision = mapaFpl.getComision();
            if (comision != null && comision.doubleValue() > 0) {
                det.setComisionOficialUsd(comision.doubleValue());
                sd.calcularComisionMxn(det, detalleDto.getPrecioReferenciaSpot().doubleValue());
            }
        }
        // No puede haber solicitudes de autorizacion si el deal se capturo en horario nocturno:
        if (deal.isNocturno() && det.haySolAut()) {
            throw new SicaException("No se puede crear el deal pues el sistema se encuentra " +
                    "en estado nocturno y se requerir\u00eda pedir una o m\u00e1s " +
                    "autorizaciones.");
        }
        validarMontoMaximo(det);
		sd.storeDeal(det.getDeal());
		if (detalleDto.getIdPlantilla() != null){
			asignarPlantillaADealDetalle(detalleDto.getIdPlantilla().intValue(), det);
		}
		detalleDto.setFolioDetalle(new Integer(det.getFolioDetalle()));
		//TODO: Aun por definir logica
		/*
		if (requiereSobreprecio) {
            getSicaServiceData().generarAutorizacionSobreprecioSucursales(deal);
            deal.setEvento(Deal.EV_SOLICITUD, Deal.EV_IND_SOBREPRECIO);
            sd.update(deal);
        }
        */
		detalleDto.setEventosDetalle(det.getEventosDetalleDeal());
		return detalleDto;
    	
    }
    
    /**
     * Revisa si el detalle de deal est&aacute; desviado y valida el estado actual del sistema.
     *
     * @param ticket El ticket de la sesi&oacute;n
     * @param det El detalle de deal a validar.
     * @param estadoActual El estado actual del sistema.
     * @return boolean.
     */
    private boolean validarSobreprecio(String ticket, DealDetalle det, Estado estadoActual) {
        boolean requiereSobreprecio = isDetalleDesviadoTcc(ticket, det.isRecibimos(),
                det.getDivisa().getIdDivisa(), det.getClaveFormaLiquidacion(),
                det.getTipoCambio(), det.getDeal().getCanalMesa().getCanal());
        if (requiereSobreprecio) {
            if (!(Estado.ESTADO_OPERACION_NORMAL == estadoActual.getIdEstado() ||
                    Estado.ESTADO_OPERACION_RESTRINGIDA == estadoActual.getIdEstado())) {
                throw new SicaException("No se permiten desviaciones al tipo de cambio " +
                        "durante los horarios de operaci\u00f3n vespertina o nocturna");
            }
        }
        return requiereSobreprecio;
    }

    /**
     * Valida el monto m&aacute;ximo operado por la sucursal durante los horarios vespertino y
     * nocturno.
     *
     * @param det El detalle de deal a comparar.
     */
    private void validarMontoMaximo(DealDetalle det) {
        SicaServiceData sd = getSicaServiceData();

        if (isVespertinoNocturno()) {
            sd.actualizarMontoOperadoSucursal(det.getDeal().getCanalMesa().getCanal().getIdCanal(),
                    det.getDivisa().getIdDivisa(), det.getClaveFormaLiquidacion(),
                    det.isRecibimos(), new BigDecimal("" + det.getMonto()));
        }
    }
    
    /**
     * Regresa los l&iacute;mites de restricci&oacute;n de operaci&oacute;n del contrato sica
     * que se encuentra operando.
     *
     * @param deal El deal a operar.
     * @param usuario El usuario que realiza la operaci&oacute;n.
     * @return LimitesRestriccionOperacion
     */
    private LimitesRestriccionOperacion getLimitesActualizados(Deal deal, IUsuario usuario) {
    	String noCuenta = deal.getContratoSica() != null ? deal.getContratoSica().getNoCuenta() :
    		null;
    	if (noCuenta == null) {
    		return null;
    	}
    	ComplementoDatos complDatos = getSicaServiceData().findComplementoDatosByNoCuenta(noCuenta);
    	LimitesRestriccionOperacion limRestOper = new LimitesRestriccionOperacion();
    	if (complDatos == null) {
    		if (deal.getCliente().getPais() == null) {
    			throw new SicaException("No se puede realizar la operaci\u00f3n, por " +
    					"falta de documentaci\u00f3n requerida en el M\u00f3dulo de " +
    					"Contrataci\u00f3n.\n" + "No se puede identificar la nacionalidad del" +
    					" cliente.");
    		}
    		complDatos = new ComplementoDatos(noCuenta, deal.getCliente().getPais().getIdPais(),
    				null, null, null, null, null, getSicaServiceData().isClienteOrUsuario(deal.
    						getCliente().getIdPersona()) ? SiNoType.TRUE : SiNoType.FALSE,
    								new Date(), new Date(), usuario);
    		getSicaServiceData().store(complDatos);
    	}
    		String codigoPostalCliente = getContratoDireccionesService().
    				getDireccionFiscalParaPersona(deal.getCliente().getIdPersona().intValue()).
    				getCodigoPostal();
    	if (codigoPostalCliente == null || codigoPostalCliente.equals("")) {
    		throw new SicaException("No se puede realizar la operaci\u00f3n, por " +
    				"falta de documentaci\u00f3n requerida en el M\u00f3dulo de " +
    				"Contrataci\u00f3n.\n" + "El cliente no cuenta con un c\u00f3digo postal" +
    				" asociado.");
    	}
    	String codigoPostalSucursal = "";
    	if (!(deal.getCanalMesa().getCanal().getSucursal() != null && deal.getCanalMesa().
    			getCanal().getSucursal().getAddress() != null && (deal.getCanalMesa().
    					getCanal().getSucursal().getAddress().getCodigoPostal() != null ||
    					!deal.getCanalMesa().getCanal().getSucursal().getAddress().
    					getCodigoPostal().equals("")))) {
    		codigoPostalSucursal = deal.getCanalMesa().getCanal().getCodigoPostal();
    	}
    	else {
    		codigoPostalSucursal = deal.getCanalMesa().getCanal().getSucursal().getAddress().
						getCodigoPostal();
    	}
    	if (codigoPostalSucursal == null || codigoPostalSucursal.equals("")) {
    		throw new SicaException("No se puede realizar la operaci\u00f3n, por " +
    				"falta de documentaci\u00f3n requerida en el M\u00f3dulo de " +
    				"Contrataci\u00f3n.\n" + "La sucursal no cuenta con un c\u00f3digo postal" +
    				" asociado.");
    	}
    	CodigoPostalListaBlanca codPostLBCliente = getSicaServiceData().
    								findCodigoPostalListaBlanca(codigoPostalCliente, true);
    	CodigoPostalListaBlanca codPostLBSucursal = getSicaServiceData().
    								findCodigoPostalListaBlanca(codigoPostalSucursal, false);
        boolean zonaTuristica = false;
    	boolean zonaFronteriza = false;
    	if (codPostLBCliente != null && codPostLBSucursal != null &&
                 !codPostLBCliente.equals("") && !codPostLBSucursal.equals("")) {
               zonaTuristica = codPostLBCliente.getZonaTuristica().booleanValue() &&
                    codPostLBSucursal.getZonaTuristica().booleanValue();
                zonaFronteriza = codPostLBCliente.getZonaFronteriza().booleanValue() &&
    					codPostLBSucursal.getZonaFronteriza().booleanValue();
    	}
    	limRestOper.setNoCuenta(complDatos.getNoCuenta());
        // Se obtiene la lista para las zonas que aplique
        String zona = Constantes.ZONA_NORMAL;
    	if (!zonaTuristica && ! zonaFronteriza) {
    	// Zona Normal
    	limRestOper.setListaLimitesNormales(getSicaServiceData().
                    findLimitesRestriccionOperacion(deal.getCliente().getTipoPersona(),
                            new Boolean(complDatos.getCliente().equals(SiNoType.TRUE)),
                            Constantes.ZONA_NORMAL,
                            new Boolean(complDatos.getNacionalidad().equals(String.
                                    valueOf(Num.I_48)))));
        } else {
            List listaZonaTuristica = null;
            List listaZonaFronteriza = null;
            if (zonaTuristica) {
                // Zona Turistica
                listaZonaTuristica =
                    getSicaServiceData().
                        findLimitesRestriccionOperacion(
                            deal.getCliente().getTipoPersona(),
                            new Boolean(complDatos.getCliente().equals(SiNoType.TRUE)),
                            Constantes.ZONA_TURISTICA,
                            new Boolean(complDatos.getNacionalidad().equals(String.
                                    valueOf(Num.I_48))));
        }
        if (zonaFronteriza) {
             // Zona Fronteriza
             listaZonaFronteriza =
                getSicaServiceData().
                     findLimitesRestriccionOperacion(
                         deal.getCliente().getTipoPersona(),
                         new Boolean(complDatos.getCliente().equals(SiNoType.TRUE)),
                         Constantes.ZONA_FRONTERIZA,
                         new Boolean(complDatos.getNacionalidad().equals(String.
                                 valueOf(Num.I_48))));
        }
        if (listaZonaTuristica != null && listaZonaFronteriza != null) {
                // Se toma el limite mayor.
                for (Iterator i = listaZonaTuristica.iterator(); i.hasNext(); ) {
                    LimiteEfectivo limiteTur = (LimiteEfectivo) i.next();
                    for (Iterator j = listaZonaFronteriza.iterator(); j.hasNext(); ) {
                         LimiteEfectivo limiteFro = (LimiteEfectivo) i.next();
                         if (limiteTur.getDivisa().equals(limiteFro.getDivisa())) {
                             if (limiteTur.getLimiteMensual().doubleValue() >
                                     limiteFro.getLimiteMensual().doubleValue()) {
                            	 	 // Zona turistica es mayor
                                    limRestOper.setListaLimitesNormales(listaZonaTuristica);
                                    zona = Constantes.ZONA_TURISTICA;
                             }
                             else if (limiteTur.getLimiteMensual().doubleValue() <
                                      limiteFro.getLimiteMensual().doubleValue()) {
                                    // zona fronteriza es mayor
                                    limRestOper.setListaLimitesNormales(listaZonaFronteriza);
                                    zona = Constantes.ZONA_FRONTERIZA;
                             }
                         }
                    }
                }
                if (limRestOper.getListaLimitesNormales() == null) {
                    // Si todas las reglas son iguales, se toma zona turistica
                    limRestOper.setListaLimitesNormales(listaZonaTuristica);
                    zona = Constantes.ZONA_TURISTICA;
                }
        }
        else if (listaZonaTuristica != null) {
            // Se toma el limite de zona turistica
            limRestOper.setListaLimitesNormales(listaZonaTuristica);
            zona = Constantes.ZONA_TURISTICA;
        }
        else if (listaZonaFronteriza != null) {
            // Se toma el limite se zona fronteriza
            limRestOper.setListaLimitesNormales(listaZonaFronteriza);
            zona = Constantes.ZONA_FRONTERIZA;
        }
        }
    	limRestOper.setLimiteDiarioIxeCpra(0);
    	limRestOper.setLimiteMensualIxeCpra(0);
    	limRestOper.setTipoExcepcion(PersonaListaBlanca.SIN_EXCEPCION);
    	PersonaListaBlanca plb = getSicaServiceData().findPersonaListaBlanca(noCuenta);
    	if (plb != null) {
    		limRestOper.setLimiteDiarioIxeCpra(plb.getLimiteDiario().doubleValue());
    		limRestOper.setLimiteMensualIxeCpra(plb.getLimiteMensual().doubleValue());
    		limRestOper.setTipoExcepcion(plb.getTipoExcepcion());
    	}
    	deal.setTipoZona(zona);
    	deal.setTipoExcepcion(limRestOper.getTipoExcepcion());
    	deal.setEsCliente(complDatos.getCliente());
    	return limRestOper;
    }
    
    /**
     * Regresa los l&iacute;mites de restricci&oacute;n de operaci&oacute;n del contrato sica
     * que se encuentra operando.
     *
     * @param noCuenta El n&uacute;mero de cuenta del contrato sica.
     * @param persona
     * @param noPasaporte El n&uacute;mero del pasaporte enviado desde el Teller.
     * @param ifeFolio El n&uacute;mero del folio de la credencial del IFE enviado desde el Teller.
     * @param ifeAnioRegistro El anio de registro del IFE enviado desde el Teller.
     * @param ifeNumeroEmision El n&uacute;mero de emisi&oacute;n de la credencial del IFE enviado
     * desde el Teller.
     * @param ifeOcr El n&uacute;mero del ocr del IFE enviado desde el Teller.
     * @param idSucursal El id de la sucursal enviado desde el Teller.
     * @param claveIsoPais La clave iso del pa&iacute;s enviado desde el Teller.
     * @param ticket El ticket.
     * @param usuario El usuario operando.
     * @return
     */
    private LimitesRestriccionOperacion getLimitesActualizados(String noCuenta, Persona persona,
    		String noPasaporte, String ifeFolio, String ifeAnioRegistro, String ifeNumeroEmision,
    		String ifeOcr, int idSucursal, String claveIsoPais, String ticket, IUsuario usuario) {
    	if (noCuenta == null) {
    		return null;
    	}
    	ComplementoDatos complDatos = getSicaServiceData().findComplementoDatosByNoCuenta(noCuenta);
    	LimitesRestriccionOperacion limRestOper = new LimitesRestriccionOperacion();
    	if (complDatos == null) {
    		Pais pais = ((SiteService) getBeanFactory().getBean("siteService")).
    				getPaisXClaveISO(ticket, claveIsoPais);
    		if (pais == null || pais.getIdBupPais() == null) {
    			throw new SicaException("No se puede realizar la operaci\u00f3n, por " +
    					"falta de documentaci\u00f3n requerida en el M\u00f3dulo de " +
    					"Contrataci\u00f3n.\n" + "No se puede identificar la nacionalidad del" +
    					" cliente.");
    		}
    		complDatos = new ComplementoDatos(noCuenta, pais.getIdBupPais(),
    				noPasaporte, ifeFolio, ifeAnioRegistro, ifeNumeroEmision, ifeOcr,
    				getSicaServiceData().isClienteOrUsuario(persona.getIdPersona()) ?
    						SiNoType.TRUE : SiNoType.FALSE, new Date(), new Date(), usuario);
    		getSicaServiceData().store(complDatos);
    	}
    	String codigoPostalCliente = getContratoDireccionesService().
    				getDireccionFiscalParaPersona(persona.getIdPersona().intValue()).
    				getCodigoPostal();
    	if (codigoPostalCliente == null || codigoPostalCliente.equals("")) {
    		throw new SicaException("No se puede realizar la operaci\u00f3n, por " +
    				"falta de documentaci\u00f3n requerida en el M\u00f3dulo de " +
    				"Contrataci\u00f3n.\n" + "El cliente no cuenta con un c\u00f3digo postal" +
    				" asociado.");
    	}
    	String codigoPostalSucursal = "";
    	Canal canal = getSicaServiceData().findCanalByIdSucursal(String.valueOf(idSucursal));
    	if (!(canal.getSucursal() != null && canal.getSucursal().getAddress() != null && (canal.
    			getSucursal().getAddress().getCodigoPostal() != null ||
    					!canal.getSucursal().getAddress().
    					getCodigoPostal().equals("")))) {
    		codigoPostalSucursal = canal.getCodigoPostal();
    	}
    	else {
    		codigoPostalSucursal = canal.getSucursal().getAddress().getCodigoPostal();
    	}
    	if (codigoPostalSucursal == null || codigoPostalSucursal.equals("")) {
    		throw new SicaException("No se puede realizar la operaci\u00f3n, por " +
    				"falta de documentaci\u00f3n requerida en el M\u00f3dulo de " +
    				"Contrataci\u00f3n.\n" + "La sucursal no cuenta con un c\u00f3digo postal" +
    				" asociado.");
    	}
    	CodigoPostalListaBlanca codPostLBCliente = getSicaServiceData().
    								findCodigoPostalListaBlanca(codigoPostalCliente, true);
    	CodigoPostalListaBlanca codPostLBSucursal = getSicaServiceData().
    								findCodigoPostalListaBlanca(codigoPostalSucursal, false);
        boolean zonaTuristica = false;
    	boolean zonaFronteriza = false;
    	if (codPostLBCliente != null && codPostLBSucursal != null &&
                 !codPostLBCliente.equals("") && !codPostLBSucursal.equals("")) {
               zonaTuristica = codPostLBCliente.getZonaTuristica().booleanValue() &&
                    codPostLBSucursal.getZonaTuristica().booleanValue();
                zonaFronteriza = codPostLBCliente.getZonaFronteriza().booleanValue() &&
    					codPostLBSucursal.getZonaFronteriza().booleanValue();
    	}
    	limRestOper.setNoCuenta(complDatos.getNoCuenta());
        // Se obtiene la lista para las zonas que aplique
    	if (!zonaTuristica && ! zonaFronteriza) {
    	// Zona Normal
    	limRestOper.setListaLimitesNormales(getSicaServiceData().
                    findLimitesRestriccionOperacion(persona.getTipoPersona(),
                            new Boolean(complDatos.getCliente().equals(SiNoType.TRUE)),
                            Constantes.ZONA_NORMAL,
                            new Boolean(complDatos.getNacionalidad().equals(String.
                                    valueOf(Num.I_48)))));
        } else {
            List listaZonaTuristica = null;
            List listaZonaFronteriza = null;
            if (zonaTuristica) {
                // Zona Turistica
                listaZonaTuristica =
                    getSicaServiceData().
                        findLimitesRestriccionOperacion(persona.getTipoPersona(),
                            new Boolean(complDatos.getCliente().equals(SiNoType.TRUE)),
                            Constantes.ZONA_TURISTICA,
                            new Boolean(complDatos.getNacionalidad().equals(String.
                                    valueOf(Num.I_48))));
        }
        if (zonaFronteriza) {
             // Zona Fronteriza
             listaZonaFronteriza =
                getSicaServiceData().
                     findLimitesRestriccionOperacion(persona.getTipoPersona(),
                         new Boolean(complDatos.getCliente().equals(SiNoType.TRUE)),
                         Constantes.ZONA_FRONTERIZA,
                         new Boolean(complDatos.getNacionalidad().equals(String.
                                 valueOf(Num.I_48))));
        }
        if (listaZonaTuristica != null && listaZonaFronteriza != null) {
                // Se toma el limite mayor.
                for (Iterator i = listaZonaTuristica.iterator(); i.hasNext(); ) {
                    LimiteEfectivo limiteTur = (LimiteEfectivo) i.next();
                    for (Iterator j = listaZonaFronteriza.iterator(); j.hasNext(); ) {
                         LimiteEfectivo limiteFro = (LimiteEfectivo) i.next();
                         if (limiteTur.getDivisa().equals(limiteFro.getDivisa())) {
                             if (limiteTur.getLimiteMensual().doubleValue() >
                                     limiteFro.getLimiteMensual().doubleValue()) {
                            	 	 // Zona turistica es mayor
                                    limRestOper.setListaLimitesNormales(listaZonaTuristica);
                             }
                             else if (limiteTur.getLimiteMensual().doubleValue() <
                                      limiteFro.getLimiteMensual().doubleValue()) {
                                    // zona fronteriza es mayor
                                    limRestOper.setListaLimitesNormales(listaZonaFronteriza);
                             }
                         }
                    }
                }
                if (limRestOper.getListaLimitesNormales() == null) {
                    // Si todas las reglas son iguales, se toma zona turistica
                    limRestOper.setListaLimitesNormales(listaZonaTuristica);
                }
        }
        else if (listaZonaTuristica != null) {
            // Se toma el limite de zona turistica
            limRestOper.setListaLimitesNormales(listaZonaTuristica);
        }
        else if (listaZonaFronteriza != null) {
            // Se toma el limite se zona fronteriza
            limRestOper.setListaLimitesNormales(listaZonaFronteriza);
        }
        }
    	limRestOper.setLimiteDiarioIxeCpra(0);
    	limRestOper.setLimiteMensualIxeCpra(0);
    	limRestOper.setTipoExcepcion(PersonaListaBlanca.SIN_EXCEPCION);
    	PersonaListaBlanca plb = getSicaServiceData().findPersonaListaBlanca(noCuenta);
    	if (plb != null) {
    		limRestOper.setLimiteDiarioIxeCpra(plb.getLimiteDiario().doubleValue());
    		limRestOper.setLimiteMensualIxeCpra(plb.getLimiteMensual().doubleValue());
    		limRestOper.setTipoExcepcion(plb.getTipoExcepcion());
    	}
    	return limRestOper;
    }

    /**
     * Asigna las instrucciones al detalle de deal correspondiente. El detalle debe ser una venta de
     * transferencia.
     *
     * @param ticket El ticket de la sesi&oacute;n.
     * @param idDealPosicion El identificador del detalle del deal.
     * @param instruccionesBeneficiario Las instrucciones al beneficiario de la transferencia
     *          (opcional).
     * @param instruccionesIntermediario Las instrucciones al banco intermediario (opcional).
     * @param instruccionesPagador Las instrucciones al banco pagador (o banco beneficiario).
     * @return ResultadoOperacionDto.
     */
    public ResultadoOperacionDto asignarInstruccionesADealDetalle(String ticket, int idDealPosicion,
                                                String instruccionesBeneficiario,
                                                String instruccionesIntermediario,
                                                String instruccionesPagador) {
        ResultadoOperacionDto resultadoDto;

        try {
            debug("*** asignarInstruccionesADealDetalle " + ticket +  " " + idDealPosicion, null);
            validarTicket(ticket);
            SicaServiceData sd = getSicaServiceData();
            DealDetalle dealDetalle = sd.findDealDetalle(idDealPosicion);
            if (!(Constantes.TRANSFERENCIA.equals(dealDetalle.getClaveFormaLiquidacion())) &&
                    !dealDetalle.isRecibimos()) {
                throw new SicaException("Esta operaci\u00f3n s\u00f3lo est\u00e1 disponible para " +
                        "Venta de Transferencia.");
            }
            dealDetalle.setInstruccionesBeneficiario(instruccionesBeneficiario);
            dealDetalle.setInstruccionesIntermediario(instruccionesIntermediario);
            dealDetalle.setInstruccionesPagador(instruccionesPagador);
            sd.update(dealDetalle);
            resultadoDto = crearResultadoOperacionDto(0, RESULTADO_OK);
            Map resultado = new HashMap();
            resultado.put("renglonesIntermediario", instruccionesIntermediario);
            resultado.put("instruccionesIntermediarioValidas", instruccionesIntermediario);
            resultado.put("renglonesPagador", instruccionesPagador);
            resultado.put("instruccionesPagadorValidas", instruccionesPagador);
            resultado.put("renglonesBeneficiario", instruccionesBeneficiario);
            resultado.put("instruccionesBeneficiarioValidas", instruccionesBeneficiario);
            resultadoDto.setObjeto(resultado);
        }
        catch (SeguridadException e) {
        	
            warn(e.getMessage(), e);
            resultadoDto = crearResultadoOperacionDto(-1, e.getMessage());
        }
        catch (SicaException e) {
        	
            warn(e.getMessage(), e);
            resultadoDto = crearResultadoOperacionDto(-1, e.getMessage());
        }
        return resultadoDto;
    }

    /**
     * Permite asignar un mnem&oacute;nico a un detalle de deal. El detalle no debe tener un
     * mnem&oacute;nico ya asignado. Debe utilizarse s&oacute;lo en los casos en que ese
     * mnem&oacute;nico no tenga una plantilla relacionada.
     * 
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param mnemonico el mnem&oacute;nico a asignar.
     * @param idDealPosicion el identificador del detalle del deal.
     * @return ResultadoOperacionDto.
     */
    public ResultadoOperacionDto asignarMnemonicoADealDetalle(String ticket, String mnemonico,
                                                              int idDealPosicion) {
        debug("asignarMnemonicoADealDetalle " + ticket +  " " + idDealPosicion, null);
        try {
            validarTicket(ticket);
            SicaServiceData sd = getSicaServiceData();
            DealDetalle dealDetalle = sd.findDealDetalle(idDealPosicion);
            if (dealDetalle.getMnemonico() != null) {
                throw new SicaException("El detalle ya tiene un mnem\u00f3nico asignado.");
            }
            dealDetalle.setMnemonico(mnemonico);
            sd.update(dealDetalle);
            return crearResultadoOperacionDto(0, RESULTADO_OK);
        }
        catch (SeguridadException e) {
        	
        	warn(e.getMessage(), e);
            return crearResultadoOperacionDto(-1, e.getMessage());
        }
        catch (SicaException e) {
        	
        	warn(e.getMessage(), e);
            return crearResultadoOperacionDto(-1, e.getMessage());
        }
    }

    /**
     * Permite ligar una plantilla a un detalle de deal. El detalle no debe tener una plantilla ya
     * asignada. El mnem&oacute;nico de la plantilla debe corresponder al del detalle (si es que
     * est&aacute; definido). El contrato de la plantilla debe ser el mismo que el contrato del
     * deal.
     *
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param idPlantilla el identificador de la plantilla.
     * @param idDealPosicion el identificador del detalle del deal.
     * @return ResultadoOperacionDto.
     */
    public ResultadoOperacionDto asignarPlantillaADealDetalle(String ticket, int idPlantilla,
                                                              int idDealPosicion) {
        try {
            debug("asignarPlantillaADealDetalle " + ticket +  " " + idDealPosicion, null);
            validarTicket(ticket);
            SicaServiceData sd = getSicaServiceData();
            DealDetalle dealDetalle = sd.findDealDetalle(idDealPosicion);
            if (dealDetalle.getPlantilla() != null) {
                throw new SicaException("El detalle ya tiene una plantilla asignada.");
            }
            Plantilla plantilla = (Plantilla) sd.find(Plantilla.class, new Integer(idPlantilla));
            if (! plantilla.getContratoSica().getNoCuenta().equals(
                    dealDetalle.getDeal().getContratoSica().getNoCuenta())) {
                throw new SicaException("El contrato de la plantilla no corresponde al contrato " +
                        "del deal.");
            }
            if (dealDetalle.getMnemonico() == null) {
                dealDetalle.setMnemonico(plantilla.getMnemonico());
            }
            else if (! plantilla.getMnemonico().equals(dealDetalle.getMnemonico())) {
                throw new SicaException("El mnem\u00f3nico de la plantilla no corresponde al del " +
                        "detalle del deal.");
            }
            dealDetalle.setPlantilla(plantilla);
            sd.update(dealDetalle);
            return crearResultadoOperacionDto(0, RESULTADO_OK);
        }
        catch (SeguridadException e) {
        	
        	warn(e.getMessage(), e);
            return crearResultadoOperacionDto(-1, e.getMessage());
        }
        catch (SicaException e) {
        	
        	warn(e.getMessage(), e);
            return crearResultadoOperacionDto(-1, e.getMessage());
        }
    }
    
    /**
     * Permite ligar una plantilla a un detalle de deal. El detalle no debe tener una plantilla ya
     * asignada. El mnem&oacute;nico de la plantilla debe corresponder al del detalle (si es que
     * est&aacute; definido). El contrato de la plantilla debe ser el mismo que el contrato del
     * deal.
     *
     * @param idPlantilla El ID de la plantilla a asignar
     * @param dealDetalle El DealDetalla a ligar.
     */
    private void asignarPlantillaADealDetalle(int idPlantilla, DealDetalle dealDetalle) {
        SicaServiceData sd = getSicaServiceData();

        debug("asignarPlantillaADealDetalle " + idPlantilla + " " + dealDetalle, null);
        if (dealDetalle.getPlantilla() != null) {
            throw new SicaException("El detalle ya tiene una plantilla asignada.");
        }
        Plantilla plantilla = (Plantilla) sd.find(Plantilla.class, new Integer(idPlantilla));
        if (!plantilla.getContratoSica().getNoCuenta().equals(
                dealDetalle.getDeal().getContratoSica().getNoCuenta())) {
            throw new SicaException("El contrato de la plantilla no corresponde al contrato " +
                    "del deal.");
        }
        if (dealDetalle.getMnemonico() == null) {
            dealDetalle.setMnemonico(plantilla.getMnemonico());
        }
        else if (!plantilla.getMnemonico().equals(dealDetalle.getMnemonico())) {
            throw new SicaException("El mnem\u00f3nico de la plantilla no corresponde al del " +
                    "detalle del deal.");
        }
        dealDetalle.setPlantilla(plantilla);
        sd.update(dealDetalle);
    }

    /**
     * Regresa la informaci&oacute;n de los beneficiarios en un mapa en la llave beneficiarios, 
     * la lista de mapas con la informaci&oacute;n de los beneficiarios del contrato sica
     * especificados. 
     *
     * Cada mapa contiene dos llaves: idPersona y nombreCompleto.
     *
     * @param ticket El ticket de la sesi&oacute;n del Usuario.
     * @param noCuenta El n&uacute;mero de contrato sica.
     * @return List de PersonaDto.
     */
    public ResultadoOperacionDto getBeneficiariosParaContrato(String ticket, String noCuenta) {
        ResultadoOperacionDto resultadoDto = crearResultadoOperacionDto(0, RESULTADO_OK);

        try {
            debug("obtenerBeneficiariosParaContrato " + ticket + " " + noCuenta, null);
            validarTicket(ticket);
            List beneficiarios = getSicaServiceData().
                    findBeneficiariosByNoCuentaByIdRol(noCuenta, "BENT");
            List resultados = new ArrayList();
            
            for (Iterator it = beneficiarios.iterator(); it.hasNext();) {
                Persona benef = (Persona) it.next();
                PersonaDto dto = new PersonaDto(benef.getIdPersona().intValue(),
                        benef.getNombreCompleto());

                resultados.add(dto);
            }
            resultadoDto.setObjeto(resultados);
        }
        catch (SicaException e) {
        	
        	warn(e.getMessage(), e);
            resultadoDto = crearResultadoOperacionDto(-1, e.getMessage());
        }
        catch (Exception e) {
        	
        	warn(e.getMessage(), e);
            resultadoDto = crearResultadoOperacionDto(-1, "Error no clasificado: " +
                    e.getMessage());
        }
        return resultadoDto;
    }
    
    /**
     * Crea e inserta en la base de datos una nueva plantilla de cuentas internacional sin
     * intermediario, con la informaci&oacute;n proporcionada. Regresa en el mapa de resultados, la
     * llave idPlantilla con el n&uacute;mero de plantilla que le fue asignado.
     *
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param noCuenta El n&uacute;mero de contrato SICA del cliente.
     * @param mnemonico El mnem&oacute;nico a asignar a la plantilla.
     * @param clavePlantilla Una clave de hasta 4 caracteres para identificar la plantilla (opc.).
     * @param idBeneficiario El idPersona del beneficiario de la plantilla.
     * @param idDivisa La divisa para la plantilla.
     * @param noCuentaBeneficiario El n&uacute;mero de cuenta del beneficiario.
     * @param tipoCuentaBanco El tipo de sistema utilizado ABA | SWFT | CHIP.
     * @param claveBanco La clave ABA, SWIFT o CHIPS del banco.
     * @param nombreBanco El nombre del banco al que va la transferencia.
     * @param claveIsoPais La clave del pa&iacute;s del banco beneficiario.
     * @return ResultadoOperacionDto.
     */
    public ResultadoOperacionDto crearPlantillaInternacional(String ticket, String noCuenta,
                                                             String mnemonico,
                                                             String clavePlantilla,
                                                             int idBeneficiario,
                                                             String idDivisa,
                                                             String noCuentaBeneficiario,
                                                             String tipoCuentaBanco,
                                                             String claveBanco, 
                                                             String nombreBanco,
                                                             String claveIsoPais) {
        try {
        	debug("crearPlantillaCuentaIxe " + ticket + " " + noCuenta, null);
            validarTicket(ticket);
            IPlantillaIntl plantilla = llenarPlantillaInternacional(noCuenta, mnemonico,
                    clavePlantilla, idBeneficiario, idDivisa, noCuentaBeneficiario, tipoCuentaBanco,
                    claveBanco, nombreBanco, claveIsoPais);
            getSicaServiceData().store(plantilla);
            ResultadoOperacionDto resultado = crearResultadoOperacionDto(0, RESULTADO_OK);
            resultado.setObjeto(new Integer(plantilla.getIdPlantilla()));
            return resultado;
        }
        catch (SeguridadException e) {
        	
        	warn(e.getMessage(), e);
            return crearResultadoOperacionDto(-1, e.getMessage());
        }
    }

    /**
     * Regresa el canal sica que corresponde a la sucursal proporcionada en la llave 'canal' del
     * mapa resultado.
     *
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param idSucursalOriginal El n&uacute;mero de sucursal, seg&uacute;n la bup.
     * @return ResultadoOperacionDto.
     */
    public ResultadoOperacionDto getCanalByIdSucursal(String ticket, String idSucursalOriginal) {
    	debug("*** getCanalByIdSucursal " + ticket +  " " + idSucursalOriginal, null);
        try {
            validarTicket(ticket);
            Canal canal = getSicaServiceData().findCanalByIdSucursal(idSucursalOriginal);
            if (canal == null) {
                throw new SicaException("No se encontr\u00f3 el canal " + idSucursalOriginal);
            }
            Map mapaCanal = new HashMap();
            mapaCanal.put("idCanal", canal.getIdCanal());
            mapaCanal.put("idTipoPizarron", canal.getTipoPizarron().getIdTipoPizarron());
            mapaCanal.put("nombre", canal.getNombre());
            mapaCanal.put("idMesaCambio", new Integer(canal.getMesaCambio().getIdMesaCambio()));
            ResultadoOperacionDto resultado = crearResultadoOperacionDto(0, RESULTADO_OK);
            resultado.setObjeto(mapaCanal);
            return resultado;
        }
        catch (SeguridadException e) {
        	
        	warn(e.getMessage(), e);
            return crearResultadoOperacionDto(-1, e.getMessage());
        }
        catch (SicaException e) {
        	
        	warn(e.getMessage(), e);
            return crearResultadoOperacionDto(-1, e.getMessage());
        }
    }

    /**
     * Encuentra el deal con el n&uacute;mero proporcionado como un DTO, contenido toda su
     * informaci&oacute;n, incluyendo los detalles y sus plantillas asignadas.
     *
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param idDeal El n&uacute;mero de deal que se desea encontrar.
     * @return ResultadoOperacionDto.
     */
    public ResultadoOperacionDto getDeal(String ticket, int idDeal) {
        debug("*** getDeal " + ticket +  " " + idDeal, null);
        try {
            validarTicket(ticket);
            DealDto dealDto = getDealDto(getSicaServiceData().findDealConCanal(idDeal));
            ResultadoOperacionDto resultado = crearResultadoOperacionDto(0, RESULTADO_OK);
            resultado.setObjeto(dealDto);
            return resultado;
        }
        catch (SeguridadException e) {
        	
        	warn(e.getMessage(), e);
            return crearResultadoOperacionDto(-1, e.getMessage());
        }
    }

    /**
     * Obtiene los atributos de un deal y los almacena en un mapa.
     *
     * @param deal El deal.
     * @return DealDto
     */
    private DealDto getDealDto(Deal deal) {
        return new DealDto(deal.isCompra(), new Integer(deal.getCanalMesa().getMesaCambio().
                getIdMesaCambio()), deal.getCanalMesa().getCanal().getIdCanal(),
                deal.getContratoSica().getNoCuenta(), deal.getEventosDeal(), deal.getFechaCaptura(),
                deal.getFechaLiquidacion(), new Integer(deal.getIdDeal()), deal.getIdLiquidacion(),
                deal.getObservaciones(), deal.getPromotor().getIdPersona(), deal.getStatusDeal(),
                deal.getTipoDeal(), deal.getTipoValor(),
                new Integer(deal.getUsuario().getIdUsuario()));
    }

    /**
     * Regresa un arreglo de 3 fechas: Fecha CASH, Fecha TOM y Fecha SPOT.
     *
     * @return Date[].
     */
    private Date[] getFechasValor() {
        debug("*** getFechasValor ", null);
        Date[] fechasValor = new Date[Num.I_3];
        fechasValor[0] = new Date();
        Calendar gc = new GregorianCalendar();
        PizarronServiceData pizarronServiceData = (PizarronServiceData) getBeanFactory().
                getBean("pizarronServiceData");
        Map map = pizarronServiceData.getDifCarryMap(new Date());
        gc.add(Calendar.DAY_OF_MONTH, Integer.parseInt(map.get("difTomCash").toString()));
        fechasValor[1] = gc.getTime();
        gc = new GregorianCalendar();
        map = pizarronServiceData.getDifCarryMap(new Date());
        gc.add(Calendar.DAY_OF_MONTH, Integer.parseInt(map.get("difTomCash").toString()) +
                Integer.parseInt(map.get("difSpotTom").toString()));
        fechasValor[2] = gc.getTime();
        return fechasValor;
    }

    /**
     * Obtiene las distintas formas de liquidaci&oacute;n para un detalle de deal con base en el
     * nombre del sistema y el canal de operaci&oacute;n.
     *
     * @param ticket El ticket de la sesi&oacute;n.
     * @param recibimos Define si la operaci&oacute;n es compra o venta.
     * @param idDivisa El id de la divisa.
     * @param claveFormaLiquidacion La clave de la forma de liquidaci&oacute;n.
     * @return List
     */
    private List getFormasPagoLiqAplicablesParaDetalle(String ticket, boolean recibimos,
                                                       String idDivisa,
                                                       String claveFormaLiquidacion) {
        SiteService siteService = (SiteService) getBeanFactory().getBean("siteService");
        List formasPagoLiq = siteService.findFormaPagoLiq(ticket, "SICA", "PMY");
        List resultado = new ArrayList();
        for (Iterator it = formasPagoLiq.iterator(); it.hasNext();) {
            FormaPagoLiq fpl = (FormaPagoLiq) it.next();

            if (fpl.getIdDivisa().equals(idDivisa) &&
                    fpl.isRecibimos().booleanValue() == recibimos &&
                    fpl.getClaveFormaLiquidacion().equals(claveFormaLiquidacion) &&
                    fpl.getDesplegableTeller() != null &&
                    fpl.getDesplegableTeller().booleanValue()) {
                resultado.add(new FormaPagoLiqDto(fpl.getClaveFormaLiquidacion(), fpl.getComision(),
                        fpl.getDescripcion(), fpl.getIdBanco(), fpl.getIdDivisa(),
                        fpl.getMnemonico(), fpl.getMontoMaximo(), fpl.getMontoMinimo(),
                        fpl.getMultiplo(), fpl.getNombreBanco(), fpl.getOperacionMinima(),
                        fpl.getRecibimos() != null && fpl.getRecibimos().booleanValue(),
                        fpl.getUsaPlantilla() != null && fpl.getUsaPlantilla().booleanValue()));
            }
        }
        return resultado;
    }

    /**
     * Regresa la lista de claves de divisas frecuentes.
     * 
     * @return List. La lista de las divisas fdrecuentes-
     */
    private List getDivisasFrecuentes() {
	    List divisas = getSicaServiceData().findDivisasFrecuentes();
	    List ids = new ArrayList();
	    for (Iterator it = divisas.iterator(); it.hasNext();) {
	        Divisa d = (Divisa) it.next();
	        DivisaDto divisaDto = new DivisaDto(d.getIdDivisa(), d.getDescripcion(),
	                d.getIdMoneda(), d.getTipo());
	        ids.add(divisaDto);
	    }
	    return ids;
    }

    /**
     * Regresa una lista con la informaci&oacute;n de las divisas no frecuentes o metales
     * amonedados.
     *
     * @param metales true para metales amonedados, false para divisas no frecuentes.
     * @return
     */
    private List getDivisasNoFrecuentes(boolean metales) {
    	
        List fds = metales ? getSicaServiceData().findDivisasMetalesFactor() :
                getSicaServiceData().findDivisasNoFrecuentesFactor();
        List divisasList = new ArrayList();
        for (Iterator it = fds.iterator(); it.hasNext();) {
            FactorDivisaActual fd = (FactorDivisaActual) it.next();
            
            DivisaDto divisaDto = new DivisaDto(fd.getFacDiv().getToDivisa().getIdDivisa(),
                    fd.getFacDiv().getToDivisa().getDescripcion(), fd.getFacDiv().getToDivisa().
                            getIdMoneda(),
                    fd.getFacDiv().getToDivisa().getTipo());
            divisasList.add(divisaDto);
        }
        return divisasList;
    }

    /**
     * Regresa el mapa del pizarr&oacute;n.
     *
     * @param ticket El ticket de la sesi&oacute;n.
     * @param idSucursalOriginal El n&uacute;mero de sucursal, seg&uacute;n la bup.
     * @return Map.
     */
    public ResultadoOperacionDto getMapasPizarron(String ticket, String idSucursalOriginal) {
        
        debug("*** getMapasPizarron " + ticket + " " + idSucursalOriginal, null);
        try {
            validarTicket(ticket);
            PizarronServiceData psd = (PizarronServiceData) getBeanFactory().
                    getBean("pizarronServiceData");
            Canal canal = getSicaServiceData().findCanalByIdSucursal(idSucursalOriginal);
            if (canal == null) {
                throw new SicaException("No se pudo crear el pizarr\u00f3n ya que no se " +
                        "encontr\u00f3 un canal para la sucursal" + idSucursalOriginal);
            }
            Map pizarron = psd.crearPizarron(ticket, canal.getTipoPizarron(),
                    canal.getSucursal() == null);
            ResultadoOperacionDto resultado = crearResultadoOperacionDto(0, RESULTADO_OK);
            resultado.setObjeto(pizarron);
            return resultado;
        }
        catch (SeguridadException e) {
        	
        	warn(e.getMessage(), e);
            return crearResultadoOperacionDto(-1, e.getMessage());
        }
        catch (SicaException e) {
        	
        	warn(e.getMessage(), e);
            return crearResultadoOperacionDto(-1, e.getMessage());
        }
    }

    /**
     * Regresa los Par&aacute;metros necesarios para el c&aacutelculo del l&iacute;mite de
     * desviaci&oacute;n.
     *
     * @return Map.
     */
    public ResultadoOperacionDto getParsLimDesv() {
        debug("*** getParsLimDesv ", null);
        ResultadoOperacionDto resultado = crearResultadoOperacionDto(0, RESULTADO_OK);
        resultado.setObjeto(getSicaServiceData().getParsLimDesv());
        return resultado;
    }

    /**
     * Regresa la lista de DTOs<PlantillaCuentaIxeDto> para el
     * contrato y mnem&oacute;nico proporcionados.
     *
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param noCuenta El n&uacute;mero de contrato SICA del cliente.
     * @param mnemonico El mnem&oacute;nico para filtrar las plantillas.
     * @return ResultadoOperacionDto.
     */
    public ResultadoOperacionDto getPlantillasCuentaIxeParaContrato(String ticket, String noCuenta,
                                                  String mnemonico) {
        debug("*** getPlantillasCuentaIxeParaContrato " + ticket + " " + noCuenta, null);
        try {
            validarTicket(ticket);
            List resultados = new ArrayList();
            List plantillas = getSicaServiceData().findPlantillas("CuentaIxe", noCuenta, mnemonico);
            for (Iterator iterator = plantillas.iterator(); iterator.hasNext();) {
                PlantillaCuentaIxe p = (PlantillaCuentaIxe) iterator.next();
                PlantillaCuentaIxeDto dto = new PlantillaCuentaIxeDto(p.getIdPlantilla(), 
                        p.getClavePlantilla(), p.getContratoSica().getNoCuenta(), p.getMnemonico(),
                        p.getUltimaModificacion(), p.getStatusPlantilla(),
                        p.getBeneficiario().getIdPersona().intValue(),
                        p.getBeneficiario().getNombreCompleto(), p.getNoCuentaIxe());
                resultados.add(dto);
            }
            ResultadoOperacionDto resultado = crearResultadoOperacionDto(0, RESULTADO_OK);
            resultado.setObjeto(resultados);
            return resultado;
        }
        catch (SeguridadException e) {
        	
        	warn(e.getMessage(), e);
            return crearResultadoOperacionDto(-1, e.getMessage());
        }
    }

    /**
     * Regresa la lista de DTOs<PlantillaInternacionalDto> para el
     * contrato y mnem&oacute;nico proporcionados.
     *
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param noCuenta El n&uacute;mero de contrato SICA del cliente.
     * @param mnemonico El mnem&oacute;nico para filtrar las plantillas.
     * @return ResultadoOperacionDto.
     */
    public ResultadoOperacionDto getPlantillasIntlsParaContrato(String ticket, String noCuenta,
                                                                String mnemonico) {
        debug("*** getPlantillasIntlsParaContrato " + ticket + " " + noCuenta, null);
        try {
            validarTicket(ticket);
            List resultados = new ArrayList();
            List plantillas = getSicaServiceData().findPlantillas("Intls", noCuenta, mnemonico);
            for (Iterator iterator = plantillas.iterator(); iterator.hasNext();) {
                PlantillaIntl p = (PlantillaIntl) iterator.next();
                PlantillaInternacionalDto dto = new PlantillaInternacionalDto(p.getIdPlantilla(),
                        p.getClavePlantilla(), p.getContratoSica().getNoCuenta(), p.getMnemonico(),
                        p.getUltimaModificacion(), p.getStatusPlantilla(),
                        p.getBeneficiario().getIdPersona().intValue(),
                        p.getBeneficiario().getNombreCompleto(), p.getDivisa().getIdDivisa(),
                        p.getTipoCuentaBanco(), p.getTipoCuentaBancoInterm(),
                        p.getClaveBanco(), p.getClaveBancoInterm(), p.getIdPais(),
                        p.getIdPaisInterm(), p.getNoCuentaBeneficiario(), p.getNombreBanco(),
                        p.getCuentaBancoInterm(), p.getNombreBancoInterm(),
                        p.getInstrBcoBeneficiario(), p.getInstrBcoIntermediario());
                resultados.add(dto);
            }
            ResultadoOperacionDto resultado = crearResultadoOperacionDto(0, RESULTADO_OK);
            resultado.setObjeto(resultados);
            return resultado;
        }
        catch (SeguridadException e) {
        	
        	warn(e.getMessage(), e);
            return crearResultadoOperacionDto(-1, e.getMessage());
        }
    }

    /**
     * Regresa la lista de DTOs<PlantillaNacionalDto> para el
     * contrato y mnem&oacute;nico proporcionados.
     *
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param noCuenta El n&uacute;mero de contrato SICA del cliente.
     * @param mnemonico El mnem&oacute;nico para filtrar las plantillas.
     * @return ResultadoOperacionDto.
     */
    public ResultadoOperacionDto getPlantillasNacionalesParaContrato(String ticket, String noCuenta,
                                                   String mnemonico) {
        if (logger.isWarnEnabled()) {
            logger.warn("*** getPlantillasNacionalesParaContrato " + ticket + " " + noCuenta);
        }
        try {
            validarTicket(ticket);
            List resultados = new ArrayList();
            List plantillas = getSicaServiceData().findPlantillas("Nacionales", noCuenta,
                    mnemonico);
            for (Iterator iterator = plantillas.iterator(); iterator.hasNext();) {
                PlantillaNacional p = (PlantillaNacional) iterator.next();
                PlantillaNacionalDto dto = new PlantillaNacionalDto(p.getIdPlantilla(),
                        p.getClavePlantilla(), p.getContratoSica().getNoCuenta(), p.getMnemonico(),
                        p.getUltimaModificacion(), p.getStatusPlantilla(),
                        p.getBeneficiario().getIdPersona().intValue(),
                        p.getBeneficiario().getNombreCompleto());
                resultados.add(dto);
            }
            ResultadoOperacionDto resultado = crearResultadoOperacionDto(0, RESULTADO_OK);
            resultado.setObjeto(resultados);
            return resultado;
        }
        catch (SeguridadException e) {
        	
        	warn(e.getMessage(), e);
            return crearResultadoOperacionDto(-1, e.getMessage());
        }
    }

    /**
     * Regresa la lista de DTOs<PlantillaTranNacionalDto> para el
     * contrato y mnem&oacute;nico proporcionados.
     *
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param noCuenta El n&uacute;mero de contrato SICA del cliente.
     * @param mnemonico El mnem&oacute;nico para filtrar las plantillas.
     * @return ResultadoOperacionDto.
     */
    public ResultadoOperacionDto getPlantillasTranNacionalesParaContrato(String ticket,
                                                                         String noCuenta,
                                                                         String mnemonico) {
        if (logger.isWarnEnabled()) {
            logger.warn("*** getPlantillasTranNacionalesParaContrato " + ticket + " " + noCuenta);
        }
        try {
            validarTicket(ticket);
            List resultados = new ArrayList();
            List plantillas = getSicaServiceData().findPlantillas("TranNacionales", noCuenta,
                    mnemonico);
            for (Iterator iterator = plantillas.iterator(); iterator.hasNext();) {
                PlantillaTranNacional p = (PlantillaTranNacional) iterator.next();
                PlantillaTranNacionalDto dto = new PlantillaTranNacionalDto(p.getIdPlantilla(), 
                        p.getClavePlantilla(), p.getContratoSica().getNoCuenta(), p.getMnemonico(),
                        p.getUltimaModificacion(), p.getStatusPlantilla(),
                        p.getBeneficiario().getIdPersona().intValue(),
                        p.getBeneficiario().getNombreCompleto(), p.getClabe(), p.getIdBanco(),
                        p.getNombreBanco(), p.getDivisa().getIdDivisa());
                resultados.add(dto);
            }
            ResultadoOperacionDto resultado = crearResultadoOperacionDto(0, RESULTADO_OK);
            resultado.setObjeto(resultados);
            return resultado;
        }
        catch (SeguridadException e) {
        	
        	warn(e.getMessage(), e);
            return crearResultadoOperacionDto(-1, e.getMessage());
        }
    }

    /**
     * Obtiene la instancia del servicio SicaServiceData
     *
     * @return SicaServiceData.
     */
    private SicaServiceData getSicaServiceData() {
        return (SicaServiceData) getBeanFactory().getBean("sicaServiceData");
    }
    
    /**
     * Obtiene la instancia del servicio ContratoDireccionesService
     *
     * @return ContratoDireccionesService
     */
    private ContratoDireccionesService getContratoDireccionesService() {
    	return (ContratoDireccionesService) getBeanFactory().getBean("contratoDireccionesService");
    }
    
    /**
     * Obtiene la instancia del servicio EsbService
     *
     * @return EsbService.
     */
    private EsbService getSicaEsbService() {
        return (EsbService) getBeanFactory().getBean("sicaEsbService");
    }
    
    /**
     * Obtiene una referencia al EJB del servicio de Contratacion 
     * <code>SicaBusquedaPersonaService</code>.
     *
     * @return SicaBusquedaPersonaService
     */
    private SicaBusquedaPersonaService getSicaBusquedaPersonaService() {
        return (SicaBusquedaPersonaService) getBeanFactory().getBean("sicaBusquedaPersonaService");
    }
    
    /**
     * Obtiene una referencia al EJB del servicio de Contratacion 
     * <code>SicaConsultaProductosPersonaService</code>.
     *
     * @return SicaConsultaProductosPersonaService
     */
    private SicaConsultaProductosPersonaService getSicaConsultaProductosPersonaService() {
        return (SicaConsultaProductosPersonaService) getBeanFactory().
                getBean("sicaConsultaProductosPersonaService");
    }
    
    /**
     * Obtiene una referencia al EJB del servicio de Contratacion 
     * <code>SicaRegistroRelacionCuentaPersonaService</code>.
     *
     * @return SicaRegistroRelacionCuentaPersonaService
     */
    private SicaRegistroRelacionCuentaPersonaService getSicaRegistroRelacionCuentaPersonaService() {
        return (SicaRegistroRelacionCuentaPersonaService) getBeanFactory().
                getBean("sicaRegistroRelacionCuentaPersonaService");
    }

    /**
     * Obtiene la instancia del servicio pizarronServiceData
     *
     * @return SicaServiceData.
     */
    private PizarronServiceData getPizarronServiceData() {
        return (PizarronServiceData) getBeanFactory().getBean("pizarronServiceData");
    }

    /**
     * Asigna los valores correspondientes para una plantilla.
     *
     * @param plantilla La interfaz de la plantilla.
     * @param noCuenta El n&uacute;mero de cuenta.
     * @param mnemonico El mnem&oacute;nico de la forma liquidaci&oacute;n.
     * @param clavePlantilla La clave de la plantilla.
     * @param idBeneficiario  El id personas del beneficiario.
     */
    private void llenarPlantilla(IPlantilla plantilla, String noCuenta, String mnemonico,
                                 String clavePlantilla, int idBeneficiario) {
        SicaServiceData sd = getSicaServiceData();
        Persona beneficiario = sd.findBeneficiarioById("" + idBeneficiario);
        if (beneficiario == null) {
    		throw new SicaException("No se encontr\u00f3 el beneficiario con la clave " + 
    				idBeneficiario);
    	}
        plantilla.setBeneficiario(beneficiario);
        plantilla.setClavePlantilla(clavePlantilla);
        ContratoSica contratoSica = sd.findContrato(noCuenta);
        if (contratoSica == null) {
        	throw new SicaException("No se encontr\u00f3 el contrato sica " + noCuenta);
        }
        plantilla.setContratoSica(contratoSica);
        plantilla.setMnemonico(mnemonico);
        plantilla.setStatusPlantilla(PlantillaNacional.STATUS_PLANTILLA_ACTIVA);
        plantilla.setUltimaModificacion(new Date());
    }

    /**
     * Asigna los valores correspondientes para una plantilla de transferencia internacional.
     *
     * @param noCuenta El n&uacute;mero de cuenta.
     * @param mnemonico El mnem&oacute;nico a asignar a la plantilla.
     * @param clavePlantilla La clave de 4 caracteres (opcional).
     * @param idBeneficiario El idPersona del beneficiario (o destinatario) de la transferencia.
     * @param idDivisa La clave de la divisa.
     * @param noCuentaBeneficiario El n&uacute;mero de cuenta del destinatario.
     * @param tipoCuentaBanco El tipo de cuenta del banco (ABA, SWIFT, etc).
     * @param claveBanco La clave ABA, SWIFT, etc del banco.
     * @param nombreBanco El nombre del banco extranjero.
     * @param claveIsoPais La clave ISO del pa&iacute;s donde se encuentra el banco destinatario.
     * @return IPlantillaIntl.
     */
    private IPlantillaIntl llenarPlantillaInternacional(String noCuenta, String mnemonico,
                                                        String clavePlantilla, int idBeneficiario,
                                                        String idDivisa,
                                                        String noCuentaBeneficiario,
                                                        String tipoCuentaBanco,
                                                        String claveBanco, String nombreBanco,
                                                        String claveIsoPais) {
        IPlantillaIntl plantilla = new PlantillaIntl();
        llenarPlantilla(plantilla, noCuenta, mnemonico, clavePlantilla, idBeneficiario);
        plantilla.setTipoCuentaBanco(tipoCuentaBanco);
        plantilla.setClaveBanco(claveBanco);
        plantilla.setDivisa(getSicaServiceData().findDivisa(idDivisa));
        plantilla.setIdPais(claveIsoPais);
        plantilla.setNoCuentaBeneficiario(noCuentaBeneficiario);
        plantilla.setNombreBanco(nombreBanco);
        return plantilla;
    }
    
    /**
     * Obtiene la lista de plantillas nacional para un n&uacute;mero de cuenta
     * 
     * @param noCuenta El n&uacute;mero de cuenta.
     * @return List. La lista con objetos {@link PlantillaNacionalDto}
     */
	private List getPlantillaNacionalPorNoCuenta(String noCuenta) {
		List plantillasDto = new ArrayList();
		for (Iterator iter = getSicaServiceData().findPlantillasWithContratoSicaAndBeneficiario(
				"Nacionales", noCuenta).iterator(); iter.hasNext();) {
			PlantillaNacional plantilla = (PlantillaNacional) iter.next();
			plantillasDto.add(new PlantillaNacionalDto(plantilla.getIdPlantilla(), 
					plantilla.getClavePlantilla(), plantilla.getContratoSica().getNoCuenta(), 
					plantilla.getMnemonico(), plantilla.getUltimaModificacion(), 
					plantilla.getStatusPlantilla(), 
					plantilla.getBeneficiario().getIdPersona().intValue(), 
					plantilla.getBeneficiario().getNombreCompleto()));
		}
		return plantillasDto;
	}

	/**
     * Obtiene la lista de plantillas nacional para un n&uacute;mero de cuenta
     * 
     * @param noCuenta El n&uacute;mero de cuenta.
     * @return List. La lista con objetos {@link PlantillaTranNacionalDto}
     */
	private List getPlantillaTraNalPorNoCuenta(String noCuenta) {
		List plantillasDto = new ArrayList();
		for (Iterator iter = getSicaServiceData().findPlantillasWithContratoSicaAndBeneficiario(
				"TranNacionales", noCuenta).iterator(); iter.hasNext();) {
			PlantillaTranNacional plantilla = (PlantillaTranNacional) iter.next();
			plantillasDto.add(new PlantillaTranNacionalDto(plantilla.getIdPlantilla(), 
					plantilla.getClavePlantilla(), plantilla.getContratoSica().getNoCuenta(), 
					plantilla.getMnemonico(), plantilla.getUltimaModificacion(), 
					plantilla.getStatusPlantilla(), 
					plantilla.getBeneficiario().getIdPersona().intValue(), 
					plantilla.getBeneficiario().getNombreCompleto(), plantilla.getClabe(), 
					plantilla.getIdBanco(), plantilla.getNombreBanco(), 
					plantilla.getDivisa().getIdDivisa()));
		}
		return plantillasDto;
	}
    
    /**
     * Obtiene la lista de plantillas con cuenta IXE registradas para un n&uacute;mero de cuenta.
     * @param noCuenta
     * 
     * @return List La lista con objetos {@link PlantillaCuentaIxe}
     */
    private List getPlantillaCuentaIxePorNoCuenta(String noCuenta) {
    	List plantillasDto = new ArrayList();
    	for (Iterator iter = getSicaServiceData().findPlantillasWithContratoSicaAndBeneficiario(
    			"CuentaIxe", noCuenta).iterator(); iter.hasNext();) {
    		PlantillaCuentaIxe plantilla = (PlantillaCuentaIxe) iter.next();
    		plantillasDto.add(new PlantillaCuentaIxeDto(plantilla.getIdPlantilla(), 
    				plantilla.getClavePlantilla(), plantilla.getContratoSica().getNoCuenta(), 
    				plantilla.getMnemonico(), plantilla.getUltimaModificacion(), 
    				plantilla.getStatusPlantilla(), 
    				plantilla.getBeneficiario().getIdPersona().intValue(), 
    				plantilla.getBeneficiario().getNombreCompleto(), 
    				plantilla.getContratoSica().getNoCuenta()));
		}
		return plantillasDto;
	}
    
    /**
     * Obtiene el l&iacute;mite diario y l&iacute;mite mensual maximos que puede operar un
     * n&uacute;mero de cuenta.
     *
     * @param noCuenta El n&uacute;mero de cuenta del contrato sica.
     * @param divisa La divisa.
     * @param claveFormaLiquidacion La clave forma de liquidaci&oacute;n del detalle.
     * @param recibimos Si se trata de un recibimos o un entregamos.
     * @param persona El objeto persona.
     * @param pasaporte El n&uacute;mero de pasaporte.
     * @param ifeFolio El n&uacute;mero de folio de la IFE.
     * @param ifeAnioRegistro El anio de registro de la IFE.
     * @param ifeNumeroEmision El n&uacute;mero de emisi&oacute;n del IFE.
     * @param ifeOcr El n&uacute;mero de emisi&oacute;n del OCR.
     * @param idSucursal El id de la sucursal.
     * @param claveIsoPais La clave iso del pa&iacute;s.
     * @param ticket El n&uacute;mero de ticket.
     * @param usuario El objeto Iusuario.
     * @return LimiteOperacionDto
     */
    private LimiteOperacionDto getLimitesMaximosDeOperacion(String noCuenta, String divisa,
    		String claveFormaLiquidacion, Boolean recibimos, Persona persona, String pasaporte,
    		String ifeFolio, String ifeAnioRegistro, String ifeNumeroEmision, String ifeOcr,
    		int idSucursal, String claveIsoPais, String ticket, IUsuario usuario) {
    	LimiteOperacionDto limOperDto = new LimiteOperacionDto();
    	List productosEnRestriccion = new ArrayList();
    	productosEnRestriccion.add(claveFormaLiquidacion);
    	double acumDiarioProdsRest =
			getSicaServiceData().findAcumuladoUsdCompraVentaDiarioMensual(noCuenta, new Date(),
					true, divisa, productosEnRestriccion, recibimos.booleanValue());
    	double acumMensualProdsRest = 
    		getSicaServiceData().findAcumuladoUsdCompraVentaDiarioMensual(noCuenta, new Date(),
    				false, divisa, productosEnRestriccion, recibimos.booleanValue());
    	LimitesRestriccionOperacion limRestOper = getLimitesActualizados(noCuenta, persona,
    			pasaporte, ifeFolio, ifeAnioRegistro, ifeNumeroEmision, ifeOcr, idSucursal,
    			claveIsoPais, ticket, usuario);
        Double limiteMaximoMensual =
            limRestOper.getLimiteMaximoMensual(divisa, recibimos.booleanValue());
        Double limiteMaximoDiario =
            limRestOper.getLimiteMaximoDiario(divisa, recibimos.booleanValue());
        if (limiteMaximoMensual == null) {
            limOperDto.setLimiteMensual(-1.0);
        }
        else if (limiteMaximoMensual.doubleValue() - acumMensualProdsRest <= 0.0) {
    		limOperDto.setLimiteMensual(0.0);
    	}
    	else {
    		limOperDto.setLimiteMensual(
                limiteMaximoMensual.doubleValue() - acumMensualProdsRest);
    	}
        if (limiteMaximoDiario == null) {
            limOperDto.setLimiteDiario(-1.0);
        }
        else if (limiteMaximoDiario.doubleValue() - acumDiarioProdsRest <= 0.0) {
    		limOperDto.setLimiteDiario(0.0);
    	}
    	else {
    		limOperDto.setLimiteDiario(
                limiteMaximoDiario.doubleValue() - acumDiarioProdsRest);
    	}
    	limOperDto.setIdDivisa(divisa);
    	limOperDto.setClaveFormaLiquidacion(claveFormaLiquidacion);
    	limOperDto.setRecibimos(recibimos.booleanValue());
    	return limOperDto;
    }
    
    /**
     * Obtiene la lista de plantillas internacionales para un n&uacute;mero de cuenta
     * 
     * @param noCuenta El n&uacute;mero de cuenta.
     * @return List. La lista con objetos {@link PlantillaInternacionalDto}
     */
	private List getPlantillaInternacionalPorNoCuenta(String noCuenta) {
		List plantillasDto = new ArrayList();
    	for (Iterator iter = getSicaServiceData().findPlantillasWithContratoSicaAndBeneficiario(
    			"Intls", noCuenta).iterator(); iter.hasNext();) {
    		PlantillaIntl plantilla = (PlantillaIntl) iter.next();
    		plantillasDto.add(new PlantillaInternacionalDto(plantilla.getIdPlantilla(), 
    				plantilla.getClavePlantilla(), plantilla.getContratoSica().getNoCuenta(), 
    				plantilla.getMnemonico(), plantilla.getUltimaModificacion(), 
    				plantilla.getStatusPlantilla(), 
    				plantilla.getBeneficiario().getIdPersona().intValue(), 
    				plantilla.getBeneficiario().getNombreCompleto(), 
    				plantilla.getDivisa().getIdDivisa(), plantilla.getTipoCuentaBanco(), 
    				plantilla.getTipoCuentaBancoInterm(), plantilla.getClaveBanco(), 
    				plantilla.getClaveBancoInterm(), plantilla.getIdPais(), 
    				plantilla.getIdPaisInterm(), plantilla.getNoCuentaBeneficiario(), 
    				plantilla.getNombreBanco(), plantilla.getCuentaBancoInterm(), 
    				plantilla.getNombreBancoInterm(), plantilla.getInstrBcoBeneficiario(), 
    				plantilla.getInstrBcoIntermediario()));
		}
		return plantillasDto;
	}    

    /**
     * Arranca el procesamiento de deals.
     *
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param idDeal El n&uacute;mero de deal a procesar.
     * @return ResultadoOperacionDto.
     */
    public ResultadoOperacionDto procesarDeal(String ticket, int idDeal) {
        debug("*** procesarDeal " + ticket + " " + idDeal, null);
        try {
            validarTicket(ticket);
            WorkFlowServiceData wfsd = (WorkFlowServiceData) getBeanFactory().
                    getBean("sicaWorkFlowServiceData");
            wfsd.wfIniciarProcesoDeal(ticket, idDeal);
            return crearResultadoOperacionDto(0, RESULTADO_OK);
        }
        catch (SeguridadException e) {
        	
        	warn(e.getMessage(), e);
            return crearResultadoOperacionDto(-1, e.getMessage());
        }
    }

    /**
     * Levanta una excepci&oacute;n si el estado del sistema no es: Operaci&oacute;n Normal,
     * Restringida, Vespertina o Nocturna.
     *
     * @return Estado.
     */
    private Estado validarEstadoSistema() {
        Estado estado = getSicaServiceData().findEstadoSistemaActual();
        switch (estado.getIdEstado()) {
            case Estado.ESTADO_OPERACION_NORMAL:
            case Estado.ESTADO_OPERACION_RESTRINGIDA:
            case Estado.ESTADO_OPERACION_VESPERTINA:
            case Estado.ESTADO_OPERACION_NOCTURNA:
                break;
            default:
                throw new SicaException("No se puede operar cuando el SICA est\u00e1 en estado " +
                        "de " + estado.getNombreEstado());
        }
        return estado;
    }

    /**
     * Regresa true si el estado actual del sistema es Operaci&oacute;n Vespertina u
     * Operaci&oacute;n nocturna.
     *
     * @return boolean.
     */
    private boolean isVespertinoNocturno() {
        ParametroSica param = getSicaServiceData().findParametro(ParametroSica.HORA_LIM_SUC);
        String horaActual = HOUR_FORMAT.format(new Date());
        Estado estado = getSicaServiceData().findEstadoSistemaActual();
        return param.getValor().compareTo(horaActual) < 0 ||
                Estado.ESTADO_OPERACION_VESPERTINA == estado.getIdEstado() ||
                Estado.ESTADO_OPERACION_NOCTURNA == estado.getIdEstado();
    }

    /**
     * Valida el ticket de la sesi&oacute;n.
     *
     * @param ticket El ticket de la sesi&oacute;n.
     * @throws SeguridadException Se dispara en caso de ocurrir algun error.
     */
    private void validarTicket(String ticket) throws SeguridadException {
        ((TicketService) getBeanFactory().getBean("ticketService")).isTicketValido(ticket);
    }

    /**
     * Llama al debug del logger, si &eacute;ste est&aacute; habilitado.
     *
     * @param o El objeto a 'loggear'.
     * @param t La excepci&oacute;n.
     */
    private void debug(Object o, Throwable t) {
        if (logger.isDebugEnabled()) {
            logger.debug(o, t);
        }
    }

    /**
     * Llama al warn del logger, si &eacute;ste est&aacute; habilitado.
     *
     * @param o El objeto a 'loggear'.
     * @param t La excepci&oacute;n.
     */
    private void warn(Object o, Throwable t) {
        if (logger.isWarnEnabled()) {
            logger.warn(o, t);
        }
    }

    /**
     * Constante que define como correcto el resultado de la operaci&oacute;n.
     */
    private static final String RESULTADO_OK = "OK";
    
    /**
     * Constante que define el tipo de divisa como Frecuente.
     */
    public static final String FRECUENTE = "F";
    
    /**
     * Constante que define el tipo de divisa como No frecuente.
     */
    public static final String NO_FRECUENTE = "N";
    
    /**
     * Constante que define el tipo de divisa como Metal Amonedado.
     */
    public static final String METAL_AMONEDADO = "M";
    
    /**
     * Constante que define el tipo de divisa como Todas.
     */
    public static final String TODAS = "T";

    /**
     * Constante con el formato de horas y minutos.
     */
    public static final SimpleDateFormat HOUR_FORMAT = new SimpleDateFormat("HH:mm");
      
    /**
     * El UID para serializaci&oacute;n.
     */
    private static final long serialVersionUID = -2763372770581226857L;
}