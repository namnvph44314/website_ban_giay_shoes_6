package com.group6.du_an_tot_nghiep.Dto.KhachHang;

import com.group6.du_an_tot_nghiep.Entities.GioHangChiTiet;
import com.group6.du_an_tot_nghiep.Entities.KichThuoc;
import com.group6.du_an_tot_nghiep.Entities.MauSac;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListGioHangChiTietResponse {
    private int idGhct;
    private int idSpct;
    private BigDecimal giaGiam;
    private String tenSanPham;
    private int soLuongMua;
    private int soLuongKho;
    private KichThuoc kichThuoc;
    private MauSac mauSac;
    private boolean selected;
}
