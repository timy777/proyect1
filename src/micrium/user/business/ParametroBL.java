package micrium.user.business;

import java.io.Serializable;
import java.util.List;

import javax.inject.Named;

import micrium.user.model.MuParametro;

import com.tigo.dao.MasterDao;
import com.tigo.dao.PQuery;

@Named
public class ParametroBL extends MasterDao implements Serializable {
	private static final long serialVersionUID = 1L;

	public String validate(MuParametro parametro) throws Exception {
		String resp = "";
		if (parametro != null) {
			switch (parametro.getTipo()) {
			case MuParametro.TIPO_CADENA: {
				if (parametro.getValorCadena() == null || parametro.getValorCadena().trim().isEmpty()) {
					resp = "[Error]El valor del Parametro " + parametro.getNombre() + ", esta Vacio.. ";
				}
				break;
			}
			case MuParametro.TIPO_NUMERICO: {
				if (parametro.getValorNumerico() == null) {
					resp = "[Error]El valor del Parametro " + parametro.getNombre() + ", esta Vacio.. ";
				}
				break;
			}
			case MuParametro.TIPO_FECHA: {
				if (parametro.getValorFecha() == null) {
					resp = "[Error]El valor del Parametro " + parametro.getNombre() + ", esta Vacio.. ";
				}
				break;
			}
			case MuParametro.TIPO_BOOLEANO: {
				if (parametro.getValorBooleano() == null) {
					resp = "[Error]El valor del Parametro " + parametro.getNombre() + ", esta Vacio.. ";
				}
				break;
			}
			}
		}
		return resp;
	}

	@SuppressWarnings("unchecked")
	public List<MuParametro> getParameters(long tipoParametro) throws Exception {
		String sql = "SELECT pa FROM MuParametro pa where pa.tipoParametro.muTipoParametroId = :tipoParametro Order by pa.parametroId";
		PQuery p = PQuery.getInstancia();
		p.put("tipoParametro", tipoParametro);
		return findAllQuery(MuParametro.class, sql, p);
	}

	public MuParametro getParametro(int idParametro) throws Exception {
		return (MuParametro) find(idParametro, MuParametro.class);
	}

}
