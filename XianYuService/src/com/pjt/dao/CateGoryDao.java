package com.pjt.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.pjt.pojo.CateGory;

public class CateGoryDao {

	private Connection conn = null;
	private PreparedStatement ps = null;
	private ResultSet rs = null;

	public ArrayList<CateGory> findCate() {
		ArrayList<CateGory> list = new ArrayList<CateGory>();
		try {
			conn = DBUtil.getConnection();
			String sql = "select * from xianyu_category";
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {
				CateGory cate = new CateGory();
				cate.setId(rs.getLong(1));
				cate.setCate_name(rs.getString(2));
				cate.setCate_img(rs.getString(3));
				list.add(cate);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(conn, ps, rs);
		}

		return list;
	}

}
