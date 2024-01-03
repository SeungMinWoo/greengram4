package com.green.greengream4.user.model;

import lombok.Data;

@Data
public class UserFollowDto {
    private int fromIuser;
    private int toIuser;
}
