package com.pjt.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.pjt.dao.UserDao;
import com.pjt.md5.MD5;
import com.pjt.pojo.User;

public class LoginServlet extends HttpServlet {

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
		String type = request.getParameter("type");
		String username = request.getParameter("username");
		String password = MD5.getMD5(request.getParameter("password"));
		String action = request.getParameter("action");
		User user = new User();
		user.setUsername(username);
		user.setPassword(password);
		UserDao userdao = new UserDao();
		Gson gson = new Gson();
		if (type.equals("android")) {
			if (action.equals("login")) {
				User u = userdao.getlogin(user);
				String st = gson.toJson(u);
				System.out.println(st);
				out.println(st);

			} else if (action.equals("find")) {
				System.out.println("≤È’“");
				long user_id = Long.parseLong(request.getParameter("user_id"));
				User user1 = userdao.findUser(user_id);
				String st = gson.toJson(user1);
				out.println(st);

			}
		}

		out.flush();
		out.close();
	}

}
