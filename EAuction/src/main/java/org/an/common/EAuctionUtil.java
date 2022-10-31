package org.an.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.an.entity.BuyerEntity;
import org.an.entity.ProductEntity;
import org.an.entity.SellerEntity;
import org.an.model.BidResponse;
import org.an.model.Buyer;
import org.an.model.BuyerRequest;
import org.an.model.BuyerResponse;
import org.an.model.Product;
import org.an.model.Seller;
import org.an.model.SellerRequest;
import org.an.model.SellerResponse;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

@Component
public class EAuctionUtil implements InitializingBean {
	String format="dd-MM-yyyy";
	DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(format);
	SimpleDateFormat sdf=new SimpleDateFormat(format);
	String emailRegex="[a-zA-Z0-9_]+@[a-zA-Z0-9]+\\.[a-zA-Z]{3}";
	
	String doubleval="\\d*.??\\d*?";
	Pattern doublevalPrice=Pattern.compile(doubleval);
	Pattern emailPatter=Pattern.compile(emailRegex);
	Pattern no=Pattern.compile("[0-9]*");
	//List<String> productCategoryList=null;
	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		/*
		 * productCategoryList=new ArrayList<String>();
		 * 
		 * ProductCategory []lstPrdctCatg=ProductCategory.values();
		 * 
		 * Stream.of(lstPrdctCatg).forEach(pc->productCategoryList.add(pc.v));
		 */
		
