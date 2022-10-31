package org.an;
import javax.annotation.PostConstruct;

import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;

@SpringBootApplication
@EnableDynamoDBRepositories
(basePackages = "org.an")
@Configuration()
@EnableAutoConfiguration 
public class EAuctionApplication {
//http://localhost:8965/v2/api-docs
	@Value("${amazon.dynamodb.endpoint}")
    private String amazonDynamoDBEndpoint;
	
	@Value("${amazon.aws.accesskey}")
    private String amazonAWSAccessKey;
	
	@Value("${amazon.aws.secretkey}")
    private String amazonAWSSecretKey;
	@PostConstruct
	public void after() {
		//System.setProperty("AWS_ACCESS_KEY_ID", amazonAWSAccessKey);
		//System.setProperty("AWS_SECRET_ACCESS_KEY", amazonAWSSecretKey);
	}
	
	/*
	 * @Bean ApplicationRunner applicationRunner(Environment environment) { return
	 * args -> { System.setProperty("AWS_ACCESS_KEY_ID", amazonAWSAccessKey);
	 * System.setProperty("AWS_SECRET_ACCESS_KEY", amazonAWSSecretKey); }; }
	 */
	
	 
		
	@SuppressWarnings("deprecation")
	
	@Bean
	public AmazonDynamoDB amazonDynamoDB() {
		
		
		
		  AmazonDynamoDB amazonDynamoDB = new
		  AmazonDynamoDBClient(amazonAWSCredentials());
		 
//		AmazonDynamoDB amazonDynamoDB=AmazonDynamoDBClientBuilder.standard().withRegion(region)
//				 .withCredentials(new AWSStaticCredentialsProvider(amazonAWSCredentials())).build();
//                 
		  if (!StringUtils.isEmpty(amazonDynamoDBEndpoint)) {
		  amazonDynamoDB.setEndpoint(amazonDynamoDBEndpoint);
		  
		  }
		 return amazonDynamoDB;
		 
	}
	
	@Bean
    public AWSCredentials amazonAWSCredentials() {
        return new BasicAWSCredentials(
          amazonAWSAccessKey, amazonAWSSecretKey);
    }
	
	private static final DynamoDBMapperConfig.TableNameResolver TABLE_NAME_RESOLVER = (classname, config) -> "product";
	ClientConfiguration getClientConfiguration() {
		ClientConfiguration cfg = new ClientConfiguration();
		cfg.setProtocol(Protocol.HTTPS);
		cfg.setProxyPort(8099);
		return cfg;
	}
	
	@Bean(name="dynamoDBMapper")
	public DynamoDBMapper dynamoDBMapperLocal() {
		Regions region = Regions.US_EAST_2;
		DynamoDBMapperConfig dbMapperConfig = new DynamoDBMapperConfig.Builder().withTableNameResolver(TABLE_NAME_RESOLVER).build();
		AmazonDynamoDBClient dynamoClient = getAmazonDynamoDBLocalClient(region);
		return new DynamoDBMapper(dynamoClient, dbMapperConfig);
	}
	
	  private AmazonDynamoDBClient getAmazonDynamoDBLocalClient(Regions region) {
//	  return (AmazonDynamoDBClient)
	 //AmazonDynamoDBClientBuilder.standard().withRegion(region).build(); 
			  AWSCredentialsProvider credentialsProvider = new AWSStaticCredentialsProvider(
						new BasicAWSCredentials(amazonAWSAccessKey, amazonAWSSecretKey));

				AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withCredentials(credentialsProvider)
						.withRegion("us-east-2").build();
				return (AmazonDynamoDBClient)client;
	  }
	 
//	private AmazonDynamoDBClient getAmazonDynamoDBLocalClient(Regions region) {
//		return (AmazonDynamoDBClient) AmazonDynamoDBClientBuilder.standard().withRegion(region).build();
//	}
	
	public static void main(String[] args) {
		SpringApplication.run(EAuctionApplication.class, args);
	}
	
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
			
				
				registry.addMapping("/**").allowedOrigins("http://localhost:8091").allowedMethods("*").maxAge(3600L)
                .allowedHeaders("*")
                .exposedHeaders("Authorization")
                .allowCredentials(true);
			}
		};
	}

 
	/*
	 * @Bean public Class<ProductRepository> ProductRepositoryLocal() { return
	 * ProductRepository.class; }
	 */
	  
}
