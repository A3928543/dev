package com.banorte.ods.sica.batch.transfint;
import java.io.File;
import java.io.IOException;

import jxl.Cell;
import jxl.CellView;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;


public class WriteExcel {

  private WritableCellFormat timesBoldUnderline;
  private WritableCellFormat times;
 

  public void write(String transferencia,int contadorColumnas,int contadorRenglones,File file,WritableWorkbook workbook,WritableSheet excelSheet) throws IOException, WriteException {

	createContent(excelSheet,transferencia,contadorColumnas,contadorRenglones);

  }

  
  public void createLabel(WritableSheet sheet)
      throws WriteException {
	  
	  //WritableFont TableFormat = new WritableFont(WritableFont.ARIAL, 8, WritableFont.BOLD,false, UnderlineStyle.NO_UNDERLINE, Colour.WHITE);
	     
    // Lets create a times font
    WritableFont times10pt = new WritableFont(WritableFont.TIMES, 10, WritableFont.BOLD,false, UnderlineStyle.NO_UNDERLINE);
    times10pt.setColour(Colour.BLUE);
    // Define the cell format
    times = new WritableCellFormat(times10pt);
    times.setBackground(Colour.ORANGE);
    times.setBorder(Border.ALL, BorderLineStyle.THIN);
    // Lets automatically wrap the cells
    times.setWrap(true);


	//cellFormat.setBorder(Border.ALL, BorderLineStyle.THIN);

    // create create a bold font with unterlines
    WritableFont times11ptBoldUnderline = new WritableFont(WritableFont.TIMES, 11, WritableFont.BOLD, false,
        UnderlineStyle.NO_UNDERLINE,Colour.WHITE);
    timesBoldUnderline = new WritableCellFormat(times11ptBoldUnderline);
    timesBoldUnderline.setBackground(Colour.ORANGE);
    timesBoldUnderline.setBorder(Border.ALL, BorderLineStyle.THIN);
    // Lets automatically wrap the cells
    timesBoldUnderline.setWrap(true);

    CellView cv = new CellView();
    cv.setFormat(times);
    cv.setFormat(timesBoldUnderline);
    //cv.setAutosize(true);
 

    // Write a few headers
    addCaption(sheet, 0, 0, "Fec de la Op.");
    addCaption(sheet, 1, 0, "Hora de la Op.");
    addCaption(sheet, 2, 0, "Fec de la Instr. del Ord.");
    addCaption(sheet, 3, 0, "Hora de la Instr. del Ord.");
    addCaption(sheet, 4, 0, "Tipo de Op.");
    addCaption(sheet, 5, 0, "Tipo de Transfer.");
    addCaption(sheet, 6, 0, "Id. de la Op.");
    addCaption(sheet, 7, 0, "Medio Ut. Transfer.");
    addCaption(sheet, 8, 0, "Otro medio Ut. para la Transfer.");
    addCaption(sheet, 9, 0, "Folio Ut. en el medio");
    addCaption(sheet, 10, 0, "Id. institución que reporta");
    addCaption(sheet, 11, 0, "Id. del Bco usuario del Clte");
    addCaption(sheet, 12, 0, "Id. del Clte");
    addCaption(sheet, 13, 0, "Tipo de Clte");
    addCaption(sheet, 14, 0, "Razón social del Clte");
    addCaption(sheet, 15, 0, "Ap pat Clte");
    addCaption(sheet, 16, 0, "Ap mat Clte");
    addCaption(sheet, 17, 0, "Nombre Clte");
    addCaption(sheet, 18, 0, "Fec nac/const Clte");
    addCaption(sheet, 19, 0, "Nac del Clte");
    addCaption(sheet, 20, 0, "Tipo Id. Clte ext");
    addCaption(sheet, 21, 0, "Id. de Clte ext");
    addCaption(sheet, 22, 0, "Sexo del Clte");
    addCaption(sheet, 23, 0, "Edo nac Clte");
    addCaption(sheet, 24, 0, "Tipo cuenta Clte");
    addCaption(sheet, 25, 0, "Cuenta Clte");
    addCaption(sheet, 26, 0, "Tipo de Id. inst receptora/emisora");
    addCaption(sheet, 27, 0, "Id inst receptora/emisora");
    addCaption(sheet, 28, 0, "Tipo Id inst benef u Ord.");
    addCaption(sheet, 29, 0, "Id inst benef u Ord.");
    addCaption(sheet, 30, 0, "Tipo benef u Ord.");
    addCaption(sheet, 31, 0, "Razón soc/nombre benef/Ord");
    addCaption(sheet, 32, 0, "Cta del benef/Ord");
    addCaption(sheet, 33, 0, "País del Bco benef/Ord");
    addCaption(sheet, 34, 0, "Monto de la Op.");
    addCaption(sheet, 35, 0, "Moneda Op.");
    addCaption(sheet, 36, 0, "Propósito Transfer.");
    addCaption(sheet, 37, 0, "Folio Transfer nal");
    addCaption(sheet, 38, 0, "Tipo fondeo Transfer");
    addCaption(sheet, 39, 0, "Fondeo de la Transfer");
    addCaption(sheet, 40, 0, "Numero de Deal");
  }

