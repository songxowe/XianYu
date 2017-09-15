package com.pjt.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.pjt.pojo.ClickLike;

public class ClickLikeDao {

	private Connection conn = null;
	private PreparedStatement ps = null;
	private ResultSet rs = null;
	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public boolean addLike(ClickLike c) {
		boolean bool = false;
		try {
			conn = DBUtil.getConnection();
			String sql = "insert into xianyu_product_good(id,product_id,user_id,time)  ";
			sql += " values(seq_xianyu_product_good.nextval," + "?,?,sysdate)";
			ps = conn.prepareStatement(sql);
			ps.setLong(1, c.getProduct_id());
			ps.setLong(2, c.getUser_id());
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

	public int findLike(long pid, long user_id) {
		int count = 0;
		try {
			conn = DBUtil.getConnection();

			String sql = "select count(*) from xianyu_product_good "
					+ "where product_id=? and user_id=? ";
			ps = conn.prepareStatement(sql);
			ps.setLong(1, pid);
			ps.setLong(2, user_id);

			rs = ps.executeQuery();
			if (rs.next()) {
				count = rs.getInt(1);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(conn, ps, rs);
		}

		return count;
	}

	public boolean removeLike(long pid, long user_id) {
		boolean bool = false;
		try {
			conn = DBUtil.getConnection();
			String sql = "delete xianyu_product_good where product_id=?  and user_id=? ";
			ps = conn.prepareStatement(sql);
			ps.setLong(1, pid);
			ps.setLong(2, user_id);
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

	public ArrayList<ClickLike> findByUser(long user_id) {
		ArrayList<ClickLike> list = new ArrayList<ClickLike>();
		try {
			conn = DBUtil.getConnection();
			String sql = " select g.id, g.user_id fromuser, to_char(g.time,'yyyy-mm-dd hh24:mi:ss'), p.id product_id ,p.user_id touser "
					+ " from xianyu_product_good g, xianyu_product p "
					+ " where g.product_id=p.id and  p.user_id=? and g.user_id!=?  order by g.id";
			ps = conn.prepareStatement(sql);
			ps.setLong(1, user_id);
			ps.setLong(2, user_id);
			rs = ps.executeQuery();
			while (rs.next()) {
				ClickLike like = new ClickLike();
				like.setId(rs.getLong(1));
				like.setUser_id(rs.getLong(2));

				Date date = df.parse(rs.getString(3));
				long time = System.currentTimeMillis() - date.getTime();
				long day = time / (24 * 60 * 60 * 1000);

				if (day > 0) {
					like.setTime(day + "天前");
				} else {
					long h = time / (1000 * 60 * 60);
					if (h > 0) {
						like.setTime(h + "小时前");
					} else {
						h = time / (1000 * 60);
						like.setTime(h + "分钟前");
					}
				}
				like.setProduct_id(rs.getLong(4));
				list.add(like);

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
			String sql = " select max(g.id) from xianyu_product_good g, xianyu_product p "
					+ " where g.product_id=p.id and  p.user_id=? and g.user_id!=? order by g.id ";

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
