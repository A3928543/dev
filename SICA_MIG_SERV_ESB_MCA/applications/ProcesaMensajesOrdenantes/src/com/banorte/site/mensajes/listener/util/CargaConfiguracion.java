package com.banorte.site.mensajes.listener.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.banorte.site.mensajes.listener.vo.ConfiguracionVO;

public class CargaConfiguracion 
{
	private static Logger logger = Logger.getLogger(CargaConfiguracion.class);
			
	public ConfiguracionVO cargarConfiguracion()
	{
		Properties properties = null;
		FileInputStream fis = null;
		ConfiguracionVO config = null;
		
		try
		{
			fis = new FileInputStream("config/configuracion.properties");
			properties = new Properties();
			properties.load(fis);
			config = leerPropiedades(properties);
			logger.warn("config:" + config.toString());
		}
		catch(FileNotFoundException e)
		{
			System.out.println("Error: no fue posible localizar el archivo configuracion.properties");
			e.printStackTrace();
		}
		catch(IOException e)
		{
			logger.error("Error: no fue posible cargar la configuracion del proceso.", e);
			e.printStackTrace();
		}
		catch(NumberFormatException e)
		{
			logger.error("Error: El número de puerto para conectarse con la Queue no es válido.", e);
			e.printStackTrace();
		}
		
		return config;
	}
	
	private ConfiguracionVO leerPropiedades(Properties properties) throws NumberFormatException
	{
		ConfiguracionVO config = new ConfiguracionVO();
		
		config.setDataSourceName(properties.getProperty("dataSourceName"));
		config.setInitialContextFactory(properties.getProperty("initialContextFactory"));
		config.setProviderURL(properties.getProperty("providerURL"));
		
		config.setQueueManagerHost(properties.getProperty("queueManagerHost"));
		config.setQueueManagerPort(Integer.parseInt(properties.getProperty("queueManagerPort")));
		config.setQueueManagerChannel(properties.getProperty("queueManagerChannel"));
		config.setQueueManager(properties.getProperty("queueManager"));
		config.setQueueIn(properties.getProperty("queueIn"));
		config.setUserIdMCA(properties.getProperty("userIdMCA"));
		
		logger.warn("Se realizó la lectura de las propiedades de configuración.");
		
		return config;
	}
	
	public static Properties cargarLogj4()
	{
		FileInputStream fis = null;
		Properties log4j = null;
		
		try
		{
			fis = new FileInputStream("config/log4j.properties");
			log4j = new Properties();
			log4j.load(fis);
			fis.close();
			fis = null;
		}
		catch(FileNotFoundException e)
		{
			System.out.println("No se encuentra el archivo de configuración de log4j.properties en la carpeta config.");
			e.printStackTrace();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
		return log4j;
	}
}
