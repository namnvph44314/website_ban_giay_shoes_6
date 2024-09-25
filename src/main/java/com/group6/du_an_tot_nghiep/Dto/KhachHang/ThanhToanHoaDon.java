package com.group6.du_an_tot_nghiep.Dto.KhachHang;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ThanhToanHoaDon {
    private ThongTinThanhToan thongTinThanhToan;
    private List<ListGioHangChiTietResponse> listSanPham;
    private ThongTinKhachHang thongTinKhachHang;
}
