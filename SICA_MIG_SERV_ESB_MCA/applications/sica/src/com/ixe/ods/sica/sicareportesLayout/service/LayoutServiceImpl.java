package com.ixe.ods.sica.sicareportesLayout.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Calendar;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.model.ParametroSica;
import com.ixe.ods.sica.sdo.SicaServiceData;
import com.ixe.ods.sica.utils.DateUtils;
import com.ixe.ods.sica.sdo.ReportesServiceData;
import com.ixe.ods.sica.services.GeneralMailSender;


public class LayoutServiceImpl implements LayoutService{
	

	
    /** El logger de la clase * */
    private Logger _logger = Logger.getLogger(LayoutService.class);
    
	private String usuario;
	private String contrasena;
	private String rutaServidorFTP;
	private String ipServidor;
	private String rutaLayoutClientes;
	private String rutaLayoutContratos;
	private String rutaLayoutTransacciones;
	private int    respuesta;
	private int    timeOut;
    private FTPClient ftp = new FTPClient();
    private SicaServiceData sicaServiceData;
    private ReportesServiceData reportesServiceData;
    private GeneralMailSender generalMailSender;
    protected ApplicationContext _appContext;
    

    /** Prefijo del Archivo de Clientes * */
    public static final String PREFIJO_NOMBRE_ARCHIVO_CLIENTES = "sicaClientes";
    
    /** Prefijo del Archivo de Clientes antes de renombrarlo * */
    public static final String PREFIJO_NOMBRE_ARCHIVO_CLIENTES_AR = "AntRenombrarClientes";
    
    /** Prefijo del Archivo de Contratos * */
    public static final String PREFIJO_NOMBRE_ARCHIVO_CONTRATOS = "sicaContratos";
    
    /** Prefijo del Archivo de Contratos antes de renombrarlo * */
    public static final String PREFIJO_NOMBRE_ARCHIVO_CONTRATOS_AR = "AntRenombrarContratos";
    
    /** Prefijo del Archivo de Transacciones * */
    public static final String PREFIJO_NOMBRE_ARCHIVO_TRANSACCIONES = "sicaTransacciones";
    
    /** Prefijo del Archivo de Transacciones * */
    public static final String PREFIJO_NOMBRE_ARCHIVO_TRANSACCIONES_AR = "AntRenombrarTransacciones";
    
    /** Nombre del Archivo de Cifras de Control para el archivo de clientes **/
    public static final String NOMBRE_ARCHIVO_CIFRAS_CTRL_CLIENTES = "ALTCLIENTES";
    
    /** Nombre del Archivo de Cifras de Control para el archivo de clientes **/
    public static final String NOMBRE_ARCHIVO_CIFRAS_CTRL_CONTRATOS = "ALTCTAABC";

    

    /** Extensión del archivo txt * */
    public static final String EXTENSION_ARCHIVO = ".txt";
	
    public static final String SEPARADOR_CSV = ",";

    /** Cadena Vacia  **/
    public static final String EMPTY_STR = "";
    
    /** Salto de linea **/
    public static final String SALTO_LINEA = "\n";

    /** Columna NUMCLIEN **/
    public static final String NUMCLIEN = "numClien";

    /** Columna CODIDENT **/
    public static final String CODIDENT = "codIdent";

    /**  Columna CLAIDENT **/
    public static final String CLAIDENT = "claIdent";

    /**  Columna PRIAPE **/
    public static final String PRIAPE = "priApe";

    /**  Columna SEGAPE **/
    public static final String SEGAPE = "segApe";

    /**  Columna NOMBRE **/
    public static final String NOMBRE = "nombre";

    /** Columna  SEXO **/
    public static final String SEXO = "sexo";

    /** Columna ESTCIVIL **/
    public static final String ESTCIVIL = "estCivil";

    /** Columna TITULO **/
    public static final String TITULO = "titulo";

    /** Columna TRATAMIE **/
    public static final String TRATAMIE = "tratamie";

    /** Columna  PETDOMIC **/
    public static final String PETDOMIC = "petDomic";

    /** Columna IDEDIREC **/
    public static final String IDEDIREC = "ideDirec";

    /** Columna NOMCALLE  **/
    public static final String NOMCALLE = "nomCalle";

    /** Columna  NUMCALLE **/
    public static final String NUMCALLE = "numCalle";

    /** Columna  TIPOVIVI **/
    public static final String TIPOVIVI = "tipoVivi";

    /** Columna  ENTRADA **/
    public static final String ENTRADA = "entrada";

    /** Columna PISO **/
    public static final String PISO = "piso";

    /** Columna DEPARTAM **/
    public static final String DEPARTAM = "departam";

    /** Columna COLONIA **/
    public static final String COLONIA = "colonia";

    /** Columna DELMUNI **/
    public static final String DELMUNI = "delMuni";

    /** Columna  DESDELMUNI **/
    public static final String DESDELMUNI = "desDelMuni";
    
    /** Columna  ESTADO **/
    public static final String ESTADO = "estado";

    /** Columna  DESESTADO **/
    public static final String DESESTADO = "desestado";

    /** Columna CODPOST **/
    public static final String CODPOST = "codPost";

    /** Columna CODPAIS **/
    public static final String CODPAIS = "codPais";

    /** Columna  CURP **/
    public static final String CURP = "curp";

    /** Columna RFC **/
    public static final String RFC = "rfc";

    /** Columna  IMSS **/
    public static final String IMSS = "imss";
    
    /** Columna  INFONAVI **/
    public static final String INFONAVI = "infonavi";

    /** Columna SUJGRUP **/
    public static final String SUJGRUP = "sujGrup";

    /** Columna  SUJSUBG1 **/
    public static final String SUJSUBG1 = "sujSubG1";
    
    /** Columna  FENACIMI **/
    public static final String FENACIMI = "fenaCimi";

    /** Columna CNAEG **/
    public static final String CNAEG = "cnaeg";

    /** Columna CNO **/
    public static final String CNO = "cno";
    
    /** Columna SEGMENTO **/
    public static final String SEGMENTO = "segmento";

    /** Columna  NUMDOCLI **/
    public static final String NUMDOCLI = "numDoClie";
    
    /** Columna  INDGRANC **/
    public static final String INDGRANC = "indgranc";

    /** Columna CLATELEF1 **/
    public static final String CLATELEF1 = "clatelef1";

    /** Columna NUMTELEF1 **/
    public static final String NUMTELEF1 = "numtelef1";
    
    /** Columna CLATELEF2 **/
    public static final String CLATELEF2 = "clatelef2";
    
    /** Columna NUMTELEF2 **/
    public static final String NUMTELEF2 = "numtelef2";
    
    /** Columna CLATELEF3 **/
    public static final String CLATELEF3 = "clatelef3";
    
    /** Columna NUMTELEF3 **/
    public static final String NUMTELEF3 = "numtelef3";
    
    /** Columna CRGESTOR     **/
    public static final String CRGESTOR     = "crgestor";
    
    /** Columna FUNCIONA **/
    public static final String FUNCIONA = "funciona";
    
    /** Columna AREANEG **/
    public static final String AREANEG = "areaneg";
    
    /** Columna INDBAJA **/
    public static final String INDBAJA = "indbaja";
    
    /** Columna FEALTCLI **/
    public static final String FEALTCLI = "fealtcli";
    
    /** Columna PEHSTAMP **/
    public static final String PEHSTAMP = "pehstamp";
    
    /** Columna PECNACIO **/
    public static final String PECNACIO = "pecnacio";
    
    /** Columna LUGANACI **/
    public static final String LUGANACI = "luganaci";
    
    /** Columna PECNARES **/
    public static final String PECNARES = "pecnares";
    
    /** Columna EMAIL **/
    public static final String EMAIL = "email";
    
    /** Columna WEB2 **/
    public static final String WEB2 = "web2";
    
    /** Columna CENTRABA **/
    public static final String CENTRABA = "centraba";
    
    /** Columna FECHINGR     **/
    public static final String FECHINGR     = "fechingr";
    
    /** Columna REGIMENM **/
    public static final String REGIMENM = "regimenm";
    
