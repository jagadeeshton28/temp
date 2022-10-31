package org.an.entity;

import java.io.Serializable;
import java.util.List;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import lombok.Data;
@Data
@DynamoDBDocument
@DynamoDBTable(tableName = "product")
public class ProductEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;
	@DynamoDBHashKey(attributeName = "product_id")
	private String productId;
	@DynamoDBAttribute(attributeName = "product_name")
	private String productName;
	@DynamoDBAttribute(attributeName = "short_description")
	private String shortDescription;
	@DynamoDBAttribute(attributeName = "category")
	private String category;
	@DynamoDBAttribute(attributeName = "starting_price")
	private String startingPrice;
	@DynamoDBAttribute(attributeName = "bid_end_date")
	private String bidEndDate;
	
	@DynamoDBAttribute(attributeName="sellerEntity")
	private SellerEntity sellerEntity;
	@DynamoDBAttribute(attributeName="buyerEntitys")
	private  List<BuyerEntity> buyerEntitys;

}
