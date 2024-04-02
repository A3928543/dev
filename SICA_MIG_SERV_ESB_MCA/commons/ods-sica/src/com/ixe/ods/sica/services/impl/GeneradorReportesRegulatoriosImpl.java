package com.ixe.ods.sica.services.impl;

//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
//import org.apache.poi.hssf.usermodel.HSSFCellStyle;
//import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

//import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class GeneradorReportesRegulatoriosImpl 
{
    public static final int STRING = HSSFCell.CELL_TYPE_STRING;
    public static final int NUMBER = HSSFCell.CELL_TYPE_NUMERIC;
    
    public static final String EMPTY = "EMPTY";
    
    // PM: Persona Moral
    public static final int PM_CLAVE_BANXICO = 0;
    public static final int PM_NOMBRE_CONTRAPARTE = 1;
    public static final int PM_SOC_MERCANTIL = 2;
    public static final int PM_TIPO_IDENTIFICADOR = 3;
    public static final int PM_CLAVE_IDENTIFICADOR = 4;
    public static final int PM_ACTVIDAD_ECONOMICA = 5;
    public static final int PM_CLAVE_LEI = 6;
    public static final int PM_SECTOR_INDUSTRIAL = 7;
    public static final int PM_PAIS_CONTROL_CONTRAPARTE = 8;
    public static final int PM_PAIS_RESIDENCIA = 9;
    public static final int PM_CLAVE_LEI_MATRIZ_DIRECTA = 10;
    public static final int PM_CLAVE_LEI_MATRIZ_ULTIMA = 11;
    public static final int PM_FECHA = 12;
    
    // PR: Parte relacionada --> La relación entre el cliente y la institución
    public static final int PR_CLAVE_BANXICO = 0;
    public static final int PR_TIPO_RELACION = 1;
    public static final int PR_EN_GRUPO_FINANCIERO = 2;
    public static final int PR_EVENTO_RELACION = 3;
    public static final int PR_FECHA_RELACION = 4;
    
    public static final int REPORTE_PERSONA_MORAL = 1;
    public static final int REPORTE_PARTE_RELACIONADA = 2;
    
    public static final int tiposDeDatos_PM[] = new int[]{STRING, STRING, NUMBER, NUMBER, STRING, NUMBER, STRING, NUMBER, STRING, STRING, STRING, STRING, STRING};
    public static final int tiposDeDatos_PR[] = new int[]{STRING, NUMBER, NUMBER, NUMBER, STRING};
	
	/**
	 * Escribe en el OutputStream especificado el contenido del archivo excel.
	 * @param os El OutputStream donde se escribir&aacute;n los bytes del excel.
	 * @param datosReporte StringBuffer que contiene la información regulatoria de los clientes, 
	 *        cada registro va separado por un '|' y cada campo del registro va separado por ';'
	 * @param tipoReporte Constante que indica el tipo de reporte a crear       
	 */
    public void escribirXls(OutputStream os, StringBuffer datosReporte, int tipoReporte) throws IOException
    {
    	HSSFWorkbook wb = null;
        HSSFSheet sh = null;
        String[] registros = null, registro = null;
        int tiposDeDato[] = null;
        String nombreHoja = null;
        
        if(datosReporte != null && datosReporte.length() > 0)
        {
        	if(tipoReporte == REPORTE_PERSONA_MORAL)
        	{
        		tiposDeDato = tiposDeDatos_PM;
        		nombreHoja = "SECCION I Personas Morales";
        	}
        	else
        	{
        		tiposDeDato = tiposDeDatos_PR;
        		nombreHoja = "SECCION IV Parte Relacionada";
        	}
        	
        	wb = new HSSFWorkbook();
        	sh = wb.createSheet(nombreHoja);
        		
        	registros = datosReporte.toString().split("\\|");
        	for(int indiceFila = 0; indiceFila < registros.length; indiceFila++)
        	{
        		registro = registros[indiceFila].split(";");
        		crearFila(indiceFila, sh, registro, tiposDeDato);
        	}
        	
        	wb.write(os);
        }
    }
    
    private void crearFila(int indice, HSSFSheet sh, String registro[], int tiposDeDato[])
    {
    	HSSFRow fila = sh.createRow(indice);
    	HSSFCell celda = null;
    	
    	for(int indiceColumna = 0; indiceColumna < registro.length; indiceColumna++)
    	{
    		celda = fila.createCell((short)indiceColumna);
    		
    		if(EMPTY.equals(registro[indiceColumna]))
    			celda.setCellValue("");
    		else
    		{
    			if(NUMBER == tiposDeDato[indiceColumna])
    			{
    				try
    				{
    					celda.setCellValue((int)Double.parseDouble(registro[indiceColumna]));
    					celda.setCellType(tiposDeDato[indiceColumna]);
    				}
    				catch(Exception e)
    				{
    					celda.setCellValue(registro[indiceColumna]);
    					celda.setCellType(tiposDeDato[indiceColumna]);
    				}
    			}
    			else
    			{
    				if(indiceColumna == 0) // clave Banxico
    				{
    					try
        				{
    						System.out.println("Clave Banxico: " + registro[indiceColumna]);
    						celda.setCellValue((int)Double.parseDouble(registro[indiceColumna]));
    						celda.setCellType(NUMBER);
        				}
    					catch(Exception e)
    					{
    						System.out.println("Clave Banxico(no se pudo castear): " + registro[indiceColumna]);
    						celda.setCellValue(registro[indiceColumna]);
    					}
    				}
    				else
    				{
    					celda.setCellValue(registro[indiceColumna]);
    					celda.setCellType(tiposDeDato[indiceColumna]);
    				}
    			}
    		}
    	}
    }
    
    /**
     * Escribe en el OutputStream especificado el contenido del archivo CSV.
     * @param os El OutputStream donde se escribir&aacute;n los bytes del archivo separado por comas (CSV).
     * @param datosReporte
     * @throws IOException
     */
    public void excribirCSV(OutputStream os, StringBuffer datosReporte) throws IOException
    {
    	String[] registros = null;
    	String registro = null;
    	
    	if(datosReporte != null && datosReporte.length() > 0)
    	{
    		registros = datosReporte.toString().split("\\|");
    		for(int indiceFila = 0; indiceFila < registros.length; indiceFila++)
    		{
    			registro = registros[indiceFila].replaceAll(EMPTY, "").replaceAll(";", ",");
    			os.write(registro.getBytes());
    			if(indiceFila < (registros.length - 1))
    				os.write("\n".getBytes());
    		}
    	}
    }
}
