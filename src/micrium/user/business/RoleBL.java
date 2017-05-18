package micrium.user.business;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import micrium.user.dao.FormDAO;
import micrium.user.dao.GrupoAdDAO;
import micrium.user.dao.RoleDAO;
import micrium.user.dao.UsuarioDAO;
import micrium.user.id.ParametroID;
import micrium.user.model.MuFormulario;
import micrium.user.model.MuGrupoAd;
import micrium.user.model.MuRol;
import micrium.user.model.MuRolFormulario;
import micrium.user.model.MuRolFormularioPK;
import micrium.user.model.MuRolTipoparametro;
import micrium.user.model.MuRolTipoparametroPK;
import micrium.user.model.MuTipoParametro;
import micrium.user.model.MuUsuario;
import micrium.user.sys.P;

import org.apache.log4j.Logger;

import com.tigo.dao.MasterDao;

@Named
public class RoleBL extends MasterDao implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(RoleBL.class);

	@Inject
	private RoleDAO dao;

	@Inject
	private FormDAO formDao;

	@Inject
	private GrupoAdDAO grupoDao;

	@Inject
	private UsuarioDAO usuarioDao;

	@Inject
	private TipoParametroBL blTipoParametro;

	public String validate(MuRol rol, String idStr) throws Exception {
		if (rol == null) {
			return "Fallo al procesar solicitud";
		}
		if (rol.getNombre() == null || rol.getNombre().trim().isEmpty()) {
			return "El campo 'NOMBRE' es requerido";
		}

		MuRol rolAux = dao.getName(rol.getNombre().trim());
		if (rolAux == null)
			return "";

		if (idStr != null && !idStr.isEmpty()) {
			int id = Integer.parseInt(idStr);
			if (id == rolAux.getRolId()) {
				if (rol.getNombre().trim().equalsIgnoreCase(rolAux.getNombre().trim()))
					return "";
			}

		}
		return "Ya existe otro registro con el mismo 'NOMBRE'";
	}

	public void saveRole(MuRol role) throws Exception {

		role.setEstado(true);
		save(role);
		List<MuFormulario> lista = formDao.getList();

		for (MuFormulario formulario : lista) {
			MuRolFormularioPK rfk = new MuRolFormularioPK();
			rfk.setRolId(role.getRolId());
			rfk.setFormularioId(formulario.getId());
			MuRolFormulario rolFor = new MuRolFormulario();
			rolFor.setEstado(true);
			rolFor.setId(rfk);
			// dao.saveRolForulario(rolFor);
			save(rolFor);
		}

		List<MuTipoParametro> lstTipoParametro = blTipoParametro.findAll();
		for (MuTipoParametro old : lstTipoParametro) {
			MuRolTipoparametroPK pk = new MuRolTipoparametroPK();
			pk.setMuRolId(role.getRolId());
			pk.setMuTipoParametroId(old.getMuTipoParametroId());

			MuRolTipoparametro nuevo = new MuRolTipoparametro();
			nuevo.setId(pk);
			nuevo.setTipoPermiso(0);

			save(nuevo);
		}
	}

	public void updateRole(MuRol role) throws Exception {
		MuRol roleAux = (MuRol) dao.find(role.getRolId(), MuRol.class);
		roleAux.setNombre(role.getNombre());
		roleAux.setDescripcion(role.getDescripcion());
		dao.update(roleAux);
	}

	public void updateRoleFormulario(MuRolFormulario roleForm) throws Exception {
		dao.update(roleForm);
	}

	public void deleteRole(long idRole) throws Exception {
		boolean swCascade = (Boolean) P.getParamVal(ParametroID.ACCION_DELETE_ROLE_CASCADE);
		log.info("[Eliminar ROl] Parametro Eliminacion Cascade:" + swCascade);

		if (swCascade) {
			List<MuGrupoAd> listGrupo = grupoDao.getList(idRole);
			for (MuGrupoAd g : listGrupo) {
				g.setEstado(false);
				grupoDao.update(g);
			}

			List<MuUsuario> listUser = usuarioDao.getList(idRole);
			for (MuUsuario u : listUser) {
				u.setEstado(false);
				usuarioDao.update(u);
			}
		}

		List<MuRolFormulario> listRolForm = dao.getRolFormularioDelete(idRole);
		for (MuRolFormulario rf : listRolForm) {
			rf.setEstado(false);
			// dao.updateRolFormulario(rf);
			dao.update(rf);
		}

		MuRol rol = (MuRol) dao.find(idRole, MuRol.class);
		rol.setEstado(false);
		dao.update(rol);
	}

	public void deleteRolFormulario(long rolId) throws Exception {
		dao.deleteRolFormulario(rolId);
	}

	public List<MuRol> getRoles() throws Exception {
		return dao.getList();
	}

	public MuRol getRole(int idRole) throws Exception {
		// return dao.get(idRole);
		return (MuRol) dao.find(idRole, MuRol.class);
	}

	public List<MuRolFormulario> getRolFormulario(long id) throws Exception {
		return dao.getRolFormulario(id);
	}

	public void updateRoleFormularioList(List<String> listaAvil, int idRol) {
		// try {
		//
		// List<RolFormulario> listRolFor = getRolFormulario(idRol);
		//
		// if (listRolFor != null) {
		// Map<Integer, RolFormulario> mapRolForIdForm = new HashMap<Integer,
		// RolFormulario>();
		// Map<String, RolFormulario> mapRolForName = new HashMap<String,
		// RolFormulario>();
		// Map<String, RolFormulario> mapRolForNivel = new HashMap<String,
		// RolFormulario>();
		//
		// List<RolFormulario> listRFAux = new ArrayList<RolFormulario>();
		// List<RolFormulario> listRFDepende = new ArrayList<RolFormulario>();
		//
		// for (RolFormulario rolFor : listRolFor) {
		// String str = rolFor.getFormulario().getNivel();
		//
		// if (str != null && !str.isEmpty()) {
		// mapRolForName.put(rolFor.getFormulario().getNombre(), rolFor);
		// mapRolForNivel.put(rolFor.getFormulario().getNivel(), rolFor);
		// listRFAux.add(0, rolFor);
		// } else
		// listRFDepende.add(rolFor);
		//
		// mapRolForIdForm.put(rolFor.getFormulario().getFormularioId(),
		// rolFor);
		// }
		//
		// for (RolFormulario rolFor : listRFAux) {
		//
		// String name = rolFor.getFormulario().getNombre();
		// if (listaAvil.indexOf(name) != -1) {
		//
		// RolFormulario rolForAux = mapRolForName.get(name);
		// String str = rolForAux.getFormulario().getNivel();
		// int k = str.lastIndexOf(".");
		// if (k != -1) {
		// String path = str.substring(0, k);
		// rolForAux = mapRolForNivel.get(path);
		// name = rolForAux.getFormulario().getNombre();
		// if (listaAvil.indexOf(name) == -1) {
		// listaAvil.add(name);
		// }
		// }
		// }
		// }
		//
		// try {
		//
		// for (RolFormulario rolFor : listRFAux) {
		// String name = rolFor.getFormulario().getNombre();
		// if (listaAvil.indexOf(name) != -1)
		// rolFor.setEstado(Boolean.TRUE);
		// else
		// rolFor.setEstado(Boolean.FALSE);
		//
		// updateRoleFormulario(rolFor);
		// }
		//
		// for (RolFormulario rolFor : listRFDepende) {
		//
		// RolFormulario rolForAux;
		// do {
		// rolForAux = mapRolForIdForm.get(rolFor.getFormulario().getDepende());
		// } while (rolForAux.getFormulario().getDepende() != 0);
		//
		// rolFor.setEstado(rolForAux.getEstado());
		// updateRoleFormulario(rolFor);
		// }
		//
		// } catch (Exception e) {
		// log.error("[updateRoleFormularioList] error al guardar los roles",
		// e);
		// }
		// } else {
		// log.error("[updateRoleFormularioList] Error al obtener la lista de Roles.");
		// }
		//
		// } catch (Exception e) {
		// log.error("[updateRoleFormularioList] error al guardar los roles",
		// e);
		// }

	}

}
