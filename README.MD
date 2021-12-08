# CocoaInput
Support IME - Input Method Editor(Japanese,Chinese,Korean) in Minecraft on Multi-platforms(macOS, Windows, X11).

## Compiling
1. Clone git repository
```Bash
git clone https://github.com/Axeryok/CocoaInput.git
cd CocoaInput
```
2. Compile native library<br />
CocoaInput needs native-platform libraries to work. 
To compile them, run one of below scripts which is your platform.
    - build_lib_for_mac.sh - For macOS
    - build_lib_for_x11.sh - For Linux
    - build_lib_for_win.sh - For Windows
    - (build_lib_all.sh - For all platform, You should create remote_build.sh to build macOS lib remotely.)
3. Compile Forge Mod<br />
Type below command.Forge mod will be located in "fabric/build/libs".
```Bash
./gradlew build
```
4. Compile Fabric Mod<br />
Type below command.Fabric mod will be located in "fabric/build/libs".
```Bash
cd fabric
./gradlew build
```

## Installing
CocoaInput official binaries has been distributed on https://www.curseforge.com/minecraft/mc-mods/cocoainput .
Get jar from above URL or trying ”Compiling" task.
Place it in your mods directory.

CocoaInput requires MinecraftForge or Fabric.

This mod uses Java Native Access (Apache Licence2) and binuary for Minecraft1.7.10 contains it.

## License
Minecraft Mod Public License Japanese Transration
