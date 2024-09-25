package com.group6.du_an_tot_nghiep.Service.QuanLy;

import com.group6.du_an_tot_nghiep.Dao.GiamGiaSanPhamChiTietDao;
import com.group6.du_an_tot_nghiep.Dao.GiamGiaSanPhamDao;
import com.group6.du_an_tot_nghiep.Dao.SanPhamChiTietDao;
import com.group6.du_an_tot_nghiep.Dto.QuanLy.GiamGiaSanPham.GiamGiaSanPhamUpdate;
import com.group6.du_an_tot_nghiep.Dto.QuanLy.PhieuGiamGia.PhieuGiamGiaUpdate;
import com.group6.du_an_tot_nghiep.Entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class GiamGiaSanPhamChiTietService {
    @Autowired
    GiamGiaSanPhamChiTietDao giamGiaSanPhamChiTietDao;
    @Autowired
    SanPhamChiTietDao sanPhamChiTietDao;
    @Autowired
    GiamGiaSanPhamDao giamGiaSanPhamDao;

    public String add(List<Integer> lstIdSpct,int idGgsp,String nguoiTao){

        Instant now = Instant.now();
        Timestamp ngayTao = Timestamp.from(now);
        for(int i=0;i<lstIdSpct.size();i++){
            GiamGiaSanPhamChiTiet giamGiaSanPhamChiTiet=new GiamGiaSanPhamChiTiet();

            SanPhamChiTiet sanPhamChiTiet=new SanPhamChiTiet();
            sanPhamChiTiet.setId(lstIdSpct.get(i));
            giamGiaSanPhamChiTiet.setSanPhamChiTietByIdSpct(sanPhamChiTiet);

            GiamGiaSanPham giamGiaSanPham= giamGiaSanPhamDao.findById(idGgsp).get();
            giamGiaSanPhamChiTiet.setIdGiamGia(giamGiaSanPham);

            giamGiaSanPhamChiTiet.setNgayTao(ngayTao);
            giamGiaSanPhamChiTiet.setNguoiTao(nguoiTao);
            giamGiaSanPhamChiTiet.setNguoiSua(null);
            giamGiaSanPhamChiTiet.setNguoiSua(null);

            Integer trangThai=null;
            System.out.println(giamGiaSanPham.getTrangThai()+"///////////////////");
            if(giamGiaSanPham.getTrangThai()==1){
                GiamGiaSanPhamChiTiet ggspct=giamGiaSanPhamChiTietDao.updateTtGgspct(lstIdSpct.get(i),GiamGiaSanPhamChiTietDao.DANG_HOAT_DONG);

                if(ggspct==null){
                    System.out.println("vao ggspct=null");
                    trangThai=giamGiaSanPhamChiTietDao.DANG_HOAT_DONG;
                }else{
                    if(ggspct.getIdGiamGia().getNgayBatDau().before(giamGiaSanPham.getNgayBatDau()) || ggspct.getIdGiamGia().getNgayBatDau().equals(giamGiaSanPham.getNgayBatDau())){
                        giamGiaSanPhamChiTietDao.updateTt(giamGiaSanPhamChiTietDao.NGUNG_HOAT_DONG,ggspct.getId());
                        trangThai=giamGiaSanPhamChiTietDao.DANG_HOAT_DONG;
                    }else{
                        trangThai=giamGiaSanPhamChiTietDao.NGUNG_HOAT_DONG;
                    }
                }
            }else{
                System.out.println("vao ggspct != null");
                trangThai=giamGiaSanPhamChiTietDao.NGUNG_HOAT_DONG;
            }
            giamGiaSanPhamChiTiet.setTrangThai(trangThai);

            BigDecimal giaGiam= sanPhamChiTietDao.findById(lstIdSpct.get(i)).get().getGia().multiply(new BigDecimal(giamGiaSanPham.getGiaTri())).divide(new BigDecimal(100));
            giamGiaSanPhamChiTiet.setGiaGiam(giaGiam);

            giamGiaSanPhamChiTietDao.save(giamGiaSanPhamChiTiet);
        }
        return "Thêm thành công";
    }

    public String updateSpctdaCo(int idGgsp,String nguoiTao){
        GiamGiaSanPham giamGiaSanPham=giamGiaSanPhamDao.findById(idGgsp).get();
        List<Integer> lstSpctDaCo=new ArrayList<>();
        lstSpctDaCo=giamGiaSanPhamChiTietDao.listIdSpctDaCo(idGgsp);
        System.out.println("lst: "+lstSpctDaCo);
        Integer trangThai=null;
        if(giamGiaSanPham.getTrangThai()!=giamGiaSanPhamDao.DANG_DIEN_RA){
            giamGiaSanPhamChiTietDao.updateTtHetHanChoDoiTtNhanh(idGgsp);
        }else{
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

        return "Update thành công";
    }

    public String update(List<Integer> lstIdSpct,int idGgsp,String nguoiTao){

        Instant now = Instant.now();
        Timestamp ngayTao = Timestamp.from(now);
        for(int i=0;i<lstIdSpct.size();i++){
            GiamGiaSanPhamChiTiet giamGiaSanPhamChiTiet=new GiamGiaSanPhamChiTiet();

            SanPhamChiTiet sanPhamChiTiet=new SanPhamChiTiet();
            sanPhamChiTiet.setId(lstIdSpct.get(i));
            giamGiaSanPhamChiTiet.setSanPhamChiTietByIdSpct(sanPhamChiTiet);

            GiamGiaSanPham giamGiaSanPham= giamGiaSanPhamDao.findById(idGgsp).get();
            giamGiaSanPhamChiTiet.setIdGiamGia(giamGiaSanPham);

            giamGiaSanPhamChiTiet.setNgayTao(ngayTao);
            giamGiaSanPhamChiTiet.setNguoiTao(nguoiTao);
            giamGiaSanPhamChiTiet.setNguoiSua(null);
            giamGiaSanPhamChiTiet.setNguoiSua(null);

            Integer trangThai=null;
            System.out.println(giamGiaSanPham.getTrangThai()+"///////////////////");
            if(giamGiaSanPham.getTrangThai()==1){
                GiamGiaSanPhamChiTiet ggspct=giamGiaSanPhamChiTietDao.updateTtGgspct(lstIdSpct.get(i),GiamGiaSanPhamChiTietDao.DANG_HOAT_DONG);

                if(ggspct==null){
                    System.out.println("vao ggspct=null");
                    trangThai=giamGiaSanPhamChiTietDao.DANG_HOAT_DONG;
                }else{
                    if(ggspct.getIdGiamGia().getNgayBatDau().before(giamGiaSanPham.getNgayBatDau()) || ggspct.getIdGiamGia().getNgayBatDau().equals(giamGiaSanPham.getNgayBatDau())){
                        System.out.println("vao day khong");
                        giamGiaSanPhamChiTietDao.updateTt(giamGiaSanPhamChiTietDao.NGUNG_HOAT_DONG,ggspct.getId());
                        trangThai=giamGiaSanPhamChiTietDao.DANG_HOAT_DONG;
                    }else{
                        trangThai=giamGiaSanPhamChiTietDao.NGUNG_HOAT_DONG;
                    }
                }
            }else{
                System.out.println("vao ggspct != null");
                trangThai=giamGiaSanPhamChiTietDao.NGUNG_HOAT_DONG;
            }
            giamGiaSanPhamChiTiet.setTrangThai(trangThai);

            BigDecimal giaGiam= sanPhamChiTietDao.findById(lstIdSpct.get(i)).get().getGia().multiply(new BigDecimal(giamGiaSanPham.getGiaTri())).divide(new BigDecimal(100));
            giamGiaSanPhamChiTiet.setGiaGiam(giaGiam);

            giamGiaSanPhamChiTietDao.save(giamGiaSanPhamChiTiet);
        }
        return "Thêm thành công";
    }

    public Page<GiamGiaSanPhamChiTiet> getListGgspctByIdGgsp(int idGgsp, int page){
        Pageable p= PageRequest.of(page,10);
        Page<GiamGiaSanPhamChiTiet> pageData=giamGiaSanPhamChiTietDao.getListGgspctByIdGgsp(idGgsp,p);
        return pageData;
    }

    public Page<SanPhamChiTiet> getListSpctByIdGgspChuaAdd(List<Integer> ids,int idGg,int page){
        Pageable p=PageRequest.of(page,5);
        Page<SanPhamChiTiet> pageData=giamGiaSanPhamChiTietDao.getListSpctByIdGgspChuaAdd(ids,idGg,p);
        return pageData;
    }

    public Page<SanPhamChiTiet> loc(int idGgsp,List<Integer> ids,Integer mauSac,Integer chatLieu,Integer kichCo,Integer deGiay, Integer thuongHieu, int page){
        Pageable p=PageRequest.of(page,5);
        Page<SanPhamChiTiet> pageData=giamGiaSanPhamChiTietDao.loc(idGgsp,ids,mauSac,chatLieu,kichCo,deGiay,thuongHieu,p);
        return pageData;
    }


    public void updateTtggspct(int idggspct, int idGgsp){
        GiamGiaSanPham giamGiaSanPham=giamGiaSanPhamDao.findById(idGgsp).get();
        if(giamGiaSanPham.getTrangThai()!=giamGiaSanPhamDao.DANG_DIEN_RA){
            giamGiaSanPhamChiTietDao.updateTtHetHanChoDoiTtNhanh(idGgsp);
        }else{
            GiamGiaSanPhamChiTiet giamGiaSanPhamChiTiet=giamGiaSanPhamChiTietDao.findById(idggspct).get();
            int idSpct=giamGiaSanPhamChiTiet.getSanPhamChiTietByIdSpct().getId();
            System.out.println(idSpct+" idSpct");
            GiamGiaSanPhamChiTiet ggspct=giamGiaSanPhamChiTietDao.updateTtGgspctDaCo(idSpct,GiamGiaSanPhamChiTietDao.DANG_HOAT_DONG,idGgsp);
            System.out.println("ggspct: "+ggspct);
            if(giamGiaSanPham.getTrangThai()!=1){
                giamGiaSanPhamChiTiet.setTrangThai(0);
            }else {
                if(giamGiaSanPhamChiTiet.getTrangThai()==1){
                    giamGiaSanPhamChiTiet.setTrangThai(0);
                }else{
                    if(ggspct==null){
                        System.out.println("vao ggspct=null");
                        giamGiaSanPhamChiTiet.setTrangThai(giamGiaSanPhamChiTietDao.DANG_HOAT_DONG);
                    }else{
                        if(ggspct.getIdGiamGia().getNgayBatDau().before(giamGiaSanPham.getNgayBatDau()) || ggspct.getIdGiamGia().getNgayBatDau().equals(giamGiaSanPham.getNgayBatDau())){
                            giamGiaSanPhamChiTietDao.updateTt(giamGiaSanPhamChiTietDao.NGUNG_HOAT_DONG,ggspct.getId());
                            System.out.println("vao day khong");
                            giamGiaSanPhamChiTiet.setTrangThai(giamGiaSanPhamChiTietDao.DANG_HOAT_DONG);
                        }else{
                            giamGiaSanPhamChiTiet.setTrangThai(giamGiaSanPhamChiTietDao.NGUNG_HOAT_DONG);
                        }
                    }
                }
            }

            giamGiaSanPhamChiTietDao.save(giamGiaSanPhamChiTiet);
        }

    }

}
