package com.group6.du_an_tot_nghiep.Dao;

import com.group6.du_an_tot_nghiep.Dto.QuanLy.ThongKe.ThongKeSanPhamBanChay;
import com.group6.du_an_tot_nghiep.Entities.HoaDon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;


@Repository
public interface HoaDonDao extends JpaRepository<HoaDon, Integer>, JpaSpecificationExecutor<HoaDon> {
    Page<HoaDon> findAll(Pageable pageable);
//    @Query("SELECT h FROM HoaDon h WHERE h.hoVaTen LIKE %:hoVaTen%")
//    Page<HoaDon> findByHoVaTenContaining(@Param("hoVaTen") String hoVaTen, Pageable pageable);

    @Query("SELECT h FROM HoaDon h WHERE h.ngayTao BETWEEN :startDate AND :endDate")
    Page<HoaDon> findByNgayTaoBetween(@Param("startDate") Timestamp startDate, @Param("endDate") Timestamp endDate, Pageable pageable);


    @Query("SELECT hd FROM HoaDon hd WHERE hd.trangThai IN :trangThais")
    Page<HoaDon> findByTrangThais(@Param("trangThais") List<Integer> trangThais, Pageable pageable);

    Page<HoaDon> findByLoaiDonHang(int loaiDonHang, Pageable pageable);

    List<HoaDon> findByTrangThai(int trangThai);

    Optional<HoaDon> findByMaHoaDon(String maHoaDon);

    @Query("SELECT hd from HoaDon hd where hd.trangThai = 10")
    List<HoaDon> getHoaDonCho();

    @Transactional
    @Modifying
    @Query("update HoaDon hd set hd.tienKhachTra = :tienKhachTra where hd.id = :billId")
    void addMoneyCustomerPay(@Param("billId") Integer billId, @Param("tienKhachTra") BigDecimal tienKhachTra);

    @Query("select sum(lstt.soTien) from LichSuTraTien lstt where lstt.idHoaDon = :billId")
    BigDecimal getTienKhachTra(@Param("billId") Integer billId);

    @Query("select hd.tienKhachTra from HoaDon hd where hd.id = :billId")
    BigDecimal getTienKhachTraOld(@Param("billId") Integer billId);

    @Transactional
    @Modifying
    @Query("update HoaDon hd set hd.tongTien = :tongTien where hd.id = :billId")
    void updateTongTien(@Param("billId") Integer billId, @Param("tongTien") BigDecimal tongTien);

    @Transactional
    @Modifying
    @Query("update HoaDon hd set hd.idPhieuGiamGia.id = :idPGG where hd.id = :billId")
    void updatePhieuGiamGia(@Param("billId") Integer billId, @Param("idPGG") Integer idPGG);

    @Transactional
    @Modifying
    @Query("update HoaDon hd set hd.tienSauGiam =:tienSauGiam, hd.tienGiamGia =:tienGiamGia, hd.tienGiaoHang =:tienGiaoHang, hd.soDienThoaiNhan =:soDienThoai, hd.trangThai = :trangThai, hd.phuongthucthanhtoan = :phuongThucThanhToan, hd.diaChi = :diaChi, hd.tongTien = :tongTien, hd.hoVaTen = :tenKhachHang, hd.idTinh = :idTinh, hd.idHuyen = :idHuyen, hd.idXa = :idXa where hd.id = :billId")
    Integer finalCheckout(@Param("billId") Integer billId, @Param("tienSauGiam") BigDecimal tienSauGiam, @Param("tienGiamGia") BigDecimal tienGiamGia, @Param("tienGiaoHang") BigDecimal tienGiaoHang, @Param("soDienThoai") String soDienThoai, @Param("trangThai") Integer trangThai, @Param("phuongThucThanhToan") Integer phuongThucThanhToan, @Param("diaChi") String diaChi, @Param("tongTien") BigDecimal tongTien, @Param("tenKhachHang") String tenKhachHang, @Param("idTinh") Integer idTinh, @Param("idXa") Integer idXa, @Param("idHuyen") Integer idHuyen);

    @Transactional
    @Modifying
    @Query("update HoaDon hd set hd.tienSauGiam =:tienSauGiam, hd.tienGiamGia =:tienGiamGia, hd.tienGiaoHang =:tienGiaoHang, hd.soDienThoaiNhan =:soDienThoai, hd.trangThai = :trangThai, hd.phuongthucthanhtoan = :phuongThucThanhToan, hd.diaChi = :diaChi, hd.tongTien = :tongTien, hd.idTinh = :idTinh, hd.idHuyen = :idHuyen, hd.idXa = :idXa where hd.id = :billId")
    Integer finalCheckoutNotCusName(@Param("billId") Integer billId, @Param("tienSauGiam") BigDecimal tienSauGiam, @Param("tienGiamGia") BigDecimal tienGiamGia, @Param("tienGiaoHang") BigDecimal tienGiaoHang, @Param("soDienThoai") String soDienThoai, @Param("trangThai") Integer trangThai, @Param("phuongThucThanhToan") Integer phuongThucThanhToan, @Param("diaChi") String diaChi, @Param("tongTien") BigDecimal tongTien, @Param("idTinh") Integer idTinh, @Param("idXa") Integer idXa, @Param("idHuyen") Integer idHuyen);


    @Query("SELECT hd.maHoaDon FROM HoaDon hd order by hd.ngayTao desc limit 1")
    String getLastestMaSo ();

    @Query(value = "SELECT SUM(hd.tien_sau_giam) FROM hoa_don hd WHERE DATE(hd.ngay_hoan_thanh) = CURRENT_DATE AND hd.trang_thai = 5", nativeQuery = true)
    public BigDecimal getTongTienByNgay();
    @Query(value = "SELECT SUM(hd.tien_sau_giam) FROM hoa_don hd WHERE DATE(hd.ngay_hoan_thanh) = CURRENT_DATE - INTERVAL '1 day' AND hd.trang_thai = 5", nativeQuery = true)
    public BigDecimal getTongTienByNgayHomQua();

    @Query("SELECT SUM(hd.tienSauGiam) FROM HoaDon hd WHERE EXTRACT(WEEK FROM hd.ngayHoanThanh) = EXTRACT(WEEK FROM CURRENT_DATE) AND EXTRACT(YEAR FROM hd.ngayHoanThanh) = EXTRACT(YEAR FROM CURRENT_DATE) and hd.trangThai=5")
    public BigDecimal getTongTienByTuan();

    @Query(value = "SELECT SUM(hd.tien_sau_giam) FROM hoa_don hd WHERE EXTRACT(WEEK FROM hd.ngay_hoan_thanh) = EXTRACT(WEEK FROM CURRENT_DATE - INTERVAL '1 week') AND EXTRACT(YEAR FROM hd.ngay_hoan_thanh) = EXTRACT(YEAR FROM CURRENT_DATE - INTERVAL '1 week') AND hd.trang_thai = 5", nativeQuery = true)
    BigDecimal getTongTienByTuanTruoc();

