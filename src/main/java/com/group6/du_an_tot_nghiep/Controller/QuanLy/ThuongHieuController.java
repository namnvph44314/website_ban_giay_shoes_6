package com.group6.du_an_tot_nghiep.Controller.QuanLy;

import com.group6.du_an_tot_nghiep.Dao.ThuongHieuDao;
import com.group6.du_an_tot_nghiep.Dto.QuanLy.ThuongHieu.ThuongHieuResponse;
import com.group6.du_an_tot_nghiep.Entities.ThuongHieu;
import com.group6.du_an_tot_nghiep.Service.QuanLy.ThuongHieuService;
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
@RequestMapping("/admin/thuong-hieu")
public class ThuongHieuController {

    String _messageSuccessAddThuongHieu = "";
    int _hasError = 0;
    int _pageNumber;
    Page<ThuongHieuResponse> _pageData = null;


    @Autowired
    ThuongHieuService thuongHieuService;

    @Autowired
    ThuongHieuDao thuongHieuDao;

    @GetMapping("/index-thuong-hieu")
    public String indexThuongHieu(Model model, @RequestParam Optional<Integer> pageNumber)
    {
        _pageNumber = pageNumber.orElse(0);
        _pageData = thuongHieuService.findAllThuongHieu(_pageNumber);
        model.addAttribute("pageData", _pageData);
        if (_messageSuccessAddThuongHieu.length() > 0) {
            model.addAttribute("successMessage", _messageSuccessAddThuongHieu);
            _messageSuccessAddThuongHieu = "";
        }
        model.addAttribute("thuong_hieu", new ThuongHieu());
        return "/quan-ly/thuong_hieu/index_thuong_hieu";
    }

    @PostMapping("/add_thuong_hieu")
    public String submitForm(@Valid @ModelAttribute("thuong_hieu") ThuongHieu thuong_hieu, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            _hasError = 1;
            System.out.println("Có lỗi");
            model.addAttribute("pageData", _pageData);
            model.addAttribute("error", _hasError);
            return "/quan-ly/thuong_hieu/index_thuong_hieu";
        }
        int size = thuongHieuDao.findAll().size();

        String nguoiTao = "admin1234";

        ThuongHieu newThuongHieu = new ThuongHieu();
        newThuongHieu.setTenThuongHieu(thuong_hieu.getTenThuongHieu());
        newThuongHieu.setTrangThai(1);
        newThuongHieu.setNguoiTao(nguoiTao);

        thuongHieuDao.save(newThuongHieu);
        _messageSuccessAddThuongHieu = "Thêm thương hiệu thành công";
        return "redirect:/admin/thuong-hieu/index-thuong-hieu";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") int id, Model model)
    {
        ThuongHieu thuongHieuDetail = thuongHieuDao.findById(id).get();
        model.addAttribute("thuong_hieu", thuongHieuDetail);

        return "/quan-ly/thuong_hieu/update_thuong_hieu";
    }

    @PostMapping("/update_thuong_hieu/{id}")
    public String updateForm(@PathVariable("id") ThuongHieu thuongHieuRq, @Valid @ModelAttribute("thuong_hieu") ThuongHieu thuong_hieu, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors())
        {
            System.out.println("Có lỗi");
            return "/quan-ly/thuong_hieu/update_thuong_hieu";
        }

        thuongHieuRq.setTenThuongHieu(thuong_hieu.getTenThuongHieu());

        thuongHieuDao.save(thuongHieuRq);

        return "redirect:/admin/thuong-hieu/index-thuong-hieu";
    }

    @PostMapping("/search")
    public String search(@RequestParam("param") String search, @RequestParam Optional<Integer> pageNumber, Model model)
    {
        _pageNumber = pageNumber.orElse(0);
        Pageable pageable = PageRequest.of(_pageNumber, 5);
        Page<ThuongHieu> pageDataSearch = thuongHieuDao.findAllByTenThuongHieuIsLike(search, pageable);
        model.addAttribute("pageData", pageDataSearch);

        model.addAttribute("thuong_hieu", new ThuongHieu());
        return "/quan-ly/thuong_hieu/index_thuong_hieu";
    }

}
