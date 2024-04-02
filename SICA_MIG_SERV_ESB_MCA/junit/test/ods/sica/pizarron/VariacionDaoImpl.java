package test.ods.sica.pizarron;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

/**
 * @see test.ods.sica.pizarron.VariacionDao
 * 
 * @author Cesar Jeronimo Gomez
 *
 */
public class VariacionDaoImpl extends JdbcDaoSupport implements VariacionDao {

	/**
	 * @see test.ods.sica.pizarron.VariacionDao#insert(VariacionDto)
	 */
	public void insert(VariacionDto variacionDto) throws DataAccessException {
		getJdbcTemplate().update(VariacionSql.INSERT_VARIACION, variacionDto.toObjectArray());
	}

}