    /** Columna CODESTUD  **/
    public static final String CODESTUD  = "codestud";
    
    /** Columna EMPLEADO   **/
    public static final String EMPLEADO   = "empleado";
    
    /** Columna ENTINACI   **/
    public static final String ENTINACI   = "entinaci";
    
    /** Columna FECNACHI   **/
    public static final String FECNACHI   = "fecnachi";
    
    /** Columna NUMAUTOS   **/
    public static final String NUMAUTOS   = "numautos";
    
    /** Columna NUMHIJOS   **/
    public static final String NUMHIJOS   = "numhijos";
    
    /** Columna NUMBIENE   **/
    public static final String NUMBIENE   = "numbiene";
    
    /** Columna ORIGEN   **/
    public static final String ORIGEN   = "origen";
    
    /** Columna TIEMPRES   **/
    public static final String TIEMPRES   = "tiempres";
    
    /** Columna CODAFICI   **/
    public static final String CODAFICI   = "codafici";
    
    /** Columna OTROSBAN   **/
    public static final String OTROSBAN   = "otrosban";
    
    /** Columna COBGEOG   **/
    public static final String COBGEOG   = "cobgeog";
    
    /** Columna NUMSUC   **/
    public static final String NUMSUC   = "numsuc";
    
    /** Columna NUMEMPLE   **/
    public static final String NUMEMPLE   = "numemple";
    
    /** Columna FUENTEIN   **/
    public static final String FUENTEIN   = "fuentein";
    
    /** Columna INGMENS   **/
    public static final String INGMENS   = "ingmens";
    
    /** Columna IMPVTAS   **/
    public static final String IMPVTAS   = "impvtas";
    
    /** Columna ACTIVO   **/
    public static final String ACTIVO   = "activo";
    
    /** Columna PASIVO   **/
    public static final String PASIVO   = "pasivo";
    
    /** Columna CAPITAL   **/
    public static final String CAPITAL   = "capital";
    
    /** Columna IMPEXP   **/
    public static final String IMPEXP   = "impexp";
    
    /** Columna FEALTCLI2   **/
    public static final String FEALTCLI2   = "fealtcli2";
    
    /** Columna FECPROC   **/
    public static final String FECPROC   = "fecproc";
    
    /** Columna RIESGO   **/
    public static final String RIESGO   = "riesgo";
    
    /** Columna FIEL   **/
    public static final String FIEL   = "fiel";
    
    /** Columna ART10   **/
    public static final String ART10   = "art10";
    
    /** Columna VARIOS **/
    public static final String VARIOS = "varios";

    /** Columna PECENTID **/
    public static final String PECENTID = "pecentid";
    
    /** Columna OFIAPE **/
    public static final String OFIAPE = "ofiape";

    /** Columna CODISER **/
    public static final String CODISER = "codiser";

    /** Columna CTALTA **/
    public static final String CTALTA = "ctalta";

    /** Columna CLAINTER **/
    public static final String CLAINTER = "clainter";

    /** Columna SECINTER **/
    public static final String SECINTER = "secinter";

    /** Columna NUMDOMIC12 **/
    public static final String NUMDOMIC12 = "numdomic12";

    /** Columna FEALRELA **/
    public static final String FEALRELA = "fealrela";

    /** Columna FECHAPE **/
    public static final String FECHAPE = "fechape";

    /** Columna PRODUCTO **/
    public static final String PRODUCTO = "producto";

    /** Columna SUBPRO **/
    public static final String SUBPRO = "subpro";

    /** Columna FECHCANCEL **/
    public static final String FECHCANCEL = "fechcancel";

    /** Columna FECHAUMO **/
    public static final String FECHAUMO = "fechaumo";

    /** Columna SECTOR **/
    public static final String SECTOR = "sector";

    /** Columna TASAINT **/
    public static final String TASAINT = "tasaint";

    /** Columna SDOPROMDOCE **/
    public static final String SDOPROMDOCE = "sdopromdoce";

    /** Columna INDESTA **/
    public static final String INDESTA = "indesta";

    /** Columna CENTRO-CONTAB **/
    public static final String CENTRO_CONTAB = "centro-contab";

    /** Columna FECHA-ALTA **/
    public static final String FECHA_ALTA = "fecha-alta";

    /** Columna INDICADOR **/
    public static final String INDICADOR = "indicador";

    /** Columna DIVISA **/
    public static final String DIVISA = "divisa";

    /** Columna BALANCE **/
    public static final String BALANCE = "balance";

    /** Columna CODEMPLEADO **/
    public static final String CODEMPLEADO = "codempleado";

    /** Columna ACCT_OPEN_DATE **/
    public static final String ACCT_OPEN_DATE = "acct_open_date";

    /** Columna ROW_UPDATE_DATE **/
    public static final String ROW_UPDATE_DATE = "row_update_date";

    /** Columna VALOR **/
    public static final String VALOR = "valor";

    /** Columna TRANSACTION_TYPE_CD **/
    public static final String TRANSACTION_TYPE_CD = "transaction_type_cd";

    /** Columna TRANSACTION_CODE_CD **/
    public static final String TRANSACTION_CODE_CD = "transaction_code_cd";

    /** Columna TRANS_TYPE_CATEGORY **/
    public static final String TRANS_TYPE_CATEGORY = "trans_type_category";

    /** Columna TRANS_TYPE_CD **/
    public static final String TRANS_TYPE_CD = "trans_type_cd";

    /** Columna ACCOUNT_KEY **/
    public static final String ACCOUNT_KEY = "account_key";

    /** Columna ACCT_CURRENCY_AMOUNT **/
    public static final String ACCT_CURRENCY_AMOUNT = "acct_currency_amount";

    /** Columna ID_DIVISA **/
    public static final String ID_DIVISA = "id_divisa";

    /** Columna MONTO_MONEDA_NACIONAL **/
    public static final String MONTO_MONEDA_NACIONAL = "monto_moneda_nacional";

    /** Columna PAYEE_ID_NO **/
    public static final String PAYEE_ID_NO = "payee_id_no";

    /** Columna TRANSACTION_KEY **/
    public static final String TRANSACTION_KEY = "transaction_key";

    /** Columna OPP_ACCOUNT_KEY **/
    public static final String OPP_ACCOUNT_KEY = "opp_account_key";

    /** Columna OPP_COUNTRY_CD **/
    public static final String OPP_COUNTRY_CD = "opp_country_cd";

    /** Columna OPP_ORG_KEY **/
    public static final String OPP_ORG_KEY = "opp_org_key";

    /** Columna COD1 **/
    public static final String COD1 = "cod1";

    /** Columna COD2 **/
    public static final String COD2 = "cod2";

    
    
