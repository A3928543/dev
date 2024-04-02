/*
 * $Id: HibernateSwapsServiceData.java,v 1.20.8.1.4.3 2011/02/03 16:35:12 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2006 - 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica.sdo.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;

import org.apache.commons.lang.StringUtils;
import org.springframework.orm.hibernate.support.HibernateDaoSupport;

import com.ixe.ods.bup.model.ContratoSica;
import com.ixe.ods.bup.model.EmpleadoIxe;
import com.ixe.ods.seguridad.model.IUsuario;
import com.ixe.ods.seguridad.model.Usuario;
import com.ixe.ods.sica.Constantes;
import com.ixe.ods.sica.Keys;
import com.ixe.ods.sica.Num;
import com.ixe.ods.sica.SicaCodedException;
import com.ixe.ods.sica.dao.BitacoraEnviadasDao;
import com.ixe.ods.sica.dao.SwapDao;
import com.ixe.ods.sica.model.BitacoraEnviadas;
import com.ixe.ods.sica.model.Broker;
import com.ixe.ods.sica.model.Canal;
import com.ixe.ods.sica.model.CanalMesa;
import com.ixe.ods.sica.model.ContraparteBanxico;
import com.ixe.ods.sica.model.Deal;
import com.ixe.ods.sica.model.DetalleReporteDiario;
import com.ixe.ods.sica.model.Divisa;
import com.ixe.ods.sica.model.MesaCambio;
import com.ixe.ods.sica.model.ParametroSica;
import com.ixe.ods.sica.model.RespAltaKondor;
import com.ixe.ods.sica.model.Swap;
import com.ixe.ods.sica.sdo.PizarronServiceData;
import com.ixe.ods.sica.sdo.SicaServiceData;
import com.ixe.ods.sica.sdo.SwapsServiceData;
import com.ixe.ods.sica.services.DesviacionInterbancaria;
import com.ixe.ods.sica.utils.DateUtils;
import com.ixe.ods.sica.utils.MapUtils;
import com.ixe.ods.sica.vo.BrokerVO;
import com.ixe.ods.sica.vo.DetalleSwapVO;
import com.ixe.ods.sica.vo.SwapVO;

/**
 * Implementaci&oacute;n en Hibernate de la interfaz SwapsServiceData.
 *
 * @author Jean C. Favila
 * @version $Revision: 1.20.8.1.4.3 $ $Date: 2011/02/03 16:35:12 $
 * @see SwapsServiceData
 */
public class HibernateSwapsServiceData extends HibernateDaoSupport implements SwapsServiceData {

    /**
     * Constructor por default.
     */
    public HibernateSwapsServiceData() {
        super();
    }

    /**
     * @param broker     El nuevo broker.
     * @param noCuenta   El n&uacute;mero de cuenta del broker.
     * @param idPromotor El id del promotor.
     * @return BrokerVO
     * @see com.ixe.ods.sica.sdo.SwapsServiceData#crearBrokerVO(Broker, String, int)
     */
    public BrokerVO crearBrokerVO(Broker broker, String noCuenta, int idPromotor) {
        return new BrokerVO(broker.getId().getPersonaMoral().getIdPersona().intValue(),
                broker.getId().getPersonaMoral().getNombreCompleto(), broker.getClaveReuters(),
                noCuenta, idPromotor);
    }

    /**
     * @deprecated Se migro el catalogo de contrapartes a la tabla SC_BROKER,
     * usar findContraparteBanxico que retorna un objeto Broker.
     * @param claveContraparte La clave contraparte.
     * @return ContraparteBanxico.
     * @see com.ixe.ods.sica.sdo.SwapsServiceData#findContraparteBanxico(String)
     */
    /*public ContraparteBanxico findContraparteBanxico(String claveContraparte) {
        // Obtener el contrato sica:
        List contrapartes = getHibernateTemplate().
                find("FROM ContraparteBanxico AS cb WHERE cb.idSaif = ?", claveContraparte);
        if (contrapartes.isEmpty()) {
            throw new SicaCodedException(
                    SicaCodedException.KONDOR_CONTRAPARTE_BANXICO_NO_ENCONTRADA_IA1,
                    claveContraparte);
        }
        return (ContraparteBanxico) contrapartes.get(0);
    }*/
    
