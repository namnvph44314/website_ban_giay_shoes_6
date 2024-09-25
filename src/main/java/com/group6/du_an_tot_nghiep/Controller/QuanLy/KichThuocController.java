package com.group6.du_an_tot_nghiep.Controller.QuanLy;

import com.group6.du_an_tot_nghiep.Dao.KichThuocDao;
import com.group6.du_an_tot_nghiep.Dto.QuanLy.KichThuoc.KichThuocResponse;
import com.group6.du_an_tot_nghiep.Entities.KichThuoc;
import com.group6.du_an_tot_nghiep.Service.QuanLy.KichThuocService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/admin/kich-thuoc")
public class KichThuocController {

    String _messageSuccessAddKichThuoc = "";
    int _hasError = 0;
    int _pageNumber;
    Page<KichThuocResponse> _pageData = null;


    @Autowired
    KichThuocService kichThuocService;

    @Autowired
    KichThuocDao kichThuocDao;

    @GetMapping("/index-kich-thuoc")
    public String indexDeGiay(Model model, @RequestParam Optional<Integer> pageNumber)
    {
        _pageNumber = pageNumber.orElse(0);
        _pageData = kichThuocService.findAllKichThuoc(_pageNumber);
        model.addAttribute("pageData", _pageData);
        if (_messageSuccessAddKichThuoc.length() > 0) {
            model.addAttribute("successMessage", _messageSuccessAddKichThuoc);
            _messageSuccessAddKichThuoc = "";
        }
        model.addAttribute("kich_thuoc", new KichThuoc());
        return "/quan-ly/kich_thuoc/index_kich_thuoc";
    }

    @PostMapping("/add_kich_thuoc")
    public String submitForm(@Valid @ModelAttribute("kich_thuoc") KichThuoc kich_thuoc, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            _hasError = 1;
            System.out.println("Có lỗi");
            model.addAttribute("pageData", _pageData);
            model.addAttribute("error", _hasError);
            return "/quan-ly/kich_thuoc/index_kich_thuoc";
        }
        int size = kichThuocDao.findAll().size();

        String nguoiTao = "admin1234";

        KichThuoc newKichThuoc = new KichThuoc();
        newKichThuoc.setTenKichThuoc(kich_thuoc.getTenKichThuoc());
        newKichThuoc.setTrangThai(1);
        newKichThuoc.setNguoiTao(nguoiTao);

        kichThuocDao.save(newKichThuoc);
        _messageSuccessAddKichThuoc = "Thêm kích thước thành công";
        return "redirect:/admin/kich-thuoc/index-kich-thuoc";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") int id, Model model)
    {
        KichThuoc kichThuocDetail = kichThuocDao.findById(id).get();
        model.addAttribute("kich_thuoc", kichThuocDetail);

        return "/quan-ly/kich_thuoc/update_kich_thuoc";
    }

    @PostMapping("/update_kich_thuoc/{id}")
    public String updateForm(@PathVariable("id") KichThuoc kichThuocRq, @Valid @ModelAttribute("kich_thuoc") KichThuoc kich_thuoc, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors())
        {
            System.out.println("Có lỗi");
            return "/quan-ly/kich_thuoc/update_kich_thuoc";
        }

        kichThuocRq.setTenKichThuoc(kich_thuoc.getTenKichThuoc());

        kichThuocDao.save(kichThuocRq);

        return "redirect:/admin/kich-thuoc/index-kich-thuoc";
    }

    @PostMapping("/search")
    public String search(@RequestParam("param") float search, @RequestParam Optional<Integer> pageNumber, Model model)
    {
        _pageNumber = pageNumber.orElse(0);
        Pageable pageable = PageRequest.of(_pageNumber, 5);
        Page<KichThuoc> pageDataSearch = kichThuocDao.findByTenKichThuoc(search, pageable);
        model.addAttribute("pageData", pageDataSearch);

        model.addAttribute("kich_thuoc", new KichThuoc());
        return "/quan-ly/kich_thuoc/index_kich_thuoc";
    }

}
