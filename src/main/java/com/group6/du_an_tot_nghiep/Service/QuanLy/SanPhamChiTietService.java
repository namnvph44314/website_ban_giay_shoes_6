package com.group6.du_an_tot_nghiep.Service.QuanLy;

import com.group6.du_an_tot_nghiep.Dao.*;
import com.group6.du_an_tot_nghiep.Dto.QuanLy.SanPhamChiTiet.SanPhamChiTietFilterRequest;
import com.group6.du_an_tot_nghiep.Dto.QuanLy.SanPhamChiTiet.SanPhamChiTietUpdateRequest;
import com.group6.du_an_tot_nghiep.Entities.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class SanPhamChiTietService {
    @Autowired
    SanPhamChiTietDao sanPhamChiTietDao;

    @Autowired
    SanPhamDao sanPhamDao;

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
    private HinhAnhDao hinhAnhDao;

    public Page<SanPhamChiTiet> findAllSanPhamChiTiet(int id, int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, 5);
        Page<SanPhamChiTiet> pageData = sanPhamChiTietDao.findAllByIdSanPham(id, pageable);
        if (pageData.getContent().isEmpty()) {
            return null;
        } else {
            return pageData;
        }

    }

    public Page<SanPhamChiTiet> findSanPhamChiTietByMa(String ma) {
        Pageable pageable = PageRequest.of(0, 5);
        Page<SanPhamChiTiet> pageData = sanPhamChiTietDao.findByIdSanPhamChiTiet(ma, pageable);
        if (pageData.getContent().isEmpty()) {
            return null;
        } else {
            return pageData;
        }
    }

    public Integer delete(List<Integer> listId) {
        for (Integer id : listId
        ) {
            SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietDao.findById(id).orElse(null);
            if (sanPhamChiTiet == null) {
                return 0;
            } else {
                if (sanPhamChiTiet.getTrangThai() == SanPhamDao.DANG_BAN) {
                    sanPhamChiTiet.setTrangThai(SanPhamDao.NGUNG_BAN);
                    sanPhamChiTietDao.save(sanPhamChiTiet);
                }else{
                    sanPhamChiTiet.setTrangThai(SanPhamDao.DANG_BAN);
                    sanPhamChiTietDao.save(sanPhamChiTiet);
                }
            }
        }
        return 1;
    }

    public Page<SanPhamChiTiet> filter(SanPhamChiTietFilterRequest filterRequest, int idSp, int pageNumber) {
        BigDecimal start = filterRequest.getStartPrice();
        BigDecimal end = filterRequest.getEndPrice();
        List<Integer> idChatLieu = new ArrayList<>();
        List<Integer> idDeGiay = new ArrayList<>();
        List<Integer> idMauSac = new ArrayList<>();
        List<Integer> idTheLoai = new ArrayList<>();
        List<Integer> idThuongHieu = new ArrayList<>();
        List<Integer> idKichCo = new ArrayList<>();
        List<Integer> trangThai = new ArrayList<>();
        if (filterRequest.getKichCo() == -1) {
            idKichCo = kichThuocDao.findAllIdDangBanNgungBan();
        }else {
            idKichCo.add(filterRequest.getKichCo());
        }
        if (filterRequest.getChatLieu() == -1) {
            idChatLieu = chatLieuDao.findAllIdDangBanNgungBan();
        }else {
            idChatLieu.add(filterRequest.getChatLieu());
        }
        if (filterRequest.getDeGiay() == -1) {
            idDeGiay = deGiayDao.findAllIdDangBanNgungBan();
        }else {
            idDeGiay.add(filterRequest.getDeGiay());
        }
        if (filterRequest.getMauSac() == -1) {
            idMauSac = mauSacDao.findAllIdDangBanNgungBan();
        }else {
            idMauSac.add(filterRequest.getMauSac());
        }
        if (filterRequest.getDanhMuc() == -1) {
            idTheLoai = theLoaiDao.findAllIdDangBanNgungBan();
        }else {
            idTheLoai.add(filterRequest.getDanhMuc());
        }
        if (filterRequest.getThuongHieu() == -1) {
            idThuongHieu = thuongHieuDao.findAllIdDangBanNgungBan();
        }else {
            idThuongHieu.add(filterRequest.getThuongHieu());
        }
        if (filterRequest.getTrangThai() == -1) {
            trangThai.add(0);
            trangThai.add(1);
        }else {
            trangThai.add(filterRequest.getTrangThai());
        }
        Pageable pageable = PageRequest.of(pageNumber,5);
        Page<SanPhamChiTiet> pageData = sanPhamChiTietDao.filter(start,end,idChatLieu,idDeGiay,idMauSac,idTheLoai,idThuongHieu,trangThai,idSp,idKichCo,pageable);
        if (pageData.getContent().isEmpty()) {
            return null;
        }else {
            return pageData;
        }

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

    public Page<SanPhamChiTiet> getPageSpctByIdSp(List<Integer> ids, int page){
        Pageable p=PageRequest.of(page,5);
        Page<SanPhamChiTiet> pageData=sanPhamChiTietDao.getPageSpctByIdSp(ids,p);
        return pageData;
    }

    public Page<SanPhamChiTiet> loc(List<Integer> ids,Integer mauSac,Integer chatLieu,Integer kichCo,Integer deGiay, Integer thuongHieu, int page){
        Pageable p=PageRequest.of(page,5);
        Page<SanPhamChiTiet> pageData=sanPhamChiTietDao.loc(ids,mauSac,chatLieu,kichCo,deGiay,thuongHieu,p);
        return pageData;
    }

    public Page<SanPhamChiTiet> findAllByTrangThai(int trangThai, Pageable pageable) {
        try {
            Page<SanPhamChiTiet> sanPhamChiTietPage = sanPhamChiTietDao.findAllByTrangThai(trangThai, pageable);
            return sanPhamChiTietPage;
        } catch (Exception exception) {
            log.error("[ERROR] findAllByTrangThai {} " + exception.getMessage());
            return null;
        }
    }

    public SanPhamChiTiet findById(Integer productId) {
        return sanPhamChiTietDao.findById(productId).orElse(null);
    }


    public List<String> getListHinhAnh(Integer productId) {
        List<String> hinhAnhList = hinhAnhDao.getAllHinhAnhByIDSPCT(productId);
        return hinhAnhList;
    }

    public Integer updateDetailProduct(SanPhamChiTietUpdateRequest request){
        if (sanPhamChiTietDao.findById(request.getId()).isEmpty()){
            return 0;
        }

        Integer isUpdated = sanPhamChiTietDao.updateDetailProduct(request.getId(), request.getChatLieu(), request.getDeGiay(), request.getKichCo(), request.getMauSac(), request.getDanhMuc(), request.getThuongHieu(), request.getCanNang(), request.getSoLuong(), request.getDonGia(), request.getMoTa());
        return isUpdated;
    }

    public void updateHinhAnh(Integer productId, List<String> listHinhAnh){
        hinhAnhDao.deleteImage(productId);
        int count = 0;
        for (String e: listHinhAnh) {
            count += 1;
            HinhAnh hinhAnh = new HinhAnh();
            SanPhamChiTiet sanPhamChiTiet = new SanPhamChiTiet();
            sanPhamChiTiet.setId(productId);
            hinhAnh.setSanPhamChiTietByIdSpct(sanPhamChiTiet);
            hinhAnh.setUrl(e);
            hinhAnh.setNguoiTao("DBA");
            hinhAnh.setNgayTao(new Timestamp(System.currentTimeMillis()));
            if (count == 1) {
                hinhAnh.setAnhMacDinh(true);
            } else {
                hinhAnh.setAnhMacDinh(false);
            }
            hinhAnh.setTrangThai(1);
            hinhAnhDao.save(hinhAnh);
        }
    }

    public Page<SanPhamChiTiet> findSPCTByName(String name, Optional<Integer> page){
        Page<SanPhamChiTiet> pages;
        Pageable pageProductList = PageRequest.of(page.orElse(0), 6);
        if (name.equals("") || name == null){
            pages = findAllByTrangThai(1, pageProductList);
        }else{
            pages = sanPhamChiTietDao.findByName(name, pageProductList);
        }
        return pages;
    }
}
