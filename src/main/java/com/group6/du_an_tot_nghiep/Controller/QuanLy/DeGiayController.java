package com.group6.du_an_tot_nghiep.Controller.QuanLy;

import com.group6.du_an_tot_nghiep.Dao.DeGiayDao;
import com.group6.du_an_tot_nghiep.Dto.QuanLy.DeGiay.DeGiayResponse;
import com.group6.du_an_tot_nghiep.Entities.DeGiay;
import com.group6.du_an_tot_nghiep.Service.QuanLy.DeGiayService;
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
@RequestMapping("/admin/de-giay")
public class DeGiayController {

    String _messageSuccessAddDeGiay = "";
    int _hasError = 0;
    int _pageNumber;
    Page<DeGiayResponse> _pageData = null;


    @Autowired
    DeGiayService deGiayService;

    @Autowired
    DeGiayDao deGiayDao;

    @GetMapping("/index-de-giay")
    public String indexDeGiay(Model model, @RequestParam Optional<Integer> pageNumber)
    {
        _pageNumber = pageNumber.orElse(0);
        _pageData = deGiayService.findAllDeGiay(_pageNumber);
        model.addAttribute("pageData", _pageData);
        if (_messageSuccessAddDeGiay.length() > 0) {
            model.addAttribute("successMessage", _messageSuccessAddDeGiay);
            _messageSuccessAddDeGiay = "";
        }
        model.addAttribute("de_giay", new DeGiay());
        return "/quan-ly/de_giay/index_de_giay";
    }

    @PostMapping("/add_de_giay")
    public String submitForm(@Valid @ModelAttribute("de_giay") DeGiay de_giay, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            _hasError = 1;
            System.out.println("Có lỗi");
            model.addAttribute("pageData", _pageData);
            model.addAttribute("error", _hasError);
            return "/quan-ly/de_giay/index_de_giay";
        }
        int size = deGiayDao.findAll().size();


        String nguoiTao = "admin1234";

        DeGiay newDeGiay = new DeGiay();
        newDeGiay.setTenDeGiay(de_giay.getTenDeGiay());
        newDeGiay.setTrangThai(1);
        newDeGiay.setNguoiTao(nguoiTao);

        deGiayDao.save(newDeGiay);
        _messageSuccessAddDeGiay = "Thêm đế giày thành công";
        return "redirect:/admin/de-giay/index-de-giay";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") int id, Model model)
    {
        DeGiay deGiayDetail = deGiayDao.findById(id).get();
        model.addAttribute("de_giay", deGiayDetail);

        return "/quan-ly/de_giay/update_de_giay";
    }

    @PostMapping("/update_de_giay/{id}")
    public String updateForm(@PathVariable("id") DeGiay deGiayRq, @Valid @ModelAttribute("de_giay") DeGiay de_giay, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors())
        {
            System.out.println("Có lỗi");
            return "/quan-ly/de_giay/update_de_giay";
        }

        deGiayRq.setTenDeGiay(de_giay.getTenDeGiay());

        deGiayDao.save(deGiayRq);

        return "redirect:/admin/de-giay/index-de-giay";
    }

    @PostMapping("/search")
    public String search(@RequestParam("param") String search, @RequestParam Optional<Integer> pageNumber, Model model)
    {
        _pageNumber = pageNumber.orElse(0);
        Pageable pageable = PageRequest.of(_pageNumber, 5);
        Page<DeGiay> pageDataSearch = deGiayDao.findAllByTenDeGiayIsLike(search, pageable);
        model.addAttribute("pageData", pageDataSearch);

        model.addAttribute("de_giay", new DeGiay());
        return "/quan-ly/de_giay/index_de_giay";
    }

}
