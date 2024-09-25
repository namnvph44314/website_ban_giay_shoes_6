package com.group6.du_an_tot_nghiep.Entities;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "giam_gia_san_pham", schema = "public", catalog = "shoes_6")
public class GiamGiaSanPham {
    private String tenGiamGia;
    private Timestamp ngayBatDau;
    private Timestamp ngayKetThuc;
    private int giaTri;
    private int id;
    private int loaiGiamGia;
    private String nguoiTao;
    private Timestamp ngayTao;
    private String nguoiSua;
    private Timestamp ngaySua;
    private int trangThai;

    public String getTT(byte trangThai) {
        if (trangThai == 0) {
            return "Dừng hoạt động";
        }else if(trangThai==2){
            return "Chưa diễn ra";
        }else if(trangThai==1){
            return "Đang diễn ra";
        }

        return "Hết hạn";

    }

    @Basic
    @Column(name = "ten_giam_gia")
    public String getTenGiamGia() {
        return tenGiamGia;
    }

    public void setTenGiamGia(String tenGiamGia) {
        this.tenGiamGia = tenGiamGia;
    }

    @Basic
    @Column(name = "ngay_bat_dau")
    public Timestamp getNgayBatDau() {
        return ngayBatDau;
    }

    public void setNgayBatDau(Timestamp ngayBatDau) {
        this.ngayBatDau = ngayBatDau;
    }

    @Basic
    @Column(name = "ngay_ket_thuc")
    public Timestamp getNgayKetThuc() {
        return ngayKetThuc;
    }

    public void setNgayKetThuc(Timestamp ngayKetThuc) {
        this.ngayKetThuc = ngayKetThuc;
    }

    @Basic
    @Column(name = "gia_tri")
    public int getGiaTri() {
        return giaTri;
    }

    public void setGiaTri(int giaTri) {
        this.giaTri = giaTri;
    }

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
    @Column(name = "loai_giam_gia")
    public int getLoaiGiamGia() {
        return loaiGiamGia;
    }

    public void setLoaiGiamGia(int loaiGiamGia) {
        this.loaiGiamGia = loaiGiamGia;
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
        GiamGiaSanPham that = (GiamGiaSanPham) o;
        return giaTri == that.giaTri && id == that.id && loaiGiamGia == that.loaiGiamGia && trangThai == that.trangThai && Objects.equals(tenGiamGia, that.tenGiamGia) && Objects.equals(ngayBatDau, that.ngayBatDau) && Objects.equals(ngayKetThuc, that.ngayKetThuc) && Objects.equals(nguoiTao, that.nguoiTao) && Objects.equals(ngayTao, that.ngayTao) && Objects.equals(nguoiSua, that.nguoiSua) && Objects.equals(ngaySua, that.ngaySua);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tenGiamGia, ngayBatDau, ngayKetThuc, giaTri, id, loaiGiamGia, nguoiTao, ngayTao, nguoiSua, ngaySua, trangThai);
    }
}
