package com.group6.du_an_tot_nghiep.Dao;


import com.group6.du_an_tot_nghiep.Entities.GioHang;
import com.group6.du_an_tot_nghiep.Entities.TaiKhoan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GioHangDao extends JpaRepository<GioHang,Integer> {
    public static int ACTIVE = 1;
    public static int COD = 1;
    public static int VNPAY = 2;
    Optional<GioHang> findByTaiKhoanByIdTaiKhoan(TaiKhoan taiKhoan);
}
