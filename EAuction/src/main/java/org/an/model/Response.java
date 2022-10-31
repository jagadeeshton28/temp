package org.an.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response implements Serializable {
	private static final long serialVersionUID = 1L;
	private String msg;
	private int statusCode;
}