    /**
     * Genera el encabezado del Reporte del Layout de clientes
     *
     * @param b <code>StringBuffer</code>
     * 		que será poblado con el encabezado.
     */
    public void generarEncabezadoArchivoReporteClientes(StringBuffer b) {
        b.append(NUMCLIEN);
        b.append(SEPARADOR_CSV);

        b.append(CODIDENT);
        b.append(SEPARADOR_CSV);

        b.append(CLAIDENT);
        b.append(SEPARADOR_CSV);

        b.append(PRIAPE);
        b.append(SEPARADOR_CSV);

        b.append(SEGAPE);
        b.append(SEPARADOR_CSV);

        b.append(NOMBRE);
        b.append(SEPARADOR_CSV);

        b.append(SEXO);
        b.append(SEPARADOR_CSV);

        b.append(ESTCIVIL);
        b.append(SEPARADOR_CSV);

        b.append(TITULO);
        b.append(SEPARADOR_CSV);

        b.append(TRATAMIE);
        b.append(SEPARADOR_CSV);

        b.append(PETDOMIC);
        b.append(SEPARADOR_CSV);

        b.append(IDEDIREC);
        b.append(SEPARADOR_CSV);

        b.append(NOMCALLE);
        b.append(SEPARADOR_CSV);

        b.append(NUMCALLE);
        b.append(SEPARADOR_CSV);

        b.append(TIPOVIVI);
        b.append(SEPARADOR_CSV);

        b.append(ENTRADA);
        b.append(SEPARADOR_CSV);

        b.append(PISO);
        b.append(SEPARADOR_CSV);

        b.append(DEPARTAM);
        b.append(SEPARADOR_CSV);

        b.append(COLONIA);
        b.append(SEPARADOR_CSV);

        b.append(DELMUNI);
        b.append(SEPARADOR_CSV);

        b.append(DESDELMUNI);
        b.append(SEPARADOR_CSV);
        
        b.append(ESTADO);
        b.append(SEPARADOR_CSV);

        b.append(DESESTADO);
        b.append(SEPARADOR_CSV);

        b.append(CODPOST);
        b.append(SEPARADOR_CSV);

        b.append(CODPAIS);
        b.append(SEPARADOR_CSV);

        b.append(CURP);
        b.append(SEPARADOR_CSV);
        
        b.append(RFC);
        b.append(SEPARADOR_CSV);

        b.append(IMSS);
        b.append(SEPARADOR_CSV);

        b.append(INFONAVI);
        b.append(SEPARADOR_CSV);

        b.append(SUJGRUP);
        b.append(SEPARADOR_CSV);

        b.append(SUJSUBG1);
        b.append(SEPARADOR_CSV);

        b.append(FENACIMI);
        b.append(SEPARADOR_CSV);

        b.append(CNAEG);
        b.append(SEPARADOR_CSV);

        b.append(CNO);
        b.append(SEPARADOR_CSV);

        b.append(SEGMENTO);
        b.append(SEPARADOR_CSV);

        b.append(NUMDOCLI);
        b.append(SEPARADOR_CSV);

        b.append(INDGRANC);
        b.append(SEPARADOR_CSV);

        b.append(CLATELEF1);
        b.append(SEPARADOR_CSV);

        b.append(NUMTELEF1);
        b.append(SEPARADOR_CSV);

        b.append(CLATELEF2);
        b.append(SEPARADOR_CSV);

        b.append(NUMTELEF2);
        b.append(SEPARADOR_CSV);

        b.append(CLATELEF3);
        b.append(SEPARADOR_CSV);

        b.append(NUMTELEF3);
        b.append(SEPARADOR_CSV);

        b.append(CRGESTOR);
        b.append(SEPARADOR_CSV);

        b.append(FUNCIONA);
        b.append(SEPARADOR_CSV);

        b.append(AREANEG);
        b.append(SEPARADOR_CSV);

        b.append(INDBAJA);
        b.append(SEPARADOR_CSV);

        b.append(FEALTCLI);
        b.append(SEPARADOR_CSV);

        b.append(PEHSTAMP);
        b.append(SEPARADOR_CSV);

        b.append(PECNACIO);
        b.append(SEPARADOR_CSV);

        b.append(LUGANACI);
        b.append(SEPARADOR_CSV);

        b.append(PECNARES);
        b.append(SEPARADOR_CSV);

        b.append(EMAIL);
        b.append(SEPARADOR_CSV);

        b.append(WEB2);
        b.append(SEPARADOR_CSV);

        b.append(CENTRABA);
        b.append(SEPARADOR_CSV);

        b.append(FECHINGR);
        b.append(SEPARADOR_CSV);

        b.append(REGIMENM);
        b.append(SEPARADOR_CSV);

        b.append(CODESTUD);
        b.append(SEPARADOR_CSV);

        b.append(EMPLEADO);
        b.append(SEPARADOR_CSV);

        b.append(ENTINACI);
        b.append(SEPARADOR_CSV);

        b.append(FECNACHI);
        b.append(SEPARADOR_CSV);

        b.append(NUMAUTOS);
        b.append(SEPARADOR_CSV);

        b.append(NUMHIJOS);
        b.append(SEPARADOR_CSV);

        b.append(NUMBIENE);
        b.append(SEPARADOR_CSV);

        b.append(ORIGEN);
        b.append(SEPARADOR_CSV);

        b.append(TIEMPRES);
        b.append(SEPARADOR_CSV);

        b.append(CODAFICI);
        b.append(SEPARADOR_CSV);

        b.append(OTROSBAN);
        b.append(SEPARADOR_CSV);

        b.append(COBGEOG);
        b.append(SEPARADOR_CSV);

        b.append(NUMSUC);
        b.append(SEPARADOR_CSV);

        b.append(NUMEMPLE);
        b.append(SEPARADOR_CSV);

        b.append(FUENTEIN);
        b.append(SEPARADOR_CSV);

        b.append(INGMENS);
        b.append(SEPARADOR_CSV);

        b.append(IMPVTAS);
        b.append(SEPARADOR_CSV);

        b.append(ACTIVO);
        b.append(SEPARADOR_CSV);

        b.append(PASIVO);
        b.append(SEPARADOR_CSV);

        b.append(CAPITAL);
        b.append(SEPARADOR_CSV);

        b.append(IMPEXP);
        b.append(SEPARADOR_CSV);

        b.append(FEALTCLI2);
        b.append(SEPARADOR_CSV);

        b.append(FECPROC);
        b.append(SEPARADOR_CSV);

        b.append(RIESGO);
        b.append(SEPARADOR_CSV);

        b.append(FIEL);
        b.append(SEPARADOR_CSV);

        b.append(ART10);
        b.append(SEPARADOR_CSV);

        b.append(VARIOS);

        b.append(SALTO_LINEA);
    }


    
    /**
     * Genera los registros del Reporte
     *
     * @param b <code>StringBuffer</code>
     * 		que será poblado con los registros.

     * @param resultadoBusqueda <code>List</code>
     * 		de objetos <code>String</code>
     * 		que son procesados.
     */
    public void generarRegistrosArchivoClientes(StringBuffer b, List resultadoBusqueda) {

        for (Iterator iter = resultadoBusqueda.iterator(); iter.hasNext();) {
            
        	Object[] tupla = (Object[]) iter.next();
        	
    if (tupla[0] != null)
                b.append((String)tupla[0].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[1] != null)
                b.append((String)tupla[1].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[2] != null)
                b.append((String)tupla[2].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[3] != null)
                b.append((String)tupla[3].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[4] != null)
                b.append((String)tupla[4].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[5] != null)
                b.append((String)tupla[5].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[6] != null)
                b.append((String)tupla[6].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[7] != null)
                b.append((String)tupla[7].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[8] != null)
                b.append((String)tupla[8].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[9] != null)
                b.append((String)tupla[9].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[10] != null)
                b.append((String)tupla[10].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[11] != null)
                b.append((String)tupla[11].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[12] != null)
                b.append((String)tupla[12].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[13] != null)
                b.append((String)tupla[13].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[14] != null)
                b.append((String)tupla[14].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[15] != null)
                b.append((String)tupla[15].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[16] != null)
                b.append((String)tupla[16].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[17] != null)
                b.append((String)tupla[17].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[18] != null)
                b.append((String)tupla[18].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[19] != null)
                b.append((String)tupla[19].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[20] != null)
                b.append((String)tupla[20].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[21] != null)
                b.append((String)tupla[21].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[22] != null)
                b.append((String)tupla[22].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[23] != null)
                b.append((String)tupla[23].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[24] != null)
                b.append((String)tupla[24].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[25] != null)
                b.append((String)tupla[25].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[26] != null)
                b.append((String)tupla[26].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[27] != null)
                b.append((String)tupla[27].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[28] != null)
                b.append((String)tupla[28].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[29] != null)
                b.append((String)tupla[29].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[30] != null)
                b.append((String)tupla[30].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[31] != null)
                b.append((String)tupla[31].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[32] != null)
                b.append((String)tupla[32].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[33] != null)
                b.append((String)tupla[33].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[34] != null)
                b.append((String)tupla[34].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[35] != null)
                b.append((String)tupla[35].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[36] != null)
                b.append((String)tupla[36].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[37] != null)
                b.append((String)tupla[37].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[38] != null)
                b.append((String)tupla[38].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[39] != null)
                b.append((String)tupla[39].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[40] != null)
                b.append((String)tupla[40].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[41] != null)
                b.append((String)tupla[41].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[42] != null)
                b.append((String)tupla[42].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[43] != null)
                b.append((String)tupla[43].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[44] != null)
                b.append((String)tupla[44].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[45] != null)
                b.append((String)tupla[45].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[46] != null)
                b.append((String)tupla[46].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[47] != null)
                b.append((String)tupla[47].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[48] != null)
                b.append((String)tupla[48].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[49] != null)
                b.append((String)tupla[49].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[50] != null)
                b.append((String)tupla[50].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[51] != null)
                b.append((String)tupla[51].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[52] != null)
                b.append((String)tupla[52].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[53] != null)
                b.append((String)tupla[53].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[54] != null)
                b.append((String)tupla[54].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[55] != null)
                b.append((String)tupla[55].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[56] != null)
                b.append((String)tupla[56].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[57] != null)
                b.append((String)tupla[57].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[58] != null)
                b.append((String)tupla[58].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[59] != null)
                b.append((String)tupla[59].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[60] != null)
                b.append((String)tupla[60].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[61] != null)
                b.append((String)tupla[61].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[62] != null)
                b.append((String)tupla[62].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[63] != null)
                b.append((String)tupla[63].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[64] != null)
                b.append((String)tupla[64].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[65] != null)
                b.append((String)tupla[65].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[66] != null)
                b.append((String)tupla[66].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[67] != null)
                b.append((String)tupla[67].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[68] != null)
                b.append((String)tupla[68].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[69] != null)
                b.append((String)tupla[69].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[70] != null)
                b.append((String)tupla[70].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[71] != null)
                b.append((String)tupla[71].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[72] != null)
                b.append((String)tupla[72].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[73] != null)
                b.append((String)tupla[73].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[74] != null)
                b.append((String)tupla[74].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[75] != null)
                b.append((String)tupla[75].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[76] != null)
                b.append((String)tupla[76].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[77] != null)
                b.append((String)tupla[77].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[78] != null)
                b.append((String)tupla[78].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[79] != null)
                b.append((String)tupla[79].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[80] != null)
                b.append((String)tupla[80].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[81] != null)
                b.append((String)tupla[81].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[82] != null)
                b.append((String)tupla[82].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[83] != null)
                b.append((String)tupla[83].toString().replaceAll(","," "));
                

            b.append(SALTO_LINEA);
        }
    }
    
