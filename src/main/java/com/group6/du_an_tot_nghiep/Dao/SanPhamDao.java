package com.group6.du_an_tot_nghiep.Dao;

import com.group6.du_an_tot_nghiep.Entities.SanPham;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SanPhamDao extends JpaRepository<SanPham,Integer> {
    public static final int DANG_BAN = 1;
    public static final int NGUNG_BAN = 0;
    public static final int DA_XOA = 2;

    Page<SanPham> findAllByTrangThaiOrTrangThai (int DANG_BAN, int NGUNG_BAN,Pageable pageable);

    Page<SanPham> findAllByTrangThai (int trangThai, Pageable pageable);

    @Query("SELECT sp.id FROM SanPham sp WHERE sp.trangThai =1 OR sp.trangThai =0")
    List<Integer> findAllId ();

    @Query("SELECT sp.id FROM SanPham sp WHERE sp.trangThai = 1")
    List<Integer> findAllIdDangBan ();

    @Query("SELECT sp FROM SanPham sp WHERE sp.tenSanPham like %?1% AND sp.trangThai in ?2")
    Page<SanPham>  findByTenAndTrangThai (String ten, List<Integer> trangThai, Pageable pageable);

    @Query("select sp from SanPham sp where sp.tenSanPham like %:tenSp%")
    public Page<SanPham> getPageSpByTen(@Param("tenSp") String tenSp,Pageable pageable);

    SanPham findByTenSanPham(String tenSp);
}
