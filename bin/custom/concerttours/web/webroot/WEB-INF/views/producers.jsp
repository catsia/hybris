<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">

<head>
	<title>producers</title>
</head>
<body>
<table border="1px">
    <thead>
      <tr>
        <td>Code</td>
        <td>Name</td>
      </tr>
    </thead>
    <c:forEach begin="0" end="${producers.size() - 1}" varStatus="loop">
          <tr>
            <td>
              ${producers[loop.index].code}
            </td>
            <td>
              ${producers[loop.index].firstName} ${producers[loop.index].lastName}
            </td>
          </tr>
        </c:forEach>
</table>
</body>

</html>
