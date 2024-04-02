package com.ixe.ods.sica.pages.catalogos;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IPropertySelectionModel;

import com.ixe.ods.sica.Constantes;
import com.ixe.ods.sica.Visit;
import com.ixe.ods.sica.model.Divisa;
import com.ixe.ods.sica.model.LimiteEfectivo;
import com.ixe.ods.sica.pages.SicaPage;
import com.ixe.ods.sica.pizarron.Consts;
import com.legosoft.tapestry.model.RecordSelectionModel;

/**
 * Cat&aacute;logo de l&iacute;mites para restricci&oacute;n de efectivo.
 *
 * @author lvillegas
 * @version $Revision: 1.1.2.2 $ $Date: 2010/10/25 18:08:28 $
 */
public abstract class CatLimitesEfectivo extends SicaPage {

	/**
	 * Asigna los valores.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
	public void activate(IRequestCycle cycle) {
		super.activate(cycle);
        
        List lista = new ArrayList();
        List divisas = getSicaServiceData().findDivisasFrecuentes();
        Map mapa;
        for (Iterator i = divisas.iterator(); i.hasNext(); ) {
            mapa = new HashMap();
            Divisa divisa = (Divisa) i.next();
            mapa.put(VALUE, divisa.getIdDivisa());
            mapa.put(LABEL, divisa.getIdDivisa());
            lista.add(mapa);
        }
        setListaDivisas(lista);
        
        lista = new ArrayList();
        lista.add(new MapaPantalla(Constantes.COMPRA, Boolean.TRUE));
        lista.add(new MapaPantalla(Constantes.VENTA, Boolean.FALSE));
        setListaTipoOperaciones(lista);
        setCompraSeleccionada(Boolean.TRUE);
        
        lista = new ArrayList();
        lista.add(new MapaPantalla("Persona Física", Constantes.PERSONA_FISICA));
        lista.add(new MapaPantalla("Persona Moral", Constantes.PERSONA_MORAL));
        setListaTipoPersonas(lista);
        setTipoPersonaSeleccionada(Constantes.PERSONA_FISICA);
        
        lista = new ArrayList();
        lista.add(new MapaPantalla());
        lista.add(new MapaPantalla("Cliente", Boolean.TRUE));
        lista.add(new MapaPantalla("Usuario", Boolean.FALSE));
        setListaTipoClientes(lista);
        
        lista = new ArrayList();
        lista.add(new MapaPantalla());
        lista.add(new MapaPantalla("Zona Fronteriza", Constantes.ZONA_FRONTERIZA));
        lista.add(new MapaPantalla("Zona Turística", Constantes.ZONA_TURISTICA));
        lista.add(new MapaPantalla("Zona Normal", Constantes.ZONA_NORMAL));
        setListaTipoZonas(lista);
        
        lista = new ArrayList();
        lista.add(new MapaPantalla());
        lista.add(new MapaPantalla("Sí", Boolean.TRUE));
        lista.add(new MapaPantalla("No", Boolean.FALSE));
        setListaEsMexicano(lista);
        
        setLimites(getSicaServiceData().findAll(LimiteEfectivo.class));
	}

	/**
     * Crea una nueva instancia del registro LimiteEfectivo.
     *
     * @param cycle El IRequestCycle.
     */
	public void insert(IRequestCycle cycle) {
        if (!isCamposValidos()) {
            return;
        }
        LimiteEfectivo limite = new LimiteEfectivo(
            (String) getDivisaSeleccionada().get(VALUE),
            getCompraSeleccionada(),
            getTipoPersonaSeleccionada(),
            getClienteSeleccionado(),
            getTipoZonaSeleccionada(),
            getEsMexicano(),
            new Double(getLimiteDiario()),
            new Double(getLimiteMensual()),
            getObservaciones(),
            new Date(),
            new Date(),
            ((Visit) getVisit()).getUser()
        );
        if (!isReglaUnica(limite)) {
            return;
        }
        getSicaServiceData().store(limite);
        setLevel(1);
        getDelegate().record("La regla se agreg\u00f3 exitosamente.", null);
        setLimites(getSicaServiceData().findAll(LimiteEfectivo.class));
	}
    
