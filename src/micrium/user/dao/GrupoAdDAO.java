package micrium.user.dao;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.UserTransaction;

import micrium.user.model.MuGrupoAd;

@Named
public class GrupoAdDAO implements Serializable {

	private static final long serialVersionUID = 1L;

	@PersistenceContext(unitName = "pUnit_dbSystem")
	private transient EntityManager entityManager;

	@Resource
	private transient UserTransaction transaction;

	public void save(MuGrupoAd dato) throws Exception {
		transaction.begin();
		entityManager.persist(dato);
		transaction.commit();
	}

	public MuGrupoAd get(long id) {
		return entityManager.find(MuGrupoAd.class, id);
	}

	public void update(MuGrupoAd dato) throws Exception {
		transaction.begin();
		entityManager.merge(dato);
		transaction.commit();
	}

	@SuppressWarnings("unchecked")
	public List<MuGrupoAd> getList() {
		return entityManager.createQuery("SELECT us FROM MuGrupoAd us WHERE us.estado = true Order By us.nombre").getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<MuGrupoAd> getList(long idRol) {
		String sql = "SELECT us FROM MuGrupoAd us WHERE us.muRol.rolId = :idRol and us.muRol.estado = true and us.estado=true";
		Query qu = entityManager.createQuery(sql).setParameter("idRol", idRol);
		List<MuGrupoAd> lista = qu.getResultList();
		return lista;
	}

	@SuppressWarnings("unchecked")
	public MuGrupoAd getGroupName(String name) {
		String consulta = "SELECT us FROM MuGrupoAd us WHERE us.nombre = :name and us.estado = true and us.muRol.estado = true";
		Query qu = entityManager.createQuery(consulta).setParameter("name", name);
		List<MuGrupoAd> lista = qu.getResultList();
		return lista.isEmpty() ? null : lista.get(0);

	}
}