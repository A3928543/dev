package com.ixe.ods.sica.util;
/**
 * @author JGVILLAN Jorge Isaac Godinez Villanueva
 * @version [CVS] $Id: DataSourceImpl.java,v 1.1 2009/01/07 17:08:16 ccovian Exp $ 
 * @since <release version>
 * DataSourceImpl es un clase implementacion de javax.sql.DataSource extiende 
 * a HibernateDaoSupport para poder obtener los datos del datasource
 * que obtiene Spring por medio del session factory
 *
 */
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.orm.hibernate.SessionFactoryUtils;
import org.springframework.orm.hibernate.support.HibernateDaoSupport;



public class DataSourceImpl extends HibernateDaoSupport implements DataSource {
	
	private DataSource ds;
	
	/*
	 * (non-Javadoc)
	 * @see javax.sql.DataSource#getConnection()
	 */

	public Connection getConnection() throws SQLException {
		// TODO Auto-generated method stub
		ds=SessionFactoryUtils.getDataSource(this.getSessionFactory());
		return ds.getConnection();
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.sql.DataSource#getConnection(java.lang.String, java.lang.String)
	 */	

	public Connection getConnection(String arg0, String arg1) throws SQLException {
		// TODO Auto-generated method stub
		ds=SessionFactoryUtils.getDataSource(this.getSessionFactory());
		return ds.getConnection(arg0,arg1);
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.sql.DataSource#getLogWriter()
	 */
	public PrintWriter getLogWriter() throws SQLException {
		// TODO Auto-generated method stub
		ds=SessionFactoryUtils.getDataSource(this.getSessionFactory());
		return ds.getLogWriter();
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.sql.DataSource#getLoginTimeout()
	 */
	
	public int getLoginTimeout() throws SQLException {
		// TODO Auto-generated method stub
		ds=SessionFactoryUtils.getDataSource(this.getSessionFactory());
		return ds.getLoginTimeout();
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.sql.DataSource#setLogWriter(java.io.PrintWriter)
	 */
	public void setLogWriter(PrintWriter arg0) throws SQLException {
		// TODO Auto-generated method stub
		ds=SessionFactoryUtils.getDataSource(this.getSessionFactory());
		ds.setLogWriter(arg0);
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.sql.DataSource#setLoginTimeout(int)
	 */
	public void setLoginTimeout(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		ds=SessionFactoryUtils.getDataSource(this.getSessionFactory());
		ds.setLoginTimeout(arg0);
	}


	/*
	 *  No borrar este codigo es necesario en caso de migrar la aplicacion a JDK 1.6
	 *  
		public boolean isWrapperFor(Class<?> arg0) throws SQLException {
			// TODO Auto-generated method stub
			ds=SessionFactoryUtils.getDataSource(this.getSessionFactory());
			return ds.isWrapperFor(arg0);
		}

		public <T> T unwrap(Class<T> arg0) throws SQLException {
			// TODO Auto-generated method stub
			ds=SessionFactoryUtils.getDataSource(this.getSessionFactory());
			return ds.unwrap(arg0);
		}
	*/



}
