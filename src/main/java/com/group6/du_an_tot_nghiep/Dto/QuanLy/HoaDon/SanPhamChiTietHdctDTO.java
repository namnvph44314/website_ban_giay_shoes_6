package com.group6.du_an_tot_nghiep.Dto.QuanLy.HoaDon;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SanPhamChiTietHdctDTO {
    private Integer idHoaDon;
    private Integer idSanPhamChiTiet;
    private Integer soLuong;
    private String username;


}
