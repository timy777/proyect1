package micrium.user.business;

import java.io.Serializable;
import java.util.List;

import javax.inject.Named;

import micrium.user.model.MuRolTipoparametro;

import com.tigo.dao.MasterDao;
import com.tigo.dao.PQuery;

@Named
public class RolTipoparametroBL extends MasterDao implements Serializable {

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	public List<MuRolTipoparametro> getMuRolTipoparametro(long rolId) throws Exception {
		String sql = "select rtp from MuRolTipoparametro rtp where rtp.id.muRolId = :rolId order by rtp.muTipoParametro.nombre";
		PQuery p = PQuery.getInstancia();
		p.put("rolId", rolId);
		return findAllQuery(MuRolTipoparametro.class, sql, p);
	}

	@SuppressWarnings("unchecked")
	public boolean validarRolTipoparametro(long rolId, long tipoParametroId) throws Exception {
		String sql = "select r from MuRolTipoparametro r where r.id.muRolId = :rolId and r.id.muTipoParametroId = :tipoParametroId and r.tipoPermiso = :permiso ";
		PQuery p = PQuery.getInstancia();
		p.put("rolId", rolId);
		p.put("tipoParametroId", tipoParametroId);
		p.put("permiso", 2);// El permiso 2 es de escritura
		List<MuRolTipoparametro> lst = findAllQuery(MuRolTipoparametro.class, sql, p);
		return lst != null && lst.size() > 0;
	}
}
