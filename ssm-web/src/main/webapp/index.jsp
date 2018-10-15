<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<body>
<h2>Hello World!</h2>
<div>
    <form method="post" action="/test/login">
        <img src="/web/checkCode" alt="" width="100" height="32" class="passcode" style="height:43px;cursor:pointer;" onclick="this.src=this.src+'?'">
        <input type="text" name="code" >
        <input type="submit" value="确定">
    </form>
</div>
</body>
</html>
