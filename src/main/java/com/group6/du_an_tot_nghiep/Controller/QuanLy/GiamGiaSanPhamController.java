package com.group6.du_an_tot_nghiep.Controller.QuanLy;

import com.group6.du_an_tot_nghiep.Dao.GiamGiaSanPhamDao;
import com.group6.du_an_tot_nghiep.Dao.SanPhamDao;
import com.group6.du_an_tot_nghiep.Dto.QuanLy.GiamGiaSanPham.GiamGiaSanPhamFilter;
import com.group6.du_an_tot_nghiep.Dto.QuanLy.GiamGiaSanPham.GiamGiaSanPhamRequest;
import com.group6.du_an_tot_nghiep.Dto.QuanLy.GiamGiaSanPham.GiamGiaSanPhamUpdate;
import com.group6.du_an_tot_nghiep.Dto.QuanLy.GiamGiaSanPhamChiTiet.GiamGiaSanPhamChiTietAdd;
import com.group6.du_an_tot_nghiep.Dto.QuanLy.GiamGiaSanPhamChiTiet.GiamGiaSanPhamChiTietFillter;
import com.group6.du_an_tot_nghiep.Dto.QuanLy.PhieuGiamGia.PhieuGiamGiaFilter;
import com.group6.du_an_tot_nghiep.Dto.QuanLy.PhieuGiamGia.PhieuGiamGiaRequest;
import com.group6.du_an_tot_nghiep.Dto.QuanLy.PhieuGiamGiaKhachHang.PhieuGiamGiaKhachHangAdd;
import com.group6.du_an_tot_nghiep.Entities.GiamGiaSanPham;
import com.group6.du_an_tot_nghiep.Entities.SanPhamChiTiet;
import com.group6.du_an_tot_nghiep.Service.QuanLy.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Controller
@RequestMapping("/admin/giam-gia-san-pham")
public class GiamGiaSanPhamController {

    @Autowired
    GiamGiaSanPhamService giamGiaSanPhamService;
    @Autowired
    SanPhamDao sanPhamDao;
    @Autowired
    SanPhamChiTietService sanPhamChiTietService;
    @Autowired
    SanPhamService sanPhamService;
    @Autowired
    GiamGiaSanPhamChiTietService giamGiaSanPhamChiTietService;
    @Autowired
    MauSacService mauSacService;
    @Autowired
    ChatLieuService chatLieuService;
    @Autowired
    KichThuocService kichThuocService;
    @Autowired
    DeGiayService deGiayService;
    @Autowired
    ThuongHieuService thuongHieuService;
    @Autowired
    GiamGiaSanPhamDao giamGiaSanPhamDao;

    private String nguoiTao = "namnv";
    int checkPage = 0;
    boolean checkSearch = true;
    boolean checkLoc = true;
    boolean checkAdd = true;
    Integer _idGgsp=null;

    GiamGiaSanPhamUpdate _giamGiaSanPhamUpdate= new GiamGiaSanPhamUpdate();

    @GetMapping("/index")
    public String index(
            Model model,
            @RequestParam("page") Optional<Integer> pageParam
    ) {
        checkSearch = true;
        int page = pageParam.orElse(0);
        checkPage = 0;
        checkLoc = true;
        model.addAttribute("checkLoc", checkLoc);
        model.addAttribute("p", checkPage);
        model.addAttribute("ggspFilter", new GiamGiaSanPhamFilter(null, null, -1));
        model.addAttribute("lstGgsp", giamGiaSanPhamService.getAllPage(page));
        model.addAttribute("url", "/admin/giam-gia-san-pham/index");

        return "quan_ly/giam_gia_san_pham/index";
    }

