#!bin/sh -
echo "Erics.sh"
ln -s /bin/systemctl /usr/bin/systemctl
do_ubi()
{
	#modprobe nandsim first_id_byte=0x20 second_id_byte=0xAA third_id_byte=0x00 fourth_id_byte=0x15
	#    modprobe nandsim first_id_byte=0x20 second_id_byte=0x33 - 16MiB, 512 bytes page;
	#    modprobe nandsim first_id_byte=0x20 second_id_byte=0x35 - 32MiB, 512 bytes page;
	#    modprobe nandsim first_id_byte=0x20 second_id_byte=0x36 - 64MiB, 512 bytes page;
	modprobe nandsim first_id_byte=0x20 second_id_byte=0x78 #- 128MiB, 512 bytes page;
	#    modprobe nandsim first_id_byte=0x20 second_id_byte=0x71 - 256MiB, 512 bytes page;
	#    modprobe nandsim first_id_byte=0x20 second_id_byte=0xa2 third_id_byte=0x00 fourth_id_byte=0x15 - 64MiB, 2048 bytes page;
	#    modprobe nandsim first_id_byte=0xec second_id_byte=0xa1 third_id_byte=0x00 fourth_id_byte=0x15 - 128MiB, 2048 bytes page;
	#    modprobe nandsim first_id_byte=0x20 second_id_byte=0xaa third_id_byte=0x00 fourth_id_byte=0x15 - 256MiB, 2048 bytes page;
	#    modprobe nandsim first_id_byte=0x20 second_id_byte=0xac third_id_byte=0x00 fourth_id_byte=0x15 - 512MiB, 2048 bytes page;
	#    modprobe nandsim first_id_byte=0xec second_id_byte=0xd3 third_id_byte=0x51 fourth_id_byte=0x95 - 1GiB, 2048 bytes page;

	#ubiformat /dev/mtd0
	modprobe ubi   mtd=/dev/mtd0
	major=`cat /proc/devices | grep ubi0 | cut -d' ' -f 1`
	mknod /dev/ubi_ctrl c 10 58
	mknod /dev/ubi0 c ${major} 0
	mknod /dev/ubi0_0  c ${major} 1

	ubimkvol /dev/ubi0 -N rootfs -s 96MiB
	#nandwrite /dev/mtd0 /home/nand -o 
	#ubiattach /dev/ubi_ctrl -m0
	mkdir /sysroot
	mount -t ubifs ubi0:rootfs /sysroot	 
}
fill_ubi(){
	cp -a /bin /sysroot 
	cp -a /sbin /sysroot 
	cp -a /usr /sysroot 
	cp -a /etc /sysroot 
	cp -a /lib /sysroot 
	cp -a /home /sysroot 
	cp -a /linuxrc /sysroot 
	cp -a /var /sysroot 
	#cp  -a /dev /sysroot/dev
	#cd /sysroot/etc/rc5.d
	#ln -s ../init.d/liveramdisk ./S98liveramdisk
	#cd /
	mkdir /sysroot/sys
	mkdir /sysroot/dev
	mkdir /sysroot/proc
	mkdir /sysroot/media
	for a in `ls /media ` ; do mkdir /sysroot/media/${a};done
	cp /media/* /sysroot/media
	mkdir /sysroot/mnt
	for a in `ls /mnt ` ; do mkdir /sysroot/mnt/${a};done
	cp /mnt/* /sysroot/mnt
	mkdir /sysroot/root
	mkdir /sysroot/run
	mkdir /sysroot/tmp
	echo "Ubi" > /sysroot/Info.txt
	mknod /sysroot/dev/console c 	5 1
	rm  /sysroot/var/volatile/* -rf
	mount -o bind /var/volatile /sysroot/var/volatile
	
}

do_ubi
fill_ubi

