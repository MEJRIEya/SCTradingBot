package com.solidcode.SCTradingBot.security.user;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping(value = {"/{userName}"})
    public ResponseEntity<User> getUser(@PathVariable String userName) {

        try {
            return ResponseEntity.ok().body(userService.getUser(userName));
        } catch (DataAccessException ex) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
    }

    @PostMapping(value = {"/add"})
    public ResponseEntity<ApiUserPayload> add(@RequestBody ApiUserPayload apiUserPayload) {
        try {
            return ResponseEntity.ok().body(userService.add(apiUserPayload));
        } catch (DataAccessException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
    }

    @PutMapping("/update")
    public ResponseEntity<ApiUserPayload> update(@RequestBody ApiUserPayload apiUserPayload) {

        try {
            return ResponseEntity.ok().body(userService.update(apiUserPayload));
        } catch (DataAccessException ex) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        try {
            userService.delete(id);
            return ResponseEntity.ok().build();
        } catch (DataAccessException ex) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
    }
}
