package micrium.user.business;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import micrium.user.dao.AccionesDAO;
import micrium.user.dao.FormDAO;
import micrium.user.model.MuAccion;
import micrium.user.model.MuFormulario;

@Named
public class FormularioBL implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject
	private FormDAO formularioDao;

	@Inject
	private AccionesDAO accionesDAO;

	public String validate(MuFormulario formulario, boolean typo) {

		if (formulario.getNombre() == null || formulario.getNombre().trim().isEmpty()) {
			return "El campo Nombre Esta Vacio...";
		}
		if ((formulario.getUrl() == null || formulario.getUrl().trim().isEmpty()) && (typo == MuFormulario.V_PAGINA)) {
			return "El campo path/url Esta Vacio...";
		}

		return "";
	}

	public MuFormulario get(long id) throws Exception {
		return formularioDao.get(id);
	}

	public long obtenerNextIdFormulario() throws Exception {
		return formularioDao.getMaxIDForm() + 1;
	}

	public long obtenerNextOrdenMod() throws Exception {
		return formularioDao.getMaxOrdenMod() + 1;
	}

	public long obtenerNextOrdenForm(long formularioId) throws Exception {
		return formularioDao.getMaxOrdenForm(formularioId) + 1;
	}

	public void saveFormulario(MuFormulario formulario) throws Exception {
		formularioDao.save(formulario);
	}

	public void updateFormulario(MuFormulario formulario) throws Exception {
		formularioDao.update(formulario);
	}

	public void deleteFormulario(long idForm) throws Exception {
		formularioDao.remove(idForm);
	}

	public void deleteFormularioLogicoCascade(long idForm) throws Exception {
		formularioDao.removeLogicoCascade(idForm);
	}

	public List<MuFormulario> getModulos() throws Exception {
		return formularioDao.getListMod();
	}

	public List<MuFormulario> getPages() throws Exception {
		return formularioDao.getListPage();
	}

	public List<MuFormulario> getPages(long idModForm) throws Exception {
		return formularioDao.getListPage(idModForm);
	}

	public long updateOrdenMod(long idForm) throws Exception {
		MuFormulario muFormulario = formularioDao.get(idForm);
		if (muFormulario != null) {
			if (muFormulario.getUrl() != null) {
				return formularioDao.getMaxOrdenMod() + 1;
			}
			return muFormulario.getOrden();
		}
		return 0;
	}

	public List<MuAccion> getAccionesActivas() throws Exception {
		return accionesDAO.getList();

	}
}
