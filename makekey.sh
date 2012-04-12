#!/bin/bash

export CLASSPATH=/home/tom/Tools/BouncyCastle/bcprov-jdk16-145.jar
CERTSTORE=android-opencnam-library/res/raw/certstore.bks
PROVIDER=org.bouncycastle.jce.provider.BouncyCastleProvider

rm -i ${CERTSTORE}

if [ -a $CERTSTORE ]; then
    rm $CERTSTORE || exit 1
fi

keytool -import -v -trustcacerts -alias 0 -file <(openssl x509 -in ${1}) \
    -keystore ${CERTSTORE} -storetype BKS -provider org.bouncycastle.jce.provider.BouncyCastleProvider -providerpath /usr/share/java/bcprov.jar  -storepass abc123




