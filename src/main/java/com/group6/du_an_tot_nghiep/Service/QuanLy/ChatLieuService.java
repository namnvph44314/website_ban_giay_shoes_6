package com.group6.du_an_tot_nghiep.Service.QuanLy;

import com.group6.du_an_tot_nghiep.Dao.ChatLieuDao;
import com.group6.du_an_tot_nghiep.Dto.QuanLy.ChatLieu.ChatLieuResponse;
import com.group6.du_an_tot_nghiep.Entities.ChatLieu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatLieuService {

    @Autowired
    ChatLieuDao chatLieuDao;

   public Page<ChatLieuResponse> findAllChatLieu(int pageNumber)
   {
       Pageable pageable = PageRequest.of(pageNumber, 5);
       Page<ChatLieu> pageData = chatLieuDao.findAllByTrangThaiOrTrangThaiOrderByIdDesc(ChatLieuDao.DANG_BAN, ChatLieuDao.NGUNG_BAN, pageable);
       Page<ChatLieuResponse> pageDataResponse = pageData.map(ChatLieuResponse::convertToResponse);

       return pageDataResponse;
   }

   public List<ChatLieu> findAllByTrangThai(){
       return chatLieuDao.findAllByTrangThai(ChatLieuDao.DANG_BAN);
   }


}
