package com.group6.du_an_tot_nghiep.Dao;

import com.group6.du_an_tot_nghiep.Dto.QuanLy.TaiKhoan.NhanVienDetail;
import com.group6.du_an_tot_nghiep.Dto.QuanLy.TaiKhoan.TaiKhoanResponse;


import com.group6.du_an_tot_nghiep.Entities.DiaChi;
import com.group6.du_an_tot_nghiep.Entities.PhieuGiamGia;
import com.group6.du_an_tot_nghiep.Entities.TaiKhoan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaiKhoanDao extends JpaRepository<TaiKhoan, Integer>, JpaSpecificationExecutor<TaiKhoan> {
    public static final int DANG_HOAT_DONG = 1;
    public static final int DA_XOA = 0;

    Optional<TaiKhoan> findByHoVaTen(String hovaTen);

    @Query("select tk from TaiKhoan tk where tk.trangThai=1 and tk.quyen='Khach_Hang'")
    public Page<TaiKhoan> getPage(Pageable pageable);

    public List<TaiKhoan> findAllByTrangThaiAndQuyen(int trangThai, String quyen);

    @Query("select tk from TaiKhoan tk where (:soDienThoai is null or tk.soDienThoai like %:soDienThoai%) and tk.trangThai=1 and tk.quyen='Khach_Hang'")
    public Page<TaiKhoan> getTKBySdt(@Param("soDienThoai") String soDienThoai, Pageable pageable);

    @Query("SELECT tk.anhDaiDien FROM TaiKhoan tk WHERE tk.id = ?1 ")
    String findUrlAnhDaiDien(int idTk);

    @Query("SELECT new com.group6.du_an_tot_nghiep.Dto.QuanLy.TaiKhoan.TaiKhoanResponse(tk.id, tk.anhDaiDien, tk.ma,tk.hoVaTen,tk.email,tk.soDienThoai,tk.ngaySinh,tk.gioiTinh,tk.quyen,tk.trangThai) FROM TaiKhoan tk WHERE tk.quyen='Khach_Hang' and " +
            "(:gioiTinh IS NULL OR tk.gioiTinh = :gioiTinh) AND " +
            "(:trangThai IS NULL OR tk.trangThai = :trangThai) order by tk.id DESC")
    public Page<TaiKhoanResponse> loc(@Param("gioiTinh") Integer gioiTinh, @Param("trangThai") Integer trangThai, Pageable pageable);

    @Query("SELECT new com.group6.du_an_tot_nghiep.Dto.QuanLy.TaiKhoan.TaiKhoanResponse(tk.id, tk.anhDaiDien, tk.ma,tk.hoVaTen,tk.email,tk.soDienThoai,tk.ngaySinh,tk.gioiTinh,tk.quyen,tk.trangThai) FROM TaiKhoan tk where tk.quyen='Khach_Hang' order by tk.id DESC")
    public Page<TaiKhoanResponse> getPageTk(Pageable pageable);

    @Query("SELECT new com.group6.du_an_tot_nghiep.Dto.QuanLy.TaiKhoan.TaiKhoanResponse(tk.id, tk.anhDaiDien, tk.ma,tk.hoVaTen,tk.email,tk.soDienThoai,tk.ngaySinh,tk.gioiTinh,tk.quyen,tk.trangThai) FROM TaiKhoan tk WHERE tk.quyen='Khach_Hang' and " +
            "(:timKiem IS NULL OR (tk.hoVaTen like %:timKiem% or tk.soDienThoai like %:timKiem% or tk.email like %:timKiem%)) order by tk.id DESC")
    public Page<TaiKhoanResponse> timKiem(@Param("timKiem") String timKiem,Pageable pageable);

    public TaiKhoan findTopByOrderByIdDesc();

    @Query("select tk from TaiKhoan tk where :soDienThoai is null or :soDienThoai=tk.soDienThoai")
    public TaiKhoan getTKBySdt(@Param("soDienThoai") String soDienThoai);

    @Query("SELECT tk from TaiKhoan tk where tk.id = :idCustomer and tk.quyen = 'Khach_Hang'")
    Optional<TaiKhoan> getCustomerById(@Param("idCustomer") Integer idCustomer);
    @Query("select tk from TaiKhoan tk where tk.soDienThoai=:sdt and tk.trangThai=1 and tk.quyen='Khach_Hang'")
    public TaiKhoan getSdt(@Param("sdt") String sdt);

    @Query("select tk from TaiKhoan tk where tk.soDienThoai=:sdt and tk.trangThai=1 and tk.quyen='Nhan_Vien'")
    public TaiKhoan getSdtNV(@Param("sdt") String sdt);

    @Query("select tk from TaiKhoan tk where tk.email=:email and tk.trangThai=1 and tk.quyen='Khach_Hang'")
    public TaiKhoan getEmail(@Param("email") String email);

    @Query("select tk from TaiKhoan tk where tk.email=:email and tk.trangThai=1 and tk.quyen='Nhan_Vien'")
    public TaiKhoan getEmailNV(@Param("email") String email);

    Optional<TaiKhoan> findBySoCanCuoc(String soCanCuoc);

    @Query("SELECT new com.group6.du_an_tot_nghiep.Dto.QuanLy.TaiKhoan.NhanVienDetail(tk.id, tk.anhDaiDien,tk.hoVaTen,tk.email,tk.soCanCuoc,tk.soDienThoai,tk.ngaySinh,tk.gioiTinh, dc.idTinh, dc.idHuyen, dc.idXa, dc.diaChiCuThe, tk.quyen,tk.trangThai) FROM TaiKhoan tk join DiaChi dc on dc.taiKhoanByIdTaiKhoan.id = tk.id where tk.id =:idTaiKhoan")
    NhanVienDetail getTkNhanVien(@Param("idTaiKhoan")Integer idTaiKhoan);

    @Query("select tk from TaiKhoan tk where tk.soDienThoai=:sdt and tk.trangThai=1 and tk.quyen='Khach_Hang' and tk.id != :id")
    public TaiKhoan getSdtUpdate(@Param("sdt") String sdt, @Param("id") int id);

    @Query("select tk from TaiKhoan tk where tk.email=:email and tk.trangThai=1 and tk.quyen='Khach_Hang' and tk.id != :id")
    public TaiKhoan getEmailUpdate(@Param("email") String email, @Param("id") int id);


    @Query("select tk from TaiKhoan tk where tk.soDienThoai=:sdt and tk.trangThai=1 and tk.quyen='Nhan_Vien' and tk.id != :id")
    public TaiKhoan getSdtUpdateNv(@Param("sdt") String sdt, @Param("id") int id);

    @Query("select tk from TaiKhoan tk where tk.email=:email and tk.trangThai=1 and tk.quyen='Nhan_Vien' and tk.id != :id")
    public TaiKhoan getEmailUpdateNV(@Param("email") String email, @Param("id") int id);

    @Query("select tk from TaiKhoan tk where tk.soCanCuoc=:soCanCuoc and tk.trangThai=1 and tk.quyen='Nhan_Vien' and tk.id != :id")
    public TaiKhoan getScccdUpdateNV(@Param("soCanCuoc") String soCanCuoc, @Param("id") int id);

    @Modifying
    @Transactional
    @Query("update TaiKhoan tk set tk.trangThai=?1 where tk.trangThai=?2 and tk.id=?3")
    public void doiTrangThai(Integer trangThai1, Integer trangThai2, int id);

    @Query("select dc from DiaChi dc where dc.id=:id")
    public DiaChi getDcByIdKhAndMd(@Param("id") int id);

    @Query("select dc from DiaChi dc where dc.taiKhoanByIdTaiKhoan.id=:id order by dc.id ASC")
    public Page<DiaChi> getPageDiaChi(@Param("id") int id, Pageable pageable);

    @Modifying
    @Transactional
    @Query("delete from DiaChi dc where dc.id=:idDc ")
    public void delete(@Param("idDc") int idDc);

    @Modifying
    @Transactional
    @Query("update DiaChi set macDinh=true where id=:idDc")
    public void updateMacDinhTrue(@Param("idDc") int idDc);

    @Modifying
    @Transactional
    @Query("update DiaChi set macDinh=false where id != :idDc and taiKhoanByIdTaiKhoan.id=:idTaiKhoan")
    public void updateMacDinhFalse(@Param("idDc") int idDc, @Param("idTaiKhoan") int idTaiKhoan);

    public TaiKhoan readByEmail(String email);

    @Transactional
    @Modifying
    @Query("update TaiKhoan tk set tk.matKhau = :matKhau where tk.email = :email")
    public Integer reChangePassword(@Param("matKhau") String matKhau, @Param("email") String email);
}
