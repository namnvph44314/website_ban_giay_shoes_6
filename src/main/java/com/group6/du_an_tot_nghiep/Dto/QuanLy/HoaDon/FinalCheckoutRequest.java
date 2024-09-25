package com.group6.du_an_tot_nghiep.Dto.QuanLy.HoaDon;

import com.group6.du_an_tot_nghiep.Entities.DiaChi;
import com.group6.du_an_tot_nghiep.Entities.TaiKhoan;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FinalCheckoutRequest {
    private BigDecimal tienSauGiam;

    private BigDecimal tienGiamGia;

    private BigDecimal tienGiaoHang;

    private String soDienThoai;

    private String diaChi;

    private Integer trangThai;

    private DiaChi diaChiNew;

    private Integer phuongThucThanhToan;

    private TaiKhoan khachHang;

    private BigDecimal tongTien;

    private Integer idKhachHang;

    private String tenKhachHang;

    private Integer idTinh;

    private Integer idHuyen;

    private Integer idXa;
}
