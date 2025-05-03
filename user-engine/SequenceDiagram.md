```mermaid
sequenceDiagram
    participant Client
    participant UserController
    participant UserService
    participant UserRepository
    participant JwtTokenUtil
    participant PasswordEncoder
    
    Client->>UserController: POST /sign-up (SignUpRequest)
    UserController->>UserService: signUp(request)
    UserService->>UserRepository: findByEmail(request.email)
    
    alt Email disponible
        UserRepository-->>UserService: Optional.empty()
        UserService->>PasswordEncoder: encode(password)
        UserService->>JwtTokenUtil: generateToken()
        UserService->>UserRepository: save(user)
        UserRepository-->>UserService: User saved
        UserService-->>UserController: UserResponse
        UserController-->>Client: 201 Created
    else Email existe
        UserRepository-->>UserService: Optional.of(user)
        UserService-->>UserController: Error
        UserController-->>Client: 409 Conflict
    end