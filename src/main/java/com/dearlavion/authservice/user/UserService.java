package com.dearlavion.authservice.user;

import com.dearlavion.authservice.common.exception.ConflictException;
import com.dearlavion.authservice.common.exception.NotFoundException;
import com.dearlavion.authservice.tenant.TenantService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/** Every lookup/write is scoped to a customer's own database, resolved via TenantService. */
@Service
@RequiredArgsConstructor
public class UserService {

    private final TenantService tenants;
    private final PasswordEncoder passwordEncoder;

    public User findByUsername(String customer, String username) {
        MongoTemplate db = tenants.db(customer);
        return db.findOne(Query.query(Criteria.where("username").is(username)), User.class);
    }

    public User findByEmail(String customer, String email) {
        MongoTemplate db = tenants.db(customer);
        return db.findOne(Query.query(Criteria.where("email").is(email)), User.class);
    }

    public User loadByUsernameOrThrow(String customer, String username) {
        User user = findByUsername(customer, username);
        if (user == null) throw new NotFoundException("User not found");
        return user;
    }

    public User registerUser(String customer, UserVoRequest vo, AuthType type) {
        MongoTemplate db = tenants.db(customer);
        User user = new User();
        user.setUsername(vo.username());
        user.setEmail(vo.email());
        user.setPhone(vo.phone());
        user.setPassword(vo.password() != null ? passwordEncoder.encode(vo.password()) : null);
        // Already gated by the controller (privileged roles need X-Provision-Secret); defaults to USER.
        if (vo.activeProfile() != null) user.setActiveProfile(vo.activeProfile());
        user.setType(type);
        try {
            return db.insert(user);
        } catch (DuplicateKeyException e) {
            throw new ConflictException("User already exists");
        }
    }

    public User updateUser(String customer, String username, UserVoRequest u) {
        User user = loadByUsernameOrThrow(customer, username);
        if (u.firstname() != null) user.setFirstname(u.firstname());
        if (u.lastname() != null) user.setLastname(u.lastname());
        if (u.email() != null) user.setEmail(u.email());
        if (u.phone() != null) user.setPhone(u.phone());
        if (u.activeProfile() != null) user.setActiveProfile(u.activeProfile());
        if (u.image() != null) user.setImage(u.image());
        return tenants.db(customer).save(user);
    }

    public void updatePassword(String customer, String username, String newPassword) {
        User user = loadByUsernameOrThrow(customer, username);
        user.setPassword(passwordEncoder.encode(newPassword));
        tenants.db(customer).save(user);
    }

    /** Every user for a tenant — admin's Users list fetches once and filters/paginates
     * client-side, same convention as the store-engine admin lists. */
    public List<User> listUsers(String customer) {
        return tenants.db(customer).findAll(User.class);
    }

    /** Soft delete/restore — false blocks login (see SimpleAuthStrategy's active check) without
     * touching the document, so nothing elsewhere that references this user by id breaks. */
    public User setActive(String customer, String username, boolean active) {
        User user = loadByUsernameOrThrow(customer, username);
        user.setActive(active);
        return tenants.db(customer).save(user);
    }

    public UserView toView(User user) {
        return new UserView(
                user.getUsername(), user.getEmail(), user.getFirstname(), user.getLastname(),
                user.getPhone(), user.getImage(), user.getActiveProfile(), user.isActive()
        );
    }
}
