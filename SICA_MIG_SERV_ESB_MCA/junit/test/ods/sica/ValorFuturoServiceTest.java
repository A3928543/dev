/*
 * $Id: ValorFuturoServiceTest.java,v 1.13 2009/11/17 16:54:18 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2006 - 2009 LegoSoft S.C.
 */

package test.ods.sica;

import com.ixe.ods.sica.services.ValorFuturoService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import junit.framework.TestCase;

/**
 * Pruebas de unidad para los servicios de ValorFuturoService.
 * 
 * @author Jean C. Favila
 * @version $Revision: 1.13 $ $Date: 2009/11/17 16:54:18 $
 */
public class ValorFuturoServiceTest extends TestCase {

    /**
     * Constructor por default.
     */
    public ValorFuturoServiceTest() {
        super();
    }

    /**
     * Inicializa el application context.
     * 
     * @throws Exception Si no se puede inicializar el application context de spring.
     */
    protected void setUp() throws Exception {
        super.setUp();
        String[] paths = {"junit/applicationContext.xml"};
        appContext = new FileSystemXmlApplicationContext(paths);
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
     * Prueba la asignaci&oacute;n de d&iacute;a inh&aacute;bil en EUA.
     * 
     * @see com.ixe.ods.sica.services.ValorFuturoService#esFestivaEnEua(Date).
     */
    public void testDiaInhabil() {
        try {
            ValorFuturoService vfs = (ValorFuturoService) appContext.getBean("valorFuturoService");
            Date fecha = new SimpleDateFormat("dd/MM/yyyy").parse("04/07/2011");
            assertTrue("El 4 de julio debe ser inhabil", vfs.esFestivaEnEua(fecha));
        }
        catch (ParseException e) {
            fail("Error al parsear la fecha");
        }
    }
    
    /**
     * Prueba la asignaci&oacute;n de d&iacute;a inh&aacute;bil en EUA.
     * 
     * @see com.ixe.ods.sica.services.ValorFuturoService#esFestivaEnEua(Date).
     */
    public void testDiaHabil() {
        try {
            ValorFuturoService vfs = (ValorFuturoService) appContext.getBean("valorFuturoService");
            Date fecha = new SimpleDateFormat("dd/MM/yyyy").parse("13/10/2006");
            assertTrue("El 13 de octubre debe ser habil", !vfs.esFestivaEnEua(fecha));
        }
        catch (ParseException e) {
            fail("Error al parsear la fecha");
        }
    }
    
    /**
     * El contexto de la aplicaci&oacute;n.
     */
    private ApplicationContext appContext;
}

