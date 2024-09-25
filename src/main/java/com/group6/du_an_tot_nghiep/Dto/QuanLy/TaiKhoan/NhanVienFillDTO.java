package com.group6.du_an_tot_nghiep.Dto.QuanLy.TaiKhoan;

import lombok.Data;



@Data
public class NhanVienFillDTO {
    private String search;
    private String hoVaTen;
    private String soDienThoai;
    private Integer gioiTinh;
    private Integer trangThai;
    private String quyen;

    private int page;
    private int size;
    private String sort;

}
