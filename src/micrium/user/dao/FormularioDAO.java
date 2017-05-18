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

import org.apache.log4j.Logger;

@Named
public class FormularioDAO implements Serializable {

	private static final long serialVersionUID = 1L;
	public static Logger log = Logger.getLogger(FormularioDAO.class);

	@PersistenceContext(unitName = "pUnit_dbSystem")
	private transient EntityManager entityManager;

	@Resource
	private transient UserTransaction transaction;

	public MuFormulario find(long id) {
		return (MuFormulario) entityManager.find(MuFormulario.class, id);
	}

	@SuppressWarnings("unchecked")
	public List<MuFormulario> findPadres(long rolId) {
		String sql = "SELECT f FROM MuFormulario f,  MuRolFormulario rf where  f.id = rf.id.formularioId and f.formularioMenu is null AND rf.id.rolId = :rolId AND rf.estado = true AND f.estado = true order by f.orden";
		Query query = entityManager.createQuery(sql, MuFormulario.class);
		query.setParameter("rolId", rolId);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<MuFormulario> findHijos(long raizId, long rolId) {
		String sql = "SELECT f FROM MuFormulario f, MuRolFormulario rf where f.id = rf.id.formularioId and f.formularioMenu.id = :raizId AND rf.id.rolId = :rolId AND rf.estado = true AND f.estado = true order by f.orden";
		Query query = entityManager.createQuery(sql, MuFormulario.class);
		query.setParameter("raizId", raizId);
		query.setParameter("rolId", rolId);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> findPadresPermisos(long rolId) {
		String sql = "SELECT F.id, F.nombre, CASE WHEN (SELECT FF.id FROM MuFormulario FF, MuRolFormulario RR where FF.id = RR.id.formularioId and RR.id.rolId = :rolId AND FF.estado = true AND RR.estado = true AND F.id = FF.id) IS NULL THEN 0 ELSE 1 END  as permiso FROM MuFormulario F, MuRolFormulario R where F.id = R.id.formularioId "
				+ "and F.formularioMenu is null and R.id.rolId = 1 AND F.estado = true AND R.estado = true order by F.orden";
		Query query = entityManager.createQuery(sql);
		query.setParameter("rolId", rolId);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> findHijosPermisos(long raizId, long rolId) {
		String sql = "SELECT F.id, F.nombre, CASE WHEN (SELECT FF.id FROM MuFormulario FF, MuRolFormulario RR where FF.id = RR.id.formularioId " + "and RR.id.rolId = :rolId AND FF.estado = true AND RR.estado = true AND F.id = FF.id) IS NULL THEN 0 ELSE 1 END  as permiso " + "FROM MuFormulario F, MuRolFormulario R where F.id = R.id.formularioId "
				+ "and F.formularioMenu.id = :raizId and R.id.rolId = 1 AND F.estado = true AND R.estado = true  order by F.orden";
		Query query = entityManager.createQuery(sql);
		query.setParameter("raizId", raizId);
		query.setParameter("rolId", rolId);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<MuRolFormulario> findPrivilegios(long rolId) {
		String sql = "SELECT rf FROM MuRolFormulario rf WHERE rf.id.rolId = :rolId and rf.estado = true";
		Query query = entityManager.createQuery(sql);
		query.setParameter("rolId", rolId);
		return query.getResultList();
	}

	@SuppressWarnings("rawtypes")
	public MuRolFormulario findPrivilegios(long formId, long rolId) {
		String sql = "SELECT rf FROM MuRolFormulario rf WHERE rf.id.rolId = :rolId AND rf.id.formularioId = :formId";
		Query query = entityManager.createQuery(sql);
		query.setParameter("formId", formId);
		query.setParameter("rolId", rolId);
		List list = query.getResultList();
		if (list.size() > 0)
			return (MuRolFormulario) list.get(0);
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<MuAccion> findAccionesFormulario(long formId) {
		String sql = "SELECT AC FROM MuAccion AC INNER JOIN AC.muFormularios formulario WHERE formulario.id = :formId AND AC.estado = true";
		Query query = entityManager.createQuery(sql, MuAccion.class);
		query.setParameter("formId", formId);
		return query.getResultList();
	}

}
