/*
 * $Id: GeneracionPolizaSolomon.java,v 1.13 2009/12/22 17:19:36 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2004 - 2009 LegoSoft S.C.
 */

package com.ixe.ods.sica.pages.contabilidad;

import com.ixe.ods.sica.model.Divisa;
import com.ixe.ods.sica.model.Estado;
import com.ixe.ods.sica.model.Poliza;
import com.ixe.ods.sica.pages.SicaPage;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import org.apache.tapestry.IRequestCycle;

/**
 * P&aacute;gina que permite al usuario generas los movimientos contables.
 *
 * @author Edgar Leija
 * @version  $Revision: 1.13 $ $Date: 2009/12/22 17:19:36 $
 */
public abstract class GeneracionPolizaSolomon extends SicaPage {

    /**
     * M&eacut;etodo que manda a llamar a la parte de contabilidad dentro del cierre del d&iacute;a
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void activate(IRequestCycle cycle) {
        super.activate(cycle);
    }

    /**
     * Regresa la lista de estados v&aacute;lidos: operaci&oacute;n normal, restringida, vespertina,
     * fin de liquidaciones, generaci&oacute;n contable y operaci&oacute;n nocturna.
     *
     * @return int[].
     */
    protected int[] getEstadosValidos() {
        return new int[]{Estado.ESTADO_OPERACION_NORMAL, Estado.ESTADO_OPERACION_RESTRINGIDA,
                Estado.ESTADO_OPERACION_VESPERTINA, Estado.ESTADO_FIN_LIQUIDACIONES,
                Estado.ESTADO_GENERACION_CONTABLE, Estado.ESTADO_CIERRE_DIA,
                Estado.ESTADO_OPERACION_NOCTURNA
        };
    }

    /**
     * Activa el submit de la forma.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void formSubmit(IRequestCycle cycle) {
    	try {
			export(cycle);
		}
        catch (Exception e) {
			e.printStackTrace();
		}
    }

    /**
     * Verifica el formato de 10 d&iacute;gitos de las cuentas contables;
     * si la cuenta tiene menos de 10 d&iacute;gitos, se aumentan '0' a
     * la izquierda.
     *
     * @param cuentaContable La cuenta contable.
     * @return String
     */
    public String formatearCuentaContable(String cuentaContable) {
    	if (cuentaContable.length()!= 10) {
    		for (int i=0; i<cuentaContable.length(); i++) {
                cuentaContable = "0" + cuentaContable;
            }
    		return cuentaContable;
    	}
    	else {
    		return cuentaContable;
    	}
    }

    /**
     * Verifica el fomato de las entidades; si la entidad tiene un
     * espacio en blanco, se sustituye por el caracter '-'
     *
     * @param entidad La entidad de la cuenta.
     * @return String.
     */
    public String  formatearEntidad(String entidad){
    	String ent = "";
    	Character C = new Character('-');
    	char c = C.charValue();
    	for (int i = 0; i < entidad.length(); i ++) {
    		if (entidad.charAt(i) != c){
    			ent += entidad.charAt(i);
    		}
    	}
    	return ent;
    }

