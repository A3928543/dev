/*
 * $Id: SicaPizarronSessionBean.java,v 1.15 2008/12/26 23:17:34 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 - 2007 LegoSoft S.C.
 */
package com.ixe.ods.sica;

import com.ixe.ods.sica.model.Divisa;
import com.ixe.ods.sica.sdo.PizarronServiceData;
import com.ixe.ods.sica.sdo.SicaServiceData;
import com.ixe.treasury.dom.common.FormaPagoLiq;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.ejb.CreateException;
import javax.ejb.SessionContext;
import org.springframework.context.access.ContextSingletonBeanFactoryLocator;
import org.springframework.ejb.support.AbstractStatelessSessionBean;

/**
 * Bean de Servicios del SICA para operaciones del Pizarr&oacute;n.
 * 
 * @author Jean C. Favila
 * @version $Revision: 1.15 $ $Date: 2008/12/26 23:17:34 $
 * @ejbgen:session ejb-name = SicaPizarronSessionEJB
 * type = Stateless
 * initial-beans-in-free-pool = 5
 * max-beans-in-free-pool = 50
 * default-transaction = Required
 * trans-timeout-seconds = 30
 * enable-call-by-reference = true
 * @ejbgen:jndi-name remote = ejb/sica/SicaPizarronHome
 * local = ejb/sica/SicaPizarronLocalHome
 */
public class SicaPizarronSessionBean extends AbstractStatelessSessionBean {

    /**
     * Constructor por default.
     */
    public SicaPizarronSessionBean() {
        super();
    }

    /**
     * Realiza el arranque necesario para proporcionar los servicios.
     *
     * @throws CreateException Si no se puede crear el ejb.
     */
    protected void onEjbCreate() throws CreateException {
    }

    /**
     * Cambia los valores por omisi&oacute;n de que archivo tomar como configuraci&oacute;n para
     * Spring. Tambien indica que el BeanFactory ser&aacute; compartido (singleton).
     *
     * @param sessionContext El sessionContext del EJB.
     */
    public void setSessionContext(SessionContext sessionContext) {
        super.setSessionContext(sessionContext);
        setBeanFactoryLocator(ContextSingletonBeanFactoryLocator.getInstance());
        setBeanFactoryLocatorKey("sicaApplicationContext");
    }

    /**
     * Crea el Pizarr&oacute;n de precios para el canal definido.
     * 
     * @ejbgen:remote-method transaction-attribute = NotSupported
     * @ejbgen:local-method transaction-attribute = NotSupported
     * @see com.ixe.ods.sica.sdo.PizarronServiceData#crearPizarron(String, String).
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param idCanal La clave de canal.
     * @return Map.
     * @throws SicaException Si algo sale mal.
     */
    public Map crearPizarron(String ticket, String idCanal) throws SicaException {
        return new HashMap();
    }

    /**
     * Obtiene las formas de liquidaci&oacute;n para el Pizarr&oacute;n.
     * 
     * @ejbgen:remote-method transaction-attribute = NotSupported
     * @ejbgen:local-method transaction-attribute = NotSupported
     * @param ticket El ticket de la sesi&oacute;n.
     * @return List.
     */
    public List getFormasPagoLiq(String ticket) {
        List fpls = ((SicaSiteCache) getBeanFactory().getBean("sicaSiteCache")).
                obtenerFormasPagoLiq(ticket);
        List resultados = new ArrayList();
        for (Iterator it = fpls.iterator(); it.hasNext();) {
            FormaPagoLiq fpl = (FormaPagoLiq) it.next();
            Map map = new HashMap();
            map.put("claveFormaLiquidacion", fpl.getClaveFormaLiquidacion());
            map.put("mnemonico", fpl.getMnemonico());
            map.put("idDivisa", fpl.getIdDivisa());
            map.put("minimo", fpl.getMontoMinimo() != null
                    ? fpl.getMontoMinimo() : new BigDecimal(1.0));
            map.put("multiplo", fpl.getMultiplo() != null ? fpl.getMultiplo() : new Double(1.0));
            map.put("desc", fpl.getNombreFormaLiquidacion());
            resultados.add(map);
        }
        return resultados;
    }

    /**
     * Obtiene la lista de de divisas frecuentes para el Pizarr&oacute;n.
     * 
     * @ejbgen:remote-method transaction-attribute = NotSupported
     * @ejbgen:local-method transaction-attribute = NotSupported
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @return List.
     */
    public List getDivisasFrecuentes(String ticket) {
        List divs = ((SicaServiceData) getBeanFactory().getBean("sicaServiceData")).
                findDivisasFrecuentes();
        List resultados = new ArrayList();
        for (Iterator it = divs.iterator(); it.hasNext();) {
            Divisa div = (Divisa) it.next();
            if (!Divisa.PESO.equals(div.getIdDivisa())) {
                resultados.add(div.getIdDivisa());
            }
        }
        return resultados;
    }

    /**
     * Obtiene la lista de divisas no frecuentes para el Pizarr&oacute;n.
     * 
     * @ejbgen:remote-method transaction-attribute = NotSupported
     * @ejbgen:local-method transaction-attribute = NotSupported
     * @param metales true para metales amonedados, false para divisas noFrecuentes.
     * @return List.
     */
    public List obtenerPizarronOtrasDivisas(boolean metales) {
        return getPizarronServiceData().obtenerPizarronOtrasDivisas(metales);
    }

    /**
     * Regresa el bean pizarronServiceData.
     *
     * @return PizarronServiceData.
     */
    private PizarronServiceData getPizarronServiceData() {
        return (PizarronServiceData) getBeanFactory().getBean("pizarronServiceData");
    }
}
