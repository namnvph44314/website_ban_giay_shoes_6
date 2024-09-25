package com.group6.du_an_tot_nghiep.Dto.QuanLy.SanPham;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SanPhamCustom {
    private String tenSanPham;

    private int idChatLieu;

    private int idDanhMuc;

    private int idDeGiay;

    private int idThuongHieu;

    private String moTa;
}
