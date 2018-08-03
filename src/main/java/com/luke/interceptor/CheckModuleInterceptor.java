package com.luke.interceptor;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
//        //»ñÈ¡µÇÂ¼ÓÃ»§ÓÐÈ¨ÏÞµÄËùÓÐÄ£¿é
//        List<Module> modules = (List<Module>)
//                request.getSession().getAttribute("allModules");
//        //»ñÈ¡ÓÃ»§µ±Ç°Òª·ÃÎÊµÄÄ£¿é
//        int currentModule = (Integer)
//                request.getSession().getAttribute("currentModule");
//        //ÅÐ¶ÏÓÃ»§ÓÐÈ¨ÏÞµÄÄ£¿éÊÇ·ñ°üº¬µ±Ç°Ä£¿é
//        for (Module module : modules) {
//            if (module.getModule_id() == currentModule) {
//                //ÓÐµ±Ç°·ÃÎÊÄ£¿éµÄÈ¨ÏÞ
//                return true;
//            }
//        }
        //Ã»ÓÐµ±Ç°·ÃÎÊÄ£¿éµÄÈ¨ÏÞ
        response.sendRedirect(
                request.getContextPath() + "/login/nopower.do");
        return false;
    }

}