    /**
     * Implementa la funci&oacute;n CSVExport.
     *
     * @param cycle Objeto controlador de Tapestry que administra el ciclo de cada petici&oacute;n
     * (<i>request</i>) en el servidor web.
     *
     * @throws Exception Si ocurre alg&uacute;n error.
     */
    public void export(IRequestCycle cycle) throws Exception {
    	try {
        	CsvExportPage nextPage = (CsvExportPage) cycle.getPage("CsvExportPage");
        	StringBuffer sb = new StringBuffer();
        	SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy");
        	SimpleDateFormat DATE_FORMAT2 = new SimpleDateFormat("yyyyMM");
        	NumberFormat NUMBER_FORMAT = new DecimalFormat("#0.00");
        	Calendar gcInicio = new GregorianCalendar();
            Calendar gcFin = new GregorianCalendar();
            gcInicio.setTime(getRegisterDate());
            gcFin.setTime(getRegisterDate());
            gcInicio.set(Calendar.HOUR_OF_DAY,0);
            gcInicio.set(Calendar.MINUTE, 0);
            gcInicio.set(Calendar.SECOND, 0);
            gcInicio.set(Calendar.MILLISECOND, 0);
            gcFin.add(Calendar.DAY_OF_MONTH, 1);
            gcFin.add(Calendar.HOUR_OF_DAY, -gcFin.get(Calendar.HOUR_OF_DAY));
            gcFin.add(Calendar.MINUTE, -gcFin.get(Calendar.MINUTE));
            gcFin.add(Calendar.SECOND, -gcFin.get(Calendar.SECOND));
            gcFin.add(Calendar.MILLISECOND, -gcFin.get(Calendar.MILLISECOND));
            List polizas = getSicaServiceData().findPolizaPorFecha(gcInicio.getTime(),
                    gcFin.getTime());
            if (polizas.isEmpty()) {
                getDelegate().record("No hay polizas para la fecha seleccionada.", null);
            	return;
            }
            List divisas = findDivisasEnPolizas(polizas);
            for (Iterator it = divisas.iterator(); it.hasNext();) {
            	Divisa div = (Divisa) it.next();
                List tiposCambio = findTiposdeCambioEnPolizas(polizas, div.getIdDivisa());
                for (Iterator it4 = tiposCambio.iterator(); it4.hasNext();) {
                	Double tipoDeCambio = (Double) it4.next();
                    List cuentas = findCuentasContablesEnPolizasByDivisa(polizas, div.getIdDivisa(),
                            tipoDeCambio);
                    List importes = findImportePolizasByDivisa(polizas, div.getIdDivisa(), "C",
                            tipoDeCambio);
    	    		double importeTotal=0;
    	    		Date fechaValor = new Date();
    	    		for (Iterator it1 = importes.iterator(); it1.hasNext();) {
    	    			Poliza pol = (Poliza) it1.next();
    	    			importeTotal += pol.getImporte();
    	    			fechaValor = pol.getFechaValor();
    	    		}
                	if (!Divisa.PESO.equals(div.getIdDivisa())) {
                		sb.append("Currency").append(",");
                        sb.append(Divisa.LIBRA_ESTERLINA.equals(div.getIdDivisa()) ?
                                "STG" : div.getIdDivisa()).append("\r\n");
        	    		sb.append("Curyrate").append(",");
        	    		sb.append("OPER").append(",");
        	    		sb.append(DATE_FORMAT.format(fechaValor)).append(",");
        	    		sb.append(tipoDeCambio.toString()).append("\r\n");
                	}
                	sb.append("Batch").append(",");
    	    		sb.append("").append(",");
    	    		sb.append("CA").append(",");
    	    		sb.append("N").append(",");
    	    		sb.append(DATE_FORMAT2.format(fechaValor)).append(",");
    	    		sb.append("B").append(",");
    	    		sb.append("CONTA").append(",");
    	    		sb.append("").append(",");
    	    		sb.append("").append(",");
    	    		sb.append("").append(",");
    	    		sb.append("").append(",");
    	    		sb.append(NUMBER_FORMAT.format(importeTotal)).append("\r\n");
                    List entidades = findEntidadesEnPolizasByDivisa(polizas,
                            div.getIdDivisa(), tipoDeCambio);
                	for (Iterator it1 = cuentas.iterator(); it1.hasNext();) {
                		String cuentaContable = (String) it1.next();
                		for (Iterator it2 = entidades.iterator(); it2.hasNext();) {
                			String entidad = (String) it2.next();
                			double importeC = 0;
                			double importeA = 0;
                            List polizasSumC = findSumPolizasByEntidadCuentaDivisa(polizas,
                                    div.getIdDivisa(), cuentaContable, entidad, "C", tipoDeCambio);
                			for (Iterator it3 = polizasSumC.iterator(); it3.hasNext();) {
                				Poliza pol = (Poliza) it3.next();
                				importeC += pol.getImporte();
                			}
                            List polizasSumA = findSumPolizasByEntidadCuentaDivisa(polizas,
                                    div.getIdDivisa(), cuentaContable, entidad, "A", tipoDeCambio);
                			for (Iterator it3 = polizasSumA.iterator(); it3.hasNext();) {
                				Poliza pol = (Poliza) it3.next();
                				importeA += pol.getImporte();
                			}
                			List polizaTmp = new ArrayList();
                			if (polizasSumC.size() < 1) {
                				polizaTmp = polizasSumA;
                			}
                			else {
                				polizaTmp = polizasSumC;
                			}
                			for (Iterator it3 = polizaTmp.iterator(); it3.hasNext();) {
                				Poliza pol = (Poliza) it3.next();
                				if (!it3.hasNext()) {
                		    		sb.append("Detail").append(",");
                		    		sb.append("002").append(",");
                                    sb.append(formatearCuentaContable(pol.getCuentaContable())).
                                            append(",");
                		    		sb.append(formatearEntidad(pol.getEntidad())).append(",");
                		    		sb.append("").append(",");
                		    		sb.append(DATE_FORMAT.format(fechaValor)).append(",");
                		    		sb.append("").append(",");
                		    		sb.append(NUMBER_FORMAT.format(importeC)).append(",");
                		    		sb.append("").append(",");
                		    		sb.append("COMPRA VENTA VALORES MESA CAMBIOS");
                		    		sb.append("").append(",");
                		    		sb.append("").append(",\r\n");
                		    		sb.append("Detail").append(",");
                		    		sb.append("002").append(",");
                                    sb.append(formatearCuentaContable(pol.getCuentaContable())).
                                            append(",");
                		    		sb.append(formatearEntidad(pol.getEntidad())).append(",");
                		    		sb.append("").append(",");
                		    		sb.append(DATE_FORMAT.format(fechaValor)).append(",");
                		    		sb.append("").append(",");
                		    		sb.append("").append(",");
                		    		sb.append(NUMBER_FORMAT.format(importeA)).append(",");
                		    		sb.append("COMPRA VENTA VALORES MESA CAMBIOS");
                		    		sb.append("").append(",");
                		    		sb.append("").append(",\r\n");
                		    		importeC = 0;
                		    		importeA = 0;
                				}
                			}
                		}
                	}
                }
            }
            nextPage.setSB(sb);
            cycle.activate(nextPage);
        }
        catch (Exception e) {
        	e.printStackTrace();
        }
    }

