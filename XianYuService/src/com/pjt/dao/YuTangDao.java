package com.pjt.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.pjt.pojo.YuTang;

public class YuTangDao {

	private Connection conn = null;
	private PreparedStatement ps = null;
	private ResultSet rs = null;

	public ArrayList<YuTang> findMyYutang(long user_id) {
		ArrayList<YuTang> list = new ArrayList<YuTang>();
		try {
			conn = DBUtil.getConnection();
			String sql = "select u.yutang_id, u.identity,y.name,y.imgurl,y.lat,y.lng  from (select * from xianyu_yutang_user  where user_id=?)u ,  ";
			sql += "xianyu_yutang y where u.yutang_id=y.id";
			ps = conn.prepareStatement(sql);
			ps.setLong(1, user_id);
			rs = ps.executeQuery();
			while (rs.next()) {
				YuTang yutang = new YuTang();
				yutang.setYutang_id(rs.getLong(1));
				yutang.setIdentity(rs.getString(2));
				yutang.setName(rs.getString(3));
				yutang.setImgurl(rs.getString(4));
				yutang.setLat(rs.getString(5));
				yutang.setLng(rs.getString(6));
				list.add(yutang);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(conn, ps, rs);
		}

		return list;
	}

	public long findPopCount(long id) {
		long count = 0;
		try {
			conn = DBUtil.getConnection();
			String sql = "select count(*) from xianyu_yutang_user where  yutang_id=?";
			ps = conn.prepareStatement(sql);
			ps.setLong(1, id);
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

	public ArrayList<YuTang> findByCate(int cate_id) {
		ArrayList<YuTang> list = new ArrayList<YuTang>();
		try {
			conn = DBUtil.getConnection();
			String sql = "select y.id,y.name,y.imgurl,y.lat,y.lng, nvl(c.num,0)num from "
					+ "  xianyu_yutang y ,(select yutang_id,count(*)num from xianyu_yutang_user group by yutang_id ) "
					+ "  c where y.id=c.yutang_id(+) and cate=?";

			ps = conn.prepareStatement(sql);
			ps.setLong(1, cate_id);

			rs = ps.executeQuery();
			while (rs.next()) {
				YuTang yutang = new YuTang();
				yutang.setYutang_id(rs.getLong(1));
				yutang.setName(rs.getString(2));
				yutang.setImgurl(rs.getString(3));
				yutang.setLat(rs.getString(4));
				yutang.setLng(rs.getString(5));
				yutang.setPopularity(rs.getLong(6));
				list.add(yutang);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(conn, ps, rs);
		}

		return list;
	}

	public ArrayList<YuTang> findAll() {
		ArrayList<YuTang> list = new ArrayList<YuTang>();
		try {
			conn = DBUtil.getConnection();
			String sql = "select * from xianyu_yutang";
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				YuTang y = new YuTang();
				y.setYutang_id(rs.getLong(1));
				y.setName(rs.getString(2));
				y.setImgurl(rs.getString(3));
				y.setLat(rs.getString(4));
				y.setLng(rs.getString(5));
				list.add(y);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(conn, ps, rs);
		}

		return list;
	}

	public int findpop(long yutang_id) {
		int count = 0;
		try {
			conn = DBUtil.getConnection();
			String sql = " select count(*) from  xianyu_yutang_user where yutang_id=? ";

			ps = conn.prepareStatement(sql);
			ps.setLong(1, yutang_id);
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

	public int findPostNum(long yutang_id) {
		int count = 0;
		try {
			conn = DBUtil.getConnection();
			String sql = " select count(*) from xianyu_product where yutang_id=?";
			ps = conn.prepareStatement(sql);
			ps.setLong(1, yutang_id);
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

	public boolean isAdd(long yutang_id, long user_id) {
		boolean bool = false;
		try {
			conn = DBUtil.getConnection();
			String sql = " select count(*) from xianyu_yutang_user  where yutang_id=? and user_id=?";
			ps = conn.prepareStatement(sql);
			ps.setLong(1, yutang_id);
			ps.setLong(2, user_id);
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

	public YuTang findById(long yutang_id) {
		YuTang y = new YuTang();
		try {
			conn = DBUtil.getConnection();
			String sql = " select * from xianyu_yutang where id=? ";
			ps = conn.prepareStatement(sql);
			ps.setLong(1, yutang_id);
			rs = ps.executeQuery();
			if (rs.next()) {
				y.setYutang_id(rs.getLong(1));
				y.setName(rs.getString(2));
				y.setImgurl(rs.getString(3));
				y.setLat(rs.getString(4));
				y.setLng(rs.getString(5));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(conn, ps, rs);
		}

		return y;
	}

	public boolean addYuTangUser(long user_id, long yutang_id) {
		boolean bool = false;
		try {
			conn = DBUtil.getConnection();
			String sql = " insert into xianyu_yutang_user(id,yutang_id,user_id,identity) "
					+ " values(seq_xianyu_yutang_user.nextval,?,?,'ÈºÖÚ')";
			ps = conn.prepareStatement(sql);
			ps.setLong(1, yutang_id);
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

}
