package org.an.entity;

import java.io.Serializable;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@DynamoDBDocument

@AllArgsConstructor
@NoArgsConstructor
public class BuyerEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;
	@DynamoDBAttribute(attributeName = "first_name")
	private String firstName;
	@DynamoDBAttribute(attributeName = "lastName")
	private String lastName;
	@DynamoDBAttribute(attributeName = "address")
	private String address;
	@DynamoDBAttribute(attributeName = "city")
	private String city;
	@DynamoDBAttribute(attributeName = "state")
	private String state;
	@DynamoDBAttribute(attributeName = "pin")
	private String pin;
	@DynamoDBAttribute(attributeName = "phone")
	private String phone;
	@DynamoDBAttribute(attributeName = "email")
	private String email;
	@DynamoDBAttribute(attributeName = "product_id")
	private String productId;
	@DynamoDBAttribute(attributeName = "bid_amount")
	private String bidAmount;
}
