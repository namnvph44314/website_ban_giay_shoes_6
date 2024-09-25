package com.group6.du_an_tot_nghiep.Controller.QuanLy;

import com.group6.du_an_tot_nghiep.Dao.HoaDonDao;
import com.group6.du_an_tot_nghiep.Dao.PhieuGiamGiaDao;
import com.group6.du_an_tot_nghiep.Dao.TaiKhoanDao;
import com.group6.du_an_tot_nghiep.Dto.KhachHang.ListPggGioHang;
import com.group6.du_an_tot_nghiep.Dto.QuanLy.HoaDon.*;
import com.group6.du_an_tot_nghiep.Dto.QuanLy.TaiKhoan.NhanVienFillDTO;
import com.group6.du_an_tot_nghiep.Dto.QuanLy.TaiKhoan.TaiKhoanRequest;
import com.group6.du_an_tot_nghiep.Entities.HoaDon;
import com.group6.du_an_tot_nghiep.Entities.PhieuGiamGia;
import com.group6.du_an_tot_nghiep.Entities.SanPhamChiTiet;
import com.group6.du_an_tot_nghiep.Entities.TaiKhoan;
import com.group6.du_an_tot_nghiep.Service.QuanLy.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class HoaDonTestController {

    @Autowired
    private HoaDonChiTietService hoaDonChiTietService;

    @Autowired
    private TrangThaiHoaDonService trangThaiHoaDonService;

    @Autowired
    private HoaDonService hoaDonService;

    @Autowired
    private HoaDonDao hoaDonDao;
    @Autowired
    private TaiKhoanDao taiKhoanDao;
    @Autowired
    private NhanVienService nhanVienService;

    @Autowired
    private PhieuGiamGiaDao phieuGiamGiaDao;

 
//    @PostMapping("/nhan-vien/search")
//    public ResponseEntity<Page<TaiKhoan>> searchTaiKhoan(@RequestBody NhanVienFillDTO nhanVienFillDTO) {
//        try {
//            Page<TaiKhoan> result = nhanVienService.searchByNhanVien(nhanVienFillDTO);
//            return new ResponseEntity<>(result, HttpStatus.OK);
//        } catch (Exception e) {
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//    @GetMapping("/hien-thi-trang-thai/{idHoaDon}")
//    @ResponseBody
//    public ResponseEntity<List<TrangThaiHoaDonRepose>> getAllTrangThaiHoaDon(@PathVariable Integer idHoaDon) {
//        return new ResponseEntity<>(hoaDonChiTietService.getAllTrangThaiHoaDon(idHoaDon), HttpStatus.OK);
//    }
//
//    @PostMapping("/confirm-order/{idHoaDon}")
//
//    public ResponseEntity<MessageResponse> confirmOrder(
//            @RequestBody @Valid TrangThaiHoaDonRequest request,
//            @PathVariable Integer idHoaDon) {
//
//        try {
//            // Gọi service để xử lý logic
//            MessageResponse response = trangThaiHoaDonService.confirmOrder(idHoaDon, request);
//
//            return ResponseEntity.ok(response);
//        } catch (Exception e) {
//            // Xử lý ngoại lệ và trả về lỗi nếu có
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(new MessageResponse("Error: " + e.getMessage()));
//        }
//    }
//
//    @PutMapping("/cancel/{hoadonId}")
//    public ResponseEntity<MessageResponse> cancelOrder(
//            @PathVariable Integer hoadonId,
//            @RequestBody TrangThaiHoaDonRequest request,
//            @RequestParam Integer idTaiKhoan) {
//
//        try {
//            // Gọi dịch vụ để hủy đơn hàng
//            MessageResponse response = trangThaiHoaDonService.cancelOrder(hoadonId, request, idTaiKhoan);
//            return ResponseEntity.ok(response);
//        } catch (Exception e) {
//            // Xử lý lỗi và trả về thông báo lỗi
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                    .body(MessageResponse.builder().message(e.getMessage()).build());
//        }
//    }
//
//    @PostMapping("/rollback/{hoadonId}")
//    public MessageResponse rollbackOrderStatus(@PathVariable Integer hoadonId, @RequestBody TrangThaiHoaDonRequest request) {
//        return trangThaiHoaDonService.rollbackOrderStatus(hoadonId, request);
//    }

//    @PostMapping("/them/{idHoaDon}")
//    public ResponseEntity<MessageResponse> themSanPhamVaoHoaDonChiTiet(
//            @PathVariable Integer idHoaDon,
//            @RequestParam Integer idSanPhamChiTiet,
//            @RequestParam int soLuong,
//            @RequestParam String username) {
//        try {
//            MessageResponse response = hoaDonChiTietService.themSanPhamVaoHoaDonChiTiet(idHoaDon, idSanPhamChiTiet, soLuong, username);
//            return new ResponseEntity<>(response, HttpStatus.OK);
//        } catch (Exception e) {
//            return new ResponseEntity<>(MessageResponse.builder().message("Lỗi: " + e.getMessage()).build(), HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

//    @PostMapping("/search-hd")
//    @ResponseBody
//    public Page<HoaDonResponse> search_hd(@RequestBody HoaDonFillRequest hoaDonFillRequest) {
//        return hoaDonService.search(hoaDonFillRequest);
//    }
//    @GetMapping("/dssp/{idHoaDon}")
//
//    public ResponseEntity<List<SanPhamChiTietDTO>> getAllSp(@PathVariable Integer idHoaDon){
//        return new ResponseEntity<>(hoaDonChiTietService.findSanPhamChiTietByIdHoaDon(idHoaDon), HttpStatus.OK);
//    }

//    @GetMapping("tong-tien-don-hang/{id}")
//    public ResponseEntity<BigDecimal> tongTienSanPham(@PathVariable("id") Integer id) {
//        return new ResponseEntity<>(hoaDonChiTietService.tongTienHang(id), HttpStatus.OK);
//    }
//@PutMapping("/update/{idHoaDon}")
//public ResponseEntity<?> updateHoaDon(
//        @PathVariable Integer idHoaDon,
//        @RequestParam Integer soLuong,
//        @RequestParam Integer idHoaDonChiTiet) {
//    try {
//        HoaDon updatedHoaDon = hoaDonChiTietService.updateHoaDon(idHoaDon, soLuong, idHoaDonChiTiet);
//        return ResponseEntity.ok(updatedHoaDon);
//    } catch (RuntimeException e) {
//        // Trả về thông báo lỗi cụ thể nếu xảy ra ngoại lệ RuntimeException
//        return ResponseEntity.badRequest().body("Lỗi: " + e.getMessage());
//    } catch (Exception e) {
//        // Trả về thông báo lỗi chung cho các ngoại lệ khác
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Có lỗi xảy ra. Vui lòng thử lại sau.");
//    }
//}


//
//    @DeleteMapping("/xoa/{idHoaDon}")
//    public ResponseEntity<MessageResponse> xoaSanPhamKhoiHoaDon(@PathVariable Integer idHoaDon,
//                                                                @RequestParam Integer idSanPhamChiTiet) {
//        MessageResponse response = hoaDonChiTietService.xoaSanPhamKhoiHoaDonChiTiet(idHoaDon, idSanPhamChiTiet);
//        return ResponseEntity.ok(response);
//    }

//    @PostMapping("/applyVoucherAndPayment")
//    public ResponseEntity<HoaDon> applyVoucherAndPayment(@RequestParam Integer hoaDonId, @RequestParam Integer phieuGiamGiaId, @RequestParam BigDecimal ktVoucher) {
//        Optional<HoaDon> optionalHoaDon = hoaDonDao.findById(hoaDonId);
//        Optional<PhieuGiamGia> optionalPhieuGiamGia = phieuGiamGiaDao.findById(phieuGiamGiaId);
//
//        if (optionalHoaDon.isPresent() && optionalPhieuGiamGia.isPresent()) {
//            HoaDon hoaDon = optionalHoaDon.get();
//            PhieuGiamGia phieuGiamGia = optionalPhieuGiamGia.get();
//            hoaDonChiTietService.applyVoucherAndPayment(hoaDon, phieuGiamGia, ktVoucher);
//            hoaDonDao.save(hoaDon);
//            return ResponseEntity.ok(hoaDon);
//        } else {
//            return ResponseEntity.badRequest().build();
//        }
//    }
//        @PostMapping("/add-voucher/{idHoaDon}")
//        public ResponseEntity<MessageResponse> addPhieuGiamGiaOrder(
//        @PathVariable Integer idHoaDon,
//        @RequestParam(required = false) Integer idPhieuGiamGia,
//        @RequestParam String hoVaTen) {
//    try {
//        MessageResponse response = hoaDonChiTietService.addPhieuGiamGiaOrder(idHoaDon, idPhieuGiamGia, hoVaTen);
//        return ResponseEntity.ok(response);
//    } catch (Exception e) {
//        return ResponseEntity.status(500).body(MessageResponse.builder().message("Lỗi hệ thống: " + e.getMessage()).build());
//    }
//}

//    @PostMapping("/searchBySp")
//    public Page<SanPhamChiTiet> searchBySp(@RequestBody SanPhamChiTietFillReq sanPhamChiTietFillReq) {
//
//        return hoaDonChiTietService.searchBySanPham(sanPhamChiTietFillReq);
//
//    }


//    @PostMapping("/get-best-voucher")
//    @ResponseBody
//    public ResponseEntity<Object> getBestVoucher(
//            @RequestParam Integer idHoaDon,
//            @RequestParam BigDecimal tongTien,
//            @RequestParam(required = false) Integer taiKhoanId) {
//        try {
//            TaiKhoan taiKhoan = taiKhoanId != null ? taiKhoanDao.findById(taiKhoanId).orElse(null) : null;
//            ListPggGioHang bestVoucher = hoaDonChiTietService.getBestPgg(idHoaDon,tongTien, taiKhoan);
//
//            if (bestVoucher != null) {
//                return ResponseEntity.ok(bestVoucher);
//            } else {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không có phiếu giảm giá phù hợp");
//            }
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi hệ thống: " + e.getMessage());
//        }
//    }

}
