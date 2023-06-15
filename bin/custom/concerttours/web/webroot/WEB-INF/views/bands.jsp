<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.List" %>
<%@ page import="concerttours.model.BandModel" %>
<!DOCTYPE html>
<html lang="en">

<head>
	<title>Bands</title>
</head>
<body>
<table border="1px">
    <thead>
      <tr>
        <td>Code</td>
        <td>Name</td>
        <td>Album sales</td>
        <td>History</td>
      </tr>
    </thead>
    <c:forEach begin="0" end="${bands.size()}" varStatus="loop">
      <tr>
        <td>
          ${bands[loop.index].code}
        </td>
        <td>
          ${bands[loop.index].name}
        </td>
        <td>
          ${bands[loop.index].albumSales}
        </td>
        <td>
          ${bands[loop.index].history}
        </td>
      </tr>
    </c:forEach>
</table>
</body>

</html>
