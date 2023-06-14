<html>
<head><title>Contact Request</title></head>
<body>
   <form method="post">
       <label for="login">Sender: </label>
       <input type="text" name="newSender" value="${contactRequest.sender}"/>
       <br/>
       <label for="password">Message:</label>
       <textarea name="newMessage">${contactRequest.message}</textarea>
       <br/>
       <input type="submit" value="Send"/>
   </form>
</body>
</html>