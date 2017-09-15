package com.pjt.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.pjt.pager.Pager;
import com.pjt.pojo.Product;

public class ProductDao {
	Connection conn = null;
	PreparedStatement ps = null;
	ResultSet rs = null;
	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public int getTotalpager(String tableName) {
		int count = 0;
		try {
			conn = DBUtil.getConnection();
			String sql = "select count(*) from  " + tableName;
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			if (rs.next()) {
				count = rs.getInt(1);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(conn, ps, rs);
		}

		return count % Pager.getSize() == 0 ? count / Pager.getSize() : count
				/ Pager.getSize() + 1;
	}

	public ArrayList<Product> findAll(int pageno) {
		ArrayList<Product> list = new ArrayList<Product>();
		try {
			conn = DBUtil.getConnection();
			String sql = "select * from (select  p.id pid, p.title,p.info,p.price,p.priceinfo,"
					+ "p.looknum,to_char(p.posttime,'yyyy-MM-dd HH24:mi:ss') ,u.id userid, u.username, u.address,u.imgurl,"
					+ "y.id yid,y.name, row_number()over(order by p.posttime desc)rn"
					+ " from xianyu_product p , xianyu_user u ,xianyu_yutang y "
					+ " where y.id=p.yutang_id and p.user_id=u.id"

					+ "  ) where rn>=? and rn<? ";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, Pager.getSize() * (pageno - 1));
			ps.setInt(2, Pager.getSize() * pageno);
			rs = ps.executeQuery();
			while (rs.next()) {
				Product p = new Product();
				p.setId(rs.getLong(1));
				p.setTitle(rs.getString(2));
				p.setInfo(rs.getString(3));
				p.setPrice(rs.getDouble(4));
				p.setPriceinfo(rs.getString(5));
				p.setLookcount(rs.getLong(6));
				Date date = df.parse(rs.getString(7));
				long time = System.currentTimeMillis() - date.getTime();
				long day = time / (24 * 60 * 60 * 1000);

				if (day > 0) {
					p.setTime(day + "天前");
				} else {
					long h = time / (1000 * 60 * 60);
					if (h > 0) {
						p.setTime(h + "小时前");
					} else {
						h = time / (1000 * 60);
						p.setTime(h + "分钟前");
					}
				}

				p.setUser_id(rs.getLong(8));
				p.setUsername(rs.getString(9));
				p.setAddress(rs.getString(10));
				p.setUserimg(rs.getString(11));
				p.setYutang_id(rs.getLong(12));
				p.setYutang_name(rs.getString(13));
				list.add(p);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(conn, ps, rs);
		}

		return list;
	}

	public Product findById(long pid) {
		Product p = new Product();
		try {
			conn = DBUtil.getConnection();
			String sql = "select * from (select  p.id pid, p.title,p.info,p.price,p.priceinfo,"
					+ "p.looknum,to_char(p.posttime,'yyyy-MM-dd HH24:mi:ss') ,p.category_id ,u.id userid, u.username, u.address,u.imgurl,"
					+ "y.id yid,y.name, row_number()over(order by p.posttime desc)rn"
					+ " from xianyu_product p , xianyu_user u ,xianyu_yutang y "
					+ " where y.id=p.yutang_id and p.user_id=u.id"

					+ "  ) where pid=?  ";

			ps = conn.prepareStatement(sql);
			ps.setLong(1, pid);
			rs = ps.executeQuery();
			if (rs.next()) {
				p.setId(rs.getLong(1));
				p.setTitle(rs.getString(2));
				p.setInfo(rs.getString(3));
				p.setPrice(rs.getDouble(4));
				p.setPriceinfo(rs.getString(5));
				p.setLookcount(rs.getLong(6));
				Date date = df.parse(rs.getString(7));
				long time = System.currentTimeMillis() - date.getTime();
				long day = time / (24 * 60 * 60 * 1000);

				if (day > 0) {
					p.setTime(day + "天前");
				} else {
					long h = time / (1000 * 60 * 60);
					if (h > 0) {
						p.setTime(h + "小时前");
					} else {
						h = time / (1000 * 60);
						p.setTime(h + "分钟前");
					}
				}
				p.setCategory_id(rs.getLong(8));
				p.setUser_id(rs.getLong(9));
				p.setUsername(rs.getString(10));
				p.setAddress(rs.getString(11));
				p.setUserimg(rs.getString(12));
				p.setYutang_id(rs.getLong(13));
				p.setYutang_name(rs.getString(14));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(conn, ps, rs);
		}

		return p;
	}

	public int findCommentCount(long product_id) {
		int count = 0;
		try {
			conn = DBUtil.getConnection();
			String sql = "select count(*) from xianyu_product_comment"
					+ "  where product_id=? ";
			ps = conn.prepareStatement(sql);
			ps.setLong(1, product_id);
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

	public int findGoodCount(long product_id) {
		int count = 0;
		try {
			conn = DBUtil.getConnection();
			String sql = "select count(*) from xianyu_product_good"
					+ "  where product_id =? ";
			ps = conn.prepareStatement(sql);
			ps.setLong(1, product_id);
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

	public ArrayList<String> getImgUrl(long product_id) {
		ArrayList<String> list = new ArrayList<String>();
		try {
			conn = DBUtil.getConnection();
			String sql = "select imgurl from xianyu_product_img  where "
					+ " product_id=? ";
			ps = conn.prepareStatement(sql);
			ps.setLong(1, product_id);
			rs = ps.executeQuery();
			while (rs.next()) {
				list.add(rs.getString(1));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(conn, ps, rs);
		}

		return list;
	}

	public ArrayList<Product> findTuijian(long category_id, long pid) {
		ArrayList<Product> list = new ArrayList<Product>();
		try {
			conn = DBUtil.getConnection();
			String sql = "select * from (select p.*,row_number()over(order by id)rn"
					+ " from xianyu_product p) ";
			sql += "  where rn<=8 and category_id=? and id!=? ";
			ps = conn.prepareStatement(sql);
			ps.setLong(1, category_id);
			ps.setLong(2, pid);
			rs = ps.executeQuery();
			while (rs.next()) {

				Product p = new Product();
				p.setId(rs.getLong(1));
				p.setTitle(rs.getString(2));
				p.setInfo(rs.getString(3));
				p.setUser_id(rs.getLong(4));
				p.setPrice(rs.getDouble(5));
				p.setPriceinfo(rs.getString(6));
				p.setYutang_id(rs.getLong(7));
				p.setLookcount(rs.getLong(8));
				p.setCategory_id(rs.getLong(10));
				list.add(p);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	public boolean addProduct(Product p) {
		boolean bool = false;
		long seq = 0;
		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			String sql = "select seq_xianyu_product.nextval  from  dual";
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			if (rs.next()) {
				seq = rs.getLong(1);
			}
			if (seq != 0) {
				sql = "insert into xianyu_product(id,title,info,user_id,price,priceinfo,yutang_id,looknum, posttime, category_id) ";
				sql += " values(?,?,?,?,?,?,?,0,sysdate,?)";
				ps = conn.prepareStatement(sql);
				ps.setLong(1, seq);
				ps.setString(2, p.getTitle());
				ps.setString(3, p.getInfo());
				ps.setLong(4, p.getUser_id());
				ps.setDouble(5, p.getPrice());
				ps.setString(6, p.getPriceinfo());
				ps.setLong(7, p.getYutang_id());
				ps.setLong(8, p.getCategory_id());
				ps.executeUpdate();
				for (String st : p.getBitsurl()) {
					sql = "insert into xianyu_product_img(id,imgurl,product_id)";
					sql += " values(seq_xianyu_product_img.nextval,?,? )";
					ps = conn.prepareStatement(sql);
					ps.setString(1, st);
					ps.setLong(2, seq);
					ps.executeUpdate();
				}

				conn.commit();
				bool = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			bool = false;
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} finally {
			DBUtil.close(conn, ps, rs);
		}

		return bool;
	}

	public ArrayList<Product> findByUser(long user_id) {
		ArrayList<Product> list = new ArrayList<Product>();
		try {
			conn = DBUtil.getConnection();
			String sql = "select * from (select  p.id pid, p.title,p.info,p.price,p.priceinfo,"
					+ " p.looknum,to_char(p.posttime,'yyyy-MM-dd HH24:mi:ss') ,u.id userid, "
					+ "  u.username, u.address,u.imgurl,  y.id yid,y.name, row_number()over(order by p.posttime desc)rn  "
					+ "from xianyu_product p , xianyu_user u ,xianyu_yutang y  "
					+ " where y.id=p.yutang_id and p.user_id=u.id)"
					+ " where userid=? ";

			ps = conn.prepareStatement(sql);
			ps.setLong(1, user_id);
			rs = ps.executeQuery();
			while (rs.next()) {
				Product p = new Product();
				p.setId(rs.getLong(1));
				p.setTitle(rs.getString(2));
				p.setInfo(rs.getString(3));
				p.setPrice(rs.getDouble(4));
				p.setPriceinfo(rs.getString(5));
				p.setLookcount(rs.getLong(6));
				Date date = df.parse(rs.getString(7));
				long time = System.currentTimeMillis() - date.getTime();
				long day = time / (24 * 60 * 60 * 1000);

				if (day > 0) {
					p.setTime(day + "天前");
				} else {
					long h = time / (1000 * 60 * 60);
					if (h > 0) {
						p.setTime(h + "小时前");
					} else {
						h = time / (1000 * 60);
						p.setTime(h + "分钟前");
					}
				}

				p.setUser_id(rs.getLong(8));
				p.setUsername(rs.getString(9));
				p.setAddress(rs.getString(10));
				p.setUserimg(rs.getString(11));
				p.setYutang_id(rs.getLong(12));
				p.setYutang_name(rs.getString(13));
				list.add(p);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(conn, ps, rs);
		}

		return list;
	}

	public ArrayList<Product> findByCate(long cate_id) {
		ArrayList<Product> list = new ArrayList<Product>();
		try {
			conn = DBUtil.getConnection();
			String sql = "select p.id,p.title,p.info,p.price,p.priceinfo, to_char(p.posttime,'yyyy-mm-dd hh:mi:ss'), "
					+ " p.category_id,"
					+ "  u.address from xianyu_product p, xianyu_user u where u.id=p.user_id"
					+ " and category_id=?";
			ps = conn.prepareStatement(sql);
			ps.setLong(1, cate_id);
			rs = ps.executeQuery();
			while (rs.next()) {
				Product p = new Product();
				p.setId(rs.getLong(1));
				p.setTitle(rs.getString(2));
				p.setInfo(rs.getString(3));
				p.setPrice(rs.getDouble(4));
				p.setPriceinfo(rs.getString(5));

				Date date = df.parse(rs.getString(6));
				long time = System.currentTimeMillis() - date.getTime();
				long day = time / (24 * 60 * 60 * 1000);

				if (day > 0) {
					p.setTime(day + "天前");
				} else {
					long h = time / (1000 * 60 * 60);
					if (h > 0) {
						p.setTime(h + "小时前");
					} else {
						h = time / (1000 * 60);
						p.setTime(h + "分钟前");
					}
				}

				p.setCategory_id(rs.getLong(7));
				p.setAddress(rs.getString(8));
				list.add(p);

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(conn, ps, rs);
		}

		return list;

	}

	public ArrayList<Product> findAll() {
		ArrayList<Product> list = new ArrayList<Product>();
		try {
			conn = DBUtil.getConnection();
			String sql = "select p.id,p.title,p.info,p.price,p.priceinfo, to_char(p.posttime,'yyyy-mm-dd hh24:mi:ss'), "
					+ "  u.address from xianyu_product p, xianyu_user u where u.id=p.user_id";
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				Product p = new Product();
				p.setId(rs.getLong(1));
				p.setTitle(rs.getString(2));
				p.setInfo(rs.getString(3));
				p.setPrice(rs.getDouble(4));
				p.setPriceinfo(rs.getString(5));

				Date date = df.parse(rs.getString(6));
				long time = System.currentTimeMillis() - date.getTime();
				long day = time / (24 * 60 * 60 * 1000);

				if (day > 0) {
					p.setTime(day + "天前");
				} else {
					long h = time / (1000 * 60 * 60);
					if (h > 0) {
						p.setTime(h + "小时前");
					} else {
						h = time / (1000 * 60);
						p.setTime(h + "分钟前");
					}
				}

				p.setCategory_id(rs.getLong(7));
				p.setAddress(rs.getString(8));
				list.add(p);

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(conn, ps, rs);
		}

		return list;

	}

	public ArrayList<Product> findByYuTang(long yutang_id) {
		ArrayList<Product> list = new ArrayList<Product>();
		try {
			conn = DBUtil.getConnection();
			String sql = " select * from (select  p.id pid, p.title,p.info,p.price,p.priceinfo,"
					+ " p.looknum,to_char(p.posttime,'yyyy-MM-dd HH24:mi:ss') , u.id userid, "
					+ " u.username, u.address,u.imgurl,  y.id yid,y.name, row_number()over(order by p.posttime desc)rn  "
					+ " 	from xianyu_product p , xianyu_user u ,xianyu_yutang y  "
					+ " where y.id=p.yutang_id and p.user_id=u.id)"
					+ "  where  yid=?";
			ps = conn.prepareStatement(sql);
			ps.setLong(1, yutang_id);
			rs = ps.executeQuery();
			while (rs.next()) {
				Product p = new Product();
				p.setId(rs.getLong(1));
				p.setTitle(rs.getString(2));
				p.setInfo(rs.getString(3));
				p.setPrice(rs.getDouble(4));
				p.setPriceinfo(rs.getString(5));
				p.setLookcount(rs.getLong(6));
				Date date = df.parse(rs.getString(7));
				long time = System.currentTimeMillis() - date.getTime();
				long day = time / (24 * 60 * 60 * 1000);

				if (day > 0) {
					p.setTime(day + "天前");
				} else {
					long h = time / (1000 * 60 * 60);
					if (h > 0) {
						p.setTime(h + "小时前");
					} else {
						h = time / (1000 * 60);
						p.setTime(h + "分钟前");
					}
				}

				p.setUser_id(rs.getLong(8));
				p.setUsername(rs.getString(9));
				p.setAddress(rs.getString(10));
				p.setUserimg(rs.getString(11));
				p.setYutang_id(rs.getLong(12));
				p.setYutang_name(rs.getString(13));
				list.add(p);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(conn, ps, rs);
		}

		return list;
	}

}
