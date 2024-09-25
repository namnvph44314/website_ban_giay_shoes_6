package com.group6.du_an_tot_nghiep.Dto.QuanLy.HoaDon;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ThongTinDonHang {
    String maHoaDon;
    String email;
    String hoVaTen;
    String diaChi;
    Integer loaiDonHang;
    String soDienThoaiNhan;
    Integer trangThai;
    Integer idTaiKhoan;
    Integer idTinh;
    Integer idHuyen;
    String idXa;
    String ghiChu;
    Integer phuongthucthanhtoan;
}
