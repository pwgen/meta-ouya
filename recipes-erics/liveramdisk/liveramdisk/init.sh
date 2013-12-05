#!/bin/sh

MODULE_DIR=/initrd.d
BOOT_ROOT=
ROOT_DEVICE=

early_setup() {
    mkdir -p /proc /sys /mnt /tmp
    mount -t proc proc /proc
    mount -t sysfs sysfs /sys
    modprobe -q mtdblock

    
}

dev_setup()
{
    echo -n "initramfs: Creating device nodes: "
    grep '^ *[0-9]' /proc/partitions | while read major minor blocks dev
    do
        if [ ! -e /dev/$dev ]; then
            echo -n "$dev "
            [ -e /dev/$dev ] || mknod /dev/$dev b $major $minor
        fi
    done
    mknod /dev/mmcblk0 b 179 0
    mknod /dev/mmcblk0p1 b 179 1
    mknod /dev/mmcblk0p2 b 179 2
    mknod /dev/mmcblk0p3 b 179 3
    mknod /dev/mmcblk0p4 b 179 4
    echo "done creating dev nodes"
}

read_args() {
    [ -z "$CMDLINE" ] && CMDLINE=`cat /proc/cmdline`
    for arg in $CMDLINE; do
        optarg=`expr "x$arg" : 'x[^=]*=\(.*\)'`
        case $arg in
            root=*)
                ROOT_DEVICE=$optarg ;;
            rootfstype=*)
                ROOT_FSTYPE=$optarg ;;
            rootdelay=*)
                rootdelay=$optarg ;;
            debug) set -x ;;
            shell) sh ;;
        esac
    done
}

do_depmod() {
	[ -e "/lib/modules/$(uname -r)/modules.dep" ] || depmod
}

load_module() {
    # Cannot redir to $CONSOLE here easily - may not be set yet
    echo "initramfs: Loading $module module"
    source $1
}

load_modules() {
    for module in $MODULE_DIR/$1; do
        [ -e "$module"  ] && load_module $module
    done
}

boot_root() {
    cd $BOOT_ROOT
mknod /mnt/ubi/dev/console c 	5 1
umount /sys
umount /proc
exec /usr/sbin/switch_root -c /dev/console $BOOT_ROOT /sbin/init

}

fatal() {
    echo $1 >$CONSOLE
    echo >$CONSOLE
    exec sh
}
do_special(){
mkdir /mnt/sdcard
mount /dev/mmcblk0p1 /mnt/sdcard && cp /mnt/sdcard/special.sh /tmp && umount /dev/mmcblk0p1 && chmod ugo+rwx /tmp/special.sh && exec /tmp/special.sh
}
############ the beginning

echo "Starting initramfs boot..."
early_setup
load_modules '0*'
do_depmod
[ -z "$CONSOLE" ] && CONSOLE="/dev/console"

read_args

if [ -z "$rootdelay" ]; then
    echo "rootdelay parameter was not passed on kernel command line - assuming 2s delay"
    echo "If you would like to avoid this delay, pass explicit rootdelay=0"
    rootdelay="2"
fi
if [ -n "$rootdelay" ]; then
    echo "Waiting $rootdelay seconds for devices to settle..." >$CONSOLE
    sleep $rootdelay
fi

dev_setup
load_modules '[1-9]*'
echo " done depmod"
#/bin/sh
#exec /sbin/loadnand.sh
do_special
#/bin/sh

#/bin/sh
#do_special
BOOT_ROOT=/mnt/ubi
[ -n "$BOOT_ROOT" ] && boot_root
fatal "No valid root device was specified.  Please add root=/dev/something to the kernel command-line and try again."