    /**
     * @param claveContraparte La clave contraparte.
     * @return ContraparteBanxico.
     * @see com.ixe.ods.sica.sdo.SwapsServiceData#findContraparteBanxico(String)
     */
    public Broker findContraparteBanxico(String claveContraparte) {
        // Obtener el contrato sica:
        List contrapartes = getHibernateTemplate().
                find("FROM Broker AS b WHERE b.idSaif = ? " +
                		"and b.visibleKondor = 'S'", claveContraparte);
        if (contrapartes.isEmpty()) {
            throw new SicaCodedException(
                    SicaCodedException.KONDOR_CONTRAPARTE_BANXICO_NO_ENCONTRADA_IA1,
                    claveContraparte);
        }
        return (Broker) contrapartes.get(0);
    }

    /**
     * @param folioTas       El folio de la bitacora en el Tas.
     * @param idBroker       El broker correspondiente.
     * @param be             El objecto java de BitacoraEnviadas.
     * @param noContratoSica El n&uacute;mero de contrato sica.
     * @see com.ixe.ods.sica.sdo.SwapsServiceData#cargarBitacorasEnvadas(int, Integer,
     *      com.ixe.ods.sica.model.BitacoraEnviadas, String).
     */
    public void cargarBitacorasEnvadas(int folioTas, Integer idBroker, BitacoraEnviadas be,
                                       String noContratoSica) {
        List brokers = getSicaServiceData().findBrokerByIdPersona(idBroker);
        if (brokers.isEmpty()) {
            throw new SicaCodedException(
                    SicaCodedException.KONDOR_PERSONA_CONTRAPARTE_NO_ENCONTRADA_IA1, idBroker);
        }
        Broker broker = (Broker) brokers.get(0);
        if (be.getPlazo() == null || be.getPlazo().intValue() < 0 ||
                be.getPlazo().intValue() > Num.I_3) {
            throw new SicaCodedException(SicaCodedException.KONDOR_FECHA_VALOR_INVALIDA_IA1,
                    be.getPlazo());
        }
        String fechaValor = getPizarronServiceData().calcularFechaValorParaFechasCapturaLiquidacion(
                DateUtils.inicioDia(), DateUtils.inicioDia(be.getFechaLiquidacion()));
        if (Constantes.CASH.equals(fechaValor)) {
            be.setPlazo(new Integer(Num.I_0));
        }
        else if (Constantes.TOM.equals(fechaValor)) {
            be.setPlazo(new Integer(Num.I_1));
        }
        else if (Constantes.SPOT.equals(fechaValor)) {
            be.setPlazo(new Integer(Num.I_2));
        }
        else {
            be.setPlazo(new Integer(Num.I_3));
        }
        Divisa div = getSicaServiceData().findDivisa(be.getId().getDivisa());
        Double tipoCambioDesplegable = Divisa.DOLAR.equals(be.getContraDivisa()) && div.isDivide() ?
                new Double(1.0 / be.getTipoCambio().doubleValue()) : be.getTipoCambio();
        be.setDatosAdicionales(MapUtils.generar(
                new String[]{"idConfReducido", "compra", "tipoOperacion", "tipoCambioDesplegable",
                        "fechaValor", "noCuenta", "estrategia",
                        "idPersonaContraparte", "nombreContraparte"},
                new Object[]{getIdConfReducido(be.getId().getIdConf()),
                        Boolean.valueOf(!BitacoraEnviadas.TIPO_OPE_COMPRA.equals(be.getTipoOper())),
                        !BitacoraEnviadas.TIPO_OPE_COMPRA.equals(be.getTipoOper()) ? "Compra" :
                                "Venta", tipoCambioDesplegable, fechaValor, noContratoSica,
                        Boolean.valueOf("1".equals(be.getSwap())),
                        broker != null ? broker.getId().getPersonaMoral().getIdPersona() : null,
                        broker != null ? broker.getId().getPersonaMoral().getNombreCompleto() :
                                null}));
    }

