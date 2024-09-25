package com.group6.du_an_tot_nghiep.Entities;

import jakarta.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "phieu_giam_gia")
public class PhieuGiamGia implements Serializable {
    private int id;
    private String maKhuyenMai;
    private String ten;
    private BigDecimal giaTriToiDa;
    private BigDecimal giaTriNhoNhat;
    private int soLuong;
    private byte trangThai;
    private int kieu;
    private int kieuGiaTri;
    private BigDecimal giaTri;
    private Timestamp ngayBatDau;
    private Timestamp ngayKetThuc;
    private String nguoiTao;
    private Timestamp ngayTao;
    private String nguoiSua;
    private Timestamp ngaySua;

    public String getTT(byte trangThai) {
        if (trangThai == 0) {
            return "Hết hạn";
        }else if(trangThai==2){
            return "Chưa diễn ra";
        }else if(trangThai==4){
            return "Dừng hoạt động";
        }

        return "Đang diễn ra";

    }

    public String getKieu(int kieu) {
        if (kieu == 0) {
            return "Cá nhân";
        }
        return "Công khai";
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
    @Column(name = "ma_khuyen_mai")
    public String getMaKhuyenMai() {
        return maKhuyenMai;
    }

    public void setMaKhuyenMai(String maKhuyenMai) {
        this.maKhuyenMai = maKhuyenMai;
    }

    @Basic
    @Column(name = "ten")
    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    @Basic
    @Column(name = "gia_tri_toi_da")
    public BigDecimal getGiaTriToiDa() {
        return giaTriToiDa;
    }

    public void setGiaTriToiDa(BigDecimal giaTriToiDa) {
        this.giaTriToiDa = giaTriToiDa;
    }

    @Basic
    @Column(name = "gia_tri_nho_nhat")
    public BigDecimal getGiaTriNhoNhat() {
        return giaTriNhoNhat;
    }

    public void setGiaTriNhoNhat(BigDecimal giaTriNhoNhat) {
        this.giaTriNhoNhat = giaTriNhoNhat;
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
    @Column(name = "trang_thai")
    public byte getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(byte trangThai) {
        this.trangThai = trangThai;
    }

    @Basic
    @Column(name = "kieu")
    public int getKieu() {
        return kieu;
    }

    public void setKieu(int kieu) {
        this.kieu = kieu;
    }

    @Basic
    @Column(name = "kieu_gia_tri")
    public int getKieuGiaTri() {
        return kieuGiaTri;
    }

    public void setKieuGiaTri(int kieuGiaTri) {
        this.kieuGiaTri = kieuGiaTri;
    }

    @Basic
    @Column(name = "gia_tri")
    public BigDecimal getGiaTri() {
        return giaTri;
    }

    public void setGiaTri(BigDecimal giaTri) {
        this.giaTri = giaTri;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PhieuGiamGia that = (PhieuGiamGia) o;
        return id == that.id && soLuong == that.soLuong && trangThai == that.trangThai && kieu == that.kieu && giaTri == that.giaTri && Objects.equals(maKhuyenMai, that.maKhuyenMai) && Objects.equals(ten, that.ten) && Objects.equals(giaTriToiDa, that.giaTriToiDa) && Objects.equals(giaTriNhoNhat, that.giaTriNhoNhat) && Objects.equals(kieuGiaTri, that.kieuGiaTri) && Objects.equals(ngayBatDau, that.ngayBatDau) && Objects.equals(ngayKetThuc, that.ngayKetThuc) && Objects.equals(nguoiTao, that.nguoiTao) && Objects.equals(ngayTao, that.ngayTao) && Objects.equals(nguoiSua, that.nguoiSua) && Objects.equals(ngaySua, that.ngaySua);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, maKhuyenMai, ten, giaTriToiDa, giaTriNhoNhat, soLuong, trangThai, kieu, kieuGiaTri, giaTri, ngayBatDau, ngayKetThuc, nguoiTao, ngayTao, nguoiSua, ngaySua);
    }
}
