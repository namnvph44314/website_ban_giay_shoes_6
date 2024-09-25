package com.group6.du_an_tot_nghiep.Entities;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "san_pham")
public class SanPham {
    private int id;
    private String tenSanPham;
    private Timestamp ngayTao;
    private String nguoiTao;
    private Timestamp ngaySua;
    private String nguoiSua;
    private int trangThai;

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
    @Column(name = "ten_san_pham")
    public String getTenSanPham() {
        return tenSanPham;
    }

    public void setTenSanPham(String tenSanPham) {
        this.tenSanPham = tenSanPham;
    }

    @Basic
    @Column(name = "ngay_tao")
    public Timestamp getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(Timestamp ngayTao) {
        this.ngayTao = ngayTao;
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
    @Column(name = "ngay_sua")
    public Timestamp getNgaySua() {
        return ngaySua;
    }

    public void setNgaySua(Timestamp ngaySua) {
        this.ngaySua = ngaySua;
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
    @Column(name = "trang_thai")
    public int getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(int trangThai) {
        this.trangThai = trangThai;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SanPham sanPham = (SanPham) o;
        return id == sanPham.id && trangThai == sanPham.trangThai && Objects.equals(tenSanPham, sanPham.tenSanPham) && Objects.equals(ngayTao, sanPham.ngayTao) && Objects.equals(nguoiTao, sanPham.nguoiTao) && Objects.equals(ngaySua, sanPham.ngaySua) && Objects.equals(nguoiSua, sanPham.nguoiSua);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tenSanPham, ngayTao, nguoiTao, ngaySua, nguoiSua, trangThai);
    }
}
