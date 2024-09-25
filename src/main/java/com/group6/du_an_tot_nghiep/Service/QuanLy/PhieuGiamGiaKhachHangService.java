package com.group6.du_an_tot_nghiep.Service.QuanLy;

import com.group6.du_an_tot_nghiep.Dao.PhieuGiamGiaDao;
import com.group6.du_an_tot_nghiep.Dao.PhieuGiamGiaKhachHangDao;
import com.group6.du_an_tot_nghiep.Dao.TaiKhoanDao;
import com.group6.du_an_tot_nghiep.Dto.QuanLy.PhieuGiamGiaKhachHang.PhieuGiamGiaKhachHangAdd;
import com.group6.du_an_tot_nghiep.Entities.PhieuGiamGia;
import com.group6.du_an_tot_nghiep.Entities.PhieuGiamGiaKhachHang;
import com.group6.du_an_tot_nghiep.Entities.TaiKhoan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;

@Service
public class PhieuGiamGiaKhachHangService {
    @Autowired
    PhieuGiamGiaKhachHangDao phieuGiamGiaKhachHangDao;
    @Autowired
    PhieuGiamGiaDao phieuGiamGiaDao;
    @Autowired
    TaiKhoanDao taiKhoanDao;
    @Autowired
    EmailService emailService;

    /**
     Thêm phieu_giam_gia_khach_hang
     */
    public String addPggKh(List<Integer> lstPggKh, String nguoiTao, PhieuGiamGia phieuGiamGia){
        for(Integer pggKH: lstPggKh){
            System.out.println(lstPggKh+"//////////////////////////////////////////////////////////");
            PhieuGiamGiaKhachHang phieuGiamGiaKhachHang=new PhieuGiamGiaKhachHang();
            phieuGiamGiaKhachHang.setIdPhieuGiamGia(phieuGiamGia);
            phieuGiamGiaKhachHang.setId(null);
            //
            phieuGiamGiaKhachHang.setNguoiTao(nguoiTao);
            //Lấy ngày hiện tại
            Instant instant = Instant.now();
            Timestamp ngayTao=java.sql.Timestamp.from(instant);
            phieuGiamGiaKhachHang.setNgayTao(ngayTao);
            //
            phieuGiamGiaKhachHang.setNguoiSua(null);
            phieuGiamGiaKhachHang.setNgaySua(null);
            Optional<TaiKhoan> taiKhoan=taiKhoanDao.findById(pggKH);
            phieuGiamGiaKhachHang.setIdTaiKhoan(taiKhoan.get());
            phieuGiamGiaKhachHang.setTrangThai((byte) 0);
            phieuGiamGiaKhachHangDao.save(phieuGiamGiaKhachHang);
        }
        List<String> lstEmail=new ArrayList<>();
        for(Integer pggKh: lstPggKh){
            lstEmail.add(taiKhoanDao.findById(pggKh).get().getEmail());
        }
        emailService.sendBulkEmails(lstEmail,"Shop giày thể thao SHOES 6","Chúc mừng bạn đã nhận được 1 phiếu giảm giá của của hàng: "+phieuGiamGia.getTen());
        return "Thành công";
    }

    /**
     Lấy được phieuGiamGiaKhachHangAdd để lấy list pggKhAdd
     */
    public PhieuGiamGiaKhachHangAdd findAllPGGKHByIdPGG(int idPGG){
        List<PhieuGiamGiaKhachHang> lstPGG=phieuGiamGiaKhachHangDao.findAllPGGKHByIdPGG(idPGG);
        List<Integer> lstId=new ArrayList<>();
        PhieuGiamGiaKhachHangAdd phieuGiamGiaKhachHangAdd=new PhieuGiamGiaKhachHangAdd();
        for(PhieuGiamGiaKhachHang phieuGiamGiaKhachHang: lstPGG){
            lstId.add(phieuGiamGiaKhachHang.getIdTaiKhoan().getId());
        }
        phieuGiamGiaKhachHangAdd.setPggKh(lstId);
        return phieuGiamGiaKhachHangAdd;
    }

