function encryptPassword() {
	var texto = document.getElementById("formLogin:passwordId").value;
	document.getElementById("formLogin:passwordId").value = Base64
			.encode(texto);
}

function FinalizarBO() {
	var ok = document.getElementById("formPanel:completar");
	if (ok != null) {
		if (ok.value == 0) {
			return false;

		} else {
			return true;
		}

	}
	return false;

}

PrimeFaces.locales['es'] = {
	closeText : 'Cerrar',
	prevText : 'Anterior',
	nextText : 'Siguiente',
	monthNames : [ 'Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio',
			'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre',
			'Diciembre' ],
	monthNamesShort : [ 'Ene', 'Feb', 'Mar', 'Abr', 'May', 'Jun', 'Jul', 'Ago',
			'Sep', 'Oct', 'Nov', 'Dic' ],
	dayNames : [ 'Domingo', 'Lunes', 'Martes', 'Miercoles', 'Jueves',
			'Viernes', 'Sabado' ],
	dayNamesShort : [ 'Dom', 'Lun', 'Mar', 'Mie', 'Jue', 'Vie', 'Sab' ],
	dayNamesMin : [ 'D', 'L', 'M', 'X', 'J', 'V', 'S' ],
	weekHeader : 'Semana',
	firstDay : 1,
	isRTL : false,
	showMonthAfterYear : false,
	yearSuffix : '',
	timeOnlyTitle : 'Solo hora',
	timeText : 'Tiempo',
	hourText : 'Hora',
	minuteText : 'Minuto',
	secondText : 'Segundo',
	currentText : 'Fecha actual',
	ampm : false,
	month : 'Mes',
	week : 'Semana',
	day : 'Dia',
	allDayText : 'Todo el dia'
};
