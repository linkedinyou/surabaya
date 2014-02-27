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
var kShowSizeDidChangeEvent="ScriptManager:ShowSizeDidChangeEvent";var kScriptDidDownloadEvent="ScriptManager:ScriptDidDownloadEvent";var kScriptDidNotDownloadEvent="ScriptManager:ScriptDidNotDownloadEvent";var ScriptManager=Class.create({initialize:function(a){debugMessage(kDebugScriptMangaer_Initialize);this.script=null;this.showUrl=a;this.debugFirstTime=true},downloadScript:function(){debugMessage(kDebugScriptMangaer_DownloadScript,"showUrl: "+this.showUrl);this.downloadTimeout=setTimeout(function(){c.scriptDidNotDownload(null)},kMaxScriptDownloadWaitTime);this.downloadAlreadyFailed=false;var d=new Date();var e=this.showUrl+"/kpf.json?ts="+escape(d.valueOf());var c=this;var b=function(f){c.scriptDidDownload(f)};var a=function(f){c.scriptDidNotDownload(f)};if(gDebugSimulateScriptDownloadFailure==true){if(this.debugFirstTime){debugMessage(kDebugScriptMangaer_DownloadScript,"overridding succes completion routine for script download...");b=function(f){c.scriptDidNotDownload_debug(f)};this.debugFirstTime=false}}debugMessage(kDebugScriptMangaer_DownloadScript,"starting download of script from: "+e);new Ajax.Request(e,{method:"get",onSuccess:b,onFailure:a})},scriptDidDownload:function(transport){debugMessage(kDebugScriptMangaer_ScriptDidDownload,"download complete.");clearTimeout(this.downloadTimeout);debugMessage(kDebugScriptMangaer_ScriptDidDownload,"parsing script...");this.script=eval("("+transport.responseText+")");debugMessage(kDebugScriptMangaer_ScriptDidDownload,"script parsing complete.");debugMessage(kDebugScriptMangaer_ScriptDidDownload,"        filename: "+this.script.filename);debugMessage(kDebugScriptMangaer_ScriptDidDownload," nativeShowWidth: "+this.script.slideWidth);debugMessage(kDebugScriptMangaer_ScriptDidDownload,"nativeShowHeight: "+this.script.slideHeight);debugMessage(kDebugScriptMangaer_ScriptDidDownload,"       showWidth: "+showWidth);debugMessage(kDebugScriptMangaer_ScriptDidDownload,"      showHeight: "+showHeight);var script=this.script;var showWidth=this.script.slideWidth;var showHeight=this.script.slideHeight;this.preProcessScript();document.fire(kScriptDidDownloadEvent,{script:script});document.fire(kShowSizeDidChangeEvent,{width:showWidth,height:showHeight})},preProcessScript:function(){debugMessage(kDebugScriptMangaer_PreProcessScript);debugMessage(kDebugScriptMangaer_PreProcessScript,"- adjust for the fact that we don't play the last 'fade-to-black' scene...");this.script.numScenes=this.script.eventTimelines.length-1;this.script.lastSceneIndex=this.script.numScenes-1;this.script.lastSlideIndex=this.script.navigatorEvents.length-1;this.degradeStatistics={numDegradedSlides:0,numDegradedTextures:0,maxNumDegradedTexturesPerSlide:0};if(gIpad){var j=this.script.eventTimelines.length;debugMessage(kDebugScriptMangaer_PreProcessScript,"- perform per-scene pixel bugeting... (there are "+j+" scenes)");var y;for(y=0;y<j;y++){var e=this.script.eventTimelines[y];var v=e.eventInitialStates;var a=v.length;var k=0;var l=0;debugMessage(kDebugScriptMangaer_PreProcessScript_Detailed,"- Scene: "+y+" ("+a+" textures)");var b=0;var f=true;var c=false;var q;while(f&&!c){debugMessage(kDebugScriptMangaer_PreProcessScript_Detailed,"-    Pass: "+b);var s=0;var n=-1;var r=-1;var h=-1;for(q=0;q<a;q++){var x=v[q];var B=x.texture;var o=this.script.textures[B];var w=o.width;var u=o.height;var m=Math.floor(w*u);var z=w*this.script.scalefactor480;var i=w*this.script.scalefactor480;var p=Math.floor(z*i);var d=false;var g=false;var t=(x.hasOwnProperty("degrade")&&x.degrade);var A=(t?p:m);s+=A;if(!t){if(B.match(".background")){h=q;g=true}if(m>r){d=true;r=m;n=q}}debugMessage(kDebugScriptMangaer_PreProcessScript_ExtremelyDetailed,"-       "+q+": "+B+" using: "+(t?"small":"large")+" ("+A+" pixels)")}debugMessage(kDebugScriptMangaer_PreProcessScript_Detailed,"-       End of Pass: "+b+" totalPixelsPerScene: "+s+" safePixelCount: "+gSafeMaxPixelCount);if(l==0){l=s}if(s>gSafeMaxPixelCount){debugMessage(kDebugScriptMangaer_PreProcessScript_Detailed,"-          Still too big... ("+s+" vs. "+gSafeMaxPixelCount+")");if(h!=-1){debugMessageAlways(kDebugScriptMangaer_PreProcessScript_Detailed,"-          Degrading background: "+this.script.eventTimelines[y].eventInitialStates[n].texture);this.script.eventTimelines[y].eventInitialStates[h].degrade=true;k++}else{if(n!=-1){debugMessageAlways(kDebugScriptMangaer_PreProcessScript_Detailed,"-          Degrading largest texture: "+n+": "+this.script.eventTimelines[y].eventInitialStates[n].texture);this.script.eventTimelines[y].eventInitialStates[n].degrade=true;k++}else{debugMessage(kDebugScriptMangaer_PreProcessScript_Detailed,"-          Still too big, but we're out of textures to degrade, uh-oh!");c=true}}}else{debugMessage(kDebugScriptMangaer_PreProcessScript_Detailed,"-          Within limits");f=false}b++}this.degradeStatistics.numDegradedTextures+=k;if(k>0){debugMessage(kDebugScriptMangaer_PreProcessScript,"-    Summary for Scene: "+y+" - degraded "+k+" of "+a+" textures to go from "+l+" to "+s);this.degradeStatistics.numDegradedSlides++}if(k>this.degradeStatistics.maxNumDegradedTexturesPerSlide){this.degradeStatistics.maxNumDegradedTexturesPerSlide=k}}}},scriptDidNotDownload_debug:function(a){debugWarning(kDebugScriptMangaer_ScriptDidNotDownload,"simulating failure of script download");this.scriptDidNotDownload(a)},scriptDidNotDownload:function(a){debugWarning(kDebugScriptMangaer_ScriptDidNotDownload,(a!=null)?"Ajax.Request returned an error":"kMaxScriptDownloadWaitTime exceeded while waiting for script to download");if(this.downloadAlreadyFailed){debugMessage(kDebugScriptMangaer_ScriptDidNotDownload,"already executed due to previous timeout or onFailure invocation, ignoring...")}this.downloadAlreadyFailed=true;if(a){debugWarning(kDebugScriptMangaer_ScriptDidNotDownload,"readyState: "+a.readyState+" status: "+a.status);clearTimeout(this.downloadTimeout)}document.fire(kScriptDidNotDownloadEvent,{})},isOnlyActionInSceneAMovieStart:function(e){debugMessage(kDebugScriptMangaer_IsOnlyActionInSceneAMovieStart,"sceneIndex: "+e);if(e<=-1){debugMessageAlways(kDebugScriptMangaer_IsOnlyActionInSceneAMovieStart,"- sceneIndex less than zero, returning false");return false}if(e>this.script.lastSceneIndex){debugMessageAlways(kDebugScriptMangaer_IsOnlyActionInSceneAMovieStart,"- sceneIndex ("+e+") greater than this.script.lastSceneIndex, returning false");return false}var b=this.script.eventTimelines[e];var d=b.eventAnimations;var c;for(c=0;c<d.length;c++){var a=d[c];debugMessage(kDebugScriptMangaer_IsOnlyActionInSceneAMovieStart,"- eventAnimationIndex: "+c+" effect: "+a.effect);if(a.effect!="apple:movie-start"){debugMessage(kDebugScriptMangaer_IsOnlyActionInSceneAMovieStart,"-  this one's not a movie start action, return false");return false}}debugMessage(kDebugScriptMangaer_IsOnlyActionInSceneAMovieStart,"-  nothing but movie start actions, return true");return true},isMovieInScene:function(k,i,g){debugMessage(kDebugScriptMangaer_IsMovieInScene,"sceneIndex: "+k+" movieId: '"+i+"'");if(k==-1){debugMessage(kDebugScriptMangaer_IsMovieInScene,"- a value of -1 specified for sceneIndex, returning false...");return false}else{var j=this.script.eventTimelines[k];var c=j.eventInitialStates;var b=c.length;var e=i.substring(i.indexOf("-")+1);var h=this.slideIndexFromSceneIndex(k);debugMessage(kDebugScriptMangaer_IsMovieInScene,"- looking for a movie with textureId: "+e);var f;for(f=0;f<b;f++){var l=c[f];var a=l.texture;var d=h+"-"+l.canvasObjectID;debugMessage(kDebugScriptMangaer_IsMovieInScene,"- "+f+": "+a+" canvasObjectId: "+l.canvasObjectID);if(e==d){debugMessage(kDebugScriptMangaer_IsMovieInScene,"- found movie at texture index: "+f);return true}}debugMessage(kDebugScriptMangaer_IsMovieInScene,"- movie not found");return false}},getMoviesInScene:function(h){debugMessage(kDebugScriptMangaer_GetMoviesInScene,"sceneIndex: "+h);if(this.script==null){return null}if(h>this.script.lastSceneIndex){return null}var e=new Array();var a=this.script.eventTimelines[h];var f=a.eventInitialStates;var c=f.length;var d;for(d=0;d<c;d++){var b=f[d];var g=b.texture;debugMessage(kDebugScriptMangaer_GetMoviesInScene," - textureId: "+g);if(g.indexOf("movie")!=-1){debugMessage(kDebugScriptMangaer_GetMoviesInScene," - is a movie, add to list...");e.push(g)}}return e},sceneIndexFromSlideIndex:function(a){if((this.script==null)||(a<0)||(a>=this.script.navigatorEvents.length)){return -1}debugMessage(kDebugScriptMangaer_SceneIndexFromSlideIndex," slideIndex: "+a);var b=this.script.navigatorEvents[a];return b.eventIndex},slideIndexFromSceneIndex:function(d){if((this.script==null)||(d<0)||(d>=this.script.eventTimelines.length)){return -1}debugMessage(kDebugScriptMangaer_SlideIndexFromSceneIndex,"sceneIndex: "+d);var a;debugMessage(kDebugScriptMangaer_SlideIndexFromSceneIndex,"  SlideIndex => SceneIndex");for(a=this.script.slideCount-1;a>0;a--){var c=this.script.navigatorEvents[a];var b=c.eventIndex;if(d>=b){debugMessage(kDebugScriptMangaer_SlideIndexFromSceneIndex,"  "+a+" => "+b+" - that's close enough!");return a}debugMessage(kDebugScriptMangaer_SlideIndexFromSceneIndex,"  "+a+" => "+b+" - too high, NEXT!")}debugMessage(kDebugScriptMangaer_SlideIndexFromSceneIndex,"  must be first slide!");return a}});
