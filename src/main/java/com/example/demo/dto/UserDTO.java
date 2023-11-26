package com.example.demo.dto;

import com.example.demo.entity.Product;
import com.example.demo.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private Long id;
    private String lastname;
    private String name;
    private String email;
    private String password;
    private Role role;
//    private List<ProductReservado> productReservado;
    private List<Product> productsFavs;
}
