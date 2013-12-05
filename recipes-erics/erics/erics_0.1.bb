DESCRIPTION = "Erics  Ststemd target and other stuff"
SECTION = "base"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${WORKDIR}/COPYRIGHT;md5=349c872e0066155e1818b786938876a4"
RDEPENDS_${PN} = "systemd"
PR = "r2"

SRC_URI = "file://erics \
	   file://erics_test.c \
	   file://COPYRIGHT \
	   file://opkg.conf \
	   file://all-feed.conf \
	   file://raspberrypi-feed.conf \
	   file://armv6-vfp-feed.conf\
	   file://armv6-hf-feed.conf\	 
	   file://erics.service\
	   file://erics.target\
	   file://erics.sh\
	   file://update-initrd.sh\
	"
S = "${WORKDIR}/"
#inherit allarch
PACKAGES =+ "erics-systemd"
CONFFILES_${PN} += "${sysconfdir}/init.d/erics"

FILES_${PN} += "/bin/erics-test \
  	 /etc/opkg/opkg.conf\
	/etc/opkg/all-feed.conf\
	 /etc/opkg/raspberrypi-feed.conf\
	/etc/opkg/armv6-vfp-feed.conf\
	/lib/systemd/system/erics.service\
	/lib/systemd/system/erics.target\
	/lib/systemd/system/erics.target.wants/erics.service\
	/etc/init.d/erics\
	/usr/bin/erics.sh\
	/usr/bin/update-initrd.sh\
	/etc/systemd/system/multi-user.target.wants/erics.service\
"

do_compile () {
	${CC} ${WORKDIR}/erics_test.c -o ${WORKDIR}/erics-test
}

do_install () {
	install -d ${D}${sysconfdir}/init.d
	cat ${WORKDIR}/erics | \
	  sed -e 's,/etc,${sysconfdir},g' \
	      -e 's,/usr/sbin,${sbindir},g' \
	      -e 's,/var,${localstatedir},g' \
	      -e 's,/usr/bin,${bindir},g' \
	      -e 's,/usr,${prefix},g' > ${D}${sysconfdir}/init.d/erics
	chmod a+x ${D}${sysconfdir}/init.d/erics
	install -d ${D}/lib
#	install -d ${D}/sysroot
	install -d ${D}/lib/systemd
	install -d ${D}/lib/systemd/system
	install -d ${D}/etc
	install -d ${D}/etc/systemd/
	install -d ${D}/etc/systemd/system
	install -d ${D}/etc/systemd/system/multi-user.target.wants	
	install -d ${D}/lib/systemd/system/erics.target.wants
	install -d ${D}/etc/systemd/system/erics.target.wants
	ln -sf    /lib/systemd/system/erics.service  ${D}/etc/systemd/system/multi-user.target.wants/erics.service
	ln -sf    /lib/systemd/system/erics.service  ${D}/etc/systemd/system/erics.target.wants/erics.service
	ln -sf    /lib/systemd/system/erics.service  ${D}/lib/systemd/system/erics.target.wants/erics.service
#        ln -s ${systemd_unitdir}/systemd ${D}/init
	install -m 0755 ${WORKDIR}/erics.target  ${D}/lib/systemd/system/ 
	install -m 0755 ${WORKDIR}/erics.service  ${D}/lib/systemd/system/ 
	install -d ${D}${bindir}
	install -m 0755 ${WORKDIR}/erics.sh ${D}${bindir}/erics.sh
	install -m 0755 ${WORKDIR}/update-initrd.sh ${D}${bindir}/update-initrd.sh
	install -d ${D}/etc/opkg
	install -m 0755 ${WORKDIR}/opkg.conf ${D}/etc/opkg/
	install -m 0755 ${WORKDIR}/armv6-vfp-feed.conf ${D}/etc/opkg/
	install -m 0755 ${WORKDIR}/armv6-hf-feed.conf ${D}/etc/opkg/
	install -m 0755 ${WORKDIR}/raspberrypi-feed.conf ${D}/etc/opkg/
	install -m 0755 ${WORKDIR}/all-feed.conf ${D}/etc/opkg/
}



#SRC_URI += "file://erics.socket file://erics@.service "



