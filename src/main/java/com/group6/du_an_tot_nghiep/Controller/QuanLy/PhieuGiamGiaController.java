package com.group6.du_an_tot_nghiep.Controller.QuanLy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.group6.du_an_tot_nghiep.Dao.PhieuGiamGiaDao;
import com.group6.du_an_tot_nghiep.Dto.QuanLy.PhieuGiamGia.PhieuGiamGiaFilter;
import com.group6.du_an_tot_nghiep.Dto.QuanLy.PhieuGiamGia.PhieuGiamGiaRequest;
import com.group6.du_an_tot_nghiep.Dto.QuanLy.PhieuGiamGia.PhieuGiamGiaUpdate;
import com.group6.du_an_tot_nghiep.Dto.QuanLy.PhieuGiamGiaKhachHang.PhieuGiamGiaKhachHangAdd;
import com.group6.du_an_tot_nghiep.Entities.PhieuGiamGia;
import com.group6.du_an_tot_nghiep.Service.QuanLy.EmailService;
import com.group6.du_an_tot_nghiep.Service.QuanLy.PhieuGiamGiaKhachHangService;
import com.group6.du_an_tot_nghiep.Service.QuanLy.PhieuGiamGiaService;
import com.group6.du_an_tot_nghiep.Service.QuanLy.TaiKhoanService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/phieu-giam-gia")

public class PhieuGiamGiaController {

    @Autowired
    PhieuGiamGiaService phieuGiamGiaService;
    @Autowired
    TaiKhoanService taiKhoanService;
    @Autowired
    PhieuGiamGiaKhachHangService phieuGiamGiaKhachHangService;
    @Autowired
    EmailService emailService;

    private String nguoiTao = "namnv";
    int checkPage = 0;
    boolean checkSearch = true;
    boolean checkLoc = true;
    boolean checkAdd = true;

    @GetMapping("/index")
    public String getAll(
            Model model,
            @RequestParam("page") Optional<Integer> pageParam
    ) {
        checkSearch = true;
        int page = pageParam.orElse(0);
        checkPage = 0;
        checkLoc = true;

        model.addAttribute("checkLoc", checkLoc);
        model.addAttribute("p", checkPage);
        model.addAttribute("PGGFilter", new PhieuGiamGiaFilter(null, null, 3, 3, 3));
        model.addAttribute("lstPGG", phieuGiamGiaService.getAll(page));
        model.addAttribute("url", "/admin/phieu-giam-gia/index");
        return "quan_ly/phieu_giam_gia/index";
    }

    @GetMapping("/loc")
    public String loc(
            Model model,
            @RequestParam("page") Optional<Integer> pageParam,
            @Valid @ModelAttribute("PGGFilter") PhieuGiamGiaFilter PGGFilter,
            BindingResult result
    ) {
        checkSearch = true;
        int page = pageParam.orElse(0);
        if (result.hasErrors()) {

            checkPage = 1;

            model.addAttribute("p", checkPage);
            model.addAttribute("lstPGG", phieuGiamGiaService.getAll(page));
            model.addAttribute("url", "/admin/phieu-giam-gia/index");
            return "quan_ly/phieu_giam_gia/index";
        }

        if (PGGFilter.getNgayBatDau().trim().equals("") && !PGGFilter.getNgayKetThuc().trim().equals("")) {
            checkLoc = false;
            model.addAttribute("checkLoc", checkLoc);
            model.addAttribute("nbd", "Vui lòng nhập ngày bắt đầu");

            checkPage = 1;
            model.addAttribute("p", checkPage);
            model.addAttribute("lstPGG", phieuGiamGiaService.getAll(page));
            model.addAttribute("url", "/admin/phieu-giam-gia/index");
            return "quan_ly/phieu_giam_gia/index";
        }
        if (!PGGFilter.getNgayBatDau().trim().equals("") && PGGFilter.getNgayKetThuc().trim().equals("")) {
            checkLoc = false;
            model.addAttribute("checkLoc", checkLoc);
            model.addAttribute("nkt", "Vui lòng nhập ngày kết thúc");

            checkPage = 1;

            model.addAttribute("p", checkPage);
            model.addAttribute("lstPGG", phieuGiamGiaService.getAll(page));
            model.addAttribute("url", "/admin/phieu-giam-gia/index");
            return "quan_ly/phieu_giam_gia/index";
        }

        String url = "/admin/phieu-giam-gia/loc?ngayBatDau=" + PGGFilter.getNgayBatDau() + "&ngayKetThuc=" + PGGFilter.getNgayKetThuc() + "&kieu=" + PGGFilter.getKieu() + "&kieuGiaTri=" + PGGFilter.getTrangThai() + "&trangThai=" + PGGFilter.getTrangThai();
        if (PGGFilter.getNgayBatDau().trim().equals("")) {
            PGGFilter.setNgayBatDau(null);
        }
        if (PGGFilter.getNgayKetThuc().trim().equals("")) {
            PGGFilter.setNgayKetThuc(null);
        }
        if (PGGFilter.getKieu() == 3) {
            PGGFilter.setKieu(null);
        }
        if (PGGFilter.getKieuGiaTri() == 3) {
            PGGFilter.setKieuGiaTri(null);
        }
        if (PGGFilter.getTrangThai() == 3) {
            PGGFilter.setTrangThai(null);
        }

        checkPage = 1;
        checkLoc = true;
        model.addAttribute("checkLoc", checkLoc);
        model.addAttribute("p", checkPage);
        model.addAttribute("PGGFilter", PGGFilter);
        model.addAttribute("lstPGG", phieuGiamGiaService.loc(PGGFilter, page));
        model.addAttribute("url", url);
        return "quan_ly/phieu_giam_gia/index";
    }

