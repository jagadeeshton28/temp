package org.an.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

import org.an.common.EAuctionUtil;
import org.an.entity.BuyerEntity;
import org.an.entity.ProductEntity;
import org.an.model.BidResponse;
import org.an.model.BuyerRequest;
import org.an.model.BuyerResponse;
import org.an.model.EAuctionConstants;
import org.an.model.Product;
import org.an.model.ProductsResponse;
import org.an.model.Response;
import org.an.model.SellerRequest;
import org.an.model.SellerResponse;
import org.an.repository.EAuctionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.MapUtils;

import reactor.core.publisher.Mono;

@Service
public class EAuctionService {
	
	@Autowired
	EAuctionUtil eAuctionUtil;
	
	@Autowired
	EAuctionRepository eAuctionRepository;
	
	
	public Map<Integer, String> saveProductValidation(SellerRequest sellerRequest) {
		Map<Integer, String> errorMap = new HashMap<Integer, String>();
		if (sellerRequest == null) {
			errorMap.put(EAuctionConstants.INVALID_REQUEST, EAuctionConstants.INVALID_SELLER_REQUEST);
		} else if (sellerRequest.getProduct() == null) {
			errorMap.put(EAuctionConstants.INVALID_REQUEST, EAuctionConstants.INVALID_PRODUCT_REQUEST);
		}
		 

		else if (!eAuctionUtil.validateProductCategory(sellerRequest.getProduct().getCategory())) {
			errorMap.put(EAuctionConstants.INVALID_REQUEST, EAuctionConstants.INVALID_PRODUCT_CATEGORY);

		} else if (!eAuctionUtil.validatePrice(sellerRequest.getProduct().getStartingPrice())) {
			errorMap.put(EAuctionConstants.INVALID_REQUEST, EAuctionConstants.INVALID_PRODUCT_STARTING_PRICE);

		} else if (sellerRequest.getProduct().getProductName() == null
				|| sellerRequest.getProduct().getProductName().trim().length() < 3
				|| sellerRequest.getProduct().getProductName().length() > 30) {
			errorMap.put(EAuctionConstants.INVALID_REQUEST, EAuctionConstants.INVALID_PRODUCT_NAME);
		}

		else if (sellerRequest.getSeller().getFirstName() == null
				|| sellerRequest.getSeller().getFirstName().length() < 5
				|| sellerRequest.getSeller().getFirstName().length() > 30) {
			errorMap.put(EAuctionConstants.INVALID_REQUEST, EAuctionConstants.INVALID_SELLER_FIRST_NAME);
		} else if (sellerRequest.getSeller().getLastName() == null
				|| sellerRequest.getSeller().getLastName().length() < 3
				|| sellerRequest.getSeller().getLastName().length() > 25) {
			errorMap.put(EAuctionConstants.INVALID_REQUEST, EAuctionConstants.INVALID_SELLER_LAST_NAME);
		} else if (!eAuctionUtil.validatePhoneNo(sellerRequest.getSeller().getPhone())) {
			errorMap.put(EAuctionConstants.INVALID_REQUEST, EAuctionConstants.INVALID_SELLER_LAST_NAME);
		} else if (!eAuctionUtil.validEmail(sellerRequest.getSeller().getEmail())) {
			errorMap.put(EAuctionConstants.INVALID_REQUEST, EAuctionConstants.INVALID_SELLER_LAST_NAME);

		} else {
			
			if (sellerRequest.getProduct().getBidEndDate() != null) {
				if (!eAuctionUtil.validateDate(sellerRequest.getProduct().getBidEndDate())) {

					errorMap.put(EAuctionConstants.INVALID_REQUEST,
							EAuctionConstants.INVALID_SELLER_PRODUCT_BID_ENDATE);

				} else {
					String bidEndDate = sellerRequest.getProduct().getBidEndDate();

					Date bed = eAuctionUtil.getParsedDate(bidEndDate);
					Date cd = eAuctionUtil.getParsedDate(eAuctionUtil.getFormatLocalDate(eAuctionUtil.getLocalDate()));

					if (bed.before(cd)) {
						errorMap.put(EAuctionConstants.INVALID_REQUEST,
								EAuctionConstants.INVALID_SELLER_PRODUCT_BID_ENDATE);
					}
				}

			} else {
				errorMap.put(EAuctionConstants.INVALID_REQUEST, EAuctionConstants.INVALID_SELLER_PRODUCT_BID_ENDATE_NULL);
			}

		}

		return errorMap;
	}
	
