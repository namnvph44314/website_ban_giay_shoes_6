package com.group6.du_an_tot_nghiep.Dto.QuanLy.HoaDon;


import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MoneyReponse {
    private BigDecimal tienGiamGia;
    private BigDecimal tienGiaoHang;
    private BigDecimal tongTien;
    private BigDecimal tienSauGiam;

    private BigDecimal tienHang;
}
