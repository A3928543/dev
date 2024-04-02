package com.ixe.ods.sica.services.model;

import java.util.List;

/**
 * @author Jean C. Favila
 *         $Revision: 1.2 $ $Date: 2008/12/26 23:17:35 $
 */
public class ModeloPdf extends ModeloXls {

    public ModeloPdf() {
        super();
    }

    public ModeloPdf(String titulo, List subtitulos, List propiedades, List datos,
                     String[] formatos) {
        super(titulo, subtitulos, propiedades, datos);
        this.formatos = formatos;
    }

    public ModeloPdf(String titulo, String[] subtits, String[] props, List datos,
                     String[] formatos) {
        super(titulo, subtits, props, datos);
        this.formatos = formatos;
    }

    public String[] getFormatos() {
        return formatos;
    }

    public void setFormatos(String[] formatos) {
        this.formatos = formatos;
    }

    private String[] formatos;
}
