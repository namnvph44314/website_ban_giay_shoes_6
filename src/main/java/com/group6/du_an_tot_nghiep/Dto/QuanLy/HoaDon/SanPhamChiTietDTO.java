package com.group6.du_an_tot_nghiep.Dto.QuanLy.HoaDon;

import com.group6.du_an_tot_nghiep.Entities.SanPhamChiTiet;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SanPhamChiTietDTO {

  private  SanPhamChiTiet sanPhamChiTiet;
  private Integer soLuongSanPham;
  private BigDecimal gia;
  private Integer idHoaDonChiTiet;

}
