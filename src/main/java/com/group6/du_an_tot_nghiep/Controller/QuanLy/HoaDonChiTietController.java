package com.group6.du_an_tot_nghiep.Controller.QuanLy;


import com.group6.du_an_tot_nghiep.Dao.HoaDonDao;
import com.group6.du_an_tot_nghiep.Dao.TaiKhoanDao;
import com.group6.du_an_tot_nghiep.Dto.KhachHang.ListPggGioHang;
import com.group6.du_an_tot_nghiep.Dto.QuanLy.HoaDon.*;
import com.group6.du_an_tot_nghiep.Entities.*;
import com.group6.du_an_tot_nghiep.Service.KhachHang.KhachHangSanPhamService;
import com.group6.du_an_tot_nghiep.Service.QuanLy.*;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.*;


import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
//@RequestMapping("/admin/hdct")

public class HoaDonChiTietController {

    @Autowired
    private HoaDonChiTietService hoaDonChiTietService;

    @Autowired
    private TrangThaiHoaDonService trangThaiHoaDonService;

    @Autowired
    private HoaDonService hoaDonService;

    @Autowired
    private PdfService pdfService;

    @Autowired
    private PhieuGiamGiaService phieuGiamGiaService;

    @Autowired
    private KhachHangSanPhamService _khachHangSanPhamService;

    @Autowired
    private TaiKhoanDao taiKhoanDao;

    @Autowired
            private HoaDonDao hoaDonDao;


    Integer _idHoaDon;
    Integer _idTaiKhoan;

    @GetMapping("/hdct/index/{idHoaDon}")
    public String viewThongTinDonHang1(@PathVariable Integer idHoaDon) {
        _idHoaDon = idHoaDon;
        HoaDon hoaDon = hoaDonDao.findById(idHoaDon).orElse(null);
        if (hoaDon.getIdTaiKhoan() == null){
            _idTaiKhoan = -1;
        }else {
            _idTaiKhoan = hoaDon.getIdTaiKhoan().getId();
        }
        return "/quan-ly/hoa-don/hoadonchitiet";
    }


    @GetMapping("/api/read-file-hdct/{url}")
    @ResponseBody
    public ResponseEntity<ByteArrayResource> readFile(@PathVariable("url") String url) {
        try {
            String photo = url;
            Path fileName = Paths.get("D:\\du_an_tot_nghiep\\src\\main\\resources\\uploads", photo);
            byte[] buffer = Files.readAllBytes(fileName);
            ByteArrayResource byteArrayResource = new ByteArrayResource(buffer);
            return ResponseEntity.ok()
                    .contentLength(buffer.length)
                    .contentType(MediaType.parseMediaType("image/png"))
                    .body(byteArrayResource);
        } catch (Exception e) {

            return null;
        }
    }

    @GetMapping("/api/read-file-by-idspct/{id}")
    @ResponseBody
    public ResponseEntity<ByteArrayResource> readFileByIdSpct(@PathVariable("id") int id) {
        try {
            String photo = _khachHangSanPhamService.findUrlAnhMacDinh(id);
            Path fileName = Paths.get("D:\\du_an_tot_nghiep\\src\\main\\resources\\uploads", photo);
            byte[] buffer = Files.readAllBytes(fileName);
            ByteArrayResource byteArrayResource = new ByteArrayResource(buffer);
            return ResponseEntity.ok()
                    .contentLength(buffer.length)
                    .contentType(MediaType.parseMediaType("image/png"))
                    .body(byteArrayResource);
        } catch (Exception e) {
            return null;
        }
    }

    @GetMapping("/api/hdct/hien-thi-don")
    @ResponseBody
    public ResponseEntity<ThongTinDonHang> viewThongTinDonHang() {
        return new ResponseEntity<>(hoaDonChiTietService.getThongTinDonHang(_idHoaDon), HttpStatus.OK);
    }


    @GetMapping("/api/hdct/hien-thi-lich-su")
    @ResponseBody
    public ResponseEntity<List<LichSuHoaDonResponse>> getLichSuThanhToan() {
        return new ResponseEntity<>(hoaDonChiTietService.getLichSuHoaDon(_idHoaDon), HttpStatus.OK);
    }

    @GetMapping("/api/hdct/hien-thi-trang-thai")
    @ResponseBody
    public ResponseEntity<List<TrangThaiHoaDonRepose>> getAllTrangThaiHoaDon() {
        return new ResponseEntity<>(hoaDonChiTietService.getlimitTrangThaiHoaDon(_idHoaDon), HttpStatus.OK);
    }

    @GetMapping("/api/hdct/get-all-lshd")
    @ResponseBody
    public ResponseEntity<List<TrangThaiHoaDonRepose>> getAlllichSuThayDoi() {
        return new ResponseEntity<>(hoaDonChiTietService.getAllTrangThaiHoaDon(_idHoaDon), HttpStatus.OK);
    }

    @GetMapping("/api/hdct/tong-tien")
    @ResponseBody
    public ResponseEntity<MoneyReponse> getAllMoneyByIdHoaDon() {
        return new ResponseEntity<>(hoaDonChiTietService.getAllMonyByIdHoaDon(_idHoaDon), HttpStatus.OK);
    }

    @PostMapping("/api/hdct/confirm-order")
    @ResponseBody
    public ResponseEntity<MessageResponse> confirmOrder(
            @RequestBody @Valid TrangThaiHoaDonRequest request
    ) {
        try {
            // Gọi service để xử lý logic
            MessageResponse response = trangThaiHoaDonService.confirmOrder(_idHoaDon, request);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // Xử lý ngoại lệ và trả về lỗi nếu có
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Error: " + e.getMessage()));
        }
    }

