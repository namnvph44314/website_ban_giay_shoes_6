package com.group6.du_an_tot_nghiep.Dao;

import com.group6.du_an_tot_nghiep.Entities.PhieuGiamGia;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Repository
public interface PhieuGiamGiaDao extends JpaRepository<PhieuGiamGia,Integer> {
    public static final int DANG_DIEN_RA=1;
    public static final int NGUNG_HOAT_DONG=0;
    public static final int CHUA_DIEN_RA=2;
    public static final int DA_DUNG=4;

    public static final int PHAN_TRAM=0;
    public static final int TIEN=1;

    @Query("select pgg from PhieuGiamGia pgg  order by pgg.id DESC")
    public Page<PhieuGiamGia> hienThi(Pageable pageable);

    @Query("SELECT pgg FROM PhieuGiamGia pgg WHERE " +
            "(COALESCE(:ngayBatDau, :ngayKetThuc) IS NULL OR " +
            "pgg.ngayBatDau BETWEEN :ngayBatDau AND :ngayKetThuc OR " +
            "pgg.ngayKetThuc BETWEEN :ngayBatDau AND :ngayKetThuc) AND " +
            "(:kieu IS NULL OR pgg.kieu = :kieu) AND " +
            "(:kieuGiaTri IS NULL OR pgg.kieuGiaTri = :kieuGiaTri) AND " +
            "(:trangThai IS NULL OR pgg.trangThai = :trangThai) order by pgg.id DESC")
    public Page<PhieuGiamGia> loc(@Param("ngayBatDau") Timestamp ngayBatDau, @Param("ngayKetThuc") Timestamp ngayKetThuc, @Param("kieu") Integer kieu, @Param("kieuGiaTri") Integer kieuGiaTri, @Param("trangThai") Integer trangThai, Pageable pageable);

    @Query("select pgg from PhieuGiamGia pgg where pgg.maKhuyenMai=:ma")
    public PhieuGiamGia  search( @Param("ma") String ma);

    public PhieuGiamGia findTopByOrderByIdDesc();

    @Modifying
    @Transactional
    @Query("update PhieuGiamGia pgg set pgg.trangThai=0 where pgg.ngayKetThuc < :ngayHienTai ")
    public void updateTTNHD(@Param("ngayHienTai") Timestamp ngayHienTai);

    @Modifying
    @Transactional
    @Query("update PhieuGiamGia pgg set pgg.trangThai=1 where :ngayHienTai between pgg.ngayBatDau and pgg.ngayKetThuc")
    public void updateTTDDR(@Param("ngayHienTai") Timestamp ngayHienTai);

    @Modifying
    @Transactional
    @Query("update PhieuGiamGia pgg set pgg.trangThai=2 where :ngayHienTai < pgg.ngayBatDau")
    public void updateTTCDR(@Param("ngayHienTai") Timestamp ngayHienTai);

    public PhieuGiamGia findByMaKhuyenMai(String maKM);

    public PhieuGiamGia findPhieuGiamGiaByMaKhuyenMai(String maKm);


    @Query("select pgg from PhieuGiamGia pgg where pgg.giaTriNhoNhat <= ?1 " +
            "and pgg.kieu = 1 " +
            "and pgg.ngayKetThuc > ?2 and pgg.ngayBatDau < ?3 " +
            "and pgg.soLuong > 0" +
            "and pgg.trangThai = 1")
    List<PhieuGiamGia> findPggCongKhai (BigDecimal tongTien, Timestamp now, Timestamp now2);

    @Query("select pgg from PhieuGiamGia pgg where pgg.giaTriNhoNhat <= ?1 " +
            "and pgg.id in (select pggCt.idPhieuGiamGia.id from PhieuGiamGiaKhachHang pggCt where pggCt.idTaiKhoan.id = ?2 and pggCt.trangThai = 0)" +
            "and pgg.ngayKetThuc > ?3 and pgg.ngayBatDau < ?4 " +
            "and pgg.soLuong > 0" +
            "and pgg.trangThai = 1")
    List<PhieuGiamGia> findPggCaNhan (BigDecimal tongTien, int idTaiKhoan,Timestamp now,Timestamp now2);

    @Query("select pgg from PhieuGiamGia pgg where " +
            " pgg.id in (select pggCt.idPhieuGiamGia.id from PhieuGiamGiaKhachHang pggCt where pggCt.idTaiKhoan.id = ?1 and pggCt.trangThai = 0)" +
            "and pgg.ngayKetThuc > ?2 and pgg.ngayBatDau < ?3 " +
            "and pgg.soLuong > 0" +
            "and pgg.trangThai = 1")
    List<PhieuGiamGia> findPggCaNhanWithoutTongTien (int idTaiKhoan,Timestamp now,Timestamp now2);

    PhieuGiamGia findByIdAndTrangThai (int id, int trangThai);


    @Query("SELECT pgg FROM PhieuGiamGia pgg WHERE pgg.trangThai = 1")
    List<PhieuGiamGia> getAllPhieuGiamGia();



    @Query("SELECT pgg from PhieuGiamGia pgg where pgg.giaTriNhoNhat <= :tongTien and pgg.kieuGiaTri=0")
    List<PhieuGiamGia> findVoucherMoney(@Param("tongTien") BigDecimal tongTien);

    @Query("select pgg from PhieuGiamGia pgg where pgg.giaTriNhoNhat <= :tongTien and pgg.kieuGiaTri=1")
    List<PhieuGiamGia> findVoucherPercent(@Param("tongTien") BigDecimal tongTien);




}
