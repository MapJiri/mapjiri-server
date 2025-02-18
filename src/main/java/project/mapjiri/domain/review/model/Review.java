package project.mapjiri.domain.review.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.mapjiri.domain.restaurant.model.Restaurant;

import java.time.LocalDate;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @Column(nullable = false, length = 3000)
    private String reviewContent;

    @Column(nullable = false)
    private int reviewPoint;

    @Column(nullable = false)
    private LocalDate date;

    @Column
    private String reviewImageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    private Review(String reviewContent, int reviewPoint, LocalDate date, String reviewImageUrl, Restaurant restaurant) {
        this.reviewContent = reviewContent;
        this.reviewPoint = reviewPoint;
        this.date = date;
        this.reviewImageUrl = reviewImageUrl;
        this.restaurant = restaurant;
    }

    public static Review of(String reviewContent, int reviewPoint, LocalDate date, String reviewImageUrl, Restaurant restaurant) {
        return new Review(reviewContent, reviewPoint, date, reviewImageUrl, restaurant);
    }
}
