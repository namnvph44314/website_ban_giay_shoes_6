package com.group6.du_an_tot_nghiep.Dto.QuanLy.HoaDon;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SanPhamChiTietFillReq {

    private Integer trangThai;
    private String tenSanPham;
    private int  page;
    private int size;

    private String sort;
}