    /**
     * Regresa el idConf tal como se puede visualizar en Kondor.
     *
     * @param idConf El idConf que incluye el subfolio.
     * @return El idConf sin el folio.
     */
    private String getIdConfReducido(String idConf) {
        if (!StringUtils.isEmpty(idConf) && idConf.length() > Num.I_3) {
            return idConf.substring(0, idConf.length() - 2);
        }
        return idConf;
    }

    /**
     * @param idFolioSwap El n&uacute;mero de swap.
     * @return Swap.
     * @see com.ixe.ods.sica.sdo.SwapsServiceData#findSwap(int).
     */
    public Swap findSwap(int idFolioSwap) {
        List swaps = getHibernateTemplate().find("FROM Swap AS s INNER JOIN FETCH s.deals AS d " +
                "INNER JOIN FETCH d.canalMesa.canal c LEFT JOIN FETCH c.sucursal " +
                "WHERE s.idFolioSwap = ? ", new Integer(idFolioSwap));
        if (swaps.isEmpty()) {
            return null;
        }
        Swap swap = (Swap) swaps.get(0);
        // Se inicializa la relaci&oacute;n con divisa.
        swap.getDivisa().getIdDivisa();
        return swap;
    }

    /**
     * @param idFolioSwap    El n&uacute;mero de swap.
     * @param idDeal         El n&uacute;mero de deal que est&aacute; incluido en el swap.
     * @param fechaOperacion La fecha en la que se registr&oacute; el swap.
     * @param status         El status del swap a buscar.
     * @return List.
     * @see com.ixe.ods.sica.sdo.SwapsServiceData#findSwaps(int, int, java.util.Date , String).
     */
    public List findSwaps(int idFolioSwap, int idDeal, Date fechaOperacion, String status) {
    	
    	List resultados;
    	
        if (idFolioSwap > 0) {
        	resultados = getHibernateTemplate().find("FROM Swap AS s WHERE s.idFolioSwap = ?",
                    new Integer(idFolioSwap));
        }
        else if (idDeal > 0) {
        	resultados = getHibernateTemplate().find("SELECT s FROM Swap AS s LEFT OUTER JOIN " +	
        			"s.deals AS d WHERE d.idDeal = ?", new Integer(idDeal));
        }
        else {
            if (status.length() > 0) {
            	resultados = getHibernateTemplate().find("FROM Swap AS s WHERE s.fechaOperacion " +
                        "BETWEEN ? AND ? AND s.status = ?", new Object[]{
                        DateUtils.inicioDia(fechaOperacion),
                        DateUtils.finDia(fechaOperacion), status});
            }
            else {        
            	resultados = getHibernateTemplate().find("FROM Swap AS s WHERE s.fechaOperacion " +
                        "BETWEEN ? AND ?", new Object[]{
                        DateUtils.inicioDia(fechaOperacion), DateUtils.finDia(fechaOperacion)});
            }
        }
        
        List deals = null;
    
        for (Iterator i = resultados.iterator(); i.hasNext(); ) {
        	try {
        		
        		deals = ((Swap) i.next()).getDeals();
                Hibernate.initialize(deals);
              }
            catch (HibernateException e) {
          	  e.printStackTrace();
            	logger.error(e);
            }
            
            
            for (Iterator j = deals.iterator(); j.hasNext(); ) {
            	
              try {
                  Hibernate.initialize(((Deal)j.next()).getDetalles());
                }
              catch (HibernateException e) {
            	  e.printStackTrace();
              	logger.error(e);
              }
            }
        }
        
        return resultados;
    }

