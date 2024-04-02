/*
 * $Id: PdfServiceTest.java,v 1.3 2008/12/26 23:17:28 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2008 LegoSoft S.C.
 */

package test.ods.sica;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.ixe.ods.sica.services.impl.PdfServiceImpl;
import com.ixe.ods.sica.services.model.ModeloPdf;

import junit.framework.TestCase;

/**
 * Prueba unitaria para la clase GeneradorPdf.
 *
 * @author Jean C. Favila
 * @version  $Revision: 1.3 $ $Date: 2008/12/26 23:17:28 $
 */
public class PdfServiceTest extends TestCase {

    /**
     * Inicializa el application context.
     *
     * @throws Exception Si ocurre alg&uacute;n error.
     */
    protected void setUp() throws Exception {
        super.setUp();
    }

    /**
     * Regresa la lista de datos que se escribir&aacute;n en el archivo de prueba.
     *
     * @param indice El &iacute;ndice para diferenciar los registros.
     * @return List de HashMap.
     */
    private List getDatos(int indice) {
        List datos = new ArrayList();
        HashMap mapa = new HashMap();
        mapa.put(CAMPO1_KEY, indice + "valor11");
        mapa.put(CAMPO2_KEY, indice + "valor12");
        datos.add(mapa);
        mapa = new HashMap();
        mapa.put(CAMPO1_KEY, indice + "valor21");
        mapa.put(CAMPO2_KEY, indice + "valor22");
        datos.add(mapa);
        return datos;
    }

    /**
     * Regresa la lista de expresiones ognl para realizar las pruebas.
     *
     * @return List.
     */
    private List getPropiedades() {
        List propiedades = new ArrayList();
        propiedades.add(CAMPO1_KEY);
        propiedades.add(CAMPO2_KEY);
        return propiedades;
    }

    /**
     * Regresa la lista de t&iacute;tulos para realizar las pruebas.
     *
     * @param indice El indice para diferenciar los t&iacute;tulos.
     * @return List.
     */
    private List getTitulos(int indice) {
        List titulos = new ArrayList();
        titulos.add(indice + "CAMPO 1");
        titulos.add(indice + "CAMPO 2");
        return titulos;

    }

    /**
     * Prueba el servicio utilizando un s&oacute;lo modelo.
     */
    public void testSinModelos() {
        FileOutputStream archivo = null;
        try {
            archivo = new FileOutputStream("/tmp/SinModelos.pdf");
            new PdfServiceImpl().escribirPdf(null, "El titulo",
                new ModeloPdf(EL_PRIMERO_STR, getTitulos(CINCO),
                getPropiedades(), getDatos(CINCO), new String[] {"rc0", "rc0"}), archivo);
        }
        catch (Exception e) {
            fail(e.getMessage());
        }
        finally {
            try {
                if (archivo != null) {
                    archivo.close();
                }
            }
            catch (IOException e) {
                fail(e.getMessage());
            }
        }
    }

    /**
     * Prueba el servicio utilizando una lista de modelos.
     */
    public void testConModelos() {
        List modelos = new ArrayList();
        modelos.add(new ModeloPdf(EL_PRIMERO_STR, getTitulos(CINCO), getPropiedades(),
                getDatos(CINCO), new String[] {"rc0", "rc0"}));
        modelos.add(new ModeloPdf("El segundo", getTitulos(DIEZ), getPropiedades(),
                getDatos(DIEZ), new String[] {"rc0", "rc0"}));
        FileOutputStream archivo = null;
        try {
            archivo = new FileOutputStream("/tmp/ConModelos.pdf");
            new PdfServiceImpl().escribirPdf(null, "Titulo", modelos, archivo);
        }
        catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
        finally {
            try {
                if (archivo != null) {
                    archivo.close();
                }
            }
            catch (IOException e) {
                fail(e.getMessage());
            }
        }
    }

    /**
     * Constante para el valor 5.
     */
    private static final int CINCO = 5;

    /**
     * Constante para el valor 10.
     */
    private static final int DIEZ = 10;

    /**
     * Constante para la cadena "El primero".
     */
    private static final String EL_PRIMERO_STR = "El primero";

    /**
     * Constante para la cadena "campo1".
     */
    private static final String CAMPO1_KEY = "campo1";

    /**
     * Constante para la cadena "campo2".
     */
    private static final String CAMPO2_KEY = "campo2";
}

