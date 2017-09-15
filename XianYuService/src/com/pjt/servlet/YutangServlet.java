package com.pjt.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.pjt.dao.YuTangDao;
import com.pjt.pojo.YuTang;
import com.pjt.pojo.YuTangCate;

public class YutangServlet extends HttpServlet {

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
		String action = request.getParameter("action");
		Gson gson = new Gson();
		YuTangDao ytdao = new YuTangDao();
		if (type.equals("android")) {
			if (action.equals("findMyYutang")) {
				long user_id = Long.parseLong(request.getParameter("user_id"));
				ArrayList<YuTang> list = ytdao.findMyYutang(user_id);
				for (YuTang y : list) {
					y.setPopularity(ytdao.findPopCount(y.getYutang_id()));
				}
				String st = gson.toJson(list);
				out.println(st);

			} else if (action.equals("findProximity")) {
				ArrayList<YuTang> list = ytdao.findAll();
				for (YuTang y : list) {
					y.setPopularity(ytdao.findPopCount(y.getYutang_id()));
					y.setPost_num(ytdao.findPostNum(y.getYutang_id()));
				}

				String st = gson.toJson(list);
				out.println(st);

			} else if (action.equals("findAll")) {
				String[] titles = new String[] { "不可错过的鱼塘", "每日精选", "每日最新",
						"壕的世界", "高冷地带" };
				ArrayList<YuTangCate> cate_list = new ArrayList<YuTangCate>();
				for (int i = 0; i < 5; i++) {
					ArrayList<YuTang> list = ytdao.findByCate(i + 1);
					YuTangCate yutangcate = new YuTangCate();
					yutangcate.setTitle(titles[i]);
					yutangcate.setList(list);
					cate_list.add(yutangcate);
				}
				String st = gson.toJson(cate_list);
				out.println(st);
				System.out.println("asd");

			} else if (action.equals("isAddYuTang")) {
				long user_id = Long.parseLong(request.getParameter("user_id"));
				long yutang_id = Long.parseLong(request
						.getParameter("yutang_id"));

				if (ytdao.isAdd(yutang_id, user_id)) {
					out.println("yes");
				} else {
					out.println("no");
				}

			} else if (action.equals("findInfo")) {
				long yutang_id = Long.parseLong(request
						.getParameter("yutang_id"));
				YuTang yt = ytdao.findById(yutang_id);
				yt.setPopularity(ytdao.findPopCount(yt.getYutang_id()));
				yt.setPost_num(ytdao.findPostNum(yt.getYutang_id()));
				String st = gson.toJson(yt);
				out.println(st);
			} else if (action.equals("addYuTang")) {
				long yutang_id = Long.parseLong(request
						.getParameter("yutang_id"));
				long user_id = Long.parseLong(request.getParameter("user_id"));
				if (ytdao.addYuTangUser(user_id, yutang_id)) {
					out.println("yes");

				} else {
					out.println("no");
				}

			}

		}

		out.flush();
		out.close();
	}

}
