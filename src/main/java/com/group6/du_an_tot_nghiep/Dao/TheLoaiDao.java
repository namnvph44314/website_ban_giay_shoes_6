package com.group6.du_an_tot_nghiep.Dao;

import com.group6.du_an_tot_nghiep.Entities.MauSac;
import com.group6.du_an_tot_nghiep.Entities.TheLoai;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TheLoaiDao extends JpaRepository<TheLoai,Integer> {

    public static final int DANG_BAN = 1;
    public static final int NGUNG_BAN = 0;
    public static final int DA_XOA = 2;

    @Query("Select data.id from TheLoai data")
    List<Integer> findAllId ();

    @Query("Select data.id from TheLoai data where data.trangThai=1")
    List<Integer> findAllIdDangBan ();

    @Query("Select data.id from TheLoai data where data.trangThai=1 or data.trangThai=0")
    List<Integer> findAllIdDangBanNgungBan ();

    Page<TheLoai> findAllByTrangThaiOrTrangThaiOrderByIdDesc (int DANG_BAN, int NGUNG_BAN, Pageable pageable);

    Page<TheLoai> findAllByTenTheLoaiIsLike(String tenDeGiay, Pageable pageable);

    List<TheLoai> findAllByTrangThaiOrTrangThai(int DANG_BAN, int NGUNG_BAN );

    @Query("Select data from TheLoai data where data.trangThai= ?1 ORDER BY data.ngayTao DESC LIMIT 6")
    List<TheLoai> findAllDanhMucForTrangChu(int trangThai);
}
