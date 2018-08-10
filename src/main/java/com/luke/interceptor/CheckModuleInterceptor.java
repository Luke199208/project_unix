package com.luke.interceptor;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.luke.role.bean.Module_info;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;


public class CheckModuleInterceptor implements HandlerInterceptor {

    @Override
    public void afterCompletion(HttpServletRequest arg0,
                                HttpServletResponse arg1, Object arg2, Exception arg3)
            throws Exception {

    }

    @Override
    public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1,
                           Object arg2, ModelAndView arg3) throws Exception {

    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object obj) throws Exception {
        //获得当前用户可操作的所有模块
        List<Module_info> modules = (List<Module_info>)
                request.getSession().getAttribute("allModules");
        //获得当前用户请求(url)的模块(可能是用户直接拼接网址url)
        int currentModule = (Integer)
                request.getSession().getAttribute("currentModule");
        //检查用户是否可操作其请求的功能
        for (Module_info module : modules) {
            if (module.getModule_id().equals(String.valueOf(currentModule))) {
                //请求的模块含有  跳出
                return true;
            }
        }
        //用户所有的模块不包含,跳转页面
        response.sendRedirect(
                request.getContextPath() + "/login/toNopower.do");
        return false;
    }

}

