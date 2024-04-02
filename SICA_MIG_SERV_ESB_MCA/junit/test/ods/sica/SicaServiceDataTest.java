/*
 * $Id: SicaServiceDataTest.java,v 1.26 2009/11/17 16:53:52 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2006 - 2009 LegoSoft S.C.
 */

package test.ods.sica;

import com.ixe.ods.bup.model.ContratoSica;
import com.ixe.ods.bup.model.Direccion;
import com.ixe.ods.bup.model.EjecutivoOrigen;
import com.ixe.ods.bup.model.EmpleadoIxe;
import com.ixe.ods.bup.model.Persona;
import com.ixe.ods.bup.model.PersonaCuentaRol;
import com.ixe.ods.bup.model.PersonaMoral;
import com.ixe.ods.bup.model.SectorEconomico;
import com.ixe.ods.seguridad.model.SeguridadException;
import com.ixe.ods.sica.Constantes;
import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.model.Actividad;
import com.ixe.ods.sica.model.Broker;
import com.ixe.ods.sica.model.Canal;
import com.ixe.ods.sica.model.Deal;
import com.ixe.ods.sica.model.DealDetalle;
import com.ixe.ods.sica.model.Divisa;
import com.ixe.ods.sica.model.FactorDivisa;
import com.ixe.ods.sica.model.FechaInhabilEua;
import com.ixe.ods.sica.model.LineaCambio;
import com.ixe.ods.sica.model.MesaCambio;
import com.ixe.ods.sica.model.PlantillaPantalla;
import com.ixe.ods.sica.model.RecoUtilidad;
import com.ixe.ods.sica.pizarron.SicaPizarronService;
import com.ixe.ods.sica.sdo.WorkFlowServiceData;
import com.ixe.ods.sica.vo.WorklistTotalVO;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import junit.framework.TestCase;

/**
 * Pruebas de unidad para los servicios de SicaServiceData
 *
 * @author Jean C. Favila
 * @version $Revision: 1.26 $ $Date: 2009/11/17 16:53:52 $
 */
public class SicaServiceDataTest extends TestCase {

    /**
     * Constructor por default.
     */
    public SicaServiceDataTest() {
        super();
    }

    /**
     * Inicializa el application context.
     *
     * @throws Exception Si ocurre alg&uacute;n error.
     */
	protected void setUp() throws Exception {
        super.setUp();
        String[] paths = {"junit/applicationContext.xml"};
        appContext = new FileSystemXmlApplicationContext(paths);
        sdo = (WorkFlowServiceData) appContext.getBean("sicaServiceData");
    }

	/**
     * Limpia los recursos ocupados.
     *
     * @throws Exception Si ocurre alg&uacute;n error.
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testActualizarMontoOperadoSucursalRecibimos() {
        try {
            sdo.actualizarMontoOperadoSucursal("EMP", Divisa.DOLAR, Constantes.TRANSFERENCIA, true,
                    new BigDecimal("500.34"));
        }
        catch (SicaException e) {
            fail(e.getMessage());
        }
    }

    public void testActualizarMontoOperadoSucursalEntregamos() {
        try {
            sdo.actualizarMontoOperadoSucursal("EMP", Divisa.DOLAR, Constantes.TRANSFERENCIA, false,
                    new BigDecimal("333.34"));
        }
        catch (SicaException e) {
            fail(e.getMessage());
        }
    }

    /**
     * Realiza la prueba del servicio findFactoresDivisaActuales().
     *
     * @see com.ixe.ods.sica.sdo.WorkFlowServiceData#findFactoresDivisaActuales().
     */
    public void testFindFactoresDivisaActuales() {
        sdo.findFactoresDivisaActuales();
    }

    /**
     * Realiza la prueba del servicio findDealsPendientesDia().
     * @see com.ixe.ods.sica.sdo.WorkFlowServiceData#findDealsPendientesDia(int).
     */
    public void testFindDealsPendientesDia() {
        List deals = sdo.findDealsPendientesDia(184898);
        for (Iterator it = deals.iterator(); it.hasNext();) {
            Deal deal = (Deal) it.next();
            logger.info(deal);
        }
    }

