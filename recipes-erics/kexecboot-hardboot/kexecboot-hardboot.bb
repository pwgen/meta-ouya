RDEPENDS_${PN} = "kexec-tools-hardboot-klibc"
PV = "0.5.9"
PR = "r1+hardbootr${SRCREV}"

SRC_URI = "git://github.com/kexecboot/kexecboot.git;protocol=git"
SRCREV = "0daa774eac019602cd89048961e95985ea50dadf"
S = "${WORKDIR}/git"

require kexecboot.inc
BBCLASSEXTEND = "klibc"
