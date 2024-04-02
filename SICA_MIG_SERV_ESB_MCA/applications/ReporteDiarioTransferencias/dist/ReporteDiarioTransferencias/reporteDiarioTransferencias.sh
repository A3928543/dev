#!/bin/bash

REPORTEDIARIOTRANSFERENCIAS=/java/ReporteDiarioTransferencias
export REPORTEDIARIOTRANSFERENCIAS

cd $REPORTEDIARIOTRANSFERENCIAS

PATH=/java/jdk1.6.0_16/bin/:$PATH
export PATH

java -jar reporteDiarioTransferencias.jar