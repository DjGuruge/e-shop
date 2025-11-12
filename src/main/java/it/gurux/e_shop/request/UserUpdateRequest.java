package it.gurux.e_shop.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Data
public class UserUpdateRequest {

    private String firstName;
    private String lastName;

}
