DESCRIPTION = "Init system for liveramdisk"
SECTION = "base"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${WORKDIR}/COPYRIGHT;md5=349c872e0066155e1818b786938876a4"
RDEPENDS_${PN} = "systemd"
#RDEPENDS_${PN} = "initscripts"
PR = "r0"

SRC_URI = "file://liveramdisk \
	   file://COPYRIGHT \
	 file://initrd-release file://shadow  file://loadnand.sh\
	   "
#	   file://liveramdisk_test.c 
#	   file://init	
#	   file://initrd-switch-root.service file://initrd-switch-root.target file://dracut-pre-pivot.sh file://dracut-pre-pivot.service file://initrd-release 

CONFFILES_${PN} += "${sysconfdir}/init.d/liveramdisk"
#FILES_${PN} += " /init "
FILES_${PN} += " /sbin/loadnand.sh \
		/etc/init.d/liveramdisk"


#do_compile () {./meta-openembedded/meta-systemd/oe-core
#	${CC} ${WORKDIR}/erics_test.c -o ${WORKDIR}/erics-test
#}

do_install () {
	install -d ${D}${sysconfdir}/init.d
	cat ${WORKDIR}/liveramdisk | \
	  sed -e 's,/etc,${sysconfdir},g' \
	      -e 's,/usr/sbin,${sbindir},g' \
	      -e 's,/var,${localstatedir},g' \
	      -e 's,/usr/bin,${bindir},g' \
	      -e 's,/usr,${prefix},g' > ${D}${sysconfdir}/init.d/liveramdisk
	chmod a+x ${D}${sysconfdir}/init.d/liveramdisk
#	cd ${D}${sysconfdir}/
#	ln -s init.d/liveramdisk ${D}${sysconfdir}/rc5.d/S98liveramdisk
	install -d ${D}${sbindir}
	install -d ${D}/sbin
	install -d ${D}/lib
	install -d ${D}/sysroot
	install -d ${D}/lib/systemd
#	install -m 0755 ${WORKDIR}/initrd-switch-root.service  ${D}/lib/systemd/	
#	install -m 0755 ${WORKDIR}/initrd-switch-root.target  ${D}/lib/systemd/
#	install -m 0755 ${WORKDIR}/dracut-pre-pivot.sh   ${D}/lib/systemd/
#	install -m 0755 ${WORKDIR}/dracut-pre-pivot.service  ${D}/lib/systemd/
	install -m 0755 ${WORKDIR}/initrd-release  ${D}${sysconfdir}/initrd-release
#	rm ${D}${sysconfdir}/shadow
	install -m 0644 ${WORKDIR}/shadow  ${D}${sysconfdir}/shadow
	install -m 0644 ${WORKDIR}/shadow  ${D}${sysconfdir}/shadow-

	cp ${D}${sysconfdir}/shadow- ${D}${sysconfdir}/shadow

#	install -m 0755 ${WORKDIR}/init ${D}/init
	ln -sf  ${D}/lib/systemd/lib/systemd/systemd init
#	chmod ugo+rx ${D}/init
	install -m 0755  ${WORKDIR}/loadnand.sh ${D}/sbin
		
}	

#find . | cpio --create --'format=newc' | gzip >/media/CA41-6388/Image.initrd 


