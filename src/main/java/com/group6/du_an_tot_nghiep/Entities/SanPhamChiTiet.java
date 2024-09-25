package com.group6.du_an_tot_nghiep.Entities;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "san_pham_chi_tiet")
public class SanPhamChiTiet {
    private int id;
    private String ma;
    private String moTa;
    private double canNang;
    private BigDecimal gia;
    private int soLuong;
    private String nguoiTao;
    private Timestamp ngayTao;
    private String nguoiSua;
    private Timestamp ngaySua;
    private int trangThai;
    private SanPham sanPhamByIdSanPham;
    private TheLoai theLoaiByIdDanhMuc;
    private ThuongHieu thuongHieuByIdThuongHieu;
    private DeGiay deGiayByIdDeGiay;
    private ChatLieu chatLieuByIdChatLieu;
    private MauSac mauSacByIdMauSac;
    private KichThuoc kichThuocByIdKichCo;

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
    @Column(name = "ma")
    public String getMa() {
        return ma;
    }

    public void setMa(String ma) {
        this.ma = ma;
    }

    @Basic
    @Column(name = "mo_ta")
    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    @Basic
    @Column(name = "can_nang")
    public double getCanNang() {
        return canNang;
    }

    public void setCanNang(double canNang) {
        this.canNang = canNang;
    }

    @Basic
    @Column(name = "gia")
    public BigDecimal getGia() {
        return gia;
    }

    public void setGia(BigDecimal gia) {
        this.gia = gia;
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
        SanPhamChiTiet that = (SanPhamChiTiet) o;
        return id == that.id && canNang == that.canNang && soLuong == that.soLuong && Objects.equals(ma, that.ma) && Objects.equals(moTa, that.moTa) && Objects.equals(gia, that.gia) && Objects.equals(nguoiTao, that.nguoiTao) && Objects.equals(ngayTao, that.ngayTao) && Objects.equals(nguoiSua, that.nguoiSua) && Objects.equals(ngaySua, that.ngaySua) && Objects.equals(trangThai, that.trangThai);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ma, moTa, canNang, gia, soLuong, nguoiTao, ngayTao, nguoiSua, ngaySua, trangThai);
    }

    @ManyToOne
    @JoinColumn(name = "id_san_pham", referencedColumnName = "id", nullable = false)
    public SanPham getSanPhamByIdSanPham() {
        return sanPhamByIdSanPham;
    }

    public void setSanPhamByIdSanPham(SanPham sanPhamByIdSanPham) {
        this.sanPhamByIdSanPham = sanPhamByIdSanPham;
    }

    @ManyToOne
    @JoinColumn(name = "id_danh_muc", referencedColumnName = "id", nullable = false)
    public TheLoai getTheLoaiByIdDanhMuc() {
        return theLoaiByIdDanhMuc;
    }

    public void setTheLoaiByIdDanhMuc(TheLoai theLoaiByIdDanhMuc) {
        this.theLoaiByIdDanhMuc = theLoaiByIdDanhMuc;
    }

    @ManyToOne
    @JoinColumn(name = "id_thuong_hieu", referencedColumnName = "id", nullable = false)
    public ThuongHieu getThuongHieuByIdThuongHieu() {
        return thuongHieuByIdThuongHieu;
    }

    public void setThuongHieuByIdThuongHieu(ThuongHieu thuongHieuByIdThuongHieu) {
        this.thuongHieuByIdThuongHieu = thuongHieuByIdThuongHieu;
    }

    @ManyToOne
    @JoinColumn(name = "id_de_giay", referencedColumnName = "id", nullable = false)
    public DeGiay getDeGiayByIdDeGiay() {
        return deGiayByIdDeGiay;
    }

    public void setDeGiayByIdDeGiay(DeGiay deGiayByIdDeGiay) {
        this.deGiayByIdDeGiay = deGiayByIdDeGiay;
    }

    @ManyToOne
    @JoinColumn(name = "id_chat_lieu", referencedColumnName = "id", nullable = false)
    public ChatLieu getChatLieuByIdChatLieu() {
        return chatLieuByIdChatLieu;
    }

    public void setChatLieuByIdChatLieu(ChatLieu chatLieuByIdChatLieu) {
        this.chatLieuByIdChatLieu = chatLieuByIdChatLieu;
    }

    @ManyToOne
    @JoinColumn(name = "id_mau_sac", referencedColumnName = "id", nullable = false)
    public MauSac getMauSacByIdMauSac() {
        return mauSacByIdMauSac;
    }

    public void setMauSacByIdMauSac(MauSac mauSacByIdMauSac) {
        this.mauSacByIdMauSac = mauSacByIdMauSac;
    }

    @ManyToOne
    @JoinColumn(name = "id_kich_co", referencedColumnName = "id", nullable = false)
    public KichThuoc getKichThuocByIdKichCo() {
        return kichThuocByIdKichCo;
    }

    public void setKichThuocByIdKichCo(KichThuoc kichThuocByIdKichCo) {
        this.kichThuocByIdKichCo = kichThuocByIdKichCo;
    }
}