    /**
     * @param swapVO La instancia con los valores para el swap.
     * @return Map.
     * @see com.ixe.ods.sica.sdo.SwapsServiceData#insertarSwap(com.ixe.ods.sica.vo.SwapVO).
     */
    public Map insertarSwap(SwapVO swapVO) {
        SicaServiceData ssd = getSicaServiceData();
        List brokers = ssd.findBrokerByIdPersona(
                new Integer(swapVO.getContraparte().getIdPersona()));
        Broker broker = (Broker) (brokers.size() > 0 ? brokers.get(0) : null);
        ContratoSica cs = ssd.findContrato(swapVO.getContraparte().getNoCuenta());
        Divisa divisa = ssd.findDivisa(swapVO.getIdDivisa());
        MesaCambio mc = ssd.findMesaCambio(swapVO.getIdMesaCambio());
        CanalMesa cm = new CanalMesa(ssd.findCanal(swapVO.getIdCanal()), mc);
        IUsuario usuario = (IUsuario) ssd.find(Usuario.class, new Integer(swapVO.getIdUsuario()));
        int idFolioSwap = getSwapDao().crearFolioSwap();
        Swap swap = new Swap(idFolioSwap, swapVO.isCompra(), 0, Swap.TIPO_SWAP_SICA, null, cs,
                divisa, null, null);
        getHibernateTemplate().save(swap);
        Map resultado = new HashMap();
        resultado.put("idFolioSwap", new Integer(swap.getIdFolioSwap()));
        int[] idsDeals = new int[swapVO.getDetalles().size()];
        int i = 0;
        ParametroSica par = ssd.findParametro(ParametroSica.ID_PERSONA_IXE_FORWARD);
        Integer idPersonaIxeForward = Integer.valueOf(par.getValor());
        for (Iterator it = swapVO.getDetalles().iterator(); it.hasNext(); i++) {
            DetalleSwapVO detSwapVO = (DetalleSwapVO) it.next();
            Deal deal = new Deal();
            deal.setSwap(swap);
            deal.setCanalMesa(cm);
            if ("IXE FORWARDS".equals(detSwapVO.getTipo())) {
                List brs = ssd.findBrokerByIdPersona(idPersonaIxeForward);
                if (!brs.isEmpty()) {
                    deal.setBroker((Broker) brs.get(0));
                }
                deal.setContratoSica(ssd.findContratoSicaByIdPersona(idPersonaIxeForward));
            }
            else {
                deal.setBroker(broker);
                deal.setContratoSica(cs);
            }
            deal.setTipoDeal(Deal.TIPO_INTERBANCARIO);
            deal.setTipoValor(detSwapVO.getFechaValor());
            deal.setPromotor((EmpleadoIxe) ssd.find(EmpleadoIxe.class,
                    new Integer(swapVO.getContraparte().getIdPromotor())));
            deal.setUsuario(usuario);
            ssd.crearDealInterbancario(deal, detSwapVO.isCompra(), detSwapVO.getFechaLiquidacion(),
                    detSwapVO.getTipoCambio(), detSwapVO.getClaveFormaLiquidacion(),
                    detSwapVO.getMonto(), divisa, mc, usuario, i > 0);
            idsDeals[i] = deal.getIdDeal();
            if (i == 0) {
                swap.setMonto(detSwapVO.getMonto());
                swap.setFolioSwapInicio(new Integer(deal.getIdDeal()));
                swap.setStatus(Swap.STATUS_COMPLETAMENTE_ASIGNADO);
                swap.getDeals().add(deal);
            }
            swap.getDeals().add(deal);
            getHibernateTemplate().update(swap);
        }
        resultado.put("idsDeals", idsDeals);
        return resultado;
    }

