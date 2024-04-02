/*
 * $Id: ServletContextHolder.java,v 1.10 2008/02/22 18:25:51 ccovian Exp $
 * Ixe, Mar 9, 2005
 * Copyright (C) 2001-2004 Grupo Financiero Ixe, S.A.
 */
package com.ixe.ods.springframework.web.context;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;

/**
 * @author David Solis (dsolis)
 * @version $Revision: 1.10 $ $Date: 2008/02/22 18:25:51 $
 */
public class ServletContextHolder implements ServletContextAware {
    private static ServletContext servletContext;

    public void setServletContext(ServletContext context) {
        servletContext = context;
    }

    public static ApplicationContext getApplicationContext() {
        if (servletContext == null) {
            throw new IllegalStateException("Application context not initialized");
        }
        return WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
    }
}
