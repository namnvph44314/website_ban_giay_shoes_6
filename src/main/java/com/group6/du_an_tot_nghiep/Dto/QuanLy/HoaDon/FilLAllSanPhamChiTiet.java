package com.group6.du_an_tot_nghiep.Dto.QuanLy.HoaDon;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FilLAllSanPhamChiTiet {
    private Integer idSanPhamChiTiet;

    private String imgage;

    private String tenSanPham;

    private String tenChatLieu;

    private String giaBan;

    private String giaGiamGia;

    private Integer soLuong;

    private String tenMauSac;

    private Float tenKichThuoc;



    private Integer idThuongHieu;

    private int  page;
    private int size;


}
