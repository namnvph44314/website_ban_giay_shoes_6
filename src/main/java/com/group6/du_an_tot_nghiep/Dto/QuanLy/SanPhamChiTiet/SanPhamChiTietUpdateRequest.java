package com.group6.du_an_tot_nghiep.Dto.QuanLy.SanPhamChiTiet;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class SanPhamChiTietUpdateRequest {
    private Integer id;

    private Integer danhMuc;

    private Integer thuongHieu;

    private Integer deGiay;

    private Integer chatLieu;

    private Integer mauSac;

    private Integer kichCo;

    private Integer canNang;

    private Integer soLuong;

    private Integer donGia;

    private String moTa;
}
