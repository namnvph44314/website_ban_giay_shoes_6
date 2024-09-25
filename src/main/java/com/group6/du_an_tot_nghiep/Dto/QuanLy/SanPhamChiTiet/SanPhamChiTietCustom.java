package com.group6.du_an_tot_nghiep.Dto.QuanLy.SanPhamChiTiet;

import com.group6.du_an_tot_nghiep.Entities.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SanPhamChiTietCustom {
    private int canNang;

    private ChatLieu chatLieu;

    private TheLoai danhMuc;

    private DeGiay deGiay;

    private BigDecimal gia;

    private List<String> images;

    private KichThuoc kichCo;

    private MauSac mauSac;

    private String moTa;

    private Integer soLuong;

    private String ten;

    private ThuongHieu thuongHieu;
}
