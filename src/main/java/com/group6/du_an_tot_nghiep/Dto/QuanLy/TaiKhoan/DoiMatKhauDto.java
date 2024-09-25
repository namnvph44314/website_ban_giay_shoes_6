package com.group6.du_an_tot_nghiep.Dto.QuanLy.TaiKhoan;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DoiMatKhauDto {
    private int id;
    private String matKhau;
    private String nhapLaiMatKhau;

    public DoiMatKhauDto() {
        this.matKhau = "";
        this.nhapLaiMatKhau = "";
    }
}
