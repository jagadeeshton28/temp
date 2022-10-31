package org.an.controller;

import javax.validation.Valid;

import org.an.model.BidResponse;
import org.an.model.BuyerRequest;
import org.an.model.BuyerResponse;
import org.an.model.ProductsResponse;
import org.an.model.Response;
import org.an.model.SellerRequest;
import org.an.model.SellerResponse;
import org.an.service.EAuctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
public class EAuctionController {
	
	@Autowired
	private EAuctionService eAuctionService;
	@PostMapping(value="/api/v1/seller/add-product",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Mono<SellerResponse> saveProduct(@Valid @RequestBody SellerRequest sellerRequest) {
		
		return eAuctionService.saveProduct(sellerRequest);
	}
	@GetMapping(value="/api/v1/seller/products",  produces = MediaType.APPLICATION_JSON_VALUE)
	public Mono<ProductsResponse> getAllBidsForProduct() {
		
		return eAuctionService.getAllProducts();
		
	}
	
	 @GetMapping(value="/api/v1/seller/products/{sellerEmail}",  produces = MediaType.APPLICATION_JSON_VALUE)

     public Mono<ProductsResponse> getSellerProducts(@PathVariable String sellerEmail) {

                   

                    return eAuctionService.getAllSellerProducts(sellerEmail);

                   

     }
	@GetMapping(value="/api/v1/seller/show-bids/{productId}",  produces = MediaType.APPLICATION_JSON_VALUE)
	public Mono<BidResponse> getAllBidsForProduct(@PathVariable String productId) {
		
		return eAuctionService.getAllBidsForProduct(productId);
		
	}
	
	
	
	@DeleteMapping(value="/api/v1/seller/delete/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Mono<Response> deleteProduct(@PathVariable String productId) {
		
		return  eAuctionService.deleteProduct(productId);
		
	}
	
	
	
	
	@PutMapping(value="/api/v1/buyer/place-bid",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Mono<BuyerResponse> bidForProduct(@Valid  @RequestBody BuyerRequest buyerRequest) {
		
		return  eAuctionService.bidForProduct(buyerRequest);
		
	}
	
	@PutMapping(value="/api/v1/bid/{productId}/{buyerEmailId}/{newBidAmount}",produces = MediaType.APPLICATION_JSON_VALUE)
	public Mono<BuyerResponse> updateBuyerProduct(@PathVariable String productId,@PathVariable String buyerEmailId,@PathVariable String newBidAmount) {
		
		return  eAuctionService.updateBuyerProductAmt(productId,buyerEmailId,newBidAmount);
		
	}

}
