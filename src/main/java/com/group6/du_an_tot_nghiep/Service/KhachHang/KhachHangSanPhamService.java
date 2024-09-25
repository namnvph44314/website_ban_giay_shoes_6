package com.group6.du_an_tot_nghiep.Service.KhachHang;

import com.group6.du_an_tot_nghiep.Dao.*;
import com.group6.du_an_tot_nghiep.Dto.KhachHang.ChangeSizeSanPham;
import com.group6.du_an_tot_nghiep.Dto.KhachHang.KhachHangSanPhamResponse;
import com.group6.du_an_tot_nghiep.Dto.KhachHang.ListSanPhamResponse;
import com.group6.du_an_tot_nghiep.Dto.KhachHang.SanPhamLienQuan;
import com.group6.du_an_tot_nghiep.Entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class KhachHangSanPhamService {
    @Autowired
    SanPhamChiTietDao sanPhamChiTietDao;

    @Autowired
    SanPhamDao sanPhamDao;

    @Autowired
    GiamGiaSanPhamDao giamGiaSanPhamDao;

    @Autowired
    GiamGiaSanPhamChiTietDao giamGiaSanPhamChiTietDao;

    @Autowired
    ChatLieuDao chatLieuDao;

    @Autowired
    DeGiayDao deGiayDao;

    @Autowired
    KichThuocDao kichThuocDao;

    @Autowired
    MauSacDao mauSacDao;

    @Autowired
    TheLoaiDao theLoaiDao;

    @Autowired
    ThuongHieuDao thuongHieuDao;

    @Autowired
    HinhAnhDao hinhAnhDao;

    @Autowired
    GioHangDao gioHangDao;

    @Autowired
    GioHangChiTietDao gioHangChiTietDao;

    public String findUrlByIdHinhAnh (int id) {
        return hinhAnhDao.findUrlById(id);
    }

    public String findUrlAnhMacDinh (int idSpct) {
        return hinhAnhDao.findUrlAnhMacDinh(idSpct);
    }

    public KhachHangSanPhamResponse getReponseById (int idSpct, int idMauSac, int idKichThuoc) {
        SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietDao.findSanPhamByMauSacAndKichThuoc(idMauSac,idSpct,idKichThuoc);
        GiamGiaSanPhamChiTiet giamGiaSanPhamChiTiet = giamGiaSanPhamChiTietDao.findGiaGiamAndIdGiamGiaByIdScpt(idSpct);
        List<HinhAnh> hinhAnhList = hinhAnhDao.findAllByIdSpct(sanPhamChiTiet.getId());
        KhachHangSanPhamResponse response = new KhachHangSanPhamResponse();
        response.setIdSpct(sanPhamChiTiet.getId());
        response.setGia(sanPhamChiTiet.getGia());
        response.setTenSanPham(sanPhamChiTiet.getSanPhamByIdSanPham().getTenSanPham());
        if (giamGiaSanPhamChiTiet == null) {
            response.setGiaGiam(new BigDecimal(0));
        }else {
            response.setGiaGiam(sanPhamChiTiet.getGia().subtract(giamGiaSanPhamChiTiet.getGiaGiam()));
        }
        response.setKichThuocList(sanPhamChiTietDao.getSizesByIdspctAndIdMau(idMauSac,idSpct));
        response.setSoLuongKho(sanPhamChiTiet.getSoLuong());
        response.setMoTa(sanPhamChiTiet.getMoTa());
        response.setChatLieu(sanPhamChiTiet.getChatLieuByIdChatLieu());
        response.setDeGiay(sanPhamChiTiet.getDeGiayByIdDeGiay());
        response.setMauSacList(sanPhamChiTietDao.findAllMauSacSanPhamKhachHang(sanPhamChiTiet.getId()));
        response.setTheLoai(sanPhamChiTiet.getTheLoaiByIdDanhMuc());
        response.setThuongHieu(sanPhamChiTiet.getThuongHieuByIdThuongHieu());
        response.setMauDangHien(sanPhamChiTiet.getMauSacByIdMauSac().getId());
        response.setTenMauDangHien(sanPhamChiTiet.getMauSacByIdMauSac().getTenMauSac());
        response.setSizeDangHien(sanPhamChiTiet.getKichThuocByIdKichCo().getId());
        response.setHinhAnhList(hinhAnhList);
        List<HinhAnh> anhMacDinhList = new ArrayList<>();
        for (MauSac mauSac: response.getMauSacList()) {
            HinhAnh hinhAnh = hinhAnhDao.findAllAnhMacDinhSp(sanPhamChiTiet.getSanPhamByIdSanPham(), mauSac);
            anhMacDinhList.add(hinhAnh);
        }
        response.setAnhMacDinhList(anhMacDinhList);
        return response;
    }

    public KhachHangSanPhamResponse getReponseByIdChuyenMau (int idSpct, int idMauSac) {
        SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietDao.findSanPhamByMauSac(idMauSac,idSpct);
        GiamGiaSanPhamChiTiet giamGiaSanPhamChiTiet = giamGiaSanPhamChiTietDao.findGiaGiamAndIdGiamGiaByIdScpt(sanPhamChiTiet.getId());
        List<HinhAnh> hinhAnhList = hinhAnhDao.findAllByIdSpct(sanPhamChiTiet.getId());
        KhachHangSanPhamResponse response = new KhachHangSanPhamResponse();
        response.setIdSpct(sanPhamChiTiet.getId());
        response.setGia(sanPhamChiTiet.getGia());
        response.setTenSanPham(sanPhamChiTiet.getSanPhamByIdSanPham().getTenSanPham());
        if (giamGiaSanPhamChiTiet == null) {
            response.setGiaGiam(new BigDecimal(0));
        }else {
            response.setGiaGiam(sanPhamChiTiet.getGia().subtract(giamGiaSanPhamChiTiet.getGiaGiam()));
        }
        response.setKichThuocList(sanPhamChiTietDao.getSizesByIdspctAndIdMau(idMauSac,idSpct));
        response.setSoLuongKho(sanPhamChiTiet.getSoLuong());
        response.setMoTa(sanPhamChiTiet.getMoTa());
        response.setChatLieu(sanPhamChiTiet.getChatLieuByIdChatLieu());
        response.setDeGiay(sanPhamChiTiet.getDeGiayByIdDeGiay());
        response.setMauSacList(sanPhamChiTietDao.findAllMauSacSanPhamKhachHang(sanPhamChiTiet.getId()));
        response.setTheLoai(sanPhamChiTiet.getTheLoaiByIdDanhMuc());
        response.setThuongHieu(sanPhamChiTiet.getThuongHieuByIdThuongHieu());
        response.setMauDangHien(sanPhamChiTiet.getMauSacByIdMauSac().getId());
        response.setTenMauDangHien(sanPhamChiTiet.getMauSacByIdMauSac().getTenMauSac());
        response.setSizeDangHien(sanPhamChiTiet.getKichThuocByIdKichCo().getId());
        response.setHinhAnhList(hinhAnhList);
        List<HinhAnh> anhMacDinhList = new ArrayList<>();
        for (MauSac mauSac: response.getMauSacList()) {
            HinhAnh hinhAnh = hinhAnhDao.findAllAnhMacDinhSp(sanPhamChiTiet.getSanPhamByIdSanPham(), mauSac);
            anhMacDinhList.add(hinhAnh);
        }
        response.setAnhMacDinhList(anhMacDinhList);
        return response;
    }

    public List<SanPhamLienQuan> findSanPhamLienQuan (int idDanhMuc, int idSpctHienTai) {
        List<Integer> listIdSanPham = sanPhamChiTietDao.findIdSanPhamLienQuan(idDanhMuc,sanPhamChiTietDao.findIdSanPhamByIdspct(idSpctHienTai));
        List<SanPhamChiTiet> sanPhamChiTietList = new ArrayList<>();
        listIdSanPham.stream().forEach(idsp -> {
            sanPhamChiTietDao.findDistinctIdMauSacForSanPhamLienQuan(idsp,idDanhMuc).stream().forEach(idMauSac -> {
                sanPhamChiTietList.add(sanPhamChiTietDao.findAllSpDistinctMauSac(idsp,idMauSac));
            });
        });
        List<SanPhamLienQuan> listData = new ArrayList<>();
        for (int i = 0; i < sanPhamChiTietList.size(); i++) {
            if (i == 4) {
                break;
            }else {
                SanPhamChiTiet spct = sanPhamChiTietList.get(i);
                SanPhamLienQuan sanPhamLienQuan = new SanPhamLienQuan();
                sanPhamLienQuan.setTenSanPham(sanPhamChiTietDao.findTenSanPhamByIdspct(spct.getId()));
                sanPhamLienQuan.setIdSpct(spct.getId());
                sanPhamLienQuan.setGia(spct.getGia());
                sanPhamLienQuan.setMauSac(spct.getMauSacByIdMauSac());
                sanPhamLienQuan.setKichThuoc(spct.getKichThuocByIdKichCo());
                GiamGiaSanPhamChiTiet giamGiaSanPhamChiTiet = giamGiaSanPhamChiTietDao.findGiaGiamAndIdGiamGiaByIdScpt(spct.getId());
                if (giamGiaSanPhamChiTiet == null) {
                    sanPhamLienQuan.setGiaGiam(new BigDecimal(0));
                }else {
                    sanPhamLienQuan.setGiaGiam(spct.getGia().subtract(giamGiaSanPhamChiTiet.getGiaGiam()));
                }
                listData.add(sanPhamLienQuan);
            }
        }
        return listData;
    }

    public ChangeSizeSanPham changeSize (int idSpct, int idMauSac, int idKichCo) {
        SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietDao.findSpctToAddGioHang(idMauSac,idSpct,idKichCo);
        BigDecimal giaGiam = giamGiaSanPhamChiTietDao.findGiaGiamByIdSpct(sanPhamChiTiet.getId());
        if (giaGiam == null) {
            return new ChangeSizeSanPham(sanPhamChiTiet.getId(),sanPhamChiTiet.getGia(),new BigDecimal(0),sanPhamChiTiet.getSoLuong());
        }else{
            return new ChangeSizeSanPham(sanPhamChiTiet.getId(),sanPhamChiTiet.getGia(),sanPhamChiTiet.getGia().subtract(giaGiam),sanPhamChiTiet.getSoLuong());
        }
    }

    public int getSoLuongSpTrongGh (TaiKhoan taiKhoan) {
        return gioHangChiTietDao.fingGhctByGioHangAndTrangThaiSpct(gioHangDao.findByTaiKhoanByIdTaiKhoan(taiKhoan).get(),1).size();
    }

}
