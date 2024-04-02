/*
 * $Id: IntegracionTellerTest.java,v 1.17.34.1 2010/10/08 01:23:07 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 - 2009 LegoSoft S.C.
 */

package test.ods.sica;

import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ixe.ods.seguridad.model.SeguridadException;
import com.ixe.ods.sica.Constantes;
import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.SicaTellerSession;
import com.ixe.ods.sica.SicaTellerSessionBean;
import com.ixe.ods.sica.SicaTellerSessionHome;
import com.ixe.ods.sica.model.Deal;
import com.ixe.ods.sica.model.Divisa;
import com.ixe.ods.sica.teller.dto.BancoInternacionalDto;
import com.ixe.ods.sica.teller.dto.ContratoSicaDto;
import com.ixe.ods.sica.teller.dto.DealDetalleDto;
import com.ixe.ods.sica.teller.dto.DealDto;
import com.ixe.ods.sica.teller.dto.FormaPagoLiqDto;
import com.ixe.ods.sica.teller.dto.PersonaDto;
import com.ixe.ods.sica.teller.dto.PlantillaCuentaIxeDto;
import com.ixe.ods.sica.teller.dto.PlantillaInternacionalDto;
import com.ixe.ods.sica.teller.dto.PlantillaNacionalDto;
import com.ixe.ods.sica.teller.dto.PlantillaTranNacionalDto;
import com.ixe.ods.sica.teller.dto.ResultadoOperacionDto;
import com.ixe.ods.sica.teller.dto.TipoCambioDto;

import junit.framework.TestCase;

/**
 * Prueba unitaria para la integracion del SICA con el TELLER.
 * 
 * @author Jean C. Favila
 * @version $Revision: 1.17.34.1 $ $Date: 2010/10/08 01:23:07 $
 */
public class IntegracionTellerTest extends TestCase {

    /**
     * Constructor vac&iacute;o.
     */
    public IntegracionTellerTest() {
        super();
    }

