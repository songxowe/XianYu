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
import com.pjt.pojo.ClickLike;
import com.pjt.pojo.Comment;
import com.pjt.pojo.Product;

public class ProductServlet extends HttpServlet {

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
		ProductDao pd = new ProductDao();
		CommentDao commentdao = new CommentDao();
		UserDao userdao=new UserDao();
		ClickLikeDao cldao = new ClickLikeDao();
		String type = request.getParameter("type");
		String action = request.getParameter("action");
		Gson gson = new Gson();
		if (type.equals("android")) {
			if (action.equals("findall")) {
				int pageno = Integer.parseInt(request.getParameter("pageno"));
				ArrayList<Product> list = pd.findAll(pageno);
				for (Product p : list) {
					p.setComment(pd.findCommentCount(p.getId()));
					p.setGood(pd.findGoodCount(p.getId()));
					p.setBitsurl(pd.getImgUrl(p.getId()));
				}
				String st = gson.toJson(list);
				out.println(st);
			} else if (action.equals("findById")) {
				long pid = Long.parseLong(request.getParameter("pid"));
				Product p = pd.findById(pid);

				p.setComment(pd.findCommentCount(p.getId()));
				p.setGood(pd.findGoodCount(p.getId()));
				p.setBitsurl(pd.getImgUrl(p.getId()));

				String st = gson.toJson(p);
				out.println(st);

			} else if (action.equals("findComment")) {
				long pid = Long.parseLong(request.getParameter("pid"));
				ArrayList<Comment> list = commentdao.findByPid(pid);
			    for(Comment comment:list){
			    	if(comment.getTo_user_id()!=0){
			    	comment.setTo_username(userdao.findUser(comment.getTo_user_id()).getUsername());
			    	}
			    }
				String st = gson.toJson(list);
				out.println(st);

			} else if (action.equals("addComment")) {
				String text = request.getParameter("text");
				long user_id = Long.parseLong(request.getParameter("user_id"));
				long to_user_id = Long.parseLong(request
						.getParameter("to_user_id"));
				long pid = Long.parseLong(request.getParameter("pid"));
				Comment comment = new Comment();
				comment.setText(text);
				comment.setUser_id(user_id);
				comment.setTo_user_id(to_user_id);
				comment.setProduct_id(pid);
				if (commentdao.addComment(comment)) {
					out.println("ok");
				} else {
					out.println("no");
				}

			} else if (action.equals("findByTuijian")) {
				long category_id = Long.parseLong(request
						.getParameter("category_id"));
				long pid = Long.parseLong(request.getParameter("pid"));
				ArrayList<Product> list = pd.findTuijian(category_id, pid);
				for (Product p : list) {
					p.setBitsurl(pd.getImgUrl(p.getId()));
					p.setComment(pd.findCommentCount(p.getId()));
					p.setGood(pd.findGoodCount(p.getId()));
				}
				String st = gson.toJson(list);
				out.println(st);

			} else if (action.equals("clickLike")) {
				long pid = Long.parseLong(request.getParameter("pid"));
				long user_id = Long.parseLong(request.getParameter("user_id"));
				ClickLike cl = new ClickLike();
				cl.setProduct_id(pid);
				cl.setUser_id(user_id);

				boolean bool = cldao.addLike(cl);
				if (bool) {
					out.println("ok");
				} else {
					out.println("no");
				}

			} else if (action.equals("findLike")) {
				long pid = Long.parseLong(request.getParameter("pid"));
				long user_id = Long.parseLong(request.getParameter("user_id"));
				int count = cldao.findLike(pid, user_id);
				if (count > 0) {
					out.println("ok");
				} else {
					out.println("no");
				}

			} else if (action.equals("removeLike")) {
				long pid = Long.parseLong(request.getParameter("pid"));
				long user_id = Long.parseLong(request.getParameter("user_id"));
				if (cldao.removeLike(pid, user_id)) {
					out.println("ok");
				} else {
					out.println("no");
				}

			} else if (action.equals("addProduct")) {
				
				Product product = new Product();
				String imgurl = request.getParameter("imgurl");
			
				
				String[] imgs = imgurl.split("\\*");
		
				ArrayList<String> urls = new ArrayList<String>();
				for (String st : imgs) {
					urls.add(st);
				}
				product.setBitsurl(urls);
				String title = request.getParameter("title");
				String info = request.getParameter("info");
				long user_id = Long.parseLong(request.getParameter("user_id"));
				double price = Double
						.parseDouble(request.getParameter("price"));
				String priceInfo = request.getParameter("priceinfo");
				long yutang_Id = Long.parseLong(request
						.getParameter("yutang_id"));
				long cate_id = Long.parseLong(request.getParameter("cate_id"));

				product.setTitle(title);
				product.setInfo(info);
				product.setUser_id(user_id);
				product.setPrice(price);
				product.setPriceinfo(priceInfo);
				product.setYutang_id(yutang_Id);
				product.setCategory_id(cate_id);
				boolean bool = pd.addProduct(product);
			
				if(bool){
					out.println("ok");
				}

			}else if(action.equals("findByCate")){
				long cate_id=Long.parseLong(request.getParameter("cate_id"));
				if(cate_id!=0){
				ArrayList<Product>list=pd.findByCate(cate_id);
				for(Product p:list){
					p.setBitsurl(pd.getImgUrl(p.getId()));
				}
				
				String st=gson.toJson(list);
				out.println(st);
				}else{
					ArrayList<Product>list=pd.findAll();
					String st=gson.toJson(list);
					out.println(st);
				}
			}else if(action.equals("findByYt")){
				long yutang_id=Long.parseLong(request.getParameter("yutang_id"));
				
				ArrayList<Product> list = pd.findByYuTang(yutang_id);
				for (Product p : list) {
					p.setComment(pd.findCommentCount(p.getId()));
					p.setGood(pd.findGoodCount(p.getId()));
					p.setBitsurl(pd.getImgUrl(p.getId()));
				}
				String st = gson.toJson(list);
				out.println(st);
				
			}
		}

		out.flush();
		out.close();
	}

}
