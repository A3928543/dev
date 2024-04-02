package com.ixe.ods.sica.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.dao.PhoenixCuentaMigradaDao;
import com.ixe.ods.sica.dto.CuentaMigradaDto;


/**
 * Clase para consultar 
 * las cuentas migradas de phoenix hacia
 * Altamira.
 *
 * @author Diego Pazaran
 *  Banorte
 *
 * @version $Revision: 1.1.2.1 $
 */
public class JdbcPhoenixCuentaMigradaDao extends JdbcDaoSupport implements PhoenixCuentaMigradaDao {
    /** Sentencia sql para la consulta de una cuenta migrada * */
    private static final String QUERY_CUENTA_MIGRADA =
        "select top 1 cta.cuenta_ant, cta.cuenta_nue, cta.status_cta, cta.cte_tit01 " +
        "from dbo.phx_mig_bgwcmcta cta " + "where cta.status_mig = 'S'" + "and cta.cuenta_ant = ?";

    /**
     * El objeto para logging.
     */
    private final transient Log _logger = LogFactory.getLog(getClass());

    /**
     * Creates a new JdbcPhoenixCuentaMigradaDao object.
     */
    public JdbcPhoenixCuentaMigradaDao() {
        super();
    }

    /**
     * Método que búsca en la tabla de cuentas migradas
     * una cuenta en particular.
     * @param noCuentaIxe la cuenta a buscar.
     * @return un objeto <code>CuentaMigradaDto</code>
     *          con la información de la cuenta.
     */
    public CuentaMigradaDto buscarCuenta(String noCuentaIxe) {
        Object[] args = new Object[] { noCuentaIxe };
        int[] types = new int[] { Types.VARCHAR };

        try {
            _logger.debug("Buscando cuenta en phx_mig_bgwcmcta... ");

            RowMapper mapper = new RowMapper() {
                    public Object mapRow(ResultSet rs, int rownum)
                        throws SQLException {
                        CuentaMigradaDto cuentaMigradaDto = new CuentaMigradaDto();
                        cuentaMigradaDto.setCuentaIxe(rs.getString("cuenta_ant").trim());
                        cuentaMigradaDto.setCuentaAltamira(rs.getString("cuenta_nue").trim());
                        cuentaMigradaDto.setEstatus(rs.getString("status_cta").trim());
                        cuentaMigradaDto.setNoClienteAltamiraIxe(rs.getString("cte_tit01").trim());

                        return cuentaMigradaDto;
                    }
                };

            return (CuentaMigradaDto) getJdbcTemplate().queryForObject(QUERY_CUENTA_MIGRADA, args,
                types, mapper);
        }
        catch(IncorrectResultSizeDataAccessException irsde) {
        	_logger.debug("No se encontraron registros para la cuenta: " + 
        			noCuentaIxe + " " + irsde.getMessage());
        	return null;
        }
        catch (DataAccessException dae) {
            throw new SicaException("No fue posible realizar la consulta.", dae);
        }
    }
}
