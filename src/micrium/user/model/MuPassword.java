package micrium.user.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;

/**
 * The persistent class for the mu_password database table.
 * 
 */
@Entity
@Table(name = "mu_password")
@NamedQuery(name = "MuPassword.findAll", query = "SELECT m FROM MuPassword m")
public class MuPassword implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "MU_PASSWORD_MUPASSWORDID_GENERATOR", sequenceName = "SEQ_MU_PASSWORD", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MU_PASSWORD_MUPASSWORDID_GENERATOR")
	@Column(name = "mu_password_id")
	private long muPasswordId;

	@Temporal(TemporalType.DATE)
	@Column(name = "fecha_registro")
	private Date fechaRegistro;

	private String pwd;

	@Column(name = "pwd_generado")
	private boolean pwdGenerado;

	// uni-directional many-to-one association to MuUsuarioLocal
	@ManyToOne
	@JoinColumn(name = "mu_usuario_local_id")
	private MuUsuarioLocal muUsuarioLocal;

	public MuPassword() {
	}

	public long getMuPasswordId() {
		return this.muPasswordId;
	}

	public void setMuPasswordId(long muPasswordId) {
		this.muPasswordId = muPasswordId;
	}

	public Date getFechaRegistro() {
		return this.fechaRegistro;
	}

	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	public String getPwd() {
		return this.pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public MuUsuarioLocal getMuUsuarioLocal() {
		return this.muUsuarioLocal;
	}

	public void setMuUsuarioLocal(MuUsuarioLocal muUsuarioLocal) {
		this.muUsuarioLocal = muUsuarioLocal;
	}

	public boolean isPwdGenerado() {
		return pwdGenerado;
	}

	public void setPwdGenerado(boolean pwdGenerado) {
		this.pwdGenerado = pwdGenerado;
	}

}