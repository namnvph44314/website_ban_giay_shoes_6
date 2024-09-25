package com.group6.du_an_tot_nghiep.Dto.QuanLy.TaiKhoan;

import lombok.*;

import java.sql.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class NhanVienDetail {

    private int id;
    private String anh;
    private String tenKhachHang;
    private String email;
    private String soCanCuoc;
    private String sdt;
    private Date ngaySinh;
    private int gioiTinh;
    private int idTinh;
    private int idHuyen;
    private String idXa;
    private String diaChi;
    private String quyen;
    private int trangThai;


}
