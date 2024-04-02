/*
* $Id: Menu.java,v 1.10 2008/02/22 18:25:50 ccovian Exp $
* Ixe, Sep 13, 2003
* Copyright (C) 2001-2004 Grupo Financiero Ixe
*/

package com.ixe.ods.tapestry.components.menu;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author  Jean C. Favila
 * @version $Revision: 1.10 $ $Date: 2008/02/22 18:25:50 $
 */
public class Menu implements Serializable, IMenu {

    public Menu(String name) {
        this.name = name;
    }

    public String render(String dl, String replacePermission, String replaceData, boolean renderAdditionalInfo) {
        String result = "stm_bm(['menu79af',400,'','blank.gif',1,'1','0',0,0,250,0,1000,1,0,0]);\n" +
                "stm_bp('p0',[0,4,0,0,0,3,0,7,100,'',-2,'',-2,50,2,5,'#777777','#cccccc','',3,1,1,'#7d000d']);\n";
        for (Iterator it = menuitems.iterator(); it.hasNext(); ) {
            MenuItem menuItem = (MenuItem) it.next();
            if(renderItems(dl, replacePermission, replaceData, menuItem)) {
	            result += "  stm_ai('p0i0',[1,'" + menuItem.getCaption() + "','','',-1,-1,0," + menuItem.getRealUrl(dl, replacePermission, replaceData) + ",'_self','','','','',0,0,0,'images/arrow_r.gif','images/arrow_r.gif',7,7,0,0,1,'#cccccc',0,'#cccc99',0,'','',3,3,1,1,'#cccccc','#7d000d','#7d000d','#7d000d','8pt Verdana','8pt Verdana',0,0]);\n" +
	                      "  stm_bpx('p1','p0',[1,4,0,0,0,3,0,7]);\n";
	            if (menuItem.getMenuItems() != null) {
	                for (Iterator it2 = menuItem.getMenuItems().iterator(); it2.hasNext(); ) {
	                    MenuItem submenuItem = (MenuItem) it2.next();
	                    if(renderItems(dl, replacePermission, replaceData, submenuItem)) {
		                    result += "    stm_ai('p1i0',[1,'" + submenuItem.getCaption() + "','','',-1,-1,0," + submenuItem.getRealUrl(dl, replacePermission, replaceData) + ",'_self'," + submenuItem.getRealUrl(dl, replacePermission, replaceData) + ",'','','',0,0,0,'images/arrow_r.gif','images/arrow_r.gif',0,0,0,0,1,'#cccccc',0,'#cccc99',0,'','',3,3,1,1,'#cccccc','#7d000d','#7d000d','#7d000d','8pt Verdana','8pt Verdana',0,0]);\n" +
		                              "    stm_bpx('p2','p1',[1,2,0,0,0,3,0,7]);\n";
		                    if (submenuItem.getMenuItems() != null) {
		                        for (Iterator it3 = submenuItem.getMenuItems().iterator(); it3.hasNext(); ) {
		                            MenuItem subsubmenuItem = (MenuItem) it3.next();
		                            if(renderItems(dl, replacePermission, replaceData, subsubmenuItem)) {
			                            result += "      stm_ai('p2i0',[1,'" + subsubmenuItem.getCaption() + "','','',-1,-1,0," + subsubmenuItem.getRealUrl(dl, replacePermission, replaceData) + ",'_self'," +subsubmenuItem.getRealUrl(dl, replacePermission, replaceData) + ",'','','',0,0,0,'images/arrow_r.gif','images/arrow_r.gif',0,0,0,0,1,'#cccccc',0,'#cccc99',0,'','',3,3,1,1,'#cccccc','#7d000d','#7d000d','#7d000d','8pt Verdana','8pt Verdana',0,0]);\n" +
			                                      "      stm_bpx('p3','p2',[1,2,0,0,0,3,0,7]);\n";
			                            if (subsubmenuItem.getMenuItems() != null) {
			                                for (Iterator it4 = subsubmenuItem.getMenuItems().iterator(); it4.hasNext(); ) {
			                                    MenuItem subsubsubmenuItem = (MenuItem) it4.next();
			                                    if(renderItems(dl, replacePermission, replaceData, subsubsubmenuItem)) {
			                                    	result += "        stm_ai('p3i0',[1,'" + subsubsubmenuItem.getCaption() + "','','',-1,-1,0," + subsubsubmenuItem.getRealUrl(dl, replacePermission, replaceData) + ",'_self'," +subsubsubmenuItem.getRealUrl(dl, replacePermission, replaceData) + ",'','','',0,0,0,'images/arrow_r.gif','images/arrow_r.gif',0,0,0,0,1,'#cccccc',0,'#cccc99',0,'','',3,3,1,1,'#cccccc','#7d000d','#7d000d','#7d000d','8pt Verdana','8pt Verdana',0,0]);\n";
			                                    }
			                                }
			                            }
			                            result += "      stm_ep();\n";
		                            }
		                        }
		                    }
		                    result += "    stm_ep();\n";
	                    }
	                }
	            }
	            result += "  stm_ep();\n";
            }
        }
        result += "stm_ep();\nstm_em();\n";
        return result;
    }

    /**
     * Generates the content for each item.
     *
     * @param dl
     * @param replacePermission
     * @param replaceData
     * @return content of render process
     */
    public boolean renderItems(String dl, String replacePermission, String replaceData, IMenuItem menuItem) {
        return menuItem.hasPermission(delegate);
    }

    /**
     * Add a new element to menu
     *
     * @param item element to add
     */
    public void addMenuItem(IMenuItem item) {
        if (menuitems == null) {
            menuitems = new ArrayList();
        }
        menuitems.add(item);
    }

    public String toString() {
        String str = "<menu name=\"" + name + "\">";
        if (menuitems != null) {
            Iterator it = menuitems.iterator();
            while (it.hasNext()) {
                str += "\n  " + it.next();
            }
        }
        str += "\n</menu>";
        return str;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List getMenuItems() {
        return menuitems;
    }

    public void setMenuItems(List menuItems) {
        this.menuitems = menuItems;
    }

    /**
     * Returns delegate.
     *
     * @return IMenuDelegate
     */
    public IMenuDelegate getDelegate() {
        return delegate;
    }

    /**
     * Sets the delegate.
     *
     * @param delegate The delegate to set
     */
    public void setDelegate(IMenuDelegate delegate) {
        this.delegate = delegate;
    }

    private String name;
    private List menuitems;
    private IMenuDelegate delegate;
}