    /**
     * M&eacute;todo que hace la validaci&oacute;n de los campos que se desean
     * insertar.
     * @return boolean que determina si los campos son v&aacute;lidos o no.
     */
    private boolean isCamposValidos() {
        boolean valido = true;
        setLevel(0);
        if (getDivisaSeleccionada() == null) {
            getDelegate().record("Favor de seleccionar una divisa.", null);
            valido = false;
        }
        else if (getCompraSeleccionada() == null) {
            getDelegate().record("Favor de seleccionar el tipo de operaci\u00f3n " +
                "(compra o venta).", null);
            valido = false;
        }
        else if (getTipoPersonaSeleccionada() == null) {
            getDelegate().record("Favor de seleccionar un tipo de persona " +
                "(f\u00edsica o moral).", null);
            valido = false;
        }
        else if (getLimiteDiario() < 0.0 || getLimiteDiario() > Consts.NUMD_999999999P99) {
            getDelegate().record("El l\u00edmite diario debe ser un valor mayor " +
                "o igual a cero y menor a 1,000,000,000.00.", null);
            valido = false;
        }
        else if (getLimiteMensual() < 0.0 || getLimiteMensual() > Consts.NUMD_999999999P99) {
            getDelegate().record("El l\u00edmite mensual debe ser un valor mayor " +
                "o igual a cero y menor a 1,000,000,000.00.", null);
            valido = false;
        }
        else if (getLimiteDiario() > getLimiteMensual()) {
            getDelegate().record("El l\u00edmite diario no puede ser mayor al " +
                "l\u00edmite mensual.", null);
            valido = false;            
        }
        else if (getObservaciones().length() > 255) {
            getDelegate().record("Las observaciones no pueden ser mayores a 255 caracteres.", null);
            valido = false;
        }
        return valido;
    }
    
    /**
     * M&eacute;todo que verifica que la regla que se obtiene como par&aacute;metro
     * no se encuentre registrada en la lista actual.
     * @param limite que se desea validar.
     * @return boolean que determina si la regla se encuentra registrada o no.
     */
    private boolean isReglaUnica(LimiteEfectivo limite) {
        LimiteEfectivo registrado =
            getSicaServiceData().findLimitesByCriteria(
                getSicaServiceData().findDivisa(limite.getDivisa()),
                limite.getCompra(), limite.getTipoPersona(),
                limite.getCliente(), limite.getTipoZona(),
                limite.getMexicano());
        if (registrado != null) {
            setLevel(0);
            getDelegate().record("La regla que intent\u00f3 agregar ya existe " +
                "o es excluyente con otra. Recuerde que 'Cualquiera' incluye " +
                "a todos en la regla.", null);
        }
        return registrado == null;
    }
	
	/**
     * Elimina la regla de neteo seleccionada.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void eliminar(IRequestCycle cycle) {
        LimiteEfectivo limite = (LimiteEfectivo) getSicaServiceData().find(
                LimiteEfectivo.class,
    			cycle.getServiceParameters()[0].toString());
        if (limite != null) {
            getSicaServiceData().delete(limite);
            setLevel(1);
            getDelegate().record("La regla se elimin\u00f3 exitosamente.", null);
        }
        setLimites(getSicaServiceData().findAll(LimiteEfectivo.class));
        setCompraSeleccionada(Boolean.TRUE);
        setTipoPersonaSeleccionada(Constantes.PERSONA_FISICA);
    }
    
    /**
     * Regresar la lista de divisas que se pueden asociar a una regla.
     * @return List.
     */
    public abstract List getListaDivisas();
    
    /**
     * Guarda la lista de divisas que se pueden asociar a una regla.
     * @param lista que se desea guardar.
     */
    public abstract void setListaDivisas(List lista);
    
    /**
     * Regresa la divisa seleccionada de una lista.
     * @return String.
     */
    public abstract Map getDivisaSeleccionada();
    
    /**
     * Selecciona la divisa de una lista.
     * @param mapa que se desea seleccionar.
     */
    public abstract void setDivisaSeleccionada(Map mapa);
    
    /**
     * Regresar la lista de tipos de operaciones que se pueden asociar a una regla.
     * @return List.
     */
    public abstract List getListaTipoOperaciones();
    
    /**
     * Guarda la lista de operaciones que se pueden asociar a una regla.
     * @param lista que se desea guardar.
     */
    public abstract void setListaTipoOperaciones(List lista);

    /**
     * Regresa el tipo de operaci&oacute;n seleccionada de una lista.
     * @return Boolean.
     */
    public abstract Boolean getCompraSeleccionada();
    
    /**
     * Selecciona el tipo de operaci&oacute;n de una lista.
     * @param bool que se desea seleccionar.
     */
    public abstract void setCompraSeleccionada(Boolean bool);
    
    /**
     * Regresar la lista de tipos de persona que se pueden asociar a una regla.
     * @return List.
     */
    public abstract List getListaTipoPersonas();
    
    /**
     * Guarda la lista de personas que se pueden asociar a una regla.
     * @param lista que se desea guardar.
     */
    public abstract void setListaTipoPersonas(List lista);

    /**
     * Regresa el tipo de persona seleccionada de una lista.
     * @return String.
     */
    public abstract String getTipoPersonaSeleccionada();
    
    /**
     * Selecciona el tipo de persona de una lista.
     * @param cadena que se desea seleccionar.
     */
    public abstract void setTipoPersonaSeleccionada(String cadena);
    
    /**
     * Regresar la lista de clientes que se pueden asociar a una regla.
     * @return List.
     */
    public abstract List getListaTipoClientes();
    
