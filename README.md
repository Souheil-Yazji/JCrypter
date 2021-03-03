# JCrypter
A Java access control, authentication, and encryption learning project. 
This is somewhat incomplete as ideally, as it should interface with a database rather than store passwords in a txt file.

## About
The project lays the foundation for an access control and user authentication system for a basic financial system. Users can enrol to the system, and it's assumed that they will enrol with the correct role (trusted user).

## Implementation
Access Control is implemented via a hybrid of RBAC and ABAC policies.
Password encryption uses PBKDF2 with HMAC SHA-512. 
The lengths of the hashing parameters all follow NIST recommendations. 
The key length used for PBKDF2 is 256-bits, which will be the length of the hashed password produced. 
The salt is pseudorandom generated 128-bits using the java “Secure Random” library. 
