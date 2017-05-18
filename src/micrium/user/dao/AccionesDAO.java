package micrium.user.dao;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

import micrium.user.model.MuAccion;

@Named
public class AccionesDAO implements Serializable {
	private static final long serialVersionUID = 1L;

	@PersistenceContext(unitName = "pUnit_dbSystem")
	private transient EntityManager entityManager;

	@Resource
	private transient UserTransaction transaction;

	public MuAccion get(long id) throws Exception {
		return entityManager.find(MuAccion.class, id);
	}

	@SuppressWarnings("unchecked")
	public List<MuAccion> getList() {
		return entityManager.createQuery("SELECT ac FROM MuAccion ac where ac.estado = true order by ac.id").getResultList();
	}

}
