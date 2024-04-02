/*
* $Id: SoThinkMenuFactory.java,v 1.10 2008/02/22 18:25:50 ccovian Exp $
* Ixe, Sep 13, 2003
* Copyright (C) 2001-2004 Grupo Financiero Ixe
*/

package com.ixe.ods.tapestry.components.menu;

import com.thoughtworks.xstream.XStream;

/**
 *
 * @author  Jean C. Favila
 * @version $Revision: 1.10 $ $Date: 2008/02/22 18:25:50 $
 */
public class SoThinkMenuFactory extends AbstractMenuFactory {

    public SoThinkMenuFactory() {

    }

    /**
     *
     * @return
     */
    protected XStream buildXStream() {
        XStream stream = new XStream();
        stream.alias("menu", Menu.class);
        stream.alias("menuitem", MenuItem.class);
        return stream;
    }
}
