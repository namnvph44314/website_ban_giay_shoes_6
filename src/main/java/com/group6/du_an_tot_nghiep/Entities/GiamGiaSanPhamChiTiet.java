package com.group6.du_an_tot_nghiep.Entities;

import com.group6.du_an_tot_nghiep.Entities.SanPhamChiTiet;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "giam_gia_san_pham_chi_tiet")
public class GiamGiaSanPhamChiTiet {
    private int id;
    private GiamGiaSanPham idGiamGia;
    private BigDecimal giaGiam;
    private String nguoiTao;
    private Timestamp ngayTao;
    private String nguoiSua;
    private Timestamp ngaySua;
    private int trangThai;
    private SanPhamChiTiet sanPhamChiTietByIdSpct;

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
    @ManyToOne
    @JoinColumn(name = "id_giam_gia")
    public GiamGiaSanPham getIdGiamGia() {
        return idGiamGia;
    }

    public void setIdGiamGia(GiamGiaSanPham idGiamGia) {
        this.idGiamGia = idGiamGia;
    }

    @Basic
    @Column(name = "gia_giam")
    public BigDecimal getGiaGiam() {
        return giaGiam;
    }

    public void setGiaGiam(BigDecimal giaGiam) {
        this.giaGiam = giaGiam;
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


    @ManyToOne
    @JoinColumn(name = "id_spct", referencedColumnName = "id", nullable = false)
    public SanPhamChiTiet getSanPhamChiTietByIdSpct() {
        return sanPhamChiTietByIdSpct;
    }

    public void setSanPhamChiTietByIdSpct(SanPhamChiTiet sanPhamChiTietByIdSpct) {
        this.sanPhamChiTietByIdSpct = sanPhamChiTietByIdSpct;
    }
}
