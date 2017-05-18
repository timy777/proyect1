package micrium.user.business;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import micrium.user.dao.GrupoAdDAO;
import micrium.user.dao.RoleDAO;
import micrium.user.dao.UsuarioDAO;
import micrium.user.model.MuGrupoAd;
import micrium.user.model.MuRol;
import micrium.user.model.MuRolFormulario;
import micrium.user.model.MuUsuario;

@Named
public class UsuarioBL implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private UsuarioDAO dao;

	@Inject
	private RoleDAO rolDao;

	@Inject
	private GrupoAdDAO groupDao;

	public String validate(MuUsuario user, String idStr) throws Exception {

		if (user.getLogin().isEmpty()) {
			return "El campo Login esta Vacio";
		}

		MuUsuario usAux = dao.getUsuarioLogin(user.getLogin());
		if (usAux == null)
			return "";

		if (idStr != null && !idStr.isEmpty()) {
			int id = Integer.parseInt(idStr);
			if (id == usAux.getUsuarioId())
				if (user.getLogin().equals(usAux.getLogin()))
					return "";
		}
		return "este login existe";
	}

	public void saveUserRole(MuUsuario user, int idRole) throws Exception {
		MuRol rol = new MuRol();
		rol.setRolId(idRole);
		user.setMuRol(rol);
		user.setEstado(true);
		// dao.save(user);
		dao.save(user);
	}

	public void updateUser(MuUsuario user, int idRole) throws Exception {

		MuUsuario userAux = dao.get(user.getUsuarioId());
		userAux.setLogin(user.getLogin());
		userAux.setNombre(user.getNombre());
		MuRol rol = new MuRol();
		rol.setRolId(idRole);
		userAux.setMuRol(rol);
		dao.update(userAux);
	}

	public void deleteUser(long idUser) throws Exception {
		MuUsuario user = dao.get(idUser);
		user.setEstado(false);
		dao.update(user);
	}

	public List<MuUsuario> getUsers() throws Exception {
		return dao.getList();
	}

	public MuUsuario getUser(int idUser) throws Exception {
		return dao.get(idUser);
	}

	public List<String> getListFormUser(String login) throws Exception {

		long k = 1;
		MuUsuario user = dao.getUsuarioLogin(login);
		if (user != null)
			k = user.getMuRol().getRolId();

		List<MuRolFormulario> lista = rolDao.getRolFormularioUser(k);
		List<String> listaUrl = new ArrayList<String>();
		for (MuRolFormulario rolFormulario : lista) {
			listaUrl.add(rolFormulario.getMuFormulario().getUrl());
		}
		return listaUrl;

	}

	public List<MuRolFormulario> getRolFormularios(String login) throws Exception {
		long k = 1;
		MuUsuario user = dao.getUsuarioLogin(login);
		if (user != null)
			k = user.getMuRol().getRolId();

		return rolDao.getRolFormulario(k);

	}

	public long getIdRole(String login, List<Object> userGroups) {

		MuUsuario user = dao.getUsuarioLogin(login);

		if (user != null)
			return user.getMuRol().getRolId();

		for (Object object : userGroups) {
			MuGrupoAd gr = groupDao.getGroupName(object.toString());
			if (gr != null)
				return gr.getMuRol().getRolId();
		}

		return -1;
	}

	public MuUsuario getUsuario(String login) {
		return dao.getUsuarioLogin(login);
	}
}
