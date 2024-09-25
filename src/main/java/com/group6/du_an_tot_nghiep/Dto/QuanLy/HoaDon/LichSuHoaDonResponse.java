package com.group6.du_an_tot_nghiep.Dto.QuanLy.HoaDon;

import lombok.*;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LichSuHoaDonResponse {
     Timestamp ngayHoanThanh;
     BigDecimal tienKhachTra;
     Integer phuongthucthanhtoan;
     String ghiChu;
     String hoVaTen;
}
