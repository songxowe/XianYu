package com.pjt.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.pjt.pojo.Comment;

public class CommentDao {
	Connection conn = null;
	PreparedStatement ps = null;
	ResultSet rs = null;
	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public ArrayList<Comment> findByPid(long pid) {
		ArrayList<Comment> list = new ArrayList<Comment>();
		try {
			conn = DBUtil.getConnection();
			String sql = "select c.id, c.text, c.user_id, c.to_user_id, c.product_id, "
					+ "to_char(c.time,'yyyy-mm-dd HH24:mi:ss'), u.username, u.imgurl  from xianyu_product_comment c, xianyu_user u where "
					+ " c.user_id=u.id and " + "product_id=? ";
			ps = conn.prepareStatement(sql);
			ps.setLong(1, pid);
			rs = ps.executeQuery();
			while (rs.next()) {
				Comment comment = new Comment();
				comment.setId(rs.getLong(1));
				comment.setText(rs.getString(2));
				comment.setUser_id(rs.getLong(3));
				comment.setTo_user_id(rs.getLong(4));
				comment.setProduct_id(rs.getLong(5));
				Date date = df.parse(rs.getString(6));
				long time = System.currentTimeMillis() - date.getTime();
				long day = time / (24 * 60 * 60 * 1000);

				if (day > 0) {
					comment.setTime(day + "天前");
				} else {
					long h = time / (1000 * 60 * 60);
					if (h > 0) {
						comment.setTime(h + "小时前");
					} else {
						h = time / (1000 * 60);
						comment.setTime(h + "分钟前");
					}
				}
				comment.setUsername(rs.getString(7));
				comment.setUserimg(rs.getString(8));
				list.add(comment);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(conn, ps, rs);
		}

		return list;
	}

	public boolean addComment(Comment comment) {
		boolean bool = false;
		try {
			conn = DBUtil.getConnection();
			String sql = "insert into xianyu_product_comment"
					+ "(id,text,user_id,to_user_id,product_id,time) ";
			sql += "  values(seq_xianyu_product_comment.nextval,?,?,?,?,sysdate)";
			ps = conn.prepareStatement(sql);
			ps.setString(1, comment.getText());
			ps.setLong(2, comment.getUser_id());
			ps.setLong(3, comment.getTo_user_id());
			ps.setLong(4, comment.getProduct_id());
			int count = ps.executeUpdate();
			if (count > 0) {
				bool = true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(conn, ps, rs);
		}

		return bool;
	}

	public ArrayList<Comment> findMyComment(long user_id) {
		ArrayList<Comment> list = new ArrayList<Comment>();
		try {
			conn = DBUtil.getConnection();
			String sql = " select c.id, c.user_id,c.text, c.product_id , to_char(c.time,'yyyy-mm-dd hh24:mi:ss') from xianyu_product_comment c ,"
					+ "  xianyu_product p where c.product_id= p.id"
					+ "  and p.user_id=? and c.user_id!=? order by c.id";
			ps = conn.prepareStatement(sql);
			ps.setLong(1, user_id);
			ps.setLong(2, user_id);

			rs = ps.executeQuery();
			while (rs.next()) {
				Comment comment = new Comment();
				comment.setId(rs.getLong(1));
				comment.setUser_id(rs.getLong(2));
				comment.setText(rs.getString(3));
				comment.setProduct_id(rs.getLong(4));
				Date date = df.parse(rs.getString(5));
				long time = System.currentTimeMillis() - date.getTime();
				long day = time / (24 * 60 * 60 * 1000);

				if (day > 0) {
					comment.setTime(day + "天前");
				} else {
					long h = time / (1000 * 60 * 60);
					if (h > 0) {
						comment.setTime(h + "小时前");
					} else {
						h = time / (1000 * 60);
						comment.setTime(h + "分钟前");
					}
				}

				list.add(comment);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(conn, ps, rs);
		}

		return list;

	}

	public long findMaxId(long user_id) {
		long max_id = 0;
		try {
			conn = DBUtil.getConnection();
			String sql = " select max(c.id) from xianyu_product_comment c, xianyu_product p where c.product_id= p.id "
					+ " and p.user_id=? and c.user_id!=? order by c.id ";

			ps = conn.prepareStatement(sql);
			ps.setLong(1, user_id);
			ps.setLong(2, user_id);
			rs = ps.executeQuery();
			if (rs.next()) {
				max_id = rs.getLong(1);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(conn, ps, rs);
		}

		return max_id;
	}

}
