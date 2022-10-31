package org.an.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.an.entity.ProductEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpServerErrorException;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.QueryResultPage;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.DeleteItemSpec;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;

import reactor.core.publisher.Mono;

@Repository
@Configuration
public class EAuctionRepository {

	@Resource(name = "dynamoDBMapper")
	DynamoDBMapper dbMapper;

	@Value("${amazon.aws.accesskey}")
	private String amazonAWSAccessKey;

	@Value("${amazon.aws.secretkey}")
	private String amazonAWSSecretKey;
	 
	/*
	 * @PostConstruct public void after() { System.setProperty("AWS_ACCESS_KEY_ID",
	 * amazonAWSAccessKey); System.setProperty("AWS_SECRET_ACCESS_KEY",
	 * amazonAWSSecretKey); }
	 */
	public Mono<ProductEntity> saveProductEntity(ProductEntity pe){
		
		dbMapper.save(pe);
		
		return Mono.just(pe);
	}

	public List<ProductEntity> getProductDetails(String productCode) {

		List<ProductEntity> productDetails = new ArrayList<ProductEntity>();
		QueryResultPage<ProductEntity> prdList;
		// Expression Attributes
		Map<String, String> expressionAttributeNames = new HashMap<String, String>();
		expressionAttributeNames.put("#product_id", "product_id");
		Map<String, AttributeValue> expressionAttributeValues = new HashMap<String, AttributeValue>();
		expressionAttributeValues.put(":product_id", new AttributeValue().withS(productCode));

		DynamoDBQueryExpression<ProductEntity> retrieveQuery = new DynamoDBQueryExpression<ProductEntity>()
				.withKeyConditionExpression("#product_id=:product_id")
				.withExpressionAttributeNames(expressionAttributeNames)
				.withExpressionAttributeValues(expressionAttributeValues).withScanIndexForward(false);

		try {
			// DynamoDB Query
			prdList = dbMapper.queryPage(ProductEntity.class, retrieveQuery);
			productDetails = prdList.getResults();

		} catch (ResourceNotFoundException e) {
			throw e;
		} catch (HttpServerErrorException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
		return productDetails;
	}

	public List<ProductEntity> getAllProducts() {
		DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();

		List<ProductEntity> prdList = dbMapper.scan(ProductEntity.class, scanExpression);
		
		return prdList;
	}

	public void deleteProduct(String productId) {

		AWSCredentialsProvider credentialsProvider = new AWSStaticCredentialsProvider(
				new BasicAWSCredentials(amazonAWSAccessKey, amazonAWSSecretKey));

		AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withCredentials(credentialsProvider)
				.withRegion("us-east-2").build();

		DynamoDB dynamoDB = new DynamoDB(client);

		Table table = dynamoDB.getTable("product");

		String prdctId = productId;

		DeleteItemSpec deleteItemSpec = new DeleteItemSpec().withPrimaryKey("product_id", prdctId);

		try {
			//System.out.println("Attempting a delete...");
			table.deleteItem(deleteItemSpec);
			//System.out.println("DeleteItem succeeded");
		} catch (Exception e) {
			//System.err.println("Unable to delete item: " + prdctId);
			//System.err.println(e.getMessage());
			e.printStackTrace();
		}
	}

}
