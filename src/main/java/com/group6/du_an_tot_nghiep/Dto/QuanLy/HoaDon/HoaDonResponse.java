package com.group6.du_an_tot_nghiep.Dto.QuanLy.HoaDon;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class HoaDonResponse {
    private Integer id;
    private String maHoaDon;
    private String email;
    private String hoVaTen;
    private String diaChi;
    private int LoaiDonHang;
    private String soDienThoaiNhan;
    private BigDecimal tongTien;
    private int trangThai;
    private Timestamp ngayTao;


}
