package com.example.webfluxSecuriryExample.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.security.Principal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppDataPrincipal implements Principal {
    private Long id;
    private String name;
}
