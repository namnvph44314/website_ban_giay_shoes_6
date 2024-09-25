package com.group6.du_an_tot_nghiep.Controller.QuanLy;

import com.group6.du_an_tot_nghiep.Entities.TaiKhoan;
import com.group6.du_an_tot_nghiep.Service.QuanLy.TaiKhoanService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/tai-khoan")
public class TaiKhoanController {
    @Autowired
    TaiKhoanService taiKhoanService;

    @Autowired
    HttpSession session;


    @GetMapping("/index")
    public String index (Model model) {
        return "/quan_ly/khach_hang/index";
    }

    @GetMapping("/add")
    public String add(){
        return "/quan_ly/khach_hang/add";
    }

    @GetMapping("/detail/{id}")
    public String detail(Model model, @PathVariable("id") int id){
        TaiKhoan taiKhoan= (TaiKhoan) session.getAttribute("username");
        model.addAttribute("id",id);
        model.addAttribute("idKhTt", taiKhoan.getId() );
        System.out.println(taiKhoan.getId()+"////////////////////////");
        return "/quan_ly/khach_hang/detail";
    }
}