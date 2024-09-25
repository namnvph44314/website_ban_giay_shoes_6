package com.group6.du_an_tot_nghiep.Service.QuanLy;

import com.group6.du_an_tot_nghiep.Dao.TheLoaiDao;
import com.group6.du_an_tot_nghiep.Dto.QuanLy.TheLoai.TheLoaiResponse;
import com.group6.du_an_tot_nghiep.Entities.TheLoai;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TheLoaiService {

    @Autowired
    TheLoaiDao theLoaiDao;

   public Page<TheLoaiResponse> findAllTheLoai(int pageNumber)
   {
       Pageable pageable = PageRequest.of(pageNumber, 5);
       Page<TheLoai> pageData = theLoaiDao.findAllByTrangThaiOrTrangThaiOrderByIdDesc(TheLoaiDao.DANG_BAN, TheLoaiDao.NGUNG_BAN, pageable);
       Page<TheLoaiResponse> pageDataResponse = pageData.map(TheLoaiResponse::convertToResponse);

       return pageDataResponse;
   }

}
