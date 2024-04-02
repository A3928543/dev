/*
 * $Id: RiesgosServiceImpl.java,v 1.4.38.1 2011/04/26 02:28:47 galiciad Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2008 LegoSoft S.C.
 */

package com.ixe.ods.sica.services.impl;

import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.Iterator;

import com.ixe.ods.sica.model.ParametroSica;
import com.ixe.ods.sica.model.Posicion;
import com.ixe.ods.sica.model.Divisa;
import com.ixe.ods.sica.model.MesaCambio;
import com.ixe.ods.sica.model.PrecioReferenciaActual;
import com.ixe.ods.sica.sdo.SicaServiceData;
import com.ixe.ods.sica.dao.LimiteDao;
import com.ixe.ods.sica.dto.PosicionMesaDto;
import com.ixe.ods.sica.dto.TotalPosicionDto;
import com.ixe.ods.sica.services.RiesgosService;

/**
 * Implementaci&oacute;n de la interfaz RiesgosService.
 *
 * @author Jean C. Favila
 * @version $Revision: 1.4.38.1 $ $Date: 2011/04/26 02:28:47 $
 */
public class RiesgosServiceImpl implements RiesgosService {

    /**
	 * M&eacut;etodo que regresa la informaci&oacute;n que se despliega en el monitor de riesgos.
     *
     * @return Map.
     * @see com.ixe.ods.sica.services.RiesgosService#obtenerMonitorRiesgos().
	 */
    public Map obtenerMonitorRiesgos() {
        return obtenerMonitorRiesgos(null, null, null);
    }

    /**
	 * M&eacut;etodo que regresa la informaci&oacute;n que se despliega en el monitor de riesgos.
     *
     * @return Map.
     * @see com.ixe.ods.sica.services.RiesgosService#obtenerMonitorRiesgos(Integer, String, Integer).
	 */
	public Map obtenerMonitorRiesgos(Integer idMesaCambio, String idDivisa, Integer horizonTiempo) {
        Map resultados = new HashMap();
        PrecioReferenciaActual pr = getSicaServiceData().findPrecioReferenciaActual();
        resultados.put("midSpot", pr.getPreRef().getMidSpot());
        resultados.put("porcentajeAvisoGlobal", Integer.valueOf((getSicaServiceData().
                findParametro(ParametroSica.PORC_AVISO_LIMITE_REGULATORIO)).getValor()));
        resultados.put("porcentajeAlarmaGlobal", Integer.valueOf((getSicaServiceData().
                findParametro(ParametroSica.PORC_ALARMA_LIMITE_REGULATORIO)).getValor()));
        double capitalDelGrupo = Double.parseDouble((getSicaServiceData().
                findParametro(ParametroSica.CAPITAL_DEL_GRUPO)).getValor());
        resultados.put("capitalDelGrupo", new Double(capitalDelGrupo));
        double porcentajeCapital = Double.parseDouble((getSicaServiceData().
                findParametro("PORCENTAJE_CAPITAL_GRUPO")).getValor());
        resultados.put("porcentajeCapital", new Double(porcentajeCapital));
        resultados.put("limiteRegulatorio", new Double((capitalDelGrupo) *
                (porcentajeCapital / CIEN)));
        List posicionesDivisasFrecuentes = getSicaServiceData().findPosicionDivisasFrecuentes();
        // Filtramos si es necesario:
        if (idMesaCambio != null || idDivisa != null) {
            for (Iterator it = posicionesDivisasFrecuentes.iterator(); it.hasNext();) {
                Posicion pos = (Posicion) it.next();
                if (idMesaCambio != null &&
                        pos.getMesaCambio().getIdMesaCambio() != idMesaCambio.intValue()) {
                    it.remove();
                }
                else if (idDivisa != null && !pos.getDivisa().getIdDivisa().equals(idDivisa)) {
                    it.remove();
                }
            }
        }
        resultados.put("posicionesMesas", clasificarPosicionesPorMesa(posicionesDivisasFrecuentes,
                pr, resultados, horizonTiempo));
        return resultados;
	}

