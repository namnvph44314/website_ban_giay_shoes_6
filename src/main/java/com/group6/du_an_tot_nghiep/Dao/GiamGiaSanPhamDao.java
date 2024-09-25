package com.group6.du_an_tot_nghiep.Dao;

import com.group6.du_an_tot_nghiep.Entities.GiamGiaSanPham;

import com.group6.du_an_tot_nghiep.Entities.PhieuGiamGia;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

@Repository
public interface GiamGiaSanPhamDao extends JpaRepository<GiamGiaSanPham,Integer> {
    public static final int DANG_DIEN_RA = 1;
    public static final int NGUNG_HOAT_DONG = 0;
    public static final int CHUA_DIEN_RA = 2;
    public static final int HET_HAN = 3;

    @Query("select ggsp from GiamGiaSanPham ggsp order by ggsp.id DESC")
    public Page<GiamGiaSanPham> getAllPage(Pageable pageable);

    @Query("SELECT ggsp FROM GiamGiaSanPham ggsp WHERE " +
            "(COALESCE(:ngayBatDau, :ngayKetThuc) IS NULL OR " +
            "ggsp.ngayBatDau BETWEEN :ngayBatDau AND :ngayKetThuc OR " +
            "ggsp.ngayKetThuc BETWEEN :ngayBatDau AND :ngayKetThuc) AND " +
            "(:trangThai IS NULL OR ggsp.trangThai = :trangThai) order by ggsp.id DESC")
    public Page<GiamGiaSanPham> loc(@Param("ngayBatDau") Timestamp ngayBatDau, @Param("ngayKetThuc") Timestamp ngayKetThuc, @Param("trangThai") Integer trangThai, Pageable pageable);

    @Query("select ggsp from GiamGiaSanPham ggsp where ggsp.tenGiamGia like :ten")
    public GiamGiaSanPham search(@Param("ten") String ten);

    public GiamGiaSanPham findTopByOrderByIdDesc();

    @Modifying
    @Transactional
    @Query("update GiamGiaSanPham ggsp set ggsp.trangThai=3 where ggsp.ngayKetThuc < :ngayHienTai")
    public void updateHetHan(@Param("ngayHienTai") Timestamp ngayHienTai);

    @Modifying
    @Transactional
    @Query("update GiamGiaSanPham ggsp set ggsp.trangThai=1 where ggsp.ngayBatDau < :ngayHienTai and ggsp.ngayKetThuc > :ngayHienTai and ggsp.trangThai=2")
    public void updateDangHoatDong(@Param("ngayHienTai") Timestamp ngayHienTai);

//    @Modifying
//    @Transactional
//    @Query(value = "UPDATE giam_gia_san_pham_chi_tiet " +
//            "SET trang_thai = 1 " +
//            "WHERE id_giam_gia IN ( " +
//            "  SELECT ggsp.id FROM giam_gia_san_pham ggsp " +
//            "  WHERE ggsp.ngay_bat_dau = ( " +
//            "    SELECT MAX(ggsp2.ngay_bat_dau) FROM giam_gia_san_pham ggsp2) and ggsp.trang_thai=1)",
//            nativeQuery = true)
//    public void updateGiamGiaSanPhamChiTietWithMaxNgayBatDau();

//    @Modifying
//    @Transactional
//    @Query(value = "update GiamGiaSanPhamChiTiet ggspct set ggspct.trangThai=0 where ggspct.id in (select ggspct2.id from GiamGiaSanPhamChiTiet ggspct2 where ggspct2.trangThai=1 and ggspct2.idGiamGia.ngayBatDau > ggspct.idGiamGia.ngayBatDau )")
//    public void updateGiamGiaSanPhamChiTietWithNotMaxNgayBatDau();

    @Query("SELECT gg.giaTri FROM GiamGiaSanPham gg WHERE gg = ?1")
    Integer findGiaTriById (GiamGiaSanPham idGiamGia);

    @Query("SELECT gg.loaiGiamGia FROM GiamGiaSanPham gg WHERE gg = ?1")
    Integer findLoaiById (GiamGiaSanPham idGiamGia);
}
