package com.group6.du_an_tot_nghiep.Dto.KhachHang;

import com.group6.du_an_tot_nghiep.Entities.KichThuoc;
import com.group6.du_an_tot_nghiep.Entities.MauSac;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListSanPhamResponse {
    private int id;
    private String ma;
    private BigDecimal gia;
    private String tenSanPham;
    private int giaTri;
    private MauSac mauSac;
    private KichThuoc kichThuoc;
    private int loaiGiamGia;
    private BigDecimal giaGiam;
    private Timestamp ngayTao;
}