    /**
     * Realiza la prueba del servicio findDireccion().
     *
     * @see com.ixe.ods.sica.sdo.SicaServiceData#findDireccion(int).
     */
    public void testFindDireccion() {
        Direccion dir = sdo.findDireccion(20);
        logger.info("Calle y n\u00fam: " + dir.getCalleYNumero());
        logger.info("Delegaci\u00f3n: " + dir.getDelegacionMunicipio());
        logger.info("Pais: " + dir.getPais().getDescripcion());
        logger.info("Tipo: " + dir.getTipoDireccion().getDescripcion());
    }

    /**
     * Realiza la prueba del servicio findAllBrokersInstFin. La prueba es exitosa si la lista de
     * contrapartes no est&aacute; vac&iacute;a.
     *
     * @see com.ixe.ods.sica.sdo.SicaServiceData#findAllBrokersInstFin().
     */
    public void testFindAllBrokersInstFin() {
        List brokers = sdo.findAllBrokersInstFin();
        assertFalse(brokers.isEmpty());
        for (Iterator iterator = brokers.iterator(); iterator.hasNext();) {
            Broker b = (Broker) iterator.next();
            logger.info(b.getClaveReuters());
        }
    }

    /**
     * Realiza la prueba del servicio findPosicionByIdMesaCambioAndIdDivisa().
     *
     * @see com.ixe.ods.sica.sdo.WorkFlowServiceData#findPosicionByIdMesaCambioAndIdDivisa(int,
            String).
     */
    public void testFindPosicionByIdMesaCambioAndIdDivisa() {
        logger.info(sdo.findPosicionByIdMesaCambioAndIdDivisa(1, "XCE"));
    }

    /**
     * Realiza la prueba del servicio findPromotorByContratoSica. La prueba es exitosa si regresa un
     * promotor para el contrato sica especificado.
     *
     * @see com.ixe.ods.sica.sdo.SicaServiceData#findPromotorByContratoSica(String).
     */
    public void testFindPromotorByContratoSica() {
        try {
            EmpleadoIxe promotor = sdo.findPromotorByContratoSica("2000441066-1");
            assertTrue(promotor != null);
            logger.info(promotor.getNombreCompleto());
        }
        catch (SicaException e) {
            fail(e.getMessage());
        }
    }

    /**
     * Realiza la prueba del servicio findContratoSicaByIdPersona. La prueba es exitosa si existe un
     * contrato sica para el n&uacute;mero de persona proporcionado.
     *
     * @see com.ixe.ods.sica.sdo.SicaServiceData#findContratoSicaByIdPersona(Integer).
     */
    public void testFindContratoSicaByIdPersona() {
        ContratoSica cs = sdo.findContratoSicaByIdPersona(new Integer(950561));
        assertTrue(cs != null);
        for (Iterator it = cs.getRoles().iterator(); it.hasNext();) {
            PersonaCuentaRol pcr = (PersonaCuentaRol) it.next();
            logger.info(pcr.getId().getCuenta().getNoCuenta() + " "
                    + pcr.getId().getPersona().getNombreCompleto() + " " + pcr.getId().getRol()
                    + " " + pcr.getParticipacion());
        }
    }

    /**
     * Prueba el servicio de findContrato().
     *
     * @see com.ixe.ods.sica.sdo.SicaServiceData#findContrato(String).
     */
    public void testFindContrato() {
        Date fechaInicio = new Date();
        ContratoSica cs = sdo.findContrato("2000441066-1");
        logger.info("Tiempo: " + ((new Date().getTime() - fechaInicio.getTime()) / 1000)
                + " segs.");
        assertTrue(cs != null);
    }

    /**
     * Prueba el servicio de findContrato().
     *
     * @see com.ixe.ods.sica.sdo.SicaServiceData#findContrato(String).
     */
    public void findContratoSicaConSectorEconomico() {
        Date fechaInicio = new Date();
        ContratoSica cs = sdo.findContratoSicaConSectorEconomico("2000441066-1");
        logger.info("Tiempo: " + ((new Date().getTime() - fechaInicio.getTime()) / 1000)
                + " segs.");
        assertTrue(cs != null);
        for (Iterator it = cs.getRoles().iterator(); it.hasNext();) {
            PersonaCuentaRol pcr = (PersonaCuentaRol) it.next();
            logger.info(pcr.getId().getPersona().getSectorEconomico().getDescripcion());
        }
    }

