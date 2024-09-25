package com.group6.du_an_tot_nghiep.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "phieu_giam_gia_khach_hang")
public class PhieuGiamGiaKhachHang {
    private Integer id;
    private TaiKhoan idTaiKhoan;
    private PhieuGiamGia idPhieuGiamGia;
    private String nguoiTao;
    private Timestamp ngayTao;
    private String nguoiSua;
    private Timestamp ngaySua;
    private byte trangThai;

    public String getTtPggKh(int trangThai){
        if(trangThai==0){
            return "Chưa sử dụng";
        }else if(trangThai==1){
            return "Đã sử dụng";
        }else{
            return "Hết hạn";
        }
    }

    @Override
    public String toString() {
        return "PhieuGiamGiaKhachHang{" +
                "id=" + id +
                ", idTaiKhoan=" + idTaiKhoan +
                ", idPhieuGiamGia=" + idPhieuGiamGia +
                ", nguoiTao='" + nguoiTao + '\'' +
                ", ngayTao=" + ngayTao +
                ", nguoiSua='" + nguoiSua + '\'' +
                ", ngaySua=" + ngaySua +
                ", trangThai=" + trangThai +
                '}';
    }

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Basic
    @JoinColumn(name = "id_tai_khoan")
    @ManyToOne
    public TaiKhoan getIdTaiKhoan() {
        return idTaiKhoan;
    }

    public void setIdTaiKhoan(TaiKhoan idTaiKhoan) {
        this.idTaiKhoan = idTaiKhoan;
    }

    @Basic
    @JoinColumn(name = "id_phieu_giam_gia")
    @ManyToOne
    public PhieuGiamGia getIdPhieuGiamGia() {
        return idPhieuGiamGia;
    }

    public void setIdPhieuGiamGia(PhieuGiamGia idPhieuGiamGia) {
        this.idPhieuGiamGia = idPhieuGiamGia;
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
    public byte getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(byte trangThai) {
        this.trangThai = trangThai;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PhieuGiamGiaKhachHang that = (PhieuGiamGiaKhachHang) o;
        return id == that.id && idTaiKhoan == that.idTaiKhoan && idPhieuGiamGia == that.idPhieuGiamGia && Objects.equals(nguoiTao, that.nguoiTao) && Objects.equals(ngayTao, that.ngayTao) && Objects.equals(nguoiSua, that.nguoiSua) && Objects.equals(ngaySua, that.ngaySua) && trangThai == that.trangThai;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, idTaiKhoan, idPhieuGiamGia, nguoiTao, ngayTao, nguoiSua, ngaySua,trangThai);
    }

}