    /**
     * Obtiene la lista de registros de Bit&aacute;cora enviada dado el folio en el TAS.
     *
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param bes El arreglo de objetos BitacoraEnviadas que componen el Swap.
     * @return List de HashMap.
     */
    public RespAltaKondor[] crearSwapKondor(String ticket, ArrayList bes) {
        //getTicketService().isTicketValido(ticket);
        validarContenido(bes);
        // Asignamos el folio a todos los registros:
        int folio = getSwapDao().crearFolioSwap();
        for (Iterator it = bes.iterator(); it.hasNext();) {
            BitacoraEnviadas be = (BitacoraEnviadas) it.next();
            be.setFolio(new Integer(folio));
            be.setAccion(new Integer(71));
            be.setCreateDt(new Date());
            be.setStatus("ENVIAR");
            be.setProgressRecid(new Integer(getSwapDao().crearFolioProgressRecid()));            
        }
        // Obtengo el canal que debe asignarse:
        String idCanal = Divisa.DOLAR.equals(((BitacoraEnviadas) bes.get(0)).
                getContraDivisa()) ? Constantes.CANAL_MESA_TRADER_USD :
                Divisa.PESO.equals(((BitacoraEnviadas) bes.get(0)).getContraDivisa()) ?
                        Constantes.CANAL_MESA_OPERACION : null;
        if (idCanal == null) {
            throw new SicaCodedException(SicaCodedException.KONDOR_CONTRADIVISA_INVALIDA);
        }
        Divisa divRef = getSicaServiceData().findCanal(idCanal).getMesaCambio().
                getDivisaReferencia();
        ArrayList resultados = new ArrayList();
        Integer idPersonaIxeForwards = getIdPersonaIxeForwards();
        for (Iterator it = bes.iterator(); it.hasNext();) {
            BitacoraEnviadas be = (BitacoraEnviadas) it.next();
            if (!divRef.getIdDivisa().equals(be.getContraDivisa())) {
                throw new SicaCodedException(SicaCodedException.KONDOR_DIVISA_CONTRADIVISA_IA2,
                        new String[]{be.getContraDivisa(), divRef.getIdDivisa()});
            }
            Broker broker = findContraparteBanxico(be.getClaveContraparte());
            Integer idPersonaContraparte = broker.getId().getPersonaMoral().getIdPersona();
            ContratoSica cs;
            if (idPersonaContraparte.equals(idPersonaIxeForwards)) {
                // Se cambia el n&uacute;mero de persona al del folder KONDOR:
                idPersonaContraparte = getIdPersonaPorFolderKondor(be.getFolderk());
            }
            cs = getSicaServiceData().findContratoSicaByIdPersona(idPersonaContraparte);
            if (cs == null) {
                throw new SicaCodedException(SicaCodedException.CONTRATO_SICA_NO_ENCONTRADO_IA1,
                        be.getNoCuenta());
            }
            //Map beMap = ssd.cargarBitacorasEnvadas(folioTas, broker, be, cs.getNoCuenta());
            cargarBitacorasEnvadas(-1, idPersonaContraparte, be, cs.getNoCuenta());
            validarDesviaciones(be);
            resultados.add(be);
        }
        // todo: id_usuario
        return insertarSwapKondor(490, idCanal, bes);
        /*BitacoraEnviadas be = (BitacoraEnviadas) bes.get(0);
        getMailKondorService().enviarCorreo(be.getId().getIdConf(), be.getFolio().intValue(),
                be.getFolderk(), MailKondorService.TIPO_ALTA_OPERACION);
        */
    }

