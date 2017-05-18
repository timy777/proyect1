package micrium.user.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import micrium.aes.AlgoritmoAES;

/**
 * The persistent class for the PARAMETRO database table.
 * 
 */
@Entity
@Table(name = "MU_PARAMETRO")
public class MuParametro implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final int TIPO_CADENA = 1;
	public static final int TIPO_FECHA = 2;
	public static final int TIPO_NUMERICO = 3;
	public static final int TIPO_BOOLEANO = 4;
	public static final int TIPO_COLOR = 5;
	//>>>>>>>>>>>>>>>>>>>>>>>>>>
	public static final int TIPO_LISTADO_VALORES_TEXTO = 6;
	public static final int TIPO_LISTADO_VALORES_NUMERICOS = 7;
	//<<<<<<<<<<<<<<<<<<<<<<<<<

	@Id
	@Column(name = "PARAMETRO_ID")
	private int parametroId;

	private String nombre;

	private int tipo;

	private String valorCadena;

	@Temporal(TemporalType.TIMESTAMP)
	private Date valorFecha;

	private BigDecimal valorNumerico;

	private Boolean valorBooleano;

	@Column(name = "DESCRIPCION_CAMPO")
	private String descripcionCampo;

	// uni-directional many-to-one association to MuTipoParametro
	@ManyToOne
	@JoinColumn(name = "MU_TIPO_PARAMETRO_ID")
	private MuTipoParametro tipoParametro;

	public MuParametro() {
	}

	public MuParametro(int parametroId, String nombre, int tipo, String valorCadena) {
		this.parametroId = parametroId;
		this.nombre = nombre;
		this.tipo = tipo;
		this.valorCadena = valorCadena;
	}

	public Integer getParametroId() {
		return this.parametroId;
	}

	public void setParametroId(int parametroId) {
		this.parametroId = parametroId;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getTipo() {
		return this.tipo;
	}

	public void setTipo(int tipo) {
		this.tipo = tipo;
	}

	public String getValorCadena() {
		return aes.desencriptar(this.valorCadena);
	}

	public void setValorCadena(String valorCadena) {
		this.valorCadena = aes.encriptar(valorCadena);
	}

	public Date getValorFecha() {
		return this.valorFecha;
	}

	public void setValorFecha(Date valorFecha) {
		this.valorFecha = valorFecha;
	}

	public BigDecimal getValorNumerico() {
		return this.valorNumerico;
	}

	public void setValorNumerico(BigDecimal valorNumerico) {
		this.valorNumerico = valorNumerico;
	}

	public Boolean getValorBooleano() {
		return valorBooleano;
	}

	public void setValorBooleano(Boolean valorBooleano) {
		this.valorBooleano = valorBooleano;
	}

	public String getDescripcionCampo() {
		return descripcionCampo;
	}

	public void setDescripcionCampo(String descripcionCampo) {
		this.descripcionCampo = descripcionCampo;
	}

	public MuTipoParametro getTipoParametro() {
		return tipoParametro;
	}

	public void setTipoParametro(MuTipoParametro tipoParametro) {
		this.tipoParametro = tipoParametro;
	}

	@Override
	public String toString() {
		return "Parametro [parametroId=" + parametroId + ", nombre=" + nombre + ", tipo=" + tipo + ", valorCadena=" + valorCadena + ", valorFecha=" + valorFecha + ", valorNumerico=" + valorNumerico + ", valorBooleano=" + valorBooleano + ", descripcionCampo=" + descripcionCampo + ", tipoParametro=" + tipoParametro + "]";
	}

	@Transient
	private AlgoritmoAES aes = new AlgoritmoAES();

}