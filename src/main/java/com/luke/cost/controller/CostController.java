package com.luke.cost.controller;

import com.luke.cost.bean.Cost;
import com.luke.cost.bean.CostPage;
import com.luke.cost.service.CostService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
@RequestMapping("/cost")
public class CostController {

    @Resource(name = "CostA")
    private CostService service;

    //初始分页
    @RequestMapping("/findAll.do")
    public String findAll(Model model){
        CostPage page = service.findAll();
        model.addAttribute("costPage",page);
        return "cost/cost_list";
    }

    //easyui 测试
    @RequestMapping(value = "/findAllEU.do",method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> findAllEU(){
        System.out.println(1);
        CostPage page = service.findAll();
        List<Cost> costs = page.getList();
        Map<String,Object> map = new HashMap<>();
        map.put("total",costs.size());
        map.put("rows",costs);
        return map;
    }

    @RequestMapping(value = "/treeData.do",method = RequestMethod.POST)
    @ResponseBody
    public String tree(){
        String str = "[{\n" +
                "\t\"id\":1,\n" +
                "\t\"text\":\"My Documents\",\n" +
                "\t\"children\":[{\n" +
                "\t\t\"id\":11,\n" +
                "\t\t\"text\":\"Photos\",\n" +
                "\t\t\"state\":\"closed\",\n" +
                "\t\t\"children\":[{\n" +
                "\t\t\t\"id\":111,\n" +
                "\t\t\t\"text\":\"Friend\"\n" +
                "\t\t},{\n" +
                "\t\t\t\"id\":112,\n" +
                "\t\t\t\"text\":\"Wife\"\n" +
                "\t\t},{\n" +
                "\t\t\t\"id\":113,\n" +
                "\t\t\t\"text\":\"Company\"\n" +
                "\t\t}]\n" +
                "\t},{\n" +
                "\t\t\"id\":12,\n" +
                "\t\t\"text\":\"Program Files\",\n" +
                "\t\t\"state\":\"closed\",\n" +
                "\t\t\"children\":[{\n" +
                "\t\t\t\"id\":121,\n" +
                "\t\t\t\"text\":\"Intel\"\n" +
                "\t\t},{\n" +
                "\t\t\t\"id\":122,\n" +
                "\t\t\t\"text\":\"Java\"\n" +
                "\t\t},{\n" +
                "\t\t\t\"id\":123,\n" +
                "\t\t\t\"text\":\"Microsoft Office\"\n" +
                "\t\t},{\n" +
                "\t\t\t\"id\":124,\n" +
                "\t\t\t\"text\":\"Games\"\n" +
                "\t\t}]\n" +
                "\t},{\n" +
                "\t\t\"id\":16,\n" +
                "\t\t\"text\":\"Actions\",\n" +
                "\t\t\"children\":[{\n" +
                "\t\t\t\"text\":\"Add\",\n" +
                "\t\t\t\"iconCls\":\"icon-add\"\n" +
                "\t\t},{\n" +
                "\t\t\t\"text\":\"Remove\",\n" +
                "\t\t\t\"iconCls\":\"icon-remove\"\n" +
                "\t\t},{\n" +
                "\t\t\t\"text\":\"Save\",\n" +
                "\t\t\t\"iconCls\":\"icon-save\"\n" +
                "\t\t},{\n" +
                "\t\t\t\"text\":\"Search\",\n" +
                "\t\t\t\"iconCls\":\"icon-search\"\n" +
                "\t\t}]\n" +
                "\t},{\n" +
                "\t\t\"id\":13,\n" +
                "\t\t\"text\":\"index.html\"\n" +
                "\t},{\n" +
                "\t\t\"id\":14,\n" +
                "\t\t\"text\":\"about.html\"\n" +
                "\t},{\n" +
                "\t\t\"id\":15,\n" +
                "\t\t\"text\":\"welcome.html\"\n" +
                "\t}]\n" +
                "}]\n";
        return str;
    }

    //点击页码查询
    @RequestMapping("/findCostByLimit.do")
    public String findCostByLimit(@RequestParam String currentPage, Model model){

        CostPage page = new CostPage();
        page.setCurrentPage(Integer.parseInt(currentPage));
        CostPage costPage = service.findCostByLimit(page);

        model.addAttribute("costPage",costPage);

        return "cost/cost_list";
    }

    @RequestMapping("/finddetail.do")
    public String finddetail(@RequestParam String id , Model model){
        Cost cost = service.finddetail(id);
        model.addAttribute("cost",cost);
        return "cost/cost_detail";
    }

    @RequestMapping("/addCost.do")
    public String addCost(){
        return "cost/cost_add";
    }

    @RequestMapping("/addCostT.do")
    public String addCostT( Cost cost,Model model){
        System.out.println(cost);
        boolean flag = service.insertCost(cost);
        if (flag){
            CostPage costPage = service.findAll();
            model.addAttribute("costPage",costPage);
            return "cost/cost_list";
        }else {
            model.addAttribute("msg","保存失败，资费名称重复！");
            model.addAttribute("flag",true);
        }
        return "cost/cost_add";
    }

    @RequestMapping("/updateStatus.do")
    public String updateStatus(@RequestParam String id , Model model){
        System.out.println(id);
        boolean flag = service.updateStatus(id);
        CostPage costPage = service.findAll();
        model.addAttribute("costPage",costPage);
        model.addAttribute("msg","成功启用");
        return "cost/cost_list";
    }

    @RequestMapping("/deleteCostById.do")
    public String deleteCostById(@RequestParam String id , Model model){
        System.out.println(id);
        boolean flag = service.deleteCostById(id);
        CostPage costPage = service.findAll();
        model.addAttribute("costPage",costPage);
        model.addAttribute("msg","删除成功");
        return "cost/cost_list";
    }

    @RequestMapping("/modifyCostT.do")
    public String modifyCostT(@RequestParam String id, Model model){
        Cost cost = service.finddetail(id);
        model.addAttribute("cost",cost);
        return "cost/cost_modi";
    }

    @RequestMapping("/modifyCost.do")
    public String modifyCost(Cost cost, Model model){
        System.out.println(cost);
        boolean flag = service.modifyCost(cost);
        if (flag){
            return "redirect:findAll.do";
        }else {
            model.addAttribute("msg","保存失败，资费名称重复！");
            model.addAttribute("flag",true);
        }
        return "cost/cost_modi";
    }

    @RequestMapping("/findAllAscSortByBD.do")
    public String findAllAscSortByBD(Model model){
        List<Cost> costs = service.findAllAscSortByBD();
        model.addAttribute("costs",costs);
        return "cost/cost_list";
    }

    @RequestMapping("/findAllDecSortByBD.do")
    public String findAllDecSortByBD(Model model){
        List<Cost> costs = service.findAllDecSortByBD();
        model.addAttribute("costs",costs);
        return "cost/cost_list";
    }

    @RequestMapping("/findAllAscSortByBC.do")
    public String findAllAscSortByBC(Model model){
        List<Cost> costs = service.findAllAscSortByBC();
        model.addAttribute("costs",costs);
        return "cost/cost_list";
    }

    @RequestMapping("/findAllDecSortByBC.do")
    public String findAllDecSortByBC(Model model){
        List<Cost> costs = service.findAllDecSortByBC();
        model.addAttribute("costs",costs);
        return "cost/cost_list";
    }

}
