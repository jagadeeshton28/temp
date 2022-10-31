package org.an.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product implements Serializable {
	private static final long serialVersionUID = 1L;
	
	
	private String productId;
	/*
	 * @NotNull
	 * 
	 * @Min(value = 5)
	 * 
	 * @Max(value = 30)
	 */
	private String productName;
	private String shortDescription;
	private String category;
	private String startingPrice;
	private String bidEndDate;

}
