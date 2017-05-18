package micrium.user.dao;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.UserTransaction;

import micrium.user.model.MuUsuario;

@Named
public class UsuarioDAO implements Serializable {

	private static final long serialVersionUID = 1L;

	@PersistenceContext(unitName = "pUnit_dbSystem")
	private transient EntityManager entityManager;

	@Resource
	private transient UserTransaction transaction;

	public void save(MuUsuario dato) throws Exception {
		transaction.begin();
		entityManager.persist(dato);
		transaction.commit();
	}

	public MuUsuario get(long id) throws Exception {
		return entityManager.find(MuUsuario.class, id);
	}

	public void update(MuUsuario dato) throws Exception {
		transaction.begin();
		entityManager.merge(dato);
		transaction.commit();
	}

	@SuppressWarnings("unchecked")
	public List<MuUsuario> getList() throws Exception {
		return entityManager.createQuery("SELECT us FROM MuUsuario us WHERE us.estado=true Order by us.nombre").getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<MuUsuario> getList(long idRol) throws Exception {

		String consulta = "SELECT us FROM MuUsuario us WHERE us.muRol.rolId = :idRol and us.muRol.estado = true and us.estado = true";
		Query qu = entityManager.createQuery(consulta).setParameter("idRol", idRol);
		List<MuUsuario> lista = qu.getResultList();
		return lista;
	}

	@SuppressWarnings("unchecked")
	public MuUsuario getUsuarioLogin(String login) {

		String consulta = "SELECT us FROM MuUsuario us WHERE us.login = :login and us.estado = true";
		Query qu = entityManager.createQuery(consulta).setParameter("login", login);
		List<MuUsuario> lista = qu.getResultList();
		return lista.isEmpty() ? null : lista.get(0);

	}

}