	public Map<Integer, String> bidForProductValidation(BuyerRequest buyerRequest) {
		Map<Integer, String> errorMap = new HashMap<Integer, String>();
		if (buyerRequest == null) {
			errorMap.put(EAuctionConstants.INVALID_REQUEST, EAuctionConstants.INVALID_BUYER_REQUEST);
		} 

		else if (buyerRequest.getFirstName() == null
				|| buyerRequest.getFirstName().length() < 5
				|| buyerRequest.getFirstName().length() > 30) {
			errorMap.put(EAuctionConstants.INVALID_REQUEST, EAuctionConstants.INVALID_BUYER_FIRST_NAME);
		} else if (buyerRequest.getLastName() == null
				|| buyerRequest.getLastName().length() < 3
				|| buyerRequest.getLastName().length() > 25) {
			errorMap.put(EAuctionConstants.INVALID_REQUEST, EAuctionConstants.INVALID_BUYER_LAST_NAME);
		} else if (!eAuctionUtil.validatePhoneNo(buyerRequest.getPhone())) {
			errorMap.put(EAuctionConstants.INVALID_REQUEST, EAuctionConstants.INVALID_BUYER_PHONE_NUMBER);
		} else if (!eAuctionUtil.validEmail(buyerRequest.getEmail())) {
			errorMap.put(EAuctionConstants.INVALID_REQUEST, EAuctionConstants.INVALID_BUYER_EMAIL);

		}
		 else  if(!eAuctionUtil.validatePrice(buyerRequest.getBidAmount()))
		 {
			 errorMap.put(EAuctionConstants.INVALID_REQUEST, EAuctionConstants.INVALID_BID_AMT);
		 }
		return errorMap;
	}
	
	public Mono<SellerResponse> saveProduct(SellerRequest sellerRequest) {

		Map<Integer, String> errorMap = saveProductValidation(sellerRequest);

		if (MapUtils.isEmpty(errorMap)) {

			ProductEntity pe = eAuctionUtil.getProductEntity(sellerRequest);

			Mono<ProductEntity> ppe = eAuctionRepository.saveProductEntity(pe);

			return ppe.flatMap(pr -> {

				SellerResponse sr = eAuctionUtil.getProductResponse(pr);
				sr.setResponse(eAuctionUtil.getSucessResponse(EAuctionConstants.PRODUCT_CREATED));
				return Mono.just(sr);
			});
		} else {
			SellerResponse sr = new SellerResponse();
			Response errResp = eAuctionUtil.getFailureResponse(EAuctionConstants.INVALID_REQUEST,
					errorMap.get(EAuctionConstants.INVALID_REQUEST));
			sr.setResponse(errResp);
			return Mono.just(sr);

		}

	}
	
	public Mono<ProductsResponse>  getAllProducts(){
		List<ProductEntity>prdList=eAuctionRepository.getAllProducts();
		Response res=new Response();
		ProductsResponse pr=new ProductsResponse();
			if(prdList!=null && prdList.size()>0) {
				List<Product>lstProduct=new ArrayList<Product>();
				prdList.parallelStream().forEach(p->{
					lstProduct.add(eAuctionUtil.getProduct(p));
				});
				pr.setProducts(lstProduct);
				res.setStatusCode(EAuctionConstants.SUCCESS);
				res.setMsg(EAuctionConstants.PRODUCT_RETRIEVED);
			}else {
				res.setStatusCode(EAuctionConstants.INVALID_SERVER_REQUEST);
				res.setMsg(EAuctionConstants.PRODUCT_NOT_RETRIEVED);
			}
			pr.setResponse(res);
			
			return Mono.just(pr);
		

	}
	