    /**
     * Regresa una lista de objetos PosicionMesaDto.
     *
     * @param posicionesDivisasFrecuentes La lista de todos los registros de la tabla de
     *          posici&oacute;n para las divisas frecuentes.
     * @param pr El precio de referencia vigente.
     * @param resultadosMap El mapa con los resultados.
     * @return List.
     */
    private List clasificarPosicionesPorMesa(List posicionesDivisasFrecuentes, PrecioReferenciaActual pr,
                                             Map resultadosMap, Integer horizonTiempo) {
        List posicionesMesas = new ArrayList();
        List factoresDivisaActuales = getSicaServiceData().findFactoresDivisaActuales();
        for (Iterator it = posicionesDivisasFrecuentes.iterator(); it.hasNext();) {
            Posicion posicion = (Posicion) it.next();
            PosicionMesaDto posicionMesaDto = null;
            for (Iterator it2 = posicionesMesas.iterator(); it2.hasNext();) {
                PosicionMesaDto dto = (PosicionMesaDto) it2.next();
                if (posicion.getMesaCambio().getIdMesaCambio() ==
                        dto.getMesaCambio().getIdMesaCambio()) {
                    posicionMesaDto = dto;
                    break;
                }
            }
            if (posicionMesaDto == null) {
                posicionMesaDto = new PosicionMesaDto();
                posicionMesaDto.setMesaCambio(posicion.getMesaCambio());
                posicionesMesas.add(posicionMesaDto);
            }
            // Esto inicializa las divisas disponibles, favor de no borrar:
            posicionMesaDto.getPosiciones().add(new TotalPosicionDto(posicion,
                    factoresDivisaActuales, pr));
        }
        // Generamos la entrada general para todas las mesas:
        calcularPosicionTodasLasMesas(posicionesMesas);
        // Generamos los registros de totales:
        for (Iterator it = posicionesMesas.iterator(); it.hasNext();) {
            PosicionMesaDto mesasDto = (PosicionMesaDto) it.next();
            TotalPosicionDto totalesDto = new TotalPosicionDto();
            Divisa divTot = new Divisa();
            divTot.setIdDivisa(TOTAL_STR);
            divTot.setDescripcion(TOTAL_STR);
            totalesDto.setDivisa(divTot);
            for (Iterator it2 = mesasDto.getPosiciones().iterator(); it2.hasNext();) {
                totalesDto.sumarDolarizado((TotalPosicionDto) it2.next());
            }
            mesasDto.getPosiciones().add(totalesDto);
        }
        // Sumamos los USD de trader USD a los registros generales:
        integrarUsdDeTraderUsd(posicionesMesas);
        // Se calculan los limites:
        calcularLimites(posicionesMesas, horizonTiempo);
        // Sacamos el nivel, que es la posici&oacute;n final del primer elemento de la lista:
        PosicionMesaDto posMesaDto = (PosicionMesaDto) posicionesMesas.get(0);
        resultadosMap.put("nivel", new Double(((TotalPosicionDto) posMesaDto.getPosiciones().
                get(posMesaDto.getPosiciones().size() - 1)).getPosicionFinal()));
        return posicionesMesas;
    }

    /**
     * Inicializa la lista <code>posicionesMesas</code> con objetos de la clase
     * <code>PosicionMesaDto.</code>
     *
     * @param posicionesMesas La lista de registros de la posici&oacute;n.
     */
    private void calcularPosicionTodasLasMesas(List posicionesMesas) {
        PosicionMesaDto generalDto = new PosicionMesaDto();
        List posGrales = generalDto.getPosiciones();
        MesaCambio mesaGeneral = new MesaCambio();
        mesaGeneral.setNombre("TODAS");
        generalDto.setMesaCambio(mesaGeneral);
        for (Iterator it = posicionesMesas.iterator(); it.hasNext();) {
            PosicionMesaDto posicionMesaDto = (PosicionMesaDto) it.next();
            for (Iterator it2 = posicionMesaDto.getPosiciones().iterator(); it2.hasNext();) {
                TotalPosicionDto currDto = (TotalPosicionDto) it2.next();
                TotalPosicionDto gralDto = null;
                // Encontramos el correspondiente a la divisa:
                for (Iterator it3 = posGrales.iterator(); it3.hasNext();) {
                    TotalPosicionDto currGralDto = (TotalPosicionDto) it3.next();
                    if (currGralDto.getDivisa().getIdDivisa().
                            equals(currDto.getDivisa().getIdDivisa())) {
                        gralDto = currGralDto;
                        break;
                    }
                }
                if (gralDto == null) {
                    gralDto = new TotalPosicionDto();
                    posGrales.add(gralDto);
                }
                gralDto.sumar(currDto);
            }
        }
        posicionesMesas.add(0, generalDto);
    }

