package micrium.user.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * The persistent class for the MU_USUARIO database table.
 * 
 */
@Entity
@Table(name = "MU_USUARIO")
public class MuUsuario implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "MU_USUARIO_USUARIOID_GENERATOR", sequenceName = "SEQ_MU_USUARIO", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MU_USUARIO_USUARIOID_GENERATOR")
	@Column(name = "USUARIO_ID")
	private long usuarioId;

	private boolean estado;

	private String login;

	private String nombre;

	// uni-directional many-to-one association to MuRol
	@ManyToOne
	@JoinColumn(name = "ROL_ID")
	private MuRol muRol;

	public MuUsuario() {
	}

	public long getUsuarioId() {
		return this.usuarioId;
	}

	public void setUsuarioId(long usuarioId) {
		this.usuarioId = usuarioId;
	}

	public boolean getEstado() {
		return this.estado;
	}

	public void setEstado(boolean estado) {
		this.estado = estado;
	}

	public String getLogin() {
		return this.login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public MuRol getMuRol() {
		return this.muRol;
	}

	public void setMuRol(MuRol muRol) {
		this.muRol = muRol;
	}

}