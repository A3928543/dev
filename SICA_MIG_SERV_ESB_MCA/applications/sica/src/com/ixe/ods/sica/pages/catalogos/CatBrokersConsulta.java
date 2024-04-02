/*
 * $Id: CatBrokersConsulta.java,v 1.10 2008/02/22 18:25:39 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright © 2005 LegoSoft S.C.
 */

package com.ixe.ods.sica.pages.catalogos;

import com.ixe.ods.bup.model.ContratoSica;
import com.ixe.ods.bup.model.PersonaMoral;
import com.ixe.ods.sica.pages.SicaPage;
import org.apache.commons.lang.StringUtils;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.valid.IValidationDelegate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Clase que permite buscar y agregar Brokers.
 *
 * @author Gerardo Corzo, Javier Cordova
 * @version $Revision: 1.10 $ $Date: 2008/02/22 18:25:39 $
 */
public abstract class CatBrokersConsulta extends SicaPage {

    /**
     * M&eacut;etodo que busca un broker
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void buscarBroker(IRequestCycle cycle) {
        IValidationDelegate delegate = (IValidationDelegate) getBeans().getBean("delegate");
    	List list = new ArrayList();
    	convertirAUpperCase();
    	//Validaciones de Campos:
        if (getRazonSocial().indexOf("%") >= 0) {
            setBrokers(list);
            delegate.record("No se permite utilizar el caracter '%' en los criterios de consulta.", null);
            return;
        }
        else if (StringUtils.isEmpty(getRazonSocial().trim())) {
            setBrokers(list);
    		delegate.record("Debe definir el criterio de b\u00fasqueda.", null);
    		return;
		}
        else if (StringUtils.isNotEmpty(getRazonSocial()) && (getRazonSocial().length() < 3)) {
            setBrokers(list);
			delegate.record("Los criterios de b\u00fasqueda deben tener m\u00e1s de 2 caracteres.", null);
			return;
		}
    	List l = getSicaServiceData().findPersonaMoralNotBrokerByRazonSocial(getRazonSocial());
        for (Iterator iter = l.iterator(); iter.hasNext();) {
			PersonaMoral element = (PersonaMoral) iter.next();
			if (getNoCuenta(element.getIdPersona()) != null){
				list.add(element);
			}
		}
        setBrokers(list);
        if (getBrokers().isEmpty()) {
            delegate.record("Los criterios de b\u00fasqueda especificados no arrojaron resultados.", null);
        }
    }

    /**
     * Redirige a la p&aacute;gina <code>CatBroker</code> para permitir al usuario agregar el broker.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void agregar(IRequestCycle cycle) {
    	IValidationDelegate delegate = (IValidationDelegate) getBeans().getBean("delegate");
        PersonaMoral pm = getSicaServiceData().findPersonaMoralByIdPersona((Integer) cycle.getServiceParameters()[0]);
        if (pm != null) {
	        CatBroker nextPage = (CatBroker) cycle.getPage("CatBroker");
	        nextPage.setPersonaMoral(pm);
	        nextPage.setClaveReuters("");
	        nextPage.setPaginaAnterior(getPaginaAnterior());
	        nextPage.setTipoBrokerSeleccionado("Inst. No Financiera");
	        nextPage.setPagoAnticipadoSeleccionado("N");
	        nextPage.setTipoOperacion(0);
	        nextPage.activate(cycle);
        }
        else {
        	delegate.record("No se encontr\u00f3 la Persona Moral seleccionada u ocurri\u00f3 un error al buscarla.", null);
        }
    }
    
    /**
	 * Convierte lo escrito en los Criterios de Busqueda a Uppercase para evitar fallas en las busquedas 
	 * de la Base de Datos.
	 */
	public void convertirAUpperCase() {
        if (StringUtils.isNotEmpty(getRazonSocial())) {
            setRazonSocial(getRazonSocial().toUpperCase());
        }
    }
	
	/**
     * Regresa el n&uacute;mero de contrato sica para la persona especificada, o null si &eacute;ste no existe.
     *
     * @param idPersona El n&uacute;mero de la persona en la bup.
     * @return String El Contrato Sica, si existe.
     */
    public String getNoCuenta(Integer idPersona) {
    	ContratoSica cs = getSicaServiceData().findContratoSicaByIdPersona(idPersona);
        return cs != null ? cs.getNoCuenta() : null;
    }

    /**
     * Regresa el valor de brokers.
     *
     * @return List.
     */
	public abstract List getBrokers();

    /**
     * Fija el valor de brokers.
     *
     * @param brokers El valor a asignar.
     */
	public abstract void setBrokers(List brokers);
	
    /**
     * Regresa el valor de personaMoral.
     *
     * @return PersonaMoral.
     */
	public abstract PersonaMoral getPersonaMoral();

    /**
     * Fija el valor de personaMoral.
     *
     * @param personaMoral El valor a asignar.
     */
	public abstract void setPersonaMoral(PersonaMoral personaMoral);
	
    /**
     * Regresa el valor de razonSocial.
     *
     * @return String.
     */
	public abstract String getRazonSocial();

    /**
     * Fija el valor de razonSocial.
     *
     * @param razonSocial El valor a asignar.
     */
	public abstract void setRazonSocial(String razonSocial);

    /**
     * Fija el valor de paginaAnterior.
     *
     * @param paginaAnterior El valor a asignar.
     */
	public abstract void setPaginaAnterior(String paginaAnterior);
	
    /**
     * Regresa el valor de paginaAnterior.
     *
     * @return String.
     */
	public abstract String getPaginaAnterior();
}
