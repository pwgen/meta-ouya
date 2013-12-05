COMPATIBLE_MACHINE = "ouya"

#require linux.inc
inherit kernel
DESCRIPTION = "Linux kernel for the Ouya"


LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=d7810fab7487fb0aad327b76f1be7cd7"
PR = "r1"

# Bump MACHINE_KERNEL_PR in the machine config if you update the kernel.
# This is on the rpi-patches branch
#SRCREV = "523029f607564ab2080e83a3384feac4439b2b38" #3.6.9
#SRCREV = "95009dbb77849634cefea237dc952881406b0119" #3.6.11
#SRCREV = "b7153abdcb98a2ce2fcf74474f97fa9ffb2b0ba0" #3.6.11+
SRCREV = "daea9942a8b09b905d998ad3837479de8c5127b1"
SRCREV = "00f7e2cba75c7762a9fe579d034692db082095e5"
#https://github.com/ouya/ouya_1_1-kernel.git
#SRC_URI = "git://github.com/raspberrypi/linux.git;protocol=git;branch=rpi-i3.6.y 
SRC_URI = "git://github.com/ouya/ouya_1_1-kernel.git;protocol=git;branch=master \
	file://kern_kexec.patch\
	file://defconfig \
"          
#file://kern_video.patch
#	

LINUX_VERSION ?= "3.1.10-ouya"
#PV = "${LINUX_VERSION}+${PR}+git${SRCREV}"
PV = "${LINUX_VERSION}+${PR}"
S = "${WORKDIR}/git"

# NOTE: For now we pull in the default config from the RPi kernel GIT tree.

#KERNEL_DEFCONFIG = "bcmrpi_defconfig"
KERNEL_DEFCONFIG = "defconfig"
#KERNEL_DEFCONFIG = ".config"
# CMDLINE for raspberrypi
#CMDLINE_raspberrypi = "dwc_otg.lpm_enable=0 console=ttyAMA0,115200 kgdboc=ttyAMA0,115200 root=/dev/mmcblk0p2 rootfstype=ext4 rootwait"
#CMDLINE_ouya = ""
PARALLEL_MAKEINST = ""
#do_unpack_prepend() {
#        install -m 0644 ${WORKDIR}/defconfig-3.1.10 ${WORKDIR}/defconfig
#        install -m 0644 ${WORKDIR}/defconfig ${S}/.config
#}
do_configure_prepend() {
#        install -m 0644 ${WORKDIR}/defconfig-3.1.10 ${WORKDIR}/defconfig
#	rm ${S}/.config -f
#        cp ${WORKDIR}/defconfig ${S}/TESTTEST 
        cp ${WORKDIR}/defconfig ${S}/arch/arm/configs/defconfig
}

#do_install_prepend() {
#	install -d ${D}/lib/firmware
#}
