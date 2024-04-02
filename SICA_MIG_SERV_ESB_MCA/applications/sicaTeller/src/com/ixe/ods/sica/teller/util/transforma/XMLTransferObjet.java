package com.ixe.ods.sica.teller.util.transforma;

import com.ixe.fcesb.transformacion.Transformador;
import com.ixe.fcesb.transformacion.TransformadorException;
import com.ixe.fcesb.transformacion.TransformadorFactory;

/**
 * Clase para transformar de xml a objeto y viceversa.
 * @author Lorena Bernal
 *
 */
public class XMLTransferObjet {
    /**
     * Instancia a transformador
     */
    private Transformador transformador;

    /**
     * Constructor de la clase
     */
    public XMLTransferObjet() {
        cargaTransformadorDefault();
    }
    /**
     * Método que transforma de xml a objetos
     * @param xml parametro del xml
     * @param clase parametro de la clase
     * @return objeto
     * @throws TransformadorException error de transformador
     */
    public Object getTransferObjectfromXML(String xml, Class clase)
        throws TransformadorException {
        Object objeto = null;
        objeto = transformador.unmarshall(xml, clase);

        return objeto;
    }
    /**
     * Método que transforma de objeto a xml
     * @param bean parametro del objeto
     * @param clase parametro de la clase
     * @return objeto
     * @throws TransformadorException error de trasnformado
     */
    public String getXMLfromTransferObject(Object bean, Class clase)
        throws TransformadorException {
        String objeto = null;
        objeto = transformador.marshall(bean, clase);

        return objeto;
    }
    /**
     * Método que carga el trasnformador
     */
    private void cargaTransformadorDefault() {
        TransformadorFactory factoryTransformador = new TransformadorFactory();
        this.transformador = factoryTransformador.getTransformador();
    }
}