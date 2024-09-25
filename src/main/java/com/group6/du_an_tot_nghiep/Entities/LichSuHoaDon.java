package com.group6.du_an_tot_nghiep.Entities;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "lich_su_hoa_don", schema = "public", catalog = "shoes_6")
public class LichSuHoaDon {
    private int id;
    private int idTaiKhoan;
    private int idHoaDon;
    private int trangThai;
    private String ghiChu;
    private String nguoiTao;
    private Timestamp ngayTao;
    private String nguoiSua;
    private Timestamp ngaySua;
    private TaiKhoan taiKhoanByIdTaiKhoan;
    private HoaDon hoaDonByIdHoaDon;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    @Basic
    @Column(name = "trang_thai")
    public int getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(int trangThai) {
        this.trangThai = trangThai;
    }

    @Basic
    @Column(name = "ghi_chu")
    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    @Basic
    @Column(name = "nguoi_tao")
    public String getNguoiTao() {
        return nguoiTao;
    }

    public void setNguoiTao(String nguoiTao) {
        this.nguoiTao = nguoiTao;
    }

    @Basic
    @CreationTimestamp
    @Column(name = "ngay_tao")
    public Timestamp getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(Timestamp ngayTao) {
        this.ngayTao = ngayTao;
    }

    @Basic
    @Column(name = "nguoi_sua")
    public String getNguoiSua() {
        return nguoiSua;
    }

    public void setNguoiSua(String nguoiSua) {
        this.nguoiSua = nguoiSua;
    }

    @Basic
    @Column(name = "ngay_sua")
    public Timestamp getNgaySua() {
        return ngaySua;
    }

    public void setNgaySua(Timestamp ngaySua) {
        this.ngaySua = ngaySua;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LichSuHoaDon that = (LichSuHoaDon) o;
        return id == that.id && idTaiKhoan == that.idTaiKhoan && idHoaDon == that.idHoaDon && trangThai == that.trangThai  && Objects.equals(ghiChu, that.ghiChu) && Objects.equals(nguoiTao, that.nguoiTao) && Objects.equals(ngayTao, that.ngayTao) && Objects.equals(nguoiSua, that.nguoiSua) && Objects.equals(ngaySua, that.ngaySua);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, idTaiKhoan, idHoaDon, trangThai, ghiChu, nguoiTao, ngayTao, nguoiSua, ngaySua);
    }

    @ManyToOne
    @JoinColumn(name = "id_tai_khoan", referencedColumnName = "id", nullable = false)
    public TaiKhoan getTaiKhoanByIdTaiKhoan() {
        return taiKhoanByIdTaiKhoan;
    }

    public void setTaiKhoanByIdTaiKhoan(TaiKhoan taiKhoanByIdTaiKhoan) {
        this.taiKhoanByIdTaiKhoan = taiKhoanByIdTaiKhoan;
    }

    @ManyToOne
    @JoinColumn(name = "id_hoa_don", referencedColumnName = "id", nullable = false)
    public HoaDon getHoaDonByIdHoaDon() {
        return hoaDonByIdHoaDon;
    }

    public void setHoaDonByIdHoaDon(HoaDon hoaDonByIdHoaDon) {
        this.hoaDonByIdHoaDon = hoaDonByIdHoaDon;
    }
}