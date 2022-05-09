package com.example.project.payload.request;

import com.example.project.validator.NonEmptyIntegerArray;
import com.example.project.validator.Numeric;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequest {

    @NotNull
    @NonEmptyIntegerArray(message = "Product array should not be empty")
    private int[] productsId;


    @NotNull
    @NonEmptyIntegerArray(message = "Product quantity should not be empty")
    private int[] productsQty;


    @Numeric
    @Positive(message = "Staff id  should be positive")
    @Min(value = 0, message = "Staff id should be greater than or equal to 0")
    private int staffId;


    @Numeric
    @Positive(message = "Store id should be positive")
    @Min(value = 0, message = "Store id should be greater than or equal to 0")
    private int storeId;

}
