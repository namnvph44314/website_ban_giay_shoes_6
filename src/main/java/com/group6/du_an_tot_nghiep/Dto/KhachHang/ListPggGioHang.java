package com.group6.du_an_tot_nghiep.Dto.KhachHang;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListPggGioHang {
    private int idPgg;
    private String maPgg;
    private BigDecimal giaTriGiam;
    private BigDecimal giamToiDa;
    private BigDecimal donToiThieu;
    private int kieuGiaTri;
    private Timestamp ngayKetThuc;
    private int soLuong;
    private BigDecimal tienDuocGiamTheoDon;
}
