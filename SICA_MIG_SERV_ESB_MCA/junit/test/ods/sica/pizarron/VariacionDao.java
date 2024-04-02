package test.ods.sica.pizarron;

import org.springframework.dao.DataAccessException;

/**
 * DAO para SICA_VARIACION
 * 
 * @author Cesar Jeronimo Gomez
 *
 */
public interface VariacionDao {
	
	/**
	 * Inserta en SICA_VARIACION
	 * 
	 * @param variacionDto
	 * @throws DataAccessException
	 */
	public void insert(VariacionDto variacionDto) throws DataAccessException;
	
}
