package com.group6.du_an_tot_nghiep.Dto.QuanLy.HoaDon;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SanPhamHoaDonChiTietResponse {
    private Integer idHoaDonChiTiet;
    private String url;
    private String tenSanPham;
    private BigDecimal gia;
    private BigDecimal tienSauGiam;
    private int soLuongSanPham;
    private double tenKichThuoc;
}