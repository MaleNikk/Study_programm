package com.example.webfluxSecuriryExample.mapper;

import com.example.webfluxSecuriryExample.entity.AppUser;
import com.example.webfluxSecuriryExample.model.UserModel;
import org.springframework.stereotype.Component;

@Component
public class AppMapper implements MappingEntity {


    @Override
    public AppUser revert(UserModel userModel) {
        return new AppUser(
                userModel.getId(),
                userModel.getUserName(),
                userModel.getFirstName(),
                userModel.getLastName(),
                userModel.getPassword(),
                userModel.getRole(),
                userModel.getDateCreate(),
                userModel.getDateUpdate(),
                userModel.getEnable());
    }

    @Override
    public UserModel revert(AppUser appUser) {
        return new UserModel(
                appUser.getId(),
                appUser.getUserName(),
                appUser.getFirstName(),
                appUser.getLastName(),
                appUser.getPassword(),
                appUser.getRole(),
                appUser.getDateCreate(),
                appUser.getDateUpdate(),
                appUser.getEnable()
        );
    }
}