    /**
     * Genera el encabezado del Reporte del Layout de contratos
     *
     * @param b <code>StringBuffer</code>
     * 		que será poblado con el encabezado.
     */
    public void generarEncabezadoArchivoReporteContratos(StringBuffer b) {
        b.append(NUMCLIEN);
        b.append(SEPARADOR_CSV);

        b.append(PECENTID);
        b.append(SEPARADOR_CSV);

        b.append(OFIAPE);
        b.append(SEPARADOR_CSV);

        b.append(CODISER);
        b.append(SEPARADOR_CSV);

        b.append(CTALTA);
        b.append(SEPARADOR_CSV);

        b.append(CLAINTER);
        b.append(SEPARADOR_CSV);

        b.append(SECINTER);
        b.append(SEPARADOR_CSV);

        b.append(NUMDOMIC12);
        b.append(SEPARADOR_CSV);

        b.append(FEALRELA);
        b.append(SEPARADOR_CSV);

        b.append(FECHAPE);
        b.append(SEPARADOR_CSV);

        b.append(PEHSTAMP);
        b.append(SEPARADOR_CSV);

        b.append(INDBAJA);
        b.append(SEPARADOR_CSV);

        b.append(PRODUCTO);
        b.append(SEPARADOR_CSV);

        b.append(SUBPRO);
        b.append(SEPARADOR_CSV);

        b.append(FECHCANCEL);
        b.append(SEPARADOR_CSV);

        b.append(FECHAUMO);
        b.append(SEPARADOR_CSV);

        b.append(SECTOR);
        b.append(SEPARADOR_CSV);

        b.append(TASAINT);
        b.append(SEPARADOR_CSV);

        b.append(SDOPROMDOCE);
        b.append(SEPARADOR_CSV);

        b.append(INDESTA);
        b.append(SEPARADOR_CSV);

        b.append(CENTRO_CONTAB);
        b.append(SEPARADOR_CSV);
        
        b.append(FECHA_ALTA);
        b.append(SEPARADOR_CSV);

        b.append(INDICADOR);
        b.append(SEPARADOR_CSV);

        b.append(DIVISA);
        b.append(SEPARADOR_CSV);

        b.append(BALANCE);
        b.append(SEPARADOR_CSV);

        b.append(EMPLEADO);
        b.append(SEPARADOR_CSV);
        
        b.append(CODEMPLEADO);
                
        b.append(SALTO_LINEA);
        
    }


    
    /**
     * Genera los registros del Reporte
     *
     * @param b <code>StringBuffer</code>
     * 		que será poblado con los registros.

     * @param resultadoBusqueda <code>List</code>
     * 		de objetos <code>String</code>
     * 		que son procesados.
     */
    public void generarRegistrosArchivoContratos(StringBuffer b, List resultadoBusqueda) {

        for (Iterator iter = resultadoBusqueda.iterator(); iter.hasNext();) {
            
        	Object[] tupla = (Object[]) iter.next();
        	
    if (tupla[0] != null)
                b.append((String)tupla[0].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[1] != null)
                b.append((String)tupla[1].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[2] != null)
                b.append((String)tupla[2].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[3] != null)
                b.append((String)tupla[3].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[4] != null)
                b.append((String)tupla[4].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[5] != null)
                b.append((String)tupla[5].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[6] != null)
                b.append((String)tupla[6].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[7] != null)
                b.append((String)tupla[7].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[8] != null)
                b.append((String)tupla[8].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[9] != null)
                b.append((String)tupla[9].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[10] != null)
                b.append((String)tupla[10].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[11] != null)
                b.append((String)tupla[11].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[12] != null)
                b.append((String)tupla[12].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[13] != null)
                b.append((String)tupla[13].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[14] != null)
                b.append((String)tupla[14].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[15] != null)
                b.append((String)tupla[15].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[16] != null)
                b.append((String)tupla[16].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[17] != null)
                b.append((String)tupla[17].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[18] != null)
                b.append((String)tupla[18].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[19] != null)
                b.append((String)tupla[19].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[20] != null)
                b.append((String)tupla[20].toString().replaceAll(","," "));
                //.append(SEPARADOR_CSV);

    if (tupla[21] != null)
                b.append((String)tupla[21].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[22] != null)
                b.append((String)tupla[22].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[23] != null)
                b.append((String)tupla[23].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[24] != null)
                b.append((String)tupla[24].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[25] != null)
                b.append((String)tupla[25].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[26] != null)
                b.append((String)tupla[26].toString().replaceAll(","," "));

            b.append(SALTO_LINEA);
        }
    }
    
