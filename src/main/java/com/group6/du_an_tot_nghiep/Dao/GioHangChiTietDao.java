package com.group6.du_an_tot_nghiep.Dao;

import com.group6.du_an_tot_nghiep.Entities.GioHang;
import com.group6.du_an_tot_nghiep.Entities.GioHangChiTiet;
import com.group6.du_an_tot_nghiep.Entities.HinhAnh;
import com.group6.du_an_tot_nghiep.Entities.SanPhamChiTiet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GioHangChiTietDao extends JpaRepository<GioHangChiTiet,Integer> {
    public static int DA_THANH_TOAN = 2;
    public static int CHO_THANH_TOAN = 1;
    public static int DA_XOA = 0;

    @Query("select ghct from GioHangChiTiet ghct where ghct.gioHangByIdGioHang = ?1 and ghct.trangThai = ?2 and ghct.sanPhamChiTietByIdSpct in " +
            "(select spct from SanPhamChiTiet spct where spct.trangThai = 1)")
    List<GioHangChiTiet> fingGhctByGioHangAndTrangThaiSpct (GioHang gioHang, int trangThai);

    @Query("select ghct from GioHangChiTiet ghct where ghct.gioHangByIdGioHang = ?1 and ghct.trangThai = ?2 and ghct.selected = true and ghct.sanPhamChiTietByIdSpct in " +
            "(select spct from SanPhamChiTiet spct where spct.trangThai = 1)")
    List<GioHangChiTiet> findGhctByGioHangAndTrangThaiSpctForThongTinGioHang (GioHang gioHang, int trangThai);

//    List<GioHangChiTiet> findAllByGioHangByIdGioHangAndTrangThai (GioHang gioHang, int trangThai);

    GioHangChiTiet findBySanPhamChiTietByIdSpctAndGioHangByIdGioHangAndTrangThai (SanPhamChiTiet sanPhamChiTiet, GioHang gioHang, int choThanhToan);

    @Query("select ghct from GioHangChiTiet ghct where ghct.sanPhamChiTietByIdSpct.id = ?1 and ghct.gioHangByIdGioHang = ?2 and ghct.trangThai = ?3")
    GioHangChiTiet findByIdSpctAndGioHangAndTrangThai (int idSpct, GioHang gioHang, int choThanhToan);

    @Query("select ghct.soLuong from GioHangChiTiet ghct where ghct.sanPhamChiTietByIdSpct = ?1 and ghct.gioHangByIdGioHang = ?2 and ghct.trangThai = 1")
    Optional<Integer> getSoLuongSpctTrongGio (SanPhamChiTiet sanPhamChiTiet, GioHang gioHang);
}
