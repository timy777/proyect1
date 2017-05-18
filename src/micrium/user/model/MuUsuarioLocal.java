package micrium.user.model;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The persistent class for the mu_usuario_local database table.
 * 
 */
@Entity
@Table(name = "mu_usuario_local")
@NamedQuery(name = "MuUsuarioLocal.findAll", query = "SELECT m FROM MuUsuarioLocal m")
public class MuUsuarioLocal implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "MU_USUARIO_LOCAL_MUUSUARIOLOCALID_GENERATOR", sequenceName = "SEQ_MU_USUARIO_LOCAL", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MU_USUARIO_LOCAL_MUUSUARIOLOCALID_GENERATOR")
	@Column(name = "mu_usuario_local_id")
	private long muUsuarioLocalId;

	@Column(name = "apellido_materno")
	private String apellidoMaterno;

	@Column(name = "apellido_paterno")
	private String apellidoPaterno;

	private Boolean bloqueado;

	private long ci;

	private String ehumano;

	@Column(name = "ehumano_padre")
	private String ehumanoPadre;

	private String email;

	private boolean estado;

	private String nombre;

	private Long telefono;

	// uni-directional many-to-one association to MuRol
	@ManyToOne
	@JoinColumn(name = "mu_rol_id")
	private MuRol muRol;

	public MuUsuarioLocal() {
	}

	public Long getMuUsuarioLocalId() {
		return this.muUsuarioLocalId;
	}

	public void setMuUsuarioLocalId(Long muUsuarioLocalId) {
		this.muUsuarioLocalId = muUsuarioLocalId;
	}

	public String getApellidoMaterno() {
		return this.apellidoMaterno;
	}

	public void setApellidoMaterno(String apellidoMaterno) {
		this.apellidoMaterno = apellidoMaterno;
	}

	public String getApellidoPaterno() {
		return this.apellidoPaterno;
	}

	public void setApellidoPaterno(String apellidoPaterno) {
		this.apellidoPaterno = apellidoPaterno;
	}

	public Boolean getBloqueado() {
		return this.bloqueado;
	}

	public void setBloqueado(Boolean bloqueado) {
		this.bloqueado = bloqueado;
	}

	public long getCi() {
		return this.ci;
	}

	public void setCi(long ci) {
		this.ci = ci;
	}

	public String getEhumano() {
		return this.ehumano;
	}

	public void setEhumano(String ehumano) {
		this.ehumano = ehumano;
	}

	public String getEhumanoPadre() {
		return this.ehumanoPadre;
	}

	public void setEhumanoPadre(String ehumanoPadre) {
		this.ehumanoPadre = ehumanoPadre;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean getEstado() {
		return this.estado;
	}

	public void setEstado(boolean estado) {
		this.estado = estado;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Long getTelefono() {
		return this.telefono;
	}

	public void setTelefono(Long telefono) {
		this.telefono = telefono;
	}

	public MuRol getMuRol() {
		return this.muRol;
	}

	public void setMuRol(MuRol muRol) {
		this.muRol = muRol;
	}

	@Override
	public String toString() {
		return "MuUsuarioLocal [muUsuarioLocalId=" + muUsuarioLocalId + ", apellidoMaterno=" + apellidoMaterno + ", apellidoPaterno=" + apellidoPaterno + ", bloqueado=" + bloqueado + ", ci=" + ci + ", ehumano=" + ehumano + ", ehumanoPadre=" + ehumanoPadre + ", email=" + email + ", estado=" + estado + ", nombre=" + nombre + ", telefono=" + telefono + ", muRol=" + muRol + "]";
	}

}