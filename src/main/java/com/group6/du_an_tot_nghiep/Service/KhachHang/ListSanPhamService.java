package com.group6.du_an_tot_nghiep.Service.KhachHang;

import com.group6.du_an_tot_nghiep.Dao.*;
import com.group6.du_an_tot_nghiep.Dto.KhachHang.KhachHangSanPhamSearchRequest;
import com.group6.du_an_tot_nghiep.Dto.KhachHang.ListSanPhamResponse;
import com.group6.du_an_tot_nghiep.Dto.KhachHang.ListSanPhamSideBarFilterRequest;
import com.group6.du_an_tot_nghiep.Entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ListSanPhamService {
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
    GioHangDao gioHangDao;

    @Autowired
    GioHangChiTietDao gioHangChiTietDao;

    public PagedListHolder<ListSanPhamResponse> findAll (int pageNumber, int kieuLoc) {
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
                giaGiam = spct.getGia().subtract(giamGiaSanPhamChiTiet.getGiaGiam());
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
        PagedListHolder<ListSanPhamResponse> pageData = new PagedListHolder<>(listData);
        pageData.setPage(pageNumber);
        pageData.setPageSize(9);
        List<ListSanPhamResponse> listCanLoc = pageData.getSource();
        List<ListSanPhamResponse> listDaLoc = new ArrayList<>();
        if (kieuLoc == 0){
            listDaLoc = listCanLoc.stream().sorted(Comparator.comparing(ListSanPhamResponse::getNgayTao).reversed())
                    .collect(Collectors.toList());
            pageData.setSource(listDaLoc);
        }
        if (kieuLoc == 1){
            listDaLoc = listCanLoc.stream().sorted(Comparator.comparing(ListSanPhamResponse::getNgayTao))
                    .collect(Collectors.toList());
            pageData.setSource(listDaLoc);
        }
        if (kieuLoc == 2){
            listDaLoc = listCanLoc.stream().sorted(Comparator.comparing(ListSanPhamResponse::getGiaGiam))
                    .collect(Collectors.toList());
            pageData.setSource(listDaLoc);
        }
        if (kieuLoc == 3){
            listDaLoc = listCanLoc.stream().sorted(Comparator.comparing(ListSanPhamResponse::getGiaGiam).reversed())
                    .collect(Collectors.toList());
            pageData.setSource(listDaLoc);
        }
        return pageData;
    }

    public PagedListHolder<ListSanPhamResponse> sideBarFilter (int pageNumber, ListSanPhamSideBarFilterRequest request, int kieuLoc) {
        if (request.getChatLieuList().isEmpty()) {
            request.setChatLieuList(chatLieuDao.findAllIdDangBanNgungBan());
        }
        if (request.getDeGiayList().isEmpty()) {
            request.setDeGiayList(deGiayDao.findAllIdDangBanNgungBan());
        }
        if (request.getMauSacList().isEmpty()) {
            request.setMauSacList(mauSacDao.findAllIdDangBanNgungBan());
        }
        if (request.getTheLoaiList().isEmpty()) {
            request.setTheLoaiList(theLoaiDao.findAllIdDangBanNgungBan());
        }
        if (request.getThuongHieuList().isEmpty()) {
            request.setThuongHieuList(thuongHieuDao.findAllIdDangBanNgungBan());
        }
        if (request.getKichThuocList().isEmpty()) {
            request.setKichThuocList(kichThuocDao.findAllIdDangBanNgungBan());
        }
        List<Integer> listIdSanPham = sanPhamChiTietDao.findDistinctIdSpForSideBarFilter(
                request.getStartPrice(),
                request.getEndPrice(),
                request.getChatLieuList(),
                request.getDeGiayList(),
                request.getMauSacList(),
                request.getTheLoaiList(),
                request.getThuongHieuList(),
                request.getKichThuocList());
        List<SanPhamChiTiet> sanPhamChiTietList = new ArrayList<>();
        listIdSanPham.stream().forEach(idsp -> {
            sanPhamChiTietDao.findDistinctIdMauSacSideBarFilter(
                    request.getStartPrice(),
                    request.getEndPrice(),
                    request.getChatLieuList(),
                    request.getDeGiayList(),
                    request.getMauSacList(),
                    request.getTheLoaiList(),
                    request.getThuongHieuList(),
                    request.getKichThuocList(),
                    idsp
            ).stream().forEach(idMauSac -> {
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
        PagedListHolder<ListSanPhamResponse> pageData = new PagedListHolder<>(listData);
        pageData.setPage(pageNumber);
        pageData.setPageSize(9);
        List<ListSanPhamResponse> listCanLoc = pageData.getSource();
        List<ListSanPhamResponse> listDaLoc = new ArrayList<>();
        if (kieuLoc == 0){
            listDaLoc = listCanLoc.stream().sorted(Comparator.comparing(ListSanPhamResponse::getNgayTao).reversed())
                    .collect(Collectors.toList());
            pageData.setSource(listDaLoc);
        }
        if (kieuLoc == 1){
            listDaLoc = listCanLoc.stream().sorted(Comparator.comparing(ListSanPhamResponse::getNgayTao))
                    .collect(Collectors.toList());
            pageData.setSource(listDaLoc);
        }
        if (kieuLoc == 2){
            listDaLoc = listCanLoc.stream().sorted(Comparator.comparing(ListSanPhamResponse::getGiaGiam))
                    .collect(Collectors.toList());
            pageData.setSource(listDaLoc);
        }
        if (kieuLoc == 3){
            listDaLoc = listCanLoc.stream().sorted(Comparator.comparing(ListSanPhamResponse::getGiaGiam).reversed())
                    .collect(Collectors.toList());
            pageData.setSource(listDaLoc);
        }
        return pageData;
    }

    public PagedListHolder<ListSanPhamResponse> findByDanhMuc (int pageNumber, ListSanPhamSideBarFilterRequest request) {


            request.setChatLieuList(chatLieuDao.findAllIdDangBanNgungBan());


            request.setDeGiayList(deGiayDao.findAllIdDangBanNgungBan());


            request.setMauSacList(mauSacDao.findAllIdDangBanNgungBan());


            request.setThuongHieuList(thuongHieuDao.findAllIdDangBanNgungBan());


            request.setKichThuocList(kichThuocDao.findAllIdDangBanNgungBan());

        List<Integer> listIdSanPham = sanPhamChiTietDao.findDistinctIdSpForSideBarFilter(
                request.getStartPrice(),
                request.getEndPrice(),
                request.getChatLieuList(),
                request.getDeGiayList(),
                request.getMauSacList(),
                request.getTheLoaiList(),
                request.getThuongHieuList(),
                request.getKichThuocList());
        List<SanPhamChiTiet> sanPhamChiTietList = new ArrayList<>();
        listIdSanPham.stream().forEach(idsp -> {
            sanPhamChiTietDao.findDistinctIdMauSacSideBarFilter(
                    request.getStartPrice(),
                    request.getEndPrice(),
                    request.getChatLieuList(),
                    request.getDeGiayList(),
                    request.getMauSacList(),
                    request.getTheLoaiList(),
                    request.getThuongHieuList(),
                    request.getKichThuocList(),
                    idsp
            ).stream().forEach(idMauSac -> {
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
        PagedListHolder<ListSanPhamResponse> pageData = new PagedListHolder<>(listData);
        pageData.setPage(pageNumber);
        pageData.setPageSize(9);
        List<ListSanPhamResponse> listCanLoc = pageData.getSource();
        List<ListSanPhamResponse> listDaLoc = new ArrayList<>();

            listDaLoc = listCanLoc.stream().sorted(Comparator.comparing(ListSanPhamResponse::getNgayTao).reversed())
                    .collect(Collectors.toList());
            pageData.setSource(listDaLoc);

        return pageData;
    }

//    public PagedListHolder<ListSanPhamResponse> headFilter (int kieuLoc, PagedListHolder<ListSanPhamResponse> pageData) {
//        List<ListSanPhamResponse> listCanLoc = pageData.getSource();
//        List<ListSanPhamResponse> listDaLoc = new ArrayList<>();
//        if (kieuLoc == 0){
//            listDaLoc = listCanLoc.stream().sorted(Comparator.comparing(ListSanPhamResponse::getNgayTao).reversed())
//                    .collect(Collectors.toList());
//            pageData.setSource(listDaLoc);
//        }
//        if (kieuLoc == 1){
//            listDaLoc = listCanLoc.stream().sorted(Comparator.comparing(ListSanPhamResponse::getNgayTao))
//                    .collect(Collectors.toList());
//            pageData.setSource(listDaLoc);
//        }
//        if (kieuLoc == 2){
//            listDaLoc = listCanLoc.stream().sorted(Comparator.comparing(ListSanPhamResponse::getGiaGiam))
//                    .collect(Collectors.toList());
//            pageData.setSource(listDaLoc);
//        }
//        if (kieuLoc == 3){
//            listDaLoc = listCanLoc.stream().sorted(Comparator.comparing(ListSanPhamResponse::getGiaGiam).reversed())
//                    .collect(Collectors.toList());
//            pageData.setSource(listDaLoc);
//        }
//        return pageData;
//    }

    public PagedListHolder<ListSanPhamResponse> headFilter (int kieuLoc, ListSanPhamSideBarFilterRequest sideBarFilterRequest, KhachHangSanPhamSearchRequest searchRequest) {
        if (searchRequest.getTen() == null) {
            return sideBarFilter(0,sideBarFilterRequest,kieuLoc);
        }else if (searchRequest.getTen() != null){
            return search(0,searchRequest,kieuLoc);
        }else {
            return null;
        }
    }

    public PagedListHolder<ListSanPhamResponse> search (int pageNumber, KhachHangSanPhamSearchRequest request, int kieuLoc) {
        List<Integer> listIdSanPham = sanPhamChiTietDao.findDistinctIdMauSacSearch(request.getTen());
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
        PagedListHolder<ListSanPhamResponse> pageData = new PagedListHolder<>(listData);
        pageData.setPage(pageNumber);
        pageData.setPageSize(9);
        List<ListSanPhamResponse> listCanLoc = pageData.getSource();
        List<ListSanPhamResponse> listDaLoc = new ArrayList<>();
        if (kieuLoc == 0){
            listDaLoc = listCanLoc.stream().sorted(Comparator.comparing(ListSanPhamResponse::getNgayTao).reversed())
                    .collect(Collectors.toList());
            pageData.setSource(listDaLoc);
        }
        if (kieuLoc == 1){
            listDaLoc = listCanLoc.stream().sorted(Comparator.comparing(ListSanPhamResponse::getNgayTao))
                    .collect(Collectors.toList());
            pageData.setSource(listDaLoc);
        }
        if (kieuLoc == 2){
            listDaLoc = listCanLoc.stream().sorted(Comparator.comparing(ListSanPhamResponse::getGiaGiam))
                    .collect(Collectors.toList());
            pageData.setSource(listDaLoc);
        }
        if (kieuLoc == 3){
            listDaLoc = listCanLoc.stream().sorted(Comparator.comparing(ListSanPhamResponse::getGiaGiam).reversed())
                    .collect(Collectors.toList());
            pageData.setSource(listDaLoc);
        }
        return pageData;
    }

    public int getSoLuongSpTrongGh (GioHang gioHang) {
        return gioHangChiTietDao.fingGhctByGioHangAndTrangThaiSpct(gioHang,1).size();
    }

    public GioHang findGioHangByTaiKhoan (TaiKhoan taiKhoan) {
        return gioHangDao.findByTaiKhoanByIdTaiKhoan(taiKhoan).orElse(null);
    }

    public List<ChatLieu> findAllChatLieu () {
        return chatLieuDao.findAllByTrangThaiOrTrangThai(1,2);
    }

    public List<DeGiay> findAllDeGiay () {
        return deGiayDao.findAllByTrangThaiOrTrangThai(1,2);
    }

    public List<KichThuoc> findAllKichThuoc () {
        return kichThuocDao.findAllByTrangThaiOrTrangThai(1,2);
    }

    public List<MauSac> findAllMauSac () {
        return mauSacDao.findAllByTrangThaiOrTrangThai(1,2);
    }

    public List<TheLoai> findAllDanhMuc () {
        return theLoaiDao.findAllByTrangThaiOrTrangThai(1,2);
    }

    public List<ThuongHieu> findAllThuongHieu() {
        return thuongHieuDao.findAllByTrangThaiOrTrangThai(1,2);
    }
}