  private void createContent(WritableSheet sheet,String transferencia,int contadorColumnas,int contadorRenglones) throws WriteException,
      RowsExceededException {

	  String[] arrayCampos = transferencia.split(";");
	  
    // now a bit of text
	  for(int i = 0;i< contadorColumnas; i++){
	  addLabel(sheet, i, contadorRenglones, arrayCampos[i]);
	  }
  }

  private void addCaption(WritableSheet sheet, int column, int row, String s)
      throws RowsExceededException, WriteException {
	  
    Label label;
    label = new Label(column, row, s, timesBoldUnderline);
    sheet.addCell(label);
  }

  private void addLabel(WritableSheet sheet, int column, int row, String s)
      throws WriteException, RowsExceededException {
	  
	  WritableCellFormat cellFormat = new WritableCellFormat();
	  //cellFormat.setBackground(Colour.ORANGE);
	  cellFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
	  cellFormat.setWrap(true);
	  
    Label label;
    System.out.println(" COLUMNA: "+column+" RENGLON " + row+" Strimg: "+s);
    
    label = new Label(column, row, s, cellFormat);
    //label = new Label(column, row, s, times);
    sheet.addCell(label);
  }
  
  public void sheetAutoFitColumns(WritableSheet sheet) {
	    for (int i = 0; i < sheet.getColumns(); i++) {
	        Cell[] cells = sheet.getColumn(i);
	        int longestStrLen = -1;

	        if (cells.length == 0)
	            continue;

	        /* Find the widest cell in the column. */
	        for (int j = 0; j < cells.length; j++) {
	            if ( cells[j].getContents().length() > longestStrLen ) {
	                String str = cells[j].getContents();
	                if (str == null || str.isEmpty())
	                    continue;
	                longestStrLen = str.trim().length();
	            }
	        }

	        /* If not found, skip the column. */
	        if (longestStrLen == -1) 
	            continue;

	        /* If wider than the max width, crop width */
	        if (longestStrLen > 255)
	            longestStrLen = 255;

	        CellView cv = sheet.getColumnView(i);
	        cv.setSize(longestStrLen * 256 + 100); /* Every character is 256 units wide, so scale it. */
	        sheet.setColumnView(i, cv);
	    }
	}

//  public static void main(String[] args) throws WriteException, IOException {
//    WriteExcel test = new WriteExcel();
//    //test.setOutputFile("C:\\Documents and Settings\\hcohj810\\Mis documentos\\workspace\\FormatearExcel\\src\\lars.xls");
//    test.setOutputFile("D:\\lars.xls");
//    test.write("");
//    System.out.println("Please check the result file under c:/temp/lars.xls ");
//  }
} 