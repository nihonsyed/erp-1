package com.brainstation23.erp.Authentication;

import com.brainstation23.erp.Jwt.Jwt;
import com.brainstation23.erp.model.domain.User;
import com.brainstation23.erp.model.dto.CreateLoginRequest;

import java.util.UUID;

public class UserAuthentication {

   public static boolean isValidLoginCredential(User user, CreateLoginRequest createLoginRequest)
   {
       if (user == null || !createLoginRequest.getPassword().matches(user.getPassword()))
       {
           return false;
       }
       //2e566272-607b-429d-ae35-54be68f0e71a
       //423fd64f-d18a-406e-8355-5bafc5753d5b
       else
       {
           return true;
       }
   }

   public static boolean isAuthorized(UUID id, String authHeader)
   {
       String userData= Jwt.getDecodedUserData(authHeader);
       String userIdPart=userData.split(",")[2];
       String userRolePart=userData.split(",")[1];
       if((userIdPart.contains(id.toString()))||(userRolePart.contains("Admin")))
           return true;
       else
           return false;
   }


   public static boolean isAdmin(String authHeader)
   {
       String userData = Jwt.getDecodedUserData(authHeader);
       String userRole=userData.split(",")[1];
       if(userRole.contains("Admin"))
           return true;
       else
           return false;
   }




}
