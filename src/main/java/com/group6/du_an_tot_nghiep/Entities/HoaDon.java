package com.group6.du_an_tot_nghiep.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "hoa_don")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class HoaDon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_tai_khoan")
    @JsonBackReference
    private TaiKhoan idTaiKhoan;

    @ManyToOne
    @JoinColumn(name = "id_phieu_giam_gia")
    private PhieuGiamGia idPhieuGiamGia;

    @Basic
    @Column(name = "ma_hoa_don")
    private String maHoaDon;

    @Basic
    @Column(name = "email")
    private String email;

    @Basic
    @Column(name = "ho_va_ten")
    private String hoVaTen;

    @Basic
    @Column(name = "ghi_chu")
    private String ghiChu;

    @Basic
    @Column(name = "dia_chi")
    private String diaChi;

    @Basic
    @Column(name = "tien_khach_tra")
    private BigDecimal tienKhachTra;

    @Basic
    @Column(name = "tien_sau_giam")
    private BigDecimal tienSauGiam;

    @Basic
    @Column(name = "tien_giam_gia")
    private BigDecimal tienGiamGia;

    @Basic
    @Column(name = "tien_giao_hang")
    private BigDecimal tienGiaoHang;

    @Basic
    @Column(name = "loai_don_hang")
    private Integer loaiDonHang;

    @Basic
    @Column(name = "so_dien_thoai_nhan")
    private String soDienThoaiNhan;

    @Basic
    @Column(name = "ngay_hoan_thanh")
    private Timestamp ngayHoanThanh;

    @Basic
    @Column(name = "ngay_xac_nhan")
    private Timestamp ngayXacNhan;

    @Basic
    @Column(name = "tong_tien")
    private BigDecimal tongTien;

    @Basic
    @Column(name = "ngay_giao_hang")
    private Timestamp ngayGiaoHang;

    @Basic
    @Column(name = "ngay_nhan_hang_du_kien")
    private Timestamp ngayNhanHangDuKien;

    @Basic
    @Column(name = "phuong_thuc_thanh_toan")
    private int phuongthucthanhtoan;

    @Basic
    @Column(name = "ngay_nhan")
    private Timestamp ngayNhan;

    @Basic
    @Column(name = "trang_thai")
    private int trangThai;

    @Basic
    @Column(name = "ngay_tao")
    @CreationTimestamp
    private Timestamp ngayTao;

    @Basic
    @Column(name = "nguoi_tao")
    private String nguoiTao;

    @Basic
    @Column(name = "ngay_sua")
    private Timestamp ngaySua;

    @Basic
    @Column(name = "nguoi_sua")
    private String nguoiSua;

    @OneToMany(mappedBy = "idHoaDon", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<HoaDonChiTiet> hoaDonChiTiets;

    @OneToMany(mappedBy = "hoaDonByIdHoaDon", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<LichSuHoaDon> trangThaiHoaDonList;

    @Basic
    @Column(name = "id_tinh")
    private Integer idTinh;

    @Basic
    @Column(name = "id_huyen")
    private Integer idHuyen;

    @Basic
    @Column(name = "id_xa")
    private String idXa;

}
