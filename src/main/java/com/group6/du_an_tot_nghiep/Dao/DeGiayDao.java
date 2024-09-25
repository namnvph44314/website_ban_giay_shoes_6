package com.group6.du_an_tot_nghiep.Dao;

import com.group6.du_an_tot_nghiep.Entities.DeGiay;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeGiayDao extends JpaRepository<DeGiay,Integer> {

    public static final int DANG_BAN = 1;
    public static final int NGUNG_BAN = 0;
    public static final int DA_XOA = 2;

    @Query("Select data.id from DeGiay data")
    List<Integer> findAllId ();

    @Query("Select data.id from DeGiay data where data.trangThai=1")
    List<Integer> findAllIdDangBan ();

    @Query("Select data.id from DeGiay data where data.trangThai=1 or data.trangThai=0")
    List<Integer> findAllIdDangBanNgungBan ();

    Page<DeGiay> findAllByTrangThaiOrTrangThaiOrderByIdDesc (int DANG_BAN, int NGUNG_BAN,Pageable pageable);

    Page<DeGiay> findAllByTenDeGiayIsLike(String tenDeGiay, Pageable pageable);

    List<DeGiay> findAllByTrangThaiOrTrangThai(int DANG_BAN, int NGUNG_BAN );

    public List<DeGiay> findAllByTrangThai(int trangThai);
}
