package com.group6.du_an_tot_nghiep.Dto.KhachHang;

import com.group6.du_an_tot_nghiep.Entities.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Data
@AllArgsConstructor
public class KhachHangSanPhamResponse {
    private int idSpct;
    private BigDecimal gia;
    private String tenSanPham;
    private BigDecimal giaGiam;
    private int soLuongKho;
    private int soLuongMua;
    private String moTa;
    private ChatLieu chatLieu;
    private DeGiay deGiay;
    private List<KichThuoc> kichThuocList;
    private List<MauSac> mauSacList;
    private int mauDangHien;
    private String tenMauDangHien;
    private int sizeDangHien;
    private TheLoai theLoai;
    private ThuongHieu thuongHieu;
    private List<HinhAnh> hinhAnhList;
    private List<HinhAnh> anhMacDinhList;
    public KhachHangSanPhamResponse() {
        this.soLuongMua = 0;
    }
}