    /**
     * Genera el encabezado del Reporte del Layout de transacciones
     *
     * @param b <code>StringBuffer</code>
     * 		que será poblado con el encabezado.
     */
    public void generarEncabezadoArchivoReporteTransacciones(StringBuffer b) {
    	b.append(ACCOUNT_KEY);
    	b.append(SEPARADOR_CSV);

    	b.append(ACCT_OPEN_DATE);
    	b.append(SEPARADOR_CSV);

    	b.append(ROW_UPDATE_DATE);
    	b.append(SEPARADOR_CSV);

    	b.append(TRANS_TYPE_CD);
    	b.append(SEPARADOR_CSV);

    	b.append(VALOR);
    	b.append(SEPARADOR_CSV);

    	b.append(TRANSACTION_TYPE_CD);
    	b.append(SEPARADOR_CSV);

    	b.append(TRANSACTION_CODE_CD);
    	b.append(SEPARADOR_CSV);

    	b.append(TRANS_TYPE_CATEGORY);
    	b.append(SEPARADOR_CSV);

    	b.append(TRANS_TYPE_CD);
    	b.append(SEPARADOR_CSV);

    	b.append(ACCOUNT_KEY);
    	b.append(SEPARADOR_CSV);

    	b.append(ACCT_CURRENCY_AMOUNT);
    	b.append(SEPARADOR_CSV);

    	b.append(ID_DIVISA);
    	b.append(SEPARADOR_CSV);

    	b.append(MONTO_MONEDA_NACIONAL);
    	b.append(SEPARADOR_CSV);

    	b.append(PAYEE_ID_NO);
    	b.append(SEPARADOR_CSV);

    	b.append(TRANSACTION_KEY);
    	b.append(SEPARADOR_CSV);

    	b.append(OPP_ACCOUNT_KEY);
    	b.append(SEPARADOR_CSV);

    	b.append(OPP_COUNTRY_CD);
    	b.append(SEPARADOR_CSV);

    	b.append(OPP_ORG_KEY);
    	b.append(SEPARADOR_CSV);

    	b.append(COD1);
    	b.append(SEPARADOR_CSV);

    	b.append(COD2);
    	
        b.append(SALTO_LINEA);

    }


    
    /**
     * Genera los registros del Reporte
     *
     * @param b <code>StringBuffer</code>
     * 		que será poblado con los registros.

     * @param resultadoBusqueda <code>List</code>
     * 		de objetos <code>String</code>
     * 		que son procesados.
     */
    public void generarRegistrosArchivoTransacciones(StringBuffer b, List resultadoBusqueda) {

        for (Iterator iter = resultadoBusqueda.iterator(); iter.hasNext();) {
            
        	Object[] tupla = (Object[]) iter.next();
        	
    if (tupla[0] != null)
                b.append((String)tupla[0].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[1] != null)
                b.append((String)tupla[1].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[2] != null)
                b.append((String)tupla[2].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[3] != null)
                b.append((String)tupla[3].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[4] != null)
                b.append((String)tupla[4].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[5] != null)
                b.append((String)tupla[5].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[6] != null)
                b.append((String)tupla[6].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[7] != null)
                b.append((String)tupla[7].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[8] != null)
                b.append("SICA");//(String)tupla[8].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[9] != null)
                b.append((String)tupla[9].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[10] != null)
                b.append((String)tupla[10].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[11] != null)
                b.append((String)tupla[11].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[12] != null)
                b.append((String)tupla[12].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[13] != null)
                b.append((String)tupla[13].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[14] != null)
                b.append((String)tupla[14].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[15] != null)
                b.append((String)tupla[15].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[16] != null)
                b.append((String)tupla[16].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[17] != null)
                b.append((String)tupla[17].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[18] != null)
                b.append((String)tupla[18].toString().replaceAll(","," "));
                //b.append(SEPARADOR_CSV);

    if (tupla[19] != null)
                b.append((String)tupla[19].toString().replaceAll(","," "));

                b.append(SALTO_LINEA);
        }
    }

