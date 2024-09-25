package com.group6.du_an_tot_nghiep.Controller.KhachHang;

import com.group6.du_an_tot_nghiep.Dao.TaiKhoanDao;
import com.group6.du_an_tot_nghiep.Dto.KhachHang.KhachHangSanPhamSearchRequest;
import com.group6.du_an_tot_nghiep.Dto.KhachHang.ListPggGioHang;
import com.group6.du_an_tot_nghiep.Dto.QuanLy.HoaDon.*;
import com.group6.du_an_tot_nghiep.Entities.TaiKhoan;
import com.group6.du_an_tot_nghiep.Service.KhachHang.KhachHangSanPhamService;
import com.group6.du_an_tot_nghiep.Service.KhachHang.TaiKhoanCuaToiService;
import com.group6.du_an_tot_nghiep.Service.QuanLy.HoaDonChiTietService;
import com.group6.du_an_tot_nghiep.Service.QuanLy.HoaDonService;
import com.group6.du_an_tot_nghiep.Service.QuanLy.TaiKhoanService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

@Controller
public class TaiKhoanCuaToiController {
    TaiKhoan _taiKhoan;
    @Autowired
    TaiKhoanService _taiKhoanService;
    @Autowired
    KhachHangSanPhamService _khachHangSanPhamService;
    @Autowired
    TaiKhoanCuaToiService taiKhoanCuaToiService;
    @Autowired
    HttpSession session;

    @Autowired
    private TaiKhoanDao taiKhoanDao;

    @Autowired
    private HoaDonService hoaDonService;

    @Autowired
    private HoaDonChiTietService hoaDonChiTietService;



    String _imgUrl="";



    @GetMapping("/khach-hang/tai-khoan/me")
    @ResponseBody
    public TaiKhoan me(HttpSession session){
        return taiKhoanDao.findById(_taiKhoan.getId()).get();
    }

    @GetMapping("/khach-hang/tai-khoan/ho-so")
    public String hoSo (Model model) {
        TaiKhoan taiKhoan= (TaiKhoan) session.getAttribute("username");
        _taiKhoan=taiKhoan;
        System.out.println("tkaka: "+_taiKhoan.getId());
        model.addAttribute("idKhTt", taiKhoan.getId() );
        model.addAttribute("searchRequest",new KhachHangSanPhamSearchRequest());
        return "/khach_hang/tai_khoan/ho_so";
    }

    @PostMapping(value = "/api/khach-hang/tai-khoan/uploadFile-detail")
    public String uploadFileDetail(@RequestParam MultipartFile file) throws IOException {
        _imgUrl = file.getOriginalFilename();
        Path path = Paths.get("D:\\du_an_tot_nghiep\\src\\main\\resources\\uploads\\");
        if (file.getOriginalFilename() != null && !file.getOriginalFilename().isEmpty()){
            TaiKhoan tk= _taiKhoanService.getTkById(_taiKhoan.getId());
            tk.setAnhDaiDien(_imgUrl);
            taiKhoanDao.save(tk);
            InputStream inputStream = file.getInputStream();
            Files.copy(inputStream,path.resolve(file.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);
        }
        return "redirect:/khach-hang/tai-khoan/ho-so";

    }

    @GetMapping("/khach-hang/tai-khoan/dia-chi")
    public String diaChi (Model model) {
        model.addAttribute("searchRequest",new KhachHangSanPhamSearchRequest());
        _taiKhoan = (TaiKhoan) session.getAttribute("username");
        return "/khach_hang/tai_khoan/dia_chi";
    }

    @GetMapping("/khach-hang/tai-khoan/don-mua")
    public String donMua (Model model) {
        model.addAttribute("searchRequest",new KhachHangSanPhamSearchRequest());
        _taiKhoan = (TaiKhoan) session.getAttribute("username");
        return "/khach_hang/tai_khoan/don_mua";
    }

    @GetMapping("/khach-hang/tai-khoan/phieu-giam-gia")
    public String phieuGiamGia (Model model) {
        model.addAttribute("searchRequest",new KhachHangSanPhamSearchRequest());
        _taiKhoan = (TaiKhoan) session.getAttribute("username");
        return "/khach_hang/tai_khoan/phieu_giam_gia";
    }

    @GetMapping("/khach-hang/tai-khoan/doi-mat-khau")
    public String doiMatKhau (Model model) {
        model.addAttribute("searchRequest",new KhachHangSanPhamSearchRequest());
        _taiKhoan = (TaiKhoan) session.getAttribute("username");
        return "/khach_hang/tai_khoan/doi_mat_khau";
    }

    @GetMapping("/khach-hang/tai-khoan/get-tai-khoan")
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

    @GetMapping("/khach-hang/tai-khoan/get-so-sp-trong-gio")
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

    @GetMapping("/khach-hang/tai-khoan/get-list-pgg-private")
    @ResponseBody
    public ResponseEntity<List<ListPggGioHang>> getListPggPrivateInit() {
        _taiKhoan = (TaiKhoan) session.getAttribute("username");
        List<ListPggGioHang> listPggGioHangs = taiKhoanCuaToiService.getListPggPrivate(_taiKhoan);
        try {
            return ResponseEntity.ok()
                    .body(listPggGioHangs);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    @PostMapping("/api/kh/search-tt")
    @ResponseBody
    public List<HoaDonResponse> search(
            @RequestBody HoaDonFillRequest hoaDonFillRequest
            ) {
        _taiKhoan = (TaiKhoan) session.getAttribute("username");


        return taiKhoanCuaToiService.searchKh(hoaDonFillRequest, _taiKhoan);
    }

    @GetMapping("/api/kh/hien-thi-don/{idHoaDon}")
    @ResponseBody
    public ResponseEntity<ThongTinDonHang> viewThongTinDonHang(@PathVariable Integer idHoaDon) {
        return new ResponseEntity<>(hoaDonChiTietService.getThongTinDonHang(idHoaDon), HttpStatus.OK);
    }

    @GetMapping("/api/kh/ds-sp/{idHoaDon}")
    @ResponseBody
    public ResponseEntity<List<SanPhamChiTietDTO>> getSanphamChiTiet(@PathVariable Integer idHoaDon) {
        return new ResponseEntity<>(hoaDonChiTietService.findSanPhamChiTietByIdHoaDon(idHoaDon), HttpStatus.OK);
    }


    @GetMapping("/api/kh/tong-tien/{idHoaDon}")
    @ResponseBody
    public ResponseEntity<MoneyReponse> getAllMoneyByIdHoaDon(@PathVariable Integer idHoaDon) {
        return new ResponseEntity<>(hoaDonChiTietService.getAllMonyByIdHoaDon(idHoaDon), HttpStatus.OK);
    }


    @PutMapping ("/api/kh/hdct/cancel/{idHoaDon}")
    @ResponseBody
    public ResponseEntity<MessageResponse> cancelOrder(@PathVariable Integer idHoaDon,
            @RequestBody TrangThaiHoaDonRequest request
    ) {

        try {
            // Gọi dịch vụ để hủy đơn hàng
            MessageResponse response = taiKhoanCuaToiService.cancelOrderkh(idHoaDon, request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // Xử lý lỗi và trả về thông báo lỗi
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(MessageResponse.builder().message(e.getMessage()).build());
        }
    }

}





