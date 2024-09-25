package com.group6.du_an_tot_nghiep.Dao;

import com.group6.du_an_tot_nghiep.Entities.DiaChi;
import com.group6.du_an_tot_nghiep.Entities.TaiKhoan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

@Repository
public interface DiaChiDao extends JpaRepository<DiaChi,Integer> {

    @Query("select dc from DiaChi dc where dc.taiKhoanByIdTaiKhoan = ?1 and dc.macDinh=true")
    DiaChi findMacDinhByTaiKhoan (TaiKhoan taiKhoan);

    List<DiaChi> findByTaiKhoanByIdTaiKhoan (TaiKhoan taiKhoan);

    @Modifying
    @Transactional
    @Query("update DiaChi dc set dc.macDinh = false where dc.taiKhoanByIdTaiKhoan = ?1 and dc.macDinh = true")
    int updateDiaChiMacDinh(TaiKhoan taiKhoan);

    @Query("select dc from DiaChi dc where dc.taiKhoanByIdTaiKhoan.id = :id")
    List<DiaChi> getDiaChiByTaiKhoanByIdTaiKhoan(@Param("id") Integer idTaiKhoan);

    @Query("select count(dc) from DiaChi dc where dc.taiKhoanByIdTaiKhoan.id = :id")
    Integer countAllByTaiKhoanByIdTaiKhoan(@Param("id") Integer idTaiKhoan);

    @Query("select dc from DiaChi dc where dc.taiKhoanByIdTaiKhoan.id = :id and dc.macDinh = true")
    Optional<DiaChi> getAddressDefault(@Param("id") Integer idTaiKhoan);

}