    /**
     * Prueba el servicio getPrsLimDesv.
     *
     * @see com.ixe.ods.sica.sdo.SicaServiceData#getParsLimDesv().
     */
    public void testGetParsLimDesv() {
        Map params = sdo.getParsLimDesv();
        assertFalse(params.isEmpty());
        logger.info(params);
    }

    /**
     * Prueba el cambio de estatus de deals pendientes a 'Procesados'.
     *
     * @see com.ixe.ods.sica.sdo.WorkFlowServiceData#wfIniciarProcesoDealsPendientes(String)
     */
    public void testProcesarDealsPendientes() {
        try {
            sdo.wfIniciarProcesoDealsPendientes(ticket);
            assertTrue(true);
        }
        catch (SicaException e) {
            fail(e.getMessage());
        }
    }

    /**
     * Prueba la obtencion de los datos del pizarr&oacute;n para divisas no frecuentes.
     *
     * @see com.ixe.ods.sica.sdo.PizarronServiceData#obtenerPizarronOtrasDivisas(boolean)
     */
    public void testObtenerPizarronOtrasDivisas() {
        SicaPizarronService serv = (SicaPizarronService) appContext.
                getBean("pizarronService");
        logger.info("No Frecuentes: ");
        List noFrecuentes = serv.obtenerPizarronOtrasDivisas(false);
        for (Iterator it = noFrecuentes.iterator(); it.hasNext();) {
            logger.info(it.next());
        }
        logger.info("Metales: ");
        List metales = serv.obtenerPizarronOtrasDivisas(true);
        for (Iterator it = metales.iterator(); it.hasNext();) {
            logger.info(it.next());
        }
    }

    /**
     * Prueba el servicio findDealsCanceladosCapturaRapida().
     *
     * @see com.ixe.ods.sica.sdo.DealServiceData#findDealsCapturaRapida(java.util.Date,
            java.util.Date, String, Integer, boolean)
     */
    public void testFindDealsCanceladosCapturaRapida() {
/*        try {
            Collection deals = sdo.findDealsCapturaRapida(new Date(), new Date(), null,
                    null, true);
            for (Iterator it = deals.iterator(); it.hasNext();) {
                Deal deal = (Deal) it.next();
                logger.info("Deal canc. capt. rap.: " + deal.getIdDeal() + " "
                        + deal.getFechaCaptura() + " " + deal.getStatusDeal() + " "
                        + deal.getCanalMesa().getCanal().getIdCanal() + " "
                        + deal.getUsuario().getPersona().getNombreCompleto());
                for (Iterator it2 = deal.getDetalles().iterator(); it2.hasNext();) {
                    DealDetalle dealDetalle = (DealDetalle) it2.next();
                    logger.info("   " + dealDetalle.getDivisa().getIdDivisa() + " "
                            + dealDetalle.getMonto());
                }
            }
        }
        catch (SicaException e) {
            fail(e.getMessage());
        }*/
    }

    /**
     * Prueba la b&uacute;squeda de deals.
     *
     * @see com.ixe.ods.sica.sdo.ServiceData#findDeal(int)
     */
    public void testFindDeal() {
        try {
            Deal deal = sdo.findDeal(27058);
            logger.info("1. ***" + deal.getContratoSica()  + " " + deal.getUsuario() + " "
                    + deal.getPromotor() + " " + ((DealDetalle) deal.getDetalles().get(0)).
                    getDivisa().getDescripcion() + deal.getCanalMesa().getCanal().getNombre());
            deal = sdo.findDeal(14126);
            logger.info(deal.getContratoSica()  + " " + deal.getUsuario() + " "
                    + deal.getPromotor() + " " + ((DealDetalle) deal.getDetalles().get(0)).
                    getDivisa().getDescripcion());
        }
        catch (SicaException e) {
            fail(e.getMessage());
        }
    }

    /**
     * Prueba la b&uacute;squeda de deals.
     *
     * @see com.ixe.ods.sica.sdo.ServiceData#findDealConCanal(int)
     */
    public void testFindDealConCanal() {
        try {
            Deal deal = sdo.findDealConCanal(27686);
            logger.info(deal.getContratoSica()  + " " + deal.getUsuario() + " "
                    + deal.getPromotor() + " " + ((DealDetalle) deal.getDetalles().get(0)).
                    getDivisa().getDescripcion());
            if (deal.getCanalMesa().getCanal().getSucursal() != null) {
                logger.info(deal.getCanalMesa().getCanal().getSucursal().getNombre());
            }
        }
        catch (SicaException e) {
            fail(e.getMessage());
        }
    }

