/*
 * $Id: ModeloXls.java,v 1.2 2008/12/26 23:17:35 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2008 LegoSoft S.C.
 */

package com.ixe.ods.sica.services.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Modelo para generar un bloque de celdas de excel con t&iacute;tulo, subt&iacute;tulos y renglones
 * de datos.
 *
 * @author Jean C. Favila
 * @version  $Revision: 1.2 $ $Date: 2008/12/26 23:17:35 $
 */
public class ModeloXls implements Serializable {

    /**
     * Constructor por default.
     */
    public ModeloXls() {
        super();
    }

    /**
     * Constructor para inicializar todas las variables de instancia.
     *
     * @param titulo El t&iacute;tulo del bloque.
     * @param subtitulos La lista de subt&iacute;tulos para el bloque.
     * @param propiedades La lista de expresiones ognl para generar los valores del bloque.
     * @param datos La lista de objetos que responde a las propiedades (expresiones ognl), y de
     * donde se sacar&aacute; el contenido del bloque.
     */
    public ModeloXls(String titulo, List subtitulos, List propiedades, List datos) {
        this();
        this.titulo = titulo;
        this.subtitulos = subtitulos;
        this.propiedades = propiedades;
        this.datos = datos;
    }

    /**
     * Constructor para inicializar todas las variables de instancia.
     *
     * @param titulo El t&iacute;tulo del bloque.
     * @param subtits La lista de subt&iacute;tulos para el bloque.
     * @param props La lista de expresiones ognl para generar los valores del bloque.
     * @param datos La lista de objetos que responde a las propiedades (expresiones ognl), y de
     * donde se sacar&aacute; el contenido del bloque.
     */
    public ModeloXls(String titulo, String[] subtits, String[] props, List datos) {
        this();
        this.titulo = titulo;
        subtitulos = new ArrayList();
        subtitulos.addAll(Arrays.asList(subtits));
        propiedades = new ArrayList();
        propiedades.addAll(Arrays.asList(props));
        this.datos = datos;
    }

    /**
     * Regresa el valor de titulo.
     *
     * @return String.
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * Establece el valor de titulo.
     *
     * @param titulo El valor a asignar.
     */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    /**
     * Regresa el valor de subtitulos.
     *
     * @return List.
     */
    public List getSubtitulos() {
        return subtitulos;
    }

    /**
     * Establece el valor de subtitulos.
     *
     * @param subtitulos El valor a asignar.
     */
    public void setSubtitulos(List subtitulos) {
        this.subtitulos = subtitulos;
    }

    /**
     * Regresa el valor de propiedades.
     *
     * @return List.
     */
    public List getPropiedades() {
        return propiedades;
    }

    /**
     * Establece el valor de propiedades.
     *
     * @param propiedades El valor a asignar.
     */
    public void setPropiedades(List propiedades) {
        this.propiedades = propiedades;
    }

    /**
     * Regresa el valor de datos.
     *
     * @return List.
     */
    public List getDatos() {
        return datos;
    }

    /**
     * Establece el valor de datos.
     *
     * @param datos El valor a asignar.
     */
    public void setDatos(List datos) {
        this.datos = datos;
    }

    /**
     * El t&iacute;tulo principal del bloque.
     */
    private String titulo = "";

    /**
     * La lista de encabezados para las columnas del bloque.
     */
    private List subtitulos = new ArrayList();

    /**
     * La lista de expresiones ognl para generar los valores del bloque.
     */
    private List propiedades = new ArrayList();

    /**
     * La lista de objetos que responde a las propiedades (expresiones ognl), y de donde se
     * sacar&aacute; el contenido del bloque.
     */
    private List datos = new ArrayList();

    /**
     * El UID para serializaci&oacute;n.
     */
    private static final long serialVersionUID = -3259187436516955130L;
}
