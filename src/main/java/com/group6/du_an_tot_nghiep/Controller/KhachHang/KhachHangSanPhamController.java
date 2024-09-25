package com.group6.du_an_tot_nghiep.Controller.KhachHang;

import com.group6.du_an_tot_nghiep.Dto.KhachHang.*;
import com.group6.du_an_tot_nghiep.Entities.GioHang;
import com.group6.du_an_tot_nghiep.Entities.SanPham;
import com.group6.du_an_tot_nghiep.Entities.SanPhamChiTiet;
import com.group6.du_an_tot_nghiep.Entities.TaiKhoan;
import com.group6.du_an_tot_nghiep.Service.KhachHang.GioHangService;
import com.group6.du_an_tot_nghiep.Service.KhachHang.KhachHangSanPhamService;
import com.group6.du_an_tot_nghiep.Service.QuanLy.TaiKhoanService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Controller

public class KhachHangSanPhamController {

    @Autowired
    TaiKhoanService _taiKhoanService;
    TaiKhoan _taiKhoan;

    @Autowired
    KhachHangSanPhamService _khachHangSanPhamService;

    @Autowired
    GioHangService gioHangService;

    @Autowired
    HttpSession session;

    int _idMauSac;
    int _idSpct;
    int _idKichThuoc;




    @GetMapping("/api/khach-hang/san-pham/get-tai-khoan")
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

    @GetMapping("/khach-hang-san-pham/index/{id}/{idMauSac}/{idKichThuoc}")
    public String index(Model model, @PathVariable int id, @PathVariable int idMauSac, @PathVariable int idKichThuoc) {
        _idMauSac = idMauSac;
        _idSpct = id;
        _idKichThuoc = idKichThuoc;
        _taiKhoan = (TaiKhoan) session.getAttribute("username");
        model.addAttribute("searchRequest",new KhachHangSanPhamSearchRequest());
        return "/khach_hang/san_pham";
    }


    @GetMapping("/api/khach-hang/san-pham/get-san-pham")
    @ResponseBody
    public ResponseEntity<KhachHangSanPhamResponse> getSanPham() {
        _taiKhoan = (TaiKhoan) session.getAttribute("username");
        try {
            KhachHangSanPhamResponse response = _khachHangSanPhamService.getReponseById(_idSpct,_idMauSac,_idKichThuoc);
            return ResponseEntity.ok()
                    .body(response);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/api/khach-hang/san-pham/get-san-pham-lien-quan/{idDanhMuc}")
    @ResponseBody
    public ResponseEntity<List<SanPhamLienQuan>> getSanPham(@PathVariable int idDanhMuc) {
        _taiKhoan = (TaiKhoan) session.getAttribute("username");
        try {
            List<SanPhamLienQuan> list = _khachHangSanPhamService.findSanPhamLienQuan(idDanhMuc,_idSpct);
            return ResponseEntity.ok()
                    .body(list);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/api/khach-hang/san-pham/chuyen-mau/{id}/{idMauSac}")
    @ResponseBody
    public ResponseEntity<KhachHangSanPhamResponse> chuyenMau(@PathVariable int id, @PathVariable int idMauSac) {
        _taiKhoan = (TaiKhoan) session.getAttribute("username");
        try {
            KhachHangSanPhamResponse response = _khachHangSanPhamService.getReponseByIdChuyenMau(id, idMauSac);
            _idSpct = response.getIdSpct();
            _idMauSac = response.getMauDangHien();
            return ResponseEntity.ok()
                    .body(response);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/api/khach-hang/san-pham/change-size/{id}/{idMauSac}/{idKichThuoc}")
    @ResponseBody
    public ResponseEntity<ChangeSizeSanPham> changeSize(@PathVariable int id, @PathVariable int idMauSac, @PathVariable int idKichThuoc) {
        _taiKhoan = (TaiKhoan) session.getAttribute("username");
        try {
            ChangeSizeSanPham response = _khachHangSanPhamService.changeSize(id,idMauSac,idKichThuoc);
            _idSpct = id;
            _idMauSac = idMauSac;
            _idKichThuoc = idKichThuoc;
            return ResponseEntity.ok()
                    .body(response);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/api/khach-hang/san-pham/get-so-luong-spct-trong-gio/{idSpct}")
    @ResponseBody
    public ResponseEntity<Integer> getSoLuongChiTietTrongGio(@PathVariable int idSpct) {
        try {
            Integer result;
            _taiKhoan = (TaiKhoan) session.getAttribute("username");
            if (_taiKhoan == null) {
                result = 0;
            }else{
                result = gioHangService.getSoLuongSpTrongGh(idSpct,_taiKhoan);
                if (result == null) {
                    result = 0;
                }
            }
            return ResponseEntity.ok()
                    .body(result);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/api/khach-hang/san-pham/them-vao-gio/{idSpct}/{soLuongMua}")
    @ResponseBody
    public ResponseEntity<Boolean> themVaoGio(@PathVariable int idSpct, @PathVariable int soLuongMua) {
        _taiKhoan = (TaiKhoan) session.getAttribute("username");
        TaiKhoan taiKhoan = _taiKhoan;
        boolean result = gioHangService.themVaoGio(idSpct, soLuongMua,taiKhoan);
        try {
            return ResponseEntity.ok()
                    .body(result);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/api/khach-hang/san-pham/get-so-sp-trong-gio")
    @ResponseBody
    public ResponseEntity<Integer> getSoSpTrongGio() {
        _taiKhoan = (TaiKhoan) session.getAttribute("username");
        System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        System.out.println(_taiKhoan.getId());
        int result = _khachHangSanPhamService.getSoLuongSpTrongGh(_taiKhoan);
        try {
            return ResponseEntity.ok()
                    .body(result);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
//
//
//    @GetMapping("/getSize/{idSize}")
//    @ResponseBody
//    public ResponseEntity<KhachHangSanPhamResponse> test (@PathVariable("idSize") int idSize){
//        _taiKhoan = _taiKhoanService.getById(16);
//        _gioHang = _khachHangSanPhamService.findGioHangByTaiKhoan(_taiKhoan);
//
//        try {
//            KhachHangSanPhamResponse response = _khachHangSanPhamService.getReponseById(_idSpct,_idMauSac,idSize);
//            _idSpct = response.getIdSpct();
//            return ResponseEntity.ok()
//                    .body(response);
//        }catch (Exception e){
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    @GetMapping("/them-vao-gio/{idSize}/{soLuongMua}")
//    @ResponseBody
//    public ResponseEntity<Boolean> themVaoGio(@PathVariable int idSize, @PathVariable int soLuongMua) {
//        _taiKhoan = _taiKhoanService.getById(16);
//        _gioHang = _khachHangSanPhamService.findGioHangByTaiKhoan(_taiKhoan);
//        boolean result = gioHangService.themVaoGio(_idSpct, _idMauSac, idSize, soLuongMua,_taiKhoan);
//        try {
//            return ResponseEntity.ok()
//                    .body(result);
//        }catch (Exception e){
//            e.printStackTrace();
//            return null;
//        }
//    }
//

}
