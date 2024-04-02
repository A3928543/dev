package com.ixe.ods.sica.sicamurex.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;

import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.model.Corte;
import com.ixe.ods.sica.model.CorteDetalle;
import com.ixe.ods.sica.model.ParametroSica;
import com.ixe.ods.sica.sdo.CorteMurexServiceData;
import com.ixe.ods.sica.sdo.SicaServiceData;


/**
*
 *@author HMASG771
 *@version $Revision: 1.1.2.4.12.1.10.2 $ $Date: 2016/06/13 17:34:00 $
 *   Clase que permite realizar la generación y transferencia del archivo
 *   con los acumualdos de compra/venta de Divisas
 */
public class FTPServiceImpl implements FTPService {
    
	/** El bean corteMurexServiceData* */
    private CorteMurexServiceData corteMurexServiceData;
	private SicaServiceData sicaServiceData;
    private FTPClient ftp = new FTPClient();
	private String usuario;
	private String contrasena;
	private String rutaServidorFTP;
	private String ipServidor;
	private int    respuesta;
	private int    timeOut;

    /** El logger de la clase * */
    private Logger _logger = Logger.getLogger(FTPServiceImpl.class);

    /**
     * Este metodo se encarga de armar el corte y enviarlo a MUREX
     *
     * @param String usuario que realiza la transferencia del archivo csv
     *
     * @return boolean si la transferencia fue exitosa o no.
     *  
     */
    public boolean transferir(String usuario){
		boolean bTransferencia;
		FileInputStream inputStream = null;
		rutaServidorFTP = getSicaServiceData().findParametro(ParametroSica.FTP_MUREX_RUTA).getValor();
		ipServidor      = getSicaServiceData().findParametro(ParametroSica.FTP_MUREX_IP).getValor();
		Corte corteHoy  = getCorteMurexServiceData().findCorteByFechaHoy();
		File archivo    = escribeArchivo(getRegistrosArchivoCorte(corteHoy));
		try 
		{
			inputStream     = new FileInputStream(archivo);
		} 

		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
			throw new SicaException("No se pudo crear el archivo de corte de hoy: "+ new Date());
		}
		
		finally
		{
			if(inputStream != null)
			{
				safeClose(inputStream);
			}
		}


		
		try{
			_logger.debug("Enviando archivo a MUREX .... "+ archivo.getName());	
			bTransferencia = enviaArchivo(inputStream,corteHoy,usuario);
			if(!bTransferencia){
						getCorteMurexServiceData().actualizarEstatusCorte(corteHoy.getIdCorte(),usuario,
						CorteMurexServiceData.ERROR_TRANSFERENCIA,
						CorteMurexServiceData.ERROR_TRANSFERENCIA + " " + 
						corteHoy.getIdCorte());
					throw new SicaException("Ocurrio un problema al enviar el corte al servidor FTP MUREX!!!...");
			}
		} catch (SicaException ex) {
			_logger.debug(" Exception -> Reintentando el envio del corte de hoy: "+ new Date().toString());
			bTransferencia = enviaArchivo(inputStream,corteHoy,usuario);
			if(!bTransferencia){
						getCorteMurexServiceData().actualizarEstatusCorte(corteHoy.getIdCorte(),usuario,
						CorteMurexServiceData.ERROR_TRANSFERENCIA,
						CorteMurexServiceData.ERROR_TRANSFERENCIA + " " + 
						corteHoy.getIdCorte());
				throw new SicaException(
					"Ocurrio un problema al al enviar el corte al servidor FTP MUREX!!!...");
			}else{
				_logger.debug("Archivo enviado a MUREX" + new Date().toString());
			}
			
		}

