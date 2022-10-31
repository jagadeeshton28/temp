package org.an.repository;

import org.an.entity.ProductEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
//@EnableScan
public interface ProductRepository  extends CrudRepository<ProductEntity, String>{
		
		//Optional<ProductEntity> findByProductId(String productId);

	}
