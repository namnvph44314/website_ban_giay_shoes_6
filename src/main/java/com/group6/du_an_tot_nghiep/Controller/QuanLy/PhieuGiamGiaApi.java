package com.group6.du_an_tot_nghiep.Controller.QuanLy;

import com.group6.du_an_tot_nghiep.Dto.QuanLy.PhieuGiamGia.PhieuGiamGiaRequest;
import com.group6.du_an_tot_nghiep.Dto.QuanLy.PhieuGiamGia.PhieuGiamGiaUpdate;
import com.group6.du_an_tot_nghiep.Dto.QuanLy.PhieuGiamGiaKhachHang.PhieuGiamGiaKhachHangAdd;
import com.group6.du_an_tot_nghiep.Entities.PhieuGiamGia;
import com.group6.du_an_tot_nghiep.Entities.PhieuGiamGiaKhachHang;
import com.group6.du_an_tot_nghiep.Entities.TaiKhoan;
import com.group6.du_an_tot_nghiep.Service.QuanLy.PhieuGiamGiaKhachHangService;
import com.group6.du_an_tot_nghiep.Service.QuanLy.PhieuGiamGiaService;
import com.group6.du_an_tot_nghiep.Service.QuanLy.TaiKhoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/kh/phieu-giam-gia/api")
public class PhieuGiamGiaApi {

    @Autowired
    PhieuGiamGiaService phieuGiamGiaService;
    @Autowired
    PhieuGiamGiaKhachHangService phieuGiamGiaKhachHangService;
    @Autowired
    TaiKhoanService taiKhoanService;

    @PostMapping("/add")
    public ResponseEntity<PhieuGiamGiaRequest> add(@RequestBody PhieuGiamGiaRequest phieuGiamGiaRequest) {
        return ResponseEntity.ok(phieuGiamGiaService.add(phieuGiamGiaRequest, "namnv"));
    }

    @PostMapping("/add-pgg-kh")
    public ResponseEntity<List<Integer>> addPggKh(@RequestBody List<Integer> phieuGiamGiaKhachHangAdd) {
        phieuGiamGiaKhachHangService.addPggKh(phieuGiamGiaKhachHangAdd, "namnv", phieuGiamGiaService.getOnePgg());
        return ResponseEntity.ok(phieuGiamGiaKhachHangAdd);
    }

    @GetMapping("/page-khach-hang")
    public ResponseEntity<Page<TaiKhoan>> getPageKh(@RequestParam("page") Optional<Integer> pageParam) {
        int page = pageParam.orElse(0);
        return ResponseEntity.ok(taiKhoanService.getPage(page));
    }

    @GetMapping("/search-khach-hang")
    public ResponseEntity<Page<TaiKhoan>> searchPageKh(@RequestParam("page") Optional<Integer> pageParam, @RequestParam("timKiem") String sdt) {
        int page = pageParam.orElse(0);
        return ResponseEntity.ok(taiKhoanService.getTkBySdt(sdt, page));
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<PhieuGiamGiaUpdate> detaiPgg(
            @PathVariable("id") PhieuGiamGia phieuGiamGia
    ) {
        return ResponseEntity.ok(phieuGiamGiaService.detail(phieuGiamGia.getId()));
    }

    @GetMapping("/detail-khach-hang/{id}")
    public ResponseEntity<Page<TaiKhoan>> detailKh(
            @PathVariable("id") PhieuGiamGia phieuGiamGia,
            @RequestParam("page") Optional<Integer> pageParam
    ){
        int page=pageParam.orElse(0);
        if(phieuGiamGia.getKieu()==1){
            return ResponseEntity.ok(taiKhoanService.getPage(page));
        }else{
            return ResponseEntity.ok(phieuGiamGiaKhachHangService.getListKhByIdPggChuaAdd(phieuGiamGia.getId(), page));
        }
    }

    @GetMapping("/search-khach-hang/{id}")
    public ResponseEntity<Page<TaiKhoan>> SearchKh(
            @PathVariable("id") PhieuGiamGia phieuGiamGia,
            @RequestParam("page") Optional<Integer> pageParam,
            @RequestParam("timKiem") Optional<String> timKiemParam
    ){
        int page=pageParam.orElse(0);
        if(phieuGiamGia.getKieu()==1){
            return ResponseEntity.ok(taiKhoanService.getTkBySdt(timKiemParam.get(),page));
        }else{
            return ResponseEntity.ok(phieuGiamGiaKhachHangService.getKhByIdPggChuaAdd(phieuGiamGia.getId(), timKiemParam,page));
        }
    }

    @GetMapping("/page-kh-pgg/{id}")
    public ResponseEntity<Page<PhieuGiamGiaKhachHang>> pageKhByPgg(
            @PathVariable("id") PhieuGiamGia phieuGiamGia,
            @RequestParam("pageKhByPgg") Optional<Integer> pageParam
    ){
        int page=pageParam.orElse(0);
        return ResponseEntity.ok(phieuGiamGiaKhachHangService.getListPggKhByIdPgg(phieuGiamGia.getId(), page));
    }

    @PutMapping("/update-pgg/{id}")
    public ResponseEntity<PhieuGiamGiaRequest> updatePgg(@RequestBody PhieuGiamGiaRequest phieuGiamGiaRequest,@PathVariable("id") PhieuGiamGia phieuGiamGia){
        return ResponseEntity.ok(phieuGiamGiaService.update(phieuGiamGia.getMaKhuyenMai(),phieuGiamGiaRequest,"namnv"));
    }

    @PostMapping("/add-pgg-kh-update/{id}")
    public ResponseEntity<List<Integer>> addPggKhUpdate(
            @RequestBody List<Integer> pggKh,
            @PathVariable("id") PhieuGiamGia phieuGiamGia
    ){
        phieuGiamGiaKhachHangService.addPggKh(pggKh,"namnv",phieuGiamGia);
        return ResponseEntity.ok(pggKh);
    }

    @GetMapping("/check-ma")
    public ResponseEntity<PhieuGiamGia> checkMa(
            @RequestParam("ma") String ma
    ){
        return ResponseEntity.ok(phieuGiamGiaService.getOneByMa(ma));
    }

    @DeleteMapping("/delete-pgg-kh")
    public ResponseEntity<Void> deletePggKh(
            @RequestParam("idPggKh") String idPggKh
    ){
        phieuGiamGiaKhachHangService.deletePggKh(Integer.parseInt(idPggKh));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/update-trang-thai/{id}")
    public ResponseEntity<Void> updateTrangThai(@PathVariable("id") PhieuGiamGia phieuGiamGia){
        phieuGiamGiaService.updatePggNhd(phieuGiamGia.getId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/get-so-luong-kh-so-huu/{id}")
    public ResponseEntity<Integer> getSoLuongKhSoHuu(
            @PathVariable("id") PhieuGiamGia phieuGiamGia
    ){
        return ResponseEntity.ok(phieuGiamGiaKhachHangService.soLuongKhSoHuu(phieuGiamGia.getId()));
    }

}
