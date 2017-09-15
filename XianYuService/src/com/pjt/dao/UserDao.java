package com.pjt.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.pjt.pojo.User;

public class UserDao {
	private Connection conn = null;
	private PreparedStatement ps = null;
	private ResultSet rs = null;

	public User getlogin(User us) {
		User user = new User();
		try {
			conn = DBUtil.getConnection();
			String sql = "select * from xianyu_user "
					+ "where (username=? and password=?) or  "
					+ " (phone=? and password=? ) ";

			ps = conn.prepareStatement(sql);
			ps.setString(1, us.getUsername());
			ps.setString(2, us.getPassword());
			ps.setString(3, us.getUsername());
			ps.setString(4, us.getPassword());
			rs = ps.executeQuery();
			if (rs.next()) {
				user.setId(rs.getLong(1));
				user.setUsername(rs.getString(2));
				user.setSex(rs.getString(4));
				user.setAddress(rs.getString(5));
				user.setPhone(rs.getString(6));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(conn, ps, rs);
		}

		return user;
	}

	public User findUser(long id) {
		User user = new User();
		conn = DBUtil.getConnection();
		try {
			String sql = "select * from xianyu_user where id=? ";
			ps = conn.prepareStatement(sql);
			ps.setLong(1, id);

			rs = ps.executeQuery();
			if (rs.next()) {
				user.setId(rs.getLong(1));
				user.setUsername(rs.getString(2));
				user.setSex(rs.getString(4));
				user.setAddress(rs.getString(5));
				user.setPhone(rs.getString(6));
				user.setImg(rs.getString(7));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(conn, ps, rs);
		}

		return user;
	}

	public long findCommentCount(long user_id) {
		long count = 0;
		try {
			conn = DBUtil.getConnection();
			String sql = "select count(*) from (select * from xianyu_product where user_id=?)p ";
			sql += " ,(select * from xianyu_product_comment)c where p.id=c.product_id  ";
			ps = conn.prepareStatement(sql);
			ps.setLong(1, user_id);

			rs = ps.executeQuery();
			if (rs.next()) {
				count = rs.getLong(1);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(conn, ps, rs);
		}

		return count;
	}

	public ArrayList<User> findClickPerson(long product_id) {
		ArrayList<User> list = new ArrayList<User>();
		try {
			conn = DBUtil.getConnection();
			String sql = "select u.id,u.imgurl from xianyu_product_good g ,xianyu_user u  ";
			sql += " where g.user_id=u.id and g.product_id=? ";
			ps = conn.prepareStatement(sql);
			ps.setLong(1, product_id);
			rs = ps.executeQuery();
			while (rs.next()) {
				User user = new User();
				user.setId(rs.getLong(1));
				user.setImg(rs.getString(2));
				list.add(user);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(conn, ps, rs);
		}

		return list;
	}

	public boolean isHas(String phone) {

		boolean bool = false;
		try {
			conn = DBUtil.getConnection();
			String sql = " select count(*) from xianyu_user  where phone=? ";
			ps = conn.prepareStatement(sql);
			ps.setString(1, phone);
			rs = ps.executeQuery();
			if (rs.next()) {
				int count = rs.getInt(1);
				if (count > 0) {
					bool = true;
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(conn, ps, rs);
		}

		return bool;
	}

	public boolean addUser(User user) {
		boolean bool = false;
		try {
			conn = DBUtil.getConnection();
			String sql = " insert into xianyu_user(id,username,password,sex,address,phone,imgurl) "
					+ " values(seq_xianyu_user.nextval,?,?,?,?,?,?)";
			ps = conn.prepareStatement(sql);
			ps.setString(1, user.getUsername());
			ps.setString(2, user.getPassword());
			ps.setString(3, user.getSex());
			ps.setString(4, user.getAddress());
			ps.setString(5, user.getPhone());
			ps.setString(6, user.getImg());
			

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

}
