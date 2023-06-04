<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page isErrorPage="true" trimDirectiveWhitespaces="true" %>
<%
    if (exception.getClass().getSimpleName().equals("InvalidResourceException")) {
        response.setStatus(400);
    }
    if (exception.getClass().getSimpleName().equals("UnknownIdentifierException")) {
        response.setStatus(400);
    }
%>
<c:choose>
    <c:when test="${header.accept=='application/xml'}">
<% response.setContentType("application/xml"); %>
<?xml version='1.0' encoding='UTF-8'?>
<errors>
   <error>
       <c:set value="<%=exception.getClass().getSimpleName()%>" var="errorMessage"/>
       <type>${fn:escapeXml(errorMessage.replace("Exception", "Error"))}</type>
   </error>
</errors>
</c:when>
    <c:otherwise><% response.setContentType("application/json"); %>{
   "errors": [ {
      "type": <%=exception.getClass().getSimpleName().replace("Exception", "Error")%>
   } ]
}
</c:otherwise>
</c:choose>