    /**
     * Genera un deal interbancario para cada elemento de la lista de Mapas de BitacoraEnviadas.
     *
     * @param idUsuario La clave del usuario que desea generar el swap.
     * @param idCanal   El canal en el que se opera.
     * @param bes       Lista de Mapas que representan registros de la tabla SC_BITACORA_ENVIADAS.
     * @return List de Mapas (ID_CONF, ID_DEAL).
     */
    private RespAltaKondor[] insertarSwapKondor(int idUsuario, String idCanal, List bes) {
        SicaServiceData ssd = getSicaServiceData();
        RespAltaKondor[] resultados = new RespAltaKondor[bes.size()];
        if (bes == null || bes.isEmpty()) {
            throw new SicaCodedException(
                    SicaCodedException.KONDOR_BITACORAS_ENVIADAS_NO_ESPECIFICADAS);
        }
        Canal canal = ssd.findCanal(idCanal);
        BitacoraEnviadasDao beDao = getBitacoraEnviadasDao();
        IUsuario usuario = (IUsuario) getHibernateTemplate().load(Usuario.class,
                new Integer(idUsuario));
        CanalMesa cm = new CanalMesa(canal, canal.getMesaCambio());
        Swap swap = null;
        PizarronServiceData psd = getPizarronServiceData();
        int i = 0;
        for (Iterator it = bes.iterator(); it.hasNext(); i++) {
            BitacoraEnviadas be = (BitacoraEnviadas) it.next();
            Map daMap = be.getDatosAdicionales();
            ContratoSica cs = ssd.findContrato((String) daMap.get(Keys.NO_CUENTA));
            Divisa div = ssd.findDivisa(be.getId().getDivisa());
            if (cs == null) {
                throw new SicaCodedException(SicaCodedException.CONTRATO_SICA_NO_ENCONTRADO_IA1,
                        daMap.get(Keys.NO_CUENTA));
            }
            if (!BitacoraEnviadas.ST_ESTRAT_PENDIENTE.equals(be.getStatusEstrategia())) {
                throw new SicaCodedException(SicaCodedException.KONDOR_FOLIO_EXISTENTE_IA1,
                        be.getFolio());
            }
            be.setStatusEstrategia(BitacoraEnviadas.ST_ESTRAT_INICIO_SWAP);
            beDao.store(be);
            // La primera iteracion se inicializa el swap y se actualizan los registros de
            // SC_BITACORA_ENVIADAS:
            if (swap == null) {
                // todo: folioSwapInicio
                swap = new Swap(be.getFolio().intValue(),
                        ((Boolean) daMap.get("compra")).booleanValue(),
                        be.getMonto().doubleValue(),
                        ((Boolean) daMap.get(Keys.ESTRATEGIA)).booleanValue() ?
                                Swap.TIPO_SWAP_ESTRATEGIA : Swap.TIPO_SWAP_SICA,
                        null, cs, div, be.getFolderk(), getIdConfReducido(be.getId().getIdConf()));
                getHibernateTemplate().save(swap);
            }
            // Se crea el deal:
            Deal deal = new Deal();
            deal.setObservaciones(be.getComentarios());
            deal.setSwap(swap);
            deal.setCanalMesa(cm);
            deal.setBroker((Broker) ssd.
                    findBrokerByIdPersona((Integer) daMap.get(Keys.ID_PERSONA_CONTRAPARTE)).get(0));
            deal.setContratoSica(cs);
            deal.setTipoDeal(Deal.TIPO_INTERBANCARIO);
            deal.setTipoValor((String) daMap.get(Keys.FECHA_VALOR));
            deal.setPromotor(ssd.findPromotorByContratoSica(deal.getContratoSica().getNoCuenta()));
            deal.setUsuario(usuario);
            Date fechaLiquidacion = deal.getTipoValor().equals(Constantes.CASH) ? new Date() :
                    deal.getTipoValor().equals(Constantes.TOM) ? psd.getFechaTOM() :
                            deal.getTipoValor().equals(Constantes.SPOT) ? psd.getFechaSPOT() :
                                    deal.getTipoValor().equals(Constantes.HR72) ?
                                            psd.getFecha72HR() : psd.getFechaVFUT();
            ssd.crearDealInterbancario(deal, ((Boolean) daMap.get(Keys.COMPRA)).booleanValue(),
                    fechaLiquidacion, ((Double) daMap.get("tipoCambioDesplegable")).doubleValue(),
                    Constantes.TRANSFERENCIA, be.getMontoDivisa().doubleValue(), div,
                    canal.getMesaCambio(), usuario, i > 0);
            resultados[i] = new RespAltaKondor(be.getId().getIdConf(),
                    String.valueOf(deal.getIdDeal()));
            if (i == 0) {
                swap.setMonto(be.getMonto().doubleValue());
                swap.setStatus(Swap.STATUS_COMPLETAMENTE_ASIGNADO);
            }
            getHibernateTemplate().update(swap);
        }
        return resultados;
    }

