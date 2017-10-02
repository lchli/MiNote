<#-- @ftlvariable name="error" type="java.lang.String" -->
<#-- @ftlvariable name="userId" type="java.lang.String" -->

<form class="pure-form-stacked" action="/login" method="post" enctype="application/x-www-form-urlencoded">
    <#if error??>
        <p class="error">${error}</p>
    </#if>

    <label for="userId">Login
        <input type="text" name="userName" id="userName" value="${userId}">
    </label>


    <label for="password">Password
        <input type="password" name="userPwd" id="userPwd">
    </label>

    <input class="pure-button pure-button-primary" type="submit" value="Login">
</form>
