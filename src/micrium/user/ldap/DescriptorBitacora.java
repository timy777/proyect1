package micrium.user.ldap;

public enum DescriptorBitacora {
	// Parametro
	FORMULARIO("GESTOR FORMULARIO"),

	// Parametro
	PARAMETRO("GESTOR PARAMETRO"),

	// usuario
	USUARIO("GESTOR USUARIO"),

	// grupo
	GRUPO("GESTOR GRUPO"),

	// rol
	ROL("ROL"),

	// gestion de plan
	GESTION_DE_PLAN("Gestión de plan"),

	// solicitar promocion
	SOLICITAR_PROMOCION("Solicitar promoción"),
	
	// gestion de regalo
	GESTION_DE_REGALO("Gestion de regalo"),
	
	// gestion de prodcuto
	GESTION_DE_PRODUCTO("Gestion de producto"),
	
	USUARIO_LOCAL("GESTOR USUARIO LOCAL");

	private String formulario;

	DescriptorBitacora(String formulario) {
		this.formulario = formulario;
	}

	public String getFormulario() {
		return formulario;
	}

}
