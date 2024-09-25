package com.group6.du_an_tot_nghiep.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "hoa_don_chi_tiet")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class HoaDonChiTiet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "id_spct", referencedColumnName = "id", nullable = false)
    private SanPhamChiTiet sanPhamChiTietByIdSpct;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "id_hoa_don", referencedColumnName = "id", nullable = false)
    private HoaDon idHoaDon;

    @Basic
    @Column(name = "so_luong_san_pham", nullable = false)
    private int soLuongSanPham;

    @Basic
    @Column(name = "gia", nullable = false)
    private BigDecimal gia;

    @Basic
    @Column(name = "trang_thai", nullable = false)
    private int trangThai;

    @Basic
    @Column(name = "nguoi_tao", nullable = false)
    private String nguoiTao;

    @Basic
    @Column(name = "ngay_tao", nullable = false)
    @CreationTimestamp
    private Timestamp ngayTao;

    @Basic
    @Column(name = "nguoi_dang")
    private String nguoiDang;

    @Basic
    @Column(name = "ngay_dang")
    private Timestamp ngayDang;
}
