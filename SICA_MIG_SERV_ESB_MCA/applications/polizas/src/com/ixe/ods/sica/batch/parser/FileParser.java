package com.ixe.ods.sica.batch.parser;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ixe.ods.sica.batch.error.FileParserException;
import com.ixe.ods.sica.batch.error.RowParserException;

import static org.apache.commons.io.FileUtils.readLines;

/**
 * The Class ParserFile.
 */
public abstract class FileParser {
	
	/** The Constant LOG. */
	private static final Logger LOG = LoggerFactory.getLogger(FileParser.class);
	
	/** The ENCODING. */
    public static final String ENCODING = "ISO-8859-1";
    
    /** The rows. */
    private List<String> rows;
    
    /** The total rows. */
    private int totalRows;
    
    /** The count. */
    protected int numRow;
    
	/**
	 * Instantiates a new parser file.
	 */
	public FileParser() {
	}
	
	
	public FileParser(File archivo) 
			throws FileParserException{
		creaRows(archivo);
	}
    
    
    /**
     * Crea rows.
     *
     * @param archivo the archivo
     * @throws FileParserException the parser file exception
     */
    private synchronized void creaRows(File archivo)
	throws FileParserException {
        try {    	
            this.rows = readLines(archivo, ENCODING);
            if (this.rows.isEmpty()) {
            	throw new FileParserException("El archivo \"" + 
            			archivo.getName() + "\" esta vac\u00EDo.");
            }
            else {
                totalRows = rows.size();
                LOG.debug("Se van a procesar {} registro(s).", totalRows);
            }
        } 
        catch (IOException ex) {
        	LOG.error("IOException creaRows() ", ex);
            throw new FileParserException(ex);
        } 
    }

    /**
     * Checks for more rows.
     *
     * @return true, if successful
     */
    public boolean hasMoreRows() {
    	return numRow < totalRows;
    }

    protected abstract boolean validateRow(String row) 
    		throws RowParserException;
    	
    
    public abstract Map<String, Object> nextRow() 
    		throws RowParserException;

	/**
	 * Gets the rows.
	 *
	 * @return the rows
	 */
	public List<String> getRows() {
		return rows;
	}

	/**
	 * Sets the rows.
	 *
	 * @param rows the rows to set
	 */
	public void setRows(List<String> rows) {
		this.rows = rows;
	}

	/**
	 * Sets the total rows.
	 *
	 * @param totalRows the totalRows to set
	 */
	public void setTotalRows(Integer totalRows) {
		this.totalRows = totalRows;
	}
	
	/**
     * Gets the total rows.
     *
     * @return the totalRows
     */
    public Integer getTotalRows() {
        return totalRows;
    }

}
