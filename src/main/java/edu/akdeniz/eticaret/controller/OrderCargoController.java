package edu.akdeniz.eticaret.controller;

import java.util.List;
import java.util.Locale;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import edu.akdeniz.eticaret.model.OrderModel;
import edu.akdeniz.eticaret.service.EticaretService;

@Controller
public class OrderCargoController {
	
	@Autowired EticaretService eticaretService;
	
	@RequestMapping(value = "/admin/kargodakiSiparisler", method = RequestMethod.GET)
	public String getOrderInCargoList(@RequestParam("pageId") Integer pageId,Locale locale,Model model) {
		Integer total = 2;
		model.addAttribute("currentPage", pageId);
		Integer noOfPages = eticaretService.getOrderListInCargoCount();
		double a = (double)noOfPages / (double)total;
		noOfPages = (int) Math.ceil(a);
		if(pageId==1) {
			
		}
		else {
			 pageId = (pageId-1)*total+1;
		}
		List<OrderModel> orderList = eticaretService.getOrderListInCargo(pageId, total);
		List<OrderModel> stateList = eticaretService.getOrderStates();
		model.addAttribute("noOfPages", noOfPages);
		model.addAttribute("orderList", orderList);
		model.addAttribute("stateList", stateList);
		return "orderCargoList";
		 
	}
	
	@RequestMapping(value = "/admin/orderProductsInCargo", method = RequestMethod.GET)
	public String getOrderProduct(@RequestParam("SiparisID") Integer SiparisID,Model model) {
		List<OrderModel> products = eticaretService.getOrdersInCargo(SiparisID);
		for (OrderModel i : products) {
			try {
				StringBuilder sb = new StringBuilder();
				sb.append("data:image/png;base64,");
				sb.append(StringUtils.newStringUtf8(Base64.encodeBase64(i.getDosyaThumb(), false)));
				i.setDosyaThumbString(sb.toString());
				if(i.getDosyaThumbString().length()<30){
					i.setDosyaThumbString(null);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		model.addAttribute("products", products);
		return "orderProducts";
		
	}
	
	@RequestMapping(value = "/admin/updateStateCargo/{SiparisID}",method = RequestMethod.POST)
	public String updateCargoState(@ModelAttribute("orderCargoModel") OrderModel orderCargoModel,
			@PathVariable("SiparisID") Integer SiparisID,Locale  locale,Model model) {
		eticaretService.orderStateUpdate(orderCargoModel, SiparisID);		
		return "redirect:/admin/orderCargoList?pageId=1";
		
	}
	
	@RequestMapping(value = "/admin/searchCargoOrders",method = RequestMethod.GET)
	public String getsearchCargoOrders(@RequestParam("searchVal") String searchVal,Model model) {
		Integer pageId = 1;
		int total = 2;
		Integer noOfPages = eticaretService.getOrderListInCargoCount();
		double a = (double)noOfPages / (double)total;
		noOfPages = (int) Math.ceil(a);
		if(pageId==1) {
			
		}
		else {
			 pageId = (pageId-1)*total+1;
		}
		List<OrderModel> orderList = eticaretService.searchCargoOrders(searchVal,pageId,total);
		model.addAttribute("searchVal", searchVal);
		model.addAttribute("orderList", orderList);
		model.addAttribute("noOfPages", noOfPages);
		return "orderCargoListSearch";
		
	}
	
	@RequestMapping(value = "/admin/searchCargoOrders",method = RequestMethod.POST)
	public String searchCargoOrders(@RequestParam("searchVal") String searchVal,Model model) {
//		Integer pageId = 1;
//		int total = 2;
//		Integer noOfPages = eticaretService.getOrderListInCargoCount();
//		double a = (double)noOfPages / (double)total;
//		noOfPages = (int) Math.ceil(a);
//		if(pageId==1) {
//			
//		}
//		else {
//			 pageId = (pageId-1)*total+1;
//		}
//		List<OrderModel> orderList = eticaretService.searchCargoOrders(searchVal);
//		model.addAttribute("orderList", orderList);
//		model.addAttribute("noOfPages", noOfPages);
		return "redirect:/admin/searchCargoOrders?searchVal="+searchVal+"&pageId=1";
		
	}
}
