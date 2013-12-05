#!/bin/sh
insmod /lib/modules/2.6.32.9/kernel/drivers/mtd/nand/nandsim.ko
rmmod nandsim.ko
insmod /lib/modules/2.6.32.9/kernel/drivers/mtd/nand/nandsim.ko first_id_byte=0x20 second_id_byte=0xAA third_id_byte=0x00 fourth_id_byte=0x15
echo "nandsim inserted"
mount /dev/sdb2 /mnt
dd if=/mnt/home/nand of=/dev/mtd0
echo "dumped"
umount /mnt/
insmod /lib/modules/2.6.32.9/kernel/drivers/mtd/ubi/ubi.ko mtd=0
insmod /lib/modules/2.6.32.9/kernel/lib/crc16.ko
insmod /lib/modules/2.6.32.9/kernel/crypto/deflate.ko
insmod /lib/modules/2.6.32.9/kernel/lib/lzo/lzo_decompress.ko
insmod /lib/modules/2.6.32.9/kernel/lib/lzo/lzo_compress.ko
insmod /lib/modules/2.6.32.9/kernel/crypto/lzo.ko
insmod /lib/modules/2.6.32.9/kernel/fs/ubifs/ubifs.ko
echo "before mount "
mount -t ubifs ubi0:rootfs /mnt
BOOT_ROOT=/mnt

