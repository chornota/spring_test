package com.family.family.response;

public class UserResponse {
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

//ApiResponse
//boolean success:true/false
//Object data

//ResponseEntity.ok(new ApiResponse(true,obj)

{
    "success":true,
    "data":[]
}
class UnableToPerformOperationException extends Exception{
    
}

//ExceptionHandlers spring
//User
