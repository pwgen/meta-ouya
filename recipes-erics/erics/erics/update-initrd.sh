#!/bin/sh
ifup eth0
cd /media/mmcblk0p1
rm Image.initrd
wget http://loki.prv/Image.initrd