    @Query(value = "SELECT SUM(hd.tien_sau_giam) FROM hoa_don hd WHERE hd.ngay_hoan_thanh >= DATE_TRUNC('month', CURRENT_DATE) AND hd.ngay_hoan_thanh < DATE_TRUNC('month', CURRENT_DATE) + INTERVAL '1 month' and hd.trang_thai=5", nativeQuery = true)
    public BigDecimal getTongTienByThang();

    @Query(value = "SELECT SUM(hd.tien_sau_giam) FROM hoa_don hd WHERE hd.ngay_hoan_thanh >= DATE_TRUNC('month', CURRENT_DATE - INTERVAL '1 month') AND hd.ngay_hoan_thanh < DATE_TRUNC('month', CURRENT_DATE) AND hd.trang_thai = 5", nativeQuery = true)
    public BigDecimal getTongTienByThangTruoc();

    @Query(value = "SELECT SUM(hd.tien_sau_giam) FROM hoa_don hd WHERE hd.ngay_hoan_thanh >= DATE_TRUNC('year', CURRENT_DATE) AND hd.ngay_hoan_thanh < DATE_TRUNC('year', CURRENT_DATE) + INTERVAL '1 year' and hd.trang_thai=5", nativeQuery = true)
    public BigDecimal getTongTienByNam();

    @Query(value = "SELECT SUM(hd.tien_sau_giam) FROM hoa_don hd WHERE hd.ngay_hoan_thanh >= DATE_TRUNC('year', CURRENT_DATE - INTERVAL '1 year') AND hd.ngay_hoan_thanh < DATE_TRUNC('year', CURRENT_DATE) AND hd.trang_thai = 5", nativeQuery = true)
    public BigDecimal getTongTienByNamTruoc();

    @Query(value = "SELECT SUM(hd.tien_sau_giam) FROM hoa_don hd WHERE hd.trang_thai=5", nativeQuery = true)
    public BigDecimal getTongTien();

    @Query(value = "SELECT SUM(hd.tien_sau_giam) FROM hoa_don hd WHERE hd.trang_thai=5 and hd.ngay_hoan_thanh between :tuNgay and :denNgay", nativeQuery = true)
    public BigDecimal getTongTienTuyChinh(@Param("tuNgay") Timestamp tuNgay, @Param("denNgay") Timestamp denNgay);

    @Query(value = "select count(hd.id) from hoa_don hd where DATE(hd.ngay_hoan_thanh) = CURRENT_DATE and hd.trang_thai=5",nativeQuery = true)
    public BigDecimal getDonTCByNgay();
    @Query("SELECT COUNT(hd.id) FROM HoaDon hd WHERE hd.ngayHoanThanh = FUNCTION('DATEADD', day, -1, CURRENT_DATE) AND hd.trangThai = 5")
    public BigDecimal getDonTCByNgayHomQua();

    @Query("SELECT count(hd.id) FROM HoaDon hd WHERE EXTRACT(WEEK FROM hd.ngayHoanThanh) = EXTRACT(WEEK FROM CURRENT_DATE) AND EXTRACT(YEAR FROM hd.ngayHoanThanh) = EXTRACT(YEAR FROM CURRENT_DATE) and hd.trangThai=5")
    public BigDecimal getDonTCByTuan();
    @Query(value = "SELECT count(hd.id) FROM hoa_don hd WHERE EXTRACT(WEEK FROM hd.ngay_hoan_thanh) = EXTRACT(WEEK FROM CURRENT_DATE - INTERVAL '1 week') AND EXTRACT(YEAR FROM hd.ngay_hoan_thanh) = EXTRACT(YEAR FROM CURRENT_DATE - INTERVAL '1 week') and hd.trang_thai=5",nativeQuery = true)
    public BigDecimal getDonTCByTuanTruoc();

    @Query(value = "SELECT count(hd.id) FROM hoa_don hd WHERE hd.ngay_hoan_thanh >= DATE_TRUNC('month', CURRENT_DATE) AND hd.ngay_hoan_thanh < DATE_TRUNC('month', CURRENT_DATE) + INTERVAL '1 month' and hd.trang_thai=5", nativeQuery = true)
    public BigDecimal getDonTCByThang();
    @Query(value = "SELECT count(hd.id) FROM hoa_don hd WHERE hd.ngay_hoan_thanh >= DATE_TRUNC('month', CURRENT_DATE - INTERVAL '1 month') AND hd.ngay_hoan_thanh < DATE_TRUNC('month', CURRENT_DATE) and hd.trang_thai=5", nativeQuery = true)
    public BigDecimal getDonTCByThangTruoc();

    @Query(value = "SELECT count(hd.id) FROM hoa_don hd WHERE hd.ngay_hoan_thanh >= DATE_TRUNC('year', CURRENT_DATE) AND hd.ngay_hoan_thanh < DATE_TRUNC('year', CURRENT_DATE) + INTERVAL '1 year' and hd.trang_thai=5", nativeQuery = true)
    public BigDecimal getDonTCByNam();
    @Query(value = "SELECT count(hd.id) FROM hoa_don hd WHERE hd.ngay_hoan_thanh >= DATE_TRUNC('year', CURRENT_DATE - INTERVAL '1 year') AND hd.ngay_hoan_thanh < DATE_TRUNC('year', CURRENT_DATE) and hd.trang_thai=5", nativeQuery = true)
    public BigDecimal getDonTCByNamTruoc();

    @Query(value = "SELECT count(hd.id) FROM hoa_don hd WHERE hd.trang_thai=5", nativeQuery = true)
    public BigDecimal getDonTC();

    @Query(value = "SELECT count(hd.id) FROM hoa_don hd WHERE hd.trang_thai=5 and hd.ngay_hoan_thanh between :tuNgay and :denNgay", nativeQuery = true)
    public BigDecimal getDonTCTuyChinh(@Param("tuNgay") Timestamp tuNgay, @Param("denNgay") Timestamp denNgay);

    @Query(value = "WITH LatestDates AS ( " +
            "    SELECT id_hoa_don, MAX(ngay_tao) AS max_ngay_tao " +
            "    FROM lich_su_hoa_don " +
            "    GROUP BY id_hoa_don " +
            "), " +
            "LatestStatus AS ( " +
            "    SELECT l.id_hoa_don, lshd.trang_thai, lshd.ngay_tao " +
            "    FROM LatestDates l " +
            "    JOIN lich_su_hoa_don lshd " +
            "    ON l.id_hoa_don = lshd.id_hoa_don " +
            "    AND l.max_ngay_tao = lshd.ngay_tao " +
            ") " +
            "SELECT COUNT(*) " +
            "FROM LatestStatus " +
            "WHERE trang_thai = 6 " +
            "AND DATE(ngay_tao) = :ngay", nativeQuery = true)
    public long getDonHuyByNgay(@Param("ngay")Date ngay);

