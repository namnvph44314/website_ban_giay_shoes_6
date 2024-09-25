package com.group6.du_an_tot_nghiep.Service.QuanLy;

import com.group6.du_an_tot_nghiep.Dao.KichThuocDao;
import com.group6.du_an_tot_nghiep.Dto.QuanLy.KichThuoc.KichThuocResponse;
import com.group6.du_an_tot_nghiep.Entities.KichThuoc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KichThuocService {

    @Autowired
    KichThuocDao kichThuocDao;

   public Page<KichThuocResponse> findAllKichThuoc(int pageNumber)
   {
       Pageable pageable = PageRequest.of(pageNumber, 5);
       Page<KichThuoc> pageData = kichThuocDao.findAllByTrangThaiOrTrangThaiOrderByIdDesc(KichThuocDao.DANG_BAN, KichThuocDao.NGUNG_BAN, pageable);
       Page<KichThuocResponse> pageDataResponse = pageData.map(KichThuocResponse::convertToResponse);

       return pageDataResponse;
   }

   public List<KichThuoc> findAllByTrangThai(){
       return kichThuocDao.findAllByTrangThai(KichThuocDao.DANG_BAN);
   }

}