    /**
     * Prueba la b&uacute;squeda de detalles de deal.
     *
     * @see com.ixe.ods.sica.sdo.SicaServiceData#findDealDetalle(int)
     */
    public void ttestFindDealDetalle() {
        try {
            DealDetalle det = sdo.findDealDetalle(13092);
            logger.info(det.getIdDealPosicion() + " " + det.getDivisa().getIdDivisa());
        }
        catch (SicaException e) {
            fail(e.getMessage());
        }
    }

    /**
     * Prueba la b&uacute;squeda de l&iacute;neas de cr&eacute;dito para el cierre.
     *
     * @see com.ixe.ods.sica.sdo.SicaServiceData#findAllLineaCreditoCierre()
     */
    public void testFindLineasCreditoCierre() {
        List lcs = sdo.findAllLineaCreditoCierre();
        for (Iterator it = lcs.iterator(); it.hasNext();) {
            LineaCambio lc = (LineaCambio) it.next();
            logger.info(lc.getIdLineaCambio() + " " + lc.getCorporativo().
                    getNombreCompleto());
        }
    }

    /**
     * Prueba la b&uacute;squeda del factor para metales amonedados.
     *
     * @see com.ixe.ods.sica.sdo.SicaServiceData#findDivisasMetalesFactor()
     */
    public void testFindDivisasMetalesFactor() {
        List lcs = sdo.findDivisasMetalesFactor();
        assertTrue(! lcs.isEmpty());
        for (Iterator it = lcs.iterator(); it.hasNext();) {
            FactorDivisa fd = (FactorDivisa) it.next();
            logger.info(fd.getFacDiv().getToDivisa().getDescripcion());
        }
    }

    /**
     * Prueba la b&uacute;squeda del factor para divisas no frecuentes.
     *
     * @see com.ixe.ods.sica.sdo.SicaServiceData#findDivisasNoFrecuentesFactor()
     */
    public void testFindDivisasNoFrecuentesFactor() {
        List lcs = sdo.findDivisasNoFrecuentesFactor();
        assertTrue(! lcs.isEmpty());
        for (Iterator it = lcs.iterator(); it.hasNext();) {
            FactorDivisa fd = (FactorDivisa) it.next();
            logger.info(fd.getFacDiv().getToDivisa().getDescripcion());
        }
    }

    /**
     * Prueba la b&uacute;squeda de deals anteriores a tres meses.
     *
     * @see com.ixe.ods.sica.sdo.SicaServiceData#findAllDealsTresMeses(Date)
     */
    public void testFindAllDealsTresMeses() {
        List deals = sdo.findAllDealsTresMeses(new Date());
        for (Iterator it = deals.iterator(); it.hasNext(); ) {
            Deal d = (Deal) it.next();
            logger.info("Id deal: " + d.getIdDeal());
        }
    }

    /**
     * Prueba el servicio de findAllSectores72HR.
     *
     * @see com.ixe.ods.sica.sdo.SicaServiceData#findAllSectoresUltimaFechaValor().
     */
    public void testFindAllSectores72HR() {
        List sectores = sdo.findAllSectoresUltimaFechaValor();
        assertFalse(sectores.isEmpty());
        for (Iterator it = sectores.iterator(); it.hasNext();) {
            SectorEconomico sectorEconomico = (SectorEconomico) it.next();
            logger.info(sectorEconomico.getDescripcion());
        }
    }

    /**
     * Prueba la b&uacute;squeda de deals no cancelados y sin contrato sica.
     *
     * @see com.ixe.ods.sica.sdo.SicaServiceData#findDealsNoCancelYSinContrato()
     */
    public void testFindDealsNoCancelYSinContrato() {
        List deals = sdo.findDealsNoCancelYSinContrato();
        for (Iterator it = deals.iterator(); it.hasNext(); ) {
            Deal d = (Deal) it.next();
            logger.info("Id Deal: " + d.getIdDeal());
        }
    }