    @Query(value = "WITH LatestDates AS (" +
            "    SELECT id_hoa_don, MAX(ngay_tao) AS max_ngay_tao " +
            "    FROM lich_su_hoa_don " +
            "    GROUP BY id_hoa_don " +
            "), " +
            "LatestStatus AS (" +
            "    SELECT l.id_hoa_don, lshd.trang_thai, lshd.ngay_tao " +
            "    FROM LatestDates l " +
            "    JOIN lich_su_hoa_don lshd " +
            "    ON l.id_hoa_don = lshd.id_hoa_don " +
            "    AND l.max_ngay_tao = lshd.ngay_tao " +
            ") " +
            "SELECT COUNT(*) " +
            "FROM LatestStatus " +
            "WHERE trang_thai = 6 " +
            "AND DATE_TRUNC('week', ngay_tao) = DATE_TRUNC('week', CAST(:ngay AS DATE))",
            nativeQuery = true)
    public BigDecimal getDonHuyByTuan(@Param("ngay") Date ngay);

    @Query(value = "WITH LatestDates AS (" +
            "    SELECT id_hoa_don, MAX(ngay_tao) AS max_ngay_tao " +
            "    FROM lich_su_hoa_don " +
            "    GROUP BY id_hoa_don " +
            "), " +
            "LatestStatus AS (" +
            "    SELECT l.id_hoa_don, lshd.trang_thai, lshd.ngay_tao " +
            "    FROM LatestDates l " +
            "    JOIN lich_su_hoa_don lshd " +
            "    ON l.id_hoa_don = lshd.id_hoa_don " +
            "    AND l.max_ngay_tao = lshd.ngay_tao " +
            ") " +
            "SELECT COUNT(*) " +
            "FROM LatestStatus " +
            "WHERE trang_thai = 1 " +
            "AND DATE_TRUNC('week', ngay_tao) = DATE_TRUNC('week', CAST(:ngay AS DATE))",
            nativeQuery = true)
    public BigDecimal getDonChoXacNhanByTuan(@Param("ngay") Date ngay);

    @Query(value = "WITH LatestDates AS (" +
            "    SELECT id_hoa_don, MAX(ngay_tao) AS max_ngay_tao " +
            "    FROM lich_su_hoa_don " +
            "    GROUP BY id_hoa_don " +
            "), " +
            "LatestStatus AS (" +
            "    SELECT l.id_hoa_don, lshd.trang_thai, lshd.ngay_tao " +
            "    FROM LatestDates l " +
            "    JOIN lich_su_hoa_don lshd " +
            "    ON l.id_hoa_don = lshd.id_hoa_don " +
            "    AND l.max_ngay_tao = lshd.ngay_tao " +
            ") " +
            "SELECT COUNT(*) " +
            "FROM LatestStatus " +
            "WHERE trang_thai = 3 " +
            "AND DATE_TRUNC('week', ngay_tao) = DATE_TRUNC('week', CAST(:ngay AS DATE))",
            nativeQuery = true)
    public BigDecimal getDonChoGiaoHangByTuan(@Param("ngay") Date ngay);

    @Query(value = "WITH LatestDates AS (" +
            "    SELECT id_hoa_don, MAX(ngay_tao) AS max_ngay_tao " +
            "    FROM lich_su_hoa_don " +
            "    GROUP BY id_hoa_don " +
            "), " +
            "LatestStatus AS (" +
            "    SELECT l.id_hoa_don, lshd.trang_thai, lshd.ngay_tao " +
            "    FROM LatestDates l " +
            "    JOIN lich_su_hoa_don lshd " +
            "    ON l.id_hoa_don = lshd.id_hoa_don " +
            "    AND l.max_ngay_tao = lshd.ngay_tao " +
            ") " +
            "SELECT COUNT(*) " +
            "FROM LatestStatus " +
            "WHERE trang_thai = 6 " +
            "AND DATE_TRUNC('month', ngay_tao) = DATE_TRUNC('month', CAST(:ngay AS DATE))",
            nativeQuery = true)
    public BigDecimal getDonHuyByThang(@Param("ngay") Date ngay);

    @Query(value = "WITH LatestDates AS (" +
            "    SELECT id_hoa_don, MAX(ngay_tao) AS max_ngay_tao " +
            "    FROM lich_su_hoa_don " +
            "    GROUP BY id_hoa_don " +
            "), " +
            "LatestStatus AS (" +
            "    SELECT l.id_hoa_don, lshd.trang_thai, lshd.ngay_tao " +
            "    FROM LatestDates l " +
            "    JOIN lich_su_hoa_don lshd " +
            "    ON l.id_hoa_don = lshd.id_hoa_don " +
            "    AND l.max_ngay_tao = lshd.ngay_tao " +
            ") " +
            "SELECT COUNT(*) " +
            "FROM LatestStatus " +
            "WHERE trang_thai = 1 " +
            "AND DATE_TRUNC('month', ngay_tao) = DATE_TRUNC('month', CAST(:ngay AS DATE))",
            nativeQuery = true)
    public BigDecimal getDonChoXacNhanByThang(@Param("ngay") Date ngay);

    @Query(value = "WITH LatestDates AS (" +
            "    SELECT id_hoa_don, MAX(ngay_tao) AS max_ngay_tao " +
            "    FROM lich_su_hoa_don " +
            "    GROUP BY id_hoa_don " +
            "), " +
            "LatestStatus AS (" +
            "    SELECT l.id_hoa_don, lshd.trang_thai, lshd.ngay_tao " +
            "    FROM LatestDates l " +
            "    JOIN lich_su_hoa_don lshd " +
            "    ON l.id_hoa_don = lshd.id_hoa_don " +
            "    AND l.max_ngay_tao = lshd.ngay_tao " +
            ") " +
            "SELECT COUNT(*) " +
            "FROM LatestStatus " +
            "WHERE trang_thai = 3 " +
            "AND DATE_TRUNC('month', ngay_tao) = DATE_TRUNC('month', CAST(:ngay AS DATE))",
            nativeQuery = true)
    public BigDecimal getDonChoGiaoHangByThang(@Param("ngay") Date ngay);

    @Query(value = "WITH LatestDates AS (" +
            "    SELECT id_hoa_don, MAX(ngay_tao) AS max_ngay_tao " +
            "    FROM lich_su_hoa_don " +
            "    GROUP BY id_hoa_don " +
            "), " +
            "LatestStatus AS (" +
            "    SELECT l.id_hoa_don, lshd.trang_thai, lshd.ngay_tao " +
            "    FROM LatestDates l " +
            "    JOIN lich_su_hoa_don lshd " +
            "    ON l.id_hoa_don = lshd.id_hoa_don " +
            "    AND l.max_ngay_tao = lshd.ngay_tao " +
            ") " +
            "SELECT COUNT(*) " +
            "FROM LatestStatus " +
            "WHERE trang_thai = 6 " +
            "AND DATE_TRUNC('year', ngay_tao) = DATE_TRUNC('year', CAST(:ngay AS DATE))",
            nativeQuery = true)
    public BigDecimal getDonHuyByNam(@Param("ngay") Date ngay);

