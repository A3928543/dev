/*
 * $Id: MontoTextField.java,v 1.10.46.2 2012/09/15 14:43:00 mejiar Exp $
 * Ixe Grupo Financiero
 * Copyright © 2005 LegoSoft S.C.
 */

package com.ixe.ods.sica.components;

import com.ixe.ods.sica.Constantes;
import org.apache.tapestry.*;
import org.apache.tapestry.engine.IScriptSource;
import org.apache.tapestry.form.AbstractFormComponent;
import org.apache.tapestry.html.Body;
import org.apache.tapestry.request.RequestContext;
import org.apache.tapestry.valid.IValidationDelegate;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>Variaci&oacute;n de MoneyTextField que formatea el monto con separador de miles conforme el usuario va tecleando
 * el monto. Utiliza el script MontoTextField.script para agregar los eventos necesarios en JavaScript.</p>
 *
 * <p>Si el monto tecleado no es v&aacute;lido, el valor asignado al par&aacute;metro value es cero, por lo que es
 * necesario que el programador valide esta condici&oacute;n.</p>
 *
 * @author Jean C. Favila, Carlos Covi&aacute;n
 * @version $Revision: 1.10.46.2 $ $Date: 2012/09/15 14:43:00 $
 */
public abstract class MontoTextField extends AbstractFormComponent {

    /**
     * Hace el 'rendering' del campo de texto, ejecuta el script MontoTextField.script para agregar los eventos
     * JavaScript.
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
        if (cycle.isRewinding())
            return;
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
                        specLocation.getRelativeLocation("MontoTextField.script");
                _script = source.getScript(scriptLocation);
            }
        }
        Body body = Body.get(cycle);
        Map symbols = new HashMap();
        symbols.put("montoTextField", this);
        _script.execute(cycle, body, symbols);
    }

    /**
     * Asigna el valor al double parseando el par&aacute;metro del request con el formato predeterminado.
     *
     * @param name El nombre del componente.
     * @param form La forma.
     * @param delegate El delegate de la forma.
     * @param cycle El IRequestCycle.
     */
    private void updateValueFromRequest(String name, IForm form, IValidationDelegate delegate, IRequestCycle cycle) {
        RequestContext context = cycle.getRequestContext();
        String value = context.getParameter(name);
        delegate.recordFieldInputValue(value);
        try {
            Number num = Constantes.MONEY_FORMAT.parse(value);
            setValue(num.doubleValue());
        }
        catch(Exception e) {
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
        if (value == null)
            value = Constantes.MONEY_FORMAT.format(getValue());
        if (value == null)
            return "0.00";
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
     * La instancia de MontoTextField.script.
     */
    private IScript _script;

    /**
     * Si el campo est&aacute; o no deshabilitado.
     */
    private boolean _disabled;
}