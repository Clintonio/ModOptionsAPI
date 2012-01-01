#!/bin/bash

# Config that needs editing per mod/ change
MCPDIR="../../dev/mcpcur"
MODNAME="moapi"
VERSION=`cat version`
OUTNAME=$MODNAME".$VERSION"
DEVNAME=$MODNAME".dev.$VERSION"

# Config that unlikely needs changing
DOCDIR="./doc"
LIBDIR="./lib"
RELDIR="./rel"
SRCDIR="./src"
BINDIR="./bin"

# Variables derived from config
MODDIR=$MCPDIR/$MODNAME

# MCP dependent variables
MCPSRC=$MODDIR"/modsrc"
MCPBIN=$MODDIR"/reobf"
MCPLIB=$MODDIR"/bin"

# Derived vars
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

# Remove old files
rm -r $DOCDIR
rm -r $LIBDIR
rm -r $RELDIR
rm -r $SRCDIR
rm -r $BINDIR

# Replace dirs
mkdir $DOCDIR
mkdir $LIBDIR
mkdir $RELDIR
mkdir $SRCDIR
mkdir $BINDIR

# Generate the sources
cd $MODDIR
./recompile.sh
./reobfuscate.sh
./getmodsource.sh

cd $DIR

# Simple operations
cp -r $MCPSRC"/client/"* $SRCDIR
cp -r $MCPBIN"/minecraft/"* $BINDIR

# Create the documentation
javadoc -quiet -classpath "$MODDIR/jars/bin/lwjgl.jar:$MODDIR/bin/minecraft/" -d $DOCDIR -sourcepath $SRCDIR $SRCDIR/moapi/*.java $SRCDIR/moapi/**/*.java

# Copy the lib files

mkdir -p $LIBDIR/net/minecraft/src

cp -r $MCPLIB/minecraft/moapi/ $LIBDIR/
cp $MCPLIB/minecraft/net/minecraft/src/GuiConnecting.class $LIBDIR/net/minecraft/src/GuiConnecting.class
cp $MCPLIB/minecraft/net/minecraft/src/GuiSelectWorld.class $LIBDIR/net/minecraft/src/GuiSelectWorld.class
cp $MCPLIB/minecraft/net/minecraft/src/GuiOptions.class $LIBDIR/net/minecraft/src/GuiOptions.class
cp $MCPLIB/minecraft/net/minecraft/src/GuiIngameMenu.class $LIBDIR/net/minecraft/src/GuiIngameMenu.class

# Pack the client file into a ZIP folder
zip -b $BINDIR -b $BINDIR -r $RELDIR/$OUTNAME.zip *
zip -b $BINDIR -x "rel/*" -x pack.sh -r $RELDIR/$DEVNAME.zip *
