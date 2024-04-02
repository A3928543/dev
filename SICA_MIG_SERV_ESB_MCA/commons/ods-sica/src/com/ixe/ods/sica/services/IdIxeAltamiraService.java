package com.ixe.ods.sica.services;

import com.banorte.www.ws.exception.SicaAltamiraException;


/**
 * Interfaz de Servicio
 * 	para consulta de numero de id_persona
 *  a partir de cliente Altamira.
 *
 * @author Diego Pazaran
 * 		Banorte.
 * @version $Revision: 1.1.2.1 $
 */
public interface IdIxeAltamiraService {
    /**
     * Metodo que obtiene el Id_cliente
     * 	de la bup a partir de un número
     *  de cliente de Altamira.
     *
     * @param noClienteBanorte el identificador
     * 	del cliente en Altamira.
     *
     * @return String con el id_persona bup
     *
     * @throws SicaAltamiraException DOCUMENT ME!
     */
    public String getIdClienteIxe(String noClienteBanorte)
        throws SicaAltamiraException;
}