    /**
     Update pggKh khi thay đổi các khách hàng trong table
     */
    public List<Integer> lstUpdatePggKh(PhieuGiamGiaKhachHangAdd phieuGiamGiaKhachHangAdd,String maPgg,String nguoiTao){

        List<PhieuGiamGiaKhachHang> lstPggKh=phieuGiamGiaKhachHangDao.findAllPGGKHByMaPGG(maPgg);
        //list pggKh cũ
        List<Integer> lstIdKh=new ArrayList<>();
        for(PhieuGiamGiaKhachHang phieuGiamGiaKhachHang:lstPggKh){
            lstIdKh.add(phieuGiamGiaKhachHang.getIdTaiKhoan().getId());
        }
        //list pggKh mới
        List<Integer> lstKhNew=phieuGiamGiaKhachHangAdd.getPggKh();

        //TH1: Nếu lstIdKh không có phần tử nào trùng với lstKhNew -> delete PggKh với lstIdKh và insert lstKhNew
        if(listTrung(lstIdKh,lstKhNew)==null){
            for(int idKh: lstIdKh){
                delete(maPgg,idKh);
            }
            for(int idKh: lstKhNew){
                save(maPgg,idKh,nguoiTao);
            }
            System.out.println("TH1: "+lstKhNew);
            return lstKhNew;
        }

        //TH2: Nếu tất cả phần tử trong lstIdKh có trong lstKhNew -> Bỏ phần tử trùng nhau của 2 mảng và insert các phần tử còn lại của lstKhNew
        if(listTrung(lstIdKh,lstKhNew).size()==lstIdKh.size()){
            //list pggKhUpdate
            List<Integer> lstKhUpdate=mergeAndRemoveDuplicates(lstIdKh, lstKhNew);
            for(int idKh: lstKhUpdate){
                save(maPgg,idKh,nguoiTao);
            }
            System.out.println("TH2: "+lstKhUpdate);
            return lstKhUpdate;
        }

        //TH3
        if(listTrung(lstIdKh,lstKhNew)!=null && listTrung(lstIdKh,lstKhNew).size()<=lstKhNew.size()){
            //list pggKhUpdate
            List<Integer> lstKhMerge=mergeAndRemoveDuplicates(lstIdKh, lstKhNew);
            List<Integer> lstKhDelete=listTrung(lstIdKh, lstKhMerge); //->Lấy khách hàng để xóa khi bỏ tích ở phiếu giảm giá
            List<Integer> lstKhInsert=mergeAndRemoveDuplicates(lstKhDelete, lstKhMerge);
            System.out.println("TH3: "+lstKhInsert);

            for(int idKh: lstKhDelete){
                delete(maPgg,idKh);
            }
            for(int idKh: lstKhInsert){
                save(maPgg,idKh,nguoiTao);
            }
            return lstKhInsert;
        }

        phieuGiamGiaKhachHangDao.deleteByPgg(maPgg);
        System.out.println("Ra null òi");
        return null;
    }

    public static List<Integer> listTrung(List<Integer> list1, List<Integer> list2){
        Set<Integer> set1 = new HashSet<>(list1);
        Set<Integer> set2 = new HashSet<>(list2);

        // Lấy các phần tử xuất hiện trong cả 2
        Set<Integer> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);

        return new ArrayList<>(intersection);
    }

    public static List<Integer> mergeAndRemoveDuplicates(List<Integer> list1, List<Integer> list2) {
        Set<Integer> set1 = new HashSet<>(list1);
        Set<Integer> set2 = new HashSet<>(list2);

        // Lấy các phần tử xuất hiện trong cả 2
        Set<Integer> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);

        // Lấy tất cả phần twut xuất hiện trong cả 2 rồi loại bỏ intersection
        Set<Integer> union = new HashSet<>(set1);
        union.addAll(set2);
        union.removeAll(intersection);

        return new ArrayList<>(union);
    }


    public void delete(String maPgg, int idKh){
        phieuGiamGiaKhachHangDao.deletePggKh(maPgg,idKh);
    }

    public void save(String maPgg, int idKh, String nguoiTao){
        PhieuGiamGiaKhachHang phieuGiamGiaKhachHang=new PhieuGiamGiaKhachHang();

        phieuGiamGiaKhachHang.setIdPhieuGiamGia(phieuGiamGiaDao.findByMaKhuyenMai(maPgg));
        phieuGiamGiaKhachHang.setIdTaiKhoan(taiKhoanDao.findById(idKh).get());
        phieuGiamGiaKhachHang.setNguoiTao(nguoiTao);
        //Lấy ngày hiện tại
        Instant instant = Instant.now();
        Timestamp ngayTao = java.sql.Timestamp.from(instant);
        phieuGiamGiaKhachHang.setNgayTao(ngayTao);
        phieuGiamGiaKhachHang.setNguoiSua(null);
        phieuGiamGiaKhachHang.setNgaySua(null);
        phieuGiamGiaKhachHangDao.save(phieuGiamGiaKhachHang);
    }

    /**
     Lấy list tài khoản với id phiếu giảm giá
     */
    public Page<TaiKhoan> getListKhByIdPgg(int idPgg,int page){
        Pageable p= PageRequest.of(page,6);
        Page<TaiKhoan> pageData=phieuGiamGiaKhachHangDao.getListTkByIdPgg(idPgg,p);
        return pageData;
    }

    public Page<PhieuGiamGiaKhachHang> getListPggKhByIdPgg(int idPgg,int page){
        Pageable p= PageRequest.of(page,10);
        Page<PhieuGiamGiaKhachHang> pageData=phieuGiamGiaKhachHangDao.getListPggKhByIdPgg(idPgg,p);
        return pageData;
    }

    /**
     Lấy list tài khoản mà chưa add vô phiếu giảm giá
     */
    public Page<TaiKhoan> getListKhByIdPggChuaAdd(int idPgg,int page){
        Pageable p= PageRequest.of(page,6);
        Page<TaiKhoan> pageData=phieuGiamGiaKhachHangDao.getListTkByIdPggChuaAdd(idPgg,p);
        return pageData;
    }

    public Page<TaiKhoan> getKhByIdPggChuaAdd(int idPgg, Optional<String> sdt, int page){
        Pageable p= PageRequest.of(page,6);
        Page<TaiKhoan> pageData=phieuGiamGiaKhachHangDao.getTkByIdPggChuaAdd(idPgg,sdt,p);
        return pageData;
    }

    public void deletePggKh(int id){
        phieuGiamGiaKhachHangDao.deleteById(id);
    }

    public int soLuongKhSoHuu(int idPgg){
        return phieuGiamGiaKhachHangDao.soLuongKhSoHuu(idPgg);
    }

}
