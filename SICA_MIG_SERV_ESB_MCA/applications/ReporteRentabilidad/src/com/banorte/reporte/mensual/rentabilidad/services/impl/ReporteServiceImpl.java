package com.banorte.reporte.mensual.rentabilidad.services.impl;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;

import com.banorte.reporte.mensual.rentabilidad.dto.DatosTransferencia;
import com.banorte.reporte.mensual.rentabilidad.dto.DatosTransferenciaRecibida;
import com.banorte.reporte.mensual.rentabilidad.services.IReporteService;


public class ReporteServiceImpl implements IReporteService
{

	private static final Logger LOG = Logger.getLogger(ReporteServiceImpl.class);
	
	public ByteArrayOutputStream crearReporteExcel(List<DatosTransferenciaRecibida> transferencias)
	{
		Workbook wb = new HSSFWorkbook();
		CreationHelper creationHelper = wb.getCreationHelper();
        Sheet sheet = wb.createSheet("Transferencias");
        Row fila = null;
        CellStyle alineacionIzquierda = null;
        CellStyle alineacionDerecha = null;
        CellStyle alineacionCentro = null;
        int indice = 0;
        
        ByteArrayOutputStream bas = new ByteArrayOutputStream();
        DatosTransferenciaRecibida transferencia = null;
        
        Font arial = wb.createFont();
        arial.setFontHeightInPoints((short)8);
        arial.setFontName("Arial");
        arial.setColor(IndexedColors.BLACK.getIndex());
        
        alineacionIzquierda = wb.createCellStyle();
        alineacionIzquierda.setAlignment(XSSFCellStyle.ALIGN_LEFT);
        alineacionIzquierda.setFont(arial);
        
        alineacionDerecha = wb.createCellStyle();
        alineacionDerecha.setAlignment(XSSFCellStyle.ALIGN_RIGHT);
        alineacionDerecha.setFont(arial);
        
        alineacionCentro = wb.createCellStyle();
        alineacionCentro.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        alineacionCentro.setFont(arial);
        
        for(indice = 0;indice < 1;indice ++)
        {
        	
        	fila = sheet.createRow(indice);
        	
          crearCelda(fila, alineacionCentro, creationHelper, "<fecha>", 0);
          crearCelda(fila, alineacionCentro, creationHelper, "<referencia>", 1);
          crearCelda(fila, alineacionCentro, creationHelper, "<deal>", 2);
          crearCelda(fila, alineacionCentro, creationHelper, "<folio_detalle>", 3);
          crearCelda(fila, alineacionCentro, creationHelper, "<nombre_corresponsal>", 4);
          crearCelda(fila, alineacionCentro, creationHelper, "<nombre_entidad_origen>", 5);
          crearCelda(fila, alineacionCentro, creationHelper, "<bic_aba>", 6);
          crearCelda(fila, alineacionCentro, creationHelper, "<datos_ordenante>", 7);
          crearCelda(fila, alineacionCentro, creationHelper, "<operacion_origen>", 8);
          crearCelda(fila, alineacionCentro, creationHelper, "<nombre_entidad_destino>", 9);
          crearCelda(fila, alineacionCentro, creationHelper, "<datos_destinatario>", 10);
          crearCelda(fila, alineacionCentro, creationHelper, "<operacion_destino>", 11);
          crearCelda(fila, alineacionCentro, creationHelper, "<instrumento_monetario_destin>", 12);
          crearCelda(fila, alineacionCentro, creationHelper, "<moneda_destino>", 13);
          crearCelda(fila, alineacionCentro, creationHelper, "<monto_instrumento_destino>", 14);
          crearCelda(fila, alineacionCentro, creationHelper, "<mensaje>", 15);
          crearCelda(fila, alineacionCentro, creationHelper, "<tipo_persona>", 16);
          crearCelda(fila, alineacionCentro, creationHelper, "<nombre>", 17);
          crearCelda(fila, alineacionCentro, creationHelper, "<apellido_paterno>", 18);
          crearCelda(fila, alineacionCentro, creationHelper, "<apellido_materno>", 19);
          crearCelda(fila, alineacionCentro, creationHelper, "<fecha_nacimiento>", 20);
          crearCelda(fila, alineacionCentro, creationHelper, "<curp>", 21);
          crearCelda(fila, alineacionCentro, creationHelper, "<rfc>", 22);
          crearCelda(fila, alineacionCentro, creationHelper, "<pais_nacimiento>", 23);
          crearCelda(fila, alineacionCentro, creationHelper, "<pais_nacionalidad>", 24);
          crearCelda(fila, alineacionCentro, creationHelper, "<actividad_economica>", 25);
          crearCelda(fila, alineacionCentro, creationHelper, "<razon_social>", 26);
          crearCelda(fila, alineacionCentro, creationHelper, "<fecha_constitucion>", 27);
          crearCelda(fila, alineacionCentro, creationHelper, "<giro_mercantil>", 28);
          crearCelda(fila, alineacionCentro, creationHelper, "<apoderado_legal>", 29);
          crearCelda(fila, alineacionCentro, creationHelper, "<domicilio_unificado>", 30);
          crearCelda(fila, alineacionCentro, creationHelper, "<ciudad_poblacion>", 31);
          crearCelda(fila, alineacionCentro, creationHelper, "<codigo_postal>", 32);
          crearCelda(fila, alineacionCentro, creationHelper, "<telefono>", 33);
          crearCelda(fila, alineacionCentro, creationHelper, "<numero_telefono>", 34);
          crearCelda(fila, alineacionCentro, creationHelper, "<clave_pais>", 35);
          crearCelda(fila, alineacionCentro, creationHelper, "<extension>", 36);
          crearCelda(fila, alineacionCentro, creationHelper, "<correo_electronico>", 37);
          crearCelda(fila, alineacionCentro, creationHelper, "<fiel>", 38);        	
        }

        
        
        for(indice = 0; indice < transferencias.size(); indice++)
        {
        	transferencia = transferencias.get(indice);
        	
        	fila = sheet.createRow(indice + 1);
        	
        	crearCelda(fila, alineacionDerecha, creationHelper, transferencia.getFecha(), 0);
        	crearCelda(fila, alineacionIzquierda, creationHelper, transferencia.getReferencia(), 1);
        	crearCelda(fila, alineacionIzquierda, creationHelper, transferencia.getDeal(), 2);
        	crearCelda(fila, alineacionIzquierda, creationHelper, transferencia.getFolioDetalle(), 3);
        	crearCelda(fila, alineacionDerecha, creationHelper, transferencia.getNombreCorresponsal(), 4);
        	crearCelda(fila, alineacionIzquierda, creationHelper, transferencia.getNombreEntidadOrigen(), 5);
        	crearCelda(fila, alineacionDerecha, creationHelper, transferencia.getBicAba(), 6);
        	crearCelda(fila, alineacionDerecha, creationHelper, transferencia.getDatosOrdenante(), 7);
        	crearCelda(fila, alineacionIzquierda, creationHelper, transferencia.getOperacionOrigen(), 8);
        	crearCelda(fila, alineacionIzquierda, creationHelper, transferencia.getNombreEntidadDestino(), 9);
        	crearCelda(fila, alineacionIzquierda, creationHelper, transferencia.getDatosDestinatario(), 10);
        	crearCelda(fila, alineacionIzquierda, creationHelper, transferencia.getOperacionDestino(), 11);
        	crearCelda(fila, alineacionIzquierda, creationHelper, transferencia.getInstrumentoMonetarioDestino(), 12);
        	crearCelda(fila, alineacionIzquierda, creationHelper, transferencia.getMonedaDestino(), 13);
        	crearCelda(fila, alineacionIzquierda, creationHelper, transferencia.getMontoInstrumentoDestino(), 14);
        	crearCelda(fila, alineacionDerecha, creationHelper, transferencia.getMensaje(), 15);
        	crearCelda(fila, alineacionDerecha, creationHelper, transferencia.getTipoPersona(), 16);
        	crearCelda(fila, alineacionCentro, creationHelper, transferencia.getNombre(), 17);
        	crearCelda(fila, alineacionIzquierda, creationHelper, transferencia.getApellidoPaterno(), 18);
        	crearCelda(fila, alineacionIzquierda, creationHelper, transferencia.getApellidoMaterno(), 19);
        	crearCelda(fila, alineacionIzquierda, creationHelper, transferencia.getFechaNacimiento(), 20);
        	crearCelda(fila, alineacionDerecha, creationHelper, transferencia.getCurp(), 21);
        	crearCelda(fila, alineacionIzquierda, creationHelper, transferencia.getRfc(), 22);
        	crearCelda(fila, alineacionIzquierda, creationHelper, transferencia.getPaisNacimiento(), 23);
        	crearCelda(fila, alineacionDerecha, creationHelper, transferencia.getPaisNacionalidad(), 24);
        	crearCelda(fila, alineacionDerecha, creationHelper, transferencia.getActividadEconomica(), 25);
        	crearCelda(fila, alineacionIzquierda, creationHelper, transferencia.getRazonSocial(), 26);
        	crearCelda(fila, alineacionIzquierda, creationHelper, transferencia.getFechaConstitucion(), 27);
        	crearCelda(fila, alineacionIzquierda, creationHelper, transferencia.getGiroMercantil(), 28);
        	crearCelda(fila, alineacionIzquierda, creationHelper, transferencia.getApoderadoLegal(), 29);
        	crearCelda(fila, alineacionIzquierda, creationHelper, transferencia.getDomicilioUnificado(), 30);
        	crearCelda(fila, alineacionIzquierda, creationHelper, transferencia.getCiudadPoblacion(), 31);
        	crearCelda(fila, alineacionDerecha, creationHelper, transferencia.getCodigoPostal(), 32);
        	crearCelda(fila, alineacionIzquierda, creationHelper, transferencia.getTelefono(), 33);
        	crearCelda(fila, alineacionIzquierda, creationHelper, transferencia.getNumeroTelefono(), 34);
        	crearCelda(fila, alineacionDerecha, creationHelper, transferencia.getClavePais(), 35);
        	crearCelda(fila, alineacionDerecha, creationHelper, transferencia.getExtension(), 36);
        	crearCelda(fila, alineacionIzquierda, creationHelper, transferencia.getCorreoElectronico(), 37);
        	crearCelda(fila, alineacionIzquierda, creationHelper, transferencia.getFiel(), 38);
        }
        
        try
        {
        	wb.write(bas);
        	bas.close();
        	
        	//FileOutputStream fileOut = new FileOutputStream("ReporteDiarioTransferencias.xlsx");
            //wb.write(fileOut);
            //fileOut.close();
            
            wb.close();
        }
        catch(FileNotFoundException e)
        {
        	LOG.error(e);
        }
        catch(IOException e)
        {
        	LOG.error(e);
        }
        
        return bas;
	}
	
