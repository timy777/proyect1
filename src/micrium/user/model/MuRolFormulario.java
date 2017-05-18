package micrium.user.model;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * The persistent class for the MU_ROL_FORMULARIO database table.
 * 
 */
@Entity
@Table(name = "MU_ROL_FORMULARIO")
public class MuRolFormulario implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private MuRolFormularioPK id;

	private boolean estado;

	private String privilegio;

	// uni-directional many-to-one association to MuFormulario
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "FORMULARIO_ID", nullable = false, insertable = false, updatable = false)
	private MuFormulario muFormulario;

	// uni-directional many-to-one association to MuRol
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ROL_ID", nullable = false, insertable = false, updatable = false)
	private MuRol muRol;

	public MuRolFormulario() {
	}

	public MuRolFormulario(MuRolFormularioPK id) {
		this.id = id;
		this.estado = true;
	}

	public MuRolFormularioPK getId() {
		return this.id;
	}

	public void setId(MuRolFormularioPK id) {
		this.id = id;
	}

	public boolean getEstado() {
		return this.estado;
	}

	public void setEstado(boolean estado) {
		this.estado = estado;
	}

	public MuFormulario getMuFormulario() {
		return this.muFormulario;
	}

	public void setMuFormulario(MuFormulario muFormulario) {
		this.muFormulario = muFormulario;
	}

	public MuRol getMuRol() {
		return this.muRol;
	}

	public void setMuRol(MuRol muRol) {
		this.muRol = muRol;
	}

	public String getPrivilegio() {
		return privilegio;
	}

	public void setPrivilegio(String privilegio) {
		this.privilegio = privilegio;
	}

	@Override
	public String toString() {
		return "MuRolFormulario [id=" + id + ", estado=" + estado + ", privilegio=" + privilegio + "]";
	}

}