    /**
     * Prueba la b&uacute;squeda de deals con alertas.
     *
     * @see com.ixe.ods.sica.sdo.SicaServiceData#findDealsAlertas()
     */
    public void testFindDealsAlertas() {
        List deals = sdo.findDealsAlertas();
        for (Iterator it = deals.iterator(); it.hasNext(); ) {
            Deal d = (Deal) it.next();
            logger.info("Deal: " + d.getIdDeal());
        }
    }

    /**
     * Prueba el servicio <code>findFechasInhabilesEua()</code>
     *
     * @see com.ixe.ods.sica.sdo.SicaServiceData#findFechasInhabilesEua().
     */
    public void testFindFechasInhabilesEua() {
        List fechas = sdo.findFechasInhabilesEua();
        for (Iterator it = fechas.iterator(); it.hasNext();) {
            FechaInhabilEua fecha = (FechaInhabilEua) it.next();
            logger.info(fecha.getIdFechaInhabilEua().getFecha());
        }
    }
    
    /**
     * Prueba la b&uacute;squeda de deals.
     *
     * @see com.ixe.ods.sica.sdo.SicaServiceData#findDeals(boolean, Date, Date, Date, Date,
            String, Integer, Integer, String, String, String, String)
     */
    public void testFindDeals() {
        GregorianCalendar gc = new GregorianCalendar();
        gc.add(Calendar.DAY_OF_MONTH, -1);
        List ds = sdo.findDeals(false, gc.getTime(), new Date(), gc.getTime(), new Date(), null,
                null, null, null, null, null, null);
        logger.info(String.valueOf(ds.size()));
        for (Iterator it = ds.iterator(); it.hasNext();) {
            Deal r = (Deal) it.next();
            logger.info(r.getIdDeal() + " " +  ((DealDetalle) r.getDetalles().get(0)).
                    getFolioDetalle() + ((DealDetalle) r.getDetalles().get(0)).getDivisa());
        }
    }

    /**
     * Prueba el servicio findAutMediosContactoPersona().
     *
     * @see com.ixe.ods.sica.sdo.SicaServiceData#findAutMediosContactoPersona(int).
     */
    public void testFindMediosContactoPorAutorizar() {
        List amcs = sdo.findAutMediosContactoPersona(694713);
        for (Iterator it = amcs.iterator(); it.hasNext();) {
            logger.info(it.next());
            
        }
    }

    /**
     * Prueba la b&uacute;squeda de deals.
     *
     * @see com.ixe.ods.sica.sdo.SicaServiceData#findDeals(boolean, Date, Date, Date, Date, String,
            Integer, Integer, String, String, String, String)
     */
    public void testFindPersonaMoralByIdPersona() {
        PersonaMoral pm = sdo.findPersonaMoralByIdPersona(new Integer(950561));
        if (pm != null) {
            logger.info(pm.getRazonSocial() + " " + pm.getSectorEconomico().
                    getDescripcion());
        }
        else {
            fail("No se encontr\u00f3 la persona moral");
        }
    }

    /**
     * Prueba la b&uacute;squeda de personas morales no brokers por su raz&oacute;n social.
     *
     * @see com.ixe.ods.sica.sdo.SicaServiceData#findPersonaMoralNotBrokerByRazonSocial(String)
     */
    public void testFindPersonaMoralNotBrokerByRazonSocial() {
        List pms = sdo.findPersonaMoralNotBrokerByRazonSocial("BANO");
        assertFalse(pms.isEmpty());
        for (Iterator iterator = pms.iterator(); iterator.hasNext();) {
            PersonaMoral pm = (PersonaMoral) iterator.next();
            logger.info(pm.getIdPersona() + " " + pm.getRazonSocial() + " ");
            if (pm.getSectorEconomico() != null) {
                logger.info(pm.getSectorEconomico().getDescripcion() + " ");
            }
            if (pm.getActividadEconomica() != null) {
                logger.info(pm.getActividadEconomica().getDescripcion());
            }
        }
    }

    /**
     * Prueba la b&uacute;squeda de plantillas pantalla por mnem&poacute;nico.
     *
     * @see com.ixe.ods.sica.sdo.SicaServiceData#findPlantillaPantallaByMnemonico(String)
     */
    public void testFindPlantillaPantallaByMnemonico() {
        try {
            PlantillaPantalla pp = sdo.findPlantillaPantallaByMnemonico("EUSDTRFBOFAM");
            logger.info(pp.getNombrePantalla());
        }
        catch (SicaException e) {
            fail(e.getMessage());
        }
    }

