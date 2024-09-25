package com.group6.du_an_tot_nghiep.Entities;

import jakarta.persistence.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigInteger;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "tai_khoan")
public class TaiKhoan {
    private Integer id;
    private String ma;
    private String soCanCuoc;
    private String hoVaTen;
    private Integer gioiTinh;
    private Date ngaySinh;
    private String soDienThoai;
    private String email;
    private String matKhau;
    private String quyen;
    private String anhDaiDien;
    private String nguoiTao;
    private Timestamp ngayTao;
    private String nguoiSua;
    private Timestamp ngaySua;
    private int trangThai;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "ma")
    public String getMa() {
        return ma;
    }

    public void setMa(String ma) {
        this.ma = ma;
    }

    @Basic
    @Column(name = "so_can_cuoc")
    public String getSoCanCuoc() {
        return soCanCuoc;
    }

    public void setSoCanCuoc(String soCanCuoc) {
        this.soCanCuoc = soCanCuoc;
    }

    @Basic
    @Column(name = "ho_va_ten")
    public String getHoVaTen() {
        return hoVaTen;
    }

    public void setHoVaTen(String hoVaTen) {
        this.hoVaTen = hoVaTen;
    }

    @Basic
    @Column(name = "gioi_tinh")
    public Integer getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(Integer gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    @Basic
    @Column(name = "ngay_sinh")
    public Date getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(Date ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    @Basic
    @Column(name = "so_dien_thoai")
    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    @Basic
    @Column(name = "email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Basic
    @Column(name = "mat_khau")
    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    @Basic
    @Column(name = "quyen")
    public String getQuyen() {
        return quyen;
    }

    public void setQuyen(String quyen) {
        this.quyen = quyen;
    }

    @Basic
    @Column(name = "anh_dai_dien")
    public String getAnhDaiDien() {
        return anhDaiDien;
    }

    public void setAnhDaiDien(String anhDaiDien) {
        this.anhDaiDien = anhDaiDien;
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
        TaiKhoan taiKhoan = (TaiKhoan) o;
        return id == taiKhoan.id && gioiTinh == taiKhoan.gioiTinh && ngaySinh == taiKhoan.ngaySinh && matKhau == taiKhoan.matKhau && quyen == taiKhoan.quyen && trangThai == taiKhoan.trangThai && Objects.equals(soCanCuoc, taiKhoan.soCanCuoc) && Objects.equals(hoVaTen, taiKhoan.hoVaTen) && Objects.equals(soDienThoai, taiKhoan.soDienThoai) && Objects.equals(email, taiKhoan.email) && Objects.equals(anhDaiDien, taiKhoan.anhDaiDien) && Objects.equals(nguoiTao, taiKhoan.nguoiTao) && Objects.equals(ngayTao, taiKhoan.ngayTao) && Objects.equals(nguoiSua, taiKhoan.nguoiSua) && Objects.equals(ngaySua, taiKhoan.ngaySua);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, soCanCuoc, hoVaTen, gioiTinh, ngaySinh, soDienThoai, email, matKhau, quyen, anhDaiDien, nguoiTao, ngayTao, nguoiSua, ngaySua, trangThai);
    }
}