    /**
     * Regresa la lista de divisas que se encuentran en las p&oacute;lizas.
     *
     * @param polizas la lista de p&oacute;lizas.
     * @return List de Divisa.
     */
    private List findDivisasEnPolizas(List polizas) {
        List divisas = new ArrayList();
        for (Iterator it = polizas.iterator(); it.hasNext();) {
            Poliza p = (Poliza) it.next();
            if (!divisas.contains(p.getDivisa())) {
                divisas.add(p.getDivisa());
            }
        }
        return divisas;
    }

    /**
     * Regresa los distintos tipos de cambio que existen para un divisa dentro de la lista de
     * p&oacute;lizas.
     *
     * @param polizas La lista de polizas.
     * @param idDivisa La clave de la divisa a filtrar.
     * @return List de Double.
     */
    public List findTiposdeCambioEnPolizas(List polizas, String idDivisa) {
        List tiposCambio = new ArrayList();
        for (Iterator it = polizas.iterator(); it.hasNext();) {
            Poliza p = (Poliza) it.next();
            if (idDivisa.equals(p.getDivisa().getIdDivisa()) &&
                    !tiposCambio.contains(new Double(p.getTipoCambio()))) {
                tiposCambio.add(new Double(p.getTipoCambio()));
            }
        }
        return tiposCambio;
    }

    /**
     * Regresa las distintas cuentas contables que existen para un divisa y tipo de cambio, dentro
     * de la lista de p&oacute;lizas.
     *
     * @param polizas La lista de polizas.
     * @param idDivisa La clave de la divisa a filtrar.
     * @param tipoCambio El tipo de cambio.
     * @return List de String.
     */
    public List findCuentasContablesEnPolizasByDivisa(List polizas, String idDivisa,
                                                      Double tipoCambio) {
        List cuentas = new ArrayList();
        for (Iterator it = polizas.iterator(); it.hasNext();) {
            Poliza p = (Poliza) it.next();
            if (idDivisa.equals(p.getDivisa().getIdDivisa()) &&
                    tipoCambio.equals(new Double(p.getTipoCambio())) &&
                    !cuentas.contains(p.getCuentaContable())) {
                cuentas.add(p.getCuentaContable());
            }
        }
        return cuentas;
    }

