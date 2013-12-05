#!/bin/sh
#exit 0
modprobe nandsim first_id_byte=0x20 second_id_byte=0xAA third_id_byte=0x00 fourth_id_byte=0x15
dd if=/home/nand of=/dev/mtd0
sleep 2
#/bin/bash
modprobe ubi   mtd=0
#sleep 1
#/bin/sh
modprobe ubifs
#/bin/sh
mount -t ubifs ubi0:rootfs /mnt/ubi	 
#/bin/sh
pivot_root /mnt/ubi /mnt/ubi/old

#/bin/sh
