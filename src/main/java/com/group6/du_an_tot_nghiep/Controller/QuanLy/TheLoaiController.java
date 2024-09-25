package com.group6.du_an_tot_nghiep.Controller.QuanLy;

import com.group6.du_an_tot_nghiep.Dao.TheLoaiDao;
import com.group6.du_an_tot_nghiep.Dto.QuanLy.TheLoai.TheLoaiResponse;
import com.group6.du_an_tot_nghiep.Entities.TaiKhoan;
import com.group6.du_an_tot_nghiep.Entities.TheLoai;
import com.group6.du_an_tot_nghiep.Service.QuanLy.TaiKhoanService;
import com.group6.du_an_tot_nghiep.Service.QuanLy.TheLoaiService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Controller
@RequestMapping("/admin/the-loai")
public class TheLoaiController {

    String _messageSuccessAddTheLoai = "";
    int _hasError = 0;
    int _pageNumber;
    Page<TheLoaiResponse> _pageData = null;


    @Autowired
    TheLoaiService theLoaiService;

    @Autowired
    TheLoaiDao theLoaiDao;
    TaiKhoan _taiKhoan;
    @Autowired
    TaiKhoanService _taiKhoanService;

    @GetMapping("/get-avatar/{id}")
    @ResponseBody
    public ResponseEntity<ByteArrayResource> readFile (@PathVariable("id") int id){
        try {
            String photo = _taiKhoanService.findUrlAnhDaiDien(id);
            Path fileName = Paths.get("uploads", photo);
            byte[] buffer = Files.readAllBytes(fileName);
            ByteArrayResource byteArrayResource = new ByteArrayResource(buffer);
            return ResponseEntity.ok()
                    .contentLength(buffer.length)
                    .contentType(MediaType.parseMediaType("image/png"))
                    .body(byteArrayResource);
        }catch (Exception e){
            return null;
        }
    }

    @GetMapping("/get-tai-khoan")
    @ResponseBody
    public ResponseEntity<TaiKhoan> getTaiKhoan() {
        _taiKhoan = _taiKhoanService.getById(16);
        try {
            return ResponseEntity.ok()
                    .body(_taiKhoan);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/read-file/{url}")
    @ResponseBody
    public ResponseEntity<ByteArrayResource> readFile(@PathVariable("url") String url) {
        try {
            String photo = url;
            Path fileName = Paths.get("D:\\IntelijiWorkspaces\\datn\\du_an_tot_nghiep\\src\\main\\resources\\uploads", photo);
            byte[] buffer = Files.readAllBytes(fileName);
            ByteArrayResource byteArrayResource = new ByteArrayResource(buffer);
            return ResponseEntity.ok()
                    .contentLength(buffer.length)
                    .contentType(MediaType.parseMediaType("image/png"))
                    .body(byteArrayResource);
        } catch (Exception e) {

            return null;
        }
    }

    @GetMapping("/index-the-loai")
    public String indexTheLoai(Model model, @RequestParam Optional<Integer> pageNumber)
    {
        _pageNumber = pageNumber.orElse(0);
        _pageData = theLoaiService.findAllTheLoai(_pageNumber);
        model.addAttribute("pageData", _pageData);
        if (_messageSuccessAddTheLoai.length() > 0) {
            model.addAttribute("successMessage", _messageSuccessAddTheLoai);
            _messageSuccessAddTheLoai = "";
        }
        model.addAttribute("the_loai", new TheLoai());
        return "/quan-ly/the_loai/index_the_loai";
    }

    @PostMapping("/add_the_loai")
    public String submitForm(@Valid @ModelAttribute("the_loai") TheLoai the_loai, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            _hasError = 1;
            System.out.println("Có lỗi");
            model.addAttribute("pageData", _pageData);
            model.addAttribute("error", _hasError);
            return "/quan-ly/the_loai/index_the_loai";
        }
        int size = theLoaiDao.findAll().size();

        String nguoiTao = "admin1234";

        TheLoai newTheLoai = new TheLoai();
        newTheLoai.setTenTheLoai(the_loai.getTenTheLoai());
        newTheLoai.setTrangThai(1);
        newTheLoai.setNguoiTao(nguoiTao);

        theLoaiDao.save(newTheLoai);
        _messageSuccessAddTheLoai = "Thêm thể loại thành công";
        return "redirect:/admin/the-loai/index-the-loai";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") int id, Model model)
    {
        TheLoai theLoaiDetail = theLoaiDao.findById(id).get();
        model.addAttribute("the_loai", theLoaiDetail);

        return "/quan-ly/the_loai/update_the_loai";
    }

    @PostMapping("/update_the_loai/{id}")
    public String updateForm(@PathVariable("id") TheLoai theLoaiRq, @Valid @ModelAttribute("the_loai") TheLoai the_loai, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors())
        {
            System.out.println("Có lỗi");
            return "/quan-ly/the_loai/update_the_loai";
        }

        theLoaiRq.setTenTheLoai(the_loai.getTenTheLoai());

        theLoaiDao.save(theLoaiRq);

        return "redirect:/admin/the-loai/index-the-loai";
    }

    @PostMapping("/search")
    public String search(@RequestParam("param") String search, @RequestParam Optional<Integer> pageNumber, Model model)
    {
        _pageNumber = pageNumber.orElse(0);
        Pageable pageable = PageRequest.of(_pageNumber, 5);
        Page<TheLoai> pageDataSearch = theLoaiDao.findAllByTenTheLoaiIsLike(search, pageable);
        model.addAttribute("pageData", pageDataSearch);

        model.addAttribute("the_loai", new TheLoai());
        return "/quan-ly/the_loai/index_the_loai";
    }

}
