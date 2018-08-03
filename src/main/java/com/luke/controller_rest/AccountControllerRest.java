package com.luke.controller_rest;


import com.luke.account.bean.Account;
import com.luke.account.bean.AccountPage;
import com.luke.account.service.AccountService;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.beans.IntrospectionException;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/***
 * com.luke.account.controller
 * dllo
 * 18/7/17
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
@RequestMapping("/accountRest")
//@SessionAttributes("accountPage")
// 存放并刷新表单传回的accountPage,
// 则会取之前存放的accountPage取值执行操作,
//但不建议使用,因为很多项目使用前后端分离,则此方式无法成功
public class AccountControllerRest {

    @Resource(name = "accountA")
    private AccountService service;

    @RequestMapping(value = "/findAll.do",method = RequestMethod.POST)
    @ResponseBody
    public AccountPage findAll() {
        AccountPage account = service.findAccountByLimit();
        return account;
    }

    //开启或暂停
    @RequestMapping(value = "/setState.do",method = RequestMethod.POST)
    @ResponseBody
    public String setState(@RequestBody Map map) {
        boolean flag = service.setState((String) map.get("id"));
        if (flag) {
            return "操作成功";

        } else {
            return "操作失败";

        }
    }

    //删除
    @RequestMapping(value = "/deleteAccount.do",method = RequestMethod.POST)
    @ResponseBody
    public AccountPage deleteAccount(@RequestBody Map map) {
        boolean flag = service.deleteAccount((String) map.get("id"));
        if (flag) {
            AccountPage account = service.findAccountByLimit();
            return account;
        } else {
            AccountPage account = service.findAccountByLimit();
            return account;
        }
    }


    //条件分页查询
    @RequestMapping(value = "/ConditionQueryByLimit.do",method = RequestMethod.POST)
    @ResponseBody
    public AccountPage ConditionQueryByLimit(@RequestBody AccountPage page) {
        //System.out.println(page+"Condition");

        AccountPage accountPage = service.ConditionQueryByLimit(page);

        return accountPage;
    }

    @RequestMapping(value = "/addAccount.do",method = RequestMethod.POST)
    @ResponseBody
    public String addAccount(@RequestBody Account account) {
        boolean flag = service.insertAccount(account);

        return String.valueOf(flag);
    }

    //detail
    @RequestMapping(value = "/findDetail.do",method = RequestMethod.POST)
    @ResponseBody
    public List<Account> findDetail(@RequestBody Map map) {
        Account account = service.findDetail((String) map.get("id"));
        Account reAccount = service.findDetail(account.getRecommender_id());
        List<Account> accounts = new ArrayList<>();
        accounts.add(account);
        accounts.add(reAccount);
        return accounts;
    }

    //modify
    @RequestMapping("/modiAcc.do")
    @ResponseBody
    public String modiAcc(@RequestBody Account account) {

        boolean flag = service.modiAcc(account);
        return String.valueOf(flag);
    }

    //TODO:数据库导入  未实现
    @RequestMapping("/import.do")
    public String doImport(HttpServletRequest request, Model model) throws Exception {

        //获取上传的文件
        // MultipartHttpServletRequest multipart = (MultipartHttpServletRequest) request;
        //MultipartFile file = multipart.getFile("upfile");
        MultipartResolver resolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        MultipartHttpServletRequest multipartRequest = resolver.resolveMultipart(request);
        MultipartFile file = multipartRequest.getFile("upfile");

        InputStream in = file.getInputStream();
        //数据导入
        service.importExcelInfo(in, file);
        in.close();
        model.addAttribute("msg", "导入成功");
        return "account/account.list";
    }

    @RequestMapping("/export.do")
    public
    @ResponseBody
    void export(HttpServletRequest request, HttpServletResponse response)
            throws UnsupportedEncodingException, ClassNotFoundException,
            IntrospectionException, IllegalAccessException, ParseException,
            InvocationTargetException {
        String accountDate = request.getParameter("accountDate");
        if (accountDate != "") {
            response.reset(); //清除buffer缓存
            Map<String, Object> map = new HashMap<String, Object>();
            // 指定下载的文件名，浏览器都会使用本地编码，即GBK，浏览器收到这个文件名后，用ISO-8859-1来解码，然后用GBK来显示
            // 所以我们用GBK解码，ISO-8859-1来编码，在浏览器那边会反过来执行。
            response.setHeader("Content-Disposition", "attachment;filename=" + new String(accountDate.getBytes("GBK"), "ISO-8859-1"));
            response.setContentType("application/vnd.excel;charset=UTF-8");
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);
            XSSFWorkbook workbook = null;
            //导出Excel对象
            workbook = service.exportExcelInfo(accountDate);
            OutputStream output;
            try {
                output = response.getOutputStream();
                BufferedOutputStream bufferedOutPut = new BufferedOutputStream(output);
                bufferedOutPut.flush();
                workbook.write(bufferedOutPut);
                bufferedOutPut.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
