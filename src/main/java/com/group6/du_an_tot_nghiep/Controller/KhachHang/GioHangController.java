package com.group6.du_an_tot_nghiep.Controller.KhachHang;

import com.group6.du_an_tot_nghiep.Dao.GioHangChiTietDao;
import com.group6.du_an_tot_nghiep.Dao.TaiKhoanDao;
import com.group6.du_an_tot_nghiep.Dto.KhachHang.KhachHangSanPhamSearchRequest;
import com.group6.du_an_tot_nghiep.Dto.KhachHang.ListGioHangChiTietResponse;
import com.group6.du_an_tot_nghiep.Dto.KhachHang.ListPggGioHang;
import com.group6.du_an_tot_nghiep.Dto.KhachHang.ThongTinGioHang;
import com.group6.du_an_tot_nghiep.Entities.GioHang;
import com.group6.du_an_tot_nghiep.Entities.GioHangChiTiet;
import com.group6.du_an_tot_nghiep.Entities.PhieuGiamGia;
import com.group6.du_an_tot_nghiep.Entities.TaiKhoan;
import com.group6.du_an_tot_nghiep.Service.KhachHang.GioHangService;
import com.group6.du_an_tot_nghiep.Service.QuanLy.TaiKhoanService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.swing.text.html.Option;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Controller
//@RequestMapping("/khach-hang/gio-hang")
public class GioHangController {

    @Autowired
    TaiKhoanDao taiKhoanDao;

    TaiKhoan _taiKhoan;

    @Autowired
    TaiKhoanService _taiKhoanService;

    @Autowired
    GioHangService gioHangService;

@Autowired
    HttpSession session;

    public static GioHang _gioHang;



    @GetMapping("/khach-hang/gio-hang-index")
    public String index (Model model) {
        model.addAttribute("searchRequest",new KhachHangSanPhamSearchRequest());
        _taiKhoan = (TaiKhoan) session.getAttribute("username");
        model.addAttribute("taiKhoan", _taiKhoan);
        if (_taiKhoan == null) {
            if (_gioHang == null) {
                _gioHang = gioHangService.taoGioHang();
            }
        }else {
            GioHang gioHang = gioHangService.getGioHangByTaiKhoan(_taiKhoan);
            if (gioHang == null) {
                _gioHang = gioHangService.taoGioHang();
            }else {
                _gioHang = gioHang;
            }
        }
         return "/khach_hang/gio_hang";
    }



