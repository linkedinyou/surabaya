#-------------------------------------------------------------------------------
# Copyright (c) 2014 Akira Sonoda.
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the GNU Public License v3.0
# which accompanies this distribution, and is available at
# http://www.gnu.org/licenses/gpl.html
# 
# Contributors:
#     Akira Sonoda - initial API and implementation
#-------------------------------------------------------------------------------
var kOrientationChangedEvent="OrientationController:OrientationChangedEvent";var OrientationController=Class.create({initialize:function(){var a=this;if(gDevice==kDeviceMobile){debugMessage(kDebugOrientationController_Initialize,"adding event listener for window orientation change events...");window.onorientationchange=function(b){a.handleDeviceOrientationChangeEvent(b)};this.handleDeviceOrientationChangeEvent()}else{debugMessage(kDebugOrientationController_Initialize,"adding event listener for window resize events...");window.addEventListener("resize",function(b){a.handleWindowResizeEvent(b)},false);this.handleWindowResizeEvent()}this.orientation=kOrientationUnknown},handleWindowResizeEvent:function(b){debugMessage(kDebugOrientationController_HandleWindowResizeEvent,"");var a=kOrientationUnknown;if(window.innerWidth<window.innerHeight){a=kOrientationPortrait}else{a=kOrientationLandscape}this.changeOrientation(a)},handleDeviceOrientationChangeEvent:function(b){debugMessage(kDebugOrientationController_HandleDeviceOrientationChangeEvent,"");var c=window.orientation;var a=kOrientationUnknown;if((c==0)||(c==180)){a=kOrientationPortrait}else{a=kOrientationLandscape}this.changeOrientation(a)},changeOrientation:function(a){this.orientation=a;document.fire(kOrientationChangedEvent,{orientation:this.orientation})}});