    /**
	 * Levanta el contexto para la prueba.
     *
     * @throws Exception Si ocurre alg&uacute;n problema.
	 */
    protected void setUp() throws Exception {
        super.setUp();
        Properties ps = new Properties();
        ps.put(InitialContext.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
        //ps.put(InitialContext.PROVIDER_URL, "t3://10.4.162.25:7025");
        ps.put(InitialContext.PROVIDER_URL, "t3://127.0.0.1:7001");
        logger.info(ps);
        Context ctx;
        try {
            ctx = new InitialContext(ps);
            SicaTellerSessionHome home = (SicaTellerSessionHome) ctx.
                    lookup("ejb/sica/SicaTellerHome");
            ejb = home.create();
            System.out.println(ejb);
        }
        catch (RemoteException e) {
            fail(e.getMessage());
        } 
    }

    /**
     * Contrato sica
     */
    private static String CONTRATO_SICA_MOLES = "2000773757-2";
    
    /**
     * Id usuario
     */
    private static int ID_USUARIO_PERSONA = 64824;
    
    /**
     * Limpia los recursos ocupados.
     *
     * @throws Exception Si ocurre alg&uacute;n error.
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Prueba el servicio getContratoSica() a trav&eacute;s del criterio de Contrato SICA.
     */
    public void testFindContratoSicaPorContratoSica() { //OK
        try {
            // 2000773757-2
	    	ResultadoOperacionDto res = ejb.getContratoSica(ticket, null, null, null, null, null, null,
	                null, ID_USUARIO_PERSONA, "MX", null, "0000143110332",
                    "2002", "2081090930001", "01", ID_SUCURSAL, "USD",
	                "EFECTI", new Boolean(true), new Integer(490));
        	logger.info("ResultadoOperacionDto: ");
        	logger.info(res);
            System.out.println(res);
        	ContratoSicaDto contrato = (ContratoSicaDto) res.getObjeto();
            logger.info("ejb - getContratoSica");
            logger.info("Contrato: ");
            logger.info(contrato);
        }
        catch (RemoteException e) {
            fail(e.getMessage());
        }
    }

    /**
     * Prueba el servicio getContratoSica() a trav&eacute;s del criterio de cuenta de cheques.
     */
    /*public void testFindContratoSicaPorNoCuentaIxe() { //OK
        try {
            
            ResultadoOperacionDto res = ejb.getContratoSica(ticket, null, "1122370-7", null, null, null, null, null,
            		ID_USUARIO_CAPTURA);
        	logger.info("ResultadoOperacionDto: ");
        	logger.info(res);
        	ContratoSicaDto contrato = (ContratoSicaDto) res.getObjeto();
            logger.info("ejb - getContratoSica");
            logger.info("Contrato: ");
            logger.info(contrato);

        }
        catch (RemoteException e) {
            fail(e.getMessage());
        }
    }*/

    /**
     * Prueba el servicio getContratoSica() a trav&eacute;s del criterio de RFC.
     */
    /*public void testFindContratoSicaPorRfc() { //OK
        try {
            ResultadoOperacionDto res = ejb.getContratoSica(ticket, null, null, "CAN050909NO9 ", null, null, null,
                    null, ID_USUARIO_CAPTURA);
        	logger.info("ResultadoOperacionDto: ");
        	logger.info(res);
        	ContratoSicaDto contrato = (ContratoSicaDto) res.getObjeto();
            logger.info("ejb - getContratoSica");
            logger.info("Contrato: ");
            logger.info(contrato);
        }
        catch (RemoteException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }*/
    
    /**
     * Prueba el servicio getContratoSica() a trav&eacute;s del criterio de RFC.
     */
    /*public void testGetContratoSicaCreandoUsuario() { //No corre en ambiente local
        try {
            ResultadoOperacionDto res = ejb.getContratoSica(ticket, null, null, "REII840326 ", "Israel", "Rebollar", "Ibarra",
                    null, ID_USUARIO_CAPTURA);
        	logger.info("ResultadoOperacionDto: ");
        	logger.info(res);
        	ContratoSicaDto contrato = (ContratoSicaDto) res.getObjeto();
            logger.info("ejb - getContratoSica");
            logger.info("Contrato: ");
            logger.info(contrato);
        }
        catch (RemoteException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }*/
    
    /**
     * Prueba la b&uacute;squeda del tipo de cambio.
     * 
     * @see com.ixe.ods.sica.SicaTellerSessionBean#obtenerTipoDeCambioPorDivisa(String, String,
     *      String, int)
     */
    public void testObtenerTipoCambio() { //OK
        try {
        	ResultadoOperacionDto resultado = (ResultadoOperacionDto) 
        		ejb.obtenerTipoDeCambioPorDivisa(ticket, "USD", 
        				"CHVIAJ", "101");
            System.out.println("ResultadoOperacionDto: ");
            System.out.println(resultado);
            System.out.println("ejb - obtenerTipoDeCambioPorDivisa");
        	TipoCambioDto tipoCambio = (TipoCambioDto) resultado.getObjeto();
            System.out.println("tipoCambio: " + tipoCambio);
        }
        catch (RemoteException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }
    
    /**
     * Prueba el servicio para crear un deal para el teller.
     */
    public void testCrearDealParaTeller() { //OK
        try {
        	List temp = new ArrayList();
        	temp.add(new DealDetalleDto(true, new Double("1000"), new Double("11.11"), 
        			new Double("11.08"), "USD", "EFECTI", null, 
        			new Integer(2492635), new Integer(291), new Integer(179684),  
        			"instruccionesBeneficiario", "instruccionesIntermediario", "instruccionesPagador", 
                    TOKEN, false));
        	ResultadoOperacionDto resp = null; 
        	try {
        		resp =  (ResultadoOperacionDto) ejb.crearDealParaTeller(ticket, CONTRATO_SICA_MOLES, 
        				String.valueOf(ID_SUCURSAL), ID_USUARIO_PERSONA, temp, IP);
        	}
        	catch (SicaException se) {
        		logger.info("LA EXCEPCION FUE: " + se.getMessage());
        	}
        	/*resp =  (ResultadoOperacionDto) ejb.crearDealParaTeller(ticket, "2000566742-9", 
        			ID_SUCURSAL, ID_USUARIO, temp, IP);*/
        	logger.info("Codigo: " + resp.getCodigo());
        	logger.info("Descipci\00f3n: " + resp.getDescripcion());
        	logger.info("Objeto: " + resp.getObjeto().getClass().getName());
        	DealDto dealDto = (DealDto) resp.getObjeto();
        	logger.info(" * Eventos Deal: " + dealDto.getEventosDeal());
        	logger.info(" * Id Canal: " + dealDto.getIdCanal());
        	logger.info(" * No Cuenta: " + dealDto.getNoCuenta());
        	logger.info(" * Observaciones: " + dealDto.getObservaciones());
        	logger.info(" * Status Deal: " + dealDto.getStatusDeal());
        	logger.info(" * Tipo Deal: " + dealDto.getTipoDeal());
        	logger.info(" * Tipo valor: " + dealDto.getTipoValor());
        	logger.info(" * fecha captura: " + dealDto.getFechaCaptura());
        	logger.info(" * fecha de liquidaci\00f3n: " + dealDto.getFechaLiquidacion());
        	logger.info(" * id deal: " + dealDto.getIdDeal());
        	logger.info(" * id liquidaci\00f3n: " + dealDto.getIdLiquidacion());
        	logger.info(" * id mesa cambio: " + dealDto.getIdMesaCambio());
        	logger.info(" * id promotor: " + dealDto.getIdPromotor());
        	logger.info(" * id usuario: " + dealDto.getIdUsuario());
        	for (Iterator iter = dealDto.getDetalles().iterator(); iter.hasNext();) {
				DealDetalleDto dealDetalleDto = (DealDetalleDto) iter.next();
				logger.info(" * * : detalle forma liq: " + dealDetalleDto.getClaveFormaLiquidacion());
				logger.info(" * * : detalle eventos det: " + dealDetalleDto.getEventosDetalle());
				logger.info(" * * : detalle id divisa: " + dealDetalleDto.getIdDivisa());
				logger.info(" * * : detalle instr benef: " + dealDetalleDto.getInstruccionesBeneficiario());
				logger.info(" * * : detalle instr inter: " + dealDetalleDto.getInstruccionesIntermediario());
				logger.info(" * * : detalle instr pagad: " + dealDetalleDto.getInstruccionesPagardor());
				logger.info(" * * : detalle token: " + dealDetalleDto.getToken());
				logger.info(" * * : detalle folio det: " + dealDetalleDto.getFolioDetalle());
				logger.info(" * * : detalle id posici\00f3n: " + dealDetalleDto.getIdDealPosicion());
				logger.info(" * * : detalle id factor div: " + dealDetalleDto.getIdFactorDivisa());
				logger.info(" * * : detalle id plantilla: " + dealDetalleDto.getIdPlantilla());
				logger.info(" * * : detalle id precio ref: " + dealDetalleDto.getIdPrecioReferencia());
				logger.info(" * * : detalle id spread: " + dealDetalleDto.getIdSpread());
				logger.info(" * * : detalle monto: " + dealDetalleDto.getMonto());
				logger.info(" * * : detalle tcc: " + dealDetalleDto.getTcc());
				logger.info(" * * : detalle tcm: " + dealDetalleDto.getTcm());
			}
            //logger.info(ejb.crearDealParaTeller(ticket, "2000234566-8", ID_SUCURSAL, ID_USUARIO, temp));
        }
        catch (RemoteException e) {
            fail(e.getMessage());
        }
    }
    
    /**
     * 
     */
    public void testCrearPlantillaCuentaIxe() { //OK 973545
        try {
            logger.info("Nueva plantilla cuenta ixe: " + ejb.crearPlantillaCuentaIxe(ticket,
                "2000234566-8", "EUSDEFEBO100", "JCF", 973545, "1117914-7"));
        }
        catch (Exception e) {
            fail(e.getMessage());
        }
    }
    
    public void testCrearPlantillaIntl() { //OK
        try {
        	System.out.println(ejb.crearPlantillaInternacional(ticket, "USDDABONIXEB", "2000234566-8", 
        			"1117914-7", 973545, "USD", "ABA", "394837483", "Banco Uno", 
        			"US", "", "", "", "", "", "", ""));
            /*logger.info("Nueva plantilla intl: " + ejb.crearPlantillaInternacional(ticket,
                "2000566742-9", EUSDTRFBOFAM, "JCF", 973545, "USD", "1117914-7", "ABA", "394837483",
                "Banco Uno", "US"));*/
        }
        catch (Exception e) {
            fail(e.getMessage());
        }

    }
    
    public void crearPlantillaNacional() { //OK
        try {
            logger.info("Nueva plantilla intl: " + ejb.crearPlantillaNacional(ticket,
                "2000234566-8", "EUSDEFEBO100", "JCF", 973545));
        }
        catch (Exception e) {
            fail(e.getMessage());
        }

    }
    
    /**
     * Prueba la b&uacute;squeda de bancos internacionales.
     * 
     * @see com.ixe.ods.sica.SicaTellerSessionBean#getBancoInternacional(String, String, String,
     *      String)
     */
    public void getBancoInternacionalByClave() { //OK
        try {
        	ResultadoOperacionDto res = (ResultadoOperacionDto) ejb.getBancoInternacionalByClave(
        			ticket, "ABA", "021000089", Divisa.DOLAR);
            logger.info(res);
            logger.info((BancoInternacionalDto)res.getObjeto());
            
        }
        catch (RemoteException e) {
            fail(e.getMessage());
        }
    }
    
    /**
     * Prueba la b&uacute;squeda de bancos por nombre del Pa&iacute;s.
     *
     * @see com.ixe.ods.sica.SicaTellerSessionBean#getBancosInternacionalesByNombrePais(String,
     *      String, String)
     */
    public void testFindBancosByNombrePais() { //OK
        try {
        	ResultadoOperacionDto res = (ResultadoOperacionDto) 
        		ejb.getBancosInternacionalesByNombrePais(ticket, "BANK OF", "US");
            logger.info(res);
            List list = (List) res.getObjeto();
            for (Iterator iter = list.iterator(); iter.hasNext();) {
				BancoInternacionalDto banco = (BancoInternacionalDto) iter.next();
				logger.info(banco);
			}
        }
        catch (RemoteException e) {
            fail(e.getMessage());
        }

    }

    /**
     * Prueba el servicio getBancosInternacionales.
     *
     * @see com.ixe.ods.sica.SicaTellerSession#getBancosInternacionales(String, java.util.Map).
     */
    public void testFindBancosInternacionales() { //OK
        try {
            Map parametros = new LinkedHashMap();
            parametros.put("tipoClave", "SWIFT");               //String, 'SWIFT', 'CHIPS'
            parametros.put("claveSwift", "DANBDK21ARR"); //String, clave necesaria por SWIFT
            parametros.put("claveChips", ""); //String, clave necesaria para busqueda por CHIPS
            parametros.put("claveRuteo", "DKRN"); //String, cadena con 'ABA' u otra clave ruteo
            // distinta como 'NIB', 'BLZ', ETC...  para casos que no sean SWIFT y CHIPS
            parametros.put("numeroRuteo", "5950"); //String, numero ruteo necesaria para todos
            // excepto SWIFT Y CHIPS
            parametros.put("nombreBanco", "ANDELSKASSE");     //String, nombre del banco
            parametros.put("pais", "denmark"); //String,  nombre pais
            parametros.put("ciudad", "aarre"); //nombre ciudad
            ResultadoOperacionDto bancos = ejb.getBancosInternacionales(ticket, parametros);
            logger.info(bancos);
        }
        catch (RemoteException e) {
            fail(e.getMessage());
        }
    }
    
    /**
     * Prueba el servicio getDivisas.
     *
     * @see com.ixe.ods.sica.SicaTellerSession#getDivisas(String, String).
     */
    public void testGetDivisas() { //OK
        try {
        	//ResultadoOperacionDto result = ejb.getDivisas(ticket, "T");
        	ResultadoOperacionDto result = ejb.getDivisas("11111111111111111", "T");
            logger.info(result);
        }
        catch (RemoteException e) {
            fail(e.getMessage());
        }
    }
    
    /**
     * Prueba el servicio getFormasPagoLiq.
     *
     * @see com.ixe.ods.sica.SicaTellerSession#getFormasPagoLiq(String).
     */
    public void testGetFormasPagoLiq() { //OK
        try {
            ResultadoOperacionDto result = ejb.getFormasPagoLiq(ticket);
            logger.info(result);
        }
        catch (RemoteException e) {
            fail(e.getMessage());
        }
    }
    
    /**
     * Prueba el servicio getPaises.
     *
     * @see com.ixe.ods.sica.SicaTellerSession#getPaises(String).
     */
    public void testGetPaises() { //OK
        try {
            ResultadoOperacionDto result = ejb.getPaises(ticket);
            logger.info(result);
        }
        catch (RemoteException e) {
            fail(e.getMessage());
        }
    }
    
    /**
     * Prueba el servicio marcarDealCancelado.
     *
     * @see com.ixe.ods.sica.SicaTellerSession#marcarDealCancelado(String, int, int).
     */
    public void testMarcarDealCancelado() { 
        try {
            ResultadoOperacionDto result = ejb.marcarDealCancelado(ticket, 224714, ID_USUARIO_CAPTURA);
            logger.info(result);
        }
        catch (RemoteException e) {
            fail(e.getMessage());
        }
    }
    
    /**
     * 
     */
    public void testRegistrarBeneficiarioParaContratoSica() {
    	try {
    		ResultadoOperacionDto result = ejb.registrarBeneficiarioParaContratoSica(ticket, 
    				"2000566742-9", 973548, 0, 689638);
    		logger.info(result);
    	} catch (RemoteException e) {
    		fail(e.getMessage());
    	}
    	
    }
    
    /**
     * Prueba la asignaci&oacute;n del mnemonico al detalle de deal.
     *
     * @see com.ixe.ods.sica.SicaTellerSessionBean#asignarMnemonicoADealDetalle(String, String, int)
     *//*
    public void testAsignarMnemonicoADetalle() {
        try {
            logger.info(ejb.asignarMnemonicoADealDetalle(ticket, EUSDTRFBOFAM, ID_DEAL_POSICION));
        }
        catch (RemoteException e) {
            fail(e.getMessage());
        }
    }*/

    /**
     * Prueba la asignaci&oacute;n de una plantilla al detall de deal.
     *  
     * @see com.ixe.ods.sica.SicaTellerSessionBean#asignarPlantillaADealDetalle(String, int, int)
     *//*
    public void testAsignarPlantillaADetalle() {
        try {
            logger.info(ejb.asignarPlantillaADealDetalle(ticket, ID_PLANTILLA, ID_DEAL_POSICION));
        }
        catch (RemoteException e) {
            fail(e.getMessage());
        }
    }*/

    /**
     * Prueba el envio del deal a 'procesar'.
     *  
     * @see com.ixe.ods.sica.SicaTellerSessionBean#procesarDeal(String, int)
     *//*
    public void testProcesarDeal() {
        try {
            logger.info(ejb.procesarDeal(ticket, 30000));
        }
        catch (RemoteException e) {
            fail(e.getMessage());
        }
    }*/
    
    /**
     * Prueba la b&uacute;squeda de beneficiarios.
     *
     * @see com.ixe.ods.sica.SicaTellerSessionBean#getBeneficiariosParaContrato(String, String)
     *//*
    public void testGetBeneficiarios() {
        try {
            List benefs = ejb.getBeneficiariosParaContrato(ticket, "2000610003-1");
            
            for (Iterator it = benefs.iterator(); it.hasNext();) {
                PersonaDto personaDto = (PersonaDto) it.next();

                logger.info(personaDto);
            }
        }
        catch (RemoteException e) {
            fail(e.getMessage());
        }
    }*/
/*
    public void testCrearBancoInternacional() {
        try {
            Map resultado = ejb.crearBancoInternacional(_ticket, "Banco de prueba " +
                new Date().getTime(), "US", "Illinois", "Chicago", "192837483", null, null);
            int idBanco = ((Integer) resultado.get("idBanco")).intValue();
            assertTrue(idBanco > 0);
            _logger.info("Banco internacional creado: " + idBanco);
        }
        catch (Exception e) {
            fail(e.getMessage());
        }
    }

    public void testCrearPlantillaNacional() {
        try {
            _logger.info("Nueva plantilla nacional: " + ejb.crearPlantillaNacional(_ticket,
                "2000275106-2", "EUSDDOCBOFAM", "JCF", 57821));
        }
        catch (Exception e) {
            fail(e.getMessage());
        }
    }

    

    
*/

    

    /**
     * Prueba la b&uacute;squeda de divisas frecuentes.
     * 
     * @see com.ixe.ods.sica.SicaTellerSessionBean#getIdsDivisasFrecuentes(String)
     *//*
    public void testFindDivisasFrecuentes() {
        try {
            assertTrue(ejb.getIdsDivisasFrecuentes(ticket).size() > 0);
        }
        catch (SeguridadException e) {
            fail(e.getMessage());
        }
        catch (RemoteException e) {
            fail(e.getMessage());
        }
    }*/
    
    /**
     * Prueba la b&uacute;squeda de plantillas internacionales.
     * 
     * @see com.ixe.ods.sica.SicaTellerSessionBean#getPlantillasIntlsParaContrato(String, String,
     *      String)
     *//*
    public void testFindPlantillasIntlsParaContrato() {
        try {
            List plantillas = ejb.getPlantillasIntlsParaContrato(ticket, "2000610003-1",
                    EUSDTRFBOFAM);

            for (Iterator it = plantillas.iterator(); it.hasNext();) {
                PlantillaInternacionalDto p = (PlantillaInternacionalDto) it.next();
                
                logger.info("Plantilla Internacional: " + p);
            }
        }
        catch (RemoteException e) {
            fail(e.getMessage());
        }
    }*/

    /**
     * Prueba la b&uacute;squeda de plantillas Cuenta Ixe.
     *
     * @see com.ixe.ods.sica.SicaTellerSessionBean#getPlantillasCuentaIxeParaContrato(String,
     *          String, String)
     *//*
    public void testFindPlantillasCuentaIxeParaContrato() {
        try {
            List plantillas = ejb.getPlantillasCuentaIxeParaContrato(ticket, "2000254057-6",
                    "RMXNCARGIXEB");

            for (Iterator it = plantillas.iterator(); it.hasNext();) {
                PlantillaCuentaIxeDto p = (PlantillaCuentaIxeDto) it.next();

                logger.info("Plantilla cuenta ixe: " + p);
            }
        }
        catch (RemoteException e) {
            fail(e.getMessage());
        }
    }*/

    /**
     * Prueba la b&uacute;squeda de plantillas de cheques.
     *
     * @see com.ixe.ods.sica.SicaTellerSessionBean#getPlantillasNacionalesParaContrato(String,
     *          String, String)
     *//*
    public void testFindPlantillasNacionalParaContrato() {
        try {
            List plantillas = ejb.getPlantillasNacionalesParaContrato(ticket, "2000234523-4",
                    "EEURESPBOFAM");

            for (Iterator it = plantillas.iterator(); it.hasNext();) {
                PlantillaNacionalDto p = (PlantillaNacionalDto) it.next();

                logger.info("Plantilla de cheques: " + p);
            }
        }
        catch (RemoteException e) {
            fail(e.getMessage());
        }
    }*/
    
    /**
     * Prueba la b&uacute;squeda de plantillas de transferencias nacionales.
     *
     * @see com.ixe.ods.sica.SicaTellerSessionBean#getPlantillasTranNacionalesParaContrato(String,
     *          String, String)
     *//*
    public void testFindPlantillasTranNacionalParaContrato() {
        try {
            List plantillas = ejb.getPlantillasTranNacionalesParaContrato(ticket, "2000234403-3",
                    "EMXNLIBAHSBC");
            
            for (Iterator it = plantillas.iterator(); it.hasNext();) {
                PlantillaTranNacionalDto p = (PlantillaTranNacionalDto) it.next();

                logger.info("Plantilla de transf. nacional: " + p);
            }
        }
        catch (RemoteException e) {
            fail(e.getMessage());
        }
    }*/

    /**
     * Prueba la consulta de deals.
     * 
     * @see com.ixe.ods.sica.SicaTellerSessionBean#getDeal(Map)
     *//*
    public void testGetDeal() {
        try {
            Map resultado = ejb.getDeal(ticket, ID_DEAL);
            assertTrue(((Integer) resultado.get("codigo")).intValue() == 0);
            logger.info("Deal " + ID_DEAL + ": " + resultado.get("deal"));
        }
        catch (RemoteException e) {
            fail(e.getMessage());
        }
    }*/
    
    /**
     * Prueba la consulta de paises.
     * @see com.ixe.ods.sica.SicaTellerSessionBean#getPaises(String)
     *//*
    public void testGetPaises() {
        try {
            Map resultado = ejb.getPaises(ticket);
            List paises = (List) resultado.get("paises");
            assertTrue(!paises.isEmpty());
            for (Iterator it = paises.iterator(); it.hasNext();) {
                Map map = (Map) it.next();
                logger.info("Pais: " + map);
            }
        }
        catch (RemoteException e) {
            fail(e.getMessage());
        }
    }*/
    
    /**
     * Prueba la cancelaci&pacute;n del deal.
     *
     * @see com.ixe.ods.sica.SicaTellerSessionBean#marcarDealCancelado(String, int, int)
     *//*
    public void testMarcarDealCancelado() {
        try {
            logger.info(ejb.marcarDealCancelado(ticket, ID_DEAL, ID_USUARIO));
        }
        catch (RemoteException e) {
            fail(e.getMessage());
        }
    }*/
    
    /**
     * Prueba la asignaci&oacute;n de instrucciones al detalle de deal.
     *
     * @see com.ixe.ods.sica.SicaTellerSessionBean#asignarInstruccionesADealDetalle(String, int,
     *          String, String, String)
     *//*
    public void testAsignarInstruccionesADealDetalle() {
        try {
            String instruccionesBeneficiario = "instruccionesBeneficiarioinstruccionesBeneficiari" +
                    "oinstruccionesBeneficiarioinstruccionesBeneficiarioinstruccionesBeneficiario";
            String instruccionesIntermediario = "instruccionesIntermediarioinstruccionesIntermedi" +
                    "ario";
            String instruccionesPagador = "instruccionesPagadorinstruccionesPagadorinstruccionesP" +
                    "agador";
            logger.info(ejb.asignarInstruccionesADealDetalle(ticket, ID_DEAL_POSICION,
                    instruccionesBeneficiario, instruccionesIntermediario, instruccionesPagador));
        }
        catch (RemoteException e) {
            fail(e.getMessage());
        }
    }*/

    /**
     * Prueba el servicio getMapasDivisasNoFrecuentes.
     *
     * @see com.ixe.ods.sica.SicaTellerSessionBean#getMapasDivisasNoFrecuentes(String, boolean).
     *//*
    public void testGetMapasDivisasNoFrecuentes() {
        try {
            logger.info(ejb.getMapasDivisasNoFrecuentes(ticket, true));
        }
        catch (RemoteException e) {
            fail(e.getMessage());
        }
    }*/

    /**
     * Prueba el servicio consultarDeals() por rango de fechas, para un canal en particular.
     *//*
    public void testConsultarDealsPorFecha() {
        Calendar gc = new GregorianCalendar();
        gc.add(Calendar.DAY_OF_MONTH, -DIAS_ATRAS);
        logger.info(gc.getTime());
        try {
            Map resultado = ejb.consultarDeals(ticket, gc.getTime(), new Date(), null, null, null,
                    ID_SUCURSAL);
            logger.info("** Deals: " + ((List) resultado.get(DEALS_KEY)).size());
            logger.info(resultado);
        }
        catch (RemoteException e) {
            fail(e.getMessage());
        }
    }*/

    /**
     * Prueba el servicio consultarDeals() para un deal en particular.
     *//*
    public void testConsultarDealsPorIdDeal() {
        try {
            Map resultado = ejb.consultarDeals(ticket, null, null, null, new Integer(ID_DEAL), null,
                    ID_SUCURSAL);
            logger.info("Deals: " + ((List) resultado.get(DEALS_KEY)).size());
            logger.info(resultado);
        }
        catch (RemoteException e) {
            fail(e.getMessage());
        }
    }*/

    /**
     * Prueba el servicio getFormasPagoLiq().
     *
     * @see com.ixe.ods.sica.SicaTellerSession#getFormasPagoLiq(String).
     *//*
    public void testGetFormasPagoLiq() {
        try {            
            for (Iterator it = ejb.getFormasPagoLiq(ticket).iterator(); it.hasNext();) {
                FormaPagoLiqDto dto = (FormaPagoLiqDto) it.next();
                
                logger.info(dto);
            }
        }
        catch (RemoteException e) {
            fail(e.getMessage());
        }
    }*/

    /**
     * Instancia del EJB del TELLER.
     */
    private SicaTellerSession ejb;

    /**
     * El mnem&oacute;nico para
     */
    private String EUSDTRFBOFAM = "EUSDTRFBOFAM";
    
    /**
     * 
     */
    private String RUSDCARGIXEB = "RUSDCARGIXEB";

    /**
     * La constante para el canal de pruebas (8).
     */
    private static final int ID_SUCURSAL = 11;

    /**
     * Constante para el n&uacute;mero de d&iacute;as para la consulta.
     */
    private static final int DIAS_ATRAS = 30;

    /**
     * Constante para el n&uacute;mero de usuario.
     */
    private static final int ID_USUARIO = 709;
    
    private static String IP = "127.0.0.1";

    /**
     * Constante para el n&uacute;mero de deal.
     */
    private static final int ID_DEAL = 30000;

    /**
     * Constante para el n&uacute;mero de detalle de deal.
     */
    private static final int ID_DEAL_POSICION = 51648;

    /**
     * Constante para el n&uacute;mero de plantilla.
     */
    private static final int ID_PLANTILLA = 1841;

    /**
     * Constante para la llave 'deals' para los mapas.
     */
    private static final String DEALS_KEY = "deals";
    
    /**
     * Constante para el ID del usuario que captura
     */
    private static final int ID_USUARIO_CAPTURA = 689638;

    /**
     * El objeto para logging.
     */
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * Constante para el valor del ticket.
     */
    private static final String ticket = "8a8422992b4f2712012b54fee3bc0047";
    //private String ticket = "402897c006f1a12f01070155a8bf0180";
    
    private static final String TOKEN = "8a829b252b7cfe32012b7d17f79a0018";
    
    /**
     * 
     */
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
}