    /**
     * Regresa las distintas p&oacute;lizas que existen para un divisa, tipo de cambio y tipo de
     * asiento, dentro de la lista de p&oacute;lizas.
     *
     * @param polizas La lista de polizas.
     * @param idDivisa La clave de la divisa a filtrar.
     * @param cargoAbono El tipo de asiento 'A' o 'B'.
     * @param tipoCambio El tipo de cambio.
     * @return List de Poliza.
     */
    public List findImportePolizasByDivisa(List polizas, String idDivisa, String cargoAbono,
                                           Double tipoCambio) {
        List resultados = new ArrayList();
        for (Iterator it = polizas.iterator(); it.hasNext();) {
            Poliza p = (Poliza) it.next();
            if (idDivisa.equals(p.getDivisa().getIdDivisa()) &&
                    tipoCambio.equals(new Double(p.getTipoCambio())) &&
                    p.getCargoAbono().indexOf(cargoAbono) >= 0) {
                resultados.add(p);
            }
        }
        return resultados;
    }

    /**
     * Regresa las distintas entidades contables que existen para un divisa, y tipo de cambio,
     * dentro de la lista de p&oacute;lizas.
     *
     * @param polizas La lista de polizas.
     * @param idDivisa La clave de la divisa a filtrar.
     * @param tipoCambio El tipo de cambio.
     * @return List de String.
     */
    public List findEntidadesEnPolizasByDivisa(List polizas, String idDivisa,
                                               Double tipoCambio) {
        List resultados = new ArrayList();
        for (Iterator it = polizas.iterator(); it.hasNext();) {
            Poliza p = (Poliza) it.next();
            if (idDivisa.equals(p.getDivisa().getIdDivisa()) &&
                    tipoCambio.equals(new Double(p.getTipoCambio())) &&
                    !resultados.contains(p.getEntidad())) {
                resultados.add(p.getEntidad());
            }
        }
        return resultados;
    }

    /**
     * Regresa las distintas entidades contables que existen para un divisa, cuenta, entidad, tipo
     * de asiento y tipo de cambio, dentro de la lista de p&oacute;lizas.
     *
     * @param polizas La lista de polizas.
     * @param idDivisa La clave de la divisa a filtrar.
     * @param cuenta El n&uacute;mero de cuenta contable.
     * @param entidad El n&uacute;mero de entidad contable.
     * @param cargoAbono El tipo de asiento 'A' o 'B'.
     * @param tipoCambio El tipo de cambio.
     * @return List de Polizas.
     */
    public List findSumPolizasByEntidadCuentaDivisa(List polizas, String idDivisa,
                                                    String cuenta, String entidad,
                                                    String cargoAbono, Double tipoCambio) {
        List resultados = new ArrayList();
        for (Iterator it = polizas.iterator(); it.hasNext();) {
            Poliza p = (Poliza) it.next();
            if (idDivisa.equals(p.getDivisa().getIdDivisa()) &&
                    cuenta.equals(p.getCuentaContable()) &&
                    entidad.equals(p.getEntidad()) &&
                    p.getCargoAbono().indexOf(cargoAbono) == 0 &&
                    tipoCambio.equals(new Double(p.getTipoCambio()))) {
                resultados.add(p);
            }
        }
        return resultados;
    }

    /**
    * Fija el valor del resultado.
     *
     * @param s El valor a asignar.
     */
    public abstract void setResult(String s);

    /**
     * Regresa el string del resultado.
     *
     * @return String
     */
    public abstract String getResult();

    /**
     * Obtiene el valor de registerDate
     *
     * @return Date
     */
    public abstract Date getRegisterDate();

    /**
     * Establece el valor para registerDate
     *
     * @param registerDate Valor de la fecha seleccionada
     */
    public abstract void setRegisterDate(Date registerDate);
}

