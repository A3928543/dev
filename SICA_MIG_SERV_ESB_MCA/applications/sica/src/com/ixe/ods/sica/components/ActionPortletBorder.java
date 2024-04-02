/*
* $Id: ActionPortletBorder.java,v 1.10 2008/02/22 18:25:35 ccovian Exp $
* Ixe, Sep 13, 2003
* Copyright (C) 2001-2003 Grupo Financiero Ixe
*/

package com.ixe.ods.sica.components;

import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.IRequestCycle;

/**
 * Variante de PortletBorder que permite al usuario minimizar o restaurar el portlet.
 *
 * @author  Jean C. Favila
 * @version $Revision: 1.10 $ $Date: 2008/02/22 18:25:35 $
 */
public abstract class ActionPortletBorder extends BaseComponent {

    /**
     * Inverts the minimized value.
     * @param cycle The IRequestCycle.
     */
    public void toggle(IRequestCycle cycle) {
        setMinimized(!isMinimized());
    }

    /**
     * Returns the minimized flag.
     * @return boolean.
     */
    public abstract boolean isMinimized();

    /**
     * Sets th minimized flag.
     * @param minimized The minimized flag to set.
     */
    public abstract void setMinimized(boolean minimized);
}
