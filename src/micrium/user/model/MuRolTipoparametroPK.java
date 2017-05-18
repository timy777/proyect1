package micrium.user.model;

import java.io.Serializable;

import javax.persistence.*;

/**
 * The primary key class for the MU_ROL_TIPOPARAMETRO database table.
 * 
 */
@Embeddable
public class MuRolTipoparametroPK implements Serializable {
	// default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name = "MU_ROL_ID", insertable = false, updatable = false)
	private long muRolId;

	@Column(name = "MU_TIPO_PARAMETRO_ID", insertable = false, updatable = false)
	private long muTipoParametroId;

	public MuRolTipoparametroPK() {
	}

	public long getMuRolId() {
		return this.muRolId;
	}

	public void setMuRolId(long muRolId) {
		this.muRolId = muRolId;
	}

	public long getMuTipoParametroId() {
		return this.muTipoParametroId;
	}

	public void setMuTipoParametroId(long muTipoParametroId) {
		this.muTipoParametroId = muTipoParametroId;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof MuRolTipoparametroPK)) {
			return false;
		}
		MuRolTipoparametroPK castOther = (MuRolTipoparametroPK) other;
		return (this.muRolId == castOther.muRolId) && (this.muTipoParametroId == castOther.muTipoParametroId);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + ((int) (this.muRolId ^ (this.muRolId >>> 32)));
		hash = hash * prime + ((int) (this.muTipoParametroId ^ (this.muTipoParametroId >>> 32)));

		return hash;
	}

}