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
        <td>Image</td>
      </tr>
    </thead>
    <c:forEach begin="0" end="${bands.size() - 1}" varStatus="loop">
      <tr>
        <td>
          ${bands[loop.index].id}
        </td>
        <td>
          ${bands[loop.index].name}
        </td>
        <td>
          ${bands[loop.index].albumsSold}
        </td>
        <td>
          ${bands[loop.index].description}
        </td>
        <td><img src="${bands[loop.index].imageURL}" /></td>
      </tr>
    </c:forEach>
</table>
</body>

</html>
