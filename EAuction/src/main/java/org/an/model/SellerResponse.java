package org.an.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SellerResponse implements Serializable {
	private static final long serialVersionUID = 1L;
	private Seller seller;
	private Product product;
	private Response response;
}
