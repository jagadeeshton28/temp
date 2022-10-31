package org.an.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BuyerResponse implements Serializable{
	private static final long serialVersionUID = 1L;
	/*
	 * private String firstName; private String lastName; private String address;
	 * private String city; private String state; private String pin; private String
	 * phone; private String email; private String productId; private String bidAmt;
	 */
	private Buyer buyer;
	private Response response;
}
