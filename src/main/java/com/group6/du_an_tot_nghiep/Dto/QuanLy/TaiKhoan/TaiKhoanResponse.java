package com.group6.du_an_tot_nghiep.Dto.QuanLy.TaiKhoan;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaiKhoanResponse {
    private int id;
    private String anh;
    private String ma;
    private String hoTen;
    private String email;
    private String sdt;
    private Date ngaySinh;
    private Integer gioiTinh;
    private String quyen;
    private int trangThai;

    public String getGt(int gioiTinh){
        if(gioiTinh==1){
            return "Nam";
        }
        return "Nữ";
    }
}