	public Mono<BidResponse> getAllBidsForProduct(String productId) {
		List<ProductEntity> productDetails =eAuctionRepository.getProductDetails(productId);
		BidResponse bidResponse=null;
		Response resp=new Response();
		
		if(productDetails!=null && productDetails.size()>0) {
			
			ProductEntity pe=productDetails.get(0);
			
			if(pe!=null && pe.getBuyerEntitys()!=null) {
				
				bidResponse=eAuctionUtil.getBidResponse(pe);
				resp.setStatusCode(EAuctionConstants.SUCCESS);
				resp.setMsg(EAuctionConstants.PRODUCT_BIDS_RETRIEVE);
				bidResponse.setResponse(resp);
			}else {
				bidResponse=eAuctionUtil.getBidResponse(pe);
				resp.setStatusCode(EAuctionConstants.SUCCESS);
				resp.setMsg(EAuctionConstants.PRODUCT_BIDS_NOT_RETRIEVED);
				bidResponse.setResponse(resp);
			}
		}else {
			bidResponse=new BidResponse();
			resp.setStatusCode(EAuctionConstants.INVALID_SERVER_REQUEST);
			resp.setMsg(EAuctionConstants.INVALID_PRODUCT);
			bidResponse.setResponse(resp);
		}
		
		return Mono.just(bidResponse);
		
	}
	
	public Mono<Response> deleteProduct(String productId) {
		List<ProductEntity> productDetails =eAuctionRepository.getProductDetails(productId);
		Response resp=new Response();
		if(productDetails!=null && productDetails.size()>0) {
			
			ProductEntity pe=productDetails.get(0);
			Date bidDate=eAuctionUtil.getParsedDate(pe.getBidEndDate());
			LocalDate ld=eAuctionUtil.getLocalDate();
			 String localDateString=eAuctionUtil.getFormatLocalDate(ld);
			 Date currentDate=eAuctionUtil.getParsedDate(localDateString);
			if(pe!=null && !currentDate.after(bidDate) && (pe.getBuyerEntitys()==null || pe.getBuyerEntitys().size()==0)) {
				resp.setMsg(EAuctionConstants.PRODUCT_DELETED);
				eAuctionRepository.deleteProduct(productId);
				resp.setStatusCode(200);
				
			}else {
				resp.setMsg(EAuctionConstants.INVALID_PRODUCT_BID_CURRENT_DATE);
				resp.setStatusCode(EAuctionConstants.INVALID_SERVER_REQUEST);
			}
		}else {
			resp.setMsg(EAuctionConstants.INVALID_PRODUCT_FOR_DELETION);
			resp.setStatusCode(EAuctionConstants.INVALID_SERVER_REQUEST);
		}
		return Mono.just(resp);
		
	}
	
