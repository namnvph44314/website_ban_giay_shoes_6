package com.group6.du_an_tot_nghiep.Dto.QuanLy.TaiKhoan;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;

@Data
@AllArgsConstructor
public class TaiKhoanRequest {

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
    private int trangThai;
    public TaiKhoanRequest() {
        super();
    }

}
