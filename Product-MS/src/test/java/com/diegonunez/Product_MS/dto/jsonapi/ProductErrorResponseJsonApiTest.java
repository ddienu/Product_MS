package com.diegonunez.Product_MS.dto.jsonapi;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class ProductErrorResponseJsonApiTest {

    @Test
    void productErrorResponseOK(){
        String status = "404";
        String title = "Not Found";
        String detail = "Entity not found";

        ProductErrorResponseJsonApi response = new ProductErrorResponseJsonApi(
                status, title, detail
        );

        Assertions.assertEquals(status, response.getErrors().get(0).getStatus());
        Assertions.assertEquals(title, response.getErrors().get(0).getTitle());
        Assertions.assertEquals(detail, response.getErrors().get(0).getDetail());
    }

    @Test
    void productErrorResponseWithListOfErrors(){
        String status = "404";
        String title = "Not Found";
        String detail = "Entity not found";

        String status1 = "400";
        String title1 = "Bad Request";
        String detail1 = "Required fields are missing";

        ProductErrorResponseJsonApi.ErrorItem errorItem = new ProductErrorResponseJsonApi.ErrorItem(
                status,
                title,
                detail
        );

        ProductErrorResponseJsonApi.ErrorItem errorItem2 = new ProductErrorResponseJsonApi.ErrorItem(
                status1,
                title1,
                detail1
        );

        List<ProductErrorResponseJsonApi.ErrorItem> errorList = new ArrayList<>();
        errorList.add(errorItem);
        errorList.add(errorItem2);

        ProductErrorResponseJsonApi response = new ProductErrorResponseJsonApi(errorList);

        Assertions.assertEquals(status, response.getErrors().get(0).getStatus());
        Assertions.assertEquals(title, response.getErrors().get(0).getTitle());
        Assertions.assertEquals(detail, response.getErrors().get(0).getDetail());

    }

}