    @GetMapping("/loc")
    public String loc(
            Model model,
            @RequestParam("page") Optional<Integer> pageParam,
            @Valid @ModelAttribute("ggspFilter") GiamGiaSanPhamFilter ggspFilter,
            BindingResult result
    ) {
        checkSearch = true;
        if (result.hasErrors()) {
            int page = pageParam.orElse(0);
            checkPage = 0;
            model.addAttribute("p", checkPage);
            model.addAttribute("lstGgsp", giamGiaSanPhamService.getAllPage(page));
            model.addAttribute("url", "/admin/giam-gia-san-pham/index");
            return "quan_ly/giam_gia_san_pham/index";
        }

        if (ggspFilter.getNgayBatDau().trim().equals("") && !ggspFilter.getNgayKetThuc().trim().equals("")) {
            checkLoc = false;
            model.addAttribute("checkLoc", checkLoc);
            model.addAttribute("nbd", "Vui lòng nhập ngày bắt đầu");
            int page = pageParam.orElse(0);
            checkPage = 0;
            model.addAttribute("p", checkPage);
            model.addAttribute("lstGgsp", giamGiaSanPhamService.getAllPage(page));
            model.addAttribute("url", "/admin/giam-gia-san-pham/index");
            return "quan_ly/giam_gia_san_pham/index";
        }
        if (!ggspFilter.getNgayBatDau().trim().equals("") && ggspFilter.getNgayKetThuc().trim().equals("")) {
            checkLoc = false;
            model.addAttribute("checkLoc", checkLoc);
            model.addAttribute("nkt", "Vui lòng nhập ngày kết thúc");
            int page = pageParam.orElse(0);
            checkPage = 0;
            model.addAttribute("p", checkPage);
            model.addAttribute("lstGgsp", giamGiaSanPhamService.getAllPage(page));
            model.addAttribute("url", "/admin/giam-gia-san-pham/index");
            return "quan_ly/giam_gia_san_pham/index";
        }

        String url = "/admin/giam-gia-san-pham/loc?ngayBatDau=" + ggspFilter.getNgayBatDau() + "&ngayKetThuc=" + "&trangThai=" + ggspFilter.getTrangThai();
        if (ggspFilter.getNgayBatDau().trim().equals("")) {
            ggspFilter.setNgayBatDau(null);
        }
        if (ggspFilter.getNgayKetThuc().trim().equals("")) {
            ggspFilter.setNgayKetThuc(null);
        }
        if (ggspFilter.getTrangThai() == 4) {
            ggspFilter.setTrangThai(null);
        }
        int page = pageParam.orElse(0);
        checkPage = 0;
        checkLoc = true;
        model.addAttribute("checkLoc", checkLoc);
        model.addAttribute("p", checkPage);
        model.addAttribute("ggspFilter", ggspFilter);
        model.addAttribute("lstGgsp", giamGiaSanPhamService.loc(ggspFilter, page));
        model.addAttribute("url", url);
        model.addAttribute("checkboxValues", null);
        return "quan_ly/giam_gia_san_pham/index";
    }

    @GetMapping("/search")
    public String search(
            Model model,
            @RequestParam("search") String param,
            @RequestParam("page") Optional<Integer> pageParam
    ) {
        if (param == null || param.trim().equals("")) {
            checkSearch = false;
            checkLoc = true;
            model.addAttribute("checkSearch", checkSearch);
            model.addAttribute("messageSr", "Vui lòng nhập tên giảm giá cần tìm");
            int page = pageParam.orElse(0);
            checkPage = 0;
            model.addAttribute("checkLoc", checkLoc);
            model.addAttribute("p", checkPage);
            model.addAttribute("ggspFilter", new GiamGiaSanPhamFilter(null, null, 3));
            model.addAttribute("lstGgsp", giamGiaSanPhamService.getAllPage(page));
            model.addAttribute("url", "/admin/giam-gia-san-pham/index");
            return "quan_ly/giam_gia_san_pham/index";
        }
        checkSearch = true;
        checkPage = 2;
        checkLoc = true;
        model.addAttribute("checkLoc", checkLoc);
        model.addAttribute("p", checkPage);
        model.addAttribute("search", param);
        model.addAttribute("lstGgsp", giamGiaSanPhamService.search(param));
        model.addAttribute("ggspFilter", new GiamGiaSanPhamFilter(null, null, 3));
        return "quan_ly/giam_gia_san_pham/index";
    }

