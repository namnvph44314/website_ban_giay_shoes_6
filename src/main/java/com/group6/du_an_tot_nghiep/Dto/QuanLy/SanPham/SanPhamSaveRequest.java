package com.group6.du_an_tot_nghiep.Dto.QuanLy.SanPham;

import com.group6.du_an_tot_nghiep.Dto.QuanLy.SanPhamChiTiet.SanPhamChiTietCustom;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SanPhamSaveRequest {
    private SanPhamCustom sanPham;

    private List<SanPhamChiTietCustom> sanPhamList;
}
