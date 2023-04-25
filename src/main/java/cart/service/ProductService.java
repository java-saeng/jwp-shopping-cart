package cart.service;

import cart.dao.ProductDao;
import cart.dao.ProductEntity;
import cart.service.dto.ProductModifyRequest;
import cart.service.dto.ProductRegisterRequest;
import cart.service.dto.ProductSearchResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductDao productDao;

    public ProductService(final ProductDao productDao) {
        this.productDao = productDao;
    }

    public void registerProduct(final ProductRegisterRequest productRegisterRequest) {
        ProductEntity productEntity = new ProductEntity(productRegisterRequest.getName(),
                                                        productRegisterRequest.getPrice(),
                                                        productRegisterRequest.getImageUrl());

        productDao.save(productEntity);
    }

    public List<ProductSearchResponse> searchAllProducts() {
        List<ProductEntity> productEntities = productDao.findAll();
        return productEntities.stream()
                              .map(entity -> new ProductSearchResponse(entity.getId(),
                                                                       entity.getName(),
                                                                       entity.getPrice(),
                                                                       entity.getImageUrl()))
                              .collect(Collectors.toList());
    }

    public void modifyProduct(final Long productId, final ProductModifyRequest productModifyRequest) {
        final ProductEntity modifiedProductEntity = new ProductEntity(productId,
                                                              productModifyRequest.getName(),
                                                              productModifyRequest.getPrice(),
                                                              productModifyRequest.getImageUrl());

        productDao.modify(modifiedProductEntity);
    }

    public void deleteProduct(final Long productId) {
        productDao.deleteById(productId);
    }
}