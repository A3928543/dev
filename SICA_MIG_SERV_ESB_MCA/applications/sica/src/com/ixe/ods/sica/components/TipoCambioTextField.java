/*
 * $Id: TipoCambioTextField.java,v 1.2 2010/04/30 15:51:23 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica.components;

import java.util.HashMap;
import java.util.Map;

import org.apache.tapestry.IForm;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.IResourceLocation;
import org.apache.tapestry.IScript;
import org.apache.tapestry.engine.IScriptSource;
import org.apache.tapestry.form.AbstractFormComponent;
import org.apache.tapestry.html.Body;
import org.apache.tapestry.request.RequestContext;
import org.apache.tapestry.valid.IValidationDelegate;

import com.ixe.ods.sica.Constantes;

public abstract class TipoCambioTextField extends AbstractFormComponent {
	
	/**
     * Hace el 'rendering' del campo de texto, ejecuta el script MontoTextField.script para agregar
     * los eventos JavaScript.
     *
     * @param writer El IMarkupWriter.
     * @param cycle El IRequestCycle.
     */
    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle) {
        IForm form = getForm(cycle);
        String name = form.getElementId(this);
        boolean disabled = isDisabled();
        IValidationDelegate delegate = form.getDelegate();
        if (form.isRewinding()) {
            if (!disabled) {
                updateValueFromRequest(name, form, delegate, cycle);
            }
            return;
        }
        if (cycle.isRewinding()) {
            return;
        }
        String value = extractValue(delegate);
        delegate.writePrefix(writer, cycle, this, null);
        writer.beginEmpty("input");
        writer.attribute("type", "text");
        writer.attribute("name", name);
        writer.attribute("value", value);
        writer.attribute("class", "textoderecha");
        if (disabled) {
            writer.attribute("disabled", "disabled");
        }
        delegate.writeAttributes(writer, cycle, this, null);
        delegate.writeSuffix(writer, cycle, this, null);
        if (!disabled) {
            if (_script == null) {
                IScriptSource source = cycle.getEngine().getScriptSource();
                IResourceLocation specLocation =
                        getSpecification().getLocation().getResourceLocation();
                IResourceLocation scriptLocation =
                        specLocation.getRelativeLocation("TipoCambioTextField.script");
                _script = source.getScript(scriptLocation);
            }
        }
        Body body = Body.get(cycle);
        Map symbols = new HashMap();
        int numDec = new Integer(getBinding("numDecimals").getString()).intValue();
        symbols.put("numDecimals", "" + (numDec + 1));
        symbols.put("tipoCambioTextField", this);
        _script.execute(cycle, body, symbols);
	}

	/**
     * Asigna el valor al double parseando el par&aacute;metro del request con el formato
     * predeterminado.
     *
     * @param name     El nombre del componente.
     * @param form     La forma.
     * @param delegate El delegate de la forma.
     * @param cycle    El IRequestCycle.
     */
    private void updateValueFromRequest(String name, IForm form, IValidationDelegate delegate,
                                        IRequestCycle cycle) {
        RequestContext context = cycle.getRequestContext();
        String value = context.getParameter(name);
        delegate.recordFieldInputValue(value);
        try {
            Number num = Constantes.TCC_FORMAT.parse(value);
            setValue(num.doubleValue());
        }
        catch (Exception e) {
            setValue(0.0);
        }
    }

    /**
     * Regresa el valor del double formateado.
     *
     * @param delegate El validation delegate de la forma.
     * @return String.
     */
    private String extractValue(IValidationDelegate delegate) {
        String value = delegate.getFieldInputValue();
        if (value == null) {
            value = Constantes.TCC_FORMAT.format(getValue());
        }
        if (value == null) {
            return "0.00";
        }
        return value;
    }

    /**
     * Regresa el valor de disabled.
     *
     * @return boolean.
     */
    public boolean isDisabled() {
        return _disabled;
    }

    /**
     * Establece el valor de disabled.
     *
     * @param disabled El valor a asignar.
     */
    public void setDisabled(boolean disabled) {
        _disabled = disabled;
    }

    /**
     * Regresa el valor de value.
     *
     * @return double
     */
    public abstract double getValue();

    /**
     * Establece el valor de value.
     *
     * @param value El valor a asignar.
     */
    public abstract void setValue(double value);
    
    /**
     * Regresa el valor de numDecimals.
     *
     * @return int
     */
    public abstract int getNumDecimals();

    /**
     * Establece el valor de numDecimals.
     *
     * @param decimals El valor a asignar.
     */
    public abstract void setNumDecimals(int numDecimals);

    /**
     * La instancia de MontoTextField.script.
     */
    private IScript _script;

    /**
     * Si el campo est&aacute; o no deshabilitado.
     */
    private boolean _disabled;

}
