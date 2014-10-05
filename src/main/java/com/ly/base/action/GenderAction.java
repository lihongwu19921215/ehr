package com.ly.base.action;

import com.ly.comm.Dwz;
import com.ly.comm.Page;
import com.ly.comm.ParseObj;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.*;
import org.nutz.mvc.filter.CheckSession;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

import com.ly.base.vo.Gender;
import com.ly.base.service.GenderService;


@IocBean
@At("/gender")
@Fail("json")
@Filters(@By(type=CheckSession.class, args={"username", "/WEB-INF/login.html"}))
public class GenderAction {

	private static final Log log = Logs.getLog(GenderAction.class);
	
	@Inject
	private GenderService genderService;

    @At("/")
    @Ok("beetl:/WEB-INF/base/gender_list.html")
    public void index(@Param("..")Page p,
                      @Param("..")Gender gender,
                      HttpServletRequest request){
        Cnd c = new ParseObj(gender).getCnd();
        List<Gender> list_m = genderService.query(c, p);
        p.setRecordCount(genderService.count(c));

        request.setAttribute("list_obj", list_m);
        request.setAttribute("page", p);
        request.setAttribute("gender", gender);
    }

    @At
    @Ok("beetl:/WEB-INF/base/gender.html")
    public void edit(@Param("id")Long id,
                      HttpServletRequest request){
        if(id == null || id == 0){
            request.setAttribute("gender", null);
        }else{
            request.setAttribute("gender", genderService.fetch(id));
        }
    }

    @At
    @Ok("json")
    public Map<String,String> save( @Param("..")Gender gender){
        Object rtnObject;
        if (gender.getId() == null || gender.getId() == 0) {
            rtnObject = genderService.dao().insert(gender);
        }else{
            rtnObject = genderService.dao().updateIgnoreNull(gender);
        }
        return Dwz.rtnMap((rtnObject == null) ? false : true, "gender", "closeCurrent");
    }

    @At
    @Ok("json")
    public Map<String,String> del(@Param("id")Long id)
    {
        int num =  genderService.delete(id);
        return Dwz.rtnMap((num > 0) ? true : false , "gender", "");
    }

}