	public Mono<BuyerResponse> bidForProduct(BuyerRequest buyerRequest) {

		BuyerResponse br = null;

		Map<Integer, String> errorMap = bidForProductValidation(buyerRequest);

		if (MapUtils.isEmpty(errorMap)) {

			List<ProductEntity> productDetails = eAuctionRepository.getProductDetails(buyerRequest.getProductId());

			if (productDetails != null && productDetails.size() > 0) {

				ProductEntity pe = productDetails.get(0);
				Date bidDate = eAuctionUtil.getParsedDate(pe.getBidEndDate());
				LocalDate ld = eAuctionUtil.getLocalDate();
				String localDateString = eAuctionUtil.getFormatLocalDate(ld);
				Date currentDate = eAuctionUtil.getParsedDate(localDateString);

				if (!currentDate.after(bidDate)) {
					BuyerEntity be = eAuctionUtil.getBuyerEntity(buyerRequest);
					if (pe.getBuyerEntitys() != null) {

						Optional<BuyerEntity> matchingObject = pe.getBuyerEntitys().stream()
								.filter(p -> p.getEmail().equals(buyerRequest.getEmail())).findFirst();
						if (matchingObject.isEmpty()) {
							pe.getBuyerEntitys().add(be);
							Response res = new Response();
							br = updateBuyer(res, pe, be);
						} else {
							br = new BuyerResponse();
							Response errResp = eAuctionUtil.getFailureResponse(EAuctionConstants.INVALID_SERVER_REQUEST,
									EAuctionConstants.INVALID_PRODUCT_BID_TWICE);
							br.setResponse(errResp);
						}
					} else {
						Response res = new Response();
						List<BuyerEntity> bee = new ArrayList<BuyerEntity>();
						bee.add(be);
						pe.setBuyerEntitys(bee);
						br = updateBuyer(res, pe, be);
					}

				} else {
					br = new BuyerResponse();
					Response errResp = eAuctionUtil.getFailureResponse(EAuctionConstants.INVALID_SERVER_REQUEST,
							EAuctionConstants.INVALID_PRODUCT_BID_DATE);
					br.setResponse(errResp);
				}

			} else {
				br = new BuyerResponse();

				Response errResp = eAuctionUtil.getFailureResponse(EAuctionConstants.INVALID_SERVER_REQUEST,
						EAuctionConstants.INVALID_PRODUCT_BID_ID);
				br.setResponse(errResp);
			}
			return Mono.justOrEmpty(br);
		} else {
			br = new BuyerResponse();
			Response errResp = eAuctionUtil.getFailureResponse(EAuctionConstants.INVALID_REQUEST,
					errorMap.get(EAuctionConstants.INVALID_REQUEST));
			br.setResponse(errResp);
			return Mono.justOrEmpty(br);
		}

	}

	private BuyerResponse updateBuyer(Response res, ProductEntity pe, BuyerEntity be) {
		BuyerResponse br;
		eAuctionRepository.saveProductEntity(pe);
		br = eAuctionUtil.getBuyerResponse(be);
		br.getBuyer().setProductId(pe.getProductId());
		br.getBuyer().setProductName(pe.getProductName());
		res.setMsg(EAuctionConstants.PRODUCT_AVAILABLE_BID);
		res.setStatusCode(EAuctionConstants.SUCCESS);
		br.setResponse(res);
		return br;
	}
	public Map<Integer, String> updateBuyerProductAmtValidation( String buyerEmailId, String newBidAmount) {
		
		 Map<Integer, String>  map=new HashMap<Integer, String>();
		 
		 if(!eAuctionUtil.validEmail(buyerEmailId))
		 {
			 map.put(EAuctionConstants.INVALID_REQUEST, EAuctionConstants.INVALID_BUYER_EMAIL);
		 }
		 else  if(!eAuctionUtil.validatePrice(newBidAmount))
		 {
			 map.put(EAuctionConstants.INVALID_REQUEST, EAuctionConstants.INVALID_BID_AMT);
		 }
		 return map;
	}
	public Mono<ProductsResponse> getAllSellerProducts(String sellerEmailId) {

        List<ProductEntity>prdList=eAuctionRepository.getAllProducts();




        

         Response res=new Response();

         ProductsResponse pr=new ProductsResponse();

                       if(prdList!=null && prdList.size()>0) {

                                      List<Product>lstProduct=new ArrayList<Product>();

                                     

                                      Predicate<ProductEntity> pe=

                                                                 

                                                                  ppe->ppe.getSellerEntity()!=null && ppe.getSellerEntity().getEmail()!=null && ppe.getSellerEntity().getEmail()

                                                                  .equals(sellerEmailId);

                                     prdList.parallelStream().filter(pe).forEach(p->{

                                                    lstProduct.add(eAuctionUtil.getProduct(p));

                                      });

                                     

                                      if(lstProduct!=null && lstProduct.size()>0) {

                                                    pr.setProducts(lstProduct);

                                     res.setStatusCode(EAuctionConstants.SUCCESS);

                                     res.setMsg(EAuctionConstants.SELLER_PRODUCT_RETRIEVED);

                                      }

                                      else {

                                                    res.setStatusCode(EAuctionConstants.INVALID_SERVER_REQUEST);

                                                   res.setMsg(EAuctionConstants.SELLER_PRODUCT_NOT_RETRIEVED);

                                      }

                       }else {

                                     res.setStatusCode(EAuctionConstants.INVALID_SERVER_REQUEST);

                                    res.setMsg(EAuctionConstants.SELLER_PRODUCT_NOT_RETRIEVED);

                       }

                       pr.setResponse(res);

                      

                       return Mono.just(pr);

        

}

