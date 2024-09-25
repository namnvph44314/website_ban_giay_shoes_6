package com.group6.du_an_tot_nghiep.Dto.KhachHang;

import com.group6.du_an_tot_nghiep.Entities.HinhAnh;
import com.group6.du_an_tot_nghiep.Entities.KichThuoc;
import com.group6.du_an_tot_nghiep.Entities.MauSac;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SanPhamLienQuan {
    private int idSpct;
    private BigDecimal gia;
    private String tenSanPham;
    private BigDecimal giaGiam;
    private MauSac mauSac;
    private KichThuoc kichThuoc;
}
