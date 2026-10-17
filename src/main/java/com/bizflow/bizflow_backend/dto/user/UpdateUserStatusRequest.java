package com.bizflow.bizflow_backend.dto.user;

import com.bizflow.bizflow_backend.entity.User;
import com.bizflow.bizflow_backend.exception.BadRequestException;
import com.bizflow.bizflow_backend.exception.ResourceNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public class UpdateUserStatusRequest {

    private boolean active;

    public UpdateUserStatusRequest() {
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }


}