    @GetMapping("/add")
    public String add(
            Model model,
            @RequestParam("page") Optional<Integer> pageparam,
            @RequestParam("pageCtsp") Optional<Integer> pageCtspParam,
            @RequestParam(name = "checkboxValues", required = false) List<String> checkboxValues
    ) {
        List<Integer> lstIdSp = new ArrayList<>();
        List<Integer> lstIdSpct = new ArrayList<>();
        int page = pageparam.orElse(0);
        int pageCtsp = pageCtspParam.orElse(0);
        GiamGiaSanPhamRequest giamGiaSanPhamRequest = new GiamGiaSanPhamRequest(null, 0, null, null);
        model.addAttribute("ggspRequest", giamGiaSanPhamRequest);
        if (checkboxValues != null) {
            for (String ids : checkboxValues) {
                lstIdSp.add(Integer.parseInt(ids));
            }
            for (SanPhamChiTiet spct : sanPhamChiTietService.getPageSpctByIdSp(lstIdSp, pageCtsp).getContent()) {
                lstIdSpct.add(spct.getId());
            }
            model.addAttribute("lstIdSpct", lstIdSpct);
            model.addAttribute("lstId", lstIdSp);
            model.addAttribute("lstSpct", sanPhamChiTietService.getPageSpctByIdSp(lstIdSp, pageCtsp));
        } else {
            lstIdSp = new ArrayList<>();
            model.addAttribute("lstIdSpct", new ArrayList<>());
            model.addAttribute("lstSpct", sanPhamChiTietService.getPageSpctByIdSp(lstIdSp, pageCtsp));
        }

        model.addAttribute("lstSp", giamGiaSanPhamService.findAllSpByTrangThai(sanPhamDao.DANG_BAN, page));
        model.addAttribute("checkboxValues", checkboxValues);
        addAttributeThuocTinh(model);
        return "quan_ly/giam_gia_san_pham/add";
    }

    @GetMapping("/add-search")
    public String searchKhBySdt(
            Model model,
            @RequestParam("page") Optional<Integer> pageparam,
            @RequestParam("pageCtsp") Optional<Integer> pageCtspParam,
            @RequestParam(name = "tenSp", required = false, defaultValue = "") String tenSp,
            @ModelAttribute("ggspRequest") GiamGiaSanPhamRequest giamGiaSanPhamRequest,
            @RequestParam(name = "checkboxValues", required = false) List<String> checkboxValues
    ) {
        int page = pageparam.orElse(0);
        int pageCtsp = pageCtspParam.orElse(0);

        List<Integer> lstIdSp = new ArrayList<>();
        List<Integer> lstIdSpct = new ArrayList<>();

        if (checkboxValues != null) {
            for (String ids : checkboxValues) {
                lstIdSp.add(Integer.parseInt(ids));
            }
            for (SanPhamChiTiet spct : sanPhamChiTietService.getPageSpctByIdSp(lstIdSp, pageCtsp).getContent()) {
                lstIdSpct.add(spct.getId());
            }
            model.addAttribute("lstIdSpct", lstIdSpct);
            model.addAttribute("lstId", lstIdSp);
            model.addAttribute("lstSpct", sanPhamChiTietService.getPageSpctByIdSp(lstIdSp, pageCtsp));
        }
        if (checkboxValues == null) {
            lstIdSp = new ArrayList<>();
            model.addAttribute("lstIdSpct", lstIdSpct);

            model.addAttribute("lstSpct", sanPhamChiTietService.getPageSpctByIdSp(lstIdSp, pageCtsp));
        }
        if (tenSp.trim().equals("")) {
            if (checkboxValues == null) {
                model.addAttribute("lstIdSpct", lstIdSpct);
            }
            model.addAttribute("ggspRequest", giamGiaSanPhamRequest);
            model.addAttribute("tenSp", tenSp);
            model.addAttribute("checkboxValues", checkboxValues);
            model.addAttribute("lstSp", giamGiaSanPhamService.findAllSpByTrangThai(sanPhamDao.DANG_BAN, page));
            addAttributeThuocTinh(model);
            return "quan_ly/giam_gia_san_pham/add";
        }
        model.addAttribute("ggspRequest", giamGiaSanPhamRequest);
        model.addAttribute("tenSp", tenSp);
        model.addAttribute("lstSp", sanPhamService.getPageSpByTen(tenSp, page));
        model.addAttribute("checkboxValues", checkboxValues);
        addAttributeThuocTinh(model);
        return "quan_ly/giam_gia_san_pham/add";
    }

