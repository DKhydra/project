<%--
  Created by IntelliJ IDEA.
  User: 15802
  Date: 2017/12/22
  Time: 11:07
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>用户上传证照</title>
</head>
<body>
<h2>用户上传证照</h2>
<form action="/upload/register" enctype="multipart/form-data" method="post">
    <table>
        <tr>
            <td>用户名:</td>
            <td><input type="text" name="username"></td>
        </tr>
        <tr>
            <td>请上传头像:</td>
            <td>

                file 1 : <input type="file" name="files"><br />
                file 2 : <input type="file" name="files"><br />
                file 3 : <input type="file" name="files"><br />
                <input type="text" name="id">
            </td>
        </tr>
        <tr>
            <td><input type="submit" value="上传"></td>
        </tr>
    </table>
</form>
</body>
</html>