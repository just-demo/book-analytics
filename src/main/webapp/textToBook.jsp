<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Book reader</title>
<script type="text/javascript">
</script>
</head>
<body>
	<form action="rest/book/textToBook" target="_blank" method="POST">
	    <textarea name="text" rows="20" cols="100"></textarea>
	    <button type="submit" name="submit" value="true">Submit</button>
	</form>
</body>
</html>
