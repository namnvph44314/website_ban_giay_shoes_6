package com.group6.du_an_tot_nghiep.Controller.QuanLy;


import com.group6.du_an_tot_nghiep.Dto.QuanLy.TaiKhoan.NhanVienDetail;
import com.group6.du_an_tot_nghiep.Dto.QuanLy.TaiKhoan.DoiMatKhauDto;
import com.group6.du_an_tot_nghiep.Entities.TaiKhoan;
import com.group6.du_an_tot_nghiep.Service.QuanLy.NhanVienService;
import com.group6.du_an_tot_nghiep.Service.QuanLy.TaiKhoanService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TaiKhoanQuanLyController {
    @Autowired
    TaiKhoanService _taiKhoanService;
    @Autowired
    NhanVienService nhanVienService;
    @Autowired
    HttpSession session;
    TaiKhoan _taiKhoan;


    @GetMapping("/tai-khoan-quan-ly/ho-so")

    public String hoSo (Model model) {
        _taiKhoan = (TaiKhoan) session.getAttribute("username");
        return "/quan-ly/thong_tin_tai_khoan/profile";
    }

    @GetMapping("/tai-khoan-quan-ly/doi-mat-khau")
    public String doiMatKhau (Model model) {
        _taiKhoan = (TaiKhoan) session.getAttribute("username");
        return "/quan-ly/thong_tin_tai_khoan/doi_pass";
    }

    @GetMapping("/api/admin/tai-khoan/get-tai-khoan-entity")
    @ResponseBody
    public ResponseEntity<TaiKhoan> getTaiKhoan() {
        _taiKhoan = (TaiKhoan) session.getAttribute("username");
        try {
            return ResponseEntity.ok()
                    .body(_taiKhoan);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/api/detail-nhan-vien")
    public ResponseEntity<NhanVienDetail> detailNhanVien() {
        _taiKhoan = (TaiKhoan) session.getAttribute("username");
        NhanVienDetail nhanVienDetail = nhanVienService.getdetailNv(_taiKhoan.getId());
        if (nhanVienDetail.getQuyen().equals("Nhan_Vien")) {
            nhanVienDetail.setQuyen("Nhân Viên");
        }else if (nhanVienDetail.getQuyen().equals("ADMIN")) {
            nhanVienDetail.setQuyen("Admin");
        }
        return ResponseEntity.ok(nhanVienDetail);
    }

    @PostMapping("/api/doi-mat-khau")
    @ResponseBody
    public Boolean register(@RequestBody DoiMatKhauDto request){
        return _taiKhoanService.doiMatKhau(request);
    }

    @GetMapping("/api/get-doi-mat-khau-dto")
    @ResponseBody
    public DoiMatKhauDto getDoiMatKhauDto(@RequestBody DoiMatKhauDto request){
        return new DoiMatKhauDto();
    }

}
