package com.borneo.ecommerce.service;

import com.borneo.ecommerce.dto.ProductDTO;
import com.borneo.ecommerce.model.Category;
import com.borneo.ecommerce.model.Product;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-02-25T01:00:58+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 23 (Oracle Corporation)"
)
public class ProductMapperImpl implements ProductMapper {

    @Override
    public ProductDTO toDTO(Product product) {
        if ( product == null ) {
            return null;
        }

        ProductDTO productDTO = new ProductDTO();

        productDTO.setCategoryId( productCategoryId( product ) );
        productDTO.setId( product.getId() );
        productDTO.setName( product.getName() );
        productDTO.setDescription( product.getDescription() );
        productDTO.setPrice( product.getPrice() );
        productDTO.setStock( product.getStock() );
        productDTO.setImagePath( product.getImagePath() );
        productDTO.setBarcodeNumber( product.getBarcodeNumber() );

        return productDTO;
    }

    @Override
    public Product toEntity(ProductDTO productDTO) {
        if ( productDTO == null ) {
            return null;
        }

        Product product = new Product();

        product.setCategory( productDTOToCategory( productDTO ) );
        product.setId( productDTO.getId() );
        product.setName( productDTO.getName() );
        product.setDescription( productDTO.getDescription() );
        product.setPrice( productDTO.getPrice() );
        if ( productDTO.getStock() != null ) {
            product.setStock( productDTO.getStock() );
        }
        product.setImagePath( productDTO.getImagePath() );
        product.setBarcodeNumber( productDTO.getBarcodeNumber() );

        return product;
    }

    private Long productCategoryId(Product product) {
        if ( product == null ) {
            return null;
        }
        Category category = product.getCategory();
        if ( category == null ) {
            return null;
        }
        Long id = category.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    protected Category productDTOToCategory(ProductDTO productDTO) {
        if ( productDTO == null ) {
            return null;
        }

        Category category = new Category();

        category.setId( productDTO.getCategoryId() );

        return category;
    }
}
