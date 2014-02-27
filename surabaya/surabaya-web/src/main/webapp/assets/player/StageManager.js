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
function static_url(a){window.console.log("1 static_url: ",a);return a}var kStageIsReadyEvent="StageManager:StageIsReadyEvent";var StageManager=Class.create({initialize:function(a,c){debugMessage(kDebugStageManager_Initialize,"adding event listeners for show & stage size change events...");var b=this;document.observe(kShowSizeDidChangeEvent,function(d){b.handleShowSizeDidChangeEvent(d)},false);document.observe(kStageSizeDidChangeEvent,function(d){b.handleStageSizeDidChangeEvent(d)},false);this.textureManager=a;this.scriptManager=c;this.stage=document.getElementById("stage");this.hyperlinkPlane=document.getElementById("hyperlinkPlane");this.stageWidth=0;this.stageHeight=0;this.showWidth=0;this.showHeight=0;this.audioTrackOffset=0;this.audioTrackIconSize=0},removeTexture:function(a){a.parentNode.removeChild(a)},addHyperlink:function(b){debugMessage(kDebugStageManager_AddHyperlink,"creating div for hyperlink at "+b.x+","+b.y+", "+b.width+", "+b.height);var a=document.createElement("div");a.setAttribute("class","hyperlink");a.style.left=b.x;a.style.top=b.y;a.style.width=b.width;a.style.height=b.height;this.hyperlinkPlane.appendChild(a)},clearAllHyperlinks:function(){debugMessage(kDebugStageManager_ClearAllHyperlinks);var a;while(this.hyperlinkPlane.childNodes.length>0){this.hyperlinkPlane.removeChild(this.hyperlinkPlane.firstChild)}this.audioTrackOffset=this.audioTrackSpacer},handleStageSizeDidChangeEvent:function(a){debugMessage(kDebugStageManager_HandleStageSizeDidChangeEvent,"now: "+a.memo.width+" X "+a.memo.height);this.stageWidth=a.memo.width;this.stageHeight=a.memo.height;debugMessage(kDebugStageManager_HandleStageSizeDidChangeEvent,"- adjust stage...");this.adjustStageToFit(this.stage);debugMessage(kDebugStageManager_HandleStageSizeDidChangeEvent,"- adjust hyperlink plane...");this.adjustStageToFit(this.hyperlinkPlane)},handleShowSizeDidChangeEvent:function(a){debugMessage(kDebugStageManager_HandleShowSizeDidChangeEvent,"now: "+a.memo.width+" X "+a.memo.height);this.showWidth=a.memo.width;this.showHeight=a.memo.height;debugMessage(kDebugStageManager_HandleShowSizeDidChangeEvent,"- adjust stage...");this.adjustStageToFit(this.stage);debugMessage(kDebugStageManager_HandleShowSizeDidChangeEvent,"- adjust hyperlink plane...");this.adjustStageToFit(this.hyperlinkPlane);this.audioTrackIconSize=this.showHeight/4;this.audioTrackSpacer=this.audioTrackIconSize/4;this.audioTrackOffset=this.audioTrackSpacer},anyVideoElementsOnStage:function(){var a=document.getElementsByTagName("video");return a.length>0},setMutedStateOnAllVideoElements:function(a){debugMessage(kDebugStageManager_SetMutedStateOnAllVideoElements);var b=document.getElementsByTagName("video");if(b.length==0){debugMessage(kDebugStageManager_SetMutedStateOnAllVideoElements,"- there are no video elements on the stage right now");return}for(var d=0;d<b.length;d++){var c=b[d];debugMessage(kDebugStageManager_SetMutedStateOnAllVideoElements,"- "+d+": "+c.id);c.muted=a}},adjustStageToFit:function(d){if((this.showWidth!=0)&&(this.stageWidth!=0)){setElementPosition(d,0,0,this.showWidth,this.showHeight);var f=this.stageHeight/this.showHeight;var g=this.stageWidth/this.showWidth;var c=8;var a=c/180*Math.PI;var e=(this.showHeight/2)/Math.tan(a);var b="scaleX( "+g+" ) scaleY( "+f+" )";debugMessage(kDebugStageManager_AdjustStageToFit,"stageTransform: "+b);debugMessage(kDebugStageManager_AdjustStageToFit,"- perspective: "+e);d.style.setProperty(kTransformOriginPropertyName,kTransformOriginTopLeftPropertyValue);d.style.setProperty(kTransformPropertyName,b);d.style.setProperty(kPerspectivePropertyName,e);d.style.setProperty(kPerspectiveOriginPropertyName,kTransformOriginCenterPropertyValue);d.style.setProperty(kTransformStylePropertyName,kTransformStylePreserve3DPropertyValue);document.fire(kStageIsReadyEvent,{})}},debugRecursivelyWalkDomFrom:function(f,d){var b=f.id;var e=f.nodeName.toLowerCase();debugMessage(kDebugStageManager_DebugRecursivelyWalkDomFrom,"- <"+e+"> id='"+b+"'");if(e=="#text"){return}if(e=="img"){var g=f.naturalWidth*f.naturalHeight;debugMessage(kDebugStageManager_DebugRecursivelyWalkDomFrom,"- "+f.naturalWidth+"x"+f.naturalHeight+" = "+g);d.numTextures++;d.numPixels+=g;if(gIpad&&f.src.match("480x268")){d.numDegraded++}return}var a;for(a=0;a<f.childNodes.length;a++){var c=f.childNodes[a];this.debugRecursivelyWalkDomFrom(c,d)}},debugGetStageStatistics:function(){var a={numTextures:0,numPixels:0,numDegraded:0};this.debugRecursivelyWalkDomFrom(this.stage,a);return a},addPreviousEmphasisTransformDiv:function(g,f,a,h,e,d,c){var b=document.createElement("div");b.setAttribute("id",a);b.style.top=0;b.style.left=0;b.style.width=h;b.style.height=e;b.style.position=kPositionAbsolutePropertyValue;b.style.setProperty(kTransformStylePropertyName,kTransformStylePreserve3DPropertyValue);if(d!=""){b.style.setProperty(kTransformOriginPropertyName,d)}if(b){b.style.setProperty(kTransformPropertyName,c)}f.appendChild(b);debugMessage(kDebugStageManager_AddPreviousEmphasisTransformDiv,"- "+g+": "+a+(c?(" - ephasisTransform: "+c):"")+((d=="")?"":" - origin: "+d));return b},resetMovieCount:function(){this.numMoviesInScene=0},addTextureToStage:function(W,U,ah,E,x,ab,B,K,S,p,b){debugMessage(kDebugStageManager_AddTextureToStage);debugMessage(kDebugStageManager_AddTextureToStage,"---------------------------------------------------");debugMessage(kDebugStageManager_AddTextureToStage,"-        a d d T e x t u r e T o S t a g e        -");debugMessage(kDebugStageManager_AddTextureToStage,"---------------------------------------------------");debugMessage(kDebugStageManager_AddTextureToStage,"- textureId: "+U);var i;var F="movieCanvasObject.";var O=U.indexOf(F);if(O!=-1){i=gShowController.movieCanvasObjectIdToTextureIdTable[U];debugMessage(kDebugStageManager_AddTextureToStage,"- it's a movie, which use canvasObjectId, originalTextureId: "+i)}else{i=U}var Z=escapeTextureId(U);var M=escapeTextureId(i);var T=document.createElement("div");var e=T;var ag=gIpad?p.degrade:false;var n=this.textureManager.urlForTexture(i,ag);var m=this.textureManager.urlForMovie(i);var g=(m!="");var Q=false;if(g==true){this.numMoviesInScene++}if((g==true)&&(E<=1)&&(ah<=1)){Q=true;E=0;ah=0}var c;if(Q&&gMode!=kModeMobile){x="hidden"}if(x=="hidden"){c=0}else{c=1}debugMessage(kDebugStageManager_AddTextureToStage,"-    transform: "+K);debugMessage(kDebugStageManager_AddTextureToStage,"-   textureUrl: "+n);debugMessage(kDebugStageManager_AddTextureToStage,"-     movieUrl: "+(g?m:"N/A"));debugMessage(kDebugStageManager_AddTextureToStage,"-  nativeWidth: "+ah);debugMessage(kDebugStageManager_AddTextureToStage,"- nativeHeight: "+E);debugMessage(kDebugStageManager_AddTextureToStage,"-   visibility: "+x+" (rootOpacity: "+c+")");debugMessage(kDebugStageManager_AddTextureToStage,"-      opacity: "+ab);debugMessage(kDebugStageManager_AddTextureToStage,"-       zOrder: "+B);debugMessage(kDebugStageManager_AddTextureToStage,"-    transform: "+K);T.setAttribute("id",Z+"-root");if(Q){ah=this.audioTrackIconSize;E=this.audioTrackIconSize;K="matrix( 1, 0, 0, 1, "+this.audioTrackOffset+", "+(this.showHeight-this.audioTrackSpacer-this.audioTrackIconSize)+" )";debugMessage(kDebugStageManager_AddTextureToStage,"-    transform: "+K+" (modified for audio track)");this.audioTrackOffset+=(this.audioTrackIconSize+this.audioTrackSpacer)}T.style.top="0px";T.style.left="0px";T.style.width=ah+"px";T.style.height=E+"px";T.style.position=kPositionAbsolutePropertyValue;T.style.opacity=c;T.style.setProperty(kTransformPropertyName,K);var y=false;var C=this.scriptManager.script.eventTimelines[W];if(C){var h=C.eventAnimations[0];if(h){if(h.animationType=="transition"){y=true}}}if(!y){T.style.setProperty(kTransformStylePropertyName,kTransformStyleFlatPropertyValue)}else{T.style.setProperty(kTransformStylePropertyName,kTransformStylePreserve3DPropertyValue)}T.style.setProperty(kZIndexPropertyName,Q?100:B);var X=0;debugMessage(kDebugStageManager_AddTextureToStage,"- adding '"+Z+"' - nesting in the following "+S.length+" divs:");debugMessage(kDebugStageManager_AddTextureToStage,"- 0: "+Z+"-root");var z=false;var N=false;var w=false;for(var V=0;V<S.length;V++){var r=S[V];switch(r){case"rotationEmphasis":z=true;break;case"scaleEmphasis":N=true;break;case"translationEmphasis":w=true;break;default:var k=Z+"-"+escapeTextureId(S[V]);debugMessage(kDebugStageManager_AddTextureToStage,"- "+(++X)+": "+k);var l=document.createElement("div");l.setAttribute("id",k);l.style.top=0;l.style.left=0;l.style.width=ah;l.style.height=E;l.style.position=kPositionAbsolutePropertyValue;l.style.setProperty(kTransformStylePropertyName,kTransformStylePreserve3DPropertyValue);e.appendChild(l);e=l;break}}if(w||p.translationEmphasis){var k=Z+"-translationEmphasis";var L="";var a="";var H;if(w){debugMessage(kDebugStageManager_AddTextureToStage,"--- need to add a translationEmphasis div because we have an action build coming up...")}else{debugMessage(kDebugStageManager_AddTextureToStage,"--- need to add a translationEmphasis div because we have a pre-existing emphasisTransform we need to include...")}H=p.translationEmphasis;if(H){a="translateX("+H[0]+"px) translateY("+H[1]+"px) translateZ("+H[2]+"px)"}e=this.addPreviousEmphasisTransformDiv(++X,e,k,ah,E,L,a)}if(N||p.scaleEmphasis){var k=Z+"-scaleEmphasis";var L=N?"":p.scaleEmphasis[0]+" "+p.scaleEmphasis[1]+" "+p.scaleEmphasis[2];var a=N?"":"scale3d("+p.scaleEmphasis[3]+","+p.scaleEmphasis[4]+","+p.scaleEmphasis[5]+")";e=this.addPreviousEmphasisTransformDiv(++X,e,k,ah,E,L,a)}if(z||p.rotationEmphasis){var k=Z+"-rotationEmphasis";var L=z?"":p.rotationEmphasis[0]+"px "+p.rotationEmphasis[1]+"px";var a=z?"":"rotate3d("+p.rotationEmphasis[3]+","+p.rotationEmphasis[4]+","+p.rotationEmphasis[5]+","+p.rotationEmphasis[6]+"rad)";e=this.addPreviousEmphasisTransformDiv(++X,e,k,ah,E,L,a)}if(p.opacityMultiplier){T.style.opacity=p.opacityMultiplier;debugMessage(kDebugStageManager_AddTextureToStage,"- "+(++X)+": "+Z+"-root -  opacityMultiplier: "+p.opacityMultiplier)}if(Q){textureElement=document.createElement("img");if(gMode==kModeMobile){debugMessage(kDebugStageManager_AddTextureToStage,"= since this is an audio track, and we're on a mobile device, we replace the textureUrl with the URL of the waiting_bezel");textureElement.src=static_url("waiting_bezel.png");ab=1}else{debugMessage(kDebugStageManager_AddTextureToStage,"= since this is an audio track, and we're on the desktop, we replace the textureUrl with the URL of the nullImage");textureElement.src=static_url("nullImage.png");ab=0}}else{textureElement=this.textureManager.getImageObjectForTexture(W,i)}if(textureElement==null){debugWarning(kDebugStageManager_AddTextureToStage,"textureElement returned from textureManager is null - must be a movie ("+U+")")}textureElement.id=Z;var o=false;if(gIpad&&this.numMoviesInScene>kiPadMaxMoviesPerScene){debugMessage(kDebugStageManager_AddTextureToStage,"- we're on an iPad and we already have "+kiPadMaxMoviesPerScene+" movies in this scene, can't add any more...");o=true}if(g&&!o){debugMessage(kDebugStageManager_AddTextureToStage,"- it's "+(Q?"an audio track":"a movie")+". URL: "+n);var u;var ae;var q;var af;var Y;var A=Z+"-movieObject";q=0;af=0;Y="visible";if(Q&&gMode==kModeMobile&&!gIpad){u=1;ae=1;q=1000;af=1000}else{if(gPlayMoviesInPlace){u=ah;ae=E}else{u=0;ae=0;Y="hidden"}}if(gMoviesRespectTransforms){A=Z+"-movieObject"}else{A=Z+"-movieObjectClone";q=1000;af=1000}debugMessage(kDebugStageManager_AddTextureToStage,"- Movie Geometry: ");debugMessage(kDebugStageManager_AddTextureToStage,"-          top: "+q);debugMessage(kDebugStageManager_AddTextureToStage,"-         left: "+af);debugMessage(kDebugStageManager_AddTextureToStage,"-        width: "+u);debugMessage(kDebugStageManager_AddTextureToStage,"-       height: "+ae);debugMessage(kDebugStageManager_AddTextureToStage,"-   visibility: "+Y);if(b==false){debugMessage(kDebugStageManager_AddTextureToStage,"- using movieObjectId: '"+A+"'");var j=document.createElement("video");j.id=A;j.src=m;j.muted=gShowController.muted;j.poster=n;j.style.visibility=Y;j.style.position=kPositionAbsolutePropertyValue;j.style.top=q+"px";j.style.left=af+"px";j.style.width=u+"px";j.style.height=ae+"px";j.style.opacity=ab;if(gMode!=kModeMobile){var f=this.scriptManager.script.textures[i];debugMessage(kDebugStageManager_AddTextureToStage,"- textureInfo: "+f);debugMessage(kDebugStageManager_AddTextureToStage,"- textureInfo.movieVolume: "+f.movieVolume);var R=f.movieVolume;var d=f.movieLooping;if(R){debugMessage(kDebugStageManager_AddTextureToStage,"- volume attribute supplied: "+R);j.volume=R}if(d){debugMessage(kDebugStageManager_AddTextureToStage,"- looping supplied: "+d);if(d!="none"){j.loop="loop"}}}e.appendChild(j)}}textureElement.style.top=0;textureElement.style.left=0;textureElement.style.position=kPositionAbsolutePropertyValue;textureElement.style.width=ah;textureElement.style.height=E;textureElement.style.opacity=ab;textureElement.style.setProperty(kBackfaceVisibilityPropertyName,kBackfaceVisibilityHiddenPropertyValue);if(b==false){e.appendChild(textureElement)}if(g&&gMode==kModeMobile&&!o){var ad=parseTransformMatrix(K);var ac=65;var D=32;var G;var aa;var J=1;var I=1;var t;if(Q){aa=E/2.5;G=aa*24/21;t=static_url("snd_on_n.png")}else{J=1/ad.scaleX;I=1/ad.scaleY;if((E>ac)&&(ah>ac)){G=ac;aa=ac;t=static_url("play_badge.png")}else{G=D;aa=D;t=static_url("play_badge_sm.png")}}var v=new Image();v.id=A+"-playBadge";v.src=t;v.style.visibility="visible";v.style.position=kPositionAbsolutePropertyValue;v.style.width=G+"px";v.style.height=aa+"px";v.style.left=(ah-G)/2+"px";if(Q){v.style.top=(E*0.9-aa)/2+"px"}else{v.style.top=(E-aa)/2+"px"}var s="matrix("+J+",0,0,"+I+",0,0)";debugMessage(kDebugStageManager_AddTextureToStage,"- playBadgeTransform: "+s);v.style.setProperty(kTransformPropertyName,s);e.appendChild(v);debugMessage(kDebugStageManager_AddTextureToStage,"- creating hyperlink over region: x: "+ad.x+", y: "+ad.y+", w: "+ah+", h: "+E);var P={x:ad.x,y:ad.y,width:ah,height:E};gShowController.addMovieHyperlink(P,"playMovie:"+A)}debugMessage(kDebugStageManager_AddTextureToStage,"- adding rootDiv for this texture to stage...");if(b==false){this.stage.appendChild(T)}}});