    /**
     * Valida que los datos no sean nulos o contengan valores negativos.
     *
     * @param bes El arreglo de objetos BitacoraEnviadas a revisar.
     */
    private void validarContenido(List bes) {
        // El arreglo debe contener algo:
        if (bes == null || bes.isEmpty()) {
            throw new SicaCodedException(
                    SicaCodedException.KONDOR_BITACORAS_ENVIADAS_NO_ESPECIFICADAS);
        }
        for (Iterator it = bes.iterator(); it.hasNext();) {
            BitacoraEnviadas be = (BitacoraEnviadas) it.next();
            if (be.getId() == null || be.getId().getIdConf() == null ||
                    be.getId().getDivisa() == null) {
                throw new SicaCodedException(SicaCodedException.KONDOR_ID_CONF_O_DIVISA_VACIA);
            }
            if (be.getClaveContraparte() == null) {
                throw new SicaCodedException(SicaCodedException.KONDOR_CLAVE_CONTRAPARTE_VACIA);
            }
            if (be.getPlazo() == null) {
                throw new SicaCodedException(SicaCodedException.KONDOR_PLAZO_VACIO);
            }
            if (be.getTipoOper() == null) {
                throw new SicaCodedException(SicaCodedException.KONDOR_TIPO_OPER_VACIO);
            }
            if (be.getMonto() == null || be.getMonto().doubleValue() < 0.01) {
                throw new SicaCodedException(SicaCodedException.KONDOR_MONTO_VACIO);
            }
            if (be.getTipo() == null) {
                throw new SicaCodedException(SicaCodedException.KONDOR_TIPO_VACIO);
            }
            if (be.getFechaConcertacion() == null) {
                throw new SicaCodedException(SicaCodedException.KONDOR_FECHA_CONCERTACION_VACIA);
            }
            if (be.getFechaLiquidacion() == null) {
                throw new SicaCodedException(SicaCodedException.KONDOR_FECHA_LIQUIDACION_VACIA);
            }
            if (be.getMontoDivisa() == null || be.getMontoDivisa().doubleValue() < 0.01) {
                throw new SicaCodedException(SicaCodedException.KONDOR_MONTO_DIVISA_VACIO);
            }
            if (be.getTipoCambio() == null || be.getTipoCambio().doubleValue() <= 0.0) {
                throw new SicaCodedException(SicaCodedException.KONDOR_TIPO_CAMBIO_VACIO);
            }
            if (be.getFolderk() == null) {
                throw new SicaCodedException(SicaCodedException.KONDOR_FOLDERK_VACIO);
            }
        }
    }

    /**
     * Regresa el n&uacute;mero de persona de la contraparte IxeForwards, de acuerdo al SC_PARAMETRO
     * ID_PERSONA_IXE_FORWARD.
     *
     * @return Integer.
     */
    private Integer getIdPersonaIxeForwards() {
        ParametroSica ps = getSicaServiceData().findParametro(ParametroSica.ID_PERSONA_IXE_FORWARD);
        if (ps == null) {
            throw new SicaCodedException(SicaCodedException.PERSONA_IXE_FORWARDS_NO_ENCONTRADA);
        }
        return Integer.valueOf(ps.getValor());
    }

    /**
     * Regresa el n&uacute;mero de persona que est&aacute; configurado en SC_PARAMETRO bajo la llave
     * 'CONTRATO_FOLDER_KONDOR' + folderK.
     *
     * @param folderk La clave del portafolios de Kondor.
     * @return Integer.
     */
    private Integer getIdPersonaPorFolderKondor(String folderk) {
        ParametroSica ps = getSicaServiceData().findParametro("CONTRATO_FOLDER_KONDOR_" +
                folderk.trim());
        if (ps == null) {
            throw new SicaCodedException(SicaCodedException.KONDOR_PERSONA_FOLDER_ERROR_IA1,
                    folderk);
        }
        return Integer.valueOf(ps.getValor());
    }

