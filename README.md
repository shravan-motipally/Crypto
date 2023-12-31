# Crypto
Collection of algorithms that enable RSA and El Gamal

# Requirements to run these tests.
Java version 11 is required to work with this project.  Please find instructions on getting Java 11 as follows.

Please install sdkman by following the directions here:  https://sdkman.io/install


```shell
$ sdk install java 11.0.21-tem
```

<h><i>Trouble?</i></h>
<br/>
As of 12/10 - these instructions work, if not, please find a suitable Java 11 version identifier by doing the following:

```shell
$ sdk list java
```

Please note that you may install maven from sdk man as well; however, the maven wrapper is already part 
of the project and will allow you to work without installing it yourself.  Please follow directions closely.

# Known issues:
Factorization using Rho is sometimes shown to reach max tries, please re-run the tests and they will work.

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

## Instructions for pulling all dependencies 
Note: This skips tests - you can remove skip tests if you want, all tests pass, but you'll have to wait a bit.
```shell
./mvnw clean install -DskipTests=true
```

## Instructions for generating a prime
1. Find `PrimesTest.java` and find the test `getPrimeNumberForTesting()`
2. Change the maxBits as you wish.
3. Run the following to retrieve the prime number.
```shell
$ ./mvnw test -Dtest="PrimesTest#getPrimeNumberForTesting"
```

## Instructions for generating a random number
1. Find `RandomGeneratorTest.java` and find the test `testBbgGeneration()`
2. Change the maxBits as you wish.
3. Run the following to retrieve the random number.
```shell
$ ./mvnw test -Dtest="RandomGeneratorTest#testBbgGeneration"
```

# Instructions for testing RSA.

### Within *ix based systems

#### Testing RSA Encryption 
1. Find `RSATest.java` and find the test `encryptionTestRSA()`.
2. Change message if you wish as `BigInteger.valueOf("<integer>")`
   If you'd like to generate Bob's public/priv key, use the following.
```shell
$ ./mvnw test -Dtest="RSATest#generateBobsPublicKey"
```
3. Run the following to test encryptionTestRSA.
```shell
$ ./mvnw test -Dtest="RSATest#encryptionTestRSA"
```

#### Testing RSA Decryption
1. Find `RSATest.java` and find the test `decryptionTestRSA`
2. Change the values as you wish below for `p`, `q`, `message`, `aliceEncryptedMsg`, `bobsPubKey` and `bobsPrivKey`.
    Note that bobsPrivKey can be taken from Step 2 of Testing RSA encryption.
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

# Instructions for testing El Gamal.

### Within *ix based systems

#### Testing El Gamal Encryption
0. Generate a common group, generator, Alice's public key and secret & Bob's public and secret using the following commands.
```java
$ ./mvnw test -Dtest="ElGamalTest#generateGroupAndGenerator"
```
Find `ElGamalTest.java` and find the test `generateBobsEncryptionKey` and paste the values found above for group and generator.  Then run:
```java
$ ./mvnw test -Dtest="ElGamalTest#generateBobsEncryptionKey"
```
1. Find `ElGamalTest.java` and find the test `testElGamalAsAlice()`.
2. Change the values for the parameters as you wish below:  Please feel free to update the message to a random number by following instructions above.
```java
BigInteger group = new BigInteger("10901741");
BigInteger generator = new BigInteger("7200621");
BigInteger aliceEncryptionKey = new BigInteger("4855820");
BigInteger aliceSecret = new BigInteger("8316155");
BigInteger bobEncryptionKey = new BigInteger("7106903");
BigInteger message = new BigInteger("9458119"); 
```
3. Run the following to test testElGamalAsAlice.
```shell
$ ./mvnw test -Dtest="ElGamalTest#testElGamalAsAlice"
```


#### Testing El Gamal Decryption
1. Find `ElGamalTest.java` and find the test `testElGamalAsBob()`.
2. Change the values for the parameters as you wish below:
```java
BigInteger group = new BigInteger("16300051");
BigInteger generator = new BigInteger("6353629");
BigInteger aliceEncryptionKey = new BigInteger("1183985");
BigInteger bobSecret = new BigInteger("4309990");
BigInteger bobKey = new BigInteger("1378805");
BigInteger message = new BigInteger("11335333");
```
3. Run the following to test testElGamalAsBob.
```shell
$ ./mvnw test -Dtest="ElGamalTest#testElGamalAsBob"
```


#### Testing El Gamal Eavesdropping
1. Find `ElGamalTest.java` and find the test `testElGamalAsEve()`.
2. Change the values for the parameters as you wish below:
ElGamalPair takes in three constructor arguments. Group, Generator and EncryptionKey
```java
BigInteger group = valueOf(16396147L);
BigInteger generator = new BigInteger("2");
BigInteger encryptedMessage = new BigInteger("14521569");
ElGamalPair bobEncKey = new ElGamalPair(group, generator, new BigInteger("15682381"));
ElGamalPair aliceEncKey = new ElGamalPair(group, generator, new BigInteger("7197288"));
BigInteger message = new BigInteger("1314");
```
3. Run the following to test testElGamalAsEve.
```shell
$ ./mvnw test -Dtest="ElGamalTest#testElGamalAsEve"
```


### Within Windows based systems

Instead of `./mvnw` use `mvnw.cmd` to run commands for maven.