	public Mono<BuyerResponse> updateBuyerProductAmt(String productId, String buyerEmailId, String newBidAmount) {
		Map<Integer, String>  map =  updateBuyerProductAmtValidation(buyerEmailId,newBidAmount);

		BuyerResponse br = null;

		if (MapUtils.isEmpty(map)) {
			List<ProductEntity> productDetails = eAuctionRepository.getProductDetails(productId);
			//Response res = new Response();
			br = new BuyerResponse();
			if (productDetails != null && productDetails.size() > 0) {

				ProductEntity pe = productDetails.get(0);

				if (pe.getBuyerEntitys() != null && pe.getBuyerEntitys().size() > 0) {

					Predicate<BuyerEntity> emailPredicate = ep -> ep != null && ep.getEmail() != null
							&& ep.getEmail().equals(buyerEmailId);

					/*
					 * pe.getBuyerEntitys().forEach(p->{ if(p.getEmail().equals(buyerEmailId)) {
					 * p.setBidAmount(newBidAmount); } });
					 */
					// BuyerEntity be=null;
					/*
					 * for(BuyerEntity b: pe.getBuyerEntitys()) {
					 * 
					 * if(b.getEmail().equals(buyerEmailId)) { b.setBidAmount(newBidAmount); be=b; }
					 * }
					 */

					Optional<BuyerEntity> be = pe.getBuyerEntitys().stream().filter(emailPredicate).findFirst();

					if (be.get() != null) {

						pe.getBuyerEntitys().stream().forEach(p -> {
							if (p.getEmail().equals(buyerEmailId)) {
								//p.setBidAmount(Double.parseDouble(newBidAmount));
								p.setBidAmount(newBidAmount);
							}
						});
						eAuctionRepository.saveProductEntity(pe);

						br = eAuctionUtil.getBuyerResponse(be.get());
						br.getBuyer().setProductId(pe.getProductId());
						br.getBuyer().setProductName(pe.getProductName());

						Response resp = eAuctionUtil.getFailureResponse(EAuctionConstants.SUCCESS,
								EAuctionConstants.UPDATED_BUYER_DETAILS);
						br.setResponse(resp);
					} else {

						Response resp = eAuctionUtil.getFailureResponse(EAuctionConstants.INVALID_SERVER_REQUEST,
								EAuctionConstants.INVALID_BUYER_EMAIL);
						br.setResponse(resp);
					}
				} else {

					Response resp = eAuctionUtil.getFailureResponse(EAuctionConstants.INVALID_SERVER_REQUEST,
							EAuctionConstants.INVALID_BUYER_DETAILS);
					br.setResponse(resp);
				}

			 
			} else {
				// br=new BuyerResponse();
				Response resp = eAuctionUtil.getFailureResponse(EAuctionConstants.INVALID_REQUEST,
						EAuctionConstants.INVALID_BUYER_EMAIL);
				br.setResponse(resp);
			}

		} else {
			br=new BuyerResponse();
			Response resp = eAuctionUtil.getFailureResponse(EAuctionConstants.INVALID_REQUEST,
					map.get(EAuctionConstants.INVALID_REQUEST));
			br.setResponse(resp);
		}
		return Mono.just(br);
	}
}
