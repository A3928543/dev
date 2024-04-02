package com.banorte.contrapartes.services.impl;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
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
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.banorte.contrapartes.dto.OperacionDto;
import com.banorte.contrapartes.services.IReporteOperaciones;

public class ReporteOperacionesImpl implements IReporteOperaciones 
{
	private static final Logger LOG = Logger.getLogger(ReporteOperacionesImpl.class);
	
	public ByteArrayOutputStream crearReporteExcel(List<OperacionDto> operaciones)
	{
		Workbook wb = new XSSFWorkbook();
		CreationHelper creationHelper = wb.getCreationHelper();
        Sheet sheet = wb.createSheet("Operaciones");
        Row fila = sheet.createRow(0);
        Cell celda = null;
        CellStyle alineacionIzquierda = null;
        CellStyle alineacionDerecha = null;
        CellStyle alineacionCentro = null;
        
        ByteArrayOutputStream bas = new ByteArrayOutputStream();
        
        OperacionDto dto = null;
        
        int indice = 0;
        
        // Establecer estilo de encabezado
        Font arialBlack = wb.createFont();
        arialBlack.setFontHeightInPoints((short)9);
        arialBlack.setFontName("Arial");
        arialBlack.setColor(IndexedColors.BLACK.getIndex());
        arialBlack.setBold(true);
        
        alineacionCentro = wb.createCellStyle();
        alineacionCentro.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        alineacionCentro.setFont(arialBlack);
        
        for(indice = 0; indice < columnas.length; indice++)
        {
        	celda = fila.createCell(indice);
        	celda.setCellStyle(alineacionCentro);
        	celda.setCellType(XSSFCell.CELL_TYPE_STRING);
        	celda.setCellValue(encabezados.get(indice));
        	sheet.setColumnWidth(celda.getColumnIndex(), widthColumns[indice]);
        }
        
        //System.out.println("ancho de columna: " + sheet.getColumnWidth(fila.getLastCellNum()));
        
        // Establecer estilos con alineacion a la izquierda y derecha
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
        
        for(indice = 0; indice < operaciones.size(); indice++)
        {
        	dto = operaciones.get(indice);
        	
        	fila = sheet.createRow(indice + 1);
        	
        	crearCelda(fila, alineacionIzquierda, creationHelper, dto.getCliente(), _0_CLIENTE);
        	crearCelda(fila, alineacionIzquierda, creationHelper, dto.getContratoSica(), _1_CONTRATO_SICA);
        	crearCelda(fila, alineacionIzquierda, creationHelper, dto.getTipoPersona(), _2_TIPO_PERSONA);
        	crearCelda(fila, alineacionIzquierda, creationHelper, dto.getRfc(), _3_RFC);
        	crearCelda(fila, alineacionDerecha, creationHelper, dto.getNumeroDeal(), _4_NUMERO_DEAL);
        	crearCelda(fila, alineacionDerecha, creationHelper, new Integer(dto.getFolioDetalle()), _5_FOLIO_DETALLE);
        	crearCelda(fila, alineacionIzquierda, creationHelper, dto.getEntregarRecibir(), _6_ENTREGAR_RECIBIR);
        	crearCelda(fila, alineacionIzquierda, creationHelper, dto.getIdDivisa(), _7_ID_DIVISA);
        	crearCelda(fila, alineacionDerecha, creationHelper, dto.getMontoDivisaOriginal(), _8_MONTO_DIVISA_ORIGINAL);
        	crearCelda(fila, alineacionDerecha, creationHelper, dto.getMontoUsdDetalle(), _9_MONTO_USD_DETALLE);
        	crearCelda(fila, alineacionIzquierda, creationHelper, dto.getStatusDetalleDeal(), _10_STATUS_DETALLE_DEAL);
        	crearCelda(fila, alineacionIzquierda, creationHelper, dto.getPromotor(), _11_PROMOTOR);
        	crearCelda(fila, alineacionIzquierda, creationHelper, dto.getIdCanal(), _12_ID_CANAL);
        	crearCelda(fila, alineacionIzquierda, creationHelper, dto.getFechaCreacion(), _13_FECHA_CREACION);
        	crearCelda(fila, alineacionIzquierda, creationHelper, dto.getFechaValor(), _14_FECHA_VALOR);
        	crearCelda(fila, alineacionIzquierda, creationHelper, dto.getStatusDeal(), _15_STATUS_DEAL);
        	crearCelda(fila, alineacionIzquierda, creationHelper, dto.getDealCorreccion(), _16_DEAL_CORRECCION);
        	crearCelda(fila, alineacionDerecha, creationHelper, dto.getNumDealOriginal(), _17_NUM_DEAL_ORIGINAL);
        }
        
        try
        {
        	wb.write(bas);
        	bas.close();
        	
        	FileOutputStream fileOut = new FileOutputStream("OperacionesRegulatorio.xlsx");
            wb.write(fileOut);
            fileOut.close();
            
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
			case _0_CLIENTE:          case _1_CONTRATO_SICA: case _2_TIPO_PERSONA:
			case _3_RFC:              case _15_STATUS_DEAL:  case _16_DEAL_CORRECCION:
			case _14_FECHA_VALOR:     case _11_PROMOTOR:     case _12_ID_CANAL:
			case _6_ENTREGAR_RECIBIR: case _7_ID_DIVISA:     case _10_STATUS_DETALLE_DEAL:
	        	celda.setCellType(XSSFCell.CELL_TYPE_STRING);
	        	if(valor != null)
	        		celda.setCellValue((String)valor); 
	        	celda.setCellStyle(estilo);
			break;
				
			case _4_NUMERO_DEAL: case _17_NUM_DEAL_ORIGINAL: case _5_FOLIO_DETALLE:
				celda.setCellType(XSSFCell.CELL_TYPE_NUMERIC);
				if(valor != null)
					celda.setCellValue(((Integer)valor).floatValue());
			break;
			
			case _8_MONTO_DIVISA_ORIGINAL: case _9_MONTO_USD_DETALLE:
				celda.setCellType(XSSFCell.CELL_TYPE_NUMERIC);
				if(valor != null)
					celda.setCellValue(((BigDecimal)valor).floatValue());
	        	estilo.setDataFormat(creationHelper.createDataFormat().getFormat("#,##0.00"));
	        	celda.setCellStyle(estilo);
            break;
            
			case _13_FECHA_CREACION:
				celda.setCellType(XSSFCell.CELL_TYPE_STRING);
				if(valor != null)
					celda.setCellValue(((Date)valor));
				estilo.setDataFormat(creationHelper.createDataFormat().getFormat("dd/mm/yyyy")); // m/d/yy h:mm
				celda.setCellStyle(estilo);
			break;
		}
		
		return celda;
	}
}
