/*
 * $Id: SwapsServiceData.java,v 1.15.14.1 2010/09/09 00:36:14 galiciad Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2006 - 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica.sdo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.ixe.ods.sica.model.BitacoraEnviadas;
import com.ixe.ods.sica.model.Broker;
import com.ixe.ods.sica.model.ContraparteBanxico;
import com.ixe.ods.sica.model.RespAltaKondor;
import com.ixe.ods.sica.model.Swap;
import com.ixe.ods.sica.vo.BrokerVO;
import com.ixe.ods.sica.vo.SwapVO;

/**
 * Service Data Object para las operaciones a la base de datos que requieren las operaciones para
 * Swaps.
 * 
 * @author Jean C. Favila
 * @version $Revision: 1.15.14.1 $ $Date: 2010/09/09 00:36:14 $
 */
public interface SwapsServiceData {

	/**
	 * Crea un nuevo broker con los par&aacute;metros especificados.
	 * 
	 * @param broker El nuevo broker.
	 * @param noCuenta El n&uacute;mero de cuenta del broker.
	 * @param idPromotor El id del promotor.
	 * @return BrokerVO
	 */
    BrokerVO crearBrokerVO(Broker broker, String noCuenta, int idPromotor);
    
    /**
     * Encuentra la descripci&oacute;n de la contraparte de Banxico
     * con la base en la clave de la contraparte.
     * 
     * @deprecated Se migro el catalogo de contrapartes a la tabla SC_BROKER,
     * usar findContraparteBanxico que retorna un objeto Broker.
     * @param claveContraparte La clave contraparte.
     * @return ContraparteBanxico.
     */
    //ContraparteBanxico findContraparteBanxico(String claveContraparte);
    
    /**
     * Encuentra la descripci&oacute;n de la contraparte de Banxico
     * con la base en la clave de la contraparte.
     * 
     * @param claveContraparte La clave contraparte.
     * @return ContraparteBanxico.
     */
    Broker findContraparteBanxico(String claveContraparte);
    
    /**
     * Obtiene el Value Object de BitacorasEnviadas con base en su folio en el tas,
     * el Broker y su correspondiente objeto de BitacoraEnviadas.
     * 
     * @param folioTas El folio de la bitacora en el Tas.
     * @param idBroker El broker correspondiente.
     * @param be El objecto java de BitacoraEnviadas. 
     * @param noContratoSica El n&uacute;mero de contrato sica de la contraparte.
     */
    void cargarBitacorasEnvadas(int folioTas, Integer idBroker, BitacoraEnviadas be,
                                               String noContratoSica);

    /**
     * Utilizado en la captura de contrapartes de un swap para encontrar el swap que tiene el folio
     * correspondiente.
     * Regresa null si este no existe.
     *
     * @param idFolioSwap El n&uacute;mero de swap.
     * @return Swap.
     */
    Swap findSwap(int idFolioSwap);

    /**
     * Regresa los swaps que cumplan con estos criterios. Si el idFolioSwap es mayor a cero se
     * utiliza este criterio. Si idDeal es mayor a cero se utiliza este otro, de lo contrario, se
     * busca por el status proporcionado.
     *
     * @param idFolioSwap El n&uacute;mero de swap.
     * @param idDeal El n&uacute;mero de deal que est&aacute; incluido en el swap.
     * @param fechaOperacion La fecha en la que se registr&oacute; el swap.
     * @param status El status del swap a buscar.
     * @return List.
     */
    List findSwaps(int idFolioSwap, int idDeal, Date fechaOperacion, String status);    

    /**
     * Para un swap de derivados, crea un swap con el n&uacute;mero igual al <i>folioTas</i>. La
     * informaci&oacute;n es obtenida a partir de un registro de SC_BITACORA_ENVIADAS que
     * corresponda al folio especificado. Este &uacute;ltimo debe tener statusEstrategia 'PE' para
     * poder continuar y es actualizado a 'IN'. El nuevo swap es insertado en la base de datos.
     *
     * @param swapVO La instancia con los valores para el swap.
     * @return Map.
     */
    Map insertarSwap(SwapVO swapVO);

    /**
     * Genera un deal interbancario para cada elemento de la lista de Mapas de BitacoraEnviadas.
     *
     * @param ticket El ticket de la sesi&oacute;n del usuario.
     * @param bes El arreglo de objetos BitacoraEnviadas que componen el Swap.
     * @return List de HashMap.
     * @ejbgen:remote-method transaction-attribute = Required
     * @ejbgen:local-method transaction-attribute = Required
     */
    public RespAltaKondor[] crearSwapKondor(String ticket, ArrayList bes);    
}
