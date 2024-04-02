/*
* $Id: MenuItem.java,v 1.10 2008/02/22 18:25:50 ccovian Exp $
* Ixe, Sep 13, 2003
* Copyright (C) 2001-2004 Grupo Financiero Ixe
*/

package com.ixe.ods.tapestry.components.menu;

import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author  Jean C. Favila
 * @version $Revision: 1.10 $ $Date: 2008/02/22 18:25:50 $
 */
public class MenuItem implements Serializable, IMenuItem {

    public MenuItem(String caption, String permission, String data) {
        this.caption = caption;
        this.data = data;
        this.permission = permission;
    }

    public boolean hasPermission(IMenuDelegate delegate) {
        if (delegate == null || permission == null) {
            return true;
        }
        return delegate.hasPermission(permission);
    }

    public String render(String name, String dl, String replacePermission, String replaceData, IMenuDelegate delegate) {
        String result = getInfo(dl, replacePermission, replaceData);
        if (menuitems != null && menuitems.size() > 0) {
            result += ",";
            boolean first = true;
            for (int i = 0; i < menuitems.size(); i++) {
                IMenuItem menuItem = (IMenuItem) menuitems.get(i);
                if (menuItem.hasPermission(delegate)) {
                    if (!first) {
                        result += ",";
                    }
                    else {
                        first = false;
                    }
                    result += menuItem.render(name, dl, replacePermission, replaceData, delegate);
                }
            }
        }
        return result + "]";
    }

    private String getInfo(String dl, String replacePermission, String replaceData) {
        String realUrl = replaceString(dl, replacePermission, permission);
        realUrl = replaceString(realUrl, replaceData, data);
        url = url != null ? StringUtils.replace(url, "${url}", realUrl, 1) : normalizeString(realUrl);
        return "[" + normalizeCaption(caption) + "," + url + "," + (StringUtils.isEmpty(data) ? "null" : data);
    }

    private String normalizeCaption(String caption) {
        return caption == null ? "null" : caption;
    }

    public String getRealUrl(String dl, String replacePermission, String replaceData) {
        String realUrl = replaceString(dl, replacePermission, permission);
        realUrl = replaceString(realUrl, replaceData, data);
        return url != null ? StringUtils.replace(url, "${url}", realUrl, 1) : normalizeString(realUrl);
    }

    private String replaceString(String source, String replace, String with) {
        with = StringUtils.isEmpty(with) ? "" : with;
        return StringUtils.replace(source, replace, with, 1);
    }

    private String normalizeString(String str) {
        if (str == null) {
            return "null";
        }
        else {
            return "'" + str + "'";
        }
    }

    /**
     * Adds a menu item to child list
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
        String str = "<menuitem";
        if (caption != null) {
            str += " caption=\"" + caption + "\"";
        }
        if (data != null) {
            str += " data=\"" + data + "\"";
        }
        str += ">";
        if (menuitems != null) {
            str += "\n<menuitems>";
            Iterator it = menuitems.iterator();
            while (it.hasNext()) {
                str += "\n" + (it.next()).toString();
            }
            str += "\n</menuitems>";
        }
        str += "\n</menuitem>";
        return str;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public List getMenuItems() {
        return menuitems;
    }

    public void setMenuItems(List childs) {
        this.menuitems = childs;
    }

    private String caption;
    private String url;
    private String data;
    private String permission;
    private List menuitems;
}

