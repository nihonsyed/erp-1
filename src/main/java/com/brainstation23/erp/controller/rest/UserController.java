 package com.brainstation23.erp.controller.rest;
import com.brainstation23.erp.Authentication.UserAuthentication;
import com.brainstation23.erp.Jwt.Jwt;
import com.brainstation23.erp.mapper.UserMapper;
import com.brainstation23.erp.model.dto.*;
import com.brainstation23.erp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import javax.validation.Valid;
import java.util.UUID;

@Tag(name = "User")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/users")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;


    @Operation(summary = "Get All Users")
    @GetMapping
    public ResponseEntity<?> getAll(@ParameterObject Pageable pageable,@RequestHeader(value = "optional-value", required = true) String authHeader) {
        if(UserAuthentication.isAdmin(authHeader)==false) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Only admin can access to this operation!");
        }
        else
        {
            log.info("Getting List of Organizations");
          var domains = userService.getAll(pageable);
          return ResponseEntity.ok(domains.map(userMapper::domainToResponse));
        }
    }

    @Operation(summary = "Get A Single User")
    @GetMapping("{id}")
    public ResponseEntity<?> getOne(@PathVariable UUID id,@RequestHeader(value = "Token", required = true) String authHeader) {

        if(UserAuthentication.isAuthorized(id,authHeader)==true)
        {
            log.info("Getting Details of User({})", id);
            var domain = userService.getOne(id);
            return ResponseEntity.ok(userMapper.domainToResponse(domain));
        }
        else
        {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Sorry! you don't have the permission to do this operation!");
        }
    }

    @Operation(summary = "Register A User")
    @PostMapping
    public ResponseEntity<Void> createOne(@RequestBody @Valid CreateUserRequest createRequest) {
        log.info("Creating an Organization: {} ", createRequest);
        var id = userService.createOne(createRequest);
        var location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri();
        return ResponseEntity.created(location).build();
    }

    @Operation(summary = "Login")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid CreateLoginRequest createLoginRequest) {

        var existingUser = userService.getOneByEmail(createLoginRequest.getEmail());

         if(UserAuthentication.isValidLoginCredential(existingUser,createLoginRequest)==false)
         {
             return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
         }
        else {
            String token = Jwt.getToken(existingUser);
            return ResponseEntity.ok(token);
        }
    }


    @Operation(summary = "Update Single Organization")
    @PutMapping("{id}")
    public ResponseEntity<?> updateOne(@PathVariable UUID id,
                                          @RequestBody @Valid UpdateUserRequest updateRequest,@RequestHeader(value = "Token", required = true) String authHeader) {

//
        if(UserAuthentication.isAuthorized(id,authHeader)==true)
        { log.info("Updating an Organization({}): {} ", id, updateRequest);
           userService.updateOne(id, updateRequest);
           return ResponseEntity.noContent().build();
        }
        else
        {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Sorry! you don't have the permission to do this operation!");
        }
    }

    @Operation(summary = "Delete Single Organization")
    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteOne(@PathVariable UUID id,@RequestHeader(value = "Token", required = true) String authHeader) {
        if(UserAuthentication.isAuthorized(id,authHeader)==true)
           {log.info("Deleting an Organization({}) ", id);
            userService.deleteOne(id);
            return ResponseEntity.noContent().build();}
        else
        {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Sorry! you don't have the permission to do this operation!");
        }
    }
}
