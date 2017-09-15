package com.pjt.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.pjt.dao.CateGoryDao;
import com.pjt.pojo.CateGory;

public class CateGroyServlet extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		CateGoryDao cgdao = new CateGoryDao();
		ArrayList<CateGory> list = cgdao.findCate();
		String type=request.getParameter("type");
		if(type.equals("android")){
		Gson gson = new Gson();
		String st = gson.toJson(list);
		out.println(st);
		
		}

		out.flush();
		out.close();
	}

}
