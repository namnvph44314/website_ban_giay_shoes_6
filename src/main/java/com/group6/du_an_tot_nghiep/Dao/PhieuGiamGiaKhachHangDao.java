package com.group6.du_an_tot_nghiep.Dao;

import com.group6.du_an_tot_nghiep.Entities.PhieuGiamGia;
import com.group6.du_an_tot_nghiep.Entities.PhieuGiamGiaKhachHang;
import com.group6.du_an_tot_nghiep.Entities.TaiKhoan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public interface PhieuGiamGiaKhachHangDao extends JpaRepository<PhieuGiamGiaKhachHang,Integer> {
    public static final int DA_DUNG=0;
    public static final int CHUA_DUNG=1;
    public static final int HET_HAN=2;

    PhieuGiamGiaKhachHang findByIdPhieuGiamGia (PhieuGiamGia phieuGiamGia);

    @Query("select pggKh from PhieuGiamGiaKhachHang pggKh where pggKh.idPhieuGiamGia.id=:idPgg")
    public List<PhieuGiamGiaKhachHang> findAllPGGKHByIdPGG(@Param("idPgg") int id);

    @Query("select pggKh from PhieuGiamGiaKhachHang pggKh where pggKh.idPhieuGiamGia.maKhuyenMai=:maPgg")
    public List<PhieuGiamGiaKhachHang> findAllPGGKHByMaPGG(@Param("maPgg") String maPgg);

    @Modifying
    @Transactional
    @Query("delete from PhieuGiamGiaKhachHang pggKh where pggKh.idPhieuGiamGia.maKhuyenMai=:maKM and pggKh.idTaiKhoan.id=:idKh")
    public void deletePggKh(@Param("maKM") String maKM, @Param("idKh") int idKh);

    @Modifying
    @Transactional
    @Query("delete from PhieuGiamGiaKhachHang pggKh where pggKh.idPhieuGiamGia.maKhuyenMai=:maKM")
    public void deleteByPgg(@Param("maKM") String ma);


    /**
     Lấy list kh đã add vô pgg theo id pgg
     */
    @Query("select tk from TaiKhoan tk join PhieuGiamGiaKhachHang pggKh on tk.id=pggKh.idTaiKhoan.id where pggKh.idPhieuGiamGia.id=:idPgg")
    public Page<TaiKhoan> getListTkByIdPgg(@Param("idPgg") int idPgg, Pageable pageable);

    @Query("select pggKh from  PhieuGiamGiaKhachHang pggKh where pggKh.idPhieuGiamGia.id=:idPgg")
    public Page<PhieuGiamGiaKhachHang> getListPggKhByIdPgg(@Param("idPgg") int idPgg, Pageable pageable);

    /**
     Lấy list kh chưa add vô pgg theo id pgg
     */
    @Query("select tk from TaiKhoan tk where tk.id not in (select tk.id from TaiKhoan tk join PhieuGiamGiaKhachHang pggKh on tk.id=pggKh.idTaiKhoan.id where pggKh.idPhieuGiamGia.id = :idPgg) and tk.trangThai=1 and tk.quyen='Khach_Hang'")
    public Page<TaiKhoan> getListTkByIdPggChuaAdd(@Param("idPgg") int idPgg, Pageable pageable);

    /**
     Lấy list kh chưa add vô pgg theo id pgg và sđt
     */
    @Query("select tk from TaiKhoan tk where tk.id not in (select tk.id from TaiKhoan tk join PhieuGiamGiaKhachHang pggKh on tk.id=pggKh.idTaiKhoan.id where pggKh.idPhieuGiamGia.id = :idPgg) and tk.soDienThoai like %:sdt% and tk.trangThai=1 and tk.quyen='Khach_Hang'")
    public Page<TaiKhoan> getTkByIdPggChuaAdd(@Param("idPgg") int idPgg, @Param("sdt") Optional<String> sdt, Pageable pageable);


    @Query("select count(pggKh.idTaiKhoan) from PhieuGiamGiaKhachHang pggKh where pggKh.idPhieuGiamGia.id=:idPgg")
    public int soLuongKhSoHuu(@Param("idPgg") int idPgg);

    /**
     Update trạng thái kh sử dụng pgg
     */
    @Transactional
    @Modifying
    @Query("update PhieuGiamGiaKhachHang pggKh set pggKh.trangThai=2 where pggKh.trangThai=0 and pggKh.idPhieuGiamGia.ngayKetThuc < current_timestamp")
    public void updateTtPggKh();

        Optional<PhieuGiamGiaKhachHang> findByIdPhieuGiamGiaAndIdTaiKhoan(PhieuGiamGia idPhieuGiamGia, TaiKhoan idTaiKhoan);

    @Transactional
    @Modifying
    @Query("update PhieuGiamGiaKhachHang pggKh set pggKh.trangThai = 1, pggKh.ngayTao = :ngayTao, pggKh.nguoiTao = :nguoiTao where pggKh.idPhieuGiamGia.id = :idPGG and pggKh.idTaiKhoan.id = :idTaiKhoan")
    public void updateTrangThaiPGGKH(@Param("ngayTao") Timestamp ngayTao, @Param("nguoiTao") String nguoiTao, @Param("idPGG") Integer idPGG, @Param("idTaiKhoan") Integer idTaiKhoan);
}