    @PostMapping("/store")
    public String add(
            Model model,
            @Valid @ModelAttribute("ggspRequest") GiamGiaSanPhamRequest giamGiaSanPhamRequest,
            BindingResult result,
            @RequestParam(name = "checkboxValuesSpct", required = false) List<String> checkboxValuesSpct,
            @RequestParam("page") Optional<Integer> pageparam,
            @RequestParam("pageCtsp") Optional<Integer> pageCtspParam,
            @RequestParam(name = "checkboxValues", required = false) List<String> checkboxValues
    ) {
        if (result.hasErrors()) {
            List<Integer> lstIdSp = new ArrayList<>();
            List<Integer> lstIdSpct = new ArrayList<>();
            int page = pageparam.orElse(0);
            int pageCtsp = pageCtspParam.orElse(0);
            model.addAttribute("ggspRequest", giamGiaSanPhamRequest);
            if (checkboxValues != null) {
                for (String ids : checkboxValues) {
                    lstIdSp.add(Integer.parseInt(ids));
                }
                for (SanPhamChiTiet spct : sanPhamChiTietService.getPageSpctByIdSp(lstIdSp, pageCtsp).getContent()) {
                    lstIdSpct.add(spct.getId());
                }
                model.addAttribute("lstIdSpct", lstIdSpct);
                model.addAttribute("lstId", lstIdSp);
                model.addAttribute("lstSpct", sanPhamChiTietService.getPageSpctByIdSp(lstIdSp, pageCtsp));
            } else {
                lstIdSp = new ArrayList<>();
                model.addAttribute("lstIdSpct", new ArrayList<>());
                model.addAttribute("lstSpct", sanPhamChiTietService.getPageSpctByIdSp(lstIdSp, pageCtsp));
            }

            model.addAttribute("lstSp", giamGiaSanPhamService.findAllSpByTrangThai(sanPhamDao.DANG_BAN, page));
            model.addAttribute("checkboxValues", checkboxValues);
            addAttributeThuocTinh(model);
            return "quan_ly/giam_gia_san_pham/add";

        }
        giamGiaSanPhamService.add(giamGiaSanPhamRequest, nguoiTao);
        if (checkboxValuesSpct != null) {
            int idGgsp = giamGiaSanPhamService.findTopByOrderByIdDesc().getId();
            List<Integer> lstIdSpct = new ArrayList<>();
            for (String idCtsp : checkboxValuesSpct) {
                lstIdSpct.add(Integer.parseInt(idCtsp));
            }
            giamGiaSanPhamChiTietService.add(lstIdSpct, idGgsp, nguoiTao);
        }
        model.addAttribute("checkboxValues", null);
        addAttributeThuocTinh(model);
        return "redirect:/admin/giam-gia-san-pham/index";
    }

    @GetMapping("/add-loc")
    public String loc(
            Model model,
            @RequestParam("page") Optional<Integer> pageparam,
            @RequestParam("pageCtsp") Optional<Integer> pageCtspParam,
            @RequestParam(name = "checkboxValues", required = false) List<String> checkboxValues,
            @ModelAttribute("ggspRequest") GiamGiaSanPhamRequest giamGiaSanPhamRequest,
            @RequestParam(name = "mauSac", required = false) String mauSacParam,
            @RequestParam(name = "chatLieu", required = false) String chatLieuParam,
            @RequestParam(name = "kichCo", required = false) String kichCoParam,
            @RequestParam(name = "deGiay", required = false) String deGiayParam,
            @RequestParam(name = "thuongHieu", required = false) String thuongHieuParam
    ) {
        Integer mauSac = Integer.parseInt(mauSacParam);
        Integer chatLieu = Integer.parseInt(chatLieuParam);
        Integer kichCo = Integer.parseInt(kichCoParam);
        Integer deGiay = Integer.parseInt(deGiayParam);
        Integer thuongHieu = Integer.parseInt(thuongHieuParam);

        if (mauSac == 0) {
            mauSac = null;
        }
        if (chatLieu == 0) {
            chatLieu = null;
        }
        if (kichCo == 0) {
            kichCo = null;
        }
        if (deGiay == 0) {
            deGiay = null;
        }
        if (thuongHieu == 0) {
            thuongHieu = null;
        }

        List<Integer> lstIdSp = new ArrayList<>();
        List<Integer> lstIdSpct = new ArrayList<>();
        int page = pageparam.orElse(0);
        int pageCtsp = pageCtspParam.orElse(0);
        model.addAttribute("ggspRequest", giamGiaSanPhamRequest);
        if (checkboxValues != null) {
            for (String ids : checkboxValues) {
                lstIdSp.add(Integer.parseInt(ids));
            }
            for (SanPhamChiTiet spct : sanPhamChiTietService.getPageSpctByIdSp(lstIdSp, pageCtsp).getContent()) {
                lstIdSpct.add(spct.getId());
            }
            model.addAttribute("lstIdSpct", lstIdSpct);
            model.addAttribute("lstId", lstIdSp);
            model.addAttribute("lstSpct", sanPhamChiTietService.loc(lstIdSp, mauSac, chatLieu, kichCo, deGiay, thuongHieu, pageCtsp));
        } else {
            lstIdSp = new ArrayList<>();
            model.addAttribute("lstIdSpct", new ArrayList<>());
            model.addAttribute("lstSpct", sanPhamChiTietService.getPageSpctByIdSp(lstIdSp, pageCtsp));
        }

        model.addAttribute("lstSp", giamGiaSanPhamService.findAllSpByTrangThai(sanPhamDao.DANG_BAN, page));
        model.addAttribute("checkboxValues", checkboxValues);
        addAttributeThuocTinh(model);

        return "quan_ly/giam_gia_san_pham/add";
    }

