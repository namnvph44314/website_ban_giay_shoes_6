package com.group6.du_an_tot_nghiep.Entities;

import com.group6.du_an_tot_nghiep.Entities.TaiKhoan;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "dia_chi", schema = "public", catalog = "shoes_6")
public class DiaChi {
    private int id;
    private String email;
    private int idTinh;
    private int idHuyen;
    private String idXa;
    private String soDienThoai;
    private String diaChiCuThe;
    private int loai;
    private Timestamp ngayTao;
    private String nguoiTao;
    private Timestamp ngaySua;
    private String nguoiSua;
    private TaiKhoan taiKhoanByIdTaiKhoan;
    private boolean macDinh;
    private String hoVaTen;

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
    @Column(name = "email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Basic
    @Column(name = "id_tinh")
    public int getIdTinh() {
        return idTinh;
    }

    public void setIdTinh(int idTinh) {
        this.idTinh = idTinh;
    }

    @Basic
    @Column(name = "id_huyen")
    public int getIdHuyen() {
        return idHuyen;
    }

    public void setIdHuyen(int idHuyen) {
        this.idHuyen = idHuyen;
    }

    @Basic
    @Column(name = "id_xa")
    public String getIdXa() {
        return idXa;
    }

    public void setIdXa(String idXa) {
        this.idXa = idXa;
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
    @Column(name = "dia_chi_cu_the")
    public String getDiaChiCuThe() {
        return diaChiCuThe;
    }

    public void setDiaChiCuThe(String diaChiCuThe) {
        this.diaChiCuThe = diaChiCuThe;
    }

    @Basic
    @Column(name = "loai")
    public int getLoai() {
        return loai;
    }

    public void setLoai(int loai) {
        this.loai = loai;
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
    @Column(name = "mac_dinh")
    public boolean isMacDinh() {
        return macDinh;
    }

    public void setMacDinh(boolean macDinh) {
        this.macDinh = macDinh;
    }

    @Basic
    @Column(name = "ho_va_ten")
    public String getHoVaTen() {
        return hoVaTen;
    }

    public void setHoVaTen(String hoVaTen) {
        this.hoVaTen = hoVaTen;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DiaChi diaChi = (DiaChi) o;
        return id == diaChi.id && idTinh == diaChi.idTinh && idHuyen == diaChi.idHuyen && idXa == diaChi.idXa && loai == diaChi.loai && Objects.equals(email, diaChi.email) && Objects.equals(soDienThoai, diaChi.soDienThoai) && Objects.equals(diaChiCuThe, diaChi.diaChiCuThe) && Objects.equals(ngayTao, diaChi.ngayTao) && Objects.equals(nguoiTao, diaChi.nguoiTao) && Objects.equals(ngaySua, diaChi.ngaySua) && Objects.equals(nguoiSua, diaChi.nguoiSua);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, idTinh, idHuyen, idXa, soDienThoai, diaChiCuThe, loai, ngayTao, nguoiTao, ngaySua, nguoiSua);
    }

    @ManyToOne
    @JoinColumn(name = "id_tai_khoan", referencedColumnName = "id", nullable = false)
    public TaiKhoan getTaiKhoanByIdTaiKhoan() {
        return taiKhoanByIdTaiKhoan;
    }

    public void setTaiKhoanByIdTaiKhoan(TaiKhoan taiKhoanByIdTaiKhoan) {
        this.taiKhoanByIdTaiKhoan = taiKhoanByIdTaiKhoan;
    }
}
