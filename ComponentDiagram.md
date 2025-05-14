```mermaid
componentDiagram
    component "UserController" {
        [POST /sign-up]
        [GET /login]
    }
    
    component "UserService" {
        [Business Logic]
        [Validation]
    }
    
    component "UserRepository" {
        [CRUD Operations]
    }
    
    component "JwtTokenUtil" {
        [Token Generation]
        [Token Validation]
    }
    
    component "H2 Database" {
        [Data Storage]
    }
    
    [UserController] --> [UserService] : Uses
    [UserService] --> [UserRepository] : Uses
    [UserService] --> [JwtTokenUtil] : Uses
    [UserRepository] --> [H2 Database] : Persists