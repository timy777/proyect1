package micrium.user.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the MU_ROL_FORMULARIO database table.
 * 
 */
@Embeddable
public class MuRolFormularioPK implements Serializable {
	// default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name = "FORMULARIO_ID", insertable = false, updatable = false)
	private long formularioId;

	@Column(name = "ROL_ID", insertable = false, updatable = false)
	private long rolId;

	public MuRolFormularioPK() {
	}

	public MuRolFormularioPK(long formularioId, long rolId) {
		this.formularioId = formularioId;
		this.rolId = rolId;
	}

	public long getFormularioId() {
		return this.formularioId;
	}

	public void setFormularioId(long formularioId) {
		this.formularioId = formularioId;
	}

	public long getRolId() {
		return this.rolId;
	}

	public void setRolId(long rolId) {
		this.rolId = rolId;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof MuRolFormularioPK)) {
			return false;
		}
		MuRolFormularioPK castOther = (MuRolFormularioPK) other;
		return ((this.formularioId == castOther.formularioId) && (this.rolId == castOther.rolId));
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + ((int) (this.formularioId ^ (this.formularioId >>> 32)));
		hash = hash * prime + ((int) (this.rolId ^ (this.rolId >>> 32)));

		return hash;
	}
}