    @GetMapping("/api/khach-hang/gio-hang/get-list-gio_hang_chi-tiet")
    @ResponseBody
    public ResponseEntity<List<ListGioHangChiTietResponse>> getListGioHangChiTiet() {
        _taiKhoan = (TaiKhoan) session.getAttribute("username");
        _gioHang = gioHangService.getGioHangByTaiKhoan(_taiKhoan);
        List<ListGioHangChiTietResponse> responseList = gioHangService.getListGioHangChiTiet(_gioHang);
        try {
            return ResponseEntity.ok()
                    .body(responseList);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


    @GetMapping("/api/khach-hang/gio-hang/update-so-luong/{idGhct}/{soLuong}")
    @ResponseBody
    public ResponseEntity<Boolean> updateSoLuong(@PathVariable int idGhct, @PathVariable int soLuong) {
        boolean result = gioHangService.updateSoLuong(idGhct,soLuong);
        try {
            return ResponseEntity.ok()
                    .body(result);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/api/khach-hang/gio-hang/update-selected/{idGhct}/{selected}")
    @ResponseBody
    public ResponseEntity<Boolean> updateSelect(@PathVariable int idGhct, @PathVariable boolean selected) {
        boolean result = gioHangService.updateSelected(idGhct,selected);
        try {
            return ResponseEntity.ok()
                    .body(result);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/api/khach-hang/gio-hang/delete/{idGhct}")
    @ResponseBody
    public ResponseEntity<Boolean> updateSelect(@PathVariable int idGhct) {
        boolean result = gioHangService.updateTrangThai(idGhct, GioHangChiTietDao.DA_XOA);
        try {
            return ResponseEntity.ok()
                    .body(result);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


    @GetMapping("/api/khach-hang/gio-hang/get-list-pgg-public/{tongTien}")
    @ResponseBody
    public ResponseEntity<List<ListPggGioHang>> getListPggPublic(@PathVariable BigDecimal tongTien) {
        List<ListPggGioHang> listPggGioHangs = gioHangService.getListPggPublic(tongTien);
        try {
            return ResponseEntity.ok()
                    .body(listPggGioHangs);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/api/khach-hang/gio-hang/get-list-pgg-private/{tongTien}")
    @ResponseBody
    public ResponseEntity<List<ListPggGioHang>> getListPggPrivate(@PathVariable BigDecimal tongTien) {
        _taiKhoan = (TaiKhoan) session.getAttribute("username");
        _gioHang = gioHangService.getGioHangByTaiKhoan(_taiKhoan);
        List<ListPggGioHang> listPggGioHangs = gioHangService.getListPggPrivate(tongTien,_taiKhoan);
        try {
            return ResponseEntity.ok()
                    .body(listPggGioHangs);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/api/khach-hang/gio-hang/get-list-pgg-private-init")
    @ResponseBody
    public ResponseEntity<List<ListPggGioHang>> getListPggPrivateInit() {
        _taiKhoan = (TaiKhoan) session.getAttribute("username");
        _gioHang = gioHangService.getGioHangByTaiKhoan(_taiKhoan);
        List<ListPggGioHang> listPggGioHangs = gioHangService.getListPggPrivateInit(_gioHang,_taiKhoan);
        try {
            return ResponseEntity.ok()
                    .body(listPggGioHangs);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/api/khach-hang/gio-hang/get-list-pgg-public-init")
    @ResponseBody
    public ResponseEntity<List<ListPggGioHang>> getListPggPublicInit() {
        _taiKhoan = (TaiKhoan) session.getAttribute("username");
        _gioHang = gioHangService.getGioHangByTaiKhoan(_taiKhoan);
        List<ListPggGioHang> listPggGioHangs = gioHangService.getListPggPublicInit(_gioHang);
        try {
            return ResponseEntity.ok()
                    .body(listPggGioHangs);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/api/khach-hang/gio-hang/get-tai-khoan")
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

    @GetMapping("/api/khach-hang/gio-hang/get-thong-tin-gio-hang")
    @ResponseBody
    public ResponseEntity<ThongTinGioHang> getThongTinDonHang() {
        _taiKhoan = (TaiKhoan) session.getAttribute("username");
        _gioHang = gioHangService.getGioHangByTaiKhoan(_taiKhoan);
        ThongTinGioHang thongTinGioHang = gioHangService.getThongTinGioHang(_gioHang);
        try {
            return ResponseEntity.ok()
                    .body(thongTinGioHang);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/api/khach-hang/gio-hang/kiem-tra-pgg/{tongTienTruocGiam}")
    @ResponseBody
    public ResponseEntity<Boolean> kiemTraPgg(@PathVariable BigDecimal tongTienTruocGiam) {
        _taiKhoan = (TaiKhoan) session.getAttribute("username");
        _gioHang = gioHangService.getGioHangByTaiKhoan(_taiKhoan);
        boolean result = gioHangService.kiemTraPgg(_gioHang,tongTienTruocGiam);
        try {
            return ResponseEntity.ok()
                    .body(result);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/api/khach-hang/gio-hang/update-pgg/{idPgg}")
    @ResponseBody
    public ResponseEntity<Boolean> kiemTraPgg(@PathVariable int idPgg) {
        _taiKhoan = (TaiKhoan) session.getAttribute("username");
        _gioHang = gioHangService.getGioHangByTaiKhoan(_taiKhoan);
        boolean result = gioHangService.updatePgg(_gioHang,idPgg);
        try {
            return ResponseEntity.ok()
                    .body(result);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/test")
    public String test() {
        return "/khach_hang/test";
    }
}
