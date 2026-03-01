package com.borneo.ecommerce.service;

import com.borneo.ecommerce.dto.ProductDTO;
import com.borneo.ecommerce.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProductMapper {
  ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

  @Mapping(source = "category.id", target = "categoryId")
  ProductDTO toDTO(Product product);

}
