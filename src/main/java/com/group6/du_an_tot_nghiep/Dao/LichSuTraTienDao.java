package com.group6.du_an_tot_nghiep.Dao;

import com.group6.du_an_tot_nghiep.Entities.LichSuTraTien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LichSuTraTienDao extends JpaRepository<LichSuTraTien, Integer> {

    @Query("select sum(lstt.soTien) from LichSuTraTien lstt where lstt.idHoaDon = :billId")
    Integer sumSoTienByIdHoaDon(@Param("billId") Integer billId);

    @Query("select ls from LichSuTraTien ls where ls.idHoaDon = :billId and (ls.phuongThuc = 1 or ls.phuongThuc = 2)")
    List<LichSuTraTien> getLichSuTraTienMat(@Param("billId") Integer billId);

    @Query("select ls from LichSuTraTien ls where ls.idHoaDon = :billId and ls.phuongThuc = 2")
    List<LichSuTraTien> getLichSuChuyenKhoan(@Param("billId") Integer billId);

    @Query("select ls from LichSuTraTien ls where ls.idHoaDon = :billId")
    List<LichSuTraTien> getLichSuTraTien(@Param("billId") Integer billId);
}
