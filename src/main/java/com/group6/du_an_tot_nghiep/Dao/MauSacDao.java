package com.group6.du_an_tot_nghiep.Dao;

import com.group6.du_an_tot_nghiep.Entities.MauSac;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MauSacDao extends JpaRepository<MauSac,Integer> {

    public static final int DANG_BAN = 1;
    public static final int NGUNG_BAN = 0;
    public static final int DA_XOA = 2;

    @Query("Select data.id from MauSac data")
    List<Integer> findAllId ();

    @Query("Select data.id from MauSac data where data.trangThai=1")
    List<Integer> findAllIdDangBan ();

    @Query("Select data.id from MauSac data where data.trangThai=1 or data.trangThai=0")
    List<Integer> findAllIdDangBanNgungBan ();

    Page<MauSac> findAllByTrangThaiOrTrangThaiOrderByIdDesc (int DANG_BAN, int NGUNG_BAN, Pageable pageable);

    Page<MauSac> findAllByTenMauSacIsLike(String tenMauSac, Pageable pageable);

    List<MauSac> findAllByTrangThaiOrTrangThai(int DANG_BAN, int NGUNG_BAN );

    public List<MauSac> findAllByTrangThai(int trangThai);
}
