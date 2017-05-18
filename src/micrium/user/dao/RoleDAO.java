package micrium.user.dao;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.UserTransaction;

import com.tigo.dao.MasterDao;
import com.tigo.dao.PQuery;

import micrium.user.model.MuRol;
import micrium.user.model.MuRolFormulario;

@Named
public class RoleDAO extends MasterDao implements Serializable {

	private static final long serialVersionUID = 1L;

	// public void save(MuRol dato) throws Exception {
	// save(dato);
	// transaction.begin();
	// entityManager.persist(dato);
	// transaction.commit();
	// }

	// public void saveRolForulario(MuRolFormulario dato) throws Exception {
	//
	// transaction.begin();
	// entityManager.persist(dato);
	// transaction.commit();
	// }

	// public MuRol get(long id) {
	// return entityManager.find(MuRol.class, id);
	// }

	// public void update(MuRol dato) throws Exception {
	// transaction.begin();
	// entityManager.merge(dato);
	// transaction.commit();
	// }

	// public void updateRolFormulario(MuRolFormulario dato) throws Exception {
	// transaction.begin();
	// entityManager.merge(dato);
	// transaction.commit();
	// }

	// public void updateRolForulario(MuRolFormulario dato) throws Exception {
	// transaction.begin();
	// entityManager.merge(dato);
	// transaction.commit();
	// }

	@SuppressWarnings("unchecked")
	public List<MuRol> getList() throws Exception {
		// return
		// entityManager.createQuery("SELECT r FROM MuRol r WHERE  r.estado = true Order By r.nombre").getResultList();
		String sql = "SELECT r FROM MuRol r WHERE  r.estado = true Order By r.nombre";
		return findAllQuery(MuRol.class, sql, null);
	}

	@SuppressWarnings("unchecked")
	public MuRol getName(String name) throws Exception {
		String sql = "SELECT r FROM MuRol r WHERE r.nombre = :name and r.estado = true";
		PQuery p = PQuery.getInstancia();
		p.put("name", name);
		List<MuRol> lst = findAllQuery(MuRol.class, sql, p);
		return lst != null && lst.size() > 0 ? lst.get(0) : null;
	}

	@SuppressWarnings("unchecked")
	public List<MuRolFormulario> getRolFormulario(long id) throws Exception {
		String sql = "FROM MuRolFormulario r WHERE r.muRol.estado = true and r.estado = true and r.muRol.rolId = :id ORDER BY r.muFormulario.orden";
		// String sql =
		// "FROM MuRolFormulario r WHERE r.muRol.rolId = :id ORDER BY r.muFormulario.orden";
		PQuery p = PQuery.getInstancia();
		p.put("id", id);
		return findAllQuery(MuRolFormulario.class, sql, p);
	}

	@SuppressWarnings("unchecked")
	public List<MuRolFormulario> getRolFormularioDelete(long idRol) throws Exception {
		// String consulta =
		// "SELECT r FROM MuRolFormulario r WHERE r.muRol.rolId = :idRol and r.muRol.estado = true";
		// Query qu = entityManager.createQuery(consulta).setParameter("idRol",
		// idRol);
		// return qu.getResultList();

		String sql = "SELECT r FROM MuRolFormulario r WHERE r.muRol.rolId = :idRol and r.muRol.estado = true";
		PQuery p = PQuery.getInstancia();
		p.put("idRol", idRol);
		return findAllQuery(MuRolFormulario.class, sql, p);
	}

	public void deleteRolFormulario(long rolId) throws Exception {
		String sql = "UPDATE MuRolFormulario rf SET rf.estado = false, rf.privilegio = NULL WHERE rf.id.rolId = :rolId";
		// transaction.begin();
		// Query q = entityManager.createQuery(sql);
		// q.setParameter("rolId", rolId);
		// q.executeUpdate();
		// transaction.commit();
		PQuery p = PQuery.getInstancia();
		p.put("rolId", rolId);
		executeUpdateQuery(sql, p);
	}

	@SuppressWarnings("unchecked")
	public List<MuRolFormulario> getRolFormularioUser(long id) throws Exception {
		// String consulta =
		// "SELECT r FROM MuRolFormulario r WHERE  r.rol.rolId = :id  ORDER BY r.formulario.posicionColumna, r.formulario.posicionFila";
		// Query qu = entityManager.createQuery(consulta).setParameter("id",
		// id);
		// return qu.getResultList();

		String sql = "SELECT r FROM MuRolFormulario r WHERE  r.rol.rolId = :id  ORDER BY r.formulario.posicionColumna, r.formulario.posicionFila";
		PQuery p = PQuery.getInstancia();
		p.put("id", id);
		return findAllQuery(MuRolFormulario.class, sql, p);
	}

}