    @Query(value = "WITH LatestDates AS (" +
            "    SELECT id_hoa_don, MAX(ngay_tao) AS max_ngay_tao " +
            "    FROM lich_su_hoa_don " +
            "    GROUP BY id_hoa_don " +
            "), " +
            "LatestStatus AS (" +
            "    SELECT l.id_hoa_don, lshd.trang_thai, lshd.ngay_tao " +
            "    FROM LatestDates l " +
            "    JOIN lich_su_hoa_don lshd " +
            "    ON l.id_hoa_don = lshd.id_hoa_don " +
            "    AND l.max_ngay_tao = lshd.ngay_tao " +
            ") " +
            "SELECT COUNT(*) " +
            "FROM LatestStatus " +
            "WHERE trang_thai = 1 " +
            "AND DATE_TRUNC('year', ngay_tao) = DATE_TRUNC('year', CAST(:ngay AS DATE))",
            nativeQuery = true)
    public BigDecimal getDonChoXacNhanByNam(@Param("ngay") Date ngay);

    @Query(value = "WITH LatestDates AS (" +
            "    SELECT id_hoa_don, MAX(ngay_tao) AS max_ngay_tao " +
            "    FROM lich_su_hoa_don " +
            "    GROUP BY id_hoa_don " +
            "), " +
            "LatestStatus AS (" +
            "    SELECT l.id_hoa_don, lshd.trang_thai, lshd.ngay_tao " +
            "    FROM LatestDates l " +
            "    JOIN lich_su_hoa_don lshd " +
            "    ON l.id_hoa_don = lshd.id_hoa_don " +
            "    AND l.max_ngay_tao = lshd.ngay_tao " +
            ") " +
            "SELECT COUNT(*) " +
            "FROM LatestStatus " +
            "WHERE trang_thai = 3 " +
            "AND DATE_TRUNC('year', ngay_tao) = DATE_TRUNC('year', CAST(:ngay AS DATE))",
            nativeQuery = true)
    public BigDecimal getDonChoGiaoHangByNam(@Param("ngay") Date ngay);

    @Query(value = "WITH LatestDates AS (" +
            "    SELECT id_hoa_don, MAX(ngay_tao) AS max_ngay_tao " +
            "    FROM lich_su_hoa_don " +
            "    GROUP BY id_hoa_don " +
            "), " +
            "LatestStatus AS (" +
            "    SELECT l.id_hoa_don, lshd.trang_thai " +
            "    FROM LatestDates l " +
            "    JOIN lich_su_hoa_don lshd " +
            "    ON l.id_hoa_don = lshd.id_hoa_don " +
            "    AND l.max_ngay_tao = lshd.ngay_tao " +
            ") " +
            "SELECT COUNT(*) " +
            "FROM LatestStatus " +
            "WHERE trang_thai = 6",
            nativeQuery = true)
    public BigDecimal getDonHuy();

    @Query(value = "WITH LatestDates AS (" +
            "    SELECT id_hoa_don, MAX(ngay_tao) AS max_ngay_tao " +
            "    FROM lich_su_hoa_don " +
            "    GROUP BY id_hoa_don " +
            "), " +
            "LatestStatus AS (" +
            "    SELECT l.id_hoa_don, lshd.trang_thai, lshd.ngay_tao " +
            "    FROM LatestDates l " +
            "    JOIN lich_su_hoa_don lshd " +
            "    ON l.id_hoa_don = lshd.id_hoa_don " +
            "    AND l.max_ngay_tao = lshd.ngay_tao " +
            ") " +
            "SELECT COUNT(*) " +
            "FROM LatestStatus " +
            "WHERE trang_thai = 6 " +
            "AND Date(ngay_tao) BETWEEN Date(:tuNgay) AND Date(:denNgay)",
            nativeQuery = true)
    public BigDecimal getDonHuyTuyChinh(@Param("tuNgay") Timestamp tuNgay, @Param("denNgay") Timestamp denNgay);

    @Query("select count(hd.id) from HoaDon hd where hd.ngayHoanThanh = CURRENT_DATE and hd.trangThai=6")
    public BigDecimal getDonTraByNgay();

    @Query("SELECT count(hd.id) FROM HoaDon hd WHERE EXTRACT(WEEK FROM hd.ngayHoanThanh) = EXTRACT(WEEK FROM CURRENT_DATE) AND EXTRACT(YEAR FROM hd.ngayHoanThanh) = EXTRACT(YEAR FROM CURRENT_DATE) and hd.trangThai=6")
    public BigDecimal getDonTraByTuan();

    @Query(value = "SELECT count(hd.id) FROM hoa_don hd WHERE hd.ngay_hoan_thanh >= DATE_TRUNC('month', CURRENT_DATE) AND hd.ngay_hoan_thanh < DATE_TRUNC('month', CURRENT_DATE) + INTERVAL '1 month' and hd.trang_thai=6", nativeQuery = true)
    public BigDecimal getDonTraByThang();

    @Query(value = "SELECT count(hd.id) FROM hoa_don hd WHERE hd.ngay_hoan_thanh >= DATE_TRUNC('year', CURRENT_DATE) AND hd.ngay_hoan_thanh < DATE_TRUNC('year', CURRENT_DATE) + INTERVAL '1 year' and hd.trang_thai=6", nativeQuery = true)
    public BigDecimal getDonTraByNam();

    @Query(value = "SELECT count(hd.id) FROM hoa_don hd WHERE hd.trang_thai=6", nativeQuery = true)
    public BigDecimal getDonTra();

    @Query(value = "SELECT count(hd.id) FROM hoa_don hd WHERE hd.trang_thai=6 and hd.ngay_hoan_thanh between :tuNgay and :denNgay", nativeQuery = true)
    public BigDecimal getDonTraTuyChinh(@Param("tuNgay") Timestamp tuNgay, @Param("denNgay") Timestamp denNgay);

    @Query(value = "select sum(hdct.so_luong_san_pham) from hoa_don_chi_tiet hdct inner join hoa_don as hd on hd.id=hdct.id_hoa_don where DATE(hd.ngay_hoan_thanh) = CURRENT_DATE and hd.trang_thai=5",nativeQuery = true)
    public BigDecimal getSpByNgay();
    @Query("select sum(hdct.soLuongSanPham) from HoaDonChiTiet hdct where hdct.idHoaDon.ngayHoanThanh = FUNCTION('DATEADD', day, -1, CURRENT_DATE) and hdct.idHoaDon.trangThai=5 ")
    public BigDecimal getSpByNgayHomQua();

