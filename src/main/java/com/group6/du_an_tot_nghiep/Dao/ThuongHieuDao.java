package com.group6.du_an_tot_nghiep.Dao;

import com.group6.du_an_tot_nghiep.Entities.ThuongHieu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ThuongHieuDao extends JpaRepository<ThuongHieu,Integer> {

    public static final int DANG_BAN = 1;
    public static final int NGUNG_BAN = 0;
    public static final int DA_XOA = 2;

    @Query("Select data.id from ThuongHieu data")
    List<Integer> findAllId ();

    @Query("Select data.id from ThuongHieu data where data.trangThai=1")
    List<Integer> findAllIdDangBan ();

    @Query("Select data.id from ThuongHieu data where data.trangThai=1 or data.trangThai=0")
    List<Integer> findAllIdDangBanNgungBan ();

    Page<ThuongHieu> findAllByTrangThaiOrTrangThaiOrderByIdDesc (int DANG_BAN, int NGUNG_BAN, Pageable pageable);

    Page<ThuongHieu> findAllByTenThuongHieuIsLike(String tenThuongHieu, Pageable pageable);

    List<ThuongHieu> findAllByTrangThaiOrTrangThai(int DANG_BAN, int NGUNG_BAN );

    public List<ThuongHieu> findAllByTrangThai(int trangThai);
}
