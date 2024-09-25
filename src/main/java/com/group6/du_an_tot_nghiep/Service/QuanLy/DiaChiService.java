package com.group6.du_an_tot_nghiep.Service.QuanLy;

import com.group6.du_an_tot_nghiep.Dao.DiaChiDao;
import com.group6.du_an_tot_nghiep.Entities.DiaChi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class DiaChiService {

    private DiaChiDao diaChiDao;

    @Autowired
    public DiaChiService(DiaChiDao diaChiDao) {
        this.diaChiDao = diaChiDao;
    }

    public List<DiaChi> getAddressByIdAccount(Integer accountId){
        try{
            List<DiaChi> addressAccount = diaChiDao.getDiaChiByTaiKhoanByIdTaiKhoan(accountId);
            Integer total = diaChiDao.countAllByTaiKhoanByIdTaiKhoan(accountId);

            if (total == 0){
                throw new RuntimeException("Your account don't have address");
            }

            return addressAccount;

        }catch (Exception exception){
            log.error("[ERROR] getAddressByIdAccount {} " + exception.getMessage());
            return new ArrayList<>();
        }
    }
}
