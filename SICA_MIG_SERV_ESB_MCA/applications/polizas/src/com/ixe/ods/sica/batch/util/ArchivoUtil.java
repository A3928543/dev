package com.ixe.ods.sica.batch.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.util.Collections.addAll;
import static org.apache.commons.lang.StringUtils.isEmpty;
import static org.apache.commons.lang.StringUtils.isNotEmpty;
import static org.apache.commons.lang.StringUtils.lastIndexOf;
import static org.apache.commons.lang.StringUtils.substring;
import static net.lingala.zip4j.util.Zip4jConstants.COMP_DEFLATE;
import static net.lingala.zip4j.util.Zip4jConstants.DEFLATE_LEVEL_NORMAL;
import static net.lingala.zip4j.util.Zip4jConstants.ENC_METHOD_STANDARD;

public class ArchivoUtil {
	
	private static final Logger LOG = LoggerFactory.getLogger(ArchivoUtil.class);
	
	private static final String ZIP_EXTENSION = ".ZIP";

	private ArchivoUtil() {
	}
	
	public static boolean descomprimir(String archivo, String archivoBuscado, 
			String rutaDestino, String password) {
		File zip = new File(archivo);
		
		return descomprimir(zip, archivoBuscado, rutaDestino, password);
	}
	
	public static boolean descomprimir(File archivo, String archivoBuscado, 
			String rutaDestino, String password) {
		boolean descompresion = false;
		ZipFile zipFile = null;
		try {
			zipFile = new ZipFile(archivo);
			LOG.info("zipFile.isValidZipFile(): {}", zipFile.isValidZipFile());
			if (zipFile.isValidZipFile()) {
				if (zipFile.isEncrypted() && isNotEmpty(password)) {
					zipFile.setPassword(password);
				}
				if (isNotEmpty(archivoBuscado)) {
					zipFile.extractFile(archivoBuscado, rutaDestino);
				}
				else {
					zipFile.extractAll(rutaDestino);
				}
						descompresion = true;
				LOG.debug("Termina descompresion de archivo.");
			}
		}
		catch (ZipException ex) {
			LOG.error("Error en descomprimir() ", ex); 
		}
		
		return descompresion;
	}
	
	public static File comprimir(String src, String dest, boolean isCreateDir, String pwd) {
		File srcFile = new File(src);
		return comprimir(srcFile, dest, isCreateDir, pwd);
	}
	
	public static File comprimir(File src, String dest, boolean isCreateDir, String passwd) {
		File result = null;
		ZipParameters parameters = new ZipParameters();
		parameters.setCompressionMethod(COMP_DEFLATE);
		parameters.setCompressionLevel(DEFLATE_LEVEL_NORMAL);
		if (isNotEmpty(passwd)) {
			parameters.setEncryptFiles(true); 
			parameters.setEncryptionMethod(ENC_METHOD_STANDARD);
			parameters.setPassword(passwd.toCharArray()); 
		}  
		try {
			dest = ifDestFileEmpty(src, dest);
			LOG.debug("dest: {}", dest);
			ZipFile zipFile = new ZipFile(dest);  
			if (src.isDirectory()) { 
	            if (!isCreateDir) {  
	                File [] subFiles = src.listFiles();  
	                ArrayList<File> temp = new ArrayList<File>();  
	                addAll(temp, subFiles);  
	                zipFile.addFiles(temp, parameters);  
	            }
	            else {
	            	zipFile.addFolder(src, parameters);
	            }
            } 
			else {  
                zipFile.addFile(src, parameters);  
            }
			result = zipFile.getFile();
		} 
		catch (ZipException ex) {
		   	LOG.error("ZipException en ", ex);
	    }
		
		return result;
	}
	
	private static String ifDestFileEmpty(File srcFile, String dest) {
		String nameFile = dest;
		String separator = File.separator;
		if (srcFile != null) {
			if (isEmpty(nameFile)) {
				String dirName = "";
				if (srcFile.isDirectory()) {
					nameFile = srcFile.getName() + ZIP_EXTENSION;
					dirName = srcFile.getPath();
				}
				else {
					int index = lastIndexOf(srcFile.getName(), ".");
					LOG.debug("index {}", index);
					if (index != -1) {
						nameFile = substring(srcFile.getName(), 0, index);
					}
					nameFile = nameFile + ZIP_EXTENSION;
					dirName = srcFile.getParent();
				}
				nameFile = dirName + separator + nameFile;
			}
		}
		
		return nameFile;
	}
	
	public static void sortFilesByName(File[] files, final boolean sortAsc) {
		Comparator<File> comparator = new Comparator<File> () {
			@Override
			public int compare(File o1, File o2) {
				if (sortAsc) {
					return (o1.getName()).compareTo(o2.getName());
				} 
				else {
					return -1 * (o1.getName()).compareTo(o2.getName());
				}
			}
		};
		Arrays.sort(files, comparator);
	}
	
	public static void main(String args[]) throws ZipException {
		//comprimir("archivos/PorProcesar", 
		//		  null, false, null);
		
		//LOG.debug("{}", 
		//descomprimir("archivos/indices/Procesados/CFDI_SICA_TIMBRADO_20151113.ZIP",  
		//		  null, "archivos/indices/Procesados/CFDI_SICA_TIMBRADO_20151113", null));
		String dir = "C:/Users/IHernandez/workspace/polizas/archivos/indices/Procesados";
		File[] files = new File(dir).listFiles();
		sortFilesByName(files, true);
	}

}