    /**
     * Guarda la lista de clientes que se pueden asociar a una regla.
     * @param lista que se desea guardar.
     */
    public abstract void setListaTipoClientes(List lista);
    
    /**
     * Regresa el tipo de cliente seleccionada de una lista.
     * @return Boolean.
     */
    public abstract Boolean getClienteSeleccionado();
    
    /**
     * Selecciona el tipo de cliente de una lista.
     * @param bool que se desea seleccionar.
     */
    public abstract void setClienteSeleccionado(Boolean bool);
    
    /**
     * Regresar la lista de zonas que se pueden asociar a una regla.
     * @return List.
     */
    public abstract List getListaTipoZonas();
    
    /**
     * Guarda la lista de zonas que se pueden asociar a una regla.
     * @param lista que se desea guardar.
     */
    public abstract void setListaTipoZonas(List lista);
    
    /**
     * Regresa el tipo de zona seleccionada de una lista.
     * @return String.
     */
    public abstract String getTipoZonaSeleccionada();
    
    /**
     * Selecciona el tipo de zona de una lista.
     * @param cadena que se desea seleccionar.
     */
    public abstract void setTipoZonaSeleccionada(String cadena);
    
    /**
     * Regresar la lista de nacionalidades que se pueden asociar a una regla.
     * @return List.
     */
    public abstract List getListaEsMexicano();
    
    /**
     * Guarda la lista de nacionalidades que se pueden asociar a una regla.
     * @param lista que se desea guardar.
     */
    public abstract void setListaEsMexicano(List lista);
    
    /**
     * Regresa el tipo de nacionalidad seleccionada de una lista.
     * @return Boolean.
     */
    public abstract Boolean getEsMexicano();
    
    /**
     * Selecciona el tipo de nacionalidad de una lista.
     * @param bool que se desea seleccionar.
     */
    public abstract void setEsMexicano(Boolean bool);
    
    /**
     * Regresa el l&iacute;mite diario.
     * @return double.
     */
    public abstract double getLimiteDiario();
    
    /**
     * Guarda el l&iacute;mite diario.
     * @param numero que se desea guardar.
     */
    public abstract void setLimiteDiario(double numero);

    /**
     * Regresa el l&iacute;mite mensual.
     * @return double.
     */
    public abstract double getLimiteMensual();
    
    /**
     * Guarda el l&iacute;mite mensual.
     * @param numero que se desea guardar.
     */
    public abstract void setLimiteMensual(double numero);
    
    /**
     * Regresa las observaciones.
     * @return String.
     */
    public abstract String getObservaciones();
    
    /**
     * Guarda las observaciones.
     * @param cadena que se desea guardar.
     */
    public abstract void setObservaciones(String cadena);
    
    /**
     * Regresar la lista de reglas de l&iacute;mites.
     * @return List.
     */
    public abstract List getLimites();
    
    /**
     * Guarda la lista de reglas de l&iacute;mites.
     * @param lista que se desea guardar.
     */
    public abstract void setLimites(List lista);
    
    /**
     * Modelo para la lista de Tipos de Divisas.
     *
     * @return RecordSelectionModel El modelo de las divisas en el combo.
     */
    public IPropertySelectionModel getDivisasModel() {
        return new RecordSelectionModel(getListaDivisas(), VALUE, LABEL);
    }
    
    /**
     * Nivel de los mensajes al usuario: -1 Error, 0 Warning, 1 Exito.
     *
     * @param level El valor a asignar.
     */
    public abstract void setLevel(int level);
    
    /**
     * Constante  value.
     */
    private static final String VALUE = "value";

    /**
     * Constante label.
     */
    private static final String LABEL = "label";
    
    /**
     * Constante de no aplica.
     */
    private static final String NO_APLICA = "Cualquiera";

    /**
     * @author lvillegas
     */
    private class MapaPantalla {
        /**
         * Constructor por default.
         */
        public MapaPantalla() {
            textoPantalla = NO_APLICA;
            claveBD = null;
        }
        
        /**
         * Constructor con par&aacute;metros.
         * @param texto a desplegar en pantalla.
         * @param clave de la base de datos.
         */
        public MapaPantalla(String texto, Object clave) {
            textoPantalla = texto;
            claveBD = clave;
        }
        
        /**
         * Texto que se utiliza en pantalla.
         */
        private String textoPantalla;
        /**
         * Clave que se utiliza en base de datos.
         */
        private Object claveBD;

        /**
         * @return the claveBD
         */
        public final Object getClaveBD() {
            return claveBD;
        }

        /**
         * @param claveBD the claveBD to set
         */
        public final void setClaveBD(Object claveBD) {
            this.claveBD = claveBD;
        }

        /**
         * @return the textoPantalla
         */
        public final String getTextoPantalla() {
            return textoPantalla;
        }

        /**
         * @param textoPantalla the textoPantalla to set
         */
        public final void setTextoPantalla(String textoPantalla) {
            this.textoPantalla = textoPantalla;
        }
    }
}

