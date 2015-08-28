#LoveAutoComp
An continouus Löve2D packaging application based upon [LoveComp](https://love2d.org/forums/viewtopic.php?f=5&t=78361&p=170932&hilit=linux+binary)

```
Usage: lovec Input_dir Output_dir bundle_identifier bundle_name [-w]

input_dir: the LOVE project

output_dir: the directory to put builds in

bundle_identifier: the macosx bundle identifier ex: com.example.MyGame

bundle_name: The macosx bundle name, aka, the name of your game ex: MyGame

The -w switch watches the input folder for changes and recompiles on every change.
```

###Tests###

```
simple_compile.bat - One-Time compilitation
watch_compile - Continouus compilation on every change
```