    @Query("SELECT sum(hdct.soLuongSanPham) FROM HoaDonChiTiet hdct WHERE EXTRACT(WEEK FROM hdct.idHoaDon.ngayHoanThanh) = EXTRACT(WEEK FROM CURRENT_DATE) AND EXTRACT(YEAR FROM hdct.idHoaDon.ngayHoanThanh) = EXTRACT(YEAR FROM CURRENT_DATE) and hdct.idHoaDon.trangThai=5 ")
    public BigDecimal getSpByTuan();
    @Query(value = "SELECT sum(hdct.so_luong_san_pham) FROM hoa_don_chi_tiet as hdct inner join hoa_don as hd on hd.id=hdct.id_hoa_don WHERE EXTRACT(WEEK FROM hd.ngay_hoan_thanh) = EXTRACT(WEEK FROM CURRENT_DATE - INTERVAL '1 week') AND EXTRACT(YEAR FROM hd.ngay_hoan_thanh) = EXTRACT(YEAR FROM CURRENT_DATE - INTERVAL '1 week') and hd.trang_thai=5",nativeQuery = true)
    public BigDecimal getSpByTuanTruoc();

    @Query(value = "SELECT sum(hdct.so_luong_san_pham) FROM hoa_don_chi_tiet hdct inner join hoa_don hd on hd.id=hdct.id_hoa_don WHERE hd.ngay_hoan_thanh >= DATE_TRUNC('month', CURRENT_DATE) AND hd.ngay_hoan_thanh < DATE_TRUNC('month', CURRENT_DATE) + INTERVAL '1 month' and hd.trang_thai=5 ", nativeQuery = true)
    public BigDecimal getSpByThang();
    @Query(value = "SELECT sum(hdct.so_luong_san_pham) FROM hoa_don_chi_tiet hdct inner join hoa_don hd on hd.id=hdct.id_hoa_don WHERE hd.ngay_hoan_thanh >= DATE_TRUNC('month', CURRENT_DATE - INTERVAL '1 month') AND hd.ngay_hoan_thanh < DATE_TRUNC('month', CURRENT_DATE) and hd.trang_thai=5 ", nativeQuery = true)
    public BigDecimal getSpByThangTruoc();

    @Query(value = "SELECT sum(hdct.so_luong_san_pham) FROM hoa_don_chi_tiet hdct inner join hoa_don hd on hd.id=hdct.id_hoa_don WHERE hd.ngay_hoan_thanh >= DATE_TRUNC('year', CURRENT_DATE) AND hd.ngay_hoan_thanh < DATE_TRUNC('year', CURRENT_DATE) + INTERVAL '1 year' and hd.trang_thai=5 ", nativeQuery = true)
    public BigDecimal getSpByNam();
    @Query(value = "SELECT sum(hdct.so_luong_san_pham) FROM hoa_don_chi_tiet hdct inner join hoa_don hd on hd.id=hdct.id_hoa_don WHERE hd.ngay_hoan_thanh >= DATE_TRUNC('year', CURRENT_DATE - INTERVAL '1 year') AND hd.ngay_hoan_thanh < DATE_TRUNC('year', CURRENT_DATE) and hd.trang_thai=5 ", nativeQuery = true)
    public BigDecimal getSpByNamTruoc();

    @Query(value = "SELECT sum(hdct.so_luong_san_pham) FROM hoa_don_chi_tiet hdct inner join hoa_don hd on hd.id=hdct.id_hoa_don WHERE hd.trang_thai=5", nativeQuery = true)
    public BigDecimal getSp();

    @Query(value = "SELECT sum(hdct.so_luong_san_pham) FROM hoa_don_chi_tiet hdct inner join hoa_don hd on hd.id=hdct.id_hoa_don WHERE  hd.trang_thai=5 and hd.ngay_hoan_thanh between :tuNgay and :denNgay", nativeQuery = true)
    public BigDecimal getSpTuyChinh(@Param("tuNgay") Timestamp tuNgay, @Param("denNgay") Timestamp denNgay);

    @Query("select count(hd.id) from HoaDon hd where hd.trangThai=:trangThai")
    public Integer getSoLuongHoaDon(@Param("trangThai") Integer trangThai);
    @Query("select count(hd.id) from HoaDon hd where hd.trangThai=:trangThai")
    public Integer getSoLuongHoaDonGiaoHang(@Param("trangThai") Integer trangThai);
    @Query("select count(hd.id) from HoaDon hd where hd.trangThai=:trangThai")
    public Integer getSoLuongHoaDonXacNhan(@Param("trangThai") Integer trangThai);
    @Query(value = "select count(hd.id) from hoa_don hd where DATE(hd.ngay_hoan_thanh) = CURRENT_DATE and  hd.trang_thai=:trangThai",nativeQuery = true)
    public Integer getSoLuongHoaDonNgay(@Param("trangThai") Integer trangThai);
    @Query(value = "WITH LatestDates AS (" +
            "    SELECT id_hoa_don, MAX(ngay_tao) AS max_ngay_tao " +
            "    FROM lich_su_hoa_don " +
            "    GROUP BY id_hoa_don " +
            "), " +
            "LatestStatus AS (" +
            "    SELECT l.id_hoa_don, lshd.trang_thai " +
            "    FROM LatestDates l " +
            "    JOIN lich_su_hoa_don lshd " +
            "    ON l.id_hoa_don = lshd.id_hoa_don " +
            "    AND l.max_ngay_tao = lshd.ngay_tao " +
            ") " +
            "SELECT COUNT(*) " +
            "FROM LatestStatus " +
            "WHERE trang_thai = :trangThai",
            nativeQuery = true)
    public Integer tongHdHuyChoTtChoXn(@Param("trangThai") int trangThai);
    @Query(value = "WITH LatestDates AS ( " +
            "    SELECT id_hoa_don, MAX(ngay_tao) AS max_ngay_tao " +
            "    FROM lich_su_hoa_don " +
            "    GROUP BY id_hoa_don " +
            "), " +
            "LatestStatus AS ( " +
            "    SELECT l.id_hoa_don, lshd.trang_thai, lshd.ngay_tao " +
            "    FROM LatestDates l " +
            "    JOIN lich_su_hoa_don lshd " +
            "    ON l.id_hoa_don = lshd.id_hoa_don " +
            "    AND l.max_ngay_tao = lshd.ngay_tao " +
            ") " +
            "SELECT COUNT(*) " +
            "FROM LatestStatus " +
            "WHERE trang_thai = :trangThai " +
            "AND DATE(ngay_tao) = :ngay", nativeQuery = true)
    public Integer getSoLuongHoaDonHuyChoChoGiaoNgay(@Param("trangThai") Integer trangThai, @Param("ngay") Date ngay);
    @Query(value = "select count(hd.id) from hoa_don hd where DATE(hd.ngay_xac_nhan) = CURRENT_DATE and  hd.trang_thai=:trangThai",nativeQuery = true)
    public Integer getSoLuongHoaDonXacNhanNgay(@Param("trangThai") Integer trangThai);
    @Query(value = "select count(hd.id) from hoa_don hd where DATE(hd.ngay_giao_hang) = CURRENT_DATE and  hd.trang_thai=:trangThai",nativeQuery = true)
    public Integer getSoLuongHoaDonGiaoHangNgay(@Param("trangThai") Integer trangThai);
    @Query(value = "select count(hd.id) from hoa_don hd where DATE(hd.ngay_hoan_thanh) = CURRENT_DATE and  hd.trang_thai=:trangThai",nativeQuery = true)
    public Integer getSoLuongHoaDonHoanThanhNgay(@Param("trangThai") Integer trangThai);
    @Query("select count(hd.id) from HoaDon hd where EXTRACT(WEEK FROM hd.ngayHoanThanh) = EXTRACT(WEEK FROM CURRENT_DATE) AND EXTRACT(YEAR FROM hd.ngayHoanThanh) = EXTRACT(YEAR FROM CURRENT_DATE) and  hd.trangThai=:trangThai")
    public Integer getSoLuongHoaDonTuan(@Param("trangThai") Integer trangThai);