    @GetMapping("/detail/{id}")
    public String detail(
            Model model,
            @PathVariable("id") int idGgsp,
            @RequestParam("page") Optional<Integer> pageparam,
            @RequestParam("pageCtsp") Optional<Integer> pageCtspParam,
            @RequestParam("pageGgspctByGgsp") Optional<Integer> pageGgspctByGgspParam,
            @RequestParam(name = "checkboxValues", required = false) List<String> checkboxValues
    ) {
        _giamGiaSanPhamUpdate.setTen(giamGiaSanPhamDao.findById(idGgsp).get().getTenGiamGia());
        _giamGiaSanPhamUpdate.setGiaTri(giamGiaSanPhamDao.findById(idGgsp).get().getGiaTri());
        _giamGiaSanPhamUpdate.setNgayBatDau(giamGiaSanPhamDao.findById(idGgsp).get().getNgayBatDau()+"");
        _giamGiaSanPhamUpdate.setNgayKetThuc(giamGiaSanPhamDao.findById(idGgsp).get().getNgayKetThuc()+"");
        _idGgsp=idGgsp;
        List<Integer> lstIdSp = new ArrayList<>();
        List<Integer> lstIdSpct = new ArrayList<>();
        int page = pageparam.orElse(0);
        int pageCtsp = pageCtspParam.orElse(0);
        int pageGgspctByGgsp = pageGgspctByGgspParam.orElse(0);
        GiamGiaSanPhamUpdate giamGiaSanPhamUpdate = giamGiaSanPhamService.findById(idGgsp);
        model.addAttribute("ggspUpdate", giamGiaSanPhamUpdate);
        if (checkboxValues != null) {
            for (String ids : checkboxValues) {
                lstIdSp.add(Integer.parseInt(ids));
            }
            for (SanPhamChiTiet spct : sanPhamChiTietService.getPageSpctByIdSp(lstIdSp, pageCtsp).getContent()) {
                lstIdSpct.add(spct.getId());
            }
            model.addAttribute("lstIdSpct", lstIdSpct);
            model.addAttribute("lstId", lstIdSp);
            model.addAttribute("lstSpct", giamGiaSanPhamChiTietService.getListSpctByIdGgspChuaAdd(lstIdSp, idGgsp, pageCtsp));
        } else {
            lstIdSp = new ArrayList<>();
            model.addAttribute("lstIdSpct", new ArrayList<>());
            model.addAttribute("lstSpct", giamGiaSanPhamChiTietService.getListSpctByIdGgspChuaAdd(lstIdSp, idGgsp, pageCtsp));
        }
        model.addAttribute("lstGgspctByIdGgsp", giamGiaSanPhamChiTietService.getListGgspctByIdGgsp(idGgsp, pageGgspctByGgsp));
        model.addAttribute("lstSp", giamGiaSanPhamService.findAllSpByTrangThai(sanPhamDao.DANG_BAN, page));
        model.addAttribute("checkboxValues", checkboxValues);
        model.addAttribute("idGgsp", idGgsp);
        addAttributeThuocTinh(model);
        return "quan_ly/giam_gia_san_pham/detail";
    }

