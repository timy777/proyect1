<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<ui:composition xmlns="http://www.w3.org/1999/xhtml"
	xmlns:h="http://java.sun.com/jsf/html"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:ui="http://java.sun.com/jsf/facelets"
	template="./../resources/templates/template_login.xhtml">

	<ui:define name="content">

		<center>
			<h1>¡ Página no encontrada !</h1>
			<br /> <br /> <br /> <img id="deleteImage"
				src="<%out.write(request.getContextPath());%>/resources/icons/404.png"
				alt="Error" />
		</center>

	</ui:define>

</ui:composition>