    /**
     * Recorre el arreglo de objetos PosicionMesaDto y para cada uno de ellos, asigna los
     * l&iacute;mites de riesgo, utilizando el servicio LimiteDao.getLimitesParaMesaDivisa().
     *
     * @param posicionesMesas La lista de objetos PosicionMesaDto.
     * @see com.ixe.ods.sica.dao.LimiteDao#getLimitesParaMesaDivisa(Integer, String, Integer).
     */
    private void calcularLimites(List posicionesMesas, Integer horizonTiempo) {
        for (Iterator it = posicionesMesas.iterator(); it.hasNext();) {
            PosicionMesaDto pmDto = (PosicionMesaDto) it.next();
            Integer idMesaCambio = pmDto.getMesaCambio().getIdMesaCambio() == 0 ? null :
                    new Integer(pmDto.getMesaCambio().getIdMesaCambio());
            String idDivisa = "*todas*".equals(pmDto.getDivisaSeleccionada().getIdDivisa()) ? null :
                    pmDto.getDivisaSeleccionada().getIdDivisa();
            pmDto.setLimites(getLimiteDao().getLimitesParaMesaDivisa(idMesaCambio, idDivisa,
                    horizonTiempo));
        }
    }
    
    /**
     * Suma a la secci&oacute;n de todas las mesas el dolarizado de la mesa Trader USD.
     *
     * @param posicionesMesas La lista de objetos PosicionMesaDto.
     */
    private void integrarUsdDeTraderUsd(List posicionesMesas) {
        PosicionMesaDto posMesasTdl = null;
        for (Iterator it = posicionesMesas.iterator(); it.hasNext();) {
            PosicionMesaDto posicionMesaDto = (PosicionMesaDto) it.next();
            if (posicionMesaDto.getMesaCambio().getDivisaReferencia() != null &&
                    Divisa.DOLAR.equals(
                            posicionMesaDto.getMesaCambio().getDivisaReferencia().getIdDivisa())) {
                posMesasTdl = posicionMesaDto;
                break;
            }
        }
        if (posMesasTdl != null) {
            List posics = posMesasTdl.getPosiciones();
            TotalPosicionDto totalesTdl = (TotalPosicionDto) posics.get(posics.size() - 1);
            // Buscamos el grupo de Todas las Mesas:
            PosicionMesaDto posicionMesaTodas = (PosicionMesaDto) posicionesMesas.get(0);
            TotalPosicionDto totalMesaTodas = (TotalPosicionDto) posicionMesaTodas.getPosiciones().
                    get(posicionMesaTodas.getPosiciones().size() - 1);
            TotalPosicionDto totalUsdTodas = null;
            // Encontramos el registro de USD:
            for (Iterator it = posicionMesaTodas.getPosiciones().iterator(); it.hasNext();) {
                TotalPosicionDto reg = (TotalPosicionDto) it.next();
                if (Divisa.DOLAR.equals(reg.getDivisa().getIdDivisa())) {
                    totalUsdTodas = reg;
                    break;
                }
            }
            if (totalUsdTodas != null) {
                totalUsdTodas.sumar(totalesTdl);
                totalMesaTodas.sumar(totalesTdl);
            }
        }
    }

    public LimiteDao getLimiteDao() {
        return limiteDao;
    }

    public void setLimiteDao(LimiteDao limiteDao) {
        this.limiteDao = limiteDao;
    }

    public SicaServiceData getSicaServiceData() {
        return sicaServiceData;
    }

    public void setSicaServiceData(SicaServiceData sicaServicaData) {
        this.sicaServiceData = sicaServicaData;
    }

    private LimiteDao limiteDao;

    private SicaServiceData sicaServiceData;

    /**
     * Constante para el valor 100.0.
     */
    private static final double CIEN = 100.0;   

    /**
     * Constante para la cadena "Total".
     */
    private static final String TOTAL_STR = "Total:";    
}
