package com.group6.du_an_tot_nghiep.Dto.QuanLy.HoaDon;

import com.group6.du_an_tot_nghiep.Entities.SanPhamChiTiet;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SanPhamCtHoaDon {

    private SanPhamChiTiet sanPhamChiTiet;
    private Integer soLuongSanPham;

}
