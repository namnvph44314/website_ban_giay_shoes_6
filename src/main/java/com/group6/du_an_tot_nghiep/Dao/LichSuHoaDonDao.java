package com.group6.du_an_tot_nghiep.Dao;

import com.group6.du_an_tot_nghiep.Entities.HoaDon;
import com.group6.du_an_tot_nghiep.Entities.LichSuHoaDon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LichSuHoaDonDao extends JpaRepository<LichSuHoaDon,Integer> {
    public static final int DANG_BAN = 1;
    public static final int NGUNG_BAN = 0;
    public static final int DA_XOA = 2;


//    List<LichSuHoaDon> findAllByHoaDonByIdHoaDon (HoaDon hoaDon);

    // Tìm bản ghi lịch sử hóa đơn gần nhất của hóa đơn theo ID, sắp xếp theo ngày tạo giảm dần
    List<LichSuHoaDon> findAllByHoaDonByIdHoaDonOrderByNgayTaoDesc(HoaDon hoaDon);
}


