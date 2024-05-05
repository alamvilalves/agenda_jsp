<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	
%>

<!DOCTYPE html>
<html lang="pt-br">
<head>
<meta charset="UTF-8">
<title>Agenda de Contatos</title>
<link rel="icon" href="imagens/favicon.png">
<link rel="stylesheet" href="style.css">
<script src="scripts/validador.js"></script>
</head>
<body>
	<h1>Editar contato</h1>
	<form name="frmContato" action="update">
		<table>
			<tr>
				<td>
					<input type="text" value="<% out.print(request.getAttribute("idcon")); %>" id="Caixa3" readonly>
				</td>
			</tr>
			<tr>
				<td>
					<input type="text" value="<% out.print(request.getAttribute("nome")); %>" name="nome" class="Caixa1">
				</td>
			</tr>
			<tr>
				<td>
					<input type="text" value="<%= request.getAttribute("fone") %>" name="fone" class="Caixa2">
				</td>
			</tr>
			<tr>
				<td>
					<input type="text" value="<%= request.getAttribute("email") %>" name="email" class="Caixa1">
				</td>
			</tr>
			<tr>
				<td>
					<input type="button" class="Botao1" value="Salvar" onclick="validar()">
				</td>
			</tr>
		</table>
	</form>
	
</body>
</html>