    /**
     * Prueba la b&uacute;squeda de brokers.
     *
     * @see com.ixe.ods.sica.sdo.SicaServiceData#findAllBrokers()
     */
    public void testFindAllBrokers() {
        Date fechaInicio = new Date();
        List brokers = sdo.findAllBrokers();
        logger.info("*** Tiempo: " + ((new Date().getTime() - fechaInicio.getTime()) / 1000)
                + " segs.");
        for (Iterator it = brokers.iterator(); it.hasNext();) {
            Broker b = (Broker) it.next();
            logger.info(b.getId().getPersonaMoral().getRazonSocial() + " "
                    + b.getClaveReuters() + " ");
            if (b.getId().getPersonaMoral().getSectorEconomico() != null) {
                logger.info(b.getId().getPersonaMoral().getSectorEconomico().getDescripcion());
            }
        }
    }

    /**
     * Prueba la b&uacute;squeda del canal por el id de la sucursal.
     *
     * @see com.ixe.ods.sica.sdo.SicaServiceData#findCanalByIdSucursal(int)
     */
    public void testFindCanalByIdSucursal() {
        Canal c = sdo.findCanalByIdSucursal(119);
        logger.info(c.getNombre() + " " + c.getMesaCambio().getNombre()
                /*+ " " + c.getSucursal()*/);
    }

    /**
     * Prueba la b&uacute;squeda de las mesas de cambio por su id.
     *
     * @see com.ixe.ods.sica.sdo.SicaServiceData#findMesaCambio(int)
     */
    public void testFindMesaCambio() {
        MesaCambio m = sdo.findMesaCambio(1);
        logger.info(m.getNombre() + " " + m.getDivisaReferencia().getDescripcion());
    }

    /**
     * Prueba la b&uacute;squeda del canal por su id.
     *
     * @see com.ixe.ods.sica.sdo.SicaServiceData#findCanal(String)
     */
    public void testFindCanal() {
        try {
            Canal c = sdo.findCanal("8");
            logger.info(c.getNombre() + " " + c.getMesaCambio().getNombre() + " "
                    + c.getMesaCambio().getDivisaReferencia().getDescripcion());
        }
        catch (SicaException e) {
            fail(e.getMessage());
        }
    }

    /**
     * Prueba la b&uacute;squeda de las divisas por mesa de cambio.
     *
     * @see com.ixe.ods.sica.sdo.SicaServiceData#findDivisasByCanal(String).
     */
    public void testFindDivisasByMesa() {
        List divs = sdo.findDivisasByTipoPizarron(1);
        for (Iterator it = divs.iterator(); it.hasNext();) {
            Divisa d = (Divisa) it.next();
            logger.info(d.getIdDivisa() + " " + d.getDescripcion());
        }
    }
    
    /**
     * Prueba la b&uacute;squeda del factor de la divisa por su id.
     * 
     * @see com.ixe.ods.sica.sdo.SicaServiceData#findFactorDivisa(int)
     */
    public void testFindFactorDivisa() {
        FactorDivisa fd = sdo.findFactorDivisa(76);
        logger.info(fd.getIdFactorDivisa() + " " + fd.getFacDiv().getFromDivisa().
                getDescripcion() + " " + fd.getFacDiv().getToDivisa().getDescripcion());
    }

    /**
     * Prueba el servicio findEjecutivoOrigenByIdPersona.
     *
     * @see com.ixe.ods.sica.sdo.SicaServiceData#findEjecutivoOrigenByIdPersona(Integer).
     */
    public void testFindEjecutivoOrigenByIdPersona() {
        EjecutivoOrigen eje = sdo.findEjecutivoOrigenByIdPersona(new Integer(1089797));
        logger.info(eje.getNoEmpleado());
    }

    /**
     * Prueba el servicio findUtilidadesGlobales.
     *
     * @see com.ixe.ods.sica.sdo.SicaServiceData#findUtilidadesGlobales(java.util.Date,
            java.util.Date)
     */
    public void testFindUtilidadesGlobales() {
        GregorianCalendar gc = new GregorianCalendar();
        gc.set(Calendar.DAY_OF_MONTH, 9);
        gc.set(Calendar.MONTH, 6);
        gc.set(Calendar.YEAR, 2007);
        List utilidades = sdo.findUtilidadesGlobales(gc.getTime(), new Date());
        assertFalse("No se encontraron registros de utilidad", utilidades.isEmpty());
        for (Iterator it = utilidades.iterator(); it.hasNext();) {
            RecoUtilidad ut = (RecoUtilidad) it.next();
            logger.info(ut.getFechaOperacion() + " " + ut.getDivisa().getIdDivisa() + " " +
                    ut.getMonto() + " " + ut.getTipoReco() + " " + ut.isManual() + " " +
                    ut.isRecibimos());
        }
    }

