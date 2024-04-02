/*
 * $Id: AjaxTextField.java,v 1.10 2008/02/22 18:25:35 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright © 2005 LegoSoft S.C.
 */

package com.ixe.ods.sica.components;

import org.apache.tapestry.*;
import org.apache.tapestry.request.RequestContext;
import org.apache.tapestry.html.Body;
import org.apache.tapestry.engine.IScriptSource;
import org.apache.tapestry.valid.IValidationDelegate;
import org.apache.tapestry.valid.ValidatorException;
import org.apache.tapestry.form.AbstractFormComponent;

import java.util.Map;
import java.util.HashMap;

/**
 * @author Jean C. Favila
 * @version $Revision: 1.10 $ $Date: 2008/02/22 18:25:35 $
 */
public abstract class AjaxTextField extends AbstractFormComponent {

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
        String fieldName = name + "$g" + 0;
        writer.beginEmpty("input");
        writer.attribute("type", "text");
        writer.attribute("name", fieldName);
        writer.attribute("value", value);
        if (disabled) {
            writer.attribute("disabled", "disabled");
        }
        delegate.writeAttributes(writer, cycle, this, null);
        delegate.writeSuffix(writer, cycle, this, null);

        writer.beginEmpty("br");

        writer.begin("div");
        writer.attribute("id", "salida_" + name);
        writer.attribute("style", "OVERFLOW: auto; WIDTH: 280px; HEIGHT: 100px; background: #ffffcc; " +
                "position:absolute; z-index:1; visibility:hidden;");
        writer.end("div");

        writer.beginEmpty("br");
        if (!disabled) {
            if (_script == null) {
                IScriptSource source = cycle.getEngine().getScriptSource();
                IResourceLocation specLocation =
                        getSpecification().getLocation().getResourceLocation();
                IResourceLocation scriptLocation =
                        specLocation.getRelativeLocation("AjaxTextField.script");
                _script = source.getScript(scriptLocation);
            }
        }
        Body body = Body.get(cycle);
        Map symbols = new HashMap();
        symbols.put("baseUrl", getBinding("baseUrl").getString());
        symbols.put("ajaxTextField", this);
        _script.execute(cycle, body, symbols);
    }

    private void updateValueFromRequest(String name, IForm form, IValidationDelegate delegate, IRequestCycle cycle) {
        RequestContext context = cycle.getRequestContext();
        String value = context.getParameter(name + "$g0");
        delegate.recordFieldInputValue(value);
        //try {
            //validate(cardNumber);
            setValue(value);
        /*}
        catch (ValidatorException ex) {
            delegate.record(ex);
        } */
    }

    private String extractValue(IValidationDelegate delegate) {
        String value = delegate.getFieldInputValue();
        if (value == null)
            value = getValue();
        if (value == null)
            return "";
        return value;
    }


    public boolean isDisabled() {
        return _disabled;
    }

    public void setDisabled(boolean disabled) {
        _disabled = disabled;
    }

    public abstract String getValue();

    public abstract void setValue(String value);

    private IScript _script;
    private boolean _disabled;
}