    @GetMapping("/detail-loc/{id}")
    public String detailFillter(
            Model model,
            @PathVariable("id") int idGgsp,
            @RequestParam("page") Optional<Integer> pageparam,
            @RequestParam("pageCtsp") Optional<Integer> pageCtspParam,
            @RequestParam("pageGgspctByGgsp") Optional<Integer> pageGgspctByGgspParam,
            @RequestParam(name = "checkboxValues", required = false) List<String> checkboxValues,
            @ModelAttribute("ggspUpdate") GiamGiaSanPhamUpdate giamGiaSanPhamUpdate,
            @RequestParam(name = "mauSac", required = false) String mauSacParam,
            @RequestParam(name = "chatLieu", required = false) String chatLieuParam,
            @RequestParam(name = "kichCo", required = false) String kichCoParam,
            @RequestParam(name = "deGiay", required = false) String deGiayParam,
            @RequestParam(name = "thuongHieu", required = false) String thuongHieuParam
    ) {
        Integer mauSac = Integer.parseInt(mauSacParam);
        Integer chatLieu = Integer.parseInt(chatLieuParam);
        Integer kichCo = Integer.parseInt(kichCoParam);
        Integer deGiay = Integer.parseInt(deGiayParam);
        Integer thuongHieu = Integer.parseInt(thuongHieuParam);

        if (mauSac == 0) {
            mauSac = null;
        }
        if (chatLieu == 0) {
            chatLieu = null;
        }
        if (kichCo == 0) {
            kichCo = null;
        }
        if (deGiay == 0) {
            deGiay = null;
        }
        if (thuongHieu == 0) {
            thuongHieu = null;
        }

        List<Integer> lstIdSp = new ArrayList<>();
        List<Integer> lstIdSpct = new ArrayList<>();
        int page = pageparam.orElse(0);
        int pageCtsp = pageCtspParam.orElse(0);
        int pageGgspctByGgsp = pageGgspctByGgspParam.orElse(0);

        if (checkboxValues != null) {
            for (String ids : checkboxValues) {
                lstIdSp.add(Integer.parseInt(ids));
            }
            for (SanPhamChiTiet spct : sanPhamChiTietService.getPageSpctByIdSp(lstIdSp, pageCtsp).getContent()) {
                lstIdSpct.add(spct.getId());
            }
            model.addAttribute("lstIdSpct", lstIdSpct);
            model.addAttribute("lstId", lstIdSp);
            model.addAttribute("lstSpct", giamGiaSanPhamChiTietService.loc(idGgsp, lstIdSp, mauSac, chatLieu, kichCo, deGiay, thuongHieu, pageCtsp));
        } else {
            lstIdSp = new ArrayList<>();
            model.addAttribute("lstIdSpct", new ArrayList<>());
            model.addAttribute("lstSpct", giamGiaSanPhamChiTietService.getListSpctByIdGgspChuaAdd(lstIdSp, idGgsp, pageCtsp));
        }

        model.addAttribute("ggspUpdate", _giamGiaSanPhamUpdate);
        model.addAttribute("idGgsp", idGgsp);
        model.addAttribute("lstGgspctByIdGgsp", giamGiaSanPhamChiTietService.getListGgspctByIdGgsp(idGgsp, pageGgspctByGgsp));
        model.addAttribute("lstSp", giamGiaSanPhamService.findAllSpByTrangThai(sanPhamDao.DANG_BAN, page));
        model.addAttribute("checkboxValues", checkboxValues);
        addAttributeThuocTinh(model);

        return "quan_ly/giam_gia_san_pham/detail";
    }

