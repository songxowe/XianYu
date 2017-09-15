package com.pjt.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.pjt.dao.ClickLikeDao;
import com.pjt.dao.CommentDao;
import com.pjt.dao.ProductDao;
import com.pjt.dao.UserDao;
import com.pjt.md5.MD5;
import com.pjt.pojo.ClickLike;
import com.pjt.pojo.Comment;
import com.pjt.pojo.Product;
import com.pjt.pojo.User;

public class UserServlet extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		String type = request.getParameter("type");
		String action = request.getParameter("action");
		Gson gson = new Gson();
		UserDao userdao = new UserDao();
		ClickLikeDao cldao=new ClickLikeDao();
		CommentDao cddao=new CommentDao();
		ProductDao pd = new ProductDao();
		if (type.equals("android")) {
			if (action.equals("findUser")) {
				long id = Long.parseLong(request.getParameter("user_id"));
				User user = userdao.findUser(id);
				user.setCommentcount(userdao.findCommentCount(id));
				String st = gson.toJson(user);
				out.println(st);
			} else if (action.endsWith("userProduct")) {
				long id = Long.parseLong(request.getParameter("user_id"));
				ArrayList<Product> list = pd.findByUser(id);
				for (Product p : list) {
					p.setComment(pd.findCommentCount(p.getId()));
					p.setGood(pd.findGoodCount(p.getId()));
					p.setBitsurl(pd.getImgUrl(p.getId()));
				}
				String st = gson.toJson(list);
				out.println(st);
			} else if (action.equals("findClickPerson")) {
				long product_id = Long.parseLong(request
						.getParameter("product_id"));
				ArrayList<User> list = userdao.findClickPerson(product_id);
				String st = gson.toJson(list);
				out.println(st);

			} else if (action.equals("ishas")) {
				String phone = request.getParameter("phone");
				if (userdao.isHas(phone)) {
					// Õâ¸öºÅÂë´æÔÚ

					out.println("yes");

				} else {
					// Õâ¸öºÅÂëÎ´±»×¢²á
					out.println("no");

				}

			}else if(action.equals("regist")){
				String phone=request.getParameter("phone");
			
				String pass=MD5.getMD5((request.getParameter("pass")));
				User user=new User();
				user.setPhone(phone);
				user.setPassword(pass);
				user.setImg("user_img.png");
				user.setAddress("³¤É³");
				user.setUsername("admin");
				user.setSex("ÄÐ");
				if(userdao.addUser(user)){
					 //×¢²á³É¹¦
					out.println("yes");
					
				}else{
					//×¢²áÊ§°Ü
					out.println("no");
				}
				
			}else if(action.equals("findMyGood")){
				long user_id=Long.parseLong(request.getParameter("user_id"));
				ArrayList<ClickLike>list=cldao.findByUser(user_id);
				for(ClickLike like:list){
					User user=userdao.findUser(like.getUser_id());
					like.setFromUsername(user.getUsername());
					like.setFromUserImg(user.getImg());
					like.setP_img(pd.getImgUrl(like.getProduct_id()).get(0));
				}
				String st=gson.toJson(list);
				out.println(st);
			}else if(action.equals("findMyComment")){
				long user_id=Long.parseLong(request.getParameter("user_id"));
				
				ArrayList<Comment>list=cddao.findMyComment(user_id);
				for(Comment comment:list){
					User user=userdao.findUser(comment.getUser_id());
					comment.setUsername(user.getUsername());
					comment.setP_img(pd.getImgUrl(comment.getProduct_id()).get(0));
				}
				String st=gson.toJson(list);
				out.println(st);
				
			}else if(action.equals("findMaxGood")){
				long user_id=Long.parseLong(request.getParameter("user_id"));
				long zan_id=Long.parseLong(request.getParameter("zan_id"));
				long max_id=cldao.findMaxId(user_id);
				if(zan_id<max_id){
					out.println(max_id+"");
				}
				
			}else if(action.equals("findMaxComment")){
				long user_id=Long.parseLong(request.getParameter("user_id"));
				long zan_id=Long.parseLong(request.getParameter("comment_id"));
				long max_id=cddao.findMaxId(user_id);
				if(zan_id<max_id){
					out.println(max_id+"");
				}
				
			}
		}
		out.flush();
		out.close();
	}

}
