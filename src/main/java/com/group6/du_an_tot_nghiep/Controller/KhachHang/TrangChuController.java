package com.group6.du_an_tot_nghiep.Controller.KhachHang;

import com.group6.du_an_tot_nghiep.Dao.TheLoaiDao;
import com.group6.du_an_tot_nghiep.Dto.KhachHang.KhachHangSanPhamSearchRequest;
import com.group6.du_an_tot_nghiep.Dto.KhachHang.ListSanPhamResponse;
import com.group6.du_an_tot_nghiep.Entities.GioHang;
import com.group6.du_an_tot_nghiep.Entities.TaiKhoan;
import com.group6.du_an_tot_nghiep.Entities.TheLoai;
import com.group6.du_an_tot_nghiep.Service.KhachHang.KhachHangSanPhamService;
import com.group6.du_an_tot_nghiep.Service.KhachHang.TrangChuService;
import com.group6.du_an_tot_nghiep.Service.QuanLy.TaiKhoanService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller

public class TrangChuController {
    @Autowired
    TaiKhoanService _taiKhoanService;
    TaiKhoan _taiKhoan;

    @Autowired
    KhachHangSanPhamService _khachHangSanPhamService;

    @Autowired
    TrangChuService _trangChuService;

    @Autowired
    HttpSession session;

    @GetMapping("/trang-chu/index")
    public String index (Model model) {
        model.addAttribute("searchRequest",new KhachHangSanPhamSearchRequest());
        _taiKhoan = (TaiKhoan) session.getAttribute("username");
        return "/khach_hang/trang_chu";
    }

    @GetMapping("/api/khach-hang/trang-chu/get-tai-khoan")
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

    @GetMapping("/api/khach-hang/trang-chu/get-so-sp-trong-gio")
    @ResponseBody
    public ResponseEntity<Integer> getSoSpTrongGio() {
        _taiKhoan = (TaiKhoan) session.getAttribute("username");
        int result = _khachHangSanPhamService.getSoLuongSpTrongGh(_taiKhoan);
        try {
            return ResponseEntity.ok()
                    .body(result);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/api/khach-hang/trang-chu/get-list-danh-muc")
    @ResponseBody
    public ResponseEntity<List<TheLoai>> getAllDanhMuc() {
        _taiKhoan = (TaiKhoan) session.getAttribute("username");
        try {
            List<TheLoai> theLoaiList = _trangChuService.findAllDanhMucForTrangChu(TheLoaiDao.DANG_BAN);
            return ResponseEntity.ok()
                    .body(theLoaiList);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/api/khach-hang/trang-chu/get-hang-moi-ve")
    @ResponseBody
    public ResponseEntity<List<ListSanPhamResponse>> getHangMoiVe() {
        _taiKhoan = (TaiKhoan) session.getAttribute("username");
        try {
            List<ListSanPhamResponse> listHangMoiVe = _trangChuService.findHangMoiVe();
            return ResponseEntity.ok()
                    .body(listHangMoiVe);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


}