    @GetMapping("/search")
    public String search(
            Model model,
            @RequestParam("search") String param,
            @RequestParam("page") Optional<Integer> pageParam
    ) {
        if (param == null || param.trim().equals("")) {
            checkLoc = true;
            int page = pageParam.orElse(0);
            checkPage = 0;
            model.addAttribute("checkLoc", checkLoc);
            model.addAttribute("p", checkPage);
            model.addAttribute("PGGFilter", new PhieuGiamGiaFilter(null, null, 3, 3, 3));
            model.addAttribute("lstPGG", phieuGiamGiaService.getAll(page));
            model.addAttribute("url", "/admin/phieu-giam-gia/index");
            return "quan_ly/phieu_giam_gia/index";
        }
        checkSearch = true;
        checkPage = 2;
        checkLoc = true;
        model.addAttribute("checkLoc", checkLoc);
        model.addAttribute("p", checkPage);
        model.addAttribute("search", param);
        model.addAttribute("lstPGG", phieuGiamGiaService.search(param));
        model.addAttribute("PGGFilter", new PhieuGiamGiaFilter(null, null, 3, 3, 3));
        return "quan_ly/phieu_giam_gia/index";
    }


    @GetMapping("/add")
    public String add(
    ) {
        return "quan_ly/phieu_giam_gia/add";
    }


    @GetMapping("/detail/{id}")
    public String detail(
            @PathVariable("id") int id,
            Model model,
            @RequestParam("page") Optional<Integer> pageParam,
            @RequestParam(name = "sdt") Optional<String> sdtParam,
            @RequestParam("pageKhByPgg") Optional<Integer> pageParamKhByPgg
    ) {
        model.addAttribute("idPgg",id);
        System.out.println(phieuGiamGiaService.getOneById(id).get().getKieu()+"//////////////////");
        model.addAttribute("tuNgay",phieuGiamGiaService.getOneById(id).get().getNgayBatDau());
        model.addAttribute("tuNgay",phieuGiamGiaService.getOneById(id).get().getNgayKetThuc());
        model.addAttribute("kieu",phieuGiamGiaService.getOneById(id).get().getKieu());
        return "quan_ly/phieu_giam_gia/detail";
    }




    @GetMapping("/delete-pggKh/{idPgg}&&{idKh}")
    public String deletePggKhByPgg(
            @PathVariable("idPgg") int idPgg,
            @PathVariable("idKh") int idKh
//            @ModelAttribute("pggKhDelete") PhieuGiamGiaKhachHangAdd phieuGiamGiaKhachHangDelete
    ) {
        String maPgg = phieuGiamGiaService.getOneById(idPgg).get().getMaKhuyenMai();
        phieuGiamGiaKhachHangService.delete(maPgg, idKh);
        return "redirect:/admin/phieu-giam-gia/detail/" + idPgg;
    }

    @GetMapping("/update-trang-thai/{id}")
    public String updateTrangThai(@PathVariable("id") PhieuGiamGia phieuGiamGia){
        phieuGiamGiaService.updatePggNhd(phieuGiamGia.getId());
        return "redirect:/admin/phieu-giam-gia/index";
    }
}
