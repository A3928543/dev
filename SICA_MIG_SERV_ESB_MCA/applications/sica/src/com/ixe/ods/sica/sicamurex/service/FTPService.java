package com.ixe.ods.sica.sicamurex.service;

import java.io.IOException;
import java.net.SocketException;

import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.model.Corte;



/**
 *
 * @author HMASG771
 * @version $Revision: 1.1.2.2 $ $Date: 2013/09/27 23:31:59 $
 *Interface que permite realizar la transferencia del archivo
 *con los acumualdos de compra/venta de Divisas
 */
public interface FTPService {
	
    /** Prefijo del Archivo de Corte * */
    public static final String PREFIJO_NOMBRE_ARCHIVO_CORTE = "ATSICA";

    /** Guion * */
    public static final String GUION = "-";

    /** Extensión del archivo CSV * */
    public static final String EXTENSION_ARCHIVO_CORTE = ".csv";

    /** Separador de campos del archivo * */
    public static final String SEPARADOR_CSV = ",";

    /** Cadena Vacia  **/
    public static final String EMPTY_STR = "";
    
    /** Salto de linea * */
    public static final String SALTO_LINEA = "\n";

    /** Columna type * */
    public static final String TYPE = "type";

    /** Columna action * */
    public static final String ACTION = "action";

    /**  Columna tradeGlobalId * */
    public static final String TRADE_GLOBAL_ID = "tradeGlobalId";

    /**  Columna tradeDate * */
    public static final String TRADE_DATE = "tradeDate";

    /**  Columna ourName * */
    public static final String OUR_NAME = "ourName";

    /**  Columna theirName * */
    public static final String THEIR_NAME = "theirName";

    /** Columna  ourPortfolio * */
    public static final String OUR_PORTFOLIO = "ourPortfolio";

    /** Columna theirPortfolio * */
    public static final String THEIR_PORTFOLIO = "theirPortfolio";

    /** Columna user * */
    public static final String USER = "user";

    /** Columna group * */
    public static final String GROUP = "group";

    /** Columna  buySell * */
    public static final String BUY_SELL = "buySell";

    /** Columna contract  * */
    public static final String CONTRACT = "contract";

    /** Columna deliveryDate  * */
    public static final String DELIVERY_DATE_LABEL = "deliveryDateLabel";

    /** Columna  deliveryDate * */
    public static final String DELIVERY_DATE = "deliveryDate";

    /** Columna  amount* */
    public static final String AMOUNT = "amount";

    /** Columna  * */
    public static final String ALTAMIRA_PRICE = "altamiraPrice";

    /** Columna altamiraPrice * */
    public static final String FUNDING_COST = "fundingCost";

    /** Columna ccy * */
    public static final String CCY = "ccy";

    /** Columna  accountingSectionSource* */
    public static final String ACCOUNTING_SECTION_SOURCE = "accountingSectionSource";

    /** Columna backToBackPortfolioName * */
    public static final String BACK_TO_BACK_PORTFOLIO_NAME = "backToBackPortfolioName";

    /** Columna  sourceComments* */
    public static final String SOURCE_COMMENTS = "sourceComments";

    /**
     * Este método se encarga de transferir un archivo via FTP
     * @param usuario 
     * @param archivo archivo a tranferir.
     * @return bandera que indica que la transferencia fue efecutada correctamente.
     * @throws IOException 
     * @throws SocketException 
     */
    public boolean transferir(String usuario) throws SicaException;

    /**
     * Genera el archivo del corte del día hoy.
     *
     * @return un <code>byte[]</code> con el flujo
     *                 generado.
     */
    public String getRegistrosArchivoCorte(Corte corte);

    /**
     * Obtiene el nombre del archivo 
     * 		de Corte que se reporta a Murex.
     * 		
     *
     * @return <code>String</code> con el nombre
     * 	del archivo de corte.
     */
    public String getNombreArchivoCorte();
   
}
