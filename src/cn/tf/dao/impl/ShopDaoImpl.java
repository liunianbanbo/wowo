package cn.tf.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.tf.dao.ShopDao;
import cn.tf.entities.AdminInfo;
import cn.tf.entities.Roles;
import cn.tf.entities.Shopping;
import cn.tf.utils.DBHelper;

public class ShopDaoImpl implements ShopDao {

	@Override
	public int add(String sname, String tid, String prov, String city,
			String area, String points, String tel, String date, String info,
			String aid) {
		DBHelper db=new DBHelper();
		String sql=null;
		sql="insert into shopping values(seq_shopping_spid.nextval,?,?,?,?,?,?,?,?,?,0,to_date(?,'yyyy-mm-dd'))";
		List<Object>  params=new ArrayList<Object>();
		params.add(sname);
		params.add(aid);
		params.add(tid);
		params.add(prov);
		params.add(city);
		params.add(area);
		params.add(points);
		params.add(tel);
		params.add(info);
		params.add(date);
	
		return db.doUpdate(sql,params);
		
	}

	@Override
	public Integer find(Integer aid) {
		DBHelper db=new DBHelper();
		List<Object>  params=new ArrayList<Object>();
		String sql="select count(*) from shopping s ,adminInfo a where s.aid=a.aid  and  s.aid=? ";
		params.add(aid);
		return db.findByOne(sql, params);
		
	}

	@Override
	public int update(String sname, String tid, String prov, String city,
			String area, String points, String tel, String date, String info,
			String aid) {
		DBHelper db=new DBHelper();
		String sql=null;
		sql="update  from  shopping  set  sname=?,tid=?,prov=?,city=?,area=?,points=?,tel=?,info=?,date=to_date(?,'yyyy-mm-dd'))   where aid=?";
		List<Object>  params=new ArrayList<Object>();
		params.add(sname);
		params.add(tid);
		params.add(prov);
		params.add(city);
		params.add(area);
		params.add(points);
		params.add(tel);
		params.add(info);
		params.add(date);
		params.add(aid);
		
		
		return db.doUpdate(sql,params);
	}

	@Override
	public List<Shopping> find(Integer aid,Integer rid,Integer pageNo, Integer pageSize) {
		DBHelper db=new DBHelper();
		List<Object>  params=new ArrayList<Object>();
		
		
		String sql=null;
		if(rid==1002 || rid==1003){
			if(pageNo==null){
				sql=" select select s.spid,s.sname,a.aid,a.aname,g.tname,prov,city,area,points,s.tel,s.status  from shopping s ,adminInfo a ,goodstype  g  where s.aid=a.aid  and s.tid=g.tid  order by spid desc ";
			}else{
				
				sql="select * from(select A.*,rownum  rn from (  select s.spid,s.sname,a.aid,a.aname,g.tname,prov,city,area,points,s.tel,s.status  from shopping s ,adminInfo a ,goodstype  g  where s.aid=a.aid  and s.tid=g.tid  order by spid desc ) A  where rownum<=? ) where rn>? ";
				params.add(pageNo*pageSize);
				params.add((pageNo-1)*pageSize);
				
				
			}
		}else  if(rid==1024){
			if(pageNo==null){
				sql=" select select s.spid,s.sname,a.aid,a.aname,g.tname,prov,city,area,points,s.tel,s.status from shopping  where  aid=?  order by spid desc ";
				params.add(aid);
			}else{
				sql="select * from(select a.*,rownum  rn from (  select select s.spid,s.sname,a.aid,a.aname,g.tname,prov,city,area,points,s.tel,s.status from shopping  where aid=? order by spid asc ) a  where rownum<=? ) where rn>?";
				params.add(aid);
				params.add(pageNo*pageSize);
				params.add((pageNo-1)*pageSize);
			}
		}
		
		
		return db.find(sql, params,Shopping.class);
		
	}

	@Override
	public Integer getTotal(Integer spid) {
		DBHelper db=new DBHelper();
		String sql=null;
		List<Object>  params=new ArrayList<Object>();
		if(spid==null){
			sql="select count(spid) from shopping ";
		}else{
			sql="select count(spid) from shopping  where spid=? ";
			params.add(spid);
		}
		return db.findByOne(sql, params);
	}

	@Override
	public List<Shopping> find(Map<String, String> param, Integer pageNo,
			Integer pageSize) {
		DBHelper db=new DBHelper();
		List<Object>  params=new ArrayList<Object>();
		

		
		
		String sql="select s.spid,s.sname,a.aname,g.tname,prov,city,area,points,s.tel,s.status,a.aid  from shopping s ,adminInfo a ,goodstype  g  where s.aid=a.aid  and s.tid=g.tid    " ;
		if(param!=null && param.size()>0){
			Set<String> keys=param.keySet();
			for (String key : keys) {
				sql+=" and "+key+" ? " ;
				params.add(param.get(key));
			}
		}
		sql+=" order by spid desc ";
		
		if(pageNo!=null){
		
			sql="select * from(select A.*,rownum  rn from (  "+sql+" ) A  where rownum<=? ) where rn>?";
			params.add(pageNo*pageSize);
			params.add((pageNo-1)*pageSize);
		}
	
		return db.find(sql, params,Shopping.class);
	}

}
