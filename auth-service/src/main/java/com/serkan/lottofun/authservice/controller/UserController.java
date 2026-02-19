package com.serkan.lottofun.authservice.controller;

import com.serkan.lottofun.authservice.dto.BalanceUpdateRequest;
import com.serkan.lottofun.authservice.dto.UserResponse;
import com.serkan.lottofun.authservice.entity.User;
import com.serkan.lottofun.authservice.exception.BaseException;
import com.serkan.lottofun.authservice.exception.ErrorMessage;
import com.serkan.lottofun.authservice.repository.UserRepository;
import com.serkan.lottofun.authservice.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final IUserService userService;
    private final UserRepository userRepository;
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }
    @GetMapping("/user-info/{username}")
    public ResponseEntity<UserResponse> getUser(@PathVariable String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BaseException(ErrorMessage.USER_NOT_FOUND));

        UserResponse response = new UserResponse();
        BeanUtils.copyProperties(user, response);
        return ResponseEntity.ok(response);
    }
    @PatchMapping("/user-update/{id}")
    public ResponseEntity<Void> updateBalance(
            @PathVariable Long id,
            @RequestBody BalanceUpdateRequest request) {

        userService.updateBalance(id, request.getBalance());
        return ResponseEntity.ok().build();
    }
    @PostMapping("/users/decrease-balance")
    public ResponseEntity<Void> decreaseBalance(@RequestParam Long userId,
                                                @RequestParam BigDecimal amount) {
        userService.decreaseBalance(userId, amount);
        return ResponseEntity.ok().build();
    }
}