    public void obtenerLayoutClientes(Date fechaProceso){
        //Se obtienen la información de los clientes
        List clientes = new ArrayList();
        _logger.info("entro a obtenerLayoutClientes con fechaProceso: " + fechaProceso);

        try {
        	Date inicioDia  = DateUtils.inicioDia(fechaProceso); 
        	//java.sql.Date fechaProceso1 = new java.sql.Date(fechaProceso.getTime());
        	java.sql.Date inicioDiaSql =  new java.sql.Date(inicioDia.getTime());
        	clientes = getReportesServiceData().findAllClientes(inicioDiaSql);
        	int numRegistros = clientes.size();
        	_logger.info("entro a obtenerLayoutClientes con inicioDiaSql: " + inicioDiaSql);
        	_logger.info("clientes.size() -------> "  + clientes.size());
        	for (int i = 0; i < clientes.size(); i++) {
				//_logger.info(clientes.get(i));
				_logger.info(ToStringBuilder.reflectionToString(clientes.get(i)));
			}
        	
			StringBuffer buffer = new StringBuffer();
			
			//generarEncabezadoArchivoReporteClientes(buffer);
			generarRegistrosArchivoClientes(buffer, clientes);
            _logger.debug("Reporte armado: "+ buffer.toString());
            File archivo = escribeArchivo(buffer.toString(),PREFIJO_NOMBRE_ARCHIVO_CLIENTES_AR);
            
            File archivo_renombrado = new File(rutaLayoutClientes + getNombreArchivoCorte(PREFIJO_NOMBRE_ARCHIVO_CLIENTES)); 
            archivo.createNewFile();
            //archivo_prueba.createNewFile();
            _logger.debug("ruta del archivo que renombrará " + archivo_renombrado.getCanonicalPath());
            _logger.debug("ruta archivo que será renombrado " + archivo.getCanonicalPath());
            boolean a = archivo.renameTo(archivo_renombrado);
            _logger.debug("renombro archivo --> "+a);
            
            obtenerCifrasControlClientes(numRegistros);
            
            //transferir("SICA",archivo,numRegistros,PREFIJO_NOMBRE_ARCHIVO_CLIENTES); ya no se transferirá vía FTP
            enviarCorreo(getNombreArchivoCorte(PREFIJO_NOMBRE_ARCHIVO_CLIENTES),numRegistros);
        }
        catch (SicaException e1) {
            if (_logger.isDebugEnabled()) {
                _logger.debug(e1);
            }
        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
    
    public void obtenerCifrasControlClientes(int numRegistros){
    	rutaLayoutClientes = getSicaServiceData().findParametro(ParametroSica.ARMS_RUTA_LAYOUT_CLIENTES).getValor();
    	File archivo = new File(rutaLayoutClientes + NOMBRE_ARCHIVO_CIFRAS_CTRL_CLIENTES + ".DAT");
    	BufferedWriter bw;
    	String nombreArchivo;
        try {
        	_logger.debug("<--Obtiene Cifras de control para Clientes --> ");
			bw = new BufferedWriter(new FileWriter(archivo));
			
		    String DATE_FORMAT = "yyyyMMdd";
		    SimpleDateFormat sdf =
		          new SimpleDateFormat(DATE_FORMAT);
		    Calendar c1 = Calendar.getInstance(); // today
		    _logger.debug("Fecha del archivo de cifras de control " + sdf.format(c1.getTime()));
		    
		    nombreArchivo = NOMBRE_ARCHIVO_CIFRAS_CTRL_CLIENTES + sdf.format(c1.getTime()) + ".DAT";
		    
	        bw.write(sdf.format(c1.getTime()) + "|"+ nombreArchivo + "|" + numRegistros);
	        bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
    

    public void obtenerLayoutContratos(Date fechaProceso){
        //Se obtienen la información de los contratos
        List contratos = new ArrayList();
        _logger.info("entro a obtenerLayoutContratos con fechaProceso: " + fechaProceso);

        try {
        	Date inicioDia  = DateUtils.inicioDia(fechaProceso); 
        	//java.sql.Date fechaProceso1 = new java.sql.Date(fechaProceso.getTime());
        	java.sql.Date inicioDiaSql =  new java.sql.Date(inicioDia.getTime());
        	contratos = getReportesServiceData().findAllContratos(inicioDiaSql);
        	int numRegistros = contratos.size();
        	_logger.info("entro a obtenerLayoutContratos con inicioDiaSql: " + inicioDiaSql);
        	_logger.info("contratos.size() -------> "  + contratos.size());
        	for (int i = 0; i < contratos.size(); i++) {
				//_logger.info(contratos.get(i));
				_logger.info(ToStringBuilder.reflectionToString(contratos.get(i)));
			}
        	
			StringBuffer buffer = new StringBuffer();
			
			//generarEncabezadoArchivoReporteContratos(buffer);
			generarRegistrosArchivoContratos(buffer, contratos);
            _logger.debug("Reporte armado: "+ buffer.toString());
            File archivo = escribeArchivo(buffer.toString(),PREFIJO_NOMBRE_ARCHIVO_CONTRATOS_AR);
            File archivo_renombrado_contratos = new File(rutaLayoutContratos + getNombreArchivoCorte(PREFIJO_NOMBRE_ARCHIVO_CONTRATOS)); 
            archivo.createNewFile();
            //archivo_prueba.createNewFile();
            _logger.debug("ruta del archivo que renombrará " + archivo_renombrado_contratos.getCanonicalPath());
            _logger.debug("ruta archivo que será renombrado " + archivo.getCanonicalPath());
            boolean a = archivo.renameTo(archivo_renombrado_contratos);
            _logger.debug("renombro archivo --> "+a);
            
            obtenerCifrasControlContratos(numRegistros);

            enviarCorreo(getNombreArchivoCorte(PREFIJO_NOMBRE_ARCHIVO_CONTRATOS),numRegistros);
        }
        catch (SicaException e1) {
            if (_logger.isDebugEnabled()) {
                _logger.debug(e1);
            }
        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
    
    public void obtenerCifrasControlContratos(int numRegistros){
    	rutaLayoutContratos = getSicaServiceData().findParametro(ParametroSica.ARMS_RUTA_LAYOUT_CONTRATOS).getValor();
    	File archivo = new File(rutaLayoutContratos + NOMBRE_ARCHIVO_CIFRAS_CTRL_CONTRATOS + ".DAT");
    	BufferedWriter bw;
    	String nombreArchivo;
        try {
        	_logger.debug("<--Obtiene Cifras de control para Contratos --> ");
			bw = new BufferedWriter(new FileWriter(archivo));
			
		    String DATE_FORMAT = "yyyyMMdd";
		    SimpleDateFormat sdf =
		          new SimpleDateFormat(DATE_FORMAT);
		    Calendar c1 = Calendar.getInstance(); // today
		    _logger.debug("Fecha del archivo de cifras de control " + sdf.format(c1.getTime()));
		    
		    nombreArchivo = NOMBRE_ARCHIVO_CIFRAS_CTRL_CONTRATOS + sdf.format(c1.getTime()) + ".DAT";
		    
	        bw.write(sdf.format(c1.getTime()) + "|"+ nombreArchivo + "|" + numRegistros);
	        bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
    
    public void obtenerLayoutTransacciones(Date fechaProceso){
        //Se obtienen la información de los contratos
        List transacciones = new ArrayList();
        _logger.info("entro a obtenerLayoutTransacciones con fechaProceso: " + fechaProceso);

        try {
        	Date inicioDia  = DateUtils.inicioDia(fechaProceso); 
        	java.sql.Date inicioDiaSql =  new java.sql.Date(inicioDia.getTime());
        	transacciones = getReportesServiceData().findAllTransacciones(inicioDiaSql);
        	int numRegistros = transacciones.size();
        	_logger.info("entro a obtenerLayoutTransacciones con inicioDiaSql: " + inicioDiaSql);
        	_logger.info("transacciones.size() -------> "  + transacciones.size());
        	for (int i = 0; i < transacciones.size(); i++) {
				//_logger.info(transacciones.get(i));
				_logger.info(ToStringBuilder.reflectionToString(transacciones.get(i)));
				
			}
        	
			StringBuffer buffer = new StringBuffer();
			
			//generarEncabezadoArchivoReporteTransacciones(buffer);
			generarRegistrosArchivoTransacciones(buffer, transacciones);
            _logger.debug("Reporte armado: "+ buffer.toString());
            File archivo = escribeArchivo(buffer.toString(),PREFIJO_NOMBRE_ARCHIVO_TRANSACCIONES_AR);
            //transferir("SICA",archivo,numRegistros,PREFIJO_NOMBRE_ARCHIVO_TRANSACCIONES);
            File archivo_renombrado_transacciones = new File(rutaLayoutTransacciones + getNombreArchivoCorte(PREFIJO_NOMBRE_ARCHIVO_TRANSACCIONES)); 
            archivo.createNewFile();
            //archivo_prueba.createNewFile();
            _logger.debug("ruta del archivo que renombrará " + archivo_renombrado_transacciones.getCanonicalPath());
            _logger.debug("ruta archivo que será renombrado " + archivo.getCanonicalPath());
            boolean a = archivo.renameTo(archivo_renombrado_transacciones);
            _logger.debug("renombro archivo --> "+a);
            enviarCorreo(getNombreArchivoCorte(PREFIJO_NOMBRE_ARCHIVO_TRANSACCIONES),numRegistros);
        }
        catch (SicaException e1) {
            if (_logger.isDebugEnabled()) {
                _logger.debug(e1);
            }
        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
    

    
//    public File escribeArchivo (String arreglo){
//    	File file = new File(getNombreArchivoCorte());
//    	_logger.debug("Escribiendo Layout de Clientes archivo...");
//    	BufferedWriter writer;
//		try {
//			file.createNewFile();
//			writer = new BufferedWriter(new FileWriter(file));
//			writer.write(arreglo);
//			writer.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//			throw new SicaException("Ocurrio un problema al obtener el archivo de hoy" +
//			new Date().toString());
//		}
//    	_logger.debug("Archivo creado"+ ToStringBuilder.reflectionToString(file));
//    	return file;
//    }
    
    public File escribeArchivo (String arreglo,String prefijo){
    	if (prefijo.equals(PREFIJO_NOMBRE_ARCHIVO_CLIENTES_AR)){
    	rutaLayoutClientes = getSicaServiceData().findParametro(ParametroSica.ARMS_RUTA_LAYOUT_CLIENTES).getValor();
    	File file = new File(rutaLayoutClientes + getNombreArchivoCorte(prefijo));
    	_logger.debug("Escribiendo Layout de Clientes archivo...");
    	//BufferedWriter writer;
    	FileOutputStream fosLayoutClientes = null;
		try {
			//file.createNewFile();
			//writer = new BufferedWriter(new FileWriter(file));
			//writer.write(arreglo);
			//writer.close();
	//jdch		
			//rutaLayoutClientes = getSicaServiceData().findParametro(ParametroSica.ARMS_RUTA_LAYOUT_CLIENTES).getValor();
			_logger.info("guardando archivo de Clientes en : " + rutaLayoutClientes);
			fosLayoutClientes = new FileOutputStream(rutaLayoutClientes + getNombreArchivoCorte(prefijo));
			fosLayoutClientes.write(arreglo.getBytes());
			fosLayoutClientes.flush();
    //jdch
 
		} catch (IOException e) {
			e.printStackTrace();
			_logger.debug(e.getMessage(), e);
			enviarCorreoError(getNombreArchivoCorte(prefijo),e);
			throw new SicaException("Ocurrio un problema al obtener el archivo Layout de hoy " +
			new Date().toString() );
		}
        finally {
            if (fosLayoutClientes != null) {
                try {
                	fosLayoutClientes.close();
                }
                catch (IOException e) {
                    _logger.debug(e.getMessage(), e);
                }
            }
        }
    	_logger.debug("Archivo creado"+ ToStringBuilder.reflectionToString(file));
    	return file;
    	}
    	
    	else if(prefijo.equals(PREFIJO_NOMBRE_ARCHIVO_CONTRATOS_AR)){
    		rutaLayoutContratos = getSicaServiceData().findParametro(ParametroSica.ARMS_RUTA_LAYOUT_CONTRATOS).getValor();
        	File file = new File(rutaLayoutContratos + getNombreArchivoCorte(prefijo));
        	_logger.debug("Escribiendo Layout de Contratos archivo...");
        	//BufferedWriter writer;
        	FileOutputStream fosLayoutContratos = null;
    		try {
    			//file.createNewFile();
    			//writer = new BufferedWriter(new FileWriter(file));
    			//writer.write(arreglo);
    			//writer.close();
    			//rutaLayoutContratos = getSicaServiceData().findParametro(ParametroSica.ARMS_RUTA_LAYOUT_CONTRATOS).getValor();
    			_logger.info("guardando archivo de Contratos en : " + rutaLayoutContratos);
    			fosLayoutContratos = new FileOutputStream(rutaLayoutContratos + getNombreArchivoCorte(prefijo));
    			fosLayoutContratos.write(arreglo.getBytes());
    			fosLayoutContratos.flush();
    			
    		} catch (IOException e) {
    			e.printStackTrace();
    			enviarCorreoError(getNombreArchivoCorte(prefijo),e);
    			throw new SicaException("Ocurrio un problema al obtener el archivo de hoy" +
    			new Date().toString());
    		}
    		
            finally {
                if (fosLayoutContratos != null) {
                    try {
                    	fosLayoutContratos.close();
                    }
                    catch (IOException e) {
                        _logger.debug(e.getMessage(), e);
                    }
                }
            }
        	_logger.debug("Archivo creado "+ ToStringBuilder.reflectionToString(file));
        	return file;
        	
    		
    	}
    	else{
    		rutaLayoutTransacciones = getSicaServiceData().findParametro(ParametroSica.ARMS_RUTA_LAYOUT_TRANSACCIONES).getValor();
        	File file = new File(rutaLayoutTransacciones + getNombreArchivoCorte(prefijo));
        	_logger.debug("Escribiendo Layout de Transacciones archivo...");
        	//BufferedWriter writer;
        	FileOutputStream fosLayoutTransacciones = null;
    		try {
    			//file.createNewFile();
    			//writer = new BufferedWriter(new FileWriter(file));
    			//writer.write(arreglo);
    			//writer.close();
    			_logger.info("guardando archivo de Transacciones en : " + rutaLayoutTransacciones);
    			fosLayoutTransacciones = new FileOutputStream(rutaLayoutTransacciones + getNombreArchivoCorte(prefijo));
    			fosLayoutTransacciones.write(arreglo.getBytes());
    			fosLayoutTransacciones.flush();
    			
    		} catch (IOException e) {
    			e.printStackTrace();
    			enviarCorreoError(getNombreArchivoCorte(prefijo),e);
    			throw new SicaException("Ocurrio un problema al obtener el archivo de hoy " +
    			new Date().toString());
    		}
    		
            finally {
                if (fosLayoutTransacciones != null) {
                    try {
                    	fosLayoutTransacciones.close();
                    }
                    catch (IOException e) {
                        _logger.debug(e.getMessage(), e);
                    }
                }
            }
        	_logger.debug("Archivo creado"+ ToStringBuilder.reflectionToString(file));
        	return file;
    	}
    }
    
    /**
     *@see com.ixe.ods.sica.util.sicareportesLayout.service.LayoutClientes#getNombreArchivoCorte().
     */
    public String getNombreArchivoCorte(String prefijo) {
        String nombreArchivo = null;
        String fechaHoy = null;
        SimpleDateFormat dateFormat = null;
        Date fechaProceso = new Date();

        dateFormat = new SimpleDateFormat("ddMMyyyy");
        
        if (prefijo.equals(PREFIJO_NOMBRE_ARCHIVO_CLIENTES))
        nombreArchivo = PREFIJO_NOMBRE_ARCHIVO_CLIENTES + dateFormat.format(fechaProceso) + EXTENSION_ARCHIVO;

        if (prefijo.equals(PREFIJO_NOMBRE_ARCHIVO_CLIENTES_AR))
        nombreArchivo = PREFIJO_NOMBRE_ARCHIVO_CLIENTES_AR + dateFormat.format(fechaProceso) + EXTENSION_ARCHIVO;
        
        if (prefijo.equals(PREFIJO_NOMBRE_ARCHIVO_CONTRATOS))
        nombreArchivo = PREFIJO_NOMBRE_ARCHIVO_CONTRATOS + dateFormat.format(fechaProceso) + EXTENSION_ARCHIVO;
        
        if (prefijo.equals(PREFIJO_NOMBRE_ARCHIVO_CONTRATOS_AR))
        nombreArchivo = PREFIJO_NOMBRE_ARCHIVO_CONTRATOS_AR + dateFormat.format(fechaProceso) + EXTENSION_ARCHIVO;
        
        if (prefijo.equals(PREFIJO_NOMBRE_ARCHIVO_TRANSACCIONES))
        nombreArchivo = PREFIJO_NOMBRE_ARCHIVO_TRANSACCIONES + dateFormat.format(fechaProceso) + EXTENSION_ARCHIVO;
        
        if (prefijo.equals(PREFIJO_NOMBRE_ARCHIVO_TRANSACCIONES_AR))
        nombreArchivo = PREFIJO_NOMBRE_ARCHIVO_TRANSACCIONES_AR
        + dateFormat.format(fechaProceso) + EXTENSION_ARCHIVO;

        return nombreArchivo;
    }
    
    /**
     * Este metodo se encarga de transferir el Layout hacia ARMS
     *
     * @param String usuario que realiza la transferencia del archivo txt
     * @param File archivo que contiene el layout correspondiente
     * @param int numRegistros que contiene el archivo a enviar
     *
     * @return boolean si la transferencia fue exitosa o no.
     *  
     */
    public boolean transferir(String usuario,File archivo, int numRegistros,String prefijo){
		boolean bTransferencia;
		FileInputStream inputStream;
		
		rutaServidorFTP = getSicaServiceData().findParametro(ParametroSica.FTP_ARMS_RUTA).getValor();
		ipServidor      = getSicaServiceData().findParametro(ParametroSica.FTP_ARMS_IP).getValor();

		try {
			inputStream     = new FileInputStream(archivo);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new SicaException("No se pudo crear el archivo del Layout de hoy: "+ new Date());
		}
		
		try{
			_logger.debug("Enviando archivo a RCM-ARMS .... "+ archivo.getName());	
			bTransferencia = enviaArchivo(inputStream,usuario,archivo.getName(),numRegistros,prefijo,false);
			if(!bTransferencia){
					throw new SicaException("Revisar parametros de conexión.");
			}
		} catch (SicaException ex) {
			_logger.debug(" Exception -> Reintentando el envio del layout de hoy: "+ new Date().toString());
			try {
				inputStream     = new FileInputStream(archivo);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			bTransferencia = enviaArchivo(inputStream,usuario,archivo.getName(),numRegistros,prefijo,true);
			if(!bTransferencia){
				_logger.debug("Ocurrio un error en el envío se genera correo informando de la excepcion "+ archivo.getName());
				
				GeneralMailSender gms = getGeneralMailSender();
	           
	            String parametroSica;
	            parametroSica = getSicaServiceData().findParametro(ParametroSica.EMAIL_ARMS).getValor();
	            
	            String correos = parametroSica.trim();
	            
	            String[] arrayCorreos = correos.split(",");
	            int n = 0;
	    		while (n<arrayCorreos.length ){
	    			_logger.info("correos del arreglo: " + arrayCorreos[n]);
	    			n++;
	    		}
	            

	            gms.enviarMail("ixecambios@ixe.com.mx", arrayCorreos, null,
	            		"ERROR en el envío del Layout hacia RCM-ARMS!!",
	            		"Ocurrió un error en el envío del Layout  hacia RCM-ARMS"+
	            		"\n=============================================================" +		
	                    "\n\nOcurrió una excepcion al tratar de enviar el archivo: " + archivo.getName() + 
	                    "\n\nEl archivo que se intentó enviar contiene "+ numRegistros + " registros."+
	                    "\n\nLa excepcion ocurrida es: "+ ex.getMessage() +
	                    "\n\n\nEste correo ha sido generado automáticamente por el sistema SICA. No responda ni envíe correo a esta dirección por favor.");

	        
			
				
				throw new SicaException(
					"Ocurrio un problema al al enviar el layout al servidor FTP RCM-ARMS!!!..."+ ex.getMessage());
			}else{
				_logger.debug("Archivo enviado a RCM-ARMS ...." + new Date().toString());
			}
			
		}

		return bTransferencia;
    }

    
	private boolean enviaArchivo(FileInputStream inputStream, String usuarioSICA, String nombreArchivo, int numRegistros,String prefijo,boolean reenvio)throws SicaException {
		boolean bTransferencia= false;
		ftp.setConnectTimeout(timeOut);
		try {
			ftp.connect(ipServidor);
		} catch (Exception e) {
			
			if (reenvio){
			e.printStackTrace();
			
			GeneralMailSender gms = getGeneralMailSender();
	           
            String parametroSica;
            
            parametroSica = getSicaServiceData().findParametro(ParametroSica.EMAIL_ARMS).getValor();
            
            String correos = parametroSica.trim();
            
            String[] arrayCorreos = correos.split(",");
            int n = 0;
    		while (n<arrayCorreos.length ){
    			_logger.info("correos del arreglo: " + arrayCorreos[n]);
    			n++;
    		}
			gms.enviarMail("ixecambios@ixe.com.mx", arrayCorreos, null,
            		"ERROR en el envío del Layout hacia RCM-ARMS!!",
            		"Ocurrió un error en el envío del Layout hacia RCM-ARMS --> "+ nombreArchivo +
            		"\n=============================================================" +		
                    "\n\nOcurrió una excepcion al tratar intentar conectar con el servidor: " + ipServidor + 
                    "\n\nLa excepcion ocurrida es: "+ e.getMessage() +
                    "\n\n\nEste correo ha sido generado automáticamente por el sistema SICA. No responda ni envíe correo a esta dirección por favor.");
			//throw new SicaException("No se pudo conectar al Servidor FTP RCM-ARMS:  "+ ipServidor + " msg: "+ e.getMessage());
			}
		}
		try {
			
			usuario = getSicaServiceData().findParametro(ParametroSica.FTP_ARMS_USUARIO).getValor();
			contrasena      = getSicaServiceData().findParametro(ParametroSica.FTP_ARMS_CONTRASENA).getValor();
			
			ftp.login(usuario, contrasena);
			ftp.enterLocalPassiveMode();
			respuesta = ftp.getReplyCode();
			_logger.debug("Respuesta del servidor FTP: "+ ipServidor);
			if (FTPReply.isPositiveCompletion(respuesta)){
				_logger.debug("BEEP - Conectado satisfactoriamente!!!");
				ftp.setFileType(FTP.ASCII_FILE_TYPE);
				_logger.debug("Enviando archivo a RCM-ARMS...");
				ftp.changeWorkingDirectory(rutaServidorFTP);
				bTransferencia = ftp.storeFile(getNombreArchivoCorte(prefijo), inputStream);
				_logger.debug("Archivo enviado... " + bTransferencia);
				
				if (bTransferencia){
					
					GeneralMailSender gms = getGeneralMailSender();
		           
		            String parametroSica;
		            parametroSica = getSicaServiceData().findParametro(ParametroSica.EMAIL_ARMS).getValor();
		            
		            String correos = parametroSica.trim();
		            
		            String[] arrayCorreos = correos.split(",");
		            int n = 0;
		    		while (n<arrayCorreos.length ){
		    			
		    			_logger.info("correos del arreglo: " + arrayCorreos[n]);

		    			n++;

		    		}
		            

		            gms.enviarMail("ixecambios@ixe.com.mx", arrayCorreos, null,
		            		"Generación exitosa de Layout correspondiente que será enviado hacia RCM-ARMS!!",
		            		"================================================== Generación exitosa de Layout SICA hacia RCM-ARMS"+
		            		"\n==================================================" +		
		                    "\n\nSe generó exitosamente el archivo: " + nombreArchivo + 
		                    "\nEl archivo generado contiene  "+ numRegistros + " registros."+
		                    "\n\n\nEste correo ha sido generado automáticamente por el sistema SICA. No responda ni envíe correo a esta dirección por favor.");

		        
				}
			} else {
				_logger.debug("Ocurrio un problema al establecer la conexion con servidor FTP ARMS!!!");
				ftp.disconnect();
				_logger.debug("en caso de error se enviara correo con la excepcion ");
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new SicaException("Ocurrio un problema al transferir el archivo " + e.toString());
		}catch (Exception e) {
			e.printStackTrace();
			throw new SicaException("Ocurrio un problema al transferir el archivo ---->" + e.toString());
		}finally{
			if (ftp.isConnected()){
				try {
					ftp.logout();
					ftp.disconnect();
				} catch (IOException e) {
					e.printStackTrace();
					throw new SicaException("** Ocurrio un problema al transferir el archivo ** " + e.toString());
				}
			}
		}
		return bTransferencia;
	}
	

	public void enviarCorreo(String nombreArchivo,int numRegistros){
		
	GeneralMailSender gms = getGeneralMailSender();
   
    String parametroSica;
    parametroSica = getSicaServiceData().findParametro(ParametroSica.EMAIL_ARMS).getValor();
    
    String correos = parametroSica.trim();
    
    String[] arrayCorreos = correos.split(",");
    int n = 0;
	while (n<arrayCorreos.length ){
		
		_logger.info("correos del arreglo: " + arrayCorreos[n]);

		n++;

	}
    

    gms.enviarMail("ixecambios@ixe.com.mx", arrayCorreos, null,
    		"Generación exitosa de Layout correspondiente que será enviado hacia RCM-ARMS!!",
    		"================================================== Generación exitosa de Layout SICA que será enviado hacia RCM-ARMS"+
    		"\n==================================================" +		
            "\n\nSe generó exitosamente el archivo: " + nombreArchivo + 
            "\nEl archivo generado contiene  "+ numRegistros + " registros."+
            "\n\n\nEste correo ha sido generado automáticamente por el sistema SICA. No responda ni envíe correo a esta dirección por favor.");

	}
	
	public void enviarCorreoError(String nombreArchivo,Exception e){
	_logger.info("Ocurrió un error al generar archivo y guardarlo en la ruta correspondiente");
		
	GeneralMailSender gms = getGeneralMailSender();
   
    String parametroSica;
    parametroSica = getSicaServiceData().findParametro(ParametroSica.EMAIL_ARMS).getValor();
    
    String correos = parametroSica.trim();
    
    String[] arrayCorreos = correos.split(",");
    int n = 0;
	while (n<arrayCorreos.length ){
		
		_logger.info("correos del arreglo: " + arrayCorreos[n]);

		n++;

	}
    

	gms.enviarMail("ixecambios@ixe.com.mx", arrayCorreos, null,
    		"ERROR en la generación del Layout hacia RCM-ARMS!!",
    		"Ocurrió un error en la generación del Layout hacia RCM-ARMS --> "+ nombreArchivo +
    		"\n=============================================================" +		
            "\n\nOcurrió una excepcion al tratar de guardar el archivo en la ruta correspondiente " + 
            "\n\nLa excepcion ocurrida es: "+ e.getMessage() +
            "\n\n\nEste correo ha sido generado automáticamente por el sistema SICA. No responda ni envíe correo a esta dirección por favor.");

	}

	
	public SicaServiceData getSicaServiceData() {
		return sicaServiceData;
	}
	
	public void setSicaServiceData(SicaServiceData sicaServiceData) {
		this.sicaServiceData = sicaServiceData;
	}
	
	public ReportesServiceData getReportesServiceData() {
		return reportesServiceData;
	}
	
	public void setReportesServiceData(ReportesServiceData reportesServiceData) {
		this.reportesServiceData = reportesServiceData;
	}
	
	
	public FTPClient getFtp() {
		return ftp;
	}

	public void setFtp(FTPClient ftp) {
		this.ftp = ftp;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getContrasena() {
		return contrasena;
	}

	public void setContrasena(String contrasena) {
		this.contrasena = contrasena;
	}

	public String getServidorFTP() {
		return rutaServidorFTP;
	}

	public void setServidorFTP(String servidorFTP) {
		this.rutaServidorFTP = servidorFTP;
	}

	public String getRutaServidorFTP() {
		return rutaServidorFTP;
	}

	public void setRutaServidorFTP(String rutaServidorFTP) {
		this.rutaServidorFTP = rutaServidorFTP;
	}

	public int getRespuesta() {
		return respuesta;
	}

	public void setRespuesta(int respuesta) {
		this.respuesta = respuesta;
	}

	public int getTimeOut() {
		return timeOut;
	}

	public void setTimeOut(int timeOut) {
		this.timeOut = timeOut;
	}

	public String getIpServidor() {
		return ipServidor;
	}

	public void setIpServidor(String ipServidor) {
		this.ipServidor = ipServidor;
	}



	public GeneralMailSender getGeneralMailSender() {
		return generalMailSender;
	}



	public void setGeneralMailSender(GeneralMailSender generalMailSender) {
		this.generalMailSender = generalMailSender;
	}





}
