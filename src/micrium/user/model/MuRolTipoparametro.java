package micrium.user.model;

import java.io.Serializable;

import javax.persistence.*;

/**
 * The persistent class for the MU_ROL_TIPOPARAMETRO database table.
 * 
 */
@Entity
@Table(name = "MU_ROL_TIPOPARAMETRO")
public class MuRolTipoparametro implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private MuRolTipoparametroPK id;

	@Column(name = "TIPO_PERMISO")
	private int tipoPermiso;

	// uni-directional many-to-one association to MuRol
	// @ManyToOne(fetch = FetchType.EAGER)
	// @JoinColumn(name = "MU_ROL_ID", insertable = false, updatable = false)
	// private MuRol muRol;

	// uni-directional many-to-one association to MuTipoParametro
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "MU_TIPO_PARAMETRO_ID", insertable = false, updatable = false)
	private MuTipoParametro muTipoParametro;

	public MuRolTipoparametro() {
	}

	public MuRolTipoparametroPK getId() {
		return this.id;
	}

	public void setId(MuRolTipoparametroPK id) {
		this.id = id;
	}

	public int getTipoPermiso() {
		return this.tipoPermiso;
	}

	public void setTipoPermiso(int tipoPermiso) {
		this.tipoPermiso = tipoPermiso;
	}

	// public MuRol getMuRol() {
	// return this.muRol;
	// }
	//
	// public void setMuRol(MuRol muRol) {
	// this.muRol = muRol;
	// }

	public MuTipoParametro getMuTipoParametro() {
		return this.muTipoParametro;
	}

	public void setMuTipoParametro(MuTipoParametro muTipoParametro) {
		this.muTipoParametro = muTipoParametro;
	}

}