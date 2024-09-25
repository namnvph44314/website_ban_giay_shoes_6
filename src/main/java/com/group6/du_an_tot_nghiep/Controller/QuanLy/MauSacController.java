package com.group6.du_an_tot_nghiep.Controller.QuanLy;

import com.group6.du_an_tot_nghiep.Dao.MauSacDao;
import com.group6.du_an_tot_nghiep.Dto.QuanLy.MauSac.MauSacResponse;
import com.group6.du_an_tot_nghiep.Entities.MauSac;
import com.group6.du_an_tot_nghiep.Service.QuanLy.MauSacService;
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
@RequestMapping("/admin/mau-sac")
public class MauSacController {

    String _messageSuccessAddMauSac = "";
    int _hasError = 0;
    int _pageNumber;
    Page<MauSacResponse> _pageData = null;


    @Autowired
    MauSacService mauSacService;

    @Autowired
    MauSacDao mauSacDao;

    @GetMapping("/index-mau-sac")
    public String indexDeGiay(Model model, @RequestParam Optional<Integer> pageNumber)
    {
        _pageNumber = pageNumber.orElse(0);
        _pageData = mauSacService.findAllMauSac(_pageNumber);
        model.addAttribute("pageData", _pageData);
        if (_messageSuccessAddMauSac.length() > 0) {
            model.addAttribute("successMessage", _messageSuccessAddMauSac);
            _messageSuccessAddMauSac = "";
        }
        model.addAttribute("mau_sac", new MauSac());
        return "/quan-ly/mau_sac/index_mau_sac";
    }

    @PostMapping("/add_mau_sac")
    public String submitForm(@Valid @ModelAttribute("mau_sac") MauSac mau_sac, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            _hasError = 1;
            System.out.println("Có lỗi");
            model.addAttribute("pageData", _pageData);
            model.addAttribute("error", _hasError);
            return "/quan-ly/mau_sac/index_mau_sac";
        }
        int size = mauSacDao.findAll().size();

        String nguoiTao = "admin1234";

        MauSac newMauSac = new MauSac();
        newMauSac.setTenMauSac(mau_sac.getTenMauSac());
        newMauSac.setTrangThai(1);
        newMauSac.setNguoiTao(nguoiTao);

        mauSacDao.save(newMauSac);
        _messageSuccessAddMauSac = "Thêm màu sắc thành công";
        return "redirect:/admin/mau-sac/index-mau-sac";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") int id, Model model)
    {
        MauSac mauSacDetail = mauSacDao.findById(id).get();
        model.addAttribute("mau_sac", mauSacDetail);

        return "/quan-ly/mau_sac/update_mau_sac";
    }

    @PostMapping("/update_mau_sac/{id}")
    public String updateForm(@PathVariable("id") MauSac mauSacRq, @Valid @ModelAttribute("mau_sac") MauSac mau_sac, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors())
        {
            System.out.println("Có lỗi");
            return "/quan-ly/mau_sac/update_mau_sac";
        }

        mauSacRq.setTenMauSac(mau_sac.getTenMauSac());
        mauSacDao.save(mauSacRq);

        return "redirect:/admin/mau-sac/index-mau-sac";
    }

    @PostMapping("/search")
    public String search(@RequestParam("param") String search, @RequestParam Optional<Integer> pageNumber, Model model)
    {
        _pageNumber = pageNumber.orElse(0);
        Pageable pageable = PageRequest.of(_pageNumber, 5);
        Page<MauSac> pageDataSearch = mauSacDao.findAllByTenMauSacIsLike(search, pageable);
        model.addAttribute("pageData", pageDataSearch);

        model.addAttribute("mau_sac", new MauSac());
        return "/quan-ly/mau_sac/index_mau_sac";
    }

}
