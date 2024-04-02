package test.ods.sica;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.model.Corte;
import com.ixe.ods.sica.model.CorteDetalle;
import com.ixe.ods.sica.model.EstatusCorte;
import com.ixe.ods.sica.sdo.CorteMurexServiceData;
import com.ixe.ods.sica.services.FTPService;

import junit.framework.TestCase;


/**
 * Clase de pruebas
 * 	para objetos de Acceso a datos y
 * 	generación de archivo de corte.
 *
 * @author Diego Pazaran
 *  Banorte.
 *
 * @version $Revision: 1.1.2.1 $
 */
public class CorteMurexDaoTest extends TestCase {
 

    /**
     * El contexto de la aplicaci&oacute;n.
     */
    private ApplicationContext appContext;

    /**
     * Instancia de WorkFlowServiceData.
     */
    private CorteMurexServiceData ssd;

    /** Instancia de servicios FTP */
    private FTPService ftpService;

    /**
     * Inicializa contexto de spring
     *
     * @throws Exception en caso 
     * 	de algún error al levantar el contexto.
     */
    protected void setUp() throws Exception {
        super.setUp();

        String[] paths = { "junit/applicationContext.xml" };
        appContext = new FileSystemXmlApplicationContext(paths);
        ssd = (CorteMurexServiceData) appContext.getBean("corteMurexServiceData");

        ftpService = (FTPService) appContext.getBean("ftpService");
    }

    /**
     * Limpia los recursos ocupados.
     *
     * @throws Exception Si ocurre alg&uacute;n error.
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Recupera un registro de SC_ESTATUS_CORTE
     */
    public void testConsultaEstatusCorte() {
        try {
            EstatusCorte ec = ssd.findEstatusCorteByEstatus("VP");

            System.out.println("Estatus:" + ec.getEstatusCorte());
            System.out.println("Descripcion:" + ec.getDescripcion());
        }
        catch (SicaException e) {
            fail(e.getMessage());
        }
    }

    /**
     * Genera un registro de corte en SC_CORTE
     */
    public void testRegistraCorteMurex() {
        try {
            Corte c = ssd.insertarCorteMurex("A3745707");

            System.out.println("IdCorte:" + c.getIdCorte());
            System.out.println("Usuario:" + c.getUsuario());
            System.out.println("Estatus: " + c.getEstatusCorte());
            System.out.println("FechaAlta:" + c.getFechaAlta());
            System.out.println("UsuarioUltMod:" + c.getUsuarioUltMod());
            System.out.println("FechaUlMod:" + c.getFechaUltMod());
        }
        catch (SicaException e) {
            fail(e.getMessage());
        }
    }

    /**
     * Actualiza el estatus de un cuorte ME!
     */
    public void testActualizaCorteMurex() {
        try {
            Corte c = ssd.actualizarEstatusCorte(13, "garciaa", "EM", null);

            System.out.println("IdCorte:" + c.getIdCorte());
            System.out.println("Usuario:" + c.getUsuario());
            System.out.println("Estatus: " + c.getEstatusCorte());
            System.out.println("FechaAlta:" + c.getFechaAlta());
            System.out.println("UsuarioUltMod:" + c.getUsuarioUltMod());
            System.out.println("FechaUlMod:" + c.getFechaUltMod());
        }
        catch (SicaException e) {
            fail(e.getMessage());
        }
    }

    /**
     * Busca el corte del día de hoy.
     */
    public void testFindCorteFechaHoy() {
        try {
            Corte c = ssd.findCorteByFechaHoy();

            if (c != null) {
                System.out.println("IdCorte:" + c.getIdCorte());
                System.out.println("Usuario:" + c.getUsuario());
                System.out.println("Estatus: " + c.getEstatusCorte());
                System.out.println("FechaAlta:" + c.getFechaAlta());
                System.out.println("UsuarioUltMod:" + c.getUsuarioUltMod());
                System.out.println("FechaUlMod:" + c.getFechaUltMod());
            }
            else {
                System.out.println("No se encontró el corte del día de hoy");
            }
        }
        catch (SicaException e) {
            fail(e.getMessage());
        }
    }

