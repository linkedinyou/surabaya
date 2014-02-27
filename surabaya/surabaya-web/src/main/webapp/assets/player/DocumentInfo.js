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
var kIsPublicViewer=window.location.hostname.indexOf("public")==0;var DocumentInfo=Class.create({initialize:function(b,c,d,a){this.authorId=b;this.documentName=c;this.username=d;this.password=a;if(kIsPublicViewer){this.documentURL="/iw/"+b+"/"+encodeURIComponent(c)}else{this.documentURL="/iw/"+b+"/.iWork/Share/"+encodeURIComponent(c)}},getInfo:function(b){b=$H({method:"get",sanitizeJSON:true}).merge(b||{}).toObject();var a,d;if(kIsPublicViewer){a=this.documentURL+"/metadata.json?salt="+(new Date().getTime());d=this.handleJSON}else{if(this.username&&this.password){b.requestHeaders={"X-Heckler-Username":this.username,"X-Heckler-Remember":0,"X-Heckler-Scope":"Reviewer","X-Heckler-Password":this.password}}else{b.requestHeaders={}}b.requestHeaders.Depth="2";a=this.documentURL+"?webdav-method=PROPFIND";d=this.handleXML}b.onSuccess=d.bind(this,b.onSuccess);var c=new Ajax.Request(a,b)},handleJSON:function(b,a){b(this.parseJSON(a.responseJSON))},handleXML:function(e,b){var a=$A(),d=getHecklerElementsByTagName(b.responseXML,"Heckler.document");if(d&&d.length){d[0].normalize();a=a.concat(d[0].firstChild.nodeValue.evalJSON(true))}d=getHecklerElementsByTagName(b.responseXML,"Heckler.user");for(var c=0;c<d.length;c++){d[c].normalize();a=a.concat(d[c].firstChild.nodeValue.evalJSON(true))}e(this.parseJSON(a))},parseJSON:function(b){var a=0,c,d;for(var a=0;a<b.length;a++){switch(b[a].type){case"Document":d=b[a];break;case"User":if(b[a].role==1){c=b[a]}break}}return{authorImage:this.documentURL+(kIsPublicViewer?"":"/Resources")+"/author.jpg?ts="+d.publishDate,authorFirstName:c.firstName,authorLastName:c.lastName,authorEmail:c.email,date:new Date(d.publishDate*1000).format(CoreDocs.loc("MMM d, y h:mm a","date.format() parameters for document publish date")),documentTitle:d.title}}});
