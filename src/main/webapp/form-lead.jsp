<jsp:directive.page contentType="text/html; charset=UTF-8" />
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <%@ include file="base-head.jsp" %>
</head>
<body>
    <%@ include file="nav-menu.jsp" %>

    <div id="container" class="container-fluid">
        <h3 class="page-header">Adicionar Lead</h3>

        <form action="${pageContext.request.contextPath}/lead/${action}" method="POST">
            <input type="hidden" value="${lead.getId()}" name="leadId">
            <div class="row">
                <div class="form-group col-md-4">
                    <label for="name">Nome</label>
                    <input type="text" class="form-control" id="name" name="name"
                           autofocus="autofocus" placeholder="Nome do lead"
                           required oninvalid="this.setCustomValidity('Por favor, informe o nome do lead.')"
                           oninput="setCustomValidity('')"
                           value="${lead.getName()}">
                </div>

                <div class="form-group col-md-4">
                    <label for="email">E-mail</label>
                    <input type="email" class="form-control" id="email" name="email"
                           placeholder="E-mail do lead"
                           required oninvalid="this.setCustomValidity('Por favor, informe o e-mail do lead.')"
                           oninput="setCustomValidity('')"
                           value="${lead.getEmail()}">
                </div>

                <div class="form-group col-md-4">
                    <label for="phone">Telefone</label>
                    <input type="text" class="form-control" id="phone" name="phone"
                           placeholder="Telefone do lead"
                           required oninvalid="this.setCustomValidity('Por favor, informe o telefone do lead.')"
                           oninput="setCustomValidity('')"
                           value="${lead.getPhone()}">
                </div>
            </div>

            <div class="row">
                <div class="form-group col-md-4">
                    <label for="status">Status</label>
                    <select id="status" class="form-control selectpicker" name="status"
                            required oninvalid="this.setCustomValidity('Por favor, selecione o status do lead.')"
                            oninput="setCustomValidity('')">
                        <option value="" disabled ${not empty lead ? "" : "selected"}>Selecione o status</option>
                        <option value="Ativo" ${lead.getStatus() == 'Ativo' ? "selected" : ""}>Ativo</option>
                        <option value="Inativo" ${lead.getStatus() == 'Inativo' ? "selected" : ""}>Inativo</option>
                    </select>
                </div>

                <div class="form-group col-md-4">
                    <label for="userId">ID do Usuário</label>
                    <select id="userId" class="form-control" name="userId"
                            required oninvalid="this.setCustomValidity('Por favor, selecione o ID do usuário.')"
                            oninput="setCustomValidity('')">
                        <option value="" disabled ${not empty lead ? "" : "selected"}>Selecione o ID do usuário</option>
                        <c:forEach var="user" items="${users}">
                            <option value="${user.getId()}" ${lead.getUserId() == user.getId() ? "selected" : ""}>${user.getId()}</option>
                        </c:forEach>
                    </select>
                </div>
            </div>

            <hr />

            <div id="actions" class="row pull-right">
                <div class="col-md-12">
                    <a href="${pageContext.request.contextPath}/leads" class="btn btn-default">Cancelar</a>
                    <button type="submit" class="btn btn-primary">${not empty lead ? "Alterar Lead" : "Cadastrar Lead"}</button>
                </div>
            </div>
        </form>
    </div>

    <script src="js/jquery.min.js"></script>
    <script src="js/bootstrap.min.js"></script>
</body>
</html>