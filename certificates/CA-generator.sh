#!/bin/bash

#Generate a CA, self signed certificate
openssl req -x509 -newkey rsa:2048 -keyout CApriv.pem -out CA

#Create a new clienttruststore and put CA certificate inside it
keytool -import -file CA -alias CA -keystore clienttruststore -storepass password

#Copy clienttruststore to create servertruststore
cp clienttruststore servertruststore
