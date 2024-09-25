package com.group6.du_an_tot_nghiep.Controller.QuanLy;

import com.group6.du_an_tot_nghiep.Dto.QuanLy.HoaDon.HoaDonFillRequest;
import com.group6.du_an_tot_nghiep.Dto.QuanLy.HoaDon.HoaDonResponse;
import com.group6.du_an_tot_nghiep.Service.QuanLy.HoaDonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;



@Controller
//@RequestMapping("/admin/api/hoadon")
public class HoaDonController {
    @Autowired
    private HoaDonService hoaDonService;

    @PostMapping("/api/hoa-don/search-hd")
    @ResponseBody
    public Page<HoaDonResponse> search_hd(@RequestBody HoaDonFillRequest hoaDonFillRequest) {
        return hoaDonService.search(hoaDonFillRequest);
    }

    @GetMapping("/hoa-don/hien-thi")
    public String viewThongTinDonHang1() {
        return "/quan-ly/hoa-don/index";
    }






}
