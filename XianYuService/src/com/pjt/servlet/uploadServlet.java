package com.pjt.servlet;


import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;



public class uploadServlet extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
               doPost(request, response);
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		boolean bool=ServletFileUpload.isMultipartContent(request);
			if(bool){
				DiskFileItemFactory factory=new DiskFileItemFactory(10000,new File("/"));
				ServletFileUpload upload=new ServletFileUpload(factory);
				upload.setHeaderEncoding("utf-8");
				try{
	           List<FileItem>list=upload.parseRequest(request);
	           Iterator<FileItem> it=list.iterator();
	            while(it.hasNext()){
	            	FileItem file=it.next();
	            	if(!file.isFormField()){
	            		String path=file.getName();
	            		String filename=path.substring(path.lastIndexOf("\\")+1);
	            		String uploadpath=this.getServletContext().getRealPath("/product");
	            		File upfile=new File(uploadpath);
	            		if(!upfile.exists()){
	            			upfile.mkdir();
	            		}
	            		file.write(new File(uploadpath,filename));
	            		file.delete();
	            	}
	            	
	            }
				}catch(Exception e){
					
				}
		}
		
		out.flush();
		out.close();
	}

}