    /**
     * Valida que la desviaci&oacute;n al tipo de cambio con respecto al pizarr&oacute;n no exceda
     * el 5%.
     *
     * @param be El registro de Bitacora enviadas a validar.
     */
    private void validarDesviaciones(BitacoraEnviadas be) {
        SicaServiceData ssd = getSicaServiceData();
        Map da = be.getDatosAdicionales();
        getDesviacionInterbancaria().validarDesviacionTc(
                ((Boolean) da.get(Keys.COMPRA)).booleanValue(),
                ssd.findDivisa(be.getId().getDivisa()), (String) da.get(Keys.FECHA_VALOR),
                Constantes.TRANSFERENCIA, be.getContraDivisa(),
                ((Double) da.get("tipoCambioDesplegable")).doubleValue());
    }
    
    /**
     * Regresa el valor de bitacoraEnviadasDao.
     *
     * @return BitacoraEnviadasDao.
     */
    public BitacoraEnviadasDao getBitacoraEnviadasDao() {
        return bitacoraEnviadasDao;
    }

    /**
     * Establece el valor de bitacoraEnviadasDao.
     *
     * @param bitacoraEnviadasDao El valor a asignar.
     */
    public void setBitacoraEnviadasDao(BitacoraEnviadasDao bitacoraEnviadasDao) {
        this.bitacoraEnviadasDao = bitacoraEnviadasDao;
    }

    /**
     * Regresa el valor de desviacionInterbancaria.
     *
     * @return DesviacionInterbancaria.
     */
    public DesviacionInterbancaria getDesviacionInterbancaria() {
        return desviacionInterbancaria;
    }

    /**
     * Establece el valor de desviacionInterbancaria.
     *
     * @param desviacionInterbancaria El valor a asignar.
     */
    public void setDesviacionInterbancaria(DesviacionInterbancaria desviacionInterbancaria) {
        this.desviacionInterbancaria = desviacionInterbancaria;
    }

    /**
     * Regresa el valor de pizarronServiceData.
     *
     * @return PizarronServiceData.
     */
    public PizarronServiceData getPizarronServiceData() {
        return pizarronServiceData;
    }

    /**
     * Establece el valor de pizarronServiceData.
     *
     * @param pizarronServiceData El valor a asignar.
     */
    public void setPizarronServiceData(PizarronServiceData pizarronServiceData) {
        this.pizarronServiceData = pizarronServiceData;
    }

    /**
     * Regresa el valor de sicaServiceData.
     *
     * @return SicaServiceData.
     */
    public SicaServiceData getSicaServiceData() {
        return sicaServiceData;
    }

    /**
     * Establece el valor de sicaServiceData.
     *
     * @param sicaServiceData El valor a asignar.
     */
    public void setSicaServiceData(SicaServiceData sicaServiceData) {
        this.sicaServiceData = sicaServiceData;
    }

    /**
     * Regresa el valor de swapDao.
     *
     * @return SwapDao.
     */
    public SwapDao getSwapDao() {
        return swapDao;
    }

    /**
     * Establece el valor de swapDao.
     *
     * @param swapDao El valor a asignar.
     */
    public void setSwapDao(SwapDao swapDao) {
        this.swapDao = swapDao;
    }

    /**
     * Data Access Object para acceder a registros de la tabla SC_BITACORA_ENVIADAS.
     */
    private BitacoraEnviadasDao bitacoraEnviadasDao;

    /**
     * Bean que realiza validaciones a la desviaci&oacute;n en tipo de cambio.
     */
    private DesviacionInterbancaria desviacionInterbancaria;

    /**
     * Service Data Object para acceder a informaci&oacute;n del pizarr&oacute;n.
     */
    private PizarronServiceData pizarronServiceData;

    /**
     * Service Data Object para acceder a tablas del sica en general.
     */
    private SicaServiceData sicaServiceData;

    /**
     * Data Access Object para acceder a la tabla SC_SWAP.
     */
    private SwapDao swapDao;
}
