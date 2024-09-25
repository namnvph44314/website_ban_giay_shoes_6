package com.group6.du_an_tot_nghiep.Service.QuanLy;

import com.group6.du_an_tot_nghiep.Dao.*;
import com.group6.du_an_tot_nghiep.Dto.QuanLy.SanPham.SanPhamRequest;
import com.group6.du_an_tot_nghiep.Dto.QuanLy.SanPham.SanPhamResponse;
import com.group6.du_an_tot_nghiep.Dto.QuanLy.SanPham.SanPhamSave;
import com.group6.du_an_tot_nghiep.Dto.QuanLy.SanPham.SanPhamSaveRequest;
import com.group6.du_an_tot_nghiep.Dto.QuanLy.SanPhamChiTiet.SanPhamChiTietCustom;
import com.group6.du_an_tot_nghiep.Entities.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SanPhamService {
    @Autowired
    SanPhamDao sanPhamDao;
    @Autowired
    SanPhamChiTietDao sanPhamChiTietDao;
    @Autowired
    HinhAnhDao hinhAnhDao;

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
    HttpSession session;

    public boolean updateTenSp (int idSp, String tenSp) {
        SanPham sanPham = sanPhamDao.findByTenSanPham(tenSp);
        if (sanPham == null){
            SanPham sanPham1 = sanPhamDao.findById(idSp).get();
            sanPham1.setTenSanPham(tenSp);
            sanPhamDao.save(sanPham1);
            return true;
        }else{
            return false;
        }
    }


    public Boolean doiTrangThaiSanPham (List<Integer> listId) {
        for (Integer id: listId
             ) {
            SanPham sanPham = sanPhamDao.findById(id).orElse(null);
            if (sanPham == null) {
                return false;
            }else {
                if (sanPham.getTrangThai() == SanPhamDao.DANG_BAN) {
                    sanPham.setTrangThai(SanPhamDao.NGUNG_BAN);
                    sanPhamDao.save(sanPham);
                    for (SanPhamChiTiet spct: sanPhamChiTietDao.findAllBySanPhamByIdSanPham(sanPham)
                    ) {
                        spct.setTrangThai(SanPhamChiTietDao.NGUNG_BAN);
                        sanPhamChiTietDao.save(spct);
                    }
                }else{
                    sanPham.setTrangThai(SanPhamDao.DANG_BAN);
                    sanPhamDao.save(sanPham);
                    for (SanPhamChiTiet spct: sanPhamChiTietDao.findAllBySanPhamByIdSanPham(sanPham)
                    ) {
                        spct.setTrangThai(SanPhamChiTietDao.DANG_BAN);
                        sanPhamChiTietDao.save(spct);
                    }
                }
            }
        }
        return true;
    }

    public String findUrlAnhMacDinh (int idSpct) {
        return hinhAnhDao.findUrlAnhMacDinh(idSpct);
    }



    public Page<SanPhamResponse> findAllSanPham (int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber,5);
        Page<SanPham> pageData = sanPhamDao.findAllByTrangThaiOrTrangThai(SanPhamDao.DANG_BAN,SanPhamDao.NGUNG_BAN,pageable);
        Page<SanPhamResponse> pageDataResponse = pageData.map(SanPhamResponse::convertToResponse);
        for (SanPhamResponse response: pageDataResponse.getContent()
             ) {
                response.setSoLuong(sanPhamChiTietDao.getSoLuongByIdSp(response.getId()).orElse(0));
        }
        return pageDataResponse;
    }

    public Page<SanPhamResponse> findByTenAndTrangThai (String ten, int trangThai) {
        List<Integer> lstTrangThai = new ArrayList<>();
        if (trangThai == -1) {
            lstTrangThai.add(1);
            lstTrangThai.add(0);
        }else {
            lstTrangThai.add(trangThai);
        }
        Pageable pageable = PageRequest.of(0,5);
        Page<SanPham> pageData = sanPhamDao.findByTenAndTrangThai(ten,lstTrangThai,pageable);
        if (pageData.getTotalElements() > 0) {
            Page<SanPhamResponse> pageDataResponse = pageData.map(SanPhamResponse::convertToResponse);
            for (SanPhamResponse response: pageDataResponse.getContent()
            ) {
                response.setSoLuong(sanPhamChiTietDao.getSoLuongByIdSp(response.getId()).orElse(0));
            }
            return pageDataResponse;
        }else {
            return null;
        }
    }


    public boolean saveAll(SanPhamSave sanPhamSave){
        SanPham sanPham = new SanPham();
        sanPham.setTenSanPham(sanPhamSave.getListSP().get(0).getTen());
        sanPham.setNgayTao(new Timestamp(System.currentTimeMillis()));
        sanPham.setNguoiTao("DBA");
        sanPham.setTrangThai(sanPhamDao.DANG_BAN);
        SanPham sanPhamSaved = sanPhamDao.saveAndFlush(sanPham);
        for (int i = 0; i < sanPhamSave.getListSP().size(); i++) {
            SanPhamRequest sanPhamRequest = sanPhamSave.getListSP().get(i);
            SanPhamChiTiet sanPhamChiTiet = new SanPhamChiTiet();
            sanPhamChiTiet.setSanPhamByIdSanPham(sanPhamSaved);
            sanPhamChiTiet.setChatLieuByIdChatLieu(chatLieuDao.findById(sanPhamRequest.getChatLieu()).orElse(null));
            sanPhamChiTiet.setDeGiayByIdDeGiay(deGiayDao.findById(sanPhamRequest.getDeGiay()).orElse(null));
            sanPhamChiTiet.setMauSacByIdMauSac(mauSacDao.findById(sanPhamRequest.getIdMauSac()).orElse(null));
            sanPhamChiTiet.setKichThuocByIdKichCo(kichThuocDao.findById(sanPhamRequest.getIdKichCo()).orElse(null));
            sanPhamChiTiet.setTheLoaiByIdDanhMuc(theLoaiDao.findById(sanPhamRequest.getDanhMuc()).orElse(null));
            sanPhamChiTiet.setThuongHieuByIdThuongHieu(thuongHieuDao.findById(sanPhamRequest.getThuongHieu()).orElse(null));
            sanPhamChiTiet.setCanNang(sanPhamRequest.getCanNang());
            sanPhamChiTiet.setSoLuong(sanPhamRequest.getSoLuong());
            sanPhamChiTiet.setMoTa(sanPhamRequest.getMoTa());
            sanPhamChiTiet.setNgayTao(new Timestamp(System.currentTimeMillis()));
            sanPhamChiTiet.setNguoiTao("DBA");
            sanPhamChiTiet.setGia(sanPhamRequest.getGia());
            int maSo;
            try {
                maSo  = Integer.parseInt(sanPhamChiTietDao.getLastestMaSo().substring(4)) + 1;
            }catch (Exception e){
                maSo=1;
            }
            sanPhamChiTiet.setMa("SPCT" + maSo);
            sanPhamChiTiet.setTrangThai(sanPhamChiTietDao.DANG_BAN);
            SanPhamChiTiet sanPhamChiTietSaved = sanPhamChiTietDao.save(sanPhamChiTiet);
            if (sanPhamRequest.getImages()!=null) {
                for (int j = 0; j < sanPhamRequest.getImages().size(); j++) {
                    HinhAnh hinhAnh = new HinhAnh();
                    int imageId;
                    try {
                        imageId = hinhAnhDao.getLastestid()+1;
                    }catch (Exception e){
                        imageId=1;
                    }
                    hinhAnh.setId(imageId);
                    if (j == 0) {
                        hinhAnh.setAnhMacDinh(true);
                    }else {
                        hinhAnh.setAnhMacDinh(false);
                    }
                    hinhAnh.setSanPhamChiTietByIdSpct(sanPhamChiTietSaved);
                    hinhAnh.setUrl(sanPhamRequest.getImages().get(j));
                    hinhAnh.setNgayTao(new Timestamp(System.currentTimeMillis()));
                    hinhAnh.setNguoiTao("DBA");
                    hinhAnh.setTrangThai(hinhAnhDao.ACTIVE);
                    hinhAnhDao.save(hinhAnh);
                }
            }
        }
        return true;
    }

    public Page<SanPham> getPageSpByTen(String tenSp, int page){
        Pageable p=PageRequest.of(page,5);
        Page<SanPham> pageData=sanPhamDao.getPageSpByTen(tenSp,p);
        return pageData;
    }

    public Boolean saveDetailProduct(SanPhamSaveRequest request){
        TaiKhoan taiKhoan = (TaiKhoan) session.getAttribute("username");
        SanPham sanPham = new SanPham();
        sanPham.setNguoiTao(taiKhoan.getEmail());
        sanPham.setNgayTao(new Timestamp(System.currentTimeMillis()));
        sanPham.setTrangThai(1);
        sanPham.setTenSanPham(request.getSanPham().getTenSanPham());
        SanPham sanPhamSave = sanPhamDao.save(sanPham);

        for (SanPhamChiTietCustom spct : request.getSanPhamList()){
            SanPhamChiTiet sanPhamChiTiet = new SanPhamChiTiet();
            sanPhamChiTiet.setSanPhamByIdSanPham(sanPhamSave);
            sanPhamChiTiet.setNguoiTao(taiKhoan.getEmail());
            sanPhamChiTiet.setMoTa(spct.getMoTa());
            sanPhamChiTiet.setNgayTao(new Timestamp(System.currentTimeMillis()));
            sanPhamChiTiet.setGia(spct.getGia());
            sanPhamChiTiet.setKichThuocByIdKichCo(spct.getKichCo());
            sanPhamChiTiet.setMauSacByIdMauSac(spct.getMauSac());
            sanPhamChiTiet.setDeGiayByIdDeGiay(deGiayDao.findById(request.getSanPham().getIdDeGiay()).orElse(null));
            sanPhamChiTiet.setChatLieuByIdChatLieu(chatLieuDao.findById(request.getSanPham().getIdChatLieu()).orElse(null));
            sanPhamChiTiet.setThuongHieuByIdThuongHieu(thuongHieuDao.findById(request.getSanPham().getIdThuongHieu()).orElse(null));
            sanPhamChiTiet.setTheLoaiByIdDanhMuc(theLoaiDao.findById(request.getSanPham().getIdDanhMuc()).orElse(null));
            sanPhamChiTiet.setSoLuong(spct.getSoLuong());
            sanPhamChiTiet.setCanNang(spct.getCanNang());
            sanPhamChiTiet.setTrangThai(SanPhamDao.DANG_BAN);
            int maSo;
            try {
                maSo  = Integer.parseInt(sanPhamChiTietDao.getLastestMaSo().substring(4)) + 1;
            }catch (Exception e){
                maSo=1;
            }
            sanPhamChiTiet.setMa("SPCT" + maSo);
            SanPhamChiTiet sanPhamChiTietSave = sanPhamChiTietDao.save(sanPhamChiTiet);
            int index = 0;
            if (spct.getImages().size() > 0){
                for (String image : spct.getImages()){
                    HinhAnh hinhAnh = new HinhAnh();
                    if (index == 0) {
                        hinhAnh.setAnhMacDinh(true);
                    }else {
                        hinhAnh.setAnhMacDinh(false);
                    }
                    hinhAnh.setTrangThai(1);
                    hinhAnh.setNgayTao(new Timestamp(System.currentTimeMillis()));
                    hinhAnh.setUrl(image);
                    hinhAnh.setSanPhamChiTietByIdSpct(sanPhamChiTietSave);
                    hinhAnh.setNguoiTao(taiKhoan.getEmail());
                    hinhAnhDao.save(hinhAnh);
                    index ++;
                }
            }
        }
        return true;
    }

    public Boolean checkProductNameMatch(String productName){
        SanPham isMatch = sanPhamDao.findByTenSanPham(productName);
        if(isMatch != null){
            return true;
        }else{
            return false;
        }
    }
}