    @GetMapping("/api/hdct/ds-sp")
    @ResponseBody
    public ResponseEntity<List<SanPhamChiTietDTO>> getSanphamChiTiet() {
        return new ResponseEntity<>(hoaDonChiTietService.findSanPhamChiTietByIdHoaDon(_idHoaDon), HttpStatus.OK);
    }

    @GetMapping("/api/hdct/hinh-anh/{idSanPhamCt}")
    @ResponseBody
    public ResponseEntity<List<HinhAnh>> getAnh(@PathVariable(name = "idSanPhamCt") Integer idSanPhamCt) {
        return new ResponseEntity<>(hoaDonChiTietService.getAnh(idSanPhamCt), HttpStatus.OK);
    }


    @PutMapping("/api/hdct/updatethongtin")
    @ResponseBody
    public ResponseEntity<ThongTinDonHang> updateThongTinDonHang(
            @RequestBody ThongTinDonHang thongTinDonHang) {
        ThongTinDonHang updatedThongTinDonHang = hoaDonChiTietService.updateThongTinDonHang(_idHoaDon, thongTinDonHang);
        if (updatedThongTinDonHang == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedThongTinDonHang);
    }

    @PutMapping("/api/hdct/cancel")
    @ResponseBody
    public ResponseEntity<MessageResponse> cancelOrder(
            @RequestBody TrangThaiHoaDonRequest request
           ) {

        try {
            // Gọi dịch vụ để hủy đơn hàng
            MessageResponse response = trangThaiHoaDonService.cancelOrder(_idHoaDon, request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // Xử lý lỗi và trả về thông báo lỗi
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(MessageResponse.builder().message(e.getMessage()).build());
        }
    }

    @PostMapping("/api/hdct/rollback")
    @ResponseBody
    public ResponseEntity<MessageResponse> rollbackOrderStatus(@RequestBody TrangThaiHoaDonRequest request) {
        try {
            MessageResponse response = trangThaiHoaDonService.rollbackOrderStatus(_idHoaDon, request);

            return ResponseEntity.ok(response);
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Error: " + e.getMessage()));
        }
    }

    @GetMapping("/api/hdct/pdf")
    @ResponseBody
    public void generatePDF(HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=pdf_" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);

        this.pdfService.orderCouter(response, _idHoaDon);
    }

    @GetMapping("/api/hdct/spct")
    @ResponseBody
    public ResponseEntity<Page<SanPhamChiTiet>> getAllSPCT(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<SanPhamChiTiet> result = hoaDonChiTietService.getAll(page, size);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/api/hdct/updatesoluong")
    @ResponseBody
    public ResponseEntity<?> updateHoaDon(

            @RequestParam Integer soLuong,
            @RequestParam Integer idHoaDonChiTiet) {
        try {
            HoaDon updatedHoaDon = hoaDonChiTietService.updateHoaDon(_idHoaDon, soLuong, idHoaDonChiTiet);
            return ResponseEntity.ok(updatedHoaDon);
        } catch (RuntimeException e) {

            return ResponseEntity.badRequest().body("Lỗi: " + e.getMessage());
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Có lỗi xảy ra. Vui lòng thử lại sau.");
        }
    }

    @PostMapping("/api/hdct/themsphdct")
    @ResponseBody
    public ResponseEntity<MessageResponse> themSanPhamVaoHoaDonChiTiet(

            @RequestParam Integer idSanPhamChiTiet,
            @RequestParam int soLuong,
            @RequestParam String username) {
        try {
            MessageResponse response = hoaDonChiTietService.themSanPhamVaoHoaDonChiTiet(_idHoaDon, idSanPhamChiTiet, soLuong, username);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(MessageResponse.builder().message("Lỗi: " + e.getMessage()).build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/api/hdct/xoa")
    @ResponseBody
    public ResponseEntity<MessageResponse> xoaSanPhamKhoiHoaDon(
            @RequestParam Integer idSanPhamChiTiet) {
        MessageResponse response = hoaDonChiTietService.xoaSanPhamKhoiHoaDonChiTiet(_idHoaDon, idSanPhamChiTiet);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/hdct/getAllPgg")
    @ResponseBody
    public ResponseEntity<List<PhieuGiamGia>> getAllPgg() {
        return ResponseEntity.ok(phieuGiamGiaService.getAllPgg());
    }


    @PostMapping("/api/hdct/getAllSpct")
    @ResponseBody
    public ResponseEntity<Page<SanPhamChiTiet>> searchBySp(@RequestBody SanPhamChiTietFillReq sanPhamChiTietFillReq) {
        Page<SanPhamChiTiet> result = hoaDonChiTietService.searchBySanPham(sanPhamChiTietFillReq);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/api/hdct/get-best-voucher")
    @ResponseBody
    public ResponseEntity<Object> getBestVoucher(
            @RequestParam BigDecimal tongTien
           ) {
        try {

            ListPggGioHang bestVoucher = hoaDonChiTietService.getBestPgg(_idHoaDon, tongTien, _idTaiKhoan);

            if (bestVoucher != null) {
                return ResponseEntity.ok(bestVoucher);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không có phiếu giảm giá phù hợp");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi hệ thống: " + e.getMessage());
        }
    }


}