    @Query("select count(hd.id) from HoaDon hd where EXTRACT(WEEK FROM hd.ngayXacNhan) = EXTRACT(WEEK FROM CURRENT_DATE) AND EXTRACT(YEAR FROM hd.ngayXacNhan) = EXTRACT(YEAR FROM CURRENT_DATE) and  hd.trangThai=:trangThai")
    public Integer getSoLuongHoaDonXacNhanTuan(@Param("trangThai") Integer trangThai);
    @Query("select count(hd.id) from HoaDon hd where EXTRACT(WEEK FROM hd.ngayGiaoHang) = EXTRACT(WEEK FROM CURRENT_DATE) AND EXTRACT(YEAR FROM hd.ngayGiaoHang) = EXTRACT(YEAR FROM CURRENT_DATE) and  hd.trangThai=:trangThai")
    public Integer getSoLuongHoaDonGiaoHangTuan(@Param("trangThai") Integer trangThai);
    @Query("select count(hd.id) from HoaDon hd where EXTRACT(WEEK FROM hd.ngayGiaoHang) = EXTRACT(WEEK FROM CURRENT_DATE) AND EXTRACT(YEAR FROM hd.ngayGiaoHang) = EXTRACT(YEAR FROM CURRENT_DATE) and  hd.trangThai=:trangThai")
    public Integer getSoLuongHoaDonHoanThanhTuan(@Param("trangThai") Integer trangThai);

    @Query(value = "select count(hd.id) from hoa_don hd where hd.ngay_xac_nhan >= DATE_TRUNC('month', CURRENT_DATE) AND hd.ngay_xac_nhan < DATE_TRUNC('month', CURRENT_DATE) + INTERVAL '1 month' and  hd.trang_thai=:trangThai",nativeQuery = true)
    public Integer getSoLuongHoaDonXacNhanThang(@Param("trangThai") Integer trangThai);
    @Query(value = "select count(hd.id) from hoa_don hd where hd.ngay_giao_hang >= DATE_TRUNC('month', CURRENT_DATE) AND hd.ngay_giao_hang < DATE_TRUNC('month', CURRENT_DATE) + INTERVAL '1 month' and  hd.trang_thai=:trangThai",nativeQuery = true)
    public Integer getSoLuongHoaDonGiaoHangThang(@Param("trangThai") Integer trangThai);
    @Query(value = "select count(hd.id) from hoa_don hd where hd.ngay_hoan_thanh >= DATE_TRUNC('month', CURRENT_DATE) AND hd.ngay_hoan_thanh < DATE_TRUNC('month', CURRENT_DATE) + INTERVAL '1 month' and  hd.trang_thai=:trangThai",nativeQuery = true)
    public Integer getSoLuongHoaDonHoanThanhThang(@Param("trangThai") Integer trangThai);

    @Query(value = "select count(hd.id) from hoa_don hd where hd.ngay_xac_nhan >= DATE_TRUNC('year', CURRENT_DATE) AND hd.ngay_xac_nhan < DATE_TRUNC('year', CURRENT_DATE) + INTERVAL '1 year' and hd.trang_thai=:trangThai",nativeQuery = true)
    public Integer getSoLuongHoaDonXacNhanNam(@Param("trangThai") Integer trangThai);
    @Query(value = "select count(hd.id) from hoa_don hd where hd.ngay_giao_hang >= DATE_TRUNC('year', CURRENT_DATE) AND hd.ngay_giao_hang < DATE_TRUNC('year', CURRENT_DATE) + INTERVAL '1 year' and hd.trang_thai=:trangThai",nativeQuery = true)
    public Integer getSoLuongHoaDonGiaoHangNam(@Param("trangThai") Integer trangThai);

    @Query(value = "select count(hd.id) from hoa_don hd where hd.ngay_hoan_thanh >= DATE_TRUNC('month', CURRENT_DATE) AND hd.ngay_hoan_thanh < DATE_TRUNC('month', CURRENT_DATE) + INTERVAL '1 month' and  hd.trang_thai=:trangThai",nativeQuery = true)
    public Integer getSoLuongHoaDonThang(@Param("trangThai") Integer trangThai);
    @Query(value = "select count(hd.id) from hoa_don hd where hd.ngay_hoan_thanh >= DATE_TRUNC('year', CURRENT_DATE) AND hd.ngay_hoan_thanh < DATE_TRUNC('year', CURRENT_DATE) + INTERVAL '1 year' and hd.trang_thai=:trangThai",nativeQuery = true)
    public Integer getSoLuongHoaDonNam(@Param("trangThai") Integer trangThai);
    @Query(value = "select count(hd.id) from hoa_don hd where hd.trang_thai=:trangThai and hd.ngay_hoan_thanh between :tuNgay and :denNgay",nativeQuery = true)
    public Integer getSoLuongHoaDonHoanThanhTuyChinh(@Param("trangThai") Integer trangThai, @Param("tuNgay") Timestamp tuNgay, @Param("denNgay") Timestamp denNgay);
    @Query(value = "select count(hd.id) from hoa_don hd where hd.trang_thai=:trangThai and hd.ngay_giao_hang between :tuNgay and :denNgay",nativeQuery = true)
    public Integer getSoLuongHoaDonGiaoHangTuyChinh(@Param("trangThai") Integer trangThai, @Param("tuNgay") Timestamp tuNgay, @Param("denNgay") Timestamp denNgay);
    @Query(value = "select count(hd.id) from hoa_don hd where hd.trang_thai=:trangThai and hd.ngay_xac_nhan between :tuNgay and :denNgay",nativeQuery = true)
    public Integer getSoLuongHoaDonXacNhanTuyChinh(@Param("trangThai") Integer trangThai, @Param("tuNgay") Timestamp tuNgay, @Param("denNgay") Timestamp denNgay);

