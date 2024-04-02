package com.banorte.reporte.diario.transferencias.services.impl;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
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
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.banorte.reporte.diario.transferencias.dto.DatosTransferencia;
import com.banorte.reporte.diario.transferencias.services.IReporteService;

public class ReporteServiceImpl implements IReporteService 
{
	private static final Logger LOG = Logger.getLogger(ReporteServiceImpl.class);
	
	public ByteArrayOutputStream crearReporteExcel(List<DatosTransferencia> transferencias)
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
        DatosTransferencia transferencia = null;
        
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
        
        for(indice = 0; indice < transferencias.size(); indice++)
        {
        	transferencia = transferencias.get(indice);
        	
        	fila = sheet.createRow(indice);
        	
        	//crearCelda(fila, alineacionDerecha, creationHelper, transferencia.getIdScTraextEnv(), 0);
        	crearCelda(fila, alineacionCentro, creationHelper, transferencia.getFechaOperacion(), 0);
        	crearCelda(fila, alineacionIzquierda, creationHelper, transferencia.getTipoOperacion(), 1);
        	crearCelda(fila, alineacionIzquierda, creationHelper, transferencia.getTipoTransferencia(), 2);
        	crearCelda(fila, alineacionIzquierda, creationHelper, transferencia.getIdOperacion(), 3);
        	crearCelda(fila, alineacionDerecha, creationHelper, transferencia.getMedio(), 4);
        	crearCelda(fila, alineacionDerecha, creationHelper, transferencia.getOtroMedio(), 5);
        	crearCelda(fila, alineacionIzquierda, creationHelper, transferencia.getFolio(), 6);
        	crearCelda(fila, alineacionDerecha, creationHelper, transferencia.getIdInstReporta(), 7);
        	crearCelda(fila, alineacionDerecha, creationHelper, transferencia.getIdBcoCliOriginador(), 8);
        	crearCelda(fila, alineacionIzquierda, creationHelper, transferencia.getIdCliOriginador(), 9);
        	crearCelda(fila, alineacionIzquierda, creationHelper, transferencia.getTipoCliOriginador(), 10);
        	crearCelda(fila, alineacionIzquierda, creationHelper, transferencia.getRazonSocialCli(), 11);
        	crearCelda(fila, alineacionIzquierda, creationHelper, transferencia.getApPaternoCli(), 12);
        	crearCelda(fila, alineacionIzquierda, creationHelper, transferencia.getApMaternoCli(), 13);
        	crearCelda(fila, alineacionIzquierda, creationHelper, transferencia.getApNombreCli(), 14);
        	crearCelda(fila, alineacionIzquierda, creationHelper, transferencia.getFechaNac(), 15);
        	crearCelda(fila, alineacionDerecha, creationHelper, transferencia.getNacionCliOriginador(), 16);
        	crearCelda(fila, alineacionDerecha, creationHelper, transferencia.getTipoIdOriginadorExt(), 17);
        	crearCelda(fila, alineacionCentro, creationHelper, transferencia.getIdOriginadorExt(), 18);
        	crearCelda(fila, alineacionIzquierda, creationHelper, transferencia.getSexoCliOriginador(), 19);
        	crearCelda(fila, alineacionIzquierda, creationHelper, transferencia.getEdoNacCliOriginador(), 20);
        	crearCelda(fila, alineacionDerecha, creationHelper, transferencia.getTipoCuentaOrd(), 21);
        	crearCelda(fila, alineacionDerecha, creationHelper, transferencia.getCuentaOrd(), 22);
        	crearCelda(fila, alineacionCentro, creationHelper, transferencia.getTipoIdInstRecept(), 23);
        	crearCelda(fila, alineacionIzquierda, creationHelper, transferencia.getIdInstRecept(), 24);
        	crearCelda(fila, alineacionIzquierda, creationHelper, transferencia.getTipoIdInstBenef(), 25);
        	crearCelda(fila, alineacionIzquierda, creationHelper, transferencia.getIdInstBenef(), 26);
        	crearCelda(fila, alineacionDerecha, creationHelper, transferencia.getTipoBenef(), 27);
        	crearCelda(fila, alineacionIzquierda, creationHelper, transferencia.getNombreRazonSocBenef(), 28);
        	crearCelda(fila, alineacionIzquierda, creationHelper, transferencia.getCuentaBenef(), 29);
        	crearCelda(fila, alineacionDerecha, creationHelper, transferencia.getPaisBcoBenef(), 30);
        	crearCelda(fila, alineacionDerecha, creationHelper, transferencia.getMontoOp(), 31);
        	crearCelda(fila, alineacionDerecha, creationHelper, transferencia.getIdDivisa(), 32);
        	crearCelda(fila, alineacionIzquierda, creationHelper, transferencia.getObservaciones(), 33);
        	crearCelda(fila, alineacionIzquierda, creationHelper, transferencia.getFolioTransNacional(), 34);
        	crearCelda(fila, alineacionIzquierda, creationHelper, transferencia.getTipoFondeoTrans(), 35);
        	crearCelda(fila, alineacionIzquierda, creationHelper, transferencia.getFondeoTrans(), 36);
        	//crearCelda(fila, alineacionIzquierda, creationHelper, transferencia.getReportadaBanxico(), 31);
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