		return bTransferencia;
    }
    
    public static void safeClose(FileInputStream fis)
    {
    	if (fis != null) 
    	{
	    	try 
	    	{
	    		fis.close();
	    	} 
	    	catch (IOException e) 
	    	{
	    		System.out.println(e);

	    	}
    	}
    }

    public static void safeClose(BufferedWriter bw)
    {
    	if (bw != null) 
    	{
	    	try 
	    	{
	    		bw.close();
	    	} 
	    	catch (IOException e) 
	    	{
	    		System.out.println(e);

	    	}
    	}
    }
	private boolean enviaArchivo(FileInputStream inputStream, Corte corteHoy, String usuarioSICA){
		boolean bTransferencia= true;
//		ftp.setConnectTimeout(timeOut);
//		try {
//			ftp.connect(ipServidor);
//		} catch (Exception e) {
//			e.printStackTrace();
//			getCorteMurexServiceData().actualizarEstatusCorte(corteHoy.getIdCorte(),
//					usuarioSICA,
//					CorteMurexServiceData.ERROR_TRANSFERENCIA,
//					CorteMurexServiceData.ERROR_TRANSFERENCIA + " " + 
//					corteHoy.getIdCorte());
//			throw new SicaException("No se pudo conectar al Servidor FTP MUREX");
//		}
//		try {
//			ftp.login(usuario, contrasena);
//			ftp.enterLocalPassiveMode();
//			respuesta = ftp.getReplyCode();
//			_logger.debug("Respuesta del servidor FTP: "+ ipServidor);
//			if (FTPReply.isPositiveCompletion(respuesta)){
//				_logger.debug("BEEP - Conectado satisfactoriamente!!!");
//				ftp.setFileType(FTP.ASCII_FILE_TYPE);
//				_logger.debug("Enviando archivo a MUREX...");
//				ftp.changeWorkingDirectory(rutaServidorFTP);
//				bTransferencia = ftp.storeFile(getNombreArchivoCorte(), inputStream);
//				_logger.debug("Archivo enviado..." + bTransferencia);
				if (bTransferencia){
				getCorteMurexServiceData().actualizarEstatusCorte(corteHoy.getIdCorte(),
																	usuarioSICA,
																	CorteMurexServiceData.ENVIADO_MUREX,
																	CorteMurexServiceData.ENVIADO_MUREX + " " + 
																	corteHoy.getIdCorte());
				_logger.debug("Bitacora actualizada");
				}
//			} //else {
//				_logger.debug("Ocurrio un problema al establecer la conexion con servidor FTP MUREX!!!");
//				
//				getCorteMurexServiceData().actualizarEstatusCorte(corteHoy.getIdCorte(),usuarioSICA,
//						CorteMurexServiceData.ERROR_TRANSFERENCIA,
//						CorteMurexServiceData.ERROR_TRANSFERENCIA + " " + 
//						corteHoy.getIdCorte());
//				ftp.disconnect();
//				_logger.debug("Bitácora con error en transferencia actualizada");
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//			_logger.debug("Actualizando la bitácora con error en transferencia" + getCorteMurexServiceData().actualizarEstatusCorte(corteHoy.getIdCorte(),"",
//					CorteMurexServiceData.ERROR_TRANSFERENCIA,
//					CorteMurexServiceData.ERROR_TRANSFERENCIA + " " + 
//					corteHoy.getIdCorte()));
//			throw new SicaException("Ocurrio un problema al transferir el archivo ");
//		}finally{
//			if (ftp.isConnected()){
//				try {
//					ftp.logout();
//					ftp.disconnect();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//		}
		return bTransferencia;
	}

    /**
     *@see com.ixe.ods.sica.util.FTPService#getRegistrosArchivoCorte().
     */
    public String getRegistrosArchivoCorte(Corte corteHoy) {
        StringBuffer buffer = null;
        List detallesCorte = null;
        if (corteHoy != null) {
            //se obtienten los detalles del corte
            detallesCorte = getCorteMurexServiceData().findDetallesCortebyIdCorte(corteHoy.getIdCorte());

            if ((detallesCorte != null) && (detallesCorte.size() > 0)) {
                buffer = new StringBuffer();
                generarEncabezadoArchivoCorte(buffer);
                generarRegistrosArchivoCorte(buffer, detallesCorte);
                _logger.debug("Corte armado: "+ buffer.toString());
                return buffer.toString();
            }
            else {
                _logger.debug("No se encontraron registros para el corte: " +
                    corteHoy.getIdCorte());
                throw new SicaException("No se encontraron registros para el corte: " +
                    corteHoy.getIdCorte());
            }
        }else {
            _logger.debug("No se encontraron registros para el corte de hoy : "+ new Date());
            throw new SicaException("No hay un corte generado para el día de hoy" + new Date());
        }
    }

    
    private File escribeArchivo (String arreglo){
    	File file = new File(getNombreArchivoCorte());
    	_logger.debug("Escribiendo corte a archivo...");
    	BufferedWriter writer = null;
		try {
			file.createNewFile();
			writer = new BufferedWriter(new FileWriter(file));
			writer.write(arreglo);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new SicaException("Ocurrio un problema al obtener el archivo de corte de hoy" +
			new Date().toString());
		}
		finally {
			if (writer != null) {
			safeClose(writer);
			}
			}
		
    	_logger.debug("Archivo creado"+ ToStringBuilder.reflectionToString(file));
    	return file;
    }
    
    /**
     *@see com.ixe.ods.sica.util.FTPService#getNombreArchivoCorte().
     */
    public String getNombreArchivoCorte() {
        String nombreArchivo = null;
        String fechaHoy = null;
        SimpleDateFormat dateFormat = null;
        Corte corte = getCorteMurexServiceData().findCorteByFechaHoy();

        dateFormat = new SimpleDateFormat("yyyyMMdd");
        fechaHoy = dateFormat.format(corte.getFechaAlta());

        nombreArchivo = PREFIJO_NOMBRE_ARCHIVO_CORTE + fechaHoy + GUION + corte.getIdCorte() +
            EXTENSION_ARCHIVO_CORTE;

        return nombreArchivo;
    }

    /**
     * Genera el encabezado del archivo 
     * 	de corte.
     *
     * @param b <code>StringBuffer</code>
     * 		que será poblado con el encabezado.
     */
    private void generarEncabezadoArchivoCorte(StringBuffer b) {
        b.append(TYPE);
        b.append(SEPARADOR_CSV);

        b.append(ACTION);
        b.append(SEPARADOR_CSV);

        b.append(TRADE_GLOBAL_ID);
        b.append(SEPARADOR_CSV);

        b.append(TRADE_DATE);
        b.append(SEPARADOR_CSV);

        b.append(OUR_NAME);
        b.append(SEPARADOR_CSV);

        b.append(THEIR_NAME);
        b.append(SEPARADOR_CSV);

        b.append(OUR_PORTFOLIO);
        b.append(SEPARADOR_CSV);

        b.append(THEIR_PORTFOLIO);
        b.append(SEPARADOR_CSV);

        b.append(USER);
        b.append(SEPARADOR_CSV);

        b.append(GROUP);
        b.append(SEPARADOR_CSV);

        b.append(BUY_SELL);
        b.append(SEPARADOR_CSV);

        b.append(CONTRACT);
        b.append(SEPARADOR_CSV);

        b.append(DELIVERY_DATE_LABEL);
        b.append(SEPARADOR_CSV);

        b.append(DELIVERY_DATE);
        b.append(SEPARADOR_CSV);

        b.append(AMOUNT);
        b.append(SEPARADOR_CSV);

        b.append(ALTAMIRA_PRICE);
        b.append(SEPARADOR_CSV);

        b.append(FUNDING_COST);
        b.append(SEPARADOR_CSV);

        b.append(CCY);
        b.append(SEPARADOR_CSV);

        b.append(ACCOUNTING_SECTION_SOURCE);
        b.append(SEPARADOR_CSV);

        b.append(BACK_TO_BACK_PORTFOLIO_NAME);
        b.append(SEPARADOR_CSV);

        b.append(SOURCE_COMMENTS);

        b.append(SALTO_LINEA);
    }

    /**
     * Genera los registros del archivo de
     * 	corte 	
     *
     * @param b <code>StringBuffer</code>
     * 		que será poblado con los registros.

     * @param detallesCorte <code>List</code>
     * 		de objetos <code>CorteDetalle</code>
     * 		que son procesados.
     */
    private void generarRegistrosArchivoCorte(StringBuffer b, List detallesCorte) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

        for (Iterator iter = detallesCorte.iterator(); iter.hasNext();) {
            final CorteDetalle det = (CorteDetalle) iter.next();

            b.append(det.getType1());
            b.append(SEPARADOR_CSV);

            b.append(det.getAction());
            b.append(SEPARADOR_CSV);

            b.append(det.getTradeGlobalId());
            b.append(SEPARADOR_CSV);

            b.append(dateFormat.format(det.getTradeDate()));
            b.append(SEPARADOR_CSV);

            b.append(det.getOurName());
            b.append(SEPARADOR_CSV);

            b.append(det.getTheirName());
            b.append(SEPARADOR_CSV);

            b.append(det.getOurPortfolio());
            b.append(SEPARADOR_CSV);
            
            if (det.getTheirPortfolio() == null) {
            	 b.append(EMPTY_STR);
            }else{
            	 b.append(det.getTheirPortfolio());
            }
            b.append(SEPARADOR_CSV);
            
            b.append(det.getUser1());
            b.append(SEPARADOR_CSV);

            b.append(det.getGroup1());
            b.append(SEPARADOR_CSV);

            b.append(det.getBuySell());
            b.append(SEPARADOR_CSV);

            b.append(det.getContract());
            b.append(SEPARADOR_CSV);

            if (det.getDeliveryDateLabel() != null) {
                b.append(dateFormat.format(det.getDeliveryDateLabel()));
            }
            else {
                b.append(EMPTY_STR);
            }

            b.append(SEPARADOR_CSV);

            b.append(dateFormat.format(det.getDeliveryDate()));
            b.append(SEPARADOR_CSV);

            b.append(det.getAmount());
            b.append(SEPARADOR_CSV);

            b.append(det.getPrice());
            b.append(SEPARADOR_CSV);

            b.append(det.getFundingCost());
            b.append(SEPARADOR_CSV);

            b.append(det.getCcy());
            b.append(SEPARADOR_CSV);

            b.append(det.getAcountingSectionSource());
            b.append(SEPARADOR_CSV);

            b.append(det.getBackToBackPortfolioName());
            b.append(SEPARADOR_CSV);

            b.append(det.getSourceComments());

            b.append(SALTO_LINEA);
        }
    }

    /**
     * Getter bean corteMurexServiceData
     *
     * @return la referencia al bean
     * 		corteMurexServiceData.
     */
    public CorteMurexServiceData getCorteMurexServiceData() {
        return corteMurexServiceData;
    }

    /**
     * Setter bean  corteMurexServiceData
     *
     * @param corteMurexServiceData con
     * 		el valor a asignar.
     */
    public void setCorteMurexServiceData(CorteMurexServiceData corteMurexServiceData) {
        this.corteMurexServiceData = corteMurexServiceData;
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

	public SicaServiceData getSicaServiceData() {
		return sicaServiceData;
	}

	public void setSicaServiceData(SicaServiceData sicaServiceData) {
		this.sicaServiceData = sicaServiceData;
	}

	public String getIpServidor() {
		return ipServidor;
	}

	public void setIpServidor(String ipServidor) {
		this.ipServidor = ipServidor;
	}
}
