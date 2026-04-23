package com.example.liveklass.dto.enrollment;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record PaymentRequest(

        @NotNull(message = "결제 금액은 필수입니다.")
        @Positive(message = "결제 금액은 양수여야 합니다.")
        Long paidAmount

) {
}
