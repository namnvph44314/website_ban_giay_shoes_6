package com.group6.du_an_tot_nghiep.Config;

import com.group6.du_an_tot_nghiep.Dto.QuanLy.SanPham.SanPhamSearchFilterRequest;
import com.group6.du_an_tot_nghiep.Dto.QuanLy.SanPhamChiTiet.SanPhamChiTietFilterRequest;
import com.group6.du_an_tot_nghiep.Dto.QuanLy.SanPhamChiTiet.SanPhamChiTietSearchRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Beans {

    @Bean
    public SanPhamChiTietSearchRequest SanPhamChiTietSearchRequest () {
        return new SanPhamChiTietSearchRequest();
    }

    @Bean
    public SanPhamChiTietFilterRequest SanPhamChiTietFilterRequest () {
        return new SanPhamChiTietFilterRequest();
    }

    @Bean
    public SanPhamSearchFilterRequest SanPhamSearchFilterRequest () {
        return new SanPhamSearchFilterRequest();
    }
}
