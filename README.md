# Crypto
Collection of algorithms that enable RSA and El Gamal

# Instructions to run tests.

### Within *ix based systems use the following command for maven.
```shell
./mvnw
```

### Within windows based systems.
```
mvnw.cmd
```

# Preparing tests
```shell
./mvnw clean install -DskipTests=true
```

# Instructions for testing RSA.

### Within *ix based systems

#### Testing RSA Encryption
1. Find `RSATest.java` and find the test `encryptionTestRSA()`.
2. Change message if you wish as `BigInteger.valueOf("<integer>")`
3. Run the following to test encryptionTestRSA.
```shell
$ ./mvnw test -Dtest="RSATest#encryptionTestRSA"
```

#### Testing RSA Decryption
1. Find `RSATest.java` and find the test `decryptionTestRSA`
2. Change the values as you wish below for `p`, `q`, `message`, `aliceEncryptedMsg`, `bobsPubKey` and `bobsPrivKey`.  
Note that the first part of `RSAPair` constructor is the encryptionKey.  The second is the modulus.
```java
BigInteger p = new BigInteger("9107159");
BigInteger q = new BigInteger("15289943");
BigInteger message = new BigInteger("3333");
BigInteger aliceEncryptedMsg = new BigInteger("38122060084554");
RSAPair bobsPubKey = new RSAPair(new BigInteger("13647839"), new BigInteger("139247942001937"));
RSAPair bobsPrivKey = new RSAPair(new BigInteger("87885123486543"), new BigInteger("139247942001937"));
```
3. Run the following to test `decryptionTestRSA`.
```shell
$ ./mvnw test -Dtest="RSATest#decryptionTestRSA"
```

#### Testing RSA Eavesdropping
1. Find `RSATest.java` and find the test `eavesdroppingTest()`.
2. Change message if you wish as `BigInteger.valueOf("<integer>")`
3. Run the following to test eavesdroppingTest.
```shell
$ ./mvnw test -Dtest="RSATest#eavesdroppingTest"
```

### Within Windows based systems

Instead of `./mvnw` use `mvnw.cmd` to run commands for maven.