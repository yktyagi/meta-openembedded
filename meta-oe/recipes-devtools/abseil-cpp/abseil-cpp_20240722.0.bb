SUMMARY = "Abseil is a cpp library like STL"
DESCRIPTION = "Abseil provides pieces missing from the C++ standard. Contains \
additional useful libraries like algorithm, container, debugging, hash, memory, \
meta, numeric, strings, synchronization, time, types and utility."
HOMEPAGE = "https://abseil.io/"
SECTION = "libs"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=df52c6edb7adc22e533b2bacc3bd3915"

SRCREV = "4447c7562e3bc702ade25105912dce503f0c4010"
BRANCH = "lts_2024_07_22"
SRC_URI = "git://github.com/abseil/abseil-cpp;branch=${BRANCH};protocol=https \
           file://0001-absl-always-use-asm-sgidefs.h.patch             \
           file://0002-Remove-maes-option-from-cross-compilation.patch \
           file://0003-Remove-neon-option-from-cross-compilation.patch \
           file://0004-abseil-ppc-fixes.patch \
          "

S = "${WORKDIR}/git"

inherit cmake

EXTRA_OECMAKE = "-DBUILD_SHARED_LIBS=ON \
                 -DBUILD_TESTING=OFF    \
                 -DABSL_ENABLE_INSTALL=ON \
                "

SYSROOT_DIRS:append:class-nativesdk:mingw32 = " ${bindir}"

PACKAGES_DYNAMIC = "^libabsl-*"
PACKAGES_DYNAMIC:class-native = ""

PACKAGESPLITFUNCS =+ "split_dynamic_packages"

python split_dynamic_packages() {
    libdir = d.getVar('libdir')

    libpackages = do_split_packages(
        d,
        root=libdir,
        file_regex=r'^libabsl_(.*)\.so\..*$',
        output_pattern='libabsl-%s',
        description="abseil shared library %s",
        prepend=True,
        extra_depends='',
    )
    if libpackages:
        d.appendVar('RDEPENDS:' + d.getVar('PN'), ' ' + ' '.join(libpackages))
}

ALLOW_EMPTY:${PN} = "1"

BBCLASSEXTEND = "native nativesdk"