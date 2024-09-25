package com.group6.du_an_tot_nghiep.Service.QuanLy;

import com.group6.du_an_tot_nghiep.Dao.GiamGiaSanPhamChiTietDao;
import com.group6.du_an_tot_nghiep.Dao.GiamGiaSanPhamDao;
import com.group6.du_an_tot_nghiep.Dao.SanPhamDao;
import com.group6.du_an_tot_nghiep.Dto.QuanLy.GiamGiaSanPham.GiamGiaSanPhamFilter;
import com.group6.du_an_tot_nghiep.Dto.QuanLy.GiamGiaSanPham.GiamGiaSanPhamRequest;
import com.group6.du_an_tot_nghiep.Dto.QuanLy.GiamGiaSanPham.GiamGiaSanPhamUpdate;
import com.group6.du_an_tot_nghiep.Dto.QuanLy.PhieuGiamGia.PhieuGiamGiaFilter;
import com.group6.du_an_tot_nghiep.Entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class GiamGiaSanPhamService {
    @Autowired
    GiamGiaSanPhamDao giamGiaSanPhamDao;
    @Autowired
    SanPhamDao sanPhamDao;
    @Autowired
    GiamGiaSanPhamChiTietDao giamGiaSanPhamChiTietDao;

    public Page<GiamGiaSanPham> getAllPage(int page) {
        Pageable p = PageRequest.of(page, 8);
        Page<GiamGiaSanPham> pageData = giamGiaSanPhamDao.getAllPage(p);
        return pageData;
    }

    public Page<GiamGiaSanPham> loc(GiamGiaSanPhamFilter giamGiaSanPhamFilter, int page) {
        Pageable p = PageRequest.of(page, 10);
        if (giamGiaSanPhamFilter.getNgayBatDau() != null && giamGiaSanPhamFilter.getNgayKetThuc() != null) {
            Date sqlDateNBD = Date.valueOf(giamGiaSanPhamFilter.getNgayBatDau());
            Date sqlDateNKT = Date.valueOf(giamGiaSanPhamFilter.getNgayKetThuc());
            Timestamp ngayBatDau = new Timestamp(sqlDateNBD.getTime());
            Timestamp ngayKetThuc = new Timestamp(sqlDateNKT.getTime());
            System.out.println(ngayBatDau);
            System.out.println(ngayKetThuc);
            Page<GiamGiaSanPham> pageData = giamGiaSanPhamDao.loc(ngayBatDau, ngayKetThuc, giamGiaSanPhamFilter.getTrangThai(), p);
            return pageData;
        }
        Page<GiamGiaSanPham> pageData = giamGiaSanPhamDao.loc(null, null, giamGiaSanPhamFilter.getTrangThai(), p);
        return pageData;
    }

    public GiamGiaSanPham search(String ten) {
        return giamGiaSanPhamDao.search(ten);
    }

    public Page<SanPham> findAllSpByTrangThai(int trangThai,int page){
        Pageable p=PageRequest.of(page,5);
        Page<SanPham> pageData=sanPhamDao.findAllByTrangThai(trangThai,p);
        return pageData;
    }

    public String add(GiamGiaSanPhamRequest giamGiaSanPhamRequest,String nguoiTao){

        Instant now = Instant.now();
        Timestamp ngayTao = Timestamp.from(now);

        ZonedDateTime zonedDateTimeNBD = giamGiaSanPhamRequest.getNgayBatDau().atZone(ZoneId.systemDefault());
        ZonedDateTime zonedDateTimeNKT = giamGiaSanPhamRequest.getNgayKetThuc().atZone(ZoneId.systemDefault());
        // Chuyển đổi ZonedDateTime sang Instant
        Instant instantNBD = zonedDateTimeNBD.toInstant();
        Instant instantNKT = zonedDateTimeNKT.toInstant();
        // Chuyển đổi Instant sang Timestamp
        Timestamp ngayBatDau = Timestamp.from(instantNBD);
        Timestamp ngayKetThuc = Timestamp.from(instantNKT);
        GiamGiaSanPham giamGiaSanPham=new GiamGiaSanPham();
        giamGiaSanPham.setTenGiamGia(giamGiaSanPhamRequest.getTen());
        giamGiaSanPham.setNgayBatDau(ngayBatDau);
        giamGiaSanPham.setNgayKetThuc(ngayKetThuc);
        giamGiaSanPham.setGiaTri(giamGiaSanPhamRequest.getGiaTri());
        giamGiaSanPham.setLoaiGiamGia(1);
        giamGiaSanPham.setNguoiTao(nguoiTao);
        giamGiaSanPham.setNgayTao(ngayTao);
        giamGiaSanPham.setNguoiSua(null);
        giamGiaSanPham.setNgaySua(null);

        Integer trangThai=null;
        if(ngayKetThuc.before(ngayTao)){
            trangThai=giamGiaSanPhamDao.HET_HAN;
        }
        if(ngayBatDau.after(ngayTao)){
            trangThai=giamGiaSanPhamDao.CHUA_DIEN_RA;
        }
        if(ngayBatDau.before(ngayTao) && ngayKetThuc.after(ngayTao)){
            trangThai=giamGiaSanPhamDao.DANG_DIEN_RA;
        }

        giamGiaSanPham.setTrangThai(trangThai);

        giamGiaSanPhamDao.save(giamGiaSanPham);
        return "Thêm thành công";
    }

    public GiamGiaSanPham findTopByOrderByIdDesc(){
        return giamGiaSanPhamDao.findTopByOrderByIdDesc();
    }

    public GiamGiaSanPhamUpdate findById(int id){
        GiamGiaSanPham giamGiaSanPham=giamGiaSanPhamDao.findById(id).get();
        GiamGiaSanPhamUpdate giamGiaSanPhamUpdate=new GiamGiaSanPhamUpdate();
        giamGiaSanPhamUpdate.setTen(giamGiaSanPham.getTenGiamGia());
        giamGiaSanPhamUpdate.setGiaTri(giamGiaSanPham.getGiaTri());
        giamGiaSanPhamUpdate.setNgayBatDau(giamGiaSanPham.getNgayBatDau()+"");
        giamGiaSanPhamUpdate.setNgayKetThuc(giamGiaSanPham.getNgayKetThuc()+"");
        return giamGiaSanPhamUpdate;
    }

    public String update(int idGg,GiamGiaSanPhamUpdate giamGiaSanPhamUpdate,String nguoiSua){
        GiamGiaSanPham giamGiaSanPham=giamGiaSanPhamDao.findById(idGg).get();

        Instant now = Instant.now();
        Timestamp ngaySua = Timestamp.from(now);

        // Lấy thời gian hiện tại dưới dạng LocalDateTime
        LocalDateTime localDateTime = LocalDateTime.now();

        // Định dạng datetime nhận được từ input
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

        // Chuyển đổi datetime từ chuỗi sang LocalDateTime
        LocalDateTime localDateTimeNBD = LocalDateTime.parse(giamGiaSanPhamUpdate.getNgayBatDau(), formatter);
        LocalDateTime localDateTimeNKT = LocalDateTime.parse(giamGiaSanPhamUpdate.getNgayKetThuc(), formatter);
        // Chuyển đổi LocalDateTime sang timestamp
        Timestamp ngayBatDau = Timestamp.valueOf(localDateTimeNBD);
        Timestamp ngayKetThuc = Timestamp.valueOf(localDateTimeNKT);

        giamGiaSanPham.setTenGiamGia(giamGiaSanPhamUpdate.getTen());
        giamGiaSanPham.setNgayBatDau(ngayBatDau);
        giamGiaSanPham.setNgayKetThuc(ngayKetThuc);
        giamGiaSanPham.setGiaTri(giamGiaSanPhamUpdate.getGiaTri());
        giamGiaSanPham.setLoaiGiamGia(1);

        giamGiaSanPham.setNguoiSua(nguoiSua);
        giamGiaSanPham.setNgaySua(ngaySua);

        Integer trangThai=null;
        if(ngayKetThuc.before(ngaySua)){
            trangThai=giamGiaSanPhamDao.HET_HAN;
        }
        if(ngayBatDau.after(ngaySua)){
            trangThai=giamGiaSanPhamDao.CHUA_DIEN_RA;
        }
        if(ngayBatDau.before(ngaySua) && ngayKetThuc.after(ngaySua)){
            trangThai=giamGiaSanPhamDao.DANG_DIEN_RA;
        }

        giamGiaSanPham.setTrangThai(trangThai);

        giamGiaSanPhamDao.save(giamGiaSanPham);
        return "Thêm thành công";
    }

    public void updateGgspNhd(int idPgg){
        Instant now = Instant.now();
        Timestamp ngayHienTai = Timestamp.from(now);
        GiamGiaSanPham giamGiaSanPham=giamGiaSanPhamDao.findById(idPgg).get();
        if(giamGiaSanPham.getTrangThai()!=0){
            giamGiaSanPham.setTrangThai((byte) giamGiaSanPhamDao.NGUNG_HOAT_DONG);
        }else{
            if (ngayHienTai.before(giamGiaSanPham.getNgayBatDau())) {
                giamGiaSanPham.setTrangThai((byte) giamGiaSanPhamDao.CHUA_DIEN_RA);
            } else if (giamGiaSanPham.getNgayKetThuc().before(ngayHienTai)) {
                giamGiaSanPham.setTrangThai((byte) giamGiaSanPhamDao.HET_HAN);
            } else {
                giamGiaSanPham.setTrangThai((byte) giamGiaSanPhamDao.DANG_DIEN_RA);
            }
        }

        updateTtGgspct(idPgg);

        giamGiaSanPhamDao.save(giamGiaSanPham);
    }

    public void updateTtGgspct(int idGgsp){
        GiamGiaSanPham giamGiaSanPham=giamGiaSanPhamDao.findById(idGgsp).get();
        if(giamGiaSanPham.getTrangThai()!=giamGiaSanPhamDao.DANG_DIEN_RA){
            giamGiaSanPhamChiTietDao.updateTtHetHanChoDoiTtNhanh(idGgsp);
        }else{
            List<Integer> lstSpctDaCo=new ArrayList<>();
            lstSpctDaCo=giamGiaSanPhamChiTietDao.listIdSpctDaCo(idGgsp);
            Integer trangThai=null;
            if(lstSpctDaCo.size()!=0){
                for(int i=0;i<lstSpctDaCo.size();i++){
                    GiamGiaSanPhamChiTiet ggspct=giamGiaSanPhamChiTietDao.updateTtGgspctDaCo(lstSpctDaCo.get(i),GiamGiaSanPhamChiTietDao.DANG_HOAT_DONG,idGgsp);
                    GiamGiaSanPhamChiTiet giamGiaSanPhamChiTiet=giamGiaSanPhamChiTietDao.getByIdSpctAndIdGgsp(lstSpctDaCo.get(i),idGgsp);
                    if(ggspct==null){
                        System.out.println("vao ggspct=null");
                        trangThai=giamGiaSanPhamChiTietDao.DANG_HOAT_DONG;
                    }else{
                        if(ggspct.getIdGiamGia().getNgayBatDau().before(giamGiaSanPham.getNgayBatDau()) || ggspct.getIdGiamGia().getNgayBatDau().equals(giamGiaSanPham.getNgayBatDau())){
                            giamGiaSanPhamChiTietDao.updateTt(giamGiaSanPhamChiTietDao.NGUNG_HOAT_DONG,ggspct.getId());
                            System.out.println("vao day khong");
                            trangThai=giamGiaSanPhamChiTietDao.DANG_HOAT_DONG;
                        }else{
                            trangThai=giamGiaSanPhamChiTietDao.NGUNG_HOAT_DONG;
                        }
                    }
                    giamGiaSanPhamChiTiet.setTrangThai(trangThai);
                    giamGiaSanPhamChiTietDao.save(giamGiaSanPhamChiTiet);
                }
            }
        }
    }
}
