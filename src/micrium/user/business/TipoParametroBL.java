package micrium.user.business;

import java.io.Serializable;
import java.util.List;

import javax.inject.Named;

import micrium.user.model.MuTipoParametro;

import com.tigo.dao.MasterDao;
import com.tigo.dao.PQuery;

@Named
public class TipoParametroBL extends MasterDao implements Serializable {

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	public List<MuTipoParametro> findAll() throws Exception {
		String sql = "SELECT pa FROM MuTipoParametro pa where pa.estado = true Order by pa.nombre";
		return findAllQuery(MuTipoParametro.class, sql, null);
	}

	@SuppressWarnings("unchecked")
	public List<MuTipoParametro> findAll(long rolId) throws Exception {
		String sql = "SELECT pa FROM MuTipoParametro pa, MuRolTipoparametro rpa where pa.muTipoParametroId = rpa.id.muTipoParametroId and pa.estado = true and rpa.id.muRolId = :rolId and rpa.tipoPermiso > 0 Order by pa.nombre";
		PQuery p = PQuery.getInstancia();
		p.put("rolId", rolId);
		return findAllQuery(MuTipoParametro.class, sql, p);
	}

}
