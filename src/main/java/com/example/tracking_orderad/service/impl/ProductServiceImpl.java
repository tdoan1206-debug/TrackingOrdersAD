package com.example.tracking_orderad.service.impl;

import com.example.tracking_orderad.config.basicauthconfig.AuthenticationFacade;
import com.example.tracking_orderad.configmapper.ProductMapper;
import com.example.tracking_orderad.configmapper.ProductVariantMapper;
import com.example.tracking_orderad.dto.request.CreateProductRequest;
import com.example.tracking_orderad.dto.request.CreateVariantRequest;
import com.example.tracking_orderad.dto.request.UpdateProductRequest;
import com.example.tracking_orderad.dto.request.UpdateVariantRequest;
import com.example.tracking_orderad.dto.response.*;
import com.example.tracking_orderad.entity.*;
import com.example.tracking_orderad.exception.BusinessException;
import com.example.tracking_orderad.exception.NotFoundException;
import com.example.tracking_orderad.repository.InventoryRepo;
import com.example.tracking_orderad.repository.ProductCategoryRepo;
import com.example.tracking_orderad.repository.ProductRepo;
import com.example.tracking_orderad.repository.ProductVariantRepo;
import com.example.tracking_orderad.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {
    private final ProductRepo productRepo;
    private final ProductMapper productMapper;
    private final ProductVariantMapper productVariantMapper;
    private final ProductCategoryRepo productCategoryRepo;
    private final AuthenticationFacade authenticationFacade;
    private final ProductVariantRepo productVariantRepo;
    private final InventoryRepo inventoryRepo ;

    @Override
    @Transactional(readOnly = true)
    public List<ProductRes> getAll() {
        log.info("Bắt đầu lấy danh sách sản phẩm");
        List<Product> products = productRepo.findAllProduct();

        log.info("Lấy danh sách sản phẩm Thành CÔng");
        return productMapper.toProductResList(products);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDetailRes> getAllAdmin() {
        List<Product> products = productRepo.findAllProduct();
        return products.stream().map(product -> {
            ProductDetailRes res = productMapper.toProductDetailRes(product);
            res.setVariants(productVariantMapper.toProductVariantResList(product.getProductVariants()));
            return res;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDetailRes getById(String id) {
        Product product = productRepo.findProductDetail(id)
                .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND, "Product not found"));

        ProductDetailRes res = productMapper.toProductDetailRes(product);
        // Gắn danh sách variants vào response để frontend biết productVariantId
        res.setVariants(productVariantMapper.toProductVariantResList(product.getProductVariants()));
        return res;
    }


    @Override
    @Transactional
    public CreateProductResponse createProduct(CreateProductRequest request) {
        ProductCategory cat = productCategoryRepo.findById(request.getCategoryId())
                .orElseThrow(() -> new NotFoundException(HttpStatus.BAD_REQUEST, "Danh mục không tồn tại"));
        User user = authenticationFacade.getCurrentUser();

        log.info("BẮt đầu tạo sản phẩm ");
        Product product = new Product();
        product.setName(request.getProductName());
        product.setBasePrice(request.getBasePrice());
        product.setDescription(request.getDescription());
        product.setWeightFromGram(request.getWeightGram());
        product.setProductCategory(cat);
        product.setSeller(user);

        product = productRepo.save(product);

        log.info("Tạo xong sản phẩm");

        CreateProductResponse createProductResponse = new CreateProductResponse();
        createProductResponse.setProductId(product.getId());
        return createProductResponse;
    }

    @Override
    @Transactional
    public CreateVariantResponse createVariant(CreateVariantRequest request) {
        // Kiểm tra product
        Product product = productRepo.findById(request.getProductId())
                .orElseThrow(() -> new NotFoundException(HttpStatus.BAD_REQUEST, "Product không tồn tại"));

        log.info("Đang check sku");
        //  Kiểm tra SKU
        if (productVariantRepo.existsBySku(request.getSku())) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "SKU đã tồn tại: " + request.getSku());
        }

        log.info("Bắt đầu tạo Variant");
        //  Tạo variant
        ProductVariant variant = new ProductVariant();

        variant.setProduct(product);
        variant.setName(request.getVariantName());
        variant.setSku(request.getSku());
        variant.setPriceModifier(request.getPriceModifier());

        variant = productVariantRepo.save(variant);


        // Tạo inventory cho variant này
        Inventory inventory = new Inventory();
        inventory.setProduct(product);
        inventory.setProductVariant(variant);
        inventory.setQuantityInStock(request.getStock());

        inventoryRepo.save(inventory);

        CreateVariantResponse createVariantResponse = new CreateVariantResponse() ;
        createVariantResponse.setVariantId(variant.getId());

        log.info("Tạo thành công variant");
        return createVariantResponse ;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductCategoryRes> getCategories() {
        List<ProductCategory> categories = productCategoryRepo.findAll();

        List<ProductCategoryRes> result = new ArrayList<>();

        for (ProductCategory category : categories) {
            ProductCategoryRes res = new ProductCategoryRes();
            res.setId(category.getId());
            res.setName(category.getName());
            result.add(res);
        }

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(UpdateProductRequest request, String productId) {

        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND, "sản phẩm không tồn tại"));

        log.info("Update product  ");
        //  Update Product

        if (request.getProductName() != null) {
            product.setName(request.getProductName());
        }

        if (request.getDescription() != null) {
            product.setDescription(request.getDescription());
        }

        if (request.getBasePrice() != null) {
            product.setBasePrice(request.getBasePrice());
        }

        if (request.getWeightGram() >= 0) {
            product.setWeightFromGram(request.getWeightGram());
        }

        // Update Category
        if (request.getCategoryId() != null) {
            ProductCategory category = productCategoryRepo.findById(request.getCategoryId())
                    .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND, "Category not found"));

            product.setProductCategory(category);
        }

        productRepo.save(product);

        log.info("Update các variant của product đó");
        // Update các Variant
        if (request.getVariants() != null) {

            for (UpdateVariantRequest variantRequest : request.getVariants()) {

                ProductVariant variant = productVariantRepo.findById(variantRequest.getVariantId())
                        .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND, "Variant not found"));

                // Kiểm tra variant có thuộc product đang update không
                if (!variant.getProduct().getId().equals(productId)) {
                    throw new RuntimeException("Variant does not belong to this product");
                }

                if (variantRequest.getVariantName() != null) {
                    variant.setName(variantRequest.getVariantName());
                }

                if (variantRequest.getSku() != null) {
                    variant.setSku(variantRequest.getSku());
                }

                if (variantRequest.getPriceModifier() != null) {
                    variant.setPriceModifier(variantRequest.getPriceModifier());
                }

                // 6. Update Inventory
                if (variantRequest.getQuantityInStock() != null) {

                    Inventory inventory = variant.getInventory();

                    if (inventory == null) {
                        inventory = new Inventory();
                        inventory.setProductVariant(variant);
                    }

                    inventory.setQuantityInStock(variantRequest.getQuantityInStock());

                    inventoryRepo.save(inventory);
                }

                productVariantRepo.save(variant);
            }
        }
        log.info("Update xong ");

    }
}