    @Query(value = "WITH LatestDates AS (" +
            "    SELECT id_hoa_don, MAX(ngay_tao) AS max_ngay_tao " +
            "    FROM lich_su_hoa_don " +
            "    GROUP BY id_hoa_don " +
            "), " +
            "LatestStatus AS (" +
            "    SELECT l.id_hoa_don, lshd.trang_thai, lshd.ngay_tao " +
            "    FROM LatestDates l " +
            "    JOIN lich_su_hoa_don lshd " +
            "    ON l.id_hoa_don = lshd.id_hoa_don " +
            "    AND l.max_ngay_tao = lshd.ngay_tao " +
            ") " +
            "SELECT COUNT(*) " +
            "FROM LatestStatus " +
            "WHERE trang_thai = :trangThai " +
            "AND ngay_tao BETWEEN :tuNgay AND :denNgay",
            nativeQuery = true)
    public Integer getSoLuongHoaDonHuyChoTtChoGh(@Param("trangThai") int trangThai, @Param("tuNgay") Timestamp tuNgay, @Param("denNgay") Timestamp denNgay);

    @Query(value = "SELECT spct.id as idSanPham, sp.ten_san_pham || COALESCE('-' || ms.ten_mau_sac , '') || " +
            "COALESCE('-' || cl.ten_chat_lieu , '') || COALESCE('-' || dg.ten_de_giay , '') || " +
            "COALESCE('-' || kt.ten_kich_thuoc , '') as tenSanPham, " +
            "sum(hdct.so_luong_san_pham) as soLuong, spct.gia as giaTien " +
            "FROM hoa_don_chi_tiet hdct " +
            "INNER JOIN hoa_don hd ON hdct.id_hoa_don = hd.id " +
            "INNER JOIN san_pham_chi_tiet spct ON spct.id = hdct.id_spct " +
            "INNER JOIN san_pham sp ON sp.id = spct.id_san_pham " +
            "INNER JOIN kich_thuoc kt ON kt.id = spct.id_kich_co " +
            "INNER JOIN chat_lieu cl ON cl.id = spct.id_chat_lieu " +
            "INNER JOIN thuong_hieu th ON th.id = spct.id_thuong_hieu " +
            "INNER JOIN mau_sac ms ON ms.id = spct.id_mau_sac " +
            "INNER JOIN de_giay dg ON dg.id = spct.id_de_giay " +
            "WHERE hd.trang_thai = 5 " +
            "GROUP BY spct.id, sp.ten_san_pham, spct.id, ms.id, cl.id, dg.id, kt.id order by COUNT(spct.id) DESC",
            nativeQuery = true)
    Page<Object[]> getPageSanPhamBanChay(Pageable pageable);

    @Query(value = "SELECT spct.id as idSanPham, sp.ten_san_pham || COALESCE('-' || ms.ten_mau_sac , '') || " +
            "COALESCE('-' || cl.ten_chat_lieu , '') || COALESCE('-' || dg.ten_de_giay , '') || " +
            "COALESCE('-' || kt.ten_kich_thuoc , '') as tenSanPham, " +
            "sum(hdct.so_luong_san_pham) as soLuong, spct.gia as giaTien " +
            "FROM hoa_don_chi_tiet hdct " +
            "INNER JOIN hoa_don hd ON hdct.id_hoa_don = hd.id " +
            "INNER JOIN san_pham_chi_tiet spct ON spct.id = hdct.id_spct " +
            "INNER JOIN san_pham sp ON sp.id = spct.id_san_pham " +
            "INNER JOIN kich_thuoc kt ON kt.id = spct.id_kich_co " +
            "INNER JOIN chat_lieu cl ON cl.id = spct.id_chat_lieu " +
            "INNER JOIN thuong_hieu th ON th.id = spct.id_thuong_hieu " +
            "INNER JOIN mau_sac ms ON ms.id = spct.id_mau_sac " +
            "INNER JOIN de_giay dg ON dg.id = spct.id_de_giay " +
            "WHERE hd.trang_thai = 5 and hd.ngay_hoan_thanh between :tuNgay and :denNgay " +
            "GROUP BY spct.id, sp.ten_san_pham, spct.id, ms.id, cl.id, dg.id, kt.id order by sum(hdct.so_luong_san_pham) DESC",
            nativeQuery = true)
    Page<Object[]> getPageSanPhamBanChayTuyChinh(Pageable pageable,@Param("tuNgay") Timestamp tuNgay, @Param("denNgay") Timestamp denNgay);