    /**
     * Prueba el servicio findCuentasEje.
     *
     * @see com.ixe.ods.sica.sdo.SicaServiceData#findCuentasEje(Integer).
     */
    public void testFindCuentasEje() {
        String[] ctas = sdo.findCuentasEje(new Integer(2257642));
        logger.info("Hay " + ctas.length + " ctas.");
        for (int i = 0; i < ctas.length; i++) {
            logger.info(ctas[i]);
        }
    }

    /**
     * Prueba el servicio findDealsBlockerVespertino.
     */
    public void testFindDealsBlockerVespertino() {
        try {
            List deals = sdo.findDealsBlockerVespertino();
            for (Iterator it = deals.iterator(); it.hasNext();) {
                Deal deal = (Deal) it.next();
                logger.info(deal.getIdDeal() + " " + deal.getContratoSica());
            }
            logger.info(String.valueOf(deals.size()));
        }
        catch (SicaException e) {
            fail(e.getMessage());
        }
    }

    /**
     * Prueba el servicio findAllActividadesPendientes().
     *
     * @see com.ixe.ods.sica.sdo.WorkFlowServiceData#findAllActividadesPendientes(String, String).
     */
    public void testFindAllActividadesPendientes() {
        try {
            List actividades = sdo.findAllActividadesPendientes(ticket,
                    Actividad.ACT_DN_DOCUMENTACION);
            logger.info("Actividades encontradas: " + actividades.size());
        }
        catch (SeguridadException e) {
            fail(e.getMessage());
        }
    }

    /**
     * Prueba el servicio findActividadesPendientesTotales().
     *
     * @see com.ixe.ods.sica.sdo.WorkFlowServiceData#findActividadesPendientesTotales(boolean).
     */
    public void testFindActividadesPendientesTotales() {
        List objs = sdo.findActividadesPendientesTotales(true);
        for (Iterator it = objs.iterator(); it.hasNext();) {
            WorklistTotalVO total = (WorklistTotalVO) it.next();
            logger.info(total);

        }
    }

    public void testFindPlantillasByContratoMnemonicos() {
        List plantillas = sdo.findPlantillasByContratoMnemonicos(ticket, "2000234570-6",
                "EMXNCHEBNORT", null, "MXN", false);
        logger.info("Plantillas encontradas: " + plantillas.size());
    }

    /**
     * Prueba el servicio <code>findMontoChequesViajeroUsdFechaActual()</code>.
     *
     * @see com.ixe.ods.sica.sdo.SicaServiceData#findMontoChequesViajeroUsdFechaLiquidacion(String,
     *          java.util.Date).
     */
    public void testFindMontoChequesViajeroUsdFechaActual() {
        logger.info("" + sdo.findMontoChequesViajeroUsdFechaLiquidacion("2000234570-6",
                new Date()));
    }

    /**
     * Prueba el servicio <code>findEjecutivosIxeDirecto()</code>.
     *
     * @see com.ixe.ods.sica.sdo.SicaServiceData#findEjecutivosIxeDirecto().
     */
    public void testFindIxeDirecto() {
        List ejecutivos = sdo.findEjecutivosIxeDirecto();
        for (Iterator it = ejecutivos.iterator(); it.hasNext();) {
            Persona persona = (Persona) it.next();
            logger.info(persona.getIdPersona() + " " + persona.getNombreCorto());
        }
    }

    /**
     * El objeto para logging.
     */
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * El contexto de la aplicaci&oacute;n.
     */
    private ApplicationContext appContext;
    
    /**
     * Instancia de WorkFlowServiceData.
     */
    private WorkFlowServiceData sdo;
        
    /**
     * Constante para el valor del ticket de la sesi&oacute;n.
     */
    private String ticket = "402897c006f1a12f01070155a8bf0180";
}
