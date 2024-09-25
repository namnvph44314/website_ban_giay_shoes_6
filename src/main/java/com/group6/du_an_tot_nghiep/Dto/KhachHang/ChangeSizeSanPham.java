package com.group6.du_an_tot_nghiep.Dto.KhachHang;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeSizeSanPham {
    private int idSpct;
    private BigDecimal gia;
    private BigDecimal giaGiam;
    private int soLuongKho;
}
