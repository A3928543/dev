package com.ixe.ods.sica.services;

import java.util.Map;

import com.banorte.www.ws.exception.SicaAltamiraException;

import com.ixe.ods.sica.dto.DatosBasicosClienteAltamiraDto;


/**
 * Interface del servicio que consulta datos
 * 	b�sicos de cliente en Altamira. [TX PE80]
 *
 * @author $author$
 * @version $Revision: 1.1.2.1 $
 */
public interface ConsultaDatosBasicosClienteService {
    /**
     * Obtiene datos basicos de un cliente en Altamira.
     *
     * @param noCliente el n�mero de cliente (sic)
     * 		  a consultar
     * @param param <code>Map</code> con los parametros
     * 		  de ejecuci�n del servicio.
     *
     * @return <code>DatosBasicosClienteAltamiraDto</code>
     * 		con la informacion del cliente Altamira. 
     *
     * @throws SicaAltamiraException  
     * 			en caso de un error en la ejecuci�n del Servicio.
     */
    public DatosBasicosClienteAltamiraDto getDatosBasicosClienteAltamira(Integer noCliente, Map param)
        throws SicaAltamiraException;
}
