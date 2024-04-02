package com.banorte.site.mensajes.listener.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

public class ConexionOracleDB 
{
	private static Logger logger = Logger.getLogger(ConexionOracleDB.class);
			
	private DataSource dataSource = null;
	private String dataSourceName;
	private String initialContextFactory;
	private String providerURL;
	
	public ConexionOracleDB(String dataSourceName, String initialContextFactory, String providerURL)
	{
		this.dataSourceName = dataSourceName;
        this.initialContextFactory = initialContextFactory;
        this.providerURL = providerURL;
        solicitaDataSource();
	}
	
	public Connection getConnectionOracle()
	{
		try
		{
			if(isDataSourceValido())
				return dataSource.getConnection();
			else
				return null; // solicitaDataSource()
		}
		catch(SQLException e)
		{
			System.out.println("Error: No fue posible obtener una conexión con la base de datos.");
			e.printStackTrace();
			return null;
		}
	}
	
	private void solicitaDataSource()
	{
		InitialContext env = null;
        Hashtable<String,String> ht = new Hashtable<String,String>();

        ht.put(Context.INITIAL_CONTEXT_FACTORY, initialContextFactory); // "weblogic.jndi.WLInitialContextFactory"
        ht.put(Context.PROVIDER_URL, providerURL); // "t3://localhost:7001"
    
        try 
        {
	         env = new InitialContext(ht);
	         dataSource = (javax.sql.DataSource) env.lookup (dataSourceName);  
	         logger.warn("Se consiguió la referencia al dataSource de conexión a base de datos.");
	    } 
        catch (NamingException ne) 
        { 
        	logger.error("Error: no se pudo establecer la conexión con la base de datos.", ne);
        	ne.printStackTrace();
        }
	}
	
	public boolean isDataSourceValido()
	{
		return dataSource != null;
	}
	
	public void ejecutaInstruccionesSQL(ArrayList<String> instruccionesSql)
	{
		Connection c = null;
		Statement s = null;
		int totalEjecucionesSQL[];
		
		try 
		{
			c = dataSource.getConnection();
			s = c.createStatement();
			
			for(int indice = 0; indice < instruccionesSql.size(); indice++)
			{
				s.addBatch(instruccionesSql.get(indice));
				logger.warn("Se va a ejecutar la instrucción: " + instruccionesSql.get(indice));
			}
			
			totalEjecucionesSQL = s.executeBatch();
			//c.commit();
			
			logger.warn("Se procesaron " + totalEjecucionesSQL.length + " instrucciones SQL");
		} 
		catch(java.sql.BatchUpdateException e)
		{
			logger.error(" (BatchUpdateException) Ocurrió un error al procesar las instrucciones para insertar datos del ordenante enviados por el mediador.", e);
			e.printStackTrace();
			//throw new MQException(2, 2009, e);
		}
		catch (SQLException e) 
		{
			logger.error("(SQLException) Ocurrió un error al procesar las instrucciones para insertar datos del ordenante enviados por el mediador..", e);
			e.printStackTrace();
		}
		finally
		{
			if(s != null)
			{
				try 
				{
					s.close();
				} catch (SQLException e) 
				{
					e.printStackTrace();
				}
				s = null;
			}
			
			if(c != null)
			{
				try {
					c.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public String consultarNumeroOrden(String idDetalleLiquidacion)
	{
		String numeroOrden = null;
		String query = "select tl.numero_orden numeroOrden from tes_detalle_liquidacion td, tes_liquidacion tl " +
				       "where td.id_detalle_liquidacion = " + idDetalleLiquidacion + " and td.id_liquidacion = tl.id_liquidacion";
		
		Connection c = null;
		Statement s = null;
		ResultSet r = null;
		
		try
		{
			c = dataSource.getConnection();
			s = c.createStatement();
			r = s.executeQuery(query);
			
			if(r.next())
				numeroOrden = r.getString("numeroOrden");
			
			r.close();
			r = null;
			s.close();
			s = null;
			c.close();
		}
		catch(SQLException e)
		{
			logger.error("Ocurrió un error al consultar el numero de orden asociado al detalle de liquidacion: " + idDetalleLiquidacion, e);
			e.printStackTrace();
		}
		
		return numeroOrden;
	}
}
