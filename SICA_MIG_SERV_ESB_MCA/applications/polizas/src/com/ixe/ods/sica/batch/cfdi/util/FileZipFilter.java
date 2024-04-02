package com.ixe.ods.sica.batch.cfdi.util;

import java.io.File;
import java.io.FileFilter;

import static org.apache.commons.lang.StringUtils.lastIndexOf;
import static org.apache.commons.lang.StringUtils.startsWith;
import static org.apache.commons.lang.StringUtils.upperCase;

public class FileZipFilter implements FileFilter {
	
	private static final String EXTENSION_ZIP = ".ZIP";
		
	private String prefijo;

	public FileZipFilter(String prefijo) {
		this.prefijo = prefijo;
	}

	@Override
	public boolean accept(File pathname) {
		boolean accept = false;
		
		String nameFile = pathname.getName();
		if (pathname.isFile() && startsWith(nameFile, prefijo) && 
				lastIndexOf(upperCase(nameFile), EXTENSION_ZIP) != -1) {
			accept = true;
		}
		
		
		return accept;
	}

}
