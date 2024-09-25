package com.group6.du_an_tot_nghiep.Config;

import com.group6.du_an_tot_nghiep.Dao.GiamGiaSanPhamChiTietDao;
import com.group6.du_an_tot_nghiep.Dao.GiamGiaSanPhamDao;
import com.group6.du_an_tot_nghiep.Service.QuanLy.PhieuGiamGiaKhachHangService;
import com.group6.du_an_tot_nghiep.Service.QuanLy.PhieuGiamGiaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;

@Component
public class StatusUpdateTask {

    @Autowired
    private GiamGiaSanPhamDao giamGiaSanPhamDao;
    @Autowired
    private GiamGiaSanPhamChiTietDao giamGiaSanPhamChiTietDao;
    @Autowired
    private PhieuGiamGiaService phieuGiamGiaService;
    @Autowired
    private PhieuGiamGiaKhachHangService phieuGiamGiaKhachHangService;

    @Scheduled(cron = "0 * * * * ?") // Chạy mỗi phút
    public void updateStatusEveryMinute() {
        Instant now = Instant.now();
        Timestamp ngayHienTai = Timestamp.from(now);
        giamGiaSanPhamDao.updateHetHan(ngayHienTai);
        giamGiaSanPhamDao.updateDangHoatDong(ngayHienTai);
//        giamGiaSanPhamDao.updateGiamGiaSanPhamChiTietWithMaxNgayBatDau();
//        giamGiaSanPhamDao.updateGiamGiaSanPhamChiTietWithNotMaxNgayBatDau();

        giamGiaSanPhamChiTietDao.updateTtHetHan();
        phieuGiamGiaService.updateTTNHD(ngayHienTai);
        phieuGiamGiaService.updateTTDDR(ngayHienTai);
    }

}
