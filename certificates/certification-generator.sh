#!/bin/bash

if [ $# -lt 1 ]
	then
	echo "Please input wanted name of keystore";
	exit
fi
KEYSTORE=$1;

#Generate key-pair for students
keytool -genkeypair -alias Hospital -keystore $KEYSTORE -storepass password

#Create a certificate signing request
keytool -certreq -keystore $KEYSTORE -alias Hospital -keyalg rsa -file certreq.csr -storepass password

#Sign the certificate request with the CA key
openssl x509 -req -CA CA -CAkey CApriv.pem -in certreq.csr -out usercert.cer -days 365 -CAcreateserial -CAserial CA.srl

#Import CA certificate to clientkeystore and signed response
keytool -import -file CA -alias CA -keystore $KEYSTORE -storepass password
keytool -import -file usercert.cer -trustcacerts -alias Hospital -keystore $KEYSTORE -storepass password
rm certreq.csr
rm usercert.cer