    @GetMapping("/detail-search/{id}")
    public String searchDetail(
            Model model,
            @PathVariable("id") int idGgsp,
            @RequestParam("page") Optional<Integer> pageparam,
            @RequestParam("pageCtsp") Optional<Integer> pageCtspParam,
            @RequestParam("pageGgspctByGgsp") Optional<Integer> pageGgspctByGgspParam,
            @RequestParam(name = "tenSp", required = false, defaultValue = "") String tenSp,
            @ModelAttribute("ggspUpdate") GiamGiaSanPhamUpdate giamGiaSanPhamUpdate,
            @RequestParam(name = "checkboxValues", required = false) List<String> checkboxValues
    ) {
        int page = pageparam.orElse(0);
        int pageCtsp = pageCtspParam.orElse(0);
        int pageGgspctByGgsp = pageGgspctByGgspParam.orElse(0);

        List<Integer> lstIdSp = new ArrayList<>();
        List<Integer> lstIdSpct = new ArrayList<>();

        if (checkboxValues != null) {
            for (String ids : checkboxValues) {
                lstIdSp.add(Integer.parseInt(ids));
            }
            for (SanPhamChiTiet spct : sanPhamChiTietService.getPageSpctByIdSp(lstIdSp, pageCtsp).getContent()) {
                lstIdSpct.add(spct.getId());
            }
            model.addAttribute("lstIdSpct", lstIdSpct);
            model.addAttribute("lstId", lstIdSp);
            model.addAttribute("lstSpct", giamGiaSanPhamChiTietService.getListSpctByIdGgspChuaAdd(lstIdSp, idGgsp, pageCtsp));
//            model.addAttribute("lstSpct", sanPhamChiTietService.getPageSpctByIdSp(lstIdSp, pageCtsp));
        }
        if (checkboxValues == null) {
            lstIdSp = new ArrayList<>();
            model.addAttribute("lstIdSpct", lstIdSpct);
//            model.addAttribute("lstSpct", giamGiaSanPhamChiTietService.getListSpctByIdGgspChuaAdd(lstIdSp, idGgsp, pageCtsp));
            model.addAttribute("lstSpct", sanPhamChiTietService.getPageSpctByIdSp(lstIdSp, pageCtsp));
        }
        if (tenSp.trim().equals("")) {
            if (checkboxValues == null) {
                model.addAttribute("lstIdSpct", lstIdSpct);
            }
            System.out.println("ggspUpdate: "+giamGiaSanPhamUpdate.toString());
            model.addAttribute("tenSp", tenSp);
            model.addAttribute("checkboxValues", checkboxValues);
            model.addAttribute("lstSp", giamGiaSanPhamService.findAllSpByTrangThai(sanPhamDao.DANG_BAN, page));
            addAttributeThuocTinh(model);
            return "quan_ly/giam_gia_san_pham/add";
        }
        model.addAttribute("idGgsp", idGgsp);
        model.addAttribute("lstGgspctByIdGgsp", giamGiaSanPhamChiTietService.getListGgspctByIdGgsp(idGgsp, pageGgspctByGgsp));
        model.addAttribute("ggspUpdate", _giamGiaSanPhamUpdate);
        System.out.println("ggspUpdate: "+giamGiaSanPhamUpdate.toString());
        model.addAttribute("tenSp", tenSp);
        model.addAttribute("lstSp", sanPhamService.getPageSpByTen(tenSp, page));
        model.addAttribute("checkboxValues", checkboxValues);
        addAttributeThuocTinh(model);
        return "quan_ly/giam_gia_san_pham/detail";
    }

