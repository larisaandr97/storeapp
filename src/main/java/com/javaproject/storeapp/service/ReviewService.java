package com.javaproject.storeapp.service;

import com.javaproject.storeapp.entity.Product;
import com.javaproject.storeapp.entity.Review;
import com.javaproject.storeapp.exception.ResourceNotFoundException;
import com.javaproject.storeapp.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductService productService;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository, ProductService productService) {
        this.reviewRepository = reviewRepository;
        this.productService = productService;
    }

    public Review createReview(Review review) {
        return reviewRepository.save(review);
    }

    public List<Review> getReviewsForProduct(int productId) {
        Product product = productService.findProductById(productId);
        return reviewRepository.findReviewsByProduct(productId);
    }

    public void deleteReview(Review review) {
        reviewRepository.delete(review);
    }

    public Review findReviewById(int id) {
        Optional<Review> reviewOptional = reviewRepository.findById(id);
        if (reviewOptional.isPresent()) {
            return reviewOptional.get();
        } else {
            throw new ResourceNotFoundException("Review with Id " + id + " not found.");
        }
    }
}
