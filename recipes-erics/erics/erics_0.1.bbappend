FLESEXTRAPATHS := "${THISDIR}/${PN}"

PRINC := "${@int(PRINC) + 1}"


#SYSTEMD_PACKAGES = "${PN}-systemd"
SYSTEMD_PACKAGES = "erics-systemd"
SYSTEMD_SERVICE = "erics1@.service"
SYSTEMD_AUTO_ENABLE = "disable"

inherit systemd
SRC_URI_append += "file://erics@.service"



