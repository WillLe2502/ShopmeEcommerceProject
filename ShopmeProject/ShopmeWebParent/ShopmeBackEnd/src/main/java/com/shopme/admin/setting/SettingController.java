package com.shopme.admin.setting;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.FileUploadUtil;
import com.shopme.common.entity.Currency;
import com.shopme.common.entity.setting.Setting;

@Controller
public class SettingController {
	
	@Autowired
	private SettingService service;
	
	@Autowired
	private CurrencyRepository currencyRepo;
	
	@GetMapping("/settings")
	public String listAll(Model model) {
		
		List<Setting> listSettings = service.listAllSettings();
		List<Currency> listCurrencies = currencyRepo.findAllByOrderByNameAsc();
		
		model.addAttribute("listCurrencies", listCurrencies);
		
		for(Setting setting : listSettings) {
			model.addAttribute(setting.getKey(), setting.getValue());
		}
		return "settings/settings";
	}
	
	@PostMapping("/settings/save_general")
	public String saveGeneralSettings(@RequestParam("fileImage")MultipartFile multipartFile,
									  HttpServletRequest request,
									  RedirectAttributes ra) throws IOException {
		
		GeneralSettingBag settingBag = service.getGeneralSetting();
		
		saveSiteLogo(multipartFile, settingBag);
		saveCurrencySymbol(request, settingBag);
		updateSettingValuesFromForm(request, settingBag.list());
		
		ra.addFlashAttribute("message", "General settings have been saved.");
			
		return "redirect:/settings";
	}

	private void saveSiteLogo(MultipartFile multipartFile, GeneralSettingBag settingBag) throws IOException {
		if(!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			String value = "/site-logo/" + fileName;
			settingBag.updateSiteLogo(value);
			
			String uploadDir = "../site-logo/";
			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
		}
	}
	
	private void saveCurrencySymbol(HttpServletRequest request, GeneralSettingBag settingBag) {
		Integer currencyId = Integer.parseInt(request.getParameter("CURRENCY_ID"));
		Optional<Currency> findByIdResult = currencyRepo.findById(currencyId);
		
		if(findByIdResult.isPresent()) {
			Currency currency = findByIdResult.get();
			settingBag.updateCurrencySymbol(currency.getSymbol());
		}
	}
	
	private void updateSettingValuesFromForm(HttpServletRequest request, List<Setting> listSettings) {
		for(Setting setting : listSettings) {
			String value = request.getParameter(setting.getKey());
			if(value != null) {
				setting.setValue(value);
			}
		}
		
		service.saveAll(listSettings);
	}
	
	@PostMapping("/settings/save_mail_server")
	public String saveMailServerSettings(HttpServletRequest request,
			  							 RedirectAttributes ra) {
		List<Setting> mailServerSettings = service.getMailServerSettings();
		updateSettingValuesFromForm(request, mailServerSettings);
		
		ra.addFlashAttribute("message", "Mail server settings have been saved");
		
		return "redirect:/settings";
		
	}
	
	@PostMapping("/settings/save_mail_template")
	public String saveMailTemplatesSettings(HttpServletRequest request,
			  							    RedirectAttributes ra) {
		List<Setting> mailTempaltesSettings = service.getMailTemplatesSettings();
		updateSettingValuesFromForm(request, mailTempaltesSettings);
		
		ra.addFlashAttribute("message", "Mail template have been saved");
		
		return "redirect:/settings";
		
	}
	
	@PostMapping("/settings/save_payment")
	public String savePaymentSettings(HttpServletRequest request,
			  				  		  RedirectAttributes ra) {
		List<Setting> paymentSettings = service.getPaymenSettings();
		updateSettingValuesFromForm(request, paymentSettings);
		
		ra.addFlashAttribute("message", "Payment setting have been saved");
		return "redirect:/settings";
	}
}
