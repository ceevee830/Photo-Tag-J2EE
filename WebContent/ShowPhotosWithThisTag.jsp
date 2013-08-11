<%@ page import="java.util.Set, java.io.File, com.obs.photo.PhotoFile, com.obs.photo.PhotoTag,com.obs.photo.PhotoDAO" language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Show Photos With This Tag</title>
<style type="text/css">
	@import url("style.css");
</style>
</head>
<body>

Photos with this tag <%=request.getParameter("tag")%>:

<%
PhotoDAO dao = new PhotoDAO();
Set<PhotoFile> files = dao.getPhotoFiles(request.getParameter("tag"));
%>
size = <%= files.size() %>
<%

for (PhotoFile file : files)
{
   String file2 = "images/" + file.getFileName();
   String title = file.getFileName().substring(file.getFileName().lastIndexOf("\\")+1);
   
   String tags = "";
   Set<PhotoTag> tagsOnThisPhoto = file.getTags();
   for (PhotoTag tag : tagsOnThisPhoto)
   {
      if (tags.length() > 0)
      {
         tags += ", ";
      }
      
      tags += tag.getTag();
   }
   
   
%>
<h3><%=title%></h3>
&#160;&#160;&#160;&#160;&#160;&#160;<img src="<%=file2 %>" alt="<%=file2 %>" title="<%=file.getFileName() %>&#13;Tags: <%= tags %>">
<%
}
%>

</body>
</html>