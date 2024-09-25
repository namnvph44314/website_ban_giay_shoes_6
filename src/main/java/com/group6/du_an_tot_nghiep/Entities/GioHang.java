package com.group6.du_an_tot_nghiep.Entities;

import com.group6.du_an_tot_nghiep.Entities.PhieuGiamGia;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Table(name = "gio_hang", schema = "public", catalog = "shoes_6")
public class GioHang {
    private int id;
    private String nguoiTao;
    private Timestamp ngayTao;
    private String nguoiSua;
    private Timestamp ngaySua;
    private int trangThai;
    private TaiKhoan taiKhoanByIdTaiKhoan;
    private PhieuGiamGia phieuGiamGiaByIdPgg;
    private Integer phuongThucThanhToan;
    private BigDecimal tienGiaoHang;
    private String ghiChu;

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

    @Basic
    @Column(name = "phuong_thuc_thanh_toan")
    public Integer getPhuongThucThanhToan() {
        return phuongThucThanhToan;
    }

    public void setPhuongThucThanhToan(Integer phuongThucThanhToan) {
        this.phuongThucThanhToan = phuongThucThanhToan;
    }

    @Basic
    @Column(name = "tien_giao_hang")
    public BigDecimal getTienGiaoHang() {
        return tienGiaoHang;
    }

    public void setTienGiaoHang(BigDecimal tienGiaoHang) {
        this.tienGiaoHang = tienGiaoHang;
    }

    @Basic
    @Column(name = "ghi_chu")
    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
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
    @JoinColumn(name = "id_pgg", referencedColumnName = "id")
    public PhieuGiamGia getPhieuGiamGiaByIdPgg() {
        return phieuGiamGiaByIdPgg;
    }

    public void setPhieuGiamGiaByIdPgg(PhieuGiamGia phieuGiamGiaByIdPgg) {
        this.phieuGiamGiaByIdPgg = phieuGiamGiaByIdPgg;
    }
}
