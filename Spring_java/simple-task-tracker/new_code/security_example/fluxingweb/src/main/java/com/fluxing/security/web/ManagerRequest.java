package com.fluxing.security.web;

import com.fluxing.security.dto.RoleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ManagerRequest {

    private String username;

    private Set<String> idTasks;

}
