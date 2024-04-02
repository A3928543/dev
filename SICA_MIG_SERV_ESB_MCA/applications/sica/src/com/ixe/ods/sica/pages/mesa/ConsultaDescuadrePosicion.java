/*
 * $Id: ConsultaDescuadrePosicion.java,v 1.2 2008/02/22 18:25:32 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2008 LegoSoft S.C.
 */
package com.ixe.ods.sica.pages.mesa;

import com.ixe.ods.sica.dao.DescuadreDao;
import com.ixe.ods.sica.pages.SicaPage;
import java.util.List;
import org.apache.tapestry.IRequestCycle;

/**
 * P&aacute;gina que permite detectar descuadres en la Posici&oacute;n, por medio del Dao
 * que encuentra los deals cancelados y no cancelados inconsistentes con SC_POSICION_LOG.
 *
 * @author Gustavo Gonzalez
 * @version $Revision: 1.2 $ $Date: 2008/02/22 18:25:32 $
 */
public abstract class ConsultaDescuadrePosicion extends SicaPage {

	/**
	 * Asigna los parametros necesarios para la p&aacute;gina.
	 *
	 * @param cycle El ciclo de la p&aacute;gina.
	 */
    public void activate(IRequestCycle cycle) {
        super.activate(cycle);
        consultarDeals(cycle);
    }

    /**
     * Realiza la b&uacute;squeda de deals que esten descuadrando la posici&oacute;n
     * por medio de DescuadreDao.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     * @see com.ixe.ods.sica.dao.JdbcDescuadreDaoImpl#findDescuadreDetallesDealsCancelados().
     * @see com.ixe.ods.sica.dao.JdbcDescuadreDaoImpl#findDescuadreDetallesDealsNoCancelados().
     */
    public void consultarDeals(IRequestCycle cycle) {
        DescuadreDao descDao = (DescuadreDao) getApplicationContext().getBean("descuadreDao");
        setDealsNoCancelados(descDao.findDescuadreDetallesDealsNoCancelados());
        setDealsCancelados(descDao.findDescuadreDetallesDealsCancelados());
    }

    /**
     * Obtiene la lista de deals no Cancelados que descuadran la Posici&opacute;n.
     *
     * @return List
     */
    public abstract List getDealsNoCancelados();

    /**
     * Asigna el valor para la lista de deals no Cancelados que descuadran la posici&oacute;n.
     *
     * @param dealsNoCancelados El valor para la lista.
     */
    public abstract void setDealsNoCancelados(List dealsNoCancelados);

    /**
     * Asigna el valor para la lista de deals Cancelados que descuadran la posici&oacute;n.
     *
     * @return List.
     */
    public abstract List getDealsCancelados();

    /**
     * Obtiene la lista de deals Cancelados que descuadran la Posici&opacute;n.
     *
     * @param dealsCancelados El valor para la lista.
     */
    public abstract void setDealsCancelados(List dealsCancelados);
}
