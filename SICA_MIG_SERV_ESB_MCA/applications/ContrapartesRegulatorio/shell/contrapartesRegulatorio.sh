#!/bin/bash

CONTRAPARTES=/java/ContrapartesRegulatorio
export CONTRAPARTES

cd $CONTRAPARTES

PATH=/java/jdk1.6.0_16/bin/:$PATH
export PATH

java -jar contrapartesRegulatorio.jar