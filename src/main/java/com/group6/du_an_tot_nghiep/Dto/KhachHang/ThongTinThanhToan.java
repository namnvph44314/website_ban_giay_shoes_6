package com.group6.du_an_tot_nghiep.Dto.KhachHang;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ThongTinThanhToan {
    BigDecimal tongTienTruocGiam;
    BigDecimal tongTienSauGiam;
    BigDecimal tienGiam;
    String maPgg;
    int idPgg;
    int phuongThucThanhToan;
    BigDecimal tienGiaoHang;
}
