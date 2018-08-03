package com.luke.controller_rest;


import com.luke.admin.bean.Admin;
import com.luke.admin.service.AdminService;
import com.luke.utils.VerifyCode;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

/***
 * com.luke.cost.controller
 * dllo
 * 18/7/16
 *             ,%%%%%%%%,
 *           ,%%/\%%%%/\%%
 *          ,%%%\c "" J/%%%
 * %.       %%%%/ 0  0 \%%%
 * `%%.     %%%%    _  |%%%
 *  `%%     `%%%%(__Y__)%%'
 *  //       ;%%%%`\-/%%%'
 * ((       /  `%%%%%%%'
 *  \\    .'     '%%%'|    攻
 *   \\  /       \  | |    城
 *    \\/         ) | |    湿
 *     \         /_ | |__
 *     (___________))))))) 
 *
 *       我湿一吼  BUG无有                        
 */
@Controller
@RequestMapping("/loginRest")
public class LoginControllerRest {

    @Resource(name = "AdminA")
    private AdminService adminService;

    @RequestMapping(value = "/VerifyCodeServlet.do", method = RequestMethod.GET)
    public void VerifyCodeServlet(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("enterV");
        VerifyCode vc = new VerifyCode();
        BufferedImage image = vc.getImage();//获取一次性验证码图片
        // 该方法必须在getImage()方法之后来调用
//		System.out.println(vc.getText());//获取图片上的文本
        try {
            VerifyCode.output(image, response.getOutputStream());//把图片写到指定流中
            // 把文本保存到session中，为LoginServlet验证做准备
            request.getSession().setAttribute("vCode", vc.getText());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @RequestMapping(value = "/login.do")
    public String login() {
        return "main";

    }

    @RequestMapping(value = "/validate.do",method = RequestMethod.POST)
    @ResponseBody
    public String validate(@RequestParam String admin_code,
                           @RequestParam String admin_password,
                           @RequestParam String verifyCode,
                           HttpServletRequest request){
        Admin admin = adminService.findByCode(admin_code);
        String vcode = (String) request.getSession().getAttribute("vCode");
        boolean c = verifyCode.equalsIgnoreCase(vcode);
        if (admin != null){
            Admin admin1 = adminService.findByCodePasswd(admin.getAdmin_code(), admin_password);
            if (admin1 != null && c){
                request.getSession().setAttribute("admin",admin1);
                return "true";
            }else if (admin1 == null){
                return "2";
            }else {
                return "3";
            }
        }else {
           return "1";
        }
    }

//    @RequestMapping(value = "/validateCode.do", method = RequestMethod.POST)
//    @ResponseBody
//    public String validateCode(@RequestParam String admin_code) {
//        System.out.println(admin_code);
//        Admin admin = adminService.findByCode(admin_code);
//        if (admin != null) {
//            return "true";
//        }
//        return "false";
//    }
//
//    @RequestMapping(value = "/validatePasswd.do", method = RequestMethod.POST)
//    @ResponseBody
//    public String validatePasswd(@RequestParam String admin_code, @RequestParam String admin_password) {
//        System.out.println(admin_code + admin_password);
//        Admin admin = adminService.findByCodePasswd(admin_code, admin_password);
//        if (admin != null) {
//            return "true";
//        }
//        return "false";
//    }
//
//
//    @RequestMapping(value = "/validateVcode.do", method = RequestMethod.POST)
//    @ResponseBody
//    public String validateVcode(@RequestParam String verifyCode, HttpServletRequest request) {
//        System.out.println(verifyCode);
//        String vcode = (String) request.getSession().getAttribute("vCode");
//        System.out.println(vcode);
//        boolean c = verifyCode.equalsIgnoreCase(vcode);
//        return String.valueOf(c);
//    }


}