    @Query(value = "SELECT spct.id as idSanPham, sp.ten_san_pham || COALESCE('-' || ms.ten_mau_sac , '') || " +
            "COALESCE('-' || cl.ten_chat_lieu , '') || COALESCE('-' || dg.ten_de_giay , '') || " +
            "COALESCE('-' || kt.ten_kich_thuoc , '') as tenSanPham, " +
            "sum(hdct.so_luong_san_pham), spct.gia as giaTien " +
            "FROM hoa_don_chi_tiet hdct " +
            "INNER JOIN hoa_don hd ON hdct.id_hoa_don = hd.id " +
            "INNER JOIN san_pham_chi_tiet spct ON spct.id = hdct.id_spct " +
            "INNER JOIN san_pham sp ON sp.id = spct.id_san_pham " +
            "INNER JOIN kich_thuoc kt ON kt.id = spct.id_kich_co " +
            "INNER JOIN chat_lieu cl ON cl.id = spct.id_chat_lieu " +
            "INNER JOIN thuong_hieu th ON th.id = spct.id_thuong_hieu " +
            "INNER JOIN mau_sac ms ON ms.id = spct.id_mau_sac " +
            "INNER JOIN de_giay dg ON dg.id = spct.id_de_giay " +
            "WHERE DATE(hd.ngay_hoan_thanh) = CURRENT_DATE and hd.trang_thai = 5 " +
            "GROUP BY spct.id, sp.ten_san_pham, spct.id, ms.id, cl.id, dg.id, kt.id order by sum(hdct.so_luong_san_pham) DESC",
            nativeQuery = true)
    Page<Object[]> getPageSanPhamBanChayByNgay(Pageable pageable);
    @Query(value = "SELECT spct.id as idSanPham, sp.ten_san_pham || COALESCE('-' || ms.ten_mau_sac , '') || " +
            "COALESCE('-' || cl.ten_chat_lieu , '') || COALESCE('-' || dg.ten_de_giay , '') || " +
            "COALESCE('-' || kt.ten_kich_thuoc , '') as tenSanPham, " +
            "sum(hdct.so_luong_san_pham) as soLuong, spct.gia as giaTien " +
            "FROM hoa_don_chi_tiet hdct " +
            "INNER JOIN hoa_don hd ON hdct.id_hoa_don = hd.id " +
            "INNER JOIN san_pham_chi_tiet spct ON spct.id = hdct.id_spct " +
            "INNER JOIN san_pham sp ON sp.id = spct.id_san_pham " +
            "INNER JOIN kich_thuoc kt ON kt.id = spct.id_kich_co " +
            "INNER JOIN chat_lieu cl ON cl.id = spct.id_chat_lieu " +
            "INNER JOIN thuong_hieu th ON th.id = spct.id_thuong_hieu " +
            "INNER JOIN mau_sac ms ON ms.id = spct.id_mau_sac " +
            "INNER JOIN de_giay dg ON dg.id = spct.id_de_giay " +
            "WHERE EXTRACT(WEEK FROM hd.ngay_hoan_thanh) = EXTRACT(WEEK FROM CURRENT_DATE) AND EXTRACT(YEAR FROM hd.ngay_hoan_thanh) = EXTRACT(YEAR FROM CURRENT_DATE) and  hd.trang_thai = 5 " +
            "GROUP BY spct.id, sp.ten_san_pham, spct.id, ms.id, cl.id, dg.id, kt.id order by sum(hdct.so_luong_san_pham) DESC",
            nativeQuery = true)
    Page<Object[]> getPageSanPhamBanChayByTuan(Pageable pageable);
    @Query(value = "SELECT spct.id as idSanPham, sp.ten_san_pham || COALESCE('-' || ms.ten_mau_sac , '') || " +
            "COALESCE('-' || cl.ten_chat_lieu , '') || COALESCE('-' || dg.ten_de_giay , '') || " +
            "COALESCE('-' || kt.ten_kich_thuoc , '') as tenSanPham, " +
            "sum(hdct.so_luong_san_pham) as soLuong, spct.gia as giaTien " +
            "FROM hoa_don_chi_tiet hdct " +
            "INNER JOIN hoa_don hd ON hdct.id_hoa_don = hd.id " +
            "INNER JOIN san_pham_chi_tiet spct ON spct.id = hdct.id_spct " +
            "INNER JOIN san_pham sp ON sp.id = spct.id_san_pham " +
            "INNER JOIN kich_thuoc kt ON kt.id = spct.id_kich_co " +
            "INNER JOIN chat_lieu cl ON cl.id = spct.id_chat_lieu " +
            "INNER JOIN thuong_hieu th ON th.id = spct.id_thuong_hieu " +
            "INNER JOIN mau_sac ms ON ms.id = spct.id_mau_sac " +
            "INNER JOIN de_giay dg ON dg.id = spct.id_de_giay " +
            "WHERE hd.ngay_hoan_thanh >= DATE_TRUNC('month', CURRENT_DATE) AND hd.ngay_hoan_thanh < DATE_TRUNC('month', CURRENT_DATE) + INTERVAL '1 month' and  hd.trang_thai = 5 " +
            "GROUP BY spct.id, sp.ten_san_pham, spct.id, ms.id, cl.id, dg.id, kt.id order by sum(hdct.so_luong_san_pham) DESC",
            nativeQuery = true)
    Page<Object[]> getPageSanPhamBanChayByThang(Pageable pageable);
    @Query(value = "SELECT spct.id as idSanPham, sp.ten_san_pham || COALESCE('-' || ms.ten_mau_sac , '') || " +
            "COALESCE('-' || cl.ten_chat_lieu , '') || COALESCE('-' || dg.ten_de_giay , '') || " +
            "COALESCE('-' || kt.ten_kich_thuoc , '') as tenSanPham, " +
            "sum(hdct.so_luong_san_pham) as soLuong, spct.gia as giaTien " +
            "FROM hoa_don_chi_tiet hdct " +
            "INNER JOIN hoa_don hd ON hdct.id_hoa_don = hd.id " +
            "INNER JOIN san_pham_chi_tiet spct ON spct.id = hdct.id_spct " +
            "INNER JOIN san_pham sp ON sp.id = spct.id_san_pham " +
            "INNER JOIN kich_thuoc kt ON kt.id = spct.id_kich_co " +
            "INNER JOIN chat_lieu cl ON cl.id = spct.id_chat_lieu " +
            "INNER JOIN thuong_hieu th ON th.id = spct.id_thuong_hieu " +
            "INNER JOIN mau_sac ms ON ms.id = spct.id_mau_sac " +
            "INNER JOIN de_giay dg ON dg.id = spct.id_de_giay " +
            "WHERE hd.ngay_hoan_thanh >= DATE_TRUNC('year', CURRENT_DATE) AND hd.ngay_hoan_thanh < DATE_TRUNC('year', CURRENT_DATE) + INTERVAL '1 year' and hd.trang_thai = 5 " +
            "GROUP BY spct.id, sp.ten_san_pham, spct.id, ms.id, cl.id, dg.id, kt.id order by sum(hdct.so_luong_san_pham) DESC",
            nativeQuery = true)
    Page<Object[]> getPageSanPhamBanChayByNam(Pageable pageable);

    @Query(value = "\n" +
            "select spct.id as idSanPham, sp.ten_san_pham || COALESCE('-' || ms.ten_mau_sac , '') || COALESCE('-' || cl.ten_chat_lieu , '') || COALESCE('-' || dg.ten_de_giay , '') || COALESCE('-' || kt.ten_kich_thuoc , '')  as tenSanPham, spct.so_luong as soLuong, spct.gia as giaTien from san_pham_chi_tiet spct \n" +
            "inner join san_pham sp on sp.id =spct.id_san_pham " +
            "inner join kich_thuoc kt on kt.id = spct.id_kich_co " +
            "inner join chat_lieu cl on cl.id = spct.id_chat_lieu " +
            "inner join thuong_hieu th on th.id = spct.id_thuong_hieu " +
            "inner join mau_sac ms on ms.id =spct.id_mau_sac " +
            "inner join de_giay dg on dg.id =spct.id_de_giay " +
            "where  spct.so_luong <= :soLuongSp " +
            "group by spct.id, sp.ten_san_pham, spct.id, ms.id, cl.id, dg.id, kt.id order by spct.so_luong ASC ",
            nativeQuery = true)
    Page<Object[]> getPageSanPhamSapHet(@Param("soLuongSp") Integer soLuong, Pageable pageable);


    @Query("select count(hd) from HoaDon hd where hd.trangThai = 10")
    Integer countBillWait();

    @Transactional
    @Modifying
    @Query("update HoaDon hd set hd.trangThai = 6 where hd.id = :billId")
    Integer deleteBill(@Param("billId") Integer billId);
}
