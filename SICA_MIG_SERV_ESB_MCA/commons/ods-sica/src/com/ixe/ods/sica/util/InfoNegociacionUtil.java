package com.ixe.ods.sica.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * Utilidades para el modulo de informacion de negociacion
 * 
 * @author Cesar Jeronimo Gomez
 */
public class InfoNegociacionUtil {
	
	/**
	 * Verifica si el deal dado se encuentra dentro de la cadena csv
	 * 
	 * @param strIdDeal
	 * @param csvStrDeal
	 * @return
	 */
	public static boolean isDealInCsv(String strIdDeal, String csvStrDeal) {
    	String[] dealsArray = csvStrDeal.split(",");
		for(int i = 0; i < dealsArray.length; i ++) {
			if(strIdDeal.equals(dealsArray[i])) {
				return true;
			}
		}
		return false;
    }
    
	/**
	 * Inserta un deal dentro de una cadena csv eliminando el ultimo deal de ser necesario
	 * 
	 * @param strIdDeal
	 * @param csvStrDeal
	 * @return
	 */
    public static String pushIdDealInCsv(String strIdDeal, String csvStrDeal) {
    	if(csvStrDeal == null || StringUtils.isEmpty(strIdDeal)) return "";
    	
    	String[] dealsArray = null;
    	if(StringUtils.isEmpty(csvStrDeal)) {
    		dealsArray = new String[0];
    	} else {
    		dealsArray = csvStrDeal.split(",");
    	}
    	
    	List dealsList = new ArrayList();
    	for(int i = 0; i < dealsArray.length; i ++) {
    		dealsList.add(dealsArray[i]);
    	}
    	
    	if(dealsList.size() == 5) {
    		dealsList.remove(dealsList.size() - 1);
    		dealsList.add(0, strIdDeal);
    	} else if (dealsList.size() < 5) {
    		dealsList.add(0, strIdDeal);
    	}
    	
    	StringBuffer sbDealCsvList = new StringBuffer("");
    	int i = 1;
    	for(Iterator it = dealsList.iterator(); it.hasNext();) {
			String current = (String) it.next();
			sbDealCsvList.append(current);
			if(i < dealsList.size()) {
				sbDealCsvList.append(",");
			}
			i ++;
		}
    	
    	return sbDealCsvList.toString();
    }

}
