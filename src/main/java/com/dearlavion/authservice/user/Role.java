package com.dearlavion.authservice.user;

/** Access role. ADMIN/STAFF are privileged (admin on consuming backends); USER is a normal user. */
public enum Role {
    ADMIN,
    STAFF,
    USER
}
