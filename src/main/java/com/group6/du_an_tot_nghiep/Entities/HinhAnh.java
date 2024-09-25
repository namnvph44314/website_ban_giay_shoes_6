package com.group6.du_an_tot_nghiep.Entities;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "hinh_anh")
public class HinhAnh {
    private int id;
    private boolean anhMacDinh;
    private String url;
    private String nguoiTao;
    private Timestamp ngayTao;
    private String nguoiSua;
    private Timestamp ngaySua;
    private int trangThai;
    private SanPhamChiTiet sanPhamChiTietByIdSpct;

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
    @Column(name = "anh_mac_dinh")
    public boolean isAnhMacDinh() {
        return anhMacDinh;
    }

    public void setAnhMacDinh(boolean anhMacDinh) {
        this.anhMacDinh = anhMacDinh;
    }

    @Basic
    @Column(name = "url")
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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
        HinhAnh that = (HinhAnh) o;
        return id == that.id && anhMacDinh == that.anhMacDinh && trangThai == that.trangThai && Objects.equals(url, that.url) && Objects.equals(nguoiTao, that.nguoiTao) && Objects.equals(ngayTao, that.ngayTao) && Objects.equals(nguoiSua, that.nguoiSua) && Objects.equals(ngaySua, that.ngaySua);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, anhMacDinh, url, nguoiTao, ngayTao, nguoiSua, ngaySua, trangThai);
    }

    @ManyToOne
    @JoinColumn(name = "id_spct", referencedColumnName = "id", nullable = false)
    public SanPhamChiTiet getSanPhamChiTietByIdSpct() {
        return sanPhamChiTietByIdSpct;
    }

    public void setSanPhamChiTietByIdSpct(SanPhamChiTiet sanPhamChiTietByIdSpct) {
        this.sanPhamChiTietByIdSpct = sanPhamChiTietByIdSpct;
    }
}