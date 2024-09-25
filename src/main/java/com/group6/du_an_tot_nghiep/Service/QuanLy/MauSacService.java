package com.group6.du_an_tot_nghiep.Service.QuanLy;

import com.group6.du_an_tot_nghiep.Dao.MauSacDao;
import com.group6.du_an_tot_nghiep.Dto.QuanLy.MauSac.MauSacResponse;
import com.group6.du_an_tot_nghiep.Entities.MauSac;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MauSacService {

    @Autowired
    MauSacDao mauSacDao;

   public Page<MauSacResponse> findAllMauSac(int pageNumber)
   {
       Pageable pageable = PageRequest.of(pageNumber, 5);
       Page<MauSac> pageData = mauSacDao.findAllByTrangThaiOrTrangThaiOrderByIdDesc(MauSacDao.DANG_BAN, MauSacDao.NGUNG_BAN, pageable);
       Page<MauSacResponse> pageDataResponse = pageData.map(MauSacResponse::convertToResponse);

       return pageDataResponse;
   }

   public List<MauSac> findAllByTrangThai(){
       return mauSacDao.findAllByTrangThai(MauSacDao.DANG_BAN);
   }

}