		sdf.setLenient(false);
		
	}
	
	public boolean validatePrice(String price) {
		boolean validPrice=false;
		
		if(price!=null) {
			
			if(price.contains(".")) {
				
				StringBuilder sb=new StringBuilder(price);
				
				sb.replace(price.indexOf("."),price.indexOf(".")+1, "");
				
				
				if(no.matcher(sb.toString()).matches()) {
					
					validPrice= doublevalPrice.matcher(price).matches();
				}
				
				
			}else {
				if(no.matcher(price.toString()).matches()) {
					
					validPrice= doublevalPrice.matcher(price).matches();
				}
				}
		}
		return validPrice;
	}
	public boolean validEmail(String email) {
		boolean validEmail=false;
		
		if(email!=null) {
			validEmail=emailPatter.matcher(email).matches();
		}
		return validEmail;
	}
	
	
	/*
	 * public boolean validateStartingPrice(String startingPrice) { boolean
	 * validateStartingPrice=false; Pattern p=Pattern.compile("[0-9]*");
	 * if(startingPrice!=null && p.matcher(startingPrice).matches()) {
	 * validateStartingPrice=true;
	 * 
	 * } return validateStartingPrice;
	 * 
	 * }
	 */
	
	public boolean validateProductCategory(String category) {

		boolean categoryb = false;

		if (category != null) {

			switch (category) {

			case "PAINTING":
			case "SCULPTOR":
			case "ORNAMENT":
				categoryb = true;
				break;

			}
		}

		return categoryb;
	}
	
	public boolean validatePhoneNo(String phone) {
		
		boolean bPhone=false;
		if(phone!=null && phone.length()==10) {
			
			 
			 Pattern p=Pattern.compile("\\d{10}");
			 
			 if(p.matcher(phone).matches()) {
				 bPhone=true;
			 }
		}
		
		return bPhone;
		
	}
	
	public boolean validateBidEndDate(String dt) {
		
		boolean endDate=false;
		if(dt!=null) {
			
			
			Date d=getParsedDate(dt);
			
			Date ld=getParsedDate(getFormatLocalDate(getLocalDate()));
			
			if(d.after(ld)) {
				endDate=true;
			}
		}
		
		return endDate;
		
	}
	
	public ProductEntity getProductEntity(Product p) {
		ProductEntity pe=new ProductEntity();
		pe.setBidEndDate(getParsedDateString(getParsedDate(p.getBidEndDate())));
		pe.setCategory(p.getCategory());
		pe.setProductId(p.getProductId());
		pe.setProductName(p.getProductName());
		pe.setShortDescription(p.getShortDescription());
		pe.setStartingPrice(p.getStartingPrice());
		return pe;
	}
	
	public Product getProduct(ProductEntity p) {
		Product pe=new Product();
		pe.setBidEndDate(p.getBidEndDate());
		pe.setCategory(p.getCategory());
		pe.setProductId(p.getProductId());
		pe.setProductName(p.getProductName());
		pe.setShortDescription(p.getShortDescription());
		pe.setStartingPrice(p.getStartingPrice());
		return pe;
	}
	
	public SellerEntity getSellerEntity(Seller s) {
		
		SellerEntity se=new SellerEntity();
		se.setAddress(s.getAddress());
		se.setCity(s.getCity());
		se.setEmail(s.getEmail());
		se.setFirstName(s.getFirstName());
		se.setLastName(s.getLastName());
		se.setPhone(s.getPhone());
		se.setPin(s.getPin());
		se.setState(s.getState());
		return se;
	}
	
	public Seller getSeller(SellerEntity s) {
		
		Seller se=new Seller();
		se.setAddress(s.getAddress());
		se.setCity(s.getCity());
		se.setEmail(s.getEmail());
		se.setFirstName(s.getFirstName());
		se.setLastName(s.getLastName());
		se.setPhone(s.getPhone());
		se.setPin(s.getPin());
		se.setState(s.getState());
		return se;
	}
	public ProductEntity getProductEntity(SellerRequest sr) {
		
		ProductEntity pe= getProductEntity(sr.getProduct());
		pe.setSellerEntity(getSellerEntity(sr.getSeller()));
		return pe;
		
	}
	
	/*
	 * public SellerResponse getFailureSellerResponse(String msg) {
	 * 
	 * SellerResponse sr=new SellerResponse();
	 * 
	 * Response resp=getFailureResponse(msg, 400);
	 * 
	 * sr.setResponse(resp);
	 * 
	 * return sr; }
	 */
	
	
	public SellerResponse getProductResponse(ProductEntity p) {
		
		 SellerResponse ser=new SellerResponse();
		
		 Product ps=getProduct(p);
		 Seller s=getSeller(p.getSellerEntity());
		 ser.setProduct(ps);
		 ser.setSeller(s);
		
		
		return ser;
		
	}
	
	public BuyerEntity getBuyerEntity(Buyer b) {
		BuyerEntity be=new BuyerEntity();
		
		be.setAddress(b.getAddress());
		 
		/*
		 * if(b.getBidAmount()!=null) {
		 * be.setBidAmount(Double.parseDouble(b.getBidAmount())); }
		 */
		be.setBidAmount(b.getBidAmount());
		be.setCity(b.getCity());
		be.setEmail(b.getEmail());
		be.setFirstName(b.getFirstName());
		be.setLastName(b.getLastName());
		be.setPhone(b.getPhone());
		be.setPin(b.getProductId());
		be.setState(b.getState());
		
		return be;
	}
	
	public Buyer getBuyer(BuyerEntity b) {
		Buyer be=new Buyer();
		
		be.setAddress(b.getAddress());
		
		if(b.getBidAmount()!=null)
		{
			be.setBidAmount(String.valueOf(b.getBidAmount()));
		}
		 
		be.setCity(b.getCity());
		be.setEmail(b.getEmail());
		be.setFirstName(b.getFirstName());
		be.setLastName(b.getLastName());
		be.setPhone(b.getPhone());
		be.setPin(b.getPin());
		be.setState(b.getState());
		
		return be;
	}
	
	public BuyerEntity getBuyerEntity(BuyerRequest br) {
		BuyerEntity be=new BuyerEntity();
		be.setAddress(br.getAddress());
		/*
		 * if(br.getBidAmount()!=null)
		 * be.setBidAmount(Double.parseDouble(br.getBidAmount()));
		 */
		be.setBidAmount(br.getBidAmount());
		be.setCity(br.getCity());
		be.setEmail(br.getEmail());
		be.setFirstName(br.getFirstName());
		be.setLastName(br.getLastName());
		be.setPhone(br.getPhone());
		be.setPin(br.getPin());
		be.setState(br.getState());
		return be;
	}
	public BuyerResponse getBuyerProductResponse(BuyerEntity be) {
		BuyerResponse ber=new BuyerResponse();
		
		ber.setBuyer(getBuyer(be));
		
		return ber;
	}
	public BuyerResponse getBuyerResponse(BuyerEntity be) {
		BuyerResponse ber=new BuyerResponse();
		
		ber.setBuyer(getBuyer(be));
		
		return ber;
	}
	
	public BidResponse getBidResponse(ProductEntity p) {
		
		
		BidResponse ber=new BidResponse();
		
		if(p!=null) {
			Product pr=getProduct(p);
			List<Buyer> lstBuyer=new ArrayList<Buyer>();
			List<BuyerEntity> lstBE=p.getBuyerEntitys();
			if(lstBE!=null && lstBE.size()>0 ) {
				Predicate<BuyerEntity> ba=ba1->ba1.getBidAmount()!=null;
				
				
				List < BuyerEntity > lstBeSorted = lstBE.stream().filter(ba).collect(Collectors.toList());
				
				
				Collections.sort(lstBeSorted, new Comparator<BuyerEntity>() {
					
					public int compare(BuyerEntity o1, BuyerEntity o2) {
						 double d1=Double.valueOf(o1.getBidAmount());
						 double d2=Double.valueOf(o2.getBidAmount());
						 if(d2>d1){
							 return 0;
						 }else{
							 return -1;
						 }
					 }
				});
				lstBeSorted.forEach(be->{
					Buyer byer=getBuyer(be);
					byer.setProductId(pr.getProductId());
					byer.setProductName(pr.getProductName());
					lstBuyer.add(byer);
				});
				
				
			}
			ber.setProduct(pr);
			ber.setBuyers(lstBuyer);
			
			
			
		}
		return ber;
	}
	public LocalDate getLocalDate() {
		LocalDate now = LocalDate.now();
		
		return now;
	}
	public boolean validateDate(String dateString) {
		 
		 boolean validDate=false;
		 
		 if(dateString!=null) {
			 
			 Pattern p=Pattern.compile("\\d{2}-\\d{2}-\\d{4}");
			 
			 
			 validDate= p.matcher(dateString).matches();
		 }
		 return validDate;
	}
	public String getFormatLocalDate(LocalDate ld) {
		 
		return dateFormatter.format(ld);
	}
	public String getParsedDateString(Date date) {
		//SimpleDateFormat sdf=new SimpleDateFormat(format);
		return sdf.format(date);
	}
	public Date getParsedDate(String date) {
	//	SimpleDateFormat sdf=new SimpleDateFormat(format);
		
		try {
			return sdf.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public String getFormatDate(Date date) {
		 
		SimpleDateFormat sdf=new SimpleDateFormat(format);
		try {
			return sdf.format(date);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public org.an.model.Response getSucessResponse(String msg){
		
		org.an.model.Response resp=new org.an.model.Response();
		
		resp.setStatusCode(200);
		resp.setMsg(msg);
		return resp;
	}
public org.an.model.Response getFailureResponse(int code,String msg){
		
		org.an.model.Response resp=new org.an.model.Response();
		
		resp.setStatusCode(code);
		resp.setMsg(msg);
		return resp;
	}

}
