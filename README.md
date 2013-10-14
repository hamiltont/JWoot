JWoot
=====

A Java/Android simple JSON based parser for woot.com v2 api. Deals with the fact that the Woot.com returned data is huge (e.g. 1-2MB, so more than most Android apps can load on the heap) by doing parsing online. 


Using
-----


This project requires [JSON.simple](https://code.google.com/p/json-simple/), which you can just download, drop inside the libs/ directory of your project, and add to the classpath. To use JWoot inside of Android, do this: 

````java
WootEventListener wel = new WootEventListener() {
  @Override
  public void onWootEvent(WootEvent event) {    
      // event.getTitle();
  }
};

WootApi api = new WootApi("<my-api-key>");
api.fetch(WootApi.WOOT_URL_NORM, wel);
```

The calls to onWootEvent will by default use the 
main thread, so it's ok to modify any UI elements. 
Note that you can expect WootEvents to take up a 
lot of memory, so it makes sense to use them and 
forget them, or to null any fields you don't plan 
on using later. 


Operation
---------

This main class is the WootEventParser, which has keys for all of the json object. 'Keys' is a generic 
term for any of the JSON keys, and special keys
that I use to denote certain places in the JSON, 
such as WOOT_ITEM is a key that denotes the current
JSON object we are parsing is an WootItem object. 

The WootEventParser keeps a stack of currently 
active keys so that it can understand it's place
in the JSON, and any non-understood keys are 
ignored. 

API Coverage
------------

This only covers the core bits of the API, but it 
shouldn't be too hard to extend WootEventParser to 
cover them all. Note that this library is LGPL, so 
you are required to share back any improvements you
have made so that the rest of the community can 
benefit. 