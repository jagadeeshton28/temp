package org.an.model;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BidResponse  implements Serializable{
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private Product product;
private List<Buyer> buyers;
private Response response;
}
