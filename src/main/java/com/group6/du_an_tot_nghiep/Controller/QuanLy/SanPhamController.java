package com.group6.du_an_tot_nghiep.Controller.QuanLy;

import com.group6.du_an_tot_nghiep.Dao.SanPhamDao;
import com.group6.du_an_tot_nghiep.Dto.KhachHang.ThongTinGioHang;
import com.group6.du_an_tot_nghiep.Dto.QuanLy.SanPham.*;
import com.group6.du_an_tot_nghiep.Entities.KichThuoc;
import com.group6.du_an_tot_nghiep.Entities.MauSac;
import com.group6.du_an_tot_nghiep.Entities.SanPham;
import com.group6.du_an_tot_nghiep.Entities.TaiKhoan;
import com.group6.du_an_tot_nghiep.Service.QuanLy.KichThuocService;
import com.group6.du_an_tot_nghiep.Service.QuanLy.MauSacService;
import com.group6.du_an_tot_nghiep.Service.QuanLy.SanPhamChiTietService;
import com.group6.du_an_tot_nghiep.Service.QuanLy.SanPhamService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

@Controller
@RequestMapping("/admin/san-pham")
public class SanPhamController {

    @Autowired
    SanPhamService sanPhamService;

    @Autowired
    MauSacService mauSacService;

    @Autowired
    KichThuocService kichThuocService;

    @Autowired
    private SanPhamChiTietService sanPhamChiTietService;

    int _pageNumber;

    @Autowired
    SanPhamSearchFilterRequest _sanPhamSearchFilterRequest;

    Page<SanPhamResponse> _pageData = null;
    int _deleteResult = 0;

    private List<Integer> _idMauSac = new ArrayList<>();
    private List<SanPhamRequest> _sanPham = new ArrayList<>();
    private List<String> _images = new ArrayList<>();

    @GetMapping("/index")
    public String indexSanPham(Model model, @RequestParam Optional<Integer> pageNumber) {
        _pageNumber = pageNumber.orElse(0);
        _pageData = sanPhamService.findAllSanPham(_pageNumber);
        model.addAttribute("searchFilterRequest", _sanPhamSearchFilterRequest);
        model.addAttribute("pageData", _pageData);
        model.addAttribute("deleteRequest", new SanPhamDeleteRequest());
        model.addAttribute("deleteResult", _deleteResult);
        _deleteResult = 0;
        return "/quan-ly/san-pham/index-san-pham";
    }

