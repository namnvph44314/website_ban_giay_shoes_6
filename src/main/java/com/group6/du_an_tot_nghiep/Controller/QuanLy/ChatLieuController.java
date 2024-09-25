package com.group6.du_an_tot_nghiep.Controller.QuanLy;

import com.group6.du_an_tot_nghiep.Dao.ChatLieuDao;
import com.group6.du_an_tot_nghiep.Dto.QuanLy.ChatLieu.ChatLieuResponse;
import com.group6.du_an_tot_nghiep.Entities.ChatLieu;
import com.group6.du_an_tot_nghiep.Service.QuanLy.ChatLieuService;
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
@RequestMapping("/admin/chat-lieu")
public class ChatLieuController {

    String _messageSuccessAddChatLieu = "";
    int _hasError = 0;
    int _pageNumber;
    Page<ChatLieuResponse> _pageData = null;


    @Autowired
    ChatLieuService chatLieuService;

    @Autowired
    ChatLieuDao chatLieuDao;

    @GetMapping("/index-chat-lieu")
    public String indexDeGiay(Model model, @RequestParam Optional<Integer> pageNumber)
    {
        _pageNumber = pageNumber.orElse(0);
        _pageData = chatLieuService.findAllChatLieu(_pageNumber);
        model.addAttribute("pageData", _pageData);
        if (_messageSuccessAddChatLieu.length() > 0) {
            model.addAttribute("successMessage", _messageSuccessAddChatLieu);
            _messageSuccessAddChatLieu = "";
        }
        model.addAttribute("chat_lieu", new ChatLieu());
        return "/quan-ly/chat_lieu/index_chat_lieu";
    }

    @PostMapping("/add_chat_lieu")
    public String submitForm(@Valid @ModelAttribute("chat_lieu") ChatLieu chat_lieu, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            _hasError = 1;
            System.out.println("Có lỗi");
            model.addAttribute("pageData", _pageData);
            model.addAttribute("error", _hasError);
            return "/quan-ly/chat_lieu/index_chat_lieu";
        }
        int size = chatLieuDao.findAll().size();

        String nguoiTao = "admin1234";

        ChatLieu newChatLieu = new ChatLieu();
        newChatLieu.setTenChatLieu(chat_lieu.getTenChatLieu());
        newChatLieu.setTrangThai(1);
        newChatLieu.setNguoiTao(nguoiTao);

        chatLieuDao.save(newChatLieu);
        _messageSuccessAddChatLieu = "Thêm chất liệu thành công";
        return "redirect:/admin/chat-lieu/index-chat-lieu";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") int id, Model model)
    {
        ChatLieu chatLieuDetail = chatLieuDao.findById(id).get();
        model.addAttribute("chat_lieu", chatLieuDetail);

        return "/quan-ly/chat_lieu/update_chat_lieu";
    }

    @PostMapping("/update_chat_lieu/{id}")
    public String updateForm(@PathVariable("id") ChatLieu chatLieuRq, @Valid @ModelAttribute("chat_lieu") ChatLieu chat_lieu, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors())
        {
            System.out.println("Có lỗi");
            return "/quan-ly/chat_lieu/update_chat_lieu";
        }

        chatLieuRq.setTenChatLieu(chat_lieu.getTenChatLieu());

        chatLieuDao.save(chatLieuRq);

        return "redirect:/admin/chat-lieu/index-chat-lieu";
    }

    @PostMapping("/search")
    public String search(@RequestParam("param") String search, @RequestParam Optional<Integer> pageNumber, Model model)
    {
        _pageNumber = pageNumber.orElse(0);
        Pageable pageable = PageRequest.of(_pageNumber, 5);
        Page<ChatLieu> pageDataSearch = chatLieuDao.findAllByTenChatLieuIsLike(search, pageable);
        model.addAttribute("pageData", pageDataSearch);

        model.addAttribute("chat_lieu", new ChatLieu());
        return "/quan-ly/chat_lieu/index_chat_lieu";
    }

}
