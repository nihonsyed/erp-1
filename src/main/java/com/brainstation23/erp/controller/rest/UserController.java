 package com.brainstation23.erp.controller.rest;
import com.brainstation23.erp.Decoder.JwtDecoder;
import com.brainstation23.erp.mapper.UserMapper;
import com.brainstation23.erp.model.dto.*;
import com.brainstation23.erp.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
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
import java.util.Date;
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
        log.info("Getting List of Organizations");
        // Get the payload(data) from the authentication token
        String payload = JwtDecoder.getPayload(authHeader);
        // Split the payload into an array of strings
        String [] splittedPayload=payload.split(",");
        // Check if the user has the "Admin" role, return error message if not
        if(!splittedPayload[1].contains("Admin")) {
            System.out.println(splittedPayload[1]);
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Only admin can access to this operation!");
        }
        //Otherwise, return the paginated list of organizations
        else
        {
          var domains = userService.getAll(pageable);
          return ResponseEntity.ok(domains.map(userMapper::domainToResponse));
        }
    }

    @Operation(summary = "Get A Single User")
    @GetMapping("{id}")
    public ResponseEntity<?> getOne(@PathVariable UUID id,@RequestHeader(value = "Token", required = true) String authHeader) {
        log.info("Getting Details of User({})", id);
        String payload= JwtDecoder.getPayload(authHeader);
        String [] splittedPayload=payload.split(",");
        if((splittedPayload[2].contains(id.toString()))||(splittedPayload[1].contains("Admin"))) {
            var domain = userService.getOne(id);
            return ResponseEntity.ok(userMapper.domainToResponse(domain));
        }
        else
        {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Sorry! you don't have the permission to do this operation!");
        }
    }

    @Operation(summary = "Entry A Single User")
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
        // Get the user with the given email
        var existingUser = userService.getOneByEmail(createLoginRequest.getEmail());

        // Check if the user exists and the password matches
        if (existingUser == null || !createLoginRequest.getPassword().matches(existingUser.getPassword())) {
            // Return a ResponseEntity with an unauthorized status and an error message
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        } else {
            // Create a JWT token with the user's email, role, and ID as claims
            String token = Jwts.builder()
                    .setSubject(existingUser.getEmail())
                    .claim("role", existingUser.getRole())
                    .claim("id", existingUser.getId())
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + 120000)) // Token will expire in 2 minutes
                    .signWith(SignatureAlgorithm.HS512, "secretkey")
                    .compact();

            // Return a ResponseEntity with the JWT token
            return ResponseEntity.ok(token);
        }
    }


    @Operation(summary = "Update Single Organization")
    @PutMapping("{id}")
    public ResponseEntity<?> updateOne(@PathVariable UUID id,
                                          @RequestBody @Valid UpdateUserRequest updateRequest,@RequestHeader(value = "Token", required = true) String authHeader) {

        String payload= JwtDecoder.getPayload(authHeader);
        String [] splittedPayload=payload.split(",");
        // Check if the user is authorized to update the user
        if((splittedPayload[2].contains(id.toString()))||(splittedPayload[1].contains("Admin")))
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
        String payload= JwtDecoder.getPayload(authHeader);
        String [] splittedPayload=payload.split(",");
        // Check if the user is authorized to delete the user
        if((splittedPayload[2].contains(id.toString()))||(splittedPayload[1].contains("Admin")))
           {log.info("Deleting an Organization({}) ", id);
               // Delete the user with the given ID
            userService.deleteOne(id);
            return ResponseEntity.noContent().build();}
        else
        {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Sorry! you don't have the permission to do this operation!");
        }
    }
}
