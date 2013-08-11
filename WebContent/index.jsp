<%@ page import="java.util.List, com.obs.photo.PhotoFile, com.obs.photo.PhotoTag, com.obs.photo.PhotoDAO" language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<title>CV's Photo Collection</title>

<style type="text/css">
	@import url("style.css");
</style>

<script type="text/javascript" language="javascript">


	function addRow(tableID) 
	{
		var table = document.getElementById(tableID);
		var rowCount = table.rows.length;
		var row = table.insertRow(rowCount);

		row.insertCell(0);
		row.insertCell(1);
		row.insertCell(2);
		row.insertCell(3);
		row.insertCell(4);
	}

	function displayResult() {
		var table = document.getElementById("box-table-b");
		var row = table.insertRow(0);
		var cell1 = row.insertCell(0);
		var cell2 = row.insertCell(1);
		cell1.innerHTML = "New";
		cell2.innerHTML = "New";
	}
	
	function setCell(row, col, text)
	{
		try
		{
			document.getElementById("box-table-b").getElementsByTagName('tr')[row].getElementsByTagName('td')[col].innerHTML = text;
		}
		catch (e)
		{
			alert(e);
		}
	}

	function setLink(row, col, link, text)
	{
		try
		{
			document.getElementById("box-table-b").getElementsByTagName('tr')[row].getElementsByTagName('td')[col].innerHTML = '<a href="ShowPhotosWithThisTag.jsp?tag='+link+'">' + text + '</a>';
		}
		catch (e)
		{
			alert(e);
		}
	}

	function insertRow(row) {
		var table = document.getElementById("box-table-b");
		var row = table.insertRow(row);
	}

	function addCell(row, col, text) 
	{
		try
		{
			var table = document.getElementById("box-table-b");
			var row = table.getRow(row);
			var cell = row.getCell(col);
			cell.innerHTML = text;
		}
		catch (e)
		{
			alert(e);
		}
	}
</script>

</head>
<body>

<!-- 
Files in the Database:<br>

<%
	PhotoDAO dao = new PhotoDAO();
	List<PhotoFile> files = dao.getAllPhotoFiles();
	for (PhotoFile file : files)
	{
%>
		<li>&#160;&#160;&#160;<%=file.getFileName()%><li>
<%
	}
%>
 -->
 
<br>Select a tag<br>

<!-- Table markup-->

<table id="box-table-b" summary="">
</table>

<!-- 
<button type="button" onclick="addRow('box-table-b')">Insert new row</button>
<button type="button" onclick="setCell(1, 1, 'hey11')">Add Text11</button>
<button type="button" onclick="setLink(1, 2, 'ShowPhotosWithThisTag.jsp?tag=Anita%20Woodson', 'Anita12')">Anita12</button>
-->

   <script>
	   addRow('box-table-b');
   </script>

<%
final int COLS_IN_TABLE = 5;
int rowCounter = 0;
int colCounter = 0;
List<PhotoTag> tags = dao.getAllTags();
for (PhotoTag tag : tags)
{
   if (colCounter == COLS_IN_TABLE)
   {
      rowCounter++;
      
%>
   <script>
	   addRow('box-table-b');
   </script>
<%      
      
      colCounter=0;
   }
   
   String tagToDisplay = tag.getTag().replace("'", "");

%>

   
<script>
	setLink(<%= rowCounter %>, <%= colCounter++ %>, '<%=tagToDisplay%>', '<%=tagToDisplay%> (<%= tag.getPhotoFiles().size() %>)');
</script>

<!--      	
<li>&#160;&#160;&#160;<a href="ShowPhotosWithThisTag.jsp?tag=<%=tag.getTag()%>"><%=tag.getTag()%></a></li>
-->
<%
}
%>

</body>
</html>