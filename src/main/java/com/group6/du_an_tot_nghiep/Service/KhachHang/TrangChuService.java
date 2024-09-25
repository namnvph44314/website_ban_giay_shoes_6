package com.group6.du_an_tot_nghiep.Service.KhachHang;

import com.group6.du_an_tot_nghiep.Dao.*;
import com.group6.du_an_tot_nghiep.Dto.KhachHang.ListGioHangChiTietResponse;
import com.group6.du_an_tot_nghiep.Dto.KhachHang.ListSanPhamResponse;
import com.group6.du_an_tot_nghiep.Entities.GiamGiaSanPhamChiTiet;
import com.group6.du_an_tot_nghiep.Entities.SanPhamChiTiet;
import com.group6.du_an_tot_nghiep.Entities.TheLoai;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TrangChuService {
    @Autowired
    TheLoaiDao theLoaiDao;

    @Autowired
    SanPhamChiTietDao sanPhamChiTietDao;

    @Autowired
    SanPhamDao sanPhamDao;

    @Autowired
    GiamGiaSanPhamDao giamGiaSanPhamDao;

    @Autowired
    GiamGiaSanPhamChiTietDao giamGiaSanPhamChiTietDao;

    public List<TheLoai> findAllDanhMucForTrangChu (int trangThai) {
        return theLoaiDao.findAllDanhMucForTrangChu(trangThai);
    }

    public List<ListSanPhamResponse> findHangMoiVe () {
        List<Integer> listIdSanPham = sanPhamDao.findAllIdDangBan();
        List<SanPhamChiTiet> sanPhamChiTietList = new ArrayList<>();
        listIdSanPham.stream().forEach(idsp -> {
            sanPhamChiTietDao.findDistinctIdMauSac(idsp).stream().forEach(idMauSac -> {
                sanPhamChiTietList.add(sanPhamChiTietDao.findAllSpDistinctMauSac(idsp,idMauSac));
            });
        });
        List<ListSanPhamResponse> listData = new ArrayList<>();
        sanPhamChiTietList.stream().forEach(spct -> {
            BigDecimal giaGiam = new BigDecimal(0);
            int giaTri = 0;
            int loaiGiamGia = 0;
            GiamGiaSanPhamChiTiet giamGiaSanPhamChiTiet = new GiamGiaSanPhamChiTiet();
            ListSanPhamResponse response = new ListSanPhamResponse();
            giamGiaSanPhamChiTiet = giamGiaSanPhamChiTietDao.findGiaGiamAndIdGiamGiaByIdScpt(spct.getId());
            if (giamGiaSanPhamChiTiet == null) {
                response.setGiaGiam(spct.getGia());
                response.setGiaTri(giaTri);
                response.setLoaiGiamGia(loaiGiamGia);
                response.setTenSanPham(spct.getSanPhamByIdSanPham().getTenSanPham());
                response.setId(spct.getId());
                response.setMa(spct.getMa());
                response.setGia(spct.getGia());
                response.setNgayTao(spct.getNgayTao());
                response.setMauSac(spct.getMauSacByIdMauSac());
                response.setKichThuoc(spct.getKichThuocByIdKichCo());
                listData.add(response);
            }else {
                giaGiam = giamGiaSanPhamChiTiet.getGiaGiam();
                giaTri = giamGiaSanPhamDao.findGiaTriById(giamGiaSanPhamChiTiet.getIdGiamGia());
                loaiGiamGia = giamGiaSanPhamDao.findLoaiById(giamGiaSanPhamChiTiet.getIdGiamGia());
                response.setGiaGiam(giaGiam);
                response.setGiaTri(giaTri);
                response.setLoaiGiamGia(loaiGiamGia);
                response.setTenSanPham(spct.getSanPhamByIdSanPham().getTenSanPham());
                response.setId(spct.getId());
                response.setMa(spct.getMa());
                response.setGia(spct.getGia());
                response.setNgayTao(spct.getNgayTao());
                response.setMauSac(spct.getMauSacByIdMauSac());
                response.setKichThuoc(spct.getKichThuocByIdKichCo());
                listData.add(response);
            }
        });
        Collections.sort(listData, Comparator.comparing(ListSanPhamResponse::getNgayTao).reversed());
        if (listData.size() <= 8) {
            return listData;
        }else{
            for (int i = listData.size() - 1 ; i > 7; i--) {
                listData.remove(i);
            }
            return listData;
        }
    }
}
