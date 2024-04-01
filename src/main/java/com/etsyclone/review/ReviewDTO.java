package com.etsyclone.review;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReviewDTO {

    private Long productId;
    private String comment;
    private Short rating;
}
