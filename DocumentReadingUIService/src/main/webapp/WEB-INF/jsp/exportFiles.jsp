<%
if(request.getParameter("docId") != null){
	String path = "/home/common/documents/invoices/" + request.getParameter("docId") + ".png";
	response.setContentType("image/png");
	java.io.BufferedOutputStream bufferedOutputStream = new java.io.BufferedOutputStream(response.getOutputStream());
	java.io.FileInputStream fileInputStream = new java.io.FileInputStream(path);
	int len;
	byte[] buffer = new byte[1024];
	
	while((len=fileInputStream.read(buffer)) > 0){
		bufferedOutputStream.write(buffer, 0, len);
	}
	fileInputStream.close();
	bufferedOutputStream.flush();
	bufferedOutputStream.close();
}
%>