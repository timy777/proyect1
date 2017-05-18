package micrium.user.dao;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.UserTransaction;

import micrium.user.model.MuAccion;
import micrium.user.model.MuFormulario;
import micrium.user.model.MuRolFormulario;
import micrium.user.model.MuRolFormularioPK;

@Named
public class FormDAO implements Serializable {
	private static final long serialVersionUID = 1L;

	@PersistenceContext(unitName = "pUnit_dbSystem")
	private transient EntityManager entityManager;

	@Resource
	private transient UserTransaction transaction;

	public void save(MuFormulario dato) throws Exception {
		transaction.begin();
		entityManager.persist(dato);
		String privilegio = null;
		if (dato.getMuAccions() != null) {
			privilegio = "";
			for (MuAccion muAccion : dato.getMuAccions()) {
				if (privilegio.isEmpty())
					privilegio = muAccion.getId() + "";
				else
					privilegio = privilegio + "," + muAccion.getId();
			}
		}
		MuRolFormulario rolForm = new MuRolFormulario(new MuRolFormularioPK(dato.getId(), 1));
		rolForm.setPrivilegio(privilegio);
		entityManager.persist(rolForm);
		transaction.commit();
	}

	public void update(MuFormulario dato) throws Exception {
		String sql = "UPDATE MuRolFormulario rf SET rf.privilegio = :privilegio WHERE rf.id.formularioId = :IdForm";
		transaction.begin();
		entityManager.merge(dato);
		String privilegio = null;
		if ((dato.getMuAccions() != null) && !dato.getMuAccions().isEmpty()) {
			privilegio = "";
			for (MuAccion muAccion : dato.getMuAccions()) {
				if (privilegio.isEmpty())
					privilegio = muAccion.getId() + "";
				else
					privilegio = privilegio + "," + muAccion.getId();
			}
		}
		Query q = entityManager.createQuery(sql);
		q.setParameter("IdForm", dato.getId());
		q.setParameter("privilegio", privilegio);
		q.executeUpdate();
		transaction.commit();
	}

	public void remove(long idForm) throws Exception {
		String sql = "Delete from MuRolFormulario rf where rf.id.formularioId = :idForm";
		transaction.begin();
		entityManager.createQuery(sql).setParameter("idForm", idForm).executeUpdate();

		sql = "Delete from MuFormulario f where f.id = :idForm";
		entityManager.createQuery(sql).setParameter("idForm", idForm).executeUpdate();

		sql = "update MuFormulario f set f.estado = false where f.formularioMenu.id = :idForm";
		entityManager.createQuery(sql).setParameter("idForm", idForm).executeUpdate();
		transaction.commit();
	}

	public void removeLogicoCascade(long idForm) throws Exception {
		transaction.begin();

		String sql = "update MuFormulario f set f.estado = false where f.id = :idForm or f.formularioMenu.id = :idForm";
		entityManager.createQuery(sql).setParameter("idForm", idForm).executeUpdate();

		transaction.commit();
	}

	public MuFormulario get(long id) throws Exception {
		return entityManager.find(MuFormulario.class, id);
	}

	// private MuRolFormulario obtenerRoloForm(MuRolFormularioPK id) {
	// return entityManager.find(MuRolFormulario.class, id);
	// }

	@SuppressWarnings("unchecked")
	public List<MuFormulario> getList() {
		return entityManager.createQuery("SELECT us FROM MuFormulario us").getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<MuFormulario> getListMod() {
		return entityManager.createQuery("SELECT us FROM MuFormulario us where us.formularioMenu is null and us.estado = true ORDER BY us.formularioMenu.id, us.orden").getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<MuFormulario> getListPage() {
		// return
		// entityManager.createQuery("SELECT us FROM MuFormulario us where us.formularioId is not null ORDER BY us.formularioId, us.orden").getResultList();
		return entityManager.createQuery("SELECT us FROM MuFormulario us where us.estado = true ORDER BY us.formularioMenu.id, us.orden").getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<MuFormulario> getListPage(long idModForm) {
		return entityManager.createQuery("SELECT us FROM MuFormulario us where us.formularioMenu.id = :idModForm ORDER BY us.formularioMenu.id, us.orden").setParameter("idModForm", idModForm).getResultList();
	}

	public Long getMaxIDForm() {
		long response = 0;
		String sql = "SELECT max(f.id) FROM MuFormulario f";
		Query query = entityManager.createQuery(sql);
		List<?> list = query.getResultList();
		if (list.size() == 0) {
			response = 0;
		} else {
			if (list.get(0) == null) {
				response = 0;
			} else {
				response = (Long) list.get(0);
			}
		}
		return response;
	}

	public Long getMaxOrdenForm(long formularioId) {
		long response = 0;
		String sql = "SELECT max(f.orden) FROM MuFormulario f where f.formularioMenu.id = :formularioId";
		Query query = entityManager.createQuery(sql);
		query.setParameter("formularioId", formularioId);
		List<?> list = query.getResultList();
		if (list.size() == 0) {
			response = 0;
		} else {
			if (list.get(0) == null) {
				response = 0;
			} else {
				response = (Long) list.get(0);
			}
		}
		return response;
	}

	public Long getMaxOrdenMod() {
		long response = 0;
		String sql = "SELECT max(f.orden) FROM MuFormulario f where f.formularioMenu is null";
		Query query = entityManager.createQuery(sql);
		List<?> list = query.getResultList();
		if (list.size() == 0) {
			response = 0;
		} else {
			if (list.get(0) == null) {
				response = 0;
			} else {
				response = (Long) list.get(0);
			}
		}
		return response;
	}
}
