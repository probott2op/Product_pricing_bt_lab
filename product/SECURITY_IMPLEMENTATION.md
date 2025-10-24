# Product Pricing API - Security Implementation

## Overview
This module implements JWT-based authentication and authorization using Spring Security. The security is integrated with the NEXA Bank Authentication Service.

## Security Rules

### Public Endpoints (No Authentication Required)
- **GET /api/products/*** - All GET requests are public
- **GET /swagger-ui/*** - API documentation
- **GET /actuator/health** - Health check

### Protected Endpoints (Admin Only)
- **POST /api/products/*** - Create operations require ADMIN role
- **PUT /api/products/*** - Update operations require ADMIN role  
- **DELETE /api/products/*** - Delete operations require ADMIN role

## JWT Token Structure

The JWT token from the Auth Service contains:
```json
{
  "sub": "admin@nexabank.com",
  "jti": "bbfcd4f6-47cf-4935-b0d5-3fe12533178f",
  "userId": "cc11f7d0-dfaa-42dd-b5a6-098ef87e9b60",
  "userType": "ADMIN",
  "roles": "ADMIN_FULL_ACCESS,ADMIN_VIEW,ADMIN_REPORTS,ADMIN_SYSTEM_CONFIG,ADMIN_USER_MANAGEMENT",
  "iat": 1761121018,
  "exp": 1761121048
}
```

## Authentication Flow

1. **User logs in** to Auth Service (http://localhost:3020/api/auth/login)
2. **Receives JWT token** in response
3. **Includes token** in Authorization header: `Bearer <token>`
4. **Product Service verifies** token using public key from Auth Service
5. **Extracts roles** from JWT and grants/denies access

## Public Key Verification

The service automatically fetches the public key from Auth Service:
- **Endpoint**: http://localhost:3020/api/auth/public-key
- **Format**: JWK (JSON Web Key)
- **Algorithm**: RS256 (RSA with SHA-256)

The public key is cached by Spring Security's JwtDecoder.

## Configuration

### application.properties
```properties
# Auth Service JWK endpoint
auth.service.jwk-set-uri=http://localhost:3020/api/auth/public-key
```

### Environment Variables (Optional)
```bash
export AUTH_SERVICE_JWK_URI=http://localhost:3020/api/auth/public-key
```

## Testing

### 1. Get JWT Token
```bash
curl -X POST http://localhost:3020/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "admin@nexabank.com",
    "password": "Admin@123"
  }'
```

**Response:**
```json
{
  "token": "eyJhbGciOiJSUzI1NiJ9...",
  "userId": "cc11f7d0-dfaa-42dd-b5a6-098ef87e9b60",
  "userType": "ADMIN"
}
```

### 2. Access Public Endpoint (No Token)
```bash
# GET requests work without token
curl -X GET http://localhost:8080/api/products
```

### 3. Access Protected Endpoint (With Token)
```bash
# POST requests require admin token
curl -X POST http://localhost:8080/api/products \
  -H "Authorization: Bearer eyJhbGciOiJSUzI1NiJ9..." \
  -H "Content-Type: application/json" \
  -d '{
    "productCode": "FD-2025",
    "productName": "Fixed Deposit"
  }'
```

### 4. Access Protected Endpoint (Without Token)
```bash
# Returns 401 Unauthorized
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "productCode": "FD-2025",
    "productName": "Fixed Deposit"
  }'
```

**Response:**
```json
{
  "error": "Unauthorized",
  "message": "Full authentication is required to access this resource",
  "path": "/api/products",
  "timestamp": 1761121018000
}
```

## Error Responses

### 401 Unauthorized
Missing or invalid JWT token:
```json
{
  "error": "Unauthorized",
  "message": "Full authentication is required to access this resource",
  "path": "/api/products",
  "timestamp": 1761121018000
}
```

### 403 Forbidden
Valid token but insufficient permissions:
```json
{
  "error": "Forbidden",
  "message": "Access denied. Admin privileges required.",
  "path": "/api/products",
  "timestamp": 1761121018000
}
```

## Security Components

### 1. SecurityConfig.java
- Configures HTTP security rules
- Sets up JWT decoder with JWK endpoint
- Defines public vs protected endpoints
- Enables CORS for frontend integration

### 2. CustomJwtAuthenticationConverter.java
- Extracts roles from JWT claims
- Converts to Spring Security authorities
- Handles both `userType` and `roles` claims

### 3. JwtAuthenticationEntryPoint.java
- Handles authentication failures (401)
- Handles authorization failures (403)
- Returns JSON error responses

## Role Hierarchy

The JWT `userType` and `roles` claims are converted to Spring Security authorities:

- **userType: "ADMIN"** → `ROLE_ADMIN`
- **roles: "ADMIN_FULL_ACCESS,ADMIN_VIEW"** → `ROLE_ADMIN_FULL_ACCESS`, `ROLE_ADMIN_VIEW`

## CORS Configuration

Allowed origins:
- http://localhost:3000 (Frontend)
- http://localhost:3020 (Auth Service)
- https://nexabank.com (Production)

Allowed methods:
- GET, POST, PUT, DELETE, OPTIONS

## Troubleshooting

### Issue: 401 Unauthorized with valid token
**Solution**: Ensure Auth Service is running on port 3020 and public key endpoint is accessible.

### Issue: Cannot connect to Auth Service
**Solution**: Check `auth.service.jwk-set-uri` in application.properties matches your Auth Service URL.

### Issue: Token expired
**Solution**: Tokens expire after 24 hours. Login again to get a new token or use refresh token endpoint.

### Issue: 403 Forbidden
**Solution**: Ensure your user has ADMIN role. Check JWT claims using https://jwt.io

## Development Mode

To disable security during development (not recommended):
```java
// In SecurityConfig.java
.authorizeHttpRequests(authorize -> authorize
    .anyRequest().permitAll()  // WARNING: Only for development!
)
```

## Production Considerations

1. **Use HTTPS**: Always use HTTPS in production
2. **Environment Variables**: Use environment variables for JWK URI
3. **Key Rotation**: Support key rotation with `kid` (key ID) matching
4. **Token Expiration**: Implement refresh token flow
5. **Rate Limiting**: Add rate limiting to prevent abuse
6. **Audit Logging**: Log all authentication/authorization events

## Support

For security issues or questions:
- Email: security@nexabank.com
- Documentation: https://www.nexabank.com/security-docs
