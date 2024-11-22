package com.example.webfluxSecuriryExample.mapper;

import com.example.webfluxSecuriryExample.entity.AppUser;
import com.example.webfluxSecuriryExample.model.UserModel;

public interface MappingEntity {

    AppUser revert(UserModel userModel);

    UserModel revert(AppUser appUser);
}
