package com.group6.du_an_tot_nghiep.Entities;


import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Table(name = "gio_hang_chi_tiet", schema = "public", catalog = "shoes_6")
public class GioHangChiTiet {
    private int id;
    private int soLuong;
    private SanPhamChiTiet sanPhamChiTietByIdSpct;
    private GioHang gioHangByIdGioHang;
    private String nguoiTao;
    private Timestamp ngayTao;
    private Boolean selected;
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
    @Column(name = "so_luong")
    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
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
    @Column(name = "ngay_tao")
    @CreationTimestamp
    public Timestamp getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(Timestamp ngayTao) {
        this.ngayTao = ngayTao;
    }

    @Basic
    @Column(name = "selected")
    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    @Basic
    @Column(name = "trang_thai")
    public int getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(int trangThai) {
        this.trangThai = trangThai;
    }

    @ManyToOne
    @JoinColumn(name = "id_spct", referencedColumnName = "id")
    public SanPhamChiTiet getSanPhamChiTietByIdSpct() {
        return sanPhamChiTietByIdSpct;
    }

    public void setSanPhamChiTietByIdSpct(SanPhamChiTiet sanPhamChiTietByIdSpct) {
        this.sanPhamChiTietByIdSpct = sanPhamChiTietByIdSpct;
    }

    @ManyToOne
    @JoinColumn(name = "id_gio_hang", referencedColumnName = "id", nullable = false)
    public GioHang getGioHangByIdGioHang() {
        return gioHangByIdGioHang;
    }

    public void setGioHangByIdGioHang(GioHang gioHangByIdGioHang) {
        this.gioHangByIdGioHang = gioHangByIdGioHang;
    }
}
