package com.ixe.ods.sica.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.ixe.ods.sica.util.XSSRequestWrapper;

/**
 * @author HCIUJ950
 * Servlet Filter implementation class CrossSiteFilter
 * F-51213- Task Force, Remediación de seguridad detectado por pruebas de caja gris
 * Filtro encargado de implementar RequestWrapper para el reemplazo de cadenas que contengan scripts
 */
public class CrossSiteFilter implements Filter {
	
	private ServletContext context;
	
    /**
     * Constructor simple. 
     */
    public CrossSiteFilter() {
    }
    
    /**
	 * @see Filter#destroy()
	 */
	public void destroy() {
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 * doFilter que implementa el Wrapper para el reemplazo de scripting 
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		chain.doFilter(new XSSRequestWrapper((HttpServletRequest) request), response);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 * Asignación del ServletContext
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		this.context = fConfig.getServletContext();
	}

}