	private Cell crearCelda(Row fila, CellStyle estilo, CreationHelper creationHelper, Object valor,
            int indiceCelda)
	{
		Cell celda = fila.createCell(indiceCelda);
		
		switch(indiceCelda)
		{	
//			case 0:
//				celda.setCellType(XSSFCell.CELL_TYPE_NUMERIC);
//				if(valor != null)
//					celda.setCellValue(((Integer)valor).floatValue());
//			break;
			
//			case 24:
//				celda.setCellType(XSSFCell.CELL_TYPE_NUMERIC);
//				if(valor != null)
//					celda.setCellValue(((BigDecimal)valor).floatValue());
//				estilo.setDataFormat(creationHelper.createDataFormat().getFormat("#,##0.00"));
//				celda.setCellStyle(estilo);
//			break;
			
			//case 1: case 15:
//			case 0: case 14:
//				celda.setCellType(XSSFCell.CELL_TYPE_STRING);
//				if(valor != null)
//					celda.setCellValue(((Date)valor));
//				//estilo.setDataFormat(creationHelper.createDataFormat().getFormat("dd/mm/yyyy")); // m/d/yy h:mm
//				estilo.setDataFormat(creationHelper.createDataFormat().getFormat("yyyymmdd")); // m/d/yy h:mm
//				celda.setCellStyle(estilo);
//			break;
			
			default:
				celda.setCellType(XSSFCell.CELL_TYPE_STRING);
				if(valor != null)
					celda.setCellValue((String)valor); 
				celda.setCellStyle(estilo);
		}
	
		return celda;
	}

}
