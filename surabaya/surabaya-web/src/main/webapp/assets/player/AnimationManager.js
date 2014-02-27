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
var kKeyframeRule=window.CSSRule.WEBKIT_KEYFRAMES_RULE;var AnimationManager=Class.create({initialize:function(){this.styleSheet=document.styleSheets.item(0);this.ruleNumber=0;this.createdAnimations=new Object()},findAnimation:function(b){debugMessage(kDebugAnimationManager_FindAnimation,"looking for '"+b+"', there are "+this.styleSheet.cssRules.length+" rules on the sheet");var d;var a=this.styleSheet.cssRules.length;for(d=0;d<a;d++){debugMessage(kDebugAnimationManager_FindAnimation,"- "+d+": "+this.styleSheet.cssRules[d].name);var c=this.styleSheet.cssRules[d];if((c.type==kKeyframeRule)&&(c.name==b)){debugMessage(kDebugAnimationManager_FindAnimation,"- found it!");return c}}debugMessage(kDebugAnimationManager,kDebugFindAnimation,"- not found.");return null},createAnimation:function(a){debugMessage(kDebugAnimationManager_CreateAnimation,a);this.styleSheet.insertRule("@-webkit-keyframes "+a+" {}");return this.findAnimation(a)},deleteAllAnimations:function(){debugMessage(kDebugAnimationManager_DeleteAllAnimations);var c;var a=this.styleSheet.cssRules.length;for(c=0;c<a;c++){var b=this.styleSheet.cssRules[c];if(b&&b.type==kKeyframeRule){debugMessage(kDebugAnimationManager_DeleteAllAnimations,"- "+c+": "+b.name);this.styleSheet.deleteRule(b.name)}}this.createdAnimations=new Object()},markAnimationsCreated:function(a){this.createdAnimations[a]=true},animationsCreated:function(a){return this.createdAnimations[a]}});
