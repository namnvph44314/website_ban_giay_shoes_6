package com.group6.du_an_tot_nghiep.Service.QuanLy;

import com.group6.du_an_tot_nghiep.Dao.DeGiayDao;
import com.group6.du_an_tot_nghiep.Dto.QuanLy.DeGiay.DeGiayResponse;
import com.group6.du_an_tot_nghiep.Entities.DeGiay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeGiayService {

    @Autowired
    DeGiayDao deGiayDao;

   public Page<DeGiayResponse> findAllDeGiay(int pageNumber)
   {
       Pageable pageable = PageRequest.of(pageNumber, 5);
       Page<DeGiay> pageData = deGiayDao.findAllByTrangThaiOrTrangThaiOrderByIdDesc(DeGiayDao.DANG_BAN, DeGiayDao.NGUNG_BAN, pageable);
       Page<DeGiayResponse> pageDataResponse = pageData.map(DeGiayResponse::convertToResponse);

       return pageDataResponse;
   }

   public List<DeGiay> findAllByTrangThai(){
       return deGiayDao.findAllByTrangThai(DeGiayDao.DANG_BAN);
   }

}
