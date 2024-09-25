package com.group6.du_an_tot_nghiep.Dto.QuanLy.HoaDon;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TrangThaiHoaDonRepose {

    private Integer trangThai;

    private Timestamp ngayTao;

    private String hovaTen;

    private String ghiChu;

    private String nguoiTao;


}
