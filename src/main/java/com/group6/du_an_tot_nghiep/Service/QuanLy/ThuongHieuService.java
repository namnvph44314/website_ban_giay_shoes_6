package com.group6.du_an_tot_nghiep.Service.QuanLy;

import com.group6.du_an_tot_nghiep.Dao.ThuongHieuDao;
import com.group6.du_an_tot_nghiep.Dto.QuanLy.ThuongHieu.ThuongHieuResponse;
import com.group6.du_an_tot_nghiep.Entities.ThuongHieu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ThuongHieuService {

    @Autowired
    ThuongHieuDao thuongHieuDao;

   public Page<ThuongHieuResponse> findAllThuongHieu(int pageNumber)
   {
       Pageable pageable = PageRequest.of(pageNumber, 5);
       Page<ThuongHieu> pageData = thuongHieuDao.findAllByTrangThaiOrTrangThaiOrderByIdDesc(ThuongHieuDao.DANG_BAN, ThuongHieuDao.NGUNG_BAN, pageable);
       Page<ThuongHieuResponse> pageDataResponse = pageData.map(ThuongHieuResponse::convertToResponse);

       return pageDataResponse;
   }

   public List<ThuongHieu> findAllByTrangThai(){
       return thuongHieuDao.findAllByTrangThai(ThuongHieuDao.DANG_BAN);
   }

}