    @GetMapping("/read-file/{id}")
    @ResponseBody
    public ResponseEntity<ByteArrayResource> readFile (@PathVariable("id") int id){
        try {
            String photo = sanPhamService.findUrlAnhMacDinh(id);
            Path fileName = Paths.get("D:\\du_an_tot_nghiep\\src\\main\\resources\\uploads", photo);
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



    @PostMapping("/search-filter")
    public String searchByTen(Model model, @Valid @ModelAttribute("searchFilterRequest") SanPhamSearchFilterRequest request, BindingResult bindingResult) {
        _sanPhamSearchFilterRequest = request;
        if (bindingResult.hasErrors()) {
            model.addAttribute("pageData", _pageData);
            model.addAttribute("deleteRequest", new SanPhamDeleteRequest());
            _deleteResult = 0;
            model.addAttribute("deleteResult", _deleteResult);
            return "/quan-ly/san-pham/index-san-pham";
        } else {
            _pageData = sanPhamService.findByTenAndTrangThai(request.getTen(),request.getTrangThai());
            model.addAttribute("pageData", _pageData);
            model.addAttribute("deleteRequest", new SanPhamDeleteRequest());
            _deleteResult = 0;
            model.addAttribute("deleteResult", _deleteResult);
            return "/quan-ly/san-pham/index-san-pham";
        }
    }


    @PostMapping("/delete")
    public String deleteSanPham(Model model,@Valid @ModelAttribute("deleteRequest") SanPhamDeleteRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("pageData", _pageData);
            model.addAttribute("searchFilterRequest", _sanPhamSearchFilterRequest);
            _deleteResult = 2;
            model.addAttribute("deleteResult", _deleteResult);
            return "/quan-ly/san-pham/index-san-pham";
        }else {
            sanPhamService.doiTrangThaiSanPham(request.getListId());
            _deleteResult = 1;
            return "redirect:/admin/san-pham/index";
        }
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("listSanPham", _sanPham);
        SanPhamSave sanPhamSave = new SanPhamSave();
        sanPhamSave.setListSP(_sanPham);
        model.addAttribute("sanPhamSave", sanPhamSave);
        model.addAttribute("listMauSac", _idMauSac);
        return "/quan-ly/san-pham/create-san-pham";
    }

    @GetMapping("/update-ten-sp")
    public ResponseEntity<Boolean> updateTenSp(@RequestParam int idSp, @RequestParam String tenSp){
        Boolean result = sanPhamService.updateTenSp(idSp,tenSp);
        try {
            return ResponseEntity.ok()
                    .body(result);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("sanPham") SanPhamRequest sanPhamRequest) {
        _idMauSac = sanPhamRequest.getMauSac();
        for (int i = 0; i < _idMauSac.size(); i++){
            for (int j = 0; j < sanPhamRequest.getKichCo().size(); j++){
                _sanPham.add(new SanPhamRequest(sanPhamRequest.getTen(), sanPhamRequest.getDanhMuc(), sanPhamRequest.getThuongHieu(), sanPhamRequest.getDeGiay(), sanPhamRequest.getChatLieu(), sanPhamRequest.getMoTa(), sanPhamRequest.getMauSac().get(i), sanPhamRequest.getKichCo().get(j)));
            }
        }
        return "redirect:/admin/san-pham/create";
    }

    @PostMapping("/save-product")
    public String saveProduct(@ModelAttribute("sanPhamSave") SanPhamSave sanPhamSave){
        sanPhamSave.getListSP();
        return "redirect:/admin/san-pham/create";
    }

    @PostMapping("/save-image/{id}")
    public String saveImage (List<MultipartFile> files, @PathVariable("id") int idColor) {
        Path path = Paths.get("D:\\du_an_tot_nghiep\\src\\main\\resources\\uploads\\");

        try {
            List<String> imagesForColor = new ArrayList<>(); // Danh sách ảnh cho màu sắc hiện tại

            // Lưu file vào thư mục
            for (MultipartFile file : files) {
                if (file.getOriginalFilename() != null && !file.getOriginalFilename().isEmpty()) {
                    InputStream inputStream = file.getInputStream();
                    Files.copy(inputStream, path.resolve(file.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);
                    imagesForColor.add(file.getOriginalFilename()); // Thêm ảnh vào danh sách riêng
                }
            }

            // Cập nhật ảnh cho màu sắc tương ứng
            for (SanPhamRequest sanPham : _sanPham) {
                if (sanPham.getIdMauSac() == idColor) {
                    sanPham.setImages(imagesForColor);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/admin/san-pham/create";
    }

    @GetMapping("/read-image/{url}")
    @ResponseBody
    public ResponseEntity<ByteArrayResource> readImage (@PathVariable("url") String url){
        try {
            String photo =  url;
            Path fileName = Paths.get("D:\\du_an_tot_nghiep\\src\\main\\resources\\uploads", photo);
            byte[] buffer = Files.readAllBytes(fileName);
            ByteArrayResource byteArrayResource = new ByteArrayResource(buffer);
            return ResponseEntity.ok()
                    .contentLength(buffer.length)
                    .contentType(MediaType.parseMediaType("image/png"))
                    .body(byteArrayResource);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @PostMapping("/delete-image")
    public String deleteImage(@ModelAttribute("sanPhamSave") SanPhamSave sanPhamSave, @RequestParam("idColor") int idColor){
        for (SanPhamRequest sanPham : _sanPham) {
            if (sanPham.getIdMauSac() == idColor) {
                List<String> currentImages = sanPham.getImages();
                if (currentImages != null) {
                    for (String image : sanPhamSave.getListImgDelete()) {
                        currentImages.removeIf(img -> img.equalsIgnoreCase(image));
                    }
                }
                break;
            }
        }
        return "redirect:/admin/san-pham/create";
    }

    @PostMapping("/save-all")
    public String saveProductDetail(@ModelAttribute("sanPhamSave") SanPhamSave sanPhamSave){
        for (SanPhamRequest sanPhamRequest: sanPhamSave.getListSP()) {
            sanPhamRequest.setChatLieu(_sanPham.get(0).getChatLieu());
            sanPhamRequest.setDeGiay(_sanPham.get(0).getDeGiay());
            sanPhamRequest.setDanhMuc(_sanPham.get(0).getDanhMuc());
            sanPhamRequest.setThuongHieu(_sanPham.get(0).getThuongHieu());
            sanPhamRequest.setMoTa(_sanPham.get(0).getMoTa());
            for (SanPhamRequest sanPhamRequest1:_sanPham) {
                if (sanPhamRequest1.getImages() != null && sanPhamRequest.getIdMauSac() == sanPhamRequest1.getIdMauSac()) {
                    sanPhamRequest.setImages(sanPhamRequest1.getImages());
                    break;
                }
            }
        }

        sanPhamService.saveAll(sanPhamSave);
        return "redirect:/admin/san-pham/create";
    }
}