    /**
     * Registra un detalle del corte.
     */
    public void testRegistrarCorteDetalle() {
        CorteDetalle cd = new CorteDetalle();

        cd.setTradeDate(new Date());
        cd.setTheirPortfolio(" ");
        cd.setBuySell(CorteMurexServiceData.DET_CORTE_BUY);
        cd.setContract("USD/MXN");
        //cd.setDeliveryDateLabel(new Date());
        cd.setAmount(new Double("14500").doubleValue());
        cd.setPrice(new Double("12.87654321").doubleValue());
        cd.setFundingCost(new Double("13.12345678").doubleValue());
        cd.setDeliveryDate(ssd.findCorteByFechaHoy().getFechaAlta());
        cd.setCcy("EUR");
        cd.setSourceComments("PRUEBA: " + Math.round(100 * Math.random()));

        int res = ssd.registrarDetalleCorte(cd, ssd.findCorteByFechaHoy().getIdCorte());

        System.out.println("Se registro el detalle: " + res + " para el corte: " + cd.getIdCorte());
    }

    /**
     * Recupera los detalles asociados a un cote.
     */
    public void testBuscarDetallesCorte() {
        List detallesCorte = ssd.findDetallesCortebyIdCorte(ssd.findCorteByFechaHoy().getIdCorte());

        Iterator iter = detallesCorte.iterator();

        for (; iter.hasNext();) {
            final CorteDetalle cd = (CorteDetalle) iter.next();
            System.out.print("idCorte: " + cd.getIdCorte());
            System.out.print(" idDetalle: " + cd.getIdCorteDetalle());
            System.out.print(" type: " + cd.getType1());
            System.out.print(" action: " + cd.getAction());
            System.out.print(" sourceModule: " + cd.getSourceModule());
            System.out.print(" tradedate: " + cd.getTradeDate());
            System.out.print(" ourName: " + cd.getOurName());
            System.out.print(" theirName: " + cd.getTheirName());
            System.out.print(" ourPortfolio: " + cd.getOurPortfolio());
            System.out.print(" user: " + cd.getUser1());
            System.out.print(" group: " + cd.getGroup1());
            System.out.print(" theirportfolio: " + cd.getTheirPortfolio());
            System.out.print(" buySell" + cd.getBuySell());
            System.out.print(" contract: " + cd.getContract());
            System.out.print(" deliveryDate: " + cd.getDeliveryDate());
            System.out.print(" deliveryDatelabel: " + cd.getDeliveryDateLabel());
            System.out.print(" amount: " + cd.getAmount());
            System.out.print(" price: " + cd.getPrice());
            System.out.print(" fundingCost: " + cd.getFundingCost());
            System.out.print(" ccy: " + cd.getCcy());
            System.out.print(" acountingSectionSource: " + cd.getAcountingSectionSource());
            System.out.print(" sourceComments: " + cd.getSourceComments());
            System.out.print(" backToBackPortfolioName: " + cd.getBackToBackPortfolioName());
            System.out.println(" tradeGlobalId" + cd.getTradeGlobalId());
        }
    }

    /**
     * Crea el archivo de corte que 
     *  se envia a Murex.
     *
     * @throws IOException En caso de que 
     * 	ocurra algún error.
     */
    public void testCrearArchivoCorteMurex() throws IOException {
        
    	File file = new File(ftpService.getNombreArchivoCorte());
        FileOutputStream fos = new FileOutputStream(file);

        fos.write(ftpService.getRegistrosArchivoCorte());

        fos.close();
    }

    /**
     * En caso de existir lee y borra el archivo de corte 
     * 	que se envia a Murex.
     */
    public void testLeerArchivoCorteMurex() {
    	String linea = null;
    	File file = null;
        BufferedReader br = null;
        InputStreamReader isr = null;

        try {
            file = new File(ftpService.getNombreArchivoCorte());

            if (!file.exists()) {
                System.out.println("El archivo no existe");
            }
            else {
                FileInputStream fis = new FileInputStream(file);
                isr = new InputStreamReader(fis);
                br = new BufferedReader(isr);


                while ((linea = br.readLine()) != null) {
                    System.out.println(linea);
                }
            }
        }
        catch (FileNotFoundException fnfe) {
            System.out.println("No se encontró el archivo");
        }
        catch (IOException ie) {
            System.out.println("Error al leer el archivo");
        }
        finally {
            try {
                br.close();
                isr.close();
                
                file.delete();
            }
            catch (IOException ie) {
                System.out.println("Error al cerrar el archivo");
            }
        }
    }
}
