package com.group6.du_an_tot_nghiep.Dto.QuanLy.HoaDon;

import com.group6.du_an_tot_nghiep.Entities.PhieuGiamGia;
import com.group6.du_an_tot_nghiep.Entities.TaiKhoan;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BillResponse {
    private int id;
    private Integer idTaiKhoan;
    private PhieuGiamGia idPhieuGiamGia;
    private String maHoaDon;
    private String email;
    private String hoVaTen;
    private String soDienThoai;
    private String ghiChu;
    private String diaChi;
    private BigDecimal tienKhachTra;
    private BigDecimal tienSauGiam;
    private BigDecimal tienGiamGia;
    private BigDecimal tienGiaoHang;

    private int loaiDonHang;
    private String soDienThoaiNhan;
    private Timestamp ngayHoanThanh;
    private Timestamp ngayXacNhan;
    private BigDecimal tongTien;

    private Timestamp ngayGiaoHang;
    private Timestamp ngayNhanHangDuKien;
    private int phuongthucthanhtoan;
    private Timestamp ngayNhan;
    private int trangThai;
    private Timestamp ngayTao;
    private String nguoiTao;
    private Timestamp ngaySua;
    private String nguoiSua;
}
