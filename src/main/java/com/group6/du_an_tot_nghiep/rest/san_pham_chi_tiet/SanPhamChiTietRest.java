package com.group6.du_an_tot_nghiep.rest.san_pham_chi_tiet;

import com.group6.du_an_tot_nghiep.Entities.SanPhamChiTiet;
import com.group6.du_an_tot_nghiep.Service.QuanLy.SanPhamChiTietService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/detail-product")
public class SanPhamChiTietRest {

    private SanPhamChiTietService sanPhamChiTietService;

    @Autowired
    public SanPhamChiTietRest(SanPhamChiTietService sanPhamChiTietService) {
        this.sanPhamChiTietService = sanPhamChiTietService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<SanPhamChiTiet> getDetailProduct(@PathVariable("id") Integer productId) {
        return ResponseEntity.ok(sanPhamChiTietService.findById(productId));
    }
}
