package org.an.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Seller implements Serializable {

	private static final long serialVersionUID = 1L;
	/*
	 * @NotNull
	 * 
	 * @Min(value = 5)
	 * 
	 * @Max(value = 30)
	 */
	private String firstName;
	/*
	 * @NotNull
	 * 
	 * @Min(value = 3)
	 * 
	 * @Max(value = 25)
	 */
	private String lastName;
	private String address;
	private String city;
	private String state;
	private String pin;
	/*
	 * @NotNull
	 * 
	 * @Min(value = 10)
	 * 
	 * @Max(value = 10)
	 */
	
	private String phone;
	/*
	 * @NotNull
	 * 
	 * @Email(regexp = "[a-zA-Z0-9_]+@[a-zA-Z0-9]+\\.[a-zA-Z]{3}")
	 */
	private String email;
}
