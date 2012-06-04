#!/bin/bash

# Config that needs editing per mod/ change
MCPDIR="../../dev/mcpcur"
MODNAME="moapi"
VERSION=`cat version`
OUTNAME=$MODNAME".$VERSION.zip"
DEVNAME=$MODNAME".dev.$VERSION.zip"
SSHLOC=clint@apollo
PUBDIR="~/links/mcmods/$MODNAME/$VERSION"
MODURL="http://www.digitaluppercut.com/downloads/mc/mods/$MODNAME/$VERSION"

# Config that unlikely needs changing
DOCDIR=./doc
LIBDIR=./lib
RELDIR=./rel
SRCDIR=./src
BINDIR=./bin
PATCHDIR=./"patch"

# Variables derived from config
MODDIR=$MCPDIR/$MODNAME

# MCP dependent variables
MCPSRC=$MODDIR"/modsrc"
MCPBIN=$MODDIR"/reobf"
MCPLIB=$MODDIR"/bin"

# Derived vars
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

read -p "Have you commited to GIT and updated the readme and version files? (y/n) "

if [ "$REPLY" == "y" ] 
	then
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
	cp -r $MCPSRC"/minecraft/"* $SRCDIR
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
	cd $BINDIR
	zip -q -r $DIR/$RELDIR/$OUTNAME *
	cd $DIR
	zip -q -x "rel/*" -x pack.sh -x urls.txt -r $RELDIR/$DEVNAME *
	
	# Ask to publish
	read -p "Publish version "$VERSION" to live? (y/n) "
	if [ "$REPLY" == "y" ] 
		then
		ssh $SSHLOC "mkdir -p $PUBDIR/"
		scp $RELDIR/* $SSHLOC:$PUBDIR/
		scp $PATCHDIR/* $SSHLOC:$PUBDIR/
		ssh $SSHLOC "chmod -R 0775 $PUBDIR/"
		echo "scp $RELDIR/* $SSHLOC:$PUBDIR/"
		
		echo -e "$MODURL/$OUTNAME\n$MODURL/$DEVNAME" > urls.txt 
	fi
fi