    @PostMapping("/update/{id}")
    public String update(
            Model model,
            @PathVariable("id") int idGgsp,
            @Valid @ModelAttribute("ggspUpdate") GiamGiaSanPhamUpdate giamGiaSanPhamUpdate,
            BindingResult result,
            @RequestParam(name = "checkboxValuesSpct", required = false) List<String> checkboxValuesSpct,
            @RequestParam("page") Optional<Integer> pageparam,
            @RequestParam("pageCtsp") Optional<Integer> pageCtspParam,
            @RequestParam("pageGgspctByGgsp") Optional<Integer> pageGgspctByGgspParam,
            @RequestParam(name = "checkboxValues", required = false) List<String> checkboxValues
    ) {
        if (result.hasErrors()) {
            List<Integer> lstIdSp = new ArrayList<>();
            List<Integer> lstIdSpct = new ArrayList<>();
            int page = pageparam.orElse(0);
            int pageCtsp = pageCtspParam.orElse(0);
            int pageGgspctByGgsp = pageGgspctByGgspParam.orElse(0);
            model.addAttribute("ggspUpdate", giamGiaSanPhamUpdate);
            if (checkboxValues != null) {
                for (String ids : checkboxValues) {
                    lstIdSp.add(Integer.parseInt(ids));
                }
                for (SanPhamChiTiet spct : sanPhamChiTietService.getPageSpctByIdSp(lstIdSp, pageCtsp).getContent()) {
                    lstIdSpct.add(spct.getId());
                }
                model.addAttribute("lstIdSpct", lstIdSpct);
                model.addAttribute("lstId", lstIdSp);
                model.addAttribute("lstSpct", giamGiaSanPhamChiTietService.getListSpctByIdGgspChuaAdd(lstIdSp, idGgsp, pageCtsp));
            } else {
                lstIdSp = new ArrayList<>();
                model.addAttribute("lstIdSpct", new ArrayList<>());
                model.addAttribute("lstSpct", giamGiaSanPhamChiTietService.getListSpctByIdGgspChuaAdd(lstIdSp, idGgsp, pageCtsp));
            }
            model.addAttribute("lstGgspctByIdGgsp", giamGiaSanPhamChiTietService.getListGgspctByIdGgsp(idGgsp, pageGgspctByGgsp));
            model.addAttribute("lstSp", giamGiaSanPhamService.findAllSpByTrangThai(sanPhamDao.DANG_BAN, page));
            model.addAttribute("checkboxValues", checkboxValues);
            model.addAttribute("idGgsp", idGgsp);
            addAttributeThuocTinh(model);
            return "quan_ly/giam_gia_san_pham/detail";
        }
        boolean checkGt=true;
        if(giamGiaSanPhamUpdate.getGiaTri()<0 || giamGiaSanPhamUpdate.getGiaTri()>100) {
            checkGt = false;
            model.addAttribute("checkGt", checkGt);
            model.addAttribute("gt", "Vui lòng nhập giá trị trong khoảng từ 0-100");
            List<Integer> lstIdSp = new ArrayList<>();
            List<Integer> lstIdSpct = new ArrayList<>();
            int page = pageparam.orElse(0);
            int pageCtsp = pageCtspParam.orElse(0);
            int pageGgspctByGgsp = pageGgspctByGgspParam.orElse(0);
            model.addAttribute("ggspUpdate", giamGiaSanPhamUpdate);
            if (checkboxValues != null) {
                for (String ids : checkboxValues) {
                    lstIdSp.add(Integer.parseInt(ids));
                }
                for (SanPhamChiTiet spct : sanPhamChiTietService.getPageSpctByIdSp(lstIdSp, pageCtsp).getContent()) {
                    lstIdSpct.add(spct.getId());
                }
                model.addAttribute("lstIdSpct", lstIdSpct);
                model.addAttribute("lstId", lstIdSp);
                model.addAttribute("lstSpct", giamGiaSanPhamChiTietService.getListSpctByIdGgspChuaAdd(lstIdSp, idGgsp, pageCtsp));
            } else {
                lstIdSp = new ArrayList<>();
                model.addAttribute("lstIdSpct", new ArrayList<>());
                model.addAttribute("lstSpct", giamGiaSanPhamChiTietService.getListSpctByIdGgspChuaAdd(lstIdSp, idGgsp, pageCtsp));
            }
            model.addAttribute("lstGgspctByIdGgsp", giamGiaSanPhamChiTietService.getListGgspctByIdGgsp(idGgsp, pageGgspctByGgsp));
            model.addAttribute("lstSp", giamGiaSanPhamService.findAllSpByTrangThai(sanPhamDao.DANG_BAN, page));
            model.addAttribute("checkboxValues", checkboxValues);
            model.addAttribute("idGgsp", idGgsp);
            addAttributeThuocTinh(model);
            return "quan_ly/giam_gia_san_pham/detail";
        }
        giamGiaSanPhamService.update(idGgsp, giamGiaSanPhamUpdate, nguoiTao);
        giamGiaSanPhamChiTietService.updateSpctdaCo(idGgsp,nguoiTao);
        if (checkboxValuesSpct != null) {
            List<Integer> lstIdSpct = new ArrayList<>();
            for (String idCtsp : checkboxValuesSpct) {
                lstIdSpct.add(Integer.parseInt(idCtsp));
            }
            giamGiaSanPhamChiTietService.update(lstIdSpct, idGgsp, nguoiTao);
        }
        model.addAttribute("checkboxValues", null);
        addAttributeThuocTinh(model);
        return "redirect:/admin/giam-gia-san-pham/index" ;
    }

    @GetMapping("/update-trang-thai/{id}")
    public String updateTrangThai(@PathVariable("id") GiamGiaSanPham giamGiaSanPham){
        giamGiaSanPhamService.updateGgspNhd(giamGiaSanPham.getId());
        return "redirect:/admin/giam-gia-san-pham/index";
    }

    @GetMapping("/update-trang-thai-ggsp-sp/{id}")
    public String updateGgspSp(
            @PathVariable("id") Integer idGgspct
    ){
        giamGiaSanPhamChiTietService.updateTtggspct(idGgspct,_idGgsp);
        return "redirect:/admin/giam-gia-san-pham/detail/" + _idGgsp;
    }

    public void addAttributeThuocTinh(Model model) {
        model.addAttribute("lstMauSac", mauSacService.findAllByTrangThai());
        model.addAttribute("lstChatLieu", chatLieuService.findAllByTrangThai());
        model.addAttribute("lstKichCo", kichThuocService.findAllByTrangThai());
        model.addAttribute("lstDeGiay", deGiayService.findAllByTrangThai());
        model.addAttribute("lstThuongHieu", thuongHieuService.findAllByTrangThai());

    }


}
